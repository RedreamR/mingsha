package team.rainfall.mingsha;

import com.badlogic.gdx.files.FileHandle;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.DirectoryStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

/** Compact, indexed container for numbered files in a map data/provinces directory. */
public final class ProvincePack {
    private static final byte[] MAGIC = new byte[]{'M', 'S', 'H', 'P', 'K', '0', '0', '1'};
    private static final byte[] TEXTURE_MAGIC = new byte[]{'M', 'S', 'H', 'T', 'X', '0', '0', '1'};
    private static final int VERSION = 1;
    private static final Map<String, Reader> CACHE = new ConcurrentHashMap<>();
    private static final Map<String, TextureReader> TEXTURE_CACHE = new ConcurrentHashMap<>();

    private ProvincePack() {
    }

    public static byte[] read(FileHandle packFile, int provinceId) throws IOException {
        if (packFile == null || !packFile.exists()) {
            return null;
        }
        String key = packFile.path();
        Reader reader = CACHE.get(key);
        if (reader == null) {
            reader = new Reader(packFile.readBytes());
            Reader previous = CACHE.putIfAbsent(key, reader);
            if (previous != null) {
                reader = previous;
            }
        }
        return reader.read(provinceId);
    }

    public static void clearCache() {
        CACHE.clear();
        TEXTURE_CACHE.clear();
    }

    /** Reads one CIM texture from a Mingsha texture pack. */
    public static byte[] readTexture(FileHandle packFile, int scale, int provinceId) throws IOException {
        if (packFile == null || !packFile.exists()) {
            return null;
        }
        String key = packFile.path();
        TextureReader reader = TEXTURE_CACHE.get(key);
        if (reader == null) {
            reader = new TextureReader(packFile.readBytes());
            TextureReader previous = TEXTURE_CACHE.putIfAbsent(key, reader);
            if (previous != null) {
                reader = previous;
            }
        }
        return reader.read(scale, provinceId);
    }

    /** Decodes a zlib-compressed CIM payload without creating a temporary file. */
    public static com.badlogic.gdx.graphics.Pixmap readCim(byte[] bytes) throws IOException {
        try (DataInputStream in = new DataInputStream(new InflaterInputStream(
                new ByteArrayInputStream(bytes)))) {
            int width = in.readInt();
            int height = in.readInt();
            int format = in.readInt();
            if (width <= 0 || height <= 0 || width > 32768 || height > 32768) {
                throw new IOException("Invalid CIM dimensions: " + width + "x" + height);
            }
            com.badlogic.gdx.graphics.Pixmap pixmap = new com.badlogic.gdx.graphics.Pixmap(width, height,
                    com.badlogic.gdx.graphics.Pixmap.Format.fromGdx2DPixmapFormat(format));
            java.nio.ByteBuffer pixels = pixmap.getPixels();
            pixels.position(0);
            pixels.limit(pixels.capacity());
            byte[] buffer = new byte[32000];
            int total = 0;
            int read;
            while ((read = in.read(buffer)) > 0) {
                if (read > pixels.remaining()) {
                    pixmap.dispose();
                    throw new IOException("CIM pixel data exceeds buffer");
                }
                pixels.put(buffer, 0, read);
                total += read;
            }
            if (total != pixels.capacity()) {
                pixmap.dispose();
                throw new IOException("CIM pixel data is truncated");
            }
            pixels.position(0);
            pixels.limit(pixels.capacity());
            return pixmap;
        }
    }

    /** Builds data/provinces.pack from files whose names are decimal province IDs. */
    public static void packDirectory(Path sourceDirectory, Path outputFile) throws IOException {
        List<EntryData> entries = new ArrayList<>();
        try (DirectoryStream<Path> paths = Files.newDirectoryStream(sourceDirectory)) {
            for (Path path : paths) {
                if (!Files.isRegularFile(path)) {
                    continue;
                }
                try {
                    int id = Integer.parseInt(path.getFileName().toString());
                    byte[] raw = Files.readAllBytes(path);
                    entries.add(new EntryData(id, raw, deflate(raw)));
                } catch (NumberFormatException ignored) {
                    // Ignore backups and editor sidecar files.
                } catch (IOException e) {
                    throw new PackRuntimeException(e);
                }
            }
        } catch (PackRuntimeException e) {
            throw e.getCause();
        }
        entries.sort(Comparator.comparingInt(entry -> entry.id));
        Files.createDirectories(outputFile.toAbsolutePath().getParent());
        long headerSize = MAGIC.length + 4L + 4L + entries.size() * (4L + 8L + 4L + 4L);
        long offset = headerSize;
        try (DataOutputStream out = new DataOutputStream(Files.newOutputStream(outputFile))) {
            out.write(MAGIC);
            out.writeInt(VERSION);
            out.writeInt(entries.size());
            for (EntryData entry : entries) {
                out.writeInt(entry.id);
                out.writeLong(offset);
                out.writeInt(entry.compressed.length);
                out.writeInt(entry.raw.length);
                offset += entry.compressed.length;
            }
            for (EntryData entry : entries) {
                out.write(entry.compressed);
            }
        }
    }

    /** Builds a single texture pack from data/scales/provinces/<scale>/<provinceId>. */
    public static void packTextureDirectory(Path sourceDirectory, Path outputFile) throws IOException {
        List<TextureEntryData> entries = new ArrayList<>();
        try (DirectoryStream<Path> scales = Files.newDirectoryStream(sourceDirectory)) {
            for (Path scaleDirectory : scales) {
                if (!Files.isDirectory(scaleDirectory)) {
                    continue;
                }
                int scale;
                try {
                    scale = Integer.parseInt(scaleDirectory.getFileName().toString());
                } catch (NumberFormatException ignored) {
                    continue;
                }
                try (DirectoryStream<Path> files = Files.newDirectoryStream(scaleDirectory)) {
                    for (Path file : files) {
                        if (!Files.isRegularFile(file)) {
                            continue;
                        }
                        int provinceId;
                        try {
                            provinceId = Integer.parseInt(file.getFileName().toString());
                        } catch (NumberFormatException ignored) {
                            continue;
                        }
                        byte[] raw = Files.readAllBytes(file);
                        entries.add(new TextureEntryData(scale, provinceId, raw, deflate(raw)));
                    }
                }
            }
        }
        entries.sort(Comparator.comparingInt((TextureEntryData entry) -> entry.scale)
                .thenComparingInt(entry -> entry.provinceId));
        Files.createDirectories(outputFile.toAbsolutePath().getParent());
        long headerSize = TEXTURE_MAGIC.length + 4L + 4L + entries.size() * (4L + 4L + 8L + 4L + 4L);
        long offset = headerSize;
        try (DataOutputStream out = new DataOutputStream(Files.newOutputStream(outputFile))) {
            out.write(TEXTURE_MAGIC);
            out.writeInt(VERSION);
            out.writeInt(entries.size());
            for (TextureEntryData entry : entries) {
                out.writeInt(entry.scale);
                out.writeInt(entry.provinceId);
                out.writeLong(offset);
                out.writeInt(entry.compressed.length);
                out.writeInt(entry.raw.length);
                offset += entry.compressed.length;
            }
            for (TextureEntryData entry : entries) {
                out.write(entry.compressed);
            }
        }
    }

    private static byte[] deflate(byte[] raw) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (DeflaterOutputStream out = new DeflaterOutputStream(bytes, new Deflater(Deflater.BEST_COMPRESSION))) {
            out.write(raw);
        }
        return bytes.toByteArray();
    }

    private static final class TextureEntryData {
        private final int scale;
        private final int provinceId;
        private final byte[] raw;
        private final byte[] compressed;

        private TextureEntryData(int scale, int provinceId, byte[] raw, byte[] compressed) {
            this.scale = scale;
            this.provinceId = provinceId;
            this.raw = raw;
            this.compressed = compressed;
        }
    }

    private static final class TextureReader {
        private final byte[] data;
        private final Map<Long, TextureIndex> index = new ConcurrentHashMap<>();

        private TextureReader(byte[] data) throws IOException {
            this.data = data;
            try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(data))) {
                byte[] magic = new byte[TEXTURE_MAGIC.length];
                in.readFully(magic);
                for (int i = 0; i < TEXTURE_MAGIC.length; i++) {
                    if (magic[i] != TEXTURE_MAGIC[i]) {
                        throw new IOException("Invalid Mingsha texture pack magic");
                    }
                }
                if (in.readInt() != VERSION) {
                    throw new IOException("Unsupported Mingsha texture pack version");
                }
                int count = in.readInt();
                if (count < 0 || count > 10_000_000) {
                    throw new IOException("Invalid texture pack entry count");
                }
                for (int i = 0; i < count; i++) {
                    int scale = in.readInt();
                    int provinceId = in.readInt();
                    long offset = in.readLong();
                    int compressedLength = in.readInt();
                    int rawLength = in.readInt();
                    if (scale < 0 || provinceId < 0 || offset < 0 || compressedLength < 0 || rawLength < 0
                            || offset > data.length - compressedLength) {
                        throw new IOException("Invalid texture pack entry");
                    }
                    index.put(textureKey(scale, provinceId),
                            new TextureIndex(offset, compressedLength, rawLength));
                }
            }
        }

        private byte[] read(int scale, int provinceId) throws IOException {
            TextureIndex item = index.get(textureKey(scale, provinceId));
            if (item == null) {
                return null;
            }
            if (item.rawLength > 256 * 1024 * 1024) {
                throw new IOException("Texture entry is too large");
            }
            ByteArrayInputStream source = new ByteArrayInputStream(data, (int) item.offset, item.compressedLength);
            try (InputStream in = new InflaterInputStream(source);
                 ByteArrayOutputStream out = new ByteArrayOutputStream(item.rawLength)) {
                byte[] buffer = new byte[8192];
                int total = 0;
                int read;
                while ((read = in.read(buffer)) != -1) {
                    total += read;
                    if (total > item.rawLength) {
                        throw new IOException("Texture entry exceeds declared size");
                    }
                    out.write(buffer, 0, read);
                }
                if (total != item.rawLength) {
                    throw new IOException("Texture entry is truncated");
                }
                return out.toByteArray();
            }
        }
    }

    private static long textureKey(int scale, int provinceId) {
        return ((long) scale << 32) ^ (provinceId & 0xffffffffL);
    }

    private static final class TextureIndex {
        private final long offset;
        private final int compressedLength;
        private final int rawLength;

        private TextureIndex(long offset, int compressedLength, int rawLength) {
            this.offset = offset;
            this.compressedLength = compressedLength;
            this.rawLength = rawLength;
        }
    }

    private static final class EntryData {
        private final int id;
        private final byte[] raw;
        private final byte[] compressed;

        private EntryData(int id, byte[] raw, byte[] compressed) {
            this.id = id;
            this.raw = raw;
            this.compressed = compressed;
        }
    }

    private static final class Reader {
        private final byte[] data;
        private final Map<Integer, Index> index = new ConcurrentHashMap<>();

        private Reader(byte[] data) throws IOException {
            this.data = data;
            try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(data))) {
                byte[] magic = new byte[MAGIC.length];
                in.readFully(magic);
                for (int i = 0; i < MAGIC.length; i++) {
                    if (magic[i] != MAGIC[i]) {
                        throw new IOException("Invalid province pack magic");
                    }
                }
                if (in.readInt() != VERSION) {
                    throw new IOException("Unsupported province pack version");
                }
                int count = in.readInt();
                if (count < 0 || count > 10_000_000) {
                    throw new IOException("Invalid province pack entry count");
                }
                for (int i = 0; i < count; i++) {
                    int id = in.readInt();
                    long offset = in.readLong();
                    int compressedLength = in.readInt();
                    int rawLength = in.readInt();
                    if (offset < 0 || compressedLength < 0 || rawLength < 0 || offset > data.length - compressedLength) {
                        throw new IOException("Invalid province pack entry");
                    }
                    index.put(id, new Index(offset, compressedLength, rawLength));
                }
            }
        }

        private byte[] read(int provinceId) throws IOException {
            Index item = index.get(provinceId);
            if (item == null) {
                return null;
            }
            if (item.rawLength > 256 * 1024 * 1024) {
                throw new IOException("Province entry is too large");
            }
            ByteArrayInputStream source = new ByteArrayInputStream(data, (int) item.offset, item.compressedLength);
            try (InputStream in = new InflaterInputStream(source); ByteArrayOutputStream out = new ByteArrayOutputStream(item.rawLength)) {
                byte[] buffer = new byte[8192];
                int total = 0;
                int read;
                while ((read = in.read(buffer)) != -1) {
                    total += read;
                    if (total > item.rawLength) {
                        throw new IOException("Province entry exceeds declared size");
                    }
                    out.write(buffer, 0, read);
                }
                if (total != item.rawLength) {
                    throw new IOException("Province entry is truncated");
                }
                return out.toByteArray();
            }
        }
    }

    private static final class Index {
        private final long offset;
        private final int compressedLength;
        private final int rawLength;

        private Index(long offset, int compressedLength, int rawLength) {
            this.offset = offset;
            this.compressedLength = compressedLength;
            this.rawLength = rawLength;
        }
    }

    private static final class PackRuntimeException extends RuntimeException {
        private PackRuntimeException(IOException cause) {
            super(cause);
        }

        @Override
        public synchronized IOException getCause() {
            return (IOException) super.getCause();
        }
    }
}

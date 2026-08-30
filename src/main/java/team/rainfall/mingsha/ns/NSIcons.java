package team.rainfall.mingsha.ns;

import age.of.civilizations2.jakowski.lukasz.CFG;
import age.of.civilizations2.jakowski.lukasz.IMGManager;
import age.of.civilizations2.jakowski.lukasz.Image;
import age.of.civilizations2.jakowski.lukasz.Files.FileManager;
import com.badlogic.gdx.files.FileHandle;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Icons of the national spirits: one {@code gfx/ns/&lt;id&gt;.png} per definition,
 * named after the id so a spirit needs no icon field and dropping in a file is all
 * it takes.
 * <p>
 * Icons are not registered with {@link IMGManager#addIMG} — that appends to the
 * global image list, which is indexed by the {@code Images} constants and never
 * shrinks, so a reload would leak a slot every time. {@link IMGManager#loadImage}
 * hands back a standalone {@code Image} instead, and this class owns it.
 * <p>
 * Lookups are cached per id <em>including the miss</em>, both because
 * {@code FileManager.loadFile} walks every mod folder and because the bar asks for
 * every visible icon on every frame.
 */
public final class NSIcons {

    public static final String DIR = "gfx/ns/";
    public static final String EXT = ".png";

    /** Cache entry meaning "this id ships no icon"; distinct from "not looked up yet". */
    private static final Object ABSENT = new Object();

    private static final Map<String, Object> CACHE = new ConcurrentHashMap<String, Object>();

    private NSIcons() {
    }

    public static String pathOf(String nID) {
        return DIR + (nID == null ? "" : nID) + EXT;
    }

    /**
     * The icon of one spirit, or null when it ships none — callers draw their own
     * placeholder in that case.
     * <p>
     * Creating the texture touches GL state, so this may only be called from the
     * render thread. Every draw path already is, and so is menu construction.
     */
    public static Image get(String nID) {
        if (nID == null || nID.isEmpty()) {
            return null;
        }

        Object tCached = CACHE.get(nID);
        if (tCached == null) {
            tCached = load(nID);
            Object tPrevious = CACHE.putIfAbsent(nID, tCached);
            if (tPrevious != null) {
                tCached = tPrevious;
            }
        }
        return tCached == ABSENT ? null : (Image)tCached;
    }

    public static boolean has(String nID) {
        return get(nID) != null;
    }

    /**
     * Probes for the file before handing the path to {@code IMGManager}, whose
     * {@code loadTexture} answers a missing file with a whole
     * {@code UI/imageNotFound.png} texture — that would land in this cache and be
     * drawn as though it were the icon.
     * <p>
     * The probe goes through {@code FileManager.loadFile}, the same way
     * {@link NSDefs} reads the catalogue, so a mod folder that ships
     * {@code national_spirits.json} ships the icons next to it.
     */
    private static Object load(String nID) {
        try {
            String tPath = pathOf(nID);
            FileHandle tFile = FileManager.loadFile(tPath);
            if (tFile == null || !tFile.exists()) {
                return ABSENT;
            }
            Image tImage = IMGManager.loadImage(tPath);
            return tImage == null ? ABSENT : tImage;
        } catch (Exception e) {
            CFG.exceptionStack(e);
            return ABSENT;
        }
    }

    /**
     * Drops one id, disposing its texture, so the next {@link #get(String)} reads the
     * file again. Safe for an id that never had an icon.
     */
    public static void forget(String nID) {
        if (nID == null || nID.isEmpty()) {
            return;
        }
        dispose(CACHE.remove(nID));
    }

    /** Drops every icon. Must run on the render thread, as it disposes textures. */
    public static void clearCache() {
        for (Object tCached : CACHE.values()) {
            dispose(tCached);
        }
        CACHE.clear();
    }

    private static void dispose(Object nCached) {
        if (nCached == null || nCached == ABSENT) {
            return;
        }
        try {
            ((Image)nCached).dispose();
        } catch (Exception e) {
            CFG.exceptionStack(e);
        }
    }
}

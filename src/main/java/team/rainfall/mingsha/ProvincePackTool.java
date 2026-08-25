package team.rainfall.mingsha;

import java.nio.file.Path;
import java.nio.file.Paths;

/** Command-line packer for Mingsha province data and CIM texture formats. */
public final class ProvincePackTool {
    private ProvincePackTool() {
    }

    public static void main(String[] args) throws Exception {
        boolean textures = args.length == 3 && "--textures".equals(args[0]);
        if (args.length != 2 && !textures) {
            System.err.println("Usage: ProvincePackTool <data/provinces> <output>");
            System.err.println("   or: ProvincePackTool --textures <data/scales/provinces> <output>");
            System.exit(2);
        }
        int first = textures ? 1 : 0;
        Path source = Paths.get(args[first]);
        Path output = Paths.get(args[first + 1]);
        if (textures) {
            ProvincePack.packTextureDirectory(source, output);
        } else {
            ProvincePack.packDirectory(source, output);
        }
        System.out.println("Wrote province pack: " + output.toAbsolutePath());
    }
}

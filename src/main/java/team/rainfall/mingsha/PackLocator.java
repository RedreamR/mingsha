package team.rainfall.mingsha;

import age.of.civilizations2.jakowski.lukasz.Files.FileManager;
import com.badlogic.gdx.files.FileHandle;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import team.rainfall.finality.FinalityLogger;

/**
 * Resolves Mingsha pack files once per path instead of once per province.
 *
 * <p>{@code FileManager.loadFile} walks every local mod folder and every installed workshop item,
 * calling {@code exists()} on each, and only falls back to the game folder afterwards. Doing that
 * per province costs tens of thousands of stat calls per map load, so the answer is cached here -
 * including the negative answer, which is what unpacked maps hit.
 */
public final class PackLocator {
    private static final Object ABSENT = new Object();
    private static final Map<String, Object> RESOLVED = new ConcurrentHashMap<>();

    private PackLocator() {
    }

    /** Returns the pack for {@code packPath}, or null when this map ships no pack. */
    public static FileHandle find(String packPath) {
        Object cached = RESOLVED.get(packPath);
        if (cached == null) {
            cached = resolve(packPath);
            Object previous = RESOLVED.putIfAbsent(packPath, cached);
            if (previous != null) {
                cached = previous;
            }
        }
        return cached == ABSENT ? null : (FileHandle)cached;
    }

    private static Object resolve(String packPath) {
        FileHandle handle;
        try {
            handle = FileManager.loadFile(packPath);
            if (handle == null || !handle.exists()) {
                return ABSENT;
            }
        } catch (Exception ex) {
            FinalityLogger.error("[Mingsha] Pack lookup failed: " + packPath, ex);
            return ABSENT;
        }
        FinalityLogger.info("[Mingsha] Pack found: " + handle.path());
        return handle;
    }

    /** Forgets every resolved path; call after packing a map at runtime. */
    public static void clearCache() {
        RESOLVED.clear();
    }
}

package team.rainfall.mingsha.sound;

import age.of.civilizations2.jakowski.lukasz.CFG;
import age.of.civilizations2.jakowski.lukasz.SFXManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import team.rainfall.mingsha.config.MingshaConfig;
import team.rainfall.mingsha.config.UnitVoiceConfigData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class UnitVoiceManager {
    public enum VoiceType {
        IDLE("idle"),
        MOVE_OUT("move_out"),
        NEUTRAL_COMBAT("neutral_combat"),
        POSITIVE_COMBAT("positive_combat"),
        RETREAT("retreat");

        public final String folder;

        VoiceType(String folder) {
            this.folder = folder;
        }
    }

    private static final String VO_ROOT = "sounds/vo/";

    private static boolean initialized = false;
    private static boolean enabled = true;
    private static float voiceVolume = 1.0f;
    private static long globalCooldownMs = 2000L;
    private static long categoryCooldownMs = 4000L;

    private static final Map<String, String> tagToFolder = new HashMap<String, String>();
    private static final Map<String, String> categoryFallback = new HashMap<String, String>();
    private static final Map<String, List<FileHandle>> voiceIndex = new HashMap<String, List<FileHandle>>();
    private static final Map<String, Integer> lastPlayedIndex = new HashMap<String, Integer>();
    private static final Map<String, Long> lastPlayedByCategory = new HashMap<String, Long>();
    private static long lastPlayedGlobal = 0L;
    private static final Random random = new Random();
    private static Music currentVoice = null;

    private UnitVoiceManager() {
    }

    private static synchronized void init() {
        if (initialized) {
            return;
        }
        initialized = true;
        try {
            System.out.println("[UnitVoice] init, vo root = " + Gdx.files.local(VO_ROOT).file().getAbsolutePath());
            loadConfig();
            buildIndex();
            System.out.println("[UnitVoice] init done, enabled=" + enabled + ", categories=" + voiceIndex.keySet());
        } catch (Exception e) {
            enabled = false;
            System.out.println("[UnitVoice] init failed: " + e);
            e.printStackTrace();
        }
    }

    private static void loadConfig() {
        MingshaConfig.ensureInit();
        UnitVoiceConfigData cfg = MingshaConfig.unitVoice;
        if (cfg == null) {
            return;
        }
        enabled = cfg.enabled;
        voiceVolume = cfg.voiceVolume;
        globalCooldownMs = cfg.globalCooldownMs;
        categoryCooldownMs = cfg.categoryCooldownMs;
        if (cfg.tagMapping != null) {
            for (Map.Entry<String, String> e : cfg.tagMapping.entrySet()) {
                if (e.getKey() != null) {
                    tagToFolder.put(e.getKey().toLowerCase(), e.getValue() == null ? "" : e.getValue().toLowerCase());
                }
            }
        }
        if (cfg.categoryFallback != null) {
            for (Map.Entry<String, String> e : cfg.categoryFallback.entrySet()) {
                if (e.getKey() != null) {
                    categoryFallback.put(e.getKey().toLowerCase(), e.getValue() == null ? "" : e.getValue().toLowerCase());
                }
            }
        }
    }

    private static void buildIndex() {
        FileHandle rootDir = Gdx.files.local(VO_ROOT);
        if (!rootDir.exists() || !rootDir.isDirectory()) {
            enabled = false;
            return;
        }
        for (FileHandle civDir : rootDir.list()) {
            if (!civDir.isDirectory()) {
                continue;
            }
            for (FileHandle catDir : civDir.list()) {
                if (!catDir.isDirectory()) {
                    continue;
                }
                List<FileHandle> files = new ArrayList<FileHandle>();
                for (FileHandle f : catDir.list()) {
                    String ext = f.extension().toLowerCase();
                    if (ext.equals("ogg") || ext.equals("wav") || ext.equals("mp3")) {
                        files.add(f);
                    }
                }
                if (!files.isEmpty()) {
                    voiceIndex.put(civDir.name().toLowerCase() + "/" + catDir.name().toLowerCase(), files);
                }
            }
        }
        if (voiceIndex.isEmpty()) {
            enabled = false;
        }
    }

    private static String resolveFolder(String civTag) {
        if (civTag == null) {
            return null;
        }
        String tag = civTag.toLowerCase();
        String mapped = tagToFolder.get(tag);
        if (mapped != null) {
            return mapped;
        }
        for (Map.Entry<String, String> e : tagToFolder.entrySet()) {
            String key = e.getKey();
            if (key.endsWith("*") && tag.startsWith(key.substring(0, key.length() - 1))) {
                return e.getValue();
            }
        }
        for (VoiceType t : VoiceType.values()) {
            if (voiceIndex.containsKey(tag + "/" + t.folder)) {
                return tag;
            }
        }
        return null;
    }

    private static List<FileHandle> resolveFiles(String folder, VoiceType type) {
        List<FileHandle> files = voiceIndex.get(folder + "/" + type.folder);
        if (files != null) {
            return files;
        }
        String fallback = categoryFallback.get(type.folder);
        if (fallback != null) {
            return voiceIndex.get(folder + "/" + fallback);
        }
        return null;
    }

    public static void play(String civTag, VoiceType type) {
        try {
            init();
            if (!enabled) {
                return;
            }
            long now = System.currentTimeMillis();
            if (currentVoice != null && currentVoice.isPlaying()) {
                return;
            }
            if (now - lastPlayedGlobal < globalCooldownMs) {
                return;
            }
            Long lastCat = lastPlayedByCategory.get(type.folder);
            if (lastCat != null && now - lastCat < categoryCooldownMs) {
                return;
            }
            String folder = resolveFolder(civTag);
            System.out.println("[UnitVoice] play tag=" + civTag + " type=" + type + " folder=" + folder);
            if (folder == null) {
                return;
            }
            List<FileHandle> files = resolveFiles(folder, type);
            if (files == null || files.isEmpty()) {
                return;
            }
            String indexKey = folder + "/" + type.folder;
            int idx = random.nextInt(files.size());
            Integer last = lastPlayedIndex.get(indexKey);
            if (files.size() > 1 && last != null && idx == last) {
                idx = (idx + 1 + random.nextInt(files.size() - 1)) % files.size();
            }
            lastPlayedIndex.put(indexKey, idx);
            FileHandle file = files.get(idx);
            float volume = CFG.SFXManager.getSoundsVolume() * CFG.SFXManager.getMasterVolume() * voiceVolume;
            if (volume <= 0.0f) {
                return;
            }
            if (currentVoice != null) {
                currentVoice.dispose();
                currentVoice = null;
            }
            System.out.println("[UnitVoice] playing " + file.path() + " volume=" + volume);
            currentVoice = Gdx.audio.newMusic(file);
            currentVoice.setVolume(volume);
            currentVoice.play();
            lastPlayedGlobal = now;
            lastPlayedByCategory.put(type.folder, now);
        } catch (Exception e) {
            System.out.println("[UnitVoice] play error: " + e);
            e.printStackTrace();
        }
    }

    private static boolean loggedFirstSfx = false;

    public static void onSfxPlayed(int soundID, float fPercOfVolume) {
        try {
            if (!loggedFirstSfx) {
                loggedFirstSfx = true;
                System.out.println("[UnitVoice] onSfxPlayed hook active, first id=" + soundID);
            }
            if (isMoveArmySound(soundID)) {
                if (CFG.chosenProvinceID >= 0) {
                    play(getPlayerCivTag(), VoiceType.MOVE_OUT);
                }
            } else if (soundID == SFXManager.SFX_CLICK && fPercOfVolume == SFXManager.PERC_VOLUME_SELECT_PROVINCE) {
                int provID = CFG.core.getActiveProvID();
                int civID = CFG.core.getPlayer(CFG.PLAYER_TURN_ID).getCivId();
                if (provID >= 0 && civID > 0 && CFG.core.getProv(provID).getArmyCivID1(civID) > 0) {
                    play(getPlayerCivTag(), VoiceType.IDLE);
                }
            }
        } catch (Exception e) {
            System.out.println("[UnitVoice] onSfxPlayed error: " + e);
            e.printStackTrace();
        }
    }

    private static boolean isMoveArmySound(int soundID) {
        return soundID == SFXManager.SFX_MOVE_ARMY
            || soundID == SFXManager.SFX_MOVE_ARMY2
            || soundID == SFXManager.SFX_MOVE_REGROUP
            || soundID == SFXManager.SFX_MOVE_ARMY_0
            || soundID == SFXManager.SFX_MOVE_ARMY_1
            || soundID == SFXManager.SFX_MOVE_ARMY_2
            || soundID == SFXManager.SFX_MOVE_ARMY_3
            || soundID == SFXManager.SFX_MOVE_ARMY_4;
    }

    private static String getPlayerCivTag() {
        try {
            int civID = CFG.core.getPlayer(CFG.PLAYER_TURN_ID).getCivId();
            if (civID <= 0) {
                return null;
            }
            return CFG.core.getCiv(civID).getCivTag();
        } catch (Exception e) {
            return null;
        }
    }
}

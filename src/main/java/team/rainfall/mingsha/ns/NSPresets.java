package team.rainfall.mingsha.ns;

import age.of.civilizations2.jakowski.lukasz.CFG;
import age.of.civilizations2.jakowski.lukasz.Event_GameData;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Which civilizations start a scenario already owning which national spirits.
 * <p>
 * The definitions themselves are global ({@link NSDefs}), but the starting layout
 * belongs to one scenario, so it is kept the way the counter system keeps its own
 * scenario data: inside a hidden, never-firing event, which the game's scenario
 * serializer saves and loads for free.
 * <p>
 * Payload lines in {@code Event_GameData.missionDesc}:
 * {@code <civTag><FIELD_SEP><id>,<id>,…}. A civilization with no spirits yet is a
 * legitimate line of its own ending right after the separator — it is how the editor
 * remembers a civilization the author has picked but not filled in, and dropping those
 * lines made picking a civilization look like it did nothing.
 */
public class NSPresets {

    public static final String MARKER_NAME = "$$MSNS_PRESET";
    private static final int NEVER_YEAR = 9999998;
    private static final char FIELD_SEP = '|';
    private static final char ENTRY_SEP = '\n';
    private static final char ID_SEP = ',';

    /** The spirits one civilization tag starts with. */
    public static class Preset {

        public String civTag = "";
        public List<String> ids = new ArrayList<String>();

        public Preset() {
        }

        public Preset(String nCivTag) {
            this.civTag = nCivTag == null ? "" : nCivTag;
        }
    }

    private static Event_GameData findPresetEvent() {
        try {
            for (int i = 0; i < CFG.eventsManager.events.lEvents.size(); i++) {
                Event_GameData tEvent = CFG.eventsManager.events.lEvents.get(i);
                if (tEvent != null && MARKER_NAME.equals(tEvent.getEventName())) {
                    return tEvent;
                }
            }
        } catch (Exception e) {
            CFG.exceptionStack(e);
        }
        return null;
    }

    private static Event_GameData getOrCreatePresetEvent() {
        Event_GameData tEvent = findPresetEvent();
        if (tEvent == null) {
            tEvent = new Event_GameData();
            tEvent.setEventName(MARKER_NAME);
            tEvent.setEventTag(MARKER_NAME);
            tEvent.setWasFired(true);
            tEvent.setWasTriedToRunOnce(true);
            tEvent.setEventDate_Since(1, 1, NEVER_YEAR);
            tEvent.setEventDate_Until(1, 1, NEVER_YEAR);
            tEvent.isMission = false;
            CFG.eventsManager.addEvent(tEvent);
        }
        return tEvent;
    }

    public static boolean isPresetEvent(Event_GameData nEvent) {
        return nEvent != null && MARKER_NAME.equals(nEvent.getEventName());
    }

    public static List<Preset> list() {
        List<Preset> tPresets = new ArrayList<Preset>();
        try {
            Event_GameData tEvent = findPresetEvent();
            if (tEvent == null || tEvent.missionDesc == null || tEvent.missionDesc.isEmpty()) {
                return tPresets;
            }
            String[] tEntries = splitOn(tEvent.missionDesc, ENTRY_SEP);
            for (String tEntry : tEntries) {
                if (tEntry == null || tEntry.isEmpty()) {
                    continue;
                }
                String[] tFields = splitOn(tEntry, FIELD_SEP);
                if (tFields.length < 1 || tFields[0].isEmpty()) {
                    continue;
                }
                Preset tPreset = new Preset(tFields[0]);
                if (tFields.length > 1 && !tFields[1].isEmpty()) {
                    String[] tIDs = splitOn(tFields[1], ID_SEP);
                    for (String tID : tIDs) {
                        if (tID != null && !tID.trim().isEmpty()) {
                            tPreset.ids.add(tID.trim());
                        }
                    }
                }
                tPresets.add(tPreset);
            }
        } catch (Exception e) {
            CFG.exceptionStack(e);
        }
        return tPresets;
    }

    /**
     * Splits on one literal separator character.
     * <p>
     * {@code String.split} takes a regular expression, and {@code FIELD_SEP} is
     * {@code '|'} — alternation, which matches the empty string between every
     * character and silently shredded every payload line into single letters. The
     * separators are quoted here rather than at each call so changing one cannot
     * reintroduce that.
     */
    private static String[] splitOn(String nText, char nSeparator) {
        return nText.split(Pattern.quote(String.valueOf(nSeparator)), -1);
    }

    public static void save(List<Preset> nPresets) {
        try {
            StringBuilder tBuilder = new StringBuilder();
            if (nPresets != null) {
                for (Preset tPreset : nPresets) {
                    // An empty spirit list is kept: it is a civilization the author has
                    // added and not filled in yet, and the editor has to be able to show
                    // it. Only a tagless entry is meaningless.
                    if (tPreset == null || tPreset.civTag == null || tPreset.civTag.isEmpty()) {
                        continue;
                    }
                    if (tBuilder.length() > 0) {
                        tBuilder.append(ENTRY_SEP);
                    }
                    tBuilder.append(sanitize(tPreset.civTag)).append(FIELD_SEP);
                    for (int i = 0; i < tPreset.ids.size(); i++) {
                        if (i > 0) {
                            tBuilder.append(ID_SEP);
                        }
                        tBuilder.append(sanitize(tPreset.ids.get(i)));
                    }
                }
            }
            getOrCreatePresetEvent().missionDesc = tBuilder.toString();
        } catch (Exception e) {
            CFG.exceptionStack(e);
        }
    }

    private static String sanitize(String nText) {
        if (nText == null) {
            return "";
        }
        return nText
            .replace(String.valueOf(FIELD_SEP), "")
            .replace(String.valueOf(ENTRY_SEP), "")
            .replace(String.valueOf(ID_SEP), "");
    }

    public static List<String> idsOf(String nCivTag) {
        for (Preset tPreset : list()) {
            if (tPreset.civTag.equals(nCivTag)) {
                return tPreset.ids;
            }
        }
        return new ArrayList<String>();
    }

    public static boolean hasCiv(String nCivTag) {
        if (nCivTag == null || nCivTag.isEmpty()) {
            return false;
        }
        for (Preset tPreset : list()) {
            if (tPreset.civTag.equals(nCivTag)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gives a civilization a row of its own, spirits or not.
     * <p>
     * Picking a civilization is a step the author takes before choosing anything for it,
     * so it has to be remembered on its own; otherwise backing out of the spirit list
     * loses the civilization and the pick looks like it never happened.
     *
     * @return true when the row was created by this call
     */
    public static boolean ensureCiv(String nCivTag) {
        if (nCivTag == null || nCivTag.isEmpty() || hasCiv(nCivTag)) {
            return false;
        }
        List<Preset> tPresets = list();
        tPresets.add(new Preset(nCivTag));
        save(tPresets);
        return true;
    }

    /** Drops a civilization's row entirely, spirits and all. */
    public static void removeCiv(String nCivTag) {
        if (nCivTag == null || nCivTag.isEmpty()) {
            return;
        }
        List<Preset> tPresets = list();
        for (int i = 0; i < tPresets.size(); i++) {
            if (tPresets.get(i).civTag.equals(nCivTag)) {
                tPresets.remove(i);
                save(tPresets);
                return;
            }
        }
    }

    /**
     * Replaces one civilization's starting spirits. An empty list leaves the
     * civilization in place with nothing selected — removing its last spirit is not the
     * same as removing the civilization, which is what {@link #removeCiv} is for.
     */
    public static void setIDsOf(String nCivTag, List<String> nIDs) {
        if (nCivTag == null || nCivTag.isEmpty()) {
            return;
        }
        List<Preset> tPresets = list();
        for (int i = 0; i < tPresets.size(); i++) {
            if (tPresets.get(i).civTag.equals(nCivTag)) {
                tPresets.remove(i);
                break;
            }
        }
        Preset tPreset = new Preset(nCivTag);
        if (nIDs != null) {
            tPreset.ids = new ArrayList<String>(nIDs);
        }
        tPresets.add(tPreset);
        save(tPresets);
    }

    /** Map id of a civilization tag, or -1 when this map has no such civilization. */
    public static int civIDOf(String nCivTag) {
        if (nCivTag == null || nCivTag.isEmpty()) {
            return -1;
        }
        for (int i = 0; i < CFG.core.getCivsSize(); i++) {
            try {
                if (nCivTag.equals(CFG.core.getCiv(i).getCivTag())) {
                    return i;
                }
            } catch (Exception ignored) {
            }
        }
        return -1;
    }

    /**
     * Name of the civilization a preset row belongs to, falling back to the raw tag —
     * a preset survives being edited on a map that no longer has that civilization,
     * and the editor has to be able to show and delete it either way.
     */
    public static String civNameOf(String nCivTag) {
        try {
            int tCivID = civIDOf(nCivTag);
            return tCivID > 0 ? CFG.core.getCiv(tCivID).getCivName() : nCivTag;
        } catch (Exception e) {
            return nCivTag == null ? "" : nCivTag;
        }
    }

    /**
     * Rewrites every mention of a definition id, or drops it when {@code nNewID} is
     * null — the cleanup {@link NSDefs#rename} and {@link NSDefs#delete} need so an
     * edited catalogue does not leave dangling rows in the starting-spirit table.
     * <p>
     * Bails out when the loaded scenario has no marker event: the catalogue is global
     * and can be edited with any scenario open, and writing here would otherwise
     * plant preset data in a scenario that never asked for any.
     */
    private static void replaceID(String nOldID, String nNewID) {
        if (nOldID == null || nOldID.isEmpty() || findPresetEvent() == null) {
            return;
        }
        try {
            List<Preset> tPresets = list();
            boolean tChanged = false;
            for (int i = 0; i < tPresets.size(); i++) {
                List<String> tIDs = tPresets.get(i).ids;
                for (int j = tIDs.size() - 1; j >= 0; j--) {
                    if (!nOldID.equals(tIDs.get(j))) {
                        continue;
                    }
                    // A rename onto an id the civilization already owns would duplicate
                    // it, which means nothing, so the old entry just goes away.
                    if (nNewID == null || nNewID.isEmpty() || tIDs.contains(nNewID)) {
                        tIDs.remove(j);
                    } else {
                        tIDs.set(j, nNewID);
                    }
                    tChanged = true;
                }
            }
            if (tChanged) {
                save(tPresets);
            }
        } catch (Exception e) {
            CFG.exceptionStack(e);
        }
    }

    /** Follows a definition rename into the starting-spirit table. */
    public static void renameSpirit(String nOldID, String nNewID) {
        replaceID(nOldID, nNewID);
    }

    /** Drops a deleted definition from the starting-spirit table. */
    public static void forgetSpirit(String nID) {
        replaceID(nID, null);
    }

    /**
     * Grants every preset to its civilization. Called once the scenario's civs and
     * events are both in place; {@code NSStore.grant} refuses duplicates, so running
     * this more than once for the same start is harmless.
     */
    public static void apply() {
        try {
            List<Preset> tPresets = list();
            for (int i = 0; i < tPresets.size(); i++) {
                int tCivID = civIDOf(tPresets.get(i).civTag);
                if (tCivID < 0) {
                    continue;
                }
                for (int j = 0; j < tPresets.get(i).ids.size(); j++) {
                    NSStore.grant(tCivID, tPresets.get(i).ids.get(j));
                }
            }
        } catch (Exception e) {
            CFG.exceptionStack(e);
        }
    }
}

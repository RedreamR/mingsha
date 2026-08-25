package team.rainfall.mingsha.counter;

import age.of.civilizations2.jakowski.lukasz.CFG;
import age.of.civilizations2.jakowski.lukasz.Event_GameData;

import java.util.ArrayList;
import java.util.List;

/**
 * Scenario-level counter definitions (name, initial value, localizable display name).
 * Stored inside a hidden, never-firing event in the scenario events file so they are
 * saved and loaded together with the scenario without changing the save format.
 */
public class CounterDefs {

    public static final String MARKER_NAME = "$$MSCTR_DEFS";
    private static final int NEVER_YEAR = 9999998;
    private static final char FIELD_SEP = '\u0001';
    private static final char ENTRY_SEP = '\n';

    private static Event_GameData findDefsEvent() {
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

    private static Event_GameData getOrCreateDefsEvent() {
        Event_GameData tEvent = findDefsEvent();
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

    public static boolean isDefsEvent(Event_GameData tEvent) {
        return tEvent != null && MARKER_NAME.equals(tEvent.getEventName());
    }

    public static List<CounterDef> list() {
        List<CounterDef> defs = new ArrayList<CounterDef>();
        try {
            Event_GameData tEvent = findDefsEvent();
            if (tEvent == null || tEvent.missionDesc == null || tEvent.missionDesc.isEmpty()) {
                return defs;
            }
            String[] entries = tEvent.missionDesc.split(String.valueOf(ENTRY_SEP));
            for (String entry : entries) {
                if (entry == null || entry.isEmpty()) {
                    continue;
                }
                String[] fields = entry.split(String.valueOf(FIELD_SEP), -1);
                if (fields.length < 1 || fields[0].isEmpty()) {
                    continue;
                }
                CounterDef def = new CounterDef();
                def.name = fields[0];
                if (fields.length > 1) {
                    try {
                        def.initialValue = Integer.parseInt(fields[1]);
                    } catch (NumberFormatException ignored) {
                    }
                }
                if (fields.length > 2) {
                    def.displayName = fields[2];
                }
                defs.add(def);
            }
        } catch (Exception e) {
            CFG.exceptionStack(e);
        }
        return defs;
    }

    public static void save(List<CounterDef> defs) {
        try {
            StringBuilder sb = new StringBuilder();
            if (defs != null) {
                for (CounterDef def : defs) {
                    if (def == null || def.name == null || def.name.isEmpty()) {
                        continue;
                    }
                    if (sb.length() > 0) {
                        sb.append(ENTRY_SEP);
                    }
                    sb.append(sanitize(def.name))
                        .append(FIELD_SEP)
                        .append(def.initialValue)
                        .append(FIELD_SEP)
                        .append(sanitize(def.displayName));
                }
            }
            getOrCreateDefsEvent().missionDesc = sb.toString();
        } catch (Exception e) {
            CFG.exceptionStack(e);
        }
    }

    private static String sanitize(String s) {
        if (s == null) {
            return "";
        }
        return s.replace(String.valueOf(FIELD_SEP), "").replace(String.valueOf(ENTRY_SEP), " ");
    }

    public static CounterDef find(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        for (CounterDef def : list()) {
            if (name.equals(def.name)) {
                return def;
            }
        }
        return null;
    }

    public static int initialOf(String name) {
        CounterDef def = find(name);
        return def != null ? def.initialValue : 0;
    }

    /**
     * Localized display name of a counter. Falls back to the internal name.
     * A display name starting with '#' is resolved as a language key, e.g.
     * "#Population" -> CFG.lang.get("Population").
     */
    public static String displayOf(String name) {
        try {
            CounterDef def = find(name);
            if (def == null || def.displayName == null || def.displayName.isEmpty()) {
                return name;
            }
            if (def.displayName.startsWith("#") && def.displayName.length() > 1) {
                return CFG.lang.get(def.displayName.substring(1));
            }
            return def.displayName;
        } catch (Exception e) {
            return name;
        }
    }

    public static String createNewCounter() {
        List<CounterDef> defs = list();
        int n = defs.size() + 1;
        String name = "counter_" + n;
        while (find(name) != null) {
            name = "counter_" + ++n;
        }
        defs.add(new CounterDef(name, 0, ""));
        save(defs);
        return name;
    }
}

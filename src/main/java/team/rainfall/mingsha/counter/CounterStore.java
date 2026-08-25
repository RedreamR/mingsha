package team.rainfall.mingsha.counter;

import age.of.civilizations2.jakowski.lukasz.CFG;
import age.of.civilizations2.jakowski.lukasz.Civilization;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CounterStore {

    public static int civIDByTag(String civTag) {
        if (civTag == null || civTag.isEmpty()) {
            return -1;
        }
        for (int i = 0; i < CFG.core.getCivsSize(); i++) {
            try {
                if (civTag.equals(CFG.core.getCiv(i).getCivTag())) {
                    return i;
                }
            } catch (Exception ignored) {
            }
        }
        return -1;
    }

    public static String stripDollar(String name) {
        return name != null && name.startsWith("$") ? name.substring(1) : name;
    }

    /**
     * Resolves a counter reference of the form "name" or "name:CIVTAG".
     * Returns int[]{targetCivID} name via out; implemented as helpers below.
     */
    public static String refName(String ref) {
        String name = stripDollar(ref);
        int sep = name.indexOf(':');
        return sep >= 0 ? name.substring(0, sep) : name;
    }

    public static int refCivID(String ref, int fallbackCivID) {
        String name = stripDollar(ref);
        int sep = name.indexOf(':');
        if (sep >= 0) {
            int id = civIDByTag(name.substring(sep + 1));
            if (id >= 0) {
                return id;
            }
        }
        return fallbackCivID;
    }

    private static List<String> variablesOf(int iCivID) {
        if (iCivID < 0 || iCivID >= CFG.core.getCivsSize()) {
            return null;
        }
        Civilization civ = CFG.core.getCiv(iCivID);
        if (civ == null) {
            return null;
        }
        if (civ.civGD.lEvents_DecisionsTaken == null) {
            civ.civGD.lEvents_DecisionsTaken = new ArrayList<String>();
        }
        return civ.civGD.lEvents_DecisionsTaken;
    }

    public static Counter getCounter(int iCivID, String ref) {
        int targetCivID = refCivID(ref, iCivID);
        String name = refName(ref);
        List<String> vars = variablesOf(targetCivID);
        if (vars == null || name == null || name.isEmpty()) {
            return null;
        }
        String prefix = Counter.entryPrefixOf(name);
        for (String s : vars) {
            if (s != null && s.startsWith(prefix)) {
                Counter counter = new Counter(s);
                counter.iCivID = targetCivID;
                return counter;
            }
        }
        return null;
    }

    public static int getValue(int iCivID, String ref) {
        Counter counter = getCounter(iCivID, ref);
        return counter != null ? counter.value : CounterDefs.initialOf(refName(ref));
    }

    public static boolean exists(int iCivID, String ref) {
        return getCounter(iCivID, ref) != null;
    }

    public static void setCounter(int iCivID, String ref, int value) {
        int targetCivID = refCivID(ref, iCivID);
        String name = refName(ref);
        List<String> vars = variablesOf(targetCivID);
        if (vars == null || name == null || name.isEmpty()) {
            return;
        }
        String prefix = Counter.entryPrefixOf(name);
        Iterator<String> iterator = vars.iterator();
        while (iterator.hasNext()) {
            String s = iterator.next();
            if (s != null && s.startsWith(prefix)) {
                iterator.remove();
            }
        }
        Counter counter = new Counter();
        counter.iCivID = targetCivID;
        counter.name = name;
        counter.value = value;
        vars.add(counter.getRaw());
    }

    /** Replaces $counterName tokens in a description with the civ's counter values. */
    public static String formatDesc(String desc, int iCivID) {
        try {
            if (desc == null || desc.indexOf('$') < 0) {
                return desc;
            }
            List<String> vars = variablesOf(iCivID);
            if (vars == null) {
                return desc;
            }
            for (String s : vars) {
                if (s != null && s.startsWith(Counter.PREFIX)) {
                    Counter counter = new Counter(s);
                    if (counter.name != null && !counter.name.isEmpty()) {
                        desc = desc.replace("$" + counter.name, String.valueOf(counter.value));
                    }
                }
            }
            if (desc.indexOf('$') >= 0) {
                for (CounterDef def : CounterDefs.list()) {
                    if (def.name != null && !def.name.isEmpty()) {
                        desc = desc.replace("$" + def.name, String.valueOf(def.initialValue));
                    }
                }
            }
            return desc;
        } catch (Exception e) {
            CFG.exceptionStack(e);
            return desc;
        }
    }
}

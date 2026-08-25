package team.rainfall.mingsha.counter;

import age.of.civilizations2.jakowski.lukasz.View;

/** Shared state for the counter picker / manager screens (SelectDecision view reuse). */
public class CounterUI {
    public static final int MODE_VANILLA = 0;
    public static final int MODE_PICK_COND = 1;
    public static final int MODE_PICK_OUT = 2;
    public static final int MODE_MANAGER = 3;
    public static final int MODE_EDIT = 4;

    public static int mode = MODE_VANILLA;
    public static View returnView = null;
    public static String filter = "";
    public static String editName = "";
    /** When true, picking a counter appends "$name" to the field instead of replacing it. */
    public static boolean pickAppend = false;

    public static void reset() {
        mode = MODE_VANILLA;
        returnView = null;
        filter = "";
        editName = "";
        pickAppend = false;
    }
}

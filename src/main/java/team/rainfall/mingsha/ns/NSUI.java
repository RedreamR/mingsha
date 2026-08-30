package team.rainfall.mingsha.ns;

import age.of.civilizations2.jakowski.lukasz.CFG;
import age.of.civilizations2.jakowski.lukasz.Menu;
import age.of.civilizations2.jakowski.lukasz.View;

/**
 * Shared UI state for the national spirit feature.
 * <p>
 * All mutable state lives here rather than in {@code MixinMenuManager}: Luminosity's
 * mixin processor skips {@code <init>}/{@code <clinit>}, so fields added to a mixin
 * class would never run their initializers.
 */
public class NSUI {

    /** No pending standalone-view request. */
    public static final int REQ_NONE = 0;
    /** Open the national spirit list / manager. */
    public static final int REQ_LIST = 1;
    /** Open the per-spirit modifier editor. */
    public static final int REQ_DATA = 2;
    /** Open the civilization list of the scenario's starting spirits. */
    public static final int REQ_PRESETS = 3;
    /** Open the civilization picker that adds a row to that list. */
    public static final int REQ_PRESET_CIV = 4;
    /** Open the starting spirits of {@link #presetCivTag} alone. */
    public static final int REQ_PRESET_SPIRITS = 5;

    private static final int REQ_COUNT = 6;

    /** The list manages definitions: picking a row opens its modifier editor. */
    public static final int MODE_MANAGE = 0;
    /** The list is picking a spirit for the condition currently being edited. */
    public static final int MODE_PICK_COND = 1;
    /** The list is picking a spirit for the outcome currently being edited. */
    public static final int MODE_PICK_OUT = 2;
    /** The list is picking a spirit to add to {@link #presetCivTag}'s starting set. */
    public static final int MODE_PICK_PRESET = 3;

    public static int mode = MODE_MANAGE;

    /** Substring the list filters definitions by; empty shows everything. */
    public static String filter = "";

    /** Slot of the in-game NS bar inside {@code MenuManager.IN_GAME_MENU}; -1 = not registered. */
    public static int barSlotID = -1;

    /**
     * Lazily created standalone view ids, indexed by request; -1 = not created yet.
     * The views outlive the menus inside them — {@code msResolveNSView} rebuilds the
     * menu and keeps the slot.
     */
    private static final int[] viewIDs = newViewIDs();

    private static int[] newViewIDs() {
        int[] tIDs = new int[REQ_COUNT];
        for (int i = 0; i < REQ_COUNT; i++) {
            tIDs[i] = -1;
        }
        return tIDs;
    }

    /** View the editor returns to when backing out. */
    public static View returnView = View.eCREATE_SCENARIO_SETTINGS;

    /** Id of the spirit currently being edited. */
    public static String editID = "";

    /**
     * Which screen the definition editor backs out to. The catalogue list is the usual
     * caller, but the starting-spirit table opens the editor too and has to get its own
     * table back rather than the list it never came from.
     */
    public static int editReturnReq = REQ_LIST;

    /** Civilization tag whose starting spirits the preset screens are working on. */
    public static String presetCivTag = "";

    /** Substring the preset civilization picker filters by; empty shows everything. */
    public static String presetFilter = "";

    /**
     * Rebuilds the in-game bar after a grant or removal changed a civilization's
     * spirit count. Does nothing unless the bar already exists and the change hit the
     * civilization whose info column is on screen, which also keeps it out of the way
     * during scenario loading and new-turn processing. Purely cosmetic, so failures
     * are swallowed rather than logged.
     */
    public static void invalidateBar(int nCivID) {
        try {
            if (barSlotID < 0 || nCivID != CFG.getActiveCivInfoId()) {
                return;
            }
            CFG.menus.rebuildInGame_Civ_Info_Diplomacy();
        } catch (Exception ignored) {
        }
    }

    private static int pendingRequest = REQ_NONE;

    /** Queues a standalone-view switch; the following setMenuID call picks it up. */
    public static void requestView(int nRequest) {
        pendingRequest = nRequest;
    }

    public static int consumeRequest() {
        int tRequest = pendingRequest;
        pendingRequest = REQ_NONE;
        return tRequest;
    }

    public static int viewIDOf(int nRequest) {
        return nRequest > REQ_NONE && nRequest < REQ_COUNT ? viewIDs[nRequest] : -1;
    }

    public static void setViewIDOf(int nRequest, int nViewID) {
        if (nRequest > REQ_NONE && nRequest < REQ_COUNT) {
            viewIDs[nRequest] = nViewID;
        }
    }

    /** Builds the menu that backs a standalone view request. */
    public static Menu createMenu(int nRequest) {
        switch (nRequest) {
            case REQ_LIST:
                return new Menu_NS_List();
            case REQ_DATA:
                return new Menu_NS_Edit_Data();
            case REQ_PRESETS:
                return new Menu_NS_Presets();
            case REQ_PRESET_CIV:
                return new Menu_NS_Preset_SelectCiv();
            case REQ_PRESET_SPIRITS:
                return new Menu_NS_Preset_Spirits();
            default:
                return null;
        }
    }

    public static void reset() {
        pendingRequest = REQ_NONE;
        editID = "";
        editReturnReq = REQ_LIST;
        mode = MODE_MANAGE;
        filter = "";
        presetCivTag = "";
        presetFilter = "";
    }
}

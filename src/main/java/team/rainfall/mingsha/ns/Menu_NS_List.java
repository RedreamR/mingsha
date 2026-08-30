package team.rainfall.mingsha.ns;

import age.of.civilizations2.jakowski.lukasz.CFG;
import age.of.civilizations2.jakowski.lukasz.Event_Conditions;
import age.of.civilizations2.jakowski.lukasz.Event_Outcome;
import age.of.civilizations2.jakowski.lukasz.Menu;
import age.of.civilizations2.jakowski.lukasz.View;
import age.of.civilizations2.jakowski.lukasz.Button.MenuElemUI;
import age.of.civilizations2.jakowski.lukasz.Button.Classic.Button_Classic;
import age.of.civilizations2.jakowski.lukasz.Button.Classic.Button_Classic_Description;
import age.of.civilizations2.jakowski.lukasz.Button.Classic.Button_Classic_LR_Line;
import age.of.civilizations2.jakowski.lukasz.Title.TitleM;

import java.util.ArrayList;
import java.util.List;

/**
 * National spirit list — a standalone view created by {@code MixinMenuManager}
 * rather than by a {@code View} enum constant, so it costs no vanilla view slot.
 * <p>
 * The same menu serves three jobs, told apart by {@link NSUI#mode}: managing the
 * global catalogue (a row opens the modifier editor) and picking a spirit for the
 * event outcome or condition currently being edited (a row writes its id there and
 * backs out). Row indices are laid out identically in all three so
 * {@link #actionEL(int)} needs no per-mode arithmetic.
 */
public class Menu_NS_List extends Menu {

    /** Element index of the first definition row; 0 = Back, 1 = Search, 2 = new. */
    private static final int FIRST_ROW = 3;

    /** Ids of the rows actually on screen, i.e. the catalogue after filtering. */
    private final List<String> shownIDs = new ArrayList<String>();

    private final boolean pickMode;

    public Menu_NS_List() {
        this.pickMode = NSUI.mode != NSUI.MODE_MANAGE;
        List<MenuElemUI> menuElements = new ArrayList<MenuElemUI>();
        // Element 0 is the back row; initMenuWithBackButton moves it to the bottom,
        // so the y given here is thrown away and the content starts at row 0.
        menuElements.add(new Button_Classic_LR_Line(null, -1, 0, 0, CFG.GAMEWIDTH, CFG.BUTTON_H, true));
        int tPosY = 0;
        menuElements.add(this.labeledInput(CFG.lang.get("Search") + ": ", NSUI.filter, tPosY++));
        menuElements.add(
            new Button_Classic(
                "+  " + CFG.lang.get("NSNew"), (int)(50.0F * CFG.GUI_SCALE), 0, rowY(tPosY++), CFG.GAMEWIDTH, CFG.BUTTON_H, true
            )
        );

        String tFilter = NSUI.filter == null ? "" : NSUI.filter.trim().toLowerCase();
        List<NationalSpirit> tDefs = NSDefs.list();
        for (int i = 0; i < tDefs.size(); i++) {
            NationalSpirit tSpirit = tDefs.get(i);
            if (!matches(tSpirit, tFilter)) {
                continue;
            }
            this.shownIDs.add(tSpirit.id);
            menuElements.add(
                new Button_Classic_Description(
                    rowDesc(tSpirit),
                    tSpirit.displayName(),
                    (int)(50.0F * CFG.GUI_SCALE),
                    0,
                    rowY(tPosY++),
                    CFG.GAMEWIDTH,
                    CFG.BUTTON_H,
                    true
                )
            );
        }
        if (this.shownIDs.isEmpty()) {
            // Nothing to pick reads as a broken screen otherwise: an empty catalogue and
            // a filter that matched nothing look identical, and the first one is the one
            // the author has to be told how to get out of. The row is unclickable, and
            // actionEL's index guard already rejects it, so no index shifts.
            menuElements.add(
                new Button_Classic_Description(
                    tDefs.isEmpty() ? CFG.lang.get("NSTapToCreate") : CFG.lang.get("Search") + ": " + NSUI.filter,
                    CFG.lang.get("NSNoDefs"),
                    (int)(50.0F * CFG.GUI_SCALE),
                    0,
                    rowY(tPosY++),
                    CFG.GAMEWIDTH,
                    CFG.BUTTON_H,
                    false
                )
            );
        }

        this.initMenuWithBackButton(
            new TitleM(null, CFG.BUTTON_H * 3 / 4, false, false),
            0,
            CFG.BUTTON_H * 3 / 4,
            CFG.GAMEWIDTH,
            CFG.GAMEHEIGHT - CFG.BUTTON_H * 3 / 4,
            menuElements,
            true,
            true
        );
        this.updateLang();
    }

    private static int rowY(int nRow) {
        return CFG.BUTTON_H * nRow + CFG.PADD * (nRow + 1);
    }

    /** Matches the filter against everything the user can see: id, name and group. */
    private static boolean matches(NationalSpirit nSpirit, String nFilter) {
        if (nFilter.isEmpty()) {
            return true;
        }
        return (nSpirit.id != null && nSpirit.id.toLowerCase().contains(nFilter))
            || nSpirit.displayName().toLowerCase().contains(nFilter)
            || (nSpirit.group != null && nSpirit.group.toLowerCase().contains(nFilter));
    }

    /** Small grey second line of a row: id, duration, group and the modifiers. */
    private static String rowDesc(NationalSpirit nSpirit) {
        StringBuilder tText = new StringBuilder(nSpirit.id);
        tText.append(" | ").append(nSpirit.durationText());
        if (nSpirit.group != null && !nSpirit.group.isEmpty()) {
            tText.append(" | ").append(nSpirit.group);
        }
        String tSummary = nSpirit.summaryText(3);
        if (!tSummary.isEmpty()) {
            tText.append(" | ").append(tSummary);
        }
        return tText.toString();
    }

    private final MenuElemUI labeledInput(final String nLabel, String nValue, int nRow) {
        return new Button_Classic(nValue, (int)(50.0F * CFG.GUI_SCALE), 0, rowY(nRow), CFG.GAMEWIDTH, CFG.BUTTON_H, true) {
            @Override
            public String getTextToDrawElem() {
                return nLabel + super.getTextToDrawElem();
            }
        };
    }

    @Override
    public void updateLang() {
        this.getTitleM().setText(CFG.lang.get(this.pickMode ? "NSSelectTitle" : "NationalSpirits"));
        this.getMenuElem(0).setTextE(CFG.lang.get("Back"));
    }

    /** Rebuilds this view in place; the request is what makes the mixin hand it back. */
    private final void rebuild() {
        NSUI.requestView(NSUI.REQ_LIST);
        CFG.menus.setMenuIDWithoutAnim(View.eCREATE_SCENARIO_SETTINGS);
    }

    /** Opens the modifier editor for one definition, which returns to this list. */
    private final void editSpirit(String nID) {
        NSUI.editID = nID;
        NSUI.editReturnReq = NSUI.REQ_LIST;
        NSUI.requestView(NSUI.REQ_DATA);
        CFG.menus.setMenuID(View.eCREATE_SCENARIO_SETTINGS);
    }

    /**
     * Writes the picked id into whatever asked for it and backs out. Event components
     * are looked up the same way their editor menu does, so a stale index simply
     * picks nothing instead of throwing.
     */
    private final void pickSpirit(String nID) {
        try {
            if (NSUI.mode == NSUI.MODE_PICK_COND) {
                Event_Conditions tCond = CFG.eventsManager
                    .createScenarioEvents
                    .getTrigger(CFG.eventsManager.createEvent_EditTriggerID)
                    .lConditions
                    .get(CFG.eventsManager.createEvent_EditConditionID);
                tCond.setText(nID);
            } else if (NSUI.mode == NSUI.MODE_PICK_OUT) {
                Event_Outcome tOutcome = CFG.eventsManager
                    .createScenarioEvents
                    .lDecisions
                    .get(CFG.eventsManager.createEvent_EditTriggerID)
                    .lOutcomes
                    .get(CFG.eventsManager.createEvent_EditConditionID);
                tOutcome.setText(nID);
            } else if (NSUI.mode == NSUI.MODE_PICK_PRESET) {
                this.addToPreset(nID);
            }
        } catch (IndexOutOfBoundsException ignored) {
        }

        this.onBackPressed();
    }

    /**
     * Appends the pick to the scenario's starting spirits for the civilization the
     * preset table is on. Owning the same spirit twice is meaningless, so a repeat pick
     * is dropped — and said out loud, because the list backs out either way and a
     * silent no-op is indistinguishable from the pick not having registered at all.
     */
    private final void addToPreset(String nID) {
        if (NSUI.presetCivTag == null || NSUI.presetCivTag.isEmpty()) {
            return;
        }
        List<String> tIDs = NSPresets.idsOf(NSUI.presetCivTag);
        if (tIDs.contains(nID)) {
            CFG.toastM.addM(CFG.lang.get("NSAlreadyOwned") + ": " + NSDefs.displayOf(nID), CFG.COLOR_NEGATIVE_2);
            return;
        }
        tIDs.add(nID);
        NSPresets.setIDsOf(NSUI.presetCivTag, tIDs);
        CFG.toastM.addM(CFG.lang.get("NSAdded") + ": " + NSDefs.displayOf(nID), CFG.COLOR_POSITIVE);
    }

    @Override
    public final void actionEL(int iID) {
        switch (iID) {
            case 0:
                this.onBackPressed();
                return;
            case 1:
                CFG.showKeyboard();
                CFG.keyboardSave = new CFG.Keyboard_Action() {
                    @Override
                    public void action() {
                        NSUI.filter = CFG.keybMess == null ? "" : CFG.keybMess.trim();
                        Menu_NS_List.this.rebuild();
                    }
                };
                return;
            case 2: {
                // Always the editor, even when picking: a spirit created here has no
                // modifiers yet, and the global catalogue's own entry point is over in
                // the game editor, so this is the only way to fill one in from inside a
                // scenario. Coming back lands on this list again, still in pick mode.
                this.editSpirit(NSDefs.createNew());
                return;
            }
            default: {
                int tIndex = iID - FIRST_ROW;
                if (tIndex >= 0 && tIndex < this.shownIDs.size()) {
                    if (this.pickMode) {
                        this.pickSpirit(this.shownIDs.get(tIndex));
                    } else {
                        this.editSpirit(this.shownIDs.get(tIndex));
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        // One civilization's starting-spirit screen opened this list, so back belongs to
        // it and the feature's shared state has to survive the hop — including the tag
        // that screen is for; every other caller is done with it.
        if (NSUI.mode == NSUI.MODE_PICK_PRESET) {
            NSUI.mode = NSUI.MODE_MANAGE;
            NSUI.filter = "";
            NSUI.requestView(NSUI.REQ_PRESET_SPIRITS);
            CFG.menus.setMenuID(NSUI.returnView == null ? View.eMAINMENU : NSUI.returnView);
            CFG.menus.setBackAnimation(true);
            return;
        }

        View tBack = NSUI.returnView;
        NSUI.reset();
        CFG.menus.setMenuID(tBack == null ? View.eMAINMENU : tBack);
        CFG.menus.setBackAnimation(true);
    }

    @Override
    public void actionCloseMenu() {
        this.onBackPressed();
    }
}

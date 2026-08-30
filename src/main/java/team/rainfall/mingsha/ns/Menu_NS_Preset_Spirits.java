package team.rainfall.mingsha.ns;

import age.of.civilizations2.jakowski.lukasz.CFG;
import age.of.civilizations2.jakowski.lukasz.Menu;
import age.of.civilizations2.jakowski.lukasz.View;
import age.of.civilizations2.jakowski.lukasz.Button.MenuElemUI;
import age.of.civilizations2.jakowski.lukasz.Button.Classic.Button_Classic;
import age.of.civilizations2.jakowski.lukasz.Button.Classic.Button_Classic_Description;
import age.of.civilizations2.jakowski.lukasz.Button.Classic.Button_Classic_LR_Line;
import age.of.civilizations2.jakowski.lukasz.Button.Classic.Button_Classic_Remove;
import age.of.civilizations2.jakowski.lukasz.Title.TitleM;

import java.util.ArrayList;
import java.util.List;

/**
 * The scenario's starting national spirits, second level: the spirits of the one
 * civilization {@link NSUI#presetCivTag} names.
 * <p>
 * Adding, editing and removing all happen here, so the screen only ever shows spirits
 * and the civilization it belongs to is stated once, in the title. Clicking a spirit
 * opens its definition editor and comes back here; the {@code -} button beside it is
 * what removes it from this civilization's starting set, which is a different thing from
 * deleting the definition and is why the row itself is never destructive.
 * <p>
 * Element ids are mapped the same way the civilization list maps them —
 * {@link #rowKinds} beside {@link #rowSpiritIDs}, indexed by element id minus
 * {@link #FIRST_ROW} — because each row carries a button of its own.
 * <p>
 * Removing the last spirit leaves the civilization in the table with none selected —
 * {@link NSPresets#setIDsOf} keeps the row either way — so this screen can legitimately
 * be empty while still being the right place to add the next one. Dropping the
 * civilization itself is the level above's job.
 */
public class Menu_NS_Preset_Spirits extends Menu {

    /** Element index of the first spirit row; 0 = Back, 1 = add spirit. */
    private static final int FIRST_ROW = 2;

    private static final int KIND_NONE = 0;
    private static final int KIND_EDIT_SPIRIT = 1;
    private static final int KIND_REMOVE_SPIRIT = 2;

    private final List<Integer> rowKinds = new ArrayList<Integer>();
    private final List<String> rowSpiritIDs = new ArrayList<String>();

    /** Tag this screen is editing, captured at build time so a later reset cannot move it. */
    private final String civTag;

    public Menu_NS_Preset_Spirits() {
        this.civTag = NSUI.presetCivTag == null ? "" : NSUI.presetCivTag;

        List<MenuElemUI> menuElements = new ArrayList<MenuElemUI>();
        // Element 0 is the back row; initMenuWithBackButton moves it to the bottom,
        // so the y given here is thrown away and the content starts at the top.
        menuElements.add(new Button_Classic_LR_Line(null, -1, 0, 0, CFG.GAMEWIDTH, CFG.BUTTON_H, true));
        int tY = CFG.PADD;
        menuElements.add(
            new Button_Classic(
                "+  " + CFG.lang.get("NSAddSpirit"), (int)(50.0F * CFG.GUI_SCALE), 0, tY, CFG.GAMEWIDTH, CFG.BUTTON_H, true
            )
        );
        tY += CFG.BUTTON_H + CFG.PADD;

        List<String> tIDs = this.civTag.isEmpty() ? new ArrayList<String>() : NSPresets.idsOf(this.civTag);
        if (tIDs.isEmpty()) {
            tY = this.addEmptyState(menuElements, tY);
        } else {
            for (int i = 0; i < tIDs.size(); i++) {
                tY = this.addSpiritRow(menuElements, tIDs.get(i), tY);
            }
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

    /** Points at the row above while this civilization has nothing yet. */
    private final int addEmptyState(List<MenuElemUI> nElements, int nPosY) {
        nElements.add(
            new Button_Classic_Description(
                CFG.lang.get("NSTapToAdd"),
                CFG.lang.get("NSEmptyTitle"),
                (int)(50.0F * CFG.GUI_SCALE),
                0,
                nPosY,
                CFG.GAMEWIDTH,
                CFG.BUTTON_H,
                false
            )
        );
        this.mapRow(KIND_NONE, "");
        return nPosY + CFG.BUTTON_H + CFG.PADD;
    }

    /** One spirit and its remove button, returning the y the next row starts at. */
    private final int addSpiritRow(List<MenuElemUI> nElements, String nID, int nPosY) {
        int tRowW = CFG.GAMEWIDTH - CFG.BUTTON_W;
        nElements.add(new Button_NS_SpiritRow(nID, spiritDesc(nID), NSDefs.displayOf(nID), 0, nPosY, tRowW, CFG.BUTTON_H));
        this.mapRow(KIND_EDIT_SPIRIT, nID);
        nElements.add(removeButton(CFG.lang.get("NSRemoveSpirit"), tRowW, nPosY));
        this.mapRow(KIND_REMOVE_SPIRIT, nID);
        return nPosY + CFG.BUTTON_H + CFG.PADD;
    }

    /** Records what the element just added is for. */
    private final void mapRow(int nKind, String nSpiritID) {
        this.rowKinds.add(Integer.valueOf(nKind));
        this.rowSpiritIDs.add(nSpiritID);
    }

    /** Second line of a spirit row: how long it lasts and what it actually does. */
    private static String spiritDesc(String nID) {
        NationalSpirit tSpirit = NSDefs.get(nID);
        if (tSpirit == null) {
            // Overridden by the row itself, which knows the definition is gone.
            return nID;
        }
        StringBuilder tText = new StringBuilder(tSpirit.durationText());
        if (tSpirit.group != null && !tSpirit.group.isEmpty()) {
            tText.append("  |  ").append(tSpirit.group);
        }
        String tSummary = tSpirit.summaryText(3);
        tText.append("  |  ").append(tSummary.isEmpty() ? CFG.lang.get("NSNoModifiers") : tSummary);
        return tText.toString();
    }

    private static MenuElemUI removeButton(final String nHoverText, int nPosX, int nPosY) {
        return new Button_Classic_Remove(nPosX, nPosY, CFG.BUTTON_W, CFG.BUTTON_H, true) {
            @Override
            public void buildElemHover() {
                this.menuElemHover = NSHover.text(nHoverText);
            }
        };
    }

    @Override
    public void updateLang() {
        this.getTitleM()
            .setText(
                this.civTag.isEmpty()
                    ? CFG.lang.get("NSStartingTitle")
                    : NSPresets.civNameOf(this.civTag) + "  ·  " + CFG.lang.get("NSStartingTitle")
            );
        this.getMenuElem(0).setTextE(CFG.lang.get("Back"));
    }

    /** Rebuilds this view in place, picking up whatever the last edit changed. */
    private final void rebuild() {
        NSUI.presetCivTag = this.civTag;
        NSUI.requestView(NSUI.REQ_PRESET_SPIRITS);
        CFG.menus.setMenuIDWithoutAnim(View.eCREATE_SCENARIO_SETTINGS);
    }

    /** Hands over to the spirit list, which appends its pick here and comes back. */
    private final void addSpirit() {
        if (this.civTag.isEmpty()) {
            return;
        }
        NSUI.presetCivTag = this.civTag;
        NSUI.mode = NSUI.MODE_PICK_PRESET;
        NSUI.filter = "";
        NSUI.requestView(NSUI.REQ_LIST);
        CFG.menus.setMenuID(View.eCREATE_SCENARIO_SETTINGS);
    }

    /**
     * Opens the definition editor and comes back here rather than to the catalogue list,
     * which is what {@link NSUI#editReturnReq} is for. A dead id has nothing to open, so
     * it only says so — removing it is still possible through the button beside it.
     */
    private final void editSpirit(String nSpiritID) {
        if (NSDefs.get(nSpiritID) == null) {
            CFG.toastM.addM(CFG.lang.get("NSMissing") + ": " + nSpiritID, CFG.COLOR_NEGATIVE_2);
            return;
        }
        NSUI.presetCivTag = this.civTag;
        NSUI.mode = NSUI.MODE_MANAGE;
        NSUI.editID = nSpiritID;
        NSUI.editReturnReq = NSUI.REQ_PRESET_SPIRITS;
        NSUI.requestView(NSUI.REQ_DATA);
        CFG.menus.setMenuID(View.eCREATE_SCENARIO_SETTINGS);
    }

    /** Drops one spirit from this civilization's starting set. */
    private final void removeSpirit(String nSpiritID) {
        List<String> tIDs = NSPresets.idsOf(this.civTag);
        tIDs.remove(nSpiritID);
        NSPresets.setIDsOf(this.civTag, tIDs);
        this.rebuild();
    }

    @Override
    public final void actionEL(int iID) {
        switch (iID) {
            case 0:
                this.onBackPressed();
                return;
            case 1:
                this.addSpirit();
                return;
            default: {
                int tIndex = iID - FIRST_ROW;
                if (tIndex < 0 || tIndex >= this.rowKinds.size()) {
                    return;
                }
                String tSpiritID = this.rowSpiritIDs.get(tIndex);
                switch (this.rowKinds.get(tIndex).intValue()) {
                    case KIND_EDIT_SPIRIT:
                        this.editSpirit(tSpiritID);
                        return;
                    case KIND_REMOVE_SPIRIT:
                        this.removeSpirit(tSpiritID);
                        return;
                    default:
                }
            }
        }
    }

    /** Back goes up one level, to the civilization list this screen was opened from. */
    @Override
    public void onBackPressed() {
        NSUI.presetCivTag = "";
        NSUI.requestView(NSUI.REQ_PRESETS);
        CFG.menus.setMenuID(NSUI.returnView == null ? View.eMAINMENU : NSUI.returnView);
        CFG.menus.setBackAnimation(true);
    }

    @Override
    public void actionCloseMenu() {
        this.onBackPressed();
    }
}

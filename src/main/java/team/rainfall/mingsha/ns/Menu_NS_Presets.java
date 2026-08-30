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
 * The scenario's starting national spirits, first level: which civilizations have any.
 * <p>
 * One row per civilization — flag, name and how many spirits it begins with — and
 * nothing else, because a flat table that mixed civilizations and their spirits in one
 * scrolling list turned out to be unreadable. Clicking a civilization opens
 * {@link Menu_NS_Preset_Spirits}, which is where spirits are actually added, edited and
 * removed; the {@code -} button at the right edge is the only destructive thing here.
 * <p>
 * Element ids do not line up with civilizations once every one of them carries a button
 * of its own, so what an element does is recorded while it is built: {@link #rowKinds}
 * beside {@link #rowCivTags}, both indexed by element id minus {@link #FIRST_ROW}.
 * <p>
 * Every edit writes straight through to {@link NSPresets}, so the Back row saves nothing
 * and there is no unsaved state to lose.
 */
public class Menu_NS_Presets extends Menu {

    /** Element index of the first civilization row; 0 = Back, 1 = add civilization. */
    private static final int FIRST_ROW = 2;

    private static final int KIND_NONE = 0;
    private static final int KIND_OPEN_CIV = 1;
    private static final int KIND_REMOVE_CIV = 2;

    private final List<Integer> rowKinds = new ArrayList<Integer>();
    private final List<String> rowCivTags = new ArrayList<String>();

    public Menu_NS_Presets() {
        List<MenuElemUI> menuElements = new ArrayList<MenuElemUI>();
        // Element 0 is the back row; initMenuWithBackButton moves it to the bottom,
        // so the y given here is thrown away and the content starts at the top.
        menuElements.add(new Button_Classic_LR_Line(null, -1, 0, 0, CFG.GAMEWIDTH, CFG.BUTTON_H, true));
        int tY = CFG.PADD;
        menuElements.add(
            new Button_Classic(
                "+  " + CFG.lang.get("NSAddCiv"), (int)(50.0F * CFG.GUI_SCALE), 0, tY, CFG.GAMEWIDTH, CFG.BUTTON_H, true
            )
        );
        tY += CFG.BUTTON_H + CFG.PADD;

        List<NSPresets.Preset> tPresets = NSPresets.list();
        if (tPresets.isEmpty()) {
            tY = this.addEmptyState(menuElements, tY);
        } else {
            for (int i = 0; i < tPresets.size(); i++) {
                tY = this.addCivRow(menuElements, tPresets.get(i), tY);
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

    /** Says what the screen is for while it has nothing to show. */
    private final int addEmptyState(List<MenuElemUI> nElements, int nPosY) {
        nElements.add(
            new Button_Classic_Description(
                CFG.lang.get("NSEmptyDesc"),
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

    /** One civilization and its remove button, returning the y the next row starts at. */
    private final int addCivRow(List<MenuElemUI> nElements, NSPresets.Preset nPreset, int nPosY) {
        int tRowW = CFG.GAMEWIDTH - CFG.BUTTON_W;
        nElements.add(
            new Button_NS_CivRow(
                NSPresets.civIDOf(nPreset.civTag),
                civDesc(nPreset),
                NSPresets.civNameOf(nPreset.civTag),
                CFG.lang.get("NSTapToEdit"),
                0,
                nPosY,
                tRowW,
                CFG.BUTTON_H
            )
        );
        this.mapRow(KIND_OPEN_CIV, nPreset.civTag);
        nElements.add(removeButton(CFG.lang.get("NSRemoveCiv"), tRowW, nPosY));
        this.mapRow(KIND_REMOVE_CIV, nPreset.civTag);
        return nPosY + CFG.BUTTON_H + CFG.PADD;
    }

    /** Records what the element just added is for. */
    private final void mapRow(int nKind, String nCivTag) {
        this.rowKinds.add(Integer.valueOf(nKind));
        this.rowCivTags.add(nCivTag);
    }

    /**
     * Second line of a civilization row: its tag, its count and the first few spirit
     * names, so the list still says something without opening every civilization.
     */
    private static String civDesc(NSPresets.Preset nPreset) {
        StringBuilder tText = new StringBuilder(nPreset.civTag);
        tText.append("  |  ").append(CFG.lang.get("NationalSpirits")).append(": ").append(nPreset.ids.size());
        for (int i = 0; i < nPreset.ids.size() && i < 3; i++) {
            tText.append(i == 0 ? "  |  " : ", ").append(NSDefs.displayOf(nPreset.ids.get(i)));
        }
        if (nPreset.ids.size() > 3) {
            tText.append(", ...");
        }
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
        this.getTitleM().setText(CFG.lang.get("NSStartingTitle"));
        this.getMenuElem(0).setTextE(CFG.lang.get("Back"));
    }

    /** Rebuilds this view in place, picking up whatever the last edit changed. */
    private final void rebuild() {
        NSUI.requestView(NSUI.REQ_PRESETS);
        CFG.menus.setMenuIDWithoutAnim(View.eCREATE_SCENARIO_SETTINGS);
    }

    /** Hands over to the civilization picker, which comes back here. */
    private final void addCivilization() {
        NSUI.presetFilter = "";
        NSUI.requestView(NSUI.REQ_PRESET_CIV);
        CFG.menus.setMenuID(View.eCREATE_SCENARIO_SETTINGS);
    }

    /** Opens one civilization's own screen, which is where its spirits are edited. */
    private final void openCivilization(String nCivTag) {
        NSUI.presetCivTag = nCivTag;
        NSUI.requestView(NSUI.REQ_PRESET_SPIRITS);
        CFG.menus.setMenuID(View.eCREATE_SCENARIO_SETTINGS);
    }

    /** Drops a whole civilization from the table; its spirits go with it. */
    private final void removeCivilization(String nCivTag) {
        NSPresets.removeCiv(nCivTag);
        this.rebuild();
    }

    @Override
    public final void actionEL(int iID) {
        switch (iID) {
            case 0:
                this.onBackPressed();
                return;
            case 1:
                this.addCivilization();
                return;
            default: {
                int tIndex = iID - FIRST_ROW;
                if (tIndex < 0 || tIndex >= this.rowKinds.size()) {
                    return;
                }
                String tCivTag = this.rowCivTags.get(tIndex);
                switch (this.rowKinds.get(tIndex).intValue()) {
                    case KIND_OPEN_CIV:
                        this.openCivilization(tCivTag);
                        return;
                    case KIND_REMOVE_CIV:
                        this.removeCivilization(tCivTag);
                        return;
                    default:
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
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

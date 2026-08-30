package team.rainfall.mingsha.ns;

import age.of.civilizations2.jakowski.lukasz.CFG;
import age.of.civilizations2.jakowski.lukasz.Menu;
import age.of.civilizations2.jakowski.lukasz.View;
import age.of.civilizations2.jakowski.lukasz.Button.MenuElemUI;
import age.of.civilizations2.jakowski.lukasz.Button.Classic.Button_Classic;
import age.of.civilizations2.jakowski.lukasz.Button.Classic.Button_Classic_LR_Line;
import age.of.civilizations2.jakowski.lukasz.Title.TitleM;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Picks the civilization a starting-spirit row is for.
 * <p>
 * Vanilla's civilization pickers are a pair of menus — an alphabet index beside a
 * list — which only pays off for the thousands of civilizations a full map has. This
 * one is a single searchable list instead, the same shape as {@link Menu_NS_List},
 * because it is reached from a scenario-authoring screen and not from gameplay.
 * <p>
 * Picking creates the row right away, empty, and lands on that civilization's own
 * spirit screen. A civilization that already has a row is simply opened, so searching
 * for one is a second way in and not a way to lose what it already owns.
 */
public class Menu_NS_Preset_SelectCiv extends Menu {

    /** Element index of the first civilization row; 0 = Back, 1 = Search. */
    private static final int FIRST_ROW = 2;

    /** Civilization ids of the rows on screen, i.e. the map after filtering. */
    private final List<Integer> shownIDs = new ArrayList<Integer>();

    public Menu_NS_Preset_SelectCiv() {
        List<MenuElemUI> menuElements = new ArrayList<MenuElemUI>();
        // Element 0 is the back row; initMenuWithBackButton moves it to the bottom,
        // so the y given here is thrown away and the content starts at row 0.
        menuElements.add(new Button_Classic_LR_Line(null, -1, 0, 0, CFG.GAMEWIDTH, CFG.BUTTON_H, true));
        int tRow = 0;
        menuElements.add(this.labeledInput(CFG.lang.get("Search") + ": ", NSUI.presetFilter, tRow++));

        String tFilter = NSUI.presetFilter == null ? "" : NSUI.presetFilter.trim().toLowerCase();
        Set<String> tTaken = takenTags();
        for (int i = 1; i < CFG.core.getCivsSize(); i++) {
            String tName;
            String tTag;
            try {
                tName = CFG.core.getCiv(i).getCivName();
                tTag = CFG.core.getCiv(i).getCivTag();
            } catch (Exception e) {
                continue;
            }
            if (!matches(tName, tTag, tFilter)) {
                continue;
            }

            this.shownIDs.add(i);
            int tOwned = tTaken.contains(tTag) ? NSPresets.idsOf(tTag).size() : 0;
            menuElements.add(
                new Button_NS_CivRow(
                    i,
                    tOwned > 0 ? tTag + "  |  " + CFG.lang.get("NationalSpirits") + ": " + tOwned : tTag,
                    tName,
                    CFG.lang.get("NSTapToEdit"),
                    0,
                    rowY(tRow++),
                    CFG.GAMEWIDTH,
                    CFG.BUTTON_H
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

    /**
     * Tags that already have a row, collected in one pass — the per-civilization
     * count below would otherwise re-parse the whole table for every civilization on
     * the map.
     */
    private static Set<String> takenTags() {
        Set<String> tTags = new HashSet<String>();
        List<NSPresets.Preset> tPresets = NSPresets.list();
        for (int i = 0; i < tPresets.size(); i++) {
            tTags.add(tPresets.get(i).civTag);
        }
        return tTags;
    }

    private static boolean matches(String nName, String nTag, String nFilter) {
        if (nFilter.isEmpty()) {
            return true;
        }
        return (nName != null && nName.toLowerCase().contains(nFilter))
            || (nTag != null && nTag.toLowerCase().contains(nFilter));
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
        this.getTitleM().setText(CFG.lang.get("SelectCivilization"));
        this.getMenuElem(0).setTextE(CFG.lang.get("Back"));
    }

    /** Rebuilds this view in place after the search box changed. */
    private final void rebuild() {
        NSUI.requestView(NSUI.REQ_PRESET_CIV);
        CFG.menus.setMenuIDWithoutAnim(View.eCREATE_SCENARIO_SETTINGS);
    }

    /**
     * Creates the civilization's row and opens it.
     * <p>
     * The row is written before the hop so the civilization is remembered even if the
     * author picks nothing there, and so its own screen has something to show.
     */
    private final void pickCiv(int nCivID) {
        String tCivTag;
        try {
            tCivTag = CFG.core.getCiv(nCivID).getCivTag();
        } catch (Exception e) {
            CFG.exceptionStack(e);
            return;
        }
        if (tCivTag == null || tCivTag.isEmpty()) {
            return;
        }
        NSPresets.ensureCiv(tCivTag);
        NSUI.presetCivTag = tCivTag;
        NSUI.presetFilter = "";
        NSUI.requestView(NSUI.REQ_PRESET_SPIRITS);
        CFG.menus.setMenuID(View.eCREATE_SCENARIO_SETTINGS);
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
                        NSUI.presetFilter = CFG.keybMess == null ? "" : CFG.keybMess.trim();
                        Menu_NS_Preset_SelectCiv.this.rebuild();
                    }
                };
                return;
            default: {
                int tIndex = iID - FIRST_ROW;
                if (tIndex >= 0 && tIndex < this.shownIDs.size()) {
                    this.pickCiv(this.shownIDs.get(tIndex));
                }
            }
        }
    }

    /** Back goes to the table this picker was opened from, not out of the feature. */
    @Override
    public void onBackPressed() {
        NSUI.presetFilter = "";
        NSUI.requestView(NSUI.REQ_PRESETS);
        CFG.menus.setMenuID(NSUI.returnView == null ? View.eMAINMENU : NSUI.returnView);
        CFG.menus.setBackAnimation(true);
    }

    @Override
    public void actionCloseMenu() {
        this.onBackPressed();
    }
}

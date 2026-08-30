package team.rainfall.mingsha.ns;

import age.of.civilizations2.jakowski.lukasz.CFG;
import age.of.civilizations2.jakowski.lukasz.Menu;
import age.of.civilizations2.jakowski.lukasz.View;
import age.of.civilizations2.jakowski.lukasz.Button.MenuElemUI;
import age.of.civilizations2.jakowski.lukasz.Button.Classic.Button_Classic;
import age.of.civilizations2.jakowski.lukasz.Button.Classic.Button_Classic_LR_Line;
import age.of.civilizations2.jakowski.lukasz.Button.NewGame.Button_NewGameStyle_Left;
import age.of.civilizations2.jakowski.lukasz.Button.NewGame.Button_NewGameStyle_Middle;
import age.of.civilizations2.jakowski.lukasz.Button.NewGame.Button_NewGameStyle_Right;
import age.of.civilizations2.jakowski.lukasz.Title.TitleM;

import java.util.ArrayList;
import java.util.List;

/**
 * Editor for one national spirit definition, laid out like the leader editor:
 * keyboard rows for the text fields and a {@code - value +} triple per modifier,
 * stepping by {@code 0.01F} and shown as whole percent points.
 * <p>
 * The eleven modifier rows are generated in a loop rather than written out one by
 * one, so their element ids are {@link #ELEM_MOD_FIRST} {@code + index * 3} and a
 * twelfth modifier would need no change here.
 * <p>
 * Edits land on the live {@link NationalSpirit} immediately; leaving the menu — by
 * the back row or by the hardware back key — is what writes the catalogue to disk,
 * as there is no cancel affordance that would make an unsaved state meaningful.
 */
public class Menu_NS_Edit_Data extends Menu {

    /** Moved to the bottom of the menu by {@code initMenuWithBackButton}. */
    private static final int ELEM_BACK = 0;
    private static final int ELEM_ID = 1;
    private static final int ELEM_NAME = 2;
    private static final int ELEM_DESC = 3;
    private static final int ELEM_GROUP = 4;
    /** Shows where the icon is looked for; clicking it re-reads the file. */
    private static final int ELEM_ICON = 5;
    /** First of the {@code - turns +} triple. */
    private static final int ELEM_TURNS = 6;
    /** First of the eleven {@code - value +} modifier triples. */
    private static final int ELEM_MOD_FIRST = 9;
    private static final int ELEM_DELETE = ELEM_MOD_FIRST + NationalSpirit.MOD_COUNT * 3;

    /** The definition being edited; detached when its id has gone missing. */
    private final NationalSpirit spirit;

    public Menu_NS_Edit_Data() {
        NationalSpirit tSpirit = NSDefs.get(NSUI.editID);
        this.spirit = tSpirit != null ? tSpirit : new NationalSpirit(NSUI.editID);

        List<MenuElemUI> menuElements = new ArrayList<MenuElemUI>();
        menuElements.add(new Button_Classic_LR_Line(null, -1, 0, 0, CFG.GAMEWIDTH, CFG.BUTTON_H, true));
        int tY = CFG.PADD;
        menuElements.add(this.labeledInput("ID: ", this.spirit.id, tY));
        tY += CFG.BUTTON_H + CFG.PADD;
        menuElements.add(this.labeledInput(CFG.lang.get("Name") + ": ", this.spirit.name, tY));
        tY += CFG.BUTTON_H + CFG.PADD;
        menuElements.add(this.labeledInput(CFG.lang.get("Description") + ": ", this.spirit.desc, tY));
        tY += CFG.BUTTON_H + CFG.PADD;
        menuElements.add(this.labeledInput(CFG.lang.get("NSGroup") + ": ", this.spirit.group, tY));
        tY += CFG.BUTTON_H + CFG.PADD;
        // Not an input: the icon is named after the id, so there is nothing to type.
        menuElements.add(new Button_Classic(null, (int)(50.0F * CFG.GUI_SCALE), 0, tY, CFG.GAMEWIDTH, CFG.BUTTON_H, true));
        tY += CFG.BUTTON_H + CFG.PADD;

        tY = addLRLine(menuElements, tY);
        for (int i = 0; i < NationalSpirit.MOD_COUNT; i++) {
            tY = addLRLine(menuElements, tY);
        }

        menuElements.add(new Button_Classic_LR_Line(null, -1, 0, tY, CFG.GAMEWIDTH, CFG.BUTTON_H, true));
        tY += CFG.BUTTON_H + CFG.PADD;

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

    private final MenuElemUI labeledInput(final String nLabel, String nValue, int nPosY) {
        return new Button_Classic(nValue, (int)(50.0F * CFG.GUI_SCALE), 0, nPosY, CFG.GAMEWIDTH, CFG.BUTTON_H, true) {
            @Override
            public String getTextToDrawElem() {
                return nLabel + super.getTextToDrawElem();
            }
        };
    }

    /**
     * Appends one {@code - value +} row, the same three-element shape the leader
     * editor uses, and returns the y the next row starts at.
     */
    private static int addLRLine(List<MenuElemUI> nElements, int nPosY) {
        int tArrowW = (int)(CFG.BUTTON_H * 0.8F);
        int tRowH = (int)(CFG.BUTTON_H * 0.6F);
        int tMiddleW = CFG.GAMEWIDTH - CFG.PADD * 2 - tArrowW * 2;
        nElements.add(new Button_NewGameStyle_Left("-", -1, CFG.PADD, nPosY, tArrowW, tRowH, true));
        nElements.add(new Button_NewGameStyle_Middle(null, -1, CFG.PADD + tArrowW, nPosY, tMiddleW, tRowH, true));
        nElements.add(new Button_NewGameStyle_Right("+", -1, CFG.PADD + tArrowW + tMiddleW, nPosY, tArrowW, tRowH, true));
        return nPosY + tRowH + CFG.PADD;
    }

    /** Percent form of one modifier, with the sign spelled out like the leader editor. */
    private final String modifierText(int nIndex) {
        int tPercent = Math.round(this.spirit.getModifier(nIndex) * 100.0F);
        return NationalSpirit.labelOf(nIndex) + ": " + (tPercent > 0 ? "+" : "") + tPercent + "%";
    }

    /**
     * The icon row: where the file is expected and whether it is there. The id shown
     * is the saved one, so a pending edit of the ID field only moves it after Save.
     */
    private final String iconText() {
        return CFG.lang.get("NSIcon")
            + ": "
            + NSIcons.pathOf(this.spirit.id)
            + (NSIcons.has(this.spirit.id) ? "" : "  (" + CFG.lang.get("NSIconMissing") + ")");
    }

    @Override
    public void updateLang() {
        this.getTitleM().setText(CFG.lang.get("NSEditSpirit"));
        this.getMenuElem(ELEM_BACK).setTextE(CFG.lang.get("Save"));
        this.getMenuElem(ELEM_ICON).setTextE(this.iconText());
        this.getMenuElem(ELEM_TURNS + 1)
            .setTextE(
                CFG.lang.get("Turns")
                    + ": "
                    + (this.spirit.defaultTurns < 0 ? CFG.lang.get("NSPermanent") : String.valueOf(this.spirit.defaultTurns))
            );
        for (int i = 0; i < NationalSpirit.MOD_COUNT; i++) {
            this.getMenuElem(ELEM_MOD_FIRST + i * 3 + 1).setTextE(this.modifierText(i));
        }

        this.getMenuElem(ELEM_DELETE).setTextE(CFG.lang.get("Delete"));
    }

    /**
     * Copies the four text rows into the definition and persists the catalogue.
     * The id goes through {@link NSDefs#rename} so a collision is refused rather
     * than silently merging two definitions; the old id simply stays in that case.
     */
    private final void saveData() {
        this.spirit.name = textOf(this.getMenuElem(ELEM_NAME));
        this.spirit.desc = textOf(this.getMenuElem(ELEM_DESC));
        this.spirit.group = textOf(this.getMenuElem(ELEM_GROUP));
        String tID = textOf(this.getMenuElem(ELEM_ID));
        if (!tID.isEmpty() && !tID.equals(this.spirit.id)) {
            if (NSDefs.rename(this.spirit.id, tID)) {
                NSUI.editID = tID;
            } else {
                CFG.toastM.addM(CFG.lang.get("NSIDInUse") + ": " + tID, CFG.COLOR_NEGATIVE_2);
            }
        }

        NSDefs.save();
    }

    private static String textOf(MenuElemUI nElem) {
        return nElem.getTextE() == null ? "" : nElem.getTextE().trim();
    }

    /** Returns to whichever screen opened this editor, still in the mode it was in. */
    private final void backToList() {
        NSUI.requestView(NSUI.editReturnReq);
        CFG.menus.setMenuID(NSUI.returnView == null ? View.eMAINMENU : NSUI.returnView);
        CFG.menus.setBackAnimation(true);
    }

    @Override
    public final void actionEL(int iID) {
        switch (iID) {
            case ELEM_BACK:
                this.onBackPressed();
                return;
            case ELEM_ID:
            case ELEM_NAME:
            case ELEM_DESC:
            case ELEM_GROUP:
                CFG.showKeyboard();
                return;
            case ELEM_ICON:
                // Re-read the file, so an icon dropped in while the game is running
                // shows up without a restart.
                NSIcons.forget(this.spirit.id);
                this.updateLang();
                CFG.toastM.addM(
                    this.getMenuElem(ELEM_ICON).getTextE(),
                    NSIcons.has(this.spirit.id) ? CFG.COLOR_POSITIVE : CFG.COLOR_NEGATIVE_2
                );
                return;
            case ELEM_TURNS:
                this.spirit.defaultTurns = Math.max(-1, this.spirit.defaultTurns - 1);
                this.updateLang();
                return;
            case ELEM_TURNS + 1:
                CFG.toastM.addM(this.getMenuElem(iID).getTextE(), CFG.COLOR_TEXT_NUM_OF_PROVINCES);
                return;
            case ELEM_TURNS + 2:
                this.spirit.defaultTurns++;
                this.updateLang();
                return;
            case ELEM_DELETE:
                NSDefs.delete(this.spirit.id);
                NSUI.editID = "";
                this.backToList();
                return;
            default:
                this.actionModifierEL(iID);
        }
    }

    /** Handles the three element ids of whichever modifier row was clicked. */
    private final void actionModifierEL(int iID) {
        int tOffset = iID - ELEM_MOD_FIRST;
        if (tOffset < 0 || tOffset >= NationalSpirit.MOD_COUNT * 3) {
            return;
        }

        int tIndex = tOffset / 3;
        switch (tOffset % 3) {
            case 0:
                this.spirit.setModifier(tIndex, this.spirit.getModifier(tIndex) - 0.01F);
                this.updateLang();
                return;
            case 1:
                CFG.toastM.addM(this.getMenuElem(iID).getTextE(), CFG.COLOR_TEXT_NUM_OF_PROVINCES);
                return;
            default:
                this.spirit.setModifier(tIndex, this.spirit.getModifier(tIndex) + 0.01F);
                this.updateLang();
        }
    }

    @Override
    public void onBackPressed() {
        this.saveData();
        this.backToList();
    }

    @Override
    public void actionCloseMenu() {
        this.onBackPressed();
    }
}

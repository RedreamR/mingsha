package team.rainfall.mingsha.ns;

import age.of.civilizations2.jakowski.lukasz.AoCGame;
import age.of.civilizations2.jakowski.lukasz.CFG;
import age.of.civilizations2.jakowski.lukasz.IMGManager;
import age.of.civilizations2.jakowski.lukasz.Images;
import age.of.civilizations2.jakowski.lukasz.Menu;
import age.of.civilizations2.jakowski.lukasz.Renderer;
import age.of.civilizations2.jakowski.lukasz.Button.MenuElemUI;
import age.of.civilizations2.jakowski.lukasz.Core.Core;
import age.of.civilizations2.jakowski.lukasz.GameValues.GameValues;
import age.of.civilizations2.jakowski.lukasz.Menus.Civilization.Menu_InGame_Civ;
import age.of.civilizations2.jakowski.lukasz.Menus.Civilization.Menu_InGame_Civ_DiplomacyORActions;
import age.of.civilizations2.jakowski.lukasz.Title.TitleM;
import age.of.civilizations2.jakowski.lukasz.Title.TitleM_TextSmall;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

/**
 * In-game national spirit bar, sitting directly above the diplomacy box in the
 * left civilization-info column.
 * <p>
 * Layout is a single row of square icon slots, five of them fitting the panel
 * width; anything beyond that scrolls horizontally, which {@code Menu} provides
 * for free once the elements are wider than the menu ({@code scrollableX} plus
 * clipping in {@code beginClipM}).
 * <p>
 * The slide-in/out animation and the edge-line chrome are copied from
 * {@code Menu_InGame_Civ_Diplomacy} so the bar moves as one with the rest of the
 * column.
 */
public class Menu_InGame_Civ_NS extends Menu {

    /** Slots that fit the panel width before scrolling starts. */
    public static final int SLOTS_PER_ROW = 5;

    public static int getTitleHeight() {
        return Menu_InGame_Civ_DiplomacyORActions.getButtonHeight();
    }

    public Menu_InGame_Civ_NS() {
        List<MenuElemUI> menuElements = new ArrayList<MenuElemUI>();
        int tMenuW = Menu_InGame_Civ.getMenuCivInfoWidth();
        int tSlotW = (tMenuW - 2) / SLOTS_PER_ROW;
        int tSlotH = tSlotW;
        int tPosX = 0;

        List<String> tOwned = NSStore.listOwnedIDs(CFG.getActiveCivInfoId());
        for (int i = 0; i < tOwned.size(); i++) {
            menuElements.add(new Button_NS_Slot(tOwned.get(i), tPosX, 0, tSlotW, tSlotH));
            tPosX += tSlotW;
        }

        this.initMenu(
            (TitleM)new TitleM_TextSmall(CFG.lang.get("NationalSpirits"), getTitleHeight(), false, false) {
                @Override
                public void drawT(SpriteBatch oSB, int iTranslateX, int nPosX, int nPosY, int nWidth, boolean sliderMenuIsActive) {
                    IMGManager.getIMG(Images.gameTopEdgeLine)
                        .draw2O(
                            oSB,
                            Menu_InGame_Civ_NS.this.getPosX() + iTranslateX,
                            Menu_InGame_Civ_NS.this.getPosY()
                                - IMGManager.getIMG(Images.gameTopEdgeLine).getHeight()
                                - this.getHeightT(),
                            Menu_InGame_Civ_NS.this.getWidthM() + Core.PADDING,
                            this.getHeightT(),
                            true,
                            false
                        );
                    CFG.drawRectInfoBox_Left_Title(
                        oSB,
                        Menu_InGame_Civ_NS.this.getPosX() + iTranslateX,
                        Menu_InGame_Civ_NS.this.getPosY() - this.getHeightT(),
                        Menu_InGame_Civ_NS.this.getWidthM() - 2,
                        this.getHeightT()
                    );
                    Renderer.drawTextWithShadow(
                        oSB,
                        CFG.FONT_BOLD_SMALL,
                        this.getText(),
                        nPosX + nWidth / 2 - this.getTextWidth() / 2 + iTranslateX,
                        nPosY - this.getHeightT() + this.getHeightT() / 2 - this.getTextHeight() / 2,
                        CFG.COLOR_TEXT_CIV_INFO_TITLE
                    );
                }
            },
            0 + AoCGame.LEFT,
            0,
            tMenuW,
            tSlotH + 2,
            menuElements,
            false,
            false
        );
        this.updateLang();
    }

    /**
     * Bypasses the {@link #setVisibleM(boolean)} guard below so the bar can be
     * force-hidden when the civilization owns no spirits at all.
     */
    public void msForceVisible(boolean nVisible) {
        super.setVisibleM(nVisible);
    }

    @Override
    public void draw(SpriteBatch oSB, int iTranslateX, int iTranslateY, boolean sliderMenuIsActive) {
        if (Menu_InGame_Civ.lTime + (long)GameValues.gvInGame.MENUS_ANIMATION_TIME >= System.currentTimeMillis()) {
            if (Menu_InGame_Civ.hideAnimation) {
                iTranslateX -= (int)(
                    (float)this.getWidthM()
                        * ((float)(System.currentTimeMillis() - Menu_InGame_Civ.lTime) / (float)GameValues.gvInGame.MENUS_ANIMATION_TIME)
                );
            } else {
                iTranslateX += -this.getWidthM()
                    + (int)(
                        (float)this.getWidthM()
                            * ((float)(System.currentTimeMillis() - Menu_InGame_Civ.lTime) / (float)GameValues.gvInGame.MENUS_ANIMATION_TIME)
                    );
            }

            CFG.setRenderO(true);
        } else if (Menu_InGame_Civ.hideAnimation) {
            super.setVisibleM(false);
            return;
        }

        IMGManager.getIMG(Images.gameTopEdgeLine)
            .draw2O(
                oSB,
                this.getPosX() + iTranslateX,
                this.getPosY() - IMGManager.getIMG(Images.gameTopEdgeLine).getHeight() + iTranslateY,
                this.getWidthM() + Core.PADDING,
                this.getHeightM() + 2,
                true,
                false
            );
        this.beginClipM(oSB, iTranslateX, iTranslateY, sliderMenuIsActive);
        oSB.setColor(Color.WHITE);
        this.drawMenuM(oSB, iTranslateX, iTranslateY, sliderMenuIsActive);
        this.endClipM(oSB, iTranslateX, iTranslateY, sliderMenuIsActive);
        if (AoCGame.LEFT != 0) {
            oSB.setColor(CFG.COLOR_CREATE_NEW_GAME_BOX_PLAYERS);
            IMGManager.getIMG(Images.pix255)
                .draw2O(
                    oSB,
                    this.getPosX() + iTranslateX,
                    this.getPosY() - IMGManager.getIMG(Images.pix255).getHeight() + iTranslateY,
                    1,
                    this.getHeightM() + 2,
                    true,
                    false
                );
            oSB.setColor(Color.WHITE);
        }
    }

    @Override
    public void drawScrollPos(SpriteBatch oSB, int iTranslateX, int iTranslateY, boolean sliderMenuIsActive) {
        if (sliderMenuIsActive) {
            super.drawScrollPos(oSB, iTranslateX, iTranslateY, sliderMenuIsActive);
        }
    }

    @Override
    public void onHovered() {
        CFG.menus.setOrderOfMenu_InGame_CivInfo();
    }

    @Override
    public void actionEL(int iID) {
        this.getMenuElem(iID).actionElem(iID);
    }

    /** Mirrors the diplomacy box: hiding is driven by the slide-out animation only. */
    @Override
    public void setVisibleM(boolean visible) {
        if (visible) {
            super.setVisibleM(visible);
        }
    }
}

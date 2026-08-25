//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package age.of.civilizations2.jakowski.lukasz.Menus.Z_Rest;

import age.of.civilizations2.jakowski.lukasz.Button.Button_Close;
import age.of.civilizations2.jakowski.lukasz.Button.Button_DialogAgree;
import age.of.civilizations2.jakowski.lukasz.Button.Button_DialogDisagree;
import age.of.civilizations2.jakowski.lukasz.Button.MenuElemUI;
import age.of.civilizations2.jakowski.lukasz.CFG;
import age.of.civilizations2.jakowski.lukasz.Core.Core;
import age.of.civilizations2.jakowski.lukasz.IMGManager;
import age.of.civilizations2.jakowski.lukasz.Images;
import age.of.civilizations2.jakowski.lukasz.Menu;
import age.of.civilizations2.jakowski.lukasz.TextB.Texts.TextScrollable;
import age.of.civilizations2.jakowski.lukasz.Title.TitleM;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import team.rainfall.mingsha.config.MingshaConfig;
import team.rainfall.mingsha.utils.AnimationUtil;

import java.util.ArrayList;
import java.util.List;

public class Menu_Dialog extends Menu {
    private int iBackgroundAlpha = 5;
    private int animationStepID = 0;
    private long sinceTime = -1;
    private int animationChangePosY;
    private boolean closeMenu = false;

    public Menu_Dialog() {
        List<MenuElemUI> menuElements = new ArrayList();
        int tWidth = CFG.CIV_INFO_MENU_WIDTH * 2 + CFG.CIV_INFO_MENU_WIDTH / 2;
        if (CFG.GAMEWIDTH <= tWidth - CFG.PADD * 8) {
            tWidth = CFG.GAMEWIDTH - CFG.PADD * 8;
        }

        menuElements.add(new Button_Close(CFG.GAMEWIDTH / 2 + tWidth / 2 + CFG.PADD * 3 - IMGManager.getIMG(Images.btnClose).getWidth(), CFG.GAMEHEIGHT / 2 - CFG.BUTTON_H - IMGManager.getIMG(Images.btnClose).getHeight() / 2 + 1, IMGManager.getIMG(Images.btnClose).getWidth(), IMGManager.getIMG(Images.btnClose).getHeight()) {
            public int getPosY() {
                return Menu_Dialog.this.getMenuElem(3).getPosY();
            }
        });
        menuElements.add(new Button_DialogAgree((String) null, -1, CFG.GAMEWIDTH / 2 - CFG.PADD * 3 - tWidth / 2, CFG.GAMEHEIGHT / 2, tWidth / 2 + CFG.PADD * 3, CFG.BUTTON_H, false) {
            public int getPosY() {
                return Menu_Dialog.this.getMenuElem(3).getPosY() + Menu_Dialog.this.getMenuElem(3).getHeightE();
            }
        });
        menuElements.add(new Button_DialogDisagree((String) null, -1, CFG.GAMEWIDTH / 2, CFG.GAMEHEIGHT / 2, tWidth / 2 + CFG.PADD * 3, CFG.BUTTON_H, false) {
            public int getPosY() {
                return Menu_Dialog.this.getMenuElem(3).getPosY() + Menu_Dialog.this.getMenuElem(3).getHeightE();
            }
        });
        menuElements.add(new TextScrollable("", CFG.GAMEWIDTH / 2 - tWidth / 2, CFG.GAMEHEIGHT / 2 - CFG.BUTTON_H, tWidth, CFG.BUTTON_H, Color.WHITE, 1.0F) {
            public Color getColor(boolean isActive) {
                return isActive ? CFG.COLOR_TEXT_GRAY_LEFT_NS_ACTIVE : (this.getIsClickable() ? CFG.COLOR_TEXT_GRAY_LEFT_NS : new Color(0.78F, 0.78F, 0.78F, 0.7F));
            }
        });
        this.initMenu((TitleM) null, 0, 0, CFG.GAMEWIDTH, CFG.GAMEHEIGHT, menuElements, false, false);
        this.updateLang();
    }

    public void updateLang() {
        this.getMenuElem(1).setTextE(CFG.lang.get("Yes"));
        this.getMenuElem(2).setTextE(CFG.lang.get("No"));
    }

    public final void draw(SpriteBatch oSB, int iTranslateX, boolean sliderMenuIsActive) {
        if (sinceTime < 100) {
            sinceTime = System.currentTimeMillis();

        }
        if (this.closeMenu) {
            this.iBackgroundAlpha -= 8;
            if (this.iBackgroundAlpha <= 0) {
                this.iBackgroundAlpha = 0;
            }

            this.updateChangePosY();
        } else if (this.iBackgroundAlpha < 100) {
            this.iBackgroundAlpha += 4;
            this.updateChangePosY();
        }else {
            this.updateChangePosY();
        }
        oSB.setColor(new Color(CFG.COLOR_GRADIENT_DARK_BLUE.r, CFG.COLOR_GRADIENT_DARK_BLUE.g, CFG.COLOR_GRADIENT_DARK_BLUE.b, (float) this.iBackgroundAlpha / 255.0F));
        IMGManager.getIMG(Images.sliderGradient).drawO(oSB, this.getPosX() + iTranslateX, this.getPosY() - IMGManager.getIMG(Images.sliderGradient).getHeight(), CFG.CIV_INFO_MENU_WIDTH, this.getHeightM());
        IMGManager.getIMG(Images.sliderGradient).drawO(oSB, this.getPosX() + this.getWidthM() - CFG.CIV_INFO_MENU_WIDTH + iTranslateX, this.getPosY() - IMGManager.getIMG(Images.sliderGradient).getHeight(), CFG.CIV_INFO_MENU_WIDTH, this.getHeightM(), true, false);
        IMGManager.getIMG(Images.gradient).drawO(oSB, this.getPosX() + iTranslateX, this.getPosY() - IMGManager.getIMG(Images.gradient).getHeight(), this.getWidthM(), CFG.CIV_INFO_MENU_WIDTH);
        IMGManager.getIMG(Images.gradient).drawO(oSB, this.getPosX() + iTranslateX, this.getPosY() + this.getHeightM() - CFG.CIV_INFO_MENU_WIDTH - IMGManager.getIMG(Images.gradient).getHeight(), this.getWidthM(), CFG.CIV_INFO_MENU_WIDTH, false, true);
        oSB.setColor(new Color(0.0125F, 0.0125F, 0.0125F, (float) this.iBackgroundAlpha * 1.45F / 255.0F));
        IMGManager.getIMG(Images.pattern).drawO(oSB, iTranslateX, -IMGManager.getIMG(Images.pix255).getHeight(), CFG.GAMEWIDTH, CFG.GAMEHEIGHT, 0.0F, 0);
        oSB.setColor(new Color(0.1F, 0.1F, 0.1F, 0.3F));
        IMGManager.getIMG(Images.pattern).drawO(oSB, iTranslateX, -IMGManager.getIMG(Images.pattern).getHeight(), CFG.GAMEWIDTH, CFG.GAMEHEIGHT, 0.0F, 0);
        oSB.setColor(new Color(1.0F, 1.0F, 1.0F, (float) this.iBackgroundAlpha * 0.85F / 255.0F));
        IMGManager.getIMG(Images.gameLogo).drawO(oSB, this.getPosX() + CFG.PADD * 2 + iTranslateX, this.getPosY() + this.getHeightM() - IMGManager.getIMG(Images.gameLogo).getHeight() - CFG.PADD * 2);
        oSB.setColor(Color.WHITE);
        IMGManager.getIMG(Images.dialog_title).draw2O(oSB, this.getMenuElem(3).getPosXE() - CFG.PADD * 3 + iTranslateX, this.getMenuPosY() + this.getMenuElem(3).getPosY() - Core.PADDING - IMGManager.getIMG(Images.dialog_title).getHeight() + this.animationChangePosY, this.getMenuElem(3).getWidthE() + CFG.PADD * 6 - IMGManager.getIMG(Images.dialog_title).getWidth(), this.getMenuElem(3).getHeightE() + Core.PADDING);
        IMGManager.getIMG(Images.dialog_title).draw2O(oSB, this.getMenuElem(3).getPosXE() - CFG.PADD * 3 + this.getMenuElem(3).getWidthE() + CFG.PADD * 6 - IMGManager.getIMG(Images.dialog_title).getWidth() + iTranslateX, this.getMenuElem(3).getPosY() - Core.PADDING - IMGManager.getIMG(Images.dialog_title).getHeight() + this.animationChangePosY, IMGManager.getIMG(Images.dialog_title).getWidth(), this.getMenuElem(3).getHeightE() + Core.PADDING, true, false);
        oSB.setColor(new Color(CFG.COLOR_GRADIENT_BLUE.r, CFG.COLOR_GRADIENT_BLUE.g, CFG.COLOR_GRADIENT_BLUE.b, 0.55F));
        IMGManager.getIMG(Images.gradient).drawO(oSB, this.getMenuElem(3).getPosXE() - CFG.PADD * 3 + 2 + iTranslateX, this.getMenuPosY() + this.getMenuElem(3).getPosY() - IMGManager.getIMG(Images.gradient).getHeight() + this.getMenuElem(3).getHeightE() - this.getMenuElem(3).getHeightE() * 3 / 5 + this.animationChangePosY, this.getMenuElem(3).getWidthE() + CFG.PADD * 6 - 4, this.getMenuElem(3).getHeightE() * 3 / 5, false, true);
        oSB.setColor(0.0F, 0.0F, 0.0F, 0.65F);
        IMGManager.getIMG(Images.gradient).drawO(oSB, this.getMenuElem(3).getPosXE() - CFG.PADD * 3 + 2 + iTranslateX, this.getMenuPosY() + this.getMenuElem(3).getPosY() - IMGManager.getIMG(Images.gradient).getHeight() + this.getMenuElem(3).getHeightE() - CFG.PADD * 2 + this.animationChangePosY, this.getMenuElem(3).getWidthE() + CFG.PADD * 6 - 4, CFG.PADD * 2, false, true);
        oSB.setColor(0.0F, 0.0F, 0.0F, 0.65F);
        IMGManager.getIMG(Images.pix255).draw2O(oSB, this.getMenuElem(3).getPosXE() + 2 - CFG.PADD * 3 + iTranslateX, this.getMenuPosY() + this.getMenuElem(3).getHeightE() + this.getMenuElem(3).getPosY() - 2 - IMGManager.getIMG(Images.pix255).getHeight() + this.animationChangePosY, this.getMenuElem(3).getWidthE() + CFG.PADD * 6 - 4, 1);
        oSB.setColor(CFG.COLOR_NEW_GAME_EDGE_LINE);
        IMGManager.getIMG(Images.pix255).draw2O(oSB, this.getMenuElem(3).getPosXE() + 2 - CFG.PADD * 3 + iTranslateX, this.getMenuPosY() + this.getMenuElem(3).getHeightE() + this.getMenuElem(3).getPosY() - 1 - IMGManager.getIMG(Images.pix255).getHeight() + this.animationChangePosY, this.getMenuElem(3).getWidthE() + CFG.PADD * 6 - 4, 1);
        oSB.setColor(new Color(CFG.COLOR_GRADIENT_DARK_BLUE.r, CFG.COLOR_GRADIENT_DARK_BLUE.g, CFG.COLOR_GRADIENT_DARK_BLUE.b, 0.55F));
        IMGManager.getIMG(Images.line32Off1).drawO(oSB, this.getMenuElem(3).getPosXE() + 2 - CFG.PADD * 3 + iTranslateX, this.getMenuPosY() + this.getMenuElem(3).getHeightE() + this.getMenuElem(3).getPosY() - 1 - IMGManager.getIMG(Images.line32Off1).getHeight() + this.animationChangePosY, this.getMenuElem(3).getWidthE() + CFG.PADD * 6 - 4, 1);
        oSB.setColor(Color.WHITE);
        super.drawMenuElements(oSB, iTranslateX, this.animationChangePosY, sliderMenuIsActive);
    }

    public final void actionEL(int iID) {
        switch (iID) {
            case 0:
                this.getMenuElem(1).setClickable(false);
                this.getMenuElem(2).setClickable(false);
                this.closeMenu();
                break;
            case 1:
                this.getMenuElem(1).setClickable(false);
                this.getMenuElem(2).setClickable(false);
                CFG.dialog_True();
                this.closeMenu();
                break;
            case 2:
                this.getMenuElem(1).setClickable(false);
                this.getMenuElem(2).setClickable(false);
                CFG.dialog_False();
                this.closeMenu();
                break;
            case 3:
                CFG.toastM.addM(this.getMenuElem(iID).getTextE());
        }

    }

    public final void onBackPressed() {
        this.closeMenu();
    }

    private final void updateChangePosY() {
        if (MingshaConfig.isNewDialogAnimation()) {
            if (this.closeMenu && this.animationStepID == 13) {
                this.animationChangePosY = CFG.GAMEHEIGHT / 2 + CFG.BUTTON_H;
                this.setVisibleM(false);
            }
            if (System.currentTimeMillis() > sinceTime + 300) {
                this.animationStepID = 13;
                this.animationChangePosY = this.closeMenu ? CFG.GAMEHEIGHT / 2 + CFG.BUTTON_H : 0;
                if(closeMenu){
                    this.setVisibleM(false);
                }
                return;
            }

            float base = (float) CFG.GAMEHEIGHT / 2 + CFG.BUTTON_H;
            float startPos = this.closeMenu ? 0 : base;
            float endPos = this.closeMenu ? base : 0;
            float progress = (float) Math.min(AnimationUtil.easeOut(System.currentTimeMillis(), sinceTime + 300, sinceTime), 1.0f);
            this.animationChangePosY = (int) (startPos + (endPos - startPos) * progress);
            return;
        }
        switch (this.animationStepID) {
            case 0:
            case 1:
            case 12:
                this.animationChangePosY = (int) ((float) this.animationChangePosY - (float) (CFG.GAMEHEIGHT / 2 + CFG.BUTTON_H) * 2.5F / 100.0F * (float) (this.closeMenu ? -1 : 1));
                break;
            case 2:
            case 3:
            case 10:
            case 11:
                this.animationChangePosY = (int) ((float) this.animationChangePosY - (float) (CFG.GAMEHEIGHT / 2 + CFG.BUTTON_H) * 5.0F / 100.0F * (float) (this.closeMenu ? -1 : 1));
                break;
            case 4:
            case 5:
            case 8:
            case 9:
                this.animationChangePosY = (int) ((float) this.animationChangePosY - (float) (CFG.GAMEHEIGHT / 2 + CFG.BUTTON_H) * 10.0F / 100.0F * (float) (this.closeMenu ? -1 : 1));
                break;
            case 6:
            case 7:
                this.animationChangePosY = (int) ((float) this.animationChangePosY - (float) (CFG.GAMEHEIGHT / 2 + CFG.BUTTON_H) * 15.0F / 100.0F * (float) (this.closeMenu ? -1 : 1));
                break;
            case 13:
                this.animationChangePosY = 0;
        }

        if (CFG.iNumOfFPS < 22) {
            this.animationStepID = 13;
            this.animationChangePosY = 0;
        }

        if (this.closeMenu && this.animationStepID == 13) {
            this.animationChangePosY = CFG.GAMEHEIGHT / 2 + CFG.BUTTON_H;
            this.setVisibleM(false);
        }

        ++this.animationStepID;
        CFG.setRenderO(true);
    }

    private final void closeMenu() {
        this.closeMenu = true;
        this.resetAnimation();
    }

    public final void setVisibleM(boolean visible) {
        super.setVisibleM(visible);
        this.closeMenu = !visible;
        this.iBackgroundAlpha = 5;
        this.resetAnimation();
    }

    private final void resetAnimation() {
        this.animationStepID = 0;
        this.sinceTime = -1;
        if (!this.closeMenu) {
            this.animationChangePosY = CFG.GAMEHEIGHT / 2 + CFG.BUTTON_H;
        }

    }
}

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package age.of.civilizations2.jakowski.lukasz.Menus.Decisions.Missions;

import age.of.civilizations2.jakowski.lukasz.Button.Diplomacy.Action.Button_DiplomacyAction;
import age.of.civilizations2.jakowski.lukasz.Button.Diplomacy.Action.Button_DiplomacyAction_TextRight;
import age.of.civilizations2.jakowski.lukasz.Button.Flag.Button_InGameAction;
import age.of.civilizations2.jakowski.lukasz.Button.MenuElemUI;
import age.of.civilizations2.jakowski.lukasz.*;
import age.of.civilizations2.jakowski.lukasz.Core.Core;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.*;
import age.of.civilizations2.jakowski.lukasz.Menus.Civilization.Menu_InGame_Civ_Decisions;
import age.of.civilizations2.jakowski.lukasz.Menus.Relations.Actions.Menu_InGameOfferAlliance;
import age.of.civilizations2.jakowski.lukasz.TextB.Texts.TextBuildTitle;
import age.of.civilizations2.jakowski.lukasz.TextB.Texts.TextScale;
import age.of.civilizations2.jakowski.lukasz.Title.TitleM_TextSmall;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public class Menu_InGame_Missions extends Menu {
    public static int nCivID = 0;

    public Menu_InGame_Missions() {
        List<MenuElemUI> menuElements = new ArrayList();
        int tempWidth = CFG.CIV_INFO_MENU_WIDTH * 2;
        int tY = 0;
        nCivID = CFG.core.getPlayer(CFG.PLAYER_TURN_ID).getCivId();
        menuElements.add(new Button_InGameAction(CFG.lang.get("Close"), -1, CFG.PADD, tY, CFG.BUTTON_W, true) {
            public int getWidthE() {
                return Menu_InGame_Missions.this.getW2() - CFG.PADD * 2;
            }

            public void actionElem(int iID) {
                Menu_InGame_Missions.this.setVisibleM(false);
            }
        });
        tY += menuElements.get(0).getHeightE() + CFG.PADD;
        int tempElemH = Menu_InGame_Civ_Decisions.getButtonH();

        try {
            if (!CFG.core.getCiv(nCivID).iDMAS.isEmpty()) {
                menuElements.add(new TextBuildTitle(CFG.lang.get("Missions") + ": " + CFG.core.getCiv(CFG.core.getPlayer(CFG.PLAYER_TURN_ID).getCivId()).getCivName(), -1, 0, tY, tempWidth, CFG.TEXT_HEIGHT_DEFAULT + CFG.PADD * 4) {
                    public Color getColor(boolean isActive) {
                        return isActive ? CFG.COLOR_TEXT_GRAY_NS_HOVER : (this.getIsClickable() ? (this.getIsHovered() ? CFG.COLOR_TEXT_GRAY_NS : Color.WHITE) : new Color(0.78F, 0.78F, 0.78F, 0.7F));
                    }

                    public int getWidthE() {
                        return Menu_InGame_Missions.this.getW2();
                    }
                });
                tY += menuElements.get(menuElements.size() - 1).getHeightE();
                int completed = 0;
                int i = 0;
                int added = 0;

                for(int iSize = CFG.core.getCiv(nCivID).iDMAS.size(); i < iSize; ++i) {
                    try {
                        if (!CFG.eventsManager.events.lEvents.get(CFG.core.getCiv(nCivID).iDMAS.get(i)).getRepeatable() && CFG.eventsManager.events.lEvents.get(CFG.core.getCiv(nCivID).iDMAS.get(i)).getWasFired()) {
                            ++completed;
                        } else if (CFG.eventsManager.canDisplayMissionID(CFG.core.getCiv(nCivID).iDMAS.get(i), CFG.core.getPlayer(CFG.PLAYER_TURN_ID).getCivId())) {
                            menuElements.add(new Button_DiplomacyAction_TextRight(Images.diploMessage, CFG.lang.get(CFG.eventsManager.events.lEvents.get(CFG.core.getCiv(nCivID).iDMAS.get(i)).getEventName()), 0, 0, tY, CFG.BUTTON_W, tempElemH, true, "", CFG.eventsManager.canRunMissionID(CFG.core.getCiv(nCivID).iDMAS.get(i), CFG.core.getPlayer(CFG.PLAYER_TURN_ID).getCivId()) ? Images.iconTrue : Images.iconFalse) {
                                public int id = 0;

                                public void actionElem(int iID) {
                                    if(CFG.menus.getVisibleInGame_Event()){
                                        CFG.toastM.addM(CFG.lang.get("NotAllowedToRunBeforeDecide"),CFG.COLOR_MOVEMENT_ZERO_HOVER);
                                        return;
                                    }
                                    if (CFG.eventsManager.runMissionPlayer(CFG.core.getCiv(Menu_InGame_Missions.nCivID).iDMAS.get(this.id), CFG.core.getPlayer(CFG.PLAYER_TURN_ID).getCivId())) {
                                        Menu_InGame_Missions.this.setVisibleM(false);
                                    }

                                }

                                public int getWidthE() {
                                    return Menu_InGame_Missions.this.getW2();
                                }

                                public void setCurr(int nCurrent) {
                                    this.id = nCurrent;
                                }

                                public void setMin(int iMin) {
                                    this.row = iMin % 2 == 1;
                                }

                                public void buildElemHover() {
                                    List<MEHover_2E> nElements = new ArrayList();
                                    List<ME_Hover_2Type> nData = new ArrayList();
                                    nData.add(new ME_Hover_2Type_Flag_Big(CFG.core.getPlayer(CFG.PLAYER_TURN_ID).getCivId(), 0, CFG.PADD));
                                    nData.add(new ME_Hover_2Type_Text_Big(CFG.lang.get("Mission") + ": "));
                                    nData.add(new ME_Hover_2Type_Text_Big(CFG.lang.get(CFG.eventsManager.events.lEvents.get(CFG.core.getCiv(Menu_InGame_Missions.nCivID).iDMAS.get(this.id)).getEventName()), CFG.COLOR_HOVER_TITLE));
                                    nData.add(new ME_Hover_2Type_Image_Big(Images.diploMessage, CFG.PADD, 0));
                                    nElements.add(new MEHover_2E(nData));
                                    nData.clear();
                                    if (CFG.eventsManager.events.lEvents.get(CFG.core.getCiv(Menu_InGame_Missions.nCivID).iDMAS.get(this.id)).missionDesc != null && CFG.eventsManager.events.lEvents.get(CFG.core.getCiv(Menu_InGame_Missions.nCivID).iDMAS.get(this.id)).missionDesc.length() > 0) {
                                        nData.add(new ME_Hover_2Type_Space());
                                        nElements.add(new MEHover_2E(nData));
                                        nData.clear();
                                        nData.add(new ME_Hover_2Type_TextDesc(CFG.lang.get(CFG.eventsManager.events.lEvents.get(CFG.core.getCiv(Menu_InGame_Missions.nCivID).iDMAS.get(this.id)).missionDesc)));
                                        nElements.add(new MEHover_2E(nData));
                                        nData.clear();
                                    }

                                    this.menuElemHover = new ME_Hover_v2(nElements);
                                }
                            });
                            menuElements.get(menuElements.size() - 1).setCurr(i);
                            menuElements.get(menuElements.size() - 1).setMin(added++);
                            tY += menuElements.get(menuElements.size() - 1).getHeightE();
                        }
                    } catch (Exception ex) {
                        CFG.exceptionStack(ex);
                    }
                }

                if (menuElements.size() == 2) {
                    menuElements.add(new TextScale(CFG.lang.get("None"), -1, 2, tY, tempWidth - 4, CFG.BUTTON_H * 3 / 4, 0.75F) {
                        public int getWidthE() {
                            return Menu_InGame_Missions.this.getElementW() * 2;
                        }
                    });
                    menuElements.get(menuElements.size() - 1).setClickable(false);
                    tY += menuElements.get(menuElements.size() - 1).getHeightE();
                }

                if (completed > 0) {
                    menuElements.add(new TextBuildTitle(CFG.lang.get("Missions") + ": " + CFG.lang.get("Completed"), -1, 0, tY, tempWidth, CFG.TEXT_HEIGHT_DEFAULT + CFG.PADD * 4) {
                        public Color getColor(boolean isActive) {
                            return isActive ? CFG.COLOR_TEXT_GRAY_NS_HOVER : (this.getIsClickable() ? (this.getIsHovered() ? CFG.COLOR_TEXT_GRAY_NS : Color.WHITE) : new Color(0.78F, 0.78F, 0.78F, 0.7F));
                        }

                        public int getWidthE() {
                            return Menu_InGame_Missions.this.getW2();
                        }
                    });
                    tY += menuElements.get(menuElements.size() - 1).getHeightE();
                    i = 0;
                    added = 0;

                    for(int iSize = CFG.core.getCiv(nCivID).iDMAS.size(); i < iSize; ++i) {
                        try {
                            if (!CFG.eventsManager.events.lEvents.get(CFG.core.getCiv(nCivID).iDMAS.get(i)).getRepeatable() && CFG.eventsManager.events.lEvents.get(CFG.core.getCiv(nCivID).iDMAS.get(i)).getWasFired()) {
                                menuElements.add(new Button_DiplomacyAction(Images.iconTrue, CFG.lang.get(CFG.eventsManager.events.lEvents.get(CFG.core.getCiv(nCivID).iDMAS.get(i)).getEventName()), 0, 0, tY, CFG.BUTTON_W, tempElemH, true) {
                                    public int id = 0;

                                    public void actionElem(int iID) {
                                    }

                                    public int getWidthE() {
                                        return Menu_InGame_Missions.this.getW2();
                                    }

                                    public void setCurr(int nCurrent) {
                                        this.id = nCurrent;
                                    }

                                    public void setMin(int iMin) {
                                        this.row = iMin % 2 == 1;
                                    }

                                    public void buildElemHover() {
                                        List<MEHover_2E> nElements = new ArrayList();
                                        List<ME_Hover_2Type> nData = new ArrayList();
                                        nData.add(new ME_Hover_2Type_Flag_Big(CFG.core.getPlayer(CFG.PLAYER_TURN_ID).getCivId(), 0, CFG.PADD));
                                        nData.add(new ME_Hover_2Type_Text_Big(CFG.lang.get("Mission") + ": "));
                                        nData.add(new ME_Hover_2Type_Text_Big(CFG.lang.get(CFG.eventsManager.events.lEvents.get(CFG.core.getCiv(Menu_InGame_Missions.nCivID).iDMAS.get(this.id)).getEventName()), CFG.COLOR_HOVER_TITLE));
                                        nData.add(new ME_Hover_2Type_Image_Big(Images.diploMessage, CFG.PADD, 0));
                                        nElements.add(new MEHover_2E(nData));
                                        nData.clear();
                                        if (CFG.eventsManager.events.lEvents.get(CFG.core.getCiv(Menu_InGame_Missions.nCivID).iDMAS.get(this.id)).missionDesc != null && CFG.eventsManager.events.lEvents.get(CFG.core.getCiv(Menu_InGame_Missions.nCivID).iDMAS.get(this.id)).missionDesc.length() > 0) {
                                            nData.add(new ME_Hover_2Type_Space());
                                            nElements.add(new MEHover_2E(nData));
                                            nData.clear();
                                            nData.add(new ME_Hover_2Type_TextDesc(CFG.lang.get(CFG.eventsManager.events.lEvents.get(CFG.core.getCiv(Menu_InGame_Missions.nCivID).iDMAS.get(this.id)).missionDesc)));
                                            nElements.add(new MEHover_2E(nData));
                                            nData.clear();
                                        }

                                        if (CFG.eventsManager.events.lEvents.get(CFG.core.getCiv(Menu_InGame_Missions.nCivID).iDMAS.get(this.id)).getEvent_PopUp().sText != null && CFG.eventsManager.events.lEvents.get(CFG.core.getCiv(Menu_InGame_Missions.nCivID).iDMAS.get(this.id)).getEvent_PopUp().sText.length() > 0) {
                                            nData.add(new ME_Hover_2Type_Space());
                                            nElements.add(new MEHover_2E(nData));
                                            nData.clear();
                                            nData.add(new ME_Hover_2Type_TextDesc(CFG.lang.get(CFG.eventsManager.events.lEvents.get(CFG.core.getCiv(Menu_InGame_Missions.nCivID).iDMAS.get(this.id)).getEvent_PopUp().sText)));
                                            nElements.add(new MEHover_2E(nData));
                                            nData.clear();
                                        }

                                        this.menuElemHover = new ME_Hover_v2(nElements);
                                    }
                                });
                                menuElements.get(menuElements.size() - 1).setCurr(i);
                                menuElements.get(menuElements.size() - 1).setMin(added++);
                                tY += menuElements.get(menuElements.size() - 1).getHeightE();
                            } else {
                                ++completed;
                            }
                        } catch (Exception ex) {
                            CFG.exceptionStack(ex);
                        }
                    }
                }
            } else {
                menuElements.add(new TextScale(CFG.lang.get("None"), -1, 2, tY, tempWidth - 4, CFG.BUTTON_H * 3 / 4, 0.75F) {
                    public int getWidthE() {
                        return Menu_InGame_Missions.this.getElementW() * 2;
                    }
                });
                menuElements.get(menuElements.size() - 1).setClickable(false);
                int var21 = tY + menuElements.get(menuElements.size() - 1).getHeightE();
            }
        } catch (Exception exr) {
            CFG.exceptionStack(exr);
        }

        int shownMissions = 0;

        try {
            for(int i = 0, iSize = CFG.core.getCiv(nCivID).iDMAS.size(); i < iSize; ++i) {
                if (!CFG.eventsManager.events.lEvents.get(CFG.core.getCiv(nCivID).iDMAS.get(i)).getRepeatable() && CFG.eventsManager.events.lEvents.get(CFG.core.getCiv(nCivID).iDMAS.get(i)).getWasFired()) {
                    ++shownMissions;
                } else if (CFG.eventsManager.canDisplayMissionID(CFG.core.getCiv(nCivID).iDMAS.get(i), CFG.core.getPlayer(CFG.PLAYER_TURN_ID).getCivId())) {
                    ++shownMissions;
                }
            }
        } catch (Exception ex) {
            CFG.exceptionStack(ex);
        }

        int tempMenuPosY = IMGManager.getIMG(Images.topBar).getHeight() + CFG.PADD * 2 + CFG.BUTTON_H * 3 / 4;
        this.initMenu(new TitleM_TextSmall(CFG.lang.get("Missions") + ": " + CFG.getNumberWthSpaces("" + shownMissions), CFG.BUTTON_H * 3 / 4, true, true) {
            public void drawT(SpriteBatch oSB, int iTranslateX, int nPosX, int nPosY, int nWidth, boolean sliderMenuIsActive) {
                IMGManager.getIMG(Images.dialog_title).draw2O(oSB, nPosX - 2 - Core.PADDING + iTranslateX, nPosY - this.getHeightT() - IMGManager.getIMG(Images.dialog_title).getHeight() - Core.PADDING, nWidth + Core.PADDING * 2 + 4 - IMGManager.getIMG(Images.dialog_title).getWidth(), this.getHeightT() + Core.PADDING);
                IMGManager.getIMG(Images.dialog_title).draw2O(oSB, nPosX + nWidth + Core.PADDING + 2 - IMGManager.getIMG(Images.dialog_title).getWidth() + iTranslateX, nPosY - this.getHeightT() - Core.PADDING - IMGManager.getIMG(Images.dialog_title).getHeight(), IMGManager.getIMG(Images.dialog_title).getWidth(), this.getHeightT() + Core.PADDING, true, false);
                oSB.setColor(new Color(0.0F, 0.29803923F, 0.43137255F, 0.165F));
                IMGManager.getIMG(Images.line32Off1).drawO(oSB, nPosX + iTranslateX, nPosY - this.getHeightT() + 2 - IMGManager.getIMG(Images.line32Off1).getHeight(), nWidth, this.getHeightT() - 2, false, true);
                oSB.setColor(new Color(0.0F, 0.29803923F, 0.43137255F, 0.375F));
                IMGManager.getIMG(Images.gradient).drawO(oSB, nPosX + iTranslateX, nPosY - this.getHeightT() * 2 / 3 - IMGManager.getIMG(Images.gradient).getHeight(), nWidth, this.getHeightT() * 2 / 3, false, true);
                oSB.setColor(new Color(0.0F, 0.0F, 0.0F, 0.65F));
                IMGManager.getIMG(Images.gradient).drawO(oSB, nPosX + iTranslateX, nPosY - CFG.PADD - IMGManager.getIMG(Images.gradient).getHeight(), nWidth, CFG.PADD, false, true);
                oSB.setColor(CFG.COLOR_NEW_GAME_EDGE_LINE);
                IMGManager.getIMG(Images.pix255).drawO(oSB, nPosX + iTranslateX, nPosY - 1 - IMGManager.getIMG(Images.pix255).getHeight(), nWidth, 1);
                oSB.setColor(new Color(0.0F, 0.0F, 0.0F, 0.55F));
                IMGManager.getIMG(Images.line32Off1).drawO(oSB, nPosX + iTranslateX, nPosY - 2 - IMGManager.getIMG(Images.line32Off1).getHeight(), nWidth, 1);
                oSB.setColor(new Color(0.0F, 0.0F, 0.0F, 0.8F));
                IMGManager.getIMG(Images.line32Off1).drawO(oSB, nPosX + iTranslateX, nPosY - 1 - IMGManager.getIMG(Images.line32Off1).getHeight(), nWidth, 1);
                oSB.setColor(new Color(CFG.COLOR_FLAG_FRAME.r, CFG.COLOR_FLAG_FRAME.g, CFG.COLOR_FLAG_FRAME.b, 0.45F));
                IMGManager.getIMG(Images.sliderGradient).drawO(oSB, nPosX + iTranslateX, nPosY - 1 - IMGManager.getIMG(Images.sliderGradient).getHeight(), nWidth / 2, 1);
                IMGManager.getIMG(Images.sliderGradient).drawO(oSB, nPosX + nWidth - nWidth / 2 + iTranslateX, nPosY - 1 - IMGManager.getIMG(Images.sliderGradient).getHeight(), nWidth / 2, 1, true, false);
                oSB.setColor(Color.WHITE);
                CFG.core.getCiv(CFG.core.getPlayer(CFG.PLAYER_TURN_ID).getCivId()).getFlagC().drawO(oSB, Menu_InGame_Missions.this.getPosX() + CFG.PADD * 2 + iTranslateX, Menu_InGame_Missions.this.getPosY() - this.getHeightT() / 2 - CFG.CIV_FLAG_HEIGHT / 2 - CFG.core.getCiv(CFG.core.getPlayer(CFG.PLAYER_TURN_ID).getCivId()).getFlagC().getHeight(), CFG.CIV_FLAG_WIDTH, CFG.CIV_FLAG_HEIGHT);
                IMGManager.getIMG(Images.flagRectSmall).drawO(oSB, Menu_InGame_Missions.this.getPosX() + CFG.PADD * 2 + iTranslateX, Menu_InGame_Missions.this.getPosY() - this.getHeightT() / 2 - CFG.CIV_FLAG_HEIGHT / 2);
                Renderer.drawText(oSB, CFG.FONT_BOLD_SMALL, this.getText(), nPosX + (nWidth - this.getTextWidth()) / 2 + iTranslateX, 2 + nPosY - this.getHeightT() + (this.getHeightT() - this.getTextHeight()) / 2, Color.WHITE);
            }
        }, CFG.GAMEWIDTH / 2 - tempWidth / 2, tempMenuPosY, tempWidth, menuElements.get(menuElements.size() - 1).getPosY() + menuElements.get(menuElements.size() - 1).getHeightE() + CFG.PADD + tempMenuPosY > CFG.GAMEHEIGHT - CFG.BUTTON_H - CFG.PADD * 2 ? Math.max(CFG.GAMEHEIGHT - CFG.BUTTON_H - CFG.PADD * 2 - tempMenuPosY, (CFG.TEXT_HEIGHT_DEFAULT + CFG.PADD * 2) * 6) : menuElements.get(menuElements.size() - 1).getPosY() + menuElements.get(menuElements.size() - 1).getHeightE() + CFG.PADD, menuElements, true, true);
        this.updateLang();
        Menu_InGameOfferAlliance.lTime = System.currentTimeMillis();
    }

    public void updateLang() {
    }

    public void draw(SpriteBatch oSB, int iTranslateX, int iTranslateY, boolean sliderMenuIsActive) {
        oSB.setColor(Color.WHITE);
        IMGManager.getIMG(Images.gameTopEdge).draw2O(oSB, this.getPosX() - 2 - Core.PADDING + iTranslateX, this.getPosY() - IMGManager.getIMG(Images.gameTopEdge).getHeight() + iTranslateY, this.getWidthM() - IMGManager.getIMG(Images.gameTopEdge).getWidth() + Core.PADDING * 2 + 4, this.getHeightM() + CFG.PADD + Core.PADDING, false, true);
        IMGManager.getIMG(Images.gameTopEdge).draw2O(oSB, this.getPosX() + 2 + Core.PADDING + this.getWidthM() - IMGManager.getIMG(Images.gameTopEdge).getWidth() + iTranslateX, this.getPosY() - IMGManager.getIMG(Images.gameTopEdge).getHeight() + iTranslateY, IMGManager.getIMG(Images.gameTopEdge).getWidth(), this.getHeightM() + CFG.PADD + Core.PADDING, true, true);
        oSB.setColor(new Color(0.0F, 0.0F, 0.0F, 0.45F));
        IMGManager.getIMG(Images.gradient).drawO(oSB, this.getPosX() + 2 + iTranslateX, this.getPosY() - IMGManager.getIMG(Images.gradient).getHeight() + iTranslateY, this.getWidthM() - 4, this.getHeightM() / 4);
        IMGManager.getIMG(Images.pix255).drawO(oSB, this.getPosX() + 2 + iTranslateX, this.getPosY() - IMGManager.getIMG(Images.pix255).getHeight() + iTranslateY, this.getWidthM() - 4, 1);
        oSB.setColor(Color.WHITE);
        this.beginClipM(oSB, iTranslateX, iTranslateY, sliderMenuIsActive);
        this.drawMenuM(oSB, iTranslateX, iTranslateY, sliderMenuIsActive);
        oSB.setColor(Color.WHITE);
        this.endClipM(oSB, iTranslateX, iTranslateY, sliderMenuIsActive);
    }

    public void drawScrollPos(SpriteBatch oSB, int iTranslateX, int iTranslateY, boolean sliderMenuIsActive) {
        if (sliderMenuIsActive) {
            super.drawScrollPos(oSB, iTranslateX, iTranslateY, sliderMenuIsActive);
        }

    }

    public final int getW() {
        return this.getWidthM() - 4;
    }

    public final int getElementW() {
        return this.getW() / 2;
    }

    public final int getW2() {
        return this.getWidthM();
    }
}

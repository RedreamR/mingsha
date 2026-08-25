package age.of.civilizations2.jakowski.lukasz.Menus.NewGame;

import age.of.civilizations2.jakowski.lukasz.CFG;
import age.of.civilizations2.jakowski.lukasz.Colors;
import age.of.civilizations2.jakowski.lukasz.IMGManager;
import age.of.civilizations2.jakowski.lukasz.Images;
import age.of.civilizations2.jakowski.lukasz.MapScale;
import age.of.civilizations2.jakowski.lukasz.Menu;
import age.of.civilizations2.jakowski.lukasz.Province;
import age.of.civilizations2.jakowski.lukasz.RTS;
import age.of.civilizations2.jakowski.lukasz.SFXManager;
import age.of.civilizations2.jakowski.lukasz.Start_The_Game_Data;
import age.of.civilizations2.jakowski.lukasz.Touch;
import age.of.civilizations2.jakowski.lukasz.View;
import age.of.civilizations2.jakowski.lukasz.Button.MenuElemUI;
import age.of.civilizations2.jakowski.lukasz.Button.Game.Button_Game;
import age.of.civilizations2.jakowski.lukasz.Button.Game.Button_Game_ExtraText;
import age.of.civilizations2.jakowski.lukasz.Button.Game.Button_Game_PLAY;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.MEHover_2E;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.ME_Hover_2Type;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.ME_Hover_2Type_Flag_Big;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.ME_Hover_2Type_Ideology_Big;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.ME_Hover_2Type_Image;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.ME_Hover_2Type_Image_Big;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.ME_Hover_2Type_Religion_Big;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.ME_Hover_2Type_Space;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.ME_Hover_2Type_Text;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.ME_Hover_2Type_TextDesc;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.ME_Hover_2Type_Text_Big;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.ME_Hover_v2;
import age.of.civilizations2.jakowski.lukasz.Menus.CivN.Menu_CreateNewGame_AddCiv;
import age.of.civilizations2.jakowski.lukasz.Menus.Options.Menu_CreateNewGame_Options2;
import age.of.civilizations2.jakowski.lukasz.Save.Save_Civ_GameData;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;
import java.util.List;

public class Menu_CreateNewGame extends Menu {
   public static int CHALLENGE_MODE_NG = -1;

   public Menu_CreateNewGame() {
      List<MenuElemUI> menuElements = new ArrayList<>();
      if (CFG.getIsDesktop()) {
         menuElements.add(new Button_Game_PLAY(null, -1, CFG.GAMEWIDTH - CFG.BUTTON_W * 2 - CFG.PADD, CFG.PADD, CFG.BUTTON_W * 2, true) {
            @Override public Color getColorE(boolean isActive) {
               return isActive ? CFG.COLOR_BUTTON_GAME_TEXT_IMPORTANT_ACTIVE : (this.getIsClickable() ? (this.getIsHovered() ? CFG.COLOR_BUTTON_GAME_TEXT_IMPORTANT_HOVER : CFG.COLOR_HOVER_TITLE) : CFG.COLOR_BUTTON_GAME_TEXT_NOT_CLICKABLE);
            }
            @Override public void buildElemHover() { buildPlayHover(this, false); }
            @Override public void drawMEH2(SpriteBatch oSB, int iTranslateX, int iTranslateY, boolean isActive) { drawPlayHover(this, oSB); }
         });
      } else {
         menuElements.add(new Button_Game_PLAY(
               null, -1, CFG.GAMEWIDTH - CFG.BUTTON_W * 2 - CFG.PADD, CFG.PADD, CFG.BUTTON_W * 2, true
         ) {
            @Override public Color getColorE(boolean isActive) {
               return isActive ? CFG.COLOR_BUTTON_GAME_TEXT_IMPORTANT_ACTIVE : (this.getIsClickable() ? (this.getIsHovered() ? CFG.COLOR_BUTTON_GAME_TEXT_IMPORTANT_HOVER : Colors.COLOR_TOP_STATS3) : CFG.COLOR_BUTTON_GAME_TEXT_NOT_CLICKABLE);
            }
            @Override public void buildElemHover() { buildPlayHover(this, true); }
            @Override public void drawMEH2(SpriteBatch oSB, int iTranslateX, int iTranslateY, boolean isActive) { drawPlayHover(this, oSB); }
         });
      }

      menuElements.add(new Button_Game_ExtraText(null, -1, CFG.PADD * 2 + CFG.BUTTON_W, CFG.PADD, CFG.BUTTON_W + CFG.BUTTON_W / 2, true) {
         @Override public void updateText() {
            if (this.lastActiveProvinceID != 50) {
               this.lastActiveProvinceID = 50;
               this.textB = "" + Menu_CreateNewGame_Options2.NUM_OF_OPTIONS;
               CFG.glyphLay.setText(CFG.fontMain.get(this.fontID2), this.textB);
               this.iTextBWidth = (int) CFG.glyphLay.width;
            }
         }
         @Override public void drawButtonBGE(SpriteBatch oSB, int iTranslateX, int iTranslateY, boolean isActive) {
            super.drawButtonBGE(oSB, iTranslateX, iTranslateY, isActive);
            if (isActive) oSB.setColor(Color.WHITE); else if (this.getIsHovered()) oSB.setColor(new Color(1.0F, 1.0F, 1.0F, 0.75F)); else oSB.setColor(new Color(1.0F, 1.0F, 1.0F, 0.575F));
            IMGManager.getIMG(Images.editorGame).drawO(oSB, this.getPosXE() + this.getWidthE() - IMGManager.getIMG(Images.editorGame).getWidth() - CFG.PADD + iTranslateX, this.getPosY() + this.getHeightE() - IMGManager.getIMG(Images.editorGame).getHeight() - CFG.PADD + iTranslateY);
            oSB.setColor(Color.WHITE);
         }
         @Override public Color getColorE(boolean isActive) {
            return isActive ? CFG.COLOR_BUTTON_GAME_TEXT_IMPORTANT_ACTIVE : (this.getIsClickable() ? (this.getIsHovered() ? CFG.COLOR_BUTTON_GAME_TEXT_IMPORTANT_HOVER : CFG.COLOR_BUTTON_GAME_TEXT_IMPORTANT) : CFG.COLOR_BUTTON_GAME_TEXT_NOT_CLICKABLE);
         }
         @Override public int getSFXElem() { return SFXManager.SFX_CLICK2; }
      });
      menuElements.add(new Button_Game(null, -1, CFG.PADD, CFG.PADD, true) {
         @Override public Color getColorE(boolean isActive) {
            return isActive ? CFG.COLOR_BUTTON_GAME_TEXT_IMPORTANT_ACTIVE : (this.getIsClickable() ? (this.getIsHovered() ? CFG.COLOR_BUTTON_GAME_TEXT_IMPORTANT_HOVER : CFG.COLOR_BUTTON_GAME_TEXT_IMPORTANT) : CFG.COLOR_BUTTON_GAME_TEXT_NOT_CLICKABLE);
         }
      });
      menuElements.add(new Button_Game(null, -1, CFG.PADD * 3 + CFG.BUTTON_W + CFG.BUTTON_W + CFG.BUTTON_W / 2, CFG.PADD, CFG.BUTTON_W + CFG.BUTTON_W / 2, true) {
         @Override public Color getColorE(boolean isActive) {
            return isActive ? CFG.COLOR_BUTTON_GAME_TEXT_IMPORTANT_ACTIVE : (this.getIsClickable() ? (this.getIsHovered() ? CFG.COLOR_BUTTON_GAME_TEXT_IMPORTANT_HOVER : CFG.COLOR_BUTTON_GAME_TEXT_IMPORTANT) : CFG.COLOR_BUTTON_GAME_TEXT_NOT_CLICKABLE);
         }
         @Override public int getSFXElem() { return SFXManager.SFX_CLICK2; }
         @Override public boolean getIsClickable() {
            try { return CFG.core.getActiveProvID() >= 0 && CFG.core.getProv(CFG.core.getActiveProvID()).getWastelandLvl() < 0 && !CFG.core.getProv(CFG.core.getActiveProvID()).getSeaProv() && !CFG.core.getProv(CFG.core.getActiveProvID()).getIsCapital2(); }
            catch (Exception ignored) { return false; }
         }
         @Override public void setTextE(String sText) {
            try {
               super.setTextE(sText);
               int maxAttempts = 0;
               while (this.iTextWidth > this.getWidthE() - CFG.PADD && this.getTextE().length() > 5) {
                  if (++maxAttempts >= 100) break;
                  super.setTextE(this.getTextE().substring(0, Math.max(1, this.getTextE().length() - 3)) + "..");
               }
            } catch (Exception exception) { CFG.exceptionStack(exception); }
         }
         @Override public void buildElemHover() {
            List<MEHover_2E> elements = new ArrayList<>();
            List<ME_Hover_2Type> data = new ArrayList<>();
            data.add(new ME_Hover_2Type_Text_Big(CFG.lang.get("AddNewCivilization"), CFG.COLOR_TEXT_NUM_OF_PROVINCES));
            data.add(new ME_Hover_2Type_Image_Big(Images.diploLord, CFG.PADD, 0));
            elements.add(new MEHover_2E(data)); data.clear();
            data.add(new ME_Hover_2Type_Space()); elements.add(new MEHover_2E(data)); data.clear();
            data.add(new ME_Hover_2Type_TextDesc(CFG.lang.get("AddANewCivilizationInTheSelectedProvince"), CFG.COLOR_NEUTRAL)); elements.add(new MEHover_2E(data)); data.clear();
            data.add(new ME_Hover_2Type_Space()); elements.add(new MEHover_2E(data)); data.clear();
            data.add(new ME_Hover_2Type_Text(CFG.lang.get("SelectLandProvince")));
            try { data.add(new ME_Hover_2Type_Image(CFG.core.getActiveProvID() >= 0 && CFG.core.getProv(CFG.core.getActiveProvID()).getWastelandLvl() < 0 && !CFG.core.getProv(CFG.core.getActiveProvID()).getSeaProv() ? Images.iconTrue : Images.iconFalse, CFG.PADD, 0)); }
            catch (Exception ignored) { data.add(new ME_Hover_2Type_Image(Images.iconFalse, CFG.PADD, 0)); }
            elements.add(new MEHover_2E(data)); data.clear();
            data.add(new ME_Hover_2Type_Text(CFG.lang.get("CapitalCity"))); data.add(new ME_Hover_2Type_Image(Images.btnX, CFG.PADD, 0));
            try { data.add(new ME_Hover_2Type_Image(CFG.core.getActiveProvID() >= 0 && !CFG.core.getProv(CFG.core.getActiveProvID()).getIsCapital2() ? Images.iconTrue : Images.iconFalse, CFG.PADD, 0)); }
            catch (Exception ignored) { data.add(new ME_Hover_2Type_Image(Images.iconFalse, CFG.PADD, 0)); }
            elements.add(new MEHover_2E(data));
            this.menuElemHover = new ME_Hover_v2(elements);
         }
         @Override public void drawMEH2(SpriteBatch oSB, int iTranslateX, int iTranslateY, boolean isActive) { drawPlayHover(this, oSB); }
      });
      this.initMenu(null, 0, CFG.GAMEHEIGHT - CFG.BUTTON_H - CFG.PADD * 2, CFG.GAMEWIDTH, CFG.BUTTON_H + CFG.PADD * 2, menuElements);
      this.updateLang();
   }

   private static void buildPlayHover(MenuElemUI button, boolean mobile) {
      try {
         List<MEHover_2E> elements = new ArrayList<>();
         List<ME_Hover_2Type> data = new ArrayList<>();
         data.add(new ME_Hover_2Type_Text_Big(CFG.lang.get("StartTheGame"), CFG.COLOR_TEXT_NUM_OF_PROVINCES));
         data.add(new ME_Hover_2Type_Image_Big(Images.gameLogo, CFG.PADD, 0));
         elements.add(new MEHover_2E(data)); data.clear();
         data.add(new ME_Hover_2Type_Space()); elements.add(new MEHover_2E(data)); data.clear();
         for (int i = 0; i < CFG.core.getPlayersSize(); i++) {
            data.add(new ME_Hover_2Type_Flag_Big(CFG.core.getPlayer(i).getCivId(), 0, CFG.PADD));
            data.add(new ME_Hover_2Type_Text_Big(CFG.core.getCiv(CFG.core.getPlayer(i).getCivId()).getCivName(), mobile ? CFG.COLOR_TEXT_NUM_OF_PROVINCES : CFG.COLOR_TEXT_NUM_OF_PROVINCES));
            if (!mobile) {
               data.add(new ME_Hover_2Type_Ideology_Big(CFG.core.getCiv(CFG.core.getPlayer(i).getCivId()).getIdeology(), CFG.PADD, 0));
               data.add(new ME_Hover_2Type_Religion_Big(CFG.core.getCiv(CFG.core.getPlayer(i).getCivId()).getReligionID(), CFG.PADD, 0));
            }
            elements.add(new MEHover_2E(data)); data.clear();
         }
         button.menuElemHover = new ME_Hover_v2(elements);
      } catch (Exception ignored) { button.menuElemHover = null; }
   }

   private static void drawPlayHover(MenuElemUI button, SpriteBatch oSB) {
      if (button.menuElemHover != null) button.menuElemHover.drawAlwaysOverM(oSB, Touch.getMousePosX(), CFG.GAMEHEIGHT - CFG.BUTTON_H - CFG.PADD * 2);
   }

   @Override public void updateLang() {
      this.getMenuElem(0).setTextE(CFG.lang.get("PLAY")); this.getMenuElem(1).setTextE(CFG.lang.get("Options")); this.getMenuElem(2).setTextE(CFG.lang.get("Back")); this.getMenuElem(3).setTextE(CFG.lang.get("AddCivilization"));
   }
   @Override public void draw(SpriteBatch oSB, int iTranslateX, int iTranslateY, boolean sliderMenuIsActive) {
      if (CFG.getIsDesktop()) {
         CFG.drawEditorButtons_Bot_Edge_R(oSB, iTranslateX, CFG.GAMEHEIGHT - CFG.BUTTON_H - CFG.PADD * 2 - 1 + iTranslateY, this.getMenuElem(3).getPosXE() + this.getMenuElem(3).getWidthE() + CFG.PADD + 1, CFG.BUTTON_H + CFG.PADD * 2 + 1);
         CFG.drawEditorButtons_Bot_Edge_R_Reflected(oSB, this.getMenuElem(0).getPosXE() - CFG.PADD - 1 + iTranslateX, CFG.GAMEHEIGHT - CFG.BUTTON_H - CFG.PADD * 2 - 1 + iTranslateY, this.getMenuElem(0).getWidthE() + CFG.PADD * 2 + 1, CFG.BUTTON_H + CFG.PADD * 2 + 1);
      } else {
         CFG.drawEditorButtons_Bot_Edge_R(oSB, iTranslateX, CFG.GAMEHEIGHT - CFG.BUTTON_H - CFG.PADD * 2 - 1 + iTranslateY, this.getMenuElem(0).getPosXE() + this.getMenuElem(0).getWidthE() + CFG.PADD + 1, CFG.BUTTON_H + CFG.PADD * 2 + 1);
      }
      super.draw(oSB, iTranslateX, iTranslateY, sliderMenuIsActive);
   }
   public static final void clickOptions() {
      if (CFG.menus.getVisible_CreateNewGame_AddCiv()) { CFG.menus.setVisible_CreateNewGame_AddCiv(false); CFG.menus.setVisible_CreateNewGame_AddCiv_Gov(false); }
      if (!CFG.menus.getVisible_CreateNewGame_Options()) CFG.menus.getColorPicker().setVisible(false, null);
      CFG.menus.setVisible_CreateNewGame_Options(!CFG.menus.getVisible_CreateNewGame_Options());
   }
   @Override public final void actionEL(int id) {
      CFG.brushMode = false;
      switch (id) {
         case 0: newGame(); if (CHALLENGE_MODE_NG >= 0 && CFG.core.getPlayersSize() == 1 && CFG.core.getPlayer(0).getCivId() > 0) CFG.core.getPlayer(0).playerGD.challengeID = CHALLENGE_MODE_NG; CHALLENGE_MODE_NG = -1; break;
         case 1: clickOptions(); break;
         case 2: CHALLENGE_MODE_NG = -1; this.onBackPressed(); break;
         case 3:
            CHALLENGE_MODE_NG = -1;
            if (!CFG.menus.getVisible_CreateNewGame_AddCiv()) {
               if (CFG.menus.getVisible_CreateNewGame_Options()) CFG.menus.setVisible_CreateNewGame_Options(false);
               if (CFG.menus.getVisible_CreateNewGame_Options_Scenarios()) CFG.menus.setVisible_CreateNewGame_Options_Scenarios(false);
               if (CFG.menus.getVisible_CreateNewGame_Options_Pallets()) CFG.menus.setVisible_CreateNewGame_Options_Pallets(false);
               Menu_CreateNewGame_AddCiv.provinceID = CFG.core.getActiveProvID(); Menu_CreateNewGame_AddCiv.techLevel = CFG.core.getCiv(1).getTechLevel(); Menu_CreateNewGame_AddCiv.civTag = null; Menu_CreateNewGame_AddCiv.provinces.clear(); CFG.menus.setVisible_CreateNewGame_AddCiv(true);
            } else CFG.menus.setVisible_CreateNewGame_AddCiv(false);
      }
   }
   @Override public final void onBackPressed() {
      if (!CFG.menus.getVisible_CreateNewGame_Options_Pallets() && !CFG.menus.getVisible_CreateNewGame_Options_Scenarios()) {
         CFG.menus.getColorPicker().setVisible(false, null); CFG.core.disableDrawCivlizationsRegions_Players(); CFG.core.setActiveProvID(-1); CFG.menus.setMenuID(View.eGAMES); CFG.menus.setOrderOfMenu_Games(); CFG.menus.setBackAnimation(true);
      } else CFG.menus.setVisible_CreateNewGame_Options(true);
   }
   public static final void newGame() {
      CFG.brushMode = false; CFG.menus.getColorPicker().setVisible(false, null); RTS.reset(); CFG.core.disableDrawCivlizationsRegions_Players(); CFG.mapModesManager.disableAllViews();
      if (CFG.map.getMpS().getCurrSc() < MapScale.STANDARD_SCALE) CFG.map.getMpS().setCurrScale(MapScale.STANDARD_SCALE);
      CFG.gameNewGame.newGamePrep(); CFG.EDITOR_ACTIVE_GAMEDATA_TAG = CFG.lang.get(CFG.core.getGameScenars().getScenarioNameID(CFG.core.getScenarioID())); CFG.startTheGameData = new Start_The_Game_Data(false); buildStartPopulationEconomy(); CFG.menus.setMenuIDWithoutAnim(View.eSTART_THE_GAME);
      try { CFG.SFXManager.playSound(SFXManager.SFX_START); } catch (Exception ignored) { }
   }
   public static void buildStartPopulationEconomy() {
      for (int i = 0; i < CFG.core.getProvinSize(); i++) {
         Province province = CFG.core.getProv(i); province.provGD.startingPopulation = province.getPop().getPops(); province.provGD.startingEconomy = province.getEco();
         if (province.getCivId() > 0) {
            Save_Civ_GameData data = CFG.core.getCiv(province.getCivId()).civGD; data.startingPopulation += province.getPop().getPops(); data.startingEconomy += province.getEco();
         }
      }
   }
}

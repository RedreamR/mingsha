package age.of.civilizations2.jakowski.lukasz.Menus.Settings;

import age.of.civilizations2.jakowski.lukasz.AoCGame;
import age.of.civilizations2.jakowski.lukasz.CFG;
import age.of.civilizations2.jakowski.lukasz.Event_GameData;
import age.of.civilizations2.jakowski.lukasz.GameCalendar;
import age.of.civilizations2.jakowski.lukasz.HolyRomanEmpire_Manager;
import age.of.civilizations2.jakowski.lukasz.IMGManager;
import age.of.civilizations2.jakowski.lukasz.Image;
import age.of.civilizations2.jakowski.lukasz.Images;
import age.of.civilizations2.jakowski.lukasz.Menu;
import age.of.civilizations2.jakowski.lukasz.RenderProvince;
import age.of.civilizations2.jakowski.lukasz.View;
import age.of.civilizations2.jakowski.lukasz.Button.Button_Transparent;
import age.of.civilizations2.jakowski.lukasz.Button.MenuElemUI;
import age.of.civilizations2.jakowski.lukasz.Button.Classic.Button_Classic;
import age.of.civilizations2.jakowski.lukasz.Button.Classic.Button_Classic_Description;
import age.of.civilizations2.jakowski.lukasz.Button.Classic.Button_Classic_ReflectedBG;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.MEHover_2E;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.ME_Hover_2Type;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.ME_Hover_2Type_Image;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.ME_Hover_2Type_Space;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.ME_Hover_2Type_Text;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.ME_Hover_v2;
import age.of.civilizations2.jakowski.lukasz.Menus.Budget.Menu_InGame_FA_Top;
import age.of.civilizations2.jakowski.lukasz.Sliders.Slider;
import team.rainfall.mingsha.counter.CounterUI;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;
import java.util.List;

public class Menu_CreateScenario_Settings extends Menu {
   private String sName;
   private String sAuthor;
   private String sWiki;
   private int iWikiWidth = 0;
   private static Image previewImage = null;

   public Menu_CreateScenario_Settings() {
      List<MenuElemUI> menuElements = new ArrayList<>();
      menuElements.add(
         new Button_Classic("", CFG.PADD * 2, 0, CFG.PADD * 3, Menu_InGame_FA_Top.getWindowWidth(), CFG.BUTTON_H, true) {
            @Override
            public String getTextToDrawElem() {
               return Menu_CreateScenario_Settings.this.sName + ": " + super.getTextE();
            }

            @Override
            public void drawButtonBGE(SpriteBatch oSB, int iTranslateX, int iTranslateY, boolean isActive) {
               if (isActive) {
                  oSB.setColor(0.981F, 0.981F, 0.981F, 0.65F);
               } else {
                  oSB.setColor(0.019F, 0.024F, 0.03F, 0.65F);
               }

               IMGManager.getIMG(Images.pix255)
                  .draw2O(
                     oSB,
                     this.getPosXE() + iTranslateX,
                     this.getPosY() - IMGManager.getIMG(Images.pix255).getHeight() + iTranslateY,
                     this.getWidthE(),
                     this.getHeightE()
                  );
               IMGManager.getIMG(Images.sliderGradient)
                  .drawO(
                     oSB,
                     this.getPosXE() + iTranslateX,
                     this.getPosY() - IMGManager.getIMG(Images.sliderGradient).getHeight() + iTranslateY,
                     this.getHeightE() / 2,
                     this.getHeightE(),
                     false,
                     false
                  );
               IMGManager.getIMG(Images.gradient)
                  .drawO(
                     oSB,
                     this.getPosXE() + iTranslateX,
                     this.getPosY() - IMGManager.getIMG(Images.gradient).getHeight() + iTranslateY,
                     this.getWidthE(),
                     this.getHeightE() / 4,
                     false,
                     false
                  );
               IMGManager.getIMG(Images.gradient)
                  .drawO(
                     oSB,
                     this.getPosXE() + iTranslateX,
                     this.getPosY() - IMGManager.getIMG(Images.gradient).getHeight() + this.getHeightE() - this.getHeightE() / 4 + iTranslateY,
                     this.getWidthE(),
                     this.getHeightE() / 4,
                     false,
                     true
                  );
               if (isActive) {
                  oSB.setColor(0.0F, 0.0F, 0.0F, 1.0F);
               } else {
                  oSB.setColor(CFG.COLOR_MINIMAP_BORDER);
               }

               IMGManager.getIMG(Images.pix255)
                  .drawO(oSB, this.getPosXE() + iTranslateX, this.getPosY() - IMGManager.getIMG(Images.pix255).getHeight() + iTranslateY, this.getWidthE(), 1);
               IMGManager.getIMG(Images.pix255)
                  .drawO(
                     oSB,
                     this.getPosXE() + iTranslateX,
                     this.getPosY() + this.getHeightE() - 1 - IMGManager.getIMG(Images.pix255).getHeight() + iTranslateY,
                     this.getWidthE(),
                     1
                  );
               oSB.setColor(new Color(CFG.COLOR_FLAG_FRAME.r, CFG.COLOR_FLAG_FRAME.g, CFG.COLOR_FLAG_FRAME.b, 0.3F));
               IMGManager.getIMG(Images.line32Off1)
                  .drawO(
                     oSB, this.getPosXE() + iTranslateX, this.getPosY() - IMGManager.getIMG(Images.line32Off1).getHeight() + iTranslateY, this.getWidthE(), 1
                  );
               IMGManager.getIMG(Images.line32Off1)
                  .drawO(
                     oSB,
                     this.getPosXE() + iTranslateX,
                     this.getPosY() + this.getHeightE() - 1 - IMGManager.getIMG(Images.line32Off1).getHeight() + iTranslateY,
                     this.getWidthE(),
                     1
                  );
               oSB.setColor(Color.WHITE);
            }

            @Override
            public void buildElemHover() {
               List<MEHover_2E> nElements = new ArrayList<>();
               List<ME_Hover_2Type> nData = new ArrayList<>();
               nData.add(new ME_Hover_2Type_Text(CFG.lang.get("ScenarioName") + ".", CFG.COLOR_HOVER_TITLE));
               nElements.add(new MEHover_2E(nData));
               nData.clear();
               this.menuElemHover = new ME_Hover_v2(nElements);
            }
         }
      );
      menuElements.add(
         new Button_Classic("", CFG.PADD * 2, 0, CFG.BUTTON_H + CFG.PADD * 4, Menu_InGame_FA_Top.getWindowWidth(), CFG.BUTTON_H - CFG.PADD * 2, true) {
            @Override
            public void drawButtonBGE(SpriteBatch oSB, int iTranslateX, int iTranslateY, boolean isActive) {
               if (isActive) {
                  oSB.setColor(0.981F, 0.981F, 0.981F, 0.65F);
               } else {
                  oSB.setColor(0.019F, 0.024F, 0.03F, 0.65F);
               }

               IMGManager.getIMG(Images.pix255)
                  .draw2O(
                     oSB,
                     this.getPosXE() + iTranslateX,
                     this.getPosY() - IMGManager.getIMG(Images.pix255).getHeight() + iTranslateY,
                     this.getWidthE(),
                     this.getHeightE()
                  );
               IMGManager.getIMG(Images.sliderGradient)
                  .drawO(
                     oSB,
                     this.getPosXE() + iTranslateX,
                     this.getPosY() - IMGManager.getIMG(Images.sliderGradient).getHeight() + iTranslateY,
                     this.getHeightE() / 2,
                     this.getHeightE(),
                     false,
                     false
                  );
               IMGManager.getIMG(Images.gradient)
                  .drawO(
                     oSB,
                     this.getPosXE() + iTranslateX,
                     this.getPosY() - IMGManager.getIMG(Images.gradient).getHeight() + iTranslateY,
                     this.getWidthE(),
                     this.getHeightE() / 4,
                     false,
                     false
                  );
               IMGManager.getIMG(Images.gradient)
                  .drawO(
                     oSB,
                     this.getPosXE() + iTranslateX,
                     this.getPosY() - IMGManager.getIMG(Images.gradient).getHeight() + this.getHeightE() - this.getHeightE() / 4 + iTranslateY,
                     this.getWidthE(),
                     this.getHeightE() / 4,
                     false,
                     true
                  );
               if (isActive) {
                  oSB.setColor(0.0F, 0.0F, 0.0F, 1.0F);
               } else {
                  oSB.setColor(CFG.COLOR_MINIMAP_BORDER);
               }

               IMGManager.getIMG(Images.pix255)
                  .drawO(oSB, this.getPosXE() + iTranslateX, this.getPosY() - IMGManager.getIMG(Images.pix255).getHeight() + iTranslateY, this.getWidthE(), 1);
               IMGManager.getIMG(Images.pix255)
                  .drawO(
                     oSB,
                     this.getPosXE() + iTranslateX,
                     this.getPosY() + this.getHeightE() - 1 - IMGManager.getIMG(Images.pix255).getHeight() + iTranslateY,
                     this.getWidthE(),
                     1
                  );
               oSB.setColor(new Color(CFG.COLOR_FLAG_FRAME.r, CFG.COLOR_FLAG_FRAME.g, CFG.COLOR_FLAG_FRAME.b, 0.3F));
               IMGManager.getIMG(Images.line32Off1)
                  .drawO(
                     oSB, this.getPosXE() + iTranslateX, this.getPosY() - IMGManager.getIMG(Images.line32Off1).getHeight() + iTranslateY, this.getWidthE(), 1
                  );
               IMGManager.getIMG(Images.line32Off1)
                  .drawO(
                     oSB,
                     this.getPosXE() + iTranslateX,
                     this.getPosY() + this.getHeightE() - 1 - IMGManager.getIMG(Images.line32Off1).getHeight() + iTranslateY,
                     this.getWidthE(),
                     1
                  );
               oSB.setColor(Color.WHITE);
            }

            @Override
            public void buildElemHover() {
               List<MEHover_2E> nElements = new ArrayList<>();
               List<ME_Hover_2Type> nData = new ArrayList<>();
               nData.add(new ME_Hover_2Type_Image(Images.time));
               nData.add(new ME_Hover_2Type_Text(CFG.lang.get("DateAndAgeOfScenario") + ".", CFG.COLOR_HOVER_TITLE));
               nElements.add(new MEHover_2E(nData));
               nData.clear();
               nData.add(new ME_Hover_2Type_Space());
               nElements.add(new MEHover_2E(nData));
               nData.clear();
               nData.add(new ME_Hover_2Type_Text(CFG.gameAges.getAge(CFG.CREATE_SCENARIO_AGE).getName()));
               nElements.add(new MEHover_2E(nData));
               nData.clear();
               nData.add(new ME_Hover_2Type_Text(CFG.lang.get("Date") + ": "));
               nData.add(new ME_Hover_2Type_Text(GameCalendar.getCurrDate(), CFG.COLOR_HOVER_TITLE));
               nElements.add(new MEHover_2E(nData));
               nData.clear();
               this.menuElemHover = new ME_Hover_v2(nElements);
            }
         }
      );
      menuElements.add(
         new Button_Classic("", CFG.PADD * 2, 0, CFG.BUTTON_H * 2 + CFG.PADD * 3, Menu_InGame_FA_Top.getWindowWidth(), CFG.BUTTON_H - CFG.PADD * 2, true) {
            @Override
            public String getTextToDrawElem() {
               return Menu_CreateScenario_Settings.this.sAuthor + ": " + super.getTextE();
            }

            @Override
            public void drawButtonBGE(SpriteBatch oSB, int iTranslateX, int iTranslateY, boolean isActive) {
               if (isActive) {
                  oSB.setColor(0.981F, 0.981F, 0.981F, 0.65F);
               } else {
                  oSB.setColor(0.019F, 0.024F, 0.03F, 0.65F);
               }

               IMGManager.getIMG(Images.pix255)
                  .draw2O(
                     oSB,
                     this.getPosXE() + iTranslateX,
                     this.getPosY() - IMGManager.getIMG(Images.pix255).getHeight() + iTranslateY,
                     this.getWidthE(),
                     this.getHeightE()
                  );
               IMGManager.getIMG(Images.sliderGradient)
                  .drawO(
                     oSB,
                     this.getPosXE() + iTranslateX,
                     this.getPosY() - IMGManager.getIMG(Images.sliderGradient).getHeight() + iTranslateY,
                     this.getHeightE() / 2,
                     this.getHeightE(),
                     false,
                     false
                  );
               IMGManager.getIMG(Images.gradient)
                  .drawO(
                     oSB,
                     this.getPosXE() + iTranslateX,
                     this.getPosY() - IMGManager.getIMG(Images.gradient).getHeight() + iTranslateY,
                     this.getWidthE(),
                     this.getHeightE() / 4,
                     false,
                     false
                  );
               IMGManager.getIMG(Images.gradient)
                  .drawO(
                     oSB,
                     this.getPosXE() + iTranslateX,
                     this.getPosY() - IMGManager.getIMG(Images.gradient).getHeight() + this.getHeightE() - this.getHeightE() / 4 + iTranslateY,
                     this.getWidthE(),
                     this.getHeightE() / 4,
                     false,
                     true
                  );
               if (isActive) {
                  oSB.setColor(0.0F, 0.0F, 0.0F, 1.0F);
               } else {
                  oSB.setColor(CFG.COLOR_MINIMAP_BORDER);
               }

               IMGManager.getIMG(Images.pix255)
                  .drawO(oSB, this.getPosXE() + iTranslateX, this.getPosY() - IMGManager.getIMG(Images.pix255).getHeight() + iTranslateY, this.getWidthE(), 1);
               IMGManager.getIMG(Images.pix255)
                  .drawO(
                     oSB,
                     this.getPosXE() + iTranslateX,
                     this.getPosY() + this.getHeightE() - 1 - IMGManager.getIMG(Images.pix255).getHeight() + iTranslateY,
                     this.getWidthE(),
                     1
                  );
               oSB.setColor(new Color(CFG.COLOR_FLAG_FRAME.r, CFG.COLOR_FLAG_FRAME.g, CFG.COLOR_FLAG_FRAME.b, 0.3F));
               IMGManager.getIMG(Images.line32Off1)
                  .drawO(
                     oSB, this.getPosXE() + iTranslateX, this.getPosY() - IMGManager.getIMG(Images.line32Off1).getHeight() + iTranslateY, this.getWidthE(), 1
                  );
               IMGManager.getIMG(Images.line32Off1)
                  .drawO(
                     oSB,
                     this.getPosXE() + iTranslateX,
                     this.getPosY() + this.getHeightE() - 1 - IMGManager.getIMG(Images.line32Off1).getHeight() + iTranslateY,
                     this.getWidthE(),
                     1
                  );
               oSB.setColor(Color.WHITE);
            }

            @Override
            public void buildElemHover() {
               List<MEHover_2E> nElements = new ArrayList<>();
               List<ME_Hover_2Type> nData = new ArrayList<>();
               nData.add(new ME_Hover_2Type_Text(CFG.lang.get("AuthorOfScenario") + ".", CFG.COLOR_HOVER_TITLE));
               nElements.add(new MEHover_2E(nData));
               nData.clear();
               this.menuElemHover = new ME_Hover_v2(nElements);
            }
         }
      );
      menuElements.add(new Button_Classic("-", -1, 0, CFG.BUTTON_H * 5 + CFG.PADD * 6, CFG.BUTTON_W, CFG.BUTTON_H, true));
      menuElements.add(
         new Slider(
            null,
            CFG.BUTTON_W,
            CFG.BUTTON_H * 5 + CFG.PADD * 7,
            Menu_InGame_FA_Top.getWindowWidth() - CFG.BUTTON_W * 2,
            CFG.BUTTON_H - CFG.PADD * 2,
            0,
            1000,
            CFG.core.getGameScenars().getScenario_StartingArmyInCapitals() / 25
         ) {
            @Override
            public String getDrawText() {
               return super.getTextE() + this.getCurr() * 25;
            }

            @Override
            public void drawE(SpriteBatch oSB, int iTranslateX, int iTranslateY, boolean isActive, boolean scrollableY) {
               IMGManager.getIMG(Images.btnMenu1H).drawO(oSB, this.getPosXE() + iTranslateX, this.getPosY() - CFG.PADD + iTranslateY, this.getWidthE());
               super.drawE(oSB, iTranslateX, iTranslateY, isActive, scrollableY);
            }

            @Override
            public void setCurr(int nCurrent) {
               CFG.core.getGameScenars().setScenarioStartingArmyInCapitals(nCurrent * 25);
               super.setCurr(nCurrent);
            }
         }
      );
      menuElements.add(
         new Button_Classic_ReflectedBG(
            "+", -1, Menu_InGame_FA_Top.getWindowWidth() - CFG.BUTTON_W, CFG.BUTTON_H * 5 + CFG.PADD * 6, CFG.BUTTON_W, CFG.BUTTON_H, true
         )
      );
      menuElements.add(new Button_Classic("-", -1, 0, CFG.BUTTON_H * 6 + CFG.PADD * 7, CFG.BUTTON_W, CFG.BUTTON_H, true));
      menuElements.add(
         new Slider(
            null,
            CFG.BUTTON_W,
            CFG.BUTTON_H * 6 + CFG.PADD * 8,
            Menu_InGame_FA_Top.getWindowWidth() - CFG.BUTTON_W * 2,
            CFG.BUTTON_H - CFG.PADD * 2,
            1,
            20000,
            CFG.core.getGameScenars().getScenario_StartingPopulation() / 100
         ) {
            @Override
            public String getDrawText() {
               return super.getTextE() + this.getCurr() * 100;
            }

            @Override
            public void drawE(SpriteBatch oSB, int iTranslateX, int iTranslateY, boolean isActive, boolean scrollableY) {
               IMGManager.getIMG(Images.btnMenu1H).drawO(oSB, this.getPosXE() + iTranslateX, this.getPosY() - CFG.PADD + iTranslateY, this.getWidthE());
               super.drawE(oSB, iTranslateX, iTranslateY, isActive, scrollableY);
            }

            @Override
            public Color getColorLEFT() {
               return CFG.COLOR_POP_GRADIENT[CFG.COLOR_POP_GRADIENT.length - 1];
            }

            @Override
            public void setCurr(int nCurrent) {
               CFG.core.getGameScenars().setScenarioStartingPopulation(nCurrent * 100);
               super.setCurr(nCurrent);
            }
         }
      );
      menuElements.add(
         new Button_Classic_ReflectedBG(
            "+", -1, Menu_InGame_FA_Top.getWindowWidth() - CFG.BUTTON_W, CFG.BUTTON_H * 6 + CFG.PADD * 7, CFG.BUTTON_W, CFG.BUTTON_H, true
         )
      );
      menuElements.add(new Button_Classic("-", -1, 0, CFG.BUTTON_H * 7 + CFG.PADD * 8, CFG.BUTTON_W, CFG.BUTTON_H, true));
      menuElements.add(
         new Slider(
            null,
            CFG.BUTTON_W,
            CFG.BUTTON_H * 7 + CFG.PADD * 9,
            Menu_InGame_FA_Top.getWindowWidth() - CFG.BUTTON_W * 2,
            CFG.BUTTON_H - CFG.PADD * 2,
            1,
            10000,
            CFG.core.getGameScenars().getScenario_StartingEconomy() / 100
         ) {
            @Override
            public String getDrawText() {
               return super.getTextE() + this.getCurr() * 100;
            }

            @Override
            public void drawE(SpriteBatch oSB, int iTranslateX, int iTranslateY, boolean isActive, boolean scrollableY) {
               IMGManager.getIMG(Images.btnMenu1H).drawO(oSB, this.getPosXE() + iTranslateX, this.getPosY() - CFG.PADD + iTranslateY, this.getWidthE());
               super.drawE(oSB, iTranslateX, iTranslateY, isActive, scrollableY);
            }

            @Override
            public Color getColorLEFT() {
               return CFG.COLOR_ECONOMY_GRADIENT[CFG.COLOR_ECONOMY_GRADIENT.length - 1];
            }

            @Override
            public void setCurr(int nCurrent) {
               CFG.core.getGameScenars().setScenarioStartingEconomy(nCurrent * 100);
               super.setCurr(nCurrent);
            }
         }
      );
      menuElements.add(
         new Button_Classic_ReflectedBG(
            "+", -1, Menu_InGame_FA_Top.getWindowWidth() - CFG.BUTTON_W, CFG.BUTTON_H * 7 + CFG.PADD * 8, CFG.BUTTON_W, CFG.BUTTON_H, true
         )
      );
      menuElements.add(
         new Button_Classic_Description(
            CFG.lang.get("ChangeDiplomaticRelationsBetweenCivilizations"),
            null,
            (int)(50.0F * CFG.GUI_SCALE),
            0,
            CFG.BUTTON_H * 4 + CFG.PADD * 5,
            Menu_InGame_FA_Top.getWindowWidth(),
            CFG.BUTTON_H,
            true
         ) {
            @Override
            public void buildElemHover() {
               List<MEHover_2E> nElements = new ArrayList<>();
               List<ME_Hover_2Type> nData = new ArrayList<>();
               nData.add(new ME_Hover_2Type_Text(CFG.lang.get("ChangeDiplomaticRelationsBetweenCivilizations") + ".", CFG.COLOR_HOVER_TITLE));
               nElements.add(new MEHover_2E(nData));
               nData.clear();
               this.menuElemHover = new ME_Hover_v2(nElements);
            }

            @Override
            public void drawTextE(SpriteBatch oSB, int iTranslateX, int iTranslateY, boolean isActive) {
               IMGManager.getIMG(Images.topDiplomacyPoints)
                  .drawO(
                     oSB,
                     this.getPosXE() + this.getTextPosElem() / 2 - IMGManager.getIMG(Images.topDiplomacyPoints).getWidth() / 2 + iTranslateX,
                     this.getPosY() + this.getHeightE() / 2 - IMGManager.getIMG(Images.topDiplomacyPoints).getHeight() / 2 + iTranslateY
                  );
               super.drawTextE(oSB, iTranslateX, iTranslateY, isActive);
            }
         }
      );
      menuElements.add(
         new Button_Classic(null, (int)(50.0F * CFG.GUI_SCALE), 0, CFG.BUTTON_H * 3 + CFG.PADD * 4, Menu_InGame_FA_Top.getWindowWidth(), CFG.BUTTON_H, true) {
            @Override
            public void drawTextE(SpriteBatch oSB, int iTranslateX, int iTranslateY, boolean isActive) {
               IMGManager.getIMG(Images.technology)
                  .drawO(
                     oSB,
                     this.getPosXE() + this.getTextPosElem() / 2 - IMGManager.getIMG(Images.technology).getWidth() / 2 + iTranslateX,
                     this.getPosY() + this.getHeightE() / 2 - IMGManager.getIMG(Images.technology).getHeight() / 2 + iTranslateY
                  );
               super.drawTextE(oSB, iTranslateX, iTranslateY, isActive);
            }
         }
      );
      menuElements.add(
         new Button_Classic(null, (int)(50.0F * CFG.GUI_SCALE), 0, CFG.BUTTON_H * 9 + CFG.PADD * 10, Menu_InGame_FA_Top.getWindowWidth(), CFG.BUTTON_H, true)
      );
      menuElements.add(new Button_Transparent(CFG.PADD, CFG.PADD * 3, CFG.PADD, CFG.BUTTON_H * 3, true) {
         @Override
         public void buildElemHover() {
            List<MEHover_2E> nElements = new ArrayList<>();
            List<ME_Hover_2Type> nData = new ArrayList<>();
            nData.add(new ME_Hover_2Type_Text(CFG.lang.get("Civilizations") + ": "));
            nData.add(new ME_Hover_2Type_Text("" + (CFG.core.getCivsSize() - 1), CFG.COLOR_HOVER_TITLE));
            nElements.add(new MEHover_2E(nData));
            nData.clear();
            this.menuElemHover = new ME_Hover_v2(nElements);
         }
      });
      menuElements.add(
         new Button_Classic(null, -1, 0, CFG.BUTTON_H * 17 + CFG.PADD * 18, Menu_InGame_FA_Top.getWindowWidth(), CFG.BUTTON_H, true) {
            @Override
            public void drawTextE(SpriteBatch oSB, int iTranslateX, int iTranslateY, boolean isActive) {
               if (CFG.palletManager.getActivePalletID() == 0) {
                  CFG.palletManager
                     .drawSampleColors_Standard(
                        oSB,
                        this.getPosXE() + this.getWidthE() / 2 - CFG.BUTTON_W - CFG.BUTTON_W / 2 + iTranslateX,
                        this.getPosY() + this.getHeightE() / 2 - this.getHeightE() / 4 + Menu_CreateScenario_Settings.this.getMenuPosY(),
                        CFG.BUTTON_W * 3,
                        this.getHeightE() / 2,
                        CFG.palletManager.getActivePalletID(),
                        isActive
                     );
               } else {
                  CFG.palletManager
                     .drawSampleColors(
                        oSB,
                        this.getPosXE() + this.getWidthE() / 2 - CFG.BUTTON_W - CFG.BUTTON_W / 2 + iTranslateX,
                        this.getPosY() + this.getHeightE() / 2 - this.getHeightE() / 4 + Menu_CreateScenario_Settings.this.getMenuPosY(),
                        CFG.BUTTON_W * 3,
                        this.getHeightE() / 2,
                        CFG.palletManager.getActivePalletID() - 1,
                        isActive
                     );
               }

               CFG.fontMain.get(0).getData().setScale(0.8F);
               if (isActive) {
                  CFG.drawTextDefault(
                     oSB,
                     this.getTextToDrawElem(),
                     this.getPosXE() + this.getWidthE() / 2 - (int)((float)this.getTextWidthU() * 0.8F) / 2 + iTranslateX,
                     this.getPosY() + this.getHeightE() / 2 - (int)((float)this.getTextHeight() * 0.8F) / 2 + iTranslateY,
                     this.getColorE(isActive)
                  );
               } else {
                  CFG.drawTextDefaultWithShadow(
                     oSB,
                     this.getTextToDrawElem(),
                     this.getPosXE() + this.getWidthE() / 2 - (int)((float)this.getTextWidthU() * 0.8F) / 2 + iTranslateX,
                     this.getPosY() + this.getHeightE() / 2 - (int)((float)this.getTextHeight() * 0.8F) / 2 + iTranslateY,
                     this.getColorE(isActive)
                  );
               }

               CFG.fontMain.get(0).getData().setScale(1.0F);
            }

            @Override
            public void buildElemHover() {
               List<MEHover_2E> nElements = new ArrayList<>();
               List<ME_Hover_2Type> nData = new ArrayList<>();
               nData.add(new ME_Hover_2Type_Text(CFG.lang.get("DefaultSetOfCivilizationsColorsInAScenario") + ".", CFG.COLOR_HOVER_TITLE));
               nElements.add(new MEHover_2E(nData));
               nData.clear();
               this.menuElemHover = new ME_Hover_v2(nElements);
            }
         }
      );
      menuElements.add(new Button_Classic("-", -1, 0, CFG.BUTTON_H * 8 + CFG.PADD * 9, CFG.BUTTON_W, CFG.BUTTON_H, true));
      menuElements.add(
         new Slider(
            null,
            CFG.BUTTON_W,
            CFG.BUTTON_H * 8 + CFG.PADD * 10,
            Menu_InGame_FA_Top.getWindowWidth() - CFG.BUTTON_W * 2,
            CFG.BUTTON_H - CFG.PADD * 2,
            -200,
            1500,
            CFG.core.getGameScenars().getScenario_StartingMoney() / 50
         ) {
            @Override
            public String getDrawText() {
               return super.getTextE() + this.getCurr() * 50;
            }

            @Override
            public void drawE(SpriteBatch oSB, int iTranslateX, int iTranslateY, boolean isActive, boolean scrollableY) {
               IMGManager.getIMG(Images.btnMenu1H).drawO(oSB, this.getPosXE() + iTranslateX, this.getPosY() - CFG.PADD + iTranslateY, this.getWidthE());
               super.drawE(oSB, iTranslateX, iTranslateY, isActive, scrollableY);
            }

            @Override
            public Color getColorLEFT() {
               return new Color(0.15686275F, 0.50980395F, 0.26666668F, 1.0F);
            }

            @Override
            public void setCurr(int nCurrent) {
               CFG.core.getGameScenars().setScenarioStartingMoney(nCurrent * 50);
               super.setCurr(nCurrent);
            }
         }
      );
      menuElements.add(
         new Button_Classic_ReflectedBG(
            "+", -1, Menu_InGame_FA_Top.getWindowWidth() - CFG.BUTTON_W, CFG.BUTTON_H * 8 + CFG.PADD * 9, CFG.BUTTON_W, CFG.BUTTON_H, true
         )
      );
      menuElements.add(
         new Button_Classic(null, (int)(50.0F * CFG.GUI_SCALE), 0, CFG.BUTTON_H * 14 + CFG.PADD * 15, Menu_InGame_FA_Top.getWindowWidth(), CFG.BUTTON_H, true) {
            @Override
            public void drawTextE(SpriteBatch oSB, int iTranslateX, int iTranslateY, boolean isActive) {
               IMGManager.getIMG(Images.topGold())
                  .drawO(
                     oSB,
                     this.getPosXE() + this.getTextPosElem() / 2 - IMGManager.getIMG(Images.topGold()).getWidth() / 2 + iTranslateX,
                     this.getPosY() + this.getHeightE() / 2 - IMGManager.getIMG(Images.topGold()).getHeight() / 2 + iTranslateY
                  );
               super.drawTextE(oSB, iTranslateX, iTranslateY, isActive);
            }
         }
      );
      menuElements.add(
         new Button_Classic(null, (int)(50.0F * CFG.GUI_SCALE), 0, CFG.BUTTON_H * 12 + CFG.PADD * 13, Menu_InGame_FA_Top.getWindowWidth(), CFG.BUTTON_H, true)
      );
      menuElements.add(
         new Button_Classic(null, (int)(50.0F * CFG.GUI_SCALE), 0, CFG.BUTTON_H * 15 + CFG.PADD * 16, Menu_InGame_FA_Top.getWindowWidth(), CFG.BUTTON_H, true) {
            @Override
            public void drawTextE(SpriteBatch oSB, int iTranslateX, int iTranslateY, boolean isActive) {
               IMGManager.getIMG(Images.happiness)
                  .drawO(
                     oSB,
                     this.getPosXE() + this.getTextPosElem() / 2 - IMGManager.getIMG(Images.happiness).getWidth() / 2 + iTranslateX,
                     this.getPosY() + this.getHeightE() / 2 - IMGManager.getIMG(Images.happiness).getHeight() / 2 + iTranslateY
                  );
               super.drawTextE(oSB, iTranslateX, iTranslateY, isActive);
            }
         }
      );
      menuElements.add(
         new Button_Classic(null, (int)(50.0F * CFG.GUI_SCALE), 0, CFG.BUTTON_H * 13 + CFG.PADD * 14, Menu_InGame_FA_Top.getWindowWidth(), CFG.BUTTON_H, true) {
            @Override
            public void drawTextE(SpriteBatch oSB, int iTranslateX, int iTranslateY, boolean isActive) {
               IMGManager.getIMG(Images.hreFlag)
                  .drawO(
                     oSB,
                     this.getPosXE() + this.getTextPosElem() / 2 - CFG.CIV_FLAG_WIDTH / 2 + iTranslateX,
                     this.getPosY() - IMGManager.getIMG(Images.hreFlag).getHeight() + this.getHeightE() / 2 - CFG.CIV_FLAG_HEIGHT / 2 + iTranslateY,
                     CFG.CIV_FLAG_WIDTH,
                     CFG.CIV_FLAG_HEIGHT
                  );
               IMGManager.getIMG(Images.flagRectSmall)
                  .drawO(
                     oSB,
                     this.getPosXE() + this.getTextPosElem() / 2 - IMGManager.getIMG(Images.flagRectSmall).getWidth() / 2 + iTranslateX,
                     this.getPosY() + this.getHeightE() / 2 - IMGManager.getIMG(Images.flagRectSmall).getHeight() / 2 + iTranslateY
                  );
               super.drawTextE(oSB, iTranslateX, iTranslateY, isActive);
            }
         }
      );
      menuElements.add(
         new Button_Classic(null, (int)(50.0F * CFG.GUI_SCALE), 0, CFG.BUTTON_H * 10 + CFG.PADD * 11, Menu_InGame_FA_Top.getWindowWidth(), CFG.BUTTON_H, true) {
            @Override
            public void drawTextE(SpriteBatch oSB, int iTranslateX, int iTranslateY, boolean isActive) {
               IMGManager.getIMG(Images.time)
                  .drawO(
                     oSB,
                     this.getPosXE() + this.getTextPosElem() / 2 - IMGManager.getIMG(Images.time).getWidth() / 2 + iTranslateX,
                     this.getPosY() + this.getHeightE() / 2 - IMGManager.getIMG(Images.time).getHeight() / 2 + iTranslateY
                  );
               super.drawTextE(oSB, iTranslateX, iTranslateY, isActive);
            }
         }
      );
      menuElements.add(
         new Button_Classic(null, (int)(50.0F * CFG.GUI_SCALE), 0, CFG.BUTTON_H * 11 + CFG.PADD * 12, Menu_InGame_FA_Top.getWindowWidth(), CFG.BUTTON_H, true) {
            @Override
            public void drawTextE(SpriteBatch oSB, int iTranslateX, int iTranslateY, boolean isActive) {
               IMGManager.getIMG(Images.time)
                  .drawO(
                     oSB,
                     this.getPosXE() + this.getTextPosElem() / 2 - IMGManager.getIMG(Images.time).getWidth() / 2 + iTranslateX,
                     this.getPosY() + this.getHeightE() / 2 - IMGManager.getIMG(Images.time).getHeight() / 2 + iTranslateY
                  );
               super.drawTextE(oSB, iTranslateX, iTranslateY, isActive);
            }
         }
      );
      menuElements.add(
         new Button_Classic(null, (int)(50.0F * CFG.GUI_SCALE), 0, CFG.BUTTON_H * 16 + CFG.PADD * 17, Menu_InGame_FA_Top.getWindowWidth(), CFG.BUTTON_H, true) {
            @Override
            public void drawTextE(SpriteBatch oSB, int iTranslateX, int iTranslateY, boolean isActive) {
               IMGManager.getIMG(Images.wikipedia)
                  .drawO(
                     oSB,
                     this.getPosXE() + this.getTextPosElem() / 2 - IMGManager.getIMG(Images.wikipedia).getWidth() / 2 + iTranslateX,
                     this.getPosY() + this.getHeightE() / 2 - IMGManager.getIMG(Images.wikipedia).getHeight() / 2 + iTranslateY
                  );
               CFG.drawTextDefault(
                  oSB,
                  Menu_CreateScenario_Settings.this.sWiki,
                  this.getPosXE() + this.getTextPosElem() + iTranslateX,
                  this.getPosY() + this.getHeightE() / 2 - CFG.TEXT_HEIGHT_DEFAULT / 2 + iTranslateY,
                  this.getColorE(isActive)
               );
               super.drawTextE(oSB, Menu_CreateScenario_Settings.this.iWikiWidth + iTranslateX, iTranslateY, isActive);
            }
         }
      );
      menuElements.add(new Button_Classic("-", -1, 0, CFG.BUTTON_H * 18 + CFG.PADD * 19, CFG.BUTTON_W, CFG.BUTTON_H, true));
      menuElements.add(
         new Slider(
            null,
            CFG.BUTTON_W,
            CFG.BUTTON_H * 18 + CFG.PADD * 20,
            Menu_InGame_FA_Top.getWindowWidth() - CFG.BUTTON_W * 2,
            CFG.BUTTON_H - CFG.PADD * 2,
            10,
            200,
            100 + (int)(CFG.core.getGameScenars().getScenario_PopulationGrowthRate_Modifier() * 100.0F)
         ) {
            @Override
            public String getDrawText() {
               return super.getTextE() + (this.getCurr() - 100 >= 0 ? "+" : "") + (this.getCurr() - 100) + "%";
            }

            @Override
            public void drawE(SpriteBatch oSB, int iTranslateX, int iTranslateY, boolean isActive, boolean scrollableY) {
               IMGManager.getIMG(Images.btnMenu1H).drawO(oSB, this.getPosXE() + iTranslateX, this.getPosY() - CFG.PADD + iTranslateY, this.getWidthE());
               super.drawE(oSB, iTranslateX, iTranslateY, isActive, scrollableY);
            }

            @Override
            public void setCurr(int nCurrent) {
               CFG.core.getGameScenars().setScenario_PopulationGrowthRate_Modifier((float)(nCurrent - 100) / 100.0F);
               super.setCurr(nCurrent);
            }
         }
      );
      menuElements.add(
         new Button_Classic_ReflectedBG(
            "+", -1, Menu_InGame_FA_Top.getWindowWidth() - CFG.BUTTON_W, CFG.BUTTON_H * 18 + CFG.PADD * 19, CFG.BUTTON_W, CFG.BUTTON_H, true
         )
      );
      menuElements.add(new Button_Classic("-", -1, 0, CFG.BUTTON_H * 19 + CFG.PADD * 20, CFG.BUTTON_W, CFG.BUTTON_H, true));
      menuElements.add(
         new Slider(
            null,
            CFG.BUTTON_W,
            CFG.BUTTON_H * 19 + CFG.PADD * 21,
            Menu_InGame_FA_Top.getWindowWidth() - CFG.BUTTON_W * 2,
            CFG.BUTTON_H - CFG.PADD * 2,
            10,
            200,
            100 + (int)(CFG.core.getGameScenars().getScenario_EconomyGrowthRate_Modifier() * 100.0F)
         ) {
            @Override
            public String getDrawText() {
               return super.getTextE() + (this.getCurr() - 100 >= 0 ? "+" : "") + (this.getCurr() - 100) + "%";
            }

            @Override
            public void drawE(SpriteBatch oSB, int iTranslateX, int iTranslateY, boolean isActive, boolean scrollableY) {
               IMGManager.getIMG(Images.btnMenu1H).drawO(oSB, this.getPosXE() + iTranslateX, this.getPosY() - CFG.PADD + iTranslateY, this.getWidthE());
               super.drawE(oSB, iTranslateX, iTranslateY, isActive, scrollableY);
            }

            @Override
            public void setCurr(int nCurrent) {
               CFG.core.getGameScenars().setScenario_EconomyGrowthRate_Modifier((float)(nCurrent - 100) / 100.0F);
               super.setCurr(nCurrent);
            }
         }
      );
      menuElements.add(
         new Button_Classic_ReflectedBG(
            "+", -1, Menu_InGame_FA_Top.getWindowWidth() - CFG.BUTTON_W, CFG.BUTTON_H * 19 + CFG.PADD * 20, CFG.BUTTON_W, CFG.BUTTON_H, true
         )
      );
      menuElements.add(new Button_Classic("-", -1, 0, CFG.BUTTON_H * 20 + CFG.PADD * 21, CFG.BUTTON_W, CFG.BUTTON_H, true));
      menuElements.add(
         new Slider(
            null,
            CFG.BUTTON_W,
            CFG.BUTTON_H * 20 + CFG.PADD * 22,
            Menu_InGame_FA_Top.getWindowWidth() - CFG.BUTTON_W * 2,
            CFG.BUTTON_H - CFG.PADD * 2,
            15,
            200,
            100 + (int)(CFG.core.getGameScenars().getScenario_DiseasesDeathRate_Modifier() * 100.0F)
         ) {
            @Override
            public String getDrawText() {
               return super.getTextE() + (this.getCurr() - 100 >= 0 ? "+" : "") + (this.getCurr() - 100) + "%";
            }

            @Override
            public void drawE(SpriteBatch oSB, int iTranslateX, int iTranslateY, boolean isActive, boolean scrollableY) {
               IMGManager.getIMG(Images.btnMenu1H).drawO(oSB, this.getPosXE() + iTranslateX, this.getPosY() - CFG.PADD + iTranslateY, this.getWidthE());
               super.drawE(oSB, iTranslateX, iTranslateY, isActive, scrollableY);
            }

            @Override
            public void setCurr(int nCurrent) {
               CFG.core.getGameScenars().setScenario_DiseasesDeathRate_Modifier((float)(nCurrent - 100) / 100.0F);
               super.setCurr(nCurrent);
            }
         }
      );
      menuElements.add(
         new Button_Classic_ReflectedBG(
            "+", -1, Menu_InGame_FA_Top.getWindowWidth() - CFG.BUTTON_W, CFG.BUTTON_H * 20 + CFG.PADD * 21, CFG.BUTTON_W, CFG.BUTTON_H, true
         )
      );
      menuElements.add(
         new Button_Classic(null, (int)(50.0F * CFG.GUI_SCALE), 0, CFG.BUTTON_H * 21 + CFG.PADD * 22, Menu_InGame_FA_Top.getWindowWidth(), CFG.BUTTON_H, true)
      );
      this.initMenu(
         null, 0 + AoCGame.LEFT, CFG.BUTTON_H + CFG.PADD * 2, Menu_InGame_FA_Top.getWindowWidth(), CFG.GAMEHEIGHT - (CFG.BUTTON_H + CFG.PADD * 2), menuElements
      );
      this.updateLang();
   }

   @Override
   public void updateLang() {
      this.sName = CFG.lang.get("ScenarioName");
      this.sAuthor = CFG.lang.get("Author");
      this.sWiki = CFG.lang.get("Wiki") + ": ";

      try {
         CFG.glyphLay.setText(CFG.fontMain.get(0), this.sWiki);
         this.iWikiWidth = (int)CFG.glyphLay.width;
      } catch (NullPointerException var2) {
         this.iWikiWidth = 0;
      } catch (IndexOutOfBoundsException var3) {
         this.iWikiWidth = 0;
      }

      this.getMenuElem(0).setTextE(CFG.CREATE_SCENARIO_NAME);
      this.getMenuElem(1)
         .setTextE(
            CFG.gameAges.getAge(CFG.CREATE_SCENARIO_AGE).getName()
               + " ["
               + GameCalendar.currDay
               + " "
               + GameCalendar.getMonthName(GameCalendar.currMonth)
               + " "
               + CFG.gameAges.getYear(GameCalendar.currYear)
               + "]"
         );
      this.getMenuElem(2).setTextE(CFG.CREATE_SCENARIO_AUTHOR);
      this.getMenuElem(4).setTextE(CFG.lang.get("StartingArmyInCapitals") + ": ");
      this.getMenuElem(7).setTextE(CFG.lang.get("StartingPopulation") + ": ");
      this.getMenuElem(10).setTextE(CFG.lang.get("StartingEconomy") + ": ");
      this.getMenuElem(12).setTextE(CFG.lang.get("ManageDiplomacy"));
      this.getMenuElem(13).setTextE(CFG.lang.get("TechnologyLevels"));
      this.getMenuElem(14).setTextE(CFG.lang.get("SetUpArmy"));
      this.getMenuElem(16).setTextE(CFG.lang.get("PalletCivColors"));
      this.getMenuElem(18).setTextE(CFG.lang.get("StartingMoney") + ": ");
      this.getMenuElem(20).setTextE(CFG.lang.get("StartingMoney"));
      this.getMenuElem(21).setTextE(CFG.lang.get("Cores") + ", " + CFG.lang.get("Population"));
      this.getMenuElem(22).setTextE(CFG.lang.get("Happiness"));
      this.getMenuElem(23).setTextE(CFG.lang.get("HolyRomanEmpire"));
      this.getMenuElem(24).setTextE(CFG.lang.get("SetEvents"));
      this.getMenuElem(25).setTextE(CFG.lang.get("Colonization"));
      this.getMenuElem(26).setTextE(CFG.CREATE_SCENARIO_WIKI);
      this.getMenuElem(28).setTextE(CFG.lang.get("PopulationGrowthModifier") + ": ");
      this.getMenuElem(31).setTextE(CFG.lang.get("EconomyGrowthModifier") + ": ");
      this.getMenuElem(34).setTextE(CFG.lang.get("DiseasesDeathRate") + ": ");
      this.getMenuElem(36).setTextE("Counters");
   }

   @Override
   public void draw(SpriteBatch oSB, int iTranslateX, int iTranslateY, boolean sliderMenuIsActive) {
      super.beginClipM(oSB, iTranslateX, iTranslateY, sliderMenuIsActive);
      oSB.setColor(new Color(0.0F, 0.01F, 0.012F, 0.45F));
      IMGManager.getIMG(Images.pix255)
         .draw2O(
            oSB,
            this.getPosX() + iTranslateX,
            -CFG.PADD + this.getMenuPosY() + (this.getMenuElem(0).getPosY() - CFG.PADD) - IMGManager.getIMG(Images.pix255).getHeight() + iTranslateY,
            this.getWidthM(),
            CFG.BUTTON_H * 3 + CFG.PADD * 2
         );
      oSB.setColor(new Color(0.0F, 0.01F, 0.012F, 0.32F));
      IMGManager.getIMG(Images.patternReversed)
         .draw2O(
            oSB,
            this.getPosX() + iTranslateX,
            -CFG.PADD + this.getMenuPosY() + (this.getMenuElem(0).getPosY() - CFG.PADD) - IMGManager.getIMG(Images.patternReversed).getHeight() + iTranslateY,
            this.getWidthM(),
            CFG.BUTTON_H * 3 + CFG.PADD * 2
         );
      oSB.setColor(new Color(0.0F, 0.01F, 0.012F, 0.75F));
      IMGManager.getIMG(Images.gradient)
         .drawO(
            oSB,
            this.getPosX() + iTranslateX,
            -CFG.PADD + this.getMenuPosY() + (this.getMenuElem(0).getPosY() - CFG.PADD) - IMGManager.getIMG(Images.gradient).getHeight() + iTranslateY,
            this.getWidthM(),
            CFG.BUTTON_H / 2
         );
      IMGManager.getIMG(Images.gradient)
         .drawO(
            oSB,
            this.getPosX() + iTranslateX,
            -CFG.PADD
               + this.getMenuPosY()
               + (this.getMenuElem(0).getPosY() - CFG.PADD)
               + CFG.BUTTON_H * 3
               + CFG.PADD * 2
               - CFG.BUTTON_H / 2
               - IMGManager.getIMG(Images.gradient).getHeight()
               + iTranslateY,
            this.getWidthM(),
            CFG.BUTTON_H / 2,
            false,
            true
         );
      oSB.setColor(CFG.COLOR_CREATE_NEW_GAME_BOX_PLAYERS);
      IMGManager.getIMG(Images.pix255)
         .draw2O(
            oSB,
            this.getPosX() + iTranslateX,
            -CFG.PADD + this.getMenuPosY() + (this.getMenuElem(0).getPosY() - CFG.PADD) - IMGManager.getIMG(Images.pix255).getHeight() + iTranslateY,
            this.getWidthM(),
            1
         );
      IMGManager.getIMG(Images.pix255)
         .draw2O(
            oSB,
            this.getPosX() + iTranslateX,
            -CFG.PADD
               + this.getMenuPosY()
               + (this.getMenuElem(0).getPosY() - CFG.PADD)
               + CFG.BUTTON_H * 3
               + CFG.PADD * 2
               - 1
               - IMGManager.getIMG(Images.pix255).getHeight()
               + iTranslateY,
            this.getWidthM(),
            1
         );
      oSB.setColor(new Color(0.0F, 0.0F, 0.0F, 0.6F));
      IMGManager.getIMG(Images.pix255)
         .draw2O(
            oSB,
            this.getPosX() + iTranslateX,
            -CFG.PADD + this.getMenuPosY() + (this.getMenuElem(0).getPosY() - CFG.PADD) - IMGManager.getIMG(Images.pix255).getHeight() - 1 + iTranslateY,
            this.getWidthM(),
            1
         );
      IMGManager.getIMG(Images.pix255)
         .draw2O(
            oSB,
            this.getPosX() + iTranslateX,
            -CFG.PADD
               + this.getMenuPosY()
               + (this.getMenuElem(0).getPosY() - CFG.PADD)
               + CFG.BUTTON_H * 3
               + CFG.PADD * 2
               - IMGManager.getIMG(Images.pix255).getHeight()
               + iTranslateY,
            this.getWidthM(),
            1
         );
      oSB.setColor(Color.WHITE);

      try {
         oSB.setColor(Color.BLACK);
         IMGManager.getIMG(Images.pix255)
            .drawO(
               oSB,
               this.getPosX() + CFG.PADD * 2 + iTranslateX,
               this.getMenuPosY() + this.getMenuElem(0).getPosY() - IMGManager.getIMG(Images.pix255).getHeight() + iTranslateY,
               previewImage.getWidth(),
               previewImage.getHeight()
            );
         oSB.setColor(Color.WHITE);
         previewImage.drawO(
            oSB,
            this.getPosX() + CFG.PADD * 2 + iTranslateX,
            this.getMenuPosY() + this.getMenuElem(0).getPosY() - previewImage.getHeight() + iTranslateY,
            (int)((float)previewImage.getWidth() * CFG.GUI_SCALE),
            (int)((float)previewImage.getHeight() * CFG.GUI_SCALE),
            false,
            true
         );
         CFG.map
            .getMpB()
            .getMinimapOverlay()
            .draw2O(
               oSB,
               this.getPosX() + CFG.PADD * 2 + iTranslateX,
               this.getMenuPosY() + this.getMenuElem(0).getPosY() - CFG.map.getMpB().getMinimapOverlay().getHeight() + iTranslateY,
               (int)((float)previewImage.getWidth() * CFG.GUI_SCALE) - CFG.map.getMpB().getMinimapOverlay().getWidth(),
               (int)((float)previewImage.getHeight() * CFG.GUI_SCALE) - CFG.map.getMpB().getMinimapOverlay().getHeight()
            );
         CFG.map
            .getMpB()
            .getMinimapOverlay()
            .draw2O(
               oSB,
               this.getPosX()
                  + CFG.PADD * 2
                  + (int)((float)previewImage.getWidth() * CFG.GUI_SCALE)
                  - CFG.map.getMpB().getMinimapOverlay().getWidth()
                  + iTranslateX,
               this.getMenuPosY() + this.getMenuElem(0).getPosY() - CFG.map.getMpB().getMinimapOverlay().getHeight() + iTranslateY,
               CFG.map.getMpB().getMinimapOverlay().getWidth(),
               (int)((float)previewImage.getHeight() * CFG.GUI_SCALE) - CFG.map.getMpB().getMinimapOverlay().getHeight(),
               true
            );
         CFG.map
            .getMpB()
            .getMinimapOverlay()
            .draw2O(
               oSB,
               this.getPosX() + CFG.PADD * 2 + iTranslateX,
               this.getMenuPosY()
                  + this.getMenuElem(0).getPosY()
                  - CFG.map.getMpB().getMinimapOverlay().getHeight()
                  + (int)((float)previewImage.getHeight() * CFG.GUI_SCALE)
                  - CFG.map.getMpB().getMinimapOverlay().getHeight()
                  + iTranslateY,
               (int)((float)previewImage.getWidth() * CFG.GUI_SCALE) - CFG.map.getMpB().getMinimapOverlay().getWidth(),
               CFG.map.getMpB().getMinimapOverlay().getHeight(),
               false,
               true
            );
         CFG.map
            .getMpB()
            .getMinimapOverlay()
            .draw2O(
               oSB,
               this.getPosX()
                  + CFG.PADD * 2
                  + (int)((float)previewImage.getWidth() * CFG.GUI_SCALE)
                  - CFG.map.getMpB().getMinimapOverlay().getWidth()
                  + iTranslateX,
               this.getMenuPosY()
                  + this.getMenuElem(0).getPosY()
                  - CFG.map.getMpB().getMinimapOverlay().getHeight()
                  + (int)((float)previewImage.getHeight() * CFG.GUI_SCALE)
                  - CFG.map.getMpB().getMinimapOverlay().getHeight()
                  + iTranslateY,
               CFG.map.getMpB().getMinimapOverlay().getWidth(),
               CFG.map.getMpB().getMinimapOverlay().getHeight(),
               true,
               true
            );
         oSB.setColor(CFG.COLOR_MINIMAP_BORDER);
         CFG.drawRect(
            oSB,
            this.getPosX() + CFG.PADD * 2 + iTranslateX,
            this.getMenuPosY() + this.getMenuElem(0).getPosY() - 1 + iTranslateY,
            (int)((float)previewImage.getWidth() * CFG.GUI_SCALE),
            (int)((float)previewImage.getHeight() * CFG.GUI_SCALE)
         );
         oSB.setColor(new Color(0.0F, 0.0F, 0.0F, 0.65F));
         CFG.drawRect(
            oSB,
            this.getPosX() + 1 + CFG.PADD * 2 + iTranslateX,
            this.getMenuPosY() + this.getMenuElem(0).getPosY() + iTranslateY,
            (int)((float)previewImage.getWidth() * CFG.GUI_SCALE) - 2,
            (int)((float)previewImage.getHeight() * CFG.GUI_SCALE) - 2
         );
         oSB.setColor(Color.WHITE);
      } catch (NullPointerException var6) {
         previewImage = CFG.map.getMpB().getScenarioMinimapPreviewTexture(oSB);
         oSB.setColor(Color.BLACK);
         IMGManager.getIMG(Images.pix255).drawO(oSB, 0, -IMGManager.getIMG(Images.pix255).getHeight(), previewImage.getWidth(), previewImage.getHeight());
         oSB.setColor(Color.WHITE);
         this.getMenuElem(0).setPosX(CFG.PADD * 2 + (int)((float)previewImage.getWidth() * CFG.GUI_SCALE) + 1);
         this.getMenuElem(0).setWidthE(Menu_InGame_FA_Top.getWindowWidth() - this.getMenuElem(0).getPosXE());
         this.getMenuElem(1).setPosX(CFG.PADD * 2 + (int)((float)previewImage.getWidth() * CFG.GUI_SCALE) + 1);
         this.getMenuElem(1).setWidthE(Menu_InGame_FA_Top.getWindowWidth() - this.getMenuElem(1).getPosXE());
         this.getMenuElem(2).setPosX(CFG.PADD * 2 + (int)((float)previewImage.getWidth() * CFG.GUI_SCALE) + 1);
         this.getMenuElem(2).setWidthE(Menu_InGame_FA_Top.getWindowWidth() - this.getMenuElem(2).getPosXE());
         this.getMenuElem(15).setPosX(CFG.PADD * 2);
         this.getMenuElem(15).setHeightE(previewImage.getHeight());
         this.getMenuElem(15).setWidthE(previewImage.getWidth());
      }

      super.drawMenuM(oSB, iTranslateX, iTranslateY, sliderMenuIsActive);
      super.endClipM(oSB, iTranslateX, iTranslateY, sliderMenuIsActive);
   }

   public static final void disposePreview() {
      if (previewImage != null) {
         previewImage.getTexture().dispose();
         previewImage = null;
      }
   }

   private final void updateStartingArmyInCapitals(int nArmyBefore) {
      for (int i = 1; i < CFG.core.getCivsSize(); i++) {
         if (CFG.core.getCiv(i).getCapitalProvID() >= 0
            && CFG.core.getProv(CFG.core.getCiv(i).getCapitalProvID()).getCivId() == i
            && CFG.core.getProv(CFG.core.getCiv(i).getCapitalProvID()).getArmyID(0) == nArmyBefore) {
            CFG.core.getProv(CFG.core.getCiv(i).getCapitalProvID()).updateArmy4(CFG.core.getGameScenars().getScenario_StartingArmyInCapitals());
         }
      }
   }

   @Override
   public final void actionEL(int iID) {
      switch (iID) {
         case 0:
            CFG.showKeyboard();
            return;
         case 1:
            CFG.menus.saveCreateScenarioSettings_Data();
            CFG.backToMenu = View.eCREATE_SCENARIO_SETTINGS;
            CFG.menus.setMenuID(View.eSCENARIO_AGE);
            CFG.menus.updateSelecetScenarioAge_Slider();
            return;
         case 2:
            CFG.showKeyboard();
            return;
         case 3:
            int nBefore = CFG.core.getGameScenars().getScenario_StartingArmyInCapitals();
            this.getMenuElem(iID + 1).setCurr(this.getMenuElem(iID + 1).getCurr() - 1);
            this.updateStartingArmyInCapitals(nBefore);
            return;
         case 4:
            int nBefore2 = CFG.core.getGameScenars().getScenario_StartingArmyInCapitals();
            CFG.core.getGameScenars().setScenarioStartingArmyInCapitals(this.getMenuElem(iID).getCurr() * 25);
            this.updateStartingArmyInCapitals(nBefore2);
            return;
         case 5:
            int nBefore3 = CFG.core.getGameScenars().getScenario_StartingArmyInCapitals();
            this.getMenuElem(iID - 1).setCurr(this.getMenuElem(iID - 1).getCurr() + 1);
            this.updateStartingArmyInCapitals(nBefore3);
            return;
         case 6:
            this.getMenuElem(iID + 1).setCurr(this.getMenuElem(iID + 1).getCurr() - 1);
            return;
         case 7:
            CFG.core.getGameScenars().setScenarioStartingPopulation(this.getMenuElem(iID).getCurr() * 100);
            return;
         case 8:
            this.getMenuElem(iID - 1).setCurr(this.getMenuElem(iID - 1).getCurr() + 1);
            return;
         case 9:
            this.getMenuElem(iID + 1).setCurr(this.getMenuElem(iID + 1).getCurr() - 1);
            return;
         case 10:
            CFG.core.getGameScenars().setScenarioStartingEconomy(this.getMenuElem(iID).getCurr() * 100);
            return;
         case 11:
            this.getMenuElem(iID - 1).setCurr(this.getMenuElem(iID - 1).getCurr() + 1);
            return;
         case 12:
            CFG.menus.saveCreateScenarioSettings_Data();
            CFG.core.setActiveProvID(-1);
            CFG.menus.rebuildManageDiplomacy_Alliances();
            CFG.chosenAlphabetCharachter = null;
            CFG.resetManageDiplomacyIDs();
            CFG.backToMenu = View.eCREATE_SCENARIO_SETTINGS;
            CFG.menus.setMenuID(View.eMANAGE_DIPLOMACY);
            RenderProvince.updateDrawProvinces();
            CFG.map.getTouchMgr().ueExA();
            return;
         case 13:
            CFG.menus.saveCreateScenarioSettings_Data();
            CFG.createScenarioAssignProvsCiv = 0;
            CFG.menus.setMenuID(View.eCREATE_SCENARIO_TECHNOLOGY_LEVELS);
            CFG.VIEW_SHOW_VALUES = true;

            for (int ixx = 0; ixx < CFG.core.getProvinSize(); ixx++) {
               if (CFG.core.getProv(ixx).isCapital()) {
                  CFG.core.getProv(ixx).getArmyObject(0).updateArmyWidth(CFG.core.getCiv(CFG.core.getProv(ixx).getCivId()).getTechLevel());
               }
            }

            RenderProvince.updateDrawProvinces();
            return;
         case 14:
            CFG.menus.saveCreateScenarioSettings_Data();
            CFG.core.checkArmies();
            CFG.core.setActiveProvID(-1);
            CFG.core.getProvSelected().clearSelectedProvinces();

            try {
               CFG.menus.setVisible_CreateScenario_SetUpArmies_Neutral(false);
               CFG.menus.setVisible_CreateScenario_SetUpArmies_Civs(false);
               CFG.menus.setVisible_CreateScenario_SetUpArmies_Sliders(false);
            } catch (IndexOutOfBoundsException var6) {
            }

            CFG.MANAGE_DIPLOMACY_CUSTOMIZE_ALLIANCE_ID = 100000;
            CFG.core.sortCivilizationsAZ();
            CFG.menus.setMenuID(View.eCREATE_SCENARIO_SET_UP_ARMY);
            return;
         case 15:
            CFG.menus.saveCreateScenarioSettings_Data();
            CFG.menus.setMenuID(View.eCREATE_SCENARIO_PREVIEW);
            return;
         case 16:
            CFG.menus.saveCreateScenarioSettings_Data();
            CFG.menus.setMenuID(View.eCREATE_SCENARIO_PALLET_OF_COLORS);
            return;
         case 17:
            this.getMenuElem(iID + 1).setCurr(this.getMenuElem(iID + 1).getCurr() - 1);
            return;
         case 18:
            CFG.core.getGameScenars().setScenarioStartingMoney(this.getMenuElem(iID).getCurr() * 50);
            return;
         case 19:
            this.getMenuElem(iID - 1).setCurr(this.getMenuElem(iID - 1).getCurr() + 1);
            return;
         case 20:
            CFG.menus.saveCreateScenarioSettings_Data();
            CFG.createScenarioAssignProvsCiv = 0;
            CFG.menus.setMenuID(View.eCREATE_SCENARIO_STARTING_MONEY);
            CFG.VIEW_SHOW_VALUES = true;

            for (int ix = 0; ix < CFG.core.getProvinSize(); ix++) {
               if (CFG.core.getProv(ix).isCapital()) {
                  CFG.core
                     .getProv(ix)
                     .getArmyObject(0)
                     .updateArmyWidth(
                        (int)(
                           CFG.core.getCiv(CFG.core.getProv(ix).getCivId()).getGold() == -999999L
                              ? (long)CFG.core.getGameScenars().getScenario_StartingMoney()
                              : CFG.core.getCiv(CFG.core.getProv(ix).getCivId()).getGold()
                        )
                     );
               }
            }

            RenderProvince.updateDrawProvinces();
            return;
         case 21:
            CFG.menus.saveCreateScenarioSettings_Data();
            CFG.core.setActiveProvID(-1);
            CFG.core.getProvSelected().clearSelectedProvinces();
            CFG.selectMode = true;
            CFG.brushMode = false;
            CFG.VIEW_SHOW_VALUES = true;
            CFG.menus.setMenuID(View.eCREATE_SCENARIO_CORES);
            return;
         case 22:
            CFG.menus.saveCreateScenarioSettings_Data();
            CFG.menus.setMenuID(View.eCREATE_SCENARIO_HAPPINESS);

            for (int i = 0; i < CFG.core.getProvinSize(); i++) {
               if (CFG.core.getProv(i).isCapital()) {
                  CFG.core.getProv(i).getArmyObject(0).updateArmyWidth(CFG.core.getCiv(CFG.core.getProv(i).getCivId()).getHappiness() + "%");
               }
            }

            RenderProvince.updateDrawProvinces();
            return;
         case 23:
            CFG.menus.saveCreateScenarioSettings_Data();
            CFG.core.setActiveProvID(-1);
            CFG.core.getProvSelected().clearSelectedProvinces();
            CFG.selectMode = true;
            CFG.brushMode = false;
            CFG.VIEW_SHOW_VALUES = true;

            for (int i = 0; i < CFG.hreMgr.getHRE().getProvincesSize(); i++) {
               CFG.core.getProvSelected().addProv(CFG.hreMgr.getHRE().getProvinces(i));
            }

            CFG.menus.setMenuID(View.eCREATE_SCENARIO_HOLY_ROMAN_EMPIRE);
            CFG.toastM.addM(this.getMenuElem(iID).getTextE(), HolyRomanEmpire_Manager.oColorHRE);
            return;
         case 24:
            CFG.menus.saveCreateScenarioSettings_Data();
            CFG.eventsManager.createScenarioEvents = new Event_GameData();
            CFG.menus.setMenuID(View.eCREATE_SCENARIO_EVENTS);
            return;
         case 25:
            CFG.menus.saveCreateScenarioSettings_Data();
            CFG.menus.setMenuID(View.eCREATE_SCENARIO_COLONIZATION);
            return;
         case 26:
            CFG.showKeyboard();
            return;
         case 27:
            this.getMenuElem(iID + 1).setCurr(this.getMenuElem(iID + 1).getCurr() - 1);
            return;
         case 28:
            CFG.core.getGameScenars().setScenario_PopulationGrowthRate_Modifier((float)(this.getMenuElem(iID).getCurr() - 100) / 100.0F);
            return;
         case 29:
            this.getMenuElem(iID - 1).setCurr(this.getMenuElem(iID - 1).getCurr() + 1);
            return;
         case 30:
            this.getMenuElem(iID + 1).setCurr(this.getMenuElem(iID + 1).getCurr() - 1);
            return;
         case 31:
            CFG.core.getGameScenars().setScenario_EconomyGrowthRate_Modifier((float)(this.getMenuElem(iID).getCurr() - 100) / 100.0F);
            return;
         case 32:
            this.getMenuElem(iID - 1).setCurr(this.getMenuElem(iID - 1).getCurr() + 1);
            return;
         case 33:
            this.getMenuElem(iID + 1).setCurr(this.getMenuElem(iID + 1).getCurr() - 1);
            return;
         case 34:
            CFG.core.getGameScenars().setScenario_DiseasesDeathRate_Modifier((float)(this.getMenuElem(iID).getCurr() - 100) / 100.0F);
            return;
         case 35:
            this.getMenuElem(iID - 1).setCurr(this.getMenuElem(iID - 1).getCurr() + 1);
            return;
         case 36:
            CFG.menus.saveCreateScenarioSettings_Data();
            CounterUI.reset();
            CounterUI.mode = CounterUI.MODE_MANAGER;
            CounterUI.returnView = View.eCREATE_SCENARIO_SETTINGS;
            CFG.menus.setMenuID(View.eCREATE_SCENARIO_EVENTS_SELECTDECISION);
            return;
      }
   }
}

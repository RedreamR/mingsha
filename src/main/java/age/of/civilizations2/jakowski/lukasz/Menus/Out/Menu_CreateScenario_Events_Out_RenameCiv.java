package age.of.civilizations2.jakowski.lukasz.Menus.Out;

import age.of.civilizations2.jakowski.lukasz.CFG;
import age.of.civilizations2.jakowski.lukasz.Event_Outcome;
import age.of.civilizations2.jakowski.lukasz.Event_Outcome_Counter_Add;
import age.of.civilizations2.jakowski.lukasz.Event_Outcome_Counter_Apply;
import age.of.civilizations2.jakowski.lukasz.Event_Outcome_Counter_Div;
import age.of.civilizations2.jakowski.lukasz.Event_Outcome_Counter_Mul;
import age.of.civilizations2.jakowski.lukasz.Event_Outcome_Counter_Set;
import age.of.civilizations2.jakowski.lukasz.Event_Outcome_Counter_Sub;
import age.of.civilizations2.jakowski.lukasz.Event_SelectCivAction;
import age.of.civilizations2.jakowski.lukasz.IMGManager;
import age.of.civilizations2.jakowski.lukasz.Images;
import age.of.civilizations2.jakowski.lukasz.Menu;
import age.of.civilizations2.jakowski.lukasz.View;
import age.of.civilizations2.jakowski.lukasz.Button.MenuElemUI;
import age.of.civilizations2.jakowski.lukasz.Button.Classic.Button_Classic;
import age.of.civilizations2.jakowski.lukasz.Button.Classic.Button_Classic_LR_Line;
import age.of.civilizations2.jakowski.lukasz.Title.TitleM;
import team.rainfall.mingsha.counter.CounterUI;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;
import java.util.List;

public class Menu_CreateScenario_Events_Out_RenameCiv extends Menu {
   public String tName;
   private boolean counterMode = false;
   private boolean counterIsSet = false;
   private boolean counterIsApply = false;
   private String counterTitle = "";
   private String counterNameLabel = "Name: ";
   private String counterValueLabel = "";

   private static final Event_Outcome getCurrentOutcome() {
      return CFG.eventsManager
         .createScenarioEvents
         .lDecisions
         .get(CFG.eventsManager.createEvent_EditTriggerID)
         .lOutcomes
         .get(CFG.eventsManager.createEvent_EditConditionID);
   }

   public Menu_CreateScenario_Events_Out_RenameCiv() {
      Event_Outcome tOutcome = null;

      try {
         tOutcome = getCurrentOutcome();
      } catch (IndexOutOfBoundsException var3) {
      }

      this.counterMode = tOutcome instanceof Event_Outcome_Counter_Set
         || tOutcome instanceof Event_Outcome_Counter_Add
         || tOutcome instanceof Event_Outcome_Counter_Sub
         || tOutcome instanceof Event_Outcome_Counter_Mul
         || tOutcome instanceof Event_Outcome_Counter_Div
         || tOutcome instanceof Event_Outcome_Counter_Apply;
      if (this.counterMode) {
         this.counterIsSet = tOutcome instanceof Event_Outcome_Counter_Set;
         this.counterIsApply = tOutcome instanceof Event_Outcome_Counter_Apply;
         if (this.counterIsApply) {
            this.counterTitle = "Counter APPLY (expr)";
            this.counterNameLabel = "Target: ";
            this.counterValueLabel = "Expr: ";
         } else if (this.counterIsSet) {
            this.counterTitle = "Counter SET (expr)";
            this.counterValueLabel = "Expr: ";
         } else if (tOutcome instanceof Event_Outcome_Counter_Add) {
            this.counterTitle = "Counter +";
            this.counterValueLabel = "Value: ";
         } else if (tOutcome instanceof Event_Outcome_Counter_Sub) {
            this.counterTitle = "Counter -";
            this.counterValueLabel = "Value: ";
         } else if (tOutcome instanceof Event_Outcome_Counter_Mul) {
            this.counterTitle = "Counter *";
            this.counterValueLabel = "Value: ";
         } else {
            this.counterTitle = "Counter /";
            this.counterValueLabel = "Value: ";
         }
      }

      List<MenuElemUI> menuElements = new ArrayList<>();
      int tY = CFG.PADD;
      menuElements.add(new Button_Classic_LR_Line(null, -1, 0, tY, CFG.GAMEWIDTH, CFG.BUTTON_H, true));
      menuElements.add(new Button_Classic(null, (int)(50.0F * CFG.GUI_SCALE), 0, tY, CFG.GAMEWIDTH, CFG.BUTTON_H, true));
      tY += menuElements.get(menuElements.size() - 1).getHeightE() + CFG.PADD;
      this.tName = CFG.lang.get("CivilizationName") + ": ";
      if (this.counterMode) {
         menuElements.add(
            new Button_Classic(
               this.counterIsApply ? Event_Outcome_Counter_Apply.targetName(tOutcome.getValue()) : tOutcome.getText(),
               (int)(50.0F * CFG.GUI_SCALE),
               0,
               tY,
               CFG.GAMEWIDTH,
               CFG.BUTTON_H,
               true
            ) {
               @Override
               public String getTextToDrawElem() {
                  return Menu_CreateScenario_Events_Out_RenameCiv.this.counterNameLabel + super.getTextToDrawElem();
               }
            }
         );
         tY += menuElements.get(menuElements.size() - 1).getHeightE() + CFG.PADD;
         String tInitValue;
         if (this.counterIsApply) {
            tInitValue = ((Event_Outcome_Counter_Apply)tOutcome).expStr;
         } else if (this.counterIsSet) {
            tInitValue = ((Event_Outcome_Counter_Set)tOutcome).expStr;
         } else {
            tInitValue = String.valueOf(tOutcome.getValue());
         }
         menuElements.add(
            new Button_Classic(tInitValue, (int)(50.0F * CFG.GUI_SCALE), 0, tY, CFG.GAMEWIDTH, CFG.BUTTON_H, true) {
               @Override
               public String getTextToDrawElem() {
                  return Menu_CreateScenario_Events_Out_RenameCiv.this.counterValueLabel + super.getTextToDrawElem();
               }
            }
         );
         tY += menuElements.get(menuElements.size() - 1).getHeightE() + CFG.PADD;
         menuElements.add(new Button_Classic("[ Counters ]", (int)(50.0F * CFG.GUI_SCALE), 0, tY, CFG.GAMEWIDTH, CFG.BUTTON_H, true));
         tY += menuElements.get(menuElements.size() - 1).getHeightE() + CFG.PADD;
      } else {
         menuElements.add(
            new Button_Classic(
               getCurrentOutcome().getText(),
               (int)(50.0F * CFG.GUI_SCALE),
               0,
               tY,
               CFG.GAMEWIDTH,
               CFG.BUTTON_H,
               true
            ) {
               @Override
               public String getTextToDrawElem() {
                  return Menu_CreateScenario_Events_Out_RenameCiv.this.tName + super.getTextToDrawElem();
               }
            }
         );
         tY += menuElements.get(menuElements.size() - 1).getHeightE() + CFG.PADD;
      }

      this.initMenuWithBackButton(
         new TitleM(null, CFG.BUTTON_H * 3 / 4, false, false), 0, CFG.BUTTON_H * 3 / 4, CFG.GAMEWIDTH, CFG.GAMEHEIGHT - CFG.BUTTON_H * 3 / 4, menuElements
      );
      this.updateLang();
   }

   @Override
   public void updateLang() {
      this.getMenuElem(0).setTextE(CFG.lang.get("Save"));

      try {
         this.getMenuElem(1)
            .setTextE(
               getCurrentOutcome().getCivID() >= 0
                  ? CFG.lang.get("SelectCivilization") + ": " + CFG.core.getCiv(getCurrentOutcome().getCivID()).getCivName()
                  : CFG.lang.get("SelectCivilization")
            );
      } catch (IndexOutOfBoundsException var2) {
         this.getMenuElem(1).setTextE(CFG.lang.get("SelectCivilization"));
      }

      this.getTitleM()
         .setText(
            this.counterMode
               ? this.counterTitle
               : CFG.lang.get("CivilizationName")
         );
   }

   @Override
   public void draw(SpriteBatch oSB, int iTranslateX, int iTranslateY, boolean sliderMenuIsActive) {
      super.beginClipM(oSB, iTranslateX, iTranslateY, sliderMenuIsActive);
      super.drawMenuM(oSB, iTranslateX, iTranslateY, sliderMenuIsActive);
      int tempButtonID = 1;

      try {
         CFG.core
            .getCiv(getCurrentOutcome().getCivID())
            .getFlagC()
            .drawO(
               oSB,
               this.getMenuElem(tempButtonID).getPosXE() + this.getMenuElem(tempButtonID).getTextPosElem() / 2 - CFG.CIV_FLAG_WIDTH / 2 + iTranslateX,
               -CFG.core.getCiv(getCurrentOutcome().getCivID()).getFlagC().getHeight()
                  + this.getMenuElem(tempButtonID).getPosY()
                  + this.getMenuPosY()
                  + this.getMenuElem(tempButtonID).getHeightE() / 2
                  - CFG.CIV_FLAG_HEIGHT / 2
                  + iTranslateY,
               CFG.CIV_FLAG_WIDTH,
               CFG.CIV_FLAG_HEIGHT
            );
      } catch (IndexOutOfBoundsException var7) {
         IMGManager.getIMG(Images.randomCivilizationFlag)
            .drawO(
               oSB,
               this.getMenuElem(tempButtonID).getPosXE() + this.getMenuElem(tempButtonID).getTextPosElem() / 2 - CFG.CIV_FLAG_WIDTH / 2 + iTranslateX,
               this.getMenuElem(tempButtonID).getPosY()
                  + this.getMenuPosY()
                  - IMGManager.getIMG(Images.randomCivilizationFlag).getHeight()
                  + this.getMenuElem(tempButtonID).getHeightE() / 2
                  - CFG.CIV_FLAG_HEIGHT / 2
                  + iTranslateY,
               CFG.CIV_FLAG_WIDTH,
               CFG.CIV_FLAG_HEIGHT
            );
      }

      IMGManager.getIMG(Images.flagRectSmall)
         .drawO(
            oSB,
            this.getMenuElem(tempButtonID).getPosXE() + this.getMenuElem(tempButtonID).getTextPosElem() / 2 - CFG.CIV_FLAG_WIDTH / 2 + iTranslateX,
            this.getMenuElem(tempButtonID).getPosY()
               + this.getMenuPosY()
               + this.getMenuElem(tempButtonID).getHeightE() / 2
               - CFG.CIV_FLAG_HEIGHT / 2
               + iTranslateY
         );
      super.endClipM(oSB, iTranslateX, iTranslateY, sliderMenuIsActive);
   }

   private final void saveCounterData() {
      try {
         Event_Outcome tOutcome = getCurrentOutcome();
         String tValue = this.getMenuElem(3).getTextE() == null ? "" : this.getMenuElem(3).getTextE().trim();
         if (this.counterIsApply) {
            ((Event_Outcome_Counter_Apply)tOutcome).expStr = tValue;
            return;
         }
         tOutcome.setText(this.getMenuElem(2).getTextE() == null ? "" : this.getMenuElem(2).getTextE().trim());
         if (this.counterIsSet) {
            ((Event_Outcome_Counter_Set)tOutcome).expStr = tValue;
         } else {
            try {
               tOutcome.setValue(Integer.parseInt(tValue));
            } catch (NumberFormatException var4) {
            }
         }
      } catch (IndexOutOfBoundsException var5) {
      }
   }

   @Override
   public final void actionEL(int iID) {
      if (this.counterMode) {
         switch (iID) {
            case 0:
               this.saveCounterData();
               this.onBackPressed();
               break;
            case 1:
               this.saveCounterData();
               CFG.eventsManager.eSelectCivAction = Event_SelectCivAction.OUT_SELECTCIV_RENAMECIV;
               CFG.menus.setMenuID(View.eCREATE_SCENARIO_EVENTS_SELECT_CIV);
               break;
            case 2:
               if (this.counterIsApply) {
                  Event_Outcome tOutcome = getCurrentOutcome();
                  tOutcome.setValue(tOutcome.getValue() + 1);
                  this.getMenuElem(2).setTextE(Event_Outcome_Counter_Apply.targetName(tOutcome.getValue()));
               } else {
                  CFG.showKeyboard();
               }
               break;
            case 3:
               CFG.showKeyboard();
               break;
            case 4:
               this.saveCounterData();
               CounterUI.mode = CounterUI.MODE_PICK_OUT;
               CounterUI.pickAppend = this.counterIsApply || this.counterIsSet;
               CounterUI.filter = "";
               CounterUI.returnView = View.eCREATE_SCENARIO_EVENTS_OUT_RENAME_CIV;
               CFG.menus.setMenuID(View.eCREATE_SCENARIO_EVENTS_SELECTDECISION);
         }
      } else {
         switch (iID) {
            case 0:
               getCurrentOutcome().setText(this.getMenuElem(2).getTextE());
               this.onBackPressed();
               break;
            case 1:
               CFG.eventsManager.eSelectCivAction = Event_SelectCivAction.OUT_SELECTCIV_RENAMECIV;
               CFG.menus.setMenuID(View.eCREATE_SCENARIO_EVENTS_SELECT_CIV);
               break;
            case 2:
               CFG.showKeyboard();
         }
      }
   }

   @Override
   public final void onBackPressed() {
      CFG.menus.setMenuID(View.eCREATE_SCENARIO_EVENTS_DECISION);
      CFG.menus.setBackAnimation(true);
   }
}

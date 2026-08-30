package age.of.civilizations2.jakowski.lukasz.Menus.Cond;

import age.of.civilizations2.jakowski.lukasz.CFG;
import age.of.civilizations2.jakowski.lukasz.Event_Conditions;
import age.of.civilizations2.jakowski.lukasz.Event_Conditions_Counter_EqualTo;
import age.of.civilizations2.jakowski.lukasz.Event_Conditions_Counter_If;
import age.of.civilizations2.jakowski.lukasz.Event_Conditions_Counter_LessThan;
import age.of.civilizations2.jakowski.lukasz.Event_Conditions_Counter_MoreThan;
import age.of.civilizations2.jakowski.lukasz.Event_Conditions_NS_Has;
import age.of.civilizations2.jakowski.lukasz.Event_SelectCivAction;
import age.of.civilizations2.jakowski.lukasz.Event_Type;
import age.of.civilizations2.jakowski.lukasz.IMGManager;
import age.of.civilizations2.jakowski.lukasz.Images;
import age.of.civilizations2.jakowski.lukasz.Menu;
import age.of.civilizations2.jakowski.lukasz.View;
import age.of.civilizations2.jakowski.lukasz.Button.MenuElemUI;
import age.of.civilizations2.jakowski.lukasz.Button.Classic.Button_Classic;
import age.of.civilizations2.jakowski.lukasz.Button.Classic.Button_Classic_LR_Line;
import age.of.civilizations2.jakowski.lukasz.Title.TitleM;
import team.rainfall.mingsha.counter.CounterUI;
import team.rainfall.mingsha.ns.NSUI;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;
import java.util.List;

public class Menu_CreateScenario_Events_Cond_DecisionTaken extends Menu {
   private boolean counterMode = false;
   private boolean counterHasValue = false;
   private String counterTitle = "";
   private String counterTextLabel = "";
   private String counterValueLabel = "";
   private boolean nsMode = false;

   private static final Event_Conditions getCurrentCondition() {
      return CFG.eventsManager
         .createScenarioEvents
         .getTrigger(CFG.eventsManager.createEvent_EditTriggerID)
         .lConditions
         .get(CFG.eventsManager.createEvent_EditConditionID);
   }

   public Menu_CreateScenario_Events_Cond_DecisionTaken() {
      Event_Conditions tCond = null;

      try {
         tCond = getCurrentCondition();
      } catch (IndexOutOfBoundsException var3) {
      }

      this.counterMode = tCond instanceof Event_Conditions_Counter_EqualTo
         || tCond instanceof Event_Conditions_Counter_LessThan
         || tCond instanceof Event_Conditions_Counter_MoreThan
         || tCond instanceof Event_Conditions_Counter_If;
      if (this.counterMode) {
         this.counterHasValue = !(tCond instanceof Event_Conditions_Counter_If);
         if (tCond instanceof Event_Conditions_Counter_EqualTo) {
            this.counterTitle = "Counter ==";
         } else if (tCond instanceof Event_Conditions_Counter_LessThan) {
            this.counterTitle = "Counter <";
         } else if (tCond instanceof Event_Conditions_Counter_MoreThan) {
            this.counterTitle = "Counter >";
         } else {
            this.counterTitle = "Counter IF (expr)";
         }

         this.counterTextLabel = this.counterHasValue ? "Name: " : "Expr: ";
         this.counterValueLabel = "Value: ";
      }

      this.nsMode = tCond instanceof Event_Conditions_NS_Has;

      List<MenuElemUI> menuElements = new ArrayList<>();
      int tY = CFG.PADD;
      menuElements.add(new Button_Classic_LR_Line(null, -1, 0, tY, CFG.GAMEWIDTH, CFG.BUTTON_H, true));
      menuElements.add(
         new Button_Classic_LR_Line(null, -1, 0, tY, CFG.GAMEWIDTH / 3, CFG.BUTTON_H, true) {
            @Override
            public Color getColorE(boolean isActive) {
               return getCurrentCondition().conditionType == Event_Type.AND ? CFG.COLOR_POSITIVE : super.getColorE(isActive);
            }
         }
      );
      menuElements.add(
         new Button_Classic_LR_Line(null, -1, CFG.GAMEWIDTH / 3, tY, CFG.GAMEWIDTH / 3, CFG.BUTTON_H, true) {
            @Override
            public Color getColorE(boolean isActive) {
               return getCurrentCondition().conditionType == Event_Type.NOT ? CFG.COLOR_POSITIVE : super.getColorE(isActive);
            }
         }
      );
      menuElements.add(
         new Button_Classic_LR_Line(null, -1, CFG.GAMEWIDTH - CFG.GAMEWIDTH / 3, tY, CFG.GAMEWIDTH - CFG.GAMEWIDTH / 3 * 2, CFG.BUTTON_H, true) {
            @Override
            public Color getColorE(boolean isActive) {
               return getCurrentCondition().conditionType == Event_Type.OR ? CFG.COLOR_POSITIVE : super.getColorE(isActive);
            }
         }
      );
      tY += menuElements.get(menuElements.size() - 1).getHeightE() + CFG.PADD;
      menuElements.add(new Button_Classic(null, (int)(50.0F * CFG.GUI_SCALE), 0, tY, CFG.GAMEWIDTH, CFG.BUTTON_H, true));
      tY += menuElements.get(menuElements.size() - 1).getHeightE() + CFG.PADD;
      if (this.counterMode) {
         menuElements.add(
            new Button_Classic(tCond.getText(), (int)(50.0F * CFG.GUI_SCALE), 0, tY, CFG.GAMEWIDTH, CFG.BUTTON_H, true) {
               @Override
               public String getTextToDrawElem() {
                  return Menu_CreateScenario_Events_Cond_DecisionTaken.this.counterTextLabel + super.getTextToDrawElem();
               }
            }
         );
         tY += menuElements.get(menuElements.size() - 1).getHeightE() + CFG.PADD;
         if (this.counterHasValue) {
            menuElements.add(
               new Button_Classic(String.valueOf(tCond.getValue()), (int)(50.0F * CFG.GUI_SCALE), 0, tY, CFG.GAMEWIDTH, CFG.BUTTON_H, true) {
                  @Override
                  public String getTextToDrawElem() {
                     return Menu_CreateScenario_Events_Cond_DecisionTaken.this.counterValueLabel + super.getTextToDrawElem();
                  }
               }
            );
            tY += menuElements.get(menuElements.size() - 1).getHeightE() + CFG.PADD;
         }

         menuElements.add(new Button_Classic("[ Counters ]", (int)(50.0F * CFG.GUI_SCALE), 0, tY, CFG.GAMEWIDTH, CFG.BUTTON_H, true));
         tY += menuElements.get(menuElements.size() - 1).getHeightE() + CFG.PADD;
      } else if (this.nsMode) {
         menuElements.add(
            new Button_Classic(tCond.getText(), (int)(50.0F * CFG.GUI_SCALE), 0, tY, CFG.GAMEWIDTH, CFG.BUTTON_H, true) {
               @Override
               public String getTextToDrawElem() {
                  return "Spirit: " + super.getTextToDrawElem();
               }
            }
         );
         tY += menuElements.get(menuElements.size() - 1).getHeightE() + CFG.PADD;
         menuElements.add(
            new Button_Classic("[ National Spirits ]", (int)(50.0F * CFG.GUI_SCALE), 0, tY, CFG.GAMEWIDTH, CFG.BUTTON_H, true)
         );
         tY += menuElements.get(menuElements.size() - 1).getHeightE() + CFG.PADD;
      } else {
         menuElements.add(new Button_Classic(null, (int)(50.0F * CFG.GUI_SCALE), 0, tY, CFG.GAMEWIDTH, CFG.BUTTON_H, true));
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
      this.getMenuElem(1).setTextE(CFG.lang.get("AND"));
      this.getMenuElem(2).setTextE(CFG.lang.get("NOT"));
      this.getMenuElem(3).setTextE(CFG.lang.get("OR"));

      try {
         this.getMenuElem(4)
            .setTextE(
               getCurrentCondition().getCivID() > 0
                  ? CFG.lang.get("Civilization") + ": " + CFG.core.getCiv(getCurrentCondition().getCivID()).getCivName()
                  : CFG.lang.get("SelectCivilization")
            );
      } catch (IndexOutOfBoundsException var5) {
         this.getMenuElem(4).setTextE(CFG.lang.get("SelectCivilization"));
      }

      if (this.counterMode) {
         this.getTitleM().setText(this.counterTitle);
         return;
      }

      if (this.nsMode) {
         this.getTitleM().setText(CFG.lang.get("NSSelectTitle"));
         return;
      }

      String tName = "";

      try {
         if (getCurrentCondition().getText().length() > 0) {
            String[] tData = getCurrentCondition().getText().split("_");
            int tID = Integer.parseInt(tData[1]);

            for (int i = 0; i < CFG.eventsManager.getEventsSize(); i++) {
               if (tData[0].equals(CFG.eventsManager.getEvent(i).getEventTag())) {
                  tName = CFG.eventsManager.getEvent(i).lDecisions.get(tID).sTitle;
               }
            }
         }
      } catch (IndexOutOfBoundsException var6) {
      } catch (IllegalArgumentException var7) {
      }

      this.getMenuElem(5).setTextE(CFG.lang.get("Decision") + ": " + tName);
      this.getTitleM().setText(CFG.lang.get("DecisionTaken"));
   }

   @Override
   public void draw(SpriteBatch oSB, int iTranslateX, int iTranslateY, boolean sliderMenuIsActive) {
      super.beginClipM(oSB, iTranslateX, iTranslateY, sliderMenuIsActive);
      super.drawMenuM(oSB, iTranslateX, iTranslateY, sliderMenuIsActive);
      int tempButtonID = 4;

      try {
         CFG.core
            .getCiv(getCurrentCondition().getCivID())
            .getFlagC()
            .drawO(
               oSB,
               this.getMenuElem(tempButtonID).getPosXE() + this.getMenuElem(tempButtonID).getTextPosElem() / 2 - CFG.CIV_FLAG_WIDTH / 2 + iTranslateX,
               -CFG.core.getCiv(getCurrentCondition().getCivID()).getFlagC().getHeight()
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
                  - IMGManager.getIMG(Images.randomCivilizationFlag).getHeight()
                  + this.getMenuPosY()
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
         Event_Conditions tCond = getCurrentCondition();
         tCond.setText(this.getMenuElem(5).getTextE() == null ? "" : this.getMenuElem(5).getTextE().trim());
         if (this.counterHasValue) {
            try {
               tCond.setValue(Integer.parseInt(this.getMenuElem(6).getTextE().trim()));
            } catch (NumberFormatException var3) {
            }
         }
      } catch (IndexOutOfBoundsException var4) {
      }
   }

   /** Writes the spirit id back into the condition. It carries no value field. */
   private final void saveNSData() {
      try {
         getCurrentCondition().setText(this.getMenuElem(5).getTextE() == null ? "" : this.getMenuElem(5).getTextE().trim());
      } catch (IndexOutOfBoundsException var2) {
      }
   }

   @Override
   public final void actionEL(int iID) {
      if (this.nsMode) {
         switch (iID) {
            case 0:
               this.saveNSData();
               this.onBackPressed();
               break;
            case 1:
               getCurrentCondition().conditionType = Event_Type.AND;
               CFG.toastM.addM(this.getMenuElem(iID).getTextE(), CFG.COLOR_HOVER_TITLE);
               break;
            case 2:
               getCurrentCondition().conditionType = Event_Type.NOT;
               CFG.toastM.addM(this.getMenuElem(iID).getTextE(), CFG.COLOR_HOVER_TITLE);
               break;
            case 3:
               getCurrentCondition().conditionType = Event_Type.OR;
               CFG.toastM.addM(this.getMenuElem(iID).getTextE(), CFG.COLOR_HOVER_TITLE);
               break;
            case 4:
               this.saveNSData();
               CFG.eventsManager.eSelectCivAction = Event_SelectCivAction.COND_SELECTCIV_DECISIONTAKEN;
               CFG.menus.setMenuID(View.eCREATE_SCENARIO_EVENTS_SELECT_CIV);
               break;
            case 5:
               CFG.showKeyboard();
               break;
            case 6:
               this.openNSPicker();
         }
      } else if (this.counterMode) {
         switch (iID) {
            case 0:
               this.saveCounterData();
               this.onBackPressed();
               break;
            case 1:
               getCurrentCondition().conditionType = Event_Type.AND;
               CFG.toastM.addM(this.getMenuElem(iID).getTextE(), CFG.COLOR_HOVER_TITLE);
               break;
            case 2:
               getCurrentCondition().conditionType = Event_Type.NOT;
               CFG.toastM.addM(this.getMenuElem(iID).getTextE(), CFG.COLOR_HOVER_TITLE);
               break;
            case 3:
               getCurrentCondition().conditionType = Event_Type.OR;
               CFG.toastM.addM(this.getMenuElem(iID).getTextE(), CFG.COLOR_HOVER_TITLE);
               break;
            case 4:
               this.saveCounterData();
               CFG.eventsManager.eSelectCivAction = Event_SelectCivAction.COND_SELECTCIV_DECISIONTAKEN;
               CFG.menus.setMenuID(View.eCREATE_SCENARIO_EVENTS_SELECT_CIV);
               break;
            case 5:
            case 6:
               if (iID == 6 && !this.counterHasValue) {
                  this.openCounterPicker();
               } else {
                  CFG.showKeyboard();
               }
               break;
            case 7:
               this.openCounterPicker();
         }
      } else {
         switch (iID) {
            case 0:
               this.onBackPressed();
               break;
            case 1:
               getCurrentCondition().conditionType = Event_Type.AND;
               CFG.toastM.addM(this.getMenuElem(iID).getTextE(), CFG.COLOR_HOVER_TITLE);
               break;
            case 2:
               getCurrentCondition().conditionType = Event_Type.NOT;
               CFG.toastM.addM(this.getMenuElem(iID).getTextE(), CFG.COLOR_HOVER_TITLE);
               break;
            case 3:
               getCurrentCondition().conditionType = Event_Type.OR;
               CFG.toastM.addM(this.getMenuElem(iID).getTextE(), CFG.COLOR_HOVER_TITLE);
               break;
            case 4:
               CFG.eventsManager.eSelectCivAction = Event_SelectCivAction.COND_SELECTCIV_DECISIONTAKEN;
               CFG.menus.setMenuID(View.eCREATE_SCENARIO_EVENTS_SELECT_CIV);
               break;
            case 5:
               CFG.eventsManager.eSelectCivAction = Event_SelectCivAction.COND_SELECTDECISION_DECISIONTAKEN;
               CFG.menus.setMenuID(View.eCREATE_SCENARIO_EVENTS_SELECTDECISION);
         }
      }
   }

   /**
    * Hands over to the national spirit list in picking mode. The list is a view
    * {@code MixinMenuManager} creates on demand, so the {@code View} passed to
    * {@code setMenuID} is only what the request falls back to.
    */
   private final void openNSPicker() {
      this.saveNSData();
      NSUI.mode = NSUI.MODE_PICK_COND;
      NSUI.filter = "";
      NSUI.returnView = View.eCREATE_SCENARIO_EVENTS_COND_DECISIONTAKEN;
      NSUI.requestView(NSUI.REQ_LIST);
      CFG.menus.setMenuID(View.eCREATE_SCENARIO_EVENTS_COND_DECISIONTAKEN);
   }

   private final void openCounterPicker() {
      this.saveCounterData();
      CounterUI.mode = CounterUI.MODE_PICK_COND;
      CounterUI.pickAppend = !this.counterHasValue;
      CounterUI.filter = "";
      CounterUI.returnView = View.eCREATE_SCENARIO_EVENTS_COND_DECISIONTAKEN;
      CFG.menus.setMenuID(View.eCREATE_SCENARIO_EVENTS_SELECTDECISION);
   }

   @Override
   public final void onBackPressed() {
      CFG.menus.setMenuID(View.eCREATE_SCENARIO_EVENTS_TRIGGER);
      CFG.menus.setBackAnimation(true);
   }
}

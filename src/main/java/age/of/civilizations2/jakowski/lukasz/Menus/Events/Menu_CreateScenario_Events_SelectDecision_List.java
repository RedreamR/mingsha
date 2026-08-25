package age.of.civilizations2.jakowski.lukasz.Menus.Events;

import age.of.civilizations2.jakowski.lukasz.CFG;
import age.of.civilizations2.jakowski.lukasz.Event_Conditions;
import age.of.civilizations2.jakowski.lukasz.Event_Outcome;
import age.of.civilizations2.jakowski.lukasz.GameCalendar;
import age.of.civilizations2.jakowski.lukasz.Menu;
import age.of.civilizations2.jakowski.lukasz.View;
import age.of.civilizations2.jakowski.lukasz.Button.MenuElemUI;
import age.of.civilizations2.jakowski.lukasz.Button.Classic.Button_Classic;
import age.of.civilizations2.jakowski.lukasz.Button.Classic.Button_Classic_Description;
import java.util.ArrayList;
import java.util.List;
import team.rainfall.mingsha.counter.CounterDef;
import team.rainfall.mingsha.counter.CounterDefs;
import team.rainfall.mingsha.counter.CounterUI;

public class Menu_CreateScenario_Events_SelectDecision_List extends Menu {
   private final List<String> shownNames = new ArrayList<>();
   private boolean counterMode = false;
   private boolean editMode = false;

   public Menu_CreateScenario_Events_SelectDecision_List() {
      this.counterMode = CounterUI.mode != CounterUI.MODE_VANILLA;
      this.editMode = CounterUI.mode == CounterUI.MODE_EDIT;
      List<MenuElemUI> menuElements = new ArrayList<>();
      if (this.counterMode) {
         int nPosY = 0;
         if (this.editMode) {
            CounterDef tDef = CounterDefs.find(CounterUI.editName);
            String tName = tDef != null ? tDef.name : CounterUI.editName;
            String tInitial = tDef != null ? String.valueOf(tDef.initialValue) : "0";
            String tDisplay = tDef != null ? tDef.displayName : "";
            menuElements.add(this.labeledInput("Name: ", tName, nPosY++));
            menuElements.add(this.labeledInput("Initial: ", tInitial, nPosY++));
            menuElements.add(this.labeledInput("Display: ", tDisplay, nPosY++));
            menuElements.add(
               new Button_Classic(CFG.lang.get("Save"), (int)(50.0F * CFG.GUI_SCALE), 0, CFG.BUTTON_H * nPosY + CFG.PADD * (nPosY + 1), CFG.GAMEWIDTH, CFG.BUTTON_H, true)
            );
            nPosY++;
            menuElements.add(
               new Button_Classic(CFG.lang.get("Delete"), (int)(50.0F * CFG.GUI_SCALE), 0, CFG.BUTTON_H * nPosY + CFG.PADD * (nPosY + 1), CFG.GAMEWIDTH, CFG.BUTTON_H, true)
            );
            nPosY++;
         } else {
            menuElements.add(this.labeledInput("Search: ", CounterUI.filter, nPosY++));
            menuElements.add(
               new Button_Classic("+ Counter", (int)(50.0F * CFG.GUI_SCALE), 0, CFG.BUTTON_H * nPosY + CFG.PADD * (nPosY + 1), CFG.GAMEWIDTH, CFG.BUTTON_H, true)
            );
            nPosY++;
            String tFilter = CounterUI.filter == null ? "" : CounterUI.filter.trim().toLowerCase();
            for (CounterDef tDef : CounterDefs.list()) {
               if (!tFilter.isEmpty()
                  && !tDef.name.toLowerCase().contains(tFilter)
                  && !(tDef.displayName != null && tDef.displayName.toLowerCase().contains(tFilter))) {
                  continue;
               }
               this.shownNames.add(tDef.name);
               menuElements.add(
                  new Button_Classic_Description(
                     CounterDefs.displayOf(tDef.name) + "  [" + CFG.lang.get("Value") + ": " + tDef.initialValue + "]",
                     tDef.name,
                     (int)(50.0F * CFG.GUI_SCALE),
                     0,
                     CFG.BUTTON_H * nPosY + CFG.PADD * (nPosY + 1),
                     CFG.GAMEWIDTH,
                     CFG.BUTTON_H,
                     true
                  )
               );
               nPosY++;
            }
         }
      } else {
         int nPosY = 0;

         for (int i = 0; i < CFG.eventsManager.getEventsSize(); i++) {
            for (int j = 0; j < CFG.eventsManager.getEvent(i).lDecisions.size(); j++) {
               CFG.eventsManager.iCreateEvent_Day = CFG.eventsManager.getEvent(i).getEventDate_Since().iEventDay;
               CFG.eventsManager.iCreateEvent_Month = CFG.eventsManager.getEvent(i).getEventDate_Since().iEventMonth;
               CFG.eventsManager.iCreateEvent_Year = CFG.eventsManager.getEvent(i).getEventDate_Since().iEventYear;
               menuElements.add(
                  new Button_Classic_Description(
                     CFG.eventsManager.iCreateEvent_Year == 9999999 ? CFG.lang.get("NoDate") : GameCalendar.getCurrDate_CreateEvent(),
                     CFG.eventsManager.getEvent(i).lDecisions.get(j).sTitle
                        + " - ["
                        + CFG.eventsManager.getEvent(i).getEventName()
                        + ", "
                        + (
                           CFG.eventsManager.getEvent(i).getCivID() >= 0 && CFG.eventsManager.getEvent(i).getCivID() < CFG.core.getCivsSize()
                              ? CFG.core.getCiv(CFG.eventsManager.getEvent(i).getCivID()).getCivName()
                              : CFG.lang.get("AnyCivilization")
                        )
                        + "]",
                     (int)(50.0F * CFG.GUI_SCALE),
                     0,
                     CFG.BUTTON_H * nPosY + CFG.PADD * (nPosY + 1),
                     CFG.GAMEWIDTH,
                     CFG.BUTTON_H,
                     i != CFG.eventsManager.createEvent_EditEventID
                  )
               );
               nPosY++;
            }
         }
      }

      this.initMenu(null, 0, CFG.BUTTON_H * 3 / 4, CFG.GAMEWIDTH, CFG.GAMEHEIGHT - CFG.BUTTON_H * 3 / 4 - CFG.BUTTON_H - CFG.PADD, menuElements, true, false);
      this.updateLang();
   }

   private final MenuElemUI labeledInput(final String nLabel, String nValue, int nPosY) {
      return new Button_Classic(nValue, (int)(50.0F * CFG.GUI_SCALE), 0, CFG.BUTTON_H * nPosY + CFG.PADD * (nPosY + 1), CFG.GAMEWIDTH, CFG.BUTTON_H, true) {
         @Override
         public String getTextToDrawElem() {
            return nLabel + super.getTextToDrawElem();
         }
      };
   }

   @Override
   public void updateLang() {
   }

   private final void rebuild() {
      CFG.menus.setMenuIDWithoutAnim(View.eCREATE_SCENARIO_EVENTS_SELECTDECISION);
   }

   private final void pickCounter(String nName) {
      try {
         if (CounterUI.mode == CounterUI.MODE_PICK_COND) {
            Event_Conditions tCond = CFG.eventsManager
               .createScenarioEvents
               .getTrigger(CFG.eventsManager.createEvent_EditTriggerID)
               .lConditions
               .get(CFG.eventsManager.createEvent_EditConditionID);
            if (CounterUI.pickAppend) {
               tCond.setText((tCond.getText() == null ? "" : tCond.getText()) + "$" + nName);
            } else {
               tCond.setText(nName);
            }
         } else if (CounterUI.mode == CounterUI.MODE_PICK_OUT) {
            Event_Outcome tOutcome = CFG.eventsManager
               .createScenarioEvents
               .lDecisions
               .get(CFG.eventsManager.createEvent_EditTriggerID)
               .lOutcomes
               .get(CFG.eventsManager.createEvent_EditConditionID);
            if (CounterUI.pickAppend) {
               tOutcome.setText((tOutcome.getText() == null ? "" : tOutcome.getText()) + "$" + nName);
            } else {
               tOutcome.setText(nName);
            }
         }
      } catch (IndexOutOfBoundsException ignored) {
      }

      View tBack = CounterUI.returnView;
      CounterUI.reset();
      if (tBack != null) {
         CFG.menus.setMenuID(tBack);
         CFG.menus.setBackAnimation(true);
      }
   }

   @Override
   public final void actionEL(int iID) {
      if (this.counterMode) {
         if (this.editMode) {
            switch (iID) {
               case 0:
               case 1:
               case 2:
                  CFG.showKeyboard();
                  return;
               case 3: {
                  List<CounterDef> defs = CounterDefs.list();
                  for (CounterDef tDef : defs) {
                     if (tDef.name.equals(CounterUI.editName)) {
                        String nName = this.getMenuElem(0).getTextE() == null ? "" : this.getMenuElem(0).getTextE().trim();
                        if (!nName.isEmpty()) {
                           tDef.name = nName;
                        }
                        try {
                           tDef.initialValue = Integer.parseInt(this.getMenuElem(1).getTextE().trim());
                        } catch (NumberFormatException ignored) {
                        }
                        tDef.displayName = this.getMenuElem(2).getTextE() == null ? "" : this.getMenuElem(2).getTextE().trim();
                        break;
                     }
                  }
                  CounterDefs.save(defs);
                  CounterUI.mode = CounterUI.MODE_MANAGER;
                  CounterUI.editName = "";
                  this.rebuild();
                  return;
               }
               case 4: {
                  List<CounterDef> defs = CounterDefs.list();
                  for (int i = 0; i < defs.size(); i++) {
                     if (defs.get(i).name.equals(CounterUI.editName)) {
                        defs.remove(i);
                        break;
                     }
                  }
                  CounterDefs.save(defs);
                  CounterUI.mode = CounterUI.MODE_MANAGER;
                  CounterUI.editName = "";
                  this.rebuild();
                  return;
               }
            }
         } else {
            switch (iID) {
               case 0:
                  CFG.showKeyboard();
                  CFG.keyboardSave = new CFG.Keyboard_Action() {
                     @Override
                     public void action() {
                        CounterUI.filter = CFG.keybMess == null ? "" : CFG.keybMess.trim();
                        Menu_CreateScenario_Events_SelectDecision_List.this.rebuild();
                     }
                  };
                  return;
               case 1: {
                  String nName = CounterDefs.createNewCounter();
                  if (CounterUI.mode == CounterUI.MODE_MANAGER) {
                     CounterUI.mode = CounterUI.MODE_EDIT;
                     CounterUI.editName = nName;
                     this.rebuild();
                  } else {
                     this.pickCounter(nName);
                  }
                  return;
               }
               default: {
                  int tIndex = iID - 2;
                  if (tIndex >= 0 && tIndex < this.shownNames.size()) {
                     if (CounterUI.mode == CounterUI.MODE_MANAGER) {
                        CounterUI.mode = CounterUI.MODE_EDIT;
                        CounterUI.editName = this.shownNames.get(tIndex);
                        this.rebuild();
                     } else {
                        this.pickCounter(this.shownNames.get(tIndex));
                     }
                  }
                  return;
               }
            }
         }
         return;
      }

      int tNum = 0;

      for (int i = 0; i < CFG.eventsManager.getEventsSize(); i++) {
         for (int j = 0; j < CFG.eventsManager.getEvent(i).lDecisions.size(); j++) {
            if (tNum++ == iID) {
               CFG.eventsManager
                  .createScenarioEvents
                  .getTrigger(CFG.eventsManager.createEvent_EditTriggerID)
                  .lConditions
                  .get(CFG.eventsManager.createEvent_EditConditionID)
                  .setText(CFG.eventsManager.getEvent(i).getEventTag() + "_" + j);
               CFG.eventsManager.selectCivBack();
               return;
            }
         }
      }

      CFG.eventsManager.selectCivBack();
   }
}

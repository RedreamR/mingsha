package age.of.civilizations2.jakowski.lukasz;

import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.MEHover_2E;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.ME_Hover_2Type;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.ME_Hover_2Type_Flag;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.ME_Hover_2Type_Text;
import java.util.ArrayList;
import java.util.List;
import team.rainfall.mingsha.counter.CounterDefs;
import team.rainfall.mingsha.counter.CounterStore;

public class Event_Outcome_Counter_Sub extends Event_Outcome {
    private static final long serialVersionUID = 7141030003L;
    public String counterName = "";
    private int iValue = 0;
    private int iCivID = -1;

    public Event_Outcome_Counter_Sub() {
    }

    public Event_Outcome_Counter_Sub(String counterName, int nValue) {
        this.counterName = counterName;
        this.iValue = nValue;
    }

    @Override
    public String getText() {
        return this.counterName;
    }

    @Override
    public void setText(String nText) {
        this.counterName = nText;
    }

    @Override
    public int getValue() {
        return this.iValue;
    }

    @Override
    public void setValue(int nValue) {
        this.iValue = nValue;
    }

    @Override
    public int getCivID() {
        return this.iCivID;
    }

    @Override
    public void setCivID(int nCivID) {
        this.iCivID = nCivID;
    }

    @Override
    public boolean updateCivIDAfterRemove(int nRemovedCivID) {
        if (this.iCivID == nRemovedCivID) {
            this.iCivID = -1;
            return true;
        } else {
            if (nRemovedCivID < this.iCivID) {
                this.iCivID--;
            }
            return false;
        }
    }

    @Override
    public void outcomeAction() {
        try {
            int current = CounterStore.getValue(this.iCivID, this.counterName);
            CounterStore.setCounter(this.iCivID, this.counterName, current - this.iValue);
        } catch (Exception e) {
            CFG.exceptionStack(e);
        }
    }

    @Override
    public String getConditionText() {
        return CFG.lang.get("Counter") + ": " + CounterDefs.displayOf(this.counterName) + " - " + this.iValue;
    }

    @Override
    public List<MEHover_2E> getHoverText() {
        try {
            List<MEHover_2E> tElements = new ArrayList<>();
            List<ME_Hover_2Type> tData = new ArrayList<>();
            if (this.getCivID() >= 0) {
                tData.add(new ME_Hover_2Type_Flag(this.getCivID()));
            }
            tData.add(new ME_Hover_2Type_Text(this.getConditionText()));
            tElements.add(new MEHover_2E(tData));
            tData.clear();
            return tElements;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public final void editViewID() {
        CFG.menus.setMenuID(View.eCREATE_SCENARIO_EVENTS_OUT_RENAME_CIV);
    }
}


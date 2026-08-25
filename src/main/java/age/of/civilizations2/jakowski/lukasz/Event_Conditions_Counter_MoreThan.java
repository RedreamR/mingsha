package age.of.civilizations2.jakowski.lukasz;

import team.rainfall.mingsha.counter.CounterDefs;
import team.rainfall.mingsha.counter.CounterStore;

public class Event_Conditions_Counter_MoreThan extends Event_Conditions {
    private static final long serialVersionUID = 7141020003L;
    private String counterName = "";
    private int iValue = 0;
    private int iCivID = -1;

    public Event_Conditions_Counter_MoreThan() {
    }

    public Event_Conditions_Counter_MoreThan(String counterName, int nValue) {
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
    public boolean outCondition() {
        try {
            return CounterStore.getValue(this.iCivID, this.counterName) > this.iValue;
        } catch (Exception e) {
            CFG.exceptionStack(e);
            return false;
        }
    }

    @Override
    public String getConditionText() {
        return CFG.lang.get("Counter") + ": " + CounterDefs.displayOf(this.counterName) + " > " + this.iValue;
    }

    @Override
    public final void editViewID() {
        CFG.menus.setMenuID(View.eCREATE_SCENARIO_EVENTS_COND_DECISIONTAKEN);
    }
}


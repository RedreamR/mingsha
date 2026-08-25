package age.of.civilizations2.jakowski.lukasz;

import team.rainfall.mingsha.counter.ExpressionProcessor;

public class Event_Conditions_Counter_If extends Event_Conditions {
    private static final long serialVersionUID = 7141020004L;
    private String expStr = "";
    private int iCivID = -1;

    public Event_Conditions_Counter_If() {
    }

    public Event_Conditions_Counter_If(String expStr) {
        this.expStr = expStr;
    }

    @Override
    public String getText() {
        return this.expStr;
    }

    @Override
    public void setText(String nText) {
        this.expStr = nText;
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
            return ExpressionProcessor.satisfied(this.iCivID, this.expStr);
        } catch (Exception e) {
            CFG.exceptionStack(e);
            return false;
        }
    }

    @Override
    public String getConditionText() {
        return CFG.lang.get("Counter") + ": " + this.expStr;
    }

    @Override
    public final void editViewID() {
        CFG.menus.setMenuID(View.eCREATE_SCENARIO_EVENTS_COND_DECISIONTAKEN);
    }
}

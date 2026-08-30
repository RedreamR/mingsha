package age.of.civilizations2.jakowski.lukasz;

import team.rainfall.mingsha.ns.NSDefs;
import team.rainfall.mingsha.ns.NSStore;

/**
 * True while the civilization owns a given national spirit.
 * <p>
 * The condition ignores how much time a timed spirit has left; combine it with the
 * NOT type for "does not own", which is what the editor's AND / NOT / OR row is for.
 */
public class Event_Conditions_NS_Has extends Event_Conditions {
    private static final long serialVersionUID = 7141050001L;

    private String nsID = "";
    private int iCivID = -1;

    public Event_Conditions_NS_Has() {
    }

    public Event_Conditions_NS_Has(String nNsID) {
        this.nsID = nNsID;
    }

    @Override
    public String getText() {
        return this.nsID;
    }

    @Override
    public void setText(String nText) {
        this.nsID = nText;
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
            return NSStore.has(this.iCivID, this.nsID);
        } catch (Exception e) {
            CFG.exceptionStack(e);
            return false;
        }
    }

    @Override
    public String getConditionText() {
        return CFG.lang.get("NSHas") + ": " + NSDefs.displayOf(this.nsID);
    }

    @Override
    public final void editViewID() {
        CFG.menus.setMenuID(View.eCREATE_SCENARIO_EVENTS_COND_DECISIONTAKEN);
    }
}

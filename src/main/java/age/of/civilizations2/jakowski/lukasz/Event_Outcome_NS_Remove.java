package age.of.civilizations2.jakowski.lukasz;

import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.MEHover_2E;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.ME_Hover_2Type;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.ME_Hover_2Type_Flag;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.ME_Hover_2Type_Text;

import java.util.ArrayList;
import java.util.List;

import team.rainfall.mingsha.ns.NSDefs;
import team.rainfall.mingsha.ns.NSStore;

/**
 * Takes one national spirit away from a civilization, undoing exactly the modifier
 * change the grant applied — {@link NSStore} keeps that snapshot in the ownership
 * entry, so editing the definition in between cannot leave the civ drifting.
 * <p>
 * Doing nothing when the civ does not own the spirit is intentional: the outcome is
 * safe to fire from an event that may or may not have granted it earlier.
 */
public class Event_Outcome_NS_Remove extends Event_Outcome {
    private static final long serialVersionUID = 7141040002L;

    public String nsID = "";
    private int iCivID = -1;

    public Event_Outcome_NS_Remove() {
    }

    public Event_Outcome_NS_Remove(String nNsID) {
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
    public void outcomeAction() {
        try {
            NSStore.remove(this.iCivID, this.nsID);
        } catch (Exception e) {
            CFG.exceptionStack(e);
        }
    }

    @Override
    public String getConditionText() {
        return CFG.lang.get("NSLose") + ": " + NSDefs.displayOf(this.nsID);
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

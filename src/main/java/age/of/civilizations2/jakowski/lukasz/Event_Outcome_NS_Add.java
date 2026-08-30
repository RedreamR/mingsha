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
 * Grants one national spirit to a civilization.
 * <p>
 * The duration lives in the outcome's {@code value} slot so the editor's existing
 * number field can drive it: {@code 0} means "whatever the definition says",
 * a positive number overrides it with that many turns, and a negative number
 * forces the spirit to be permanent regardless of the definition.
 */
public class Event_Outcome_NS_Add extends Event_Outcome {
    private static final long serialVersionUID = 7141040001L;

    /** Turn count that hands the decision back to the definition's own default. */
    public static final int TURNS_FROM_DEF = 0;

    public String nsID = "";
    private int iTurns = TURNS_FROM_DEF;
    private int iCivID = -1;

    public Event_Outcome_NS_Add() {
    }

    public Event_Outcome_NS_Add(String nNsID, int nTurns) {
        this.nsID = nNsID;
        this.iTurns = nTurns;
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
    public int getValue() {
        return this.iTurns;
    }

    @Override
    public void setValue(int nValue) {
        this.iTurns = nValue;
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
            if (this.iTurns == TURNS_FROM_DEF) {
                NSStore.grant(this.iCivID, this.nsID);
            } else {
                NSStore.grant(this.iCivID, this.nsID, this.iTurns);
            }
        } catch (Exception e) {
            CFG.exceptionStack(e);
        }
    }

    /** Human-readable form of the turn override, empty when the definition decides. */
    public final String turnsText() {
        if (this.iTurns == TURNS_FROM_DEF) {
            return "";
        }
        return this.iTurns < 0
            ? ", " + CFG.lang.get("NSPermanent")
            : ", " + CFG.lang.get("Turns") + ": " + this.iTurns;
    }

    @Override
    public String getConditionText() {
        return CFG.lang.get("NSGain") + ": " + NSDefs.displayOf(this.nsID) + this.turnsText();
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

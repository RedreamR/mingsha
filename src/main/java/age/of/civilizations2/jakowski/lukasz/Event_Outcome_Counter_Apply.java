package age.of.civilizations2.jakowski.lukasz;

import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.MEHover_2E;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.ME_Hover_2Type;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.ME_Hover_2Type_Flag;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.ME_Hover_2Type_Text;
import java.util.ArrayList;
import java.util.List;
import team.rainfall.mingsha.counter.ExpressionProcessor;

public class Event_Outcome_Counter_Apply extends Event_Outcome {
    private static final long serialVersionUID = 7141030006L;

    public static final int TARGET_MONEY = 0;
    public static final int TARGET_POPULATION = 1;
    public static final int TARGET_ECONOMY = 2;
    public static final int TARGET_HAPPINESS = 3;
    public static final int TARGET_ARMY = 4;
    public static final int NUM_TARGETS = 5;

    public int target = TARGET_MONEY;
    public String expStr = "0";
    private int iCivID = -1;

    public Event_Outcome_Counter_Apply() {
    }

    @Override
    public String getText() {
        return this.expStr;
    }

    @Override
    public void setText(String nText) {
        this.expStr = nText == null ? "" : nText;
    }

    @Override
    public int getValue() {
        return this.target;
    }

    @Override
    public void setValue(int nValue) {
        this.target = nValue < 0 ? 0 : nValue % NUM_TARGETS;
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

    public static String targetName(int nTarget) {
        switch (nTarget) {
            case TARGET_MONEY:
                return CFG.lang.get("Treasury");
            case TARGET_POPULATION:
                return CFG.lang.get("Population") + " %";
            case TARGET_ECONOMY:
                return CFG.lang.get("Economy") + " %";
            case TARGET_HAPPINESS:
                return CFG.lang.get("Happiness") + " %";
            case TARGET_ARMY:
                return CFG.lang.get("Army");
            default:
                return "?";
        }
    }

    @Override
    public void outcomeAction() {
        try {
            if (this.iCivID <= 0 || this.iCivID >= CFG.core.getCivsSize()) {
                return;
            }
            int tValue = ExpressionProcessor.compute(this.iCivID, this.expStr);
            switch (this.target) {
                case TARGET_MONEY:
                    CFG.core.getCiv(this.iCivID).setGold(CFG.core.getCiv(this.iCivID).getGold() + (long)tValue);
                    break;
                case TARGET_POPULATION:
                    for (int i = 0; i < CFG.core.getCiv(this.iCivID).getNumOfProvs(); i++) {
                        for (int j = 0; j < CFG.core.getProv(CFG.core.getCiv(this.iCivID).getProvID(i)).getPop().getNatsSize(); j++) {
                            CFG.core
                                .getProv(CFG.core.getCiv(this.iCivID).getProvID(i))
                                .getPop()
                                .setPopulationOfCivID(
                                    CFG.core.getProv(CFG.core.getCiv(this.iCivID).getProvID(i)).getPop().getCivID(j),
                                    CFG.core.getProv(CFG.core.getCiv(this.iCivID).getProvID(i)).getPop().getPopulationID(j)
                                        + (int)(
                                            (float)CFG.core.getProv(CFG.core.getCiv(this.iCivID).getProvID(i)).getPop().getPopulationID(j)
                                                * ((float)tValue / 100.0F)
                                        )
                                );
                        }
                    }
                    break;
                case TARGET_ECONOMY:
                    for (int i = 0; i < CFG.core.getCiv(this.iCivID).getNumOfProvs(); i++) {
                        CFG.core
                            .getProv(CFG.core.getCiv(this.iCivID).getProvID(i))
                            .setEco(
                                CFG.core.getProv(CFG.core.getCiv(this.iCivID).getProvID(i)).getEco()
                                    + (int)((float)CFG.core.getProv(CFG.core.getCiv(this.iCivID).getProvID(i)).getEco() * ((float)tValue / 100.0F))
                            );
                    }
                    break;
                case TARGET_HAPPINESS:
                    for (int i = 0; i < CFG.core.getCiv(this.iCivID).getNumOfProvs(); i++) {
                        CFG.core
                            .getProv(CFG.core.getCiv(this.iCivID).getProvID(i))
                            .setHappi(CFG.core.getProv(CFG.core.getCiv(this.iCivID).getProvID(i)).getHappi() + (float)tValue / 100.0F);
                    }
                    break;
                case TARGET_ARMY:
                    int tCapitalID = CFG.core.getCiv(this.iCivID).getCapitalProvID();
                    if (tCapitalID >= 0 && CFG.core.getProv(tCapitalID).getCivId() == this.iCivID) {
                        CFG.core.getProv(tCapitalID).updateArmy4(this.iCivID, CFG.core.getProv(tCapitalID).getArmyCivID1(this.iCivID) + tValue);
                        CFG.core.getCiv(this.iCivID).updateNumberOfUnits();
                    }
                    break;
            }
        } catch (Exception e) {
            CFG.exceptionStack(e);
        }
    }

    @Override
    public String getConditionText() {
        return CFG.lang.get("Counter") + " " + CFG.lang.get("Apply") + ": " + targetName(this.target) + " = " + this.expStr;
    }

    @Override
    public List<MEHover_2E> getHoverText() {
        try {
            List<MEHover_2E> tElements = new ArrayList<>();
            List<ME_Hover_2Type> tData = new ArrayList<>();
            if (this.getCivID() > 0) {
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

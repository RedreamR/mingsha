package age.of.civilizations2.jakowski.lukasz;

import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.MEHover_2E;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.ME_Hover_2Type;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.ME_Hover_2Type_Flag;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.ME_Hover_2Type_Text;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import team.rainfall.finality.FinalityLogger;
import team.rainfall.mingsha.config.MingshaConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static age.of.civilizations2.jakowski.lukasz.CFG.exceptionStack;

public final class Event_Outcome_Leader extends Event_Outcome {
    private static final long serialVersionUID = -7549812752549347218L;
    private int iCivID = -1;
    private LeaderOfCiv_GameData data = new LeaderOfCiv_GameData();
    @Override
    public int getCivID() {
        return this.iCivID;
    }

    @Override
    public void setCivID(int nCivID) {
        this.iCivID = nCivID;
    }

    public LeaderOfCiv_GameData getLeader() {
        return this.data;
    }

    public void setLeader(LeaderOfCiv_GameData data) {
        this.data = data;
    }

    @Override
    public boolean updateCivIDAfterRemove(int nRemovedCivID) {
        if (this.iCivID == nRemovedCivID) {
            this.iCivID = -1;
            return true;
        }
        if (nRemovedCivID < this.iCivID) this.iCivID--;
        return false;
    }

    @Override
    public void outcomeAction() {
        if (canMakeAction()) {
            CFG.core.getCiv(getCivID()).civGD.leaderData = data;
            if (getCivID() == CFG.getActiveCivInfoId()) {
                if(CFG.menus.getInGame_Civ_Info_Diplomacy().getVisibleM()){
                    CFG.menus.rebuildInGame_Civ_Info_Diplomacy();
                }
                if(MingshaConfig.isRefreshLeaderView()){
                //if (CFG.viewsManager.getActiveViewID() == ViewsManager.VIEW_DIPLOMACY_MODE) {
                    if (CFG.core.getCiv(CFG.getActiveCivInfoId()).civGD.leaderData != null && !CFG.core.getCiv(CFG.getActiveCivInfoId()).civGD.leaderData.getImage().isEmpty()) {
                        CFG.menus.getInGame_CivInfo().getMenuElement(4).setVisible(true);
                        CFG.menus.getInGame_CivInfo().getMenuElement(4).setTextE(CFG.core.getCiv(CFG.getActiveCivInfoId()).civGD.leaderData.getName());
                        CFG.menus.getInGame_CivInfo().getMenuElement(3).setHeightE(CFG.PADD * 2 + CFG.TEXT_HEIGHT_DEFAULT);
                        CFG.menus.getInGame_CivInfo().getMenuElement(4).setHeightE(CFG.PADD * 2 + CFG.TEXT_HEIGHT_DEFAULT);
                        int totalH = CFG.menus.getInGame_CivInfo().getHeightM();
                        final int elemH = (int) (CFG.TEXT_HEIGHT_DEFAULT+ CFG.TEXT_HEIGHT_DEFAULT* 0.8f * 2.0f + CFG.PADD * 2);
                        totalH -= elemH;
                        totalH = Math.min(totalH, CFG.menus.getInGame_CivInfo().getMenuElement(2).getPosY() * 2);
                        CFG.menus.getInGame_CivInfo().getMenuElement(1).setPosY(totalH / 2);
                        CFG.menus.getInGame_CivInfo().getMenuElement(4).setPosY(CFG.menus.getInGame_CivInfo().getMenuElement(1).getPosY() + CFG.TEXT_HEIGHT_DEFAULT+ CFG.PADD);
                        CFG.menus.getInGame_CivInfo().getMenuElement(3).setPosY((int) (CFG.menus.getInGame_CivInfo().getMenuElement(4).getPosY() + CFG.TEXT_HEIGHT_DEFAULT* 0.8f + CFG.PADD));
                        try {
                            try {
                                CFG.activeCivLeader = Collections.singletonList(new Image(new Texture(Gdx.files.internal("game/leadersIMG/" + CFG.core.getCiv(CFG.getActiveCivInfoId()).civGD.leaderData.getImage())), Texture.TextureFilter.Linear));
                            } catch (GdxRuntimeException e) {
                                CFG.activeCivLeader = Collections.singletonList(new Image(new Texture(Gdx.files.local("game/leadersIMG/" + CFG.core.getCiv(CFG.getActiveCivInfoId()).civGD.leaderData.getImage())), Texture.TextureFilter.Linear));
                            }
                            CFG.menus.getInGameCivStats().getMenuElement(0).setVisibleE(false);
                            CFG.menus.getInGameCivStats().getMenuElement(2).setVisible(false);
                            CFG.menus.getInGameCivStats().getMenuElement(4).setVisible(false);
                        } catch (OutOfMemoryError | RuntimeException e) {
                            CFG.activeCivLeader = null;
                            CFG.menus.getInGameCivStats().getMenuElement(0).setVisible(true);
                            CFG.menus.getInGameCivStats().getMenuElement(2).setVisible(true);
                            CFG.menus.getInGameCivStats().getMenuElement(4).setVisible(true);
                            exceptionStack(e);
                        }
                    } else {
                        CFG.menus.getInGame_CivInfo().getMenuElement(4).setVisible(false);
                        CFG.menus.getInGame_CivInfo().getMenuElement(3).setHeightE(CFG.PADD * 4 + CFG.TEXT_HEIGHT_DEFAULT);
                        CFG.menus.getInGame_CivInfo().getMenuElement(1).setPosY(CFG.menus.getInGame_CivInfo().getMenuElement(2).getPosY() + CFG.menus.getInGame_CivInfo().getMenuElement(2).getHeightE() / 2 - (int) ((CFG.TEXT_HEIGHT_DEFAULT+ CFG.TEXT_HEIGHT_DEFAULT* 0.8f + CFG.PADD * 2) / 2.0f));
                        CFG.menus.getInGame_CivInfo().getMenuElement(3).setPosY(CFG.menus.getInGame_CivInfo().getMenuElement(1).getPosY() + CFG.TEXT_HEIGHT_DEFAULT+ CFG.PADD);
                    }
                }
            }
        }
    }

    private boolean canMakeAction() {
        try {
            return (getCivID() >= 0 && getCivID() < CFG.core.getCivsSize() && CFG.core.getCiv(getCivID()).getNumOfProvs() > 0);
        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            return false;
        }
    }

    @Override
    public String getConditionText() {
        try {
            return CFG.lang.get("UpdateLeader") + ": " + CFG.core.getCiv(getCivID()).getCivName() + ", " + getLeader().getName();
        } catch (IndexOutOfBoundsException ex) {
            return CFG.lang.get("UpdateLeader");
        }
    }

    @Override
    public List<MEHover_2E> getHoverText() {
        try {
            List<MEHover_2E> tElements = new ArrayList<>();
            List<ME_Hover_2Type> tData = new ArrayList<>();
            FinalityLogger.info("canMakeAction "+canMakeAction());
            if (canMakeAction()) {
                tData.add(new ME_Hover_2Type_Flag(getCivID()));
                tData.add(new ME_Hover_2Type_Text(CFG.lang.get(data.getName()), CFG.COLOR_BUTTON_GAME_TEXT_ACTIVE));
                if (CFG.core.getCiv(iCivID).civGD.leaderData != null) {
                    if(data.getName().equals(CFG.core.getCiv(iCivID).civGD.leaderData.getName())) {
                        tData.add(new ME_Hover_2Type_Text(CFG.lang.get("WillUpdate")));
                    }
                } else {
                    tData.add(new ME_Hover_2Type_Text(" " + CFG.lang.get("BecomeNewLeader")));
                }
                tElements.add(new MEHover_2E(tData));
                tData.clear();
            }
            return tElements;
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            FinalityLogger.error("EvtOut Leader(X) ",e);
        }
        return new ArrayList<>();
    }

    @Override
    public final void editViewID() {
        CFG.toastM.addM("Could not edit X system outcomes! ");
    }
}
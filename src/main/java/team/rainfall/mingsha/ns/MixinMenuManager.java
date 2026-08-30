package team.rainfall.mingsha.ns;

import age.of.civilizations2.jakowski.lukasz.CFG;
import age.of.civilizations2.jakowski.lukasz.Menu;
import age.of.civilizations2.jakowski.lukasz.View;
import age.of.civilizations2.jakowski.lukasz.Menus.Civilization.Menu_InGame_Civ_Diplomacy;
import team.rainfall.finality.luminosity2.annotations.Mixin;
import team.rainfall.finality.luminosity2.annotations.Shadow;

import java.util.ArrayList;
import java.util.List;

/**
 * Gives the national spirit feature the two things it cannot get from outside
 * {@code MenuManager}:
 * <ul>
 *   <li>a genuinely new standalone view, created on demand without burning a
 *       {@code View} enum slot — {@code setMenuID}/{@code setMenuIDWithoutAnim}
 *       first consult {@link NSUI#consumeRequest()};</li>
 *   <li>the NS bar wedged between the civilization stats box and the diplomacy
 *       box in the left in-game column, with everything below it shifted down.</li>
 * </ul>
 * Luminosity replaces whole methods by {@code name+desc}, so the three vanilla
 * methods below are reproduced verbatim apart from the NS hooks. New methods are
 * added to the class, which is why they can touch the private {@code menus} and
 * {@code orderOfMenu} lists directly.
 */
@Mixin(mixinClass = "age.of.civilizations2.jakowski.lukasz.MenuManager")
public class MixinMenuManager {

    @Shadow
    private List<List<Menu>> menus;
    @Shadow
    private List<List<Integer>> orderOfMenu;
    @Shadow
    private Menu keyboard;
    @Shadow
    private int viewID;
    @Shadow
    private int fromViewID;
    @Shadow
    private int toViewID;
    @Shadow
    public int IN_GAME_MENU;
    @Shadow
    public int INGAME_CIV_INFO;
    @Shadow
    public int INGAME_CIV_INFO_STATS;
    @Shadow
    public int INGAME_CIV_DIPLOMACY;
    @Shadow
    public int INGAME_CIV_DIPLO_ORACTIONS;
    @Shadow
    public int INGAME_CIV_ACTIONS;
    @Shadow
    public int INGAME_CIV_OPINIONS;
    @Shadow
    public int INGAME_CIV_DECISIONS;

    public final void setMenuID(View eMenu) {
        this.resetHoverActive();
        this.keyboard.setVisibleM(false);
        this.fromViewID = this.viewID;
        int tNSViewID = this.msResolveNSView();
        this.viewID = tNSViewID >= 0 ? tNSViewID : this.getViewID(eMenu);
        this.toViewID = this.viewID;
        CFG.setRenderO(true);
        this.updateViewID();
    }

    public final void setMenuIDWithoutAnim(View eMenu) {
        this.resetHoverActive();
        this.keyboard.setVisibleM(false);
        int tNSViewID = this.msResolveNSView();
        this.viewID = tNSViewID >= 0 ? tNSViewID : this.getViewID(eMenu);
        this.resetChangeViewMode();
        CFG.setRenderO(true);
        this.updateViewID();
    }

    /**
     * Serves a pending {@link NSUI} view request, creating the view on first use.
     * Replicates {@code addMenu}'s body because that method is private and adding
     * a view is the one thing {@code addNewMenuToViewID} cannot do.
     *
     * @return the view id to switch to, or -1 when nothing was requested
     */
    public final int msResolveNSView() {
        try {
            int tRequest = NSUI.consumeRequest();
            if (tRequest == NSUI.REQ_NONE) {
                return -1;
            }
            Menu tMenu = NSUI.createMenu(tRequest);
            if (tMenu == null) {
                return -1;
            }
            int tSlotID = NSUI.viewIDOf(tRequest);
            if (tSlotID < 0 || tSlotID >= this.menus.size()) {
                List<Menu> tNewView = new ArrayList<Menu>();
                tNewView.add(tMenu);
                this.menus.add(tNewView);
                List<Integer> tNewOrder = new ArrayList<Integer>();
                tNewOrder.add(0);
                this.orderOfMenu.add(tNewOrder);
                tSlotID = this.menus.size() - 1;
                NSUI.setViewIDOf(tRequest, tSlotID);
            } else {
                this.menus.get(tSlotID).set(0, tMenu);
            }
            return tSlotID;
        } catch (Exception e) {
            CFG.exceptionStack(e);
            return -1;
        }
    }

    public final void rebuildInGame_Civ_Info_Diplomacy() {
        boolean tVisible = this.menus.get(this.IN_GAME_MENU).get(this.INGAME_CIV_DIPLOMACY).getVisibleM();
        this.menus.get(this.IN_GAME_MENU).set(this.INGAME_CIV_DIPLOMACY, new Menu_InGame_Civ_Diplomacy());
        int tStatsBottom = 2
            + this.menus.get(this.IN_GAME_MENU).get(this.INGAME_CIV_INFO_STATS).getPosY()
            + this.menus.get(this.IN_GAME_MENU).get(this.INGAME_CIV_INFO_STATS).getHeightM();
        int tNSOffset = 0;
        try {
            tNSOffset = this.msRebuildNSBar(tStatsBottom, tVisible);
        } catch (Exception e) {
            CFG.exceptionStack(e);
        }
        this.menus
            .get(this.IN_GAME_MENU)
            .get(this.INGAME_CIV_DIPLOMACY)
            .setPosY(
                tStatsBottom
                    + tNSOffset
                    + this.menus.get(this.IN_GAME_MENU).get(this.INGAME_CIV_DIPLOMACY).getTitleM().getHeightT()
            );
        this.menus.get(this.IN_GAME_MENU).get(this.INGAME_CIV_DIPLOMACY).setVisibleM(tVisible);
        this.rebuildInGame_Civ_Actions();
    }

    /**
     * Rebuilds the NS bar and places it directly below the stats box.
     *
     * @param nStatsBottom bottom edge of the civilization stats box
     * @param nVisible     whether the civilization info column is on screen
     * @return how far down everything below the bar has to move; 0 when the
     *         civilization owns no spirits and the whole bar stays hidden
     */
    public final int msRebuildNSBar(int nStatsBottom, boolean nVisible) {
        if (this.IN_GAME_MENU < 0 || this.IN_GAME_MENU >= this.menus.size()) {
            return 0;
        }
        List<Menu> tInGame = this.menus.get(this.IN_GAME_MENU);
        if (NSUI.barSlotID >= 0
            && (NSUI.barSlotID >= tInGame.size() || !(tInGame.get(NSUI.barSlotID) instanceof Menu_InGame_Civ_NS))) {
            NSUI.barSlotID = -1;
        }

        Menu_InGame_Civ_NS tBar = new Menu_InGame_Civ_NS();
        if (NSUI.barSlotID < 0) {
            NSUI.barSlotID = this.addNewMenuToViewID(this.IN_GAME_MENU, tBar);
        } else {
            tInGame.set(NSUI.barSlotID, tBar);
        }

        if (tBar.getMenuElemsSize() == 0) {
            tBar.msForceVisible(false);
            return 0;
        }
        tBar.setPosY(nStatsBottom + tBar.getTitleM().getHeightT());
        tBar.setVisibleM(nVisible);
        return tBar.getTitleM().getHeightT() + tBar.getHeightM() + 2;
    }

    public final void setOrderOfMenu_InGame_CivInfo() {
        if (this.menus.get(this.IN_GAME_MENU).get(this.INGAME_CIV_INFO).getVisibleM()) {
            this.setOrderOfMenuID(this.INGAME_CIV_INFO);
            this.setOrderOfMenuID(this.INGAME_CIV_INFO_STATS);
            if (NSUI.barSlotID >= 0) {
                this.setOrderOfMenuID(NSUI.barSlotID);
            }
            this.setOrderOfMenuID(this.INGAME_CIV_DIPLOMACY);
            this.setOrderOfMenuID(this.INGAME_CIV_DIPLO_ORACTIONS);
            this.setOrderOfMenuID(this.INGAME_CIV_ACTIONS);
            this.setOrderOfMenuID(this.INGAME_CIV_OPINIONS);
            this.setOrderOfMenuID(this.INGAME_CIV_DECISIONS);
        }
    }

    @Shadow
    public final void resetHoverActive() {
    }

    @Shadow
    private final void updateViewID() {
    }

    @Shadow
    private final void resetChangeViewMode() {
    }

    @Shadow
    public final void rebuildInGame_Civ_Actions() {
    }

    @Shadow
    public final int getViewID(View eMenu) {
        return 0;
    }

    @Shadow
    public final int addNewMenuToViewID(int toView, Menu menu) {
        return 0;
    }

    @Shadow
    public final void setOrderOfMenuID(int menuID) {
    }
}

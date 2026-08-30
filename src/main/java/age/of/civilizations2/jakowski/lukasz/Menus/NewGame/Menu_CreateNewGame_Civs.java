package age.of.civilizations2.jakowski.lukasz.Menus.NewGame;

import age.of.civilizations2.jakowski.lukasz.CFG;
import age.of.civilizations2.jakowski.lukasz.IMGManager;
import age.of.civilizations2.jakowski.lukasz.Images;
import age.of.civilizations2.jakowski.lukasz.Menu;
import age.of.civilizations2.jakowski.lukasz.Button.ButtonFlag_CivName;
import age.of.civilizations2.jakowski.lukasz.Button.MenuElemUI;
import java.util.ArrayList;
import java.util.List;

public class Menu_CreateNewGame_Civs extends Menu {
   public Menu_CreateNewGame_Civs() {
      List<MenuElemUI> menuElements = new ArrayList<>();
      int tX = 0;
      int tY = CFG.PADD;
      int menuPosX = CFG.BUTTON_W + (CFG.BUTTON_W + CFG.BUTTON_W / 2) * 2 + CFG.PADD * 5;
      int menuW = CFG.GAMEWIDTH - menuPosX - CFG.BUTTON_W * 2 - CFG.PADD * 3;
      int menuH = Math.max(CFG.BUTTON_H, IMGManager.getIMG(Images.flagDiplomacyOver).getHeight() + CFG.TEXT_HEIGHT_DEFAULT + CFG.PADD * 2 + tY * 2);
      // 原版这里是 if (CFG.getIsDesktop()) { ...按国力排出推荐文明... } else { 只放一个 civID = 0 的占位旗 }
      // 手机端因此只显示一个空白占位旗。这里去掉平台判断，安卓也走完整逻辑。
      List<Integer> tCivs = new ArrayList<>();

      for (int i = 1; i < CFG.core.getCivsSize(); i++) {
         if (CFG.core.getCiv(i).getNumOfProvs() > 0 && CFG.core.getCiv(i).getCapitalProvID() >= 0) {
            tCivs.add(i);
         }
      }

      while (!tCivs.isEmpty()) {
         int bestID = 0;
         int ix = 1;

         for (int iSize = tCivs.size(); ix < iSize; ix++) {
            if (CFG.core.getCiv(tCivs.get(bestID)).getRankScore() < CFG.core.getCiv(tCivs.get(ix)).getRankScore()) {
               bestID = ix;
            }
         }

         menuElements.add(new ButtonFlag_CivName(tCivs.get(bestID), tX, tY, true));
         tX += menuElements.get(menuElements.size() - 1).getWidthE() + CFG.PADD / 2;
         tCivs.remove(bestID);
         if (menuElements.size() >= 50) {
            break;
         }
      }

      this.initMenu(null, menuPosX, CFG.GAMEHEIGHT - menuH, menuW, menuH, menuElements, false, false);
      this.updateLang();
   }

   @Override
   public void actionEL(int nMenuElementID) {
      Menu_CreateNewGame.CHALLENGE_MODE_NG = -1;
      super.actionEL(nMenuElementID);
   }
}

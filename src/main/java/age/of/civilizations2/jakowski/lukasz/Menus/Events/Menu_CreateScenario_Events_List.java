package age.of.civilizations2.jakowski.lukasz.Menus.Events;

import age.of.civilizations2.jakowski.lukasz.CFG;
import age.of.civilizations2.jakowski.lukasz.Event_GameData;
import age.of.civilizations2.jakowski.lukasz.GameCalendar;
import age.of.civilizations2.jakowski.lukasz.IMGManager;
import age.of.civilizations2.jakowski.lukasz.Images;
import age.of.civilizations2.jakowski.lukasz.Menu;
import age.of.civilizations2.jakowski.lukasz.Button.MenuElemUI;
import age.of.civilizations2.jakowski.lukasz.Button.Game.Button_Game_NewGameBoxStyle_LEFT;
import age.of.civilizations2.jakowski.lukasz.Button.Game.Button_Game_NewGameBoxStyle_RIGHT_Remove;
import age.of.civilizations2.jakowski.lukasz.Button.GameN.Options.Button_Opt_NS_MapModes_R2;
import age.of.civilizations2.jakowski.lukasz.Button.NewGame.Button_In_Game_Box_CivID_LEFT;
import age.of.civilizations2.jakowski.lukasz.Button.NewGame.Button_NewGameStyle;
import age.of.civilizations2.jakowski.lukasz.Core.Core;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.MEHover_2E;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.ME_Hover_2Type;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.ME_Hover_2Type_Flag;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.ME_Hover_2Type_Image;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.ME_Hover_2Type_Text;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.ME_Hover_v2;
import age.of.civilizations2.jakowski.lukasz.Title.TitleM;
import age.of.civilizations2.jakowski.lukasz.Z_Other.DialogType;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import team.rainfall.mingsha.utils.EventCopyUtil;
import team.rainfall.mingsha.utils.HiddenEvents;

/**
 * 事件列表。marker 事件（{@link HiddenEvents}）不进列表：它们只是某个功能的存档容器，
 * 打开/复制/删除都没有意义，删掉还会连带丢数据。
 * <p>
 * 元素与事件下标不再一一对应，但这里本来也不是靠算术对应的 —— 行元素自己用
 * {@code setCurr(i)} 记住了事件下标，{@code actionEL} 里的 {@code % 3} 只用来分辨
 * 一行里点的是行本身、复制还是删除，所以跳过若干事件不影响点击落点。行首序号改用
 * 显示序号，否则被隐藏的事件会在编号里留下缺口，反而把它暴露出来。
 */
public class Menu_CreateScenario_Events_List extends Menu {
   public static String sSearch = "";
   public static String searchText = "";

   public Menu_CreateScenario_Events_List() {
      int tempW = CFG.CIV_INFO_MENU_WIDTH + CFG.CIV_INFO_MENU_WIDTH * 5 / 4;
      int tempElemH = CFG.BUTTON_H * 3 / 4;
      if (sSearch.length() == 0) {
         sSearch = CFG.lang.get("Search") + ": ";
      }

      List<MenuElemUI> menuElements = new ArrayList<>();
      int tPosY = CFG.PADD;
      menuElements.add(
         new Button_Opt_NS_MapModes_R2(-2, "", CFG.PADD * 2, CFG.PADD, tPosY, tempW - CFG.PADD * 2, (int)((float)CFG.BUTTON_H * 0.7F), true, true, 0) {
            @Override
            public String getTextToDrawElem() {
               return Menu_CreateScenario_Events_List.sSearch + Menu_CreateScenario_Events_List.searchText;
            }
         }
      );
      tPosY += menuElements.get(menuElements.size() - 1).getHeightE() + CFG.PADD;
      menuElements.add(
         new Button_NewGameStyle(CFG.lang.get("AddNewEvent"), -1, CFG.PADD, tPosY, tempW - CFG.PADD * 2, (int)((float)CFG.BUTTON_H * 0.75F), true)
      );
      tPosY += menuElements.get(menuElements.size() - 1).getHeightE() + CFG.PADD;
      if (searchText != null && searchText.length() != 0) {
         String searchTL = searchText.toLowerCase();
         int tShown = 0;

         for (int i = 0; i < CFG.eventsManager.getEventsSize(); i++) {
            if (!HiddenEvents.isHidden(CFG.eventsManager.getEvent(i))
               && CFG.eventsManager.getEvent(i).getEventName().toLowerCase().contains(searchTL)) {
               CFG.eventsManager.iCreateEvent_Day = CFG.eventsManager.getEvent(i).getEventDate_Since().iEventDay;
               CFG.eventsManager.iCreateEvent_Month = CFG.eventsManager.getEvent(i).getEventDate_Since().iEventMonth;
               CFG.eventsManager.iCreateEvent_Year = CFG.eventsManager.getEvent(i).getEventDate_Since().iEventYear;
               menuElements.add(
                  new Button_In_Game_Box_CivID_LEFT(
                     CFG.eventsManager.getEvent(i).getCivID(),
                     ++tShown
                        + ". "
                        + CFG.eventsManager.getEvent(i).getEventName()
                        + (
                           CFG.eventsManager.getEvent(i).getCivID() > 0 && CFG.eventsManager.getEvent(i).getCivID() < CFG.core.getCivsSize()
                              ? ", " + CFG.core.getCiv(CFG.eventsManager.getEvent(i).getCivID()).getCivName()
                              : ""
                        )
                        + ", "
                        + (CFG.eventsManager.iCreateEvent_Year == 9999999 ? CFG.lang.get("NoDate") : GameCalendar.getCurrDate_CreateEvent()),
                     CFG.PADD * 2,
                     CFG.PADD,
                     tPosY,
                     tempW - CFG.PADD * 2 - (int)((float)CFG.BUTTON_H * 0.75F) - CFG.BUTTON_W,
                     true
                  ) {
                     int iCurrent = 0;

                     @Override
                     public int getCurr() {
                        return this.iCurrent;
                     }

                     @Override
                     public void setCurr(int nCurrent) {
                        this.iCurrent = nCurrent;
                     }

                     @Override
                     public void buildElemHover() {
                        List<MEHover_2E> nElements = new ArrayList<>();
                        List<ME_Hover_2Type> nData = new ArrayList<>();
                        nData.add(new ME_Hover_2Type_Text(CFG.lang.get("Title") + ": "));
                        nData.add(new ME_Hover_2Type_Text(CFG.eventsManager.getEvent(this.getCurr()).getEventName(), CFG.COLOR_HOVER_TITLE));
                        nElements.add(new MEHover_2E(nData));
                        nData.clear();

                        try {
                           nData.add(new ME_Hover_2Type_Text(CFG.lang.get("Recipient") + ": "));
                           if (this.getCurr() == 0) {
                              nData.add(new ME_Hover_2Type_Text(CFG.lang.get("GlobalEvent"), CFG.COLOR_HOVER_TITLE));
                              nData.add(new ME_Hover_2Type_Image(Images.editorMap, CFG.PADD, 0));
                           } else {
                              nData.add(
                                 new ME_Hover_2Type_Text(
                                    CFG.core.getCiv(CFG.eventsManager.getEvent(this.getCurr()).getCivID()).getCivName(), CFG.COLOR_HOVER_TITLE
                                 )
                              );
                              nData.add(new ME_Hover_2Type_Flag(CFG.eventsManager.getEvent(this.getCurr()).getCivID(), CFG.PADD, 0));
                           }

                           nElements.add(new MEHover_2E(nData));
                           nData.clear();
                        } catch (IndexOutOfBoundsException var4) {
                        }

                        CFG.eventsManager.iCreateEvent_Day = CFG.eventsManager.getEvent(this.getCurr()).getEventDate_Since().iEventDay;
                        CFG.eventsManager.iCreateEvent_Month = CFG.eventsManager.getEvent(this.getCurr()).getEventDate_Since().iEventMonth;
                        CFG.eventsManager.iCreateEvent_Year = CFG.eventsManager.getEvent(this.getCurr()).getEventDate_Since().iEventYear;
                        nData.add(new ME_Hover_2Type_Text(CFG.lang.get("Since") + ": "));
                        nData.add(
                           new ME_Hover_2Type_Text(
                              CFG.eventsManager.iCreateEvent_Year == 9999999 ? CFG.lang.get("NoDate") : GameCalendar.getCurrDate_CreateEvent(),
                              CFG.COLOR_HOVER_TITLE
                           )
                        );
                        nElements.add(new MEHover_2E(nData));
                        nData.clear();
                        CFG.eventsManager.iCreateEvent_Day = CFG.eventsManager.getEvent(this.getCurr()).getEventDate_Until().iEventDay;
                        CFG.eventsManager.iCreateEvent_Month = CFG.eventsManager.getEvent(this.getCurr()).getEventDate_Until().iEventMonth;
                        CFG.eventsManager.iCreateEvent_Year = CFG.eventsManager.getEvent(this.getCurr()).getEventDate_Until().iEventYear;
                        nData.add(new ME_Hover_2Type_Text(CFG.lang.get("Until") + ": "));
                        nData.add(
                           new ME_Hover_2Type_Text(
                              CFG.eventsManager.iCreateEvent_Year == 9999999 ? CFG.lang.get("NoDate") : GameCalendar.getCurrDate_CreateEvent(),
                              CFG.COLOR_HOVER_TITLE
                           )
                        );
                        nElements.add(new MEHover_2E(nData));
                        nData.clear();
                        this.menuElemHover = new ME_Hover_v2(nElements);
                     }
                  }
               );
               menuElements.get(menuElements.size() - 1).setCurr(i);
               menuElements.add(
                  new Button_Game_NewGameBoxStyle_LEFT(
                     CFG.lang.get("Copy"),
                     -1,
                     tempW - CFG.PADD - (int)((float)CFG.BUTTON_H * 0.75F) - CFG.BUTTON_W,
                     tPosY,
                     CFG.BUTTON_W,
                     (int)((float)CFG.BUTTON_H * 0.75F),
                     true
                  ) {
                  }
               );
               menuElements.add(
                  new Button_Game_NewGameBoxStyle_RIGHT_Remove(
                     tempW - CFG.PADD - (int)((float)CFG.BUTTON_H * 0.75F), tPosY, (int)((float)CFG.BUTTON_H * 0.75F), (int)((float)CFG.BUTTON_H * 0.75F), true
                  ) {
                  }
               );
               tPosY += menuElements.get(menuElements.size() - 1).getHeightE() + CFG.PADD;
            }
         }
      } else {
         int tShown = 0;

         for (int ix = 0; ix < CFG.eventsManager.getEventsSize(); ix++) {
            if (HiddenEvents.isHidden(CFG.eventsManager.getEvent(ix))) {
               continue;
            }

            CFG.eventsManager.iCreateEvent_Day = CFG.eventsManager.getEvent(ix).getEventDate_Since().iEventDay;
            CFG.eventsManager.iCreateEvent_Month = CFG.eventsManager.getEvent(ix).getEventDate_Since().iEventMonth;
            CFG.eventsManager.iCreateEvent_Year = CFG.eventsManager.getEvent(ix).getEventDate_Since().iEventYear;
            menuElements.add(
               new Button_In_Game_Box_CivID_LEFT(
                  CFG.eventsManager.getEvent(ix).getCivID(),
                  ++tShown
                     + ". "
                     + CFG.eventsManager.getEvent(ix).getEventName()
                     + (
                        CFG.eventsManager.getEvent(ix).getCivID() > 0 && CFG.eventsManager.getEvent(ix).getCivID() < CFG.core.getCivsSize()
                           ? ", " + CFG.core.getCiv(CFG.eventsManager.getEvent(ix).getCivID()).getCivName()
                           : ""
                     )
                     + ", "
                     + (CFG.eventsManager.iCreateEvent_Year == 9999999 ? CFG.lang.get("NoDate") : GameCalendar.getCurrDate_CreateEvent()),
                  CFG.PADD * 2,
                  CFG.PADD,
                  tPosY,
                  tempW - CFG.PADD * 2 - (int)((float)CFG.BUTTON_H * 0.75F) - CFG.BUTTON_W,
                  true
               ) {
                  int iCurrent = 0;

                  @Override
                  public int getCurr() {
                     return this.iCurrent;
                  }

                  @Override
                  public void setCurr(int nCurrent) {
                     this.iCurrent = nCurrent;
                  }

                  @Override
                  public void buildElemHover() {
                     List<MEHover_2E> nElements = new ArrayList<>();
                     List<ME_Hover_2Type> nData = new ArrayList<>();
                     nData.add(new ME_Hover_2Type_Text(CFG.lang.get("Title") + ": "));
                     nData.add(new ME_Hover_2Type_Text(CFG.eventsManager.getEvent(this.getCurr()).getEventName(), CFG.COLOR_HOVER_TITLE));
                     nElements.add(new MEHover_2E(nData));
                     nData.clear();

                     try {
                        nData.add(new ME_Hover_2Type_Text(CFG.lang.get("Recipient") + ": "));
                        if (this.getCurr() == 0) {
                           nData.add(new ME_Hover_2Type_Text(CFG.lang.get("GlobalEvent"), CFG.COLOR_HOVER_TITLE));
                           nData.add(new ME_Hover_2Type_Image(Images.editorMap, CFG.PADD, 0));
                        } else {
                           nData.add(
                              new ME_Hover_2Type_Text(
                                 CFG.core.getCiv(CFG.eventsManager.getEvent(this.getCurr()).getCivID()).getCivName(), CFG.COLOR_HOVER_TITLE
                              )
                           );
                           nData.add(new ME_Hover_2Type_Flag(CFG.eventsManager.getEvent(this.getCurr()).getCivID(), CFG.PADD, 0));
                        }

                        nElements.add(new MEHover_2E(nData));
                        nData.clear();
                     } catch (IndexOutOfBoundsException var4) {
                     }

                     CFG.eventsManager.iCreateEvent_Day = CFG.eventsManager.getEvent(this.getCurr()).getEventDate_Since().iEventDay;
                     CFG.eventsManager.iCreateEvent_Month = CFG.eventsManager.getEvent(this.getCurr()).getEventDate_Since().iEventMonth;
                     CFG.eventsManager.iCreateEvent_Year = CFG.eventsManager.getEvent(this.getCurr()).getEventDate_Since().iEventYear;
                     nData.add(new ME_Hover_2Type_Text(CFG.lang.get("Since") + ": "));
                     nData.add(
                        new ME_Hover_2Type_Text(
                           CFG.eventsManager.iCreateEvent_Year == 9999999 ? CFG.lang.get("NoDate") : GameCalendar.getCurrDate_CreateEvent(),
                           CFG.COLOR_HOVER_TITLE
                        )
                     );
                     nElements.add(new MEHover_2E(nData));
                     nData.clear();
                     CFG.eventsManager.iCreateEvent_Day = CFG.eventsManager.getEvent(this.getCurr()).getEventDate_Until().iEventDay;
                     CFG.eventsManager.iCreateEvent_Month = CFG.eventsManager.getEvent(this.getCurr()).getEventDate_Until().iEventMonth;
                     CFG.eventsManager.iCreateEvent_Year = CFG.eventsManager.getEvent(this.getCurr()).getEventDate_Until().iEventYear;
                     nData.add(new ME_Hover_2Type_Text(CFG.lang.get("Until") + ": "));
                     nData.add(
                        new ME_Hover_2Type_Text(
                           CFG.eventsManager.iCreateEvent_Year == 9999999 ? CFG.lang.get("NoDate") : GameCalendar.getCurrDate_CreateEvent(),
                           CFG.COLOR_HOVER_TITLE
                        )
                     );
                     nElements.add(new MEHover_2E(nData));
                     nData.clear();
                     this.menuElemHover = new ME_Hover_v2(nElements);
                  }
               }
            );
            menuElements.get(menuElements.size() - 1).setCurr(ix);
            menuElements.add(
               new Button_Game_NewGameBoxStyle_LEFT(
                  CFG.lang.get("Copy"),
                  -1,
                  tempW - CFG.PADD - (int)((float)CFG.BUTTON_H * 0.75F) - CFG.BUTTON_W,
                  tPosY,
                  CFG.BUTTON_W,
                  (int)((float)CFG.BUTTON_H * 0.75F),
                  true
               ) {
               }
            );
            menuElements.add(
               new Button_Game_NewGameBoxStyle_RIGHT_Remove(
                  tempW - CFG.PADD - (int)((float)CFG.BUTTON_H * 0.75F), tPosY, (int)((float)CFG.BUTTON_H * 0.75F), (int)((float)CFG.BUTTON_H * 0.75F), true
               ) {
               }
            );
            tPosY += menuElements.get(menuElements.size() - 1).getHeightE() + CFG.PADD;
         }
      }

      this.initMenu(
         new TitleM(null, CFG.BUTTON_H * 3 / 5, false, false) {
            @Override
            public void drawT(SpriteBatch oSB, int iTranslateX, int nPosX, int nPosY, int nWidth, boolean sliderMenuIsActive) {
               IMGManager.getIMG(Images.dialog_title)
                  .draw2O(
                     oSB,
                     nPosX - 2 - Core.PADDING + iTranslateX,
                     nPosY - Core.PADDING - this.getHeightT() - IMGManager.getIMG(Images.dialog_title).getHeight(),
                     nWidth + 4 + Core.PADDING,
                     this.getHeightT() + Core.PADDING
                  );
               oSB.setColor(new Color(0.003921569F, 0.32941177F, 0.50980395F, 0.165F));
               IMGManager.getIMG(Images.line32Off1)
                  .drawO(
                     oSB,
                     nPosX + iTranslateX,
                     nPosY - this.getHeightT() + 2 - IMGManager.getIMG(Images.line32Off1).getHeight(),
                     nWidth,
                     this.getHeightT() - 2,
                     false,
                     true
                  );
               oSB.setColor(new Color(0.003921569F, 0.32941177F, 0.50980395F, 0.375F));
               IMGManager.getIMG(Images.gradient)
                  .drawO(
                     oSB,
                     nPosX + iTranslateX,
                     nPosY - this.getHeightT() * 2 / 3 - IMGManager.getIMG(Images.gradient).getHeight(),
                     nWidth,
                     this.getHeightT() * 2 / 3,
                     false,
                     true
                  );
               oSB.setColor(new Color(0.0F, 0.0F, 0.0F, 0.65F));
               IMGManager.getIMG(Images.gradient)
                  .drawO(oSB, nPosX + iTranslateX, nPosY - CFG.PADD - IMGManager.getIMG(Images.gradient).getHeight(), nWidth, CFG.PADD, false, true);
               oSB.setColor(CFG.COLOR_NEW_GAME_EDGE_LINE);
               IMGManager.getIMG(Images.pix255).drawO(oSB, nPosX + iTranslateX, nPosY - 1 - IMGManager.getIMG(Images.pix255).getHeight(), nWidth, 1);
               oSB.setColor(new Color(0.0F, 0.0F, 0.0F, 0.55F));
               IMGManager.getIMG(Images.line32Off1).drawO(oSB, nPosX + iTranslateX, nPosY - 2 - IMGManager.getIMG(Images.line32Off1).getHeight(), nWidth, 1);
               oSB.setColor(new Color(0.0F, 0.0F, 0.0F, 0.8F));
               IMGManager.getIMG(Images.line32Off1).drawO(oSB, nPosX + iTranslateX, nPosY - 1 - IMGManager.getIMG(Images.line32Off1).getHeight(), nWidth, 1);
               oSB.setColor(new Color(CFG.COLOR_FLAG_FRAME.r, CFG.COLOR_FLAG_FRAME.g, CFG.COLOR_FLAG_FRAME.b, 0.45F));
               IMGManager.getIMG(Images.sliderGradient)
                  .drawO(oSB, nPosX + iTranslateX, nPosY - 1 - IMGManager.getIMG(Images.sliderGradient).getHeight(), nWidth / 2, 1);
               IMGManager.getIMG(Images.sliderGradient)
                  .drawO(
                     oSB,
                     nPosX + nWidth - nWidth / 2 + iTranslateX,
                     nPosY - 1 - IMGManager.getIMG(Images.sliderGradient).getHeight(),
                     nWidth / 2,
                     1,
                     true,
                     false
                  );
               oSB.setColor(Color.WHITE);
               IMGManager.getIMG(Images.time)
                  .drawO(
                     oSB,
                     nPosX + (nWidth - this.getTextWidth()) / 2 - CFG.PADD - IMGManager.getIMG(Images.time).getWidth() + iTranslateX,
                     2 + nPosY - this.getHeightT() + this.getHeightT() / 2 - IMGManager.getIMG(Images.time).getHeight() / 2
                  );
               CFG.drawTextDefault(
                  oSB,
                  this.getText(),
                  nPosX + (nWidth - this.getTextWidth()) / 2 + iTranslateX,
                  2 + nPosY - this.getHeightT() + (this.getHeightT() - this.getTextHeight()) / 2,
                  Color.WHITE
               );
            }
         },
         CFG.GAMEWIDTH - tempW,
         CFG.BUTTON_H + CFG.PADD * 3 + CFG.BUTTON_H * 3 / 5,
         tempW,
         Math.min(tPosY, CFG.GAMEHEIGHT - (CFG.BUTTON_H + CFG.PADD * 3 + CFG.BUTTON_H * 3 / 5) - CFG.PADD * 2),
         menuElements
      );
      this.updateLang();
   }

   @Override
   public void updateLang() {
      this.getTitleM().setText(CFG.lang.get("Events") + " [" + CFG.getNumberWthSpaces("" + HiddenEvents.visibleCount()) + "]");
   }

   @Override
   public void draw(SpriteBatch oSB, int iTranslateX, int iTranslateY, boolean sliderMenuIsActive) {
      IMGManager.getIMG(Images.gameTopEdgeLine)
         .draw2O(
            oSB,
            this.getPosX() - 2 - Core.PADDING + iTranslateX,
            this.getPosY() - IMGManager.getIMG(Images.gameTopEdgeLine).getHeight() + iTranslateY,
            this.getWidthM() + 2 + Core.PADDING,
            this.getHeightM(),
            false,
            true
         );
      super.draw(oSB, iTranslateX, iTranslateY, sliderMenuIsActive);
      oSB.setColor(CFG.COLOR_CREATE_NEW_GAME_BOX_PLAYERS);
      IMGManager.getIMG(Images.pix255)
         .drawO(oSB, this.getPosX() + iTranslateX, this.getPosY() - IMGManager.getIMG(Images.pix255).getHeight() + this.getHeightM(), this.getWidthM());
      oSB.setColor(new Color(CFG.COLOR_FLAG_FRAME.r, CFG.COLOR_FLAG_FRAME.g, CFG.COLOR_FLAG_FRAME.b, 0.4F));
      IMGManager.getIMG(Images.line32Off1)
         .drawO(
            oSB,
            this.getPosX() + iTranslateX,
            this.getPosY() - IMGManager.getIMG(Images.pix255).getHeight() - IMGManager.getIMG(Images.line32Off1).getHeight() + this.getHeightM(),
            this.getWidthM(),
            1
         );
      oSB.setColor(new Color(0.0F, 0.0F, 0.0F, 0.5F));
      IMGManager.getIMG(Images.pix255).drawO(oSB, this.getPosX() - 2 + iTranslateX, this.getPosY() + this.getHeightM(), this.getWidthM() + 2);
      oSB.setColor(Color.WHITE);
   }

   @Override
   public void actionEL(int iID) {
      switch (iID) {
         case 0:
            if (CFG.menus.getKeyboard().getVisibleM()) {
               CFG.menus.getKeyboard().setVisibleM(false);
            } else {
               CFG.showKeyboard();
            }

            return;
         case 1:
            CFG.eventsManager.addEvent(new Event_GameData());
            CFG.eventsManager.createEvent_EditEventID = CFG.eventsManager.getEventsSize() - 1;
            CFG.eventsManager.createScenarioEvents = CFG.eventsManager.getEvent(CFG.eventsManager.createEvent_EditEventID);
            CFG.menus.setVisibleCreateScenario_Events_Edit(true);
            if (CFG.menus.getKeyboard().getVisibleM()) {
               CFG.menus.getKeyboard().setVisibleM(false);
            }

            return;
         default:
            int realID = iID;
            iID -= 2;
            if (iID % 3 == 0) {
               CFG.eventsManager.createEvent_EditEventID = this.getMenuElem(realID).getCurr();
               CFG.eventsManager.createScenarioEvents = CFG.eventsManager.getEvent(CFG.eventsManager.createEvent_EditEventID);
               CFG.menus.setVisibleCreateScenario_Events_Edit(true);
            } else if (iID % 3 == 1) {
               try {
                  // 原版这里是 addEvent2(dc(...))：dc() 深拷贝失败时返回 null，addEvent2(null) 静默不加事件，
                  // 但后面照样把编辑目标指向"最后一个事件"并弹 Copy: Done —— 复制列表最后一项时编辑到的
                  // 就是原事件本身，于是表现为"复制出来的事件与原事件 id 相同"。
                  EventCopyUtil.copyEvent(this.getMenuElem(realID - 1).getCurr());
               } catch (Exception var4) {
                  CFG.exceptionStack(var4);
               }
            } else {
               CFG.eventsManager.createEvent_EditEventID = this.getMenuElem(realID - 2).getCurr();
               CFG.setDialogType(DialogType.CREATE_SCENARIO_REMOVE_EVENT);
            }

            if (CFG.menus.getKeyboard().getVisibleM()) {
               CFG.menus.getKeyboard().setVisibleM(false);
            }
      }
   }

   public static Event_GameData dc(Event_GameData original) {
      try {
         ByteArrayOutputStream bos = new ByteArrayOutputStream();
         ObjectOutputStream out = new ObjectOutputStream(bos);
         out.writeObject(original);
         out.flush();
         ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
         return (Event_GameData)in.readObject();
      } catch (Exception var4) {
         var4.printStackTrace();
         return null;
      }
   }
}

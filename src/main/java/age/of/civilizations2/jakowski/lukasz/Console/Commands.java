package age.of.civilizations2.jakowski.lukasz.Console;

import age.of.civilizations2.jakowski.lukasz.AoCGame;
import age.of.civilizations2.jakowski.lukasz.CFG;
import age.of.civilizations2.jakowski.lukasz.CivInvest;
import age.of.civilizations2.jakowski.lukasz.CivTask;
import age.of.civilizations2.jakowski.lukasz.Civilization;
import age.of.civilizations2.jakowski.lukasz.Event_GameData;
import age.of.civilizations2.jakowski.lukasz.MapScale;
import age.of.civilizations2.jakowski.lukasz.Point_XY2;
import age.of.civilizations2.jakowski.lukasz.RenderProvince;
import age.of.civilizations2.jakowski.lukasz.View;
import age.of.civilizations2.jakowski.lukasz.Editor.Editor_NeighboringProvinces;
import age.of.civilizations2.jakowski.lukasz.GameValues.GameValues;
import age.of.civilizations2.jakowski.lukasz.HistoryLog.HistoryLog_JoinAlliance;
import age.of.civilizations2.jakowski.lukasz.Menus.Civilization.Menu_InGame_Civ_Decisions;
import age.of.civilizations2.jakowski.lukasz.Menus.War.Menu_InGame_PrepareForWar;
import age.of.civilizations2.jakowski.lukasz.Menus.ZRest.Menu_InGame_Event;
import age.of.civilizations2.jakowski.lukasz.Messages.Truce.SignPeace.Message_WeCanSignPeace;
import age.of.civilizations2.jakowski.lukasz.Z_Other.ST.sUM;
import com.badlogic.gdx.Gdx;
import java.util.ArrayList;
import java.util.List;

public class Commands {
   public static final int CONSOLE_LIMIT = 300;
   public static List<String> sConsole = new ArrayList<>();
   public static List<Point_XY2> lFlagsParty = new ArrayList<>();
   public static long lFlagsPartyTime = 0L;
   /** ev s 单次最多打印的条数，避免刷爆控制台缓冲区 */
   private static final int EVENT_SEARCH_PRINT_LIMIT = 60;
   /** 最近一次 ev s 的搜索结果（事件ID），供 ev c 序号 使用 */
   private static List<Integer> lEventSearchResult = new ArrayList<>();
   /** 最近一次 ev s 使用的关键词 */
   private static String sEventSearchKeyword = "";

   public static void addMessage(String nMess) {
      sConsole.add(nMess);
      if (sConsole.size() > 300) {
         sConsole.remove(0);
      }
   }

   public static void execute(String nCommand) {
      if (nCommand.length() != 0) {
         addMessage("");
         addMessage("#" + nCommand);
         String[] tempCommand = nCommand.toLowerCase().split(" ");

         try {
            if (tempCommand.length > 0) {
               // ev / ev=事件id / ev+s+关键词 / ev+c+序号：放在最前面，无需先打开控制台
               if (tempCommand[0].equals("ev") || tempCommand[0].startsWith("ev=") || tempCommand[0].startsWith("ev+")) {
                  executeEventCommand(nCommand);
                  return;
               }

               if (tempCommand[0].equals("console")) {
                  CFG.menus.setVisible_InGame_FlagAction_Console(!CFG.menus.getVisible_InGame_FlagAction_Console());
                  if (CFG.menus.getVisible_InGame_FlagAction_Console()) {
                     CFG.toastM.addM("Hello");
                  }

                  return;
               }

               if (tempCommand[0].equals("info")) {
                  addMessage("FramesPerSecond: " + Gdx.graphics.getFramesPerSecond());
                  addMessage("Width: " + Gdx.graphics.getWidth());
                  addMessage("Height: " + Gdx.graphics.getHeight());
                  addMessage("PpiX: " + Gdx.graphics.getPpiX());
                  addMessage("PpiY: " + Gdx.graphics.getPpiY());
                  addMessage("Density: " + Gdx.graphics.getDensity());
                  addMessage("XHDPI: " + CFG.XHDPI);
                  addMessage("XXHDPI: " + CFG.XXHDPI);
                  addMessage("XXXHDPI: " + CFG.XXXHDPI);
                  return;
               }

               if (tempCommand[0].equals("debug")) {
                  CFG.DEBUG_MODE = !CFG.DEBUG_MODE;
                  addMessage(CFG.lang.get(CFG.lang.get("DEBUG") + ": " + (CFG.DEBUG_MODE ? CFG.lang.get("Enabled") : CFG.lang.get("Disabled"))));
                  CFG.toastM.addM(CFG.lang.get(CFG.lang.get("DEBUG") + ": " + (CFG.DEBUG_MODE ? CFG.lang.get("Enabled") : CFG.lang.get("Disabled"))));
                  return;
               }

               if (tempCommand[0].equals("neutral")) {
                  for (int i = 0; i < CFG.core.getProvinSize(); i++) {
                     if (CFG.core.getProv(i).getWastelandLvl() < 0 && CFG.core.getProv(i).getCivId() == 0 && !CFG.core.getProv(i).getSeaProv()) {
                        CFG.core.setActiveProvID(i);
                        CFG.map.getMpC().centerToProvID(CFG.core.getActiveProvID());
                        break;
                     }
                  }

                  return;
               }

               if (tempCommand[0].equals("center")) {
                  if (tempCommand.length > 1) {
                     try {
                        int tempID = Integer.parseInt(tempCommand[1]);
                        if (tempID < CFG.core.getProvinSize()) {
                           CFG.map.getMpC().centerToProvID(tempID);
                           CFG.core.setActiveProvID(tempID);
                           CFG.toastM.addM(CFG.core.getProv(tempID).getName());
                        } else {
                           IllegalCommand();
                        }

                        return;
                     } catch (IllegalArgumentException var18) {
                        IllegalCommand();
                     } catch (IndexOutOfBoundsException var19) {
                        IllegalCommand();
                     }
                  } else {
                     CFG.map.getMpSl().stopScrollingTheMap();
                     CFG.map.getMpS().setCurrScale(MapScale.MINSCALE);
                     CFG.map.getMpC().setNewPosX(-((int)((float)(CFG.map.getMpB().getWidthM() / 2) - (float)CFG.GAMEWIDTH / MapScale.MINSCALE / 2.0F)));
                     CFG.map.getMpC().setNewPosY(-((int)((float)(CFG.map.getMpB().getHeightM() / 2) - (float)CFG.GAMEHEIGHT / MapScale.MINSCALE / 2.0F)));
                  }

                  return;
               }

               if (tempCommand[0].equals("centerciv")) {
                  if (tempCommand.length > 1) {
                     try {
                        int tempID = Integer.parseInt(tempCommand[1]);
                        if (tempID < CFG.core.getCivsSize() && tempID > 0) {
                           CFG.map.getMpC().centerToCivilizationBox(tempID, true);
                           CFG.toastM.addM(CFG.core.getCiv(tempID).getCivName());
                        }
                     } catch (IllegalArgumentException var27) {
                        for (int ix = 1; ix < CFG.core.getCivsSize(); ix++) {
                           if (tempCommand[1].equals(CFG.core.getCiv(ix).getCivName()) || tempCommand[1].equals(CFG.core.getCiv(ix).getCivTag())) {
                              CFG.map.getMpC().centerToCivilizationBox(ix, true);
                              CFG.toastM.addM(CFG.core.getCiv(ix).getCivName());
                              return;
                           }
                        }

                        IllegalCommand();
                     } catch (IndexOutOfBoundsException var28) {
                        IllegalCommand();
                     }
                  } else {
                     IllegalCommand();
                  }

                  return;
               }

               if (tempCommand[0].equals("scale")) {
                  if (tempCommand.length > 1) {
                     try {
                        tempCommand[1] = tempCommand[1].replace(',', '.');
                        float tempS = Float.parseFloat(tempCommand[1]);
                        CFG.map.getMpS().setCurrScale(tempS);
                        return;
                     } catch (IllegalArgumentException var26) {
                        IllegalCommand();
                     }
                  } else {
                     CFG.map.getMpS().setCurrScale(1.0F);
                  }

                  return;
               }

               if (tempCommand[0].equals("removeplayer") && tempCommand.length > 1) {
                  if (CFG.core.getPlayersSize() > 1) {
                     try {
                        int pTID = Integer.parseInt(tempCommand[1]);
                        CFG.core.removePlayer(pTID);
                        if (pTID > 0 && CFG.PLAYER_TURN_ID == pTID) {
                           CFG.PLAYER_TURN_ID = pTID - 1;
                           CFG.gameAction.loadActivePlayerData();
                        } else {
                           CFG.gameAction.loadActivePlayerData();
                        }
                     } catch (Exception var29) {
                     }
                  }
               } else if (tempCommand[0].equals("removeplayer")) {
                  if (CFG.core.getPlayersSize() > 1) {
                     int pTID = CFG.PLAYER_TURN_ID;
                     CFG.core.removePlayer(CFG.PLAYER_TURN_ID);
                     if (pTID > 0) {
                        CFG.PLAYER_TURN_ID = pTID - 1;
                        CFG.gameAction.loadActivePlayerData();
                     } else {
                        CFG.gameAction.loadActivePlayerData();
                     }
                  }
               } else {
                  label1363: {
                     if (!tempCommand[0].equals("close") && !tempCommand[0].equals("bye")) {
                        if (tempCommand[0].equals("fps")) {
                           AoCGame.drawFPS = !AoCGame.drawFPS;
                           return;
                        }

                        if (!tempCommand[0].equals("psand") && !tempCommand[0].equals("partial_sandbox") && !tempCommand[0].equals("partsand")) {
                           if (!tempCommand[0].equals("hi") && !tempCommand[0].equals("hello")) {
                              if (!tempCommand[0].equals("spin")
                                 && !tempCommand[0].equals("iss")
                                 && !tempCommand[0].equals("wheee")
                                 && !tempCommand[0].equals("whee")) {
                                 if (tempCommand[0].equals("explode")) {
                                    try {
                                       if (CFG.core.getActiveProvID() >= 0 && CFG.core.getProv(CFG.core.getActiveProvID()).getCivId() > 0) {
                                          Menu_InGame_PrepareForWar.explode(CFG.core.getProv(CFG.core.getActiveProvID()).getCivId());
                                       }
                                    } catch (Exception var20) {
                                    }
                                    break label1363;
                                 }

                                 if (!tempCommand[0].equals("help") && !tempCommand[0].equals("commands")) {
                                    if (!tempCommand[0].equals("party")
                                       && !tempCommand[0].equals("fuck")
                                       && !tempCommand[0].equals("fuk")
                                       && !tempCommand[0].equals("flags")) {
                                       if (!CFG.menus.getVisible_InGame_FlagAction_Console()) {
                                          break label1363;
                                       }

                                       if (tempCommand[0].equals("clear")) {
                                          sConsole.clear();
                                          lFlagsParty.clear();
                                          return;
                                       }

                                       if (!tempCommand[0].equals("Drew Durnil")
                                          && !tempCommand[0].equals("drew durnil")
                                          && !tempCommand[0].equals("drewdurnil")
                                          && !tempCommand[0].equals("drew")
                                          && !tempCommand[0].equals("Drew")
                                          && !tempCommand[0].equals("Durnil")
                                          && !tempCommand[0].equals("durnil")
                                          && !tempCommand[0].equals("observe")
                                          && !tempCommand[0].equals("noob")
                                          && !tempCommand[0].equals("Spectator")
                                          && !tempCommand[0].equals("spectator")) {
                                          if (!tempCommand[0].equals("civs") && !tempCommand[0].equals("tags")) {
                                             if (tempCommand[0].equals("civ")) {
                                                if (CFG.core.getActiveProvID() >= 0
                                                   && !CFG.core.getProv(CFG.core.getActiveProvID()).getSeaProv()
                                                   && CFG.core.getProv(CFG.core.getActiveProvID()).getWastelandLvl() < 0) {
                                                   addMessage(
                                                      "CIV ID: "
                                                         + CFG.core.getProv(CFG.core.getActiveProvID()).getCivId()
                                                         + ", TAG: "
                                                         + CFG.core.getCiv(CFG.core.getProv(CFG.core.getActiveProvID()).getCivId()).getCivTag()
                                                         + ", "
                                                         + CFG.core.getCiv(CFG.core.getProv(CFG.core.getActiveProvID()).getCivId()).getCivName()
                                                   );
                                                } else {
                                                   IllegalCommand();
                                                   CFG.toastM.addM(CFG.lang.get("ChooseAProvince"), CFG.COLOR_NEGATIVE_2);
                                                   addMessage(CFG.lang.get(CFG.lang.get("ChooseAProvince")));
                                                   addMessage("");
                                                }

                                                return;
                                             }

                                             if (tempCommand[0].equals("province")) {
                                                if (CFG.core.getActiveProvID() >= 0
                                                   && !CFG.core.getProv(CFG.core.getActiveProvID()).getSeaProv()
                                                   && CFG.core.getProv(CFG.core.getActiveProvID()).getWastelandLvl() < 0) {
                                                   addMessage(
                                                      "PROVINCE ID: "
                                                         + CFG.core.getActiveProvID()
                                                         + ", CIV TAG"
                                                         + CFG.core.getCiv(CFG.core.getProv(CFG.core.getActiveProvID()).getCivId()).getCivTag()
                                                   );
                                                   addMessage(
                                                      "POPULATION: "
                                                         + CFG.core.getProv(CFG.core.getActiveProvID()).getPop().getPops()
                                                         + ", ECONOMY"
                                                         + CFG.core.getProv(CFG.core.getActiveProvID()).getEco()
                                                   );
                                                } else {
                                                   IllegalCommand();
                                                   CFG.toastM.addM(CFG.lang.get("ChooseAProvince"), CFG.COLOR_NEGATIVE_2);
                                                   addMessage(CFG.lang.get(CFG.lang.get("ChooseAProvince")));
                                                   addMessage("");
                                                }

                                                return;
                                             }

                                             if (!tempCommand[0].equals("showids") && !tempCommand[0].equals("ids")) {
                                                if (tempCommand[0].equals("occupy")) {
                                                   if (CFG.core.getActiveProvID() < 0
                                                      || CFG.core.getProv(CFG.core.getActiveProvID()).getSeaProv()
                                                      || CFG.core.getProv(CFG.core.getActiveProvID()).getWastelandLvl() >= 0
                                                      || CFG.core.getProv(CFG.core.getActiveProvID()).getCivId() <= 0) {
                                                      CFG.toastM.addM(CFG.lang.get("ChooseAProvince"), CFG.COLOR_NEGATIVE_2);
                                                      addMessage(CFG.lang.get(CFG.lang.get("ChooseAProvince")));
                                                   } else if (CFG.core.getProv(CFG.core.getActiveProvID()).getCivId()
                                                      == CFG.core.getPlayer(CFG.PLAYER_TURN_ID).getCivId()) {
                                                      addMessage(
                                                         CFG.lang
                                                            .get(CFG.lang.get("ChooseAProvince") + ": " + CFG.lang.get("NOT") + " " + CFG.lang.get("Player"))
                                                      );
                                                   } else {
                                                      Civilization nCiv = CFG.core.getCiv(CFG.core.getProv(CFG.core.getActiveProvID()).getCivId());

                                                      for (int ixx = nCiv.getNumOfProvs() - 1; ixx >= 0; ixx--) {
                                                         int provID = nCiv.getProvID(ixx);
                                                         int playerArmy = CFG.core
                                                            .getProv(provID)
                                                            .getArmyCivID1(CFG.core.getPlayer(CFG.PLAYER_TURN_ID).getCivId());
                                                         CFG.core.getProv(provID).updateArmy4(CFG.core.getProv(CFG.core.getActiveProvID()).getCivId(), 0);
                                                         CFG.core.getProv(provID).setCivId(CFG.core.getPlayer(CFG.PLAYER_TURN_ID).getCivId(), false);
                                                         CFG.core.getProv(provID).updateArmy4(CFG.core.getPlayer(CFG.PLAYER_TURN_ID).getCivId(), playerArmy);
                                                      }

                                                      nCiv.updateNumberOfUnits();
                                                      addMessage(CFG.lang.get(CFG.lang.get("Occupy") + ": " + nCiv.getCivName()));
                                                   }

                                                   return;
                                                }

                                                if (tempCommand[0].equals("md")) {
                                                   try {
                                                      CFG.core.setActiveProvID(-1);
                                                      CFG.menus.rebuildManageDiplomacy_Alliances();
                                                      CFG.core.disableDrawCivlizationsRegions_Players();
                                                      CFG.chosenAlphabetCharachter = null;
                                                      CFG.resetManageDiplomacyIDs();
                                                      CFG.backToMenu = View.eINGAME;
                                                      CFG.menus.setMenuID(View.eMANAGE_DIPLOMACY);
                                                      RenderProvince.updateDrawProvinces();
                                                      CFG.map.getTouchMgr().ueExA();
                                                   } catch (Exception var17) {
                                                      IllegalCommand();
                                                      CFG.toastM.addM(CFG.lang.get("Error"), CFG.COLOR_NEGATIVE_2);
                                                      addMessage(CFG.lang.get(CFG.lang.get("Error")));
                                                   }

                                                   return;
                                                }

                                                if (tempCommand[0].equals("relation") && tempCommand.length > 3) {
                                                   try {
                                                      int civA = Integer.parseInt(tempCommand[1]);
                                                      int civB = Integer.parseInt(tempCommand[2]);
                                                      int relation = Integer.parseInt(tempCommand[3]);
                                                      if (civA > 0 && civB > 0) {
                                                         CFG.core.getCiv(civA).setRelationD(civB, (float)relation);
                                                         CFG.core.getCiv(civB).setRelationD(civA, (float)relation);
                                                         CFG.toastM
                                                            .addM(
                                                               cheatMess()
                                                                  + CFG.lang.get("Relations")
                                                                  + ": "
                                                                  + CFG.core.getCiv(civA).getCivName()
                                                                  + " - "
                                                                  + CFG.core.getCiv(civB).getCivName()
                                                                  + ": "
                                                                  + relation
                                                            );
                                                         addMessage(
                                                            CFG.lang.get("Relations")
                                                               + ": "
                                                               + CFG.core.getCiv(civA).getCivName()
                                                               + " - "
                                                               + CFG.core.getCiv(civB).getCivName()
                                                               + ": "
                                                               + relation
                                                         );
                                                      } else {
                                                         IllegalCommand();
                                                         CFG.toastM.addM(CFG.lang.get("Error"), CFG.COLOR_NEGATIVE_2);
                                                         addMessage(CFG.lang.get(CFG.lang.get("Error")));
                                                      }
                                                   } catch (Exception var25) {
                                                      IllegalCommand();
                                                      CFG.toastM.addM(CFG.lang.get("Error"), CFG.COLOR_NEGATIVE_2);
                                                      addMessage(CFG.lang.get(CFG.lang.get("Error")));
                                                   }

                                                   return;
                                                }

                                                if (tempCommand[0].equals("showarmy")) {
                                                   CFG.core.buildDrawArmy();
                                                   return;
                                                }

                                                if (tempCommand[0].equals("chaos")) {
                                                   CFG.AGE_OF_CHAOS_MODE = !CFG.AGE_OF_CHAOS_MODE;
                                                   return;
                                                }

                                                if (tempCommand[0].equals("nukes")) {
                                                   CFG.ENABLE_NUKES = !CFG.ENABLE_NUKES;
                                                   addMessage(
                                                      CFG.lang.get("EnableNuclearWeapons")
                                                         + ": "
                                                         + (CFG.ENABLE_NUKES ? CFG.lang.get("Enabled") : CFG.lang.get("Disabled"))
                                                   );
                                                   CFG.toastM
                                                      .addM(
                                                         CFG.lang.get("EnableNuclearWeapons")
                                                            + ": "
                                                            + (CFG.ENABLE_NUKES ? CFG.lang.get("Enabled") : CFG.lang.get("Disabled")),
                                                         CFG.COLOR_NEGATIVE_2
                                                      );
                                                   return;
                                                }

                                                if (tempCommand[0].equals("totalwar")) {
                                                   CFG.TOTAL_WARMODE = !CFG.TOTAL_WARMODE;
                                                   return;
                                                }

                                                if (tempCommand[0].equals("aiwar")) {
                                                   CFG.USE_NEW_DECLARE_WAR_SYSTEM = !CFG.USE_NEW_DECLARE_WAR_SYSTEM;
                                                   return;
                                                }

                                                if (tempCommand[0].equals("retreat2")) {
                                                   try {
                                                      CFG.ARMY_RETREAT = Float.parseFloat(tempCommand[1]);
                                                      CFG.ARMY_RETREAT = Math.min(Math.max(0.0F, CFG.ARMY_RETREAT), 0.99F);
                                                   } catch (Exception var16) {
                                                   }

                                                   return;
                                                }

                                                if (tempCommand[0].equals("retreat")) {
                                                   try {
                                                      CFG.ARMY_RETREAT = (float)Integer.parseInt(tempCommand[1]) / 100.0F;
                                                      CFG.ARMY_RETREAT = Math.min(Math.max(0.0F, CFG.ARMY_RETREAT), 0.99F);
                                                   } catch (Exception var15) {
                                                   }

                                                   return;
                                                }

                                                if (tempCommand[0].equals("minarmy")) {
                                                   try {
                                                      CFG.MIN_ARMY_REQUIRED_TO_ATTACK = Integer.parseInt(tempCommand[1]);
                                                   } catch (Exception var14) {
                                                   }

                                                   return;
                                                }

                                                if (!CFG.SPECTATOR_MODE && tempCommand[0].equals("addplayer")) {
                                                   if (CFG.core.getActiveProvID() >= 0
                                                      && !CFG.core.getProv(CFG.core.getActiveProvID()).getSeaProv()
                                                      && CFG.core.getProv(CFG.core.getActiveProvID()).getWastelandLvl() < 0
                                                      && CFG.core.getProv(CFG.core.getActiveProvID()).getCivId() > 0
                                                      && !CFG.core.getCiv(CFG.core.getProv(CFG.core.getActiveProvID()).getCivId()).getIsPlayer()) {
                                                      if (CFG.SPECTATOR_MODE) {
                                                         CFG.SPECTATOR_MODE = false;
                                                         if (CFG.core.getPlayersSize() == 1) {
                                                            CFG.core.removePlayer(0);
                                                         }
                                                      }

                                                      CFG.core.addPlayer(CFG.core.getProv(CFG.core.getActiveProvID()).getCivId());
                                                      CFG.gameAction.buildFogOfWar(CFG.core.getPlayersSize() - 1);
                                                      if (CFG.FOG_OF_WAR == 2) {
                                                         CFG.core.getPlayer(CFG.core.getPlayersSize() - 1).buildMetProvsAndCivs();
                                                      }

                                                      CFG.core.getPlayer(CFG.core.getPlayersSize() - 1).loadPlayersFlag();
                                                      addMessage(
                                                         CFG.lang.get("Added")
                                                            + ": "
                                                            + CFG.core.getCiv(CFG.core.getProv(CFG.core.getActiveProvID()).getCivId()).getCivName()
                                                      );
                                                      return;
                                                   }

                                                   IllegalCommand();
                                                   CFG.toastM.addM(CFG.lang.get("ChooseAProvince"), CFG.COLOR_NEGATIVE_2);
                                                   addMessage(CFG.lang.get(CFG.lang.get("ChooseAProvince")));
                                                   addMessage("");
                                                   break label1363;
                                                }

                                                if (tempCommand[0].equals("gold")) {
                                                   try {
                                                      CFG.core
                                                         .getCiv(CFG.core.getPlayer(CFG.PLAYER_TURN_ID).getCivId())
                                                         .setGold((long)Integer.parseInt(tempCommand[1]));
                                                      CFG.menus.updateInGameTopAll(CFG.core.getPlayer(CFG.PLAYER_TURN_ID).getCivId());
                                                      return;
                                                   } catch (Exception var38) {
                                                      break label1363;
                                                   }
                                                }

                                                if (tempCommand[0].equals("sandbox")) {
                                                   try {
                                                      CFG.SANDBOX_MODE = !CFG.SANDBOX_MODE;
                                                      return;
                                                   } catch (Exception var37) {
                                                      break label1363;
                                                   }
                                                }

                                                if (tempCommand[0].equals("editor")) {
                                                   try {
                                                      CFG.INGAME_WORLD_EDITOR = !CFG.INGAME_WORLD_EDITOR;
                                                      return;
                                                   } catch (Exception var36) {
                                                      break label1363;
                                                   }
                                                }

                                                if (!tempCommand[0].equals("ass") && !tempCommand[0].equals("assign")) {
                                                   if (tempCommand[0].equals("sandboxai")) {
                                                      try {
                                                         CFG.SANDBOX_MODE_AI = !CFG.SANDBOX_MODE_AI;
                                                         return;
                                                      } catch (Exception var34) {
                                                         break label1363;
                                                      }
                                                   }

                                                   if (tempCommand[0].equals("nuke") && tempCommand.length > 1) {
                                                      try {
                                                         CFG.core.getCiv(CFG.core.getPlayer(CFG.PLAYER_TURN_ID).getCivId()).civGD.iNukes = Integer.parseInt(
                                                            tempCommand[1]
                                                         );
                                                         CFG.menus.setVisible_Menu_InGame_CurrentWars(true);
                                                         return;
                                                      } catch (Exception var33) {
                                                         break label1363;
                                                      }
                                                   }

                                                   if (tempCommand[0].equals("nuke")) {
                                                      try {
                                                         CFG.core.getCiv(CFG.core.getPlayer(CFG.PLAYER_TURN_ID).getCivId()).civGD.iNukes++;
                                                         CFG.menus.setVisible_Menu_InGame_CurrentWars(true);
                                                         return;
                                                      } catch (Exception var32) {
                                                         break label1363;
                                                      }
                                                   }

                                                   if (tempCommand[0].equals("move")) {
                                                      try {
                                                         CFG.core
                                                            .getCiv(CFG.core.getPlayer(CFG.PLAYER_TURN_ID).getCivId())
                                                            .setMovementPoints(Integer.parseInt(tempCommand[1]));
                                                         CFG.menus.updateInGameTopAll(CFG.core.getPlayer(CFG.PLAYER_TURN_ID).getCivId());
                                                         return;
                                                      } catch (Exception var31) {
                                                         break label1363;
                                                      }
                                                   }

                                                   if (tempCommand[0].equals("diplo")) {
                                                      try {
                                                         CFG.core
                                                            .getCiv(CFG.core.getPlayer(CFG.PLAYER_TURN_ID).getCivId())
                                                            .setDiploPoints(Integer.parseInt(tempCommand[1]));
                                                         CFG.menus.updateInGameTopAll(CFG.core.getPlayer(CFG.PLAYER_TURN_ID).getCivId());
                                                         return;
                                                      } catch (Exception var30) {
                                                         break label1363;
                                                      }
                                                   }

                                                   if (tempCommand[0].equals("addciv")) {
                                                      if (tempCommand.length <= 1) {
                                                         IllegalCommand();
                                                      } else if (CFG.core.getActiveProvID() >= 0
                                                         && !CFG.core.getProv(CFG.core.getActiveProvID()).getSeaProv()
                                                         && CFG.core.getProv(CFG.core.getActiveProvID()).getWastelandLvl() < 0
                                                         && !CFG.core.getProv(CFG.core.getActiveProvID()).isCapital()) {
                                                         for (int ixx = 1; ixx < CFG.core.getCivsSize(); ixx++) {
                                                            if (CFG.core.getCiv(ixx).getCivTag().equals(tempCommand[1])) {
                                                               IllegalCommand();
                                                               addMessage(CFG.core.getCiv(ixx).getCivName() + ": IS IN THE GAME");
                                                               addMessage("");
                                                               return;
                                                            }
                                                         }

                                                         CFG.core
                                                            .getProv(CFG.core.getActiveProvID())
                                                            .updateArmy4(CFG.core.getProv(CFG.core.getActiveProvID()).getCivId(0), 0);
                                                         CFG.core
                                                            .createScenarioAddCivilization(tempCommand[1], CFG.core.getActiveProvID(), false, true, true, false);
                                                         if (CFG.FOG_OF_WAR == 2) {
                                                            for (int ixxx = 0; ixxx < CFG.core.getPlayersSize(); ixxx++) {
                                                               CFG.core.getPlayer(ixxx).addMetCiv(true);
                                                            }
                                                         }

                                                         int tempPop = CFG.core.getProv(CFG.core.getActiveProvID()).getPop().getPops();
                                                         CFG.core.getProv(CFG.core.getActiveProvID()).getPop().clearData();
                                                         CFG.core
                                                            .getProv(CFG.core.getActiveProvID())
                                                            .getPop()
                                                            .setPopulationOfCivID(CFG.core.getProv(CFG.core.getActiveProvID()).getCivId(), tempPop);
                                                         CFG.core.getCiv(CFG.core.getProv(CFG.core.getActiveProvID()).getCivId()).setGold(100L);
                                                         CFG.gameAction.updateCivsMovementPoints(CFG.core.getProv(CFG.core.getActiveProvID()).getCivId());
                                                         CFG.gameAction.updateCivsDiploPoints(CFG.core.getProv(CFG.core.getActiveProvID()).getCivId());
                                                         CFG.gameAction.buildRank_Score(CFG.core.getProv(CFG.core.getActiveProvID()).getCivId());
                                                         int tActiveProvince = CFG.core.getActiveProvID();
                                                         CFG.core.setActiveProvID(-1);
                                                         CFG.core.setActiveProvID(tActiveProvince);
                                                         addMessage(
                                                            CFG.lang.get("Added")
                                                               + ": "
                                                               + CFG.core.getCiv(CFG.core.getProv(CFG.core.getActiveProvID()).getCivId()).getCivName()
                                                         );
                                                      } else {
                                                         IllegalCommand();
                                                         CFG.toastM.addM(CFG.lang.get("ChooseAProvince"), CFG.COLOR_NEGATIVE_2);
                                                         addMessage(CFG.lang.get(CFG.lang.get("ChooseAProvince")));
                                                         addMessage("");
                                                      }

                                                      return;
                                                   }

                                                   if (tempCommand[0].equals("union") && tempCommand.length > 2) {
                                                      try {
                                                         int civA = Integer.parseInt(tempCommand[1]);
                                                         int civB = Integer.parseInt(tempCommand[2]);
                                                         if (civA > 0 && civB > 0) {
                                                            CFG.core
                                                               .setCivRelationOfCivB(civA, civB, Math.max(CFG.core.getCivRelationOfCivB(civA, civB), 25.0F));
                                                            CFG.core
                                                               .setCivRelationOfCivB(civB, civA, Math.max(CFG.core.getCivRelationOfCivB(civB, civA), 25.0F));
                                                            CFG.core.getCiv(civA).civGD.numOfUnions++;
                                                            CFG.core.getCiv(civB).civGD.numOfUnions++;
                                                            CFG.createUnionCivs(civA, civB);
                                                            CFG.toastM
                                                               .addM(
                                                                  cheatMess()
                                                                     + CFG.lang.get("Union")
                                                                     + ": "
                                                                     + CFG.core.getCiv(civA).getCivName()
                                                                     + " - "
                                                                     + CFG.core.getCiv(civB).getCivName()
                                                               );
                                                            addMessage(
                                                               CFG.lang.get("Union")
                                                                  + ": "
                                                                  + CFG.core.getCiv(civA).getCivName()
                                                                  + " - "
                                                                  + CFG.core.getCiv(civB).getCivName()
                                                            );
                                                         } else {
                                                            IllegalCommand();
                                                            CFG.toastM.addM(CFG.lang.get("Error"), CFG.COLOR_NEGATIVE_2);
                                                            addMessage(CFG.lang.get(CFG.lang.get("Error")));
                                                         }
                                                      } catch (Exception var24) {
                                                         IllegalCommand();
                                                         CFG.toastM.addM(CFG.lang.get("Error"), CFG.COLOR_NEGATIVE_2);
                                                         addMessage(CFG.lang.get(CFG.lang.get("Error")));
                                                      }

                                                      return;
                                                   }

                                                   if (tempCommand[0].equals("annex") && tempCommand.length > 2) {
                                                      try {
                                                         int civA = Integer.parseInt(tempCommand[1]);
                                                         int civB = Integer.parseInt(tempCommand[2]);
                                                         if (civA > 0 && civB > 0) {
                                                            List<Integer> tempProvinces = new ArrayList<>();

                                                            for (int ixxx = 0; ixxx < CFG.core.getCiv(civB).getNumOfProvs(); ixxx++) {
                                                               tempProvinces.add(CFG.core.getCiv(civB).getProvID(ixxx));
                                                            }

                                                            for (int ixxx = 0; ixxx < tempProvinces.size(); ixxx++) {
                                                               if (CFG.core.getProv(tempProvinces.get(ixxx)).getCivId() == civB
                                                                  && CFG.core.getProv(tempProvinces.get(ixxx)).getTrueOwnerOfProv() == civB) {
                                                                  int nArmyNewOwnerArmy = CFG.core.getProv(tempProvinces.get(ixxx)).getArmyCivID1(civA);
                                                                  CFG.core.getProv(tempProvinces.get(ixxx)).updateArmy4(0);
                                                                  CFG.core.getProv(tempProvinces.get(ixxx)).updateArmy4(civA, 0);
                                                                  CFG.core.getProv(tempProvinces.get(ixxx)).setTrueOwnerOfProv(civA);
                                                                  CFG.core.getProv(tempProvinces.get(ixxx)).setCivId(civA, false);
                                                                  CFG.core.getProv(tempProvinces.get(ixxx)).updateArmy4(civA, nArmyNewOwnerArmy);

                                                                  for (int j = CFG.core.getProv(tempProvinces.get(ixxx)).getCivsSize() - 1; j >= 0; j--) {
                                                                     if (CFG.core
                                                                              .getCiv(CFG.core.getProv(tempProvinces.get(ixxx)).getCivId(j))
                                                                              .getPuppetOfCiv()
                                                                           != civA
                                                                        && CFG.core.getCiv(civA).getPuppetOfCiv()
                                                                           != CFG.core.getProv(tempProvinces.get(ixxx)).getCivId(j)
                                                                        && (
                                                                           CFG.core.getCiv(CFG.core.getProv(tempProvinces.get(ixxx)).getCivId(j)).getAlliance()
                                                                                 <= 0
                                                                              || CFG.core
                                                                                    .getCiv(CFG.core.getProv(tempProvinces.get(ixxx)).getCivId(j))
                                                                                    .getAlliance()
                                                                                 != CFG.core.getCiv(civA).getAlliance()
                                                                        )
                                                                        && CFG.core
                                                                              .getMilitaryAccess(CFG.core.getProv(tempProvinces.get(ixxx)).getCivId(j), civA)
                                                                           <= 0) {
                                                                        CFG.gameAction
                                                                           .accessLost_MoveArmyToClosetsProvince(
                                                                              CFG.core.getProv(tempProvinces.get(ixxx)).getCivId(j), tempProvinces.get(ixxx)
                                                                           );
                                                                     }
                                                                  }
                                                               }
                                                            }

                                                            if (CFG.core.getCiv(civB).getCapitalProvID() >= 0) {
                                                               CFG.core.getProv(CFG.core.getCiv(civB).getCapitalProvID()).setIsCapital(false);

                                                               for (int ixxxx = 0;
                                                                  ixxxx < CFG.core.getProv(CFG.core.getCiv(civB).getCapitalProvID()).getCitiesSize();
                                                                  ixxxx++
                                                               ) {
                                                                  if (CFG.core.getProv(CFG.core.getCiv(civB).getCapitalProvID()).getCit(ixxxx).getCityLevel()
                                                                     == CFG.getEditorCityLevel(0)) {
                                                                     CFG.core
                                                                        .getProv(CFG.core.getCiv(civB).getCapitalProvID())
                                                                        .getCit(ixxxx)
                                                                        .setCityLevel(CFG.getEditorCityLevel(1));
                                                                  }
                                                               }
                                                            }

                                                            CFG.core.getCiv(civB).updateNumberOfUnits();
                                                            tempProvinces.clear();
                                                            CFG.core.buildCivilizationsRegions_TextOver(civB);
                                                            CFG.core.buildCivilizationsRegions_TextOver(civA);
                                                            CFG.core.getCiv(civB).setPuppetOfCivId(civB);
                                                            CFG.toastM
                                                               .addM(
                                                                  cheatMess()
                                                                     + CFG.lang.get("Annexation")
                                                                     + ": "
                                                                     + CFG.core.getCiv(civA).getCivName()
                                                                     + " -> "
                                                                     + CFG.core.getCiv(civB).getCivName()
                                                               );
                                                            addMessage(
                                                               CFG.lang.get("Annexation")
                                                                  + ": "
                                                                  + CFG.core.getCiv(civA).getCivName()
                                                                  + " -> "
                                                                  + CFG.core.getCiv(civB).getCivName()
                                                            );
                                                         } else {
                                                            IllegalCommand();
                                                            CFG.toastM.addM(CFG.lang.get("Error"), CFG.COLOR_NEGATIVE_2);
                                                            addMessage(CFG.lang.get(CFG.lang.get("Error")));
                                                         }
                                                      } catch (Exception var23) {
                                                         IllegalCommand();
                                                         CFG.toastM.addM(CFG.lang.get("Error"), CFG.COLOR_NEGATIVE_2);
                                                         addMessage(CFG.lang.get(CFG.lang.get("Error")));
                                                      }

                                                      return;
                                                   }

                                                   if ((tempCommand[0].equals("vassal") || tempCommand[0].equals("puppet")) && tempCommand.length > 2) {
                                                      int civA = Integer.parseInt(tempCommand[2]);
                                                      int civB = Integer.parseInt(tempCommand[1]);
                                                      if (civA > 0 && civB > 0) {
                                                         CFG.core.getCiv(civA).setPuppetOfCivId(civB);
                                                         if (CFG.FOG_OF_WAR > 0) {
                                                            if (CFG.core.getCiv(civA).getIsPlayer()) {
                                                               int tPlayerID = CFG.core.getPlayerIDbyCivID(civA);
                                                               if (tPlayerID >= 0) {
                                                                  for (int ixxxxx = 0; ixxxxx < CFG.core.getCiv(civB).getNumOfProvs(); ixxxxx++) {
                                                                     CFG.core.getProv(CFG.core.getCiv(civB).getProvID(ixxxxx)).updateFogOfWar(tPlayerID);
                                                                  }
                                                               }
                                                            }

                                                            if (CFG.core.getCiv(civB).getIsPlayer()) {
                                                               int tPlayerID = CFG.core.getPlayerIDbyCivID(civB);
                                                               if (tPlayerID >= 0) {
                                                                  for (int ixxxxx = 0; ixxxxx < CFG.core.getCiv(civA).getNumOfProvs(); ixxxxx++) {
                                                                     CFG.core.getProv(CFG.core.getCiv(civA).getProvID(ixxxxx)).updateFogOfWar(tPlayerID);
                                                                  }
                                                               }
                                                            }
                                                         }

                                                         CFG.toastM
                                                            .addM(
                                                               cheatMess()
                                                                  + CFG.lang.get("Lord")
                                                                  + ":  "
                                                                  + CFG.core.getCiv(civB).getCivName()
                                                                  + ", "
                                                                  + CFG.lang.get("Vassal")
                                                                  + ": "
                                                                  + CFG.core.getCiv(civA).getCivName()
                                                            );
                                                         addMessage(
                                                            CFG.lang.get("Lord")
                                                               + ":  "
                                                               + CFG.core.getCiv(civB).getCivName()
                                                               + ", "
                                                               + CFG.lang.get("Vassal")
                                                               + ": "
                                                               + CFG.core.getCiv(civA).getCivName()
                                                         );
                                                      } else {
                                                         IllegalCommand();
                                                         CFG.toastM.addM(CFG.lang.get("Error"), CFG.COLOR_NEGATIVE_2);
                                                         addMessage(CFG.lang.get(CFG.lang.get("Error")));
                                                      }

                                                      return;
                                                   }

                                                   if (tempCommand[0].equals("ww") && tempCommand.length > 1) {
                                                      try {
                                                         int value = Integer.parseInt(tempCommand[1]);
                                                         CFG.core
                                                            .getCiv(CFG.core.getProv(CFG.core.getActiveProvID()).getCivId())
                                                            .setWarWeariness((float)value / 100.0F);
                                                         addMessage(
                                                            CFG.lang.get("WarWeariness")
                                                               + ": "
                                                               + value
                                                               + "% -> "
                                                               + CFG.core.getCiv(CFG.core.getProv(CFG.core.getActiveProvID()).getCivId()).getCivName()
                                                         );
                                                         addMessage("");
                                                         CFG.toastM
                                                            .addM(
                                                               cheatMess()
                                                                  + CFG.lang.get("WarWeariness")
                                                                  + ": "
                                                                  + value
                                                                  + "% -> "
                                                                  + CFG.core.getCiv(CFG.core.getProv(CFG.core.getActiveProvID()).getCivId()).getCivName()
                                                            );
                                                      } catch (Exception var13) {
                                                         IllegalCommand();
                                                         CFG.toastM.addM(CFG.lang.get("Error"), CFG.COLOR_NEGATIVE_2);
                                                         addMessage(CFG.lang.get(CFG.lang.get("Error")));
                                                      }

                                                      return;
                                                   }

                                                   if (!tempCommand[0].equals("as") && !tempCommand[0].equals("assimilate")) {
                                                      if (!tempCommand[0].equals("technology") && !tempCommand[0].equals("tech")) {
                                                         if (tempCommand[0].equals("population")) {
                                                            if (CFG.core.getActiveProvID() >= 0
                                                               && CFG.core.getProv(CFG.core.getActiveProvID()).getWastelandLvl() < 0
                                                               && !CFG.core.getProv(CFG.core.getActiveProvID()).getSeaProv()) {
                                                               CFG.core
                                                                  .getProv(CFG.core.getActiveProvID())
                                                                  .getPop()
                                                                  .setPopulationOfCivID(
                                                                     CFG.core.getProv(CFG.core.getActiveProvID()).getCivId(),
                                                                     750
                                                                        + CFG.core
                                                                           .getProv(CFG.core.getActiveProvID())
                                                                           .getPop()
                                                                           .getPopulationOfCivID(CFG.core.getProv(CFG.core.getActiveProvID()).getCivId())
                                                                  );
                                                               addMessage(cheatMess() + CFG.lang.get("Population") + ": +" + 750);
                                                               addMessage("");
                                                               int tActiveProvince = CFG.core.getActiveProvID();
                                                               CFG.core.setActiveProvID(-1);
                                                               CFG.core.setActiveProvID(tActiveProvince);
                                                               CFG.toastM.addM(cheatMess() + CFG.lang.get("Population"));
                                                               if (CFG.menus.getVisibleInGame_CensusOfProvince()) {
                                                                  CFG.menus.rebuildInGame_CensusOfProvince(CFG.core.getActiveProvID());
                                                               }
                                                            } else {
                                                               IllegalCommand();
                                                               CFG.toastM.addM(CFG.lang.get("ChooseAProvince"), CFG.COLOR_NEGATIVE_2);
                                                               addMessage(CFG.lang.get(CFG.lang.get("ChooseAProvince")));
                                                               addMessage("");
                                                            }

                                                            return;
                                                         }

                                                         if (!tempCommand[0].equals("armyset") && !tempCommand[0].equals("setarmy")) {
                                                            if (tempCommand[0].equals("noliberity")) {
                                                               CFG.VASSALS_CAN_DECLARE_INDEPENDENCE = !CFG.VASSALS_CAN_DECLARE_INDEPENDENCE;
                                                               addMessage(
                                                                  cheatMess()
                                                                     + CFG.lang.get("Liberation")
                                                                     + ": "
                                                                     + (
                                                                        CFG.VASSALS_CAN_DECLARE_INDEPENDENCE
                                                                           ? CFG.lang.get("Disabled")
                                                                           : CFG.lang.get("Enabled")
                                                                     )
                                                               );
                                                               addMessage("");
                                                               CFG.toastM
                                                                  .addM(
                                                                     cheatMess()
                                                                        + CFG.lang.get("Liberation")
                                                                        + ": "
                                                                        + (
                                                                           CFG.VASSALS_CAN_DECLARE_INDEPENDENCE
                                                                              ? CFG.lang.get("Disabled")
                                                                              : CFG.lang.get("Enabled")
                                                                        )
                                                                  );
                                                               return;
                                                            }

                                                            if (tempCommand[0].equals("id")) {
                                                               if (CFG.core.getActiveProvID() >= 0) {
                                                                  addMessage(cheatMess() + CFG.lang.get("Province") + ": " + CFG.core.getActiveProvID());
                                                                  addMessage(
                                                                     cheatMess()
                                                                        + CFG.lang.get("Civilization")
                                                                        + ": "
                                                                        + CFG.core.getCiv(CFG.core.getProv(CFG.core.getActiveProvID()).getCivId()).getCivName()
                                                                        + ": "
                                                                        + CFG.core.getCiv(CFG.core.getProv(CFG.core.getActiveProvID()).getCivId()).getCivId()
                                                                  );
                                                                  addMessage("");
                                                                  int tActiveProvince = CFG.core.getActiveProvID();
                                                                  CFG.core.setActiveProvID(-1);
                                                                  CFG.core.setActiveProvID(tActiveProvince);
                                                                  CFG.toastM.addM(cheatMess() + CFG.lang.get("War"));
                                                               } else {
                                                                  IllegalCommand();
                                                                  CFG.toastM.addM(CFG.lang.get("ChooseAProvince"), CFG.COLOR_NEGATIVE_2);
                                                                  addMessage(CFG.lang.get(CFG.lang.get("ChooseAProvince")));
                                                                  addMessage("");
                                                               }

                                                               return;
                                                            }

                                                            if (tempCommand[0].equals("war")) {
                                                               int civA = Integer.parseInt(tempCommand[1]);
                                                               int civB = Integer.parseInt(tempCommand[2]);
                                                               if (civA >= 0
                                                                  && civB >= 0
                                                                  && CFG.core.getCiv(civA).getNumOfProvs() > 0
                                                                  && CFG.core.getCiv(civB).getNumOfProvs() > 0) {
                                                                  CFG.core.declareWar(civA, civB, true);
                                                                  addMessage(
                                                                     cheatMess()
                                                                        + CFG.lang.get("War")
                                                                        + ": "
                                                                        + CFG.core.getCiv(civA).getCivName()
                                                                        + " -> "
                                                                        + CFG.core.getCiv(civB).getCivName()
                                                                  );
                                                                  addMessage("");
                                                                  int tActiveProvince = CFG.core.getActiveProvID();
                                                                  CFG.core.setActiveProvID(-1);
                                                                  CFG.core.setActiveProvID(tActiveProvince);
                                                                  CFG.toastM.addM(cheatMess() + CFG.lang.get("War"));
                                                               } else {
                                                                  IllegalCommand();
                                                                  CFG.toastM.addM(CFG.lang.get("Error"), CFG.COLOR_NEGATIVE_2);
                                                                  addMessage(CFG.lang.get(CFG.lang.get("Error")));
                                                                  addMessage("");
                                                               }

                                                               return;
                                                            }

                                                            if (tempCommand[0].equals("peace")) {
                                                               int civA = Integer.parseInt(tempCommand[1]);
                                                               int civB = Integer.parseInt(tempCommand[2]);
                                                               if (civA >= 0 && civB >= 0 && CFG.core.getCivsAtWar(civA, civB)) {
                                                                  CFG.core
                                                                     .getCiv(civB)
                                                                     .civGD
                                                                     .civDiploGD
                                                                     .messageBox
                                                                     .addMessage(new Message_WeCanSignPeace(civA));
                                                                  addMessage(
                                                                     cheatMess()
                                                                        + CFG.lang.get("Added")
                                                                        + ": "
                                                                        + CFG.core.getCiv(civA).getCivName()
                                                                        + " -> "
                                                                        + CFG.core.getCiv(civB).getCivName()
                                                                  );
                                                                  addMessage("");
                                                                  int tActiveProvince = CFG.core.getActiveProvID();
                                                                  CFG.core.setActiveProvID(-1);
                                                                  CFG.core.setActiveProvID(tActiveProvince);
                                                                  CFG.toastM.addM(cheatMess() + CFG.lang.get("Added"));
                                                               } else {
                                                                  IllegalCommand();
                                                                  CFG.toastM.addM(CFG.lang.get("Error"), CFG.COLOR_NEGATIVE_2);
                                                                  addMessage(CFG.lang.get(CFG.lang.get("Error")));
                                                                  addMessage("");
                                                               }

                                                               return;
                                                            }

                                                            if (!tempCommand[0].equals("ally") && !tempCommand[0].equals("alliance")) {
                                                               if (tempCommand[0].equals("buildport")) {
                                                                  if (CFG.core.getActiveProvID() >= 0
                                                                     && CFG.core.getProv(CFG.core.getActiveProvID()).getWastelandLvl() < 0
                                                                     && CFG.core.getProv(CFG.core.getActiveProvID()).getLvlOfPort() >= 0) {
                                                                     CFG.core.getProv(CFG.core.getActiveProvID()).setLvlOfPort(1);
                                                                     addMessage(cheatMess() + "Port built");
                                                                     addMessage("");
                                                                     int tActiveProvince = CFG.core.getActiveProvID();
                                                                     CFG.core.setActiveProvID(-1);
                                                                     CFG.core.setActiveProvID(tActiveProvince);
                                                                     CFG.toastM.addM(cheatMess() + CFG.lang.get("Port built"));
                                                                  } else {
                                                                     IllegalCommand();
                                                                     CFG.toastM.addM(CFG.lang.get("ChooseAProvince"), CFG.COLOR_NEGATIVE_2);
                                                                     addMessage(CFG.lang.get(CFG.lang.get("ChooseAProvince")));
                                                                     addMessage("");
                                                                  }

                                                                  return;
                                                               }

                                                               if (tempCommand[0].equals("buildfort")) {
                                                                  if (CFG.core.getActiveProvID() >= 0
                                                                     && CFG.core.getProv(CFG.core.getActiveProvID()).getWastelandLvl() < 0) {
                                                                     CFG.core.getProv(CFG.core.getActiveProvID()).setLvlOfFort(1);
                                                                     CFG.core.getProv(CFG.core.getActiveProvID()).updateDrawArmyInProv();
                                                                     addMessage(cheatMess() + "Fort built");
                                                                     addMessage("");
                                                                     int tActiveProvince = CFG.core.getActiveProvID();
                                                                     CFG.core.setActiveProvID(-1);
                                                                     CFG.core.setActiveProvID(tActiveProvince);
                                                                     CFG.toastM.addM(cheatMess() + CFG.lang.get("Fort built"));
                                                                  } else {
                                                                     IllegalCommand();
                                                                     CFG.toastM.addM(CFG.lang.get("ChooseAProvince"), CFG.COLOR_NEGATIVE_2);
                                                                     addMessage(CFG.lang.get(CFG.lang.get("ChooseAProvince")));
                                                                     addMessage("");
                                                                  }

                                                                  return;
                                                               }

                                                               if (tempCommand[0].equals("buildtower")) {
                                                                  if (CFG.core.getActiveProvID() >= 0
                                                                     && CFG.core.getProv(CFG.core.getActiveProvID()).getWastelandLvl() < 0) {
                                                                     CFG.core.getProv(CFG.core.getActiveProvID()).setLvlOfWatchTower(1);
                                                                     CFG.core.getProv(CFG.core.getActiveProvID()).updateDrawArmyInProv();
                                                                     addMessage(cheatMess() + "Tower built");
                                                                     addMessage("");
                                                                     int tActiveProvince = CFG.core.getActiveProvID();
                                                                     CFG.core.setActiveProvID(-1);
                                                                     CFG.core.setActiveProvID(tActiveProvince);
                                                                     CFG.toastM.addM(cheatMess() + CFG.lang.get("Tower built"));
                                                                  } else {
                                                                     IllegalCommand();
                                                                     CFG.toastM.addM(CFG.lang.get("ChooseAProvince"), CFG.COLOR_NEGATIVE_2);
                                                                     addMessage(CFG.lang.get(CFG.lang.get("ChooseAProvince")));
                                                                     addMessage("");
                                                                  }

                                                                  return;
                                                               }

                                                               if (tempCommand[0].equals("economy")) {
                                                                  if (CFG.core.getActiveProvID() >= 0
                                                                     && CFG.core.getProv(CFG.core.getActiveProvID()).getWastelandLvl() < 0
                                                                     && !CFG.core.getProv(CFG.core.getActiveProvID()).getSeaProv()) {
                                                                     CFG.core
                                                                        .getProv(CFG.core.getActiveProvID())
                                                                        .setEco(CFG.core.getProv(CFG.core.getActiveProvID()).getEco() + 600);
                                                                     addMessage(cheatMess() + CFG.lang.get("Economy") + ": +" + 600);
                                                                     addMessage("");
                                                                     int tActiveProvince = CFG.core.getActiveProvID();
                                                                     CFG.core.setActiveProvID(-1);
                                                                     CFG.core.setActiveProvID(tActiveProvince);
                                                                     CFG.toastM.addM(cheatMess() + CFG.lang.get("Economy"));
                                                                     if (CFG.menus.getVisibleInGame_CensusOfProvince()) {
                                                                        CFG.menus.rebuildInGame_CensusOfProvince(CFG.core.getActiveProvID());
                                                                     }
                                                                  } else {
                                                                     IllegalCommand();
                                                                     CFG.toastM.addM(CFG.lang.get("ChooseAProvince"), CFG.COLOR_NEGATIVE_2);
                                                                     addMessage(CFG.lang.get(CFG.lang.get("ChooseAProvince")));
                                                                     addMessage("");
                                                                  }

                                                                  return;
                                                               }

                                                               if (tempCommand[0].equals("invest")) {
                                                                  if (CFG.core.getActiveProvID() >= 0
                                                                     && CFG.core.getProv(CFG.core.getActiveProvID()).getCivId() > 0) {
                                                                     for (int ixxxxx = 0;
                                                                        ixxxxx
                                                                           < CFG.core
                                                                              .getCiv(CFG.core.getProv(CFG.core.getActiveProvID()).getCivId())
                                                                              .getNumOfProvs();
                                                                        ixxxxx++
                                                                     ) {
                                                                        int economy = CFG.core.getProv(CFG.core.getActiveProvID()).getPop().getPops() / 10;
                                                                        CFG.core
                                                                           .getCiv(CFG.core.getProv(CFG.core.getActiveProvID()).getCivId())
                                                                           .addInvest(
                                                                              new CivInvest(
                                                                                 CFG.core
                                                                                    .getCiv(CFG.core.getProv(CFG.core.getActiveProvID()).getCivId())
                                                                                    .getProvID(ixxxxx),
                                                                                 GameValues.gvInvestEconomy.INVEST_ECO_NUM_OF_TURNS,
                                                                                 economy,
                                                                                 economy / GameValues.gvInvestEconomy.INVEST_ECO_NUM_OF_TURNS
                                                                              )
                                                                           );
                                                                     }

                                                                     addMessage(
                                                                        cheatMess()
                                                                           + CFG.lang.get("Invest")
                                                                           + ", "
                                                                           + CFG.core
                                                                              .getCiv(CFG.core.getProv(CFG.core.getActiveProvID()).getCivId())
                                                                              .getCivName()
                                                                           + ", "
                                                                           + CFG.lang.get("Provinces")
                                                                           + ": "
                                                                           + CFG.core
                                                                              .getCiv(CFG.core.getProv(CFG.core.getActiveProvID()).getCivId())
                                                                              .getNumOfProvs()
                                                                     );
                                                                     addMessage("");
                                                                     int tActiveProvince = CFG.core.getActiveProvID();
                                                                     CFG.core.setActiveProvID(-1);
                                                                     CFG.core.setActiveProvID(tActiveProvince);
                                                                     CFG.toastM
                                                                        .addM(
                                                                           cheatMess()
                                                                              + CFG.lang.get("Invest")
                                                                              + ", "
                                                                              + CFG.core
                                                                                 .getCiv(CFG.core.getProv(CFG.core.getActiveProvID()).getCivId())
                                                                                 .getCivName()
                                                                              + ", "
                                                                              + CFG.lang.get("Provinces")
                                                                              + ": "
                                                                              + CFG.core
                                                                                 .getCiv(CFG.core.getProv(CFG.core.getActiveProvID()).getCivId())
                                                                                 .getNumOfProvs()
                                                                        );
                                                                     if (CFG.menus.getVisibleInGame_CensusOfProvince()) {
                                                                        CFG.menus.rebuildInGame_CensusOfProvince(CFG.core.getActiveProvID());
                                                                     }
                                                                  } else {
                                                                     IllegalCommand();
                                                                     CFG.toastM.addM(CFG.lang.get("ChooseAProvince"), CFG.COLOR_NEGATIVE_2);
                                                                     addMessage(CFG.lang.get(CFG.lang.get("ChooseAProvince")));
                                                                     addMessage("");
                                                                  }

                                                                  return;
                                                               }

                                                               if ((tempCommand[0].equals("pop") || tempCommand[0].equals("population"))
                                                                  && tempCommand.length > 1) {
                                                                  try {
                                                                     if (CFG.core.getActiveProvID() >= 0) {
                                                                        int population = Math.max(100, Integer.parseInt(tempCommand[1]));
                                                                        CFG.core.getProv(CFG.core.getActiveProvID()).getPop().clearData();
                                                                        CFG.core
                                                                           .getProv(CFG.core.getActiveProvID())
                                                                           .getPop()
                                                                           .setPopulationOfCivID(
                                                                              CFG.core.getProv(CFG.core.getActiveProvID()).getCivId(), population
                                                                           );
                                                                     }
                                                                  } catch (Exception var11) {
                                                                  }

                                                                  return;
                                                               }

                                                               if (tempCommand[0].equals("setarmy") && tempCommand.length > 1) {
                                                                  try {
                                                                     if (CFG.core.getActiveProvID() >= 0) {
                                                                        CFG.core
                                                                           .getProv(CFG.core.getActiveProvID())
                                                                           .updateArmy4(
                                                                              CFG.core
                                                                                 .getProv(CFG.core.getActiveProvID())
                                                                                 .getCivId(CFG.activeCivilizationArmyID),
                                                                              Integer.parseInt(tempCommand[1])
                                                                           );
                                                                        addMessage(cheatMess() + CFG.lang.get("Army") + ": " + Integer.parseInt(tempCommand[1]));
                                                                        addMessage("");
                                                                        int tActiveProvince = CFG.core.getActiveProvID();
                                                                        CFG.core.setActiveProvID(-1);
                                                                        CFG.core.setActiveProvID(tActiveProvince);
                                                                        CFG.toastM.addM(cheatMess() + CFG.lang.get("Army"));
                                                                        if (CFG.menus.getVisibleInGame_CensusOfProvince()) {
                                                                           CFG.menus.rebuildInGame_CensusOfProvince(CFG.core.getActiveProvID());
                                                                        }
                                                                     } else {
                                                                        IllegalCommand();
                                                                        CFG.toastM.addM(CFG.lang.get("ChooseAProvince"), CFG.COLOR_NEGATIVE_2);
                                                                        addMessage(CFG.lang.get(CFG.lang.get("ChooseAProvince")));
                                                                        addMessage("");
                                                                     }
                                                                  } catch (Exception var10) {
                                                                  }

                                                                  return;
                                                               }

                                                               if (tempCommand[0].equals("army") && tempCommand.length > 1) {
                                                                  try {
                                                                     if (CFG.core.getActiveProvID() >= 0) {
                                                                        CFG.core
                                                                           .getProv(CFG.core.getActiveProvID())
                                                                           .updateArmy4(
                                                                              CFG.core
                                                                                 .getProv(CFG.core.getActiveProvID())
                                                                                 .getCivId(CFG.activeCivilizationArmyID),
                                                                              CFG.core
                                                                                    .getProv(CFG.core.getActiveProvID())
                                                                                    .getArmyID(CFG.activeCivilizationArmyID)
                                                                                 + Integer.parseInt(tempCommand[1])
                                                                           );
                                                                        addMessage(
                                                                           cheatMess() + CFG.lang.get("Army") + ": +" + Integer.parseInt(tempCommand[1])
                                                                        );
                                                                        addMessage("");
                                                                        int tActiveProvince = CFG.core.getActiveProvID();
                                                                        CFG.core.setActiveProvID(-1);
                                                                        CFG.core.setActiveProvID(tActiveProvince);
                                                                        CFG.toastM.addM(cheatMess() + CFG.lang.get("Army"));
                                                                        if (CFG.menus.getVisibleInGame_CensusOfProvince()) {
                                                                           CFG.menus.rebuildInGame_CensusOfProvince(CFG.core.getActiveProvID());
                                                                        }
                                                                     } else {
                                                                        IllegalCommand();
                                                                        CFG.toastM.addM(CFG.lang.get("ChooseAProvince"), CFG.COLOR_NEGATIVE_2);
                                                                        addMessage(CFG.lang.get(CFG.lang.get("ChooseAProvince")));
                                                                        addMessage("");
                                                                     }
                                                                  } catch (Exception var9) {
                                                                  }

                                                                  return;
                                                               }

                                                               if (tempCommand[0].equals("army")) {
                                                                  if (CFG.core.getActiveProvID() >= 0) {
                                                                     CFG.core
                                                                        .getProv(CFG.core.getActiveProvID())
                                                                        .updateArmy4(
                                                                           CFG.core.getProv(CFG.core.getActiveProvID()).getCivId(CFG.activeCivilizationArmyID),
                                                                           CFG.core.getProv(CFG.core.getActiveProvID()).getArmyID(CFG.activeCivilizationArmyID)
                                                                              + 300
                                                                        );
                                                                     addMessage(cheatMess() + CFG.lang.get("Army") + ": +" + 300);
                                                                     addMessage("");
                                                                     int tActiveProvince = CFG.core.getActiveProvID();
                                                                     CFG.core.setActiveProvID(-1);
                                                                     CFG.core.setActiveProvID(tActiveProvince);
                                                                     CFG.toastM.addM(cheatMess() + CFG.lang.get("Army"));
                                                                     if (CFG.menus.getVisibleInGame_CensusOfProvince()) {
                                                                        CFG.menus.rebuildInGame_CensusOfProvince(CFG.core.getActiveProvID());
                                                                     }
                                                                  } else {
                                                                     IllegalCommand();
                                                                     CFG.toastM.addM(CFG.lang.get("ChooseAProvince"), CFG.COLOR_NEGATIVE_2);
                                                                     addMessage(CFG.lang.get(CFG.lang.get("ChooseAProvince")));
                                                                     addMessage("");
                                                                  }

                                                                  return;
                                                               }

                                                               if (!tempCommand[0].equals("money") && !tempCommand[0].equals("Gold")) {
                                                                  if (tempCommand[0].equals("movement")) {
                                                                     CFG.core
                                                                        .getCiv(CFG.core.getPlayer(CFG.PLAYER_TURN_ID).getCivId())
                                                                        .setMovementPoints(
                                                                           CFG.core.getCiv(CFG.core.getPlayer(CFG.PLAYER_TURN_ID).getCivId()).getMovemPoints()
                                                                              + CFG.ideologiesMgr
                                                                                    .getIdeologyID(
                                                                                       CFG.core
                                                                                          .getCiv(CFG.core.getPlayer(CFG.PLAYER_TURN_ID).getCivId())
                                                                                          .getIdeology()
                                                                                    )
                                                                                    .COST_OF_MOVE
                                                                                 * 10
                                                                        );
                                                                     addMessage(
                                                                        cheatMess()
                                                                           + CFG.lang.get("MovementPoints")
                                                                           + ": +"
                                                                           + CFG.ideologiesMgr
                                                                                 .getIdeologyID(
                                                                                    CFG.core
                                                                                       .getCiv(CFG.core.getPlayer(CFG.PLAYER_TURN_ID).getCivId())
                                                                                       .getIdeology()
                                                                                 )
                                                                                 .COST_OF_MOVE
                                                                              * 10
                                                                     );
                                                                     addMessage("");
                                                                     CFG.toastM.addM(cheatMess() + CFG.lang.get("movement"));
                                                                     CFG.menus.updateInGameTopAll(CFG.core.getPlayer(CFG.PLAYER_TURN_ID).getCivId());
                                                                     return;
                                                                  }

                                                                  if (tempCommand[0].equals("diplomacy")) {
                                                                     CFG.core
                                                                        .getCiv(CFG.core.getPlayer(CFG.PLAYER_TURN_ID).getCivId())
                                                                        .setDiploPoints(
                                                                           CFG.core.getCiv(CFG.core.getPlayer(CFG.PLAYER_TURN_ID).getCivId()).getDiploPoints()
                                                                              + CFG.ideologiesMgr
                                                                                    .getIdeologyID(
                                                                                       CFG.core
                                                                                          .getCiv(CFG.core.getPlayer(CFG.PLAYER_TURN_ID).getCivId())
                                                                                          .getIdeology()
                                                                                    )
                                                                                    .COST_OF_MOVE
                                                                                 * 3
                                                                                 / 4
                                                                        );
                                                                     addMessage(
                                                                        cheatMess()
                                                                           + CFG.lang.get("DiplomacyPoints")
                                                                           + ": +"
                                                                           + (float)(
                                                                                 CFG.ideologiesMgr
                                                                                       .getIdeologyID(
                                                                                          CFG.core
                                                                                             .getCiv(CFG.core.getPlayer(CFG.PLAYER_TURN_ID).getCivId())
                                                                                             .getIdeology()
                                                                                       )
                                                                                       .COST_OF_MOVE
                                                                                    * 3
                                                                                    / 4
                                                                              )
                                                                              / 10.0F
                                                                     );
                                                                     addMessage("");
                                                                     CFG.toastM.addM(cheatMess() + CFG.lang.get("diplomacy"));
                                                                     CFG.menus.updateInGameTopAll(CFG.core.getPlayer(CFG.PLAYER_TURN_ID).getCivId());
                                                                     return;
                                                                  }

                                                                  if (tempCommand[0].equals("reloadprovince")) {
                                                                     try {
                                                                        int tempID = Integer.parseInt(tempCommand[1]);
                                                                        if (tempID < CFG.core.getProvinSize()) {
                                                                           Editor_NeighboringProvinces.updateProvince(tempID);
                                                                           CFG.core.setActiveProvID(tempID);
                                                                           CFG.toastM.addM(CFG.core.getProv(tempID).getName());
                                                                        } else {
                                                                           IllegalCommand();
                                                                        }

                                                                        return;
                                                                     } catch (Exception var8) {
                                                                        IllegalCommand();
                                                                        return;
                                                                     }
                                                                  }
                                                                  break label1363;
                                                               }

                                                               CFG.core
                                                                  .getCiv(CFG.core.getPlayer(CFG.PLAYER_TURN_ID).getCivId())
                                                                  .setGold(CFG.core.getCiv(CFG.core.getPlayer(CFG.PLAYER_TURN_ID).getCivId()).getGold() + 450L);
                                                               addMessage(cheatMess() + CFG.lang.get("Treasury") + ": +" + 450);
                                                               addMessage("");
                                                               CFG.toastM.addM(cheatMess() + CFG.lang.get("Treasury"));
                                                               CFG.menus.updateInGameTopAll(CFG.core.getPlayer(CFG.PLAYER_TURN_ID).getCivId());
                                                               return;
                                                            }

                                                            try {
                                                               int civA = Integer.parseInt(tempCommand[1]);
                                                               int civB = Integer.parseInt(tempCommand[2]);
                                                               if (civA > 0 && civB > 0 && !CFG.core.getCivsAtWar(civA, civB)) {
                                                                  if (CFG.core.getCiv(civA).getAlliance() == 0 && CFG.core.getCiv(civB).getAlliance() == 0) {
                                                                     CFG.core.addAlliance(CFG.getRandomAllianceName(0));
                                                                     int tempAllianceID = CFG.core.getAlliancesSize() - 1;
                                                                     if (CFG.core.getCiv(civA).getIsPlayer()) {
                                                                        CFG.core.getAlliance(tempAllianceID).addCivilization(civA);
                                                                        CFG.core.getAlliance(tempAllianceID).addCivilization(civB);
                                                                     } else if (CFG.core.getCiv(civB).getIsPlayer()) {
                                                                        CFG.core.getAlliance(tempAllianceID).addCivilization(civB);
                                                                        CFG.core.getAlliance(tempAllianceID).addCivilization(civA);
                                                                     } else {
                                                                        CFG.core.getAlliance(tempAllianceID).addCivilization(civA);
                                                                        CFG.core.getAlliance(tempAllianceID).addCivilization(civB);
                                                                     }

                                                                     CFG.core.getCiv(civA).setAlliance(tempAllianceID);
                                                                     CFG.core.getCiv(civB).setAlliance(tempAllianceID);
                                                                     CFG.historyManager.addHistoryLog(new HistoryLog_JoinAlliance(civA, tempAllianceID));
                                                                     CFG.historyManager.addHistoryLog(new HistoryLog_JoinAlliance(civB, tempAllianceID));
                                                                  } else if (CFG.core.getCiv(civB).getAlliance() > 0
                                                                     && CFG.core.getCiv(civA).getAlliance() == 0) {
                                                                     CFG.core.getAlliance(CFG.core.getCiv(civB).getAlliance()).addCivilization(civA);
                                                                     CFG.core.getCiv(civA).setAlliance(CFG.core.getCiv(civB).getAlliance());
                                                                     CFG.historyManager
                                                                        .addHistoryLog(new HistoryLog_JoinAlliance(civA, CFG.core.getCiv(civB).getAlliance()));
                                                                  } else if (CFG.core.getCiv(civA).getAlliance() > 0
                                                                     && CFG.core.getCiv(civB).getAlliance() == 0) {
                                                                     CFG.core.getAlliance(CFG.core.getCiv(civA).getAlliance()).addCivilization(civB);
                                                                     CFG.core.getCiv(civB).setAlliance(CFG.core.getCiv(civA).getAlliance());
                                                                     CFG.historyManager
                                                                        .addHistoryLog(new HistoryLog_JoinAlliance(civB, CFG.core.getCiv(civA).getAlliance()));
                                                                  } else {
                                                                     CFG.core.getAlliance(CFG.core.getCiv(civA).getAlliance()).removeCivilization(civA);
                                                                     CFG.core.getAlliance(CFG.core.getCiv(civB).getAlliance()).addCivilization(civA);
                                                                     CFG.core.getCiv(civA).setAlliance(CFG.core.getCiv(civB).getAlliance());
                                                                     CFG.core.getCiv(civA).setAlliance(CFG.core.getCiv(civB).getAlliance());
                                                                  }

                                                                  if (CFG.core.getCiv(civA).getIsPlayer()) {
                                                                     CFG.gameAction.buildFogOfWar(CFG.core.getPlayerIDbyCivID(civA));
                                                                     CFG.core.getPlayer(CFG.core.getPlayerIDbyCivID(civA)).buildMetProvsAndCivs();
                                                                  }

                                                                  if (CFG.core.getCiv(civB).getIsPlayer()) {
                                                                     CFG.gameAction.buildFogOfWar(CFG.core.getPlayerIDbyCivID(civB));
                                                                     CFG.core.getPlayer(CFG.core.getPlayerIDbyCivID(civB)).buildMetProvsAndCivs();
                                                                  }

                                                                  addMessage(
                                                                     cheatMess()
                                                                        + CFG.lang.get("Alliance")
                                                                        + ": "
                                                                        + CFG.core.getCiv(civA).getCivName()
                                                                        + " - "
                                                                        + CFG.core.getCiv(civB).getCivName()
                                                                  );
                                                                  addMessage("");
                                                                  int tActiveProvince = CFG.core.getActiveProvID();
                                                                  CFG.core.setActiveProvID(-1);
                                                                  CFG.core.setActiveProvID(tActiveProvince);
                                                                  CFG.toastM
                                                                     .addM(
                                                                        cheatMess()
                                                                           + CFG.lang.get("Alliance")
                                                                           + ": "
                                                                           + CFG.core.getCiv(civA).getCivName()
                                                                           + " - "
                                                                           + CFG.core.getCiv(civB).getCivName()
                                                                     );
                                                               } else {
                                                                  IllegalCommand();
                                                                  CFG.toastM.addM(CFG.lang.get("Error"), CFG.COLOR_NEGATIVE_2);
                                                                  addMessage(CFG.lang.get(CFG.lang.get("Error")));
                                                                  addMessage("");
                                                               }
                                                            } catch (Exception var21) {
                                                               IllegalCommand();
                                                               CFG.toastM.addM(CFG.lang.get("Error"), CFG.COLOR_NEGATIVE_2);
                                                               addMessage(CFG.lang.get(CFG.lang.get("Error")));
                                                               addMessage("");
                                                            }

                                                            return;
                                                         }

                                                         int tArmy = Integer.parseInt(tempCommand[1]);
                                                         if (tArmy >= 0
                                                            && CFG.core.getActiveProvID() >= 0
                                                            && CFG.core.getProv(CFG.core.getActiveProvID()).getWastelandLvl() < 0
                                                            && !CFG.core.getProv(CFG.core.getActiveProvID()).getSeaProv()) {
                                                            CFG.core
                                                               .getCiv(CFG.core.getProv(CFG.core.getActiveProvID()).getCivId())
                                                               .setNumberOfUnits(
                                                                  CFG.core.getCiv(CFG.core.getProv(CFG.core.getActiveProvID()).getCivId()).getNumberOfUnits()
                                                                     - CFG.core.getProv(CFG.core.getActiveProvID()).getArmyID(0)
                                                               );
                                                            CFG.core.getProv(CFG.core.getActiveProvID()).updateArmy4(tArmy);
                                                            CFG.core
                                                               .getCiv(CFG.core.getProv(CFG.core.getActiveProvID()).getCivId())
                                                               .setNumberOfUnits(
                                                                  CFG.core.getCiv(CFG.core.getProv(CFG.core.getActiveProvID()).getCivId()).getNumberOfUnits()
                                                                     + tArmy
                                                               );
                                                            addMessage(cheatMess() + CFG.lang.get("Army") + ": " + tArmy);
                                                            addMessage("");
                                                            int tActiveProvince = CFG.core.getActiveProvID();
                                                            CFG.core.setActiveProvID(-1);
                                                            CFG.core.setActiveProvID(tActiveProvince);
                                                            CFG.toastM.addM(cheatMess() + CFG.lang.get("Army"));
                                                         } else {
                                                            IllegalCommand();
                                                            CFG.toastM.addM(CFG.lang.get("ChooseAProvince"), CFG.COLOR_NEGATIVE_2);
                                                            addMessage(CFG.lang.get(CFG.lang.get("ChooseAProvince")));
                                                            addMessage("");
                                                         }

                                                         return;
                                                      }

                                                      if (CFG.core.getActiveProvID() < 0
                                                         || CFG.core.getProv(CFG.core.getActiveProvID()).getWastelandLvl() >= 0
                                                         || CFG.core.getProv(CFG.core.getActiveProvID()).getSeaProv()) {
                                                         IllegalCommand();
                                                         CFG.toastM.addM(CFG.lang.get("ChooseAProvince"), CFG.COLOR_NEGATIVE_2);
                                                         addMessage(CFG.lang.get(CFG.lang.get("ChooseAProvince")));
                                                         addMessage("");
                                                      } else if (tempCommand.length > 1) {
                                                         try {
                                                            int tempTech = Integer.parseInt(tempCommand[1]);
                                                            if (tempTech > 200) {
                                                               tempTech = 200;
                                                            } else if (tempTech < 1) {
                                                               tempTech = 1;
                                                            }

                                                            CFG.core
                                                               .getCiv(CFG.core.getProv(CFG.core.getActiveProvID()).getCivId())
                                                               .setTechLevel((float)tempTech / 100.0F);
                                                            addMessage(
                                                               cheatMess()
                                                                  + CFG.lang.get("Technology")
                                                                  + ": "
                                                                  + CFG.core.getCiv(CFG.core.getProv(CFG.core.getActiveProvID()).getCivId()).getTechLevel()
                                                                  + ", "
                                                                  + CFG.core.getCiv(CFG.core.getProv(CFG.core.getActiveProvID()).getCivId()).getCivName()
                                                            );
                                                            addMessage("");
                                                            int tActiveProvince = CFG.core.getActiveProvID();
                                                            CFG.core.setActiveProvID(-1);
                                                            CFG.core.setActiveProvID(tActiveProvince);
                                                            CFG.toastM.addM(cheatMess() + CFG.lang.get("Technology"));
                                                         } catch (IllegalArgumentException var12) {
                                                            IllegalCommand();
                                                         }
                                                      } else {
                                                         IllegalCommand();
                                                      }

                                                      return;
                                                   }

                                                   try {
                                                      int num = 0;

                                                      for (int ixxxxx = 0;
                                                         ixxxxx < CFG.core.getCiv(CFG.core.getPlayer(CFG.PLAYER_TURN_ID).getCivId()).getNumOfProvs();
                                                         ixxxxx++
                                                      ) {
                                                         CFG.core
                                                            .getCiv(CFG.core.getPlayer(CFG.PLAYER_TURN_ID).getCivId())
                                                            .addAssimilate(
                                                               new CivTask(
                                                                  CFG.core.getCiv(CFG.core.getPlayer(CFG.PLAYER_TURN_ID).getCivId()).getProvID(ixxxxx),
                                                                  GameValues.gvAssimilate.ASSIMILATE_NUM_OF_TURNS_MAX
                                                               )
                                                            );
                                                         num++;
                                                      }

                                                      addMessage(CFG.lang.get("Assimilate") + ", " + CFG.lang.get("Provinces") + ": " + num);
                                                      addMessage("");
                                                      CFG.toastM.addM(cheatMess() + CFG.lang.get("Assimilate") + ", " + CFG.lang.get("Provinces") + ": " + num);
                                                   } catch (Exception var22) {
                                                      IllegalCommand();
                                                      CFG.toastM.addM(CFG.lang.get("Error"), CFG.COLOR_NEGATIVE_2);
                                                      addMessage(CFG.lang.get(CFG.lang.get("Error")));
                                                   }

                                                   return;
                                                }

                                                try {
                                                   Menu_InGame_Civ_Decisions.assignProvinces();
                                                   return;
                                                } catch (Exception var35) {
                                                   break label1363;
                                                }
                                             }

                                             CFG.core.buildDrawArmy_ShowIDs();
                                             CFG.toastM.addM("showarmy");
                                             CFG.toastM.setTimeInView(3500);
                                             addMessage(CFG.lang.get("Disable") + ": showarmy");
                                             return;
                                          }

                                          for (int ixxxxx = 1; ixxxxx < CFG.core.getCivsSize(); ixxxxx++) {
                                             addMessage(
                                                "CIV ID: "
                                                   + ixxxxx
                                                   + ", TAG: "
                                                   + CFG.core.getCiv(ixxxxx).getCivTag()
                                                   + ", "
                                                   + CFG.core.getCiv(ixxxxx).getCivName()
                                             );
                                          }

                                          return;
                                       }

                                       CFG.toastM.addM("Games -> New Game -> Options -> Spectactor Mode");
                                       CFG.toastM.setTimeInView(3500);
                                       addMessage("Games -> New Game -> Options -> Spectator Mode");
                                       return;
                                    }

                                    if (!CFG.menus.getVisible_InGame_FlagAction_Console()) {
                                       CFG.menus.setVisible_InGame_FlagAction_Console(true);
                                    }

                                    lFlagsParty.clear();

                                    for (int ixxxxx = 0; ixxxxx < CFG.GAMEWIDTH + CFG.GAMEHEIGHT; ixxxxx++) {
                                       lFlagsParty.add(new Point_XY2(CFG.oR.nextInt(CFG.GAMEWIDTH), CFG.oR.nextInt(CFG.GAMEHEIGHT)));
                                    }

                                    lFlagsPartyTime = System.currentTimeMillis();
                                    CFG.toastM.addM(CFG.lang.get("clear"));
                                    CFG.menus.getKeyboard().setVisibleM(false);
                                    return;
                                 }

                                 if (!CFG.menus.getVisible_InGame_FlagAction_Console()) {
                                    CFG.menus.setVisible_InGame_FlagAction_Console(true);
                                 }

                                 CFG.toastM.addM(CFG.lang.get("Help"));
                                 addMessage("#" + CFG.sVERSION + ": " + "2.01 Definitive Edition");
                                 addMessage("");
                                 addMessage("console - Toggles the console visibility.");
                                 addMessage("hi, hello - Greets the player and opens console if closed.");
                                 addMessage("help, commands - Shows a list of basic commands and game version.");
                                 addMessage("clear - Clears console output.");
                                 addMessage("fps - Toggles FPS display.");
                                 addMessage("info - Displays technical info: resolution, PPI, density.");
                                 addMessage("debug - Toggles debug mode.");
                                 addMessage("close, bye - Closes the console and keyboard.");
                                 addMessage("spin, wheee, whee, iss - Makes the map spin with a Wheee! toast.");
                                 addMessage("party, flags - Triggers flag party visual effect.");
                                 addMessage("scale X - Sets map zoom scale to X. Resets to 1.0 if no argument.");
                                 addMessage("center X - Centers the map on province ID X.");
                                 addMessage("centerciv X - Centers the map on civ ID or tag.");
                                 addMessage("neutral - Moves camera to a neutral (unclaimed) province.");
                                 addMessage("ids, showids - Shows province/civ IDs on the map.");
                                 addMessage("showarmy - Displays army positions on map.");
                                 addMessage("buildport - Builds a port in the selected province.");
                                 addMessage("buildfort - Builds a fort in the selected province.");
                                 addMessage("buildtower - Builds a watch tower in the selected province.");
                                 addMessage("economy - Increases economy in selected province by 80% of DEFAULT_ARMY.");
                                 addMessage("invest - Invests in all provinces of the civ that owns the selected province.");
                                 addMessage("addciv TAG - Adds a new civ with tag TAG to selected province.");
                                 addMessage("addplayer - Adds selected province's civ as a player.");
                                 addMessage("civ - Shows civ info (ID, name, tag) of selected province.");
                                 addMessage("civs, tags - Lists all civilizations with their ID and tag.");
                                 addMessage(
                                    "md - Redirects you to the Manage Diplomacy menu, where you can create alliances, vassals, pacts, and adjust relations"
                                 );
                                 addMessage("union X Y - Unites civilizations with IDs X and Y into a union.");
                                 addMessage("annex X Y - Civ X annexes all provinces of civ Y.");
                                 addMessage("vassal X Y, puppet X Y - Makes civ X a vassal of civ Y.");
                                 addMessage("noliberity - Toggles the No Liberation rule on/off.");
                                 addMessage("diplo X - Sets diplomacy points to X.");
                                 addMessage("relation A B X - Sets relationship between civ A and B to value X.");
                                 addMessage("war X Y - Declares war between civs with IDs X and Y.");
                                 addMessage("peace X Y - Signs peace between civs with IDs X and Y.");
                                 addMessage("ally X Y, alliance X Y - Forms alliance between civs with IDs X and Y.");
                                 addMessage("diplomacy - Adds diplomacy points (75% of ideology’s move cost).");
                                 addMessage("army X - Adds X units to selected province.");
                                 addMessage("army - Adds default number of units (40% of DEFAULT_ARMY).");
                                 addMessage("setarmy X, armyset X - Sets army size to X in selected province.");
                                 addMessage("showarmy - Shows armies on map.");
                                 addMessage("movement - Adds movement points (half ideology’s move cost).");
                                 addMessage("move X - Sets movement points to X.");
                                 addMessage("ww X - Sets war weariness of selected province's civ to X%.");
                                 addMessage("nuke - Player gets one atomic bomb");
                                 addMessage("nuke X - Player gets X atomic bombs");
                                 addMessage("population - Adds default population to selected province.");
                                 addMessage("pop X, population X - Sets population of selected province to X (min 100).");
                                 addMessage("as, assimilate - Starts assimilation in all player-owned provinces.");
                                 addMessage("technology X, tech X - Sets tech level of civ in selected province to X%.");
                                 addMessage("gold X - Sets player's money to X.");
                                 addMessage("money, gold - Adds default gold (60% of DEFAULT_ARMY).");
                                 addMessage("province - Shows detailed info of selected province.");
                                 addMessage("id - Shows ID of selected province and its civ.");
                                 addMessage("reloadprovince X - Reloads and selects province with ID X.");
                                 addMessage("chaos - Toggles Age of Chaos mode.");
                                 return;
                              }

                              CFG.map.getMpSl().setScrollPos(125000, 10);
                              CFG.map.getMpSl().setScrollPos(10, 10);
                              CFG.menus.getKeyboard().setVisibleM(false);
                              CFG.menus.setVisible_InGame_FlagAction(false);
                              CFG.map.getMpSl().startScrollingTheMap();
                              CFG.toastM.addM(CFG.lang.get("Wheee") + "!");
                              addMessage(CFG.lang.get("Wheee") + "!");
                              return;
                           }

                           if (!CFG.menus.getVisible_InGame_FlagAction_Console()) {
                              CFG.menus.setVisible_InGame_FlagAction_Console(true);
                           }

                           CFG.toastM.addM(CFG.lang.get("Hello") + ", welcome to Age of History 2: Definitive Edition");
                           addMessage(CFG.lang.get("Hello") + ", welcome to Age of History 2: Definitive Edition");
                           return;
                        }

                        CFG.PXSX = !CFG.PXSX;
                        CFG.toastM.addM(CFG.lang.get("Partial Sandbox") + ": " + (CFG.PXSX ? CFG.lang.get("Enabled") : CFG.lang.get("Disabled")));
                        addMessage(CFG.lang.get("Partial Sandbox") + ": " + (CFG.PXSX ? CFG.lang.get("Enabled") : CFG.lang.get("Disabled")));
                        return;
                     }

                     if (CFG.menus.getVisible_InGame_FlagAction_Console()) {
                        CFG.menus.setVisible_InGame_FlagAction_Console(false);
                     }

                     CFG.menus.getKeyboard().setVisibleM(false);
                     return;
                  }
               }
            }
         } catch (Exception var39) {
            CFG.exceptionStack(var39);
         }

         IllegalCommand();
      }
   }

   public int getImageWidth(int image) {
      return sUM.sUT.getImageWidth(image);
   }

   public int getImageHeight(int image) {
      return sUM.sUT.getImageHeight(image);
   }

   private static final String cheatMess() {
      return "[" + CFG.lang.get("Cheat") + "] ";
   }

   private static final void IllegalCommand() {
      addMessage("# -- " + CFG.lang.get("UnknownCommand"));
      CFG.toastM.addM("# -- " + CFG.lang.get("UnknownCommand"), CFG.COLOR_NEGATIVE_2);
      addMessage("");
   }

   /* =============== 控制台触发事件 =============== */

   /**
    * 事件相关指令入口，'=' 和 '+' 都当作分隔符（方便没有空格键的输入法）：
    * <ul>
    * <li>{@code ev=事件ID} —— 直接触发该事件</li>
    * <li>{@code ev+s+关键词} —— 按事件名 / 国家名搜索，打印带序号的事件列表</li>
    * <li>{@code ev+c+序号} —— 触发上一次搜索结果中该序号对应的事件</li>
    * </ul>
    */
   private static final void executeEventCommand(String nCommand) {
      String tArgs = nCommand.trim().substring(2).replace('=', ' ').replace('+', ' ').trim();
      String[] tParts = tArgs.length() == 0 ? new String[0] : tArgs.split(" +");
      if (tParts.length == 0) {
         printEventHelp();
         return;
      }

      String tSub = tParts[0].toLowerCase();
      if (tSub.equals("s")) {
         if (tParts.length < 2) {
            printEventHelp();
         } else {
            searchEvents(joinFrom(tParts, 1));
         }

         return;
      }

      if (tSub.equals("c")) {
         if (tParts.length < 2) {
            printEventHelp();
            return;
         }

         try {
            int tNum = Integer.parseInt(tParts[1]);
            if (tNum < 1 || tNum > lEventSearchResult.size()) {
               addMessage("# -- ev c: 1 - " + lEventSearchResult.size());
               IllegalCommand();
            } else {
               triggerEvent(lEventSearchResult.get(tNum - 1).intValue());
            }
         } catch (NumberFormatException var4) {
            IllegalCommand();
         }

         return;
      }

      try {
         triggerEvent(Integer.parseInt(tSub));
      } catch (NumberFormatException var5) {
         printEventHelp();
      }
   }

   /** 按关键词搜索事件并打印带序号的结果，结果同时缓存供 {@code ev c 序号} 使用 */
   private static final void searchEvents(String nKeyword) {
      lEventSearchResult = new ArrayList<>();
      sEventSearchKeyword = nKeyword;
      String tKeyword = nKeyword.toLowerCase();

      for (int i = 0; i < CFG.eventsManager.getEventsSize(); i++) {
         if (getEventSearchText(i).toLowerCase().contains(tKeyword)) {
            lEventSearchResult.add(Integer.valueOf(i));
         }
      }

      int tPrint = Math.min(lEventSearchResult.size(), EVENT_SEARCH_PRINT_LIMIT);
      // 控制台自下往上渲染，倒序输出让列表在屏幕上按序号正序显示
      if (lEventSearchResult.size() > tPrint) {
         addMessage("... +" + (lEventSearchResult.size() - tPrint));
      }

      for (int i = tPrint - 1; i >= 0; i--) {
         int tEventID = lEventSearchResult.get(i).intValue();
         addMessage("[" + (i + 1) + "] " + getEventDisplayName(tEventID) + " (ID: " + tEventID + ")");
      }

      addMessage("# EV SEARCH: " + nKeyword + " -> " + lEventSearchResult.size());
      CFG.toastM.addM("EV: " + nKeyword + " -> " + lEventSearchResult.size());
   }

   /** 触发指定ID的事件：直接弹出原版事件窗口，由玩家在窗口里选择决议 */
   private static final void triggerEvent(int nEventID) {
      if (nEventID < 0 || nEventID >= CFG.eventsManager.getEventsSize()) {
         addMessage("# -- ev: 0 - " + (CFG.eventsManager.getEventsSize() - 1));
         IllegalCommand();
         return;
      }

      String tName = getEventDisplayName(nEventID);
      addMessage(cheatMess() + "Event " + nEventID + ": " + tName);
      addMessage("");
      CFG.toastM.addM(cheatMess() + tName);
      // 收起控制台与键盘，否则事件弹窗会被控制台遮住
      CFG.menus.getKeyboard().setVisibleM(false);
      if (CFG.menus.getVisible_InGame_FlagAction_Console()) {
         CFG.menus.setVisible_InGame_FlagAction_Console(false);
      }

      // 这两行就是原版 Civilization.runNextEvent2() / EventsManager.runMissionPlayer()
      // 给玩家弹事件框的路径
      try {
         Menu_InGame_Event.EVENT_ID = nEventID;
         CFG.menus.rebuildInGame_Event();
      } catch (Exception var4) {
         // 事件窗口构建失败（例如该事件没有任何决议）时，退回原版的入队逻辑
         CFG.exceptionStack(var4);
         CFG.eventsManager.try_RunEvent(nEventID);
      }
   }

   private static final void printEventHelp() {
      if (lEventSearchResult.size() > 0) {
         addMessage("# LAST: " + sEventSearchKeyword + " -> " + lEventSearchResult.size());
      }

      addMessage("ev+c+NUM  : run event NUM of the last search");
      addMessage("ev+s+KEY  : search events by name / civ name");
      addMessage("ev=ID     : run event by ID");
      addMessage("# EV: '=' and '+' work as separators");
   }

   /** 搜索用文本：原始事件名 + 本地化事件名 + 所属国家名（与事件列表的搜索框一致） */
   private static final String getEventSearchText(int nEventID) {
      Event_GameData tEvent = CFG.eventsManager.getEvent(nEventID);
      return tEvent.getEventName() + " " + CFG.lang.get(tEvent.getEventName()) + " " + getEventCivName(nEventID);
   }

   private static final String getEventDisplayName(int nEventID) {
      Event_GameData tEvent = CFG.eventsManager.getEvent(nEventID);
      String tCivName = getEventCivName(nEventID);
      return CFG.lang.get(tEvent.getEventName()) + (tCivName.length() > 0 ? ", " + tCivName : "");
   }

   private static final String getEventCivName(int nEventID) {
      int tCivID = CFG.eventsManager.getEvent(nEventID).getCivID();
      return tCivID > 0 && tCivID < CFG.core.getCivsSize() ? CFG.core.getCiv(tCivID).getCivName() : "";
   }

   private static final String joinFrom(String[] nParts, int nFrom) {
      StringBuilder tBuilder = new StringBuilder();

      for (int i = nFrom; i < nParts.length; i++) {
         if (i > nFrom) {
            tBuilder.append(" ");
         }

         tBuilder.append(nParts[i]);
      }

      return tBuilder.toString();
   }
}

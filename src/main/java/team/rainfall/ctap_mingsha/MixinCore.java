package team.rainfall.ctap_mingsha;


import age.of.civilizations2.jakowski.lukasz.CFG;
import age.of.civilizations2.jakowski.lukasz.GameCalendar;
import age.of.civilizations2.jakowski.lukasz.Images;
import age.of.civilizations2.jakowski.lukasz.Civilization;
import age.of.civilizations2.jakowski.lukasz.Files.FileManager;
import age.of.civilizations2.jakowski.lukasz.Province;
import age.of.civilizations2.jakowski.lukasz.Province_GameData2;
import age.of.civilizations2.jakowski.lukasz.War_GameData;
import age.of.civilizations2.jakowski.lukasz.Core.Core;
import com.badlogic.gdx.files.FileHandle;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.*;
import team.rainfall.finality.luminosity2.annotations.Getter;
import team.rainfall.finality.luminosity2.annotations.Mixin;
import team.rainfall.finality.luminosity2.annotations.Setter;
import team.rainfall.finality.luminosity2.annotations.Shadow;
import team.rainfall.finality.FinalityLogger;
import team.rainfall.mingsha.PackLocator;
import team.rainfall.mingsha.ProvincePack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Mixin(mixinClass = "age.of.civilizations2.jakowski.lukasz.Core.Core")
public class MixinCore {
    @Shadow
    private List<Province> lProvs = null;
    private static boolean missingProvinceWarningLogged;
    private static boolean provincePackErrorLogged;

    /** Load province data from the Mingsha pack, then fall back to legacy files. */
    public final void loadProvince(int provinceId) {
        String base = "map/" + CFG.map.getFileActiveMapPath() + "data/";
        FileHandle pack = PackLocator.find(base + "provinces.pack");
        if (pack != null) {
            try {
                byte[] bytes = ProvincePack.read(pack, provinceId);
                if (bytes != null) {
                    lProvs.add(new Province(provinceId, (Province_GameData2) CFG.deserialize(bytes)));
                    return;
                }
            } catch (Exception ex) {
                // A corrupt or incompatible pack must not make old maps unloadable.
                if (!provincePackErrorLogged) {
                    provincePackErrorLogged = true;
                    FinalityLogger.error("[Mingsha] Province pack read failed; falling back to files", ex);
                    if (CFG.LOGs) {
                        CFG.exceptionStack(ex);
                    }
                }
            }
        }

        try {
            FileHandle file = FileManager.loadFile(base + "provinces/" + provinceId);
            lProvs.add(new Province(provinceId, (Province_GameData2) CFG.deserialize(file.readBytes())));
            return;
        } catch (Exception fileError) {
            // Keep the original game's recovery path for maps that retain update/<id>.
            try {
                FileHandle update = FileManager.loadFile("map/" + CFG.map.getFileActiveMapPath() + "update/" + provinceId);
                String[] parts = update.readString().split(";");
                if (parts.length >= 2) {
                    String[] xs = parts[0].split(",");
                    String[] ys = parts[1].split(",");
                    if (xs.length != ys.length || xs.length == 0) {
                        throw new IOException("Invalid update province point data");
                    }
                    List<Short> pointsX = new ArrayList<>();
                    List<Short> pointsY = new ArrayList<>();
                    for (int i = 0; i < xs.length; i++) {
                        pointsX.add((short) Integer.parseInt(xs[i].trim()));
                        pointsY.add((short) Integer.parseInt(ys[i].trim()));
                    }
                    lProvs.add(new Province(provinceId, new Province_GameData2(-1, pointsX, pointsY, null,
                            new ArrayList<>(), new ArrayList<>())));
                    return;
                }
            } catch (Exception updateError) {
                if (CFG.LOGs && !missingProvinceWarningLogged) {
                    missingProvinceWarningLogged = true;
                    CFG.exceptionStack(updateError);
                }
            }
            if (CFG.LOGs && !missingProvinceWarningLogged) {
                missingProvinceWarningLogged = true;
                CFG.exceptionStack(fileError);
            }
        }
    }
    @Shadow
    private List<War_GameData> lWars = null;
    @Getter(fieldName = "lWars")
    public static List<War_GameData> get_lWars(Core core){
        return null;
    }
    @Setter(fieldName = "iWarsSize")
    public static void set_iWarsSize(Core core,int i){

    }
    @Getter(fieldName = "iWarsSize")
    public static int get_iWarsSize(Core core){
        return 0;
    }
    @Shadow
    private List<Civilization> lCivs = null;
    @Getter(fieldName = "lCivs")
    public static List<Civilization> get_lCivs(Core core){
        return null;
    }
    @Shadow
    private int iCivsSize = 0;
    @Setter(fieldName = "iCivsSize")
    public static void set_iCivsSize(Core core,int i){

    }
    @Getter(fieldName = "iCivsSize")
    public static int get_iCivsSize(Core core){
        return 0;
    }
    public final ME_Hover_v2 getHover_LeaderOfCiv(int nCivID) {
        try {
            List<MEHover_2E> nElements = new ArrayList();
            List<ME_Hover_2Type> nData = new ArrayList();
            if (CFG.core.getCiv(nCivID).civGD.leaderData != null) {
                nData.add(new ME_Hover_2Type_Text_Big(CFG.core.getCiv(nCivID).civGD.leaderData.getName(), CFG.COLOR_TEXT_NUM_OF_PROVINCES));
                nData.add(new ME_Hover_2Type_Flag_Big(nCivID, CFG.PADD, 0));
                nData.add(new ME_Hover_2Type_Religion_Big(CFG.core.getCiv(nCivID).getReligionID(), CFG.PADD, 0));
                nElements.add(new MEHover_2E(nData));
                nData.clear();
                nData.add(new ME_Hover_2Type_Space());
                nElements.add(new MEHover_2E(nData));
                nData.clear();
                nData.add(new ME_Hover_2Type_Text(CFG.lang.get("Born") + ": "));
                String s1 = CFG.core.getCiv(nCivID).civGD.leaderData.getDay() + " " + GameCalendar.getMonthName(CFG.core.getCiv(nCivID).civGD.leaderData.getMonth()) + " " + CFG.gameAges.getYear(CFG.core.getCiv(nCivID).civGD.leaderData.getYear());
                if(CFG.settingsGD.LANG_TAG.contains("cn")) s1 = DateUtil.getCurrDate_Leader(nCivID);
                nData.add(new ME_Hover_2Type_Text(
                        s1
                        , CFG.COLOR_HOVER_TITLE));
                nData.add(new ME_Hover_2Type_Text(" - " + CFG.gameAges.getAge(CFG.gameAges.getAgeOfYear(CFG.core.getCiv(nCivID).civGD.leaderData.getYear())).getName(), CFG.COLOR_NEUTRAL));
                nData.add(new ME_Hover_2Type_Image(Images.time, CFG.PADD, 0));
                nElements.add(new MEHover_2E(nData));
                nData.clear();
                if (CFG.core.getCiv(nCivID).civGD.leaderData.fModifier_AttackBonus != 0.0F) {
                    nData.add(new ME_Hover_2Type_Text(CFG.lang.get("AttackBonus") + ": "));
                    nData.add(new ME_Hover_2Type_Text((CFG.core.getCiv(nCivID).civGD.leaderData.fModifier_AttackBonus > 0.0F ? "+" : "") + (int)(CFG.core.getCiv(nCivID).civGD.leaderData.fModifier_AttackBonus * 100.0F) + "%", CFG.core.getCiv(nCivID).civGD.leaderData.fModifier_AttackBonus > 0.0F ? CFG.COLOR_POSITIVE : CFG.COLOR_NEGATIVE_2));
                    nData.add(new ME_Hover_2Type_Image(Images.diploRivals, CFG.PADD, 0));
                    nElements.add(new MEHover_2E(nData));
                    nData.clear();
                }

                if (CFG.core.getCiv(nCivID).civGD.leaderData.fModifier_DefenseBonus != 0.0F) {
                    nData.add(new ME_Hover_2Type_Text(CFG.lang.get("DefenseBonus") + ": "));
                    nData.add(new ME_Hover_2Type_Text((CFG.core.getCiv(nCivID).civGD.leaderData.fModifier_DefenseBonus > 0.0F ? "+" : "") + (int)(CFG.core.getCiv(nCivID).civGD.leaderData.fModifier_DefenseBonus * 100.0F) + "%", CFG.core.getCiv(nCivID).civGD.leaderData.fModifier_DefenseBonus > 0.0F ? CFG.COLOR_POSITIVE : CFG.COLOR_NEGATIVE_2));
                    nData.add(new ME_Hover_2Type_Image(Images.diploRivals, CFG.PADD, 0));
                    nElements.add(new MEHover_2E(nData));
                    nData.clear();
                }

                if (CFG.core.getCiv(nCivID).civGD.leaderData.fModifier_PopGrowth != 0.0F) {
                    nData.add(new ME_Hover_2Type_Text(CFG.lang.get("PopulationGrowthModifier") + ": "));
                    nData.add(new ME_Hover_2Type_Text((CFG.core.getCiv(nCivID).civGD.leaderData.fModifier_PopGrowth > 0.0F ? "+" : "") + (int)(CFG.core.getCiv(nCivID).civGD.leaderData.fModifier_PopGrowth * 100.0F) + "%", CFG.core.getCiv(nCivID).civGD.leaderData.fModifier_PopGrowth > 0.0F ? CFG.COLOR_POSITIVE : CFG.COLOR_NEGATIVE_2));
                    nData.add(new ME_Hover_2Type_Image(Images.popGrowth, CFG.PADD, 0));
                    nElements.add(new MEHover_2E(nData));
                    nData.clear();
                }

                if (CFG.core.getCiv(nCivID).civGD.leaderData.fModifier_EconomyGrowth != 0.0F) {
                    nData.add(new ME_Hover_2Type_Text(CFG.lang.get("EconomyGrowthModifier") + ": "));
                    nData.add(new ME_Hover_2Type_Text((CFG.core.getCiv(nCivID).civGD.leaderData.fModifier_EconomyGrowth > 0.0F ? "+" : "") + (int)(CFG.core.getCiv(nCivID).civGD.leaderData.fModifier_EconomyGrowth * 100.0F) + "%", CFG.core.getCiv(nCivID).civGD.leaderData.fModifier_EconomyGrowth > 0.0F ? CFG.COLOR_POSITIVE : CFG.COLOR_NEGATIVE_2));
                    nData.add(new ME_Hover_2Type_Image(Images.economy, CFG.PADD, 0));
                    nElements.add(new MEHover_2E(nData));
                    nData.clear();
                }

                if (CFG.core.getCiv(nCivID).civGD.leaderData.fModifier_IncomeTaxation != 0.0F) {
                    nData.add(new ME_Hover_2Type_Text(CFG.lang.get("IncomeTaxation") + ": "));
                    nData.add(new ME_Hover_2Type_Text((CFG.core.getCiv(nCivID).civGD.leaderData.fModifier_IncomeTaxation > 0.0F ? "+" : "") + (int)(CFG.core.getCiv(nCivID).civGD.leaderData.fModifier_IncomeTaxation * 100.0F) + "%", CFG.core.getCiv(nCivID).civGD.leaderData.fModifier_IncomeTaxation > 0.0F ? CFG.COLOR_POSITIVE : CFG.COLOR_NEGATIVE_2));
                    nData.add(new ME_Hover_2Type_Image(Images.topGold(), CFG.PADD, 0));
                    nElements.add(new MEHover_2E(nData));
                    nData.clear();
                }

                if (CFG.core.getCiv(nCivID).civGD.leaderData.fModifier_IncomeProduction != 0.0F) {
                    nData.add(new ME_Hover_2Type_Text(CFG.lang.get("IncomeProduction") + ": "));
                    nData.add(new ME_Hover_2Type_Text((CFG.core.getCiv(nCivID).civGD.leaderData.fModifier_IncomeProduction > 0.0F ? "+" : "") + (int)(CFG.core.getCiv(nCivID).civGD.leaderData.fModifier_IncomeProduction * 100.0F) + "%", CFG.core.getCiv(nCivID).civGD.leaderData.fModifier_IncomeProduction > 0.0F ? CFG.COLOR_POSITIVE : CFG.COLOR_NEGATIVE_2));
                    nData.add(new ME_Hover_2Type_Image(Images.development, CFG.PADD, 0));
                    nElements.add(new MEHover_2E(nData));
                    nData.clear();
                }

                if (CFG.core.getCiv(nCivID).civGD.leaderData.fModifier_Administration != 0.0F) {
                    nData.add(new ME_Hover_2Type_Text(CFG.lang.get("Administration") + ": "));
                    nData.add(new ME_Hover_2Type_Text((CFG.core.getCiv(nCivID).civGD.leaderData.fModifier_Administration > 0.0F ? "+" : "") + (int)(CFG.core.getCiv(nCivID).civGD.leaderData.fModifier_Administration * 100.0F) + "%", CFG.core.getCiv(nCivID).civGD.leaderData.fModifier_Administration < 0.0F ? CFG.COLOR_POSITIVE : CFG.COLOR_NEGATIVE_2));
                    nData.add(new ME_Hover_2Type_Image(Images.administration, CFG.PADD, 0));
                    nElements.add(new MEHover_2E(nData));
                    nData.clear();
                }

                if (CFG.core.getCiv(nCivID).civGD.leaderData.fModifier_MilitaryUpkeep != 0.0F) {
                    nData.add(new ME_Hover_2Type_Text(CFG.lang.get("MilitaryUpkeep") + ": "));
                    nData.add(new ME_Hover_2Type_Text((CFG.core.getCiv(nCivID).civGD.leaderData.fModifier_MilitaryUpkeep > 0.0F ? "+" : "") + (int)(CFG.core.getCiv(nCivID).civGD.leaderData.fModifier_MilitaryUpkeep * 100.0F) + "%", CFG.core.getCiv(nCivID).civGD.leaderData.fModifier_MilitaryUpkeep < 0.0F ? CFG.COLOR_POSITIVE : CFG.COLOR_NEGATIVE_2));
                    nData.add(new ME_Hover_2Type_Image(Images.diploArmy, CFG.PADD, 0));
                    nElements.add(new MEHover_2E(nData));
                    nData.clear();
                }

                if (CFG.core.getCiv(nCivID).civGD.leaderData.fModifier_Research != 0.0F) {
                    nData.add(new ME_Hover_2Type_Text(CFG.lang.get("Research") + ": "));
                    nData.add(new ME_Hover_2Type_Text((CFG.core.getCiv(nCivID).civGD.leaderData.fModifier_Research > 0.0F ? "+" : "") + (int)(CFG.core.getCiv(nCivID).civGD.leaderData.fModifier_Research * 100.0F) + "%", CFG.core.getCiv(nCivID).civGD.leaderData.fModifier_Research > 0.0F ? CFG.COLOR_POSITIVE : CFG.COLOR_NEGATIVE_2));
                    nData.add(new ME_Hover_2Type_Image(Images.research, CFG.PADD, 0));
                    nElements.add(new MEHover_2E(nData));
                    nData.clear();
                }

                if (CFG.core.getCiv(nCivID).civGD.leaderData.fModifier_MovementPoints != 0.0F) {
                    nData.add(new ME_Hover_2Type_Text(CFG.lang.get("MovementPoints") + ": "));
                    nData.add(new ME_Hover_2Type_Text((CFG.core.getCiv(nCivID).civGD.leaderData.fModifier_MovementPoints > 0.0F ? "+" : "") + (int)(CFG.core.getCiv(nCivID).civGD.leaderData.fModifier_MovementPoints * 100.0F) + "%", CFG.core.getCiv(nCivID).civGD.leaderData.fModifier_MovementPoints > 0.0F ? CFG.COLOR_POSITIVE : CFG.COLOR_NEGATIVE_2));
                    nData.add(new ME_Hover_2Type_Image(Images.topMovementPoints, CFG.PADD, 0));
                    nElements.add(new MEHover_2E(nData));
                    nData.clear();
                }

                if (CFG.core.getCiv(nCivID).civGD.leaderData.getWiki().length() > 0) {
                    nData.add(new ME_Hover_2Type_Space());
                    nElements.add(new MEHover_2E(nData));
                    nData.clear();
                    if (CFG.core.getCiv(nCivID).civGD.leaderData.getWiki().contains("_")) {
                        nData.add(new ME_Hover_2Type_Flag(nCivID));
                        nData.add(new ME_Hover_2Type_Text(CFG.lang.get("Wiki") + ": "));
                        nData.add(new ME_Hover_2Type_Text("" + CFG.core.getCiv(nCivID).civGD.leaderData.getWiki(), CFG.COLOR_HOVER_TITLE));
                        nData.add(new ME_Hover_2Type_Image(Images.wikipedia, CFG.PADD, 0));
                        nElements.add(new MEHover_2E(nData));
                        nData.clear();
                    } else {
                        nData.add(new ME_Hover_2Type_TextDesc("" + CFG.lang.get(CFG.core.getCiv(nCivID).civGD.leaderData.getWiki())));
                        nElements.add(new MEHover_2E(nData));
                        nData.clear();
                    }
                }
            } else {
                nData.add(new ME_Hover_2Type_Flag(nCivID));
                nData.add(new ME_Hover_2Type_Text(CFG.core.getCiv(nCivID).getCivName(), CFG.COLOR_HOVER_TITLE));
                nData.add(new ME_Hover_2Type_Religion_Big(CFG.core.getCiv(nCivID).getReligionID(), CFG.PADD, 0));
                nElements.add(new MEHover_2E(nData));
                nData.clear();
            }

            return new ME_Hover_v2(nElements);
        } catch (Exception var4) {
            return null;
        }
    }
}

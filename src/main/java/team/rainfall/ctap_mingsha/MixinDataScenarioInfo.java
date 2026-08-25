package team.rainfall.ctap_mingsha;

import team.rainfall.finality.luminosity2.annotations.Mixin;

/** Preserves compatibility with scenario metadata produced by the legacy mod. */
@Mixin(mixinClass = "age.of.civilizations2.jakowski.lukasz.CFG$Data_Scenario_Info")
public class MixinDataScenarioInfo {
    public int iEventSize;
}

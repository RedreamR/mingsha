package team.rainfall.mingsha.ns;

import age.of.civilizations2.jakowski.lukasz.CFG;
import age.of.civilizations2.jakowski.lukasz.CivBonus_GameData;
import age.of.civilizations2.jakowski.lukasz.Save.Save_Civ_GameData;
import team.rainfall.finality.luminosity2.annotations.Mixin;
import team.rainfall.finality.luminosity2.annotations.Shadow;

/**
 * Ages the civilization's timed national spirits once per turn.
 * <p>
 * {@code updateBonuses()} is the one place the game already visits every turn to
 * expire temporary bonuses ({@code NewTurn} calls it for every civilization), which
 * makes it the natural home for the spirit countdown. Luminosity replaces whole
 * methods, so the vanilla body is reproduced verbatim with the NS tick appended.
 */
@Mixin(mixinClass = "age.of.civilizations2.jakowski.lukasz.Civilization")
public class MixinCivilization {

    @Shadow
    public Save_Civ_GameData civGD;

    public final void updateBonuses() {
        for (int i = 0; i < this.civGD.bonusesCiv.size(); i++) {
            this.civGD.bonusesCiv.get(i).iTurnsLeft--;
            if (this.civGD.bonusesCiv.get(i).iTurnsLeft <= 0) {
                this.applyBonusChangesExpired(this.civGD.bonusesCiv.get(i));
                this.civGD.bonusesCiv.remove(i--);
            }
        }

        try {
            NSStore.tick(this.getCivId());
        } catch (Exception e) {
            CFG.exceptionStack(e);
        }
    }

    @Shadow
    private final void applyBonusChangesExpired(CivBonus_GameData nBonus) {
    }

    @Shadow
    public final int getCivId() {
        return 0;
    }
}

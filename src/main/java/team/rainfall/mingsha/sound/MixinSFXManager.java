package team.rainfall.mingsha.sound;

import com.badlogic.gdx.audio.Sound;
import team.rainfall.finality.luminosity2.annotations.Mixin;
import team.rainfall.finality.luminosity2.annotations.Shadow;

import java.util.List;

@Mixin(mixinClass = "age.of.civilizations2.jakowski.lukasz.SFXManager")
public class MixinSFXManager {
    @Shadow
    private List<Sound> lSounds = null;
    @Shadow
    private float soundsVolume = 0.55F;
    @Shadow
    private float masterVolume = 0.0F;

    public final void playSound(int id, float fPercOfVolume) {
        this.lSounds.get(id).stop();
        this.lSounds.get(id).play(this.soundsVolume * this.masterVolume * fPercOfVolume);
        UnitVoiceManager.onSfxPlayed(id, fPercOfVolume);
    }
}

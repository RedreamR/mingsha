package team.rainfall.ctap_mingsha;

import age.of.civilizations2.jakowski.lukasz.CFG;
import team.rainfall.finality.luminosity2.annotations.Mixin;
import team.rainfall.mingsha.config.MingshaConfig;

@Mixin(mixinClass = "age.of.civilizations2.jakowski.lukasz.TouchManager")
public class MixinTouchManager {
    private boolean enableScaling = false;
    public final void updateEnableScaling() {
        if (MingshaConfig.isAlwaysEnableScaling()) {
            this.enableScaling = true;
            return;
        }
        try {
            this.enableScaling = !CFG.menus.getIn_MainMenu()
                    && !CFG.menus.getInFlagPainter()
                    && !CFG.menus.getIn_AboutMenu()
                    && !CFG.menus.getIn_SKMenu()
                    && !CFG.menus.getIn_MMMenu()
                    && !CFG.menus.getIn_FBMenu()
                    && !CFG.menus.getIn_NVMenu()
                    && !CFG.menus.getIn_InitMenu()
                    && !CFG.menus.getInLoadMap()
                    && !CFG.menus.getInLoadSave();
        } catch (Exception e) {
            this.enableScaling = true;
        }
    }

    /**
     * A modal map panel can disable panning while still displaying the map.
     * Keep pinch zoom available when the module explicitly enables scaling.
     */
    public final void actionMove(int nPosX, int nPosY, int nPosX2, int nPosY2) {
        boolean forceScaling = MingshaConfig.isAlwaysEnableScaling();
        if ((this.enableScaling || forceScaling)
                && (forceScaling || !CFG.map.getMpC().getDisableMovingMap())) {
            if (CFG.map.getMpS().getStartScalePosY() <= 0) {
                CFG.map.getMpS().startScaleTheMap(nPosX, nPosX2, nPosY, nPosY2);
            } else {
                CFG.map.getMpS().scaleTheMap(nPosX, nPosX2, nPosY, nPosY2);
            }
        }
    }
}

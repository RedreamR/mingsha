package team.rainfall.ctap_mingsha;

import age.of.civilizations2.jakowski.lukasz.CFG;
import age.of.civilizations2.jakowski.lukasz.Files.FileManager;
import age.of.civilizations2.jakowski.lukasz.GameValues.GameValues;
import age.of.civilizations2.jakowski.lukasz.Image;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import team.rainfall.finality.FinalityLogger;
import team.rainfall.finality.luminosity2.annotations.Mixin;
import team.rainfall.finality.luminosity2.annotations.Shadow;
import team.rainfall.mingsha.PackLocator;
import team.rainfall.mingsha.ProvincePack;

/** Loads Mingsha's indexed CIM texture pack before the original per-file path. */
@Mixin(mixinClass = "age.of.civilizations2.jakowski.lukasz.Province")
public class MixinProvince {
    @Shadow
    private int iProvinceID;
    @Shadow
    private int iContinentID;
    @Shadow
    private Image provBG;
    private static boolean texturePackErrorLogged;

    public final void loadProvinceBG() {
        if ((!GameValues.gvInGame.LOAD_SEA_PROVINCES_IMAGES || !CFG.getIsDesktop()) && getSeaProv()) {
            this.provBG = age.of.civilizations2.jakowski.lukasz.IMGManager.loadImage("UI/pixEmpty.png");
            return;
        }

        int scale = this.iContinentID == CFG.map.getMapContinents().getOceanContinentID()
                ? 1 : CFG.map.getMpB().getMapScale_PreExtra();
        String base = "map/" + CFG.map.getFileActiveMapPath() + "data/scales/provinces/";
        FileHandle texturePack = PackLocator.find(base + "provinces.pack");
        if (texturePack != null) {
            try {
                byte[] bytes = ProvincePack.readTexture(texturePack, scale, this.iProvinceID);
                if (bytes != null) {
                    Pixmap pixmap = ProvincePack.readCim(bytes);
                    try {
                        this.provBG = new Image(new Texture(pixmap), TextureFilter.Nearest, TextureWrap.ClampToEdge);
                    } finally {
                        pixmap.dispose();
                    }
                    return;
                }
            } catch (Exception ex) {
                if (!texturePackErrorLogged) {
                    texturePackErrorLogged = true;
                    FinalityLogger.error("[Mingsha] Texture pack read failed; falling back to files", ex);
                }
            }
        }

        try {
            Pixmap pixmap = com.badlogic.gdx.graphics.PixmapIO.readCIM(
                    FileManager.loadFile(base + scale + "/" + this.iProvinceID));
            try {
                this.provBG = new Image(new Texture(pixmap), TextureFilter.Nearest, TextureWrap.ClampToEdge);
            } finally {
                pixmap.dispose();
            }
        } catch (Exception ignored) {
            this.provBG = age.of.civilizations2.jakowski.lukasz.IMGManager.loadImage("UI/pixEmpty.png");
        }
    }

    @Shadow
    public final boolean getSeaProv() {
        return false;
    }
}

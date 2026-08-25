package team.rainfall.ctap_mingsha;

import age.of.civilizations2.jakowski.lukasz.Files.FileManager;
import age.of.civilizations2.jakowski.lukasz.GameValues.GameValues;
import age.of.civilizations2.jakowski.lukasz.IMGManager;
import age.of.civilizations2.jakowski.lukasz.Images;
import age.of.civilizations2.jakowski.lukasz.Renderer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.GdxRuntimeException;
import team.rainfall.finality.luminosity2.annotations.Mixin;
import team.rainfall.mingsha.HOI4Loading;
import team.rainfall.mingsha.config.MingshaConfig;

import static age.of.civilizations2.jakowski.lukasz.CFG.*;
@Mixin(mixinClass = "age.of.civilizations2.jakowski.lukasz.CFG")
public class MixinCFG {

    public static void drLOA(SpriteBatch oSB, int nPosX, int nPosY, int nWidth, int nHeight, float nProgress) {
        drLOA(oSB, nPosX, nPosY, nWidth, nHeight, nProgress, "");
    }

    public static void drLOA(SpriteBatch oSB, int nPosX, int nPosY, int nWidth, int nHeight, float nProgress, String extraText) {
        float clamped = Math.min(1.0F, Math.max(0.0F, nProgress));
        if (MingshaConfig.isHoi4LoadingEnabled()) {
            HOI4Loading.draw(oSB, clamped, sLoading + " " + (int)(clamped * 100.0F) + "%" + extraText);
        } else {
            drLOAVanilla(oSB, nPosX, nPosY, nWidth, nHeight, clamped, extraText);
        }
    }

    /** 原版 AoH2DE 加载条，作为 HOI4 风格加载动画被关闭时的回退。 */
    private static void drLOAVanilla(SpriteBatch oSB, int nPosX, int nPosY, int nWidth, int nHeight, float nProgress, String extraText) {
        if (nProgress > 1.0F) {
            nProgress = 1.0F;
        } else if (nProgress < 0.0F) {
            nProgress = 0.0F;
        }

        if (System.currentTimeMillis() - 2500L > loaTM) {
            try {
                sLOATXT = lang.getLOA("L" + oR.nextInt(lang.iLNOT)) + "..";
                loaTM = System.currentTimeMillis();
                glyphLay.setText(fontMain.get(FONT_BOLD), sLOATXT);
                iLOADW = (int) glyphLay.width;
                iLOAH = (int) glyphLay.height;
            } catch (Exception var9) {
                exceptionStack(var9);
            }
        }

        if (PRT == 0L) {
            PRT = System.currentTimeMillis();
        }

        if (System.currentTimeMillis() < PRT + 6500L) {
            int tH = TEXT_HEIGHT_DEFAULT * 3 + PADD * 8;
            int tY = GAMEHEIGHT / 2 - tH / 2;
            oSB.setColor(new Color(0.0F, 0.0F, 0.0F, 0.2F));
            IMGManager.getIMG(Images.pix255).draw(oSB, 0, tY, GAMEWIDTH, tH);
            oSB.setColor(new Color(0.0F, 0.0F, 0.0F, 0.2F));
            IMGManager.getIMG(Images.line32Off1).draw(oSB, 0, tY, GAMEWIDTH, tH);
            oSB.setColor(new Color(0.0F, 0.0F, 0.0F, 0.6F));
            IMGManager.getIMG(Images.gradient).draw(oSB, 0, tY, GAMEWIDTH, PADD);
            IMGManager.getIMG(Images.gradient).draw(oSB, 0, tY + tH - PADD, GAMEWIDTH, PADD, false, true);
            oSB.setColor(new Color(COLOR_CREATE_NEW_GAME_BOX_PLAYERS.r, COLOR_CREATE_NEW_GAME_BOX_PLAYERS.g, COLOR_CREATE_NEW_GAME_BOX_PLAYERS.b, 0.85F));
            IMGManager.getIMG(Images.pix255).draw(oSB, 0, tY + 1, GAMEWIDTH, 1);
            IMGManager.getIMG(Images.pix255).draw(oSB, 0, tY + tH - 2, GAMEWIDTH, 1);
            oSB.setColor(0.0F, 0.0F, 0.0F, 0.325F);
            IMGManager.getIMG(Images.pix255).draw(oSB, 0, tY, GAMEWIDTH, 1);
            IMGManager.getIMG(Images.pix255).draw(oSB, 0, tY + tH - 1, GAMEWIDTH, 1);
            oSB.setColor(Color.WHITE);
            drawTextDefault(oSB, gLG(), GAMEWIDTH / 2 - iJGW / 2, tY + PADD * 2 + PADD / 2, COLOR_HOVER_TITLE);
            drawTextDefault(oSB, "presents", GAMEWIDTH / 2 - iJGPW / 2, tY + TEXT_HEIGHT_DEFAULT + PADD * 3 + PADD / 2, COLOR_HOVER_TITLE);
            drawTextDefault(
                oSB,
                "Age of History 2: Definitive Edition",
                GAMEWIDTH / 2 - iDXW / 2,
                tY + TEXT_HEIGHT_DEFAULT * 2 + PADD * 5 + PADD / 2,
                COLOR_TEXT_NUM_OF_PROVINCES
            );
            oSB.setColor(Color.WHITE);
        }

        oSB.setColor(new Color(0.0F, 0.0F, 0.0F, 0.2F));
        IMGManager.getIMG(Images.gradientXY).draw(oSB, nPosX, nPosY - PADD, nWidth, PADD);
        IMGManager.getIMG(Images.gradientXY).draw(oSB, nPosX, nPosY + nHeight, nWidth, PADD, false, true);
        oSB.setColor(new Color(0.0F, 0.0F, 0.0F, 0.35F));
        Renderer.drawBox2(oSB, Images.statsRectBG, nPosX, nPosY, nWidth, nHeight, 1.0F);
        oSB.setColor(new Color(0.0F, 0.0F, 0.0F, 0.35F));
        Renderer.drawBox2(oSB, Images.statsRectBG, nPosX + 3, nPosY + 3, (int)((float)(nWidth - 6) * nProgress), nHeight - 6, 1.0F);
        oSB.setColor(new Color(COLOR_CREATE_NEW_GAME_BOX_PLAYERS.r, COLOR_CREATE_NEW_GAME_BOX_PLAYERS.g, COLOR_CREATE_NEW_GAME_BOX_PLAYERS.b, 0.85F));
        Renderer.drawBox2(oSB, Images.statsRectBGBorder, nPosX + 1, nPosY + 1, nWidth - 2, nHeight - 2, 1.0F);
        oSB.setColor(Color.WHITE);
        Renderer.drawTextWithShadow(
            oSB,
            FONT_BOLD,
            sLOATXT,
            nPosX + nWidth / 2 - iLOADW / 2,
            nPosY + (nHeight - iLOAH) / 2,
            new Color(COLOR_HOVER_TITLE.r, COLOR_HOVER_TITLE.g, COLOR_HOVER_TITLE.b, 1.0F)
        );
        Renderer.drawTextWithShadow(
            oSB,
            FONT_REGULAR_SMALL,
            sLoading + " " + (int)(nProgress * 100.0F) + "%" + extraText,
            nPosX + PADD * 2,
            nPosY - PADD - TEXT_HEIGHT_DEFAULT_SMALL,
            new Color(COLOR_HOVER_TITLE.r, COLOR_HOVER_TITLE.g, COLOR_HOVER_TITLE.b, 1.0F)
        );
        oSB.setColor(new Color(1.0F, 1.0F, 1.0F, 0.35F));
        IMGManager.getIMG(Images.gameLogo)
            .draw2O(
                oSB,
                nPosX + nWidth - PADD * 2 - IMGManager.getIMG(Images.gameLogo).getWidth(),
                nPosY - PADD * 2 - IMGManager.getIMG(Images.gameLogo).getHeight() * 2,
                IMGManager.getIMG(Images.gameLogo).getWidth(),
                IMGManager.getIMG(Images.gameLogo).getHeight()
            );
        oSB.setColor(Color.WHITE);
        IMGManager.getIMG(Images.gameLogo)
            .draw2O(
                oSB,
                nPosX + nWidth - PADD * 2 - IMGManager.getIMG(Images.gameLogo).getWidth(),
                nPosY - PADD * 2 - IMGManager.getIMG(Images.gameLogo).getHeight() * 2,
                (int)((float)IMGManager.getIMG(Images.gameLogo).getWidth() * nProgress),
                IMGManager.getIMG(Images.gameLogo).getHeight()
            );
        oSB.setColor(Color.WHITE);
    }

    public static final void drawVersionLB(SpriteBatch oSB, int iTranslateX) {
        Renderer.drawText(oSB, FONT_REGULAR_SMALL, "Mingsha AoH2DE by Team Rainfall", PADD + iTranslateX, GAMEHEIGHT - PADD * 2 - TEXT_HEIGHT_DEFAULT_SMALL * 2, new Color(1.0F, 1.0F, 1.0F, 0.25F));
        Renderer.drawText(oSB, FONT_REGULAR_SMALL, sVERSION + ": " + "2.01 Definitive Edition", PADD + iTranslateX, GAMEHEIGHT - PADD - TEXT_HEIGHT_DEFAULT_SMALL, new Color(1.0F, 1.0F, 1.0F, 0.25F));
    }
    public static final void drawVersion_LEFT_BOT(SpriteBatch oSB, int iTranslateX) {
        Renderer.drawText(oSB, FONT_REGULAR_SMALL, "Mingsha AoH2DE by Team Rainfall", PADD + iTranslateX, GAMEHEIGHT - PADD * 2 - TEXT_HEIGHT_DEFAULT_SMALL * 2, new Color(1.0F, 1.0F, 1.0F, 0.25F));
        Renderer.drawText(oSB, FONT_REGULAR_SMALL, sVERSION + ": " + "2.01 Definitive Edition", PADD + iTranslateX, GAMEHEIGHT - PADD - TEXT_HEIGHT_DEFAULT_SMALL, new Color(1.0F, 1.0F, 1.0F, 0.25F));
    }
    public static final float changeAnimationPos(int animationStepID, float animationChangeViewPos, boolean backAnimation, int nWidth) {
        final int TOTAL_FRAMES = 20;
        int step = Math.min(animationStepID, TOTAL_FRAMES);
        float progress = (float) step / TOTAL_FRAMES;
        float easedProgress = 1.0f - (float) Math.pow(1.0f - progress, 3);
        float targetOffset = -nWidth * (backAnimation ? -1 : 1);
        if (step == TOTAL_FRAMES) {
            return targetOffset;
        }
        return targetOffset * easedProgress;
    }
    public static final void loadFont(String sFont, String charset, int fontSize) {
        float texSize = charset.getBytes().length;
        int texSize2 = (int) (texSize * ((float) 2 / 3) + 1024);
        if (texSize2 < 4096) {
            texSize2 = 4096;
        }
        FreeTypeFontGenerator.setMaxTextureSize(texSize2);

        FreeTypeFontGenerator generator = null;
        if (fontSize < 0) {
            fontSize = (int)((float) GameValues.DEFAULT_FONT_SIZE * GUI_SCALE);
        }

        try {
            generator = new FreeTypeFontGenerator(FileManager.loadFile("game/fonts/" + sFont));
        } catch (Exception var5) {
            generator = new FreeTypeFontGenerator(FileManager.loadFile("game/fonts/rbold.ttf"));
        }

        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();

        params.characters = charset;
        params.incremental = true;
        params.size = Math.max(fontSize, 6);
        params.color = Color.WHITE;
        params.minFilter = Texture.TextureFilter.Linear;
        params.magFilter = Texture.TextureFilter.Linear;
        fontMain.add(generator.generateFont(params));
        if (fontMain.size() == 1) {
            glyphLay.setText(fontMain.get(0), "AyӏdZOP38901ERLj");
            TEXT_HEIGHT_DEFAULT = (int)glyphLay.height;
        }
        fontMain.get(fontMain.size() - 1).getData().markupEnabled = true;
        settingsGD.updateCitiesFontScale();
    }

    public static final void loadFontArmy() {
        if (fontArmy != null) {
            fontArmy.dispose();
            fontArmy = null;
        }

        String sFont = lang.get("fontArmy");
        if (sFont.equals("fontArmy")) {
            sFont = "rbold.ttf";
        }

        FreeTypeFontGenerator genaratorArmy = null;

        try {
            genaratorArmy = new FreeTypeFontGenerator(FileManager.loadFile("game/fonts/" + sFont));
        } catch (GdxRuntimeException var3) {
            genaratorArmy = new FreeTypeFontGenerator(FileManager.loadFile("game/fonts/rbold.ttf"));
        }

        FreeTypeFontGenerator.FreeTypeFontParameter paramsArmy = new FreeTypeFontGenerator.FreeTypeFontParameter();
        paramsArmy.size = Math.max(settingsGD.FONT_ARMY_SIZEX, 6);
        paramsArmy.color = Color.WHITE;
        paramsArmy.minFilter = Texture.TextureFilter.Linear;
        paramsArmy.magFilter = Texture.TextureFilter.Linear;
        paramsArmy.characters = "0123456789+-.,%?!ABCDEFGHIJKLMNOPQRSTUVWXYZ百千万亿";
        fontArmy = genaratorArmy.generateFont(paramsArmy);
        genaratorArmy.dispose();
        glyphLay.setText(fontArmy, "-+1234567890");
        ARMY_HEIGHT = (int)glyphLay.height;
    }

    public static final void loadFontBorder() {
        if (fontBorder != null) {
            fontBorder.dispose();
            fontBorder = null;
        }

        String sFont = lang.get("fontCivNames");
        if (sFont.equals("font2")) {
            sFont = "rbold.ttf";
        }

        FreeTypeFontGenerator genarator = null;

        try {
            genarator = new FreeTypeFontGenerator(FileManager.loadFile("game/fonts/" + sFont));
        } catch (GdxRuntimeException var3) {
            genarator = new FreeTypeFontGenerator(FileManager.loadFile("game/fonts/rbold.ttf"));
        }

        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.incremental = true;
        params.characters = lang.get("charset");
        params.size = settingsGD.FONT_BORDER_SIZEX;
        params.color = new Color(settingsGD.civNamesFontColor.getR(), settingsGD.civNamesFontColor.getG(), settingsGD.civNamesFontColor.getB(), settingsGD.civNamesFontColor_ALPHA);
        params.minFilter = Texture.TextureFilter.Linear;
        params.magFilter = Texture.TextureFilter.Linear;
        params.borderColor = new Color(settingsGD.civNamesFontColorBorder.getR(), settingsGD.civNamesFontColorBorder.getG(), settingsGD.civNamesFontColorBorder.getB(), settingsGD.civNamesFontColorBorder_ALPHA);
        params.borderWidth = (float)settingsGD.FONT_BORDER_WIDTH;
        fontBorder = genarator.generateFont(params);
        //genarator.dispose();
        loadFontBorder2();
    }

    public static final void loadFontBorder2() {
        if (fontBorder2 != null) {
            fontBorder2.dispose();
            fontBorder2 = null;
        }

        String sFont = lang.get("fontCivNames");
        if (sFont.equals("font2")) {
            sFont = "rbold.ttf";
        }

        FreeTypeFontGenerator genarator = null;

        try {
            genarator = new FreeTypeFontGenerator(FileManager.loadFile("game/fonts/" + sFont));
        } catch (GdxRuntimeException var3) {
            genarator = new FreeTypeFontGenerator(FileManager.loadFile("game/fonts/rbold.ttf"));
        }
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.incremental = true;
        params.characters = lang.get("charset");
        params.size = settingsGD.FONT_BORDER_SIZEX;
        params.color = new Color(settingsGD.civNamesFontColor.getR(), settingsGD.civNamesFontColor.getG(), settingsGD.civNamesFontColor.getB(), settingsGD.civNamesFontColor_ALPHA);
        params.minFilter = Texture.TextureFilter.Linear;
        params.magFilter = Texture.TextureFilter.Linear;
        params.borderColor = new Color(settingsGD.civNamesFontColorBorder.getR(), settingsGD.civNamesFontColorBorder.getG(), settingsGD.civNamesFontColorBorder.getB(), settingsGD.civNamesFontColorBorder_ALPHA);
        params.borderWidth = (float)settingsGD.FONT_BORDER_WIDTH;
        fontBorder2 = genarator.generateFont(params);
        //genarator.dispose();
    }
    public static final String getNumber_SHORT(int nValue) {
        if(settingsGD.LANG_TAG.contains("cn")){
            return NumberShortenUtil.getNumber_SHORT(nValue);
        }
        if (nValue < 1000) {
            return "" + nValue;
        } else if (nValue < 1000000) {
            String outValue = "" + (float)nValue / 1000.0F;

            try {
                return outValue.charAt(outValue.indexOf(".") + 1) == '0' ? nValue / 1000 + lang.get("Value_Thousand") : outValue.substring(0, outValue.indexOf(".") + 2) + lang.get("Value_Thousand");
            } catch (IndexOutOfBoundsException var3) {
                return nValue / 1000 + lang.get("Value_Thousand");
            }
        } else {
            String outValue = "" + (float)nValue / 1000000.0F;

            try {
                return outValue.charAt(outValue.indexOf(".") + 1) == '0' ? nValue / 1000 + lang.get("Value_Million") : outValue.substring(0, outValue.indexOf(".") + 2) + lang.get("Value_Million");
            } catch (IndexOutOfBoundsException var4) {
                return nValue / 1000 + lang.get("Value_Million");
            }
        }
    }

    public static String getNumber_SHORT_ARMY(int nValue) {
        if(settingsGD.LANG_TAG.contains("cn")){
            return NumberShortenUtil.getNumber_SHORT_ARMY(nValue);
        }
        if (nValue < settingsGD.SHORTEN_ARMY_OVER) {
            return "" + nValue;
        } else if (nValue < 1000000) {
            String outValue = "" + (float)nValue / 1000.0F;

            try {
                return outValue.charAt(outValue.indexOf(".") + 1) == '0' ? nValue / 1000 + lang.get("Value_Thousand") : outValue.substring(0, outValue.indexOf(".") + 2) + lang.get("Value_Thousand");
            } catch (IndexOutOfBoundsException var3) {
                return nValue / 1000 + lang.get("Value_Thousand");
            }
        } else {
            String outValue = "" + (float)nValue / 1000000.0F;

            try {
                return outValue.charAt(outValue.indexOf(".") + 1) == '0' ? nValue / 1000 + lang.get("Value_Million") : outValue.substring(0, outValue.indexOf(".") + 2) + lang.get("Value_Million");
            } catch (IndexOutOfBoundsException var4) {
                return nValue / 1000 + lang.get("Value_Million");
            }
        }
    }

    public static String getNumber_SHORT(long nValue) {
        if(settingsGD.LANG_TAG.contains("cn")){
            return NumberShortenUtil.getNumber_SHORT(nValue);
        }
        if (nValue < 1000L) {
            return "" + nValue;
        } else if (nValue < 1000000L) {
            float thousands = (float)nValue / 1000.0F;
            String outValue = String.format("%.1f", thousands);
            if (outValue.endsWith(".0")) {
                outValue = outValue.substring(0, outValue.length() - 2);
            }

            return outValue.replace(',', '.') + lang.get("Value_Thousand");
        } else {
            float millions = (float)nValue / 1000000.0F;
            String outValue = String.format("%.1f", millions);
            if (outValue.endsWith(".0")) {
                outValue = outValue.substring(0, outValue.length() - 2);
            }

            return outValue.replace(',', '.') + lang.get("Value_Million");
        }
    }
}

package team.rainfall.mingsha;

import age.of.civilizations2.jakowski.lukasz.CFG;
import age.of.civilizations2.jakowski.lukasz.IMGManager;
import age.of.civilizations2.jakowski.lukasz.Image;
import age.of.civilizations2.jakowski.lukasz.Renderer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import team.rainfall.mingsha.config.Hoi4LoadingConfigData;
import team.rainfall.mingsha.config.MingshaConfig;

/**
 * 钢铁雄心4风格加载界面：
 * 顶部居中状态面板（当前加载步骤 + 进度条），底部居中名言面板。
 * 美术资源位于 UI/loading_hoi4/，名言使用原版语言文件的加载文本（支持 \n 换行）。
 */
public class HOI4Loading {
    private static final String RES_DIR = "UI/loading_hoi4/";
    private static final float REF_WIDTH = 1024.0F;

    private static Image barFrame = null;
    private static Image barFill = null;
    private static Image panelSmall = null;
    private static Image panelLarge = null;
    private static boolean loadFailed = false;

    private static String[] quoteLines = null;
    private static int[] quoteLineWidths = null;
    private static int quoteMaxWidth = 0;
    private static long lastDrawTime = 0L;

    public static void draw(SpriteBatch oSB, float nProgress, String statusText) {
        if (!MingshaConfig.isHoi4LoadingEnabled()) {
            return;
        }
        try {
            ensureLoaded();
            if (loadFailed) {
                return;
            }

            Hoi4LoadingConfigData cfg = MingshaConfig.hoi4Loading;
            long gap = cfg != null ? cfg.quoteRotationMs : 3000L;
            if (System.currentTimeMillis() - lastDrawTime > gap) {
                pickQuote();
            }
            lastDrawTime = System.currentTimeMillis();

            drawStatusPanel(oSB, nProgress, statusText, cfg);
            drawQuotePanel(oSB, cfg);
        } catch (Exception ex) {
            CFG.exceptionStack(ex);
        }
    }

    private static float scale() {
        return (float)CFG.GAMEWIDTH / REF_WIDTH;
    }

    private static float panelWidthRatio(Hoi4LoadingConfigData cfg) {
        float v = cfg != null ? cfg.panelWidthRatio : 0.49F;
        return (v > 0.05F && v < 1.0F) ? v : 0.49F;
    }

    private static Color statusColor(Hoi4LoadingConfigData cfg) {
        return toColor(cfg != null ? cfg.statusTextColor : null, 0.92F, 0.92F, 0.92F);
    }

    private static Color quoteColor(Hoi4LoadingConfigData cfg) {
        return toColor(cfg != null ? cfg.quoteTextColor : null, 0.88F, 0.88F, 0.88F);
    }

    private static Color toColor(float[] rgba, float dr, float dg, float db) {
        if (rgba != null && rgba.length >= 3) {
            float a = rgba.length >= 4 ? rgba[3] : 1.0F;
            return new Color(clamp01(rgba[0]), clamp01(rgba[1]), clamp01(rgba[2]), clamp01(a));
        }
        return new Color(dr, dg, db, 1.0F);
    }

    private static float clamp01(float v) {
        return Math.min(1.0F, Math.max(0.0F, v));
    }

    private static void drawStatusPanel(SpriteBatch oSB, float nProgress, String statusText, Hoi4LoadingConfigData cfg) {
        if (nProgress > 1.0F) {
            nProgress = 1.0F;
        } else if (nProgress < 0.0F) {
            nProgress = 0.0F;
        }

        int panelW = (int)((float)CFG.GAMEWIDTH * panelWidthRatio(cfg));
        int panelH = panelW * panelSmall.getHeight() / panelSmall.getWidth();
        int panelX = (CFG.GAMEWIDTH - panelW) / 2;
        int panelY = 0;

        oSB.setColor(Color.WHITE);
        panelSmall.draw(oSB, panelX, panelY, panelW, panelH);

        CFG.glyphLay.setText(CFG.fontMain.get(CFG.FONT_BOLD), statusText);
        Renderer.drawTextWithShadow(
            oSB,
            CFG.FONT_BOLD,
            statusText,
            CFG.GAMEWIDTH / 2 - (int)CFG.glyphLay.width / 2,
            panelY + (panelH - CFG.TEXT_HEIGHT_DEFAULT) / 2,
            statusColor(cfg)
        );

        int barW = (int)((float)panelW * 0.96F);
        int barH = barW * barFrame.getHeight() / barFrame.getWidth();
        int barX = (CFG.GAMEWIDTH - barW) / 2;
        int barY = panelY + panelH - (int)((float)panelH * 0.15F);
        oSB.setColor(Color.WHITE);
        barFrame.draw(oSB, barX, barY, barW, barH);
        if (nProgress > 0.0F) {
            int fillW = (int)((float)barW * nProgress);
            int srcW = (int)((float)barFill.getWidth() * nProgress);
            if (fillW > 0 && srcW > 0) {
                oSB.draw(barFill.getTexture(), (float)barX, (float)(-(barY + barH)), (float)fillW, (float)barH, 0, 0, srcW, barFill.getHeight(), false, false);
            }
        }
    }

    private static void drawQuotePanel(SpriteBatch oSB, Hoi4LoadingConfigData cfg) {
        if (quoteLines == null || quoteLines.length == 0) {
            return;
        }

        Image panel = panelLarge;
        int textBlockH = quoteLines.length * CFG.TEXT_HEIGHT_DEFAULT + (quoteLines.length - 1) * CFG.PADD;
        int panelW = (int)((float)CFG.GAMEWIDTH * panelWidthRatio(cfg));
        int panelH = panelW * panel.getHeight() / panel.getWidth();
        int panelX = (CFG.GAMEWIDTH - panelW) / 2;
        int panelY = CFG.GAMEHEIGHT - panelH;

        oSB.setColor(Color.WHITE);
        panel.draw(oSB, panelX, panelY, panelW, panelH);

        int textY = panelY + (panelH - textBlockH) / 2;
        for (int i = 0; i < quoteLines.length; i++) {
            Renderer.drawTextWithShadow(
                oSB,
                CFG.FONT_BOLD,
                quoteLines[i],
                CFG.GAMEWIDTH / 2 - quoteLineWidths[i] / 2,
                textY + i * (CFG.TEXT_HEIGHT_DEFAULT + CFG.PADD),
                quoteColor(cfg)
            );
        }
    }

    private static void pickQuote() {
        try {
            if (CFG.lang == null || CFG.lang.iLNOT <= 0) {
                quoteLines = null;
                return;
            }

            String raw = CFG.lang.getLOA("L" + CFG.oR.nextInt(CFG.lang.iLNOT));
            String[] lines = raw.replace("\\n", "\n").split("\r?\n");
            int count = 0;
            for (String line : lines) {
                if (line.trim().length() > 0) {
                    count++;
                }
            }
            if (count == 0) {
                quoteLines = null;
                return;
            }

            quoteLines = new String[count];
            quoteLineWidths = new int[count];
            quoteMaxWidth = 0;
            int i = 0;
            for (String line : lines) {
                line = line.trim();
                if (line.length() == 0) {
                    continue;
                }

                CFG.glyphLay.setText(CFG.fontMain.get(CFG.FONT_BOLD), line);
                quoteLines[i] = line;
                quoteLineWidths[i] = (int)CFG.glyphLay.width;
                quoteMaxWidth = Math.max(quoteMaxWidth, quoteLineWidths[i]);
                i++;
            }
        } catch (Exception ex) {
            quoteLines = null;
            CFG.exceptionStack(ex);
        }
    }

    private static void ensureLoaded() {
        if (barFrame != null || loadFailed) {
            return;
        }

        try {
            barFrame = IMGManager.loadImage(RES_DIR + "bar_frame.png", Format.RGBA8888, TextureFilter.Linear);
            barFill = IMGManager.loadImage(RES_DIR + "bar_fill.png", Format.RGBA8888, TextureFilter.Linear);
            panelSmall = IMGManager.loadImage(RES_DIR + "panel_small.png", Format.RGBA8888, TextureFilter.Linear);
            panelLarge = IMGManager.loadImage(RES_DIR + "panel_large.png", Format.RGBA8888, TextureFilter.Linear);
        } catch (Exception ex) {
            loadFailed = true;
            CFG.exceptionStack(ex);
        }
    }
}

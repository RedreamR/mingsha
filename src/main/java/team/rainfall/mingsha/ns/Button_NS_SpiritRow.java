package team.rainfall.mingsha.ns;

import age.of.civilizations2.jakowski.lukasz.CFG;
import age.of.civilizations2.jakowski.lukasz.IMGManager;
import age.of.civilizations2.jakowski.lukasz.Image;
import age.of.civilizations2.jakowski.lukasz.Images;
import age.of.civilizations2.jakowski.lukasz.Renderer;
import age.of.civilizations2.jakowski.lukasz.Button.Classic.Button_Classic_Description;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * One spirit inside a civilization's block on the starting-spirit screen: its icon,
 * its name and what it does. Clicking it opens the definition editor.
 * <p>
 * A preset holds ids, not definitions, and {@code NSDefs.delete} deliberately leaves
 * owned spirits alone, so a row can outlive the thing it names. Such a row says so in
 * red instead of rendering as an empty label, because it is still the only place the
 * dead id can be removed from.
 */
public class Button_NS_SpiritRow extends Button_Classic_Description {

    private static final Color COLOR_MISSING = new Color(0.85F, 0.35F, 0.35F, 1.0F);

    /** Side of the square icon, sized off the row so it follows the GUI scale. */
    public static int iconSize(int nRowHeight) {
        return Math.max(1, nRowHeight - CFG.PADD * 2);
    }

    public static int textIndent(int nRowHeight) {
        return CFG.PADD * 3 + iconSize(nRowHeight);
    }

    private final String sNSID;

    public Button_NS_SpiritRow(String nID, String sDesc, String sText, int iPosX, int iPosY, int iWidth, int iHeight) {
        super(sDesc, sText, textIndent(iHeight), iPosX, iPosY, iWidth, iHeight, true);
        this.sNSID = nID == null ? "" : nID;
    }

    private boolean isMissing() {
        return NSDefs.get(this.sNSID) == null;
    }

    @Override
    public void drawButtonBGE(SpriteBatch oSB, int iTranslateX, int iTranslateY, boolean isActive) {
        super.drawButtonBGE(oSB, iTranslateX, iTranslateY, isActive);
        oSB.setColor(Color.WHITE);
        int tSize = iconSize(this.getHeightE());
        Image tIcon = NSIcons.get(this.sNSID);
        (tIcon != null ? tIcon : IMGManager.getIMG(Images.noLeader))
            .draw(
                oSB,
                this.getPosXE() + CFG.PADD * 2 + iTranslateX,
                this.getPosY() + this.getHeightE() / 2 - tSize / 2 + iTranslateY,
                tSize,
                tSize
            );
    }

    /**
     * Same two lines {@code Button_Classic_Description} draws, with the name turning
     * red when the definition is gone.
     */
    @Override
    public void drawTextE(SpriteBatch oSB, int iTranslateX, int iTranslateY, boolean isActive) {
        if (!this.isMissing()) {
            super.drawTextE(oSB, iTranslateX, iTranslateY, isActive);
            return;
        }

        Renderer.drawText(
            oSB,
            this.fontID,
            this.getTextE(),
            this.getPosXE() + this.getTextPosElem() + iTranslateX,
            this.getPosY() + this.getHeightE() / 2 - CFG.PADD * 3 / 4 - this.getTextHeight() + iTranslateY,
            COLOR_MISSING
        );
        Renderer.drawText(
            oSB,
            CFG.FONT_BOLD_SMALL,
            CFG.lang.get("NSMissing") + ": " + this.sNSID,
            this.getPosXE() + this.getTextPosElem() + iTranslateX,
            this.getPosY() + this.getHeightE() / 2 + CFG.PADD * 3 / 4 + iTranslateY,
            new Color(0.58F, 0.58F, 0.58F, 1.0F)
        );
    }

    @Override
    public void buildElemHover() {
        try {
            this.menuElemHover = NSHover.of(this.sNSID);
        } catch (Exception e) {
            this.menuElemHover = null;
        }
    }
}

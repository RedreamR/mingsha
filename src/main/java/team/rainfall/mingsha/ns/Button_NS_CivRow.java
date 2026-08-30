package team.rainfall.mingsha.ns;

import age.of.civilizations2.jakowski.lukasz.CFG;
import age.of.civilizations2.jakowski.lukasz.IMGManager;
import age.of.civilizations2.jakowski.lukasz.Images;
import age.of.civilizations2.jakowski.lukasz.Button.Classic.Button_Classic_Description;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.MEHover_2E;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.ME_Hover_2Type;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.ME_Hover_2Type_Flag_Big;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.ME_Hover_2Type_Text;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.ME_Hover_2Type_Text_Big;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.ME_Hover_v2;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

/**
 * A civilization row of the starting-spirit screens: its flag, its name and how many
 * spirits it starts with. What a click does differs per screen, so the row is told
 * what to promise in its tooltip rather than assuming.
 * <p>
 * The flag is drawn the way {@code Button_CivName} draws it — {@code getFlagC()} plus
 * the {@code flagRectSmall} frame — so it costs no texture of its own; {@code Core}
 * owns and disposes those images. A preset may name a civilization the current map does
 * not have, in which case there is no flag to ask for and the random-civilization
 * placeholder stands in.
 */
public class Button_NS_CivRow extends Button_Classic_Description {

    /**
     * Where the text starts, leaving room for the flag. A method rather than a
     * constant: {@code CFG.PADD} and the flag size follow the GUI scale, so they are
     * only the right numbers at the moment the row is built.
     */
    public static int textIndent() {
        return CFG.PADD * 3 + CFG.CIV_FLAG_WIDTH;
    }

    private final int iCivID;

    /** Second tooltip line: what clicking this row is going to do. */
    private final String sActionText;

    public Button_NS_CivRow(
        int nCivID, String sDesc, String sText, String nActionText, int iPosX, int iPosY, int iWidth, int iHeight
    ) {
        super(sDesc, sText, textIndent(), iPosX, iPosY, iWidth, iHeight, true);
        this.iCivID = nCivID;
        this.sActionText = nActionText == null ? "" : nActionText;
    }

    @Override
    public void drawButtonBGE(SpriteBatch oSB, int iTranslateX, int iTranslateY, boolean isActive) {
        super.drawButtonBGE(oSB, iTranslateX, iTranslateY, isActive);
        oSB.setColor(Color.WHITE);
        int tPosX = this.getPosXE() + CFG.PADD * 2 + iTranslateX;
        int tPosY = this.getPosY() + this.getHeightE() / 2 - CFG.CIV_FLAG_HEIGHT / 2 + iTranslateY;
        try {
            if (this.iCivID <= 0) {
                throw new NullPointerException();
            }
            CFG.core
                .getCiv(this.iCivID)
                .getFlagC()
                .drawO(oSB, tPosX, tPosY - CFG.core.getCiv(this.iCivID).getFlagC().getHeight(), CFG.CIV_FLAG_WIDTH, CFG.CIV_FLAG_HEIGHT);
        } catch (Exception e) {
            IMGManager.getIMG(Images.randomCivilizationFlag)
                .drawO(
                    oSB,
                    tPosX,
                    tPosY - IMGManager.getIMG(Images.randomCivilizationFlag).getHeight(),
                    CFG.CIV_FLAG_WIDTH,
                    CFG.CIV_FLAG_HEIGHT
                );
        }

        IMGManager.getIMG(Images.flagRectSmall)
            .drawO(
                oSB,
                tPosX,
                tPosY - IMGManager.getIMG(Images.flagRectSmall).getHeight(),
                CFG.CIV_FLAG_WIDTH,
                CFG.CIV_FLAG_HEIGHT
            );
    }

    @Override
    public void buildElemHover() {
        try {
            List<MEHover_2E> nElements = new ArrayList<MEHover_2E>();
            List<ME_Hover_2Type> nData = new ArrayList<ME_Hover_2Type>();
            if (this.iCivID > 0) {
                nData.add(new ME_Hover_2Type_Flag_Big(this.iCivID));
            }
            nData.add(new ME_Hover_2Type_Text_Big(this.getTextE()));
            nElements.add(new MEHover_2E(nData));
            nData.clear();
            if (!this.sActionText.isEmpty()) {
                nData.add(new ME_Hover_2Type_Text(this.sActionText, CFG.COLOR_HOVER_TITLE));
                nElements.add(new MEHover_2E(nData));
                nData.clear();
            }
            this.menuElemHover = new ME_Hover_v2(nElements);
        } catch (Exception e) {
            this.menuElemHover = null;
        }
    }
}

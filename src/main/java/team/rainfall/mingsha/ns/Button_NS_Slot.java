package team.rainfall.mingsha.ns;

import age.of.civilizations2.jakowski.lukasz.CFG;
import age.of.civilizations2.jakowski.lukasz.IMGManager;
import age.of.civilizations2.jakowski.lukasz.Image;
import age.of.civilizations2.jakowski.lukasz.Images;
import age.of.civilizations2.jakowski.lukasz.Button.Button_Transparent;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * One square icon slot of the in-game national spirit bar.
 * <p>
 * {@code ButtonM.drawE} is final, so the visuals go into {@code drawButtonBGE}
 * and {@code drawTextE} is silenced — the slot draws an icon, never a label.
 */
public class Button_NS_Slot extends Button_Transparent {

    private static final Color COLOR_SLOT = new Color(0.11F, 0.11F, 0.17F, 1.0F);
    private static final Color COLOR_SLOT_HOVER = new Color(0.2F, 0.2F, 0.29F, 1.0F);
    private static final Color COLOR_SLOT_EDGE = new Color(0.06F, 0.06F, 0.1F, 1.0F);

    /** Spirit id this slot shows; empty for a layout placeholder. */
    public String sNSID;

    public Button_NS_Slot(String nID, int iPosX, int iPosY, int iWidth, int iHeight) {
        super(iPosX, iPosY, iWidth, iHeight, true);
        this.sNSID = nID == null ? "" : nID;
    }

    @Override
    public void drawButtonBGE(SpriteBatch oSB, int iTranslateX, int iTranslateY, boolean isActive) {
        int tPosX = this.getPosXE() + iTranslateX;
        int tPosY = this.getPosY() + iTranslateY;
        oSB.setColor(COLOR_SLOT_EDGE);
        IMGManager.getIMG(Images.pix255).draw(oSB, tPosX, tPosY, this.getWidthE(), this.getHeightE());
        oSB.setColor(isActive || this.getIsHovered() ? COLOR_SLOT_HOVER : COLOR_SLOT);
        IMGManager.getIMG(Images.pix255)
            .draw(oSB, tPosX + 1, tPosY + 1, this.getWidthE() - 2, this.getHeightE() - 2);
        oSB.setColor(Color.WHITE);
        int tIconSize = Math.min(this.getWidthE(), this.getHeightE()) - CFG.PADD * 2;
        if (tIconSize > 0) {
            // A spirit without a gfx/ns/<id>.png, and every layout placeholder, keeps
            // the empty-portrait frame the leader slots use.
            Image tIcon = NSIcons.get(this.sNSID);
            (tIcon != null ? tIcon : IMGManager.getIMG(Images.noLeader))
                .draw(
                    oSB,
                    tPosX + this.getWidthE() / 2 - tIconSize / 2,
                    tPosY + this.getHeightE() / 2 - tIconSize / 2,
                    tIconSize,
                    tIconSize
                );
        }
    }

    @Override
    public void drawTextE(SpriteBatch oSB, int iTranslateX, int iTranslateY, boolean isActive) {
    }

    /**
     * The slot is an icon with no label, so the tooltip is the only place its name,
     * remaining turns and modifiers are readable at all. It describes what the
     * civilization owns rather than the definition, since an already-granted spirit
     * keeps the percentages it was granted with.
     */
    @Override
    public void buildElemHover() {
        try {
            this.menuElemHover = NSHover.ofOwned(CFG.getActiveCivInfoId(), this.sNSID);
        } catch (Exception e) {
            this.menuElemHover = null;
        }
    }
}

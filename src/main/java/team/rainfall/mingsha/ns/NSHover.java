package team.rainfall.mingsha.ns;

import age.of.civilizations2.jakowski.lukasz.CFG;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.MEHover_2E;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.ME_Hover_2Type;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.ME_Hover_2Type_Text;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.ME_Hover_2Type_TextDesc;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.ME_Hover_2Type_Text_Big;
import age.of.civilizations2.jakowski.lukasz.MenuE_HoverP.ME_Hover_v2;
import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds the national spirit tooltip, shared by the in-game bar slot and the rows of
 * the starting-spirit table so both describe a spirit the same way.
 * <p>
 * A slot shows what the civilization actually has — the percentages stored in its
 * ownership entry and the turns still to run — while a table row shows the definition,
 * since no civilization owns it yet. Both go through {@link #build} so the layout stays
 * identical: name, one grey status line, the description, then one line per modifier.
 */
public final class NSHover {

    /** Same red the rows use for a definition that has gone missing. */
    public static final Color COLOR_MISSING = new Color(0.85F, 0.35F, 0.35F, 1.0F);

    /** Characters per line when a description has to be wrapped by hand. */
    private static final int DESC_CHARS_PER_LINE = 24;

    private NSHover() {
    }

    /** Tooltip of a definition: what granting this spirit would do. */
    public static ME_Hover_v2 of(String nID) {
        NationalSpirit tSpirit = NSDefs.get(nID);
        if (tSpirit == null) {
            return missing(nID);
        }
        return build(tSpirit.displayName(), nID + "  |  " + tSpirit.durationText(), tSpirit.desc, tSpirit.snapshot());
    }

    /**
     * Tooltip of a spirit a civilization owns: its own percentages and its remaining
     * turns, which is what the in-game bar is showing. Falls back to the definition
     * when the entry cannot be read.
     */
    public static ME_Hover_v2 ofOwned(int nCivID, String nID) {
        NSStore.Owned tOwned = NSStore.getOwned(nCivID, nID);
        if (tOwned == null || !tOwned.isValid()) {
            return of(nID);
        }
        NationalSpirit tSpirit = NSDefs.get(nID);
        String tStatus = tOwned.isPermanent()
            ? CFG.lang.get("NSPermanent")
            : CFG.lang.get("NSTurnsLeft") + ": " + tOwned.turnsLeft;
        return build(
            tSpirit != null ? tSpirit.displayName() : nID,
            tStatus,
            tSpirit != null ? tSpirit.desc : "",
            tOwned.mods
        );
    }

    /** Says which id is dangling, since that is all there is left to say about it. */
    public static ME_Hover_v2 missing(String nID) {
        List<MEHover_2E> nElements = new ArrayList<MEHover_2E>();
        List<ME_Hover_2Type> nData = new ArrayList<ME_Hover_2Type>();
        nData.add(new ME_Hover_2Type_Text_Big(CFG.lang.get("NSMissing"), COLOR_MISSING));
        nElements.add(new MEHover_2E(nData));
        nData.clear();
        nData.add(new ME_Hover_2Type_Text(nID == null ? "" : nID));
        nElements.add(new MEHover_2E(nData));
        nData.clear();
        return new ME_Hover_v2(nElements);
    }

    /** One-line tooltip, the shape every vanilla row button uses for a plain label. */
    public static ME_Hover_v2 text(String nText) {
        List<MEHover_2E> nElements = new ArrayList<MEHover_2E>();
        List<ME_Hover_2Type> nData = new ArrayList<ME_Hover_2Type>();
        nData.add(new ME_Hover_2Type_Text(nText, CFG.COLOR_HOVER_TITLE));
        nElements.add(new MEHover_2E(nData));
        nData.clear();
        return new ME_Hover_v2(nElements);
    }

    /**
     * Appends the description, one tooltip line per text line.
     * <p>
     * {@code ME_Hover_2Type_TextDesc} wraps on spaces, which a Chinese description does
     * not have — it would come out as one line as wide as the whole sentence — so text
     * without spaces is hard-wrapped by character count instead.
     */
    private static void addDesc(List<MEHover_2E> nElements, List<ME_Hover_2Type> nData, String nDesc) {
        if (nDesc.indexOf(' ') >= 0) {
            nData.add(new ME_Hover_2Type_TextDesc(nDesc));
            nElements.add(new MEHover_2E(nData));
            nData.clear();
            return;
        }

        for (int i = 0; i < nDesc.length(); i += DESC_CHARS_PER_LINE) {
            nData.add(new ME_Hover_2Type_Text(nDesc.substring(i, Math.min(nDesc.length(), i + DESC_CHARS_PER_LINE))));
            nElements.add(new MEHover_2E(nData));
            nData.clear();
        }
    }

    /**
     * @param nPercents the eleven modifiers already rounded to whole percent points,
     *                  i.e. either a definition's snapshot or an entry's stored values
     */
    private static ME_Hover_v2 build(String nName, String nStatus, String nDesc, int[] nPercents) {
        List<MEHover_2E> nElements = new ArrayList<MEHover_2E>();
        List<ME_Hover_2Type> nData = new ArrayList<ME_Hover_2Type>();

        nData.add(new ME_Hover_2Type_Text_Big(nName));
        nElements.add(new MEHover_2E(nData));
        nData.clear();
        if (nStatus != null && !nStatus.isEmpty()) {
            nData.add(new ME_Hover_2Type_Text(nStatus, CFG.COLOR_HOVER_TITLE));
            nElements.add(new MEHover_2E(nData));
            nData.clear();
        }
        if (nDesc != null && !nDesc.isEmpty()) {
            addDesc(nElements, nData, nDesc);
        }

        int tShown = 0;
        for (int i = 0; i < NationalSpirit.MOD_COUNT && i < nPercents.length; i++) {
            if (nPercents[i] == 0) {
                continue;
            }
            nData.add(new ME_Hover_2Type_Text(NationalSpirit.labelOf(i) + ": "));
            nData.add(
                new ME_Hover_2Type_Text(
                    (nPercents[i] > 0 ? "+" : "") + nPercents[i] + "%",
                    nPercents[i] > 0 ? CFG.COLOR_POSITIVE : CFG.COLOR_NEGATIVE_2
                )
            );
            nElements.add(new MEHover_2E(nData));
            nData.clear();
            tShown++;
        }
        if (tShown == 0) {
            nData.add(new ME_Hover_2Type_Text(CFG.lang.get("NSNoModifiers")));
            nElements.add(new MEHover_2E(nData));
            nData.clear();
        }

        return new ME_Hover_v2(nElements);
    }
}

package team.rainfall.mingsha.ns;

import age.of.civilizations2.jakowski.lukasz.CFG;

/**
 * One national spirit definition.
 * <p>
 * The eleven modifiers mirror the eleven {@code modifier_*} fields of
 * {@code Save_Civ_GameData} one-for-one and use the same scale the game does:
 * {@code 0.05F} means +5%. They are named fields rather than an array so the
 * global {@code game/national_spirits.json} stays readable by hand, with
 * {@link #getModifier(int)} / {@link #setModifier(int, float)} giving the editor
 * and the apply logic an indexed view.
 * <p>
 * Beware of {@code COLONIZATION_COST}: the game consumes it as
 * {@code (1.0F - modifier_ColonizationCost)}, so a <em>positive</em> value makes
 * colonization <em>cheaper</em>.
 */
public class NationalSpirit {

    public static final int MOD_COUNT = 11;

    public static final int POP_GROWTH = 0;
    public static final int ECONOMY_GROWTH = 1;
    public static final int INCOME_TAXATION = 2;
    public static final int INCOME_PRODUCTION = 3;
    public static final int ADMINISTRATION = 4;
    public static final int RESEARCH = 5;
    public static final int MILITARY_UPKEEP = 6;
    public static final int ATTACK_BONUS = 7;
    public static final int DEFENSE_BONUS = 8;
    public static final int MOVEMENT_POINTS = 9;
    public static final int COLONIZATION_COST = 10;

    /** Vanilla language keys, indexed exactly like the modifiers above. */
    private static final String[] LANG_KEYS = {
        "PopulationGrowthModifier",
        "EconomyGrowthModifier",
        "IncomeTaxation",
        "IncomeProduction",
        "Administration",
        "Research",
        "MilitaryUpkeep",
        "AttackBonus",
        "DefenseBonus",
        "MovementPoints",
        "ColonizationCost"
    };

    /** Internal id; also the icon file name and the key used by events and saves. */
    public String id = "";
    /** Display name. A leading '#' makes the rest a language key. */
    public String name = "";
    /** Description shown in the tooltip; supports the counter system's {@code $name} tokens. */
    public String desc = "";
    /** Spirits sharing a non-empty group are mutually exclusive. */
    public String group = "";
    /** Turns the spirit lasts when granted without an explicit duration; -1 = permanent. */
    public int defaultTurns = -1;

    public float popGrowth;
    public float economyGrowth;
    public float incomeTaxation;
    public float incomeProduction;
    public float administration;
    public float research;
    public float militaryUpkeep;
    public float attackBonus;
    public float defenseBonus;
    public float movementPoints;
    public float colonizationCost;

    public NationalSpirit() {
    }

    public NationalSpirit(String nID) {
        this.id = nID == null ? "" : nID;
    }

    public static String langKeyOf(int nIndex) {
        return nIndex >= 0 && nIndex < LANG_KEYS.length ? LANG_KEYS[nIndex] : "";
    }

    /** Localized label of one modifier, e.g. "Research". */
    public static String labelOf(int nIndex) {
        return CFG.lang.get(langKeyOf(nIndex));
    }

    public float getModifier(int nIndex) {
        switch (nIndex) {
            case POP_GROWTH:
                return this.popGrowth;
            case ECONOMY_GROWTH:
                return this.economyGrowth;
            case INCOME_TAXATION:
                return this.incomeTaxation;
            case INCOME_PRODUCTION:
                return this.incomeProduction;
            case ADMINISTRATION:
                return this.administration;
            case RESEARCH:
                return this.research;
            case MILITARY_UPKEEP:
                return this.militaryUpkeep;
            case ATTACK_BONUS:
                return this.attackBonus;
            case DEFENSE_BONUS:
                return this.defenseBonus;
            case MOVEMENT_POINTS:
                return this.movementPoints;
            case COLONIZATION_COST:
                return this.colonizationCost;
            default:
                return 0.0F;
        }
    }

    public void setModifier(int nIndex, float nValue) {
        switch (nIndex) {
            case POP_GROWTH:
                this.popGrowth = nValue;
                break;
            case ECONOMY_GROWTH:
                this.economyGrowth = nValue;
                break;
            case INCOME_TAXATION:
                this.incomeTaxation = nValue;
                break;
            case INCOME_PRODUCTION:
                this.incomeProduction = nValue;
                break;
            case ADMINISTRATION:
                this.administration = nValue;
                break;
            case RESEARCH:
                this.research = nValue;
                break;
            case MILITARY_UPKEEP:
                this.militaryUpkeep = nValue;
                break;
            case ATTACK_BONUS:
                this.attackBonus = nValue;
                break;
            case DEFENSE_BONUS:
                this.defenseBonus = nValue;
                break;
            case MOVEMENT_POINTS:
                this.movementPoints = nValue;
                break;
            case COLONIZATION_COST:
                this.colonizationCost = nValue;
                break;
            default:
        }
    }

    /**
     * The eleven modifiers rounded to whole percent points — exactly what gets
     * written into the ownership entry and later subtracted again, so granting and
     * removing cancel out to the bit even if this definition is edited in between.
     */
    public int[] snapshot() {
        int[] tSnapshot = new int[MOD_COUNT];
        for (int i = 0; i < MOD_COUNT; i++) {
            tSnapshot[i] = Math.round(this.getModifier(i) * 100.0F);
        }
        return tSnapshot;
    }

    /** Whether every modifier is zero, i.e. the spirit is purely cosmetic. */
    public boolean isEmpty() {
        for (int i = 0; i < MOD_COUNT; i++) {
            if (Math.round(this.getModifier(i) * 100.0F) != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * One-line list of the non-zero modifiers for list rows and tooltips, e.g.
     * "Research +10%, Administration -5%". Long spirits are cut off with an ellipsis
     * rather than pushed off the row.
     *
     * @param nMax how many modifiers to name before giving up
     */
    public String summaryText(int nMax) {
        StringBuilder tText = new StringBuilder();
        int tShown = 0;
        for (int i = 0; i < MOD_COUNT; i++) {
            int tPercent = Math.round(this.getModifier(i) * 100.0F);
            if (tPercent == 0) {
                continue;
            }
            if (tShown == nMax) {
                tText.append(", ...");
                break;
            }
            if (tShown > 0) {
                tText.append(", ");
            }
            tText.append(labelOf(i)).append(tPercent > 0 ? " +" : " ").append(tPercent).append('%');
            tShown++;
        }
        return tText.toString();
    }

    /** "Permanent" or the default turn count, for list rows and tooltips. */
    public String durationText() {
        return this.defaultTurns < 0 ? CFG.lang.get("NSPermanent") : CFG.lang.get("Turns") + ": " + this.defaultTurns;
    }

    /** Display name, resolving a leading '#' as a language key; falls back to the id. */
    public String displayName() {
        try {
            if (this.name == null || this.name.isEmpty()) {
                return this.id;
            }
            if (this.name.length() > 1 && this.name.charAt(0) == '#') {
                return CFG.lang.get(this.name.substring(1));
            }
            return this.name;
        } catch (Exception e) {
            return this.id;
        }
    }

    public NationalSpirit copy() {
        NationalSpirit tCopy = new NationalSpirit(this.id);
        tCopy.name = this.name;
        tCopy.desc = this.desc;
        tCopy.group = this.group;
        tCopy.defaultTurns = this.defaultTurns;
        for (int i = 0; i < MOD_COUNT; i++) {
            tCopy.setModifier(i, this.getModifier(i));
        }
        return tCopy;
    }
}

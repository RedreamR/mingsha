package team.rainfall.mingsha.ns;

import age.of.civilizations2.jakowski.lukasz.CFG;
import age.of.civilizations2.jakowski.lukasz.Civilization;
import age.of.civilizations2.jakowski.lukasz.Save.Save_Civ_GameData;

import java.util.ArrayList;
import java.util.List;

/**
 * Ownership of national spirits, stored as entries inside
 * {@code Civilization.civGD.lEvents_DecisionsTaken} — the same list the counter
 * system piggybacks on, so nothing about the save format changes and the game's
 * own serializer persists it for free.
 * <p>
 * Entry layout: {@code $MSNS_<id>=<turnsLeft>;<m0>,<m1>,…,<m10>}.
 * {@code turnsLeft} is -1 for a permanent spirit. The eleven integers are whole
 * percent points and are a snapshot of what was actually added to
 * {@code civGD.modifier_*} when the spirit was granted, so removal reverses
 * exactly that, even if the definition was edited in between.
 */
public class NSStore {

    public static final String PREFIX = "$MSNS_";
    private static final char VALUE_SEP = '=';
    private static final char FIELD_SEP = ';';
    private static final char MOD_SEP = ',';

    /** A spirit a civilization currently owns, parsed out of its raw entry. */
    public static class Owned {

        public String id = "";
        public int turnsLeft = -1;
        public int[] mods = new int[NationalSpirit.MOD_COUNT];

        public Owned() {
        }

        /** Parses a raw {@code $MSNS_…} entry; leaves {@link #id} empty when malformed. */
        public Owned(String nRaw) {
            try {
                if (nRaw == null || !nRaw.startsWith(PREFIX)) {
                    return;
                }
                int tSep = nRaw.indexOf(VALUE_SEP, PREFIX.length());
                if (tSep <= PREFIX.length()) {
                    return;
                }
                this.id = nRaw.substring(PREFIX.length(), tSep);
                String[] tFields = nRaw.substring(tSep + 1).split(String.valueOf(FIELD_SEP), -1);
                if (tFields.length > 0 && !tFields[0].isEmpty()) {
                    this.turnsLeft = Integer.parseInt(tFields[0].trim());
                }
                if (tFields.length > 1 && !tFields[1].isEmpty()) {
                    String[] tMods = tFields[1].split(String.valueOf(MOD_SEP), -1);
                    for (int i = 0; i < this.mods.length && i < tMods.length; i++) {
                        if (!tMods[i].isEmpty()) {
                            this.mods[i] = Integer.parseInt(tMods[i].trim());
                        }
                    }
                }
            } catch (Exception e) {
                CFG.exceptionStack(e);
            }
        }

        public boolean isValid() {
            return this.id != null && !this.id.isEmpty();
        }

        public boolean isPermanent() {
            return this.turnsLeft < 0;
        }

        public String getRaw() {
            StringBuilder tBuilder = new StringBuilder();
            tBuilder.append(PREFIX).append(this.id).append(VALUE_SEP).append(this.turnsLeft).append(FIELD_SEP);
            for (int i = 0; i < this.mods.length; i++) {
                if (i > 0) {
                    tBuilder.append(MOD_SEP);
                }
                tBuilder.append(this.mods[i]);
            }
            return tBuilder.toString();
        }
    }

    /** Prefix that identifies every entry belonging to one spirit id. */
    public static String entryPrefixOf(String nID) {
        return PREFIX + nID + VALUE_SEP;
    }

    private static List<String> variablesOf(int nCivID) {
        if (nCivID < 0 || nCivID >= CFG.core.getCivsSize()) {
            return null;
        }
        Civilization tCiv = CFG.core.getCiv(nCivID);
        if (tCiv == null) {
            return null;
        }
        if (tCiv.civGD.lEvents_DecisionsTaken == null) {
            tCiv.civGD.lEvents_DecisionsTaken = new ArrayList<String>();
        }
        return tCiv.civGD.lEvents_DecisionsTaken;
    }

    private static Save_Civ_GameData gameDataOf(int nCivID) {
        if (nCivID < 0 || nCivID >= CFG.core.getCivsSize()) {
            return null;
        }
        Civilization tCiv = CFG.core.getCiv(nCivID);
        return tCiv == null ? null : tCiv.civGD;
    }

    /** Ids of every spirit the civilization currently owns, in the order they were granted. */
    public static List<String> listOwnedIDs(int nCivID) {
        List<String> tIDs = new ArrayList<String>();
        try {
            List<String> tVars = variablesOf(nCivID);
            if (tVars == null) {
                return tIDs;
            }
            for (String tEntry : tVars) {
                if (tEntry == null || !tEntry.startsWith(PREFIX)) {
                    continue;
                }
                int tSep = tEntry.indexOf(VALUE_SEP, PREFIX.length());
                if (tSep > PREFIX.length()) {
                    tIDs.add(tEntry.substring(PREFIX.length(), tSep));
                }
            }
        } catch (Exception e) {
            CFG.exceptionStack(e);
        }
        return tIDs;
    }

    /** Parsed ownership entries of one civilization, in grant order. */
    public static List<Owned> listOwned(int nCivID) {
        List<Owned> tOwned = new ArrayList<Owned>();
        try {
            List<String> tVars = variablesOf(nCivID);
            if (tVars == null) {
                return tOwned;
            }
            for (String tEntry : tVars) {
                if (tEntry != null && tEntry.startsWith(PREFIX)) {
                    Owned tEntryParsed = new Owned(tEntry);
                    if (tEntryParsed.isValid()) {
                        tOwned.add(tEntryParsed);
                    }
                }
            }
        } catch (Exception e) {
            CFG.exceptionStack(e);
        }
        return tOwned;
    }

    public static Owned getOwned(int nCivID, String nID) {
        try {
            List<String> tVars = variablesOf(nCivID);
            if (tVars == null || nID == null || nID.isEmpty()) {
                return null;
            }
            String tPrefix = entryPrefixOf(nID);
            for (String tEntry : tVars) {
                if (tEntry != null && tEntry.startsWith(tPrefix)) {
                    Owned tOwned = new Owned(tEntry);
                    return tOwned.isValid() ? tOwned : null;
                }
            }
        } catch (Exception e) {
            CFG.exceptionStack(e);
        }
        return null;
    }

    public static boolean has(int nCivID, String nID) {
        return getOwned(nCivID, nID) != null;
    }

    public static int turnsLeftOf(int nCivID, String nID) {
        Owned tOwned = getOwned(nCivID, nID);
        return tOwned != null ? tOwned.turnsLeft : 0;
    }

    public static int countOwned(int nCivID) {
        return listOwnedIDs(nCivID).size();
    }

    /**
     * Grants a spirit, using the definition's own duration.
     *
     * @return true when the civilization did not already own it
     */
    public static boolean grant(int nCivID, String nID) {
        NationalSpirit tSpirit = NSDefs.get(nID);
        return grant(nCivID, nID, tSpirit != null ? tSpirit.defaultTurns : -1);
    }

    /**
     * Grants a spirit for a given number of turns ({@code -1} = permanent), removing
     * any spirit of the same non-empty group first. The modifiers actually applied
     * are snapshotted into the entry.
     *
     * @return true when the civilization did not already own it
     */
    public static boolean grant(int nCivID, String nID, int nTurns) {
        try {
            if (nID == null || nID.isEmpty()) {
                return false;
            }
            List<String> tVars = variablesOf(nCivID);
            Save_Civ_GameData tGameData = gameDataOf(nCivID);
            if (tVars == null || tGameData == null || has(nCivID, nID)) {
                return false;
            }

            NationalSpirit tSpirit = NSDefs.get(nID);
            String tGroup = tSpirit != null && tSpirit.group != null ? tSpirit.group : "";
            if (!tGroup.isEmpty()) {
                List<String> tOwnedIDs = listOwnedIDs(nCivID);
                for (int i = 0; i < tOwnedIDs.size(); i++) {
                    if (tGroup.equals(NSDefs.groupOf(tOwnedIDs.get(i)))) {
                        remove(nCivID, tOwnedIDs.get(i));
                    }
                }
            }

            Owned tOwned = new Owned();
            tOwned.id = nID;
            tOwned.turnsLeft = nTurns < 0 ? -1 : nTurns;
            tOwned.mods = tSpirit != null ? tSpirit.snapshot() : new int[NationalSpirit.MOD_COUNT];
            applyModifiers(tGameData, tOwned.mods, 1);
            tVars.add(tOwned.getRaw());
            NSUI.invalidateBar(nCivID);
            return true;
        } catch (Exception e) {
            CFG.exceptionStack(e);
            return false;
        }
    }

    /**
     * Removes a spirit and gives back exactly the modifiers its entry recorded.
     *
     * @return true when the civilization owned it
     */
    public static boolean remove(int nCivID, String nID) {
        try {
            if (nID == null || nID.isEmpty()) {
                return false;
            }
            List<String> tVars = variablesOf(nCivID);
            Save_Civ_GameData tGameData = gameDataOf(nCivID);
            if (tVars == null || tGameData == null) {
                return false;
            }
            String tPrefix = entryPrefixOf(nID);
            for (int i = 0; i < tVars.size(); i++) {
                String tEntry = tVars.get(i);
                if (tEntry == null || !tEntry.startsWith(tPrefix)) {
                    continue;
                }
                Owned tOwned = new Owned(tEntry);
                tVars.remove(i);
                if (tOwned.isValid()) {
                    applyModifiers(tGameData, tOwned.mods, -1);
                }
                NSUI.invalidateBar(nCivID);
                return true;
            }
        } catch (Exception e) {
            CFG.exceptionStack(e);
        }
        return false;
    }

    /** Drops every spirit a civilization owns, reverting all of their modifiers. */
    public static void removeAll(int nCivID) {
        List<String> tOwnedIDs = listOwnedIDs(nCivID);
        for (int i = 0; i < tOwnedIDs.size(); i++) {
            remove(nCivID, tOwnedIDs.get(i));
        }
    }

    /**
     * One turn of ageing for a civilization's timed spirits: permanent ones are
     * skipped, the rest count down and are removed — modifiers and all — when they
     * hit zero. Called from the replaced {@code Civilization.updateBonuses()}.
     */
    public static void tick(int nCivID) {
        try {
            List<String> tVars = variablesOf(nCivID);
            Save_Civ_GameData tGameData = gameDataOf(nCivID);
            if (tVars == null || tGameData == null) {
                return;
            }
            for (int i = 0; i < tVars.size(); i++) {
                String tEntry = tVars.get(i);
                if (tEntry == null || !tEntry.startsWith(PREFIX)) {
                    continue;
                }
                Owned tOwned = new Owned(tEntry);
                if (!tOwned.isValid() || tOwned.isPermanent()) {
                    continue;
                }
                tOwned.turnsLeft--;
                if (tOwned.turnsLeft <= 0) {
                    tVars.remove(i--);
                    applyModifiers(tGameData, tOwned.mods, -1);
                    NSUI.invalidateBar(nCivID);
                } else {
                    tVars.set(i, tOwned.getRaw());
                }
            }
        } catch (Exception e) {
            CFG.exceptionStack(e);
        }
    }

    /**
     * Adds ({@code nSign} = 1) or gives back ({@code nSign} = -1) a snapshot of
     * whole percent points to the eleven {@code modifier_*} fields. Two of them have
     * no setter in the vanilla class, so all eleven are written directly, exactly as
     * the game itself does when a leader takes office.
     */
    private static void applyModifiers(Save_Civ_GameData nGameData, int[] nMods, int nSign) {
        if (nGameData == null || nMods == null) {
            return;
        }
        for (int i = 0; i < nMods.length && i < NationalSpirit.MOD_COUNT; i++) {
            if (nMods[i] == 0) {
                continue;
            }
            float tDelta = (float)(nSign * nMods[i]) / 100.0F;
            switch (i) {
                case NationalSpirit.POP_GROWTH:
                    nGameData.modifier_PopGrowth += tDelta;
                    break;
                case NationalSpirit.ECONOMY_GROWTH:
                    nGameData.modifier_EconomyGrowth += tDelta;
                    break;
                case NationalSpirit.INCOME_TAXATION:
                    nGameData.modifier_IncomeTaxation += tDelta;
                    break;
                case NationalSpirit.INCOME_PRODUCTION:
                    nGameData.modifier_IncomeProduction += tDelta;
                    break;
                case NationalSpirit.ADMINISTRATION:
                    nGameData.modifier_Administration += tDelta;
                    break;
                case NationalSpirit.RESEARCH:
                    nGameData.modifier_Research += tDelta;
                    break;
                case NationalSpirit.MILITARY_UPKEEP:
                    nGameData.modifier_MilitaryUpkeep += tDelta;
                    break;
                case NationalSpirit.ATTACK_BONUS:
                    nGameData.modifier_AttackBonus += tDelta;
                    break;
                case NationalSpirit.DEFENSE_BONUS:
                    nGameData.modifier_DefenseBonus += tDelta;
                    break;
                case NationalSpirit.MOVEMENT_POINTS:
                    nGameData.modifier_MovementPoints += tDelta;
                    break;
                case NationalSpirit.COLONIZATION_COST:
                    nGameData.modifier_ColonizationCost += tDelta;
                    break;
                default:
            }
        }
    }

    /**
     * Replaces national spirit tokens in a description:
     * {@code $NS_<id>} becomes that spirit's localized name, and a bare {@code $NS}
     * becomes the comma-separated list of the civilization's own spirits.
     */
    public static String formatDesc(String nDesc, int nCivID) {
        try {
            if (nDesc == null || nDesc.indexOf('$') < 0) {
                return nDesc;
            }
            String tDesc = nDesc;
            if (tDesc.contains("$NS_")) {
                List<NationalSpirit> tDefs = NSDefs.list();
                for (int i = 0; i < tDefs.size(); i++) {
                    NationalSpirit tSpirit = tDefs.get(i);
                    if (tSpirit.id != null && !tSpirit.id.isEmpty()) {
                        tDesc = tDesc.replace("$NS_" + tSpirit.id, tSpirit.displayName());
                    }
                }
            }
            if (tDesc.contains("$NS")) {
                List<String> tOwnedIDs = listOwnedIDs(nCivID);
                StringBuilder tBuilder = new StringBuilder();
                for (int i = 0; i < tOwnedIDs.size(); i++) {
                    if (i > 0) {
                        tBuilder.append(", ");
                    }
                    tBuilder.append(NSDefs.displayOf(tOwnedIDs.get(i)));
                }
                tDesc = tDesc.replace("$NS", tBuilder.toString());
            }
            return tDesc;
        } catch (Exception e) {
            CFG.exceptionStack(e);
            return nDesc;
        }
    }
}

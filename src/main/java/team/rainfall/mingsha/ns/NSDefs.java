package team.rainfall.mingsha.ns;

import age.of.civilizations2.jakowski.lukasz.CFG;
import age.of.civilizations2.jakowski.lukasz.Files.FileManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import java.util.ArrayList;
import java.util.List;

/**
 * The global national spirit catalogue, kept in {@code game/national_spirits.json}
 * the way Polaris Core keeps {@code game/NationalSpirit.json} — definitions are
 * shared by every scenario, while <em>who owns what at scenario start</em> is a
 * per-scenario matter handled by {@link NSPresets}.
 * <p>
 * Reads go through {@code FileManager.loadFile} so a mod folder can ship its own
 * catalogue; the in-game editor always writes to the local game directory.
 */
public class NSDefs {

    public static final String FILE = "game/national_spirits.json";

    /** Root object of the json file. */
    public static class Data {
        public ArrayList<NationalSpirit> nationalSpirits = new ArrayList<NationalSpirit>();
    }

    private static List<NationalSpirit> defs = null;

    private static Json json() {
        Json tJson = new Json();
        tJson.setIgnoreUnknownFields(true);
        tJson.setElementType(Data.class, "nationalSpirits", NationalSpirit.class);
        tJson.setOutputType(JsonWriter.OutputType.json);
        return tJson;
    }

    /** Every definition, loading the file on first use. Never null. */
    public static List<NationalSpirit> list() {
        if (defs == null) {
            load();
        }
        return defs;
    }

    public static void reload() {
        defs = null;
    }

    private static void load() {
        defs = new ArrayList<NationalSpirit>();
        try {
            FileHandle tFile = FileManager.loadFile(FILE);
            if (tFile == null || !tFile.exists()) {
                return;
            }
            String tContent = tFile.readString("UTF-8");
            if (tContent == null || tContent.trim().isEmpty()) {
                return;
            }
            Data tData = json().fromJson(Data.class, tContent);
            if (tData == null || tData.nationalSpirits == null) {
                return;
            }
            for (int i = 0; i < tData.nationalSpirits.size(); i++) {
                NationalSpirit tSpirit = tData.nationalSpirits.get(i);
                if (tSpirit != null && tSpirit.id != null && !tSpirit.id.isEmpty() && find(tSpirit.id) == null) {
                    defs.add(tSpirit);
                }
            }
        } catch (Exception e) {
            CFG.exceptionStack(e);
        }
    }

    public static void save() {
        try {
            Data tData = new Data();
            tData.nationalSpirits = new ArrayList<NationalSpirit>(list());
            FileHandle tFile = Gdx.files.local(FILE);
            tFile.writeString(json().prettyPrint(tData), false, "UTF-8");
        } catch (Exception e) {
            CFG.exceptionStack(e);
        }
    }

    public static NationalSpirit find(String nID) {
        if (nID == null || nID.isEmpty() || defs == null) {
            return null;
        }
        for (int i = 0; i < defs.size(); i++) {
            if (nID.equals(defs.get(i).id)) {
                return defs.get(i);
            }
        }
        return null;
    }

    /** Like {@link #find(String)} but triggers the lazy load first. */
    public static NationalSpirit get(String nID) {
        list();
        return find(nID);
    }

    public static int indexOf(String nID) {
        List<NationalSpirit> tDefs = list();
        for (int i = 0; i < tDefs.size(); i++) {
            if (tDefs.get(i).id != null && tDefs.get(i).id.equals(nID)) {
                return i;
            }
        }
        return -1;
    }

    /** Display name of a definition, falling back to the raw id for unknown spirits. */
    public static String displayOf(String nID) {
        NationalSpirit tSpirit = get(nID);
        return tSpirit != null ? tSpirit.displayName() : nID;
    }

    public static String groupOf(String nID) {
        NationalSpirit tSpirit = get(nID);
        return tSpirit != null && tSpirit.group != null ? tSpirit.group : "";
    }

    /** Adds a blank definition with a free id and returns that id. */
    public static String createNew() {
        List<NationalSpirit> tDefs = list();
        int n = tDefs.size() + 1;
        String tID = "ns_" + n;
        while (find(tID) != null) {
            tID = "ns_" + ++n;
        }
        NationalSpirit tSpirit = new NationalSpirit(tID);
        tSpirit.name = tID;
        tDefs.add(tSpirit);
        save();
        return tID;
    }

    /**
     * Deletes a definition. Civilizations that already own it keep their entry —
     * their snapshot still describes what to give back — so nothing drifts; the
     * spirit simply loses its name, icon and duration source.
     * <p>
     * The loaded scenario's starting-spirit table is a different matter: a row there
     * would only ever grant something that no longer exists, so it goes with it.
     */
    public static void delete(String nID) {
        List<NationalSpirit> tDefs = list();
        for (int i = 0; i < tDefs.size(); i++) {
            if (tDefs.get(i).id != null && tDefs.get(i).id.equals(nID)) {
                tDefs.remove(i);
                NSIcons.forget(nID);
                NSPresets.forgetSpirit(nID);
                save();
                return;
            }
        }
    }

    /**
     * Renames a definition's id. Ownership entries are keyed by id, so this is only
     * safe in the editor; it refuses to collide with an existing id.
     * <p>
     * The icon is keyed by id too, and its file is not renamed along — both ids are
     * dropped from the icon cache so the editor shows the truth either way. The
     * loaded scenario's starting-spirit rows follow the new id.
     */
    public static boolean rename(String nOldID, String nNewID) {
        if (nNewID == null || nNewID.isEmpty() || find(nNewID) != null) {
            return false;
        }
        NationalSpirit tSpirit = get(nOldID);
        if (tSpirit == null) {
            return false;
        }
        tSpirit.id = nNewID;
        NSIcons.forget(nOldID);
        NSIcons.forget(nNewID);
        NSPresets.renameSpirit(nOldID, nNewID);
        save();
        return true;
    }
}

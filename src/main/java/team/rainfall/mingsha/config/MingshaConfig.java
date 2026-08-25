package team.rainfall.mingsha.config;

import age.of.civilizations2.jakowski.lukasz.CFG;
import age.of.civilizations2.jakowski.lukasz.Files.FileManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

/**
 * Mingsha 模组配置文件管理类，参照游戏内置 GameValues 的标准实现，支持多个配置文件：
 *
 * <pre>
 *   GameValues.init()                              MingshaConfig.init()
 *   ------------------------------------------     ------------------------------------------
 *   Json json = new Json();                        Json json = new Json();
 *   gvDefensivePosition = json.fromJson(           data        = load(json, "game/mingsha/
 *     GV_DefensivePosition.class,                      mingshaConfig.json", MingshaConfigData.class);
 *     FileManager.loadFile("game/gameValues/       unitVoice   = load(json, "game/mingsha/
 *       gvDefensivePosition.json"));                   voConfig.json", UnitVoiceConfigData.class);
 *   gvMilitary = json.fromJson( ... );             hoi4Loading = load(json, "game/mingsha/
 *                                                     hoi4Loading.json", Hoi4LoadingConfigData.class);
 * </pre>
 *
 * <p>约定：一个 JSON 文件对应一个配置数据类（public 字段 = JSON 键、字段初始化值 = 默认值）；
 * 文件缺失、字段缺失或解析失败均回退到该数据类的 Java 默认值。新增配置时仿照 GameValues 增加一个
 * 数据类 + 一行 load() 即可。</p>
 */
public final class MingshaConfig {

    /** 通用配置文件路径。 */
    public static final String GENERAL_CONFIG_PATH = "game/mingsha/mingshaConfig.json";

    /** 部队语音配置文件路径（优先）。 */
    public static final String UNIT_VOICE_CONFIG_PATH = "game/mingsha/voConfig.json";

    /** 部队语音配置文件旧路径（兼容回退）。 */
    public static final String UNIT_VOICE_CONFIG_PATH_LEGACY = "sounds/vo/vo_config.json";

    /** HOI4 加载动画配置文件路径。 */
    public static final String HOI4_LOADING_CONFIG_PATH = "game/mingsha/hoi4Loading.json";

    /** 通用配置。字段见 {@link MingshaConfigData}。 */
    public static MingshaConfigData data = new MingshaConfigData();

    /** 部队语音配置。字段见 {@link UnitVoiceConfigData}。 */
    public static UnitVoiceConfigData unitVoice = new UnitVoiceConfigData();

    /** HOI4 加载动画参数。字段见 {@link Hoi4LoadingConfigData}。 */
    public static Hoi4LoadingConfigData hoi4Loading = new Hoi4LoadingConfigData();

    private static boolean initialized = false;

    private MingshaConfig() {
    }

    /** 加载全部配置（幂等）。每个文件独立 try/catch，单个文件损坏不影响其它文件。 */
    public static synchronized void init() {
        if (initialized) {
            return;
        }
        initialized = true;

        Json json = new Json();
        data = load(json, GENERAL_CONFIG_PATH, MingshaConfigData.class, data);
        hoi4Loading = load(json, HOI4_LOADING_CONFIG_PATH, Hoi4LoadingConfigData.class, hoi4Loading);

        // 语音配置：优先统一目录 game/mingsha/voConfig.json，缺失/损坏时回退旧路径 sounds/vo/vo_config.json
        UnitVoiceConfigData v = load(json, UNIT_VOICE_CONFIG_PATH, UnitVoiceConfigData.class, null);
        if (v == null) {
            v = load(json, UNIT_VOICE_CONFIG_PATH_LEGACY, UnitVoiceConfigData.class, null);
        }
        if (v != null) {
            unitVoice = v;
        }
    }

    /** 读取单个配置文件；文件缺失或解析失败时返回 fallback。 */
    private static <T> T load(Json json, String path, Class<T> type, T fallback) {
        try {
            FileHandle file = FileManager.loadFile(path);
            if (file.exists()) {
                return json.fromJson(type, file);
            }
        } catch (Exception e) {
            CFG.exceptionStack(e);
        }
        return fallback;
    }

    /** 惰性初始化：供无法确定 {@link #init()} 是否已被调用的调用方使用。 */
    public static void ensureInit() {
        if (!initialized) {
            init();
        }
    }

    /** HOI4 风格加载动画开关。 */
    public static boolean isHoi4LoadingEnabled() {
        ensureInit();
        return hoi4Loading != null && hoi4Loading.hoi4LoadingEnabled;
    }

    /** 是否始终启用缩放。 */
    public static boolean isAlwaysEnableScaling() {
        ensureInit();
        return data != null && data.alwaysEnableScaling;
    }

    /** 对话框是否使用新缓动动画。 */
    public static boolean isNewDialogAnimation() {
        ensureInit();
        return data != null && data.newDialogAnimation;
    }

    /** 变更领袖后是否刷新原版外交视图。 */
    public static boolean isRefreshLeaderView() {
        ensureInit();
        return data != null && data.refreshLeaderView;
    }
}

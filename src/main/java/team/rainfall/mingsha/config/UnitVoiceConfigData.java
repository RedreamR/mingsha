package team.rainfall.mingsha.config;

import java.util.HashMap;

/**
 * 部队语音（HOI4 风格）配置数据。
 *
 * <p>与 GameValues 的 {@code GV_*} 类同风格：public 字段即 JSON 键，字段初始化值即默认值。
 * 对应文件：{@code sounds/vo/vo_config.json}。</p>
 */
public class UnitVoiceConfigData {
    /** 总开关。设为 false 完全关闭部队语音。 */
    public boolean enabled = true;
    /** 语音额外音量系数（实际音量 = 音效音量 × 主音量 × 此值）。 */
    public float voiceVolume = 1.0f;
    /** 全局冷却（毫秒）。任意两条语音之间至少间隔这么久。 */
    public long globalCooldownMs = 2000L;
    /** 同类别冷却（毫秒）。 */
    public long categoryCooldownMs = 4000L;
    /** 文明 tag → 语音文件夹 的映射（键/值不区分大小写）。 */
    public HashMap<String, String> tagMapping = new HashMap<String, String>();
    /** 类别缺失时的回退映射（如 move_out → idle）。 */
    public HashMap<String, String> categoryFallback = new HashMap<String, String>();
}

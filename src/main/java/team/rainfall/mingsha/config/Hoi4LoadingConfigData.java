package team.rainfall.mingsha.config;

/**
 * HOI4 风格加载动画的界面参数。
 *
 * <p>与 GameValues 的 {@code GV_*} 类同风格：public 字段即 JSON 键，字段初始化值即默认值。
 * 对应文件：{@code game/mingsha/hoi4Loading.json}。</p>
 */
public class Hoi4LoadingConfigData {
    /** 是否使用 HOI4 风格加载动画。true = HOI4 风格；false = 回退原版加载条。 */
    public boolean hoi4LoadingEnabled = true;
    /** 面板宽度占屏幕宽度的比例（0~1）。 */
    public float panelWidthRatio = 0.49f;
    /** 底部名言轮换间隔（毫秒）。 */
    public long quoteRotationMs = 3000L;
    /** 状态文本颜色 [r, g, b, a]，各分量 0~1。 */
    public float[] statusTextColor = new float[]{0.92f, 0.92f, 0.92f, 1.0f};
    /** 名言文本颜色 [r, g, b, a]，各分量 0~1。 */
    public float[] quoteTextColor = new float[]{0.88f, 0.88f, 0.88f, 1.0f};
}

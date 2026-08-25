package team.rainfall.mingsha.config;

/**
 * Mingsha 模组运行时配置数据（通用配置）。
 *
 * <p>与游戏内置 GameValues 的 {@code GV_*} 类保持同一风格：
 * public 字段名即 JSON 键，字段初始化值即默认值。
 * libGDX {@code Json.fromJson} 只会覆盖 JSON 中出现的键，缺失键保留这里的默认值。</p>
 */
public class MingshaConfigData {
    /** 是否始终启用缩放（true 时忽略原版各菜单的缩放开关判断）。 */
    public boolean alwaysEnableScaling = true;
    /** 对话框是否使用新的时间缓动动画（false 回退原版步进动画）。 */
    public boolean newDialogAnimation = true;
    /** 事件"变更领袖"后是否刷新原版外交视图。 */
    public boolean refreshLeaderView = false;
}

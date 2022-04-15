package zxc.rich.client.features.visuals;

import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.ui.settings.impl.BooleanSetting;

public class NoRender extends Feature {
    public static BooleanSetting hurtcam = new BooleanSetting("HurtCam", true, () -> true);
    public static BooleanSetting cameraClip = new BooleanSetting("Camera Clip", true, () -> true);
    public static BooleanSetting antiTotem = new BooleanSetting("AntiTotemAnimation", false, () -> true);
    public static BooleanSetting noFire = new BooleanSetting("NoFireOverlay", false, () -> true);
    public static BooleanSetting noBossBar = new BooleanSetting("NoBossBar", false, () -> true);

    public NoRender() {
        super("NoRender", "Убирает опредленные элементы рендера в игре", FeatureCategory.VISUALS);
        addSettings(hurtcam, cameraClip, antiTotem, noFire);
    }

}

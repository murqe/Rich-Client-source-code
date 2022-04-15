package zxc.rich.client.features.misc;

import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.ui.settings.impl.NumberSetting;

public class ModuleSoundAlert extends Feature {
    public static NumberSetting volume = new NumberSetting("Volume", 1.8f, 0.1f, 2, 0.1f, () -> true);
    public static NumberSetting pitch = new NumberSetting("Pitch", 0.71999997F, 0.1f, 2, 0.1f, () -> true);
    public ModuleSoundAlert() {
        super("ModuleSoundAlert", "Звуки при включении функции и выключении", FeatureCategory.MISC);
        addSettings(volume,pitch);
    }
}

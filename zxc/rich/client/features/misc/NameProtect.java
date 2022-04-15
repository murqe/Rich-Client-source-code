package zxc.rich.client.features.misc;

import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.ui.settings.impl.BooleanSetting;

public class NameProtect extends Feature {
    public static BooleanSetting myName = new BooleanSetting("My Name", true, () -> true);
    public static BooleanSetting otherName = new BooleanSetting("Other Names", false, () -> true);
    public static BooleanSetting tabSpoof = new BooleanSetting("Tab Spoof", false, () -> true);
    public static BooleanSetting scoreboardSpoof = new BooleanSetting("Scoreboard Spoof", true, () -> true);

    public NameProtect() {
        super("NameProtect", "Позволяет скрывать информацию о себе и других игроках", FeatureCategory.MISC);
        addSettings(myName, otherName, tabSpoof, scoreboardSpoof);
    }
}

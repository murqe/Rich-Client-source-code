package zxc.rich.client.features.player;

import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.ui.settings.impl.BooleanSetting;

public class NoPush extends Feature {
    public static BooleanSetting water;
    public static BooleanSetting players;
    public static BooleanSetting blocks;

    public NoPush() {
        super("NoPush", "Убирает отталкивание от игроков, воды и блоков", FeatureCategory.PLAYER);
        players = new BooleanSetting("Entity", true, () -> true);
        water = new BooleanSetting("Water", true, () -> true);
        blocks = new BooleanSetting("Blocks", true, () -> true);
        addSettings(players, water, blocks);
    }
}

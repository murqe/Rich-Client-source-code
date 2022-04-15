package zxc.rich.client.features.hud;

import zxc.rich.Main;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.ui.settings.impl.BooleanSetting;
import zxc.rich.client.ui.settings.impl.ListSetting;

public class ClientFont extends Feature {
    public static ListSetting fontMode;
    public static BooleanSetting minecraftfont;

    public ClientFont() {
        super("ClientFont", "Меняет шрифт во всем клиенте", FeatureCategory.HUD);
        fontMode = new ListSetting("FontList", "SFUI", () -> !minecraftfont.getBoolValue(), "URWGeometric", "Myseo", "SFUI", "Lato", "Tenacity","Roboto Regular");
        minecraftfont = new BooleanSetting("Minecraft Font", false, () -> true);
        addSettings(fontMode, minecraftfont);
    }

    @Override
    public void onEnable() {
        Main.instance.featureDirector.getFeature(ClientFont.class).setEnabled(false);
        super.onEnable();
    }
}

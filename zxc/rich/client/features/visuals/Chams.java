package zxc.rich.client.features.visuals;

import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.ui.settings.impl.BooleanSetting;
import zxc.rich.client.ui.settings.impl.ColorSetting;
import zxc.rich.client.ui.settings.impl.ListSetting;

import java.awt.*;

public class Chams extends Feature {

    public static ColorSetting colorChams;
    public static BooleanSetting clientColor;

    public Chams() {
        super("Chams", "Подсвечивает игроков", FeatureCategory.VISUALS);
        clientColor = new BooleanSetting("Client Colored", false, () -> true);
        colorChams = new ColorSetting("Chams Color", new Color(0xFFFFFF).getRGB(), () -> !clientColor.getBoolValue());
        addSettings(colorChams, clientColor);
    }
}
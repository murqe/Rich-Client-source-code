package zxc.rich.client.features.visuals;

import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.ui.settings.impl.ColorSetting;

import java.awt.*;

public class Snow extends Feature {
    public static ColorSetting weatherColor;

    public Snow() {
        super("Snow", "Добавляет частички снега в мир", FeatureCategory.VISUALS);
        weatherColor = new ColorSetting("Weather",new Color(0xFFFFFF).getRGB(), () -> true);
        addSettings(weatherColor);
    }
}

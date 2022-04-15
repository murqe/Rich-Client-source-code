package zxc.rich.client.features.visuals;

import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.ui.settings.impl.ColorSetting;

import java.awt.*;

public class HitColor extends Feature {

    public static ColorSetting hitColor = new ColorSetting("Hit Color", Color.RED.getRGB(), () -> true);

    public HitColor() {
        super("HitColor", "Изменяет чамсы энтити при ударе" , FeatureCategory.VISUALS);
        addSettings(hitColor);
    }
}

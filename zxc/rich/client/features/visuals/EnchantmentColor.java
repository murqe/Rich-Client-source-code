package zxc.rich.client.features.visuals;

import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.ui.settings.impl.ColorSetting;
import zxc.rich.client.ui.settings.impl.ListSetting;

import java.awt.*;

public class EnchantmentColor extends Feature {

    public static ListSetting colorMode;
    public static ColorSetting customColor;

    public EnchantmentColor() {
        super("EnchantmentColor", "Изменяет цвет зачарований", FeatureCategory.VISUALS);
        colorMode = new ListSetting("Crumbs Color", "Rainbow", () -> true, "Rainbow", "Client", "Custom");
        customColor = new ColorSetting("Custom Enchantment", new Color(0xFFFFFF).getRGB(), () -> colorMode.currentMode.equals("Custom"));
        addSettings(colorMode, customColor);
    }
}

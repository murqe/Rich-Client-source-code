package zxc.rich.client.features.hud;

import org.lwjgl.input.Keyboard;
import zxc.rich.Main;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.ui.settings.impl.BooleanSetting;
import zxc.rich.client.ui.settings.impl.ColorSetting;
import zxc.rich.client.ui.settings.impl.ListSetting;
import zxc.rich.client.ui.settings.impl.NumberSetting;

import java.awt.*;

public class ClickGUI extends Feature {
    public static BooleanSetting girl = new BooleanSetting("Girl",true,() -> true);

    public static BooleanSetting blur = new BooleanSetting("Blur",true,() -> true);
    public static NumberSetting blurInt = new NumberSetting("Blur Amount",1,1,10,1,() -> blur.getBoolValue());

    public static ListSetting clickGuiColor = new ListSetting("ClickGui Color", "Astolfo", () -> true, "Astolfo", "Rainbow", "Static");
    public static ListSetting backGroundColor = new ListSetting("Background Color", "Astolfo", () -> true, "Astolfo", "Rainbow", "Static");

    public static ColorSetting color;
    public static ColorSetting bgcolor;
    public static NumberSetting speed = new NumberSetting("Speed", 35, 10, 100, 1, () -> true);

    public ClickGUI() {
        super("ClickGUI", FeatureCategory.HUD);
        setKey(Keyboard.KEY_RSHIFT);
        color = new ColorSetting("Color One", new Color(255, 255, 255, 120).getRGB(), () ->  clickGuiColor.currentMode.equals("Static"));
        bgcolor = new ColorSetting("Color One", new Color(255, 255, 255, 120).getRGB(), () -> backGroundColor.currentMode.equals("Static"));
        addSettings(clickGuiColor, color, speed,backGroundColor,bgcolor,blur,blurInt,girl);

    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(Main.instance.clickGui);
        Main.instance.featureDirector.getFeature(ClickGUI.class).setEnabled(false);
        super.onEnable();
    }
}

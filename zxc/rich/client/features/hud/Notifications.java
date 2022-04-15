package zxc.rich.client.features.hud;

import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.ui.settings.impl.BooleanSetting;
import zxc.rich.client.ui.settings.impl.ListSetting;

public class Notifications extends Feature {
    public static ListSetting notifMode;

    public static BooleanSetting state;

    public Notifications() {
        super("Notifications", "Показывает необходимую информацию о модулях", FeatureCategory.HUD);
        state = new BooleanSetting("Module State", true, () -> true);
        notifMode = new ListSetting("Notification Mode", "Rect", () -> true, "Rect", "Chat");
        addSettings(notifMode,state);
    }
}
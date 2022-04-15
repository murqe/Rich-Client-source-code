package zxc.rich.client.features.hud;

import zxc.rich.Main;
import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.Event2D;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.ui.draggable.DraggableModule;
import zxc.rich.client.ui.settings.impl.BooleanSetting;

public class InfoDisplay extends Feature {
    public static BooleanSetting potion = new BooleanSetting("Potion Status", true, () -> true);
    public static BooleanSetting clientInfo = new BooleanSetting("Client Info", true, () -> true);
    public static BooleanSetting coordsInfo = new BooleanSetting("Coordinates", true, () -> clientInfo.getBoolValue());
    public static BooleanSetting fps = new BooleanSetting("FPS", true, () -> clientInfo.getBoolValue());
    public static BooleanSetting sessionInfo = new BooleanSetting("Session Info", true, () -> true);
    public InfoDisplay() {
        super("InfoInterface", "blob.", FeatureCategory.HUD);
        addSettings(clientInfo,coordsInfo,fps, sessionInfo, potion);
    }

    @EventTarget
    public void onRender(Event2D event2D) {
        for (DraggableModule draggableModule : Main.instance.draggableManager.getMods()) {
            if (isEnabled() && !draggableModule.name.equals("TargetHUDComponent")) {
                draggableModule.draw();
            }
        }
    }
}

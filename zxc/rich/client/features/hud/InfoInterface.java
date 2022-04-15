package zxc.rich.client.features.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import zxc.rich.Main;
import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.Event2D;
import zxc.rich.api.utils.world.TimerHelper;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.ui.draggable.DraggableModule;

public class InfoInterface extends Feature {
    public static String clientName;
    public static float count = 0.0F;
    public TimerHelper timer;
    public static float globalOffset;
    public InfoInterface() {
        super("InfoInterface", "Показывает информацию на экране", FeatureCategory.HUD);
    }

    @EventTarget
    public void onRender(Event2D event2D) {
        for (DraggableModule draggableModule : Main.instance.draggableManager.getMods()) {
            if (isEnabled() && !draggableModule.name.equals("TargetHUDComponent")) {
                draggableModule.draw();
            }
        }
        float target = (mc.currentScreen instanceof GuiChat) ? 15 : 0;
        float delta = globalOffset - target;
        globalOffset -= delta / Math.max(1, Minecraft.getDebugFPS()) * 10;
        if (!Double.isFinite(globalOffset)) {
            globalOffset = 0;
        }
        if (globalOffset > 15) {
            globalOffset = 15;
        }
        if (globalOffset < 0) {
            globalOffset = 0;
        }
    }
}

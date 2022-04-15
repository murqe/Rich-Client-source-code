package zxc.rich.client.features.misc;

import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.EventPreMotionUpdate;
import zxc.rich.api.event.events.impl.EventUpdate;
import zxc.rich.api.utils.world.TimerHelper;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.ui.settings.impl.ListSetting;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Disabler extends Feature {
    public ListSetting disablerMode = new ListSetting("Disabler Mode", "Old Matrix", () -> true, "Old Matrix", "Storm Movement");

    public Disabler() {
        super("Disabler", "Ослабляет воздействие античитов на вас", FeatureCategory.MISC);
        addSettings(disablerMode);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        this.setSuffix(disablerMode.currentMode);
        if (disablerMode.currentMode.equals("Matrix Old")) {
            if (mc.player.ticksExisted % 6 == 0) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
            }
        }
    }

    @EventTarget
    public void onPreUpdate(EventPreMotionUpdate event) {
        if (disablerMode.currentMode.equals("Storm Movement")) {
            event.setOnGround(false);
        }
    }
}

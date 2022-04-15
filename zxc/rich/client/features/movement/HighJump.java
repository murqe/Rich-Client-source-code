package zxc.rich.client.features.movement;

import net.minecraft.network.play.client.CPacketEntityAction;
import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.EventPreMotionUpdate;
import zxc.rich.api.event.events.impl.EventUpdate;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;

public class HighJump extends Feature {
    public HighJump() {
        super("HighJump", "Подкидывает вас высоко вверх", FeatureCategory.MOVEMENT);
    }

    @EventTarget
    public void onPreMotion(EventPreMotionUpdate eventPreMotionUpdate) {
        if (mc.player.hurtTime > 0) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
            mc.player.motionY += 150;
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        }
    }
}

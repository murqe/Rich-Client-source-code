package zxc.rich.client.features.movement;

import net.minecraft.network.play.server.SPacketPlayerPosLook;
import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.EventPreMotionUpdate;
import zxc.rich.api.event.events.impl.EventReceivePacket;
import zxc.rich.api.utils.movement.MovementUtils;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.ui.settings.impl.BooleanSetting;
import zxc.rich.client.ui.settings.impl.NumberSetting;

public class Spider extends Feature {

    public NumberSetting climbTicks = new NumberSetting("Climb Speed", 1, 0, 5, 0.1F, () -> true);

    public Spider() {
        super("Spider", "Позволяет забираться вверх по стенам", FeatureCategory.MOVEMENT);
        addSettings(climbTicks);

    }



    @EventTarget
    public void onPreMotion(EventPreMotionUpdate event) {
        this.setSuffix("" + climbTicks.getNumberValue());
        if (MovementUtils.isMoving() && mc.player.isCollidedHorizontally) {
            if (timerHelper.hasReached(climbTicks.getNumberValue() * 100)) {
                event.setOnGround(true);
                mc.player.jump();
                timerHelper.reset();
            }
        }
    }
}

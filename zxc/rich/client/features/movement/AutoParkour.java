package zxc.rich.client.features.movement;

import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.EventUpdate;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;

public class AutoParkour extends Feature {
    public AutoParkour() {
        super("AutoParkour", "Автоматически прыгает с конца блока", FeatureCategory.MOVEMENT);
    }
    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0, -0.5, 0).expand(-0.001, 0, -0.001)).isEmpty() && mc.player.onGround && !mc.gameSettings.keyBindJump.isKeyDown()) {
            mc.player.jump();
        }
    }
}

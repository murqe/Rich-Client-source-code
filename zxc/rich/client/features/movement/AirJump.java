package zxc.rich.client.features.movement;

import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.EventUpdate;
import zxc.rich.api.utils.movement.MovementUtils;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;

public class AirJump extends Feature {

    public AirJump() {
        super("AirJump", "Позволяет прыгать по воздуху", FeatureCategory.MOVEMENT);
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        if(mc.gameSettings.keyBindJump.isKeyDown()) {
                mc.player.jump();
        }
    }
}

package zxc.rich.client.features.movement;

import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.EventUpdate;
import zxc.rich.api.utils.movement.MovementUtils;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;

public class Strafe extends Feature {
    public Strafe() {
        super("Strafe", "ׂû למזורü סענויפטעüסÿ", FeatureCategory.MOVEMENT);
    }

    @EventTarget
    public void onPlayerTick(EventUpdate e) {
        if (!mc.gameSettings.keyBindForward.pressed)
            return;
        if(!MovementUtils.isMoving())
            return;

        MovementUtils.strafe(MovementUtils.getSpeed() - 0.0135F);
    }
}
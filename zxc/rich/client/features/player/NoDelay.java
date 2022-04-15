package zxc.rich.client.features.player;

import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.EventUpdate;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.ui.settings.impl.BooleanSetting;

public class NoDelay extends Feature {
    private final BooleanSetting rightClickDelay = new BooleanSetting("NoRightClickDelay", true, () -> true);
    private final BooleanSetting jumpDelay = new BooleanSetting("NoJumpDelay", true, () -> true);

    public NoDelay() {
        super("NoDelay", "Убирает задержку", FeatureCategory.PLAYER);
        addSettings(rightClickDelay, jumpDelay);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (rightClickDelay.getBoolValue()) {
            mc.rightClickDelayTimer = 0;
        }

        if (jumpDelay.getBoolValue()) {
            mc.player.jumpTicks = 0;
        }
    }

    @Override
    public void onDisable() {
        mc.rightClickDelayTimer = 4;
        mc.player.jumpTicks = 6;
        super.onDisable();
    }
}
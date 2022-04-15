package zxc.rich.client.features.player;

import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.EventUpdate;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.ui.settings.impl.ListSetting;
import zxc.rich.client.ui.settings.impl.NumberSetting;

public class NoSlowDown extends Feature {
    public static NumberSetting percentage = new NumberSetting("Percentage", 100, 0, 100, 1, () -> true, NumberSetting.NumberType.PERCENTAGE);
    private final ListSetting noSlowMode = new ListSetting("NoSlow Mode", "Matrix", () -> true, "Vanilla", "Matrix");
    public int usingTicks;

    public NoSlowDown() {
        super("NoSlowDown", "Убирает замедление при использовании еды и других предметов", FeatureCategory.PLAYER);
        addSettings(noSlowMode,percentage);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        this.setSuffix(noSlowMode.getCurrentMode() + ", " + percentage.getNumberValue() + "%");
        usingTicks = mc.player.isUsingItem() ? ++usingTicks : 0;
        if (!this.isEnabled() || !mc.player.isUsingItem()) {
            return;
        }
        if (noSlowMode.currentMode.equals("Matrix")) {
            if (mc.player.isUsingItem()) {
                if (mc.player.onGround && !mc.gameSettings.keyBindJump.isKeyDown()) {
                    if (mc.player.ticksExisted % 2 == 0) {
                        mc.player.motionX *= 0.35;
                        mc.player.motionZ *= 0.35;
                    }
                } else if (mc.player.fallDistance > 0.2) {
                    mc.player.motionX *= 0.9100000262260437;
                    mc.player.motionZ *= 0.9100000262260437;
                }
            }
        }
    }
}

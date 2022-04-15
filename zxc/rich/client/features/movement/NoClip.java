package zxc.rich.client.features.movement;

import net.minecraft.entity.Entity;
import zxc.rich.Main;
import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.EventUpdate;
import zxc.rich.api.utils.movement.MovementUtils;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.ui.settings.impl.BooleanSetting;
import zxc.rich.client.ui.settings.impl.NumberSetting;

public class NoClip extends Feature {
    public static NumberSetting speed;
    public static BooleanSetting customSpeed;

    public NoClip() {
        super("NoClip", "Позволяет ходить сквозь стены", FeatureCategory.PLAYER);
        customSpeed = new BooleanSetting("Custom Speed", false, () -> true);
        speed = new NumberSetting("Speed", 0.02F, 0F, 2, 0.01F, () -> customSpeed.getBoolValue());
        addSettings(customSpeed, speed);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.player != null) {
            mc.player.noClip = true;
            mc.player.motionY = 0.00001;
            if (customSpeed.getBoolValue()) {
                MovementUtils.setSpeed(speed.getNumberValue() == 0 ? MovementUtils.getSpeed() : speed.getNumberValue());
            }
            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                mc.player.motionY = 0.4;
            }
            if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                mc.player.motionY = -0.4;
            }
        }
    }


    public static boolean isNoClip(Entity entity) {
        if (Main.instance.featureDirector.getFeature(NoClip.class).isEnabled() && mc.player != null
                && (mc.player.ridingEntity == null || entity == mc.player.ridingEntity))
            return true;
        return entity.noClip;

    }

    public void onDisable() {
        mc.player.noClip = false;
        super.onDisable();
    }
}

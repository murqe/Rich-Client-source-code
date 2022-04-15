package zxc.rich.client.features.combat;


import net.minecraft.entity.EntityLivingBase;
import zxc.rich.Main;
import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.EventMove;
import zxc.rich.api.event.events.impl.EventPreMotionUpdate;
import zxc.rich.api.event.events.impl.EventUpdate;
import zxc.rich.api.utils.combat.RotationHelper;
import zxc.rich.api.utils.movement.MovementUtils;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.ui.settings.impl.BooleanSetting;
import zxc.rich.client.ui.settings.impl.NumberSetting;

public class    TargetStrafe extends Feature {
    public NumberSetting tstrafeRange;
    public NumberSetting spd;
    public BooleanSetting autoJump;


    public static int direction = -1;

    public TargetStrafe() {
        super("TargetStrafe", "Стрефит вокруг энтети", FeatureCategory.COMBAT);
        tstrafeRange = new NumberSetting("Strafe Distance", "Дистанция по которой вы будите стрейфить", 2.4F, 0.1F, 6.0F, 0.1F, () -> true);
        spd = new NumberSetting("Strafe Speed", 0.23F, 0.1F, 2, 0.01F, () -> true);
        autoJump = new BooleanSetting("AutoJump", true, () -> true);
        addSettings(tstrafeRange, spd, autoJump);
    }

    @EventTarget
    public void onMotionUpdate(EventMove e) {
        EntityLivingBase entity = KillAura.target;
        float[] rotations = RotationHelper.getTargetRotations(entity);
        if (mc.player.getDistanceToEntity(entity) <= tstrafeRange.getNumberValue()) {
            MovementUtils.setMotion(e, spd.getNumberValue(), rotations[0], direction, 0.0D);
        } else {
            MovementUtils.setMotion(e, spd.getNumberValue(), rotations[0], direction, 1.0D);
        }
    }

    @EventTarget
    public void onUpdate(EventPreMotionUpdate event) {
        if (mc.player.isCollidedHorizontally)
            switchDirection();
        if (mc.gameSettings.keyBindLeft.isPressed())
            direction = 1;
        if (mc.gameSettings.keyBindRight.isPressed())
            direction = -1;
        if (KillAura.target.getHealth() > 0) {
            if (autoJump.getBoolValue() && Main.instance.featureDirector.getFeature(KillAura.class).isEnabled() && Main.instance.featureDirector.getFeature(TargetStrafe.class).isEnabled()) {
                if (mc.player.onGround) {
                    mc.player.jump();
                }
            }
        }
    }

    @EventTarget
    public void onSwitchDir(EventUpdate event) {
        if (KillAura.target == null)
            return;
        if (mc.player.isCollidedHorizontally)
            switchDirection();
        if (mc.gameSettings.keyBindLeft.isKeyDown())
            direction = 1;
        if (mc.gameSettings.keyBindRight.isKeyDown())
            direction = -1;
    }

    private void switchDirection() {
        direction = direction == 1 ? -1 : 1;
    }

    @EventTarget
    public void onMove(EventMove e) {
        if (Main.instance.featureDirector.getFeature(KillAura.class).isEnabled() && KillAura.target != null && KillAura.target.getHealth() > 0) {
            if (mc.player.isCollidedHorizontally)
                switchDirection();
            if (KillAura.target.getHealth() > 0.0F)
                onMotionUpdate(e);

        }
    }
}



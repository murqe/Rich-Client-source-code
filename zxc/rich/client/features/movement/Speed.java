package zxc.rich.client.features.movement;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import zxc.rich.Main;
import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.EventPreMotionUpdate;
import zxc.rich.api.event.events.impl.EventReceivePacket;
import zxc.rich.api.event.events.impl.EventUpdate;
import zxc.rich.api.utils.movement.MovementUtils;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.ui.notification.NotificationMode;
import zxc.rich.client.ui.notification.NotificationRenderer;
import zxc.rich.client.ui.settings.impl.BooleanSetting;
import zxc.rich.client.ui.settings.impl.ListSetting;
import zxc.rich.client.ui.settings.impl.NumberSetting;

public class Speed extends Feature {
    private float ticks = 35;
    private final ListSetting speedMode = new ListSetting("Speed Mode", "Matrix", () -> true, "Matrix", "Matrix Long", "SunriseDamage","MatrixDamage");

    public Speed() {
        super("Speed", "Увеличивает вашу скорость", FeatureCategory.MOVEMENT);
        addSettings(speedMode);

    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        String modezxc = speedMode.getOptions();
        this.setSuffix("" + modezxc);
    }

    @EventTarget
    public void onPreMotion(EventPreMotionUpdate event) {
        String mode = speedMode.getOptions();
        if (mode.equalsIgnoreCase("Matrix")) {
            if (mc.player.isInWeb || mc.player.isOnLadder() || mc.player.isInLiquid()) {
                return;
            }
            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                return;
            }
            final double x = mc.player.posX;
            final double y = mc.player.posY;
            final double z = mc.player.posZ;
            final double yaw = mc.player.rotationYaw * 0.017453292;
            if (mc.player.onGround) {
                mc.player.jump();
                mc.timer.timerSpeed = 1.3f;
                this.ticks = 11;
            } else if (this.ticks < 11) {
                ++this.ticks;
            }
            if (mc.player.motionY == -0.4448259643949201) {
                mc.player.motionX *= 2.05f;
                mc.player.motionZ *= 2.05f;
                mc.player.setPosition(x - Math.sin(yaw) * 0.003, y, z + Math.cos(yaw) * 0.003);
            }
            this.ticks = 0;

        } else if (mode.equalsIgnoreCase("Matrix Long")) {
            if (mc.player.isInWeb || mc.player.isOnLadder() || mc.player.isInLiquid()) {
                return;
            }
            if (mc.player.onGround) {
                this.ticks = 11;
            } else if (this.ticks < 11) {
                ++this.ticks;
            }
            if (mc.player.motionY == -0.4448259643949201) {
                mc.player.motionX *= 2f;
                mc.player.motionZ *= 2f;
            }
            this.ticks = 0;
        } else if (mode.equalsIgnoreCase("SunriseDamage")) {
            if (MovementUtils.isMoving()) {
                if (mc.player.onGround) {
                    mc.player.addVelocity(-Math.sin(MovementUtils.getAllDirection()) * 9.5 / 24.5, 0, Math.cos(MovementUtils.getAllDirection()) * 9.5 / 24.5);
                    MovementUtils.strafe();
                } else if (mc.player.isInWater()) {
                    mc.player.addVelocity(-Math.sin(MovementUtils.getAllDirection()) * 8.5 / 24.5, 0, Math.cos(MovementUtils.getAllDirection()) * 9.5 / 24.5);
                    MovementUtils.strafe();
                } else if (!mc.player.onGround) {
                    mc.player.addVelocity(-Math.sin(MovementUtils.getAllDirection()) * 0.11 / 24.5, 0, Math.cos(MovementUtils.getAllDirection()) * 0.11 / 24.5);
                    MovementUtils.strafe();
                } else {
                    mc.player.addVelocity(-Math.sin(MovementUtils.getAllDirection()) * 0.005 * MovementUtils.getSpeed(), 0, Math.cos(MovementUtils.getAllDirection()) * 0.005 * MovementUtils.getSpeed());
                    MovementUtils.strafe();

                }
            }
        } else if (mode.equalsIgnoreCase("MatrixDamage")) {
              if(mc.gameSettings.keyBindJump.pressed && mc.gameSettings.keyBindForward.pressed) {
                  return;
              }
            if (MovementUtils.isMoving()) {
                    mc.player.addVelocity(-Math.sin(MovementUtils.getAllDirection()) * 20 / 24.5, 0, Math.cos(MovementUtils.getAllDirection()) * 20 / 24.5);
                    MovementUtils.strafe();

                }
            }
    }
    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0f;
        super.onDisable();
    }
}
package zxc.rich.client.features.movement;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Keyboard;
import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.EventPreMotionUpdate;
import zxc.rich.api.event.events.impl.EventUpdate;
import zxc.rich.api.utils.movement.MovementUtils;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.ui.settings.impl.BooleanSetting;
import zxc.rich.client.ui.settings.impl.ListSetting;
import zxc.rich.client.ui.settings.impl.NumberSetting;

public class
Flight extends Feature {
    public final ListSetting flyMode = new ListSetting("Flight Mode", "Matrix Glide", () -> true, "Vanilla", "Matrix Glide", "Matrix Pearl", "Matrix Pearl New", "Matrix Web");
    public final NumberSetting speed = new NumberSetting("Flight Speed", 5F, 0.1F, 15F, 0.1F, () -> true);

    public Flight() {
        super("Flight", "Позволяет летать без креатив режима", FeatureCategory.MOVEMENT);
        addSettings(flyMode, speed);
    }


    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        String mode = flyMode.getOptions();
        if (mode.equalsIgnoreCase("Matrix Pearl New")) {
                if (mc.player.ticksExisted % 1.5 == 0) {
                        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
                    }
                    if (MovementUtils.isMoving()) {
                        mc.player.jump();
                        mc.player.addVelocity(-Math.sin(MovementUtils.getAllDirection()) * 15.5 / 24.5, 0, Math.cos(MovementUtils.getAllDirection()) * 15.5 / 24.5);
                        MovementUtils.strafe();
                    }
                    if (mc.player.ticksExisted % 1.5 == 0) {
                        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                    }
                }
        }
    @EventTarget
    public void onPreUpdate(EventPreMotionUpdate event) {
        String mode = flyMode.getOptions();
        setSuffix("" + mode);
        if (mode.equalsIgnoreCase("Matrix Glide")) {
            if (mc.player.onGround) {
                mc.player.jump();
                timerHelper.reset();
            } else if (!mc.player.onGround && timerHelper.hasReached(280)) {
                mc.player.motionY = -0.1D;

                if (mc.gameSettings.keyBindJump.pressed) {
                    mc.player.motionY = 2.0;
                }

                if (mc.gameSettings.keyBindSneak.pressed) {
                    mc.player.motionY = -2.0;
                }

                MovementUtils.setSpeed(speed.getNumberValue());
            }
        } else if (mode.equalsIgnoreCase("Vanilla")) {
            mc.player.capabilities.isFlying = true;
            mc.player.capabilities.allowFlying = true;

            if (mc.gameSettings.keyBindJump.pressed) {
                mc.player.motionY = 2.0;
            }

            if (mc.gameSettings.keyBindSneak.pressed) {
                mc.player.motionY = -2.0;
            }
            MovementUtils.setSpeed(speed.getNumberValue());
        } else if (mode.equalsIgnoreCase("Matrix Pearl")) {
            if (mc.player.hurtTime > 0) {
                mc.player.motionY += 0.13;
                if (mc.gameSettings.keyBindForward.pressed && !mc.player.onGround) {
                    mc.player.motionX -= MathHelper.sin((float) Math.toRadians(mc.player.rotationYaw));
                    mc.player.motionZ += MathHelper.cos((float) Math.toRadians(mc.player.rotationYaw));
                }
            }
        } else if (mode.equalsIgnoreCase("Matrix Web")) {

            if (mc.player.isInWeb) {
                mc.player.isInWeb = false;
                mc.player.motionY *= mc.player.ticksExisted % 2 == 0 ? -100 : -0.05;
            }
        }

    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.player.capabilities.isFlying = false;
        mc.timer.timerSpeed = 1f;
    }
}
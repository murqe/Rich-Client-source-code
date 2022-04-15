package zxc.rich.api.utils.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import zxc.rich.api.event.events.impl.EventMove;
import zxc.rich.api.utils.Helper;

public class MovementUtils implements Helper {

    public static boolean isMoving() {
        return mc.player.movementInput.moveStrafe != 0.0 || mc.player.movementInput.moveForward != 0.0;
    }
    public static void setEventSpeed(EventMove event, double speed) {
        double forward = mc.player.movementInput.moveForward;
        double strafe = mc.player.movementInput.moveStrafe;
        float yaw = mc.player.rotationYaw;
        if (forward == 0 && strafe == 0) {
            event.setX(0);
            event.setZ(0);
        } else {
            if (forward != 0) {
                if (strafe > 0) {
                    yaw += (forward > 0 ? -45 : 45);
                } else if (strafe < 0) {
                    yaw += (forward > 0 ? 45 : -45);
                }
                strafe = 0;
                if (forward > 0) {
                    forward = 1;
                } else if (forward < 0) {
                    forward = -1;
                }
            }
            event.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F)));
            event.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F)));
        }
    }
    public static void setSpeed(double speed) {
        float f = mc.player.movementInput.moveForward;
        float f1 = mc.player.movementInput.moveStrafe;
        float f2 = mc.player.rotationYaw;

        if (f == 0.0F && f1 == 0.0F) {
            mc.player.motionX = 0.0D;
            mc.player.motionZ = 0.0D;
        } else if (f != 0.0F) {
            if (f1 >= 1.0F) {
                f2 += (f > 0.0F ? -35 : 35);
                f1 = 0.0F;
            } else if (f1 <= -1.0F) {
                f2 += (f > 0.0F ? 35 : -35);
                f1 = 0.0F;
            }

            if (f > 0.0F) {
                f = 1.0F;
            } else if (f < 0.0F) {
                f = -1.0F;
            }
        }

        double d0 = Math.cos(Math.toRadians(f2 + 90.0F));
        double d1 = Math.sin(Math.toRadians(f2 + 90.0F));
        mc.player.motionX = f * speed * d0 + f1 * speed * d1;
        mc.player.motionZ = f * speed * d1 - f1 * speed * d0;
    }

    public static void setMotion(EventMove e, double speed, float pseudoYaw, double aa, double po4) {
        double forward = po4;
        double strafe = aa;
        float yaw = pseudoYaw;
        if (po4 != 0.0) {
            if (aa > 0.0) {
                yaw = pseudoYaw + (float) (po4 > 0.0 ? -45 : 45);
            } else if (aa < 0.0) {
                yaw = pseudoYaw + (float) (po4 > 0.0 ? 45 : -45);
            }
            strafe = 0.0;
            if (po4 > 0.0) {
                forward = 1.0;
            } else if (po4 < 0.0) {
                forward = -1.0;
            }
        }
        if (strafe > 0.0) {
            strafe = 1.0;
        } else if (strafe < 0.0) {
            strafe = -1.0;
        }
        double kak = Math.cos(Math.toRadians(yaw + 90.0f));
        double nety = Math.sin(Math.toRadians(yaw + 90.0f));
        e.setX(forward * speed * kak + strafe * speed * nety);
        e.setZ(forward * speed * nety - strafe * speed * kak);
    }
    public static boolean isBlockAboveHead() {
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(mc.player.posX - 0.3, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ + 0.3, mc.player.posX + 0.3, mc.player.posY + (!mc.player.onGround ? 1.5 : 2.5), mc.player.posZ - 0.3);
        return mc.world.getCollisionBoxes(mc.player, axisAlignedBB).isEmpty();
    }

    public static float getMoveDirection() {
        double motionX = mc.player.motionX;
        double motionZ = mc.player.motionZ;
        float direction = (float) (Math.atan2(motionX, motionZ) / Math.PI * 180);
        return -direction;
    }

    public static float getDirection2() {
        float rotationYaw = mc.player.rotationYaw;
        if (mc.player.moveForward < 0.0f) {
            rotationYaw += 180.0f;
        }
        float forward = 1.0f;
        if (mc.player.moveForward < 0.0f) {
            forward = -50.5f;
        } else if (mc.player.moveForward > 0.0f) {
            forward = 50.5f;
        }
        if (mc.player.moveStrafing > 0.0f) {
            rotationYaw -= 22.0f * forward;
        }
        if (mc.player.moveStrafing < 0.0f) {
            rotationYaw += 22.0f * forward;
        }
        return rotationYaw *= (float) Math.PI / 180;
    }


    public static void strafe() {
        if (mc.gameSettings.keyBindBack.isKeyDown()) {
            return;
        }
        MovementUtils.strafe(MovementUtils.getSpeed());
    }

    public static void strafe(float speed) {
        if (!MovementUtils.isMoving()) {
            return;
        }
        double yaw = MovementUtils.getAllDirection();
        mc.player.motionX = -Math.sin(yaw) * (double) speed;
        mc.player.motionZ = Math.cos(yaw) * (double) speed;
    }

    public static float getSpeed() {
        return (float) Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ);
    }
    public static float getAllDirection() {
        float rotationYaw = mc.player.rotationYaw;

        float factor = 0f;

        if (mc.player.movementInput.moveForward > 0)
            factor = 1;
        if (mc.player.movementInput.moveForward < 0)
            factor = -1;

        if (factor == 0) {
            if (mc.player.movementInput.moveStrafe > 0)
                rotationYaw -= 90;

            if (mc.player.movementInput.moveStrafe < 0)
                rotationYaw += 90;
        } else {
            if (mc.player.movementInput.moveStrafe > 0)
                rotationYaw -= 45 * factor;

            if (mc.player.movementInput.moveStrafe < 0)
                rotationYaw += 45 * factor;
        }

        if (factor < 0)
            rotationYaw -= 180;

        return (float) Math.toRadians(rotationYaw);
    }
    public static double getDirection() {
        float rotationYaw = mc.player.rotationYaw;
        float forward = 1f;
        if (mc.player.moveForward < 0f)
            rotationYaw += 180f;
        if (mc.player.moveForward < 0f)
            forward = -0.5f;
        else if (mc.player.moveForward > 0f)
            forward = 0.5f;
        if (mc.player.moveStrafing > 0f)
            rotationYaw -= 40f * forward;
        if (mc.player.moveStrafing < 0f)
            rotationYaw += 40f * forward;
        return Math.toRadians(rotationYaw);
    }


}
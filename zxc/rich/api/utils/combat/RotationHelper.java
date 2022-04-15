package zxc.rich.api.utils.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.EventPlayerState;
import zxc.rich.api.event.events.impl.EventSendPacket;
import zxc.rich.api.utils.Helper;
import zxc.rich.api.utils.math.MathematicHelper;
import zxc.rich.api.utils.movement.MovementUtils;

public class RotationHelper implements Helper {
    public static float[] lastRotations;

    public static boolean isLookingAtEntity(float yaw, float pitch, float xExp, float yExp, float zExp, Entity entity, double range) {
        Vec3d src = mc.player.getPositionEyes(1.0F);
        Vec3d vectorForRotation = Entity.getVectorForRotation(pitch, yaw);
        Vec3d dest = src.addVector(vectorForRotation.xCoord * range, vectorForRotation.yCoord * range, vectorForRotation.zCoord * range);
        RayTraceResult rayTraceResult = mc.world.rayTraceBlocks(src, dest, false, false, true);
        if (rayTraceResult == null) {
            return false;
        }
        return (entity.getEntityBoundingBox().expand(xExp, yExp, zExp).calculateIntercept(src, dest) != null);
    }

    public static boolean isAimAtMe(Entity entity, float breakRadius) {
        float entityYaw = MathHelper.wrapDegrees(entity.rotationYaw);
        return Math.abs(MathHelper.wrapDegrees(getYawToEntity(entity, mc.player) - entityYaw)) <= breakRadius;
    }

    public static Vec3d getEyesPos() {
        return new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ);
    }
    public static float[] getRotationVector(Vec3d vec, boolean randomRotation, float yawRandom, float pitchRandom, float speedRotation) {
        Vec3d eyesPos = getEyesPos();
        double diffX = vec.xCoord - eyesPos.xCoord;
        double diffY = vec.yCoord - (mc.player.posY + mc.player.getEyeHeight() + 0.5);
        double diffZ = vec.zCoord - eyesPos.zCoord;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

        float randomYaw = 0;
        if (randomRotation) {
            randomYaw = MathematicHelper.randomizeFloat(-yawRandom, yawRandom);
        }
        float randomPitch = 0;
        if (randomRotation) {
            randomPitch = MathematicHelper.randomizeFloat(-pitchRandom, pitchRandom);
        }

        float yaw = (float) (Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f) + randomYaw;
        float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, diffXZ))) + randomPitch;
        yaw = mc.player.rotationYaw + GCDFix.getFixedRotation(MathHelper.wrapDegrees(yaw - mc.player.rotationYaw));
        pitch = mc.player.rotationPitch + GCDFix.getFixedRotation(MathHelper.wrapDegrees(pitch - mc.player.rotationPitch));
        pitch = MathHelper.clamp(pitch, -90F, 90F);
        yaw = RotationHelper.updateRotation(mc.player.rotationYaw, yaw, speedRotation);
        pitch = RotationHelper.updateRotation(mc.player.rotationPitch, pitch, speedRotation);

        return new float[]{yaw, pitch};
    }

    public static float[] rotats(EntityLivingBase entity) {

        double diffX = entity.posX - mc.player.posX;
        double diffZ = entity.posZ - mc.player.posZ;
        double diffY = entity.posY + entity.getEyeHeight() * 0.7 - (mc.player.posY + mc.player.getEyeHeight());
        if (!mc.player.canEntityBeSeen(entity)) {
            diffY = entity.posY + entity.height - (mc.player.posY + mc.player.getEyeHeight());
        }

        double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);


        float yaw = (float) (Math.toDegrees(Math.atan2(diffZ, diffX)) - 90) +  GCDFix.getFixedRotation(MathematicHelper.randomizeFloat(-2.5f, 2.5f));
        float pitch = (float) (Math.toDegrees(-Math.atan2(diffY, dist))) + GCDFix.getFixedRotation(MathematicHelper.randomizeFloat(-2.5f, 2.5f));

        yaw = mc.player.prevRotationYaw + GCDFix.getFixedRotation(MathHelper.wrapDegrees(yaw - mc.player.rotationYaw));
        pitch = mc.player.prevRotationPitch + GCDFix.getFixedRotation(pitch - mc.player.rotationPitch);
        pitch = MathHelper.clamp(pitch, -80, 70);
        return new float[]{yaw, pitch};
    }
    public static float[] getTargetRotations(Entity ent) {
        double x = ent.posX;
        double z = ent.posZ;
        double y = ent.posY + ent.getEyeHeight() / 2.0F;
        return getRotationFromPosition(x, z, y);
    }
    public static float[] getRotationFromPosition(double x, double z, double y) {
        double xDiff = x - Minecraft.getMinecraft().player.posX;
        double zDiff = z - Minecraft.getMinecraft().player.posZ;
        double yDiff = y - Minecraft.getMinecraft().player.posY - 1.7;

        double dist = MathHelper.sqrt(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0D / 3.141592653589793D) - 90.0F;
        float pitch = (float) -(Math.atan2(yDiff, dist) * 180.0D / 3.141592653589793D);
        return new float[]{yaw, pitch};
    }
    public static float getYawToEntity(Entity mainEntity, Entity targetEntity) {
        double pX = mainEntity.posX;
        double pZ = mainEntity.posZ;
        double eX = targetEntity.posX;
        double eZ = targetEntity.posZ;
        double dX = pX - eX;
        double dZ = pZ - eZ;
        double yaw = Math.toDegrees(Math.atan2(dZ, dX)) + 90.0;
        return (float) yaw;
    }
    public static float[] lookAtEntity(Entity targetEntity) {
        double diffX = targetEntity.posX - mc.player.posX;
        double diffZ = targetEntity.posZ - mc.player.posZ;
        double diffY;

        if (targetEntity instanceof EntityLivingBase) {
             diffY = targetEntity.posY + targetEntity.getEyeHeight() * 0.6 - (mc.player.posY + mc.player.getEyeHeight());
        } else {
            diffY = (targetEntity.getEntityBoundingBox().minY + targetEntity.getEntityBoundingBox().maxY) / 2.0D - mc.player.posY + mc.player.getEyeHeight() - 0.5;
        }

        double dist = MathHelper.sqrt((float) (diffX * diffX + diffZ * diffZ));

        float yaw = (float) (((Math.atan2(diffZ, diffX) * 180.0 / Math.PI) - 90.0f)) + GCDFix.getFixedRotation(MathematicHelper.randomizeFloat(-2, 2));
        float pitch = (float) (-(Math.atan2(diffY, dist) * 180.0 / Math.PI)) + GCDFix.getFixedRotation(MathematicHelper.randomizeFloat(-2, 2));
        yaw = mc.player.rotationYaw + GCDFix.getFixedRotation(MathHelper.wrapDegrees(yaw - mc.player.rotationYaw));
        pitch = mc.player.rotationPitch + GCDFix.getFixedRotation(MathHelper.wrapDegrees(pitch - mc.player.rotationPitch));
        pitch = MathHelper.clamp(pitch, -90F, 90F);

        return new float[]{yaw, pitch};
    }
    public static float[] getRotations(Entity entityIn, boolean random, float yawRandom, float pitchRandom) {
        final double diffX = entityIn.posX + (entityIn.posX - entityIn.prevPosX) - mc.player.posX - mc.player.motionX;
        final double diffZ = entityIn.posZ + (entityIn.posZ - entityIn.prevPosZ) - mc.player.posZ - mc.player.motionZ;
        double diffY;


        if (entityIn instanceof EntityLivingBase) {
            diffY = entityIn.posY + entityIn.getEyeHeight() - (mc.player.posY + mc.player.getEyeHeight()) - 0.35;
        } else {
            diffY = (entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2 - (mc.player.posY + mc.player.getEyeHeight());
        }
        if (!mc.player.canEntityBeSeen(entityIn)) {
            diffY = entityIn.posY + entityIn.height - (mc.player.posY + mc.player.getEyeHeight());
        }
        final double diffXZ = GCDFix.getFixedRotation(MathHelper.sqrt(diffX * diffX + diffZ * diffZ));

        float randomYaw = 0.0f;
        if (random) {
            randomYaw = GCDFix.getFixedRotation(MathematicHelper.randomizeFloat(yawRandom, -yawRandom));
        }
        float randomPitch = 0.0f;
        if (random) {
            randomPitch = GCDFix.getFixedRotation(MathematicHelper.randomizeFloat(pitchRandom, -pitchRandom));
        }

        float yaw = (float) ((Math.toDegrees(Math.atan2(diffZ, diffX)) - 90)) + randomYaw;
        float pitch = (float) Math.toDegrees(-(Math.atan2(diffY, diffXZ))) + randomPitch;

        yaw = (mc.player.rotationYaw + GCDFix.getFixedRotation(MathHelper.wrapDegrees(yaw - mc.player.rotationYaw)));
        pitch = mc.player.rotationPitch + GCDFix.getFixedRotation(MathHelper.wrapDegrees(pitch - mc.player.rotationPitch));
        pitch = MathHelper.clamp(pitch, -90F, 90F);
        lastRotations = new float[]{yaw, pitch};
        return lastRotations;
    }
    public static float updateRotation(float current, float newValue, float speed) {
        float f = MathHelper.wrapDegrees(newValue - current);
        if (f > speed) {
            f = speed;
        }
        if (f < -speed) {
            f = -speed;
        }
        return current + f;
    }
}

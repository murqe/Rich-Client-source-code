package zxc.rich.api.utils.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.*;
import zxc.rich.api.utils.Helper;

public class EntityHelper implements Helper {

    public static double getDistance(double x, double y, double z, double x1, double y1, double z1) {
        double posX = x - x1;
        double posY = y - y1;
        double posZ = z - z1;
        return Math.sqrt(posX * posX + posY * posY + posZ * posZ);
    }

    public static double getDistance(double x1, double z1, double x2, double z2) {
        double deltaX = x1 - x2;
        double deltaZ = z1 - z2;
        return Math.hypot(deltaX, deltaZ);
    }

    public static Entity rayCast(Entity entityIn, double range) {
        Vec3d vec = entityIn.getPositionVector().add(new Vec3d(0, entityIn.getEyeHeight(), 0));
        Vec3d vecPositionVector = mc.player.getPositionVector().add(new Vec3d(0, mc.player.getEyeHeight(), 0));
        AxisAlignedBB axis = mc.player.getEntityBoundingBox().addCoord(vec.xCoord - vecPositionVector.xCoord, vec.yCoord - vecPositionVector.yCoord, vec.zCoord - vecPositionVector.zCoord).expand(1, 1, 1);
        Entity entityRayCast = null;
        for (Entity entity : mc.world.getEntitiesWithinAABBExcludingEntity(mc.player, axis)) {
            if (entity.canBeCollidedWith() && entity instanceof EntityLivingBase) {
                float size = entity.getCollisionBorderSize();
                AxisAlignedBB axis1 = entity.getEntityBoundingBox().expand(size, size, size);
                RayTraceResult rayTrace = axis1.calculateIntercept(vecPositionVector, vec);
                if (axis1.isVecInside(vecPositionVector)) {
                    if (range >= 0) {
                        entityRayCast = entity;
                        range = 0;
                    }
                } else if (rayTrace != null) {
                    double dist = vecPositionVector.distanceTo(rayTrace.hitVec);
                    if (range == 0 || dist < range) {
                        entityRayCast = entity;
                        range = dist;
                    }
                }
            }
        }

        return entityRayCast;
    }


    public static boolean checkArmor(Entity entity) {
        return ((EntityLivingBase) entity).getTotalArmorValue() < 6;
    }

    public static int getPing(EntityPlayer entityPlayer) {
        return (mc.player.connection.getPlayerInfo(entityPlayer.getUniqueID()).getResponseTime());
    }

    public static double getDistanceOfEntityToBlock(Entity entity, BlockPos pos) {
        return getDistance(entity.posX, entity.posY, entity.posZ, pos.getX(), pos.getY(), pos.getZ());
    }
}

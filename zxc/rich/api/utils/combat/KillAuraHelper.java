package zxc.rich.api.utils.combat;

import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.EnumHand;
import zxc.rich.Main;
import zxc.rich.api.utils.Helper;
import zxc.rich.client.features.combat.AntiBot;
import zxc.rich.client.features.combat.KillAura;
import zxc.rich.client.friend.Friend;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class KillAuraHelper implements Helper {

    public static boolean canAttack(EntityLivingBase player) {
        if (player instanceof EntityPlayer || player instanceof EntityAnimal || player instanceof EntityMob
                || player instanceof EntityVillager) {
            if (player instanceof EntityPlayer && !KillAura.auraplayers.getBoolValue()) {
                return false;
            }
            if (player instanceof EntityAnimal && !KillAura.auramobs.getBoolValue()) {
                return false;
            }
            if (player instanceof EntityMob && !KillAura.auramobs.getBoolValue()) {
                return false;
            }

            if (player instanceof EntityVillager && !KillAura.auramobs.getBoolValue()) {
                return false;
            }
        }
        if (player.isInvisible() && !KillAura.invisiblecheck.getBoolValue()) {
            return false;
        }
        if (player instanceof EntityArmorStand) {
            return false;
        }
        if (KillAura.nakedPlayer.getBoolValue() && EntityHelper.checkArmor(player)) {
            return false;
        }
        for (Friend friend : Main.instance.friendManager.getFriends()) {
            if (!player.getName().equals(friend.getName()))
                continue;
            return false;
        }

        if (Main.instance.featureDirector.getFeature(AntiBot.class).isEnabled() && AntiBot.isBotPlayer.contains(player)) {
            return false;
        }

        if (!canSeeEntityAtFov(player, (float) KillAura.fov.getNumberValue()) && !canSeeEntityAtFov(player, (float) KillAura.fov.getNumberValue())) {
            return false;
        }
        if (!range(player, KillAura.range.getNumberValue() + KillAura.prerange.getNumberValue())) {
            return false;
        }
        if (!player.canEntityBeSeen(mc.player)) {
            return KillAura.walls.getBoolValue();
        }
        return player != mc.player;
    }

    public static boolean canSeeEntityAtFov(Entity entityLiving, float scope) {
        double diffX = entityLiving.posX - mc.player.posX;
        double diffZ = entityLiving.posZ - mc.player.posZ;
        float yaw = (float) (Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0);
        double difference = angleDifference(yaw, mc.player.rotationYaw);
        return difference <= scope;
    }

    public static double angleDifference(float oldYaw, float newYaw) {
        float yaw = Math.abs(oldYaw - newYaw) % 360;
        if (yaw > 180) {
            yaw = 360 - yaw;
        }
        return yaw;
    }

    private static boolean range(EntityLivingBase entity, float range) {
        return mc.player.getDistanceToEntity(entity) <= range;
    }
    public static void attackEntitySuccess(EntityLivingBase target) {
        if (target.getHealth() > 0) {
            float attackDelay = KillAura.attackCoolDown.getNumberValue();
            if (mc.player.getCooledAttackStrength(attackDelay) == 1) {
                mc.player.setSprinting(false);
                mc.playerController.attackEntity(mc.player, EntityHelper.rayCast(target, KillAura.range.getNumberValue() + KillAura.prerange.getNumberValue()));
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.setSprinting(true);
            }
        }
    }

    public static EntityLivingBase getSortEntities() {
        List<EntityLivingBase> entity = new ArrayList<>();
        for (Entity e : mc.world.loadedEntityList) {
            if (e instanceof EntityLivingBase) {
                EntityLivingBase player = (EntityLivingBase) e;
                if (mc.player.getDistanceToEntity(player) < KillAura.range.getNumberValue() + KillAura.prerange.getNumberValue() && (canAttack(player))) {
                    if (player.getHealth() > 0) {
                        entity.add(player);
                    } else {
                        entity.remove(player);
                    }
                }
            }
        }

        String sortMode = KillAura.sortMode.getOptions();
        if (sortMode.equalsIgnoreCase("Higher Armor")) {
            entity.sort(Comparator.comparing(EntityLivingBase::getTotalArmorValue).reversed());
        } else if (sortMode.equalsIgnoreCase("Lowest Armor")) {
            entity.sort(Comparator.comparing(EntityLivingBase::getTotalArmorValue));
        } else if (sortMode.equalsIgnoreCase("Health")) {
            entity.sort((o1, o2) -> (int) (o1.getHealth() - o2.getHealth()));
        } else if (sortMode.equalsIgnoreCase("Distance")) {
            entity.sort(Comparator.comparingDouble(mc.player::getDistanceToEntity));
        }

        if (entity.isEmpty())
            return null;

        return entity.get(0);
    }
}

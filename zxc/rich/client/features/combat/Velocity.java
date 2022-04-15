package zxc.rich.client.features.combat;

import net.minecraft.block.Block;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.util.math.BlockPos;
import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.EventReceivePacket;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.ui.settings.impl.BooleanSetting;
import zxc.rich.client.ui.settings.impl.ListSetting;

public class Velocity extends Feature {
    BooleanSetting cancelOtherDamage;
    ListSetting velocityMode;
    float ticks = 0.2f;

    public Velocity() {
        super("Velocity", "Вы не будете откидываться", FeatureCategory.COMBAT);
        velocityMode = new ListSetting("Velocity Mode", "Packet", () -> true, "Packet", "Matrix");
        cancelOtherDamage = new BooleanSetting("Cancel Other Damage", true, () -> true);
        addSettings(velocityMode, cancelOtherDamage);
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        String mode = velocityMode.getOptions();
        this.setSuffix(mode);
        if (cancelOtherDamage.getBoolValue()) {
            if (mc.player.hurtTime > 0 && event.getPacket() instanceof SPacketEntityVelocity) {
                if (mc.player.isPotionActive(MobEffects.POISON) || (mc.player.isPotionActive(MobEffects.WITHER) || mc.player.isBurning())) {
                    event.setCancelled(true);
                }
            }
        }
        if (mode.equalsIgnoreCase("Packet")) {
            if (event.getPacket() instanceof SPacketEntityVelocity || event.getPacket() instanceof SPacketExplosion) {
                if (((SPacketEntityVelocity) event.getPacket()).getEntityID() == mc.player.getEntityId()) {
                    event.setCancelled(true);
                }
            }
        } else if (mode.equals("Matrix")) {
            if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock() == Block.getBlockById(0)) {
                if (mc.player.hurtTime > 0) {
                    mc.player.motionY = -ticks;
                    ticks += 1.5f;
                }
            }
        }
    }
}
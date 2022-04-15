package zxc.rich.client.features.movement;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.EventUpdate;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.ui.settings.impl.NumberSetting;

public class WaterLeave extends Feature {
    private final NumberSetting leaveMotion;

    public WaterLeave() {
        super("WaterLeave", "Игрок высоко прыгает при погружении в воду", FeatureCategory.MOVEMENT);
        leaveMotion = new NumberSetting("Motion Y", 10, 0.5F, 10, 0.5F, () -> true);
        addSettings(leaveMotion);
    }


    @EventTarget
    public void onUpdate(EventUpdate event) {
        final BlockPos blockPos = new BlockPos(mc.player.posX, mc.player.posY - 0.1, mc.player.posZ);
        final Block block = mc.world.getBlockState(blockPos).getBlock();
        if (block instanceof BlockLiquid) {
            if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 0.01, mc.player.posZ)).getBlock() == Block.getBlockById(9) && mc.player.isInsideOfMaterial(Material.AIR)) {
                mc.player.motionY = 0.02;
            }
            if (!WaterLeave.mc.player.isInLiquid() && WaterLeave.mc.player.fallDistance > 0.0f && WaterLeave.mc.player.motionY < 0.02) {
                WaterLeave.mc.player.motionY += this.leaveMotion.getNumberValue();
            }
        }
    }
}
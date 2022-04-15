package zxc.rich.client.features.movement;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import zxc.rich.Main;
import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.EventLiquidSolid;
import zxc.rich.api.event.events.impl.EventPreMotionUpdate;
import zxc.rich.api.event.events.impl.EventReceivePacket;
import zxc.rich.api.utils.movement.MovementUtils;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.ui.notification.NotificationMode;
import zxc.rich.client.ui.notification.NotificationRenderer;
import zxc.rich.client.ui.settings.impl.BooleanSetting;
import zxc.rich.client.ui.settings.impl.ListSetting;
import zxc.rich.client.ui.settings.impl.NumberSetting;

public class Jesus extends Feature {
    public static ListSetting mode = new ListSetting("Jesus Mode", "Matrix", () -> true, "Matrix", "ReallyWorld", "Matrix Zoom", "NCP");
    public static NumberSetting speed = new NumberSetting("Speed", 0.65F, 0.1F, 10.0F, 0.01F, () -> !mode.currentMode.equals("NCP"));
    public static NumberSetting NCPSpeed = new NumberSetting("NCP Speed", 0.25F, 0.01F, 0.5F, 0.01F, () -> mode.currentMode.equals("NCP"));
    public static NumberSetting motionUp = new NumberSetting("Motion Up", 0.42F, 0.1F, 2.0F, 0.01F, () -> mode.currentMode.equals("Matrix"));
    public static BooleanSetting useTimer = new BooleanSetting("Use Timer", false, () -> true);
    private final NumberSetting timerSpeed = new NumberSetting("Timer Speed", 1.05F, 1.01F, 1.5F, 0.01F, () -> useTimer.getBoolValue());
    private final BooleanSetting speedCheck = new BooleanSetting("Speed Potion Check", false, () -> true);
    private final BooleanSetting autoMotionStop = new BooleanSetting("Auto Motion Stop", true, () -> mode.currentMode.equals("ReallyWorld"));
    private final BooleanSetting autoWaterDown = new BooleanSetting("Auto Water Down", false, () -> mode.currentMode.equals("ReallyWorld"));

    private int waterTicks = 0;

    public Jesus() {
        super("Jesus", "Позволяет прыгать на воде", FeatureCategory.MOVEMENT);
        this.addSettings(mode, speed, NCPSpeed, useTimer, timerSpeed, motionUp, speedCheck,autoWaterDown, autoMotionStop);
    }

    public void onDisable() {
        mc.timer.timerSpeed = 1.0F;
        if ((mode.currentMode.equals("ReallyWorld")) && this.autoWaterDown.getBoolValue()) {
            mc.player.motionY -= 500.0D;
        }

        this.waterTicks = 0;
        super.onDisable();
    }

    @EventTarget
    public void onLiquidBB(EventLiquidSolid event) {
        if ( mode.currentMode.equals("NCP")) {
            event.setCancelled(true);
        }
    }

    private boolean isWater() {
        BlockPos bp1 = new BlockPos(mc.player.posX - 0.5D, mc.player.posY - 0.5D, mc.player.posZ - 0.5D);
        BlockPos bp2 = new BlockPos(mc.player.posX - 0.5D, mc.player.posY - 0.5D, mc.player.posZ + 0.5D);
        BlockPos bp3 = new BlockPos(mc.player.posX + 0.5D, mc.player.posY - 0.5D, mc.player.posZ + 0.5D);
        BlockPos bp4 = new BlockPos(mc.player.posX + 0.5D, mc.player.posY - 0.5D, mc.player.posZ - 0.5D);
        return mc.player.world.getBlockState(bp1).getBlock() == Blocks.WATER && mc.player.world.getBlockState(bp2).getBlock() == Blocks.WATER && mc.player.world.getBlockState(bp3).getBlock() == Blocks.WATER && mc.player.world.getBlockState(bp4).getBlock() == Blocks.WATER || mc.player.world.getBlockState(bp1).getBlock() == Blocks.LAVA && mc.player.world.getBlockState(bp2).getBlock() == Blocks.LAVA && mc.player.world.getBlockState(bp3).getBlock() == Blocks.LAVA && mc.player.world.getBlockState(bp4).getBlock() == Blocks.LAVA;
    }

    @EventTarget
    public void onPreMotion(EventPreMotionUpdate event) {
        this.setSuffix(mode.getCurrentMode());
        if (mc.player.isPotionActive(MobEffects.SPEED) || !this.speedCheck.getBoolValue()) {
            BlockPos blockPos = new BlockPos(Minecraft.getMinecraft().player.posX, Minecraft.getMinecraft().player.posY - 0.1, Minecraft.getMinecraft().player.posZ);
            Block block = Minecraft.getMinecraft().world.getBlockState(blockPos).getBlock();
            if (useTimer.getBoolValue()) {
                mc.timer.timerSpeed = this.timerSpeed.getNumberValue();
            }
            if (mode.currentMode.equalsIgnoreCase("Matrix")) {
                if (mc.player.isInLiquid() && mc.player.motionY < 0.0D) {
                    mc.player.motionY = (double) motionUp.getNumberValue();
                    MovementUtils.setSpeed(speed.getNumberValue());
                } else if (mode.currentMode.equalsIgnoreCase("NCP")) {
                    if (this.isWater() && block instanceof BlockLiquid) {
                        mc.player.motionY = 0.0D;
                        mc.player.onGround = false;
                        mc.player.isAirBorne = true;
                        MovementUtils.setSpeed(NCPSpeed.getNumberValue());
                        event.setPosY(mc.player.ticksExisted % 2 == 0 ? event.getPosY() + 0.02D : event.getPosY() - 0.02D);
                        event.setOnGround(false);
                    }
                }
            } else if (mode.currentMode.equalsIgnoreCase("ReallyWorld")) {

                if (block instanceof BlockLiquid && !Minecraft.getMinecraft().player.onGround) {
                    if (Minecraft.getMinecraft().world.getBlockState(new BlockPos(Minecraft.getMinecraft().player.posX, Minecraft.getMinecraft().player.posY , Minecraft.getMinecraft().player.posZ)).getBlock() == Blocks.WATER) {
                        Minecraft.getMinecraft().player.motionX = 0.0F;
                        Minecraft.getMinecraft().player.motionY = 0.04f;
                        Minecraft.getMinecraft().player.motionZ = 0.0F;
                        Minecraft.getMinecraft().player.fallDistance = 0.0F;
                        Minecraft.getMinecraft().player.jumpMovementFactor = 0f;
                    } else {
                        MovementUtils.setSpeed(speed.getNumberValue());
                    }
                    if(Minecraft.getMinecraft().player.isCollidedHorizontally)
                        Minecraft.getMinecraft().player.motionY = 0.2;
                }
            } else if (mode.currentMode.equalsIgnoreCase("Matrix Zoom")) {
                if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - 0.20000000298023224D, mc.player.posZ)).getBlock() instanceof BlockLiquid && !mc.player.onGround) {
                    MovementUtils.setSpeed(speed.getNumberValue());
                }

                if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 9.999999747378752E-5D, mc.player.posZ)).getBlock() instanceof BlockLiquid) {
                    mc.player.motionX = 0.0D;
                    mc.player.motionZ = 0.0D;
                    mc.player.motionY = 0.05000000074505806D;
                }
            }
        }

    }
}
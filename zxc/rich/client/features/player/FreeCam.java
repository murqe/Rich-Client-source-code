package zxc.rich.client.features.player;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import zxc.rich.Main;
import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.Event2D;
import zxc.rich.api.event.events.impl.EventReceivePacket;
import zxc.rich.api.event.events.impl.EventUpdate;
import zxc.rich.api.utils.movement.MovementUtils;
import zxc.rich.api.utils.render.ColorUtils;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.ui.settings.impl.BooleanSetting;
import zxc.rich.client.ui.settings.impl.NumberSetting;

public class FreeCam extends Feature {

    private final NumberSetting speed = new NumberSetting("Flying Speed", 0.5F, 0.1F, 1F, 0.1F, () -> true);


    private final BooleanSetting reallyWorld = new BooleanSetting("ReallyWorld Bypass", false, () -> true);

    double x, y, z;

    public FreeCam() {
        super("FreeCam", "Позволяет летать в свободной камере", FeatureCategory.PLAYER);
        addSettings(speed, reallyWorld);
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        if (reallyWorld.getBoolValue()) {
            if (event.getPacket() instanceof SPacketPlayerPosLook) {
                event.setCancelled(true);
            }
        }
    }

    public void onEnable() {
        super.onEnable();
        x = mc.player.posX;
        y = mc.player.posY;
        z = mc.player.posZ;
        EntityOtherPlayerMP ent = new EntityOtherPlayerMP(mc.world, mc.player.getGameProfile());
        ent.inventory = mc.player.inventory;
        ent.inventoryContainer = mc.player.inventoryContainer;
        ent.setHealth(mc.player.getHealth());
        ent.setPositionAndRotation(this.x, mc.player.getEntityBoundingBox().minY, this.z, mc.player.rotationYaw, mc.player.rotationPitch);
        ent.rotationYawHead = mc.player.rotationYawHead;
        mc.world.addEntityToWorld(-1, ent);
    }

    @EventTarget
    public void onScreen(Event2D e) {
        ScaledResolution sr = new ScaledResolution(mc);
        String yCoord = "" + Math.round(mc.player.posY - y);

        String str = "Y: " + yCoord;
        mc.neverlose900_15.drawStringWithShadow(str, (sr.getScaledWidth() - mc.fontRendererObj.getStringWidth(str)) / 1.98, sr.getScaledHeight() / 1.8 - 20, -1);

    }

    @EventTarget
    public void onPreMotion(EventUpdate e) {
        mc.player.motionY = 0;
        if (mc.gameSettings.keyBindJump.pressed) {
            mc.player.motionY = speed.getNumberValue();
        }
        if (mc.gameSettings.keyBindSneak.pressed) {
            mc.player.motionY = -speed.getNumberValue();
        }
        mc.player.noClip = true;
        MovementUtils.setSpeed(speed.getNumberValue());
        e.setCancelled(true);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.player.setPosition(x, y, z);
        mc.getConnection().sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.01, mc.player.posZ, mc.player.onGround));
        mc.player.capabilities.isFlying = false;
        mc.player.noClip = false;
        mc.world.removeEntityFromWorld(-1);
    }
}

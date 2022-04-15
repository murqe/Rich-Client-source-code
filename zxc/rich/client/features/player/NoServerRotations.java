package zxc.rich.client.features.player;

import net.minecraft.network.play.server.SPacketPlayerPosLook;
import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.EventReceivePacket;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;

public class NoServerRotations extends Feature {

    public NoServerRotations() {
        super("NoServerRotation", "Убирает ротацию со стороны сервера", FeatureCategory.PLAYER);
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            SPacketPlayerPosLook packet = (SPacketPlayerPosLook) event.getPacket();
            packet.yaw = mc.player.rotationYaw;
            packet.pitch = mc.player.rotationPitch;
        }
    }
}
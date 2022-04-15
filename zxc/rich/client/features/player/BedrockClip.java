package zxc.rich.client.features.player;

import net.minecraft.network.play.client.CPacketPlayer;
import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.EventUpdate;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;

public class BedrockClip extends Feature {
    public BedrockClip() {
        super("BedrockClip", "Телепортирует вас под бедрок когда вы на воде, или получили урон", FeatureCategory.MOVEMENT);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.player.isInWater() || mc.player.hurtTime > 0) {
            for (int i = 0; i < 10; i++) {
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));

            }
            for (int i = 0; i < 10; i++) {
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 50, mc.player.posZ, false));
            }
            mc.player.setPosition(mc.player.posX, mc.player.posY + 50, mc.player.posZ);

            toggle();
        }
    }

}

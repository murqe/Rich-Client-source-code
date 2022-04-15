package zxc.rich.client.features.player;


import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import zxc.rich.Main;
import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.EventReceivePacket;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;

public class AutoFish extends Feature {
    public AutoFish() {
        super("AutoFish", "Автоматически ловит рыбу", FeatureCategory.PLAYER);
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        if(timerHelper.hasReached(500)) {
            System.out.println(event.getPacket().toString());
        }
        if (event.getPacket() instanceof SPacketSoundEffect) {
            SPacketSoundEffect packet = (SPacketSoundEffect) event.getPacket();
            if (packet.getCategory() == SoundCategory.NEUTRAL && packet.getSound() == SoundEvents.ENTITY_BOBBER_SPLASH) {
                if (mc.player.getHeldItemMainhand().getItem() instanceof ItemFishingRod || mc.player.getHeldItemOffhand().getItem() instanceof ItemFishingRod) {
                    mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                    mc.player.swingArm(EnumHand.MAIN_HAND);
                    mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                    mc.player.swingArm(EnumHand.MAIN_HAND);
                }
            }
        }
    }
}

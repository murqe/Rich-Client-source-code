package zxc.rich.client.features.combat;

import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.EventUpdate;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.ui.settings.impl.NumberSetting;

public class FastBow extends Feature {

    private final NumberSetting bowDelay;

    public FastBow() {
        super("FastBow", "При зажатии на ПКМ игрок быстро стреляет из лука", FeatureCategory.COMBAT);
        this.bowDelay = new NumberSetting("Bow Delay", 1.0f, 1.0f, 10, 0.5f, () -> true);
        addSettings(bowDelay);
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        if (mc.player.inventory.getCurrentItem().getItem() instanceof ItemBow && mc.player.isBowing() && mc.player.getItemInUseMaxCount() >= bowDelay.getNumberValue()) {
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            mc.player.stopActiveHand();
        }
    }
}

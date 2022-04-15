package zxc.rich.client.features.combat;

import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import zxc.rich.Main;
import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.EventPreMotionUpdate;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.ui.settings.impl.NumberSetting;

public class ShieldDesync extends Feature {
    public ShieldDesync() {
        super("ShieldDesync", "Десинкает шилд-брейкеры противников(вам не смогут сломать щит)", FeatureCategory.COMBAT);
        addSettings();
    }

    @EventTarget
    public void onEventPreMotion(EventPreMotionUpdate event) {
        if (Main.instance.featureDirector.getFeature(KillAura.class).isEnabled() && mc.player.isBlocking() && KillAura.target != null && mc.player.ticksExisted % 2 == 0) {
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(900, 900, 900), EnumFacing.NORTH));
            mc.playerController.processRightClick(mc.player, mc.world, EnumHand.OFF_HAND);
        }
    }
}

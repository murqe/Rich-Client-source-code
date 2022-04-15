package zxc.rich.client.features.player;

import net.minecraft.entity.player.EntityPlayer;
import zxc.rich.Main;
import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.EventReceiveMessage;
import zxc.rich.api.event.events.impl.EventUpdate;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.ui.settings.impl.BooleanSetting;
import zxc.rich.client.ui.settings.impl.NumberSetting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class AutoTPAccept extends Feature {

    private final NumberSetting tpadelay = new NumberSetting("Accept Delay", 500, 100, 1000, 100, () -> true);
    private final BooleanSetting friendOnlytp = new BooleanSetting("Only Friend", false, () -> true);

    public AutoTPAccept() {
        super("AutoTPAccept", "Автоматически пишет /tpyes , когда к вам кто-то телепортируется", FeatureCategory.PLAYER);
        addSettings(tpadelay, friendOnlytp);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        this.setSuffix("" + tpadelay.getNumberValue());
    }

    @EventTarget
    public void onReceiveChat(EventReceiveMessage event) {
            if (friendOnlytp.getBoolValue()) {
            for (EntityPlayer entity : mc.world.playerEntities) {
                if ((event.getMessage().contains("/tpyes") || event.getMessage().contains("/tpaccept") || event.getMessage().contains("просит телепортироваться к Вам") && Main.instance.friendManager.isFriend(entity.getName()) && timerHelper.hasReached(500))) {
                    mc.player.sendChatMessage("/tpaccept");
                    timerHelper.reset();
                }
            }
        } else {
            if ((event.getMessage().contains("/tpyes") || event.getMessage().contains("/tpaccept") || event.getMessage().contains("просит телепортироваться к Вам")) && timerHelper.hasReached(500)) {
                mc.player.sendChatMessage("/tpaccept");
                timerHelper.reset();
            }
        }
    }
}

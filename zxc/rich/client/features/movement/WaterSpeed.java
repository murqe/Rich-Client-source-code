package zxc.rich.client.features.movement;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import zxc.rich.Main;
import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.EventReceivePacket;
import zxc.rich.api.event.events.impl.EventUpdate;
import zxc.rich.api.utils.movement.MovementUtils;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.ui.notification.NotificationMode;
import zxc.rich.client.ui.notification.NotificationRenderer;
import zxc.rich.client.ui.settings.impl.BooleanSetting;
import zxc.rich.client.ui.settings.impl.NumberSetting;

public class WaterSpeed extends Feature {

    private final NumberSetting speed;
    private final BooleanSetting speedCheck;

    public WaterSpeed() {
        super("WaterSpeed", "Делает вас быстрее в воде" ,FeatureCategory.MOVEMENT);
        speed = new NumberSetting("Speed Amount", 0.4f, 0.1F, 4, 0.01F, () -> true);
        speedCheck = new BooleanSetting("Speed Potion Check", false, () -> true);
        addSettings(speedCheck, speed);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (!mc.player.isPotionActive(MobEffects.SPEED) && speedCheck.getBoolValue()) {
            return;
        }
        if (mc.player.isInWater() || mc.player.isInLava()) {
            MovementUtils.setSpeed(speed.getNumberValue());
        }
    }
}

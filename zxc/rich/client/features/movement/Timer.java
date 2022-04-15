package zxc.rich.client.features.movement;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import prot.rich.Client;
import zxc.rich.Main;
import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.*;
import zxc.rich.api.utils.render.AnimationHelper;
import zxc.rich.api.utils.render.RenderUtils;
import zxc.rich.api.utils.world.TimerHelper;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.features.combat.KillAura;
import zxc.rich.client.ui.notification.Notification;
import zxc.rich.client.ui.notification.NotificationMode;
import zxc.rich.client.ui.notification.NotificationRenderer;
import zxc.rich.client.ui.settings.impl.BooleanSetting;
import zxc.rich.client.ui.settings.impl.NumberSetting;

import java.awt.*;

public class Timer extends Feature {
    private final NumberSetting timer;
    private final BooleanSetting stopOnAttack = new BooleanSetting("Stop on attack", false, () -> true);

    public Timer() {
        super("Timer", "Увеличивает скорость игры", FeatureCategory.MOVEMENT);
        timer = new NumberSetting("Timer Amount", 2.0F, 0.1F, 10.0F, 0.1F, () -> true);
        addSettings(stopOnAttack, timer);

    }


    @EventTarget
    public void onMove(EventMove event) {
        if (this.isEnabled()) {
            mc.timer.timerSpeed = timer.getNumberValue();
            if (Main.instance.featureDirector.getFeature(KillAura.class).isEnabled() && KillAura.target != null && stopOnAttack.getBoolValue()) {
                mc.timer.timerSpeed = 1.0f;
            }
            this.setSuffix("" + timer.getNumberValue());
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.timer.timerSpeed = 1.0f;
    }
}

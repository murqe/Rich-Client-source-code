package zxc.rich.client.features.misc;

import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextFormat;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.text.TextFormatting;
import zxc.rich.Main;
import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.EventReceivePacket;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.features.combat.KillAura;
import zxc.rich.client.features.combat.TargetStrafe;
import zxc.rich.client.features.movement.*;
import zxc.rich.client.ui.notification.NotificationMode;
import zxc.rich.client.ui.notification.NotificationRenderer;
import zxc.rich.client.ui.settings.impl.BooleanSetting;

public class FlagDetector extends Feature {
    public BooleanSetting speedDetect = new BooleanSetting("Speed Detect", true, () -> true);
    public BooleanSetting flightDetect = new BooleanSetting("Flight Detect", true, () -> true);
    public BooleanSetting jesusDetect = new BooleanSetting("Jesus Detect", true, () -> true);
    public BooleanSetting timerDetect = new BooleanSetting("Timer Detect", true, () -> true);
    public BooleanSetting spiderDetect = new BooleanSetting("Spider Detect", true, () -> true);
    public BooleanSetting tStrafeDetect = new BooleanSetting("TargetStrafe Detect", true, () -> true);
    public BooleanSetting waterSpeedDetect = new BooleanSetting("WaterSpeed Detect", true, () -> true);
    public BooleanSetting waterLeaveDetect = new BooleanSetting("WaterLeave Detect", true, () -> true);

    public FlagDetector() {
        super("FlagDetector", "Автоматически выключает модуль при его детекте", FeatureCategory.MISC);
        addSettings(speedDetect,flightDetect,jesusDetect,timerDetect,spiderDetect,tStrafeDetect,waterSpeedDetect,waterLeaveDetect);
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        if (this.isEnabled()) {
            if (event.getPacket() instanceof SPacketPlayerPosLook) {
                if (Main.instance.featureDirector.getFeature(Speed.class).isEnabled() && speedDetect.getBoolValue()) {
                    featureAlert("Speed");
                    Main.instance.featureDirector.getFeature(Speed.class).toggle();
                } else if (Main.instance.featureDirector.getFeature(Flight.class).isEnabled() && flightDetect.getBoolValue()) {
                    featureAlert("Flight");
                    Main.instance.featureDirector.getFeature(Flight.class).toggle();
                } else if (Main.instance.featureDirector.getFeature(TargetStrafe.class).isEnabled() && KillAura.target != null && tStrafeDetect.getBoolValue()) {
                    featureAlert("TargetStrafe");
                    Main.instance.featureDirector.getFeature(TargetStrafe.class).toggle();
                } else if (Main.instance.featureDirector.getFeature(Jesus.class).isEnabled() && mc.player.isInLiquid() && jesusDetect.getBoolValue()) {
                    featureAlert("Jesus");
                    Main.instance.featureDirector.getFeature(Jesus.class).toggle();
                } else if (Main.instance.featureDirector.getFeature(Timer.class).isEnabled() && timerDetect.getBoolValue()) {
                    featureAlert("Timer");
                    Main.instance.featureDirector.getFeature(Timer.class).toggle();
                } else if (Main.instance.featureDirector.getFeature(WaterSpeed.class).isEnabled() && waterSpeedDetect.getBoolValue()) {
                    featureAlert("WaterSpeed");
                    Main.instance.featureDirector.getFeature(WaterSpeed.class).toggle();
                } else if (Main.instance.featureDirector.getFeature(WaterLeave.class).isEnabled() && waterLeaveDetect.getBoolValue()) {
                    featureAlert("WaterLeave");
                    Main.instance.featureDirector.getFeature(WaterLeave.class).toggle();
                } else if (Main.instance.featureDirector.getFeature(Spider.class).isEnabled() && spiderDetect.getBoolValue()) {
                    featureAlert("Spider");
                    Main.instance.featureDirector.getFeature(Spider.class).toggle();
                }
            }
        }
    }

    public void featureAlert(String feature) {
        NotificationRenderer.queue(TextFormatting.RED + feature + TextFormatting.RESET, "Disabling due to lag back", 3, NotificationMode.WARNING);
    }
}
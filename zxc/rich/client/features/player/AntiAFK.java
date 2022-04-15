package zxc.rich.client.features.player;

import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.EventPreMotionUpdate;
import zxc.rich.api.event.events.impl.EventUpdate;
import zxc.rich.api.utils.combat.GCDFix;
import zxc.rich.api.utils.math.MathematicHelper;
import zxc.rich.api.utils.movement.MovementUtils;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.ui.settings.impl.BooleanSetting;
import zxc.rich.client.ui.settings.impl.NumberSetting;

public class AntiAFK extends Feature {
    private final BooleanSetting sendMessage;
    private final BooleanSetting spin;

    public final NumberSetting sendDelay;
    public final BooleanSetting jump = new BooleanSetting("Jump", true, () -> true);

    public float rot = 0;

    public AntiAFK() {
        super("AntiAFK", "Позволяет встать в афк режим, без риска кикнуться",  FeatureCategory.PLAYER);
        spin = new BooleanSetting("Spin", true, () -> true);
        sendMessage = new BooleanSetting("Send Message", true, () -> true);
        sendDelay = new NumberSetting("Send Delay", 500, 100, 1000, 100, sendMessage::getBoolValue);
        addSettings(spin, sendMessage, sendDelay, jump);
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        if (jump.getBoolValue() && mc.player.onGround) {
            mc.player.jump();
        }
    }

    @EventTarget
    public void onPreMotion(EventPreMotionUpdate event) {
            if (spin.getBoolValue()) {
                float yaw = GCDFix.getFixedRotation((float) (Math.floor(spinAim(5F)) + MathematicHelper.randomizeFloat(-4, 44)));
                event.setYaw(yaw);
                mc.player.renderYawOffset = yaw;
                mc.player.rotationPitchHead = 0;
                mc.player.rotationYawHead = yaw;
            }
            if (timerHelper.hasReached(sendDelay.getNumberValue() * 70) && sendMessage.getBoolValue()) {
                mc.player.sendChatMessage("/richbestcheat");
                timerHelper.reset();
        }
    }

    public float spinAim(float rots) {
        rot += rots;
        return rot;
    }
}

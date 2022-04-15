package zxc.rich.client.features.movement;

import org.lwjgl.input.Keyboard;
import zxc.rich.Main;
import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.EventUpdate;
import zxc.rich.api.utils.movement.MovementUtils;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.features.combat.KillAura;
import zxc.rich.client.ui.settings.impl.BooleanSetting;

public class Sprint extends Feature {
    public static BooleanSetting keepSprint = new BooleanSetting("Keep Sprint", false, () -> true);

    public Sprint() {
        super("Sprint", "Автоматически зажимает за вас CTRL", FeatureCategory.MOVEMENT);
        addSettings(keepSprint);
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        if (!(Main.instance.featureDirector.getFeature(KillAura.class).isEnabled() && KillAura.stopSprint.getBoolValue() && KillAura.target != null)) {
            mc.player.setSprinting(MovementUtils.isMoving());
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.player.setSprinting(false);
    }
}

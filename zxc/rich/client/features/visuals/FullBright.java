package zxc.rich.client.features.visuals;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.EventUpdate;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.ui.settings.impl.ListSetting;

import java.util.Objects;

public class FullBright extends Feature {

    private final ListSetting brightMode = new ListSetting("FullBright Mode", "Gamma", () -> true, "Gamma", "Potion");;

    public FullBright() {
        super("FullBright", "Убирает темноту в игре", FeatureCategory.VISUALS);
        addSettings(brightMode);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (isEnabled()) {
            String mode = brightMode.getOptions();
            if (mode.equalsIgnoreCase("Gamma")) {
                mc.gameSettings.gammaSetting = 1000F;
            }
            if (mode.equalsIgnoreCase("Potion")) {
                mc.player.addPotionEffect(new PotionEffect(Objects.requireNonNull(Potion.getPotionById(16)), 817, 1));
            } else {
                mc.player.removePotionEffect(Objects.requireNonNull(Potion.getPotionById(16)));
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.gameSettings.gammaSetting = 1F;
        mc.player.removePotionEffect(Objects.requireNonNull(Potion.getPotionById(16)));
    }
}

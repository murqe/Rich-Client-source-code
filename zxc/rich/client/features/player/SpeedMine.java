package zxc.rich.client.features.player;

import net.minecraft.init.MobEffects;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.EventUpdate;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.ui.settings.impl.ListSetting;
import zxc.rich.client.ui.settings.impl.NumberSetting;

public class SpeedMine extends Feature {

    private final ListSetting speedmode = new ListSetting("SpeedMine Mode", "Potion", () -> true, "Damage", "Potion");;
    private final NumberSetting damageValue = new NumberSetting("Damage Value", 0.8F, 0.7F, 4, 0.1F, () -> speedmode.currentMode.equals("Damage"));

    public SpeedMine() {
        super("SpeedMine", "Ускоряет скорость ломания блоков", FeatureCategory.PLAYER);

        addSettings(speedmode, damageValue);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        this.setSuffix(speedmode.currentMode);
    }

    @EventTarget
    public void onBlockInteract(EventUpdate event) {
        switch (speedmode.currentMode) {
            case "Potion":
                mc.player.addPotionEffect(new PotionEffect(MobEffects.HASTE, 817, 1));
                break;
            case "Damage":
                if (mc.playerController.curBlockDamageMP >= 0.7) {
                    mc.playerController.curBlockDamageMP = damageValue.getNumberValue();
                }
                break;
        }
    }

    @Override
    public void onDisable() {
        mc.player.removePotionEffect(MobEffects.HASTE);
        super.onDisable();
    }
}

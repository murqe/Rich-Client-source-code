package zxc.rich.client.features.combat;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemStack;
import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.EventPreMotionUpdate;
import zxc.rich.api.event.events.impl.EventUpdate;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.ui.settings.impl.NumberSetting;

public class AutoGApple extends Feature {
    private boolean isActive;
    public static NumberSetting health;

    public AutoGApple() {
        super("AutoGapple", "Кушает гепл при определенном хп", FeatureCategory.COMBAT);
        health = new NumberSetting("Health Amount", 15, 1, 20, 1, () -> true);
        addSettings(health);
    }
    @EventTarget
    public void onUpdate(EventPreMotionUpdate e) {
        this.setSuffix("" + (int) health.getNumberValue());
        if (mc.player == null || mc.world == null)
            return;
        if (isGoldenApple(mc.player.getHeldItemOffhand()) && mc.player.getHealth() <= health.getNumberValue()) {
            isActive = true;
            mc.gameSettings.keyBindUseItem.pressed = true;
        } else if (isActive) {
            mc.gameSettings.keyBindUseItem.pressed = false;
            isActive = false;
        }
    }

    private boolean isGoldenApple(ItemStack itemStack) {
        return (itemStack != null && !itemStack.func_190926_b() && itemStack.getItem() instanceof ItemAppleGold);
    }
}

package zxc.rich.client.features.combat;

import com.mysql.cj.util.TimeUtil;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumHand;
import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.EventPreMotionUpdate;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.ui.settings.impl.BooleanSetting;
import zxc.rich.client.ui.settings.impl.NumberSetting;

import java.util.Objects;

public class AutoPotion extends Feature {

    public NumberSetting delay;
    public BooleanSetting onlyGround = new BooleanSetting("Only Ground", true, () -> true);
    public BooleanSetting strenght = new BooleanSetting("Strenght", true, () -> true);
    public BooleanSetting speed = new BooleanSetting("Speed", true, () -> true);
    public BooleanSetting fire_resistance = new BooleanSetting("Fire Resistance", true, () -> true);


    public AutoPotion() {
        super("AutoPotion", "Автоматически бросает Splash зелья находящиеся в инвентаре", FeatureCategory.COMBAT);
        this.delay = new NumberSetting("Throw Delay", 300, 10, 800, 10, () -> true);
        addSettings(delay, onlyGround, strenght, speed, fire_resistance);
    }

    ItemStack held;

    @EventTarget
    public void onUpdate(EventPreMotionUpdate event) {
        if ((mc.currentScreen instanceof GuiInventory)) {
            return;
        }
        if (onlyGround.getBoolValue() && !mc.player.onGround) {
            return;
        }
        if (timerHelper.hasReached(delay.getNumberValue())) {
            if (isPotionOnHotBar() && mc.player.onGround) {
                if (!mc.player.isPotionActive((Objects.requireNonNull(Potion.getPotionById(1)))) && speed.getBoolValue()) {
                    throwPot(Potions.SPEED);
                }
                if (!mc.player.isPotionActive((Objects.requireNonNull(Potion.getPotionById(5)))) && strenght.getBoolValue()) {
                    throwPot(Potions.STRENGTH);
                }
                if (!mc.player.isPotionActive((Objects.requireNonNull(Potion.getPotionById(12)))) && fire_resistance.getBoolValue()) {
                    throwPot(Potions.FIRERES);
                }
            }
            timerHelper.reset();
        }
    }

    void throwPot(Potions potion) {
        int slot = getPotionSlot(potion);
        mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
        mc.playerController.updateController();
        mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, 90, mc.player.onGround));
        mc.playerController.updateController();
        mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        mc.playerController.updateController();
        mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
    }

    int getPotionSlot(Potions potion) {
        for (int i = 0; i < 9; ++i) {
            if (this.isStackPotion(mc.player.inventory.getStackInSlot(i), potion))
                return i;
        }
        return -1;
    }

    boolean isPotionOnHotBar() {
        for (int i = 0; i < 9; ++i) {
            if (isStackPotion(mc.player.inventory.getStackInSlot(i), Potions.STRENGTH) && strenght.getBoolValue() || isStackPotion(mc.player.inventory.getStackInSlot(i), Potions.SPEED) && speed.getBoolValue() || isStackPotion(mc.player.inventory.getStackInSlot(i), Potions.FIRERES) && fire_resistance.getBoolValue()) {
                return true;
            }
        }
        return false;
    }

    boolean isStackPotion(ItemStack stack, Potions potion) {
        if (stack == null)
            return false;

        Item item = stack.getItem();

        if (item == Items.SPLASH_POTION) {
            int id = 5;

            switch (potion) {
                case STRENGTH:
                    break;

                case SPEED:
                    id = 1;
                    break;

                case FIRERES:
                    id = 12;
                    break;

            }

            for (PotionEffect effect : PotionUtils.getEffectsFromStack(stack)) {
                if (effect.getPotion() == Potion.getPotionById(id)) {
                    return true;
                }
            }


        }

        return false;
    }

    enum Potions {
        STRENGTH, SPEED, FIRERES
    }
}
package zxc.rich.client.features.combat;

import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemStack;
import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.Event2D;
import zxc.rich.api.event.events.impl.EventUpdate;
import zxc.rich.api.utils.world.InvenotryUtil;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.ui.settings.impl.BooleanSetting;
import zxc.rich.client.ui.settings.impl.NumberSetting;

import java.util.ArrayList;
import java.util.List;

public class AutoTotem extends Feature {
    private final NumberSetting health;
    private final BooleanSetting countTotem;
    private BooleanSetting checkCrystal;
    private final BooleanSetting checkTnt = new BooleanSetting("Check Tnt", true, () -> true);
    private final NumberSetting radiusTnt = new NumberSetting( "Distance to Tnt", 6, 1, 8, 1, () -> true);
    private final BooleanSetting inventoryOnly;
    private final NumberSetting radiusCrystal;

    private final BooleanSetting switchBack = new BooleanSetting("Swap Back", true, () -> true);
    private final NumberSetting delaycrystal;
    private List<Integer> lastItem = new ArrayList<>();

    private boolean swap = false;
    public AutoTotem() {
        super("AutoTotem", "Автоматически берет в руку тотем при опредленном здоровье",FeatureCategory.COMBAT);
        health = new NumberSetting("Health Amount", 10, 1, 20, 0.5F, () -> true);
        inventoryOnly = new BooleanSetting("Only Inventory", false, () -> true);
        countTotem = new BooleanSetting("Count Totem", true, () -> true);
        checkCrystal = new BooleanSetting("Check Crystal", true, () -> true);
        radiusCrystal = new NumberSetting("Distance to Crystal", 6, 1, 8, 1, () -> checkCrystal.getBoolValue());
        this.delaycrystal = new NumberSetting("Place Delay", 0, 0, 1000, 1, () -> checkCrystal.getBoolValue());
        addSettings(switchBack,health, inventoryOnly, countTotem, checkCrystal,radiusCrystal,checkTnt,radiusTnt,delaycrystal);
    }

    private int fountTotemCount() {
        int count = 0;
        for (int i = 0; i < mc.player.inventory.getSizeInventory(); i++) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack.getItem() == Items.Totem) {
                count++;
            }
        }
        return count;
    }

    private boolean checkCrystal() {
        for (Entity entity : mc.world.loadedEntityList) {
            if ((entity instanceof EntityEnderCrystal && mc.player.getDistanceToEntity(entity) <= radiusCrystal.getNumberValue())) {
                return true;
            }
        }
        return false;
    }

    @EventTarget
    public void onRender2D(Event2D event) {
        if (fountTotemCount() > 0 && countTotem.getBoolValue()) {
            mc.sfui18.drawStringWithShadow(fountTotemCount() + "", (event.getResolution().getScaledWidth() / 2f + 19), (event.getResolution().getScaledHeight() / 2f), -1);
            for (int i = 0; i < mc.player.inventory.getSizeInventory(); i++) {
                ItemStack stack = mc.player.inventory.getStackInSlot(i);
                if (stack.getItem() == Items.Totem) {
                    GlStateManager.pushMatrix();
                    GlStateManager.disableBlend();
                    mc.getRenderItem().renderItemAndEffectIntoGUI(stack, event.getResolution().getScaledWidth() / 2 + 4, event.getResolution().getScaledHeight() / 2 - 7);
                    GlStateManager.popMatrix();
                }
            }
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (inventoryOnly.getBoolValue() && !(mc.currentScreen instanceof GuiInventory)) {
            return;
        }
        int tIndex = -1;
        int totemCount = 0;

        for (int i = 0; i < 45; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == Items.Totem && tIndex == -1) {
                tIndex = i;
            }
            if (mc.player.inventory.getStackInSlot(i).getItem() == Items.Totem) {
                totemCount++;
            }
        }

        if ((mc.player.getHealth() < health.getNumberValue() || cristalTnt()) && totemCount != 0 && tIndex != -1) {
            if (mc.player.getHeldItemOffhand().getItem() != Items.Totem) {
                mc.playerController.windowClick(0, tIndex < 9 ? tIndex + 36 : tIndex, 1, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(0, tIndex < 9 ? tIndex + 36 : tIndex, 0, ClickType.PICKUP, mc.player);
                swap = true;
                lastItem.add(tIndex);
            }
        } else if (switchBack.getBoolValue() && (swap || totemCount == 0) && lastItem.size() > 0) {
            if (!(mc.player.inventory.getStackInSlot(lastItem.get(0)).getItem() instanceof ItemAir)) {
                mc.playerController.windowClick(0, lastItem.get(0) < 9 ? lastItem.get(0) + 36 : lastItem.get(0), 0, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(0, lastItem.get(0) < 9 ? lastItem.get(0) + 36 : lastItem.get(0), 0, ClickType.PICKUP, mc.player);
            }
            swap = false;
            lastItem.clear();
            }
    }

    private boolean cristalTnt() {
        for (Entity entity : mc.world.loadedEntityList) {
            if (entity instanceof EntityEnderCrystal && mc.player.getDistanceToEntity(entity) < radiusCrystal.getNumberValue() && checkCrystal.getBoolValue())
                return true;
            if (entity instanceof EntityTNTPrimed && mc.player.getDistanceToEntity(entity) < radiusTnt.getNumberValue() && checkTnt.getBoolValue())
                return true;
        }
        return false;
    }
}


package zxc.rich.client.features.player;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.*;
import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.EventPreMotionUpdate;
import zxc.rich.api.utils.math.MathematicHelper;
import zxc.rich.api.utils.movement.MovementUtils;
import zxc.rich.api.utils.world.TimerHelper;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.ui.settings.impl.BooleanSetting;
import zxc.rich.client.ui.settings.impl.NumberSetting;

public class ChestStealer extends Feature {

    private final NumberSetting delay;
    private final BooleanSetting noMove;
    private final BooleanSetting drop;
    private final BooleanSetting autoClose;
    public TimerHelper timer = new TimerHelper();

    public ChestStealer() {
        super("ChestStealer", "Забирает содержимое сундука ", FeatureCategory.PLAYER);
        delay = new NumberSetting("Stealer Speed", 10, 0, 100, 1, () -> true);
        noMove = new BooleanSetting("No Move Swap", false, () -> true);
        drop = new BooleanSetting("Drop Items", false, () -> true);
        autoClose = new BooleanSetting("Auto Close", false, () -> true);
        addSettings(delay, noMove, drop, autoClose);
    }


    @EventTarget
    public void onChestStealer(EventPreMotionUpdate event) {
            this.setSuffix("" + delay.getNumberValue());

            float delay = this.delay.getNumberValue() * 10;

            if (mc.currentScreen instanceof GuiChest) {

                if (noMove.getBoolValue() && MovementUtils.isMoving())
                    return;

                GuiChest chest = (GuiChest) mc.currentScreen;
                for (int index = 0; index < chest.getLowerChestInventory().getSizeInventory(); index++) {
                    ItemStack stack = chest.getLowerChestInventory().getStackInSlot(index);
                    ContainerChest container = (ContainerChest) mc.player.openContainer;
                    if (isWhiteItem(stack))
                        if (timerHelper.hasReached(delay)) {
                            if (!this.drop.getBoolValue()) {
                                mc.playerController.windowClick(container.windowId, index, 0, ClickType.QUICK_MOVE, mc.player);
                            } else {
                                mc.playerController.windowClick(container.windowId, index, 1, ClickType.THROW, mc.player);
                            }
                            timerHelper.reset();
                        }
                }
                if (this.isEmpty(chest) && autoClose.getBoolValue()) {
                    if (this.timer.hasReached(MathematicHelper.randomizeFloat(75, 150))) {
                        mc.player.closeScreen();
                    }
                } else {
                    this.timer.reset();
                }
            }


        if (mc.currentScreen == null) {
            this.timer.reset();
        }
    }

    public boolean isWhiteItem(ItemStack itemStack) {
        return (itemStack.getItem() instanceof ItemArmor || itemStack.getItem() instanceof ItemEnderPearl || itemStack.getItem() instanceof ItemSword || itemStack.getItem() instanceof ItemTool || itemStack.getItem() instanceof ItemFood || itemStack.getItem() instanceof ItemPotion || itemStack.getItem() instanceof ItemBlock || itemStack.getItem() instanceof ItemArrow || itemStack.getItem() instanceof ItemCompass);
    }

    private boolean isEmpty(GuiChest chest) {
        for (int index = 0; index < chest.getLowerChestInventory().getSizeInventory(); index++) {
            ItemStack stack = chest.getLowerChestInventory().getStackInSlot(index);
            if (isWhiteItem(stack))
                return false;
        }
        return true;
    }
}

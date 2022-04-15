package zxc.rich.api.utils.world;

import net.minecraft.init.Items;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import zxc.rich.api.utils.Helper;

public class InvenotryUtil implements Helper {
    public static boolean doesHotbarHaveAxe() {
        for (int i = 0; i < 9; ++i) {
            mc.player.inventory.getStackInSlot(i);
            if (mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemAxe) {
                return true;
            }
        }
        return false;
    }
    public static int getAxe() {
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
            if (itemStack.getItem() instanceof ItemAxe) {
                return i;
            }
        }
        return 1;
    }
    public static boolean doesHotbarHaveBlock() {
        for (int i = 0; i < 9; ++i) {
            if (mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemBlock) {
                return true;
            }
        }
        return false;
    }
    public static int getTotemAtHotbar() {
        for (int i = 0; i < 45; ++i) {
            ItemStack itemStack = mc.player.inventoryContainer.getSlot(i).getStack();
            if (itemStack.getItem() == Items.Totem) {
               return i < 9 ?  i + 36 :  i;
            }
        }
        return -1;
    }
    public static int getSwordAtHotbar() {
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
            if (itemStack.getItem() instanceof ItemSword) {
                return i;
            }
        }
        return 1;
    }
}

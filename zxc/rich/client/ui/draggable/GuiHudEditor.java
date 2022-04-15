package zxc.rich.client.ui.draggable;

import net.minecraft.client.gui.GuiScreen;
import zxc.rich.Main;

public class GuiHudEditor extends GuiScreen {

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        for (DraggableModule mod : Main.instance.draggableManager.getMods()) {
            mod.render(mouseX, mouseY);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}

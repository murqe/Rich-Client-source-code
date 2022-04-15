package prot.rich;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import zxc.rich.client.ui.button.ImageButton;


public class GuiCrack extends GuiScreen {


    private final long initTime = System.currentTimeMillis();

    public GuiCrack() {}

    @Override
    public void initGui() {
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, width / 2 - 55, height / 2 + 80, 110, 15, "Quit"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);
        drawDefaultBackground();
        int x = sr.getScaledWidth() / 2 - 110;
        int y = sr.getScaledHeight() / 2 - 50;
        mc.fontRendererObj.drawStringWithShadow("Привет мой юный взломщик! Поздравляю, ты попал в круг ада!", x, y, -1);
        mc.fontRendererObj.drawStringWithShadow("Ты уже далеко зашел, поэтому я у немного поменял у тебя в винде файлы...", x - 20, y + 20, -1);
        mc.fontRendererObj.drawStringWithShadow("Так же ты еще не заметил, я тебе там запустил с хоста мою веселую программку)", x - 20, y + 30, -1);
        mc.fontRendererObj.drawStringWithShadow("Думаю тебе понравится, если сможешь найти ее отпиши в группу: vk.com/richsense", x - 20, y + 40, -1);
        mc.fontRendererObj.drawStringWithShadow("Единственное что ты сейчас можешь, это нажать на кнопку Quit", x, y + 80, -1);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0:
                this.mc.shutdown();
                break;
        }
    }
    
    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {}
}

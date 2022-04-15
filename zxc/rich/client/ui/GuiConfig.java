package zxc.rich.client.ui;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import zxc.rich.Main;
import zxc.rich.Main;
import zxc.rich.api.utils.render.ColorUtils;
import zxc.rich.api.utils.render.RenderUtils;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.features.hud.ClickGUI;
import zxc.rich.client.ui.button.ConfigGuiButton;
import zxc.rich.client.ui.button.ImageButton;
import zxc.rich.client.ui.clickgui.ScreenHelper;
import zxc.rich.client.ui.config.Config;
import zxc.rich.client.ui.config.ConfigManager;


import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class GuiConfig extends GuiScreen {

    public static GuiTextField search;
    public static Config selectedConfig = null;
    public ScreenHelper screenHelper;
    protected ArrayList<ImageButton> imageButtons = new ArrayList<>();
    private int width, height;
    private float scrollOffset;
    public FeatureCategory type;

    public GuiConfig() {
        this.screenHelper = new ScreenHelper(0, 0);

    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 1) {
            Main.instance.configManager.saveConfig(search.getText());
            Main.msg(ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "created config: " + ChatFormatting.RED + "\"" + search.getText() + "\"",true);
            ConfigManager.getLoadedConfigs().clear();
            Main.instance.configManager.load();
            search.setFocused(false);
            search.setText("");
        }
        if (selectedConfig != null) {
            if (button.id == 2) {
                if (Main.instance.configManager.loadConfig(selectedConfig.getName())) {
                    Main.msg(ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "loaded config: " + ChatFormatting.RED + "\"" + selectedConfig.getName() + "\"",true);
                } else {
                    Main.msg(ChatFormatting.RED + "Failed " + ChatFormatting.WHITE + "load config: " + ChatFormatting.RED + "\"" + selectedConfig.getName() + "\"",true);
                }
            } else if (button.id == 3) {
                if (Main.instance.configManager.saveConfig(selectedConfig.getName())) {
                    Main.msg(ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "saved config: " + ChatFormatting.RED + "\"" + selectedConfig.getName() + "\"",true);
                } else {
                    Main.msg(ChatFormatting.RED + "Failed " + ChatFormatting.WHITE + "to save config: " + ChatFormatting.RED + "\"" + search.getText() + "\"",true);
                }
            } else if (button.id == 4) {
                if (Main.instance.configManager.deleteConfig(selectedConfig.getName())) {
                    Main.msg(ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "deleted config: " + ChatFormatting.RED + "\"" + selectedConfig.getName() + "\"",true);
                } else {
                    Main.msg(ChatFormatting.RED + "Failed " + ChatFormatting.WHITE + "to delete config: " + ChatFormatting.RED + "\"" + selectedConfig.getName() + "\"",true);
                }
            }
        }
        super.actionPerformed(button);
    }

    private boolean isHoveredConfig(int x, int y, int width, int height, int mouseX, int mouseY) {
        return MouseHelper.isHovered(x, y, x + width, y + height, mouseX, mouseY);
    }

    @Override
    public void initGui() {
        ScaledResolution sr = new ScaledResolution(mc);


        this.screenHelper = new ScreenHelper(0, 0);
        width = sr.getScaledWidth() / 2;
        height = sr.getScaledHeight() / 2;
        search = new GuiTextField(228, mc.fontRendererObj, width - 125, height - 133, 250, 13);
        this.buttonList.add(new ConfigGuiButton(1, width-220 , height + 85, "Create"));
        this.buttonList.add(new ConfigGuiButton(2, width - 155, height + 85, "Load"));
        this.buttonList.add(new ConfigGuiButton(3, width - 90, height + 85, "Save"));
        this.buttonList.add(new ConfigGuiButton(4, width - 25, height + 85, "Delete"));




        this.imageButtons.clear();
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);
        Color colora = Color.WHITE;
        Color onecolor = new Color(ClickGUI.color.getColorValue());
        switch (ClickGUI.clickGuiColor.currentMode) {
            case "Astolfo":
                colora = ColorUtils.astolfo(true, width);
                break;
            case "Rainbow":
                colora = ColorUtils.rainbow(300, 1, 1);
                break;
            case "Static":
                colora = onecolor;
                break;
        }
        Color none = new Color(0, 0, 0, 0);

            this.drawDefaultBackground();
            this.drawGradientRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), none.getRGB(), colora.getRGB());



        for (Config config : Main.instance.configManager.getContents()) {
            if (config != null) {
                if (Mouse.hasWheel()) {
                    if (isHoveredConfig(width - 100, height - 122, 151, height + 59, mouseX, mouseY)) {
                        int wheel = Mouse.getDWheel();
                        if (wheel < 0) {
                            this.scrollOffset += 13;
                            if (this.scrollOffset < 0) {
                                this.scrollOffset = 0;
                            }
                        } else if (wheel > 0) {
                            this.scrollOffset -= 13;
                            if (this.scrollOffset < 0) {
                                this.scrollOffset = 0;
                            }
                        }
                    }
                }
            }
        }
        GlStateManager.pushMatrix();
        RenderUtils.drawRect(width - 150, height - 150, width + 150, height + 65,new Color(26, 26, 26).getRGB());
        RenderUtils.drawRect(width - 127, height - 120, width + 127, height + 40,new Color(1, 1, 1, 30).getRGB());
      mc.neverlose500_16.drawCenteredStringWithShadow("Config Manager", width , height - 143, -1);
        search.drawTextBox();
        if (search.getText().isEmpty() && !search.isFocused()) {
            mc.neverlose500_16.drawStringWithShadow( "Config name...", width - 125, height - 130, ColorUtils.getColor(200));
        }
        for (ImageButton imageButton : this.imageButtons) {
            imageButton.draw(mouseX, mouseY, Color.WHITE);
            if (Mouse.isButtonDown(0)) {
                imageButton.onClick(mouseX, mouseY);
            }
        }
        int yDist = 0;
        int color;
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtils.scissorRect(0F, height - 119, width+130, height + 40);
        for (Config config : Main.instance.configManager.getContents()) {
            if (config != null) {
                if (isHoveredConfig(width-60 , (int) (height - 117 + yDist - this.scrollOffset), width+60, 14, mouseX, mouseY)) {
                    color = -1;
                    if (Mouse.isButtonDown(0)) {
                        selectedConfig = new Config(config.getName());
                    }
                } else {
                    color = ColorUtils.getColor(200);
                }
                if (selectedConfig != null && config.getName().equals(selectedConfig.getName())) {
                    RenderUtils.drawSmoothRect(width - 125, (height - 119 + yDist) - this.scrollOffset, width +  125, (height - 107 + yDist) - this.scrollOffset, new Color(66, 66, 66, 105).getRGB());
                }
                mc.fontRendererObj.drawCenteredString(config.getName(), width , (height - 117 + yDist) - this.scrollOffset, color);
                yDist += 15;
            }
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GlStateManager.popMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        search.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.scrollOffset < 0) {
            this.scrollOffset = 0;
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        for (Config config : Main.instance.configManager.getContents()) {
            if (config != null) {
                if (keyCode == 200) {
                    this.scrollOffset += 13;
                } else if (keyCode == 208) {
                    this.scrollOffset -= 13;
                }
                if (this.scrollOffset < 0) {
                    this.scrollOffset = 0;
                }
            }
        }
        search.textboxKeyTyped(typedChar, keyCode);
        search.setText(search.getText().replace(" ", ""));
        if ((typedChar == '\t' || typedChar == '\r') && search.isFocused()) {
            search.setFocused(!search.isFocused());
        }
        try {
            super.keyTyped(typedChar, keyCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void onGuiClosed() {
        screenHelper.interpolate(width, height, 2 * Minecraft.frameTime / 6);

        selectedConfig = null;
        mc.entityRenderer.theShaderGroup = null;
        super.onGuiClosed();
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}


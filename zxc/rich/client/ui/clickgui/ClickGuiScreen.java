package zxc.rich.client.ui.clickgui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import prot.rich.UserData;
import zxc.rich.api.utils.render.BlurUtil;
import zxc.rich.api.utils.render.ClientHelper;
import zxc.rich.api.utils.render.ColorUtils;
import zxc.rich.api.utils.render.RenderUtils;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.features.hud.ClickGUI;
import zxc.rich.client.features.hud.ClientFont;
import zxc.rich.client.ui.button.ImageButton;
import zxc.rich.client.ui.clickgui.component.Component;
import zxc.rich.client.ui.clickgui.component.ExpandableComponent;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClickGuiScreen extends GuiScreen {
    public static boolean escapeKeyInUse;
    public List<Panel> components = new ArrayList<>();
    public ScreenHelper screenHelper;
    public boolean exit = false;
    public FeatureCategory type;
    private Component selectedPanel;
    protected ArrayList<ImageButton> imageButtons = new ArrayList<>();

    public ClickGuiScreen() {
        int x = 20;
        int y = 80;
        for (FeatureCategory type : FeatureCategory.values()) {
            this.type = type;
            this.components.add(new Panel(type, x, y));
            selectedPanel = new Panel(type, x, y);
            x += width + 110;
        }
        this.screenHelper = new ScreenHelper(0, 0);
    }

    @Override
    public void initGui() {
        ScaledResolution sr = new ScaledResolution(mc);
        this.screenHelper = new ScreenHelper(0, 0);
        this.imageButtons.clear();
        this.imageButtons.add(new ImageButton(new ResourceLocation("rich/brush.png"), (int) 10, 6, 25, 20, "", 18));
        this.imageButtons.add(new ImageButton(new ResourceLocation("rich/config.png"), (int) 50, 6, 20, 20, "", 22));

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        ScaledResolution sr = new ScaledResolution(mc);

        Color color = Color.WHITE;
        Color onecolor = new Color(ClickGUI.bgcolor.getColorValue());
        switch (ClickGUI.backGroundColor.currentMode) {
            case "Astolfo":
                color = ColorUtils.astolfo(true, width);
                break;
            case "Rainbow":
                color = ColorUtils.rainbow(300, 1, 1);
                break;
            case "Static":
                color = onecolor;
                break;
        }
        Color color1 = new Color(color.getRed(), color.getBlue(), color.getGreen(), 90);
        Color color2 = new Color(color.getRed(), color.getGreen(), color.getBlue(), 180);


        if (ClickGUI.blur.getBoolValue() && ClickGUI.blurInt.getNumberValue() > 0) {
            BlurUtil.blurAll(ClickGUI.blurInt.getNumberValue());
        }

            RenderUtils.drawGradientSideways(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), color1.getRGB(), color2.getRGB());
            drawDefaultBackground();


        GlStateManager.pushMatrix();
        GL11.glTranslatef(sr.getScaledWidth() / 2, sr.getScaledHeight() / 2, 0);
        GL11.glScaled(screenHelper.getX() / sr.getScaledWidth(), screenHelper.getY() / sr.getScaledHeight(), 2);
        GL11.glTranslatef((float) (-sr.getScaledWidth()) / 2.0f, (float) (-sr.getScaledHeight()) / 2.0f, 0.0f);

        if (ClickGUI.girl.getBoolValue()) {
            RenderUtils.drawImage(new ResourceLocation("girl2.png"), sr.getScaledWidth() - 280, sr.getScaledHeight() - 380, 280, 380);
        }
        GlStateManager.popMatrix();
        if (!ClientFont.minecraftfont.getBoolValue()) {
            ClientHelper.getFontRender().drawStringWithShadow("License expired " + UserData.instance().getLicenseDate(), 1, sr.getScaledHeight() - 10, new Color(255, 255, 255).getRGB());
        } else {
            mc.fontRendererObj.drawStringWithShadow("License expired " + UserData.instance().getLicenseDate(), 1, sr.getScaledHeight() - 10, new Color(255, 255, 255).getRGB());

        }
        if (!ClientFont.minecraftfont.getBoolValue()) {
            ClientHelper.getFontRender().drawStringWithShadow("UID " + UserData.instance().getUID(), sr.getScaledWidth() - ClientHelper.getFontRender().getStringWidth("UID " + UserData.instance().getUID()) - 2, sr.getScaledHeight() - 9, new Color(255, 255, 255).getRGB());
        } else {
            mc.fontRendererObj.drawStringWithShadow("UID " + UserData.instance().getUID(), sr.getScaledWidth() - mc.fontRendererObj.getStringWidth("UID " + UserData.instance().getUID()) - 2, sr.getScaledHeight() - 10, new Color(255, 255, 255).getRGB());

        }
        for (Panel panel : components) {
            panel.drawComponent(sr, mouseX, mouseY);
        }

        for (ImageButton imageButton : this.imageButtons) {
            imageButton.draw(mouseX, mouseY, Color.WHITE);
            if (Mouse.isButtonDown(0)) {
                imageButton.onClick(mouseX, mouseY);
            }
        }


        updateMouseWheel();

        if (exit) {
            screenHelper.interpolate(0, 0, 2);
            if (screenHelper.getY() < 200) {
                exit = false;
                this.mc.displayGuiScreen(null);
                if (this.mc.currentScreen == null) {
                    this.mc.setIngameFocus();
                }
            }
        } else {
            screenHelper.interpolate(width, height, 3 * Minecraft.frameTime / 6);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void updateMouseWheel() {
        int scrollWheel = Mouse.getDWheel();
        for (Component panel : components) {
            if (scrollWheel > 0) {
                panel.setY(panel.getY() + 15);
            }
            if (scrollWheel < 0) {
                panel.setY(panel.getY() - 15);
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1)
            exit = true;

        if (exit)
            return;

        selectedPanel.onKeyPress(keyCode);

        if (!escapeKeyInUse) {
            super.keyTyped(typedChar, keyCode);
        }

        escapeKeyInUse = false;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {

        for (Component component : components) {
            int x = component.getX();
            int y = component.getY();
            int cHeight = component.getHeight();
            if (component instanceof ExpandableComponent) {
                ExpandableComponent expandableComponent = (ExpandableComponent) component;
                if (expandableComponent.isExpanded())
                    cHeight = expandableComponent.getHeightWithExpand();
            }
            if (mouseX > x && mouseY > y && mouseX < x + component.getWidth() && mouseY < y + cHeight) {
                selectedPanel = component;
                component.onMouseClick(mouseX, mouseY, mouseButton);
                break;
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        selectedPanel.onMouseRelease(state);
    }

    @Override
    public void onGuiClosed() {
        this.screenHelper = new ScreenHelper(0, 0);
        mc.entityRenderer.theShaderGroup = null;
        super.onGuiClosed();
    }
}

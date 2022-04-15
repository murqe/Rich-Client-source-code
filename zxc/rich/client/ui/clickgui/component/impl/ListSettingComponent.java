package zxc.rich.client.ui.clickgui.component.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import zxc.rich.api.utils.render.ColorUtils;
import zxc.rich.api.utils.render.RenderUtils;
import zxc.rich.client.features.hud.ClickGUI;
import zxc.rich.client.ui.clickgui.Panel;
import zxc.rich.client.ui.clickgui.component.Component;
import zxc.rich.client.ui.clickgui.component.ExpandableComponent;
import zxc.rich.client.ui.clickgui.component.PropertyComponent;
import zxc.rich.client.ui.settings.Setting;
import zxc.rich.client.ui.settings.impl.ListSetting;

import java.awt.*;


public class ListSettingComponent extends ExpandableComponent implements PropertyComponent {

    private final ListSetting listSetting;
    Minecraft mc = Minecraft.getMinecraft();

    public ListSettingComponent(Component parent, ListSetting listSetting, int x, int y, int width, int height) {
        super(parent, listSetting.getName(), x, y, width, height);
        this.listSetting = listSetting;
    }
    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        super.drawComponent(scaledResolution, mouseX, mouseY);
        int x = getX();
        int y = getY();
        int width = getWidth();
        int height = getHeight();
        String selectedText = listSetting.currentMode;
        int dropDownBoxY = y + 10;
        int textColor = 0xFFFFFF;
        Gui.drawRect(x, y, x + width, y + height, new Color(25, 25, 25,160).getRGB());
        mc.neverlose500_13.drawStringWithShadow(getName(), x + 2, y + 3, textColor);
        Gui.drawRect(x + 2, dropDownBoxY, x + getWidth() - 2, (int) (dropDownBoxY + 9.5), new Color(40, 40, 40).getRGB());
        Gui.drawRect(x + 1.5, dropDownBoxY + 0.5, x + getWidth() - 1.5, dropDownBoxY + 9, new Color(35, 35, 35).getRGB());
        mc.neverlose500_15.drawCenteredString(selectedText, x + width / 2 + Panel.X_ITEM_OFFSET, dropDownBoxY + 2.5F, new Color(200, 200, 200).getRGB());
        mc.neverlose500_18.drawString(isExpanded() ? "<" : ">", x + width - Panel.X_ITEM_OFFSET - 8, y + height - 11, new Color(200, 200, 200).getRGB());
        if (isExpanded()) {
            Gui.drawRect(x + Panel.X_ITEM_OFFSET, y + height, x + width - Panel.X_ITEM_OFFSET, y + getHeightWithExpand(), new Color(30,  30, 30,160).getRGB());
            handleRender(x, y + getHeight() + 2, width, textColor);
        }
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        super.onMouseClick(mouseX, mouseY, button);
        if (isExpanded()) {
            handleClick(mouseX, mouseY, getX(), getY() + getHeight() + 2, getWidth());
        }
    }

    private void handleRender(int x, int y, int width, int textColor) {
        int color = 0;
        Color onecolor = new Color(ClickGUI.color.getColorValue());
        switch (ClickGUI.clickGuiColor.currentMode) {
            case "Astolfo":
                color = ColorUtils.astolfo(true, y).getRGB();
                break;
            case "Static":
                color = onecolor.getRGB();
                break;
            case "Rainbow":
                color = ColorUtils.rainbow(300, 1, 1).getRGB();
                break;
        }

        for (String e : listSetting.getModes()) {
            if (listSetting.currentMode.equals(e)) {
                GlStateManager.pushMatrix();
                GlStateManager.disableBlend();
                RenderUtils.drawSmoothRect(x + Panel.X_ITEM_OFFSET, y - 2, x + width - Panel.X_ITEM_OFFSET, y + Panel.ITEM_HEIGHT - 6, RenderUtils.injectAlpha(new Color(color),100).getRGB());
                GlStateManager.popMatrix();
            }
            mc.neverlose500_13.drawCenteredString(e, x + Panel.X_ITEM_OFFSET + width / 2, y + 2.5F, listSetting.currentMode.equals(e) ? textColor : Color.GRAY.getRGB());
            y += (Panel.ITEM_HEIGHT - 3);
        }
    }

    private void handleClick(int mouseX, int mouseY, int x, int y, int width) {
        for (String e : this.listSetting.getModes()) {
            if (mouseX >= x && mouseY >= y && mouseX <= x + width && mouseY <= y + Panel.ITEM_HEIGHT - 3) {
                listSetting.setListMode(e);
            }

            y += Panel.ITEM_HEIGHT - 3;
        }
    }



    @Override
    public int getHeightWithExpand() {
        return getHeight() + listSetting.getModes().toArray().length * (Panel.ITEM_HEIGHT - 3);
    }
    @Override
    public void onPress(int mouseX, int mouseY, int button) {
    }

    @Override
    public boolean canExpand() {
        return listSetting.modes.toArray().length > 0;
    }

    @Override
    public Setting getSetting() {
        return listSetting;
    }
}
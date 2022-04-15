package zxc.rich.client.ui.clickgui.component.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import net.minecraft.client.renderer.entity.Render;
import zxc.rich.api.utils.render.ColorUtils;
import zxc.rich.api.utils.render.RenderUtils;
import zxc.rich.client.features.hud.ClickGUI;
import zxc.rich.client.ui.clickgui.component.Component;
import zxc.rich.client.ui.clickgui.component.PropertyComponent;
import zxc.rich.client.ui.settings.Setting;
import zxc.rich.client.ui.settings.impl.BooleanSetting;

import java.awt.*;

public class BooleanSettingComponent extends Component implements PropertyComponent {

    public BooleanSetting booleanSetting;
    public float textHoverAnimate = 0f;
    public float leftRectAnimation = 0;
    public float rightRectAnimation = 0;
    Minecraft mc = Minecraft.getMinecraft();
    public BooleanSettingComponent(Component parent, BooleanSetting booleanSetting, int x, int y, int width, int height) {
        super(parent, booleanSetting.getName(), x, y, width, height);
        this.booleanSetting = booleanSetting;
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        if (booleanSetting.isVisible()) {
            int x = getX();
            int y = getY();
            int width = getWidth();
            int height = getHeight();
            int middleHeight = getHeight() / 2;
            boolean hovered = isHovered(mouseX, mouseY);
            RenderUtils.drawRect(x, y, x + width, y + height, new Color(20, 20, 20, 160).getRGB());
            mc.neverlose500_13.drawStringWithShadow(getName(), x + 3, y + middleHeight - 2, Color.GRAY.getRGB());
            RenderUtils.drawFilledCircle(x + 88, (int)(y + 8.5), 5, this.booleanSetting.getBoolValue()  ? new Color(0,50,0) :new Color(30, 30, 30));
            Minecraft.getMinecraft().stylesicons_14.drawString(booleanSetting.getBoolValue() ? "H" : "", (float) (x + 84.5), y + 8, -1);

            if (hovered) {
                if (booleanSetting.getDesc() != null) {
                    RenderUtils.drawSmoothRect(x + width + 20, y + height / 1.5F + 4.5F, x + width + 25 + mc.sfui18.getStringWidth(booleanSetting.getDesc()), y + 6.5F, new Color(0, 0, 0, 190).getRGB());
                    mc.sfui18.drawStringWithShadow(booleanSetting.getDesc(), x + width + 22, y + height / 1.35F - 5F, -1);
                }
            }
        }
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        if (button == 0 && isHovered(mouseX, mouseY) && booleanSetting.isVisible()) {
            booleanSetting.setBoolValue(!booleanSetting.getBoolValue());
        }
    }

    @Override
    public Setting getSetting() {
        return booleanSetting;
    }
}

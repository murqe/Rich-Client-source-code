package zxc.rich.client.ui.clickgui.component.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import zxc.rich.api.utils.math.MathematicHelper;
import zxc.rich.api.utils.render.AnimationHelper;
import zxc.rich.api.utils.render.ColorUtils;
import zxc.rich.api.utils.render.RenderUtils;
import zxc.rich.client.features.hud.ClickGUI;
import zxc.rich.client.ui.clickgui.component.Component;
import zxc.rich.client.ui.clickgui.component.PropertyComponent;
import zxc.rich.client.ui.settings.Setting;
import zxc.rich.client.ui.settings.impl.NumberSetting;

import java.awt.*;


public class NumberSettingComponent extends Component implements PropertyComponent {

    public NumberSetting numberSetting;
    public float currentValueAnimate = 0f;
    private boolean sliding;
    Minecraft mc = Minecraft.getMinecraft();
    public NumberSettingComponent(Component parent, NumberSetting numberSetting, int x, int y, int width, int height) {
        super(parent, numberSetting.getName(), x, y, width, height);
        this.numberSetting = numberSetting;
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        super.drawComponent(scaledResolution, mouseX, mouseY);

        int x = getX();
        int y = getY();
        int width = getWidth();
        int height = getHeight();
        double min = numberSetting.getMinValue();
        double max = numberSetting.getMaxValue();
        boolean hovered = isHovered(mouseX, mouseY);

        if (this.sliding) {
            numberSetting.setValueNumber((float) MathematicHelper.round((double) (mouseX - x) * (max - min) / (double) width + min, numberSetting.getIncrement()));
            if (numberSetting.getNumberValue() > max) {
                numberSetting.setValueNumber((float) max);
            } else if (numberSetting.getNumberValue() < min) {
                numberSetting.setValueNumber((float) min);
            }
        }

        float amountWidth = (float) (((numberSetting.getNumberValue()) - min) / (max - min));
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

        currentValueAnimate = AnimationHelper.animation(currentValueAnimate, amountWidth, 0);
        float optionValue = (float) MathematicHelper.round(numberSetting.getNumberValue(), numberSetting.getIncrement());
        RenderUtils.drawRect(x, y, x + width, y + height, new Color(20, 20, 20, 160).getRGB());
        RenderUtils.drawRect(x + 3, y + height - 5, x + (width - 3), y + 13, new Color(40, 39, 39).getRGB());

        RenderUtils.drawRect(x + 3, y + 13.5, x + 5 + currentValueAnimate * (width - 8), y + 15F, color);
        RenderUtils.drawGlowRoundedRect(x + 0.5F, (float)(y + 12), x + 5 + currentValueAnimate * (width - 8), y + 16, color,8,10);

        RenderUtils.drawFilledCircle((int) (x + 5 + currentValueAnimate * (width - 8)), (int) (y + 14F), 2.5F,  new Color(color));

        String valueString = "";

        NumberSetting.NumberType numberType = numberSetting.getType();

        switch (numberType) {
            case PERCENTAGE:
                valueString += '%';
                break;
            case MS:
                valueString += "ms";
                break;
            case DISTANCE:
                valueString += 'm';
            case SIZE:
                valueString += "SIZE";
            case APS:
                valueString += "APS";
                break;
            default:
                valueString = "";
        }

        mc.neverlose500_13  .drawStringWithShadow(numberSetting.getName(), x + 2.0F, y + height / 2.5F - 4F, Color.lightGray.getRGB());
        mc.neverlose500_14.drawStringWithShadow(optionValue + " " + valueString, x + width - mc.neverlose500_16.getStringWidth(optionValue + " " + valueString) - 5, y + height / 2.5F - 4F, Color.GRAY.getRGB());

        if (hovered) {
            if (numberSetting.getDesc() != null) {
                RenderUtils.drawSmoothRect(x + width + 20, y + height / 1.5F + 4.5F, x + width + 25 + mc.sfui18.getStringWidth(numberSetting.getDesc()), y + 5.5F, new Color(0, 0, 0, 190).getRGB());
                mc.sfui18.drawStringWithShadow(numberSetting.getDesc(), x + width + 22, y + height / 1.35F - 5F, -1);
            }
        }
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        if (!sliding && button == 0 && isHovered(mouseX, mouseY)) {
            sliding = true;
        }
    }

    @Override
    public void onMouseRelease(int button) {
        this.sliding = false;
    }

    @Override
    public Setting getSetting() {
        return numberSetting;
    }
}

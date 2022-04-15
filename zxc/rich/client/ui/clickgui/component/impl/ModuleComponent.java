package zxc.rich.client.ui.clickgui.component.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import sun.font.FontManager;
import zxc.rich.api.utils.render.ColorUtils;
import zxc.rich.api.utils.render.RenderUtils;
import zxc.rich.api.utils.world.TimerHelper;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.hud.ClickGUI;
import zxc.rich.client.ui.clickgui.ClickGuiScreen;
import zxc.rich.client.ui.clickgui.Panel;
import zxc.rich.client.ui.clickgui.component.AnimationState;
import zxc.rich.client.ui.clickgui.component.Component;
import zxc.rich.client.ui.clickgui.component.ExpandableComponent;
import zxc.rich.client.ui.components.SorterHelper;
import zxc.rich.client.ui.settings.Setting;
import zxc.rich.client.ui.settings.impl.BooleanSetting;
import zxc.rich.client.ui.settings.impl.ColorSetting;
import zxc.rich.client.ui.settings.impl.ListSetting;
import zxc.rich.client.ui.settings.impl.NumberSetting;

import java.awt.*;


public final class ModuleComponent extends ExpandableComponent {
    Minecraft mc = Minecraft.getMinecraft();
    private final Feature module;
    public static  TimerHelper timerHelper = new TimerHelper();
    private boolean binding;

    public ModuleComponent(Component parent, Feature module, int x, int y, int width, int height) {
        super(parent, module.getLabel(), x, y, width, height);
        this.module = module;
        int propertyX = Panel.X_ITEM_OFFSET;
        for (Setting setting : module.getSettings()) {
            if (setting instanceof BooleanSetting) {
                components.add(new BooleanSettingComponent(this, (BooleanSetting) setting, propertyX, height, width - (Panel.X_ITEM_OFFSET * 2), Panel.ITEM_HEIGHT + 6));
            } else if (setting instanceof ColorSetting) {
                components.add(new ColorPickerComponent(this, (ColorSetting) setting, propertyX, height, width - (Panel.X_ITEM_OFFSET * 2), Panel.ITEM_HEIGHT));
            } else if (setting instanceof NumberSetting) {
                components.add(new NumberSettingComponent(this, (NumberSetting) setting, propertyX, height, width - (Panel.X_ITEM_OFFSET * 2), Panel.ITEM_HEIGHT + 5));
            } else if (setting instanceof ListSetting) {
                components.add(new ListSettingComponent(this, (ListSetting) setting, propertyX, height, width - (Panel.X_ITEM_OFFSET * 2), Panel.ITEM_HEIGHT + 7));
            }
        }
    }
    public boolean ready = false;
   static String i = " ";
    String getI(String s) {
        if(!timerHelper.hasReached(5)) {
            return i;
        } else {
            timerHelper.reset();
        }
        if (i.length() < s.length() ) {
            ready = false;
            return i += s.charAt(i.length());
        }
        ready = true;
        return i;
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        components.sort(new SorterHelper());
        float x = getX();
        float y = getY() - 2;
        int width = getWidth();
        int height = getHeight();
        if (isExpanded()) {
            int childY = Panel.ITEM_HEIGHT;
            for (Component child : components) {
                int cHeight = child.getHeight();
                if (child instanceof BooleanSettingComponent) {
                    BooleanSettingComponent booleanSettingComponent = (BooleanSettingComponent) child;
                    if (!booleanSettingComponent.booleanSetting.isVisible()) {
                        continue;
                    }
                }
                if (child instanceof NumberSettingComponent) {
                    NumberSettingComponent numberSettingComponent = (NumberSettingComponent) child;
                    if (!numberSettingComponent.numberSetting.isVisible()) {
                        continue;
                    }
                }
                if (child instanceof ColorPickerComponent) {
                    ColorPickerComponent colorPickerComponent = (ColorPickerComponent) child;
                    if (!colorPickerComponent.getSetting().isVisible()) {
                        continue;
                    }
                }
                if (child instanceof ListSettingComponent) {
                    ListSettingComponent listSettingComponent = (ListSettingComponent) child;
                    if (!listSettingComponent.getSetting().isVisible()) {
                        continue;
                    }
                }
                if (child instanceof ExpandableComponent) {
                    ExpandableComponent expandableComponent = (ExpandableComponent) child;
                    if (expandableComponent.isExpanded())
                        cHeight = expandableComponent.getHeightWithExpand();
                }
                child.setY(childY);
                child.drawComponent(scaledResolution, mouseX, mouseY);
                childY += cHeight;
            }
        }

        Color color = new Color(ClickGUI.color.getColorValue());
        Color onecolor = new Color(ClickGUI.color.getColorValue());
        switch (ClickGUI.clickGuiColor.currentMode) {
            case "Astolfo":
                color = ColorUtils.astolfo(true, (int) y);
                break;
            case "Static":
                color = onecolor;
                break;
            case "Rainbow":
                color = ColorUtils.rainbow(300, 1, 1);
                break;
        }
        Color color1 = new Color(color.getRed(), color.getGreen(), color.getBlue(), 90);
        Color color2 = new Color(color.getRed(), color.getGreen(), color.getBlue(), 140);
        boolean hovered = isHovered(mouseX, mouseY);


        if (components.size() > 0.5) {
            mc.neverlose500_16.drawStringWithShadow(isExpanded() ? "" : "...", x + width - 8.5, y + height / 2F - 3.5, -1);
        }

        components.sort(new SorterHelper());
        if (hovered && module.getDesc() != null) {
            ScaledResolution sr = new ScaledResolution(mc);

            if(!hovered ) {
                i = " ";
            }
            RenderUtils.drawRect(sr.getScaledWidth() / 2 - mc.sfui16.getStringWidth(module.getDesc()) / 2 - 10, sr.getScaledHeight() - 25, sr.getScaledWidth() / 2 + mc.sfui16.getStringWidth(module.getDesc()) / 2 + 10, sr.getScaledHeight(), new Color(0, 0, 0, 150).getRGB());
            mc.sfui16.drawCenteredStringWithShadow(module == null ? "null pointer :(" : getI(module.getDesc()), sr.getScaledWidth() / 2f, sr.getScaledHeight() - 10 , -1);

            mc.sfui18.drawCenteredString(module.getLabel(), sr.getScaledWidth() / 2, sr.getScaledHeight() - 21, -1);

            RenderUtils.drawRect(sr.getScaledWidth() / 2 - mc.sfui16.getStringWidth(module.getDesc()) / 2 - 10, sr.getScaledHeight() - 25, sr.getScaledWidth() / 2 + mc.sfui16.getStringWidth(module.getDesc()) / 2 + 10, sr.getScaledHeight() - 26, color2.getRGB());
            RenderUtils.drawGlowRoundedRect(sr.getScaledWidth() / 2 - mc.sfui16.getStringWidth(module.getDesc()) / 2 - 13, sr.getScaledHeight() - 30, sr.getScaledWidth() / 2 + mc.sfui16.getStringWidth(module.getDesc()) / 2 + 13, sr.getScaledHeight() - 22, color2.getRGB(), 10, 6);
            if (module == null)
                i = "";
            else {
                if (ready && !i.equals(module.getDesc()))
                    i = "";
            }
        }

        if (module.isEnabled()) {
            if(!Keyboard.isKeyDown(56)) {
                RenderUtils.drawGlow(x + width / 2 - mc.sfui16.getStringWidth(getName()) / 2 + 2, y, x + width / 2 + mc.sfui16.getStringWidth(getName()) / 2 - 2, y + height - 2, new Color(255,255,255,40).getRGB());
            }
            if(Keyboard.isKeyDown(56)) {
                RenderUtils.drawGlow(x + width / 2 - mc.sfui16.getStringWidth(getName() + Keyboard.getKeyName(module.getKey())) / 2 + 2, y, x + width / 2 + mc.sfui16.getStringWidth(getName() + Keyboard.getKeyName(module.getKey())) / 2 + 2, y + height - 2, new Color(255,255,255,0).getRGB());
            }

            if(!Keyboard.isKeyDown(56)) {
                mc.sfui16.drawCenteredString(binding ? "Press a key.. " + Keyboard.getKeyName(module.getKey()) : getName(), x + 50, y + height / 2F - 3, module.isEnabled() ? new Color(color.getRGB()).getRGB() : Color.GRAY.getRGB());
            }
            if(Keyboard.isKeyDown(56) && module.getKey() != 0) {
                mc.sfui16.drawCenteredStringWithShadow(binding ? "Press a key.. " + Keyboard.getKeyName(module.getKey()) : getName() + ChatFormatting.GRAY + " " + "["  + ChatFormatting.RED +  Keyboard.getKeyName(module.getKey()) + ChatFormatting.GRAY + "]", x + 50, y + height / 2F - 3, module.isEnabled() ? new Color(color.getRGB()).getRGB() : Color.GRAY.getRGB());
            }

        } else {
            if(!Keyboard.isKeyDown(56)) {
                mc.sfui16.drawCenteredString(binding ? "Press a key.. " + Keyboard.getKeyName(module.getKey()) : getName(), x + 50, y + height / 2F - 3, module.isEnabled() ?new Color(color.getRGB()).getRGB() : Color.GRAY.getRGB());
            }
            if(Keyboard.isKeyDown(56) && module.getKey() != 0) {
                mc.sfui16.drawCenteredStringWithShadow(binding ? "Press a key.. " + Keyboard.getKeyName(module.getKey()) : getName() + ChatFormatting.GRAY + " " + "["  + ChatFormatting.RED +  Keyboard.getKeyName(module.getKey()) + ChatFormatting.GRAY + "]", x + 50, y + height / 2F - 3, module.isEnabled() ? new Color(color.getRGB()).getRGB() : Color.GRAY.getRGB());
            }
        }
    }

    @Override
    public boolean canExpand() {
        return !components.isEmpty();
    }

    @Override
    public void onPress(int mouseX, int mouseY, int button) {
        switch (button) {
            case 0:
                module.toggle();
                break;
            case 2:
                binding = !binding;
                break;
        }
    }

    @Override
    public void onKeyPress(int keyCode) {
        if (binding) {
            ClickGuiScreen.escapeKeyInUse = true;
            module.setKey(keyCode == Keyboard.KEY_DELETE ? Keyboard.KEY_NONE : keyCode);
            binding = false;
        }
    }

    @Override
    public int getHeightWithExpand() {
        int height = getHeight();
        if (isExpanded()) {
            for (Component child : components) {
                int cHeight = child.getHeight();
                if (child instanceof BooleanSettingComponent) {
                    BooleanSettingComponent booleanSettingComponent = (BooleanSettingComponent) child;
                    if (!booleanSettingComponent.booleanSetting.isVisible()) {
                        continue;
                    }
                }
                if (child instanceof NumberSettingComponent) {
                    NumberSettingComponent numberSettingComponent = (NumberSettingComponent) child;
                    if (!numberSettingComponent.numberSetting.isVisible()) {
                        continue;
                    }
                }
                if (child instanceof ColorPickerComponent) {
                    ColorPickerComponent colorPickerComponent = (ColorPickerComponent) child;
                    if (!colorPickerComponent.getSetting().isVisible()) {
                        continue;
                    }
                }
                if (child instanceof ListSettingComponent) {
                    ListSettingComponent listSettingComponent = (ListSettingComponent) child;
                    if (!listSettingComponent.getSetting().isVisible()) {
                        continue;
                    }
                }
                if (child instanceof ExpandableComponent) {
                    ExpandableComponent expandableComponent = (ExpandableComponent) child;
                    if (expandableComponent.isExpanded())
                        cHeight = expandableComponent.getHeightWithExpand();
                }
                height += cHeight;
            }
        }
        return height;
    }

}

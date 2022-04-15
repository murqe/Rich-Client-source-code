package zxc.rich.client.features.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.util.glu.GLU;
import zxc.rich.Main;
import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.Event2D;
import zxc.rich.api.event.events.impl.EventUpdate;
import zxc.rich.api.utils.GLUtils;
import zxc.rich.api.utils.render.AnimationHelper;
import zxc.rich.api.utils.render.ClientHelper;
import zxc.rich.api.utils.render.ColorUtils;
import zxc.rich.api.utils.render.RenderUtils;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.ui.clickgui.ScreenHelper;
import zxc.rich.client.ui.settings.impl.BooleanSetting;
import zxc.rich.client.ui.settings.impl.ColorSetting;
import zxc.rich.client.ui.settings.impl.ListSetting;
import zxc.rich.client.ui.settings.impl.NumberSetting;

import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class
FeatureList extends Feature {

    public BooleanSetting backGround;
    public ColorSetting backGroundColor = new ColorSetting("BackGround Color", Color.BLACK.getRGB(), () -> backGround.getBoolValue());
    public BooleanSetting rightBorder;
    public BooleanSetting border;
    public final ListSetting borderMode;
    public NumberSetting xOffset;
    public NumberSetting yOffset;
    public NumberSetting offset;
    public NumberSetting size;
    public NumberSetting borderWidth;
    public NumberSetting rainbowSaturation;
    public NumberSetting rainbowBright;
    public NumberSetting fontX;
    public NumberSetting fontY;
    public BooleanSetting suffix;
    public static ListSetting colorList;
    public static NumberSetting colortime = new NumberSetting("Color Time", 30, 1, 100, 1, () -> colorList.currentMode.equalsIgnoreCase("Custom"));

    public static ColorSetting onecolor = new ColorSetting("One Color", new Color(0xFFFFFF).getRGB(), () -> colorList.currentMode.equals("Custom"));
    public static ColorSetting twocolor = new ColorSetting("Two Color", new Color(0xFF0000).getRGB(), () -> colorList.currentMode.equals("Custom"));

    public FeatureList() {
        super("ArrayList", "Показывает список всех включенных модулей", FeatureCategory.HUD);
        colorList = new ListSetting("ArrayList Color", "Astolfo", () -> true, "Astolfo", "Rainbow", "Custom");
        backGround = new BooleanSetting("Background", true, () -> true);
        rightBorder = new BooleanSetting("Right Border", true, () -> true);
        suffix = new BooleanSetting("Suffix", true, () -> true);
        border = new BooleanSetting("Border", true, () -> true);
        borderMode = new ListSetting("Border Mode", "Full", () -> border.getBoolValue(), "Full", "Single");
        rainbowSaturation = new NumberSetting("Rainbow Saturation", 0.8F, 0.1F, 1F, 0.1F, () -> colorList.currentMode.equals("Rainbow"));
        rainbowBright = new NumberSetting("Rainbow Brightness", 1F, 0.1F, 1F, 0.1F, () -> colorList.currentMode.equals("Rainbow"));
        fontX = new NumberSetting("FontX", 0, -4, 20, 0.1F, () -> true);
        fontY = new NumberSetting("FontY", 0, -4, 20, 0.01F, () -> true);
        xOffset = new NumberSetting("FeatureList X", 0, 0, 500, 1, () -> true);
        yOffset = new NumberSetting("FeatureList Y", 0, 0, 500, 1, () -> true);
        offset = new NumberSetting("Font Offset", 11, 7, 20, 0.5F, () -> true);
        borderWidth = new NumberSetting("Border Width", 1, 0, 10, 0.1F, () -> rightBorder.getBoolValue());
        addSettings(colorList, onecolor, twocolor, fontX, fontY, border, borderMode, rightBorder, borderWidth, suffix, backGround, backGroundColor, colortime, rainbowSaturation, rainbowBright, xOffset, yOffset, offset);
    }


    @EventTarget
    public void onUpdate(EventUpdate event) {
        setSuffix(colorList.getCurrentMode());
    }
    private static Feature getNextEnabledFeature(List<Feature> features, int index) {
        for (int i = index; i < features.size(); i++) {
            Feature feature = features.get(i);
            if (feature.isEnabled()) {
                if (!feature.getSuffix().equals("ClickGui")) {
                    return feature;
                }
            }
        }
        return null;
    }
    @EventTarget
    public void Event2D(Event2D event) {
        final float width = event.getResolution().getScaledWidth() - (rightBorder.getBoolValue() ? borderWidth.getNumberValue() : 0);
        float y = 1;
        int yTotal = 0;
        for (int i = 0; i < Main.instance.featureDirector.getAllFeatures().size(); ++i) {
            yTotal += ClientHelper.getFontRender().getFontHeight() + 3;
        }
        if (Main.instance.featureDirector.getFeature(FeatureList.class).isEnabled() && !mc.gameSettings.showDebugInfo) {
            try {
                Main.instance.featureDirector.getAllFeatures().sort(Comparator.comparingInt(module -> !ClientFont.minecraftfont.getBoolValue() ? -ClientHelper.getFontRender().getStringWidth(suffix.getBoolValue() ? module.getSuffix() : module.getLabel()) : -mc.fontRendererObj.getStringWidth(suffix.getBoolValue() ? module.getSuffix() : module.getLabel())));
                for (Feature feature : Main.instance.featureDirector.getAllFeatures()) {
                    ScreenHelper animationHelper = feature.getScreenHelper();
                    String featureSuffix = suffix.getBoolValue() ? feature.getSuffix() : feature.getLabel();

                    final float listOffset = offset.getNumberValue();
                    final float length = !ClientFont.minecraftfont.getBoolValue() ? ClientHelper.getFontRender().getStringWidth(featureSuffix) : mc.fontRendererObj.getStringWidth(featureSuffix);
                    final float featureX = width - length;
                    final boolean state = feature.isEnabled();

                    if (state) {
                        animationHelper.interpolate(featureX, y, 4F * Minecraft.frameTime / 4);
                    } else {
                        animationHelper.interpolate(width, y, 4F * Minecraft.frameTime / 4);
                    }


                    final float translateY = animationHelper.getY();
                    final float translateX = animationHelper.getX() - (rightBorder.getBoolValue() ? 2.5F : 1.5F) - fontX.getNumberValue();
                    int color = 0;
                    final int colorCustom = onecolor.getColorValue();
                    final int colorCustom2 = twocolor.getColorValue();
                    final double time = colortime.getNumberValue();
                    final String mode = colorList.getOptions();
                    final boolean visible = animationHelper.getX() < width;

                    if (visible) {
                        switch (mode.toLowerCase()) {
                            case "rainbow":
                                color = ColorUtils.rainbow((int) (y * time), rainbowSaturation.getNumberValue(), rainbowBright.getNumberValue()).getRGB();
                                break;
                            case "astolfo":
                                color = ColorUtils.astolfo(y, yTotal, 0.9f, 5).getRGB();
                                break;
                            case "custom":
                                color = ColorUtils.fadeColor(new Color(colorCustom).getRGB(), new Color(colorCustom2).getRGB(), (float) Math.abs(((((System.currentTimeMillis() / time) / time) + y * 6L / 61 * 2) % 2)));
                                break;
                        }

                        GlStateManager.pushMatrix();
                        GlStateManager.translate(-xOffset.getNumberValue() , yOffset.getNumberValue(), 1);

                        if (backGround.getBoolValue()) {
                            RenderUtils.drawRect(translateX - 2, translateY - 1, width, translateY + listOffset - 1, backGroundColor.getColorValue());
                        }
                        if (!ClientFont.minecraftfont.getBoolValue()) {
                            String modeArrayFont = ClientFont.fontMode.getOptions();
                            final float yOffset = modeArrayFont.equalsIgnoreCase("Lato") ? 1.2f : modeArrayFont.equalsIgnoreCase("Myseo") ? 0.5f : modeArrayFont.equalsIgnoreCase("URWGeometric") ? 0.5f : modeArrayFont.equalsIgnoreCase("Roboto Regular") ? 0.5f : modeArrayFont.equalsIgnoreCase("SFUI") ? 1.3f : 2f;
                            if (!ClientFont.minecraftfont.getBoolValue()) {
                                if (suffix.getBoolValue()) {
                                    ClientHelper.getFontRender().drawStringWithShadow(feature.getSuffix(), translateX, translateY + yOffset + fontY.getNumberValue(), new Color(192, 192, 192).getRGB());
                                }
                                ClientHelper.getFontRender().drawStringWithShadow(feature.getLabel(), translateX, translateY + yOffset + fontY.getNumberValue(), color);

                            }
                        } else if (ClientFont.minecraftfont.getBoolValue()) {
                            if (suffix.getBoolValue()) {
                                mc.fontRendererObj.drawStringWithShadow(feature.getSuffix(), translateX, translateY + 1 + fontY.getNumberValue(), new Color(192, 192, 192).getRGB());
                            }
                            mc.fontRendererObj.drawStringWithShadow(feature.getLabel(), translateX, translateY + 1 + fontY.getNumberValue(), color);
                        }
                        y += listOffset;

                        if (rightBorder.getBoolValue()) {
                            float checkY = 0.6F;
                            RenderUtils.drawRect(width, translateY - 1, width + borderWidth.getNumberValue(), translateY + listOffset - checkY, color);
                        }
                        if (border.getBoolValue() && borderMode.currentMode.equals("Full")) {
                            RenderUtils.drawRect(translateX - 3.5, translateY - 1, translateX - 2, translateY + listOffset - 1, color);
                        }
                        Feature nextFeature = null;
                        int index = Main.instance.featureDirector.getAllFeatures().indexOf(feature) + 1;

                        if (Main.instance.featureDirector.getAllFeatures().size() > index) {
                            nextFeature = getNextEnabledFeature(Main.instance.featureDirector.getAllFeatures(), index);
                        }
                        if (nextFeature != null && borderMode.currentMode.equals("Full")) {
                            String name = suffix.getBoolValue() ? nextFeature.getSuffix() : nextFeature.getLabel();
                            final float font = !ClientFont.minecraftfont.getBoolValue() ? ClientHelper.getFontRender().getStringWidth(name) : mc.fontRendererObj.getStringWidth(name);
                            final float dif = (length - font);
                            if (border.getBoolValue() && borderMode.currentMode.equals("Full")) {
                                RenderUtils.drawRect(translateX - 3.5, translateY + listOffset + 1, translateX - 2 + dif, translateY + listOffset - 1, color);
                            }
                        } else {
                            if (border.getBoolValue() && borderMode.currentMode.equals("Full")) {
                                RenderUtils.drawRect(translateX - 3.5, translateY + listOffset + 1, width, translateY + listOffset - 1, color);
                            }
                        }

                        if (borderMode.currentMode.equals("Single") && border.getBoolValue()) {
                            RenderUtils.drawRect(translateX - 3.5, translateY - 1, translateX - 2, translateY + listOffset - 1, color);
                        }
                        GlStateManager.popMatrix();

                    }

                }
            } catch (Exception e) {
            }
        }
    }
}
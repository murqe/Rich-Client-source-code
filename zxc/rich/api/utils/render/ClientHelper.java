package zxc.rich.api.utils.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import zxc.rich.api.utils.Helper;
import zxc.rich.client.features.hud.ClientFont;
import zxc.rich.client.features.hud.FeatureList;
import zxc.rich.client.ui.font.MCFontRenderer;

import java.awt.*;

public class ClientHelper implements Helper {
    public static ServerData serverData;

    public static Color getClientColor() {
        Color color = Color.white;
        Color onecolor = new Color(FeatureList.onecolor.getColorValue());
        Color twoColor = new Color(FeatureList.twocolor.getColorValue());
        double time = FeatureList.colortime.getNumberValue();
        String mode = FeatureList.colorList.getOptions();
        float yDist = (float) 4;
        int yTotal = 0;
        for (int i = 0; i < 30; i++) {
            yTotal += Minecraft.getMinecraft().sfui18.getFontHeight() + 5;
        }
        if (mode.equalsIgnoreCase("Rainbow")) {
            color = ColorUtils.rainbow((int) (1 * 200 * 0.1f), 0.5f, 1.0f);
        } else if (mode.equalsIgnoreCase("Astolfo")) {
            color = ColorUtils.astolfo((int) yDist, yTotal);
        } else if (mode.equalsIgnoreCase("Custom")) {
            color = ColorUtils.TwoColoreffect(new Color(onecolor.getRGB()), new Color(twoColor.getRGB()), Math.abs(System.currentTimeMillis() / time) / 100.0 + 3.0F * (yDist * 2.55) / 60);
        }
        return color;
    }

    public static Color getClientColor(float yStep, float yStepFull, int speed) {
        Color color = Color.white;
        Color onecolor = new Color(FeatureList.onecolor.getColorValue());
        Color twoColor = new Color(FeatureList.twocolor.getColorValue());
        double time = FeatureList.colortime.getNumberValue();
        String mode = FeatureList.colorList.getOptions();
        float yDist = (float) 4;
        int yTotal = 0;
        for (int i = 0; i < 30; i++) {
            yTotal += Minecraft.getMinecraft().sfui18.getFontHeight() + 5;
        }
        if (mode.equalsIgnoreCase("Rainbow")) {
            color = ColorUtils.rainbowCol(yStep, yStepFull, 0.5f, speed);
        } else if (mode.equalsIgnoreCase("Astolfo")) {
            color = ColorUtils.astolfo(yStep, yStepFull, 0.5f, speed);
        } else if (mode.equalsIgnoreCase("Custom")) {
            color = ColorUtils.TwoColoreffect(new Color(onecolor.getRGB()), new Color(twoColor.getRGB()), Math.abs(System.currentTimeMillis() / time) / 100.0 + 3.0F * (yStep * 2.55) / 60);
        }
        return color;
    }

    public static Color getClientColor(float yStep, float astolfoastep, float yStepFull, int speed) {
        Color color = Color.white;
        Color onecolor = new Color(FeatureList.onecolor.getColorValue());
        Color twoColor = new Color(FeatureList.twocolor.getColorValue());
        double time = FeatureList.colortime.getNumberValue();
        String mode = FeatureList.colorList.getOptions();int yTotal = 0;
        for (int i = 0; i < 30; i++) {
            yTotal += Minecraft.getMinecraft().sfui18.getFontHeight() + 5;
        }
        if (mode.equalsIgnoreCase("Rainbow")) {
            color = ColorUtils.rainbowCol(yStep, yStepFull, 0.5f, speed);
        } else if (mode.equalsIgnoreCase("Astolfo")) {
            color = ColorUtils.astolfo(astolfoastep, yStepFull, 0.5f, speed);
        } else if (mode.equalsIgnoreCase("Custom")) {
            color = ColorUtils.TwoColoreffect(new Color(onecolor.getRGB()), new Color(twoColor.getRGB()), Math.abs(System.currentTimeMillis() / time) / 100.0 + 3.0F * (yStep * 2.55) / 60);
        }
        return color;
    }

    public static MCFontRenderer getFontRender() {
        Minecraft mc = Minecraft.getMinecraft();
        MCFontRenderer font = mc.sfui18;
        String mode = ClientFont.fontMode.getOptions();
        switch (mode) {
            case "Myseo":
                font = mc.neverlose500_18;
                break;
            case "SFUI":
                font = mc.sfui18;
                break;
            case "Lato":
                font = mc.lato;
                break;
            case "Roboto Regular":
                font = mc.robotoRegular;
                break;
            case "Tenacity":
                font = mc.tenacity;
                break;
            case "URWGeometric":
                font = mc.urwgeometric;
                break;
        }
        return font;
    }
}
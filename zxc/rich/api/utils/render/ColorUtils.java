package zxc.rich.api.utils.render;

import net.minecraft.util.math.MathHelper;
import zxc.rich.api.utils.Helper;
import zxc.rich.api.utils.math.MathematicHelper;
import zxc.rich.client.features.hud.ClickGUI;

import java.awt.*;

public class ColorUtils implements Helper {
    public static Color astolfo(boolean clickgui, int yOffset) {
        float speed = clickgui ? ClickGUI.speed.getNumberValue() * 100 : 10 * 100;
        float hue = (System.currentTimeMillis() % (int) speed) + yOffset;
        if (hue > speed) {
            hue -= speed;
        }
        hue /= speed;
        if (hue > 0.5F) {
            hue = 0.5F - (hue - 0.5F);
        }
        hue += 0.5F;
        return Color.getHSBColor(hue, 0.4F, 1F);
    }

    public static Color interpolateColorC(Color color1, Color color2, float amount) {
        amount = Math.min(1, Math.max(0, amount));
        return new Color(MathematicHelper.interpolateInt(color1.getRed(), color2.getRed(), amount),
                MathematicHelper.interpolateInt(color1.getGreen(), color2.getGreen(), amount),
                MathematicHelper.interpolateInt(color1.getBlue(), color2.getBlue(), amount),
                MathematicHelper.interpolateInt(color1.getAlpha(), color2.getAlpha(), amount));
    }
    public static Color interpolateColorsBackAndForth(int speed, int index, Color start, Color end, boolean trueColor) {
        int angle = (int) (((System.currentTimeMillis()) / speed + index) % 360);
        angle = (angle >= 180 ? 360 - angle : angle) * 2;
        return trueColor ? ColorUtils.interpolateColorHue(start, end, angle / 360f) : ColorUtils.interpolateColorC(start, end, angle / 360f);
    }
    public static Color interpolateColorHue(Color color1, Color color2, float amount) {
        amount = Math.min(1, Math.max(0, amount));

        float[] color1HSB = Color.RGBtoHSB(color1.getRed(), color1.getGreen(), color1.getBlue(), null);
        float[] color2HSB = Color.RGBtoHSB(color2.getRed(), color2.getGreen(), color2.getBlue(), null);

        Color resultColor = Color.getHSBColor(MathematicHelper.interpolateFloat(color1HSB[0], color2HSB[0], amount),
                MathematicHelper.interpolateFloat(color1HSB[1], color2HSB[1], amount), MathematicHelper.interpolateFloat(color1HSB[2], color2HSB[2], amount));

        return new Color(resultColor.getRed(), resultColor.getGreen(), resultColor.getBlue(),
                MathematicHelper.interpolateInt(color1.getAlpha(), color2.getAlpha(), amount));
    }
    public static Color TwoColoreffect(Color cl1, Color cl2, double speed) {
        double thing = speed / 4.0 % 1.0;
        float val = MathHelper.clamp((float) Math.sin(Math.PI * 6 * thing) / 2.0f + 0.5f, 0.0f, 1.0f);
        return new Color(lerp((float) cl1.getRed() / 255.0f, (float) cl2.getRed() / 255.0f, val),
                lerp((float) cl1.getGreen() / 255.0f, (float) cl2.getGreen() / 255.0f, val),
                lerp((float) cl1.getBlue() / 255.0f, (float) cl2.getBlue() / 255.0f, val));
    }

    public static float lerp(float a, float b, float f) {
        return a + f * (b - a);
    }

    public static int fadeColor(int startColor, int endColor, float progress) {
        if (progress > 1) {
            progress = 1 - progress % 1;
        }
        return fade(startColor, endColor, progress);
    }

    public static Color rainbowCol(float yDist, float yTotal, float saturation, float speedt) {
        float speed = 1800f;
        float hue = (System.currentTimeMillis() % (int) speed) + (yTotal - yDist) * speedt;
        while (hue > speed) {
            hue -= speed;
        }
        hue /= speed;
        if (hue > 5) {
            hue = 5 - (hue - 5);
        }
        hue += 5;
        return Color.getHSBColor(hue, saturation, 1F);
    }

    public static int fade(int startColor, int endColor, float progress) {
        float invert = 1.0f - progress;
        int r = (int) ((startColor >> 16 & 0xFF) * invert + (endColor >> 16 & 0xFF) * progress);
        int g = (int) ((startColor >> 8 & 0xFF) * invert + (endColor >> 8 & 0xFF) * progress);
        int b = (int) ((startColor & 0xFF) * invert + (endColor & 0xFF) * progress);
        int a = (int) ((startColor >> 24 & 0xFF) * invert + (endColor >> 24 & 0xFF) * progress);
        return (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF);
    }

    public static Color astolfo(float yDist, float yTotal) {
        float speed = 3500f;
        float hue = (System.currentTimeMillis() % (int) speed) + (yTotal - yDist) * 12;
        while (hue > speed) {
            hue -= speed;
        }
        hue /= speed;
        if (hue > 0.5) {
            hue = 0.5F - (hue - 0.5f);
        }
        hue += 0.5F;
        return new Color(hue, 0.4f, 1);
    }

    public static Color astolfo(float yDist, float yTotal, float saturation, float speedt) {
        float speed = 1800f;
        float hue = (System.currentTimeMillis() % (int) speed) + (yTotal - yDist) * speedt;
        while (hue > speed) {
            hue -= speed;
        }
        hue /= speed;
        if (hue > 0.5) {
            hue = 0.5F - (hue - 0.5f);
        }
        hue += 0.5F;
        return Color.getHSBColor(hue, saturation, 1F);
    }

    public static int getColor(int red, int green, int blue) {
        return ColorUtils.getColor(red, green, blue, 255);
    }

    public static int getColor(int red, int green, int blue, int alpha) {
        int color = 0;
        color |= alpha << 24;
        color |= red << 16;
        color |= green << 8;
        return color |= blue;
    }

    public static int getColor(Color color) {
        return getColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public static int getColor(int bright) {
        return getColor(bright, bright, bright, 255);
    }

    public static int getColor(int brightness, int alpha) {
        return ColorUtils.getColor(brightness, brightness, brightness, alpha);
    }


    public static Color rainbow(int delay, float saturation, float brightness) {
        double rainbow = Math.ceil((System.currentTimeMillis() + delay) / 16);
        rainbow %= 360;
        return Color.getHSBColor((float) (rainbow / 360), saturation, brightness);
    }
}

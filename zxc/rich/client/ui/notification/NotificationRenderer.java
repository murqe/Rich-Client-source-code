package zxc.rich.client.ui.notification;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import zxc.rich.Main;
import zxc.rich.api.utils.Helper;
import zxc.rich.api.utils.render.ClientHelper;
import zxc.rich.api.utils.render.RenderUtils;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.hud.ClickGUI;
import zxc.rich.client.features.hud.Notifications;
import zxc.rich.client.ui.clickgui.ClickGuiScreen;

import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class NotificationRenderer implements Helper {
    private static final List<Notification> NOTIFICATIONS = new CopyOnWriteArrayList<>();

    public static void queue(String title, String content, int second, NotificationMode type) {
        NOTIFICATIONS.add(new Notification(title, content, type, second * 1000, Minecraft.getMinecraft().neverlose500_18));
    }

    public static void publish(ScaledResolution sr) {
        if (Main.instance.featureDirector.getFeature(Notifications.class).isEnabled() && Notifications.notifMode.currentMode.equalsIgnoreCase("Rect") && !mc.gameSettings.showDebugInfo &&  mc.world != null && !(mc.currentScreen instanceof GuiContainer) && !(mc.currentScreen instanceof ClickGuiScreen)) {
            if (!NOTIFICATIONS.isEmpty()) {
                int y = sr.getScaledHeight() - 40;
                double better;
                for (Notification notification : NOTIFICATIONS) {
                    better = Minecraft.getMinecraft().neverlose500_18.getStringWidth(notification.getTitle() + " " + notification.getContent());

                    if (!notification.getTimer().hasReached(notification.getTime() / 2)) {
                        notification.notificationTimeBarWidth = 360;
                    } else {
                        notification.notificationTimeBarWidth = MathHelper.lerp(notification.notificationTimeBarWidth, 0, 4 * Feature.deltaTime());
                    }

                    if (!notification.getTimer().hasReached(notification.getTime())) {
                        notification.x = MathHelper.lerp(notification.x, notification.sr.getScaledWidth() - better, 12 * Feature.deltaTime());
                        notification.y = MathHelper.lerp(notification.y, y, 12 * Feature.deltaTime());
                    } else {
                        notification.x = MathHelper.lerp(notification.x, notification.sr.getScaledWidth() + 25, 12 * Feature.deltaTime());
                        notification.y = MathHelper.lerp(notification.y, y, 12 * Feature.deltaTime());
                        if (notification.x > notification.sr.getScaledWidth() + 24 && mc.player != null && mc.world != null && !mc.gameSettings.showDebugInfo) {
                            NOTIFICATIONS.remove(notification);
                        }
                    }
                    GlStateManager.pushMatrix();
                    GlStateManager.disableBlend();
                    RenderUtils.drawSmoothRect(notification.x - 24, notification.y, notification.sr.getScaledWidth(), notification.y + 12.0f, new Color(20, 20, 20, 0).getRGB());
                    RenderUtils.drawGlowRoundedRect((float)notification.x - 24, (float)notification.y - 2, notification.sr.getScaledWidth(), (float)notification.y + 14.0f, new Color(20, 20, 20, 255).getRGB(), 8,10);

                    RenderUtils.drawCircle((float) (notification.x) - 16, (float) (notification.y + 6.5F), 0, (float) notification.notificationTimeBarWidth, 3, ClientHelper.getClientColor().getRGB(), 2);
                    Minecraft.getMinecraft().neverlose500_18.drawString(notification.getTitle() + " " + notification.getContent(), (float) (notification.x - 6), (float) (notification.y + 4), -1);
                    GlStateManager.popMatrix();
                    y -= 18;
                }
            }
        }
    }
}
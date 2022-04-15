package zxc.rich.client.ui.notification;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import zxc.rich.api.utils.world.TimerHelper;
import zxc.rich.client.ui.font.MCFontRenderer;

public class Notification {
    public final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
    public static final int HEIGHT = 30;
    private final String title;
    private final String content;
    private final int time;
    private final NotificationMode type;
    private final TimerHelper timer;
    private final MCFontRenderer fontRenderer;
    public double x = sr.getScaledWidth();
    public double y = sr.getScaledHeight();
    public double notificationTimeBarWidth;

    public Notification(String title, String content, NotificationMode type, int second, MCFontRenderer fontRenderer) {
        this.title = title;
        this.content = content;
        this.time = second;
        this.type = type;
        this.timer = new TimerHelper();
        this.fontRenderer = fontRenderer;

    }

    public final int getWidth() {
        return Math.max(100, Math.max(this.fontRenderer.getStringWidth(this.title), this.fontRenderer.getStringWidth(this.content)) + 90);
    }

    public final String getTitle() {
        return this.title;
    }

    public final String getContent() {
        return this.content;
    }

    public final int getTime() {
        return this.time;
    }

    public final NotificationMode getType() {
        return this.type;
    }

    public final TimerHelper getTimer() {
        return this.timer;
    }

}
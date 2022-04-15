package zxc.rich.client.ui.draggable.impl;

import com.mysql.cj.xdevapi.Client;
import net.minecraft.client.Minecraft;
import zxc.rich.Main;
import zxc.rich.api.utils.GLUtils;
import zxc.rich.api.utils.render.ClientHelper;
import zxc.rich.api.utils.render.RenderUtils;
import zxc.rich.client.features.combat.KillAura;
import zxc.rich.client.features.hud.InfoDisplay;
import zxc.rich.client.features.hud.InfoInterface;
import zxc.rich.client.ui.draggable.DraggableModule;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SessionInfoComponent extends DraggableModule {

    public SessionInfoComponent() {
        super("SessionInfoComponent", 100, 200);
    }

    @Override
    public int getWidth() {
        return 155;
    }

    @Override
    public int getHeight() {
        return 62;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        GLUtils.INSTANCE.rescale(scale);
        String server = mc.isSingleplayer() ? "local" : mc.getCurrentServerData() != null ? mc.getCurrentServerData().serverIP.toLowerCase() : "null";
        String name = "Name: " + mc.player.getName();

        String time;
        if (Minecraft.getMinecraft().isSingleplayer()) {
            time = "SinglePlayer";
        } else {
            long durationInMillis = System.currentTimeMillis() - Main.playTimeStart;
            long second = (durationInMillis / 1000) % 60;
            long minute = (durationInMillis / (1000 * 60)) % 60;
            long hour = (durationInMillis / (1000 * 60 * 60)) % 24;
            time = String.format("%02dh %02dm %02ds", hour, minute, second);
        }
        RenderUtils.drawRect(getX(), getY(), getX() + 155, getY() + 62, new Color(20, 20, 20, 0).getRGB());
        RenderUtils.drawSmoothRect(getX() + 2.4f, getY() + 3.5f, getX() + 153, getY() + 4.5f, new Color(ClientHelper.getClientColor().getRGB()).getRGB());
        RenderUtils.drawRect(getX() + 2.8f, getY() + 19, getX() + 152, getY() + 19.5f, new Color(65, 65, 65).getRGB());

        mc.mono18.drawString("Session Information", getX() + 5, getY() + 9.3f, -1);
        mc.mono17.drawString("Server: " + server, getX() + 5, getY() + 25, -1);
        mc.mono17.drawString(name, getX() + 5, getY() + 34.5f, -1);
        mc.mono17.drawString("Play Time: " + time, getX() + 5, getY() + 44, -1);
        GLUtils.INSTANCE.rescaleMC();
        super.render(mouseX, mouseY);
    }

    @Override
    public void draw() {
        if (Main.instance.featureDirector.getFeature(InfoDisplay.class).isEnabled() && InfoDisplay.sessionInfo.getBoolValue() && !mc.gameSettings.showDebugInfo) {

            GLUtils.INSTANCE.rescale(scale);
            String server = mc.isSingleplayer() ? "local" : mc.getCurrentServerData() != null ? mc.getCurrentServerData().serverIP.toLowerCase() : "null";
            String name = "Name: " + mc.player.getName();

            String time;
            if (Minecraft.getMinecraft().isSingleplayer()) {
                time = "SinglePlayer";
            } else {
                long durationInMillis = System.currentTimeMillis() - Main.playTimeStart;
                long second = (durationInMillis / 1000) % 60;
                long minute = (durationInMillis / (1000 * 60)) % 60;
                long hour = (durationInMillis / (1000 * 60 * 60)) % 24;
                time = String.format("%02dh %02dm %02ds", hour, minute, second);
            }
            RenderUtils.drawRect(getX(), getY(), getX() + 155, getY() + 62, new Color(20, 20, 20, 0).getRGB());
            RenderUtils.drawGlowRoundedRect(getX(), getY() + 2, getX() + 155, getY() + 55, new Color(20, 20, 20, 255).getRGB(), 6, 5);
            RenderUtils.drawSmoothRect(getX() + 2.4f, getY() + 3.5f, getX() + 153, getY() + 4.5f, new Color(ClientHelper.getClientColor().getRGB()).getRGB());
            RenderUtils.drawRect(getX() + 2.8f, getY() + 19, getX() + 152, getY() + 19.5f, new Color(65, 65, 65).getRGB());

            mc.mono18.drawString("Session Information", getX() + 5, getY() + 9.3f, -1);
            mc.mono17.drawString("Server: " + server, getX() + 5, getY() + 25, -1);
            mc.mono17.drawString(name, getX() + 5, getY() + 34.5f, -1);
            mc.mono17.drawString("Play Time: " + time, getX() + 5, getY() + 44, -1);
            GLUtils.INSTANCE.rescaleMC();

        }
        super.draw();
    }
}

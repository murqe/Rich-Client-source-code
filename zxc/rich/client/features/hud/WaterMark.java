package zxc.rich.client.features.hud;

import net.minecraft.client.Minecraft;
import prot.rich.UserData;
import zxc.rich.Main;
import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.Event2D;
import zxc.rich.api.utils.GLUtils;
import zxc.rich.api.utils.render.ClientHelper;
import zxc.rich.api.utils.render.RenderUtils;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.ui.settings.impl.ListSetting;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;


public class WaterMark extends Feature {
    public static ListSetting waterMarkMode;
    public float scale = 2;

    public WaterMark() {
        super("Watermark", "Логотип клиента", FeatureCategory.HUD);
        waterMarkMode = new ListSetting("WaterMark Mode", "Neverlose", () -> true, "Onetap", "Dev", "OnetapV2", "Neverlose");
        this.addSettings(waterMarkMode);
    }

    @EventTarget
    public void onRender(Event2D render) {
        String watermark = waterMarkMode.getOptions();
        String name = UserData.instance().getName();
        this.setSuffix(watermark);
        if (Main.instance.featureDirector.getFeature(WaterMark.class).isEnabled() && !mc.gameSettings.showDebugInfo) {
            if (watermark.equalsIgnoreCase("OnetapV2")) {
                String server = mc.isSingleplayer() ? "local" : mc.getCurrentServerData() != null ? mc.getCurrentServerData().serverIP.toLowerCase() : "null";

                String time = (new SimpleDateFormat("HH:mm:ss")).format(Calendar.getInstance().getTime());
                String text = "Rich Premium | " + name + " | " + server + " | " + mc.getDebugFPS() + " fps" + " | " + Objects.requireNonNull(mc.getConnection()).getPlayerInfo(mc.player.getUniqueID()).getResponseTime() + "ms" + " | " + time;
                RenderUtils.drawSmoothRect(5, 6, Minecraft.getMinecraft().neverlose500_15.getStringWidth(text) + 9, 18, new Color(31, 31, 31, 255).getRGB());
                for (float l = 0; l < Minecraft.getMinecraft().neverlose500_15.getStringWidth(text) + 4; l += 1f) {
                    RenderUtils.drawSmoothRect(5 + l, 5, l + 6, 6, ClientHelper.getClientColor(5, l, 5).getRGB());
                }
                RenderUtils.drawSmoothRect(5, 6f, Minecraft.getMinecraft().neverlose500_15.getStringWidth(text) + 9, 6.5f, new Color(20, 20, 20, 100).getRGB());

                Minecraft.getMinecraft().neverlose500_15.drawStringWithShadow(text, 7, 10.5, -1);
            } else if (watermark.equalsIgnoreCase("Dev")) {
                ClientHelper.getFontRender().drawStringWithFade(Main.instance.name, 3, 4);
            } else if (watermark.equalsIgnoreCase("Onetap")) {
                String server = mc.isSingleplayer() ? "local" : mc.getCurrentServerData() != null ? mc.getCurrentServerData().serverIP.toLowerCase() : "null";
                String time = (new SimpleDateFormat("HH:mm:ss")).format(Calendar.getInstance().getTime());
                String ping = mc.getConnection().getPlayerInfo(mc.player.getUniqueID()).getResponseTime() + "";
                RenderUtils.drawGradientRect(5, 185, 95, 198, new Color(22, 22, 22, 205).getRGB(), new Color(21, 21, 21, 0).getRGB());
                for (int l = 0; l <= 90; l = l + 1) {
                    RenderUtils.drawSmoothRect(l + 6, 184, l + 5, 185, ClientHelper.getClientColor(5, l, 5).getRGB());
                }
                Minecraft.getMinecraft().neverlose500_13.drawStringWithShadow("richclient.pub", 28, 190, -1);
                Minecraft.getMinecraft().neverlose500_13.drawStringWithShadow("user", 9, 200, -1);
                Minecraft.getMinecraft().neverlose500_13.drawStringWithShadow("[" + mc.player.getName() + "]", 93 - Minecraft.getMinecraft().neverlose500_13.getStringWidth("[" + mc.player.getName() + "]"), 200, -1);
                Minecraft.getMinecraft().neverlose500_13.drawStringWithShadow("server", 9, 208, -1);
                Minecraft.getMinecraft().neverlose500_13.drawStringWithShadow("[" + server + "]", 93 - Minecraft.getMinecraft().neverlose500_13.getStringWidth("[" + server + "]"), 208, -1);
                Minecraft.getMinecraft().neverlose500_13.drawStringWithShadow("time", 9, 216, -1);
                Minecraft.getMinecraft().neverlose500_13.drawStringWithShadow("[" + time + "]", 93 - Minecraft.getMinecraft().neverlose500_13.getStringWidth("[" + time + "]"), 216, -1);
                Minecraft.getMinecraft().neverlose500_13.drawStringWithShadow("latency", 9, 224, -1);
                Minecraft.getMinecraft().neverlose500_13.drawStringWithShadow("[" + ping + "]", 93 - Minecraft.getMinecraft().neverlose500_13.getStringWidth("[" + ping + "]"), 224, -1);
            } else if (watermark.equalsIgnoreCase("Neverlose")) {
                GLUtils.INSTANCE.rescale(scale);
                String server = mc.isSingleplayer() ? "local" : mc.getCurrentServerData() != null ? mc.getCurrentServerData().serverIP.toLowerCase() : "null";
                String text = UserData.instance().getName() + " | " + Minecraft.getDebugFPS() + " fps" + " | " + server;
                String scam = "RICH PREMIUM | " + UserData.instance().getName() + " | " + Minecraft.getDebugFPS() + " fps" + " | " + server;
                RenderUtils.drawGlowRoundedRect(2, 4  , Minecraft.getMinecraft().neverlose500_15.getStringWidth(scam) + 15  + scale - 1.9f, 20 + scale - 1.9f, new Color(10, 10, 10, 200).getRGB(), 5, 5);
                RenderUtils.drawSmoothRect(5, 6, Minecraft.getMinecraft().neverlose500_15.getStringWidth(scam) + 12, 18, new Color(10, 10, 10, 255).getRGB());
                Minecraft.getMinecraft().neverlose900_15.drawStringWithShadow("RICH PREMIUM", 7.5, 10, new Color(22, 97, 141).getRGB());
                Minecraft.getMinecraft().neverlose900_15.drawStringWithShadow("RICH PREMIUM", 8, 10.5, -1);
                Minecraft.getMinecraft().neverlose500_15.drawStringWithShadow("| " + text, 7 + Minecraft.getMinecraft().neverlose500_15.getStringWidth("RICH PREMIUM | "), 10.5, -1);
                GLUtils.INSTANCE.rescaleMC();
            }
        }
        }
    }

package zxc.rich.client.ui.draggable.impl;

import net.minecraft.client.Minecraft;

import zxc.rich.Main;
import zxc.rich.api.utils.GLUtils;
import zxc.rich.api.utils.render.ClientHelper;
import zxc.rich.client.features.hud.InfoDisplay;
import zxc.rich.client.ui.draggable.DraggableModule;

public class InfoComponent extends DraggableModule {

    public InfoComponent() {
        super("InfoComponent", 100, 400);
    }

    @Override
    public int getWidth() {
        return 75;
    }

    @Override
    public int getHeight() {
        return 31;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        String fps = "" + Minecraft.getDebugFPS();
        String xCoord = "" + Math.round(mc.player.posX);
        String yCoord = "" + Math.round(mc.player.posY);
        String zCoord = "" + Math.round(mc.player.posZ);
        mc.robotoRegular.drawStringWithShadow("X: ", getX(), getY(), ClientHelper.getClientColor().getRGB());
        mc.robotoRegular.drawStringWithShadow(xCoord, getX() + 10, getY(), -1);
        mc.robotoRegular.drawStringWithShadow("Y: ", getX() + 30 + mc.robotoRegular.getStringWidth(xCoord) - 17, getY(), ClientHelper.getClientColor().getRGB());
        mc.robotoRegular.drawStringWithShadow(yCoord, getX() + 40 + mc.robotoRegular.getStringWidth(xCoord) - 17, getY(), -1);
        mc.robotoRegular.drawStringWithShadow("Z: ", getX() + 66 + mc.robotoRegular.getStringWidth(xCoord) - 23 + mc.robotoRegular.getStringWidth(yCoord) - 17, getY(), ClientHelper.getClientColor().getRGB());
        mc.robotoRegular.drawStringWithShadow(zCoord, getX() + 76 + mc.robotoRegular.getStringWidth(xCoord) - 23 + mc.robotoRegular.getStringWidth(yCoord) - 17, getY(), -1);
        mc.robotoRegular.drawStringWithShadow("FPS: ", getX(), getY() - 11, ClientHelper.getClientColor().getRGB());
        mc.robotoRegular.drawStringWithShadow(fps, getX() + 22, getY() - 11, -1);
        super.render(mouseX, mouseY);
    }

    @Override
    public void draw() {
        if (Main.instance.featureDirector.getFeature(InfoDisplay.class).isEnabled() && InfoDisplay.clientInfo.getBoolValue() && !mc.gameSettings.showDebugInfo) {
            GLUtils.INSTANCE.rescale(scale);
            String fps = "" + Minecraft.getDebugFPS();
            String xCoord = "" + Math.round(mc.player.posX);
            String yCoord = "" + Math.round(mc.player.posY);
            String zCoord = "" + Math.round(mc.player.posZ);
            if (InfoDisplay.coordsInfo.getBoolValue()) {
                mc.robotoRegular.drawStringWithShadow("X: ", getX(), getY(), ClientHelper.getClientColor().getRGB());
                mc.robotoRegular.drawStringWithShadow(xCoord, getX() + 10, getY(), -1);
                mc.robotoRegular.drawStringWithShadow("Y: ", getX() + 30 + mc.robotoRegular.getStringWidth(xCoord) - 17, getY(), ClientHelper.getClientColor().getRGB());
                mc.robotoRegular.drawStringWithShadow(yCoord, getX() + 40 + mc.robotoRegular.getStringWidth(xCoord) - 17, getY(), -1);
                mc.robotoRegular.drawStringWithShadow("Z: ", getX() + 66 + mc.robotoRegular.getStringWidth(xCoord) - 23 + mc.robotoRegular.getStringWidth(yCoord) - 17, getY(), ClientHelper.getClientColor().getRGB());
                mc.robotoRegular.drawStringWithShadow(zCoord, getX() + 76 + mc.robotoRegular.getStringWidth(xCoord) - 23 + mc.robotoRegular.getStringWidth(yCoord) - 17, getY(), -1);
            }
            if (InfoDisplay.fps.getBoolValue()) {
                mc.robotoRegular.drawStringWithShadow("FPS: ", getX(), getY() - 11, ClientHelper.getClientColor().getRGB());
                mc.robotoRegular.drawStringWithShadow(fps, getX() + 22, getY() - 11, -1);
            }
            GLUtils.INSTANCE.rescaleMC();


        }
        super.draw();
    }
}
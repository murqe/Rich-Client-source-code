package zxc.rich.client.features.visuals;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import zxc.rich.Main;
import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.Event3D;
import zxc.rich.api.utils.render.RenderUtils;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.ui.settings.impl.BooleanSetting;
import zxc.rich.client.ui.settings.impl.ColorSetting;
import zxc.rich.client.ui.settings.impl.NumberSetting;

import java.awt.*;

public class Tracers extends Feature {

    private final ColorSetting colorGlobal;
    private final ColorSetting friendColor;

    private final BooleanSetting friend;
    private final BooleanSetting onlyPlayer = new BooleanSetting("Only Player", true, () -> true);
    private final NumberSetting width;
    private final BooleanSetting seeOnly = new BooleanSetting("See Only", false, () -> true);

    public Tracers() {
        super("Tracers", "Показывает линию к игрокам", FeatureCategory.VISUALS);
        friend = new BooleanSetting("Friend Highlight", true, () -> true);
        friendColor = new ColorSetting("Friend Color", new Color(0xFFFFFF).getRGB(), friend::getBoolValue);

        colorGlobal = new ColorSetting("Tracers Color", new Color(0xFFFFFF).getRGB(), () -> true);
        width = new NumberSetting("Tracers Width", 1.5F, 0.1F, 5F, 0.1F, () -> true);
        addSettings(colorGlobal, friend, friendColor, seeOnly, onlyPlayer, width);
    }

    public static boolean canSeeEntityAtFov(Entity entityLiving, float scope) {
        double diffX = entityLiving.posX - mc.player.posX;
        double diffZ = entityLiving.posZ - mc.player.posZ;
        float yaw = (float) (Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0);
        double difference = angleDifference(yaw, mc.player.rotationYaw);
        return difference <= scope;
    }

    public static double angleDifference(float oldYaw, float newYaw) {
        float yaw = Math.abs(oldYaw - newYaw) % 360;
        if (yaw > 180) {
            yaw = 360 - yaw;
        }
        return yaw;
    }

    @EventTarget
    public void onEvent3D(Event3D event) {
        for (Entity entity : mc.world.loadedEntityList) {
            if (entity != mc.player) {
                if (onlyPlayer.getBoolValue() && !(entity instanceof EntityPlayer))
                    continue;

                if (seeOnly.getBoolValue() && !canSeeEntityAtFov(entity, 150))
                    return;

                boolean old = mc.gameSettings.viewBobbing;
                mc.gameSettings.viewBobbing = false;
                mc.entityRenderer.setupCameraTransform(event.getPartialTicks(), 0);
                mc.gameSettings.viewBobbing = old;

                double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.getPartialTicks() - mc.getRenderManager().renderPosX;
                double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.getPartialTicks() - mc.getRenderManager().renderPosY - 1;
                double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.getPartialTicks() - mc.getRenderManager().renderPosZ;
                GlStateManager.pushMatrix();
                GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_LINE_SMOOTH);
                GlStateManager.glLineWidth(width.getNumberValue());
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GlStateManager.depthMask(false);
                if (Main.instance.friendManager.isFriend(entity.getName()) && friend.getBoolValue()) {
                    RenderUtils.glColor(new Color(friendColor.getColorValue()));
                } else {
                    RenderUtils.glColor(new Color(colorGlobal.getColorValue()));
                }
                GlStateManager.glBegin(GL11.GL_LINE_STRIP);
                Vec3d vec = new Vec3d(0, 0, 1).rotatePitch((float) -(Math.toRadians(mc.player.rotationPitch))).rotateYaw((float) -Math.toRadians(mc.player.rotationYaw));
                GL11.glVertex3d(vec.xCoord, mc.player.getEyeHeight() + vec.yCoord, vec.zCoord);
                GL11.glVertex3d(x, y + 1.10, z);
                GlStateManager.glEnd();
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_LINE_SMOOTH);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GlStateManager.depthMask(true);
                GL11.glDisable(GL11.GL_BLEND);
                GlStateManager.resetColor();
                GlStateManager.popMatrix();

            }
        }
    }

}

package zxc.rich.client.features.visuals;


import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import zxc.rich.Main;
import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.Event2D;
import zxc.rich.api.event.events.impl.Event3D;
import zxc.rich.api.utils.combat.KillAuraHelper;
import zxc.rich.api.utils.math.MathematicHelper;
import zxc.rich.api.utils.render.ClientHelper;
import zxc.rich.api.utils.render.ColorUtils;
import zxc.rich.api.utils.render.RenderUtils;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.ui.settings.impl.BooleanSetting;
import zxc.rich.client.ui.settings.impl.ColorSetting;
import zxc.rich.client.ui.settings.impl.ListSetting;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class PearlESP extends Feature {
    public ColorSetting globalColor = new ColorSetting("Global Color", Color.PINK.getRGB(), () -> true);
    public BooleanSetting tracers = new BooleanSetting("Tracers", true, () -> true);
    public BooleanSetting esp = new BooleanSetting("ESP", true, () -> true);
    public BooleanSetting triangleESP = new BooleanSetting("TriangleESP", true, () -> true);
    private final ListSetting triangleMode = new ListSetting("Triangle Mode", "Custom", () -> triangleESP.getBoolValue(), "Astolfo", "Rainbow", "Client", "Custom");
    private final ColorSetting triangleColor;


    public PearlESP() {
        super("PearlESP", "Показывает есп перла", FeatureCategory.VISUALS);
        triangleColor = new ColorSetting("Triangle Color", Color.PINK.getRGB(), () -> triangleESP.getBoolValue() && triangleMode.currentMode.equals("Custom"));

        addSettings(globalColor, triangleESP, triangleMode, triangleColor, esp, tracers);
    }

    @EventTarget
    public void onRender3D(Event3D event) {
        GlStateManager.pushMatrix();
        List<EntityEnderPearl> check = mc.world.loadedEntityList.stream().filter(x -> x instanceof EntityEnderPearl)
                .map(x -> (EntityEnderPearl) x).collect(Collectors.toList());
        check.forEach(entity -> {
            boolean viewBobbing = mc.gameSettings.viewBobbing;
            mc.gameSettings.viewBobbing = false;
            mc.entityRenderer.setupCameraTransform(event.getPartialTicks(), 0);
            mc.gameSettings.viewBobbing = viewBobbing;

            if (tracers.getBoolValue()) {
                GL11.glPushMatrix();
                GL11.glEnable(GL11.GL_LINE_SMOOTH);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDepthMask(false);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glLineWidth(1);
                double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.getPartialTicks() - mc.getRenderManager().renderPosX;
                double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.getPartialTicks() - mc.getRenderManager().renderPosY - 1;
                double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.getPartialTicks() - mc.getRenderManager().renderPosZ;
                RenderUtils.setColor(globalColor.getColorValue());
                Vec3d vec = new Vec3d(0, 0, 1).rotatePitch((float) -(Math.toRadians(mc.player.rotationPitch))).rotateYaw((float) -Math.toRadians(mc.player.rotationYaw));
                GL11.glBegin(2);
                GL11.glVertex3d(vec.xCoord, mc.player.getEyeHeight() + vec.yCoord, vec.zCoord);
                GL11.glVertex3d(x, y + 1.10, z);
                GL11.glEnd();
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glDepthMask(true);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glDisable(GL11.GL_LINE_SMOOTH);
                GL11.glPopMatrix();
            }

            if (esp.getBoolValue()) {
                RenderUtils.drawEntityBox(entity, new Color(globalColor.getColorValue()), true, 0.20F);
            }
        });
        GlStateManager.resetColor();
        GlStateManager.popMatrix();
    }

    @EventTarget
    public void onRenderTriangle(Event2D eventRender2D) {
        if (!isEnabled())
            return;

        if (triangleESP.getBoolValue()) {

            ScaledResolution sr = new ScaledResolution(mc);
            float size = 50;
            float xOffset = sr.getScaledWidth() / 2F - 24.5F;
            float yOffset = sr.getScaledHeight() / 2F - 25.2F;
            List<EntityEnderPearl> check = mc.world.loadedEntityList.stream().filter(x -> x instanceof EntityEnderPearl)
                    .map(x -> (EntityEnderPearl) x).collect(Collectors.toList());
            check.forEach(entity -> {
                int color = 0;
                switch (triangleMode.currentMode) {
                    case "Client":
                        color = ClientHelper.getClientColor().getRGB();
                        break;
                    case "Custom":
                        color = triangleColor.getColorValue();
                        break;
                    case "Astolfo":
                        color = ColorUtils.astolfo(false, 1).getRGB();
                        break;
                    case "Rainbow":
                        color = ColorUtils.rainbow(300, 1, 1).getRGB();
                        break;
                }
                GlStateManager.pushMatrix();
                GlStateManager.disableBlend();
                double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosX;
                double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosZ;
                double cos = Math.cos(mc.player.rotationYaw * (Math.PI * 2 / 360));
                double sin = Math.sin(mc.player.rotationYaw * (Math.PI * 2 / 360));
                double rotY = -(z * cos - x * sin);
                double rotX = -(x * cos + z * sin);
                if (MathHelper.sqrt(rotX * rotX + rotY * rotY) < size) {
                    float angle = (float) (Math.atan2(rotY, rotX) * 180 / Math.PI);
                    double xPos = ((size / 2) * Math.cos(Math.toRadians(angle))) + xOffset + size / 2;
                    double y = ((size / 2) * Math.sin(Math.toRadians(angle))) + yOffset + size / 2;
                    GlStateManager.translate(xPos, y, 0);
                    GlStateManager.rotate(angle, 0, 0, 1);
                    GlStateManager.scale(1.5, 1, 1);
                    RenderUtils.drawFillTriangle(10, 0, 4 + 0.5F, 90, new Color(color).getRGB());
                }
                GlStateManager.resetColor();
                GlStateManager.popMatrix();
            });
        }
    }
}

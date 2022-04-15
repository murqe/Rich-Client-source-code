package zxc.rich.client.features.visuals;

import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.Event2D;
import zxc.rich.api.utils.render.ClientHelper;
import zxc.rich.api.utils.render.ColorUtils;
import zxc.rich.api.utils.render.RenderUtils;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.ui.settings.impl.ColorSetting;
import zxc.rich.client.ui.settings.impl.ListSetting;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;
import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class ItemESP extends Feature {

    private final int black = Color.BLACK.getRGB();
    private final ColorSetting colorEsp;
    public ListSetting colorMode = new ListSetting("Color Box Mode", "Custom", () -> true, "Astolfo", "Rainbow", "Client", "Custom");

    public ItemESP() {
        super("ItemESP", "Отображение айтемов", FeatureCategory.VISUALS);
        colorEsp = new ColorSetting("ESP Color", new Color(0xFFFFFF).getRGB(), () -> colorMode.currentMode.equals("Custom"));
        addSettings(colorMode, colorEsp);
    }
    @EventTarget
    public void onRender2D(Event2D event) {
        String mode = colorMode.getOptions();
        setSuffix(mode);
        float partialTicks = mc.timer.renderPartialTicks;
        int scaleFactor = event.getResolution().getScaleFactor();
        double scaling = scaleFactor / Math.pow(scaleFactor, 2);
        GlStateManager.scale(scaling, scaling, scaling);
        Color customColor = new Color(colorEsp.getColorValue());
        Color c = new Color(customColor.getRed(), customColor.getGreen(), customColor.getBlue());
        int color = 0;
        switch (colorMode.currentMode) {
            case "Client":
                color = ClientHelper.getClientColor().getRGB();
                break;
            case "Custom":
                color = c.getRGB();
                break;
            case "Astolfo":
                color = ColorUtils.astolfo(false, 1).getRGB();
                break;
            case "Rainbow":
                color = ColorUtils.rainbow(300, 1, 1).getRGB();
                break;
        }

        float scale = 1;

        for (Entity entity : mc.world.loadedEntityList) {
            if (isValid(entity) && RenderUtils.isInViewFrustum(entity)) {
                EntityItem entityItem = (EntityItem) entity;
                double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.getRenderPartialTicks();
                double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.getRenderPartialTicks();
                double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.getRenderPartialTicks();
                AxisAlignedBB axisAlignedBB2 = entity.getEntityBoundingBox();
                AxisAlignedBB axisAlignedBB = new AxisAlignedBB(axisAlignedBB2.minX - entity.posX + x - 0.05, axisAlignedBB2.minY - entity.posY + y, axisAlignedBB2.minZ - entity.posZ + z - 0.05, axisAlignedBB2.maxX - entity.posX + x + 0.05, axisAlignedBB2.maxY - entity.posY + y + 0.15, axisAlignedBB2.maxZ - entity.posZ + z + 0.05);
                Vector3d[] vectors = new Vector3d[]{new Vector3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ)};
                mc.entityRenderer.setupCameraTransform(partialTicks, 0);

                Vector4d position = null;
                for (Vector3d vector : vectors) {
                    vector = project2D(scaleFactor, vector.x - mc.getRenderManager().renderPosX, vector.y - mc.getRenderManager().renderPosY, vector.z - mc.getRenderManager().renderPosZ);
                    if (vector != null && vector.z > 0 && vector.z < 1) {
                        if (position == null)
                            position = new Vector4d(vector.x, vector.y, vector.z, 0);
                        position.x = Math.min(vector.x, position.x);
                        position.y = Math.min(vector.y, position.y);
                        position.z = Math.max(vector.x, position.z);
                        position.w = Math.max(vector.y, position.w);
                    }
                }

                if (position != null) {
                    mc.entityRenderer.setupOverlayRendering();
                    double posX = position.x;
                    double posY = position.y;
                    double endPosX = position.z;
                    double endPosY = position.w;

                    RenderUtils.drawRect(posX - 1F, posY, posX + 0.5, endPosY + 0.5, black);
                    RenderUtils.drawRect(posX - 1F, posY - 0.5, endPosX + 0.5, posY + 0.5 + 0.5, black);
                    RenderUtils.drawRect(endPosX - 0.5 - 0.5, posY, endPosX + 0.5, endPosY + 0.5, black);
                    RenderUtils.drawRect(posX - 1, endPosY - 0.5 - 0.5, endPosX + 0.5, endPosY + 0.5, black);
                    RenderUtils.drawRect(posX - 0.5, posY, posX + 0.5 - 0.5, endPosY, color);
                    RenderUtils.drawRect(posX, endPosY - 0.5, endPosX, endPosY, color);
                    RenderUtils.drawRect(posX - 0.5, posY, endPosX, posY + 0.5, color);
                    RenderUtils.drawRect(endPosX - 0.5, posY, endPosX, endPosY, color);

                    float diff = (float) (endPosX - posX) / 2;
                    float textWidth = (mc.fontRendererObj.getStringWidth(entityItem.getEntityItem().getDisplayName()) * scale);
                    float tagX = (float) ((posX + diff - textWidth / 2) * scale);
                    mc.sfui18.drawStringWithShadow(entityItem.getEntityItem().getDisplayName(), tagX, (float) posY - 10, -1);
                }
            }
        }
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        mc.entityRenderer.setupOverlayRendering();
    }

    private Vector3d project2D(int scaleFactor, double x, double y, double z) {
        float xPos = (float) x;
        float yPos = (float) y;
        float zPos = (float) z;
        IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
        FloatBuffer modelview = GLAllocation.createDirectFloatBuffer(16);
        FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);
        FloatBuffer vector = GLAllocation.createDirectFloatBuffer(4);
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelview);
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projection);
        GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);
        if (GLU.gluProject(xPos, yPos, zPos, modelview, projection, viewport, vector))
            return new Vector3d((vector.get(0) / scaleFactor), ((Display.getHeight() - vector.get(1)) / scaleFactor), vector.get(2));
        return null;
    }

    private boolean isValid(Entity entity) {
        return entity instanceof EntityItem;
    }

}

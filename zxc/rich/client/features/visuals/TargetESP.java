package zxc.rich.client.features.visuals;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.GLU;
import zxc.rich.Main;
import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.Event3D;
import zxc.rich.api.utils.render.ColorUtils;
import zxc.rich.api.utils.render.RenderUtils;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.features.combat.KillAura;
import zxc.rich.client.ui.settings.impl.BooleanSetting;
import zxc.rich.client.ui.settings.impl.ColorSetting;
import zxc.rich.client.ui.settings.impl.ListSetting;
import zxc.rich.client.ui.settings.impl.NumberSetting;

import java.awt.*;

import static org.lwjgl.opengl.GL11.GL_LINE_LOOP;

public class TargetESP extends Feature {
    private double circleAnim;
    double height;
    boolean animat;
    public ListSetting bebraPonyxana;
    public NumberSetting circlesize;
    public NumberSetting points;
    public BooleanSetting depthTest;
    public ColorSetting targetEspColor;

    public TargetESP() {
        super("TargetESP", "Рисует красивый круг на энтити", FeatureCategory.VISUALS);
        bebraPonyxana = new ListSetting("TargetESP Mode", "Jello", () -> true, "Jello", "Astolfo");
        circlesize = new NumberSetting("Circle Size", "Размер круга", 0.4F, 0.1F, 3F, 0.1F, () -> bebraPonyxana.currentMode.equalsIgnoreCase("Jello") || bebraPonyxana.currentMode.equalsIgnoreCase("Astolfo"));
        points = new NumberSetting("Points", 30F, 3F, 30F, 1F, () -> bebraPonyxana.currentMode.equalsIgnoreCase("Astolfo"));
        depthTest = new BooleanSetting("DepthTest", "Глубина(test)", false, () -> bebraPonyxana.currentMode.equalsIgnoreCase("Jello"));
        targetEspColor = new ColorSetting("TargetESP Color", Color.PINK.getRGB(), () -> true);

        addSettings(bebraPonyxana, circlesize, points, targetEspColor, depthTest);
    }

    @EventTarget
    public void onRender(Event3D event3D) {

        String mode = bebraPonyxana.getOptions();

        this.setSuffix(mode);

        if (KillAura.target != null && KillAura.target.getHealth() > 0.0 && mc.player.getDistanceToEntity(KillAura.target) <= KillAura.range.getNumberValue()  + KillAura.prerange.getNumberValue() && Main.instance.featureDirector.getFeature(KillAura.class).isEnabled()) {

            if (mode.equalsIgnoreCase("Sims")) {
                float radius = 0.2f;
                int side = 4;

                if (animat) {
                    height = MathHelper.lerp(height, 0.4, 2 * Feature.deltaTime());
                    if (height > 0.39) animat = false;
                } else {
                    height = MathHelper.lerp(height, 0.1, 4 * Feature.deltaTime());
                    if (height < 0.11) animat = true;
                }

                GL11.glPushMatrix();
                GL11.glTranslated(KillAura.target.lastTickPosX + (KillAura.target.posX - KillAura.target.lastTickPosX) * event3D.getPartialTicks() - mc.renderManager.viewerPosX, (KillAura.target.lastTickPosY + (KillAura.target.posY - KillAura.target.lastTickPosY) * event3D.getPartialTicks() - mc.renderManager.viewerPosY) + KillAura.target.height + height, KillAura.target.lastTickPosZ + (KillAura.target.posZ - KillAura.target.lastTickPosZ) * event3D.getPartialTicks() - mc.renderManager.viewerPosZ);
                GL11.glRotatef((mc.player.ticksExisted + mc.timer.renderPartialTicks) * 10, 0.0f, 1.0F, 0.0f);
                RenderUtils.setColor(KillAura.target.hurtTime > 0 ? ColorUtils.TwoColoreffect(new Color(255, 50, 50), new Color(79, 9, 9), Math.abs(System.currentTimeMillis() / (long) 15) / 100.0 + 6.0F * (1 * 2.55) / 60).getRGB() : ColorUtils.TwoColoreffect(new Color(90, 200, 79), new Color(30, 120, 20), Math.abs(System.currentTimeMillis() / (long) 15) / 100.0 + 6.0F * (1 * 2.55) / 90).getRGB());
                RenderUtils.enableSmoothLine(0.5F);
                Cylinder c = new Cylinder();
                c.setDrawStyle(GLU.GLU_LINE);
                GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
                c.draw(0F, radius, 0.3f, side, 100);
                GL11.glTranslated(0.0, 0.0, 0.3);
                c.draw(radius, 0f, 0.3f, side, 100);
                RenderUtils.disableSmoothLine();
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glPopMatrix();
            } else if (mode.equalsIgnoreCase("Jello")) {
                double everyTime = 1500;
                double drawTime = (System.currentTimeMillis() % everyTime);
                boolean drawMode = drawTime > (everyTime / 2);
                double drawPercent = drawTime / (everyTime / 2);
                // true when goes up
                if (!drawMode) {
                    drawPercent = 1 - drawPercent;
                } else {
                    drawPercent -= 1;
                }

                drawPercent = MathHelper.easeInOutQuad(drawPercent, 2);

                mc.entityRenderer.disableLightmap();
                GL11.glPushMatrix();
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glEnable(GL11.GL_LINE_SMOOTH);
                GL11.glEnable(GL11.GL_BLEND);
                if (depthTest.getBoolValue())
                    GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glDisable(GL11.GL_CULL_FACE);
                GL11.glShadeModel(7425);
                mc.entityRenderer.disableLightmap();

                double radius = circlesize.getNumberValue();
                double height = KillAura.target.height + 0.1;
                double x = KillAura.target.lastTickPosX + (KillAura.target.posX - KillAura.target.lastTickPosX) * event3D.getPartialTicks() - mc.renderManager.viewerPosX;
                double y = (KillAura.target.lastTickPosY + (KillAura.target.posY - KillAura.target.lastTickPosY) * event3D.getPartialTicks() - mc.renderManager.viewerPosY) + height * drawPercent;
                double z = KillAura.target.lastTickPosZ + (KillAura.target.posZ - KillAura.target.lastTickPosZ) * event3D.getPartialTicks() - mc.renderManager.viewerPosZ;
                double eased = (height / 3) * ((drawPercent > 0.5) ? 1 - drawPercent : drawPercent) * ((drawMode) ? -1 : 1);

                for (int lox = 0; lox < 360; lox += 5) {
                    double x1 = x - Math.sin(lox * Math.PI / 180F) * radius;
                    double z1 = z + Math.cos(lox * Math.PI / 180F) * radius;
                    double x2 = x - Math.sin((lox - 5) * Math.PI / 180F) * radius;
                    double z2 = z + Math.cos((lox - 5) * Math.PI / 180F) * radius;
                    GL11.glBegin(GL11.GL_QUADS);
                    RenderUtils.glColor(targetEspColor.getColorValue(), 0);
                    GL11.glVertex3d(x1, y + eased, z1);
                    GL11.glVertex3d(x2, y + eased, z2);
                    RenderUtils.glColor(targetEspColor.getColorValue(), 255);
                    GL11.glVertex3d(x2, y, z2);
                    GL11.glVertex3d(x1, y, z1);
                    GL11.glEnd();

                    GL11.glBegin(GL_LINE_LOOP);
                    GL11.glVertex3d(x2, y, z2);
                    GL11.glVertex3d(x1, y, z1);
                    GL11.glEnd();
                }

                GL11.glEnable(GL11.GL_CULL_FACE);
                GL11.glShadeModel(7424);
                GL11.glColor4f(1f, 1f, 1f, 1f);
                if (depthTest.getBoolValue())
                    GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glDisable(GL11.GL_LINE_SMOOTH);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glPopMatrix();
            } else if (mode.equalsIgnoreCase("Astolfo")) {
                if (KillAura.target != null) {
                    if (KillAura.target.getHealth() > 0) {
                        circleAnim += 0.015F * Minecraft.frameTime / 10;
                        RenderUtils.drawCircle3D(KillAura.target, circleAnim + 0.001, event3D.getPartialTicks(), (int) points.getNumberValue(), 4, Color.black.getRGB());
                        RenderUtils.drawCircle3D(KillAura.target, circleAnim - 0.001, event3D.getPartialTicks(), (int) points.getNumberValue(), 4, Color.black.getRGB());
                        RenderUtils.drawCircle3D(KillAura.target, circleAnim, event3D.getPartialTicks(), (int) points.getNumberValue(), 2, targetEspColor.getColorValue());
                        circleAnim = MathHelper.clamp(circleAnim, 0, circlesize.getNumberValue() * 0.5f);
                    } else {
                        circleAnim = 0;
                    }
                }
            }
        }
    }
}

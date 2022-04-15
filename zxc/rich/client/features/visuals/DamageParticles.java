package zxc.rich.client.features.visuals;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ibm.icu.math.BigDecimal;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.Event3D;
import zxc.rich.api.event.events.impl.EventUpdate;
import zxc.rich.api.event.events.impl.RespawnEvent;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.ui.settings.impl.NumberSetting;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class DamageParticles extends Feature {


    public NumberSetting deleteAfter;

    private final Map<Integer, Float> hpData = Maps.newHashMap();
    private final List<Particle> particles = Lists.newArrayList();

    public DamageParticles() {
        super("DamageParticles", "Отображает дамаг-партиклы при ударе", FeatureCategory.VISUALS);
        deleteAfter = new NumberSetting("Delete After", 7, 1, 20, 1, () -> true);
        addSettings(deleteAfter);
    }

    @EventTarget
    public void onRespawn(RespawnEvent event) {
        particles.clear();
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        for (Entity entity : mc.world.loadedEntityList) {
            if (entity instanceof EntityLivingBase) {
                EntityLivingBase ent = (EntityLivingBase) entity;
                final double lastHp = hpData.getOrDefault(ent.getEntityId(), ent.getMaxHealth());
                hpData.remove(entity.getEntityId());
                hpData.put(entity.getEntityId(), ent.getHealth());
                if (lastHp == ent.getHealth()) continue;
                Color color;
                if (lastHp > ent.getHealth()) {
                    color = Color.red;
                } else {
                    color = Color.GREEN;
                }
                Vec3d loc = new Vec3d(entity.posX + Math.random() * 0.5 * (Math.random() > 0.5 ? -1 : 1), entity.getEntityBoundingBox().minY + (entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY) * 0.5, entity.posZ + Math.random() * 0.5 * (Math.random() > 0.5 ? -1 : 1));
                double str = new BigDecimal(Math.abs(lastHp - ent.getHealth())).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
                particles.add(new Particle("" + str, loc.xCoord, loc.yCoord, loc.zCoord, color));
            }
        }
    }

    @EventTarget
    public void onRender3d(Event3D e) {
        if (timerHelper.hasReached(deleteAfter.getNumberValue() * 300)) {
            particles.clear();
            timerHelper.reset();
        }
        if (!particles.isEmpty()) {
            for (Particle p : particles) {
                if (p != null) {
                    GlStateManager.pushMatrix();
                    GlStateManager.enablePolygonOffset();
                    GlStateManager.doPolygonOffset(1, -1500000);
                    GlStateManager.translate(p.posX - mc.getRenderManager().renderPosX, p.posY - mc.getRenderManager().renderPosY, p.posZ - mc.getRenderManager().renderPosZ);
                    GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0, 1, 0);
                    GlStateManager.rotate(mc.getRenderManager().playerViewX, mc.gameSettings.thirdPersonView == 2 ? -1 : 1, 0, 0);
                    GlStateManager.scale(-0.03, -0.03, 0.03);
                    GL11.glDepthMask(false);
                    mc.fontRendererObj.drawStringWithShadow(p.str, (float) (-mc.fontRendererObj.getStringWidth(p.str) * 0.5), -mc.fontRendererObj.FONT_HEIGHT + 1, p.color.getRGB());
                    GL11.glColor4f(1, 1, 1, 1);
                    GL11.glDepthMask(true);
                    GlStateManager.doPolygonOffset(1, 1500000);
                    GlStateManager.disablePolygonOffset();
                    GlStateManager.resetColor();
                    GlStateManager.popMatrix();
                }
            }
        }
    }
    @SuppressWarnings("All")
    class Particle {
        public String str;
        public double posX, posY, posZ;
        public Color color;
        public int ticks;

        public Particle(String str, double posX, double posY, double posZ, Color color) {
            this.str = str;
            this.posX = posX;
            this.posY = posY;
            this.posZ = posZ;
            this.color = color;
            this.ticks = 0;
        }
    }
}
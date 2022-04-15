package zxc.rich.client.ui.draggable.impl;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import zxc.rich.Main;
import zxc.rich.api.utils.math.MathematicHelper;
import zxc.rich.api.utils.render.AnimationHelper;
import zxc.rich.api.utils.render.ColorUtils;
import zxc.rich.api.utils.render.RenderUtils;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.combat.KillAura;
import zxc.rich.client.features.misc.NameProtect;
import zxc.rich.client.ui.draggable.DraggableModule;

import java.awt.*;
import java.util.Objects;

public class TargetHUDComponent extends DraggableModule {

    private float healthBarWidth;
    private double hudHeight;
    private float displayPercent;
    private long lastUpdate;
    private double hurttimeBarWidth;
    private static EntityLivingBase curTarget = null;

    public TargetHUDComponent() {
        super("TargetHUDComponent", 200, 200);
    }

    @Override
    public int getWidth() {
        return 279;
    }

    @Override
    public int getHeight() {
        return 87;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        String mode = KillAura.targetHudMode.getOptions();
        EntityPlayer target = mc.player;
        if (mode.equalsIgnoreCase("Astolfo")) {
            float x = getX(), y = getY();
            double healthWid = (target.getHealth() / target.getMaxHealth() * 120);
            healthWid = MathHelper.clamp(healthWid, 0.0D, 120.0D);
            double check = target != null && target.getHealth() < (target instanceof EntityPlayer ? 18 : 10) && target.getHealth() > 1 ? 8 : 0;
            this.healthBarWidth = MathHelper.lerp((float) healthWid, this.healthBarWidth, 5 * Feature.deltaTime());
            RenderUtils.drawGlowRoundedRect(x, y, x + 155, y + 62, new Color(20, 20, 20, 255).getRGB(), 6, 5);

            if (!target.getName().isEmpty()) {
                mc.fontRendererObj.drawStringWithShadow(target.getName(), x + 31, y + 5, -1);
            }
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, 1);
            GL11.glScalef(2.5f, 2.5f, 2.5f);
            GlStateManager.translate(-x - 3, -y - 2, 1);
            mc.fontRendererObj.drawStringWithShadow(MathematicHelper.round((target.getHealth() / 2.0f), 1) + " \u2764", x + 16, y + 10, new Color(KillAura.targetHudColor.getColorValue()).getRGB());
            GlStateManager.popMatrix();
            GlStateManager.color(1, 1, 1, 1);

            mc.getRenderItem().renderItemOverlays(mc.neverlose500_18, target.getHeldItem(EnumHand.OFF_HAND), (int) x + 137, (int) y + 7);
            mc.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), (int) x + 137, (int) y + 1);


            GuiInventory.drawEntityOnScreen((int) x + 16, (int) y + 55, 25, target.rotationYaw, -target.rotationPitch, target);
            RenderUtils.drawRect2(x + 30, y + 48, 120, 8, new Color(KillAura.targetHudColor.getColorValue()).darker().darker().darker().getRGB());
            RenderUtils.drawRect2(x + 30, y + 48, healthBarWidth + check, 8, new Color(KillAura.targetHudColor.getColorValue()).darker().darker().getRGB());
            RenderUtils.drawRect2(x + 30, y + 48, healthWid, 8, new Color(KillAura.targetHudColor.getColorValue()).getRGB());
        } else if (mode.equalsIgnoreCase("Celestial")) {
            if (target == null)
                return;
            if (target.getHealth() < 0)
                return;
            float x = getX(), y = getY();
            final float health = target.getHealth();
            double hpPercentage = health / target.getMaxHealth();
            hpPercentage = MathHelper.clamp(hpPercentage, 0.0, 1.0);
            final double hpWidth = 110 * hpPercentage;
            final String healthStr = String.valueOf((int) target.getHealth() / 2.0f);
            this.healthBarWidth = MathHelper.lerp(healthBarWidth, (float) hpWidth, 7 * Feature.deltaTime());
            this.hudHeight = MathHelper.lerp(40.0, this.hudHeight, 0.0429999852180481);


            RenderUtils.drawGlowRoundedRect(x + 121, y - 27, x + 279, y + 30f, new Color(22, 22, 22, 200).getRGB(), 8, 9);
            RenderUtils.drawRect(x + 127.5, y - 11.5, x + 273, y - 11, new Color(140, 140, 140).getRGB());
            RenderUtils.drawSmoothRect(x + 162f, y + 16.0f, x + 272f, y + 21f, new Color(22, 22, 22, 150).getRGB());
            RenderUtils.drawGlowRoundedRect(x + 159f, y + 13.5f, x + 165f + healthBarWidth, y + 23.5f, RenderUtils.injectAlpha(new Color(KillAura.targetHudColor.getColorValue()), 100).getRGB(), 8, 10);
            RenderUtils.drawSmoothRect(x + 162f, y + 16.0f, x + 162f + healthBarWidth, y + 21, KillAura.targetHudColor.getColorValue());
            mc.sfui16.drawStringWithShadow("Ground: " + (target.onGround ? "true" : "false"), x + 162f, y - 3f, -1);
            mc.sfui16.drawStringWithShadow("HurtTime", x + 162.5f, y + 7f, -1);
            mc.sfui16.drawCenteredString(target.getName(), (float) (x + 279 / 1.39), y - 19.5f, -1);

            double hurttimePercentage = MathHelper.clamp(target.hurtTime, 1.0, 0.3);
            final double hurttimeWidth = 71.0 * hurttimePercentage;
            this.hurttimeBarWidth = MathHelper.lerp(hurttimeWidth, this.hurttimeBarWidth, 0.0529999852180481);
            RenderUtils.drawRect(x + 201f, y + 9f, x + 272, y + 11, new Color(22, 22, 22, 150).getRGB());
            RenderUtils.drawSmoothRect(x + 201f, y + 9f, (float) (x + 201 + this.hurttimeBarWidth), y + 11, KillAura.targetHudColor.getColorValue());
            RenderUtils.drawGlowRoundedRect(x + 198.5f, y + 6.5f, (float) (x + 203.5f + this.hurttimeBarWidth), y + 13, RenderUtils.injectAlpha(new Color(KillAura.targetHudColor.getColorValue()), 140).getRGB(), 8, 9);
            mc.getRenderItem().renderItemOverlays(mc.neverlose500_18, target.getHeldItem(EnumHand.OFF_HAND), (int) x + 243, (int) y - 13);
            mc.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), (int) x + 100, (int) y - 10);
            RenderUtils.drawSmoothRect((float) (x + 125.5), y - 25f, x + 275, y - 24f, KillAura.targetHudColor.getColorValue());

        } else if (mode.equalsIgnoreCase("Moon")) {
            if (KillAura.targetHud.getBoolValue()) {

                if (target == null)
                    return;
                if (target.getHealth() < 0)
                    return;
                float x = getX(), y = getY();
                final float health = target.getHealth();
                double hpPercentage = health / target.getMaxHealth();
                hpPercentage = MathHelper.clamp(hpPercentage, 0.0, 1.0);
                final double hpWidth = 110 * hpPercentage;
                this.healthBarWidth = MathHelper.EaseOutBack((float) healthBarWidth, (float) hpWidth, (float) (6 * Feature.deltaTime()));
                this.hudHeight = MathHelper.lerp(40.0, this.hudHeight, 0.0429999852180481);

                RenderUtils.drawGradientRect((float) (x + 125), y - 22, x + 242, y + 16, new Color(0, 0, 0, 200).getRGB(), new Color(26, 26, 26, 200).getRGB());
                RenderUtils.drawRect(x + 127, y + 12, x + 240, y + 14, new Color(22, 22, 22, 150).getRGB());
                RenderUtils.drawRect(x + 127, y + 12, x + 130 + healthBarWidth, y + 14, RenderUtils.getHealthColor(target).getRGB());
                String string2 = "" + MathematicHelper.round(target.getHealth(), 1);
                String string23 = "" + MathematicHelper.round(mc.player.getDistanceToEntity(target), 1);
                String string24 = "" + MathematicHelper.round(target.hurtTime, 1);

                mc.sfui16.drawStringWithShadow("Health: " + string2, x + 162f, y - 7.5f, -1);
                mc.sfui16.drawStringWithShadow("Distance: " + string23 + "m", x + 162f, y + 0.5f, -1);
                mc.sfui16.drawStringWithShadow("Hurt " + string24, x + 172f, y + 0.5f, -1);

                mc.neverlose900_15.drawStringWithShadow(target.getName(), (float) (x + 162f), y - 17.5f, -1);
                mc.getRenderItem().renderItemOverlays(mc.neverlose500_18, target.getHeldItem(EnumHand.OFF_HAND), (int) x + 222, (int) y - 54);
                mc.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), (int) x + 225, (int) y - 39);
                try {
                    for (NetworkPlayerInfo targetHead : mc.player.connection.getPlayerInfoMap()) {
                        if (mc.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == target) {
                            mc.getTextureManager().bindTexture(targetHead.getLocationSkin());
                            float hurtPercent = getHurtPercent(target);
                            GL11.glPushMatrix();
                            GL11.glColor4f(1, 1 - hurtPercent, 1 - hurtPercent, 1);
                            Gui.drawScaledCustomSizeModalRect((int) x + 127, (int) (y - 20f), 8.0f, 8.0f, 8, 8, 31, 31, 64.0f, 64.0f);
                            GL11.glPopMatrix();
                            GlStateManager.bindTexture(0);
                        }
                        GL11.glDisable(3089);
                        GlStateManager.resetColor();

                    }
                } catch (Exception ignored) {
                }
            }
        } else if (mode.equalsIgnoreCase("Rise")) {
            if (KillAura.targetHud.getBoolValue()) {

                if (target == null)
                    return;
                if (target.getHealth() < 0)
                    return;
                float x = getX(), y = getY();
                final float health = target.getHealth();
                double hpPercentage = health / target.getMaxHealth();
                hpPercentage = MathHelper.clamp(hpPercentage, 0.0, 1.0);
                final double hpWidth = 110 * hpPercentage;
                this.healthBarWidth = MathHelper.EaseOutBack((float) healthBarWidth, (float) hpWidth, (float) (6 * Feature.deltaTime()));
                this.hudHeight = MathHelper.lerp(40.0, this.hudHeight, 0.0429999852180481);

                RenderUtils.drawGlowRoundedRect((float) (x + 121), y - 27, x + 279, y + 27f, new Color(41, 41, 41, 200).getRGB(), 8, 9);
                RenderUtils.drawGradientRect(x + 128f, y + 14.0f, x + 145f + healthBarWidth, y + 20, new Color(KillAura.targetHudColor.getColorValue()).getRGB(), new Color(KillAura.targetHudColor.getColorValue()).darker().getRGB());

                mc.sfui16.drawString("Name " + target.getName(), (float) (x + 160), y - 11.01f, -1);
                String string23 = "" + MathematicHelper.round(mc.player.getDistanceToEntity(target), 1);
                mc.sfui16.drawStringWithShadow("Distance " + string23, x + 160f, y - 0.01f, -1);
                String string24 = "" + MathematicHelper.round(target.hurtTime, 1);
                mc.sfui16.drawStringWithShadow("Hurt " + string24, x + 208f, y - 0.01f, -1);
                String string2 = "" + MathematicHelper.round(target.getHealth(), 1);
                mc.sfui16.drawStringWithShadow(string2, x + 147 + healthBarWidth, y + 15, -1);
                mc.getRenderItem().renderItemOverlays(mc.neverlose500_18, target.getHeldItem(EnumHand.OFF_HAND), (int) x + 240, (int) y - 27);
                mc.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), (int) x + 259, (int) y - 25);

                try {
                    for (NetworkPlayerInfo targetHead : mc.player.connection.getPlayerInfoMap()) {
                        if (mc.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == target) {
                            mc.getTextureManager().bindTexture(targetHead.getLocationSkin());
                            float hurtPercent = getHurtPercent(target);
                            GL11.glPushMatrix();
                            GL11.glColor4f(1, 1 - hurtPercent, 1 - hurtPercent, 1);
                            Gui.drawScaledCustomSizeModalRect((int) x + 128, (int) (y - 17f), 8.0f, 8.0f, 8, 8, 28, 28, 64.0f, 64.0f);
                            GL11.glPopMatrix();
                            GlStateManager.bindTexture(0);
                        }
                        GL11.glDisable(3089);
                        GlStateManager.resetColor();

                    }
                } catch (Exception ignored) {
                }
            }
        }
        super.render(mouseX, mouseY);
    }

    float scale = 0.0f;

    @Override
    public void draw() {
        if (KillAura.target != null) {
            curTarget = KillAura.target;
            scale = MathHelper.lerp(scale, 1, 7 * Feature.deltaTime());
        } else {
            scale = MathHelper.lerp(scale, 0, 7 * Feature.deltaTime());
        }
        int color = KillAura.targetHudColor.getColorValue();
        String mode = KillAura.targetHudMode.getOptions();
        EntityLivingBase target = KillAura.target;
        long time = System.currentTimeMillis();
        float pct = (float) (time - this.lastUpdate) / (20 * 50.0f);
        this.lastUpdate = System.currentTimeMillis();
        if (target != null) {
            if (this.displayPercent < 1.0f) {
                displayPercent += pct;
            }
            if (this.displayPercent > 1.0f) {
                this.displayPercent = 1.0f;
            }
        } else {
            if (this.displayPercent > 0.0f) {
                displayPercent -= pct;
            }
            if (this.displayPercent < 0.0f) {
                this.displayPercent = 0.0f;
            }
        }

        if (mode.equalsIgnoreCase("Astolfo")) {
            if (KillAura.targetHud.getBoolValue()) {

                if (target == null)
                    return;
                if (target.getHealth() < 0)
                    return;

                float x = getX(), y = getY();
                double healthWid = (target.getHealth() / target.getMaxHealth() * 120);
                healthWid = MathHelper.clamp(healthWid, 0.0D, 120.0D);
                double check = target.getHealth() < 18 && target.getHealth() > 1 ? 8 : 0;
                healthBarWidth = AnimationHelper.calculateCompensation((float) healthWid, healthBarWidth, (long) 0.005, 0.005);
                RenderUtils.drawRect2(x, y, 155, 60, new Color(20, 20, 20, 200).getRGB());
                if (!target.getName().isEmpty()) {
                    mc.fontRendererObj.drawStringWithShadow(Main.instance.featureDirector.getFeature(NameProtect.class).isEnabled() && NameProtect.otherName.getBoolValue() ? "Protected" : target.getName(), x + 31, y + 5, -1);
                }
                GlStateManager.pushMatrix();
                GlStateManager.translate(x, y, 1);
                GlStateManager.scale(2.5f, 2.5f, 2.5f);
                GlStateManager.translate(-x - 3, -y - 2, 1);
                mc.fontRendererObj.drawStringWithShadow(MathematicHelper.round((target.getHealth() / 2.0f), 1) + " \u2764", x + 16, y + 10, new Color(color).getRGB());
                GlStateManager.popMatrix();
                GlStateManager.color(1, 1, 1, 1);

                mc.getRenderItem().renderItemOverlays(mc.neverlose500_18, target.getHeldItem(EnumHand.OFF_HAND), (int) x + 137, (int) y + 7);
                mc.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), (int) x + 137, (int) y + 1);

                GuiInventory.drawEntityOnScreen(x + 16, y + 55, 25, target.rotationYaw, -target.rotationPitch, target);
                RenderUtils.drawRect2(x + 30, y + 48, 120, 8, new Color(color).darker().darker().darker().getRGB());
                RenderUtils.drawRect2(x + 30, y + 48, healthBarWidth + check, 8, new Color(color).darker().getRGB());
                RenderUtils.drawRect2(x + 30, y + 48, healthWid, 8, new Color(color).getRGB());
            }

        } else if (mode.equalsIgnoreCase("Celestial")) {
            if (KillAura.targetHud.getBoolValue()) {

                if (target == null)
                    return;
                if (target.getHealth() < 0)
                    return;
                float x = getX(), y = getY();
                final float health = curTarget.getHealth();
                double hpPercentage = health / curTarget.getMaxHealth();
                hpPercentage = MathHelper.clamp(hpPercentage, 0.0, 1.0);
                final double hpWidth = 110 * hpPercentage;
                this.healthBarWidth = MathHelper.EaseOutBack((float) healthBarWidth, (float) hpWidth, (float) (6 * Feature.deltaTime()));
                this.hudHeight = MathHelper.lerp(40.0, this.hudHeight, 0.0429999852180481);
                GlStateManager.pushMatrix();
                GL11.glTranslated(x + 181, y + 26, 0);
                GL11.glScaled(scale, scale, 0);
                GL11.glTranslated(-(x + 181), -(y + 26), 0);
                RenderUtils.drawGlowRoundedRect((float) (x + 121), y - 27, x + 279, y + 30f, new Color(26, 26, 26, 200).getRGB(), 8, 9);
                RenderUtils.drawRect(x + 127.5, y - 11.5, x + 273, y - 11, new Color(140, 140, 140).getRGB());
                RenderUtils.drawSmoothRect(x + 162f, y + 16.0f, x + 272f, y + 21f, new Color(22, 22, 22, 150).getRGB());
                RenderUtils.drawGlowRoundedRect(x + 159f, y + 13.5f, (float) (x + 165f + healthBarWidth), y + 23.5f, RenderUtils.injectAlpha(new Color(color), (int) 100).getRGB(), 8, 10);
                RenderUtils.drawSmoothRect(x + 162f, y + 16.0f, x + 162f + healthBarWidth, y + 21, color);

                mc.sfui16.drawStringWithShadow("Ground: " + (curTarget.onGround ? "true;" : "false;"), x + 162f, y - 3f, -1);
                mc.sfui16.drawStringWithShadow("HurtTime", x + 162.5f, y + 7f, -1);
                if (!target.getName().isEmpty()) {
                    mc.sfui16.drawCenteredString(Main.instance.featureDirector.getFeature(NameProtect.class).isEnabled() && NameProtect.otherName.getBoolValue() ? "Protected" : curTarget.getName(), (float) (x + 279 / 1.39), y - 19.5f, -1);
                }
                double hurttimePercentage = net.minecraft.util.math.MathHelper.clamp(KillAura.target.hurtTime, 0.0, 0.9);
                final double hurttimeWidth = 80.0 * hurttimePercentage;
                this.hurttimeBarWidth = MathHelper.EaseOutBack((float) this.hurttimeBarWidth, (float) hurttimeWidth, (float) (4 * Feature.deltaTime()));
                RenderUtils.drawRect(x + 201f, y + 9f, (float) (x + 201 + this.hurttimeBarWidth), y + 11, color);

                mc.getRenderItem().renderItemOverlays(mc.neverlose500_18, curTarget.getHeldItem(EnumHand.OFF_HAND), (int) x + 243, (int) y - 13);
                mc.getRenderItem().renderItemIntoGUI(curTarget.getHeldItem(EnumHand.OFF_HAND), (int) x + 259, (int) y - 10);

                //Gui.drawRect(x + 44, y + 219 - 406, x + 166, y + 222.5 - 406, Main.getClientColor().getRGB());
                RenderUtils.drawSmoothRect((float) (x + 125.5), y - 25f, x + 275, y - 24f, color);
                try {
                    for (NetworkPlayerInfo targetHead : mc.player.connection.getPlayerInfoMap()) {
                        if (mc.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == target) {
                            mc.getTextureManager().bindTexture(targetHead.getLocationSkin());
                            float hurtPercent = getHurtPercent(target);
                            GL11.glPushMatrix();
                            GL11.glColor4f(1, 1 - hurtPercent, 1 - hurtPercent, 1);
                            Gui.drawScaledCustomSizeModalRect((int) x + 127, (int) (y - 8f), 8.0f, 8.0f, 8, 8, 31, 31, 64.0f, 64.0f);
                            GL11.glPopMatrix();
                            GlStateManager.bindTexture(0);
                        }
                        GL11.glDisable(3089);
                    }
                } catch (Exception ignored) {
                } finally {
                    GlStateManager.popMatrix();
                    GlStateManager.resetColor();

                }

            }
        } else if (mode.equalsIgnoreCase("Moon")) {
            if (KillAura.targetHud.getBoolValue()) {

                if (target == null)
                    return;
                if (target.getHealth() < 0)
                    return;
                float x = getX(), y = getY();
                final float health = curTarget.getHealth();
                double hpPercentage = health / curTarget.getMaxHealth();
                hpPercentage = MathHelper.clamp(hpPercentage, 0.0, 1.0);
                final double hpWidth = 110 * hpPercentage;
                this.healthBarWidth = MathHelper.EaseOutBack((float) healthBarWidth, (float) hpWidth, (float) (6 * Feature.deltaTime()));
                this.hudHeight = MathHelper.lerp(40.0, this.hudHeight, 0.0429999852180481);
                GlStateManager.pushMatrix();
                GL11.glTranslated(x + 181, y + 26, 0);
                GL11.glScaled(scale, scale, 0);
                GL11.glTranslated(-(x + 181), -(y + 26), 0);
                RenderUtils.drawGradientRect((float) (x + 125), y - 22, x + 242, y + 16, new Color(0, 0, 0, 200).getRGB(), new Color(26, 26, 26, 200).getRGB());
                RenderUtils.drawRect(x + 127, y + 12, x + 240, y + 14, new Color(22, 22, 22, 150).getRGB());
                RenderUtils.drawRect(x + 127, y + 12, x + 130 + healthBarWidth, y + 14, RenderUtils.getHealthColor(target).getRGB());
                String string2 = "" + MathematicHelper.round(target.getHealth(), 1);
                String string23 = "" + MathematicHelper.round(mc.player.getDistanceToEntity(target), 1);

                mc.sfui16.drawStringWithShadow("Health: " + string2, x + 162f, y - 7.5f, -1);
                mc.sfui16.drawStringWithShadow("Distance: " + string23 + "m", x + 162f, y + 0.5f, -1);
                if (!target.getName().isEmpty()) {
                    mc.neverlose900_15.drawStringWithShadow(Main.instance.featureDirector.getFeature(NameProtect.class).isEnabled() && NameProtect.otherName.getBoolValue() ? "Protected" : curTarget.getName(), (float) (x + 162f), y - 17.5f, -1);
                }
                mc.getRenderItem().renderItemOverlays(mc.neverlose500_18, curTarget.getHeldItem(EnumHand.OFF_HAND), (int) x + 222, (int) y - 54);
                mc.getRenderItem().renderItemIntoGUI(curTarget.getHeldItem(EnumHand.OFF_HAND), (int) x + 225, (int) y - 39);

                try {
                    for (NetworkPlayerInfo targetHead : mc.player.connection.getPlayerInfoMap()) {
                        if (mc.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == target) {
                            mc.getTextureManager().bindTexture(targetHead.getLocationSkin());
                            float hurtPercent = getHurtPercent(target);
                            GL11.glPushMatrix();
                            GL11.glColor4f(1, 1 - hurtPercent, 1 - hurtPercent, 1);
                            Gui.drawScaledCustomSizeModalRect((int) x + 127, (int) (y - 20f), 8.0f, 8.0f, 8, 8, 31, 31, 64.0f, 64.0f);
                            GL11.glPopMatrix();
                            GlStateManager.bindTexture(0);
                        }
                        GL11.glDisable(3089);
                        GlStateManager.resetColor();

                    }
                } catch (Exception ignored) {
                } finally {
                    GlStateManager.popMatrix();
                }
            }
        } else if (mode.equalsIgnoreCase("Rise")) {
            if (KillAura.targetHud.getBoolValue()) {

                if (target == null)
                    return;
                if (target.getHealth() < 0)
                    return;
                float x = getX(), y = getY();
                final float health = curTarget.getHealth();
                double hpPercentage = health / curTarget.getMaxHealth();
                hpPercentage = MathHelper.clamp(hpPercentage, 0.0, 1.0);
                final double hpWidth = 110 * hpPercentage;
                this.healthBarWidth = MathHelper.EaseOutBack((float) healthBarWidth, (float) hpWidth, (float) (6 * Feature.deltaTime()));
                this.hudHeight = MathHelper.lerp(40.0, this.hudHeight, 0.0429999852180481);
                GlStateManager.pushMatrix();
                GL11.glTranslated(x + 181, y + 26, 0);
                GL11.glScaled(scale, scale, 0);
                GL11.glTranslated(-(x + 181), -(y + 26), 0);
                RenderUtils.drawGlowRoundedRect((float) (x + 121), y - 27, x + 279, y + 27f, new Color(25, 25, 25, 120).getRGB(), 6, 9);
                RenderUtils.drawRect(x + 128f, y + 14.0f, x + 145f + healthBarWidth, y + 20, new Color(KillAura.targetHudColor.getColorValue()).getRGB());

                mc.sfui16.drawString("Name " + curTarget.getName(), (float) (x + 160), y - 11.01f, -1);
                String string23 = "" + MathematicHelper.round(mc.player.getDistanceToEntity(target), 1);
                mc.sfui16.drawStringWithShadow("Distance " + string23, x + 160f, y - 0.01f, -1);
                String string24 = "" + MathematicHelper.round(target.hurtTime, 1);
                mc.sfui16.drawStringWithShadow("Hurt " + string24, x + 208f, y - 0.01f, -1);
                String string2 = "" + MathematicHelper.round(target.getHealth(), 1);
                mc.sfui16.drawStringWithShadow(string2, x + 147 + healthBarWidth, y + 15, -1);
                mc.getRenderItem().renderItemOverlays(mc.neverlose500_18, curTarget.getHeldItem(EnumHand.OFF_HAND), (int) x + 240, (int) y - 27);
                mc.getRenderItem().renderItemIntoGUI(curTarget.getHeldItem(EnumHand.OFF_HAND), (int) x + 259, (int) y - 25);

                //Gui.drawRect(x + 44, y + 219 - 406, x + 166, y + 222.5 - 406, Main.getClientColor().getRGB());
                try {
                    for (NetworkPlayerInfo targetHead : mc.player.connection.getPlayerInfoMap()) {
                        if (mc.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == target) {
                            mc.getTextureManager().bindTexture(targetHead.getLocationSkin());
                            float hurtPercent = getHurtPercent(target);
                            GL11.glPushMatrix();
                            GL11.glColor4f(1, 1 - hurtPercent, 1 - hurtPercent, 1);
                            Gui.drawScaledCustomSizeModalRect((int) x + 128, (int) (y - 17f), 8.0f, 8.0f, 8, 8, 28, 28, 64.0f, 64.0f);
                            GL11.glPopMatrix();
                            GlStateManager.bindTexture(0);
                        }
                        GL11.glDisable(3089);
                        GlStateManager.resetColor();
                    }
                } catch (Exception ignored) {
                } finally {
                    GlStateManager.popMatrix();
                }
            }
        }
        super.draw();
    }

    public static float getRenderHurtTime(EntityLivingBase hurt) {
        return (float) hurt.hurtTime - (hurt.hurtTime != 0 ? mc.timer.renderPartialTicks : 0.0f);
    }

    public static float getHurtPercent(EntityLivingBase hurt) {
        return getRenderHurtTime(hurt) / (float) 10;
    }

}

package zxc.rich.client.features.combat;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Mouse;
import zxc.rich.Main;
import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.*;
import zxc.rich.api.utils.combat.EntityHelper;
import zxc.rich.api.utils.combat.GCDFix;
import zxc.rich.api.utils.combat.KillAuraHelper;
import zxc.rich.api.utils.combat.RotationHelper;
import zxc.rich.api.utils.math.MathematicHelper;
import zxc.rich.api.utils.movement.MovementUtils;
import zxc.rich.api.utils.world.InvenotryUtil;
import zxc.rich.api.utils.world.TimerHelper;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.features.movement.Jesus;
import zxc.rich.client.ui.draggable.DraggableModule;
import zxc.rich.client.ui.notification.NotificationMode;
import zxc.rich.client.ui.notification.NotificationRenderer;
import zxc.rich.client.ui.settings.impl.BooleanSetting;
import zxc.rich.client.ui.settings.impl.ColorSetting;
import zxc.rich.client.ui.settings.impl.ListSetting;
import zxc.rich.client.ui.settings.impl.NumberSetting;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class KillAura extends Feature {
    public static TimerHelper oldTimerPvp = new TimerHelper();
    public static float yaw;
    public float pitch;
    public float pitch2 = 0;
    public float yaw2 = 0;
    private boolean isBlocking;
    public TimerHelper shieldBreakerTimer = new TimerHelper();
    private int changeSlotCounter;
    private boolean isBreaked;
    public static EntityLivingBase target;
    private static float[] lastRotations;
    public static ListSetting clickMode = new ListSetting("PvP Mode", "1.9", () -> true, "1.9", "1.8");
    public static ListSetting rotationMode = new ListSetting("Rotation Mode", "Matrix", () -> true, "Matrix", "Sunrise", "Snap");
    public static ListSetting sortMode = new ListSetting("Priority", "Distance", () -> true, "Distance", "Health", "Higher Armor", "Lowest Armor");
    public static NumberSetting fov = new NumberSetting("FOV", "Позволяет редактировать радиус в котором вы можете ударить игрока", 180, 0, 180, 1, () -> true);
    public static NumberSetting attackCoolDown = new NumberSetting("Attack CoolDown", "Редактирует скорость удара", 0.85F, 0.1F, 1F, 0.01F, () -> true);
    public static NumberSetting range = new NumberSetting("AttackRange", "Дистанция в которой вы можете ударить игрока", 3.6F, 3, 6, 0.01F, () -> true);
    public static NumberSetting prerange = new NumberSetting("Pre Range", 0.3f, 0, 6, 0.01F, () -> true);
    public static NumberSetting minAps = new NumberSetting("Min CPS", "Минимальное количество кликов в секунду", 12, 1, 20, 1, () -> clickMode.currentMode.equals("1.8"), NumberSetting.NumberType.APS);
    public static NumberSetting maxAps = new NumberSetting("Max CPS", "Максимальное количество кликов в секунду", 13, 1, 20, 1, () -> clickMode.currentMode.equals("1.8"), NumberSetting.NumberType.APS);
    public static BooleanSetting auraplayers = new BooleanSetting("Players", "Позволяет бить игроков", true, () -> true);
    public static BooleanSetting auramobs = new BooleanSetting("Mobs", "Позволяет бить мобов", true, () -> true);
    public static BooleanSetting invisiblecheck = new BooleanSetting("Invisible", "Позволяет бить невидемых существ", true, () -> true);
    public static BooleanSetting walls = new BooleanSetting("Walls", "Позволяет бить сквозь стены", true, () -> true);
    public static BooleanSetting nakedPlayer = new BooleanSetting("Ignore Naked Players", "Не бьет голых игроков", false, () -> true);
    public static BooleanSetting onlyCrit = new BooleanSetting("Only Critical", "Бьет в нужный момент для крита", false, () -> true);
    public static NumberSetting critFallDistance = new NumberSetting("Criticals Fall Distance", "Регулировка дистанции до земли для крита", 0.2F, 0.08F, 1F, 0.01f, () -> onlyCrit.getBoolValue());
    public static BooleanSetting stopSprint = new BooleanSetting("Stop Sprinting", "Автоматически выключает спринт", false, () -> true);
    public static BooleanSetting shieldBreak = new BooleanSetting("Break Shield", "Автоматически ломает щит сопернику", false, () -> true);
    public static NumberSetting breaker_delay = new NumberSetting("Breaker Delay", "Регулировка скорости шилдбрейкера(чем меньше - тем быстрее закливает)", 80F, 10F, 250F, 1f, () -> shieldBreak.getBoolValue());
    private final BooleanSetting shieldFix = new BooleanSetting("Shield Fix", "Автоматически зажимает щит(обход)", false, () -> true);
    public static BooleanSetting rayCast = new BooleanSetting("Ray-Cast", true, () -> true);
    public static BooleanSetting strafing = new BooleanSetting("Strafing", false, () -> true);
    public static BooleanSetting silentMove = new BooleanSetting("SilentMove", false, () -> true);
    public static BooleanSetting targetHud = new BooleanSetting("TargetHUD", "Отображает хп, еду, броню соперника на экране", true, () -> true);
    public static ListSetting targetHudMode = new ListSetting("TargetHud Mode", "Astolfo", () -> targetHud.getBoolValue(), "Astolfo", "Celestial", "Moon", "Rise");
    public static ColorSetting targetHudColor = new ColorSetting("TargetHUD Color", Color.PINK.getRGB(), () -> targetHud.getBoolValue());

    public KillAura() {
        super("KillAura", "Автоматически аттакует энтити", FeatureCategory.COMBAT);
        addSettings(rotationMode, sortMode, clickMode, fov, range, prerange, attackCoolDown, minAps, maxAps, auraplayers, auramobs, invisiblecheck, walls, nakedPlayer, onlyCrit, critFallDistance, stopSprint, shieldBreak, breaker_delay, shieldFix, rayCast, strafing, silentMove, targetHud, targetHudMode, targetHudColor);
    }


    @EventTarget
    public void onPreAttack(EventPreMotionUpdate event) {
        if (mc.player.getHealth() > 0) {

            /* VARIABLES */

            String mode = rotationMode.getOptions();

            this.setSuffix(mode + ", " + MathematicHelper.round(range.getNumberValue(), 1));

            /* ENTITY SORT BY MODE */

            target = KillAuraHelper.getSortEntities();

            /* ANOTHER CHECKS */
            if (target == null) {
                return;
            }

            if (!RotationHelper.isLookingAtEntity(yaw, pitch, 0.06F, 0.06F, 0.06F, target, range.getNumberValue()) && rayCast.getBoolValue()) {
                return;
            }
            /* ATTACK METHOD */

            if (MovementUtils.isBlockAboveHead()) {
                if (!(mc.player.fallDistance >= critFallDistance.getNumberValue()) && mc.player.getCooledAttackStrength(0.8F) == 1 && onlyCrit.getBoolValue() && !mc.player.isOnLadder() && !mc.player.isInLiquid() && !mc.player.isInWeb) {
                    return;
                }
            } else {
                if (mc.player.fallDistance != 0 && onlyCrit.getBoolValue() && !mc.player.isOnLadder() && !mc.player.isInLiquid() && !mc.player.isInWeb) {
                    return;
                }
            }
            if (strafing.getBoolValue()) {
                if (!mc.gameSettings.keyBindForward.pressed)
                    return;
                if (!MovementUtils.isMoving())
                    return;

                MovementUtils.strafe(MovementUtils.getSpeed() - 0.0135F);
            }
            attackEntitySuccess(target);
        }
    }

    @EventTarget
    public void onRotations(EventPreMotionUpdate event) {
        /* VARIABLES */
        String mode = rotationMode.getOptions();

        /* CHECKS AND ROTATIONS */

        if (target == null) {
            return;
        }
        if (target.getHealth() > 0) {

            /* ROTATIONS */
            float[] matrixrots = RotationHelper.getRotations(target, true, 1.5f, 1.5f);
            float[] sunriseRots = RotationHelper.getRotations(target, true, 1.5f, 1.5f);
            float[] snaprots = RotationHelper.lookAtEntity(target);

            if (mode.equalsIgnoreCase("Matrix")) {
                if (timerHelper.hasReached(50) || lastRotations == null) {
                    event.setYaw(matrixrots[0]);
                    event.setPitch(matrixrots[1]);
                    yaw = matrixrots[0];
                    pitch = matrixrots[1];
                    mc.player.renderYawOffset = matrixrots[0];
                    mc.player.rotationYawHead = matrixrots[0];
                    mc.player.rotationPitchHead = matrixrots[1];
                    timerHelper.reset();
                }

            } else if (mode.equalsIgnoreCase("Sunrise")) {
                if (timerHelper.hasReached(50) || lastRotations == null) {
                    yaw2 = GCDFix.getFixedRotation(MathHelper.Rotate(yaw2, sunriseRots[0], 40, 50));
                    pitch2 = GCDFix.getFixedRotation(MathHelper.Rotate(pitch2, sunriseRots[1], 0.35f, 2.1f));
                    event.setYaw(yaw2);
                    event.setPitch(pitch2);
                    yaw = yaw2;
                    pitch = pitch2;
                    mc.player.renderYawOffset = yaw2;
                    mc.player.rotationYawHead = yaw2;
                    mc.player.rotationPitchHead = pitch2;
                    timerHelper.reset();
                }
            } else if (mode.equalsIgnoreCase("Snap") && mc.player.getCooledAttackStrength(0) >= attackCoolDown.getNumberValue() && target != null) {
                mc.player.rotationYaw = snaprots[0];
                yaw = snaprots[0];
                pitch = snaprots[1];
                mc.player.rotationPitch = snaprots[1];
            }
        }
    }

    public static boolean canApsAttack() {
        int apsMultiplier = (int) (14 / MathematicHelper.randomizeFloat((int) maxAps.getNumberValue(), (int) minAps.getNumberValue()));
        if (oldTimerPvp.hasReached(50 * apsMultiplier)) {
            oldTimerPvp.reset();
            return true;
        }
        return false;
    }

    private void attackEntitySuccess(EntityLivingBase target) {
        /* ATTACK METHOD (MODS) */

        if (target.getHealth() > 0) {
            switch (clickMode.getOptions()) {
                case "1.9":
                    float attackDelay = attackCoolDown.getNumberValue();
                    if (mc.player.getCooledAttackStrength(attackDelay) == 1) {
                        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
                        mc.playerController.attackEntity(mc.player, EntityHelper.rayCast(target, range.getNumberValue()));
                        mc.player.swingArm(EnumHand.MAIN_HAND);

                    }
                    break;
                case "1.8":
                    if (canApsAttack()) {
                        if (this.isBlocking && mc.player.getHeldItemMainhand().getItem() instanceof ItemSword) {
                            if (timerHelper.hasReached(100)) {
                                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -1, -1), EnumFacing.DOWN));
                                this.isBlocking = false;
                                timerHelper.reset();
                            }
                        }
                        mc.playerController.attackEntity(mc.player, EntityHelper.rayCast(target, range.getNumberValue()));
                        mc.player.swingArm(EnumHand.MAIN_HAND);
                    }
                    break;
            }
        }
    }

    @EventTarget
    public void onRender(Event2D event2D) {
        for (DraggableModule draggableModule : Main.instance.draggableManager.getMods()) {
            if (isEnabled() && !draggableModule.name.equals("InfoComponent") && !draggableModule.name.equals("PotionComponent") && !draggableModule.name.equals("SessionInfoComponent")) {
                draggableModule.draw();
            }
        }
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        /* INTERACT FIX */
        if (event.getPacket() instanceof CPacketUseEntity) {
            CPacketUseEntity cPacketUseEntity = (CPacketUseEntity) event.getPacket();

            if (cPacketUseEntity.getAction() == CPacketUseEntity.Action.INTERACT) {
                event.setCancelled(true);
            }

            if (cPacketUseEntity.getAction() == CPacketUseEntity.Action.INTERACT_AT) {
                event.setCancelled(true);
            }
        }
    }

    @EventTarget
    public void onAttackSilent(EventAttackSilent eventAttackSilent) {
        /* SHIELD UNPRESSER */
        if (mc.player.isBlocking() && mc.player.getHeldItemOffhand().getItem() instanceof ItemShield && shieldFix.getBoolValue()) {
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-0.8, -0.8, -0.8), EnumFacing.DOWN));
            mc.playerController.processRightClick(mc.player, mc.world, EnumHand.OFF_HAND);
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        /* SHIELD FIX */
        if (shieldFix.getBoolValue()) {
            if (target.getHeldItemMainhand().getItem() instanceof ItemAxe) {
                if (mc.gameSettings.keyBindUseItem.isKeyDown()) {
                    mc.gameSettings.keyBindUseItem.pressed = false;
                }
            } else {
                mc.gameSettings.keyBindUseItem.pressed = Mouse.isButtonDown(1);
            }
        }
    }

    private boolean desync = false;

    @EventTarget
    public void onSound(EventReceivePacket sound) {
        if (sound.getPacket() instanceof SPacketSoundEffect) {
            SPacketSoundEffect so = (SPacketSoundEffect) sound.getPacket();
            if (so.getSound() == SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE) {
                desync = true;
            }
        }
    }

    @EventTarget
    public void onShieldBreaker(EventPreMotionUpdate eventPreMotionUpdate) {
        if (target == null) {
            return;
        }
        if (InvenotryUtil.doesHotbarHaveAxe()) {
            if (shieldBreak.getBoolValue() && desync &&  (target.getHeldItemOffhand().getItem() instanceof ItemShield || target.getHeldItemMainhand().getItem() instanceof ItemShield)) {
                if (target.isBlocking() && target.isHandActive() && target.isActiveItemStackBlocking(2) && (mc.player.getDistanceToEntity(target) < KillAura.range.getNumberValue())) {
                    if (RotationHelper.isAimAtMe(target, 100)) {
                        if (mc.player.inventory.currentItem != InvenotryUtil.getAxe()) {
                            mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem = InvenotryUtil.getAxe()));
                        }
                        if (mc.player.inventory.currentItem == InvenotryUtil.getAxe()) {
                            if (shieldBreakerTimer.hasReached((breaker_delay.getNumberValue()))) {
                                isBreaked = true;
                                mc.playerController.attackEntity(mc.player, target);
                                mc.player.swingArm(EnumHand.MAIN_HAND);
                                mc.player.resetCooldown();
                                shieldBreakerTimer.reset();


                            }

                            this.changeSlotCounter = -1;
                        } else {
                            this.changeSlotCounter = 0;
                        }
                    }
                } else if (mc.player.inventory.currentItem != InvenotryUtil.getSwordAtHotbar() && this.changeSlotCounter == -1 && InvenotryUtil.getSwordAtHotbar() != -1 && (!target.isBlocking() || !target.isHandActive() || !target.isActiveItemStackBlocking(2))) {
                    mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem = InvenotryUtil.getSwordAtHotbar()));
                    this.changeSlotCounter = 0;
                    NotificationRenderer.queue(TextFormatting.GREEN + "Shield-Breaker", TextFormatting.RESET + "Successfully destroyed " + target.getName() + " shield", 2, NotificationMode.SUCCESS);
                    isBreaked = false;

                }
                desync = false;
            }
        }
    }

    @Override
    public void onDisable() {
        target = null;
        super.onDisable();
    }
}
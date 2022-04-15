package zxc.rich.client.features;

import net.minecraft.client.Minecraft;
import zxc.rich.client.features.combat.*;
import zxc.rich.client.features.hud.*;
import zxc.rich.client.features.misc.*;
import zxc.rich.client.features.movement.*;
import zxc.rich.client.features.player.*;
import zxc.rich.client.features.visuals.*;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class FeatureDirector {
    public CopyOnWriteArrayList<Feature> features = new CopyOnWriteArrayList<>();

    public FeatureDirector() {

        // COMBAT.
        features.add(new TargetStrafe());
        features.add(new ShieldDesync());
        features.add(new AutoPotion());
        features.add(new AutoTotem());
        features.add(new AutoArmor());
        features.add(new AutoGApple());
        features.add(new AntiCrystal());
        features.add(new TriggerBot());
        features.add(new KillAura());
        features.add(new Velocity());
        features.add(new FastBow());
        features.add(new AntiBot());
        features.add(new HitBox());
        features.add(new Reach());
        // MOVEMENT.
        features.add(new WaterSpeed());
        features.add(new AutoParkour());
        features.add(new AirJump());
        features.add(new Flight());
        features.add(new WaterLeave());
        features.add(new Sprint());
        features.add(new Spider());
        features.add(new HighJump());
        features.add(new Jesus());
        features.add(new NoWeb());
        features.add(new NoClip());
        features.add(new Strafe());
        features.add(new Timer());
        features.add(new Speed());
        // VISUALS.
        features.add(new EnchantmentColor());
        features.add(new SwingAnimations());
        features.add(new DamageParticles());
        features.add(new Scoreboard());
        features.add(new FullBright());
        features.add(new CustomModel());
        features.add(new ViewModel());
        features.add(new EntityESP());
        features.add(new TargetESP());
        features.add(new HitColor());
        features.add(new PearlESP());
        features.add(new NameTags());
        features.add(new BlockESP());
        features.add(new ItemESP());
        features.add(new ChinaHat());
        features.add(new NoRender());
        features.add(new FogColor());
        features.add(new Tracers());
        features.add(new Trails());
        features.add(new Chams());
        features.add(new Snow());
        features.add(new XRay());
        // PLAYER.
        features.add(new MiddleClickPearl());
        features.add(new NoServerRotations());
        features.add(new DeathCoordinates());
        features.add(new AutoTPAccept());
        features.add(new ChestStealer());
        features.add(new AntiAFK());
        features.add(new SpeedMine());
        features.add(new BedrockClip());
        features.add(new NoInteract());
        features.add(new ItemScroller());
        features.add(new NoSlowDown());
        features.add(new AutoFish());
        features.add(new FreeCam());
        features.add(new NoDelay());
        features.add(new GuiWalk());
        features.add(new NoPush());
        // MISC.
        features.add(new MiddleClickFriend());
        features.add(new ModuleSoundAlert());
        features.add(new FlagDetector());
        features.add(new FastWorldLoad());
        features.add(new NameProtect());
        features.add(new BetterChat());
        features.add(new Disabler());
        // HUD.
        features.add(new Notifications());
        features.add(new FeatureList());
        features.add(new ClientFont());
        features.add(new InfoDisplay());
        features.add(new WaterMark());
        features.add(new ClickGUI());
        try {
            features.sort(Comparator.comparingInt(m -> Minecraft.getMinecraft().neverlose500_15.getStringWidth(((Feature) m).getLabel())).reversed());
        } catch (Exception ignored) {
        }
    }

    public List<Feature> getAllFeatures() {
        return this.features;
    }

    public Feature getFeature(Class<? extends Feature> classFeature) {
        for (Feature feature : getAllFeatures()) {
            if (feature != null) {
                if (feature.getClass() == classFeature) {
                    return feature;
                }
            }
        }
        return null;
    }

    public List<Feature> getFeaturesForCategory(FeatureCategory category) {
        List<Feature> featureList = new ArrayList<>();
        for (Feature feature : getAllFeatures()) {
            if (feature.getCategory() == category) {
                featureList.add(feature);
            }
        }
        return featureList;
    }

    public Feature getFeatureByLabel(String name) {
        for (Feature feature : getAllFeatures()) {
            if (feature.getLabel().equals(name)) {
                return feature;
            }
        }
        return null;
    }

}

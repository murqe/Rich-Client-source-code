package zxc.rich.client.features.visuals;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;
import zxc.rich.Main;
import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.EventTransformSideFirstPerson;
import zxc.rich.api.event.events.impl.EventUpdate;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.features.combat.KillAura;
import zxc.rich.client.ui.settings.impl.BooleanSetting;
import zxc.rich.client.ui.settings.impl.ListSetting;
import zxc.rich.client.ui.settings.impl.NumberSetting;

public class SwingAnimations extends Feature {
    public static boolean blocking;
    public static ListSetting swordAnim;
    public static BooleanSetting auraOnly;
    public static BooleanSetting item360;
    public static NumberSetting item360Speed;
    public static ListSetting item360Mode;
    public static ListSetting item360Hand;


    public static NumberSetting swingSpeed;
    public static NumberSetting spinSpeed;
    public static NumberSetting fapSmooth;

    public SwingAnimations() {
        super("SwingAnimations", "Добавляет анимацию на меч", FeatureCategory.VISUALS);
        swordAnim = new ListSetting("Blocking Animation Mode", "Astolfo", () -> true, "Astolfo", "Swipe", "Rich", "Spin", "Fap", "Big","New","Glide");
        auraOnly = new BooleanSetting("Aura Only", false, () -> true);
        swingSpeed = new NumberSetting("Swing Speed", "Скорость удара меча", 8.0F, 1.0F, 20.0F, 1, () -> true);
        spinSpeed = new NumberSetting("Spin Speed", 4, 1, 10, 1, () -> swordAnim.currentMode.equals("Astolfo") || swordAnim.currentMode.equals("Spin"));
        fapSmooth = new NumberSetting("Fap Smooth", 4, 0.5f, 10, 0.5f, () -> swordAnim.currentMode.equals("Fap"));
        item360 = new BooleanSetting("Item360", false, () -> true);
        item360Mode = new ListSetting("Item360 Mode", "Horizontal", () -> item360.getBoolValue(), "Horizontal", "Vertical", "Zoom");
        item360Hand = new ListSetting("Item360 Hand", "All", () -> item360.getBoolValue(), "All", "Left", "Right");
        item360Speed = new NumberSetting("Item360 Speed", 8, 1, 15, 1, () -> item360.getBoolValue());
        addSettings(auraOnly, swordAnim, spinSpeed, fapSmooth, swingSpeed, item360, item360Mode, item360Hand, item360Speed);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        String anim = swordAnim.getOptions();
        blocking = Main.instance.featureDirector.getFeature(KillAura.class).isEnabled() && KillAura.target != null;
        this.setSuffix(anim);

    }

    @EventTarget
    public void onSidePerson(EventTransformSideFirstPerson event) {
        if (blocking) {
            if (event.getEnumHandSide() == EnumHandSide.RIGHT) {
                GlStateManager.translate(0.29, 0.10, -0.31);
            }
        }
    }
}
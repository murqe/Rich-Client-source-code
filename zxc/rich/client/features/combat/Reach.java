package zxc.rich.client.features.combat;

import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.EventUpdate;
import zxc.rich.api.utils.math.MathematicHelper;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.ui.settings.impl.NumberSetting;

public class Reach extends Feature {

    public static NumberSetting reachValue;

    public Reach() {
        super("Reach", "Увеличивает дистанцию удара", FeatureCategory.COMBAT);
        reachValue = new NumberSetting("Reach Distance", 3.2F, 3, 5, 0.1F, () -> true);
        addSettings(reachValue);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        this.setSuffix("" + MathematicHelper.round(reachValue.getNumberValue(), 1));
    }
}
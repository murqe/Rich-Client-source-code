package zxc.rich.client.features.combat;

import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.EventUpdate;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.ui.settings.impl.NumberSetting;

public class HitBox extends Feature {
    public static NumberSetting hitboxsize = new NumberSetting("Size", "Размер хитбокса", 0.2f, 0.1f, 3, 0.1f, () -> true);

    public HitBox() {
        super("HitBox", "Увеличивает хитбокс у ентити", FeatureCategory.COMBAT);
        addSettings(hitboxsize);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        this.setSuffix("" + hitboxsize.getNumberValue());
    }
}

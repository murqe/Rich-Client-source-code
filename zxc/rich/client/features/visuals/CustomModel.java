package zxc.rich.client.features.visuals;

import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.ui.settings.impl.BooleanSetting;

public class CustomModel extends Feature {
     public CustomModel() {
        super("CustomModel", "Изменяет модель игрока",  FeatureCategory.VISUALS);
        addSettings();
    }
}

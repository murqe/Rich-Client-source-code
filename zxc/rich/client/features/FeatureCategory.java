package zxc.rich.client.features;

import java.awt.*;

public enum FeatureCategory {
    COMBAT("Combat"),
    MOVEMENT("Movement"),
    VISUALS("Visual"),
    PLAYER("Player"),
    MISC("Misc"),
    HUD("Hud");
    private final String displayName;

    FeatureCategory(final String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}

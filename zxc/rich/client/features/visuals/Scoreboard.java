package zxc.rich.client.features.visuals;

import net.minecraft.client.renderer.GlStateManager;
import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.EventRenderScoreboard;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.ui.settings.impl.BooleanSetting;
import zxc.rich.client.ui.settings.impl.NumberSetting;

public class Scoreboard extends Feature {

    public static BooleanSetting noScore;
    public NumberSetting x;
    public NumberSetting y;

    public Scoreboard() {
        super("Scoreboard", "Позволяет настроить скорборд на сервере", FeatureCategory.VISUALS);
        noScore = new BooleanSetting("No Scoreboard", false, () -> true);
        x = new NumberSetting("Scoreboard X", 0, -1000, 1000, 1, () -> !noScore.getBoolValue());
        y = new NumberSetting("Scoreboard Y", 0, -500, 500, 1, () -> !noScore.getBoolValue());
        addSettings(noScore, x, y);
    }

    @EventTarget
    public void onRenderScoreboard(EventRenderScoreboard event) {
        if (event.isPre()) {
            GlStateManager.translate(-x.getNumberValue(), y.getNumberValue(), 12);
        } else {
            GlStateManager.translate(x.getNumberValue(), -y.getNumberValue(), 12);
        }
    }
}

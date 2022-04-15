package zxc.rich.client.features.player;

import net.minecraft.client.gui.GuiGameOver;
import zxc.rich.Main;
import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.EventUpdate;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.ui.notification.NotificationMode;
import zxc.rich.client.ui.notification.NotificationRenderer;

public class DeathCoordinates extends Feature {

    public DeathCoordinates() {
        super("DeathCoordinates", "ѕосле смерти пишит ваши координаты в чат",  FeatureCategory.PLAYER);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.player.getHealth() < 1 && mc.currentScreen instanceof GuiGameOver) {
            int x = mc.player.getPosition().getX();
            int y = mc.player.getPosition().getY();
            int z = mc.player.getPosition().getZ();
            if (mc.player.ticksExisted % 20 == 0) {
                NotificationRenderer.queue("Death Coordinates", "X: " + x + " Y: " + y + " Z: " + z, 3, NotificationMode.INFO);
                Main.msg("Death Coordinates: " + "X: " + x + " Y: " + y + " Z: " + z, true);
            }
        }
    }
}
package zxc.rich.client.features.combat;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.EventPreMotionUpdate;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.ui.settings.impl.BooleanSetting;
import zxc.rich.client.ui.settings.impl.ListSetting;

import java.util.ArrayList;
import java.util.List;

public class AntiBot extends Feature {
    public static List<Entity> isBotPlayer = new ArrayList<>();
    public ListSetting antiBotMode = new ListSetting("Anti Bot Mode", "Matrix", () -> true, "Matrix", "Reflex");
    public BooleanSetting invisIgnore = new BooleanSetting("Invisible Ignore", "Игнорирует невидимых сущностей", false, () -> true);

    public AntiBot() {
        super("AntiBot", "Удаляет ботов созданных анти-читом", FeatureCategory.COMBAT);
        addSettings(antiBotMode, invisIgnore);
    }

    @EventTarget
    public void onPreMotion(EventPreMotionUpdate event) {
        for (Entity entity : mc.world.loadedEntityList) {
            switch (antiBotMode.getOptions()) {
                case "Matrix":
                    if (entity != mc.player && entity.ticksExisted < 5 && entity instanceof EntityOtherPlayerMP) {
                        if (((EntityOtherPlayerMP) entity).hurtTime > 0 && mc.player.getDistanceToEntity(entity) <= 25 && mc.player.connection.getPlayerInfo(entity.getUniqueID()).getResponseTime() != 0) {
                            isBotPlayer.add(entity);
                        }
                    }
                    break;
                case "Reflex":
                    if (entity.getDisplayName().getUnformattedText().length() == 8 && mc.player.posY < entity.posY && entity.ticksExisted == 1 && !entity.isCollidedVertically && !entity.isEntityInsideOpaqueBlock() && entity.fallDistance == 0 && !(entity.posX == 0) && !(entity.posZ == 0)) {
                        isBotPlayer.add(entity);
                        break;
                    }
                    if (invisIgnore.getBoolValue() && entity.isInvisible() && entity != mc.player) {
                        isBotPlayer.add(entity);
                    }
            }
        }
    }

}

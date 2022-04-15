package zxc.rich.client.features.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.EntityLivingBase;
import zxc.rich.Main;
import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.EventMouse;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.friend.Friend;
import zxc.rich.client.ui.notification.NotificationMode;
import zxc.rich.client.ui.notification.NotificationRenderer;

public class MiddleClickFriend extends Feature {

    EntityLivingBase friend;

    public MiddleClickFriend() {
        super("MiddleClickFriend", "Добавляет игрока в френд лист при нажатии на кнопку мыши", FeatureCategory.MISC);
    }

    @EventTarget
    public void onMouseEvent(EventMouse event) {
        if (event.getKey() == 2 && mc.pointedEntity instanceof EntityLivingBase) {
            if (Main.instance.friendManager.getFriends().stream().anyMatch(friend -> friend.getName().equals(mc.pointedEntity.getName()))) {
                Main.instance.friendManager.getFriends().remove(Main.instance.friendManager.getFriend(mc.pointedEntity.getName()));
                Main.msg(ChatFormatting.RED + "Removed " + ChatFormatting.RESET + "'" + mc.pointedEntity.getName() + "'" + " as Friend!", true);
                NotificationRenderer.queue("", "Removed " + "'" + mc.pointedEntity.getName() + "'" + " as Friend!", 4,NotificationMode.INFO);
            } else {
                Main.instance.friendManager.addFriend(new Friend(mc.pointedEntity.getName()));
                Main.msg(ChatFormatting.GREEN + "Added " + ChatFormatting.RESET + "'" + mc.pointedEntity.getName() + "'" + " as Friend!", true);
                NotificationRenderer.queue("", "Added " + mc.pointedEntity.getName() + " as Friend!",4, NotificationMode.SUCCESS);
            }
        }
    }
}
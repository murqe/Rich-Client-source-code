package zxc.rich.api.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import zxc.rich.Main;
import zxc.rich.api.command.CommandAbstract;
import zxc.rich.client.ui.notification.NotificationMode;
import zxc.rich.client.ui.notification.NotificationRenderer;

public class FriendCommand extends CommandAbstract {
    Minecraft mc = Minecraft.getMinecraft();

    public FriendCommand() {
        super("friend", "friend list", "§6.friend" + ChatFormatting.RED + " add " + "§7<nickname> | §6.friend" + ChatFormatting.RED + " del " + "§7<nickname> | §6.friend" + ChatFormatting.RED + " list " + "| §6.friend" + ChatFormatting.RED + " clear", "friend");
    }

    @Override
    public void execute(String... arguments) {
        try {
            if (arguments.length > 1) {
                if (arguments[0].equalsIgnoreCase("friend")) {
                    if (arguments[1].equalsIgnoreCase("add")) {
                        String name = arguments[2];
                        if (name.equals(mc.player.getName())) {
                            Main.msg(ChatFormatting.RED + "You can't add yourself!", true);
                            NotificationRenderer.queue("Friend Manager", "You can't add yourself!", 1, NotificationMode.WARNING);
                            return;
                        }
                        if (!Main.instance.friendManager.isFriend(name)) {
                            Main.instance.friendManager.addFriend(name);
                            Main.msg("Friend " + ChatFormatting.GREEN + name + ChatFormatting.WHITE + " successfully added to your friend list!", true);
                            NotificationRenderer.queue("Friend Manager", "Friend " + ChatFormatting.RED + name + ChatFormatting.WHITE + " deleted from your friend list!", 1, NotificationMode.SUCCESS);
                        }
                    }
                    if (arguments[1].equalsIgnoreCase("del")) {
                        String name = arguments[2];
                        if (Main.instance.friendManager.isFriend(name)) {
                            Main.instance.friendManager.removeFriend(name);
                            Main.msg("Friend " + ChatFormatting.RED + name + ChatFormatting.WHITE + " deleted from your friend list!", true);
                            NotificationRenderer.queue("Friend Manager", "Friend " + ChatFormatting.RED + name + ChatFormatting.WHITE + " deleted from your friend list!", 1, NotificationMode.SUCCESS);
                        }
                    }
                    if (arguments[1].equalsIgnoreCase("clear")) {
                        if (Main.instance.friendManager.getFriends().isEmpty()) {
                            Main.msg(ChatFormatting.RED + "Your friend list is empty!", true);
                            NotificationRenderer.queue("Friend Manager", "Your friend list is empty!", 1, NotificationMode.WARNING);
                            return;
                        }
                        Main.instance.friendManager.getFriends().clear();
                        Main.msg("Your " + ChatFormatting.GREEN + "friend list " + ChatFormatting.WHITE + "was cleared!", true);
                        NotificationRenderer.queue("Friend Manager", "Your " + ChatFormatting.GREEN + "friend list " + ChatFormatting.WHITE + "was cleared!", 1, NotificationMode.SUCCESS);
                    }
                    if (arguments[1].equalsIgnoreCase("list")) {
                        if (Main.instance.friendManager.getFriends().isEmpty()) {
                            Main.msg(ChatFormatting.RED + "Your friend list is empty!", true);
                            NotificationRenderer.queue("Friend Manager", "Your friend list is empty!", 1, NotificationMode.WARNING);
                            return;
                        }
                        Main.instance.friendManager.getFriends().forEach(friend -> Main.msg(ChatFormatting.GREEN + "Friend list: " + ChatFormatting.RED + friend.getName(), true));
                    }
                }
            } else {
                Main.msg(getUsage(), true);
            }
        } catch (Exception e) {
            Main.msg("§cNo, no, no. Usage: " + getUsage(), true);
        }
    }
}

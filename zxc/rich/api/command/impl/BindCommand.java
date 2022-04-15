package zxc.rich.api.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;

import zxc.rich.Main;
import zxc.rich.api.command.CommandAbstract;
import zxc.rich.client.features.Feature;
import zxc.rich.client.ui.notification.NotificationMode;
import zxc.rich.client.ui.notification.NotificationRenderer;

import javax.xml.soap.Text;

public class BindCommand extends CommandAbstract {

    public BindCommand() {
        super("bind", "bind", "§6.bind" + ChatFormatting.RED + " add " + "§7<name> §7<key> " + TextFormatting.RED + "\n" + "[" + TextFormatting.RED + "Rich Premium" + TextFormatting.GRAY + "]: " + "§6.bind " + ChatFormatting.RED + "remove " + "§7<name> §7<key> " + "\n" + "[" + TextFormatting.RED + "Rich Premium" + TextFormatting.GRAY + "]  " + "§6.bind " + ChatFormatting.RED + "list ", "§6.bind" + ChatFormatting.RED + " add " + "§7<name> §7<key> | §6.bind" + ChatFormatting.RED + "remove " + "§7<name> <key> | §6.bind" + ChatFormatting.RED + "clear " + "/" + " §6.bind " + ChatFormatting.RED + "list ", "bind");
    }

    @Override
    public void execute(String... arguments) {
        try {
            if (arguments.length == 4) {
                String moduleName = arguments[2];
                String bind = arguments[3].toUpperCase();
                Feature feature = Main.instance.featureDirector.getFeatureByLabel(moduleName);
                if (arguments[0].equalsIgnoreCase("bind") && arguments[1].equalsIgnoreCase("add")) {
                    feature.setKey(Keyboard.getKeyIndex(bind));
                    Main.msg(ChatFormatting.GREEN + feature.getLabel() + ChatFormatting.WHITE + " was set on key " + ChatFormatting.RED + "\"" + bind + "\"", true);
                    NotificationRenderer.queue("Bind Manager", ChatFormatting.GREEN + feature.getLabel() + ChatFormatting.WHITE + " was set on key " + ChatFormatting.RED + "\"" + bind + "\"", 1, NotificationMode.SUCCESS);

                } else if (arguments[0].equalsIgnoreCase("bind") && arguments[1].equalsIgnoreCase("remove")) {
                    feature.setKey(0);
                    Main.msg(ChatFormatting.GREEN + feature.getLabel() + ChatFormatting.WHITE + " bind was deleted from key " + ChatFormatting.RED + "\"" + bind + "\"", true);
                    NotificationRenderer.queue("Bind Manager", ChatFormatting.GREEN + feature.getLabel() + ChatFormatting.WHITE + " bind was deleted from key " + ChatFormatting.RED + "\"" + bind + "\"", 1, NotificationMode.SUCCESS);
                }
            } else if (arguments.length == 2) {
                if (arguments[0].equalsIgnoreCase("bind") && arguments[1].equalsIgnoreCase("list")) {
                    for (Feature f : Main.instance.featureDirector.getAllFeatures()) {
                        if (f.getKey() != 0) {
                            Main.msg(f.getLabel() + " : " + Keyboard.getKeyName(f.getKey()), true);
                        }

                    }
                } else {
                    Main.msg(this.getUsage(), true);
                }

            } else if (arguments[0].equalsIgnoreCase("bind")) {
                Main.msg(this.getUsage(), true);
            }

        } catch (Exception ignored) {

        }
    }
}
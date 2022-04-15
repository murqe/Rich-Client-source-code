package zxc.rich.api.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;

import zxc.rich.Main;
import zxc.rich.api.command.CommandAbstract;

public class HelpCommand extends CommandAbstract {

    public HelpCommand() {
        super("help", "help", ".help", "help");
    }

    @Override
    public void execute(String... args) {
        if (args.length == 1) {
            if (args[0].equals("help")) {
                Main.msg(ChatFormatting.AQUA + "All Commands:", true);
                Main.msg(ChatFormatting.WHITE + ".bind -> (Позволяет забиндить команду)", true);
                Main.msg(ChatFormatting.WHITE + ".macro -> (Позволяет отправить команду по нажатию кнопки)", true);
                Main.msg(ChatFormatting.WHITE + ".panic -> (Позволяет отключить все модули чита)", true);
                Main.msg(ChatFormatting.WHITE + ".vclip -> (Позволяет телепортироваться по вертикали)", true);
                Main.msg(ChatFormatting.WHITE + ".hclip -> (Позволяет телепортироваться по горизонтали)", true);

            }
        } else {
            Main.msg(this.getUsage(), true);
        }
    }
}

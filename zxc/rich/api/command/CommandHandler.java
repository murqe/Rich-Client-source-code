package zxc.rich.api.command;


import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.EventMessage;

public class CommandHandler {

    public CommandManager commandManager;

    public CommandHandler(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @EventTarget
    public void onMessage(EventMessage event) {
        String msg = event.getMessage();
        if (msg.length() > 0 && msg.startsWith(".")) {
            event.setCancelled(this.commandManager.execute(msg));
        }
    }
}

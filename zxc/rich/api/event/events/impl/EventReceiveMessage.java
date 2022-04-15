package zxc.rich.api.event.events.impl;

import zxc.rich.api.event.events.callables.EventCancellable;

import java.awt.*;

public class EventReceiveMessage extends EventCancellable {

    public String message;
    public boolean cancelled;

    public EventReceiveMessage(String chat) {
        message = chat;
    }

    public String getMessage() {
        return message;
    }

    public void setCancelled(boolean b) {
        this.cancelled = b;
    }
}
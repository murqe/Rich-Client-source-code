package zxc.rich.api.event.events.impl;

import zxc.rich.api.event.events.Event;
import zxc.rich.api.event.events.callables.EventCancellable;

public class EventMessage extends EventCancellable implements Event {

    public String message;

    public EventMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}

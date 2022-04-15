package zxc.rich.api.event.events.callables;


import zxc.rich.api.event.events.Cancellable;
import zxc.rich.api.event.events.Event;

public abstract class EventCancellable implements Event, Cancellable {

    private boolean cancelled;

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean state) {
        cancelled = state;
    }

}

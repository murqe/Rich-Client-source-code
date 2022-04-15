package zxc.rich.api.event.events;

public interface Cancellable {

    boolean isCancelled();

    void setCancelled(boolean state);

}

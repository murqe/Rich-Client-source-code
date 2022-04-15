package zxc.rich.api.event.events.impl;


import zxc.rich.api.event.events.Event;

public class EventKey implements Event {
    private final int key;

    public EventKey(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }
}

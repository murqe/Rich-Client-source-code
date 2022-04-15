package zxc.rich.api.event.events.impl;


import zxc.rich.api.event.events.Event;

public class Event3D implements Event {
    private final float partialTicks;

    public Event3D(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}

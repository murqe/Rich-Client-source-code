package zxc.rich.api.event.events.impl;
import net.minecraft.client.gui.ScaledResolution;
import zxc.rich.api.event.events.Event;

public class Event2D implements Event {
    private final ScaledResolution resolution;

    public Event2D(ScaledResolution resolution) {
        this.resolution = resolution;
    }

    public ScaledResolution getResolution() {
        return resolution;
    }

}

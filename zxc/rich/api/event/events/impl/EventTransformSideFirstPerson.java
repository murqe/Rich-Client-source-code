package zxc.rich.api.event.events.impl;

import net.minecraft.util.EnumHandSide;
import zxc.rich.api.event.events.Event;

public class EventTransformSideFirstPerson implements Event {

    private final EnumHandSide enumHandSide;

    public EventTransformSideFirstPerson(EnumHandSide enumHandSide) {
        this.enumHandSide = enumHandSide;
    }

    public EnumHandSide getEnumHandSide() {
        return this.enumHandSide;
    }
}

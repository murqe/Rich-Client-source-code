package zxc.rich.api.event.events.impl;

import net.minecraft.network.Packet;
import zxc.rich.api.event.events.callables.EventCancellable;

public class EventSendPacket extends EventCancellable {

    private final Packet<?> packet;

    public EventSendPacket(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return this.packet;
    }
}

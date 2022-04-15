package zxc.rich.api.event.events.impl;

import net.minecraft.network.Packet;
import zxc.rich.api.event.events.Event;
import zxc.rich.api.event.events.callables.EventCancellable;

public class EventReceivePacket extends EventCancellable {
    private Packet packet;

    public EventReceivePacket(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return packet;
    }
    public void setPacket(Packet packet) {
        this.packet = packet;
    }
}

package zxc.rich.api.event.events.impl;

import net.minecraft.entity.Entity;
import zxc.rich.api.event.events.Event;
import zxc.rich.api.event.events.callables.EventCancellable;

public class EventAttackSilent extends EventCancellable {

    private final Entity targetEntity;

    public EventAttackSilent(Entity targetEntity) {
        this.targetEntity = targetEntity;
    }

    public Entity getTargetEntity() {
        return this.targetEntity;
    }
}

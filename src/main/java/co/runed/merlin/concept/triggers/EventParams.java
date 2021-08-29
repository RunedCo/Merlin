package co.runed.merlin.concept.triggers;

import org.bukkit.event.Event;

public abstract class EventParams<T extends Event> {
    T baseEvent;

    public EventParams(T baseEvent) {
        this.baseEvent = baseEvent;
    }

    public T getBaseEvent() {
        return baseEvent;
    }
}

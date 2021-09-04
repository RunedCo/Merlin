package co.runed.merlin.concept.triggers;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public abstract class EventParams<T extends Event> implements Params {
    T baseEvent;
    boolean cancelled = false;

    public EventParams(T baseEvent) {
        this.baseEvent = baseEvent;
    }

    public T getBaseEvent() {
        return baseEvent;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        if (baseEvent instanceof Cancellable cancellable) {
            cancellable.setCancelled(cancelled);
        }

        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        if (baseEvent instanceof Cancellable cancellable) {
            return cancellable.isCancelled();
        }

        return cancelled;
    }
}

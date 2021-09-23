package co.runed.merlin.concept.triggers;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public class EventTrigger<T extends Event> extends BaseTrigger {
    private T baseEvent;

    public EventTrigger(T baseEvent) {
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

        super.setCancelled(cancelled);
    }

    @Override
    public boolean isCancelled() {
        if (baseEvent instanceof Cancellable cancellable) {
            return cancellable.isCancelled();
        }

        return super.isCancelled();
    }
}

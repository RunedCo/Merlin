package co.runed.merlin.triggers.lifecycle;

import co.runed.merlin.triggers.EventTrigger;
import org.bukkit.event.Event;

public class SpawnTrigger extends EventTrigger<Event> {
    public SpawnTrigger(Event baseEvent) {
        super(baseEvent);
    }
}

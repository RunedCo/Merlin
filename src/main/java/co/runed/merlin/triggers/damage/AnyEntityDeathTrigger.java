package co.runed.merlin.triggers.damage;

import org.bukkit.event.entity.EntityDeathEvent;

public class AnyEntityDeathTrigger extends AbstractDeathTrigger {
    public AnyEntityDeathTrigger(EntityDeathEvent baseEvent) {
        super(baseEvent);
    }
}

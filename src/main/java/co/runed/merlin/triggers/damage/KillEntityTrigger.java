package co.runed.merlin.triggers.damage;

import org.bukkit.event.entity.EntityDeathEvent;

public class KillEntityTrigger extends AbstractDeathTrigger {
    public KillEntityTrigger(EntityDeathEvent baseEvent) {
        super(baseEvent);
    }
}

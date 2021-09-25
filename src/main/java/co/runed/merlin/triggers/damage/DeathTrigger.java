package co.runed.merlin.triggers.damage;

import org.bukkit.event.entity.EntityDeathEvent;

public class DeathTrigger extends AbstractDeathTrigger {
    public DeathTrigger(EntityDeathEvent baseEvent) {
        super(baseEvent);
    }
}

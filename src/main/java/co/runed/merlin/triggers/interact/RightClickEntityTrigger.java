package co.runed.merlin.triggers.interact;

import co.runed.merlin.triggers.EventTrigger;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class RightClickEntityTrigger extends EventTrigger<PlayerInteractAtEntityEvent> {
    public RightClickEntityTrigger(PlayerInteractAtEntityEvent baseEvent) {
        super(baseEvent);
    }

    public Entity getTarget() {
        return getBaseEvent().getRightClicked();
    }
}

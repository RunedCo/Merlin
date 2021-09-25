package co.runed.merlin.triggers.movement;

import co.runed.merlin.triggers.EventTrigger;
import org.bukkit.event.player.PlayerPortalEvent;

public class EnterPortalTrigger extends EventTrigger<PlayerPortalEvent> {
    public EnterPortalTrigger(PlayerPortalEvent baseEvent) {
        super(baseEvent);
    }
}

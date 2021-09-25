package co.runed.merlin.triggers.lifecycle;

import co.runed.merlin.triggers.EventTrigger;
import org.bukkit.event.player.PlayerQuitEvent;

public class DisconnectTrigger extends EventTrigger<PlayerQuitEvent> {
    public DisconnectTrigger(PlayerQuitEvent baseEvent) {
        super(baseEvent);
    }
}

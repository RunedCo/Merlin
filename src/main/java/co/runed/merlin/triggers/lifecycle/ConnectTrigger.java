package co.runed.merlin.triggers.lifecycle;

import co.runed.merlin.triggers.EventTrigger;
import org.bukkit.event.player.PlayerJoinEvent;

public class ConnectTrigger extends EventTrigger<PlayerJoinEvent> {
    public ConnectTrigger(PlayerJoinEvent baseEvent) {
        super(baseEvent);
    }
}

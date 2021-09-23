package co.runed.merlin.triggers.movement;

import co.runed.merlin.triggers.EventTrigger;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class OnSneakTrigger extends EventTrigger<PlayerToggleSneakEvent> {
    public OnSneakTrigger(PlayerToggleSneakEvent baseEvent) {
        super(baseEvent);
    }

    public boolean isSneaking() {
        return getBaseEvent().isSneaking();
    }
}

package co.runed.merlin.triggers.movement;

import co.runed.merlin.triggers.EventTrigger;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class SneakTrigger extends EventTrigger<PlayerToggleSneakEvent> {
    public SneakTrigger(PlayerToggleSneakEvent baseEvent) {
        super(baseEvent);
    }

    public Player getPlayer() {
        return getBaseEvent().getPlayer();
    }

    public boolean isSneaking() {
        return getBaseEvent().isSneaking();
    }
}

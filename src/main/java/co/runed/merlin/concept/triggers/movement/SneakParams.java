package co.runed.merlin.concept.triggers.movement;

import co.runed.merlin.concept.triggers.EventParams;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class SneakParams extends EventParams<PlayerToggleSneakEvent> {
    public SneakParams(PlayerToggleSneakEvent baseEvent) {
        super(baseEvent);
    }

    public boolean isSneaking() {
        return getBaseEvent().isSneaking();
    }
}

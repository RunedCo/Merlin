package co.runed.merlin.triggers.item;

import co.runed.merlin.triggers.EventTrigger;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FishHook;
import org.bukkit.event.player.PlayerFishEvent;

public class CatchFishTrigger extends EventTrigger<PlayerFishEvent> {
    public CatchFishTrigger(PlayerFishEvent baseEvent) {
        super(baseEvent);
    }

    public FishHook getHook() {
        return getBaseEvent().getHook();
    }

    public Entity getCaught() {
        return getBaseEvent().getCaught();
    }

    public PlayerFishEvent.State getState() {
        return getBaseEvent().getState();
    }
}

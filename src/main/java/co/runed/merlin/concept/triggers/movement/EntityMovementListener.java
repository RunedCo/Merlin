package co.runed.merlin.concept.triggers.movement;

import co.runed.merlin.concept.spells.SpellManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class EntityMovementListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    private void onPlayerSneak(PlayerToggleSneakEvent event) {
        var player = event.getPlayer();

        var params = new SneakParams(event);

        SpellManager.getInstance().run(player, SneakTrigger.class, (sneak, ctx) -> sneak.onSneak(ctx.setParams(params), params));
    }
}

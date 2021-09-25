package co.runed.merlin.triggers.item;

import co.runed.merlin.core.SpellManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

/**
 * Event that triggers casting an ability when an item is consumed (food, potions)
 */
public class PlayerEatListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    private void onPlayerEat(PlayerItemConsumeEvent event) {
        var player = event.getPlayer();
        var stack = event.getItem();

        SpellManager.getInstance().run(player, new ConsumeItemTrigger(event, stack));
    }
}

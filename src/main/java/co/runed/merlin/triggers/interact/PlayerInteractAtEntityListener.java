package co.runed.merlin.triggers.interact;

import co.runed.merlin.core.SpellManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class PlayerInteractAtEntityListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteractAtEntity(PlayerInteractAtEntityEvent event) {
        var player = event.getPlayer();

        SpellManager.getInstance().run(player, new RightClickEntityTrigger(event));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onLeftClickEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;

        var inv = player.getEquipment();
        var stack = inv.getItemInMainHand();

        SpellManager.getInstance().run(player, new LeftClickEntityTrigger(event, stack));
    }
}

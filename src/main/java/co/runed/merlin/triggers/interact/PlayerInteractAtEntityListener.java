package co.runed.merlin.triggers.interact;

import co.runed.bolster.damage.DamageType;
import co.runed.bolster.events.entity.EntityDamageInfoEvent;
import co.runed.merlin.core.SpellManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class PlayerInteractAtEntityListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteractAtEntity(PlayerInteractAtEntityEvent event) {
        var player = event.getPlayer();

        SpellManager.getInstance().run(player, new RightClickEntityTrigger(event));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onLeftClickEntity(EntityDamageInfoEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        if (event.getDamageInfo().getDamageType() != DamageType.PRIMARY) return;

        var inv = player.getEquipment();
        var stack = inv.getItemInMainHand();

        SpellManager.getInstance().run(player, new LeftClickEntityTrigger(event, stack));
    }
}

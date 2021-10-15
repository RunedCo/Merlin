package co.runed.merlin.core;

import co.runed.bolster.damage.DamageInfo;
import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.events.entity.EntityDamageInfoEvent;
import co.runed.merlin.items.ItemDefinition;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class SpellListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    private void onDamage(EntityDamageInfoEvent event) {
        var info = event.getDamageInfo();

        if (!(info instanceof DamageInfo.EventDamageInfo) || !(info.getDamageSource() instanceof BolsterEntity entity)) return;

        var mainItem = entity.getEntity().getEquipment().getItemInMainHand();
        var definition = ItemDefinition.from(mainItem);

        if (definition == null) return;

        var provider = SpellManager.getInstance().getProvider(entity.getEntity(), definition);

        if (provider == null || !provider.isEnabled()) return;

        info.withSource(provider);
    }
}

package co.runed.merlin.triggers.damage;

import co.runed.bolster.events.entity.EntityDamageInfoEvent;
import org.bukkit.inventory.ItemStack;

public class TakeDamageTrigger extends AbstractDamageTrigger {
    public TakeDamageTrigger(EntityDamageInfoEvent baseEvent, ItemStack itemStack) {
        super(baseEvent, itemStack);
    }
}

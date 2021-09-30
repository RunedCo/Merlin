package co.runed.merlin.triggers.damage;

import co.runed.bolster.events.entity.EntityDamageInfoEvent;
import org.bukkit.inventory.ItemStack;

public class DamageTrigger extends AbstractDamageTrigger {
    public DamageTrigger(EntityDamageInfoEvent baseEvent, ItemStack itemStack) {
        super(baseEvent, itemStack);
    }
}

package co.runed.merlin.triggers.damage;

import co.runed.bolster.events.entity.EntityDamageInfoEvent;
import org.bukkit.inventory.ItemStack;

public class FatalDamageTrigger extends AbstractDamageTrigger {
    public FatalDamageTrigger(EntityDamageInfoEvent baseEvent, ItemStack itemStack) {
        super(baseEvent, itemStack);
    }
}

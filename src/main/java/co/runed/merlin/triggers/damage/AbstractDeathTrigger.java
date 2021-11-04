package co.runed.merlin.triggers.damage;

import co.runed.bolster.damage.DamageInfo;
import co.runed.bolster.wip.DamageListener;
import co.runed.merlin.triggers.EventTrigger;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class AbstractDeathTrigger extends EventTrigger<EntityDeathEvent> {
    public AbstractDeathTrigger(EntityDeathEvent baseEvent) {
        super(baseEvent);
    }

    public LivingEntity getTarget() {
        return getBaseEvent().getEntity();
    }

    public List<ItemStack> getDrops() {
        return getBaseEvent().getDrops();
    }

    public Player getKiller() {
        return getTarget().getKiller();
    }

    public DamageInfo getDamageInfo() {
        if (getKiller() == null) return null;

        return DamageListener.getInstance().getLastDamageInfo(getKiller());
    }
}

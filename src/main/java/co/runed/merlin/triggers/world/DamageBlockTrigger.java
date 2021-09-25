package co.runed.merlin.triggers.world;

import org.bukkit.event.block.BlockDamageEvent;

public class DamageBlockTrigger extends AbstractBlockTrigger<BlockDamageEvent> {
    public DamageBlockTrigger(BlockDamageEvent baseEvent) {
        super(baseEvent);
    }
}

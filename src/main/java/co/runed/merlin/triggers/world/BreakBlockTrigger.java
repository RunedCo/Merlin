package co.runed.merlin.triggers.world;

import org.bukkit.event.block.BlockBreakEvent;

public class BreakBlockTrigger extends AbstractBlockTrigger<BlockBreakEvent> {
    public BreakBlockTrigger(BlockBreakEvent baseEvent) {
        super(baseEvent);
    }
}

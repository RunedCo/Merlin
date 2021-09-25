package co.runed.merlin.triggers.world;

import co.runed.merlin.triggers.EventTrigger;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockEvent;

public abstract class AbstractBlockTrigger<T extends BlockEvent> extends EventTrigger<T> {
    public AbstractBlockTrigger(T baseEvent) {
        super(baseEvent);
    }

    public World getWorld() {
        return getBlock().getWorld();
    }

    public Block getBlock() {
        return getBaseEvent().getBlock();
    }
}

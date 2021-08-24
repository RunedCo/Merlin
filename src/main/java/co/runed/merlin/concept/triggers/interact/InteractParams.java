package co.runed.merlin.concept.triggers.interact;

import co.runed.merlin.concept.triggers.AbstractItemEventParams;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

public class InteractParams extends AbstractItemEventParams<PlayerInteractEvent> {
    public InteractParams(PlayerInteractEvent event) {
        super(event, event.getItem(), event.getHand());
    }

    public Player getPlayer() {
        return getBaseEvent().getPlayer();
    }

    public Action getAction() {
        return getBaseEvent().getAction();
    }

    public boolean isRightClick() {
        return getAction() == Action.RIGHT_CLICK_AIR || getAction() == Action.RIGHT_CLICK_BLOCK;
    }

    public boolean isLeftClick() {
        return !isRightClick();
    }

    public boolean hasBlock() {
        return getBaseEvent().hasBlock();
    }

    public Block getBlock() {
        return getBaseEvent().getClickedBlock();
    }

    public BlockFace getBlockFace() {
        return getBaseEvent().getBlockFace();
    }

    public Location getInteractionPoint() {
        return getBaseEvent().getInteractionPoint();
    }

    @NotNull
    public Event.Result useInteractedBlock() {
        return getBaseEvent().useInteractedBlock();
    }

    public void setUseInteractedBlock(@NotNull Event.Result useInteractedBlock) {
        getBaseEvent().setUseInteractedBlock(useInteractedBlock);
    }

    @NotNull
    public Event.Result useItemInHand() {
        return getBaseEvent().useItemInHand();
    }

    public void setUseItemInHand(@NotNull Event.Result useItemInHand) {
        getBaseEvent().setUseItemInHand(useItemInHand);
    }
}

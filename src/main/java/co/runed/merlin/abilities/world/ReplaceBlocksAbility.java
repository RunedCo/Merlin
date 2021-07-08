package co.runed.merlin.abilities.world;

import co.runed.bolster.common.util.collection.RandomCollection;
import co.runed.bolster.util.BukkitUtil;
import co.runed.bolster.util.properties.Properties;
import co.runed.merlin.target.Target;
import co.runed.merlin.abilities.Ability;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Collection;

public class ReplaceBlocksAbility extends Ability
{
    Target<Location> target;

    Collection<Material> materialsToReplace;
    RandomCollection<Material> replaceWith;

    int yOffset;
    int range;
    int radiusUp;
    int radiusDown;
    int radiusHorizontal;

    public ReplaceBlocksAbility(Target<Location> target, Collection<Material> materialsToReplace, RandomCollection<Material> replaceWith, int yOffset, int range, int radiusUp, int radiusDown, int radiusHorizontal)
    {
        super();

        this.target = target;

        this.materialsToReplace = materialsToReplace;
        this.replaceWith = replaceWith;

        this.yOffset = yOffset;

        this.range = range;
        this.radiusUp = radiusUp;
        this.radiusDown = radiusDown;
        this.radiusHorizontal = radiusHorizontal;
    }

    @Override
    public void onActivate(Properties properties)
    {
        Location target = this.target.get(properties);
        target.add(0, yOffset, 0);

        for (Block block : BukkitUtil.getBlocksRadius(target, radiusUp, radiusDown, radiusHorizontal))
        {
            for (Material material : this.materialsToReplace)
            {
                // If specific blocks are being replaced, skip if the block isn't replaceable.
                if (!material.equals(block.getType())) continue;
                // If all blocks are being replaced, skip if the block is already replaced.
                if (replaceWith.contains(block.getType())) continue;

                Material next = replaceWith.next();
                BlockData data = block.getBlockData();
                BlockData newBlockData = Bukkit.createBlockData(data.getAsString().replaceAll(block.getType().getKey().getKey(), next.getKey().getKey()));

                block.setType(next);
                block.setBlockData(newBlockData);
                break;
            }
        }
    }

    @Override
    public void loadConfig(ConfigurationSection config)
    {

    }
}

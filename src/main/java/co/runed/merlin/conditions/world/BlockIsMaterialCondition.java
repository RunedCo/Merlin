package co.runed.merlin.conditions.world;

import co.runed.bolster.util.properties.Properties;
import co.runed.merlin.target.Target;
import co.runed.merlin.conditions.Condition;
import co.runed.merlin.conditions.IConditional;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class BlockIsMaterialCondition extends Condition
{
    Target<Block> blockTarget;
    Collection<Material> materials = new ArrayList<>();

    public BlockIsMaterialCondition(Material material)
    {
        this(Collections.singletonList(material));
    }

    public BlockIsMaterialCondition(Tag<Material> tag)
    {
        this(tag.getValues());
    }

    public BlockIsMaterialCondition(Collection<Material> materials)
    {
        this.blockTarget = Target.BLOCK;

        this.materials.addAll(materials);
    }

    @Override
    public boolean evaluate(IConditional conditional, Properties properties)
    {
        Block block = this.blockTarget.get(properties);

        if (block == null) return false;

        return this.materials.contains(block.getType());
    }

    @Override
    public void onFail(IConditional conditional, Properties properties, boolean inverted)
    {

    }

    @Override
    public String getErrorMessage(IConditional conditional, Properties properties, boolean inverted)
    {
        return null;
    }
}

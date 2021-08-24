package co.runed.merlin.concept;

import co.runed.bolster.util.BukkitUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.Collection;
import java.util.stream.Collectors;

public class AOE {
    public static EntityAOE entities(Location location, double radius) {
        return new EntityAOE(location, radius);
    }

    public static LivingEntityAOE livingEntities(Location location, double radius) {
        return new LivingEntityAOE(location, radius);
    }

    public static BlockAOE blocks(Location location, double radius) {
        return new BlockAOE(location, radius);
    }

    public static class EntityAOE extends Target<Entity> {
        Location location;
        double radius;

        public EntityAOE(Location location, double radius) {
            this.location = location;
            this.radius = radius;
        }

        @Override
        Collection<Entity> getTargets() {
            return BukkitUtil.getEntitiesRadius(location, radius);
        }
    }

    public static class LivingEntityAOE extends Target<LivingEntity> {
        Location location;
        double radius;

        public LivingEntityAOE(Location location, double radius) {
            this.location = location;
            this.radius = radius;
        }

        @Override
        Collection<LivingEntity> getTargets() {
            return BukkitUtil.getEntitiesRadius(location, radius)
                    .stream().map(entity -> {
                        if (!(entity instanceof LivingEntity livingEntity)) return null;

                        return livingEntity;
                    }).collect(Collectors.toList());
        }
    }

    public static class BlockAOE extends Target<Block> {
        Location location;
        double radius;

        public BlockAOE(Location location, double radius) {
            this.location = location;
            this.radius = radius;
        }

        @Override
        Collection<Block> getTargets() {
            return BukkitUtil.getBlocksRadius(location, radius, radius, radius);
        }

    }
}

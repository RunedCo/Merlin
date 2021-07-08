package co.runed.merlin.core;

import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.events.entity.EntityDestroyEvent;
import co.runed.bolster.util.Definition;
import co.runed.bolster.util.Manager;
import co.runed.merlin.classes.MerlinClass;
import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class ClassManager extends Manager
{
    //private final Map<UUID, BolsterClass> bolsterClasses = new HashMap<>();

    public static final NamespacedKey CLASS_KEY = new NamespacedKey("bolster", "class");

    private static ClassManager _instance;

    public ClassManager(Plugin plugin)
    {
        super(plugin);

        _instance = this;
    }

    public MerlinClass setClass(LivingEntity entity, Definition<MerlinClass> classDefinition)
    {
        return this.setClass(entity, classDefinition.create());
    }

    /**
     * Sets an entity's {@link MerlinClass}
     *
     * @param entity      the entity
     * @param merlinClass the class
     */
    public MerlinClass setClass(LivingEntity entity, MerlinClass merlinClass)
    {
        if (merlinClass == null)
        {
            AbilityManager.getInstance().reset(entity, AbilityProviderType.CLASS);
        }

        if (merlinClass != null)
        {
            if (!merlinClass.isConfigSet())
            {
                merlinClass.setConfig(MerlinRegistries.CLASSES.getConfig(merlinClass.getId()));
            }

            merlinClass = (MerlinClass) AbilityManager.getInstance().addProvider(entity, merlinClass);

            merlinClass.setEntity(entity);
            merlinClass.rebuild();
        }

        return merlinClass;
    }

    /**
     * Gets an entity's {@link MerlinClass}
     *
     * @param entity the entity
     * @return the class
     */
    public MerlinClass getClass(LivingEntity entity)
    {
        List<AbilityProvider> providers = AbilityManager.getInstance().getProviders(entity, AbilityProviderType.CLASS);

        return (MerlinClass) providers.stream().filter(prov -> prov instanceof MerlinClass && prov.isEnabled()).findFirst().orElse(null);
    }

    /**
     * Resets an entity's {@link MerlinClass}
     *
     * @param entity the entity
     */
    public void reset(LivingEntity entity)
    {
        this.setClass(entity, (MerlinClass) null);
    }

    @EventHandler
    private void onConnect(PlayerJoinEvent event)
    {
        MerlinClass merlinClass = this.getClass(event.getPlayer());

        if (merlinClass == null) return;

        merlinClass.setEntity(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onDeath(EntityDeathEvent event)
    {
        LivingEntity entity = event.getEntity();

        if (entity instanceof Player) return;

        this.reset(entity);
    }

    // TODO MONITOR PERFORMANCE
    @EventHandler
    private void onChunkLoaded(ChunkLoadEvent event)
    {
        Chunk chunk = event.getChunk();

        this.loadFromChunk(chunk);
    }

    @EventHandler
    private void onWorldLoaded(WorldLoadEvent event)
    {
        for (Chunk chunk : event.getWorld().getLoadedChunks())
        {
            this.loadFromChunk(chunk);
        }
    }

    private void loadFromChunk(Chunk chunk)
    {
        //if (chunk.isLoaded()) return;

        for (Entity entity : chunk.getEntities())
        {
            if (!(entity instanceof LivingEntity livingEntity)) continue;
            if (entity instanceof Player) continue;

            PersistentDataContainer data = entity.getPersistentDataContainer();

            if (data.has(CLASS_KEY, PersistentDataType.STRING))
            {
                String classKey = data.get(CLASS_KEY, PersistentDataType.STRING);
                MerlinClass merlinClass = MerlinRegistries.CLASSES.get(classKey).create();

                if (merlinClass == null || ClassManager.getInstance().getClass(livingEntity) != null)
                {
                    continue;
                }

                ClassManager.getInstance().setClass(livingEntity, merlinClass);
            }
        }
    }

    @EventHandler
    private void onDestroyEntity(EntityDestroyEvent event)
    {
        BolsterEntity entity = event.getEntity();
        MerlinClass merlinClass = this.getClass(entity.getBukkit());

        if (merlinClass == null) return;

        merlinClass.destroy();
    }

    public static ClassManager getInstance()
    {
        return _instance;
    }
}

package co.runed.merlin.classes;

import co.runed.bolster.events.entity.EntityDestroyEvent;
import co.runed.bolster.managers.Manager;
import co.runed.bolster.util.registries.Definition;
import co.runed.merlin.Merlin;
import co.runed.merlin.core.MerlinRegistries;
import co.runed.merlin.core.SpellManager;
import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class ClassManager extends Manager {
    //private final Map<UUID, BolsterClass> bolsterClasses = new HashMap<>();

    public static final NamespacedKey CLASS_KEY = new NamespacedKey(Merlin.getInstance(), "class");

    private static ClassManager _instance;

    public ClassManager(Plugin plugin) {
        super(plugin);

        _instance = this;
    }

    public ClassImpl setClass(LivingEntity entity, Definition<ClassImpl> classDefinition) {
        return setClass(entity, classDefinition.create());
    }

    /**
     * Sets an entity's {@link ClassImpl}
     *
     * @param entity        the entity
     * @param classInstance the class
     */
    public ClassImpl setClass(LivingEntity entity, ClassImpl classInstance) {
        SpellManager.getInstance().removeProvider(entity, getClass(entity));

        if (classInstance != null) {
            classInstance.setConfig(MerlinRegistries.CLASSES.getConfig(classInstance.getId()));

            SpellManager.getInstance().addProvider(entity, classInstance);

            classInstance.setOwner(entity);
        }

        return classInstance;
    }

    /**
     * Gets an entity's {@link ClassImpl}
     *
     * @param entity the entity
     * @return the class
     */
    public ClassImpl getClass(LivingEntity entity) {
        var providers = SpellManager.getInstance().getProviders(entity);

        return (ClassImpl) providers.stream().filter(prov -> prov instanceof ClassImpl impl && impl.isEnabled()).findFirst().orElse(null);
    }

    /**
     * Resets an entity's {@link ClassImpl}
     *
     * @param entity the entity
     */
    public void reset(LivingEntity entity) {
        setClass(entity, (ClassImpl) null);
    }

    public boolean hasClass(LivingEntity entity, ClassDefinition definition) {
        return definition.equals(getClass(entity));
    }

    @EventHandler
    private void onConnect(PlayerJoinEvent event) {
        var classInstance = this.getClass(event.getPlayer());

        if (classInstance == null) return;

        classInstance.setOwner(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onDeath(EntityDeathEvent event) {
        var entity = event.getEntity();

        if (entity instanceof Player) return;

        reset(entity);
    }

    // TODO MONITOR PERFORMANCE
    @EventHandler
    private void onChunkLoaded(ChunkLoadEvent event) {
        var chunk = event.getChunk();

        loadFromChunk(chunk);
    }

    @EventHandler
    private void onWorldLoaded(WorldLoadEvent event) {
        for (var chunk : event.getWorld().getLoadedChunks()) {
            loadFromChunk(chunk);
        }
    }

    private void loadFromChunk(Chunk chunk) {
        //if (chunk.isLoaded()) return;

        for (var entity : chunk.getEntities()) {
            if (!(entity instanceof LivingEntity livingEntity)) continue;
            if (entity instanceof Player) continue;

            var data = entity.getPersistentDataContainer();

            if (data.has(CLASS_KEY, PersistentDataType.STRING)) {
                var classKey = data.get(CLASS_KEY, PersistentDataType.STRING);
                var classInstance = MerlinRegistries.CLASSES.get(classKey).create();

                if (classInstance == null || getClass(livingEntity) != null) continue;

                setClass(livingEntity, classInstance);
            }
        }
    }

    @EventHandler
    private void onDestroyEntity(EntityDestroyEvent event) {
        var entity = event.getEntity();
        var classInstance = this.getClass(entity.getEntity());

        if (classInstance == null) return;

        SpellManager.getInstance().destroyProvider(entity.getEntity(), classInstance);
    }

    public static ClassManager getInstance() {
        return _instance;
    }
}

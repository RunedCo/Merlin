package co.runed.merlin;

import co.runed.bolster.managers.CommandManager;
import co.runed.bolster.common.properties.Property;
import co.runed.bolster.util.registries.Registries;
import co.runed.bolster.util.registries.Registry;
import co.runed.merlin.commands.*;
import co.runed.merlin.core.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class Merlin extends JavaPlugin
{
    private ItemManager itemManager;
    private ClassManager classManager;
    private ManaManager manaManager;
    private AbilityManager abilityManager;

    private static Merlin _instance;

    @Override
    public void onEnable()
    {
        _instance = this;

        // Create Managers
        this.itemManager = new ItemManager(this);
        this.classManager = new ClassManager(this);
        this.abilityManager = new AbilityManager(this);

        // SET MANA MANAGER SETTINGS
        this.manaManager = new ManaManager(this);
        this.manaManager.setDefaultMaximumMana(200);
        this.manaManager.setEnableXpManaBar(true);

        // Register Commands
        CommandManager commandManager = CommandManager.getInstance();
        commandManager.add(new CommandItems());
        commandManager.add(new CommandItemsGUI());
        commandManager.add(new CommandBecome());
        commandManager.add(new CommandBecomeGUI());
        commandManager.add(new CommandMana());
        commandManager.add(new CommandSummonDummy());

        this.registerTraits();
    }

    @Override
    public void onDisable()
    {
        // Plugin shutdown logic
    }

    private void registerTraits()
    {
        Registry<Property<?>> registry = Registries.TRAITS;

        registry.register(MerlinTraits.ATTACK_DAMAGE);
        registry.register(MerlinTraits.ATTACK_SPEED);
        registry.register(MerlinTraits.KNOCKBACK);
        registry.register(MerlinTraits.KNOCKBACK_RESISTANCE);
        registry.register(MerlinTraits.MANA_PER_SECOND);
        registry.register(MerlinTraits.SPELL_DAMAGE);
    }

    public static Merlin getInstance()
    {
        return _instance;
    }
}

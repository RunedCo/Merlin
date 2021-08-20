package co.runed.merlin.classes;

import co.runed.bolster.game.traits.Traits;
import co.runed.bolster.managers.StatusEffectManager;
import co.runed.bolster.util.ItemBuilder;
import co.runed.bolster.common.properties.Properties;
import co.runed.merlin.abilities.Ability;
import co.runed.merlin.abilities.AbilityTrigger;
import co.runed.merlin.core.*;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public abstract class MerlinClass extends AbilityProvider
{
    ItemStack icon = new ItemStack(Material.PLAYER_HEAD);
    String description;
    double maxHealth = 20;

    private double startingBaseAttackDamage;

    @Override
    public void loadConfig(ConfigurationSection config)
    {
        if (config.isDouble("attack-damage")) this.setBaseAttackDamage(config.getDouble("attack-damage"));
    }

    @Override
    public void create()
    {

    }

    @Override
    public AbilityProviderType getType()
    {
        return AbilityProviderType.CLASS;
    }

    public void setMaxHealth(double maxHealth)
    {
        this.maxHealth = maxHealth;
        this.setTrait(Traits.MAX_HEALTH, maxHealth);
    }

    public double getMaxHealth()
    {
        return maxHealth;
    }

    @Override
    public ItemStack getIcon()
    {
        String desc = this.getDescription();

        return new ItemBuilder(this.icon)
                .setDisplayName(Component.text(this.getName()))
                .setLoreComponent(desc != null ? Component.text(desc) : Component.empty())
                .build();
    }

    public void setIcon(ItemStack icon)
    {
        this.icon = icon;
    }

    @Override
    public String getId()
    {
        return MerlinRegistries.CLASSES.getIdFromValue(this);
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    @Override
    public String getDescription()
    {
        String out = super.getDescription();

        if (out != null && !out.isEmpty() && this.description != null && !this.description.isEmpty())
        {
            out += "\n\n" + ChatColor.WHITE + this.description;
        }

        return out;
    }

    @Override
    public void setEntity(LivingEntity entity, boolean trigger)
    {
        boolean firstTime = this.getEntity() == null || !this.getEntity().getUniqueId().equals(entity.getUniqueId());

        if (entity.equals(this.getEntity())) return;

        super.setEntity(entity, trigger);

        if (trigger)
        {
            if (firstTime)
            {
                AbilityManager.getInstance().trigger(entity, this, AbilityTrigger.SET_CLASS, new Properties());
            }

            if (this.getEntity() != entity) ClassManager.getInstance().setClass(entity, this);
        }
    }

    @Override
    public void onEnable()
    {
        super.onEnable();

        AttributeInstance attackDamageAttribute = this.getEntity().getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        if (attackDamageAttribute != null) attackDamageAttribute.setBaseValue(this.getBaseAttackDamage());
    }

    @Override
    public void onDisable()
    {
        super.onDisable();

        AttributeInstance attackDamageAttribute = this.getEntity().getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        if (attackDamageAttribute != null) attackDamageAttribute.setBaseValue(attackDamageAttribute.getDefaultValue());

        // clear status effects
        StatusEffectManager.getInstance().clearStatusEffects(this.getEntity());
    }

    @Override
    public void onCastAbility(Ability ability, boolean success)
    {

    }

    @Override
    public void onToggleCooldown(Ability ability)
    {

    }

    public void setBaseAttackDamage(double damage)
    {
        this.setTrait(MerlinTraits.ATTACK_DAMAGE, damage);
    }

    public double getBaseAttackDamage()
    {
        return this.getTrait(MerlinTraits.ATTACK_DAMAGE);
    }

    @Override
    public boolean rebuild()
    {
        if (!this.isDirty()) return false;

        super.rebuild();

        if (this.getEntity() != null && !(this.getEntity() instanceof Player))
        {
            PersistentDataContainer data = this.getEntity().getPersistentDataContainer();
            data.set(ClassManager.CLASS_KEY, PersistentDataType.STRING, this.getId());
        }

        return true;
    }

    @Override
    public void destroy(boolean trigger)
    {
        if (this.getEntity() != null)
        {
            if (trigger)
            {
                AbilityManager.getInstance().trigger(this.getEntity(), this, AbilityTrigger.REMOVE_CLASS, new Properties());
            }
        }

        super.destroy(trigger);

        if (!(this.getEntity() instanceof Player))
        {
            this.getEntity().getPersistentDataContainer().remove(ClassManager.CLASS_KEY);
        }

        // REMOVE UPGRADES
    }
}

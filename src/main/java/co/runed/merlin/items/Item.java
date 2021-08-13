package co.runed.merlin.items;

import co.runed.bolster.Bolster;
import co.runed.bolster.game.traits.Traits;
import co.runed.bolster.util.Definition;
import co.runed.bolster.util.ItemBuilder;
import co.runed.merlin.abilities.Ability;
import co.runed.merlin.conditions.ConditionPriority;
import co.runed.merlin.conditions.item.HasItemCondition;
import co.runed.merlin.conditions.item.IsItemEquippedCondition;
import co.runed.merlin.core.*;
import co.runed.merlin.target.Target;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public abstract class Item extends AbilityProvider
{
    public static final NamespacedKey ITEM_ID_KEY = new NamespacedKey(Bolster.getInstance(), "item-id");
    public static final NamespacedKey ITEM_SKIN_KEY = new NamespacedKey(Bolster.getInstance(), "item-skin");

    public static final String ATTACK_DAMAGE_KEY = "attack-damage";
    public static final String ATTACK_SPEED_KEY = "attack-speed";
    public static final String KNOCKBACK_RESISTANCE_KEY = "knockback-resistance";
    public static final String KNOCKBACK_KEY = "knockback";
    public static final String HEALTH_KEY = "health";
    public static final String DROPPABLE_KEY = "droppable";

    private static final UUID attackDamageUuid = new UUID(1234, 1234);
    private static final UUID attackSpeedUuid = new UUID(1235, 1235);
    private static final UUID knockbackResistanceUuid = new UUID(1236, 1236);
    private static final UUID knockBackUuid = new UUID(1237, 1237);
    private static final UUID healthUuid = new UUID(1238, 1238);

    ItemStack itemStack = new ItemStack(Material.SAND);
    ItemSkin skin;

    boolean droppable = false;
    boolean clearOnRemove = true;
    int maxInInventory = -1;

    private final Map<Ability, Boolean> abilityCooldowns = new HashMap<>();

    @Override
    public void loadConfig(ConfigurationSection config)
    {
        super.loadConfig(config);

        /* Attributes */
        this.setTrait(MerlinTraits.ATTACK_DAMAGE, config.getDouble(ATTACK_DAMAGE_KEY, 1));
        this.setTrait(MerlinTraits.ATTACK_SPEED, config.getDouble(ATTACK_SPEED_KEY, 0));
        this.setTrait(MerlinTraits.KNOCKBACK_RESISTANCE, config.getDouble(KNOCKBACK_RESISTANCE_KEY, 0));
        this.setTrait(MerlinTraits.KNOCKBACK, config.getDouble(KNOCKBACK_KEY, 0));
        this.setTrait(Traits.MAX_HEALTH, config.getDouble(HEALTH_KEY, 0));

        /* Properties */
        this.setDroppable(config.getBoolean(DROPPABLE_KEY, droppable));
    }

    @Override
    public AbilityProviderType getType()
    {
        return AbilityProviderType.ITEM;
    }

    public boolean isDroppable()
    {
        return droppable;
    }

    public void setDroppable(boolean droppable)
    {
        this.droppable = droppable;
    }

    public void setClearOnRemove(boolean clearOnRemove)
    {
        this.clearOnRemove = clearOnRemove;
    }

    public boolean shouldClearOnRemove()
    {
        return clearOnRemove;
    }

    public int getMaxInInventory()
    {
        return maxInInventory;
    }

    public void setMaxInInventory(int maxInInventory)
    {
        this.maxInInventory = maxInInventory;
    }

    public ItemStack getBaseItemStack()
    {
        return itemStack;
    }

    public void setBaseItemStack(ItemBuilder itemStack)
    {
        this.itemStack = itemStack.build();
    }

    public void setBaseItemStack(ItemStack itemStack)
    {
        this.itemStack = itemStack;
    }

    /* Item Definition */
    public Definition<Item> getDefinition()
    {
        return MerlinRegistries.ITEMS.get(this.getId());
    }

    public boolean hasSkin()
    {
        return this.getSkin() != null;
    }

    public ItemSkin getSkin()
    {
        return skin;
    }

    public List<Component> getStatsLore()
    {
        List<Component> out = new ArrayList<>();

        // TODO

        var attackDamage = this.getTrait(MerlinTraits.ATTACK_DAMAGE);
        var attackSpeed = this.getTrait(MerlinTraits.ATTACK_SPEED);
        var knockBackResistance = this.getTrait(MerlinTraits.KNOCKBACK_RESISTANCE);
        var knockBack = this.getTrait(MerlinTraits.KNOCKBACK);
        var maxHealth = this.getTrait(Traits.MAX_HEALTH);

        if (attackDamage > 1)
        {
            out.add(Component.text(ChatColor.GRAY + "Attack Damage: " + ChatColor.AQUA + attackDamage));
        }

        if (knockBack > 0)
        {
            out.add(Component.text(ChatColor.GRAY + "Knockback: " + ChatColor.AQUA + knockBack));
        }

        if (knockBackResistance > 0)
        {
            out.add(Component.text(ChatColor.GRAY + "Knockback Resistance: " + ChatColor.AQUA + knockBackResistance));
        }

        if (maxHealth > 0)
        {
            out.add(Component.text(ChatColor.GRAY + "Health: " + ChatColor.AQUA + maxHealth));
        }

        return out;
    }

    @Override
    public Ability addAbility(Ability ability)
    {
        if (ability.showCooldown()) this.abilityCooldowns.put(ability, true);

        if (ability.itemMustBeActive())
        {
            ability.condition(ConditionPriority.HIGHEST, new IsItemEquippedCondition(Target.CASTER, this.getDefinition()));
        }
        else if (ability.itemMustBeInInventory())
        {
            ability.condition(ConditionPriority.HIGHEST, new HasItemCondition(Target.CASTER, this.getDefinition(), 1));
        }

        return super.addAbility(ability);
    }

    @Override
    public void onCastAbility(Ability ability, boolean success)
    {
        if (this.getEntity() instanceof Player && this.getId() != null)
        {
            ItemManager.getInstance().rebuildItemStack(this.getEntity(), this.getId());
        }
    }

    @Override
    public void onToggleCooldown(Ability ability)
    {
        if (this.abilityCooldowns.containsKey(ability) && this.abilityCooldowns.get(ability) && this.getEntity() instanceof Player player)
        {
            if (ability.isOnCooldown())
            {
                player.setCooldown(this.getBaseItemStack().getType(), (int) (ability.getRemainingCooldown() * 20));
            }
            else
            {
                player.setCooldown(this.getBaseItemStack().getType(), 0);
            }
        }
    }

    @Override
    public boolean rebuild()
    {
        if (!this.isDirty()) return false;

        super.rebuild();

        if (this.getEntity() != null && this.getEntity() instanceof Player player)
        {
            ItemManager.getInstance().rebuildItemStack(player, this);
        }

        return true;
    }

    @Override
    public void destroy(boolean trigger)
    {
        super.destroy(trigger);

        this.abilityCooldowns.clear();
//        this.lore.clear();
    }

    public ItemStack toItemStack()
    {
        ItemBuilder builder = new ItemBuilder(this.getBaseItemStack())
                .setDisplayName(Component.text(this.getName()))
                .setPersistentData(Item.ITEM_ID_KEY, PersistentDataType.STRING, this.getId())
                .setUnbreakable(true)
                .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .addItemFlag(ItemFlag.HIDE_UNBREAKABLE);

        var lore = new ArrayList<Component>();
        lore.addAll(getStatsLore());
        lore.addAll(this.getLore());

        if (lore.size() > 0) builder = builder.setLoreComponent(lore);

        var attackDamage = this.getTrait(MerlinTraits.ATTACK_DAMAGE);
        var attackSpeed = this.getTrait(MerlinTraits.ATTACK_SPEED);
        var knockBackResistance = this.getTrait(MerlinTraits.KNOCKBACK_RESISTANCE);
        var knockBack = this.getTrait(MerlinTraits.KNOCKBACK);
        var maxHealth = this.getTrait(Traits.MAX_HEALTH);

        builder = builder.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(attackDamageUuid, "attack_damage", attackDamage > 0 ? attackDamage : 1, AttributeModifier.Operation.ADD_NUMBER))
                .addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(attackSpeedUuid, "attack_speed", attackSpeed, AttributeModifier.Operation.ADD_NUMBER))
                .addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, new AttributeModifier(knockbackResistanceUuid, "knockback_resistance", knockBackResistance, AttributeModifier.Operation.ADD_NUMBER))
                .addAttributeModifier(Attribute.GENERIC_ATTACK_KNOCKBACK, new AttributeModifier(knockBackUuid, "knockback", knockBack, AttributeModifier.Operation.ADD_NUMBER))
                .addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(healthUuid, "health", maxHealth, AttributeModifier.Operation.ADD_NUMBER));

        if (this.hasSkin())
        {
            ItemSkin skin = this.getSkin();

            builder = new ItemBuilder(skin.toItemStack(builder.build()));
        }

        return builder.build();
    }
}

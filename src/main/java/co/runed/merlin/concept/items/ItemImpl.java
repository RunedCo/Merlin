package co.runed.merlin.concept.items;

import co.runed.bolster.damage.DamageInfo;
import co.runed.bolster.damage.DamageSource;
import co.runed.bolster.game.traits.Traits;
import co.runed.bolster.util.ComponentUtil;
import co.runed.bolster.util.ItemBuilder;
import co.runed.merlin.concept.spells.CastResult;
import co.runed.merlin.concept.spells.SpellDefinition;
import co.runed.merlin.concept.spells.SpellOption;
import co.runed.merlin.concept.spells.SpellProvider;
import co.runed.merlin.concept.triggers.Trigger;
import co.runed.merlin.core.MerlinTraits;
import co.runed.merlin.core.SpellProviderType;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class ItemImpl extends SpellProvider implements DamageSource {
    public static final String ATTACK_DAMAGE_KEY = "attack-damage";
    public static final String ATTACK_SPEED_KEY = "attack-speed";
    public static final String KNOCKBACK_RESISTANCE_KEY = "knockback-resistance";
    public static final String KNOCKBACK_KEY = "knockback";
    public static final String DROPPABLE_KEY = "droppable";

    private static final UUID attackDamageUuid = new UUID(1234, 1234);
    private static final UUID attackSpeedUuid = new UUID(1235, 1235);
    private static final UUID knockbackResistanceUuid = new UUID(1236, 1236);
    private static final UUID knockBackUuid = new UUID(1237, 1237);

    private final Map<SpellDefinition, ItemRequirement> itemRequirements = new HashMap<>();

    private boolean droppable = false;

    public ItemImpl(ItemDefinition definition) {
        super(definition);
    }

    @Override
    public void loadConfig(ConfigurationSection config) {
        super.loadConfig(config);

        /* Attributes */
        setTrait(MerlinTraits.ATTACK_DAMAGE, config.getDouble(ATTACK_DAMAGE_KEY, 1));
        setTrait(MerlinTraits.ATTACK_SPEED, config.getDouble(ATTACK_SPEED_KEY, 0));
        setTrait(MerlinTraits.KNOCKBACK_RESISTANCE, config.getDouble(KNOCKBACK_RESISTANCE_KEY, 0));
        setTrait(MerlinTraits.KNOCKBACK, config.getDouble(KNOCKBACK_KEY, 0));

        /* Properties */
        setDroppable(config.getBoolean(DROPPABLE_KEY, droppable));

        rebuild();
    }

    public boolean isDroppable() {
        return droppable;
    }

    public void setDroppable(boolean droppable) {
        this.droppable = droppable;
    }

    @Override
    public CastResult preCast(Trigger trigger) {
        var context = trigger.getContext();
        var spellDef = context.getSpell().getDefinition();
        var result = itemRequirements.getOrDefault(spellDef, ItemRequirement.ALWAYS);

        if (!spellDef.hasOption(SpellOption.IGNORE_ITEM_REQUIREMENTS) && !result.evaluate(context)) return CastResult.fail();

        return super.preCast(trigger);
    }

    @Override
    public void postCast(Trigger trigger) {
        var context = trigger.getContext();

        super.postCast(trigger);

        if (context.getSpell().hasOption(SpellOption.SHOW_COOLDOWN)) {
            // TODO SHOW ITEM COOLDOWN
        }

        this.rebuild();
    }

    @Override
    public String getDeathMessage(Player killer, Player victim, DamageInfo damageInfo) {
        return super.getDeathMessage(killer, victim, damageInfo) + " using " + getName();
    }

    public void setItemRequirements(Map<SpellDefinition, ItemRequirement> requirements) {
        this.itemRequirements.putAll(requirements);
    }

    @Override
    public ItemDefinition getDefinition() {
        return (ItemDefinition) super.getDefinition();
    }

    @Override
    public SpellProviderType getType() {
        return SpellProviderType.ITEM;
    }

    public void rebuild() {
        if (getOwner() != null) {
            ItemManager.getInstance().rebuildItemStack(getOwner(), getDefinition());
        }
    }

    public List<Component> getStatsLore() {
        List<Component> out = new ArrayList<>();

        // TODO atk spd
        var attackDamage = getTrait(MerlinTraits.ATTACK_DAMAGE);
        var attackSpeed = getTrait(MerlinTraits.ATTACK_SPEED);
        var knockBackResistance = getTrait(MerlinTraits.KNOCKBACK_RESISTANCE);
        var knockBack = getTrait(MerlinTraits.KNOCKBACK);
        var maxHealth = getTrait(Traits.MAX_HEALTH);

        if (getDefinition().getMaxLevel() > 0) {
            out.add(Component.text(ChatColor.GRAY + "Level " + this.getLevel()));
        }

        if (attackDamage > 1) {
            out.add(Component.text(ChatColor.GRAY + "Attack Damage: " + ChatColor.AQUA + attackDamage));
        }

        if (knockBack > 0) {
            out.add(Component.text(ChatColor.GRAY + "Knockback: " + ChatColor.AQUA + knockBack));
        }

        if (knockBackResistance > 0) {
            out.add(Component.text(ChatColor.GRAY + "Knockback Resistance: " + ChatColor.AQUA + knockBackResistance));
        }

        if (maxHealth > 0) {
            out.add(Component.text(ChatColor.GRAY + "Health: " + ChatColor.AQUA + maxHealth));
        }

        return out;
    }

    public Collection<Component> getLore() {
        return ComponentUtil.wrappedText(getDescription());
    }

    @Override
    public ItemStack getIcon() {
        return this.toItemStack();
    }

    public ItemStack toItemStack() {
        var builder = new ItemBuilder(getDefinition().getBaseItemStack())
                .setDisplayName(Component.text(ChatColor.WHITE + getName()))
                .setPersistentData(ItemManager.ITEM_ID_KEY, PersistentDataType.STRING, this.getId())
                .setUnbreakable(true)
                .addItemFlag(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);

        var lore = new ArrayList<Component>();
        lore.addAll(getStatsLore());
        lore.addAll(getLore());

        if (lore.size() > 0) builder = builder.setLoreComponent(lore);

        var attackDamage = getTrait(MerlinTraits.ATTACK_DAMAGE);
        var attackSpeed = getTrait(MerlinTraits.ATTACK_SPEED);
        var knockBackResistance = getTrait(MerlinTraits.KNOCKBACK_RESISTANCE);
        var knockBack = getTrait(MerlinTraits.KNOCKBACK);

        builder = builder.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(attackDamageUuid, "attack_damage", attackDamage > 0 ? attackDamage - 1 : attackDamage, AttributeModifier.Operation.ADD_NUMBER))
                .addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(attackSpeedUuid, "attack_speed", attackSpeed, AttributeModifier.Operation.ADD_NUMBER))
                .addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, new AttributeModifier(knockbackResistanceUuid, "knockback_resistance", knockBackResistance, AttributeModifier.Operation.ADD_NUMBER))
                .addAttributeModifier(Attribute.GENERIC_ATTACK_KNOCKBACK, new AttributeModifier(knockBackUuid, "knockback", knockBack, AttributeModifier.Operation.ADD_NUMBER));

//        if (this.hasSkin()) {
//            ItemSkin skin = this.getSkin();
//
//            builder = new ItemBuilder(skin.toItemStack(builder.build()));
//        }

        return builder.build();
    }
}

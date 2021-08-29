package co.runed.merlin.concept.items;

import co.runed.bolster.util.Category;
import co.runed.bolster.util.ItemBuilder;
import co.runed.merlin.concept.spells.SpellDefinition;
import co.runed.merlin.concept.spells.SpellManager;
import co.runed.merlin.concept.spells.SpellType;
import org.bukkit.Material;

public class Items {
    /* Base Spells */
    public static SpellDefinition ON_SHOOT_BOW = new SpellDefinition("on_shoot_bow", OnShootBow::new);

    /* Base Items */
    private static ItemDefinition WEAPON_BASE = new ItemDefinition("weapon_base")
            .setBaseItemStack(new ItemBuilder(Material.NETHERITE_HOE))
            .category(Category.WEAPONS, Category.LEVELABLE);

    public static ItemDefinition RANGED_WEAPON_BASE = new ItemDefinition("ranged_weapon_base")
            .from(WEAPON_BASE)
            .setBaseItemStack(new ItemBuilder(Material.BOW))
            .category(Category.RANGED);

    // TODO add cancel event on fire bow? maybe move this to be per bow
    public static ItemDefinition BOW_BASE = new ItemDefinition("bow_base")
            .from(RANGED_WEAPON_BASE)
            .addSpell(ON_SHOOT_BOW, SpellType.NORMAL, ItemRequirement.MAIN_HAND);

    /* DVZ Items */
    public static ItemDefinition RUNEBLADE = new ItemDefinition("runeblade")
            .from(WEAPON_BASE)
            .addSpell(SpellManager.RUNEDASH, SpellType.NORMAL, ItemRequirement.ANY_HAND)
            .register();

    public static AmmoDefinition DANCER_ARROW = new AmmoDefinition("dancer_arrow")
            .setName("Dancer Arrow")
            .register();

    public static ItemDefinition LIGHT_DANCER = new ItemDefinition("light_dancer")
            .from(BOW_BASE)
            .setName("Light Dancer")
            .setAmmoDefinition(DANCER_ARROW)
            .setMaxAmmo(30)
            .register();
}

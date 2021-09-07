package co.runed.merlin.concept.items;

import co.runed.bolster.game.traits.Traits;
import co.runed.bolster.util.Category;
import co.runed.bolster.util.ItemBuilder;
import co.runed.merlin.concept.costs.AmmoCost;
import co.runed.merlin.concept.items.ammo.AmmoDefinition;
import co.runed.merlin.concept.spells.AmmoShootSpell;
import co.runed.merlin.concept.spells.SpellDefinition;
import co.runed.merlin.concept.spells.SpellOption;
import co.runed.merlin.concept.spells.TickTest;
import co.runed.merlin.concept.spells.dvz.longbow.TNTBowShoot;
import co.runed.merlin.concept.spells.dvz.rocketboots.RocketJump;
import co.runed.merlin.concept.spells.dvz.runeblade.Runedash;
import co.runed.merlin.concept.spells.dvz.wand.WandBlast;
import org.bukkit.Material;

public class Items {
    /* Test Spells */
    public static SpellDefinition TICK_TEST = new SpellDefinition("tick_test", TickTest::new);

    /* Base Spells */
    public static SpellDefinition AMMO_SHOOT_BASE_NO_ARROW = new SpellDefinition("ammo_shoot_bow_no_arrow", AmmoShootSpell::new)
            .priority(1000)
            .cost(() -> new AmmoCost(1));

    public static SpellDefinition AMMO_SHOOT_BASE_ARROW = new SpellDefinition("ammo_shoot_bow_arrow", (def) -> new AmmoShootSpell(def, true))
            .priority(1000)
            .cost(() -> new AmmoCost(1));

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
            .addSpell(AMMO_SHOOT_BASE_NO_ARROW, ItemRequirement.MAIN_HAND)
            .from(RANGED_WEAPON_BASE);

    /* DVZ Items */
    // Runeblade
    public static final SpellDefinition RUNEDASH = new SpellDefinition("runedash", Runedash::new)
            .setName("Runedash")
            .options(SpellOption.ALERT_WHEN_READY)
            .cooldown(10)
            .priority(1000);

    public static ItemDefinition RUNEBLADE = new ItemDefinition("runeblade")
            .from(WEAPON_BASE)
            .addSpell(RUNEDASH, ItemRequirement.ANY_HAND)
            .setTrait(Traits.MAX_HEALTH, 20d)
            .register();

    // Light Dancer
    public static SpellDefinition ON_SHOOT_DANCER = new SpellDefinition("on_shoot_longbow", TNTBowShoot::new);

    public static AmmoDefinition DANCER_ARROW = new AmmoDefinition("dancer_arrow")
            .setName("Dancer Arrow")
            .register();

    public static ItemDefinition LIGHT_DANCER = new ItemDefinition("light_dancer")
            .from(BOW_BASE)
            .setName("Light Dancer")
            .setAmmoDefinition(DANCER_ARROW)
            .setMaxAmmo(30)
            .register();

    // Rangers Longbow
    public static SpellDefinition ON_SHOOT_LONGBOW = new SpellDefinition("on_shoot_longbow", TNTBowShoot::new);

    public static AmmoDefinition RANGER_ARROW = new AmmoDefinition("ranger_arrow")
            .setName("Ranger's Arrow")
            .register();

    public static ItemDefinition RANGERS_LONGBOW = new ItemDefinition("rangers_longbow")
            .from(BOW_BASE)
            .setName("Ranger's Longbow")
            .addSpell(ON_SHOOT_LONGBOW, ItemRequirement.MAIN_HAND)
            .setAmmoDefinition(RANGER_ARROW)
            .setMaxAmmo(20)
            .register();

    // WAND TEST
    public static SpellDefinition WAND_BLAST = new SpellDefinition("wand_blast", WandBlast::new);

    public static ItemDefinition MAGIC_WAND = new ItemDefinition("magic_wand")
            .setName("Magic Wand")
            .setBaseItemStack(new ItemBuilder(Material.STICK))
            .addSpell(WAND_BLAST, ItemRequirement.MAIN_HAND)
            .register();

    // BOOTS TEST
    public static SpellDefinition ROCKET_JUMP = new SpellDefinition("rocket_jump", RocketJump::new)
            .cooldown(20);

    public static ItemDefinition ROCKET_BOOTS = new ItemDefinition("rocket_boots")
            .setName("Rocket Boots")
            .setBaseItemStack(new ItemBuilder(Material.DIAMOND_BOOTS))
            .addSpell(ROCKET_JUMP, ItemRequirement.ARMOR)
            .register();
}

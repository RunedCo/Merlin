package co.runed.merlin.spells.dvz.warhammer;

import co.runed.bolster.util.StringUtil;
import co.runed.bolster.util.config.ConfigEntry;
import co.runed.dayroom.properties.Property;
import co.runed.merlin.core.MerlinTraits;
import co.runed.merlin.spells.CastResult;
import co.runed.merlin.spells.Spell;
import co.runed.merlin.spells.SpellDefinition;
import co.runed.merlin.triggers.SpellTrigger;
import co.runed.merlin.triggers.damage.KillEntityTrigger;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class WarhammerLevelUp extends Spell {
    public static final Property<Integer> WARHAMMER_MONSTER_KILLS = new Property<>("warhammer_monster_kills", 0);
    public static final Property<Integer> WARHAMMER_RANK = new Property<>("warhammer_level", 0);

    public static final Sound RANK_UP_SOUND = Sound.sound(Key.key("warhammerrankup"), Sound.Source.PLAYER, 1, 1);

    @ConfigEntry("required-kills")
    private int levelUpRequiredKills = 1;

    @ConfigEntry("max-level")
    private int levelUpMaxLevel = 11;

    @ConfigEntry("kill-armor-increase")
    private int killArmorIncrease = 1;

    @ConfigEntry("kill-armor-increase-enabled")
    private boolean killArmorIncreaseEnabled = false;

    private int previousRank = 0;

    public WarhammerLevelUp(@NotNull SpellDefinition definition) {
        super(definition);
    }

    @SpellTrigger
    public CastResult onKillMonster(KillEntityTrigger trigger) {
        var parentProperties = getParent().getProperties();
        var kills = parentProperties.get(WARHAMMER_MONSTER_KILLS);
        var target = trigger.getTarget();

        parentProperties.set(WARHAMMER_MONSTER_KILLS, kills + 1);
        parentProperties.set(WARHAMMER_RANK, getRank());

        // TODO: check target is monster!

        updateRank();

        return CastResult.success();
    }

    public int getRank() {
        var parent = getParent();
        var parentProperties = parent.getProperties();
        var kills = parentProperties.get(WARHAMMER_MONSTER_KILLS);

        return Math.min(levelUpMaxLevel, (kills + 1) / levelUpRequiredKills);
    }

    private void updateRank() {
        var parent = getParent();
        var rank = getRank();

        parent.setLangReplacement("warhammer-rank", (rank > 0 ? " Rank " + StringUtil.toRoman(rank) : ""));

//        if (this.killArmorIncrease != 0) {
//            this.setMaxArmor(this.getMaxArmor() + this.killArmorIncrease);
//        }

        if (rank > this.previousRank) {
            parent.setTrait(MerlinTraits.ATTACK_DAMAGE, parent.getTrait(MerlinTraits.ATTACK_DAMAGE) + 1);

            if (this.getOwner() instanceof Player player) {
                player.playSound(RANK_UP_SOUND);
                player.sendTitle(ChatColor.GOLD + "RANK UP!", "", 5, 20, 0);
            }
        }

        this.previousRank = rank;
    }
}

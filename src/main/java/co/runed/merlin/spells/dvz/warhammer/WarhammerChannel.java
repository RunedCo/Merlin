package co.runed.merlin.spells.dvz.warhammer;

import co.runed.merlin.spells.CastResult;
import co.runed.merlin.spells.Spell;
import co.runed.merlin.spells.SpellDefinition;
import co.runed.merlin.triggers.SpellTrigger;
import co.runed.merlin.triggers.Trigger;
import co.runed.merlin.triggers.core.TickTrigger;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import org.jetbrains.annotations.NotNull;

public class WarhammerChannel extends Spell {
    public static final Key WARHAMMER_CHARGE_KEY = Key.key("warhammercharge");
    public static final Sound WARHAMMER_CHARGE = Sound.sound(WARHAMMER_CHARGE_KEY, Sound.Source.PLAYER, 1, 1);

    public WarhammerChannel(@NotNull SpellDefinition definition) {
        super(definition);
    }

    @Override
    public CastResult preCast(Trigger trigger) {
        var result = super.preCast(trigger);

        if (!trigger.getContext().getCaster().isDrawingBow()) return CastResult.skip();

        if (result.isSuccess()) {
            trigger.getContext().getCasterEntity().playSound(WARHAMMER_CHARGE);
        }

        return result;
    }

    @SpellTrigger
    public CastResult onTick(TickTrigger trigger) {
        var caster = trigger.getContext().getCaster();
        var bolsterEntity = caster.toBolster();

        bolsterEntity.addHealth(2);

        return CastResult.success();
    }

    @Override
    public void cancel() {
        super.cancel();

        if (getOwner() != null) getOwner().stopSound(SoundStop.named(WARHAMMER_CHARGE_KEY));
    }
}

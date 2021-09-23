package co.runed.merlin.spells.type;

import co.runed.merlin.triggers.Trigger;

public class ToggleSpellType extends RepeatingSpellType {
    Class<? extends Trigger> trigger;

    // TODO UNIMPLEMENTED
    public ToggleSpellType(Class<? extends Trigger> trigger, long frequency) {
        super(frequency);

        this.trigger = trigger;
    }

    public Class<? extends Trigger> getTrigger() {
        return trigger;
    }
}

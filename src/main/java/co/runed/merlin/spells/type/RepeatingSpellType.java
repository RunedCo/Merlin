package co.runed.merlin.spells.type;

public class RepeatingSpellType extends SpellType {
    private long frequency;

    public RepeatingSpellType(long frequency) {
        super();

        this.frequency = frequency;
    }

    public long getFrequency() {
        return frequency;
    }
}

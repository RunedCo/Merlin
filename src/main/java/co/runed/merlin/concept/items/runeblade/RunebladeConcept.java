package co.runed.merlin.concept.items.runeblade;

import co.runed.merlin.concept.items.ItemRequirements;
import co.runed.merlin.concept.spells.SpellProvider;
import co.runed.merlin.concept.spells.Spells;

public class RunebladeConcept extends SpellProvider {
    @Override
    public void create() {
        super.create();

        this.addSpell(Spells.RUNEBLADE_RUNEDASH, ItemRequirements.ANY_HAND);
    }
}

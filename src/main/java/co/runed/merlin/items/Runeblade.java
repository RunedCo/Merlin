package co.runed.merlin.items;

import co.runed.bolster.util.DamageType;
import co.runed.merlin.abilities.Ability;
import co.runed.merlin.abilities.AbilityTrigger;
import co.runed.merlin.abilities.core.RepeatingAbility;
import co.runed.merlin.abilities.entity.health.DamageEntityAbility;
import co.runed.merlin.abilities.entity.movement.LeapAbility;
import co.runed.merlin.abilities.entity.selectors.AOEAbility;
import co.runed.merlin.conditions.Condition;
import co.runed.merlin.conditions.entity.IsAlliedCondition;
import co.runed.merlin.costs.ManaCost;
import co.runed.merlin.target.Target;
import org.bukkit.configuration.ConfigurationSection;

public class Runeblade extends Item
{
    private double runedashCooldown = 20;
    private double runedashUpVel = 0.5;
    private double runedashForwardVel = 5;
    private double runedashDamage = 20;
    private double runedashDamageRadius = 2;
    private boolean runedashEnabled = true;
    private double runedashCdr = 0;

    @Override
    public void loadConfig(ConfigurationSection config)
    {
        super.loadConfig(config);

        runedashCooldown = config.getDouble("runedash-cooldown", runedashCooldown);
        runedashUpVel = config.getDouble("runedash-upward-velocity", runedashUpVel);
        runedashForwardVel = config.getDouble("runedash-forward-velocity", runedashForwardVel);
        runedashDamage = config.getDouble("runedash-damage", runedashDamage);
        runedashCdr = config.getDouble("runedash-cooldown-reduction", runedashCdr);
        runedashDamageRadius = config.getDouble("runedash-damage-radius", runedashDamageRadius);
        runedashEnabled = config.getBoolean("runedash-enabled", runedashEnabled);
    }

    @Override
    public void create()
    {
        Ability runedash = this.addAbility(Ability.builder()
                .id("runedash")
                .name("Runedash")
                .enabled(this.runedashEnabled)
                .cooldown(this.runedashCooldown)
                .trigger(AbilityTrigger.RIGHT_CLICK, AbilityTrigger.LEFT_CLICK)
                .cost(new ManaCost(100))
                .ability(new LeapAbility((float) this.runedashUpVel, (float) this.runedashForwardVel))
                .ability(new RepeatingAbility()
                        .frequency(1)
                        .until(() -> true)
                        .ability(new AOEAbility(Target.CASTER_LOCATION, this.runedashDamageRadius)
                                .ignoredTarget(Target.CASTER)
                                .ability(new DamageEntityAbility(Target.TARGET, this.runedashDamage, DamageType.SECONDARY)
                                        .condition(Condition.not(new IsAlliedCondition(Target.TARGET)))
                                )
                        )
                )
                .then((Ability) null)
                .last((Ability) null)
        );
    }

    @Override
    public void onToggleCooldown(Ability ability)
    {

    }
}

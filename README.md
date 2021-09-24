# Merlin

## General
Functionality is mostly implemented via `Definition` classes. Currently implemented definitions are `SpellDefinition`, `ItemDefinition`, and `ClassDefinition`. These definition classes are statically defined (and registered) and describe either an implemetation of a `Spell` or a `SpellProvider`.

## SpellProviders
### SpellProvider
Handles general implementation for a spell provider. For more specific implementation requirements, this can be overriden.

### ItemSpellProvider
Provides specific implementation details for an item. This is mostly handling the actual display on an `ItemStack`.

### ClassSpellProvider
Provised specific implementation details for classes.

## Example
Below is an example of an item with a spell that damages all entities in a 5 block radius for 20 damage when the caster right clicks. It also puts the spell on cooldown for 10 seconds and consumes the item. Much of this behaviour is configurable in the config at the bottom.

### ItemDefinition
```java
public static ItemDefinition EXAMPLE_ITEM = new ItemDefinition("example_item")
        .addSpell(EXAMPLE_SPELL, SpellType.NORMAL, ItemRequirements.ANY_HAND);
```

### SpellDefinition
```java
public static final SpellDefinition EXAMPLE_SPELL = new SpellDefinition("example_spell", ExampleSpell::new)
        .options(SpellOption.ALERT_WHEN_READY)
        .cost(new ItemCost(1))
        .priority(2000);
```
### ExampleSpell
```java
public class ExampleSpell extends Spell implements InteractTrigger {
    double damage = 10d;
    double radius = 5d;

    @Override
    public void loadConfig(ConfigurationSection config) {
        super.loadConfig(config);

        damage = config.getDouble("damage", damage);
        radius = config.getDouble("radius", radius);
    }

    @SpellTrigger
    public CastResult onRightClick(RightClickTrigger trigger) {
        var context = trigger.getCastContext();
        var targets = AOE.livingEntities(context.getCastLocation(), radius);

        for (var target : targets) {
            var damage = new DamageInfo(damage)
                    .withAttacker(context.getCaster().getEntity())
                    .withSource(this)
                    .apply(target);
        }

        return CastResult.success();
    }
}
```

### Config
```yaml
example_item:
  name: "Example Item"
  description: "This is an example item!"

  example_spell: 
    cooldown: 10
    damage: 20
    name: "Example Spell"
```

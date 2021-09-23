package co.runed.merlin.spells;

public enum SpellOption {
    IGNORE_COSTS,                   // Will ignore all costs and trigger anyway
    IGNORE_COOLDOWN,                // Will ignore cooldown
    IGNORE_ITEM_REQUIREMENTS,       // Ignores ItemRequirement needed
    HIDE_ERROR_MESSAGES,            // Don't show any error messages
    IGNORE_CANCELLED,               // Still runs even if event is cancelled
    ALERT_WHEN_READY,               // Sends caster a message when they are next able to cast
    SHOW_COOLDOWN,                  // Show cooldown on item icon,
    CANCEL_BY_CAST,
    CANCEL_BY_MOVEMENT,
    CANCEL_BY_DEALING_DAMAGE,
    CANCEL_BY_TAKING_DAMAGE,
    RESET_COOLDOWN_ON_DEATH
}

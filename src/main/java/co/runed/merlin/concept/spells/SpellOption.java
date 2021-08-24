package co.runed.merlin.concept.spells;

public enum SpellOption {
    IGNORE_COSTS,                   // Will ignore all costs and trigger anyway
    IGNORE_COOLDOWN,                // Will ignore cooldown
    IGNORE_ITEM_REQUIREMENTS,       // Ignores ItemRequirement needed
    HIDE_ERROR_MESSAGES,            // Don't show any error messages
    IGNORE_CANCELLED,               // Still runs even if event is cancelled
    ALERT_WHEN_READY,               // Sends caster a message when they are next able to cast
    SHOW_COOLDOWN                   // Show cooldown on item icon
}

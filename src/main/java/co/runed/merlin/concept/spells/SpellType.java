package co.runed.merlin.concept.spells;

public enum SpellType {
    NORMAL,             // Normal spell, triggers only when the trigger is active
    REPEATING,          // Repeating spell, triggers constantly based on frequency
    TOGGLE              // Toggle spell, toggles based on initial trigger and repeats based on frequency
}

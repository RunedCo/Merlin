package co.runed.merlin.concept.items;

public enum ItemRequirements {
    ALWAYS,                    // Always trigger regardless of equipped item
    MAIN_HAND,                 // Item is in Main Hand
    OFF_HAND,                  // Item is in Off Hand
    ANY_HAND,                  // Item is in Off Hand or Main Hand
    ARMOR,                     // Item is in any armor equipment slot
    EQUIPPED,                  // Item is in any EquipmentSlot
    INVENTORY,                 // Item is anywhere in inventory
}

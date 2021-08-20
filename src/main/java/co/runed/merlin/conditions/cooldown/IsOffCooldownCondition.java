package co.runed.merlin.conditions.cooldown;

import co.runed.bolster.util.cooldown.ICooldownSource;
import co.runed.bolster.common.properties.Properties;
import co.runed.merlin.abilities.Ability;
import co.runed.merlin.conditions.Condition;
import co.runed.merlin.conditions.IConditional;
import org.bukkit.ChatColor;

import java.text.DecimalFormat;

public class IsOffCooldownCondition extends Condition
{
    private static final DecimalFormat decimalFormatter = new DecimalFormat("#.#");

    @Override
    public boolean evaluate(IConditional conditional, Properties properties)
    {
        if (conditional instanceof Ability ability)
        {
            return !ability.isOnCooldown();
        }

        return false;
    }

    //TODO: REMOVE HARDCODED MESSAGE
    @Override
    public void onFail(IConditional conditional, Properties properties, boolean inverted)
    {

    }

    @Override
    public String getErrorMessage(IConditional conditional, Properties properties, boolean inverted)
    {
        double cooldown = ((ICooldownSource) conditional).getRemainingCooldown();
        String formattedCooldown = "" + (int) cooldown;
        String name = "Ability";

        if (conditional instanceof Ability ability)
        {
            String abilityName = ability.getName();

            if (abilityName != null) name = abilityName + ChatColor.RESET;
        }

        if (cooldown < 1)
        {
            formattedCooldown = decimalFormatter.format(cooldown);
        }

        if (inverted) return ChatColor.RED + name + " is not on cooldown!";

        return ChatColor.RED + name + ChatColor.RED + " on cooldown (" + formattedCooldown + " seconds remaining)";
    }
}

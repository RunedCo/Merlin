package co.runed.merlin.upgrade;

import co.runed.merlin.abilities.Ability;
import co.runed.merlin.core.AbilityProvider;
import co.runed.merlin.core.AbilityProviderType;

public class UpgradeProvider extends AbilityProvider
{
    String id;

    public void setId(String id)
    {
        this.id = id;
    }

    @Override
    public String getId()
    {
        return this.id;
    }

    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    public void onCastAbility(Ability ability, boolean success)
    {

    }

    @Override
    public AbilityProviderType getType()
    {
        return AbilityProviderType.UPGRADE;
    }

    @Override
    public void onToggleCooldown(Ability ability)
    {

    }

    @Override
    public void create()
    {

    }
}

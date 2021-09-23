package co.runed.merlin.triggers;

import co.runed.merlin.core.CastContext;

public abstract class BaseTrigger implements Trigger {
    private boolean cancelled = false;
    private CastContext context;

    @Override
    public CastContext getContext() {
        return context;
    }

    @Override
    public void setContext(CastContext context) {
        this.context = context;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
}

package co.runed.merlin.triggers;

import co.runed.merlin.core.CastContext;

public interface Trigger {
    CastContext getContext();

    void setContext(CastContext context);

    void setCancelled(boolean cancelled);

    boolean isCancelled();
}

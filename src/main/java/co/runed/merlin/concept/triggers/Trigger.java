package co.runed.merlin.concept.triggers;

import co.runed.merlin.concept.CastContext;

public interface Trigger {
    CastContext getContext();

    void setContext(CastContext context);

    void setCancelled(boolean cancelled);

    boolean isCancelled();
}

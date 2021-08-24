package co.runed.merlin.concept;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class Target<T> implements Iterable<T> {
    private List<T> _cachedValue = null;

    Supplier<Collection<T>> ignoreTargets = ArrayList::new;

    abstract Collection<T> getTargets();

    private Collection<T> getFilteredTargets() {
        Collection<T> ignored = ignoreTargets.get();

        return getTargets().stream().filter(t -> !ignored.contains(t)).toList();
    }

    public Target<T> ignoreIf(Supplier<Collection<T>> ignoreTargets) {
        this.ignoreTargets = ignoreTargets;

        return this;
    }

    public Target<T> run(Consumer<T> consumer) {
        for (T target : getFilteredTargets()) {
            consumer.accept(target);
        }

        return this;
    }

    public List<T> toList() {
        return new ArrayList<>(getFilteredTargets());
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        if (_cachedValue == null) _cachedValue = toList();

        return _cachedValue.iterator();
    }
}

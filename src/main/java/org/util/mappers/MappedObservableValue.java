package org.util.mappers;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.function.Function;

public class MappedObservableValue<C, T> implements ObservableValue<T> {

    private final ObservableValue<C> value;
    private final Function<C, T> to;
    private final Function<T, C> from;

    public MappedObservableValue(ObservableValue<C> value, Function<T, C> from, Function<C, T> to) {
        this.value = value;
        this.to = to;
        this.from = from;
    }

    @Override
    public void addListener(ChangeListener<? super T> changeListener) {
        ChangeListener<? super C> changeListener1 = new MappedChangeListener<T, C>((ChangeListener<T>) changeListener, this.from, this.to);
        this.value.addListener(changeListener1);
    }

    @Override
    public void removeListener(ChangeListener<? super T> changeListener) {
        ChangeListener<? super C> changeListener1 = new MappedChangeListener<T, C>((ChangeListener<T>) changeListener, this.from, this.to);
        this.value.removeListener(changeListener1);
    }

    @Override
    public T getValue() {
        return this.to.apply(this.value.getValue());
    }

    @Override
    public void addListener(InvalidationListener invalidationListener) {
        throw new IllegalStateException("Not implemented");
    }

    @Override
    public void removeListener(InvalidationListener invalidationListener) {
        throw new IllegalStateException("Not implemented");
    }
}

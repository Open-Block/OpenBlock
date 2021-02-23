package org.util.mappers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.function.Function;

public class MappedChangeListener<C, T> implements ChangeListener<T> {

    private final Function<T, C> to;
    private final Function<C, T> from;

    private final ChangeListener<C> listener;

    public MappedChangeListener(ChangeListener<C> listener, Function<C, T> from, Function<T, C> to) {
        this.to = to;
        this.from = from;
        this.listener = listener;
    }

    @Override
    public void changed(ObservableValue<? extends T> observableValue, T t, T t1) {
        var obValue = new MappedObservableValue<>((ObservableValue<T>) observableValue, this.from, this.to);
        this.listener.changed(obValue, this.to.apply(t), this.to.apply(t1));
    }
}

package org.util.mappers;

import javafx.beans.InvalidationListener;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.function.Function;

public class MappedBindedProperty<C, T> implements Property<T> {

    private final Property<C> property;
    private final Function<C, T> to;
    private final Function<T, C> from;

    public MappedBindedProperty(Property<C> property, Function<T, C> from, Function<C, T> to){
        this.property = property;
        this.from = from;
        this.to = to;
    }

    @Override
    public void bind(ObservableValue<? extends T> observableValue) {
        throw new IllegalStateException("Not Implemented");
    }

    @Override
    public void unbind() {
    this.property.unbind();
    }

    @Override
    public boolean isBound() {
        return this.property.isBound();
    }

    @Override
    public void bindBidirectional(Property<T> property) {
        this.property.bindBidirectional(new MappedBindedProperty<>(property, this.to, this.from));
    }

    @Override
    public void unbindBidirectional(Property<T> property) {
        this.property.unbindBidirectional(new MappedBindedProperty<>(property, this.to, this.from));
    }

    @Override
    public Object getBean() {
        return this.property.getBean();
    }

    @Override
    public String getName() {
        return this.property.getName();
    }

    @Override
    public void addListener(ChangeListener<? super T> changeListener) {

    }

    @Override
    public void removeListener(ChangeListener<? super T> changeListener) {

    }

    @Override
    public T getValue() {
        return null;
    }

    @Override
    public void setValue(T t) {

    }

    @Override
    public void addListener(InvalidationListener invalidationListener) {

    }

    @Override
    public void removeListener(InvalidationListener invalidationListener) {

    }
}

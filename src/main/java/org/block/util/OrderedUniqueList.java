package org.block.util;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.UnaryOperator;

public class OrderedUniqueList<T> extends ArrayList<T> {

    private BiPredicate<T, T> equalsCheck;
    private Comparator<T> order;

    public OrderedUniqueList(Comparator<T> order) {
        this(Object::equals, order);
    }

    public OrderedUniqueList(Collection<T> collection, Comparator<T> order) {
        this(collection, Objects::equals, order);
    }

    public OrderedUniqueList(BiPredicate<T, T> predicate, Comparator<T> order) {
        init(predicate, order);
    }

    public OrderedUniqueList(Collection<T> collection, BiPredicate<T, T> predicate, Comparator<T> order) {
        super(collection);
        init(predicate, order);
    }

    public void sort() {
        this.sort(this.order);
    }

    public <E> boolean contains(E obj, BiPredicate<E, T> predicate) {
        return this.parallelStream().anyMatch(v -> predicate.test(obj, v));
    }

    @Override
    public boolean contains(Object o) {
        try {
            T value = (T) o;
            return this.parallelStream().anyMatch(t -> this.equalsCheck.test(value, t));
        } catch (ClassCastException e) {
            return super.contains(o);
        }
    }

    @Override
    public T set(int index, T element) {
        if (this.parallelStream().anyMatch(v -> this.equalsCheck.test(v, element))) {
            this.remove(element);
            return this.remove(index);
        }
        T ret = super.set(index, element);
        this.sort(this.order);
        return ret;
    }

    @Override
    public boolean add(T t) {
        boolean add = super.add(t);
        this.sort(this.order);
        return add;
    }

    @Override
    public void add(int index, T element) {
        super.add(index, element);
        this.sort(this.order);
    }

    @Override
    public T remove(int index) {
        T ret = super.remove(index);
        this.sort(this.order);
        return ret;
    }

    @Override
    public boolean remove(Object o) {
        boolean ret = super.remove(o);
        this.sort(this.order);
        return ret;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean ret = super.addAll(c);
        this.sort(this.order);
        return ret;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        boolean ret = super.addAll(index, c);
        this.sort(this.order);
        return ret;
    }

    @NotNull
    @Override
    public ListIterator<T> listIterator(int index) {
        List<T> temp = new ArrayList<>(this);
        return temp.listIterator(index);
    }

    @NotNull
    @Override
    public ListIterator<T> listIterator() {
        List<T> temp = new ArrayList<>(this);
        return temp.listIterator();
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        List<T> temp = new ArrayList<>(this);
        return temp.iterator();
    }

    @Override
    public Spliterator<T> spliterator() {
        List<T> temp = new ArrayList<>(this);
        return temp.spliterator();
    }

    @Override
    public void replaceAll(UnaryOperator<T> operator) {
        super.replaceAll(operator);
        this.sort(this.order);
    }

    private void init(BiPredicate<T, T> predicate, Comparator<T> order) {
        this.equalsCheck = predicate;
        this.order = order;
    }
}

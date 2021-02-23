package org.block.util;

import java.util.function.Function;

public class ToStringWrapper<W> {

    private final W value;
    private final Function<W, String> toString;

    public ToStringWrapper(W value, Function<W, String> toString) {
        this.toString = toString;
        this.value = value;
    }

    public W getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object obj) {
        return this.value.equals(obj);
    }

    @Override
    public String toString() {
        return this.toString.apply(this.value);
    }
}

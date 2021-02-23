package org.vector;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class Vector<N extends Number, VSelf extends Vector<N, ?>> {

    protected BigDecimal[] points;
    protected Function<BigDecimal, N> toNumber;

    public Vector(Function<BigDecimal, N> function, BigDecimal... points) {
        this.points = points;
        this.toNumber = function;
    }

    public abstract <Num extends Number> Vector<Num, ?> toVector(Function<BigDecimal, Num> function);

    protected abstract VSelf createNew(BigDecimal... values);

    public VSelf plus(VSelf vector) {
        return action(vector, BigDecimal::add);
    }

    public VSelf minus(VSelf vector) {
        return action(vector, BigDecimal::subtract);
    }

    public VSelf divide(VSelf vector, RoundingMode mode) {
        return action(vector, (b, v) -> b.divide(v, mode));
    }

    public VSelf divide(BigDecimal amount, RoundingMode mode) {
        BigDecimal[] array = new BigDecimal[this.points.length];
        for (int A = 0; A < this.points.length; A++) {
            array[A] = this.points[A].divide(amount, mode);
        }
        return this.createNew(array);
    }

    public VSelf multiply(VSelf vector) {
        return action(vector, BigDecimal::multiply);
    }

    public VSelf multiply(BigDecimal amount) {
        BigDecimal[] array = new BigDecimal[this.points.length];
        for (int A = 0; A < this.points.length; A++) {
            array[A] = this.points[A].multiply(amount);
        }
        return this.createNew(array);
    }

    public VSelf multiply(Number number) {
        return multiply(BigDecimal.valueOf(number.doubleValue()));
    }

    public <Num extends Number, C extends Vector<Num, ?>> C toVector(VectorConverter.Specific<Num, C> converter) {
        return converter.convert(this);
    }

    public <Num extends Number, C extends Vector<Num, ?>> C toVector(Function<BigDecimal, Num> function, VectorConverter converter) {
        return (C) converter.convert(function, this);
    }

    public N getPoint(int A) {
        return this.toNumber.apply(getRawPoint(A));
    }

    public BigDecimal getRawPoint(int A) {
        return this.points[A];
    }

    public int getPointCount() {
        return this.points.length;
    }

    private VSelf action(VSelf vector, BiFunction<BigDecimal, BigDecimal, BigDecimal> function) {
        BigDecimal[] array = new BigDecimal[this.points.length];
        for (int A = 0; A < this.points.length; A++) {
            array[A] = function.apply(this.points[A], vector.getRawPoint(A));
        }
        return this.createNew(array);
    }
}

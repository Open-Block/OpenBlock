package org.vector.bounds;

import org.vector.Vector;
import org.vector.type.Vector2;

public interface Bound<V extends Vector<?, ?>> {

    class Bound2D<N extends Number> implements Bound<Vector2<N>>{

        private Vector2<N> point1;
        private Vector2<N> point2;

        public Bound2D(Vector2<N> vector, Vector2<N> vector2){
            this.point1 = vector;
            this.point2 = vector2;
        }

        @Override
        public Vector2<N> getPoint(int bound) {
            switch (bound){
                case 0: return this.point1;
                case 1: return this.point2;
                default: throw new IndexOutOfBoundsException("Bound");
            }
        }

        @Override
        public int getBoundSize() {
            return 2;
        }

    }

    V getPoint(int bound);
    int getBoundSize();

    default boolean contains(V vector) {
        for(int A = 0; A < vector.getPointCount(); A++) {
            if (!(vector.getRawPoint(A).doubleValue() <= this.getPoint(A).getRawPoint(A).doubleValue() && vector.getRawPoint(A).doubleValue() >= this.getPoint(A).getRawPoint(A).doubleValue())) {
                return false;
            }
        }
        return true;
    }
}

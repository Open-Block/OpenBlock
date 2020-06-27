package org.block.util;

import org.array.utils.ArrayUtils;

import java.util.*;
import java.util.function.Function;

public class ClassCompare {

    /**
     * When doing maths using different number classes, Java will give the outputted value as the best number class out of the provided. This allows the comparing of multiple values by the number class type
     * @param function This converts the provided data into a number class
     * @param collection The provided data
     * @param <T> The provided data type
     * @return The best value from the provided data
     */
    public static <T> Optional<T> compareNumberClasses(Function<T, Class<? extends Number>> function, Collection<T> collection){
        final Map<String, Integer> map = new HashMap<>();
        map.put(Byte.class.getSimpleName(), 0);
        map.put(Short.class.getSimpleName(), 1);
        map.put(Integer.class.getSimpleName(), 2);
        map.put(Long.class.getSimpleName(), 3);
        map.put(Float.class.getSimpleName(), 4);
        map.put(Double.class.getSimpleName(), 5);
        return ArrayUtils.getBest(b -> map.get(function.apply(b)), (c1, c2) -> c1 > c2, collection);
    }

    /**
     * Converts object based number class type to the primitive form
     * @param class1 The object based number class
     * @return The primitive class type
     * @throws IllegalArgumentException Unknown primitive form
     */
    public static Class<? extends Number> toPrimitive(Class<? extends Number> class1){
        if(class1.equals(Byte.class) || class1.equals(byte.class)){
            return byte.class;
        }
        if(class1.equals(Short.class) || class1.equals(short.class)){
            return short.class;
        }
        if(class1.equals(Integer.class) || class1.equals(int.class)){
            return int.class;
        }
        if(class1.equals(Float.class) || class1.equals(float.class)){
            return float.class;
        }
        if(class1.equals(Double.class) || class1.equals(double.class)){
            return double.class;
        }
        throw new IllegalArgumentException("Unknown primitive type");
    }
}

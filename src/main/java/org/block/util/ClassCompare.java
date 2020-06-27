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
}

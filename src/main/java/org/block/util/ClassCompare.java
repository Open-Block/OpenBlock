package org.block.util;

import org.array.utils.ArrayUtils;

import java.util.*;
import java.util.function.Function;

public class ClassCompare {


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

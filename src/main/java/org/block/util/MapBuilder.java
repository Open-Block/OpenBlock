package org.block.util;

import java.util.HashMap;
import java.util.Map;

public class MapBuilder<K, V> {

    private Map<K, V> map;

    public MapBuilder() {
        this(new HashMap<>());
    }

    public MapBuilder(Map<K, V> emptyMap) {
        this.map = emptyMap;
    }

    public MapBuilder<K, V> put(K key, V value) {
        this.map.put(key, value);
        return this;
    }

    public Map<K, V> build() {
        return this.map;
    }

    public <M extends Map<K, V>> M build(M map) {
        map.putAll(this.map);
        return map;
    }

    public static class MapArrayBuilder<K, V> extends MapBuilder<K, V[]> {

        public MapArrayBuilder() {
            super();
        }

        public MapArrayBuilder(Map<K, V[]> emptyMap) {
            super(emptyMap);
        }

        @Override
        public MapBuilder.MapArrayBuilder<K, V> put(K key, V... value) {
            return (MapArrayBuilder<K, V>) super.put(key, value);
        }
    }
}

package com.mycompany.sudoku;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class OurHashMap<K1, K2, V> extends HashMap<K1, HashMap<K2, V>> implements Serializable, Cloneable {

    private final Map<K1, Map<K2, V>> mMap;

    public OurHashMap() {
        mMap = new HashMap<>();
    }

    public V put(final K1 key1, final K2 key2, final V value) {
        Map<K2, V> map;
        if (mMap.containsKey(key1)) {
            map = mMap.get(key1);
            mMap.put(key1, map);
        } else {
            map = new HashMap<>();
            mMap.put(key1, map);
        }

        return map.put(key2, value);
    }

    public V get(final K1 key1, final K2 key2) {
        if (mMap.containsKey(key1)) {
            return mMap.get(key1).get(key2);
        } else {
            return null;
        }
    }

}

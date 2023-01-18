package ru.job4j.cache;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCache<K, V> {

    protected final Map<K, SoftReference<V>> cache = new HashMap<>();

    public void put(K key, V value) {
        cache.put(key, new SoftReference<>(value));
    }

    public V get(K key) {
        V inCache = null;
        for (K keys : cache.keySet()) {
            if (keys.toString().contains(key.toString())) {
                inCache = cache.getOrDefault(key, cache.get(keys)).get();
                if (inCache == null) {
                    cache.put(keys, new SoftReference<>(load(keys)));
                    inCache = load(key);
                }
            }
        }
    return inCache;
    }

    protected abstract V load(K key);

}
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
        V inCache = cache.getOrDefault(key,  new SoftReference<>(null)).get();
        if (inCache == null) {
            inCache = load(key);
            put(key, inCache);
        }
        return inCache;
    }

    protected abstract V load(K key);

}
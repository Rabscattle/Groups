package com.github.dschreid.groups.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractManager<K, I> {
    private final Map<K, I> objects = new HashMap<>();

    public I getOrCreate(K key) {
        key = normalize(key);
        if (contains(key)) {
            return objects.get(key);
        }

        I newOne = apply(key);
        this.objects.put(key, newOne);
        return newOne;
    }

    public boolean contains(K key) {
        key = normalize(key);
        return this.objects.containsKey(key);
    }

    public I get(K key) {
        key = normalize(key);
        return this.objects.get(key);
    }

    protected I remove(K key) {
        key = normalize(key);
        return this.objects.remove(key);
    }

    public Collection<I> getAll() {
        return this.objects.values();
    }

    /**
     * Creates a new object from a key which wil be used to populate
     */
    protected abstract I apply(K key);

    /**
     * Normalizes the key to a specific format used by the manager
     */
    protected K normalize(K key) {
        return key;
    }
}

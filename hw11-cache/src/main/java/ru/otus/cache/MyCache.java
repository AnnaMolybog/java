package ru.otus.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
    private final String PUT_ACTION = "put";
    private final String REMOVE_ACTION = "remove";
    private final String GET_ACTION = "get";

    private final Map<K, V> cache = new WeakHashMap<>();
    private final List<HwListener<K, V>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        this.cache.put(key, value);
        notify(key, value, PUT_ACTION);
    }

    @Override
    public void remove(K key) {
        V value = this.cache.remove(key);
        if (value != null) {
            notify(key, value, REMOVE_ACTION);
        }
    }

    @Override
    public V get(K key) {
        V value = this.cache.get(key);
        if (value != null) {
            notify(key, value, GET_ACTION);
        }
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        this.listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        this.listeners.remove(listener);
    }

    public int getCacheSize() {
        return cache.size();
    }

    private void notify(K key, V value, String action) {
        listeners.forEach(listener -> {
            try {
                listener.notify(key, value, action);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}

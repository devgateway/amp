package org.dgfoundation.amp.algo;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * an auto-vivificating map which delegates all of its calls and adds one more (getOrCreate())
 * @author Dolghier Constantin
 *
 */
public class VivificatingMap<K, V> implements Map<K, V> {
    protected final Map<K, V> map;
    protected final Function<K, V> defaultValueSupplier;

    public VivificatingMap(Map<K, V> map, final Supplier<V> defaultValueSupplier) {
        this.map = map;
        this.defaultValueSupplier = (k -> defaultValueSupplier.get());
    }
    
    public VivificatingMap(Map<K, V> map, Function<K, V> defaultValueSupplier) {
        this.map = map;
        this.defaultValueSupplier = defaultValueSupplier;
    }
    
    public V getOrCreate(K key) {
        return computeIfAbsent(key, defaultValueSupplier);
    }
    
    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return map.get(key);
    }

    @Override
    public V put(K key, V value) {
        return map.put(key, value);
    }

    @Override
    public V remove(Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        return map.values();
    }

    @Override
    public Set<java.util.Map.Entry<K, V>> entrySet() {
        return map.entrySet();
    }
    
    public Map<K, V> decapsulate() {
        return this.map;
    }
    
    @Override
    public String toString() {
        return String.format("VivificatingMap with defaultValueSupplier %s encapsulating: %s", defaultValueSupplier, this.map);
    }
}

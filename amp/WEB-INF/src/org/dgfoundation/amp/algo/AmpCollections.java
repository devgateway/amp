package org.dgfoundation.amp.algo;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.digijava.module.aim.util.Identifiable;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class AmpCollections {
    
    /**
     * returns a map of all the input values
     * keys appear in map in input order; on each level values appear in input order
     * @param values
     * @param mapper
     * @return
     */
    public static<K, V> Map<K, List<V>> distribute(Collection<V> values, Function<V, K> mapper) {
        Map<K, List<V>> res = new TreeMap<K, List<V>>();
        for(V v:values)
        {
            K key = mapper.apply(v);
            if (!res.containsKey(key))
                res.put(key, new ArrayList<V>());
            res.get(key).add(v);
        }
        return res;
    }
    
    public static final Function<Identifiable, Long> BY_ID_DISTRIBUTION = from -> (Long) from.getIdentifier(); 
    
    /**
     * 
     * merges N lists
     * @param lists
     * @return
     */
    @SafeVarargs
    public static<V> List<V> mergeLists(Iterable<V>... lists) {
        return Lists.newArrayList(Iterables.concat(lists));
    }
    
    /**
     * returns null if both are null, else non-null < null, else a.compareTo(b)
     * @param a
     * @param b
     * @return
     */
    public static Integer nullCompare(Comparable a, Comparable b) {
        if (a == null && b == null)
            return null;
        if (a == null)
            return 1;
        if (b == null)
            return -1;
        
        return a.compareTo(b);
    }
    
    /**
     * safely gets an element of an array
     * @param row
     * @param j
     * @return -1 if element is offbounds; 0 if element is null; elementvalue else
     */
    public static long safeGet(List<Long> row, int j) {
        if (j < 0 || j >= row.size())
            return 0;
        
        Long z = row.get(j);
        if (z == null)
            return 0;
        
        return z.longValue();
    }
    
    /**
     * returns the iteration order of an element in a collection. The equivalent of {@link List#indexOf(Object)}. <br />
     * Please notice that the result of this function is meaningful only if the underlying collection iterates in a consistent way
     * @param coll
     * @param elem
     * @return
     */
    public static<K> int indexOf(Collection<K> coll, K elem) {
        int pos = 0;
        Iterator<K> it = coll.iterator();
        while (it.hasNext()) {
            if (it.next().equals(elem))
                return pos;
            pos ++;
        }
        return -1;
    }
    
    /**
     * returns an ordered set which is the union of 2 collections. The elements in the result are contained in the same order as (s1 + <any in s2 which are not contained in s1>)
     * @param s1
     * @param s2
     * @return
     */
    public static<K> LinkedHashSet<K> union(Collection<K> s1, Collection<K> s2) {
        LinkedHashSet<K> res = new LinkedHashSet<>(s1);
        for(K k:s2) {
            if (!res.contains(k))
                res.add(k);
        }
        return res;
    }

    /**
     * returns an ordered set which contains one set prefixed by all the elements in an another set which are not present in the first set
     * @param prefix
     * @param s2
     * @return
     */
    public static<K> LinkedHashSet<K> prefixUnion(Collection<K> prefix, Set<K> s2) {
        LinkedHashSet<K> res = new LinkedHashSet<>();
        
        for(K k:prefix)
            if (!s2.contains(k))
                res.add(k);
        
        res.addAll(s2);
        return res;
    }
    
    /**
     * returns an ordered set which is the difference of 2 collections. The elements in the result are contained in the same order as (s1 + <any in s2 which are not contained in s1>)
     * @param s1
     * @param s2
     * @return
     */
    public static<K> LinkedHashSet<K> difference(Collection<K> s1, Collection<K> s2) {
        LinkedHashSet<K> res = new LinkedHashSet<>();
        res.addAll(s1);
        res.removeAll(s2);
        return res;
    }

    /**
     * returns a map which is the result of applying a function to all the elements of a map
     * @param in
     * @param mapper
     * @param out
     * @return
     */
    public static<K, V, Z> Map<K, Z> remap(Map<K, V> in, BiFunction<K, V, Z> mapper, Map<K, Z> out) {
        if (out == null)
            out = new HashMap<>();
        for(Entry<K, V> entry:in.entrySet())
            out.put(entry.getKey(), mapper.apply(entry.getKey(), entry.getValue()));
        return out;
    }

    /**
     * A short hand version of {@link #remap(Map, Function, Map)}.
     */
    public static <K, V, Z> Map<K, Z> remap(Map<K, V> in, Function<V, Z> mapper) {
        return remap(in, mapper, null);
    }

    /**
     * returns a map which is the result of applying a function to all the values of a map
     * @param in
     * @param mapper
     * @param out
     * @return
     */
    public static<K, V, Z> Map<K, Z> remap(Map<K, V> in, Function<V, Z> mapper, Map<K, Z> out) {
        if (out == null)
            out = new HashMap<>();
        for(Entry<K, V> entry:in.entrySet())
            out.put(entry.getKey(), mapper.apply(entry.getValue()));
        return out;
    }
    
    /**
     * remaps both keys and values
     * @param in
     * @param keyMapper
     * @param valueMapper
     * @param sorted
     * @return
     */
    public static<K, V, A, B> Map<A, B> remap(Map<K, V> in, Function<K, A> keyMapper, Function<V, B> valueMapper, boolean sorted) {
        Map<A, B> res = sorted ? new TreeMap<>() : new LinkedHashMap<>();
        in.forEach((k, v) -> 
            res.put(keyMapper.apply(k), valueMapper.apply(v)));
        return res;
    }
    
    /**
     * @param in
     * @param keysToKeep
     * @return
     */
    public static<K, V> Map<K, V> keepEntries(Map<K, V> in, Set<K> keysToKeep) {
        return in.entrySet().stream().filter(z -> keysToKeep.contains(z.getKey())).collect(Collectors.toMap(z -> z.getKey(), z -> z.getValue()));
    }
    
    /**
     * mutates in-place a map 
     * @param in
     * @param keysToKeep
     */
    public static<K, V> void mutableKeepEntries(Map<K, V> in, Set<K> keysToKeep) {
        Iterator<Map.Entry<K, V>> it = in.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<K, V> z = it.next();
            if (!keysToKeep.contains(z.getKey()))
                it.remove();
        }
    }
    
    /**
     * returns a sorted-map-view of the input map, with its keys sorted according to a given comparator
     * @param in
     * @param comp
     * @return
     */
    public static<K, V> SortedMap<K, V> sortedMap(Map<K, V> in, Comparator<K> comp) {
        SortedMap<K, V> res = new TreeMap<K, V>(comp);
        res.putAll(in);
        return res;
    }
    
    public static<K, V> List<V> relist(Collection<K> in, Function<K, V> func) {
        return in.stream().map(func).collect(Collectors.toList());
    }
    
    public static<K, V> Map<K, V> map(List<K> in, Function<K, V> func) {
        return in.stream().collect(Collectors.toMap(z -> z, z -> func.apply(z)));
    }
    
    public static<K, V> Set<V> mapToSet(List<K> in, Function<K, V> func) {
        return in.stream().map(func).collect(Collectors.toSet());
    }

    /**
     * returns a list containing the input elements in sorted order
     * @param in
     * @return
     */
    public static<K> List<K> sorted(Collection<K> in) {
        return sorted(in, null);
    }
    
    /**
     * sorts the input list in a copy
     * @param in
     * @return
     */
    public static<K> List<K> sorted(Collection<K> in, Comparator<K> comp) {
        if (in.isEmpty())
            return Collections.emptyList();
        
        if (in.size() == 1)
            return Arrays.asList(in.iterator().next());
        
        ArrayList<K> res = new ArrayList<>(in);
        res.sort(comp);
        return res;
    }

    /**
     * chooses an arbitrary element from a collection. If the collection is empty, returns a given element
     * @param col
     * @param defaultValue
     * @return
     */
    public static<K> K any(Collection<K> col, K defaultValue) {
        if (col.isEmpty())
            return defaultValue;
        return col.iterator().next();
    }
    
    /**
     * returns a Comparable token which would compare the elements of a list in given order
     * @param col
     * @return
     */
    public static<K extends Comparable<?>> Comparable<?> orderedListWrapper(List<K> col) {
        return new ComparableList<K>(col);
    }
    
    /**
     * returns a mutable result of (init - minus + plus)
     * @param a
     * @param b
     * @return
     */
    public static<K> Set<K> minusPlus(Set<K> init, Set<K> minus, Set<K> plus) {
        Set<K> res = new HashSet<>(init);
        res.removeAll(minus);
        res.addAll(plus);
        return res;
    }
    
    /**
     * returns a predicate which is true iff ANY of its inputs is true. If input is empty or null, returns a "true" predicate
     * @param in
     * @return
     */
    public static<K> Predicate<K> orPredicates(List<Predicate<K>> in) {
        if (in == null || in.isEmpty())
            return (z -> true);

        return k -> in.stream().anyMatch(p -> p.test(k));
    }
    
    /**
     * returns a predicate which is true iff all of its inputs are true. If input is empty or null, returns a "true" predicate
     * @param in
     * @return
     */
    public static<K> Predicate<K> mergePredicates(List<Predicate<K>> in) {
        if (in == null || in.isEmpty())
            return (z -> true);
        
        Predicate<K> res = in.get(0);
        for(int i = 1; i < in.size(); i++)
            res = res.and(in.get(i));
        
        return res;
    }
    
    /**
     * returns a list containing the elements of a list which pass a filter
     * @param in
     * @param pred
     * @return
     */
    public static<K> List<K> filter(List<K> in, Predicate<K> pred) {
        return in.stream().filter(pred).collect(Collectors.toList());
    }
    
    /**
     * returns a readonly list containing the result of adding an element to a list
     * @param list
     * @param elem
     * @return
     */
    public static<K> List<K> cat(List<K> list, K elem) {
        List<K> res = new ArrayList<>(list);
        res.add(elem);
        return Collections.unmodifiableList(res);
    }
    
    /**
     * returns the first of the values which is not null
     * @param a
     * @param b
     * @return
     */
    public static<K> K firstOf(K...vals) {
        for(K k:vals) {
            if (k != null)
                return k;
        }
        return null;
    }

    /**
     * A stateful predicate that can be used to filter distinct objects by some key.
     * @param keyExtractor key extractor
     * @param <T> type of filtered object
     * @return predicate that will return true if the object was seen for the first time
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), true) == null;
    }
}

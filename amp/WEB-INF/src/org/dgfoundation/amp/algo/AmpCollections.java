package org.dgfoundation.amp.algo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.digijava.module.aim.util.Identifiable;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

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
	 * returns an ordered set which is the union of 2 union. The elements in the result are contained in the same order as (s1 + <any in s2 which are not contained in s1>)
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static<K> LinkedHashSet<K> union(Collection<K> s1, Collection<K> s2) {
		LinkedHashSet<K> res = new LinkedHashSet<>();
		res.addAll(s1);
		for(K k:s2) {
			if (!res.contains(k))
				res.add(k);
		}
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
}

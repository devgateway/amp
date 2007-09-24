package org.dgfoundation.amp.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for collections.
 * will add other methods soon.
 * @author Irakli Kobiashvili
 *
 */
public class AmpCollectionUtils {

	/**
	 * Interface that know how to resolve key of type K from element of Tye E.
	 * Used in createMap() method.
	 *
	 * @param <K> Key type
	 * @param <E> element type
	 * 
	 */
	public static interface KeyResolver<K,E>{
		K resolveKey(E element);
	}
	
	
	/**
	 * Creates Map object from Collection.
	 * @param <K> type of the key of elements..
	 * @param <E> type of element in collection and map
	 * @param col collection of E's
	 * @param resolver interface implementation which 'knows' how to resolve key from element
	 * @return Map object where keys are of type K and values are E
	 */
	public static <K,E> Map<K, E> createMap(Collection<E> col, KeyResolver<K, E> resolver){
		Map<K,E> result=new HashMap<K, E>();
		for (E element : col){
			result.put(resolver.resolveKey(element), element);
		}
		return result;
	}
	
}

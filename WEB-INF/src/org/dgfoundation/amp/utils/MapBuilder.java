package org.dgfoundation.amp.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.digijava.module.aim.util.ActivityUtil;

/**
 * Constructs Map objects from collections.
 * Sublcasses should specify how to reolve key from element 
 * by implementing abstract {@link #resolveKey(Object)} method.
 * @author Irakli Kobiashvili
 * @see ActivityUtil.CategoryIdRefDocMapBuilder
 *
 * @param <K> key type for map
 * @param <E> element type for map
 */
public abstract class MapBuilder<K,E> {
	
	/**
	 * Creates Map from colection.
	 * 
	 * @param col collection of elements to put in resulting map object, null not permited
	 * @return HashMap containing elements from col
	 */
	public Map<K, E> createMap(Collection<E> col){
		HashMap<K, E> map=new HashMap<K, E>();
		for(E e : col){
			K key=resolveKey(e);
			map.put(key, e);
		}
		return map;
	}
	
	/**
	 * retrives key from element.
	 * @param e element to retrive key from.
	 * @return key object for element.
	 */
	public abstract K resolveKey(E e);
	
	
}

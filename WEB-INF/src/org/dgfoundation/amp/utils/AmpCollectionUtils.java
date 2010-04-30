package org.dgfoundation.amp.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Utility class for collections.
 * will add other methods soon.
 * @author Irakli Kobiashvili
 *
 */
public class AmpCollectionUtils {

	/**
	 * Interface that knows how to resolve key of type K from element of type E.
	 * Used in createMap() method.
	 *
	 * @param <K> Key type
	 * @param <E> element type
	 * 
	 */
	public static interface KeyResolver<K,E>{
		K resolveKey(E element);
	}

	public static interface KeyWorker<K,E> extends KeyResolver<K,E>{
		void updateKey(E element,K newKey);
		void markKeyForRemoval(E element);
	}
	
	/**
	 * Creates Map object from Collection.
	 * Uses {@link HashMap} implementation of Map
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

	/**
	 * Creates set of keys.
	 * First creates map using createMap() method in this util and then returns set of keys from that map.
	 * @param <K> key or ID type of the element
	 * @param <E> element type
	 * @param col collection of E elements
	 * @param resolver key resolver interface implementation
	 * @return
	 */
	public static <K,E> Set<K> getKeys(Collection<E> col,KeyResolver<K, E> resolver){
		return createMap(col, resolver).keySet();
	}
	
	/**
	 * Returns array of keys of all elements.
	 * This one does not use Map to get keys. 
	 * @param <K> key or ID type of the element
	 * @param <E> element type
	 * @param col collection of E elements
	 * @param resolver key resolver interface implementation
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <K,E> K[] getIdsArray(Collection<E> col,KeyResolver<K, E> resolver){
		List<K> result=new ArrayList<K>(col.size());
		for (E element : col) {
			K key=resolver.resolveKey(element);
			result.add(key);
		}
		return (K[])result.toArray();
	}

	/**
	 * Search for element with key.
	 * K should implement equals() method correctly.
	 * @param <K>
	 * @param <E>
	 * @param col
	 * @param key
	 * @param resolver
	 * @return
	 */
	public static <K,E> E searchByKey(Collection<E> col,K key,KeyResolver<K, E> resolver){
		for (E element : col) {
			K elementKey=resolver.resolveKey(element);
			if (key.equals(elementKey)) return element;
		}
		return null;
	}
	
	/**
	 * Return new collection based on comparing elements in two other collections.
	 * sesCol is used to iterate all elements and dbCol to check for IDs in.
	 * If same ID exists in both, dbCol element will go in result collection.
	 * If ID is only in dbCol then it will NOT go in result collection.
	 * If ID is only in sesCol then it will go in result collection. 
	 * Please note that this method returns new collection object and if used with Hibernate in some cases
	 * may result in error which says that collection cannot be dereferenced.
	 * @param <E> Element type
	 * @param <K> Element ID type.
	 * @param sesCol main collection to iterate for elements. Usually this is one from session or form. 
	 * @param dbCol collection which is used as reference to compare changes. usually this one is old state in db.
	 * @param keyResolver class which implements {@link KeyResolver} and knows how to resolve ID property of type K from E.
	 * @return
	 */
	public static <E,K> Collection<E> joinInNew(Collection<E> sesCol,Collection<E> dbCol, KeyResolver<K, E> keyResolver) {
		Map<K,E> oldEmap = createMap(dbCol, keyResolver);
		Collection<E> result = new LinkedList<E>();
		for (E newE : sesCol) {
			E oldE = oldEmap.get(keyResolver.resolveKey(newE));
			if (oldE == null){
				result.add(newE);
			}else{
				result.add(oldE);
			}
		}
		return result;
	}
	
	/**
	 * Joins two collection using rules of synchronization of session and db collections.
	 * Result is same mainCol object but updated according refCol with following rules :
	 * if E is in both collection then it is moved from refCol to resulting mainCol.
	 * if E is only in mainCol then it is removed from it - from result.
	 * if E is only in refCol then it is added to resulting mainCol.
	 * E is element specified (and compared) by K type property which usually is ID of entity bean.
	 * @param <E> type of elements in collections
	 * @param <K> type of PK of entity bean or type of some property that is used to compare elements.
	 * @param mainCol collection that would be updated according to refCol. usually this is collection of db.
	 * @param refCol collection of reference. usually this is new state from ActionForm or session.
	 * @param keyResolver
	 * @return
	 */
	public static <E, K> Collection<E> join(Collection<E> mainCol, Collection<E> refCol, KeyResolver<K, E> keyResolver){
		Map<K, E> mapEref = createMap(refCol, keyResolver);
		Iterator<E> iterEmain = mainCol.iterator();
		while (iterEmain.hasNext()) {
			E mainE = (E) iterEmain.next();
			K mainK = keyResolver.resolveKey(mainE);
			E refE = mapEref.get(mainK);
			if (refE == null){
				iterEmain.remove();
			}else{
				mapEref.remove(mainK);
			}
		}
		mainCol.addAll(mapEref.values());

		for (E e : refCol) {
			K key = keyResolver.resolveKey(e);
			if (null == key){
				mainCol.add(e);
			}
		}
		
		return mainCol;
	}

	/**
	 * Joins two collections in mainCol marking elements that exists only in mainCol for removal.
	 * How to mark elements is defined by {@link KeyWorker} interface implementation. 
	 * Usually id values are set to same but negative, this let us know which should be deleted or updated or inserted.
	 * @param <E>
	 * @param <K>
	 * @param mainCol
	 * @param refCol
	 * @param keyWorker
	 * @return
	 */
	public static <E, K> Collection<E> joinAndMarkRemoved(Collection<E> mainCol, Collection<E> refCol, KeyWorker<K, E> keyWorker){
		Map<K, E> mapEref = createMap(refCol, keyWorker);
		Iterator<E> iterEmain = mainCol.iterator();
		while (iterEmain.hasNext()) {
			E mainE = (E) iterEmain.next();
			K mainEkey = keyWorker.resolveKey(mainE);
			E refE = mapEref.get(mainEkey);
			if (refE == null){
				keyWorker.markKeyForRemoval(mainE);
			}else{
				iterEmain.remove();
				//mapEref.remove(mainEkey);
			}
		}
		
		mainCol.addAll(mapEref.values());
		
		for (E e : refCol) {
			K key = keyWorker.resolveKey(e);
			if (null == key){
				mainCol.add(e);
			}
		}
		return mainCol;
	}
	
	public static <K,E> Collection<E> split(Collection<E> mainCol,Collection<E> refCol,KeyWorker<K, E> keyWorker){
		Collection<E> deleted = null;
		Map<K, E> mapEref = createMap(refCol, keyWorker);
		Iterator<E> iterEmain = mainCol.iterator();
		while (iterEmain.hasNext()) {
			E mainE = (E) iterEmain.next();
			E refE = mapEref.get(keyWorker.resolveKey(mainE));
			if (refE == null){
				iterEmain.remove();
				if (deleted == null){
					deleted = new ArrayList<E>();
				}
				deleted.add(mainE);
			}else{
				mapEref.remove(keyWorker.resolveKey(mainE));
			}
		}
		mainCol.addAll(mapEref.values());

		for (E e : refCol) {
			K key = keyWorker.resolveKey(e);
			if (null == key){
				mainCol.add(e);
			}
		}
		
		return deleted;
	}
	
}

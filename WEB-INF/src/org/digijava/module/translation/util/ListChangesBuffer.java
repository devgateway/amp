package org.digijava.module.translation.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.utils.AmpCollectionUtils.KeyResolver;

/**
 * Buffers changes to list.
 * If there are several changes to same list item E, then this will store only last one.
 * @author Irakli Kobiashvili
 *
 * @param <K> key of elements stored in list
 * @param <E> elements stored in the list
 */
public class ListChangesBuffer<K,E> {
	private KeyResolver<K, E> keyResolver = null;
	private Map<K, ChangedItem<K, E>> itemsByKey = null;
	
	public ListChangesBuffer(KeyResolver<K, E> keyResolver){
		this.keyResolver = keyResolver;
		this.itemsByKey = new HashMap<K, ChangedItem<K,E>>();
	}
	
	public void operationAdd(E element){
		operation(Operation.ADD,element);
	}

	public void operationEdit(E element){
		operation(Operation.EDIT,element);
	}
	
	public void operationDelete(E element){
		operation(Operation.DELETE,element);
	}
	
	private void operation(Operation oper,E element){
		K key = keyResolver.resolveKey(element);
		ChangedItem<K, E> operation = new ChangedItem<K, E>(oper, key, element);
		this.itemsByKey.put(key, operation);
	}
	
	public void undoOperation(E element){
		K key = keyResolver.resolveKey(element);
		undoOperationFor(key);
	}
	public void undoOperationFor(K key){
		this.itemsByKey.remove(key);
	}
	
	public List<ChangedItem<K, E>> listChanges(){
		List<ChangedItem<K, E>> result = new ArrayList<ChangedItem<K,E>>(this.itemsByKey.values());
		return result;
	}
	
	public List<E> marge(List<E> with){
		List<E> result = new ArrayList<E>();
		List<E> toAdd = new ArrayList<E>();
		for (Iterator<E> iterator = with.iterator(); iterator.hasNext();) {
			E external = iterator.next();
			K key = keyResolver.resolveKey(external);
			ChangedItem<K, E> operation = this.itemsByKey.get(key);
			if (operation!=null){
				//If we have operation registered for this item
				if (operation.getOperation() == Operation.ADD){
					toAdd.add(operation.getElement());
				}
				if(operation.getOperation() == Operation.EDIT){
					result.add(operation.getElement());
				}
				if(operation.getOperation() == Operation.DELETE){
					//Do nothing, just do not put this item in result list
				}
			}else{
				//if we do not have operation for this item then just move it to result
				result.add(external);
			}
		}
		result.addAll(toAdd);
		return result;
	}
	
	/**
	 * Possible operations on the list item.
	 * @author Irakli Kobiashvili
	 *
	 */
	public static enum Operation{ADD,EDIT,DELETE};
	
	/**
	 * Change to one particular item.
	 * @author Irakli Kobiashvili
	 *
	 * @param <K>
	 * @param <E>
	 */
	public static class ChangedItem<K,E>{
		private E element;
		private K key;
		private Operation operation;
		
		public ChangedItem(Operation oper,K key,E element){
			this.element=element;
			this.operation=oper;
			this.key=key;
		}

		public K getKey() {
			return key;
		}

		public E getElement() {
			return element;
		}

		public Operation getOperation() {
			return operation;
		}
		
		public String getOperationName(){
			return this.operation.toString();
		}
	}
}

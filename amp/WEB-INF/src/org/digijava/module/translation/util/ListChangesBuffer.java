package org.digijava.module.translation.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.utils.AmpCollectionUtils.KeyResolver;
import org.digijava.kernel.persistence.WorkerException;

/**
 * Buffers changes to list.
 * If there are several changes to same list item E, then this will store only last one.
 * @author Irakli Kobiashvili
 *
 * @param <K> type of keys of elements stored in list
 * @param <E> type of elements stored in the list
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

	public void operationUpdate(E element){
		operation(Operation.UPDATE,element);
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
		
	public void clear(){
		this.itemsByKey.clear();
	}
	
	public List<ChangedItem<K, E>> listChanges(){
		List<ChangedItem<K, E>> result = new ArrayList<ChangedItem<K,E>>(this.itemsByKey.values());
		return result;
	}
	
	/**
	 * Returns elements of only specified operation.
	 * @param operation
	 * @return
	 */
	public List<E> elements(Operation operation){
		ArrayList<E> result = new ArrayList<E>(this.itemsByKey.size());
		List<ChangedItem<K, E>> allChanges = this.listChanges();
		for (ChangedItem<K, E> changedItem : allChanges) {
			if (operation.equals(changedItem.getOperation())){
				result.add(changedItem.getElement());
			}
		}
		return result;
	}
	
	/**
	 * Merges buffered changes with the list.
	 * Deleted elements are removed from result list.
	 * Added elements are adder at the beginning of result list.
	 * Updated elements are replaced with new version is result list.
	 * @param with
	 * @return
	 */
	public List<E> merge(List<E> with){
		List<E> result = new ArrayList<E>();
		List<E> tempResult = new ArrayList<E>();
		for (Iterator<E> iterator = with.iterator(); iterator.hasNext();) {
			E external = iterator.next();
			K key = keyResolver.resolveKey(external);
			ChangedItem<K, E> operation = this.itemsByKey.get(key);
			if (operation!=null){
				//If we have operation registered for this item
				if(operation.getOperation() == Operation.UPDATE){
					//if element is edited then use one from buffer.
					tempResult.add(operation.getElement());
				}
				if(operation.getOperation() == Operation.DELETE){
					//Do nothing, just do not put this item in result list
				}
			}else{
				//if we do not have operation for this item then just move it to result
				tempResult.add(external);
			}
		}
		//first put all added elements
		result.addAll(this.elements(Operation.ADD));
		//then add edited and removed 
		result.addAll(tempResult);
		return result;
	}
	
	/**
	 * Fixes all buffered changes.
	 * @param fixer
	 * @throws WorkerException 
	 */
	public void fixChnages(OperationFixer<E> fixer) throws WorkerException{
		List<ChangedItem<K, E>> changes = listChanges();
		if (changes!=null && changes.size()>0){
			try {
				//prepare
				fixer.start();
				//do actions
				for (ChangedItem<K, E> changedItem : changes) {
					Operation operation = changedItem.getOperation();
					E element = changedItem.getElement();
					if (Operation.ADD.equals(operation)){
						fixer.add(element);
					}
					if (Operation.UPDATE.equals(operation)){
						fixer.update(element);
					}
					if (Operation.DELETE.equals(operation)){
						fixer.delete(element);
					}
				}
				//finalize
				fixer.end();
			} catch (WorkerException e) {
				try {
					//process error
					fixer.error();
				} catch (Exception e1) {
					throw new WorkerException("Error when reporting erro to changes fixer.", e1);
				}
				throw new WorkerException("Error when fixing changes to db", e);
			}
		}
	}

	/**
	 * Implementors "know" how to fix each change operation. 
	 * @author Irakli Kobiashvili
	 *
	 * @param <E> type of elements
	 */
	public static interface OperationFixer<E>{
		
		/**
		 * Starts fixing of changes.
		 * Use this to prepare storage.
		 * For example start transaction, open file, or make connection.
		 * @throws WorkerException
		 */
		void start() throws WorkerException;
		
		void add(E element) throws WorkerException;
		void update(E element) throws WorkerException;
		void delete(E elemenet) throws WorkerException;
		
		/**
		 * End fix process. 
		 * Finish working with store.
		 * For Example commit transaction, close file or make connection.
		 * @throws WorkerException
		 */
		void end() throws WorkerException;
		
		/**
		 * This is called when error happens at any stage during fix process.
		 * Use this to rollback transaction, or delete/restore file. 
		 * @throws WorkerException
		 */
		void error() throws WorkerException;
	}
	
	/**
	 * Possible operations on the list item.
	 * @author Irakli Kobiashvili
	 *
	 */
	public static enum Operation{ADD,UPDATE,DELETE};
	
	/**
	 * Change to one particular item.
	 * @author Irakli Kobiashvili
	 *
	 * @param <K> key type
	 * @param <E> element type
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

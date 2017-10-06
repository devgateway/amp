package org.digijava.module.translation.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.utils.AmpCollectionUtils.KeyResolver;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.util.TrnUtil;

/**
 * Buffers changes to list.
 * Elements in buffer are stored by their keys (Map) so it can contain only one operation on one element.
 * If client adds element with same key several time switch different operations then only last will be stored.
 * This can be used to buffer changes like add, update, delete on list and later stored changes.
 * Also it is possible to undo each operation, see list of performed operations and merge changes with original list.
 * Merging is done by removing deleted elements from list, adding new elements to the list and replacing updated elements.
 * This class is not thread safe and because usually it's instances live in HTTP Session, it requires synchronization on client side.
 * To avoid problems use correct method. For example to undo some operation use only {@link #undoOperation(Object)} method.
 * @author Irakli Kobiashvili
 *
 * @param <K> type of keys of elements stored in list
 * @param <E> type of elements stored in the list
 */
public class ListChangesBuffer<K,E> {
    private KeyResolver<K, E> keyResolver = null;
    private Map<K, ChangedItem<K, E>> elementsByKey = null;
    
    /**
     * Constructs list buffer.
     * You have to pass key resolver to constructor to let it know how to 
     * resolve keys from elements.
     * @param keyResolver
     */
    public ListChangesBuffer(KeyResolver<K, E> keyResolver){
        this.keyResolver = keyResolver;
        this.elementsByKey = new HashMap<K, ChangedItem<K,E>>();
    }
    
    /**
     * Add element to the list.
     * Element with ADD operation will be placed in the buffer.
     * This new element will be visible when buffer is merged with original list.
     * Also it will be visible in the changes list and can be undone at any time.
     * @param element element to add to the list.
     */
    public void operationAdd(E element){
        operation(Operation.ADD,element);
    }

    /**
     * Update element in the list.
     * Element with UPDATE operation will be placed in the buffer.
     * This element will replace corresponding element (which has same key) in the
     * original list when buffer and the list are merged.
     * The element will also show up in changes list and can be undone at any time.
     * Note that if you are changing fields of the element in original list as 
     * result of its update, then there is no mean using this method. 
     * This will be useful only if update means new instance of the 
     * element with new values of the fields.  
     * @param element updated element of the list.
     */
    public void operationUpdate(E element){
        operation(Operation.UPDATE,element);
    }
    
    /**
     * Delete element from list.
     * Element with DELETE operation will be placed in the buffer.
     * When buffer is merged with original list, element with same
     * key will be removed from merge result - it will be appear as deleted.
     * This element with its operation will be visible in the changes list from
     * where it can be undone at any time.
     * @param element element to remove from the list
     */
    public void operationDelete(E element){
        operation(Operation.DELETE,element);
    }
    
    /**
     * Associates element with operation and add to the buffer. 
     * @param oper operation performed on the element
     * @param element element instance
     */
    private void operation(Operation oper,E element){
        K key = keyResolver.resolveKey(element);
        ChangedItem<K, E> operation = new ChangedItem<K, E>(oper, key, element);
        this.elementsByKey.put(key, operation);
    }
    
    /**
     * Undo Operation on specified element.
     * Change for the element is removed from buffer.
     * To find change of the element, its key is resolved and used for search.
     * So if you pass element instance which is not from the buffer but 
     * has same key value then it will remove from buffer with same key value.
     * @param element
     * @return undone change
     */
    public ChangedItem<K, E> undoOperation(E element){
        K key = keyResolver.resolveKey(element);
        return undoOperationFor(key);
    }
    
    /**
     * Undo operation on element specified by key
     * @param key
     * @return change that was undone
     * @see #undoOperation(Object)
     */
    public ChangedItem<K, E> undoOperationFor(K key){
        return this.elementsByKey.remove(key);
    }
    
    /**
     * Clears buffer.
     * All changes are removed
     */
    public void clear(){
        this.elementsByKey.clear();
    }
    
    /**
     * Lists all changes.
     * @return list of {@link ChangedItem} beans.
     */
    public List<ChangedItem<K, E>> listChanges(){
        List<ChangedItem<K, E>> result = new ArrayList<ChangedItem<K,E>>(this.elementsByKey.values());
        return result;
    }
    
    /**
     * Checks if element is in buffer.
     * Key for the element is resolved and used to search.
     * @param element
     * @return
     */
    public boolean contains(E element){
        K key = this.keyResolver.resolveKey(element);
        return this.containsKey(key);
    }

    /**
     * Check if element with specified key exists in the buffer.
     * @param key
     * @return
     */
    public boolean containsKey(K key){
        return this.elementsByKey.containsKey(key);
    }
    
    /**
     * Get operation associated with element.
     * @param element element whos operation should be cheked
     * @return value of operation with which chngenhas been associated, null otherwise.
     */
    public Operation getOperation(E element){
        K key = this.keyResolver.resolveKey(element);
        return getOperationByKey(key);
    }
    
    /**
     * Get operation associated with element.
     * @param key
     * @return
     */
    public Operation getOperationByKey(K key){
        ChangedItem<K, E> element = this.elementsByKey.get(key);
        if (element!=null){
            return element.getOperation();
        }
        return null;
    }
    
    /**
     * Returns elements of only specified operation.
     * @param operation
     * @return
     */
    public List<E> elements(Operation operation){
        ArrayList<E> result = new ArrayList<E>(this.elementsByKey.size());
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
            ChangedItem<K, E> operation = this.elementsByKey.get(key);
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
    public void fixChnages(OperationFixer<E> fixer) {
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
            } catch (Exception e) {
                try {
                    //process error
                    fixer.error();
                } catch (Exception e1) {
                    throw new RuntimeException("Error when reporting erro to changes fixer.", e1);
                }
                throw new RuntimeException("Error when fixing changes to db", e);
            }
        }
    }

    /**
     * Implementors "know" how to fix each change operation.
     * Implement your own fixer to store changes in db, file or any other store.
     * It works with transactional stores as it has {@link #start()} and {@link #end()} 
     * methods which are called before starting fixing changes and when this process is finished.
     * So it can be used to start transaction or open file and then commit or close.
     * @see TrnUtil.TrnDb 
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

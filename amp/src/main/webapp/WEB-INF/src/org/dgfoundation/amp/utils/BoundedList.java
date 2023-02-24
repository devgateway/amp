package org.dgfoundation.amp.utils;

import java.util.*;

/**
 * a class which is a like linkedlist bounded by a maximum length<br />
 * <b>only the add method enforces the limit - the others are seen just through inheritance!</b><br />
 * used inheritance in lieu of encapsulation because this list needs to be iterated by JSP files
 * repeatedly-inserted elements are moved to front, the bottom is lost to make place 
 * @author simple
 *
 */
public class BoundedList<K> extends LinkedList<K>{
    
    private Comparator<K> comparator;
    private int maxN;
    
    public BoundedList(int maxN, Comparator<K> comparator)
    {
        this.maxN = maxN;
        this.comparator = comparator;
    }
    
    public boolean add(K item)
    {
        int pos = findItemPos(item);
        if (pos >= 0)
        {
            // item already exists... remove it
            remove(pos);
        }
        if (size() >= maxN)
            removeLast();
        super.addFirst(item);
        return true;
    }
    
    private int findItemPos(K item)
    {
        for(int i = 0; i < size(); i++)
            if (equalItems(get(i), item))
                return i;
        return -1;
    }
    
    private boolean equalItems(K item1, K item2)
    {
        if (comparator != null)
            return comparator.compare(item1, item2) == 0;
        return item1.equals(item2);
    }
}



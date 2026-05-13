package org.digijava.kernel.ampapi.endpoints.util;

import java.util.LinkedHashMap;
import java.util.Map;
/**
 * 
 * Class that implements a LinkedHashMap with a maximum size
 * If that size is reached the first element will be removed 
 * @author jdeanquin@developmentgateway.org
 *
 * @param <K>
 * @param <V>
 */
public class MaxSizeLinkedHashMap<K, V> extends LinkedHashMap<K, V> {
    /**
     * 
     */
    private static final long serialVersionUID = 4467118985640179660L;
    private final int maxSize;

    public MaxSizeLinkedHashMap(int maxSize) {
        super();
        this.maxSize = maxSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maxSize;
    }
}



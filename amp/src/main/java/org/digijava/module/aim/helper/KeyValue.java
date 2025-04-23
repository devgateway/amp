package org.digijava.module.aim.helper;

import org.dgfoundation.amp.algo.AmpCollections;

import java.io.Serializable;
import java.util.Comparator;

public class KeyValue implements Serializable, Comparable<KeyValue> {
    String key;
    String value;
    
    public KeyValue(String key, String value) {
        this.key    = key;
        this.value  = value;
    }
    
    public KeyValue(Long key, String value){
        this(Long.toString(key), value);
    }
    
    public String getKey() {
        return key;
    }
    
    public void setKey(String key) {
        this.key = key;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    
    /**
     * convenience for JSPs
     * @return
     */
    public String getName()
    {
        return getValue();
    }
    
    public String getId()
    {
        return getKey();
    }
    
    public final static Comparator<KeyValue> keyComparator      = new Comparator<KeyValue>() {
                                    public int compare(KeyValue o1, KeyValue o2) {
                                        return o1.key.compareTo(o2.key);
                                    }
                            };
    public final static Comparator<KeyValue> valueComparator        = new Comparator<KeyValue>() {
                                public int compare(KeyValue o1, KeyValue o2) {
                                    return o1.value.compareTo(o2.value);
                                }
                        };
                        
    @Override
    public String toString()
    {
        return String.format("KeyValue: (%s, %s)", this.key, this.value);
    }
    
    /**
     * returns key parsed as Long. Throws exception if key not present or non-parseable
     * @return
     */
    public Long getKeyAsLong()
    {
        try
        {
            return Long.parseLong(this.key);
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    @Override public int compareTo(KeyValue oth)
    {
        Long a = this.getKeyAsLong(), b = oth.getKeyAsLong();
        Integer comp = AmpCollections.nullCompare(a, b);
        if (comp != null)
            return comp;
        return 0;
    }
    
    @Override public boolean equals(Object oth){
        return this.compareTo((KeyValue) oth) == 0;
    }
    
    @Override public int hashCode(){
        return (this.key.hashCode() * 17) ^ this.value.hashCode();
    }
}

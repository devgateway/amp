package org.digijava.module.common.dbentity;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Catalin Andrei
 * @since Oct 1, 2008
 */
public abstract class Enum implements Serializable {
    
    protected static Map nameMap = new HashMap(15);
    protected static Map valueMap = new HashMap(15);
    protected int value;
    protected transient String name;

    /**
     * @param name
     * @param value
     */
    protected Enum(String name, int value) {
        this.value = value;
        this.name = name;
        add();
    }

    /**
     * 
     */
    protected void add() {
        nameMap.put(this.name, this);
        valueMap.put(new Integer(this.value), this);
    }

    /**
     * @param name
     * @return
     */
    public static Enum get(String name) {
        return (Enum) nameMap.get(name);
    }

    /**
     * @param value
     * @return
     */
    public static Enum get(int value) {
        return (Enum) valueMap.get(new Integer(value));
    }

    /**
     * @return
     */
    public int getValue() {
        return value;
    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @return
     * @throws ObjectStreamException
     */
    protected Object readResolve() throws ObjectStreamException{
        return get(this.value);
    }
}

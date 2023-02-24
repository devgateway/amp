package org.digijava.kernel.lucene;

import java.util.List;

/**
 * Defines registry for lucene modules
 * @author Irakli Kobiashvili
 *
 */
public interface LuceneModuleRegistry {
    /**
     * Get lucene module by unique name.
     * Usually class name + suffix if required
     * @param name unique name of lucene module.
     * @return lucene module implementation/
     */
    LucModule<?> get(String name);
    
    /**
     * Store lucene module by unique name.
     * @param name
     * @param module
     */
    void put(String name, LucModule<?> module);
    
    /**
     * Retrive all registered lucene modules
     * @return list of lucene modules
     */
    List<LucModule<?>> getAll();
}

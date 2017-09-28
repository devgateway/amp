/**
 * 
 */
package org.digijava.module.aim.util;

import java.util.List;

import org.dgfoundation.amp.visibility.data.RuleBasedData;
import org.digijava.kernel.persistence.PersistenceManager;


/**
 * @author Nadejda Mandrescu
 */
public class VisibilityUtil {
    
    /**
     * Retrieves all entries that have a visibility rule defined 
     * @param clazz that has a visibility 
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<? extends RuleBasedData> getDataWithVisibilityRule(Class<? extends RuleBasedData> clazz) {
        return PersistenceManager.getSession().createQuery(
                "select rbd from " + clazz.getName() + " rbd where rbd.rule is not null"
                ).list();
    }
    
    /**
     * Retrieves all entries that have no visibility rule defined
     * @param clazz that has a visibility 
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<? extends RuleBasedData> getDataWithoutVisibilityRule(Class<? extends RuleBasedData> clazz) {
        return PersistenceManager.getSession().createQuery(
                "select rbd from " + clazz.getName() + " rbd where rbd.rule is null"
                ).list();
    }
    
    /**
     * Retrieves all entries names that have no visibility rule defined
     * @param clazz that has a visibility 
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<String> getDataNamesWithoutVisibilityRule(Class<? extends RuleBasedData> clazz) {
        return PersistenceManager.getSession().createQuery(
                "select rbd.name from " + clazz.getName() + " rbd where rbd.rule is null"
                ).list();
    }
    
}

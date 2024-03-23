/**
 * 
 */
package org.dgfoundation.amp.utils;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ColumnConstants;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * General purpose constants utility class
 * 
 * @author Nadejda Mandrescu
 */
public class ConstantsUtil {
    protected static final Logger logger = Logger.getLogger(ConstantsUtil.class);
    
    private static Map<Class<?>, Map<String, String>> classColumnsValueToNameMap = 
            Collections.synchronizedMap(new HashMap<Class<?>, Map<String, String>>());
    private static Map<Class<?>, Map<String, String>> classColumnsNameToValueMap = 
            Collections.synchronizedMap(new HashMap<Class<?>, Map<String, String>>());
    
    /**
     * Retrieves the list of static constants values of a class mapped to their names
     * 
     * @param clazz the class to process 
     * @return constants map
     */
    public static final Map<String, String> getConstantsValueToNameMap(Class<?> clazz) {
        return new HashMap<String, String>(getConstantsMap(clazz, true));
    }
    
    /**
     * Retrieves the list of static constants names of a class mapped to their values
     * 
     * @param clazz the class to process 
     * @return constants map
     */
    public static final Map<String, String> getConstantsNameToValueMap(Class<?> clazz) {
        return new HashMap<String, String>(getConstantsMap(clazz, false));
    }
    
    /**
     * Retrieves the full list of static constants values, 
     * including duplicated values
     * 
     * @param clazz the class to process
     * @return the list
     */
    public static final Collection<String> getConstantsList(Class<?> clazz) {
        return new ArrayList<String>(getConstantsMap(clazz, false).values());
    }
    
    /**
     * Retrieves the set of static constants values 
     * (i.e excluding duplicated values)
     * 
     * @param clazz the class to process
     * @return the set
     */
    public static final Set<String> getConstantsSet(Class<?> clazz) {
        return new HashSet<String>(getConstantsMap(clazz, true).keySet());
    }
    
    synchronized private static final Map<String, String> getConstantsMap(Class<?> clazz, boolean valueToName) {
        Map<String, String> constants = valueToName ? classColumnsValueToNameMap.get(clazz) :
            classColumnsNameToValueMap.get(clazz);
        
        if (constants == null) {
            constants = Collections.synchronizedMap(new HashMap<String, String>());
            Field[] fields = clazz.getFields();
             new HashMap<String, String>();
            for (Field field : fields) {
                if (Modifier.isStatic(field.getModifiers())) {
                    try {
                        String value = String.valueOf(field.get(ColumnConstants.class.getClass()));
                        if (valueToName)
                            constants.put(value, field.getName());
                        else 
                            constants.put(field.getName(), value);
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }
            if (valueToName)
                classColumnsValueToNameMap.put(clazz, constants);
            else 
                classColumnsNameToValueMap.put(clazz, constants);
        }
        return constants;
    }
    
}

package org.digijava.kernel.ampapi.endpoints.activity;

/**
 * @author Octavian Ciubotaru
 */
public interface FieldAccessor {

    Object get(Object targetObject);

    void set(Object targetObject, Object value);
}

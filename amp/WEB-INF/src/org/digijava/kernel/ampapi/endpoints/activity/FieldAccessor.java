package org.digijava.kernel.ampapi.endpoints.activity;

/**
 * @author Octavian Ciubotaru
 */
public interface FieldAccessor {

    <T> T get(Object targetObject);

    void set(Object targetObject, Object value);
}

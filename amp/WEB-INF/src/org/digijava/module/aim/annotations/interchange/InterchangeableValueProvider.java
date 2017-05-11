package org.digijava.module.aim.annotations.interchange;

/**
 * @author Octavian Ciubotaru
 */
public interface InterchangeableValueProvider<T> {

    String getValue(T object);

    default Object getExtraInfo(T object) {
        return null;
    }
}

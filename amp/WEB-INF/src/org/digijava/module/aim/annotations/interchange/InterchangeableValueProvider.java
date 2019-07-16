package org.digijava.module.aim.annotations.interchange;

/**
 * This class allows to provide value for generic case.
 * @author Octavian Ciubotaru
 */
public interface InterchangeableValueProvider<T> {

    String getValue(T object);

    default boolean isTranslatable() {
        return false;
    }

    default Object getExtraInfo(T object) {
        return null;
    }
}

package org.dgfoundation.amp.metamodel.type;

import java.util.function.Function;

/**
 * Represents an attribute in an {@link ObjectType object}.
 *
 * @author Octavian Ciubotaru
 */
public final class Attribute {

    private Function<Object, Object> accessor;
    private String name;
    private Type type;

    public Attribute(Function<Object, Object> accessor, String name, Type type) {
        this.accessor = accessor;
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public Object get(Object object) {
        return accessor.apply(object);
    }

    public String toString() {
        return name;
    }
}

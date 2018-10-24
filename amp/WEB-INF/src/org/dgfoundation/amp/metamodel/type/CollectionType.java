package org.dgfoundation.amp.metamodel.type;

import org.digijava.module.aim.dbentity.AuditedEntity;

/**
 * Represents a collection of homogeneous items (i.e. all items must be of the same type).
 * Must be a subclass of {@link java.util.Collection} and can represent a {@link java.util.Set} or a
 * {@link java.util.List}.
 *
 * <p>Currently only collections of {@link ObjectType objects} are supported.
 * <p>When comparing two collections of objects, objects are matched by checking {@link AuditedEntity#getId()} and
 * {@link AuditedEntity#getOriginalObjectId()}. Objects must extend from {@link AuditedEntity}.
 *
 * @author Octavian Ciubotaru
 */
public final class CollectionType implements Type {

    private Type elementType;

    public CollectionType(Type elementType) {
        this.elementType = elementType;
    }

    /**
     * @return the type of the elements
     */
    public Type getElementType() {
        return elementType;
    }
}

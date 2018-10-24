package org.dgfoundation.amp.metamodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.dgfoundation.amp.metamodel.type.CollectionType;
import org.dgfoundation.amp.metamodel.type.ObjectType;
import org.dgfoundation.amp.metamodel.type.ValueType;
import org.digijava.module.aim.dbentity.AuditedEntity;
import org.dgfoundation.amp.metamodel.diff.AttributeChange;
import org.dgfoundation.amp.metamodel.diff.Change;
import org.dgfoundation.amp.metamodel.diff.CollectionChange;
import org.dgfoundation.amp.metamodel.diff.ObjectChange;
import org.dgfoundation.amp.metamodel.diff.ValueChange;

/**
 * @author Octavian Ciubotaru
 */
public class MetaModelDiffer {

    /**
     * Compute diff between two objects.
     *
     * @param oldObject old object
     * @param newObject new object
     * @param type type that describes the object
     * @return changes between objects or null if no changes are detected
     */
    public ObjectChange diff(Object oldObject, Object newObject, ObjectType type) {
        List<AttributeChange> attrChanges = new ArrayList<>();

        type.forEach(a -> {

            Object o1 = oldObject != null ? a.get(oldObject) : null;
            Object o2 = newObject != null ? a.get(newObject) : null;

            Change change;
            if (a.getType() instanceof ValueType) {
                change = diff(o1, o2, (ValueType) a.getType());
            } else if (a.getType() instanceof ObjectType) {
                change = diff(o1, o2, (ObjectType) a.getType());
            } else if (a.getType() instanceof CollectionType) {
                change = diff((Collection) o1, (Collection) o2, (CollectionType) a.getType());
            } else {
                throw new IllegalStateException("Unknown type " + a.getType());
            }

            if (change != null) {
                attrChanges.add(new AttributeChange(a, change));
            }
        });

        if (!attrChanges.isEmpty()) {
            return new ObjectChange(oldObject, newObject, type, attrChanges);
        } else {
            return null;
        }
    }

    public ValueChange diff(Object oldObj, Object newObj, ValueType type) {
        String oldValue = toStrOrNull(oldObj);
        String newValue = toStrOrNull(newObj);
        if (!Objects.equals(oldValue, newValue)) {
            return new ValueChange(oldValue, newValue);
        } else {
            return null;
        }
    }

    public CollectionChange diff(Collection oldCollection, Collection newCollection, CollectionType type) {
        if (type.getElementType() instanceof ObjectType) {
            return diffForCollectionOfObjects(oldCollection, newCollection, (ObjectType) type.getElementType());
        } else {
            throw new IllegalStateException("Collections of " + type.getElementType().getClass() +
                    " not yet supported!");
        }
    }

    private CollectionChange diffForCollectionOfObjects(Collection oldCollection, Collection newCollection,
            ObjectType elementType) {

        CollectionChange changes = new CollectionChange();

        Map<Long, AuditedEntity> newObjectsByOriginalId = new LinkedHashMap<>();
        for (Object object : Optional.ofNullable(newCollection).orElse(Collections.emptyList())) {
            AuditedEntity entity = (AuditedEntity) object;
            Long id = getId(entity);
            if (newObjectsByOriginalId.put(id, entity) != null) {
                throw new IllegalStateException("Duplicate object id " + id); //TODO add db constraint
            }
        }

        for (Object oldObject : Optional.ofNullable(oldCollection).orElse(Collections.emptyList())) {
            AuditedEntity oldEntity = (AuditedEntity) oldObject;
            Long id = getId(oldEntity);
            AuditedEntity newObject = newObjectsByOriginalId.remove(id);
            changes.addIfNotNull(diff(oldObject, newObject, elementType));
        }

        for (AuditedEntity newObject : newObjectsByOriginalId.values()) {
            changes.addIfNotNull(diff(null, newObject, elementType));
        }

        if (!changes.isEmpty()) {
            return changes;
        } else {
            return null;
        }
    }

    private Long getId(AuditedEntity e) {
        return e.getOriginalObjectId() != null ? e.getOriginalObjectId() : e.getId();
    }

    private String toStrOrNull(Object o) {
        return o == null ? null : o.toString();
    }
}

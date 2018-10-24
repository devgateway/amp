package org.dgfoundation.amp.metamodel.diff;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.dgfoundation.amp.metamodel.type.ObjectType;

/**
 * @author Octavian Ciubotaru
 */
public class ObjectChange implements Change {

    private Object oldObject;
    private Object newObject;
    private ObjectType objectType;
    private List<AttributeChange> changes;

    public ObjectChange(Object oldObject, Object newObject, ObjectType objectType, AttributeChange... changes) {
        this(oldObject, newObject, objectType, Arrays.asList(changes));
    }

    public ObjectChange(Object oldObject, Object newObject, ObjectType objectType, List<AttributeChange> changes) {
        this.oldObject = oldObject;
        this.newObject = newObject;
        this.objectType = objectType;
        this.changes = changes;
    }

    public List<AttributeChange> getChanges() {
        return changes;
    }

    public AttributeChange getChange(String name) {
        return changes.stream().filter(c -> c.getAttribute().getName().equals(name)).findAny().orElse(null);
    }

    public Object getOldObject() {
        return oldObject;
    }

    public void setOldObject(Object oldObject) {
        this.oldObject = oldObject;
    }

    public Object getNewObject() {
        return newObject;
    }

    public void setNewObject(Object newObject) {
        this.newObject = newObject;
    }

    public ObjectType getObjectType() {
        return objectType;
    }

    public void setObjectType(ObjectType objectType) {
        this.objectType = objectType;
    }

    @Override
    public String toString() {
        return changes.size() + " attributes changed";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ObjectChange)) {
            return false;
        }
        ObjectChange that = (ObjectChange) o;
        return Objects.equals(oldObject, that.oldObject) &&
                Objects.equals(newObject, that.newObject) &&
                Objects.equals(objectType, that.objectType) &&
                Objects.equals(changes, that.changes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(oldObject, newObject, objectType, changes);
    }

    @Override
    public ChangeType getChangeType() {
        // TODO make it O(n)
        if (changes.stream().allMatch(a -> a.getChange().getChangeType() == ChangeType.ADDED)) {
            return ChangeType.ADDED;
        }
        if (changes.stream().allMatch(a -> a.getChange().getChangeType() == ChangeType.DELETED)) {
            return ChangeType.DELETED;
        }
        return ChangeType.MODIFIED;
    }
}

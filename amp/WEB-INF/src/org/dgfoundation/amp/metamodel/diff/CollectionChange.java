package org.dgfoundation.amp.metamodel.diff;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Shortcomings: a changed object is represented through {@link ObjectChange}.
 * However new objects or deleted objects are represented through the same class only having left or right side null
 * in each {@link AttributeChange} of the {@link ObjectChange}. Maybe we should implement new classes like NewObject
 * and DeletedObject to reflect the change more correctly and not rely on nulls anymore.
 *
 * @author Octavian Ciubotaru
 */
public class CollectionChange extends ArrayList<Change> implements Change {

    public CollectionChange() {
    }

    public CollectionChange(Change... changes) {
        this.addAll(Arrays.asList(changes));
    }

    public void addIfNotNull(Change change) {
        if (change != null) {
            add(change);
        }
    }

    @Override
    public ChangeType getChangeType() {
        // TODO make it O(N)
        if (stream().allMatch(c -> c.getChangeType() == ChangeType.ADDED)) {
            return ChangeType.ADDED;
        }
        if (stream().allMatch(c -> c.getChangeType() == ChangeType.DELETED)) {
            return ChangeType.DELETED;
        }
        return ChangeType.MODIFIED;
    }
}

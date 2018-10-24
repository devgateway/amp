package org.dgfoundation.amp.metamodel.diff;

import java.util.Objects;

/**
 * @author Octavian Ciubotaru
 */
public class ValueChange implements Change {

    private String oldValue, newValue;

    public ValueChange(String oldValue, String newValue) {
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public String getOldValue() {
        return oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    @Override
    public String toString() {
        return oldValue + " -> " + newValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ValueChange)) {
            return false;
        }
        ValueChange that = (ValueChange) o;
        return Objects.equals(oldValue, that.oldValue) &&
                Objects.equals(newValue, that.newValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(oldValue, newValue);
    }

    @Override
    public ChangeType getChangeType() {
        if (newValue == null) {
            return ChangeType.DELETED;
        } else if (oldValue == null) {
            return ChangeType.ADDED;
        } else {
            return ChangeType.MODIFIED;
        }
    }
}

package org.dgfoundation.amp.metamodel.diff;

import java.util.Objects;

import org.dgfoundation.amp.metamodel.type.Attribute;

/**
 * Represents a change to an attribute.
 *
 * Improvements:
 *  - new/removed attributes are represented by changes where left/right branch is null, maybe should switch to more
 *    concrete classes for this kinds of change
 *
 * @author Octavian Ciubotaru
 */
public class AttributeChange {

    private Attribute attribute;

    private Change change;

    public AttributeChange(Attribute attribute, Change change) {
        this.attribute = attribute;
        this.change = change;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public Change getChange() {
        return change;
    }

    @Override
    public String toString() {
        return "AttributeChange{" +
                "attribute=" + attribute +
                ", change=" + change +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AttributeChange)) {
            return false;
        }
        AttributeChange that = (AttributeChange) o;
        return Objects.equals(attribute, that.attribute) &&
                Objects.equals(change, that.change);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attribute, change);
    }
}

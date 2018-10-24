package org.dgfoundation.amp.metamodel.diff;

/**
 * Represents an abstract change on the domain model.
 *
 * Improvements:
 *  - possibility to copy change from left to right or vice versa
 *
 * @author Octavian Ciubotaru
 */
public interface Change {

    ChangeType getChangeType();
}

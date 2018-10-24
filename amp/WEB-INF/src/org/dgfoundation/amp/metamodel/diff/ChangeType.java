package org.dgfoundation.amp.metamodel.diff;

/**
 * @author Octavian Ciubotaru
 */
public enum ChangeType {
    ADDED('+'), MODIFIED('!'), DELETED('-');

    private char textualMarker;

    ChangeType(char textualMarker) {
        this.textualMarker = textualMarker;
    }

    public char getTextualMarker() {
        return textualMarker;
    }
}

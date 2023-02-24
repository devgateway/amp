package org.digijava.kernel.validation;

/**
 * Describes a field path.
 *
 * @author Octavian Ciubotaru
 */
public interface Path extends Iterable<Path.Node> {

    /**
     * Describes a field.
     */
    interface Node {

        String getName();

//        boolean inIterable();
    }
}

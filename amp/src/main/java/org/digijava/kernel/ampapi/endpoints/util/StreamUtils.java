package org.digijava.kernel.ampapi.endpoints.util;

import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author Octavian Ciubotaru
 */
public final class StreamUtils {

    private StreamUtils() {
    }

    /**
     * Creates a sequential Stream from an iterator.
     *
     * @param sourceIterator iterator describing stream elements
     * @param <T> the type of stream elements
     * @return a new sequential Stream
     */
    public static <T> Stream<T> asStream(Iterator<T> sourceIterator) {
        return asStream(sourceIterator, false);
    }

    /**
     * Creates a sequential or parallel Stream from an iterator.
     *
     * @param sourceIterator iterator describing stream elements
     * @param parallel if true then the returned stream is a parallel stream;
     *                 if false the returned stream is a sequential stream
     * @param <T> the type of stream elements
     * @return a new sequential or parallel Stream
     */
    public static <T> Stream<T> asStream(Iterator<T> sourceIterator, boolean parallel) {
        Iterable<T> iterable = () -> sourceIterator;
        return StreamSupport.stream(iterable.spliterator(), parallel);
    }
}

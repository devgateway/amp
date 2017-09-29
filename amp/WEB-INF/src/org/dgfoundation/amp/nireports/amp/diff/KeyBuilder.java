package org.dgfoundation.amp.nireports.amp.diff;

import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.schema.NiReportColumn;

/**
 * an interface which builds a key for an AMP columns' caching context
 * @author Dolghier Constantin
 *
 * @param <K> the type of the built key
 */
public interface KeyBuilder<K> {
    public K buildKey(NiReportsEngine engine, NiReportColumn<?> col);
}

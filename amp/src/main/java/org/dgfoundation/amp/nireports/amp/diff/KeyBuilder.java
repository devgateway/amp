package org.dgfoundation.amp.nireports.amp.diff;

import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.schema.NiReportColumn;

/**
 * an interface which builds a key for an AMP columns' caching context
 * @author Dolghier Constantin
 */
public interface KeyBuilder {
    String buildKey(NiReportsEngine engine, NiReportColumn<?> col);
}

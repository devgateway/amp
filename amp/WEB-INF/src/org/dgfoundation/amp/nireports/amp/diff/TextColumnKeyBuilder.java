package org.dgfoundation.amp.nireports.amp.diff;

import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.schema.NiReportColumn;
import org.digijava.kernel.request.TLSUtils;

/**
 * the {@link KeyBuilder} for textual columns, whose only context key is the current-request locale
 * @author Dolghier Constantin
 *
 */
public class TextColumnKeyBuilder implements KeyBuilder {

    @Override
    public String buildKey(NiReportsEngine engine, NiReportColumn<?> col) {
        return TLSUtils.getEffectiveLangCode();
    }
    
    public final static TextColumnKeyBuilder instance = new TextColumnKeyBuilder();
}

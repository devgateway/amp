package org.dgfoundation.amp.nireports.amp.diff;

import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.amp.AmpDifferentialColumn;
import org.dgfoundation.amp.nireports.schema.NiReportColumn;

/**
 * @author Octavian Ciubotaru
 */
public class SSCKeyBuilder implements KeyBuilder {

    private KeyBuilder targetKeyBuilder;
    private AmpDifferentialColumn column;

    public SSCKeyBuilder(AmpDifferentialColumn column, KeyBuilder targetKeyBuilder) {
        this.column = column;
        this.targetKeyBuilder = targetKeyBuilder;
    }

    @Override
    public String buildKey(NiReportsEngine engine, NiReportColumn<?> col) {
        return targetKeyBuilder.buildKey(engine, col) + Boolean.toString(column.inSSCWorkspace());
    }
}

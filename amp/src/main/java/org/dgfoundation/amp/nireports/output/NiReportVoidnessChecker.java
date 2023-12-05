package org.dgfoundation.amp.nireports.output;

import org.dgfoundation.amp.nireports.runtime.CellColumn;

/**
 * returns true IFF the report contains no value in a given CellColumn
 * @author Dolghier Constantin
 *
 */
public class NiReportVoidnessChecker implements NiReportDataVisitor<Boolean> {

    protected final CellColumn cc;
    
    public NiReportVoidnessChecker(CellColumn cc) {
        this.cc = cc;
    }   

    @Override
    public Boolean visit(NiColumnReportData crd) {
        return crd.contents.get(cc).isEmpty();
    }

    @Override
    public Boolean visit(NiGroupReportData grd) {
        return grd.subreports.stream().allMatch(z -> z.accept(this));
    }
}

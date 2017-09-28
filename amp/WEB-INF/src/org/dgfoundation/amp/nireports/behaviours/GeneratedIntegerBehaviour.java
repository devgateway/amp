package org.dgfoundation.amp.nireports.behaviours;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import org.dgfoundation.amp.nireports.IntCell;
import org.dgfoundation.amp.nireports.output.NiReportData;
import org.dgfoundation.amp.nireports.output.NiRowId;
import org.dgfoundation.amp.nireports.output.nicells.NiIntCell;
import org.dgfoundation.amp.nireports.output.nicells.NiOutCell;
import org.dgfoundation.amp.nireports.runtime.CellColumn;
import org.dgfoundation.amp.nireports.runtime.ColumnReportData;
import org.dgfoundation.amp.nireports.runtime.GroupReportData;
import org.dgfoundation.amp.nireports.schema.TimeRange;

/**
 * {@link GeneratedColumnBehaviour} which generates integers 
 * @author Dolghier Constantin
 *
 */
public abstract class GeneratedIntegerBehaviour extends GeneratedColumnBehaviour<IntCell, NiIntCell> {
    
    public final static GeneratedIntegerBehaviour ENTITIES_COUNT_BEHAVIOUR = withFormulas(
            (grd, children) -> Long.valueOf(grd.getIds().size()), 
            (crd, contents) -> Long.valueOf(crd.getIds().size()));
    
    @Override
    public TimeRange getTimeRange() {
        return TimeRange.NONE;
    }

    @Override
    public NiIntCell buildGroupTrailCell(GroupReportData grd, CellColumn cc, List<NiReportData> visitedChildren) {
        return new NiIntCell(groupTrailCell(grd, cc, visitedChildren), -1);
    }

    @Override
    public NiIntCell buildColumnTrailCell(ColumnReportData crd, CellColumn cc, Map<CellColumn, Map<NiRowId, NiOutCell>> mappedContents) {
        return new NiIntCell(columnTrailCell(crd, cc, mappedContents), -1);
    }

    protected abstract long groupTrailCell(GroupReportData grd, CellColumn cc, List<NiReportData> visitedChildren);
    protected abstract long columnTrailCell(ColumnReportData crd, CellColumn cc, Map<CellColumn, Map<NiRowId, NiOutCell>> mappedContents);
    
    
    /**
     * generates a behaviour for the case where the value is generated based on children/contents only
     * @param grdFormula
     * @param crdFormula
     * @return
     */
    public static GeneratedIntegerBehaviour withFormulas(BiFunction<GroupReportData, List<NiReportData>, Long> grdFormula, BiFunction<ColumnReportData, Map<CellColumn, Map<NiRowId, NiOutCell>>, Long> crdFormula) {
        return new GeneratedIntegerBehaviour() {
                        
            @Override
            protected long groupTrailCell(GroupReportData grd, CellColumn cc, List<NiReportData> visitedChildren) {
                return grdFormula.apply(grd, visitedChildren);
            }
            
            @Override
            protected long columnTrailCell(ColumnReportData crd, CellColumn cc, Map<CellColumn, Map<NiRowId, NiOutCell>> mappedContents) {
                return crdFormula.apply(crd, mappedContents);
            }
        };
    }
}

package org.dgfoundation.amp.nireports.schema;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import org.dgfoundation.amp.nireports.IntCell;
import org.dgfoundation.amp.nireports.output.NiIntCell;
import org.dgfoundation.amp.nireports.output.NiOutCell;
import org.dgfoundation.amp.nireports.output.NiReportData;
import org.dgfoundation.amp.nireports.runtime.CellColumn;
import org.dgfoundation.amp.nireports.runtime.ColumnReportData;
import org.dgfoundation.amp.nireports.runtime.GroupReportData;

/**
 * column behaviour of a generated integer column. Also provides shortcut static builders for common cases
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
	public NiIntCell buildColumnTrailCell(ColumnReportData crd, CellColumn cc, Map<CellColumn, Map<Long, NiOutCell>> mappedContents) {
		return new NiIntCell(columnTrailCell(crd, cc, mappedContents), -1);
	}

	protected abstract long groupTrailCell(GroupReportData grd, CellColumn cc, List<NiReportData> visitedChildren);
	protected abstract long columnTrailCell(ColumnReportData crd, CellColumn cc, Map<CellColumn, Map<Long, NiOutCell>> mappedContents);
	
	
	/**
	 * generates a behaviour for the case where the value is generated based on children/contents only
	 * @param grdFormula
	 * @param crdFormula
	 * @return
	 */
	public static GeneratedIntegerBehaviour withFormulas(BiFunction<GroupReportData, List<NiReportData>, Long> grdFormula, BiFunction<ColumnReportData, Map<CellColumn, Map<Long, NiOutCell>>, Long> crdFormula) {
		return new GeneratedIntegerBehaviour() {
						
			@Override
			protected long groupTrailCell(GroupReportData grd, CellColumn cc, List<NiReportData> visitedChildren) {
				return grdFormula.apply(grd, visitedChildren);
			}
			
			@Override
			protected long columnTrailCell(ColumnReportData crd, CellColumn cc, Map<CellColumn, Map<Long, NiOutCell>> mappedContents) {
				return crdFormula.apply(crd, mappedContents);
			}
		};
	}
}

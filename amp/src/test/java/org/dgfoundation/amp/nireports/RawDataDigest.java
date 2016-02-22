package org.dgfoundation.amp.nireports;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.nireports.output.NiColumnReportData;
import org.dgfoundation.amp.nireports.output.NiGroupReportData;
import org.dgfoundation.amp.nireports.output.NiOutCell;
import org.dgfoundation.amp.nireports.output.NiReportDataVisitor;
import org.dgfoundation.amp.nireports.runtime.CellColumn;


public class RawDataDigest implements NiReportDataVisitor<Map<Long, NiOutCell>> {

	/**
	 * filter on activity id
	 * @param idFilter
	 */
	public RawDataDigest(Set<Long> idFilter) {
		this.idFilter = idFilter;
	}

	private final Set<Long> idFilter;
	
	@Override
	public Map<Long, NiOutCell> visit(NiColumnReportData crd) {
		Map<Long, NiOutCell> res = new LinkedHashMap<>();
		Set<Map.Entry<CellColumn, Map<Long, NiOutCell>>> contents = crd.contents.entrySet();
		Long resultId = 0L;
		for (Map.Entry<CellColumn, Map<Long, NiOutCell>> row : contents) {
			Map<Long, NiOutCell> cells = row.getValue();
			for (Map.Entry<Long, NiOutCell> cell : cells.entrySet()) {
				if (idFilter.contains(cell.getKey())) {
					res.put(resultId, cell.getValue());
					resultId++;
				}
			}
		}
		return res;
	}

	@Override
	public Map<Long, NiOutCell> visit(NiGroupReportData grd) {
		Map<Long, NiOutCell> res = new LinkedHashMap<>();
		return res;
	}

}

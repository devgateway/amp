package org.dgfoundation.amp.testmodels.nicolumns;

import java.util.List;

import org.dgfoundation.amp.newreports.ReportRenderWarning;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.schema.Behaviour;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.schema.NiReportColumn;

public  class HardcodedColumn<K extends Cell> extends NiReportColumn<K> {
	protected List<K> cells;
	
	public HardcodedColumn(String name, HardcodedCells<K> cells, LevelColumn levelColumn, Behaviour<?> behaviour) {
		super(name, levelColumn, behaviour, null);
		this.cells = cells.getCells();
	}
	
	@Override
	public List<K> fetch(NiReportsEngine engine) throws Exception {
		return cells;
	}

	@Override
	public List<ReportRenderWarning> performCheck() {
		return null;
	}
}

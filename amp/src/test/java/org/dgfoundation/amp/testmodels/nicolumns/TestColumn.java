package org.dgfoundation.amp.testmodels.nicolumns;

import java.util.List;

import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.schema.Behaviour;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.schema.NiReportColumn;

public  class TestColumn<K extends Cell> extends NiReportColumn<K> {

	private List<K> cells;

	
	public TestColumn(String name, HardcodedCells<K> cells, LevelColumn levelColumn,
			@SuppressWarnings("rawtypes") Behaviour behaviour) {
		super(name, levelColumn, behaviour, null);
		this.cells = cells.getCells();
	}

	
	@Override
	public List<K> fetch(NiReportsEngine engine) throws Exception {
		return cells;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List performCheck() {
		return null;
	}
}

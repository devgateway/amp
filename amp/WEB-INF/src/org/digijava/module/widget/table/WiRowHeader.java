package org.digijava.module.widget.table;

import java.util.List;
import java.util.Map;

public class WiRowHeader extends WiRow {
	
	public WiRowHeader(Long pk, Map<Long, WiColumn> colsByIds) {
		super(pk,colsByIds);
	}
	
	@Override
	public WiCell getCell(Long columnId) {
		// TODO Auto-generated method stub
		return super.getCell(columnId);
	}

	@Override
	public List<WiCell> getCells() {
		// TODO Auto-generated method stub
		return super.getCells();
	}

	@Override
	public void updateCell(WiCell cell, Long columnId) {
		// TODO Auto-generated method stub
		super.updateCell(cell, columnId);
	}

}

package org.digijava.module.widget.table.filteredColumn;

import org.digijava.module.widget.table.WiCell;

public class WiCellFiltered extends WiCell {

	private Long filterItemId;
	private String value;
	
	@Override
	public String getValue() {
		return value;
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}

	public void setFilterItemId(Long filterItemId) {
		this.filterItemId = filterItemId;
	}

	public Long getFilterItemId() {
		return filterItemId;
	}

}

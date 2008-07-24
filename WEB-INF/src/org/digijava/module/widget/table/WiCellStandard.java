package org.digijava.module.widget.table;

public class WiCellStandard extends WiCell {

	private String value = "HAHA!";
	
	@Override
	public String getValue() {
		return value;
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}

}

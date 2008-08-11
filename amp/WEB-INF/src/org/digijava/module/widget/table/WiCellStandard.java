package org.digijava.module.widget.table;

import org.digijava.module.widget.dbentity.AmpDaValue;

/**
 * Standard simple cell where value is entered manually.
 * @author Irakli Kobiashvili
 *
 */
public class WiCellStandard extends WiCell {

	private String value = "";
	
	public WiCellStandard(){
		
	}
	public WiCellStandard(WiColumn col){
		setHeaderCell(true);
		setColumn(col);
		value = col.getName();
	}
	public WiCellStandard(AmpDaValue dbValue){
		this.value = dbValue.getValue();
	}
	
	@Override
	public String getValue() {
		return value;
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String tagContent() {
		if (isEditMode()){
			StringBuffer buff = new StringBuffer("<input type='edit' name='cell");
			buff.append(getColumn().getId().toString());
			buff.append("'>");
			buff.append(getValue());
			buff.append("</imput>");
			return buff.toString();
		}
		return getValue();
	}

}

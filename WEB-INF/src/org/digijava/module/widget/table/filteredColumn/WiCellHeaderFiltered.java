package org.digijava.module.widget.table.filteredColumn;


public class WiCellHeaderFiltered extends WiCellFiltered {

	@Override
	public String tagContent() {
		StringBuffer buf = new StringBuffer("<select id='mydropdown");
		buf.append(this.getId());
		buf.append("' name='");
		buf.append(this.getId());
		buf.append("'>");
		
		buf.append("<option id='1'>AVOE1</option>");
		buf.append("<option id='2'>AVOE2</option>");
		
		buf.append("</select>");
		return buf.toString();
	}

}

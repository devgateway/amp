package org.digijava.module.widget.table.filteredColumn;


public class WiCellHeaderFiltered extends WiCellFiltered {

	public WiCellHeaderFiltered(){
		setCellTypeId(FILTER_HEADER_CELL);
	}
	
	@Override
	public String tagContent() {
		StringBuffer buf = new StringBuffer("<select id='mydropdown");
		buf.append(this.getColumn().getTable().getId());
		buf.append("' name='");
		buf.append(this.getId());
		buf.append("' onchange='tableWidgetFilterChanged_");
		buf.append(this.getColumn().getTable().getId());
		buf.append("(");
		buf.append(this.getColumn().getId());
		buf.append(")'> ");
		for (FilterItem item : getItemProvider().getItems()) {
			buf.append("<option id='");
			buf.append(item.getId());
			buf.append("'>");
			buf.append(item.getName());
			buf.append("</option>");
		}
		buf.append("</select>");
		return buf.toString();
	}

}

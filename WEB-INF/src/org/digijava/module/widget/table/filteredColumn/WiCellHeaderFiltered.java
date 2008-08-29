package org.digijava.module.widget.table.filteredColumn;


public class WiCellHeaderFiltered extends WiCellFiltered {

	public WiCellHeaderFiltered(){
		setCellTypeId(FILTER_HEADER_CELL);
	}
	
	@Override
	public String tagContent() {
		WiColumnDropDownFilter masterColumn = (WiColumnDropDownFilter) this.getColumn();
		
		StringBuffer buf = new StringBuffer("<select id='tableWidgetDropDown_");
		buf.append(this.getColumn().getTable().getId());
		buf.append("' name='selectedFilterItemId_");
		buf.append(this.getColumn().getTable().getId());
		buf.append("' onchange='tableWidgetFilterChanged_");
		buf.append(this.getColumn().getTable().getId());
		buf.append("(");
		buf.append(this.getColumn().getId());
		buf.append(")'>\n ");
		buf.append("<option value='-1' selected='true'>Select Donor Group</option>\n");
		for (FilterItem item : getItemProvider().getItems()) {
			buf.append("<option value='");
			buf.append(item.getId());
			buf.append("'");
			//if (item.getId().equals(masterColumn.getActiveItemId())){
				//buf.append(" selected='true' ");
			//}
			buf.append(">");
			buf.append(item.getName());
			buf.append("</option>\n");
		}
		buf.append("</select>");
		return buf.toString();
	}

}

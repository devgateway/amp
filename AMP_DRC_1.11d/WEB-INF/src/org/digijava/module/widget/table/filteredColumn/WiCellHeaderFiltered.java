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
		buf.append(")'");
		buf.append(" idColumn='");
		buf.append(this.getColumn().getId());
		buf.append("' idTable='");
		buf.append(this.getColumn().getTable().getId());
		buf.append("'>\n ");
		
		//AMP-4097 start
//		buf.append("<option value='-1'>");
//		buf.append("Select Donor Group");
//		buf.append("</option>");
		//AMP-4097 end
		
		for (FilterItem item : getItemProvider().getItems()) {
			buf.append("<option value='");
			buf.append(item.getId());
			buf.append("'");
			if (item.getId().equals(masterColumn.getActiveItemId())){
				buf.append(" selected='true' ");
			}
			buf.append(">");
			buf.append(item.getName());
			buf.append("</option>\n");
		}
		buf.append("</select>");
		return buf.toString();
	}

}

package org.digijava.module.widget.table;

public final class FilterColumnUtil {
	private FilterColumnUtil(){
		
	}
	
	public static WiColumn newColumn(Long columnId){
		WiColumn column = null;
		ColumnType type = column.getType();
		if (type == ColumnType.STANDARD){
			column = new WiColumnStandard();
		}
		if (type == ColumnType.CALCULATED){
			column = new WiColumnCalculated();
		}
		if (type == ColumnType.FILTER){
			column = new WiColumnDropDownFilter();
		}
		return column;
	}
	
	public static FilterItemProvider getItemsProvider(ColumnType type){
		return null;
	}
}

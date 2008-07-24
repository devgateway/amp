package org.digijava.module.widget.table.util;

import org.digijava.module.widget.dbentity.AmpDaColumn;
import org.digijava.module.widget.dbentity.AmpDaTable;
import org.digijava.module.widget.dbentity.AmpDaValue;
import org.digijava.module.widget.table.WiCell;
import org.digijava.module.widget.table.WiColumn;
import org.digijava.module.widget.table.WiTable;

public final class TableWidgetUtil {

	private TableWidgetUtil(){
		//Do not even try to instantiate this class, even from itself.
		throw new AssertionError();
	}
	
	public static WiTable buildTable(AmpDaTable dbTable){
		return null;
	}
	
	public static WiColumn buildColumn(AmpDaColumn dbColumn){
		return null;
	}
	
	public static WiCell buildCell(AmpDaValue value){
		return null;
	}
	
}

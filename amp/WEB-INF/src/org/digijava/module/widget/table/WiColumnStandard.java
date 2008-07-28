package org.digijava.module.widget.table;

import java.util.Set;

import org.digijava.module.widget.dbentity.AmpDaColumn;
import org.digijava.module.widget.dbentity.AmpDaValue;
import org.digijava.module.widget.table.util.TableWidgetUtil;

public class WiColumnStandard extends WiColumn {

	public WiColumnStandard(){
		
	}
	
	public WiColumnStandard(AmpDaColumn dbColumn) {
		super(dbColumn);
		Set<AmpDaValue> values = dbColumn.getValues();
		if (values!=null){
			for (AmpDaValue value : values) {
				WiCell cell = TableWidgetUtil.newCell(value);
				cell.setColumn(this);
				setCell(cell);
			}
		}
	}

}

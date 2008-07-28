package org.digijava.module.widget.table.filteredColumn;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.digijava.module.widget.dbentity.AmpDaColumnFilter;
import org.digijava.module.widget.dbentity.AmpDaValue;
import org.digijava.module.widget.table.WiCell;
import org.digijava.module.widget.table.WiColumn;
import org.digijava.module.widget.table.WiColumnStandard;
import org.digijava.module.widget.table.util.TableWidgetUtil;

public class WiColumnDropDownFilter extends WiColumn {
	private FilterItemProvider provider;
	private Long filterState = new Long(0);
	private Map<Long, WiColumn> columns = new HashMap<Long, WiColumn>();

	public WiColumnDropDownFilter(AmpDaColumnFilter dbColumn) {
		super(dbColumn);
		provider = TableWidgetUtil.getFilterItemProvider(dbColumn);
		Set<AmpDaValue> values = dbColumn.getValues();
		for (AmpDaValue value : values) {
			WiCellFiltered cell = (WiCellFiltered)TableWidgetUtil.newCell(value);
			WiColumn col = columns.get(dbColumn.getFilterItemProvider());
			if (col == null){
				col = new WiColumnStandard();
				FilterItem item = provider.getItem(cell.getFilterItemId());
				col.setId(item.getId());
				col.setName(item.getName());
				columns.put(col.getId(), col);
			}
			col.setCell(cell);
		}
	}

	public void setProvider(FilterItemProvider provider) {
		this.provider = provider;
	}

	public FilterItemProvider getProvider() {
		return provider;
	}
	
	public void setFilterOn(Long filter){
		this.filterState = filter;
	}

	public WiColumn getSelectedColumn(){
		return columns.get(filterState);
	}
	
	@Override
	public WiCell getCell(Long rowPk) {
		return getSelectedColumn().getCell(rowPk);
	}

	@Override
	public void setCell(WiCell cell) {
		getSelectedColumn().setCell(cell);
	}
	
}

package org.digijava.module.widget.table.filteredColumn;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.widget.dbentity.AmpDaColumnFilter;
import org.digijava.module.widget.dbentity.AmpDaValue;
import org.digijava.module.widget.dbentity.AmpDaValueFiltered;
import org.digijava.module.widget.table.WiCell;
import org.digijava.module.widget.table.WiColumn;
import org.digijava.module.widget.table.util.TableWidgetUtil;
import org.hibernate.Session;

/**
 * Drop down filter column.
 * @author Irakli Kobiashvili
 *
 */
public class WiColumnDropDownFilter extends WiColumn {
	private FilterItemProvider provider;
	private Long activeItemId = new Long(0);
	private Map<Long, WiColumnFilterSubColumn> columns = new HashMap<Long, WiColumnFilterSubColumn>();

	public WiColumnDropDownFilter(AmpDaColumnFilter dbColumn, HttpServletRequest request) {
		super(dbColumn);
		Site site = RequestUtils.getSite(request);
	 	org.digijava.kernel.entity.Locale navigationLanguage = RequestUtils.getNavigationLanguage(request);        
	 	String siteId = site.getId()+"";
	 	String locale = navigationLanguage.getCode();
	 	//get provider of the drop down filter items. current donors.
	 	provider = TableWidgetUtil.getFilterItemProvider(dbColumn, siteId, locale);
		//create map from filter items.
		Map<Long, WiColumnFilterSubColumn> columnMap = createColumnsFromProvider(provider, this);
		//setup currently selected item in drop down
		if (columnMap.values().size()>0){
			this.setActiveItemId(new Long(columnMap.values().iterator().next().getFilterItemId()));
//			this.setActiveItemId(new Long(-1));
		}
		//store map in this specialized filter column.
		this.columns = columnMap;
		//setup cells from db values
		Set<AmpDaValue> values = dbColumn.getValues();
		for (AmpDaValue value : values) {
			AmpDaValueFiltered valueFiltered = (AmpDaValueFiltered)value;
			//create new filter cell for each value
			WiCellFiltered cell = (WiCellFiltered)TableWidgetUtil.newCell(value);
			//search for values corresponding sub column created from filter items. 
			WiColumnFilterSubColumn col = columns.get(cell.getFilterItemId());
			//if there is no such column create new one.
			if (col == null){
				col = new WiColumnFilterSubColumn();
				FilterItem item = provider.getItem(valueFiltered.getFilterItemId());
				col.setId(dbColumn.getId());
				col.setFilterItemId(valueFiltered.getFilterItemId());//this column is for this filter item
				col.setName(item.getName());
				col.setCssClass(dbColumn.getCssClass());
				col.setOrderId(dbColumn.getOrderNo());
				col.setPattern(dbColumn.getPattern());
				columns.put(col.getFilterItemId(), col);
			}
			//set cell to the sub column
			col.setCell(cell);
			cell.setColumn(col);
		}
	}

	public WiColumnDropDownFilter(AmpDaColumnFilter dbColumn) {
		super(dbColumn);
		//get provider of the drop down filter items. current donors.
		provider = TableWidgetUtil.getFilterItemProvider(dbColumn);
		//create map from filter items.
		Map<Long, WiColumnFilterSubColumn> columnMap = createColumnsFromProvider(provider, this);
		//setup currently selected item in drop down
		if (columnMap.values().size()>0){
			this.setActiveItemId(new Long(columnMap.values().iterator().next().getFilterItemId()));
//			this.setActiveItemId(new Long(-1));
		}
		//store map in this specialized filter column.
		this.columns = columnMap;
		//setup cells from db values
		Set<AmpDaValue> values = dbColumn.getValues();
		for (AmpDaValue value : values) {
			AmpDaValueFiltered valueFiltered = (AmpDaValueFiltered)value;
			//create new filter cell for each value
			WiCellFiltered cell = (WiCellFiltered)TableWidgetUtil.newCell(value);
			//search for values corresponding sub column created from filter items. 
			WiColumnFilterSubColumn col = columns.get(cell.getFilterItemId());
			//if there is no such column create new one.
			if (col == null){
				col = new WiColumnFilterSubColumn();
				FilterItem item = provider.getItem(valueFiltered.getFilterItemId());
				col.setId(dbColumn.getId());
				col.setFilterItemId(valueFiltered.getFilterItemId());//this column is for this filter item
				col.setName(item.getName());
				col.setCssClass(dbColumn.getCssClass());
				col.setOrderId(dbColumn.getOrderNo());
				col.setPattern(dbColumn.getPattern());
				columns.put(col.getFilterItemId(), col);
			}
			//set cell to the sub column
			col.setCell(cell);
			cell.setColumn(col);
		}
	}

	private Map<Long,WiColumnFilterSubColumn> createColumnsFromProvider(FilterItemProvider provider,WiColumn masterColumn){
		List<FilterItem> items = provider.getItems();
		Map<Long, WiColumnFilterSubColumn> columns = new HashMap<Long,WiColumnFilterSubColumn>(items.size()+1);
		
		//AMP-4097, pint 2.  do not like this idea...
//		WiColumnFilterSubColumn dummyColumn = new WiColumnFilterSubColumn();
//		dummyColumn.setId(masterColumn.getId());
//		dummyColumn.setFilterItemId(new Long(-1));
//		dummyColumn.setName("Select Donor Group");
//		columns.put(dummyColumn.getFilterItemId(), dummyColumn);
		//AMP-4097 end
		
		for (FilterItem item : items) {
			WiColumnFilterSubColumn column = new WiColumnFilterSubColumn();
			column.setId(masterColumn.getId());
			column.setFilterItemId(item.getId());
			column.setName(item.getName());
			column.setOrderId(masterColumn.getOrderId());
			column.setCssClass(masterColumn.getCssClass());
			column.setPattern(masterColumn.getPattern());
			columns.put(column.getFilterItemId(),column);
		}
		return columns;
	}
	
	/**
	 * Delegates to active sub column 
	 */
	@Override
	public WiCell getCell(Long rowPk) {
		return getActiveSubColumn().getCell(rowPk);
	}

	/**
	 * Delegates to active sub column
	 */
	@Override
	public void setCell(WiCell cell) {
		getActiveSubColumn().setCell(cell);
	}

	@Override
	public int getType() {
		return WiColumn.FILTER;
	}

	
	@Override
	public void saveData(Session dbSession) throws DgException {
		Collection<WiColumnFilterSubColumn> subColumns = this.columns.values();
		for (WiColumnFilterSubColumn subColumn : subColumns) {
			subColumn.saveData(dbSession);
		}
	}

	/**
	 * Delegates to all sub columns because filter column has several sub columns. 
	 */
	@Override
	public void replacePk(Long oldPk, Long newPk) {
		for (WiColumnFilterSubColumn subColumn : this.columns.values()) {
			subColumn.replacePk(oldPk, newPk);
		}
	}
	
	public void setProvider(FilterItemProvider provider) {
		this.provider = provider;
	}

	public FilterItemProvider getProvider() {
		return provider;
	}
	
	public void setFilterOn(Long filterItemId){
		this.setActiveItemId(filterItemId);
	}

	public WiColumnFilterSubColumn getActiveSubColumn(){
		return columns.get(getActiveItemId());
	}

	public void setActiveItemId(Long activeItemId) {
		this.activeItemId = activeItemId;
	}

	public Long getActiveItemId() {
		return activeItemId;
	}
	
}

package org.digijava.module.widget.table.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.widget.dbentity.AmpDaColumn;
import org.digijava.module.widget.dbentity.AmpDaColumnFilter;
import org.digijava.module.widget.dbentity.AmpDaTable;
import org.digijava.module.widget.dbentity.AmpDaValue;
import org.digijava.module.widget.dbentity.AmpDaValueFiltered;
import org.digijava.module.widget.table.WiCell;
import org.digijava.module.widget.table.WiCellHeader;
import org.digijava.module.widget.table.WiCellStandard;
import org.digijava.module.widget.table.WiColumn;
import org.digijava.module.widget.table.WiColumnStandard;
import org.digijava.module.widget.table.WiRow;
import org.digijava.module.widget.table.WiTable;
import org.digijava.module.widget.table.calculated.WiColumnCalculated;
import org.digijava.module.widget.table.filteredColumn.FilterItem;
import org.digijava.module.widget.table.filteredColumn.FilterItemProvider;
import org.digijava.module.widget.table.filteredColumn.WiCellFiltered;
import org.digijava.module.widget.table.filteredColumn.WiCellHeaderFiltered;
import org.digijava.module.widget.table.filteredColumn.WiColumnDropDownFilter;

/**
 * Utility methods for table widgets and its children.
 * @author Irakli Kobiashvili
 *
 */
public final class TableWidgetUtil {

	/**
	 * Restrict instance creation for this class.
	 */
	private TableWidgetUtil(){
		//Do not even try to instantiate this class, even from itself.
		throw new AssertionError();
	}
	
	/**
	 * Not very useful table widget factory.
	 * @param dbTable
	 * @return
	 * @throws DgException
	 */
	public static WiTable newTable(AmpDaTable dbTable) throws DgException{
		WiTable table = new WiTable.TableBuilder(dbTable.getId()).name("Some Name").showTitle(true).build();
		return table;
	}
	
	/**
	 * Creates new column. Exact type of the result depends on the parameter.
	 * @param dbColumn
	 * @return
	 */
	public static WiColumn newColumn(AmpDaColumn dbColumn){
		int type = (dbColumn.getColumnType()==null)?WiColumn.STANDARD:dbColumn.getColumnType();
		WiColumn column = null;
		if (type == WiColumn.STANDARD){
			column = new WiColumnStandard(dbColumn);
		}else if (type == WiColumn.CALCULATED){
			column =new WiColumnCalculated();
		}else if (type == WiColumn.FILTER && (dbColumn instanceof AmpDaColumnFilter)){
			column = new WiColumnDropDownFilter((AmpDaColumnFilter)dbColumn);
		}
		return column;
	}
	
	/**
	 * Creates new widget table cell.
	 * Exact type depends on value object.
	 * @param value
	 * @return
	 */
	public static WiCell newCell(AmpDaValue value){
		WiCell cell = null;
		if (value instanceof AmpDaValueFiltered){
			cell = new WiCellFiltered();
			((WiCellFiltered)cell).setFilterItemId(((AmpDaValueFiltered)value).getFilterItemId());
		}else{
			cell = new WiCellStandard(value);
		}
		cell.setId(value.getId());
		cell.setPk(value.getPk());
		cell.setValue(value.getValue());
		return cell;
	}

	/**
	 * Creates new header cell for header row.
	 * @param column
	 * @return
	 */
	public static WiCell newHeaderCell(WiColumn column){
		WiCell cell = null;
		if (column instanceof WiColumnDropDownFilter){
			WiColumnDropDownFilter filterCol = (WiColumnDropDownFilter)column;
			cell = new WiCellHeaderFiltered();
			cell.setValue(filterCol.getSelectedColumn().getName());
		}else{
			cell = new WiCellHeader();
			cell.setValue(column.getName());
		}
		cell.setColumn(column);
		cell.setPk(0L);
		return cell;
	}
	
	/**
	 * Loads table entity from db.
	 * @param id db key of the table.
	 * @return
	 * @throws DgException
	 */
	public static AmpDaTable getDbTable(Long id) throws DgException{
		return org.digijava.module.widget.util.TableWidgetUtil.getTableWidget(id);
	}
	
	/**
	 * Returns provider of filter items. Which provider is returned depends on col.filterItemProvider property.
	 * TODO currently returns always same - donor provider.
	 * @param col
	 * @return
	 */
	public static FilterItemProvider getFilterItemProvider(AmpDaColumnFilter col){
		//TODO this may return different providers depending on col.filterItemProvider
		return new DonorFilter();
	}
	
	/**
	 * private implementation of filter data provider for donors.
	 * @author Irakli Kobiashvili
	 *
	 */
	private static class DonorFilter implements FilterItemProvider{
		
		private Map<Long, FilterItem> itemsById = new HashMap<Long, FilterItem>();
		private List<FilterItem> items = new ArrayList<FilterItem>();
		
		@SuppressWarnings("unchecked")
		public DonorFilter(){
			Collection<AmpOrganisation> donors = DbUtil.getAllDonorOrgs();
			if (donors==null){
				donors = new ArrayList<AmpOrganisation>();
			}
			for (AmpOrganisation org : donors) {
				FilterItem item= new DonorFilterItem(org);
				items.add(item);
				itemsById.put(item.getId(), item);
			}
			
		}
		
		public FilterItem getItem(Long id) {
			return itemsById.get(id);
		}

		public List<FilterItem> getItems() {
			return items;
		}
		
	}
	
	/**
	 * item implementation for donor.
	 * Probably AmpOrganization can implement this, but if there is no conflict with exiting fields.
	 * @author Irakli Kobiashvili
	 *
	 */
	private static class DonorFilterItem implements FilterItem{
		private Long id;
		private String name;
		
		public DonorFilterItem(AmpOrganisation org){
			this.id = org.getAmpOrgId();
			this.name = org.getName();
		}
		
		public Long getId() {
			return id;
		}

		public String getName() {
			return name;
		}
		
	}
	
	/**
	 * Compares columns with their column order numbers.
	 * Used for cell ordering in rows by 
	 * When table is created user specifies order of the columns, so each row should provide cells in this order. 
	 * If cell has no column then 0 is assumed. may be not good idea.
	 * @author Irakli Kobiashvili
	 *
	 */
	public static class WiCellColumnOrderComparator implements Comparator<WiCell>{
		public int compare(WiCell cell1, WiCell cell2) {
			Integer o1=(cell1.getColumn()!=null && cell1.getColumn().getOrderId()!=null)?cell1.getColumn().getOrderId():new Integer(0);
			Integer o2=(cell2.getColumn()!=null && cell2.getColumn().getOrderId()!=null)?cell2.getColumn().getOrderId():new Integer(0);
			return o1.compareTo(o2);
		}
		
	}

	/**
	 * Compares columns with their order numbers.
	 * @author Irakli Kobiashvili
	 *
	 */
	public static class WiColumnOrderComparator implements Comparator<WiColumn>{
		public int compare(WiColumn col1, WiColumn col2) {
			Integer o1=(col1.getOrderId()!=null)?col1.getOrderId():new Integer(0);
			Integer o2=(col2.getOrderId()!=null)?col2.getOrderId():new Integer(0);
			return o1.compareTo(o2);
		}

	}
	
	/**
	 * Compares rows with PK filed.
	 * Because value of the PK is generated by increment when data is entered, this field is also order number for rows.
	 * @author Irakli Kobiashvili
	 *
	 */
	public static class WiRowPkComparator implements Comparator<WiRow>{
		public int compare(WiRow row1, WiRow row2) {
			Long pk1=(row1.getPk()==null)?0L:row1.getPk();
			Long pk2=(row2.getPk()==null)?0L:row2.getPk();
			return pk1.compareTo(pk2);
		}
		
	}
	
	
	
}

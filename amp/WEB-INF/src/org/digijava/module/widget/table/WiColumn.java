package org.digijava.module.widget.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.utils.AmpCollectionUtils;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.widget.dbentity.AmpDaColumn;
import org.digijava.module.widget.dbentity.AmpDaValue;
import org.digijava.module.widget.table.WiTable.TableProxy;
import org.digijava.module.widget.table.util.TableWidgetUtil;
import org.hibernate.Session;

public abstract class WiColumn {
	
	public static final int STANDARD = 1;
	public static final int CALCULATED = 2;
	public static final int FILTER = 3;
	
	private Long id;
	private String name;
	private Integer orderId;
	private String pattern;
	private String cssClass;
	private int type = STANDARD;
	private Map<Long, WiCell> cells = new HashMap<Long, WiCell>();
	private List<WiCell> trashedCells = new ArrayList<WiCell>();
	private TableProxy tableProxy;
	
	//TODO do we need this constructor?
	public WiColumn(){
		
	}

	/**
	 * Constructs column from db column entity.
	 * @param dbColumn
	 */
	public WiColumn(AmpDaColumn dbColumn){
		this.id = dbColumn.getId();
		this.name = dbColumn.getName();
		this.setPattern(dbColumn.getPattern());
		this.setCssClass(dbColumn.getCssClass());
		this.setOrderId(dbColumn.getOrderNo());
	}
	
	/**
	 * Persists cells.
	 * @param dbSession
	 * @throws DgException
	 */
	public abstract void saveData(Session dbSession) throws DgException;
	
	/**
	 * Create empty cell of required subclass.
	 * @return each concrete class will create its own type of cell.
	 */
	public abstract WiCell createCell();
	
	/**
	 * Create cell from db 
	 * @param value
	 * @return
	 */
	public abstract WiCell createCell(AmpDaValue value);

	/**
	 * Removes cell from column, moves all cells below removed one up.
	 * @param pk
	 * @return
	 */
	public WiCell removeCellWithPk(Long pk){
		WiCell removedCell = cells.remove(pk);
		List<WiCell> cells = getAllCells();
		Collections.sort(cells,new TableWidgetUtil.WiCellPkComparator());
		long toDecrement = pk.longValue()+1;
		for (WiCell cell : cells) {
			if (cell.getPk().longValue() == toDecrement){
				cell.setPk(cell.getPk()-1);
				toDecrement++;
			}
		}
		this.cells = AmpCollectionUtils.createMap(cells, new TableWidgetUtil.WiCellPkKeyResolver());
		addTrashedCell(removedCell);
		return removedCell;
	}

	/**
	 * Adds new empty cell at specified position.
	 * @param index
	 */
	public void addCellAt(long index){
		List<WiCell> oldCells = getAllCells();
		//order cells by pk's
		Collections.sort(oldCells, new TableWidgetUtil.WiCellPkComparator());
		//"move" pk's down starting from 'index'
		long toIncrement = index;
		for (WiCell cell : oldCells) {
			if (cell.getPk().longValue()==toIncrement){
				cell.setPk(new Long(cell.getPk().longValue()+1));
				toIncrement = cell.getPk();
			}
		}
		//create new cell
		WiCell newCell = this.createCell();
		newCell.setPk(new Long(index));
		//rebuild map of cells cos keys in map are old while pk's of cells have been changed.
		this.cells = AmpCollectionUtils.createMap(oldCells, new TableWidgetUtil.WiCellPkKeyResolver());
		//add new cell to that map.
		this.cells.put(newCell.getPk(), newCell);
		
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public WiCell getCell(Long rowPk){
		WiCell cell = cells.get(rowPk);
		if (cell ==null){
			cell = createCell();
			cell.setPk(rowPk);
			this.setCell(cell);
		}
		return cell;
	}

	public void clearTrashedCells(){
		this.trashedCells.clear();
	}
	
	public void setCell(WiCell cell){
		cells.put(cell.getPk(), cell);
	}

	public List<WiCell> getAllCells() {
		return new ArrayList<WiCell>(cells.values());
	}

	public List<WiCell> getAllTrashedCells() {
		return trashedCells;
	}

	public void addTrashedCell(WiCell cell) {
		this.trashedCells.add(cell);
	}
	public int getType() {
		return type;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getPattern() {
		return pattern;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	public String getCssClass() {
		return cssClass;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setTableProxy(TableProxy tableProxy) {
		this.tableProxy = tableProxy;
	}

	public TableProxy getTableProxy() {
		return tableProxy;
	}
	
	public WiTable getTable(){
		return this.tableProxy.getTable();
	}
}

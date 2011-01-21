package org.digijava.module.widget.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.digijava.kernel.exception.DgException;
import org.digijava.module.widget.dbentity.AmpDaColumn;
import org.digijava.module.widget.table.WiTable.TableProxy;
import org.digijava.module.widget.table.util.TableWidgetUtil;
import org.hibernate.Session;

public abstract class WiColumn {
	
	//TODO if hibernate 2.1x understands Enums let's use them, but I do not think so cos 1.4 != 1.5 :(
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
	        private List<WiCell>trashedCells = new ArrayList<WiCell>();
	private TableProxy tableProxy;
	
	public WiColumn(){
		
	}

	public WiColumn(AmpDaColumn dbColumn){
		this.id = dbColumn.getId();
		this.name = dbColumn.getName();
		this.setPattern(dbColumn.getPattern());
		this.setCssClass(dbColumn.getCssClass());
		this.setOrderId(dbColumn.getOrderNo());
	}
	
	public WiCell removeCellWithPk(Long pk){
		return cells.remove(pk);
	}
	
	public abstract void saveData(Session dbSession) throws DgException;
	
	public abstract void replacePk(Long oldPk, Long newPk);
	
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
//	public void setCells(Map<Long, WiCell> cells) {
//		this.cells = cells;
//	}
//	public Map<Long, WiCell> getCells() {
//		return cells;
//	}
	public WiCell getCell(Long rowPk){
		WiCell cell = cells.get(rowPk);
		if (cell ==null){
			cell = TableWidgetUtil.newCell(this);
			cell.setPk(rowPk);
			this.setCell(cell);
		}
		return cell;
	}
	public void setCell(WiCell cell){
		cells.put(cell.getPk(), cell);
	}
	public List<WiCell> getAllCells(){
		return new ArrayList<WiCell>(cells.values());
	}
	
        public List<WiCell> getAllTrashedCells(){		return trashedCells;	}                 public void addTrashedCell(WiCell cell){		this.trashedCells.add(cell);	}//	public void setType(int type) {
//		this.type = type;
//	}
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

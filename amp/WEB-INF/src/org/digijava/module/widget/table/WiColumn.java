package org.digijava.module.widget.table;

import java.util.HashMap;
import java.util.Map;

public abstract class WiColumn {
	private Long id;
	private String name;
	private ColumnType type;
	private Map<Long, WiCell> cells = new HashMap<Long, WiCell>();
	
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
	public void setType(ColumnType type) {
		this.type = type;
	}
	public ColumnType getType() {
		return type;
	}
	public void setCells(Map<Long, WiCell> cells) {
		this.cells = cells;
	}
	public Map<Long, WiCell> getCells() {
		return cells;
	}
	public WiCell getCellByPk(Long pk){
		return cells.get(pk);
	}
	public void setCell(WiCell cell){
		cells.put(cell.getPk(), cell);
	}
}

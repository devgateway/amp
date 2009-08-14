package org.digijava.module.widget.oldTable;

import java.util.HashMap;
import java.util.Map;

import org.digijava.module.widget.dbentity.AmpDaColumn;
import org.digijava.module.widget.dbentity.AmpDaValue;

/**
 * Column Helper
 * @author Irakli Kobiashvili
 *
 */
public class DaColumn {

	private Long id;
	private Integer type;
	private String name;
	private Integer orderNo;
	private Map<Long, DaCell> cellsByPK;
	
	public DaColumn(){
		cellsByPK = new HashMap<Long, DaCell>();
	}

	public DaColumn(AmpDaColumn dbColumn){
		this.id = dbColumn.getId();
		this.type = dbColumn.getColumnType();
		this.name = dbColumn.getName();
		this.orderNo = dbColumn.getOrderNo();
		cellsByPK = new HashMap<Long, DaCell>();
		if (dbColumn.getValues()==null){
			return;
		}
		for (AmpDaValue dbValue : dbColumn.getValues()) {
			addCell(new DaCell(dbValue));
		}
	}
	
	public void addCell(DaCell cell){
		cellsByPK.put(cell.getRowPk(), cell);
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
}

package org.digijava.module.widget.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.digijava.module.widget.web.HtmlGenerator;

public abstract class WiRow implements HtmlGenerator{

	private Long pk;
	private Map<Long, WiColumn> columnsByIds = new HashMap<Long, WiColumn>();
	private List<WiColumn> columns = new ArrayList<WiColumn>();
	
	public WiRow(Long pk){
		this(pk, new ArrayList<WiColumn>());
		
	}
	
	public WiRow(Long pk,List<WiColumn> columns){
		this.pk = pk;
		this.columns = columns;
		//TODO create map from this columns for for access
	}
	
	public List<WiCell> getCells() {
		List<WiCell> result = new ArrayList<WiCell>(columns.size());
		for (WiColumn column : columns) {
			result.add(column.getCellByPk(this.pk));
		}
		return result;
	}

	public String generateHtml() {
		StringBuffer result = new StringBuffer("\t<TR");
		result.append(">\n");
		List<WiCell> cells = getCells();
		for (WiCell cell : cells) {
			result.append(cell.generateHtml());
		}
		result.append("\t</TR>\n");
		return result.toString();
	}
	
	public void updateCell(WiCell cell,Long columnId){
		cell.setPk(this.pk);
		columnsByIds.get(columnId).setCell(cell);
	}
}

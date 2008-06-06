package org.digijava.module.gis.widget.table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.digijava.module.gis.dbentity.AmpDaColumn;

/**
 * Table row.
 * @author Irakli Kobiashvili
 *
 */
public class DaRow implements HtmlGenerator{

	private Long id;
	private String pk;
	private String cssClass;
	private List<DaCell> cells;
	private boolean isHeader;
	
	public DaRow(){
		this.isHeader=false;
	}
	
	public DaRow(Collection<AmpDaColumn> columns){
		this.isHeader = true;
		cells = new ArrayList<DaCell>(); 
		for (AmpDaColumn col : columns) {
			DaCell cell = new DaCell(col);
			cells.add(cell);
		}
	}

	public String generateHtml() {
		String result="<TR>";
		
		if (cells!=null){
			for (DaCell cell : cells) {
				result+=cell.generateHtml();
			}
		}else{
			result+="<TD/>";
		}
		
		result+="</TR>";
		return result;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public String getCssClass() {
		return cssClass;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	public List<DaCell> getCells() {
		return cells;
	}

	public void setCells(List<DaCell> cells) {
		this.cells = cells;
	}

	public boolean isHeader() {
		return isHeader;
	}

	public void setHeader(boolean isHeader) {
		this.isHeader = isHeader;
	}

}

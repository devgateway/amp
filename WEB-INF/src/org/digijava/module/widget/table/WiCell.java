package org.digijava.module.widget.table;

import org.digijava.module.widget.web.HtmlGenerator;

public abstract class WiCell implements HtmlGenerator{
	
	private Long id;
	private Long pk;
	private WiColumn column;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPk() {
		return pk;
	}

	public void setPk(Long pk) {
		this.pk = pk;
	}

	public abstract String getValue();

	public abstract void setValue(String value);

	public String generateHtml() {
		StringBuffer result = new StringBuffer("\t\t<TD");
		result.append(">");
		result.append(getValue());
		result.append("</TD>\n");
		return result.toString();
	}

	public void setColumn(WiColumn column) {
		this.column = column;
	}

	public WiColumn getColumn() {
		return column;
	}

}

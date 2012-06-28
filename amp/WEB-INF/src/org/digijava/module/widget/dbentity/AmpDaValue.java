package org.digijava.module.widget.dbentity;

import java.io.Serializable;

/**
 * Represents one value cell in db.
 * @author Irakli Kobiashvili
 *
 */
public class AmpDaValue implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private Long pk;
	private String value;
	private AmpDaColumn column;
	
	public void replaceValues(AmpDaValue val){
		this.id = val.getId();
		this.pk = val.getPk();
		this.value = val.getValue();
		this.column = val.getColumn();
	}
	
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
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public AmpDaColumn getColumn() {
		return column;
	}
	public void setColumn(AmpDaColumn column) {
		this.column = column;
	}
}

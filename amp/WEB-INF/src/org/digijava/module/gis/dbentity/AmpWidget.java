package org.digijava.module.gis.dbentity;

import java.io.Serializable;

/**
 * Abstract widget.
 * @author Irakli Kobiashvili
 *
 */
public class AmpWidget implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String code;
	private AmpDaWidgetPlace place;
	
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public AmpDaWidgetPlace getPlace() {
		return place;
	}
	public void setPlace(AmpDaWidgetPlace place) {
		this.place = place;
	}

}

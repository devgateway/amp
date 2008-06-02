package org.digijava.module.gis.dbentity;

import java.io.Serializable;

/**
 * Teaser place.
 * @author Irakli Kobiashvili
 *
 */
public class AmpDaWidgetPlace implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private String code;
	private String module;
	private String moduleInstance;
	private AmpDaTable widget;
	
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
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getModuleInstance() {
		return moduleInstance;
	}
	public void setModuleInstance(String moduleInstance) {
		this.moduleInstance = moduleInstance;
	}
	public AmpDaTable getWidget() {
		return widget;
	}
	public void setWidget(AmpDaTable widget) {
		this.widget = widget;
	}
}

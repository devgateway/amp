package org.digijava.module.widget.dbentity;

import java.io.Serializable;
import java.util.Date;

/**
 * Teaser place.
 * Places are defined in digi layout definition as parameter to teaser.
 * Same teaser inserted on different places may have different parameters.
 * These parameters are retrieved from teaser context in teaser TileAction execute method.
 * @author Irakli Kobiashvili
 *
 */
public class AmpDaWidgetPlace implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final int TABLE_PLACE = 1; 
	public static final int CHART_PLACE = 2; 

	private Long id;
	private String name;
	private String code;
	/**
	 * DiGi module name.
	 * This is set automatically from teaser context on each update.
	 */
	private String module;
	/**
	 * DiGi module instance.
	 * This is set at save too.
	 */
	private String moduleInstance;
	/**
	 * Last render time.
	 * This should be updated on each render to see which places are not used any more.
	 */
	private Date lastRendered;
	/**
	 * Type specifies which kind of teaser is on this place.
	 * Use constants defined in this class for values. 
	 */
	private Integer placeType;
	/**
	 * Widget Assigned to this place.
	 */
	private AmpWidget assignedWidget;
	
	
	
	
	public AmpWidget getAssignedWidget() {
		return assignedWidget;
	}
	public void setAssignedWidget(AmpWidget assignedWidget) {
		this.assignedWidget = assignedWidget;
	}
	public Date getLastRendered() {
		return lastRendered;
	}
	public void setLastRendered(Date lastRendered) {
		this.lastRendered = lastRendered;
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
	public Integer getPlaceType() {
		return placeType;
	}
	public void setPlaceType(Integer placeType) {
		this.placeType = placeType;
	}
}

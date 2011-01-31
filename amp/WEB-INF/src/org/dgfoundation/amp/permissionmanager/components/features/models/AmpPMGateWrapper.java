/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.models;

import java.io.Serializable;

/**
 * @author dan
 *
 */
public class AmpPMGateWrapper implements Serializable, Comparable{

	private Long id;
	private String name;
	private Boolean readFlag = Boolean.FALSE;
	private Boolean editFlag = Boolean.FALSE;
	private String parameter;
	private Class gate;


	public AmpPMGateWrapper() {
		// TODO Auto-generated constructor stub
	}

	public AmpPMGateWrapper(String name, Boolean readFlag, Boolean editFlag) {
		super();
		this.name = name;
		this.readFlag = readFlag;
		this.editFlag = editFlag;
	}

	public AmpPMGateWrapper(String name) {
		super();
		this.name = name;
	}

	public AmpPMGateWrapper(Long id, String name, Boolean readFlag, Boolean editFlag) {
		super();
		this.id = id;
		this.name = name;
		this.readFlag = readFlag;
		this.editFlag = editFlag;
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

	public Boolean getReadFlag() {
		return readFlag;
	}

	public void setReadFlag(Boolean readFlag) {
		this.readFlag = readFlag;
	}

	public Boolean getEditFlag() {
		return editFlag;
	}

	public void setEditFlag(Boolean editFlag) {
		this.editFlag = editFlag;
	}
	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	
	public Class getGate() {
		return gate;
	}

	public void setGate(Class gate) {
		this.gate = gate;
	}

	public AmpPMGateWrapper(Long id, String name,String parameter, Class gate, Boolean readFlag, Boolean editFlag) {
		super();
		this.id = id;
		this.name = name;
		this.readFlag = readFlag;
		this.editFlag = editFlag;
		this.parameter = parameter;
		this.gate = gate;
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		if(!(o instanceof AmpPMGateWrapper)) return -1;
		AmpPMGateWrapper obj = (AmpPMGateWrapper)o;
		return this.getId().compareTo(obj.getId());
	}

}

package org.dgfoundation.amp.onepager.helper;

import java.io.Serializable;

public class OnepagerSection implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String className;
	private int position;
	private boolean folded;
	private boolean dependent;
	private String dependentClassName;
	
	public OnepagerSection(String name, String className, int position,
			boolean folded, boolean dependent, String dependentClassName) {
		super();
		this.name = name;
		this.className = className;
		this.position = position;
		this.folded = folded;
		this.dependent = dependent;
		this.dependentClassName = dependentClassName;
	}

	public OnepagerSection(String name, String className, int position,
			boolean folded) {
		this(name, className, position, folded, false, null);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public boolean isFolded() {
		return folded;
	}
	public void setFolded(boolean folded) {
		this.folded = folded;
	}
	public boolean isDependent() {
		return dependent;
	}
	public void setDependent(boolean dependent) {
		this.dependent = dependent;
	}
	public String getDependentClassName() {
		return dependentClassName;
	}
	public void setDependentClassName(String dependentClassName) {
		this.dependentClassName = dependentClassName;
	}
}

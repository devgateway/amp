/**
 * 
 */
package org.digijava.module.dataExchange.pojo;

import java.util.ArrayList;

import org.digijava.module.dataExchange.jaxb.CodeValueType;

/**
 * @author dan
 *
 */
public abstract class DELog {

	private String description;
	private String entityName;
	private ArrayList<DELog> items;
	
	public ArrayList<DELog> getItems() {
		return items;
	}

	public void setItems(ArrayList<DELog> items) {
		this.items = items;
	}

	public DELog() {
	}
	
	public DELog(CodeValueType cvt) {
		
		this.setEntityName(cvt.getValue()+" "+cvt.getCode());
	}
	
	public DELog(String description, String entityName) {
		super();
		this.description = description;
		this.entityName = entityName;
	}

	public DELog(String entityName) {
		super();
		this.entityName = entityName;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public String display() {
		// TODO Auto-generated method stub
			return  this.getEntityName()+ this.getDescription();
	}
	public String display(String separator) {
		// TODO Auto-generated method stub
		return  this.getEntityName()+separator+ this.getDescription();
	}
	
	
}

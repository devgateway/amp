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

	public abstract String getLogType();
	
	public boolean equalType(String s){
		return this.getLogType().equals(s);
	}
	
	public void setItems(ArrayList<DELog> items) {
		this.items = items;
	}

	public DELog() {
	}
	
	public DELog(CodeValueType cvt) {
		String result = "";
		if(cvt!=null && cvt.getValue()!=null && cvt.getValue().trim()!="" )
			result +=cvt.getValue();

		if(cvt!=null && cvt.getCode()!=null && cvt.getCode().trim()!="" )
			result +=" with code: "+cvt.getCode();

		this.setEntityName(result);
		
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
		String result = "";
		if(this.getEntityName()!=null && this.getEntityName()!=null && this.getEntityName().trim()!="" )
			result +=this.getEntityName();
		if(this.getDescription()!=null && this.getDescription()!=null && this.getDescription().trim()!="" )
			result +=this.getDescription();

			return  result;
	}
	public String display(String separator) {
		// TODO Auto-generated method stub
		return  this.getEntityName()+separator+ this.getDescription();
	}
	
	
}

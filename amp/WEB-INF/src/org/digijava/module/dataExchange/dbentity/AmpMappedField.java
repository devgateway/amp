/**
 * 
 */
package org.digijava.module.dataExchange.dbentity;

import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * @author dan
 *
 */
public class AmpMappedField {

	/**
	 * 
	 */
	
	DEMappingFields item;
	String description;
	
	public DEMappingFields getItem() {
		return item;
	}
	public void setItem(DEMappingFields item) {
		this.item = item;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public AmpMappedField(DEMappingFields item, String description) {
		super();
		this.item = item;
		this.description = description;
	}
	
	public AmpMappedField() {
		super();
	}
	public AmpMappedField(DEMappingFields item) {
		super();
		this.item = item;
		this.description = "";
	}	
	
	public boolean isOK(){
		if(this.getDescription() == null || "".compareTo(this.getDescription().trim()) == 0)
			return true;
		else return false;
	}
 
	public void add(String log){
		this.description += " "+log;
	}

	public void add(String log, String separator){
		this.description += separator+log;
	}

	
}

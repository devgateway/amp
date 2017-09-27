package org.digijava.module.dataExchange.type;

import java.util.ArrayList;
import java.util.List;

public class AmpColumnEntry {
	
	
	public String key = null;
	
	public String name = null;
	
	public String path = null;
	
	public boolean select = false;
	
	public boolean mandatory = false;
	
	public List<AmpColumnEntry> elements = null;

	public AmpColumnEntry(){
		
	}
	
	public AmpColumnEntry(String name){
		this.name = name;
	}

	public AmpColumnEntry(String key, String name){
		this(name);
		this.key = key;
	}

	public AmpColumnEntry(String key, String name, String path){
		this(key, name);
		this.path = path;
	}
	
	public AmpColumnEntry(String key, String name, boolean select, boolean mandatory){
		this(key, name);
		this.select = select;
		this.mandatory = mandatory;
	}
	
	public AmpColumnEntry(String key, String name, boolean select, List<AmpColumnEntry> elements){
		this(key, name);
		this.select = select;
		this.elements = elements;
	}

	public AmpColumnEntry(String key, String name, boolean select, boolean mandatory, List<AmpColumnEntry> elements){
		this(key, name);
		this.select = select;
		this.elements = elements;
		this.mandatory = mandatory;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSelect() {
		return select;
	}

	public void setSelect(boolean select) {
		this.select = select;
	}

	public List<AmpColumnEntry> getElements() {
		return elements;
	}

	public AmpColumnEntry getFirstElement() {
		AmpColumnEntry retvalue = null;
		if (elements != null && !elements.isEmpty()){
			retvalue = elements.get(0);
		}
		return retvalue;
	}
	
	public AmpColumnEntry getElementByName(String name) {
		AmpColumnEntry retvalue = null;
		if (elements != null && !elements.isEmpty()){
			
			for (AmpColumnEntry elem : elements) {
				if (name.trim().equalsIgnoreCase(elem.getName().trim())){
					retvalue = elem;
					break;
				}
			}
		}
		return retvalue;
	}

	public AmpColumnEntry getElementByKey(String key) {
		AmpColumnEntry retvalue = null;
		if (elements != null && !elements.isEmpty()){
			
			for (AmpColumnEntry elem : elements) {
				if (key.trim().equalsIgnoreCase(elem.getKey().trim())){
					retvalue = elem;
					break;
				}
			}
		}
		return retvalue;
	}

	public AmpColumnEntry getElementByPath(String path) {
		AmpColumnEntry retvalue = null;
		if (elements != null && !elements.isEmpty()){
			
			for (AmpColumnEntry elem : elements) {
				if (path.trim().equalsIgnoreCase(elem.getPath().trim())){
					retvalue = elem;
					break;
				}
			}
		}
		return retvalue;
	}
	
	
	public void setElements(List<AmpColumnEntry> elements) {
		this.elements = elements;
	}
	
	public List<AmpColumnEntry> getList() {
		if (elements == null){
			elements = new ArrayList<AmpColumnEntry>();
		}
		return elements;
	}

	public boolean isMandatory() {
		return mandatory;
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

	public String getPath(){
		return this.path;
	}
	
	public boolean canExport(){
		return this.isMandatory() || this.isSelect();
	}
	
	@Override 
	public String toString() {
		return this.path;
	}
}

package org.digijava.module.aim.fmtool.types;

import java.util.ArrayList;
import java.util.List;

import org.digijava.module.aim.fmtool.util.FMToolConstants;

public class FMCheckTreeEntry {

	
	public String key = null;
	
	public String name = null;
	
	public String path = null;
	
	public boolean correct = false;
	
	private String type = null;
	
	public List<FMCheckTreeEntry> elements = null;

	private FMCheckTreeEntry parent = null;
	
	public FMCheckTreeEntry(){
		
	}
	
	public FMCheckTreeEntry(String name){
		this.name = name;
	}

	public FMCheckTreeEntry(String key, String name){
		this(name);
		this.key = key;
	}

	public FMCheckTreeEntry(String key, String name, String path){
		this(key, name);
		this.path = path;
	}
	
	public FMCheckTreeEntry(String key, String name, boolean correct){
		this(key, name);
		this.correct = correct;
	}
	
	public FMCheckTreeEntry(String key, String name, boolean correct, List<FMCheckTreeEntry> elements){
		this(key, name);
		this.correct = correct;
		this.elements = elements;
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

	public boolean isCorrect() {
		return correct;
	}

	public void setCorrect(boolean correct) {
		this.correct = correct;
	}

	public List<FMCheckTreeEntry> getElements() {
		return elements;
	}

	public FMCheckTreeEntry getFirstElement() {
		FMCheckTreeEntry retvalue = null;
		if (elements != null && !elements.isEmpty()){
			retvalue = elements.get(0);
		}
		return retvalue;
	}
	
	public FMCheckTreeEntry getElementByName(String name) {
		FMCheckTreeEntry retvalue = null;
		if (elements != null && !elements.isEmpty()){
			
			for (FMCheckTreeEntry elem : elements) {
				if (name.trim().equalsIgnoreCase(elem.getName().trim())){
					retvalue = elem;
					break;
				}
			}
		}
		return retvalue;
	}

	public FMCheckTreeEntry getElementByKey(String key) {
		FMCheckTreeEntry retvalue = null;
		if (elements != null && !elements.isEmpty()){
			
			for (FMCheckTreeEntry elem : elements) {
				if (key.trim().equalsIgnoreCase(elem.getKey().trim())){
					retvalue = elem;
					break;
				}
			}
		}
		return retvalue;
	}

	public FMCheckTreeEntry getElementByPath(String path) {
		FMCheckTreeEntry retvalue = null;
		if (elements != null && !elements.isEmpty()){
			
			for (FMCheckTreeEntry elem : elements) {
				if (path.trim().equalsIgnoreCase(elem.getPath().trim())){
					retvalue = elem;
					break;
				}
			}
		}
		return retvalue;
	}
	
	
//	public void setElements(List<FMCheckTreeEntry> elements) {
//		this.elements = elements;
//	}

	public void addElements(List<FMCheckTreeEntry> elements) {
		if (this.elements == null){
			this.elements = new ArrayList<FMCheckTreeEntry>();
		}
		for (FMCheckTreeEntry item : elements) {
			item.setParent(this);
			this.elements.add(item);
			setStatus(item.isCorrect());
		}
	}

	public void setStatus(boolean isOk){
		if (isOk)
			return;
		this.setCorrect(isOk);
		if (parent != null)
			parent.setStatus(isOk);
	}
	
	public List<FMCheckTreeEntry> getList() {
		if (elements == null){
			elements = new ArrayList<FMCheckTreeEntry>();
		}
		return elements;
	}

	public String getPath(){
		return this.path;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public FMCheckTreeEntry getParent() {
		return parent;
	}

	public void setParent(FMCheckTreeEntry parent) {
		this.parent = parent;
	}

	public boolean isCircularity(String key){
		boolean retValue = false;
		
		if (parent != null){
			if (parent.getKey().equalsIgnoreCase(key)){
				retValue = true;
			} else {
				retValue = parent.isCircularity(key);
			}
		}
		return retValue;
	}

}

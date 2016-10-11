/**
 * 
 */
package org.digijava.module.dataExchange.dbentity;

import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.axis.utils.StringUtils;
import org.digijava.kernel.translator.TranslatorWorker;

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
	private boolean doNotImport = false;
	private boolean mainEntry = false;
	private String warningMsg = "";
	
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
		if((this.getDescription() == null || "".compareTo(this.getDescription().trim()) == 0) && StringUtils.isEmpty(warningMsg))
			return true;
		return false;
	}

	public boolean isMapped(){
		if(this.getItem().getAmpId()!=null)
			return true;
		return false;
	}

	
	public void add(String log){
		this.description += " "+log;
	}

	public void add(String log, String separator){
		this.description += separator+log;
	}

	public String getErrors(){
		if(this.getItem() == null ) return "";// return this.getDescription();
		return TranslatorWorker.translateText(this.getItem().getIatiPath())+": "+this.getItem().getIatiValues();
	}

	public String getWarnings(){
		if(this.getItem() == null ) return this.getDescription() + (!StringUtils.isEmpty(this.description) ? ". ": "") + this.warningMsg;
		return StringUtils.isEmpty(this.warningMsg) ? "" : (this.mainEntry ? "" : this.getItem().getIatiPath()+": ") + this.warningMsg;
	}
	
	public String getIatiPath(){
		return this.getItem().getIatiPath();
	}

	public String getIatiValues(){
		return this.getItem().getIatiValues();
	}
	/**
	 * @return true if the current field (or entire Activity) must be not be imported    
	 */
	public boolean isDoNotImport() {
		return doNotImport;
	}
	/**
	 * @param doNotImport flag current field (or entire Activity) to not import
	 */
	public void setDoNotImport(boolean doNotImport) {
		this.doNotImport = doNotImport;
	}
	/**
	 * @return true if this is the main identification entry of the fields set 
	 */
	public boolean isMainEntry() {
		return mainEntry;
	}
	/**
	 * @param mainEntry of the fields set (e.g. Activity)
	 */
	public void setMainEntry(boolean mainEntry) {
		this.mainEntry = mainEntry;
	}

	/**
	 * @param warningMsg is an explicit warning message that will be always displayed, either there are errors or not
	 */
	public void setWarningMsg(String warningMsg) {
		this.warningMsg = warningMsg;
	}

	
}

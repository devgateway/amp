/**
 * 
 */
package org.digijava.module.dataExchange.dbentity;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.digijava.module.dataExchange.jaxb.Activities;

/**
 * @author dan
 *
 */
public class AmpDEImportLog {

	private Set<String> warnings = null;
	private Set<String> errors = null;
	private String objectNameLogged = null;
	private String objectTypeLogged = null;
	private OutputStream outputStream;
	private List<AmpDEImportLog> elements = null;
	private boolean select = false;
	private String key = null;
	private Integer counter = null;
	
	private Activities activities = null;
	


	public Activities getActivities() {
		return activities;
	}

	public void setActivities(Activities activities) {
		this.activities = activities;
	}

	public String getObjectNameLogged() {
		return objectNameLogged;
	}

	public void setObjectNameLogged(String objectNameLogged) {
		this.objectNameLogged = objectNameLogged;
	}

	public String getObjectTypeLogged() {
		return objectTypeLogged;
	}

	public void setObjectTypeLogged(String objectTypeLogged) {
		this.objectTypeLogged = objectTypeLogged;
	}

	public AmpDEImportLog(){
		this.warnings = new HashSet();
		this.errors = new HashSet();
	}
	
	public AmpDEImportLog(Set<String> warnings, Set<String> errors) {
		super();
		this.warnings = warnings;
		this.errors = errors;
	}
	
	public Set<String> getWarnings() {
		return warnings;
	}
	public void setWarnings(Set<String> warnings) {
		this.warnings = warnings;
	}
	public Set<String> getErrors() {
		return errors;
	}
	public void setErrors(Set<String> errors) {
		this.errors = errors;
	}
	
	public void addError(String error){
		
		if(!isError()) this.errors = new HashSet<String>();
		
		if(error!=null && !"".equals(error.trim())){
			errors.add(error);
		}
	}

	public void addWarning(String warning){
		
		if(!isWarning()) this.warnings = new HashSet<String>();
		
		if(warning!=null && !"".equals(warning.trim())){
			warnings.add(warning);
		}
	}
	
	public boolean isError(){
		if(errors !=null && errors.size() >0) return true;
		return false;
	}
	
	public boolean isWarning(){
		if(warnings !=null && warnings.size() >0) return true;
		return false;
	}
	
	public String printWarnings(String delimitator){
		String result = "";
		if(isWarning())
			for (Iterator it = warnings.iterator(); it.hasNext();) {
				result += (String) it.next()+delimitator;
				
			}
		//if("".equals(result)) return null;
		return result;
	}
	
	public String printErrors(String delimitator){
		String result = "";
		if(isWarning())
			for (Iterator it = errors.iterator(); it.hasNext();) {
				result += (String) it.next()+delimitator;
				
			}
		//if("".equals(result)) return null;
		return result;
	}

	public OutputStream getOutputStream() {
		return outputStream;
	}

	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}
	
	public InputStream getInputStream(){
		return new ByteArrayInputStream(this.getOutputStream().toString().getBytes());
	}
	
	public String printStringWithColor(String s, String color){
		return "<div style=\"color:"+color+"; \">"+s+"</div>";
	}
	
	public String printStringWithColorAndBold(String s, String color){
		//return "<b><div style=\"color:"+color+",font-weight:bold\">"+s+"</div></b>";
		return "<div style=\"color: "+color+"; font-weight: bolder; font-size: 130%\">"+s+"</div>";
	}
	
	public String printStringBold(String s){
		return "<div style=\"font-weight: bolder; font-size: 115%\">"+s+"</div>";
	}
	
	public String printLog(String delimitator){

		return delimitator+this.printStringWithColorAndBold(this.getObjectNameLogged(),"red")+delimitator+
		printStringBold("Warnings")+printWarnings(delimitator)+delimitator+printStringBold("Errors")+printErrors(delimitator)+"<hr/>"+delimitator;
	}

	public List<AmpDEImportLog> getElements() {
		return elements;
	}

	public void setElements(List<AmpDEImportLog> elements) {
		this.elements = elements;
	}

	public boolean isSelect() {
		return select;
	}

	public void setSelect(boolean select) {
		this.select = select;
	}

	public String getKey() {
		if(counter.equals(0)) return "activityTree.select";
		Integer i = counter -1 ;
		return "activityTree.elements["+i.toString()+"].select"; 
		//this.getObjectNameLogged().replaceAll(" ", "");//this.getObjectNameLogged().trim();//;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Integer getCounter() {
		return counter;
	}

	public void setCounter(Integer counter) {
		this.counter = counter;
	}
	
}

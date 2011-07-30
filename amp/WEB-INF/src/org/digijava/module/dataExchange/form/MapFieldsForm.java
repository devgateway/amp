/**
 * 
 */
package org.digijava.module.dataExchange.form;

import java.util.Collection;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.dataExchange.dbentity.DEMappingFieldsDisplay;

/**
 * @author Dan Mihaila
 *
 */
public class MapFieldsForm extends ActionForm {
	private String action					= null;
	Collection<DEMappingFieldsDisplay> mappedFields =  null;
	
	public Collection<DEMappingFieldsDisplay> getMappedFields() {
		return mappedFields;
	}

	public void setMappedFields(Collection<DEMappingFieldsDisplay> mappedFields) {
		this.mappedFields = mappedFields;
	}

	Set<String> ampClasses = null;
	private String [] selectedFields;
	
	private String selectedAmpClass;
	

	public String[] getSelectedFields() {
		return selectedFields;
	}

	public void setSelectedFields(String[] selectedFields) {
		this.selectedFields = selectedFields;
	}

	public String getSelectedAmpClass() {
		return selectedAmpClass;
	}

	public void setSelectedAmpClass(String selectedAmpClass) {
		this.selectedAmpClass = selectedAmpClass;
	}

	public Set<String> getAmpClasses() {
		return ampClasses;
	}

	public void setAmpClasses(Set<String> ampClasses) {
		this.ampClasses = ampClasses;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		super.reset(mapping, request);
		selectedFields=null;
	}
	
}

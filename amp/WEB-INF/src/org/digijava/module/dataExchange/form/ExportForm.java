package org.digijava.module.dataExchange.form;



import org.apache.struts.action.ActionForm;
import org.digijava.module.dataExchange.type.AmpColumnEntry;

public class ExportForm extends ActionForm{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	private AmpColumnEntry activityTree = null;
	

	public ExportForm(){
	
	}


	public AmpColumnEntry getActivityTree() {
		return activityTree;
	}


	public void setActivityTree(AmpColumnEntry activityTree) {
		this.activityTree = activityTree;
	}

	
	
}


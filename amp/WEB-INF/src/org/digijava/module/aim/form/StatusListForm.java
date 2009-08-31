package org.digijava.module.aim.form ;

import java.util.Collection;

import org.apache.struts.action.ActionForm;


public class StatusListForm extends ActionForm
{
	private Collection statusCollection;
	
	private String name ;
	

		
	public Collection getStatusCollection() 
	{
		return statusCollection;
	}

	public void setStatusCollection(Collection collection) 
	{
		statusCollection = collection;
	}
	
	/**
		 * @return
		 */
		
}

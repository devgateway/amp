package org.digijava.module.aim.form ;

import org.apache.struts.action.ActionForm ;
import java.util.Collection;


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

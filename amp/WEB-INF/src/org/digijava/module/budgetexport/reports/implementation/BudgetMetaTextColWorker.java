/**
 * 
 */
package org.digijava.module.budgetexport.reports.implementation;

import javax.servlet.http.HttpSession;

import org.dgfoundation.amp.ar.ReportGenerator;
import org.dgfoundation.amp.ar.workers.MetaTextColWorker;
import org.digijava.module.budgetexport.util.MappingEncoder;

/**
 * @author Alex Gartner
 *
 */
public class BudgetMetaTextColWorker extends MetaTextColWorker {
	
	
	ColWorkerInsider insider;

	/**
	 * @param condition
	 * @param viewName
	 * @param columnName
	 * @param generator
	 */
	public BudgetMetaTextColWorker(String condition, String viewName,
			String columnName, ReportGenerator generator) {
		super(condition, viewName, columnName, generator);
		
		this.insider	= new ColWorkerInsider(condition, viewName, columnName, generator);
	}

	@Override
	protected String retrieveValueFromRSObject( Object rsObj ) {
		return this.insider.encoder.encode( rsObj.toString() );
	}
	
	@Override
	public void setSession(HttpSession session) {
		this.insider.setSession( session );
	}
	
	@Override
	public MappingEncoder getEncoder () {
		if ( insider != null )
			return insider.getEncoder();
		return null;
	}
	
}

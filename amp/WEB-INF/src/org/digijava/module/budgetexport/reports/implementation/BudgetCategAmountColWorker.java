/**
 * 
 */
package org.digijava.module.budgetexport.reports.implementation;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpSession;

import org.dgfoundation.amp.ar.ReportGenerator;
import org.dgfoundation.amp.ar.workers.CategAmountColWorker;

/**
 * @author Alex Gartner
 *
 */
public class BudgetCategAmountColWorker extends CategAmountColWorker {

	ColWorkerInsider insider;
	
	/**
	 * @param condition
	 * @param viewName
	 * @param columnName
	 * @param generator
	 */
	public BudgetCategAmountColWorker(String condition, String viewName,
			String columnName, ReportGenerator generator) {
		super(condition, viewName, columnName, generator);
		
		this.insider	= new ColWorkerInsider(condition, viewName, columnName, generator);
	}

	@Override
	protected String retrieveValueFromRS ( ResultSet rs, String columnName ) throws SQLException {
		return this.insider.encoder.encode( rs.getString(columnName) );
	}
	
	@Override
	public void setSession(HttpSession session) {
		this.insider.setSession( session );
	}
}

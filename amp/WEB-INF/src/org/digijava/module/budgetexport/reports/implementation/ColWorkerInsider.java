package org.digijava.module.budgetexport.reports.implementation;

import javax.servlet.http.HttpSession;

import org.dgfoundation.amp.ar.ReportGenerator;
import org.digijava.module.budgetexport.form.Encoder;
import org.digijava.module.budgetexport.util.BudgetExportConstants;
import org.digijava.module.budgetexport.util.MappingEncoder;

public class ColWorkerInsider {
	MappingEncoder encoder;
	HttpSession session;
	String viewName;
	String columnName;
	ReportGenerator generator;
	/**
	 * @param condition
	 * @param viewName
	 * @param columnName
	 * @param generator
	 */
	public ColWorkerInsider(String condition, String viewName,
			String columnName, ReportGenerator generator) {
		this.generator	= generator;
		this.columnName	= columnName;
		this.viewName	= viewName;
	}
	

	protected String retrieveValueFromRSObject( Object rsObj ) {
		return this.encoder.encode( rsObj.toString() );
	}
	
	public void setSession(HttpSession session) {
		this.session	= session;
		this.prepareEncoder();
	}
	
	public void prepareEncoder() {
		String exportType		= (String) session.getAttribute(BudgetExportConstants.BUDGET_EXPORT_TYPE);
		this.encoder			= new Encoder(exportType, this.viewName);
	}

}

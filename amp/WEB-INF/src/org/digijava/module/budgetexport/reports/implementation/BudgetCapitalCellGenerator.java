/**
 * 
 */
package org.digijava.module.budgetexport.reports.implementation;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpSession;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.CapitalCellGenerator;
import org.dgfoundation.amp.ar.MetaInfo;

/**
 * @author alex
 *
 */
public class BudgetCapitalCellGenerator extends CapitalCellGenerator {


	ColWorkerInsider insider;
	HttpSession session;
	/**
	 * @param metaDataName
	 * @param measureName
	 * @param originalMeasureName
	 */
	public BudgetCapitalCellGenerator(String metaDataName,
			String measureName, String originalMeasureName) {
		super(metaDataName, measureName, originalMeasureName);
		
		this.insider	= new ColWorkerInsider(null, "v_capital_and_exp", "Capital - Expenditure", null);
	}
	
	
	@Override
	public void setSession ( HttpSession session ) {
		this.insider.setSession(session);
	}
	
	@Override
	public Collection<MetaInfo> syntheticMetaInfo() {
		ArrayList<MetaInfo> ret	= new ArrayList<MetaInfo>();
		String value			= this.insider.encoder.encode( "Expenditure" );
		MetaInfo mi				= new MetaInfo(ArConstants.COLUMN_CAPITAL_EXPENDITRURE, value );
		ret.add(mi);
		
		return ret;
	}

}

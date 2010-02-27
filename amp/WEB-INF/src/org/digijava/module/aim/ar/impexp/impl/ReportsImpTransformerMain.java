/**
 * 
 */
package org.digijava.module.aim.ar.impexp.impl;

import org.digijava.module.aim.ar.impexp.ImpTransformerMain;
import org.digijava.module.aim.ar.impexp.data.Reports;
import org.digijava.module.aim.ar.impexp.data.ReportsType;
import org.digijava.module.aim.dbentity.AmpReports;

/**
 * @author Alex Gartner
 *
 */
public class ReportsImpTransformerMain extends
		ImpTransformerMain<AmpReports, Reports, ReportsType, AmpReportsImpTransformerFactory> {

	
	public ReportsImpTransformerMain() {
		this.rootJaxbClass		= Reports.class;
		this.tFactory				= new AmpReportsImpTransformerFactory();
	}
	@Override
	protected void processEntityRoots() {
		if ( this.root != null)
			this.entityRoots			= root.getReport(); 
	}

}

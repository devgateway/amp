package org.digijava.module.aim.ar.impexp.impl;

import org.digijava.module.aim.ar.impexp.ExpTransformerMain;
import org.digijava.module.aim.ar.impexp.data.Reports;
import org.digijava.module.aim.ar.impexp.data.ReportsType;
import org.digijava.module.aim.dbentity.AmpReports;

public class ReportsExpTransformerMain extends
		ExpTransformerMain<Reports, ReportsType, AmpReports, AmpReportsExpTransformerFactory, Long> {

	public ReportsExpTransformerMain() {
		this.tfactory		= new AmpReportsExpTransformerFactory();
		this.root			= new Reports();
		this.entityClass	= AmpReports.class;
	}
	
	@Override
	protected void process(ReportsType child) {
				this.root.getReport().add( child );
	}


}

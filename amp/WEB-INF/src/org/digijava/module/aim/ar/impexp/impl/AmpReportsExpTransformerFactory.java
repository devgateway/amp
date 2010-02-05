package org.digijava.module.aim.ar.impexp.impl;

import org.digijava.module.aim.ar.impexp.ExpTransformer;
import org.digijava.module.aim.ar.impexp.ExpTransformerFactory;
import org.digijava.module.aim.ar.impexp.data.ReportsType;
import org.digijava.module.aim.dbentity.AmpReports;

public class AmpReportsExpTransformerFactory implements
		ExpTransformerFactory<AmpReports, ReportsType> {

	@Override
	public ExpTransformer<AmpReports, ReportsType> generateExpTransformer() {
		return new AmpReportsExpTransformer();
	}

}

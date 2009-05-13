package org.digijava.module.aim.ar.impexp.impl;

import org.digijava.module.aim.ar.impexp.ImpTransformer;
import org.digijava.module.aim.ar.impexp.ImpTransformerFactory;
import org.digijava.module.aim.ar.impexp.data.ReportsType;
import org.digijava.module.aim.dbentity.AmpReports;

public class AmpReportsImpTransformerFactory implements
		ImpTransformerFactory<AmpReports, ReportsType> {

	@Override
	public ImpTransformer<AmpReports> generateImpTransformer(ReportsType r, String propertyName) {
		
		return new AmpReportsImpTransformer(r, propertyName);
	}

}

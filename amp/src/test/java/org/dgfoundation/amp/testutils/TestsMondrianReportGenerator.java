package org.dgfoundation.amp.testutils;

import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.newreports.CompleteWorkspaceFilter;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportEnvironment;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.reports.mondrian.MondrianReportGenerator;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXConfig;

public class TestsMondrianReportGenerator extends MondrianReportGenerator {
		
	public TestsMondrianReportGenerator(String locale) {
		super(ReportAreaImpl.class, new ReportEnvironment(locale, new CompleteWorkspaceFilter(null, null), "EUR"));
	}
	
	@Override protected MDXConfig toMDXConfig(ReportSpecification spec) throws AMPException {
		MDXConfig res = super.toMDXConfig(spec);
		mdxConfigCallback(res, spec);
		return res;
	}
	
	protected void mdxConfigCallback(MDXConfig config, ReportSpecification spec) {};
	
	@Override public GeneratedReport executeReport(ReportSpecification spec) {
		try {return super.executeReport(spec);}
		catch(Exception e) {throw new RuntimeException(e);}
	}
}

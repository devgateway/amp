package org.dgfoundation.amp.ar.amp212;

import java.util.List;

import org.dgfoundation.amp.mondrian.MondrianReportsTestCase;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.nireports.output.NiReportOutputBuilder;

/**
 * 
 * testcases for the fetching states of AMP + the AMP schema
 * 
 * @author Constantin Dolghier
 *
 */
public class AmpSchemaSanityTests extends BasicSanityChecks {

	public AmpSchemaSanityTests() {
		super("AmpReportsSchema sanity tests");
	}
	
	@Override
	protected <K> K buildNiReportDigest(ReportSpecification spec, List<String> activityNames,
			NiReportOutputBuilder<K> outputBuilder) {
		return MondrianReportsTestCase.buildNiReportDigest(spec, activityNames, outputBuilder);
	}
}

package org.dgfoundation.amp.ar.amp212;

import java.util.Arrays;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.mondrian.MondrianReportsTestCase;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportExecutor;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.nireports.NiReportsGenerator;
import org.dgfoundation.amp.nireports.amp.AmpCurrencyConvertor;
import org.dgfoundation.amp.nireports.amp.AmpReportsSchema;
import org.junit.Test;


/**
 * 
 * testcases for the fetching states of AMP + the AMP schema
 * @author Constantin Dolghier
 *
 */
public class NiReportsFetchingTests extends MondrianReportsTestCase {
		
	public NiReportsFetchingTests() {
		super("inclusive runner tests");
	}
		
	@Test
	public void testProjectTitle() throws AMPException {
		ReportExecutor executor = new NiReportsGenerator(AmpReportsSchema.getInstance(), AmpCurrencyConvertor.getInstance());
		ReportSpecification spec = buildSpecification("simple report", Arrays.asList(ColumnConstants.PROJECT_TITLE), Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS), null, GroupingCriteria.GROUPING_YEARLY);
		executor.executeReport(spec);
		executor.executeReport(spec);
		executor.executeReport(spec);
	}
}

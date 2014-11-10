package org.dgfoundation.amp.ar.amp210;

import java.util.Arrays;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.mondrian.MondrianReportsTestCase;
import org.dgfoundation.amp.mondrian.ReportAreaForTests;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportEntityType;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.reports.mondrian.MondrianReportUtils;
import org.junit.Test;

public class DateColumnsMondrianReportTests extends MondrianReportsTestCase {
	
	public DateColumnsMondrianReportTests() {
		super("basic mondrian tests");
	}
	
	@Test
	public void testActualStartDateDoesNotCrash() {
		ReportAreaForTests correctReport = null;
		
		runMondrianTestCase(
				buildSpecification("with-dates", 
						Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.ACTUAL_START_DATE, ColumnConstants.PROPOSED_START_DATE), 
						Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
						null, GroupingCriteria.GROUPING_YEARLY),
				"en", 
				Arrays.asList("Project with documents"),
				correctReport); 

		//System.out.println(describeReportOutputInCode(rep));
		//System.out.println(rep.toString());
	}
}

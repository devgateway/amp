package org.dgfoundation.amp.test.reports;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnReportData;
import org.dgfoundation.amp.exprlogic.MathExpression;
import org.digijava.module.aim.helper.FormatHelper;

public class TesComputedDatesColumns extends TestReportBase {

	@Override
	public Long[] getHierarchies() throws Exception {
		// TODO Auto-generated method stub
		return new Long[] {};
	}

	@Override
	public Long[] getColumns() throws Exception {
		return new Long[] { getColumnByName("Overage Project").getColumnId(), getColumnByName("Age of Project").getColumnId(), getColumnByName("Project Period").getColumnId(),
				getColumnByName("Overage").getColumnId(), getColumnByName("Project Age Ratio").getColumnId() };
	}

	/**
	 * 
	 * Project Period Project Period (months) = Proposed Completion Date -
	 * Actual Start date Overage Overage (months) = Age of project - Project Age
	 * Ratio Project Age Ratio = Age of project / Project Period
	 */

	public void testGrandTotalValues() throws Exception {
	}

	// CURRENT_DATE_VALUE - ACTUAL_START_DATE_VALUE
	public void testAgeOfProject() throws Exception {
		Date actualStartDate = new SimpleDateFormat("dd/MM/yyyy").parse("01/02/2009");
		Date currentDate = new Date();
		BigDecimal result = MathExpression.getMonthDifference(new BigDecimal(currentDate.getTime()), new BigDecimal(actualStartDate.getTime()));
		ColumnReportData data = ((org.dgfoundation.amp.ar.ColumnReportData) generatedReport.getItem(0));
		assertTrue(checkStringValues(data.getColumn("Age of Project").getItems(), result.toString()));
	}

	// ArConstants.PROPOSED_COMPLETION_DATE_VALUE-
	// ArConstants.ACTUAL_START_DATE_VALUE
	public void testProjectPeriod() throws Exception {
		ColumnReportData data = ((org.dgfoundation.amp.ar.ColumnReportData) generatedReport.getItem(0));
		assertTrue(checkStringValues(data.getColumn("Project Period").getItems(), "6"));
	}

	// ArConstants.CURRENT_DATE_VALUE -
	// ArConstants.PROPOSED_COMPLETION_DATE_VALUE
	public void testOverageProject() throws Exception {

		Date proposedCompletionDate = new SimpleDateFormat("dd/MM/yyyy").parse("01/08/2009");
		Date currentDate = new Date();
		Long result = MathExpression.getMonthDifference(new BigDecimal(currentDate.getTime()), new BigDecimal(proposedCompletionDate.getTime())).longValue();

		ColumnReportData data = ((org.dgfoundation.amp.ar.ColumnReportData) generatedReport.getItem(0));

		assertTrue(checkStringValues(data.getColumn("Overage Project").getItems(), result.toString()));
	}

	// Overage (months) = Age of project - Project period
	public void testOverage() throws Exception {
		Date actualStartDate = new SimpleDateFormat("dd/MM/yyyy").parse("01/02/2009");
		Date currentDate = new Date();
		Double ageOfProject = MathExpression.getMonthDifference(new BigDecimal(currentDate.getTime()), new BigDecimal(actualStartDate.getTime())).doubleValue();
		Double projectPeriod = 6d;
		Double result = ageOfProject - projectPeriod;
		ColumnReportData data = ((org.dgfoundation.amp.ar.ColumnReportData) generatedReport.getItem(0));
		assertTrue(checkStringValues(data.getColumn("Overage").getItems(),FormatHelper.formatNumber(result) ));
	}

	public void testProjectAgeRatio() throws Exception {
		Date actualStartDate = new SimpleDateFormat("dd/MM/yyyy").parse("01/02/2009");
		Date currentDate = new Date();
		Double ageOfProject = MathExpression.getMonthDifference(new BigDecimal(currentDate.getTime()), new BigDecimal(actualStartDate.getTime())).doubleValue();
		Double projectPeriod = 6d;
		Double result = new Double(ageOfProject / projectPeriod);
		ColumnReportData data = ((org.dgfoundation.amp.ar.ColumnReportData) generatedReport.getItem(0));
		assertTrue(checkStringValues(data.getColumn("Project Age Ratio").getItems(), FormatHelper.formatNumber(result)));

	}

}

package org.dgfoundation.amp.test.reports;

import org.dgfoundation.amp.ar.ColumnReportData;

public class TestComputedColumnReport extends TestReportBase {

	@Override
	public Long[] getHierarchies() throws Exception {
		return new Long[] {};
	}

	public Long[] getMeasures() throws Exception {

		return new Long[] { getMeasureByName("Actual Commitments").getMeasureId(), getMeasureByName("Actual Disbursements").getMeasureId() };
	}

	@Override
	public Long[] getColumns() throws Exception {
		return new Long[] { getColumnByName("Project Title").getColumnId(), getColumnByName("Cumulative Disbursement").getColumnId(), getColumnByName("Cumulative Commitment").getColumnId(),
				getColumnByName("Activity Count").getColumnId(), getColumnByName("Average Size of Disbursements").getColumnId(), getColumnByName("Average Size of Projects").getColumnId(),
				getColumnByName("Cumulative Execution Rate").getColumnId(), getColumnByName("Average Disbursement Rate").getColumnId(), getColumnByName("Predictability of Funding").getColumnId(),
				getColumnByName("Variance Of Commitments").getColumnId(), getColumnByName("Variance Of Disbursements").getColumnId() };
	}

	@Override
	public void testGrandTotalValues() throws Exception {
		assertTrue(checkValues(generatedReport.getTrailCells(), null, 16000d, 12000d, 2d, 4000d, 6000d, 133.33d, 66.67d, 33.33d, 8000d, 8000d, 6000d, 8000d, 6000d, 8000d, 12000d, 16000d));
	}

	public void testCumulativeDisbursement() throws Exception {
		System.out.println(generatedReport);
		ColumnReportData data = ((org.dgfoundation.amp.ar.ColumnReportData) generatedReport.getItem(0));
		assertTrue(checkValues(data.getColumn("Cumulative Disbursement").getItems(), 4000d, 12000d));
	}

	public void testCumulativeCommitment() throws Exception {
		ColumnReportData data = ((org.dgfoundation.amp.ar.ColumnReportData) generatedReport.getItem(0));
		assertTrue(checkValues(data.getColumn("Cumulative Commitment").getItems(),2000d,10000d));

	}

	public void testAverageSizeOfDisbursements() throws Exception {
		ColumnReportData data = ((org.dgfoundation.amp.ar.ColumnReportData) generatedReport.getItem(0));
		assertTrue(checkValues(data.getColumn("Average Size of Disbursements").getItems(), 2000d, 6000d));
	}

	public void testPredictabilityOfFunding() throws Exception {
		ColumnReportData data = ((org.dgfoundation.amp.ar.ColumnReportData) generatedReport.getItem(0));
		assertTrue(checkValues(data.getColumn("Predictability of Funding").getItems(), 50d, 25d));

	}

}

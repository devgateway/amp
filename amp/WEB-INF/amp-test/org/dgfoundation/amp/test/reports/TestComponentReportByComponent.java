package org.dgfoundation.amp.test.reports;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnReportData;
import org.dgfoundation.amp.ar.GroupColumn;
import org.dgfoundation.amp.ar.GroupReportData;
import org.dgfoundation.amp.ar.TotalAmountColumn;

public class TestComponentReportByComponent extends TestReportBase {

	protected Integer getReportType() {
		return ArConstants.COMPONENT_TYPE;
	}

	public Long[] getHierarchies() throws Exception {
		// TODO Auto-generated method stub
		return new Long[] { getColumnByName("Component Name").getColumnId() };
	}

	public void testGrandTotalValues() throws Exception {
		assertTrue(checkValues(generatedReport.getTrailCells(), null, 12000d, 16000d, 20000d, 24000d, 12000d, 16000d, 20000d, 24000d, 24000d, 32000d, 40000d, 48000d));
	}

	public void testInfrastructureSupportValues() throws Exception {
		GroupReportData report = (GroupReportData) generatedReport.getItem(0);
		ColumnReportData colData = (ColumnReportData) report.getItem(1);
		System.err.println(colData);
		GroupColumn grColumn = (GroupColumn) colData.getColumn("Funding");

		GroupColumn gC2009 = (GroupColumn) grColumn.getColumn("2009");

		TotalAmountColumn aC2009 = (TotalAmountColumn) gC2009.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2009.getItems(), 1000d, 5000d));

		TotalAmountColumn aD2009 = (TotalAmountColumn) gC2009.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2009.getItems(), 2000d, 6000d));

		TotalAmountColumn pC2009 = (TotalAmountColumn) gC2009.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2009.getItems(), 3000d, 7000d));

		TotalAmountColumn pD2009 = (TotalAmountColumn) gC2009.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2009.getItems(), 4000d, 8000d));

		GroupColumn gC2010 = (GroupColumn) grColumn.getColumn("2010");

		TotalAmountColumn aC2010 = (TotalAmountColumn) gC2010.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2010.getItems(), 1000d, 5000d));

		TotalAmountColumn aD2010 = (TotalAmountColumn) gC2010.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2010.getItems(), 2000d, 6000d));

		TotalAmountColumn pC2010 = (TotalAmountColumn) gC2010.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2010.getItems(), 3000d, 7000d));

		TotalAmountColumn pD2010 = (TotalAmountColumn) gC2010.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2010.getItems(), 4000d, 8000d));
		/**
		 * 120 240 360 480 1500 1800 2100 2400
		 */

		GroupColumn totalColumns = (GroupColumn) colData.getColumn("Total Costs");
		TotalAmountColumn tAC = (TotalAmountColumn) totalColumns.getColumn("Actual Commitments");
		assertTrue(checkValues(tAC.getItems(), 2000d, 10000d));

		TotalAmountColumn tAD = (TotalAmountColumn) totalColumns.getColumn("Actual Disbursements");
		assertTrue(checkValues(tAD.getItems(), 4000d, 12000d));

		TotalAmountColumn tPC = (TotalAmountColumn) totalColumns.getColumn("Planned Commitments");
		assertTrue(checkValues(tPC.getItems(), 6000d, 14000d));

		TotalAmountColumn tPD = (TotalAmountColumn) totalColumns.getColumn("Planned Disbursements");
		assertTrue(checkValues(tPD.getItems(), 8000d, 16000d));

	}

	public void testTechnicalAssistanceValues() throws Exception {
		GroupReportData report = (GroupReportData) generatedReport.getItem(0);
		ColumnReportData colData = (ColumnReportData) report.getItem(0);
		System.err.println(colData);
		GroupColumn grColumn = (GroupColumn) colData.getColumn("Funding");
		GroupColumn gC2009 = (GroupColumn) grColumn.getColumn("2009");

		TotalAmountColumn aC2009 = (TotalAmountColumn) gC2009.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2009.getItems(), 1000d, 5000d));

		TotalAmountColumn aD2009 = (TotalAmountColumn) gC2009.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2009.getItems(), 2000d, 6000d));

		TotalAmountColumn pC2009 = (TotalAmountColumn) gC2009.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2009.getItems(), 3000d, 7000d));

		TotalAmountColumn pD2009 = (TotalAmountColumn) gC2009.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2009.getItems(), 4000d, 8000d));

		GroupColumn gC2010 = (GroupColumn) grColumn.getColumn("2010");

		TotalAmountColumn aC2010 = (TotalAmountColumn) gC2010.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2010.getItems(), 1000d, 5000d));

		TotalAmountColumn aD2010 = (TotalAmountColumn) gC2010.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2010.getItems(), 2000d, 6000d));

		TotalAmountColumn pC2010 = (TotalAmountColumn) gC2010.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2010.getItems(), 3000d, 7000d));

		TotalAmountColumn pD2010 = (TotalAmountColumn) gC2010.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2010.getItems(), 4000d, 8000d));
		/**
		 * 120 240 360 480 1500 1800 2100 2400
		 */

		GroupColumn totalColumns = (GroupColumn) colData.getColumn("Total Costs");
		TotalAmountColumn tAC = (TotalAmountColumn) totalColumns.getColumn("Actual Commitments");
		assertTrue(checkValues(tAC.getItems(), 2000d, 10000d));

		TotalAmountColumn tAD = (TotalAmountColumn) totalColumns.getColumn("Actual Disbursements");
		assertTrue(checkValues(tAD.getItems(), 4000d, 12000d));

		TotalAmountColumn tPC = (TotalAmountColumn) totalColumns.getColumn("Planned Commitments");
		assertTrue(checkValues(tPC.getItems(), 6000d, 14000d));

		TotalAmountColumn tPD = (TotalAmountColumn) totalColumns.getColumn("Planned Disbursements");
		assertTrue(checkValues(tPD.getItems(), 8000d, 16000d));
	}

}

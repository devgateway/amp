package org.dgfoundation.amp.test.reports;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnReportData;
import org.dgfoundation.amp.ar.GroupColumn;
import org.dgfoundation.amp.ar.GroupReportData;
import org.dgfoundation.amp.ar.TotalAmountColumn;

public class TestRegionalReportbySector extends TestReportBase {

	public TestRegionalReportbySector(String name) {
		super(name);
	}

	@Override
	protected Integer getReportType() {
		// TODO Auto-generated method stub
		return ArConstants.REGIONAL_TYPE;
	}

	@Override
	public Long[] getHierarchies() throws Exception {
		// TODO Auto-generated method stub
		return new Long[] { getColumnByName("Primary Sector").getColumnId() };
	}

	@Override
	public void testGrandTotalValues() throws Exception {
		assertTrue(checkValues(generatedReport.getTrailCells(), null, 12000d, 16000d, 20000d, 24000d, 12000d, 16000d, 20000d, 24000d, 24000d, 32000d, 40000d, 48000d));
	}

	public void testAgricultureValues() throws Exception {
		GroupReportData report = (GroupReportData) generatedReport.getItem(0);
		ColumnReportData colData = (ColumnReportData) report.getItem(0);

		GroupColumn grColumn = (GroupColumn) colData.getColumn("Funding");

		GroupColumn gC2009 = (GroupColumn) grColumn.getColumn("2009");

		TotalAmountColumn aC2009 = (TotalAmountColumn) gC2009.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2009.getItems(), 400d, 3000d));

		TotalAmountColumn aD2009 = (TotalAmountColumn) gC2009.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2009.getItems(), 800d, 3600d));

		TotalAmountColumn pC2009 = (TotalAmountColumn) gC2009.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2009.getItems(), 1200d, 4200d));

		TotalAmountColumn pD2009 = (TotalAmountColumn) gC2009.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2009.getItems(), 1600d, 4800d));
		GroupColumn gC2010 = (GroupColumn) grColumn.getColumn("2010");

		TotalAmountColumn aC2010 = (TotalAmountColumn) gC2010.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2010.getItems(), 400d, 3000d));

		TotalAmountColumn aD2010 = (TotalAmountColumn) gC2010.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2010.getItems(), 800d, 3600d));

		TotalAmountColumn pC2010 = (TotalAmountColumn) gC2010.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2010.getItems(), 1200d, 4200d));

		TotalAmountColumn pD2010 = (TotalAmountColumn) gC2010.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2010.getItems(), 1600d, 4800d));

		GroupColumn totalColumns = (GroupColumn) colData.getColumn("Total Costs");
		TotalAmountColumn tAC = (TotalAmountColumn) totalColumns.getColumn("Actual Commitments");
		assertTrue(checkValues(tAC.getItems(), 800d, 6000d));

		TotalAmountColumn tAD = (TotalAmountColumn) totalColumns.getColumn("Actual Disbursements");
		assertTrue(checkValues(tAD.getItems(), 1600d, 7200d));

		TotalAmountColumn tPC = (TotalAmountColumn) totalColumns.getColumn("Planned Commitments");
		assertTrue(checkValues(tPC.getItems(), 2400d, 8400d));

		TotalAmountColumn tPD = (TotalAmountColumn) totalColumns.getColumn("Planned Disbursements");
		assertTrue(checkValues(tPD.getItems(), 3200d, 9600d));
	}

	public void testEducationValues() throws Exception {
		GroupReportData report = (GroupReportData) generatedReport.getItem(0);
		ColumnReportData colData = (ColumnReportData) report.getItem(1);

		GroupColumn grColumn = (GroupColumn) colData.getColumn("Funding");

		GroupColumn gC2009 = (GroupColumn) grColumn.getColumn("2009");

		TotalAmountColumn aC2009 = (TotalAmountColumn) gC2009.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2009.getItems(), 1600d, 7000d));

		TotalAmountColumn aD2009 = (TotalAmountColumn) gC2009.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2009.getItems(), 3200d, 8400d));

		TotalAmountColumn pC2009 = (TotalAmountColumn) gC2009.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2009.getItems(), 4800d, 9800d));

		TotalAmountColumn pD2009 = (TotalAmountColumn) gC2009.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2009.getItems(), 6400d, 11200d));
		GroupColumn gC2010 = (GroupColumn) grColumn.getColumn("2010");

		TotalAmountColumn aC2010 = (TotalAmountColumn) gC2010.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2010.getItems(), 1600d, 7000d));

		TotalAmountColumn aD2010 = (TotalAmountColumn) gC2010.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2010.getItems(), 3200d, 8400d));

		TotalAmountColumn pC2010 = (TotalAmountColumn) gC2010.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2010.getItems(), 4800d, 9800d));

		TotalAmountColumn pD2010 = (TotalAmountColumn) gC2010.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2010.getItems(), 6400d, 11200d));

		GroupColumn totalColumns = (GroupColumn) colData.getColumn("Total Costs");
		TotalAmountColumn tAC = (TotalAmountColumn) totalColumns.getColumn("Actual Commitments");
		assertTrue(checkValues(tAC.getItems(), 3200d, 14000d));

		TotalAmountColumn tAD = (TotalAmountColumn) totalColumns.getColumn("Actual Disbursements");
		assertTrue(checkValues(tAD.getItems(), 6400d, 16800d));

		TotalAmountColumn tPC = (TotalAmountColumn) totalColumns.getColumn("Planned Commitments");
		assertTrue(checkValues(tPC.getItems(), 9600d, 19600d));

		TotalAmountColumn tPD = (TotalAmountColumn) totalColumns.getColumn("Planned Disbursements");
		assertTrue(checkValues(tPD.getItems(), 12800d, 22400d));
	}

}

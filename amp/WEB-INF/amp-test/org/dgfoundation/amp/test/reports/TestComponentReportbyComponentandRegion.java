package org.dgfoundation.amp.test.reports;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnReportData;
import org.dgfoundation.amp.ar.GroupColumn;
import org.dgfoundation.amp.ar.GroupReportData;
import org.dgfoundation.amp.ar.TotalAmountColumn;

public class TestComponentReportbyComponentandRegion extends TestReportBase {

	@Override
	public Long[] getHierarchies() throws Exception {
		return new Long[] { getColumnByName("Component Name").getColumnId(), getColumnByName("Region").getColumnId() };
	}

	@Override
	protected Integer getReportType() {
		return ArConstants.COMPONENT_TYPE;
	}

	@Override
	public void testGrandTotalValues() throws Exception {
		assertTrue(checkValues(generatedReport.getTrailCells(), null, 12000d, 16000d, 20000d, 24000d, 12000d, 16000d, 20000d, 24000d, 24000d, 32000d, 40000d, 48000d));
	}

	public void testTechnicalAssistanceDakar() throws Exception {
		GroupReportData report = (GroupReportData) generatedReport.getItem(0);
		GroupReportData firstLevel = (GroupReportData) report.getItem(1);
		ColumnReportData colData = (ColumnReportData) firstLevel.getItem(0);
		System.out.println(firstLevel + " " + colData);
		GroupColumn grColumn = (GroupColumn) colData.getColumn("Funding");

		GroupColumn gC2009 = (GroupColumn) grColumn.getColumn("2009");
		TotalAmountColumn aC2009 = (TotalAmountColumn) gC2009.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2009.getItems(), 700d, 1250d));

		TotalAmountColumn aD2009 = (TotalAmountColumn) gC2009.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2009.getItems(), 1400d, 1500d));

		TotalAmountColumn pC2009 = (TotalAmountColumn) gC2009.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2009.getItems(), 2100d, 1750d));

		TotalAmountColumn pD2009 = (TotalAmountColumn) gC2009.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2009.getItems(), 2800d, 2000d));

		GroupColumn gC2010 = (GroupColumn) grColumn.getColumn("2010");

		TotalAmountColumn aC2010 = (TotalAmountColumn) gC2010.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2010.getItems(), 700d, 1250d));

		TotalAmountColumn aD2010 = (TotalAmountColumn) gC2010.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2010.getItems(), 1400d, 1500d));

		TotalAmountColumn pC2010 = (TotalAmountColumn) gC2010.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2010.getItems(), 2100d, 1750d));

		TotalAmountColumn pD2010 = (TotalAmountColumn) gC2010.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2010.getItems(), 2800d, 2000d));

		GroupColumn totalColumns = (GroupColumn) colData.getColumn("Total Costs");
		TotalAmountColumn tAC = (TotalAmountColumn) totalColumns.getColumn("Actual Commitments");
		assertTrue(checkValues(tAC.getItems(), 1400d, 2500d));

		TotalAmountColumn tAD = (TotalAmountColumn) totalColumns.getColumn("Actual Disbursements");
		assertTrue(checkValues(tAD.getItems(), 2800d, 3000d));

		TotalAmountColumn tPC = (TotalAmountColumn) totalColumns.getColumn("Planned Commitments");
		assertTrue(checkValues(tPC.getItems(), 4200d, 3500d));

		TotalAmountColumn tPD = (TotalAmountColumn) totalColumns.getColumn("Planned Disbursements");
		assertTrue(checkValues(tPD.getItems(), 5600d, 4000d));

	}

	public void testTechnicalAssistanceNairobi() throws Exception {
		GroupReportData report = (GroupReportData) generatedReport.getItem(0);
		GroupReportData firstLevel = (GroupReportData) report.getItem(1);
		ColumnReportData colData = (ColumnReportData) firstLevel.getItem(1);
		System.out.println(firstLevel + " " + colData);
		GroupColumn grColumn = (GroupColumn) colData.getColumn("Funding");

		GroupColumn gC2009 = (GroupColumn) grColumn.getColumn("2009");
		TotalAmountColumn aC2009 = (TotalAmountColumn) gC2009.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2009.getItems(), 300d, 3750d));

		TotalAmountColumn aD2009 = (TotalAmountColumn) gC2009.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2009.getItems(), 600d, 4500d));

		TotalAmountColumn pC2009 = (TotalAmountColumn) gC2009.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2009.getItems(), 900d, 5250d));

		TotalAmountColumn pD2009 = (TotalAmountColumn) gC2009.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2009.getItems(), 1200d, 6000d));

		GroupColumn gC2010 = (GroupColumn) grColumn.getColumn("2010");

		TotalAmountColumn aC2010 = (TotalAmountColumn) gC2010.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2010.getItems(), 300d, 3750d));

		TotalAmountColumn aD2010 = (TotalAmountColumn) gC2010.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2010.getItems(), 600d, 4500d));

		TotalAmountColumn pC2010 = (TotalAmountColumn) gC2010.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2010.getItems(), 900d, 5250d));

		TotalAmountColumn pD2010 = (TotalAmountColumn) gC2010.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2010.getItems(), 1200d, 6000d));

		GroupColumn totalColumns = (GroupColumn) colData.getColumn("Total Costs");
		TotalAmountColumn tAC = (TotalAmountColumn) totalColumns.getColumn("Actual Commitments");
		assertTrue(checkValues(tAC.getItems(), 600d, 7500d));

		TotalAmountColumn tAD = (TotalAmountColumn) totalColumns.getColumn("Actual Disbursements");
		assertTrue(checkValues(tAD.getItems(), 1200d, 9000d));

		TotalAmountColumn tPC = (TotalAmountColumn) totalColumns.getColumn("Planned Commitments");
		assertTrue(checkValues(tPC.getItems(), 1800d, 10500d));

		TotalAmountColumn tPD = (TotalAmountColumn) totalColumns.getColumn("Planned Disbursements");
		assertTrue(checkValues(tPD.getItems(), 2400d, 12000d));

	}

	public void testInfrastructureSupportDakar() throws Exception {
		GroupReportData report = (GroupReportData) generatedReport.getItem(0);
		GroupReportData firstLevel = (GroupReportData) report.getItem(0);
		ColumnReportData colData = (ColumnReportData) firstLevel.getItem(0);
		System.out.println(firstLevel + " " + colData);
		GroupColumn grColumn = (GroupColumn) colData.getColumn("Funding");

		GroupColumn gC2009 = (GroupColumn) grColumn.getColumn("2009");
		TotalAmountColumn aC2009 = (TotalAmountColumn) gC2009.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2009.getItems(), 700d, 1250d));

		TotalAmountColumn aD2009 = (TotalAmountColumn) gC2009.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2009.getItems(), 1400d, 1500d));

		TotalAmountColumn pC2009 = (TotalAmountColumn) gC2009.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2009.getItems(), 2100d, 1750d));

		TotalAmountColumn pD2009 = (TotalAmountColumn) gC2009.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2009.getItems(), 2800d, 2000d));

		GroupColumn gC2010 = (GroupColumn) grColumn.getColumn("2010");

		TotalAmountColumn aC2010 = (TotalAmountColumn) gC2010.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2010.getItems(), 700d, 1250d));

		TotalAmountColumn aD2010 = (TotalAmountColumn) gC2010.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2010.getItems(), 1400d, 1500d));

		TotalAmountColumn pC2010 = (TotalAmountColumn) gC2010.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2010.getItems(), 2100d, 1750d));

		TotalAmountColumn pD2010 = (TotalAmountColumn) gC2010.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2010.getItems(), 2800d, 2000d));

		GroupColumn totalColumns = (GroupColumn) colData.getColumn("Total Costs");
		TotalAmountColumn tAC = (TotalAmountColumn) totalColumns.getColumn("Actual Commitments");
		assertTrue(checkValues(tAC.getItems(), 1400d, 2500d));

		TotalAmountColumn tAD = (TotalAmountColumn) totalColumns.getColumn("Actual Disbursements");
		assertTrue(checkValues(tAD.getItems(), 2800d, 3000d));

		TotalAmountColumn tPC = (TotalAmountColumn) totalColumns.getColumn("Planned Commitments");
		assertTrue(checkValues(tPC.getItems(), 4200d, 3500d));

		TotalAmountColumn tPD = (TotalAmountColumn) totalColumns.getColumn("Planned Disbursements");
		assertTrue(checkValues(tPD.getItems(), 5600d, 4000d));

	}

	public void testInfrastructureSupportNairobi() throws Exception {
		GroupReportData report = (GroupReportData) generatedReport.getItem(0);
		GroupReportData firstLevel = (GroupReportData) report.getItem(0);
		ColumnReportData colData = (ColumnReportData) firstLevel.getItem(1);
		System.out.println(firstLevel + " " + colData);
		GroupColumn grColumn = (GroupColumn) colData.getColumn("Funding");
		GroupColumn gC2009 = (GroupColumn) grColumn.getColumn("2009");
		TotalAmountColumn aC2009 = (TotalAmountColumn) gC2009.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2009.getItems(), 300d, 3750d));

		TotalAmountColumn aD2009 = (TotalAmountColumn) gC2009.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2009.getItems(), 600d, 4500d));

		TotalAmountColumn pC2009 = (TotalAmountColumn) gC2009.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2009.getItems(), 900d, 5250d));

		TotalAmountColumn pD2009 = (TotalAmountColumn) gC2009.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2009.getItems(), 1200d, 6000d));

		GroupColumn gC2010 = (GroupColumn) grColumn.getColumn("2010");

		TotalAmountColumn aC2010 = (TotalAmountColumn) gC2010.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2010.getItems(), 300d, 3750d));

		TotalAmountColumn aD2010 = (TotalAmountColumn) gC2010.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2010.getItems(), 600d, 4500d));

		TotalAmountColumn pC2010 = (TotalAmountColumn) gC2010.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2010.getItems(), 900d, 5250d));

		TotalAmountColumn pD2010 = (TotalAmountColumn) gC2010.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2010.getItems(), 1200d, 6000d));

		GroupColumn totalColumns = (GroupColumn) colData.getColumn("Total Costs");
		TotalAmountColumn tAC = (TotalAmountColumn) totalColumns.getColumn("Actual Commitments");
		assertTrue(checkValues(tAC.getItems(), 600d, 7500d));

		TotalAmountColumn tAD = (TotalAmountColumn) totalColumns.getColumn("Actual Disbursements");
		assertTrue(checkValues(tAD.getItems(), 1200d, 9000d));

		TotalAmountColumn tPC = (TotalAmountColumn) totalColumns.getColumn("Planned Commitments");
		assertTrue(checkValues(tPC.getItems(), 1800d, 10500d));

		TotalAmountColumn tPD = (TotalAmountColumn) totalColumns.getColumn("Planned Disbursements");
		assertTrue(checkValues(tPD.getItems(), 2400d, 12000d));

	}

}

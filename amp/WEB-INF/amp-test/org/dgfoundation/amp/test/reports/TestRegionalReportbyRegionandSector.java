package org.dgfoundation.amp.test.reports;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnReportData;
import org.dgfoundation.amp.ar.GroupColumn;
import org.dgfoundation.amp.ar.GroupReportData;
import org.dgfoundation.amp.ar.TotalAmountColumn;

public class TestRegionalReportbyRegionandSector extends TestReportBase {

	public TestRegionalReportbyRegionandSector(String name) {
		super(name);
	}

	@Override
	protected Integer getReportType() {
		return ArConstants.REGIONAL_TYPE;
	}

	@Override
	public Long[] getHierarchies() throws Exception {
		// TODO Auto-generated method stub
		return new Long[] { getColumnByName("Region").getColumnId(), getColumnByName("Primary Sector").getColumnId() };
	}

	@Override
	public void testGrandTotalValues() throws Exception {
		assertTrue(checkValues(generatedReport.getTrailCells(), null, 12000d, 16000d, 20000d, 24000d, 12000d, 16000d, 20000d, 24000d, 24000d, 32000d, 40000d, 48000d));
	}

	public void testDakarAgriculture() throws Exception {
		GroupReportData report = (GroupReportData) generatedReport.getItem(0);
		GroupReportData firstLevel = (GroupReportData) report.getItem(0);
		ColumnReportData colData = (ColumnReportData) firstLevel.getItem(0);
		System.err.println(firstLevel + " " + colData);
		GroupColumn grColumn = (GroupColumn) colData.getColumn("Funding");
		/**
		 * 200 400 600 800 1500 1800 2100 2400
		 */

		GroupColumn gC2009 = (GroupColumn) grColumn.getColumn("2009");

		TotalAmountColumn aC2009 = (TotalAmountColumn) gC2009.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2009.getItems(), 200d, 1500d));

		TotalAmountColumn aD2009 = (TotalAmountColumn) gC2009.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2009.getItems(), 400d, 1800d));

		TotalAmountColumn pC2009 = (TotalAmountColumn) gC2009.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2009.getItems(), 600d, 2100d));

		TotalAmountColumn pD2009 = (TotalAmountColumn) gC2009.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2009.getItems(), 800d, 2400d));
		/**
		 * 200 400 600 800 1500 1800 2100 2400
		 **/
		GroupColumn gC2010 = (GroupColumn) grColumn.getColumn("2010");

		TotalAmountColumn aC2010 = (TotalAmountColumn) gC2010.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2010.getItems(), 200d, 1500d));

		TotalAmountColumn aD2010 = (TotalAmountColumn) gC2010.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2010.getItems(), 400d, 1800d));

		TotalAmountColumn pC2010 = (TotalAmountColumn) gC2010.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2010.getItems(), 600d, 2100d));

		TotalAmountColumn pD2010 = (TotalAmountColumn) gC2010.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2010.getItems(), 800d, 2400d));

		GroupColumn totalColumns = (GroupColumn) colData.getColumn("Total Costs");
		/**
		 * 400 800 1200 1600 3000 3600 4200 4800
		 */
		TotalAmountColumn tAC = (TotalAmountColumn) totalColumns.getColumn("Actual Commitments");
		assertTrue(checkValues(tAC.getItems(), 400d, 3000d));

		TotalAmountColumn tAD = (TotalAmountColumn) totalColumns.getColumn("Actual Disbursements");
		assertTrue(checkValues(tAD.getItems(), 800d, 3600d));

		TotalAmountColumn tPC = (TotalAmountColumn) totalColumns.getColumn("Planned Commitments");
		assertTrue(checkValues(tPC.getItems(), 1200d, 4200d));

		TotalAmountColumn tPD = (TotalAmountColumn) totalColumns.getColumn("Planned Disbursements");
		assertTrue(checkValues(tPD.getItems(), 1600d, 4800d));
	}

	public void testDakarEducation() throws Exception {
		GroupReportData report = (GroupReportData) generatedReport.getItem(0);
		GroupReportData firstLevel = (GroupReportData) report.getItem(0);
		ColumnReportData colData = (ColumnReportData) firstLevel.getItem(1);
		System.err.println(firstLevel + " " + colData);
		GroupColumn grColumn = (GroupColumn) colData.getColumn("Funding");
		/**
		 * 800 1600 2400 3200 3500 4200 4900 5600
		 */

		GroupColumn gC2009 = (GroupColumn) grColumn.getColumn("2009");

		TotalAmountColumn aC2009 = (TotalAmountColumn) gC2009.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2009.getItems(), 800d, 3500d));

		TotalAmountColumn aD2009 = (TotalAmountColumn) gC2009.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2009.getItems(), 1600d, 4200d));

		TotalAmountColumn pC2009 = (TotalAmountColumn) gC2009.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2009.getItems(), 2400d, 4900d));

		TotalAmountColumn pD2009 = (TotalAmountColumn) gC2009.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2009.getItems(), 3200d, 5600d));
		/**
		 * 800 1600 2400 3200 3500 4200 4900 5600
		 **/
		GroupColumn gC2010 = (GroupColumn) grColumn.getColumn("2010");

		TotalAmountColumn aC2010 = (TotalAmountColumn) gC2010.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2010.getItems(), 800d, 3500d));

		TotalAmountColumn aD2010 = (TotalAmountColumn) gC2010.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2010.getItems(), 1600d, 4200d));

		TotalAmountColumn pC2010 = (TotalAmountColumn) gC2010.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2010.getItems(), 2400d, 4900d));

		TotalAmountColumn pD2010 = (TotalAmountColumn) gC2010.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2010.getItems(), 3200d, 5600d));

		GroupColumn totalColumns = (GroupColumn) colData.getColumn("Total Costs");

		TotalAmountColumn tAC = (TotalAmountColumn) totalColumns.getColumn("Actual Commitments");
		assertTrue(checkValues(tAC.getItems(), 1600d, 7000d));

		TotalAmountColumn tAD = (TotalAmountColumn) totalColumns.getColumn("Actual Disbursements");
		assertTrue(checkValues(tAD.getItems(), 3200d, 8400d));

		TotalAmountColumn tPC = (TotalAmountColumn) totalColumns.getColumn("Planned Commitments");
		assertTrue(checkValues(tPC.getItems(), 4800d, 9800d));
		TotalAmountColumn tPD = (TotalAmountColumn) totalColumns.getColumn("Planned Disbursements");
		assertTrue(checkValues(tPD.getItems(), 6400d, 11200d));
	}

	public void testNairobiAgriculture() throws Exception {
		GroupReportData report = (GroupReportData) generatedReport.getItem(0);
		GroupReportData firstLevel = (GroupReportData) report.getItem(1);
		ColumnReportData colData = (ColumnReportData) firstLevel.getItem(0);
		System.err.println(firstLevel + " " + colData);
		GroupColumn grColumn = (GroupColumn) colData.getColumn("Funding");
		GroupColumn gC2009 = (GroupColumn) grColumn.getColumn("2009");

		/**
		 * 200 400 600 800 1500 1800 2100 2400
		 */
		TotalAmountColumn aC2009 = (TotalAmountColumn) gC2009.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2009.getItems(), 200d, 1500d));

		TotalAmountColumn aD2009 = (TotalAmountColumn) gC2009.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2009.getItems(), 400d, 1800d));

		TotalAmountColumn pC2009 = (TotalAmountColumn) gC2009.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2009.getItems(), 600d, 2100d));

		TotalAmountColumn pD2009 = (TotalAmountColumn) gC2009.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2009.getItems(), 800d, 2400d));
		/**
		 * 200 400 600 800 1500 1800 2100 2400
		 **/
		GroupColumn gC2010 = (GroupColumn) grColumn.getColumn("2010");

		TotalAmountColumn aC2010 = (TotalAmountColumn) gC2010.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2010.getItems(), 200d, 1500d));

		TotalAmountColumn aD2010 = (TotalAmountColumn) gC2010.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2010.getItems(), 400d, 1800d));

		TotalAmountColumn pC2010 = (TotalAmountColumn) gC2010.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2010.getItems(), 600d, 2100d));

		TotalAmountColumn pD2010 = (TotalAmountColumn) gC2010.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2010.getItems(), 800d, 2400d));

		GroupColumn totalColumns = (GroupColumn) colData.getColumn("Total Costs");
		/**
		 * 400 800 1200 1600 3000 3600 4200 4800
		 */
		TotalAmountColumn tAC = (TotalAmountColumn) totalColumns.getColumn("Actual Commitments");
		assertTrue(checkValues(tAC.getItems(), 400d, 3000d));

		TotalAmountColumn tAD = (TotalAmountColumn) totalColumns.getColumn("Actual Disbursements");
		assertTrue(checkValues(tAD.getItems(), 800d, 3600d));

		TotalAmountColumn tPC = (TotalAmountColumn) totalColumns.getColumn("Planned Commitments");
		assertTrue(checkValues(tPC.getItems(), 1200d, 4200d));
		TotalAmountColumn tPD = (TotalAmountColumn) totalColumns.getColumn("Planned Disbursements");
		assertTrue(checkValues(tPD.getItems(), 1600d, 4800d));

	}

	public void testNairobiEducation() throws Exception {
		GroupReportData report = (GroupReportData) generatedReport.getItem(0);
		GroupReportData firstLevel = (GroupReportData) report.getItem(1);
		ColumnReportData colData = (ColumnReportData) firstLevel.getItem(1);
		System.err.println(firstLevel + " " + colData);
		GroupColumn grColumn = (GroupColumn) colData.getColumn("Funding");
		GroupColumn gC2009 = (GroupColumn) grColumn.getColumn("2009");
		/**
		 800	1600	2400	3200
		3500	4200	4900	5600

		 */
		TotalAmountColumn aC2009 = (TotalAmountColumn) gC2009.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2009.getItems(), 800d, 3500d));

		TotalAmountColumn aD2009 = (TotalAmountColumn) gC2009.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2009.getItems(), 1600d, 4200d));

		TotalAmountColumn pC2009 = (TotalAmountColumn) gC2009.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2009.getItems(), 2400d, 4900d));

		TotalAmountColumn pD2009 = (TotalAmountColumn) gC2009.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2009.getItems(), 3200d, 5600d));
	
		
		GroupColumn gC2010 = (GroupColumn) grColumn.getColumn("2010");
		/**
		 800	1600	2400	3200
		3500	4200	4900	5600

		 */
		TotalAmountColumn aC2010 = (TotalAmountColumn) gC2010.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2010.getItems(), 800d, 3500d));

		TotalAmountColumn aD2010 = (TotalAmountColumn) gC2010.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2010.getItems(), 1600d, 4200d));

		TotalAmountColumn pC2010 = (TotalAmountColumn) gC2010.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2010.getItems(), 2400d, 4900d));

		TotalAmountColumn pD2010 = (TotalAmountColumn) gC2010.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2010.getItems(), 3200d, 5600d));

		GroupColumn totalColumns = (GroupColumn) colData.getColumn("Total Costs");

		TotalAmountColumn tAC = (TotalAmountColumn) totalColumns.getColumn("Actual Commitments");
		assertTrue(checkValues(tAC.getItems(), 1600d, 7000d));

		TotalAmountColumn tAD = (TotalAmountColumn) totalColumns.getColumn("Actual Disbursements");
		assertTrue(checkValues(tAD.getItems(), 3200d, 8400d));

		TotalAmountColumn tPC = (TotalAmountColumn) totalColumns.getColumn("Planned Commitments");
		assertTrue(checkValues(tPC.getItems(), 4800d, 9800d));
		TotalAmountColumn tPD = (TotalAmountColumn) totalColumns.getColumn("Planned Disbursements");
		assertTrue(checkValues(tPD.getItems(), 6400d, 11200d));
	}

}

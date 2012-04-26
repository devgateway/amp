package org.dgfoundation.amp.test.reports;

import org.dgfoundation.amp.ar.ColumnReportData;
import org.dgfoundation.amp.ar.GroupColumn;
import org.dgfoundation.amp.ar.TotalAmountColumn;

public class TestDonorReportbyDonorAgency extends TestReportBase {

	public TestDonorReportbyDonorAgency(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		removeData();
	}

	/**
	 * Donor
	 * 
	 * @throws Exception
	 */
	public Long[] getHierarchies() throws Exception {
		// TODO Auto-generated method stub
		return new Long[] { getColumnByName("Donor Agency").getColumnId() };
	}

	public void testGrandTotalValues() throws Exception {
		assertTrue(checkValues(generatedReport.getTrailCells(), null, 6000d, 8000d, 10000d, 12000d, 6000d, 8000d, 10000d, 12000d, 12000d, 16000d, 20000d, 24000d));
	}

	public void testValuesForWorldBank() throws Exception {
		ColumnReportData data = (ColumnReportData) generatedReport.getFirstColumnReport().getParent().getItem(1);
		System.out.println(data);

		GroupColumn grColumn = (GroupColumn) data.getColumn("Funding");
		// 2009
		GroupColumn gC2009 = (GroupColumn) grColumn.getColumn("2009");
		TotalAmountColumn aC2009 = (TotalAmountColumn) gC2009.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2009.getItems(), 1000d, 5000d));

		TotalAmountColumn aD2009 = (TotalAmountColumn) gC2009.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2009.getItems(), 2000d, 6000d));

		TotalAmountColumn pC2009 = (TotalAmountColumn) gC2009.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2009.getItems(), 3000d, 7000d));

		TotalAmountColumn pD2009 = (TotalAmountColumn) gC2009.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2009.getItems(), 4000d, 8000d));

		// 2010
		GroupColumn gC2010 = (GroupColumn) grColumn.getColumn("2010");
		TotalAmountColumn aC2010 = (TotalAmountColumn) gC2010.getColumn("Actual Commitments");
		// assertTrue(chekValues(aC2010.getItems(), 200d, 1500d));

		TotalAmountColumn aD2010 = (TotalAmountColumn) gC2010.getColumn("Actual Disbursements");
		// assertTrue(chekValues(aD2010.getItems(), 400d, 1800d));

		TotalAmountColumn pC2010 = (TotalAmountColumn) gC2010.getColumn("Planned Commitments");
		// assertTrue(chekValues(aD2010.getItems(), 400d, 1800d));

		TotalAmountColumn pD2010 = (TotalAmountColumn) gC2010.getColumn("Planned Disbursements");
		// assertTrue(chekValues(pD2010.getItems(), 800d, 2400d));

		GroupColumn totalColumns = (GroupColumn) data.getColumn("Total Costs");
		TotalAmountColumn tAC = (TotalAmountColumn) totalColumns.getColumn("Actual Commitments");
		assertTrue(checkValues(tAC.getItems(), 1000d, 5000d));

		TotalAmountColumn tAD = (TotalAmountColumn) totalColumns.getColumn("Actual Disbursements");
		assertTrue(checkValues(tAD.getItems(), 2000d, 6000d));

		TotalAmountColumn tPC = (TotalAmountColumn) totalColumns.getColumn("Planned Commitments");
		assertTrue(checkValues(tPC.getItems(), 3000d, 7000d));

		TotalAmountColumn tPD = (TotalAmountColumn) totalColumns.getColumn("Planned Disbursements");
		assertTrue(checkValues(tPD.getItems(), 4000d, 8000d));

		// Checking for totals

	}

	public void testValuesForACDI() throws Exception {
		ColumnReportData data = (ColumnReportData) generatedReport.getFirstColumnReport().getParent().getItem(0);
		System.out.println(data);

		GroupColumn grColumn = (GroupColumn) data.getColumn("Funding");
		// 2009
		GroupColumn gC2010 = (GroupColumn) grColumn.getColumn("2010");
		TotalAmountColumn aC2009 = (TotalAmountColumn) gC2010.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2009.getItems(), 1000d, 5000d));

		TotalAmountColumn aD2009 = (TotalAmountColumn) gC2010.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2009.getItems(), 2000d, 6000d));

		TotalAmountColumn pC2009 = (TotalAmountColumn) gC2010.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2009.getItems(), 3000d, 7000d));

		TotalAmountColumn pD2009 = (TotalAmountColumn) gC2010.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2009.getItems(), 4000d, 8000d));

		GroupColumn totalColumns = (GroupColumn) data.getColumn("Total Costs");
		TotalAmountColumn tAC = (TotalAmountColumn) totalColumns.getColumn("Actual Commitments");
		assertTrue(checkValues(tAC.getItems(), 1000d, 5000d));

		TotalAmountColumn tAD = (TotalAmountColumn) totalColumns.getColumn("Actual Disbursements");
		assertTrue(checkValues(tAD.getItems(), 2000d, 6000d));

		TotalAmountColumn tPC = (TotalAmountColumn) totalColumns.getColumn("Planned Commitments");
		assertTrue(checkValues(tPC.getItems(), 3000d, 7000d));

		TotalAmountColumn tPD = (TotalAmountColumn) totalColumns.getColumn("Planned Disbursements");
		assertTrue(checkValues(tPD.getItems(), 4000d, 8000d));
	}

}

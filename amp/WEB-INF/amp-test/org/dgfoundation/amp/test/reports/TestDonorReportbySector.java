package org.dgfoundation.amp.test.reports;

import org.dgfoundation.amp.ar.ColumnReportData;
import org.dgfoundation.amp.ar.GroupColumn;
import org.dgfoundation.amp.ar.TotalAmountColumn;

public class TestDonorReportbySector extends TestReportBase {

	public TestDonorReportbySector(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
	}

	/**
	 * Sector
	 * 
	 * @throws Exception
	 */
	public Long[] getHierarchies() throws Exception {
		return new Long[] { getColumnByName("Primary Sector").getColumnId() };
	}

	public void testGrandTotalValues() throws Exception {
		assertTrue(checkValues(generatedReport.getTrailCells(), null, 6000d, 8000d, 10000d, 12000d, 6000d, 8000d, 10000d, 12000d, 12000d, 16000d, 20000d, 24000d));
	}

	public void testValuesForSectorAgriculture() throws Exception {
		ColumnReportData data = (ColumnReportData) generatedReport.getFirstColumnReport().getParent().getItem(0);
		System.out.println(data);

		GroupColumn grColumn = (GroupColumn) generatedReport.getFirstColumnReport().getItems().get(1);
		// 2009
		GroupColumn gC2009 = (GroupColumn) grColumn.getColumn("2009");
		TotalAmountColumn aC2009 = (TotalAmountColumn) gC2009.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2009.getItems(), 200d, 1500d));

		TotalAmountColumn aD2009 = (TotalAmountColumn) gC2009.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2009.getItems(), 400d, 1800d));

		TotalAmountColumn pC2009 = (TotalAmountColumn) gC2009.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2009.getItems(), 600d, 2100d));

		TotalAmountColumn pD2009 = (TotalAmountColumn) gC2009.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2009.getItems(), 800d, 2400d));

		// 2010
		GroupColumn gC2010 = (GroupColumn) grColumn.getColumn("2010");
		TotalAmountColumn aC2010 = (TotalAmountColumn) gC2010.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2010.getItems(), 200d, 1500d));

		TotalAmountColumn aD2010 = (TotalAmountColumn) gC2010.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2010.getItems(), 400d, 1800d));

		TotalAmountColumn pC2010 = (TotalAmountColumn) gC2010.getColumn("Planned Commitments");
		assertTrue(checkValues(aD2010.getItems(), 400d, 1800d));

		TotalAmountColumn pD2010 = (TotalAmountColumn) gC2010.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2010.getItems(), 800d, 2400d));

		GroupColumn totalColumns = (GroupColumn) data.getColumn("Total Costs");
		TotalAmountColumn tAC = (TotalAmountColumn) totalColumns.getColumn("Actual Commitments");
		assertTrue(checkValues(tAC.getItems(), 400d, 3000d));

		TotalAmountColumn tAD = (TotalAmountColumn) totalColumns.getColumn("Actual Disbursements");
		assertTrue(checkValues(tAD.getItems(), 800d, 3600d));

		TotalAmountColumn tPC = (TotalAmountColumn) totalColumns.getColumn("Planned Commitments");
		assertTrue(checkValues(tPC.getItems(), 1200d, 4200d));

		TotalAmountColumn tPD = (TotalAmountColumn) totalColumns.getColumn("Planned Disbursements");
		assertTrue(checkValues(tPD.getItems(), 1600d, 4800d));

	}

	public void testValuesForSectorEducation() throws Exception {

		ColumnReportData data = (ColumnReportData) generatedReport.getFirstColumnReport().getParent().getItem(1);
		System.out.println(data);
		GroupColumn grColumn = (GroupColumn) data.getItem(1);
		// 2009
		GroupColumn gC2009 = (GroupColumn) grColumn.getColumn("2009");
		TotalAmountColumn aC2009 = (TotalAmountColumn) gC2009.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2009.getItems(), 800d, 3500d));

		TotalAmountColumn aD2009 = (TotalAmountColumn) gC2009.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2009.getItems(), 1600d, 4200d));

		TotalAmountColumn pC2009 = (TotalAmountColumn) gC2009.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2009.getItems(), 2400d, 4900d));

		TotalAmountColumn pD2009 = (TotalAmountColumn) gC2009.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2009.getItems(), 3200d, 5600d));

		// 2010
		GroupColumn gC2010 = (GroupColumn) grColumn.getColumn("2010");
		TotalAmountColumn aC2010 = (TotalAmountColumn) gC2009.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2010.getItems(), 800d, 3500d));

		TotalAmountColumn aD2010 = (TotalAmountColumn) gC2010.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2010.getItems(), 1600d, 4200d));

		TotalAmountColumn pC2010 = (TotalAmountColumn) gC2010.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2010.getItems(), 2400d, 4900d));

		TotalAmountColumn pD2010 = (TotalAmountColumn) gC2010.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2010.getItems(), 3200d, 5600d));

		GroupColumn totalColumns = (GroupColumn) data.getColumn("Total Costs");
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

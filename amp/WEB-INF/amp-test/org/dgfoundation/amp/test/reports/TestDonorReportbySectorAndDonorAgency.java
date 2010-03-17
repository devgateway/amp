package org.dgfoundation.amp.test.reports;

import java.util.Collection;

import org.dgfoundation.amp.ar.ColumnReportData;
import org.dgfoundation.amp.ar.GroupColumn;
import org.dgfoundation.amp.ar.GroupReportData;
import org.dgfoundation.amp.ar.TotalAmountColumn;

public class TestDonorReportbySectorAndDonorAgency extends TestReportBase {

	public TestDonorReportbySectorAndDonorAgency(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Long[] getColumns() throws Exception {
		// TODO Auto-generated method stub
		return new Long[] { getColumnByName("Project Title").getColumnId() };
	}

	@Override
	public Long[] getHierarchies() throws Exception {
		// TODO Auto-generated method stub
		return new Long[] {getColumnByName("Primary Sector").getColumnId() , getColumnByName("Donor Agency").getColumnId() };
	}

	@Override
	public void testGrandTotalValues() throws Exception {
		assertTrue(checkValues(generatedReport.getTrailCells(), null, 6000d, 8000d, 10000d, 12000d, 6000d, 8000d, 10000d, 12000d, 12000d, 16000d, 20000d, 24000d));
	}

	public void testAgricultureValues() throws Exception {
		GroupReportData data = (GroupReportData) generatedReport.getItems().get(0);
		Collection<GroupReportData> items = data.getItems();

		GroupReportData item;
		item = (GroupReportData) data.getItem(0);
		System.out.println(item);
		assertTrue(checkValues(item.getTrailCells(), null, 1700d, 2200d, 2700d, 3200d, 1700d, 2200d, 2700d, 3200d, 3400d, 4400d, 5400d, 6400d));
	}

	public void testEducationValues() throws Exception {
		GroupReportData data = (GroupReportData) generatedReport.getItems().get(0);
		GroupReportData item;
		item = (GroupReportData) data.getItem(1);
		System.out.println(item);
		assertTrue(checkValues(item.getTrailCells(), null, 4300d, 5800d, 7300d, 8800d, 4300d, 5800d, 7300d, 8800d, 8600d, 11600d, 14600d, 17600d));

	}

	public void testWorldBankOnAgriculture() throws Exception {
		GroupReportData data = (GroupReportData) generatedReport.getItems().get(0);
		Collection<GroupReportData> items = data.getItems();
		GroupReportData item;
		item = (GroupReportData) data.getItem(0);
		ColumnReportData colData = (ColumnReportData) item.getItems().get(1);
		System.out.println(item + " " + colData);
		GroupColumn grColumn = (GroupColumn) colData.getColumn("Funding");

		GroupColumn gC2009 = (GroupColumn) grColumn.getColumn("2009");

		TotalAmountColumn aC2009 = (TotalAmountColumn) gC2009.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2009.getItems(), 200d, 1500d));

		TotalAmountColumn aD2009 = (TotalAmountColumn) gC2009.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2009.getItems(), 400d, 1800d));
		TotalAmountColumn pC2009 = (TotalAmountColumn) gC2009.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2009.getItems(), 600d, 2100d));

		TotalAmountColumn pD2009 = (TotalAmountColumn) gC2009.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2009.getItems(), 800d, 2400d));

		GroupColumn gC2010 = (GroupColumn) grColumn.getColumn("2010");

		GroupColumn totalColumns = (GroupColumn) colData.getColumn("Total Costs");
		TotalAmountColumn tAC = (TotalAmountColumn) totalColumns.getColumn("Actual Commitments");
		assertTrue(checkValues(tAC.getItems(), 200d, 1500d));

		TotalAmountColumn tAD = (TotalAmountColumn) totalColumns.getColumn("Actual Disbursements");
		assertTrue(checkValues(tAD.getItems(), 400d, 1800d));

		TotalAmountColumn tPC = (TotalAmountColumn) totalColumns.getColumn("Planned Commitments");
		assertTrue(checkValues(tPC.getItems(), 600d, 2100d));

		TotalAmountColumn tPD = (TotalAmountColumn) totalColumns.getColumn("Planned Disbursements");
		assertTrue(checkValues(tPD.getItems(), 800d, 2400d));

	}

	public void testWorldBankOnEducationValues() throws Exception {
		GroupReportData data = (GroupReportData) generatedReport.getItems().get(0);
		Collection<GroupReportData> items = data.getItems();
		GroupReportData item;
		item = (GroupReportData) data.getItem(1);
		ColumnReportData colData = (ColumnReportData) item.getItems().get(1);
		System.out.println(item + " " + colData);
		GroupColumn grColumn = (GroupColumn) colData.getColumn("Funding");

		GroupColumn gC2009 = (GroupColumn) grColumn.getColumn("2009");
		/**
		 *800 1600 2400 3200 3500 4200 4900 5600
		 */
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
		/**
		 * 800 1600 2400 3200 3500 4200 4900 5600
		 */
		GroupColumn totalColumns = (GroupColumn) colData.getColumn("Total Costs");
		TotalAmountColumn tAC = (TotalAmountColumn) totalColumns.getColumn("Actual Commitments");
		assertTrue(checkValues(tAC.getItems(), 800d, 3500d));

		TotalAmountColumn tAD = (TotalAmountColumn) totalColumns.getColumn("Actual Disbursements");
		assertTrue(checkValues(tAD.getItems(), 1600d, 4200d));

		TotalAmountColumn tPC = (TotalAmountColumn) totalColumns.getColumn("Planned Commitments");
		assertTrue(checkValues(tPC.getItems(), 2400d, 4900d));

		TotalAmountColumn tPD = (TotalAmountColumn) totalColumns.getColumn("Planned Disbursements");
		assertTrue(checkValues(tPD.getItems(), 3200d, 5600d));
	}

	public void testACDIOnAgriculture() throws Exception {
		GroupReportData data = (GroupReportData) generatedReport.getItems().get(0);
		Collection<GroupReportData> items = data.getItems();
		GroupReportData item;
		item = (GroupReportData) data.getItem(0);
		ColumnReportData colData = (ColumnReportData) item.getItems().get(0);
		System.out.println(item + " " + colData);
		GroupColumn grColumn = (GroupColumn) colData.getColumn("Funding");

		/**
		 * 200 400 600 800 200 400 600 800 1500 1800 2100 2400 1500 1800 2100
		 * 2400
		 */
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
		TotalAmountColumn tAC = (TotalAmountColumn) totalColumns.getColumn("Actual Commitments");
		assertTrue(checkValues(tAC.getItems(), 200d, 1500d));

		TotalAmountColumn tAD = (TotalAmountColumn) totalColumns.getColumn("Actual Disbursements");
		assertTrue(checkValues(tAD.getItems(), 400d, 1800d));

		TotalAmountColumn tPC = (TotalAmountColumn) totalColumns.getColumn("Planned Commitments");
		assertTrue(checkValues(tPC.getItems(), 600d, 2100d));

		TotalAmountColumn tPD = (TotalAmountColumn) totalColumns.getColumn("Planned Disbursements");
		assertTrue(checkValues(tPD.getItems(), 800d, 2400d));

	}

	public void testACDIOnEducationValues() throws Exception {
		GroupReportData data = (GroupReportData) generatedReport.getItems().get(0);
		Collection<GroupReportData> items = data.getItems();
		GroupReportData item;
		item = (GroupReportData) data.getItem(1);
		ColumnReportData colData = (ColumnReportData) item.getItems().get(0);
		System.out.println(item + " " + colData);
		GroupColumn grColumn = (GroupColumn) colData.getColumn("Funding");

		/**
		 * 800 1600 2400 3200 800 1600 2400 3200 3500 4200 4900 5600 3500 4200
		 * 4900 5600
		 */
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
		assertTrue(checkValues(tAC.getItems(), 800d, 3500d));

		TotalAmountColumn tAD = (TotalAmountColumn) totalColumns.getColumn("Actual Disbursements");
		assertTrue(checkValues(tAD.getItems(), 1600d, 4200d));

		TotalAmountColumn tPC = (TotalAmountColumn) totalColumns.getColumn("Planned Commitments");
		assertTrue(checkValues(tPC.getItems(), 2400d, 4900d));

		TotalAmountColumn tPD = (TotalAmountColumn) totalColumns.getColumn("Planned Disbursements");
		assertTrue(checkValues(tPD.getItems(), 3200d, 5600d));
	}
}

package org.dgfoundation.amp.test.reports;

import java.util.Collection;

import junit.framework.Test;

import org.dgfoundation.amp.ar.ColumnReportData;
import org.dgfoundation.amp.ar.GroupColumn;
import org.dgfoundation.amp.ar.GroupReportData;
import org.dgfoundation.amp.ar.TotalAmountColumn;

public class TestDonorReportbyRegionAndDonor extends TestReportBase{

	public TestDonorReportbyRegionAndDonor(String name) {
		super(name);
	}

	/**
	 * Region
	 */
	public Long[] getHierarchies() throws Exception {
		return new Long[] { getColumnByName("Region").getColumnId(), getColumnByName("Donor Agency").getColumnId() };
	}

	@Override
	public void testGrandTotalValues() throws Exception {
		assertTrue(checkValues(generatedReport.getTrailCells(), null, 6000d, 8000d, 10000d, 12000d, 6000d, 8000d, 10000d, 12000d, 12000d, 16000d, 20000d, 24000d));
	}

	public void tesDakarValues() throws Exception {
		GroupReportData data = (GroupReportData) generatedReport.getItems().get(0);
		Collection<GroupReportData> items = data.getItems();

		GroupReportData item;
		item = (GroupReportData) data.getItem(0);
		System.out.println(item);
		assertTrue(checkValues(item.getTrailCells(), null, 1700d, 2200d, 2700d, 3200d, 1700d, 2200d, 2700d, 3200d, 3400d, 4400d, 5400d, 6400d));
	}

	public void testNairobiValues() throws Exception {
		GroupReportData data = (GroupReportData) generatedReport.getItems().get(0);
		GroupReportData item;
		item = (GroupReportData) data.getItem(1);
		System.out.println(item);
		// assertTrue(chekValues(item.getTrailCells(),null, 4050d, 5100d, 6150d,
		// 7200d, 4050d, 5100d, 6150d, 7200d, 8100, 10,200, 12,300, 14,400));

	}

	public void testWorldBankOnDakar() throws Exception {
		GroupReportData data = (GroupReportData) generatedReport.getItems().get(0);
		Collection<GroupReportData> items = data.getItems();
		GroupReportData item;
		item = (GroupReportData) data.getItem(0);
		ColumnReportData colData = (ColumnReportData) item.getItems().get(1);
		System.out.println(item + " " + colData);
		GroupColumn grColumn = (GroupColumn) colData.getColumn("Funding");

		GroupColumn gC2009 = (GroupColumn) grColumn.getColumn("2009");
		/**
		 * 700 1400 2100 2800 1250 1500 1750 2000
		 */
		TotalAmountColumn aC2009 = (TotalAmountColumn) gC2009.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2009.getItems(), 700d, 1250d));

		TotalAmountColumn aD2009 = (TotalAmountColumn) gC2009.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2009.getItems(), 1400d, 1500d));

		TotalAmountColumn pC2009 = (TotalAmountColumn) gC2009.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2009.getItems(), 2100d, 1750d));

		TotalAmountColumn pD2009 = (TotalAmountColumn) gC2009.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2009.getItems(), 2800d, 2000d));

		GroupColumn gC2010 = (GroupColumn) grColumn.getColumn("2010");

		GroupColumn totalColumns = (GroupColumn) colData.getColumn("Total Costs");
		/**
		 * 700 1400 2100 2800 1250 1500 1750 2000
		 */
		TotalAmountColumn tAC = (TotalAmountColumn) totalColumns.getColumn("Actual Commitments");
		assertTrue(checkValues(tAC.getItems(), 700d, 1250d));

		TotalAmountColumn tAD = (TotalAmountColumn) totalColumns.getColumn("Actual Disbursements");
		assertTrue(checkValues(tAD.getItems(), 1400d, 1500d));

		TotalAmountColumn tPC = (TotalAmountColumn) totalColumns.getColumn("Planned Commitments");
		assertTrue(checkValues(tPC.getItems(), 2100d, 1750d));

		TotalAmountColumn tPD = (TotalAmountColumn) totalColumns.getColumn("Planned Disbursements");
		assertTrue(checkValues(tPD.getItems(), 2800d, 2000d));

	}

	public void testWorldBankOnNairobiValues() throws Exception {
		GroupReportData data = (GroupReportData) generatedReport.getItems().get(0);
		Collection<GroupReportData> items = data.getItems();
		GroupReportData item;
		item = (GroupReportData) data.getItem(1);
		ColumnReportData colData = (ColumnReportData) item.getItems().get(1);
		System.out.println(item + " " + colData);
		GroupColumn grColumn = (GroupColumn) colData.getColumn("Funding");

		GroupColumn gC2009 = (GroupColumn) grColumn.getColumn("2009");
		/**
		 * 300 600 900 1200 3750 4500 5250 6000
		 */
		TotalAmountColumn aC2009 = (TotalAmountColumn) gC2009.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2009.getItems(), 300d, 3750d));

		TotalAmountColumn aD2009 = (TotalAmountColumn) gC2009.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2009.getItems(), 600d, 4500d));

		TotalAmountColumn pC2009 = (TotalAmountColumn) gC2009.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2009.getItems(), 900d, 5250d));

		TotalAmountColumn pD2009 = (TotalAmountColumn) gC2009.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2009.getItems(), 1200d, 6000d));

		// 2010
		GroupColumn gC2010 = (GroupColumn) grColumn.getColumn("2010");
		/**
		 * 300 600 900 1200 3750 4500 5250 6000
		 */
		GroupColumn totalColumns = (GroupColumn) colData.getColumn("Total Costs");
		TotalAmountColumn tAC = (TotalAmountColumn) totalColumns.getColumn("Actual Commitments");
		assertTrue(checkValues(tAC.getItems(), 300d, 3750d));

		TotalAmountColumn tAD = (TotalAmountColumn) totalColumns.getColumn("Actual Disbursements");
		assertTrue(checkValues(tAD.getItems(), 600d, 4500d));

		TotalAmountColumn tPC = (TotalAmountColumn) totalColumns.getColumn("Planned Commitments");
		assertTrue(checkValues(tPC.getItems(), 900d, 5250d));

		TotalAmountColumn tPD = (TotalAmountColumn) totalColumns.getColumn("Planned Disbursements");
		assertTrue(checkValues(tPD.getItems(), 1200d, 6000d));
	}

	public void testACDIOnDakar() throws Exception {
		GroupReportData data = (GroupReportData) generatedReport.getItems().get(0);
		Collection<GroupReportData> items = data.getItems();
		GroupReportData item;
		item = (GroupReportData) data.getItem(0);
		ColumnReportData colData = (ColumnReportData) item.getItems().get(0);
		System.out.println(item + " " + colData);
		GroupColumn grColumn = (GroupColumn) colData.getColumn("Funding");

		/**
		 * 700 1400 2100 2800 700 1400 2100 2800 1250 1500 1750 2000 1250 1500
		 * 1750 2000
		 */
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
		assertTrue(checkValues(tAC.getItems(), 700d, 1250d));

		TotalAmountColumn tAD = (TotalAmountColumn) totalColumns.getColumn("Actual Disbursements");
		assertTrue(checkValues(tAD.getItems(), 1400d, 1500d));

		TotalAmountColumn tPC = (TotalAmountColumn) totalColumns.getColumn("Planned Commitments");
		assertTrue(checkValues(tPC.getItems(), 2100d, 1750d));

		TotalAmountColumn tPD = (TotalAmountColumn) totalColumns.getColumn("Planned Disbursements");
		assertTrue(checkValues(tPD.getItems(), 2800d, 2000d));

	}

	public void testACDIOnNairobiValues() throws Exception {
		GroupReportData data = (GroupReportData) generatedReport.getItems().get(0);
		Collection<GroupReportData> items = data.getItems();
		GroupReportData item;
		item = (GroupReportData) data.getItem(1);
		ColumnReportData colData = (ColumnReportData) item.getItems().get(0);
		System.out.println(item + " " + colData);
		GroupColumn grColumn = (GroupColumn) colData.getColumn("Funding");

		GroupColumn gC2010 = (GroupColumn) grColumn.getColumn("2010");
		/*
		 * 
		 * 300 600 900 1200 300 600 900 1200 3750 4500 5250 6000 3750 4500 5250
		 * 6000
		 */
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
		assertTrue(checkValues(tAC.getItems(), 300d, 3750d));

		TotalAmountColumn tAD = (TotalAmountColumn) totalColumns.getColumn("Actual Disbursements");
		assertTrue(checkValues(tAD.getItems(), 600d, 4500d));

		TotalAmountColumn tPC = (TotalAmountColumn) totalColumns.getColumn("Planned Commitments");
		assertTrue(checkValues(tPC.getItems(), 900d, 5250d));

		TotalAmountColumn tPD = (TotalAmountColumn) totalColumns.getColumn("Planned Disbursements");
		assertTrue(checkValues(tPD.getItems(), 1200d, 6000d));
	}
}

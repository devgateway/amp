package org.dgfoundation.amp.test.reports;

import java.util.Collection;

import org.dgfoundation.amp.ar.ColumnReportData;
import org.dgfoundation.amp.ar.GroupColumn;
import org.dgfoundation.amp.ar.GroupReportData;
import org.dgfoundation.amp.ar.TotalAmountColumn;

public class TestDonorReportbyRegionSectorandNPO extends TestReportBase {

	public TestDonorReportbyRegionSectorandNPO(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
	}

	@Override
	public Long[] getHierarchies() throws Exception {
		return new Long[] { getColumnByName("Region").getColumnId(), getColumnByName("Primary Sector").getColumnId(), getColumnByName("National Planning Objectives").getColumnId() };
	}

	@Override
	public void testGrandTotalValues() throws Exception {
		assertTrue(checkValues(generatedReport.getTrailCells(), null, 6000d, 8000d, 10000d, 12000d, 6000d, 8000d, 10000d, 12000d, 12000d, 16000d, 20000d, 24000d));
	}

	public void testRegionValues() throws Exception {
		GroupReportData data = (GroupReportData) generatedReport.getItems().get(0);
		Collection<GroupReportData> items = data.getItems();

		GroupReportData item;
		item = (GroupReportData) data.getItem(0);
		System.out.println(item);
		// 1950d,2900d,3850d,4800d,1950d,2900d,3850d,4800d,3900d,5800d,7700d,9600d
		assertTrue(checkValues(item.getTrailCells(), null, 1950d, 2900d, 3850d, 4800d, 1950d, 2900d, 3850d, 4800d, 3900d, 5800d, 7700d, 9600d));

		item = (GroupReportData) data.getItem(1);
		System.out.println(item);
		// 4050d, 5100d, 6150d, 7200d, 4050d, 5100d, 6150d, 7200d, 8100d,
		// 10200d, 12300d, 14400d
		assertTrue(checkValues(item.getTrailCells(), null, 4050d, 5100d, 6150d, 7200d, 4050d, 5100d, 6150d, 7200d, 8100d, 10200d, 12300d, 14400d));

	}

	public void testSectorValues() throws Exception {
		GroupReportData data = (GroupReportData) generatedReport.getItems().get(0);
		Collection<GroupReportData> items = data.getItems();

		GroupReportData item;
		item = (GroupReportData) data.getItem(0);
		item = (GroupReportData) item.getItems().get(0);
		System.out.println(item);
		assertTrue(checkValues(item.getTrailCells(), null, 515d, 730d, 945d, 1160d, 515d, 730d, 945d, 1160d, 1030d, 1460d, 1890d, 2320d));

		item = (GroupReportData) data.getItem(0);
		item = (GroupReportData) item.getItems().get(1);
		System.out.println(item);
		// 1435d, 2170d, 2905d, 3640d, 1435d, 2170d, 2905d, 3640d, 2870d, 4340d,
		// 5810d, 7280d
		assertTrue(checkValues(item.getTrailCells(), null, 1435d, 2170d, 2905d, 3640d, 1435d, 2170d, 2905d, 3640d, 2870d, 4340d, 5810d, 7280d));

		item = (GroupReportData) data.getItem(1);
		item = (GroupReportData) item.getItems().get(0);
		System.out.println(item);
		// 1185d,1470d,1755d, 2040d, 1185d, 1470d, 1755d, 2040d, 2370d, 2940d,
		// 3510d, 4080d
		assertTrue(checkValues(item.getTrailCells(), null, 1185d, 1470d, 1755d, 2040d, 1185d, 1470d, 1755d, 2040d, 2370d, 2940d, 3510d, 4080d));

		item = (GroupReportData) data.getItem(1);
		item = (GroupReportData) item.getItems().get(1);
		System.out.println(item);
		// 2865d, 3630d, 4395d, 5160d, 2865d, 3630d, 4395d, 5160d, 5730d, 7260d,
		// 8790d, 10320d
		assertTrue(checkValues(item.getTrailCells(), null, 2865d, 3630d, 4395d, 5160d, 2865d, 3630d, 4395d, 5160d, 5730d, 7260d, 8790d, 10320d));

	}

	public void testDakarAgricultureHealthProgramValues() throws Exception {
		GroupReportData data = (GroupReportData) generatedReport.getItems().get(0);
		Collection<GroupReportData> items = data.getItems();
		GroupReportData item;
		item = (GroupReportData) data.getItem(0);

		GroupReportData sector = (GroupReportData) item.getItems().get(0);
		ColumnReportData program = (ColumnReportData) sector.getItems().get(0);
		ColumnReportData colData = program;

		System.out.println(item + " " + colData);
		GroupColumn grColumn = (GroupColumn) colData.getColumn("Funding");
		GroupColumn gC2009 = (GroupColumn) grColumn.getColumn("2009");
		/**
		 * 42 84 126 168 42 84 126 168 84 168 252 336 187.5 225 262.5 300 187.5
		 * 225 262.5 300 375 450 525 600
		 */
		TotalAmountColumn aC2009 = (TotalAmountColumn) gC2009.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2009.getItems(), 42d, 187.5d));

		TotalAmountColumn aD2009 = (TotalAmountColumn) gC2009.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2009.getItems(), 84d, 225d));

		TotalAmountColumn pC2009 = (TotalAmountColumn) gC2009.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2009.getItems(), 126d, 262.5d));

		TotalAmountColumn pD2009 = (TotalAmountColumn) gC2009.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2009.getItems(), 168d, 300d));

		GroupColumn gC2010 = (GroupColumn) grColumn.getColumn("2010");
		TotalAmountColumn aC2010 = (TotalAmountColumn) gC2010.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2010.getItems(), 42d, 187.5d));

		TotalAmountColumn aD2010 = (TotalAmountColumn) gC2010.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2010.getItems(), 84d, 225d));

		TotalAmountColumn pC2010 = (TotalAmountColumn) gC2010.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2010.getItems(), 126d, 262.5d));

		TotalAmountColumn pD2010 = (TotalAmountColumn) gC2010.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2010.getItems(), 168d, 300d));

		GroupColumn totalColumns = (GroupColumn) colData.getColumn("Total Costs");
		/**
		 * 42 84 126 168 42 84 126 168 84 168 252 336 187.5 225 262.5 300 187.5
		 * 225 262.5 300 375 450 525 600
		 * 
		 * */
		TotalAmountColumn tAC = (TotalAmountColumn) totalColumns.getColumn("Actual Commitments");
		assertTrue(checkValues(tAC.getItems(), 84d, 375d));

		TotalAmountColumn tAD = (TotalAmountColumn) totalColumns.getColumn("Actual Disbursements");
		assertTrue(checkValues(tAD.getItems(), 168d, 450d));

		TotalAmountColumn tPC = (TotalAmountColumn) totalColumns.getColumn("Planned Commitments");
		assertTrue(checkValues(tPC.getItems(), 252d, 525d));

		TotalAmountColumn tPD = (TotalAmountColumn) totalColumns.getColumn("Planned Disbursements");
		assertTrue(checkValues(tPD.getItems(), 336d, 600d));

	}

	public void testDakarAgricultureMeternityProgramValues() throws Exception {
		GroupReportData data = (GroupReportData) generatedReport.getItems().get(0);
		Collection<GroupReportData> items = data.getItems();
		GroupReportData item;
		item = (GroupReportData) data.getItem(0);

		GroupReportData sector = (GroupReportData) item.getItems().get(0);
		ColumnReportData program = (ColumnReportData) sector.getItems().get(1);
		ColumnReportData colData = program;

		System.out.println(item + " " + colData);
		GroupColumn grColumn = (GroupColumn) colData.getColumn("Funding");

		/**
		 * 98 196 294 392 98 196 294 392 196 392 588 784 187.5 225 262.5 300
		 * 187.5 225 262.5 300 375 450 525 600
		 */
		GroupColumn gC2009 = (GroupColumn) grColumn.getColumn("2009");
		TotalAmountColumn aC2009 = (TotalAmountColumn) gC2009.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2009.getItems(), 98d, 187.5d));

		TotalAmountColumn aD2009 = (TotalAmountColumn) gC2009.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2009.getItems(), 196d, 225d));

		TotalAmountColumn pC2009 = (TotalAmountColumn) gC2009.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2009.getItems(), 294d, 262.5d));

		TotalAmountColumn pD2009 = (TotalAmountColumn) gC2009.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2009.getItems(), 392d, 300d));

		/**
		 * 98 196 294 392 98 196 294 392 196 392 588 784 187.5 225 262.5 300
		 * 187.5 225 262.5 300 375 450 525 600
		 */

		GroupColumn gC2010 = (GroupColumn) grColumn.getColumn("2010");
		TotalAmountColumn aC2010 = (TotalAmountColumn) gC2010.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2010.getItems(), 98d, 187.5d));

		TotalAmountColumn aD2010 = (TotalAmountColumn) gC2010.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2010.getItems(), 196d, 225d));

		TotalAmountColumn pC2010 = (TotalAmountColumn) gC2010.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2010.getItems(), 294d, 262.5d));

		TotalAmountColumn pD2010 = (TotalAmountColumn) gC2010.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2010.getItems(), 392d, 300d));

		GroupColumn totalColumns = (GroupColumn) colData.getColumn("Total Costs");
		TotalAmountColumn tAC = (TotalAmountColumn) totalColumns.getColumn("Actual Commitments");
		assertTrue(checkValues(tAC.getItems(), 196d, 375d));

		TotalAmountColumn tAD = (TotalAmountColumn) totalColumns.getColumn("Actual Disbursements");
		assertTrue(checkValues(tAD.getItems(), 392d, 450d));

		TotalAmountColumn tPC = (TotalAmountColumn) totalColumns.getColumn("Planned Commitments");
		assertTrue(checkValues(tPC.getItems(), 588d, 525d));

		TotalAmountColumn tPD = (TotalAmountColumn) totalColumns.getColumn("Planned Disbursements");
		assertTrue(checkValues(tPD.getItems(), 784d, 600d));
	}

	public void testDakarEducationHealthProgramValues() throws Exception {
		/**
		 * Test 1 500 1000 1500 2000 500 1000 1500 2000 1000 2000 3000 4000 Test
		 * 2 1750 2100 2450 2800 1750 2100 2450 2800 3500 4200 4900 5600
		 */
		GroupReportData data = (GroupReportData) generatedReport.getItems().get(0);
		Collection<GroupReportData> items = data.getItems();
		GroupReportData item;
		item = (GroupReportData) data.getItem(0);
		item = (GroupReportData) item.getItems().get(1);
		ColumnReportData colData = (ColumnReportData) item.getItems().get(0);
		System.out.println(item + " " + colData);
		GroupColumn grColumn = (GroupColumn) colData.getColumn("Funding");
		/**
		 * 168 336 504 672 168 336 504 672 336 672 1008 1344 437.5 525 612.5 700
		 * 437.5 525 612.5 700 875 1050 1225 1400
		 */
		GroupColumn gC2009 = (GroupColumn) grColumn.getColumn("2009");
		TotalAmountColumn aC2009 = (TotalAmountColumn) gC2009.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2009.getItems(), 168d, 437.5d));

		TotalAmountColumn aD2009 = (TotalAmountColumn) gC2009.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2009.getItems(), 336d, 525d));

		TotalAmountColumn pC2009 = (TotalAmountColumn) gC2009.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2009.getItems(), 504d, 612.5d));

		TotalAmountColumn pD2009 = (TotalAmountColumn) gC2009.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2009.getItems(), 672d, 700d));

		/**
		 * 168 336 504 672 336 672 1008 1344 437.5 525 612.5 700 875 1050 1225
		 * 1400
		 */

		GroupColumn gC2010 = (GroupColumn) grColumn.getColumn("2010");
		TotalAmountColumn aC2010 = (TotalAmountColumn) gC2010.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2010.getItems(), 168d, 437.5d));

		TotalAmountColumn aD2010 = (TotalAmountColumn) gC2010.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2010.getItems(), 336d, 525d));

		TotalAmountColumn pC2010 = (TotalAmountColumn) gC2010.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2010.getItems(), 504d, 612.5d));

		TotalAmountColumn pD2010 = (TotalAmountColumn) gC2010.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2010.getItems(), 672d, 700d));

		/**
		 * 336 672 1008 1344 875 1050 1225 1400
		 */
		GroupColumn totalColumns = (GroupColumn) colData.getColumn("Total Costs");
		TotalAmountColumn tAC = (TotalAmountColumn) totalColumns.getColumn("Actual Commitments");
		assertTrue(checkValues(tAC.getItems(), 336d, 875d));

		TotalAmountColumn tAD = (TotalAmountColumn) totalColumns.getColumn("Actual Disbursements");
		assertTrue(checkValues(tAD.getItems(), 672d, 1050d));

		TotalAmountColumn tPC = (TotalAmountColumn) totalColumns.getColumn("Planned Commitments");
		assertTrue(checkValues(tPC.getItems(), 1008d, 1225d));

		TotalAmountColumn tPD = (TotalAmountColumn) totalColumns.getColumn("Planned Disbursements");
		assertTrue(checkValues(tPD.getItems(), 1344d, 1400d));
	}

	public void testDakarEducationMeternityProgramValues() throws Exception {
		/**
		 * Test 1 500 1000 1500 2000 500 1000 1500 2000 1000 2000 3000 4000 Test
		 * 2 1750 2100 2450 2800 1750 2100 2450 2800 3500 4200 4900 5600
		 */
		GroupReportData data = (GroupReportData) generatedReport.getItems().get(0);
		Collection<GroupReportData> items = data.getItems();
		GroupReportData item;

		item = (GroupReportData) data.getItem(0);
		item = (GroupReportData) item.getItems().get(1);

		ColumnReportData colData = (ColumnReportData) item.getItems().get(1);
		System.out.println(item + " " + colData);
		GroupColumn grColumn = (GroupColumn) colData.getColumn("Funding");
		/**
		 * 392 784 1176 1568 437.5 525 612.5 700
		 */
		GroupColumn gC2009 = (GroupColumn) grColumn.getColumn("2009");
		TotalAmountColumn aC2009 = (TotalAmountColumn) gC2009.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2009.getItems(), 392d, 437.5d));

		TotalAmountColumn aD2009 = (TotalAmountColumn) gC2009.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2009.getItems(), 784d, 525d));

		TotalAmountColumn pC2009 = (TotalAmountColumn) gC2009.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2009.getItems(), 1176d, 612.5d));

		TotalAmountColumn pD2009 = (TotalAmountColumn) gC2009.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2009.getItems(), 1568d, 700d));

		/**
		 * 392 784 1176 1568 437.5 525 612.5 700
		 */

		GroupColumn gC2010 = (GroupColumn) grColumn.getColumn("2010");
		TotalAmountColumn aC2010 = (TotalAmountColumn) gC2010.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2010.getItems(), 392d, 437.5d));

		TotalAmountColumn aD2010 = (TotalAmountColumn) gC2010.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2010.getItems(), 784d, 525d));

		TotalAmountColumn pC2010 = (TotalAmountColumn) gC2010.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2010.getItems(), 1176d, 612.5d));

		TotalAmountColumn pD2010 = (TotalAmountColumn) gC2010.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2010.getItems(), 1568d, 700d));

		/**
		 * 784 1568 2352 3136 875 1050 1225 1400
		 */
		GroupColumn totalColumns = (GroupColumn) colData.getColumn("Total Costs");
		TotalAmountColumn tAC = (TotalAmountColumn) totalColumns.getColumn("Actual Commitments");
		assertTrue(checkValues(tAC.getItems(), 784d, 875d));

		TotalAmountColumn tAD = (TotalAmountColumn) totalColumns.getColumn("Actual Disbursements");
		assertTrue(checkValues(tAD.getItems(), 1568d, 1050d));

		TotalAmountColumn tPC = (TotalAmountColumn) totalColumns.getColumn("Planned Commitments");
		assertTrue(checkValues(tPC.getItems(), 2352d, 1225d));

		TotalAmountColumn tPD = (TotalAmountColumn) totalColumns.getColumn("Planned Disbursements");
		assertTrue(checkValues(tPD.getItems(), 3136d, 1400d));
	}

	public void testNairobigricultureHealthProgramValues() throws Exception {
		GroupReportData data = (GroupReportData) generatedReport.getItems().get(0);
		Collection<GroupReportData> items = data.getItems();
		GroupReportData item;
		item = (GroupReportData) data.getItem(1);
		item = (GroupReportData) item.getItems().get(0);

		ColumnReportData colData = (ColumnReportData) item.getItems().get(0);
		System.out.println(item + " " + colData);
		GroupColumn grColumn = (GroupColumn) colData.getColumn("Funding");

		GroupColumn gC2009 = (GroupColumn) grColumn.getColumn("2009");
		/**
		 * 18 36 54 72 562.5 675 787.5 900
		 */
		TotalAmountColumn aC2009 = (TotalAmountColumn) gC2009.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2009.getItems(), 18d, 562.5d));

		TotalAmountColumn aD2009 = (TotalAmountColumn) gC2009.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2009.getItems(), 36d, 675d));

		TotalAmountColumn pC2009 = (TotalAmountColumn) gC2009.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2009.getItems(), 54d, 787.5d));

		TotalAmountColumn pD2009 = (TotalAmountColumn) gC2009.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2009.getItems(), 72d, 900d));

		/**
		 * 18 36 54 72 562.5 675 787.5 900
		 */

		GroupColumn gC2010 = (GroupColumn) grColumn.getColumn("2010");
		TotalAmountColumn aC2010 = (TotalAmountColumn) gC2010.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2010.getItems(), 18d, 562.5d));

		TotalAmountColumn aD2010 = (TotalAmountColumn) gC2010.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2010.getItems(), 36d, 675d));

		TotalAmountColumn pC2010 = (TotalAmountColumn) gC2010.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2010.getItems(), 54d, 787.5d));

		TotalAmountColumn pD2010 = (TotalAmountColumn) gC2010.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2010.getItems(), 72d, 900d));

		GroupColumn totalColumns = (GroupColumn) colData.getColumn("Total Costs");
		/**
		 * 36 72 108 144 1125 1350 1575 1800
		 * 
		 * */
		TotalAmountColumn tAC = (TotalAmountColumn) totalColumns.getColumn("Actual Commitments");
		assertTrue(checkValues(tAC.getItems(), 36d, 1125d));

		TotalAmountColumn tAD = (TotalAmountColumn) totalColumns.getColumn("Actual Disbursements");
		assertTrue(checkValues(tAD.getItems(), 72d, 1350d));

		TotalAmountColumn tPC = (TotalAmountColumn) totalColumns.getColumn("Planned Commitments");
		assertTrue(checkValues(tPC.getItems(), 108d, 1575d));

		TotalAmountColumn tPD = (TotalAmountColumn) totalColumns.getColumn("Planned Disbursements");
		assertTrue(checkValues(tPD.getItems(), 144d, 1800d));

	}

	public void testNairobiAgricultureMeternityProgramValues() throws Exception {
		/**
		 * 42 84 126 168 562.5 675 787.5 900
		 */
		GroupReportData data = (GroupReportData) generatedReport.getItems().get(0);
		Collection<GroupReportData> items = data.getItems();
		GroupReportData item;
		item = (GroupReportData) data.getItem(1);
		item = (GroupReportData) item.getItems().get(0);

		ColumnReportData colData = (ColumnReportData) item.getItems().get(1);
		System.out.println(item + " " + colData);
		GroupColumn grColumn = (GroupColumn) colData.getColumn("Funding");

		GroupColumn gC2009 = (GroupColumn) grColumn.getColumn("2009");
		TotalAmountColumn aC2009 = (TotalAmountColumn) gC2009.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2009.getItems(), 42d, 562.5d));

		TotalAmountColumn aD2009 = (TotalAmountColumn) gC2009.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2009.getItems(), 84d, 675d));

		TotalAmountColumn pC2009 = (TotalAmountColumn) gC2009.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2009.getItems(), 126d, 787.5d));

		TotalAmountColumn pD2009 = (TotalAmountColumn) gC2009.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2009.getItems(), 168d, 900d));

		/**
		 * 42 84 126 168 562.5 675 787.5 900
		 */

		GroupColumn gC2010 = (GroupColumn) grColumn.getColumn("2010");
		TotalAmountColumn aC2010 = (TotalAmountColumn) gC2010.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2010.getItems(), 42d, 562.5d));

		TotalAmountColumn aD2010 = (TotalAmountColumn) gC2010.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2010.getItems(), 84d, 675d));

		TotalAmountColumn pC2010 = (TotalAmountColumn) gC2010.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2010.getItems(), 126d, 787.5d));

		TotalAmountColumn pD2010 = (TotalAmountColumn) gC2010.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2010.getItems(), 168d, 900d));

		/**
		 * 84 168 252 336 1125 1350 1575 1800
		 */
		GroupColumn totalColumns = (GroupColumn) colData.getColumn("Total Costs");
		TotalAmountColumn tAC = (TotalAmountColumn) totalColumns.getColumn("Actual Commitments");
		assertTrue(checkValues(tAC.getItems(), 84d, 1125d));

		TotalAmountColumn tAD = (TotalAmountColumn) totalColumns.getColumn("Actual Disbursements");
		assertTrue(checkValues(tAD.getItems(), 168d, 1350d));

		TotalAmountColumn tPC = (TotalAmountColumn) totalColumns.getColumn("Planned Commitments");
		assertTrue(checkValues(tPC.getItems(), 252d, 1575d));

		TotalAmountColumn tPD = (TotalAmountColumn) totalColumns.getColumn("Planned Disbursements");
		assertTrue(checkValues(tPD.getItems(), 336d, 1800d));
	}

	public void testNairobiEducationHealthProgramValues() throws Exception {
		/**
		 * Test 1 500 1000 1500 2000 500 1000 1500 2000 1000 2000 3000 4000 Test
		 * 2 1750 2100 2450 2800 1750 2100 2450 2800 3500 4200 4900 5600
		 */
		GroupReportData data = (GroupReportData) generatedReport.getItems().get(0);
		Collection<GroupReportData> items = data.getItems();
		GroupReportData item;
		item = (GroupReportData) data.getItem(1);
		item = (GroupReportData) item.getItems().get(1);

		ColumnReportData colData = (ColumnReportData) item.getItems().get(0);
		System.out.println(item + " " + colData);
		GroupColumn grColumn = (GroupColumn) colData.getColumn("Funding");

		/**
		 * 72 144 216 288 1312.5 1575 1837.5 2100
		 */
		GroupColumn gC2009 = (GroupColumn) grColumn.getColumn("2009");
		TotalAmountColumn aC2009 = (TotalAmountColumn) gC2009.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2009.getItems(), 72d, 1312.5d));

		TotalAmountColumn aD2009 = (TotalAmountColumn) gC2009.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2009.getItems(), 144d, 1575d));

		TotalAmountColumn pC2009 = (TotalAmountColumn) gC2009.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2009.getItems(), 216d, 1837.5d));

		TotalAmountColumn pD2009 = (TotalAmountColumn) gC2009.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2009.getItems(), 288d, 2100d));

		/**
		 * 72 144 216 288 1312.5 1575 1837.5 2100
		 */

		GroupColumn gC2010 = (GroupColumn) grColumn.getColumn("2010");
		TotalAmountColumn aC2010 = (TotalAmountColumn) gC2010.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2010.getItems(), 72d, 1312.5d));

		TotalAmountColumn aD2010 = (TotalAmountColumn) gC2010.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2010.getItems(), 144d, 1575d));

		TotalAmountColumn pC2010 = (TotalAmountColumn) gC2010.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2010.getItems(), 216d, 1837.5d));

		TotalAmountColumn pD2010 = (TotalAmountColumn) gC2010.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2010.getItems(), 288d, 2100d));

		/**
		 * 144 288 432 576 2625 3150 3675 4200
		 */
		GroupColumn totalColumns = (GroupColumn) colData.getColumn("Total Costs");
		TotalAmountColumn tAC = (TotalAmountColumn) totalColumns.getColumn("Actual Commitments");
		assertTrue(checkValues(tAC.getItems(), 144d, 2625d));

		TotalAmountColumn tAD = (TotalAmountColumn) totalColumns.getColumn("Actual Disbursements");
		assertTrue(checkValues(tAD.getItems(), 288d, 3150d));

		TotalAmountColumn tPC = (TotalAmountColumn) totalColumns.getColumn("Planned Commitments");
		assertTrue(checkValues(tPC.getItems(), 432d, 3675d));

		TotalAmountColumn tPD = (TotalAmountColumn) totalColumns.getColumn("Planned Disbursements");
		assertTrue(checkValues(tPD.getItems(), 576d, 4200d));
	}

	public void testNairobiEducationMeternityProgramValues() throws Exception {

		GroupReportData data = (GroupReportData) generatedReport.getItems().get(0);
		Collection<GroupReportData> items = data.getItems();
		GroupReportData item;
		item = (GroupReportData) data.getItem(1);
		item = (GroupReportData) item.getItems().get(1);

		ColumnReportData colData = (ColumnReportData) item.getItems().get(1);
		System.out.println(item + " " + colData);
		GroupColumn grColumn = (GroupColumn) colData.getColumn("Funding");
		/**
		 * 168 336 504 672 1312.5 1575 1837.5 2100
		 */
		GroupColumn gC2009 = (GroupColumn) grColumn.getColumn("2009");
		TotalAmountColumn aC2009 = (TotalAmountColumn) gC2009.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2009.getItems(), 168d, 1312.5d));

		TotalAmountColumn aD2009 = (TotalAmountColumn) gC2009.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2009.getItems(), 336d, 1575d));

		TotalAmountColumn pC2009 = (TotalAmountColumn) gC2009.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2009.getItems(), 504d, 1837.5d));

		TotalAmountColumn pD2009 = (TotalAmountColumn) gC2009.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2009.getItems(), 672d, 2100d));

		GroupColumn gC2010 = (GroupColumn) grColumn.getColumn("2010");
		/**
		 * 168 336 504 672 1312.5 1575 1837.5 2100
		 */
		TotalAmountColumn aC2010 = (TotalAmountColumn) gC2010.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2010.getItems(), 168d, 1312.5d));

		TotalAmountColumn aD2010 = (TotalAmountColumn) gC2010.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2010.getItems(), 336d, 1575d));

		TotalAmountColumn pC2010 = (TotalAmountColumn) gC2010.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2010.getItems(), 504d, 1837.5d));

		TotalAmountColumn pD2010 = (TotalAmountColumn) gC2010.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2010.getItems(), 672d, 2100d));

		/**
		 * 336 672 1008 1344 2625 3150 3675 4200
		 */
		GroupColumn totalColumns = (GroupColumn) colData.getColumn("Total Costs");
		TotalAmountColumn tAC = (TotalAmountColumn) totalColumns.getColumn("Actual Commitments");
		assertTrue(checkValues(tAC.getItems(), 336d, 2625d));

		TotalAmountColumn tAD = (TotalAmountColumn) totalColumns.getColumn("Actual Disbursements");
		assertTrue(checkValues(tAD.getItems(), 672d, 3150d));

		TotalAmountColumn tPC = (TotalAmountColumn) totalColumns.getColumn("Planned Commitments");
		assertTrue(checkValues(tPC.getItems(), 1008d, 3675d));

		TotalAmountColumn tPD = (TotalAmountColumn) totalColumns.getColumn("Planned Disbursements");
		assertTrue(checkValues(tPD.getItems(), 1344d, 4200d));
	}

}

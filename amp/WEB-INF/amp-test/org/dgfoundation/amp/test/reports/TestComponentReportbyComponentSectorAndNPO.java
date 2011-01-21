package org.dgfoundation.amp.test.reports;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnReportData;
import org.dgfoundation.amp.ar.GroupColumn;
import org.dgfoundation.amp.ar.GroupReportData;
import org.dgfoundation.amp.ar.TotalAmountColumn;

public class TestComponentReportbyComponentSectorAndNPO extends TestReportBase {

	public Long[] getHierarchies() throws Exception {
		return new Long[] { getColumnByName("Component Name").getColumnId(), getColumnByName("Primary Sector").getColumnId(), getColumnByName("National Planning Objectives").getColumnId() };

	}

	protected Integer getReportType() {
		return ArConstants.COMPONENT_TYPE;
	}

	public void testGrandTotalValues() throws Exception {
		assertTrue(checkValues(generatedReport.getTrailCells(), null, 12000d, 16000d, 20000d, 24000d, 12000d, 16000d, 20000d, 24000d, 24000d, 32000d, 40000d, 48000d));
	}

	public void testTechnicalAssistanceAgricultureHealthProgram() throws Exception {
//		60		120		180		240		60		120		180		240		120		240		360		480
//		750		900		1050	1200	750		900		1050	1200	1500	1800	2100	2400
		GroupReportData report = (GroupReportData) generatedReport.getItem(0);
		GroupReportData firstLevel = (GroupReportData) report.getItem(1);
		GroupReportData secondLevel = (GroupReportData) firstLevel.getItem(0);
		ColumnReportData colData = (ColumnReportData) secondLevel.getItem(0);
		System.out.println(firstLevel + " " + secondLevel+  colData);
		GroupColumn grColumn = (GroupColumn) colData.getColumn("Funding");
	
		GroupColumn gC2009 = (GroupColumn) grColumn.getColumn("2009");
		TotalAmountColumn aC2009 = (TotalAmountColumn) gC2009.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2009.getItems(), 60d,750d));

		TotalAmountColumn aD2009 = (TotalAmountColumn) gC2009.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2009.getItems(),120d, 900d));

		TotalAmountColumn pC2009 = (TotalAmountColumn) gC2009.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2009.getItems(), 180d, 1050d));

		TotalAmountColumn pD2009 = (TotalAmountColumn) gC2009.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2009.getItems(), 240d, 1200d));

		//		60		120		180		240			60		120		180		240			120		240		360		480
		//		750		900		1050	1200		750		900		1050	1200		1500	1800	2100	2400

		GroupColumn gC2010 = (GroupColumn) grColumn.getColumn("2010");

		TotalAmountColumn aC2010 = (TotalAmountColumn) gC2010.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2010.getItems(), 60d, 750d));

		TotalAmountColumn aD2010 = (TotalAmountColumn) gC2010.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2010.getItems(), 120d, 900d));

		TotalAmountColumn pC2010 = (TotalAmountColumn) gC2010.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2010.getItems(), 180d, 1050d));

		TotalAmountColumn pD2010 = (TotalAmountColumn) gC2010.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2010.getItems(), 240d, 1200d));
	
		//		60		120		180		240			60		120		180		240			120		240		360		480
		//		750		900		1050	1200		750		900		1050	1200		1500	1800	2100	2400


		GroupColumn totalColumns = (GroupColumn) colData.getColumn("Total Costs");
		TotalAmountColumn tAC = (TotalAmountColumn) totalColumns.getColumn("Actual Commitments");
		assertTrue(checkValues(tAC.getItems(), 120d, 1500d));

		TotalAmountColumn tAD = (TotalAmountColumn) totalColumns.getColumn("Actual Disbursements");
		assertTrue(checkValues(tAD.getItems(), 240d, 1800d));

		TotalAmountColumn tPC = (TotalAmountColumn) totalColumns.getColumn("Planned Commitments");
		assertTrue(checkValues(tPC.getItems(), 360d, 2100d));

		TotalAmountColumn tPD = (TotalAmountColumn) totalColumns.getColumn("Planned Disbursements");
		assertTrue(checkValues(tPD.getItems(), 480d, 2400d));
	}

	public void testTechnicalAssistanceAgricultureMaternityProgram() throws Exception {
//		140		280		420		560	 	140		280		420		560		280		560		840		1120
//		750		900		1050	1200 	750		900		1050	1200	1500	1800	2100	2400
		GroupReportData report = (GroupReportData) generatedReport.getItem(0);
		GroupReportData firstLevel = (GroupReportData) report.getItem(1);
		GroupReportData secondLevel = (GroupReportData) firstLevel.getItem(0);
		ColumnReportData colData = (ColumnReportData) secondLevel.getItem(1);
		System.out.println(firstLevel + " " + secondLevel+  colData);
		GroupColumn grColumn = (GroupColumn) colData.getColumn("Funding");
	
		GroupColumn gC2009 = (GroupColumn) grColumn.getColumn("2009");
		TotalAmountColumn aC2009 = (TotalAmountColumn) gC2009.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2009.getItems(), 140d,750d));

		TotalAmountColumn aD2009 = (TotalAmountColumn) gC2009.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2009.getItems(),280d, 900d));

		TotalAmountColumn pC2009 = (TotalAmountColumn) gC2009.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2009.getItems(), 420d, 1050d));

		TotalAmountColumn pD2009 = (TotalAmountColumn) gC2009.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2009.getItems(), 560d, 1200d));

		GroupColumn gC2010 = (GroupColumn) grColumn.getColumn("2010");

		TotalAmountColumn aC2010 = (TotalAmountColumn) gC2010.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2010.getItems(), 140d, 750d));

		TotalAmountColumn aD2010 = (TotalAmountColumn) gC2010.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2010.getItems(), 280d, 900d));

		TotalAmountColumn pC2010 = (TotalAmountColumn) gC2010.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2010.getItems(), 420d, 1050d));

		TotalAmountColumn pD2010 = (TotalAmountColumn) gC2010.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2010.getItems(), 560d, 1200d));

		GroupColumn totalColumns = (GroupColumn) colData.getColumn("Total Costs");
		TotalAmountColumn tAC = (TotalAmountColumn) totalColumns.getColumn("Actual Commitments");
		assertTrue(checkValues(tAC.getItems(), 280d, 1500d));

		TotalAmountColumn tAD = (TotalAmountColumn) totalColumns.getColumn("Actual Disbursements");
		assertTrue(checkValues(tAD.getItems(), 560d, 1800d));

		TotalAmountColumn tPC = (TotalAmountColumn) totalColumns.getColumn("Planned Commitments");
		assertTrue(checkValues(tPC.getItems(), 840d, 2100d));

		TotalAmountColumn tPD = (TotalAmountColumn) totalColumns.getColumn("Planned Disbursements");
		assertTrue(checkValues(tPD.getItems(), 1120d, 2400d));

	}

	public void testTechnicalAssistanceEducationHealthProgram() throws Exception {
//		240		480		720		960		240		480		720		960		480		960		1440	1920
//		1750	2100	2450	2800	1750	2100	2450	2800	3500	4200	4900	5600

		GroupReportData report = (GroupReportData) generatedReport.getItem(0);
		GroupReportData firstLevel = (GroupReportData) report.getItem(1);
		GroupReportData secondLevel = (GroupReportData) firstLevel.getItem(1);
		ColumnReportData colData = (ColumnReportData) secondLevel.getItem(0);
		System.out.println(firstLevel + " " + secondLevel+  colData);
		GroupColumn grColumn = (GroupColumn) colData.getColumn("Funding");
	
		GroupColumn gC2009 = (GroupColumn) grColumn.getColumn("2009");
		TotalAmountColumn aC2009 = (TotalAmountColumn) gC2009.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2009.getItems(), 240d,1750d));

		TotalAmountColumn aD2009 = (TotalAmountColumn) gC2009.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2009.getItems(),480d, 2100d));

		TotalAmountColumn pC2009 = (TotalAmountColumn) gC2009.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2009.getItems(), 720d, 2450d));

		TotalAmountColumn pD2009 = (TotalAmountColumn) gC2009.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2009.getItems(), 960d, 2800d));

		GroupColumn gC2010 = (GroupColumn) grColumn.getColumn("2010");

		TotalAmountColumn aC2010 = (TotalAmountColumn) gC2010.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2010.getItems(), 240d, 1750d));

		TotalAmountColumn aD2010 = (TotalAmountColumn) gC2010.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2010.getItems(), 480d, 2100d));

		TotalAmountColumn pC2010 = (TotalAmountColumn) gC2010.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2010.getItems(), 720d, 2450d));

		TotalAmountColumn pD2010 = (TotalAmountColumn) gC2010.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2010.getItems(), 960d, 2800d));

		GroupColumn totalColumns = (GroupColumn) colData.getColumn("Total Costs");
		TotalAmountColumn tAC = (TotalAmountColumn) totalColumns.getColumn("Actual Commitments");
		assertTrue(checkValues(tAC.getItems(), 480d, 3500d));

		TotalAmountColumn tAD = (TotalAmountColumn) totalColumns.getColumn("Actual Disbursements");
		assertTrue(checkValues(tAD.getItems(), 960d, 4200d));

		TotalAmountColumn tPC = (TotalAmountColumn) totalColumns.getColumn("Planned Commitments");
		assertTrue(checkValues(tPC.getItems(), 1440d, 4900d));

		TotalAmountColumn tPD = (TotalAmountColumn) totalColumns.getColumn("Planned Disbursements");
		assertTrue(checkValues(tPD.getItems(), 1920d, 5600d));
	}

	public void testTechnicalAssistanceEducationMaternityProgram() throws Exception {
//		560		1120	1680	2240	560		1120	1680	2240	1120	2240	3360	4480
//		1750	2100	2450	2800	1750	2100	2450	2800	3500	4200	4900	5600
		GroupReportData report = (GroupReportData) generatedReport.getItem(0);
		GroupReportData firstLevel = (GroupReportData) report.getItem(1);
		GroupReportData secondLevel = (GroupReportData) firstLevel.getItem(1);
		ColumnReportData colData = (ColumnReportData) secondLevel.getItem(1);
		System.out.println(firstLevel + " " + secondLevel+  colData);
		GroupColumn grColumn = (GroupColumn) colData.getColumn("Funding");
	
		GroupColumn gC2009 = (GroupColumn) grColumn.getColumn("2009");
		TotalAmountColumn aC2009 = (TotalAmountColumn) gC2009.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2009.getItems(), 560d,1750d));

		TotalAmountColumn aD2009 = (TotalAmountColumn) gC2009.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2009.getItems(),1120d, 2100d));

		TotalAmountColumn pC2009 = (TotalAmountColumn) gC2009.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2009.getItems(), 1680d, 2450d));

		TotalAmountColumn pD2009 = (TotalAmountColumn) gC2009.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2009.getItems(), 2240d, 2800d));

		GroupColumn gC2010 = (GroupColumn) grColumn.getColumn("2010");

		TotalAmountColumn aC2010 = (TotalAmountColumn) gC2010.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2010.getItems(), 560d, 1750d));

		TotalAmountColumn aD2010 = (TotalAmountColumn) gC2010.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2010.getItems(), 1120d, 2100d));

		TotalAmountColumn pC2010 = (TotalAmountColumn) gC2010.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2010.getItems(), 1680d, 2450d));

		TotalAmountColumn pD2010 = (TotalAmountColumn) gC2010.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2010.getItems(), 2240d, 2800d));

		GroupColumn totalColumns = (GroupColumn) colData.getColumn("Total Costs");
		TotalAmountColumn tAC = (TotalAmountColumn) totalColumns.getColumn("Actual Commitments");
		assertTrue(checkValues(tAC.getItems(), 1120d, 3500d));

		TotalAmountColumn tAD = (TotalAmountColumn) totalColumns.getColumn("Actual Disbursements");
		assertTrue(checkValues(tAD.getItems(), 2240d, 4200d));

		TotalAmountColumn tPC = (TotalAmountColumn) totalColumns.getColumn("Planned Commitments");
		assertTrue(checkValues(tPC.getItems(), 3360d, 4900d));

		TotalAmountColumn tPD = (TotalAmountColumn) totalColumns.getColumn("Planned Disbursements");
		assertTrue(checkValues(tPD.getItems(), 4480d, 5600d));	
	}
///
	
	
	public void testInfrastructureSupportAgricultureHealthProgram() throws Exception {
//		60		120		180		240		60		120		180		240		120		240		360		480
//		750		900		1050	1200	750		900		1050	1200	1500	1800	2100	2400
		GroupReportData report = (GroupReportData) generatedReport.getItem(0);
		GroupReportData firstLevel = (GroupReportData) report.getItem(0);
		GroupReportData secondLevel = (GroupReportData) firstLevel.getItem(0);
		ColumnReportData colData = (ColumnReportData) secondLevel.getItem(0);
		System.out.println(firstLevel + " " + secondLevel+  colData);
		GroupColumn grColumn = (GroupColumn) colData.getColumn("Funding");
	
		GroupColumn gC2009 = (GroupColumn) grColumn.getColumn("2009");
		TotalAmountColumn aC2009 = (TotalAmountColumn) gC2009.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2009.getItems(), 60d,750d));

		TotalAmountColumn aD2009 = (TotalAmountColumn) gC2009.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2009.getItems(),120d, 900d));

		TotalAmountColumn pC2009 = (TotalAmountColumn) gC2009.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2009.getItems(), 180d, 1050d));

		TotalAmountColumn pD2009 = (TotalAmountColumn) gC2009.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2009.getItems(), 240d, 1200d));

		//		60		120		180		240			60		120		180		240			120		240		360		480
		//		750		900		1050	1200		750		900		1050	1200		1500	1800	2100	2400

		GroupColumn gC2010 = (GroupColumn) grColumn.getColumn("2010");

		TotalAmountColumn aC2010 = (TotalAmountColumn) gC2010.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2010.getItems(), 60d, 750d));

		TotalAmountColumn aD2010 = (TotalAmountColumn) gC2010.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2010.getItems(), 120d, 900d));

		TotalAmountColumn pC2010 = (TotalAmountColumn) gC2010.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2010.getItems(), 180d, 1050d));

		TotalAmountColumn pD2010 = (TotalAmountColumn) gC2010.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2010.getItems(), 240d, 1200d));
	
		//		60		120		180		240			60		120		180		240			120		240		360		480
		//		750		900		1050	1200		750		900		1050	1200		1500	1800	2100	2400


		GroupColumn totalColumns = (GroupColumn) colData.getColumn("Total Costs");
		TotalAmountColumn tAC = (TotalAmountColumn) totalColumns.getColumn("Actual Commitments");
		assertTrue(checkValues(tAC.getItems(), 120d, 1500d));

		TotalAmountColumn tAD = (TotalAmountColumn) totalColumns.getColumn("Actual Disbursements");
		assertTrue(checkValues(tAD.getItems(), 240d, 1800d));

		TotalAmountColumn tPC = (TotalAmountColumn) totalColumns.getColumn("Planned Commitments");
		assertTrue(checkValues(tPC.getItems(), 360d, 2100d));

		TotalAmountColumn tPD = (TotalAmountColumn) totalColumns.getColumn("Planned Disbursements");
		assertTrue(checkValues(tPD.getItems(), 480d, 2400d));
	}

	public void testInfrastructureSupportAgricultureMaternityProgram() throws Exception {
//		140		280		420		560	 	140		280		420		560		280		560		840		1120
//		750		900		1050	1200 	750		900		1050	1200	1500	1800	2100	2400
		GroupReportData report = (GroupReportData) generatedReport.getItem(0);
		GroupReportData firstLevel = (GroupReportData) report.getItem(0);
		GroupReportData secondLevel = (GroupReportData) firstLevel.getItem(0);
		ColumnReportData colData = (ColumnReportData) secondLevel.getItem(1);
		System.out.println(firstLevel + " " + secondLevel+  colData);
		GroupColumn grColumn = (GroupColumn) colData.getColumn("Funding");
	
		GroupColumn gC2009 = (GroupColumn) grColumn.getColumn("2009");
		TotalAmountColumn aC2009 = (TotalAmountColumn) gC2009.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2009.getItems(), 140d,750d));

		TotalAmountColumn aD2009 = (TotalAmountColumn) gC2009.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2009.getItems(),280d, 900d));

		TotalAmountColumn pC2009 = (TotalAmountColumn) gC2009.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2009.getItems(), 420d, 1050d));

		TotalAmountColumn pD2009 = (TotalAmountColumn) gC2009.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2009.getItems(), 560d, 1200d));

		GroupColumn gC2010 = (GroupColumn) grColumn.getColumn("2010");

		TotalAmountColumn aC2010 = (TotalAmountColumn) gC2010.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2010.getItems(), 140d, 750d));

		TotalAmountColumn aD2010 = (TotalAmountColumn) gC2010.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2010.getItems(), 280d, 900d));

		TotalAmountColumn pC2010 = (TotalAmountColumn) gC2010.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2010.getItems(), 420d, 1050d));

		TotalAmountColumn pD2010 = (TotalAmountColumn) gC2010.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2010.getItems(), 560d, 1200d));

		GroupColumn totalColumns = (GroupColumn) colData.getColumn("Total Costs");
		TotalAmountColumn tAC = (TotalAmountColumn) totalColumns.getColumn("Actual Commitments");
		assertTrue(checkValues(tAC.getItems(), 280d, 1500d));

		TotalAmountColumn tAD = (TotalAmountColumn) totalColumns.getColumn("Actual Disbursements");
		assertTrue(checkValues(tAD.getItems(), 560d, 1800d));

		TotalAmountColumn tPC = (TotalAmountColumn) totalColumns.getColumn("Planned Commitments");
		assertTrue(checkValues(tPC.getItems(), 840d, 2100d));

		TotalAmountColumn tPD = (TotalAmountColumn) totalColumns.getColumn("Planned Disbursements");
		assertTrue(checkValues(tPD.getItems(), 1120d, 2400d));

	}

	public void testInfrastructureSupportEducationHealthProgram() throws Exception {
//		240		480		720		960		240		480		720		960		480		960		1440	1920
//		1750	2100	2450	2800	1750	2100	2450	2800	3500	4200	4900	5600

		GroupReportData report = (GroupReportData) generatedReport.getItem(0);
		GroupReportData firstLevel = (GroupReportData) report.getItem(0);
		GroupReportData secondLevel = (GroupReportData) firstLevel.getItem(1);
		ColumnReportData colData = (ColumnReportData) secondLevel.getItem(0);
		System.out.println(firstLevel + " " + secondLevel+  colData);
		GroupColumn grColumn = (GroupColumn) colData.getColumn("Funding");
	
		GroupColumn gC2009 = (GroupColumn) grColumn.getColumn("2009");
		TotalAmountColumn aC2009 = (TotalAmountColumn) gC2009.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2009.getItems(), 240d,1750d));

		TotalAmountColumn aD2009 = (TotalAmountColumn) gC2009.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2009.getItems(),480d, 2100d));

		TotalAmountColumn pC2009 = (TotalAmountColumn) gC2009.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2009.getItems(), 720d, 2450d));

		TotalAmountColumn pD2009 = (TotalAmountColumn) gC2009.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2009.getItems(), 960d, 2800d));

		GroupColumn gC2010 = (GroupColumn) grColumn.getColumn("2010");

		TotalAmountColumn aC2010 = (TotalAmountColumn) gC2010.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2010.getItems(), 240d, 1750d));

		TotalAmountColumn aD2010 = (TotalAmountColumn) gC2010.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2010.getItems(), 480d, 2100d));

		TotalAmountColumn pC2010 = (TotalAmountColumn) gC2010.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2010.getItems(), 720d, 2450d));

		TotalAmountColumn pD2010 = (TotalAmountColumn) gC2010.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2010.getItems(), 960d, 2800d));

		GroupColumn totalColumns = (GroupColumn) colData.getColumn("Total Costs");
		TotalAmountColumn tAC = (TotalAmountColumn) totalColumns.getColumn("Actual Commitments");
		assertTrue(checkValues(tAC.getItems(), 480d, 3500d));

		TotalAmountColumn tAD = (TotalAmountColumn) totalColumns.getColumn("Actual Disbursements");
		assertTrue(checkValues(tAD.getItems(), 960d, 4200d));

		TotalAmountColumn tPC = (TotalAmountColumn) totalColumns.getColumn("Planned Commitments");
		assertTrue(checkValues(tPC.getItems(), 1440d, 4900d));

		TotalAmountColumn tPD = (TotalAmountColumn) totalColumns.getColumn("Planned Disbursements");
		assertTrue(checkValues(tPD.getItems(), 1920d, 5600d));
	}

	public void testInfrastructureSupportEducationMaternityProgram() throws Exception {
//		560		1120	1680	2240	560		1120	1680	2240	1120	2240	3360	4480
//		1750	2100	2450	2800	1750	2100	2450	2800	3500	4200	4900	5600
		GroupReportData report = (GroupReportData) generatedReport.getItem(0);
		GroupReportData firstLevel = (GroupReportData) report.getItem(0);
		GroupReportData secondLevel = (GroupReportData) firstLevel.getItem(1);
		ColumnReportData colData = (ColumnReportData) secondLevel.getItem(1);
		System.out.println(firstLevel + " " + secondLevel+  colData);
		GroupColumn grColumn = (GroupColumn) colData.getColumn("Funding");
	
		GroupColumn gC2009 = (GroupColumn) grColumn.getColumn("2009");
		TotalAmountColumn aC2009 = (TotalAmountColumn) gC2009.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2009.getItems(), 560d,1750d));

		TotalAmountColumn aD2009 = (TotalAmountColumn) gC2009.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2009.getItems(),1120d, 2100d));

		TotalAmountColumn pC2009 = (TotalAmountColumn) gC2009.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2009.getItems(), 1680d, 2450d));

		TotalAmountColumn pD2009 = (TotalAmountColumn) gC2009.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2009.getItems(), 2240d, 2800d));

		GroupColumn gC2010 = (GroupColumn) grColumn.getColumn("2010");

		TotalAmountColumn aC2010 = (TotalAmountColumn) gC2010.getColumn("Actual Commitments");
		assertTrue(checkValues(aC2010.getItems(), 560d, 1750d));

		TotalAmountColumn aD2010 = (TotalAmountColumn) gC2010.getColumn("Actual Disbursements");
		assertTrue(checkValues(aD2010.getItems(), 1120d, 2100d));

		TotalAmountColumn pC2010 = (TotalAmountColumn) gC2010.getColumn("Planned Commitments");
		assertTrue(checkValues(pC2010.getItems(), 1680d, 2450d));

		TotalAmountColumn pD2010 = (TotalAmountColumn) gC2010.getColumn("Planned Disbursements");
		assertTrue(checkValues(pD2010.getItems(), 2240d, 2800d));

		GroupColumn totalColumns = (GroupColumn) colData.getColumn("Total Costs");
		TotalAmountColumn tAC = (TotalAmountColumn) totalColumns.getColumn("Actual Commitments");
		assertTrue(checkValues(tAC.getItems(), 1120d, 3500d));

		TotalAmountColumn tAD = (TotalAmountColumn) totalColumns.getColumn("Actual Disbursements");
		assertTrue(checkValues(tAD.getItems(), 2240d, 4200d));

		TotalAmountColumn tPC = (TotalAmountColumn) totalColumns.getColumn("Planned Commitments");
		assertTrue(checkValues(tPC.getItems(), 3360d, 4900d));

		TotalAmountColumn tPD = (TotalAmountColumn) totalColumns.getColumn("Planned Disbursements");
		assertTrue(checkValues(tPD.getItems(), 4480d, 5600d));	
	}

}

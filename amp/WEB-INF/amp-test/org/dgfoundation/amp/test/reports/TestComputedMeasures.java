package org.dgfoundation.amp.test.reports;

import org.dgfoundation.amp.ar.ColumnReportData;
import org.dgfoundation.amp.ar.GroupColumn;
import org.dgfoundation.amp.ar.TotalComputedMeasureColumn;

public class TestComputedMeasures extends TestReportBase {

	@Override
	public Long[] getHierarchies() throws Exception {
		return new Long[] {};
	}

	@Override
	public Long[] getColumns() throws Exception {
		return new Long[] { getColumnByName("Project Title").getColumnId() };

	}

	@Override
	public Long[] getMeasures() throws Exception {
		return new Long[] { getMeasureByName("Execution Rate").getMeasureId(), getMeasureByName("Consumption Rate").getMeasureId(),getMeasureByName("Percentage Of Total Disbursements").getMeasureId(), getMeasureByName("Selected Year Planned Disbursements").getMeasureId()

		};

	}

	@Override
	protected void buildReport() throws Exception {
		filters.setComputedYear(2009);
		//filters.setToDate(FormatHelper.formatDate(new SimpleDateFormat("dd/MM/yyyy").parse("31/12/2009")));
		super.buildReport();
	}

	@Override
	public void testGrandTotalValues() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public void testExecutionRate() throws Exception {
		ColumnReportData report=(ColumnReportData)generatedReport.getItem(0);
		GroupColumn totalCost=(GroupColumn)report.getColumn("Total Costs");
		TotalComputedMeasureColumn col=(TotalComputedMeasureColumn)totalCost.getColumn("Execution Rate");
		checkValues(col.getItems(), 50d,75d);
	}

	public void testConsumptionRate() throws Exception {
		ColumnReportData report=(ColumnReportData)generatedReport.getItem(0);
		GroupColumn totalCost=(GroupColumn)report.getColumn("Total Costs");
		TotalComputedMeasureColumn col=(TotalComputedMeasureColumn)totalCost.getColumn("Consumption Rate");
		checkValues(col.getItems(), 50d,75d);

	}


	public void testPercentageOfTotalDisbursements() throws Exception {
		ColumnReportData report=(ColumnReportData)generatedReport.getItem(0);
		GroupColumn totalCost=(GroupColumn)report.getColumn("Total Costs");
		TotalComputedMeasureColumn col=(TotalComputedMeasureColumn)totalCost.getColumn("Percentage Of Total Disbursements");
		checkValues(col.getItems(), 100d,100d);

	}

	public void testSelectedYearPlannedDisbursements() throws Exception {
		ColumnReportData report=(ColumnReportData)generatedReport.getItem(0);
		GroupColumn totalCost=(GroupColumn)report.getColumn("Total Costs");
		TotalComputedMeasureColumn col=(TotalComputedMeasureColumn)totalCost.getColumn("Selected Year Planned Disbursements");
		checkValues(col.getItems(), 4000d, 8000d);
	
	}
	
	
}

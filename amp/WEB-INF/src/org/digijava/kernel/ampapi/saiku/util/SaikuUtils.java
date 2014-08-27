/**
 * 
 */
package org.digijava.kernel.ampapi.saiku.util;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.dgfoundation.amp.newreports.ReportSpecification;
import org.digijava.kernel.ampapi.mondrian.util.MondrianUtils;
import org.olap4j.Axis;
import org.olap4j.CellSet;
import org.olap4j.CellSetAxis;
import org.olap4j.Position;
import org.olap4j.metadata.Measure;
import org.olap4j.metadata.Member;
import org.saiku.olap.dto.resultset.AbstractBaseCell;
import org.saiku.olap.dto.resultset.CellDataSet;
import org.saiku.service.olap.totals.AxisInfo;
import org.saiku.service.olap.totals.TotalNode;
import org.saiku.service.olap.totals.TotalsListsBuilder;
import org.saiku.service.olap.totals.aggregators.TotalAggregator;
import org.saiku.web.rest.objects.resultset.Cell;
import org.saiku.web.rest.objects.resultset.Total;

/**
 * Stores any utility methods that are Saiku specific (e.g. use Saiku API)
 * @author Nadejda Mandrescu
 */
public class SaikuUtils {
	
	/**
	 * Saiku mechanism to calculate the totals
	 * @param spec
	 * @param cellDataSet
	 * @throws Exception 
	 */
	public static void doTotals(ReportSpecification spec, CellDataSet result, CellSet cellSet) throws Exception {
		/* start of AMP custom part to detect the selectedMeasures list */ 
		CellSetAxis columnAxis = cellSet.getAxes().get(Axis.COLUMNS.axisOrdinal());
		List<Measure> measures = new ArrayList<Measure>(); 
		
		for(Position colPosition : columnAxis.getPositions())
			for (Member member :  colPosition.getMembers()) {
				if (member.getMemberType().equals(Member.Type.MEASURE)
						|| member.getMemberType().equals(Member.Type.FORMULA)) {
					measures.add((Measure) member);
				} 
			}
		Measure[] selectedMeasures = measures.toArray(new Measure[0]);
		/* end of AMP custom part to detect the selectedMeasures list */
		/* start of Saiku approach to calculate the totals */
		result.setSelectedMeasures(selectedMeasures);
		int rowsIndex = 0;
		if (!cellSet.getAxes().get(0).getAxisOrdinal().equals(Axis.ROWS)) {
			rowsIndex = (rowsIndex + 1) & 1;
		}
		// TODO - refactor this using axis ordinals etc.
		final AxisInfo[] axisInfos = new AxisInfo[]{new AxisInfo(cellSet.getAxes().get(rowsIndex)), new AxisInfo(cellSet.getAxes().get((rowsIndex + 1) & 1))};
		List<TotalNode>[][] totals = new List[2][];
		TotalsListsBuilder builder = null;
		for (int index = 0; index < 2; index++) {
			final int second = (index + 1) & 1;
			TotalAggregator[] aggregators = new TotalAggregator[axisInfos[second].maxDepth + 1];
			for (int i = 1; i < aggregators.length - 1; i++) {
				String totalFunctionName = "sum";//AMP change to configure the function to 'sum' manually instead of using query.getTotalFunction(axisInfos[second].uniqueLevelNames.get(i - 1));
				aggregators[i] = TotalAggregator.newInstanceByFunctionName(totalFunctionName);
			}
			String totalFunctionName = "sum";//AMP change to configure the function to 'sum' manually instead of usingquery.getTotalFunction(axisInfos[second].axis.getAxisOrdinal().name());
			aggregators[0] = totalFunctionName != null ? TotalAggregator.newInstanceByFunctionName(totalFunctionName) : null;
			builder = new TotalsListsBuilder(selectedMeasures, aggregators, cellSet, axisInfos[index], axisInfos[second]);
			totals[index] = builder.buildTotalsLists();
		}
		result.setLeftOffset(axisInfos[0].maxDepth);
		result.setRowTotalsLists(totals[1]);
		result.setColTotalsLists(totals[0]);
		/* end of Saiku approach to calculate the totals */
	}
	
	/**
	 * Prints Saiku cellDataSet to an output
	 * @param cellDataSet
	 * @param reportName
	 */
	public static void print(CellDataSet cellDataSet, String reportName) {
		PrintWriter writer = MondrianUtils.getOutput(reportName);
		print(writer, cellDataSet.getCellSetHeaders(), null, 0);
		print(writer, cellDataSet.getCellSetBody(), cellDataSet.getRowTotalsLists(), cellDataSet.getLeftOffset());
		
		writer.write(System.lineSeparator() + "Runtime = " + cellDataSet.runtime + " ms");
		
		writer.flush();
		writer.close();
	}
	
	private static void print(PrintWriter writer, AbstractBaseCell[][] data, List<TotalNode>[] totalLists, int offset) {
		Total[][] totals = convertTotals(totalLists);
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j< data[i].length; j++ )
				writer.format("%-20s\t|", data[i][j].getFormattedValue());
			writer.write(System.lineSeparator());
			if (totals != null && totals[i] != null ) {
				for (int a = 0; a < offset; a++) {
					String value = data[i][a].getFormattedValue() + " Totals";
					writer.format("%-20s\t|", value);
				}
				for (Total total : totals[i]) {
					Cell[][] res = total.getCells();
					for (int a = 0; a < res.length; a ++) {
						for (int b = 0; b < res[a].length; b++) {
							String value = res[a][b].getValue();
							writer.format("%-20s\t|", value);
						}
						writer.write(System.lineSeparator());
					}	
				}
			}
		}
		int length = data.length > 0 ? (data[0].length * 25) : 0;
		for (int j = 0; j < length; j++ ) {
			writer.write("-");
		}
		writer.write(System.lineSeparator());
	}
	
	/**
	 * Saiku method to convert totals
	 * @param totalLists
	 * @return
	 */
	public static Total[][] convertTotals(List<TotalNode>[] totalLists) {
		if (null == totalLists)
			return null;
		Total[][] retVal = new Total[totalLists.length][];
		for (int i = 0; i < totalLists.length; i++) {
			List<TotalNode> current = totalLists [i];
			retVal[i] = new Total[current.size()];
			for (int j = 0; j < current.size(); j++)
				retVal[i][j] = new Total(current.get(j));
		}
		return retVal;
	}

}

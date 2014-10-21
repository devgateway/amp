/*
* Copyright 2012 OSBI Ltd
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.digijava.kernel.ampapi.saiku.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

import org.dgfoundation.amp.newreports.ReportSpecification;
import org.digijava.kernel.ampapi.mondrian.util.MoConstants;
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
				if (Member.Type.MEASURE.equals(member.getMemberType())) {
					measures.add((Measure) member);
				} else if (MoConstants.MEASURES.equals(member.getDimension().getName())) {
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
			String totalFunctionName = "sum";//AMP change to configure the function to 'sum' manually instead of using query.getTotalFunction(axisInfos[second].uniqueLevelNames.get(i - 1));
 			if((!spec.isCalculateColumnTotals() && index == 0) || (!spec.isCalculateRowTotals() && index==1)) { // Mimic behavior of totalFunctionName from Saiku 
				totalFunctionName = "not";
			}
			for (int i = 1; i < aggregators.length - 1; i++) {
				aggregators[i] = TotalAggregator.newInstanceByFunctionName(totalFunctionName);
			}
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
	
	public static AbstractBaseCell[][] removeCollumns(AbstractBaseCell[][] cellMatrix, SortedSet<Integer> leafColumnsNumberToRemove) {
		if (cellMatrix == null || cellMatrix.length == 0 || leafColumnsNumberToRemove.size() == 0) return cellMatrix; 
		
		AbstractBaseCell[][] newCellMatrix = new AbstractBaseCell[cellMatrix.length][cellMatrix[0].length - leafColumnsNumberToRemove.size()];
		for (int i = 0; i < cellMatrix.length; i++) 
			newCellMatrix[i] = removeCollumnsInArray(cellMatrix[i], leafColumnsNumberToRemove);
		return newCellMatrix;
	}
	
	public static <T> T[] removeCollumnsInArray(T[] cellArray, SortedSet<Integer> leafColumnsNumberToRemove) {
		if (cellArray == null || cellArray.length == 0 || leafColumnsNumberToRemove.size() == 0) return cellArray;
		
		@SuppressWarnings("unchecked")
		T[] newCellArra = (T[])Array.newInstance(cellArray.getClass().getComponentType(), cellArray.length - leafColumnsNumberToRemove.size());
		Iterator<Integer> iter = leafColumnsNumberToRemove.iterator();
		int start = 0;
		int end = iter.next(); //non-inclusive end  
		int nextEnd = iter.hasNext() ? iter.next() : cellArray.length; 
		int pos = 0;
		while (start < cellArray.length) { 
			if (start < end) {
				System.arraycopy(cellArray, start, newCellArra, pos, end - start);
				pos += end - start;
			} 
			start = end + 1;
			end = nextEnd;
			nextEnd = iter.hasNext() ? iter.next() : cellArray.length;
		}
		return newCellArra;
	}

	private void removeRowTotalsColumns(CellDataSet cellDataSet, SortedSet<Integer> leafColumnsNumberToRemove) {
		//navigate through the totals list and remember the totals only for the columns we display 
		for(List<TotalNode> totalLists : cellDataSet.getRowTotalsLists()) {
			for(TotalNode totalNode : totalLists) {
				if (totalNode.getTotalGroups() != null && totalNode.getTotalGroups().length > 0) {
					int offset = cellDataSet.getLeftOffset();
					for (int i = 0; i < totalNode.getTotalGroups().length; i++) {
						totalNode.getTotalGroups()[i] = removeCollumnsInArray(totalNode.getTotalGroups()[i], leafColumnsNumberToRemove);
					}
				}
			}
		}
	}
}

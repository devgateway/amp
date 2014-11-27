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
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.digijava.kernel.ampapi.mondrian.util.MoConstants;
import org.olap4j.Axis;
import org.olap4j.CellSet;
import org.olap4j.CellSetAxis;
import org.olap4j.Position;
import org.olap4j.metadata.Measure;
import org.olap4j.metadata.Member;
import org.saiku.olap.dto.resultset.AbstractBaseCell;
import org.saiku.olap.dto.resultset.CellDataSet;
import org.saiku.olap.dto.resultset.DataCell;
import org.saiku.olap.dto.resultset.MemberCell;
import org.saiku.olap.util.SaikuProperties;
import org.saiku.service.olap.totals.AxisInfo;
import org.saiku.service.olap.totals.TotalNode;
import org.saiku.service.olap.totals.TotalsListsBuilder;
import org.saiku.service.olap.totals.aggregators.TotalAggregator;
import org.saiku.service.util.exception.SaikuServiceException;
import org.saiku.web.rest.objects.resultset.Total;

/**
 * Stores any utility methods that are Saiku specific (e.g. use Saiku API)
 * @author Nadejda Mandrescu
 */
public class SaikuUtils {
	
	/**
	 * Saiku mechanism to calculate the totals
	 * @param cellDataSet
	 * @param onColumns - true for totals on columns
	 * @param onRows - true for totals on rows
	 * @throws Exception 
	 */
	public static void doTotals(CellDataSet result, CellSet cellSet, boolean onColumns, boolean onRows) throws Exception {
		/* start of AMP custom part to detect the selectedMeasures list */ 
		
		if (cellSet.getAxes().size() < 2 || result.getCellSetBody().length == 0 )
			return; 
		CellSetAxis columnAxis = cellSet.getAxes().get(Axis.COLUMNS.axisOrdinal());
		List<Measure> uniqueMeasures = new ArrayList<Measure>();
		/*
		// adjustments for AMP-18330 start
		Member alwaysPresent = null;
		SortedSet<Integer> totalRowsColumnsToRemove = new TreeSet<Integer>();
		int colPos = 0;
		// AMP-18330 end
		 */
		
		for(Position colPosition : columnAxis.getPositions())
			for (Member member :  colPosition.getMembers()) {
				// check if this is a measure member
				if (Member.Type.MEASURE.equals(member.getMemberType())
						|| MoConstants.MEASURES.equals(member.getDimension().getName())) {
					/*
					// avoid dummy always present added as a workaround for AMP-18330
					if (MoConstants.ALWAYS_PRESENT.equals(member.getName())) {
						totalRowsColumnsToRemove.add(colPos);
						alwaysPresent = member;
					} else
					*/ 
					if (!alreadyAdded(uniqueMeasures, member.getName())) {
						// verify if not already added (to avoid intermediate totals that break Saiku display)
						uniqueMeasures.add((Measure) member);
					}
					//colPos ++;
				}
			}
		
		Measure[] uniqueSelectedMeasures = uniqueMeasures.toArray(new Measure[0]);
		
		/* end of AMP custom part to detect the selectedMeasures list */
		/* start of Saiku approach to calculate the totals */
		result.setSelectedMeasures(uniqueSelectedMeasures);
		int rowsIndex = 0; // the following code will always keep the value to either (0) or (1)
		if (!cellSet.getAxes().get(0).getAxisOrdinal().equals(Axis.ROWS)) {
			rowsIndex = 1 - rowsIndex;
		}
					
		// TODO - refactor this using axis ordinals etc.
		final AxisInfo[] axisInfos = new AxisInfo[]{
				new AxisInfo(cellSet.getAxes().get(rowsIndex)), 
				new AxisInfo(cellSet.getAxes().get(1 - rowsIndex))};
		List<TotalNode>[][] totals = new List[2][];
		TotalsListsBuilder builder = null;
		for (int index = 0; index < 2; index++) {
			final int second = 1 - index;
			TotalAggregator[] aggregators = new TotalAggregator[axisInfos[second].maxDepth + 1];
			String totalFunctionName = "sum";//AMP change to configure the function to 'sum' manually instead of using query.getTotalFunction(axisInfos[second].uniqueLevelNames.get(i - 1));
 			if((!onColumns && index == 0) || (!onRows && index==1)) { // Mimic behavior of totalFunctionName from Saiku 
				totalFunctionName = "not";
			}
 			if(index == 1) {
				for (int i = 1; i < aggregators.length - 1; i++) {
					aggregators[i] = TotalAggregator.newInstanceByFunctionName(totalFunctionName);
				}
 			}
			aggregators[0] = totalFunctionName != null ? TotalAggregator.newInstanceByFunctionName(totalFunctionName) : null;
			builder = new TotalsListsBuilder(uniqueSelectedMeasures, aggregators, cellSet, axisInfos[index], axisInfos[second]);
			totals[index] = builder.buildTotalsLists();
		}
		result.setLeftOffset(axisInfos[0].maxDepth);
		result.setRowTotalsLists(totals[1]);
		result.setColTotalsLists(totals[0]);
		/* end of Saiku approach to calculate the totals */
		/*
		if (alwaysPresent != null) {
			removeTotalsColumns(result.getRowTotalsLists(), totalRowsColumnsToRemove);
			recalculateColumnWidths(result, totalRowsColumnsToRemove);
		}
		*/
	}
	
	public static void recalculateColumnWidths(CellDataSet result, SortedSet<Integer> columnsToRemove) {
		if (result.getColTotalsLists() == null)
			return;
		
		// update grand totals
		List<TotalNode> grandColTotals = result.getColTotalsLists()[0];
		int width = result.getWidth() - result.getLeftOffset();
		for (TotalNode totalNode : grandColTotals) {
			totalNode.setWidth(width);
			totalNode.setSpan(width);
		}
		if (result.getColTotalsLists().length < 2)
			return;
		
		// update leaf columns
		List<TotalNode> leafColTotals = result.getColTotalsLists()[result.getColTotalsLists().length - 1];
		Iterator<TotalNode> iter = leafColTotals.iterator(); 
		for (int i = 0; i < leafColTotals.size() && iter.hasNext(); i++) {
			TotalNode totalNode = iter.next();
			if (columnsToRemove.contains(i))
				iter.remove();
			else {
				int w = totalNode.getMemberCaptions() == null ? 0 : totalNode.getMemberCaptions().length;
				if (w > 0 )
					totalNode.setSpan(w);
				totalNode.setWidth(w);
			}
		}
		
		// update intermidiate totals
		for (int i = 1; i < result.getColTotalsLists().length - 1; i++) {
			List<TotalNode> totals = result.getColTotalsLists()[i];
			iter = totals.iterator();
			TotalNode current = iter.next();
			int start = 0;
			int end = current.getWidth();
			for (int colId : columnsToRemove) {
				boolean nodeFound = false;
				while (!nodeFound) {
					if (start <= colId && colId < end) {
						current.setWidth(current.getWidth() - 1);
						current.setSpan(current.getSpan() - 1);
						nodeFound = true;
					} else {
						if (iter.hasNext())
							current = iter.next();
						else
							break;
						start = end;
						end += current.getWidth();
					}
				}
			}
		}
	}

	private static boolean alreadyAdded(List<Measure> measures, String name) {
		for(Measure measure : measures) {
			if(name.equals(measure.getName())) { 
					return true;
			}
		}
		return false;
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

	public static void removeTotalsColumns(List<TotalNode>[] totalListsArray, SortedSet<Integer> leafColumnsNumberToRemove) {
		if (totalListsArray == null || totalListsArray.length == 0)
			return;
		
		//navigate through the totals list and remember the totals only for the columns we display 
		for(List<TotalNode> totalLists : totalListsArray) {
			for(TotalNode totalNode : totalLists) {
				if (totalNode.getTotalGroups() != null && totalNode.getTotalGroups().length > 0) {
					for (int i = 0; i < totalNode.getTotalGroups().length; i++) {
						totalNode.getTotalGroups()[i] = removeCollumnsInArray(totalNode.getTotalGroups()[i], leafColumnsNumberToRemove);
					}
				}
			}
		}
		
		// update
		SaikuUtils.recalculateWidths(totalListsArray);
	}
	
	public static void recalculateWidths(List<TotalNode>[] newTotalLists) {
		//Create tree of relationship between hierarchies
		List<List<List<Integer>>> topTree = new ArrayList<List<List<Integer>>>();
		for(int idx = newTotalLists.length-1; idx >= 0; idx--) {
			List<List<Integer>> tree = new ArrayList<List<Integer>>();
			List<TotalNode> nodes = newTotalLists[idx];

			if(idx != 0) {
				Integer childIndex = 0;
				for (Iterator<TotalNode> iterator = nodes.iterator(); iterator.hasNext();) {
					int parentWidth;
					List<TotalNode> parentNodes = newTotalLists[idx-1];
					for (Iterator<TotalNode> iterator2 = parentNodes.iterator(); iterator2.hasNext();) {
						List<Integer> leaf = new ArrayList<Integer>();
						TotalNode parentNode = iterator2.next();
						
						parentWidth = parentNode.getWidth();
						int totalWidth = 0;
						while(iterator.hasNext() && totalWidth < parentWidth) {
							TotalNode node = iterator.next();
							int nodeWidth = node.getWidth();
							totalWidth += nodeWidth;
							leaf.add(childIndex++);
						}
						tree.add(leaf);
					}
				}
			}
			topTree.add(tree);
		}
		
		//Use the tree to reassign widths and spans
		//First assign to the bottom of the tree, the width 1/span 1
		List<TotalNode> bottomList = newTotalLists[topTree.size()-1];
		for (int i = 0; i < bottomList.size(); i++) {
			TotalNode node = bottomList.get(i);
			node.setWidth(1);
			node.setSpan(1);
		}
		int arrayOffset = 2;
		for (int i = 0; i < topTree.size()-1; i++) {
			List<List<Integer>> tree = topTree.get(i);
			for (int j = 0; j < tree.size(); j++) {
				List<Integer> list = tree.get(j);
				int width = 0;
				for(Integer k : list) {
					width += newTotalLists[newTotalLists.length-1-i].get(k).getWidth();
				}
				int previousIndex = newTotalLists.length-arrayOffset-i;
				if(previousIndex >= 0 && newTotalLists[previousIndex].size()-1 >= j) {
					newTotalLists[previousIndex].get(j).setWidth(width);
					newTotalLists[previousIndex].get(j).setSpan(1);
				}
			}
		}
	}
	
	/**
	 * Removes the specified set of columns ids from the cellDataSet header & body. 
	 * No totals are processed (assumption no totals are generated yet - pls update if changes)
	 * 
	 * @param cellDataSet
	 * @param leafColumnsNumberToRemove
	 */
	public static void removeColumns(CellDataSet cellDataSet, SortedSet<Integer> leafColumnsNumberToRemove) {
		// update headers and body entries
		cellDataSet.setCellSetHeaders(SaikuUtils.removeCollumns(cellDataSet.getCellSetHeaders(), leafColumnsNumberToRemove));
		cellDataSet.setCellSetBody(SaikuUtils.removeCollumns(cellDataSet.getCellSetBody(), leafColumnsNumberToRemove));
		cellDataSet.setWidth(cellDataSet.getWidth() - leafColumnsNumberToRemove.size());
	}
	
	public static void updateCoordinates(CellDataSet cellDataSet) {
		for (AbstractBaseCell[] newCellMatrix : cellDataSet.getCellSetBody())
		for (int j = 0; j< newCellMatrix.length; j++)
			if (newCellMatrix[j] instanceof DataCell) {
				DataCell dataCell = (DataCell) newCellMatrix[j];
				List<Integer> coordinates = dataCell.getCoordinates();
				coordinates.set(0, j-1);
				dataCell.setCoordinates(coordinates);
			}
	}

	/**
	 * Cleans up header traces when no data is available.
	 * AMP-18748
	 * 
	 * Note: another approach to avoid traces is to use "NON EMPTY" in MDX ON ROWS
	 * but at the moment it breaks the solution for AMP-18504.
	 * Even if later we'll decide to use another solution for AMP-18504,
	 * this part shall not do anything bad.
	 *  
	 * @param cellDataSet
	 */
	public static void cleanupTraceHeadersIfNoData(CellDataSet cellDataSet) {
		// When no data is available, then for some reason Saiku CellDataSet has no headers,
		// and trace headers are stored in the cellSetBody, that breaks things up.
		// => removing this header traces from cellSetBody
		if (cellDataSet.getCellSetHeaders().length == 0) {
			cellDataSet.setCellSetBody(new AbstractBaseCell[0][0]);
			cellDataSet.setHeight(0);
			cellDataSet.setWidth(0);
		}
	}
	
	
	//Export taken directly from class org.saiku.service.util.export.CsvExporter because it's private
	public static byte[] getCsv( CellDataSet table, String delimiter, String enclosing ) {
	    if ( table != null ) {
	      AbstractBaseCell[][] rowData = table.getCellSetBody();
	      AbstractBaseCell[][] rowHeader = table.getCellSetHeaders();


	      boolean offset = rowHeader.length > 0;
	      String[][] result = new String[ ( offset ? 1 : 0 ) + rowData.length ][];
	      if ( offset ) {
	        List<String> cols = new ArrayList<String>();
	        for ( int x = 0; x < rowHeader[ 0 ].length; x++ ) {
	          String col = null;
	          for ( int y = rowHeader.length - 1; y >= 0; y-- ) {
	            String value = rowHeader[ y ][ x ].getFormattedValue();
	            if ( value == null || "null".equals( value ) )  //$NON-NLS-1$
	            {
	              value = ""; //$NON-NLS-1$
	            }
	            if ( col == null && StringUtils.isNotBlank( value ) ) {
	              col = value;
	            } else if ( col != null && StringUtils.isNotBlank( value ) ) {
	              col = value + "/" + col;
	            }
	          }
	          cols.add( enclosing + col + enclosing );
	        }
	        result[ 0 ] = cols.toArray( new String[ cols.size() ] );
	      }
	      String[] lastKnownHeader = null;
	      for ( int x = 0; x < rowData.length; x++ ) {
	        int xTarget = ( offset ? 1 : 0 ) + x;
	        if ( lastKnownHeader == null ) {
	          lastKnownHeader = new String[ rowData[ x ].length ];
	        }
	        List<String> cols = new ArrayList<String>();
	        for ( int y = 0; y < rowData[ x ].length; y++ ) {
	          String value = rowData[ x ][ y ].getFormattedValue();
	          if ( !SaikuProperties.webExportCsvUseFormattedValue ) {
	            if ( rowData[ x ][ y ] instanceof DataCell && ( (DataCell) rowData[ x ][ y ] ).getRawNumber() != null ) {
	              value = ( (DataCell) rowData[ x ][ y ] ).getRawNumber().toString();
	            }
	          }
	          if ( rowData[ x ][ y ] instanceof MemberCell && StringUtils.isNotBlank( value ) && !"null".equals( value ) ) {
	            lastKnownHeader[ y ] = value;
	          } else if ( rowData[ x ][ y ] instanceof MemberCell && ( StringUtils.isBlank( value ) || "null"
	            .equals( value ) ) ) {
	            value = ( StringUtils.isNotBlank( lastKnownHeader[ y ] ) ? lastKnownHeader[ y ] : null );
	          }

	          if ( value == null || "null".equals( value ) ) {
	            value = "";
	          }
	          value = value.replace( "\"", "\"\"" );
	          value = enclosing + value + enclosing;
	          cols.add( value );
	        }
	        result[ xTarget ] = cols.toArray( new String[ cols.size() ] );

	      }
	      return export( result, delimiter );
	    }
	    return new byte[ 0 ];
	  }
	  private static byte[] export( String[][] resultSet, String delimiter ) {
		    try {
		      String output = "";
		      StringBuffer buf = new StringBuffer();
		      if ( resultSet.length > 0 ) {
		        for ( int i = 0; i < resultSet.length; i++ ) {
		          String[] vs = resultSet[ i ];

		          for ( int j = 0; j < vs.length; j++ ) {
		            String value = vs[ j ];

		            if ( j > 0 ) {
		              buf.append( delimiter + value );
		              //output += delimiter + value;
		            } else {
		              buf.append( value );
		              //output += value;
		            }
		          }
		          buf.append( "\r\n" );
		          //output += "\r\n"; //$NON-NLS-1$
		        }
		        output = buf.toString();
		        return output.getBytes( SaikuProperties.webExportCsvTextEncoding ); //$NON-NLS-1$
		      }
		    } catch ( Throwable e ) {
		      throw new SaikuServiceException( "Error creating csv export for query" ); //$NON-NLS-1$
		    }
		    return new byte[ 0 ];
		  }


}

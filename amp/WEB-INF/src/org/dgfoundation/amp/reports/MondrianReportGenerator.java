/**
 * 
 */
package org.dgfoundation.amp.reports;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.newreports.AmountCell;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportExecutor;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.TextCell;
import org.digijava.kernel.ampapi.exception.AmpApiException;
import org.digijava.kernel.ampapi.mondrian.queries.MDXGenerator;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXAttribute;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXConfig;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXMeasure;
import org.digijava.kernel.ampapi.mondrian.util.MoConstants;
import org.digijava.kernel.ampapi.mondrian.util.MondrianMaping;
import org.digijava.kernel.ampapi.mondrian.util.MondrianUtils;
import org.olap4j.Axis;
import org.olap4j.Cell;
import org.olap4j.CellSet;
import org.olap4j.CellSetAxis;
import org.olap4j.OlapException;
import org.olap4j.Position;
import org.olap4j.metadata.Member;

/**
 * Generates a report via Mondrian
 * @author Nadejda Mandrescu
 *
 */
public class MondrianReportGenerator implements ReportExecutor {
	protected static final Logger logger = Logger.getLogger(MondrianReportGenerator.class);
	
	public MondrianReportGenerator() {
	}
	
	@Override
	public GeneratedReport executeReport(ReportSpecification spec, Class<? extends ReportArea> reportAreaType) throws AMPException {
		MDXConfig config = new MDXConfig();
		config.setCubeName(MoConstants.DEFAULT_CUBE_NAME);
		config.setMdxName(spec.getReportName());
		//add requested columns
		for (ReportColumn col:spec.getColumns()) {
			MDXAttribute elem = (MDXAttribute)MondrianMaping.toMDXElement(col);
			config.addRowAttribute(elem);
		}
		//add requested measures
		for (ReportMeasure measure: spec.getMeasures()) {
			MDXMeasure elem = (MDXMeasure)MondrianMaping.toMDXElement(measure);
			config.addColumnMeasure(elem);
		}
		//TODO: configure filters
		//TODO: add dates
		//TODO: sorting - do it afterwards?
		
		CellSet cellSet = null;
		long startTime = 0;
		try {
			MDXGenerator mdxGen = new MDXGenerator();
			startTime = System.currentTimeMillis();
			String mdxQuery = mdxGen.getAdvancedOlapQuery(config);
			cellSet = mdxGen.runQuery(mdxQuery);
		} catch (AmpApiException e) {
			throw new AMPException("Cannot generate Mondrian Report: " + e.getMessage());
		}
		 
		return toGeneratedReport(spec, cellSet, reportAreaType, (int)(System.currentTimeMillis() - startTime));
	}
	
	private GeneratedReport toGeneratedReport(ReportSpecification spec, CellSet cellSet, Class<? extends ReportArea> reportAreaType, int duration) throws AMPException {
		CellSetAxis rowAxis = cellSet.getAxes().get(Axis.ROWS.axisOrdinal());
		CellSetAxis columnAxis = cellSet.getAxes().get(Axis.COLUMNS.axisOrdinal());
		ReportArea root = getNewReportArea(reportAreaType);
		List<ReportOutputColumn> leafHeaders = null; //leaf report columns list
		
		if (rowAxis.getPositionCount() > 0 && columnAxis.getPositionCount() > 0 ) {
			leafHeaders = getOrderedLeafColumnsList(rowAxis, columnAxis); 
			
			/* Build Report Areas */
			// stack of current group of children
			Deque<List<ReportArea>> stack = new ArrayDeque<List<ReportArea>>();
			int maxStackSize = rowAxis.getAxisMetaData().getHierarchies().size();
			refillStack(stack, maxStackSize); //prepare the stack
			
			int cellOrdinal = 0; //initial position of row data from the cellSet
			boolean wasAreaEnd = false; 
			
			for (Position rowPos : rowAxis.getPositions()) {
				int columnPos = 0;
				boolean areaEnd = false; 
				ReportArea reportArea = getNewReportArea(reportAreaType);
				Map<ReportOutputColumn, ReportCell> contents = new LinkedHashMap<ReportOutputColumn, ReportCell>();
				
				for (Member member : rowPos.getMembers()) {
					TextCell text = new TextCell(member.getName());
					contents.put(leafHeaders.get(columnPos++), text);
					areaEnd = areaEnd || member.isAll();
				}
				
				for (Position colPos : columnAxis.getPositions()) {
					Cell cell = cellSet.getCell(cellOrdinal++);
					if (cell.getValue() instanceof OlapException) {
						logger.error("Unexpected cell error: " + MondrianUtils.getOlapExceptionMessage((OlapException)cell.getValue()));
					} else {
						//TODO: do we need to store empty values?
						AmountCell amount = new AmountCell(new BigDecimal(cell.getValue().toString()));
						contents.put(leafHeaders.get(columnPos++), amount);
					} 
				}
				
				reportArea.setContents(contents);
				
				if (areaEnd) {
					reportArea.setChildren(stack.pop());
				} else if (wasAreaEnd) { 
					//was an area end but now is simple content => refill with new children lists to the stack 
					refillStack(stack, maxStackSize);
				}
				stack.peek().add(reportArea);
				wasAreaEnd = areaEnd;
			}
			root.setChildren(stack.pop());
			/*
			ReportArea header = root.getClass().newInstance();
			for (int i = 0; i < measuresHierarchies; i++) {
				//first row axis
				if (i + 1 == measuresHierarchies) {
					for(Position rowPosition : rowsAxis.getPositions().get(0)) {
						ReportOutputColumn textColumn = new ReportOutputColumn(rowPosition.getClass())
					}
				} else {
					ReportOutputColumn emptyHeader
				}
				
				
			}*/
		}
			
		//we should have requesting user already configure in spec - spec must have all required data
		GeneratedReport genRep = new GeneratedReport(spec, duration, null, root, getRootHeaders(leafHeaders), leafHeaders); 
		return genRep;
	}
	
	private List<ReportOutputColumn> getOrderedLeafColumnsList(CellSetAxis rowAxis, CellSetAxis columnAxis) {
		//<fully qualified column name, ReportOutputColumn instance>, where fully qualified means with all parents name: /root/root-child/root-grandchild/...
		Map<String, ReportOutputColumn> reportColumnsByFullName = new LinkedHashMap<String,ReportOutputColumn>();
		List<ReportOutputColumn> reportColumns = new ArrayList<ReportOutputColumn>(); //leaf report columns list

		//build the list of available columns
		for (Member textColumn : rowAxis.getPositions().get(0).getMembers()) {
			ReportOutputColumn reportColumn = new ReportOutputColumn(textColumn.getLevel().getName(), null);
			reportColumns.add(reportColumn);
		}
		int measuresLeafPos = columnAxis.getAxisMetaData().getHierarchies().size()-1;
		for (Position position : columnAxis.getPositions()) {
			String fullColumnName = "";
			for (Member measureColumn : position.getMembers()) {
				ReportOutputColumn parent = reportColumnsByFullName.get(fullColumnName);
				fullColumnName += "/" +  measureColumn.getName();
				ReportOutputColumn reportColumn = reportColumnsByFullName.get(fullColumnName);
				if (reportColumn == null) {
					reportColumn = new ReportOutputColumn(measureColumn.getName(), parent);
					reportColumnsByFullName.put(fullColumnName, reportColumn);
				}
				if (measureColumn.getDepth() == measuresLeafPos) {
					reportColumns.add(reportColumn);
				}
			}
		}
		
		return reportColumns;
	}
	
	private void refillStack(Deque<List<ReportArea>> stack, int maxSize) {
		for (int i = stack.size(); i < maxSize; i++) {
			stack.push(new ArrayList<ReportArea>()); 
		}
	}
	
	private List<ReportOutputColumn> getRootHeaders(List<ReportOutputColumn> leafHeaders) {
		Set<ReportOutputColumn> rootHeaders = new LinkedHashSet<ReportOutputColumn>();
		for (ReportOutputColumn leaf : leafHeaders) {
			while(leaf.parentColumn != null) {
				leaf = leaf.parentColumn;
			}
			rootHeaders.add(leaf);
		}
		return Arrays.asList(rootHeaders.toArray(new ReportOutputColumn[1]));
	}
	
	private ReportArea getNewReportArea(Class<? extends ReportArea> reportAreaType) throws AMPException {
		ReportArea reportArea = null;
		try {
			reportArea = reportAreaType.newInstance();
		} catch(Exception e) {
			throw new AMPException("Cannot instantiate " + reportAreaType.getName() + ". " + e.getMessage());
		}
		return reportArea;
	}
}

	
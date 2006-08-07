/**
 * AmpReportGenerator.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.exception.IncompatibleColumnException;
import org.dgfoundation.amp.ar.exception.UnidentifiedItemException;
import org.dgfoundation.amp.ar.workers.ColumnWorker;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.aim.dbentity.AmpReportColumn;
import org.digijava.module.aim.dbentity.AmpReportHierarchy;
import org.digijava.module.aim.dbentity.AmpReports;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jun 12, 2006
 * 
 */
public class AmpReportGenerator extends ReportGenerator {

	protected AmpReports reportMetadata;

	/**
	 * returns categories for a given column. If the column has no categories, the returned list will hold 0 elements.
	 * this method uses the reportMetadata object (AmpReport type) to compile the needed categories for the given report
	 * 
	 * @param columnName the name of the column for which the categories are being requested
	 * @return the list of categories
	 */
	public List getColumnSubCategories(String columnName) {
		List ret = new ArrayList();
		if ("Funding".equals(columnName)) {
			boolean annual = true;
			if ("Q".equals(reportMetadata.getOptions()))
				annual = false;
			ret.add("Year");
			if (!annual)
				ret.add("Quarter");
			ret.add("Funding Type");
			
			Iterator i=reportMetadata.getColumns().iterator();
			while (i.hasNext()) {
				AmpReportColumn element = (AmpReportColumn) i.next();
				if(element.getColumn().getColumnName().equals("Type Of Assistance")) {
					ret.add("Terms of Assistance");
					break;
				}
			}
		}
		return ret;
	}

	/**
	 * retrieves columns from the database. Only the columns that are specified in the reportMetadata are going to be retrieved.
	 * Each column is retrieved separately. Some columns need to be generated after the other cols are retrieved. 
	 *  
	 */
	protected void retrieveData() {
		// divide the column set into those that can be extracted
		// (extractorView!=null) and those that are to be generated
		List extractable = new ArrayList();
		List generated = new ArrayList();
		List colNames = reportMetadata.getOrderedColumns();
		Iterator i = colNames.iterator();
		while (i.hasNext()) {
			AmpColumns element = (AmpColumns) i.next();
			if (element.getExtractorView() != null)
				extractable.add(element);
			else
				generated.add(element);
		}

		//also add hierarchical columns to extractable:
		i=reportMetadata.getHierarchies().iterator();
		while (i.hasNext()) {
			AmpReportHierarchy element = (AmpReportHierarchy) i.next();
			extractable.add(element.getColumn());	
		}
		
		createDataForColumns(extractable);
		createDataForColumns(generated);
	}

	/**
	 * creates the data structures for the list of columns. 
	 * @param colNames
	 */
	protected void createDataForColumns(List colNames) {

		Iterator i = colNames.iterator();
		try {

			while (i.hasNext()) {
				AmpColumns col = (AmpColumns) i.next();
				String cellTypeName = col.getCellType();
				String extractorView = col.getExtractorView();
				String columnName = col.getColumnName();

				Class cellType = Class.forName(cellTypeName);

				// create an instance of the cell that is of type described
				Constructor cellc = cellType.getConstructors()[0];
				Cell cell = (Cell) cellc.newInstance(new Object[] {});

				// column worker is a class returned by getWorker method of each
				// Cell
				Class ceClass = cell.getWorker();

				// create an instance of the column worker

				ColumnWorker ce = null;
				if (extractorView != null) {

					Constructor ceCons = ceClass.getConstructors()[0];
					ce = (ColumnWorker) ceCons.newInstance(new Object[] {
							filter.getGeneratedFilterQuery(), extractorView, columnName });
				} else {
					Constructor ceCons = ceClass.getConstructors()[0];
					ce = (ColumnWorker) ceCons.newInstance(new Object[] {
							columnName, rawColumns });

				}

				Column column = ce.populateCellColumn();
				rawColumns.addColumn(column);
			}

		} catch (ClassNotFoundException e) {
			logger.error(e);
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			logger.error(e);
			e.printStackTrace();
		} catch (InstantiationException e) {
			logger.error(e);
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			logger.error(e);
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			logger.error(e);
			e.printStackTrace();
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}

	}

	protected void attachFundingMeta() {
		AmpColumns ac = new AmpColumns();
		ac.setCellType("org.dgfoundation.amp.ar.cell.CategAmountCell");
		ac.setColumnName("Funding");
		if (reportMetadata.getType().intValue() == 1)
			ac.setExtractorView("v_donor_funding");
		if (reportMetadata.getType().intValue() == 2)
			ac.setExtractorView("v_component_funding");
		if (reportMetadata.getType().intValue() == 3)
			ac.setExtractorView("v_regional_funding");

		reportMetadata.getOrderedColumns().add(ac);
		
	}

	/**
	 * generating the total columns of the report. createTotals is invoked after the data has been retrieved
	 * and is the first substep of processing.
	 *
	 */
	protected void createTotals() {
		List cats = new ArrayList();

		// we perform totals by categorizing only by Funding Type...
		cats.add(new String("Funding Type"));

		// get the funding column
		AmountCellColumn funding = (AmountCellColumn) rawColumns.getColumn("Funding");

		Column newcol = GroupColumn.verticalSplitByCategs(funding, cats, true);
		
		newcol.setName("Total");

		rawColumns.addColumn(newcol);
	}

	protected void prepareData() {
		createTotals();

		categorizeData();
		
		report = new GroupReportData(reportMetadata.getName());
		report.setSourceColsCount(new Integer(reportMetadata.getColumns().size()));
		
		ColumnReportData reportChild = new ColumnReportData(reportMetadata.getName());
		reportChild.addColumns(rawColumns.getItems());
		report.addReport(reportChild);
		// find out if this is a hierarchical report or not:
		if (reportMetadata.getHierarchies().size() != 0) 
		createHierarchies();

		// perform postprocessing - cell grouping and other tasks
		report.postProcess();

	}
 
	/**
	 * creates Horizontal categories (hierarchies). The column list from
	 * report's metadata is iterated and each column acts as the drivinpg filter
	 * for the split.
	 * 
	 */
	protected void createHierarchies() {
		Iterator i = reportMetadata.getHierarchies().iterator();
		while (i.hasNext()) {
			AmpReportHierarchy element = (AmpReportHierarchy) i.next();
			// TODO: the set is NOT a list, so the hierarchies are unordered.
			AmpColumns c = element.getColumn();
			try {
				report = report.horizSplitByCateg(c.getColumnName());
			} catch (UnidentifiedItemException e) {
				logger.error(e);
				e.printStackTrace();
			} catch (IncompatibleColumnException e) {
				logger.error(e);
				e.printStackTrace();
			}

		}
	}

	/**
	 * split categorizable columns into subcolumns based on metadata (categories).
	 */
	protected void categorizeData() {
		Iterator i = reportMetadata.getOrderedColumns().iterator();
		while (i.hasNext()) {
			AmpColumns element = (AmpColumns) i.next();
			String colName = element.getColumnName();
			List cats = getColumnSubCategories(element
					.getColumnName());
			CellColumn src = (CellColumn) rawColumns.getColumn(colName);
			if (cats.size() != 0) {
				Column newcol = GroupColumn.verticalSplitByCategs(src, cats,
						true);
				rawColumns.replaceColumn(colName, newcol);
			}
		}
	}

	/**
	 * @param reportMetadata
	 * @param condition
	 */
	public AmpReportGenerator(AmpReports reportMetadata, AmpNewFilter filter) {
		super();
		this.reportMetadata = reportMetadata;
		rawColumns = new GroupColumn();
		this.filter=filter;
		
		reportMetadata.createOrderedColumns();
		
		attachFundingMeta();
	}

}
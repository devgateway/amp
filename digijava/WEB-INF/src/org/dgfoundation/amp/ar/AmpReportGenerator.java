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

import org.dgfoundation.amp.ar.cell.AmountCell;
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
	protected int extractableCount;

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
			AmpReportColumn element2 = (AmpReportColumn) i.next();
			AmpColumns element=element2.getColumn();
			if (element.getExtractorView() != null)
				extractable.add(element2);
			else
				generated.add(element2);
		}
		
		extractableCount=extractable.size();

		//also add hierarchical columns to extractable:
		i=reportMetadata.getHierarchies().iterator();
		while (i.hasNext()) {
			AmpReportHierarchy element = (AmpReportHierarchy) i.next();
			AmpReportColumn arc=new AmpReportColumn();
			arc.setColumn(element.getColumn());
			arc.setOrderId(new String("1"));
			extractable.add(arc);	
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
				AmpReportColumn rcol = (AmpReportColumn) i.next();
				AmpColumns col=rcol.getColumn();
				String cellTypeName = col.getCellType();
				String extractorView = col.getExtractorView();
				String columnName = col.getColumnName();

				logger.debug("Seeking class "+cellTypeName);
				
				Class cellType = Class.forName(cellTypeName);
				
				
				Constructor cellc=ARUtil.getConstrByParamNo(cellType,0);
				
				// create an instance of the cell that is of type described
				Cell cell = (Cell) cellc.newInstance(new Object[] {});

				// column worker is a class returned by getWorker method of each
				// Cell
				Class ceClass = cell.getWorker();

				// create an instance of the column worker

				ColumnWorker ce = null;
				if (extractorView != null) {

					Constructor ceCons = ARUtil.getConstrByParamNo(ceClass,4);
					ce = (ColumnWorker) ceCons.newInstance(new Object[] {
							filter.getGeneratedFilterQuery(), extractorView, columnName,this });
				} else {
					Constructor ceCons = ARUtil.getConstrByParamNo(ceClass,3);
					ce = (ColumnWorker) ceCons.newInstance(new Object[] {
							columnName, rawColumns,this});
				}

				Column column = ce.populateCellColumn();
				rawColumns.addColumn(new Integer(rcol.getOrderId()),column);
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
		
		//generate on-the-fly a customized AmpReportColumn - we need it for later steps
		AmpReportColumn arc=new AmpReportColumn();
		AmpColumns ac = new AmpColumns();
		arc.setColumn(ac);
		arc.setOrderId(new String("0"));
		ac.setCellType("org.dgfoundation.amp.ar.cell.CategAmountCell");
		ac.setColumnName("Funding");
		if (reportMetadata.getType().intValue() == 1)
			ac.setExtractorView("v_donor_funding");
		if (reportMetadata.getType().intValue() == 2)
			ac.setExtractorView("v_component_funding");
		if (reportMetadata.getType().intValue() == 3)
			ac.setExtractorView("v_regional_funding");

		reportMetadata.getOrderedColumns().add(arc);
		
		//ugly but useful :) get cummulative columns right before funding:
		Iterator i=reportMetadata.getOrderedColumns().iterator();
		while (i.hasNext()) {
			AmpReportColumn element = (AmpReportColumn) i.next();
			if(element.getColumn().getColumnName().indexOf("Cumulative")!=-1) 
				element.setOrderId(Integer.toString(reportMetadata.getOrderedColumns().size()-1));
		}
		
		
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
		
		
		
		//we create the cummulative balance (undisbursed) = commitment - disbursement
		//iterate each owner
		
		TotalAmountColumn tac=new TotalAmountColumn("Undisbursed Balance");
		Iterator i=newcol.getOwnerIds().iterator();
		while (i.hasNext()) {
			Long ownerId=(Long)i.next();
			AmountCell acCumul=new AmountCell(ownerId);
			acCumul.setId(ownerId);
			//get each other total column
			Iterator icol=newcol.getItems().iterator();
			while (icol.hasNext()) {
				CellColumn cellCol = (CellColumn) icol.next();
				AmountCell cac=(AmountCell) cellCol.getByOwner(ownerId);
				if(cac==null) continue;
				if(cellCol.getName().indexOf("Actual Commitment")!=-1) acCumul.rawAdd(cac.getAmount());
				if(cellCol.getName().indexOf("Actual Disbursement")!=-1) acCumul.rawAdd(-cac.getAmount());
			}
			tac.addCell(acCumul);
		}
		
		newcol.getItems().add(tac);
		
		newcol.setName("Total");

		rawColumns.addColumn(newcol);
	}

	
	protected void applyExchangeRate() {
		
	}
	
	protected void prepareData() {
		
		applyExchangeRate();
		
		createTotals();

		categorizeData();
		
		report = new GroupReportData(reportMetadata.getName());
		report.setSourceColsCount(new Integer(extractableCount-1));
		
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
		List orderedHierarchies=ARUtil.createOrderedHierarchies(reportMetadata.getHierarchies());
		Iterator i = orderedHierarchies.iterator();
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
			AmpColumns element = ((AmpReportColumn) i.next()).getColumn();
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
	public AmpReportGenerator(AmpReports reportMetadata, AmpARFilter filter) {
		super();
		this.reportMetadata = reportMetadata;
		rawColumns = new GroupColumn();
		this.filter=filter;
		
		logger.info("Master report query:"+filter.getGeneratedFilterQuery());
		
		reportMetadata.setOrderedColumns(ARUtil.createOrderedColumns(reportMetadata.getColumns()));
		
		attachFundingMeta();
	}

}
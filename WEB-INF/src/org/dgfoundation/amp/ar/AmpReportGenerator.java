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
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.dgfoundation.amp.ar.cell.AmountCell;
import org.dgfoundation.amp.ar.cell.CategAmountCell;
import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.cell.TextCell;
import org.dgfoundation.amp.ar.dimension.ARDimensionable;
import org.dgfoundation.amp.ar.exception.IncompatibleColumnException;
import org.dgfoundation.amp.ar.exception.UnidentifiedItemException;
import org.dgfoundation.amp.ar.workers.ColumnWorker;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.aim.dbentity.AmpColumnsFilters;
import org.digijava.module.aim.dbentity.AmpMeasures;
import org.digijava.module.aim.dbentity.AmpReportColumn;
import org.digijava.module.aim.dbentity.AmpReportHierarchy;
import org.digijava.module.aim.dbentity.AmpReportMeasures;
import org.digijava.module.aim.dbentity.AmpReports;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jun 12, 2006
 * 
 */
public class AmpReportGenerator extends ReportGenerator {

	List<AmpReportColumn> extractable;
	protected int extractableCount;
	private List<String> columnsToBeRemoved;
	
	
	

	/**
	 * returns categories for a given column. If the column has no categories,
	 * the returned list will hold 0 elements. this method uses the
	 * reportMetadata object (AmpReport type) to compile the needed categories
	 * for the given report
	 * 
	 * @param columnName
	 *            the name of the column for which the categories are being
	 *            requested
	 * @return the list of categories
	 */
	public List<String> getColumnSubCategories(String columnName) {
		List<String> ret = new ArrayList<String>();
		if (ArConstants.COLUMN_FUNDING.equals(columnName)) {
			boolean annual = true;
			boolean monthly = false;
			if ("Q".equals(reportMetadata.getOptions()))
				annual = false;

			if ("M".equals(reportMetadata.getOptions()))
				monthly = true;

			ret.add(ArConstants.YEAR);
			if (!annual)
				ret.add(ArConstants.QUARTER);
			if (monthly)
				ret.add(ArConstants.MONTH);

			if (reportMetadata.getType().intValue() != ArConstants.CONTRIBUTION_TYPE)
				ret.add(ArConstants.FUNDING_TYPE);
			else {
				ret.add(ArConstants.FINANCING_INSTRUMENT);
			}
			Iterator i = reportMetadata.getColumns().iterator();
			while (i.hasNext()) {
				AmpReportColumn element = (AmpReportColumn) i.next();
				if (element.getColumn().getColumnName().equals(
						"Type Of Assistance")) {
					ret.add(ArConstants.TERMS_OF_ASSISTANCE);
					break;
				}
			}

		}
		return ret;
	}

	/**
	 * retrieves columns from the database. Only the columns that are specified
	 * in the reportMetadata are going to be retrieved. Each column is retrieved
	 * separately. Some columns need to be generated after the other cols are
	 * retrieved.
	 * 
	 */
	protected void retrieveData() {
		// divide the column set into those that can be extracted
		// (extractorView!=null) and those that are to be generated

		Set<String> extractableNames = new TreeSet<String>();

		extractable = new ArrayList<AmpReportColumn>();
		List<AmpReportColumn> generated = new ArrayList<AmpReportColumn>();
		List<AmpReportColumn> colNames = reportMetadata.getOrderedColumns();
		Iterator<AmpReportColumn> i = colNames.iterator();
		while (i.hasNext()) {
			AmpReportColumn element2 = (AmpReportColumn) i.next();
			AmpColumns element = element2.getColumn();
			if (element.getColumnName().equals(ArConstants.COLUMN_TOTAL))
				extractableCount--;

			if (element.getExtractorView() != null) {
				extractable.add(element2);
				if (!element.getColumnName().equals(
						ArConstants.COLUMN_PROPOSED_COST))
					extractableNames.add(element.getColumnName());
			} else
				generated.add(element2);
		}

		extractableCount += extractableNames.size();

		// also add hierarchical columns to extractable:
		Iterator<AmpReportHierarchy> ii = reportMetadata.getHierarchies()
				.iterator();
		while (ii.hasNext()) {
			AmpReportHierarchy element = (AmpReportHierarchy) ii.next();
			AmpReportColumn arc = new AmpReportColumn();
			arc.setColumn(element.getColumn());
			arc.setOrderId(1L);
			extractable.add(arc);
		}
		if(!filter.isJustSearch()){
		    this.columnsToBeRemoved=
		    ColumnFilterGenerator
			.appendFilterRetrievableColumns(extractable, filter);
		   // extractableCount += this.columnsToBeRemoved.size(); 
		}
		
		if (extractable.size() > 0) {
			createDataForColumns(extractable);
		}

		if (generated.size() > 0) {
			createDataForColumns(generated);
		}
	}

	/**
	 * creates the data structures for the list of columns.
	 * 
	 * @param extractable
	 */
	protected void createDataForColumns(Collection<AmpReportColumn> extractable) {

		Iterator<AmpReportColumn> i = extractable.iterator();
		try {

			while (i.hasNext()) {
				AmpReportColumn rcol = (AmpReportColumn) i.next();
				AmpColumns col = rcol.getColumn();
				logger.info("Extracting column " + col.getColumnName()
						+ " with view " + col.getExtractorView());
				String cellTypeName = col.getCellType();
				String extractorView = col.getExtractorView();
				String columnName = col.getColumnName();
				String relatedContentPersisterClass = col
						.getRelatedContentPersisterClass();

				logger.debug("Seeking class " + cellTypeName);

				Class cellType = Class.forName(cellTypeName);

				Constructor cellc = ARUtil.getConstrByParamNo(cellType, 0);

				// create an instance of the cell that is of type described
				Cell cell = (Cell) cellc.newInstance(new Object[] {});

				// column worker is a class returned by getWorker method of each
				// Cell
				Class ceClass = cell.getWorker();

				// create an instance of the column worker

				ColumnWorker ce = null;

				// get the column bound condition:
				String columnFilterSQLClause = "";
				
				if(!filter.isJustSearch()) {
				columnFilterSQLClause=ColumnFilterGenerator
						.generateColumnFilterSQLClause(filter, col, true);
				}

				if (columnFilterSQLClause.length() > 0)
					logger.info("Column " + col.getColumnName()
							+ " appendable SQL filter: ..."
							+ columnFilterSQLClause);

				if (extractorView != null) {

					Constructor ceCons = ARUtil.getConstrByParamNo(ceClass, 4);
					ce = (ColumnWorker) ceCons.newInstance(new Object[] {
							filter.getGeneratedFilterQuery(), extractorView,
							columnName, this });
				} else {
					Constructor ceCons = ARUtil.getConstrByParamNo(ceClass, 3);
					ce = (ColumnWorker) ceCons.newInstance(new Object[] {
							columnName, rawColumns, this });
				}

				ce.setRelatedColumn(col);
				ce.setInternalCondition(columnFilterSQLClause);

				Column column = ce.populateCellColumn();

				if (relatedContentPersisterClass != null) {
					column.setRelatedContentPersisterClass(Class
							.forName(relatedContentPersisterClass));
					// instantiate a relatedContentPersister bean to get the
					// ARDimension and store it for later use
					Constructor contentPersisterCons = ARUtil
							.getConstrByParamNo(column
									.getRelatedContentPersisterClass(), 0);
					ARDimensionable cp = (ARDimensionable) contentPersisterCons
							.newInstance();
					column.setDimensionClass(cp.getDimensionClass());
				}
				logger.info("Adding column "+column.getName());
				CellColumn older = (CellColumn) rawColumns.getColumn(column.getColumnId());
				if(older!=null) {
				    for ( Object o : column.getItems() ) 
					older.addCell(o);
				    
				} else {
				    rawColumns.addColumn(rcol.getOrderId().intValue(), column);
				    rawColumnsByName.put(column.getName(), (CellColumn) column);
				}
			}

		} catch (Exception e) {
			logger.error("Exception: ", e);
		}

	}

	/**
	 * from the extractable list, we get the columns that are filter retrievable
	 * and we apply percentages
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	protected void applyPercentagesToFilterColumns() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Iterator<AmpReportColumn> i = extractable.iterator();
		TextCell fakeMc = new TextCell();
		Set<String> hierarchyNames=new TreeSet<String>();
		Iterator itH = reportMetadata.getHierarchies().iterator();
		while (itH.hasNext()) {
			AmpReportHierarchy h = (AmpReportHierarchy) itH.next();
			hierarchyNames.add(h.getColumn().getColumnName());
		}
		
		
		while (i.hasNext()) {
			AmpReportColumn elem = (AmpReportColumn) i.next();
			Iterator it = elem.getColumn().getFilters().iterator();
			boolean filterSelected=false;
			while (it.hasNext()) {
				AmpColumnsFilters acf = (AmpColumnsFilters) it.next();
				Object property = PropertyUtils.getSimpleProperty(filter, acf
						.getBeanFieldName());
				if(property!=null) filterSelected=true;
			}
			
			//do not apply percentage for columns that were not selected in filters or for columns that are actually hierarchies
			if(!filterSelected || hierarchyNames.contains(elem.getColumn().getColumnName())) continue;
			
			if (elem.getColumn().getFilterRetrievable()!=null && elem.getColumn().getFilterRetrievable().booleanValue()) {
				CellColumn c = rawColumnsByName.get(elem.getColumn()
						.getColumnName());
				// construct a unique set of cell values to fake the metacells of
				// hierarchies
				Set<String> cellValues = new TreeSet<String>();
				Iterator<Cell> iterator = c.iterator();
				while (iterator.hasNext()) {
					// all filterRetrievable are metatextcells
					Cell cell = (Cell) iterator.next();
					cellValues.add((String) cell.getValue());
				}
				// iterate all funding columns and apply them:
				fakeMc.setColumn(c);
				//NEVER apply this for regional reports with regional metaCell:
				if(fakeMc.getColumn().getName().equals(ArConstants.REGION) && reportMetadata.getType().equals(ArConstants.REGIONAL_TYPE))
					continue;
				Iterator<String> metaValuesIt = cellValues.iterator();
				while (metaValuesIt.hasNext()) {
					String value = (String) metaValuesIt.next();
					fakeMc.setValue(value);

					Iterator<Cell> fundingIt = rawColumnsByName.get(
							ArConstants.COLUMN_FUNDING).iterator();
					while (fundingIt.hasNext()) {
						CategAmountCell cac = (CategAmountCell) fundingIt
								.next();
						cac.applyMetaFilter(c.getName(), fakeMc, cac);
					}
				}
			}

		}
	}

	protected void attachFundingMeta() {

		// generate on-the-fly a customized AmpReportColumn - we need it for
		// later steps
		AmpReportColumn arc = new AmpReportColumn();
		AmpColumns ac = new AmpColumns();
		arc.setColumn(ac);
		arc.setOrderId(0L);
		ac.setCellType("org.dgfoundation.amp.ar.cell.CategAmountCell");
		ac.setColumnName(ArConstants.COLUMN_FUNDING);
		if (reportMetadata.getType().intValue() == ArConstants.DONOR_TYPE)
			ac.setExtractorView(ArConstants.VIEW_DONOR_FUNDING);
		if (reportMetadata.getType().intValue() == ArConstants.COMPONENT_TYPE)
			ac.setExtractorView(ArConstants.VIEW_COMPONENT_FUNDING);
		if (reportMetadata.getType().intValue() == ArConstants.REGIONAL_TYPE)
			ac.setExtractorView(ArConstants.VIEW_REGIONAL_FUNDING);
		if (reportMetadata.getType().intValue() == ArConstants.CONTRIBUTION_TYPE)
			ac.setExtractorView(ArConstants.VIEW_CONTRIBUTION_FUNDING);

		ColumnFilterGenerator.attachHardcodedFilters(ac);

		reportMetadata.getOrderedColumns().add(arc);

		// ugly but useful :) get cummulative columns right before funding:
		/*Iterator i = reportMetadata.getOrderedColumns().iterator();
		while (i.hasNext()) {
			AmpReportColumn element = (AmpReportColumn) i.next();
			if (element.getColumn().getColumnName().indexOf("Cumulative") != -1)
				element.setOrderId(Integer.toString(reportMetadata
						.getOrderedColumns().size() - 1));
		}
		*/
		
		// attach funding coming from extra sources ... inject funding from
		// proposed project cost, but with isShow=false so it won't be taken
		// into calculations
		if (ARUtil.containsMeasure(ArConstants.UNCOMMITTED_BALANCE,
				reportMetadata.getMeasures())) {
			AmpReportColumn arcProp = new AmpReportColumn();
			AmpColumns acProp = new AmpColumns();
			arcProp.setColumn(acProp);
			arcProp.setOrderId(0L);
			acProp.setCellType("org.dgfoundation.amp.ar.cell.CategAmountCell");
			acProp.setColumnName(ArConstants.COLUMN_FUNDING);
			acProp.setExtractorView(ArConstants.VIEW_PROPOSED_COST);
			ColumnFilterGenerator.attachHardcodedFilters(acProp);
			reportMetadata.getOrderedColumns().add(arcProp);
		}
	}

	/**
	 * generating the total columns of the report. createTotals is invoked after
	 * the data has been retrieved and is the first substep of processing.
	 * 
	 */
	protected void createTotals() {
		// we perform totals by categorizing only by Funding Type...
		boolean categorizeByFundingType = false;
		if (reportMetadata.getType().intValue() != 4)
			categorizeByFundingType = true;

		// get the funding column
		AmountCellColumn funding = (AmountCellColumn) rawColumns
				.getColumn(ArConstants.COLUMN_FUNDING);

		Column newcol = new GroupColumn();
		if (categorizeByFundingType) {
			Set<AmpReportMeasures> measures = reportMetadata.getMeasures();
			List<AmpReportMeasures> measuresList = new ArrayList<AmpReportMeasures>(
					measures);
			Collections.sort(measuresList);
			Iterator<AmpReportMeasures> ii = measuresList.iterator();
			while (ii.hasNext()) {
				AmpReportMeasures ampReportMeasurement = ii.next();
				AmpMeasures element = ampReportMeasurement.getMeasure();
				if (element.getMeasureName().equals(
						ArConstants.UNDISBURSED_BALANCE)
						|| element.getMeasureName().equals(
								ArConstants.TOTAL_COMMITMENTS)
						|| element.getMeasureName().equals(
								ArConstants.UNCOMMITTED_BALANCE))
					continue;

				MetaInfo<FundingTypeSortedString> metaInfo = new MetaInfo<FundingTypeSortedString>(
						ArConstants.FUNDING_TYPE, new FundingTypeSortedString(
								element.getMeasureName(), reportMetadata
										.getMeasureOrder(element
												.getMeasureName())));
				CellColumn cc = new TotalAmountColumn(metaInfo.getValue()
						.toString(), true);
				newcol.getItems().add(cc);
				cc.setParent(newcol);

				cc.setContentCategory(ArConstants.COLUMN_FUNDING);
				// iterate the src column and add the items with same MetaInfo
				if (funding != null){
					Iterator it = funding.iterator();
					while (it.hasNext()) {
						Categorizable item = (Categorizable) it.next();
						if (item.hasMetaInfo(metaInfo))
							cc.addCell(item);
					}
				}
			}
		}

		// we create the cummulative balance (undisbursed) = act commitment -
		// act disbursement
		// iterate each owner

		if (ARUtil.containsMeasure(ArConstants.UNDISBURSED_BALANCE,
				reportMetadata.getMeasures())) {

			UndisbursedTotalAmountColumn tac = new UndisbursedTotalAmountColumn(
					ArConstants.UNDISBURSED_BALANCE);
			Iterator i = funding.iterator();
			while (i.hasNext()) {
				AmountCell element = (AmountCell) i.next();
				// we do not care here about filtering commitments, that is done
				// at UndisbursedAmountCell level
				tac.addCell(element);
			}

			newcol.getItems().add(tac);
		}

		// uncommitted balance
		if (ARUtil.containsMeasure(ArConstants.UNCOMMITTED_BALANCE,
				reportMetadata.getMeasures())) {

			UncommittedTotalAmountColumn tac = new UncommittedTotalAmountColumn(
					ArConstants.UNCOMMITTED_BALANCE);
			Iterator i = funding.iterator();
			while (i.hasNext()) {
				AmountCell element = (AmountCell) i.next();
				// we do not care here about filtering commitments, that is done
				// at UndisbursedAmountCell level
				tac.addCell(element);
			}

			newcol.getItems().add(tac);
		}

		// we create the total commitments column

		if (ARUtil.containsMeasure(ArConstants.TOTAL_COMMITMENTS,
				reportMetadata.getMeasures())) {

			TotalCommitmentsAmountColumn tac = new TotalCommitmentsAmountColumn(
					ArConstants.TOTAL_COMMITMENTS);
			Iterator i = funding.iterator();
			while (i.hasNext()) {
				AmountCell element = (AmountCell) i.next();
				// we do not care here about filtering commitments, that is done
				// at UndisbursedAmountCell level
				tac.addCell(element);
			}

			newcol.getItems().add(tac);
		}

		newcol
				.setName(reportMetadata.getType().intValue() == 4 ? ArConstants.COLUMN_CONTRIBUTION_TOTAL
						: ArConstants.COLUMN_TOTAL);

		// make order to measurements
		List<AmpReportMeasures> listMeasurement = new ArrayList<AmpReportMeasures>(
				reportMetadata.getMeasures());
		Collections.sort(listMeasurement);

		List<Column> columnlist = newcol.getItems();
		List<Column> tmpColumnList = new ArrayList<Column>(columnlist.size());
		// add columns as measurements order

		if (!categorizeByFundingType) {
			try {
				tmpColumnList.add((Column) funding.clone());
			} catch (CloneNotSupportedException e) {
				logger.error(e);
				e.printStackTrace();
			}

		}

		for (AmpReportMeasures measures : listMeasurement) {
			for (Column column : columnlist) {
				if (column.getName().equalsIgnoreCase(
						measures.getMeasure().getMeasureName())) {
					tmpColumnList.add(column);
					break;
				}
			}
		}

		// replace items by ordered items
		newcol.setItems(tmpColumnList);
		rawColumns.addColumn(newcol);
	}

	protected void applyExchangeRate() {

	}

	protected void removeUnselectedHierarchies() {
		AmpARFilter arf = (AmpARFilter) filter;

	}

	protected void prepareData() {

		try {
			applyPercentagesToFilterColumns();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e);
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e);
		}
		
		applyExchangeRate();

		createTotals();
		AmpARFilter arf = (AmpARFilter) filter;

		if (!arf.isWidget()) {
			categorizeData();
		}
		
		/**
		 * If we handle a normal report (not tab) and allowEmptyColumns is not set then we need to remove 
		 * empty funding columns
		 */
		if ( ( reportMetadata.getDrilldownTab()==null || !reportMetadata.getDrilldownTab() ) && 
				( reportMetadata.getAllowEmptyFundingColumns()==null || !reportMetadata.getAllowEmptyFundingColumns() ) )
				rawColumns.removeEmptyChildren(true);

		report = new GroupReportData(reportMetadata.getName());
		report.setReportMetadata(this.reportMetadata);
		report.setSourceColsCount(new Integer(extractableCount - 1));
		report.setColumnsToBeRemoved(columnsToBeRemoved);
		// ensure acess to the report metadata from the raw columns. we should
		// not need this but ...
		rawColumns.setParent(report);

		ColumnReportData reportChild = new ColumnReportData(reportMetadata
				.getName());

		// add a fake first column ONLY IF the Columns metadata is empty (if we
		// only have hierarchies but no columns). This is needed
		// in order to preserve the table structure of the output
		if (reportMetadata.getColumns().size() == 0) {
			reportChild.addColumn(new CellColumn(""));
		}

		reportChild.addColumns(rawColumns.getItems());
		report.addReport(reportChild);

		// perform removal of funding column if no measure except undisbursed
		// balance is selected. in such case,we just need totals
		// or if widget mode is true...
		if ((reportMetadata.getMeasures().size() == 1 && (ARUtil
				.containsMeasure(ArConstants.UNDISBURSED_BALANCE,
						reportMetadata.getMeasures()) || ARUtil
				.containsMeasure(ArConstants.UNCOMMITTED_BALANCE,
						reportMetadata.getMeasures())))
				|| arf.isWidget())
			reportChild.removeColumnsByName(ArConstants.COLUMN_FUNDING);

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
		List orderedHierarchies = ARUtil.createOrderedHierarchies(
				reportMetadata.getColumns(), reportMetadata.getHierarchies());
		// add Unallocated fake items for activities missing hierarchy enabled
		// data
		Iterator i = orderedHierarchies.iterator();

		while (i.hasNext()) {
			Collection allIds = report.getOwnerIds();
			AmpReportHierarchy element = (AmpReportHierarchy) i.next();
			AmpColumns c = element.getColumn();
			// the report always has one ColumnReportData since we did no
			// processing, this is raw data:
			ColumnReportData rd = (ColumnReportData) report.getItem(0);
			// column used by hierarchy
			CellColumn hc = (CellColumn) rd.getColumn(c.getColumnName());
			Collection hcIds = hc.getOwnerIds();
			// we remove from the list of all ids in all columns, the ones
			// present in the hierarchy column.
			allIds.removeAll(hcIds);
			// now we have the ids of the items that do not have the hierarchy
			// cells present, and we need to add fakes so we can
			// show the Unallocated hierarchy
			// if(hc.getName().equals("Sub-Sector")){
			// //"
			// Cell fakeC = hc.getWorker().newCellInstance();
			// fakeC.setOwnerId(new Long(340));
			// hc.addCell(fakeC);
			// }
			Iterator ii = allIds.iterator();
			while (ii.hasNext()) {
				Long id = (Long) ii.next();
				Cell fakeC = hc.getWorker().newCellInstance();
				fakeC.setOwnerId(id);
				//
				// requirements for translation purposes
				Long siteId = rd.getParent().getReportMetadata().getSiteId();
				String locale = rd.getParent().getReportMetadata().getLocale();
				String text = fakeC.getValue().toString();
				String translatedText = null;
				//String prefix = "aim:reportGenerator:"; not used cos hash keys
				try {
					translatedText = TranslatorWorker.translateText(text, locale, new Long(siteId));
				} catch (WorkerException e) {
					e.printStackTrace();
				}
				if (translatedText.compareTo("") == 0)
					translatedText = text;
				//
				fakeC.setValue(translatedText);
				//
				hc.addCell(fakeC);
			}

		}
		// now we can create the hiearchy tree
		i = orderedHierarchies.iterator();
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

		report.removeEmptyChildren();
	}

	/**
	 * split categorizable columns into subcolumns based on metadata
	 * (categories).
	 */
	protected void categorizeData() {
		Iterator i = reportMetadata.getOrderedColumns().iterator();
		while (i.hasNext()) {
			AmpColumns element = ((AmpReportColumn) i.next()).getColumn();
			String colName = element.getColumnName();
			List cats = getColumnSubCategories(element.getColumnName());
			Column c = rawColumns.getColumn(colName);
			if (c instanceof GroupColumn)
				continue; // ugly fix to AMP-2793 and the problem created by
							// mauricio after generating GroupColumnS before
							// categorizeData
			CellColumn src = (CellColumn) c;
			if (cats.size() != 0) {
				Column newcol = GroupColumn.verticalSplitByCategs(src, cats,
						true, reportMetadata);
				rawColumns.replaceColumn(colName, newcol);
			}
		}
	}

	/**
	 * @param reportMetadata
	 * @param condition
	 */
	public AmpReportGenerator(AmpReports reportMetadata, AmpARFilter filter,
			HttpServletRequest request) {
		super();
		this.reportMetadata = reportMetadata;
		rawColumns = new GroupColumn(ArConstants.COLUMN_RAW_DATA);
		this.filter = filter;
		extractableCount = 0;

		filter.generateFilterQuery(request);

		logger.info("Master report query:" + filter.getGeneratedFilterQuery());

		// remove the columns that are also hierarchies

		Iterator i = reportMetadata.getColumns().iterator();
		while (i.hasNext()) {
			AmpReportColumn col = (AmpReportColumn) i.next();
			Iterator ii = reportMetadata.getHierarchies().iterator();
			while (ii.hasNext()) {
				AmpReportHierarchy h = (AmpReportHierarchy) ii.next();
				if (h.getColumn().getColumnName().equals(
						col.getColumn().getColumnName()))
					i.remove();
			}
		}

		reportMetadata.setOrderedColumns(ARUtil.createOrderedColumns(
				reportMetadata.getColumns(), reportMetadata.getHierarchies()));

		attachFundingMeta();
	}

}
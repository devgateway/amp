/**
 * AmpReportGenerator.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.dgfoundation.amp.ar.ArConstants.SyntheticColumnsMeta;
import org.dgfoundation.amp.ar.cell.AmountCell;
import org.dgfoundation.amp.ar.cell.CategAmountCell;
import org.dgfoundation.amp.ar.cell.CategCell;
import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.cell.ComputedAmountCell;
import org.dgfoundation.amp.ar.cell.ListCell;
import org.dgfoundation.amp.ar.cell.MetaTextCell;
import org.dgfoundation.amp.ar.cell.TextCell;
import org.dgfoundation.amp.ar.dimension.ARDimensionable;
import org.dgfoundation.amp.ar.exception.IncompatibleColumnException;
import org.dgfoundation.amp.ar.exception.UnidentifiedItemException;
import org.dgfoundation.amp.ar.filtercacher.FastFilterCacher;
import org.dgfoundation.amp.ar.filtercacher.NopFilterCacher;
import org.dgfoundation.amp.ar.workers.ColumnWorker;
import org.dgfoundation.amp.ar.workers.ComputedAmountColWorker;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.aim.dbentity.AmpColumnsFilters;
import org.digijava.module.aim.dbentity.AmpMeasures;
import org.digijava.module.aim.dbentity.AmpReportColumn;
import org.digijava.module.aim.dbentity.AmpReportHierarchy;
import org.digijava.module.aim.dbentity.AmpReportMeasures;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jun 12, 2006
 * 
 */
public class AmpReportGenerator extends ReportGenerator {

	/**
	 * true = use FastFilterCachier, false = use NopFilterCacher
	 */
	public final static boolean USE_FILTER_CACHING = true;
	
	List<AmpReportColumn> extractable; // columns extractable while building the report
	protected int extractableCount; // looks like extractable.size() [need to check!]
	private List<String> columnsToBeRemoved;
	private boolean debugMode = false;
	private boolean pledgereport = false;
	private boolean cleanupMetadata = true;
	private boolean mtefExtractOnlyDonorData = false;
	private Set<String> fundingOrgHiers;
	
	private HttpSession session	= null;
	private BigDecimal totalac;


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

			//split column for type of assistance ONLY when TOA is added as column
			if(!ARUtil.hasHierarchy(reportMetadata.getHierarchies(),ArConstants.TERMS_OF_ASSISTANCE) &&
					ARUtil.containsColumn(ArConstants.TERMS_OF_ASSISTANCE, reportMetadata.getColumns()) && "true".compareTo(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SPLIT_BY_TYPE_OF_ASSISTANCE))==0) {
					ret.add(ArConstants.TERMS_OF_ASSISTANCE);
				}
			if(!ARUtil.hasHierarchy(reportMetadata.getHierarchies(),ArConstants.MODE_OF_PAYMENT) &&
					ARUtil.containsColumn(ArConstants.MODE_OF_PAYMENT, reportMetadata.getColumns())) {
				ret.add(ArConstants.MODE_OF_PAYMENT);
			}
			
			if (ARUtil.containsMeasure(ArConstants.REAL_DISBURSEMENTS, reportMetadata.getMeasures()))
			{
				ret.add(ArConstants.TRANSACTION_REAL_DISBURSEMENT_TYPE);
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
			
		// build extractable, extractableNames, generated
		while (i.hasNext()) {
			boolean skypIt=false;
			AmpReportColumn element2 = (AmpReportColumn) i.next();
			AmpColumns element = element2.getColumn();
			//if we are showing an summarized report we will hidden columns with row values
			
			
			if (element.getColumnName().equals(ArConstants.COLUMN_TOTAL))
				extractableCount--;
			
			if (element.getExtractorView() != null) {
				extractable.add(element2);
				if ((!element.getColumnName().equals(ArConstants.COLUMN_PROPOSED_COST) || (!element.getColumnName().equals(ArConstants.COSTING_GRAND_TOTAL))))
					extractableNames.add(element.getColumnName());
			} else
				generated.add(element2);
			}
		

		extractableCount += extractableNames.size();

		// also add hierarchical columns to extractable:
		for (AmpReportHierarchy element:reportMetadata.getHierarchies()) {
			AmpReportColumn arc = new AmpReportColumn();
			arc.setColumn(element.getColumn());
			arc.setOrderId(1L);
			extractable.add(arc);
		}
					
		if(!filter.isJustSearch())
		{
		    this.columnsToBeRemoved = ColumnFilterGenerator.appendFilterRetrievableColumns(extractable, filter);
		   // extractableCount += this.columnsToBeRemoved.size();		    
		}
		else
			this.columnsToBeRemoved = new ArrayList<String>();
		
		/**
		 * see whether we have any hierarchy relevant for Directed Transactions (Donor Agency, Implementing Agency, ... etc)
		 */
		this.fundingOrgHiers = reportMetadata.getHierarchyNames();
		fundingOrgHiers.retainAll(ArConstants.COLUMN_ROLE_CODES.keySet()); // list of  hiers which are relevant
		
		this.setMtefExtractOnlyDonorData(fundingOrgHiers.isEmpty());
		
		if (extractable.size() > 0) {
			createDataForColumns(extractable);
		}

		if (generated.size() > 0) {
			createDataForColumns(generated);
		}
		
		if (ARUtil.containsColumn(ArConstants.COSTING_GRAND_TOTAL,reportMetadata.getShowAblesColumns())){
			rawColumns.getItems().remove(rawColumns.getColumn(ArConstants.COSTING_GRAND_TOTAL));
		}
		
	}
	
	/**
	 * ONLY call for extractorView.equals("v_mtef_funding")
	 * @param columnFilterSQLClause - already-generated columnFilterSQLClause
	 * @param columnAlias - the alias, of the form "mtefYYYY"
	 * @return
	 */
	protected String generate_mtef_filter_statement(String columnFilterSQLClause, String columnAlias)
	{
		if (columnFilterSQLClause == null)
			columnFilterSQLClause = "";
		
//		if (columnFilterSQLClause.isEmpty())
//			columnFilterSQLClause = "AND (1 = 1)";
		
		if ((columnAlias == null) || (!columnAlias.startsWith("mtef")) || (columnAlias.length() != 8))
			throw new RuntimeException("invalid column alias for an MTEF column: " + columnAlias);
		
		String fundingYearStr = columnAlias.substring(4);
		int fundingYear = Integer.parseInt(fundingYearStr);
		
		columnFilterSQLClause += " AND (extract(year from currency_date) = " + fundingYear + ")";
		return columnFilterSQLClause;
	}

	/**
	 * creates the data structures for the list of columns.
	 * 
	 * @param extractable
	 */
	protected void createDataForColumns(Collection<AmpReportColumn> extractable) {
		
		List <SyntheticColumnsMeta> possibleSyntColMeta	= ArConstants.syntheticColumns;
		if ( this.session != null ) {
			possibleSyntColMeta	= ARUtil.getSyntheticGeneratorList(this.session);
		}
		List <SyntheticCellGenerator> syntCellGenerators	= new ArrayList<SyntheticCellGenerator>();
		
		
		for (ArConstants.SyntheticColumnsMeta scm:  possibleSyntColMeta) {
			SyntheticCellGenerator generator	= scm.getGenerator();
			if ( generator.checkIfApplicabale(reportMetadata)  ) {
				if ( this.session != null )
					generator.setSession(this.session);
				syntCellGenerators.add(generator);
			}
		}
		
		Iterator<AmpReportColumn> i = extractable.iterator();
		
		try {

			while (i.hasNext()) {
				
				long extractStartTime = System.currentTimeMillis();
				
				AmpReportColumn rcol = i.next();
				AmpColumns col = rcol.getColumn();
				String cellTypeName = col.getCellType();
				String extractorView = "";
				
				if (col.getExtractorView()!=null && this.filter.isPublicView()
						// AND not a report used for budget export
						&& (this.reportMetadata.getBudgetExporter() == null || !this.reportMetadata.getBudgetExporter()) ){
					extractorView = ArConstants.VIEW_PUBLIC_PREFIX+col.getExtractorView();
				}else{
					extractorView  = col.getExtractorView();
				}
				logger.info("Extracting column " + col.getColumnName()
						+ " with view " + extractorView);
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
					columnFilterSQLClause = ColumnFilterGenerator
						.generateColumnFilterSQLClause(filter, col, true);
				}

				if (columnFilterSQLClause.length() > 0)
					logger.info("Column " + col.getColumnName()
							+ " appendable SQL filter: ..."
							+ columnFilterSQLClause);

				if (extractorView != null) {

					Constructor<? extends ColumnWorker> ceCons = ARUtil.getConstrByParamNo(ceClass, 4, this.session);
					ce = (ColumnWorker) ceCons.newInstance(new Object[] {
							filter.getGeneratedFilterQuery(), extractorView,
							columnName, this });
				} else {
					Constructor<? extends ColumnWorker> ceCons = ARUtil.getConstrByParamNo(ceClass, 3, this.session);
					ce = (ColumnWorker) ceCons.newInstance(new Object[] {
							columnName, rawColumns, this });
				}
				
				ce.setRelatedColumn(col);
				
				if ((extractorView != null) && extractorView.equals("v_mtef_funding"))
					columnFilterSQLClause = generate_mtef_filter_statement(columnFilterSQLClause, col.getAliasName());
				
				ce.setInternalCondition(columnFilterSQLClause);
				ce.setSession(this.session);
				
				
				ce.setDebugMode(debugMode);
				ce.setPledge(pledgereport);
				
				Column column = ce.populateCellColumn();
				
				if ( syntCellGenerators.size() > 0 && ArConstants.COLUMN_FUNDING.equals(column.getName()) ) {
					for (SyntheticCellGenerator scg: syntCellGenerators) {
						int order	= reportMetadata.getMeasureOrder( scg.getMeasureName() );
						Collection<CategAmountCell> newCells	= scg.generate( column.getItems(), order );
						if ( newCells!=null )
							column.getItems().addAll(newCells);
					}
				} 
				

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
				logger.info("Adding column " + column.getName());
				CellColumn older = (CellColumn) rawColumns.getColumn(column.getColumnId());
				if (older != null) {
				    for ( Object o : column.getItems() ) 
				    	older.addCell((Cell) o);
				    
				} else {
					rawColumns.addColumn(rcol.getOrderId().intValue(), column);
				    rawColumnsByName.put(column.getName(), (CellColumn) column);
				}
				
				long extractEndTime = System.currentTimeMillis();
				System.out.format("extracting column %s took %d milliseconds\n", column.getName(), extractEndTime - extractStartTime);				
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
			AmpReportColumn elem = i.next();
			boolean filterSelected=false;
			for (AmpColumnsFilters acf:elem.getColumn().getFilters()) {
				Object property = PropertyUtils.getSimpleProperty(filter, acf
						.getBeanFieldName());
				if (property != null) filterSelected = true;
			}
			
			//do not apply percentage for columns that were not selected in filters or for columns that are actually hierarchies
			if (!filterSelected || hierarchyNames.contains(elem.getColumn().getColumnName())) continue;
			
			if (elem.getColumn().getFilterRetrievable() != null && elem.getColumn().getFilterRetrievable().booleanValue()) {
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
				if((fakeMc.getColumn().getName().equals(ArConstants.REGION) || fakeMc.getColumn().getName().equals(ArConstants.DISTRICT) || fakeMc.getColumn().getName().equals(ArConstants.ZONE) ) && reportMetadata.getType().equals(ArConstants.REGIONAL_TYPE))
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
						cac.applyMetaFilter(c.getName(), fakeMc, cac, false, false);
					}
					
					for ( CellColumn tempCellColumn: rawColumnsByName.values() ) {
						if (tempCellColumn.getName().toLowerCase().contains("mtef")) {
							Iterator<Cell> mtefCellIt	= tempCellColumn.iterator();
							while (mtefCellIt.hasNext()) {
								CategAmountCell cacParent = (CategAmountCell) mtefCellIt.next();
								if ( cacParent.getMergedCells() != null ) 
									for ( Object cacObj: cacParent.getMergedCells() ) {
										CategAmountCell cac	= (CategAmountCell) cacObj;
										cac.applyMetaFilter(c.getName(), fakeMc, cac, false, false); 
									}
							}
						}
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
		if (reportMetadata.getType().intValue() == ArConstants.PLEDGES_TYPE)
			ac.setExtractorView(ArConstants.VIEW_PLEDGES_FUNDING);

		ColumnFilterGenerator.attachHardcodedFilters(ac);

		reportMetadata.getOrderedColumns().add(arc);

		
	
		
		if (ARUtil.containsMeasure(ArConstants.UNCOMMITTED_BALANCE,reportMetadata.getMeasures())||ARUtil.containsColumn(ArConstants.COLUMN_UNCOMM_CUMULATIVE_BALANCE,reportMetadata.getShowAblesColumns())) {
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
		
		Iterator<AmpReportColumn> iterRC	= reportMetadata.getColumns().iterator();
		while ( iterRC.hasNext() ) {
			
			AmpReportColumn tempRC	= iterRC.next();
			AmpColumns tempCol		= tempRC.getColumn();
			if ( tempCol.getColumnName().toLowerCase().contains("mtef") ) {
				AmpColumns clonedCol	= new AmpColumns();
				clonedCol.setColumnName( tempCol.getColumnName() );
				clonedCol.setColumnId( tempCol.getColumnId() );
				clonedCol.setAliasName( tempCol.getAliasName() );
				clonedCol.setCellType( tempCol.getCellType() );
				clonedCol.setDescription( tempCol.getDescription() );
				clonedCol.setExtractorView( tempCol.getExtractorView() );
				clonedCol.setFilterRetrievable( tempCol.getFilterRetrievable() );
				clonedCol.setRelatedContentPersisterClass( tempCol.getRelatedContentPersisterClass() );
				clonedCol.setTokenExpression( tempCol.getTokenExpression() );
				clonedCol.setTotalExpression( tempCol.getTotalExpression() );
				
				tempRC.setColumn(clonedCol);
				
				ColumnFilterGenerator.attachHardcodedFilters(clonedCol);

                reportMetadata.incrementExtraColumnsCount();
			}
		}
	}

	/**
	 * builds subcolumns of type "Actual Commitments / Disbursements / etc" and adds them to newcol
	 * returns true iff the function has created, without the report having required it, a "Total Actual Commitments" column
	 * @return
	 */
	protected boolean buildFundingTypeCategoriesSubcolumns(AmountCellColumn<CategAmountCell> funding, GroupColumn newcol, boolean verticalSplitByTypeOfAssistence, boolean verticalSplitByModeOfPayment)
	{
		Set<AmpReportMeasures> measures = reportMetadata.getMeasures();
		List<AmpReportMeasures> measuresList = new ArrayList<AmpReportMeasures>(measures);
		Collections.sort(measuresList);

		boolean totalActualCommitmentsAdded = false;
		boolean totalActualCommitmentsLoaded = ARUtil.containsMeasure(ArConstants.ACTUAL_COMMITMENTS, measuresList);
		if (!totalActualCommitmentsLoaded) {
			AmpReportMeasures ampReportMeasurement = new AmpReportMeasures();
			AmpMeasures element = new AmpMeasures();
			element.setMeasureName(ArConstants.ACTUAL_COMMITMENTS);
			ampReportMeasurement.setMeasure(element);
			measuresList.add(ampReportMeasurement);
			totalActualCommitmentsAdded = true;
		}
		
		for(AmpReportMeasures ampReportMeasurement:measuresList)
		{
			AmpMeasures element = ampReportMeasurement.getMeasure();
			
			if (!element.isSplittable())
				continue;				
			
			MetaInfo<FundingTypeSortedString> metaInfo = new MetaInfo<FundingTypeSortedString>(ArConstants.FUNDING_TYPE, new FundingTypeSortedString(element.getMeasureName(), reportMetadata.getMeasureOrder(element.getMeasureName())));
			AmountCellColumn<CategAmountCell> cc = null;
			
			if(verticalSplitByModeOfPayment || verticalSplitByTypeOfAssistence){
				cc = new AmountCellColumn<CategAmountCell>(metaInfo.getValue().toString());	
			}else{
				cc = new TotalAmountColumn<CategAmountCell>(metaInfo.getValue().toString(), true);
			}
			
			newcol.getItems().add(cc);
			cc.setParent(newcol);

			cc.setContentCategory(ArConstants.COLUMN_FUNDING);
			// iterate the src column and add the items with same MetaInfo
			if (funding != null){
				Iterator<CategAmountCell> it = funding.iterator();
				while (it.hasNext()) {
					CategAmountCell item = (CategAmountCell) it.next();
					boolean shouldAddRealDisbursement = element.getMeasureName().equals(ArConstants.REAL_DISBURSEMENTS) &&
		    				GroupColumn.isRealDisbursement(item); // add ACTUAL DISBURSEMENT items to the REAL DISBURSEMENTS column - as they have the same datasource (ACTUAL DISBURSEMENTS), they have the same metadata
					
					boolean shouldAddEstimatedDisbursement = element.getMeasureName().equals(ArConstants.ACTUAL_DISBURSEMENTS) && 
							GroupColumn.isEstimatedDisbursement(item);
					
					boolean shouldAddGenericFunding = item.hasMetaInfo(metaInfo) && (!(element.getMeasureName().equals(ArConstants.ACTUAL_DISBURSEMENTS) || element.getMeasureName().equals(ArConstants.REAL_DISBURSEMENTS)));
					
					boolean shouldAddCellToColumn = shouldAddGenericFunding || shouldAddEstimatedDisbursement || shouldAddRealDisbursement; // ugly hack because the same data source (actual disbursements) should fill two columns: actual disbursements + real disbursements
					
					if (!shouldAddCellToColumn)
						continue;
					CategAmountCell cellToAdd = null;
					try
    				{
						cellToAdd = (CategAmountCell)(item.clone());
        				cellToAdd.cloneMetaData();
    				}
    				catch(Exception e)
    				{
    					throw new RuntimeException("while generating totals column DISBURSEMENTS, error cloning cell " + item.toString());
    				}
					
					cc.addCellRaw(cellToAdd); //addCellRaw because we don't want TotalAmountCellColumn to merge everything in one huge cell
				}
			}
			
			
			boolean shouldSplitTotalsByRealDisbursements = element.getMeasureName().equals(ArConstants.REAL_DISBURSEMENTS);
			if (funding != null && shouldSplitTotalsByRealDisbursements)
			{
				// DO SOMETHING, BOZO!
				this.addTotalsVerticalSplit(newcol, ArConstants.TRANSACTION_REAL_DISBURSEMENT_TYPE);
			}

		}
		
		newcol.detachCells();
		return !totalActualCommitmentsLoaded && totalActualCommitmentsAdded;
	}
	
	/**
	 * calculates the "Total Actual Commitments" (a computed measure) field of the report and removes the "Total Costs / Actual Commitments" subcolumn iff it was not requested in the report (it is ALWAYS calculated in order to be able to calculate this field in this function)
	 * @param newcol - the "Total Costs" GroupColumn which is being created
	 * @param removeActualCommitmentsSubcolumn
	 */
	protected void buildTotalActualCommitmentsSubcolumn(GroupColumn newcol, boolean removeActualCommitmentsSubcolumn)
	{
		//Calculate global totals for Computed Measures that require it
		TotalAmountColumn removeableColumn = null;
		BigDecimal total = new BigDecimal(0);
		for(Column el:newcol.getItems())
		{
			if (!(el instanceof TotalAmountColumn))
				continue;
			if (!el.getName().equals(ArConstants.ACTUAL_COMMITMENTS))
				continue;
			TotalAmountColumn column = (TotalAmountColumn) el;

			for(Cell el2:(List<Cell>)(column.getItems()))
			{
				if (!(el2 instanceof CategAmountCell))
					continue;
				CategAmountCell cac = (CategAmountCell) el2;
				double dbl = cac.getAmount();
				total = total.add(new BigDecimal(dbl));
			}
			if (removeActualCommitmentsSubcolumn){ //If it was added for calculation purposes, remove it
				removeableColumn = column;
			}
			break; // we have found our TotalAmountColumn -> no more work to do
		}
		if(removeableColumn != null){
			newcol.getItems().remove(removeableColumn);
		}
		this.setTotalActualCommitments(total);
	}
	
	/**
	 * generating the total columns of the report. createTotals is invoked after
	 * the data has been retrieved and is the first substep of processing.
	 * 
	 */
	protected void createTotals() {
		boolean verticalSplitByTypeOfAssistence = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SPLIT_BY_TYPE_OF_ASSISTANCE).equalsIgnoreCase("true") &&
				!ARUtil.hasHierarchy(reportMetadata.getHierarchies(),ArConstants.TERMS_OF_ASSISTANCE) &&
				ARUtil.containsColumn(ArConstants.TERMS_OF_ASSISTANCE, reportMetadata.getColumns());
		
		boolean verticalSplitByModeOfPayment = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SPLIT_BY_MODE_OF_PAYMENT)!= null && 
				FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SPLIT_BY_MODE_OF_PAYMENT).equalsIgnoreCase("true") &&
			!ARUtil.hasHierarchy(reportMetadata.getHierarchies(),ArConstants.MODE_OF_PAYMENT) &&
			ARUtil.containsColumn(ArConstants.MODE_OF_PAYMENT, reportMetadata.getColumns());
		
		// we perform totals by categorizing only by Funding Type...
		boolean categorizeByFundingType = false;
		if (reportMetadata.getType().intValue() != 4)
			categorizeByFundingType = true;
		
		// get the funding column
		AmountCellColumn funding = (AmountCellColumn) rawColumns.getColumn(ArConstants.COLUMN_FUNDING);
						
		GroupColumn newcol = new GroupColumn(reportMetadata.getType().intValue() == 4 ? ArConstants.COLUMN_CONTRIBUTION_TOTAL: ArConstants.COLUMN_TOTAL);
		
		if (categorizeByFundingType) {
			boolean removeActualCommitmentsSubcolumn = buildFundingTypeCategoriesSubcolumns(funding, newcol, verticalSplitByTypeOfAssistence, verticalSplitByModeOfPayment);
			buildTotalActualCommitmentsSubcolumn(newcol, removeActualCommitmentsSubcolumn);
		}
 
		
		//get the MTEF columns
		List<TotalComputedAmountColumn> mtefCols	= new ArrayList<TotalComputedAmountColumn>();
		for ( Object tempObj:rawColumns.getItems() ) {
			Column tempCol		= (Column) tempObj;
			if ( tempCol.getAbsoluteColumnName().contains("MTEF") ) {
				mtefCols.add( (TotalComputedAmountColumn) tempCol );
			}
		}

		/**
		 * Iterare all measure and add a column for each computed measure
		 */
		Set<AmpReportMeasures> xmeasures = reportMetadata.getMeasures();
		List<AmpReportMeasures> xmeasuresList = new ArrayList<AmpReportMeasures>(xmeasures);
		Collections.sort(xmeasuresList);
//		Iterator<AmpReportMeasures> ii = xmeasuresList.iterator();
		for (AmpReportMeasures ampReportMeasures : xmeasuresList)
		{
			if (ampReportMeasures.getMeasure().getExpression() != null){
				AmpMeasures m = ampReportMeasures.getMeasure();
				newcol.getItems().add(buildTotalComputedMeasure(m, funding, mtefCols));
			}
		}
		
		//end computed measures
		
		// we create the total commitments column

		if (reportMetadata.needsTotalCommitments()) {
			TotalCommitmentsAmountColumn tac = new TotalCommitmentsAmountColumn(ArConstants.TOTAL_COMMITMENTS);
			tac.absorbColumn(funding);
			newcol.getItems().add(tac);
		}
		
		// make order to measurements
//		List<AmpReportMeasures> listMeasurement = new ArrayList<AmpReportMeasures>( reportMetadata.getMeasures());
//		Collections.sort(listMeasurement);

		
		
		//List<Column> columnlist = newcol.getItems();
		List<Column> tmpColumnList = new ArrayList<Column>(newcol.getItems().size());
		// add columns as measurements order

		if (!categorizeByFundingType) {
			try {
				tmpColumnList.add((Column) funding.clone());
			} catch (CloneNotSupportedException e) {
				logger.error(e);
				e.printStackTrace();
			}

		}

		for (AmpReportMeasures measures : xmeasuresList) 
		{
			Column subcol = newcol.getColumnByName(measures.getMeasure().getMeasureName());
			if (subcol != null)
				tmpColumnList.add(subcol);
		}

		// replace items by ordered items
		newcol.setItems(tmpColumnList);
				
		// add subcolumns for type of assistance
		//split column for type of assistance ONLY when TOA is added as column	
		if(verticalSplitByTypeOfAssistence) {
			this.addTotalsVerticalSplit(newcol, ArConstants.TERMS_OF_ASSISTANCE/*, ArConstants.TERMS_OF_ASSISTANCE_TOTAL*/);
		}
		
		if(verticalSplitByModeOfPayment) {			
			this.addTotalsVerticalSplit(newcol, ArConstants.MODE_OF_PAYMENT/*, ArConstants.MODE_OF_PAYMENT_TOTAL*/);
		}
		
		rawColumns.addColumn(newcol);
	}
	
	protected TotalComputedMeasureColumn buildTotalComputedMeasure(AmpMeasures m, AmountCellColumn funding, List<TotalComputedAmountColumn> mtefCols)
	{
		TotalComputedMeasureColumn cTac = new TotalComputedMeasureColumn(m.getMeasureName());
		cTac.setExpression(m.getExpression());
		cTac.setDescription(m.getDescription());
		cTac.absorbColumn(funding);
		
		for (TotalComputedAmountColumn tcaCol: mtefCols ) {
			cTac.absorbColumn(tcaCol);
		}
		return cTac;
	}
	
	protected void addTotalsVerticalSplit(GroupColumn newcol, String splitterCol) {
		//iterate each column in newcol
		for (int i=0; i< newcol.getItems().size();i++){
			Column nestedCol = (Column) newcol.getItems().get(i);
			if(nestedCol instanceof GroupColumn || nestedCol instanceof TotalComputedMeasureColumn) continue;
			
//			List<String> cat = new ArrayList<String>();
//			cat.add(splitterCol);
			GroupColumn nestedCol2 = nestedCol.verticalSplitByCateg(splitterCol, true, reportMetadata);

			if (nestedCol2 != null)
				newcol.replaceColumn(nestedCol.getName(), nestedCol2);			
		}
		
	}
	public void setTotalActualCommitments(BigDecimal total) {
		this.totalac = total;
	}
	public BigDecimal getTotalActualCommitments() {
		return this.totalac;
	}

	protected void applyExchangeRate() {

	}

	protected void removeUnselectedHierarchies() {
		AmpARFilter arf = (AmpARFilter) filter;

	}
	
	/**
	 *  if has Real Disbursements column AND has a hierarchy by one of ArConstants.COLUMN_ROLE_CODES.keys(), then remove all columns which are not "v temu" from:
	 *	1) Funding -> YEAR -> Real Disbursements -> XYZ
	 *	2) Total Costs -> Real Disbursements -> XYZ
	 * @param fundingOrgHiers
	 * {@link CategAmountCell#filter(Cell, Set)}
	 */
	protected void removeUnusedRealDisbursementsFlowsFromReport(Set<String> fundingOrgHiers)
	{
		Map<String, String> hierNameToOrgRole = new HashMap<String, String>();
		hierNameToOrgRole.put(ArConstants.ROLE_NAME_EXECUTING_AGENCY, Constants.EXECUTING_AGENCY);
		hierNameToOrgRole.put(ArConstants.ROLE_NAME_IMPLEMENTING_AGENCY, Constants.IMPLEMENTING_AGENCY);
		hierNameToOrgRole.put(ArConstants.ROLE_NAME_DONOR_AGENCY, Constants.FUNDING_AGENCY);
		hierNameToOrgRole.put(ArConstants.ROLE_NAME_BENEFICIARY_AGENCY, Constants.BENEFICIARY_AGENCY);
		
		Set<String> allMandatoryRoles = new HashSet<String>();
		for(String hierRole:fundingOrgHiers)
		{
			String z = hierNameToOrgRole.get(hierRole);
			if (z == null)
			{
				logger.warn("no funding flows are legal. the report will come out with empty 'Real Disbursements' columns");
				allMandatoryRoles.add("IMPOSSIBLE_ROLE");
			}
			else
				allMandatoryRoles.add(z);
		}
//		Map<String, Set<String>> flowsToRetain = new HashMap<String, Set<String>>();
//		flowsToRetain.put(ArConstants.ROLE_NAME_DONOR_AGENCY, new HashSet<String>(){{add(ArConstants.TRANSACTION_DN_EXEC);}});
//		flowsToRetain.put(ArConstants.ROLE_NAME_EXECUTING_AGENCY, new HashSet<String>(){{add(ArConstants.TRANSACTION_DN_EXEC);add(ArConstants.TRANSACTION_EXEC_IMPL);}});
//		flowsToRetain.put(ArConstants.ROLE_NAME_IMPLEMENTING_AGENCY, new HashSet<String>(){{add(ArConstants.TRANSACTION_EXEC_IMPL);add(ArConstants.TRANSACTION_IMPL_BENF);}});
//		flowsToRetain.put(ArConstants.ROLE_NAME_BENEFICIARY_AGENCY, new HashSet<String>(){{add(ArConstants.TRANSACTION_IMPL_BENF);}});
//
//		Set<String> allLegalFlows = new HashSet<String>(){{
//			add(ArConstants.TRANSACTION_DN_EXEC);
//			add(ArConstants.TRANSACTION_EXEC_IMPL);
//			add(ArConstants.TRANSACTION_IMPL_BENF);
//		}};
//		
//		for(String hierColName:fundingOrgHiers)
//		{
//			if (!flowsToRetain.containsKey(hierColName))
//			{
//				logger.warn("funding hierarchy not found in FLOWS_TO_RETAIN, ignoring");
//				continue;
//			}
//			allLegalFlows.retainAll(flowsToRetain.get(hierColName));
//		}
//		if (allLegalFlows.isEmpty())
//		{
//			logger.warn("no funding flows are legal. Instead of removing the whole Real Disbursements column, will let it empty - Report Wizard shouldn't allow such reports to exist");
//			return;
//		}
		
		filterRealDisbursementsSubcolumns(rawColumns.getColumnByName("Funding"), allMandatoryRoles);
		filterRealDisbursementsSubcolumns(rawColumns.getColumnByName(ArConstants.COLUMN_TOTAL), allMandatoryRoles);
	}
	
	/**
	 * given at the input a Real Disbursements GroupColumn and a list of legal subcolumns, removes all the subcolumns which have a different name
	 * @param realDisbursementsCol
	 * @param allLegalFlows
	 */
	protected void cleanupRealDisbursements(GroupColumn realDisbursementsCol, Set<String> allMandatoryRoles)
	{
		Iterator<Column> cols = realDisbursementsCol.iterator();
		Set<String> allMandatoryUserFriendlyRoles = new HashSet<String>();
		for(String mandatoryRole:allMandatoryRoles)
		{
			String mandatoryRoleUserFriendly = ArConstants.userFriendlyNameOfRole(mandatoryRole);
			allMandatoryUserFriendlyRoles.add(mandatoryRoleUserFriendly);
		}		
		while (cols.hasNext())
		{
			Column col = cols.next();
			// hacky -> deduct from the column name the roles it contains. We do the hack because columns do not have attached metadata
			java.util.StringTokenizer colNameScanner = new java.util.StringTokenizer(col.getName(), "-");
			if (colNameScanner.countTokens() != 2)
				throw new RuntimeException("Real Disbursements should only have AAAA-BBBB type of subcolumns!"); // something fishy, shouldn't get here
			String role1UserFriendlyName = colNameScanner.nextToken();
			String role2UserFriendlyName = colNameScanner.nextToken();
			// the column should have all of the roles mentioned in allMandatoryRoles in its buildup
			boolean everythingOk = true;
			for(String mandatoryRoleUserFriendly:allMandatoryUserFriendlyRoles)
			{
				everythingOk &= role1UserFriendlyName.equals(mandatoryRoleUserFriendly) || role2UserFriendlyName.equals(mandatoryRoleUserFriendly);
				if (!everythingOk)
					break;
			}
			if (!everythingOk)
				cols.remove();
		}
	}
	
	/**
	 * recursively goes all the way down in a hierarchy of columns in order to find & clean all the Real Disbursements GroupColumns it can find
	 * @param col
	 * @param allLegalFlows
	 */
	protected void filterRealDisbursementsSubcolumns(Column col, Set<String> allMandatoryRoles)
	{
		if (!(col instanceof GroupColumn))
			return;
		
		GroupColumn gc = (GroupColumn) col;
		if (gc.getName().equals(ArConstants.REAL_DISBURSEMENTS))
		{
			cleanupRealDisbursements((GroupColumn) col, allMandatoryRoles);
			return;
		}
		for(Column column:gc.getItems())
			filterRealDisbursementsSubcolumns(column, allMandatoryRoles);
	}
	
//	/**
//	 * recursively goes all the way down to ColumnReportData and then through columns in order to clean up Real Disbursement's subcolumns of names NOT found in allLegalFlows
//	 * @param rd
//	 * @param allLegalFlows
//	 */
//	protected void filterRealDisbursementsSubcolumns(ReportData rd, Set<String> allLegalFlows)
//	{
//		if (rd instanceof GroupReportData)
//		{
//			GroupReportData grd = (GroupReportData) rd;
//			for(ReportData child_rd:grd.getItems())
//				filterRealDisbursementsSubcolumns(child_rd, allLegalFlows);
//			return;
//		}
//		if (rd instanceof ColumnReportData)
//		{
//			ColumnReportData crd = (ColumnReportData) rd;
//			for(Column col:crd.getItems())
//			{
//				filterRealDisbursementsSubcolumns(col, allLegalFlows);
//			}
//			return;
//		}
//	}

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

		if (!arf.isWidget() && !("N".equals(reportMetadata.getOptions()))) {
			categorizeData();
		}

		if (reportMetadata.getMeasureNames().contains(ArConstants.REAL_DISBURSEMENTS) && !fundingOrgHiers.isEmpty())
		{
			removeUnusedRealDisbursementsFlowsFromReport(fundingOrgHiers);
		}
		
		/**
		 * If we handle a normal report (not tab) and allowEmptyColumns is not set then we need to remove 
		 * empty funding columns
		 */
		if ( ( reportMetadata.getDrilldownTab()==null || !reportMetadata.getDrilldownTab() ) && 
				( reportMetadata.getAllowEmptyFundingColumns() == null || !reportMetadata.getAllowEmptyFundingColumns() ) )
				rawColumns.removeEmptyChildren(true);

		AmountCell.getPercentageCalls = AmountCell.getPercentageIterations = 0;
		AmountCell.merged_cells_get_amount_calls = AmountCell.merged_cells_get_amount_iterations = 0;
		
		report = new GroupReportData(reportMetadata.getName());
		report.setReportGenerator(this);
		report.setReportMetadata(this.reportMetadata);
		report.setReportGenerator(this);
		//report.setSourceColsCount(new Integer(extractableCount - 1));
		report.setColumnsToBeRemoved(columnsToBeRemoved);
		// ensure acess to the report metadata from the raw columns. we should
		// not need this but ...
		rawColumns.setParent(report);

		ColumnReportData reportChild = new ColumnReportData(reportMetadata.getName());

		// add a fake first column ONLY IF the Columns metadata is empty (if we
		// only have hierarchies but no columns). This is needed
		// in order to preserve the table structure of the output
		if (reportMetadata.getShowAblesColumns().size() == 0) {
			reportChild.addColumn(new CellColumn(""));
		}

		reportChild.addColumns(rawColumns.getItems());
		report.addReport(reportChild);
		
		if (reportMetadata.shouldDeleteFunding(arf))
			reportChild.removeColumnsByName(ArConstants.COLUMN_FUNDING);

		// find out if this is a hierarchical report or not:
		if (reportMetadata.getHierarchies().size() != 0)
			createHierarchies();
		
		report.setTotalActualCommitments(this.getTotalActualCommitments());
		// perform postprocessing - cell grouping and other tasks
		report.postProcess();
		report.removeChildrenWithoutActivities(); //postProcess might have created some more empty children
		
		List<Cell> listOfCells = new ArrayList<Cell>();
		report.getAllCells(listOfCells, true);
		rawColumns.getAllCells(listOfCells, true);
		
		rawColumnsByName.clear();
		rawColumnsByName = null;
		rawColumns.getItems().clear();
		rawColumns = null;
		report.removeEmptyChildren();
		
		boolean dateFilterHidesProjects = "true".equalsIgnoreCase(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DATE_FILTER_HIDES_PROJECTS));
								
		if (dateFilterHidesProjects && !reportMetadata.getDrilldownTab() && 
				(this.getFilter().wasDateFilterUsed() || (reportMetadata.getHierarchies().size() > 0))
			)
		{
			// activities without funding are removed ONLY WHEN either a date filter was used OR there is a multi-level hierarchy
			report.cleanActivitiesWithoutFunding();
			//report.cleanActivitiesWithoutFunding();
			// ugly hack for recursive elimination of empty hierarchies... we have a hier depth of at most 5
			for(int i = 0; i < 5; i++)
				report.removeEmptyChildren(); //removeChildrenWithoutActivities might have left empty GroupReportData instances
		}

		// perform postprocessing - cell grouping and other tasks		
		report.postProcess();
		report.removeChildrenWithoutActivities(); //postProcess might have left some more empty children
		removeUnrenderizableData(report); // apply year range filter from settings and any other "non-displaying" filtering which SHOULD NOT affect the report except rendering
		
		report.getAllCells(listOfCells, true); //repeatedly fetch cells, as some might have been added in the meantime (postprocessing)
		if (getCleanupMetadata())
			deleteMetadata(listOfCells);
		//logger.error(report.prettyPrint());
		System.out.format("AmpReportGenerator: AmountCell.getPercentage calls = %d, iterations = %d, iterations / call = %.2f\n", AmountCell.getPercentageCalls, AmountCell.getPercentageIterations, 1.0 * AmountCell.getPercentageIterations / (0.01 + AmountCell.getPercentageCalls));
		System.out.format("AmpReportGenerator: AmountCell.getAmountWithMergedCells calls = %d, iterations = %d, iterations / call = %.2f\n", AmountCell.merged_cells_get_amount_calls, AmountCell.merged_cells_get_amount_iterations, 1.0 * AmountCell.merged_cells_get_amount_iterations / (0.01 + AmountCell.merged_cells_get_amount_calls));
	}

	protected void applyYearRangeSettings(GroupReportData report)
	{
		AmpARFilter filter = this.getFilter();
		for(ReportData item:report.getItems())
		{
			if (item instanceof GroupReportData)
				applyYearRangeSettings((GroupReportData) item);
			if (item instanceof ColumnReportData)
			{ 
				ColumnReportData crd = (ColumnReportData) item;
				Iterator<Column> columns = crd.iterator();
				while (columns.hasNext())
				{
					Column column = columns.next();
					if (column.getColumnId().equals(ArConstants.COLUMN_FUNDING))
					{
						applyYearRangeSettings((GroupColumn)column);
						if (column.items.isEmpty())
							columns.remove();
					}
				}
			}
		}
	}
	
	/**
	 * returns null IFF z is non-parseable as an Integer
	 * @param z
	 * @return
	 */
	public final static Integer getInteger(String z)
	{
		try
		{
			return Integer.parseInt(z);
		}
		catch(Exception e)
		{
			return null;
		}
	}	
	
	/** somewhat stupid heuristic: the first number in the "year" name is a year and we can range on that */
	public final static Integer getYearInteger(String yearStr)
	{
		java.util.StringTokenizer tok = new java.util.StringTokenizer(yearStr);
		while (tok.hasMoreTokens())
		{
			String z = tok.nextToken();
			Integer k = getInteger(z);
			if (k != null)
				return k;
		}
		return null;
	}
	
	protected void applyYearRangeSettings(GroupColumn column)
	{
		Iterator it = column.iterator();
		while (it.hasNext())
		{
			Object item = it.next();
			if (item instanceof Column)
			{
				String yearStr = ((Column) item).getName();
				Integer year = getYearInteger(yearStr);
				if (year != null)
					if (!filter.passesYearRangeFilter(year))
						it.remove();
			}
		} 
	}
		
	protected void removeUnrenderizableData(GroupReportData report)
	{
		applyYearRangeSettings(report);
	}
	
	/**
	 * returns total number of MetaData instances deleted
	 * @param cell
	 * @return
	 */
	protected int clearCellMetadata(Cell cell)
	{
		int mergedCellsSum = 0;
		
		if (cell instanceof AmountCell)
		{
			AmountCell amCell = (AmountCell) cell;
			amCell.setCurrencyDate(null); // unused anymore, eats 120b per instance
			
			amCell.setNullMergedCellsIfEmpty(); // unused at this point, eats 136b per instance
			
			for(Object obj:amCell.getMergedCells())
				mergedCellsSum += clearCellMetadata((AmountCell) obj);
		}
		
		if (cell instanceof CategAmountCell)
			return mergedCellsSum + ((CategAmountCell) cell).clearMetaData();
		
		if (cell instanceof CategCell)	
			return mergedCellsSum + ((CategCell) cell).clearMetaData();
		
		if (cell instanceof ListCell)
		{
			int sum = 0;
			Iterator it = ((ListCell) cell).iterator();
			while (it.hasNext())
				sum += clearCellMetadata((Cell) it.next());
			return mergedCellsSum + sum;
		}
		// cell without metadata
		return mergedCellsSum;
	}
	
	protected void freezeCell(Cell cell)
	{
		if (cell instanceof AmountCell)
			((AmountCell) cell).freeze();
		
		if (cell instanceof ListCell)
		{
			Iterator it = ((ListCell) cell).iterator();
			while (it.hasNext())
				freezeCell((Cell) it.next());
		}
	}
	
	protected void deleteMetadata(List<Cell> cells)
	{
		logger.warn("going to clean up " + cells.size() + " cells...");
		int cl = 0;
		int i = 0;
		int sz = cells.size();
		long a = System.currentTimeMillis();
		for(Cell cell:cells)
		{
			freezeCell(cell);
			i++;
		}
		System.out.format("\t---->deleting %d cells took %d milliseconds\n", sz, System.currentTimeMillis() - a);
		for(Cell cell:cells)
		{
			cl += clearCellMetadata(cell);
		}
		logger.warn("cleaned up " + cl + " metadata instances");
	}
	
	/**
	 * creates Horizontal categories (hierarchies). The column list from
	 * report's metadata is iterated and each column acts as the drivinpg filter
	 * for the split.
	 * 
	 */
	protected void createHierarchies() {
		List<AmpReportHierarchy> orderedHierarchies = ARUtil.createOrderedHierarchies(
				reportMetadata.getShowAblesColumns(), reportMetadata.getHierarchies());
		// add Unallocated fake items for activities missing hierarchy enabled
		// data
		for(AmpReportHierarchy element:orderedHierarchies) {
			Collection<Long> allIds = report.getOwnerIds();
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

			for (Long id:allIds) {
				Cell fakeC = hc.getWorker().newCellInstance();
				fakeC.setOwnerId(id);
				//
				// requirements for translation purposes
				Long siteId = rd.getParent().getReportMetadata().getSiteId();
				String locale = rd.getParent().getReportMetadata().getLocale();
				String text = fakeC.getValue().toString();
				String text2 = c.getColumnName();
				String translatedText = null;
				String translatedText2 = null;
				//String prefix = "aim:reportGenerator:"; not used cos hash keys
				try {
					translatedText = TranslatorWorker.translateText(text, locale, siteId);
					translatedText2 = TranslatorWorker.translateText(text2, locale, siteId);
				} catch (WorkerException e) {
					e.printStackTrace();
				}
				
				fakeC.setValue(translatedText2 + " " + translatedText);
				
				hc.addCell(fakeC);
			}

		}
		// now we can create the hierarchy tree
		for (AmpReportHierarchy element:orderedHierarchies) {
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
		for(AmpReportColumn arc:reportMetadata.getOrderedColumns())
		{
			AmpColumns element = arc.getColumn();
			String colName = element.getColumnName();
			List<String> cats = getColumnSubCategories(element.getColumnName());
			Column c = rawColumns.getColumn(colName);
			if (c instanceof GroupColumn)
				continue; // ugly fix to AMP-2793 and the problem created by
						  // mauricio after generating GroupColumnS before
						  // categorizeData
			CellColumn src = (CellColumn) c;
			if (cats.size() != 0) {
				Column newcol = src.verticalSplitByCategs(cats, true, reportMetadata);
				if (newcol != null)
					rawColumns.replaceColumn(colName, newcol);
			}
		}
	}

	/**
	 * @param reportMetadata
	 * @param condition
	 */
	public AmpReportGenerator(AmpReports reportMetadata, AmpARFilter filter, boolean regenerateFilterQuery) {
		super(USE_FILTER_CACHING ? new FastFilterCacher(filter) : new NopFilterCacher(filter));
		
		if (TLSUtils.getRequest() != null)
			this.session		= TLSUtils.getRequest().getSession();
		else
			this.session = null;
		
		this.reportMetadata = reportMetadata;
		this.reportMetadata.setReportGenerator(this);
		
		rawColumns = new GroupColumn(ArConstants.COLUMN_RAW_DATA);
		this.filter = filter;
		extractableCount = 0;
		
		if (!(reportMetadata.getType()==ArConstants.PLEDGES_TYPE)){
			ReportContextData.getFromRequest().setPledgeReport(false);
		}else {
			pledgereport = true;
			ReportContextData.getFromRequest().setPledgeReport(true);
		}
		
		if (regenerateFilterQuery)
		{
			filter.generateFilterQuery(TLSUtils.getRequest(), false);
			debugMode = (TLSUtils.getRequest().getParameter("debugMode") != null);
		}
		else
			debugMode = false;


		logger.error("Master report query:" + filter.getGeneratedFilterQuery());

		// remove the columns that are also hierarchies

		Iterator i = reportMetadata.getShowAblesColumns().iterator();
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
				reportMetadata.getShowAblesColumns(), reportMetadata.getHierarchies()));

		// attach funding coming from extra sources ... inject funding from
		if (ARUtil.containsColumn(ArConstants.COSTING_GRAND_TOTAL,reportMetadata.getShowAblesColumns())) {
			AmpReportColumn grandTotal = new AmpReportColumn();
			AmpColumns grandTotalColumn = new AmpColumns();
			grandTotal.setColumn(grandTotalColumn);
			grandTotal.setOrderId(0L);
			grandTotalColumn.setCellType("org.dgfoundation.amp.ar.cell.ComputedAmountCell");
			grandTotalColumn.setColumnName(ArConstants.COSTING_GRAND_TOTAL);
			grandTotalColumn.setExtractorView(ArConstants.VIEW_COST);
			ColumnFilterGenerator.attachHardcodedFilters(grandTotalColumn);
			reportMetadata.getOrderedColumns().add(grandTotal);
		}


		attachFundingMeta();
	}
	
	public static TextCell generateFakeCell (ColumnReportData rd, Long activityId, Column column) {
		TextCell fakeC = new TextCell();
		fakeC.setValue(ArConstants.UNALLOCATED);
		fakeC.setOwnerId( activityId );
		
		// requirements for translation purposes
		Long siteId = rd.getParent().getReportMetadata().getSiteId();
		String locale = rd.getParent().getReportMetadata().getLocale();
		String text = fakeC.getValue().toString();
		String translatedText = null;
		String translatedText2 = null;
		//String prefix = "aim:reportGenerator:"; not used cos hash keys
		try {
			translatedText = TranslatorWorker.translateText(text, locale, siteId);
			translatedText2 = TranslatorWorker.translateText(column.getName(), locale, siteId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		fakeC.setValue(translatedText2 + " " + translatedText);
		return fakeC;
	}
	
	public static MetaTextCell generateFakeMetaTextCell(TextCell cell, Double percentage) {
		MetaTextCell fakeC				= new MetaTextCell(cell);
		MetaInfoSet metaSet	= new MetaInfoSet();
		metaSet.add( new MetaInfo<Double>(ArConstants.PERCENTAGE, percentage) );
		fakeC.setMetaData(metaSet);
		return fakeC;
	}

	public boolean getCleanupMetadata()
	{
		return cleanupMetadata;
	}
	
	public void setCleanupMetadata(boolean cleanupMetadata)
	{
		this.cleanupMetadata = cleanupMetadata;
	}
	
	public void setMtefExtractOnlyDonorData(boolean meodd)
	{
		this.mtefExtractOnlyDonorData = meodd;
	}
	
	public boolean getMtefExtractOnlyDonorData()
	{
		return this.mtefExtractOnlyDonorData;
	}
}
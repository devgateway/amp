package org.digijava.kernel.ampapi.mondrian.util;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import mondrian.olap.Util.PropertyList;
import mondrian.spi.DynamicSchemaProcessor;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.mondrian.MondrianETL;
import org.dgfoundation.amp.mondrian.MondrianTablesRepository;
import org.dgfoundation.amp.newreports.AmpReportFilters;
import org.dgfoundation.amp.newreports.CompleteWorkspaceFilter;
import org.dgfoundation.amp.newreports.ReportElement;
import org.dgfoundation.amp.newreports.ReportEnvironment;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportSettingsImpl;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.reports.CustomMeasures;
import org.dgfoundation.amp.reports.mondrian.MondrianReportFilters;
import org.dgfoundation.amp.reports.mondrian.MondrianSQLFilters;
import org.dgfoundation.amp.reports.mondrian.converters.MtefConverter;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryConstants.HardCodedCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.common.util.DateTimeUtil;
import org.hibernate.Session;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AmpMondrianSchemaProcessor implements DynamicSchemaProcessor {
	
	private static ThreadLocal<ReportSpecification> currentReport = new ThreadLocal<>();
	private static ThreadLocal<ReportEnvironment> currentEnvironment = new ThreadLocal<>();
	
	protected static final Logger logger = Logger.getLogger(AmpMondrianSchemaProcessor.class);
	protected static String expandedSchema;
	protected String mondrianFactTable;
	
	/**
	 * whether this is the first, e.g. expanding, run of the processor
	 */
	protected boolean expandingRun;

	@Override
	public String processSchema(String schemaURL, PropertyList connectInfo) throws Exception {
		Session hibSession = PersistenceManager.getSession();
	try {
		String contents = null;
		try(InputStreamReader isr = new InputStreamReader(this.getClass().getResourceAsStream("AMP.xml"), "utf-8")) { 
			try(Scanner scanner = new Scanner(isr)) {
				contents = scanner.useDelimiter("\\Z").next();
			}}
		if (contents == null)
			throw new RuntimeException("could not read schema");

		//If the connection is Stand Alone (coming from Saiku UI), we reset the Report Environment to refresh language
		String standAlone = connectInfo.get("Standalone");
		if (standAlone != null) {
			currentEnvironment.set(null);
			// by default DonorReport will be run
			currentReport.set(null);
		}
		mondrianFactTable = getCubeFactTableName();
		return processContents(contents);
	}
	finally {
		PersistenceManager.cleanupSession(hibSession);
	}
	};
	
	public String processContents(String contents) {
		long schemaProcessingStart = System.currentTimeMillis();
		if (currentReport.get() == null || currentEnvironment.get() == null) {
			logger.warn("currentReport || currentEnvironment == null -> Initializing with default values.");
			// default initialization to allow usage for now the Saiku standalone. Return to previous state or implement better solution.
			initDefault();
		}
		contents = expandSchema(contents);
		contents = contents.replaceAll("@@mondrian_fact_table@@", mondrianFactTable);
		contents = contents.replaceAll("@@actual@@", Long.toString(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getIdInDatabase()));
		contents = contents.replaceAll("@@planned@@", Long.toString(CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getIdInDatabase()));
		contents = contents.replaceAll("@@currency@@", Long.toString(getReportCurrency().getAmpCurrencyId()));
		contents = contents.replaceAll("@@calendar@@", getReportCalendarTag());
		contents = updateDateLimits(contents, getReportSelectedYear());
		contents = configureDatesSource(contents);
		
		// area for (pledges + activities) reports hacks. Holding my nose while writing this - let whatever genius wanted Mondrian as a report engine maintain this PoS :D
		boolean isDonorReportWithPledges = (currentReport.get().getReportType() != ArConstants.PLEDGES_TYPE && currentReport.get().isAlsoShowPledges());
		String nonAcPledgeExcluderString = isDonorReportWithPledges ? "(mondrian_fact_table.entity_id &lt; 800000000) AND " : ""; // annulate non-Actual-Commitments trivial measures IFF running an "also show pledges" report
		String actualCommitmentsDefinition = "__" + (isDonorReportWithPledges ? "Actual Commitments United" : "Actual Commitments Usual") + "__";
		contents = contents.replace(actualCommitmentsDefinition, "Actual Commitments");
		contents = contents.replace("@@non_ac_pledges_excluder@@", nonAcPledgeExcluderString);
		
		/*contents = contents.replace("&lt;", "<");
		contents = contents.replace("&gt;", ">");*/
		
		String localeTag = getReportLocale();
		contents = contents.replaceAll("@@locale@@", localeTag);
		HardCodedCategoryValue pipelineMtefAcv = CategoryConstants.MTEF_PROJECTION_PIPELINE;
		HardCodedCategoryValue projectionMtefAcv = CategoryConstants.MTEF_PROJECTION_PROJECTION;
		
		contents = contents.replaceAll("@@mteffilter@@", buildDateFilteringSubquery(ReportElement.ElementType.MTEF_DATE, "1 = 1"));
		contents = contents.replaceAll("@@pipelinemteffilter@@", buildDateFilteringSubquery(ReportElement.ElementType.PIPELINE_MTEF_DATE, String.format("adjustment_type = %d", pipelineMtefAcv.existsInDatabase() ? pipelineMtefAcv.getIdInDatabase() : 12324))); // crap code but who cares, we're dumping this stuff soon
		contents = contents.replaceAll("@@projectionmteffilter@@", buildDateFilteringSubquery(ReportElement.ElementType.PROJECTION_MTEF_DATE, String.format("adjustment_type = %d", projectionMtefAcv.existsInDatabase() ? projectionMtefAcv.getIdInDatabase() : 12324)));
		
		contents = contents.replaceAll("@@pipelinemteffilternodate@@", String.format("adjustment_type = %d", pipelineMtefAcv.existsInDatabase() ? pipelineMtefAcv.getIdInDatabase() : 12324)); // crap code but who cares, we're dumping this stuff soon
		contents = contents.replaceAll("@@projectionmteffilternodate@@", String.format("adjustment_type = %d", projectionMtefAcv.existsInDatabase() ? projectionMtefAcv.getIdInDatabase() : 12324));

		contents = contents.replaceAll("@@pipelinemtefacv@@", pipelineMtefAcv.existsInDatabase() ? pipelineMtefAcv.getIdInDatabase().toString() : "12324"); // crap code but who cares, we're dumping this stuff soon
		contents = contents.replaceAll("@@projectionmtefacv@@", projectionMtefAcv.existsInDatabase() ? projectionMtefAcv.getIdInDatabase().toString() : "12324");

		// process general filters & custom filters 
		String entityFilteringSubquery = buildFilteringSubquery(); 
		String noDatesEntityFilteringSubquery = getNoDatesFilter(entityFilteringSubquery);
		// order is important, keep it here, do not move up; these date filters tags solution will be soon removed
		entityFilteringSubquery = entityFilteringSubquery.replaceAll(FactTableFiltering.DATE_FILTERS_TAG_START + "|" + FactTableFiltering.DATE_FILTERS_TAG_END, "");
		String computedTotalsFilter = getComputedTotalsFilter();
		
		logger.info("the entity filtering subquery is: " + entityFilteringSubquery);
		logger.info("the noDatesEntityFiltering subquery is: " + noDatesEntityFilteringSubquery);
		logger.info("the computedTotalsFilter subquery is: " + computedTotalsFilter);
		contents = contents.replaceAll("@@filteredActivities@@", entityFilteringSubquery);
		contents = contents.replaceAll("@@filteredActivitiesWithoutDateFilters@@", noDatesEntityFilteringSubquery);
		contents = contents.replaceAll("@@report_totals_filter@@", computedTotalsFilter);
		
		//contents = contents.replaceAll("@@filteredActivities@@", "mondrian_fact_table.entity_id > 0");
		int pos = contents.indexOf("@@");
		if (pos >= 0)
			throw new RuntimeException("your schema contains unrecognized tag: " + contents.substring(pos, contents.indexOf("@@", pos + 2) + 2));

		//logger.info("BEFORE translation the schema is: " + contents);
		contents = translateMeasuresAndColumns(contents, currentEnvironment.get().locale);
		//logger.info("AFTER translation the schema is: " + contents);
		
		long delta = System.currentTimeMillis() - schemaProcessingStart;
		logger.info("schema processing took " + delta + " ms");
		//System.err.println("schema hashcode is: " + contents.hashCode());
		//logger.error("the schema is: " + contents);
		return contents;
	}
	
	protected String getReportCalendarTag() {
//		if (System.currentTimeMillis() > 1)
//			return "_174";
		if (currentReport.get().getSettings().getCalendar() == null)
			return "";
		return "_" + currentReport.get().getSettings().getCalendar().getIdentifier();
	}
	
	protected int getReportSelectedYear() {
		AmpReportFilters filters = (AmpReportFilters) currentReport.get().getFilters();
		Integer year = filters == null ? null : filters.getComputedYear();
		// if not set, then it means Current Year
		if (year == null) {
			year = Calendar.getInstance().get(Calendar.YEAR);
		}
		return year;
	}
	
	/**
	 * translates, using the current locale, the measure / column names
	 * @param contents
	 * @param locale
	 * @return
	 */
	protected String translateMeasuresAndColumns(String contents, String locale) {
		Map<String, String> backMapping = MondrianMapping.fromFullNameToColumnName;
		Set<String> wronglyMappedColumns = new TreeSet<>(backMapping.values());
		Set<String> allLevels = new TreeSet<>();
		
		Document xmlSchema = XMLGlobals.createNewXML(contents);
		
		NodeList measures = XMLGlobals.selectNodes(xmlSchema, "/Schema/Cube/Measure");
		for(int i = 0; i < measures.getLength(); i++) {
			Element elem = (Element) measures.item(i);
			String measureName = elem.getAttribute("name");
			elem.setAttribute("caption", TranslatorWorker.translateText(measureName, locale, 3l));
		}
		
		NodeList dimensions = XMLGlobals.selectNodes(xmlSchema, "/Schema/Cube/Dimension");
		for(int i = 0; i < dimensions.getLength(); i++) {
			Element elem = (Element) dimensions.item(i);
			String dimensionName = elem.getAttribute("name");
			elem.setAttribute("caption", TranslatorWorker.translateText(dimensionName, locale, 3l));
		}

		NodeList columns = XMLGlobals.selectNodes(xmlSchema, "/Schema/Cube/Dimension//Level");
		for(int i = 0; i < columns.getLength(); i++) {
			Element columnElem = (Element) columns.item(i);
			String fullColumnName = buildColumnName(columnElem);
			allLevels.add(fullColumnName);
			String correspondingColumn = backMapping.get(fullColumnName);
			boolean notToBeTranslated = columnElem.hasAttribute("translated") && columnElem.getAttribute("translated").toLowerCase().equals("false");
			if (correspondingColumn == null) {
				if (expandingRun && !notToBeTranslated)
					logger.error("could not find a backmapping for " + fullColumnName + ", this one will not be translated!");
				continue;
			}
			wronglyMappedColumns.remove(correspondingColumn);
			if (notToBeTranslated)
				continue;
			columnElem.setAttribute("caption", TranslatorWorker.translateText(correspondingColumn, locale, 3l));
			//columnElem.setAttribute("name", MondrianMapping.fromFullNameToColumnName.get(fullColumnName)); // NADIA: use for correct originalColumnName implementation
		}
		if (!wronglyMappedColumns.isEmpty()) {
			logger.fatal("the following columns are mapped in the MDX generator without a correspondence in the Schema: " + wronglyMappedColumns);
			throw new RuntimeException("incorrectly-mapped column: " + wronglyMappedColumns.toString());
		}
		return XMLGlobals.saveToString(xmlSchema);
	}
		
	protected String buildColumnName(Element levelElem) {
		Element hierElem = (Element) levelElem.getParentNode();
		Element dimensionElem = (Element) hierElem.getParentNode();
		
		throw_up(levelElem, "Level");
		throw_up(hierElem, "Hierarchy");
		throw_up(dimensionElem, "Dimension");
		
		return String.format("[%s.%s].[%s]", dimensionElem.getAttribute("name"), hierElem.getAttribute("name"), levelElem.getAttribute("name"));
	}
	
	protected void throw_up(Element elem, String tag) {
		if (!elem.getTagName().equals(tag))
			throw new RuntimeException("element has tag " + elem.getTagName() + ", but expected: " + tag);
	}
	
	protected String expandSchema(String contents) {
		if (expandedSchema == null) {
			logger.warn("expanding AMP schema...");
			expandingRun = true;
			Document xmlSchema = XMLGlobals.createNewXML(contents);
			
			//Node dimensionsParentNode = XMLGlobals.selectNode(xmlSchema, "/Schema/Cube");
			while(true) {
				NodeList dimUsages = XMLGlobals.selectNodes(xmlSchema, "//DimensionUsage");
				if (dimUsages.getLength() == 0) break;
				
				Element dimUsage = (Element) dimUsages.item(0);
				String name = dimUsage.getAttribute("name");
				String dim = dimUsage.getAttribute("source");
				String foreignKey = dimUsage.getAttribute("foreignKey");
				//logger.warn(String.format("expanding DimensionUsage: name = %s,  dim = %s, key = %s", name, dim, foreignKey));
				
				Node dimensionDefinition = XMLGlobals.selectNode(xmlSchema, "//Dimension[@name='" + dim + "']");
				Element newDimensionDefinition = (Element) dimensionDefinition.cloneNode(true);
				newDimensionDefinition.setAttribute("foreignKey", foreignKey);
				newDimensionDefinition.setAttribute("name", name);
				
				dimUsage.getParentNode().replaceChild(newDimensionDefinition, dimUsage);
			}
			insertCommonMeasuresDefinitions(xmlSchema);
			moveCalculatedMembersToEnd(xmlSchema);
			removeDefinitionsWithMissingDependencies(xmlSchema);
			contents = XMLGlobals.saveToString(xmlSchema);
			contents = contents.replaceAll("@@undefined_amount@@", MoConstants.UNDEFINED_AMOUNT_STR);
			contents = contents.replaceAll("@@transaction_type_gap@@", MoConstants.TRANSACTION_TYPE_GAP);
			contents = updatePledgeContacts(contents);
			expandedSchema = contents;
			// System.err.println("the expanded schema is: " + expandedSchema);
		}
		//System.err.println("the expanded schema is: " + expandedSchema);
		//expandedSchema = expandedSchema.replace("@@activity_status_key@@", buildActivityStatusSQL());
		return expandedSchema;
	}
	
	/**
	 * blablablaZZZ -> Blablablablazzz
	 * @param in
	 * @return
	 */
	public static String firstLetterUp(String in) {
		return String.format("%s%s", in.substring(0, 1), in.substring(1).toLowerCase());
	}
	
	protected void insertCommonMeasuresDefinitions(Document xmlSchema) {
		Node trivialMeasureDefinitionNode = XMLGlobals.selectNode(xmlSchema, "//Measure[@name='@@trivial_measure@@']");
		Node computedTotDefNode = XMLGlobals.selectNode(xmlSchema, "//Hierarchy[@name='Total Amounts']");
		Node grandTotFilteredDefNode = XMLGlobals.selectNode(xmlSchema, "//Hierarchy[@name='Grand Total Filtered Amounts']");
		
		//String trivialMeasureString = XMLGlobals.saveToString(trivialMeasureDefinitionNode);
		
		// add the default trivial measures
		for (String transactionType:ArConstants.TRANSACTION_TYPE_NAME_TO_ID.keySet()) {
			Integer trTypeId = ArConstants.TRANSACTION_TYPE_NAME_TO_ID.get(transactionType);
			if (trTypeId == Constants.MTEFPROJECTION)
				continue; // MTEFs are handled elsewhere
			for (AmpCategoryValue adj: CategoryManagerUtil.getAmpCategoryValueCollectionByKeyExcludeDeleted(CategoryConstants.ADJUSTMENT_TYPE_KEY)) {
				String measureName = adj.getValue() + " " + transactionType;
				
				insertComputedTotals(computedTotDefNode, adj, trTypeId, transactionType, measureName, false);
				insertComputedTotals(grandTotFilteredDefNode, adj, trTypeId, transactionType, measureName, true);
				
				if (measureName.equals(MoConstants.ACTUAL_COMMITMENTS))
					continue; // this one is hardcoded in AMP.xml for the sake of "pledges + activities" reports				
				
				addTrivialMeasure(trivialMeasureDefinitionNode, measureName, adj, trTypeId);
				addTrivialMeasure(trivialMeasureDefinitionNode, measureName + MoConstants.UNFILTERED_DATE_SUFFIX, adj, trTypeId + Integer.parseInt(MoConstants.TRANSACTION_TYPE_GAP));
			}
		}
		
//		String mtefProjectionName = ArConstants.MTEF_PROJECTION;
//		for(AmpCategoryValue adj:CategoryManagerUtil.getAmpCategoryValueCollectionByKeyExcludeDeleted(CategoryConstants.MTEF_PROJECTION_KEY)) {
//			String measureName = firstLetterUp(adj.getValue()) + " " + mtefProjectionName; 
//			addTrivialMeasure(trivialMeasureDefinitionNode, measureName, adj, Constants.MTEFPROJECTION);
//		}
//		
		// add custom SSC trivial measures
		Collection<AmpCategoryValue> sscAdjTypes = 
				CategoryManagerUtil.getAmpCategoryValueCollectionByKeyExcludeDeleted(CategoryConstants.SSC_ADJUSTMENT_TYPE_KEY);
		 
		if (sscAdjTypes.size() > 0) {
			for (Entry<String, Integer> pair : ArConstants.SSC_TRANSACTION_TYPE_NAME_TO_ID.entrySet()) {
				String transactionType = pair.getKey();
				Integer trTypeId = pair.getValue();
				for (AmpCategoryValue adj : sscAdjTypes) {
					String measureName = adj.getValue() + " " + transactionType;
					addTrivialMeasure(trivialMeasureDefinitionNode, measureName, adj, trTypeId);
				}
			}
		} else {
			// if SSC measures are not used, then we need to cleanup SSC computed measures to have a valid schema definition 
			removeSSCComputedMembers(xmlSchema);
		}
		
		trivialMeasureDefinitionNode.getParentNode().removeChild(trivialMeasureDefinitionNode);
		computedTotDefNode.getParentNode().removeChild(computedTotDefNode);
		grandTotFilteredDefNode.getParentNode().removeChild(grandTotFilteredDefNode);
	}
	
	protected void insertComputedTotals(Node computedTotalsDefinitionNode, AmpCategoryValue adj, 
			Integer trnType, String trnName, String measureName, boolean filtered) {
		if (filtered) {
			measureName = "Grand " + measureName;
		}
		Element newComputedTotals = (Element) computedTotalsDefinitionNode.cloneNode(true);
		newComputedTotals.setAttribute("allMemberName", "All Total " + measureName);
		newComputedTotals.setAttribute("name", "Total " + measureName);
		
		Element viewNode = (Element) XMLGlobals.selectNode(newComputedTotals, "//View");
		viewNode.setAttribute("alias", "v_total_" + measureName.replace(" ", "_").toLowerCase());
		
		Element sqlNode = (Element) XMLGlobals.selectNode(viewNode, "//SQL");
		CDATASection cdata = null;
		for(int pos = 0; pos < sqlNode.getChildNodes().getLength(); pos++) {
			if (sqlNode.getChildNodes().item(pos).getNodeType() == Document.CDATA_SECTION_NODE) {
				cdata = (CDATASection) sqlNode.getChildNodes().item(pos);
				break;
			}
		}
		cdata.setTextContent(cdata.getTextContent()
			.replace("@@adj_name@@", adj.getLabel())
			.replace("@@trn_name@@", trnName)
			.replace("@@adj_id@@", String.valueOf(adj.getId()))
			.replace("@@trn_type@@", String.valueOf(trnType)));
			
		Element levelNode = (Element) XMLGlobals.selectNode(newComputedTotals, "//Level");
		String name = "Total " + measureName;
		levelNode.setAttribute("name", name);
		levelNode.setAttribute("column", name.replace(" ", "_"));
		
		computedTotalsDefinitionNode.getParentNode().appendChild(newComputedTotals);
	}
	
	protected String getComputedTotalsFilter() {
		String filter = "(" + getFilterByReportType(currentReport.get().getReportType()) + ")";
		switch(currentReport.get().getReportType()) {
			case ArConstants.DONOR_TYPE:
			case ArConstants.COMPONENT_TYPE:
				filter += " AND (src_role='DN')";
			break;
		}
		return filter;
	}
	
	private void addTrivialMeasure(Node trivialMeasureDefinitionNode, String measureName, AmpCategoryValue adj,
			Integer trTypeId) {
		Element newMeasureNode = (Element) trivialMeasureDefinitionNode.cloneNode(true);
		newMeasureNode.setAttribute("name", measureName);
		Node sqlNode = XMLGlobals.selectNode(newMeasureNode, "//SQL");
						
		String newNodeText = sqlNode.getTextContent()
				.replace("@@trivial_measure_adjustment_type@@", adj.getId().toString())
				.replace("@@trivial_measure_transaction_type@@", trTypeId.toString());
		sqlNode.setTextContent(newNodeText);
		
		trivialMeasureDefinitionNode.getParentNode().appendChild(newMeasureNode);
	}
	
	private void removeSSCComputedMembers(Document xmlSchema) {
		NodeList calculatedMembers = XMLGlobals.selectNodes(xmlSchema, "//CalculatedMember[contains(@name, 'SSC')]");
		
		for (int idx = 0; idx < calculatedMembers.getLength(); idx++) {
			Node calculatedMember = calculatedMembers.item(idx);
			calculatedMember.getParentNode().removeChild(calculatedMember);
		}
	}
	
	/**
	 * Calculated members depend on existing definitions during the parsing 
	 * and thus has to be defined after other measures
	 * 
	 * @param xmlSchema
	 */
	private void moveCalculatedMembersToEnd(Document xmlSchema) {
		NodeList calculatedMembers = XMLGlobals.selectNodes(xmlSchema, "//CalculatedMember");
		if (calculatedMembers == null || calculatedMembers.getLength() == 0)
			return;
		Node parent = calculatedMembers.item(0).getParentNode();
		for (int idx = 0; idx < calculatedMembers.getLength(); idx++) {
			Node calculatedMember = calculatedMembers.item(idx);
			parent.removeChild(calculatedMember);
			parent.appendChild(calculatedMember);
		}
	}
	
	protected void removeDefinitionsWithMissingDependencies(Document xmlSchema) {
		// remove calculated members whose dependencies are missing
		for (Entry<String, List<String>> pair : CustomMeasures.MEASURE_DEPENDENCY.entrySet()) {
			for (String measure : pair.getValue()) {
				if (!MondrianMapping.definedMeasures.contains(measure)) {
					Node calculatedMember = XMLGlobals.selectNode(xmlSchema, "//CalculatedMember[@name='" + pair.getKey() + "']");
					calculatedMember.getParentNode().removeChild(calculatedMember);
					break;
				}
			}
		}
	}
	
//	protected String buildActivityStatusSQL() {
//		StringBuilder res = new StringBuilder("CASE");
//		//CASE WHEN approval_status='approved' THEN 1 WHEN approval_status='startedapproved' THEN 2 ELSE NULL END
//		//mondrian_activity_fixed_texts.approval_status
//		for(String status:AmpARFilter.activityStatusToNr.keySet()) {
//			int key = AmpARFilter.activityStatusToNr.get(status);
//			res.append(String.format(" WHEN mondrian_activity_texts.approval_status='%s' THEN %d", status, key));
//		}
//		res.append(" ELSE 999999999 END");
//		return res.toString();
//	}
	
	protected String getReportLocale() {
		return "_" + currentEnvironment.get().locale;
	}
	
	protected AmpCurrency getReportCurrency() {
		String currencyCode = currentReport.get().getSettings().getCurrencyCode();
		if (currencyCode == null)
			currencyCode = currentEnvironment.get().defaultCurrencyCode;
		AmpCurrency res = CurrencyUtil.getCurrencyByCode(currencyCode);
		return res;
	}
	
	protected String buildDateFilteringSubquery(ReportElement.ElementType elementType, String transactionTypeFilteringQuery) {
		MondrianReportFilters mrf = (currentReport.get().getFilters() != null) ? (MondrianReportFilters) currentReport.get().getFilters() : null;
		String ret = new FactTableFiltering(mrf).buildDateFilteringFragment(elementType, transactionTypeFilteringQuery);
		return ret;
	}
	
	/**
	 * computes the SQL subquery to be used for filtering purposes (workspace filter, report filter)
	 * @return
	 */
	protected String buildFilteringSubquery() {
		MondrianReportFilters mrf = (currentReport.get().getFilters() != null) ? (MondrianReportFilters) currentReport.get().getFilters() : null;
		String ret = String.format("(%s) %s", buildEntityIdFilteringSQL(mrf), new FactTableFiltering(mrf).getQueryFragment());
		return ret;
	}

	protected String getFilterByReportType(int reportType) {
		switch (reportType) {
		
			case ArConstants.DONOR_TYPE:
				return String.format("%s.component_id IS NULL OR %s.component_id = 999999999", mondrianFactTable, mondrianFactTable);

			case ArConstants.COMPONENT_TYPE:
				return String.format("%s.component_id IS NOT NULL AND %s.component_id <> 999999999", mondrianFactTable, mondrianFactTable);

			case ArConstants.PLEDGES_TYPE:
				return String.format("%s.entity_id >= " + MondrianETL.PLEDGE_ID_ADDER, mondrianFactTable);
						
			default:
				throw new RuntimeException("report type not implemented yet: " + reportType);
		}
	}
	
	/**
	 * constructs an SQL inline statement of the form <<(%s) AND (mondrian_fact_table.entity_id IN (%s))>>
	 * @param mrf
	 * @return
	 */
	protected String buildEntityIdFilteringSQL(MondrianReportFilters mrf) {
		int reportType = currentReport.get().getReportType();
		String entityFilteringSubquery = getAllowedActivitiesSubquery(mrf, reportType);
		String ret = currentReport.get().isAlsoShowPledges() ? 
				String.format("((%s) AND (%s)) OR (%s)", getFilterByReportType(reportType), entityFilteringSubquery, getFilterByReportType(ArConstants.PLEDGES_TYPE)) :
				String.format("(%s) AND (%s)", getFilterByReportType(reportType), entityFilteringSubquery);
		return ret;
	}
	
	protected String getAllowedActivitiesSubquery(MondrianReportFilters mrf, int reportType) {
		List<Set<Long>> sets = new ArrayList<>(); // list of all sets of ids which we'll have to intersect
		
		switch(reportType) {
			case ArConstants.PLEDGES_TYPE:
				if (currentEnvironment.get().pledgesFilter != null)
					sets.add(currentEnvironment.get().pledgesFilter.getIds()); // pledges don't obey workspace filters
				break;
				
			default:
				if (currentEnvironment.get().workspaceFilter != null)
					sets.add(currentEnvironment.get().workspaceFilter.getIds());
				break;
		}			
		
		if (mrf != null) {
			Set<Long> filteredIds = MondrianSQLFilters.getActivityIds(mrf);
			if (filteredIds != null)
				sets.add(filteredIds);
		}
		
		if (sets.isEmpty())
			return "1 = 1"; // no filtering to do whatsoever
		
		// compute intersection of all the sets in <sets>
		Set<Long> result = new HashSet<>(sets.get(0));
		for(int i = 1; i < sets.size(); i++)
			result.retainAll(sets.get(i));
		return String.format(mondrianFactTable + ".entity_id IN (%s)", Util.toCSStringForIN(result));
	}
	
	/**
	 * Removes dates filters
	 * @param entityFilteringSubquery
	 * @return
	 */
	protected String getNoDatesFilter(String entityFilteringSubquery) {
		String noDatesFilter = entityFilteringSubquery.replaceAll(FactTableFiltering.DATE_FILTERS_PATTERN, "");
		if ("()".equals(noDatesFilter.trim())) {
			noDatesFilter = "(1 = 1)";
		}
		return noDatesFilter;
	}
	
	/**
	 * this should be called before each and every report run using Mondrian
	 * @param spec
	 */
	public static void registerReport(ReportSpecification spec, ReportEnvironment environment) {
		currentReport.set(spec);
		currentEnvironment.set(environment);
	}

	private void initDefault() {
		ReportSpecificationImpl spec = new ReportSpecificationImpl("default", ArConstants.DONOR_TYPE);
		ReportSettingsImpl settings = new ReportSettingsImpl(null);
		spec.setSettings(settings);

		registerReport(spec, buildReportEnvironment());
	}
	
	private ReportEnvironment buildReportEnvironment() {
		if (RequestContextHolder.getRequestAttributes() != null) {
			ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			return ReportEnvironment.buildFor(sra.getRequest());
		}
		return new ReportEnvironment("en", new CompleteWorkspaceFilter(null, null), "EUR");
	}
	
	/**
	 * Update formulas with dates
	 * @param contents schema content
	 * @return updated schema content
	 */
	private String updateDateLimits(String contents, int selectedYear) {
		Calendar c = Calendar.getInstance();
		
		// calculate the end of the last month (e.g. now is /03/2015, then this will be /02/2015)
		c.set(Calendar.DAY_OF_MONTH, 1); // => 01/03/2015
		c.add(Calendar.DAY_OF_MONTH, -1); // => 28/02/205
		contents = contents.replaceAll("@@last_month_end@@", DateTimeUtil.toJulianDayString(c.getTime()));
		
		// calculate the end of the month before last month (this will be /01/2015)
		c.add(Calendar.DAY_OF_MONTH, 1); // => 01/03/2015
		c.add(Calendar.MONTH, -1); // => 01/02/2015
		c.add(Calendar.DAY_OF_MONTH, -1); // => 31/01/2015
		contents = contents.replaceAll("@@month_before_last_month_end@@", DateTimeUtil.toJulianDayString(c.getTime()));
		
		// calculate the beginning of the year date
		c.set(Calendar.DAY_OF_YEAR, 1);
		c.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR)); // reconfigure explicitly the year in case it moved back
		contents = contents.replaceAll("@@current_year_start@@", DateTimeUtil.toJulianDayString(c.getTime()));
		
		// calculate the selected year start & end
		c.set(Calendar.YEAR, selectedYear);
		contents = contents.replaceAll("@@selected_year_start@@", DateTimeUtil.toJulianDayString(c.getTime()));
		c.add(Calendar.YEAR, 1);
		c.add(Calendar.DAY_OF_YEAR, -1);
		contents = contents.replaceAll("@@selected_year_end@@", DateTimeUtil.toJulianDayString(c.getTime()));
		
		return contents;
	}
	
	protected String updatePledgeContacts(String contents) {
		String startStr = "<!-- Pledge Contacts - START -->";
		String endStr = "<!-- Pledge Contacts - END -->";
		String contactsGeneric = contents.substring(contents.indexOf(startStr), contents.indexOf(endStr) + endStr.length());
		String contacts1 = contactsGeneric.replace("@@nr@@", "1");
		String contacts2 = contactsGeneric.replace("@@nr@@", "2");		
		contents = contents.replace(contactsGeneric, (contacts1 + contacts2).replace(startStr, "").replace(endStr, ""));
		return contents;
	}
	
	/**
	 * @return the actual name of the fact table or fact view
	 */
	protected String getCubeFactTableName() {
		ReportSpecification spec = currentReport.get();
		String factTable = spec == null ? MondrianTablesRepository.FACT_TABLE_VIEW_NO_DATE_FILTER : MondrianTablesRepository.FACT_TABLE.tableName; // so that Saiku report wizard can use all the fancy hacky no-date-filters measures
		if (spec != null && spec.getMeasures() != null) {
			for (ReportMeasure m : spec.getMeasures()) {
				if (CustomMeasures.NO_DATE_FILTERS.contains(m.getMeasureName())) {
					factTable = MondrianTablesRepository.FACT_TABLE_VIEW_NO_DATE_FILTER;
					break;
				}
			}
		}
		if (spec != null && spec.getColumnNames().contains(ColumnConstants.FORECAST_EXECUTION_RATE)) {
			factTable = MondrianTablesRepository.FACT_TABLE_VIEW_NO_DATE_FILTER;
		}
		return factTable;
	}
	
	protected String configureDatesSource(String contents) {
		String mondrian_date_source = "@@mondrian_date_source%d@@";
		String datesSource = null;
		if (hasMTEFs()) {
			datesSource = "<Table name=\"mondrian_dates\" />";
		} else {
			String minCode = String.valueOf(MtefConverter.getMtefStartDayCode());
			String maxCode = String.valueOf(MtefConverter.getMtefEndDayCode());
			datesSource = "<View alias=\"v_mondrian_dates_%d\">"
				+ "				<SQL dialect=\"generic\">"
				+ "					<![CDATA["
				+ "					SELECT * FROM mondrian_dates WHERE NOT (day_code >=" + minCode + " AND day_code <= " + maxCode +")"
				+ "					]]>"
				+ "				</SQL>"
				+ "			</View>";
		}
		for (int refId = 1; refId <= 4; refId++) {
			contents = contents.replaceAll(String.format(mondrian_date_source, refId), String.format(datesSource, refId));
		}
		return contents;
	}
	
	protected boolean hasMTEFs() {
		Set<ReportMeasure> measures = currentReport.get().getMeasures();
		if (measures != null && measures.size() > 0) {
			for (ReportMeasure measure : measures) {
				if (CustomMeasures.MTEFs.contains(measure.getMeasureName())) {
					return true;
				}
			}
		}
		return false;
	}
}

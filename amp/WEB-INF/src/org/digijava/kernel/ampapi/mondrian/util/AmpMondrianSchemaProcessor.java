package org.digijava.kernel.ampapi.mondrian.util;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import mondrian.olap.Util.PropertyList;
import mondrian.spi.DynamicSchemaProcessor;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.WorkspaceFilter;
import org.dgfoundation.amp.newreports.NamedTypedEntity;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportEnvironment;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.reports.mondrian.MondrianDBUtils;
import org.dgfoundation.amp.reports.mondrian.MondrianReportFilters;
import org.dgfoundation.amp.reports.mondrian.MondrianReportSettings;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXElement;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AmpMondrianSchemaProcessor implements DynamicSchemaProcessor {
	
	private static ThreadLocal<ReportSpecification> currentReport = new ThreadLocal<>();
	private static ThreadLocal<ReportEnvironment> currentEnvironment = new ThreadLocal<>();
	
	protected static final Logger logger = Logger.getLogger(AmpMondrianSchemaProcessor.class);
	protected static String expandedSchema;

	@Override
	public String processSchema(String schemaURL, PropertyList connectInfo) throws Exception {
		String contents = null;
		try(InputStreamReader isr = new InputStreamReader(this.getClass().getResourceAsStream("AMP.xml"), "utf-8")) { 
			try(Scanner scanner = new Scanner(isr)) {
				contents = scanner.useDelimiter("\\Z").next();
			}}
		if (contents == null)
			throw new RuntimeException("could not read schema");
		return processContents(contents);
	};
	
	public String processContents(String contents) {
		long schemaProcessingStart = System.currentTimeMillis();
		if (currentReport.get() == null || currentEnvironment.get() == null) {
			logger.warn("currentReport || currentEnvironment == null -> Initializing with default values.");
			//TODO: Added a default initialization to allow usage for now the Saiku standalone. Return to previous state or implement better solution.
			//Added a try/catch to avoid startup errors.
			//the try/catch was effectively swallowing fatal errors which would appear in proper reports at production time. the correct way to treat them is to remove the source of the exception, not to ignore it
			initDefault();
		}
		contents = expandSchema(contents);		
		contents = contents.replaceAll("@@actual@@", Long.toString(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getIdInDatabase()));
		contents = contents.replaceAll("@@planned@@", Long.toString(CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getIdInDatabase()));
		contents = contents.replaceAll("@@currency@@", Long.toString(getReportCurrency().getAmpCurrencyId()));
		contents = contents.replaceAll("@@calendar@@", getReportCalendarTag());
		
		String localeTag = getReportLocale();
		contents = contents.replaceAll("@@locale@@", localeTag);
		
		contents = contents.replaceAll("@@filteredActivities@@", "mondrian_fact_table.entity_id IN (" + getAllowedActivitiesIds() + ")");
		//contents = contents.replaceAll("@@filteredActivities@@", "mondrian_fact_table.entity_id > 0");
		int pos = contents.indexOf("@@");
		if (pos >= 0)
			throw new RuntimeException("your schema contains unrecognized tag: " + contents.substring(pos, contents.indexOf("@@", pos + 2) + 2));

		//logger.info("BEFORE translation the schema is: " + contents);
		contents = translateMeasuresAndColumns(contents, currentEnvironment.get().locale);
		//logger.info("AFTER translation the schema is: " + contents);
		
		long delta = System.currentTimeMillis() - schemaProcessingStart;
		logger.info("schema processing took " + delta + " ms");
		return contents;
	}
	
	protected String getReportCalendarTag() {
		if (currentReport.get().getSettings().getCalendar() == null)
			return "";
		return "_" + currentReport.get().getSettings().getCalendar().getAmpFiscalCalId();
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
		
		NodeList columns = XMLGlobals.selectNodes(xmlSchema, "/Schema/Cube/Dimension//Level");
		for(int i = 0; i < columns.getLength(); i++) {
			Element columnElem = (Element) columns.item(i);
			String fullColumnName = buildColumnName(columnElem);
			allLevels.add(fullColumnName);
			String correspondingColumn = backMapping.get(fullColumnName);
			boolean notToBeTranslated = columnElem.hasAttribute("translated") && columnElem.getAttribute("translated").toLowerCase().equals("false");
			if (correspondingColumn == null) {
				if (!notToBeTranslated)
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
			expandedSchema = XMLGlobals.saveToString(xmlSchema);
		}
		//expandedSchema = expandedSchema.replace("@@activity_status_key@@", buildActivityStatusSQL());
		return expandedSchema;
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
		AmpCurrency res = CurrencyUtil.getCurrencyByCode(currentReport.get().getSettings().getCurrencyCode());
		return res;
	}
	
	protected String getAllowedActivitiesIds() {
		TeamMember tm = currentEnvironment.get().viewer;
		Set<Long> allowedActivities = ActivityUtil.fetchLongs(WorkspaceFilter.generateWorkspaceFilterQuery(tm));
		if (currentEnvironment.get().workspaceFilter != null) {
			Set<Long> wfIds = ActivityUtil.fetchLongs(currentEnvironment.get().workspaceFilter.getGeneratedFilterQuery());
			allowedActivities.addAll(wfIds);
		}
		
		if (currentReport.get().getFilters() != null) {
			String dateFiltersQuery = MondrianDBUtils.generateDateColumnsFilterQuery(((MondrianReportFilters)currentReport.get().getFilters()).getDateFilterRules());
			if (dateFiltersQuery != null)
				allowedActivities.retainAll(ActivityUtil.fetchLongs(dateFiltersQuery));
		}

		return Util.toCSStringForIN(allowedActivities);
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
		ReportSpecificationImpl spec = new ReportSpecificationImpl("default");
		MondrianReportSettings settings = new MondrianReportSettings();
		settings.setCurrencyCode("EUR");
		spec.setSettings(settings);
		registerReport(spec, new ReportEnvironment("en", null, null));
	}

}

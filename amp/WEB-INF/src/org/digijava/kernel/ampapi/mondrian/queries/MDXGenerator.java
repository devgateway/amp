/**
 * 
 */
package org.digijava.kernel.ampapi.mondrian.queries;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.error.keeper.ErrorReportingPlugin;
import org.digijava.kernel.ampapi.exception.AmpApiException;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXAttribute;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXConfig;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXElement;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXFilter;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXLevel;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXMeasure;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXTuple;
import org.digijava.kernel.ampapi.mondrian.util.Connection;
import org.digijava.kernel.ampapi.mondrian.util.MoConstants;
import org.digijava.kernel.ampapi.mondrian.util.MondrianMapping;
import org.digijava.kernel.ampapi.mondrian.util.MondrianUtils;
import org.olap4j.CellSet;
import org.olap4j.OlapConnection;
import org.olap4j.OlapException;
import org.olap4j.mdx.parser.MdxParser;
import org.olap4j.mdx.parser.MdxValidator;
import org.olap4j.metadata.Cube;
import org.olap4j.metadata.NamedList;
import org.olap4j.query.SortOrder;

/**
 * MDX Query Generator
 * @author Nadejda Mandrescu
 *
 */
public class MDXGenerator {
	// when properties are configured on main hierarchy, then it slows down some queries that are using all members 
	private static final boolean PROPERTIES_FILTERING_ON_WHERE = false;
	private static final MDXLevel mainDates = new MDXLevel(MoConstants.DATES, MoConstants.H_DATES, MoConstants.ATTR_DATE);
	
	protected static final Logger logger = Logger.getLogger(MDXGenerator.class); 

	private OlapConnection olapConnection = null;
	private MdxParser parser = null;
	private MdxValidator validator = null;
	
	public MDXGenerator() throws AmpApiException {
		setup();
	}
	
	private void setup() throws AmpApiException {
		try {
			this.olapConnection = Connection.getOlapConnectionByConnPath(Connection.getDefaultConnectionPath());
		} catch (Exception e) {
			logger.error("Cannot create OlapConnection using connectionPath = " + Connection.getConnectionBySchemaPath(MoConstants.SCHEMA_PATH));
			throw new RuntimeException(e);
		}
		this.parser = olapConnection.getParserFactory().createMdxParser(olapConnection);
		this.validator = olapConnection.getParserFactory().createMdxValidator(olapConnection);
	}
			
	public void tearDown() {
		try {
			this.olapConnection.close();
		} catch (SQLException e) {
			logger.error(ErrorReportingPlugin.getSQLExceptionMessage(e));
		}
	}
	
	private Cube getCube(MDXConfig config) throws AmpApiException {
		String cubeName = config.getCubeName();
		NamedList<Cube> cubes = null;
		Cube cube = null;
		//get the Cube reference
		try {
			cubes = olapConnection.getOlapSchema().getCubes(); 
		} catch (OlapException e) {
			logger.error("Cannot get cubes list from Mondrian Schema");
			throw new AmpApiException(AmpApiException.MONDRIAN_ERROR, false, e);
		}
		cube = cubes.get(cubeName);
		if (cube==null) {
			throw new AmpApiException("Cube '" + cubeName + "' not found!");
		}
		return cube;
	}
	
	/**
	 * Builds the query using advanced options that are not available via Olap4j API
	 * @param config provides data input for MDX query generation
	 * @return MDX string
	 * @throws AmpApiException
	 */
	public String getAdvancedOlapQuery(MDXConfig config) throws AmpApiException {
		//we are not using org.olap4j.query api because now it is experimental and has some limitations
		logger.info("Building MDX query for '" + config.getMdxName() + "' mdx config");
		
		//Cube cube = getCube(config);
		String cubeName = config.getCubeName();
		
		StringBuilder with = new StringBuilder("");
		String select = "SELECT ";
		String from = " FROM [" + cubeName + "]";
		String columns = " ON COLUMNS";
		String rows = " ON ROWS ";
		String where = "";
		String mdx = "";
		String notEmptyColumns = ""; // provide always all group of measures //config.isAllowColumnsEmptyData() ? "" : "NON EMPTY ";
		String notEmptyRows = config.isAllowRowsEmptyData() ? "" : "NON EMPTY ";
		
		adjustDateFilters(config);

		String axisMdx = null;
		
		/* COLUMNS */
		//if column attributes are not configured, display only measures
		axisMdx = getColumns(config, with);
		columns  =  notEmptyColumns + axisMdx + columns;
		
		/* ROWS */
		axisMdx = getRows(config, with);
		
		if (axisMdx == null) {//no attributes are defined on rows
			rows = "";
		} else {
			//filter axis by measures
			if (config.getAxisFilters().size()>0) {
				for (Map.Entry<MDXElement, List<MDXFilter>> pair : config.getAxisFilters().entrySet()) {
					if (pair.getKey() instanceof MDXMeasure) {
						axisMdx = toFilter(axisMdx, pair.getKey(), pair.getValue(), false);
					}
				}
	 		}
			//sorting
			if (config.getSortingOrder().size() > 0) {
				axisMdx = sorting(config.getSortingOrder(), axisMdx);
			}
			
			rows  = notEmptyRows + axisMdx + rows;
			columns += ", ";
		}
		
		/* WHERE  (slice data, aka filters in Reports) */
		where = getWhere(config, with);
		
		mdx = (with.length() > 0 ? "WITH " + with.toString() + " " : "") + select + columns + rows + from + where;
		
		logger.info("[" + config.getMdxName() + "] MDX query: " + mdx);
		validate(mdx, config.getMdxName());
			
		return mdx;
	}
	
	/**
	 * Builds 'ON COLUMNS' MDX query
	 * @param config - MDXConfig
	 * @param with - the 'WITH' string to be prefixed to MDX select
	 * @return 'ON COLUMNS' MDX query portion
	 * @throws AmpApiException
	 */
	private String getColumns(MDXConfig config, StringBuilder with) throws AmpApiException {
		if (config.getColumnMeasures().isEmpty())
			throw new AmpApiException("MDX Generation error: No measure specified for Columns");
		
		String axisMdx = null;
		
		String measures = "";
		String totalMeasures = "";
		
		boolean format = StringUtils.isNotBlank(config.getAmountsFormat());
		Map<MDXMeasure, String> measureTotalMemberMap = new HashMap<MDXMeasure, String>();
		Map<String, String> measureTotalMemberDefinitionMap = new HashMap<String, String>();
		for (MDXMeasure colMeasure : config.getColumnMeasures()) {
			measures += "," + (format ? addMeasureFormat(config, colMeasure, with) : colMeasure.toString()); 
			//define measure totals
			String measureTotalMember = MoConstants.MEASURES + "." + MDXElement.quote("Total " + colMeasure.getName());
			totalMeasures += "," + measureTotalMember;
			//by default no special formular is needed, just using the measure itself
			String measureTotalMemberDefinition = colMeasure.toString();
			measureTotalMemberMap.put(colMeasure, measureTotalMember);
			measureTotalMemberDefinitionMap.put(measureTotalMember, measureTotalMemberDefinition);
		}
		
		String measuresStr = "{" + measures.substring(1) + "}";
		axisMdx = measuresStr;
		totalMeasures = "{" + totalMeasures.substring(1) + "}";
		
		if (config.getColumnAttributes().size()==0) {
			//aka simple summary totals report or can be used by dashboards/reports charts (e.g. pie chart)
			/*
			 * No need to wrap into Hierarchize function, which orders measures by their AMP.xml order. 
			 * Leaving them by the configuration order. 
			axisMdx = "{" + MoConstants.FUNC_HIERARCHIZE + "(" + axisMdx + ")}";
			*/
		} else {
			List<String> dataFilterSets = new ArrayList<String>();
			axisMdx = getAxis(config, config.getColumnAttributes().listIterator(config.getColumnAttributes().size()), 
					with, "COLSET", axisMdx, config.isDoColumnsTotals(), 
					Math.min(config.getColsHierarchiesTotals(), config.getColumnAttributes().size()-1) , dataFilterSets);
			if (config.isAllowColumnsEmptyData()) {
				axisMdx = customEmptyQuarterAndMonth(config, with, axisMdx, measuresStr);
			}
			
			//if there are data filters applied directly on columns, then we need to update total measures member definition
			if (dataFilterSets.size() > 0) {
				//build formulae to be applied to all measures
				StringBuilder formulae = new StringBuilder();
				//TODO: TBD if for all measures SUM function is applicable
				formulae.append("Sum(");
				for (String dataFilterName : dataFilterSets) {
					formulae.append(MoConstants.FUNC_NON_EMPTY_CROSS_JOIN).append("(").append(dataFilterName).append(",");
				}
				formulae.append("%s").append(")"); // ) enclosing from sum
				for (int i = 0; i < dataFilterSets.size(); i++)
					formulae.append(")");
				//replace total measure member definition 
				for (MDXMeasure measure : config.getColumnMeasures()) {
					String measureTotalMember = measureTotalMemberMap.get(measure);
					String newMemberDef = String.format(formulae.toString(), measure.toString());
					measureTotalMemberDefinitionMap.put(measureTotalMember, newMemberDef);
				}
			}
		}
		
		if (config.isDoColumnsTotals()) {
			//replace last occurrence of measures set for total measures
			int lastMeasuresPos = axisMdx.lastIndexOf(measuresStr); 
			axisMdx = axisMdx.substring(0, lastMeasuresPos) + totalMeasures + axisMdx.substring(lastMeasuresPos + measuresStr.length());
			
			for (Entry<String, String> pair : measureTotalMemberDefinitionMap.entrySet()) {
				with.append(" ").append(MoConstants.MEMBER).append(" ").append(pair.getKey()).append(" AS ").append(pair.getValue());
			}
		}
		
		return axisMdx;
	}
	
	/**
	 * This is a custom approach to enable empty columns for quarters or months
	 * @param config
	 * @param with
	 * @param axisMdx
	 * @param measures
	 * @return
	 */
	private String customEmptyQuarterAndMonth(MDXConfig config, StringBuilder with, String axisMdx, String measures) {
		/*
		 * This is a custom approach.
		 * Assumption: we request empty columns only for quarters and months.
		 * Empty measures columns are always provided.
		 * Empty years are never provided.
		 * 
		 * The requirement is custom, thus there is no generic rule here
		 */
		
		// assumption that the columns group starts with year column - checking it
		if (config.getColumnAttributes() == null || config.getColumnAttributes().size() == 0 
				|| !config.getColumnAttributes().iterator().next().getName().equals(MoConstants.ATTR_YEAR)) {
			logger.error("Invalid MDXConfig: isAllowColumnsEmptyData = " + config.isAllowColumnsEmptyData() 
					+ " and column attributes: " + config.getColumnAttributes()); 
			return axisMdx;
		}
		
		axisMdx = axisMdx.replaceAll(MoConstants.FUNC_NON_EMPTY_CROSS_JOIN, MoConstants.FUNC_CROSS_JOIN);
		
		MDXAttribute yearAttr = config.getColumnAttributes().iterator().next();
		
		// get 1st cross join element (it can be either years column, either years filter)
		String years = axisMdx.substring(axisMdx.indexOf('(') + 1, axisMdx.indexOf(',')).trim();
		String yearsSet = "CUSTOM_YEARS_SET";
		with.append("SET ").append(yearsSet).append(" AS ").append(MoConstants.FUNC_EXTRACT).append("(")
		.append(MoConstants.FUNC_NON_EMPTY_CROSS_JOIN).append("(").append(years).append(", ")
		.append(measures).append("), ").append(yearAttr.getDimensionAndHierarchy()).append(")");
		
		axisMdx = axisMdx.replace(years, yearsSet);
		
		return axisMdx;
	}
	
	/**
	 * Adds formated measure member definition
	 * @param config - MDX configuration
	 * @param measure - measure to format
	 * @param with - will store formatted member definition
	 * @return member name of the formatted measure
	 */
	private String addMeasureFormat(MDXConfig config, MDXMeasure measure, StringBuilder with) {
		String formattedMeasureName = MoConstants.MEASURES + "." + MDXElement.quote(measure.getName() + " Formatted");
		
		with.append(" ").append(MoConstants.MEMBER).append(" ").append(formattedMeasureName)
		.append(" AS ").append("'").append(measure.toString()).append("', ")
		.append(MoConstants.PROP_FORMAT_STRING).append("=").append("'").append(config.getAmountsFormat()).append("'");
		
		return formattedMeasureName;
	}
	
	/**
	 * Builds 'ON ROWS' MDX query
	 * @param config - MDX Config
	 * @param with - the 'WITH' string to be prefixed to MDX select 
	 * @return 'ON ROWS' MDX query portion
	 * @throws AmpApiException
	 */
	private String getRows(MDXConfig config, StringBuilder with) throws AmpApiException {
		//if no attributes are configured on rows (e.g. summary report), the just exist
		if (config.getRowAttributes() == null || config.getRowAttributes().size() == 0) return null;
		//assumption: only attributes are displayed per rows, no measures
		return getAxis(config, config.getRowAttributes().listIterator(config.getRowAttributes().size()), 
				with, "ROWSET", null, config.isDoRowTotals(), 
				Math.min(config.getRowsHierarchiesTotals(), config.getRowAttributes().size() - 1), null);
	}
	
	/** supporting common method used by getRows and getColumns, because the algorithm is the same */
	private String getAxis(MDXConfig config, ListIterator<MDXAttribute> reverseIterator, StringBuilder with, String withPrefix,
							String initMember, boolean doTotals, int numSubTotals, List<String> dataFilterSets) throws AmpApiException {
		int setId = 0; //identifier suffix for 'with' member name
		String crossJoin = initMember;
		String prevAttrAll = initMember; //used if totals must be calculated
		
		for (ListIterator<MDXAttribute> iter = reverseIterator; iter.hasPrevious(); ) {
			MDXAttribute rowAttr = iter.previous();
			String attrNode = null;
			String attrAll = doTotals || numSubTotals > 0 ? getAll(rowAttr) : null;
			String all = (crossJoin == null && numSubTotals > 0) ? "{ %s, " + attrAll + "}" : "%s"; //build all in first crossJoin only for last element
			//build a separate rowset to have a more readable MDX 
			attrNode = configureFilters(config, rowAttr, with, withPrefix + (++setId), all, attrAll, dataFilterSets);
			if (attrNode == null) {
				attrNode = String.format(all, rowAttr.toString());
			}
			if (crossJoin == null) { 
				crossJoin = attrNode; //note: if 1 row element, then no crossjoins/unions are needed and this element will be simply returned  
				if (attrAll != null) prevAttrAll = attrAll;
			} else {
				//for query with no totals, it is enough to do cross join 
				crossJoin = String.format(MoConstants.FUNC_NON_EMPTY_CROSS_JOIN_FORMAT, attrNode, crossJoin);
				//for query with totals, we need to cross join 'all' members and make a union with standard members cross join
				if (doTotals) {
					prevAttrAll = String.format(MoConstants.FUNC_NON_EMPTY_CROSS_JOIN_FORMAT, attrAll, prevAttrAll);
				}
				if (numSubTotals > 0 || !iter.hasPrevious() && doTotals) {
					crossJoin = String.format(MoConstants.FUNC_UNION_FORMAT, crossJoin, prevAttrAll); //totals are displayed after level's members
				}
			}
			numSubTotals--;
		}
		return crossJoin;
	}
	
	/**
	 * Configures the filter sets, which filters to be used for visual filters and which one for all data filters, i.e. including totals 
	 * @param config - MDXConfig
	 * @param mdxAttr - the attribute to configure the filters for
	 * @param with - WITH clause StringBuilder that keeps track of all SETS or MEMBERS
	 * @param suffix
	 * @param visualFormat - the format string (with or without Totals)
	 * @return row filter set name or null if no filter is applied
	 * @throws AmpApiException 
	 */
	private String configureFilters(MDXConfig config, MDXAttribute mdxAttr, StringBuilder with, String suffix, 
			String visualFormat, String attrAll, List<String> dataFilters) throws AmpApiException {
		List<MDXFilter> mdxAxisFilter = findAndRemoveFilter(config.getAxisFilters(), mdxAttr);
		List<MDXFilter> mdxDataFilter = findAndRemoveFilter(config.getDataFilters(), mdxAttr);
		MDXAttribute mdxLevelFilter = findAndRemoveFilter(config.getLevelFilters(), mdxAttr);
		boolean hasAxisFilter = mdxAxisFilter != null && mdxAxisFilter.size() > 0;
		boolean hasDataFilter = mdxDataFilter != null && mdxDataFilter.size() > 0;
		boolean hasLevelFilter = mdxLevelFilter != null;
		
		String axisFilter =  hasAxisFilter ? toAxisAttrFilter(mdxAttr, mdxAxisFilter, true) : null;
		String dataFilter = hasDataFilter ? toAxisAttrFilter(mdxAttr, mdxDataFilter, true) : null;
		String levelFilter = hasLevelFilter ? mdxLevelFilter.toString() : null;
		String axisFilterName = "Axis" + suffix;
		String dataFilterName = "Data" + suffix;
		
		hasDataFilter = hasDataFilter || hasLevelFilter;
		if (hasDataFilter && dataFilter == null)
			dataFilter = "{" + levelFilter + "}";
		else if (levelFilter != null)
			dataFilter = "{" + dataFilter + ", " + levelFilter + "}";
		
		if (hasAxisFilter)
			with.append(" SET " + axisFilterName + " AS " + axisFilter).append(System.lineSeparator());
		
		if (hasDataFilter) {
			with.append(" SET " + dataFilterName + " AS " + dataFilter);
			if (dataFilters != null)
				dataFilters.add(dataFilterName);
		}
		if (hasLevelFilter) {
			config.getLevelFilters().remove(mdxLevelFilter);
		}
		
		String visualFilter = null;
		if (hasAxisFilter && hasDataFilter) {
			visualFilter = MoConstants.FUNC_INTERSECT + "(" + axisFilterName + ", " + dataFilterName + ")";
			with.append(" SET " + visualFilter + " AS '" + String.format(visualFormat, visualFilter) + "'");
		} else if (hasAxisFilter)
			visualFilter = axisFilterName;
		else if (hasDataFilter)
			visualFilter = dataFilterName;
		
		if (attrAll != null)
			with.append(" MEMBER " + attrAll + " AS Sum(" + (hasDataFilter ? dataFilterName : mdxAttr.toString()) + ")");
		
		return visualFilter;
	}
	
	// we also remove the filter to avoid duplicate filtering on where for selected columns
	private List<MDXFilter> findAndRemoveFilter(Map<? extends MDXElement, List<MDXFilter>> filterMap, MDXElement mdxElem) {
		List<MDXFilter> totalFiltersList = new ArrayList<MDXFilter>();
		for (Iterator<?>iter = filterMap.entrySet().iterator(); iter.hasNext(); ) {
			Entry<? extends MDXElement, List<MDXFilter>> pair = (Entry<? extends MDXElement, List<MDXFilter>>)iter.next();
			//if same mdx element is detected and this is not a property filter, which should be applied in where axis as it speeds up 
			if (MDXElement.filterEquals(pair.getKey(), mdxElem)) {
				List<MDXFilter> filtersList = new ArrayList<MDXFilter>();
				if (PROPERTIES_FILTERING_ON_WHERE) {
					for (MDXFilter filter : pair.getValue())
					pair.getValue().removeAll(filtersList);
					if (pair.getValue().size() == 0)
						iter.remove();
				} else {
					filtersList = pair.getValue();
					iter.remove();
				}
				totalFiltersList.addAll(filtersList);
			}
		}
		return totalFiltersList.size() == 0 ? null : totalFiltersList;
	}
	
	private MDXAttribute findAndRemoveFilter(List<MDXAttribute> filterList, MDXAttribute mdxAttr) {
		for (MDXAttribute filter : filterList) {
			if (MDXAttribute.filterEquals(filter, mdxAttr)) {
				filterList.remove(filter);
				return filter;
			}
		}
		return null;
	}
	
	private String getWhere(MDXConfig config, StringBuilder with) throws AmpApiException {
		if (config.getLevelFilters().size() == 0 && config.getDataFilters().size() == 0)
			return "";
		
		// use NonEmptyCrossJoin if both column & row axis are requesting this
		final String fCrossJoin = !config.isAllowRowsEmptyData() && !config.isAllowColumnsEmptyData() ?
				MoConstants.FUNC_NON_EMPTY_CROSS_JOIN : MoConstants.FUNC_CROSS_JOIN; 
		
		final String sep = ", ";
		//initialize with the number of commas ","  
		int size = ((config.getDataFilters().entrySet().size() == 0 ? 0 : config.getDataFilters().entrySet().size() - 1)
				+ (config.getLevelFilters().size() == 0 ? 0 : config.getLevelFilters().size() - 1) 
				+ (config.getDataFilters().entrySet().size() > 0 && config.getLevelFilters().size() > 0 ? 1 : 0)) * sep.length();
		boolean doCrossJoin = false;
		
		//store groups of filters by same level to reduce the number of cross joins
		Map<String, List<String>> filtersMap = new HashMap<String, List<String>>();
		Set<String> usesFilterFunc = new HashSet<String>();
		
		//build filters Map from MDXFilters
		for (Map.Entry<MDXAttribute, List<MDXFilter>> pair:config.getDataFilters().entrySet()) {
			String filter = toFilter(pair.getKey().toString(), pair.getKey().getCurrentMemberName(), pair.getKey(), pair.getValue(), false);
			size += filter.length();
			addToFilterMap(filtersMap, filter, pair.getKey());
			//remember that we'll need crossJoins if there is at least one range/multiple values filter (i.e. whenever a filter has no singleValue filter)
			doCrossJoin = doCrossJoin || !isSingleValueFilter(pair.getValue());
			//if Filter function is used, basically only when properties are used, then we must do cross join even if this is a single value filter 
			if (isFilterFuncUsed(pair.getKey(), pair.getValue())) {
				usesFilterFunc.add(pair.getKey().getFullName());
				doCrossJoin = true;
			}
		}
		//build filters Map from Single Level filters
		for (MDXAttribute mdxAttr:config.getLevelFilters()) {
			String filter = mdxAttr.toString();
			size += filter.length();
			addToFilterMap(filtersMap, filter, mdxAttr);
		}
				
		if (filtersMap.keySet().size() <= 1) //though cannot be 0
			doCrossJoin = false;
		else
			size += filtersMap.keySet().size() * 2 //2 = {} to be added around each filter set, except if Filter func was used, so .size() is max number of {} 
					+ (filtersMap.keySet().size() - 1) * (fCrossJoin.length() + 2); //2 = ()
		
		final String where = " WHERE (";
		StringBuilder whereFilter = new StringBuilder(size + where.length() + 1); //1 for closing )
		whereFilter.append(where);
		
		//builds WHERE slicer based on filters
		for(Iterator<List<String>> iter = filtersMap.values().iterator(); iter.hasNext(); ) {
			List<String> filterList = iter.next();
			boolean useUnion = filterList.size() > 1;
			String valuesTmp = useUnion ? filterList.subList(1, filterList.size()).toString() : filterList.toString();
			valuesTmp = valuesTmp.substring(1, valuesTmp.length()-1); //strip of []
			String values = useUnion ? "{" + filterList.get(0) + "}, " + "{" + valuesTmp + "}" : valuesTmp;
			boolean hasDates = values.contains(MoConstants.DATES);
			String datesPrefix = (hasDates ? MoConstants.FUNC_ORDER + "(" : "");
			String datesSuffix = (hasDates ? ", " + mainDates.getSortName() + ", ASC)" : "");
			String prefix = useUnion ?  datesPrefix + MoConstants.FUNC_UNION + "(" : "{"; 
			String suffix = useUnion ? ")" + datesSuffix : "}";
			
			if (doCrossJoin)
				if (iter.hasNext())
					whereFilter.append(fCrossJoin).append("(").append(prefix);
				else
					whereFilter.append(prefix);
			else if (useUnion)
				whereFilter.append(prefix);
			whereFilter.append(values); 
			if (doCrossJoin || useUnion)
				whereFilter.append(suffix);
			if (iter.hasNext())
				whereFilter.append(",");
		}
		if (doCrossJoin)
			for(int i = 0; i < filtersMap.values().size() -1; i++)
				whereFilter.append(")"); //closing ) of the crossjoins
		whereFilter.append(")");
		
		return whereFilter.toString();
	}
	
	private int addToFilterMap(Map<String, List<String>> filtersMap, String filter, MDXAttribute mdxAttr) {
		String filterKey = mainDates.getDimensionAndHierarchy().equals(mdxAttr.getDimensionAndHierarchy()) ?
				mdxAttr.getDimensionAndHierarchy() : mdxAttr.getFullName();
		List<String> filtersList = filtersMap.get(filterKey);
		if (filtersList == null) {
			filtersList = new ArrayList<String>();
			filtersMap.put(filterKey, filtersList);
		}
		filtersList.add(filter);
		return filter.length();
	}
	
	private String toAxisAttrFilter(MDXAttribute mdxAttr, List<MDXFilter> mdxFilter, boolean orderDates) throws AmpApiException {
		return toFilter(mdxAttr.toString(), mdxAttr.getCurrentMemberName(), mdxAttr, mdxFilter, orderDates);
	}
	
	private String toFilter(String toFilterSet, MDXElement filterBy, List<MDXFilter> mdxFilter, boolean orderDates) throws AmpApiException {
		return toFilter(toFilterSet, filterBy.toString(), filterBy, mdxFilter, orderDates);
	}
	
	private String toFilter(String toFilterSet, String filterBy, MDXElement ref, List<MDXFilter> filtersList, boolean orderDates) throws AmpApiException {
		if (filtersList == null || filtersList.size() == 0 ) return "";
		
		final String or = " OR ";
		String origFilterBy = filterBy;
		String filter = "";
		String allowedFilterList = "";
		String notAllowedFilterList = "";
		boolean addFilterFunc = false;
		boolean isMeasure = ref instanceof MDXMeasure;
		
		for (MDXFilter mdxFilter : filtersList) {
			addFilterFunc = addFilterFunc || isMeasure || true; //mdxFilter.isKey;
			filterBy = origFilterBy; //reset if changed by properties
			
			//this is a filter by key value
			//if (mdxFilter.isKey) {
				if (isMeasure) 
					throw new AmpApiException("Not supported. Keys are applicable to MDX Attributes only.");
				
				MDXAttribute propLevel =  (MDXAttribute) ref;
				
				//replace filter by to be filter by key
				filterBy = getKeyCast(propLevel, "Integer");
				toFilterSet = propLevel.toString();
//			} 
			
			//one value to select
			if (!isMeasure && mdxFilter.singleValue != null) {
				//if (mdxFilter.isKey)
					filter += or + filterBy + (mdxFilter.allowedFilteredValues ? " = " : " <> ") + mdxFilter.singleValue; 
//				else {
//					if (mdxFilter.allowedFilteredValues)
//						allowedFilterList += "," + toMDX(ref, mdxFilter.singleValue);
//					else 
//						notAllowedFilterList += "," + toMDX(ref, mdxFilter.singleValue);
//				}
			} else {
				//multiple values to select			
				if (mdxFilter.filteredValues!=null) {
					if (!isMeasure/* && mdxFilter.isKey*/) {
						//TODO: we should find another solution to make a similar approach done with strings, i.e. IN / NOT IN {list}, which is not working with simple integers
						StringBuilder sb = new StringBuilder((filterBy.length() + 20) *mdxFilter.filteredValues.size());
						String operator = (mdxFilter.allowedFilteredValues ? " = " : " <> ");
						for (String value: mdxFilter.filteredValues) {
							sb.append(or).append(filterBy).append(operator).append(value);
						} 
						filter += sb.toString();
					} else {
						String values = mdxFilter.filteredValues.toString();
						values = values.substring(1, values.length()-1);//strip of "[]"
//						if (!isMeasure && !mdxFilter.isKey) { 
//							String fullName = ref.getFullName();
//							StringBuilder sb = new StringBuilder((fullName.length() + 20)*mdxFilter.filteredValues.size());
//							String sep = ", ";
//							for (String value: mdxFilter.filteredValues) {
//								sb.append(fullName).append(".").append(MDXElement.quote(value)).append(sep);
//							}
//							values = sb.substring(0, sb.length()-sep.length());
//						}
						if (mdxFilter.allowedFilteredValues)
							allowedFilterList += "," + values;
						else 
							notAllowedFilterList += "," + values;
					}
				} else {
					//a range of values
					String range = getRange(mdxFilter, ref, filterBy, isMeasure);
					if (range.contains(":"))
						allowedFilterList += "," + range;
					else
						filter += or + range;
				}
			}
		}
		
		if (allowedFilterList.length() > 0)
			allowedFilterList = allowedFilterList.substring(1); //remove first ","
		if (notAllowedFilterList.length() > 0)
			notAllowedFilterList = notAllowedFilterList.substring(1); //remove first ","
		
		addFilterFunc = addFilterFunc || filter.length() - or.length() > 0 || notAllowedFilterList.length() > 0;
		if (allowedFilterList.length() > 0) 
			if (addFilterFunc)
				filter += or + filterBy + " IN {" +  allowedFilterList + "}";
			else
				filter += "{" + allowedFilterList + "}";
		if (notAllowedFilterList.length() > 0)
			filter += or + filterBy + " NOT IN {" + notAllowedFilterList + "}";
		
		if (filter.startsWith(or))
			filter = filter.substring(or.length()); //remove first OR 
		
		if (addFilterFunc && !"".equals(filter)) {
			if (!isMeasure) {
				filter = MoConstants.FUNC_IIF + "(" + getKeyCast((MDXAttribute)ref, "String") + " <> '#null', " 
					+ filter + ", NULL)";
			}
			filter = MoConstants.FUNC_FILTER + "(" + toFilterSet + ", " +filter + ")";
		} else if (ref.getFullName().contains(MoConstants.DATES) && orderDates)
			//the only ordering by default is for dates sets
			filter = order(filter, ref, SortOrder.ASC);
		//should not be this
		if ("".equals(filter)) 
			filter = toFilterSet;
		return filter;
	}
	
	private String getRange(MDXFilter mdxFilter, MDXElement ref, String filterBy, boolean isMeasure) {
		String startRange = mdxFilter.startRange == null ? null : (mdxFilter.startRange);
		String endRange = mdxFilter.endRange == null ? null : (mdxFilter.endRange);
		String filterRange = "";
		
		//checking if we can represent the range as { START : END } set
/*		if (!isMeasure && !mdxFilter.isKey && (
				mdxFilter.startRangeInclusive && mdxFilter.endRangeInclusive
				|| mdxFilter.startRangeInclusive && mdxFilter.endRange == null
				|| mdxFilter.startRange == null && mdxFilter.endRangeInclusive
				)) {
			filterRange += startRange != null ? startRange : toFirstChild(ref);
			filterRange += ":";
			filterRange += endRange !=null ? endRange : toLastChild(ref);
		} else */ 
		{
			if (startRange!=null) {
				filterRange += "(" + filterBy
						+ (mdxFilter.startRangeInclusive ? " >= " : " > ") 
						+ startRange + ")" 
						+ (endRange!=null ? " AND " : ""); 
			} 
			if (endRange!=null) {
				filterRange += "(" + filterBy 
						+ (mdxFilter.endRangeInclusive ? " <= " : " < ") 
						+ endRange + ")";
			}
		}
		return filterRange;
	}
	
	private String getKeyCast(MDXAttribute mdxAttr, String propertyType) {
		return MoConstants.FUNC_CAST + "(" + mdxAttr.getCurrentMemberName() + "." 
				+ MoConstants.PROPERTIES + "('" + MoConstants.P_KEY + "') AS " +  propertyType + ")";
	}
	
	private String order(String set, MDXElement mdxAttr, SortOrder order) {
		return MoConstants.FUNC_ORDER + "(" + set + ", " + mdxAttr.getSortName() + ", " + order.toString() +")"; 
	}
	
	private boolean isFilterFuncUsed(MDXElement ref, List<MDXFilter> mdxFilter) {
		return ref instanceof MDXMeasure || hasKeyFilters(mdxFilter);
	}
	
	private boolean hasKeyFilters(List<MDXFilter> mdxFilters) {
		return !mdxFilters.isEmpty();
//		for (MDXFilter filter: mdxFilters)
//			if (filter.isKey)
//				return true;
//		return false;
	}
	
	private boolean isSingleValueFilter(List<MDXFilter> mdxFilters) {
		return mdxFilters.size() == 1 && mdxFilters.get(0).singleValue != null;
	}
	
	private String toMDX(MDXElement elem, String value) {
		return toMDX(elem, value, false);
	}
	
	private String toMDX(MDXElement elem, String value, boolean isKey) {
		if (!elem.hasFullName()) {
			logger.error("MDXElement without fullname: " + elem.getFullName());
			return "";
		}
		return elem.getFullName() + "." + (isKey ? "&" : "") + MDXElement.quote(value);
	}
	
	private String toFirstChild(MDXElement elem) {
		return elem.getFullName() + "." + MoConstants.FIRST_CHILD;
	}
	
	private String toLastChild(MDXElement elem) {
		return elem.getFullName() + "." + MoConstants.LAST_CHILD;
	}
	
	private String getAll(MDXAttribute mdxAttr) throws AmpApiException {
		String all = MondrianMapping.getAll(mdxAttr);
		if (all == null)
			all = mdxAttr.getDimensionAndHierarchy() + "." + MDXElement.quote (mdxAttr.getName() + " Totals");
		return all;
	}
	
	/**
	 * Uniformizes dates filters to the lowest filters level. 
	 * @param config
	 */
	private void adjustDateFilters(MDXConfig config) {
		int maxLevel = 0; //i.e. no time level
		Map<MDXLevel, Integer> singleDates = new HashMap<MDXLevel, Integer>();
		Map<MDXLevel, Integer> multipleDates = new HashMap<MDXLevel, Integer>();
		Set<Integer> levels = new TreeSet<Integer>();
		
		maxLevel = findDates(config.getLevelFilters(), singleDates, maxLevel);
		maxLevel = findDates(config.getDataFilters().keySet(), multipleDates, maxLevel);
		levels.addAll(singleDates.values());
		levels.addAll(multipleDates.values());
		
		//if we have a mix of time levels, then we adjust them to 'Dates' hierarchy to be able to work with single hierarchy for dates filters
		if (levels.size() > 1) {
			for(Entry<MDXLevel, Integer> pair : singleDates.entrySet()) {
				pair.getKey().setHierarchy(MoConstants.DATES);
			}
			for(Entry<MDXLevel, Integer> pair : multipleDates.entrySet()) {
				List<MDXFilter> filtersList = config.getDataFilters().remove(pair.getKey());
				pair.getKey().setHierarchy(MoConstants.DATES);
				config.addDataFilter(pair.getKey(), filtersList);
			}
		}
	}
	
	private int findDates(Collection<? extends MDXElement> elements, Map<MDXLevel, Integer> dates, int level) {
		for (MDXElement mdxAttr : elements) {
			int currentLevel = getTimeLevel(mdxAttr);
			//first filter out any non-time level, then get maximum filtering depth
			level = Math.max(Math.max(0, currentLevel), level);
			if (currentLevel > 0)
				dates.put((MDXLevel)mdxAttr, Integer.valueOf(currentLevel));
		}
		return level;
	}
	
	private int getTimeLevel(MDXElement mdxAttr) {
		MDXLevel mdxLevel = mdxAttr instanceof MDXLevel ? (MDXLevel)mdxAttr : null;
		if (mdxLevel != null && MoConstants.DATES.equals(mdxLevel.getDimension()))
			switch(mdxLevel.getHierarchy()) { 
			case MoConstants.H_YEAR: return 1; 
			case MoConstants.H_QUARTER: return 2;
			case MoConstants.H_MONTH: return 3;
			case MoConstants.H_DATES: 
				switch(mdxLevel.getLevel()) {
				case MoConstants.ATTR_YEAR: return 1;
				case MoConstants.ATTR_QUARTER: return 2;
				case MoConstants.ATTR_MONTH: return 3;
				case MoConstants.ATTR_DATE: return 4;
				}
			default: return -1;
			}
		return -2;
	}
	
	/*
	private void adjustPropertyFilters(MDXConfig config) throws AmpApiException {
		adjustPropertyFilters(config.getColumnAttributes(), config);
		adjustPropertyFilters(config.getRowAttributes(), config);
	}
	
	private void adjustPropertyFilters(List<MDXAttribute> attrList, MDXConfig config) throws AmpApiException {
		for (MDXAttribute mdxAttr : attrList) {
			if(!adjustPropertyFilters(mdxAttr, config.getAxisFilters())) //process axis filters first of all
				adjustPropertyFilters(mdxAttr, config.getDataFilters());
		}
	}
	*/
	
	/**
	 * @return true if original attribute was adjusted
	 */
	/*
	private boolean adjustPropertyFilters(MDXAttribute mdxAttr, Map<? extends MDXElement, List<MDXFilter>> filterMap) throws AmpApiException {
		for (Entry<? extends MDXElement, List<MDXFilter>> pair : filterMap.entrySet()) 
			if (MDXElement.filterEquals(pair.getKey(), mdxAttr)) 
				for (MDXFilter filter : pair.getValue()) 
					if (filter.property != null && MDXLevel.class.isAssignableFrom(mdxAttr.getClass())) {
						MDXLevel mdxLevel = (MDXLevel)mdxAttr;
						MDXLevel propLevel = getPropertyLevel(mdxAttr, filter.property);
						if (!propLevel.getDimensionAndHierarchy().equals(propLevel.getDimensionAndHierarchy())) {
							mdxLevel.setDimension(propLevel.getDimension());
							mdxLevel.setHierarchy(propLevel.getHierarchy());
							//Once we move current element to another hierarchy, we stick with it and any further properties will remain on WHERE.
							//Properties must be defined carefully so that they work best with MDX: 
							//define them under main hierarchy (with multiple levels), though request members on their individual hierarchies. Exception is for DATE filter only 
							return true;  
						} 
					}
		return false;
	}
	*/
		
//	private MDXAttribute getPropertyLevel(MDXAttribute mdxAttr) throws AmpApiException {/*
//		if (MoConstants.DATES.equals(mdxAttr.getDimension())) {
//			MDXAttribute a = mainDates.clone();
//			a.setName(mdxAttr.getName());
//			return a;
//		} */
//		return mdxAttr;			
//		/*
//		MDXLevel propLevel = propertiesLevels.get(property); 
//		if (propLevel == null)
//			throw new AmpApiException("No level for \"" + property + "\" property found");
//		if (!propLevel.getDimension().equals(mdxAttr.getDimension()))
//			throw new AmpApiException("'" + property + "' defined under '" + propLevel.getDimension() 
//					+ "' dimension, but requested from '" + mdxAttr.getDimension() + "' dimension. Dimensions must match.");
//		return propLevel;
//		*/
//	}
	
	private String sorting(Map<MDXTuple, SortOrder> sortingOrder, String axisMdx) {
		if (sortingOrder == null || sortingOrder.size() == 0) return axisMdx;
		StringBuilder fullPrefix = new StringBuilder(sortingOrder.size() * (MoConstants.FUNC_ORDER + "(").length());
		StringBuilder fullSuffix = new StringBuilder(sortingOrder.size() * (sortingOrder.entrySet().iterator().next().getKey().toSortingString().length() + 16)); //16 aprox reserve for sort name diff + delimiters
		
		buildSuffixAndPrefix(fullPrefix, fullSuffix, sortingOrder.entrySet().iterator());
		
		axisMdx =  fullPrefix.toString() + axisMdx +  fullSuffix.toString();
		
		return axisMdx;
	}
	
	private void buildSuffixAndPrefix(StringBuilder fullPrefix, StringBuilder fullSuffix, Iterator<Entry<MDXTuple,SortOrder>> iter) {
		if (!iter.hasNext()) return;
		final String prefix = MoConstants.FUNC_ORDER + "(";
		final String delim = ", ";
		final String end = ")";
		Entry<MDXTuple, SortOrder> elem = iter.next();
		boolean breakingSort = SortOrder.BASC.equals(elem.getValue()) || SortOrder.BDESC.equals(elem.getValue());
		//for breaking sort, add first sorting rule at the end
		if (breakingSort)
			buildSuffixAndPrefix(fullPrefix, fullSuffix, iter);
		fullPrefix.append(prefix);
		fullSuffix.append(delim).append(elem.getKey().toSortingString()).append(delim).append(elem.getValue().name()).append(end);
		//for non breaking sort, add first sorting rule at the beginning
		if (!breakingSort)
			buildSuffixAndPrefix(fullPrefix, fullSuffix, iter);
	}
	
	/** 
	 * replaces levels from same hierarchy that are used in different axis by another duplicate hierarchy 
	 * (any other solution for same hierarchy on multiple axis?) 
	 */
	/*candidate for removal
	private void adjustDuplicateElementsOnDifferentAxis(MDXConfig config) {
		Set<String> usedDimensions = new HashSet<String>();
		
		usedDimensions.addAll(adjustDuplicates(usedDimensions, config.getColumnAttributes()));
		//check against column axis
		usedDimensions.addAll(adjustDuplicates(usedDimensions, config.getRowAttributes()));
		//check against column & rows axis
		adjustDuplicates(usedDimensions, config.getLevelFilters());
		
		Map<MDXAttribute, MDXFilter> replaceFilters = new HashMap<MDXAttribute, MDXFilter>();
		for (Iterator<Entry<MDXAttribute, MDXFilter>> iter = config.getDataFilters().entrySet().iterator(); iter.hasNext(); ) {
			Entry<MDXAttribute, MDXFilter> pair = iter.next();
			MDXAttribute mdxAttr  = pair.getKey();
			if (usedDimensions.contains(mdxAttr.getDimensionAndHierarchy())) {
				iter.remove();
				replaceFilters.put(MondrianUtils.getDuplicate(mdxAttr), pair.getValue());
			}
		}
		config.getDataFilters().putAll(replaceFilters);  
	}
	*/
	
	/** replaces attr hierarchy with duplicate one; supporting method for of {@link #adjustDuplicateElementsOnDifferentAxis */
	/* candidate for removal
	private Set<String> adjustDuplicates(Set<String> usedDimensions, List<MDXAttribute> attrList) {
		Set<String> newDimensions = new HashSet<String>();
		for (ListIterator<MDXAttribute> iter = attrList.listIterator(); iter.hasNext();) {
			MDXAttribute mdxAttr  = iter.next();
			if (usedDimensions.contains(mdxAttr.getDimensionAndHierarchy())) {
				iter.set(MondrianUtils.getDuplicate(mdxAttr));
			}
			newDimensions.add(mdxAttr.getDimensionAndHierarchy());
		}
		return newDimensions;
	}
	*/
	
	/* candidate for removal
	private List<String> hierchize(List<MDXAttribute> origList, List<String> mdxList) {
		List<String> attrListUpdated = new ArrayList<String>();
		Queue<String> hierarcharizeStack = new LinkedList<String>();
		for (ListIterator<MDXAttribute> iter = origList.listIterator(); iter.hasNext(); ) {
			MDXAttribute previous = iter.hasPrevious() ? iter.previous() : null; 
			if (previous !=null) iter.next(); //move back 
			MDXAttribute current = iter.next();
			if (previous != null && current.getDimension().equals(previous.getDimension())) {
				if (hierarcharizeStack.isEmpty()) {
					attrListUpdated.remove(attrListUpdated.size()-1);
					hierarcharizeStack.add(mdxList.get(iter.previousIndex()-1));
				}
				hierarcharizeStack.add(mdxList.get(iter.previousIndex()));
			} else if (hierarcharizeStack.size() > 0) {
				attrListUpdated.add(getHierarcharize(hierarcharizeStack));
			} else { 
				attrListUpdated.add(mdxList.get(iter.previousIndex()));
			}
		}
		if (hierarcharizeStack.size() > 0) 
			attrListUpdated.add(getHierarcharize(hierarcharizeStack));
		return attrListUpdated;
	} 
	
	private String getHierarcharize(Queue<String> hierarcharizeStack) {
		if (hierarcharizeStack.size() == 0) return "";
		String hierarchize = "{" + MoConstants.FUNC_HIERARCHIZE + "({";
		String sep = ", ";
		while (!hierarcharizeStack.isEmpty()) {
			String elem = hierarcharizeStack.remove();
			hierarchize += (elem.startsWith("{") ? elem : "{" + elem + "}") + sep;
		}
		hierarchize = hierarchize.substring(0, hierarchize.length()- sep.length()) + "})}";
		return hierarchize;
	}
	*/

	synchronized
	public void validate(String mdx, String mdxName) throws AmpApiException {
		try {
			validator.validateSelect(parser.parseSelect(mdx));
		} catch (Exception e) {
			String err = MondrianUtils.toString(e);
			//known Mondrian BUG for MDX validation not supporting named sets: http://jira.pentaho.com/browse/MONDRIAN-877 
			if (!(e instanceof UnsupportedOperationException && err.contains("NamedSetExpr")))
			//for some reason validation fails when Properties are in MDX queries and a NullPointerException is thrown
			//the MDX is valid (verified in Saiku) so we'll avoid throwing exception only in this particular case
			if (!(mdx.contains(MoConstants.PROPERTIES) && e instanceof NullPointerException)) {
				logger.error(mdxName + ": Invalid MDX query\"" + mdx + "\". \nError details: " + err);
				throw new AmpApiException(mdxName + ": Invalid MDX query: " + err);
			}
		}
	}
	
	/*
	synchronized
	public boolean isValid(SelectNode select) {
		try {
			validator.validateSelect(select);
		} catch (OlapException e) {
			logger.error(MondrianUtil.getOlapExceptionMessage(e));
			return false;
		}
		return true;
	}
	*/
	
	
//	/**
//	 * Executes MDX query string
//	 * @param mdx MDX query string
//	 * @return CellSet result of MDX query see {@link CellSet}
//	 * @throws AmpApiException
//	 */
//	public CellSet runQuery(String mdx) throws AmpApiException {
//		CellSet res = null;
//		try {
//			CellSet res = this.olapConnection.createStatement().executeOlapQuery(mdx);
//			return res;
//		} catch (OlapException e) {
//			logger.error("Could not execute Olap Query \"" + mdx + "\", Error Details: " + MondrianUtils.getOlapExceptionMessage(e));
//			throw new AmpApiException("Could not execute Olap Query");
//		}
//	}

	/**
	 * Executes MDX query string
	 * @param mdx MDX query string
	 * @return CellSet result of MDX query see {@link CellSet}
	 * @throws AmpApiException
	 */
	public CellSet runQuery(String mdx) throws AmpApiException { // temporary hacky replacement until AMP-18369 and related tickets are resolved
		int nrTries = 2;
		Exception ex = null;
		for(int i = 1; i <= nrTries; i++) {
			if (i > 1)
				logger.error("RETRYING to execute MDX query");
			try {
				CellSet res = this.olapConnection.createStatement().executeOlapQuery(mdx);
				return res;
			}
			catch(OlapException e){
				ex = e;
				logger.error("Could not execute Olap Query \"" + mdx + "\", Error Details: " + MondrianUtils.getOlapExceptionMessage(e));
			}
		}
		throw new AmpApiException("count not execute Olap Query after " + nrTries + " attempts, abandoning", ex);
	}
	
	/**
	 * 				WON'T USE THE ORG.OLAP4J.QUERY API - it is experimental and limited
	 */
	/*.
	/**
	 * Builds OLAP Query using Olap4J API
	 * @param config 
	 * @return
	 * @throws AmpApiException
	 */
	/*
	public Query getSimpleOlapQuery(MDXConfig config) throws AmpApiException {
		Query mdx = createQuery(config);
		//to do : other configurations
		addDimentions(mdx, Axis.COLUMNS, config.getColumnMeasures());
		addDimentions(mdx, Axis.ROWS, config.getRows());
		addDimentions(mdx, Axis.FILTER, config.getFilters());
		
		try {
			mdx.validate();
		} catch (OlapException e) {
			logger.error(MondrianUtil.getOlapExceptionMessage(e));
			throw new AmpApiException(AmpApiException.SYNTAX_ERROR, false, e);
		}
		return mdx;
	}
	
	private void addDimentions(Query mdx, Axis axis, List<MDXElement> elements) {
		QueryDimension qDimension = null;
		for(MDXElement elem:elements) {
			qDimension = mdx.getDimension(elem.getName()); //TBD 
			mdx.getAxis(axis).addDimension(qDimension);
		}
	}
	
	private Query createQuery(MDXConfig config) throws AmpApiException {
		String qName = config.getMdxName(); 
		Query mdx = null;
				
		//create Olap Query obj
		try {
			mdx = new Query(qName, getCube(config));
		} catch (SQLException e) {
			logger.error("Could not create query '" + qName + "': " + ErrorReportingPlugin.getSQLExceptionMessage(e));
			throw new AmpApiException(AmpApiException.MONDRIAN_ERROR, false, e);
		}
		return mdx;
	}
	*/
}

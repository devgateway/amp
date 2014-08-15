/**
 * 
 */
package org.digijava.kernel.ampapi.mondrian.queries;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
import org.digijava.kernel.ampapi.mondrian.util.MondrianUtils;
import org.olap4j.CellSet;
import org.olap4j.OlapConnection;
import org.olap4j.OlapException;
import org.olap4j.mdx.parser.MdxParser;
import org.olap4j.mdx.parser.MdxValidator;
import org.olap4j.metadata.Cube;
import org.olap4j.metadata.Dimension;
import org.olap4j.metadata.Hierarchy;
import org.olap4j.metadata.Level;
import org.olap4j.metadata.Member;
import org.olap4j.metadata.NamedList;
import org.olap4j.metadata.Property;
import org.olap4j.query.SortOrder;

/**
 * MDX Query Generator
 * @author Nadejda Mandrescu
 *
 */
public class MDXGenerator {
	protected static final Logger logger = Logger.getLogger(MDXGenerator.class); 
	private static Map<String, String> allNames;
	private static Map<String, MDXLevel> propertiesLevels; //<Dimension name, MDXLevel that stores properties for filtering by ids>
	private static Map<MDXLevel, Map<String, String>> levelPropertyType;
	static {
		Map<String, String> mapping = new HashMap<String, String>();
		Map<String, MDXLevel> properties = new HashMap<String, MDXLevel>();
		Map<MDXLevel, Map<String, String>> propTypes = new HashMap<MDXLevel, Map<String, String>>();
		boolean success = false;
		OlapConnection olapConnection = null;
		try {
			olapConnection = Connection.getOlapConnectionByConnPath(Connection.getDefaultConnectionPath());
			//NamedList<Dimension> dimList = olapConnection.getOlapSchema().getSharedDimensions();
			List<Dimension> dimList = new ArrayList<Dimension>();
			for (Cube cube:olapConnection.getOlapSchema().getCubes()) {
				dimList.addAll(cube.getDimensions());
			}
			for (Dimension dim:dimList) 
				for (Iterator<Hierarchy> h = dim.getHierarchies().iterator(); h.hasNext(); ){
					boolean foundAll = false;
					boolean foundProperties = false;
					for (Iterator<Level> l = h.next().getLevels().iterator(); l.hasNext() && !(foundAll && foundProperties);) {
						Level level = l.next();
						for (Member m: level.getMembers())
							if (m.isAll()) {
								mapping.put(MDXElement.quote(m.getHierarchy().getName()), m.toString());
								if (dim.getHierarchies().size() == 1) //keep also the default All member per dimension when there is only 1 hierarchy 
									mapping.put(MDXElement.quote(dim.getName()), m.toString());
								foundAll = true;
								break;
							} 
						if (level.getProperties().get("default") != null) {
							String hName = level.getHierarchy().getName().substring(dim.getName().length()+1);
							MDXLevel propLevel = new MDXLevel(dim.getName(), hName , level.getName());
							properties.put(dim.getName(), propLevel);
							Map<String, String> propTypeMap = new HashMap<String, String>();
							for(Property prop : level.getProperties()) {
								propTypeMap.put(prop.getName(), MondrianUtils.getDatatypeName(prop.getDatatype()));
							}
							propTypes.put(propLevel, propTypeMap);
							foundProperties = true;
						}
					}
				}
			success = true;
		} catch (Exception e) {
			logger.error(MondrianUtils.toString(e));
		} finally {
			allNames = success ? mapping : null;
			propertiesLevels = success ? properties : null;
			levelPropertyType = success ? propTypes : null;
			if (olapConnection!=null) {
				try {
					olapConnection.close();
				} catch (SQLException e) {
					logger.error(ErrorReportingPlugin.getSQLExceptionMessage(e));
				}
			}
		}
	}
	
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
			throw new AmpApiException(AmpApiException.MONDRIAN_ERROR, false, e);
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
		String columns = " ON COLUMNS, ";
		String rows = " ON ROWS ";
		String where = "";
		String mdx = "";
		String notEmptyColumns = config.isAllowColumnsEmptyData() ? "" : "NON EMPTY ";
		String notEmptyRows = config.isAllowRowsEmptyData() ? "" : "NON EMPTY ";
		
		adjustDuplicateElementsOnDifferentAxis(config);

		String axisMdx = null;
		
		/* COLUMNS */
		//if column attributes are not configured, display only measures
		axisMdx = getColumns(config, with);
		
		columns  =  notEmptyColumns + axisMdx + columns;
		
		/* ROWS */
		axisMdx = getRows(config, with);
				
		//filter axis by measures
		if (config.getAxisFilters().size()>0) {
			for (Map.Entry<MDXElement, MDXFilter> pair : config.getAxisFilters().entrySet()) {
				if (pair.getKey() instanceof MDXMeasure) {
					axisMdx = toFilter(axisMdx, pair.getKey(), pair.getValue());
				}
			}
 		}
		//sorting
		if (config.getSortingOrder().size() > 0) {
			axisMdx = sorting(config.getSortingOrder(), axisMdx);
		}
		rows  = notEmptyRows + axisMdx + rows;
		
		/* WHERE  (slice data, aka filters in Reports) */
		where = getWhere(config);
		
		mdx = (with.length() > 0 ? "WITH " + with.toString() : "") + select + columns + rows + from + where;
		
		logger.info("[" + config.getMdxName() + "] MDX query: " + mdx);
		validate(mdx);
			
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
		for (MDXMeasure colMeasure:config.getColumnMeasures()) {
			measures += "," + colMeasure.toString();
		}
		axisMdx = "{" + measures.substring(1) + "}";
		if (config.getColumnAttributes().size()==0) {
			//aka simple summary totals report or can be used by dashboards/reports charts (e.g. pie chart)
			axisMdx = "{" + MoConstants.FUNC_HIERARCHIZE + "(" + axisMdx + ")}"; //
		} else {
			axisMdx = getAxis(config, config.getColumnAttributes().listIterator(config.getColumnAttributes().size()), 
					with, "COLSET", axisMdx, config.isDoColumnsTotals(), config.getColsHierarchiesTotals());
		}
		return axisMdx;
	}
	
	/**
	 * Builds 'ON ROWS' MDX query
	 * @param config - MDX Config
	 * @param with - the 'WITH' string to be prefixed to MDX select 
	 * @return 'ON ROWS' MDX query portion
	 * @throws AmpApiException
	 */
	private String getRows(MDXConfig config, StringBuilder with) throws AmpApiException {
		//assumption: only attributes are displayed per rows, no measures
		return getAxis(config, config.getRowAttributes().listIterator(config.getRowAttributes().size()), 
				with, "ROWSET", null, config.isDoRowTotals(), config.getRowsHierarchiesTotals());
	}
	
	/** supporting common method used by getRows and getColumns, because the algorithm is the same similar */
	private String getAxis(MDXConfig config, ListIterator<MDXAttribute> reverseIterator, StringBuilder with, String withPrefix,
							String initMember, boolean doTotals, int numSubTotals) throws AmpApiException {
		int setId = 0; //identifier suffix for 'with' member name
		String crossJoin = initMember;
		String prevAttrAll = initMember; //used if config.isDoColumnsTotals() is true
		
		for (ListIterator<MDXAttribute> iter = reverseIterator; iter.hasPrevious(); ) {
			MDXAttribute rowAttr = iter.previous();
			String attrNode = null;
			String attrAll = doTotals || numSubTotals > 0 ? getAll(rowAttr) : null;
			String all = (crossJoin == null && numSubTotals > 0) ? "{ %s, " + attrAll + "}" : "%s"; //build all in first crossJoin only for last element
			if (config.getAxisFilters().containsKey(rowAttr)) {
				//build a separate rowset to have a more readable MDX 
				attrNode = withPrefix + (++setId);
				with.append(" SET " + attrNode + " AS '" + String.format(all, toAxisAttrFilter(rowAttr, config.getAxisFilters().get(rowAttr))) + "'");
			} else {
				attrNode = String.format(all, rowAttr.toString());
			}
			if (crossJoin == null) { 
				crossJoin = attrNode; //note: if 1 row element, then no crossjoins/unions are needed and this element will be simply returned  
				if (attrAll != null) prevAttrAll = attrAll;
			} else {
				//for query with no totals, it is enough to do cross join 
				crossJoin = String.format(MoConstants.FUNC_CROSS_JOIN_FORMAT, attrNode, crossJoin);
				//for query with totals, we need to cross join 'all' members and make a union with standard members cross join
				if (doTotals) {
					prevAttrAll = String.format(MoConstants.FUNC_CROSS_JOIN_FORMAT, attrAll, prevAttrAll);
				}
				if (numSubTotals > 0 || !iter.hasPrevious() && doTotals) {
					crossJoin = String.format(MoConstants.FUNC_UNION_FORMAT, crossJoin, prevAttrAll); //totals are displayed after level's members
				}
			}
			numSubTotals--;
		}
		return crossJoin;
	}
	
	private String getWhere(MDXConfig config) throws AmpApiException {
		if (config.getLevelFilters().size() == 0 && config.getDataFilters().size() == 0) return "";
		
		final String sep = ", ";
		//initialize with the number of commas ","  
		int size = ((config.getDataFilters().entrySet().size() == 0 ? 0 : config.getDataFilters().entrySet().size() - 1)
				+ (config.getLevelFilters().size() == 0 ? 0 : config.getLevelFilters().size() - 1) 
				+ (config.getDataFilters().entrySet().size() > 0 && config.getLevelFilters().size() > 0 ? 1 : 0)) * sep.length();
		boolean doCrossJoin = false;
		
		
		//store groups of filters by same level to reduce the number of cross joins
		Map<String, List<String>> filtersMap = new HashMap<String, List<String>>();
		Set<String> usesFilterFunc = new HashSet<String>();
		 
		for (Map.Entry<MDXAttribute, MDXFilter> pair:config.getDataFilters().entrySet()) {
			String filter = toFilter(pair.getKey().toString(), pair.getKey().getCurrentMemberName(), pair.getKey(), pair.getValue());
			size += filter.length();
			addToFilterMap(filtersMap, filter, pair.getKey());
			doCrossJoin = doCrossJoin || pair.getValue().singleValue == null;
			if (isFilterFuncUsed(pair.getKey(), pair.getValue())) {
				usesFilterFunc.add(pair.getKey().getFullName());
				doCrossJoin = true;
			}
		}
		for (MDXAttribute mdxAttr:config.getLevelFilters()) {
			String filter = mdxAttr.toString();
			size += filter.length();
			addToFilterMap(filtersMap, filter, mdxAttr);
		}
		
		if (filtersMap.keySet().size() <= 1) //though cannot be 0
			doCrossJoin = false;
		else
			size += filtersMap.keySet().size() * 2 //2 = {} to be added around each filter set, except if Filter func was used, so .size() is max number of {} 
					+ (filtersMap.keySet().size() - 1) * (MoConstants.FUNC_CROSS_JOIN.length() + 2); //2 = ()
		
		final String prefix = " WHERE (";
		StringBuilder whereFilter = new StringBuilder(size + prefix.length() + 1); //1 for closing )
		whereFilter.append(prefix);
		
		for(Iterator<List<String>> iter = filtersMap.values().iterator(); iter.hasNext(); ) {
			List<String> filterList = iter.next();
			if (doCrossJoin)
				if (iter.hasNext())
					whereFilter.append(MoConstants.FUNC_CROSS_JOIN + "({");
				else
					whereFilter.append("{");
			String values = filterList.toString();
			whereFilter.append(values.substring(1, values.length()-1)); //strip of []
			if (doCrossJoin)
				whereFilter.append("}");
			if (iter.hasNext())
				whereFilter.append(",");
		}
		if (doCrossJoin)
			for(int i = 0; i < filtersMap.values().size() -1; i++)
				whereFilter.append(")");
		whereFilter.append(")");
		
		return whereFilter.toString();
	}
	
	private int addToFilterMap(Map<String, List<String>> filtersMap, String filter, MDXAttribute mdxAttr) {
		String filterKey = mdxAttr.getFullName();
		List<String> filtersList = filtersMap.get(filterKey);
		if (filtersList == null) {
			filtersList = new ArrayList<String>();
			filtersMap.put(filterKey, filtersList);
		}
		filtersList.add(filter);
		return filter.length();
	}
	
	private String toAxisAttrFilter(MDXAttribute mdxAttr, MDXFilter mdxFilter) throws AmpApiException {
		return toFilter(mdxAttr.toString(), mdxAttr.getCurrentMemberName(), mdxAttr, mdxFilter);
	}
	
	private String toFilter(String toFilterSet, MDXElement filterBy, MDXFilter mdxFilter) throws AmpApiException {
		return toFilter(toFilterSet, filterBy.toString(), filterBy, mdxFilter);
	}
	
	private String toFilter(String toFilterSet, String filterBy, MDXElement ref, MDXFilter mdxFilter) throws AmpApiException {
		String filter = "";
		boolean isMeasure = ref instanceof MDXMeasure;
		boolean addFilterFunc = isMeasure || mdxFilter.property !=null;
		
		//this is a filter by a property value
		if (mdxFilter.property !=null) {
			if (isMeasure) 
				throw new AmpApiException("Not supported. Keys are applicable to MDX Attributes only.");
			
			MDXLevel propLevel = propertiesLevels.get(((MDXAttribute)ref).getDimension()); 
			if (propLevel == null)
				throw new AmpApiException("No level with properties is defined for dimension = " + ((MDXAttribute)ref).getDimension() + ". Define a 'default' property to flag the level to be used");
			
			String propertyType = levelPropertyType.get(propLevel).get(mdxFilter.property);
			if (propertyType == null) 
				throw new AmpApiException("Property '" + mdxFilter.property + "' not defined within dimension = " + ((MDXAttribute)ref).getDimension());
			
			//replace filter by to be filter by key
			filterBy = MoConstants.FUNC_CAST + "(" + propLevel.getCurrentMemberName() + "." 
					+ MoConstants.PROPERTIES + "('" + mdxFilter.property + "') AS " +  propertyType + ")";
			toFilterSet = propLevel.toString();
		} 
		
		//one value to select
		if (!isMeasure && mdxFilter.singleValue != null) {
			if (mdxFilter.property != null)
				filter = filterBy + (mdxFilter.allowedFilteredValues ? " = " : " <> ") + mdxFilter.singleValue; 
			else {
				filter = toMDX(ref, mdxFilter.singleValue);
			}
		} else {
			//multiple values to select			
			if (mdxFilter.filteredValues!=null) {
				if (!isMeasure && mdxFilter.property!=null) {
					//TODO: we should find another solution to make a similar approach done with strings, i.e. IN / NOT IN {list}, which is not working with simple integers
					StringBuilder sb = new StringBuilder((filterBy.length() + 20) *mdxFilter.filteredValues.size());
					String sep = " OR ";
					String operator = (mdxFilter.allowedFilteredValues ? " = " : " <> ");
					for (String value: mdxFilter.filteredValues) {
						sb.append(filterBy).append(operator).append(value).append(sep);
					} 
					filter = sb.substring(0, sb.length()-sep.length());
				} else {
					String values = mdxFilter.filteredValues.toString();
					values = values.substring(1, values.length()-1);//strip of "[]"
					if (!isMeasure && mdxFilter.property == null) { 
						String fullName = ref.getFullName();
						StringBuilder sb = new StringBuilder((fullName.length() + 20)*mdxFilter.filteredValues.size());
						String sep = ", ";
						for (String value: mdxFilter.filteredValues) {
							sb.append(fullName).append(".").append(MDXElement.quote(value)).append(sep);
						}
						values = sb.substring(0, sb.length()-sep.length());
					}
					filter = "{" + values + "}";
					if (addFilterFunc)
						filter = filterBy + (mdxFilter.allowedFilteredValues ? " IN " : " NOT IN ") + filter;
				}
			} else {
				//a range of values
				String prefix = addFilterFunc ? "(" + filterBy : "";
				String suffix = addFilterFunc ? ")" : "";
				if (mdxFilter.startRange!=null) {
					filter += prefix 
							+ (addFilterFunc ? (mdxFilter.startRangeInclusive ? " >= " : " > ") : "") 
							+ (isMeasure ? mdxFilter.startRange : toMDX(ref, mdxFilter.startRange)) 
							+ suffix 
							+ (mdxFilter.endRange!=null ? (addFilterFunc ? " AND " : ":") : ""); 
				} else if (!addFilterFunc) {
					filter += toFirstChild(ref);
				}
				if (mdxFilter.endRange!=null) {
					filter += prefix 
							+ (addFilterFunc ? (mdxFilter.endRangeInclusive ? " <= " : " < ") : "") 
							+ (isMeasure ? mdxFilter.endRange : toMDX(ref, mdxFilter.endRange)) 
							+ suffix;
				} else if (!addFilterFunc) {
					filter += toLastChild(ref);
				}
			}
		}
		if (addFilterFunc && !"".equals(filter))
			filter = MoConstants.FUNC_FILTER + "(" + toFilterSet + ", " +filter + ")";
		//should not be this
		if ("".equals(filter)) 
			filter = toFilterSet;
		return filter;
	}
	
	private boolean isFilterFuncUsed(MDXElement ref, MDXFilter mdxFilter) {
		return ref instanceof MDXMeasure || mdxFilter.property !=null;
	}
	
	private String toMDX(MDXElement elem, String value) {
		if (!elem.hasFullName()) {
			logger.error("MDXElement without fullname: " + elem.getFullName());
			return "";
		}
		return elem.getFullName() + "." + MDXElement.quote(value);
	}
	
	private String toFirstChild(MDXElement elem) {
		return elem.getFullName() + "." + MoConstants.FIRST_CHILD;
	}
	
	private String toLastChild(MDXElement elem) {
		return elem.getFullName() + "." + MoConstants.LAST_CHILD;
	}
	
	private String getAll(MDXAttribute mdxAttr) throws AmpApiException {
		String all = allNames==null ? null : allNames.get(mdxAttr.getDimensionAndHierarchy());
		if (all==null)
			throw new AmpApiException("No 'All' definition found for '" + mdxAttr.getDimensionAndHierarchy() + "'");
		return all;
	}
	
	/* candidate for removal
	private String toMDXGroup(List<String> list, String groupFuncion, String defaultVal) {
		switch (list.size()) {
		case 0: return defaultVal;
		case 1: return list.get(0);
		default: //>=2
			StringBuilder result = new StringBuilder(list.size() * (list.get(0).length() + 10)); //10=buffer for string length variations
			addFunc(list.listIterator(list.size()), groupFuncion, result);
			return result.toString();
		}
	}
	*/
	
	/**
	 * traverses the list in reverse order and builds the function list
	 * @param reversIterator
	 * @param func
	 * @param 
	 * @return
	 */
	/* candidate for removal
	private void addFunc(ListIterator<String> reversIterator, String func, StringBuilder result) {
		//if at least 2 elements are left
		if (reversIterator.previousIndex() > 0) {
			String value = reversIterator.previous(); 
			result.append(func).append("(");
			addFunc(reversIterator, func, result);
			result.append(", ").append(value).append(")");
		} else {
			//last element
			result.append(reversIterator.previous());
		}
	}
	*/
	
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
	
	/** replaces attr hierarchy with duplicate one; supporting method for of {@link #adjustDuplicateElementsOnDifferentAxis */ 
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
	public void validate(String mdx) throws AmpApiException {
		try {
			validator.validateSelect(parser.parseSelect(mdx));
		} catch (Exception e) {
			String err = MondrianUtils.toString(e);
			//for some reason validation fails when Properties are in MDX queries and a NullPointerException is thrown
			//the MDX is valid (verified in Saiku) so we'll avoid throwing exception only in this particular case
			if (!(mdx.contains(MoConstants.PROPERTIES) && e instanceof NullPointerException)) {
				logger.error("Invalid MDX query\"" + mdx + "\". \nError details: " + err);
				throw new AmpApiException("Invalid MDX query:" + err);
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
	
	/**
	 * Executes MDX query string
	 * @param mdx MDX query string
	 * @return CellSet result of MDX query see {@link CellSet}
	 * @throws AmpApiException
	 */
	public CellSet runQuery(String mdx) throws AmpApiException {
		CellSet res = null;
		try {
			res = this.olapConnection.createStatement().executeOlapQuery(mdx);
		} catch (OlapException e) {
			logger.error("Could not execute Olap Query \"" + mdx + "\", Error Details: " + MondrianUtils.getOlapExceptionMessage(e));
			throw new AmpApiException("Could not execute Olap Query");
		}
		return res; 
	}
	
	/**
	 * 				WON'T USE THE ORG.OLAP4J.QUERY API - it is experimental and limited
	 */
	/*
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

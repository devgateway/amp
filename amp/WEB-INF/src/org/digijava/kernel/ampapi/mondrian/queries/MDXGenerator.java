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

import mondrian.olap.MondrianException;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.error.keeper.ErrorReportingPlugin;
import org.digijava.kernel.ampapi.exception.AmpApiException;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXAttribute;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXConfig;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXElement;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXFilter;
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
import org.olap4j.query.SortOrder;

/**
 * MDX Query Generator
 * @author Nadejda Mandrescu
 *
 */
public class MDXGenerator {
	protected static final Logger logger = Logger.getLogger(MDXGenerator.class); 
	private static final Map<String, String> allNames;
	//initialization of "All Members" names from all dimensions in the cube
	static {
		Map<String, String> mapping = new HashMap<String, String>();
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
					boolean found = false;
					for (Iterator<Level> l = h.next().getLevels().iterator(); l.hasNext() && !found;)
						for (Member m: l.next().getMembers())
							if (m.isAll()) {
								mapping.put(MDXElement.quote(m.getHierarchy().getName()), m.toString());
								if (dim.getHierarchies().size() == 1) //keep also the default All member per dimension when there is only 1 hierarchy 
									mapping.put(MDXElement.quote(dim.getName()), m.toString());
								found = true;
								break;
							}
				}
			success = true;
		} catch (OlapException e) {
			logger.error(MondrianUtils.getOlapExceptionMessage(e));
		} catch (MondrianException e) {
			logger.error(MondrianUtils.getMondrianException(e));
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			allNames = success ? mapping : null;
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
		String notEmpty = config.isAllowEmptyData() ? "" : "NON EMPTY ";
		
		adjustDuplicateElementsOnDifferentAxis(config);

		String axisMdx = null;
		
		/* COLUMNS */
		//if column attributes are not configured, display only measures
		axisMdx = getColumns(config, with);
		
		columns  =  notEmpty + axisMdx + columns;
		
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
		rows  = notEmpty + axisMdx + rows;
		
		/* WHERE  (slice data, aka filters in Reports) */
		if (config.getLevelFilters().size()>0 || config.getDataFilters().size()>0) {
			StringBuilder whereFilter = new StringBuilder(20 * (config.getDataFilters().size() + config.getLevelFilters().size())); 
			for (Map.Entry<MDXAttribute, MDXFilter> pair:config.getDataFilters().entrySet()) {
				whereFilter.append(",").append(toFilter(pair.getKey().toString(), pair.getKey().getCurrentMemberName(), pair.getKey(), pair.getValue()));
			}
			for (MDXAttribute mdxAttr:config.getLevelFilters()) {
				whereFilter.append(",").append(mdxAttr.toString());
			}
			where = " WHERE (" + whereFilter.append(")").substring(1);
		}
		
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
					with, "COLSET", axisMdx, config.isDoColumnsTotals());
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
		return getAxis(config, config.getRowAttributes().listIterator(config.getRowAttributes().size()), with, "ROWSET", null, config.isDoColumnsTotals());
	}
	
	/** supporting common method used by getRows and getColumns, because the algorithm is the same similar */
	private String getAxis(MDXConfig config, ListIterator<MDXAttribute> reverseIterator, StringBuilder with, String withPrefix,
							String initMember, boolean doTotals) throws AmpApiException {
		int setId = 0; //identifier suffix for 'with' member name
		String crossJoin = initMember;
		String prevAttrAll = initMember; //used if config.isDoColumnsTotals() is true
		
		for (ListIterator<MDXAttribute> iter = reverseIterator; iter.hasPrevious(); ) {
			MDXAttribute rowAttr = iter.previous();
			String attrNode = null;
			String attrAll = doTotals ? getAll(rowAttr) : null;
			String all = (crossJoin == null && doTotals) ? "{ %s, " + attrAll + "}" : "%s"; //build all in first crossJoin only for last element
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
				if (attrAll != null) {
					prevAttrAll = String.format(MoConstants.FUNC_CROSS_JOIN_FORMAT, attrAll, prevAttrAll);
					crossJoin = String.format(MoConstants.FUNC_UNION_FORMAT, crossJoin, prevAttrAll); //totals are displayed after level's members
				}
			}
		}
		return crossJoin;
	}
	
	private String toAxisAttrFilter(MDXAttribute mdxAttr, MDXFilter mdxFilter) {
		return toFilter(mdxAttr.toString(), mdxAttr.getCurrentMemberName(), mdxAttr, mdxFilter);
	}
	
	private String toFilter(String toFilterSet, MDXElement filterBy, MDXFilter mdxFilter) {
		return toFilter(toFilterSet, filterBy.toString(), filterBy, mdxFilter);
	}
	
	private String toFilter(String toFilterSet, String filterBy, MDXElement ref, MDXFilter mdxFilter) {
		String filter = "";
		boolean isMeasure = ref instanceof MDXMeasure;
		//one value to select
		if (!isMeasure && mdxFilter.allowedFilteredValues && mdxFilter.filteredValues!=null && mdxFilter.filteredValues.size()==1)
			filter = toMDX(ref, mdxFilter.filteredValues.get(0).toString());
		else if (!isMeasure && mdxFilter.endRangeInclusive && mdxFilter.startRangeInclusive && mdxFilter.startRange.equals(mdxFilter.endRange)) {
			filter = toMDX(ref, mdxFilter.startRange);
		}
		else {
			//multiple values to select
			if (mdxFilter.filteredValues!=null) {
				String values = isMeasure ? mdxFilter.filteredValues.toString() : null;
				if (isMeasure) {
					values = values.substring(1, values.length()-2);//strip of "[]"
				} else {
					String fullName = ref.getFullName();
					StringBuilder sb = new StringBuilder((fullName.length() + 20)*mdxFilter.filteredValues.size());
					String sep = ", ";
					for (String value: mdxFilter.filteredValues) {
						sb.append(fullName).append(".").append(MDXElement.quote(value)).append(sep);
					}
					values = sb.substring(0, sb.length()-sep.length());
				}
				filter = filterBy + (mdxFilter.allowedFilteredValues ? " IN " : " NOT IN ") + "{" + values + "}";
			} else {
				if (mdxFilter.startRange!=null) {
					filter += "(" + filterBy + (mdxFilter.startRangeInclusive ? " >= " : " > ") 
							+ (isMeasure ? mdxFilter.startRange : toMDX(ref, mdxFilter.startRange)) + ")"
							+ (mdxFilter.startRange!=null ? " AND " : ""); 
				}
				if (mdxFilter.endRange!=null) {
					filter += "(" + filterBy + (mdxFilter.endRangeInclusive ? " <= " : " < ") 
							+ (isMeasure ? mdxFilter.endRange : toMDX(ref, mdxFilter.endRange)) + ")";
				}
			}
			if ( !"".equals(filter)) {
				filter = MoConstants.FUNC_FILTER + "(" + toFilterSet + ", " +filter + ")";
			}
		}
		//should not be this
		if ("".equals(filter)) 
			filter = toFilterSet;
		return filter;
	}
	
	private String toMDX(MDXElement elem, String value) {
		if (!elem.hasFullName()) {
			logger.error("MDXElement without fullname: " + elem.getFullName());
			return "";
		}
		return elem.getFullName() + "." + MDXElement.quote(value);
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
		buildSuffixAndPrefix(fullPrefix, fullSuffix, iter);
		fullPrefix.append(prefix);
		fullSuffix.append(delim).append(elem.getKey().toSortingString()).append(delim).append(elem.getValue().name()).append(end);
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
			logger.error("Invalid MDX query\"" + mdx + "\". \nError details: " + err);
			throw new AmpApiException("Invalid MDX query:" + err);
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

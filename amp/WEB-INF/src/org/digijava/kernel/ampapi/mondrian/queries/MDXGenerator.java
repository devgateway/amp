/**
 * 
 */
package org.digijava.kernel.ampapi.mondrian.queries;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import mondrian.olap.MondrianException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.error.keeper.ErrorReportingPlugin;
import org.digijava.kernel.ampapi.exception.AmpApiException;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXAttribute;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXConfig;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXElement;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXFilter;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXMeasure;
import org.digijava.kernel.ampapi.mondrian.util.Connection;
import org.digijava.kernel.ampapi.mondrian.util.MoConstants;
import org.digijava.kernel.ampapi.mondrian.util.MondrianUtils;
import org.olap4j.CellSet;
import org.olap4j.OlapConnection;
import org.olap4j.OlapException;
import org.olap4j.mdx.parser.MdxParseException;
import org.olap4j.mdx.parser.MdxParser;
import org.olap4j.mdx.parser.MdxValidator;
import org.olap4j.metadata.Cube;
import org.olap4j.metadata.Dimension;
import org.olap4j.metadata.Hierarchy;
import org.olap4j.metadata.Level;
import org.olap4j.metadata.Member;
import org.olap4j.metadata.NamedList;

/**
 * MDX Query Generator
 * @author Nadejda Mandrescu
 *
 */
public class MDXGenerator {
	protected static final Logger logger = Logger.getLogger(MDXGenerator.class); 
	private static final Map<String, String> allNames; 
	static {
		Map<String, String> mapping = new HashMap<String, String>();
		boolean success = false;
		OlapConnection olapConnection = null;
		try {
			olapConnection = Connection.getOlapConnectionByConnPath(Connection.getDefaultConnectionPath());
			NamedList<Dimension> dimList = olapConnection.getOlapSchema().getSharedDimensions();
			for (Cube cube:olapConnection.getOlapSchema().getCubes()) {
				dimList.addAll(cube.getDimensions());
			}
			for (Dimension dim:dimList) {
				boolean found = false;
				for (Iterator<Hierarchy> h = dim.getHierarchies().iterator(); h.hasNext() && !found;)
					for (Iterator<Level> l = h.next().getLevels().iterator(); l.hasNext() && !found;)
						for (Member m: l.next().getMembers())
							if (m.isAll()) {
								mapping.put(dim.getName(), m.getName());
								found = true;
								//mapping.put(dim.getName()+ h.getName(), m.getName());
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
	
	private void tearDown() {
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
		//SelectNode select = new SelectNode();
		//select.setFrom(new IdentifierNode(new NameSegment(cubeName)));
		
		String with = "";
		String mdx = "SELECT ";
		String from = " FROM [" + cubeName + "]";
		String columns = " ON COLUMNS, ";
		String rows = " ON ROWS ";
		String where = "";
		String notEmpty = config.isAllowEmptyData() ? "" : "NON EMPTY ";
		int setCount = 0;
		

		String axisMdx = null;
		
		//columns
		//AxisNode axisNode = select.getAxisList().get(Axis.COLUMNS.axisOrdinal());
		//axisNode.setNonEmpty(config.isAllowEmptyData());
		List<String> sortingList = new ArrayList<String>(); 
		List<String> crossJoins = new ArrayList<String>();
		//if column attributes are not configured, display only measures
		if (config.getColumnAttributes().size()==0) {
			axisMdx = "";
			for (MDXMeasure colMeasure:config.getColumnMeasures()) {
				axisMdx += "," + colMeasure.toString();
			}
			axisMdx = "{Hierarchize({" + axisMdx.substring(1) + "})}"; //
		} else {
			for (MDXAttribute colAttr:config.getColumnAttributes()) {
				String attrNode = null;
				String all = config.isDoRowTotals() ? "{ %s, " + getAll(colAttr) + "}" : "%s";
				if (config.getAxisFilters().containsKey(colAttr)) {
					attrNode = "COLSET" + (++setCount);
					with += " SET " + attrNode + " AS '"  
							+ String.format(all, toAxisAttrFilter(colAttr, config.getAxisFilters().get(colAttr))) 
							+ "'";
				} else {
					attrNode = String.format(all, colAttr.toString());
				}
				for (MDXMeasure colMeasure:config.getColumnMeasures()) {
					String measureNode = colMeasure.toString();
					crossJoins.add(MoConstants.FUNC_CROSS_JOIN + "(" + attrNode + ", {" + measureNode +"}" + ")");
					if (colMeasure.getSortAsc()!=null)
						sortingList.add(measureNode + ", " + (colMeasure.getSortAsc() ? "ASC" : "DESC"));
				}
			}
			axisMdx = toMDXGroup(crossJoins, MoConstants.FUNC_UNION, "");
		}
		columns  =  notEmpty + axisMdx + columns;
		
		//rows
		//TODO: do we expect measures on rows?
		List<String> attrList = new ArrayList<String>();
		for (MDXAttribute rowAttr: config.getRowAttributes()) {
			String attrNode = null;
			String all = config.isDoColumnsTotals() ? "{ %s, " + getAll(rowAttr) + "}" : "%s";
			if (config.getAxisFilters().containsKey(rowAttr)) {
				attrNode = "COLSET" + (++setCount);
				with += " SET " + attrNode + " AS '" + String.format(all, toAxisAttrFilter(rowAttr, config.getAxisFilters().get(rowAttr))) + "'";
			} else {
				attrNode = String.format(all, rowAttr.toString());
			}
			attrList.add(attrNode);
		}
		axisMdx = toMDXGroup(attrList, MoConstants.FUNC_CROSS_JOIN, "{}");
		//filter by measures
		if (config.getAxisFilters().size()>0) {
			for (Map.Entry<MDXElement, MDXFilter> pair : config.getAxisFilters().entrySet()) {
				//normally all attr elements should have been removed by now from filter map
				if (pair.getKey() instanceof MDXMeasure) {
					axisMdx = toFilter(axisMdx, pair.getKey(), pair.getValue());
				}
			}
 		}
		//sorting
		if (sortingList.size()>0) {
			for (String sortData: sortingList) {
				axisMdx = MoConstants.FUNC_ORDER + "(" + axisMdx + ", " + sortData + ")"; //TBD
			}
		}
		rows  = notEmpty + axisMdx + rows;
		
		//where (slice data, aka filters in Reports)
		if (config.getLevelFilters().size()>0 || config.getDataFilters().size()>0) {
			axisMdx = "";
			for (Map.Entry<MDXAttribute, MDXFilter> pair:config.getDataFilters().entrySet()) {
				axisMdx += "," + toFilter(pair.getKey().toString(), currentMemeber(pair.getKey()), pair.getKey(), pair.getValue());
			}
			for (MDXAttribute mdxAttr:config.getLevelFilters()) {
				axisMdx += "," + mdxAttr.toString();
			}
			where = " WHERE (" + axisMdx.substring(1) + ")";
		}
		
		if (StringUtils.isNotBlank(with)) with = "WITH " + with;
		mdx += with + columns + rows + from + where;
		logger.info("MDX query: " + mdx);

		if (!isValid(mdx))
			throw new AmpApiException("Invalid query");
		return mdx;
	}
	
	private String toAxisAttrFilter(MDXAttribute mdxAttr, MDXFilter mdxFilter) {
		return toFilter(mdxAttr.toString(), currentMemeber(mdxAttr), mdxAttr, mdxFilter);
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
	
	private String currentMemeber(MDXAttribute mdxAttr) {
		return MDXElement.quote(mdxAttr.getDimension()) + "." + MDXElement.quote(mdxAttr.getName()) + "." + MoConstants.CURRENT_MEMBER;
	}
	
	private String getAll(MDXAttribute mdxAttr) throws AmpApiException {
		String all = allNames==null ? null : allNames.get(mdxAttr.getDimension());
		if (all==null)
			throw new AmpApiException("No 'All' definition found for Dimension '" + mdxAttr.getDimension() + "'");
		return MDXElement.quote(mdxAttr.getDimension()) + "." + MDXElement.quote(all);
	}
	
	private String toMDXGroup(List<String> list, String groupFuncion, String defaultVal) {
		switch (list.size()) {
		case 0: return defaultVal;
		case 1: return list.get(0);
		default: //>=2
		return addFunc(list, groupFuncion);
		}
	}
	
	private String addFunc(List<String> list, String func) {
		Iterator<String> iter = list.iterator();
		if (!iter.hasNext()) return "";
		String mdx = func + "(" + iter.next() + ", ";
		String end = ")";
		while (iter.hasNext()) {
			String value = iter.next();
			if (iter.hasNext()) {
				mdx += func + "(" + value + ", ";
				end += ")";
			} else {
				mdx += value;
			}
		}
		mdx += end;
		return mdx;
	}
	
	synchronized
	public boolean isValid(String mdx) throws AmpApiException {
		try {
			validator.validateSelect(parser.parseSelect(mdx));
			return true;
		} catch (MdxParseException e) {
			logger.error(MondrianUtils.getMdxParseException(e));
		} catch (OlapException e) {
			logger.error(MondrianUtils.getOlapExceptionMessage(e));
		} catch (RuntimeException e) {
			if (e.getCause() instanceof MdxParseException) {
				logger.error(MondrianUtils.getMdxParseException((MdxParseException)e.getCause()));
			} else {
				logger.error(e.getMessage());
			}
		} 
		return false;
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

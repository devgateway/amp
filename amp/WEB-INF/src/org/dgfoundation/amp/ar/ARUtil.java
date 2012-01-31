/**
 * ARUtil.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

import java.lang.reflect.Constructor;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.cell.ListCell;
import org.dgfoundation.amp.ar.cell.MetaTextCell;
import org.dgfoundation.amp.ar.cell.TextCell;
import org.dgfoundation.amp.ar.dimension.ARDimension;
import org.dgfoundation.amp.ar.dimension.DonorDimension;
import org.dgfoundation.amp.ar.dimension.DonorGroupDimension;
import org.dgfoundation.amp.ar.dimension.DonorTypeDimension;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.action.reportwizard.ReportWizardAction;
import org.digijava.module.aim.ar.util.FilterUtil;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpMeasures;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpReportColumn;
import org.digijava.module.aim.dbentity.AmpReportHierarchy;
import org.digijava.module.aim.dbentity.AmpReportMeasures;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.helper.fiscalcalendar.ComparableMonth;
import org.digijava.module.aim.helper.fiscalcalendar.EthiopianCalendar;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.dataExchange.utils.DataExchangeUtils;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Sep 1, 2006
 * 
 */
public final class ARUtil {
	/**
	 * 
	 * @param getTabs if null gets both reports and tabs, on true only tabs, on false only reports
	 * @return list of public reports
	 */
	public static ArrayList getAllPublicReports(Boolean getTabs,String name) {
		Session session = null;
		ArrayList col = new ArrayList();
		String tabFilter	= "";

		if ( getTabs!=null ) {
			tabFilter	= "r.drilldownTab=:getTabs AND";
		}
		try {

			session = PersistenceManager.getSession();
			String queryString = "select r from " + AmpReports.class.getName()
					+ " r " + "where ( " + tabFilter + " r.publicReport=true)";
            if (name != null) {
                queryString += " and r.name like :name ";
            }
			Query qry = session.createQuery(queryString);
			if ( getTabs!=null )
				qry.setBoolean("getTabs", getTabs);
             if (name != null) {
               qry.setString("name", '%' + name+ '%');
            }

			Iterator itrTemp = qry.list().iterator();
			AmpReports ar = null;
			while (itrTemp.hasNext()) {
				ar = (AmpReports) itrTemp.next();
				col.add(ar);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return col;
	}

	protected static Logger logger = Logger.getLogger(ARUtil.class);

	public static Constructor getConstrByParamNo(Class c, int paramNo) {
		Constructor[] clist = c.getConstructors();
		for (int j = 0; j < clist.length; j++) {
			if (clist[j].getParameterTypes().length == paramNo)
				return clist[j];
		}
		logger.error("Cannot find a constructor with " + paramNo
				+ " parameters for class " + c.getName());
		return null;
	}

	public static GroupReportData generateReport(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws java.lang.Exception {

		String ampReportId = request.getParameter("ampReportId");
		AmpReports r		= null;
		if (ampReportId == null || ampReportId.length() == 0 )
			ampReportId = (String) request.getAttribute("ampReportId");
		if ( ampReportId == null || ampReportId.length() == 0 || Long.parseLong(ampReportId) <= 0) {
			r		= (AmpReports) request.getSession().getAttribute("reportMeta");
		}
		HttpSession httpSession = request.getSession();
		Session session = PersistenceManager.getSession();

		TeamMember teamMember = (TeamMember) httpSession
				.getAttribute("currentMember");
		if ( r == null) {
			r = (AmpReports) session.get(AmpReports.class, new Long(ampReportId));
		}
		// the siteid and locale are set for translation purposes
		Site site = RequestUtils.getSite(request);
		Locale navigationLanguage = RequestUtils.getNavigationLanguage(request);
		String siteId = site.getId().toString();
		String locale = navigationLanguage.getCode();

		r.setSiteId(siteId);
		r.setLocale(locale);

		httpSession.setAttribute("reportMeta", r);

		if (teamMember != null)
			logger.info("Report '" + r.getName() + "' requested by user "
					+ teamMember.getEmail() + " from team "
					+ teamMember.getTeamName());

		AmpARFilter af = (AmpARFilter) httpSession
				.getAttribute(ArConstants.REPORTS_FILTER);
		if (af == null)
			af = new AmpARFilter();
		af.readRequestData(request);
		Object initFilter	= request.getAttribute(ArConstants.INITIALIZE_FILTER_FROM_DB);
		if ( initFilter!=null && "true".equals(initFilter) ) {
			FilterUtil.prepare(request, af);
			FilterUtil.populateFilter(r, af);
			httpSession.setAttribute( ReportWizardAction.EXISTING_SESSION_FILTER, af);
		}
		httpSession.setAttribute(ArConstants.REPORTS_FILTER, af);

		AmpReportGenerator arg = new AmpReportGenerator(r, af, request);

		arg.generate();

		PersistenceManager.releaseSession(session);

		return arg.getReport();

	}

	public static Collection getFilterDonors(AmpTeam ampTeam) {
		ArrayList dbReturnSet = null;
		ArrayList ret = new ArrayList();
		dbReturnSet = DbUtil.getAmpDonors(ampTeam.getAmpTeamId());
		// logger.debug("Donor Size: " + dbReturnSet.size());
		Iterator iter = dbReturnSet.iterator();

		while (iter.hasNext()) {
			AmpOrganisation ampOrganisation = (AmpOrganisation) iter.next();
			if (ampOrganisation.getAcronym().length() > 20) {
				String temp = ampOrganisation.getAcronym().substring(0, 20)
						+ "...";
				ampOrganisation.setAcronym(temp);
			}
			ret.add(ampOrganisation);
		}

		return ret;
	}

	public static Collection<AmpOrgGroup> filterDonorGroups(Collection donorGroups) {
		Collection<AmpOrgGroup> ret = new ArrayList<AmpOrgGroup>();
		if (donorGroups == null) {
			logger
					.error("Collection of AmpOrgGroup should NOT be null in filterDonorGroups");
			return ret;
		}
		Iterator iter = donorGroups.iterator();
		while (iter.hasNext()) {
			AmpOrgGroup grp = (AmpOrgGroup) iter.next();
			if (grp.getOrgType() != null
					&& grp.getOrgType().getOrgType() != null
					&& (grp.getOrgType().getOrgType().toLowerCase().contains(
							"gov") || grp.getOrgType().getOrgType()
							.toLowerCase().contains("gouv"))) {
				continue;
			}
			ret.add(grp);
		}

		Collections.sort((List)ret, new DbUtil.HelperAmpOrgGroupNameComparator());
		
		return ret;
	}

	/**
	 * Checks if the hierarchy with the specified name as a parameter is present amount the given collection
	 * @param hierarchies the collection of hierarchies that the report holds persisted
	 * @param hierarchyName the name of the hierarchy to check
	 * @return true if the hierarchy exists
	 */
	public static boolean hasHierarchy(Collection<AmpReportHierarchy> hierarchies,String hierarchyName) {
		if (hierarchies != null) {
			for (AmpReportHierarchy h : hierarchies) {
				if(h.getColumn().getColumnName().equals(hierarchyName)) return true;
			}
		}
		return false;
	}
	
	public static boolean containsMeasure(String measureName, Set measures) {
		if (measureName == null)
			return false;
		Iterator i = measures.iterator();
		while (i.hasNext()) {
			AmpMeasures element = ((AmpReportMeasures) i.next()).getMeasure();
			if (element.getMeasureName().indexOf(measureName) != -1)
				return true;
		}
		return false;
	}
	
	
	public static boolean hasHeaderValue(AmpColumns column){
		return (column.getTotalExpression()!=null )|| column.getColumnName().equalsIgnoreCase(ArConstants.COLUMN_FUNDING);
	}
	
	public static boolean containsColumn(String columName, Set columns) {
		if (columName == null)
			return false;
		Iterator i = columns.iterator();
		while (i.hasNext()) {
			AmpReportColumn element = (AmpReportColumn) i.next();
			if (element.getColumn().getColumnName().indexOf(columName) != -1)
				return true;
		}
		return false;
	}

	public static List createOrderedHierarchies(Collection columns,
			Collection hierarchies) {
		List orderedColumns = new ArrayList(hierarchies.size());
		for (int x = 0; x < hierarchies.size() + columns.size(); x++) {
			Iterator i = hierarchies.iterator();
			while (i.hasNext()) {
				AmpReportHierarchy element = (AmpReportHierarchy) i.next();
				int order = element.getLevelId().intValue();
				if (order - 1 == x)
					orderedColumns.add(element);
			}
		}
		return orderedColumns;
	}

	/**
	 * Creates a list in the index order of the report wizard selected column
	 * order.
	 * 
	 * @param columns
	 * @param hierarchies
	 *            this set is needed because also hierarchies were ordered by
	 *            the same values and we need the max
	 * @return
	 */
	public static List createOrderedColumns(Collection columns, Set hierarchies) {
		List orderedColumns = new ArrayList(columns.size());
		for (int x = 0; x < columns.size() + hierarchies.size(); x++) {
			Iterator i = columns.iterator();
			while (i.hasNext()) {
				AmpReportColumn element = (AmpReportColumn) i.next();
				 int order = element.getOrderId().intValue();
				if (order - 1 == x)
					orderedColumns.add(element);
			}
		}
		return orderedColumns;
	}
	
	public static void insertEmptyColumns (String type, CellColumn src, Set<MetaInfo> destMetaSet, AmpARFilter filter) {
		Iterator iter									= src.iterator();
		TreeSet<Comparable<? extends Object>>	periods	= new TreeSet<Comparable<? extends Object>>();
		try{
			ARUtil.initializePeriodValues(type, periods, filter);
		
			while ( iter.hasNext() ) {
				Categorizable elem	= (Categorizable)iter.next();
				MetaInfo minfo		= MetaInfo.getMetaInfo(elem.getMetaData(),type );
				Comparable c 		= minfo.getValue();
				if ( c.compareTo( periods.first() ) > 0 &&
						c.compareTo( periods.last() ) < 0 && elem.isShow() )
					periods.add( minfo.getValue() );
			}
	
			if (periods!=null && periods.size()>0) {
				Object prevPeriod					= null;
				Object first							= periods.first();
				Object last								= periods.last();
				Iterator periodIter					= periods.iterator();
				while ( periodIter.hasNext() ) {
					Object period			= periodIter.next();
					System.out.println("Year found:" + period );
					int difference			= 0;
					if ( prevPeriod != null && 
							(difference=ARUtil.periodDifference(type, prevPeriod, period)) > 1 ) {
						for (int i=1; i< difference; i++) {
							Comparable comparable	= ARUtil.getFuturePeriod(type, prevPeriod, i, first, last);
							if (comparable != null)
								destMetaSet.add( new MetaInfo(type, comparable) );
						}
						
					}
					prevPeriod		= period;
				}
			
			}
		}
		catch (Exception e) {
			logger.error( e.getMessage() );
			e.printStackTrace();
		}
		
	}
	private static Comparable getFuturePeriod(String type, Object period, int step,  Object first, Object last) throws Exception{
		if ( ArConstants.YEAR.equals( type ) ) {
			Integer firstEl		= (Integer) first;
			Integer lastEl			= (Integer) last;
			System.out.println("Adding year:" + (((Integer)period) + step) );
			if ( firstEl != null && 
					(((Integer)period) + step) < firstEl )
				return null;
			if ( lastEl != null && 
					(((Integer)period) + step) > lastEl)
				return null;
			
			return ((Integer)period) + step;
		}
		
		if ( ArConstants.QUARTER.equals( type ) ) {
			String quarter			= ((String)period ).substring(1);
			Integer quarterId		= ( Integer.parseInt(quarter) );
			if ( quarterId >= 4 )
				throw new Exception( "There is no quarter greater than: " + quarterId );
			if ( quarterId + step > 4 )
				throw new Exception( "Max quarter is 4. Trying to generate quarter: " + (quarterId + step) );
			return "Q" + (quarterId + step); 
		}
		
		if ( ArConstants.MONTH.equals( type ) ) {
			ComparableMonth month 					= ((ComparableMonth) period);
			
			String gsCal 							= FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_CALENDAR);
			AmpFiscalCalendar ampFiscalCalendar 	= FiscalCalendarUtil.getAmpFiscalCalendar(Long.parseLong(gsCal));
			if ( ampFiscalCalendar == null )
				throw new Exception("Cannot obtain default AmpFiscalCalendar");
			
			if ( "GREG-CAL".equalsIgnoreCase( ampFiscalCalendar.getBaseCal() ) ) {
				if ( month.getMonthId() >= 11 )
					throw new Exception("Calendar type is "+ ampFiscalCalendar.getBaseCal() +
							". There is no month greater than: " + month.getMonthId());
				Integer newMonthId			= month.getMonthId() + step;
				if ( newMonthId > 11 )
					throw new Exception("Calendar type is "+ ampFiscalCalendar.getBaseCal() +
							". Max month is 11. Trying to generate month: " + newMonthId);
				DateFormatSymbols dfs		= new DateFormatSymbols();
				return new ComparableMonth(newMonthId, dfs.getMonths()[newMonthId]);
			}
			if ( "ETH-CAL".equalsIgnoreCase( ampFiscalCalendar.getBaseCal() ) ) {
				if ( month.getMonthId() >= 13 )
					throw new Exception("Calendar type is "+ ampFiscalCalendar.getBaseCal() +
							". There is no month greater than: " + month.getMonthId());
				Integer newMonthId			= month.getMonthId() + step;
				if ( newMonthId > 13 )
					throw new Exception("Calendar type is "+ ampFiscalCalendar.getBaseCal() +
							". Max month is 13. Trying to generate month: " + newMonthId);
				return new ComparableMonth( newMonthId, new EthiopianCalendar().ethMonths(newMonthId) );
			}
			throw new Exception("Unknown calendar type: " + ampFiscalCalendar.getBaseCal() );
		}
		
		throw new Exception("The specified type is neither YEAR, QUARTER nor MONTH: " + type);
	}
	private static int periodDifference(String type, Object period1, Object period2) throws Exception{
		if ( ArConstants.YEAR.equals( type ) ) {
			return ((Integer)period2) - ((Integer)period1);
		}
		
		if ( ArConstants.QUARTER.equals( type ) ) {
			String quarter1			= ((String)period1 ).substring(1);
			Integer quarterId1		= ( Integer.parseInt(quarter1) );
			
			String quarter2			= ((String)period2 ).substring(1);
			Integer quarterId2		= ( Integer.parseInt(quarter2) );
			
			return quarterId2 - quarterId1;
		}
		
		if ( ArConstants.MONTH.equals( type ) ) {
			ComparableMonth month1 		= ((ComparableMonth) period1);
			ComparableMonth month2 		= ((ComparableMonth) period2);
			
			return month2.getMonthId() - month1.getMonthId();
			
		}
		
		throw new Exception("The specified type is neither YEAR, QUARTER nor MONTH: " + type);
	}
	private static void initializePeriodValues(String type, Collection<Comparable<? extends Object>> periods, AmpARFilter filter) throws Exception {
		if ( ArConstants.YEAR.equals(type) ) {
			if ( filter == null  )
				throw new Exception("Filter is null when adding empty years to report");
			if ( filter.getRenderStartYear() != null && filter.getRenderStartYear() > 1800  && filter.getRenderStartYear() < 2200 )
				periods.add(  filter.getRenderStartYear() -1 );
			if ( filter.getRenderEndYear() != null && filter.getRenderStartYear() > 1800  && filter.getRenderEndYear() < 2200 )
				periods.add( filter.getRenderEndYear() + 1 );
		}
		if ( ArConstants.QUARTER.equals(type) ) {
			periods.add("Q0");
			periods.add("Q5");
		}
		if ( ArConstants.MONTH.equals(type) ) {
			periods.add( new ComparableMonth(-1, "Before 1st month") );
			periods.add( new ComparableMonth(12, "After last month") );
		}
	}
	
	//AMP-6541
	public static void clearDimension(Class c){
		logger.info("removing dimension: "+c.toString());
		ARDimension.DIMENSIONS.remove(c);
	}
	
	public static void clearOrgGroupTypeDimensions(){
        clearDimension(DonorDimension.class);
        clearDimension(DonorGroupDimension.class);
        clearDimension(DonorTypeDimension.class);
	}
	
	public static void cleanReportOfHtmlCodes(GroupReportData rd){
		for (Iterator it = rd.getItems().iterator(); it.hasNext();) {
			Object o  = it.next();
			if(o instanceof GroupReportData) cleanReportOfHtmlCodes((GroupReportData)o);
			if(o instanceof ColumnReportData) {
				ColumnReportData crd = (ColumnReportData) o;
				for (Iterator j = crd.getItems().iterator(); j.hasNext();) {
					Object oo = j.next();
					if(oo instanceof CellColumn){
						CellColumn cellColumn  = (CellColumn)oo;
						for (Iterator k = cellColumn.getItems().iterator(); k.hasNext();) {
							Object cell = k.next();
							ARUtil.cleanCell(cell);
						}
					}
				}
		
			}
	
		}
	}

	private static void cleanCell(Object cell) {
		if(cell instanceof TextCell) ARUtil.cleanTextCell((TextCell)cell);
		if(cell instanceof MetaTextCell) ARUtil.cleanTextCell((TextCell)cell);
		if(cell instanceof ListCell) ARUtil.cleanListCell((ListCell)cell);
	}

	private static void cleanListCell(ListCell cell) {
		/* Donor commitment date some times is a ListCell of DateCell 
		 * in such case it should not not execute the clean */
		boolean process = false;
		for (Object o : (List)cell.getValue()) {
		    if (o.getClass().equals(TextCell.class)){
		    	process = true;
		    }
		}
		if (process){
			List<TextCell> listCells = (List)cell.getValue();
			for (TextCell tCell : listCells) {
				ARUtil.cleanTextCell(tCell);
			}
		}
	}

	private static void cleanTextCell(TextCell cell) {
		cell.setValue(DataExchangeUtils.convertHTMLtoChar((String)cell.getValue()));
	}
	
	public static double retrievePercentageFromCell ( MetaTextCell mtc ) throws Exception {
		MetaInfo<Double> mInfo	= mtc.getMetaInfo(ArConstants.PERCENTAGE);
		if ( mInfo != null && mInfo.getValue() > 0) {
			Double percentage 		= mInfo.getValue();
			return percentage;
		}
		throw new Exception("No percentage found it MetaTextCell " + mtc);
	}
	
	public static double retrieveParentPercetage( Long ownerId, Cell splitterCell ) throws Exception {
		Column col				= splitterCell.getColumn();
		Iterator<TextCell> iter	= col.getItems().iterator();
		while (iter.hasNext()) {
			TextCell	textCell		= iter.next();
			if ( textCell instanceof MetaTextCell ) {
				MetaTextCell metaTextCell 	= (MetaTextCell) textCell;
				if ( splitterCell.getValue().equals(metaTextCell.getValue()) && ownerId.equals(metaTextCell.getOwnerId() ) ) {
					Double percentage		= retrievePercentageFromCell(metaTextCell);
					return percentage;
				}
			}
		}
		return 100.0 ;
	}
	
}
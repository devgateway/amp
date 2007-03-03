/**
 * ARUtil.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.swarmcache.ObjectCache;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.util.DigiCacheManager;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpMeasures;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpRegion;
import org.digijava.module.aim.dbentity.AmpReportColumn;
import org.digijava.module.aim.dbentity.AmpReportHierarchy;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpStatus;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.form.AdvancedReportForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.aim.util.TeamUtil;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Sep 1, 2006
 * 
 */
public final class ARUtil {

	
	public static ArrayList getAllPublicReports() {
		Session session = null;
		ArrayList col = new ArrayList();

		try {

			session = PersistenceManager.getSession();
			String queryString = "select r from "
					+ AmpReports.class.getName() + " r "
					+ "where (r.publicReport=true)";
			Query qry = session.createQuery(queryString);
			
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


	public static String toSQLEnum(Collection col) {
		String ret = "";
		if (col == null || col.size() == 0)
			return ret;
		Iterator i = col.iterator();
		while (i.hasNext()) {
			Object element = (Object) i.next();
			if(element==null) continue;
			if (element instanceof String)
				ret += "'" + (String) element + "'";
			else
				ret += element.toString();
			if (i.hasNext())
				ret += ",";
		}
		return ret;
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
		HttpSession httpSession = request.getSession();
		Session session = PersistenceManager.getSession();

		TeamMember teamMember = (TeamMember) httpSession
				.getAttribute("currentMember");
		

		AmpReports r = (AmpReports) session.get(AmpReports.class, new Long(
				ampReportId));

		if(teamMember!=null)
		logger.info("Report '" + r.getName() + "' requested by user "
				+ teamMember.getEmail() + " from team "
				+ teamMember.getTeamName());

		AmpARFilter af = ARUtil.createFilter(r, mapping, form, request,
				response);
		if (af == null)
			return null;

		AmpReportGenerator arg = new AmpReportGenerator(r, af);

		arg.generate();

		session.close();

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

	/**
	 * Messy old code to create filter beans...Will deal with that laters...
	 * 
	 * @param ampReports
	 *            the reports are retrieved separately
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return false if we need to redirect to homepage
	 * @throws java.lang.Exception
	 */
	public static AmpARFilter createFilter(AmpReports ampReports,
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws java.lang.Exception {
		HttpSession httpSession = request.getSession();
		TeamMember teamMember = (TeamMember) httpSession
				.getAttribute("currentMember");

		Session session = PersistenceManager.getSession();
		
		
		ArrayList filters = null;
		Iterator iter = null;
		String setFilters = "";
		int year = 0;
		Iterator iterSub = null;
		int filterCnt = 0;

		Long ampStatusId = null;
		Long ampOrgId = null;
		Long ampSectorId = null;
		String component = null;
		Long ampModalityId = null;
		String ampCurrencyCode = null;
		Long All = new Long(0);
		Collection reports = new ArrayList();
		int startYear = 0;
		int startMonth = 0;
		int startDay = 0;
		int closeYear = 0;
		int closeMonth = 0;
		int closeDay = 0;
		int fromYr = 0;
		int toYr = 0;
		int fiscalCalId = 0;
		Long ampReportId = null;
		AmpCurrency ampCurrency = null;

		GregorianCalendar c = new GregorianCalendar();
		year = c.get(Calendar.YEAR);

	
		Long ampTeamId = null;
		if(teamMember!=null) ampTeamId=teamMember.getTeamId();
		ArrayList dbReturnSet = null;

		AdvancedReportForm formBean = (AdvancedReportForm) form;
		String perspective = "DN";


		if (request.getParameter("ampReportId") != null) {
			formBean.setCreatedReportId(request.getParameter("ampReportId"));
		}

		formBean.setReportName(ampReports.getName());
		if (ampReports.getReportDescription() != null)
			formBean.setReportDescription(ampReports.getReportDescription());
		AmpTeam ampTeam = null;
			
		if(ampTeamId!=null) ampTeam=TeamUtil.getAmpTeam(ampTeamId);
		
		if(ampTeam!=null) {
			formBean.setWorkspaceType(ampTeam.getType());
			formBean.setWorkspaceName(ampTeam.getName());
		}
		
		Long ampPageId = DbUtil.getAmpPageId(ampReports.getName());

		// Set all the filters
		formBean.setFilterFlag("false");
		formBean.setStatusColl(new ArrayList());
		formBean.setSectorColl(new ArrayList());
		formBean.setRegionColl(new ArrayList());
		formBean.setDonorColl(new ArrayList());
		formBean.setModalityColl(new ArrayList());
		formBean.setCurrencyColl(new ArrayList());
		formBean.setAmpFromYears(new ArrayList());
		formBean.setAmpToYears(new ArrayList());
		formBean.setAmpStartYears(new ArrayList());
		formBean.setAmpCloseYears(new ArrayList());
		formBean.setAmpStartDays(new ArrayList());
		formBean.setAmpCloseDays(new ArrayList());
		formBean.setFiscalYears(new ArrayList());
		formBean.setRisks(new ArrayList());
		formBean.setColumnHierarchie(new ArrayList());
		
		if(ampTeamId!=null) filters = DbUtil.getTeamPageFilters(ampTeamId, ampPageId);
		// logger.debug("Filter Size: " + filters.size());

		formBean.setGoFlag("true");
		formBean.setFilterFlag("true");
		setFilters = setFilters + " PERSPECTIVE -";

		dbReturnSet = DbUtil.getAmpStatus();
		formBean.setStatusColl(dbReturnSet);
		setFilters = setFilters + " STATUS -";
		filterCnt++;

		setFilters = setFilters + " SECTOR -";
		filterCnt++;
		formBean.setSectorColl(new ArrayList());
		dbReturnSet = SectorUtil.getAmpSectors();
		iter = dbReturnSet.iterator();
		while (iter.hasNext()) {
			AmpSector ampSector = (AmpSector) iter.next();
			if (ampSector.getName().length() > 20) {
				String temp = ampSector.getName().substring(0, 20) + "...";
				ampSector.setName(temp);
			}
			formBean.getSectorColl().add(ampSector);
			dbReturnSet = SectorUtil.getAmpSubSectors(ampSector
					.getAmpSectorId());
			iterSub = dbReturnSet.iterator();
			while (iterSub.hasNext()) {
				AmpSector ampSubSector = (AmpSector) iterSub.next();
				String temp = " -- " + ampSubSector.getName();
				if (temp.length() > 20) {
					temp = temp.substring(0, 20) + "...";
					ampSubSector.setName(temp);
				}
				ampSubSector.setName(temp);
				formBean.getSectorColl().add(ampSubSector);
			}
		}

		setFilters = setFilters + " REGION -";
		filterCnt++;
		dbReturnSet = LocationUtil.getAmpLocations();
		formBean.setRegionColl(dbReturnSet);

		setFilters = setFilters + " DONORS -";
		filterCnt++;
		if(ampTeamId!=null) dbReturnSet = DbUtil.getAmpDonors(ampTeamId); else dbReturnSet=new ArrayList();
		// logger.debug("Donor Size: " + dbReturnSet.size());
		iter = dbReturnSet.iterator();
		if(ampTeam!=null) formBean.setDonorColl(getFilterDonors(ampTeam));

		while (iter.hasNext()) {
			AmpOrganisation ampOrganisation = (AmpOrganisation) iter.next();
			if (ampOrganisation.getAcronym().length() > 20) {
				String temp = ampOrganisation.getAcronym().substring(0, 20)
						+ "...";
				ampOrganisation.setAcronym(temp);
			}
			formBean.getDonorColl().add(ampOrganisation);
		}

		setFilters = setFilters + " MODALITY -";
		filterCnt++;
		dbReturnSet = DbUtil.getAmpModality();
		formBean.setModalityColl(dbReturnSet);

		setFilters = setFilters + " CURRENCY -";
		filterCnt++;
		dbReturnSet = CurrencyUtil.getAmpCurrency();
		formBean.setCurrencyColl(dbReturnSet);

		setFilters = setFilters + " CALENDAR -";
		filterCnt += 10;
		formBean.setFiscalYears(DbUtil.getAllFisCalenders());

		for (int i = (year - Constants.FROM_YEAR_RANGE); i <= (year + Constants.TO_YEAR_RANGE); i++) {
			formBean.getAmpFromYears().add(new Long(i));
			formBean.getAmpToYears().add(new Long(i));
		}

		for (int i = (year - Constants.FROM_YEAR_RANGE); i <= (year + Constants.TO_YEAR_RANGE); i++) {
			formBean.getAmpStartYears().add(new Long(i));
			formBean.getAmpCloseYears().add(new Long(i));
		}
		for (int i = 1; i <= 31; i++) {
			formBean.getAmpStartDays().add(new Long(i));
			formBean.getAmpCloseDays().add(new Long(i));
		}

		if (formBean.getAmpStatusId() == null
				|| formBean.getAmpStatusId().intValue() == 0)
			ampStatusId = All;
		else
			ampStatusId = formBean.getAmpStatusId();

		if (formBean.getAmpSectorId() == null
				|| formBean.getAmpSectorId().intValue() == 0)
			ampSectorId = All;
		else
			ampSectorId = formBean.getAmpSectorId();

		if (formBean.getAmpOrgId() == null
				|| formBean.getAmpOrgId().intValue() == 0)
			ampOrgId = All;
		else
			ampOrgId = formBean.getAmpOrgId();

		if (formBean.getAmpModalityId() == null
				|| formBean.getAmpModalityId().intValue() == 0)
			ampModalityId = All;
		else
			ampModalityId = formBean.getAmpModalityId();

		if(ampTeam!=null) {
		if (formBean.getAmpCurrencyCode() == null
				|| formBean.getAmpCurrencyCode().equals("0")) {
			ampCurrency = CurrencyUtil.getAmpcurrency(teamMember
					.getAppSettings().getCurrencyId());
			ampCurrencyCode = ampCurrency.getCurrencyCode();
			formBean.setAmpCurrencyCode(ampCurrencyCode);
		} else
			ampCurrencyCode = formBean.getAmpCurrencyCode();
		} else formBean.setAmpCurrencyCode(Constants.DEFAULT_CURRENCY);
		 
		// for storing the value of year filter
		if (formBean.getAmpToYear() == null) {
			toYr = 0;
			formBean.setAmpToYear(new Long(toYr));
		} else
			toYr = formBean.getAmpToYear().intValue();

		if (formBean.getAmpFromYear() == null) {
			fromYr = 0;
			formBean.setAmpFromYear(new Long(fromYr));
		} else
			fromYr = formBean.getAmpFromYear().intValue();

		// for storing the values of start date and close date selected from
		// filter
		if (formBean.getStartYear() == null
				|| formBean.getStartYear().intValue() == 0)
			startYear = year - Constants.FROM_YEAR_RANGE;
		else
			startYear = formBean.getStartYear().intValue();

		if (formBean.getStartMonth() == null
				|| formBean.getStartMonth().intValue() == 0)
			startMonth = 1;
		else
			startMonth = formBean.getStartMonth().intValue();

		if (formBean.getStartDay() == null
				|| formBean.getStartDay().intValue() == 0)
			startDay = 1;
		else
			startDay = formBean.getStartDay().intValue();

		if (formBean.getCloseYear() == null
				|| formBean.getCloseYear().intValue() == 0)
			closeYear = year + Constants.TO_YEAR_RANGE;
		else
			closeYear = formBean.getCloseYear().intValue();

		if (formBean.getCloseMonth() == null
				|| formBean.getCloseMonth().intValue() == 0)
			closeMonth = 12;
		else
			closeMonth = formBean.getCloseMonth().intValue();

		if (formBean.getCloseDay() == null
				|| formBean.getCloseDay().intValue() == 0)
			closeDay = 31;
		else
			closeDay = formBean.getCloseDay().intValue();

		if(teamMember!=null) {
		if (formBean.getFiscalCalId() == 0) {
			fiscalCalId = teamMember.getAppSettings().getFisCalId().intValue();
			formBean.setFiscalCalId(fiscalCalId);
		} else
			fiscalCalId = formBean.getFiscalCalId();
		}
		
		if (request.getParameter("view") != null) {
			if (request.getParameter("view").equals("reset")) {
				if(teamMember!=null) {perspective = teamMember.getAppSettings().getPerspective();
				if (perspective.equals("Donor"))
					perspective = "DN";
				if (perspective.equals("MOFED"))
					perspective = "MA";}
				else perspective="MA";
				formBean.setPerspectiveFilter(perspective);
				formBean.setAmpStatusId(All);
				ampStatusId = All;
				formBean.setAmpOrgId(All);
				ampOrgId = All;
				formBean.setAmpSectorId(All);
				ampSectorId = All;
				formBean.setAmpModalityId(All);
				ampModalityId = All;
				toYr = 0;
				fromYr = 0;

				if(teamMember!=null) {
				ampCurrency = CurrencyUtil.getAmpcurrency(teamMember
						.getAppSettings().getCurrencyId());
				ampCurrencyCode = ampCurrency.getCurrencyCode();
				} else ampCurrencyCode=Constants.DEFAULT_CURRENCY;
	
				
				formBean.setAmpFromYear(new Long(fromYr));
				formBean.setAmpToYear(new Long(toYr));
				formBean.setStartYear(All);
				formBean.setStartMonth(All);
				formBean.setStartDay(All);
				formBean.setCloseYear(All);
				formBean.setCloseMonth(All);
				formBean.setCloseDay(All);
				formBean.setPdfPageSize("default");
				
				formBean.setStatuses(null);
				formBean.setRegions(null);
				formBean.setSectors(null);
				formBean.setDonors(null);
				
				startYear = 0;
				startMonth = 0;
				startDay = 0;
				closeYear = 0;
				closeMonth = 0;
				closeDay = 0;
				if(teamMember!=null) {
				fiscalCalId = teamMember.getAppSettings().getFisCalId()
						.intValue();
				formBean.setFiscalCalId(fiscalCalId);
				}
				formBean.setAmpCurrencyCode(ampCurrencyCode);
				formBean.setAmpLocationId("All");
			}
		}

		String startDate = null;
		String closeDate = null;
		if (formBean.getStartYear() != null
				&& formBean.getStartYear().intValue() > 0)
			startDate = startYear + "-" + startMonth + "-" + startDay;
		if (formBean.getCloseYear() != null
				&& formBean.getCloseYear().intValue() > 0)
			closeDate = closeYear + "-" + closeMonth + "-" + closeDay;


		// if the report metadata has a defaultFilter attached, use that as the
		// first filter
	
		if ("reset".equals(request.getParameter("view"))
				&& ampReports.getDefaultFilter() != null) {
			AmpARFilter defFilt = ampReports.getDefaultFilter();
			if (defFilt.getAmpCurrencyCode() != null)
				formBean.setCurrency(defFilt.getAmpCurrencyCode());
			if (defFilt.getFromYear() != null)
				formBean.setAmpFromYear(new Long(defFilt.getFromYear()
						.longValue()));
			if (defFilt.getToYear() != null)
				formBean
						.setAmpToYear(new Long(defFilt.getToYear().longValue()));
			return defFilt;
		}

		// create the ampFilter bean
		AmpARFilter anf = new AmpARFilter();

		// get the sector list
		if (ampSectorId.longValue() != 0) {
			Set sectors = new TreeSet(SectorUtil
					.getAllChildSectors(ampSectorId));
			sectors.add(SectorUtil.getAmpSector(ampSectorId));
			anf.setSectors(sectors);
		}

		
		if (formBean.getSectors() != null) {
			Set sectors = new TreeSet();
			
			for (int x = 0; x < formBean.getSectors().length; x++) {
				AmpSector sector=SectorUtil.getAmpParentSector(new Long(formBean.getSectors()[x]));
				Set childSectors = new TreeSet(SectorUtil
						.getAllChildSectors(sector.getAmpSectorId()));
				sectors.add(sector);
				sectors.addAll(childSectors);
			}

			anf.setSectors(sectors);
		}

		
		
		if (ampTeam!=null && formBean.getDonors() != null) {
			Collection alldonors = getFilterDonors(ampTeam);
			Set donors = new TreeSet();
			
			for (int x = 0; x < formBean.getDonors().length; x++) {
				Iterator i = alldonors.iterator();
				while (i.hasNext()) {
					AmpOrganisation element = (AmpOrganisation) i.next();
					if (element.getAmpOrgId().longValue() == formBean
							.getDonors()[x])
						donors.add(element);
				}
			}
			anf.setDonors(donors);
		}

		
		if (formBean.getStatuses() != null) {
			Set statuses = new TreeSet();
			
			for (int x = 0; x < formBean.getStatuses().length; x++) {
				AmpStatus status=(AmpStatus) session.get(AmpStatus.class, new Long(formBean.getStatuses()[x]));
				statuses.add(status);
			}

			anf.setStatuses(statuses);
		}

		if (formBean.getRegions() != null) {
			Collection allRegions = LocationUtil.getAmpLocations();
			Set regions = new TreeSet();
			
			for (int x = 0; x < formBean.getRegions().length; x++) {
				Iterator i = allRegions.iterator();
				while (i.hasNext()) {
					AmpRegion element = (AmpRegion) i.next();
					if (element.getName().equals(formBean.getRegions()[x]))
						regions.add(element.getName());
				}
			}
			anf.setRegions(regions);
		}
		
		// get the team list
		if(teamMember!=null) {
			Set teams = TeamUtil.getAmpLevel0Teams(ampTeamId);
			teams.add(ampTeam);
			anf.setAmpTeams(teams);
		}
		
		anf.setPerspectiveCode(formBean.getPerspectiveFilter());

		if (!"reset".equals(request.getParameter("view"))) {
			anf.setAmpCurrencyCode(ampCurrencyCode);
			anf.setAmpModalityId(ampModalityId);
			// anf.setAmpOrgId(ampOrgId);

			if(formBean.getFiscalCalId()==0) anf.setCalendarType(new Integer(formBean.getFiscalCalId())); 
			else anf.setCalendarType(new Integer(Constants.GREGORIAN.intValue()));

			anf.setFromYear(fromYr == 0 ? null : new Integer(fromYr));
			anf.setToYear(toYr == 0 ? null : new Integer(toYr));
			

			// perform ethiopian to gregorian year conversion

		
		}

		String widget=(String) request.getAttribute("widget");
		if("true".equals(widget)) anf.setWidget(true);
		
		if(teamMember==null) {
			String onOff=FeaturesUtil.getGlobalSettingValue(Constants.GLOBAL_BUDGET_FILTER);
			if("On Budget".equals(onOff)) anf.setBudget(new Boolean(true));else
			if("Off Budget".equals(onOff)) anf.setBudget(new Boolean(false));
			else anf.setBudget(null);
		}
		
		anf.generateFilterQuery();

		return anf;
	}

	public static double getExchange(String currency, java.sql.Date currencyDate) {
		Connection conn = null;
		double ret = 1;

		// we try the digi cache:
		ObjectCache ratesCache = DigiCacheManager.getInstance().getCache(
				"EXCHANGE_RATES_CACHE");

		Double cacheRet = (Double) ratesCache.get(new String(currency
				+ currencyDate));
		if (cacheRet != null)
			return cacheRet.doubleValue();

		try {
			conn = PersistenceManager.getSession().connection();
		} catch (HibernateException e) {
			logger.error(e);
			e.printStackTrace();
		} catch (SQLException e) {
			logger.error(e);
			e.printStackTrace();
		}

		String query = "SELECT getExchange(?,?)";
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, currency);
			ps.setDate(2, currencyDate);

			ResultSet rs = ps.executeQuery();

			if (rs.next())
				ret = rs.getDouble(1);

			rs.close();

		} catch (SQLException e) {
			logger.error("Unable to get exchange rate for currencty "
					+ currency + " for the date " + currencyDate);
			logger.error(e);
			e.printStackTrace();
		}
		logger.info("rate for " + currency + " to USD on " + currencyDate
				+ " is " + ret);
		if (ret != 1)
			ratesCache
					.put(new String(currency + currencyDate), new Double(ret));

		return ret;

	}

	public static boolean containsMeasure(String measureName, Set measures) {
		Iterator i = measures.iterator();
		while (i.hasNext()) {
			AmpMeasures element = (AmpMeasures) i.next();
			if (element.getMeasureName().indexOf(measureName) != -1)
				return true;
		}
		return false;
	}

	public static List createOrderedHierarchies(Collection columns) {
		List orderedColumns = new ArrayList(columns.size());
		for (int x = 0; x < columns.size(); x++) {
			Iterator i = columns.iterator();
			while (i.hasNext()) {
				AmpReportHierarchy element = (AmpReportHierarchy) i.next();
				int order = Integer.parseInt(element.getLevelId());
				if (order - 1 == x)
					orderedColumns.add(element);
			}
		}
		return orderedColumns;
	}

	public static List createOrderedColumns(Collection columns) {
		List orderedColumns = new ArrayList(columns.size());
		for (int x = 0; x < columns.size(); x++) {
			Iterator i = columns.iterator();
			while (i.hasNext()) {
				AmpReportColumn element = (AmpReportColumn) i.next();
				int order = Integer.parseInt(element.getOrderId());
				if (order - 1 == x)
					orderedColumns.add(element);
			}
		}
		return orderedColumns;
	}

}

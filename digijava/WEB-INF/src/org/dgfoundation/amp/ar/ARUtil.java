/**
 * ARUtil.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.hibernate.Session;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.form.AdvancedReportForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
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

	public static GroupReportData generateReport(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws java.lang.Exception {

		String ampReportId = request.getParameter("ampReportId");

		Session session = PersistenceManager.getSession();

		AmpReports r = (AmpReports) session.get(AmpReports.class, new Long(
				ampReportId));

		AmpNewFilter af = ARUtil.createFilter(r, mapping, form, request,
				response);
		if (af == null)
			return null;

		AmpReportGenerator arg = new AmpReportGenerator(r, af);

		arg.generate();
		
		session.close();

		return arg.getReport();

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
	public static AmpNewFilter createFilter(AmpReports ampReports,
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws java.lang.Exception {
		HttpSession httpSession = request.getSession();
		TeamMember teamMember = (TeamMember) httpSession
				.getAttribute("currentMember");
		ArrayList filters = null;
		Iterator iter = null;
		String setFilters = "";
		int year = 0;
		Iterator iterSub = null;
		int filterCnt = 0;

		Long ampStatusId = null;
		Long ampOrgId = null;
		Long ampSectorId = null;
		String region = null;
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

		// do not allow access for guests:
		if (teamMember == null)
			return null;
		Long ampTeamId = teamMember.getTeamId();
		ArrayList dbReturnSet = null;

		AdvancedReportForm formBean = (AdvancedReportForm) form;
		String perspective = "DN";

		AmpTeam ampTeam = TeamUtil.getAmpTeam(ampTeamId);

		if (request.getParameter("ampReportId") != null) {
			formBean.setCreatedReportId(request.getParameter("ampReportId"));
		}

		formBean.setReportName(ampReports.getName());
		if (ampReports.getReportDescription() != null)
			formBean.setReportDescription(ampReports.getReportDescription());
		formBean.setWorkspaceType(ampTeam.getType());
		formBean.setWorkspaceName(ampTeam.getName());
		if (perspective.equals("DN"))
			formBean.setPerspective("Donor");
		if (perspective.equals("MA"))
			formBean.setPerspective("MOFED");
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
		formBean.setColumnHierarchie(new ArrayList());

		filters = DbUtil.getTeamPageFilters(ampTeamId, ampPageId);
		// logger.debug("Filter Size: " + filters.size());
		if (filters.size() == 0)
			formBean.setGoFlag("false");

		if (filters.size() > 0) {
			formBean.setGoFlag("true");
			if (filters.indexOf(Constants.PERSPECTIVE) != -1) {
				formBean.setFilterFlag("true");
				setFilters = setFilters + " PERSPECTIVE -";
				filterCnt++;
			}

			if (filters.indexOf(Constants.STATUS) != -1) {
				dbReturnSet = DbUtil.getAmpStatus();
				formBean.setStatusColl(dbReturnSet);
				setFilters = setFilters + " STATUS -";
				filterCnt++;
			}

			if (filters.indexOf(Constants.SECTOR) != -1) {
				setFilters = setFilters + " SECTOR -";
				filterCnt++;
				formBean.setSectorColl(new ArrayList());
				dbReturnSet = SectorUtil.getAmpSectors();
				iter = dbReturnSet.iterator();
				while (iter.hasNext()) {
					AmpSector ampSector = (AmpSector) iter.next();
					if (ampSector.getName().length() > 20) {
						String temp = ampSector.getName().substring(0, 20)
								+ "...";
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
			}

			if (filters.indexOf(Constants.REGION) != -1) {
				setFilters = setFilters + " REGION -";
				filterCnt++;
				dbReturnSet = LocationUtil.getAmpLocations();
				formBean.setRegionColl(dbReturnSet);
			}

			if (filters.indexOf(Constants.DONORS) != -1) {
				setFilters = setFilters + " DONORS -";
				filterCnt++;
				dbReturnSet = DbUtil.getAmpDonors(ampTeamId);
				// logger.debug("Donor Size: " + dbReturnSet.size());
				iter = dbReturnSet.iterator();
				formBean.setDonorColl(new ArrayList());
				while (iter.hasNext()) {
					AmpOrganisation ampOrganisation = (AmpOrganisation) iter
							.next();
					if (ampOrganisation.getAcronym().length() > 20) {
						String temp = ampOrganisation.getAcronym().substring(0,
								20)
								+ "...";
						ampOrganisation.setAcronym(temp);
					}
					formBean.getDonorColl().add(ampOrganisation);
				}
			}

			if (filters.indexOf(Constants.FINANCING_INSTRUMENT) != -1) {
				setFilters = setFilters + " MODALITY -";
				filterCnt++;
				dbReturnSet = DbUtil.getAmpModality();
				formBean.setModalityColl(dbReturnSet);
			}

			if (filters.indexOf(Constants.CURRENCY) != -1) {
				setFilters = setFilters + " CURRENCY -";
				filterCnt++;
				dbReturnSet = CurrencyUtil.getAmpCurrency();
				formBean.setCurrencyColl(dbReturnSet);
			}

			if (filters.indexOf(Constants.CALENDAR) != -1) {
				setFilters = setFilters + " CALENDAR -";
				filterCnt += 10;
				formBean.setFiscalYears(DbUtil.getAllFisCalenders());
			}

			if (filters.indexOf(Constants.YEAR_RANGE) != -1) {
				for (int i = (year - Constants.FROM_YEAR_RANGE); i <= (year + Constants.TO_YEAR_RANGE); i++) {
					formBean.getAmpFromYears().add(new Long(i));
					formBean.getAmpToYears().add(new Long(i));
				}
			}
			if (filters.indexOf(Constants.STARTDATE_CLOSEDATE) != -1) {
				for (int i = (year - Constants.FROM_YEAR_RANGE); i <= (year + Constants.TO_YEAR_RANGE); i++) {
					formBean.getAmpStartYears().add(new Long(i));
					formBean.getAmpCloseYears().add(new Long(i));
				}
				for (int i = 1; i <= 31; i++) {
					formBean.getAmpStartDays().add(new Long(i));
					formBean.getAmpCloseDays().add(new Long(i));
				}
			}
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

		if (formBean.getAmpLocationId() == null
				|| formBean.getAmpLocationId().equals("All"))
			region = "All";
		else
			region = formBean.getAmpLocationId();

		if (formBean.getAmpComponentId() == null
				|| formBean.getAmpComponentId().equals("All"))
			region = "All";
		else
			region = formBean.getAmpComponentId();

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

		if (formBean.getAmpCurrencyCode() == null
				|| formBean.getAmpCurrencyCode().equals("0")) {
			ampCurrency = CurrencyUtil.getAmpcurrency(teamMember
					.getAppSettings().getCurrencyId());
			ampCurrencyCode = ampCurrency.getCurrencyCode();
			formBean.setAmpCurrencyCode(ampCurrencyCode);
		} else
			ampCurrencyCode = formBean.getAmpCurrencyCode();

		// for storing the value of year filter
		if (formBean.getAmpToYear() == null) {
			toYr = year;
			formBean.setAmpToYear(new Long(toYr));
		} else
			toYr = formBean.getAmpToYear().intValue();

		if (formBean.getAmpFromYear() == null) {
			fromYr = toYr - 2;
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

		if (formBean.getFiscalCalId() == 0) {
			fiscalCalId = teamMember.getAppSettings().getFisCalId().intValue();
			formBean.setFiscalCalId(fiscalCalId);
		} else
			fiscalCalId = formBean.getFiscalCalId();

		if (request.getParameter("view") != null) {
			if (request.getParameter("view").equals("reset")) {
				perspective = teamMember.getAppSettings().getPerspective();
				if (perspective.equals("Donor"))
					perspective = "DN";
				if (perspective.equals("MOFED"))
					perspective = "MA";
				formBean.setPerspectiveFilter(perspective);
				formBean.setAmpStatusId(All);
				ampStatusId = All;
				formBean.setAmpOrgId(All);
				ampOrgId = All;
				formBean.setAmpSectorId(All);
				ampSectorId = All;
				formBean.setAmpModalityId(All);
				ampModalityId = All;
				toYr = year;
				fromYr = toYr - 2;
				formBean.setAmpFromYear(new Long(fromYr));
				formBean.setAmpToYear(new Long(toYr));
				formBean.setStartYear(All);
				formBean.setStartMonth(All);
				formBean.setStartDay(All);
				formBean.setCloseYear(All);
				formBean.setCloseMonth(All);
				formBean.setCloseDay(All);
				startYear = year - 15;
				startMonth = 1;
				startDay = 1;
				closeYear = year;
				closeMonth = 12;
				closeDay = 31;
				fiscalCalId = teamMember.getAppSettings().getFisCalId()
						.intValue();
				formBean.setFiscalCalId(fiscalCalId);
				formBean.setAmpCurrencyCode(ampCurrencyCode);
				formBean.setAmpLocationId("All");
				region = "All";
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

		// create the ampFilter bean
		AmpNewFilter anf = new AmpNewFilter();
		anf.setAmpCurrencyCode(ampCurrencyCode);
		anf.setAmpModalityId(ampModalityId);
		anf.setAmpOrgId(ampOrgId);
		anf.setAmpSectorId(ampSectorId);
		anf.setAmpStatusId(ampStatusId);
		anf.setCloseDay(closeDay);
		anf.setCloseMonth(closeMonth);
		anf.setCloseYear(closeYear);
		anf.setRegion(region);
		anf.setStartDay(startDay);
		anf.setStartMonth(startMonth);
		anf.setStartYear(startYear);

		anf.generateFilterQuery();

		return anf;
	}
}

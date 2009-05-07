/**
 * 
 */
package org.digijava.module.aim.action;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.ARUtil;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.utils.MultiAction;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.action.reportwizard.ReportWizardAction;
import org.digijava.module.aim.ar.util.FilterUtil;
import org.digijava.module.aim.ar.util.ReportsUtil;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpIndicatorRiskRatings;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.form.ReportsFilterPickerForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.helper.fiscalcalendar.ICalendarWorker;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.MEIndicatorsUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.hibernate.Session;
import org.springframework.beans.BeanWrapperImpl;

/**
 * @author mihai
 * 
 */
public class ReportsFilterPicker extends MultiAction {
	private static Logger logger = Logger.getLogger(ReportsFilterPicker.class);
	
	final String KEY_RISK_PREFIX = "aim:risk:";

	public ActionForward modePrepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		ReportsFilterPickerForm filterForm = (ReportsFilterPickerForm) form;
		String sourceIsReportWizard			= request.getParameter("sourceIsReportWizard");
		if ("true".equals(sourceIsReportWizard) ) {
			filterForm.setSourceIsReportWizard(true);
			if ( request.getParameter("doreset") != null ) {
				filterForm.setIsnewreport(true);
				reset(form, request, mapping);
				filterForm.setIsnewreport(false);
			}
		}
		else
			filterForm.setSourceIsReportWizard(false);
		
		String ampReportId 	= request.getParameter("ampReportId");
		if ( "".equals(ampReportId) )
			ampReportId		= null;

		if (ampReportId != null && filterForm.getAmpReportId() != null) {
			if (!filterForm.getAmpReportId().toString().equalsIgnoreCase(ampReportId)) {
				filterForm.setIsnewreport(true);
				reset(form, request, mapping);
				filterForm.setAmpReportId(new Long(ampReportId));
				filterForm.setIsnewreport(false);
			}
		} 
		else if( ampReportId != null ){
			filterForm.setIsnewreport(true);
			filterForm.setAmpReportId(new Long(ampReportId));
			filterForm.setIsnewreport(false);
		}

		if (("true").equalsIgnoreCase(filterForm.getResetFormat())) {
			resetFormat(form, request, mapping);
		}

		HttpSession httpSession = request.getSession();
		TeamMember teamMember = (TeamMember) httpSession.getAttribute(Constants.CURRENT_MEMBER);
		
		AmpARFilter existingFilter		= (AmpARFilter)request.getSession().getAttribute(ReportWizardAction.EXISTING_SESSION_FILTER);
		if ( existingFilter != null ) { 
			FilterUtil.populateForm(filterForm, existingFilter);
			request.getSession().setAttribute(ReportWizardAction.EXISTING_SESSION_FILTER, null);
		}

		Long ampTeamId = null;
		if (teamMember != null)
			ampTeamId = teamMember.getTeamId();
		if (teamMember != null)
			filterForm.setTeamAccessType(teamMember.getTeamAccessType());

		// create filter dropdowns
		Collection currency = CurrencyUtil.getAmpCurrency();
	     //Only currencies havening exchanges rates AMP-2620
	      Collection<AmpCurrency> validcurrencies = new ArrayList<AmpCurrency>();
	      filterForm.setCurrencies(validcurrencies);
	      for (Iterator iter = currency.iterator(); iter.hasNext();) {
			AmpCurrency element = (AmpCurrency) iter.next();
			 if( CurrencyUtil.isRate(element.getCurrencyCode())== true)
					{
				 filterForm.getCurrencies().add((CurrencyUtil.getCurrencyByCode(element.getCurrencyCode())));
					}
			}
	      
		Collection allFisCalenders = DbUtil.getAllFisCalenders();

		List<AmpSector> ampSectors = SectorUtil.getAmpSectorsAndSubSectors(AmpClassificationConfiguration.PRIMARY_CLASSIFICATION_CONFIGURATION_NAME);

		List<AmpSector> secondaryAmpSectors = SectorUtil.getAmpSectorsAndSubSectors(AmpClassificationConfiguration.SECONDARY_CLASSIFICATION_CONFIGURATION_NAME);

		List<AmpTheme> nationalPlanningObjectives;
		AmpActivityProgramSettings natPlanSetting = ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.NATIONAL_PLAN_OBJECTIVE);
		if (natPlanSetting!=null && natPlanSetting.getDefaultHierarchy() != null) {
			nationalPlanningObjectives = ProgramUtil.getAmpThemesAndSubThemes(natPlanSetting.getDefaultHierarchy());
		} else {
			nationalPlanningObjectives = ProgramUtil.getAllSubThemesFor(ProgramUtil.getAllThemes(false));
		}

		List<AmpTheme> primaryPrograms;
		AmpActivityProgramSettings primaryPrgSetting = ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.PRIMARY_PROGRAM);
		if (primaryPrgSetting!=null && primaryPrgSetting.getDefaultHierarchy() != null) {
			primaryPrograms = ProgramUtil.getAmpThemesAndSubThemes(primaryPrgSetting.getDefaultHierarchy());
		} else {
			primaryPrograms = ProgramUtil.getAllSubThemesFor(ProgramUtil.getAllThemes(false));

		}

		List<AmpTheme> secondaryPrograms;
		AmpActivityProgramSettings secondaryPrg = ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.SECONDARY_PROGRAM);
		if (secondaryPrg!=null && secondaryPrg.getDefaultHierarchy() != null) {
			secondaryPrograms = ProgramUtil.getAmpThemesAndSubThemes(secondaryPrg.getDefaultHierarchy());
		} else {
			secondaryPrograms = ProgramUtil.getAllSubThemesFor(ProgramUtil.getAllThemes(false));
		}

		/**
		 * This has been moved in SectorUtil.getAmpSectorsAndSubSectors();
		 * 
		 * Long
		 * primaryConfigClassId=SectorUtil.getPrimaryConfigClassificationId();
		 * ampSectors =
		 * (List)SectorUtil.getAllParentSectors(primaryConfigClassId);
		 */

		// ampSectors =
		// SectorUtil.getAllSectorsFromScheme(FeaturesUtil.getGlobalSettingValueLong(GlobalSettingsConstants.DEFAULT_SECTOR_SCHEME));
		/*
		 * TreeSet<AmpSector> alphaOrderedSectors = new TreeSet<AmpSector>(
		 * new Comparator<AmpSector>() {
		 * 
		 * public int compare(AmpSector as1, AmpSector as2) { if ( as1.getName() !=
		 * null && as2.getName() != null ) return
		 * as1.getName().compareToIgnoreCase(as2.getName() );
		 * 
		 * return -1; } } ); Iterator<AmpSector> sectIter = (Iterator<AmpSector>)ampSectors.iterator();
		 * while ( sectIter.hasNext() ) { alphaOrderedSectors.add(
		 * sectIter.next() ); }
		 */

		AmpApplicationSettings tempSettings = getAppSetting(request);
		if (tempSettings != null) {
			filterForm.setDefaultCurrency(tempSettings.getCurrency().getAmpCurrencyId());

			if (filterForm.getCurrency() == null) {
				filterForm.setCurrency(tempSettings.getCurrency().getAmpCurrencyId());
			}
			if (tempSettings.getFiscalCalendar()!=null && filterForm.getCalendar() == null) {
				filterForm.setCalendar(tempSettings.getFiscalCalendar().getAmpFiscalCalId());
			}
		} else {
			filterForm.setDefaultCurrency(CurrencyUtil.getCurrencyByCode(Constants.DEFAULT_CURRENCY).getAmpCurrencyId());
			if (filterForm.getCalendar() == null) {
				String value = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_CALENDAR);
				if (value != null) {
					filterForm.setCalendar(Long.parseLong(value));
				}
			}
		}

		// create the pageSizes Collection for the dropdown
		Collection pageSizes = new ArrayList();

		Collection donors;
		// if(ampTeamId!=null) donors=DbUtil.getAmpDonorsByFunding(ampTeamId);
		// else donors=new ArrayList();
		// donors = DbUtil.getAllOrgGroups();
		donors = DbUtil.getAllOrgGrpBeeingUsed();
		
		Collection meRisks = MEIndicatorsUtil.getAllIndicatorRisks();
		for (Iterator iter = meRisks.iterator(); iter.hasNext();) {
			AmpIndicatorRiskRatings element = (AmpIndicatorRiskRatings) iter.next();
			String value = element.getRatingName();
			String key = KEY_RISK_PREFIX + value.toLowerCase();
			key = key.replaceAll(" ", "");
			String msg = CategoryManagerUtil.translate(key, request, value);
			element.setRatingName(msg);
		}
		Collection donorTypes = DbUtil.getAllOrgTypesOfPortfolio();
		Collection donorGroups = ARUtil.filterDonorGroups(DbUtil.getAllOrgGroupsOfPortfolio());

		Collection allIndicatorRisks = meRisks;
		Collection regions = DynLocationManagerUtil.getLocationsOfTypeRegionOfDefCountry();
		// Collection regions=LocationUtil.getAllVRegions();
		//filterForm.setCurrencies(currency);
		filterForm.setCalendars(allFisCalenders);
		// filterForm.setDonors(donors);
		filterForm.setRisks(allIndicatorRisks);
		filterForm.setSectors(ampSectors);
		filterForm.setNationalPlanningObjectives(nationalPlanningObjectives);
		filterForm.setPrimaryPrograms(primaryPrograms);
		filterForm.setSecondaryPrograms(secondaryPrograms);
		filterForm.setSecondarySectors(secondaryAmpSectors);

		filterForm.setFromYears(new ArrayList<BeanWrapperImpl>());
		filterForm.setToYears(new ArrayList<BeanWrapperImpl>());

		filterForm.setFromMonths(new ArrayList<BeanWrapperImpl>());
		filterForm.setToMonths(new ArrayList<BeanWrapperImpl>());

		filterForm.setCountYears(new ArrayList<BeanWrapperImpl>());
		filterForm.setPageSizes(pageSizes);
		filterForm.setRegionSelectedCollection(regions);
		filterForm.setApprovalStatusSelectedCollection(new ArrayList());
		filterForm.setDonorTypes(donorTypes);
		filterForm.setDonorGroups(donorGroups);

		filterForm.setExecutingAgency(ReportsUtil.getAllOrgByRoleOfPortfolio(Constants.ROLE_CODE_EXECUTING_AGENCY));
		filterForm.setDonnorAgency((ReportsUtil.getAllOrgByRoleOfPortfolio(Constants.ROLE_CODE_DONOR)));
		filterForm.setBeneficiaryAgency(ReportsUtil.getAllOrgByRoleOfPortfolio(Constants.ROLE_CODE_BENEFICIARY_AGENCY));
		filterForm.setImplementingAgency(ReportsUtil.getAllOrgByRoleOfPortfolio(Constants.ROLE_CODE_IMPLEMENTING_AGENCY));
		filterForm.setResponsibleorg(ReportsUtil.getAllOrgByRoleOfPortfolio(Constants.ROLE_CODE_RESPONSIBLE_ORG));
		
		String calValue = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_CALENDAR);
		if (filterForm.getCalendar() == null && calValue != null) {
			filterForm.setCalendar(Long.parseLong(calValue));
		}
		// loading Activity Rank collection
		if (null == filterForm.getActRankCollection()) {
			filterForm.setActRankCollection(new ArrayList());
			for (int i = 1; i < 6; i++)
				filterForm.getActRankCollection().add(new BeanWrapperImpl(new Integer(i)));
		}

		Long yearFrom = Long.parseLong(FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.YEAR_RANGE_START));
		Long countYear = Long.parseLong(FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.NUMBER_OF_YEARS_IN_RANGE));

		if (filterForm.getCountYear() == null) {
			filterForm.setCountYear(countYear);
		}

		if (filterForm.getCountYearFrom() == null) {
			filterForm.setCountYearFrom(yearFrom);
		}

		for (long i = 10; i <= 100; i += 10) {
			filterForm.getCountYears().add(new BeanWrapperImpl(new Long(i)));
		}

		for (long i = yearFrom; i <= (yearFrom + countYear); i++) {
			filterForm.getFromYears().add(new BeanWrapperImpl(new Long(i)));
			filterForm.getToYears().add(new BeanWrapperImpl(new Long(i)));
		}

		if (filterForm.getFromYear() == null) {
			// Long
			// fromYear=Long.parseLong(FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.START_YEAR_DEFAULT_VALUE));
			filterForm.setFromYear(-1l);
		}

		if (filterForm.getToYear() == null) {
			// Long
			// toYear=Long.parseLong(FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.END_YEAR_DEFAULT_VALUE));
			filterForm.setToYear(-1l);
		}

		for (int i = 1; i <= 12; i++) {
			filterForm.getFromMonths().add(new BeanWrapperImpl(new Integer(i)));
			filterForm.getToMonths().add(new BeanWrapperImpl(new Integer(i)));
		}

		if (filterForm.getFromMonth() == null)
			filterForm.setFromMonth(-1);

		if (filterForm.getToMonth() == null)
			filterForm.setToMonth(-1);

		/*--------------------------*/
		Integer rStart = getDefaultStartYear(request);
		
		Integer rEnd = getDefaultEndYear(request);
		
		
		 if (filterForm.getCalendar() != null) {
			rStart = getYearOnCalendar(filterForm.getCalendar(), rStart,tempSettings);
			rEnd = getYearOnCalendar(filterForm.getCalendar(), rEnd,tempSettings);
		}

		if (filterForm.getRenderStartYear() == null) {
			filterForm.setRenderStartYear(rStart);
		}

		if (filterForm.getRenderEndYear() == null) {
			filterForm.setRenderEndYear(rEnd);
		}
		
		filterForm.getPageSizes().add(new BeanWrapperImpl(new String("A0")));
		filterForm.getPageSizes().add(new BeanWrapperImpl(new String("A1")));
		filterForm.getPageSizes().add(new BeanWrapperImpl(new String("A2")));
		filterForm.getPageSizes().add(new BeanWrapperImpl(new String("A3")));
		filterForm.getPageSizes().add(new BeanWrapperImpl(new String("A4")));

		if (ampReportId != null) {

			AmpReports rep = (AmpReports) DbUtil.getAmpReports(new Long(ampReportId));

			httpSession.setAttribute("filterCurrentReport", rep);
		}

		if (filterForm.getCustomDecimalSymbol() == null) {
			filterForm.setCustomDecimalSymbol(String.valueOf((FormatHelper.getDecimalFormat().getDecimalFormatSymbols().getDecimalSeparator())));
			filterForm.setCustomDecimalPlaces(FormatHelper.getDecimalFormat().getMaximumFractionDigits());
			filterForm.setCustomGroupCharacter(String.valueOf(FormatHelper.getDecimalFormat().getDecimalFormatSymbols().getGroupingSeparator()));
			filterForm.setCustomUseGrouping(FormatHelper.getDecimalFormat().isGroupingUsed());
			filterForm.setCustomGroupSize(FormatHelper.getDecimalFormat().getGroupingSize());
		}

		return modeSelect(mapping, form, request, response);

	}

	public ActionForward modeReset(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ReportsFilterPickerForm filterForm = (ReportsFilterPickerForm) form;
		request.setAttribute("reset", "reset");
		// filterForm.setSelectedDonors(null);
		filterForm.setSelectedRisks(null);
		filterForm.setSelectedSectors(null);
		filterForm.setSelectedStatuses(null);
		filterForm.setSelectedSecondaryPrograms(null);
		filterForm.setSelectedPrimaryPrograms(null);
		filterForm.setSelectedNatPlanObj(null);
		HttpSession httpSession = request.getSession();
		
		AmpApplicationSettings tempSettings = getAppSetting(request);
		if (tempSettings != null) {
			filterForm.setCurrency(tempSettings.getCurrency().getAmpCurrencyId());
			String name = "- " + tempSettings.getCurrency().getCurrencyName();
			httpSession.setAttribute(ArConstants.SELECTED_CURRENCY, name);
			filterForm.setCalendar(tempSettings.getFiscalCalendar().getAmpFiscalCalId());
		}

		// Long
		// fromYear=Long.parseLong(FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.START_YEAR_DEFAULT_VALUE));
		filterForm.setFromYear(-1l);

		// Long
		// toYear=Long.parseLong(FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.END_YEAR_DEFAULT_VALUE));

		if (tempSettings != null) {
			filterForm.setRenderStartYear(tempSettings.getReportStartYear());
			filterForm.setRenderEndYear(tempSettings.getReportEndYear());
		} else {
			filterForm.setRenderStartYear(-1);
			filterForm.setRenderEndYear(-1);
		}
		filterForm.setToYear(-1l);
		filterForm.setFromMonth(-1);
		filterForm.setToMonth(-1);
		filterForm.setFromDate(null);
		filterForm.setToDate(null);

		filterForm.setLineMinRank(null);
		filterForm.setPlanMinRank(null);
		filterForm.setText(null);
		filterForm.setPageSize(null);
		filterForm.setGovernmentApprovalProcedures(null);
		filterForm.setJointCriteria(null);
		filterForm.setJustSearch(null);
		filterForm.setRegionSelected(null);
		filterForm.setApprovalStatusSelected(null);
		// filterForm.setRegions(null);
		filterForm.setDonorGroups(null);
		filterForm.setDonorTypes(null);
		filterForm.setExecutingAgency(null);
		filterForm.setBeneficiaryAgency(null);
		filterForm.setImplementingAgency(null);
		filterForm.setDonnorAgency(null);
		if (tempSettings != null) {
			filterForm.setRenderStartYear(tempSettings.getReportStartYear());
			filterForm.setRenderEndYear(tempSettings.getReportEndYear());
		} else {
			filterForm.setRenderStartYear(-1);
			filterForm.setRenderEndYear(-1);
		}

		return modeApply(mapping, form, request, response);
	}

	public ActionForward modeSelect(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (request.getParameter("apply") != null && request.getAttribute("apply") == null)
			return modeApply(mapping, form, request, response);
		if (request.getParameter("reset") != null && request.getAttribute("reset") == null)
			return modeReset(mapping, form, request, response);

		HttpSession httpSession = request.getSession();

		if (httpSession.getAttribute(ArConstants.SELECTED_CURRENCY) == null) {
		
			AmpApplicationSettings tempSettings=getAppSetting(request);
			if (tempSettings != null) {
				String name = "- " + tempSettings.getCurrency().getCurrencyName();
				httpSession.setAttribute(ArConstants.SELECTED_CURRENCY, name);
			}
		}
		return mapping.findForward("forward");
	}

	/**
	 * generate a session based AmpARFilter object based on the form selections
	 * 
	 * @param mapping
	 * @param form
	 * @param requestTeamMember
	 *            teamMember = (TeamMember) httpSession
	 *            .getAttribute(Constants.CURRENT_MEMBER);
	 *            AmpApplicationSettings tempSettings =
	 *            DbUtil.getMemberAppSettings(teamMember.getMemberId());
	 * 
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward modeApply(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ReportsFilterPickerForm filterForm = (ReportsFilterPickerForm) form;
		HttpSession httpSession = request.getSession();

		Session session = PersistenceManager.getSession();
				
		request.setAttribute("apply", "apply");
		AmpARFilter arf;
		if ( filterForm.getSourceIsReportWizard() != null && filterForm.getSourceIsReportWizard() ) {
			arf	= new AmpARFilter();
		}
		else {
			arf 	= (AmpARFilter) httpSession.getAttribute(ArConstants.REPORTS_FILTER);
			if (arf == null)
				arf = new AmpARFilter();
		}
		arf.readRequestData(request);

		// for each sector we have also to add the subsectors

		Set selectedSectors = Util.getSelectedObjects(AmpSector.class, filterForm.getSelectedSectors());
		Set selectedSecondarySectors = Util.getSelectedObjects(AmpSector.class, filterForm.getSelectedSecondarySectors());

		if (selectedSectors != null && selectedSectors.size() > 0) {
			arf.setSelectedSectors(new HashSet());
			arf.getSelectedSectors().addAll(selectedSectors);

			arf.setSectors(SectorUtil.getSectorDescendents(selectedSectors));
		} else {
			arf.setSectors(null);
			arf.setSelectedSectors(null);
		}

		if (selectedSecondarySectors != null && selectedSecondarySectors.size() > 0) {
			arf.setSelectedSecondarySectors(new HashSet());
			arf.getSelectedSecondarySectors().addAll(selectedSecondarySectors);

			arf.setSecondarySectors(SectorUtil.getSectorDescendents(selectedSecondarySectors));
		} else {
			arf.setSecondarySectors(null);
			arf.setSelectedSecondarySectors(null);
		}

		Set selectedNatPlanObj = Util.getSelectedObjects(AmpTheme.class, filterForm.getSelectedNatPlanObj());
		Set selectedPrimaryPrograms = Util.getSelectedObjects(AmpTheme.class, filterForm.getSelectedPrimaryPrograms());
		Set selectedSecondaryPrograms = Util.getSelectedObjects(AmpTheme.class, filterForm.getSelectedSecondaryPrograms());

		if (selectedNatPlanObj != null && selectedNatPlanObj.size() > 0) {
			arf.setSelectedNatPlanObj(new HashSet());
			arf.getSelectedNatPlanObj().addAll(selectedNatPlanObj);
			arf.setNationalPlanningObjectives(new ArrayList(selectedNatPlanObj));
		} else {
			arf.setSelectedNatPlanObj(null);
			arf.setNationalPlanningObjectives(null);
		}

		if (selectedPrimaryPrograms != null && selectedPrimaryPrograms.size() > 0) {
			arf.setSelectedPrimaryPrograms(new HashSet());
			arf.getSelectedPrimaryPrograms().addAll(selectedPrimaryPrograms);
			arf.setPrimaryPrograms(new ArrayList(selectedPrimaryPrograms));
		} else {

			arf.setPrimaryPrograms(null);
			arf.setSelectedPrimaryPrograms(null);

		}

		if (selectedSecondaryPrograms != null && selectedSecondaryPrograms.size() > 0) {
			arf.setSelectedSecondaryPrograms(new HashSet());
			arf.getSelectedSecondaryPrograms().addAll(selectedSecondaryPrograms);
			arf.setSecondaryPrograms(new ArrayList(selectedSecondaryPrograms));
		} else {
			arf.setSecondaryPrograms(null);
			arf.setSelectedSecondaryPrograms(null);

		}
		AmpApplicationSettings tempSettings = getAppSetting(request);
		AmpFiscalCalendar selcal = (AmpFiscalCalendar) Util.getSelectedObject(AmpFiscalCalendar.class, filterForm.getCalendar());
		if (!selcal.equals(arf.getCalendarType())) {
			arf.setCalendarType(selcal);
			if (filterForm.getRenderEndYear().intValue() == arf.getRenderEndYear().intValue() && filterForm.getRenderStartYear().intValue() == arf.getRenderStartYear().intValue()) {
				Integer defaultStart = getDefaultStartYear(request);
				Integer defaultEnd = getDefaultEndYear(request);
				if (filterForm.getCalendar() != null) {
					if (filterForm.getRenderStartYear() > 0)
					filterForm.setRenderStartYear(getYearOnCalendar(filterForm.getCalendar(), defaultStart,tempSettings));
				
					if (filterForm.getRenderEndYear()> 0)
					filterForm.setRenderEndYear(getYearOnCalendar(filterForm.getCalendar(), defaultEnd,tempSettings));

				}

			}
		}

		if (filterForm.getText() != null) {
			arf.setText(filterForm.getText());
		}

		if (filterForm.getIndexString() != null) {
			arf.setIndexText(filterForm.getIndexString());
		}

		arf.setYearFrom(filterForm.getFromYear() == null || filterForm.getFromYear().longValue() == -1 ? null : new Integer(filterForm.getFromYear().intValue()));
		arf.setYearTo(filterForm.getToYear() == null || filterForm.getToYear().longValue() == -1 ? null : new Integer(filterForm.getToYear().intValue()));
		arf.setFromMonth(filterForm.getFromMonth() == null || filterForm.getFromMonth().intValue() == -1 ? null : new Integer(filterForm.getFromMonth().intValue()));
		arf.setToMonth(filterForm.getToMonth() == null || filterForm.getToMonth().intValue() == -1 ? null : new Integer(filterForm.getToMonth().intValue()));
		arf.setFromDate(filterForm.getFromDate() == null ? null : new String(filterForm.getFromDate()));
		arf.setToDate(filterForm.getToDate() == null ? null : new String(filterForm.getToDate()));

		// arf.setDonors(Util.getSelectedObjects(AmpOrgGroup.class,filterForm.getSelectedDonors()));
		AmpCurrency currency = (AmpCurrency) Util.getSelectedObject(AmpCurrency.class, filterForm.getCurrency());
		arf.setCurrency(currency);
		String name = "- " + currency.getCurrencyName();
		httpSession.setAttribute(ArConstants.SELECTED_CURRENCY, name);
		Integer all = new Integer(-1);
		if (!all.equals(filterForm.getLineMinRank()))
			arf.setLineMinRank(filterForm.getLineMinRank());
		if (!all.equals(filterForm.getPlanMinRank()))
			arf.setPlanMinRank(filterForm.getPlanMinRank());
		if (!all.equals(filterForm.getRegionSelected()))
			arf.setRegionSelected(filterForm.getRegionSelected() == null || filterForm.getRegionSelected() == -1 ? 
					null : DynLocationManagerUtil.getLocation(filterForm.getRegionSelected(),false) );
		if (!all.equals(filterForm.getApprovalStatusSelected())){
			if(filterForm.getApprovalStatusSelected() != null){
				ArrayList<String> appvals = new ArrayList<String>();
				for (int i = 0; i < filterForm.getApprovalStatusSelected().length; i++) {
					String id = String.valueOf("" + filterForm.getApprovalStatusSelected()[i]);
					appvals.add(id);
				}
			    arf.setApprovalStatusSelected(appvals);
			}
			else{
				arf.setApprovalStatusSelected(null);
			}
		}
		else 
			arf.setApprovalStatusSelected(null);
		
		if (filterForm.getSelectedStatuses() != null && filterForm.getSelectedStatuses().length > 0)
			arf.setStatuses(new HashSet());
		else
			arf.setStatuses(null);

		for (int i = 0; filterForm.getSelectedStatuses() != null && i < filterForm.getSelectedStatuses().length; i++) {
			Long statusId						= Long.parseLong( filterForm.getSelectedStatuses()[i].toString() );
			AmpCategoryValue value 	= (AmpCategoryValue) session.load(AmpCategoryValue.class, statusId);
			arf.getStatuses().add(value);
		}
		if (filterForm.getSelectedProjectCategory() != null && filterForm.getSelectedProjectCategory().length > 0)
			arf.setProjectCategory(new HashSet());
		else
			arf.setProjectCategory(null);
		
		for (int i = 0; filterForm.getSelectedProjectCategory() != null && i < filterForm.getSelectedProjectCategory().length; i++) {
			Long id = Long.parseLong(filterForm.getSelectedProjectCategory()[i] + "");
			AmpCategoryValue value = (AmpCategoryValue) CategoryManagerUtil.getAmpCategoryValueFromDb(id);
			arf.getProjectCategory().add(value);
		}

		if (filterForm.getSelectedFinancingInstruments() != null && filterForm.getSelectedFinancingInstruments().length > 0)
			arf.setFinancingInstruments(new HashSet());
		else
			arf.setFinancingInstruments(null);
		
		for (int i = 0; filterForm.getSelectedFinancingInstruments() != null && i < filterForm.getSelectedFinancingInstruments().length; i++) {
			Long id = Long.parseLong(filterForm.getSelectedFinancingInstruments()[i] + "");
			AmpCategoryValue value = (AmpCategoryValue) CategoryManagerUtil.getAmpCategoryValueFromDb(id);
			arf.getFinancingInstruments().add(value);
		}

		if (filterForm.getSelectedTypeOfAssistance() != null && filterForm.getSelectedTypeOfAssistance().length > 0) {
			arf.setTypeOfAssistance(new HashSet<AmpCategoryValue>());
			for (int i = 0; i < filterForm.getSelectedTypeOfAssistance().length; i++) {
				Long id = filterForm.getSelectedTypeOfAssistance()[i];
				AmpCategoryValue value = CategoryManagerUtil.getAmpCategoryValueFromDb(id);
				if (value != null)
					arf.getTypeOfAssistance().add(value);
			}
		} else
			arf.setTypeOfAssistance(null);

		if (filterForm.getPageSize() != null) {
			arf.setPageSize(filterForm.getPageSize()); // set page size in the
			// ARF filter
		}

		arf.setRisks(Util.getSelectedObjects(AmpIndicatorRiskRatings.class, filterForm.getSelectedRisks()));

		arf.setGovernmentApprovalProcedures(filterForm.getGovernmentApprovalProcedures());
		arf.setJointCriteria(filterForm.getJointCriteria());

		if (filterForm.getSelectedDonorTypes() != null && filterForm.getSelectedDonorTypes().length > 0) {
			arf.setDonorTypes(new HashSet());
			for (int i = 0; i < filterForm.getSelectedDonorTypes().length; i++) {
				Long id = Long.parseLong("" + filterForm.getSelectedDonorTypes()[i]);
				AmpOrgType type = DbUtil.getAmpOrgType(id);
				if (type != null)
					arf.getDonorTypes().add(type);
			}
		} else
			arf.setDonorTypes(null);

		if (filterForm.getSelectedDonorGroups() != null && filterForm.getSelectedDonorGroups().length > 0) {
			arf.setDonorGroups(new HashSet());
			for (int i = 0; i < filterForm.getSelectedDonorGroups().length; i++) {
				Long id = Long.parseLong("" + filterForm.getSelectedDonorGroups()[i]);
				AmpOrgGroup grp = DbUtil.getAmpOrgGroup(id);
				if (grp != null)
					arf.getDonorGroups().add(grp);
			}
		} else
			arf.setDonorGroups(null);

		if (filterForm.getSelectedBudget() != null) {
			switch (filterForm.getSelectedBudget().intValue()) {
			case 0:
				break;
			case 1:
				arf.setBudget(true);
				break;
			case 2:
				arf.setBudget(false);
				break;

			}
		}
		else 
			arf.setBudget(null);
		
		arf.setJustSearch(filterForm.getJustSearch());

		arf.setRenderStartYear((filterForm.getRenderStartYear() != -1) ? filterForm.getRenderStartYear() : 0);
		arf.setRenderEndYear((filterForm.getRenderEndYear() != -1) ? filterForm.getRenderEndYear() : 0);

		DecimalFormat custom = new DecimalFormat();
		DecimalFormatSymbols ds = new DecimalFormatSymbols();
		ds.setDecimalSeparator((!"CUSTOM".equalsIgnoreCase(filterForm.getCustomDecimalSymbol()) ? filterForm.getCustomDecimalSymbol().charAt(0) : filterForm.getCustomDecimalSymbolTxt().charAt(0)));
		ds
				.setGroupingSeparator((!"CUSTOM".equalsIgnoreCase(filterForm.getCustomGroupCharacter()) ? filterForm.getCustomGroupCharacter().charAt(0) : filterForm.getCustomGroupCharacterTxt()
						.charAt(0)));
		custom.setMaximumFractionDigits((filterForm.getCustomDecimalPlaces() != -1) ? filterForm.getCustomDecimalPlaces() : 99);
		custom.setGroupingUsed(filterForm.getCustomUseGrouping());
		custom.setGroupingSize(filterForm.getCustomGroupSize());
		custom.setDecimalFormatSymbols(ds);
		arf.setCurrentFormat(custom);

		arf.setBeneficiaryAgency(ReportsUtil.processSelectedFilters(filterForm.getSelectedBeneficiaryAgency(), AmpOrganisation.class));
		arf.setDonnorgAgency(ReportsUtil.processSelectedFilters(filterForm.getSelectedDonnorAgency(), AmpOrganisation.class));
		arf.setResponsibleorg(ReportsUtil.processSelectedFilters(filterForm.getSelectedresponsibleorg(), AmpOrganisation.class));
		
		arf.setImplementingAgency(ReportsUtil.processSelectedFilters(filterForm.getSelectedImplementingAgency(), AmpOrganisation.class));
		arf.setExecutingAgency(ReportsUtil.processSelectedFilters(filterForm.getSelectedExecutingAgency(), AmpOrganisation.class));
		arf.setProjectCategory(ReportsUtil.processSelectedFilters(filterForm.getSelectedProjectCategory(), AmpOrganisation.class));
		
		if(filterForm.getDisbursementOrder()!=null){
			if(filterForm.getDisbursementOrder().equals(0)){
				arf.setDisbursementOrderRejected(false);
			}else if(filterForm.getDisbursementOrder().equals(1)){
				arf.setDisbursementOrderRejected(true);
			}else{
				arf.setDisbursementOrderRejected(null);
			}
		}
			

		if ( filterForm.getSourceIsReportWizard() != null && filterForm.getSourceIsReportWizard() ) {
			httpSession.setAttribute(ReportWizardAction.SESSION_FILTER, arf);
			return mapping.findForward("reportWizard");
		}
			
		httpSession.setAttribute(ArConstants.REPORTS_FILTER, arf);
		if (arf.isPublicView())
			return mapping.findForward("publicView");
		return mapping.findForward(arf.isWidget() ? "mydesktop" : "reportView");
	}

	public void reset(ActionForm form, HttpServletRequest request, ActionMapping mapping) {
		request.setAttribute("apply", null);
		ReportsFilterPickerForm filterForm = (ReportsFilterPickerForm) form;
		filterForm.setSelectedRisks(null);
		filterForm.setSelectedSectors(null);
		filterForm.setSelectedStatuses(null);
		filterForm.setSelectedNatPlanObj(null);
		filterForm.setJustSearch(null);
		filterForm.setSelectedPrimaryPrograms(null);
		filterForm.setSelectedSecondarySectors(null);
		HttpSession httpSession = request.getSession();
		AmpApplicationSettings tempSettings=getAppSetting(request);
		if (tempSettings != null) {
			filterForm.setCurrency(tempSettings.getCurrency().getAmpCurrencyId());
			String name = "- " + tempSettings.getCurrency().getCurrencyName();
			if (httpSession.getAttribute(ArConstants.SELECTED_CURRENCY) == null)
				httpSession.setAttribute(ArConstants.SELECTED_CURRENCY, name);
			if(tempSettings.getFiscalCalendar() != null && tempSettings.getFiscalCalendar().getAmpFiscalCalId() != null) {
				filterForm.setCalendar(tempSettings.getFiscalCalendar().getAmpFiscalCalId());
			}
		}

		// Long
		// fromYear=Long.parseLong(FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.START_YEAR_DEFAULT_VALUE));
		filterForm.setFromYear(-1l);

		// Long
		// toYear=Long.parseLong(FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.END_YEAR_DEFAULT_VALUE));
		filterForm.setToYear(-1l);
		filterForm.setFromMonth(-1);
		filterForm.setToMonth(-1);
		filterForm.setFromDate(null);
		filterForm.setToDate(null);

		filterForm.setLineMinRank(null);
		filterForm.setPlanMinRank(null);
		filterForm.setText(null);
		filterForm.setPageSize(null);
		filterForm.setGovernmentApprovalProcedures(null);
		filterForm.setJointCriteria(null);
		filterForm.setRegionSelected(null);
		filterForm.setApprovalStatusSelected(null);
		filterForm.setDonorGroups(null);
		filterForm.setDonorTypes(null);
		filterForm.setExecutingAgency(null);
		filterForm.setBeneficiaryAgency(null);
		filterForm.setDonnorAgency(null);
		filterForm.setImplementingAgency(null);
		filterForm.reset(mapping, request);
	}

	public void resetFormat(ActionForm form, HttpServletRequest request, ActionMapping mapping) {
		HttpSession httpSession = request.getSession();
		ReportsFilterPickerForm filterForm = (ReportsFilterPickerForm) form;
		AmpARFilter arf = (AmpARFilter) httpSession.getAttribute(ArConstants.REPORTS_FILTER);
		arf.setCurrentFormat(null);
		filterForm.setCustomDecimalSymbol(null);
		filterForm.setCustomDecimalPlaces(null);
		filterForm.setCustomGroupCharacter(null);
		filterForm.setCustomUseGrouping(null);
		filterForm.setCustomGroupSize(null);
		filterForm.setResetFormat(null);
	}

	private Integer getDefaultStartYear(HttpServletRequest request) {
		AmpApplicationSettings tempSettings = getAppSetting(request);
		Integer rStart = -1;
		if (tempSettings != null && tempSettings.getReportStartYear() != null && tempSettings.getReportStartYear().intValue() != 0) {
			rStart = tempSettings.getReportStartYear();
		} else {
			String gvalue = FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.START_YEAR_DEFAULT_VALUE);
			if (gvalue != null && !"".equalsIgnoreCase(gvalue) && Integer.parseInt(gvalue) > 0) {
				rStart = Integer.parseInt(gvalue);
			}
		}

		return rStart;
	}

	private Integer getDefaultEndYear(HttpServletRequest request) {
		AmpApplicationSettings tempSettings = getAppSetting(request);
		Integer rEnd = -1;
		if (tempSettings != null && tempSettings.getReportEndYear() != null && tempSettings.getReportEndYear().intValue() != 0) {
			rEnd = tempSettings.getReportEndYear();
		} else {
			String gvalue = FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.END_YEAR_DEFAULT_VALUE);
			if (gvalue != null && !"".equalsIgnoreCase(gvalue) && Integer.parseInt(gvalue) > 0) {
				rEnd = Integer.parseInt(gvalue);
			}
		}
		return rEnd;
	}

	
	private Integer getYearOnCalendar(AmpFiscalCalendar calendar, Integer pyear, AmpFiscalCalendar defCalendar) {
		if (pyear==null) return 0;
		
		Integer year=null;
		try {
			Date testDate=new SimpleDateFormat("dd/MM/yyyy").parse("11/09/"+pyear);
			ICalendarWorker work1=defCalendar.getworker();
			work1.setTime(testDate);
			ICalendarWorker work2=calendar.getworker();
			work2.setTime(testDate);
			int diff=work2.getYearDiff(work1);
			pyear=pyear+diff;
			return pyear;
		} catch (Exception e) {
			logger.error("Can't get year on calendar",e);
		}
		return year;
	
	}
	private Integer getYearOnCalendar(Long calendarId, Integer pyear,AmpApplicationSettings 	tempSettings ) {
		if (pyear==null) return 0;
		AmpFiscalCalendar	cal = FiscalCalendarUtil.getAmpFiscalCalendar(calendarId);
	
		AmpFiscalCalendar defauCalendar=null;
		if  (tempSettings!=null)
			defauCalendar=tempSettings.getFiscalCalendar();
		
		if (defauCalendar==null){
			String calValue = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_CALENDAR);
			defauCalendar=FiscalCalendarUtil.getAmpFiscalCalendar(Long.parseLong(calValue));
		}
		
		if(defauCalendar==null) return 0;
		
		Integer year=getYearOnCalendar(cal, pyear,defauCalendar);
		cal=null;
		return year;
	}
	
	public static AmpApplicationSettings getAppSetting(HttpServletRequest request) {
		HttpSession httpSession = request.getSession();
		TeamMember teamMember = (TeamMember) httpSession.getAttribute(Constants.CURRENT_MEMBER);
		AmpApplicationSettings tempSettings = null;
		if (teamMember != null) {
			tempSettings = DbUtil.getMemberAppSettings(teamMember.getMemberId());
		}
		return tempSettings;
	}

	
}

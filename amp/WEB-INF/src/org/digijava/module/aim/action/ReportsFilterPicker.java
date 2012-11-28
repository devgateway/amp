/**
 * 
 */
package org.digijava.module.aim.action;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
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
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.action.reportwizard.ReportWizardAction;
import org.digijava.module.aim.ar.util.FilterUtil;
import org.digijava.module.aim.ar.util.ReportsUtil;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpIndicatorRiskRatings;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTeam;
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
import org.digijava.module.aim.util.HierarchyListableUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.MEIndicatorsUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.aim.util.filters.DateListableImplementation;
import org.digijava.module.aim.util.filters.GroupingElement;
import org.digijava.module.aim.util.filters.HierarchyListableImplementation;
import org.digijava.module.aim.util.time.StopWatch;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.hibernate.LazyInitializationException;
import org.hibernate.Session;
import org.springframework.beans.BeanWrapperImpl;

import com.sun.media.jai.util.RWLock;

/**
 * @author mihai
 * 
 */
public class ReportsFilterPicker extends MultiAction {
	private static Logger logger = Logger.getLogger(ReportsFilterPicker.class);
	
	final String KEY_RISK_PREFIX = "aim:risk:";
	public final static String ONLY_JOINT_CRITERIA	= "0";
	public final static String ONLY_GOV_PROCEDURES	= "1";
	
	
	int curYear=new GregorianCalendar().get(Calendar.YEAR);
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
		
		//ServletContext ampContext = getServlet().getServletContext();
 	 	Site site = RequestUtils.getSite(request);
 	 	//String siteId = site.getId().toString();
 	 	//String locale = RequestUtils.getNavigationLanguage(request).getCode();
 	 	
 	 	String ampReportId 	= request.getParameter("ampReportId");
		if ( "".equals(ampReportId) )
			ampReportId		= null;

		if (ampReportId!=null) {
			if (filterForm.getAmpReportId()==null || !ampReportId.equals(String.valueOf(filterForm.getAmpReportId()))) {
				if (Boolean.valueOf(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS)))
					filterForm.setAmountinthousands(true);
				else 
					filterForm.setAmountinthousands(false);
			}
		}
		
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
		
		if (teamMember != null && teamMember.getTeamId() == null )
			teamMember = null;
		
		if ( teamMember != null ) {
			AmpApplicationSettings tempSettings = DbUtil.getMemberAppSettings(teamMember.getMemberId());
			if (tempSettings == null)
				if (teamMember != null)
					tempSettings = DbUtil.getTeamAppSettings(teamMember.getTeamId());
			
			String applyFormat				= request.getParameter("applyFormat");
			String applyStr					= request.getParameter("apply");
			AmpARFilter existingFilter		= (AmpARFilter)request.getSession().getAttribute(ReportWizardAction.EXISTING_SESSION_FILTER);
			if ( existingFilter != null ) {
				if ( !"true".equals(applyStr) ) {
					FilterUtil.populateForm(filterForm, existingFilter);
					request.getSession().setAttribute(ReportWizardAction.EXISTING_SESSION_FILTER, null);
				}
				if (filterForm.getCalendar() == null){
					filterForm.setCalendar(existingFilter.getCalendarType()==null ? tempSettings.getFiscalCalendar().getAmpFiscalCalId() : existingFilter.getCalendarType().getAmpFiscalCalId());
				}
				if (filterForm.getCurrency() == null){
					filterForm.setCurrency(existingFilter.getCurrency()==null ? tempSettings.getCurrency().getAmpCurrencyId() : existingFilter.getCurrency().getAmpCurrencyId());
				}
			} else 
				if ( !"Apply Format".equals(applyFormat) ) {
					if (filterForm.getCalendar() == null){
						filterForm.setCalendar(tempSettings.getFiscalCalendar().getAmpFiscalCalId());
					}
					if (filterForm.getCurrency() == null){
						filterForm.setCurrency(tempSettings.getCurrency().getAmpCurrencyId());
					}
				}
		}

		Long ampTeamId = null;
		if (teamMember != null)
			ampTeamId = teamMember.getTeamId();
		if (teamMember != null)
			filterForm.setTeamAccessType(teamMember.getTeamAccessType());

		filterForm.setFromYears(new ArrayList<BeanWrapperImpl>());
		filterForm.setToYears(new ArrayList<BeanWrapperImpl>());


		Long yearFrom = Long.parseLong(FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.YEAR_RANGE_START));
		Long countYear = Long.parseLong(FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.NUMBER_OF_YEARS_IN_RANGE));
	
		if (filterForm.getCountYear() == null) {
			filterForm.setCountYear(countYear);
		}

		if (filterForm.getCountYearFrom() == null) {
			filterForm.setCountYearFrom(yearFrom);
		}

		
		for (long i = yearFrom; i <= (yearFrom + countYear); i++) {
			filterForm.getFromYears().add(new BeanWrapperImpl(new Long(i)));
			filterForm.getToYears().add(new BeanWrapperImpl(new Long(i)));
		}
		if(filterForm.getCurrency()==null){
		AmpApplicationSettings tempSettings = getAppSetting(request);
		if (tempSettings != null) {
			filterForm.setCurrency(tempSettings.getCurrency().getAmpCurrencyId());
			String name = "- " + tempSettings.getCurrency().getCurrencyName();
			httpSession.setAttribute(ArConstants.SELECTED_CURRENCY, name);
			filterForm.setCalendar(tempSettings.getFiscalCalendar().getAmpFiscalCalId());
		}
               }
		
		Collection currency = CurrencyUtil.getActiveAmpCurrencyByName();
	     //Only currencies having exchanges rates AMP-2620
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
		 filterForm.setCalendars(allFisCalenders);
		 
		 ArrayList<String> decimalseparators = new ArrayList<String>();
		 DecimalFormat defaultDecimalFormat = FormatHelper.getDecimalFormat();
		String selecteddecimalseparator  = String.valueOf((defaultDecimalFormat.getDecimalFormatSymbols().getDecimalSeparator()));
		 
		 if (!selecteddecimalseparator.equalsIgnoreCase(".") && !selecteddecimalseparator.equalsIgnoreCase(",") ){
			 decimalseparators.add(selecteddecimalseparator);
		 }
		 
		 decimalseparators.add(".");
		 decimalseparators.add(",");
		 //decimalseparators.add(TranslatorWorker.translateText("CUSTOM",request));
		 filterForm.setAlldecimalSymbols(decimalseparators);
		 
		 ArrayList<String> groupseparators = new ArrayList<String>();
		 String selectedgroupingseparator  = String.valueOf(defaultDecimalFormat.getDecimalFormatSymbols().getGroupingSeparator());
		 
		 if (!selectedgroupingseparator.equalsIgnoreCase(".") && !selectedgroupingseparator.equalsIgnoreCase(",") ){
			 groupseparators.add(selectedgroupingseparator);
		 }
		 
		 groupseparators.add(".");
		 groupseparators.add(",");
		 //groupseparators.add(TranslatorWorker.translateText("CUSTOM",request));
		 filterForm.setAllgroupingseparators(groupseparators);
		 
		 if (filterForm.getCustomDecimalSymbol() == null) {
			 filterForm.setCustomDecimalSymbol(selecteddecimalseparator);
			 filterForm.setCustomDecimalPlaces(defaultDecimalFormat.getMaximumFractionDigits());
			 filterForm.setCustomGroupCharacter(selectedgroupingseparator);
			 filterForm.setCustomUseGrouping(defaultDecimalFormat.isGroupingUsed());
			 filterForm.setCustomGroupSize(defaultDecimalFormat.getGroupingSize());
		 }
		
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
		Session session = PersistenceManager.getSession();
		if (filterForm.getAmpReportId()!=null){
			AmpReports r = (AmpReports) session.get(AmpReports.class, new Long(filterForm.getAmpReportId()));
			filterForm.setReporttype(r.getType());
		}
		
		if(request.getParameter("init")!=null || "true".equals( request.getAttribute(ReportWizardAction.REPORT_WIZARD_INIT_ON_FILTERS)) ) 
				return null; 
		else 
			modeRefreshDropdowns(mapping, form, request, response, getServlet().getServletContext());
			return modeSelect(mapping, form, request, response);
		}
	
	public void modeRefreshDropdowns(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, ServletContext ampContext) throws Exception {
		StopWatch.reset("Filters");
		StopWatch.next("Filters", true);
		String ampReportId 	= request.getParameter("ampReportId");
		
		if (ampReportId!=null && ampReportId.equalsIgnoreCase("")){
			ampReportId = null;
		}
		ReportsFilterPickerForm filterForm = (ReportsFilterPickerForm) form;
		//ServletContext ampContext = getServlet().getServletContext();
		HttpSession httpSession = request.getSession();
		TeamMember teamMember = (TeamMember) httpSession.getAttribute(Constants.CURRENT_MEMBER);
	 	Site site = RequestUtils.getSite(request);
 	 	String siteId = site.getId().toString();
 	 	String locale = RequestUtils.getNavigationLanguage(request).getCode();
 	 	
		// create filter dropdowns
		
	      
		/**
 	 	* For filterPicker ver2
 	 	*/
 	 	List<AmpSector> ampSectors = SectorUtil.getAmpSectorsAndSubSectorsHierarchy(AmpClassificationConfiguration.PRIMARY_CLASSIFICATION_CONFIGURATION_NAME);
		
 	 	List<AmpSector> secondaryAmpSectors = SectorUtil.getAmpSectorsAndSubSectorsHierarchy(AmpClassificationConfiguration.SECONDARY_CLASSIFICATION_CONFIGURATION_NAME);
        List<AmpSector> tertiaryAmpSectors = SectorUtil.getAmpSectorsAndSubSectorsHierarchy(AmpClassificationConfiguration.TERTIARY_CLASSIFICATION_CONFIGURATION_NAME);
 	 	HierarchyListableUtil.changeTranslateable(ampSectors, false);
 	 	HierarchyListableUtil.changeTranslateable(secondaryAmpSectors, false);
 	 	HierarchyListableUtil.changeTranslateable(tertiaryAmpSectors, false);
        
        
 	 	filterForm.setSectorElements(new ArrayList<GroupingElement<HierarchyListableImplementation>>());
 	 	filterForm.setProgramElements(new ArrayList<GroupingElement<AmpTheme>>()); 	 	
 	 	
 	 	if (FeaturesUtil.isVisibleField("Sector", ampContext)){                
 	 	HierarchyListableImplementation rootAmpSectors  = new HierarchyListableImplementation();
 	 	rootAmpSectors.setLabel("Primary Sectors");
 	 	rootAmpSectors.setUniqueId(0 + "");
 	 	rootAmpSectors.setChildren(ampSectors);
 	 	GroupingElement<HierarchyListableImplementation> sectorsElement = new GroupingElement<HierarchyListableImplementation>("Primary Sectors", "filter_sectors_div", rootAmpSectors, "selectedSectors");
 	 	filterForm.getSectorElements().add(sectorsElement);
 	 	}
 	 	
 	 	if (FeaturesUtil.isVisibleField("Secondary Sector", ampContext)){
 	 		HierarchyListableImplementation rootSecondaryAmpSectors = new HierarchyListableImplementation();
 	 		rootSecondaryAmpSectors.setLabel("Secondary Sectors");
 	 		rootSecondaryAmpSectors.setUniqueId("0");
 	 		rootSecondaryAmpSectors.setChildren(secondaryAmpSectors);
 	 		GroupingElement<HierarchyListableImplementation> secondarySectorsElement = new GroupingElement<HierarchyListableImplementation>("Secondary Sectors", "filter_secondary_sectors_div", rootSecondaryAmpSectors, "selectedSecondarySectors");
 	 		filterForm.getSectorElements().add(secondarySectorsElement);
 	 	}

        if (FeaturesUtil.isVisibleField("Tertiary Sector", ampContext)){
 	 		HierarchyListableImplementation rootTertiaryAmpSectors = new HierarchyListableImplementation();
 	 		rootTertiaryAmpSectors.setLabel("Tertiary Sector");
 	 		rootTertiaryAmpSectors.setUniqueId("0");
 	 		rootTertiaryAmpSectors.setChildren(tertiaryAmpSectors);
 	 		GroupingElement<HierarchyListableImplementation> tertiarySectorsElement = new GroupingElement<HierarchyListableImplementation>("Tertiary Sectors", "filter_tertiary_sectors_div", rootTertiaryAmpSectors, "selectedTertiarySectors");
 	 		filterForm.getSectorElements().add(tertiarySectorsElement);
 	 	}
 	 		
 	 	
        StopWatch.next("Filters", true, "before programs");
        if ( FeaturesUtil.isVisibleModule("National Planning Dashboard", ampContext) ) {
	        Collection<AmpTheme> allPrograms	= ProgramUtil.getAllThemes(true);
	        HashMap<Long, AmpTheme> progMap		= ProgramUtil.prepareStructure(allPrograms);
	        
			AmpActivityProgramSettings primaryPrgSetting = ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.PRIMARY_PROGRAM);
			AmpTheme primaryProg = null;
			List<AmpTheme> primaryPrograms;		
			if (primaryPrgSetting!=null && primaryPrgSetting.getDefaultHierarchy() != null) {
				//primaryProg= ProgramUtil.getAmpThemesAndSubThemesHierarchy(primaryPrgSetting.getDefaultHierarchy());
				primaryProg = progMap.get(primaryPrgSetting.getDefaultHierarchyId() );
				HierarchyListableUtil.changeTranslateable(primaryProg, false);
				GroupingElement<AmpTheme> primaryProgElement = new GroupingElement<AmpTheme>("Primary Program", "filter_primary_prog_div", primaryProg, "selectedPrimaryPrograms");
				filterForm.getProgramElements().add(primaryProgElement);
			} /*else {
				primaryPrograms = ProgramUtil.getAllSubThemesFor(ProgramUtil.getAllThemes(false));
				for (AmpTheme ampTheme : primaryPrograms) {
					GroupingElement<AmpTheme> primaryProgElement = new GroupingElement<AmpTheme>("Primary Program", "filter_primary_prog_div", ampTheme, "selectedPrimaryPrograms");
					filterForm.getProgramElements().add(primaryProgElement);
				}
			}	*/
			
	//		if (primaryPrgSetting!=null) {
	//			primaryProg = ProgramUtil.getAmpThemesAndSubThemesHierarchy(primaryPrgSetting.getDefaultHierarchy());
	//			GroupingElement<AmpTheme> primaryProgElement = new GroupingElement<AmpTheme>("Primary Program", "filter_primary_prog_div", primaryProg, "selectedPrimaryPrograms");
	//			filterForm.getProgramElements().add(primaryProgElement);
	//	 	}
			
			AmpTheme secondaryProg = null;
	 	 	AmpActivityProgramSettings secondaryPrg = ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.SECONDARY_PROGRAM);
	 	 	List<AmpTheme> secondaryPrograms;		
			if (secondaryPrg!=null && secondaryPrg.getDefaultHierarchy() != null) {
				//secondaryProg= ProgramUtil.getAmpThemesAndSubThemesHierarchy(secondaryPrg.getDefaultHierarchy());
				secondaryProg	= progMap.get(secondaryPrg.getDefaultHierarchyId() );
				HierarchyListableUtil.changeTranslateable(secondaryProg, false);
				GroupingElement<AmpTheme> secondaryProgElement = new GroupingElement<AmpTheme>("Secondary Program", "filter_secondary_prog_div", secondaryProg, "selectedSecondaryPrograms");
				filterForm.getProgramElements().add(secondaryProgElement);
			}/* else {
				secondaryPrograms = ProgramUtil.getAllSubThemesFor(ProgramUtil.getAllThemes(false));
				for (AmpTheme ampTheme : secondaryPrograms) {
					GroupingElement<AmpTheme> secondaryProgElement = new GroupingElement<AmpTheme>("Secondary Program", "filter_secondary_prog_div", ampTheme, "selectedSecondaryPrograms");
					filterForm.getProgramElements().add(secondaryProgElement);
				}
			}*/
	// 	 	if (secondaryPrg!=null) {
	//			secondaryProg = ProgramUtil.getAmpThemesAndSubThemesHierarchy(secondaryPrg.getDefaultHierarchy());
	//			GroupingElement<AmpTheme> secondaryProgElement = new GroupingElement<AmpTheme>("Secondary Program", "filter_secondary_prog_div", secondaryProg, "selectedSecondaryPrograms");
	//			filterForm.getProgramElements().add(secondaryProgElement);
	//		}
	 	 	
	 	 	
	 	 	
	 	 	
			AmpActivityProgramSettings natPlanSetting       = ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.NATIONAL_PLAN_OBJECTIVE);
	// 	 	AmpTheme nationalPlanningProg                           = ProgramUtil.getAmpThemesAndSubThemesHierarchy(natPlanSetting.getDefaultHierarchy());
	// 	 	
	// 	 	GroupingElement<AmpTheme> natPlanProgElement = new GroupingElement<AmpTheme>("National Planning Objective", "filter_nat_plan_obj_div", nationalPlanningProg, "selectedNatPlanObj"); 	 	
	// 	 	filterForm.getProgramElements().add(natPlanProgElement);
	 	 	
			List<AmpTheme> nationalPlanningObjectives;
	 	 	if (natPlanSetting!=null && natPlanSetting.getDefaultHierarchy() != null) {
	 	 		//AmpTheme nationalPlanningProg	= ProgramUtil.getAmpThemesAndSubThemesHierarchy(natPlanSetting.getDefaultHierarchy());
	 	 		AmpTheme nationalPlanningProg	= progMap.get(natPlanSetting.getDefaultHierarchyId() );
	 	 		HierarchyListableUtil.changeTranslateable(nationalPlanningProg, false);
	 	 	 	GroupingElement<AmpTheme> natPlanProgElement = new GroupingElement<AmpTheme>("National Planning Objective", "filter_nat_plan_obj_div", nationalPlanningProg, "selectedNatPlanObj"); 	 	
	 	 	 	filterForm.getProgramElements().add(natPlanProgElement);
			}/* else {
				nationalPlanningObjectives = ProgramUtil.getAllSubThemesFor(ProgramUtil.getAllThemes(false));
				for (AmpTheme ampTheme : nationalPlanningObjectives) {
					GroupingElement<AmpTheme> natPlanProgElement = new GroupingElement<AmpTheme>("National Planning Objective", "filter_nat_plan_obj_div", ampTheme, "selectedNatPlanObj"); 	 	
		 	 	 	filterForm.getProgramElements().add(natPlanProgElement);
				}
			}*/
        }
 	 	StopWatch.next("Filters", true, "After Programs");
 	 	Collection donorTypes = DbUtil.getAllOrgTypesOfPortfolio();
 	 	Collection<AmpOrgGroup> donorGroups = ARUtil.filterDonorGroups(DbUtil.getAllOrgGroupsOfPortfolio());
 	 	
 	 	HierarchyListableUtil.changeTranslateable(donorTypes, false);
 	 	HierarchyListableUtil.changeTranslateable(donorGroups, false);
 	 	
 	 	filterForm.setDonorElements(new ArrayList<GroupingElement<HierarchyListableImplementation>>());
 	 	
 	 	HierarchyListableImplementation rootOrgType = new HierarchyListableImplementation();
 	 	rootOrgType.setLabel("All Donor Types");
 	 	rootOrgType.setUniqueId("0");
 	 	rootOrgType.setChildren( donorTypes );
 	 	GroupingElement<HierarchyListableImplementation> donorTypeElement = new GroupingElement<HierarchyListableImplementation>("Donor Types", "filter_donor_types_div", rootOrgType, "selectedDonorTypes");
 	 	filterForm.getDonorElements().add(donorTypeElement);
 	 	
 	 	HierarchyListableImplementation rootOrgGroup = new HierarchyListableImplementation();
 	 	rootOrgGroup.setLabel("All Donor Groups");
 	 	rootOrgGroup.setUniqueId("0");
 	 	rootOrgGroup.setChildren( donorGroups );
 	 	GroupingElement<HierarchyListableImplementation> donorGroupElement = new GroupingElement<HierarchyListableImplementation>("Donor Groups", "filter_donor_groups_div", rootOrgGroup, "selectedDonorGroups");
 	 	filterForm.getDonorElements().add(donorGroupElement);
 	 	
 	 	Collection<AmpOrganisation> donors = ReportsUtil.getAllOrgByRoleOfPortfolio(Constants.ROLE_CODE_DONOR);
 	 	HierarchyListableUtil.changeTranslateable(donors, false);
 	 	HierarchyListableImplementation rootDonors = new HierarchyListableImplementation();
 	 	rootDonors.setLabel("All Donors");
 	 	rootDonors.setUniqueId("0");
 	 	rootDonors.setChildren( donors );
 	 	GroupingElement<HierarchyListableImplementation> donorsElement  = new GroupingElement<HierarchyListableImplementation>("Donor Agencies", "filter_donor_agencies_div", rootDonors, "selectedDonnorAgency");
 	 	filterForm.getDonorElements().add(donorsElement);
 	 	
 	 	filterForm.setRelatedAgenciesElements(new ArrayList<GroupingElement<HierarchyListableImplementation>>());
 	 	
 	 	if (FeaturesUtil.isVisibleFeature("Executing Agency", ampContext) ) {
 	 		Collection<AmpOrganisation> execAgencies = (ReportsUtil.getAllOrgByRoleOfPortfolio(Constants.ROLE_CODE_EXECUTING_AGENCY));
 	 		HierarchyListableUtil.changeTranslateable(execAgencies, false);
 	 		HierarchyListableImplementation rootExecAgencies = new HierarchyListableImplementation();
 	 		rootExecAgencies.setLabel("All Executing Agencies");
 	 		rootExecAgencies.setUniqueId("0");
 	 		rootExecAgencies.setChildren( execAgencies );
 	 		GroupingElement<HierarchyListableImplementation> execAgenciesElement = new GroupingElement<HierarchyListableImplementation>("Executing Agencies", "filter_executing_agencies_div", rootExecAgencies, "selectedExecutingAgency");
 	 		filterForm.getRelatedAgenciesElements().add(execAgenciesElement);
		}

 	 	if (FeaturesUtil.isVisibleFeature("Implementing Agency", ampContext) ) {
			Collection<AmpOrganisation> implemAgencies			= (ReportsUtil.getAllOrgByRoleOfPortfolio(Constants.ROLE_CODE_IMPLEMENTING_AGENCY));
			HierarchyListableUtil.changeTranslateable(implemAgencies, false);
			HierarchyListableImplementation rootImplemAgencies	= new HierarchyListableImplementation();
			rootImplemAgencies.setLabel("All Implementing Agencies");
			rootImplemAgencies.setUniqueId("0");
			rootImplemAgencies.setChildren( implemAgencies );
			GroupingElement<HierarchyListableImplementation> execAgenciesElement	=
					new GroupingElement<HierarchyListableImplementation>("Implementing Agencies", "filter_implementing_agencies_div", 
							rootImplemAgencies, "selectedImplementingAgency");
			filterForm.getRelatedAgenciesElements().add(execAgenciesElement);
			
		}
		if (FeaturesUtil.isVisibleFeature("Responsible Organization", ampContext) ) {
			Collection<AmpOrganisation> respAgencies			= (ReportsUtil.getAllOrgByRoleOfPortfolio(Constants.ROLE_CODE_RESPONSIBLE_ORG));
			HierarchyListableUtil.changeTranslateable(respAgencies, false);
			HierarchyListableImplementation rootRespAgencies	= new HierarchyListableImplementation();
			rootRespAgencies.setLabel("All Responsible Agencies");
			rootRespAgencies.setUniqueId("0");
			rootRespAgencies.setChildren( respAgencies );
			GroupingElement<HierarchyListableImplementation> respAgenciesElement	=
					new GroupingElement<HierarchyListableImplementation>("Responsible Agencies", "filter_responsible_agencies_div", 
							rootRespAgencies, "selectedresponsibleorg");
			filterForm.getRelatedAgenciesElements().add(respAgenciesElement);
			
		}
		if (FeaturesUtil.isVisibleFeature("Beneficiary Agency", ampContext) ) {
			Collection<AmpOrganisation> benAgencies			= (ReportsUtil.getAllOrgByRoleOfPortfolio(Constants.ROLE_CODE_BENEFICIARY_AGENCY));
			HierarchyListableUtil.changeTranslateable(benAgencies, false);
			HierarchyListableImplementation rootBenAgencies	= new HierarchyListableImplementation();
			rootBenAgencies.setLabel("All Beneficiary Agencies");
			rootBenAgencies.setUniqueId("0");
			rootBenAgencies.setChildren( benAgencies );
			GroupingElement<HierarchyListableImplementation> benAgenciesElement	=
					new GroupingElement<HierarchyListableImplementation>("Beneficiary Agencies", "filter_beneficiary_agencies_div", 
							rootBenAgencies, "selectedBeneficiaryAgency");
			filterForm.getRelatedAgenciesElements().add(benAgenciesElement);
			
		}
		
		
		filterForm.setFinancingLocationElements( new ArrayList<GroupingElement<HierarchyListableImplementation>>() );
		StopWatch.next("Filters", true, "BEFORE CATEGORY VALUES");
		if (true) { //Here needs to be a check to see if the field/feature is enabled
			Collection<AmpCategoryValue> finInstrValues	=
				CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.FINANCING_INSTRUMENT_KEY, true, request);	
			HierarchyListableImplementation rootFinancingInstrument	= new HierarchyListableImplementation();
			rootFinancingInstrument.setLabel("All Financing Instrument Values");
			rootFinancingInstrument.setUniqueId("0");
			rootFinancingInstrument.setChildren( finInstrValues );
			GroupingElement<HierarchyListableImplementation> finInstrElement	=
					new GroupingElement<HierarchyListableImplementation>("Financing Instrument", "filter_financing_instr_div", 
							rootFinancingInstrument, "selectedFinancingInstruments");
			filterForm.getFinancingLocationElements().add(finInstrElement);
		}
		
		if (true) { //Here needs to be a check to see if the field/feature is enabled
			Collection<AmpCategoryValue> typeOfAssistValues	=
				CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.TYPE_OF_ASSISTENCE_KEY, true, request);	
			HierarchyListableImplementation rootTypeOfAssistance	= new HierarchyListableImplementation();
			rootTypeOfAssistance.setLabel("All Type of Assistance Values");
			rootTypeOfAssistance.setUniqueId("0");
			rootTypeOfAssistance.setChildren( typeOfAssistValues );
			GroupingElement<HierarchyListableImplementation> typeOfAssistElement	=
					new GroupingElement<HierarchyListableImplementation>("Type of Assistance", "filter_type_of_assistance_div", 
							rootTypeOfAssistance, "selectedTypeOfAssistance");
			filterForm.getFinancingLocationElements().add(typeOfAssistElement);
		}
		
		if (FeaturesUtil.isVisibleField("Mode of Payment", ampContext)) { 
			Collection<AmpCategoryValue> modeOfPaymentValues	=
				CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.MODE_OF_PAYMENT_KEY, true, request);	
			HierarchyListableImplementation rootModeOfPayment	= new HierarchyListableImplementation();
			rootModeOfPayment.setLabel("All Mode of Payment Values");
			rootModeOfPayment.setUniqueId("0");
			rootModeOfPayment.setChildren( modeOfPaymentValues );
			GroupingElement<HierarchyListableImplementation> modeOfPaymentElement	=
					new GroupingElement<HierarchyListableImplementation>("Mode of Payment", "filter_mode_of_payment_div", 
							rootModeOfPayment, "selectedModeOfPayment");
			filterForm.getFinancingLocationElements().add(modeOfPaymentElement);
		}
		
		if (FeaturesUtil.isVisibleField("Project Category", ampContext)) { 
			Collection<AmpCategoryValue> projCategoryValues	=
				CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.PROJECT_CATEGORY_KEY, true, request);	
			HierarchyListableImplementation rootProjCategory	= new HierarchyListableImplementation();
			rootProjCategory.setLabel("All Project Category Values");
			rootProjCategory.setUniqueId("0");
			rootProjCategory.setChildren( projCategoryValues );
			GroupingElement<HierarchyListableImplementation> projCategoryElement	=
					new GroupingElement<HierarchyListableImplementation>("Project Category", "filter_project_category_div", 
							rootProjCategory, "selectedProjectCategory");
			filterForm.getFinancingLocationElements().add(projCategoryElement);
		}
		
		filterForm.setOtherCriteriaElements(new ArrayList<GroupingElement<HierarchyListableImplementation>>() );
		if (true) { //Here needs to be a check to see if the field/feature is enabled
			Collection<AmpCategoryValue> activityStatusValues	=
				CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.ACTIVITY_STATUS_KEY, true, request);	
			HierarchyListableImplementation rootActivityStatus	= new HierarchyListableImplementation();
			rootActivityStatus.setLabel("All");
			rootActivityStatus.setUniqueId("0");
			rootActivityStatus.setChildren( activityStatusValues );
			GroupingElement<HierarchyListableImplementation> activityStatusElement	=
					new GroupingElement<HierarchyListableImplementation>("Status", "filter_activity_status_div", 
							rootActivityStatus, "selectedStatuses");
			filterForm.getOtherCriteriaElements().add(activityStatusElement);
		}
		if (true) { //Here needs to be a check to see if the field/feature is enabled
			Collection<AmpTeam> creatorsList	= TeamUtil.getAllRelatedTeams();
			List<HierarchyListableImplementation> children	=
				new ArrayList<HierarchyListableImplementation>();

			HierarchyListableImplementation rootCreators = new HierarchyListableImplementation();
			rootCreators.setLabel("All");
			rootCreators.setUniqueId("0");
			rootCreators.setChildren( children );
			Iterator<AmpTeam> it = creatorsList.iterator();
			while(it.hasNext()){
				AmpTeam ampTeam = it.next();
				HierarchyListableImplementation creatorsDO	= new HierarchyListableImplementation();
				creatorsDO.setLabel( ampTeam.getName() + "" );
				creatorsDO.setUniqueId( ampTeam.getAmpTeamId() + "");
				children.add(creatorsDO);
			}

            //sort workspace list
            Collections.sort(children, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    return ((HierarchyListableImplementation) o1).getLabel().compareTo(((HierarchyListableImplementation) o2).getLabel());
                }
            });
            
			GroupingElement<HierarchyListableImplementation> activityStatusElement	=
					new GroupingElement<HierarchyListableImplementation>("Workspace", "filter_workspace_div", 
							rootCreators, "selectedWorkspaces");
			filterForm.getOtherCriteriaElements().add(activityStatusElement);
		}
		if(FeaturesUtil.isVisibleField("Project Implementing Unit", ampContext)){			
			Collection<AmpCategoryValue> projectImplementingUnits	=CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.PROJECT_IMPLEMENTING_UNIT_KEY, true, request);
			HierarchyListableImplementation rootProjectImplementingUnit	= new HierarchyListableImplementation();
			rootProjectImplementingUnit.setLabel("All Project Implementing Units");
			rootProjectImplementingUnit.setUniqueId("0");
			rootProjectImplementingUnit.setChildren(projectImplementingUnits);
			GroupingElement<HierarchyListableImplementation> projectImplUnitElement	=new GroupingElement<HierarchyListableImplementation>("Project Implementing Unit", "filter_project_impl_unit_div", 
					rootProjectImplementingUnit, "selectedProjectImplUnit");
			filterForm.getOtherCriteriaElements().add(projectImplUnitElement);
			
		}
		StopWatch.next("Filters", true, "AFTER CATEGORY VALUES");
		
		if (FeaturesUtil.isVisibleFeature("Disbursement Orders", ampContext)) { 
			Collection<HierarchyListableImplementation> children	= 
				new ArrayList<HierarchyListableImplementation>();
			HierarchyListableImplementation rootDisbursementOrders	= new HierarchyListableImplementation();
			rootDisbursementOrders.setLabel("All");
			rootDisbursementOrders.setUniqueId("-1");
			rootDisbursementOrders.setChildren( children );
			HierarchyListableImplementation notRejectedDO	= new HierarchyListableImplementation();
			notRejectedDO.setLabel("Not Rejected");
			notRejectedDO.setUniqueId("0");
			children.add(notRejectedDO);
			HierarchyListableImplementation rejectedDO	= new HierarchyListableImplementation();
			rejectedDO.setLabel("Rejected");
			rejectedDO.setUniqueId("1");
			children.add(rejectedDO);
			GroupingElement<HierarchyListableImplementation> disbOrdersElement	=
					new GroupingElement<HierarchyListableImplementation>("Disbursement Orders", "filter_disb_orders_div", 
							rootDisbursementOrders, "disbursementOrders");
			filterForm.getFinancingLocationElements().add(disbOrdersElement);
		}
		if (true) { //Here needs to be a check to see if the field/feature is enabled
			Collection<AmpCategoryValue> budgetCategoryValues	=
				CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.ACTIVITY_BUDGET_KEY, true, request);	
			HierarchyListableImplementation rootBudgetCategory	= new HierarchyListableImplementation();
			rootBudgetCategory.setLabel("All");
			rootBudgetCategory.setUniqueId("0");
			rootBudgetCategory.setChildren( budgetCategoryValues );
			GroupingElement<HierarchyListableImplementation> disbOrdersElement	=
				new GroupingElement<HierarchyListableImplementation>("Activity Budget", "filter_on_budget_div", 
						rootBudgetCategory, "selectedBudgets");
			filterForm.getFinancingLocationElements().add(disbOrdersElement);
		}
		if (true) { 
			Collection<AmpCategoryValueLocations> regions = DynLocationManagerUtil.getRegionsOfDefCountryHierarchy();
			try {
				HierarchyListableUtil.changeTranslateable(regions, false);
			}
			catch (LazyInitializationException e) {
				logger.warn("The regions had to be loaded again");
				logger.warn(e.getMessage());
				DynLocationManagerUtil.clearRegionsOfDefaultCountryCache();
				regions		= DynLocationManagerUtil.getRegionsOfDefCountryHierarchy();
				HierarchyListableUtil.changeTranslateable(regions, false);
			}
			
			Iterator<AmpCategoryValueLocations> it = regions.iterator();
			while (it.hasNext()) {
				AmpCategoryValueLocations loc = (AmpCategoryValueLocations) it
						.next();
				loc.getCountDescendants();
			}
			
			HierarchyListableImplementation rootRegions	= new HierarchyListableImplementation();
			rootRegions.setLabel("All Regions");
			rootRegions.setUniqueId("0");
			rootRegions.setChildren( regions );
			GroupingElement<HierarchyListableImplementation> regionsElement	=
					new GroupingElement<HierarchyListableImplementation>("Regions", "filter_regions_div", 
							rootRegions, "regionSelected");
			filterForm.getFinancingLocationElements().add(regionsElement);
		}
		if( FeaturesUtil.isVisibleField("Joint Criteria", ampContext) && 
				FeaturesUtil.isVisibleField("Government Approval Procedures", ampContext) ) { 
			Collection<HierarchyListableImplementation> children	= 
				new ArrayList<HierarchyListableImplementation>();
			HierarchyListableImplementation activitySettings	= new HierarchyListableImplementation();
			activitySettings.setLabel("Both Settings");
			activitySettings.setUniqueId("-1");
			activitySettings.setChildren( children );
			if ( FeaturesUtil.isVisibleField("Joint Criteria", ampContext) ) {
				HierarchyListableImplementation jointCriteriaDO	= new HierarchyListableImplementation();
				jointCriteriaDO.setLabel("Only Projects Under Joint Criteria");
				jointCriteriaDO.setUniqueId(ONLY_JOINT_CRITERIA);
				children.add(jointCriteriaDO);
			}
			if ( FeaturesUtil.isVisibleField("Government Approval Procedures", ampContext) ) {
				HierarchyListableImplementation govProceduresDO	= new HierarchyListableImplementation();
				govProceduresDO.setLabel("Only Projects Having Government Approval Procedures");
				govProceduresDO.setUniqueId(ONLY_GOV_PROCEDURES);
				children.add(govProceduresDO);
			}
			GroupingElement<HierarchyListableImplementation> activitySettingsElement	=
					new GroupingElement<HierarchyListableImplementation>("Activity Display Settings", "filter_act_settings_div", 
							activitySettings, "selectedActivitySettings");
			filterForm.getFinancingLocationElements().add(activitySettingsElement);
		}
		
		
		if (true) { //Here needs to be a check to see if the field/feature is enabled
			if(teamMember!=null){
				Collection<HierarchyListableImplementation> children	= 
					new ArrayList<HierarchyListableImplementation>();
				HierarchyListableImplementation rootApprovalStatus	= new HierarchyListableImplementation();
				rootApprovalStatus.setLabel("All");
				rootApprovalStatus.setUniqueId("-1");
				rootApprovalStatus.setChildren( children );
				HierarchyListableImplementation newDraftDO	= new HierarchyListableImplementation();
				newDraftDO.setLabel( TranslatorWorker.translateText("New Draft", locale, siteId) );
				newDraftDO.setUniqueId("1");
				children.add(newDraftDO);
				HierarchyListableImplementation newUnvalidatedDO	= new HierarchyListableImplementation();
				newUnvalidatedDO.setLabel( TranslatorWorker.translateText("New Unvalidated", locale, siteId) );
				newUnvalidatedDO.setUniqueId("2");
				children.add(newUnvalidatedDO);
				HierarchyListableImplementation validatedActDO	= new HierarchyListableImplementation();
				validatedActDO.setLabel( TranslatorWorker.translateText("Validated Activities", locale, siteId) );
				validatedActDO.setUniqueId("4");
				children.add(validatedActDO);
				HierarchyListableImplementation existingDraftDO	= new HierarchyListableImplementation();
				existingDraftDO.setLabel( TranslatorWorker.translateText("Existing Draft", locale, siteId) );
				existingDraftDO.setUniqueId("3");
				children.add(existingDraftDO);
				HierarchyListableImplementation existingUnvalidatedDO	= new HierarchyListableImplementation();
				existingUnvalidatedDO.setLabel( TranslatorWorker.translateText("Existing Unvalidated", locale, siteId) );
				existingUnvalidatedDO.setUniqueId("0");
				children.add(existingUnvalidatedDO);
				GroupingElement<HierarchyListableImplementation> approvalStatusElement	=
						new GroupingElement<HierarchyListableImplementation>("Approval Status", "filter_approval_status_div", 
								rootApprovalStatus, "approvalStatusSelected");
				filterForm.getOtherCriteriaElements().add(approvalStatusElement);
			}
		}
		
		if ( FeaturesUtil.isVisibleField("Line Ministry Rank", ampContext)) {
			Collection<HierarchyListableImplementation> children	= 
				new ArrayList<HierarchyListableImplementation>();
			HierarchyListableImplementation rootLineMinRank	= new HierarchyListableImplementation();
			rootLineMinRank.setLabel("All");
			rootLineMinRank.setUniqueId("-1");
			rootLineMinRank.setChildren( children );
			for (int i=1; i<6 ; i++) {
				HierarchyListableImplementation lineMinDO	= new HierarchyListableImplementation();
				lineMinDO.setLabel( i + "" );
				lineMinDO.setUniqueId( i + "");
				children.add(lineMinDO);
			}
			GroupingElement<HierarchyListableImplementation> lineMinRankElement	=
					new GroupingElement<HierarchyListableImplementation>("Line Ministry Rank", "filter_line_min_rank_div", 
							rootLineMinRank, "lineMinRanks");
			filterForm.getOtherCriteriaElements().add(lineMinRankElement);
		}
		if ( FeaturesUtil.isVisibleField("Ministry of Planning Rank", ampContext)) {
			Collection<HierarchyListableImplementation> children	= 
				new ArrayList<HierarchyListableImplementation>();
			HierarchyListableImplementation rootplanMinRank	= new HierarchyListableImplementation();
			rootplanMinRank.setLabel("All");
			rootplanMinRank.setUniqueId("-1");
			rootplanMinRank.setChildren( children );
			for (int i=1; i<6 ; i++) {
				HierarchyListableImplementation planMinDO	= new HierarchyListableImplementation();
				planMinDO.setLabel( i + "" );
				planMinDO.setUniqueId( i + "");
				children.add(planMinDO);
			}
			GroupingElement<HierarchyListableImplementation> planMinRankElement	=
					new GroupingElement<HierarchyListableImplementation>("Planning Ministry Rank", "filter_plan_min_rank_div", 
							rootplanMinRank, "planMinRanks");
			filterForm.getOtherCriteriaElements().add(planMinRankElement);
		}
		if (FeaturesUtil.isVisibleFeature("Archived", ampContext)) { //Here needs to be a check to see if the field/feature is enabled
			if(teamMember!=null){
				Collection<HierarchyListableImplementation> children	= 
					new ArrayList<HierarchyListableImplementation>();
				HierarchyListableImplementation rootArchivedStatus	= new HierarchyListableImplementation();
				rootArchivedStatus.setLabel("All");
				rootArchivedStatus.setUniqueId("0");
				rootArchivedStatus.setChildren( children );
				HierarchyListableImplementation unarchivedDO	= new HierarchyListableImplementation();
				unarchivedDO.setLabel( TranslatorWorker.translateText("Non-archived Activities", locale, siteId) );
				unarchivedDO.setUniqueId("1");
				children.add(unarchivedDO);
				HierarchyListableImplementation archivedDO	= new HierarchyListableImplementation();
				archivedDO.setLabel( TranslatorWorker.translateText("Archived Activities", locale, siteId) );
				archivedDO.setUniqueId("2");
				children.add(archivedDO);
				GroupingElement<HierarchyListableImplementation> archivedElement	=
						new GroupingElement<HierarchyListableImplementation>("Archived", "filter_archived_div", 
								rootArchivedStatus, "selectedArchivedStatus");
				filterForm.getOtherCriteriaElements().add(archivedElement);
			}
		}
		if ( FeaturesUtil.isVisibleFeature("Multi Donor", ampContext)) {
			Collection<HierarchyListableImplementation> children	= 
				new ArrayList<HierarchyListableImplementation>();
			HierarchyListableImplementation rootMultiDonor	= new HierarchyListableImplementation();
			rootMultiDonor.setLabel("All");
			rootMultiDonor.setUniqueId("-1");
			rootMultiDonor.setChildren( children );
			HierarchyListableImplementation trueMulti	= new HierarchyListableImplementation();
			trueMulti.setLabel( "Yes");
			trueMulti.setUniqueId( "yes" );
			children.add(trueMulti);
			
			HierarchyListableImplementation falseMulti	= new HierarchyListableImplementation();
			falseMulti.setLabel( "No");
			falseMulti.setUniqueId( "no" );
			children.add(falseMulti);
			
			GroupingElement<HierarchyListableImplementation> lineMinRankElement	=
					new GroupingElement<HierarchyListableImplementation>("Multiple Donors", "filter_multi_donor_div", 
							rootMultiDonor, "selectedMultiDonor");
			filterForm.getOtherCriteriaElements().add(lineMinRankElement);
		}
		
		if (FeaturesUtil.isVisibleField("Actual Start Date", ampContext) ) {
			
			Collection<DateListableImplementation> children		= 
					new ArrayList<DateListableImplementation>();
			DateListableImplementation fromDate			= new DateListableImplementation();	
			fromDate.setLabel("From");
			fromDate.setUniqueId("fromActivityStartDate");
			fromDate.setActionFormProperty("fromActivityStartDate");
			fromDate.setTranslateable(true);
			children.add(fromDate);
			
			DateListableImplementation toDate			= new DateListableImplementation();	
			toDate.setLabel("To");
			toDate.setUniqueId("toActivityStartDate");
			toDate.setActionFormProperty("toActivityStartDate");
			toDate.setTranslateable(true);
			children.add(toDate);
			
			DateListableImplementation rootDate			= new DateListableImplementation();
			rootDate.setLabel("Actual Start Date");
			rootDate.setTranslateable(true);
			rootDate.setUniqueId("0");
			rootDate.setChildren(children);
			GroupingElement<HierarchyListableImplementation> filterByTransactionDate	= 
					new GroupingElement<HierarchyListableImplementation>("Actual Start Date", "filter_activity_start_date_div", rootDate, "");
			
			filterForm.getOtherCriteriaElements().add(filterByTransactionDate);
		}
		if (FeaturesUtil.isVisibleField("Current Completion Date", ampContext) ) {
			
			Collection<DateListableImplementation> children		= 
					new ArrayList<DateListableImplementation>();
			DateListableImplementation fromDate			= new DateListableImplementation();	
			fromDate.setLabel("From");
			fromDate.setUniqueId("fromActivityActualCompletionDate");
			fromDate.setActionFormProperty("fromActivityActualCompletionDate");
			fromDate.setTranslateable(true);
			children.add(fromDate);
			
			DateListableImplementation toDate			= new DateListableImplementation();	
			toDate.setLabel("To");
			toDate.setUniqueId("toActivityActualCompletionDate");
			toDate.setActionFormProperty("toActivityActualCompletionDate");
			toDate.setTranslateable(true);
			children.add(toDate);
			
			DateListableImplementation rootDate			= new DateListableImplementation();
			rootDate.setLabel("Current Completion Date");
			rootDate.setTranslateable(true);
			rootDate.setUniqueId("0");
			rootDate.setChildren(children);
			GroupingElement<HierarchyListableImplementation> filterByTransactionDate	= 
					new GroupingElement<HierarchyListableImplementation>("Current Completion Date", "filter_activity_actual_completion_date_div", rootDate, "");
			
			filterForm.getOtherCriteriaElements().add(filterByTransactionDate);
		}
		if (FeaturesUtil.isVisibleField("Final Date for Contracting", ampContext) ) {
			Collection<DateListableImplementation> children		= 
					new ArrayList<DateListableImplementation>();
			DateListableImplementation fromDate			= new DateListableImplementation();	
			fromDate.setLabel("From");
			fromDate.setUniqueId("fromActivityFinalContractingDate");
			fromDate.setActionFormProperty("fromActivityFinalContractingDate");
			fromDate.setTranslateable(true);
			children.add(fromDate);
			
			DateListableImplementation toDate			= new DateListableImplementation();	
			toDate.setLabel("To");
			toDate.setUniqueId("toActivityFinalContractingDate");
			toDate.setActionFormProperty("toActivityFinalContractingDate");
			toDate.setTranslateable(true);
			children.add(toDate);
			
			DateListableImplementation rootDate			= new DateListableImplementation();
			rootDate.setLabel("Final Date for Contracting");
			rootDate.setTranslateable(true);
			rootDate.setUniqueId("0");
			rootDate.setChildren(children);
			GroupingElement<HierarchyListableImplementation> filterByTransactionDate	= 
					new GroupingElement<HierarchyListableImplementation>("Final Date for Contracting", "filter_activity_final_contracting_date_div", rootDate, "");
			
			filterForm.getOtherCriteriaElements().add(filterByTransactionDate);
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

		Collection meRisks = MEIndicatorsUtil.getAllIndicatorRisks();
		for (Iterator iter = meRisks.iterator(); iter.hasNext();) {
			AmpIndicatorRiskRatings element = (AmpIndicatorRiskRatings) iter.next();
			String value = element.getRatingName();
			String key = KEY_RISK_PREFIX + value.toLowerCase();
			key = key.replaceAll(" ", "");
			String msg = CategoryManagerUtil.translate(key, request, value);
			element.setRatingName(msg);
		}
		
		Collection allIndicatorRisks = meRisks;
		// Collection regions=LocationUtil.getAllVRegions();
		//filterForm.setCurrencies(currency);
		// filterForm.setDonors(donors);
		filterForm.setRisks(allIndicatorRisks);
		

		filterForm.setFromMonths(new ArrayList<BeanWrapperImpl>());
		filterForm.setToMonths(new ArrayList<BeanWrapperImpl>());

		filterForm.setCountYears(new ArrayList<BeanWrapperImpl>());
		filterForm.setComputedYearsRange(new ArrayList<BeanWrapperImpl>());
		filterForm.setActualAppYearsRange(new ArrayList<BeanWrapperImpl>());
		filterForm.setPageSizes(pageSizes);
		filterForm.setApprovalStatusSelectedCollection(new ArrayList());
		
		String calValue = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_CALENDAR);
		if (filterForm.getCalendar() == null && calValue != null) {
			filterForm.setCalendar(Long.parseLong(calValue));
		}
		
		
		
		
		for (long i = curYear-10; i < curYear; i ++) {
			filterForm.getComputedYearsRange().add(new BeanWrapperImpl(new Long(i)));
			filterForm.getActualAppYearsRange().add(new BeanWrapperImpl(new Long(i)));
		}
		
		for (long i = 10; i <= 100; i += 10) {
			filterForm.getCountYears().add(new BeanWrapperImpl(new Long(i)));
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

		if (filterForm.getRenderStartYear() == null || (request.getParameter("view") != null && "reset".compareTo(request.getParameter("view")) == 0)) {
			filterForm.setRenderStartYear(rStart);
			tempSettings = null;
		}

		if (filterForm.getRenderEndYear() == null || (request.getParameter("view") != null && "reset".compareTo(request.getParameter("view")) == 0)) {
			filterForm.setRenderEndYear(rEnd);
			tempSettings = null;
		}
		
		filterForm.getPageSizes().add(new BeanWrapperImpl(new String("A0")));
		filterForm.getPageSizes().add(new BeanWrapperImpl(new String("A1")));
		filterForm.getPageSizes().add(new BeanWrapperImpl(new String("A2")));
		filterForm.getPageSizes().add(new BeanWrapperImpl(new String("A3")));
		filterForm.getPageSizes().add(new BeanWrapperImpl(new String("A4")));

		if (ampReportId != null && ampReportId.length() > 0 ) {
			AmpReports rep = (AmpReports) DbUtil.getAmpReports(new Long(ampReportId));
			httpSession.setAttribute("filterCurrentReport", rep);
		}
		
		
		StopWatch.next("Filters", true);
	}
	

	public ActionForward modeReset(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ReportsFilterPickerForm filterForm = (ReportsFilterPickerForm) form;
		request.setAttribute("reset", "reset");
		// filterForm.setSelectedDonors(null);
		filterForm.setSelectedRisks(null);
		filterForm.setSelectedSectors(null);
		filterForm.setSelectedStatuses(null);
		filterForm.setSelectedWorkspaces(null);
		filterForm.setSelectedSecondaryPrograms(null);
		filterForm.setSelectedPrimaryPrograms(null);
		filterForm.setSelectedNatPlanObj(null);
		filterForm.setSelectedArchivedStatus(new Object[]{"1"});
		filterForm.setSelectedActivitySettings(null);
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

		filterForm.setLineMinRanks(null);
		filterForm.setPlanMinRanks(null);
		filterForm.setText(null);
		filterForm.setPageSize(null);
		filterForm.setGovernmentApprovalProcedures(null);
		filterForm.setJointCriteria(null);
		filterForm.setJustSearch(null);
		filterForm.setRegionSelected(null);
		filterForm.setApprovalStatusSelected(null);
		filterForm.setSelectedProjectImplUnit(null);
		// filterForm.setRegions(null);
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
        Set selectedTertiarySectors = Util.getSelectedObjects(AmpSector.class, filterForm.getSelectedTertiarySectors());

		if (selectedSectors != null && selectedSectors.size() > 0) {
			arf.setSelectedSectors(new HashSet());
			arf.getSelectedSectors().addAll(selectedSectors);

			arf.setSectors(SectorUtil.getSectorDescendents(selectedSectors));
			arf.setSectorsAndAncestors( new HashSet<AmpSector>() );
			arf.getSectorsAndAncestors().addAll( arf.getSectors() );
			arf.getSectorsAndAncestors().addAll( SectorUtil.getAmpParentSectors(selectedSectors) );
		} else {
			arf.setSectors(null);
			arf.setSelectedSectors(null);
			arf.setSectorsAndAncestors(null);
		}

		if (selectedSecondarySectors != null && selectedSecondarySectors.size() > 0) {
			arf.setSelectedSecondarySectors(new HashSet());
			arf.getSelectedSecondarySectors().addAll(selectedSecondarySectors);

			arf.setSecondarySectors(SectorUtil.getSectorDescendents(selectedSecondarySectors));
			
			arf.setSecondarySectorsAndAncestors( new HashSet<AmpSector>() );
			arf.getSecondarySectorsAndAncestors().addAll( arf.getSecondarySectors() );
			arf.getSecondarySectorsAndAncestors().addAll( SectorUtil.getAmpParentSectors(selectedSecondarySectors) );
		} else {
			arf.setSecondarySectors(null);
			arf.setSelectedSecondarySectors(null);
			arf.setSecondarySectorsAndAncestors(null);
		}
        	if (selectedTertiarySectors != null && selectedTertiarySectors.size() > 0) {
            arf.setSelectedTertiarySectors(new HashSet());
            arf.getSelectedTertiarySectors().addAll(selectedTertiarySectors);
            arf.setTertiarySectors(SectorUtil.getSectorDescendents(selectedTertiarySectors));
            arf.setTertiarySectorsAndAncestors(new HashSet<AmpSector>());
            arf.getTertiarySectorsAndAncestors().addAll(arf.getTertiarySectors());
            arf.getTertiarySectorsAndAncestors().addAll(SectorUtil.getAmpParentSectors(selectedTertiarySectors));
        } else {
            arf.setTertiarySectors(null);
            arf.setSelectedTertiarySectors(null);
            arf.setTertiarySectorsAndAncestors(null);
        }

		Set selectedNatPlanObj = Util.getSelectedObjects(AmpTheme.class, filterForm.getSelectedNatPlanObj());
		Set selectedPrimaryPrograms = Util.getSelectedObjects(AmpTheme.class, filterForm.getSelectedPrimaryPrograms());
		Set selectedSecondaryPrograms = Util.getSelectedObjects(AmpTheme.class, filterForm.getSelectedSecondaryPrograms());

		if (selectedNatPlanObj != null && selectedNatPlanObj.size() > 0) {
			arf.setSelectedNatPlanObj(new HashSet());
			arf.getSelectedNatPlanObj().addAll(selectedNatPlanObj);
			arf.setNationalPlanningObjectives( new ArrayList(selectedNatPlanObj) );
			
			arf.setRelatedNatPlanObjs(new HashSet<AmpTheme>() );
			ProgramUtil.collectFilteringInformation(selectedNatPlanObj, arf.getNationalPlanningObjectives(), arf.getRelatedNatPlanObjs() );
			
		} else {
			arf.setSelectedNatPlanObj(null);
			arf.setNationalPlanningObjectives(null);
			arf.setRelatedNatPlanObjs(null);
		}

		if (selectedPrimaryPrograms != null && selectedPrimaryPrograms.size() > 0) {
			arf.setSelectedPrimaryPrograms(new HashSet());
			arf.getSelectedPrimaryPrograms().addAll(selectedPrimaryPrograms);
			arf.setPrimaryPrograms(new ArrayList(selectedPrimaryPrograms));
			
			arf.setRelatedPrimaryProgs(new HashSet<AmpTheme>() );
			ProgramUtil.collectFilteringInformation(selectedPrimaryPrograms, arf.getPrimaryPrograms(), arf.getRelatedPrimaryProgs() );
			
		} else {
			arf.setPrimaryPrograms(null);
			arf.setSelectedPrimaryPrograms(null);
			arf.setRelatedPrimaryProgs(null);
		}

		if (selectedSecondaryPrograms != null && selectedSecondaryPrograms.size() > 0) {
			arf.setSelectedSecondaryPrograms(new HashSet());
			arf.getSelectedSecondaryPrograms().addAll(selectedSecondaryPrograms);
			arf.setSecondaryPrograms(new ArrayList(selectedSecondaryPrograms));
			
			arf.setRelatedSecondaryProgs( new HashSet<AmpTheme>() );
			ProgramUtil.collectFilteringInformation(selectedSecondaryPrograms, arf.getSecondaryPrograms(), arf.getRelatedSecondaryProgs() );
			
		} else {
			arf.setSecondaryPrograms(null);
			arf.setSelectedSecondaryPrograms(null);
			arf.setRelatedSecondaryProgs(null);

		}
		AmpApplicationSettings tempSettings = getAppSetting(request);
		AmpFiscalCalendar selcal = (AmpFiscalCalendar) Util.getSelectedObject(AmpFiscalCalendar.class, filterForm.getCalendar());
		if (!selcal.equals(arf.getCalendarType())) {
			arf.setCalendarType(selcal);
			if (filterForm.getRenderEndYear().intValue() == arf.getRenderEndYear().intValue() && filterForm.getRenderStartYear().intValue() == arf.getRenderStartYear().intValue()) {
				Integer defaultStart = getDefaultStartYear(request);
				Integer defaultEnd = getDefaultEndYear(request);
				if (filterForm.getCalendar() != null) {
					if (filterForm.getRenderStartYear() < 0)
					filterForm.setRenderStartYear(getYearOnCalendar(filterForm.getCalendar(), defaultStart,tempSettings));
				
					if (filterForm.getRenderEndYear()< 0)
					filterForm.setRenderEndYear(getYearOnCalendar(filterForm.getCalendar(), defaultEnd,tempSettings));

				}

			}
		}

		if (filterForm.getText() != null) {
			arf.setText(filterForm.getText());
		}

		if (filterForm.getIndexString() != null) {
			arf.setIndexText(filterForm.getIndexString());
			arf.setSearchMode(filterForm.getSearchMode());
		}

		arf.setYearFrom(filterForm.getFromYear() == null || filterForm.getFromYear().longValue() == -1 ? null : new Integer(filterForm.getFromYear().intValue()));
		arf.setYearTo(filterForm.getToYear() == null || filterForm.getToYear().longValue() == -1 ? null : new Integer(filterForm.getToYear().intValue()));
		arf.setFromMonth(filterForm.getFromMonth() == null || filterForm.getFromMonth().intValue() == -1 ? null : new Integer(filterForm.getFromMonth().intValue()));
		arf.setToMonth(filterForm.getToMonth() == null || filterForm.getToMonth().intValue() == -1 ? null : new Integer(filterForm.getToMonth().intValue()));
		arf.setFromDate(filterForm.getFromDate() == null ? null : new String(filterForm.getFromDate()));
		arf.setToDate(filterForm.getToDate() == null ? null : new String(filterForm.getToDate()));
		
		arf.setToActivityStartDate(filterForm.getToActivityStartDate() );
		arf.setFromActivityStartDate(filterForm.getFromActivityStartDate() );
		
		arf.setToActivityActualCompletionDate(filterForm.getToActivityActualCompletionDate() );
		arf.setFromActivityActualCompletionDate(filterForm.getFromActivityActualCompletionDate());
		
		arf.setToActivityFinalContractingDate(filterForm.getToActivityFinalContractingDate() );
		arf.setFromActivityFinalContractingDate(filterForm.getFromActivityFinalContractingDate());

		if (filterForm.getComputedYear()!=-1){
			arf.setComputedYear(filterForm.getComputedYear());
		}else{
			if (FeaturesUtil.isVisibleFeature("Computed Columns Filters", this.getServlet().getServletContext() ) )
				arf.setComputedYear(curYear);
			else
				arf.setComputedYear(null);
					
		}
		if (filterForm.getActualAppYear()!=-1){
			arf.setActualAppYear(filterForm.getActualAppYear());
		}
		else 
			arf.setActualAppYear(null);
		// arf.setDonors(Util.getSelectedObjects(AmpOrgGroup.class,filterForm.getSelectedDonors()));
		AmpCurrency currency;
		if (filterForm.getCurrency()!=null){
			currency = (AmpCurrency) Util.getSelectedObject(AmpCurrency.class, filterForm.getCurrency());
		}else{
			currency = (AmpCurrency) Util.getSelectedObject(AmpCurrency.class, filterForm.getDefaultCurrency());
		}
			
		arf.setCurrency(currency);
		String name = "- " + currency.getCurrencyName();
		httpSession.setAttribute(ArConstants.SELECTED_CURRENCY, name);
		Integer all = new Integer(-1);
		
		
		if ( filterForm.getLineMinRanks() != null && filterForm.getLineMinRanks().length > 0 ) {
	 	 	ArrayList<Integer> ranks        = new ArrayList<Integer>();
	 	 	for (int i=0; i< filterForm.getLineMinRanks().length;  i++) {
	 	 		Integer val     = Integer.parseInt((String)filterForm.getLineMinRanks()[i]);
	 	 		if ( val == 0 ) {
	 	 			ranks   = null;
	 	 			break;
	 	 		} else
	 	 			ranks.add(val);
	 	 	}
	 	 	arf.setLineMinRank(ranks);
	 	 }
	 	 
		if ( filterForm.getPlanMinRanks() != null && filterForm.getPlanMinRanks().length > 0 ) {
	 		ArrayList<Integer> ranks        = new ArrayList<Integer>();
	 	 	for (int i=0; i< filterForm.getPlanMinRanks().length;  i++) {
	 	 		Integer val     = Integer.parseInt((String)filterForm.getPlanMinRanks()[i]);
	 	 		if ( val == 0 ) {
	 	 			ranks   = null;
	 	 			break;
	 	 		} else
	 	 			ranks.add(val);
	 	 	}
	 	 	arf.setPlanMinRank(ranks);
	 	}
	 	 	
		//if (!all.equals(filterForm.getRegionSelected()))
		//	arf.setRegionSelected(filterForm.getRegionSelected() == null || filterForm.getRegionSelected() == -1 ? 
		//					null : DynLocationManagerUtil.getLocation(filterForm.getRegionSelected(),false) );
		
		Set selectedRegions = null;
		if (filterForm.getRegionSelected() != null){
			if (!filterForm.getRegionSelected()[0].toString().equals("-1")) {
				selectedRegions = Util.getSelectedObjects(AmpCategoryValueLocations.class, filterForm.getRegionSelected());
			}
		}
		
		if (selectedRegions != null && selectedRegions.size() > 0) {
			arf.setLocationSelected(new HashSet());
			arf.getLocationSelected().addAll(selectedRegions);
		} else {
			arf.setLocationSelected(null);
			arf.setRelatedLocations(null);
		}
		
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
		
		if (filterForm.getSelectedWorkspaces() != null && filterForm.getSelectedWorkspaces().length > 0)
			arf.setWorkspaces(new HashSet());
		else
			arf.setWorkspaces(null);

		for (int i = 0; filterForm.getSelectedWorkspaces() != null && i < filterForm.getSelectedWorkspaces().length; i++) {
			Long workspaceId = Long.parseLong( filterForm.getSelectedWorkspaces()[i].toString() );
			AmpTeam value 	= (AmpTeam) session.load(AmpTeam.class, workspaceId);
			arf.getWorkspaces().add(value);
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
		} else {
			arf.setTypeOfAssistance(null);
		}
			
		if (filterForm.getSelectedModeOfPayment() != null && filterForm.getSelectedModeOfPayment().length > 0) {
			arf.setModeOfPayment(new HashSet<AmpCategoryValue>());
			for (int i = 0; i < filterForm.getSelectedModeOfPayment().length; i++) {
				Long id = filterForm.getSelectedModeOfPayment()[i];
				AmpCategoryValue value = CategoryManagerUtil.getAmpCategoryValueFromDb(id);
				if (value != null)
					arf.getModeOfPayment().add(value);
			}
		} else {
			arf.setModeOfPayment(null);
		}
		
		if(filterForm.getSelectedProjectImplUnit()!=null && filterForm.getSelectedProjectImplUnit().length>0){
			arf.setProjectImplementingUnits(new HashSet<AmpCategoryValue>());
			for (int i = 0; i < filterForm.getSelectedProjectImplUnit().length; i++) {
				Long id = filterForm.getSelectedProjectImplUnit()[i];
				AmpCategoryValue value = CategoryManagerUtil.getAmpCategoryValueFromDb(id);
				if (value != null)
					arf.getProjectImplementingUnits().add(value);
			}
		}else{
			arf.setProjectImplementingUnits(null);
		}

		if (filterForm.getPageSize() != null) {
			arf.setPageSize(filterForm.getPageSize()); // set page size in the
			// ARF filter
		}

		arf.setRisks(Util.getSelectedObjects(AmpIndicatorRiskRatings.class, filterForm.getSelectedRisks()));

		if ( filterForm.getSelectedActivitySettings() != null && filterForm.getSelectedActivitySettings().length > 0 ) {
			boolean isJointCriteria	= false;
			boolean isGovProcedures	= false;
			for (int i = 0; i < filterForm.getSelectedActivitySettings().length; i++) {
				String element = filterForm.getSelectedActivitySettings()[i] + "";
				if ( ONLY_JOINT_CRITERIA.equals(element) ) {
					arf.setJointCriteria(true);
					isJointCriteria		= true;
				}
				if ( ONLY_GOV_PROCEDURES.equals(element) ) {
					arf.setGovernmentApprovalProcedures(true);
					isGovProcedures		= true;
				}
			}
			if (!isJointCriteria)
				arf.setJointCriteria(null);
			if (!isGovProcedures)
				arf.setGovernmentApprovalProcedures(null);
		}
//		arf.setGovernmentApprovalProcedures(filterForm.getGovernmentApprovalProcedures());
//		arf.setJointCriteria(filterForm.getJointCriteria());

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

		if (filterForm.getSelectedBudgets() != null && filterForm.getSelectedBudgets().length > 0) {
			arf.setBudget(new HashSet<AmpCategoryValue>());
			for (int i = 0; i < filterForm.getSelectedBudgets().length; i++) {
				Long id = filterForm.getSelectedBudgets()[i];
				AmpCategoryValue value = CategoryManagerUtil.getAmpCategoryValueFromDb(id);
				if (value != null)
					arf.getBudget().add(value);
			}
		} else {
			arf.setBudget(null);
		}
		
		if ( filterForm.getSelectedMultiDonor() != null && filterForm.getSelectedMultiDonor().length == 1 ) {
			arf.setMultiDonor( (String) filterForm.getSelectedMultiDonor()[0] );
		}
		else
			arf.setMultiDonor(null);

		arf.setJustSearch(filterForm.getJustSearch()==null?false:filterForm.getJustSearch());
		
		
		arf.setWorkspaceonly(filterForm.getWorkspaceonly()==null?false:filterForm.getWorkspaceonly());
		if(arf.isWorkspaceonly()){
			arf.setAmpTeamsforpledges(arf.getAmpTeams());
		}else{
			arf.setAmpTeamsforpledges(null);
		}
		
		arf.setRenderStartYear((filterForm.getRenderStartYear() != -1) ? filterForm.getRenderStartYear() : 0);
		arf.setRenderEndYear((filterForm.getRenderEndYear() != -1) ? filterForm.getRenderEndYear() : 0);

		DecimalFormat custom = new DecimalFormat();
		DecimalFormatSymbols ds = new DecimalFormatSymbols();
		String decimalSeparator	= !"CUSTOM".equalsIgnoreCase(filterForm.getCustomDecimalSymbol()) ? 
				filterForm.getCustomDecimalSymbol().substring(0, 1) : filterForm.getCustomDecimalSymbolTxt().substring(0, 1);
		ds.setDecimalSeparator(  decimalSeparator.charAt(0) );
		
		arf.setDecimalseparator(decimalSeparator);
		
		
		if (filterForm.getCustomUseGrouping().booleanValue() == true) {
			String groupingSeparator = !"CUSTOM".equalsIgnoreCase(filterForm.getCustomGroupCharacter()) ? 
					filterForm.getCustomGroupCharacter().substring(0, 1) : filterForm.getCustomGroupCharacterTxt().substring(0, 1);
			ds.setGroupingSeparator( groupingSeparator.charAt(0) );			
			arf.setGroupingseparator( groupingSeparator );
			custom.setGroupingUsed(filterForm.getCustomUseGrouping());
			custom.setGroupingSize(filterForm.getCustomGroupSize());	
			
			arf.setGroupingsize(filterForm.getCustomGroupSize());
			arf.setCustomusegroupings(filterForm.getCustomUseGrouping());
		}
		
		//custom.setMaximumFractionDigits((filterForm.getCustomDecimalPlaces() != -1) ? filterForm.getCustomDecimalPlaces() : 99);
		custom.setDecimalFormatSymbols(ds);
		arf.setAmountinthousand(filterForm.getAmountinthousandsint());
		//arf.setAmountinmillion(filterForm.getAmountinmillions()); TODO-Constantin: field not used anymore, to be removed in 2.4
		
		
		Integer maximumDecimalPlaces	= filterForm.getCustomDecimalPlaces();
		if ( maximumDecimalPlaces == -2 ) {//CUSTOM
			arf.setMaximumFractionDigits(filterForm.getCustomDecimalPlacesTxt());
		}
		else if (maximumDecimalPlaces > -1)
			arf.setMaximumFractionDigits(maximumDecimalPlaces );
		else{
			DecimalFormat defaultDecimalFormat = FormatHelper.getDecimalFormat();
			arf.setMaximumFractionDigits(defaultDecimalFormat.getMaximumFractionDigits());
		}
		custom.setMaximumFractionDigits( arf.getMaximumFractionDigits() );
		
		arf.setCurrentFormat(custom);

		arf.setBeneficiaryAgency(ReportsUtil.processSelectedFilters(filterForm.getSelectedBeneficiaryAgency(), AmpOrganisation.class));
		arf.setDonnorgAgency(ReportsUtil.processSelectedFilters(filterForm.getSelectedDonnorAgency(), AmpOrganisation.class));
		arf.setResponsibleorg(ReportsUtil.processSelectedFilters(filterForm.getSelectedresponsibleorg(), AmpOrganisation.class));
		
		arf.setImplementingAgency(ReportsUtil.processSelectedFilters(filterForm.getSelectedImplementingAgency(), AmpOrganisation.class));
		arf.setExecutingAgency(ReportsUtil.processSelectedFilters(filterForm.getSelectedExecutingAgency(), AmpOrganisation.class));
		arf.setProjectCategory(ReportsUtil.processSelectedFilters(filterForm.getSelectedProjectCategory(), AmpCategoryValue.class));
		if ( filterForm.getSelectedArchivedStatus() == null || filterForm.getSelectedArchivedStatus().length != 1 ) {
			arf.setShowArchived(null);
		}
		else {
			String selection 	= (String) filterForm.getSelectedArchivedStatus()[0];
			if ("1".equals(selection) )
				arf.setShowArchived(false);
			else
				arf.setShowArchived(true);
		} 

		if ( filterForm.getSourceIsReportWizard() != null && filterForm.getSourceIsReportWizard() ) {
			httpSession.setAttribute(ReportWizardAction.SESSION_FILTER, arf);
			return mapping.findForward("reportWizard");
		}
		if ( request.getParameter("queryEngine") != null && "true".equals(request.getParameter("queryEngine")) ) {
			httpSession.setAttribute(ArConstants.REPORTS_FILTER, arf);
			return mapping.findForward("queryView");
		}
			
		httpSession.setAttribute(ArConstants.REPORTS_FILTER, arf);
		if (arf.isPublicView()){
			return mapping.findForward(arf.isWidget() ? "publicView" : "reportView");
		}
		
		if (arf.isWidget()){
			return mapping.findForward("mydesktop");
		}else{
			return mapping.findForward("reportView");
		}
	}

	public void reset(ActionForm form, HttpServletRequest request, ActionMapping mapping) {
		request.setAttribute("apply", null);
		ReportsFilterPickerForm filterForm = (ReportsFilterPickerForm) form;
		filterForm.setSelectedRisks(null);
		filterForm.setSelectedSectors(null);
		filterForm.setSelectedStatuses(null);
		filterForm.setSelectedWorkspaces(null);
		filterForm.setSelectedNatPlanObj(null);
		filterForm.setJustSearch(null);
		filterForm.setSelectedPrimaryPrograms(null);
		filterForm.setSelectedSecondarySectors(null);
        filterForm.setSelectedTertiarySectors(null);
        filterForm.setSelectedArchivedStatus(new Object[]{"1"});
		filterForm.setAmountinthousands(false);
		filterForm.setAmountinmillions(false);
		filterForm.setSelectedMultiDonor(null);
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
		filterForm.setComputedYear(-1);
		// Long
		// toYear=Long.parseLong(FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.END_YEAR_DEFAULT_VALUE));
		filterForm.setToYear(-1l);
		filterForm.setFromMonth(-1);
		filterForm.setToMonth(-1);
		filterForm.setFromDate(null);
		filterForm.setToDate(null);
		
		filterForm.setAmpReportId(null);

		filterForm.setLineMinRanks(null);
		filterForm.setPlanMinRanks(null);
		filterForm.setText(null);
		filterForm.setPageSize(null);
		filterForm.setGovernmentApprovalProcedures(null);
		filterForm.setJointCriteria(null);
		filterForm.setRegionSelected(null);
		filterForm.setApprovalStatusSelected(null);
		filterForm.reset(mapping, request);
	}

	public void resetFormat(ActionForm form, HttpServletRequest request, ActionMapping mapping) {
		HttpSession httpSession = request.getSession();
		ReportsFilterPickerForm filterForm = (ReportsFilterPickerForm) form;
		AmpARFilter arf = (AmpARFilter) httpSession.getAttribute(ArConstants.REPORTS_FILTER);
		if (arf != null){
			arf.setCurrentFormat(null);
		}
		filterForm.setDecimalSymbol(null);
		filterForm.setCustomDecimalSymbol(null);
		filterForm.setCustomDecimalPlaces(null);
		filterForm.setCustomGroupCharacter(null);
		filterForm.setCustomUseGrouping(null);
		filterForm.setCustomGroupSize(null);
		filterForm.setResetFormat(null);
		filterForm.setAmountinthousands(false);
		filterForm.setAmountinmillions(null);
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

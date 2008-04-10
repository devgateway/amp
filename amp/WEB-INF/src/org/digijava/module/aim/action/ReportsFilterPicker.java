/**
 * 
 */
package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.hibernate.Session;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.ARUtil;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.utils.MultiAction;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.ar.util.ReportsUtil;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpCategoryValue;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpIndicatorRiskRatings;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.form.ReportsFilterPickerForm;
import org.digijava.module.aim.helper.CategoryManagerUtil;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.MEIndicatorsUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.springframework.beans.BeanWrapperImpl;

/**
 * @author mihai
 *
 */
public class ReportsFilterPicker extends MultiAction {
	final String KEY_RISK_PREFIX="aim:risk:";
	public ActionForward modePrepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		ReportsFilterPickerForm filterForm=(ReportsFilterPickerForm) form;
		String ampReportId=request.getParameter("ampReportId");
		if(ampReportId!=null) filterForm.setAmpReportId(new Long(ampReportId));
		
		HttpSession httpSession = request.getSession();
		TeamMember teamMember = (TeamMember) httpSession
				.getAttribute(Constants.CURRENT_MEMBER);

		Long ampTeamId = null;
		
		if(teamMember!=null) ampTeamId=teamMember.getTeamId();
	
		//create filter dropdowns		
		Collection currency = CurrencyUtil.getAmpCurrency();
		Collection allFisCalenders = DbUtil.getAllFisCalenders();
		List ampSectors;// = SectorUtil.getAmpSectorsAndSubSectors();
		ampSectors = SectorUtil.getAllSectorsFromScheme(FeaturesUtil.getGlobalSettingValueLong(GlobalSettingsConstants.DEFAULT_SECTOR_SCHEME));
		
		TreeSet<AmpSector> alphaOrderedSectors	= new TreeSet<AmpSector>( 
				new Comparator<AmpSector>() {

					public int compare(AmpSector as1, AmpSector as2) {
						if ( as1.getName() != null && as2.getName() != null )
							return as1.getName().compareToIgnoreCase(as2.getName() );
						
						return -1;	
					}
					
				}
				
		); 
		Iterator<AmpSector> sectIter	= (Iterator<AmpSector>)ampSectors.iterator();
		while ( sectIter.hasNext() ) {
			alphaOrderedSectors.add( sectIter.next() );
		}
		
		

		AmpApplicationSettings tempSettings = null;
		if (teamMember!= null)
			tempSettings = DbUtil.getMemberAppSettings(teamMember.getMemberId());
		
		if(tempSettings != null)
		{
			filterForm.setDefaultCurrency(tempSettings.getCurrency().getAmpCurrencyId());  
				
			if (filterForm.getCurrency() == null){
				filterForm.setCurrency(tempSettings.getCurrency().getAmpCurrencyId());  
			}
			if (filterForm.getCalendar() == null){
				filterForm.setCalendar(tempSettings.getFiscalCalendar().getAmpFiscalCalId());
			}
		}
		else
		{
			filterForm.setDefaultCurrency(CurrencyUtil.getCurrencyByCode(Constants.DEFAULT_CURRENCY).getAmpCurrencyId());
		}

		//create the pageSizes Collection for the dropdown	
		Collection pageSizes=new ArrayList();
		
		Collection donors;
		//if(ampTeamId!=null) donors=DbUtil.getAmpDonorsByFunding(ampTeamId); else donors=new ArrayList();
//		donors = DbUtil.getAllOrgGroups();
		donors = DbUtil.getAllOrgGrpBeeingUsed();
		
		Collection donorTypes	= DbUtil.getAllOrgTypes();
		Collection donorGroups	= ARUtil.filterDonorGroups( DbUtil.getAllOrgGroups() );
		
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
		Collection regions=LocationUtil.getAllDefCountryArRegions();
		//Collection regions=LocationUtil.getAllVRegions();
		filterForm.setCurrencies(currency);
		filterForm.setCalendars(allFisCalenders);
		//filterForm.setDonors(donors);
		filterForm.setRisks(allIndicatorRisks);
		filterForm.setSectors( alphaOrderedSectors );
		filterForm.setFromYears(new ArrayList<BeanWrapperImpl>());
		filterForm.setToYears(new ArrayList<BeanWrapperImpl>());
		filterForm.setFromMonths(new ArrayList<BeanWrapperImpl>());
		filterForm.setToMonths(new ArrayList<BeanWrapperImpl>());
		filterForm.setPageSizes(pageSizes);
		filterForm.setRegionSelectedCollection(regions);
		filterForm.setDonorTypes(donorTypes);
		filterForm.setDonorGroups(donorGroups);
		
		filterForm.setExecutingAgency( ReportsUtil.getAllOrgByRole(Constants.ROLE_CODE_EXECUTING_AGENCY) );
		filterForm.setBeneficiaryAgency( ReportsUtil.getAllOrgByRole(Constants.ROLE_CODE_BENEFICIARY_AGENCY) );
		filterForm.setImplementingAgency( ReportsUtil.getAllOrgByRole(Constants.ROLE_CODE_IMPLEMENTING_AGENCY) );
		
		
				// loading Activity Rank collection
		if (null == filterForm.getActRankCollection()) {
			filterForm.setActRankCollection(new ArrayList());
			for (int i = 1; i < 6; i++)
				filterForm.getActRankCollection().add(new BeanWrapperImpl(new Integer(i)));
		}
		
		Long yearFrom=Long.parseLong(FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.YEAR_RANGE_START));
		
		Long countYear=Long.parseLong(FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.NUMBER_OF_YEARS_IN_RANGE));
		
		for (long i=yearFrom; i <= (yearFrom+countYear);i++){
		    filterForm.getFromYears().add(new BeanWrapperImpl(new Long(i)));
		    filterForm.getToYears().add(new BeanWrapperImpl(new Long(i)));	   
		}
		
		if (filterForm.getFromYear()==null){
		    Long fromYear=Long.parseLong(FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.START_YEAR_DEFAULT_VALUE));
    			filterForm.setFromYear(fromYear);
		}
		
		if (filterForm.getToYear()==null){
		    Long toYear=Long.parseLong(FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.END_YEAR_DEFAULT_VALUE));
		    filterForm.setToYear(toYear);
		}
		
		for (int i=1; i<=12; i++) {
			filterForm.getFromMonths().add(new BeanWrapperImpl(new Integer(i)));
			filterForm.getToMonths().add(new BeanWrapperImpl(new Integer(i)));
		}
		
		if (filterForm.getFromMonth()==null)
			filterForm.setFromMonth(-1);
		
		if (filterForm.getToMonth()==null)
			filterForm.setToMonth(-1);
		/*
		for (int i = (2010 - Constants.FROM_YEAR_RANGE); i <= (2010 + Constants.TO_YEAR_RANGE); i++) {
			filterForm.getFromYears().add(new BeanWrapperImpl(new Long(i)));
			filterForm.getToYears().add(new BeanWrapperImpl(new Long(i)));		
		}
		*/
	
		filterForm.getPageSizes().add(new BeanWrapperImpl(new String("A0")));
		filterForm.getPageSizes().add(new BeanWrapperImpl(new String("A1")));
		filterForm.getPageSizes().add(new BeanWrapperImpl(new String("A2")));
		filterForm.getPageSizes().add(new BeanWrapperImpl(new String("A3")));
		filterForm.getPageSizes().add(new BeanWrapperImpl(new String("A4")));
		
		if (ampReportId != null){
			
			AmpReports rep = (AmpReports) DbUtil.getAmpReports(new Long(ampReportId));
			
			httpSession.setAttribute("filterCurrentReport", rep);
		}
		
		return modeSelect(mapping,form,request,response);
	}

	public ActionForward modeReset(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ReportsFilterPickerForm filterForm=(ReportsFilterPickerForm) form;
		request.setAttribute("reset","reset");
		//filterForm.setSelectedDonors(null);
		filterForm.setSelectedRisks(null);
		filterForm.setSelectedSectors(null);
		filterForm.setSelectedStatuses(null);
		HttpSession httpSession = request.getSession();
		TeamMember teamMember = (TeamMember) httpSession .getAttribute(Constants.CURRENT_MEMBER);

		if (teamMember!= null)
		{
			AmpApplicationSettings tempSettings = DbUtil.getMemberAppSettings(teamMember.getMemberId());
			filterForm.setCurrency(tempSettings.getCurrency().getAmpCurrencyId());  
			String name = "- " + tempSettings.getCurrency().getCurrencyName();
			httpSession.setAttribute(ArConstants.SELECTED_CURRENCY, name);
			filterForm.setCalendar(tempSettings.getFiscalCalendar().getAmpFiscalCalId());
		}
		
		Long fromYear=Long.parseLong(FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.START_YEAR_DEFAULT_VALUE));
		filterForm.setFromYear(fromYear);

		Long toYear=Long.parseLong(FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.END_YEAR_DEFAULT_VALUE));
		filterForm.setToYear(toYear);
		filterForm.setFromMonth(-1);
		filterForm.setToMonth(-1);
		
		filterForm.setLineMinRank(null);
		filterForm.setPlanMinRank(null);
		filterForm.setText(null);
		filterForm.setPageSize(null);
		filterForm.setGovernmentApprovalProcedures(null);
		filterForm.setJointCriteria(null);
		filterForm.setRegionSelected(null);
		//filterForm.setRegions(null);
		filterForm.setDonorGroups(null);
		filterForm.setDonorTypes(null);
		filterForm.setExecutingAgency(null);
		filterForm.setBeneficiaryAgency(null);
		filterForm.setImplementingAgency(null);
	
		return modeApply(mapping,form,request,response);
	}
	
	public ActionForward modeSelect(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(request.getParameter("apply")!=null && request.getAttribute("apply")==null) return modeApply(mapping,form,request,response);
		if(request.getParameter("reset")!=null && request.getAttribute("reset")==null) return modeReset(mapping,form,request,response);
		
		HttpSession httpSession = request.getSession();
		
		if (httpSession.getAttribute(ArConstants.SELECTED_CURRENCY) == null){
			TeamMember teamMember = (TeamMember) httpSession  .getAttribute(Constants.CURRENT_MEMBER);
			if(teamMember != null){
				AmpApplicationSettings tempSettings = DbUtil.getMemberAppSettings(teamMember.getMemberId());
				String name = "- " + tempSettings.getCurrency().getCurrencyName();
				httpSession.setAttribute(ArConstants.SELECTED_CURRENCY, name);
			}
		}
		return mapping.findForward("forward");
	}
	
	/**
	 * generate a session based AmpARFilter object based on the form selections
	 * @param mapping
	 * @param form
	 * @param requestTeamMember teamMember = (TeamMember) httpSession  .getAttribute(Constants.CURRENT_MEMBER);
			AmpApplicationSettings tempSettings = DbUtil.getMemberAppSettings(teamMember.getMemberId());
			
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward modeApply(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ReportsFilterPickerForm filterForm=(ReportsFilterPickerForm) form;
		HttpSession httpSession = request.getSession();
		
		Session session = PersistenceManager.getSession();
		
		request.setAttribute("apply","apply");
		AmpARFilter arf = (AmpARFilter) httpSession.getAttribute(ArConstants.REPORTS_FILTER);
		if(arf==null) arf=new AmpARFilter();
		arf.readRequestData(request);
		
		//for each sector we have also to add the subsectors
		
		Set selectedSectors		= Util.getSelectedObjects(AmpSector.class,filterForm.getSelectedSectors());
		Set generatedSectors	= new HashSet();
		
		generatedSectors.addAll( selectedSectors);
		
		
		arf.setSelectedSectors( new HashSet() );
		arf.getSelectedSectors().addAll(selectedSectors);
		
		if(selectedSectors!=null)
		{
			Iterator sectorIterator=selectedSectors.iterator();
		
			while(sectorIterator.hasNext())
			{//process each sector and get all its children
				AmpSector currentSector=(AmpSector)sectorIterator.next();
				if(currentSector!=null)
				{
					Collection childSectors=SectorUtil.getAllChildSectors(currentSector.getAmpSectorId());
					generatedSectors.addAll(childSectors); //add the children sectors to the filter
					
					//add the grand children
					Iterator childSectorsIterator=childSectors.iterator();
					while(childSectorsIterator.hasNext())
					{
						AmpSector currentChild=(AmpSector)childSectorsIterator.next();
						Collection grandChildrenSectors=SectorUtil.getAllChildSectors(currentChild.getAmpSectorId());
						generatedSectors.addAll(grandChildrenSectors);
					}
					
				}
			
			}
		}
		arf.setSectors( generatedSectors );
		
				
		
		AmpFiscalCalendar selcal=(AmpFiscalCalendar) Util.getSelectedObject(AmpFiscalCalendar.class,filterForm.getCalendar());
		arf.setCalendarType(selcal);
		if(filterForm.getText()!=null){
			arf.setText(filterForm.getText());			
		}
		
		if (filterForm.getIndexString() != null){
			arf.setIndexText(filterForm.getIndexString());
		}
		
		arf.setFromYear(filterForm.getFromYear()==null || filterForm.getFromYear().longValue()==-1?null:new Integer(filterForm.getFromYear().intValue()));
		arf.setToYear(filterForm.getToYear()==null || filterForm.getToYear().longValue()==-1?null:new Integer(filterForm.getToYear().intValue()));
		arf.setFromMonth(filterForm.getFromMonth()==null || filterForm.getFromMonth().intValue()==-1?null:new Integer(filterForm.getFromMonth().intValue()));
		arf.setToMonth(filterForm.getToMonth()==null || filterForm.getToMonth().intValue()==-1?null:new Integer(filterForm.getToMonth().intValue()));
		
		//arf.setDonors(Util.getSelectedObjects(AmpOrgGroup.class,filterForm.getSelectedDonors()));
		AmpCurrency currency = (AmpCurrency) Util.getSelectedObject(AmpCurrency.class,filterForm.getCurrency());
		arf.setCurrency(currency);
		String name = "- " + currency.getCurrencyName();
		httpSession.setAttribute(ArConstants.SELECTED_CURRENCY, name);
		Integer all=new Integer(-1);
		if(!all.equals(filterForm.getLineMinRank())) arf.setLineMinRank(filterForm.getLineMinRank());
		if(!all.equals(filterForm.getPlanMinRank())) arf.setPlanMinRank(filterForm.getPlanMinRank());
		if(!all.equals(filterForm.getRegionSelected())) arf.setRegionSelected(filterForm.getRegionSelected()==null ||filterForm.getRegionSelected()==-1?null:filterForm.getRegionSelected() );
		
		if(filterForm.getSelectedStatuses()!=null && filterForm.getSelectedStatuses().length>0)
		arf.setStatuses(new HashSet()); else arf.setStatuses(null);
	
		for (int i = 0;filterForm.getSelectedStatuses()!=null && i < filterForm.getSelectedStatuses().length; i++) {
			AmpCategoryValue value = (AmpCategoryValue) session.load(AmpCategoryValue.class,new Long((String) filterForm.getSelectedStatuses()[i]));
			arf.getStatuses().add(value);
		}
		
		if(filterForm.getSelectedFinancingInstruments()!=null && filterForm.getSelectedFinancingInstruments().length>0)
				arf.setFinancingInstruments(new HashSet()); 
		else 
			arf.setFinancingInstruments(null);
		
		for (int i = 0; filterForm.getSelectedFinancingInstruments()!=null && i < filterForm.getSelectedFinancingInstruments().length; i++) {
			Long id					= Long.parseLong(filterForm.getSelectedFinancingInstruments()[i]+"");
			AmpCategoryValue value	= (AmpCategoryValue) CategoryManagerUtil.getAmpCategoryValueFromDb(id);
			arf.getFinancingInstruments().add( value );
		}
		
		
	    if(filterForm.getPageSize()!=null)
	    {
	    	arf.setPageSize(filterForm.getPageSize()); //set page size in the ARF filter
	    }
		
		arf.setRisks(Util.getSelectedObjects(AmpIndicatorRiskRatings.class,filterForm.getSelectedRisks()));
		
		arf.setGovernmentApprovalProcedures(filterForm.getGovernmentApprovalProcedures());
		arf.setJointCriteria(filterForm.getJointCriteria());
		
		if ( filterForm.getSelectedDonorTypes() != null && filterForm.getSelectedDonorTypes().length > 0 ) {
			arf.setDonorTypes(new HashSet());
			for (int i=0; i<filterForm.getSelectedDonorTypes().length ; i++) {
				Long id			= Long.parseLong( ""+filterForm.getSelectedDonorTypes()[i] );
				AmpOrgType type	= DbUtil.getAmpOrgType( id );
				if ( type != null )
					arf.getDonorTypes().add( type );
			}
		}
		else 
			arf.setDonorTypes(null);
		
		if ( filterForm.getSelectedDonorGroups() != null && filterForm.getSelectedDonorGroups().length > 0 ) {
			arf.setDonorGroups(new HashSet() );
			for (int i=0; i<filterForm.getSelectedDonorGroups().length; i++) {
				Long id 		= Long.parseLong (""+filterForm.getSelectedDonorGroups()[i]);
				AmpOrgGroup	grp	= DbUtil.getAmpOrgGroup(id);
				if (grp != null)
					arf.getDonorGroups().add( grp );
			}
		}
		else
			arf.setDonorGroups(null);
		
		if (filterForm.getSelectedBudget() != null) {
			switch (filterForm.getSelectedBudget().intValue()) {
				case 0: break;
				case 1: arf.setBudget(true); break;
				case 2: arf.setBudget(false); break;
				
			}
		}
		
		arf.setBeneficiaryAgency( ReportsUtil.processSelectedFilters( filterForm.getSelectedBeneficiaryAgency() , AmpOrganisation.class ) );
		arf.setImplementingAgency( ReportsUtil.processSelectedFilters( filterForm.getSelectedImplementingAgency() , AmpOrganisation.class ) );
		arf.setExecutingAgency( ReportsUtil.processSelectedFilters( filterForm.getSelectedExecutingAgency(), AmpOrganisation.class ) );
		
		httpSession.setAttribute(ArConstants.REPORTS_FILTER,arf);

		return mapping.findForward(arf.isWidget()?"mydesktop":"reportView");
	}
	 
	
}


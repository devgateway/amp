package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.Util;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FundingOrganization;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.KeyValue;
import org.digijava.module.aim.helper.MTEFProjection;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.fundingpledges.dbentity.PledgesEntityHelper;
import org.digijava.module.gateperm.core.GatePermConst;

public class AddFunding extends Action {

	private static Logger logger = Logger.getLogger(AddFunding.class);

	public ActionForward execute(ActionMapping mapping,
								ActionForm form,
								javax.servlet.http.HttpServletRequest request,
								javax.servlet.http.HttpServletResponse
								response) throws java.lang.Exception {

		EditActivityForm formBean = (EditActivityForm) form;
		//this is needed to aknowledge that we are still under EDIT ACTIVITY mode:
        request.setAttribute(GatePermConst.ACTION_MODE, GatePermConst.Actions.EDIT);
		formBean.setReset(false);
		Long orgId = formBean.getFunding().getOrgId();
		 if ( logger.isDebugEnabled() )
		        logger.debug("< orgId=" + orgId);

		formBean.getFunding().setAssistanceType(null);
		formBean.getFunding().setOrgFundingId("");
		formBean.getFunding().setSignatureDate("");
		formBean.getFunding().setReportingDate("");
		formBean.getFunding().setPlannedCompletionDate("");
		formBean.getFunding().setPlannedStartDate("");
		formBean.getFunding().setActualCompletionDate("");
		formBean.getFunding().setActualStartDate("");
		formBean.getFunding().setFundingConditions("");
		formBean.getFunding().setEditFunding(false);
		formBean.getFunding().setFundingDetails(null);
		formBean.getFunding().setNumComm(0);
		formBean.getFunding().setNumDisb(0);
		formBean.getFunding().setNumExp(0);
        formBean.getFunding().setNumDisb(0);
        formBean.getFunding().setFundingMTEFProjections( new ArrayList<MTEFProjection>() );
        formBean.getFunding().setModality(null);
        formBean.getFunding().setDonorObjective(null);
        formBean.getFunding().setFundingStatus(null);
        formBean.getFunding().setModeOfPayment(null);

        boolean afterFiscalYearStart	= isAfterFiscalYearStart(null);
        MTEFProjection me	= null;
        Integer year				= null;
		for (int i=0; i<3; i++) {
			if ( i == 0 )  {
					me	= getMTEFProjection(request.getSession(), 0, afterFiscalYearStart, null );
					try {
						year	= Integer.parseInt( me.getProjectionDate().split("/")[2] );
					}
					catch (Exception e) {
						logger.error(e);
						break;
					}
			}
			else
				me		= getMTEFProjection(request.getSession(), i, false, year + i);
				
			formBean.getFunding().getFundingMTEFProjections().add( me );
		}
		List<KeyValue> availableMTEFProjectionYears		= 
			AddFunding.generateAvailableMTEFProjectionYears( formBean.getFunding().getFundingMTEFProjections() ); 
		formBean.getFunding().setAvailableMTEFProjectionYears( availableMTEFProjectionYears );
		
		int defaultIndex				= availableMTEFProjectionYears.size() - 1 - AddMTEFProjection.ADDITIONAL_AVAILABLE_YEARS;
		formBean.getFunding().setSelectedMTEFProjectionYear( Integer.parseInt( 
				availableMTEFProjectionYears.get(defaultIndex).getKey() )
		);
		
		formBean.getFunding().setNumProjections(formBean.getFunding().getFundingMTEFProjections().size());

		Collection<FundingOrganization> fundingOrganizations = formBean.getFunding().getFundingOrganizations();
		Iterator<FundingOrganization> iter = fundingOrganizations.iterator();
		while ( iter.hasNext() )	{
			FundingOrganization fundingOrganization = (FundingOrganization)iter.next();
			if ( orgId.equals( fundingOrganization.getAmpOrgId() ) )	{
				formBean.getFunding().setOrgName(fundingOrganization.getOrgName());
				formBean.getFunding().setOrgFundingId(fundingOrganization.getFundingorgid());
				break;
			}
		}
		/*Collection<AmpCategoryValue> c 	= CategoryManagerUtil.getAmpCategoryValueCollectionByKey(
								CategoryConstants.TYPE_OF_ASSISTENCE_KEY, null);
		if (c != null) {
			Iterator<AmpCategoryValue> tempItr = c.iterator();
			while (tempItr.hasNext()) {
				AmpCategoryValue assistCategoryValue = (AmpCategoryValue) tempItr.next();
				if (assistCategoryValue.getValue().equalsIgnoreCase("Grant")) {
					formBean.getFunding().setAssistanceType(assistCategoryValue.getId());
					break;
				}
			}
		}*/
		try {
			AmpCategoryValue grantCV	= CategoryManagerUtil.getAmpCategoryValueFromDB( CategoryConstants.TYPE_OF_ASSITANCE_GRANT );
			formBean.getFunding().setAssistanceType( grantCV.getId() );
		}
		catch (Exception e) {
			//e.printStackTrace();
			logger.warn("Grant value was not found in database. This could be an error. Make  sure it is not needed: " + e.getMessage());
		}
		formBean.getFunding().setOrganizations(DbUtil.getAllOrganisation());
		formBean.getFunding().setEvent(null);
		formBean.getFunding().setDupFunding(true);
		formBean.getFunding().setFirstSubmit(false);
		formBean.setTotDisbIsBiggerThanTotCom(false);
		 // load donor related pledges
		formBean.getFunding().setPledgeslist(PledgesEntityHelper.getPledgesByDonor(orgId));
		
		return mapping.findForward("forward");
	}
	public static String getFYDate(Integer numOfAddedYears, Integer year) {

		if ( year == null ) {
			String yearGS			= FeaturesUtil.getGlobalSettingValue( GlobalSettingsConstants.CURRENT_SYSTEM_YEAR );
			
			if ( yearGS.equals(GlobalSettingsConstants.SYSTEM_YEAR) )
				year		= Util.getSystemYear();
			else
				year		= Integer.parseInt( yearGS );
		}
		year			+= numOfAddedYears;
		// String date		= FeaturesUtil.getGlobalSettingValue( GlobalSettingsConstants.FISCAL_YEAR_END_DATE ) + "/" + year;
		String date;
		try {
			String fiscalCalendarId		= FeaturesUtil.getGlobalSettingValue( GlobalSettingsConstants.DEFAULT_CALENDAR );
			AmpFiscalCalendar fiscalCal	= DbUtil.getAmpFiscalCalendar( Long.parseLong(fiscalCalendarId) );
		
			date						= fiscalCal.getStartDayNum() + "/" + fiscalCal.getStartMonthNum() + "/" + year;
		}
		catch (Exception E) {
			E.printStackTrace();
			logger.error("Information about fiscal year start NOT retrievable. Using 01 January.");
			date						= "01/01/" + year; 
		}
		return date;

}

	public static boolean isAfterFiscalYearStart(String fyDate) {
		if ( fyDate == null )
			fyDate				= getFYDate(0, null);
		String [] dateElements	= fyDate.split("/");
		GregorianCalendar gcFY	= new GregorianCalendar(
										Integer.parseInt(dateElements[2]),
										Integer.parseInt(dateElements[1]) - 1,
										Integer.parseInt(dateElements[0])
										);
		GregorianCalendar gcCur	= new GregorianCalendar();
		gcCur.set(Calendar.YEAR, gcFY.get(Calendar.YEAR));

		return gcCur.after(gcFY);
	}
	public static MTEFProjection getMTEFProjection(HttpSession session, int index, boolean afterFiscalYearStart, Integer year) {
		String currCode = Constants.DEFAULT_CURRENCY;
		if ( session != null ) {
			TeamMember teamMember = (TeamMember) session.getAttribute("currentMember");
			if (teamMember.getAppSettings() != null) {
				ApplicationSettings appSettings = teamMember.getAppSettings();
				if (appSettings.getCurrencyId() != null) {
					currCode = CurrencyUtil.getCurrency(appSettings.getCurrencyId()).getCurrencyCode();
				}
			}
		}

		int offset;
		if (afterFiscalYearStart)
			offset	= 1;
		else
			offset 	= 0;

		MTEFProjection mp 		= new MTEFProjection();
		mp.setCurrencyCode(currCode);
		mp.setProjectionDate( getFYDate(offset, year) );
		mp.setIndex( index );
		return mp;
	}
	public static List<KeyValue> generateAvailableMTEFProjectionYears(List<MTEFProjection> projections) {
		ArrayList<KeyValue> list		= new ArrayList<KeyValue>();
		int maxYear							= 0;
		int firstYear							= AddFunding.getMTEFProjection(null, 0, AddFunding.isAfterFiscalYearStart(null), null).getBaseYear();
		if ( projections == null || projections.size() == 0 ) {
			list.add( keyValueFromYear(firstYear) );
			maxYear		= firstYear;
		}
		else {
			int tempYear		= projections.get(0).getBaseYear();
			maxYear			= tempYear;
			
			for ( int i=0; (firstYear+i)<tempYear; i++ ) {
				list.add( keyValueFromYear(firstYear+i) );
			}
			
			Iterator<MTEFProjection> iter 	= projections.iterator();
			iter.next(); // we skip the first element in the list
			while ( iter.hasNext() ) {
				MTEFProjection nextProjection		= iter.next();
				
				for ( int i=1; i < nextProjection.getBaseYear()-tempYear; i++ ) {
					list.add( keyValueFromYear(tempYear+i) );
				}
				maxYear	= tempYear					= nextProjection.getBaseYear();
			}
			list.add( keyValueFromYear(++maxYear) );
		}
		for (int i=1; i<=2; i++) 
			list.add( keyValueFromYear(maxYear+i) );
		return list;
	}
	public static KeyValue keyValueFromYear(int year) {
			String value	= year + "/" + (year+1);
			String key		= year+"";
			return new KeyValue(key, value);
	}
}

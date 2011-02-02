package org.digijava.module.visualization.action;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.dgfoundation.amp.Util;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DecimalWraper;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesDetails;
import org.digijava.module.orgProfile.util.OrgProfileUtil;
import org.digijava.module.visualization.util.DbUtil;
import org.digijava.module.visualization.util.DashboardUtil;
import org.digijava.module.visualization.form.VisualizationForm;
import org.digijava.module.visualization.helper.DashboardFilter;
import org.digijava.module.widget.helper.ChartOption;
import org.digijava.module.widget.util.ChartWidgetUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public class DataDispatcher extends DispatchAction {
	private static Logger logger = Logger.getLogger(DbUtil.class);
	
	//This class will return the XMLs or CSVs needed for each kind of graph
	//It will use an instance of VisualizationFilter to handle all filters
	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {
		return null;
	}

	public ActionForward applyFilter(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		VisualizationForm visualizationForm = (VisualizationForm)form;
		ArrayList<AmpOrganisation> orgs = new ArrayList<AmpOrganisation>();
		for (int i = 0; i < visualizationForm.getFilter().getOrgIds().length; i++) {
			//We need to have an empty collection in the organizationsSelected list so the query goes through Organization Group Id
			if(Long.valueOf(visualizationForm.getFilter().getOrgIds()[i]) != -1){
				orgs.add(DbUtil.getOrganisation(Long.valueOf(visualizationForm.getFilter().getOrgIds()[i])));
			}
		}
		visualizationForm.getFilter().setOrganizationsSelected(orgs);

		ArrayList<AmpSector> secs = new ArrayList<AmpSector>();
		for (int i = 0; i < visualizationForm.getFilter().getSectorIds().length; i++) {
			//We need to have an empty collection in the organizationsSelected list so the query goes through Organization Group Id
			if(Long.valueOf(visualizationForm.getFilter().getSectorIds()[i]) != -1){
				orgs.add(DbUtil.getOrganisation(Long.valueOf(visualizationForm.getFilter().getSectorIds()[i])));
			}
		}
		visualizationForm.getFilter().setSectorsSelected(secs);

		return null;
	}
	public ActionForward getThirdGraphData(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws java.lang.Exception {

		VisualizationForm visualizationForm = (VisualizationForm) form;

		DashboardFilter filter = visualizationForm.getFilter();
		
		String format = request.getParameter("format");
		Long selectedYear = request.getParameter("year") != null ? Long.parseLong(request.getParameter("year")) : null;


		boolean typeOfAid = false;

        DefaultCategoryDataset result = new DefaultCategoryDataset();
        double divideByDenominator;

        if (filter.getDivideThousands()) {
        	divideByDenominator=1000000000;
        }
        else
        {
        	divideByDenominator=1000000;
        }
        

        if ("true".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS))) {
            if (filter.getDivideThousands()) {
            	divideByDenominator=1000000;
            }
            else
            {
            	divideByDenominator=1000;
            }
        }
        Long year = filter.getYear();
        if (year == null || year == -1) {
            year = Long.parseLong(FeaturesUtil.getGlobalSettingValue("Current Fiscal Year"));
        }
        Long fiscalCalendarId = filter.getFiscalCalendarId();
        Collection<AmpCategoryValue> categoryValues = null;
        if (typeOfAid) {
            categoryValues = CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.TYPE_OF_ASSISTENCE_KEY);
        } else {
            categoryValues = CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.FINANCING_INSTRUMENT_KEY);
        }
        int yearsInRange=filter.getYearsInRange()-1;

        if(selectedYear != null){
        	year = selectedYear;
        	yearsInRange = 0;
        }
        
		if(format != null && format.equals("xml")){
			StringBuffer xmlString = new StringBuffer();
			//Loop funding types
			Iterator<AmpCategoryValue> it = categoryValues.iterator();
			while (it.hasNext()){
				AmpCategoryValue value = it.next();
				xmlString.append("<aidtype name=\"" + value.getValue() + "\">\n");
				for (int i = year.intValue() - yearsInRange; i <= year.intValue(); i++) {
					Date startDate = DashboardUtil.getStartDate(fiscalCalendarId, i);
					Date endDate = DashboardUtil.getEndDate(fiscalCalendarId, i);
	                DecimalWraper funding = null;
	                if (typeOfAid) {
	                    funding = DbUtil.getFunding(filter, startDate, endDate, value.getId(), null, filter.getTransactionType(),Constants.ACTUAL);
	                } else {
	                    funding = DbUtil.getFunding(filter, startDate, endDate, null, value.getId(), filter.getTransactionType(),Constants.ACTUAL);
	                }
					xmlString.append("<year category=\"" + i + "\" amount=\""+ funding.doubleValue()/divideByDenominator + "\"/>\n");
				}
				xmlString.append("</aidtype>\n");
			}
			
			
			PrintWriter out = new PrintWriter(new OutputStreamWriter(
					response.getOutputStream(), "UTF-8"), true);
			out.println(xmlString.toString());
			out.close();
			return null;
		}
        
        
        
        StringBuffer csvString = new StringBuffer();
		csvString.append("Year");
		csvString.append(",");
		Iterator<AmpCategoryValue> it = categoryValues.iterator();
		while (it.hasNext()){
			AmpCategoryValue value = it.next();
//            String title = TranslatorWorker.translateText(categoryValue.getValue(), opt.getLangCode(), opt.getSiteId());
			csvString.append(value.getValue());
			if(it.hasNext()) 
				csvString.append(",");
			else
				csvString.append("\n");
		}
        for (Long i = year - yearsInRange; i <= year; i++) {
    		csvString.append(i);
    		csvString.append(",");
    		it = categoryValues.iterator();
    		while (it.hasNext()){
    			AmpCategoryValue value = it.next();
                // apply calendar filter
                Date startDate = OrgProfileUtil.getStartDate(fiscalCalendarId, i.intValue());
                Date endDate = OrgProfileUtil.getEndDate(fiscalCalendarId, i.intValue());
                DecimalWraper funding = null;
                if (typeOfAid) {
                    funding = DbUtil.getFunding(filter, startDate, endDate, value.getId(), null, filter.getTransactionType(),Constants.ACTUAL);
                } else {
                    funding = DbUtil.getFunding(filter, startDate, endDate, null, value.getId(), filter.getTransactionType(),Constants.ACTUAL);
                }
        		csvString.append(funding.doubleValue()/divideByDenominator);
    			if(it.hasNext()) 
    				csvString.append(",");
    			else
    				csvString.append("\n");
    		}
        }
		
		PrintWriter out = new PrintWriter(new OutputStreamWriter(
    			response.getOutputStream(), "UTF-8"), true);

    	out.println(csvString.toString());

    	out.close();

    	return null;
		}
        
	
	public ActionForward getSecondGraphData(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws java.lang.Exception {

		VisualizationForm visualizationForm = (VisualizationForm) form;

		DashboardFilter filter = visualizationForm.getFilter();
		
		String format = request.getParameter("format");
		
		boolean nodata = true; // for displaying no data message

		Long year = filter.getYear();
		BigDecimal divideByDenominator;

        if (filter.getDivideThousands())
        	divideByDenominator=new BigDecimal(1000000000);
        else
        	divideByDenominator=new BigDecimal(1000000);
        

        if ("true".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS))) {
            if (filter.getDivideThousands())
            	divideByDenominator=new BigDecimal(1000000);
            else
            	divideByDenominator=new BigDecimal(1000);
        }


        if (year == null || year == -1) {
            year = Long.parseLong(FeaturesUtil.getGlobalSettingValue("Current Fiscal Year"));
        }

		Long currId = filter.getCurrencyId();
		String currCode;
		if (currId == null) {
			currCode = "USD";
			AmpCurrency currency = CurrencyUtil.getCurrencyByCode(currCode);
			filter.setCurrencyId(currency.getAmpCurrencyId());
			
		} else {
			currCode = CurrencyUtil.getCurrency(currId).getCurrencyCode();
		}
		int yearsInRange = filter.getYearsInRange() - 1;
		Long fiscalCalendarId = filter.getFiscalCalendarId();

		String plannedTitle = "Planned";
        String actualTitle = "Actual";

		if(format != null && format.equals("xml")){
			StringBuffer xmlString = new StringBuffer();
			//Loop funding types
			
			for (int i = year.intValue() - yearsInRange; i <= year.intValue(); i++) {
				xmlString.append("<year name=\"" + i + "\">\n");
				Date startDate = DashboardUtil.getStartDate(fiscalCalendarId, i);
				Date endDate = DashboardUtil.getEndDate(fiscalCalendarId, i);
	            DecimalWraper fundingPlanned = DbUtil.getFunding(filter, startDate, endDate,null,null,Constants.COMMITMENT, Constants.PLANNED);
				xmlString.append("<fundingtype category=\"Planned\" amount=\""+ fundingPlanned.getValue().divide(divideByDenominator) + "\"/>\n");
	            DecimalWraper fundingActual = DbUtil.getFunding(filter, startDate, endDate,null,null,Constants.COMMITMENT, Constants.ACTUAL);
				xmlString.append("<fundingtype category=\"Actual\" amount=\""+ fundingActual.getValue().divide(divideByDenominator) + "\"/>\n");
				xmlString.append("</year>\n");
				
			}
			
			PrintWriter out = new PrintWriter(new OutputStreamWriter(
					response.getOutputStream(), "UTF-8"), true);

			out.println(xmlString.toString());

			out.close();

			return null;
			
			
		}
        
        
        StringBuffer csvString = new StringBuffer();
		csvString.append("Year");
		csvString.append(",");
		csvString.append(plannedTitle);
		csvString.append(",");
		csvString.append(actualTitle);
		csvString.append("\n");

        for (int i = year.intValue() - yearsInRange; i <= year.intValue(); i++) {
            // apply calendar filter
			csvString.append(i);
			csvString.append(",");
            Date startDate = OrgProfileUtil.getStartDate(fiscalCalendarId, i);
            Date endDate = OrgProfileUtil.getEndDate(fiscalCalendarId, i);
            DecimalWraper fundingActual = DbUtil.getFunding(filter, startDate, endDate,null,null,Constants.COMMITMENT, Constants.ACTUAL);
			csvString.append(fundingActual.getValue().divide(divideByDenominator));
			csvString.append(",");
            DecimalWraper fundingPlanned = DbUtil.getFunding(filter, startDate, endDate,null,null,Constants.COMMITMENT, Constants.PLANNED);
			csvString.append(fundingPlanned.getValue().divide(divideByDenominator));
            if (fundingPlanned.doubleValue() != 0 || fundingActual.doubleValue() != 0) {
				nodata = false;
			}
			csvString.append("\n");
		}
		if (nodata) {
//			result = new DefaultCategoryDataset();
		}
		PrintWriter out = new PrintWriter(new OutputStreamWriter(
				response.getOutputStream(), "UTF-8"), true);

		out.println(csvString.toString());

		out.close();

		return null;
	}
	
	public ActionForward getFirstGraphData(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws java.lang.Exception {

		VisualizationForm visualizationForm = (VisualizationForm) form;

		DashboardFilter filter = visualizationForm.getFilter();
		
		String format = request.getParameter("format");

		Long year = filter.getYear();
		BigDecimal divideByDenominator;

		if (filter.getDivideThousands())
			divideByDenominator = new BigDecimal(1000000000);
		else
			divideByDenominator = new BigDecimal(1000000);

		if ("true"
				.equals(FeaturesUtil
						.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS))) {
			if (filter.getDivideThousands())
				divideByDenominator = new BigDecimal(1000000);
			else
				divideByDenominator = new BigDecimal(1000);
		}

		if (year == null || year == -1) {
			year = Long.parseLong(FeaturesUtil
					.getGlobalSettingValue("Current Fiscal Year"));
		}

		Long currId = filter.getCurrencyId();
		String currCode;
		if (currId == null) {
			currCode = "USD";
			AmpCurrency currency = CurrencyUtil.getCurrencyByCode(currCode);
			filter.setCurrencyId(currency.getAmpCurrencyId());
			
		} else {
			currCode = CurrencyUtil.getCurrency(currId).getCurrencyCode();
		}
		int yearsInRange = filter.getYearsInRange() - 1;
		Long fiscalCalendarId = filter.getFiscalCalendarId();
		String pledgesTranslatedTitle = "Pledges";
		String actComTranslatedTitle = "Actual commitments";
		String actDisbTranslatedTitle = "Actual disbursements";
		String actExpTranslatedTitle = "Actual expenditures";
		// String pledgesTranslatedTitle =
		// TranslatorWorker.translateText("Pledges", opt.getLangCode(),
		// opt.getSiteId());
		// String actComTranslatedTitle =
		// TranslatorWorker.translateText("Actual commitments",
		// opt.getLangCode(), opt.getSiteId());
		// String actDisbTranslatedTitle =
		// TranslatorWorker.translateText("Actual disbursements",
		// opt.getLangCode(), opt.getSiteId());
		// String actExpTranslatedTitle =
		// TranslatorWorker.translateText("Actual expenditures",
		// opt.getLangCode(), opt.getSiteId());

		Double totalPledges;
		BigDecimal totalCommitments, totalDisbursements, totalExpenditures;

		totalPledges = 0.0;
		totalCommitments = totalDisbursements = totalExpenditures = new BigDecimal(
				0);

		StringBuffer csvString = new StringBuffer();
		csvString.append("Year");
		csvString.append(",");
		csvString.append(actComTranslatedTitle);
		csvString.append(",");
		csvString.append(actDisbTranslatedTitle);
		if (filter.isExpendituresVisible()) {
			csvString.append(",");
			csvString.append(actExpTranslatedTitle);
		}
		if (filter.isPledgeVisible()) {
			csvString.append(",");
			csvString.append(pledgesTranslatedTitle);
		}
		csvString.append("\n");

		
		
		//TODO: Change this to use XML DOM Object
		if(format != null && format.equals("xml")){
			StringBuffer xmlString = new StringBuffer();
			//Loop funding types
			
			xmlString.append("<fundingtype name=\"Commitments\">\n");
			for (int i = year.intValue() - yearsInRange; i <= year.intValue(); i++) {
				Date startDate = DashboardUtil.getStartDate(fiscalCalendarId, i);
				Date endDate = DashboardUtil.getEndDate(fiscalCalendarId, i);
				DecimalWraper fundingComm = DbUtil
				.getFunding(filter, startDate, endDate, null, null,
						Constants.COMMITMENT, Constants.ACTUAL);
				xmlString
				.append("<year category=\"" + i + "\" amount=\""+ fundingComm.getValue().divide(divideByDenominator) + "\"/>\n");
				
			}
			xmlString.append("</fundingtype>\n");
			xmlString.append("<fundingtype name=\"Disbursements\">\n");
			for (int i = year.intValue() - yearsInRange; i <= year.intValue(); i++) {
				Date startDate = DashboardUtil.getStartDate(fiscalCalendarId, i);
				Date endDate = DashboardUtil.getEndDate(fiscalCalendarId, i);
				DecimalWraper fundingDisb = DbUtil
				.getFunding(filter, startDate, endDate, null, null,
						Constants.DISBURSEMENT, Constants.ACTUAL);
				xmlString
				.append("<year category=\"" + i + "\" amount=\""+ fundingDisb.getValue().divide(divideByDenominator) + "\"/>\n");
				
			}
			xmlString.append("</fundingtype>\n");
			if (filter.isExpendituresVisible()) {
				xmlString.append("<fundingtype name=\"Expenditure\">\n");
				for (int i = year.intValue() - yearsInRange; i <= year.intValue(); i++) {
					Date startDate = DashboardUtil.getStartDate(fiscalCalendarId, i);
					Date endDate = DashboardUtil.getEndDate(fiscalCalendarId, i);
					DecimalWraper fundingExp = DbUtil
					.getFunding(filter, startDate, endDate, null, null,
							Constants.EXPENDITURE, Constants.ACTUAL);
					xmlString
					.append("<year category=\"" + i + "\" amount=\""+ fundingExp.getValue().divide(divideByDenominator) + "\"/>\n");
					
				}
				xmlString.append("</fundingtype>\n");
			}
			if (filter.isPledgeVisible()) {
				xmlString.append("<fundingtype name=\"Pledges\">\n");
				for (int i = year.intValue() - yearsInRange; i <= year.intValue(); i++) {
					Double fundingPledge = 0d;
					Date startDate = DashboardUtil.getStartDate(fiscalCalendarId, i);
					Date endDate = DashboardUtil.getEndDate(fiscalCalendarId, i);
					fundingPledge = DbUtil.getPledgesFunding(filter.getOrgIds(),
							filter.getOrganizationGroupId(), startDate, endDate,
							currCode);
					xmlString
					.append("<year category=\"" + i + "\" amount=\""+ fundingPledge
							/ divideByDenominator.doubleValue() + "\"/>\n");
					
				}
				xmlString.append("</fundingtype>\n");
			}

			
			PrintWriter out = new PrintWriter(new OutputStreamWriter(
					response.getOutputStream(), "UTF-8"), true);

			out.println(xmlString.toString());

			out.close();

			return null;
			
			
		}

		for (int i = year.intValue() - yearsInRange; i <= year.intValue(); i++) {
			// apply calendar filter
			csvString.append(i);
			csvString.append(",");
			Date startDate = DashboardUtil.getStartDate(fiscalCalendarId, i);
			Date endDate = DashboardUtil.getEndDate(fiscalCalendarId, i);

			DecimalWraper fundingComm = DbUtil
					.getFunding(filter, startDate, endDate, null, null,
							Constants.COMMITMENT, Constants.ACTUAL);
			csvString
					.append(fundingComm.getValue().divide(divideByDenominator));
			csvString.append(",");
			DecimalWraper fundingDisb = DbUtil.getFunding(filter, startDate,
					endDate, null, null, Constants.DISBURSEMENT,
					Constants.ACTUAL);
			csvString
					.append(fundingDisb.getValue().divide(divideByDenominator));
			DecimalWraper fundingExp = new DecimalWraper();
			if (filter.isExpendituresVisible()) {
				csvString.append(",");
				fundingExp = DbUtil.getFunding(filter, startDate, endDate,
						null, null, Constants.EXPENDITURE, Constants.ACTUAL);
				csvString.append(fundingExp.getValue().divide(
						divideByDenominator));
			}

			Double fundingPledge = 0d;
			if (filter.isPledgeVisible()) {
				fundingPledge = DbUtil.getPledgesFunding(filter.getOrgIds(),
						filter.getOrganizationGroupId(), startDate, endDate,
						currCode);
				csvString.append(",");
				csvString.append(fundingPledge
						/ divideByDenominator.doubleValue());
			}
			if (fundingPledge.doubleValue() != 0
					|| fundingComm.doubleValue() != 0
					|| fundingDisb.doubleValue() != 0
					|| fundingExp.doubleValue() != 0) {
				// nodata = false;
			}
			csvString.append("\n");
			// totalPledges += fundingPledge;
			// totalCommitments = totalCommitments.add(fundingComm.getValue());
			// totalDisbursements =
			// totalDisbursements.add(fundingDisb.getValue());
			// totalExpenditures = totalExpenditures.add(fundingExp.getValue());
		}

		// if (totalPledges == 0.0&&filter.isPledgeVisible())
		// result.removeRow(pledgesTranslatedTitle);
		// if (totalCommitments.equals(new BigDecimal(0)))
		// result.removeRow(actComTranslatedTitle);
		// if (totalDisbursements.equals(new BigDecimal(0)))
		// result.removeRow(actDisbTranslatedTitle);
		// if (totalExpenditures.equals(new
		// BigDecimal(0))&&filter.isExpendituresVisible())
		// result.removeRow(actExpTranslatedTitle);
		//
		//
		// if (nodata) {
		// result = new DefaultCategoryDataset();
		// }
		PrintWriter out = new PrintWriter(new OutputStreamWriter(
				response.getOutputStream(), "UTF-8"), true);

		out.println(csvString.toString());

		out.close();

		return null;
	}

	public ActionForward getJSONObject(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		VisualizationForm visualizationForm = (VisualizationForm)form;
		
		String parentId = request.getParameter("parentId");
		String objectType = request.getParameter("objectType");

        JSONObject root = new JSONObject();
	    JSONArray children = new JSONArray();

	    //Gets children object according to objectType
		
		if(parentId != null && objectType != null && objectType.equals("Organization"))
		{
			//Get list of sub organizations
	        Long orgGroupId = Long.parseLong(parentId);

	        List<AmpOrganisation> orgs;
	        try {
	            orgs = DbUtil.getDonorOrganisationByGroupId(orgGroupId, false); //TODO: Second parameter for public view
				JSONObject child = new JSONObject();
				Iterator<AmpOrganisation> it = orgs.iterator();
				while(it.hasNext()){
					AmpOrganisation org = it.next();
					child.put("ID", org.getAmpOrgId());
					child.put("name", org.getName());
					children.add(child);
				}
				root.put("ID", parentId);
				root.put("objectType", objectType);
				root.put("children", children);
	        } catch (Exception e) {
	            logger.error("unable to load organizations", e);
	        }
		} else if (parentId != null && objectType != null && objectType.equals("Sector")) {
			Long sectorId = Long.parseLong(parentId);
	        List<AmpSector> sectors;
	        try {
	        	sectors = DbUtil.getSubSectors(sectorId); 
				JSONObject child = new JSONObject();
				Iterator<AmpSector> it = sectors.iterator();
				while(it.hasNext()){
					AmpSector sector = it.next();
					child.put("ID", sector.getAmpSectorId());
					child.put("name", sector.getName());
					children.add(child);
				}
				root.put("ID", parentId);
				root.put("objectType", objectType);
				root.put("children", children);
	        } catch (Exception e) {
	            logger.error("unable to load organizations", e);
	        }
		} else if (parentId != null && objectType != null && objectType.equals("Region")){
			Long regionId = Long.parseLong(parentId);
	        List<AmpCategoryValueLocations> zones = new ArrayList<AmpCategoryValueLocations>();

	        if (regionId != null && regionId != -1) {
                AmpCategoryValueLocations region = LocationUtil.getAmpCategoryValueLocationById(regionId);
                if (region.getChildLocations() != null) {
    				JSONObject child = new JSONObject();
                	Iterator<AmpCategoryValueLocations> it = region.getChildLocations().iterator();
    				while(it.hasNext()){
    					AmpCategoryValueLocations loc = it.next();
    					child.put("ID", loc.getId());
    					child.put("name",loc.getName());
    					children.add(child);
    				}
                }
            }
			root.put("ID", parentId);
			root.put("objectType", objectType);
			root.put("children", children);
		}
		response.setContentType("text/json-comment-filtered");
		OutputStreamWriter outputStream = null;

		try {
			outputStream = new OutputStreamWriter(response.getOutputStream(),"UTF-8");
			outputStream.write(root.toString());
		} finally {
			if (outputStream != null) {
				outputStream.close();
			}
		}
		return null;
	}
    
}

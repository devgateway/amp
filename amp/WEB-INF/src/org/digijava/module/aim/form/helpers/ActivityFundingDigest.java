package org.digijava.module.aim.form.helpers;

import java.util.*;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.form.ProposedProjCost;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FilterParams;
import org.digijava.module.aim.helper.FinancingBreakdown;
import org.digijava.module.aim.helper.FinancingBreakdownWorker;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.Funding;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.FundingOrganization;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.KeyValue;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.logic.FundingCalculationsHelper;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.servlet.http.HttpSession;

import lombok.Data;

/**
 * funding digest for a full activity
 * moved from being previously the inner class EditActivityForm::Funding (clashing name with org.digijava.module.aim.helper.Funding)
 * @author Dolghier Constantin.
 *
 */
@Data
public class ActivityFundingDigest {
	private ProposedProjCost proProjCost;
    private List<ProposedProjCost> proposedAnnualBudgets;
	private List<FundingOrganization> fundingOrganizations;
	private String donorObjective;
	private List<FundingDetail> fundingDetails;

	private String totalCommitments;
	private String totalPlannedCommitments;
	private String totalPlannedRoF;
	private String totalPlannedEDD;		
	private String totalActualRoF;
	private String totalActualEDD;
	
	private String totalPipelineCommitments;
	
	private String totalOdaSscCommitments;
	private String totalTriangularSscCommitments;
	private String totalBilateralSscCommitments;
	
	private double totalCommitmentsDouble;
	private String totalDisbursements;
	private String totalExpenditures;
	private String totalPlannedExpenditures;
	private String totalPlannedDisbursements;
	private String totalPlannedDisbursementsOrders;
	private String totalActualDisbursementsOrders;
	private String unDisbursementsBalance;
	private String totalMtefProjections;	
	private boolean fixerate;
	private double regionTotalDisb;
	private Collection orderedFundingOrganizations;
	private Collection financingBreakdown = null;
	
	private String consumptionRate;
	private String deliveryRate;

	// Add Funding fields

	private Collection organizations;
	private Collection<AmpCurrency> validcurrencies;
	private Collection<FundingPledges> pledgeslist;
	private boolean dupFunding;
	private String orgName;
	
	private Long assistanceType = null;  // this is id of a category value from category Type Of Assistance
	private Long modality = null; // this is id of a category value from category Financing Instrument
	private Long fundingStatus = null; // this is id of a category value from category Funding Status
	private Long modeOfPayment = null; // this is id of a category value from category Mode Of Payment
	
	//private List<MTEFProjection> fundingMTEFProjections;
	private List<KeyValue> availableMTEFProjectionYears;
	private Collection projections;
	private String orgFundingId;
	private String sourceRole;
	private int numComm;
	private int numDisb;
	private int numExp;
	private int numDisbOrder;
	private int numProjections;
	private String disbOrderId;
	private String signatureDate;
	private String reportingDate;
	private String plannedStartDate;
	private String plannedCompletionDate;
	private String actualStartDate;
	private String actualCompletionDate;
	private String fundingConditions;

	// flags field
	private boolean firstSubmit;
	private String event;
	private boolean editFunding;
	private Long fundDonor;
	private Long fundingId;
	private Long fundingRegionId;
	private Collection<AmpCategoryValueLocations> fundingRegions;
	private Long[] selRegFundings;

	private Collection regionalFundings;
	private Long selFundingOrgs[];
	private long orgId;
	private int offset;
	private int indexId;
	private long transIndexId;
    private String fundingCurrCode;
    private int selectedMTEFProjectionYear;
    
    private float capitalSpendingPercentage;
    private boolean showActual;
    private boolean showPlanned;
    private boolean showPipeline;
    private boolean showOfficialDevelopmentAid;
    private boolean showBilateralSsc;
    private boolean showTriangularSsc;
    
    
    public ActivityFundingDigest()
    {}
    
    /**
     * calculates various totals, populates {@link #fundingOrganizations} with funding sorted and digested by organization
     * @param fundings
     * @param toCurrCode
     * @param tm - ignored, left in case in the future the "buggy switch fundings to a common currency" would be enabled (AND FIXED FOR MTEF FUNDINGS!)
     * @param debug - whether to output debugging information to the results strings
     */
    public void populateFromFundings(Collection<AmpFunding> fundings, String toCurrCode, TeamMember tm, boolean debug)
    {
        FundingCalculationsHelper activityTotalCalculations = new FundingCalculationsHelper();
        activityTotalCalculations.setDebug(debug);
        
    	ArrayList<FundingOrganization> fundingOrgs = new ArrayList<FundingOrganization>();
    	Iterator<AmpFunding> fundItr = fundings.iterator();
    	while(fundItr.hasNext())
    	{
    		AmpFunding ampFunding = fundItr.next();
    		AmpOrganisation org = ampFunding.getAmpDonorOrgId();
    		if(org == null || org.getAmpOrgId()==null)
    			continue;

    		FundingOrganization fundOrg = new FundingOrganization(ampFunding);

    		Funding fund = new Funding(ampFunding, activityTotalCalculations, toCurrCode, false /*isPreview*/, tm);

    		Collection<FundingDetail> fundDetails = fund.getFundingDetails();
    		if (fundDetails != null && fundDetails.size() > 0)
    		{
    			if (this.getFundingDetails() == null)
    				this.setFundingDetails(new ArrayList<FundingDetail>());
    			this.getFundingDetails().addAll(new ArrayList<FundingDetail>(fund.getFundingDetails()));
    		}

    		int index = fundingOrgs.indexOf(fundOrg);
    		// logger.info("Getting the index as " + index
    		//	+ " for fundorg " + fundOrg.getOrgName());
    		if(index > -1) {
    			fundOrg = (FundingOrganization) fundingOrgs.get(index);
    		}
    		if (fundOrg.getFundings() == null)
    			fundOrg.setFundings(new ArrayList<Funding>());

    		fundOrg.getFundings().add(fund);

    		if (index > -1) {
    			fundingOrgs.set(index, fundOrg);
    			//logger.info("!!!! Setting the fund org obj to the index :"	+ index);
    		}
    		else
    		{
    			fundingOrgs.add(fundOrg);
    			//logger.info("???? Adding new fund org object");
    		}
    	}

        // Added for the calculation of the subtotal per Organization
    	Iterator<FundingOrganization> iterFundOrg = fundingOrgs.iterator();
    	while (iterFundOrg.hasNext())
    	{
    		FundingOrganization currFundingOrganization = iterFundOrg.next();
    		Iterator<Funding> iterFunding = currFundingOrganization.getFundings().iterator();
    		for(Funding currFunding:currFundingOrganization.getFundings())
    		{
    			FundingCalculationsHelper calculationsSubtotal=new FundingCalculationsHelper();
    			if(currFunding.getFundingDetails()!=null)
    			{
    				try
    				{
    					calculationsSubtotal.doCalculations(currFunding.getAmpRawFunding(), toCurrCode, true);
    					currFunding.setSubtotalPlannedCommitments(FormatHelper.formatNumber(calculationsSubtotal.getTotPlannedComm().doubleValue()));
    					currFunding.setSubtotalActualCommitments(FormatHelper.formatNumber(calculationsSubtotal.getTotActualComm().doubleValue()));
    					currFunding.setSubtotalPipelineCommitments(FormatHelper.formatNumber(calculationsSubtotal.getTotPipelineComm().doubleValue()));
    					currFunding.setSubtotalOfficialDevelopmentAidCommitments(FormatHelper.formatNumber(calculationsSubtotal.getTotOdaSscComm().doubleValue()));
    					currFunding.setSubtotalBilateralSscCommitments(FormatHelper.formatNumber(calculationsSubtotal.getTotBilateralSscComm().doubleValue()));
    					currFunding.setSubtotalTriangularSscCommitments(FormatHelper.formatNumber(calculationsSubtotal.getTotTriangularSscComm().doubleValue()));
    					
    					
    					currFunding.setSubtotalPlannedDisbursements(FormatHelper.formatNumber(calculationsSubtotal.getTotPlanDisb().doubleValue()));
    					currFunding.setSubtotalPipelineDisbursements(FormatHelper.formatNumber(calculationsSubtotal.getTotPipelineDisb().doubleValue()));
    					currFunding.setSubtotalDisbursements(FormatHelper.formatNumber(calculationsSubtotal.getTotActualDisb().doubleValue()));

    					currFunding.setSubtotalPlannedExpenditures(FormatHelper.formatNumber(calculationsSubtotal.getTotPlannedExp().doubleValue()));
    					currFunding.setSubtotalPipelineExpenditures(FormatHelper.formatNumber(calculationsSubtotal.getTotPipelineExp().doubleValue()));
    					currFunding.setSubtotalExpenditures(FormatHelper.formatNumber(calculationsSubtotal.getTotActualExp().doubleValue()));
    					currFunding.setSubtotalMTEFs(FormatHelper.formatNumber(calculationsSubtotal.getTotalMtef().doubleValue()));

    					currFunding.setSubtotalActualDisbursementsOrders(FormatHelper.formatNumber(calculationsSubtotal.getTotActualDisbOrder().doubleValue()));
    					currFunding.setSubtotalPlannedDisbursementsOrders(FormatHelper.formatNumber(calculationsSubtotal.getTotPlannedDisbOrder().doubleValue()));
    					currFunding.setSubtotalPipelineDisbursementsOrders(FormatHelper.formatNumber(calculationsSubtotal.getTotPipelineDisbOrder().doubleValue()));

    					currFunding.setUndisbursementbalance(FormatHelper.formatNumber(calculationsSubtotal.getUnDisbursementsBalance().doubleValue()));
    					currFunding.cleanAmpRawFunding();
    				}
    				catch(Exception ex)
    				{
    					ex.printStackTrace();
    				}
    			}
    		}
    	}

    	// logger.info("size = " + fundingOrgs);
    	Collections.sort(fundingOrgs);
    	this.setFundingOrganizations(fundingOrgs);
    	//get the total depend of the

    	if(debug)
    	{
    		this.setTotalCommitments(activityTotalCalculations.getTotalCommitments().getCalculations());
    		this.setTotalCommitmentsDouble(activityTotalCalculations.getTotalCommitments().getValue().doubleValue());

    		this.setTotalDisbursements(activityTotalCalculations.getTotActualDisb().getCalculations());
    		this.setTotalPlannedDisbursements(activityTotalCalculations.getTotPlanDisb().getCalculations());
    		this.setTotalExpenditures(activityTotalCalculations.getTotPlannedExp().getCalculations());
    		this.setTotalPlannedCommitments(activityTotalCalculations.getTotPlannedComm().getCalculations());
    		this.setTotalPlannedRoF(activityTotalCalculations.getTotPlannedReleaseOfFunds().getCalculations());
    		this.setTotalPlannedEDD(activityTotalCalculations.getTotPlannedEDD().getCalculations());
    		this.setTotalPipelineCommitments(activityTotalCalculations.getTotPipelineComm().getCalculations());
    		this.setTotalPlannedExpenditures(activityTotalCalculations.getTotPlannedExp().getCalculations());
    		this.setTotalActualDisbursementsOrders(activityTotalCalculations.getTotActualDisbOrder().getCalculations());
    		this.setTotalPlannedDisbursementsOrders(activityTotalCalculations.getTotPlannedDisbOrder().getCalculations());
    		this.setUnDisbursementsBalance(activityTotalCalculations.getUnDisbursementsBalance().getCalculations());
        }
    	else {
    		// actual
    		this.setTotalCommitments(activityTotalCalculations.getTotActualComm().toString());
    		this.setTotalDisbursements(activityTotalCalculations.getTotActualDisb().toString());
    		this.setTotalExpenditures(activityTotalCalculations.getTotActualExp().toString());
    		this.setTotalActualDisbursementsOrders(activityTotalCalculations.getTotActualDisbOrder().toString());
    		this.setTotalActualRoF(activityTotalCalculations.getTotActualReleaseOfFunds().toString());
    		this.setTotalActualEDD(activityTotalCalculations.getTotActualEDD().toString());
    		
    		// planned
    		this.setTotalPlannedDisbursements(activityTotalCalculations.getTotPlanDisb().toString());
    		this.setTotalPlannedCommitments(activityTotalCalculations.getTotPlannedComm().toString());
    		this.setTotalPlannedExpenditures(activityTotalCalculations.getTotPlannedExp().toString());
    		this.setTotalPlannedDisbursementsOrders(activityTotalCalculations.getTotPlannedDisbOrder().toString());
    		this.setTotalPlannedRoF(activityTotalCalculations.getTotPlannedReleaseOfFunds().toString());
    		this.setTotalPlannedEDD(activityTotalCalculations.getTotPlannedEDD().toString());
    		this.setUnDisbursementsBalance(activityTotalCalculations.getUnDisbursementsBalance().toString());
    		this.setTotalMtefProjections(activityTotalCalculations.getTotalMtef().toString());
    		// pipeline
    		this.setTotalPipelineCommitments(activityTotalCalculations.getTotPipelineComm().toString());
    		
    		// ssc
    		this.setTotalOdaSscCommitments(activityTotalCalculations.getTotOdaSscComm().toString());
    		this.setTotalBilateralSscCommitments(activityTotalCalculations.getTotBilateralSscComm().toString());
    		this.setTotalTriangularSscCommitments(activityTotalCalculations.getTotTriangularSscComm().toString());

        }
    	
    	// calculate consumption and delivery rates
    	if (activityTotalCalculations.getTotActualExp() != null && activityTotalCalculations.getTotActualExp().doubleValue() != 0
    			&& activityTotalCalculations.getTotActualDisb() != null && activityTotalCalculations.getTotActualDisb().doubleValue() != 0) 
    	{
    		double consumptionRate = activityTotalCalculations.getTotActualExp().doubleValue() / activityTotalCalculations.getTotActualDisb().doubleValue();
    		NumberFormat formatter = DecimalFormat.getPercentInstance();
    		this.setConsumptionRate(formatter.format(consumptionRate));
    	}

    	if (activityTotalCalculations.getTotActualComm() != null && activityTotalCalculations.getTotActualComm().doubleValue() != 0
				    && activityTotalCalculations.getTotActualDisb() != null && activityTotalCalculations.getTotActualDisb().doubleValue() !=0) 
    	{
    		double deliveryRate = activityTotalCalculations.getTotActualDisb().doubleValue() / activityTotalCalculations.getTotActualComm().doubleValue();
    		NumberFormat formatter = DecimalFormat.getPercentInstance();
    		this.setDeliveryRate(formatter.format(deliveryRate));
    	}
    }

 
	// TODO: Constantin - when doing the rewrite, all we need are flags, not lists
    public List<FundingDetail> getCommitmentsDetails() {
		if(fundingDetails != null){
			List<FundingDetail> commitments = new ArrayList<FundingDetail>();
			for (FundingDetail detail : fundingDetails){
				if(detail.getTransactionType() == Constants.COMMITMENT) commitments.add(detail);
			}
			return commitments;
		}
		return null;
	}
    
 
	
	public List<FundingDetail> getDisbursementsDetails() {
		if(fundingDetails != null){
			List<FundingDetail> disbursements = new ArrayList<FundingDetail>();
			for (FundingDetail detail : fundingDetails){
				if(detail.getTransactionType() == Constants.DISBURSEMENT) disbursements.add(detail);
			}
			return disbursements;
		}
		return null;
	}
	
	public List<FundingDetail> getDisbursementOrdersDetails() {
		
		if(fundingDetails != null){
			List<FundingDetail> disbursementOrder = new ArrayList<FundingDetail>();
			for (FundingDetail detail : fundingDetails){
				if(detail.getTransactionType() == Constants.DISBURSEMENT_ORDER) disbursementOrder.add(detail);
			}
			return disbursementOrder;
		}
		return null;
	}

	public List<FundingDetail> getExpendituresDetails() {
		if(fundingDetails != null){
			List<FundingDetail> expenditures = new ArrayList<FundingDetail>();
			for (FundingDetail detail : fundingDetails){
				if(detail.getTransactionType() == Constants.EXPENDITURE) expenditures.add(detail);
			}
			return expenditures;
		}
		return null;
	}
    
	public List<FundingDetail> getMtefDetails() {
		if(fundingDetails != null){
			List<FundingDetail> mtefs = new ArrayList<FundingDetail>();
			for (FundingDetail detail : fundingDetails){
				if(detail.getTransactionType() == Constants.MTEFPROJECTION) mtefs.add(detail);
			}
			return mtefs;
		}
		return null;
	}
		
	public Collection<AmpCategoryValueLocations> getFundingRegionsUnique() {
		Collection<AmpCategoryValueLocations> unique = new ArrayList<AmpCategoryValueLocations>();
		Iterator<AmpCategoryValueLocations> it = fundingRegions.iterator();
		while (it.hasNext()) {
			AmpCategoryValueLocations val = (AmpCategoryValueLocations) it.next();
			if (!unique.contains(val))
				unique.add(val);
		}
		return unique;
	}
			
	public boolean isDisbursementOrders() {
		boolean disbOrdersExist = false;
		if (fundingDetails != null && fundingDetails.size() > 0) {
			Iterator<FundingDetail> detailIter = fundingDetails.iterator();
			while (detailIter.hasNext()) {
				FundingDetail det = detailIter.next();
				if (det.getTransactionType() == 4) {
					disbOrdersExist = true;
					break;
				}
			}
		}
		return disbOrdersExist;

	}

	public boolean isFixerate() {
		this.fixerate = false;
		if (fundingDetails != null) {
			Iterator iter = fundingDetails.iterator();
			while (iter.hasNext()) {
				FundingDetail element = (FundingDetail) iter.next();
				if (element.getFixedExchangeRate() != null) {
					this.fixerate = true;
					break;
				}
			}
		}
		return fixerate;
	}

	public final static FilterParams buildFilterParams()
	{
		HttpSession session = TLSUtils.getRequest().getSession();
		FilterParams fp = (FilterParams) session.getAttribute("filterParams");
		if (fp == null) 
		{
			fp = new FilterParams();
			int year = new GregorianCalendar().get(Calendar.YEAR);
			fp.setFromYear(year-Constants.FROM_YEAR_RANGE);
			fp.setToYear(year+Constants.TO_YEAR_RANGE);
		}

		AmpApplicationSettings appSettings = AmpARFilter.getEffectiveSettings();
		if (appSettings != null) 
		{			
			String currCode = appSettings.getCurrency().getCurrencyCode();
			if (currCode != null) {
				fp.setCurrencyCode(currCode);
			}

			if (fp.getFiscalCalId() == null) 
			{
				if (appSettings.getFiscalCalendar() != null) {
					fp.setFiscalCalId(appSettings.getFiscalCalendar().getAmpFiscalCalId());
				} else 
				{
					fp.setFiscalCalId(FeaturesUtil.getGlobalSettingValueLong(GlobalSettingsConstants.DEFAULT_CALENDAR));
				}
			}
		}
		return fp;
	}
	
	public void fillFinancialBreakdowns(Long activityId, Collection<AmpFunding> ampFundingsAux, boolean debug)
	{
	    //Collection ampFundingsAux = DbUtil.getAmpFunding(activityId);
//	    TeamMember teamMember = (TeamMember) session.getAttribute("currentMember");

//		FilterParams fp = buildFilterParams();
//
//		Collection<FinancingBreakdown> fb = FinancingBreakdownWorker.getFinancingBreakdownList(activityId, ampFundingsAux, fp, debug);
//		this.setFinancingBreakdown(fb);
//		String overallTotalCommitted = "";
//		String overallTotalDisbursed = "";
//		String overallTotalUnDisbursed = "";
//		String overallTotalExpenditure = "";
//		String overallTotalUnExpended = "";
//		String overallTotalDisburOrder = "";
//
//		overallTotalCommitted = FinancingBreakdownWorker.getOverallTotal(fb, Constants.COMMITMENT, Constants.ACTUAL, debug);
//		overallTotalDisbursed = FinancingBreakdownWorker.getOverallTotal(fb, Constants.DISBURSEMENT,Constants.ACTUAL,debug);
//		overallTotalDisburOrder=FinancingBreakdownWorker.getOverallTotal(fb, Constants.DISBURSEMENT_ORDER,Constants.ACTUAL,debug);
//		if(!debug)
//		{
//			overallTotalUnDisbursed = FormatHelper.getDifference(overallTotalCommitted, overallTotalDisbursed);
//		}
//		else
//		{
//			overallTotalUnDisbursed =overallTotalCommitted + "-" +overallTotalDisbursed;
//		}
//		overallTotalExpenditure = FinancingBreakdownWorker.getOverallTotal(fb, Constants.EXPENDITURE,Constants.ACTUAL,debug);
//		if(!debug)
//		{
//			overallTotalUnExpended = FormatHelper.getDifference(overallTotalDisbursed, overallTotalExpenditure);
//		}
//		else
//		{
//			overallTotalExpenditure = overallTotalDisbursed+ "-" + overallTotalExpenditure;
//		}
//
//		this.setTotalCommitted(overallTotalCommitted);
//		this.setTotalDisbursed(overallTotalDisbursed);
//		this.setTotalExpended(overallTotalExpenditure);
//	    this.setTotalUnDisbursed(overallTotalUnDisbursed);
//	    this.setTotalUnExpended(overallTotalUnExpended);
//	    this.setTotalDisbOrder(overallTotalDisburOrder);
	}
	
}

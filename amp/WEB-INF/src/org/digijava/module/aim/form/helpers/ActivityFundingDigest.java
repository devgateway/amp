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

/**
 * funding digest for a full activity
 * moved from being previously the inner class EditActivityForm::Funding (clashing name with org.digijava.module.aim.helper.Funding)
 * @author Dolghier Constantin.
 *
 */
public class ActivityFundingDigest {
	private ProposedProjCost proProjCost;
    private List<ProposedProjCost> proposedAnnualBudgets;
	private List<FundingOrganization> fundingOrganizations;
	private String donorObjective;
	private List<FundingDetail> fundingDetails;

//	private String totalCommitted = "";
//	private String totalDisbursed = "";
//	private String totalUnDisbursed = "";
//	private String totalExpenditure = "";
//	private String totalUnExpended = "";
//	private String totalExpended = "";
//	private String totalDisbOrder = "";

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

    					currFunding.setUnDisbursementBalance(FormatHelper.formatNumber(calculationsSubtotal.getUnDisbursementsBalance().doubleValue()));
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
    		this.setTotalPlannedReleaseOfFunds(activityTotalCalculations.getTotPlannedRoF().getCalculations());
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
    		this.setTotalActualRoF(activityTotalCalculations.getTotalActualRoF().toString());
    		this.setTotalActualEDD(activityTotalCalculations.getTotalActualEDD().toString());
    		
    		// planned
    		this.setTotalPlannedDisbursements(activityTotalCalculations.getTotPlanDisb().toString());
    		this.setTotalPlannedCommitments(activityTotalCalculations.getTotPlannedComm().toString());
    		this.setTotalPlannedExpenditures(activityTotalCalculations.getTotPlannedExp().toString());
    		this.setTotalPlannedDisbursementsOrders(activityTotalCalculations.getTotPlannedDisbOrder().toString());
    		this.setTotalPlannedReleaseOfFunds(activityTotalCalculations.getTotPlannedRoF().toString());
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

    public List<ProposedProjCost> getProposedAnnualBudgets() {
        return proposedAnnualBudgets;
    }

    public void setProposedAnnualBudgets(List<ProposedProjCost> proposedAnnualBudgets) {
        this.proposedAnnualBudgets = proposedAnnualBudgets;
    }

    public boolean isShowPipeline() {
		return showPipeline;
	}

	public void setShowPipeline(boolean showPipeline) {
		this.showPipeline = showPipeline;
	}

	public boolean isShowActual() {
		return showActual;
	}

	public void setShowActual(boolean showActual) {
		this.showActual = showActual;
	}

	public boolean isShowPlanned() {
		return showPlanned;
	}

	public void setShowPlanned(boolean showPlanned) {
		this.showPlanned = showPlanned;
	}
	
    public void setShowOfficialDevelopmentAid(boolean showOfficialDevelopmentAid)
    {
    	this.showOfficialDevelopmentAid = showOfficialDevelopmentAid;
    }
    
    public void setShowBilateralSsc(boolean showBilateralSsc)
    {
    	this.showBilateralSsc = showBilateralSsc;
    }
    
    public void setShowTriangularSsc(boolean showTriangularSsc)
    {
    	this.showTriangularSsc = showTriangularSsc;
    }

    public boolean getShowOfficialDevelopmentAid()
    {
    	return showOfficialDevelopmentAid;
    }
    
    public boolean getShowBilateralSsc()
    {
    	return showBilateralSsc;
    }
    
    public boolean getShowTriangularSsc()
    {
    	return showTriangularSsc;
    }
    
	public float getCapitalSpendingPercentage() {
        return capitalSpendingPercentage;
    }

    public void setCapitalSpendingPercentage(float capitalSpendingPercentage) {
        this.capitalSpendingPercentage = capitalSpendingPercentage;
    }

    public void setConsumptionRate(String consumptionRate) {
        this.consumptionRate = consumptionRate;
    }   
    
	public void setDeliveryRate(String deliveryRate) {
		this.deliveryRate = deliveryRate;
	}             
    
	public String getConsumptionRate() {
		return consumptionRate;
	}
	
	public String getDeliveryRate() {
		return deliveryRate;
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
	
    public Collection<FundingPledges> getPledgeslist() {
		return pledgeslist;
	}

	public void setPledgeslist(Collection<FundingPledges> pledgeslist) {
		this.pledgeslist = pledgeslist;
	}
    
    public String getFundingCurrCode() {
        return fundingCurrCode;
    }

    public void setFundingCurrCode(String fundingCurrCode) {
        this.fundingCurrCode = fundingCurrCode;
    }
	
//	private boolean totDisbIsBiggerThanTotCom;
	
	public int getIndexId() {
		return indexId;
	}

	public void setIndexId(int indexId) {
		this.indexId = indexId;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

	public Collection<AmpCategoryValueLocations> getFundingRegions() {
		return fundingRegions;
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
			
			
	public void setFundingRegions(Collection<AmpCategoryValueLocations> fundingRegions) {
		this.fundingRegions = fundingRegions;
	}

	public int getNumComm() {
		return numComm;
	}

	public void setNumComm(int numComm) {
		this.numComm = numComm;
	}

	public int getNumDisb() {
		return numDisb;
	}

	public void setNumDisb(int numDisb) {
		this.numDisb = numDisb;
	}

	public int getNumExp() {
		return numExp;
	}

	public void setNumExp(int numExp) {
		this.numExp = numExp;
	}

	public int getNumProjections() {
		return numProjections;
	}

	public void setNumProjections(int numProjections) {
		this.numProjections = numProjections;
	}

	public ProposedProjCost getProProjCost() {
		return proProjCost;
	}

	public void setProProjCost(ProposedProjCost proProjCost) {
		this.proProjCost = proProjCost;
	}

	public List<FundingOrganization> getFundingOrganizations() {
		return fundingOrganizations;
	}

	private void setFundingOrganizations(List<FundingOrganization> fundingOrganizations) {
		this.fundingOrganizations = fundingOrganizations;
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
	
	public List<FundingDetail> getFundingDetails() {
		return fundingDetails;
	}

	private void setFundingDetails(List<FundingDetail> fundingDetails) {
		this.fundingDetails = fundingDetails;
	}

//	public String getTotalCommitted() {
//		return totalCommitted;
//	}
//
//	private void setTotalCommitted(String totalCommitted) {
//		this.totalCommitted = totalCommitted;
//	}
//
//	public String getTotalDisbursed() {
//		return totalDisbursed;
//	}
//
//	private void setTotalDisbursed(String totalDisbursed) {
//		this.totalDisbursed = totalDisbursed;
//	}
//
//	public String getTotalUnDisbursed() {
//		return totalUnDisbursed;
//	}
	
	private void setTotalActualRoF(String totalActualRoF)
	{
		this.totalActualRoF = totalActualRoF;
	}

	private void setTotalActualEDD(String totalActualEDD)
	{
		this.totalActualEDD = totalActualEDD;
	}
	
	public String getTotalActualRoF()
	{
		return this.totalActualRoF;
	}
	
	public String getTotalActualEDD()
	{
		return this.totalActualEDD;
	}
	
//	private void setTotalUnDisbursed(String totalUnDisbursed) {
//		this.totalUnDisbursed = totalUnDisbursed;
//	}
//
//	public String getTotalExpenditure() {
//		return totalExpenditure;
//	}
//
//	private void setTotalExpenditure(String totalExpenditure) {
//		this.totalExpenditure = totalExpenditure;
//	}
//
//	public String getTotalUnExpended() {
//		return totalUnExpended;
//	}
//
//	private void setTotalUnExpended(String totalUnExpended) {
//		this.totalUnExpended = totalUnExpended;
//	}
//
//	public String getTotalExpended() {
//		return totalExpended;
//	}
//
//	private void setTotalExpended(String totalExpended) {
//		this.totalExpended = totalExpended;
//	}
//
//	public String getTotalDisbOrder() {
//		return totalDisbOrder;
//	}
//
//	private void setTotalDisbOrder(String totalDisbOrder) {
//		this.totalDisbOrder = totalDisbOrder;
//	}	
	
	public String getTotalCommitments() {
		return totalCommitments;
	}

	private void setTotalCommitments(String totalCommitments) {
		this.totalCommitments = totalCommitments;
	}

	public String getTotalPlannedCommitments() {
		return totalPlannedCommitments;
	}

	private void setTotalPlannedCommitments(String totalPlannedCommitments) {
		this.totalPlannedCommitments = totalPlannedCommitments;
	}

	public String getTotalPlannedReleaseOfFunds() {
		return totalPlannedRoF;
	}

	private void setTotalPlannedReleaseOfFunds(String totalPlannedRoF) {
		this.totalPlannedRoF = totalPlannedRoF;
	}
	public String getTotalPlannedEDD() {
		return totalPlannedEDD;
	}

	private void setTotalPlannedEDD(String totalPlannedEDD) {
		this.totalPlannedEDD = totalPlannedEDD;
	}
	
	
	public double getTotalCommitmentsDouble() {
		return totalCommitmentsDouble;
	}

	private void setTotalCommitmentsDouble(double totalCommitmentsDouble) {
		this.totalCommitmentsDouble = totalCommitmentsDouble;
	}

	public String getTotalDisbursements() {
		return totalDisbursements;
	}

	private void setTotalDisbursements(String totalDisbursements) {
		this.totalDisbursements = totalDisbursements;
	}

	public String getTotalExpenditures() {
		return totalExpenditures;
	}

	private void setTotalExpenditures(String totalExpenditures) {
		this.totalExpenditures = totalExpenditures;
	}

	public String getTotalPlannedExpenditures() {
		return totalPlannedExpenditures;
	}

	private void setTotalPlannedExpenditures(String totalPlannedExpenditures) {
		this.totalPlannedExpenditures = totalPlannedExpenditures;
	}

	public String getTotalPlannedDisbursements() {
		return totalPlannedDisbursements;
	}

	private void setTotalPlannedDisbursements(String totalPlannedDisbursements) {
		this.totalPlannedDisbursements = totalPlannedDisbursements;
	}

	public String getTotalPlannedDisbursementsOrders() {
		return totalPlannedDisbursementsOrders;
	}

	private void setTotalPlannedDisbursementsOrders(String totalPlannedDisbursementsOrders) {
		this.totalPlannedDisbursementsOrders = totalPlannedDisbursementsOrders;
	}

	public String getTotalActualDisbursementsOrders() {
		return totalActualDisbursementsOrders;
	}

	private void setTotalActualDisbursementsOrders(String totalActualDisbursementsOrders) {
		this.totalActualDisbursementsOrders = totalActualDisbursementsOrders;
	}

	public String getUnDisbursementsBalance() {
		return unDisbursementsBalance;
	}

	public void setUnDisbursementsBalance(String unDisbursementsBalance) {
		this.unDisbursementsBalance = unDisbursementsBalance;
	}

	public void setFixerate(boolean fixerate) {
		this.fixerate = fixerate;
	}

	public Collection<AmpCurrency> getValidcurrencies() {
		return validcurrencies;
	}

	public void setValidcurrencies(Collection<AmpCurrency> validcurrencies) {
		this.validcurrencies = validcurrencies;
	}

	public boolean isDupFunding() {
		return dupFunding;
	}

	public void setDupFunding(boolean dupFunding) {
		this.dupFunding = dupFunding;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public Long getAssistanceType() {
		return assistanceType;
	}

	public void setAssistanceType(Long assistanceType) {
		this.assistanceType = assistanceType;
	}

	public Long getModality() {
		return modality;
	}

	public void setModality(Long modality) {
		this.modality = modality;
	}

	/**
	 * @return the fundingStatus
	 */
	public Long getFundingStatus() {
		return fundingStatus;
	}

	/**
	 * @param fundingStatus the fundingStatus to set
	 */
	public void setFundingStatus(Long fundingStatus) {
		this.fundingStatus = fundingStatus;
	}

//	public List<MTEFProjection> getFundingMTEFProjections() {
//		return fundingMTEFProjections;
//	}
//
//	public void setFundingMTEFProjections(List<MTEFProjection> fundingMTEFProjections) {
//		this.fundingMTEFProjections = fundingMTEFProjections;
//	}
	

	/**
	 * @return the availableMTEFProjectionYears
	 */
	public List<KeyValue> getAvailableMTEFProjectionYears() {
		return availableMTEFProjectionYears;
	}

	/**
	 * @param availableMTEFProjectionYears the availableMTEFProjectionYears to set
	 */
	public void setAvailableMTEFProjectionYears(
			List<KeyValue> availableMTEFProjectionYears) {
		this.availableMTEFProjectionYears = availableMTEFProjectionYears;
	}

	public Collection getProjections() {
		return projections;
	}

	public void setProjections(Collection projections) {
		this.projections = projections;
	}

	public String getOrgFundingId() {
		return orgFundingId;
	}

	public void setOrgFundingId(String orgFundingId) {
		this.orgFundingId = orgFundingId;
	}

	public Collection getOrganizations() {
		return organizations;
	}

	public void setOrganizations(Collection organizations) {
		this.organizations = organizations;
	}

	public int getNumDisbOrder() {
		return numDisbOrder;
	}

	public void setNumDisbOrder(int numDisbOrder) {
		this.numDisbOrder = numDisbOrder;
	}

	public String getDisbOrderId() {
		return disbOrderId;
	}

	public void setDisbOrderId(String disbOrderId) {
		this.disbOrderId = disbOrderId;
	}

	public String getSignatureDate() {
		return signatureDate;
	}

	public void setSignatureDate(String signatureDate) {
		this.signatureDate = signatureDate;
	}

	public String getPlannedStartDate() {
		return plannedStartDate;
	}

	public void setPlannedStartDate(String plannedStartDate) {
		this.plannedStartDate = plannedStartDate;
	}

	public String getPlannedCompletionDate() {
		return plannedCompletionDate;
	}

	public void setPlannedCompletionDate(String plannedCompletionDate) {
		this.plannedCompletionDate = plannedCompletionDate;
	}

	public String getActualStartDate() {
		return actualStartDate;
	}

	public void setActualStartDate(String actualStartDate) {
		this.actualStartDate = actualStartDate;
	}

	public String getActualCompletionDate() {
		return actualCompletionDate;
	}

	public void setActualCompletionDate(String actualCompletionDate) {
		this.actualCompletionDate = actualCompletionDate;
	}

	public String getFundingConditions() {
		return fundingConditions;
	}

	public void setFundingConditions(String fundingConditions) {
		this.fundingConditions = fundingConditions;
	}

	public boolean isFirstSubmit() {
		return firstSubmit;
	}

	public void setFirstSubmit(boolean firstSubmit) {
		this.firstSubmit = firstSubmit;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public boolean isEditFunding() {
		return editFunding;
	}

	public void setEditFunding(boolean editFunding) {
		this.editFunding = editFunding;
	}

	public String getReportingDate() {
		return reportingDate;
	}

	public void setReportingDate(String reportingDate) {
		this.reportingDate = reportingDate;
	}

	public String getDonorObjective() {
		return donorObjective;
	}

	public void setDonorObjective(String donorObjective) {
		this.donorObjective = donorObjective;
	}

	public Long getFundDonor() {
		return fundDonor;
	}

	public void setFundDonor(Long fundDonor) {
		this.fundDonor = fundDonor;
	}

	public Long getFundingId() {
		return fundingId;
	}

	public void setFundingId(Long fundingId) {
		this.fundingId = fundingId;
	}

	public Long getFundingRegionId() {
		return fundingRegionId;
	}

	public void setFundingRegionId(Long fundingRegionId) {
		this.fundingRegionId = fundingRegionId;
	}

	public double getRegionTotalDisb() {
		return regionTotalDisb;
	}

	public void setRegionTotalDisb(double regionTotalDisb) {
		this.regionTotalDisb = regionTotalDisb;
	}

	public Collection getOrderedFundingOrganizations() {
		return orderedFundingOrganizations;
	}

	public void setOrderedFundingOrganizations(Collection orderedFundingOrganizations) {
		this.orderedFundingOrganizations = orderedFundingOrganizations;
	}


	public Collection getFinancingBreakdown() {
		return financingBreakdown;
	}

	public void setFinancingBreakdown(Collection financingBreakdown) {
		this.financingBreakdown = financingBreakdown;
	}

	public Collection getRegionalFundings() {
		return regionalFundings;
	}

	public void setRegionalFundings(Collection regionalFundings) {
		this.regionalFundings = regionalFundings;
	}

	public Long[] getSelFundingOrgs() {
		return selFundingOrgs;
	}

	public void setSelFundingOrgs(Long[] selFundingOrgs) {
		this.selFundingOrgs = selFundingOrgs;
	}



	public Long[] getSelRegFundings() {
		return selRegFundings;
	}

	public void setSelRegFundings(Long[] selRegFundings) {
		this.selRegFundings = selRegFundings;
	}

	public long getTransIndexId() {
		return transIndexId;
	}

	public void setTransIndexId(long transIndexId) {
		this.transIndexId = transIndexId;
	}

	/**
	 * @return the selectedMTEFProjectionYear
	 */
	public int getSelectedMTEFProjectionYear() {
		return selectedMTEFProjectionYear;
	}

	/**
	 * @param selectedMTEFProjectionYear the selectedMTEFProjectionYear to set
	 */
	public void setSelectedMTEFProjectionYear(int selectedMTEFProjectionYear) {
		this.selectedMTEFProjectionYear = selectedMTEFProjectionYear;
	}

	public String getTotalPipelineCommitments() {
		return totalPipelineCommitments;
	}

	public void setTotalPipelineCommitments(String totalPipelineCommitments) {
		this.totalPipelineCommitments = totalPipelineCommitments;
	}

	/**
	 * @return the modeOfPayment
	 */
	public Long getModeOfPayment() {
		return modeOfPayment;
	}

	/**
	 * @param modeOfPayment the modeOfPayment to set
	 */
	public void setModeOfPayment(Long modeOfPayment) {
		this.modeOfPayment = modeOfPayment;
	}

	/**
	 * @return the sourceRole
	 */
	public String getSourceRole() {
		return sourceRole;
	}

	/**
	 * @param sourceRole the sourceRole to set
	 */
	public void setSourceRole(String sourceRole) {
		this.sourceRole = sourceRole;
	}	
	
	public String getTotalMtefProjections()
	{
		return totalMtefProjections;
	}
	
	private void setTotalMtefProjections(String totalMtefProjections)
	{
		this.totalMtefProjections = totalMtefProjections;
	}

	public String getTotalOdaSscCommitments() {
		return totalOdaSscCommitments;
	}

	public void setTotalOdaSscCommitments(String totalOdaSscCommitments) {
		this.totalOdaSscCommitments = totalOdaSscCommitments;
	}

	public String getTotalTriangularSscCommitments() {
		return totalTriangularSscCommitments;
	}

	public void setTotalTriangularSscCommitments(
			String totalTriangularSscCommitments) {
		this.totalTriangularSscCommitments = totalTriangularSscCommitments;
	}

	public String getTotalBilateralSscCommitments() {
		return totalBilateralSscCommitments;
	}

	public void setTotalBilateralSscCommitments(
			String totalBilateralSscCommitments) {
		this.totalBilateralSscCommitments = totalBilateralSscCommitments;
	}
	
}

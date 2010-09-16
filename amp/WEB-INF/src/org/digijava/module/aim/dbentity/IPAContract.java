package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.common.util.DateTimeUtil;

/**
 * @author mihai
 *
 */
public class IPAContract implements Serializable {

    private static final long serialVersionUID = 2485772788422409800L;
    private Long id;
    private String contractName;
    private String description;
    private String contractingOrganizationText;
    private AmpCategoryValue activityCategory;
    private Date startOfTendering;
    private Date signatureOfContract;
    private Date contractValidity;
    private Date contractCompletion;
    
    
    
    private Date totalPrivateContribAmountDate;
    private Date totalNationalContribRegionalAmountDate;
    private Date totalNationalContribIFIAmountDate;
    private Date totalNationalContribCentralAmountDate;
    private Date totalECContribINVAmountDate;
    private Date totalECContribIBAmountDate;
    
    private Double totalECContribIBAmount;
    private Double totalAmount;
    private Double contractTotalValue;
    private AmpCurrency totalAmountCurrency;
    private AmpCurrency dibusrsementsGlobalCurrency;
    private Double totalECContribINVAmount;
    private Double totalNationalContribCentralAmount;
    private Double totalNationalContribRegionalAmount;
    private Double totalNationalContribIFIAmount;
    private Double totalPrivateContribAmount;
    private Set disbursements;
    private AmpActivity activity;
    private AmpOrganisation organization;
    private Set<AmpOrganisation> organizations;
    private AmpCategoryValue status;
    //this disbursements and executionRate are used in Montenegro
    private Double totalDisbursements;
    private Double executionRate;
    //burkina mission - exchange rate is computed based on the disbursement entered in funding step and total amount
    //of the contract (contractTotalValue)
    private Double fundingTotalDisbursements;
    private Double fundingExecutionRate;
    
    private AmpCategoryValue type;
    private AmpCategoryValue contractType;
    
    
    /**
     * 
     */
    private Double donorContractFundinAmount;
    private AmpCurrency donorContractFundingCurrency;
    
    private Double totAmountDonorContractFunding;
    private AmpCurrency totalAmountCurrencyDonor; 
    
    private Double totAmountCountryContractFunding;
    private AmpCurrency totalAmountCurrencyCountry;  
    
    private Set amendments;    
    /**
     * 
     */
    
    public AmpCategoryValue getType() {
        return type;
    }

    public void setType(AmpCategoryValue type) {
        this.type = type;
    }

    public AmpCategoryValue getStatus() {
        return status;
    }

    public void setStatus(AmpCategoryValue status) {
        this.status = status;
    }

    public AmpOrganisation getOrganization() {
        return organization;
    }

    public void setOrganization(AmpOrganisation organization) {
        this.organization = organization;
    }

    public AmpActivity getActivity() {
        return activity;
    }

    public void setActivity(AmpActivity activity) {
        this.activity = activity;
    }

    public AmpCategoryValue getActivityCategory() {
        return activityCategory;
    }

    public void setActivityCategory(AmpCategoryValue activityCategory) {
        this.activityCategory = activityCategory;
    }

    public Date getContractCompletion() {
        return contractCompletion;
    }

    public void setContractCompletion(Date contractCompletion) {
        this.contractCompletion = contractCompletion;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getDescription() {
        return description;
    }

    public String getFormattedStartOfTendering() {
        String formatted = "";
        try{
        if (startOfTendering != null) {
            formatted = DateTimeUtil.parseDateForPicker2(startOfTendering);
        }
        }
        catch(Exception ex){
            Logger.getLogger(IPAContract.class.getName()).log(Level.SEVERE, null, ex);
        }
        return formatted;
    }

    public String getFormattedContractValidity() {
        String formatted = "";
        try {
            if (contractValidity != null) {
                formatted = DateTimeUtil.parseDateForPicker2(contractValidity);
            }
        } catch (Exception ex) {
            Logger.getLogger(IPAContract.class.getName()).log(Level.SEVERE, null, ex);
        }

        return formatted;

    }

    public String getFormattedSignatureOfContract() {
        String formatted = "";
        try {
            if (signatureOfContract != null) {
                formatted = DateTimeUtil.parseDateForPicker2(signatureOfContract);
            }
        } catch (Exception ex) {
            Logger.getLogger(IPAContract.class.getName()).log(Level.SEVERE, null, ex);
        }

        return formatted;

    }
    
    public String getFormattedContractCompletion() {
        String formatted = "";
        try {
            if (contractCompletion != null) {
                formatted = DateTimeUtil.parseDateForPicker2(contractCompletion);
            }
        } catch (Exception ex) {
            Logger.getLogger(IPAContract.class.getName()).log(Level.SEVERE, null, ex);
        }
        return formatted;

    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set getDisbursements() {
        return disbursements;
    }

    public void setDisbursements(Set disbursements) {
        this.disbursements = disbursements;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getSignatureOfContract() {
        return signatureOfContract;
    }

    public void setSignatureOfContract(Date signatureOfContract) {
        this.signatureOfContract = signatureOfContract;
    }

    public Date getStartOfTendering() {
        return startOfTendering;
    }

    public void setStartOfTendering(Date startOfTendering) {
        this.startOfTendering = startOfTendering;
    }

    public Double getTotalECContribIBAmount() {
        return FeaturesUtil.applyThousandsForVisibility(totalECContribIBAmount);
    }

    public void setTotalECContribIBAmount(Double totalECContribIBAmount) {
        this.totalECContribIBAmount = FeaturesUtil.applyThousandsForEntry(totalECContribIBAmount);
    }
    
    public Double getTotalECContribINVAmount() {
        return FeaturesUtil.applyThousandsForVisibility(totalECContribINVAmount);
    }

    public void setTotalECContribINVAmount(Double totalECContribINVAmount) {
        this.totalECContribINVAmount = FeaturesUtil.applyThousandsForEntry(totalECContribINVAmount);
    }

    public Double getTotalNationalContribCentralAmount() {
        return FeaturesUtil.applyThousandsForVisibility(totalNationalContribCentralAmount);
    }

    public void setTotalNationalContribCentralAmount(
            Double totalNationalContribCentralAmount) {
        this.totalNationalContribCentralAmount = FeaturesUtil.applyThousandsForEntry(totalNationalContribCentralAmount);
    }

    public Double getTotalNationalContribIFIAmount() {
        return FeaturesUtil.applyThousandsForVisibility(totalNationalContribIFIAmount);
    }

    public void setTotalNationalContribIFIAmount(
            Double totalNationalContribIFIAmount) {
        this.totalNationalContribIFIAmount = FeaturesUtil.applyThousandsForEntry(totalNationalContribIFIAmount);
    }

    public Double getTotalNationalContribRegionalAmount() {
        return FeaturesUtil.applyThousandsForVisibility(totalNationalContribRegionalAmount);
    }

    public void setTotalNationalContribRegionalAmount(
            Double totalNationalContribRegionalAmount) {
        this.totalNationalContribRegionalAmount = FeaturesUtil.applyThousandsForEntry(totalNationalContribRegionalAmount);
    }

    public Double getTotalPrivateContribAmount() {
        return FeaturesUtil.applyThousandsForVisibility(totalPrivateContribAmount);
    }

    public void setTotalPrivateContribAmount(Double totalPrivateContribAmount) {
        this.totalPrivateContribAmount = FeaturesUtil.applyThousandsForEntry(totalPrivateContribAmount);
    }

	public Date getContractValidity() {
		return contractValidity;
	}

	public void setContractValidity(Date contractValidity) {
		this.contractValidity = contractValidity;
	}

	public Double getTotalAmount() {
		return FeaturesUtil.applyThousandsForVisibility(totalAmount);
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = FeaturesUtil.applyThousandsForEntry(totalAmount);
	}

	public AmpCurrency getTotalAmountCurrency() {
		return totalAmountCurrency;
	}

	public void setTotalAmountCurrency(AmpCurrency totalAmountCurrency) {
		this.totalAmountCurrency = totalAmountCurrency;
	}

	public Double getTotalDisbursements() {
		return FeaturesUtil.applyThousandsForVisibility(totalDisbursements);
	}

	public void setTotalDisbursements(Double totalDisbursements) {
		this.totalDisbursements = FeaturesUtil.applyThousandsForEntry(totalDisbursements);
	}

	public Double getExecutionRate() {
		return executionRate;
	}

	public void setExecutionRate(Double executionRate) {
		this.executionRate = executionRate;
	}

	public AmpCurrency getDibusrsementsGlobalCurrency() {
		return dibusrsementsGlobalCurrency;
	}

	public void setDibusrsementsGlobalCurrency(
			AmpCurrency dibusrsementsGlobalCurrency) {
		this.dibusrsementsGlobalCurrency = dibusrsementsGlobalCurrency;
	}

	public String getContractingOrganizationText() {
		return contractingOrganizationText;
	}

	public void setContractingOrganizationText(String contractingOrganizationText) {
		this.contractingOrganizationText = contractingOrganizationText;
	}

	public Set<AmpOrganisation> getOrganizations() {
		return organizations;
	}

	public void setOrganizations(Set<AmpOrganisation> organizations) {
		this.organizations = organizations;
	}

	public Date getTotalPrivateContribAmountDate() {
		return totalPrivateContribAmountDate;
	}

	public void setTotalPrivateContribAmountDate(Date totalPrivateContribAmountDate) {
		this.totalPrivateContribAmountDate = totalPrivateContribAmountDate;
	}

	public Date getTotalNationalContribRegionalAmountDate() {
		return totalNationalContribRegionalAmountDate;
	}

	public void setTotalNationalContribRegionalAmountDate(
			Date totalNationalContribRegionalAmountDate) {
		this.totalNationalContribRegionalAmountDate = totalNationalContribRegionalAmountDate;
	}

	public Date getTotalNationalContribIFIAmountDate() {
		return totalNationalContribIFIAmountDate;
	}

	public void setTotalNationalContribIFIAmountDate(
			Date totalNationalContribIFIAmountDate) {
		this.totalNationalContribIFIAmountDate = totalNationalContribIFIAmountDate;
	}

	public Date getTotalNationalContribCentralAmountDate() {
		return totalNationalContribCentralAmountDate;
	}

	public void setTotalNationalContribCentralAmountDate(
			Date totalNationalContribCentralAmountDate) {
		this.totalNationalContribCentralAmountDate = totalNationalContribCentralAmountDate;
	}

	public Date getTotalECContribINVAmountDate() {
		return totalECContribINVAmountDate;
	}

	public void setTotalECContribINVAmountDate(Date totalECContribINVAmountDate) {
		this.totalECContribINVAmountDate = totalECContribINVAmountDate;
	}

	public Date getTotalECContribIBAmountDate() {
		return totalECContribIBAmountDate;
	}

	public void setTotalECContribIBAmountDate(Date totalECContribIBAmountDate) {
		this.totalECContribIBAmountDate = totalECContribIBAmountDate;
	}

	public AmpCategoryValue getContractType() {
		return contractType;
	}

	public void setContractType(AmpCategoryValue contractType) {
		this.contractType = contractType;
	}

	public Double getContractTotalValue() {
		return FeaturesUtil.applyThousandsForVisibility(contractTotalValue);
	}

	public void setContractTotalValue(Double contractTotalValue) {
		this.contractTotalValue = FeaturesUtil.applyThousandsForEntry(contractTotalValue);
	}

	public Double getFundingTotalDisbursements() {
		return FeaturesUtil.applyThousandsForVisibility(fundingTotalDisbursements);
	}

	public void setFundingTotalDisbursements(Double fundingTotalDisbursements) {
		this.fundingTotalDisbursements = FeaturesUtil.applyThousandsForEntry(fundingTotalDisbursements);
	}

	public Double getFundingExecutionRate() {
		return fundingExecutionRate;
	}

	public void setFundingExecutionRate(Double fundingExecutionRate) {
		this.fundingExecutionRate = fundingExecutionRate;
	}

	public Double getDonorContractFundinAmount() {
		return donorContractFundinAmount;
	}

	public void setDonorContractFundinAmount(Double donorContractFundinAmount) {
		this.donorContractFundinAmount = donorContractFundinAmount;
	}

	public AmpCurrency getDonorContractFundingCurrency() {
		return donorContractFundingCurrency;
	}

	public void setDonorContractFundingCurrency(
			AmpCurrency donorContractFundingCurrency) {
		this.donorContractFundingCurrency = donorContractFundingCurrency;
	}

	public Double getTotAmountDonorContractFunding() {
		return totAmountDonorContractFunding;
	}

	public void setTotAmountDonorContractFunding(
			Double totAmountDonorContractFunding) {
		this.totAmountDonorContractFunding = totAmountDonorContractFunding;
	}

	public AmpCurrency getTotalAmountCurrencyDonor() {
		return totalAmountCurrencyDonor;
	}

	public void setTotalAmountCurrencyDonor(AmpCurrency totalAmountCurrencyDonor) {
		this.totalAmountCurrencyDonor = totalAmountCurrencyDonor;
	}

	public Double getTotAmountCountryContractFunding() {
		return totAmountCountryContractFunding;
	}

	public void setTotAmountCountryContractFunding(
			Double totAmountCountryContractFunding) {
		this.totAmountCountryContractFunding = totAmountCountryContractFunding;
	}

	public AmpCurrency getTotalAmountCurrencyCountry() {
		return totalAmountCurrencyCountry;
	}

	public void setTotalAmountCurrencyCountry(AmpCurrency totalAmountCurrencyCountry) {
		this.totalAmountCurrencyCountry = totalAmountCurrencyCountry;
	}

	public Set getAmendments() {
		return amendments;
	}

	public void setAmendments(Set amendments) {
		this.amendments = amendments;
	}
		
}

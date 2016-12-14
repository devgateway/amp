package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.Output;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.common.util.DateTimeUtil;

/**
 * @author mihai
 *
 */
@TranslatableClass (displayName = "IPA Contract")
public class IPAContract implements Serializable, Versionable, Cloneable {

	/**
	 * Multiple fields marked as "transient" in order to fool the
	 * Wicket serializer checker. Marking a field as "transient" 
	 * shouldn't affect Hibernate which has it's own way of identifying
	 * a transient field through the "@Transient" adnotation.
	 */
	
	private static final long serialVersionUID = 2485772788422409800L;
	private Long id;
	@TranslatableField
//	@Interchangeable(fieldTitle = "Contract Name", fmPath = "/Activity Form/Contracts/Contract Item/Contract Info/Contract Name")
	private String contractName;

	@TranslatableField
//	@Interchangeable(fieldTitle = "Description", fmPath = "/Activity Form/Contracts/Contract Item/Contract Info/Contract Description")
	private String description;
//	@Interchangeable(fieldTitle = "Contracting Organization Text")
	private String contractingOrganizationText;
//	@Interchangeable(fieldTitle = "IPA activity categ")
	private transient AmpCategoryValue activityCategory;
//	@Interchangeable(fieldTitle = "Start of Tendering", fmPath = "/Activity Form/Contracts/Contract Item/Contract Details/Start of Tendering")
	private Date startOfTendering;
//	@Interchangeable(fieldTitle = "Signature of Contract", fmPath = "/Activity Form/Contracts/Contract Item/Contract Details/Signature")
	private Date signatureOfContract;
//	@Interchangeable(fieldTitle = "Contract Validity", fmPath = "/Activity Form/Contracts/Contract Item/Contract Details/Validity")
	private Date contractValidity;
//	@Interchangeable(fieldTitle = "Contract Completion", fmPath = "/Activity Form/Contracts/Contract Item/Contract Details/Completion")
	private Date contractCompletion;

//	@Interchangeable(fieldTitle = "Total Private Contrib Amount Date", fmPath = "/Activity Form/Contracts/Contract Item/Funding Allocation/IB Date")
	private Date totalPrivateContribAmountDate;
//	@Interchangeable(fieldTitle = "Total National Contrib Regional Amount Date", fmPath = "/Activity Form/Contracts/Contract Item/Funding Allocation/Regional Date")
	private Date totalNationalContribRegionalAmountDate;
//	@Interchangeable(fieldTitle = "Total NationalContrib IFI Amount Date", fmPath = "/Activity Form/Contracts/Contract Item/Funding Allocation/IFI Date")
	private Date totalNationalContribIFIAmountDate;
//	@Interchangeable(fieldTitle = "Total National Contrib Central Amount Date", fmPath = "/Activity Form/Contracts/Contract Item/Funding Allocation/Central Date")
	private Date totalNationalContribCentralAmountDate;
//	@Interchangeable(fieldTitle = "Total EC Contrib INV Amount Date", fmPath = "/Activity Form/Contracts/Contract Item/Funding Allocation/INV Date")
	private Date totalECContribINVAmountDate;
//	@Interchangeable(fieldTitle = "Total EC Contrib IB Amount Date", fmPath = "/Activity Form/Contracts/Contract Item/Funding Allocation/IB Date")
	private Date totalECContribIBAmountDate;

//	@Interchangeable(fieldTitle = "Total EC Contrib IB Amount", fmPath = "/Activity Form/Contracts/Contract Item/Funding Allocation/IB Amount")
	private Double totalECContribIBAmount;
//	@Interchangeable(fieldTitle = "Total Amount", fmPath = "/Activity Form/Contracts/Contract Item/Funding Allocation/Contract Total Amount")
	private Double totalAmount;
//	@Interchangeable(fieldTitle = "Contract Total Value", fmPath = "/Activity Form/Contracts/Contract Item/Funding Allocation/Contract Total Value")
	private Double contractTotalValue;
//	@Interchangeable(fieldTitle = "Total Amount Currency", fmPath = "/Activity Form/Contracts/Contract Item/Funding Allocation/Currency")
	private transient AmpCurrency totalAmountCurrency;
//	@Interchangeable(fieldTitle = "Disbursements Global Currency")
	private transient AmpCurrency dibusrsementsGlobalCurrency;
//	@Interchangeable(fieldTitle = "Total EC Contrib INV Amount", fmPath = "/Activity Form/Contracts/Contract Item/Funding Allocation/INV Amount")
	private Double totalECContribINVAmount;
//	@Interchangeable(fieldTitle = "Total National Contrib Central Amount", fmPath = "/Activity Form/Contracts/Contract Item/Funding Allocation/Central Amount")
	private Double totalNationalContribCentralAmount;
//	@Interchangeable(fieldTitle = "Total National Contrib Regional Amount", fmPath = "/Activity Form/Contracts/Contract Item/Funding Allocation/Regional Amount")
	private Double totalNationalContribRegionalAmount;
//	@Interchangeable(fieldTitle = "Total National Contrib IFI Amount", fmPath = "/Activity Form/Contracts/Contract Item/Funding Allocation/IFI Amount")
	private Double totalNationalContribIFIAmount;
//	@Interchangeable(fieldTitle = "Total Private Contrib Amount", fmPath = "/Activity Form/Contracts/Contract Item/Funding Allocation/IB Amount")
	private Double totalPrivateContribAmount;
//	@Interchangeable(fieldTitle = "Disbursements", fmPath = "/Activity Form/Contracts/Contract Item/Contract Disbursements")
	private transient Set<IPAContractDisbursement> disbursements;

	// @Interchangeable(fieldTitle = "Activity")
	private transient AmpActivityVersion activity;

//	@Interchangeable(fieldTitle = "Organization", pickIdOnly = true)
	private transient AmpOrganisation organization;
//	@Interchangeable(fieldTitle = "Organizations", fmPath = "/Activity Form/Contracts/Contract Item/Contract Organizations")
//	@Validators(unique = "/Activity Form/Organizations/Donor Organization/Unique Orgs Validator")
	private transient Set<AmpOrganisation> organizations;
//	@Interchangeable(fieldTitle = "IPA Status", fmPath = "/Activity Form/Contracts/Contract Item/Contract Details/Status")
	private transient AmpCategoryValue status;
	// this disbursements and executionRate are used in Montenegro
//	@Interchangeable(fieldTitle = "Total Disbursements")
	private Double totalDisbursements;
//	@Interchangeable(fieldTitle = "Execution Rate")
	private Double executionRate;
	// burkina mission - exchange rate is computed based on the disbursement
	// entered in funding step and total amount
	// of the contract (contractTotalValue)
//	@Interchangeable(fieldTitle = "Funding Total Disbursements")
	private Double fundingTotalDisbursements;
//	@Interchangeable(fieldTitle = "Funding Execution Rate")
	private Double fundingExecutionRate;
//	@Interchangeable(fieldTitle = "IPA Activity Type", fmPath = "/Activity Form/Contracts/Contract Item/Contract Info/Activity Type")
	private transient AmpCategoryValue type;
	//ATTENTION: no amp_category_classs with such a value!
//	@Interchangeable(fieldTitle = "Contract Type", fmPath = "/Activity Form/Contracts/Contract Item/Contract Info/Contract Type")
	private transient AmpCategoryValue contractType;

	/**
    * 
    */
//	@Interchangeable(fieldTitle = "Donor Contract Funding Amount")
	private Double donorContractFundinAmount;
//	@Interchangeable(fieldTitle = "Donor Contract Funding Currency")
	private transient AmpCurrency donorContractFundingCurrency;

//	@Interchangeable(fieldTitle = "Total Amount Donor Contract Funding")
	private Double totAmountDonorContractFunding;
//	@Interchangeable(fieldTitle = "Total Amount Currency Donor")
	private transient AmpCurrency totalAmountCurrencyDonor;

//	@Interchangeable(fieldTitle = "Total Amount Country Contract Funding")
	private Double totAmountCountryContractFunding;
//	@Interchangeable(fieldTitle = "Total Amount Currency Country")
	private transient AmpCurrency totalAmountCurrencyCountry;   

    
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

    public AmpActivityVersion getActivity() {
        return activity;
    }

    public void setActivity(AmpActivityVersion activity) {
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
        if (startOfTendering != null) {
            formatted = DateTimeUtil.formatDateForPicker2(startOfTendering, null);
        }
        return formatted;
    }

    public String getFormattedContractValidity() {
        String formatted = "";
		if (contractValidity != null) {
			formatted = DateTimeUtil.formatDateForPicker2(contractValidity, null);
		}
        return formatted;
    }

    public String getFormattedSignatureOfContract() {
        String formatted = "";
		if (signatureOfContract != null) {
			formatted = DateTimeUtil.formatDateForPicker2(signatureOfContract, null);
		}
        return formatted;
    }
    
    public String getFormattedContractCompletion() {
        String formatted = "";
		if (contractCompletion != null) {
			formatted = DateTimeUtil.formatDateForPicker2(contractCompletion, null);
		}
        return formatted;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<IPAContractDisbursement> getDisbursements() {
        return disbursements;
    }

    public void setDisbursements(Set<IPAContractDisbursement> disbursements) {
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


	
	@Override
	public boolean equalsForVersioning(Object obj) {
		IPAContract auxIPA = (IPAContract) obj;
		if (auxIPA.getContractName().toLowerCase().trim().equals(this.contractName.toLowerCase().trim())) {
			return true;
		}
		return false;
	}

	@Override
	public Output getOutput() {
		Output out = new Output();
		out.setOutputs(new ArrayList<Output>());
		out.getOutputs().add(new Output(null, new String[] { "Contract Name" }, new Object[] { this.contractName }));
		if (this.description != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "Description" }, new Object[] { this.description }));
		}
		if (this.activityCategory != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "Category" }, new Object[] { this.activityCategory
							.getValue() }));
		}
		if (this.contractType != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "Type" }, new Object[] { this.contractType
							.getEncodedValue() }));
		}
		if (this.status != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "Status" }, new Object[] { this.status
							.getEncodedValue() }));
		}

		if (this.startOfTendering != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "Start of Tendering" },
							new Object[] { this.startOfTendering }));
		}
		if (this.signatureOfContract != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "Signature of Contract" },
							new Object[] { this.signatureOfContract }));
		}
		if (this.contractValidity != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "Contract Validity Date" },
							new Object[] { this.contractValidity }));
		}
		if (this.organizations != null) {
			Iterator<AmpOrganisation> iterOrgs = this.organizations.iterator();
			while (iterOrgs.hasNext()) {
				AmpOrganisation auxOrg = iterOrgs.next();
				out.getOutputs().add(
						new Output(null, new String[] { "Contract Organization" }, new Object[] { auxOrg
								.getAcronymAndName() }));
			}
		}
		if (this.contractingOrganizationText != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "Contractor Name" },
							new Object[] { this.contractingOrganizationText }));
		}
		if (this.contractCompletion != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "Contract Completion" },
							new Object[] { this.contractCompletion }));
		}
		if (this.totalAmount != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "Total Amount" }, new Object[] { this.totalAmount,
							" - ", (this.totalAmountCurrency != null ? this.totalAmountCurrency.getCurrencyCode() : "")}));
		}
		if (this.contractTotalValue != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "Contract Total Value" },
							new Object[] { this.contractTotalValue }));
		}
		if (this.totalECContribIBAmount != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "Total EC Contribution IB" }, new Object[] {
							this.totalECContribIBAmount, " - ", this.totalECContribIBAmountDate }));
		}
		if (this.totalECContribINVAmount != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "Total EC Contribution INV" }, new Object[] {
							this.totalECContribINVAmount, " - ", this.totalECContribINVAmountDate }));
		}
		if (this.totalNationalContribCentralAmount != null) {
			out.getOutputs()
					.add(
							new Output(null, new String[] { "Total National Central Contribution" },
									new Object[] { this.totalNationalContribCentralAmount, " - ",
											this.totalNationalContribCentralAmountDate }));
		}
		if (this.totalNationalContribIFIAmount != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "Total National IFIs Contribution" }, new Object[] {
							this.totalNationalContribIFIAmount, " - ", this.totalNationalContribIFIAmountDate }));
		}
		if (this.totalNationalContribRegionalAmount != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "Total National Regional Contribution" },
							new Object[] { this.totalNationalContribRegionalAmount, " - ",
									this.totalNationalContribRegionalAmountDate }));
		}
		if (this.totalPrivateContribAmount != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "Total Private Contribution" }, new Object[] {
							this.totalPrivateContribAmount, " - ", this.totalPrivateContribAmountDate }));
		}
		if (this.totalDisbursements != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "Total Disbursements" },
							new Object[] { this.totalDisbursements }));
		}
		if (this.executionRate != null) {
			out.getOutputs()
					.add(
							new Output(null, new String[] { "Execution Rate" },
									new Object[] { this.executionRate }));
		}
		if (this.fundingTotalDisbursements != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "Total Funding Disbursements" },
							new Object[] { this.fundingTotalDisbursements }));
		}
		if (this.disbursements != null) {
			List auxDisbList = new ArrayList(this.disbursements);
			Collections.sort(auxDisbList, disbComparator);
			Iterator<IPAContractDisbursement> iterDisb = auxDisbList.iterator();
			while (iterDisb.hasNext()) {
				IPAContractDisbursement auxDisb = iterDisb.next();
				out.getOutputs().add(
						new Output(null, new String[] { "Disbursements" }, new Object[] {
								auxDisb.getAdjustmentType().getValue(), " - ", auxDisb.getAmount(),
								" - ", auxDisb.getCurrency(), " - ", auxDisb.getDate() }));
			}
		}
		/*
		 * Amendments don't seem to be used no more
		if (this.amendments != null) {
			List auxAmendList = new ArrayList(this.amendments);
			Collections.sort(auxAmendList, amendComparator);
			Iterator<IPAContractAmendment> iterAmend = auxAmendList.iterator();
			while (iterAmend.hasNext()) {
				IPAContractAmendment auxAmend = iterAmend.next();
				out.getOutputs().add(
						new Output(null, new String[] { "<br />", "Amendments:&nbsp;" }, new Object[] {
								auxAmend.getAmount(), " - ", auxAmend.getCurrency(), " - ", auxAmend.getDate() }));
			}
		}
		 */
		return out;
	}

	@Override
	public Object getValue() {
		String ret = "";
		ret = ret + this.description + "-" + this.contractCompletion + "-" + this.contractTotalValue + "-"
				+ (this.contractType != null ? this.contractType.getEncodedValue() : "") + "-"
				+ (this.activityCategory != null ? this.activityCategory.getEncodedValue() : "") + "-"
				+ this.contractValidity + "-"
				+ (this.dibusrsementsGlobalCurrency != null ? this.dibusrsementsGlobalCurrency.getCurrencyCode() : "")
				+ "-" + this.executionRate + "-" + this.fundingExecutionRate + "-" + this.fundingTotalDisbursements
				+ "-" + (this.organization != null ? this.organization.getOrgCode() : "") + "-"
				+ this.signatureOfContract + "-" + this.startOfTendering + "-"
				+ (this.status != null ? this.status.getEncodedValue() : "") + "-" + this.totalAmount + "-"
				+ this.totalAmountCurrency + "-" + this.totalDisbursements + "-" + this.totalECContribIBAmount + "-"
				+ this.totalECContribIBAmountDate + "-" + this.totalECContribINVAmount + "-"
				+ this.totalECContribINVAmountDate + "-" + this.totalNationalContribCentralAmount + "-"
				+ this.totalNationalContribCentralAmountDate + "-" + this.totalNationalContribIFIAmount + "-"
				+ this.totalNationalContribIFIAmountDate + "-" + this.totalNationalContribRegionalAmount + "-"
				+ this.totalNationalContribRegionalAmountDate + "-" + this.totalPrivateContribAmount + "-"
				+ this.totalPrivateContribAmountDate + (this.type != null ? this.type.getEncodedValue() : "");
		if (this.disbursements != null) {
			List auxDisbList = new ArrayList(this.disbursements);
			Collections.sort(auxDisbList, disbComparator);
			Iterator<IPAContractDisbursement> iterDisb = auxDisbList.iterator();
			while (iterDisb.hasNext()) {
				IPAContractDisbursement auxDisb = iterDisb.next();
				ret = ret + auxDisb.getAdjustmentType() + "-" + auxDisb.getAmount() + "-" + auxDisb.getCurrency() + "-"
						+ auxDisb.getDate();
			}
		}
		/*
		if (this.amendments != null) {
			List auxAmendList = new ArrayList(this.amendments);
			Collections.sort(auxAmendList, amendComparator);
			Iterator<IPAContractAmendment> iterDisb = auxAmendList.iterator();
			while (iterDisb.hasNext()) {
				IPAContractAmendment auxAmend = iterDisb.next();
				ret = ret + auxAmend.getAmount() + "-" + auxAmend.getCurrency() + "-" + auxAmend.getDate();
			}
		}
		*/
		return ret;
	}

	private transient Comparator disbComparator = new Comparator() {
		public int compare(Object o1, Object o2) {
			if( !(o1 instanceof IPAContractDisbursement) || !(o2 instanceof IPAContractDisbursement) ) return -1;
			IPAContractDisbursement aux1 = (IPAContractDisbursement) o1;
			IPAContractDisbursement aux2 = (IPAContractDisbursement) o2;
			if (aux1.getAdjustmentType().equals(aux2.getAdjustmentType())) {
				if (aux1.getAmount().equals(aux2.getAmount())) {
					return aux1.getDate().compareTo(aux2.getDate());
				} else {
					return aux1.getAmount().compareTo(aux2.getAmount());
				}
			} else {
				return aux1.getAdjustmentType().compareTo(aux2.getAdjustmentType());
			}
		}
	};
	
	private transient Comparator amendComparator = new Comparator() {
		public int compare(Object o1, Object o2) {
			if( !(o1 instanceof IPAContractAmendment) || !(o2 instanceof IPAContractAmendment) ) return -1;
			IPAContractAmendment aux1 = (IPAContractAmendment) o1;
			IPAContractAmendment aux2 = (IPAContractAmendment) o2;
			if (aux1.getReference().equals(aux2.getReference())) {
				if (aux1.getAmount().equals(aux2.getAmount())) {
					return aux1.getDate().compareTo(aux2.getDate());
				} else {
					return aux1.getAmount().compareTo(aux2.getAmount());
				}
			} else {
				return aux1.getReference().compareTo(aux2.getReference());
			}
		}
	};
	
	@Override
	public Object prepareMerge(AmpActivityVersion newActivity) throws CloneNotSupportedException {
		IPAContract aux = (IPAContract) clone();
		aux.activity = newActivity;
		aux.id = null;

		if (aux.getDisbursements() != null) {
			HashSet<IPAContractDisbursement> set = new HashSet<IPAContractDisbursement>();
			Iterator<IPAContractDisbursement> iterDisb = aux.getDisbursements().iterator();
			while (iterDisb.hasNext()) {
				IPAContractDisbursement auxDisb = iterDisb.next();
				IPAContractDisbursement newIPADisb = (IPAContractDisbursement) auxDisb.clone();
				newIPADisb.setId(null);
				newIPADisb.setContract(aux);
				set.add(auxDisb);
			}
			
			if (set.size() > 0)
				aux.disbursements = set;
			else
				aux.disbursements = null;
		}
		
		if (aux.organizations != null){
			HashSet<AmpOrganisation> set = new HashSet<AmpOrganisation>();
			Iterator<AmpOrganisation> it = aux.organizations.iterator();
			while (it.hasNext()) {
				AmpOrganisation org = (AmpOrganisation) it.next();
				set.add(org);
			}
			if (set.size() > 0)
				aux.organizations = set;
			else
				aux.organizations = null;
		}
		
		Set funding = newActivity.getFunding();
        if (funding != null){
            Iterator it = funding.iterator();
            while (it.hasNext()){
                AmpFunding fun = (AmpFunding) it.next();
                Set<AmpFundingDetail> fdSet = fun.getFundingDetails();
                if (fdSet != null){
                    for (AmpFundingDetail fd: fdSet){
                        if (fd.getContract() != null && this.equals(fd.getContract()))
                            fd.setContract(aux);
                    }
                }
 
            }
        }
		
		/*
		 * Amendments don't seem to be used no more
		if (aux.getAmendments() != null) {
			Iterator<IPAContractAmendment> iterAmend = aux.getAmendments().iterator();
			while (iterAmend.hasNext()) {
				IPAContractAmendment auxAmend = iterAmend.next();
				IPAContractAmendment newIPAAmend = (IPAContractAmendment) auxAmend.clone();
				newIPAAmend.setId(null);
				newIPAAmend.setContract(aux);
			}
		}
		*/

		
		return aux;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
	
	@Override
	public String toString() {
		return (contractName == null ? "" : contractName);
	}
}

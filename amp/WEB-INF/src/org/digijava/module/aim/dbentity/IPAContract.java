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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.Output;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.common.util.DateTimeUtil;

/**
 * @author mihai
 *
 */
public class IPAContract implements Serializable, Versionable, Cloneable {

	/**
	 * Multiple fields marked as "transient" in order to fool the
	 * Wicket serializer checker. Marking a field as "transient" 
	 * shouldn't affect Hibernate which has it's own way of identifying
	 * a transient field through the "@Transient" adnotation.
	 */
	
    private static final long serialVersionUID = 2485772788422409800L;
    private Long id;
    private String contractName;
    private String description;
    private String contractingOrganizationText;
    private transient AmpCategoryValue activityCategory;
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
    private transient AmpCurrency totalAmountCurrency;
    private transient AmpCurrency dibusrsementsGlobalCurrency;
    private Double totalECContribINVAmount;
    private Double totalNationalContribCentralAmount;
    private Double totalNationalContribRegionalAmount;
    private Double totalNationalContribIFIAmount;
    private Double totalPrivateContribAmount;
    private transient Set disbursements;
    private transient AmpActivityVersion activity;
    private transient AmpOrganisation organization;
    private transient Set<AmpOrganisation> organizations;
    private transient AmpCategoryValue status;
    //this disbursements and executionRate are used in Montenegro
    private Double totalDisbursements;
    private Double executionRate;
    //burkina mission - exchange rate is computed based on the disbursement entered in funding step and total amount
    //of the contract (contractTotalValue)
    private Double fundingTotalDisbursements;
    private Double fundingExecutionRate;
    
    private transient AmpCategoryValue type;
    private transient AmpCategoryValue contractType;
    
    
    /**
     * 
     */
    private Double donorContractFundinAmount;
    private transient AmpCurrency donorContractFundingCurrency;
    
    private Double totAmountDonorContractFunding;
    private transient AmpCurrency totalAmountCurrencyDonor; 
    
    private Double totAmountCountryContractFunding;
    private transient AmpCurrency totalAmountCurrencyCountry;  
    
    /**
     * @Deprecated
     * They don't seem to be used no more
     */
    @Deprecated
    private transient Set amendments;    
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

	@Deprecated
	public Set getAmendments() {
		return amendments;
	}
	@Deprecated
	public void setAmendments(Set amendments) {
		this.amendments = amendments;
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
		out.getOutputs().add(new Output(null, new String[] { "Contract Name:&nbsp;" }, new Object[] { this.contractName }));
		if (this.description != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "<br />", "Description:&nbsp;" }, new Object[] { this.description }));
		}
		if (this.activityCategory != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "<br />", "Category:&nbsp;" }, new Object[] { this.activityCategory
							.getValue() }));
		}
		if (this.contractType != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "<br />", "Type:&nbsp;" }, new Object[] { this.contractType
							.getEncodedValue() }));
		}
		if (this.status != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "<br />", "Status:&nbsp;" }, new Object[] { this.status
							.getEncodedValue() }));
		}

		if (this.startOfTendering != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "<br />", "Start of Tendering:&nbsp;" },
							new Object[] { this.startOfTendering }));
		}
		if (this.signatureOfContract != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "<br />", "Signature of Contract:&nbsp;" },
							new Object[] { this.signatureOfContract }));
		}
		if (this.contractValidity != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "<br />", "Contract Validity Date:&nbsp;" },
							new Object[] { this.contractValidity }));
		}
		if (this.organizations != null) {
			Iterator<AmpOrganisation> iterOrgs = this.organizations.iterator();
			while (iterOrgs.hasNext()) {
				AmpOrganisation auxOrg = iterOrgs.next();
				out.getOutputs().add(
						new Output(null, new String[] { "<br />", "Contract Organization:&nbsp;" }, new Object[] { auxOrg
								.getAcronymAndName() }));
			}
		}
		if (this.contractingOrganizationText != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "<br />", "Contractor Name:&nbsp;" },
							new Object[] { this.contractingOrganizationText }));
		}
		if (this.contractCompletion != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "<br />", "Contract Completion:&nbsp;" },
							new Object[] { this.contractCompletion }));
		}
		if (this.totalAmount != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "<br />", "Total Amount:&nbsp;" }, new Object[] { this.totalAmount,
							" - ", this.totalAmountCurrency.getCurrencyCode() }));
		}
		if (this.contractTotalValue != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "<br />", "Contract Total Value:&nbsp;" },
							new Object[] { this.contractTotalValue }));
		}
		if (this.totalECContribIBAmount != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "<br />", "Total EC Contribution IB:&nbsp;" }, new Object[] {
							this.totalECContribIBAmount, " - ", this.totalECContribIBAmountDate }));
		}
		if (this.totalECContribINVAmount != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "<br />", "Total EC Contribution INV:&nbsp;" }, new Object[] {
							this.totalECContribINVAmount, " - ", this.totalECContribINVAmountDate }));
		}
		if (this.totalNationalContribCentralAmount != null) {
			out.getOutputs()
					.add(
							new Output(null, new String[] { "<br />", "Total National Central Contribution:&nbsp;" },
									new Object[] { this.totalNationalContribCentralAmount, " - ",
											this.totalNationalContribCentralAmountDate }));
		}
		if (this.totalNationalContribIFIAmount != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "<br />", "Total National IFIs Contribution:&nbsp;" }, new Object[] {
							this.totalNationalContribIFIAmount, " - ", this.totalNationalContribIFIAmountDate }));
		}
		if (this.totalNationalContribRegionalAmount != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "<br />", "Total National Regional Contribution:&nbsp;" },
							new Object[] { this.totalNationalContribRegionalAmount, " - ",
									this.totalNationalContribRegionalAmountDate }));
		}
		if (this.totalPrivateContribAmount != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "<br />", "Total Private Contribution:&nbsp;" }, new Object[] {
							this.totalPrivateContribAmount, " - ", this.totalPrivateContribAmountDate }));
		}
		if (this.totalDisbursements != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "<br />", "Total Disbursements:&nbsp;" },
							new Object[] { this.totalDisbursements }));
		}
		if (this.executionRate != null) {
			out.getOutputs()
					.add(
							new Output(null, new String[] { "<br />", "Execution Rate:&nbsp;" },
									new Object[] { this.executionRate }));
		}
		if (this.fundingTotalDisbursements != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "<br />", "Total Funding Disbursements:&nbsp;" },
							new Object[] { this.fundingTotalDisbursements }));
		}
		if (this.disbursements != null) {
			List auxDisbList = new ArrayList(this.disbursements);
			Collections.sort(auxDisbList, disbComparator);
			Iterator<IPAContractDisbursement> iterDisb = auxDisbList.iterator();
			while (iterDisb.hasNext()) {
				IPAContractDisbursement auxDisb = iterDisb.next();
				out.getOutputs().add(
						new Output(null, new String[] { "<br />", "Disbursements:&nbsp;" }, new Object[] {
								(auxDisb.getAdjustmentType() == 0 ? "Actual" : "Planned"), " - ", auxDisb.getAmount(),
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
		aux.setAmendments(null);
		
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
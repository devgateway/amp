package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.Output;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.common.util.DateTimeUtil;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * @author mihai
 *
 */
public class IPAContract implements Serializable, Versionable {

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
    
    private BigDecimal totalECContribIBAmount;
    private BigDecimal totalAmount;
    private BigDecimal contractTotalValue;
    private AmpCurrency totalAmountCurrency;
    private AmpCurrency dibusrsementsGlobalCurrency;
    private BigDecimal totalECContribINVAmount;
    private BigDecimal totalNationalContribCentralAmount;
    private BigDecimal totalNationalContribRegionalAmount;
    private BigDecimal totalNationalContribIFIAmount;
    private BigDecimal totalPrivateContribAmount;
    private Set disbursements;
    private AmpActivity activity;
    private AmpOrganisation organization;
    private Set<AmpOrganisation> organizations;
    private AmpCategoryValue status;
    //this disbursements and executionRate are used in Montenegro
    private BigDecimal totalDisbursements;
    private BigDecimal executionRate;
    //burkina mission - exchange rate is computed based on the disbursement entered in funding step and total amount
    //of the contract (contractTotalValue)
    private BigDecimal fundingTotalDisbursements;
    private BigDecimal fundingExecutionRate;
    
    private AmpCategoryValue type;
    private AmpCategoryValue contractType;
    
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

    public BigDecimal getTotalECContribIBAmount() {
        return FeaturesUtil.applyThousandsForVisibility(totalECContribIBAmount);
    }

    public void setTotalECContribIBAmount(BigDecimal totalECContribIBAmount) {
        this.totalECContribIBAmount = FeaturesUtil.applyThousandsForEntry(totalECContribIBAmount);
    }
    
    public BigDecimal getTotalECContribINVAmount() {
        return FeaturesUtil.applyThousandsForVisibility(totalECContribINVAmount);
    }

    public void setTotalECContribINVAmount(BigDecimal totalECContribINVAmount) {
        this.totalECContribINVAmount = FeaturesUtil.applyThousandsForEntry(totalECContribINVAmount);
    }

    public BigDecimal getTotalNationalContribCentralAmount() {
        return FeaturesUtil.applyThousandsForVisibility(totalNationalContribCentralAmount);
    }

    public void setTotalNationalContribCentralAmount(
    		BigDecimal totalNationalContribCentralAmount) {
        this.totalNationalContribCentralAmount = FeaturesUtil.applyThousandsForEntry(totalNationalContribCentralAmount);
    }

    public BigDecimal getTotalNationalContribIFIAmount() {
        return FeaturesUtil.applyThousandsForVisibility(totalNationalContribIFIAmount);
    }

    public void setTotalNationalContribIFIAmount(
    		BigDecimal totalNationalContribIFIAmount) {
        this.totalNationalContribIFIAmount = FeaturesUtil.applyThousandsForEntry(totalNationalContribIFIAmount);
    }

    public BigDecimal getTotalNationalContribRegionalAmount() {
        return FeaturesUtil.applyThousandsForVisibility(totalNationalContribRegionalAmount);
    }

    public void setTotalNationalContribRegionalAmount(
    		BigDecimal totalNationalContribRegionalAmount) {
        this.totalNationalContribRegionalAmount = FeaturesUtil.applyThousandsForEntry(totalNationalContribRegionalAmount);
    }

    public BigDecimal getTotalPrivateContribAmount() {
        return FeaturesUtil.applyThousandsForVisibility(totalPrivateContribAmount);
    }

    public void setTotalPrivateContribAmount(BigDecimal totalPrivateContribAmount) {
        this.totalPrivateContribAmount = FeaturesUtil.applyThousandsForEntry(totalPrivateContribAmount);
    }

	public Date getContractValidity() {
		return contractValidity;
	}

	public void setContractValidity(Date contractValidity) {
		this.contractValidity = contractValidity;
	}

	public BigDecimal getTotalAmount() {
		return FeaturesUtil.applyThousandsForVisibility(totalAmount);
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = FeaturesUtil.applyThousandsForEntry(totalAmount);
	}

	public AmpCurrency getTotalAmountCurrency() {
		return totalAmountCurrency;
	}

	public void setTotalAmountCurrency(AmpCurrency totalAmountCurrency) {
		this.totalAmountCurrency = totalAmountCurrency;
	}

	public BigDecimal getTotalDisbursements() {
		return FeaturesUtil.applyThousandsForVisibility(totalDisbursements);
	}

	public void setTotalDisbursements(BigDecimal totalDisbursements) {
		this.totalDisbursements = FeaturesUtil.applyThousandsForEntry(totalDisbursements);
	}

	public BigDecimal getExecutionRate() {
		return executionRate;
	}

	public void setExecutionRate(BigDecimal executionRate) {
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

	public BigDecimal getContractTotalValue() {
		return FeaturesUtil.applyThousandsForVisibility(contractTotalValue);
	}

	public void setContractTotalValue(BigDecimal contractTotalValue) {
		this.contractTotalValue = FeaturesUtil.applyThousandsForEntry(contractTotalValue);
	}

	public BigDecimal getFundingTotalDisbursements() {
		return FeaturesUtil.applyThousandsForVisibility(fundingTotalDisbursements);
	}

	public void setFundingTotalDisbursements(BigDecimal fundingTotalDisbursements) {
		this.fundingTotalDisbursements = FeaturesUtil.applyThousandsForEntry(fundingTotalDisbursements);
	}

	public BigDecimal getFundingExecutionRate() {
		return fundingExecutionRate;
	}

	public void setFundingExecutionRate(BigDecimal fundingExecutionRate) {
		this.fundingExecutionRate = fundingExecutionRate;
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
		out.getOutputs().add(new Output(null, new String[] { "Contract Name: " }, new Object[] { this.contractName }));
		if (this.description != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "<br />", "Description: " }, new Object[] { this.description }));
		}
		if (this.activityCategory != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "<br />", "Category: " }, new Object[] { this.activityCategory
							.getEncodedValue() }));
		}
		if (this.contractType != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "<br />", "Type: " }, new Object[] { this.contractType
							.getEncodedValue() }));
		}
		if (this.status != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "<br />", "Status: " }, new Object[] { this.status
							.getEncodedValue() }));
		}

		if (this.startOfTendering != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "<br />", "Start of Tendering: " },
							new Object[] { this.startOfTendering }));
		}
		if (this.signatureOfContract != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "<br />", "Signature of Contract: " },
							new Object[] { this.signatureOfContract }));
		}
		if (this.contractValidity != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "<br />", "Contract Validity Date: " },
							new Object[] { this.contractValidity }));
		}
		if (this.organizations != null) {
			Iterator<AmpOrganisation> iterOrgs = this.organizations.iterator();
			while (iterOrgs.hasNext()) {
				AmpOrganisation auxOrg = iterOrgs.next();
				out.getOutputs().add(
						new Output(null, new String[] { "<br />", "Contract Organization: " }, new Object[] { auxOrg
								.getAcronymAndName() }));
			}
		}
		if (this.contractingOrganizationText != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "<br />", "Contractor Name: " },
							new Object[] { this.contractingOrganizationText }));
		}
		if (this.contractCompletion != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "<br />", "Contract Completion: " },
							new Object[] { this.contractCompletion }));
		}
		if (this.totalAmount != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "<br />", "Total Amount: " }, new Object[] { this.totalAmount,
							" - ", this.totalAmountCurrency.getCurrencyCode() }));
		}
		if (this.contractTotalValue != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "<br />", "Contract Total Value: " },
							new Object[] { this.contractTotalValue }));
		}
		if (this.totalECContribIBAmount != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "<br />", "Total EC Contribution IB: " }, new Object[] {
							this.totalECContribIBAmount, " - ", this.totalECContribIBAmountDate }));
		}
		if (this.totalECContribINVAmount != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "<br />", "Total EC Contribution INV: " }, new Object[] {
							this.totalECContribINVAmount, " - ", this.totalECContribINVAmountDate }));
		}
		if (this.totalNationalContribCentralAmount != null) {
			out.getOutputs()
					.add(
							new Output(null, new String[] { "<br />", "Total National Central Contribution: " },
									new Object[] { this.totalNationalContribCentralAmount, " - ",
											this.totalNationalContribCentralAmountDate }));
		}
		if (this.totalNationalContribIFIAmount != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "<br />", "Total National IFIs Contribution: " }, new Object[] {
							this.totalNationalContribIFIAmount, " - ", this.totalNationalContribIFIAmountDate }));
		}
		if (this.totalNationalContribRegionalAmount != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "<br />", "Total National Regional Contribution: " },
							new Object[] { this.totalNationalContribRegionalAmount, " - ",
									this.totalNationalContribRegionalAmountDate }));
		}
		if (this.totalPrivateContribAmount != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "<br />", "Total Private Contribution: " }, new Object[] {
							this.totalPrivateContribAmount, " - ", this.totalPrivateContribAmountDate }));
		}
		if (this.totalDisbursements != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "<br />", "Total Disbursements: " },
							new Object[] { this.totalDisbursements }));
		}
		if (this.executionRate != null) {
			out.getOutputs()
					.add(
							new Output(null, new String[] { "<br />", "Execution Rate: " },
									new Object[] { this.executionRate }));
		}
		if (this.fundingTotalDisbursements != null) {
			out.getOutputs().add(
					new Output(null, new String[] { "<br />", "Total Funding Disbursements: " },
							new Object[] { this.fundingTotalDisbursements }));
		}
		if (this.disbursements != null) {
			List auxDisbList = new ArrayList(this.disbursements);
			Collections.sort(auxDisbList, disbComparator);
			Iterator<IPAContractDisbursement> iterDisb = auxDisbList.iterator();
			while (iterDisb.hasNext()) {
				IPAContractDisbursement auxDisb = iterDisb.next();
				out.getOutputs().add(
						new Output(null, new String[] { "<br />", "Disbursements: " }, new Object[] {
								(auxDisb.getAdjustmentType() == 0 ? "Actual" : "Planned"), " - ", auxDisb.getAmount(),
								" - ", auxDisb.getCurrency(), " - ", auxDisb.getDate() }));
			}
		}
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
		return ret;
	}
	
	private Comparator disbComparator = new Comparator() {
		public int compare(Object o1, Object o2) {
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
}
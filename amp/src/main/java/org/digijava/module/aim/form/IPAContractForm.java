package org.digijava.module.aim.form;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.IPAContractAmendment;
import org.digijava.module.aim.dbentity.IPAContractDisbursement;
import org.digijava.module.aim.helper.FundingDetail;

/**
 * @author mihai
 *
 */
public class IPAContractForm extends ActionForm  {
    
    private List<IPAContractDisbursement> contractDisbursements;
    
    private Long contractTypeId = new Long(0);
    private Long activityCategoryId = new Long(0);
    private Long statusId = new Long(0);
    private Long typeId = new Long(0);
    
    private List<FundingDetail> fundingDetailsLinked= new ArrayList<FundingDetail>();

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public Long[] getSelOrgs() {
        return selOrgs;
    }

    public void setSelOrgs(Long[] selOrgs) {
        this.selOrgs = selOrgs;
    }

    public IPAContractDisbursement getContractDisbursement(int index) {
        int currentSize = contractDisbursements.size();
        if (index >= currentSize) {
            for (int i = 0; i <= index - currentSize; i++) {
                contractDisbursements.add(new IPAContractDisbursement());
            }
        }
        return contractDisbursements.get(index);
    }
         


    
    private List currencies;
    
    
    private Long id;
    private String contractName;
    private String description;
    private String contractingOrganizationText; 
    
    private String startOfTendering;
    
    private String signatureOfContract;
    private String contractValidity;
    private String contractCompletion;
    
    private String totalECContribIBAmount;
    private String totalECContribIBAmountDate;
    
    private String totalAmount;
    private String contractTotalValue;
    
    private Long totalAmountCurrency;
    private Long dibusrsementsGlobalCurrency;
    
    private String totalECContribINVAmount;
    private String totalECContribINVAmountDate;
    
    private String totalNationalContribCentralAmount;
    private String totalNationalContribCentralAmountDate;
    
    private String totalNationalContribRegionalAmount;
    private String totalNationalContribRegionalAmountDate;
    
    private String totalNationalContribIFIAmount;
    private String totalNationalContribIFIAmountDate;
    
    private String totalPrivateContribAmount;
    private String totalPrivateContribAmountDate;

    private Double totalDisbursements;
    private Double executionRate;
    
    private List disbursements;
    private List<AmpOrganisation> organisations;
    private Long selOrgs[] = null;

    private Long contrOrg;
    private Integer indexId;
    private Long selContractDisbursements[];

    
    /**
     * 
     */
    private String donorContractFundinAmount;   
    private String donorContractFundingCurrency;
    
    private String totAmountDonorContractFunding;   
    private String totalAmountCurrencyDonor;
    
    private String totAmountCountryContractFunding; 
    private String totalAmountCurrencyCountry;  

    private List amendments;    

    private List<IPAContractAmendment> contractAmendments;
    
    public IPAContractAmendment getContractAmendment(int index) {
        int currentSize = contractAmendments.size();
        if (index >= currentSize) {
            for (int i = 0; i <= index - currentSize; i++) {
                contractAmendments.add(new IPAContractAmendment());
            }
        }
        return contractAmendments.get(index);
    }   

    private Long selContractAmendments[];
    /**
     * 
     */
    
    public Long[] getSelContractDisbursements() {
        return selContractDisbursements;
    }

    public void setSelContractDisbursements(Long[] selContractDisbursements) {
        this.selContractDisbursements = selContractDisbursements;
    }
    

    public Integer  getIndexId() {
        return indexId;
    }

    public void setIndexId(Integer indexId) {
        this.indexId = indexId;
    }
    
   

    public Long getContrOrg() {
        return contrOrg;
    }

    public void setContrOrg(Long contrOrg) {
        this.contrOrg = contrOrg;
    }

    public List getOrganisations() {
        return organisations;
    }
    
    public AmpOrganisation getOrganisation(int index) {
        int currentSize = organisations.size();
        if (index >= currentSize) {
            for (int i = 0; i <= index - currentSize; i++) {
                organisations.add(new AmpOrganisation());
            }
        }
        return organisations.get(index);
    }


    public void setOrganisations(List organisations) {
        this.organisations = organisations;
    }

    public String getContractCompletion() {
        return contractCompletion;
    }

    public void setContractCompletion(String contractCompletion) {
        this.contractCompletion = contractCompletion;
    }

    public List<IPAContractDisbursement> getContractDisbursements() {
        return contractDisbursements;
    }

    public void setContractDisbursements(
            List<IPAContractDisbursement> contractDisbursements) {
        this.contractDisbursements = contractDisbursements;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public List getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List currencies) {
        this.currencies = currencies;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List getDisbursements() {
        return disbursements;
    }

    public void setDisbursements(List disbursements) {
        this.disbursements = disbursements;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSignatureOfContract() {
        return signatureOfContract;
    }

    public void setSignatureOfContract(String signatureOfContract) {
        this.signatureOfContract = signatureOfContract;
    }

    public String getStartOfTendering() {
        return startOfTendering;
    }

    public void setStartOfTendering(String startOfTendering) {
        this.startOfTendering = startOfTendering;
    }

    public String getTotalECContribIBAmount() {
        return totalECContribIBAmount;
    }

    public void setTotalECContribIBAmount(String totalECContribIBAmount) {
        this.totalECContribIBAmount = totalECContribIBAmount;
    }

    public String getTotalECContribINVAmount() {
        return totalECContribINVAmount;
    }

    public void setTotalECContribINVAmount(String totalECContribINVAmount) {
        this.totalECContribINVAmount = totalECContribINVAmount;
    }

    public String getTotalNationalContribCentralAmount() {
        return totalNationalContribCentralAmount;
    }

    public void setTotalNationalContribCentralAmount(
            String totalNationalContribCentralAmount) {
        this.totalNationalContribCentralAmount = totalNationalContribCentralAmount;
    }

    public String getTotalNationalContribIFIAmount() {
        return totalNationalContribIFIAmount;
    }

    public void setTotalNationalContribIFIAmount(
            String totalNationalContribIFIAmount) {
        this.totalNationalContribIFIAmount = totalNationalContribIFIAmount;
    }

    public String getTotalNationalContribRegionalAmount() {
        return totalNationalContribRegionalAmount;
    }

    public void setTotalNationalContribRegionalAmount(
            String totalNationalContribRegionalAmount) {
        this.totalNationalContribRegionalAmount = totalNationalContribRegionalAmount;
    }

    public String getTotalPrivateContribAmount() {
        return totalPrivateContribAmount;
    }

    public void setTotalPrivateContribAmount(String totalPrivateContribAmount) {
        this.totalPrivateContribAmount = totalPrivateContribAmount;
    }

    public Long getActivityCategoryId() {
        return activityCategoryId;
    }

    public void setActivityCategoryId(Long activityCategoryId) {
        this.activityCategoryId = activityCategoryId;
    }
        
    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        if (request.getParameter("editEU")!=null){
            fundingDetailsLinked=new ArrayList();
        }
        if (request.getParameter("new")!=null && request.getParameter("addFields")==null){
        contractDisbursements=null;
        activityCategoryId = null;
        contractingOrganizationText = null;
        contractName = null;
        description = null;
        
        contractValidity = null;
        startOfTendering = null;

        signatureOfContract = null;
        contractCompletion = null;

        totalECContribIBAmount = null;
        
        totalAmount = null;
        contractTotalValue=null;
        totalAmountCurrency = null;
        dibusrsementsGlobalCurrency=null;
        executionRate=null;

        totalECContribINVAmount = null;

        totalNationalContribCentralAmount = null;

        totalNationalContribRegionalAmount = null;

        totalNationalContribIFIAmount = null;

        totalPrivateContribAmount = null;
        indexId=null;
        id=null;
        selContractDisbursements=null;
        contrOrg=null;
        
        activityCategoryId = new Long(0);
        statusId = new Long(0);
        typeId = new Long(0);   
        contractTypeId = new Long(0);
        
        totalECContribIBAmountDate = null;
        totalECContribINVAmountDate = null;
        totalNationalContribCentralAmountDate = null;
        totalNationalContribIFIAmountDate = null;
        totalNationalContribRegionalAmountDate = null;
        totalPrivateContribAmountDate = null;
        fundingDetailsLinked = new ArrayList();
        }

        
    }

    public String getContractValidity() {
        return contractValidity;
    }

    public void setContractValidity(String contractValidity) {
        this.contractValidity = contractValidity;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Long getTotalAmountCurrency() {
        return totalAmountCurrency;
    }

    public void setTotalAmountCurrency(Long totalAmountCurrency) {
        this.totalAmountCurrency = totalAmountCurrency;
    }

    public Long getDibusrsementsGlobalCurrency() {
        return dibusrsementsGlobalCurrency;
    }

    public void setDibusrsementsGlobalCurrency(Long dibusrsementsGlobalCurrency) {
        this.dibusrsementsGlobalCurrency = dibusrsementsGlobalCurrency;
    }

    public Double getTotalDisbursements() {
        return totalDisbursements;
    }

    public void setTotalDisbursements(Double totalDisbursements) {
        this.totalDisbursements = totalDisbursements;
    }

    public Double getExecutionRate() {
        return executionRate;
    }

    public void setExecutionRate(Double executionRate) {
        this.executionRate = executionRate;
    }

    public String getContractingOrganizationText() {
        return contractingOrganizationText;
    }

    public void setContractingOrganizationText(String contractingOrganizationText) {
        this.contractingOrganizationText = contractingOrganizationText;
    }

    public String getTotalECContribIBAmountDate() {
        return totalECContribIBAmountDate;
    }

    public void setTotalECContribIBAmountDate(String totalECContribIBAmountDate) {
        this.totalECContribIBAmountDate = totalECContribIBAmountDate;
    }

    public String getTotalECContribINVAmountDate() {
        return totalECContribINVAmountDate;
    }

    public void setTotalECContribINVAmountDate(String totalECContribINVAmountDate) {
        this.totalECContribINVAmountDate = totalECContribINVAmountDate;
    }

    public String getTotalNationalContribCentralAmountDate() {
        return totalNationalContribCentralAmountDate;
    }

    public void setTotalNationalContribCentralAmountDate(
            String totalNationalContribCentralAmountDate) {
        this.totalNationalContribCentralAmountDate = totalNationalContribCentralAmountDate;
    }

    public String getTotalNationalContribRegionalAmountDate() {
        return totalNationalContribRegionalAmountDate;
    }

    public void setTotalNationalContribRegionalAmountDate(
            String totalNationalContribRegionalAmountDate) {
        this.totalNationalContribRegionalAmountDate = totalNationalContribRegionalAmountDate;
    }

    public String getTotalNationalContribIFIAmountDate() {
        return totalNationalContribIFIAmountDate;
    }

    public void setTotalNationalContribIFIAmountDate(
            String totalNationalContribIFIAmountDate) {
        this.totalNationalContribIFIAmountDate = totalNationalContribIFIAmountDate;
    }

    public String getTotalPrivateContribAmountDate() {
        return totalPrivateContribAmountDate;
    }

    public void setTotalPrivateContribAmountDate(
            String totalPrivateContribAmountDate) {
        this.totalPrivateContribAmountDate = totalPrivateContribAmountDate;
    }

    public Long getContractTypeId() {
        return contractTypeId;
    }

    public void setContractTypeId(Long contractTypeId) {
        this.contractTypeId = contractTypeId;
    }

    public List<FundingDetail> getFundingDetailsLinked() {
        return fundingDetailsLinked;
    }

    public void setFundingDetailsLinked(List<FundingDetail> fundingDetailsLinked) {
        this.fundingDetailsLinked = fundingDetailsLinked;
    }

    public String getContractTotalValue() {
        return contractTotalValue;
    }

    public void setContractTotalValue(String contractTotalValue) {
        this.contractTotalValue = contractTotalValue;
    }

    public String getDonorContractFundinAmount() {
        return donorContractFundinAmount;
    }

    public void setDonorContractFundinAmount(String donorContractFundinAmount) {
        this.donorContractFundinAmount = donorContractFundinAmount;
    }

    public String getDonorContractFundingCurrency() {
        return donorContractFundingCurrency;
    }

    public void setDonorContractFundingCurrency(String donorContractFundingCurrency) {
        this.donorContractFundingCurrency = donorContractFundingCurrency;
    }

    public String getTotAmountDonorContractFunding() {
        return totAmountDonorContractFunding;
    }

    public void setTotAmountDonorContractFunding(
            String totAmountDonorContractFunding) {
        this.totAmountDonorContractFunding = totAmountDonorContractFunding;
    }

    public String getTotalAmountCurrencyDonor() {
        return totalAmountCurrencyDonor;
    }

    public void setTotalAmountCurrencyDonor(String totalAmountCurrencyDonor) {
        this.totalAmountCurrencyDonor = totalAmountCurrencyDonor;
    }

    public String getTotAmountCountryContractFunding() {
        return totAmountCountryContractFunding;
    }

    public void setTotAmountCountryContractFunding(
            String totAmountCountryContractFunding) {
        this.totAmountCountryContractFunding = totAmountCountryContractFunding;
    }

    public String getTotalAmountCurrencyCountry() {
        return totalAmountCurrencyCountry;
    }

    public void setTotalAmountCurrencyCountry(String totalAmountCurrencyCountry) {
        this.totalAmountCurrencyCountry = totalAmountCurrencyCountry;
    }

    public List getAmendments() {
        return amendments;
    }

    public void setAmendments(List amendments) {
        this.amendments = amendments;
    }

    public Long[] getSelContractAmendments() {
        return selContractAmendments;
    }

    public void setSelContractAmendments(Long[] selContractAmendments) {
        this.selContractAmendments = selContractAmendments;
    }

    public List<IPAContractAmendment> getContractAmendments() {
        return contractAmendments;
    }

    public void setContractAmendments(List<IPAContractAmendment> contractAmendments) {
        this.contractAmendments = contractAmendments;
    }


}

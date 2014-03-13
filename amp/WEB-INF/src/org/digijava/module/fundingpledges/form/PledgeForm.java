package org.digijava.module.fundingpledges.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

import lombok.Data;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.KeyValue;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesDetails;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesLocation;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesProgram;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesSector;
import org.digijava.module.fundingpledges.dbentity.PledgesEntityHelper;

@Data
public class PledgeForm extends ActionForm implements Serializable{

	private static final long serialVersionUID = 1L;
	private Long pledgeId;
	private FundingPledges fundingPledges;
	private String selectedOrgId;
	private String selectedOrgGrpId;
	private Collection<AmpOrgGroup> orgGroups;
	private String selectedOrgName;
	private AmpCategoryValue pledgeTitle;
	private String titleFreeText;
	private Collection<AmpCurrency> validcurrencies;
	private String currencyCode;
	private String contact1Name;
	private String contact1Title;
	private String contact1OrgName;
	private String contact1OrgId;
	private String contact1Ministry;
	private String contact1Address;
	private String contact1Telephone;
	private String contact1Email;
	private String contact1Fax;
	private String contactAlternate1Name;
	private String contactAlternate1Email;
	private String contactAlternate1Telephone;
	
	private String contact2Name;
	private String Contact2Title;
	private String contact2OrgName;
	private String contact2OrgId;
	private String contact2Ministry;
	private String contact2Address;
	private String contact2Telephone;
	private String contact2Email;
	private String contact2Fax;
	
	private String contactAlternate2Name;
	private String contactAlternate2Email;
	private String contactAlternate2Telephone;
	
	private String additionalInformation;
	private String whoAuthorizedPledge;
	private String furtherApprovalNedded;
	private Collection<ActivitySector> pledgeSectors;
	private Collection<FundingPledgesDetails> fundingPledgesDetails;
	private Collection<AmpCategoryValue> pledgeTypeCategory;
	private Collection<AmpCategoryValue> assistanceTypeCategory;
	private Collection<AmpCategoryValue> aidModalityCategory;
	private String defaultCurrency;
	private Collection<AmpCategoryValue> pledgeNames;
	private Long pledgeTitleId;
	private Collection<String> years;
	private String year;
	
	/*Fields for Location*/
	private boolean noMoreRecords;
	private Long implemLocationLevel;
    private AmpCategoryValue implLocationValue;
	private Integer impLevelValue; // Implementation Level value
	private Long levelId = null;
	private Long parentLocId;
	private boolean defaultCountryIsSet;
	private Collection<FundingPledgesLocation> selectedLocs;
	private Long [] userSelectedLocs;
	private TreeMap<Integer, Collection<KeyValue>> locationByLayers;
	private TreeMap<Integer, Long> selectedLayers ;
	
	/*Fields for program*/
	private int programType;
	private List programLevels;
	private Long selPrograms[];
	private Collection<FundingPledgesProgram> selectedProgs;
	private AmpActivityProgramSettings nationalSetting;
	
	private String fundingEvent;
    private Long selectedFunding[];
    
    public void reset()
    {
    	this.setTitleFreeText(null);
    	this.setPledgeId(null);
		this.setPledgeTitle(null);
		this.setPledgeTitleId(null);
		this.setFundingPledges(null);
		this.setSelectedOrgId(null);
		this.setSelectedOrgGrpId(null);
    	this.setSelectedOrgName(null);
    	this.setAdditionalInformation(null);
    	this.setWhoAuthorizedPledge(null);
    	this.setFurtherApprovalNedded(null);
    	this.setContact1Address(null);
    	this.setContact1Email(null);
    	this.setContact1Fax(null);
    	this.setContact1Ministry(null);
    	this.setContact1Name(null);
    	this.setContact1OrgId(null);
    	this.setContact1OrgName(null);
    	this.setContact1Telephone(null);
    	this.setContact1Title(null);
    	this.setContactAlternate1Email(null);
    	this.setContactAlternate1Name(null);
    	this.setContactAlternate1Telephone(null);
    	this.setContact2Address(null);
    	this.setContact2Email(null);
    	this.setContact2Fax(null);
    	this.setContact2Ministry(null);
    	this.setContact2Name(null);
    	this.setContact2OrgId(null);
    	this.setContact2OrgName(null);
    	this.setContact2Telephone(null);
    	this.setContact2Title(null);
    	this.setContactAlternate2Email(null);
    	this.setContactAlternate2Name(null);
    	this.setContactAlternate2Telephone(null);
    	this.setFundingPledgesDetails(null);
    	this.setPledgeSectors(null);
    	this.setSelectedLocs(null);
    	this.setSelectedProgs(null);
    }
    
    public void importPledgeData(FundingPledges fp)
    {
    	this.setFundingPledges(fp);
    	this.setPledgeId(fp.getId());
    	if (FeaturesUtil.isVisibleField("Use Free Text")){
    		this.setTitleFreeText(fp.getTitleFreeText());
    	}else{
    		this.setPledgeTitle(fp.getTitle());
    		if (fp.getTitle() != null) {
    			this.setPledgeTitleId(fp.getTitle().getId());
    		}
    	}
		this.setSelectedOrgGrpId(fp.getOrganizationGroup().getAmpOrgGrpId().toString());
    	this.setAdditionalInformation(fp.getAdditionalInformation());
    	this.setWhoAuthorizedPledge(fp.getWhoAuthorizedPledge());
    	this.setFurtherApprovalNedded(fp.getFurtherApprovalNedded());
    	this.setContact1Address(fp.getContactAddress());
    	this.setContact1Email(fp.getContactEmail());
    	this.setContact1Fax(fp.getContactFax());
    	this.setContact1Ministry(fp.getContactMinistry());
    	this.setContact1Name(fp.getContactName());
    	if (fp.getContactOrganization()!=null){
        	AmpOrganisation cont1Org =	PledgesEntityHelper.getOrganizationById(fp.getContactOrganization().getAmpOrgId());
			this.setContact1OrgId(cont1Org.getAmpOrgId().toString());
        	this.setContact1OrgName(cont1Org.getAcronym());
    	}
    	this.setContact1Telephone(fp.getContactTelephone());
    	this.setContact1Title(fp.getContactTitle());
    	this.setContactAlternate1Email(fp.getContactAlternativeEmail());
    	this.setContactAlternate1Name(fp.getContactAlternativeName());
    	this.setContactAlternate1Telephone(fp.getContactAlternativeTelephone());
    	this.setContact2Address(fp.getContactAddress_1());
    	this.setContact2Email(fp.getContactEmail_1());
    	this.setContact2Fax(fp.getContactFax_1());
    	this.setContact2Ministry(fp.getContactMinistry_1());
    	this.setContact2Name(fp.getContactName_1());
    	
    	if (fp.getContactOrganization_1()!=null){
        	AmpOrganisation cont2Org =	PledgesEntityHelper.getOrganizationById(fp.getContactOrganization_1().getAmpOrgId());
        	this.setContact2OrgId(cont2Org.getAmpOrgId().toString());
        	this.setContact2OrgName(cont2Org.getAcronym());
    	}
    	this.setContact2Telephone(fp.getContactTelephone_1());
    	this.setContact2Title(fp.getContactTitle_1());
    	this.setContactAlternate2Email(fp.getContactAlternativeEmail_1());
    	this.setContactAlternate2Name(fp.getContactAlternativeName_1());
    	this.setContactAlternate2Telephone(fp.getContactAlternativeTelephone_1());
    	this.setFundingPledgesDetails(fp.getFundingPledgesDetails());
    	Collection<FundingPledgesSector> fpsl = PledgesEntityHelper.getPledgesSectors(fp.getId());
    	Collection<ActivitySector> asl = new ArrayList<ActivitySector>();
    	for (FundingPledgesSector fps:fpsl)
    	{			
			ActivitySector actSec = fps.createActivitySector();
			asl.add(actSec);
		}
    	
    	this.setPledgeSectors(asl);
    	this.setSelectedLocs(PledgesEntityHelper.getPledgesLocations(fp.getId()));
    	this.setSelectedProgs(PledgesEntityHelper.getPledgesPrograms(fp.getId()));
    	this.setFundingPledgesDetails(PledgesEntityHelper.getPledgesDetails(fp.getId()));
    }
}

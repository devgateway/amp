package org.digijava.module.fundingpledges.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import lombok.Data;

import org.apache.struts.action.ActionForm;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.KeyValue;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.IdWithValueShim;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.fundingpledges.action.DisableableKeyValue;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesDetails;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesLocation;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesProgram;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesSector;
import org.digijava.module.fundingpledges.dbentity.PledgesEntityHelper;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

@Data
/**
 * session form holding the current state of the Add/Edit Pledge Form
 * @author Constantin Dolghier
 *
 */
public class PledgeForm extends ActionForm implements Serializable{

	public final static String SELECT_BOX_DROP_DOWN_NAME = "Please select from below";
	private static final long serialVersionUID = 1L;
	private Long pledgeId;
	private FundingPledges fundingPledges;
	private Long selectedOrgId;
	private Long selectedOrgGrpId;
	private String titleFreeText;
	private Collection<AmpCurrency> validcurrencies;
	private String currencyCode;
	
	private PledgeFormContact contact1 = new PledgeFormContact();
	private PledgeFormContact contact2 = new PledgeFormContact();
	
	private String additionalInformation;
	private String whoAuthorizedPledge;
	private String furtherApprovalNedded;
	private Collection<ActivitySector> pledgeSectors;
	private Collection<FundingPledgesDetails> fundingPledgesDetails;
	private Collection<AmpCategoryValue> pledgeTypeCategory;
	private Collection<AmpCategoryValue> assistanceTypeCategory;
	private Collection<AmpCategoryValue> aidModalityCategory;
	private String defaultCurrency;

	private Long pledgeTitleId;
	private Collection<String> years;
	private String year;
	
	/*Fields for Location*/
	
	/**
	 * implementation location ACV.id
	 */
	private Long implemLocationLevel;
    
    /**
     * implementation level ACV.id
     */
	private Long levelId = null;
	private List<FundingPledgesLocation> selectedLocs = new ArrayList<>();	
	
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
		this.setPledgeTitleId(null);
		this.setFundingPledges(null);
		this.setSelectedOrgId(null);
		this.setSelectedOrgGrpId(null);
    	this.setAdditionalInformation(null);
    	this.setWhoAuthorizedPledge(null);
    	this.setFurtherApprovalNedded(null);
    	this.contact1.reset();
    	this.contact2.reset();
    	this.setFundingPledgesDetails(null);
    	this.setPledgeSectors(null);
    	this.selectedLocs.clear();
    	this.setSelectedProgs(null);
    	this.cleanLocationData(true);
    }
    
    /**
     * imports a FundingPledges instance into this form instance
     * @param fp
     */
    public void importPledgeData(FundingPledges fp)
    {
    	this.setFundingPledges(fp);
    	this.setPledgeId(fp.getId());
    	this.setTitleFreeText(fp.getTitleFreeText());
   		this.setPledgeTitleId(fp.getTitle() == null ? null : fp.getTitle().getId());
		this.setSelectedOrgGrpId(fp.getOrganizationGroup().getAmpOrgGrpId());
    	this.setAdditionalInformation(fp.getAdditionalInformation());
    	this.setWhoAuthorizedPledge(fp.getWhoAuthorizedPledge());
    	this.setFurtherApprovalNedded(fp.getFurtherApprovalNedded());
    	
    	this.contact1.setAddress(fp.getContactAddress());
    	this.contact1.setEmail(fp.getContactEmail());
    	this.contact1.setFax(fp.getContactFax());
    	this.contact1.setMinistry(fp.getContactMinistry());
    	this.contact1.setName(fp.getContactName());
    	if (fp.getContactOrganization()!=null){
        	AmpOrganisation cont1Org =	PledgesEntityHelper.getOrganizationById(fp.getContactOrganization().getAmpOrgId());
			this.contact1.setOrgId(cont1Org.getAmpOrgId().toString());
        	this.contact1.setOrgName(cont1Org.getAcronym());
    	}
    	this.contact1.setTelephone(fp.getContactTelephone());
    	this.contact1.setTitle(fp.getContactTitle());
    	this.contact1.setAlternateEmail(fp.getContactAlternativeEmail());
    	this.contact1.setAlternateName(fp.getContactAlternativeName());
    	this.contact1.setAlternateTelephone(fp.getContactAlternativeTelephone());
    	
    	this.contact2.setAddress(fp.getContactAddress_1());
    	this.contact2.setEmail(fp.getContactEmail_1());
    	this.contact2.setFax(fp.getContactFax_1());
    	this.contact2.setMinistry(fp.getContactMinistry_1());
    	this.contact2.setName(fp.getContactName_1());
    	if (fp.getContactOrganization_1()!=null){
        	AmpOrganisation cont1Org =	PledgesEntityHelper.getOrganizationById(fp.getContactOrganization_1().getAmpOrgId());
			this.contact2.setOrgId(cont1Org.getAmpOrgId().toString());
        	this.contact2.setOrgName(cont1Org.getAcronym());
    	}
    	this.contact2.setTelephone(fp.getContactTelephone_1());
    	this.contact2.setTitle(fp.getContactTitle_1());
    	this.contact2.setAlternateEmail(fp.getContactAlternativeEmail_1());
    	this.contact2.setAlternateName(fp.getContactAlternativeName_1());
    	this.contact2.setAlternateTelephone(fp.getContactAlternativeTelephone_1());
    	
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
    
    public void cleanLocationData(boolean cleanLevelData)
    {
    	if (cleanLevelData)
    	{
    		this.setImplemLocationLevel(-1l);
    		this.setLevelId(-1l);
    	}
        //this.setParentLocId(null);
        // this if for FundingPledgesLocation. Not sure why this is in this code
        //pledgeForm.setSelectedLocs(null);
        //this.setUserSelectedLocs(null);
    }
    
    /**
     * returns set of all the selected locations
     * @return Set<ACVL.id>
     */
    public Set<Long> getAllSelectedLocations()
    {
    	Set<Long> res = new HashSet<Long>();
    	for(FundingPledgesLocation fpl:this.getSelectedLocs())
    		res.add(fpl.getLocation().getId());
    	return res;
    }
    
    /**
     * gets <strong>from the db</strong> the ACV corresponding to {@link #getLevelId()}
     * DO NOT CACHE ITS RESULT - it is used cross-requests
     * @return
     */
    public AmpCategoryValue getImplementationLevel()
    {
    	return CategoryManagerUtil.getAmpCategoryValueFromDb(getLevelId());
    }
    
    /**
     * gets <strong>from the db</db> the ACV corresponding to {@link #getImplemLocationLevel()}
     * DO NOT CACHE ITS RESULT - it is used cross-requests
     * @return
     */
    public AmpCategoryValue getImplLocationValue()
    {
    	return CategoryManagerUtil.getAmpCategoryValueFromDb(getImplemLocationLevel());
    }
    
    /**
     * computes list of acceptable values - called by the JSP
     * @return
     */
    public List<KeyValue> getAllValidImplementationLocationChoices()
    {
    	final AmpCategoryValue implLevel = getImplementationLevel(); 
       	java.util.List<AmpCategoryValue> validChoices = 
    			CategoryManagerUtil.getAllAcceptableValuesForACVClass("implementation_location", new ArrayList<AmpCategoryValue>(){{this.add(implLevel);}});
       	List<KeyValue> res = new ArrayList<KeyValue>();
       	res.add(new KeyValue("0", TranslatorWorker.translateText(SELECT_BOX_DROP_DOWN_NAME)));
       	if (validChoices != null)
       	{
       		for(AmpCategoryValue acvl:validChoices)
       			res.add(new KeyValue(acvl.getId().toString(), TranslatorWorker.translateText(acvl.getValue())));
       	}
       	if ((res.size() == 2) && (res.get(0).getKeyAsLong() <= 0))
       	{
       		// a single option is available besides "PLEASE SELECT" -> remove "please select"
       		res.remove(0);
       		this.setImplemLocationLevel(res.get(0).getKeyAsLong()); // mark this as selected
       	}
       	return res;
    }
    
    /**
     * removes all the redundant options in case a single one is valid
     * @param res
     * @return
     */
    public List<DisableableKeyValue> selectSingleAvailableOption(List<DisableableKeyValue> res)
    {
    	List<DisableableKeyValue> realOptions = Lists.newArrayList(Iterables.filter(res, new Predicate<DisableableKeyValue>(){
    			public boolean apply(DisableableKeyValue dkv){return dkv.isEnabled() && dkv.getKeyAsLong() > 0;};
    		}));
    	if (realOptions.size() == 1)
    		return realOptions;
    	return res;
    }
    
    /**
     * computes list of acceptable locations - called by the JSP
     * @return
     */
    public List<DisableableKeyValue> getAllValidLocations()
    {
    	List<DisableableKeyValue> res = new ArrayList<DisableableKeyValue>();
    	//res.add(new DisableableKeyValue(new KeyValue("0", TranslatorWorker.translateText(SELECT_BOX_DROP_DOWN_NAME)), true)); no need for this if we are using multiselect. REENABLE if going from multiselect to simple-select
    	AmpCategoryValue implLocationValue = getImplLocationValue();
    	if (implLocationValue != null)
    	{
        	AmpCategoryValue implLevel = getImplementationLevel(); // guaranteed to be non-null or we have a bug
        	
    		// something selected -> so need to build list of forbidden locations so that they are disabled in the multiselect
            Set<Long> forbiddenLocations = DynLocationManagerUtil.getRecursiveChildrenOfCategoryValueLocations(getAllSelectedLocations()); // any selected locations and any of their descendants or ascendats are forbidden
            forbiddenLocations.addAll(DynLocationManagerUtil.getRecursiveAscendantsOfCategoryValueLocations(getAllSelectedLocations()));
        	
        	if (CategoryConstants.IMPLEMENTATION_LEVEL_NATIONAL.equalsCategoryValue(implLevel) &&
        			CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY.equalsCategoryValue(implLocationValue))
        	{
        		// Implementation Level: NATIONAL, Implementation Location: Country: only the default country is available
        		AmpCategoryValueLocations country = DynLocationManagerUtil.getDefaultCountry(); 
        		res.add(new DisableableKeyValue(new KeyValue(country.getId().toString(), country.getName()), !forbiddenLocations.contains(country.getId())));
        		return selectSingleAvailableOption(res);
        	}
                
            Collection<AmpCategoryValueLocations> levelLocations = DynLocationManagerUtil.getLocationsByLayer(implLocationValue);
            for(AmpCategoryValueLocations loc:levelLocations)
            	res.add(new DisableableKeyValue(new KeyValue(loc.getId().toString(), loc.getName()), !forbiddenLocations.contains(loc.getId())));
    	}
    	return selectSingleAvailableOption(res);
    }
    
    public void addSelectedLocation(long locId)
    {

    	FundingPledgesLocation fpl = new FundingPledgesLocation();
    	fpl.setLocation(DynLocationManagerUtil.getLocation(locId, false));
    	fpl.setLocationpercentage(0f);
    	selectedLocs.add(fpl);
    }
    
    /**
     * returns list of pledge names available for selection - called by the JSP
     */
    public List<IdWithValueShim> getPledgeNames()
    {
    	List<IdWithValueShim> res = new ArrayList<>();
    	if (this.getPledgeTitleId() == null)
    		res.add(new IdWithValueShim(-1l, TranslatorWorker.translateText("Please select")));
    	for(AmpCategoryValue acv:CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.PLEDGES_NAMES_KEY))
    		res.add(new IdWithValueShim(acv));
    	return res;
    }
    
    /**
     * returns list of AmpOrgGroup's in the DB - called by the JSP
     * @return
     */
    public List<IdWithValueShim> getOrgGroups()
    {
    	List<IdWithValueShim> res = new ArrayList<>();
    	if (this.getSelectedOrgGrpId() == null)
    		res.add(new IdWithValueShim(-1l, TranslatorWorker.translateText("Please select")));
    	for(AmpOrgGroup acv:org.digijava.module.aim.util.DbUtil.getAllOrgGroups())
    		res.add(new IdWithValueShim(acv));
    	return res;    	
    }
}


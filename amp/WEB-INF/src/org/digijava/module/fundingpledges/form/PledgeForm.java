package org.digijava.module.fundingpledges.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;

import lombok.Data;

import org.apache.struts.action.ActionForm;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.action.FeatureManager;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpSectorScheme;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.KeyValue;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;
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

import static org.dgfoundation.amp.algo.AmpCollections.*;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Data
/**
 * session form holding the current state of the Add/Edit Pledge Form
 * @author Constantin Dolghier
 *
 */
public class PledgeForm extends ActionForm implements Serializable
{
	public final static AtomicLong uniqueIds = new AtomicLong(1l);
	public final static String SELECT_BOX_DROP_DOWN_NAME = "Please select from below";
	public final static List<DisableableKeyValue> DISABLEABLE_KV_PLEASE_SELECT = new ArrayList<DisableableKeyValue>(){{add(new DisableableKeyValue(-1l, SELECT_BOX_DROP_DOWN_NAME, true));}};
	public final static List<KeyValue> KV_PLEASE_SELECT = new ArrayList<KeyValue>(){{add(new KeyValue(-1l, SELECT_BOX_DROP_DOWN_NAME));}};
	
	/**
	 * extract IdNamePercentage from a FPL entry
	 */
	public final static Function<AmpCategoryValueLocations, IdNamePercentage> PLEDGE_LOCATION_EXTRACTOR = new Function<AmpCategoryValueLocations, IdNamePercentage>() {
		public IdNamePercentage apply(AmpCategoryValueLocations acvl){
			return new IdNamePercentage(acvl.getId(), acvl.getName(), acvl.getHierarchicalName());
		}
	};

	/**
	 * extract IdNamePercentage from a FPP entry
	 */
	public final static Function<AmpTheme, IdNamePercentage> PLEDGE_PROGRAM_EXTRACTOR = new Function<AmpTheme, IdNamePercentage>() {
		public IdNamePercentage apply(AmpTheme theme){			
			return new IdNamePercentage(theme.getAmpThemeId(), theme.getName(), theme.getRootTheme().getAmpThemeId(), theme.getRootTheme().getName(), theme.getHierarchicalName());
		}
	};

	/**
	 * extract IdNamePercentage from a FPS entry
	 */
	public final static Function<AmpSector, IdNamePercentage> PLEDGE_SECTOR_EXTRACTOR = new Function<AmpSector, IdNamePercentage>() {
		public IdNamePercentage apply(AmpSector sector){
			return new IdNamePercentage(sector.getAmpSectorId(), sector.getName(), 
					sector.getAmpSecSchemeId().getAmpSecSchemeId(), sector.getAmpSecSchemeId().getSecSchemeName(), sector.getHierarchicalName());
		}
	};

	public final static Function<IdNamePercentage, KeyValue> BY_ROOT_DISTRIBUTION = new Function<IdNamePercentage, KeyValue>(){
		public KeyValue apply(IdNamePercentage from) {
			return new KeyValue(from.getRootId(), from.getRootName());}};

	public final static Function<AmpCurrency, IdWithValueShim> AMP_CURRENCY_TO_ID_WITH_SHIM = new Function<AmpCurrency, IdWithValueShim>(){
		public IdWithValueShim apply(AmpCurrency curr){return new IdWithValueShim(curr.getAmpCurrencyId(), curr.getCurrencyName());};};
		
	private static final long serialVersionUID = 1L;
	private Long pledgeId;
	//private FundingPledges fundingPledges;
	private Long selectedOrgId;
	private Long selectedOrgGrpId;
	private String titleFreeText;
	//private Collection<AmpCurrency> validcurrencies;
	//private String currencyCode;
	
	private PledgeFormContact contact1 = new PledgeFormContact();
	private PledgeFormContact contact2 = new PledgeFormContact();
	
	private String additionalInformation;
	private String whoAuthorizedPledge;
	private String furtherApprovalNedded;

	//private String defaultCurrency;

	private Long pledgeTitleId;
	//private Collection<String> years;
	private String year;
	
	private boolean locationsMultiselect = false;
	
	/*Fields for Location*/
	
	/**
	 * implementation location ACV.id
	 */
	private Long implemLocationLevel;
    
    /**
     * implementation level ACV.id
     */
	private Long levelId = null;
	private List<IdNamePercentage> selectedLocs = new ArrayList<>();	
	
	/* Fields for Programs */
	private Long selectedRootProgram;
	private List<IdNamePercentage> selectedProgs = new ArrayList<>();
	
	/* Fields for Sectors */
	/**
	 * AmpSectorScheme.id
	 */
	private Long selectedRootSector;
	private List<IdNamePercentage> selectedSectors = new ArrayList<>();
	    
	/**
	 * funding
	 */
	private List<FundingPledgesDetailsShim> selectedFunding = new ArrayList<>();
        
    public void reset()
    {
    	this.setTitleFreeText(null);
    	this.setPledgeId(null);
		this.setPledgeTitleId(null);
		//this.setFundingPledges(null);
		this.setSelectedOrgId(null);
		this.setSelectedOrgGrpId(null);
    	this.setAdditionalInformation(null);
    	this.setWhoAuthorizedPledge(null);
    	this.setFurtherApprovalNedded(null);
    	this.contact1.reset();
    	this.contact2.reset();
    	this.selectedFunding.clear();
    	this.selectedSectors.clear();
    	this.selectedLocs.clear();
    	this.selectedProgs.clear();
    	this.cleanLocationData(true);
    	this.selectedRootProgram = null;
    	this.selectedRootSector = null;
    	this.implemLocationLevel = null;
    	this.levelId = null;
    }
    
    /**
     * imports a FundingPledges instance into this form instance
     * @param fp
     */
    public void importPledgeData(FundingPledges fp)
    {
    	//this.setFundingPledges(fp);
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
    	
    	//this.setFundingPledgesDetails(fp.getFundingPledgesDetails());
    	
    	this.setSelectedSectors(new ArrayList<IdNamePercentage>());
    	for(FundingPledgesSector sec:fp.getSectorlist())
    		selectedSectors.add(PLEDGE_SECTOR_EXTRACTOR.apply(sec.getSector()).setPercentageChained(sec.getSectorpercentage()));
    	
    	this.setSelectedLocs(new ArrayList<IdNamePercentage>());    	
    	for(FundingPledgesLocation loc:fp.getLocationlist())
    		selectedLocs.add(PLEDGE_LOCATION_EXTRACTOR.apply(loc.getLocation()).setPercentageChained(loc.getLocationpercentage()));

    	this.setSelectedProgs(new ArrayList<IdNamePercentage>());
    	for(FundingPledgesProgram prog:fp.getProgramlist())
    		selectedProgs.add(PLEDGE_PROGRAM_EXTRACTOR.apply(prog.getProgram()).setPercentageChained(prog.getProgrampercentage()));

    	this.setSelectedFunding(new ArrayList<FundingPledgesDetailsShim>());
    	for(FundingPledgesDetails fpd:fp.getFundingPledgesDetails())
    		this.selectedFunding.add(new FundingPledgesDetailsShim(fpd));
    }
    
    
    public void cleanLocationData(boolean cleanLevelData){
    	if (cleanLevelData)
    	{
    		this.setImplemLocationLevel(-1l);
    		this.setLevelId(-1l);
    	}
    }
    
    /**
     * returns set of all the selected locations
     * @return Set<ACVL.id>
     */
    public Set<Long> getAllSelectedLocations(){
    	return collectIds(this.getSelectedLocs());
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
    	if (!locationsMultiselect)
    		res.add(new DisableableKeyValue(new KeyValue("0", TranslatorWorker.translateText(SELECT_BOX_DROP_DOWN_NAME)), true)); // no need for this if we are using multiselect. REENABLE if going from multiselect to simple-select
    	AmpCategoryValue implLocationValue = getImplLocationValue();
    	if (implLocationValue != null)
    	{
        	AmpCategoryValue implLevel = getImplementationLevel(); // guaranteed to be non-null or we have a bug
        	
    		// something selected -> so need to build list of forbidden locations so that they are disabled in the multiselect
            Set<Long> forbiddenLocations = DynLocationManagerUtil.getRecursiveChildrenOfCategoryValueLocations(getAllSelectedLocations());
            forbiddenLocations.addAll(DynLocationManagerUtil.getRecursiveAscendantsOfCategoryValueLocations(getAllSelectedLocations())); // any selected locations and any of their descendants or ascendants are forbidden
        	
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
    
    public void addSelectedLocation(long locId){
    	AmpCategoryValueLocations acvl = DynLocationManagerUtil.getLocation(locId, false);
    	selectedLocs.add(PLEDGE_LOCATION_EXTRACTOR.apply(acvl).setPercentageChained(0f));
    }
    
    public void addSelectedProgram(long themeId){
    	AmpTheme theme = ProgramUtil.getThemeById(themeId);
		selectedProgs.add(PLEDGE_PROGRAM_EXTRACTOR.apply(theme).setPercentageChained(0f));
    }
    
    public void addSelectedSector(long sectorId){
    	AmpSector sector = SectorUtil.getAmpSector(sectorId);
    	selectedSectors.add(PLEDGE_SECTOR_EXTRACTOR.apply(sector).setPercentageChained(0f));
    }
    
    public List<IdWithValueShim> getShimsForCategoryClass(String categoryClassKey, Long selectedValue)
    {
    	List<IdWithValueShim> res = new ArrayList<>();
    	if (selectedValue == null)
    		res.add(new IdWithValueShim(-1l, TranslatorWorker.translateText("Please select")));
    	for(AmpCategoryValue acv:CategoryManagerUtil.getAmpCategoryValueCollectionByKey(categoryClassKey))
    		res.add(new IdWithValueShim(acv));
    	return res;
    }
    
    /**
     * returns list of pledge names available for selection - called by the JSP
     */
    public List<IdWithValueShim> getPledgeNames(){
    	return getShimsForCategoryClass(CategoryConstants.PLEDGES_NAMES_KEY, this.getPledgeTitleId());
    }
    
    public List<IdWithValueShim> getPledgeTypes(){
    	return getShimsForCategoryClass(CategoryConstants.PLEDGES_TYPES_KEY, null);
    }

    public List<IdWithValueShim> getTypesOfAssistance(){
    	return getShimsForCategoryClass(CategoryConstants.TYPE_OF_ASSISTENCE_KEY, null);
    }
    
    public List<IdWithValueShim> getAidModalities(){
    	return getShimsForCategoryClass(CategoryConstants.MODALITIES_KEY, null);
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
    
    /**
     * Map<AmpTheme, List<Program_Shim>>
     * @return
     */
	public Map<KeyValue, List<IdNamePercentage>> getProgsByScheme(){
		return distribute(selectedProgs, BY_ROOT_DISTRIBUTION);
	}
	
	/**
	 * all root programs used by this activity
	 * @return
	 */
	public Collection<KeyValue> getAllUsedRootProgs(){
		return getProgsByScheme().keySet();
	}
	
	/**
	 * returns list of all the existant root programs in the system
	 * @return
	 */
	public List<DisableableKeyValue> getAllRootPrograms()
	{
		return mergeLists(DISABLEABLE_KV_PLEASE_SELECT, Lists.transform(ProgramUtil.getParentThemes(), new Function<AmpTheme, DisableableKeyValue>() {
			public DisableableKeyValue apply(AmpTheme theme) {return new DisableableKeyValue(theme.getAmpThemeId(), theme.getName(), true);};
		}));
	}
		
	/**
     * returns set of ids all the selected programs
     * @return Set<ACVL.id>
     */
    public Set<Long> getAllPrograms(){
    	return collectIds(this.getSelectedProgs());
    }
    
    
    /**
     * computes list of programs to be disabled on the JSP page
     * @return
     */
	public List<DisableableKeyValue> getAllLegalPrograms()
	{
		Set<Long> selectedProgIds = getAllPrograms();
        Set<Long> forbiddenPrograms = ProgramUtil.getRecursiveChildrenOfPrograms(selectedProgIds);
        forbiddenPrograms.addAll(ProgramUtil.getRecursiveAscendantsOfPrograms(selectedProgIds));
        List<DisableableKeyValue> res = new ArrayList<>();
        findProgramsRecursively(res, this.selectedRootProgram, forbiddenPrograms, selectedProgIds, "");
        return mergeLists(DISABLEABLE_KV_PLEASE_SELECT, res);
	};
	
	
    /**
     * Map<AmpTheme, List<Program_Shim>>
     * @return
     */
	public Map<KeyValue, List<IdNamePercentage>> getSectorsByScheme()
	{
		return distribute(selectedSectors, BY_ROOT_DISTRIBUTION);
	}
	
	/**
	 * all root programs used by this activity
	 * @return
	 */
	public Collection<KeyValue> getAllUsedRootSectors(){
		return getSectorsByScheme().keySet();
	}
	
	/**
	 * returns list of all the existant root programs in the system
	 * @return
	 */
	public List<DisableableKeyValue> getAllRootSectors()
	{
		return mergeLists(DISABLEABLE_KV_PLEASE_SELECT, Lists.transform(SectorUtil.getAllSectorSchemes() , new Function<AmpSectorScheme, DisableableKeyValue>() {
			public DisableableKeyValue apply(AmpSectorScheme theme) {return new DisableableKeyValue(theme.getAmpSecSchemeId(), theme.getSecSchemeName(), true);};
		}));
	}
		
	/**
     * returns set of ids all the selected programs
     * @return Set<ACVL.id>
     */
    public Set<Long> getAllSectors(){
    	return collectIds(this.getSelectedSectors());
    }
    
    
    /**
     * computes list of sectors to be displayed / disabled on the JSP page
     * @return
     */
	public List<DisableableKeyValue> getAllLegalSectors()
	{
		Set<Long> selectedSectorIds = getAllSectors();
        Set<Long> forbiddenSectors = SectorUtil.getRecursiveChildrenOfSectors(selectedSectorIds);
        forbiddenSectors.addAll(SectorUtil.getRecursiveAscendantsOfSectors(selectedSectorIds));
        List<DisableableKeyValue> res = new ArrayList<>();
        List<AmpSector> rootSectors = SectorUtil.getAllParentSectors(this.selectedRootSector);
        for(AmpSector rootSector:rootSectors)
        	findSectorsRecursively(res, rootSector.getAmpSectorId(), forbiddenSectors, selectedSectorIds, "");
        return mergeLists(DISABLEABLE_KV_PLEASE_SELECT, res);
	};

	public Set<Long> collectIds(Collection<IdNamePercentage> in){
    	Set<Long> res = new HashSet<Long>();
    	for(IdNamePercentage fpl:in)
    		res.add(fpl.getId());
    	return res;
	}

    void findProgramsRecursively(List<DisableableKeyValue> res, Long themeId, Set<Long> forbidden, Set<Long> selected, String prefix)
    {
    	if (themeId == null || themeId <= 0)
    		return;
    	
    	AmpTheme theme = ProgramUtil.getThemeById(themeId);
    	res.add(new DisableableKeyValue(theme.getAmpThemeId(), prefix + theme.getName(), !forbidden.contains(themeId)));
    	if (selected.contains(themeId))
    		return; // stop iterating when we're down to something selected
    	
    	for(AmpTheme subTheme:theme.getSiblings())
    		findProgramsRecursively(res, subTheme.getAmpThemeId(), forbidden, selected, prefix + "» ");
    }
    
    void findSectorsRecursively(List<DisableableKeyValue> res, Long sectorId, Set<Long> forbidden, Set<Long> selected, String prefix)
    {
    	if (sectorId == null || sectorId <= 0)
    		return;
    	
    	AmpSector sector = SectorUtil.getAmpSector(sectorId);
    	res.add(new DisableableKeyValue(sector.getAmpSectorId(), prefix + sector.getName(), !forbidden.contains(sectorId)));
    	if (selected.contains(sectorId))
    		return; // stop iterating when we're down to something selected
    	
    	for(AmpSector subSector:sector.getSiblings())
    		findSectorsRecursively(res, subSector.getAmpSectorId(), forbidden, selected, prefix + "» ");
    }
    
    public List<IdWithValueShim> getValidCurrencies(){
   		return Lists.transform(CurrencyUtil.getUsableCurrencies(), AMP_CURRENCY_TO_ID_WITH_SHIM);
    }
   		
    public boolean getFundingShowTypeOfAssistance(){
    	//<field:display name="Pledge Funding - Type Of Assistance" feature="Pledge Funding">
    	return FeaturesUtil.isVisibleField("Pledge Funding - Type Of Assistance");
    }
    
    public boolean getFundingShowAidModality() {
    	//<field:display name="Pledge Funding - Aid Modality" feature="Pledge Funding">
    	return FeaturesUtil.isVisibleField("Pledge Funding - Aid Modality");
    }
    
    public int deleteUniquelyIdentifiable(Collection<? extends UniquelyIdentifiable> col, long id){
    	int res = 0;
    	Iterator<? extends UniquelyIdentifiable> it = col.iterator();
    	while(it.hasNext()){
    		UniquelyIdentifiable elem = it.next();
    		if (elem.getUniqueId() == id){
    			res ++;
    			it.remove();
    		}
    	}
    	return res;
    }
    
    public void addNewPledgeFundingEntry()
    {
    	this.selectedFunding.add(new FundingPledgesDetailsShim(CurrencyUtil.getDefaultCurrency()));
    }
    
    /**
     * stupid block to make Struts happy for indexed properties. 
     * Please see http://www.coderanch.com/t/508028/Struts/Struts-queryString-indexed-properties-parsed
     */
    public IdNamePercentage getSelectedLocs(int index){return selectedLocs.get(index);}
    public void setSelectedLocs(int index, IdNamePercentage entry) {this.selectedLocs.set(index, entry);}
    public List<IdNamePercentage> getSelectedLocsList(){return selectedLocs;}
    public void setSelectedLocsList(List<IdNamePercentage> selectedLocs){this.selectedLocs = selectedLocs;}
    
    public IdNamePercentage getSelectedProgs(int index){return selectedProgs.get(index);}
    public void setSelectedProgs(int index, IdNamePercentage entry) {this.selectedProgs.set(index, entry);}
    public List<IdNamePercentage> getSelectedProgsList(){return selectedProgs;}
    public void setSelectedProgsList(List<IdNamePercentage> selectedProgs){this.selectedProgs = selectedProgs;}

    public IdNamePercentage getSelectedSectors(int index){return selectedSectors.get(index);}
    public void setSelectedSectors(int index, IdNamePercentage entry) {this.selectedSectors.set(index, entry);}
    public List<IdNamePercentage> getSelectedSectorsList(){return selectedSectors;}
    public void setSelectedSectorsList(List<IdNamePercentage> selectedSectors){this.selectedSectors = selectedSectors;}

    public FundingPledgesDetailsShim getSelectedFunding(int index){return selectedFunding.get(index);}
    public void setSelectedSectors(int index, FundingPledgesDetailsShim entry) {this.selectedFunding.set(index, entry);}
    public List<FundingPledgesDetailsShim> getSelectedFundingList(){return selectedFunding;}
    public void setSelectedFundingList(List<FundingPledgesDetailsShim> selectedFunding){this.selectedFunding = selectedFunding;}

}


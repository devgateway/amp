package org.digijava.module.fundingpledges.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.ar.ARUtil;
import org.dgfoundation.amp.forms.ValidationError;
import org.dgfoundation.amp.onepager.models.AmpActivityModel;
import org.dgfoundation.amp.onepager.util.ActivityGatekeeper;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.admin.helper.AmpPledgeFake;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.LuceneUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.categorymanager.util.IdWithValueShim;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesDetails;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesDocument;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesLocation;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesProgram;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesSector;
import org.digijava.module.fundingpledges.dbentity.PledgesEntityHelper;
import org.digijava.module.fundingpledges.form.DocumentShim;
import org.digijava.module.fundingpledges.form.FundingPledgesDetailsShim;
import org.digijava.module.fundingpledges.form.IdNamePercentage;
import org.digijava.module.fundingpledges.form.PledgeForm;
import org.digijava.module.fundingpledges.form.PledgeFormContact;
import org.digijava.module.fundingpledges.form.TransientDocumentShim;
import org.hibernate.Session;

import org.digijava.module.admin.helper.AmpPledgeFake;

public class SavePledge extends Action {
	private static Logger logger = Logger.getLogger(SavePledge.class);
	
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
    {			
    		PledgeForm plForm = (PledgeForm) form;
    		List<ValidationError> errors = new ArrayList<>();
    		try
    		{
    			errors = do_save(plForm);
    			if (errors.isEmpty())
    			{
    				ARUtil.writeResponse(response, "ok");
    				AddPledge.markPledgeEditorClosed(request.getSession());
    				return null;
    			}
    		}
    		catch(Exception e){
    			errors.add(new ValidationError("Error while trying to save: " + e.getLocalizedMessage()));
    			logger.error("exception while trying to save pledge", e);
    		}
    		finally {
    			try {
    				ServletContext sc = request.getServletContext();
    				boolean newPledge = plForm.isNewPledge();
    				
    				
    				Long id = plForm.getPledgeId();
    				
//    				IdWithValueShim idv = null;
    				if (plForm.getPledgeNames().size() > 0)
    					plForm.getPledgeNames().get(0);
//    				idv = plForm.getEffectiveName();
//    				String name = idv.value;
    				String additionalInfo = plForm.getAdditionalInformation();
    				String name = plForm.getEffectiveName();
    						
    				LuceneUtil.addUpdatePledge(sc.getRealPath("/"), !newPledge, new AmpPledgeFake(name, id, additionalInfo));
    				
    			} catch (Exception e) {
    				logger.error("error while trying to update lucene logs:", e);
    			}		
    		}
    		// gone till here -> errors is not empty
			JSONArray arr = new JSONArray();
			String[] fields = new String[] {"errMsg"};
			for(ValidationError err:errors)
				arr.put(new JSONObject(err, fields));
			String errs = arr.toString();
			ARUtil.writeResponse(response, errs);
			return null;
    }
    
    /**
     * returns true IFF we are allowed to save a pledge with a given name
     * @param plForm
     * @param pledges - the pledges instance which will take the load
     * @return
     */
    protected boolean checkNameUniqueness(PledgeForm plForm){
    	FundingPledges preexistingPledgeWithSameName = PledgesEntityHelper.getPledgesByFreeTextName(plForm.getTitleFreeText());
    	if (preexistingPledgeWithSameName == null)
    		return true; //nothing to do
    	
    	// same name existing, let's see whether we are overriding it (allowed) or creating a new one
    	if (plForm.isNewPledge())
    		return false; // not allowed to create a new pledge with preexisting name
    	
    	return plForm.getPledgeId() == preexistingPledgeWithSameName.getId();
    }
    
    protected List<ValidationError> do_save(PledgeForm plForm) throws Exception // it might die, ALWAYS check for exceptions and forward cleanly by AJAX
    {    	
    	Session session = PersistenceManager.getSession();
    	
    	List<ValidationError> res = new ArrayList<>();
    	
		if (plForm.getUseFreeText() && (!checkNameUniqueness(plForm)))
		{
			res.add(new org.dgfoundation.amp.forms.ValidationError(TranslatorWorker.translateText("A different pledge with the same name exists")));
			return res;
		}
		
    	FundingPledges pledge;
    	if (plForm.isNewPledge()){
    		pledge = new FundingPledges();
    	} else{
    		pledge = PledgesEntityHelper.getPledgesById(plForm.getPledgeId());
    	}

    	session.saveOrUpdate(pledge);
    	//if (FeaturesUtil.isVisibleField("Use Free Text")){
  		pledge.setTitleFreeText(plForm.getTitleFreeText());  // copy both - one of them will be null and that's it
//   		}else{
  		pledge.setTitle(CategoryManagerUtil.getAmpCategoryValueFromDb(plForm.getPledgeTitleId()));
  		pledge.setStatus(CategoryManagerUtil.getAmpCategoryValueFromDb(plForm.getPledgeStatusId()));
 
  		pledge.setOrganizationGroup(PledgesEntityHelper.getOrgGroupById(plForm.getSelectedOrgGrpId()));
    	pledge.setAdditionalInformation(plForm.getAdditionalInformation());
    	pledge.setWhoAuthorizedPledge(plForm.getWhoAuthorizedPledge());
    	pledge.setFurtherApprovalNedded(plForm.getFurtherApprovalNedded());
    	    	
    	res.addAll(do_save_contact1(pledge, plForm.getContact1()));
    	res.addAll(do_save_contact2(pledge, plForm.getContact2()));
    	res.addAll(do_save_sectors(session, pledge, plForm.getSelectedSectors()));
    	res.addAll(do_save_programs(session, pledge, plForm.getSelectedProgs()));
    	res.addAll(do_save_locations(session, pledge, plForm.getSelectedLocs()));
    	res.addAll(do_save_funding(session, pledge, plForm.getSelectedFunding()));
    	res.addAll(do_save_documents(session, pledge, plForm.getSelectedDocsList(), plForm.getInitialDocuments()));
    	session.saveOrUpdate(pledge);
    		
    	return res;
	}
    
    protected List<ValidationError> do_save_contact1(FundingPledges pledge, PledgeFormContact contact1) throws Exception
    {
       	pledge.setContactName(contact1.getName());
    	pledge.setContactTitle(contact1.getTitle());
    	if (contact1.getOrgId()!=null && contact1.getOrgId().length()!=0) {
    		pledge.setContactOrganization(PledgesEntityHelper.getOrganizationById(Long.parseLong(contact1.getOrgId())));
		}
    	pledge.setContactMinistry(contact1.getMinistry());
    	pledge.setContactAddress(contact1.getAddress());
    	pledge.setContactTelephone(contact1.getTelephone());
    	pledge.setContactFax(contact1.getFax());
    	pledge.setContactEmail(contact1.getEmail());
    	pledge.setContactAlternativeName(contact1.getAlternateName());
    	pledge.setContactAlternativeEmail(contact1.getAlternateEmail());
    	pledge.setContactAlternativeTelephone(contact1.getAlternateTelephone());
    	
    	return new ArrayList<>();
    }
    
    protected List<ValidationError> do_save_contact2(FundingPledges pledge, PledgeFormContact contact2) throws Exception
    {
    	pledge.setContactName_1(contact2.getName());
    	pledge.setContactTitle_1(contact2.getTitle());
    	if (contact2.getOrgId()!=null && contact2.getOrgId().length()!=0) {
    		pledge.setContactOrganization_1(PledgesEntityHelper.getOrganizationById(Long.parseLong(contact2.getOrgId())));
    	}
    	pledge.setContactMinistry_1(contact2.getMinistry());
    	pledge.setContactAddress_1(contact2.getAddress());
    	pledge.setContactTelephone_1(contact2.getTelephone());
    	pledge.setContactFax_1(contact2.getFax());
    	pledge.setContactEmail_1(contact2.getEmail());
    	pledge.setContactAlternativeName_1(contact2.getAlternateName());
    	pledge.setContactAlternativeEmail_1(contact2.getAlternateEmail());
    	pledge.setContactAlternativeTelephone_1(contact2.getAlternateTelephone());
    	
    	return new ArrayList<>();
    }

    protected List<ValidationError> do_save_sectors(Session session, FundingPledges pledge, List<IdNamePercentage> selectedSectors) throws Exception
    {
		Set<FundingPledgesSector> pledgesSectors = pledge.getSectorlist();
		if (pledgesSectors == null)		{
			pledgesSectors = new HashSet<>();
			pledge.setSectorlist(pledgesSectors);
		}
		pledgesSectors.clear();
		for(IdNamePercentage sector:selectedSectors)
		{
			FundingPledgesSector fps = new FundingPledgesSector();
			fps.setPledgeid(pledge);
			fps.setSectorpercentage(sector.getPercentageOrNull());
			fps.setSector(SectorUtil.getAmpSector(sector.getId()));
			session.save(fps);
			pledgesSectors.add(fps);
		}
		return new ArrayList<>();
    }
    
    protected List<ValidationError> do_save_programs(Session session, FundingPledges pledge, List<IdNamePercentage> selectedPrograms) throws Exception
    {
		Set<FundingPledgesProgram> pledgesPrograms = pledge.getProgramlist();
		if (pledgesPrograms == null)		{
			pledgesPrograms = new HashSet<>();
			pledge.setProgramlist(pledgesPrograms);
		}
		pledgesPrograms.clear();
		for(IdNamePercentage program:selectedPrograms)
		{
			FundingPledgesProgram fps = new FundingPledgesProgram();
			fps.setPledgeid(pledge);
			fps.setProgrampercentage(program.getPercentageOrNull());
			fps.setProgram(ProgramUtil.getThemeById(program.getId()));
			session.save(fps);
			pledgesPrograms.add(fps);
		}
    	return new ArrayList<>();
    }

    protected List<ValidationError> do_save_locations(Session session, FundingPledges pledge, List<IdNamePercentage> selectedLocs) throws Exception
    {
		Set<FundingPledgesLocation> pledgesLocs = pledge.getLocationlist();
		if (pledgesLocs == null){
			pledgesLocs = new HashSet<>();
			pledge.setLocationlist(pledgesLocs);
		}
		pledgesLocs.clear();
		for(IdNamePercentage location:selectedLocs)
		{
			FundingPledgesLocation fps = new FundingPledgesLocation();
			fps.setPledgeid(pledge);
			fps.setLocationpercentage(location.getPercentageOrNull());
			fps.setLocation(DynLocationManagerUtil.getLocation(location.getId(), true));
			session.save(fps);
			pledgesLocs.add(fps);
		}
    	return new ArrayList<>();
    }

    protected List<ValidationError> do_save_funding(Session session, FundingPledges pledge, List<FundingPledgesDetailsShim> selectedFunding) throws Exception
    {
		Set<FundingPledgesDetails> pledgeFunds = pledge.getFundingPledgesDetails();
		if (pledgeFunds == null){
			pledgeFunds = new HashSet<>();
			pledge.setFundingPledgesDetails(pledgeFunds);
		}
		pledgeFunds.clear();
		boolean dateRangeEnabled = FundingPledgesDetails.isDateRangeEnabled();
		List<ValidationError> errs = new ArrayList<>();
		
		for(FundingPledgesDetailsShim shim:selectedFunding){
			if (dateRangeEnabled && shim.getFundingDateEndAsDate() != null && shim.getFundingDateStartAsDate() != null && 
					shim.getFundingDateEndAsDate().before(shim.getFundingDateStartAsDate())){
				errs.add(new ValidationError("Timeframe Start must be before Timeframe End"));
			}
			else {
				FundingPledgesDetails fps = shim.buildFundingPledgesDetail(pledge);
				session.save(fps);
				pledgeFunds.add(fps);
			}
		}
    	return errs;
    }
    
    protected List<ValidationError> do_save_documents(Session session, FundingPledges pledge, List<DocumentShim> docs, Set<String> preexistingIds){
    	
		Set<FundingPledgesDocument> pledgeDocs = pledge.getDocuments();
		if (pledgeDocs == null){
			pledgeDocs = new HashSet<>();
			pledge.setDocuments(pledgeDocs);
		}
		pledgeDocs.clear();
    	
    	Set<String> stillExistingIds = new HashSet<String>();
    	List<ValidationError> errs = new ArrayList<>();
    	for(DocumentShim doc:docs){
    		/**
    		 * any document falls into one of the following 3 categories:
    		 * 1. exists in both <strong>docs</strong> and <strong>preexistingIds</strong> (KEPT file); ACTION: nothing except creating a FPD entry
    		 * 2. only exists in <strong>docs</strong> (ADDED file); ACTION: (a) check it is a TemporaryDocumentShim (b) serialize to JR  (c) create FPD entry
    		 * 3. only exists in <strong>preexistingIds</strong>(DELETED file); ACTION: delete from repo
    		 */
    		FundingPledgesDocument fpd = null;
    		String uuid;
   			uuid = doc.getUuid();
   			if (uuid == null || uuid.isEmpty()){
   				// no UUID -> temp file -> just added
   				TransientDocumentShim justAddedFile = (TransientDocumentShim) doc;
   				fpd = justAddedFile.serializeAndGetPledgeEntry(pledge);
   			}
   			else
   			{
   				// should be preexisting file
   				if (!preexistingIds.contains(uuid)){ // sanity check
   					throw new RuntimeException("Document with UUID " + uuid + " not existing in preexistingIds!");
   				}
   				stillExistingIds.add(uuid);
   				fpd = doc.buildPledgeEntry(pledge);
   			}
   			session.save(fpd);
   			pledgeDocs.add(fpd);
    	}
    	Set<String> deletedUuids = new HashSet<>(preexistingIds);
    	deletedUuids.removeAll(stillExistingIds);
     	if (!deletedUuids.isEmpty()){
     		logger.warn("Pledge " + pledge.getId() + " (name = " + pledge.getEffectiveName() + ") has been left without some documents, deleting...");
    		for(String uuidToDelete:deletedUuids){
    			DocumentManagerUtil.deleteNode(null, uuidToDelete);
    		}
    	}
    	return errs;
    }
}

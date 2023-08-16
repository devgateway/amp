package org.digijava.module.fundingpledges.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.ARUtil;
import org.dgfoundation.amp.forms.ValidationError;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.admin.helper.AmpPledgeFake;
import org.digijava.module.aim.util.*;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;
import org.digijava.module.fundingpledges.dbentity.*;
import org.digijava.module.fundingpledges.form.*;
import org.hibernate.Session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class SavePledge extends Action {
    private static Logger logger = Logger.getLogger(SavePledge.class);
    
//  AmpPledgeFake pledge = null;
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
    {
        logger.info("We do pledges here in savePledge");

        PledgeForm plForm = (PledgeForm) form;
            List<ValidationError> errors = new ArrayList<>();
            try
            {
                errors = doSave(plForm, request);
                if (errors.isEmpty())
                {
                    ARUtil.writeResponse(response, "ok");
                    AddPledge.markPledgeEditorClosed(request.getSession());
                    plForm.reset();
                    return null;
                }
            }
            catch(Exception e){
                errors.add(new ValidationError("Error while trying to save: " + e.getLocalizedMessage()));
                logger.error("exception while trying to save pledge", e);
            }
            // gone till here -> errors is not empty
        JsonNodeFactory nf = JsonNodeFactory.instance;
        ArrayNode arr = nf.arrayNode();
        for (ValidationError err : errors) {
            arr.add(nf.objectNode().put("errMsg", err.errMsg));
        }
        String errs = new ObjectMapper().writeValueAsString(arr);
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
        
        return Objects.equals(plForm.getPledgeId(), preexistingPledgeWithSameName.getId());
    }
    
    protected List<ValidationError> doSave(PledgeForm plForm, HttpServletRequest request) throws Exception {
        String action = "add";
        Session session = PersistenceManager.getRequestDBSession();

        List<ValidationError> validationErrors = new ArrayList<>();

        if (plForm.getUseFreeText() && (!checkNameUniqueness(plForm))) {
            validationErrors.add(new org.dgfoundation.amp.forms.ValidationError(
                    TranslatorWorker.translateText("A different pledge with the same name exists")));
        }
        validationErrors.addAll(validateFunding(plForm.getSelectedFunding()));
        validationErrors.addAll(validateDocuments(plForm.getSelectedDocs()));
        if (validationErrors.size() > 0) {
            return validationErrors;
        }

        FundingPledges pledge;
        logger.info("Pledge here: "+plForm);
        if (plForm.isNewPledge()) {
            pledge = new FundingPledges();
            pledge.setCreatedDate(new Date());
        } else {
            pledge = PledgesEntityHelper.getPledgesById(plForm.getPledgeId());
            action = "update";
        }
        pledge.setTitleFreeText(plForm.getTitleFreeText());
        pledge.setTitle(CategoryManagerUtil.getAmpCategoryValueFromDb(plForm.getPledgeTitleId()));
        pledge.setStatus(CategoryManagerUtil.getAmpCategoryValueFromDb(plForm.getPledgeStatusId()));
        pledge.setOrganizationGroup(PledgesEntityHelper.getOrgGroupById(plForm.getSelectedOrgGrpId()));
        pledge.setAdditionalInformation(plForm.getAdditionalInformation());
        pledge.setWhoAuthorizedPledge(plForm.getWhoAuthorizedPledge());
        pledge.setFurtherApprovalNedded(plForm.getFurtherApprovalNedded());

        session.saveOrUpdate(pledge);
        logger.info("Saved pledge: "+pledge);
        AuditLoggerUtil.logObject(request, pledge, action, null);

        doSaveContact1(pledge, plForm.getContact1());
        doSaveContact2(pledge, plForm.getContact2());
        doSaveSectors(session, pledge, plForm.getSelectedSectors());
        doSavePrograms(session, pledge, plForm.getSelectedProgs());
        doSaveLocations(session, pledge, plForm.getSelectedLocs());
        doSaveFunding(session, pledge, plForm.getSelectedFunding());
        doSaveDocuments(session, pledge, plForm.getSelectedDocs(), plForm.getInitialDocuments());
        session.saveOrUpdate(pledge);
        logger.info("Saved pledge again: "+pledge);
        boolean newPledge = plForm.isNewPledge();
        try {
            LuceneUtil.addUpdatePledge(TLSUtils.getRequest().getServletContext().getRealPath("/"), !newPledge,
                    new AmpPledgeFake(pledge.getEffectiveName(), pledge.getId(), pledge.getAdditionalInformation()));
        } catch (Exception e) {
            logger.error("error while trying to update lucene logs:", e);
        }
        logger.info("Res: "+validationErrors);


        return validationErrors;
    }
    
    protected  void doSaveContact1(FundingPledges pledge, PledgeFormContact contact1) {
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

    }
    
    protected void doSaveContact2(FundingPledges pledge, PledgeFormContact contact2) {
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
    }

    protected  void doSaveSectors(Session session, FundingPledges pledge, List<IdNamePercentage> selectedSectors) {
        Set<FundingPledgesSector> pledgesSectors = pledge.getSectorlist();
        if (pledgesSectors == null)     {
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
    }
    
    protected void doSavePrograms(Session session, FundingPledges pledge, List<IdNamePercentage> selectedPrograms) {
        Set<FundingPledgesProgram> pledgesPrograms = pledge.getProgramlist();
        if (pledgesPrograms == null)        {
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
    }

    protected void doSaveLocations(Session session, FundingPledges pledge, List<IdNamePercentage> selectedLocs) {
        Set<FundingPledgesLocation> pledgesLocs = pledge.getLocationlist();
        if (pledgesLocs == null) {
            pledgesLocs = new HashSet<>();
            pledge.setLocationlist(pledgesLocs);
        }
        pledgesLocs.clear();
        for (IdNamePercentage location : selectedLocs) {
            FundingPledgesLocation fps = new FundingPledgesLocation();
            fps.setPledgeid(pledge);
            fps.setLocationpercentage(location.getPercentageOrNull());
            fps.setLocation(DynLocationManagerUtil.getLocation(location.getId(), true));
            session.save(fps);
            pledgesLocs.add(fps);
        }
    }

    /**
     * method to validate pledge funding details. This should be invalid since its validated in the front end
     * @param selectedFunding
     * @return
     */
    protected List<ValidationError> validateFunding(List<FundingPledgesDetailsShim> selectedFunding) {
        List<ValidationError> errs = new ArrayList<>();
        boolean dateRangeEnabled = FundingPledgesDetails.isDateRangeEnabled();
        for (FundingPledgesDetailsShim shim:selectedFunding) {
            if (dateRangeEnabled && shim.getFundingDateEndAsDate() != null
                    && shim.getFundingDateStartAsDate() != null && shim.getFundingDateEndAsDate().before(shim
                    .getFundingDateStartAsDate())) {
                errs.add(new ValidationError("Timeframe Start must be before Timeframe End"));
            }
        }
        return errs;
    }

    protected void doSaveFunding(Session session, FundingPledges pledge, List<FundingPledgesDetailsShim>
            selectedFunding) {
        Set<FundingPledgesDetails> pledgeFunds = pledge.getFundingPledgesDetails();
        if (pledgeFunds == null){
            pledgeFunds = new HashSet<>();
            pledge.setFundingPledgesDetails(pledgeFunds);
        }
        pledgeFunds.clear();
        boolean dateRangeEnabled = FundingPledgesDetails.isDateRangeEnabled();

        for (FundingPledgesDetailsShim shim:selectedFunding) {
            FundingPledgesDetails fps = shim.buildFundingPledgesDetail(pledge);
            session.save(fps);
            pledgeFunds.add(fps);

        }
    }
    protected List<ValidationError> validateDocuments(List<DocumentShim> docs) {
        List<ValidationError> errors =  new ArrayList<>();
        Optional.ofNullable(docs).orElse(Collections.emptyList()).stream().forEach(document-> {
            if (document.getTitle() == null || document.getTitle().isEmpty()) {
                errors.add(new ValidationError("You need to submit or cancel the pending document"));
            }
        });
        return errors;
    }
    protected void doSaveDocuments(Session session, FundingPledges pledge, List<DocumentShim> docs, Set<String>
            preexistingIds) {
        
        Set<FundingPledgesDocument> pledgeDocs = pledge.getDocuments();
        if (pledgeDocs == null){
            pledgeDocs = new HashSet<>();
            pledge.setDocuments(pledgeDocs);
        }
        pledgeDocs.clear();
        
        Set<String> stillExistingIds = new HashSet<String>();
        //List<ValidationError> errs = new ArrayList<>();
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
        //return errs;
    }
}

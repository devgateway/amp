package org.digijava.module.fundingpledges.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.wicket.validation.ValidationError;
import org.dgfoundation.amp.ar.ARUtil;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesDetails;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesLocation;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesProgram;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesSector;
import org.digijava.module.fundingpledges.dbentity.PledgesEntityHelper;
import org.digijava.module.fundingpledges.form.FundingPledgesDetailsShim;
import org.digijava.module.fundingpledges.form.IdNamePercentage;
import org.digijava.module.fundingpledges.form.PledgeForm;
import org.digijava.module.fundingpledges.form.PledgeFormContact;
import org.hibernate.Session;

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
    				return null;
    			}
    			ARUtil.writeResponse(response, errors.toString());
    			return null;
    		}
    		catch(Exception e)
    		{
    			ARUtil.writeResponse(response, "Error while trying to save: " + e.getLocalizedMessage());
    			logger.error("error while trying to save pledge", e);
    			return null;
    		}
    }
    
    protected List<ValidationError> do_save(PledgeForm plForm) throws Exception // it might die, ALWAYS check for exceptions and forward cleanly by AJAX
    {    	
    	Session session = PersistenceManager.getRequestDBSession(false);
    	
    	FundingPledges pledge;
    	List<ValidationError> res = new ArrayList<>();
    	
    	if (plForm.getPledgeId() == null)
    	{
    		pledge = new FundingPledges();
    		pledge.setId(plForm.getPledgeId());
    	} else{
    		pledge = PledgesEntityHelper.getPledgesById(plForm.getPledgeId());
    	}	
    	session.saveOrUpdate(pledge);	
    	//if (FeaturesUtil.isVisibleField("Use Free Text")){
  		pledge.setTitleFreeText(plForm.getTitleFreeText());  // copy both - one of them will be null and that's it
//   		}else{
  		pledge.setTitle(CategoryManagerUtil.getAmpCategoryValueFromDb(plForm.getPledgeTitleId()));
 
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
    	session.saveOrUpdate(pledge);
    		
//    		if (plForm.getPledgeId()!=null && plForm.getPledgeId()!=0) {
//    			PledgesEntityHelper.updatePledge(pledge,pledgessector,plForm);
//			} else {
//				PledgesEntityHelper.savePledge(pledge,pledgessector,plForm);
//			}
    		
    		
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
			fps.setSectorpercentage(sector.getPercentage());
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
			fps.setProgrampercentage(program.getPercentage());
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
			fps.setLocationpercentage(location.getPercentage());
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
		for(FundingPledgesDetailsShim shim:selectedFunding)
		{
			FundingPledgesDetails fps = new FundingPledgesDetails();
			fps.setPledgeid(pledge);
			fps.setAmount(shim.getAmount());
			fps.setAidmodality(CategoryManagerUtil.loadAcvOrNull(shim.getAidModalityId()));
			fps.setCurrency(shim.getCurrencyId() == null ? null : CurrencyUtil.getAmpcurrency(shim.getCurrencyId()));
			fps.setFundingYear(shim.getFundingYear() == null ? null : shim.getFundingYear().toString());
			fps.setPledgetype(CategoryManagerUtil.loadAcvOrNull(shim.getPledgeTypeId()));
			fps.setTypeOfAssistance(CategoryManagerUtil.loadAcvOrNull(shim.getTypeOfAssistanceId()));
			session.save(fps);
			pledgeFunds.add(fps);
		}
    	return new ArrayList<>();
    }

}
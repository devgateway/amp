package org.digijava.module.fundingpledges.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesDetails;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesSector;
import org.digijava.module.fundingpledges.dbentity.PledgesEntityHelper;
import org.digijava.module.fundingpledges.form.PledgeForm;

public class SavePledge extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
			
    		PledgeForm plForm = (PledgeForm) form;
    		
    		if (request.getParameter("cancel")!=null && request.getParameter("cancel").equals("true")) {
    			request.getSession().removeAttribute("cancel");
    			return mapping.findForward("forward");
			}
    		
    		FundingPledges pledge = null;
    		if (plForm.getFundingPledges()==null) {
    			pledge = new FundingPledges();
			} else {
				pledge = plForm.getFundingPledges();
			}
    		
    		pledge.setId(plForm.getPledgeId());
    		//pledge.setTitle(plForm.getPledgeTitle());
    		pledge.setTitle(CategoryManagerUtil.getAmpCategoryValueFromDb(plForm.getPledgeTitleId()));
    		pledge.setOrganizationGroup(PledgesEntityHelper.getOrgGroupById(Long.parseLong(plForm.getSelectedOrgGrpId())));
    		pledge.setAdditionalInformation(plForm.getAdditionalInformation());
    		pledge.setWhoAuthorizedPledge(plForm.getWhoAuthorizedPledge());
    		pledge.setFurtherApprovalNedded(plForm.getFurtherApprovalNedded());
    		
    		pledge.setContactName(plForm.getContact1Name());
    		pledge.setContactTitle(plForm.getContact1Title());
    		if (plForm.getContact1OrgId()!=null && plForm.getContact1OrgId().length()!=0) {
    			pledge.setContactOrganization(PledgesEntityHelper.getOrganizationById(Long.parseLong(plForm.getContact1OrgId())));
			}
    		pledge.setContactMinistry(plForm.getContact1Ministry());
    		pledge.setContactAddress(plForm.getContact1Address());
    		pledge.setContactTelephone(plForm.getContact1Telephone());
    		pledge.setContactFax(plForm.getContact1Fax());
    		pledge.setContactEmail(plForm.getContact1Email());
    		pledge.setContactAlternativeName(plForm.getContactAlternate1Name());
    		pledge.setContactAlternativeEmail(plForm.getContactAlternate1Email());
    		pledge.setContactAlternativeTelephone(plForm.getContactAlternate1Telephone());
    		
    		pledge.setContactName_1(plForm.getContact2Name());
    		pledge.setContactTitle_1(plForm.getContact2Title());
    		if (plForm.getContact2OrgId()!=null && plForm.getContact2OrgId().length()!=0) {
    			pledge.setContactOrganization_1(PledgesEntityHelper.getOrganizationById(Long.parseLong(plForm.getContact2OrgId())));
    		}
    		pledge.setContactMinistry_1(plForm.getContact2Ministry());
    		pledge.setContactAddress_1(plForm.getContact2Address());
    		pledge.setContactTelephone_1(plForm.getContact2Telephone());
    		pledge.setContactFax_1(plForm.getContact2Fax());
    		pledge.setContactEmail_1(plForm.getContact2Email());
    		pledge.setContactAlternativeName_1(plForm.getContactAlternate2Name());
    		pledge.setContactAlternativeEmail_1(plForm.getContactAlternate2Email());
    		pledge.setContactAlternativeTelephone_1(plForm.getContactAlternate2Telephone());
    		
    		Set<FundingPledgesSector> pledgessector = new HashSet<FundingPledgesSector>();
    		if(plForm.getPledgeSectors()!=null && plForm.getPledgeSectors().size()>0){
	    		for (Iterator sectorIt = plForm.getPledgeSectors().iterator(); sectorIt.hasNext();) {
	       		 	ActivitySector actSector = (ActivitySector) sectorIt.next();
					AmpSector sector=SectorUtil.getAmpSector(actSector.getSectorId());
					sector.setAmpSectorId(actSector.getSectorId());
					FundingPledgesSector fps = new FundingPledgesSector();
					fps.setSector(sector);
					fps.setSectorpercentage(actSector.getSectorPercentage());
					fps.setId(actSector.getId());
					pledgessector.add(fps);
				}
	    		//pledge.setSectorlist((Set<FundingPledgesSector>) fpsl);
    		}
    		String fundings = request.getParameter("fundings");
    		String funds[] = fundings.split(";");
    		plForm.setFundingPledgesDetails(new ArrayList<FundingPledgesDetails>());

    		for (int i = 0; i < funds.length; i++) {
    			if (funds[i].length()>1){
	    			String token[] = funds[i].split("_");
	     			FundingPledgesDetails fpd = new FundingPledgesDetails();
	     			if (token[0].length()>0){
	     				fpd.setId(Long.parseLong(token[0]));
	     			}
	    			fpd.setPledgetypeid(Long.parseLong(token[1]));
	    			fpd.setTypeOfAssistanceid(Long.parseLong(token[2]));
	    			fpd.setAmount((FormatHelper.parseDouble(token[3])));
	    			fpd.setCurrencycode(token[4]);
	    			//TODO find another way to do it this is not very elegant
	    			if (token[5].equalsIgnoreCase("unspecified")){
	    				fpd.setFundingYear(null);
	    			}else{
	    				fpd.setFundingYear(token[5]);
	    			}
	    			fpd.setAidmodalityid(Long.parseLong(token[6]));
	    			plForm.getFundingPledgesDetails().add(fpd);
    			}
			}
    		
    		/*
    		if(plForm.getSelectedLocs()!=null && plForm.getSelectedLocs().size()>0){
	    		Set<FundingPledgesLocation> pledgesloc = new HashSet<FundingPledgesLocation>();
	    		Collection<FundingPledgesLocation> fplc = plForm.getSelectedLocs();
	    		Iterator<FundingPledgesLocation> itl = fplc.iterator();
	    		while (itl.hasNext()) {
	    			FundingPledgesLocation fpl = (FundingPledgesLocation) itl.next();
	    			pledgesloc.add(fpl);
				}
	    		//pledge.setLocationlist((Set<FundingPledgesLocation>) pledgesloc);
    		}*/
    		/*
    		if(plForm.getFundingPledgesDetails()!=null && plForm.getFundingPledgesDetails().size()>0){
	    		Set<FundingPledgesDetails> fundingdetails = new HashSet<FundingPledgesDetails>();
	    		Collection<FundingPledgesDetails> fpdc = plForm.getFundingPledgesDetails();
	    		Iterator<FundingPledgesDetails> itf = fpdc.iterator();
	    		while (itf.hasNext()) {
	    			FundingPledgesDetails fpd = (FundingPledgesDetails) itf.next();
	    			fpd.setPledgeid(pledge);
	    			fundingdetails.add(fpd);
	    		}
	    		//pledge.setFundingPledgesDetails((Set<FundingPledgesDetails>) fpdl);
    		}*/
    		
    		
    		
    		if (plForm.getPledgeId()!=null && plForm.getPledgeId()!=0) {
    			/*if (!PledgesEntityHelper.getPledgesById(plForm.getPledgeId()).getTitle().equals(plForm.getPledgeTitle()) ||
    					PledgesEntityHelper.getPledgesById(plForm.getPledgeId()).getOrganization().getAmpOrgId() != Long.parseLong(plForm.getSelectedOrgId())) {
    				if (isTitleDuplicated(mapping,request,plForm)) {
    					return mapping.findForward("success");
    				}
				}*/
    			PledgesEntityHelper.updatePledge(pledge,pledgessector,plForm);
			} else {
				/*if (isTitleDuplicated(mapping,request,plForm)) {
					return mapping.findForward("success");
				}*/
				PledgesEntityHelper.savePledge(pledge,pledgessector,plForm);
			}
    		
    		
    		return mapping.findForward("forward");
		}


	/*private boolean isTitleDuplicated(ActionMapping mapping, HttpServletRequest request, PledgeForm plForm){
		ArrayList<FundingPledges> fpByNameAndOrg = PledgesEntityHelper.getPledgesByDonorAndTitle(Long.valueOf(plForm.getSelectedOrgId()), plForm.getPledgeTitle());
		if (fpByNameAndOrg != null) {
			if (fpByNameAndOrg.size()!=0) {
				ActionMessages errors=new ActionMessages();
				errors.add("incorrectTitle", new ActionMessage("error.aim.pledges.duplicatedTitle"));
				saveErrors(request, errors);
				request.getSession().setAttribute("duplicatedTitleError", errors);
        		//return mapping.findForward("success");
				return true;
			}
		}
		return false;
	}*/
}
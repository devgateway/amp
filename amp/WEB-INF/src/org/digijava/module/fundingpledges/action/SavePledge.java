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
import org.digijava.module.aim.util.FeaturesUtil;
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
//    		if (plForm.getFundingPledges()==null) {
//    			pledge = new FundingPledges();
//			} else {
//				pledge = plForm.getFundingPledges();
//			}
    		
    		pledge.setId(plForm.getPledgeId());
    		if (FeaturesUtil.isVisibleField("Use Free Text")){
    			pledge.setTitleFreeText(plForm.getTitleFreeText());
    		}else{
    			pledge.setTitle(CategoryManagerUtil.getAmpCategoryValueFromDb(plForm.getPledgeTitleId()));
    		}
    		pledge.setOrganizationGroup(PledgesEntityHelper.getOrgGroupById(plForm.getSelectedOrgGrpId()));
    		pledge.setAdditionalInformation(plForm.getAdditionalInformation());
    		pledge.setWhoAuthorizedPledge(plForm.getWhoAuthorizedPledge());
    		pledge.setFurtherApprovalNedded(plForm.getFurtherApprovalNedded());
    		
    		pledge.setContactName(plForm.getContact1().getName());
    		pledge.setContactTitle(plForm.getContact1().getTitle());
    		if (plForm.getContact1().getOrgId()!=null && plForm.getContact1().getOrgId().length()!=0) {
    			pledge.setContactOrganization(PledgesEntityHelper.getOrganizationById(Long.parseLong(plForm.getContact1().getOrgId())));
			}
    		pledge.setContactMinistry(plForm.getContact1().getMinistry());
    		pledge.setContactAddress(plForm.getContact1().getAddress());
    		pledge.setContactTelephone(plForm.getContact1().getTelephone());
    		pledge.setContactFax(plForm.getContact1().getFax());
    		pledge.setContactEmail(plForm.getContact1().getEmail());
    		pledge.setContactAlternativeName(plForm.getContact1().getAlternateName());
    		pledge.setContactAlternativeEmail(plForm.getContact1().getAlternateEmail());
    		pledge.setContactAlternativeTelephone(plForm.getContact1().getAlternateTelephone());
    		
    		pledge.setContactName_1(plForm.getContact2().getName());
    		pledge.setContactTitle_1(plForm.getContact2().getTitle());
    		if (plForm.getContact2().getOrgId()!=null && plForm.getContact2().getOrgId().length()!=0) {
    			pledge.setContactOrganization_1(PledgesEntityHelper.getOrganizationById(Long.parseLong(plForm.getContact2().getOrgId())));
    		}
    		pledge.setContactMinistry_1(plForm.getContact2().getMinistry());
    		pledge.setContactAddress_1(plForm.getContact2().getAddress());
    		pledge.setContactTelephone_1(plForm.getContact2().getTelephone());
    		pledge.setContactFax_1(plForm.getContact2().getFax());
    		pledge.setContactEmail_1(plForm.getContact2().getEmail());
    		pledge.setContactAlternativeName_1(plForm.getContact2().getAlternateName());
    		pledge.setContactAlternativeEmail_1(plForm.getContact2().getAlternateEmail());
    		pledge.setContactAlternativeTelephone_1(plForm.getContact2().getAlternateTelephone());
    		
//    		Set<FundingPledgesSector> pledgessector = new HashSet<FundingPledgesSector>();
//    		if(plForm.getPledgeSectors()!=null && plForm.getPledgeSectors().size()>0){
//	    		for (Iterator sectorIt = plForm.getPledgeSectors().iterator(); sectorIt.hasNext();) {
//	       		 	ActivitySector actSector = (ActivitySector) sectorIt.next();
//					AmpSector sector=SectorUtil.getAmpSector(actSector.getSectorId());
//					sector.setAmpSectorId(actSector.getSectorId());
//					FundingPledgesSector fps = new FundingPledgesSector();
//					fps.setSector(sector);
//					fps.setSectorpercentage(actSector.getSectorPercentage());
//					fps.setId(actSector.getId());
//					pledgessector.add(fps);
//				}
//	    	}
 //   		String fundings = request.getParameter("fundings");
  //  		String funds[] = fundings.split(";");
    		//plForm.setFundingPledgesDetails(new ArrayList<FundingPledgesDetails>());

//    		for (int i = 0; i < funds.length; i++) {
//    			if (funds[i].length()>1){
//	    			String token[] = funds[i].split("_");
//	     			FundingPledgesDetails fpd = new FundingPledgesDetails();
//	     			if (token[0].length()>0){
//	     				fpd.setId(Long.parseLong(token[0]));
//	     			}
//	    			fpd.setPledgetypeid(Long.parseLong(token[1]));
//	    			fpd.setTypeOfAssistanceid(Long.parseLong(token[2]));
//	    			fpd.setAmount((FormatHelper.parseDouble(token[3])));
//	    			fpd.setCurrencycode(token[4]);
//	    			//TODO find another way to do it this is not very elegant
//	    			if (token[5].equalsIgnoreCase("unspecified")){
//	    				fpd.setFundingYear(null);
//	    			}else{
//	    				fpd.setFundingYear(token[5]);
//	    			}
//	    			fpd.setAidmodalityid(Long.parseLong(token[6]));
//	    			//plForm.getFundingPledgesDetails().add(fpd);
//    			}
//			}
    		
//    		if (plForm.getPledgeId()!=null && plForm.getPledgeId()!=0) {
//    			PledgesEntityHelper.updatePledge(pledge,pledgessector,plForm);
//			} else {
//				PledgesEntityHelper.savePledge(pledge,pledgessector,plForm);
//			}
    		
    		
    		return mapping.findForward("forward");
		}
}
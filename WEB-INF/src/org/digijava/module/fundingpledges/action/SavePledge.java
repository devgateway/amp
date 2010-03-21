package org.digijava.module.fundingpledges.action;

import java.util.Collection;
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
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesDetails;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesLocation;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesSector;
import org.digijava.module.fundingpledges.form.PledgeForm;
import org.digijava.module.fundingpledges.util.PledgeUtil;

public class SavePledge extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
			
    		PledgeForm plForm = (PledgeForm) form;
    		
    		FundingPledges pledge = new FundingPledges();
    		pledge.setTitle(plForm.getPledgeTitle());
    		pledge.setOrganization(PledgeUtil.getOrganizationById(Long.parseLong(plForm.getSelectedOrgId())));
    		pledge.setAdditionalInformation(plForm.getAdditionalInformation());
    		
    		pledge.setContactName(plForm.getContact1Name());
    		pledge.setContactTitle(plForm.getContact1Title());
    		if (plForm.getContact1OrgId()!=null && plForm.getContact1OrgId().length()!=0) {
    			pledge.setContactOrganization(PledgeUtil.getOrganizationById(Long.parseLong(plForm.getContact1OrgId())));
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
    			pledge.setContactOrganization_1(PledgeUtil.getOrganizationById(Long.parseLong(plForm.getContact2OrgId())));
    		}
    		pledge.setContactMinistry_1(plForm.getContact2Ministry());
    		pledge.setContactAddress_1(plForm.getContact2Address());
    		pledge.setContactTelephone_1(plForm.getContact2Telephone());
    		pledge.setContactFax_1(plForm.getContact2Fax());
    		pledge.setContactEmail_1(plForm.getContact2Email());
    		pledge.setContactAlternativeName_1(plForm.getContactAlternate2Name());
    		pledge.setContactAlternativeEmail_1(plForm.getContactAlternate2Email());
    		pledge.setContactAlternativeTelephone_1(plForm.getContactAlternate2Telephone());
    		
    		Set<FundingPledgesSector> fpsl = new HashSet<FundingPledgesSector>();
    		for (Iterator sectorIt = plForm.getPledgeSectors().iterator(); sectorIt.hasNext();) {
       		 	ActivitySector actSector = (ActivitySector) sectorIt.next();
				AmpSector sector=SectorUtil.getAmpSector(actSector.getSectorId());
				sector.setAmpSectorId(actSector.getSectorId());
				FundingPledgesSector fps = new FundingPledgesSector();
				fps.setSector(sector);
				fps.setSectorpercentage(actSector.getSectorPercentage());
				fpsl.add(fps);
				//pledge.getSectorlist().add(fps);
			}
    		pledge.setSectorlist((Set<FundingPledgesSector>) fpsl);
    		
    		Set<FundingPledgesLocation> fpll = new HashSet<FundingPledgesLocation>();
    		Collection<FundingPledgesLocation> fplc = plForm.getSelectedLocs();
    		Iterator<FundingPledgesLocation> itl = fplc.iterator();
    		while (itl.hasNext()) {
    			FundingPledgesLocation fpl = (FundingPledgesLocation) itl.next();
    			fpll.add(fpl);
			}
    		pledge.setLocationlist((Set<FundingPledgesLocation>) fpll);
    		
    		Set<FundingPledgesDetails> fpdl = new HashSet<FundingPledgesDetails>();
    		Collection<FundingPledgesDetails> fpdc = plForm.getFundingPledgesDetails();
    		Iterator<FundingPledgesDetails> itf = fpdc.iterator();
    		while (itf.hasNext()) {
    			FundingPledgesDetails fpd = (FundingPledgesDetails) itf.next();
    			fpdl.add(fpd);
			}
    		pledge.setFundingPledgesDetails((Set<FundingPledgesDetails>) fpdl);
    		
    		PledgeUtil.savePledge(pledge);
    		
    		return mapping.findForward("forward");
		}
}
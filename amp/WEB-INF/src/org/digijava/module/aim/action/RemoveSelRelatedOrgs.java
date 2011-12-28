/*
 * RemoveSelRelatedOrgs.java
 * Created: 24 Feb, 2005 
 */

package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.form.EditActivityForm;

/**
 * This Action Class removes the selected related organizations from the
 * related orgs list based on the value of the form bean variable value 'item'. 
 * If the value of item is 1 the selected orgs is removed from executing
 * agency list
 * If the value of item is 2 the selected orgs is removed from implementing 
 * agency list
 * If the value of item is 3 the selected orgs is removed from contractors
 * list
 */
public class RemoveSelRelatedOrgs extends Action {
	
	private static Logger logger = Logger.getLogger(RemoveSelRelatedOrgs.class);
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) {
		
		EditActivityForm eaForm = (EditActivityForm) form;

		logger.info("In RemoveSelRelatedOrgs");
		logger.info("Item = " + eaForm.getAgencies().getItem());
		
		if (eaForm.getAgencies().getItem() != 0) {
			if (eaForm.getAgencies().getItem() == 1) {
				Long selOrgs[] = eaForm.getAgencies().getSelExAgencies();
				if (selOrgs != null) {
					for (int i = 0;i < selOrgs.length;i ++) {
						AmpOrganisation org = new AmpOrganisation();
						org.setAmpOrgId(selOrgs[i]);
						eaForm.getAgencies().getExecutingAgencies().remove(org);
					}
				}				
			} else if (eaForm.getAgencies().getItem() == 2) {
				Long selOrgs[] = eaForm.getAgencies().getSelImpAgencies();
				if (selOrgs != null) {
					for (int i = 0;i < selOrgs.length;i ++) {
						AmpOrganisation org = new AmpOrganisation();
						org.setAmpOrgId(selOrgs[i]);
						eaForm.getAgencies().getImpAgencies().remove(org);
					}
				}
			} else if (eaForm.getAgencies().getItem() == 3) {
				/*
				Long selOrgs[] = eaForm.getAgencies().getSelContractors();
				if (selOrgs != null) {
					for (int i = 0;i < selOrgs.length;i ++) {
						AmpOrganisation org = new AmpOrganisation();
						org.setAmpOrgId(selOrgs[i]);
						eaForm.getAgencies().getContractors().remove(org);
					}
				}*/				
			} else if (eaForm.getAgencies().getItem() == 4) {
				Long selOrgs[] = eaForm.getAgencies().getSelReportingOrgs();
				if (selOrgs != null) {
					for (int i = 0;i < selOrgs.length;i ++) {
						AmpOrganisation org = new AmpOrganisation();
						org.setAmpOrgId(selOrgs[i]);
						eaForm.getAgencies().getReportingOrgs().remove(org);
					}
				}								
			}
			else
				if (eaForm.getAgencies().getItem() == 5) {
					Long selOrgs[] = eaForm.getAgencies().getSelBenAgencies();
					if (selOrgs != null) {
						for (int i = 0;i < selOrgs.length;i ++) {
							AmpOrganisation org = new AmpOrganisation();
							org.setAmpOrgId(selOrgs[i]);
							eaForm.getAgencies().getBenAgencies().remove(org);
						}
					}
				}
				else
					if (eaForm.getAgencies().getItem() == 6) {
						Long selOrgs[] = eaForm.getAgencies().getSelConAgencies();
						if (selOrgs != null) {
							for (int i = 0;i < selOrgs.length;i ++) {
								AmpOrganisation org = new AmpOrganisation();
								org.setAmpOrgId(selOrgs[i]);
								eaForm.getAgencies().getConAgencies().remove(org);
							}
						}
					}
					else
						if (eaForm.getAgencies().getItem() == 7) {
							Long selOrgs[] = eaForm.getAgencies().getSelRegGroups();
							if (selOrgs != null) {
								for (int i = 0;i < selOrgs.length;i ++) {
									AmpOrganisation org = new AmpOrganisation();
									org.setAmpOrgId(selOrgs[i]);
									eaForm.getAgencies().getRegGroups().remove(org);
							}
						}
					}
						else
							if (eaForm.getAgencies().getItem() == 8) {
								Long selOrgs[] = eaForm.getAgencies().getSelSectGroups();
								if (selOrgs != null) {
									for (int i = 0;i < selOrgs.length;i ++) {
										AmpOrganisation org = new AmpOrganisation();
										org.setAmpOrgId(selOrgs[i]);
										eaForm.getAgencies().getSectGroups().remove(org);
								}
							}
						}		
							else
								if (eaForm.getAgencies().getItem() == 9) {
									Long selOrgs[] = eaForm.getAgencies().getSelRespOrganisations();
									if (selOrgs != null) {
										for (int i = 0;i < selOrgs.length;i ++) {
											AmpOrganisation org = new AmpOrganisation();
											org.setAmpOrgId(selOrgs[i]);
											eaForm.getAgencies().getRespOrganisations().remove(org);
									}
								}
							}		
		}
		return mapping.findForward("forward");
	}
}

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
import org.digijava.module.aim.dbentity.AmpOrgRole;
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
		logger.info("Item = " + eaForm.getItem());
		
		if (eaForm.getItem() != 0) {
			if (eaForm.getItem() == 1) {
				Long selOrgs[] = eaForm.getSelExAgencies();
				if (selOrgs != null) {
					for (int i = 0;i < selOrgs.length;i ++) {
						AmpOrgRole org = new AmpOrgRole();
						org.setAmpOrgRoleId(selOrgs[i]);
						eaForm.getExecutingAgencies().remove(org);
					}
				}				
			} else if (eaForm.getItem() == 2) {
				Long selOrgs[] = eaForm.getSelImpAgencies();
				if (selOrgs != null) {
					for (int i = 0;i < selOrgs.length;i ++) {
						AmpOrganisation org = new AmpOrganisation();
						org.setAmpOrgId(selOrgs[i]);
						eaForm.getImpAgencies().remove(org);
					}
				}
			} else if (eaForm.getItem() == 3) {
				/*
				Long selOrgs[] = eaForm.getSelContractors();
				if (selOrgs != null) {
					for (int i = 0;i < selOrgs.length;i ++) {
						AmpOrganisation org = new AmpOrganisation();
						org.setAmpOrgId(selOrgs[i]);
						eaForm.getContractors().remove(org);
					}
				}*/				
			} else if (eaForm.getItem() == 4) {
				Long selOrgs[] = eaForm.getSelReportingOrgs();
				if (selOrgs != null) {
					for (int i = 0;i < selOrgs.length;i ++) {
						AmpOrganisation org = new AmpOrganisation();
						org.setAmpOrgId(selOrgs[i]);
						eaForm.getReportingOrgs().remove(org);
					}
				}								
			}
			else
				if (eaForm.getItem() == 5) {
					Long selOrgs[] = eaForm.getSelBenAgencies();
					if (selOrgs != null) {
						for (int i = 0;i < selOrgs.length;i ++) {
							AmpOrganisation org = new AmpOrganisation();
							org.setAmpOrgId(selOrgs[i]);
							eaForm.getBenAgencies().remove(org);
						}
					}
				}
				else
					if (eaForm.getItem() == 6) {
						Long selOrgs[] = eaForm.getSelConAgencies();
						if (selOrgs != null) {
							for (int i = 0;i < selOrgs.length;i ++) {
								AmpOrganisation org = new AmpOrganisation();
								org.setAmpOrgId(selOrgs[i]);
								eaForm.getConAgencies().remove(org);
							}
						}
					}
					else
						if (eaForm.getItem() == 7) {
							Long selOrgs[] = eaForm.getSelRegGroups();
							if (selOrgs != null) {
								for (int i = 0;i < selOrgs.length;i ++) {
									AmpOrganisation org = new AmpOrganisation();
									org.setAmpOrgId(selOrgs[i]);
									eaForm.getRegGroups().remove(org);
							}
						}
					}
						else
							if (eaForm.getItem() == 8) {
								Long selOrgs[] = eaForm.getSelSectGroups();
								if (selOrgs != null) {
									for (int i = 0;i < selOrgs.length;i ++) {
										AmpOrganisation org = new AmpOrganisation();
										org.setAmpOrgId(selOrgs[i]);
										eaForm.getSectGroups().remove(org);
								}
							}
						}		
			
		}
		return mapping.findForward("forward");
	}
}

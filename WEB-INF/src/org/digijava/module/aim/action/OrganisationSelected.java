/**
 * @author Priyajith
 * @version 1.0
 */

package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.FundingOrganization;
import org.digijava.module.aim.helper.OrgProjectId;
import org.digijava.module.aim.util.DbUtil;

public class OrganisationSelected extends Action {

	private static Logger logger = Logger.getLogger(OrganisationSelected.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception {
		
		logger.debug("In OrganisationSelected");

		EditActivityForm eaForm = (EditActivityForm) form;

		Long selOrgs[] = eaForm.getSelOrganisations();
		Collection prevSelOrgs = null;

		OrgProjectId prevOrgs[] = null;
		OrgProjectId currOrgs[] = null;
		int item = 0;

		if (eaForm.getStep().equals("1")) {
			prevOrgs = eaForm.getSelectedOrganizations();
		} else if (eaForm.getStep().equals("3")) {
			prevSelOrgs = eaForm.getFundingOrganizations();
		} else if (eaForm.getStep().equals("7")) {
			item = eaForm.getItem();
		} else {
			mapping.findForward("index");
		}

		Collection orgs = new ArrayList();
		if (prevSelOrgs != null) {
			orgs.addAll(prevSelOrgs);
		}
		
		logger.info("Step = " + eaForm.getStep());

		if (eaForm.getStep().equals("1")) {
			if (eaForm.getSelOrganisations() == null ||
					eaForm.getSelOrganisations().length == 0) return mapping.findForward("forward");	
			int index = 0;
			if (prevOrgs != null) {
				currOrgs = new OrgProjectId[prevOrgs.length + selOrgs.length];
				for (int i = 0; i < prevOrgs.length; i++) {
					currOrgs[i] = prevOrgs[i];
				}
				index = prevOrgs.length;
			} else {
				currOrgs = new OrgProjectId[selOrgs.length];
			}

			for (int i = 0; i < selOrgs.length; i++) {
				boolean flag = false;
				if (prevOrgs != null) {
					for (int j = 0; j < prevOrgs.length; j++) {
						if(prevOrgs[j]!=null){
							if (prevOrgs[j].getAmpOrgId().equals(selOrgs[i])) {
								flag = true;
								break;
							}
						}						
					}
				}

				if (!flag) {
					AmpOrganisation org = DbUtil.getOrganisation(selOrgs[i]);
					if (org != null) {
						OrgProjectId opId = new OrgProjectId();
						opId.setAmpOrgId(org.getAmpOrgId());
						opId.setName(org.getName());
						opId.setProjectId(null);
						opId.setOrganisation(org);
						
						currOrgs[index++] = opId;
					}
				}

			}
			eaForm.setSelectedOrganizations(currOrgs);
			eaForm.setNumResults(0);
			eaForm.setOrgPopupReset(true);
			return mapping.findForward("forward");
		} else if (eaForm.getStep().equals("3")) {
			if (eaForm.getSelOrganisations() == null ||
					eaForm.getSelOrganisations().length == 0) return mapping.findForward("step3");				
			for (int i = 0; i < selOrgs.length; i++) {
				boolean flag = false;
				if (prevSelOrgs != null) {
					Iterator itr = prevSelOrgs.iterator();
					while (itr.hasNext()) {
						FundingOrganization org = (FundingOrganization) itr
								.next();
						if (org.getAmpOrgId().equals(selOrgs[i])) {
							flag = true;
							break;
						}
					}
				}

				if (!flag) {
					AmpOrganisation org = DbUtil.getOrganisation(selOrgs[i]);
					if (org != null) {
						FundingOrganization fOrg = new FundingOrganization();
						fOrg.setAmpOrgId(org.getAmpOrgId());
						fOrg.setOrgName(org.getName());
						orgs.add(fOrg);
					}
				}

			}
			eaForm.setFundingOrganizations(orgs);
			return mapping.findForward("step3");

		} else {
			logger.info("item = " + item);
			if (item != 0) {
				Vector temp = new Vector();
				
				for (int i = 0;i < selOrgs.length;i ++) {
					AmpOrganisation org = DbUtil.getOrganisation(selOrgs[i]);
					temp.add(org);
				}
				
				if (item == 1) {
					if (eaForm.getExecutingAgencies() != null) {
						for (int i = 0;i < temp.size();i ++) {
							AmpOrganisation newOrg = (AmpOrganisation) temp.get(i);
							if (newOrg != null) {
								boolean flag = false;
								Iterator itr = eaForm.getExecutingAgencies().iterator();
								while (itr.hasNext()) {
									AmpOrganisation org = (AmpOrganisation) itr.next();
									if (org.getAmpOrgId().equals(newOrg.getAmpOrgId())) {
										flag = true;
										break;
									}
								}
								if (!flag) {
									eaForm.getExecutingAgencies().add(newOrg);
								}
							}
						}
					} else {
						Collection col = new ArrayList();
						col.addAll(temp);
						eaForm.setExecutingAgencies(col);
					}
				} else if (item == 2) {
					if (eaForm.getImpAgencies() != null) {
						for (int i = 0;i < temp.size();i ++) {
							AmpOrganisation newOrg = (AmpOrganisation) temp.get(i);
							if (newOrg != null) {
								boolean flag = false;
								Iterator itr = eaForm.getImpAgencies().iterator();
								while (itr.hasNext()) {
									AmpOrganisation org = (AmpOrganisation) itr.next();
									if (org.getAmpOrgId().equals(newOrg.getAmpOrgId())) {
										flag = true;
										break;
									}
								}
								if (!flag) {
									eaForm.getImpAgencies().add(newOrg);
								}
							}
						}
					} else {
						Collection col = new ArrayList();
						col.addAll(temp);
						eaForm.setImpAgencies(col);						
					}
				} else if (item == 3) {
				} else if (item == 4) {
					if (eaForm.getReportingOrgs() != null) {
						for (int i = 0;i < temp.size();i ++) {
							AmpOrganisation newOrg = (AmpOrganisation) temp.get(i);
							if (newOrg != null) {
								boolean flag = false;
								Iterator itr = eaForm.getReportingOrgs().iterator();
								while (itr.hasNext()) {
									AmpOrganisation org = (AmpOrganisation) itr.next();
									if (org.getAmpOrgId().equals(newOrg.getAmpOrgId())) {
										flag = true;
										break;
									}
								}
								if (!flag) {
									eaForm.getReportingOrgs().add(newOrg);
								}
							}
						}
					} else {
						Collection col = new ArrayList();
						col.addAll(temp);
						eaForm.setReportingOrgs(col);						
					}
				}
				else
					if (item == 5) { //Step 7 - Beneficiary Agency
						if (eaForm.getBenAgencies()!= null) {
							for (int i = 0;i < temp.size();i ++) {
								AmpOrganisation newOrg = (AmpOrganisation) temp.get(i);
								if (newOrg != null) {
									boolean flag = false;
									Iterator itr = eaForm.getBenAgencies().iterator();
									while (itr.hasNext()) {
										AmpOrganisation org = (AmpOrganisation) itr.next();
										if (org.getAmpOrgId().equals(newOrg.getAmpOrgId())) {
											flag = true;
											break;
										}
									}
									if (!flag) {
										eaForm.getBenAgencies().add(newOrg);
									}
								}
							}
						} else {
							Collection col = new ArrayList();
							col.addAll(temp);
							eaForm.setBenAgencies(col);						
						}
					}
					else
						if (item == 6) { //Step 7 - Contracting Agency
							if (eaForm.getConAgencies()!= null) {
								for (int i = 0;i < temp.size();i ++) {
									AmpOrganisation newOrg = (AmpOrganisation) temp.get(i);
									if (newOrg != null) {
										boolean flag = false;
										Iterator itr = eaForm.getConAgencies().iterator();
										while (itr.hasNext()) {
											AmpOrganisation org = (AmpOrganisation) itr.next();
											if (org.getAmpOrgId().equals(newOrg.getAmpOrgId())) {
												flag = true;
												break;
											}
										}
										if (!flag) {
											eaForm.getConAgencies().add(newOrg);
										}
									}
								}
							} else {
								Collection col = new ArrayList();
								col.addAll(temp);
								eaForm.setConAgencies(col);						
							}
						}
						else
							if (item == 7) {
								if (eaForm.getRegGroups()!= null) {
									for (int i = 0;i < temp.size();i ++) {
										AmpOrganisation newOrg = (AmpOrganisation) temp.get(i);
										if (newOrg != null) {
											boolean flag = false;
											Iterator itr = eaForm.getRegGroups().iterator();
											while (itr.hasNext()) {
												AmpOrganisation org = (AmpOrganisation) itr.next();
												if (org.getAmpOrgId().equals(newOrg.getAmpOrgId())) {
													flag = true;
													break;
												}
											}
											if (!flag) {
												eaForm.getRegGroups().add(newOrg);
											}
										}
									}
								} else {
									Collection col = new ArrayList();
									col.addAll(temp);
									eaForm.setRegGroups(col);						
								}
							}
				
							else
								if (item == 8) {
									if (eaForm.getSectGroups()!= null) {
										for (int i = 0;i < temp.size();i ++) {
											AmpOrganisation newOrg = (AmpOrganisation) temp.get(i);
											if (newOrg != null) {
												boolean flag = false;
												Iterator itr = eaForm.getSectGroups().iterator();
												while (itr.hasNext()) {
													AmpOrganisation org = (AmpOrganisation) itr.next();
													if (org.getAmpOrgId().equals(newOrg.getAmpOrgId())) {
														flag = true;
														break;
													}
												}
												if (!flag) {
													eaForm.getSectGroups().add(newOrg);
												}
											}
										}
									} else {
										Collection col = new ArrayList();
										col.addAll(temp);
										eaForm.setSectGroups(col);						
									}
								}
				
			}
			return mapping.findForward("step6");
		}
	}
}

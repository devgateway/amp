package org.digijava.module.aim.action ;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.digijava.module.aim.dbentity.*;
import org.digijava.kernel.dbentity.Country;
import java.util.Collection;
import java.util.Iterator;

import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.form.AddOrgForm;
import javax.servlet.http.*;


public class EditOrganisation extends Action {

		  private static Logger logger = Logger.getLogger(EditOrganisation.class);

		  public ActionForward execute(ActionMapping mapping,
								ActionForm form,
								HttpServletRequest request,
								HttpServletResponse response) throws java.lang.Exception {

		  			 HttpSession session = request.getSession();
					 if (session.getAttribute("ampAdmin") == null) {
						return mapping.findForward("index");
					 } 
					 else {
							String str = (String)session.getAttribute("ampAdmin");
							if (str.equals("no")) {
								return mapping.findForward("index");
							}
					 }
					 
					 logger.debug("In edit organisation action");

					 AddOrgForm editForm = (AddOrgForm) form;
					 
					 String action = request.getParameter("actionFlag");
					 editForm.setActionFlag(action);
					 
					 Collection fiscalColl = DbUtil.getAllFisCalenders();
					 Collection sectorSchemeColl = DbUtil.getAllSectorSchemes();
					 //Collection country = DbUtil.getAllCountries();
					 Collection level = DbUtil.getAllLevels();
					 Collection region = null;
					 //if (!editForm.getCountryId().equals("") || editForm.getCountryId().trim().length() != 0)
					 	Country cntry = DbUtil.getCountryByName("Ethiopia"); 
					 	region = DbUtil.getAllRegionsUnderCountry(cntry.getIso());
					 Collection orgType = DbUtil.getAllOrgTypes();
					 Collection orgGroup = null;
					 editForm.setFiscalCal(fiscalColl);
					 editForm.setSectorScheme(sectorSchemeColl);
					 //editForm.setCountry(country);
					 editForm.setLevel(level);
					 editForm.setRegion(region);
					 editForm.setOrgType(orgType);
					 editForm.setOrgGroup(orgGroup);
					 
					 String otype = editForm.getOrgTypeFlag();
					 if (!editForm.getSaveFlag().equals("yes")) {
					 	if (otype != null && otype.trim().length() != 0) {
					 		if ("regional".equals(otype))
					 			editForm.setRegionFlag("show");	// Setting style property for showing region drop-down
					 		else
					 			editForm.setRegionFlag("hide");	// Setting style property for hiding region drop-down
					 		if ("multilateral".equals(otype)) {
					 			Long id = null;
						 		Iterator itr = level.iterator();
						 		while (itr.hasNext()) {
						 			AmpLevel al = (AmpLevel) itr.next();
						 			if (al.getName().toLowerCase().equals(otype)) {
						 				id = al.getAmpLevelId();
						 				break;
						 			}
						 		}
						 		orgGroup = DbUtil.getAllOrgGroupByType(id);
						 		editForm.setOrgGroup(orgGroup);
						 		if ("edit".equals(action))
						 			editForm.setFlag("delete");
					 		}
						 		return mapping.findForward("forward");
					 	}
					 /*
					 	String regionFlag = editForm.getRegionFlag();
					 	if (regionFlag != null && "changed".equals(regionFlag)) {
					 		editForm.setRegionFlag("");
					 		if ("edit".equals(action))
					 			editForm.setFlag("delete");
					 		return mapping.findForward("forward");
					 	}
					 }*/
					 }
					 					 
					 if ("create".equals(action) || "edit".equals(action)) {
					 	AmpOrganisation ampOrg = new AmpOrganisation();
					 	
					 	if ("edit".equals(action)) {
					 		editForm.setAmpOrgId(new Long(Integer.parseInt(request.getParameter("ampOrgId"))));
							ampOrg = DbUtil.getOrganisation(editForm.getAmpOrgId());
							
							if (ampOrg == null) {
								if (session.getAttribute("ampOrg") != null) {
									session.removeAttribute("ampOrg");
								}
								return mapping.findForward("added");
							}
							editForm.setFlag("delete");
					 	}
					 	
					 	if (editForm.getName() == null) {
					 		if ("create".equals(action)) {
					 			logger.debug("Inside IF [CREATE]");
					 			/*
					 			Iterator itr = level.iterator();
								 while (itr.hasNext()) {
								 	AmpLevel al = (AmpLevel) itr.next();
								 	if (al.getName().toLowerCase().equals("regional")) {
								 		editForm.setLevelId(al.getAmpLevelId());
								 		editForm.setLevelFlag("regional");
								 		break;
								 	}
								 }*/
					 			editForm.setRegionFlag("hide");	// Setting style property for hiding region drop-down
					 			return mapping.findForward("forward");
					 		}
							
							 	if (ampOrg.getName() != null) {
							 		editForm.setName(ampOrg.getName());
							 	}
							 	else
							 		editForm.setName("");
							 	if (ampOrg.getAcronym() != null) {
							 		editForm.setAcronym(ampOrg.getAcronym());
							 	}
							 	else
							 		editForm.setAcronym("");
							 	if (ampOrg.getDescription() != null) {
							 		editForm.setDescription(ampOrg.getDescription());
							 	}
								else
									editForm.setDescription("");
							 	if (ampOrg.getDacOrgCode() != null) {
							 		 editForm.setDacOrgCode(ampOrg.getDacOrgCode());
							 	}
							 	else
							 		editForm.setDacOrgCode("");
								if (ampOrg.getOrgCode() != null) {
									editForm.setOrgCode(ampOrg.getOrgCode());
								}
								else
									editForm.setOrgCode("");
								if (ampOrg.getOrgIsoCode() != null) {
									editForm.setOrgIsoCode(ampOrg.getOrgIsoCode());
								}
								else
									editForm.setOrgIsoCode("");
								
								if (ampOrg.getContactPersonName() != null)
									editForm.setContactPersonName(ampOrg.getContactPersonName());
								else
									editForm.setContactPersonName("");
								if (ampOrg.getContactPersonTitle() != null)
									editForm.setContactPersonTitle(ampOrg.getContactPersonTitle());
								else
									editForm.setContactPersonTitle("");
								if (ampOrg.getAddress() != null)
									editForm.setAddress(ampOrg.getAddress());
								else
									editForm.setAddress("");
								if (ampOrg.getEmail() != null)
									editForm.setEmail(ampOrg.getEmail());
								else
									editForm.setEmail("");
								if (ampOrg.getPhone() != null)
									editForm.setPhone(ampOrg.getPhone());
								else
									editForm.setPhone("");
								if (ampOrg.getFax() != null)
									editForm.setFax(ampOrg.getFax());
								else
									editForm.setFax("");
								if (ampOrg.getOrgUrl() != null)
									editForm.setOrgUrl(ampOrg.getOrgUrl());
								else
									editForm.setOrgUrl("");
								
								if (ampOrg.getAmpFiscalCalId() != null) {
									editForm.setFiscalCalId(ampOrg.getAmpFiscalCalId().getAmpFiscalCalId());
								}
								else
									editForm.setFiscalCalId(new Long(-1));
								if (ampOrg.getAmpSecSchemeId() != null) {
									editForm.setAmpSecSchemeId(ampOrg.getAmpSecSchemeId().getAmpSecSchemeId());
								}
								else
									editForm.setAmpSecSchemeId(new Long(-1));
								editForm.setRegionFlag("hide");	// Setting style property for hiding region drop-down
								if (ampOrg.getOrgTypeId() != null) {
									editForm.setAmpOrgTypeId(ampOrg.getOrgTypeId().getAmpOrgTypeId());
									/*
									if (ampOrg.getLevelId() != null)
										orgGroup = DbUtil.getAllOrgGroupByType(ampOrg.getLevelId().getAmpLevelId());
									else
										orgGroup = DbUtil.getAllOrgGroupByType(null);
								 	editForm.setOrgGroup(orgGroup); */
								 	if (ampOrg.getOrgTypeId().getOrgType().equals("Ethiopian Government")
								 			|| ampOrg.getOrgTypeId().getOrgType().equals("National NGO"))
								 		editForm.setOrgTypeFlag("national");
								 	else if (ampOrg.getOrgTypeId().getOrgType().equals("Regional Government")) {
								 		editForm.setOrgTypeFlag("regional");
								 		editForm.setRegionFlag("show");	// Setting style property for showing region drop-down
								 	}
								 	else if (ampOrg.getOrgTypeId().getOrgType().equals("Multilateral")) {
								 		editForm.setOrgTypeFlag("multilateral");
								 		Long id = null;
								 		Iterator itr = level.iterator();
								 		while (itr.hasNext()) {
								 			AmpLevel al = (AmpLevel) itr.next();
								 			if (al.getName().toLowerCase().equals(otype)) {
								 				id = al.getAmpLevelId();
								 				break;
								 			}
								 		}
								 		orgGroup = DbUtil.getAllOrgGroupByType(id);
								 		editForm.setOrgGroup(orgGroup);
								 	}
								 		else
								 		   	editForm.setOrgTypeFlag("others");
								}
								else
									editForm.setAmpOrgTypeId(new Long(-1));
								if (ampOrg.getOrgGrpId() != null) {
									editForm.setAmpOrgGrpId(ampOrg.getOrgGrpId().getAmpOrgGrpId());
								}
								else
									editForm.setAmpOrgGrpId(new Long(-1));
								/*
								if (ampOrg.getCountryId() != null) {
									editForm.setCountryId(ampOrg.getCountryId().getIso());
								}
								else
									editForm.setCountryId("et"); */
								if (ampOrg.getRegionId() != null) {
									editForm.setRegionId(ampOrg.getRegionId().getAmpRegionId());
								}
								else
									editForm.setRegionId(new Long(-1));
								/*
								if (ampOrg.getLevelId() != null) {
									editForm.setLevelId(ampOrg.getLevelId().getAmpLevelId());
									editForm.setLevelFlag("others");
									Iterator itr = level.iterator();
									 while (itr.hasNext()) {
									 	AmpLevel al = (AmpLevel) itr.next();
									 	if (al.getAmpLevelId().equals(editForm.getLevelId())) {
									 		if (al.getName().toLowerCase().equals("regional")) {
									 			editForm.setLevelFlag("regional");
									 			break;
									 		}
									 	}
									 }
								}
								else
									editForm.setLevelId(new Long(-1)); */
								
								return mapping.findForward("forward");
						 }
						 else {
						 	if (session.getAttribute("ampOrg") != null) {
								session.removeAttribute("ampOrg");
							}
							
							if (editForm.getName() != null || !editForm.getName().trim().equals(""))
								ampOrg.setName(editForm.getName());
							else
								ampOrg.setName(null);
							if (editForm.getAcronym() != null || !editForm.getAcronym().trim().equals(""))
								ampOrg.setAcronym(editForm.getAcronym());
							else
								ampOrg.setAcronym(null);
							if (editForm.getDacOrgCode() != null || !editForm.getDacOrgCode().trim().equals(""))
								ampOrg.setDacOrgCode(editForm.getDacOrgCode());
							else
								ampOrg.setDacOrgCode(null);
							if (editForm.getOrgCode() != null || !editForm.getOrgCode().trim().equals("")) {
								Collection org = DbUtil.getOrgByCode(action,editForm.getOrgCode(),editForm.getAmpOrgId());
								if (org.isEmpty())   // To check for duplicate org-code
									ampOrg.setOrgCode(editForm.getOrgCode());
								else {
									editForm.setFlag("orgCodeExist");
									return mapping.findForward("forward");
								}
							}
							else
								ampOrg.setOrgCode(null);
							if (editForm.getOrgIsoCode() != null || !editForm.getOrgIsoCode().trim().equals(""))
								ampOrg.setOrgIsoCode(editForm.getOrgIsoCode());
							else
								ampOrg.setOrgIsoCode(null);
							
							if (editForm.getDescription().trim().equals("") || editForm.getDescription() == null) {
								ampOrg.setDescription(" ");
							}
							else
								ampOrg.setDescription(editForm.getDescription());
							
							if (editForm.getContactPersonName() != null || !editForm.getContactPersonName().trim().equals(""))
								ampOrg.setContactPersonName(editForm.getContactPersonName());
							else
								ampOrg.setContactPersonName(null);
							if (editForm.getContactPersonTitle() != null || !editForm.getContactPersonTitle().trim().equals(""))
								ampOrg.setContactPersonTitle(editForm.getContactPersonTitle());
							else
								ampOrg.setContactPersonTitle(null);
							if (editForm.getAddress() != null || !editForm.getAddress().trim().equals(""))
								ampOrg.setAddress(editForm.getAddress());
							else
								ampOrg.setAddress(null);
							if (editForm.getEmail() != null || !editForm.getEmail().trim().equals(""))
								ampOrg.setEmail(editForm.getEmail());
							else
								ampOrg.setEmail(null);
							if (editForm.getPhone() != null || !editForm.getPhone().trim().equals(""))
								ampOrg.setPhone(editForm.getPhone());
							else
								ampOrg.setPhone(null);
							if (editForm.getFax() != null || !editForm.getFax().trim().equals(""))
								ampOrg.setFax(editForm.getFax());
							else
								ampOrg.setFax(null);
							if (editForm.getOrgUrl() != null || !editForm.getOrgUrl().trim().equals(""))
								ampOrg.setOrgUrl(editForm.getOrgUrl());
							else
								ampOrg.setOrgUrl(null);
							
							if (!editForm.getFiscalCalId().equals(new Long(-1))) {
								AmpFiscalCalendar fiscal =  DbUtil.getAmpFiscalCalendar(editForm.getFiscalCalId());
								if (fiscal != null) {
									ampOrg.setAmpFiscalCalId(fiscal);
								}
							}
							else
								ampOrg.setAmpFiscalCalId(null);
							
							if (!editForm.getAmpSecSchemeId().equals(new Long(-1))) {
								AmpSectorScheme sector =  DbUtil.getAmpSectorScheme(editForm.getAmpSecSchemeId());
								if (sector != null) {
									ampOrg.setAmpSecSchemeId(sector);
								}
							}
							else
								ampOrg.setAmpSecSchemeId(null);
							
							if (!editForm.getAmpOrgTypeId().equals(new Long(-1))) {
								AmpOrgType ampOrgType = DbUtil.getAmpOrgType(editForm.getAmpOrgTypeId());
								if (ampOrgType != null)
									ampOrg.setOrgTypeId(ampOrgType);
							}
							else
								ampOrg.setOrgTypeId(null);
							
							if (editForm.getAmpOrgGrpId().equals(new Long(-1))) {
								ampOrg.setOrgGrpId(null);
							}
							else {
								AmpOrgGroup ampOgp = DbUtil.getAmpOrgGroup(editForm.getAmpOrgGrpId());
								if (ampOgp != null)
									ampOrg.setOrgGrpId(ampOgp);
							}
							
							String oflag = editForm.getOrgTypeFlag();
							if (oflag.equals("national") || oflag.equals("regional")) {
								//Country cntry = DbUtil.getDgCountry(editForm.getCountryId());
								if (cntry != null)
									ampOrg.setCountryId(cntry);
							}	
							else
								ampOrg.setCountryId(null);
							if (editForm.getRegionId().equals(new Long(-1)) || !oflag.equals("regional")) {
								ampOrg.setRegionId(null);
							}	
							else {
								AmpRegion reg = DbUtil.getAmpRegion(editForm.getRegionId());
								if (reg != null)
									ampOrg.setRegionId(reg);
							}
							
							if (editForm.getAmpOrgTypeId().equals(new Long(-1)) || !oflag.equals("national")
									|| !oflag.equals("regional")) {
								ampOrg.setLevelId(null);	
							}	
							else {
								AmpLevel lvl = null;
								AmpLevel al = new AmpLevel();
								Iterator itr = level.iterator();
								while (itr.hasNext()) {
									al = (AmpLevel) itr.next();
									if (al.getName().toLowerCase().equals(editForm.getOrgTypeFlag()))
										lvl = DbUtil.getAmpLevel(al.getAmpLevelId());
								}
								if (lvl != null)
									ampOrg.setLevelId(lvl);
							}
							
							if ("create".equals(action))
								DbUtil.add(ampOrg);
							else
								DbUtil.update(ampOrg);							
							logger.debug("Organisation added");
							return mapping.findForward("added");
						 }
					    } else if ("delete".equals(action)){

					    	Collection activities = DbUtil.getAllActivities();
					    	Iterator itr1 = activities.iterator();
					    	boolean flag = false;
					    	
					    	while (itr1.hasNext()) {
					    		AmpActivity testActivity = new AmpActivity();
					    		testActivity = (AmpActivity)itr1.next();
					    		
					    		//Collection testOrgrole = testActivity.getOrgrole();
					    		Collection testOrgrole = ActivityUtil.getOrgRole(testActivity.getAmpActivityId());
					    		Iterator itr2 = testOrgrole.iterator();
					    						    		
					    		while (itr2.hasNext()) {
					    			AmpOrgRole test = new AmpOrgRole();
					    			test = (AmpOrgRole)itr2.next();
					    			if (test.getOrganisation().getAmpOrgId().equals(editForm.getAmpOrgId())) {
					    				flag = true;
						    			break;
					    			}	
					    		}
					    	}
					    	if (flag) {
					    		editForm.setFlag("orgReferences");
								editForm.setActionFlag("edit");				
					    		return mapping.findForward("forward");
							} else {
								if (session.getAttribute("ampOrg") != null) {
									session.removeAttribute("ampOrg");
								}
						    	
						    	AmpOrganisation org = DbUtil.getOrganisation(editForm.getAmpOrgId());
								DbUtil.delete(org);
								logger.debug("Organisation deleted");
								
						    	return mapping.findForward("added");
							}
					    }
					 return mapping.findForward("index");
				}
}

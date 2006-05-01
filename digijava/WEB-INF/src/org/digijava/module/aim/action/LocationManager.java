/*
 * LocationManager.java
 * @author Akash Sharma
 * Created on 8/02/2005
 */

package org.digijava.module.aim.action;

import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.form.AddLocationForm;
import org.digijava.module.aim.util.DbUtil;

public class LocationManager extends Action {

		  public ActionForward execute(ActionMapping mapping,
								ActionForm form,
								javax.servlet.http.HttpServletRequest request,
								javax.servlet.http.HttpServletResponse
								response) throws java.lang.Exception {
		  	
		  			 HttpSession session = request.getSession();
		  			 if (session.getAttribute("ampAdmin") == null) {
		  			    return mapping.findForward("index");
		  		     } else {
		  			 String str = (String)session.getAttribute("ampAdmin");
		  			 if (str.equals("no")) {
		  			 	return mapping.findForward("index");
		  			 	}
		  			 }

					 AddLocationForm addForm = (AddLocationForm) form;
					 String level = request.getParameter("level");
					 boolean edFlag = true;
					 boolean edLevelFlag = false;
					 String flag = addForm.getEdFlag();
					 
					 if ( (flag != null) && flag.equals("yes")) {
		  			 	edFlag = false;
		  			 	addForm.setEdFlag(null);
		  			 	if (addForm.getEdAction().equals("delete")) {
						 	addForm.setLevel(addForm.getEdLevel());
						 	edLevelFlag = true;
						 }
		  			 	addForm.setEdAction(null);
					 	addForm.setEdLevel(null);
		  			 }
					 
		  			 if (edFlag) {
		  			 	if (request.getParameter("start") != null
		  			 			&& request.getParameter("start").equals("false")) {
		  			 		addForm.setStart(false);
		  			 		}
		  			 	else {
		  			 		addForm.setStart(true);
		  			 		addForm.reset(mapping, request);
		  			 		}
		  			 }
					 
					 if (level == null || level.trim().length() == 0) {
					 	if (addForm.getLevel() == null || addForm.getLevel().trim().length() == 0) {
					 		addForm.setLevel("country");
					 		System.out.println("addLevel(Inside IF) : " + addForm.getLevel());
					 	}
					 }
					 else {
					 	addForm.setLevel(level);
					 	System.out.println("addLevel(Inside ELSE) : " + addForm.getLevel());
					 }
					 
					 if (edFlag || edLevelFlag) {
					 	if (addForm.getLevel().equals("country") || addForm.getLevel().equals("region")) {
					 		addForm.setRegionId(new Long(-1));
					 		addForm.setZoneId(new Long(-1));
					 		addForm.setWoredaId(new Long(-1));
					 		addForm.setImpLevelValue(new Integer(1));
					 		addForm.setRegion(null);
					 		addForm.setZone(null);
					 		addForm.setWoreda(null);
					 		}
					 	if (addForm.getLevel().equals("zone")) {
					 		addForm.setZoneId(new Long(-1));
					 		addForm.setWoredaId(new Long(-1));
					 		addForm.setZone(null);
					 		addForm.setWoreda(null);
					 		}
					 	if (addForm.getLevel().equals("woreda")) {
					 		addForm.setWoredaId(new Long(-1));
					 		addForm.setWoreda(null);
					 		}
					 }
					 
					 System.out.println("RegionId :    " + addForm.getRegionId());
					 System.out.println("ZoneId   :    " + addForm.getZoneId());
					 System.out.println("WoredaId :    " + addForm.getWoredaId());
	 
					 if (addForm.getLevel().equals("country")) {
					 			addForm.setImpLevelValue(new Integer(1));
					 			System.out.println("Inside Country[ImpLevelValue] : " + addForm.getImpLevelValue());
								addForm.setCountry(DbUtil.getAllCountries());
					 } else if (addForm.getLevel().equals("region")) {
								if (addForm.getCountry() == null) {
										  return mapping.findForward("index");
								}
								if (addForm.getCountryId().equals("") || addForm.getCountryId().length() == 0) {
									addForm.setImpLevelValue(new Integer(1));
								}
								else {
									addForm.setImpLevelValue(new Integer(2));
									System.out.println("Inside Region[ImpLevelValue] : " + addForm.getImpLevelValue());
									addForm.setCountryId(addForm.getCountryId());
									addForm.setRegion(DbUtil.getAllRegionsUnderCountry(addForm.getCountryId()));
								}
					 } else if (addForm.getLevel().equals("zone")) {
								if (addForm.getCountry() == null || addForm.getRegion() == null) {
										  return mapping.findForward("index");
								}
								if (!edFlag)
									addForm.setRegion(DbUtil.getAllRegionsUnderCountry(addForm.getCountryId()));
								if (addForm.getRegionId().intValue() == -1) {
									addForm.setImpLevelValue(new Integer(2));
								}
								else {
									addForm.setImpLevelValue(new Integer(3));
									System.out.println("Inside Zone[ImpLevelValue] : " + addForm.getImpLevelValue());
									addForm.setZone(DbUtil.getAllZonesUnderRegion(addForm.getRegionId()));
									
									if (addForm.getZone().isEmpty()) {
										// Checking whether this region is currently being referenced by some activity
										// if yes then don't show the delete link against this region by setting regionFlag='no' 
										AmpLocation ampLoc = DbUtil.getAmpLocation(new Long(-1),addForm.getRegionId(),addForm.getZoneId(),addForm.getWoredaId());
								   		if (ampLoc !=null) {
								   			addForm.setRegionFlag("no");
								   		} else
								   			addForm.setRegionFlag("yes");
									}
								}
					 } else if (addForm.getLevel().equals("woreda") || addForm.getLevel().equals("nextworeda")) {
								if (addForm.getCountry() == null || addForm.getRegion() == null
													 || addForm.getZone() == null)
										  return mapping.findForward("index");
								if (!edFlag) {
									addForm.setRegion(DbUtil.getAllRegionsUnderCountry(addForm.getCountryId()));
									addForm.setZone(DbUtil.getAllZonesUnderRegion(addForm.getRegionId()));
								}
								if (addForm.getZoneId().intValue() == -1) {
									addForm.setImpLevelValue(new Integer(3));
								}
								else {
									addForm.setImpLevelValue(new Integer(4));
									System.out.println("Inside Woreda[ImpLevelValue] : " + addForm.getImpLevelValue());
									addForm.setWoreda(DbUtil.getAllWoredasUnderZone(addForm.getZoneId()));
									
									// Checking whether this zone is currently being referenced by some activity
									// if yes then don't show the delete link against this zone by setting zoneFlag='no'
									AmpLocation ampLoc = DbUtil.getAmpLocation(new Long(-1),addForm.getRegionId(),addForm.getZoneId(),addForm.getWoredaId());
									if (addForm.getWoreda().isEmpty()) {
										if (ampLoc != null) {
								   			addForm.setZoneFlag("no");
								   		} else
								   			addForm.setZoneFlag("yes");
								   	}
									else {
										System.out.println("woreda : notempty");
										if (addForm.getLevel().equals("nextworeda")) {
											if (ampLoc != null)
												addForm.setWoredaFlag("no");
											else
												addForm.setWoredaFlag("yes");
								   		}
									}
										
								}
					 } else {
								return mapping.findForward("index");
					 }
					 
					 return mapping.findForward("forward");
			}
}



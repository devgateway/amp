/*
 * LocationManager.java
 * @author Akash Sharma
 * Created on 8/02/2005
 */

package org.digijava.module.aim.action;

import java.util.Collection;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.AddLocationForm;
import org.digijava.module.aim.helper.CountryBean;
import org.digijava.module.aim.util.LocationUtil;

public class LocationManager extends Action {

	private static Logger logger = Logger.getLogger(LocationManager.class);

		  public ActionForward execute(ActionMapping mapping, ActionForm form,
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
						 	addForm.setLevel( this.getStringLevelName(addForm.getCategoryLevel()) );
						 	edLevelFlag = true;
						}
		  			 	addForm.setEdAction(null);
					 	addForm.setCategoryLevel(null);
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
					 	if (addForm.getLevel() == null || addForm.getLevel().trim().length() == 0)
					 		addForm.setLevel("country");
					 }
					 else
					 	addForm.setLevel(level);

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

					 if (addForm.getLevel().equals("country")) {
					 			addForm.setImpLevelValue(new Integer(1));
                                Collection<CountryBean> countries = org.digijava.module.aim.util.DbUtil.getTranlatedCountries(request);
					 			addForm.setCountry(countries);
                                                              
                               
					 } else if (addForm.getLevel().equals("region")) {
								if (addForm.getCountry() == null) {
									return mapping.findForward("index");
								}
								if (addForm.getCountryId()!=null && (addForm.getCountryId().equals("") || addForm.getCountryId().length() == 0)) {
									addForm.setImpLevelValue(new Integer(1));
								}
								else {
									addForm.setImpLevelValue(new Integer(2));
									addForm.setCountryId(addForm.getCountryId());
									addForm.setRegion(LocationUtil.getAllRegionsUnderCountry(addForm.getCountryId()));
								}
					 } else if (addForm.getLevel().equals("zone")) {
								if (addForm.getCountry() == null || addForm.getRegion() == null) {
										  return mapping.findForward("index");
								}
								if (!edFlag)
									addForm.setRegion(LocationUtil.getAllRegionsUnderCountry(addForm.getCountryId()));
								if (addForm.getRegionId().intValue() == -1) {
									addForm.setImpLevelValue(new Integer(2));
								}
								else {
									addForm.setImpLevelValue(new Integer(3));
									addForm.setZone(LocationUtil.getAllZonesUnderRegion(addForm.getRegionId()));

									//if (addForm.getZone().isEmpty()) {
										// Checking whether this region is currently being referenced by some activity
										// if yes then 'delete' link is not shown against this region by setting regionFlag='no'
                                                                        //   AmpLocation ampLoc = LocationUtil.getAmpLocation(new Long(-1),addForm.getRegionId(),addForm.getZoneId(),addForm.getWoredaId());
                                                                           boolean isAssignedToActivity=LocationUtil.isAssignedToActivity(new Long(-1),addForm.getRegionId(),addForm.getZoneId(),addForm.getWoredaId());
								   		if (isAssignedToActivity) {
								   			addForm.setRegionFlag("no");
								   		} else
								   			addForm.setRegionFlag("yes");
									//}
								}
					 } else if (addForm.getLevel().equals("woreda") || addForm.getLevel().equals("nextworeda")) {
								if (addForm.getCountry() == null || addForm.getRegion() == null
													 || addForm.getZone() == null)
										  return mapping.findForward("index");
								if (!edFlag) {
									addForm.setRegion(LocationUtil.getAllRegionsUnderCountry(addForm.getCountryId()));
									addForm.setZone(LocationUtil.getAllZonesUnderRegion(addForm.getRegionId()));
								}
								if (addForm.getZoneId().intValue() == -1) {
									addForm.setImpLevelValue(new Integer(3));
								}
								else {
									addForm.setImpLevelValue(new Integer(4));
									addForm.setWoreda(LocationUtil.getAllWoredasUnderZone(addForm.getZoneId()));

									// Checking whether this zone is currently being referenced by some activity

									boolean isAssignedToActivity=LocationUtil.isAssignedToActivity(new Long(-1),addForm.getRegionId(),addForm.getZoneId(),addForm.getWoredaId());
									if (!addForm.getWoreda().isEmpty() || isAssignedToActivity) {
							   			addForm.setZoneFlag("no");
							   		} else{
							   			addForm.setZoneFlag("yes");
							   		}
									if (addForm.getLevel().equals("nextworeda")) {
										if (isAssignedToActivity)
											addForm.setWoredaFlag("no");
										else
											addForm.setWoredaFlag("yes");
							   		}

								}
					 } else {
								return mapping.findForward("index");
					 }
                                           addForm.setCountryFlag(LocationUtil.countryHasRef(addForm.getCountryId()));
					 return mapping.findForward("forward");
			}


	private String getStringLevelName(Long level) {
		switch ( (int)level.longValue() ) {
			case 0:
				return "country";
			case 1:
				return "region";
			case 2:
				return "zone";
			case 3:
				return "woreda";
			default:
				return null;
		}
	}
}


package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.form.SelectLocationForm;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.KeyValue;
import org.digijava.module.aim.helper.Location;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

public class SelectSectorAF extends SelectorAction{
	private static Logger logger = Logger.getLogger(SelectSectorAF.class);
	
	public ActionForward selectorEnd(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response) throws Exception {
		
		EditActivityForm 	eaForm 	= (EditActivityForm) getForm(request,"aimEditActivityForm");
		addSector(mapping,request,eaForm);
		
		return null;
	}
	
	private ActionForward addSector(ActionMapping mapping, HttpServletRequest request,
			EditActivityForm eaForm) {
		ServletContext ampContext = null;
		HttpSession session = request.getSession();
		    	ampContext = getServlet().getServletContext();		
		Object searchedsector = session.getAttribute("add");

		if (searchedsector != null && searchedsector.equals("true")) {
			Collection selectedSecto = (Collection) session
					.getAttribute("sectorSelected");
			Collection<ActivitySector> prevSelSectors = eaForm.getSectors()
					.getActivitySectors();

			if (selectedSecto != null) {
				Iterator<ActivitySector> itre = selectedSecto.iterator();
				while (itre.hasNext()) {
					ActivitySector selectedSector = (ActivitySector) itre
							.next();

					boolean addSector = true;
					if (prevSelSectors != null) {
						Iterator<ActivitySector> itr = prevSelSectors
								.iterator();
						while (itr.hasNext()) {
							ActivitySector asec = (ActivitySector) itr
									.next();

							if (asec.getSectorName().equals(selectedSector.getSectorName())) {
								if (selectedSector.getSubsectorLevel1Name() == null) {
									addSector = false;
									break;
								}
								if (asec.getSubsectorLevel1Name() != null) {
									if (asec.getSubsectorLevel1Name().equals(selectedSector.getSubsectorLevel1Name())) {
										if (selectedSector.getSubsectorLevel2Name() == null) {
											addSector = false;
											break;
										}
										if (asec.getSubsectorLevel2Name() != null) {
											if (asec.getSubsectorLevel2Name().equals(selectedSector.getSubsectorLevel2Name())) {
												addSector = false;
												break;
											}
										} else {
											addSector = true;
											break;
										}
									}
								} else {
									addSector = true;
									break;
								}
							}
						}
					}

					if (addSector) {
						// if an activity already has one or more
						// sectors,than after adding new one
						// the percentages must equal blanks and user should
						// fill them
		                if (prevSelSectors != null) {
		                    Iterator iter = prevSelSectors.iterator();
		                    boolean firstSecForConfig = true;
	                        while (iter.hasNext()) {
	                            ActivitySector actSect = (ActivitySector) iter
	                                .next();
	                            if (actSect.getConfigId().equals(selectedSector.getConfigId())) {
	                            	if(actSect.getSectorPercentage() != null && actSect.getSectorPercentage()==100f){
	                            		actSect.setSectorPercentage(0f);
	                            	}	
	                            		
	                                firstSecForConfig = false;
	                                break;
	                            }

	                        }
	                        if (firstSecForConfig) {
		                        selectedSector.setSectorPercentage(100f);
		                    }
		                    prevSelSectors.add(selectedSector);
		                } else {
		                    selectedSector.setSectorPercentage(new Float(
		                        100));
		                    prevSelSectors = new ArrayList<ActivitySector> ();
		                    prevSelSectors.add(selectedSector);
		                }
					}

					eaForm.getSectors().setActivitySectors(prevSelSectors);
				}

			}
			session.removeAttribute("sectorSelected");
			session.removeAttribute("add");
		    session.setAttribute("selectedSectorsForActivity", eaForm.getSectors().getActivitySectors());
		    eaForm.getSectors().setPrimarySectorVisible(FeaturesUtil.isVisibleSectors("Primary", ampContext)?"true":"false");
		    eaForm.getSectors().setSecondarySectorVisible(FeaturesUtil.isVisibleSectors("Secondary", ampContext)?"true":"false");
		    session.setAttribute("Primary Sector", eaForm.getSectors().getPrimarySectorVisible());
		    session.setAttribute("Secondary Sector", eaForm.getSectors().getSecondarySectorVisible());
			return mapping.findForward("addActivityStep2");

		} else {
			ActivitySector selectedSector = (ActivitySector) session
					.getAttribute("sectorSelected");
			Collection<ActivitySector> prevSelSectors = eaForm
			.getSectors().getActivitySectors();

			boolean addSector = true;
			if (prevSelSectors != null) {
				Iterator<ActivitySector> itr = prevSelSectors.iterator();
				while (itr.hasNext()) {
					ActivitySector asec = (ActivitySector) itr.next();
					if (asec.getSectorName().equals(
							selectedSector.getSectorName())) {
						if (selectedSector.getSubsectorLevel1Name() == null) {
							addSector = false;
							break;
						}
						if (asec.getSubsectorLevel1Name() != null) {
							if (asec
									.getSubsectorLevel1Name()
									.equals(
											selectedSector
													.getSubsectorLevel1Name())) {
								if (selectedSector.getSubsectorLevel2Name() == null) {
									addSector = false;
									break;
								}
								if (asec.getSubsectorLevel2Name() != null) {
									if (asec
											.getSubsectorLevel2Name()
											.equals(
													selectedSector
															.getSubsectorLevel2Name())) {
										addSector = false;
										break;
									}
								} else {
									addSector = false;
									break;
								}
							}
						} else {
							addSector = false;
							break;
						}
					}
				}
			}

		    if (addSector) {
		        // if an activity already has one or more sectors,than after
		        // adding new one
		        // the percentages must equal blanks and user should fill
		        // them
		        if (prevSelSectors != null) {
		            Iterator iter = prevSelSectors.iterator();
		            boolean firstSecForConfig = true;
		            while (iter.hasNext()) {
		                ActivitySector actSect = (ActivitySector) iter
		                    .next();
		                if (actSect.getConfigId().equals(selectedSector.getConfigId())) {
		                	if(actSect.getSectorPercentage()==100f){
		                		actSect.setSectorPercentage(0.0f);
		                	}                            	
		                    firstSecForConfig = false;
		                    break;
		                }

		            }
		            if (firstSecForConfig) {
		                selectedSector.setSectorPercentage(100f);
		            }
		            prevSelSectors.add(selectedSector);
		        } else {
		            selectedSector.setSectorPercentage(new Float(
		                100));
		            prevSelSectors = new ArrayList<ActivitySector> ();
		            prevSelSectors.add(selectedSector);
		        }
		    }
			eaForm.getSectors().setActivitySectors(prevSelSectors);
			session.removeAttribute("sectorSelected");
		    session.setAttribute("selectedSectorsForActivity", eaForm.getSectors().getActivitySectors());
		    eaForm.getSectors().setPrimarySectorVisible(FeaturesUtil.isVisibleSectors("Primary", ampContext)?"true":"false");
		    eaForm.getSectors().setSecondarySectorVisible(FeaturesUtil.isVisibleSectors("Secondary", ampContext)?"true":"false");
		    session.setAttribute("Primary Sector", eaForm.getSectors().getPrimarySectorVisible());
		    session.setAttribute("Secondary Sector", eaForm.getSectors().getSecondarySectorVisible());
			return mapping.findForward("addActivityStep2");

		}
	}	
}

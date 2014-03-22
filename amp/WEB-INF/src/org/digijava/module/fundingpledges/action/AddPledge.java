package org.digijava.module.fundingpledges.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;
import org.digijava.module.fundingpledges.dbentity.PledgesEntityHelper;
import org.digijava.module.fundingpledges.form.PledgeForm;

public class AddPledge extends Action {

    public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws java.lang.Exception {
        	
    		PledgeForm plForm = (PledgeForm) form;
    		HttpSession session = request.getSession();
    		
//    		FundingPledges fp2 = PledgesEntityHelper.getPledgesById(3l);
//    		plForm.reset();
//    		plForm.importPledgeData(fp2);
//    		if (System.currentTimeMillis() > 1)
//    			return null;
    		
//    		// Add sectors
//    		if (request.getParameter("addSector") != null) {
//    			request.getSession().getServletContext().removeAttribute("addSector");
//    			return addSector(mapping, session, plForm);
//    		}else if (request.getParameter("remSectors") != null) {
//    			//return removeSector(mapping, request, session, plForm);
//    	    }
//    	    //
//    		
    		//plForm.setPledgeTypeCategory(CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.PLEDGES_TYPES_KEY));
    		
    		//plForm.setPledgeNames(CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.PLEDGES_NAMES_KEY));
    		
    		//plForm.setAssistanceTypeCategory(CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.TYPE_OF_ASSISTENCE_KEY));

    		//plForm.setAidModalityCategory(CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.FINANCING_INSTRUMENT_KEY));
    		
    		String yearToSpecify = TranslatorWorker.translateText("unspecified");
            
            if (plForm.getYear() == null) {     
               plForm.setYear(yearToSpecify);
            }
    		plForm.setYears(new ArrayList<String>());
            long yearFrom = Long.parseLong(FeaturesUtil.getGlobalSettingValue(Constants.GlobalSettings.YEAR_RANGE_START));
            long countYear = Long.parseLong(FeaturesUtil.getGlobalSettingValue(Constants.GlobalSettings.NUMBER_OF_YEARS_IN_RANGE));
            long maxYear = yearFrom + countYear;
            //plForm.getYears().add(yearToSpecify);
            for (long i = yearFrom; i <= maxYear; i++) {
            	plForm.getYears().add(String.valueOf(i));
            }
    		
//            List<AmpOrgGroup> orgGroups = new ArrayList<AmpOrgGroup>(DbUtil.getAllOrgGroups());
//            plForm.setOrgGroups(orgGroups);
            
 	        if (request.getParameter("reset") != null && request.getParameter("reset").equalsIgnoreCase("true")) {
	        	plForm.reset();
	        	request.getSession().removeAttribute("reset");
			} else if (request.getParameter("pledgeId") != null && Long.valueOf(request.getParameter("pledgeId")) > 0){
				FundingPledges fp = PledgesEntityHelper.getPledgesById(Long.valueOf(request.getParameter("pledgeId")));
				plForm.reset();
				plForm.importPledgeData(fp);
	        	request.getSession().removeAttribute("pledgeId");
			}
	        TeamMember teamMember = (TeamMember) session.getAttribute("currentMember");
			if (teamMember.getAppSettings() != null) {
				ApplicationSettings appSettings = teamMember.getAppSettings();
				if (appSettings.getCurrencyId() != null) {
					plForm.setDefaultCurrency(CurrencyUtil.getAmpcurrency(appSettings.getCurrencyId()).getCurrencyCode());
				}
			}
			
			//plForm.setPledgeNames(PledgesEntityHelper.getPledgeNames());
	        request.getSession().setAttribute("pledgeForm", plForm);
	        
	        ActionMessages errors = (ActionMessages)request.getSession().getAttribute("duplicatedTitleError");
	 	 	if(errors!=null){
	 	 		saveErrors(request, errors);
	 	 		request.getSession().removeAttribute("duplicatedTitleError");
	 	 	}
	        
            return mapping.findForward("forward");
            
    }
    
//    private ActionForward addSector(ActionMapping mapping, HttpSession session,
//    		PledgeForm plForm) {
//    	Object searchedsector = session.getAttribute("add");
//
//    	if (searchedsector != null && searchedsector.equals("true")) {
//    		Collection selectedSecto = (Collection) session
//    				.getAttribute("sectorSelected");
//    		Collection<ActivitySector> prevSelSectors = plForm.getPledgeSectors();
//
//    		if (selectedSecto != null) {
//    			Iterator<ActivitySector> itre = selectedSecto.iterator();
//    			while (itre.hasNext()) {
//    				ActivitySector selectedSector = (ActivitySector) itre
//    						.next();
//
//    				boolean addSector = true;
//    				if (prevSelSectors != null) {
//    					Iterator<ActivitySector> itr = prevSelSectors
//    							.iterator();
//    					while (itr.hasNext()) {
//    						ActivitySector asec = (ActivitySector) itr
//    								.next();
//
//    						if (asec.getSectorName().equals(selectedSector.getSectorName())) {
//    							if (selectedSector.getSubsectorLevel1Name() == null) {
//    								addSector = false;
//    								break;
//    							}
//    							if (asec.getSubsectorLevel1Name() != null) {
//    								if (asec.getSubsectorLevel1Name().equals(selectedSector.getSubsectorLevel1Name())) {
//    									if (selectedSector.getSubsectorLevel2Name() == null) {
//    										addSector = false;
//    										break;
//    									}
//    									if (asec.getSubsectorLevel2Name() != null) {
//    										if (asec.getSubsectorLevel2Name().equals(selectedSector.getSubsectorLevel2Name())) {
//    											addSector = false;
//    											break;
//    										}
//    									} else {
//    										addSector = true;
//    										break;
//    									}
//    								}
//    							} else {
//    								addSector = true;
//    								break;
//    							}
//    						}
//    					}
//    				}
//
//    				if (addSector) {
//    					if (prevSelSectors != null) {
//    	                    Iterator iter = prevSelSectors.iterator();
//    	                    boolean firstSecForConfig = true;
//                            while (iter.hasNext()) {
//                                ActivitySector actSect = (ActivitySector) iter
//                                    .next();
//                                if (actSect.getConfigId().equals(selectedSector.getConfigId())) {
//                                	if(actSect.getSectorPercentage() != null && actSect.getSectorPercentage()==100f){
//                                		actSect.setSectorPercentage(0f);
//                                	}	
//                                		
//                                    firstSecForConfig = false;
//                                    break;
//                                }
//
//                            }
//                            if (firstSecForConfig) {
//    	                        selectedSector.setSectorPercentage(100f);
//    	                    }
//    	                    prevSelSectors.add(selectedSector);
//    	                } else {
//    	                    selectedSector.setSectorPercentage(new Float(
//    	                        100));
//    	                    prevSelSectors = new ArrayList<ActivitySector> ();
//    	                    prevSelSectors.add(selectedSector);
//    	                }
//    				}
//
//    				plForm.setPledgeSectors (prevSelSectors);
//    			}
//
//    		}
//    		session.removeAttribute("sectorSelected");
//    		session.removeAttribute("add");
//    		session.removeAttribute("addSector");
//    	    return mapping.findForward("added");
//
//    	} else {
//    		ActivitySector selectedSector = (ActivitySector) session
//    				.getAttribute("sectorSelected");
//    		Collection<ActivitySector> prevSelSectors = plForm.getPledgeSectors();
//
//    		boolean addSector = true;
//    		if (prevSelSectors != null) {
//    			Iterator<ActivitySector> itr = prevSelSectors.iterator();
//    			while (itr.hasNext()) {
//    				ActivitySector asec = (ActivitySector) itr.next();
//    				if (asec.getSectorName().equals(
//    						selectedSector.getSectorName())) {
//    					if (selectedSector.getSubsectorLevel1Name() == null) {
//    						addSector = false;
//    						break;
//    					}
//    					if (asec.getSubsectorLevel1Name() != null) {
//    						if (asec
//    								.getSubsectorLevel1Name()
//    								.equals(
//    										selectedSector
//    												.getSubsectorLevel1Name())) {
//    							if (selectedSector.getSubsectorLevel2Name() == null) {
//    								addSector = false;
//    								break;
//    							}
//    							if (asec.getSubsectorLevel2Name() != null) {
//    								if (asec
//    										.getSubsectorLevel2Name()
//    										.equals(
//    												selectedSector
//    														.getSubsectorLevel2Name())) {
//    									addSector = false;
//    									break;
//    								}
//    							} else {
//    								addSector = false;
//    								break;
//    							}
//    						}
//    					} else {
//    						addSector = false;
//    						break;
//    					}
//    				}
//    			}
//    		}
//
//    	    if (addSector) {
//    	        // if an activity already has one or more sectors,than after
//    	        // adding new one
//    	        // the percentages must equal blanks and user should fill
//    	        // them
//    	        if (prevSelSectors != null) {
//    	            Iterator iter = prevSelSectors.iterator();
//    	            boolean firstSecForConfig = true;
//    	            while (iter.hasNext()) {
//    	                ActivitySector actSect = (ActivitySector) iter
//    	                    .next();
//    	                if (actSect.getConfigId().equals(selectedSector.getConfigId())) {
//    	                	if(actSect.getSectorPercentage()==100f){
//    	                		actSect.setSectorPercentage(0.0f);
//    	                	}                            	
//    	                    firstSecForConfig = false;
//    	                    break;
//    	                }
//
//    	            }
//    	            if (firstSecForConfig) {
//    	                selectedSector.setSectorPercentage(100f);
//    	            }
//    	            prevSelSectors.add(selectedSector);
//    	        } else {
//    	            selectedSector.setSectorPercentage(new Float(
//    	                100));
//    	            prevSelSectors = new ArrayList<ActivitySector> ();
//    	            prevSelSectors.add(selectedSector);
//    	        }
//    	    }
//    		plForm.setPledgeSectors(prevSelSectors);
//    		session.removeAttribute("sectorSelected");
//    		session.removeAttribute("addSector");
//    	    return mapping.findForward("added");
//    	}
//    }
   
}


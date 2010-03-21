package org.digijava.module.fundingpledges.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.fundingpledges.form.PledgeForm;

public class AddPledge extends Action {

    public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws java.lang.Exception {
        	
    		PledgeForm plForm = (PledgeForm) form;
    		HttpSession session = request.getSession();
    		
    		// Add sectors
    		if (request.getParameter("addSector") != null) {
    			return addSector(mapping, session, plForm);
    		}else if (request.getParameter("remSectors") != null) {
    			//return removeSector(mapping, request, session, plForm);
    	    }
    	    //
    		
    		Collection currencies = CurrencyUtil.getAmpCurrency();
    		ArrayList<AmpCurrency> validcurrencies = new ArrayList<AmpCurrency>();
	    	plForm.setValidcurrencies(validcurrencies);
	        if(currencies!=null && currencies.size()>0){
	      	  for (Iterator iter = currencies.iterator(); iter.hasNext();) {
	      			AmpCurrency element = (AmpCurrency) iter.next();
	      			 if( CurrencyUtil.isRate(element.getCurrencyCode())== true)
	      					{
	      				 		plForm.getValidcurrencies().add((CurrencyUtil.getCurrencyByCode(element.getCurrencyCode())));
	      					}
	      			}
	        }
            return mapping.findForward("forward");
            
    }
    
    private ActionForward addSector(ActionMapping mapping, HttpSession session,
    		PledgeForm plForm) {
    	Object searchedsector = session.getAttribute("add");

    	if (searchedsector != null && searchedsector.equals("true")) {
    		Collection selectedSecto = (Collection) session
    				.getAttribute("sectorSelected");
    		Collection<ActivitySector> prevSelSectors = plForm.getPledgeSectors();

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

    				plForm.setPledgeSectors (prevSelSectors);
    			}

    		}
    		session.removeAttribute("sectorSelected");
    		session.removeAttribute("add");
    		return mapping.findForward("forward");

    	} else {
    		ActivitySector selectedSector = (ActivitySector) session
    				.getAttribute("sectorSelected");
    		Collection<ActivitySector> prevSelSectors = plForm.getPledgeSectors();

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
    		plForm.setPledgeSectors(prevSelSectors);
    		session.removeAttribute("sectorSelected");
    	    return mapping.findForward("forward");
    	}
    }
   
}


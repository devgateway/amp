package org.digijava.module.fundingpledges.action;

import java.util.ArrayList;
import java.util.Calendar;
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
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesSector;
import org.digijava.module.fundingpledges.dbentity.PledgesEntityHelper;
import org.digijava.module.fundingpledges.form.PledgeForm;
import org.digijava.module.gateperm.core.GatePermConst;
import org.springframework.beans.BeanWrapperImpl;

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
    		
    		plForm.setPledgeTypeCategory(CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.PLEDGES_TYPES_KEY));
    		
    		plForm.setPledgeNames(CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.PLEDGES_NAMES_KEY));
    		
    		plForm.setAssistanceTypeCategory(CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.TYPE_OF_ASSISTENCE_KEY));

    		plForm.setAidModalityCategory(CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.FINANCING_INSTRUMENT_KEY));
    		
    		String yearToSpecify = TranslatorWorker.translateText("unspecified", request);
            
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
	        if (request.getParameter("reset") != null && request.getParameter("reset").equalsIgnoreCase("true")) {
	        	resetForm(plForm);
	        	request.getSession().removeAttribute("reset");
			} else if (request.getParameter("pledgeId") != null && Long.valueOf(request.getParameter("pledgeId")) > 0){
				FundingPledges fp = PledgesEntityHelper.getPledgesById(Long.valueOf(request.getParameter("pledgeId")));
				resetForm(plForm);
	        	plForm.setFundingPledges(fp);
				plForm.setPledgeId(fp.getId());
				plForm.setPledgeTitle(fp.getTitle());
				if (fp.getTitle() != null) {
					plForm.setPledgeTitleId(fp.getTitle().getId());
				}
				AmpOrganisation pledgeOrg =	PledgesEntityHelper.getOrganizationById(fp.getOrganization().getAmpOrgId());
				plForm.setSelectedOrgId(pledgeOrg.getAmpOrgId().toString());
	        	plForm.setSelectedOrgName(pledgeOrg.getName());
	        	plForm.setAdditionalInformation(fp.getAdditionalInformation());
	        	plForm.setWhoAuthorizedPledge(fp.getWhoAuthorizedPledge());
	        	plForm.setFurtherApprovalNedded(fp.getFurtherApprovalNedded());
	        	plForm.setContact1Address(fp.getContactAddress());
	        	plForm.setContact1Email(fp.getContactEmail());
	        	plForm.setContact1Fax(fp.getContactFax());
	        	plForm.setContact1Ministry(fp.getContactMinistry());
	        	plForm.setContact1Name(fp.getContactName());
	        	//plForm.setContact1OrgId(String.valueOf(fp.getContactOrganization().getAmpOrgId()));
	        	//plForm.setContact1OrgName(fp.getContactOrganization().getName());
	        	if (fp.getContactOrganization()!=null){
		        	AmpOrganisation cont1Org =	PledgesEntityHelper.getOrganizationById(fp.getContactOrganization().getAmpOrgId());
					plForm.setContact1OrgId(cont1Org.getAmpOrgId().toString());
		        	plForm.setContact1OrgName(cont1Org.getAcronym());
	        	}
	        	plForm.setContact1Telephone(fp.getContactTelephone());
	        	plForm.setContact1Title(fp.getContactTitle());
	        	plForm.setContactAlternate1Email(fp.getContactAlternativeEmail());
	        	plForm.setContactAlternate1Name(fp.getContactAlternativeName());
	        	plForm.setContactAlternate1Telephone(fp.getContactAlternativeTelephone());
	        	plForm.setContact2Address(fp.getContactAddress_1());
	        	plForm.setContact2Email(fp.getContactEmail_1());
	        	plForm.setContact2Fax(fp.getContactFax_1());
	        	plForm.setContact2Ministry(fp.getContactMinistry_1());
	        	plForm.setContact2Name(fp.getContactName_1());
	        	//plForm.setContact2OrgId(String.valueOf(fp.getContactOrganization_1().getAmpOrgId()));
	        	//plForm.setContact2OrgName(fp.getContactOrganization_1().getName());
	        	if (fp.getContactOrganization_1()!=null){
		        	AmpOrganisation cont2Org =	PledgesEntityHelper.getOrganizationById(fp.getContactOrganization_1().getAmpOrgId());
		        	plForm.setContact2OrgId(cont2Org.getAmpOrgId().toString());
		        	plForm.setContact2OrgName(cont2Org.getAcronym());
	        	}
	        	plForm.setContact2Telephone(fp.getContactTelephone_1());
	        	plForm.setContact2Title(fp.getContactTitle_1());
	        	plForm.setContactAlternate2Email(fp.getContactAlternativeEmail_1());
	        	plForm.setContactAlternate2Name(fp.getContactAlternativeName_1());
	        	plForm.setContactAlternate2Telephone(fp.getContactAlternativeTelephone_1());
	        	plForm.setFundingPledgesDetails(fp.getFundingPledgesDetails());
	        	Collection<FundingPledgesSector> fpsl = PledgesEntityHelper.getPledgesSectors(fp.getId());
	        	//Collection<FundingPledgesSector> fpsl = fp.getSectorlist();
	        	Collection<ActivitySector> asl = new ArrayList<ActivitySector>();
	        	Iterator it = fpsl.iterator();
	        	while (it.hasNext()) {
					FundingPledgesSector fps = (FundingPledgesSector) it.next();
					AmpSector ampSec = fps.getSector();
					
					ActivitySector actSec = new ActivitySector();
					actSec.setId(fps.getId());
					actSec.setSectorId(ampSec.getAmpSectorId());
					actSec.setSectorPercentage(fps.getSectorpercentage());
					actSec.setSectorScheme(SectorUtil.getAmpSectorScheme(ampSec.getAmpSecSchemeId().getAmpSecSchemeId()).getSecSchemeName());
					actSec.setConfigId(ampSec.getAmpSecSchemeId().getAmpSecSchemeId());
					if (ampSec.getParentSectorId()==null) {
						actSec.setSectorName(ampSec.getName());
					} else if (ampSec.getParentSectorId().getParentSectorId()==null){
						actSec.setSectorName(ampSec.getParentSectorId().getName());
						actSec.setSubsectorLevel1Name(ampSec.getName());
					} else {
						actSec.setSectorName(ampSec.getParentSectorId().getParentSectorId().getName());
						actSec.setSubsectorLevel1Name(ampSec.getParentSectorId().getName());
						actSec.setSubsectorLevel2Name(ampSec.getName());
					}
					asl.add(actSec);
				}
	        	
	        	plForm.setPledgeSectors(asl);
	        	plForm.setSelectedLocs(PledgesEntityHelper.getPledgesLocations(fp.getId()));
	        	plForm.setSelectedProgs(PledgesEntityHelper.getPledgesPrograms(fp.getId()));
	        	plForm.setFundingPledgesDetails(PledgesEntityHelper.getPledgesDetails(fp.getId()));
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
    
    private void resetForm(PledgeForm plForm){
    	plForm.setPledgeId(null);
		plForm.setPledgeTitle(null);
		plForm.setPledgeTitleId(null);
		plForm.setFundingPledges(null);
		plForm.setSelectedOrgId(null);
    	plForm.setSelectedOrgName(null);
    	plForm.setAdditionalInformation(null);
    	plForm.setWhoAuthorizedPledge(null);
    	plForm.setFurtherApprovalNedded(null);
    	plForm.setContact1Address(null);
    	plForm.setContact1Email(null);
    	plForm.setContact1Fax(null);
    	plForm.setContact1Ministry(null);
    	plForm.setContact1Name(null);
    	plForm.setContact1OrgId(null);
    	plForm.setContact1OrgName(null);
    	plForm.setContact1Telephone(null);
    	plForm.setContact1Title(null);
    	plForm.setContactAlternate1Email(null);
    	plForm.setContactAlternate1Name(null);
    	plForm.setContactAlternate1Telephone(null);
    	plForm.setContact2Address(null);
    	plForm.setContact2Email(null);
    	plForm.setContact2Fax(null);
    	plForm.setContact2Ministry(null);
    	plForm.setContact2Name(null);
    	plForm.setContact2OrgId(null);
    	plForm.setContact2OrgName(null);
    	plForm.setContact2Telephone(null);
    	plForm.setContact2Title(null);
    	plForm.setContactAlternate2Email(null);
    	plForm.setContactAlternate2Name(null);
    	plForm.setContactAlternate2Telephone(null);
    	plForm.setFundingPledgesDetails(null);
    	plForm.setPledgeSectors(null);
    	plForm.setSelectedLocs(null);
    	plForm.setSelectedProgs(null);
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
    		session.removeAttribute("addSector");
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
    		session.removeAttribute("addSector");
    	    return mapping.findForward("forward");
    	}
    }
   
}


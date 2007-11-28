

package org.digijava.module.aim.action;

import java.util.*;

import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.digijava.module.aim.form.*;
import org.digijava.module.aim.helper.*;
import org.digijava.module.aim.util.*;
import org.digijava.module.aim.dbentity.*;

import org.digijava.module.aim.dbentity.AmpSectorIndicator;

public class ViewIndicators
    extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
    	
        List<IndicatorsBean> allInds = new ArrayList();
      
        ViewIndicatorsForm allIndForm = (ViewIndicatorsForm) form;
        
        String view = request.getParameter("sector");
        if(view!=null){
        if(view.equalsIgnoreCase("viewall")){
        	allIndForm.setSectorId(new Long(-1));
        }
        }
       
        
        if(allIndForm.getCategory()==-1 ||
           allIndForm.getCategory()==0) {
            Collection<AllThemes> themeCol = ProgramUtil.getAllThemeIndicators();
        
            Collection allSectors = SectorUtil.getAllSectors();
            allIndForm.setSectors(allSectors);
           
            for(Iterator itr = themeCol.iterator(); itr.hasNext(); ) {
                AllThemes theme = (AllThemes) itr.next();
                Collection<AllPrgIndicators> themeIndsCol = theme.getAllPrgIndicators();
                if(themeIndsCol != null) {
                    for(Iterator indItr = themeIndsCol.iterator(); indItr.hasNext(); ) {
                        AllPrgIndicators tInd = (AllPrgIndicators) indItr.next();

                        IndicatorsBean ind = new IndicatorsBean(tInd);
                        ind.setType("0");
                        ind.setCategory(Integer.valueOf(tInd.getCategory()));
                        ind.setSectorName("Z");
                        
                        Collection indSector=ind.getSector();
        				if(indSector != null){
        					for(Iterator sectItr=indSector.iterator();sectItr.hasNext();){
        						AmpIndSectors sect=(AmpIndSectors)sectItr.next();
        							ind.setSectorName(sect.getSectorId().getName());
        						}
        					}
                        
                             boolean addFlag = false;

                        if(allIndForm.getSectorId()!=-1){
            				Collection indSectors=ind.getSector();
            				if(indSectors!=null){
            					for(Iterator sectItr=indSectors.iterator();sectItr.hasNext();){
            						AmpIndSectors sect=(AmpIndSectors)sectItr.next();
            						
            						if(sect.getSectorId().getAmpSectorId().equals(allIndForm.getSectorId())){
            							addFlag = true;
            							break;
            						}
            					}
            				}
            			}else{
            				addFlag=true;
            			}
            			
                        if(addFlag) {
                            if(allIndForm.getKeyword() != null && (ind.getName().toLowerCase().indexOf(allIndForm.getKeyword().toLowerCase()) > -1)) {
                                allInds.add(ind);
                            } else {
                                allInds.add(ind);
                            }
                        }
                    }
                }
            }
        }

        if((allIndForm.getCategory()==-1 || allIndForm.getCategory()==1)) {

            Collection<AllActivities> actIdsCol = MEIndicatorsUtil.getAllActivityIds();
            if(actIdsCol != null) {
                for(Iterator itr = actIdsCol.iterator(); itr.hasNext(); ) {
                    AllActivities act = (AllActivities) itr.next();
                    Collection<AllMEIndicators> prjIndsCol = act.getAllMEIndicators();
                    if(prjIndsCol != null) {
                        List<AllMEIndicators> prjIndsList = new ArrayList<AllMEIndicators>(prjIndsCol);
                        for(Iterator indItr = prjIndsList.iterator(); indItr.hasNext(); ) {
                            AllMEIndicators tInd = (AllMEIndicators) indItr.next();

                            IndicatorsBean ind = new IndicatorsBean(tInd);
                            ind.setCategory(Integer.valueOf( -1));
                            ind.setType("1");
                            ind.setSectorName("Z");
                            
                            Collection indSector=ind.getSector();
            				if(indSector !=null){
            					for(Iterator sectItr=indSector.iterator();sectItr.hasNext();){
            						AmpIndSectors sect=(AmpIndSectors)sectItr.next();
            							ind.setSectorName(sect.getSectorId().getName());
            						}
            					}

                            boolean addFlag = false;

                            if(allIndForm.getSectorId()!=-1){
                				Collection indSectors=ind.getSector();
                				if(indSectors != null){
                					for(Iterator sectItr=indSectors.iterator();sectItr.hasNext();){
                						AmpIndSectors sect=(AmpIndSectors)sectItr.next();
                							ind.setSectorName(sect.getSectorId().getName());
                							
                						//ind.setSectorName(sect.getSectorId().getName());
                						if(sect.getSectorId().getAmpSectorId().equals(allIndForm.getSectorId())){
                							addFlag = true;
                							break;
                						}
                					}
                				}
                				
                			}else{
                				
                				addFlag=true;
                			}
                			
                            if(addFlag) {
                                if(allIndForm.getKeyword() != null && (ind.getName().toLowerCase().indexOf(allIndForm.getKeyword().toLowerCase()) > -1)) {
                                    allInds.add(ind);
                                } else {
                                    allInds.add(ind);
                                }
                            }
                        }
                    }
                }
            }
        }

        if((allIndForm.getCategory()==-1 || allIndForm.getCategory()==1)) {
            Collection<AmpMEIndicatorList> meIndCol = MEIndicatorsUtil.getAllIndicators();
            if(meIndCol != null) {
                for(Iterator itr = meIndCol.iterator(); itr.hasNext(); ) {
                    AmpMEIndicatorList tInd = (AmpMEIndicatorList) itr.next();

                    IndicatorsBean ind = new IndicatorsBean(tInd);
                    ind.setCategory(Integer.valueOf(-1));
                    ind.setType("2");
                    ind.setSectorName("Z");
                    
                    Collection indSector=ind.getSector();
    				if(indSector != null){
    					for(Iterator sectItr=indSector.iterator();sectItr.hasNext();){
    						AmpIndSectors sect=(AmpIndSectors)sectItr.next();
    						
    							ind.setSectorName(sect.getSectorId().getName());
    						 
    						}
    					
    				}
                    boolean addFlag = false;

                    if(allIndForm.getSectorId()!=-1){
        				Collection indSectors=ind.getSector();
        				if(indSectors!=null){
        					for(Iterator sectItr=indSectors.iterator();sectItr.hasNext();){
        						AmpIndSectors sect=(AmpIndSectors)sectItr.next();
        						ind.setSectorName(sect.getSectorId().getName());
        						if(sect.getSectorId().getAmpSectorId().equals(allIndForm.getSectorId())){
        							addFlag = true;
        							break;
        						}
        					}
        				}
        			}else{
        				addFlag=true;
        			}
        			
                    if(addFlag) {
                        if(allIndForm.getKeyword() != null && (ind.getName().toLowerCase().indexOf(allIndForm.getKeyword().toLowerCase()) > -1)) {
                            allInds.add(ind);
                        } else {
                            allInds.add(ind);
                        }
                    }
                }
            }
        }

        switch(allIndForm.getSortBy()) {
            case 0: {
                Collections.sort(allInds, new ProgramUtil.HelperAllIndicatorBeanNameComparator());
                break;
            }
            case 1: {
                Collections.sort(allInds, new ProgramUtil.HelperAllIndicatorBeanSectorComparator());
                break;
            }
            case 2:{
                Collections.sort(allInds, new ProgramUtil.HelperAllIndicatorBeanTypeComparator());
                break;
            }
            default:{
                Collections.sort(allInds, new ProgramUtil.HelperAllIndicatorBeanNameComparator());
            }
        }

        allIndForm.setAllIndicators(allInds);
        
        return mapping.findForward("forward");
    }
}

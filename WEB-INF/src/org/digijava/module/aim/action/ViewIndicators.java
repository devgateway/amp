

package org.digijava.module.aim.action;

import java.util.*;

import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.struts.util.LabelValueBean;
import org.digijava.module.aim.form.*;
import org.digijava.module.aim.helper.*;
import org.digijava.module.aim.util.*;
import org.digijava.module.aim.dbentity.AmpIndicatorSector;
import org.digijava.module.aim.dbentity.AmpMEIndicatorValue;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.AmpThemeIndicators;

import org.digijava.module.aim.dbentity.AmpSectorIndicator;

public class ViewIndicators
    extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
    	
      
    	
        List<IndicatorsBean> allInds = new ArrayList();
      
        ViewIndicatorsForm allIndForm = (ViewIndicatorsForm) form;
        allIndForm.setThemeName("");
        
        
       
        
        String types = request.getParameter("indicator");
        String id = request.getParameter("indicatorId");
        String view = request.getParameter("sector");
    
        if(view!=null){
        if(view.equalsIgnoreCase("viewall")){
        	allIndForm.setSectorId(new Long(-1));
         }
        }
       
        
        if(allIndForm.getCategory()==-1 ||
           allIndForm.getCategory()==0) {
            //Collection<AllThemes> themeCol = ProgramUtil.getAllThemeIndicators();
            Collection indsectors = ProgramUtil.getAmpThemeIndicators();
            Collection allSectors = SectorUtil.getAllSectors();
            allIndForm.setSectors(allSectors);
            for(Iterator itr = indsectors.iterator(); itr.hasNext();){
            	AmpThemeIndicators  indicat = (AmpThemeIndicators) itr.next();
            	
                IndicatorsBean indbean = new IndicatorsBean(indicat);
                
            	indbean.getName();
            	indbean.setType("0");
            	indbean.setCategory(Integer.valueOf(indbean.getCategory()));
            	indbean.setSectorName("Z");
            	
            	Collection indSector=indbean.getSector();
            	if(indSector != null){
					for(Iterator sectItr=indSector.iterator();sectItr.hasNext();){
            	        AmpIndicatorSector sect=(AmpIndicatorSector)sectItr.next();
						indbean.setSectorName(sect.getSectorId().getName());
						}
				}
            	    boolean addFlag = false;
            	    
            	    if(allIndForm.getSectorId()!=-1){
        				Collection indSectors=indbean.getSector();
        				if(indSectors!=null){
        					for(Iterator sectItr=indSectors.iterator();sectItr.hasNext();){
        						AmpIndicatorSector sect=(AmpIndicatorSector)sectItr.next();
        						
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
                        if(allIndForm.getKeyword() != null && (indbean.getName().toLowerCase().indexOf(allIndForm.getKeyword().toLowerCase()) > -1)) {
                            allInds.add(indbean);
                        } else {
                            allInds.add(indbean);
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
                            ind.setCategory(Integer.valueOf(-1));
                            ind.setType("1");
                            ind.setSectorName("Z");

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
                    for(Iterator itre =allInds.iterator(); itre.hasNext();){
                    	IndicatorsBean inde = (IndicatorsBean) itre.next();
                    	if(ind.getName().equals(inde.getName()) && inde.getType() == "1"){
                    		 
                    		ind.setFlag(true);
                    	}
                    	
                   	}
                    ind.setCategory(Integer.valueOf(-1));
                    ind.setType("2");
                    ind.setSectorName("Z");
                       
                    boolean addFlag = false;

                    if(allIndForm.getSectorId()!=-1){
        				Collection indSectors=ind.getSector();
        				if(indSectors!=null){
        					for(Iterator sectItr=indSectors.iterator();sectItr.hasNext();){
        						AmpIndicatorSector sect=(AmpIndicatorSector)sectItr.next();
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
        			if(!ind.isFlag()){
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
        switch(allIndForm.getSortBy()) {
            case 0: {
                Collections.sort(allInds, new ProgramUtil.HelperAllIndicatorBeanNameComparator());
                break;
            }
            case 1: {
                Collections.sort(allInds, new ProgramUtil.HelperAllIndicatorBeanSectorComparator());
                break;
            }
//            case 2:{
//                Collections.sort(allInds, new ProgramUtil.HelperAllIndicatorBeanTypeComparator());
//                break;
//            }
            default:{
                Collections.sort(allInds, new ProgramUtil.HelperAllIndicatorBeanNameComparator());
            }
        }
        
        allIndForm.setAllIndicators(allInds);
        
       if (types != null && id != null)
       {
    	   if(!types.equals("deleted")){
    	   
	        if(types.equalsIgnoreCase("deleteProgram"))
			{
				 AmpThemeIndicators tempInd = new AmpThemeIndicators();
				  tempInd = ProgramUtil.getThemeIndicatorById(new Long(id));
				  if(tempInd.getThemes().size() != 0){
					  Iterator itr=tempInd.getThemes().iterator();
					  while (itr.hasNext()) {
						AmpTheme insect = (AmpTheme) itr.next();
						allIndForm.setThemeName(insect.getName());
					}
					  return mapping.findForward("forward");
				  }else{
				      ProgramUtil.deleteProgramIndicator(new Long(id));
				  }
				  
				  return mapping.findForward("deleted");
			}
	        
	        if(types.equalsIgnoreCase("deleteGlobal"))
			{
	        	MEIndicatorsUtil.deleteProjIndicator(new Long(id));
	           	 return mapping.findForward("forward");
			}
	        
	        if(types.equalsIgnoreCase("deleteProject"))
			{
	         	
	        	   Collection<AllActivities> actIdsCol = MEIndicatorsUtil.getAllActivityIds();
	               if(actIdsCol != null) {
	                   for(Iterator itr = actIdsCol.iterator(); itr.hasNext(); ) {
	                       AllActivities act = (AllActivities) itr.next();
	                       Collection<AllMEIndicators> prjIndsCol = act.getAllMEIndicators();
	                       if(prjIndsCol != null) {
	                           List<AllMEIndicators> prjIndsList = new ArrayList<AllMEIndicators>(prjIndsCol);
	                           for(Iterator indItr = prjIndsList.iterator(); indItr.hasNext(); ) {
	                               AllMEIndicators tInd = (AllMEIndicators) indItr.next();
	                               if(tInd.getAmpMEIndId().equals(Long.valueOf(id))){
	                            	   
	                            	   allIndForm.setThemeName(act.getActivityName());
	                            	   allIndForm.setFlag("project");

	                                   return mapping.findForward("forward");
	                                  }
	                               }
	                            }else{
	                             MEIndicatorsUtil.deleteProjIndicator(new Long(id));
	                  			 return mapping.findForward("delete");
	                        }
	                     }
	                 }
	        	 }
    	      }
    	   }
        return mapping.findForward("forward");
    }
  }


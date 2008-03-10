

package org.digijava.module.aim.action;

import java.util.*;

import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.struts.util.LabelValueBean;
import org.digijava.module.aim.form.*;
import org.digijava.module.aim.helper.*;
import org.digijava.module.aim.util.*;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpIndicatorSector;
import org.digijava.module.aim.dbentity.AmpMEIndicatorValue;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.AmpThemeIndicators;

import org.digijava.module.aim.dbentity.AmpSectorIndicator;

public class ViewIndicators
    extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
   
    	List<IndicatorsBean> allInds = new ArrayList<IndicatorsBean>();
    	Collection sectorsName = new ArrayList();
      
        ViewIndicatorsForm allIndForm = (ViewIndicatorsForm) form;
        allIndForm.setThemeName("");
        
        String types = request.getParameter("indicator");
        String id = request.getParameter("indicatorId");
        String view = request.getParameter("sector");
    
        if(view!=null){
        if(view.equalsIgnoreCase("viewall")){
        	allIndForm.setSectorId(new Long(-1));
        	allIndForm.setKeyword(null);
        	
         }
        }
  
        if (types != null && id != null){
            if(types.equals("delete")){
  
            	IndicatorUtil.deleteIndicator(new Long(id));
            	
             }
            }
        
        List<AmpIndicator>  searchResult = IndicatorUtil.getAllIndicators();
        Collection allSectors = SectorUtil.getAllParentSectors();
      
      
       allIndForm.setSectors(allSectors);
       for(AmpIndicator indicator : searchResult){
        	
        	
        	IndicatorsBean indbean = new IndicatorsBean(indicator);
        	
        	indbean.setName(indicator.getName().toLowerCase());
         	indbean.setType("0");
         	indbean.setCategory(Integer.valueOf(indbean.getCategory()));
            indbean.setSectorName("Z");
         	
         	
         	Collection indSector=indbean.getSector();
         	
         	
         	if(indSector != null){
				for(Iterator sectItr=indSector.iterator();sectItr.hasNext();){
        	        AmpSector sect=(AmpSector)sectItr.next();
        	        indbean.getSectorNames().add(sect.getName());
        	        indbean.setSectorName(sect.getName());
        			
        			
				 }
			  }
          	
         	boolean addFlag = false;
         	  if(allIndForm.getSectorId()!=-1){
  				Collection indSectors=indbean.getSector();
  				if(indSectors!=null){
   					for(Iterator sectItr=indSectors.iterator();sectItr.hasNext();){
  						AmpSector sect=(AmpSector)sectItr.next();
  						
  						if(sect.getAmpSectorId().equals(allIndForm.getSectorId())){
  							addFlag = true;
  							break;
  						}
  					}
  				}
  			}else{
  				addFlag=true;
  			}
         	 if(addFlag) {
               if(allIndForm.getKeyword() != null  && (indbean.getName().toLowerCase().indexOf(allIndForm.getKeyword().toLowerCase()) > -1)) {
            	  if(allIndForm.getKeyword() != ""){
            	   allInds.clear();
            	   allInds.add(indbean);
            	   break;
            	  }
            	  allInds.add(indbean);
               } else {
                   allInds.add(indbean);
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
////            case 2:{
////                Collections.sort(allInds, new ProgramUtil.HelperAllIndicatorBeanTypeComparator());
////                break;
////            }
            default:{
                Collections.sort(allInds, new ProgramUtil.HelperAllIndicatorBeanNameComparator());
            }
        }
        
        
        allIndForm.setAllIndicators(allInds); 
 
        return mapping.findForward("forward");
    }
  }


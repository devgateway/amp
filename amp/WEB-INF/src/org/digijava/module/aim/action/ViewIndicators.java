

package org.digijava.module.aim.action;

import org.apache.struts.action.*;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.form.ViewIndicatorsForm;
import org.digijava.module.aim.helper.IndicatorsBean;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class ViewIndicators
    extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
   
        List<IndicatorsBean> allInds = new ArrayList<IndicatorsBean>();
        Collection sectorsName = new ArrayList();
      
        ViewIndicatorsForm allIndForm = (ViewIndicatorsForm) form;
        allIndForm.setThemeName("");
        String view = request.getParameter("sector");
    
        if(view!=null){
        if(view.equalsIgnoreCase("viewall")){
            allIndForm.setSectorId(new Long(-1));
            allIndForm.setKeyword(null);
            
         }
        }
        
        List<AmpIndicator>  searchResult = IndicatorUtil.getAllIndicators();
        Collection allSectors = SectorUtil.getAllParentSectors();
      
      
       allIndForm.setSectors(allSectors);
       allInds.clear();
       for(AmpIndicator indicator : searchResult){
            
            
            IndicatorsBean indbean = new IndicatorsBean(indicator);
            
            indbean.setName(indicator.getName());
            indbean.setType("0");
            indbean.setCategory(Integer.valueOf(indbean.getCategory()));
            indbean.setSectorName("Z");
            
            
            Collection indSector=indbean.getSector();
            
            
            if(indSector != null){
                for (Object o : indSector) {
                    AmpSector sect = (AmpSector) o;
                    indbean.getSectorNames().add(sect.getName());
                    indbean.setSectorName(sect.getName());


                }
              }
            
            boolean addFlag = false;
              if(allIndForm.getSectorId()!=-1){
                Collection indSectors=indbean.getSector();
                if(indSectors!=null){
                    for (Object sector : indSectors) {
                        AmpSector sect = (AmpSector) sector;

                        if (sect.getAmpSectorId().equals(allIndForm.getSectorId())) {
                            addFlag = true;
                            break;
                        }
                    }
                }
            }else{
                addFlag=true;
            }
            if (addFlag) {
                if (allIndForm.getKeyword() != ""
                        && allIndForm.getKeyword() != null) {
                    if (indbean.getName().toLowerCase().indexOf(
                            allIndForm.getKeyword().toLowerCase()) > -1) {
                        allInds.add(indbean);
                    }
                } else {
                    allInds.add(indbean);
                }
            }
        }
             
        if(allIndForm.getSortBy()!=null){
            if(allIndForm.getSortBy().equalsIgnoreCase("nameAsc")){
                 Collections.sort(allInds, new ProgramUtil.HelperAllIndicatorBeanNameComparator());
            }else if(allIndForm.getSortBy().equalsIgnoreCase("nameDesc")){
                Collections.sort(allInds, new ProgramUtil.HelperAllIndicatorBeanNameDescendingComparator());
            }else if(allIndForm.getSortBy().equalsIgnoreCase("sectAsc")){
                Collections.sort(allInds, new ProgramUtil.HelperAllIndicatorBeanSectorComparator());
            }else if(allIndForm.getSortBy().equalsIgnoreCase("sectDesc")){
                Collections.sort(allInds, new ProgramUtil.HelperAllIndicatorBeanSectorDescendingComparator());
            }
        }else{
            Collections.sort(allInds, new ProgramUtil.HelperAllIndicatorBeanNameComparator());
        }
        
//        switch(allIndForm.getSortBy()) {
//            case 0: {
//                Collections.sort(allInds, new ProgramUtil.HelperAllIndicatorBeanNameComparator());
//                break;
//            }
//            case 1: {
//                Collections.sort(allInds, new ProgramUtil.HelperAllIndicatorBeanSectorComparator());
//                break;
//            }
//////            case 2:{
//////                Collections.sort(allInds, new ProgramUtil.HelperAllIndicatorBeanTypeComparator());
//////                break;
//////            }
//            default:{
//                Collections.sort(allInds, new ProgramUtil.HelperAllIndicatorBeanNameComparator());
//            }
//        }
        
        
        allIndForm.setAllIndicators(allInds); 
 
        ActionMessages errors = (ActionMessages)request.getSession().getAttribute("removeIndicatorErrors");
        if(errors!=null){
            saveErrors(request, errors);
            request.getSession().removeAttribute("removeIndicatorErrors");
        }
        return mapping.findForward("forward");
    }
  }


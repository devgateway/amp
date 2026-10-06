package org.digijava.module.aim.action;

import org.apache.struts.action.*;
import org.apache.struts.util.LabelValueBean;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.form.NewIndicatorForm;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.AllPrgIndicators;
import org.digijava.module.aim.helper.AmpPrgIndicator;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.aim.util.SectorUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class ViewEditIndicator
    extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                             HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        
        NewIndicatorForm indForm = (NewIndicatorForm) form;

        String action=request.getParameter("action");
        if(action!=null && action.equalsIgnoreCase("save") && indForm.getName() != null && indForm.getCode() != null){
//            switch(existIndForm.getIndType()) {
//                case 0: {
//                    if(existIndForm.getSelectedProgramId() == null) {
//                        return mapping.findForward("forward");
//                    }

 //         if(indForm.getIndType() == 2){
                
                    AmpPrgIndicator newInd = new AmpPrgIndicator();
                    newInd.setIndicatorId(indForm.getId());
                    newInd.setCategory(indForm.getCategory());
                    newInd.setCode(indForm.getCode());
                    newInd.setCreationDate(indForm.getDate());
                    newInd.setDescription(indForm.getDescription());
                    newInd.setName(indForm.getName());
                    newInd.setType(indForm.getType());
                    newInd.setSector(indForm.getSelActivitySector());
                    newInd.setIndSectores(indForm.getActivitySectors());
                    //existIndForm.setSelectedProgramId(new Long(1));
                    //newInd.setSelectedActivityId(existIndForm.getSelectedActivityId());
                    newInd.setSelectedActivity(indForm.getSelectedActivities());  
                  
//                    if(indForm.getPrjStatus().equals("prjUnchecked")){
//                      newInd.setPrjStatus(true);
//                      newInd.setType("programInd");
//                    }else 
                        if (indForm.getActivitySectors() != null  && indForm.getSelectedActivityId() != null){
                        newInd.setType("prg/prj");
                    }
//                    
//                    if(indForm.getPrgStatus().equals("prgUnchecked")){
//                      newInd.setPrgStatus(true);
//                      newInd.setType("projectInd");
//                    }
                    
                    //TODO INDIC code above this needs to be refactored !!!!! or deleted!
                    Date date=DateConversion.getDateForIndicator(indForm.getDate());
                    AmpIndicator indicator=new AmpIndicator();
                    indicator.setIndicatorId(indForm.getId());
                    indicator.setName(indForm.getName());
                    indicator.setDescription(indForm.getDescription());
                    indicator.setCode(indForm.getCode());
                    indicator.setCreationDate(date);
                    indicator.setType(indForm.getType());
                    Collection sectors=indForm.getActivitySectors();
                    if (sectors!=null && sectors.size()>0){
                        indicator.setSectors(new HashSet<AmpSector>());
                        for (Iterator sectIter = sectors.iterator(); sectIter.hasNext();) {
                            ActivitySector actSector= (ActivitySector) sectIter.next();
                            AmpSector sector=SectorUtil.getAmpSector(actSector.getSectorId());
                            indicator.getSectors().add(sector);
                        }
                    }
                    if(!IndicatorUtil.validateIndicatorName(indicator)){
                        IndicatorUtil.saveIndicator(indicator);
                        indForm.reset();
                        indForm.setAction("added");
                        return mapping.findForward("forward");
                    }
                    else{
                        ActionMessages errors=new ActionMessages();
                        errors.add("title", new ActionMessage("error.aim.addIndicator.duplicateName"));
                        saveErrors(request, errors);
                        return mapping.findForward("forward");
                    }
                    
                    //TODO INDIC line below commented by me.
//                    IndicatorUtil.saveIndicators(newInd);
//                    ProgramUtil.saveThemeIndicators(newInd, existIndForm.getSelectedProgramId());
                    
//                    break;
       //   }
//                }
//                case 1: {
//
//                    AllMEIndicators newInd = new AllMEIndicators();
//
//                  newInd.setAmpMEIndId(existIndForm.getId());
//                    newInd.setCode(existIndForm.getCode());
//                    newInd.setName(existIndForm.getName());
//
//                    MEIndicatorsUtil.saveIndicator(newInd);
//                    existIndForm.reset();
//
//                    break;
//                }
//                case 2: {
//                    if(existIndForm.getSelectedActivityId() == null) {
//                        return mapping.findForward("forward");
//                    }
//
//                    AmpMEIndicators newInd = new AmpMEIndicators();
//
//                    newInd.setAmpMEIndId(existIndForm.getId());
//                    newInd.setCode(existIndForm.getCode());
//                    newInd.setDescription(existIndForm.getDescription());
//                    newInd.setName(existIndForm.getName());
//                    MEIndicatorsUtil.updateMEIndicator(newInd);
//
//                    existIndForm.reset();
//
//                    break;
//                }
//            }
        }
        
        String indId=request.getParameter("id");
        String indType=request.getParameter("type");
        String parentId=request.getParameter("parentid");

        if(indId==null){
            return mapping.findForward("showList");
        }else
                 {
            indForm.reset();
           // AllPrgIndicators ind= ProgramUtil.getThemeIndicator(Long.valueOf(indId));
            AllPrgIndicators ind = IndicatorUtil.getAmpIndicator(Long.valueOf(indId));
            if(ind!=null){

                indForm.setCode(ind.getCode());
                indForm.setDate(ind.getCreationDate());
                indForm.setDescription(ind.getDescription());
                indForm.setName(ind.getName());
                indForm.setType(ind.getType());
                indForm.setCategory(ind.getCategory());
                indForm.setId(ind.getIndicatorId());                
                indForm.setPrjStatus(null);
                indForm.setPrgStatus(null);
                
                  Collection indSector=ind.getSector();
                  Collection Sectors = new ArrayList();
                if(indSector != null){
            
                    for(Iterator sectItr=indSector.iterator();sectItr.hasNext();){
                        
                        AmpSector sect=(AmpSector)sectItr.next();
                        ActivitySector actsec = new ActivitySector();
                        
                            actsec.setSectorId(sect.getAmpSectorId());
                            actsec.setSectorName(sect.getName());
                            actsec.setSubsectorLevel1Id(new Long(-1));
                                    
                          Sectors.add(actsec);
                 
                        }
                    }
               
            
                indForm.setActivitySectors(Sectors);
                
                Collection<LabelValueBean> actCol=new ArrayList<LabelValueBean>();
                if(ind.getActivity() !=null){
                    for(Iterator activity = ind.getActivity().iterator(); activity.hasNext();){
                        
                        AmpActivity act = (AmpActivity) activity.next();
                        act.getAmpActivityId();
                        String actName = act.getName();
                        LabelValueBean lvb=new LabelValueBean(actName,act.getAmpActivityId().toString());       
                        actCol.add(lvb);
                    }
                }
                indForm.setSelectedActivities(actCol);
               
              
                

//                if(ind.getThemes() != null) {
//                    Collection<LabelValueBean> prgCol=new ArrayList<LabelValueBean>();
//                    for(Iterator prgIter = ind.getThemes().iterator(); prgIter.hasNext(); ) {
//                        AmpTheme prg = (AmpTheme) prgIter.next();
//
//                        String prgName = null;
//                        if(prg.getName().length() > 35) {
//                            prgName = prg.getName().substring(0, 32) + "...";
//                        } else {
//                            prgName = prg.getName();
//                        }
//
//                        LabelValueBean lvb=new LabelValueBean(prgName,prg.getAmpThemeId().toString());
//                        prgCol.add(lvb);
//                        existIndForm.setSelectedProgramId(prg.getAmpThemeId());
//                    }
//                    existIndForm.setSelectedPrograms(prgCol);
//                }
            }
            return mapping.findForward("forward");
        }       
        
    }    
  }
        //else if(indType.equalsIgnoreCase("project")){
//          existIndForm.reset();
//            Collection<AllActivities> actIdsCol = MEIndicatorsUtil.getAllActivityIds();
//            if(actIdsCol != null) {
//                for(Iterator itr = actIdsCol.iterator(); itr.hasNext(); ) {
//                    AllActivities act = (AllActivities) itr.next();
//                    Collection<AllMEIndicators> prjIndsCol = act.getAllMEIndicators();
//                    if(prjIndsCol != null) {
//                        List<AllMEIndicators> prjIndsList = new ArrayList<AllMEIndicators>(prjIndsCol);
//                        for(Iterator indItr = prjIndsList.iterator(); indItr.hasNext(); ) {
//                            AllMEIndicators tInd = (AllMEIndicators) indItr.next();
//                            if(tInd.getAmpMEIndId().equals(Long.valueOf(indId))){
//                              existIndForm.setCode(tInd.getCode());
//                              existIndForm.setName(tInd.getName());
//                              existIndForm.setId(tInd.getAmpMEIndId());
//                              existIndForm.setIndType(2);
//
//                                Collection<LabelValueBean> actCol=new ArrayList<LabelValueBean>();
//                                String actName=null;
//                                if(act.getActivityName().length()>35){
//                                    actName=act.getActivityName().substring(0,32)+"...";
//                                }else{
//                                    actName=act.getActivityName();
//                                }
//                                LabelValueBean lvb=new LabelValueBean(actName,act.getActivityId().toString());
//                                actCol.add(lvb);
//                                existIndForm.setSelectedActivities(actCol);
//
//                                return mapping.findForward("forward");
//                            }
//                        }
//                    }
//                }
//            }
//            return mapping.findForward("forward");
//        }else if(indType.equalsIgnoreCase("global")){
//            existIndForm.reset();
//            AllMEIndicators ind =MEIndicatorsUtil.getMEIndicator(Long.valueOf(indId));
//            if(ind!=null){
//                existIndForm.setCode(ind.getCode());
//                existIndForm.setId(ind.getAmpMEIndId());
//                existIndForm.setName(ind.getName());
//                existIndForm.setIndType(1);
//            }
//            return mapping.findForward("forward");
//        }
//
//        return mapping.findForward("forward");
//    }
//    public ViewEditIndicator() {
//    }
//}

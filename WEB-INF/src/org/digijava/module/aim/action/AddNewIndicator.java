package org.digijava.module.aim.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.form.NewIndicatorForm;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.AmpPrgIndicator;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.aim.util.SectorUtil;

public class AddNewIndicator
    extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        NewIndicatorForm newIndForm = (NewIndicatorForm) form;

        String action=request.getParameter("action");
        String type=request.getParameter("type");
        String parentId=request.getParameter("parentid");
        String event=request.getParameter("event");

        if(action ==null ){
            newIndForm.reset();
            newIndForm.setActivitySectors(null);
        }
//       if(type!=null && parentId!=null){
//            if(type.equalsIgnoreCase("program")){
//                AmpTheme theme=ProgramUtil.getThemeById(Long.valueOf(parentId));
//                if(theme!=null){
//                    Collection<LabelValueBean> prgCol = new ArrayList<LabelValueBean>();
//
//                    String prgName = null;
//                    if(theme.getName().length() > 35) {
//                        prgName = theme.getName().substring(0, 32) + "...";
//                    } else {
//                        prgName = theme.getName();
//                    }
//
//                    LabelValueBean lvb = new LabelValueBean(prgName, theme.getAmpThemeId().toString());
//                    prgCol.add(lvb);
//
//                    newIndForm.setSelectedProgramId(theme.getAmpThemeId());
//                    newIndForm.setSelectedPrograms(prgCol);
//                    newIndForm.setIndType(0);
//                }
//            }else if(type.equalsIgnoreCase("activity")){
//                AmpActivity act=ActivityUtil.getAmpActivity(Long.valueOf(parentId));
//                if(act!=null){
//                    Collection<LabelValueBean> actCol=new ArrayList<LabelValueBean>();
//                    String actName=null;
//                    if(act.getName().length()>35){
//                        actName=act.getName().substring(0,32)+"...";
//                    }else{
//                        actName=act.getName();
//                    }
//
//                    LabelValueBean lvb=new LabelValueBean(actName,act.getAmpActivityId().toString());
//                    actCol.add(lvb);
//
//                    newIndForm.setSelectedActivityId(act.getAmpActivityId());
//                    newIndForm.setSelectedActivities(actCol);
//                    newIndForm.setIndType(2);
//                }
//            }
//        }
        	
        if(action!=null && action.equalsIgnoreCase("add") && newIndForm.getName() != null &&  
             newIndForm.getCode() != null){
        	
//        	if(newIndForm.getIndicatorType() == 0 || newIndForm.getIndType() == 2){
        		 AmpPrgIndicator newInd = new AmpPrgIndicator();
                 newInd.setCategory(newIndForm.getCategory());
                 newInd.setCode(newIndForm.getCode());
                 newInd.setCreationDate(newIndForm.getDate());
                 newInd.setDescription(newIndForm.getDescription());
                 newInd.setName(newIndForm.getName());               	
           
                 
                 
//                 if(newIndForm.getActivitySectors() != null &&
//                		 newIndForm.getSelectedActivityId() == null  ){
//                	newInd.setType("programInd");
//                 }
//                 
//                 if(newIndForm.getSelectedActivities() != null &&
//                	newIndForm.getActivitySectors() == null){
//                	newInd.setType("projectInd");
//                 }
                 
                 newInd.setSector(newIndForm.getSelActivitySector());
                 
                 newInd.setIndSectores(newIndForm.getActivitySectors());
	                
                 if(newIndForm.getSelectedActivityId() == null && 
	                	newIndForm.getActivitySectors() == null ){
	                	   newInd.setDefaultInd(true);
	                	   newInd.setType("global");
	                      }
	                 
	                 
                 newInd.setSelectedActivity(newIndForm.getSelectedActivities());
                 //TODO INDIC IndicatorUtil.saveIndicators(newInd);
//               ProgramUtil.saveThemeIndicators(newInd, newIndForm.getSelectedProgramId());
                 //newIndForm.reset();
                 
                 //TODO INDIC  all "add" action above is depricated!!! because it is awful!!!!
                 AmpIndicator indicator = new AmpIndicator();
                 indicator.setName(newIndForm.getName());
                 indicator.setDescription(newIndForm.getDescription());
                 indicator.setCreationDate(new Date());
                 indicator.setCode(newIndForm.getCode());
                 indicator.setType(newIndForm.getType());
                 if (newIndForm.getActivitySectors()!=null && newIndForm.getActivitySectors().size()>0){
                	 indicator.setSectors(new HashSet<AmpSector>());
                	 for (Iterator sectorIt = newIndForm.getActivitySectors().iterator(); sectorIt.hasNext();) {
                		 //This is awful! why is here AmpActivitySector???!!
						ActivitySector actSector = (ActivitySector) sectorIt.next();
						AmpSector sector=SectorUtil.getAmpSector(actSector.getSectorId());
						sector.setAmpSectorId(actSector.getSectorId());
						indicator.getSectors().add(sector);
					}
                 }
                 
                 if (indicator.getSectors() == null
					|| indicator.getSectors() != null && indicator.getSectors().size() == 0) {
                     ActionMessages errors=new ActionMessages();
                     errors.add("noSector", new ActionMessage("error.aim.addIndicator.noSector"));
                     saveErrors(request, errors);
                     return mapping.findForward("forward");
                 } 
                 
               if(!IndicatorUtil.validateIndicatorName(indicator)){
                    IndicatorUtil.saveIndicator(indicator);
                    newIndForm.setAction("added");
                }
                else{
                    ActionMessages errors=new ActionMessages();
                    errors.add("title", new ActionMessage("error.aim.addIndicator.duplicateName"));
                    saveErrors(request, errors);
                    return mapping.findForward("forward");
                }                
                 
        	}
        	
/*
        	if(newIndForm.getIndType() ==2 && newIndForm.getSelectedActivityId() == null ){
        		
        		 AllMEIndicators newInd = new AllMEIndicators();

                 newInd.setCode(newIndForm.getCode());
                 newInd.setName(newIndForm.getName());
               
                 MEIndicatorsUtil.saveIndicator(newInd);
                 newIndForm.reset();
        		
        	}
        	
             if(newIndForm.getIndType() ==2 && newIndForm.getSelectedActivityId() != null ){
            	 
            	 AmpMEIndicators newInd = new AmpMEIndicators();

                 newInd.setCode(newIndForm.getCode());
                 newInd.setDescription(newIndForm.getDescription());
                 newInd.setName(newIndForm.getName());

                 MEIndicatorsUtil.saveMEIndicator(newInd, newIndForm.getSelectedActivityId(), true);
                 newIndForm.reset();
        		
        	}*/
 //       }
        	
//            switch(newIndForm.getIndType()) {
//                case 0: {
////                    if(newIndForm.getSelectedProgramId() == null) {
////                        return mapping.findForward("forward");
////                    }
//                    AmpPrgIndicator newInd = new AmpPrgIndicator();
//                    newInd.setCategory(newIndForm.getCategory());
//                    newInd.setCode(newIndForm.getCode());
//                    newInd.setCreationDate(newIndForm.getDate());
//                    newInd.setDescription(newIndForm.getDescription());
//                    newInd.setName(newIndForm.getName());
//                    newInd.setType(newIndForm.getType());
//                    newInd.setSector(newIndForm.getSelActivitySector());
//                    newInd.setIndSectores(newIndForm.getActivitySectors());
//                    newIndForm.setSelectedProgramId(new Long(1));
//                    
//                  //, newIndForm.getSelectedProgramId()
//                    ProgramUtil.saveThemeIndicators(newInd, newIndForm.getSelectedProgramId());
//
//                    newIndForm.reset();
//
//                    break;
//                }
//                case 1: {
//
//                    AllMEIndicators newInd = new AllMEIndicators();
//
//                    newInd.setCode(newIndForm.getCode());
//                    newInd.setName(newIndForm.getName());
//
//                    MEIndicatorsUtil.saveIndicator(newInd);
//                    newIndForm.reset();
//
//                    break;
//                }
//                case 2: {
//                    if(newIndForm.getSelectedActivityId() == null) {
//                        return mapping.findForward("forward");
//                    }
//
//                    AmpMEIndicators newInd = new AmpMEIndicators();
//
//                    newInd.setCode(newIndForm.getCode());
//                    newInd.setDescription(newIndForm.getDescription());
//                    newInd.setName(newIndForm.getName());
//
//                    MEIndicatorsUtil.saveMEIndicator(newInd, newIndForm.getSelectedActivityId(), true);
//                    newIndForm.reset();
//
//                    break;
//                }
//            }
//        }
        // AMP-2828 by mouhamad
        String dateFormat = FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GLOBALSETTINGS_DATEFORMAT);
        SimpleDateFormat sdf = null;
        //temporary workaround
        if (dateFormat!=null){
        	//?????
            dateFormat = dateFormat.replace("m", "M");
            sdf = new SimpleDateFormat(dateFormat);
        }else{
        	sdf= new SimpleDateFormat();
        }
		
        newIndForm.setDate(sdf.format(new Date()).toString());
      
	    return mapping.findForward("forward");
    }
}

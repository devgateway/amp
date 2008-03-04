package org.digijava.module.aim.action;

import org.apache.struts.action.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.digijava.module.aim.form.NewIndicatorForm;
import org.digijava.module.aim.form.ThemeForm;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpThemeIndicators;
import org.digijava.module.aim.helper.AllPrgIndicators;
import org.digijava.module.aim.helper.AmpPrgIndicator;
import org.digijava.module.aim.util.MEIndicatorsUtil;
import org.digijava.module.aim.helper.AllMEIndicators;
import org.digijava.module.aim.dbentity.AmpMEIndicators;
import java.util.Date;
import org.digijava.module.aim.dbentity.AmpTheme;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.apache.struts.util.LabelValueBean;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

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
        	
        if(action!=null &&
           action.equalsIgnoreCase("add") && 
             newIndForm.getName() != null &&  
             newIndForm.getCode() != null){
        	
        	if(newIndForm.getIndicatorType() == 0 || newIndForm.getIndType() == 2){
        		 AmpPrgIndicator newInd = new AmpPrgIndicator();
                 newInd.setCategory(newIndForm.getCategory());
                 newInd.setCode(newIndForm.getCode());
                 newInd.setCreationDate(newIndForm.getDate());
                 newInd.setDescription(newIndForm.getDescription());
                 newInd.setName(newIndForm.getName());
                 newInd.setType("prg/prj");
                 
                 if(newIndForm.getActivitySectors() != null &&
                		 newIndForm.getSelectedActivityId() == null  ){
                	newInd.setType("programInd");
                 }
                 
                 if(newIndForm.getSelectedActivities() != null &&
                	newIndForm.getActivitySectors() == null){
                	newInd.setType("projectInd");
                 }
                 
                 newInd.setSector(newIndForm.getSelActivitySector());
                 
                 newInd.setIndSectores(newIndForm.getActivitySectors());
	                
                 if(newIndForm.getSelectedActivityId() == null && 
	                	newIndForm.getActivitySectors() == null ){
	                	   newInd.setDefaultInd(true);
	                	   newInd.setType("global");
	                      }
	                 
	                 
                 newInd.setSelectedActivity(newIndForm.getSelectedActivities());
                 IndicatorUtil.saveIndicators(newInd);
//                 ProgramUtil.saveThemeIndicators(newInd, newIndForm.getSelectedProgramId());
                 //newIndForm.reset();
                 return mapping.findForward("added");
        		
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
        }
        	
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
        dateFormat = dateFormat.replace("m", "M");
          
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		
        newIndForm.setDate(sdf.format(new Date()).toString());
      
	    return mapping.findForward("forward");
    }
}

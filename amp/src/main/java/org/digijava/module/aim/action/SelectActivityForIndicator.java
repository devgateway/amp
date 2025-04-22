package org.digijava.module.aim.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.form.NewIndicatorForm;
import org.digijava.module.aim.util.ActivityUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * class apparently not used anymore
 *
 */
public class SelectActivityForIndicator
        extends Action{
        public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
            
            NewIndicatorForm newIndForm=(NewIndicatorForm)form;

            String edit = request.getParameter("forward");
            String id = request.getParameter("aId");
            
            if(newIndForm.getAction()!=null && newIndForm.getAction().equalsIgnoreCase("add")){
                Collection<LabelValueBean> actCol=new ArrayList<LabelValueBean>();
                Collection activities = new ArrayList();
                
                Long selInds[] = newIndForm.getSelectedActivity();
                
                if(selInds != null){
                    for(int i=0; i<selInds.length; i++){
                        AmpActivityVersion act = ActivityUtil.loadActivity(selInds[i]);
                        activities.add(act);
                    }
                }
               if(activities != null){
                   for(Iterator activity = activities.iterator(); activity.hasNext(); ){
                       AmpActivityVersion act = (AmpActivityVersion) activity.next();
          
                       LabelValueBean lbv=null;
                        
                        if(act != null) {
                            if(act.getName().length() > 35) {
                                lbv = new LabelValueBean(act.getName().substring(0, 32) + "...", act.getAmpActivityId().toString());
                            } else {
                                lbv = new LabelValueBean(act.getName(), act.getAmpActivityId().toString());
                            }
                        }
                        
                        if(lbv != null) {
                            actCol.add(lbv);
                            newIndForm.setSelectedActivities(actCol);
                            newIndForm.setAction(null);
                            newIndForm.setSelectedPrograms(null);
                            newIndForm.setSelectedProgramId(null);
                        }
                   
                   }
                       
               }
               
                if(edit != null){
                    newIndForm.setAction(null);
                    return mapping.findForward("edit");
                    
                }else{
                    newIndForm.setAction(null);
                    return mapping.findForward("add");
                }
            }
            
            if(newIndForm.getAction() != null && newIndForm.getAction().equals("remove")){
               
               for(Iterator itr = newIndForm.getSelectedActivities().iterator(); itr.hasNext();){
                  LabelValueBean beanitr = (LabelValueBean) itr.next();
                   
                  if(beanitr.getValue().equals(id)){
                    itr.remove();
                    break;
                  }
               }
               
              if(edit != null){
                newIndForm.setAction(null);
                return mapping.findForward("edit");
                
              }else{
                newIndForm.setAction(null);
                  return mapping.findForward("add");
              }
            } 
            
            
            Collection<AmpActivityVersion> actCol = ActivityUtil.getAllActivitiesList();
            if(actCol != null) {
                List<AmpActivityVersion> actList = new ArrayList<AmpActivityVersion>(actCol);
                if(actList != null && newIndForm.getKeyword() != null) {
                    for(Iterator iter = actList.iterator(); iter.hasNext(); ) {
                        AmpActivity act = (AmpActivity) iter.next();
                        if(act.getName().indexOf(newIndForm.getKeyword()) == -1) {
                            iter.remove();
                        }
                    }
                }
                Collections.sort(actList, new ActivityUtil.HelperAmpActivityNameComparator());
                newIndForm.setActivitiesCol(actList);
                newIndForm.setKeyword(null);
                
              
            }
            return mapping.findForward("forward");
    }
}

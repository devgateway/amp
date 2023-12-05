package org.digijava.module.aim.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.form.NewIndicatorForm;
import org.digijava.module.aim.util.ProgramUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class SelectProgramForIndicator
    extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        
        String edit = request.getParameter("edit");
        
        NewIndicatorForm newIndForm=(NewIndicatorForm)form;

        Long id[]=newIndForm.getSelActivitySector();
        
        if(newIndForm.getAction()!=null && newIndForm.getAction().equalsIgnoreCase("add")){
            Collection<LabelValueBean> prgCol=new ArrayList<LabelValueBean>();
            AmpTheme theme=ProgramUtil.getThemeById(newIndForm.getSelectedProgramId());

            LabelValueBean lbv=null;
            if(theme!=null){
                if(theme.getName().length() > 35) {
                    lbv = new LabelValueBean(theme.getName().substring(0, 32) + "...", theme.getAmpThemeId().toString());
                } else {
                    lbv = new LabelValueBean(theme.getName(), theme.getAmpThemeId().toString());
                }
            }
            if(lbv != null) {
                prgCol.add(lbv);
                newIndForm.setSelectedPrograms(prgCol);
                newIndForm.setAction(null);
                newIndForm.setSelectedActivities(null);
                newIndForm.setSelectedActivityId(null);
            }
            newIndForm.setAction(null);
            if(edit == null){
                return mapping.findForward("add");
            }else{
                return mapping.findForward("edit");
            }
        }

        Collection<AmpTheme> prgCol = ProgramUtil.getAllPrograms();
        if(prgCol != null) {
            List<AmpTheme> prgList = new ArrayList<AmpTheme>(prgCol);
            if(prgList != null && newIndForm.getKeyword() != null) {
                for(Iterator iter = prgList.iterator(); iter.hasNext(); ) {
                    AmpTheme prg = (AmpTheme) iter.next();
                    if(prg.getName().indexOf(newIndForm.getKeyword()) == -1) {
                        iter.remove();
                    }
                }
            }
            Collections.sort(prgList, new ProgramUtil.HelperAmpThemeNameComparator());
            newIndForm.setProgramsCol(prgList);
        }
        return mapping.findForward("forward");
    }

    public SelectProgramForIndicator() {
    }
}

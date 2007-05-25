package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForward;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.Action;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.form.EditActivityForm;
import java.util.List;
import java.util.Iterator;
import org.digijava.module.aim.dbentity.AmpTheme;

public class RemProgram extends Action
{
    private static Logger logger = Logger.getLogger(ActivityManager.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception
    {
        EditActivityForm eaform=(EditActivityForm)form;
        List prgLst=eaform.getActPrograms();
        Iterator itr=prgLst.listIterator();
        AmpTheme theme=null;
        Long prgIds[]=eaform.getSelectedPrograms();
        while(itr.hasNext()){
            theme=(AmpTheme)itr.next();
            for(int i = 0; i < prgIds.length; i++) {
                if(theme.getAmpThemeId().equals(prgIds[i])){
                    itr.remove();
                }
            }
        }
        return mapping.findForward("forward");
    }
}

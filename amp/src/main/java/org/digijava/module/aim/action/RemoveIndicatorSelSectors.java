package org.digijava.module.aim.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.NewIndicatorForm;
import org.digijava.module.aim.helper.ActivitySector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class RemoveIndicatorSelSectors extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response)
            throws java.lang.Exception {

        NewIndicatorForm eaForm = (NewIndicatorForm) form;

        Long selSectors[] = eaForm.getSelActivitySector();
        Collection prevSelSectors = eaForm.getActivitySectors();
        Collection newSectors = new ArrayList();

        Iterator itr = prevSelSectors.iterator();

        boolean flag =false;

        while (itr.hasNext()) {
            ActivitySector asec = (ActivitySector) itr.next();
            flag=false;
            for (int i = 0; i < selSectors.length; i++) {
                if (asec.getSectorId().equals(selSectors[i]) || asec.getSubsectorLevel1Id().equals(selSectors[i]) 
                        || asec.getSubsectorLevel2Id().equals(selSectors[i])) {
                    flag=true;
                    break;
                }
            }

            if(!flag){
                newSectors.add(asec);
            }
        }

        
        eaForm.setActivitySectors(newSectors);
        return mapping.findForward("forward");
    }
}

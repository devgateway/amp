package org.digijava.module.aim.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.form.ProposedProjCost;
/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class AddProposedFunding extends Action{
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws java.lang.Exception{

        EditActivityForm eaForm = (EditActivityForm) form;
        ProposedProjCost propProjCost=new ProposedProjCost();
        eaForm.setReset(false);
        if(eaForm.getProProjCost()==null){
            propProjCost.setCurrencyCode(null);
            propProjCost.setFunAmount(null);
            propProjCost.setFunDate(null);
            eaForm.setProProjCost(propProjCost);
        }

        return mapping.findForward("forward");
    }
}

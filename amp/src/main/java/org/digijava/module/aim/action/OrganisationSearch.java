package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.OrgManagerForm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

public class OrganisationSearch extends Action {
    
    private static Logger logger = Logger.getLogger(OrganisationSearch.class);
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response)
            throws java.lang.Exception {
        
        logger.debug("In organisation manager action");
        
        OrgManagerForm eaForm = (OrgManagerForm) form;

        if (request.getParameter("orgSelReset") != null
                && request.getParameter("orgSelReset").equals("false")) {
            eaForm.setOrgSelReset(false);
            //////System.out.println("Inside IF");  //
        } else {
            eaForm.setOrgSelReset(true);
            eaForm.setPagedCol(null);
            eaForm.reset(mapping, request);
            //////System.out.println("Inside ELSE");  //
        }

        int page;
        if (request.getParameter("page") == null) {
            page = 1;
        } else {
            try {
                page = Integer.parseInt(request.getParameter("page"));
            } catch (NumberFormatException e) {
                page = 1;
            }
        }
        //////System.out.println("page = " + page); //
        if (eaForm.getNumResults() == 0 || eaForm.isOrgSelReset()) {
            eaForm.setTempNumResults(10);
            //eaForm.setOrgTypes(DbUtil.getAllOrgTypes());
            //if (eaForm.getAlphaPages() != null)
                //eaForm.setAlphaPages(null);
        } else {
            int stIndex = ((page - 1) * eaForm.getNumResults()) + 1;
            int edIndex = page * eaForm.getNumResults();
            
            if (eaForm.isStartAlphaFlag()) {
                if (edIndex > eaForm.getCols().size()) {
                    edIndex = eaForm.getCols().size();
                }
            }
            else {
                if (edIndex > eaForm.getColsAlpha().size()) {
                    edIndex = eaForm.getColsAlpha().size();
                }
            }

            Vector vect = new Vector();
            
            if (eaForm.isStartAlphaFlag())
                vect.addAll(eaForm.getCols());
            else
                vect.addAll(eaForm.getColsAlpha());

            Collection tempCol = new ArrayList();

            for (int i = (stIndex - 1); i < edIndex; i++) {
                tempCol.add(vect.get(i));
            }

            eaForm.setPagedCol(tempCol);
            eaForm.setCurrentPage(page);
        }

        return mapping.findForward("forward");
    }
}

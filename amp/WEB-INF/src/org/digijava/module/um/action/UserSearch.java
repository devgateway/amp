package org.digijava.module.um.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.action.OrganisationSearch;
import org.digijava.module.um.form.ViewAllUsersForm;

public class UserSearch extends Action {
    
    private static Logger logger = Logger.getLogger(UserSearch.class);
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        HttpSession session = request.getSession();
        if (session.getAttribute("ampAdmin") == null) {
            return mapping.findForward("index");
        } else {
                    String str = (String)session.getAttribute("ampAdmin");
                    if (str.equals("no")) {
                              return mapping.findForward("index");
                    }
        }
         
        logger.debug("In User manager action");
                
         ViewAllUsersForm vwForm = (ViewAllUsersForm) form;
         
         int page = 0;

            if (request.getParameter("page") == null) {
                page = 1;
            } else {
                page = Integer.parseInt(request.getParameter("page"));
            }
            
            if (vwForm.getNumResults() == 0 ) {
                vwForm.setTempNumResults(10);               
            } 
            if (page==-1){
                page = vwForm.getUsers().size()%vwForm.getNumResults()==0 ? vwForm.getUsers().size()/vwForm.getNumResults() : vwForm.getUsers().size()/vwForm.getNumResults()+1;
            }
            int stIndex = ((page - 1) * vwForm.getNumResults()) + 1;
            int edIndex = page * vwForm.getNumResults();
            
            if (vwForm.isSelectedNoLetter()) {
                if (edIndex > vwForm.getUsers().size()) {
                    edIndex = vwForm.getUsers().size();
                }
            }
            else {
                if (edIndex > vwForm.getAlphaUsers().size()) {
                    edIndex = vwForm.getAlphaUsers().size();
                }
            }

            Vector vect = new Vector();
            
            if (vwForm.isSelectedNoLetter())
                vect.addAll(vwForm.getUsers());
            else
                vect.addAll(vwForm.getAlphaUsers());

            Collection tempCol = new ArrayList();

            for (int i = (stIndex - 1); i < edIndex; i++) {
                tempCol.add(vect.get(i));
            }

            vwForm.setPagedUsers(tempCol);
            vwForm.setCurrentPage(new Integer(page));
            
        return mapping.findForward("forward");
    }

}

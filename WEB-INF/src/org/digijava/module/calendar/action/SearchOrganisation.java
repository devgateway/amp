package org.digijava.module.calendar.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.calendar.form.EditActivityForm;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class SearchOrganisation extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response)
            throws java.lang.Exception {

        EditActivityForm eaForm = (EditActivityForm) form;
        eaForm.setReset(false);
        eaForm.setOrgPopupReset(false);

        String alpha = request.getParameter("alpha");
        Collection col = null;
        Collection colAlpha = null;

        if (alpha == null || alpha.trim().length() == 0) {
            col = new ArrayList();
            eaForm.setNumResults(eaForm.getTempNumResults());

            if (!eaForm.getAmpOrgTypeId().equals(new Long(-1))) {
                if (eaForm.getKeyword().trim().length() != 0) {
                    // serach for organisations based on the keyword and the
                    // organisation type
                    col = DbUtil.searchForOrganisation(eaForm.getKeyword().trim(),
                            eaForm.getAmpOrgTypeId());
                } else {
                    // search for organisations based on organisation type only
                    col = DbUtil.searchForOrganisationByType(eaForm.getAmpOrgTypeId());
                }
            } else if (eaForm.getKeyword().trim().length() != 0) {
                // search based on the given keyword only.
                col = DbUtil.searchForOrganisation(eaForm.getKeyword().trim());
            } else {
                // get all organisations since keyword field is blank and org type field has 'ALL'.
                col = DbUtil.getAmpOrganisations();
            }

            if (col != null && col.size() > 0) {
                List temp = (List) col;
                Collections.sort(temp);
                col = (Collection) temp;

                if (eaForm.getCurrentAlpha() != null) {
                    eaForm.setCurrentAlpha(null);
                    //eaForm.setStartAlphaFlag(true);
                }
                eaForm.setStartAlphaFlag(true);

                String[] alphaArray = new String[26];
                int i = 0;
                for(char c = 'A'; c <= 'Z'; c++) {
                    Iterator itr = col.iterator();
                    while(itr.hasNext()) {
                        AmpOrganisation org = (AmpOrganisation) itr.next();
                        if (org.getName().toUpperCase().indexOf(c) == 0) {
                            alphaArray[i++] = String.valueOf(c);
                            break;
                        }
                    }
                }
                eaForm.setAlphaPages(alphaArray);
            } else {
                eaForm.setAlphaPages(null);
            }

        } else {
            col = eaForm.getCols();
            eaForm.setCurrentAlpha(alpha);
            if (!alpha.equals("viewAll")) {
                eaForm.setStartAlphaFlag(false);
                colAlpha = new ArrayList();
                Iterator itr = col.iterator();
                while(itr.hasNext()) {
                    AmpOrganisation org = (AmpOrganisation) itr.next();
                    if (org.getName().toUpperCase().startsWith(alpha)) {
                        colAlpha.add(org);
                    }
                }
                eaForm.setColsAlpha(colAlpha);
            }
            else
                eaForm.setStartAlphaFlag(true);
        }

        int stIndex = 1;
        int edIndex = eaForm.getNumResults();
        Vector vect = new Vector();
        int numPages;

        if (alpha == null || alpha.trim().length() == 0 || alpha.equals("viewAll")) {
            if (edIndex > col.size()) {
                edIndex = col.size();
            }
            vect.addAll(col);
            numPages = col.size() / eaForm.getNumResults();
            numPages += (col.size() % eaForm.getNumResults() != 0) ? 1 : 0;
        } else {
            if (edIndex > colAlpha.size()) {
                edIndex = colAlpha.size();
            }
            vect.addAll(colAlpha);
            numPages = colAlpha.size() / eaForm.getNumResults();
            numPages += (colAlpha.size() % eaForm.getNumResults() != 0) ? 1 : 0;
        }

        Collection tempCol = new ArrayList();
        for (int i = (stIndex - 1); i < edIndex; i++) {
            tempCol.add(vect.get(i));
        }

        Collection pages = null;

        if (numPages > 1) {
            pages = new ArrayList();
            for (int i = 0; i < numPages; i++) {
                Integer pageNum = new Integer(i + 1);
                pages.add(pageNum);
            }
        }
        eaForm.setCols(col);
        eaForm.setPagedCol(tempCol);
        eaForm.setPages(pages);
        eaForm.setCurrentPage(new Integer(1));

        return mapping.findForward("forward");
    }
}


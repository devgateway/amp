
package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpAhsurvey;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.util.DbUtil;
@Deprecated
public class SelectOrganisationForAhsurvey extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) {

        EditActivityForm eaForm = (EditActivityForm) form;
        eaForm.setReset(false);
        eaForm.setOrgPopupReset(false);

        if (eaForm.getSvAction().equals("sel")) {
            AmpAhsurvey ahs = eaForm.getSurvey().getAhsurvey();
            AmpOrganisation selOrg = DbUtil.getOrganisation(eaForm.getSurveyOrgId());
            ahs.setPointOfDeliveryDonor(selOrg);
            eaForm.getSurvey().setAhsurvey(ahs);

            eaForm.setSvAction(null);
            return mapping.findForward("finish");
        }

        if(eaForm.getOrgTypes()==null){
            Collection otCol=DbUtil.getAllOrgTypes();
            for (Iterator otIter = otCol.iterator(); otIter.hasNext(); ) {
                AmpOrgType ot = (AmpOrgType) otIter.next();
                if(!ot.getOrgTypeCode().equalsIgnoreCase("bil") && !ot.getOrgTypeCode().equalsIgnoreCase("mul")){
                    otIter.remove();
                }
            }
            eaForm.setOrgTypes(otCol);
        }
        if(eaForm.getTempNumResults()==0){
            eaForm.setTempNumResults(10);
        }

        String alpha = request.getParameter("alpha");
        Collection col = null;
        Collection colAlpha = null;

        if (alpha == null || alpha.trim().length() == 0) {
            col = new ArrayList();
            eaForm.setNumResults(eaForm.getTempNumResults());

            if (eaForm.getAmpOrgTypeId()!=null && !eaForm.getAmpOrgTypeId().equals(new Long( -1))) {
                if (eaForm.getKeyword().trim().length() != 0) {
                    col = DbUtil.searchForOrganisation(eaForm.getKeyword().trim(),eaForm.getAmpOrgTypeId());
                } else {
                    col = DbUtil.searchForOrganisationByType(eaForm.getAmpOrgTypeId());
                }
            } else if (eaForm.getKeyword()!=null && eaForm.getKeyword().trim().length() != 0) {
                col = DbUtil.searchForOrganisation(eaForm.getKeyword().trim());
            } else {
                col = DbUtil.getBilMulOrganisations();
            }

            if (col != null && col.size() > 0) {
                List temp = (List) col;
                Collections.sort(temp);
                col = (Collection) temp;

                if (eaForm.getCurrentAlpha() != null) {
                    eaForm.setCurrentAlpha(null);
                }
                eaForm.setStartAlphaFlag(true);

                String[] alphaArray = new String[26];
                int i = 0;
                for (char c = 'A'; c <= 'Z'; c++) {
                    Iterator itr = col.iterator();
                    while (itr.hasNext()) {
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
                while (itr.hasNext()) {
                    AmpOrganisation org = (AmpOrganisation) itr.next();
                    if (org.getName().toUpperCase().startsWith(alpha)) {
                        colAlpha.add(org);
                    }
                }
                eaForm.setColsAlpha(colAlpha);
            } else
                eaForm.setStartAlphaFlag(true);
        }

        Collection newCol = new ArrayList();
        newCol = col;

        if(eaForm.getSelectedOrganisationFromPages()==null || (eaForm.getSelectedOrganisationFromPages().equals(1))){
            eaForm.setSelectedOrganisationFromPages(1);
        }
        int stIndex = eaForm.getSelectedOrganisationFromPages();
        int edIndex = eaForm.getNumResults();
        Vector vect = new Vector();
        int numPages;

        if (alpha == null || alpha.trim().length() == 0 || alpha.equals("viewAll")) {
            if (edIndex > newCol.size()) {
                edIndex = newCol.size();
            }
            vect.addAll(newCol);
            numPages = newCol.size() / eaForm.getNumResults();
            numPages += (newCol.size() % eaForm.getNumResults() != 0) ? 1 : 0;
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

        eaForm.setCols(newCol);
        eaForm.setPagedCol(tempCol);
        eaForm.setPages(pages);
        eaForm.setCurrentPage(stIndex);

        return mapping.findForward("forward");
    }

    private boolean isEmpty(String str) {
        if (str != null && !str.equals("")) {
            return false;
        }
        return true;
    }
}


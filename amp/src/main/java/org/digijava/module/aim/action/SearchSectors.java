package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.util.SectorUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

public class SearchSectors
    extends Action {

  private static Logger logger = Logger.getLogger(SearchSectors.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form,
                               javax.servlet.http.HttpServletRequest request,
                               javax.servlet.http.HttpServletResponse response) throws
      java.lang.Exception {

    SelectSectorForm ssForm = (SelectSectorForm) form;
    //ssForm.setReset(false);
    //eaForm.setOrgPopupReset(false);

    ssForm.setNumResults(ssForm.getTempNumResults());

    if (ssForm.getKeyword() != null)
      request.getSession().setAttribute("keywordForSectors", ssForm.getKeyword());
    else
      ssForm.setKeyword( (String) request.getSession().getAttribute(
          "keywordForSectors"));

    Collection col = new ArrayList();
    ssForm.setNumResults(ssForm.getTempNumResults());
    int page = 0;

    if (request.getParameter("page") == null) {
      page = 1;
    }
    else {
      page = Integer.parseInt(request.getParameter("page"));
    }

    ssForm.setCurrentPage(new Integer(page));

    if (ssForm.getKeyword() != null)
      if (ssForm.getKeyword().trim().length() != 0) {
        // search based on the given keyword only.

        col = SectorUtil.searchForSector(ssForm.getKeyword().trim(),ssForm.getSectorScheme());

        int stIndex = 1;
        int edIndex = ssForm.getNumResults();

        if (ssForm.getNumResults() == 0 || ssForm.isSectorReset() == true) {
          ssForm.setTempNumResults(10);
        }
        else {
          stIndex = ( (page - 1) * ssForm.getNumResults()) + 1;
          edIndex = page * ssForm.getNumResults();
        }

        Vector vect = new Vector();
        int numPages = 0;

        if (edIndex > col.size()) {
          edIndex = col.size();
        }
        vect.addAll(col);
        if (ssForm.getNumResults() > 0) {
          numPages = col.size() / ssForm.getNumResults();
          numPages += (col.size() % ssForm.getNumResults() != 0) ? 1 : 0;
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

        ssForm.setSearchedSectors(col);

        //  ssForm.setCols(col);
        ssForm.setPagedCol(tempCol);
        ssForm.setPages(pages);
        ssForm.setCurrentPage(new Integer(page));

//      eaForm.setSearchLocs(col);
//      eaForm.setPagedCol(tempCol);
//      eaForm.setPages(pages);
//      eaForm.setCurrentPage(new Integer(1));

//      eaForm.setSearchLocs(LocationUtil.searchForLocation(eaForm.getKeyword().trim(),implvl));

      }
      else {
        //////System.out.println("no input in keyword field.....");
      }

    return mapping.findForward("forward");
  }
}

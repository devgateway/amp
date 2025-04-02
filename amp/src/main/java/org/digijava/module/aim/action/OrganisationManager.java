    package org.digijava.module.aim.action;

import java.text.Collator;
import java.util.*;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.form.OrgManagerForm;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.calendar.util.AmpUtil;

    public class OrganisationManager extends Action {

  private static Logger logger = Logger.getLogger(OrganisationManager.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form,
                               javax.servlet.http.HttpServletRequest request,
                               javax.servlet.http.HttpServletResponse response) throws
      java.lang.Exception {

    HttpSession session = request.getSession();

    TeamMember tm = (TeamMember) session.getAttribute("currentMember");
    boolean isAdmin=false;
    boolean plainTeamMember = tm==null||!tm.getTeamHead();
    if (session.getAttribute("ampAdmin") == null) {
        if(plainTeamMember){
             return mapping.findForward("index");
        }
    }
    else {
      String str = (String) session.getAttribute("ampAdmin");
      if (str.equals("no")) {
          if(plainTeamMember){
              return mapping.findForward("index");
          }
      }
      else{
          isAdmin=true;
      }
    }

    logger.debug("In organisation manager action");

    OrgManagerForm eaForm = (OrgManagerForm) form;
    eaForm.setAdminSide(isAdmin);
    if (request.getParameter("orgSelReset") != null
        && request.getParameter("orgSelReset").equals("false")) {
      eaForm.setOrgSelReset(false);
    }
    else {
      eaForm.setOrgSelReset(true);
      eaForm.setPagedCol(null);
      eaForm.reset(mapping, request);
    }

    eaForm.setOrgSelReset(false); //
    eaForm.setReset(false);
    eaForm.setOrgPopupReset(false);

    Collection<AmpOrganisation> col = null;
    Collection colAlpha = null;
    Boolean newOrganizationAdded = eaForm.getAdded();

    if (newOrganizationAdded != null && newOrganizationAdded) {
      eaForm.setAdded(false);
      eaForm.setAlpha(null);

    }
    
    //AMP-5453
    if ((eaForm.getAmpOrgTypeId() != null) && (!eaForm.getAmpOrgTypeId().equals(eaForm.getOldAmpOrgTypeId()))){
        if (eaForm.getOldAmpOrgTypeId() != null)
            eaForm.setAlpha("viewAll");
        eaForm.setOldAmpOrgTypeId(eaForm.getAmpOrgTypeId());
    }
    
    String alpha = eaForm.getAlpha(); //request.getParameter("alpha");
    if (StringUtils.isBlank(alpha)) {
        eaForm.setOrgTypes(DbUtil.getAllOrgTypes()); 
    }
    eaForm.setNumResults(eaForm.getTempNumResults());
    col = new ArrayList<AmpOrganisation>();
    if (eaForm.getAlphaPages() != null) //
        eaForm.setAlphaPages(null); //

      if (eaForm.getAmpOrgTypeId() != null &&
          !eaForm.getAmpOrgTypeId().equals(new Long( -1))) {
        if (StringUtils.isNotEmpty(eaForm.getKeyword())) {
          // serach for organisations based on the keyword and the organisation type
          col = DbUtil.searchForOrganisation(eaForm.getKeyword().trim(),
                                             eaForm.getAmpOrgTypeId());
        }
        else {
          // search for organisations based on organisation type only
          col = DbUtil.searchForOrganisationByType(eaForm.getAmpOrgTypeId());
        }
      }
      else if (StringUtils.isNotBlank(eaForm.getKeyword())) {
        // search based on the given keyword only.
        col = DbUtil.searchForOrganisation(eaForm.getKeyword().trim());
      }
      else {
        // get all organisations since keyword field is blank and org type field has 'ALL'.
        col = DbUtil.getAmpOrganisations();
      }
      //aq unda chavamato sortBy !!!!!!!!!!!!!!!!!!!!!!!!!!!
      if(request.getParameter("sortBy")!=null) {
          eaForm.setSortBy(request.getParameter("sortBy"));  
      }      
      if(eaForm.getSortBy()!=null){
          if(eaForm.getSortBy().equalsIgnoreCase("nameAscending")){
              Collections.sort((List)col, new DbUtil.HelperAmpOrganisationNameComparator()) ;
          }else if (eaForm.getSortBy().equalsIgnoreCase("nameDescending")){ 
              Collections.sort((List)col, new DbUtil.HelperAmpOrganisationNameComparator()) ;
              Collections.reverse((List)col);
          }else if(eaForm.getSortBy().equalsIgnoreCase("acronymAscending")) {
              Collections.sort((List)col, new DbUtil.HelperAmpOrganisatonAcronymComparator()) ;
          }else if(eaForm.getSortBy().equalsIgnoreCase("acronymDescending")){
              Collections.sort((List)col, new DbUtil.HelperAmpOrganisatonAcronymComparator()) ;
              Collections.reverse((List)col);
          }  else if (eaForm.getSortBy().equalsIgnoreCase("typeAscending")) {
              Collections.sort((List)col, new DbUtil.HelperAmpOrganisationTypeComparator()) ;
          }else if(eaForm.getSortBy().equalsIgnoreCase("typeDescending")){
              Collections.sort((List)col, new DbUtil.HelperAmpOrganisationTypeComparator()) ;
              Collections.reverse((List)col);
          }  else if(eaForm.getSortBy().equalsIgnoreCase("groupAscending")) {
              Collections.sort((List)col, new DbUtil.HelperAmpOrganisationGroupComparator()) ;
          }else if (eaForm.getSortBy().equalsIgnoreCase("groupDescending")) {
              Collections.sort((List)col, new DbUtil.HelperAmpOrganisationGroupComparator()) ;
              Collections.reverse((List)col);
          }
      } else {
          Collections.sort((List)col, new DbUtil.HelperAmpOrganisationNameComparator()) ;  //by default sort by name
      }
      
      
      if (col != null && col.size() > 0) {
//          List temp = (List) col;
//          Collections.sort(temp);
//          col = (Collection) temp;

          if(alpha == null || alpha.trim().length() == 0){
              if (eaForm.getCurrentAlpha() != null) {
                  eaForm.setCurrentAlpha(null);
                } 
          }else {
              eaForm.setCurrentAlpha(alpha);
          }
          
          eaForm.setStartAlphaFlag(true);

          collectAlphaArray(eaForm, col);
        }
        else {
          eaForm.setAlphaPages(null);
        }
    
      if (alpha!=null && !alpha.equals("viewAll")) {
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
        }
        else
          eaForm.setStartAlphaFlag(true);
      int stIndex = 1;
      int edIndex = eaForm.getNumResults();

      eaForm.setPagesToShow(10);
      //If ALL was selected in pagination dropdown
      if (edIndex < 0) {
        edIndex = col.size();
      }

      Vector vect = new Vector();
      int numPages;

      if (alpha == null || alpha.trim().length() == 0 || alpha.equals("viewAll")) {
        if (edIndex > col.size()) {
          edIndex = col.size();
        }
        vect.addAll(col);
        numPages = col.size() / eaForm.getNumResults();
        numPages += (col.size() % eaForm.getNumResults() != 0) ? 1 : 0;
      }
      else {
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

    private void collectAlphaArray(OrgManagerForm eaForm, Collection<AmpOrganisation> col) {
        SortedSet<String> chars = new TreeSet<String>(AmpUtil.CharUnicodeComparator);
        SortedSet<String> digits = new TreeSet<String>(AmpUtil.CharUnicodeComparator);
        for (AmpOrganisation ampOrganisation : col) {
            if (ampOrganisation.getName() != null && ampOrganisation.getName().length() > 0) {
                Character firstLetter = ampOrganisation.getName().toUpperCase().charAt(0);
                if (Character.isLetter(firstLetter)) {
                    chars.add(String.valueOf(firstLetter));
                } else if ( Character.isDigit(firstLetter)) {
                    digits.add(String.valueOf(firstLetter));
                }
            }
        }
        eaForm.setAlphaPages(chars.toArray(new String[0]));
        eaForm.setDigitPages(digits.toArray(new String[0]));
    }

}

package org.digijava.module.aim.action ;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.form.OrgGroupManagerForm;
import org.digijava.module.aim.util.DbUtil;

public class OrgGroupManager extends Action {

          private static Logger logger = Logger.getLogger(OrgGroupManager.class);

          public ActionForward execute(ActionMapping mapping,
                                ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response) throws java.lang.Exception {

                     HttpSession session = request.getSession();
                     if (session.getAttribute("ampAdmin") == null) {
                                return mapping.findForward("index");
                     } else {
                                String str = (String)session.getAttribute("ampAdmin");
                                if (str.equals("no")) {
                                          return mapping.findForward("index");
                                }
                     }                   
                     
                     int NUM_RECORDS =10;

                     Collection<AmpOrgGroup> org = new ArrayList<AmpOrgGroup>();
                     OrgGroupManagerForm orgForm = (OrgGroupManagerForm) form;
                     int page = 0;
                     
                     if (request.getParameter("orgSelReset") != null && request.getParameter("orgSelReset").equals("false")) {
                        orgForm.setOrgSelReset(false);
                     }else {
                        orgForm.setOrgSelReset(true);                       
                        orgForm.reset(mapping, request);
                     }
                     if(request.getParameter("resetAlpha") != null && request.getParameter("resetAlpha").equals("true")){
                        orgForm.setAlpha(null);
                     }
                     
                     if (orgForm.getTempNumResults() !=0){
                         orgForm.setNumResults(orgForm.getTempNumResults()); 
                     }
                     
                     if (orgForm.getNumResults() !=0){
                         NUM_RECORDS = orgForm.getNumResults();
                     }else{
                         NUM_RECORDS = 10;
                     }
                
                     logger.debug("In organisation group manager action");
                     
                     if (request.getParameter("page") == null) {
                                page = 1;
                     } else {
                                /*
                                 * check whether the page is a valid integer
                                 */
                                page = Integer.parseInt(request.getParameter("page"));
                     }
                     
                      
                     Collection<AmpOrgGroup> ampOrg = null;
                     if (ampOrg == null) {
                         if (orgForm.getAmpOrgTypeId() != null &&
                                  !orgForm.getAmpOrgTypeId().equals(new Long( -1))) {
                                if (orgForm.getKeyword().trim().length() != 0) {
                                  // serach for organisations based on the keyword and the
                                  // organisation type
                                  ampOrg = DbUtil.searchForOrganisationGroup(orgForm.getKeyword().trim(),orgForm.getAmpOrgTypeId());
                                }
                                else {
                                  // search for organisations based on organisation type only
                                    ampOrg = DbUtil.searchForOrganisationGroupByType(orgForm.getAmpOrgTypeId());
                                }
                              }
                              else if (orgForm.getKeyword() != null &&
                                      orgForm.getKeyword().trim().length() != 0) {
                                // search based on the given keyword only.
                                  ampOrg = DbUtil.searchForOrganisationGroup(orgForm.getKeyword().trim());
                              }
                              else {
                                // get all organisations since keyword field is blank and org type field has 'ALL'.
                                  ampOrg = DbUtil.getAllOrganisationGroup();
                              }
                        ampOrg = filterDeletedOrgGroups(ampOrg);
                        session.setAttribute("ampOrgGrp",ampOrg);
                     }
                     // sorting!!!
                     if(request.getParameter("sortBy")!=null){
                            orgForm.setSortBy(request.getParameter("sortBy"));
                        }                 
                        
                        if (orgForm.getSortBy()!=null){
                            if(orgForm.getSortBy().equals("nameAscending")){
                                   Collections.sort((List)ampOrg, new DbUtil.HelperAmpOrgGroupNameComparator());
                               }else if (orgForm.getSortBy().equals("nameDescending")) {
                                   Collections.sort((List)ampOrg, new DbUtil.HelperAmpOrgGroupNameComparator());
                                   Collections.reverse((List)ampOrg);
                               } else if(orgForm.getSortBy().equals("codeAscending")){
                                   Collections.sort((List)ampOrg, new DbUtil.HelperAmpOrgGroupCodeComparator());
                               } else if (orgForm.getSortBy().equals("codeDescending")){
                                   Collections.sort((List)ampOrg, new DbUtil.HelperAmpOrgGroupCodeComparator());
                                   Collections.reverse((List)ampOrg);
                               } else if (orgForm.getSortBy().equals("typeAscending")) {
                                   Collections.sort((List)ampOrg, new DbUtil.HelperAmpOrgGroupTypeComparator());
                               } else if(orgForm.getSortBy().equals("typeDescending")){
                                   Collections.sort((List)ampOrg, new DbUtil.HelperAmpOrgGroupTypeComparator());
                                   Collections.reverse((List)ampOrg);
                               }
                        }else {
                            Collections.sort((List)ampOrg, new DbUtil.HelperAmpOrgGroupNameComparator());
                        }
                        orgForm.setOrgTypes(DbUtil.getAllOrgTypes()); 
                        //pagination
                        String alpha = orgForm.getAlpha();
                        Collection<AmpOrgGroup> orgsForCurrentAlpha=null;
                        if (ampOrg!=null && ampOrg.size()>0) {                           
                            if(alpha == null || alpha.trim().length() == 0){
                              if (orgForm.getCurrentAlpha() != null) {
                                orgForm.setCurrentAlpha(null);
                                  } 
                            }else {
                                orgForm.setCurrentAlpha(alpha);
                            } 
                            String[] alphaArray = new String[26];
                            int i = 0;
                            for (char c = 'A'; c <= 'Z'; c++) {
                              Iterator<AmpOrgGroup> itr = ampOrg.iterator();
                              while (itr.hasNext()) {
                                AmpOrgGroup orgGr = itr.next();
                                if (orgGr.getOrgGrpName().toUpperCase().indexOf(c) == 0) {
                                  alphaArray[i++] = String.valueOf(c);
                                  break;
                                }
                              }
                            }
                            orgForm.setAlphaPages(alphaArray);
                        }else {
                            orgForm.setAlphaPages(null);
                        }
                        
                        if (alpha!=null && !alpha.equalsIgnoreCase("view all")){
                            orgsForCurrentAlpha=new ArrayList<AmpOrgGroup>();
                            if(ampOrg!=null){
                                Iterator<AmpOrgGroup> it=ampOrg.iterator();
                                while(it.hasNext()) {
                                    AmpOrgGroup orgGroup=it.next();
                                    if(orgGroup.getOrgGrpName().toUpperCase().startsWith(alpha)){
                                        orgsForCurrentAlpha.add(orgGroup);
                                    }
                                }
                            }
                            orgForm.setOrgsForCurrentAlpha(orgsForCurrentAlpha);
                        }                       
                        
                        if (NUM_RECORDS ==-1){
                            NUM_RECORDS = ampOrg.size() > 0 ? ampOrg.size() : 100;
                        }
                        int stIndex = ((page - 1) * NUM_RECORDS) + 1;
                        int edIndex=page * NUM_RECORDS ;
                        int numPages;
                        Vector<AmpOrgGroup> vect = new Vector<AmpOrgGroup>();
                        
                        if (alpha == null || alpha.trim().length() == 0 || alpha.equals("viewAll")){
                             if (edIndex > ampOrg.size()) {
                                    edIndex = ampOrg.size();
                             }
                            vect.addAll(ampOrg);
//                          for (AmpOrgGroup item : ampOrg) {
//                              if (!Boolean.TRUE.equals(item.getDeleted()))
//                                  vect.add(item);
//                          }
                             numPages = ampOrg.size() / NUM_RECORDS;
                             numPages += (ampOrg.size() % NUM_RECORDS != 0) ? 1 : 0;
                        }else {
                            if (edIndex > orgsForCurrentAlpha.size()) {
                                edIndex = orgsForCurrentAlpha.size();
                            }
                            vect.addAll(orgsForCurrentAlpha);
//                          for (AmpOrgGroup item : orgsForCurrentAlpha) {
//                              if (!Boolean.TRUE.equals(item.getDeleted()))
//                                  vect.add(item);
//                          }
                            numPages = orgsForCurrentAlpha.size() / NUM_RECORDS;
                             numPages += (orgsForCurrentAlpha.size() % NUM_RECORDS != 0) ? 1 : 0;
                        }                   

                     /*
                      * check whether the numPages is less than the page . if yes return error.
                      */
                     for (int i = (stIndex-1); i < edIndex; i++) {
                         if(vect.get(i)!=null){
//                           if (!Boolean.TRUE.equals(vect.get(i).getDeleted()))
                             org.add(vect.get(i));
                         }                      
                     }
                     
                     Collection pages = null;
                     
                     if (numPages > 1) {
                                pages = new ArrayList();
                                for (int i = 0;i < numPages;i ++) {
                                          Integer pageNum = new Integer(i+1);
                                          pages.add(pageNum);
                                }
                     }
                     
                     orgForm.setOrganisation(org);
                     orgForm.setPages(pages);
                     orgForm.setCurrentPage(new Integer(page));
                    
                     logger.debug("Organisation Group manager returning");
                     return mapping.findForward("forward");
          }
          /**
           * generates new list of amp org groups based on passed parameter, including only those that are not deleted
           * @param ampOrg
           * @return
           */
        private Collection<AmpOrgGroup> filterDeletedOrgGroups(
                Collection<AmpOrgGroup> ampOrg) {
            Collection<AmpOrgGroup> filtered = new ArrayList<AmpOrgGroup>();
            for (AmpOrgGroup item : ampOrg) {
                if (!Boolean.TRUE.equals(item.getDeleted()))
                    filtered.add(item);
            }
            return filtered;
        }
}

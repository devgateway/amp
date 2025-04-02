package org.digijava.module.aim.action ;

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
import org.digijava.module.aim.form.SearchSectorForm;
import org.digijava.module.aim.util.SectorUtil;


public class SearchSector extends Action {

          private static Logger logger = Logger.getLogger(SearchSector.class);

          public ActionForward execute(ActionMapping mapping,
                                ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response) throws java.lang.Exception {

                     SearchSectorForm searchForm = (SearchSectorForm) form;

                     logger.debug("In search sector");
                                
                     if (searchForm.getSearchKey() != null && searchForm.getSearchOn() != null) {
                                Collection col = null;
                                if (searchForm.getSearchOn().equals("sectorCode")) {
                                          col = SectorUtil.searchSectorCode(searchForm.getSearchKey());
                                } else {
                                          col = SectorUtil.searchSectorName(searchForm.getSearchKey());
                                }

                                setSearchResults(request,col);
                                col = getPaginatedResults(request,1);   
                                Collection pages = getPages(request);
                                searchForm.setResults(col);
                                searchForm.setPages(pages);
                                logger.debug("11");
                                return mapping.findForward("searchResults");

                     } else if (request.getParameter("page") != null) {
                                int page = Integer.parseInt(request.getParameter("page"));
                                logger.debug("12");
                                Collection col = getPaginatedResults(request,page);
                                logger.debug("13");
                                Collection pages = getPages(request);
                                logger.debug("14");
                                searchForm.setResults(col);
                                logger.debug("15");
                                searchForm.setPages(pages);
                                logger.debug("16");
                                return mapping.findForward("searchResults");
                     } else {
                                searchForm.setSearchOn("sectorName");
                     }

                     return mapping.findForward("forward");

          }

          private void setSearchResults(HttpServletRequest request,Collection col) {
                     HttpSession sess = request.getSession();
                     sess.setAttribute("serSectResults",col);
          }

          private Collection getPaginatedResults(HttpServletRequest request,int page) {
                     
                     final int NUM_RECORDS = 10;
                     
                     Collection col = new ArrayList();
                     HttpSession session = request.getSession();
                     Collection serResults = (Collection)session.getAttribute("serSectResults");
                    
                     int stIndex = ((page - 1) * NUM_RECORDS) + 1;
                     int edIndex = page * NUM_RECORDS;
                     if (edIndex > serResults.size()) {
                                edIndex = serResults.size();
                     }

                     Vector vect = new Vector();
                     vect.addAll(serResults);
                     
                     for (int i = (stIndex-1);i < edIndex;i ++) {
                                col.add(vect.get(i));
                     }

                     return col;
          }

          private Collection getPages(HttpServletRequest request) {
                     HttpSession session = request.getSession();
                     Collection serResults = (Collection)session.getAttribute("serSectResults");
                     final int NUM_RECORDS = 10;
                     
                     int numPages = serResults.size() / NUM_RECORDS;
                     numPages += (serResults.size() % NUM_RECORDS != 0) ? 1 : 0;
                     
                     Collection pages = null;

                     if (numPages > 1) {
                                pages = new ArrayList();
                                for (int i = 0;i < numPages;i ++) {
                                          Integer pageNum = new Integer(i+1);
                                          pages.add(pageNum);
                                }
                     }

                     return pages;
          }
}

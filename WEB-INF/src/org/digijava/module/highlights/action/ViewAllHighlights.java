/*
 *   ViewAllHighlights.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Oct 11, 2003
 * 	 CVS-ID: $Id$
 *
 *   This file is part of DiGi project (www.digijava.org).
 *   DiGi is a multi-site portal system written in Java/J2EE.
 *
 *   Confidential and Proprietary, Subject to the Non-Disclosure
 *   Agreement, Version 1.0, between the Development Gateway
 *   Foundation, Inc and the Recipient -- Copyright 2001-2004 Development
 *   Gateway Foundation, Inc.
 *
 *   Unauthorized Disclosure Prohibited.
 *
 *************************************************************************/

package org.digijava.module.highlights.action;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.user.UserInfo;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.common.action.PaginationAction;
import org.digijava.module.highlights.dbentity.Highlight;
import org.digijava.module.highlights.form.HighlightItemForm;
import org.digijava.module.highlights.util.DbUtil;

/**
 * <p> Action displays all(i.e. archived and active) Highlights list, which can be ordered by title,authorFirstName,creationDate properties in ascending or descending direction:</p>
 * when ordered in concrete direction (by any of title,authorFirstName,creationDate properties), next invoke of the Action orderes the list in opposite diresction by same property
 */

public class ViewAllHighlights
    extends PaginationAction {

    private static Logger logger = Logger.getLogger(ViewAllHighlights.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        HighlightItemForm formBean = (HighlightItemForm) form;
        List dbHighlightsList = null;
        List highlightsList = new ArrayList();

        ModuleInstance moduleInstance = DgUtil.getRealModuleInstance(request);

        String orderBy = new String("");
        String sort = request.getParameter("sort");
        String order = request.getParameter("order");
        boolean orderDirection = true; //desc

        if (sort != null && order != null) {
            if (order.equals("asc")) {
                orderDirection = false;
            }
            //
            if (sort.equals("author")) {
                orderBy = "order by ur.firstNames ";
                formBean.setOrderByAuthor(orderDirection);
            }
            else if (sort.equals("title")) {
                orderBy += "order by h.title ";
                formBean.setOrderByTitle(orderDirection);
            }

            orderBy += order;
        }
        else {
            if (formBean.getOrderBy() == null) {
                orderBy += "order by h.creationDate desc";
                formBean.setOrderByCreationDate(true);
            }
            else {
                orderBy = formBean.getOrderBy();
            }
        }
        logger.debug("ORDER BY:" + orderBy);
        formBean.setOrderBy(orderBy);

        dbHighlightsList = DbUtil.getHighlightList(moduleInstance.getSite().
            getSiteId(), moduleInstance.getInstanceName(), orderBy);

        // Pagination
        int numberOfItemsPerPage = Highlight.ITEMS_PER_PAGE;

        doStartPagination(formBean, numberOfItemsPerPage);

        if (dbHighlightsList != null) {
            if (dbHighlightsList.size() <= numberOfItemsPerPage) {
                formBean.setNext(null);
                formBean.setPrev(null);
            }
            else {
                genPagesList(formBean, dbHighlightsList.size());

                dbHighlightsList = dbHighlightsList.subList(getOffset(),
                    dbHighlightsList.size());

                if (dbHighlightsList.size() >= (numberOfItemsPerPage + 1)) {
                    dbHighlightsList = dbHighlightsList.subList(0,
                        (numberOfItemsPerPage + 1));
                }
                endPagination(dbHighlightsList.size());
            }
        }
        //--//--//--//
        if (dbHighlightsList != null) {

            int n;
            if ( (numberOfItemsPerPage + 1) == dbHighlightsList.size())
                n = dbHighlightsList.size() - 1;
            else {
                n = dbHighlightsList.size();
            }
            ///

            for (int i = 0; i < n; i++) {
                Highlight highlight = (Highlight) dbHighlightsList.get(i);
                HighlightItemForm.HighlightInfo hi = new HighlightItemForm.
                    HighlightInfo();

                hi.setActive(highlight.isActive());

                if (highlight.getTitle() != null) {
                  hi.setTitle(highlight.getTitle());
                }

                if (highlight.getTopicText() != null ) {
                  if (highlight.getShortTopicLength() <
                      highlight.getTopicText().length()) {
                    String truncated = org.digijava.module.common.util.
                        ModuleUtil.
                        truncateWords(highlight.getTopicText(),
                                      highlight.getShortTopicLength());
                    // 3 because truncateWords() appends "..." in the end of the
                    // cutted text
                    hi.setTopic(truncated.substring(0,
                                                    (truncated.trim().length() - 3)));
                  }
                  else {
                    hi.setTopic(highlight.getTopicText());
                  }
                }

                if (highlight.getDescription() != null ){
                  hi.setDescription(highlight.getDescription());
                }

                if (highlight.getCreationDate() != null) {
                    GregorianCalendar date = new GregorianCalendar();
                    date.setTime(highlight.getCreationDate());

                    hi.setCreationDate(DgUtil.formatDate(date.getTime()));
                }

                UserInfo author = DgUtil.getUserInfo(highlight.getAuthorUserId());

                hi.setAuthorFirstName(author.getFirstNames());
                hi.setAuthorLastName(author.getLastName());

                hi.setId(highlight.getId());

                  switch (highlight.getLayout()) {
                    case 1: case 2: {
                      hi.setHaveImage(true);
                      hi.setShowImage(highlight.isShowImage());
                      break;
                    }
                    case 3: {
                      hi.setHaveImage(false);

                      break;
                    }
                    default: {
                      break;
                    }
                  }
                hi.setIsPublic(highlight.isIsPublic());
                //
                highlightsList.add(hi);
            }

            formBean.setHighlightsList(highlightsList);

        }
        else {
            formBean.setHighlightsList(null);
        }

        return mapping.findForward("forward");
    }

}
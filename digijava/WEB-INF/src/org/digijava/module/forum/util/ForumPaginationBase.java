/*
*   ForumPaginationBase.java
*   @Author George Kvizhinadze gio@digijava.org
*   Created:
*   CVS-ID: $Id: ForumPaginationBase.java,v 1.1 2005-07-06 10:34:19 rahul Exp $
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

package org.digijava.module.forum.util;

import org.apache.struts.action.ActionForm;
import java.util.Collection;
import java.util.ArrayList;
import org.digijava.module.forum.util.ForumPaginationItem;

public class ForumPaginationBase
    extends ActionForm {
    //Page navigation
    private int startForm;
    private int size;
    private int itemPerPage;
    private Collection paginationItems;
    private int intervalOffset = 3;

    public void populatePaginationItems() {
        if (itemPerPage > 0) {

            paginationItems = new ArrayList();

            int totalPages = size / itemPerPage;
            int currentPage = startForm / itemPerPage;

            for (int pageStart = 0;
                 pageStart < size;
                 pageStart += itemPerPage) {
                int pageNo = pageStart/itemPerPage;

                if (pageNo==0 || pageNo==totalPages ||
                    ((pageNo + intervalOffset + 2) > currentPage) &&
                    ((pageNo - intervalOffset - 2) < currentPage)) {

                    ForumPaginationItem pageItem =
                        new ForumPaginationItem(pageStart);
                    pageItem.setDisplayString(String.valueOf(pageNo + 1));

                    if (pageStart == 0 && currentPage >= intervalOffset + 1) {
                        pageItem.setItemType(ForumPaginationItem.FIRST_PAGE);
                    }
                    else if (pageNo == totalPages &&
                             currentPage + 1 <= totalPages - intervalOffset) {
                        pageItem.setItemType(ForumPaginationItem.LAST_PAGE);
                    }
                    else if (pageStart == startForm) {
                        pageItem.setItemType(ForumPaginationItem.CURRENT_PAGE);
                    } else if (pageNo + intervalOffset + 1 == currentPage ||
                               pageNo - intervalOffset - 1 == currentPage){
                        pageItem.setItemType(ForumPaginationItem.INTERVAL);
                    } else {
                        pageItem.setItemType(ForumPaginationItem.GENERIC_PAGE);
                    }
                    paginationItems.add(pageItem);
                }

            }
        }
    }

    public void setToFirstPage() {
        this.startForm = 0;
    }

    public void setToLastPage() {
        if (itemPerPage > 0) {
            this.startForm = size - size % itemPerPage;
        }
    }




    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getStartForm() {
        return startForm;
    }

    public void setStartForm(int startForm) {
        this.startForm = startForm;
    }

    public int getItemPerPage() {
        return itemPerPage;
    }

    public void setItemPerPage(int itemPerPage) {
        this.itemPerPage = itemPerPage;
    }

    public Collection getPaginationItems() {
        return paginationItems;
    }

    public void setPaginationItems(Collection paginationItems) {
        this.paginationItems = paginationItems;
    }
    public int getIntervalOffset() {
        return intervalOffset;
    }
    public void setIntervalOffset(int intervalOffset) {
        this.intervalOffset = intervalOffset;
    }

}
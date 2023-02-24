package org.digijava.module.aim.form;

import java.util.Collection;

import org.apache.struts.action.ActionForm;

public class OrgTypeManagerForm extends ActionForm {

    private Collection organisation;
    private Collection pages;
    private Integer currentPage;
    private Integer pagesSize;
    
    public Integer getPagesSize() {
        return pagesSize;
    }

    public void setPagesSize(Integer pagesSize) {
        this.pagesSize = pagesSize;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Collection getOrganisation() {
         return organisation;
    }

    public void setOrganisation(Collection organisation) {
         this.organisation = organisation;
    }

    public Collection getPages() {
         return pages;
    }

    public void setPages(Collection pages) {
         this.pages = pages;
    }
    
}

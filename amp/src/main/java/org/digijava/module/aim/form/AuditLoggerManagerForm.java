package org.digijava.module.aim.form;

import java.io.Serializable;
import java.util.Collection;

import org.apache.struts.action.ActionForm;

public class AuditLoggerManagerForm extends ActionForm implements Serializable{

    
    private static final long serialVersionUID = -2614366966871314200L;
    
    private Collection logs;
    private String sortBy;
    private int pagesToShow;
    private Integer currentPage;
    private int pagesSize;
    private Collection pages = null;
    private int offset;
    private String useraction;
    private String frecuency;
    private boolean withLogin;
    
    public String getUseraction() {
        return useraction;
    }

    public void setUseraction(String useraction) {
        this.useraction = useraction;
    }

    public String getFrecuency() {
        return frecuency;
    }

    public void setFrecuency(String frecuency) {
        this.frecuency = frecuency;
    }

    public int getOffset() {
        int value;
        if (getCurrentPage()> (this.getPagesToShow()/2)){
            value = (this.getCurrentPage() - (this.getPagesToShow()/2))-1;
        }
        else {
            value = 0;
        }
        setOffset(value);
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
    
    public Collection getPages() {
        return pages;
    }
    public void setPages(Collection pages) {
        this.pages = pages;
          if(pages!=null)
          {    
              this.pagesSize=pages.size();
          }
    }
    
    public int getPagesToShow() {
        return pagesToShow;
    }
    public void setPagesToShow(int pagesToShow) {
        this.pagesToShow = pagesToShow;
    }
    public Integer getCurrentPage() {
        return currentPage;
    }
    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }
    public int getPagesSize() {
        return pagesSize;
    }
    public void setPagesSize(int pagesSize) {
        this.pagesSize = pagesSize;
    }
    public Collection getLogs() {
        return logs;
    }
    public void setLogs(Collection logs) {
        this.logs = logs;
    }
    public String getSortBy() {
        return sortBy;
    }
    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public boolean isWithLogin() {
        return withLogin;
    }

    public void setWithLogin(boolean withLogin) {
        this.withLogin = withLogin;
    }
}

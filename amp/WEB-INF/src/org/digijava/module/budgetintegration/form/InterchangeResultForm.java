package org.digijava.module.budgetintegration.form;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.AmpInterchangeableResult;

import java.util.List;

/**
 * Created by esoliani on 20/06/16.
 */
public class InterchangeResultForm extends ActionForm {
    private static final int NUMBER_OF_PAGES_TO_SHOW = 10;

    private List<AmpInterchangeableResult> results;
    private Integer offset = 0;
    private Integer pageSize = 10;

    private Integer currentPage = 1;
    private Integer pagesToShow = NUMBER_OF_PAGES_TO_SHOW;
    private Integer lastPage = 2;
    private Integer pageOffset = 1;
    private List<Integer> pages;

    private String filterDate;
    private String sortBy;

    public List<AmpInterchangeableResult> getResults() {
        return results;
    }

    public void setResults(List<AmpInterchangeableResult> results) {
        this.results = results;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
        this.setOffset((currentPage - 1) * pageSize);
    }

    public Integer getPagesToShow() {
        return pagesToShow;
    }

    public void setPagesToShow(Integer pagesToShow) {
        this.pagesToShow = pagesToShow;
    }

    public Integer getLastPage() {
        return lastPage;
    }

    public void setLastPage(Integer lastPage) {
        this.lastPage = lastPage;
    }

    public Integer getPageOffset() {
        return pageOffset;
    }

    public void setPageOffset(Integer pageOffset) {
        this.pageOffset = pageOffset;
    }

    public List<Integer> getPages() {
        return pages;
    }

    public void setPages(List<Integer> pages) {
        this.pages = pages;
    }

    public String getFilterDate() {
        return filterDate;
    }

    public void setFilterDate(String filterDate) {
        this.filterDate = filterDate;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    @Override
    public String toString() {
        return "InterchangeResultForm{" +
                "results=" + results +
                '}';
    }
}

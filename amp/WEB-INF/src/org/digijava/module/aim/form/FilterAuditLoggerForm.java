package org.digijava.module.aim.form;

import org.apache.struts.action.ActionForm;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.helper.DateConversion;
import org.joda.time.DateTime;

import java.util.Collection;
import java.util.Date;


public class FilterAuditLoggerForm extends ActionForm {

    public static final Integer LAST_HOUR_OF_DAY = 30;
    public static final Integer LAST_MINUTE_OF_HOUR = 59;
    public static final Integer LAST_SECOND_OF_MINUTE = 59;
    private Long selectedUser;
    private String selectedTeam;
    private String selectedDateFrom;
    private String selectedDateTo;
    private Long effectiveSelectedUser;
    private String effectiveSelectedTeam;
    private Date effectiveDateFrom;
    private Date effectiveDateTo;
    private Collection<User> userList;

    private Collection logs;
    private String sortBy;
    private int pagesToShow;
    private Integer currentPage;
    private int pagesSize;
    private Collection pages = null;
    private int offset;
    private String useraction;
    private String frecuency;

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
        if (getCurrentPage() > (this.getPagesToShow() / 2)) {
            value = (this.getCurrentPage() - (this.getPagesToShow() / 2)) - 1;
        } else {
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
        if (pages != null) {
            this.pagesSize = pages.size();
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


    public Collection<User> getUserList() {
        return userList;
    }

    public void setUserList(Collection<User> userList) {
        this.userList = userList;
    }

    public FilterAuditLoggerForm() {
        super();

    }

    public String getSelectedDateFrom() {
        return selectedDateFrom;
    }

    public void setSelectedDateFrom(String selectedDateFrom) {
        this.selectedDateFrom = selectedDateFrom;
    }

    public String getSelectedDateTo() {
        return selectedDateTo;
    }

    public void setSelectedDateTo(String selectedDateTo) {
        this.selectedDateTo = selectedDateTo;
    }

    public Long getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(Long selectedUser) {
        this.selectedUser = selectedUser;
    }

    public String getSelectedTeam() {
        return selectedTeam;
    }

    public void setSelectedTeam(String selectedTeam) {
        this.selectedTeam = selectedTeam;
    }

    public Long getEffectiveSelectedUser() {
        return effectiveSelectedUser;
    }

    public void setEffectiveSelectedUser(Long effectiveSelectedUser) {
        this.effectiveSelectedUser = effectiveSelectedUser;
    }

    public String getEffectiveSelectedTeam() {
        return effectiveSelectedTeam;
    }

    public void setEffectiveSelectedTeam(String effectiveSelectedTeam) {
        this.effectiveSelectedTeam = effectiveSelectedTeam;
    }

    public Date getEffectiveDateFrom() {
        return effectiveDateFrom;
    }

    public void setEffectiveDateFrom(Date effectiveDateFrom) {
        this.effectiveDateFrom = effectiveDateFrom;
    }

    public Date getEffectiveDateTo() {
        return effectiveDateTo;
    }

    public void setEffectiveDateTo(Date effectiveDateTo) {
        this.effectiveDateTo = effectiveDateTo;
    }

    public void populateEffectiveFilters() {
        effectiveSelectedUser = this.getSelectedUser() != null && !this.getSelectedUser().equals(-1L)
                ? this.getSelectedUser() : null;
        effectiveSelectedTeam = this.getSelectedTeam() != null && !this.getSelectedTeam().equals("-1")
                && effectiveSelectedUser == null ? this.getSelectedTeam() : null;
        if (effectiveSelectedTeam == null) {
            this.setSelectedTeam(null);
        }
        effectiveDateFrom = DateConversion.getDate(this.getSelectedDateFrom());
        if (this.getSelectedDateTo() != null) {

            effectiveDateTo = new DateTime(DateConversion.getDate(this.getSelectedDateTo())).
                    plusHours(LAST_HOUR_OF_DAY).plusMinutes(LAST_MINUTE_OF_HOUR).
                    plusSeconds(LAST_SECOND_OF_MINUTE).toDate();
        }
    }
}
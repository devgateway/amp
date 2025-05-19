package org.digijava.module.aim.form;

import org.apache.struts.action.ActionForm;

import java.util.Collection;

public class TeamActivitiesForm extends ActionForm {

    private Collection activities;
    private Collection allActivities;
    private Collection members;
    private Long activityId;
    private Long teamId;
    private String teamName;
    private String memberName;
    private Long memberId;
    private String activityName;
    private Long selActivities[];
    private String addActivity;
    private String removeActivity;
    private String assignActivity;
    private Collection pages;
    private Integer currentPage;
    private int page = 0;
    private String sort = null;
    private String sortOrder = null;
    private Long dnrTeamId;
    private boolean donorFlag = false;
    
    private String keyword;
    private int tempNumResults = 10;

    /**
     * @return Returns the activities.
     */
    public Collection getActivities() {
        return activities;
    }
    /**
     * @param activities The activities to set.
     */
    public void setActivities(Collection activities) {
        this.activities = activities;
    }
    /**
     * @return Returns the activityId.
     */
    public Long getActivityId() {
        return activityId;
    }
    /**
     * @param activityId The activityId to set.
     */
    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }
    /**
     * @return Returns the activityName.
     */
    public String getActivityName() {
        return activityName;
    }
    /**
     * @param activityName The activityName to set.
     */
    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
    /**
     * @return Returns the addActivity.
     */
    public String getAddActivity() {
        return addActivity;
    }
    /**
     * @param addActivity The addActivity to set.
     */
    public void setAddActivity(String addActivity) {
        this.addActivity = addActivity;
    }
    /**
     * @return Returns the allActivities.
     */
    public Collection getAllActivities() {
        return allActivities;
    }
    /**
     * @param allActivities The allActivities to set.
     */
    public void setAllActivities(Collection allActivities) {
        this.allActivities = allActivities;
    }
    /**
     * @return Returns the assignActivity.
     */
    public String getAssignActivity() {
        return assignActivity;
    }
    /**
     * @param assignActivity The assignActivity to set.
     */
    public void setAssignActivity(String assignActivity) {
        this.assignActivity = assignActivity;
    }
    /**
     * @return Returns the currentPage.
     */
    public Integer getCurrentPage() {
        return currentPage;
    }
    /**
     * @param currentPage The currentPage to set.
     */
    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }
    /**
     * @return Returns the memberId.
     */
    public Long getMemberId() {
        return memberId;
    }
    /**
     * @param memberId The memberId to set.
     */
    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
    /**
     * @return Returns the memberName.
     */
    public String getMemberName() {
        return memberName;
    }
    /**
     * @param memberName The memberName to set.
     */
    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
    /**
     * @return Returns the page.
     */
    public int getPage() {
        return page;
    }
    /**
     * @param page The page to set.
     */
    public void setPage(int page) {
        this.page = page;
    }
    /**
     * @return Returns the pages.
     */
    public Collection getPages() {
        return pages;
    }
    /**
     * @param pages The pages to set.
     */
    public void setPages(Collection pages) {
        this.pages = pages;
    }
    /**
     * @return Returns the removeActivity.
     */
    public String getRemoveActivity() {
        return removeActivity;
    }
    /**
     * @param removeActivity The removeActivity to set.
     */
    public void setRemoveActivity(String removeActivity) {
        this.removeActivity = removeActivity;
    }
    /**
     * @return Returns the selActivities.
     */
    public Long[] getSelActivities() {
        return selActivities;
    }
    /**
     * @param selActivities The selActivities to set.
     */
    public void setSelActivities(Long[] selActivities) {
        this.selActivities = selActivities;
    }
    /**
     * @return Returns the teamId.
     */
    public Long getTeamId() {
        return teamId;
    }
    /**
     * @param teamId The teamId to set.
     */
    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }
    /**
     * @return Returns the teamName.
     */
    public String getTeamName() {
        return teamName;
    }
    /**
     * @param teamName The teamName to set.
     */
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
    /**
     * @return Returns the sort.
     */
    public String getSort() {
        return sort;
    }
    /**
     * @param sort The sort to set.
     */
    public void setSort(String sort) {
        this.sort = sort;
    }
    /**
     * @return Returns the sortOrder.
     */
    public String getSortOrder() {
        return sortOrder;
    }
    /**
     * @param sortOrder The sortOrder to set.
     */
    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
    /**
     * @return Returns the dnrTeamId.
     */
    public Long getDnrTeamId() {
        return dnrTeamId;
    }
    /**
     * @param dnrTeamId The dnrTeamId to set.
     */
    public void setDnrTeamId(Long dnrTeamId) {
        this.dnrTeamId = dnrTeamId;
    }
    /**
     * @return Returns the donorFlag.
     */
    public boolean isDonorFlag() {
        return donorFlag;
    }
    /**
     * @param donorFlag The donorFlag to set.
     */
    public void setDonorFlag(boolean donorFlag) {
        this.donorFlag = donorFlag;
    }
    public Collection getMembers() {
        return members;
    }
    public void setMembers(Collection members) {
        this.members = members;
    }
    public String getKeyword() {
        return keyword;
    }
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    public int getTempNumResults() {
        return tempNumResults;
    }
    public void setTempNumResults(int tempNumResults) {
        this.tempNumResults = tempNumResults;
    }
    
}

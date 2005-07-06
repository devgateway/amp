package org.digijava.module.aim.form;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class TeamActivitiesForm extends ActionForm {

	private Collection activities = null;
	private Long activityId = null;
	private Long teamId = null;
	private String teamName = null;
	private String memberName = null;
	private Long memberId = null;
	private String activityName = null;
	private Long selActivities[] = null;
	private String addActivity = null;
	private String removeActivity = null;
	private String assignActivity = null;
	private Collection pages = null;
	private Integer currentPage = null;
	
	private int page = 0;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currPage) {
		currentPage = currPage;
	}

	public Collection getPages() {
		return pages;
	}

	public void setPages(Collection pages) {
		this.pages = pages;
	}

	public Long getMemberId() {
		return this.memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getMemberName() {
		return this.memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getAssignActivity() {
		return this.assignActivity;
	}

	public void setAssignActivity(String assignActivity) {
		this.assignActivity = assignActivity;
	}

	public String getAddActivity() {
		return this.addActivity;
	}

	public void setAddActivity(String addActivity) {
		this.addActivity = addActivity;
	}

	public String getRemoveActivity() {
		return this.removeActivity;
	}

	public void setRemoveActivity(String removeActivity) {
		this.removeActivity = removeActivity;
	}

	public Long[] getSelActivities() {
		return this.selActivities;
	}

	public void setSelActivities(Long selActivities[]) {
		this.selActivities = selActivities;
	}

	public Long getTeamId() {
		return this.teamId;
	}

	public void setTeamId(Long teamId) {
		this.teamId = teamId;
	}

	public String getTeamName() {
		return (this.teamName);
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public Collection getActivities() {
		return (this.activities);
	}

	public void setActivities(Collection activities) {
		this.activities = activities;
	}

	public Long getActivityId() {
		return (this.activityId);
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	public String getActivityName() {
		return (this.activityName);
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.selActivities = null;
	}

}
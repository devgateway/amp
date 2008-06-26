package org.digijava.module.message.form;

import java.util.Collection;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.message.helper.QuartzJobForm;

public class QuartzJobManagerForm extends ActionForm {
    /**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String name;
    private String classFullname;
    private String startDateTime;
    private String endDateTime;
    private int triggerType;
    private int selectedDay;
    private String exeTime;
    private QuartzJobForm job;
    private Collection<QuartzJobForm> jobs;
    private String action;

    public QuartzJobManagerForm() {

    }

    public void reset(ActionMapping mapping, HttpServletRequest request){
        name=null;
        startDateTime=null;
        endDateTime=null;
        triggerType=0;
        selectedDay=1;
        exeTime=null;
        jobs=null;
        action=null;
    }

    public Collection<QuartzJobForm> getJobs() {
        return jobs;
    }

    public String getAction() {
        return action;
    }

    public QuartzJobForm getJob() {
        return job;
    }

    public String getName() {
        return name;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public String getExeTime() {
        return exeTime;
    }

    public int getSelectedDay() {
        return selectedDay;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public int getTriggerType() {
        return triggerType;
    }

    public String getClassFullname() {
        return classFullname;
    }

    public void setJobs(Collection<QuartzJobForm> jobs) {
        this.jobs = jobs;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setJob(QuartzJobForm job) {
        this.job = job;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExeTime(String exeTime) {
        this.exeTime = exeTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public void setSelectedDay(int selectedDay) {
        this.selectedDay = selectedDay;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public void setTriggerType(int triggerType) {
        this.triggerType = triggerType;
    }

    public void setClassFullname(String classFullname) {
        this.classFullname = classFullname;
    }
}

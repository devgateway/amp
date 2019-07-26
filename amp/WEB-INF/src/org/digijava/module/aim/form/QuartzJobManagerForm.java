package org.digijava.module.aim.form;

import java.util.Collection;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpQuartzJobClass;
import org.digijava.module.aim.helper.QuartzJobForm;

public class QuartzJobManagerForm extends ActionForm {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private boolean invalidTrigger = false;
    private boolean invalidEndDate = false;
    private boolean invalidClass = false;
    


    private String name;
    

    private String classFullname;
    private String startDateTime;
    private String endDateTime;
    private int triggerType;
    private int selectedDay;
    private int selectedMonthDay;

    private QuartzJobForm job;
    private Collection<QuartzJobForm> jobs;
    private Collection<AmpQuartzJobClass> jcCol;
    private String action;
    private Boolean editAction;
    private String startH;
    private String startM;

    private String endH;
    private String endM;

    private String exeTimeM;
    private String exeTimeH;

    public String getEndH() {
        return endH;
    }

    public void setEndH(String endH) {
        this.endH = endH;
    }

    public String getEndM() {
        return endM;
    }

    public String getExeTimeM() {
        return exeTimeM;
    }

    public void setExeTimeM(String exeTimeM) {
        this.exeTimeM = exeTimeM;
    }

    public String getExeTimeH() {
        return exeTimeH;
    }

    public void setExeTimeH(String exeTimeH) {
        this.exeTimeH = exeTimeH;
    }

    public void setEndM(String endM) {
        this.endM = endM;
    }

    public String getStartH() {
        return startH;
    }

    public void setStartH(String startH) {
        this.startH = startH;
    }

    public String getStartM() {
        return startM;
    }

    public void setStartM(String startM) {
        this.startM = startM;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public QuartzJobManagerForm() {

    }

    public void reset() {
        resetJobForm();
        jobs = null;
    }
    
    public void resetJobForm() {
        name = null;
        startDateTime = null;
        startH = null;
        startM = null;
        endDateTime = null;
        endH = null;
        endM = null;
        triggerType = 0;
        selectedDay = 1;
        exeTimeH = null;
        exeTimeM = null;
        action = null;
        selectedMonthDay = 1;
        editAction = false;
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

    public Collection<AmpQuartzJobClass> getJcCol() {
        return jcCol;
    }

    public int getSelectedMonthDay() {
        return selectedMonthDay;
    }

    public Boolean getEditAction() {
        return editAction;
    }

    public void setEditAction(Boolean editAction) {
        this.editAction = editAction;
    }

    public void setSelectedMonthDay(int selectedMonthDay) {
        this.selectedMonthDay = selectedMonthDay;
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

    public void setJcCol(Collection<AmpQuartzJobClass> jcCol) {
        this.jcCol = jcCol;
    }
    
    public boolean isInvalidTrigger() {
        return invalidTrigger;
    }

    public void setInvalidTrigger(boolean invalidTrigger) {
        this.invalidTrigger = invalidTrigger;
    }
    public boolean isInvalidEndDate() {
        return invalidEndDate;
    }

    public void setInvalidEndDate(boolean invalidEndDate) {
        this.invalidEndDate = invalidEndDate;
    }

    public boolean isInvalidClass() {
        return invalidClass;
    }

    public void setInvalidClass(boolean invalidClass) {
        this.invalidClass = invalidClass;
    }
}

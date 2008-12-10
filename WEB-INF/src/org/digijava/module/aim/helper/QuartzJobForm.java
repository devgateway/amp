package org.digijava.module.aim.helper;

public class QuartzJobForm {
    private String name;
    private String startDateTime;
    private String endDateTime;
    private String nextFireDateTime;
    private String prevFireDateTime;
    private String finalFireDateTime;
    private String classFullname;
    private String triggerName;
    private String groupName;
    private String triggerGroupName;
    private int triggerType;
    private String exeTime;
    private boolean paused;
    private int dayOfWeek;
    private int dayOfMonth;
    private boolean manualJob;

    public boolean isManualJob() {
        return manualJob;
    }

    public void setManualJob(boolean manualJob) {
        this.manualJob = manualJob;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public QuartzJobForm() {
    }

    public String getFinalFireDateTime() {
        return finalFireDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public String getName() {
        return name;
    }

    public String getNextFireDateTime() {
        return nextFireDateTime;
    }

    public String getPrevFireDateTime() {
        return prevFireDateTime;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public String getClassFullname() {
        return classFullname;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public String getTriggerGroupName() {
        return triggerGroupName;
    }

    public int getTriggerType() {
        return triggerType;
    }

    public String getExeTime() {
        return exeTime;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public void setFinalFireDateTime(String finalFireDateTime) {
        this.finalFireDateTime = finalFireDateTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNextFireDateTime(String nextFireDateTime) {
        this.nextFireDateTime = nextFireDateTime;
    }

    public void setPrevFireDateTime(String prevFireDateTime) {
        this.prevFireDateTime = prevFireDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public void setClassFullname(String classFullname) {
        this.classFullname = classFullname;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public void setTriggerGroupName(String triggerGroupName) {
        this.triggerGroupName = triggerGroupName;
    }

    public void setTriggerType(int triggerType) {
        this.triggerType = triggerType;
    }

    public void setExeTime(String exeTime) {
        this.exeTime = exeTime;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }
}

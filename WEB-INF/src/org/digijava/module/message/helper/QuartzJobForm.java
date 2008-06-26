package org.digijava.module.message.helper;

import java.util.Date;

public class QuartzJobForm {
    private String name;
    private Date startDateTime;
    private Date endDateTime;
    private Date nextFireDateTime;
    private Date prevFireDateTime;
    private Date finalFireDateTime;
    private String classFullname;
    private String triggerName;
    private String groupName;
    private String triggerGroupName;
    private int triggerType;
    private String exeTime;
    private boolean paused;
    private int dayOfWeek;

    public QuartzJobForm() {
    }

    public Date getFinalFireDateTime() {
        return finalFireDateTime;
    }

    public Date getEndDateTime() {
        return endDateTime;
    }

    public String getName() {
        return name;
    }

    public Date getNextFireDateTime() {
        return nextFireDateTime;
    }

    public Date getPrevFireDateTime() {
        return prevFireDateTime;
    }

    public Date getStartDateTime() {
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

    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }

    public void setFinalFireDateTime(Date finalFireDateTime) {
        this.finalFireDateTime = finalFireDateTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNextFireDateTime(Date nextFireDateTime) {
        this.nextFireDateTime = nextFireDateTime;
    }

    public void setPrevFireDateTime(Date prevFireDateTime) {
        this.prevFireDateTime = prevFireDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
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

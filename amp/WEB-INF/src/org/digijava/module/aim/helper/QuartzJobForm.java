package org.digijava.module.aim.helper;

public class QuartzJobForm {

    public static final int EVERY_SECOND = 0;
    public static final int MINUTELY = 1;
    public static final int HOURLY = 2;
    public static final int DAILY = 3;
    public static final int WEEKLY = 4;
    public static final int MONTHLY = 5;

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
    private boolean paused;
    private int dayOfWeek;
    private int dayOfMonth;
    private boolean manualJob;
   
    private String startH;
    private String startM;

    private String endH;
    private String endM;

    private String exeTimeH;
    private String exeTimeM;
    private String exeTimeS;
    
    public String getExeTimeS() {
        return exeTimeS;
    }

    public void setExeTimeS(String exeTimeS) {
        this.exeTimeS = exeTimeS;
    }

    public String getExeTimeH() {
        return exeTimeH;
    }

    public void setExeTimeH(String exeTimeH) {
        this.exeTimeH = exeTimeH;
    }

    public String getExeTimeM() {
        return exeTimeM;
    }

    public void setExeTimeM(String exeTimeM) {
        this.exeTimeM = exeTimeM;
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


    public String getEndH() {
        return endH;
    }

    public void setEndH(String endH) {
        this.endH = endH;
    }

    public String getEndM() {
        return endM;
    }

    public void setEndM(String endM) {
        this.endM = endM;
    }

    
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

  

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }
}

package org.digijava.module.aim.form ;

import org.apache.struts.action.ActionForm;

public class EditPhysicalProgressForm extends ActionForm
{
    private Long pid;
    private String activityId ;
    private String title;
    private String description ;
//  private String date ;
    private String dd,mm,yyyy ;

    
    public String getActivityId()
    {
        return activityId ; 
    }

    public Long getPid()
    {
        return pid ;    
    }
    
    public String getDescription() 
    {
        return description;
    }

    public String getTitle() 
    {
        return title;
    }

    public String getDd() 
    {
        return dd;
    }
    public String getMm() 
    {
        return mm;
    }
    public String getYyyy() 
    {
        return yyyy;
    }

    public void setDescription(String description) 
    {
        this.description = description ;
    }

    public void setTitle(String title) 
    {
        this.title = title ;
    }
    
    public void setDd(String dd) 
    {
        this.dd = dd ;
    }

    public void setMm(String mm) 
    {
        this.mm = mm ;
    }

    public void setYyyy(String yyyy) 
    {
        this.yyyy = yyyy ;
    }

    public void setActivityId(String activityId) 
    {
        this.activityId = activityId ;
    }

    public void setPid(Long pid) 
    {
        this.pid = pid ;
    }
    
}

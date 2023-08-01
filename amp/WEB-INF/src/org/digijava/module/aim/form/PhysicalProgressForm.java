package org.digijava.module.aim.form ;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.helper.Components;

import java.util.ArrayList;
import java.util.Collection;

public class PhysicalProgressForm extends ActionForm
{
    private String name;
    private String title;
    private String description ;
    private Collection physicalProgress;
    private Collection selectComponent;
    private int flag;
    private Long pid;
    private String display;
    private String dd;
    private String mm;
    private String yyyy;
    private Long ampActivityId;
    private boolean validLogin;
    private String compRepDate;
    private String ppRepDate;
    private String amount;
    private String currCode;
    private String compTitle;
    
    private Components component;
    private Collection<Components> components;
    private ArrayList issues;
    
    
    public String getDescription() 
    {
        return description;
    }

    public String getDisplay() 
    {
        return display;
    }

    public Long getAmpActivityId() 
    {
        return ampActivityId;
    }

    public Long getPid()
    {
        return pid ;    
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

    public void setPid(Long pid) 
    {
        this.pid = pid ;
    }

    public String getName() 
    {
        return name;
    }

    public String getTitle() 
    {
        return title;
    }

    public Collection getPhysicalProgress() 
    {
        return physicalProgress;
    }
    
    public Collection getSelectComponent() 
    {
        return selectComponent;
    }

    public int getFlag() 
    {
        return flag;
    }

    public boolean getValidLogin() 
    {
        return validLogin;
    }

    public void setDescription(String description) 
    {
        this.description = description ;
    }

    public void setDisplay(String display) 
    {
        this.display = display;
    }

    public void setAmpActivityId(Long ampActivityId) 
    {
        this.ampActivityId = ampActivityId;
    }
    
    public void setName(String name) 
    {
        this.name = name ;
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

    public void setPhysicalProgress(Collection collection) 
    {
        physicalProgress = collection;
    }
    
    public void setSelectComponent(Collection selectComponent) 
    {
        this.selectComponent = selectComponent;
    }

    public void setFlag(int flag)
    {
        this.flag = flag;
    }
    public void setValidLogin(boolean bool) 
    {
        this.validLogin = bool ;
    }

    /**
     * @return Returns the amount.
     */
    public String getAmount() {
        return amount;
    }
    /**
     * @param amount The amount to set.
     */
    public void setAmount(String amount) {
        this.amount = amount;
    }
    /**
     * @return Returns the compRepDate.
     */
    public String getCompRepDate() {
        return compRepDate;
    }
    /**
     * @param compRepDate The compRepDate to set.
     */
    public void setCompRepDate(String compRepDate) {
        this.compRepDate = compRepDate;
    }
    /**
     * @return Returns the ppRepDate.
     */
    public String getPpRepDate() {
        return ppRepDate;
    }
    /**
     * @param ppRepDate The ppRepDate to set.
     */
    public void setPpRepDate(String ppRepDate) {
        this.ppRepDate = ppRepDate;
    }
    /**
     * @return Returns the currCode.
     */
    public String getCurrCode() {
        return currCode;
    }
    /**
     * @param currCode The currCode to set.
     */
    public void setCurrCode(String currCode) {
        this.currCode = currCode;
    }
    /**
     * @return Returns the compTitle.
     */
    public String getCompTitle() {
        return compTitle;
    }
    /**
     * @param compTitle The compTitle to set.
     */
    public void setCompTitle(String compTitle) {
        this.compTitle = compTitle;
    }

    /**
     * @return Returns the issues.
     */
    public ArrayList getIssues() {
        return issues;
    }

    /**
     * @param issues The issues to set.
     */
    public void setIssues(ArrayList issues) {
        this.issues = issues;
    }
    /**
     * @return Returns the components.
     */
    public Collection<Components> getComponents() {
        return components;
    }
    /**
     * @param components The components to set.
     */
    public void setComponents(Collection<Components> components) {
        this.components = components;
    }
    /**
     * @return Returns the component.
     */
    public Components getComponent() {
        return component;
    }
    /**
     * @param component The component to set.
     */
    public void setComponent(Components component) {
        this.component = component;
    }
}

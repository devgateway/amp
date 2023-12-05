/**
 * 
 */
package org.digijava.module.aim.form;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.AmpReports;

import java.util.Collection;

/**
 * @author Alex Gartner
 *
 */
public class TabManagerForm extends ActionForm {
    
    private Long [] tabsId;
    private Collection<AmpReports> tabs;
    
    private AmpReports defaultTeamTab   = null;
    
    private Boolean exceptionOccurred   = false;
    private Boolean dataException       = false;
    private Boolean saveException       = false;
    
    private Boolean saveSuccessful      = false;
    private Boolean dataSuccessful      = false;
    
    public Collection<AmpReports> getTabs() {
        return tabs;
    }
    public void setTabs(Collection<AmpReports> tabs) {
        this.tabs = tabs;
    }
    public Long[] getTabsId() {
        return tabsId;
    }
    public void setTabsId(Long[] tabsId) {
        this.tabsId = tabsId;
    }
    
    public AmpReports getDefaultTeamTab() {
        return defaultTeamTab;
    }
    public void setDefaultTeamTab(AmpReports defaultTeamTab) {
        this.defaultTeamTab = defaultTeamTab;
    }
    public Boolean getDataException() {
        return dataException;
    }
    public void setDataException(Boolean dataException) {
        this.dataException = dataException;
    }
    public Boolean getExceptionOccurred() {
        return exceptionOccurred;
    }
    public void setExceptionOccurred(Boolean exceptionOccurred) {
        this.exceptionOccurred = exceptionOccurred;
    }
    public Boolean getSaveException() {
        return saveException;
    }
    public void setSaveException(Boolean saveException) {
        this.saveException = saveException;
    }
    public Boolean getDataSuccessful() {
        return dataSuccessful;
    }
    public void setDataSuccessful(Boolean dataSuccessful) {
        this.dataSuccessful = dataSuccessful;
    }
    public Boolean getSaveSuccessful() {
        return saveSuccessful;
    }
    public void setSaveSuccessful(Boolean saveSuccessful) {
        this.saveSuccessful = saveSuccessful;
    }

    
    
    

}

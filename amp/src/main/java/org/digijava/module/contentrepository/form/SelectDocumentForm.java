/**
 * 
 */
package org.digijava.module.contentrepository.form;

import org.apache.struts.action.ActionForm;
import org.digijava.module.contentrepository.helper.TeamInformationBeanDM;

/**
 * @author Alex Gartner
 *
 */
public class SelectDocumentForm extends ActionForm {
    
    private String type;

    private TeamInformationBeanDM teamInformationBeanDM;
    private boolean hasAddRights;
    
    private String [] selectedDocs;
    private String action   = null;
    
    private String documentsType                = null;
    private String showTheFollowingDocuments    = null;
    
    public String getDocumentsType() {
        return documentsType;
    }
    public void setDocumentsType(String documentsType) {
        this.documentsType = documentsType;
    }
    public boolean isHasAddRights() {
        return hasAddRights;
    }
    public void setHasAddRights(boolean hasAddRights) {
        this.hasAddRights = hasAddRights;
    }
    public TeamInformationBeanDM getTeamInformationBeanDM() {
        return teamInformationBeanDM;
    }
    public void setTeamInformationBeanDM(TeamInformationBeanDM teamInformationBeanDM) {
        this.teamInformationBeanDM = teamInformationBeanDM;
    }
    public String[] getSelectedDocs() {
        return selectedDocs;
    }
    public void setSelectedDocs(String[] selectedDocs) {
        this.selectedDocs = selectedDocs;
    }
    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }
    public String getShowTheFollowingDocuments() {
        return showTheFollowingDocuments;
    }
    public void setShowTheFollowingDocuments(String showTheFollowingDocuments) {
        this.showTheFollowingDocuments = showTheFollowingDocuments;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }   
}

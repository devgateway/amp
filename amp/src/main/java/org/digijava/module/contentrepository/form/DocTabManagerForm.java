/**
 * 
 */
package org.digijava.module.contentrepository.form;

import java.util.List;

import org.apache.struts.action.ActionForm;
import org.digijava.module.contentrepository.dbentity.filter.DocumentFilter;

/**
 * @author Alex Gartner
 *
 */
public class DocTabManagerForm extends ActionForm {
    private Long[] publicViewPosition;
    private String action;
    private String savingFilterName;
    private List<DocumentFilter> availableDocumentFilters;
    
    /* For Public View */
    private List publicFiltersPositioned;
    private List publicFiltersUnpositioned;
    
    
    public Long[] getPublicViewPosition() {
        return publicViewPosition;
    }
    public void setPublicViewPosition(Long[] publicViewPosition) {
        this.publicViewPosition = publicViewPosition;
    }
    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }
    public List<DocumentFilter> getAvailableDocumentFilters() {
        return availableDocumentFilters;
    }
    public void setAvailableDocumentFilters(
            List<DocumentFilter> availableDocumentFilters) {
        this.availableDocumentFilters = availableDocumentFilters;
    }
    public String getSavingFilterName() {
        return savingFilterName;
    }
    public void setSavingFilterName(String savingFilterName) {
        this.savingFilterName = savingFilterName;
    }
    public List getPublicFiltersPositioned() {
        return publicFiltersPositioned;
    }
    public void setPublicFiltersPositioned(List publicFiltersPositioned) {
        this.publicFiltersPositioned = publicFiltersPositioned;
    }
    public List getPublicFiltersUnpositioned() {
        return publicFiltersUnpositioned;
    }
    public void setPublicFiltersUnpositioned(List publicFiltersUnpositioned) {
        this.publicFiltersUnpositioned = publicFiltersUnpositioned;
    }
    
    
}

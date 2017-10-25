package org.digijava.module.categorymanager.dbentity;

public class AmpLinkedCategoriesState {
    private Long id;
    private AmpCategoryClass mainCategory;
    private AmpCategoryClass linkedCategory;
    private Boolean singleChoice;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public AmpCategoryClass getMainCategory() {
        return mainCategory;
    }
    public void setMainCategory(AmpCategoryClass mainCategory) {
        this.mainCategory = mainCategory;
    }
    public AmpCategoryClass getLinkedCategory() {
        return linkedCategory;
    }
    public void setLinkedCategory(AmpCategoryClass linkedCategory) {
        this.linkedCategory = linkedCategory;
    }
    public Boolean getSingleChoice() {
        return singleChoice;
    }
    public void setSingleChoice(Boolean singleChoice) {
        this.singleChoice = singleChoice;
    }
    
}

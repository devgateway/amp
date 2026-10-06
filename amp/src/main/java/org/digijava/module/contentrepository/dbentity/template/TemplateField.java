package org.digijava.module.contentrepository.dbentity.template;

import java.util.List;
import java.util.Set;
/**
 * @author skvisha
 *
 */
public abstract class TemplateField {
    private Long id;
    private Set<PossibleValue> possibleValues; // select,multiple select , multibox
    
    private TemplateDoc templateDoc;
    private Integer ordinalNumber; //field has it's ordinal number in template
    
    private List<PossibleValue> possibleValuesList; //used only for drawing them on user-side page.
        
    public String getType(){
        return "tf"; //template field
    }
    
    /**
     * how field get's rendered on the page
     */
    public abstract String getRendered();
    
    //whether this field is allowed to have multiple pre-defined values
    public boolean getCanHaveMultipleValues() {
        return true;
    }
    
    //whether this field is allowd not to have pre-defined possible values
    public boolean getHasEmptyPossibleValsRights() {
        return false;
    }
    
    public TemplateDoc getTemplateDoc() {
        return templateDoc;
    }

    public void setTemplateDoc(TemplateDoc templateDoc) {
        this.templateDoc = templateDoc;
    }

    public Integer getOrdinalNumber() {
        return ordinalNumber;
    }

    public void setOrdinalNumber(Integer ordinalNumber) {
        this.ordinalNumber = ordinalNumber;
    }   

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Set<PossibleValue> getPossibleValues() {
        return possibleValues;
    }

    public void setPossibleValues(Set<PossibleValue> possibleValues) {
        this.possibleValues = possibleValues;
    }

    public List<PossibleValue> getPossibleValuesList() {
        return possibleValuesList;
    }

    public void setPossibleValuesList(List<PossibleValue> possibleValuesList) {
        this.possibleValuesList = possibleValuesList;
    }   
    
}

package org.dgfoundation.amp.onepager.helper;

import java.util.List;

import org.digijava.module.aim.dbentity.AmpActivityDocument;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.contentrepository.jcrentity.Label;

public class TemporaryActivityDocument extends TemporaryDocument {
    private static final long serialVersionUID = 1L;
    
    private String description;
    private String note;
    private AmpCategoryValue type;
    private String year;
    private List<Label> labels;
    private List <ResourceTranslation> translatedTitleList;
    private List <ResourceTranslation> translatedDescriptionList;
    private List <ResourceTranslation> translatedNoteList;
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public AmpCategoryValue getType() {
        return type;
    }

    public void setType(AmpCategoryValue type) {
        this.type = type;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public AmpActivityDocument getExistingDocument() {
        return (AmpActivityDocument) existingDocument;
    }

    public void setExistingDocument(AmpActivityDocument existingDocument) {
        this.existingDocument = existingDocument;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;       
    }

    public List<Label> getLabels() {
        return labels;
    }

    public List<ResourceTranslation> getTranslatedTitleList() {
        return translatedTitleList;
    }

    public void setTranslatedTitleList(List<ResourceTranslation> translatedTitleList) {
        this.translatedTitleList = translatedTitleList;
    }

    public List<ResourceTranslation> getTranslatedDescriptionList() {
        return translatedDescriptionList;
    }

    public void setTranslatedDescriptionList(List<ResourceTranslation> translatedDescriptionList) {
        this.translatedDescriptionList = translatedDescriptionList;
    }

    public List<ResourceTranslation> getTranslatedNoteList() {
        return translatedNoteList;
    }

    public void setTranslatedNoteList(List<ResourceTranslation> translatedNoteList) {
        this.translatedNoteList = translatedNoteList;
    }

}

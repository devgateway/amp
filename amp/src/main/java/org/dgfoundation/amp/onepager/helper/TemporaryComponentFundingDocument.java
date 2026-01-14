package org.dgfoundation.amp.onepager.helper;

import org.digijava.module.aim.dbentity.AmpComponentFundingDocument;
import org.digijava.module.contentrepository.jcrentity.Label;

import java.util.List;

public class TemporaryComponentFundingDocument extends TemporaryDocument{
    private String description;
    private String note;
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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
    public AmpComponentFundingDocument getExistingDocument() {
        return (AmpComponentFundingDocument) existingDocument;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
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

package org.digijava.module.editor.dbentity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class EditorId implements Serializable {

    @Column(name = "SITE_ID", length = 100)
    private String siteId;

    @Column(name = "EDITOR_KEY")
    private String editorKey;

    @Column(name = "LANGUAGE", length = 2)
    private String language;

    // Constructors, getters, setters, and other methods

    // Constructors
    public EditorId() {
    }

    public EditorId(String siteId, String editorKey, String language) {
        this.siteId = siteId;
        this.editorKey = editorKey;
        this.language = language;
    }

    // Getters and Setters
    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getEditorKey() {
        return editorKey;
    }

    public void setEditorKey(String editorKey) {
        this.editorKey = editorKey;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    // Overriding equals and hashCode methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EditorId)) return false;
        EditorId that = (EditorId) o;
        return Objects.equals(getSiteId(), that.getSiteId()) &&
                Objects.equals(getEditorKey(), that.getEditorKey()) &&
                Objects.equals(getLanguage(), that.getLanguage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSiteId(), getEditorKey(), getLanguage());
    }
}

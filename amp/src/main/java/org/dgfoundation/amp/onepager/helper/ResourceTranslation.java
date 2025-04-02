package org.dgfoundation.amp.onepager.helper;

import java.io.Serializable;

public class ResourceTranslation implements Serializable{

    private String uuid;
    private String translation;
    private String locale;
    
    
    public ResourceTranslation(String uuid, String translation, String locale) {
        this.uuid = uuid;
        this.translation = translation;
        this.locale = locale;
    }
    public String getTranslation() {
        return translation;
    }
    public void setTranslation(String translation) {
        this.translation = translation;
    }
    public String getLocale() {
        return locale;
    }
    public void setLocale(String locale) {
        this.locale = locale;
    }
    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    
    
}

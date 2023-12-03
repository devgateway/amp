package org.digijava.module.translation.util;

import org.digijava.module.aim.dbentity.AmpContentTranslation;

import java.util.HashMap;

/**
 * Entity that stores translations for a certain field
 * @author aartimon@developmentgateway.org
 */
public class FieldTranslationPack {
    private HashMap<String, String> translations;
    private String objClass;
    private String fieldName;

    public FieldTranslationPack(String objClass, String fieldName) {
        this.objClass = objClass;
        this.fieldName = fieldName;
        this.translations = new HashMap<String, String>();
    }

    public void add(AmpContentTranslation trn){
        add(trn.getLocale(), trn.getTranslation());
    }

    public void add(String locale, String translation){
        if (translation == null) //do not try to store null
            translation = "";
        translations.put(locale, translation);
    }

    public String get(String locale){
        return translations.get(locale);
    }
    
    public String getNonNullBaseTrn(String locale, String currentLocale){
        String ret = get(locale);
        if (ret == null){
            ret = get(currentLocale);
            if (ret != null) //this is used to get the base translation, if it's not set we need to set it
                add(locale, ret);
        }
        return ret;
    }

    public HashMap<String, String> getTranslations() {
        return translations;
    }

    public String getObjClass() {
        return objClass;
    }

    public String getFieldName() {
        return fieldName;
    }
    
    @Override
    public String toString() {
        return "FieldTranslationPack[" + objClass + "(" + fieldName + "): {" + translations.toString() + "}]";
    }
}

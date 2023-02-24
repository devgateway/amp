/**
 * @author dan
 *
 * 
 */
package org.digijava.module.aim.helper;

import java.util.ArrayList;

import org.digijava.module.translation.entity.MessageGroup;

/**
 * @author dan
 * @deprecated use {@link MessageGroup}
 *
 */
@Deprecated
public class TrnHashMap {
    String lang=new String();
    ArrayList translations= new ArrayList();
    
    public TrnHashMap() {
        super();
        
        // TODO Auto-generated constructor stub
    }
    public String getLang() {
        return lang;
    }
    public void setLang(String lang) {
        this.lang = lang;
    }
    public ArrayList getTranslations() {
        return translations;
    }
    public void setTranslations(ArrayList translations) {
        this.translations = translations;
    }

}

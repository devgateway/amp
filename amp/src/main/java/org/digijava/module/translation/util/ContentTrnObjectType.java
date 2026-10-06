package org.digijava.module.translation.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: flyer
 * Date: 9/23/13
 * Time: 4:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class ContentTrnObjectType {
    String objectType;
    String objectDisplayName;
    Set<String> langs;

    public ContentTrnObjectType() {
        langs = new HashSet();
    }

    public ContentTrnObjectType(String objectType) {
        this.objectType = objectType;
        langs = new HashSet();
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getObjectDisplayName() {
        return objectDisplayName;
    }

    public void setObjectDisplayName(String objectDisplayName) {
        this.objectDisplayName = objectDisplayName;
    }

    public Set<String> getLangs() {
        return langs;
    }

    public void setLangs(Set<String> langs) {
        this.langs = langs;
    }

    public void addLangKey(String key) {
        this.langs.add(key);
    }

    public String getLangsAsString() {
        StringBuilder retVal = new StringBuilder();
        Iterator<String> it = langs.iterator();
        while (it.hasNext()) {
            retVal.append(it.next());
            if (it.hasNext()) retVal.append(", ");
        }

        return  retVal.toString();
    }
}

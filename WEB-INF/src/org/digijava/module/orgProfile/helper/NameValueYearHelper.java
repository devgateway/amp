

package org.digijava.module.orgProfile.helper;

import java.util.List;


public class NameValueYearHelper {
    private String name;
    private Long id;
    private List<String> values;
    private boolean needTranslation;

    public boolean getNeedTranslation() {
        return needTranslation;
    }

    public void setNeedTranslation(boolean needTranslation) {
        this.needTranslation = needTranslation;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

  
}

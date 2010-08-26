

package org.digijava.module.orgProfile.helper;

import java.util.List;


public class NameValueYearHelper {
    public static final Long NATIONAL_ID=-1l;
    public static final Long REGIONAL_ID=-2l;
    public static final int COMMITMENT_INDEX=0;
    public static final int DISBURSEMENT_INDEX=1;
    private String name;
    private Long id;
    private List<String> values;
    private List<Double> rawValues;
    private boolean needTranslation;

    public List<Double> getRawValues() {
        return rawValues;
    }

    public void setRawValues(List<Double> rawValues) {
        this.rawValues = rawValues;
    }

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

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof NameValueYearHelper)) {
            return false;
        } else {
            NameValueYearHelper obj = (NameValueYearHelper) o;
            if (this.id != null) {
                return this.id.equals(obj.getId());
            } else {
                return this.name.equals(obj.getName());
            }
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 67 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
}

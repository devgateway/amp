

package org.digijava.module.orgProfile.helper;

import java.util.SortedMap;


public class NameValueYearHelper {
    private String name;
    private Long id;
    private SortedMap<Long,String> yearValues;

    public SortedMap<Long, String> getYearValues() {
        return yearValues;
    }

    public void setYearValues(SortedMap<Long, String> yearValues) {
        this.yearValues = yearValues;
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

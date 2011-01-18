package org.digijava.module.widget.helper;

import java.util.Collections;
import java.util.List;

public class SectorHelper implements Comparable<SectorHelper> {

    private List<Long> ids;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    @Override
    public String toString() {
        return getName();

    }

    public int compareTo(SectorHelper o) {
       int result=1;
       Collections.sort(ids);
       List<Long> sectorsIds=o.getIds();
       Collections.sort(sectorsIds);
       if(ids.equals(sectorsIds)){
            result=0;
       }
       return result;
    }
}

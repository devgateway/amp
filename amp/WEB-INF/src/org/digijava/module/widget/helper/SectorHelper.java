package org.digijava.module.widget.helper;

public class SectorHelper implements Comparable<SectorHelper> {

    private String ids;
    private String name;

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();

    }

    public String getIds() {
        return ids;

    }

    public int compareTo(SectorHelper o) {
       return ids.compareTo(o.getIds());
    }
}

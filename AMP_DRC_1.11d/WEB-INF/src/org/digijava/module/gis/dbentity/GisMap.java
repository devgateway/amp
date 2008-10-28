package org.digijava.module.gis.dbentity;

import java.util.List;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class GisMap {

    private long id;
    private String mapCode;
    private String mapName;
    private String parentMapCode;
    private List <GisMapSegment> segments;


    public GisMap() {
    }

    public long getId() {
        return id;
    }

    public String getMapName() {
        return mapName;
    }

    public List <GisMapSegment> getSegments() {
        return segments;
    }

    public String getMapCode() {
        return mapCode;
    }

    public String getParentMapCode() {
        return parentMapCode;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public void setSegments(List <GisMapSegment> segments) {
        this.segments = segments;
    }

    public void setMapCode(String mapCode) {
        this.mapCode = mapCode;
    }

    public void setParentMapCode(String parentMapCode) {
        this.parentMapCode = parentMapCode;
    }


}

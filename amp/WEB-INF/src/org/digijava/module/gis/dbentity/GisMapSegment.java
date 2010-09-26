package org.digijava.module.gis.dbentity;

import java.util.ArrayList;
import java.util.List;

import org.digijava.module.gis.util.CoordinateRect;

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
public class GisMapSegment {

    public static int GIS_SEGMET_TYPE_GENERAL = 0;
    public static int GIS_SEGMET_TYPE_COUNTRY = 1;
    public static int GIS_SEGMET_TYPE_REGION = 2;

    private long id;
    private GisMap map;
    private long segmentId;
    private int segmentType;
    private String segmentName;
    private String segmentCode;
    private String segmentDescription;
    private String parentSegmentCode;
    private List <GisMapShape> shapes;


    //Non persistent fields
    private CoordinateRect segmentRect;


    public GisMapSegment() {

    }

    public GisMapSegment(long segmentId) {
        this.segmentId = segmentId;
        this.shapes = new ArrayList();
    }

    public long getSegmentId() {
        return segmentId;
    }

    public List <GisMapShape> getShapes() {
        return shapes;
    }

    public long getId() {
        return id;
    }

    public GisMap getMap() {
        return map;
    }

    public String getSegmentCode() {
        return segmentCode;
    }

    public String getSegmentDescription() {
        return segmentDescription;
    }

    public String getSegmentName() {
        return segmentName;
    }

    public int getSegmentType() {
        return segmentType;
    }

    public String getParentSegmentCode() {
        return parentSegmentCode;
    }

    public CoordinateRect getSegmentRect() {
        return segmentRect;
    }

    public void setSegmentId(long segmentId) {
        this.segmentId = segmentId;
    }

    public void setShapes(List <GisMapShape> shapes) {
        this.shapes = shapes;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setMap(GisMap map) {
        this.map = map;
    }

    public void setSegmentCode(String segmentCode) {
        this.segmentCode = segmentCode;
    }

    public void setSegmentName(String segmentName) {
        this.segmentName = segmentName;
    }

    public void setSegmentType(int segmentType) {
        this.segmentType = segmentType;
    }

    public void setSegmentDescription(String segmentDescription) {
        this.segmentDescription = segmentDescription;
    }

    public void setParentSegmentCode(String parentSegmentCode) {
        this.parentSegmentCode = parentSegmentCode;
    }

    public void setSegmentRect(CoordinateRect segmentRect) {
        this.segmentRect = segmentRect;
    }


}

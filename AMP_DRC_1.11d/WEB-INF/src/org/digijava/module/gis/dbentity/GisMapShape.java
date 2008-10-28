package org.digijava.module.gis.dbentity;

import java.util.ArrayList;
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
public class GisMapShape {

    private long id;
    private GisMapSegment segment;
    private long shapeId;
    private List <GisMapPoint> shapePoints;

    public GisMapShape() {
    }


    public GisMapShape(long shapeId) {
        this.shapeId = shapeId;
        this.shapePoints = new ArrayList();
    }

    public long getShapeId() {
        return shapeId;
    }

    public List <GisMapPoint> getShapePoints() {
        return shapePoints;
    }

    public long getId() {
        return id;
    }

    public GisMapSegment getSegment() {
        return segment;
    }

    public void setShapeId(long shapeId) {
        this.shapeId = shapeId;
    }

    public void setShapePoints(List <GisMapPoint> shapePoints) {
        this.shapePoints = shapePoints;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setSegment(GisMapSegment segment) {
        this.segment = segment;
    }
}

package org.digijava.module.gis.util;

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
public class HilightData {
    private int segmentId;
    private ColorRGB color;

    public HilightData() {
    }

    public HilightData(int segmentId, ColorRGB color) {
        this.segmentId = segmentId;
        this.color = color;
    }

    public ColorRGB getColor() {
        return color;
    }

    public int getSegmentId() {
        return segmentId;
    }

    public void setColor(ColorRGB color) {
        this.color = color;
    }

    public void setSegmentId(int segmentId) {
        this.segmentId = segmentId;
    }


}

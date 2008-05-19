package org.digijava.module.gis.dbentity;

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
public class GisMapPoint {
    public GisMapPoint() {
    }

    private long id;
    private float longatude, latitude;
    private long sequenceNumber;
    private GisMapShape shape;

    public GisMapPoint(long sequenceNumber, float longitude, float latitude) {
        this.sequenceNumber = sequenceNumber;
        this.longatude = longitude;
        this.latitude = latitude;
    }


    public float getLatitude() {
        return latitude;
    }

    public float getLongatude() {
        return longatude;
    }

    public long getSequenceNumber() {
        return sequenceNumber;
    }

    public long getId() {
        return id;
    }

    public GisMapShape getShape() {
        return shape;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setLongatude(float longitude) {
        this.longatude = longitude;
    }

    public void setSequenceNumber(long sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setShape(GisMapShape shape) {
        this.shape = shape;
    }


}

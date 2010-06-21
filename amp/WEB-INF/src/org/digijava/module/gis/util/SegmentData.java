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
public class SegmentData {
    public SegmentData() {
    }

    public String getParentCode() {
        return parentCode;
    }

    public String getSegmentCode() {
        return segmentCode;
    }

    public String getSegmentValue() {
        return segmentValue;
    }

    public String getIndicatorSrcName() {
        return indicatorSrcName;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public void setSegmentCode(String segmentCode) {
        this.segmentCode = segmentCode;
    }

    public void setSegmentValue(String segmentValue) {
        this.segmentValue = segmentValue;
    }

    public void setIndicatorSrcName(String indicatorSrcName) {
        this.indicatorSrcName = indicatorSrcName;
    }

    String segmentCode;
    String parentCode;
    String segmentValue;
    String indicatorSrcName;
}

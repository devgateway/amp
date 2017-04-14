package org.digijava.kernel.ampapi.endpoints.settings;

/**
 * Created by anpicca on 04/01/2017.
 */
public class SettingRange {
    public enum Type {
        INT_VALUE,
        DATE_VALUE;
    };
    public final Type type;
    private String from;
    private String to;
    private String rangeFrom;
    private String rangeTo;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getRangeFrom() {
        return rangeFrom;
    }

    public void setRangeFrom(String rangeFrom) {
        this.rangeFrom = rangeFrom;
    }

    public String getRangeTo() {
        return rangeTo;
    }

    public void setRangeTo(String rangeTo) {
        this.rangeTo = rangeTo;
    }

    public SettingRange(Type type) {
        this.type = type;
    }
}

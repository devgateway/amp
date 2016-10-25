package org.digijava.kernel.ampapi.endpoints.settings;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author Octavian Ciubotaru
 */
public class Settings {

    @JsonProperty("currency")
    private String currencyCode;

    @JsonProperty("calendar-id")
    private String calendarId;

    @JsonProperty("year-range")
    private YearRange yearRange;

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }

    public YearRange getYearRange() {
        return yearRange;
    }

    public void setYearRange(YearRange yearRange) {
        this.yearRange = yearRange;
    }

    static class YearRange {

        private String from;
        private String to;

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
    }
}

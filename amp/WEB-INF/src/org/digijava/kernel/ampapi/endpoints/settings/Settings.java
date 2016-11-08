package org.digijava.kernel.ampapi.endpoints.settings;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Structure used to store user specified settings.
 *
 * @author Octavian Ciubotaru
 */
public class Settings {

    @JsonProperty(SettingsConstants.CURRENCY_ID)
    private String currencyCode;

    @JsonProperty(SettingsConstants.CALENDAR_TYPE_ID)
    private String calendarId;

    @JsonProperty(SettingsConstants.YEAR_RANGE_ID)
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

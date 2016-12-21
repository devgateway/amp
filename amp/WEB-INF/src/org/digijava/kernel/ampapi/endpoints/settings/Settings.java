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

    @JsonProperty(SettingsConstants.AMOUNT_FORMAT_ID)
    private AmountFormat amountFormat;

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

    public AmountFormat getAmountFormat() {
        return amountFormat;
    }

    public void setAmountFormat(AmountFormat amountFormat) {
        this.amountFormat = amountFormat;
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

    static class AmountFormat {

        @JsonProperty(SettingsConstants.AMOUNT_UNITS)
        private Integer numberDivider;

        @JsonProperty(SettingsConstants.MAX_FRACT_DIGITS)
        private Integer maxFractionDigits;

        @JsonProperty(SettingsConstants.DECIMAL_SYMBOL)
        private Character decimalSymbol;

        @JsonProperty(SettingsConstants.USE_GROUPING)
        private Boolean useGrouping;

        @JsonProperty(SettingsConstants.GROUP_SEPARATOR)
        private Character groupSeparator;

        @JsonProperty(SettingsConstants.GROUP_SIZE)
        private Integer groupSize;

        public Integer getNumberDivider() {
            return numberDivider;
        }

        public void setNumberDivider(Integer numberDivider) {
            this.numberDivider = numberDivider;
        }

        public Integer getMaxFractionDigits() {
            return maxFractionDigits;
        }

        public void setMaxFractionDigits(Integer maxFractionDigits) {
            this.maxFractionDigits = maxFractionDigits;
        }

        public Character getDecimalSymbol() {
            return decimalSymbol;
        }

        public void setDecimalSymbol(Character decimalSymbol) {
            this.decimalSymbol = decimalSymbol;
        }

        public Boolean getUseGrouping() {
            return useGrouping;
        }

        public void setUseGrouping(Boolean useGrouping) {
            this.useGrouping = useGrouping;
        }

        public Character getGroupSeparator() {
            return groupSeparator;
        }

        public void setGroupSeparator(Character groupSeparator) {
            this.groupSeparator = groupSeparator;
        }

        public Integer getGroupSize() {
            return groupSize;
        }

        public void setGroupSize(Integer groupSize) {
            this.groupSize = groupSize;
        }
    }
}

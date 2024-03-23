package org.digijava.kernel.ampapi.endpoints.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author Octavian Ciubotaru
 */
public class YearsForCalendar {

    @ApiModelProperty(example = "4")
    private Long calendarId;

    private List<YearWithRange> years;

    public YearsForCalendar(Long calendarId,
            List<YearWithRange> years) {
        this.calendarId = calendarId;
        this.years = years;
    }

    public Long getCalendarId() {
        return calendarId;
    }

    public List<YearWithRange> getYears() {
        return years;
    }
}

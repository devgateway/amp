package org.dgfoundation.amp.nireports.output;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.dgfoundation.amp.newreports.CalendarConverter;
import org.dgfoundation.amp.newreports.ReportSettings;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.helper.fiscalcalendar.BaseCalendar;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.translation.exotic.AmpDateFormatter;
import org.digijava.module.translation.exotic.AmpDateFormatterFactory;
import org.joda.time.DateTime;

/**
 * a formatter which converts dates in different calendars
 * @author Viorel Chihai
 *
 */
public class NiReportDateFormatter {
    
    final protected AmpDateFormatter ampDateFormatter;
    final protected CalendarConverter calendarConverter;
    
    public NiReportDateFormatter(ReportSettings settings, String dateDisplayFormat, CalendarConverter defaultCalendar) {
        this.calendarConverter = (settings != null && settings.getCalendar() != null) ? settings.getCalendar() : 
            defaultCalendar;
        this.ampDateFormatter = AmpDateFormatterFactory.getLocalizedFormatter(dateDisplayFormat);
    }
    
    public String formatDate(LocalDate date) {
        if (date != null) {
            if (calendarConverter != null && calendarConverter instanceof AmpFiscalCalendar) {
                AmpFiscalCalendar calendar = (AmpFiscalCalendar) calendarConverter;
                if (calendar.getBaseCal().equalsIgnoreCase(BaseCalendar.BASE_ETHIOPIAN.getValue())) {
                    DateTime convDate = FiscalCalendarUtil.convertFromGregorianDate(
                            Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()), calendar);
                    
                    return getEthiopianFormattedDate(convDate);
                }
            }

            return ampDateFormatter.format(date);
        }

        return "";
    }
    
    /**
     * Get the ethiopian date in the format dd/MM/yyyy
     * 
     * @param convDate
     * @return
     */
    private String getEthiopianFormattedDate(DateTime convDate) {
        return String.format("%02d/%02d/%d", 
                convDate.getDayOfMonth(), convDate.getMonthOfYear(), convDate.getYear());
    }
}

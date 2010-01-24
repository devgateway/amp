package org.digijava.module.gis.util;

import java.util.Date;
import java.text.SimpleDateFormat;

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
public class DateInterval {
    private Date start;
    private Date end;
    private static SimpleDateFormat dateFormat;

    static {
        dateFormat = new SimpleDateFormat("d/MMM/yyyy");
    }

    public DateInterval() {
    }

    public DateInterval(Date start, Date end) {
        this.start = start;
        this.end=end;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getStart() {
        return start;
    }

    public Long getStartTime() {
        return start.getTime();
    }

    public String getFormatedStartTime() {
            return dateFormat.format(start);
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Date getEnd() {
        return end;
    }

    public Long getEndTime() {
        return end.getTime();
    }

    public String getFormatedEndTime() {
            return dateFormat.format(end);
    }

}

package org.digijava.kernel.ampapi.endpoints.util.types;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.digijava.kernel.ampapi.endpoints.activity.InterchangeUtils;

/**
 * @author Octavian Ciubotaru
 */
public class ISO8601TimeStamp extends Date {

    public ISO8601TimeStamp(String value) {
        super(parseValue(value));
    }

    private static long parseValue(String value) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(InterchangeUtils.ISO8601_DATE_FORMAT);
            return format.parse(value).getTime();
        } catch (ParseException e) {
            throw new WebApplicationException(e, Response.Status.BAD_REQUEST);
        }
    }
}

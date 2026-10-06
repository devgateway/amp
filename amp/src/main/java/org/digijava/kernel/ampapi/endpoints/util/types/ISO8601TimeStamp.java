package org.digijava.kernel.ampapi.endpoints.util.types;

import org.digijava.kernel.ampapi.endpoints.common.CommonErrors;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.exception.AmpWebApplicationException;

import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Octavian Ciubotaru
 */
public class ISO8601TimeStamp extends Date {

    public ISO8601TimeStamp(String value) {
        super(parseValue(value));
    }

    private static long parseValue(String value) {
        if (value.isEmpty()) {
            // special case for jersey parameter handling, when passed in empty string we throw an exception, this will
            // not cancel execution and parameter represented by this type will be null
            throw new RuntimeException("No timestamp value for empty string.");
        }
        try {
            SimpleDateFormat format = new SimpleDateFormat(EPConstants.ISO8601_DATE_AND_TIME_FORMAT);
            return format.parse(value).getTime();
        } catch (ParseException e) {
            throw new AmpWebApplicationException(Response.Status.BAD_REQUEST, CommonErrors.INVALID_TIMESTAMP);
        }
    }
}

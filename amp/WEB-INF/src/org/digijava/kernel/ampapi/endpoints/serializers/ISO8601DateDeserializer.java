package org.digijava.kernel.ampapi.endpoints.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Important: this deserializer uses JVM default timezone.
 *
 * @author Octavian Ciubotaru
 */
public class ISO8601DateDeserializer extends StdScalarDeserializer<Date> {

    private SimpleDateFormat dateFormat = new SimpleDateFormat(EPConstants.ISO8601_DATE_FORMAT);

    public ISO8601DateDeserializer() {
        super(Date.class);
    }

    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (p.hasToken(JsonToken.VALUE_STRING)) {
            String str = p.getText().trim();
            if (str.length() == 0) {
                return (Date) getEmptyValue(ctxt);
            }
            synchronized (dateFormat) {
                try {
                    return dateFormat.parse(str);
                } catch (ParseException e) {
                    String msg = String.format("expected format \"%s\"", dateFormat.toPattern());
                    throw new InvalidFormatException(p, msg, str, Date.class);
                }
            }
        }
        return null;
    }
}

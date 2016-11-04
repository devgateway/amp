package org.dgfoundation.amp.newreports;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.std.SerializerBase;

/**
 * @author Octavian Ciubotaru
 */
public class AmpReportFiltersSerializer extends SerializerBase<AmpReportFilters> {

    public AmpReportFiltersSerializer() {
        super(AmpReportFilters.class);
    }

    @Override
    public void serialize(AmpReportFilters filters, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        filters.getFilterRules().forEach((el, v) -> {
//            jgen.writeFieldName(fieldNameFor(el));
//            jgen.writeObject();
        });
    }

    private String fieldNameFor(ReportElement reportElement) {
        if (ReportElement.ElementType.ENTITY.equals(reportElement.type)) {
            return reportElement.entity.getEntityName();
        } else {
            return reportElement.type.toString();
        }
    }
}

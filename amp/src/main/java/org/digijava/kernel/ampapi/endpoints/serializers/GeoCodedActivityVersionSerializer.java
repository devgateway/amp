package org.digijava.kernel.ampapi.endpoints.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.log4j.Logger;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.common.util.DateTimeUtil;
import org.digijava.module.editor.exception.EditorException;
import org.digijava.module.editor.util.DbUtil;
import org.digijava.module.translation.exotic.AmpDateFormatter;
import org.digijava.module.translation.exotic.AmpDateFormatterFactory;

import java.io.IOException;

/**
 * GeoCoded Activity json serializer
 *
 * @author Viorel Chihai
 */
public class GeoCodedActivityVersionSerializer extends JsonSerializer<AmpActivityVersion> {

    private static Logger logger = Logger.getLogger(GeoCodedActivityVersionSerializer.class);

    @Override
    public void serialize(AmpActivityVersion activity, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {

        Site site = TLSUtils.getSite();
        String lang = TLSUtils.getLangCode();

        jgen.writeNumberField("activity_id", activity.getAmpActivityId());
        jgen.writeStringField("project_title", activity.getName());
        jgen.writeStringField("amp_id", activity.getAmpId());
        jgen.writeStringField("updated_date", getDefaultDateFormatter().format(activity.getUpdatedDate()));
        try {
            jgen.writeStringField("description", DbUtil.getEditorBody(site, activity.getDescription(), lang));
        } catch (EditorException e) {
            logger.error("Unable to use editor", e);
            jgen.writeStringField("description", null);
        }
    }

    @Override
    public boolean isUnwrappingSerializer() {
        return true;
    }

    public AmpDateFormatter getDefaultDateFormatter() {
        return AmpDateFormatterFactory.getLocalizedFormatter(DateTimeUtil.getGlobalPattern());
    }

}

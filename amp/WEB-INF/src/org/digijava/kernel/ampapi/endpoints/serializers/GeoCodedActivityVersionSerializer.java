package org.digijava.kernel.ampapi.endpoints.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.common.util.DateTimeUtil;
import org.digijava.module.editor.exception.EditorException;
import org.digijava.module.editor.util.DbUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * GeoCoded Activity json serializer
 *
 * @author Viorel Chihai
 */
public class GeoCodedActivityVersionSerializer extends JsonSerializer<AmpActivityVersion> {

    private SimpleDateFormat dateFormat = new SimpleDateFormat(EPConstants.ISO8601_DATE_FORMAT);

    private static Logger logger = Logger.getLogger(GeoCodedActivityVersionSerializer.class);

    @Override
    public void serialize(AmpActivityVersion activity, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {

        List<String> projectNumberIds = activity.getInternalIds()
                .stream().map(id -> id.getInternalId())
                .collect(Collectors.toList());

        Site site = RequestUtils.getSite(TLSUtils.getRequest());
        String lang = RequestUtils.getNavigationLanguage(TLSUtils.getRequest()).getCode();

        jgen.writeNumberField("activity_id", activity.getAmpActivityId());
        jgen.writeStringField("project_title", activity.getName());
        jgen.writeStringField("project_number", String.join(",", projectNumberIds));
        jgen.writeStringField("project_date",
                DateTimeUtil.formatISO8601DateTimestamp(activity.getCreatedDate(), false));
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

}

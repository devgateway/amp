package org.digijava.kernel.ampapi.endpoints.sync;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.digijava.kernel.services.sync.model.IncrementalListDiff;
import org.digijava.kernel.services.sync.model.ListDiff;
import org.digijava.kernel.services.sync.model.SystemDiff;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Octavian Ciubotaru
 */

public class TestAmpOfflineDiff {

    private static final String X = "/api/schemas/systemDiff.schema.json";

    @Test
    public void testSchema() throws IOException, ProcessingException {
        SystemDiff diff = new SystemDiff();
        diff.setTimestamp(new Date());
        diff.setWorkspaceMembers(new IncrementalListDiff<>(Collections.emptyList(), Arrays.asList(1L, 2L)));
        diff.setUsers(new IncrementalListDiff<>(Collections.emptyList(), Collections.singletonList(122L)));
        diff.setActivities(new IncrementalListDiff<>(Collections.singletonList("a"), Arrays.asList("b", "c")));
        diff.setTranslations(new ListDiff<>());

        ObjectMapper objectMapper = new ObjectMapper();
        String diffAsJsonString = objectMapper.writeValueAsString(diff);

        assertJsonMatchesSchema(X, diffAsJsonString);
    }

    private void assertJsonMatchesSchema(String schemaResource, String json) throws ProcessingException, IOException {
        JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
        JsonSchema schema = factory.getJsonSchema(JsonLoader.fromResource(schemaResource));
        ProcessingReport report = schema.validate(JsonLoader.fromString(json));
        Assert.assertTrue(String.format("Json: %s\nValidate report: %s", json, report.toString()), report.isSuccess());
    }
}

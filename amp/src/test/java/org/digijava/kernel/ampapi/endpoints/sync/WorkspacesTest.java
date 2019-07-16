/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.sync;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.digijava.kernel.AbstractIntegrationTest;
import org.digijava.module.aim.util.TeamUtil;
import org.junit.Test;

/**
 * @author Nadejda Mandrescu
 */
public class WorkspacesTest extends AbstractIntegrationTest {
    
    private ObjectMapper mapper = new ObjectMapper();
    
    @Test
    public void testFilteredWorkspaces() throws IOException {
        String expected = "[{\"id\":4,\"name\":{\"ru\":null,\"en\":\"test workspace\",\"fr\":null},\"description\":{\"ru\":null,\"en\":\" \",\"fr\":null},\"workspace-group\":\"Government\",\"workspace-lead-id\":12,\"add-activity\":true,\"is-computed\":false,\"hide-draft\":false,\"is-cross-team-validation\":false,\"use-filter\":true,\"access-type\":\"Team\"},{\"id\":5,\"name\":{\"ru\":null,\"en\":\"SSC workspace\",\"fr\":null},\"description\":{\"ru\":null,\"en\":\"SSC activities\",\"fr\":null},\"workspace-group\":\"Donor\",\"workspace-lead-id\":14,\"add-activity\":true,\"is-computed\":false,\"hide-draft\":false,\"is-cross-team-validation\":false,\"use-filter\":true,\"access-type\":\"Team\",\"fm-template-id\":2,\"workspace-prefix\":\"SSC_\"},{\"id\":6,\"name\":{\"ru\":null,\"en\":\"Ethiopia Calendar WS\",\"fr\":null},\"description\":{\"ru\":null,\"en\":\" \",\"fr\":null},\"workspace-group\":\"Government\",\"workspace-lead-id\":15,\"add-activity\":false,\"is-computed\":true,\"hide-draft\":false,\"is-cross-team-validation\":false,\"use-filter\":true,\"access-type\":\"Team\",\"permission-strategy\":\"Full Access\",\"workspace-filters\":{\"computedYear\":2015,\"justSearch\":false,\"searchMode\":\"0\",\"selectedActivityPledgesSettings\":-1,\"showArchived\":false,\"workspaceonly\":false,\"workspaces\":[4]}}]";
        String actual = mapper.writeValueAsString(TeamUtil.getAllTeams(false, false));
        assertReflectionEquals(expected, actual);
    }

}

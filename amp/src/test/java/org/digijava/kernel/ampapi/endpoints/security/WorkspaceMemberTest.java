package org.digijava.kernel.ampapi.endpoints.security;

import static org.junit.Assert.assertTrue;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.digijava.kernel.AbstractIntegrationTest;
import org.digijava.kernel.ampapi.endpoints.security.dto.WorkspaceMember;
import org.digijava.kernel.ampapi.endpoints.security.services.WorkspaceMemberService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Nadejda Mandrescu
 */
public class WorkspaceMemberTest extends AbstractIntegrationTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private WorkspaceMemberService workspaceMemberService;
    
    @Test
    public void testValidUser() throws IOException {
        String wsMemberStr = "[  {\"id\": 13,\"user-id\": 12,\"workspace-id\": 4,\"role-id\": 2  },  {\"id\": 16,\"user-id\": 12,\"workspace-id\": 6,\"role-id\": 2  }]";
        WorkspaceMember[] expected = mapper.readValue(wsMemberStr, WorkspaceMember[].class);
        List<WorkspaceMember> actual = workspaceMemberService.getWorkspaceMembers(Arrays.asList(13L, 16L));
        assertReflectionEquals(expected, actual.toArray());
    }
    
   @Test
    public void testInvalidWorkspaceMemberIdEmptyListNoException() {
        assertTrue(workspaceMemberService.getWorkspaceMembers(Arrays.asList(1L)).isEmpty());
    }
    
    @Test
    public void testEmptyWorkspaceMemberIdEmptyListNoException() {
        assertTrue(workspaceMemberService.getWorkspaceMembers(Collections.emptyList()).isEmpty());
    }
}

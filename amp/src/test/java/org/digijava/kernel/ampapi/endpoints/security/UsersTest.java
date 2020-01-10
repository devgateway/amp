package org.digijava.kernel.ampapi.endpoints.security;

import static org.junit.Assert.assertTrue;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

import java.io.IOException;
import java.util.Collections;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.digijava.kernel.AbstractIntegrationTest;
import org.digijava.kernel.ampapi.endpoints.security.dto.User;
import org.digijava.kernel.ampapi.endpoints.security.services.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Nadejda Mandrescu
 */
public class UsersTest extends AbstractIntegrationTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private UserService userService;
    
    @Test
    public void testValidUser() throws IOException {
        String userStr = "{    \"id\": 3,    \"email\": \"atl@amp.org\",    \"first-name\": \"ATL\",    \"last-name\": \"ATL\",    \"is-banned\": false,    \"is-active\": false,    \"is-pledger\": true,    \"is-admin\": false,    \"lang-iso2\": \"en\",    \"country-iso2\": \"md\",    \"group-keys\": [      \"EDT\",      \"MEM\",      \"TRN\"    ]  }";
        User expected = mapper.readValue(userStr, User.class);
        User actual = userService.getUserInfo(Collections.singletonList(3L)).get(0);
        assertReflectionEquals(expected, actual);
    }
    
    @Test
    public void testUserPasswordChanged() throws IOException {
        String userStr = "{    \"id\": 12,    \"email\": \"idea@amp.org\",    \"first-name\": \"IDEA\",    \"last-name\": \"Importer\",    \"password-changed-at\": \"2016-12-27T14:09:11.095+0200\",    \"is-banned\": false,    \"is-active\": false,    \"is-pledger\": false,    \"is-admin\": false,    \"lang-iso2\": \"en\",    \"country-iso2\": \"md\",    \"org-type-id\": 38,    \"org-group-id\": 17,    \"org-id\": 21378,    \"group-keys\": [      \"EDT\",      \"MEM\"    ]  }";
        User expected = mapper.readValue(userStr, User.class);
        User actual = userService.getUserInfo(Collections.singletonList(12L)).get(0);
        assertReflectionEquals(expected, actual);
    }
    
    @Test
    public void testInvalidUserIdEmptyListNoException() {
        assertTrue(userService.getUserInfo(Collections.singletonList(1L)).isEmpty());
    }
    
    @Test
    public void testEmptyUserIdEmptyListNoException() {
        assertTrue(!userService.getUserInfo(Collections.emptyList()).isEmpty());
    }
}

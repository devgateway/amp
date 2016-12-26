/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.security;

import static org.junit.Assert.assertTrue;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.digijava.kernel.ampapi.endpoints.security.dto.User;
import org.junit.Test;
 
/**
 * @author Nadejda Mandrescu
 */
public class UsersTest {
    private ObjectMapper mapper;
    
    public UsersTest() {
        this.mapper = new ObjectMapper(); 
    }
    
    @Test
    public void testValidUser() throws JsonParseException, JsonMappingException, IOException {
        String userStr = "{    \"id\": 3,    \"email\": \"atl@amp.org\",    \"first-name\": \"ATL\",    \"last-name\": \"ATL\",    \"is-banned\": false,    \"is-active\": false,    \"is-pledger\": true,    \"is-admin\": false,    \"lang-iso2\": \"en\",    \"country-iso2\": \"md\",    \"group-keys\": [      \"TRN\",      \"MEM\",      \"EDT\"    ]  }";
        User expected = mapper.readValue(userStr, User.class);
        User actual = (new Security()).getUsersInfo("3").get(0);
        assertReflectionEquals(expected, actual);
    }
    
    @Test
    public void testUserPasswordChanged() throws JsonParseException, JsonMappingException, IOException {
        String userStr = "{    \"id\": 12,    \"email\": \"idea@amp.org\",    \"first-name\": \"IDEA\",    \"last-name\": \"Importer\",    \"password-changed-at\": \"2016-12-26T23:31:35.519+02:00\",    \"is-banned\": false,    \"is-active\": false,    \"is-pledger\": false,    \"is-admin\": false,    \"lang-iso2\": \"en\",    \"country-iso2\": \"md\",    \"org-type-id\": 38,    \"org-group-id\": 17,    \"org-id\": 21378,    \"group-keys\": [      \"MEM\",      \"EDT\"    ]  }";
        User expected = mapper.readValue(userStr, User.class);
        User actual = (new Security()).getUsersInfo("12").get(0);
        assertReflectionEquals(expected, actual);
    }
    
    @Test
    public void testInvalidUserIdEmptyListNoException() {
        assertTrue((new Security()).getUsersInfo("1").isEmpty());
    }

}

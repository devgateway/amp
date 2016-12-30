/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.security;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.dgfoundation.amp.AllTests_amp;
import org.dgfoundation.amp.testutils.AmpTestCase;
import org.digijava.kernel.ampapi.endpoints.security.dto.User;
import org.digijava.kernel.ampapi.endpoints.util.types.ListOfLongs;
import org.junit.Test;
 
/**
 * @author Nadejda Mandrescu
 */
public class UsersTest extends AmpTestCase {
    private ObjectMapper mapper;
    private Security securityEP; 
    
    public UsersTest() {
        super("UsersTest");
        this.mapper = new ObjectMapper();
        this.securityEP = new Security();
    }
    
    @Test
    public void testValidUser() throws IOException {
        String userStr = "{    \"id\": 3,    \"email\": \"atl@amp.org\",    \"first-name\": \"ATL\",    \"last-name\": \"ATL\",    \"is-banned\": false,    \"is-active\": false,    \"is-pledger\": true,    \"is-admin\": false,    \"lang-iso2\": \"en\",    \"country-iso2\": \"md\",    \"group-keys\": [      \"EDT\",      \"MEM\",      \"TRN\"    ]  }";
        User expected = mapper.readValue(userStr, User.class);
        User actual = securityEP.getUsersInfo(new ListOfLongs("3")).get(0);
        assertReflectionEquals(expected, actual);
    }
    
    @Test
    public void testUserPasswordChanged() throws IOException {
        String userStr = "{    \"id\": 12,    \"email\": \"idea@amp.org\",    \"first-name\": \"IDEA\",    \"last-name\": \"Importer\",    \"password-changed-at\": \"2016-12-27T14:09:11.095+02:00\",    \"is-banned\": false,    \"is-active\": false,    \"is-pledger\": false,    \"is-admin\": false,    \"lang-iso2\": \"en\",    \"country-iso2\": \"md\",    \"org-type-id\": 38,    \"org-group-id\": 17,    \"org-id\": 21378,    \"group-keys\": [      \"EDT\",      \"MEM\"    ]  }";
        User expected = mapper.readValue(userStr, User.class);
        User actual = securityEP.getUsersInfo(new ListOfLongs("12")).get(0);
        assertReflectionEquals(expected, actual);
    }
    
    @Test
    public void testInvalidUserIdEmptyListNoException() {
        assertTrue(securityEP.getUsersInfo(new ListOfLongs("1")).isEmpty());
    }
    
    @Test
    public void testEmptyUserIdEmptyListNoException() {
        assertTrue(securityEP.getUsersInfo(new ListOfLongs("")).isEmpty());
    }

    @Override
    public void setUp() {
        AllTests_amp.setUp();
    }
}

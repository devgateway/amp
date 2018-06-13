package org.digijava.kernel.ampapi.endpoints.activity.validators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.activity.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityImporter;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Viorel Chihai
 */
public class RegexPatternValidatorTest {

    private static final String VALUE_FIELD = "value";

    private static final String VALID_PHONE = "987-123-4567";
    private static final String INVALID_FAX = "987-aa123-4567";
    private static final String INVALID_EMAIL = "hello@fdsafdsa";

    private ActivityImporter importer;
    private APIField phoneFieldDescription;
    private APIField faxFieldDescription;
    private APIField emailFieldDescription;
    

    @Before
    public void setUp() throws Exception {
        importer = mock(ActivityImporter.class);

        phoneFieldDescription = new APIField();
        phoneFieldDescription.setFieldName(VALUE_FIELD);
        phoneFieldDescription.setImportable(true);
        phoneFieldDescription.setRegexPattern(ActivityEPConstants.REGEX_PATTERN_PHONE);
        
        faxFieldDescription = new APIField();
        faxFieldDescription.setFieldName(VALUE_FIELD);
        faxFieldDescription.setImportable(true);
        faxFieldDescription.setRegexPattern(ActivityEPConstants.REGEX_PATTERN_PHONE);
        
        emailFieldDescription = new APIField();
        emailFieldDescription.setFieldName(VALUE_FIELD);
        emailFieldDescription.setImportable(true);
        emailFieldDescription.setRegexPattern(ActivityEPConstants.REGEX_PATTERN_EMAIL);
    }

    @Test
    public void testRegexPatternPhone() throws Exception {
        Map<String, Object> newFieldParent = new HashMap<>();
        newFieldParent.put(VALUE_FIELD, VALID_PHONE);

        RegexPatternValidator regexValidator = new RegexPatternValidator();

        assertTrue("Value is valid",
                regexValidator.isValid(importer, newFieldParent, null, phoneFieldDescription, VALUE_FIELD));
    }

    @Test
    public void testRegexPatternFax() throws Exception {
        Map<String, Object> newFieldParent = new HashMap<>();
        newFieldParent.put(VALUE_FIELD, INVALID_FAX);

        RegexPatternValidator regexValidator = new RegexPatternValidator();

        assertFalse("Value must be invalid",
                regexValidator.isValid(importer, newFieldParent, null, faxFieldDescription, VALUE_FIELD));
        assertEquals(ActivityErrors.FIELD_INVALID_VALUE, regexValidator.getErrorMessage());
    }
    
    @Test
    public void testRegexPatternEmail() throws Exception {
        Map<String, Object> newFieldParent = new HashMap<>();
        newFieldParent.put(VALUE_FIELD, INVALID_EMAIL);

        RegexPatternValidator regexValidator = new RegexPatternValidator();

        assertFalse("Value must be invalid",
                regexValidator.isValid(importer, newFieldParent, null, emailFieldDescription, VALUE_FIELD));
        assertEquals(ActivityErrors.FIELD_INVALID_VALUE, regexValidator.getErrorMessage());
    }
}

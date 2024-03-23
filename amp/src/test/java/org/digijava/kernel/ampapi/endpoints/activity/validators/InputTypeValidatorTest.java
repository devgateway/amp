package org.digijava.kernel.ampapi.endpoints.activity.validators;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityImporter;
import org.digijava.kernel.ampapi.endpoints.activity.TranslationSettings;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIType;
import org.digijava.kernel.ampapi.endpoints.activity.field.FieldType;
import org.digijava.kernel.util.SiteUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Nadejda Mandrescu
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ SiteUtils.class })
public class InputTypeValidatorTest {

    private static final String TRANSLATABLE_FIELD = "translatable_field";
    private static final Set<String> LOCALES = ImmutableSet.of("en", "fr");
    private static final Map<String, String> MULTILINGUAL_INPUT =  ImmutableMap.of(
            "en", "EN Text",
            "fr", "FR Text"
            );

    private ActivityImporter importer;
    private APIField translatableField;

    @Before
    public void setUp() throws Exception {
        PowerMockito.mockStatic(SiteUtils.class);
        when(SiteUtils.getUserLanguagesCodes(Matchers.any())).thenReturn(LOCALES);

        translatableField = new APIField();
        translatableField.setFieldName(TRANSLATABLE_FIELD);
        translatableField.setImportable(true);
        translatableField.setApiType(new APIType(String.class, FieldType.STRING));

        // List<APIField> apiFields = Arrays.asList(translatableField);

        importer = mock(ActivityImporter.class);
    }

    @Test
    public void testValidMultilingualInput() {
        mockTranslatableField("en", "fr", LOCALES, true);

        Map<String, Object> root = new HashMap<>();
        root.put(TRANSLATABLE_FIELD, MULTILINGUAL_INPUT);

        assertTrue("Multilingual input must be valid",
                isValid(root, translatableField, TRANSLATABLE_FIELD));
    }

    @Test
    public void testValidMultilingualInputAsNull() {
        mockTranslatableField("en", "fr", LOCALES, true);

        Map<String, Object> root = new HashMap<>();
        root.put(TRANSLATABLE_FIELD, null);

        assertTrue("Null must be a valid multilingual input",
                isValid(root, translatableField, TRANSLATABLE_FIELD));
    }

    @Test
    public void testValidMultilingualInputAsEmpty() {
        mockTranslatableField("en", "fr", LOCALES, true);

        Map<String, Object> root = new HashMap<>();
        root.put(TRANSLATABLE_FIELD, Collections.EMPTY_MAP);

        assertTrue("Empty map must be a valid multilingual input",
                isValid(root, translatableField, TRANSLATABLE_FIELD));
    }

    @Test
    public void testInvalidMultilingualInputAsString() {
        mockTranslatableField("en", "fr", LOCALES, true);

        Map<String, Object> root = new HashMap<>();
        root.put(TRANSLATABLE_FIELD, "anything");

        assertFalse("String must not be allowed as multingual input",
                isValid(root, translatableField, TRANSLATABLE_FIELD));
    }

    @Test
    public void testInvalidMultilingualInputAsObject() {
        mockTranslatableField("en", "fr", LOCALES, true);

        Map<String, Object> root = new HashMap<>();
        root.put(TRANSLATABLE_FIELD, Collections.EMPTY_SET);

        assertFalse("Other objects must not be allowed as multingual input",
                isValid(root, translatableField, TRANSLATABLE_FIELD));
    }

    @Test
    public void testInvalidMultilingualInputLocales() {
        mockTranslatableField("en", "fr", LOCALES, true);

        Map<String, Object> root = new HashMap<>();
        root.put(TRANSLATABLE_FIELD, ImmutableMap.of("ro", "RO text"));

        assertTrue("Only allowed locales must be accepted",
                isValid(root, translatableField, TRANSLATABLE_FIELD));
    }

    @Test
    public void testInvalidMultilingualInputLocaleType() {
        mockTranslatableField("en", "fr", LOCALES, true);

        Map<String, Object> root = new HashMap<>();
        root.put(TRANSLATABLE_FIELD, ImmutableMap.of(Collections.EMPTY_SET, "RO text"));

        assertFalse("Locales used by multilingual input must be of string type",
                isValid(root, translatableField, TRANSLATABLE_FIELD));
    }

    @Test
    public void testInvalidMultilingualInputTranslationType() {
        mockTranslatableField("en", "fr", LOCALES, true);

        Map<String, Object> root = new HashMap<>();
        root.put(TRANSLATABLE_FIELD, ImmutableMap.of("en", Collections.EMPTY_SET));

        assertFalse("Translated text used by multilingual input must be of string type",
                isValid(root, translatableField, TRANSLATABLE_FIELD));
    }

    @Test
    public void testValidNonMultilingualInput() {
        mockTranslatableField("en", "fr", LOCALES, false);

        Map<String, Object> root = new HashMap<>();
        root.put(TRANSLATABLE_FIELD, "simple text");

        assertTrue("Non-multilingual input must be valid",
                isValid(root, translatableField, TRANSLATABLE_FIELD));
    }

    @Test
    public void testValidNonMultilingualInputAsNull() {
        mockTranslatableField("en", "fr", LOCALES, false);

        Map<String, Object> root = new HashMap<>();
        root.put(TRANSLATABLE_FIELD, null);

        assertTrue("Non-multilingual null input must be valid",
                isValid(root, translatableField, TRANSLATABLE_FIELD));
    }

    @Test
    public void testInvalidNonMultilingualInputAsMultilingualInput() {
        mockTranslatableField("en", "fr", LOCALES, false);

        Map<String, Object> root = new HashMap<>();
        root.put(TRANSLATABLE_FIELD, MULTILINGUAL_INPUT);

        assertFalse("Multilingual input disallowed when multingual is off",
                isValid(root, translatableField, TRANSLATABLE_FIELD));
    }

    @Test
    public void testInvalidNonMultilingualInputAsObject() {
        mockTranslatableField("en", "fr", LOCALES, false);

        Map<String, Object> root = new HashMap<>();
        root.put(TRANSLATABLE_FIELD, Collections.EMPTY_SET);

        assertFalse("Only string type allowed for translatable field when multilingual is off",
                isValid(root, translatableField, TRANSLATABLE_FIELD));
    }

    private void mockTranslatableField(String currentLangCode, String defaultLangCode,
            Set<String> trnLocaleCodes, boolean multilingual) {
        translatableField.setTranslatable(multilingual);

        TranslationSettings trnSettings =
                new TranslationSettings(currentLangCode, defaultLangCode, trnLocaleCodes, multilingual);
        when(importer.getTrnSettings()).thenReturn(trnSettings);
    }

    private boolean isValid(Map<String, Object> root, APIField fieldDesc, String fieldPath) {
        InputTypeValidator validator = new InputTypeValidator();
        return validator.isValid(importer, root, fieldDesc, fieldPath);
    }

}

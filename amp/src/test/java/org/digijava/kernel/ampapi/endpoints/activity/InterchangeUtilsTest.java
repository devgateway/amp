package org.digijava.kernel.ampapi.endpoints.activity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.common.TranslatorService;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpContentTranslation;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.gateperm.core.Permissible;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/**
 * @author Octavian Ciubotaru
 */
public class InterchangeUtilsTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Rule
    public AMPRequestRule ampRequestRule = new AMPRequestRule();

    @Mock private TranslatorService translatorService;

    @Before
    public void setUp() throws Exception {
        when(translatorService.loadFieldTranslations(any(), any(), any())).then(invocation -> Arrays.asList(
                acm("en", "ct+en+" + invocation.getArguments()[1] + invocation.getArguments()[2]),
                acm("fr", "ct+fr+" + invocation.getArguments()[1] + invocation.getArguments()[2]),
                acm("ru", "ct+ru+" + invocation.getArguments()[1] + invocation.getArguments()[2])));

        when(translatorService.translateText(any())).then(invocation -> "tr+" + invocation.getArguments()[0]);

        when(translatorService.getEditorBodyEmptyInclude(any(), any(), any()))
                .then(invocation -> "ed+" + invocation.getArguments()[2] + "+" + invocation.getArguments()[1]);

        InterchangeUtils.setTranslatorService(translatorService);
    }

    private static AmpContentTranslation acm(String lang, String value) {
        AmpContentTranslation acm = new AmpContentTranslation();
        acm.setLocale(lang);
        acm.setTranslation(value);
        return acm;
    }

    @Test
    public void testTranslateTranslatable() throws Exception {
        assertEquals("as-is",
                translateFieldValue(AmpActivityFields.class, "name", "as-is", null));
    }

    @Test
    public void testTranslateTranslatableInMultilingual() throws Exception {
        ampRequestRule.enableMultilingual();

        assertEquals(translationsEnFr("ct+en+1name", "ct+fr+1name"),
                translateFieldValue(AmpActivityFields.class, "name", "test", 1L));
    }

    @Test
    public void testTranslateForDate() throws Exception {
        Date date = new Date();
        assertEquals(InterchangeUtils.formatISO8601Date(date),
                translateFieldValue(AmpActivityFields.class, "actualApprovalDate", date, null));
    }

    @Test
    public void testTranslateForInt() throws Exception {
        assertEquals(1,
                translateFieldValue(AmpActivityFields.class, "budget", 1, null));
    }

    @Test
    public void testTranslateNoTranslation() throws Exception {
        assertEquals("as-is",
                translateFieldValue(AmpActivityFields.class, "vote", "as-is", null));
    }

    @Test
    public void testTranslateTextEditor() throws Exception {
        assertEquals("ed+en+test",
                translateFieldValue(AmpActivityFields.class, "projectImpact", "test", null));
    }

    @Test
    public void testTranslateTextEditorMultilingual() throws Exception {
        ampRequestRule.enableMultilingual();

        assertEquals(translationsEnFr("ed+en+test", "ed+fr+test"),
                translateFieldValue(AmpActivityFields.class, "projectImpact", "test", null));
    }

    @Test
    public void testTranslateCategoryValue() throws Exception {
        assertEquals("tr+test",
                translateFieldValue(AmpCategoryValue.class, "value", "test", null));
    }

    @Test
    public void testTranslateWithoutId() throws Exception {
        ampRequestRule.enableMultilingual();

        assertEquals(translationsEnFr(null, null),
                translateFieldValue(AmpActivityFields.class, "name", "test", null));
    }

    @Test
    public void testTranslateBlankString() throws Exception {
        assertNull(translateFieldValue(AmpActivityFields.class, "name", "", null));
    }

    @Test
    public void testTranslateCategoryValueBlank() throws Exception {
        when(translatorService.translateText(any())).thenReturn("");

        assertNull(translateFieldValue(AmpCategoryValue.class, "value", "test", null));
    }

    private Object translateFieldValue(Class<?> parentClass, String fieldName, Object fieldValue, Long parentObjectId)
            throws Exception {
        Field field = parentClass.getDeclaredField(fieldName);
        return InterchangeUtils.getTranslationValues(field, parentClass, fieldValue, parentObjectId);
    }

    private Map<String, String> translationsEnFr(String enTranslation, String frTranslation) {
        Map<String, String> translations = new HashMap<>();
        translations.put("en", enTranslation);
        translations.put("fr", frTranslation);
        return translations;
    }

    @Test
    public void testGetFieldByLongNameNonexistent() throws Exception {
        assertEquals(null,
                InterchangeUtils.getFieldByLongName("nonexistent"));
    }

    @Test
    public void testGetFieldByLongNameName() throws Exception {
        assertEquals(AmpActivityFields.class.getDeclaredField("name"),
                InterchangeUtils.getFieldByLongName("project_title"));
    }

    @Test @Ignore
    // FIXME fails because of a bug in deunderscorify
    public void testGetFieldByLongNameTeamName() throws Exception {
        assertEquals(AmpTeam.class.getDeclaredField("name"),
                InterchangeUtils.getFieldByLongName("team~name"));
    }

    @Test
    public void testGetFieldByLongNameCreatedByUser() throws Exception {
        assertEquals(AmpTeamMember.class.getDeclaredField("user"),
                InterchangeUtils.getFieldByLongName("created_by~user"));
    }

    @Test
    public void testGetFieldByLongNameNonexistentWithChildren() throws Exception {
        assertNull(InterchangeUtils.getFieldByLongName("nonexistent~name"));
    }

    @Test(expected = NullPointerException.class)
    public void testGetConfigValueNoAnnotation() throws Exception {
        InterchangeUtils.getConfigValue("project_title", AmpActivityFields.class.getDeclaredField("name"));
    }

    @Test
    public void testGetConfigValueDiscriminatedNoMatch() throws Exception {
        Field sectorsField = AmpActivityFields.class.getDeclaredField("sectors");
        assertNull(InterchangeUtils.getConfigValue("not_sector", sectorsField));
    }

    @Test
    public void testGetConfigValueDiscriminatedMatches() throws Exception {
        Field sectorsField = AmpActivityFields.class.getDeclaredField("sectors");
        assertEquals("Primary", InterchangeUtils.getConfigValue("primary_sectors", sectorsField));
    }

    @Test
    public void testIsAmpActivityVersion() throws Exception {
        // FIXME very high probability of incorrect behaviour
        assertTrue(InterchangeUtils.isAmpActivityVersion(Object.class));
        assertTrue(InterchangeUtils.isAmpActivityVersion(Permissible.class));
        assertTrue(InterchangeUtils.isAmpActivityVersion(AmpActivityFields.class));
        assertTrue(InterchangeUtils.isAmpActivityVersion(AmpActivityVersion.class));
        assertFalse(InterchangeUtils.isAmpActivityVersion(String.class));
        assertFalse(InterchangeUtils.isAmpActivityVersion(AmpActivity.class));
    }

    @Test
    public void testGetPotentiallyDiscriminatedField() throws Exception {
        assertEquals(AmpActivityFields.class.getDeclaredField("name"),
                InterchangeUtils.getPotentiallyDiscriminatedField(AmpActivityFields.class, "project_title"));

        assertEquals(AmpActivityFields.class.getDeclaredField("sectors"),
                InterchangeUtils.getPotentiallyDiscriminatedField(AmpActivityFields.class, "primary_sectors"));

        assertNull(InterchangeUtils.getPotentiallyDiscriminatedField(AmpActivityFields.class, "nonexisting"));
    }

    @Test
    public void testGetFieldValueFromJsonSimple() throws Exception {
        JsonBean activity = new JsonBean();
        activity.set("name", "Activity Name");
        assertEquals("Activity Name", InterchangeUtils.getFieldValuesFromJsonActivity(activity, "name"));
    }

    @Test
    public void testGetFieldValueFromJsonNested() throws Exception {
        JsonBean activity = new JsonBean();
        JsonBean nestedObj = new JsonBean();
        nestedObj.set("field", "Nested Value");
        activity.set("nested", nestedObj);
        assertEquals("Nested Value", InterchangeUtils.getFieldValuesFromJsonActivity(activity, "nested~field"));
    }

    @Test
    public void testGetFieldValueFromJsonNestedMissing() throws Exception {
        JsonBean activity = new JsonBean();
        assertEquals(null, InterchangeUtils.getFieldValuesFromJsonActivity(activity, "nested~field"));
    }

    @Test
    public void testGetFieldValueFromJsonNestedWrongType() throws Exception {
        JsonBean activity = new JsonBean();
        activity.set("nested", new Object());
        assertEquals(null, InterchangeUtils.getFieldValuesFromJsonActivity(activity, "nested~field"));
    }

    @Test
    public void testFormatDate() throws Exception {
        assertEquals("1973-11-26T03:52:03.123+0300", InterchangeUtils.formatISO8601Date(new Date(123123123123L)));
    }

    @Test
    public void testFormatDateNullInput() throws Exception {
        assertNull(InterchangeUtils.formatISO8601Date(null));
    }

    @Test
    public void testParseDate() throws Exception {
        assertEquals(new Date(124124124124L), InterchangeUtils.parseISO8601Date("1973-12-07T17:55:24.124+0300"));
    }

    @Test
    public void testParseDateWrongFormat() throws Exception {
        assertNull(InterchangeUtils.parseISO8601Date("xyz"));
    }

    @Test
    public void testParseDateNullInput() throws Exception {
        assertNull(InterchangeUtils.parseISO8601Date(null));
    }
}

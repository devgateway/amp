package org.digijava.kernel.ampapi.endpoints.activity;

import org.digijava.kernel.ampapi.endpoints.common.TranslatorService;
import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.dbentity.AmpContentTranslation;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.common.util.DateTimeUtil;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

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

        ActivityTranslationUtils.setTranslatorService(translatorService);
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
        return ActivityTranslationUtils.getTranslationValues(field, parentClass, fieldValue, parentObjectId);
    }

    private Map<String, String> translationsEnFr(String enTranslation, String frTranslation) {
        Map<String, String> translations = new HashMap<>();
        translations.put("en", enTranslation);
        translations.put("fr", frTranslation);
        return translations;
    }

    @Test
    public void testFormatTimestamp() throws Exception {
        assertEquals("1973-11-26T00:52:03.123+0000", DateTimeUtil.formatISO8601Timestamp(new Date(123123123123L)));
    }

    @Test
    public void testFormatDate() throws Exception {
        Date date = DateTime.now().withDate(1973, 11, 26).withTimeAtStartOfDay().toDate();
        assertEquals("1973-11-26", DateTimeUtil.formatISO8601Date(date));
    }

    @Test
    public void testFormatDateNullInput() throws Exception {
        assertNull(DateTimeUtil.formatISO8601Date(null));
    }

    @Test
    public void testParseTimestamp() {
        assertEquals(new Date(124124124124L), DateTimeUtil.parseISO8601Timestamp("1973-12-07T17:55:24.124+0300"));
    }

    /**
     * We need to assure that the date is saved correctly in DB.
     * The date values are stored in DB with midnight time (00:00:00.000) and JDBC uses the local time zone to store it.
     */
    @Test
    public void testParseDate() {
        Date date = DateTime.now().withDate(1973, 12, 7).withTimeAtStartOfDay().toDate();
        assertEquals(date, DateTimeUtil.parseISO8601Date("1973-12-07"));
    }

    @Test(expected = RuntimeException.class)
    public void testParseDateWrongFormat() {
        DateTimeUtil.parseISO8601Date("xyz");
    }

    @Test(expected = RuntimeException.class)
    public void testParseDateWrongLength() {
        DateTimeUtil.parseISO8601Date("2019-02-08x");
    }

    @Test(expected = RuntimeException.class)
    public void testParseTimestampWrongLength() {
        DateTimeUtil.parseISO8601Timestamp("1973-12-07T17:55:24.124+0300xyz");
    }

    @Test
    public void testParseDateNullInput() {
        assertNull(DateTimeUtil.parseISO8601Date(null));
    }
}

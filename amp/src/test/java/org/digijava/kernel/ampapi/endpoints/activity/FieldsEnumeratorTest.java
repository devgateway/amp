package org.digijava.kernel.ampapi.endpoints.activity;

import static org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants.FIELD_ALWAYS_REQUIRED;
import static org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants.FIELD_NOT_REQUIRED;
import static org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants.FIELD_TYPE_LIST;
import static org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants.FIELD_TYPE_LONG;
import static org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants.FIELD_TYPE_STRING;
import static org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants.REQUIRED_ALWAYS;
import static org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants.TYPE_VARCHAR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.digijava.kernel.entity.Message;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableDiscriminator;
import org.digijava.module.aim.annotations.interchange.Validators;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/**
 * @author Octavian Ciubotaru
 */
public class FieldsEnumeratorTest {

    private static final int MAX_STR_LEN = 10;

    @Rule public MockitoRule rule = MockitoJUnit.rule();

    @Mock private FieldInfoProvider provider;
    @Mock private FMService fmService;
    @Mock private TranslatorService translatorService;
    @Mock private TranslatorService throwingTranslatorService;
    @Mock private TranslatorService emptyTranslatorService;

    @Before
    public void setUp() throws Exception {
        when(fmService.isVisible(any(), any())).thenReturn(true);

        when(translatorService.getAllTranslationOfBody(any(), any())).thenAnswer(invocation -> {
            String s = (String) invocation.getArguments()[0];
            return Arrays.asList(msg("en", s + " en"), msg("fr", s + " fr"));
        });

        when(throwingTranslatorService.getAllTranslationOfBody(any(), any())).thenThrow(new WorkerException());

        when(emptyTranslatorService.getAllTranslationOfBody(any(), any())).thenReturn(Collections.emptyList());

        when(provider.getType(any())).thenAnswer(invocation -> {
            Field f = (Field) invocation.getArguments()[0];
            return String.class.isAssignableFrom(f.getType()) ? TYPE_VARCHAR : "unknown";
        });
        when(provider.getMaxLength(any())).thenAnswer(invocation -> {
            Field f = (Field) invocation.getArguments()[0];
            return String.class.isAssignableFrom(f.getType()) && !f.getName().equals("noMaxLen") ? MAX_STR_LEN : null;
        });
        when(provider.isTranslatable(any())).thenReturn(false);
    }

    private Message msg(String locale, String text) {
        Message msg = new Message();
        msg.setLocale(locale);
        msg.setMessage(text);
        return msg;
    }

    @Test
    public void testEmpty() {
        List<APIField> fields = fieldsFor(Object.class);
        assertTrue(fields.isEmpty());
    }

    private static class NotAnnotated {
        @SuppressWarnings("unused")
        private String field;
    }

    @Test
    public void testNotAnnotated() {
        List<APIField> fields = fieldsFor(NotAnnotated.class);
        assertTrue(fields.isEmpty());
    }

    private static class OneFieldClass {

        @Interchangeable(fieldTitle = "One Field")
        private String field;
    }

    @Test
    public void testOneField() {
        List<APIField> actual = fieldsFor(OneFieldClass.class);

        APIField expected = newStringField();
        expected.setFieldName("one_field");

        assertEqualsSingle(expected, actual);
    }

    @Test
    public void testInvisibleField() {
        FMService invisibleFmService = mock(FMService.class);

        when(invisibleFmService.isVisible(any(), any())).thenReturn(false);

        List<APIField> actual = new FieldsEnumerator(provider, invisibleFmService, translatorService, false)
                .getAllAvailableFields(OneFieldClass.class);

        assertEquals(Collections.emptyList(), actual);
    }

    private static class Composition {

        @Interchangeable(fieldTitle = "field")
        private NestedField field;
    }

    private static class NestedField {

        @Interchangeable(fieldTitle = "field")
        private Long field;
    }

    @Test
    public void testComposition() {
        List<APIField> actual = fieldsFor(Composition.class);

        APIField expected = newListField();
        expected.setChildren(Arrays.asList(newLongField()));

        assertEqualsSingle(expected, actual);
    }

    private static class RequiredFieldClass {

        @Interchangeable(fieldTitle = "field", required = REQUIRED_ALWAYS)
        private String field;
    }

    @Test
    public void testRequired() {
        List<APIField> actual = fieldsFor(RequiredFieldClass.class);

        APIField expected = newStringField();
        expected.setRequired(FIELD_ALWAYS_REQUIRED);

        assertEqualsSingle(expected, actual);
    }

    private static class ImportableFieldClass {

        @Interchangeable(fieldTitle = "field", importable = true)
        private Long field;
    }

    @Test
    public void testImportable() {
        List<APIField> actual = fieldsFor(ImportableFieldClass.class);

        APIField expected = newLongField();
        expected.setImportable(true);

        assertEqualsSingle(expected, actual);
    }

    private static class MultipleValuesClass {

        @Interchangeable(fieldTitle = "field", multipleValues = false)
        private List<String> field;
    }

    @Test
    public void testMultipleValues() {
        List<APIField> actual = fieldsFor(MultipleValuesClass.class);

        APIField expected = newListField();
        expected.setMultipleValues(false);

        assertEqualsSingle(expected, actual);
    }

    private static class IdClass {

        @Interchangeable(fieldTitle = "field", id = true)
        private Long field;
    }

    @Test
    public void testId() {
        List<APIField> actual = fieldsFor(IdClass.class);

        APIField expected = newAPIField();
        expected.setFieldType(FIELD_TYPE_LONG);
        expected.setId(true);

        assertEqualsSingle(expected, actual);
    }

    private static class PickIdOnlyClass {

        @Interchangeable(fieldTitle = "field", pickIdOnly = true)
        private Long field;
    }

    @Test
    public void testPickIdOnly() {
        List<APIField> actual = fieldsFor(PickIdOnlyClass.class);

        APIField expected = newLongField();
        expected.setIdOnly(true);

        assertEqualsSingle(expected, actual);
    }

    private static class DiscriminatedClass {

        @Interchangeable(fieldTitle = "field")
        @InterchangeableDiscriminator(discriminatorField = "type", settings = {
                @Interchangeable(fieldTitle = "Type A", discriminatorOption = "a", fmPath = "a"),
                @Interchangeable(fieldTitle = "Type B", discriminatorOption = "b")
        })
        private Long field;

        @Interchangeable(fieldTitle = "field2")
        @InterchangeableDiscriminator(discriminatorField = "type", settings = {
                @Interchangeable(fieldTitle = "Type C", discriminatorOption = "c"),
                @Interchangeable(fieldTitle = "Type D", discriminatorOption = "d")
        })
        private Object field2;
    }

    @Test
    public void testDiscriminated() {
        List<APIField> actual = fieldsFor(DiscriminatedClass.class);

        APIField expected1 = newLongField();
        expected1.setFieldName("type_a");

        APIField expected2 = newLongField();
        expected2.setFieldName("type_b");

        assertEquals(Arrays.asList(expected1, expected2), actual);
    }

    @Test
    public void testInvisibleDiscriminatedField() {
        FMService invisibleFmService = mock(FMService.class);

        when(invisibleFmService.isVisible(any(), any())).thenReturn(true);
        when(invisibleFmService.isVisible(eq("a"), any())).thenReturn(false);

        List<APIField> actual = new FieldsEnumerator(provider, invisibleFmService, translatorService, false)
                .getAllAvailableFields(DiscriminatedClass.class);

        APIField expected = newLongField();
        expected.setFieldName("type_b");

        assertEqualsSingle(expected, actual);
    }

    private static class InternalConstraints {

        @Interchangeable(fieldTitle = "1", validators = @Validators(maxSize = "maxSizeFmName"))
        private Collection<Object> field1;

        @Interchangeable(fieldTitle = "2")
        private Collection<Object> field2;

        @Interchangeable(fieldTitle = "3", validators = @Validators(percentage = "percentageFmName"))
        private Collection<PercentageConstrained> field3;

        @Interchangeable(fieldTitle = "4", validators = @Validators(unique = "uniqueFmName"))
        private Collection<UniqueConstrained> field4;

        @Interchangeable(fieldTitle = "5", validators = @Validators(treeCollection = "treeCollectionFmName"))
        private Collection<Object> field5;

        @Interchangeable(fieldTitle = "6", validators =
                @Validators(percentage = "percentageFmName", unique = "uniqueFmName"))
        private Collection<Object> field6;
    }

    private static class PercentageConstrained {
        @Interchangeable(fieldTitle = "field", percentageConstraint = true)
        private Long field;
    }

    private static class UniqueConstrained {
        @Interchangeable(fieldTitle = "field", uniqueConstraint = true)
        private Long field;
    }

    @Test
    public void testInternalConstraints() {
        List<APIField> actual = fieldsFor(InternalConstraints.class);

        APIField expected1 = newListField();
        expected1.setMultipleValues(false);
        expected1.setFieldName("1");

        APIField expected2 = newListField();
        expected2.setFieldName("2");
        expected2.setMultipleValues(true);

        APIField expected3child = newLongField();
        expected3child.setPercentage(true);

        APIField expected3 = newListField();
        expected3.setFieldName("3");
        expected3.setPercentageConstraint("field");
        expected3.setChildren(Arrays.asList(expected3child));
        expected3.setMultipleValues(true);

        APIField expected4child = newLongField();

        APIField expected4 = newListField();
        expected4.setFieldName("4");
        expected4.setUniqueConstraint("field");
        expected4.setChildren(Arrays.asList(expected4child));
        expected4.setMultipleValues(true);

        APIField expected5 = newListField();
        expected5.setFieldName("5");
        expected5.setTreeCollectionConstraint(true);
        expected5.setMultipleValues(true);

        APIField expected6 = newListField();
        expected6.setFieldName("6");
        expected6.setMultipleValues(true);

        assertEquals(Arrays.asList(expected1, expected2, expected3, expected4, expected5, expected6), actual);
    }

    @Test
    public void testFieldNameInternal() {
        List<APIField> fields = fieldsForInternal(OneFieldClass.class);

        APIField expected = newStringField();
        expected.setFieldName("one_field");
        expected.setFieldNameInternal("field");

        assertEqualsSingle(expected, fields);
    }

    private static class RefActivity {

        @Interchangeable(fieldTitle = "field1")
        private AmpActivityVersion activity1;

        @Interchangeable(fieldTitle = "field2", pickIdOnly = true)
        private AmpActivityVersion activity2;
    }

    @Test
    public void testActivityFlag() {
        List<APIField> fields = fieldsForInternal(RefActivity.class);

        APIField expected1 = newListField();
        expected1.setFieldName("field1");
        expected1.setFieldNameInternal("activity1");
        expected1.setActivity(true);

        APIField expected2 = newLongField();
        expected2.setFieldName("field2");
        expected2.setFieldNameInternal("activity2");
        expected2.setIdOnly(true);
        expected2.setActivity(true);

        assertEquals(Arrays.asList(expected1, expected2), fields);
    }

    private static class MaxLen {

        @Interchangeable(fieldTitle = "noMaxLen")
        private String noMaxLen;
    }

    @Test
    public void testNoMaxLen() {
        List<APIField> fields = fieldsFor(MaxLen.class);

        APIField expected = newStringField();
        expected.setFieldName("nomaxlen");
        expected.setFieldLength(null);

        assertEqualsSingle(expected, fields);
    }

    @Test(expected = RuntimeException.class)
    public void testExceptionInTranslator() {
        new FieldsEnumerator(provider, fmService, throwingTranslatorService, false)
                .getAllAvailableFields(OneFieldClass.class);
    }

    @Test
    public void testDefaultTranslation() {
        List<APIField> fields = new FieldsEnumerator(provider, fmService, emptyTranslatorService, false)
                .getAllAvailableFields(OneFieldClass.class);

        assertEquals(1, fields.size());
        assertEquals("One Field", fields.get(0).getFieldLabel().get("EN"));
    }

    private APIField newListField() {
        APIField field = newAPIField();
        field.setFieldType(FIELD_TYPE_LIST);
        return field;
    }

    private APIField newLongField() {
        APIField field = newAPIField();
        field.setFieldType(FIELD_TYPE_LONG);
        return field;
    }

    private APIField newStringField() {
        APIField field = newAPIField();
        field.setFieldType(FIELD_TYPE_STRING);
        field.setFieldLength(MAX_STR_LEN);
        field.setTranslatable(false);
        return field;
    }

    private APIField newAPIField() {
        APIField field = new APIField();
        field.setFieldName("field");
        field.setRequired(FIELD_NOT_REQUIRED);
        field.setImportable(false);
        return field;
    }

    private <T> void assertEqualsSingle(T expected, List<T> actual) {
        assertEquals(Arrays.asList(expected), actual);
    }

    private List<APIField> fieldsFor(Class<?> theClass) {
        return new FieldsEnumerator(provider, fmService, translatorService, false)
                .getAllAvailableFields(theClass);
    }

    private List<APIField> fieldsForInternal(Class<?> theClass) {
        return new FieldsEnumerator(provider, fmService, translatorService, true)
                .getAllAvailableFields(theClass);
    }
}

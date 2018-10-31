package org.digijava.kernel.ampapi.endpoints.activity;

import static org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants.FIELD_ALWAYS_REQUIRED;
import static org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants.FIELD_NON_DRAFT_REQUIRED;
import static org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants.FIELD_NOT_REQUIRED;
import static org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants.FIELD_TYPE_LIST;
import static org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants.FIELD_TYPE_LONG;
import static org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants.FIELD_TYPE_STRING;
import static org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants.TYPE_VARCHAR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.codehaus.jackson.map.ObjectMapper;
import org.digijava.kernel.ampapi.endpoints.common.TranslatorService;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
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
    private static final int SIZE_LIMIT = 3;

    @Rule public MockitoRule rule = MockitoJUnit.rule();

    @Mock private FieldInfoProvider provider;
    @Mock private FMService fmService;
    @Mock private TranslatorService translatorService;
    @Mock private TranslatorService throwingTranslatorService;
    @Mock private TranslatorService emptyTranslatorService;

    @Before
    public void setUp() throws Exception {
        when(fmService.isVisible(any(), any())).thenReturn(true);
        when(fmService.isVisible(eq("fm hidden"), any())).thenReturn(false);

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
        expected.setFieldLabel(fieldLabelFor("One Field"));

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
        APIField nestedField = newLongField();
//        nestedField.setFieldLabel(fieldLabelFor(nestedField.getFieldName()));
        expected.setChildren(Arrays.asList(nestedField));

        assertEqualsSingle(expected, actual);
    }

    private static class RequiredFieldClass {

        @Interchangeable(fieldTitle = "field_not_required_implicit")
        private String fieldNotRequiredImplicit;

        @Interchangeable(fieldTitle = "field_not_required_explicit", required = ActivityEPConstants.REQUIRED_NONE)
        private String fieldNotRequiredExplicit;

        @Interchangeable(fieldTitle = "field_required_always", required = ActivityEPConstants.REQUIRED_ALWAYS)
        private String fieldRequiredAlways;

        @Interchangeable(fieldTitle = "field_required_non_draft", required = ActivityEPConstants.REQUIRED_ND)
        private String fieldRequiredNonDraft;

        @Interchangeable(fieldTitle = "field_required_fm_visible", required = "fm visible")
        private String fieldRequiredFmEntryVisible;

        @Interchangeable(fieldTitle = "field_required_fm_hidden", required = "fm hidden")
        private String fieldRequiredFmEntryHidden;

        @Interchangeable(fieldTitle = "field_required_min_size_on", validators = @Validators(minSize = "fm visible"))
        private String fieldRequiredMinSizeOn;

        @Interchangeable(fieldTitle = "field_required_min_size_off", validators = @Validators(minSize = "fm hidden"))
        private String fieldRequiredMinSizeOff;

        @Interchangeable(fieldTitle = "field_required_and_min_size_on", required = "fm hidden",
                validators = @Validators(minSize = "fm visible"))
        private String fieldRequiredFmEntryAndMinSizeValidatorOn;

        @Interchangeable(fieldTitle = "field_required_and_min_size_off", required = "fm hidden",
                validators = @Validators(minSize = "fm hidden"))
        private String fieldRequiredFmEntryAndMinSizeValidatorOff;
    }

    @Test
    public void testRequired() {
        List<APIField> actual = fieldsFor(RequiredFieldClass.class);

        List<APIField> expected = Arrays.asList(
                newRequiredField("field_not_required_implicit", FIELD_NOT_REQUIRED),
                newRequiredField("field_not_required_explicit", FIELD_NOT_REQUIRED),
                newRequiredField("field_required_always", FIELD_ALWAYS_REQUIRED),
                newRequiredField("field_required_non_draft", FIELD_NON_DRAFT_REQUIRED),
                newRequiredField("field_required_fm_visible", FIELD_NON_DRAFT_REQUIRED),
                newRequiredField("field_required_fm_hidden", FIELD_NOT_REQUIRED),
                newRequiredField("field_required_min_size_on", FIELD_NON_DRAFT_REQUIRED),
                newRequiredField("field_required_min_size_off", FIELD_NOT_REQUIRED),
                newRequiredField("field_required_and_min_size_on", FIELD_NON_DRAFT_REQUIRED),
                newRequiredField("field_required_and_min_size_off", FIELD_NOT_REQUIRED)
        );

        assertEqualsDigest(expected, actual);
    }

    private APIField newRequiredField(String fieldName, String required) {
        APIField field = newStringField();
        field.setFieldName(fieldName);
        field.setFieldLabel(fieldLabelFor(fieldName));
        field.setRequired(required);
        return field;
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
                @Interchangeable(fieldTitle = "type_a", discriminatorOption = "a", fmPath = "fm hidden"),
                @Interchangeable(fieldTitle = "type_b", discriminatorOption = "b")
        })
        private Long field;

        @Interchangeable(fieldTitle = "field2")
        @InterchangeableDiscriminator(discriminatorField = "type", settings = {
                @Interchangeable(fieldTitle = "type_c", discriminatorOption = "c"),
                @Interchangeable(fieldTitle = "type_d", discriminatorOption = "d")
        })
        private Object field2;
    }

    @Test
    public void testDiscriminatedField() {
        List<APIField> actual = fieldsFor(DiscriminatedClass.class);

        APIField expected = newLongField();
        expected.setFieldName("type_b");
        expected.setFieldLabel(fieldLabelFor("type_b"));

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
        
        @Interchangeable(fieldTitle = "7", sizeLimit = SIZE_LIMIT)
        private Collection<Object> field7;
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
        expected1.setFieldLabel(fieldLabelFor("1"));

        APIField expected2 = newListField();
        expected2.setFieldName("2");
        expected2.setFieldLabel(fieldLabelFor("2"));
        expected2.setMultipleValues(true);

        APIField expected3child = newLongField();
        expected3child.setPercentage(true);

        APIField expected3 = newListField();
        expected3.setFieldName("3");
        expected3.setFieldLabel(fieldLabelFor("3"));
        expected3.setPercentageConstraint("field");
        expected3.setChildren(Arrays.asList(expected3child));
        expected3.setMultipleValues(true);

        APIField expected4child = newLongField();

        APIField expected4 = newListField();
        expected4.setFieldName("4");
        expected4.setFieldLabel(fieldLabelFor("4"));
        expected4.setUniqueConstraint("field");
        expected4.setChildren(Arrays.asList(expected4child));
        expected4.setMultipleValues(true);

        APIField expected5 = newListField();
        expected5.setFieldName("5");
        expected5.setFieldLabel(fieldLabelFor("5"));
        expected5.setTreeCollectionConstraint(true);
        expected5.setMultipleValues(true);

        APIField expected6 = newListField();
        expected6.setFieldName("6");
        expected6.setFieldLabel(fieldLabelFor("6"));
        expected6.setMultipleValues(true);
        
        APIField expected7 = newListField();
        expected7.setFieldName("7");
        expected7.setFieldLabel(fieldLabelFor("7"));
        expected7.setMultipleValues(true);
        expected7.setSizeLimit(SIZE_LIMIT);

        assertEqualsDigest(Arrays.asList(expected1, expected2, expected3, expected4, expected5, expected6, expected7), 
                actual);
    }

    @Test
    public void testFieldNameInternal() {
        List<APIField> fields = fieldsForInternal(OneFieldClass.class);

        APIField expected = newStringField();
        expected.setFieldName("one_field");
        expected.setFieldLabel(fieldLabelFor("One Field"));
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
        expected1.setFieldLabel(fieldLabelFor("field1"));
        expected1.setFieldNameInternal("activity1");
        expected1.setActivity(true);

        APIField expected2 = newLongField();
        expected2.setFieldName("field2");
        expected2.setFieldLabel(fieldLabelFor("field2"));
        expected2.setFieldNameInternal("activity2");
        expected2.setIdOnly(true);
        expected2.setActivity(true);

        assertEqualsDigest(Arrays.asList(expected1, expected2), fields);
    }

    private static class MaxLen {

        @Interchangeable(fieldTitle = "field")
        private String noMaxLen;
    }

    @Test
    public void testNoMaxLen() {
        List<APIField> fields = fieldsFor(MaxLen.class);

        APIField expected = newStringField();
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

    private static class Dependencies {

        @Interchangeable(fieldTitle = "field", dependencies = {"dep1", "dep2"})
        private String field;
    }

    @Test
    public void testDependencies() {
        List<APIField> fields = fieldsFor(Dependencies.class);

        APIField expected = newStringField();
        expected.setDependencies(Arrays.asList("dep1", "dep2"));

        assertEqualsSingle(expected, fields);
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
        field.setFieldLabel(fieldLabelFor("field"));
        return field;
    }

    private void assertEqualsSingle(APIField expected, List<APIField> actual) {
        assertEqualsDigest(Arrays.asList(expected), actual);
    }

    private List<APIField> fieldsFor(Class<?> theClass) {
        return new FieldsEnumerator(provider, fmService, translatorService, false)
                .getAllAvailableFields(theClass);
    }

    private List<APIField> fieldsForInternal(Class<?> theClass) {
        return new FieldsEnumerator(provider, fmService, translatorService, true)
                .getAllAvailableFields(theClass);
    }

    private void assertEqualsDigest(List<APIField> expected, List<APIField> actual) {
        assertEquals(
                expected.stream().map(this::digest).collect(Collectors.toList()),
                actual.stream().map(this::digest).collect(Collectors.toList()));
    }

    private JsonBean fieldLabelFor(String baseText) {
        JsonBean fieldLabel = new JsonBean();
        fieldLabel.set("en", baseText + " en");
        fieldLabel.set("fr", baseText + " fr");
        return fieldLabel;
    }

    private <T> String digest(T obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create digest for " + obj, e);
        }
    }
}

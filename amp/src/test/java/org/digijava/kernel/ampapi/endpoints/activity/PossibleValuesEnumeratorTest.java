package org.digijava.kernel.ampapi.endpoints.activity;

import static org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants.TYPE_VARCHAR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.apache.struts.mock.MockHttpServletRequest;
import org.apache.struts.mock.MockHttpSession;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.field.FieldInfoProvider;
import org.digijava.kernel.ampapi.endpoints.activity.field.FieldsEnumerator;
import org.digijava.kernel.ampapi.endpoints.common.TranslatorService;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponse;
import org.digijava.kernel.ampapi.endpoints.errors.ApiRuntimeException;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.PossibleValues;
import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/**
 * @author Octavian Ciubotaru
 */
public class PossibleValuesEnumeratorTest {

    private static final int MAX_STR_LEN = 10;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Rule
    public AMPRequestRule ampRequestRule = new AMPRequestRule();

    @Mock private PossibleValuesDAO possibleValuesDAO;
    @Mock private TranslatorService translatorService;

    @Mock private FieldInfoProvider provider;
    @Mock private FeatureManagerService fmService;

    @Before
    public void setup() throws WorkerException {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest(new MockHttpSession());

        Site site = new Site("Test Site", "1");
        site.setDefaultLanguage(new Locale("en", "English"));

        SiteDomain siteDomain = new SiteDomain();
        siteDomain.setSite(site);
        siteDomain.setDefaultDomain(true);

        TLSUtils.populate(mockRequest, siteDomain);

        when(provider.getType(any())).thenAnswer(invocation -> {
            Field f = (Field) invocation.getArguments()[0];
            return String.class.isAssignableFrom(f.getType()) ? TYPE_VARCHAR : "unknown";
        });
        when(provider.getMaxLength(any())).thenAnswer(invocation -> {
            Field f = (Field) invocation.getArguments()[0];
            return String.class.isAssignableFrom(f.getType()) ? MAX_STR_LEN : null;
        });
        when(provider.isTranslatable(any())).thenReturn(false);

        when(fmService.isVisible(any())).thenReturn(true);

        when(translatorService.getAllTranslationOfBody(any(), any())).thenAnswer(invocation -> {
            String s = (String) invocation.getArguments()[0];
            return Arrays.asList(msg("en", s + " en"), msg("fr", s + " fr"));
        });
    }

    private Message msg(String locale, String text) {
        Message msg = new Message();
        msg.setLocale(locale);
        msg.setMessage(text);
        return msg;
    }

    @Test(expected = NullPointerException.class)
    public void testNullField() throws IOException {
        possibleValuesFor(null);
    }

    @Test
    public void testEmptyField() {
        try {
            possibleValuesFor("");
            fail();
        } catch (ApiRuntimeException e) {
            assertErrorJsonEquals(e.getError(), "{\"error\":{\"0105\":[\"(Invalid field) \"]}}");
        }
    }

    @Test
    public void testInvalidField() {
        try {
            possibleValuesFor("no_such_field");
            fail();
        } catch (ApiRuntimeException e) {
            assertErrorJsonEquals(e.getError(), "{\"error\":{\"0105\":[\"(Invalid field) no_such_field\"]}}");
        }
    }

    @Test
    public void testString() throws IOException {
        assertJsonEquals(possibleValuesFor("project_impact"), "[]");
    }

    @Test
    public void testNonEnumerable() throws IOException {
        assertJsonEquals(possibleValuesFor("team"), "[]");
    }

    @Test
    public void testEnumerableNoValue() throws IOException {
        assertJsonEquals(possibleValuesFor("created_by"), "[]");
    }

    @Test
    public void testNested() throws IOException {
        assertJsonEquals(possibleValuesFor("donor_organization~organization"), "[]");
    }

    @Test
    public void testNestedInvalid() {
        try {
            possibleValuesFor("no_such_field~id");
            fail();
        } catch (ApiRuntimeException e) {
            assertErrorJsonEquals(e.getError(), "{\"error\":{\"0105\":[\"(Invalid field) no_such_field\"]}}");
        }
    }

    @Test
    public void testNestedNonComposite() throws IOException {
        assertJsonEquals(possibleValuesFor("fundings~type_of_assistance"), "[]");
    }

    @Test
    public void testPossibleValuesForGeneric() throws IOException {
        when(possibleValuesDAO.getGenericValues(any())).thenReturn(Arrays.asList(
                user(1, "John", "Doe"),
                user(2, "Luigi", "Bianchi")
        ));
        assertJsonEquals(possibleValuesFor("last_imported_by"),
                "[{\"id\":1,\"value\":\"John Doe\"},{\"id\":2,\"value\":\"Luigi Bianchi\"}]");
    }

    private User user(long id, String firstName, String lastName) {
        User user = new User();
        user.setId(id);
        user.setFirstNames(firstName);
        user.setLastName(lastName);
        return user;
    }

    @Test
    public void testDiscriminatorClass() throws IOException {
        assertJsonEquals(possibleValuesFor("approval_status"),
                "[{\"id\":1,\"value\":\"approved\"},"
                        + "{\"id\":2,\"value\":\"edited\"},"
                        + "{\"id\":3,\"value\":\"startedapproved\"},"
                        + "{\"id\":4,\"value\":\"started\"},"
                        + "{\"id\":5,\"value\":\"not_approved\"},"
                        + "{\"id\":6,\"value\":\"rejected\"}]");
    }

    private static class WithThrowingProvider {
        @Interchangeable(fieldTitle = "Field")
        @PossibleValues(ThrowingPossibleValuesProvider.class)
        private String field;
    }

    static class ThrowingPossibleValuesProvider implements PossibleValueProviderTest {
        @Override
        public List<PossibleValue> getPossibleValues(TranslatorService translatorService) {
            throw new RuntimeException("some reason");
        }
    }

    @Test
    public void testDiscriminatorThrows() {
        try {
            possibleValuesFor(WithThrowingProvider.class, "field");
            fail();
        } catch (ApiRuntimeException e) {
            assertErrorJsonEquals(e.getError(),
                    "{\"error\":{\"0001\":[\"(Internal Error) Failed to obtain possible values.\"]}}");
        }
    }

    @Test
    public void testSpecialCaseAmpCategoryValue() throws IOException {
        when(translatorService.translateLabel(any())).thenReturn(ImmutableMap.of("en", "en value", "fr", "fr value"));

        when(possibleValuesDAO.getCategoryValues(any())).thenReturn(Arrays.asList(
                values(1, "Planned", false, 1),
                values(2, "Canceled", true, 2)
        ));

        assertJsonEquals(possibleValuesFor("activity_status"),
                "[{\"id\":1,\"value\":\"Planned\","
                        + "\"translated-value\":{\"en\":\"en value\",\"fr\":\"fr value\"},"
                        + "\"extra_info\":{\"index\":1}}]");
    }

    @Test
    public void testSpecialCaseAmpFundingAmount() throws IOException {
        assertJsonEquals(possibleValuesFor("ppc_amount"), "[]");
    }

    @Test
    public void testSpecialCaseAmpSector() throws IOException {
        when(possibleValuesDAO.getSectors(any()))
                .thenReturn(Arrays.asList(
                        values(1, "Sector 1", null),
                        values(2, "Sector 2", null),
                        values(3, "Sector 2.1", 2L)
                ));
        assertJsonEquals(possibleValuesFor("primary_sectors~sector"),
                "[{\"id\":1,\"value\":\"Sector 1\",\"extra_info\":{\"parent-sector-id\":null}},"
                        + "{\"id\":2,\"value\":\"Sector 2\",\"children\":["
                        + "{\"id\":3,\"value\":\"Sector 2.1\",\"extra_info\":{\"parent-sector-id\":2}}],"
                        + "\"extra_info\":{\"parent-sector-id\":null}}]");
    }

    @Test
    public void testSpecialCaseAmpTheme() throws IOException {
        when(possibleValuesDAO.getThemes(any()))
                .thenReturn(Arrays.asList(
                        values(1, "Theme 1", null),
                        values(2, "Theme 1.2", 1L)
                ));
        assertJsonEquals(possibleValuesFor("primary_programs~program"),
                "[{\"id\":1,\"value\":\"Theme 1\","
                        + "\"children\":[{\"id\":2,\"value\":\"Theme 1.2\",\"extra_info\":{\"parent-program-id\":1}}],"
                        + "\"extra_info\":{\"parent-program-id\":null}}]");
    }

    @Test
    public void testStraightCaseAmpLocation() throws IOException {
        when(possibleValuesDAO.getPossibleLocations()).thenReturn(Arrays.asList(
                        values(101, 1, "Loc 1", null, null, 50, "Country", "MD"),
                        values(102, 2, "Loc 2", 1, "Loc 1", 51, "Commune", null)
                ));
        assertJsonEquals(possibleValuesFor("locations~location"),
                "[{\"id\":101,\"value\":\"Loc 1\",\"children\":[{\"id\":102,\"value\":\"Loc 2\","
                        + "\"extra_info\":{\"parent_location_id\":101,\"parent_location_name\":\"Loc 1\","
                        + "\"implementation_level_id\":51,\"implementation_location_name\":\"Commune\"}}],"
                        + "\"extra_info\":{\"parent_location_id\":null,\"parent_location_name\":null,"
                        + "\"implementation_level_id\":50,\"implementation_location_name\":\"Country\","
                        + "\"iso2\":\"MD\"}}]");
    }

    private Object[] values(Object... values) {
        return values;
    }

    private void assertJsonEquals(List<PossibleValue> possibleValues, String expectedJson) throws IOException {
        for (Object obj : possibleValues) {
            assertTrue("Possible value must extend PossibleValue class.", obj instanceof PossibleValue);
        }
        String actualJson = new ObjectMapper().writeValueAsString(possibleValues);
        assertEquals(expectedJson, actualJson);
    }

    private void assertErrorJsonEquals(ApiErrorResponse errorResponse, String expectedJson) {
        assertEquals(expectedJson, errorResponse.asJsonString());
    }

    private List<PossibleValue> possibleValuesFor(String field) {
        return possibleValuesFor(AmpActivityFields.class, field);
    }

    private List<PossibleValue> possibleValuesFor(Class<?> theClass, String field) {
        // TODO replace mock with a simple field info provider
        // TODO same for translatorService
        List<APIField> fields = new FieldsEnumerator(provider, fmService, translatorService, name -> true)
                .getAllAvailableFields(theClass);
        return new PossibleValuesEnumerator(possibleValuesDAO, translatorService)
                .getPossibleValuesForField(field, fields);
    }
}

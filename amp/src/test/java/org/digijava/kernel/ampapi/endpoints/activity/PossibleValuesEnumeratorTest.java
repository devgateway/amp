package org.digijava.kernel.ampapi.endpoints.activity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.ImmutableMap;
import org.apache.struts.mock.MockHttpServletRequest;
import org.apache.struts.mock.MockHttpSession;
import org.codehaus.jackson.map.ObjectMapper;
import org.digijava.kernel.ampapi.endpoints.common.TranslatorService;
import org.digijava.kernel.ampapi.endpoints.errors.ApiRuntimeException;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.PossibleValues;
import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.dbentity.AmpRole;
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

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Rule
    public AMPRequestRule ampRequestRule = new AMPRequestRule();

    @Mock private PossibleValuesDAO possibleValuesDAO;
    @Mock private TranslatorService translatorService;

    @Before
    public void setup() {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest(new MockHttpSession());
        
        Site site = new Site("Test Site", "1");
        site.setDefaultLanguage(new Locale("en", "English"));

        SiteDomain siteDomain = new SiteDomain();
        siteDomain.setSite(site);
        siteDomain.setDefaultDomain(true);
        
        TLSUtils.populate(mockRequest, siteDomain);
    }

    @Test(expected = NullPointerException.class)
    public void testNullField() throws IOException {
        possibleValuesFor(null);
    }

    @Test
    public void testEmptyField() throws IOException {
        try {
            possibleValuesFor("");
            fail();
        } catch (ApiRuntimeException e) {
            assertJsonEquals(e.getError(), "{\"error\":{\"0007\":[\"(Invalid field) \"]}}");
        }
    }

    @Test
    public void testInvalidField() throws IOException {
        try {
            possibleValuesFor("no_such_field");
            fail();
        } catch (ApiRuntimeException e) {
            assertJsonEquals(e.getError(), "{\"error\":{\"0007\":[\"(Invalid field) no_such_field\"]}}");
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
        assertJsonEquals(possibleValuesFor("donor_organization~amp_organization_role_id"), "[]");
    }

    @Test
    public void testNestedInvalid() throws IOException {
        try {
            possibleValuesFor("no_such_field~id");
            fail();
        } catch (ApiRuntimeException e) {
            assertJsonEquals(e.getError(), "{\"error\":{\"0007\":[\"(Invalid field) no_such_field\"]}}");
        }
    }

    @Test
    public void testNestedNonComposite() throws IOException {
        assertJsonEquals(possibleValuesFor("donor_organization~role~name"), "[]");
    }

    @Test
    public void testPossibleValuesForGeneric() throws IOException {
        when(possibleValuesDAO.getGenericValues(any())).thenReturn(Arrays.asList(
                ampRole(1, "Donor"),
                ampRole(2, "Implementing Agency")
        ));
        assertJsonEquals(possibleValuesFor("donor_organization~role"),
                "[{\"id\":1,\"value\":\"Donor\"},{\"id\":2,\"value\":\"Implementing Agency\"}]");
    }

    private AmpRole ampRole(long id, String value) {
        AmpRole role = new AmpRole();
        role.setAmpRoleId(id);
        role.setName(value);
        return role;
    }

    @Test
    public void testDiscriminatorClass() throws IOException {
        assertJsonEquals(possibleValuesFor("approval_status"),
                "[{\"id\":\"1\",\"value\":\"approved\"},{\"id\":\"5\",\"value\":\"not_approved\"},"
                        + "{\"id\":\"3\",\"value\":\"startedapproved\"},{\"id\":\"2\",\"value\":\"edited\"},"
                        + "{\"id\":\"6\",\"value\":\"rejected\"},{\"id\":\"4\",\"value\":\"started\"}]");
    }

    private static class WithThrowingProvider {
        @Interchangeable(fieldTitle = "Field")
        @PossibleValues(ThrowingPossibleValuesProvider.class)
        private String field;
    }

    static class ThrowingPossibleValuesProvider extends PossibleValuesProvider {
        @Override
        public List<PossibleValue> getPossibleValues(TranslatorService translatorService) {
            throw new RuntimeException("some reason");
        }

        @Override
        public Object toJsonOutput(Object value) {
            return null;
        }

        @Override
        public Long getIdOf(Object value) {
            return null;
        }

        @Override
        public Object toAmpFormat(Object obj) {
            return null;
        }
    }

    @Test
    public void testDiscriminatorThrows() throws IOException {
        try {
            possibleValuesFor(WithThrowingProvider.class, "field");
            fail();
        } catch (ApiRuntimeException e) {
            assertJsonEquals(e.getError(), "{\"error\":{\"0013\":["
                    + "\"(Error when accessing a method from the discriminator class) some reason\"]}}");
        }
    }

    @Test
    public void testSpecialCaseAmpCategoryValue() throws IOException {
        when(translatorService.translateLabel(any())).thenReturn(ImmutableMap.of("en", "en value", "fr", "fr value"));

        when(possibleValuesDAO.getCategoryValues(any())).thenReturn(Arrays.asList(
                values(1, "Planned", false),
                values(2, "Canceled", true)
        ));

        assertJsonEquals(possibleValuesFor("activity_status"),
                "[{\"id\":1,\"value\":\"Planned\",\"translated-value\":{\"en\":\"en value\",\"fr\":\"fr value\"}}]");
    }

    @Test
    public void testSpecialCaseAmpFundingAmount() throws IOException {
        assertJsonEquals(possibleValuesFor("ppc_amount"), "[]");
    }

    @Test
    public void testSpecialCaseAmpSector() throws IOException {
        when(possibleValuesDAO.getSectors(any()))
                .thenReturn(Arrays.asList(
                        values(1, "Sector 1"),
                        values(2, "Sector 2")
                ));
        assertJsonEquals(possibleValuesFor("primary_sectors~sector_id"),
                "[{\"id\":1,\"value\":\"Sector 1\"},{\"id\":2,\"value\":\"Sector 2\"}]");
    }

    @Test
    public void testSpecialCaseAmpTheme() throws IOException {
        when(possibleValuesDAO.getThemes(any()))
                .thenReturn(Arrays.asList(
                        values(1, "Theme 1"),
                        values(2, "Theme 2")
                ));
        assertJsonEquals(possibleValuesFor("primary_programs~program"),
                "[{\"id\":1,\"value\":\"Theme 1\"},{\"id\":2,\"value\":\"Theme 2\"}]");
    }

    @Test
    public void testStraightCaseAmpLocation() throws IOException {
        when(possibleValuesDAO.getPossibleLocations()).thenReturn(Arrays.asList(
                        values(101, 1, "Loc 1", null, null, 50, "Country"),
                        values(102, 2, "Loc 2", 1, "Loc 1", 51, "Commune")
                ));
        assertJsonEquals(possibleValuesFor("locations~location"),
                "[{\"id\":101,\"value\":\"Loc 1\",\"children\":[{\"id\":102,\"value\":\"Loc 2\","
                        + "\"extra_info\":{\"parent_location_id\":101,\"parent_location_name\":\"Loc 1\","
                        + "\"implementation_level_id\":51,\"implementation_location_name\":\"Commune\"}}],"
                        + "\"extra_info\":{\"parent_location_id\":null,\"parent_location_name\":null,"
                        + "\"implementation_level_id\":50,\"implementation_location_name\":\"Country\"}}]");
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

    private void assertJsonEquals(JsonBean jsonBean, String expectedJson) throws IOException {
        String actualJson = new ObjectMapper().writeValueAsString(jsonBean);
        assertEquals(expectedJson, actualJson);
    }

    private List<PossibleValue> possibleValuesFor(String field) {
        return possibleValuesFor(AmpActivityFields.class, field);
    }

    private List<PossibleValue> possibleValuesFor(Class<?> theClass, String field) {
        return new PossibleValuesEnumerator(possibleValuesDAO, translatorService)
                .getPossibleValuesForField(field, theClass, null);
    }
}

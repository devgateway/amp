package org.digijava.kernel.ampapi.endpoints.activity;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.struts.mock.MockHttpServletRequest;
import org.apache.struts.mock.MockHttpSession;
import org.codehaus.jackson.map.ObjectMapper;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.request.TLSUtils;
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

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock private PossibleValuesDAO possibleValuesDAO;

    @Before
    public void setup() {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest(new MockHttpSession());
        TLSUtils.populate(mockRequest);
    }

    @Test(expected = NullPointerException.class)
    public void testNullField() throws IOException {
        possibleValuesFor(null);
    }

    @Test
    public void testEmptyField() throws IOException {
        assertJsonEquals(possibleValuesFor(""),
                "[{\"error\":{\"0007\":[\"(Invalid field) \"]}}]");
    }

    @Test
    public void testInvalidField() throws IOException {
        assertJsonEquals(possibleValuesFor("no_such_field"),
                "[{\"error\":{\"0007\":[\"(Invalid field) no_such_field\"]}}]");
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
        assertJsonEquals(possibleValuesFor("no_such_field~id"),
                "[{\"error\":{\"0007\":[\"(Invalid field) no_such_field\"]}}]");
    }

    @Test
    public void testNestedNonComposite() throws IOException {
        assertJsonEquals(possibleValuesFor("donor_organization~role~name"), "[]");
    }

    @Test
    public void testPossibleValuesForGeneric() throws IOException {
        when(possibleValuesDAO.getGenericValues(any(), any(), any())).thenReturn(Arrays.asList(
                values(1, "Donor"),
                values(2, "Implementing Agency")
        ));
        assertJsonEquals(possibleValuesFor("donor_organization~role"),
                "[{\"id\":1,\"value\":\"Donor\"},{\"id\":2,\"value\":\"Implementing Agency\"}]");
    }

    @Test
    public void testDiscriminatorClass() throws IOException {
        assertJsonEquals(possibleValuesFor("approval_status"),
                "[{\"id\":\"1\",\"value\":\"approved\"},{\"id\":\"2\",\"value\":\"edited\"},"
                        + "{\"id\":\"3\",\"value\":\"startedapproved\"},{\"id\":\"4\",\"value\":\"started\"},"
                        + "{\"id\":\"5\",\"value\":\"not_approved\"},{\"id\":\"6\",\"value\":\"rejected\"}]");
    }

    private static class WithThrowingProvider {
        @Interchangeable(fieldTitle = "Field")
        @PossibleValues(ThrowingPossibleValuesProvider.class)
        private String field;
    }

    static class ThrowingPossibleValuesProvider extends PossibleValuesProvider {
        @Override
        public Map<String, ?> getPossibleValues() {
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
        assertJsonEquals(possibleValuesFor(WithThrowingProvider.class, "field"),
                "[{\"error\":{\"0013\":["
                        + "\"(Error when accessing a method from the discriminator class) some reason\"]}}]");
    }

    @Test
    public void testSpecialCaseAmpCategoryValue() throws IOException {
        when(possibleValuesDAO.getCategoryValues(any())).thenReturn(Arrays.asList(
                values(1, "Planned", false),
                values(2, "Canceled", true)
        ));

        assertJsonEquals(possibleValuesFor("activity_status"),
                "[{\"id\":1,\"value\":\"Planned\"}]");
    }

    @Test
    public void testSpecialCaseAmpFundingAmount() throws IOException {
        assertJsonEquals(possibleValuesFor("ppc_amount"), "[]");
    }

    @Test
    public void testSpecialCaseAmpSector() throws IOException {
        when(possibleValuesDAO.getSpecialCaseObjectList(any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(Arrays.asList(
                        values(1, "Sector 1"),
                        values(2, "Sector 2")
                ));
        assertJsonEquals(possibleValuesFor("primary_sectors~sector_id"),
                "[{\"id\":1,\"value\":\"Sector 1\"},{\"id\":2,\"value\":\"Sector 2\"}]");
    }

    @Test
    public void testSpecialCaseAmpTheme() throws IOException {
        when(possibleValuesDAO.getSpecialCaseObjectList(any(), any(), any(), any(), any(), any(), any()))
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
                        values(1, "Loc 1", null, null, null, null),
                        values(2, "Loc 2", 1, "Loc 1", 0, "Loc 0")
                ));
        assertJsonEquals(possibleValuesFor("locations~location"),
                "[{\"id\":1,\"value\":\"Loc 1\","
                + "\"extra_info\":{\"parent_location_id\":null,\"parent_location_name\":null,"
                + "\"implementation_level_id\":null,\"implementation_location_name\":null}},"
                + "{\"id\":2,\"value\":\"Loc 2\","
                + "\"extra_info\":{\"parent_location_id\":1,\"parent_location_name\":\"Loc 1\","
                + "\"implementation_level_id\":0,\"implementation_location_name\":\"Loc 0\"}}]");
    }

    private Object[] values(Object... values) {
        return values;
    }

    private void assertJsonEquals(Object actualObj, String expectedJson) throws IOException {
        String actualJson = new ObjectMapper().writeValueAsString(actualObj);
        assertEquals(actualJson, expectedJson);
    }

    private List<JsonBean> possibleValuesFor(String field) {
        return possibleValuesFor(AmpActivityFields.class, field);
    }

    private List<JsonBean> possibleValuesFor(Class<?> theClass, String field) {
        return new PossibleValuesEnumerator(possibleValuesDAO)
                .getPossibleValuesForField(field, theClass, null);
    }
}

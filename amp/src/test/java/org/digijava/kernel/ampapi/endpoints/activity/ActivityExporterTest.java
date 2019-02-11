package org.digijava.kernel.ampapi.endpoints.activity;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import org.dgfoundation.amp.reports.converters.HardcodedThemes;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.field.ActivityFieldsEnumerator;
import org.digijava.kernel.ampapi.endpoints.common.TestTranslatorService;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Octavian Ciubotaru
 */
public class ActivityExporterTest {

    /**
     * Required because of translations. See InterchangeUtils.getTranslationValues.
     */
    @Rule
    public AMPRequestRule ampRequestRule = new AMPRequestRule();

    private List<APIField> fields;

    @Before
    public void setUp() {
        TestTranslatorService translatorService = new TestTranslatorService();

        ActivityFieldsEnumerator enumerator = new ActivityFieldsEnumerator(
                new TestFieldInfoProvider(),
                new TestFMService(),
                translatorService,
                false,
                program -> false,
                "project_code");

        InterchangeUtils.setTranslatorService(translatorService);

        fields = enumerator.getAllAvailableFields(AmpActivityFields.class);
    }

    @Test
    public void testProjectName() {
        AmpActivityVersion activity = new AmpActivityVersion();
        activity.setName("test");

        Map<String, Object> jsonActivity = createExporter().export(activity).any();

        assertThat(jsonActivity, hasEntry("project_title", "test"));// fixme there are 100+ fields with null values!
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testPrograms() {
        HardcodedThemes hardcodedThemes = new HardcodedThemes();

        AmpActivityProgram axe1 = new AmpActivityProgram();
        axe1.setProgramPercentage(100f);
        axe1.setProgram(hardcodedThemes.getTheme("Axe 1"));
        axe1.setProgramSetting(hardcodedThemes.getPrimaryProgramSettings());

        AmpActivityProgram instReform = new AmpActivityProgram();
        instReform.setProgramPercentage(78f);
        instReform.setProgram(hardcodedThemes.getTheme("Institutional Reform"));
        instReform.setProgramSetting(hardcodedThemes.getSecondaryProgramSettings());

        AmpActivityProgram edReform = new AmpActivityProgram();
        edReform.setProgramPercentage(22f);
        edReform.setProgram(hardcodedThemes.getTheme("Educational Reform"));
        edReform.setProgramSetting(hardcodedThemes.getSecondaryProgramSettings());

        Set<AmpActivityProgram> programs = new HashSet<>();
        programs.add(axe1);
        programs.add(instReform);
        programs.add(edReform);

        AmpActivityVersion activity = new AmpActivityVersion();
        activity.setActPrograms(programs);

        Map<String, Object> jsonActivity = createExporter().export(activity).any();

        assertThat(jsonActivity,
                (Matcher) allOf(
                        hasEntry(equalTo("primary_programs"),
                                contains(actProgram(2L, 100F))),
                        hasEntry(equalTo("secondary_programs"),
                                containsInAnyOrder(
                                        actProgram(8L, 78F),
                                        actProgram(9L, 22F)))));
    }

    /**
     * Matcher that matches a map if "program" and "program_percentage" keys match provided values.
     */
    private Matcher<Map<String, Object>> actProgram(long program, float percentage) {
        return Matchers.<Map<String, Object>> allOf(
                hasEntry("program", program),
                hasEntry("program_percentage", percentage));
    }

    @Test
    public void testFilters() {
        AmpActivityVersion activity = new AmpActivityVersion();
        activity.setName("test");
        activity.setDescription("desc");

        ActivityExporter exporter = createExporter(ImmutableList.of("description"));

        Map<String, Object> jsonActivity = exporter.export(activity).any();

        assertThat(jsonActivity,
                allOf(
                        hasEntry("description", "Test Site-desc-en"),
                        not(hasEntry("project_title", "test"))));
    }

    private ActivityExporter createExporter() {
        return createExporter(ImmutableList.of());
    }

    private ActivityExporter createExporter(List<String> filterFields) {
        JsonBean filter = new JsonBean();
        filter.set(ActivityEPConstants.FILTER_FIELDS, filterFields);

        return new ActivityExporter(fields, filter);
    }
}

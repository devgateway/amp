package org.digijava.kernel.ampapi.endpoints.activity;

import com.google.common.collect.ImmutableList;
import org.dgfoundation.amp.reports.converters.HardcodedThemes;
import org.dgfoundation.amp.testutils.TransactionUtil;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.field.FieldsEnumerator;
import org.digijava.kernel.ampapi.endpoints.common.TestTranslatorService;
import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author Octavian Ciubotaru
 */
public class ActivityExporterTest {

    private List<APIField> fields;

    @BeforeEach
    public void setUp() {
        TestTranslatorService translatorService = new TestTranslatorService();

        TransactionUtil.setUpWorkspaceEmptyPrefixes();

        FieldsEnumerator enumerator = new FieldsEnumerator(
                new TestFieldInfoProvider(),
                new TestFMService(),
                translatorService,
                program -> false);

        ActivityTranslationUtils.setTranslatorService(translatorService);

        fields = enumerator.getAllAvailableFields(AmpActivityFields.class);
    }

    @Test
    public void testProjectName() {
        AmpActivityVersion activity = new AmpActivityVersion();
        activity.setName("test");

        Map<String, Object> jsonActivity = createExporter().export(activity);

        MatcherAssert.assertThat(jsonActivity, hasEntry("project_title", "test"));// fixme there are 100+ fields with null values!
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testPrograms() {
        HardcodedThemes hardcodedThemes = new HardcodedThemes();

        AmpTheme axe1 = hardcodedThemes.getTheme("Axe 1");
        AmpActivityProgram actAxe1 = new AmpActivityProgram();
        actAxe1.setProgramPercentage(100f);
        actAxe1.setProgram(axe1);
        actAxe1.setProgramSetting(hardcodedThemes.getPrimaryProgramSettings());

        AmpTheme instReform = hardcodedThemes.getTheme("Institutional Reform");
        AmpActivityProgram actInstReform = new AmpActivityProgram();
        actInstReform.setProgramPercentage(78f);
        actInstReform.setProgram(instReform);
        actInstReform.setProgramSetting(hardcodedThemes.getSecondaryProgramSettings());

        AmpTheme edReform = hardcodedThemes.getTheme("Educational Reform");
        AmpActivityProgram actEdReform = new AmpActivityProgram();
        actEdReform.setProgramPercentage(22f);
        actEdReform.setProgram(edReform);
        actEdReform.setProgramSetting(hardcodedThemes.getSecondaryProgramSettings());

        Set<AmpActivityProgram> programs = new HashSet<>();
        programs.add(actAxe1);
        programs.add(actInstReform);
        programs.add(actEdReform);

        AmpActivityVersion activity = new AmpActivityVersion();
        activity.setActPrograms(programs);

        Map<String, Object> jsonActivity = createExporter().export(activity);

        MatcherAssert.assertThat(jsonActivity, (Matcher) allOf(
                hasEntry(equalTo("primary_programs"),
                        contains(actProgram(axe1.getAmpThemeId(), 100F))),
                hasEntry(equalTo("secondary_programs"),
                        containsInAnyOrder(
                                actProgram(instReform.getAmpThemeId(), 78F),
                                actProgram(edReform.getAmpThemeId(), 22F)))));
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

        Map<String, Object> jsonActivity = exporter.export(activity);

        MatcherAssert.assertThat(jsonActivity, allOf(
                hasEntry("description", "desc"),
                not(hasEntry("project_title", "test"))));
    }

    private ActivityExporter createExporter() {
        return createExporter(ImmutableList.of());
    }

    private ActivityExporter createExporter(List<String> filterFields) {
        Map<String, Object> filter = new HashMap<>();
        filter.put(ActivityEPConstants.FILTER_FIELDS, filterFields);

        return new ActivityExporter(new NoTranslatedFieldReader(), fields, filter);
    }
}

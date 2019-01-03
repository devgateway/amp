package org.dgfoundation.amp.reports.converters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.newreports.AmpReportFilters;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportElement;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.junit.Test;

/**
 * @author Octavian Ciubotaru
 */
public class AmpARFilterConverterTest {

    private HardcodedThemes themes = new HardcodedThemes();

    @Test
    public void testProgramsLevelsAreRestored() {
        AmpTheme axe1 = themes.getTheme("Axe 1");
        AmpTheme axe1p1 = themes.getTheme("1.1");
        AmpTheme axe1p2 = themes.getTheme("1.2");
        AmpTheme axe2p1 = themes.getTheme("2.1");

        AmpARFilter arFilters = new AmpARFilter();
        arFilters.setSelectedPrimaryPrograms(ImmutableSet.of(axe1, axe1p1, axe1p2, axe2p1));

        AmpARFilterConverter converter = new AmpARFilterConverter(arFilters);

        AmpReportFilters reportFilters = converter.buildFilters();

        ReportElement level1 = new ReportElement(new ReportColumn(ColumnConstants.PRIMARY_PROGRAM_LEVEL_1));
        ReportElement level2 = new ReportElement(new ReportColumn(ColumnConstants.PRIMARY_PROGRAM_LEVEL_2));

        assertTrue(reportFilters.getFilterRules().containsKey(level1));
        assertTrue(reportFilters.getFilterRules().containsKey(level2));

        assertEquals(new FilterRule(ImmutableList.of("Axe 1"), ImmutableList.of("2"), true),
                reportFilters.getFilterRules().get(level1));

        assertEquals(new FilterRule(ImmutableList.of("1.1", "1.2", "2.1"), ImmutableList.of("4", "5", "6"), true),
                reportFilters.getFilterRules().get(level2));
    }
}

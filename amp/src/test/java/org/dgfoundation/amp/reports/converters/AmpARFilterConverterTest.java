package org.dgfoundation.amp.reports.converters;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.newreports.AmpReportFilters;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportElement;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Octavian Ciubotaru
 */
public class AmpARFilterConverterTest {

    private HardcodedThemes themes = new HardcodedThemes();
    private HardcodedSectors sectors = new HardcodedSectors();

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
    
    @Test
    public void testPrimarySubSectorColumnNameBasedOnLevels() {
        AmpARFilter arFilters = new AmpARFilter();
        AmpARFilterConverter converter = new AmpARFilterConverter(arFilters);
    
        AmpSector primarySector = sectors.getSector("Primary Sector 1");
        AmpSector sector = sectors.getSector("Sec 1");
        AmpSector sector2 = sectors.getSector("1.1");
    
        assertEquals(ColumnConstants.PRIMARY_SECTOR,
                converter.findSubSectorColumnName(ColumnConstants.PRIMARY_SECTOR, primarySector));
        
        assertEquals(ColumnConstants.PRIMARY_SECTOR_SUB_SECTOR,
                converter.findSubSectorColumnName(ColumnConstants.PRIMARY_SECTOR, sector));
        
        assertEquals(ColumnConstants.PRIMARY_SECTOR_SUB_SUB_SECTOR,
                converter.findSubSectorColumnName(ColumnConstants.PRIMARY_SECTOR, sector2));
    }
    
    @Test
    public void testSecondarySubSectorColumnNameBasedOnLevels() {
        AmpARFilter arFilters = new AmpARFilter();
        AmpARFilterConverter converter = new AmpARFilterConverter(arFilters);
        
        AmpSector primarySector = sectors.getSector("Secondary Sector 2");
        AmpSector sector = sectors.getSector("2.1");
        
        assertEquals(ColumnConstants.SECONDARY_SECTOR,
                converter.findSubSectorColumnName(ColumnConstants.SECONDARY_SECTOR, primarySector));
        
        assertEquals(ColumnConstants.SECONDARY_SECTOR_SUB_SECTOR,
                converter.findSubSectorColumnName(ColumnConstants.SECONDARY_SECTOR, sector));
    }
    
    @Test
    public void testPrimarySubSectorColumnNameBasedOnLevelsForPledges() {
        AmpARFilter arPledgeFilters = new AmpARFilter();
        arPledgeFilters.setPledgeFilter(true);
        AmpARFilterConverter converter = new AmpARFilterConverter(arPledgeFilters);
        
        AmpSector primarySector = sectors.getSector("Primary Sector 1");
        AmpSector sector = sectors.getSector("Sec 1");
        AmpSector sector2 = sectors.getSector("1.1");
        
        assertEquals(ColumnConstants.PLEDGES_SECTORS,
                converter.findSubSectorColumnName(ColumnConstants.PLEDGES_SECTORS, primarySector));
        
        assertEquals(ColumnConstants.PLEDGES_SECTORS_SUBSECTORS,
                converter.findSubSectorColumnName(ColumnConstants.PLEDGES_SECTORS, sector));
        
        assertEquals(ColumnConstants.PLEDGES_SECTORS_SUBSUBSECTORS,
                converter.findSubSectorColumnName(ColumnConstants.PLEDGES_SECTORS, sector2));
    }
    
    @Test
    public void testSecondarySubSectorColumnNameBasedOnLevelsForPledges() {
        AmpARFilter arPledgeFilters = new AmpARFilter();
        arPledgeFilters.setPledgeFilter(true);
        AmpARFilterConverter converter = new AmpARFilterConverter(arPledgeFilters);
    
        AmpSector secondarySector = sectors.getSector("Secondary Sector 2");
        AmpSector sector = sectors.getSector("2.1");
    
        assertEquals(ColumnConstants.PLEDGES_SECONDARY_SECTORS,
                converter.findSubSectorColumnName(ColumnConstants.PLEDGES_SECONDARY_SECTORS, secondarySector));
    
        assertEquals(ColumnConstants.PLEDGES_SECONDARY_SUBSECTORS,
                converter.findSubSectorColumnName(ColumnConstants.PLEDGES_SECONDARY_SECTORS, sector));
    }
}

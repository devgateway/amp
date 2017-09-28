package org.dgfoundation.amp.ar.amp212;

import java.util.Arrays;
import java.util.List;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.newreports.ReportCollapsingStrategy;
import org.dgfoundation.amp.nireports.ReportHierarchiesCollapser;
import org.dgfoundation.amp.nireports.runtime.CellColumn;
import org.dgfoundation.amp.nireports.runtime.ColumnReportData;
import org.dgfoundation.amp.nireports.runtime.GroupReportData;
import org.dgfoundation.amp.nireports.runtime.ReportData;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.testcases.generic.HardcodedReportsTestSchema;
import org.junit.Test;

/**
 * tests for {@link ReportHierarchiesCollapser}
 * @author Dolghier Constantin
 *
 */
public class HierarchiesCollapserTests extends NiTestCase {
        
    public HierarchiesCollapserTests() {
        super(HardcodedReportsTestSchema.getInstance());
    }
    
    final LevelColumn PSS = HardcodedReportsTestSchema.PS_DIM_USG.getLevelColumn(1);
    

    protected ColumnReportData buildCrd(String column, String name, long crdId, String projectTitle, long projectId, String subSectorColumn, String subSectorTitle, long subSectorId, double subSectorPercentage, LevelColumn subSectorLC) {
        final ColumnReportData res = buildColumnReportData(column, name, crdId,
                ColumnConstants.PROJECT_TITLE, Arrays.asList(textCell(projectTitle, projectId, projectId)),
                subSectorColumn, 
                    Arrays.asList(
                        percentageTextCell(subSectorTitle, projectId, subSectorId, subSectorPercentage, subSectorLC))
                );
        return res;
    }

    protected ColumnReportData buildSectorialCrd(String name, long crdId, String projectTitle, long projectId, String subSectorTitle, long subSectorId, double subSectorPercentage) {
        return buildCrd(ColumnConstants.PRIMARY_SECTOR, name, crdId, projectTitle, projectId, ColumnConstants.PRIMARY_SECTOR_SUB_SECTOR, subSectorTitle, subSectorId, subSectorPercentage, PSS);
    }

    final ColumnReportData UNDEFINED_PS_1_CRD = buildColumnReportData(ColumnConstants.PRIMARY_SECTOR, "Undefined", -1,
            ColumnConstants.PROJECT_TITLE, Arrays.asList(textCell("AAA", 1l, 1l), textCell("BBB", 2l, 2l), textCell("CCC", 3l, 3l)),
            ColumnConstants.PRIMARY_SECTOR_SUB_SECTOR, 
                Arrays.asList(
                    percentageTextCell("Undefined 11", 1l, -11l, 0.25, PSS), 
                    percentageTextCell("Undefined 12", 1l, -12l, 0.25, PSS),
                    percentageTextCell("Undefined 12", 2l, -12l, 0.3, PSS))
            );

    final ColumnReportData UNDEFINED_PS_2_CRD = buildSectorialCrd("Undefined", -2l, "BBB", 2l, "Undefined 12", -21l, 0.75);
    final ColumnReportData DEFINED_PS_HEALTH_3_CRD = buildSectorialCrd("Health", 3l, "BBB", 2l, "Children_Health", 31l, 0.75);
    final ColumnReportData DEFINED_PS_HEALTH_4_CRD = buildSectorialCrd("Health", 4l, "DDD", 14l, "Dads_Health", 41l, 0.5);
    final ColumnReportData DEFINED_PS_EDUCATION_7_CRD = buildSectorialCrd("Education", 7l, "EEE", 15l, "Developers_Education", 71l, 0.3);   

    final GroupReportData DEFINED_DONOR_AGENCY_USAID = new GroupReportData(null, splitCell(ColumnConstants.DONOR_AGENCY, "USAID", 99l),
            Arrays.asList(UNDEFINED_PS_1_CRD, UNDEFINED_PS_2_CRD, DEFINED_PS_HEALTH_3_CRD, DEFINED_PS_HEALTH_4_CRD, DEFINED_PS_EDUCATION_7_CRD));

    
    
    final ColumnReportData DEFINED_PS_HEALTH_3_CRD_FINLAND = buildSectorialCrd("Health", 3l, "TURA", 17l, "Seniors_Health", 32l, 0.72);
    final ColumnReportData DEFINED_PS_HEALTH_4_CRD_FINLAND = buildSectorialCrd("Health", 4l, "ZURA", 13l, "Aliens_Health", 29l, 0.19);

    GroupReportData DEFINED_DONOR_AGENCY_FINLAND = new GroupReportData(null, splitCell(ColumnConstants.DONOR_AGENCY, "Finland", 78l), Arrays.asList(
            DEFINED_PS_HEALTH_3_CRD_FINLAND,
            buildSectorialCrd("Unknown", -3l, "VAVA", 26l, "Seniors_Health", 32l, 0.1),
            buildSectorialCrd("Unknown", -4l, "ZUZU", 27l, "Aliens_Health", 29l, 0.23)
        ));
    
    GroupReportData DEFINED_DONOR_AGENCY_FINLAND_2 = new GroupReportData(null, splitCell(ColumnConstants.DONOR_AGENCY, "Finland", 79l), Arrays.asList(
            DEFINED_PS_HEALTH_4_CRD_FINLAND,
            buildSectorialCrd("Unknown", -3l, "JUJU", 28l, "Randomness_Health", 33l, 0.15),
            buildSectorialCrd("Unknown", -5l, "LOLA", 29l, "Ahem_Health", 34l, 0.55)
            ));
    
    GroupReportData UNDEFINED_DONOR_AGENCY_11 = new GroupReportData(null, splitCell(ColumnConstants.DONOR_AGENCY, "Undefined", -11l), Arrays.asList(
            buildSectorialCrd("Health", 3l, "TURA", 17l, "Seniors_Health", 32l, 0.72),
            buildSectorialCrd("Health", 4l, "JORA", 21l, "Zombies_Health", 34l, 0.32),
            buildSectorialCrd("some_sector_1", 64l, "some_project_1", 111l, "say_something_1", 120l, 0.5),
            buildSectorialCrd("some_sector_2", 65l, "some_project_2", 112l, "say_something_2", 121l, 0.35),
            buildSectorialCrd("Undefined", -7l, "klara", 116l, "say_something_2", 121l, 0.35)
        ));

    GroupReportData UNDEFINED_DONOR_AGENCY_12 = new GroupReportData(null, splitCell(ColumnConstants.DONOR_AGENCY, "Undefined", -12l), Arrays.asList(
            buildSectorialCrd("Health", 3l, "HAHA", 22l, "Seniors_Health", 32l, 0.62),
            buildSectorialCrd("Health", 4l, "JAJA", 25l, "Zombies_Health", 34l, 0.57),
            buildSectorialCrd("some_sector_3", 66l, "some_project_3", 113l, "say_something_3", 123l, 0.6),
            buildSectorialCrd("Undefined", -7l, "vlara", 117l, "say_something_4", 124l, 0.55),
            buildSectorialCrd("Undefined", -9l, "zoso", 118l, "say_something_5", 205l, 0.34)
        ));
    
    GroupReportData DEFINED_DONOR_TYPE_ALL = new GroupReportData(null, splitCell(ColumnConstants.DONOR_TYPE, "Default", 38l), Arrays.asList(
            DEFINED_DONOR_AGENCY_USAID, DEFINED_DONOR_AGENCY_FINLAND, DEFINED_DONOR_AGENCY_FINLAND_2, UNDEFINED_DONOR_AGENCY_11, UNDEFINED_DONOR_AGENCY_12
            ));

    final List<CellColumn> leaves = Arrays.asList(
            cachedColumns.get(ColumnConstants.PROJECT_TITLE), 
            cachedColumns.get(ColumnConstants.PRIMARY_SECTOR_SUB_SECTOR));
                
    @Test
    public void testCRDCollapsing() {
        for(ReportCollapsingStrategy strategy:Arrays.asList(ReportCollapsingStrategy.NEVER, ReportCollapsingStrategy.UNKNOWNS, ReportCollapsingStrategy.ALWAYS)) {
            ReportData rd = UNDEFINED_PS_1_CRD;
            ReportData collapsed_CRD = rd.accept(new ReportHierarchiesCollapser(strategy, leaves));
            assertEquals(
                    "crd ColumnReportData: Undefined (id: [-1]) {\n" + 
                            "  Primary Sector Sub-Sector -> {1=[Undefined 11 (id: 1, eid: -11, coos: {sectors.Primary=(level: 1, id: -11)}, p: 0.25), Undefined 12 (id: 1, eid: -12, coos: {sectors.Primary=(level: 1, id: -12)}, p: 0.25)], 2=[Undefined 12 (id: 2, eid: -12, coos: {sectors.Primary=(level: 1, id: -12)}, p: 0.30)]}\n" + 
                            "  Project Title -> {1=[AAA (id: 1, eid: 1)], 2=[BBB (id: 2, eid: 2)], 3=[CCC (id: 3, eid: 3)]}\n" + 
                            "}",
                asDebugString(rd));
        
            assertEquals(asDebugString(rd), asDebugString(collapsed_CRD));
        }
    }
    
    @Test
    public void testGRDCollapsing() {
        GroupReportData grd = DEFINED_DONOR_AGENCY_USAID;
        
        assertEquals(asDebugString(grd), asDebugString(grd.accept(new ReportHierarchiesCollapser(ReportCollapsingStrategy.NEVER, leaves))));
        
        assertEquals(
            "GroupReportData: USAID (id: [99]):\n" + 
            "  crd ColumnReportData: Education (id: [7]) {\n" + 
            "    Primary Sector Sub-Sector -> {15=[Developers_Education (id: 15, eid: 71, coos: {sectors.Primary=(level: 1, id: 71)}, p: 0.30)]}\n" + 
            "    Project Title -> {15=[EEE (id: 15, eid: 15)]}\n" + 
            "  }\n" + 
            "  crd ColumnReportData: Health (id: [3, 4]) {\n" + 
            "    Primary Sector Sub-Sector -> {2=[Children_Health (id: 2, eid: 31, coos: {sectors.Primary=(level: 1, id: 31)}, p: 0.75)], 14=[Dads_Health (id: 14, eid: 41, coos: {sectors.Primary=(level: 1, id: 41)}, p: 0.50)]}\n" +
            "    Project Title -> {2=[BBB (id: 2, eid: 2)], 14=[DDD (id: 14, eid: 14)]}\n" +
            "  }\n" +
            "  crd ColumnReportData: Undefined (id: [-1, -2]) {\n" +
            "    Primary Sector Sub-Sector -> {1=[Undefined 11 (id: 1, eid: -11, coos: {sectors.Primary=(level: 1, id: -11)}, p: 0.25), Undefined 12 (id: 1, eid: -12, coos: {sectors.Primary=(level: 1, id: -12)}, p: 0.25)], 2=[Undefined 12 (id: 2, eid: -12, coos: {sectors.Primary=(level: 1, id: -12)}, p: 0.30), Undefined 12 (id: 2, eid: -21, coos: {sectors.Primary=(level: 1, id: -21)}, p: 0.75)]}\n" +
            "    Project Title -> {1=[AAA (id: 1, eid: 1)], 2=[BBB (id: 2, eid: 2), BBB (id: 2, eid: 2)], 3=[CCC (id: 3, eid: 3)]}\n" + 
            "  }",
            asDebugString(grd.accept(new ReportHierarchiesCollapser(ReportCollapsingStrategy.ALWAYS, leaves))));
        
        assertEquals(
                "GroupReportData: USAID (id: [99]):\n" + 
                "  crd ColumnReportData: Education (id: [7]) {\n" + 
                "    Primary Sector Sub-Sector -> {15=[Developers_Education (id: 15, eid: 71, coos: {sectors.Primary=(level: 1, id: 71)}, p: 0.30)]}\n" +
                "    Project Title -> {15=[EEE (id: 15, eid: 15)]}\n" +
                "  }\n" +
                "  crd ColumnReportData: Health (id: [3]) {\n" +
                "    Primary Sector Sub-Sector -> {2=[Children_Health (id: 2, eid: 31, coos: {sectors.Primary=(level: 1, id: 31)}, p: 0.75)]}\n" +
                "    Project Title -> {2=[BBB (id: 2, eid: 2)]}\n" +
                "  }\n" +
                "  crd ColumnReportData: Health (id: [4]) {\n" +
                "    Primary Sector Sub-Sector -> {14=[Dads_Health (id: 14, eid: 41, coos: {sectors.Primary=(level: 1, id: 41)}, p: 0.50)]}\n" +
                "    Project Title -> {14=[DDD (id: 14, eid: 14)]}\n" +
                "  }\n" +
                "  crd ColumnReportData: Undefined (id: [-1, -2]) {\n" +
                "    Primary Sector Sub-Sector -> {1=[Undefined 11 (id: 1, eid: -11, coos: {sectors.Primary=(level: 1, id: -11)}, p: 0.25), Undefined 12 (id: 1, eid: -12, coos: {sectors.Primary=(level: 1, id: -12)}, p: 0.25)], 2=[Undefined 12 (id: 2, eid: -12, coos: {sectors.Primary=(level: 1, id: -12)}, p: 0.30), Undefined 12 (id: 2, eid: -21, coos: {sectors.Primary=(level: 1, id: -21)}, p: 0.75)]}\n" +
                "    Project Title -> {1=[AAA (id: 1, eid: 1)], 2=[BBB (id: 2, eid: 2), BBB (id: 2, eid: 2)], 3=[CCC (id: 3, eid: 3)]}\n" +
                "  }",
        asDebugString(grd.accept(new ReportHierarchiesCollapser(ReportCollapsingStrategy.UNKNOWNS, leaves))));  
    }
    
    @Test
    public void testEmbeddedGRDCollapsing() {
        // test that same-id defined values when merging are merged, even when only merging unknowns
        GroupReportData grd = DEFINED_DONOR_TYPE_ALL;
        assertEquals(asDebugString(grd), asDebugString(grd.accept(new ReportHierarchiesCollapser(ReportCollapsingStrategy.NEVER, leaves))));
        
        assertEquals(
            "GroupReportData: Default (id: [38]):\n" + 
            "  GroupReportData: USAID (id: [99]):\n" +
            "    crd ColumnReportData: Undefined (id: [-1]) {\n" +
            "      Primary Sector Sub-Sector -> {1=[Undefined 11 (id: 1, eid: -11, coos: {sectors.Primary=(level: 1, id: -11)}, p: 0.25), Undefined 12 (id: 1, eid: -12, coos: {sectors.Primary=(level: 1, id: -12)}, p: 0.25)], 2=[Undefined 12 (id: 2, eid: -12, coos: {sectors.Primary=(level: 1, id: -12)}, p: 0.30)]}\n" +
            "      Project Title -> {1=[AAA (id: 1, eid: 1)], 2=[BBB (id: 2, eid: 2)], 3=[CCC (id: 3, eid: 3)]}\n" +
            "    }\n" +
            "    crd ColumnReportData: Undefined (id: [-2]) {\n" +
            "      Primary Sector Sub-Sector -> {2=[Undefined 12 (id: 2, eid: -21, coos: {sectors.Primary=(level: 1, id: -21)}, p: 0.75)]}\n" +
            "      Project Title -> {2=[BBB (id: 2, eid: 2)]}\n" +
            "    }\n" +
            "    crd ColumnReportData: Health (id: [3]) {\n" +
            "      Primary Sector Sub-Sector -> {2=[Children_Health (id: 2, eid: 31, coos: {sectors.Primary=(level: 1, id: 31)}, p: 0.75)]}\n" +
            "      Project Title -> {2=[BBB (id: 2, eid: 2)]}\n" +
            "    }\n" +
            "    crd ColumnReportData: Health (id: [4]) {\n" +
            "      Primary Sector Sub-Sector -> {14=[Dads_Health (id: 14, eid: 41, coos: {sectors.Primary=(level: 1, id: 41)}, p: 0.50)]}\n" +
            "      Project Title -> {14=[DDD (id: 14, eid: 14)]}\n" +
            "    }\n" +
            "    crd ColumnReportData: Education (id: [7]) {\n" +
            "      Primary Sector Sub-Sector -> {15=[Developers_Education (id: 15, eid: 71, coos: {sectors.Primary=(level: 1, id: 71)}, p: 0.30)]}\n" +
            "      Project Title -> {15=[EEE (id: 15, eid: 15)]}\n" +
            "    }\n" +
            "  GroupReportData: Finland (id: [78]):\n" +
            "    crd ColumnReportData: Health (id: [3]) {\n" +
            "      Primary Sector Sub-Sector -> {17=[Seniors_Health (id: 17, eid: 32, coos: {sectors.Primary=(level: 1, id: 32)}, p: 0.72)]}\n" +
            "      Project Title -> {17=[TURA (id: 17, eid: 17)]}\n" +
            "    }\n" +
            "    crd ColumnReportData: Unknown (id: [-3]) {\n" +
            "      Primary Sector Sub-Sector -> {26=[Seniors_Health (id: 26, eid: 32, coos: {sectors.Primary=(level: 1, id: 32)}, p: 0.10)]}\n" +
            "      Project Title -> {26=[VAVA (id: 26, eid: 26)]}\n" +
            "    }\n" +
            "    crd ColumnReportData: Unknown (id: [-4]) {\n" +
            "      Primary Sector Sub-Sector -> {27=[Aliens_Health (id: 27, eid: 29, coos: {sectors.Primary=(level: 1, id: 29)}, p: 0.23)]}\n" +
            "      Project Title -> {27=[ZUZU (id: 27, eid: 27)]}\n" +
            "    }\n" +         
            "  GroupReportData: Finland (id: [79]):\n" +
            "    crd ColumnReportData: Health (id: [4]) {\n" +
            "      Primary Sector Sub-Sector -> {13=[Aliens_Health (id: 13, eid: 29, coos: {sectors.Primary=(level: 1, id: 29)}, p: 0.19)]}\n" +
            "      Project Title -> {13=[ZURA (id: 13, eid: 13)]}\n" +
            "    }\n" +
            "    crd ColumnReportData: Unknown (id: [-3]) {\n" + 
            "      Primary Sector Sub-Sector -> {28=[Randomness_Health (id: 28, eid: 33, coos: {sectors.Primary=(level: 1, id: 33)}, p: 0.15)]}\n" +
            "      Project Title -> {28=[JUJU (id: 28, eid: 28)]}\n" +
            "    }\n" +
            "    crd ColumnReportData: Unknown (id: [-5]) {\n" +
            "      Primary Sector Sub-Sector -> {29=[Ahem_Health (id: 29, eid: 34, coos: {sectors.Primary=(level: 1, id: 34)}, p: 0.55)]}\n" +
            "      Project Title -> {29=[LOLA (id: 29, eid: 29)]}\n" +
            "    }\n" +
            "  GroupReportData: Undefined (id: [-11]):\n" +
            "    crd ColumnReportData: Health (id: [3]) {\n" +
            "      Primary Sector Sub-Sector -> {17=[Seniors_Health (id: 17, eid: 32, coos: {sectors.Primary=(level: 1, id: 32)}, p: 0.72)]}\n" +
            "      Project Title -> {17=[TURA (id: 17, eid: 17)]}\n" +
            "    }\n" +
            "    crd ColumnReportData: Health (id: [4]) {\n" +
            "      Primary Sector Sub-Sector -> {21=[Zombies_Health (id: 21, eid: 34, coos: {sectors.Primary=(level: 1, id: 34)}, p: 0.32)]}\n" +
            "      Project Title -> {21=[JORA (id: 21, eid: 21)]}\n" +
            "    }\n" +
            "    crd ColumnReportData: some_sector_1 (id: [64]) {\n" +
            "      Primary Sector Sub-Sector -> {111=[say_something_1 (id: 111, eid: 120, coos: {sectors.Primary=(level: 1, id: 120)}, p: 0.50)]}\n" +
            "      Project Title -> {111=[some_project_1 (id: 111, eid: 111)]}\n" +
            "    }\n" +
            "    crd ColumnReportData: some_sector_2 (id: [65]) {\n" +
            "      Primary Sector Sub-Sector -> {112=[say_something_2 (id: 112, eid: 121, coos: {sectors.Primary=(level: 1, id: 121)}, p: 0.35)]}\n" +
            "      Project Title -> {112=[some_project_2 (id: 112, eid: 112)]}\n" +
            "    }\n" +
            "    crd ColumnReportData: Undefined (id: [-7]) {\n" + 
            "      Primary Sector Sub-Sector -> {116=[say_something_2 (id: 116, eid: 121, coos: {sectors.Primary=(level: 1, id: 121)}, p: 0.35)]}\n" +
            "      Project Title -> {116=[klara (id: 116, eid: 116)]}\n" +
            "    }\n" +
            "  GroupReportData: Undefined (id: [-12]):\n" +
            "    crd ColumnReportData: Health (id: [3]) {\n" +
            "      Primary Sector Sub-Sector -> {22=[Seniors_Health (id: 22, eid: 32, coos: {sectors.Primary=(level: 1, id: 32)}, p: 0.62)]}\n" +
            "      Project Title -> {22=[HAHA (id: 22, eid: 22)]}\n" +
            "    }\n" +
            "    crd ColumnReportData: Health (id: [4]) {\n" +
            "      Primary Sector Sub-Sector -> {25=[Zombies_Health (id: 25, eid: 34, coos: {sectors.Primary=(level: 1, id: 34)}, p: 0.57)]}\n" +
            "      Project Title -> {25=[JAJA (id: 25, eid: 25)]}\n" +
            "    }\n" +
            "    crd ColumnReportData: some_sector_3 (id: [66]) {\n" +
            "      Primary Sector Sub-Sector -> {113=[say_something_3 (id: 113, eid: 123, coos: {sectors.Primary=(level: 1, id: 123)}, p: 0.60)]}\n" +
            "      Project Title -> {113=[some_project_3 (id: 113, eid: 113)]}\n" +
            "    }\n" + 
            "    crd ColumnReportData: Undefined (id: [-7]) {\n" + 
            "      Primary Sector Sub-Sector -> {117=[say_something_4 (id: 117, eid: 124, coos: {sectors.Primary=(level: 1, id: 124)}, p: 0.55)]}\n" +
            "      Project Title -> {117=[vlara (id: 117, eid: 117)]}\n" +
            "    }\n" + 
            "    crd ColumnReportData: Undefined (id: [-9]) {\n" + 
            "      Primary Sector Sub-Sector -> {118=[say_something_5 (id: 118, eid: 205, coos: {sectors.Primary=(level: 1, id: 205)}, p: 0.34)]}\n" +
            "      Project Title -> {118=[zoso (id: 118, eid: 118)]}\n" +
            "    }",
            asDebugString(grd.accept(new ReportHierarchiesCollapser(ReportCollapsingStrategy.NEVER, leaves))));
        
        
        assertEquals(
            "GroupReportData: Default (id: [38]):\n" + 
            "  GroupReportData: Finland (id: [78]):\n" +
            "    crd ColumnReportData: Health (id: [3]) {\n" +
            "      Primary Sector Sub-Sector -> {17=[Seniors_Health (id: 17, eid: 32, coos: {sectors.Primary=(level: 1, id: 32)}, p: 0.72)]}\n" +
            "      Project Title -> {17=[TURA (id: 17, eid: 17)]}\n" +
            "    }\n" +
            "    crd ColumnReportData: Unknown (id: [-3, -4]) {\n" +
            "      Primary Sector Sub-Sector -> {26=[Seniors_Health (id: 26, eid: 32, coos: {sectors.Primary=(level: 1, id: 32)}, p: 0.10)], 27=[Aliens_Health (id: 27, eid: 29, coos: {sectors.Primary=(level: 1, id: 29)}, p: 0.23)]}\n" +
            "      Project Title -> {26=[VAVA (id: 26, eid: 26)], 27=[ZUZU (id: 27, eid: 27)]}\n" +
            "    }\n" +
            "  GroupReportData: Finland (id: [79]):\n" +
            "    crd ColumnReportData: Health (id: [4]) {\n" +
            "      Primary Sector Sub-Sector -> {13=[Aliens_Health (id: 13, eid: 29, coos: {sectors.Primary=(level: 1, id: 29)}, p: 0.19)]}\n" +
            "      Project Title -> {13=[ZURA (id: 13, eid: 13)]}\n" +
            "    }\n" +
            "    crd ColumnReportData: Unknown (id: [-3, -5]) {\n" +
            "      Primary Sector Sub-Sector -> {28=[Randomness_Health (id: 28, eid: 33, coos: {sectors.Primary=(level: 1, id: 33)}, p: 0.15)], 29=[Ahem_Health (id: 29, eid: 34, coos: {sectors.Primary=(level: 1, id: 34)}, p: 0.55)]}\n" +
            "      Project Title -> {28=[JUJU (id: 28, eid: 28)], 29=[LOLA (id: 29, eid: 29)]}\n" +
            "    }\n" +
            "  GroupReportData: USAID (id: [99]):\n" +
            "    crd ColumnReportData: Education (id: [7]) {\n" +
            "      Primary Sector Sub-Sector -> {15=[Developers_Education (id: 15, eid: 71, coos: {sectors.Primary=(level: 1, id: 71)}, p: 0.30)]}\n" +
            "      Project Title -> {15=[EEE (id: 15, eid: 15)]}\n" +
            "    }\n" +
            "    crd ColumnReportData: Health (id: [3]) {\n" +
            "      Primary Sector Sub-Sector -> {2=[Children_Health (id: 2, eid: 31, coos: {sectors.Primary=(level: 1, id: 31)}, p: 0.75)]}\n" +
            "      Project Title -> {2=[BBB (id: 2, eid: 2)]}\n" +
            "    }\n" +
            "    crd ColumnReportData: Health (id: [4]) {\n" +
            "      Primary Sector Sub-Sector -> {14=[Dads_Health (id: 14, eid: 41, coos: {sectors.Primary=(level: 1, id: 41)}, p: 0.50)]}\n" +
            "      Project Title -> {14=[DDD (id: 14, eid: 14)]}\n" +
            "    }\n" +
            "    crd ColumnReportData: Undefined (id: [-1, -2]) {\n" +
            "      Primary Sector Sub-Sector -> {1=[Undefined 11 (id: 1, eid: -11, coos: {sectors.Primary=(level: 1, id: -11)}, p: 0.25), Undefined 12 (id: 1, eid: -12, coos: {sectors.Primary=(level: 1, id: -12)}, p: 0.25)], 2=[Undefined 12 (id: 2, eid: -12, coos: {sectors.Primary=(level: 1, id: -12)}, p: 0.30), Undefined 12 (id: 2, eid: -21, coos: {sectors.Primary=(level: 1, id: -21)}, p: 0.75)]}\n" +
            "      Project Title -> {1=[AAA (id: 1, eid: 1)], 2=[BBB (id: 2, eid: 2), BBB (id: 2, eid: 2)], 3=[CCC (id: 3, eid: 3)]}\n" +
            "    }\n" +
            "  GroupReportData: Undefined (id: [-11, -12]):\n" +
            "    crd ColumnReportData: Health (id: [3]) {\n" +
            "      Primary Sector Sub-Sector -> {17=[Seniors_Health (id: 17, eid: 32, coos: {sectors.Primary=(level: 1, id: 32)}, p: 0.72)], 22=[Seniors_Health (id: 22, eid: 32, coos: {sectors.Primary=(level: 1, id: 32)}, p: 0.62)]}\n" +
            "      Project Title -> {17=[TURA (id: 17, eid: 17)], 22=[HAHA (id: 22, eid: 22)]}\n" +
            "    }\n" +
            "    crd ColumnReportData: Health (id: [4]) {\n" +
            "      Primary Sector Sub-Sector -> {21=[Zombies_Health (id: 21, eid: 34, coos: {sectors.Primary=(level: 1, id: 34)}, p: 0.32)], 25=[Zombies_Health (id: 25, eid: 34, coos: {sectors.Primary=(level: 1, id: 34)}, p: 0.57)]}\n" +
            "      Project Title -> {21=[JORA (id: 21, eid: 21)], 25=[JAJA (id: 25, eid: 25)]}\n" +
            "    }\n" +
            "    crd ColumnReportData: some_sector_1 (id: [64]) {\n" +
            "      Primary Sector Sub-Sector -> {111=[say_something_1 (id: 111, eid: 120, coos: {sectors.Primary=(level: 1, id: 120)}, p: 0.50)]}\n" +
            "      Project Title -> {111=[some_project_1 (id: 111, eid: 111)]}\n" +
            "    }\n" +
            "    crd ColumnReportData: some_sector_2 (id: [65]) {\n" + 
            "      Primary Sector Sub-Sector -> {112=[say_something_2 (id: 112, eid: 121, coos: {sectors.Primary=(level: 1, id: 121)}, p: 0.35)]}\n" +
            "      Project Title -> {112=[some_project_2 (id: 112, eid: 112)]}\n" +
            "    }\n" +
            "    crd ColumnReportData: some_sector_3 (id: [66]) {\n" +
            "      Primary Sector Sub-Sector -> {113=[say_something_3 (id: 113, eid: 123, coos: {sectors.Primary=(level: 1, id: 123)}, p: 0.60)]}\n" +
            "      Project Title -> {113=[some_project_3 (id: 113, eid: 113)]}\n" +
            "    }\n" +
            "    crd ColumnReportData: Undefined (id: [-7, -9]) {\n" +
            "      Primary Sector Sub-Sector -> {116=[say_something_2 (id: 116, eid: 121, coos: {sectors.Primary=(level: 1, id: 121)}, p: 0.35)], 117=[say_something_4 (id: 117, eid: 124, coos: {sectors.Primary=(level: 1, id: 124)}, p: 0.55)], 118=[say_something_5 (id: 118, eid: 205, coos: {sectors.Primary=(level: 1, id: 205)}, p: 0.34)]}\n" +
            "      Project Title -> {116=[klara (id: 116, eid: 116)], 117=[vlara (id: 117, eid: 117)], 118=[zoso (id: 118, eid: 118)]}\n" +
            "    }",
            asDebugString(grd.accept(new ReportHierarchiesCollapser(ReportCollapsingStrategy.UNKNOWNS, leaves))));
        
        assertEquals(
            "GroupReportData: Default (id: [38]):\n" + 
            "  GroupReportData: Finland (id: [78, 79]):\n" +
            "    crd ColumnReportData: Health (id: [3, 4]) {\n" +
            "      Primary Sector Sub-Sector -> {17=[Seniors_Health (id: 17, eid: 32, coos: {sectors.Primary=(level: 1, id: 32)}, p: 0.72)], 13=[Aliens_Health (id: 13, eid: 29, coos: {sectors.Primary=(level: 1, id: 29)}, p: 0.19)]}\n" +
            "      Project Title -> {17=[TURA (id: 17, eid: 17)], 13=[ZURA (id: 13, eid: 13)]}\n" +
            "    }\n" +
            "    crd ColumnReportData: Unknown (id: [-3, -4, -5]) {\n" +
            "      Primary Sector Sub-Sector -> {26=[Seniors_Health (id: 26, eid: 32, coos: {sectors.Primary=(level: 1, id: 32)}, p: 0.10)], 27=[Aliens_Health (id: 27, eid: 29, coos: {sectors.Primary=(level: 1, id: 29)}, p: 0.23)], 28=[Randomness_Health (id: 28, eid: 33, coos: {sectors.Primary=(level: 1, id: 33)}, p: 0.15)], 29=[Ahem_Health (id: 29, eid: 34, coos: {sectors.Primary=(level: 1, id: 34)}, p: 0.55)]}\n" +
            "      Project Title -> {26=[VAVA (id: 26, eid: 26)], 27=[ZUZU (id: 27, eid: 27)], 28=[JUJU (id: 28, eid: 28)], 29=[LOLA (id: 29, eid: 29)]}\n" +
            "    }\n" +
            "  GroupReportData: USAID (id: [99]):\n" +
            "    crd ColumnReportData: Education (id: [7]) {\n" +
            "      Primary Sector Sub-Sector -> {15=[Developers_Education (id: 15, eid: 71, coos: {sectors.Primary=(level: 1, id: 71)}, p: 0.30)]}\n" +
            "      Project Title -> {15=[EEE (id: 15, eid: 15)]}\n" +
            "    }\n" +
            "    crd ColumnReportData: Health (id: [3, 4]) {\n" +
            "      Primary Sector Sub-Sector -> {2=[Children_Health (id: 2, eid: 31, coos: {sectors.Primary=(level: 1, id: 31)}, p: 0.75)], 14=[Dads_Health (id: 14, eid: 41, coos: {sectors.Primary=(level: 1, id: 41)}, p: 0.50)]}\n" +
            "      Project Title -> {2=[BBB (id: 2, eid: 2)], 14=[DDD (id: 14, eid: 14)]}\n" +
            "    }\n" +
            "    crd ColumnReportData: Undefined (id: [-1, -2]) {\n" +
            "      Primary Sector Sub-Sector -> {1=[Undefined 11 (id: 1, eid: -11, coos: {sectors.Primary=(level: 1, id: -11)}, p: 0.25), Undefined 12 (id: 1, eid: -12, coos: {sectors.Primary=(level: 1, id: -12)}, p: 0.25)], 2=[Undefined 12 (id: 2, eid: -12, coos: {sectors.Primary=(level: 1, id: -12)}, p: 0.30), Undefined 12 (id: 2, eid: -21, coos: {sectors.Primary=(level: 1, id: -21)}, p: 0.75)]}\n" +
            "      Project Title -> {1=[AAA (id: 1, eid: 1)], 2=[BBB (id: 2, eid: 2), BBB (id: 2, eid: 2)], 3=[CCC (id: 3, eid: 3)]}\n" +
            "    }\n" +
            "  GroupReportData: Undefined (id: [-11, -12]):\n" +
            "    crd ColumnReportData: Health (id: [3, 4]) {\n" +
            "      Primary Sector Sub-Sector -> {17=[Seniors_Health (id: 17, eid: 32, coos: {sectors.Primary=(level: 1, id: 32)}, p: 0.72)], 21=[Zombies_Health (id: 21, eid: 34, coos: {sectors.Primary=(level: 1, id: 34)}, p: 0.32)], 22=[Seniors_Health (id: 22, eid: 32, coos: {sectors.Primary=(level: 1, id: 32)}, p: 0.62)], 25=[Zombies_Health (id: 25, eid: 34, coos: {sectors.Primary=(level: 1, id: 34)}, p: 0.57)]}\n" +
            "      Project Title -> {17=[TURA (id: 17, eid: 17)], 21=[JORA (id: 21, eid: 21)], 22=[HAHA (id: 22, eid: 22)], 25=[JAJA (id: 25, eid: 25)]}\n" +
            "    }\n" +
            "    crd ColumnReportData: some_sector_1 (id: [64]) {\n" +
            "      Primary Sector Sub-Sector -> {111=[say_something_1 (id: 111, eid: 120, coos: {sectors.Primary=(level: 1, id: 120)}, p: 0.50)]}\n" +
            "      Project Title -> {111=[some_project_1 (id: 111, eid: 111)]}\n" +
            "    }\n" +
            "    crd ColumnReportData: some_sector_2 (id: [65]) {\n" +
            "      Primary Sector Sub-Sector -> {112=[say_something_2 (id: 112, eid: 121, coos: {sectors.Primary=(level: 1, id: 121)}, p: 0.35)]}\n" +
            "      Project Title -> {112=[some_project_2 (id: 112, eid: 112)]}\n" +
            "    }\n" +
            "    crd ColumnReportData: some_sector_3 (id: [66]) {\n" +
            "      Primary Sector Sub-Sector -> {113=[say_something_3 (id: 113, eid: 123, coos: {sectors.Primary=(level: 1, id: 123)}, p: 0.60)]}\n" +
            "      Project Title -> {113=[some_project_3 (id: 113, eid: 113)]}\n" +
            "    }\n" +
            "    crd ColumnReportData: Undefined (id: [-7, -9]) {\n" +
            "      Primary Sector Sub-Sector -> {116=[say_something_2 (id: 116, eid: 121, coos: {sectors.Primary=(level: 1, id: 121)}, p: 0.35)], 117=[say_something_4 (id: 117, eid: 124, coos: {sectors.Primary=(level: 1, id: 124)}, p: 0.55)], 118=[say_something_5 (id: 118, eid: 205, coos: {sectors.Primary=(level: 1, id: 205)}, p: 0.34)]}\n" +
            "      Project Title -> {116=[klara (id: 116, eid: 116)], 117=[vlara (id: 117, eid: 117)], 118=[zoso (id: 118, eid: 118)]}\n" +
            "    }",
                asDebugString(grd.accept(new ReportHierarchiesCollapser(ReportCollapsingStrategy.ALWAYS, leaves))));
    }
}

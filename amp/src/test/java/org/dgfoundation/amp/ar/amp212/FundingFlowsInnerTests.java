package org.dgfoundation.amp.ar.amp212;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;
import java.util.function.Predicate;

import org.digijava.kernel.translator.LocalizableLabel;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.ComparableValue;
import org.dgfoundation.amp.nireports.amp.MetaCategory;
import org.dgfoundation.amp.nireports.runtime.CellColumn;
import org.dgfoundation.amp.nireports.runtime.ColumnContents;
import org.dgfoundation.amp.nireports.runtime.ColumnReportData;
import org.dgfoundation.amp.nireports.runtime.NiCell;
import org.dgfoundation.amp.nireports.runtime.VSplitStrategy;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;
import org.dgfoundation.amp.nireports.testcases.LoggingIdsAcceptors;
import org.dgfoundation.amp.nireports.testcases.generic.HardcodedReportsTestSchema;
import org.dgfoundation.amp.nireports.testcases.generic.dimensions.OrganizationsTestDimension;
import org.digijava.module.aim.helper.Constants;
import org.junit.Test;

public class FundingFlowsInnerTests extends NiTestCase {
        
    public FundingFlowsInnerTests() {
        super(HardcodedReportsTestSchema.getInstance());
    }
    
    @Test
    public void testDirectedMeasureBehaviourPercentageListener() {
        Predicate<NiDimensionUsage> pred = beh.getHierarchiesListener();
        assertTrue(pred.test(HardcodedReportsTestSchema.SS_DIM_USG));
        assertTrue(pred.test(HardcodedReportsTestSchema.PS_DIM_USG));
        assertTrue(pred.test(HardcodedReportsTestSchema.PP_DIM_USG));
        assertTrue(pred.test(HardcodedReportsTestSchema.LOC_DIM_USG));
        
        assertFalse(pred.test(HardcodedReportsTestSchema.DONOR_DIM_USG));
        assertFalse(pred.test(HardcodedReportsTestSchema.IA_DIM_USG));
    }
    
    @Test
    public void testDirectedMeasureBehaviourMisc() {
        
        // should return True for both null and non-null column
        assertTrue(beh.shouldDeleteLeafIfEmpty(null));
        assertTrue(beh.shouldDeleteLeafIfEmpty(new CellColumn("some-name", new LocalizableLabel("some-name"), new ColumnContents(Collections.emptyList()), null, schema.getColumns().get(ColumnConstants.PROJECT_TITLE), null)));
    }

    @SuppressWarnings("static-access")
    @Test
    public void testFlowName() {
        assertEquals("DN-EXEC", beh.getFlowName(buildDirectedCell(10, "DN-EXEC")));
        assertEquals("DN-DN", beh.getFlowName(buildDirectedCell(210, "DN-DN")));
        shouldFail(() -> beh.getFlowName(buildDirectedCell(99, null)));
        // cell.getMetaInfo().getMetaInfo(MetaCategory.DIRECTED_TRANSACTION_FLOW.category).v.toString();
    }
    
    @Test
    public void testSubMeasureStrategy() {
        List<VSplitStrategy> strats = beh.getSubMeasureHierarchies(null); // we can put "null" here because the current implementation does not depend on the engine
        assertEquals(1, strats.size());
        VSplitStrategy soleStrat = strats.get(0);
        ComparableValue<String> cat1 = soleStrat.categorize(buildNiCell(10, "DN-IMPL"));
        ComparableValue<String> cat2 = soleStrat.categorize(buildNiCell(10, "IMPL-EXEC"));
        ComparableValue<String> cat3 = soleStrat.categorize(buildNiCell(10, "DN-IMPL"));
        assertEquals("(<DN-IMPL>: DN-IMPL)", cat1.toString());
        assertEquals("(<IMPL-EXEC>: IMPL-EXEC)", cat2.toString());
        assertEquals("(<DN-IMPL>: DN-IMPL)", cat3.toString());
        assertEquals("[(<DN-IMPL>: DN-IMPL), (<IMPL-EXEC>: IMPL-EXEC)]", soleStrat.getSubcolumnsNames(new TreeSet<>(Arrays.asList(cat2, cat3, cat2, cat1)), false).toString());
    }
    
    void testFiltering(NiCell splitterCell, NiCell fundingCell, String expectedAcceptorCalls, boolean shouldPass, boolean isTransactionLevel) {
        LoggingIdsAcceptors acceptor = new LoggingIdsAcceptors(splitterCell.getCell());
        Cell filteredCell = beh.filterCell(acceptor.getAcceptors(), fundingCell.getCell(), splitterCell.getCell(), isTransactionLevel);
        if (expectedAcceptorCalls != null) {
            assertEquals(expectedAcceptorCalls, acceptor.getCalls().toString());
        }
        
        if (shouldPass) {
            assertTrue(filteredCell == fundingCell.getCell());
        } else {
            assertTrue(filteredCell == null);
        }
    }

    void testFiltering(NiCell splitterCell, NiCell fundingCell, String expectedAcceptorCalls, boolean shouldPass) {
        testFiltering(splitterCell, fundingCell, expectedAcceptorCalls, shouldPass, false);
    }

    @Test
    public void testFilterCellByDonor() {
        //NiCell primSectorSplitterCell = buildColumnNiCell(buildCell(10, buildMetaInfo(), buildCoos(HardcodedReportsTestSchema.DONOR_DIM_USG, new NiDimension.Coordinate(2, 2l))), ColumnConstants.PRIMARY_SECTOR);
        NiCell donorSplitterCell = buildColumnNiCell(textCell("USAID", 21696l, HardcodedReportsTestSchema.DONOR_DIM_USG.getLevelColumn(2)), ColumnConstants.DONOR_AGENCY);
        NiCell donorGroupSplitterCell = buildColumnNiCell(textCell("American", 19l, HardcodedReportsTestSchema.DONOR_DIM_USG.getLevelColumn(1)), ColumnConstants.DONOR_GROUP);
                
        NiCell fundingCell = buildMeasureNiCell(buildCell(100, 
                buildMetaInfo(MetaCategory.SOURCE_ROLE, Constants.FUNDING_AGENCY, MetaCategory.SOURCE_ORG, 21696l, MetaCategory.RECIPIENT_ROLE, Constants.EXECUTING_AGENCY, MetaCategory.RECIPIENT_ORG, 21701l), 
                buildCoos(HardcodedReportsTestSchema.DONOR_DIM_USG, new NiDimension.Coordinate(OrganizationsTestDimension.LEVEL_2, 21694l))), MeasureConstants.REAL_COMMITMENTS);
        
        testFiltering(donorSplitterCell, fundingCell, "[orgs.DN: (level: 2, id: 21696) -> true]", true);
        testFiltering(donorGroupSplitterCell, fundingCell, "[orgs.DN: (level: 2, id: 21696) -> true]", true);
    }

    /**
     * tests that cells are behaving correctly under hiers/filters by "undefined"
     */
    @Test
    public void testFilterCellByTransactionLevelHierarchy() {
        LevelColumn toa = HardcodedReportsTestSchema.catsDimension.getLevelColumn("toa", 1);
        //NiCell primSectorSplitterCell = buildColumnNiCell(buildCell(10, buildMetaInfo(), buildCoos(HardcodedReportsTestSchema.DONOR_DIM_USG, new NiDimension.Coordinate(2, 2l))), ColumnConstants.PRIMARY_SECTOR);
        NiCell defaultSplitterCell = buildColumnNiCell(textCell("default type of assistance", 2119l, toa), ColumnConstants.TYPE_OF_ASSISTANCE);
        NiCell secondSplitterCell = buildColumnNiCell(textCell("second type of assistance", 2124l, toa), ColumnConstants.TYPE_OF_ASSISTANCE);
        NiCell undefSplitterCell = buildColumnNiCell(textCell("Undefined type of assistance", ColumnReportData.UNALLOCATED_ID, toa), ColumnConstants.TYPE_OF_ASSISTANCE);
    
        NiCell defaultFundingCell = buildMeasureNiCell(buildCell(100, 
            buildMetaInfo(), 
            buildCoos(toa.dimensionUsage, new NiDimension.Coordinate(toa.level, 2119l))), 
            MeasureConstants.ACTUAL_COMMITMENTS);

        NiCell secondFundingCell = buildMeasureNiCell(buildCell(100, 
            buildMetaInfo(), 
            buildCoos(toa.dimensionUsage, new NiDimension.Coordinate(toa.level, 2124l))), 
            MeasureConstants.ACTUAL_COMMITMENTS);

        NiCell undefinedFundingCell = buildMeasureNiCell(buildCell(100, 
            buildMetaInfo(), 
            buildCoos(toa.dimensionUsage, new NiDimension.Coordinate(toa.level, ColumnReportData.UNALLOCATED_ID))), 
            MeasureConstants.ACTUAL_COMMITMENTS);

        NiCell unspeccedFundingCell = buildMeasureNiCell(buildCell(100, 
            buildMetaInfo(), 
            Collections.emptyMap()), 
            MeasureConstants.ACTUAL_COMMITMENTS);

        List<NiCell> allFundingCells = Arrays.asList(defaultFundingCell, secondFundingCell, undefinedFundingCell, unspeccedFundingCell);

        for(NiCell fcell:allFundingCells)
            testFiltering(defaultSplitterCell, fcell, null, fcell == defaultFundingCell || fcell == unspeccedFundingCell);

        for(NiCell fcell:allFundingCells)
            testFiltering(secondSplitterCell, fcell, null, fcell == secondFundingCell || fcell == unspeccedFundingCell);

        for(NiCell fcell:allFundingCells)
            testFiltering(undefSplitterCell, fcell, null, fcell == undefinedFundingCell || fcell == unspeccedFundingCell);

        for(NiCell fcell:allFundingCells)
            testFiltering(defaultSplitterCell, fcell, null, fcell == defaultFundingCell, true);

        for(NiCell fcell:allFundingCells)
            testFiltering(secondSplitterCell, fcell, null, fcell == secondFundingCell, true);

        for(NiCell fcell:allFundingCells)
            testFiltering(undefSplitterCell, fcell, null, fcell == undefinedFundingCell || fcell == unspeccedFundingCell, true);
    }

    @Test
    public void testFilterForeignCellByImplementingAgency() {
        NiCell iaSplitterCell = buildColumnNiCell(percentageTextCell("USAID", 21696l, 0.25, HardcodedReportsTestSchema.IA_DIM_USG.getLevelColumn(2)), ColumnConstants.IMPLEMENTING_AGENCY);
        NiCell iaGroupSplitterCell = buildColumnNiCell(percentageTextCell("American", 19l, 0.4, HardcodedReportsTestSchema.IA_DIM_USG.getLevelColumn(1)), ColumnConstants.IMPLEMENTING_AGENCY_GROUPS);

        NiCell fundingCell = buildMeasureNiCell(buildCell(100, 
                buildMetaInfo(MetaCategory.SOURCE_ROLE, Constants.FUNDING_AGENCY, MetaCategory.SOURCE_ORG, 21696l, MetaCategory.RECIPIENT_ROLE, Constants.EXECUTING_AGENCY, MetaCategory.RECIPIENT_ORG, 21701l), 
                buildCoos(HardcodedReportsTestSchema.DONOR_DIM_USG, new NiDimension.Coordinate(OrganizationsTestDimension.LEVEL_2, 21694l))), MeasureConstants.REAL_COMMITMENTS);

        testFiltering(iaSplitterCell, fundingCell, "[]", false);
        testFiltering(iaGroupSplitterCell, fundingCell, "[]", false);
    }
    
    @Test
    public void testFilterByImplementingAgency() {
        NiCell iaSplitterCell = buildColumnNiCell(percentageTextCell("USAID", 21696l, 0.25, HardcodedReportsTestSchema.IA_DIM_USG.getLevelColumn(2)), ColumnConstants.IMPLEMENTING_AGENCY);
        NiCell iaGroupSplitterCell = buildColumnNiCell(percentageTextCell("American", 19l, 0.4, HardcodedReportsTestSchema.IA_DIM_USG.getLevelColumn(1)), ColumnConstants.IMPLEMENTING_AGENCY_GROUPS);

        NiCell fundingCell = buildMeasureNiCell(buildCell(100, 
                buildMetaInfo(MetaCategory.SOURCE_ROLE, Constants.IMPLEMENTING_AGENCY, MetaCategory.SOURCE_ORG, 21696l, MetaCategory.RECIPIENT_ROLE, Constants.EXECUTING_AGENCY, MetaCategory.RECIPIENT_ORG, 21701l),
                buildCoos(HardcodedReportsTestSchema.DONOR_DIM_USG, new NiDimension.Coordinate(OrganizationsTestDimension.LEVEL_2, 21694l))), MeasureConstants.REAL_COMMITMENTS);

        testFiltering(iaSplitterCell, fundingCell, "[orgs.IA: (level: 2, id: 21696) -> true]", true);
        testFiltering(iaGroupSplitterCell, fundingCell, "[orgs.IA: (level: 2, id: 21696) -> true]", true);
        
        NiCell iaAdvancedCell = fundingCell.advanceHierarchy(fundingCell.getCell(), iaSplitterCell.getCell(), Collections.emptyMap());
        assertEquals("1", iaAdvancedCell.getHiersTracker().calculatePercentage(beh.getHierarchiesListener()).toString());
        
        NiCell iaGroupAdvancedCell = fundingCell.advanceHierarchy(fundingCell.getCell(), iaGroupSplitterCell.getCell(), Collections.emptyMap());
        assertEquals("1", iaGroupAdvancedCell.getHiersTracker().calculatePercentage(beh.getHierarchiesListener()).toString());
    }

}

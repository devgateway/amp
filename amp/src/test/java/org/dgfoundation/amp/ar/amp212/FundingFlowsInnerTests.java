package org.dgfoundation.amp.ar.amp212;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;
import java.util.function.Predicate;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.ComparableValue;
import org.dgfoundation.amp.nireports.amp.MetaCategory;
import org.dgfoundation.amp.nireports.runtime.CellColumn;
import org.dgfoundation.amp.nireports.runtime.ColumnContents;
import org.dgfoundation.amp.nireports.runtime.NiCell;
import org.dgfoundation.amp.nireports.runtime.VSplitStrategy;
import org.dgfoundation.amp.nireports.schema.DimensionLevel;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;
import org.dgfoundation.amp.testmodels.HardcodedReportsTestSchema;
import org.dgfoundation.amp.testmodels.LoggingIdsAcceptors;
import org.dgfoundation.amp.testmodels.dimensions.OrganizationsTestDimension;
import org.digijava.module.aim.helper.Constants;
import org.junit.Test;

public class FundingFlowsInnerTests extends NiTestCase {
		
	public FundingFlowsInnerTests() {
		super("FundingFlows tests", HardcodedReportsTestSchema.getInstance());
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
		assertTrue(beh.shouldDeleteLeafIfEmpty(new CellColumn("some-name", new ColumnContents(Collections.emptyList()), null, schema.getColumns().get(ColumnConstants.PROJECT_TITLE), null)));
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
		assertEquals("[(<DN-IMPL>: DN-IMPL), (<IMPL-EXEC>: IMPL-EXEC)]", soleStrat.getSubcolumnsNames(new TreeSet<>(Arrays.asList(cat2, cat3, cat2, cat1))).toString());
	}
	
	void testFiltering(NiCell splitterCell, NiCell fundingCell, String expectedAcceptorCalls, boolean shouldPass) {
		LoggingIdsAcceptors acceptor = new LoggingIdsAcceptors(splitterCell.getCell());
		Cell filteredCell = beh.filterCell(acceptor.getAcceptors(), fundingCell.getCell(), splitterCell.getCell());
		assertEquals(expectedAcceptorCalls, acceptor.getCalls().toString());
		
		if (shouldPass) {
			assertTrue(filteredCell == fundingCell.getCell());
		} else {
			assertTrue(filteredCell == null);
		}
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
		
		NiCell iaAdvancedCell = fundingCell.advanceHierarchy(fundingCell.getCell(), iaSplitterCell.getCell());
		assertEquals("1", iaAdvancedCell.getHiersTracker().calculatePercentage(beh.getHierarchiesListener()).toString());
		
		NiCell iaGroupAdvancedCell = fundingCell.advanceHierarchy(fundingCell.getCell(), iaGroupSplitterCell.getCell());
		assertEquals("1", iaGroupAdvancedCell.getHiersTracker().calculatePercentage(beh.getHierarchiesListener()).toString());
	}

}

package org.dgfoundation.amp.ar.amp212;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;
import java.util.function.Predicate;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
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
import org.digijava.module.aim.helper.Constants;
import org.junit.Test;

public class FundingFlowsInnerTests extends NiTestCase {
		
	public FundingFlowsInnerTests() {
		super("FundingFlows tests");
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
		assertTrue(beh.shouldDeleteLeafIfEmpty(new CellColumn("some-name", new ColumnContents(Collections.emptyList()), null, HardcodedReportsTestSchema.getInstance().getColumns().get(ColumnConstants.PROJECT_TITLE), null)));
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
	
	@Test
	public void testFilterCell() {
		NiCell primSectorSplitterCell = buildColumnNiCell(buildCell(10, buildMetaInfo(), buildCoos(HardcodedReportsTestSchema.DONOR_DIM_USG, new NiDimension.Coordinate(2, 2l))), ColumnConstants.PRIMARY_SECTOR);
		NiCell fundingCell = buildMeasureNiCell(buildCell(100, 
				buildMetaInfo(MetaCategory.SOURCE_ROLE, Constants.FUNDING_AGENCY, MetaCategory.SOURCE_ORG, 2l, MetaCategory.RECIPIENT_ROLE, Constants.EXECUTING_AGENCY, MetaCategory.RECIPIENT_ORG, 15l), 
				buildCoos()), MeasureConstants.REAL_COMMITMENTS);
		//beh.
	}
}

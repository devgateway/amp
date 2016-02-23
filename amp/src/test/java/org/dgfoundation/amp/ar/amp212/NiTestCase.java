package org.dgfoundation.amp.ar.amp212;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.MonetaryAmount;
import org.dgfoundation.amp.nireports.NiUtils;
import org.dgfoundation.amp.nireports.TranslatedDate;
import org.dgfoundation.amp.nireports.amp.AmpPrecisionSetting;
import org.dgfoundation.amp.nireports.amp.DirectedMeasureBehaviour;
import org.dgfoundation.amp.nireports.amp.MetaCategory;
import org.dgfoundation.amp.nireports.meta.MetaInfoGenerator;
import org.dgfoundation.amp.nireports.meta.MetaInfoSet;
import org.dgfoundation.amp.nireports.runtime.CacheHitsCounter;
import org.dgfoundation.amp.nireports.runtime.HierarchiesTracker;
import org.dgfoundation.amp.nireports.runtime.NiCell;
import org.dgfoundation.amp.nireports.schema.NiDimension.Coordinate;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;
import org.dgfoundation.amp.testmodels.HardcodedReportsTestSchema;
import org.dgfoundation.amp.testutils.AmpTestCase;

public class NiTestCase extends AmpTestCase {
	
	final String TCN = "TotalColumnsName";
	
	final DirectedMeasureBehaviour beh = new DirectedMeasureBehaviour(TCN);
	final MetaInfoGenerator metaInfoGenerator = new MetaInfoGenerator();
	final CacheHitsCounter chc = new CacheHitsCounter();
	
	protected NiTestCase(String name) {
		super(name);
	}
	
	protected Cell buildDirectedCell(int amount, String flow) {
		MetaInfoSet metaInfo = new MetaInfoSet(metaInfoGenerator);
		if (flow != null)
			metaInfo.add(MetaCategory.DIRECTED_TRANSACTION_FLOW.category, flow);
		CategAmountCell cell = new CategAmountCell(1l, new MonetaryAmount(BigDecimal.valueOf(amount), new AmpPrecisionSetting()), metaInfo, Collections.emptyMap(), new TranslatedDate(2000, "2000", 1, 1, "January"));
		return cell;
	}
	
	protected Cell buildCell(int amount, MetaInfoSet metaInfo, Map<NiDimensionUsage, Coordinate> coos) {
		CategAmountCell cell = new CategAmountCell(1, new MonetaryAmount(BigDecimal.valueOf(amount), new AmpPrecisionSetting()), metaInfo, coos, new TranslatedDate(2000, "2000", 1, 1, "January"));
		return cell;
	}
	
	protected MetaInfoSet buildMetaInfo(Object...z) {
		NiUtils.failIf(z.length % 2 != 0, "please supply an even number of arguments");
		MetaInfoSet metaInfo = new MetaInfoSet(metaInfoGenerator);
		for(int i = 0; i < z.length / 2; i++) {
			MetaCategory mc = (MetaCategory) z[2 * i];
			Object val = z[2 * i + 1];
			NiUtils.failIf(metaInfo.hasMetaInfo(mc.category), () -> String.format("you tried to add cat twice: %s", mc.category));
			metaInfo.add(mc.category, val);
		}
		return metaInfo;
	}
	
	protected Map<NiDimensionUsage, Coordinate> buildCoos(Object...z) {
		NiUtils.failIf(z.length % 2 != 0, "please supply an even number of arguments");
		Map<NiDimensionUsage, Coordinate> res = new HashMap<>();
		for(int i = 0; i < z.length / 2; i++) {
			NiDimensionUsage dimUsg = (NiDimensionUsage) z[2 * i];
			Coordinate coo = (Coordinate) z[2 * i + 1];
			res.put(dimUsg, coo);
		}
		return res;
	}
	
	protected NiCell buildNiCell(int amount, String flow) {
		Cell cell = buildDirectedCell(amount, flow);
		return new NiCell(cell, HardcodedReportsTestSchema.getInstance().getMeasures().get(MeasureConstants.ACTUAL_COMMITMENTS), HierarchiesTracker.buildEmpty(chc));
	}
	
	protected NiCell buildMeasureNiCell(Cell cell, String measureName) {
		return new NiCell(cell, HardcodedReportsTestSchema.getInstance().getMeasures().get(measureName), HierarchiesTracker.buildEmpty(chc));
	}

	protected NiCell buildColumnNiCell(Cell cell, String columnName) {
		return new NiCell(cell, HardcodedReportsTestSchema.getInstance().getColumns().get(columnName), HierarchiesTracker.buildEmpty(chc));
	}
}

package org.digijava.kernel.ampapi.saiku.util;

import java.util.Locale;
import mondrian.util.Format;

public class MyTotalAggregator extends org.saiku.service.olap.totals.aggregators.SumAggregator {

	protected MyTotalAggregator() {
		super(new Format("%.2f", Locale.US));
	}
}

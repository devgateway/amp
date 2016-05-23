package org.dgfoundation.amp.testmodels;

import java.util.function.Function;

import org.dgfoundation.amp.newreports.CalendarConverter;
import org.dgfoundation.amp.nireports.NiPrecisionSetting;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.SchemaSpecificScratchpad;
import org.dgfoundation.amp.nireports.amp.AmpPrecisionSetting;
import org.dgfoundation.amp.nireports.runtime.CachingCalendarConverter;

public class ReportsTestScratchpad implements SchemaSpecificScratchpad {

	protected final NiPrecisionSetting precisionSetting = new AmpPrecisionSetting();
	protected final NiReportsEngine engine;
	
	public ReportsTestScratchpad(NiReportsEngine engine) {
		this.engine = engine;
	}

	@Override
	public void close() throws Exception {
	}

	@Override
	public NiPrecisionSetting getPrecisionSetting() {
		return precisionSetting;
	}

	@Override
	public CalendarConverter getDefaultCalendar() {
		return new TestCalendar();
		
	}

	@Override
	public CachingCalendarConverter buildCalendarConverter() {
		CalendarConverter cc = this.buildUnderlyingCalendarConverter(engine.spec);
		return new CachingCalendarConverter(cc, cc.getDefaultFiscalYearPrefix(), Function.identity());
	}
}

package org.dgfoundation.amp.testmodels;

import org.dgfoundation.amp.newreports.CalendarConverter;
import org.dgfoundation.amp.nireports.NiPrecisionSetting;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.SchemaSpecificScratchpad;
import org.dgfoundation.amp.nireports.amp.AmpPrecisionSetting;

public class ReportsTestScratchpad implements SchemaSpecificScratchpad {

	protected final NiPrecisionSetting precisionSetting = new AmpPrecisionSetting();
	
	public ReportsTestScratchpad(NiReportsEngine engine) {
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
}

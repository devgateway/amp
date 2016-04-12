package org.dgfoundation.amp.newreports;

import java.util.List;
import java.util.Map;

import org.digijava.module.aim.dbentity.AmpFiscalCalendar;

/**
 * @author Dolghier Constantin
 *
 */
public interface ReportFilters {
	public Map<ReportElement, List<FilterRule>> getFilterRules();
}

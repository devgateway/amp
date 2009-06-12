package org.digijava.module.parisindicator.helper;

import java.util.Collection;

import org.digijava.module.aim.dbentity.AmpAhsurvey;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpStatus;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

public interface PIOperations {

	public abstract Collection<PIReportAbstractRow> generateReport(Collection<AmpAhsurvey> commonData, int startYear,
			int endYear, String calendar, AmpCurrency currency, Collection<AmpSector> sectorsFilter,
			Collection<AmpCategoryValue> statusFilter, Collection<AmpCategoryValue> financingInstrumentFilter);

	public abstract Collection<PIReportAbstractRow> reportPostProcess(Collection<PIReportAbstractRow> baseReport,
			int startYear, int endYear);
}
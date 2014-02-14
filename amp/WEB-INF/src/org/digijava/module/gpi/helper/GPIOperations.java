package org.digijava.module.gpi.helper;

import java.util.Collection;

import org.digijava.module.aim.dbentity.AmpGPISurvey;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.gpi.helper.row.GPIReportAbstractRow;

public interface GPIOperations {

	public abstract Collection<GPIReportAbstractRow> generateReport(Collection<AmpGPISurvey> commonData, Collection<AmpOrganisation> donors, Collection<AmpOrgGroup> donorGroups, int startYear,
			int endYear, AmpFiscalCalendar calendar, AmpCurrency currency, Collection<AmpSector> sectorsFilter, Collection<AmpCategoryValue> statusFilter,
			Collection<AmpCategoryValue> financingInstrumentFilter);

	public abstract Collection<GPIReportAbstractRow> reportPostProcess(Collection<GPIReportAbstractRow> baseReport, int startYear, int endYear) throws Exception;
}
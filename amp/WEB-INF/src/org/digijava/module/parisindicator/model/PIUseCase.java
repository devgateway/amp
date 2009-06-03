package org.digijava.module.parisindicator.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpAhsurvey;
import org.digijava.module.aim.dbentity.AmpAhsurveyIndicator;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.helper.AmpDonors;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.parisindicator.action.PIAction;
import org.digijava.module.parisindicator.form.PIForm;
import org.digijava.module.parisindicator.helper.PIAbstractReport;
import org.digijava.module.parisindicator.helper.PIReport3;
import org.digijava.module.parisindicator.util.PIConstants;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class PIUseCase {

	private static Logger logger = Logger.getLogger(PIUseCase.class);

	/*
	 * Receives the form and populates the collections used in the filters.
	 * TODO: Replace the form variable for all collections to decouple the
	 * Action from the UseCase, so this method can be used from elsewhere.
	 */
	public PIForm setupFiltersData(PIForm form) {
		if (form.getStatuses() == null || form.getStatuses().isEmpty()) {
			form.setStatuses(DbUtil.getAllActivityStatus());
		}
		if (form.getDonors() == null || form.getDonors().isEmpty()) {
			form.setDonors(DbUtil.getAllDonorOrgs());
		}
		if (form.getSectors() == null || form.getSectors().isEmpty()) {
			form.setSectors(SectorUtil.getAmpSectors());
		}
		if (form.getDonorGroups() == null || form.getDonorGroups().isEmpty()) {
			form.setDonorGroups(DbUtil.getAllOrgGroups());
		}
		return form;
	}

	public void resetFilterSelections(PIForm form) {
		form.setSelectedCalendar(null);
		form.setSelectedCurrency(null);
		form.setSelectedDonors(null);
		form.setSelectedEndYear(0);
		form.setSelectedDonorGroups(null);
		form.setSelectedSectors(null);
		form.setSelectedStartYear(0);
		form.setSelectedStatuses(null);
	}

	/*
	 * Gets the collection of PI reports from the DB and filters any report if
	 * necessary.
	 */
	public Collection<AmpAhsurveyIndicator> setupAvailablePIReports() {
		Collection<AmpAhsurveyIndicator> list = new ArrayList<AmpAhsurveyIndicator>();
		list = DbUtil.getAllAhSurveyIndicators();
		Iterator<AmpAhsurveyIndicator> iter = list.iterator();
		while (iter.hasNext()) {
			AmpAhsurveyIndicator aux = iter.next();
			if (checkReportName(aux.getIndicatorCode()) == null) {
				iter.remove();
			}
		}
		return list;
	}

	/*
	 * Checks the report name with the available PI reports.
	 */
	public String checkReportName(String name) {
		String ret = null;
		if (name == null) {
		} else if (name.equals(PIConstants.PARIS_INDICATOR_REPORT_3)
				|| name.equals(PIConstants.PARIS_INDICATOR_REPORT_4)
				|| name.equals(PIConstants.PARIS_INDICATOR_REPORT_5a)
				|| name.equals(PIConstants.PARIS_INDICATOR_REPORT_5b)
				|| name.equals(PIConstants.PARIS_INDICATOR_REPORT_6)
				|| name.equals(PIConstants.PARIS_INDICATOR_REPORT_7)
				|| name.equals(PIConstants.PARIS_INDICATOR_REPORT_9)
				|| name.equals(PIConstants.PARIS_INDICATOR_REPORT_10a)) {
			ret = name;
		}
		return ret;
	}

	/*
	 * Returns an indicator object from the list of available reports given the
	 * report code.
	 */
	public AmpAhsurveyIndicator getPIReport(String code) {
		AmpAhsurveyIndicator ret = null;
		Collection<AmpAhsurveyIndicator> list = this.setupAvailablePIReports();
		Iterator<AmpAhsurveyIndicator> iter = list.iterator();
		while (iter.hasNext()) {
			AmpAhsurveyIndicator aux = iter.next();
			if (aux.getIndicatorCode().equals(code)) {
				ret = aux;
			}
		}
		return ret;
	}

	/*
	 * Executes part of the common logic for all reports and then creates the
	 * concrete report.
	 */
	public PIAbstractReport createReport(PIForm form) {
		// Create the report.
		PIAbstractReport report = null;
		if (form.getPiReport().getIndicatorCode().equals(
				PIConstants.PARIS_INDICATOR_REPORT_3)) {
			report = new PIReport3();
		}

		// Get the common info from surveys and apply some filters.
		Collection commonData = getCommonSurveyData(
				form.getSelectedStartYear(), form.getSelectedEndYear(), form
						.getSelectedDonors(), form.getSelectedDonorGroups());

		report.generateReport();

		return report;
	}

	/*
	 * Returns a collection that basically has AmpAhSurvey objects plus
	 * organizations info, donor groups info, activities, fundings and fundings
	 * details, for further analysis on each report.
	 */
	public Collection<AmpAhsurvey> getCommonSurveyData(int startYear,
			int endYear, Collection<AmpOrganisation> filterDonors,
			Collection<AmpOrgGroup> filterDonorGroups) {

		Collection<AmpAhsurvey> commonData = null;
		Session session = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			// Set the query to return AmpAhSurvey objects.
			Criteria criteria = session.createCriteria(AmpAhsurvey.class);
			criteria.createAlias("pointOfDeliveryDonor", "podd1");
			criteria.createAlias("pointOfDeliveryDonor.orgTypeId", "podd2");
			// Set the filter for Multilateral and Bilateral PoDDs.
			criteria.add(Restrictions.in("podd2.orgTypeCode", new String[] {
					"MUL", "BIL" }));
			// If needed, filter for organizations.
			if (filterDonors != null) {
				criteria.add(Restrictions.in("pointOfDeliveryDonor",
						filterDonors));
			}
			// If needed, filter for organizations groups.
			if (filterDonorGroups != null) {
				criteria.add(Restrictions.in("podd1.orgGrpId",
						filterDonorGroups));
			}
			commonData = criteria.list();

		} catch (Exception e) {
			logger.error(e);
		}
		return commonData;
	}
}
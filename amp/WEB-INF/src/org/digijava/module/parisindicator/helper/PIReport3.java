package org.digijava.module.parisindicator.helper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpAhsurvey;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpStatus;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.fiscalcalendar.BaseCalendar;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.parisindicator.model.PIUseCase;
import org.digijava.module.parisindicator.util.PIConstants;
import org.digijava.module.parisindicator.util.PIUtils;
import org.hibernate.Query;
import org.hibernate.Session;

public class PIReport3 extends PIAbstractReport {

	private static Logger logger = Logger.getLogger(PIUseCase.class);
	private final String reportCode = PIConstants.PARIS_INDICATOR_REPORT_3;

	public String getReportCode() {
		return this.reportCode;
	}

	@Override
	public Collection<PIReportAbstractRow> generateReport(Collection<AmpAhsurvey> commonData, int startYear,
			int endYear, String calendar, AmpCurrency currency, Collection<AmpSector> sectorsFilter,
			Collection<AmpCategoryValue> statusFilter, Collection<AmpCategoryValue> financingInstrumentFilter) {

		Collection<PIReportAbstractRow> list = new ArrayList<PIReportAbstractRow>();
		PIReport3Row auxRow = null;
		int yearRange = endYear - startYear + 1;
		Date[] startDates = new Date[yearRange];
		Date[] endDates = new Date[yearRange];
		double fromExchangeRate;
		double toExchangeRate;

		try {
			// Setup year ranges according the selected calendar.
			AmpFiscalCalendar fCalendar = FiscalCalendarUtil.getAmpFiscalCalendar(Long.parseLong(calendar));
			for (int i = startYear; i < endYear; i++) {
				if (!(fCalendar.getBaseCal().equalsIgnoreCase(BaseCalendar.BASE_ETHIOPIAN.getValue()))) {
					startDates[i] = FiscalCalendarUtil.getCalendarStartDate(new Long(calendar), startYear + i);
					endDates[i] = FiscalCalendarUtil.getCalendarEndDate(new Long(calendar), startYear + i);
				}
			}

			// Iterate the filtered collection of AmpAhSurveys.
			Iterator<AmpAhsurvey> iterCommonData = commonData.iterator();
			while (iterCommonData.hasNext()) {
				AmpAhsurvey auxAmpAhsurvey = iterCommonData.next();
				AmpActivity auxActivity = auxAmpAhsurvey.getAmpActivityId();
				AmpOrganisation auxOrganisation = auxAmpAhsurvey.getAmpDonorOrgId();
				AmpOrganisation auxPoDD = auxAmpAhsurvey.getPointOfDeliveryDonor();

				// Filter by sectors. If sectorsFilter is not null then filter
				// by the sectors in that list.
				if (sectorsFilter != null && !PIUtils.containSectors(sectorsFilter, auxActivity.getSectors())) {
					// Ignore this AmpAhsurvey and continue with the next.
					continue;
				}

				// Filter by status. If statusFilter is not null then filter by
				// the statuses in that list.
				if (statusFilter != null
						&& !PIUtils.containStatus(statusFilter, CategoryManagerUtil.getAmpCategoryValueFromListByKey(
								CategoryConstants.ACTIVITY_STATUS_KEY, auxActivity.getCategories()))) {
					// Ignore this AmpAhsurvey and continue with the next.
					continue;
				}

				// Iterate the collection of fundings.
				Iterator<AmpFunding> iterFundings = auxActivity.getFunding().iterator();
				while (iterFundings.hasNext()) {
					AmpFunding auxFunding = iterFundings.next();

					// Check if the funding belongs to the PoDD not the original
					// organization.
					if (auxAmpAhsurvey.getAmpDonorOrgId().getAmpOrgId().equals(
							auxFunding.getAmpDonorOrgId().getAmpOrgId())) {

						// Filter by Financing Instrument. If
						// financingInstrumentFilter is not null then filter
						// by the financing instruments in that list.
						if (financingInstrumentFilter != null) {
							if (!PIUtils.containFinancingInstrument(auxFunding.getFinancingInstrument(),
									financingInstrumentFilter)) {
								// Ignore this funding.
								continue;
							}
						}

						// Iterate the collection of funding details.
						Iterator<AmpFundingDetail> iterFundingDetails = auxFunding.getFundingDetails().iterator();
						while (iterFundingDetails.hasNext()) {
							AmpFundingDetail auxFundingDetail = iterFundingDetails.next();

							// Check the funding details type.
							if (auxFundingDetail.getTransactionType().intValue() == Constants.DISBURSEMENT) {
								if (auxFundingDetail.getAdjustmentType().intValue() == Constants.ACTUAL) {
									auxRow = new PIReport3Row();
									// aca deberia quizas controlar el tema de
									// los
									// responses.

									// Calculate exchange rates.
									if (PIConstants.CURRENCY_USD.equalsIgnoreCase(auxFundingDetail.getAmpCurrencyId()
											.getCurrencyCode())) {
										fromExchangeRate = 1.0;
									} else {
										fromExchangeRate = Util.getExchange(auxFundingDetail.getAmpCurrencyId()
												.getCurrencyCode(), new java.sql.Date(auxFundingDetail
												.getTransactionDate().getTime()));
									}
									toExchangeRate = 0;
									if (currency != null) {
										if (currency.getCurrencyCode().equals(PIConstants.CURRENCY_USD)) {
											toExchangeRate = 1.0;
										} else {
											toExchangeRate = Util.getExchange(currency.getCurrencyCode(),
													new java.sql.Date(auxFundingDetail.getTransactionDate().getTime()));
										}
									}
									BigDecimal amount = CurrencyWorker.convert1(
											auxFundingDetail.getTransactionAmount(), fromExchangeRate, toExchangeRate);

									// Setup row.
									auxRow.setColumn1(amount);
									auxRow.setColumn2(amount);
									auxRow.setColumn3(null);
									auxRow.setDonorGroup(auxPoDD.getOrgGrpId());
									auxRow.setYear(auxFundingDetail.getTransactionDate().getYear());
									list.add(auxRow);
								}
							}
						}
					}
				}
			}

		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return list;
	}
}
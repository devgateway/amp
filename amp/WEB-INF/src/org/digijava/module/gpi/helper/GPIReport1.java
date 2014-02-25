package org.digijava.module.gpi.helper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpGPISurvey;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.fiscalcalendar.BaseCalendar;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.gpi.helper.row.GPIReport1Row;
import org.digijava.module.gpi.helper.row.GPIReportAbstractRow;
import org.digijava.module.gpi.model.GPIFilter;
import org.digijava.module.gpi.util.GPIConstants;
import org.digijava.module.gpi.util.GPIUtils;

import java.util.Collections;

public class GPIReport1 extends GPIAbstractReport {

	private static Logger logger = Logger.getLogger(GPIReport1.class);
	private final String reportCode = GPIConstants.GPI_REPORT_1;

	public String getReportCode() {
		return this.reportCode;
	}

	@Override
	public Collection<GPIReportAbstractRow> generateReport(Collection<AmpGPISurvey> commonData, GPIFilter filter) {

		Collection<GPIReportAbstractRow> list = new ArrayList<GPIReportAbstractRow>();
		GPIReport1Row auxRow = null;
		int yearRange = filter.getEndYer() - filter.getStartYear() + 1;
		Date[] startDates = new Date[yearRange];
		Date[] endDates = new Date[yearRange];

		try {
			// Setup year ranges according the selected calendar.
			AmpFiscalCalendar fCalendar = FiscalCalendarUtil.getAmpFiscalCalendar(filter.getCalendar().getAmpFiscalCalId());
			if (!(fCalendar.getBaseCal().equalsIgnoreCase(BaseCalendar.BASE_ETHIOPIAN.getValue()))) {
				for (int i = 0; i < yearRange; i++) {
					startDates[i] = FiscalCalendarUtil.getCalendarStartDate(filter.getCalendar().getAmpFiscalCalId(), filter.getStartYear() + i);
					endDates[i] = FiscalCalendarUtil.getCalendarEndDate(filter.getCalendar().getAmpFiscalCalId(), filter.getStartYear() + i);
				}
			}

			// Iterate the filtered collection of AmpGPISurveys.
			Iterator<AmpGPISurvey> iterCommonData = commonData.iterator();
			while (iterCommonData.hasNext()) {
				AmpGPISurvey auxAmpGPISurvey = iterCommonData.next();
				AmpActivityVersion auxActivity = auxAmpGPISurvey.getAmpActivityId();

				// Filter by years. Check if the project date
				// falls into one of the date ranges.
				if (GPIUtils.getYear(auxActivity.getActualCompletionDate(), startDates, endDates, filter.getStartYear(), filter.getEndYer()) == 0) {
					// Ignore this project.
					continue;
				}

				// Filter by sectors.
				if (filter.getSectors() != null && !GPIUtils.containSectors(filter.getSectors(), auxActivity.getSectors())) {
					// Ignore this AmpGPISurvey and continue with the next.
					continue;
				}

				// Filter by status.
				if (filter.getStatuses() != null
						&& !GPIUtils.containStatus(filter.getStatuses(), CategoryManagerUtil.getAmpCategoryValueFromListByKey(CategoryConstants.ACTIVITY_STATUS_KEY, auxActivity.getCategories()))) {
					// Ignore this AmpGPISurvey and continue with the next.
					continue;
				}

				// Create set of years (no duplicates) that will be used to
				// populate the report.
				// ie: if an activity has a funding with 3 funding details (each
				// commitment/disbursement/expenditure) will add it ONLY ONE
				// TIME per year.
				Set<Integer> yearsFromFunding = new HashSet<Integer>();

				Iterator<AmpFunding> iFunding = auxActivity.getFunding().iterator();
				while (iFunding.hasNext()) {
					AmpFunding auxFunding = iFunding.next();

					// Filter by organization.
					if (filter.getDonors() != null && !GPIUtils.containOrganisations(filter.getDonors(), auxFunding.getAmpDonorOrgId())) {
						// Ignore this AmpGPISurvey and continue with the next.
						continue;
					}

					// Filter by organization group.
					if (filter.getDonorGroups() != null && !GPIUtils.containOrgGrps(filter.getDonorGroups(), auxFunding.getAmpDonorOrgId().getOrgGrpId())) {
						// Ignore this AmpGPISurvey and continue with the next.
						continue;
					}

					Iterator<AmpFundingDetail> iFD = auxFunding.getFundingDetails().iterator();
					while (iFD.hasNext()) {
						AmpFundingDetail auxFundingDetail = iFD.next();
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(auxFundingDetail.getTransactionDate());
						yearsFromFunding.add(calendar.get(Calendar.YEAR));
					}

					Iterator<Integer> iYears = yearsFromFunding.iterator();
					while (iYears.hasNext()) {
						Integer auxYear = iYears.next();

						auxRow = new GPIReport1Row();

						// Check survey answers for this
						// AmpGPISurvey.
						boolean[] showColumn = GPIUtils.getSurveyAnswers(GPIConstants.GPI_REPORT_1, auxAmpGPISurvey);

						if (showColumn[0]) {
							auxRow.setColumn1(new Integer(1));
						} else {
							auxRow.setColumn1(new Integer(0));
						}
						auxRow.setColumn2(new Integer(1));
						auxRow.setColumn3(0);
						auxRow.setDonorGroup(auxFunding.getAmpDonorOrgId().getOrgGrpId());
						auxRow.setYear(auxYear);
						list.add(auxRow);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public Collection<GPIReportAbstractRow> reportPostProcess(Collection<GPIReportAbstractRow> baseReport, int startYear, int endYear) throws Exception {

		Collection<GPIReportAbstractRow> list = new ArrayList<GPIReportAbstractRow>(baseReport);

		// TODO: make a general comparator???
		Comparator compareRows = new Comparator() {
			@Override
			public int compare(Object o1, Object o2) {
				GPIReport1Row aux1 = (GPIReport1Row) o1;
				GPIReport1Row aux2 = (GPIReport1Row) o2;
				if (aux1 == null && aux2 == null) {
					return 0;
				} else if (aux1 == null) {
					return 1;
				} else if (aux2 == null) {
					return -1;
				} else {
					return (aux1.getDonorGroup().getOrgGrpName() + aux1.getYear()).compareTo(aux2.getDonorGroup().getOrgGrpName() + aux2.getYear());
				}
			}
		};

		Collection<GPIReportAbstractRow> newList = new ArrayList<GPIReportAbstractRow>();
		Collections.sort((ArrayList) list, compareRows);

		// Format the list grouping by donor grp and year.
		AmpOrgGroup auxGroup = null;
		Iterator iter = list.iterator();
		Integer auxColumn1 = new Integer(0);
		Integer auxColumn2 = new Integer(0);
		int currentYear = 0;
		while (iter.hasNext()) {
			GPIReport1Row row = (GPIReport1Row) iter.next();

			// Valid for the first row.
			if (auxGroup == null) {
				auxGroup = row.getDonorGroup();
				currentYear = row.getYear();
			}
			// True if the actual and previous donor are the same.
			if (auxGroup.getAmpOrgGrpId().equals(row.getDonorGroup().getAmpOrgGrpId())) {
				// If is the same year than the previous record then add the
				// amounts, otherwise save the amounts added to this point and
				// then update the current year and save the amounts in the
				// auxiliary variables.
				if (row.getYear() == currentYear) {
					auxColumn1 = auxColumn1 + row.getColumn1();
					auxColumn2 = auxColumn2 + row.getColumn2();
				} else {
					GPIReport1Row newRow = new GPIReport1Row();
					newRow.setColumn1(auxColumn1);
					newRow.setColumn2(auxColumn2);
					newRow.setDonorGroup(row.getDonorGroup());
					newRow.setYear(currentYear);
					newList.add(newRow);

					currentYear = row.getYear();
					auxColumn1 = row.getColumn1();
					auxColumn2 = row.getColumn2();
				}
			} else {
				GPIReport1Row newRow = new GPIReport1Row();
				newRow.setColumn1(auxColumn1);
				newRow.setColumn2(auxColumn2);
				newRow.setDonorGroup(auxGroup);
				newRow.setYear(currentYear);
				newList.add(newRow);

				auxColumn1 = row.getColumn1();
				auxColumn2 = row.getColumn2();
				auxGroup = row.getDonorGroup();
				currentYear = row.getYear();
			}
			// If this is the last record then save it.
			if (!iter.hasNext()) {
				GPIReport1Row newRow = new GPIReport1Row();
				newRow.setColumn1(auxColumn1);
				newRow.setColumn2(auxColumn2);
				newRow.setDonorGroup(auxGroup);
				newRow.setYear(currentYear);
				newList.add(newRow);
			}
		}

		// Complete the list with the same number of years on each donor group.
		newList = this.addMissingYears(newList, startYear, endYear);

		// Calculate final percentages and add 'All Donors' row.
		if (!newList.isEmpty()) {
			newList = this.calculatePercentages(newList, startYear, endYear);
		}

		return newList;
	}

	private Collection<GPIReportAbstractRow> calculatePercentages(Collection<GPIReportAbstractRow> coll, int startYear, int endYear) throws Exception {

		int range = endYear + 1 - startYear;
		Integer[] sumCol1 = new Integer[range];
		Integer[] sumCol2 = new Integer[range];
		Iterator<GPIReportAbstractRow> iterColl = coll.iterator();
		while (iterColl.hasNext()) {
			// Calculate percentages.
			GPIReport1Row auxRow = (GPIReport1Row) iterColl.next();
			if (auxRow.getColumn2().doubleValue() > 0) {
				auxRow.setColumn3(new BigDecimal(auxRow.getColumn1()).multiply(new BigDecimal(100)).divide(new BigDecimal(auxRow.getColumn2()), RoundingMode.HALF_UP).floatValue());
			} else {
				auxRow.setColumn3(0);
			}

			// Accumulate totals for each year.
			if (sumCol1[auxRow.getYear() - startYear] == null) {
				sumCol1[auxRow.getYear() - startYear] = new Integer(0);
			}
			if (sumCol2[auxRow.getYear() - startYear] == null) {
				sumCol2[auxRow.getYear() - startYear] = new Integer(0);
			}
			sumCol1[auxRow.getYear() - startYear] = sumCol1[auxRow.getYear() - startYear] + (auxRow.getColumn1());
			sumCol2[auxRow.getYear() - startYear] = sumCol2[auxRow.getYear() - startYear] + (auxRow.getColumn2());
		}

		// Add "All Donors" record at the beginning with the total amounts.
		int currentYear = startYear;
		ArrayList auxList = new ArrayList(coll);
		for (int i = 0; i < endYear + 1 - startYear; i++) {
			GPIReport1Row auxRow = new GPIReport1Row();
			AmpOrgGroup auxDonorGroup = new AmpOrgGroup();
			auxDonorGroup.setOrgGrpName(GPIConstants.ALL_DONORS);
			auxDonorGroup.setAmpOrgGrpId(new Long(0));
			auxRow.setDonorGroup(auxDonorGroup);
			auxRow.setColumn1(sumCol1[i]);
			auxRow.setColumn2(sumCol2[i]);
			auxRow.setYear(startYear + i);
			if (auxRow.getColumn2().doubleValue() > 0) {
				auxRow.setColumn3(new BigDecimal(auxRow.getColumn1()).multiply(new BigDecimal(100)).divide(new BigDecimal(auxRow.getColumn2()), RoundingMode.HALF_UP).floatValue());
			} else {
				auxRow.setColumn3(0);
			}
			auxList.add(i, auxRow);
		}
		return auxList;
	}

	private Collection<GPIReportAbstractRow> addMissingYears(Collection<GPIReportAbstractRow> coll, int startYear, int endYear) throws Exception {
		Collection ret = new ArrayList();
		AmpOrgGroup auxGroup = null;
		int j = 0;
		Iterator iter = coll.iterator();
		while (iter.hasNext()) {
			GPIReport1Row row = (GPIReport1Row) iter.next();
			if (auxGroup == null) {
				auxGroup = row.getDonorGroup();
			}
			if (j == 0 || !auxGroup.getAmpOrgGrpId().equals(row.getDonorGroup().getAmpOrgGrpId())) {
				auxGroup = row.getDonorGroup();
				for (int i = startYear; i < endYear + 1; i++) {
					GPIReport1Row newRow = new GPIReport1Row();
					newRow.setColumn1(new Integer(0));
					newRow.setColumn2(new Integer(0));
					newRow.setDonorGroup(auxGroup);
					newRow.setYear(i);
					ret.add(newRow);
				}
			}
			j++;
		}

		Iterator iterRet = ret.iterator();
		while (iterRet.hasNext()) {
			GPIReport1Row rowRet = (GPIReport1Row) iterRet.next();
			Iterator iterOrigen = coll.iterator();
			while (iterOrigen.hasNext()) {
				GPIReport1Row rowOrigen = (GPIReport1Row) iterOrigen.next();
				if (rowRet.getDonorGroup().getAmpOrgGrpId().equals(rowOrigen.getDonorGroup().getAmpOrgGrpId()) && rowRet.getYear() == rowOrigen.getYear()) {
					rowRet.setColumn1(rowOrigen.getColumn1());
					rowRet.setColumn2(rowOrigen.getColumn2());
					rowRet.setDonorGroup(rowOrigen.getDonorGroup());
				}
			}
		}

		return ret;
	}
}
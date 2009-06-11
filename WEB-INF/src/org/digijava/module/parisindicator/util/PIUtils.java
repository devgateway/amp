package org.digijava.module.parisindicator.util;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpAhsurvey;
import org.digijava.module.aim.dbentity.AmpAhsurveyResponse;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.common.util.DateTimeUtil;
import org.hibernate.Session;

public class PIUtils {

	public final static boolean containSectors(Collection<AmpSector> sectors1, Collection<AmpSector> sectors2) {
		boolean ret = false;
		Iterator<AmpSector> iter1 = sectors1.iterator();
		Iterator<AmpSector> iter2 = sectors2.iterator();
		while (iter1.hasNext()) {
			AmpSector aux1 = iter1.next();
			while (iter2.hasNext()) {
				AmpSector aux2 = iter2.next();
				if (aux1.getAmpSectorId().equals(aux2.getAmpSectorId())) {
					ret = true;
					break;
				}
			}
		}
		return ret;
	}

	public final static boolean containStatus(Collection<AmpCategoryValue> statuses, AmpCategoryValue status) {
		boolean ret = false;
		Iterator<AmpCategoryValue> iterStatus = statuses.iterator();
		while (iterStatus.hasNext()) {
			AmpCategoryValue aux = iterStatus.next();
			if (aux.getId().equals(status.getId())) {
				ret = true;
				break;
			}
		}
		return ret;
	}

	public final static boolean containFinancingInstrument(AmpCategoryValue financing1,
			Collection<AmpCategoryValue> financing2) {
		boolean ret = false;
		Iterator<AmpCategoryValue> iter2 = financing2.iterator();
		while (iter2.hasNext()) {
			AmpCategoryValue aux1 = iter2.next();
			if (aux1.getId().equals(financing1.getId())) {
				ret = true;
				break;
			}
		}
		return ret;
	}

	/**
	 * Given a transaction date and the date ranges, return the year for that
	 * transaction or 0 if it doesn't belong to any range.
	 * 
	 * @param transactionDate
	 *            The date to evaluate.
	 * @param startDates
	 *            Array of dates with the first day of each year.
	 * @param endDates
	 *            Array of dates with the last day of each year.
	 * @param startYear
	 *            The first year represented in the startDates and endDates
	 *            ranges.
	 * @return The year of the transaction for the calendar represented by the
	 *         Date ranges. Or 0 if not.
	 */
	public final static int getTransactionYear(Date transactionDate, Date[] startDates, Date[] endDates, int startYear) {
		int ret = 0;
		int auxYear = startYear;
		for (int i = 0; i < startDates.length; i++) {
			auxYear += i;
			if ((transactionDate.after(startDates[i]) || transactionDate.equals(startDates[i]))
					&& (chkEqualDates(transactionDate, endDates[i]) || chkEqualDates(transactionDate, endDates[i]))) {
				ret = auxYear;
			}
		}
		return ret;
	}

	public static boolean chkEqualDates(Date d1, Date d2) {
		boolean result = false;
		result = (DateTimeUtil.formatDate(d1).equalsIgnoreCase(DateTimeUtil.formatDate(d2))) ? true : false;
		return result;
	}

	public static boolean[] getSurveyAnswers(String reportCode, AmpAhsurvey survey) throws Exception {
		// Set the number of columns for each report.
		boolean[] columns = null;
		if (PIConstants.PARIS_INDICATOR_REPORT_3.equals(reportCode)) {
			columns = new boolean[2];
		} else if (PIConstants.PARIS_INDICATOR_REPORT_4.equals(reportCode)) {
			// same logic here.
		}

		// Prepare an array with all the responses (no problem if its not
		// sorted).
		String[] answers = new String[PIConstants.NUMBER_OF_SURVEY_QUESTIONS];
		Collection<AmpAhsurveyResponse> responses = survey.getResponses();
		Iterator<AmpAhsurveyResponse> iter = responses.iterator();
		while (iter.hasNext()) {
			AmpAhsurveyResponse auxResponse = iter.next();
			int quesNum = auxResponse.getAmpQuestionId().getQuestionNumber().intValue() - 1;
			String auxString = (auxResponse.getResponse() == null) ? "" : auxResponse.getResponse();
			answers[quesNum] = new String(auxString);
		}

		// Evaluate the report.
		// Remember: columns[0] is the first column :)
		// Remember: answers[0] is the first question :D
		if (PIConstants.PARIS_INDICATOR_REPORT_3.equals(reportCode)) {
			columns[1] = ("Yes".equalsIgnoreCase(answers[0])) ? true : false;
			columns[0] = ("Yes".equalsIgnoreCase(answers[0]) && "Yes".equalsIgnoreCase(answers[1])) ? true : false;
		} else if (PIConstants.PARIS_INDICATOR_REPORT_4.equals(reportCode)) {

		}
		return columns;
	}
}

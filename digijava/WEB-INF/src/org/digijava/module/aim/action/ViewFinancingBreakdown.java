package org.digijava.module.aim.action;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.module.aim.form.FinancingBreakdownForm;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.CommonWorker;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.Currency;
import org.digijava.module.aim.helper.DecimalToText;
import org.digijava.module.aim.helper.FilterParams;
import org.digijava.module.aim.helper.FinancialFilters;
import org.digijava.module.aim.helper.FinancingBreakdownWorker;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.helper.YearUtil;
import org.digijava.module.aim.util.DbUtil;

public class ViewFinancingBreakdown extends TilesAction {
	private static Logger logger = Logger.getLogger(ViewFinancingBreakdown.class);

	public ActionForward execute(ComponentContext context,
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		FinancingBreakdownForm formBean = (FinancingBreakdownForm) form;
		HttpSession session = request.getSession();

		if (session.getAttribute("currentMember") == null) {
			formBean.setSessionExpired(true);
		} else {
			formBean.setSessionExpired(false);
			TeamMember teamMember = (TeamMember) session
					.getAttribute("currentMember");
			FinancialFilters ff = CommonWorker.getFilters(teamMember
					.getTeamId(), "FP");
			formBean.setCalendarPresent(ff.isCalendarPresent());
			formBean.setCurrencyPresent(ff.isCurrencyPresent());
			formBean.setPerspectivePresent(ff.isPerspectivePresent());
			formBean.setYearRangePresent(ff.isYearRangePresent());
			formBean.setGoButtonPresent(ff.isGoButtonPresent());
			formBean.setFiscalYears(new ArrayList());
			formBean.setFiscalYears(DbUtil.getAllFisCalenders());

			Long id = new Long(formBean.getAmpActivityId());
			String overallTotalCommitted = "";
			String overallTotalDisbursed = "";
			String overallTotalUnDisbursed = "";
			String overallTotalExpenditure = "";
			String overallTotalUnExpended = "";
			double fromExchangeRate = 0.0;
			
			Collection ampFundings = DbUtil.getAmpFunding(id);
			FilterParams fp = (FilterParams) session
					.getAttribute("filterParams");

			if (fp == null) {
				fp = new FilterParams();
			}
			ApplicationSettings apps = null;
			if (teamMember != null) {
				apps = teamMember.getAppSettings();
			}

			if (fp.getCurrencyCode() == null) 
			{
				Currency curr = DbUtil.getCurrency(apps.getCurrencyId());
				if (curr != null) {
					fp.setCurrencyCode(curr.getCurrencyCode());
				}
			}
			


			if (fp.getFiscalCalId() == null) {
				fp.setFiscalCalId(apps.getFisCalId());
			}
			

			if (fp.getPerspective() == null) {
				String perspective = CommonWorker.getPerspective(apps
						.getPerspective());
				fp.setPerspective(perspective);
			}

			if (fp.getFromYear() == 0 || fp.getToYear() == 0) {
				int year = new GregorianCalendar().get(Calendar.YEAR);
				fp.setFromYear(year-Constants.FROM_YEAR_RANGE);
				fp.setToYear(year+Constants.TO_YEAR_RANGE);
			}
			formBean.setPerspective(fp.getPerspective());
			formBean.setCurrency(fp.getCurrencyCode());
			formBean.setFiscalCalId(fp.getFiscalCalId().longValue());
			formBean.setFromYear(fp.getFromYear());
			formBean.setToYear(fp.getToYear());
			session.setAttribute("filterParams", fp);

			Collection fb = FinancingBreakdownWorker.getFinancingBreakdownList(
					id, ampFundings, fp);

			logger.debug("The size of the Collection fb is " + fb.size());
			formBean.setFinancingBreakdown(fb);
			formBean.setYears(YearUtil.getYears());
			formBean.setCurrencies(DbUtil.getAmpCurrency());
			overallTotalCommitted = FinancingBreakdownWorker.getOverallTotal(
					fb, Constants.COMMITMENT);
			formBean.setTotalCommitted(overallTotalCommitted);
			overallTotalDisbursed = FinancingBreakdownWorker.getOverallTotal(
					fb, Constants.DISBURSEMENT);
			formBean.setTotalDisbursed(overallTotalDisbursed);
			overallTotalUnDisbursed = DecimalToText.getDifference(
					overallTotalCommitted, overallTotalDisbursed);
			formBean.setTotalUnDisbursed(overallTotalUnDisbursed);
			overallTotalExpenditure = FinancingBreakdownWorker.getOverallTotal(
					fb, Constants.EXPENDITURE);
			formBean.setTotalExpended(overallTotalExpenditure);
			overallTotalUnExpended = DecimalToText.getDifference(
					overallTotalDisbursed, overallTotalExpenditure);
			formBean.setTotalUnExpended(overallTotalUnExpended);
		}
		return null;
	}
}


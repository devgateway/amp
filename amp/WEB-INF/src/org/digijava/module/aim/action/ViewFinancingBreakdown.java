package org.digijava.module.aim.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;

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
import org.dgfoundation.amp.Util;
import org.digijava.module.aim.form.FinancingBreakdownForm;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.CommonWorker;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.Currency;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.FilterParams;
import org.digijava.module.aim.helper.FinancialFilters;
import org.digijava.module.aim.helper.FinancingBreakdownWorker;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.helper.YearUtil;
import org.digijava.module.aim.util.AmpFundingComparatorByDonor;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DecimalWraper;
import org.digijava.module.aim.util.ProposedProjCostHelper;
import org.digijava.module.gateperm.core.GatePermConst;

import java.util.Collections;

public class ViewFinancingBreakdown extends TilesAction {
	private static Logger logger = Logger.getLogger(ViewFinancingBreakdown.class);

	public ActionForward execute(ComponentContext context,
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		FinancingBreakdownForm formBean = (FinancingBreakdownForm) form;
		HttpSession session = request.getSession();
		request.setAttribute(GatePermConst.ACTION_MODE, GatePermConst.Actions.VIEW);

		if (session.getAttribute(Constants.CURRENT_MEMBER) == null) {
			formBean.setSessionExpired(true);
		} else {
			boolean debug = (request.getParameter("debug")!=null)?true:false;
			formBean.setSessionExpired(false);
			TeamMember teamMember = (TeamMember) session
					.getAttribute(Constants.CURRENT_MEMBER);
			FinancialFilters ff = CommonWorker.getFilters(teamMember
					.getTeamId(), "FP");
			formBean.setCalendarPresent(ff.isCalendarPresent());
			formBean.setCurrencyPresent(ff.isCurrencyPresent());
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
                        String overallTotalDibsOrders="";
                        String overallTotalPlannedCommitted = "";
			String overallTotalPlannedDisbursed = "";
			double fromExchangeRate = 0.0;
                      
			Collection ampFundings = DbUtil.getAmpFunding(id);
			Collections.sort((List)ampFundings, new AmpFundingComparatorByDonor());
			
			FilterParams fp = (FilterParams) session
					.getAttribute(Constants.FILTER_PARAMS);

			if (fp == null) {
				fp = new FilterParams();
			}

			ApplicationSettings apps = null;
			apps = teamMember.getAppSettings();

			if (fp.getCurrencyCode() == null)
			{
				Currency curr = CurrencyUtil.getCurrency(apps.getCurrencyId());
				if (curr != null) {
					fp.setCurrencyCode(curr.getCurrencyCode());
				}
			}

			if (fp.getFiscalCalId() == null) {
				fp.setFiscalCalId(apps.getFisCalId());
			}

			if (fp.getFromYear() == 0 || fp.getToYear() == 0) {
				int year = new GregorianCalendar().get(Calendar.YEAR);
				fp.setFromYear(year-Constants.FROM_YEAR_RANGE);
				fp.setToYear(year+Constants.TO_YEAR_RANGE);
			}
			formBean.setCurrency(fp.getCurrencyCode());
			formBean.setFiscalCalId(fp.getFiscalCalId().longValue());
			formBean.setFromYear(fp.getFromYear());
			formBean.setToYear(fp.getToYear());
			session.setAttribute(Constants.FILTER_PARAMS, fp);

                        ProposedProjCostHelper projectCost=DbUtil.getActivityProposedProjCost(id);
                        if (projectCost != null) {
                            String currencyCode=projectCost.getCurrencyCode();
                            Double amount=projectCost.getFunAmount();
                            java.sql.Date dt = new java.sql.Date(projectCost.getFunDate().getTime());
                            double frmExRt = Util.getExchange(currencyCode, dt);
                            double toExRt = Util.getExchange(fp.getCurrencyCode(), dt);
                            DecimalWraper amt = CurrencyWorker.convertWrapper(amount, frmExRt, toExRt, dt);
                            formBean.setProposedProjectCostAmount(FormatHelper.formatNumber(amt.doubleValue()));
                            formBean.setProposedProjectCostDate(FormatHelper.formatDate(projectCost.getFunDate()));
                        }
                      						
			Collection fb = FinancingBreakdownWorker.getFinancingBreakdownList(id, ampFundings, fp,debug);
			logger.debug("The size of the Collection fb is " + fb.size());
			formBean.setFinancingBreakdown(fb);
			formBean.setYears(YearUtil.getYears());
			formBean.setCurrencies(CurrencyUtil.getActiveAmpCurrencyByName());

			overallTotalCommitted = FinancingBreakdownWorker.getOverallTotal(
					fb, Constants.COMMITMENT,Constants.ACTUAL,false);
			formBean.setTotalCommitted(overallTotalCommitted);
                        overallTotalPlannedCommitted = FinancingBreakdownWorker.getOverallTotal(
					fb, Constants.COMMITMENT,Constants.PLANNED,false);
			formBean.setTotalPlannedCommitted(overallTotalPlannedCommitted);
			overallTotalDisbursed = FinancingBreakdownWorker.getOverallTotal(
					fb, Constants.DISBURSEMENT,Constants.ACTUAL,false);
                        overallTotalPlannedDisbursed = FinancingBreakdownWorker.getOverallTotal(
					fb, Constants.DISBURSEMENT,Constants.PLANNED,false);
			formBean.setTotalPlannedDisbursed(overallTotalPlannedDisbursed);
			overallTotalUnDisbursed = FormatHelper.getDifference(
					overallTotalCommitted, overallTotalDisbursed);
			formBean.setTotalUnDisbursed(overallTotalUnDisbursed);
			overallTotalExpenditure = FinancingBreakdownWorker.getOverallTotal(
					fb, Constants.EXPENDITURE,Constants.ACTUAL,false);
			formBean.setTotalExpended(overallTotalExpenditure);
			overallTotalUnExpended = FormatHelper.getDifference(
					overallTotalDisbursed, overallTotalExpenditure);
			formBean.setTotalUnExpended(overallTotalUnExpended);
                        overallTotalDibsOrders= FinancingBreakdownWorker.getOverallTotal(
					fb, Constants.DISBURSEMENT_ORDER,Constants.ACTUAL,false);
                        formBean.setTotalDisbOrdered(overallTotalDibsOrders);

			formBean.setTotalProjections( FinancingBreakdownWorker.getOverallTotal(fb, Constants.MTEFPROJECTION,Constants.ACTUAL,false) );
		}
		return null;
	}
}


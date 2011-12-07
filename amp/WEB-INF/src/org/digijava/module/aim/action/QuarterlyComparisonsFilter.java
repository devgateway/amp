package org.digijava.module.aim.action ;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.QuarterlyComparisonsForm;
import org.digijava.module.aim.helper.AllTotals;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.CommonWorker;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.Currency;
import org.digijava.module.aim.helper.FilterParams;
import org.digijava.module.aim.helper.FinancialFilters;
import org.digijava.module.aim.helper.QuarterlyComparisonsWorker;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.helper.YearUtil;
import org.digijava.module.aim.helper.YearlyComparisonsWorker;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;

public class QuarterlyComparisonsFilter extends Action	{

	private static Logger logger = Logger.getLogger(QuarterlyComparisonsFilter.class);

	public ActionForward execute(ActionMapping mapping,
								 ActionForm form,
								 HttpServletRequest request,
								 HttpServletResponse response)
								 throws java.lang.Exception 	{

		QuarterlyComparisonsForm formBean = (QuarterlyComparisonsForm) form;
		HttpSession session = request.getSession();
		if (session.getAttribute("currentMember") == null) {
			formBean.setSessionExpired(true);
		}
		else	{
			formBean.setSessionExpired(false);
			TeamMember teamMember=(TeamMember)session.getAttribute("currentMember");
			FinancialFilters ff = CommonWorker.getFilters(teamMember.getTeamId(),"FP");
			formBean.setCalendarPresent(ff.isCalendarPresent());
			formBean.setCurrencyPresent(ff.isCurrencyPresent());
			formBean.setYearRangePresent(ff.isYearRangePresent());
			formBean.setGoButtonPresent(ff.isGoButtonPresent());

			FilterParams fp = (FilterParams)session.getAttribute("filterParams");
			fp.setTransactionType(formBean.getTransactionType());

			ApplicationSettings apps = null;
			if ( teamMember != null )	{
				apps = teamMember.getAppSettings();
			}

			if ( formBean.getCurrency() != null )
				fp.setCurrencyCode(formBean.getCurrency());
			else	{
				Currency curr = CurrencyUtil.getCurrency(apps.getCurrencyId());
				fp.setCurrencyCode(curr.getCurrencyCode());
			}

			if ( formBean.getFiscalCalId() != 0 )
				fp.setFiscalCalId(new Long( formBean.getFiscalCalId() ));
			else	{
				fp.setFiscalCalId(apps.getFisCalId());
			}

			if ( formBean.getFromYear()==0 || formBean.getToYear()==0 )	{
				int year = new GregorianCalendar().get(Calendar.YEAR);
				fp.setFromYear(year-Constants.FROM_YEAR_RANGE);
				fp.setToYear(year+Constants.TO_YEAR_RANGE);
			}
			else	{
				fp.setToYear(formBean.getToYear());
				fp.setFromYear(formBean.getFromYear());
			}
			session.setAttribute("filterParams",fp);
			formBean.setYears(YearUtil.getYears());
			formBean.setCurrencies(CurrencyUtil.getActiveAmpCurrencyByName());
			formBean.setFiscalYears(new ArrayList());
			formBean.setFiscalYears(DbUtil.getAllFisCalenders());


			Collection c = QuarterlyComparisonsWorker.getQuarterlyComparisons(fp);
			if ( c.size() != 0 )
				formBean.setQuarterlyComparisons(c) ;
                             Collection colYearly = YearlyComparisonsWorker.getYearlyComparisons(fp);
			if ( colYearly.size() > 0 )	{
				AllTotals allTotals = YearlyComparisonsWorker.getAllTotals(colYearly);
				formBean.setTotalActualCommitment(allTotals.getTotalActualCommitment());
				formBean.setTotalPlannedDisbursement(allTotals.getTotalPlannedDisbursement());
				formBean.setTotalActualDisbursement(allTotals.getTotalActualDisbursement());
				formBean.setTotalActualExpenditure(allTotals.getTotalActualExpenditure());
                                    formBean.setTotalDisbOrder(allTotals.getTotalDisbOrder());
			}
			
		}
		return null;
	}
}

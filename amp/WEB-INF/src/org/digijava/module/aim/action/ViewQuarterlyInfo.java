package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.module.aim.form.QuarterlyInfoForm;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.CommonWorker;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.Currency;
import org.digijava.module.aim.helper.FilterParams;
import org.digijava.module.aim.helper.FinancialFilters;
import org.digijava.module.aim.helper.PresentationUtil;
import org.digijava.module.aim.helper.QuarterlyInfoWorker;
import org.digijava.module.aim.helper.TabColors;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.helper.TotalsQuarterly;
import org.digijava.module.aim.helper.YearUtil;
import org.digijava.module.aim.helper.YearlyInfoWorker;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;

public class ViewQuarterlyInfo extends TilesAction	{
	private static Logger logger = Logger.getLogger(ViewQuarterlyInfo.class);

	public ActionForward execute(ComponentContext context,
								 ActionMapping mapping,
								 ActionForm form,
								 HttpServletRequest request,
								 HttpServletResponse response)
								 throws java.lang.Exception 	{
		HttpSession session = request.getSession();
		TeamMember teamMember=(TeamMember)session.getAttribute("currentMember");
		QuarterlyInfoForm formBean = (QuarterlyInfoForm)form;
		if (session.getAttribute("currentMember") == null) {
			formBean.setSessionExpired(true);
		}
		else{
			boolean debug = (request.getParameter("debug")!=null)?true:false;
			formBean.setSessionExpired(false);
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

			if ( fp.getCurrencyCode() == null )	{
				Currency curr = CurrencyUtil.getCurrency(apps.getCurrencyId());
				fp.setCurrencyCode(curr.getCurrencyCode());
			}

			if ( fp.getFiscalCalId() == null )	{
				fp.setFiscalCalId(apps.getFisCalId());
			}

			if ( fp.getFromYear()==0 || fp.getToYear()==0 )	{
				int year = new GregorianCalendar().get(Calendar.YEAR);
				fp.setFromYear(year-Constants.FROM_YEAR_RANGE);
				fp.setToYear(year+Constants.TO_YEAR_RANGE);
			}
			formBean.setCurrency(fp.getCurrencyCode());
			formBean.setFiscalCalId(fp.getFiscalCalId().longValue());
			formBean.setFromYear(fp.getFromYear());
			formBean.setToYear(fp.getToYear());
			session.setAttribute("filterParams",fp);

			formBean.setYears(YearUtil.getYears());
			formBean.setCurrencies(CurrencyUtil.getAmpCurrency());
			formBean.setFiscalYears(new ArrayList());
			formBean.setFiscalYears(DbUtil.getAllFisCalenders());

			TabColors tc = PresentationUtil.setTabColors(fp.getTransactionType());
			formBean.setCommitmentTabColor(tc.getCommitmentTabColor());
			formBean.setDisbursementTabColor(tc.getDisbursementTabColor());
			formBean.setExpenditureTabColor(tc.getExpenditureTabColor());

			Collection c = QuarterlyInfoWorker.getQuarterlyInfo(fp);
			if ( c.size() != 0 )	{
				formBean.setQuarterlyInfo(c);
				TotalsQuarterly tq; 
				if(!debug){
					 tq = QuarterlyInfoWorker.getTotalsQuarterly(fp.getAmpFundingId(),fp.getCurrencyCode(),false);
				}
				else{
					tq = QuarterlyInfoWorker.getTotalsQuarterly(fp.getAmpFundingId(),fp.getCurrencyCode(),true);
				}
				formBean.setTotalCommitted(tq.getTotalCommitted());
				formBean.setTotalDisbursed(tq.getTotalDisbursed());
                                    formBean.setTotalDisbOrdered(tq.getTotalDisbOrdered());
				formBean.setTotalUnExpended(tq.getTotalUnExpended());
				formBean.setTotalRemaining(tq.getTotalRemaining());
                                    Collection yearlyInfo = YearlyInfoWorker.getYearlyInfo(fp);
                                    String strTotalPlanned = YearlyInfoWorker.getTotalYearly(
						yearlyInfo, Constants.PLANNED);
				formBean.setTotalPlanned(strTotalPlanned);
				String strTotalActual = YearlyInfoWorker.getTotalYearly(
						yearlyInfo, Constants.ACTUAL);
				formBean.setTotalActual(strTotalActual);
			}
			
		}
		return null;
	}
}

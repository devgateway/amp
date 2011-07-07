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
import org.digijava.module.aim.helper.QuarterlyGrouping;
import org.digijava.module.aim.helper.QuarterlyInfo;
import org.digijava.module.aim.helper.QuarterlyInfoWorker;
import org.digijava.module.aim.helper.TabColors;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.helper.TotalsQuarterly;
import org.digijava.module.aim.helper.YearUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;

public class ViewQuarterlyGrouping extends TilesAction	{
	private static Logger logger = Logger.getLogger(ViewQuarterlyGrouping.class);

	public ActionForward execute(ComponentContext context,
								 ActionMapping mapping,
								 ActionForm form,
								 HttpServletRequest request,
								 HttpServletResponse response)
								 throws java.lang.Exception 	{
		HttpSession session = request.getSession();

		QuarterlyInfoForm formBean = (QuarterlyInfoForm)form;
		if (session.getAttribute("currentMember") == null) {
			formBean.setSessionExpired(true);
		}
		else	{
			formBean.setSessionExpired(false);
			TeamMember teamMember=(TeamMember)session.getAttribute("currentMember");
			ArrayList qtrGroup = (ArrayList)session.getAttribute("qtrGroup");
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
			QuarterlyGrouping qg = new QuarterlyGrouping();
			String clicked = formBean.getClicked();
			String state = null;
			if ( clicked.equals("plus") )
				state="minus";
			else if ( clicked.equals("minus") )
				state="plus";
			if ( qtrGroup == null )	{
				qg.setFiscalYear(formBean.getFiscalYrGrp());
				qg.setFiscalQuarter(formBean.getFiscalQtrGrp());
				qg.setState(state);
				qtrGroup = new ArrayList();
				qtrGroup.add(qg);
				session.setAttribute("qtrGroup",qtrGroup);
			}
			else	{
				qg.setFiscalYear(formBean.getFiscalYrGrp());
				qg.setFiscalQuarter(formBean.getFiscalQtrGrp());
				qg.setState(state);
				qtrGroup.add(qg);
				session.setAttribute("qtrGroup",qtrGroup);
			}

			if ( c.size() != 0 )	{
				ArrayList arrList = new ArrayList(c);
				for ( int i = 0 ; i < qtrGroup.size() ; i++)	{
					QuarterlyGrouping qgp = (QuarterlyGrouping)qtrGroup.get(i);
					for ( int j = 0 ; j < arrList.size() ; j++ )	{
						QuarterlyInfo qi = (QuarterlyInfo) arrList.get(j);
						if ( qi.getFiscalYear()==qgp.getFiscalYear() && qi.getFiscalQuarter()==qgp.getFiscalQuarter())	{
							if ( qi.getAggregate()==1 )	{
								if (  qgp.getState().equals("minus") )	{
									qi.setDisplay(true);
									qi.setPlus(false);
								}
								else if ( qgp.getState().equals("plus") )	{
									qi.setDisplay(false);
									qi.setPlus(true);
								}
							}
							else if ( qi.getAggregate()==0)	{
								if ( qgp.getState().equals("minus") )
									qi.setPlus(false);
								else if (qgp.getState().equals("plus") )
									qi.setPlus(true);
							}
						}
					}
				}

				formBean.setQuarterlyInfo(arrList);

				TotalsQuarterly tq = QuarterlyInfoWorker.getTotalsQuarterly(fp.getAmpFundingId(),fp.getCurrencyCode(),false);
				formBean.setTotalCommitted(tq.getTotalCommitted());
				formBean.setTotalDisbursed(tq.getTotalDisbursed());
				formBean.setTotalUnExpended(tq.getTotalUnExpended());
				formBean.setTotalRemaining(tq.getTotalRemaining());
			}
		}
		return null;
	}
}

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
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DecimalWraper;
import org.digijava.module.aim.util.ProposedProjCostHelper;

public class FinancingBreakdownFilter extends TilesAction	{
	private static Logger logger = Logger.getLogger(FinancingBreakdownFilter.class);
	
	public ActionForward execute(ComponentContext context,
								 ActionMapping mapping, 
								 ActionForm form,
								 HttpServletRequest request, 
								 HttpServletResponse response) 
								 throws java.lang.Exception 	{
							 	
		String overallTotalCommitted 	= null ;
		String overallTotalDisbursed 	= null ;
                String overallTotalPlannedCommitted 	= null ;
		String overallTotalPlannedDisbursed 	= null ;
		String overallTotalExpenditure 	= null ;
		String overallTotalDibsOrders	= null;
		String overallTotalUnDisbursed 	= null ;
		String overallTotalUnExpended 	= null ;
		
		FinancingBreakdownForm formBean = (FinancingBreakdownForm) form ;
		HttpSession session = request.getSession();
		TeamMember teamMember=(TeamMember)session.getAttribute("currentMember");
		FinancialFilters ff = CommonWorker.getFilters(teamMember.getTeamId(),"FP");
		formBean.setCalendarPresent(ff.isCalendarPresent());
		formBean.setCurrencyPresent(ff.isCurrencyPresent());
		formBean.setYearRangePresent(ff.isYearRangePresent());
		formBean.setGoButtonPresent(ff.isGoButtonPresent());
		FilterParams fp = (FilterParams)session.getAttribute("filterParams");
		ApplicationSettings apps = null;
		if ( teamMember != null )	{
			apps = teamMember.getAppSettings();
		}

		if ( formBean.getCurrency() != null )
		{
			fp.setCurrencyCode(formBean.getCurrency());
		}
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
		Long id = new Long(formBean.getAmpActivityId());
		Collection ampFundings = DbUtil.getAmpFunding(id);
                ProposedProjCostHelper projectCost = DbUtil.getActivityProposedProjCost(id);
                if (projectCost != null) {
                    String currencyCode = projectCost.getCurrencyCode();
                    Double amount = projectCost.getFunAmount();
                    java.sql.Date dt = new java.sql.Date(projectCost.getFunDate().getTime());
                    double frmExRt = Util.getExchange(currencyCode, dt);
                    double toExRt = Util.getExchange(fp.getCurrencyCode(), dt);
                    DecimalWraper amt = CurrencyWorker.convertWrapper(amount, frmExRt, toExRt, dt);
                    formBean.setProposedProjectCostAmount(FormatHelper.formatNumber(amt.doubleValue()));
                    formBean.setProposedProjectCostDate(FormatHelper.formatDate(projectCost.getFunDate()));
                }
		Collection fb = FinancingBreakdownWorker.getFinancingBreakdownList(id,ampFundings,fp,false);
		formBean.setFinancingBreakdown(fb);
		formBean.setYears(YearUtil.getYears());
		formBean.setFiscalYears(new ArrayList());
			formBean.setFiscalYears(DbUtil.getAllFisCalenders());
		formBean.setCurrencies(CurrencyUtil.getAmpCurrency());
		overallTotalCommitted = FinancingBreakdownWorker.getOverallTotal(fb,Constants.COMMITMENT,Constants.ACTUAL,false);
		formBean.setTotalCommitted(overallTotalCommitted);
                overallTotalPlannedCommitted = FinancingBreakdownWorker.getOverallTotal(fb,Constants.COMMITMENT,Constants.PLANNED,false);
		formBean.setTotalPlannedCommitted(overallTotalPlannedCommitted);
		overallTotalDisbursed = FinancingBreakdownWorker.getOverallTotal(fb,Constants.DISBURSEMENT,Constants.ACTUAL,false);
		formBean.setTotalDisbursed(overallTotalDisbursed);
                overallTotalPlannedDisbursed = FinancingBreakdownWorker.getOverallTotal(fb,Constants.DISBURSEMENT,Constants.PLANNED,false);
		formBean.setTotalPlannedDisbursed(overallTotalPlannedDisbursed);
		overallTotalExpenditure = FinancingBreakdownWorker.getOverallTotal(fb,Constants.EXPENDITURE,Constants.ACTUAL,false);
		formBean.setTotalExpended(overallTotalExpenditure);
		overallTotalDibsOrders= FinancingBreakdownWorker.getOverallTotal(
				fb, Constants.DISBURSEMENT_ORDER,Constants.ACTUAL,false);
        formBean.setTotalDisbOrdered(overallTotalDibsOrders);
        
        overallTotalUnDisbursed = FormatHelper.getDifference(
				overallTotalCommitted, overallTotalDisbursed);
        formBean.setTotalUnDisbursed(overallTotalUnDisbursed);
        
        overallTotalUnExpended = FormatHelper.getDifference(overallTotalDisbursed, overallTotalExpenditure);
		formBean.setTotalUnExpended(overallTotalUnExpended);
		
		formBean.setTotalProjections( FinancingBreakdownWorker.getOverallTotal(fb, Constants.MTEFPROJECTION,Constants.ACTUAL,false) );
		
		return  null;
	}
}	
		
		
		
		
		
		
		
		
		
		
		
		
		
	





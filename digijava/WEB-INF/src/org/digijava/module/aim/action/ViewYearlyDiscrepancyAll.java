package org.digijava.module.aim.action;

import java.io.IOException;
import java.util.Collection;

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
import org.digijava.module.aim.form.YearlyDiscrepancyAllForm;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.CommonWorker;
import org.digijava.module.aim.helper.Currency;
import org.digijava.module.aim.helper.FilterParams;
import org.digijava.module.aim.helper.FinancialFilters;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.helper.YearUtil;
import org.digijava.module.aim.helper.YearlyDiscrepancyAllWorker;
import org.digijava.module.aim.util.DbUtil;

public class ViewYearlyDiscrepancyAll extends TilesAction	{
	private static Logger logger = Logger.getLogger(ViewYearlyDiscrepancyAll.class);
	
	public ActionForward execute(ComponentContext context,
								 ActionMapping mapping,
								 ActionForm form,
								 HttpServletRequest request,
								 HttpServletResponse response)	
								 throws IOException,ServletException 	{
								 	
		HttpSession session = request.getSession();
		TeamMember teamMember=(TeamMember)session.getAttribute("currentMember");						 	
		YearlyDiscrepancyAllForm formBean = (YearlyDiscrepancyAllForm) form;		
		if (session.getAttribute("currentMember") == null) {
			formBean.setSessionExpired(true);
		}
		else	{
			formBean.setSessionExpired(false);
			String perspective = null;
			FinancialFilters ff = CommonWorker.getFilters(teamMember.getTeamId(),"FP");
			formBean.setCalendarPresent(ff.isCalendarPresent());
			formBean.setCurrencyPresent(ff.isCurrencyPresent());
//			formBean.setPerspectivePresent(ff.isPerspectivePresent());
			formBean.setYearRangePresent(ff.isYearRangePresent());
			formBean.setGoButtonPresent(ff.isGoButtonPresent());	
			FilterParams fp = (FilterParams)session.getAttribute("filterParams");
			
			ApplicationSettings apps = null;
			if ( teamMember != null )	{
				apps = teamMember.getAppSettings();
			}
	
			if ( fp.getCurrencyCode() == null )	{
				Currency curr = DbUtil.getCurrency(apps.getCurrencyId());
				fp.setCurrencyCode(curr.getCurrencyCode());
			}
	
			if ( fp.getFiscalCalId() == null )	{
				fp.setFiscalCalId(apps.getFisCalId());
			}
	
			if ( fp.getPerspective() == null )	{
				perspective = CommonWorker.getPerspective(apps.getPerspective());
				fp.setPerspective(perspective);
			}
	
			if ( fp.getFromYear()==0 || fp.getToYear()==0 )	{
				fp.setFromYear(1934);
				fp.setToYear(2014);
			}
			
			formBean.setPerspective(fp.getPerspective());
			formBean.setCurrency(fp.getCurrencyCode());
			formBean.setFiscalCalId(fp.getFiscalCalId().longValue());
			formBean.setFromYear(fp.getFromYear());
			formBean.setToYear(fp.getToYear());
			formBean.setAmpFundingId(fp.getAmpFundingId().longValue());
			session.setAttribute("filterParams",fp);	
			
			formBean.setYears(YearUtil.getYears());
			formBean.setCurrencies(DbUtil.getAmpCurrency());
			
			Collection c = YearlyDiscrepancyAllWorker.getDiscrepancy(fp);
			formBean.setYearlyDiscrepanciesAll(c);
		}
		return null;					 	
	}
}	
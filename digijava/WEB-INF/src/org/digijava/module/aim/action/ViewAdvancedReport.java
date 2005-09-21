package org.digijava.module.aim.action ;

import org.apache.log4j.Logger ;
import org.apache.struts.action.Action ;
import org.apache.struts.action.ActionForm ;
import org.apache.struts.action.ActionMapping ;
import org.apache.struts.action.ActionForward ;
import org.digijava.module.aim.dbentity.AmpActivity ;
import org.digijava.module.aim.dbentity.AmpLocation ;
import org.digijava.module.aim.form.AdvancedReportForm ;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.ReportUtil;
import org.digijava.module.aim.dbentity.AmpFunding ;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpStatus;
import org.digijava.module.aim.dbentity.AmpModality;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.helper.DateConversion ;
import org.digijava.module.aim.helper.EthiopianCalendar;
import org.digijava.module.aim.helper.FilterProperties;
import org.digijava.module.aim.helper.Report ;
import org.digijava.module.aim.helper.ReportSelectionCriteria ;
import org.digijava.module.aim.helper.ReportQuarterWorker;
import org.digijava.module.aim.helper.AmpFund ;
import org.digijava.module.aim.helper.DecimalToText ;
import org.digijava.module.aim.helper.Currency ;
import org.digijava.module.aim.helper.Constants ;
import org.digijava.module.aim.helper.Column ;
import org.digijava.module.aim.helper.AdvancedReport;
import org.digijava.module.aim.helper.TeamMember ;
import javax.servlet.http.HttpServletRequest ;
import javax.servlet.http.HttpServletResponse ;
import javax.servlet.http.HttpSession ;
import java.util.* ;
import java.util.Iterator ;
import java.util.ArrayList ;
import java.text.DecimalFormat;
import org.digijava.module.aim.helper.EthDateWorker;
import org.digijava.module.aim.helper.CurrencyWorker;

public class ViewAdvancedReport extends Action
{
	private static Logger logger = Logger.getLogger(ViewAdvancedReport.class) ;
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, 
	HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
	{
		AdvancedReportForm formBean = (AdvancedReportForm) form;
		HttpSession session = request.getSession();
		TeamMember teamMember=(TeamMember)session.getAttribute("currentMember");
		if(teamMember==null)
			return mapping.findForward("index");
		Long ampTeamId=teamMember.getTeamId();
		logger.debug("Team Id: " + ampTeamId);
		String perspective = "DN";
		if(request.getParameter("ampReportId") != null)
		{
			logger.info("-------------------->>>>>>>>>" + request.getParameter("ampReportId").toString());
			formBean.setCreatedReportId(request.getParameter("ampReportId"));
		}
		if(formBean.getPerspective() == null)
		{
			perspective = teamMember.getAppSettings().getPerspective();
		}
		else
			perspective = formBean.getPerspectiveFilter();
		if(perspective.equals("Donor"))
			perspective="DN";
		if(perspective.equals("MOFED"))
			perspective="MA";
		formBean.setPerspectiveFilter(perspective);
		ArrayList dbReturnSet= null ;
		ArrayList measures=new ArrayList();
		Iterator iterst = null ;
		Iterator iterSub = null ;
		Iterator iter = null;
		Long ampStatusId=null;
		Long ampOrgId=null;
		Long ampSectorId=null;
		String region=null;
		Long ampModalityId=null;
		String ampCurrencyCode=null;
		Long All=new Long(0);
		Collection reports=new ArrayList();
		int startYear = 0;
		int startMonth = 0;
		int startDay = 0;
		int closeYear = 0;
		int closeMonth = 0;
		int closeDay = 0;
		int fromYr = 0;
		int toYr = 0;
		int year=0;
		int fiscalCalId=0;
		Long ampReportId=null;
		ArrayList filters=null;
		String setFilters="";
		int filterCnt=0;
		AmpCurrency ampCurrency=null;
		GregorianCalendar c=new GregorianCalendar();
		year=c.get(Calendar.YEAR);
		
		DecimalFormat mf = new DecimalFormat("###,###,###,###,###") ;

		if(request.getParameter("ampReportId")!=null)
		{
			ampReportId=new Long(Long.parseLong(request.getParameter("ampReportId")));
			logger.info("Report Id: " + ampReportId);
		}	


		//Set all the filters
		formBean.setFilterFlag("false");
		formBean.setStatusColl(new ArrayList()) ;
		formBean.setSectorColl(new ArrayList()) ;
		formBean.setRegionColl(new ArrayList()) ;
		formBean.setDonorColl(new ArrayList()) ;
		formBean.setModalityColl(new ArrayList()) ;
		formBean.setCurrencyColl(new ArrayList()) ;
		formBean.setAmpFromYears(new ArrayList());
		formBean.setAmpToYears(new ArrayList());
		formBean.setAmpStartYears(new ArrayList());
		formBean.setAmpCloseYears(new ArrayList());
		formBean.setAmpStartDays(new ArrayList());
		formBean.setAmpCloseDays(new ArrayList());
		formBean.setFiscalYears(new ArrayList());

			formBean.setGoFlag("true");
			//if(filters.indexOf(Constants.PERSPECTIVE)!=-1)
			//{
				formBean.setFilterFlag("true");
				setFilters = setFilters + " PERSPECTIVE -";
				filterCnt++;
			//}
		
			//if(filters.indexOf(Constants.STATUS)!=-1)
			//{
				dbReturnSet=DbUtil.getAmpStatus();
				formBean.setStatusColl(dbReturnSet) ;
				setFilters = setFilters + " STATUS -";
				filterCnt++;
			//}
		
			//if(filters.indexOf(Constants.SECTOR)!=-1)
			//{
				setFilters = setFilters + " SECTOR -";
				filterCnt++;
				formBean.setSectorColl(new ArrayList()) ;
				dbReturnSet=DbUtil.getAmpSectors();
				iter = dbReturnSet.iterator() ;
				while(iter.hasNext())
				{
					AmpSector ampSector=(AmpSector) iter.next();
					if(ampSector.getName().length()>20)
					{
						String temp=ampSector.getName().substring(0,20) + "...";
						ampSector.setName(temp);
					}
					formBean.getSectorColl().add(ampSector);
					dbReturnSet=DbUtil.getAmpSubSectors(ampSector.getAmpSectorId());
					iterSub=dbReturnSet.iterator();
					while(iterSub.hasNext())
					{
						AmpSector ampSubSector=(AmpSector) iterSub.next();
						String temp=" -- " + ampSubSector.getName();
						if(temp.length()>20)
						{
							temp=temp.substring(0,20) + "...";
							ampSubSector.setName(temp);
						}
						ampSubSector.setName(temp);
						formBean.getSectorColl().add(ampSubSector);
					}
				}
			//}
		
			//if(filters.indexOf(Constants.REGION)!=-1)
			//{
				setFilters = setFilters + " REGION -";
				filterCnt++;
				dbReturnSet=DbUtil.getAmpLocations();
				formBean.setRegionColl(dbReturnSet) ;
			//}
		
			//if(filters.indexOf(Constants.DONORS)!=-1)
			//{
				setFilters = setFilters + " DONORS -";
				filterCnt++;
				dbReturnSet=DbUtil.getAmpDonors(ampTeamId);
				iter = dbReturnSet.iterator() ;
				formBean.setDonorColl(new ArrayList()) ;
				while ( iter.hasNext() )
				{
					AmpOrganisation ampOrganisation = (AmpOrganisation) iter.next() ;
					if(ampOrganisation.getAcronym().length()>20)
					{
						String temp=ampOrganisation.getAcronym().substring(0,20) + "...";
						ampOrganisation.setAcronym(temp);
					}
					formBean.getDonorColl().add(ampOrganisation);
				}
			//}
		
			//if(filters.indexOf(Constants.FINANCING_INSTRUMENT)!=-1)
			//{
				setFilters = setFilters + " MODALITY -";
				filterCnt++;
				dbReturnSet=DbUtil.getAmpModality();
				formBean.setModalityColl(dbReturnSet);
			//}	
		
			//if(filters.indexOf(Constants.CURRENCY)!=-1)
			//{
				setFilters = setFilters + " CURRENCY -";
				filterCnt++;
				dbReturnSet=DbUtil.getAmpCurrency();
				formBean.setCurrencyColl(dbReturnSet) ;	
			//}
				
			//if(filters.indexOf(Constants.CALENDAR)!=-1)
			///{
				setFilters = setFilters + " CALENDAR -";
				filterCnt+=10;
				formBean.setFiscalYears(DbUtil.getAllFisCalenders());
			//}
			
			//if(filters.indexOf(Constants.YEAR_RANGE)!=-1)
			//{
				for(int i=(year-Constants.FROM_YEAR_RANGE);i<=(year+Constants.TO_YEAR_RANGE);i++)
				{
					formBean.getAmpFromYears().add(new Long(i));
					formBean.getAmpToYears().add(new Long(i));
				}
			//}
			//if(filters.indexOf(Constants.STARTDATE_CLOSEDATE)!=-1)
			//{
				for(int i=(year-Constants.FROM_YEAR_RANGE);i<=(year+Constants.TO_YEAR_RANGE);i++)
				{
					formBean.getAmpStartYears().add(new Long(i));
					formBean.getAmpCloseYears().add(new Long(i));
				}
				for(int i=1;i<=31;i++)
				{
					formBean.getAmpStartDays().add(new Long(i));
					formBean.getAmpCloseDays().add(new Long(i));
				}
			//}
		//}		
		
		if(formBean.getAmpStatusId()==null || formBean.getAmpStatusId().intValue()==0)
			ampStatusId=All;
		else
			ampStatusId=formBean.getAmpStatusId();
			
		if(formBean.getAmpSectorId()==null || formBean.getAmpSectorId().intValue() == 0)
			ampSectorId=All;
		else
			ampSectorId=formBean.getAmpSectorId();	
		
		if(formBean.getAmpLocationId()==null || formBean.getAmpLocationId().equals("All"))
			region="All";
		else
			region=formBean.getAmpLocationId();
		
		if(formBean.getAmpOrgId()==null || formBean.getAmpOrgId().intValue()==0)
			ampOrgId=All;
		else
			ampOrgId=formBean.getAmpOrgId();
		
		if(formBean.getAmpModalityId()==null || formBean.getAmpModalityId().intValue()==0)
			ampModalityId=All;
		else
			ampModalityId=formBean.getAmpModalityId();
		
		if(formBean.getAmpCurrencyCode()==null || formBean.getAmpCurrencyCode().equals("0"))
		{
			ampCurrency=DbUtil.getAmpcurrency(teamMember.getAppSettings().getCurrencyId());
			ampCurrencyCode=ampCurrency.getCurrencyCode();
			formBean.setAmpCurrencyCode(ampCurrencyCode);
		}
		else
			ampCurrencyCode=formBean.getAmpCurrencyCode();

		//for storing the value of year filter 
		if(formBean.getAmpToYear()==null)
		{
			toYr=year;
			formBean.setAmpToYear(new Long(toYr));
		}
		else
	  		toYr = formBean.getAmpToYear().intValue();

		if(formBean.getAmpFromYear()==null)
		{
			fromYr=toYr-2;
			formBean.setAmpFromYear(new Long(fromYr));
		}
		else
	  		fromYr = formBean.getAmpFromYear().intValue();
	  	
//		for storing the values of start date and close date selected from filter
		if(formBean.getStartYear()==null || formBean.getStartYear().intValue()==0)
			startYear=year-Constants.FROM_YEAR_RANGE;
		else
			startYear = formBean.getStartYear().intValue();

		if(formBean.getStartMonth()==null || formBean.getStartMonth().intValue()==0)
			startMonth=1;
		else
			startMonth = formBean.getStartMonth().intValue();
		
		if(formBean.getStartDay()==null || formBean.getStartDay().intValue()==0)
			startDay = 1;
		else
			startDay = formBean.getStartDay().intValue();
	
		if(formBean.getCloseYear()==null || formBean.getCloseYear().intValue()==0)
			closeYear=year + Constants.TO_YEAR_RANGE;
		else
			closeYear = formBean.getCloseYear().intValue();
		
		if(formBean.getCloseMonth()==null || formBean.getCloseMonth().intValue()==0)
			closeMonth=12;
		else
			closeMonth = formBean.getCloseMonth().intValue();
		
		if(formBean.getCloseDay()==null || formBean.getCloseDay().intValue()==0)
			closeDay = 31;
		else
			closeDay = formBean.getCloseDay().intValue();

		if(formBean.getFiscalCalId()==0)
		{
			fiscalCalId=teamMember.getAppSettings().getFisCalId().intValue();
			formBean.setFiscalCalId(fiscalCalId);
		}
		else
			fiscalCalId =  formBean.getFiscalCalId();		

		if(request.getParameter("view")!=null)
		{
			if(request.getParameter("view").equals("reset"))
			{
				perspective =teamMember.getAppSettings().getPerspective();
				if(perspective.equals("Donor"))
					perspective="DN";
				if(perspective.equals("MOFED"))
					perspective="MA";
				formBean.setPerspectiveFilter(perspective);
				formBean.setAmpStatusId(All);
				ampStatusId=All;
				formBean.setAmpOrgId(All);
				ampOrgId=All;
				formBean.setAmpSectorId(All);
				ampSectorId=All;
				formBean.setAmpModalityId(All);
				ampModalityId=All;
				toYr=year;
				fromYr=toYr-2;
				formBean.setAmpFromYear(new Long(fromYr));
				formBean.setAmpToYear(new Long(toYr));
				formBean.setStartYear(All);
				formBean.setStartMonth(All);
				formBean.setStartDay(All);
				formBean.setCloseYear(All);
				formBean.setCloseMonth(All);
				formBean.setCloseDay(All);
				startYear=year-15;
				startMonth=1;
				startDay = 1;
				closeYear=year;
				closeMonth=12;
				closeDay = 31;
				fiscalCalId=teamMember.getAppSettings().getFisCalId().intValue();
				formBean.setFiscalCalId(fiscalCalId);
				formBean.setAmpCurrencyCode(ampCurrencyCode);
				formBean.setAmpLocationId("All");				
				region="All";
				
			}
		}
		
		String startDate=null;
		String closeDate=null;
		if(formBean.getStartYear()!=null && formBean.getStartYear().intValue()>0) 
			startDate = startYear + "-" + startMonth + "-" + startDay;
		if(formBean.getCloseYear()!=null && formBean.getCloseYear().intValue()>0)
			closeDate = closeYear + "-" + closeMonth + "-" + closeDay;
	
		

		ReportSelectionCriteria rsc=ReportUtil.getReportSelectionCriteria(ampReportId);
		
		formBean.setTitles(rsc.getColumns());
		formBean.setDimColumns(rsc.getColumns().size());
		logger.info("Measures: " + rsc.getMeasures().size());
		measures=(ArrayList)rsc.getMeasures();

		if(measures.indexOf(new Long(1))>=0)
			formBean.setAcCommFlag("true");
		else
			formBean.setAcCommFlag("false");

		if(measures.indexOf(new Long(2))>=0)
			formBean.setAcDisbFlag("true");
		else
			formBean.setAcDisbFlag("false");

		if(measures.indexOf(new Long(3))>=0)
			formBean.setAcExpFlag("true");
		else
			formBean.setAcExpFlag("false");
		
		if(measures.indexOf(new Long(4))>=0)
			formBean.setPlCommFlag("true");
		else
			formBean.setPlCommFlag("false");

		if(measures.indexOf(new Long(5))>=0)
			formBean.setPlDisbFlag("true");
		else
			formBean.setPlDisbFlag("false");

		if(measures.indexOf(new Long(6))>=0)
			formBean.setPlExpFlag("true");
		else
			formBean.setPlExpFlag("false");

		if(measures.indexOf(new Long(7))>=0)
			formBean.setAcBalFlag("true");
		else
			formBean.setAcBalFlag("false");

		formBean.setFundColumns((measures.size()));

		logger.info("****************************" + measures.size());
		
		if(formBean.getAcBalFlag().equals("true"))
			formBean.setMeasureCount(measures.size() - 1);
		else
			formBean.setMeasureCount(measures.size());
		
//		dbReturnSet=ReportUtil.getAdvancedReport(ampTeamId,fromYr,toYr,perspective,ampCurrencyCode,ampModalityId,ampStatusId,ampOrgId,ampSectorId,fiscalCalId,startDate,closeDate,region,ampReportId);
//		logger.info("Number of Records:" + dbReturnSet.size());
		/*iter=dbReturnSet.iterator();
		while(iter.hasNext())
		{
			Column col =(Column) iter.next();
			formBean.getTitles().add(col.getColumnName());
		}*/	

		Collection pageRecords = new ArrayList();
		int page=0;
		if (request.getParameter("page") == null) 
		{
			page = 1;
			reports=ReportUtil.getAdvancedReport(ampTeamId,fromYr,toYr,perspective,ampCurrencyCode,ampModalityId,ampStatusId,ampOrgId,ampSectorId,fiscalCalId,startDate,closeDate,region,ampReportId);
			session.setAttribute("ampReports",reports);

		}
		else 
		{
			page = Integer.parseInt(request.getParameter("page"));
			reports=(ArrayList)session.getAttribute("ampReports");
		}
		int stIndex = ((page - 1) * 10) + 1;
		int edIndex = page * 10;
		if (edIndex > reports.size()) 
		{
			edIndex = reports.size();
		}
		Vector vect = new Vector();
		vect.addAll(reports);
					 
		for (int i = (stIndex-1);i < edIndex;i ++) 
		{
			pageRecords.add(vect.get(i));
		}

		Collection pages = null;
		int numPages=0;
		if (reports.size() > 10) 
		{
			pages = new ArrayList();
			numPages = reports.size()/10;
			if(reports.size()%10>0)
				numPages++;
			for (int i = 0;i < numPages;i++) 
			{
				Integer pageNum=new Integer(i+1);
				pages.add(pageNum);
			}
		 }
//---------------Set the Filters along with thier properties -------------------
		FilterProperties filter = new FilterProperties();
		filter.setAmpTeamId(ampTeamId);
		filter.setCurrencyCode(ampCurrencyCode);
		Integer fics = new Integer(fiscalCalId);
		filter.setCalendarId(fics);
		filter.setRegionId(region);
		filter.setModalityId(ampModalityId);
		filter.setOrgId(ampOrgId);
		filter.setStatusId(ampStatusId);
		filter.setSectorId(ampSectorId);
		filter.setFromYear(fromYr);
		filter.setToYear(toYr);
		filter.setStartDate(startDate);
		filter.setCloseDate(closeDate);
		if(perspective.equals("DN"))
			filter.setPerspective("Donor");
		if(perspective.equals("MA"))
			filter.setPerspective("MOFED");
		
		//String setFiltersNames = DbUtil.setFilterDetails(filter);
		//formBean.setFiltersNames(setFiltersNames);
		
		String filterNames[] =new String[2];
		filterNames = DbUtil.setFilterDetails(filter);
		for(int i=0; i< filterNames.length; i++)
		{
			logger.debug("Filter HTML : + " + filterNames[i]);
		}
		formBean.setFilter(filterNames);
		
//---------------------------------------------------		
		/*BEGIN CODE FOR GRAND TOTAL*/
		int yearRange=(toYr-fromYr)+1;
		double totUnDisb = 0, actTotalCommit = 0, actTotalDisb = 0, actTotalExp = 0, planTotalCommit = 0, planTotalDisb = 0, planTotalExp = 0;
		double[][] totFunds=new double[yearRange][7];
		iter = reports.iterator() ;
	//	logger.debug("Grand Total :" + grandTotal);
		while ( iter.hasNext() )
		{
			Report report=(Report) iter.next();
			Iterator advIter=report.getRecords().iterator();
			while(advIter.hasNext())
			{
				AdvancedReport advancedReport=(AdvancedReport) advIter.next();
				if(advancedReport.getAmpFund()!=null)
				{
					Iterator iterFund = advancedReport.getAmpFund().iterator();
					for(int i=0;i<yearRange ;i++ )
					{
						AmpFund ampFund=(AmpFund) iterFund.next();
						if(measures.indexOf(new Long(1))!=-1)
							totFunds[i][0]=totFunds[i][0] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getCommAmount()));
						if(measures.indexOf(new Long(2))!=-1)
							totFunds[i][1]=totFunds[i][1] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getDisbAmount()));
						if(measures.indexOf(new Long(3))!=-1)
							totFunds[i][2]=totFunds[i][2] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getExpAmount()));
						if(measures.indexOf(new Long(4))!=-1)
							totFunds[i][3]=totFunds[i][3] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlCommAmount()));
						if(measures.indexOf(new Long(5))!=-1)
							totFunds[i][4]=totFunds[i][4] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlDisbAmount()));
						if(measures.indexOf(new Long(6))!=-1)
							totFunds[i][5]=totFunds[i][5] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlExpAmount()));
						if(measures.indexOf(new Long(7))!=-1)
							totFunds[i][6]=totFunds[i][6] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getUnDisbAmount()));
						
						
						actTotalCommit = actTotalCommit + Double.parseDouble(DecimalToText.removeCommas(ampFund.getCommAmount()));
						actTotalDisb = actTotalDisb + totFunds[i][1] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getDisbAmount()));
						actTotalExp = actTotalExp + totFunds[i][2] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getExpAmount()));
						planTotalCommit = planTotalCommit + totFunds[i][3] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlCommAmount()));
						planTotalDisb = planTotalDisb + totFunds[i][4] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlDisbAmount()));
						planTotalExp = planTotalExp + totFunds[i][5] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlExpAmount()));
						totUnDisb = totUnDisb + Double.parseDouble(DecimalToText.removeCommas(ampFund.getUnDisbAmount()));

					}
				}
			}	
		}
		formBean.setTotFund(new ArrayList());
		for(int i=0;i<yearRange ;i++ )
		{
			AmpFund ampFund=new AmpFund();
			if(measures.indexOf(new Long(1))!=-1)
				ampFund.setCommAmount(mf.format(totFunds[i][0])); 
			if(measures.indexOf(new Long(2))!=-1)
				ampFund.setDisbAmount(mf.format(totFunds[i][1])); 
			if(measures.indexOf(new Long(3))!=-1)
				ampFund.setExpAmount(mf.format(totFunds[i][2]));	
			if(measures.indexOf(new Long(4))!=-1)
				ampFund.setPlCommAmount(mf.format(totFunds[i][3])); 
			if(measures.indexOf(new Long(5))!=-1)
				ampFund.setPlDisbAmount(mf.format(totFunds[i][4])); 
			if(measures.indexOf(new Long(6))!=-1)
				ampFund.setPlExpAmount(mf.format(totFunds[i][5]));	
			if(measures.indexOf(new Long(7))!=-1)
				ampFund.setUnDisbAmount(mf.format(totFunds[i][6]));	
			formBean.getTotFund().add(ampFund);
		}

		AmpFund fund = new AmpFund();
		fund.setCommAmount(mf.format(actTotalCommit));
		fund.setDisbAmount(mf.format(actTotalDisb));
		fund.setExpAmount(mf.format(actTotalExp));
		fund.setPlCommAmount(mf.format(planTotalCommit));
		fund.setPlDisbAmount(mf.format(planTotalDisb));
		fund.setPlExpAmount(mf.format(planTotalExp));
		fund.setUnDisbAmount(mf.format(totUnDisb));
		formBean.getTotFund().add(fund);

				/*END CODE FOR GRAND TOTAL*/



		formBean.setFiscalYearRange(new ArrayList());
		for(int yr=fromYr;yr<=toYr;yr++)
			formBean.getFiscalYearRange().add(new Integer(yr));
		formBean.setTotalColumns(rsc.getColumns().size() + (yearRange * measures.size()));
		formBean.setPages(pages);
		formBean.setReport(pageRecords);
		formBean.setPage(new Integer(page));
		formBean.setAllReports(reports);
		logger.debug(" page REC " + pageRecords.size());
 		logger.debug(" REPORTS  " + reports.size());
		formBean.setForecastYear(new ArrayList());
		for(int i=toYr;i<=(toYr+3);i++)
			formBean.getForecastYear().add(new Integer(i));
		
		formBean.setFilterCnt(filterCnt);
		AmpTeam ampTeam=DbUtil.getAmpTeam(ampTeamId);
		AmpReports ampReports=DbUtil.getAmpReport(ampReportId);
		formBean.setReportName(ampReports.getName());
		formBean.setWorkspaceType(ampTeam.getType());
		formBean.setWorkspaceName(ampTeam.getName());
		if(perspective.equals("DN"))
			formBean.setPerspective("Donor");
		if(perspective.equals("MA"))
			formBean.setPerspective("MOFED");
		return mapping.findForward("forward");
	}
}

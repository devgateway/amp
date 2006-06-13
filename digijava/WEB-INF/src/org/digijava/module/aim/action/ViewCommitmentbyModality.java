package org.digijava.module.aim.action ;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.form.CommitmentbyDonorForm;
import org.digijava.module.aim.helper.AmpFund;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.DecimalToText;
import org.digijava.module.aim.helper.FilterProperties;
import org.digijava.module.aim.helper.Report;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.ReportUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.aim.util.TeamUtil;


public class ViewCommitmentbyModality extends Action
{
	private static Logger logger = Logger.getLogger(ViewCommitmentbyModality.class) ;
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, 
	HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
	{
		//Declare formBean of type CommitmentbyDonorForm.
		CommitmentbyDonorForm formBean = (CommitmentbyDonorForm) form;
		
		//Get team member object from session.
		HttpSession session = request.getSession();
		TeamMember teamMember=(TeamMember)session.getAttribute("currentMember");

		//If invalid session go to login page.
		if(teamMember==null)
			return mapping.findForward("index");

		String perspective = "DN";
		if(formBean.getPerspective() == null)
			perspective = teamMember.getAppSettings().getPerspective();	//when page loaded for first time, perspective is read from application settings.
		else
			perspective = formBean.getPerspectiveFilter();	//when user selects perspective in perspective filter.

		/*teamMember.getAppSettings().getPerspective() returns "Donor" or "MOFED" as perspective.
		Hence if perspective is read from application settings it is converted to perspective code 
		"DN" and "MA" respectively.*/

		if(perspective.equals("Donor"))
			perspective="DN";
		if(perspective.equals("MOFED"))
			perspective="MA";
		
		//To set selected perspective value.
		formBean.setPerspectiveFilter(perspective);
		//logger.debug("Perspective: " + perspective);

		// AmpTeamId read from team member.
		Long ampTeamId=teamMember.getTeamId();
		//logger.debug("Team Id: " + ampTeamId);

		ArrayList dbReturnSet= null ;
		Iterator iterst = null ;
		Iterator iter = null ;
		Iterator iterSub = null ;
		Long ampStatusId=null;
		Long ampOrgId=null;
		Long ampSectorId=null;
		String region=null;
		Long ampModalityId=null;
		String ampCurrencyCode=null;
//		int ampAdjustmentId=1;
		Collection reports=new ArrayList();
		ArrayList filters=null;
		Long All=new Long(0);	
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
		String filtersSet = "";
		AmpCurrency ampCurrency=null;

		// Get the current year.
		GregorianCalendar c=new GregorianCalendar();
		year=c.get(Calendar.YEAR);
		
		DecimalFormat mf = new DecimalFormat("###,###,###,###,###") ;

		//Set all the filters
		formBean.setFilterFlag("false");
	//	formBean.setAdjustmentFlag("false");
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

		/* This method returns collection of filters configured by team (ampTeamID) for page  
		Constants.TREND*. Those filters are displayed on report.*/
		filters=DbUtil.getTeamPageFilters(ampTeamId,Constants.TREND);
		//logger.debug("Filter Size: " + filters.size());
		
		//If no filter is configured then don't display "Go" button.
		if(filters.size()==0)
			formBean.setGoFlag("false");
		
		//If one or more filters are configured.
		if(filters.size()>0)
		{
			// Display "Go" Button.
			formBean.setGoFlag("true");

			//If perspective filter is configured.
			if(filters.indexOf(Constants.PERSPECTIVE)!=-1)
			{
				/* Display perspective filter. (** Perspective filter is hardcoded in reports. In
				desktop it is not hardcoded.) */
				formBean.setFilterFlag("true");
				filtersSet = filtersSet + " PERSPECTIVE - ";
			}
			
			// If status filter is configured.
			if(filters.indexOf(Constants.STATUS)!=-1)
			{
				// If configured populate status list box with all status read from amp_status table.
				dbReturnSet=DbUtil.getAmpStatus();
				formBean.setStatusColl(dbReturnSet) ;
				filtersSet = filtersSet + " STATUS - ";
			}

			// If sector filter is configured.
			if(filters.indexOf(Constants.SECTOR)!=-1)
			{
				/* If configured populate status list box with all sectors and their subsectors
				read from amp_sector table.*/
				filtersSet = filtersSet + " SECTOR - ";
				formBean.setSectorColl(new ArrayList()) ;
				//This method returns collection of all the sectors.
				dbReturnSet=SectorUtil.getAmpSectors();
				iter = dbReturnSet.iterator() ;
				/* Sector name can be very long. To fix layout of sector filter if sector or sub sector name 
				size is greater then 20 then only first 20 characters are shown suffixed with "..."*/
				while(iter.hasNext())
				{
					AmpSector ampSector=(AmpSector) iter.next();
					if(ampSector.getName().length()>20)
					{
						String temp=ampSector.getName().substring(0,20) + "...";
						ampSector.setName(temp);
					}
					formBean.getSectorColl().add(ampSector);
					// This method returns all sub sectors for sector is passed as argument.
					dbReturnSet=SectorUtil.getAmpSubSectors(ampSector.getAmpSectorId());
					iterSub=dbReturnSet.iterator();
					/* Sub sectors are prefixed by "--" and then like sector names their size is 
					also restricted to 20 and terminated by "...".*/
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
			}
			//If region filter is configured.
			if(filters.indexOf(Constants.REGION)!=-1)
			{
				//If configured fill region filter with all the regions read from amp_region table.
				filtersSet = filtersSet + " REGION - ";
				dbReturnSet=LocationUtil.getAmpLocations();
				formBean.setRegionColl(dbReturnSet) ;
			}
			//If donor filter is configured.
			if(filters.indexOf(Constants.DONORS)!=-1)
			{
				/* If configured fill donor filter with organizations who are playing role of donor 
				for all the activities of the team. */
				filtersSet = filtersSet + " DONORS - ";
				dbReturnSet=DbUtil.getAmpDonors(ampTeamId);
				iter = dbReturnSet.iterator() ;
				formBean.setDonorColl(new ArrayList()) ;
				/* Donor acronyms are shown in list box. Like sector name donor acronym is also 
				restricted to 20 characters followed by "..." */
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
			}
			//if financing instrument is configured.
			if(filters.indexOf(Constants.FINANCING_INSTRUMENT)!=-1)
			{
				/*Financing instrument list box is populated with all financing instruments
				from amp_modality table. */
				filtersSet = filtersSet + " MODALITY - ";
				dbReturnSet=DbUtil.getAmpModality();
				formBean.setModalityColl(dbReturnSet);
			}	
			//if currency filter is configured
			if(filters.indexOf(Constants.CURRENCY)!=-1)
			{
				//currency filter is populated with all active currencies.
				filtersSet = filtersSet + " CURRENCY - ";
				dbReturnSet=CurrencyUtil.getAmpCurrency();
				formBean.setCurrencyColl(dbReturnSet) ;	
			}
			
			// if calender filter is conmfigured
			if(filters.indexOf(Constants.CALENDAR)!=-1)
			{
				/*calender filter listbox populated with all calender names 
				in amp_fiscal_calendar table.*/
				filtersSet = filtersSet + " CALENDAR - ";
				formBean.setFiscalYears(DbUtil.getAllFisCalenders());
			}
			// if year range filter is configured.
			if(filters.indexOf(Constants.YEAR_RANGE)!=-1)
			{
				/*populates fromYear and toYear listbox with years from (year-Constants.FROM_YEAR_RANGE)
				to year+Constants.TO_YEAR_RANGE.*/
				for(int i=(year-Constants.FROM_YEAR_RANGE);i<=(year+Constants.TO_YEAR_RANGE);i++)
				{
					formBean.getAmpFromYears().add(new Long(i));
					formBean.getAmpToYears().add(new Long(i));
				}
			}
			// if start/close date filter is configured.
			if(filters.indexOf(Constants.STARTDATE_CLOSEDATE)!=-1)
			{
				//populates year listboxes
				for(int i=(year-Constants.FROM_YEAR_RANGE);i<=(year+Constants.TO_YEAR_RANGE);i++)
				{
					formBean.getAmpStartYears().add(new Long(i));
					formBean.getAmpCloseYears().add(new Long(i));
				}
				//populates day listboxes.(** month listbixes are hardcoded on jsp page)
				for(int i=1;i<=31;i++)
				{
					formBean.getAmpStartDays().add(new Long(i));
					formBean.getAmpCloseDays().add(new Long(i));
				}
			}
		}		
		
		// Start read filter criteria.

		if(formBean.getAmpStatusId()==null || formBean.getAmpStatusId().intValue()==0)
			ampStatusId=All;	// When page loaded for first time or when 'All Status" selected in status filter.
		else
			ampStatusId=formBean.getAmpStatusId(); //when status other then "All Status" selected in status filter.
			
		if(formBean.getAmpSectorId()==null || formBean.getAmpSectorId().intValue() == 0)
			ampSectorId=All;	// When page loaded for first time or filter not configured or when 'All Sector" selected in sector filter.
		else
			ampSectorId=formBean.getAmpSectorId();	//when sector other then "All Sector" selected in sector filter.
		
		if(formBean.getAmpLocationId()==null || formBean.getAmpLocationId().equals("All"))
			region="All";	// When page loaded for first time or filter not configured or when 'All Region" selected in region filter.
		else
			region=formBean.getAmpLocationId(); //when region other then "All Region" selected in region filter.
		
		if(formBean.getAmpOrgId()==null || formBean.getAmpOrgId().intValue()==0)
			ampOrgId=All;	// When page loaded for first time or filter not configured or when 'All Donors" selected in donor filter.
		else
			ampOrgId=formBean.getAmpOrgId(); //when donor other then "All Donors" selected in donor filter.
		
		if(formBean.getAmpModalityId()==null || formBean.getAmpModalityId().intValue()==0)
			ampModalityId=All;	// When page loaded for first time or filter not configured or when 'All Financing Instrument" selected in financing instrument filter.
		else
			ampModalityId=formBean.getAmpModalityId();	//when financing instrument other then "All Financing Instrument" selected in financing instrument filter.
		
		if(formBean.getAmpCurrencyCode()==null || formBean.getAmpCurrencyCode().equals("0"))
		{
			/* When page loaded for first time or filter not configured.
			The default currency read from application setting and selected value
			in currency filter is set to it. The default currency is passed in filter
			criteria.*/
			ampCurrency=CurrencyUtil.getAmpcurrency(teamMember.getAppSettings().getCurrencyId());
			ampCurrencyCode=ampCurrency.getCurrencyCode();
			formBean.setAmpCurrencyCode(ampCurrencyCode);
		}
		else
			ampCurrencyCode=formBean.getAmpCurrencyCode(); //when some currency selected in currency filter.

		//for storing the value of year filter 
		if(formBean.getAmpToYear()==null)
		{
			/*when page loaded for first time or when filter not configured toYr is set to current
			year and same is set as selected value in toYr listbox of year range filter.*/
			toYr=year; 
			formBean.setAmpToYear(new Long(toYr));
		}
		else
	  		toYr = formBean.getAmpToYear().intValue(); //when to year is selected by user in toYr listbox of year range filter.

		if(formBean.getAmpFromYear()==null)
		{
			/*when page loaded for first time or when filter not configured fromYr is set to (current
			year-2) and same is set as selected value in fromYr listbox of year range filter.*/
			fromYr=toYr-2;
			formBean.setAmpFromYear(new Long(fromYr));
		}
		else
	  		fromYr = formBean.getAmpFromYear().intValue();	//when from year is selected by user in fromYr listbox of year range filter.
	  	
//		for storing the values of start date and close date selected from start date/close date filter
		if(formBean.getStartYear()==null || formBean.getStartYear().intValue()==0)
			startYear=year-Constants.FROM_YEAR_RANGE; // When page loaded for first time or filter not configured or when "YYYY" selected in start year listbox then default startYear is taken as current year - Constants.FROM_YEAR_RANGE.
		else
			startYear = formBean.getStartYear().intValue(); //when start year other then "YYYY" is selected in start year listbox.

		if(formBean.getStartMonth()==null || formBean.getStartMonth().intValue()==0)
			startMonth=1;	// When page loaded for first time or filter not configured or when "MON" selected in start month listbox then default startMonth is taken as January.
		else
			startMonth = formBean.getStartMonth().intValue();	//when start month other then "MON" is selected in start month listbox.
		
		if(formBean.getStartDay()==null || formBean.getStartDay().intValue()==0)
			startDay = 1;	// When page loaded for first time or filter not configured or when "DD" selected in start day listbox then default startDay is taken as 1.
		else
			startDay = formBean.getStartDay().intValue();	//when start day other then "DD" is selected in start day listbox.
	
		if(formBean.getCloseYear()==null || formBean.getCloseYear().intValue()==0)
			closeYear=year + Constants.TO_YEAR_RANGE;	// When page loaded for first time or filter not configured or when "YYYY" selected in close year listbox then default closeYear is taken as current year + Constants.TO_YEAR_RANGE.
		else
			closeYear = formBean.getCloseYear().intValue();	//when close year other then "YYYY" is selected in close year listbox.
		
		if(formBean.getCloseMonth()==null || formBean.getCloseMonth().intValue()==0)
			closeMonth=12;	// When page loaded for first time or filter not configured or when "MON" selected in close month listbox then default closeMonth is taken as December.
		else
			closeMonth = formBean.getCloseMonth().intValue();	//when close month other then "MON" is selected in close month listbox.
		
		if(formBean.getCloseDay()==null || formBean.getCloseDay().intValue()==0)
			closeDay = 31;	// When page loaded for first time or filter not configured or when "DD" selected in close day listbox then default closeDay is taken as 31.
		else
			closeDay = formBean.getCloseDay().intValue();	//when close day other then "DD" is selected in close day listbox.

		if(formBean.getFiscalCalId()==0)
		{
			/* when page loaded for first time or when calendar filter not configured.
			default fisacl calendar id is read from application settings and shown 
			as selected in calendar filter listbox.*/
			fiscalCalId=teamMember.getAppSettings().getFisCalId().intValue(); 
			formBean.setFiscalCalId(fiscalCalId);
		}
		else
			fiscalCalId =  formBean.getFiscalCalId();	//when some fiscal calendar is selected by user.

		//end read filter criteria.
		
		if(request.getParameter("view")!=null)
		{
			if(request.getParameter("view").equals("reset"))
			{
				/* The scope of all the reports is set to session as the values from this formBean 
				are passed for PDF,CSV and XLS export. 
				
				This solution had one side effect. When user clicks some report, does some filter 
				selection and clicks next report the filter selection in previous report were 
				getting passed to next report. Which is undesirable.
				
				To solve this problem view request parameter was set to "reset" whenever user 
				clicked report links on desktop. The following code resets all the filters to 
				their default value.*/
				perspective = teamMember.getAppSettings().getPerspective();
				if(perspective.equals("Donor"))
					perspective="DN";
				if(perspective.equals("MOFED"))
					perspective="MA";
				formBean.setPerspectiveFilter(perspective);
				
			/*	if(filters.indexOf(Constants.ACTUAL_PLANNED)!=-1)
				{
					formBean.setAmpAdjustmentId(new Integer(Constants.ACTUAL));
					ampAdjustmentId=Constants.ACTUAL;
				}*/
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
				startYear=year;
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
		/* There are separate list boxes for DD, MON and YYYY of startdate/closedate filter.
		To get start date and  close date the selected values from the three listboxes are 
		concatenated in MySQL date format (YYYY-MON-DD)*/
		String startDate=null;
		String closeDate=null;
		if(formBean.getStartYear()!=null && formBean.getStartYear().intValue()>0) 
			startDate = startYear + "-" + startMonth + "-" + startDay;
		if(formBean.getCloseYear()!=null && formBean.getCloseYear().intValue()>0)
			closeDate = closeYear + "-" + closeMonth + "-" + closeDay;
			
		logger.debug("Start Date: " + startDate);
		logger.debug("Close Date: " + closeDate);

		//code for pagination and report data which will be displayed on jsp page.
		Collection pageRecords = new ArrayList();
		int page=0;
		if (request.getParameter("page") == null) 
		{
			//when page loaded for first time. By default first page will be shown.
			page = 1;
			//Method to fetch report data based on report format and filter criteria.
			reports=ReportUtil.getAmpReportByModality(ampTeamId,fromYr,toYr,perspective,ampCurrencyCode,ampModalityId,ampStatusId,ampOrgId,ampSectorId,fiscalCalId,startDate,closeDate,region);
			//the result set is set in session.
			session.setAttribute("ampReports",reports);
		}
		else 
		{
			/* when user selects some page number then instead of again calling method from report util
			we read report data from session. */
			page = Integer.parseInt(request.getParameter("page"));
			reports=(ArrayList)session.getAttribute("ampReports");
		}

		/* start code to compute the records which are to be shown based 
		upon page number selected by user.
		Ten records are shown per page.
		If page 1 selected then first 10 records are shown. when page 2 then next 10 and so on.*/

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
		 //end code for pagination
		formBean.setYrCount( (toYr -fromYr) + 1 );
		//System.out.println(" :" + formBean.getYrCount());
		
		//Start for PDF and XLS export.
//		---------------Set the Filters along with thier properties -------------------
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
	// End for PDF and XLS export.
		
		/* By default fical year range is from (currYear-2) to currYear. The user can change this
		range usinf year range filter. fiscalYearRange stores collection of all the years in this
		range.*/
		int yearRange=(toYr-fromYr)+1;
		formBean.setFiscalYearRange(new ArrayList());
		for(int yr=fromYr;yr<=toYr;yr++)
			formBean.getFiscalYearRange().add(new Integer(yr));

		//used in colspan for Year column.
		formBean.setTotalColumns(4*yearRange);

		//Begin code to compute grand total.
		double totComm=0.0;
		double unDisbAmount=0.0;
		/*totProjFund[m][n] -> totProjFund[yearRange+1][4]
		
		m = yearRange+1 - > to store actual comm, actual disb, actual exp and planned disb for each year of year range 
		+ their totals for all the years in the year range
		
		n = 4 -> to store actual comm[m][0], actual disb[m][1], actual exp[m][2] and planned disb[m][3]*/
		double[][] totProjFund=new double[yearRange+1][4];
		iter = reports.iterator() ;	//iterate report data collection
		while ( iter.hasNext() )
		{
			Report report=(Report) iter.next();
			//to get sum of total actual commitment till date of all the activities.
			totComm=totComm + Double.parseDouble(DecimalToText.removeCommas(report.getAcCommitment()));
			Iterator iterFund=report.getAmpFund().iterator();
			for(int i=0;i<=yearRange ;i++ )
			{
				AmpFund ampFund=(AmpFund) iterFund.next();
				totProjFund[i][0]=totProjFund[i][0] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getCommAmount()));
				totProjFund[i][1]=totProjFund[i][1] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getDisbAmount()));
				totProjFund[i][2]=totProjFund[i][2] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getExpAmount()));
				totProjFund[i][3]=totProjFund[i][3] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlannedDisbAmount()));
				if(i==yearRange)
					unDisbAmount=unDisbAmount + Double.parseDouble(DecimalToText.removeCommas(ampFund.getUnDisbAmount()));
			}
		}
		//populate formBean with grand totals.
		formBean.setTotComm(mf.format(totComm));
		formBean.setTotFund(new ArrayList());
		for(int i=0;i<=yearRange ;i++ )
		{
			AmpFund ampFund=new AmpFund();
			ampFund.setCommAmount(mf.format(totProjFund[i][0]));
			ampFund.setDisbAmount(mf.format(totProjFund[i][1]));
			ampFund.setExpAmount(mf.format(totProjFund[i][2]));
			ampFund.setPlannedDisbAmount(mf.format(totProjFund[i][3]));
			if(i==yearRange)
				ampFund.setUnDisbAmount(mf.format(unDisbAmount));
			formBean.getTotFund().add(ampFund);
		}

		//End code to compute grand total

		 formBean.setPages(pages);	// collection of page numbers.
		 formBean.setReport(pageRecords);	//ten records to be displayed on the page.
		 formBean.setPage(new Integer(page));	//page number for which records are displayed.
		 formBean.setAllReports(reports);	//entire report data irrespective of pagination.
		 logger.debug(" page REC " + pageRecords.size());
 		 logger.debug(" REPORTS  " + reports.size());
//		logger.debug("formBean size " + formBean.getPageCount());
		AmpTeam ampTeam=TeamUtil.getAmpTeam(ampTeamId);
		formBean.setReportName("Annual Project Detail Report");	//set report name as title.(**hardcoded. can be read from database)
		formBean.setWorkspaceType(ampTeam.getType());	//set workspace type in subtitle
		formBean.setWorkspaceName(ampTeam.getName());	//set workspace name in subtitle

		/* formBean.getPerspective() stores "Donor" or "MOFED" value which is used to display 
		user's perspective in report breadcrumb.*/
		if(perspective.equals("DN"))
			formBean.setPerspective("Donor");
		if(perspective.equals("MA"))
			formBean.setPerspective("MOFED");
		
		return mapping.findForward("forward");
	}
}

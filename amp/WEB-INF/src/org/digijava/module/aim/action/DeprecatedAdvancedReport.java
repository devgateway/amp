/*package org.digijava.module.aim.action;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.aim.dbentity.AmpColumnsOrder;
import org.digijava.module.aim.dbentity.AmpColumnsVisibility;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFeaturesVisibility;
import org.digijava.module.aim.dbentity.AmpFieldsVisibility;
import org.digijava.module.aim.dbentity.AmpMeasures;
import org.digijava.module.aim.dbentity.AmpReportColumn;
import org.digijava.module.aim.dbentity.AmpReportHierarchy;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.form.AdvancedReportForm;
import org.digijava.module.aim.helper.AmpFund;
import org.digijava.module.aim.helper.Column;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.DecimalToText;
import org.digijava.module.aim.helper.Report;
import org.digijava.module.aim.helper.ReportSelectionCriteria;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.AuditLoggerUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.FeaturesUtil;

import org.digijava.module.aim.util.TeamUtil;


public class DeprecatedAdvancedReport extends Action {

	private static Logger logger = Logger.getLogger(Login.class);
	private String str="";

	public ActionForward execute(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
	{
		//logger.info("###---------------------------------------------------------------------->>>>>>>>");
		
		AdvancedReportForm formBean = (AdvancedReportForm) form;
		
		
		HttpSession httpSession = request.getSession();
		Query query;
		Collection reports = new ArrayList();
		Collection selectedTransc = new ArrayList();
		//Session session = null;
	//	Transaction tx = null;
		String sqlQuery;		
		Iterator iter;
		Collection coll = new ArrayList();
		DecimalFormat mf = new DecimalFormat("###,###,###,###,###") ;
		TeamMember teamMember=(TeamMember)httpSession.getAttribute("currentMember");
		//logger.info(teamMember.getMemberId());
		if(teamMember==null)
			return mapping.findForward("index");
		Long ampTeamId=teamMember.getTeamId();
		logger.debug("Team Id: " + ampTeamId);
		String perspective = "DN";
		Long All=new Long(0);
		
		//set maxStep
		String check = request.getParameter("check");
		if (check != null){
			if ("SelectCols".equals(check)){
				if (formBean.getMaxStep().intValue() < 1)
					formBean.setMaxStep(new Integer(1));
			}
			else
				if ("SelectRows".equals(check)){
					if (formBean.getMaxStep().intValue() < 2)
						formBean.setMaxStep(new Integer(2));
				}
				else
					if ("SelectMeasures".equals(check)){
						if (formBean.getMaxStep().intValue() < 3)
							formBean.setMaxStep(new Integer(3));
					}
					else
						if ("4".equals(check)){
							if (formBean.getMaxStep().intValue() < 4)
								formBean.setMaxStep(new Integer(4));
						}
		}
		//
		
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
		
		try
		{
			
			// clears all the values once portfolio is clicked
			if( request.getParameter("clear") != null && request.getParameter("clear").equals("true"))
			{
				//logger.info("from ViewMyDesktop");
				formBean.setAmpColumns(null);
				formBean.setAddedColumns(null);
				formBean.setColumnHierarchie(null);
				formBean.setAddedMeasures(null);
				formBean.setReportTitle("");
				formBean.setReportDescription("");
				formBean.setAdjustType(null);
				formBean.setSelAdjustType(null);
				formBean.setSelectedAdjustmentType(null);
				
				formBean.setInEditingMode(false);
				formBean.setDbReportId( 0 );
				formBean.setMaxStep(new Integer(0));				
				formBean.setDuplicatedReportName(false);
				formBean.setDuplicatedReportOwner(null);
			} 
			
			
			// Fills the column that can be selected from AMP_COLUMNS
			if(formBean.getAmpColumns() == null)
			{
				formBean.setAmpColumns(ReportUtil.getColumnList());
				formBean.setAmpMeasures(ReportUtil.getMeasureList());
				formBean.setReportTitle("");
				formBean.setReportDescription("");
				iter = formBean.getAmpMeasures().iterator();
				Collection tempColl = new ArrayList();
				while(iter.hasNext())
				{
					AmpMeasures ampMeasure = (AmpMeasures) iter.next();
					if(ampMeasure.getType().equals("A") == true)
						tempColl.add(ampMeasure);
				}
				formBean.setAdjustType(tempColl);
				
			}
		//	else
		//		logger.info(" AmpColumns is not NULL........");
			
			// Add the columns selected : Step 2
			if(request.getParameter("check") != null && request.getParameter("check").equals("SelectCols"))
			{
				//Advanced Report Builder Report Type Check
				String arReportType="Donor";
				arReportType=request.getParameter("reportType");
				logger.info("arReportType::::"+arReportType);
				 Only change the Advanced Report Type if it has been selected on the previous webpage 
				if ( arReportType != null && arReportType.compareTo("") != 0 )
					formBean.setArReportType(arReportType);
				
				//logger.info("inside Step 1...");
				if (formBean.getMaxStep().intValue() < 1)
					formBean.setMaxStep(new Integer(1));
				
				HashMap ampTreeColumns=this.buildAmpTreeColumnSimple(ReportUtil.getColumnList(), formBean.getArReportType());
				//TODO
				////System.out.println("ssssssssssssssssssss"+formBean.getArReportType());
				formBean.setAmpTreeColumns(ampTreeColumns);
				return mapping.findForward("SelectCols");
			}
			// add columns that are available
			if(request.getParameter("check") != null && request.getParameter("check").equals("add"))
			{
				str = request.getParameter("check");
				//logger.info( "Operation is : " + str);
				updateData(formBean.getAmpColumns(), formBean.getAddedColumns(), formBean.getSelectedColumns(), formBean);
				formBean.setSelectedColumns(null);
				if (formBean.getMaxStep().intValue() < 1)
					formBean.setMaxStep(new Integer(1));
				return mapping.findForward("SelectCols");
			}
			// Remove the columns selected
			if(request.getParameter("check") != null && request.getParameter("check").equals("delete"))
			{
				str = request.getParameter("check");
				//logger.info( "Operation is : " + str);
				updateData(formBean.getAddedColumns(), formBean.getAmpColumns() , formBean.getRemoveColumns(), formBean);
				formBean.setRemoveColumns(null);
				if (formBean.getMaxStep().intValue() < 1)
					formBean.setMaxStep(new Integer(1));
				return mapping.findForward("SelectCols");
			}
			
			// Add the columns selected : Step 2
			if(request.getParameter("check") != null && request.getParameter("check").equals("Step2AddRows"))
			{
				str = request.getParameter("check");
				updateData(formBean.getAddedColumns(), formBean.getColumnHierarchie(), formBean.getSelectedColumns(), formBean);
				formBean.setSelectedColumns(null);
				if (formBean.getMaxStep().intValue() < 2)
					formBean.setMaxStep(new Integer(2));
				return mapping.findForward("SelectRows");
			}
			// Remove the columns selected : Step 2
			if(request.getParameter("check") != null && request.getParameter("check").equals("Step2DeleteRows"))
			{
				str = request.getParameter("check");
				//logger.info( "Operation is : " + str);
				updateData(formBean.getColumnHierarchie(), formBean.getAddedColumns(), formBean.getRemoveColumns(), formBean);
				formBean.setRemoveColumns(null);
				if (formBean.getMaxStep().intValue() < 2)
					formBean.setMaxStep(new Integer(2));
				return mapping.findForward("SelectRows");
			}
			// Step 3 : Select Measures
			if(request.getParameter("check") != null && request.getParameter("check").equals("AddMeasure"))
			{
				str = request.getParameter("check");
				//logger.info( "Operation is : " + str);
				updateData(formBean.getAmpMeasures(), formBean.getAddedMeasures(), formBean.getSelectedColumns(), formBean);
				formBean.setSelectedColumns(null);
				if (formBean.getMaxStep().intValue() < 3)
					formBean.setMaxStep(new Integer(3));
				return mapping.findForward("SelectMeasures");
			}
			
			// Remove the columns selected
			if(request.getParameter("check") != null && request.getParameter("check").equals("DeleteMeasure"))
			{
				str = request.getParameter("check");
				//logger.info( "Operation is : " + str);
				updateData(formBean.getAddedMeasures(), formBean.getAmpMeasures() , formBean.getRemoveColumns(), formBean);
				formBean.setRemoveColumns(null);
				if (formBean.getMaxStep().intValue() < 3)
					formBean.setMaxStep(new Integer(3));
				return mapping.findForward("SelectMeasures");
			}

			
			// Step 3 : Select Adjustment Type
			if(request.getParameter("check") != null && request.getParameter("check").equals("AddAdjustType"))
			{
				str = request.getParameter("check");
				logger.info( "Operation is : " + str + "" + formBean.getAdjustType().size());
				updateData(formBean.getAdjustType(), formBean.getSelAdjustType(), formBean.getSelectedAdjustmentType(), formBean);
				formBean.setSelectedAdjustmentType(null);
				return mapping.findForward("SelectMeasures");
			}
			
			// Remove the columns selected
			if(request.getParameter("check") != null && request.getParameter("check").equals("DeleteAdjustType"))
			{
				str = request.getParameter("check");
				logger.info( "Operation is : " + str);
				updateData(formBean.getSelAdjustType(), formBean.getAdjustType() , formBean.getRemoveAdjustType(), formBean);
				formBean.setRemoveAdjustType(null);
				return mapping.findForward("SelectMeasures");
			}
			
			// Goto Step 2.

			if(request.getParameter("check") != null && request.getParameter("check").equals("5"))
			{
				//logger.info("In here  generating data..........");
				int fromYr = 0, toYr = 0, year=0, fiscalCalId;
				String ampCurrencyCode=null;
				AmpCurrency ampCurrency=null;
				GregorianCalendar c=new GregorianCalendar();
				year=c.get(Calendar.YEAR);
				//for storing the value of year filter 
				 report Title check 
				ActionMessages errors = new ActionMessages();	
				AmpTeamMember found = ReportUtil.checkDuplicateReportName(formBean.getReportTitle());
				if(found!=null)
				{ 
					formBean.setDuplicatedReportName(true);
					formBean.setDuplicatedReportOwner(teamMember.getMemberName() + " - " + teamMember.getTeamName());					
					return mapping.findForward("MissingReportDetails");
				}else{
					formBean.setDuplicatedReportName(false);
					formBean.setDuplicatedReportOwner(null);
				}
				
				// report title check ends hereerrors.add("DuplicateReportName", new ActionMessage("error.aim.reportManager.DuplicateReportName"));
				saveErrors(request, errors);
				
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
						fiscalCalId=teamMember.getAppSettings().getFisCalId().intValue();
					}
				}
				if(formBean.getAmpCurrencyCode()==null || formBean.getAmpCurrencyCode().equals("0"))
				{
					ampCurrency=CurrencyUtil.getAmpcurrency(teamMember.getAppSettings().getCurrencyId());
					ampCurrencyCode=ampCurrency.getCurrencyCode();
					formBean.setAmpCurrencyCode(ampCurrencyCode);
				}
				else
					ampCurrencyCode=formBean.getAmpCurrencyCode();

				
				//logger.info(fromYr + ":  From year - TO Year : " + toYr);
				// Gets the Transaction and Adjustement Type from the user
				ArrayList adjType = new ArrayList(), transc = new ArrayList();
				int count = 0;
				
				if(formBean.getSelAdjustType() != null)
				{
					
					iter = formBean.getSelAdjustType().iterator();
					while(iter.hasNext())
					{
						AmpMeasures ampMeasures = (AmpMeasures) iter.next();
						logger.info("Adjustment : " + ampMeasures.getMeasureId() + "" + ampMeasures.getMeasureName());
						adjType.add(count, ampMeasures.getMeasureId());
						count = count + 1;
					}
				}
				
				if(formBean.getAddedMeasures() != null)
				{
					count = 0;
					iter = formBean.getAddedMeasures().iterator();
					while(iter.hasNext())
					{
						AmpMeasures ampMeasure = (AmpMeasures) iter.next();
						//logger.info("Transaction : " + ampMeasure.getMeasureId() + "" + ampMeasure.getMeasureName());
						transc.add(count, ampMeasure.getMeasureId());
						selectedTransc.add(ampMeasure.getMeasureName().toString());
						count = count + 1;
					}
				}
				//logger.info(transc.size() + "________________ " + adjType.size());
				//logger.info("Adjustment: " + adjType.size());
				//logger.info("Transaction: " + transc.size());
				
				if(formBean.getColumnHierarchie()==null)
					formBean.setHierarchyFlag("false");
				else
					formBean.setHierarchyFlag("true");
				ArrayList measures = transc;
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

				if(formBean.getAcBalFlag().equals("true"))
					formBean.setMeasureCount(measures.size() - 1);
				else
					formBean.setMeasureCount(measures.size());

				formBean.setFundColumns((measures.size()));
	
				
				boolean allPages = false;
				if(formBean.getAddedColumns() != null)
				{
					//formBean.setFinalData(ReportUtil.generateQuery(formBean.getAddedColumns(),ampTeamId));
					
					Collection pageRecords = new ArrayList();
					int page=0;
					
					Collection aa= formBean.getColumnHierarchie();
					////System.out.println("hhhhhhhh: "+aa.size());
					Iterator itr=aa.iterator();
					String str="";
					AmpColumns amp=new AmpColumns();
					while(itr.hasNext()){
						////System.out.println("********************"+itr.next().getClass().getName());
						if(itr.next() instanceof AmpColumns){
							amp=(AmpColumns) itr.next();
							////System.out.println("h2h2h2h2h2h2:"+amp.getColumnName());
						}
					}
					
					ReportSelectionCriteria rsc=new ReportSelectionCriteria();
					
					if ("donor".equals(formBean.getReportType())) rsc.setType(new Long(1));
					if ("regional".equals(formBean.getReportType())) rsc.setType(new Long(3));
					if ("component".equals(formBean.getReportType())) rsc.setType(new Long(2));
					if ("contribution".equals(formBean.getReportType())) rsc.setType(new Long(4));
					
					rsc.setColumns(new ArrayList());
					iter=formBean.getAddedColumns().iterator();
					while(iter.hasNext())
					{
						AmpColumns ampColumns=(AmpColumns) iter.next();
						Column col=new Column();
						col.setColumnId(ampColumns.getColumnId());
						col.setColumnName(ampColumns.getColumnName());
						col.setColumnAlias(ampColumns.getAliasName());
						rsc.getColumns().add(col);
					}
					rsc.setHierarchy(new ArrayList());
					if(formBean.getColumnHierarchie()!=null)
					{
						iter=formBean.getColumnHierarchie().iterator();
						while(iter.hasNext())
						{
							AmpColumns ampColumns=(AmpColumns) iter.next();
							Column col=new Column();
							col.setColumnId(ampColumns.getColumnId());
							col.setColumnName(ampColumns.getColumnName());
							col.setColumnAlias(ampColumns.getAliasName());
							rsc.getHierarchy().add(col);
						}
					}
					rsc.setMeasures(transc);
					if(formBean.getReportOption()==null)
							  rsc.setOption(Constants.ANNUAL);
					else
								rsc.setOption(formBean.getReportOption());
//					String startDate = (year-Constants.FROM_YEAR_RANGE) + "-" + 01 + "-" + 01;
//					String closeDate = (year + Constants.TO_YEAR_RANGE) + "-" + 12 + "-" + 31;
					if (request.getParameter("page") == null) 
					{
						page = 1;
//						reports=ReportUtil.generateAdvancedReport(ampTeamId,fromYr,toYr,fiscalCalId,ampCurrencyCode,perspective, transc, formBean.getAddedColumns(),formBean.getColumnHierarchie());
						reports=ReportUtil.getAdvancedReport(ampTeamId,fromYr,toYr,perspective,ampCurrencyCode,All,All,All,All,fiscalCalId,null,null,"All","All",rsc);
						//logger.info("Page is NULL............................" + reports.size());
						formBean.setFinalData(reports);
						httpSession.setAttribute("ampReports",reports);
						
					}
					else 
					{
						//logger.info("  ------>>>>>>>>    " + formBean.getFinalData().size());
						if(request.getParameter("page").equals("all") == true)
							allPages = true;
						
						else
						{
							page = Integer.parseInt(request.getParameter("page"));
							reports=(ArrayList)httpSession.getAttribute("ampReports");
							//logger.info("Page is NOT NULL.................");
						}
					}
					
					if(allPages == true)
					{
						//logger.info("IN ALl Records.........>>>" + formBean.getFinalData().size());
						formBean.setReport(formBean.getFinalData());
						formBean.setFiscalYearRange(new ArrayList());
						for(int yr=fromYr;yr<=toYr;yr++)
							formBean.getFiscalYearRange().add(new Integer(yr));
						reports=(ArrayList)httpSession.getAttribute("ampReports");
					}
					else
					{
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
						//logger.info("< For each Page Record size is ..... >" + pageRecords.size());
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
						
						int yearRange=(toYr-fromYr)+1;
						formBean.setFiscalYearRange(new ArrayList());
						for(int yr=fromYr;yr<=toYr;yr++)
							formBean.getFiscalYearRange().add(new Integer(yr));
						formBean.setTotalColumns(14);
						formBean.setOption(rsc.getOption());
						formBean.setOptions(new ArrayList());
						if(rsc.getOption().equals(Constants.ANNUAL))
							formBean.getOptions().add("Y");
						else
						{
							formBean.getOptions().add("Q1");
							formBean.getOptions().add("Q2");
							formBean.getOptions().add("Q3");
							formBean.getOptions().add("Q4");
						}	
						if(transc.indexOf(new Long(7))!=-1)
							formBean.setQuarterColumns(4*(transc.size()-1));
						else
							formBean.setQuarterColumns(4*transc.size());
						if(formBean.getColumnHierarchie()==null)
						{
							formBean.setPages(pages);
							formBean.setReport(pageRecords);
							formBean.setPage(new Integer(page));
						}
						else
						{
							formBean.setMultiReport(reports);
							//logger.info("multireport size...."+formBean.getMultiReport().size());
						}
							
						formBean.setAllReports(reports);
						//logger.info(" page REC " + pageRecords.size());
				 		//logger.info(" REPORTS  " + reports.size());
					}

//					---------------------------------------------------		
					BEGIN CODE FOR GRAND TOTAL
					//logger.info("BEGIN CODE FOR GRAND TOTAL..............");
					int yearRange=(toYr-fromYr)+1;
					if(formBean.getColumnHierarchie()==null)
					{
						if(rsc.getOption().equals(Constants.ANNUAL))
						{
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
									org.digijava.module.aim.helper.AdvancedReport advancedReport=(org.digijava.module.aim.helper.AdvancedReport) advIter.next();
									if(advancedReport.getAmpFund()!=null)
									{
										Iterator iterFund = advancedReport.getAmpFund().iterator();
										for(int i=0;i<=yearRange ;i++ )
										{
											AmpFund ampFund=(AmpFund) iterFund.next();
											if(i<yearRange)
											{
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
											}
											if(i==yearRange)
											{
												actTotalCommit = actTotalCommit + Double.parseDouble(DecimalToText.removeCommas(ampFund.getCommAmount()));
												actTotalDisb = actTotalDisb + Double.parseDouble(DecimalToText.removeCommas(ampFund.getDisbAmount()));
												actTotalExp = actTotalExp + Double.parseDouble(DecimalToText.removeCommas(ampFund.getExpAmount()));
												planTotalCommit = planTotalCommit  + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlCommAmount()));
												planTotalDisb = planTotalDisb + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlDisbAmount()));
												planTotalExp = planTotalExp + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlExpAmount()));
												totUnDisb = totUnDisb + Double.parseDouble(DecimalToText.removeCommas(ampFund.getUnDisbAmount()));
											}
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
						}
						else
						{
							double totUnDisb = 0, actTotalCommit = 0, actTotalDisb = 0, actTotalExp = 0, planTotalCommit = 0, planTotalDisb = 0, planTotalExp = 0;
							double[][] totAcCommFunds=new double[yearRange][4];
							double[][] totAcDisbFunds=new double[yearRange][4];
							double[][] totAcExpFunds=new double[yearRange][4];
							double[][] totPlCommFunds=new double[yearRange][4];
							double[][] totPlDisbFunds=new double[yearRange][4];
							double[][] totPlExpFunds=new double[yearRange][4];
							iter = reports.iterator() ;
							//	logger.debug("Grand Total :" + grandTotal);
							while ( iter.hasNext() )
							{
								Report report=(Report) iter.next();
								Iterator advIter=report.getRecords().iterator();
								while(advIter.hasNext())
								{
									org.digijava.module.aim.helper.AdvancedReport advancedReport=(org.digijava.module.aim.helper.AdvancedReport) advIter.next();
									if(advancedReport.getAmpFund()!=null)
									{
										Iterator iterFund = advancedReport.getAmpFund().iterator();
										for(int i=0;i<=yearRange ;i++ )
										{
											if(i<yearRange)
											{
												for(int qtr=0;qtr<4;qtr++)
												{
													AmpFund ampFund=(AmpFund) iterFund.next();
													if(measures.indexOf(new Long(1))!=-1)
														totAcCommFunds[i][qtr]=totAcCommFunds[i][qtr] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getCommAmount()));
													if(measures.indexOf(new Long(2))!=-1)
														totAcDisbFunds[i][qtr]=totAcDisbFunds[i][qtr] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getDisbAmount()));
													if(measures.indexOf(new Long(3))!=-1)
														totAcExpFunds[i][qtr]=totAcExpFunds[i][qtr] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getExpAmount()));
													if(measures.indexOf(new Long(4))!=-1)
														totPlCommFunds[i][qtr]=totPlCommFunds[i][qtr] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlCommAmount()));
													if(measures.indexOf(new Long(5))!=-1)
														totPlDisbFunds[i][qtr]=totPlDisbFunds[i][qtr] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlDisbAmount()));
													if(measures.indexOf(new Long(6))!=-1)
														totPlExpFunds[i][qtr]=totPlExpFunds[i][qtr] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlExpAmount()));
												}
											}	

											if(i==yearRange)
											{
												AmpFund ampFund=(AmpFund) iterFund.next();
												actTotalCommit = actTotalCommit + Double.parseDouble(DecimalToText.removeCommas(ampFund.getCommAmount()));
												actTotalDisb = actTotalDisb + Double.parseDouble(DecimalToText.removeCommas(ampFund.getDisbAmount()));
												actTotalExp = actTotalExp + Double.parseDouble(DecimalToText.removeCommas(ampFund.getExpAmount()));
												planTotalCommit = planTotalCommit  + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlCommAmount()));
												planTotalDisb = planTotalDisb + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlDisbAmount()));
												planTotalExp = planTotalExp + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlExpAmount()));
												totUnDisb = totUnDisb + Double.parseDouble(DecimalToText.removeCommas(ampFund.getUnDisbAmount()));
											}
										}	
									}
								}	
							}
							formBean.setTotFund(new ArrayList());
							for(int i=0;i<yearRange ;i++ )
							{	
								for(int qtr=0;qtr<4;qtr++)
								{	
									AmpFund ampFund=new AmpFund();
									if(measures.indexOf(new Long(1))!=-1)
										ampFund.setCommAmount(mf.format(totAcCommFunds[i][qtr])); 
									if(measures.indexOf(new Long(2))!=-1)
										ampFund.setDisbAmount(mf.format(totAcDisbFunds[i][qtr])); 
									if(measures.indexOf(new Long(3))!=-1)
										ampFund.setExpAmount(mf.format(totAcExpFunds[i][qtr]));	
									if(measures.indexOf(new Long(4))!=-1)
										ampFund.setPlCommAmount(mf.format(totPlCommFunds[i][qtr])); 
									if(measures.indexOf(new Long(5))!=-1)
										ampFund.setPlDisbAmount(mf.format(totPlDisbFunds[i][qtr])); 
									if(measures.indexOf(new Long(6))!=-1)
										ampFund.setPlExpAmount(mf.format(totPlExpFunds[i][qtr]));	
									formBean.getTotFund().add(ampFund);
								}	
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
						}
					}	
					END CODE FOR GRAND TOTAL
				//	logger.info("END CODE FOR GRAND TOTAL..............");

					formBean.setForecastYear(new ArrayList());
					for(int i=toYr;i<=(toYr+3);i++)
						formBean.getForecastYear().add(new Integer(i));

				}
			//	logger.info("###----------------------------------------------------------------------#####");

				return mapping.findForward("GenerateReport");
			}
					
			

			// Step 4 : Report Details
			if(request.getParameter("check") != null && request.getParameter("check").equals("4"))
			{
				//logger.info("In here  Getting Report Details..........");
				if (formBean.getMaxStep().intValue() < 4)
					formBean.setMaxStep(new Integer(4));

				return mapping.findForward("ReportDetails");
			}
			if(request.getParameter("check") != null && request.getParameter("check").equals("SelectColumn"))
				return mapping.findForward("forward");
			
			if(request.getParameter("check") != null && request.getParameter("check").equals("SelectRows"))
				return mapping.findForward("SelectRows");


			if(request.getParameter("check") != null && request.getParameter("check").equals("SelectMeasures"))
				return mapping.findForward("SelectMeasures");


			// step 3 : 
			if(request.getParameter("check") != null && request.getParameter("check").equals("charts"))
			{
				//logger.info("In here  chart process####..........");

				//logger.info("###########################Inside GeneratePIEChart..:):)");
				//logger.info("CHART FORMBEAN SIZE::::::::::::::::"+formBean.getFinalData().size());

		        //Iterator iter2 = formBean.getFinalData().iterator();
		        Collection chart_coll=new ArrayList();
		        String title = "", commit = "", comm="", disb="", exp="";
				iter = formBean.getFinalData().iterator();
				Collection colls = null;
				Collection ampFund= null;

				Iterator it, fundItr=null;

				while(iter.hasNext())
				{
					Report report = (Report) iter.next();
					colls = report.getRecords();
					
					it = colls.iterator();

					while(it.hasNext())
					{
						org.digijava.module.aim.helper.AdvancedReport advReport = (org.digijava.module.aim.helper.AdvancedReport)it.next();
						if(advReport.getTitle() != null)
							title = advReport.getTitle();
						
						if(advReport.getAmpFund() != null){
								//logger.info("ampFund is NOT NULL....");
							fundItr = advReport.getAmpFund().iterator();

							AmpFund ampFund1 = new AmpFund();
							while(fundItr.hasNext()){
								ampFund1 = (AmpFund) fundItr.next();
								comm = ampFund1.getCommAmount();
								disb = ampFund1.getDisbAmount();
								exp = ampFund1.getExpAmount();
							}
						}

						if(advReport.getActualCommitment() != null)
							commit = advReport.getActualCommitment();
							//chart_coll.add(advReport.getActualCommitment().replaceAll("," , ""));
						//chart_coll.add(advReport.getTitle());
					}//end of while
					//logger.info("ZZZZZZZZZZz"+title+"<------***********------->"  + comm +"<------***********------->" +disb + "<------***********------->"  + exp );
					//chart_coll.add(new Double(commit.replaceAll(",", "")) );

					chart_coll.add(new Double(comm.replaceAll(",", "")) );
					chart_coll.add(title);
					chart_coll.add(new Double(disb.replaceAll(",", "")) );
					chart_coll.add(title);
					chart_coll.add(new Double(exp.replaceAll(",", "")) );
					chart_coll.add(title);

				}
				
				//logger.info("  Chart Size : " +chart_coll.size());

		        
//		    	chart_coll.add("60");
//		    	chart_coll.add("Donor 1");

				while(iter2.hasNext()){
					Report r= (Report) iter2.next();
					chart_coll.add(r.getAcCommitment().replaceAll("," , ""));
//					logger.info("filling COMM into the COLLLLL."+r.getAcCommitment());
					chart_coll.add(r.getDonor());
					//logger.info("filling DONOR NAME into the COLLLLL."+r.getDonor());
				}
				
				// calling Piechart
				//String piechartname=createPieChart(chart_coll);
				//logger.info("@@@@@@@@@@IMAGE FILE NAME:"+piechartname);
				//formBean.setPieImageUrl(piechartname);

				// calling Barchart
				//String barchartname=createBarChart(chart_coll);
				//logger.info("@@@@@@@@@@IMAGE FILE NAME:"+piechartname);
				//formBean.setBarImageUrl(barchartname);

				return mapping.findForward("GenerateChart");
			}


			// Step 4 : Report Details
			if(request.getParameter("check") != null && request.getParameter("check").equals("4"))
			{
				//logger.info("In here  Getting Report Details..........");
				return mapping.findForward("ReportDetails");
			}
			
			// Move the selected column Up : Step 1 ie Select Columns
			if(request.getParameter("check") != null && request.getParameter("check").equals("MoveUp")){
				moveColumns(formBean, "MoveUp");
				return mapping.findForward("SelectCols");
			}
			if(request.getParameter("check") != null && request.getParameter("check").equals("Step2MoveUp"))
			{
				moveColumns(formBean, "Step2MoveUp");
				return mapping.findForward("SelectRows");
			}
			if(request.getParameter("check") != null && request.getParameter("check").equals("MoveUpMeasure"))
			{
				moveColumns(formBean, "MoveUpMeasure");
				return mapping.findForward("SelectMeasures");
			}

			// Move the selected column Down : Step 1 ie Select Columns
			if(request.getParameter("check") != null && request.getParameter("check").equals("MoveDown")){
				moveColumns(formBean, "MoveDown");
				return mapping.findForward("SelectCols");
			}
			if(request.getParameter("check") != null && request.getParameter("check").equals("Step2MoveDown"))
			{
				moveColumns(formBean, "Step2MoveDown");
				return mapping.findForward("SelectRows");
			}
			if(request.getParameter("check") != null && request.getParameter("check").equals("MoveDownMeasure"))
			{
				moveColumns(formBean, "MoveDownMeasure");
				return mapping.findForward("SelectMeasures");
			}
			
			
			if(request.getParameter("check") != null && request.getParameter("check").equals("MoveUpAdjustType"))
			{
				moveColumns(formBean, "MoveUpAdjustType");
				return mapping.findForward("SelectMeasures");
			}
			if(request.getParameter("check") != null && request.getParameter("check").equals("MoveDownAdjustType"))
			{
				moveColumns(formBean, "MoveDownAdjustType");
				return mapping.findForward("SelectMeasures");
			}
			
			// save Report
			if(request.getParameter("check") != null && request.getParameter("check").equals("SaveReport"))
			{
				boolean flag = false;
				//logger.info("---------Start--Report --- Save -------------");
				ActionMessages errors = new ActionMessages();	
				if(formBean.getReportTitle() != null)
				{
					if(formBean.getReportTitle().trim().length() == 0)
					{
							errors.add("title", new ActionMessage("error.aim.reportManager.ReportNameAbsent"));
							saveErrors(request, errors);
							flag = true;
							return mapping.findForward("MissingReportDetails");
					}
				}
				
				if(flag == false )
				{
					int i 			= 0;
					AmpTeamMember found 	= null;
					 No need to check for duplicate if editing an exisiting report 
					if ( !formBean.getInEditingMode() ){
						found 			= ReportUtil.checkDuplicateReportName(formBean.getReportTitle());
						if (formBean.getInEditingMode() == true) {
							
							found = new AmpTeamMember(); //stupid fix
						}
						if(found!=null )
						{ 
							formBean.setDuplicatedReportName(true);
							formBean.setDuplicatedReportOwner(teamMember.getMemberName() + " - " + teamMember.getTeamName());
							return mapping.findForward("MissingReportDetails");
						}else{
							formBean.setDuplicatedReportName(false);
							formBean.setDuplicatedReportOwner(null);
						}
					}
					
	           	if(found == null)
	            {
						//logger.info("............no duplicate report title............");
		           		Session pmsession		= PersistenceManager.getSession();						
						AmpReports ampReports = new AmpReports();
						
						if ("donor".equals(formBean.getReportType())) ampReports.setType(new Long(1));
						if ("regional".equals(formBean.getReportType())) ampReports.setType(new Long(3));
						if ("component".equals(formBean.getReportType())) ampReports.setType(new Long(2));
						if ("contribution".equals(formBean.getReportType())) ampReports.setType(new Long(4));						
						
						ampReports.setReportDescription(formBean.getReportDescription());
						ampReports.setName(formBean.getReportTitle().trim());
						AmpTeamMember ampTeamMember = TeamUtil.getAmpTeamMember(teamMember.getMemberId());
						ampReports.setOwnerId(ampTeamMember);
						ampReports.setUpdatedDate(new Date(System.currentTimeMillis()));
						if ( formBean.getInEditingMode() )
								ampReports.setAmpReportId( new Long(formBean.getDbReportId()) );
						else 
								ampReports.setAmpReportId(new Long("0"));
						if(formBean.getReportOption()==null)
								  ampReports.setOptions(Constants.ANNUAL);
						else
								ampReports.setOptions(formBean.getReportOption());
						
						// saving the selected columns for the report
						Set columns = new HashSet();
						if(formBean.getAddedColumns() != null)
						{
							iter = formBean.getAddedColumns().iterator();
							i = 1;
							while(iter.hasNext())
							{
								AmpColumns cols = (AmpColumns)iter.next();

									AmpReportColumn	temp = new AmpReportColumn();
									temp.setColumn(cols);
									temp.setOrderId(""+i);
									columns.add(temp);
									i = i + 1;
								}
								ampReports.setColumns(columns);
							}
						
							if(formBean.getColumnHierarchie() != null)
							{
								// saving the column hierarchies in step2 
								Set hierarchies = new HashSet();
								iter  = formBean.getColumnHierarchie().iterator();
								i = 1;
								while(iter.hasNext())
								{
									AmpColumns cols = (AmpColumns)iter.next();
								
									AmpReportHierarchy reportHierarchy = new AmpReportHierarchy();
									reportHierarchy.setColumn(cols);
									reportHierarchy.setLevelId(""+i);
									hierarchies.add(reportHierarchy);
									i = i + 1;
								}
								ampReports.setHierarchies(hierarchies);
							}
						
							// saving the AMp Report Measures
							Set measures = new HashSet();
							if(formBean.getAddedMeasures() != null)
							{
								iter = formBean.getAddedMeasures().iterator();
								i = 1;
								while(iter.hasNext())
								{
									AmpMeasures ampMeasures = (AmpMeasures) iter.next();
									measures.add(ampMeasures);
									i = i + 1;
								}
							}
						
							if(formBean.getSelAdjustType() != null)
							{
								iter = formBean.getSelAdjustType().iterator();
								
								while(iter.hasNext())
								{
									AmpMeasures ampMeasures = (AmpMeasures) iter.next();
									measures.add(ampMeasures);
								}
							}
							ampReports.setMeasures(measures);
							ampReports.setHideActivities(formBean.getHideActivities());
							ampReports.setDrilldownTab(formBean.getDrilldownTab());
							ampReports.setPublicReport(formBean.getPublicReport());
							
							if ( formBean.getInEditingMode() ) { // Editing an exisiting report
//								logger.info ("Updating report.." );
								pmsession.update( ampReports );
//session.flush();
//								log the update action for a reportSaveReport
								AuditLoggerUtil.logObject(httpSession,request,ampReports,"update");
								
							}
							else { // This is the case of a new Report being created
//								logger.info ("Saving report.." );
								ReportUtil.saveReport(ampReports,teamMember.getTeamId(),teamMember.getMemberId(),teamMember.getTeamHead());
//								log the add action for a report
								AuditLoggerUtil.logObject(httpSession,request,ampReports,"add");
							}
							
							HttpSession hs = request.getSession();
							hs.removeAttribute(Constants.MY_REPORTS);
							
				
							// Clears the values of the Previous report 
							formBean.setAmpColumns(null);
							formBean.setAddedColumns(null);
							formBean.setColumnHierarchie(null);
							formBean.setAddedMeasures(null);
							
							formBean.setInEditingMode( false );
							formBean.setDbReportId( 0 );
								
							formBean.setHideActivities(null);
							formBean.setDrilldownTab(null);
							formBean.setPublicReport(null);
							formBean.setReportType(null);		
							formBean.setReportOption(null);
			            }
					
				}
				
				return mapping.findForward("viewMyDesktop");
			}

		}
		catch(Exception e)
		{
			logger.info("-------------Inside Catch-----------");
			e.printStackTrace(System.out);
		}
		return mapping.findForward("forward");
	}// end of function execute
	
// Function for Pie Chart
	
		private static String createPieChart(Collection chart_coll){

				//logger.info("@@@@@@@@@@ INSIDE createPieChart...");
				Iterator iter3 = chart_coll.iterator();
				//logger.info("@@@@@@@@@@ flag:"+chart_coll.size());
				String temp="";
				Double demp;

				DefaultPieDataset data = new DefaultPieDataset();
//		        data.setValue("test12Aug", new Double(2.0));
		        
				while (iter3.hasNext()) {
					demp=new Double(iter3.next().toString());
					temp= (String)iter3.next();
					//logger.info(temp+":::::"+demp);
					data.setValue(temp, demp);
//					iter.next();
		        }

		        //  Create the chart object
				//logger.info("@@@@@@@@@@ PLOTTTTTTT:");
		        PiePlot plot = new PiePlot(data);
		        plot.setURLGenerator(new StandardPieURLGenerator("xy_chart.jsp","section"));
		        plot.setToolTipGenerator(new StandardPieItemLabelGenerator());
				//logger.info("@@@@@@@@@@ Chart Object:");
				JFreeChart chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT, plot, false);
		        chart.setBackgroundPaint(java.awt.Color.white);

		        //  Write the chart image to the temporary directory
		        ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
		        String filename = "";
		        try{
				//logger.info("@@@@@@@@@@ IMAGE CREATION PNG:");
				//filename = ServletUtilities.saveChartAsPNG(chart, 600, 550, info, httpSession);
//				ServletUtilities.sendTempFile(filename, response);
				}
				catch(Exception e){
					logger.info("EXCEPTION thrown at PIECHARTimage: "+e);				
				}

			return filename;
		}
		
// end pie chart function.

//		 Function for BarChart
	
		private static String createBarChart(Collection chart_coll){

			//logger.info("@@@@@@@@@@ INSIDE createBARRRRRR Chart...");

			String filename = null;
			DefaultCategoryDataset data = new DefaultCategoryDataset(); 
	        Iterator iter = chart_coll.iterator();
			////////System.out.println("@@@@@@@@@@ flag:"+chart_coll.size());

//			data.addValue(new Double(30.0), "CDAC", "");
			
			String temp="";
			Double demp;

			while (iter.hasNext()) {
				demp=new Double(iter.next().toString());
				temp= (String)iter.next();
//				////System.out.println(temp+":::::"+demp);
				data.addValue(demp,temp,"comm");
				demp=new Double(iter.next().toString());
				temp= (String)iter.next();
//				////System.out.println(temp+":::::"+demp);
				data.addValue(demp,temp,"disb");
				demp=new Double(iter.next().toString());
				temp= (String)iter.next();
//				////System.out.println(temp+":::::"+demp);
				data.addValue(demp,temp,"exp");
	        }

		// Create the chart object 

			CategoryDataset categorydataset = new DefaultCategoryDataset();
			categorydataset = data;

			//String chartTitle=formBean.getReportTitle();
			String chartTitle="";
//			////System.out.println("CHART TITLE----(ACTION)----------------"+chartTitle);
			
			JFreeChart jfreechart = ChartFactory.createBarChart(chartTitle+" - Bar Chart", "Title", "Amount(in US$)", categorydataset, PlotOrientation.VERTICAL,true, true, true);
			jfreechart.setBackgroundPaint(java.awt.Color.white);
			
			// Write the chart image to the temporary directory 
			ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
	
			//logger.info("  Chart Size : " +chart_coll.size());
			int dim=chart_coll.size()/6;
			int x=dim*30;
			int y=dim*25;
				////System.out.println("IMG DIMMMMMMMMMMMMM: "+"dim="+dim+"x="+x+"::"+y);
			try{
			//filename = ServletUtilities.saveChartAsPNG(jfreechart, x, y, info, httpSession);
			}
			catch(Exception e){
					logger.info("EXCEPTION thrown at image:"+e);				
			}

//			//////System.out.println("@@@@@@@@@@IMAGE FILE NAME:"+filename);

			return filename;
		}
		
// end barchart function.

	
	public void updateData(Collection src, Collection dest, Long []selCol, AdvancedReportForm formBean)
	{
		if(str.equals("delete"))
		{
			if(src == null)
				return;
		}
		Iterator iter;
		Collection coll = new ArrayList();
		Collection temp = new ArrayList();
		Collection  dup= new ArrayList();
		AmpColumns ampColumns;
		AmpMeasures ampMeasures = null, tempMeasures = null;
		boolean flag=false;
		AmpColumns colTemp = null;
		try
		{
			if(selCol != null)
			{
				if(dest == null)
				{
					dup = src;
					coll.clear();
					temp.clear();
				}
				else
				{
					temp.clear();
					dup = src;
					coll = dest;
				}
				
				for(int i=0; i < selCol.length; i++)
				{
					iter = src.iterator();// change needed
					temp.clear();
					while(iter.hasNext())
					{
						if(str.equals("AddMeasure") == true || str.equals("DeleteMeasure") == true 
								|| str.equals("AddAdjustType") == true || str.equals("DeleteAdjustType") == true )
						{
							ampMeasures = (AmpMeasures) iter.next();
							if(ampMeasures.getMeasureId().compareTo(selCol[i]) == 0 )
							{
								coll.add(ampMeasures);
								tempMeasures = ampMeasures;
								flag = true;
							}
							else
							{
								if(temp.contains(ampMeasures) == false)
									temp.add(ampMeasures);
							}
						}
						else
						{
							ampColumns = (AmpColumns) iter.next();
							if(ampColumns.getColumnId().compareTo(selCol[i]) == 0)
							{
								coll.add(ampColumns);
								colTemp = ampColumns;
								flag = true;
							}
							else
							{
								if(temp.contains(ampColumns) == false)
									temp.add(ampColumns);
							}
						}
					}
					if(flag == true && colTemp != null)
					{
						dup.remove(colTemp);
						flag = false;
						src = dup;
					}
					if(flag == true && tempMeasures != null)
					{
						dup.remove(tempMeasures);
						flag = false;
						src = dup;
					}

				}

				if(str.equals("add"))
				{
					formBean.setAddedColumns(coll);
					formBean.setAmpColumns(temp);
				}
				if(str.equals("delete"))
				{
					formBean.setAddedColumns(temp);
					formBean.setAmpColumns(coll);
				}
				if(str.equals("Step2AddRows") == true)
				{
					formBean.setColumnHierarchie(coll);
					formBean.setAddedColumns(temp);
				}
				if(str.equals("Step2DeleteRows"))
				{
					formBean.setColumnHierarchie(temp);
					formBean.setAddedColumns(coll);
				}
			
				if(str.equals("AddMeasure") == true)
				{
					formBean.setAddedMeasures(coll);
					formBean.setAmpMeasures(temp);
				}
				if(str.equals("DeleteMeasure"))
				{
					formBean.setAddedMeasures(temp);
					formBean.setAmpMeasures(coll);
				}
				if(str.equals("AddAdjustType") == true)
				{
					formBean.setSelAdjustType(coll);
					formBean.setAdjustType(temp);
				}
				if(str.equals("DeleteAdjustType") == true)
				{
					formBean.setSelAdjustType(temp);
					formBean.setAdjustType(coll);
				}
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace(System.out);
		}
	}// end of Function ...........
	
	
	private HashMap buildAmpTreeColumn(Collection formColumns)
	{
			ArrayList ampColumnsVisibles=new ArrayList();
			ServletContext ampContext;
			ampContext=this.getServlet().getServletContext();
			
			Collection ampAllFields= FeaturesUtil.getAMPFieldsVisibility();
			Collection allAmpColumns=formColumns;
			TreeSet ampThemes=new TreeSet();
			for(Iterator it=allAmpColumns.iterator();it.hasNext();)
			{
				AmpColumns ampColumn=(AmpColumns) it.next();
				for(Iterator jt=ampAllFields.iterator();jt.hasNext();)
				{
					AmpFieldsVisibility ampFieldVisibility=(AmpFieldsVisibility) jt.next();
					if(ampColumn.getColumnName().compareTo(ampFieldVisibility.getName())==0)
					{
						//if(ampFieldVisibility.isFieldActive(ampTreeVisibility))
						{
							AmpColumnsVisibility ampColumnVisibilityObj=new AmpColumnsVisibility();
							ampColumnVisibilityObj.setAmpColumn(ampColumn);
							ampColumnVisibilityObj.setAmpfield(ampFieldVisibility);
							ampColumnVisibilityObj.setParent((AmpFeaturesVisibility) ampFieldVisibility.getParent());
							ampColumnsVisibles.add(ampColumnVisibilityObj);
							ampThemes.add(ampFieldVisibility.getParent().getName());
							////System.out.println("xxxxxxxxxxxxxxx+:"+ampFieldVisibility.getName());
						}
					}
				}
			}
			HashMap ampTreeColumn=new HashMap();
			for(Iterator it=ampThemes.iterator();it.hasNext();)
			{
				String themeName=(String) it.next();
				ArrayList aux=new ArrayList();
				for(Iterator jt=ampColumnsVisibles.iterator();jt.hasNext();)
				{
					AmpColumnsVisibility acv=(AmpColumnsVisibility) jt.next();
					if(themeName.compareTo(acv.getParent().getName())==0)
						aux.add(acv);
					
				}
				ampTreeColumn.put(themeName, aux);
			}
			return ampTreeColumn;
	}

	private HashMap buildAmpTreeColumnSimple(Collection formColumns, String reportType)
	{
			ArrayList ampColumnsVisibles=new ArrayList();
			ServletContext ampContext;
			ampContext=this.getServlet().getServletContext();
			
			Collection ampAllFields= FeaturesUtil.getAMPFieldsVisibility();
			Collection allAmpColumns=formColumns;
			TreeSet ampThemes=new TreeSet();
			TreeSet ampThemesOrdered=new TreeSet();
			ArrayList ampColumnsOrder =(ArrayList) ampContext.getAttribute("ampColumnsOrder");
			for(Iterator it=allAmpColumns.iterator();it.hasNext();)
			{
				AmpColumns ampColumn=(AmpColumns) it.next();
				for(Iterator jt=ampAllFields.iterator();jt.hasNext();)
				{
					AmpFieldsVisibility ampFieldVisibility=(AmpFieldsVisibility) jt.next();
					if(ampColumn.getColumnName().compareTo(ampFieldVisibility.getName())==0)
					{
						if(ampFieldVisibility.isFieldActive(ampTreeVisibility))
						{
							AmpColumnsVisibility ampColumnVisibilityObj=new AmpColumnsVisibility();
							ampColumnVisibilityObj.setAmpColumn(ampColumn);
							ampColumnVisibilityObj.setAmpfield(ampFieldVisibility);
							ampColumnVisibilityObj.setParent((AmpFeaturesVisibility) ampFieldVisibility.getParent());
							ampColumnsVisibles.add(ampColumnVisibilityObj);
							ampThemes.add(ampFieldVisibility.getParent().getName());
							for(Iterator kt=ampColumnsOrder.iterator();kt.hasNext();)
							{
								AmpColumnsOrder aco=(AmpColumnsOrder) kt.next();
								if(ampFieldVisibility.getParent().getName().compareTo(aco.getColumnName())==0)
									ampThemesOrdered.add(aco);
							}
						}
					}
				}
			}
			LinkedHashMap ampTreeColumn=new LinkedHashMap();
			for(Iterator it=ampThemesOrdered.iterator();it.hasNext();)
			{
				AmpColumnsOrder aco=(AmpColumnsOrder) it.next();
				String themeName=(String) aco.getColumnName();
				ArrayList aux=new ArrayList();
				boolean added=false;
				for(Iterator jt=ampColumnsVisibles.iterator();jt.hasNext();)
				{
					AmpColumnsVisibility acv=(AmpColumnsVisibility) jt.next();
					if(themeName.compareTo(acv.getParent().getName())==0)
					{
						////donor contribution regional component
						if("donor".compareTo(reportType)==0)
						{
							aux.add(acv.getAmpColumn());
							added=true;
						}
						//the contribution report doesn't have access to columns 33-38 from amp_columns
						if("contribution".compareTo(reportType)==0 )
						{
							if(acv.getAmpColumn().getColumnId().intValue()<33 || acv.getAmpColumn().getColumnId().intValue()>38) 
								{
									aux.add(acv.getAmpColumn());
									added=true;
								}
						}
						//the regional report doesn't have access to columns 33-38 from amp_columns
						
						if("regional".compareTo(reportType)==0)
						{
							
							if((acv.getAmpColumn().getColumnId().intValue()<33 || acv.getAmpColumn().getColumnId().intValue()>38) && acv.getAmpColumn().getColumnId().intValue()!=5) 
							{
								aux.add(acv.getAmpColumn());
								added=true;
							}
						}
						
						if("component".compareTo(reportType)==0)
						{
							if(acv.getAmpColumn().getColumnId().intValue()!=5)
							{
								aux.add(acv.getAmpColumn());
								added=true;
							}
						}
					}
					
				}
				if(added) {
					
					ampTreeColumn.put(themeName, aux);
				}
			}
			
			for(Iterator it=ampThemes.iterator();it.hasNext();)
			{
				String themeName=(String) it.next();
				ArrayList aux=new ArrayList();
				boolean added=false;
				for(Iterator jt=ampColumnsVisibles.iterator();jt.hasNext();)
				{
					AmpColumnsVisibility acv=(AmpColumnsVisibility) jt.next();
					if(themeName.compareTo(acv.getParent().getName())==0)
					{
						////donor contribution regional component
						if("donor".compareTo(reportType)==0)
						{
							aux.add(acv.getAmpColumn());
							added=true;
						}
						//the contribution report doesn't have access to columns 33-38 from amp_columns
						if("contribution".compareTo(reportType)==0 )
						{
							if(acv.getAmpColumn().getColumnId().intValue()<33 || acv.getAmpColumn().getColumnId().intValue()>38) 
								{
									aux.add(acv.getAmpColumn());
									added=true;
								}
						}
						//the regional report doesn't have access to columns 33-38 from amp_columns
						
						if("regional".compareTo(reportType)==0)
						{
							
							if((acv.getAmpColumn().getColumnId().intValue()<33 || acv.getAmpColumn().getColumnId().intValue()>38) && acv.getAmpColumn().getColumnId().intValue()!=5) 
							{
								aux.add(acv.getAmpColumn());
								added=true;
							}
						}
						
						if("component".compareTo(reportType)==0)
						{
							if(acv.getAmpColumn().getColumnId().intValue()!=5)
							{
								aux.add(acv.getAmpColumn());
								added=true;
							}
						}
					}
					
				}
				if(added) {
					//System.out.println("-------------->"+themeName);
					ampTreeColumn.put(themeName, aux);
				}
				
			}
			return ampTreeColumn;
	}

	
	// Function to move Columns Up and Down
	
	private static void moveColumns(AdvancedReportForm formBean, String option)
	{
		Iterator iter= null;
		AmpColumns ampColumns = null;
		AmpMeasures ampMeasures  = null;
		Collection tempColl = new ArrayList();

		if(option.equals("MoveUp") || option.equals("MoveDown"))
		{
			tempColl = formBean.getAddedColumns();
			//logger.info("Step1 " + option);
		}
		if(option.equals("Step2MoveUp") || option.equals("Step2MoveDown"))
		{
			tempColl = formBean.getColumnHierarchie();
			//logger.info("Step2 " + option);
		}
		if(option.equals("MoveUpMeasure") || option.equals("MoveDownMeasure"))
		{
			tempColl = formBean.getAddedMeasures();
			//logger.info("Step3  : " + option);
		}
		if(option.equals("MoveUpAdjustType") || option.equals("MoveDownAdjustType"))
			tempColl = formBean.getSelAdjustType();
			
		if(tempColl.size() == 1)
			logger.info(" Cannot move field up.......");
		else
		{
			Long lg = new Long(formBean.getMoveColumn());
			ArrayList temp = new ArrayList();
			AmpColumns curr = null, prev = null , next = null;
			AmpMeasures currMeasure, prevMeasure, nextMeasure;
			int index = 0;
			
			temp.addAll(tempColl);
			iter = temp.iterator();		
			//logger.info(lg.toString() + " <<< Field Selected >>>> " + formBean.getMoveColumn() + "??????" + tempColl.size());
			while(iter.hasNext())
			{
				if(option.equals("MoveUpMeasure")== true || option.equals("MoveDownMeasure")== true
					|| option.equals("MoveUpAdjustType")== true || option.equals("MoveDownAdjustType")== true)
				{
					ampMeasures = (AmpMeasures) iter.next();
					if(option.equals("MoveUpMeasure")== true || option.equals("MoveUpAdjustType")== true)
					{
						if(lg.compareTo(ampMeasures.getMeasureId()) == 0 )
						{
							if(temp.indexOf(ampMeasures) > 0)
							{
								currMeasure = (AmpMeasures)temp.get(temp.indexOf(ampMeasures));
								prevMeasure = (AmpMeasures)temp.get(temp.indexOf(ampMeasures)-1);
								index = temp.indexOf(ampMeasures);
								temp.set(index, prevMeasure);
								temp.set(index-1, currMeasure);
								if(option.equals("MoveUpAdjustType"))
									formBean.setSelAdjustType(temp);
								if(option.equals("MoveUpMeasure"))
									formBean.setAddedMeasures(temp);
								
								break;
							}
						}
					}
					if(option.equals("MoveDownMeasure")== true  || option.equals("MoveDownAdjustType")== true )
					{
						if(lg.compareTo(ampMeasures.getMeasureId()) == 0 )
						{
							if( (temp.indexOf(ampMeasures)+1) < tempColl.size() )
							{
								currMeasure = (AmpMeasures)temp.get(temp.indexOf(ampMeasures));
								nextMeasure = (AmpMeasures)temp.get(temp.indexOf(ampMeasures)+1);
								index = temp.indexOf(ampMeasures);
								temp.set(index, nextMeasure);
								temp.set(index+1, currMeasure);
								if(option.equals("MoveDownAdjustType"))
									formBean.setSelAdjustType(temp);
								if(option.equals("MoveDownMeasure"))
									formBean.setAddedMeasures(temp);
								
								break;
							}
						}
					}

				}
				else
				{
					ampColumns = (AmpColumns) iter.next();
					
					if(option.compareTo("MoveUp") == 0 || option.compareTo("Step2MoveUp") == 0)
					{
						if(lg.compareTo(ampColumns.getColumnId()) == 0 )
						{
							if(temp.indexOf(ampColumns) > 0)
							{
								curr = (AmpColumns)temp.get(temp.indexOf(ampColumns));
								prev = (AmpColumns)temp.get(temp.indexOf(ampColumns)-1);
								index = temp.indexOf(ampColumns);
		
								temp.set(index, prev);
								temp.set(index-1, curr);
								if(option.equals("MoveUp"))
									formBean.setAddedColumns(temp);
								if(option.equals("Step2MoveUp"))
									formBean.setColumnHierarchie(temp);
								break;
							}
							else
							{
								//logger.info("Cannot  Swap.........");
								break;								
							}
						}
					} // This code moves the selected Column Up
					
					if(option.compareTo("MoveDown") == 0 || option.compareTo("Step2MoveDown") == 0)
					{
						if(lg.compareTo(ampColumns.getColumnId()) == 0 )
						{
							//logger.info(" Found : " + temp.indexOf(ampColumns));
							if( (temp.indexOf(ampColumns)+1) < tempColl.size())
							{
								curr = (AmpColumns)temp.get(temp.indexOf(ampColumns));
								next = (AmpColumns)temp.get(temp.indexOf(ampColumns)+1);
								index = temp.indexOf(ampColumns);
								temp.set(index, next);
								temp.set(index+1, curr);
								if(option.equals("MoveDown"))
									formBean.setAddedColumns(temp);
								if(option.equals("Step2MoveDown"))
									formBean.setColumnHierarchie(temp);
								
								break;
							}
							else
							{
								//logger.info("Cannot  Swap.........");
								break;								
							}
						}
					} // This code moves the selected Column Down
				}				
			}
		}
	}
	
}
*/
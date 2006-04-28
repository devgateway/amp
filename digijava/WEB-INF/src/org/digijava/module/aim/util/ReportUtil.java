/*
 * ReportUtil.java Created: 01-Apr-2005 
 */

package org.digijava.module.aim.util;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityInternalId;
import org.digijava.module.aim.dbentity.AmpActor;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpFilters;
import org.digijava.module.aim.dbentity.AmpMeasures;
import org.digijava.module.aim.dbentity.AmpModality;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpPages;
import org.digijava.module.aim.dbentity.AmpRegion;
import org.digijava.module.aim.dbentity.AmpReportCache;
import org.digijava.module.aim.dbentity.AmpReportColumn;
import org.digijava.module.aim.dbentity.AmpReportHierarchy;
import org.digijava.module.aim.dbentity.AmpReportSector;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpStatus;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTeamPageFilters;
import org.digijava.module.aim.dbentity.AmpTeamReports;
import org.digijava.module.aim.dbentity.AmpTermsAssist;
import org.digijava.module.aim.helper.AdvancedHierarchyReport;
import org.digijava.module.aim.helper.AdvancedReport;
import org.digijava.module.aim.helper.AmpByAssistTypeAmount;
import org.digijava.module.aim.helper.AmpByAssistTypeList;
import org.digijava.module.aim.helper.AmpDonors;
import org.digijava.module.aim.helper.AmpFund;
import org.digijava.module.aim.helper.AmpProjectBySector;
import org.digijava.module.aim.helper.AmpTeamDonors;
import org.digijava.module.aim.helper.Column;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.DecimalToText;
import org.digijava.module.aim.helper.EthiopianCalendar;
import org.digijava.module.aim.helper.FundTotal;
import org.digijava.module.aim.helper.Issues;
import org.digijava.module.aim.helper.Measures;
import org.digijava.module.aim.helper.Project;
import org.digijava.module.aim.helper.ProjectTermAssist;
import org.digijava.module.aim.helper.Report;
import org.digijava.module.aim.helper.ReportSelectionCriteria;
import org.digijava.module.aim.helper.TermFund;
import org.digijava.module.aim.helper.TermFundTotal;
import org.digijava.module.aim.helper.multiReport;
import org.digijava.module.editor.dbentity.Editor;



/**
 * Utility class for persisting all reports related entities
 * @author priyajith
 */
public class ReportUtil {
	
	private static DecimalFormat mf = new DecimalFormat("###,###,###,###,###") ;
	private static Logger logger = Logger.getLogger(ReportUtil.class);
	
	/**
	 * Removes the team-reports and member-reports association table.
	 * @param reportId	A Long array of the reports to be updated
	 * @param teamId  	The teamId of the team whose association with 
	 * 					the specified reports must be removed. When the 
	 * 					teams are dissociated with the reports, the association
	 * 					from the members of that team also gets removed.   
	 */
	public static void removeTeamReports(Long reportId[],Long teamId) {
		Session session = null;
		Transaction tx = null;
		
		if (reportId == null || reportId.length <= 0) return;
		
		try {
			session = PersistenceManager.getSession();
			tx = session.beginTransaction();
			
			String queryString = "select tm from "
				+ AmpTeamMember.class.getName()
				+ " tm where (tm.ampTeam=:teamId)";

			Query qry = session.createQuery(queryString);
			qry.setParameter("teamId", teamId, Hibernate.LONG);
			Collection col = qry.list();
			if (col != null && col.size() > 0) {
				for (int i = 0;i < reportId.length;i ++) {
					if (reportId[i] != null) {
						queryString = "select r from " 
							+ AmpReports.class.getName() 
							+ " r where (r.ampReportId=:repId)";
						qry = session.createQuery(queryString);
						qry.setParameter("repId",reportId[i],Hibernate.LONG);
						Iterator itr = qry.list().iterator();
						if (itr.hasNext()) {
							AmpReports ampReport = (AmpReports) itr.next();
							if (ampReport.getMembers() != null) {
								/*
								 * removing the team members association with the
								 * report
								 */
								ampReport.getMembers().removeAll(col);
								session.update(ampReport);
							}
						}
						
						/*
						 * removing the teams association with the report
						 */
						queryString = "select tr from " + AmpTeamReports.class.getName() 
							+ " tr where (tr.team=:teamId) and "
							+ " (tr.report=:repId)";
						qry = session.createQuery(queryString);
						qry.setParameter("teamId",teamId,Hibernate.LONG);
						qry.setParameter("repId",reportId[i],Hibernate.LONG);
						itr = qry.list().iterator();
						if (itr.hasNext()) {
							session.delete(itr.next());
						}
					}
				}
			}
			tx.commit();

		} catch (Exception e) {
			logger.error("Exception from updateMemberReports");
			logger.error(e.getMessage());
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception tex) {
					logger.error("Transaction rollback failed");
					logger.error(tex);
				}
			}
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception ex) {
					logger.error("Failed to release session");
				}
			}
		}
	}

	/**
	 * Associated the reports with the given team 
	 * @param reportId The Long array of reportIds which are to be associated 
	 * 				   with the given team
	 * @param teamId   The team id of the team to which the reports are to be 
	 * 				   assigned	
	 */
	public static void addTeamReports(Long reportId[],Long teamId) {
		Session session = null;
		Transaction tx = null;
		
		try {
			session = PersistenceManager.getSession();
			tx = session.beginTransaction();
			
			String queryString = "select tm from "
				+ AmpTeam.class.getName()
				+ " tm where (tm.ampTeamId=:teamId)";

			Query qry = session.createQuery(queryString);
			qry.setParameter("teamId", teamId, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			AmpTeam team = null;
			if (itr.hasNext()) {
				team = (AmpTeam) itr.next();
			}
			if (team != null) {
				if (reportId != null && reportId.length > 0) {
					queryString = "select rep from "
						+ AmpReports.class.getName() 
						+ " rep where rep.ampReportId in (";
					StringBuffer temp = new StringBuffer();
					for (int i = 0;i < reportId.length;i ++) {
						temp.append(reportId[i]);
						if ((i+1) != reportId.length) {
							temp.append(",");
						}
					}
					temp.append(")");
					queryString += temp;
					qry = session.createQuery(queryString);
					logger.debug("Query :" + qry.getQueryString());
					itr = qry.list().iterator();
					while (itr.hasNext()) {
						AmpReports report = (AmpReports) itr.next();
						if (report != null) {
							String tempQry = "select teamRep from "
								+ AmpTeamReports.class.getName()
								+ " teamRep where (teamRep.team=:tId) and "
								+ " (teamRep.report=:rId)";
							Query tmpQry = session.createQuery(tempQry);
							tmpQry.setParameter("tId",team.getAmpTeamId(),Hibernate.LONG);
							tmpQry.setParameter("rId",report.getAmpReportId(),Hibernate.LONG);
							Iterator tmpItr = tmpQry.list().iterator();
							if (!tmpItr.hasNext()) {
								AmpTeamReports tr = new AmpTeamReports();
								tr.setTeam(team);
								tr.setReport(report);
								tr.setTeamView(false);
								session.save(tr);								
							}
						}
					}
				}
			}
			tx.commit();
		} catch (Exception e) {
			logger.error("Exception from addTeamReports()");
			logger.error(e.getMessage());
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception rbf) {
					logger.error("Rollback failed");
					logger.error(rbf.getMessage());
				}
			}
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed");
					logger.error(rsf.getMessage());
				}
			}
		}
	}

	public static ArrayList getAmpReportByModality(Long ampTeamId,int fromYr,int toYr,String perspective,String ampCurrencyCode,Long ampModalityId,Long ampStatusId,Long ampDonorId,Long ampSectorId,int fiscalCalId,String startDate,String closeDate,String region)
	{
		Session session = null ;
		Query q = null ;
		ArrayList approvedActivityList=new ArrayList();
		ArrayList ampReports = new ArrayList() ;
		ArrayList donors = new ArrayList() ;
		ArrayList assistance = new ArrayList() ;
		String queryString = null;
		Iterator iter=null;
		Long All=new Long(0);
		int fiscalYear=0,fiscalQuarter=0;
		int yrCount = (toYr - fromYr) + 1;
//		double[][] commFund=new double[yrCount][4];
		double[][] projFund=new double[yrCount][4];
		double[][] totProjFund=new double[yrCount][4];
		double actualCommitment = 0.0 ;
		double actualDisbursement = 0.0 ;
		double commAmount = 0.0 ;
		double disbAmount = 0.0 ;
		double expAmount = 0.0 ;
		double plannedDisbAmount = 0.0 ;
		double toExchangeRate=1.0;
		double fromExchangeRate=1.0;
		double amount=0.0;
		int flag=0;
		int count=1;
		String inClause=null;
		Iterator iterSector=null;
		DecimalFormat mf = new DecimalFormat("###,###,###,###,###") ;
		ArrayList sectorId=new ArrayList();		
		ArrayList commDate=new ArrayList();		
		ArrayList modality=new ArrayList();		
		try
		{
			/* ampTeamId is id of the team to which the user belongs to. If the team has access 
			type 'management' then the following method returns collection of ids all its child teams
			with access type 'Team' */
			ArrayList dbReturnSet=(ArrayList)DbUtil.getAmpLevel0Teams(ampTeamId);				
			if(dbReturnSet.size()==0)
				inClause= "'" + ampTeamId + "'";	//if team access type
			else
			{
				iter=dbReturnSet.iterator();		//if management access type
				//inClause is computed to get activities of all the team ids which are in inClause.
				while(iter.hasNext())
				{
					Long teamId= (Long) iter.next();
					if(inClause==null)
						inClause="'" + teamId + "'";
					else
						inClause=inClause + ",'" + teamId + "'";	
				}
			}
//			logger.debug("Inclause: " + inClause);
/*			Reports should display only approved activities. This method returns collection of activity ids of all 
			the approved activities of all the teams in inClause.*/
			approvedActivityList=DbUtil.getApprovedActivities(inClause);
			session = PersistenceManager.getSession();
			if(startDate==null && closeDate==null)
				queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.ampTeamId in(" + inClause + ")) and (report.reportType='1') order by report.activityName,report.ampActivityId,report.donorName,report.fiscalYear,report.fiscalQuarter";
			else
				queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.ampTeamId in(" + inClause + ")) and (report.actualStartDate='" + startDate + "' or report.actualCompletionDate='" + closeDate + "') and (report.reportType='1') order by report.activityName,report.ampActivityId,report.donorName,report.fiscalYear,report.fiscalQuarter";
			logger.debug("querystring: " + queryString);
			q = session.createQuery(queryString);	
			Report report=null;
			if(q!=null)
			{
				iter = q.list().iterator();
				while(iter.hasNext())
				{
					AmpReportCache ampReportCache = (AmpReportCache) iter.next(); 

					if(approvedActivityList.indexOf(ampReportCache.getAmpActivityId())==-1)
						continue;

					if(new Long(fiscalCalId).equals(Constants.ETH_FY) || new Long(fiscalCalId).equals(Constants.ETH_CAL))
					{
						if(ampReportCache.getTransactionDate()!=null)
						{
							GregorianCalendar calendar = new GregorianCalendar();
							EthiopianCalendar ec=new EthiopianCalendar();
							EthiopianCalendar tempDate=new EthiopianCalendar();
							calendar.setTime(ampReportCache.getTransactionDate());
							ec=tempDate.getEthiopianDate(calendar);
							if(new Long(fiscalCalId).equals(Constants.ETH_FY))
							{
								fiscalYear=(int)ec.ethFiscalYear;
								fiscalQuarter=(int)ec.ethFiscalQrt;
							}
							if(new Long(fiscalCalId).equals(Constants.ETH_CAL))
							{
								fiscalYear=(int)ec.ethYear;
								fiscalQuarter=(int)ec.ethQtr;
							}
//							logger.debug("Ethiopian Fiscal Year: " + fiscalYear);
//							logger.debug("From Year: " + fromYr);
//							logger.debug("From Year: " + toYr);
/*							if(fiscalYear<fromYr || fiscalYear>toYr)
								continue;*/
						}
					}

					if(!ampModalityId.equals(All))
					{
						if(ampReportCache.getAmpModalityId()==null)
							continue;
						if(!(ampModalityId.equals(ampReportCache.getAmpModalityId())))
							continue;
					}

					if(!ampStatusId.equals(All))
					{
						if(ampReportCache.getAmpStatusId()==null)
							continue;
						if(!(ampStatusId.equals(ampReportCache.getAmpStatusId())))
							continue;
					}
	
					if(!ampDonorId.equals(All))
					{
						if(ampReportCache.getAmpDonorId()==null)
							continue;
						if(!(ampDonorId.equals(ampReportCache.getAmpDonorId())))
							continue;
					}
		
					if(!ampSectorId.equals(All))
					{
						int sflag=0;
						iterSector=DbUtil.getAmpReportSectorId(ampReportCache.getAmpActivityId()).iterator();
						while(iterSector.hasNext())
						{
							AmpReportSector sector=(AmpReportSector) iterSector.next();
							if(sector.getAmpSectorId().equals(ampSectorId))
							{
								sflag=1;
								break;
							}
							if(sector.getAmpSubSectorId().equals(new Long(0)))
							{
								if(new Long(sector.getSubSectorName()).equals(ampSectorId))
								{
									sflag=1;
									break;
								}
							}
							if(!(sector.getAmpSubSectorId().equals(new Long(0))) && sector.getAmpSubSectorId().equals(ampSectorId))
							{
								sflag=1;
								break;
							}
						}
						if (sflag==0)
						{
							continue;
						}
					}
					
					if(!region.equals("All"))
					{
						ArrayList location=(ArrayList)DbUtil.getAmpReportLocation(ampReportCache.getAmpActivityId());
						if(location.indexOf(region)==-1)
							continue;
					}
					
					if(report==null || !(report.getAmpActivityId().equals(ampReportCache.getAmpActivityId())))
					{
						if(report!=null)
						{
							for(int i=0;i<yrCount;i++)
							{
								AmpFund ampFund = new AmpFund();
								ampFund.setCommAmount(mf.format(projFund[i][0])); 
								ampFund.setDisbAmount(mf.format(projFund[i][1])); 
								ampFund.setExpAmount(mf.format(projFund[i][2]));	
								ampFund.setPlannedDisbAmount(mf.format(projFund[i][3])); 
								commAmount=commAmount+ projFund[i][0];
								disbAmount=disbAmount+ projFund[i][1];
								expAmount=expAmount+ projFund[i][2];
								plannedDisbAmount=plannedDisbAmount+ projFund[i][3];
								report.getAmpFund().add(ampFund) ;
							}	
							AmpFund ampFund = new AmpFund();
							ampFund.setCommAmount(mf.format(commAmount)); 
							ampFund.setDisbAmount(mf.format(disbAmount));
							ampFund.setExpAmount(mf.format(expAmount));
							ampFund.setPlannedDisbAmount(mf.format(plannedDisbAmount)); 
							ampFund.setUnDisbAmount(mf.format(actualCommitment - actualDisbursement));
							report.getAmpFund().add(ampFund) ;	
							report.setAcCommitment(mf.format(actualCommitment));
						
							if(donors.size()==0)
								report.getDonors().add("Unspecified");
							else
								report.getDonors().addAll(donors);
							if(assistance.size()==0)
								report.getAssistance().add("Unspecified");
							else
								report.getAssistance().addAll(assistance);

							if(commDate.size()==0)
								report.getCommitmentDate().add("Unspecified");
							else
								report.getCommitmentDate().addAll(commDate);

							if(modality.size()==0)
								report.getModality().add("Unspecified");
							else
								report.getModality().addAll(modality);
							ampReports.add(report);
							for(int i=0;i<yrCount;i++)
								projFund[i][0]=projFund[i][1]=projFund[i][2]=projFund[i][3]=0;
							donors.clear();
							assistance.clear();
							commDate.clear();
							modality.clear();
							actualCommitment=0.0;
							actualDisbursement=0.0;
							plannedDisbAmount=disbAmount=expAmount=commAmount=0.0;
						}
						report=new Report();
						report.setDonors(new ArrayList());
						report.setSectors(new ArrayList());
						report.setRegions(new ArrayList());
						report.setAmpFund(new ArrayList());
						report.setAssistance(new ArrayList());
						report.setModality(new ArrayList());
						report.setTitle(ampReportCache.getActivityName());
						report.setAmpActivityId(ampReportCache.getAmpActivityId());
						if(ampReportCache.getLevelName().equals("Not Exist"))
							report.setLevel("Unspecified");
						else
							report.setLevel(ampReportCache.getLevelName());
						if(ampReportCache.getStatusName()!=null)
							report.setStatus(ampReportCache.getStatusName());
						if(DbUtil.getAmpReportSector(ampReportCache.getAmpActivityId()).size()==0)
							report.getSectors().add("Unspecified");
						else
							report.getSectors().addAll(DbUtil.getAmpReportSector(ampReportCache.getAmpActivityId()));
						if(DbUtil.getAmpReportLocation(ampReportCache.getAmpActivityId()).size()==0)
							report.getRegions().add("Unspecified");
						else
							report.getRegions().addAll(DbUtil.getAmpReportLocation(ampReportCache.getAmpActivityId()));
						if(ampReportCache.getTermAssistName()!=null)
							assistance.add(ampReportCache.getTermAssistName());
						
						if(ampReportCache.getDonorName()!=null)
							donors.add(ampReportCache.getDonorName());
						else
							donors.addAll(DbUtil.getAmpDonorsForActivity(ampReportCache.getAmpActivityId()));
						
						if(ampReportCache.getModalityName()!=null)
							modality.add(ampReportCache.getModalityName());
						if(ampReportCache.getActualStartDate()!=null)
							report.setStartDate(DateConversion.ConvertDateToString(ampReportCache.getActualStartDate()));
						else
							report.setStartDate("");
						if(ampReportCache.getActualCompletionDate()!=null)
							report.setCloseDate(DateConversion.ConvertDateToString(ampReportCache.getActualCompletionDate()));
						else
							report.setCloseDate("");
						report.setCommitmentDate(new ArrayList());
						count++;
						
					}
					if(donors.indexOf(ampReportCache.getDonorName())==-1 && ampReportCache.getDonorName()!=null)
						donors.add(ampReportCache.getDonorName());

					if(modality.indexOf(ampReportCache.getModalityName())==-1 && ampReportCache.getModalityName()!=null)
						modality.add(ampReportCache.getModalityName());

					if(assistance.indexOf(ampReportCache.getTermAssistName())==-1 && ampReportCache.getTermAssistName()!=null)
						assistance.add(ampReportCache.getTermAssistName());
					
					
					if(ampReportCache.getFiscalYear()!=null && ampReportCache.getFiscalQuarter()!=null)
					{
						if(new Long(fiscalCalId).equals(Constants.GREGORIAN))
						{
							fiscalYear=ampReportCache.getFiscalYear().intValue();
							fiscalQuarter=ampReportCache.getFiscalQuarter().intValue();
						}
			
						if(ampReportCache.getCurrencyCode().equals("USD"))
							fromExchangeRate=1.0;
						else
							fromExchangeRate=DbUtil.getExchangeRate(ampReportCache.getCurrencyCode(),Constants.ACTUAL,ampReportCache.getTransactionDate());
						if(ampCurrencyCode.equals("USD"))
							toExchangeRate=1.0;
						else
							toExchangeRate=DbUtil.getExchangeRate(ampCurrencyCode,Constants.ACTUAL,ampReportCache.getTransactionDate());

						if(ampReportCache.getActualCommitment().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
						{
							amount=CurrencyWorker.convert1(ampReportCache.getActualCommitment().doubleValue(),fromExchangeRate,toExchangeRate);
							actualCommitment=actualCommitment + amount;
							if(commDate.indexOf(DateConversion.ConvertDateToString(ampReportCache.getTransactionDate()))==-1)
								commDate.add(DateConversion.ConvertDateToString(ampReportCache.getTransactionDate()));
							if(fiscalYear>=fromYr && fiscalYear<=toYr)
							{
								if(projFund[fiscalYear%fromYr][0]==0)
									projFund[fiscalYear%fromYr][0]=amount;
								else
								if(projFund[fiscalYear%fromYr][0]>0)
									projFund[fiscalYear%fromYr][0]=projFund[fiscalYear%fromYr][0] + amount;
							}
						}
						amount=0.0;
						
						if(ampReportCache.getActualDisbursement().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
						{
							amount=CurrencyWorker.convert1(ampReportCache.getActualDisbursement().doubleValue(),fromExchangeRate,toExchangeRate);
							actualDisbursement=actualDisbursement + amount;
							if(fiscalYear>=fromYr && fiscalYear<=toYr)
							{
								if(projFund[fiscalYear%fromYr][1]==0)
									projFund[fiscalYear%fromYr][1]=amount;
								else
								if(projFund[fiscalYear%fromYr][1]>0)
									projFund[fiscalYear%fromYr][1]=projFund[fiscalYear%fromYr][1] + amount;
							}
						}
						amount=0.0;
								
						if(ampReportCache.getActualExpenditure().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
						{
							amount=CurrencyWorker.convert1(ampReportCache.getActualExpenditure().doubleValue(),fromExchangeRate,toExchangeRate);
							if(fiscalYear>=fromYr && fiscalYear<=toYr)
							{
								if(projFund[fiscalYear%fromYr][2]==0)
									projFund[fiscalYear%fromYr][2]=amount;
								else
								if(projFund[fiscalYear%fromYr][2]>0)
									projFund[fiscalYear%fromYr][2]=projFund[fiscalYear%fromYr][2] + amount;
							}
						}
						amount=0.0;
						
						if(ampReportCache.getCurrencyCode().equals("USD"))
							fromExchangeRate=1.0;
						else
							fromExchangeRate=DbUtil.getExchangeRate(ampReportCache.getCurrencyCode(),Constants.PLANNED,ampReportCache.getTransactionDate());
						if(ampCurrencyCode.equals("USD"))
							toExchangeRate=1.0;
						else
							toExchangeRate=DbUtil.getExchangeRate(ampCurrencyCode,Constants.PLANNED,ampReportCache.getTransactionDate());
						
						if(ampReportCache.getPlannedDisbursement().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
						{
							amount=CurrencyWorker.convert1(ampReportCache.getPlannedDisbursement().doubleValue(),fromExchangeRate,toExchangeRate);
							if(fiscalYear>=fromYr && fiscalYear<=toYr)
							{
								if(projFund[fiscalYear%fromYr][3]==0)
									projFund[fiscalYear%fromYr][3]=amount;
								else
								if(projFund[fiscalYear%fromYr][3]>0)
									projFund[fiscalYear%fromYr][3]=projFund[fiscalYear%fromYr][3] + amount;
							}
						}
						amount=0.0;
					
					}
				}
			
				for(int i=0;i<yrCount;i++)
				{
					AmpFund ampFund = new AmpFund();
					ampFund.setCommAmount(mf.format(projFund[i][0])); 
					ampFund.setDisbAmount(mf.format(projFund[i][1])); 
					ampFund.setExpAmount(mf.format(projFund[i][2]));	
					ampFund.setPlannedDisbAmount(mf.format(projFund[i][3])); 
					commAmount=commAmount+ projFund[i][0];
					disbAmount=disbAmount+ projFund[i][1];
					expAmount=expAmount+ projFund[i][2];
					plannedDisbAmount=plannedDisbAmount+ projFund[i][3];
					report.getAmpFund().add(ampFund) ;
				}	
				AmpFund ampFund = new AmpFund();
				ampFund.setCommAmount(mf.format(commAmount)); 
				ampFund.setDisbAmount(mf.format(disbAmount));
				ampFund.setExpAmount(mf.format(expAmount));
				ampFund.setPlannedDisbAmount(mf.format(plannedDisbAmount)); 
				ampFund.setUnDisbAmount(mf.format(actualCommitment - actualDisbursement));
				report.getAmpFund().add(ampFund) ;	
				report.setAcCommitment(mf.format(actualCommitment));
						
				if(donors.size()==0)
					report.getDonors().add("Unspecified");
				else
					report.getDonors().addAll(donors);
				if(assistance.size()==0)
					report.getAssistance().add("Unspecified");
				else
					report.getAssistance().addAll(assistance);

				if(commDate.size()==0)
					report.getCommitmentDate().add("Unspecified");
				else
					report.getCommitmentDate().addAll(commDate);

				if(modality.size()==0)
					report.getModality().add("Unspecified");
				else
					report.getModality().addAll(modality);
				ampReports.add(report);
			}	
		}
		catch(Exception ex) 		
		{
			logger.debug("Unable to get report names  from database " + ex.getMessage());
		}
		finally 
		{
			try 
			{
				PersistenceManager.releaseSession(session);
			}
			catch (Exception ex2) 
			{
				logger.debug("releaseSession() failed ");
			}
		}
		return ampReports ;
	}
	
	public static ArrayList getAmpReportBySectorByProject(Long ampTeamId,int fromYr,int toYr,String perspective,String ampCurrencyCode,Long ampModalityId,Long ampStatusId,Long ampDonorId,Long ampSectorId,int fiscalCalId,String startDate,String closeDate,String region)
	{
		Session session = null ;
		Query q = null ;
		Query qry=null;
		ArrayList ampReports = new ArrayList() ;
		ArrayList sectorAssistance = new ArrayList() ;
		ArrayList totalAssistance = new ArrayList() ;
		ArrayList approvedActivityList=new ArrayList();
		String queryString = null;
		Long All=new Long(0);
		Iterator iter=null;
		Long ampStrId=null;
		int yrCount = (toYr - fromYr)+1;
		int fiscalYear=0,fiscalQuarter=0;
		double[][] termFunds=new double[yrCount][4];
		double[][] sectorFunds=new double[yrCount][4];
		double[][] totalFunds=new double[yrCount][4];
		double[][] projFunds=new double[yrCount][4];
		double[][] loanSectorFunds=new double[(yrCount+1)][4];
		double[][] grantSectorFunds=new double[(yrCount+1)][4];
		double[][] kindSectorFunds=new double[(yrCount+1)][4];
		double[][] loanTotalFunds=new double[(yrCount+1)][4];
		double[][] grantTotalFunds=new double[(yrCount+1)][4];
		double[][] kindTotalFunds=new double[(yrCount+1)][4];
	//	double[][] expFund=new double[yrCount][4];
	//	double[][] totFund=new double[yrCount][3];
		double totComm = 0.0 ;
		double totPlannedDisb = 0.0 ;
		double totDisb = 0.0 ;
		double totExp = 0.0;
		double projCommAmount=0.0;
		double projPlannedDisbAmount=0.0;
		double projDisbAmount=0.0;
		double projExpAmount=0.0;
		double sectorCommAmount=0.0;
		double sectorPlannedDisbAmount=0.0;
		double sectorDisbAmount=0.0;
		double sectorExpAmount=0.0;
		double totalCommAmount=0.0;
		double totalPlannedDisbAmount=0.0;
		double totalDisbAmount=0.0;
		double totalExpAmount=0.0;
		int newSector=0;
		int count=1;
		int termFlag=0;
		double toExchangeRate=1.0;
		double fromExchangeRate=0.0;
		double amount=0.0;
		int donorCount=0;
		int projCount=0;
		String inClause=null;
		String inClauseTeam=null;
		Iterator iterActivity=null;
		String termAssistName=null;
		DecimalFormat mf = new DecimalFormat("###,###,###,###,###") ;
						
		try
		{
			ArrayList dbReturnSet=(ArrayList)DbUtil.getAmpLevel0Teams(ampTeamId);				
			if(dbReturnSet.size()==0)
				inClauseTeam= "'" + ampTeamId + "'";
			else
			{
				iter=dbReturnSet.iterator();
				while(iter.hasNext())
				{
					Long teamId= (Long) iter.next();
					if(inClauseTeam==null)
						inClauseTeam="'" + teamId + "'";
					else
						inClauseTeam=inClauseTeam + ",'" + teamId + "'";
				}
			}
			logger.debug("Inclause: " + inClauseTeam);
			approvedActivityList=DbUtil.getApprovedActivities(inClauseTeam);
			ArrayList sectorId=null;
			session = PersistenceManager.getSession();
			multiReport report =null;
			AmpTeamDonors ampTeamDonors =null;
			Project project=null;
			ProjectTermAssist termAssist=null;
			TermFund sectorTermAssist=null;
			logger.debug("Before dbUtil");
			//logger.debug("Sector Size: " + DbUtil.getAmpReportSectors(ampTeamId,fromYr,toYr,perspective).size());
			iter = DbUtil.getAmpReportSectors(inClauseTeam).iterator();
			while(iter.hasNext())
			{
//				AmpReportSector ampReportSector = (AmpReportSector) iter.next(); 
				AmpProjectBySector ampProjectBySector=(AmpProjectBySector) iter.next();
				if(!ampSectorId.equals(All))
				{
					AmpSector ampSector=DbUtil.getAmpParentSector(ampSectorId);
					if(!(ampProjectBySector.getSector().getAmpSectorId().equals(ampSector.getAmpSectorId())))
						continue;
				}
				if(report!=null)
				{ 
					logger.debug(report.getSector());
					logger.debug("if report not null");
					if(!(ampTeamDonors.getDonorAgency().equals("Unspecified")))
					{
						logger.debug("Inside Donor");
						if(termFlag==1)
						{
							termAssist=new ProjectTermAssist();
							termAssist.setTermAssistName(termAssistName);
							if(sectorAssistance.indexOf(termAssist.getTermAssistName())==-1)
							{
								logger.debug("Assistance Added:" + termAssist.getTermAssistName() + ":");
								sectorAssistance.add(termAssist.getTermAssistName());
							}
							termAssist.setTermAssistFund(new ArrayList());
							for(int i=0;i<yrCount;i++)
							{
								AmpFund termFund = new AmpFund();
								termFund.setCommAmount(mf.format(termFunds[i][0])); 
								termFund.setDisbAmount(mf.format(termFunds[i][1]));
								termFund.setExpAmount(mf.format(termFunds[i][2]));
								termFund.setPlannedDisbAmount(mf.format(termFunds[i][3])); 
								termAssist.getTermAssistFund().add(termFund);
								totComm=totComm + termFunds[i][0];
								totDisb=totDisb + termFunds[i][1];
								totExp=totExp + termFunds[i][2];
								totPlannedDisb=totPlannedDisb + termFunds[i][3];
							}
							termAssist.setTermCommAmount(mf.format(totComm));
							termAssist.setTermDisbAmount(mf.format(totDisb));
							termAssist.setTermExpAmount(mf.format(totExp));
							termAssist.setTermPlannedDisbAmount(mf.format(totPlannedDisb));
							termAssist.setTermUnDisbAmount(mf.format(totComm - totDisb));
							projCommAmount=projCommAmount + totComm;
							projDisbAmount=projDisbAmount + totDisb;
							projExpAmount=projExpAmount + totExp;
							projPlannedDisbAmount=projPlannedDisbAmount + totPlannedDisb;
							project.getTermAssist().add(termAssist);
							logger.debug("Term Assist:" + termAssist.getTermAssistName());
					
							for(int i=0;i<=yrCount;i++)
							{
								if(termAssist.getTermAssistName().equals("Loan"))
								{
									if(i<yrCount)
									{
										loanSectorFunds[i][0]=loanSectorFunds[i][0] + termFunds[i][0];
										loanSectorFunds[i][1]=loanSectorFunds[i][1] + termFunds[i][1];
										loanSectorFunds[i][2]=loanSectorFunds[i][2] + termFunds[i][2];
										loanSectorFunds[i][3]=loanSectorFunds[i][3] + termFunds[i][3];
										loanTotalFunds[i][0]=loanTotalFunds[i][0] + termFunds[i][0];
										loanTotalFunds[i][1]=loanTotalFunds[i][1] + termFunds[i][1];
										loanTotalFunds[i][2]=loanTotalFunds[i][2] + termFunds[i][2];
										loanTotalFunds[i][3]=loanTotalFunds[i][3] + termFunds[i][3];
									}
									if(i==yrCount)
									{
										loanSectorFunds[i][0]=loanSectorFunds[i][0] + totComm;
										loanSectorFunds[i][1]=loanSectorFunds[i][1] + totDisb;
										loanSectorFunds[i][2]=loanSectorFunds[i][2] + totExp;
										loanSectorFunds[i][3]=loanSectorFunds[i][3] + totPlannedDisb;
										loanTotalFunds[i][0]=loanTotalFunds[i][0] + totComm;
										loanTotalFunds[i][1]=loanTotalFunds[i][1] + totDisb;
										loanTotalFunds[i][2]=loanTotalFunds[i][2] + totExp;
										loanTotalFunds[i][3]=loanTotalFunds[i][3] + totPlannedDisb;

									}
								}
								if(termAssist.getTermAssistName().equals("Grant"))
								{
									if(i<yrCount)
									{
										grantSectorFunds[i][0]=grantSectorFunds[i][0] + termFunds[i][0];
										grantSectorFunds[i][1]=grantSectorFunds[i][1] + termFunds[i][1];
										grantSectorFunds[i][2]=grantSectorFunds[i][2] + termFunds[i][2];
										grantSectorFunds[i][3]=grantSectorFunds[i][3] + termFunds[i][3];
										grantTotalFunds[i][0]=grantTotalFunds[i][0] + termFunds[i][0];
										grantTotalFunds[i][1]=grantTotalFunds[i][1] + termFunds[i][1];
										grantTotalFunds[i][2]=grantTotalFunds[i][2] + termFunds[i][2];
										grantTotalFunds[i][3]=grantTotalFunds[i][3] + termFunds[i][3];
									}
									if(i==yrCount)
									{
										grantSectorFunds[i][0]=grantSectorFunds[i][0] + totComm;
										grantSectorFunds[i][1]=grantSectorFunds[i][1] + totDisb;
										grantSectorFunds[i][2]=grantSectorFunds[i][2] + totExp;
										grantSectorFunds[i][3]=grantSectorFunds[i][3] + totPlannedDisb;
										grantTotalFunds[i][0]=grantTotalFunds[i][0] + totComm;
										grantTotalFunds[i][1]=grantTotalFunds[i][1] + totDisb;
										grantTotalFunds[i][2]=grantTotalFunds[i][2] + totExp;
										grantTotalFunds[i][3]=grantTotalFunds[i][3] + totPlannedDisb;
									}
								}
								if(termAssist.getTermAssistName().equals("In Kind"))
								{
									if(i<yrCount)
									{
										kindSectorFunds[i][0]=kindSectorFunds[i][0] + termFunds[i][0];
										kindSectorFunds[i][1]=kindSectorFunds[i][1] + termFunds[i][1];
										kindSectorFunds[i][2]=kindSectorFunds[i][2] + termFunds[i][2];
										kindSectorFunds[i][3]=kindSectorFunds[i][3] + termFunds[i][3];
										kindTotalFunds[i][0]=kindTotalFunds[i][0] + termFunds[i][0];
										kindTotalFunds[i][1]=kindTotalFunds[i][1] + termFunds[i][1];
										kindTotalFunds[i][2]=kindTotalFunds[i][2] + termFunds[i][2];
										kindTotalFunds[i][3]=kindTotalFunds[i][3] + termFunds[i][3];
									}
									if(i==yrCount)
									{
										kindSectorFunds[i][0]=kindSectorFunds[i][0] + totComm;
										kindSectorFunds[i][1]=kindSectorFunds[i][1] + totDisb;
										kindSectorFunds[i][2]=kindSectorFunds[i][2] + totExp;
										kindSectorFunds[i][3]=kindSectorFunds[i][3] + totPlannedDisb;
										kindTotalFunds[i][0]=kindTotalFunds[i][0] + totComm;
										kindTotalFunds[i][1]=kindTotalFunds[i][1] + totDisb;
										kindTotalFunds[i][2]=kindTotalFunds[i][2] + totExp;
										kindTotalFunds[i][3]=kindTotalFunds[i][3] + totPlannedDisb;
									}
								}
							}
							logger.debug("After term assist");
							for(int i=0;i<yrCount;i++)
							{
								projFunds[i][0]=projFunds[i][0] + termFunds[i][0];
								projFunds[i][1]=projFunds[i][1] + termFunds[i][1];
								projFunds[i][2]=projFunds[i][2] + termFunds[i][2];
								projFunds[i][3]=projFunds[i][3] + termFunds[i][3];
							}
						}
						for(int i=0;i<yrCount;i++)
						{
							AmpFund projFund = new AmpFund();
							projFund.setCommAmount(mf.format(projFunds[i][0])); 
							projFund.setDisbAmount(mf.format(projFunds[i][1]));
							projFund.setExpAmount(mf.format(projFunds[i][2]));
							projFund.setPlannedDisbAmount(mf.format(projFunds[i][3])); 
							project.getAmpFund().add(projFund) ;
						}
						project.setProjCommAmount(mf.format(projCommAmount));
						project.setProjDisbAmount(mf.format(projDisbAmount));
						project.setProjExpAmount(mf.format(projExpAmount));
						project.setProjPlannedDisbAmount(mf.format(projPlannedDisbAmount));
						project.setProjUnDisbAmount(mf.format(projCommAmount - projDisbAmount));
						project.setRowspan(project.getTermAssist().size()+1);
						ampTeamDonors.getProject().add(project);
						for(int i=0;i<yrCount;i++)
						{
							sectorFunds[i][0]=sectorFunds[i][0] + projFunds[i][0];
							sectorFunds[i][1]=sectorFunds[i][1] + projFunds[i][1];
							sectorFunds[i][2]=sectorFunds[i][2] + projFunds[i][2];
							sectorFunds[i][3]=sectorFunds[i][3] + projFunds[i][3];
						}
						for(int i=0;i<yrCount;i++)
						{
							totalFunds[i][0]=totalFunds[i][0] + sectorFunds[i][0];
							totalFunds[i][1]=totalFunds[i][1] + sectorFunds[i][1];
							totalFunds[i][2]=totalFunds[i][2] + sectorFunds[i][2];
							totalFunds[i][3]=totalFunds[i][3] + sectorFunds[i][3];
						}
						sectorCommAmount=sectorCommAmount + projCommAmount;
						sectorDisbAmount=sectorDisbAmount + projDisbAmount;
						sectorExpAmount=sectorExpAmount + projExpAmount;
						sectorPlannedDisbAmount=sectorPlannedDisbAmount + projPlannedDisbAmount;
						totalCommAmount=totalCommAmount + sectorCommAmount;
						totalDisbAmount=totalDisbAmount + sectorDisbAmount;
						totalExpAmount=totalExpAmount + sectorExpAmount;
						totalPlannedDisbAmount=totalPlannedDisbAmount + sectorPlannedDisbAmount;
					}
					logger.debug("Size of Assistance:" + sectorAssistance.size() + ":");
					for(int i=0;i<yrCount;i++)
					{
						FundTotal total=new FundTotal();
						total.setTotCommAmount(mf.format(sectorFunds[i][0]));
						total.setTotDisbAmount(mf.format(sectorFunds[i][1]));
						total.setTotExpAmount(mf.format(sectorFunds[i][2]));
						total.setTotPlannedDisbAmount(mf.format(sectorFunds[i][3]));
						report.getTotalSectorFund().add(total);	
					}
					report.setSectorCommAmount(mf.format(sectorCommAmount));
					report.setSectorDisbAmount(mf.format(sectorDisbAmount));
					report.setSectorExpAmount(mf.format(sectorExpAmount));
					report.setSectorPlannedDisbAmount(mf.format(sectorPlannedDisbAmount));
					report.setSectorUnDisbAmount(mf.format(sectorCommAmount - sectorDisbAmount));
					Iterator termIter=sectorAssistance.iterator();
					while(termIter.hasNext())
					{
						String assist=(String) termIter.next();
//						logger.debug("Assist:" + assist + ":");
						TermFund termFund=new TermFund();
						if(assist.equals("Loan"))
						{
							termFund.setTermAssistName("Loan");
							termFund.setTermFundTotal(new ArrayList());
							for(int i=0;i<yrCount;i++)
							{
								TermFundTotal termFundTotal=new TermFundTotal();
								termFundTotal.setTotCommAmount(mf.format(loanSectorFunds[i][0]));
								termFundTotal.setTotDisbAmount(mf.format(loanSectorFunds[i][1]));
								termFundTotal.setTotExpAmount(mf.format(loanSectorFunds[i][2]));
								termFundTotal.setTotPlannedDisbAmount(mf.format(loanSectorFunds[i][3]));
								termFund.getTermFundTotal().add(termFundTotal);
							}
							termFund.setTotDonorCommAmount(mf.format(loanSectorFunds[yrCount][0]));
							termFund.setTotDonorDisbAmount(mf.format(loanSectorFunds[yrCount][1]));
							termFund.setTotDonorExpAmount(mf.format(loanSectorFunds[yrCount][2]));
							termFund.setTotDonorPlannedDisbAmount(mf.format(loanSectorFunds[yrCount][3]));
							termFund.setTotDonorUnDisbAmount(mf.format(loanSectorFunds[yrCount][0] - loanSectorFunds[yrCount][1]));
						}
						if(assist.equals("Grant"))
						{
//							logger.debug("Inside Grant");
//							logger.debug("Year Count" + yrCount);
							termFund.setTermAssistName("Grant");
							termFund.setTermFundTotal(new ArrayList());
							for(int i=0;i<yrCount;i++)
							{
								TermFundTotal termFundTotal=new TermFundTotal();
								termFundTotal.setTotCommAmount(mf.format(grantSectorFunds[i][0]));
								termFundTotal.setTotDisbAmount(mf.format(grantSectorFunds[i][1]));
								termFundTotal.setTotExpAmount(mf.format(grantSectorFunds[i][2]));
								termFundTotal.setTotPlannedDisbAmount(mf.format(grantSectorFunds[i][3]));
								termFund.getTermFundTotal().add(termFundTotal);
							}
							termFund.setTotDonorCommAmount(mf.format(grantSectorFunds[yrCount][0]));
							termFund.setTotDonorDisbAmount(mf.format(grantSectorFunds[yrCount][1]));
							termFund.setTotDonorExpAmount(mf.format(grantSectorFunds[yrCount][2]));
							termFund.setTotDonorPlannedDisbAmount(mf.format(grantSectorFunds[yrCount][3]));
							termFund.setTotDonorUnDisbAmount(mf.format(grantSectorFunds[yrCount][0] - grantSectorFunds[yrCount][1]));
						}
						if(assist.equals("In Kind"))
						{
							termFund.setTermAssistName("In Kind");
							termFund.setTermFundTotal(new ArrayList());
							for(int i=0;i<yrCount;i++)
							{
								TermFundTotal termFundTotal=new TermFundTotal();
								termFundTotal.setTotCommAmount(mf.format(kindSectorFunds[i][0]));
								termFundTotal.setTotDisbAmount(mf.format(kindSectorFunds[i][1]));
								termFundTotal.setTotExpAmount(mf.format(kindSectorFunds[i][2]));
								termFundTotal.setTotPlannedDisbAmount(mf.format(kindSectorFunds[i][3]));
								termFund.getTermFundTotal().add(termFundTotal);
							}
							termFund.setTotDonorCommAmount(mf.format(kindSectorFunds[yrCount][0]));
							termFund.setTotDonorDisbAmount(mf.format(kindSectorFunds[yrCount][1]));
							termFund.setTotDonorExpAmount(mf.format(kindSectorFunds[yrCount][2]));
							termFund.setTotDonorPlannedDisbAmount(mf.format(kindSectorFunds[yrCount][3]));
							termFund.setTotDonorUnDisbAmount(mf.format(kindSectorFunds[yrCount][0] - kindSectorFunds[yrCount][1]));
						}
//						logger.debug("Added:'" + termFund.getTermAssistName());
						report.getTotalSectorTermAssistFund().add(termFund);	
					}
					report.getDonors().add(ampTeamDonors);
					projPlannedDisbAmount=projDisbAmount=projExpAmount=projCommAmount=0;
					sectorPlannedDisbAmount=sectorDisbAmount=sectorExpAmount=sectorCommAmount=0;
					totPlannedDisb=totDisb=totExp=totComm=0;
					for(int i=0;i<yrCount;i++)
					{
						termFunds[i][0]=termFunds[i][1]=termFunds[i][2]=termFunds[i][3]=0;
						projFunds[i][0]=projFunds[i][1]=projFunds[i][2]=projFunds[i][3]=0;
						sectorFunds[i][0]=sectorFunds[i][1]=sectorFunds[i][2]=sectorFunds[i][3]=0;
						loanSectorFunds[i][0]=loanSectorFunds[i][1]=loanSectorFunds[i][2]=loanSectorFunds[i][3]=0;
						grantSectorFunds[i][0]=grantSectorFunds[i][1]=grantSectorFunds[i][2]=grantSectorFunds[i][3]=0;
						kindSectorFunds[i][0]=kindSectorFunds[i][1]=kindSectorFunds[i][2]=kindSectorFunds[i][3]=0;
					}
					loanSectorFunds[yrCount][0]=loanSectorFunds[yrCount][1]=loanSectorFunds[yrCount][2]=loanSectorFunds[yrCount][3]=0;
					grantSectorFunds[yrCount][0]=grantSectorFunds[yrCount][1]=grantSectorFunds[yrCount][2]=grantSectorFunds[yrCount][3]=0;
					kindSectorFunds[yrCount][0]=kindSectorFunds[yrCount][1]=kindSectorFunds[yrCount][2]=kindSectorFunds[yrCount][3]=0;
					projCount=0;
					sectorAssistance.clear();
					ampReports.add(report);
					report=null;
				}
		//		logger.debug("Activity Size: " + ampProjectBySector.getAmpActivityId().size());
				Iterator iterSector=ampProjectBySector.getAmpActivityId().iterator();
				while(iterSector.hasNext())
				{
					Long id = (Long) iterSector.next();
					if(inClause==null)
						inClause="'" + id + "'";
					else
						inClause=inClause + ",'" + id + "'";
		//			logger.debug("In Clause: " + inClause);
				}
				if(startDate==null && closeDate==null)
					queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.ampTeamId in(" + inClauseTeam + ")) and (report.ampActivityId in(" + inClause + ")) and (report.reportType='1') order by report.donorName,report.activityName,report.ampActivityId";
				else
					queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.ampTeamId in(" + inClauseTeam + ")) and (report.actualStartDate='" + startDate + "' or report.actualCompletionDate='" + closeDate + "') and (report.ampActivityId in(" + inClause + ")) and (report.reportType='1') order by report.donorName,report.activityName,report.ampActivityId";
				logger.debug("querystring: " + queryString);
				qry = session.createQuery(queryString);
//				qry.setParameter("ampTeamId",ampTeamId,Hibernate.LONG) ;
				iterActivity=qry.list().iterator();
				inClause=null;
				while(iterActivity.hasNext())
				{
					AmpReportCache ampReportCache = (AmpReportCache) iterActivity.next();
				/*	if(!(ampReportCache.getAmpTeamId().equals(ampTeamId)) || ampReportCache.getFiscalYear().intValue()<fromYr || ampReportCache.getFiscalYear().intValue()>toYr)		
						continue;*/
					
					if(approvedActivityList.indexOf(ampReportCache.getAmpActivityId())==-1)
						continue;

					if(new Long(fiscalCalId).equals(Constants.ETH_FY) || new Long(fiscalCalId).equals(Constants.ETH_CAL))
					{
						if(ampReportCache.getTransactionDate()!=null)
						{
							GregorianCalendar calendar = new GregorianCalendar();
							EthiopianCalendar ec=new EthiopianCalendar();
							EthiopianCalendar tempDate=new EthiopianCalendar();
							calendar.setTime(ampReportCache.getTransactionDate());
							ec=tempDate.getEthiopianDate(calendar);
							if(new Long(fiscalCalId).equals(Constants.ETH_FY))
							{
								fiscalYear=(int)ec.ethFiscalYear;
								fiscalQuarter=(int)ec.ethFiscalQrt;
							}
							if(new Long(fiscalCalId).equals(Constants.ETH_CAL))
							{
								fiscalYear=(int)ec.ethYear;
								fiscalQuarter=(int)ec.ethQtr;
							}
		//					logger.debug("Ethiopian Fiscal Year: " + fiscalYear);
		//					logger.debug("From Year: " + fromYr);
		//					logger.debug("From Year: " + toYr);
/*							if(fiscalYear<fromYr || fiscalYear>toYr)
								continue;*/
						}
					}

					if(!ampModalityId.equals(All))
					{
						if(ampReportCache.getAmpModalityId()==null)
							continue;
						if(!(ampModalityId.equals(ampReportCache.getAmpModalityId())))
							continue;
					}

					if(!ampStatusId.equals(All))
					{
						if(ampReportCache.getAmpStatusId()==null)
							continue;
						if(!(ampStatusId.equals(ampReportCache.getAmpStatusId())))
							continue;
					}
	
					if(!ampDonorId.equals(All))
					{
						if(ampReportCache.getAmpDonorId()==null)
							continue;
						if(!(ampDonorId.equals(ampReportCache.getAmpDonorId())))
							continue;
					}

					if(!ampSectorId.equals(All))
					{
						int sflag=0;
		//				logger.debug("Selected Sector Id: " + ampSectorId);
						iterSector=DbUtil.getAmpReportSectorId(ampReportCache.getAmpActivityId()).iterator();
						while(iterSector.hasNext())
						{
							AmpReportSector sector=(AmpReportSector) iterSector.next();
							if(sector.getAmpSectorId().equals(ampSectorId) || new Long(sector.getSubSectorName()).equals(ampSectorId))
							{
		//						logger.debug("Condition true");
								sflag=1;
								break;
							}
						}
						if (sflag==0)
						{
							continue;
						}
					}
					
					if(!region.equals("All"))
					{
						ArrayList location=(ArrayList)DbUtil.getAmpReportLocation(ampReportCache.getAmpActivityId());
						if(location.indexOf(region)==-1)
							continue;
					}

					if(report==null)
					{
						report = new multiReport();
						report.setSector(ampProjectBySector.getSector().getSectorName());
						logger.debug("Sector: " + ampProjectBySector.getSector().getSectorName());
						report.setDonors(new ArrayList());
						report.setTotalSectorFund(new ArrayList());
						report.setTotalSectorTermAssistFund(new ArrayList());
						report.setTotalTeamFund(new ArrayList());
						report.setTotalTeamTermAssistFund(new ArrayList());
						report.setYearCount(yrCount*3);
						report.setCount((yrCount*3+4));
						ampTeamDonors=new AmpTeamDonors();
						ampTeamDonors.setDonorCount(++donorCount);
						ampTeamDonors.setProject(new ArrayList());
						if(ampReportCache.getAmpFundingId()==null && ampReportCache.getAmpDonorId()==null)
							ampTeamDonors.setDonorAgency("Unspecified");
						else
							ampTeamDonors.setDonorAgency(ampReportCache.getDonorName());
						if(ampReportCache.getDonorName()!=null)
						{	
							project=new Project();
							project.setCount(++projCount);
							project.setName(ampReportCache.getActivityName());
							project.setAmpActivityId(ampReportCache.getAmpActivityId());
							project.setAmpFund(new ArrayList());
							project.setTermAssist(new ArrayList());
							termAssistName=ampReportCache.getTermAssistName();
							termFlag=0;
						}
					}	
					
					if(ampReportCache.getAmpFundingId()==null && ampReportCache.getAmpDonorId()==null)
					{
						project=new Project();
						project.setCount(++projCount);
						project.setName(ampReportCache.getActivityName());
						project.setAmpActivityId(ampReportCache.getAmpActivityId());
						project.setAmpFund(new ArrayList());
						for(int i=0;i<yrCount;i++)
						{
							AmpFund ampFund = new AmpFund();
							ampFund.setCommAmount(mf.format(0)); 
							ampFund.setDisbAmount(mf.format(0));
							ampFund.setExpAmount(mf.format(0));
							ampFund.setPlannedDisbAmount(mf.format(0)); 
							project.getAmpFund().add(ampFund) ;
						}
						project.setProjCommAmount(mf.format(0));
						project.setProjDisbAmount(mf.format(0));
						project.setProjExpAmount(mf.format(0));
						project.setProjPlannedDisbAmount(mf.format(0));
						project.setProjUnDisbAmount(mf.format(0));
						ampTeamDonors.getProject().add(project);
						
					}
					else
					if(ampReportCache.getAmpFundingId()!=null && !(ampReportCache.getDonorName().equals(ampTeamDonors.getDonorAgency())))
					{
						logger.debug("Inside Donor");
						logger.debug("Donor: " + ampTeamDonors.getDonorAgency());
						if(!(ampTeamDonors.getDonorAgency().equals("Unspecified")))
						{
							if(termFlag==1)
							{
								termAssist=new ProjectTermAssist();
								termAssist.setTermAssistName(termAssistName);
								if(sectorAssistance.indexOf(termAssist.getTermAssistName())==-1)
									sectorAssistance.add(termAssist.getTermAssistName());
								if(totalAssistance.indexOf(termAssist.getTermAssistName())==-1)
									totalAssistance.add(termAssist.getTermAssistName());
								termAssist.setTermAssistFund(new ArrayList());
								for(int i=0;i<yrCount;i++)
								{
									AmpFund termFund = new AmpFund();
									termFund.setCommAmount(mf.format(termFunds[i][0])); 
									termFund.setDisbAmount(mf.format(termFunds[i][1]));
									termFund.setExpAmount(mf.format(termFunds[i][2]));
									termFund.setPlannedDisbAmount(mf.format(termFunds[i][3])); 
									termAssist.getTermAssistFund().add(termFund);
									totComm=totComm + termFunds[i][0];
									totDisb=totDisb + termFunds[i][1];
									totExp=totExp + termFunds[i][2];
									totPlannedDisb=totPlannedDisb + termFunds[i][3];
								}
								termAssist.setTermCommAmount(mf.format(totComm));
								termAssist.setTermDisbAmount(mf.format(totDisb));
								termAssist.setTermExpAmount(mf.format(totExp));
								termAssist.setTermPlannedDisbAmount(mf.format(totPlannedDisb));
								termAssist.setTermUnDisbAmount(mf.format(totComm - totDisb));
								projCommAmount=projCommAmount + totComm;
								projDisbAmount=projDisbAmount + totDisb;
								projExpAmount=projExpAmount + totExp;
								projPlannedDisbAmount=projPlannedDisbAmount + totPlannedDisb;
								project.getTermAssist().add(termAssist);
		//						logger.debug("Term Assist:" + termAssist.getTermAssistName());
						
							for(int i=0;i<=yrCount;i++)
							{
								if(termAssist.getTermAssistName().equals("Loan"))
								{
									if(i<yrCount)
									{
										loanSectorFunds[i][0]=loanSectorFunds[i][0] + termFunds[i][0];
										loanSectorFunds[i][1]=loanSectorFunds[i][1] + termFunds[i][1];
										loanSectorFunds[i][2]=loanSectorFunds[i][2] + termFunds[i][2];
										loanSectorFunds[i][3]=loanSectorFunds[i][3] + termFunds[i][3];
										loanTotalFunds[i][0]=loanTotalFunds[i][0] + termFunds[i][0];
										loanTotalFunds[i][1]=loanTotalFunds[i][1] + termFunds[i][1];
										loanTotalFunds[i][2]=loanTotalFunds[i][2] + termFunds[i][2];
										loanTotalFunds[i][3]=loanTotalFunds[i][3] + termFunds[i][3];
									}
									if(i==yrCount)
									{
										loanSectorFunds[i][0]=loanSectorFunds[i][0] + totComm;
										loanSectorFunds[i][1]=loanSectorFunds[i][1] + totDisb;
										loanSectorFunds[i][2]=loanSectorFunds[i][2] + totExp;
										loanSectorFunds[i][3]=loanSectorFunds[i][3] + totPlannedDisb;
										loanTotalFunds[i][0]=loanTotalFunds[i][0] + totComm;
										loanTotalFunds[i][1]=loanTotalFunds[i][1] + totDisb;
										loanTotalFunds[i][2]=loanTotalFunds[i][2] + totExp;
										loanTotalFunds[i][3]=loanTotalFunds[i][3] + totPlannedDisb;

									}
								}
								if(termAssist.getTermAssistName().equals("Grant"))
								{
									if(i<yrCount)
									{
										grantSectorFunds[i][0]=grantSectorFunds[i][0] + termFunds[i][0];
										grantSectorFunds[i][1]=grantSectorFunds[i][1] + termFunds[i][1];
										grantSectorFunds[i][2]=grantSectorFunds[i][2] + termFunds[i][2];
										grantSectorFunds[i][3]=grantSectorFunds[i][3] + termFunds[i][3];
										grantTotalFunds[i][0]=grantTotalFunds[i][0] + termFunds[i][0];
										grantTotalFunds[i][1]=grantTotalFunds[i][1] + termFunds[i][1];
										grantTotalFunds[i][2]=grantTotalFunds[i][2] + termFunds[i][2];
										grantTotalFunds[i][3]=grantTotalFunds[i][3] + termFunds[i][3];
									}
									if(i==yrCount)
									{
										grantSectorFunds[i][0]=grantSectorFunds[i][0] + totComm;
										grantSectorFunds[i][1]=grantSectorFunds[i][1] + totDisb;
										grantSectorFunds[i][2]=grantSectorFunds[i][2] + totExp;
										grantSectorFunds[i][3]=grantSectorFunds[i][3] + totPlannedDisb;
										grantTotalFunds[i][0]=grantTotalFunds[i][0] + totComm;
										grantTotalFunds[i][1]=grantTotalFunds[i][1] + totDisb;
										grantTotalFunds[i][2]=grantTotalFunds[i][2] + totExp;
										grantTotalFunds[i][3]=grantTotalFunds[i][3] + totPlannedDisb;
									}
								}
								if(termAssist.getTermAssistName().equals("In Kind"))
								{
									if(i<yrCount)
									{
										kindSectorFunds[i][0]=kindSectorFunds[i][0] + termFunds[i][0];
										kindSectorFunds[i][1]=kindSectorFunds[i][1] + termFunds[i][1];
										kindSectorFunds[i][2]=kindSectorFunds[i][2] + termFunds[i][2];
										kindSectorFunds[i][3]=kindSectorFunds[i][3] + termFunds[i][3];
										kindTotalFunds[i][0]=kindTotalFunds[i][0] + termFunds[i][0];
										kindTotalFunds[i][1]=kindTotalFunds[i][1] + termFunds[i][1];
										kindTotalFunds[i][2]=kindTotalFunds[i][2] + termFunds[i][2];
										kindTotalFunds[i][3]=kindTotalFunds[i][3] + termFunds[i][3];
									}
									if(i==yrCount)
									{
										kindSectorFunds[i][0]=kindSectorFunds[i][0] + totComm;
										kindSectorFunds[i][1]=kindSectorFunds[i][1] + totDisb;
										kindSectorFunds[i][2]=kindSectorFunds[i][2] + totExp;
										kindSectorFunds[i][3]=kindSectorFunds[i][3] + totPlannedDisb;
										kindTotalFunds[i][0]=kindTotalFunds[i][0] + totComm;
										kindTotalFunds[i][1]=kindTotalFunds[i][1] + totDisb;
										kindTotalFunds[i][2]=kindTotalFunds[i][2] + totExp;
										kindTotalFunds[i][3]=kindTotalFunds[i][3] + totPlannedDisb;
									}
								}
							}
		//						logger.debug("After term assist");
								for(int i=0;i<yrCount;i++)
								{
									projFunds[i][0]=projFunds[i][0] + termFunds[i][0];
									projFunds[i][1]=projFunds[i][1] + termFunds[i][1];
									projFunds[i][2]=projFunds[i][2] + termFunds[i][2];
									projFunds[i][3]=projFunds[i][3] + termFunds[i][3];
								}
							}
							for(int i=0;i<yrCount;i++)
							{
								AmpFund projFund = new AmpFund();
								projFund.setCommAmount(mf.format(projFunds[i][0])); 
								projFund.setDisbAmount(mf.format(projFunds[i][1]));
								projFund.setExpAmount(mf.format(projFunds[i][2]));
								projFund.setPlannedDisbAmount(mf.format(projFunds[i][3])); 
								project.getAmpFund().add(projFund) ;
							}
							project.setProjCommAmount(mf.format(projCommAmount));
							project.setProjDisbAmount(mf.format(projDisbAmount));
							project.setProjExpAmount(mf.format(projExpAmount));
							project.setProjPlannedDisbAmount(mf.format(projPlannedDisbAmount));
							project.setProjUnDisbAmount(mf.format(projCommAmount - projDisbAmount));
							project.setRowspan(project.getTermAssist().size()+1);
							ampTeamDonors.getProject().add(project);
							for(int i=0;i<yrCount;i++)
							{
								sectorFunds[i][0]=sectorFunds[i][0] + projFunds[i][0];
								sectorFunds[i][1]=sectorFunds[i][1] + projFunds[i][1];
								sectorFunds[i][2]=sectorFunds[i][2] + projFunds[i][2];
								sectorFunds[i][3]=sectorFunds[i][3] + projFunds[i][3];
							}
							sectorCommAmount=sectorCommAmount + projCommAmount;
							sectorDisbAmount=sectorDisbAmount + projDisbAmount;
							sectorExpAmount=sectorExpAmount + projExpAmount;
							sectorPlannedDisbAmount=sectorPlannedDisbAmount + projPlannedDisbAmount;
						}
						report.getDonors().add(ampTeamDonors);
						projPlannedDisbAmount=projDisbAmount=projExpAmount=projCommAmount=0;
						totPlannedDisb=totDisb=totExp=totComm=0;
						for(int i=0;i<yrCount;i++)
						{
							termFunds[i][0]=termFunds[i][1]=termFunds[i][2]=termFunds[i][3]=0;
							projFunds[i][0]=projFunds[i][1]=projFunds[i][2]=projFunds[i][3]=0;
						}
						projCount=0;
						ampTeamDonors=new AmpTeamDonors();
						ampTeamDonors.setDonorAgency(ampReportCache.getDonorName());
						ampTeamDonors.setDonorCount(++donorCount);
						ampTeamDonors.setTotalDonorFund(new ArrayList());
						ampTeamDonors.setTotalDonorTermAssistFund(new ArrayList());
						ampTeamDonors.setProject(new ArrayList());
						project=new Project();
						project.setName(ampReportCache.getActivityName());
						project.setAmpActivityId(ampReportCache.getAmpActivityId());
						project.setCount(++projCount);
						project.setAmpFund(new ArrayList());
						project.setTermAssist(new ArrayList());
						termAssistName=ampReportCache.getTermAssistName();
						termFlag=0;
			
						logger.debug("Outside Donor");
						
					}
					else
					if(ampReportCache.getAmpFundingId()!=null && ampReportCache.getDonorName().equals(ampTeamDonors.getDonorAgency()) && !(ampReportCache.getAmpActivityId().equals(project.getAmpActivityId())))
					{
						logger.debug("Inside Project");
						if(termFlag==1)
						{
							termAssist=new ProjectTermAssist();
							termAssist.setTermAssistName(termAssistName);
							if(sectorAssistance.indexOf(termAssist.getTermAssistName())==-1)
								sectorAssistance.add(termAssist.getTermAssistName());
							if(totalAssistance.indexOf(termAssist.getTermAssistName())==-1)
								totalAssistance.add(termAssist.getTermAssistName());
							termAssist.setTermAssistFund(new ArrayList());
							for(int i=0;i<yrCount;i++)
							{
								AmpFund termFund = new AmpFund();
								termFund.setCommAmount(mf.format(termFunds[i][0])); 
								termFund.setDisbAmount(mf.format(termFunds[i][1]));
								termFund.setExpAmount(mf.format(termFunds[i][2]));
								termFund.setPlannedDisbAmount(mf.format(termFunds[i][3])); 
								termAssist.getTermAssistFund().add(termFund);
								totComm=totComm + termFunds[i][0];
								totDisb=totDisb + termFunds[i][1];
								totExp=totExp + termFunds[i][2];
								totPlannedDisb=totPlannedDisb + termFunds[i][3];
							}
							termAssist.setTermCommAmount(mf.format(totComm));
							termAssist.setTermDisbAmount(mf.format(totDisb));
							termAssist.setTermExpAmount(mf.format(totExp));
							termAssist.setTermPlannedDisbAmount(mf.format(totPlannedDisb));
							termAssist.setTermUnDisbAmount(mf.format(totComm - totDisb));
							projCommAmount=projCommAmount + totComm;
							projDisbAmount=projDisbAmount + totDisb;
							projExpAmount=projExpAmount + totExp;
							projPlannedDisbAmount=projPlannedDisbAmount + totPlannedDisb;
							project.getTermAssist().add(termAssist);
	//						logger.debug("Term Assist:" + termAssist.getTermAssistName());
					
							for(int i=0;i<=yrCount;i++)
							{
								if(termAssist.getTermAssistName().equals("Loan"))
								{
									if(i<yrCount)
									{
										loanSectorFunds[i][0]=loanSectorFunds[i][0] + termFunds[i][0];
										loanSectorFunds[i][1]=loanSectorFunds[i][1] + termFunds[i][1];
										loanSectorFunds[i][2]=loanSectorFunds[i][2] + termFunds[i][2];
										loanSectorFunds[i][3]=loanSectorFunds[i][3] + termFunds[i][3];
										loanTotalFunds[i][0]=loanTotalFunds[i][0] + termFunds[i][0];
										loanTotalFunds[i][1]=loanTotalFunds[i][1] + termFunds[i][1];
										loanTotalFunds[i][2]=loanTotalFunds[i][2] + termFunds[i][2];
										loanTotalFunds[i][3]=loanTotalFunds[i][3] + termFunds[i][3];
									}
									if(i==yrCount)
									{
										loanSectorFunds[i][0]=loanSectorFunds[i][0] + totComm;
										loanSectorFunds[i][1]=loanSectorFunds[i][1] + totDisb;
										loanSectorFunds[i][2]=loanSectorFunds[i][2] + totExp;
										loanSectorFunds[i][3]=loanSectorFunds[i][3] + totPlannedDisb;
										loanTotalFunds[i][0]=loanTotalFunds[i][0] + totComm;
										loanTotalFunds[i][1]=loanTotalFunds[i][1] + totDisb;
										loanTotalFunds[i][2]=loanTotalFunds[i][2] + totExp;
										loanTotalFunds[i][3]=loanTotalFunds[i][3] + totPlannedDisb;

									}
								}
								if(termAssist.getTermAssistName().equals("Grant"))
								{
									if(i<yrCount)
									{
										grantSectorFunds[i][0]=grantSectorFunds[i][0] + termFunds[i][0];
										grantSectorFunds[i][1]=grantSectorFunds[i][1] + termFunds[i][1];
										grantSectorFunds[i][2]=grantSectorFunds[i][2] + termFunds[i][2];
										grantSectorFunds[i][3]=grantSectorFunds[i][3] + termFunds[i][3];
										grantTotalFunds[i][0]=grantTotalFunds[i][0] + termFunds[i][0];
										grantTotalFunds[i][1]=grantTotalFunds[i][1] + termFunds[i][1];
										grantTotalFunds[i][2]=grantTotalFunds[i][2] + termFunds[i][2];
										grantTotalFunds[i][3]=grantTotalFunds[i][3] + termFunds[i][3];
									}
									if(i==yrCount)
									{
										grantSectorFunds[i][0]=grantSectorFunds[i][0] + totComm;
										grantSectorFunds[i][1]=grantSectorFunds[i][1] + totDisb;
										grantSectorFunds[i][2]=grantSectorFunds[i][2] + totExp;
										grantSectorFunds[i][3]=grantSectorFunds[i][3] + totPlannedDisb;
										grantTotalFunds[i][0]=grantTotalFunds[i][0] + totComm;
										grantTotalFunds[i][1]=grantTotalFunds[i][1] + totDisb;
										grantTotalFunds[i][2]=grantTotalFunds[i][2] + totExp;
										grantTotalFunds[i][3]=grantTotalFunds[i][3] + totPlannedDisb;
									}
								}
								if(termAssist.getTermAssistName().equals("In Kind"))
								{
									if(i<yrCount)
									{
										kindSectorFunds[i][0]=kindSectorFunds[i][0] + termFunds[i][0];
										kindSectorFunds[i][1]=kindSectorFunds[i][1] + termFunds[i][1];
										kindSectorFunds[i][2]=kindSectorFunds[i][2] + termFunds[i][2];
										kindSectorFunds[i][3]=kindSectorFunds[i][3] + termFunds[i][3];
										kindTotalFunds[i][0]=kindTotalFunds[i][0] + termFunds[i][0];
										kindTotalFunds[i][1]=kindTotalFunds[i][1] + termFunds[i][1];
										kindTotalFunds[i][2]=kindTotalFunds[i][2] + termFunds[i][2];
										kindTotalFunds[i][3]=kindTotalFunds[i][3] + termFunds[i][3];
									}
									if(i==yrCount)
									{
										kindSectorFunds[i][0]=kindSectorFunds[i][0] + totComm;
										kindSectorFunds[i][1]=kindSectorFunds[i][1] + totDisb;
										kindSectorFunds[i][2]=kindSectorFunds[i][2] + totExp;
										kindSectorFunds[i][3]=kindSectorFunds[i][3] + totPlannedDisb;
										kindTotalFunds[i][0]=kindTotalFunds[i][0] + totComm;
										kindTotalFunds[i][1]=kindTotalFunds[i][1] + totDisb;
										kindTotalFunds[i][2]=kindTotalFunds[i][2] + totExp;
										kindTotalFunds[i][3]=kindTotalFunds[i][3] + totPlannedDisb;
									}
								}
							}
	//						logger.debug("After term assist");
							for(int i=0;i<yrCount;i++)
							{
								projFunds[i][0]=projFunds[i][0] + termFunds[i][0];
								projFunds[i][1]=projFunds[i][1] + termFunds[i][1];
								projFunds[i][2]=projFunds[i][2] + termFunds[i][2];
								projFunds[i][3]=projFunds[i][3] + termFunds[i][3];
							}
						}
						for(int i=0;i<yrCount;i++)
						{
							AmpFund projFund = new AmpFund();
							projFund.setCommAmount(mf.format(projFunds[i][0])); 
							projFund.setDisbAmount(mf.format(projFunds[i][1]));
							projFund.setExpAmount(mf.format(projFunds[i][2]));
							projFund.setPlannedDisbAmount(mf.format(projFunds[i][3])); 
							project.getAmpFund().add(projFund) ;
						}
						project.setProjCommAmount(mf.format(projCommAmount));
						project.setProjDisbAmount(mf.format(projDisbAmount));
						project.setProjExpAmount(mf.format(projExpAmount));
						project.setProjPlannedDisbAmount(mf.format(projPlannedDisbAmount));
						project.setProjUnDisbAmount(mf.format(projCommAmount - projDisbAmount));
						project.setRowspan(project.getTermAssist().size()+1);
						ampTeamDonors.getProject().add(project);
						for(int i=0;i<yrCount;i++)
						{
							sectorFunds[i][0]=sectorFunds[i][0] + projFunds[i][0];
							sectorFunds[i][1]=sectorFunds[i][1] + projFunds[i][1];
							sectorFunds[i][2]=sectorFunds[i][2] + projFunds[i][2];
							sectorFunds[i][3]=sectorFunds[i][3] + projFunds[i][3];
						}
						sectorCommAmount=sectorCommAmount + projCommAmount;
						sectorDisbAmount=sectorDisbAmount + projDisbAmount;
						sectorExpAmount=sectorExpAmount + projExpAmount;
						sectorPlannedDisbAmount=sectorPlannedDisbAmount + projPlannedDisbAmount;
						projPlannedDisbAmount=projDisbAmount=projExpAmount=projCommAmount=0;
						totPlannedDisb=totDisb=totExp=totComm=0;
						for(int i=0;i<yrCount;i++)
						{
							termFunds[i][0]=termFunds[i][1]=termFunds[i][2]=termFunds[i][3]=0;
							projFunds[i][0]=projFunds[i][1]=projFunds[i][2]=projFunds[i][3]=0;
						}
						project=new Project();
						project.setName(ampReportCache.getActivityName());
						logger.debug("activity name: " + project.getName());
						project.setAmpActivityId(ampReportCache.getAmpActivityId());
						project.setCount(++projCount);
						project.setAmpFund(new ArrayList());
						project.setTermAssist(new ArrayList());
						termAssistName=ampReportCache.getTermAssistName();
						termFlag=0;
						logger.debug("Outside Project");
					}
					else
					if(ampReportCache.getAmpFundingId()!=null && ampReportCache.getDonorName().equals(ampTeamDonors.getDonorAgency()) && ampReportCache.getAmpActivityId().equals(project.getAmpActivityId()) && !(ampReportCache.getTermAssistName().equals(termAssistName)))
					{
						logger.debug("Inside Terms");
						if(termFlag==1)
						{
							termAssist=new ProjectTermAssist();
							termAssist.setTermAssistName(termAssistName);
							if(sectorAssistance.indexOf(termAssist.getTermAssistName())==-1)
								sectorAssistance.add(termAssist.getTermAssistName());
							if(totalAssistance.indexOf(termAssist.getTermAssistName())==-1)
								totalAssistance.add(termAssist.getTermAssistName());
							termAssist.setTermAssistFund(new ArrayList());
							for(int i=0;i<yrCount;i++)
							{
								AmpFund termFund = new AmpFund();
								termFund.setCommAmount(mf.format(termFunds[i][0]));
								termFund.setDisbAmount(mf.format(termFunds[i][1]));
								termFund.setExpAmount(mf.format(termFunds[i][2]));
								termFund.setPlannedDisbAmount(mf.format(termFunds[i][3])); 
								termAssist.getTermAssistFund().add(termFund);
								totComm=totComm + termFunds[i][0];
								totDisb=totDisb + termFunds[i][1];
								totExp=totExp + termFunds[i][2];
								totPlannedDisb=totPlannedDisb + termFunds[i][3];
							}
							termAssist.setTermCommAmount(mf.format(totComm));
							termAssist.setTermDisbAmount(mf.format(totDisb));
							termAssist.setTermExpAmount(mf.format(totExp));
							termAssist.setTermPlannedDisbAmount(mf.format(totPlannedDisb));
							termAssist.setTermUnDisbAmount(mf.format(totComm - totDisb));
							projCommAmount=projCommAmount + totComm;
							projDisbAmount=projDisbAmount + totDisb;
							projExpAmount=projExpAmount + totExp;
							projPlannedDisbAmount=projPlannedDisbAmount + totPlannedDisb;
							project.getTermAssist().add(termAssist);
		//						logger.debug("Term Assist:" + termAssist.getTermAssistName());
						
							for(int i=0;i<=yrCount;i++)
							{
								if(termAssist.getTermAssistName().equals("Loan"))
								{
									if(i<yrCount)
									{
										loanSectorFunds[i][0]=loanSectorFunds[i][0] + termFunds[i][0];
										loanSectorFunds[i][1]=loanSectorFunds[i][1] + termFunds[i][1];
										loanSectorFunds[i][2]=loanSectorFunds[i][2] + termFunds[i][2];
										loanSectorFunds[i][3]=loanSectorFunds[i][3] + termFunds[i][3];
										loanTotalFunds[i][0]=loanTotalFunds[i][0] + termFunds[i][0];
										loanTotalFunds[i][1]=loanTotalFunds[i][1] + termFunds[i][1];
										loanTotalFunds[i][2]=loanTotalFunds[i][2] + termFunds[i][2];
										loanTotalFunds[i][3]=loanTotalFunds[i][3] + termFunds[i][3];
									}
									if(i==yrCount)
									{
										loanSectorFunds[i][0]=loanSectorFunds[i][0] + totComm;
										loanSectorFunds[i][1]=loanSectorFunds[i][1] + totDisb;
										loanSectorFunds[i][2]=loanSectorFunds[i][2] + totExp;
										loanSectorFunds[i][3]=loanSectorFunds[i][3] + totPlannedDisb;
										loanTotalFunds[i][0]=loanTotalFunds[i][0] + totComm;
										loanTotalFunds[i][1]=loanTotalFunds[i][1] + totDisb;
										loanTotalFunds[i][2]=loanTotalFunds[i][2] + totExp;
										loanTotalFunds[i][3]=loanTotalFunds[i][3] + totPlannedDisb;

									}
								}
								if(termAssist.getTermAssistName().equals("Grant"))
								{
									if(i<yrCount)
									{
										grantSectorFunds[i][0]=grantSectorFunds[i][0] + termFunds[i][0];
										grantSectorFunds[i][1]=grantSectorFunds[i][1] + termFunds[i][1];
										grantSectorFunds[i][2]=grantSectorFunds[i][2] + termFunds[i][2];
										grantSectorFunds[i][3]=grantSectorFunds[i][3] + termFunds[i][3];
										grantTotalFunds[i][0]=grantTotalFunds[i][0] + termFunds[i][0];
										grantTotalFunds[i][1]=grantTotalFunds[i][1] + termFunds[i][1];
										grantTotalFunds[i][2]=grantTotalFunds[i][2] + termFunds[i][2];
										grantTotalFunds[i][3]=grantTotalFunds[i][3] + termFunds[i][3];
									}
									if(i==yrCount)
									{
										grantSectorFunds[i][0]=grantSectorFunds[i][0] + totComm;
										grantSectorFunds[i][1]=grantSectorFunds[i][1] + totDisb;
										grantSectorFunds[i][2]=grantSectorFunds[i][2] + totExp;
										grantSectorFunds[i][3]=grantSectorFunds[i][3] + totPlannedDisb;
										grantTotalFunds[i][0]=grantTotalFunds[i][0] + totComm;
										grantTotalFunds[i][1]=grantTotalFunds[i][1] + totDisb;
										grantTotalFunds[i][2]=grantTotalFunds[i][2] + totExp;
										grantTotalFunds[i][3]=grantTotalFunds[i][3] + totPlannedDisb;
									}
								}
								if(termAssist.getTermAssistName().equals("In Kind"))
								{
									if(i<yrCount)
									{
										kindSectorFunds[i][0]=kindSectorFunds[i][0] + termFunds[i][0];
										kindSectorFunds[i][1]=kindSectorFunds[i][1] + termFunds[i][1];
										kindSectorFunds[i][2]=kindSectorFunds[i][2] + termFunds[i][2];
										kindSectorFunds[i][3]=kindSectorFunds[i][3] + termFunds[i][3];
										kindTotalFunds[i][0]=kindTotalFunds[i][0] + termFunds[i][0];
										kindTotalFunds[i][1]=kindTotalFunds[i][1] + termFunds[i][1];
										kindTotalFunds[i][2]=kindTotalFunds[i][2] + termFunds[i][2];
										kindTotalFunds[i][3]=kindTotalFunds[i][3] + termFunds[i][3];
									}
									if(i==yrCount)
									{
										kindSectorFunds[i][0]=kindSectorFunds[i][0] + totComm;
										kindSectorFunds[i][1]=kindSectorFunds[i][1] + totDisb;
										kindSectorFunds[i][2]=kindSectorFunds[i][2] + totExp;
										kindSectorFunds[i][3]=kindSectorFunds[i][3] + totPlannedDisb;
										kindTotalFunds[i][0]=kindTotalFunds[i][0] + totComm;
										kindTotalFunds[i][1]=kindTotalFunds[i][1] + totDisb;
										kindTotalFunds[i][2]=kindTotalFunds[i][2] + totExp;
										kindTotalFunds[i][3]=kindTotalFunds[i][3] + totPlannedDisb;
									}
								}
							}
	//						logger.debug("After term assist");
							for(int i=0;i<yrCount;i++)
							{
								projFunds[i][0]=projFunds[i][0] + termFunds[i][0];
								projFunds[i][1]=projFunds[i][1] + termFunds[i][1];
								projFunds[i][2]=projFunds[i][2] + termFunds[i][2];
								projFunds[i][3]=projFunds[i][3] + termFunds[i][3];
							}
						}
						termAssistName=ampReportCache.getTermAssistName();
						termFlag=0;
						for(int i=0;i<yrCount;i++)
							termFunds[i][0]=termFunds[i][1]=termFunds[i][2]=termFunds[i][3]=0;
						totPlannedDisb=totDisb=totExp=totComm=0;
						logger.debug("Outside Terms");
					}
					logger.debug("Fiscal Year: " + ampReportCache.getFiscalYear());
					logger.debug("Fiscal Quarter: " + ampReportCache.getFiscalQuarter());
					if(ampReportCache.getFiscalYear()!=null && ampReportCache.getFiscalQuarter()!=null)
					{
						if(new Long(fiscalCalId).equals(Constants.GREGORIAN))
						{
							fiscalYear=ampReportCache.getFiscalYear().intValue();
							fiscalQuarter=ampReportCache.getFiscalQuarter().intValue();
						}
						
						if(ampReportCache.getCurrencyCode().equals("USD"))
							fromExchangeRate=1.0;
						else
							fromExchangeRate=DbUtil.getExchangeRate(ampReportCache.getCurrencyCode(),Constants.ACTUAL,ampReportCache.getTransactionDate());
						if(ampCurrencyCode.equals("USD"))
							toExchangeRate=1.0;
						else
							toExchangeRate=DbUtil.getExchangeRate(ampCurrencyCode,Constants.ACTUAL,ampReportCache.getTransactionDate());
						
						if(ampReportCache.getActualCommitment().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
						{
							amount=CurrencyWorker.convert1(ampReportCache.getActualCommitment().doubleValue(),fromExchangeRate,toExchangeRate);
							if(fiscalYear>=fromYr && fiscalYear<=toYr)
							{
								termFlag=1;
								if(termFunds[fiscalYear%fromYr][0]==0)
									termFunds[fiscalYear%fromYr][0]=amount;
								else
								if(termFunds[fiscalYear%fromYr][0]>0)
									termFunds[fiscalYear%fromYr][0]=termFunds[fiscalYear%fromYr][0] + amount;
							}
						}
						amount=0.0;

						if(ampReportCache.getActualDisbursement().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
						{
							
							amount=CurrencyWorker.convert1(ampReportCache.getActualDisbursement().doubleValue(),fromExchangeRate,toExchangeRate);
							if(fiscalYear>=fromYr && fiscalYear<=toYr)
							{
								termFlag=1;
								if(termFunds[fiscalYear%fromYr][1]==0)
									termFunds[fiscalYear%fromYr][1]=amount;
								else
								if(termFunds[fiscalYear%fromYr][1]>0)
									termFunds[fiscalYear%fromYr][1]=termFunds[fiscalYear%fromYr][1] + amount;
							}
						}
						amount=0.0;

						if(ampReportCache.getActualExpenditure().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
						{
							amount=CurrencyWorker.convert1(ampReportCache.getActualExpenditure().doubleValue(),fromExchangeRate,toExchangeRate);
							if(fiscalYear>=fromYr && fiscalYear<=toYr)
							{
								termFlag=1;
								if(termFunds[fiscalYear%fromYr][2]==0)
									termFunds[fiscalYear%fromYr][2]=amount;
								else
								if(termFunds[fiscalYear%fromYr][2]>0)
									termFunds[fiscalYear%fromYr][2]= termFunds[fiscalYear%fromYr][2] + amount;
							}
						}
						amount=0.0;
						
						if(ampReportCache.getCurrencyCode().equals("USD"))
								fromExchangeRate=1.0;
						else
							fromExchangeRate=DbUtil.getExchangeRate(ampReportCache.getCurrencyCode(),Constants.PLANNED,ampReportCache.getTransactionDate());
						if(ampCurrencyCode.equals("USD"))
							toExchangeRate=1.0;
						else
							toExchangeRate=DbUtil.getExchangeRate(ampCurrencyCode,Constants.PLANNED,ampReportCache.getTransactionDate());
						
						if(ampReportCache.getPlannedDisbursement().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
						{
							amount=CurrencyWorker.convert1(ampReportCache.getPlannedDisbursement().doubleValue(),fromExchangeRate,toExchangeRate);
							if(fiscalYear>=fromYr && fiscalYear<=toYr)
							{
								termFlag=1;
								if(termFunds[fiscalYear%fromYr][3]==0)
									termFunds[fiscalYear%fromYr][3]=amount;
								else
								if(termFunds[fiscalYear%fromYr][3]>0)
									termFunds[fiscalYear%fromYr][3]=termFunds[fiscalYear%fromYr][3] + amount;
							}
						}
						amount=0.0;
					}
					logger.debug("Funding Complete");
				}
			}
			if(!ampTeamDonors.getDonorAgency().equals("Unspecified"))
			{
				if(termFlag==1)
				{
					termAssist=new ProjectTermAssist();
					termAssist.setTermAssistName(termAssistName);
					if(sectorAssistance.indexOf(termAssist.getTermAssistName())==-1)
						sectorAssistance.add(termAssist.getTermAssistName());
					if(totalAssistance.indexOf(termAssist.getTermAssistName())==-1)
						totalAssistance.add(termAssist.getTermAssistName());
					termAssist.setTermAssistFund(new ArrayList());
					for(int i=0;i<yrCount;i++)
					{
						AmpFund termFund = new AmpFund();
						termFund.setCommAmount(mf.format(termFunds[i][0])); 
						termFund.setDisbAmount(mf.format(termFunds[i][1]));
						termFund.setExpAmount(mf.format(termFunds[i][2]));
						termFund.setPlannedDisbAmount(mf.format(termFunds[i][3])); 
						termAssist.getTermAssistFund().add(termFund);
						totComm=totComm + termFunds[i][0];
						totDisb=totDisb + termFunds[i][1];
						totExp=totExp + termFunds[i][2];
						totPlannedDisb=totPlannedDisb + termFunds[i][3];
					}
					termAssist.setTermCommAmount(mf.format(totComm));
					termAssist.setTermDisbAmount(mf.format(totDisb));
					termAssist.setTermExpAmount(mf.format(totExp));
					termAssist.setTermPlannedDisbAmount(mf.format(totPlannedDisb));
					termAssist.setTermUnDisbAmount(mf.format(totComm - totDisb));
					projCommAmount=projCommAmount + totComm;
					projDisbAmount=projDisbAmount + totDisb;
					projExpAmount=projExpAmount + totExp;
					projPlannedDisbAmount=projPlannedDisbAmount + totPlannedDisb;
					project.getTermAssist().add(termAssist);
		//			logger.debug("Term Assist:" + termAssist.getTermAssistName());
						
					for(int i=0;i<=yrCount;i++)
					{
						if(termAssist.getTermAssistName().equals("Loan"))
						{
							if(i<yrCount)
							{
								loanSectorFunds[i][0]=loanSectorFunds[i][0] + termFunds[i][0];
								loanSectorFunds[i][1]=loanSectorFunds[i][1] + termFunds[i][1];
								loanSectorFunds[i][2]=loanSectorFunds[i][2] + termFunds[i][2];
								loanSectorFunds[i][3]=loanSectorFunds[i][3] + termFunds[i][3];
								loanTotalFunds[i][0]=loanTotalFunds[i][0] + termFunds[i][0];
								loanTotalFunds[i][1]=loanTotalFunds[i][1] + termFunds[i][1];
								loanTotalFunds[i][2]=loanTotalFunds[i][2] + termFunds[i][2];
								loanTotalFunds[i][3]=loanTotalFunds[i][3] + termFunds[i][3];
							}
							if(i==yrCount)
							{
								loanSectorFunds[i][0]=loanSectorFunds[i][0] + totComm;
								loanSectorFunds[i][1]=loanSectorFunds[i][1] + totDisb;
								loanSectorFunds[i][2]=loanSectorFunds[i][2] + totExp;
								loanSectorFunds[i][3]=loanSectorFunds[i][3] + totPlannedDisb;
								loanTotalFunds[i][0]=loanTotalFunds[i][0] + totComm;
								loanTotalFunds[i][1]=loanTotalFunds[i][1] + totDisb;
								loanTotalFunds[i][2]=loanTotalFunds[i][2] + totExp;
								loanTotalFunds[i][3]=loanTotalFunds[i][3] + totPlannedDisb;
							}
						}
						if(termAssist.getTermAssistName().equals("Grant"))
						{
							if(i<yrCount)
							{
								grantSectorFunds[i][0]=grantSectorFunds[i][0] + termFunds[i][0];
								grantSectorFunds[i][1]=grantSectorFunds[i][1] + termFunds[i][1];
								grantSectorFunds[i][2]=grantSectorFunds[i][2] + termFunds[i][2];
								grantSectorFunds[i][3]=grantSectorFunds[i][3] + termFunds[i][3];
								grantTotalFunds[i][0]=grantTotalFunds[i][0] + termFunds[i][0];
								grantTotalFunds[i][1]=grantTotalFunds[i][1] + termFunds[i][1];
								grantTotalFunds[i][2]=grantTotalFunds[i][2] + termFunds[i][2];
								grantTotalFunds[i][3]=grantTotalFunds[i][3] + termFunds[i][3];
							}
							if(i==yrCount)
							{
								grantSectorFunds[i][0]=grantSectorFunds[i][0] + totComm;
								grantSectorFunds[i][1]=grantSectorFunds[i][1] + totDisb;
								grantSectorFunds[i][2]=grantSectorFunds[i][2] + totExp;
								grantSectorFunds[i][3]=grantSectorFunds[i][3] + totPlannedDisb;
								grantTotalFunds[i][0]=grantTotalFunds[i][0] + totComm;
								grantTotalFunds[i][1]=grantTotalFunds[i][1] + totDisb;
								grantTotalFunds[i][2]=grantTotalFunds[i][2] + totExp;
								grantTotalFunds[i][3]=grantTotalFunds[i][3] + totPlannedDisb;
							}
						}
						if(termAssist.getTermAssistName().equals("In Kind"))
						{
							if(i<yrCount)
							{
								kindSectorFunds[i][0]=kindSectorFunds[i][0] + termFunds[i][0];
								kindSectorFunds[i][1]=kindSectorFunds[i][1] + termFunds[i][1];
								kindSectorFunds[i][2]=kindSectorFunds[i][2] + termFunds[i][2];
								kindSectorFunds[i][3]=kindSectorFunds[i][3] + termFunds[i][3];
								kindTotalFunds[i][0]=kindTotalFunds[i][0] + termFunds[i][0];
								kindTotalFunds[i][1]=kindTotalFunds[i][1] + termFunds[i][1];
								kindTotalFunds[i][2]=kindTotalFunds[i][2] + termFunds[i][2];
								kindTotalFunds[i][3]=kindTotalFunds[i][3] + termFunds[i][3];
							}
							if(i==yrCount)
							{
								kindSectorFunds[i][0]=kindSectorFunds[i][0] + totComm;
								kindSectorFunds[i][1]=kindSectorFunds[i][1] + totDisb;
								kindSectorFunds[i][2]=kindSectorFunds[i][2] + totExp;
								kindSectorFunds[i][3]=kindSectorFunds[i][3] + totPlannedDisb;
								kindTotalFunds[i][0]=kindTotalFunds[i][0] + totComm;
								kindTotalFunds[i][1]=kindTotalFunds[i][1] + totDisb;
								kindTotalFunds[i][2]=kindTotalFunds[i][2] + totExp;
								kindTotalFunds[i][3]=kindTotalFunds[i][3] + totPlannedDisb;
							}
						}
					}
		//			logger.debug("After term assist");
					for(int i=0;i<yrCount;i++)
					{
						projFunds[i][0]=projFunds[i][0] + termFunds[i][0];
						projFunds[i][1]=projFunds[i][1] + termFunds[i][1];
						projFunds[i][2]=projFunds[i][2] + termFunds[i][2];
						projFunds[i][3]=projFunds[i][3] + termFunds[i][3];
					}
				}
				for(int i=0;i<yrCount;i++)
				{
					AmpFund projFund = new AmpFund();
					projFund.setCommAmount(mf.format(projFunds[i][0])); 
					projFund.setDisbAmount(mf.format(projFunds[i][1]));
					projFund.setExpAmount(mf.format(projFunds[i][2]));
					projFund.setPlannedDisbAmount(mf.format(projFunds[i][3])); 
					project.getAmpFund().add(projFund) ;
				}
				project.setProjCommAmount(mf.format(projCommAmount));
				project.setProjDisbAmount(mf.format(projDisbAmount));
				project.setProjExpAmount(mf.format(projExpAmount));
				project.setProjPlannedDisbAmount(mf.format(projPlannedDisbAmount));
				project.setProjUnDisbAmount(mf.format(projCommAmount - projDisbAmount));
				project.setRowspan(project.getTermAssist().size()+1);
				ampTeamDonors.getProject().add(project);
				for(int i=0;i<yrCount;i++)
				{
					sectorFunds[i][0]=sectorFunds[i][0] + projFunds[i][0];
					sectorFunds[i][1]=sectorFunds[i][1] + projFunds[i][1];
					sectorFunds[i][2]=sectorFunds[i][2] + projFunds[i][2];
					sectorFunds[i][3]=sectorFunds[i][3] + projFunds[i][3];
				}
				for(int i=0;i<yrCount;i++)
				{
					totalFunds[i][0]=totalFunds[i][0] + sectorFunds[i][0];
					totalFunds[i][1]=totalFunds[i][1] + sectorFunds[i][1];
					totalFunds[i][2]=totalFunds[i][2] + sectorFunds[i][2];
					totalFunds[i][3]=totalFunds[i][3] + sectorFunds[i][3];
				}
				sectorCommAmount=sectorCommAmount + projCommAmount;
				sectorDisbAmount=sectorDisbAmount + projDisbAmount;
				sectorExpAmount=sectorExpAmount + projExpAmount;
				sectorPlannedDisbAmount=sectorPlannedDisbAmount + projPlannedDisbAmount;
				totalCommAmount=totalCommAmount + sectorCommAmount;
				totalDisbAmount=totalDisbAmount + sectorDisbAmount;
				totalExpAmount=totalExpAmount + sectorExpAmount;
				totalPlannedDisbAmount=totalPlannedDisbAmount + sectorPlannedDisbAmount;
				
			}
		//	logger.debug("Size of Assistance:" + sectorAssistance.size() + ":");
				for(int i=0;i<yrCount;i++)
				{
					FundTotal total=new FundTotal();
					total.setTotCommAmount(mf.format(sectorFunds[i][0]));
					total.setTotDisbAmount(mf.format(sectorFunds[i][1]));
					total.setTotExpAmount(mf.format(sectorFunds[i][2]));
					total.setTotPlannedDisbAmount(mf.format(sectorFunds[i][3]));
					report.getTotalSectorFund().add(total);	
				}
				report.setSectorCommAmount(mf.format(sectorCommAmount));
				report.setSectorDisbAmount(mf.format(sectorDisbAmount));
				report.setSectorExpAmount(mf.format(sectorExpAmount));
				report.setSectorPlannedDisbAmount(mf.format(sectorPlannedDisbAmount));
				report.setSectorUnDisbAmount(mf.format(sectorCommAmount - sectorDisbAmount));
				for(int i=0;i<yrCount;i++)
				{
					FundTotal total=new FundTotal();
					total.setTotCommAmount(mf.format(totalFunds[i][0]));
					total.setTotDisbAmount(mf.format(totalFunds[i][1]));
					total.setTotExpAmount(mf.format(totalFunds[i][2]));
					total.setTotPlannedDisbAmount(mf.format(totalFunds[i][3]));
					report.getTotalTeamFund().add(total);	
				}
				report.setTeamCommAmount(mf.format(totalCommAmount));
				report.setTeamDisbAmount(mf.format(totalDisbAmount));
				report.setTeamExpAmount(mf.format(totalExpAmount));
				report.setTeamPlannedDisbAmount(mf.format(totalPlannedDisbAmount));
				report.setTeamUnDisbAmount(mf.format(totalCommAmount - totalDisbAmount));
				Iterator termIter=sectorAssistance.iterator();
				while(termIter.hasNext())
				{
					String assist=(String) termIter.next();
		//			logger.debug("Assist:" + assist + ":");
					TermFund termFund=new TermFund();
					if(assist.equals("Loan"))
					{
						termFund.setTermAssistName("Loan");
						termFund.setTermFundTotal(new ArrayList());
						for(int i=0;i<yrCount;i++)
						{
							TermFundTotal termFundTotal=new TermFundTotal();
							termFundTotal.setTotCommAmount(mf.format(loanSectorFunds[i][0]));
							termFundTotal.setTotDisbAmount(mf.format(loanSectorFunds[i][1]));
							termFundTotal.setTotExpAmount(mf.format(loanSectorFunds[i][2]));
							termFundTotal.setTotPlannedDisbAmount(mf.format(loanSectorFunds[i][3]));
							termFund.getTermFundTotal().add(termFundTotal);
						}
						termFund.setTotDonorCommAmount(mf.format(loanSectorFunds[yrCount][0]));
						termFund.setTotDonorDisbAmount(mf.format(loanSectorFunds[yrCount][1]));
						termFund.setTotDonorExpAmount(mf.format(loanSectorFunds[yrCount][2]));
						termFund.setTotDonorPlannedDisbAmount(mf.format(loanSectorFunds[yrCount][3]));
						termFund.setTotDonorUnDisbAmount(mf.format(loanSectorFunds[yrCount][0] - loanSectorFunds[yrCount][1]));
					}
					if(assist.equals("Grant"))
					{
		//				logger.debug("Inside Grant");
		//				logger.debug("Year Count" + yrCount);
						termFund.setTermAssistName("Grant");
						termFund.setTermFundTotal(new ArrayList());
						for(int i=0;i<yrCount;i++)
						{
							TermFundTotal termFundTotal=new TermFundTotal();
							termFundTotal.setTotCommAmount(mf.format(grantSectorFunds[i][0]));
							termFundTotal.setTotDisbAmount(mf.format(grantSectorFunds[i][1]));
							termFundTotal.setTotExpAmount(mf.format(grantSectorFunds[i][2]));
							termFundTotal.setTotPlannedDisbAmount(mf.format(grantSectorFunds[i][3]));
							termFund.getTermFundTotal().add(termFundTotal);
						}
						termFund.setTotDonorCommAmount(mf.format(grantSectorFunds[yrCount][0]));
						termFund.setTotDonorDisbAmount(mf.format(grantSectorFunds[yrCount][1]));
						termFund.setTotDonorExpAmount(mf.format(grantSectorFunds[yrCount][2]));
						termFund.setTotDonorPlannedDisbAmount(mf.format(grantSectorFunds[yrCount][3]));
						termFund.setTotDonorUnDisbAmount(mf.format(grantSectorFunds[yrCount][0] - grantSectorFunds[yrCount][1]));
					}
					if(assist.equals("In Kind"))
					{
						termFund.setTermAssistName("In Kind");
						termFund.setTermFundTotal(new ArrayList());
						for(int i=0;i<yrCount;i++)
						{
							TermFundTotal termFundTotal=new TermFundTotal();
							termFundTotal.setTotCommAmount(mf.format(kindSectorFunds[i][0]));
							termFundTotal.setTotDisbAmount(mf.format(kindSectorFunds[i][1]));
							termFundTotal.setTotExpAmount(mf.format(kindSectorFunds[i][2]));
							termFundTotal.setTotPlannedDisbAmount(mf.format(kindSectorFunds[i][3]));
							termFund.getTermFundTotal().add(termFundTotal);
						}
						termFund.setTotDonorCommAmount(mf.format(kindSectorFunds[yrCount][0]));
						termFund.setTotDonorDisbAmount(mf.format(kindSectorFunds[yrCount][1]));
						termFund.setTotDonorExpAmount(mf.format(kindSectorFunds[yrCount][2]));
						termFund.setTotDonorPlannedDisbAmount(mf.format(kindSectorFunds[yrCount][3]));
						termFund.setTotDonorUnDisbAmount(mf.format(kindSectorFunds[yrCount][0] - kindSectorFunds[yrCount][1]));
					}
		//			logger.debug("Added:'" + termFund.getTermAssistName());
					report.getTotalSectorTermAssistFund().add(termFund);	
				}
				report.getDonors().add(ampTeamDonors);
				termIter=totalAssistance.iterator();
				while(termIter.hasNext())
				{
					String assist=(String) termIter.next();
		//			logger.debug("Assist:" + assist + ":");
					TermFund termFund=new TermFund();
					if(assist.equals("Loan"))
					{
						termFund.setTermAssistName("Loan");
						termFund.setTermFundTotal(new ArrayList());
						for(int i=0;i<yrCount;i++)
						{
							TermFundTotal termFundTotal=new TermFundTotal();
							termFundTotal.setTotCommAmount(mf.format(loanTotalFunds[i][0]));
							termFundTotal.setTotDisbAmount(mf.format(loanTotalFunds[i][1]));
							termFundTotal.setTotExpAmount(mf.format(loanTotalFunds[i][2]));
							termFundTotal.setTotPlannedDisbAmount(mf.format(loanTotalFunds[i][3]));
							termFund.getTermFundTotal().add(termFundTotal);
						}
						termFund.setTotDonorCommAmount(mf.format(loanTotalFunds[yrCount][0]));
						termFund.setTotDonorDisbAmount(mf.format(loanTotalFunds[yrCount][1]));
						termFund.setTotDonorExpAmount(mf.format(loanTotalFunds[yrCount][2]));
						termFund.setTotDonorPlannedDisbAmount(mf.format(loanTotalFunds[yrCount][3]));
						termFund.setTotDonorUnDisbAmount(mf.format(loanTotalFunds[yrCount][0] - loanTotalFunds[yrCount][1]));
					}
					if(assist.equals("Grant"))
					{
		//				logger.debug("Inside Grant");
		//				logger.debug("Year Count" + yrCount);
						termFund.setTermAssistName("Grant");
						termFund.setTermFundTotal(new ArrayList());
						for(int i=0;i<yrCount;i++)
						{
							TermFundTotal termFundTotal=new TermFundTotal();
							termFundTotal.setTotCommAmount(mf.format(grantTotalFunds[i][0]));
							termFundTotal.setTotDisbAmount(mf.format(grantTotalFunds[i][1]));
							termFundTotal.setTotExpAmount(mf.format(grantTotalFunds[i][2]));
							termFundTotal.setTotPlannedDisbAmount(mf.format(grantTotalFunds[i][3]));
							termFund.getTermFundTotal().add(termFundTotal);
						}
						termFund.setTotDonorCommAmount(mf.format(grantTotalFunds[yrCount][0]));
						termFund.setTotDonorDisbAmount(mf.format(grantTotalFunds[yrCount][1]));
						termFund.setTotDonorExpAmount(mf.format(grantTotalFunds[yrCount][2]));
						termFund.setTotDonorPlannedDisbAmount(mf.format(grantTotalFunds[yrCount][3]));
						termFund.setTotDonorUnDisbAmount(mf.format(grantTotalFunds[yrCount][0] - grantTotalFunds[yrCount][1]));
					}
					if(assist.equals("In Kind"))
					{
						termFund.setTermAssistName("In Kind");
						termFund.setTermFundTotal(new ArrayList());
						for(int i=0;i<yrCount;i++)
						{
							TermFundTotal termFundTotal=new TermFundTotal();
							termFundTotal.setTotCommAmount(mf.format(kindTotalFunds[i][0]));
							termFundTotal.setTotDisbAmount(mf.format(kindTotalFunds[i][1]));
							termFundTotal.setTotExpAmount(mf.format(kindTotalFunds[i][2]));
							termFundTotal.setTotPlannedDisbAmount(mf.format(kindTotalFunds[i][3]));
							termFund.getTermFundTotal().add(termFundTotal);
						}
						termFund.setTotDonorCommAmount(mf.format(kindTotalFunds[yrCount][0]));
						termFund.setTotDonorDisbAmount(mf.format(kindTotalFunds[yrCount][1]));
						termFund.setTotDonorExpAmount(mf.format(kindTotalFunds[yrCount][2]));
						termFund.setTotDonorPlannedDisbAmount(mf.format(kindTotalFunds[yrCount][3]));
						termFund.setTotDonorUnDisbAmount(mf.format(kindTotalFunds[yrCount][0] - kindTotalFunds[yrCount][1]));
					}
		//			logger.debug("Added:'" + termFund.getTermAssistName());
					report.getTotalTeamTermAssistFund().add(termFund);	
				}
			
			ampReports.add(report);
		}
		catch(Exception ex) 		
		{
			logger.debug("Unable to get report names  from database " + ex.getMessage());
		}
		finally 
		{
			try 
			{
				PersistenceManager.releaseSession(session);
			}
			catch (Exception ex2) 
			{
				logger.debug("releaseSession() failed ");
			}
		}
		return ampReports ;
	}
	
	public static ArrayList getAmpReportMultilateral(Long ampTeamId,int fromYr,int toYr,String perspective,String ampCurrencyCode,Long ampModalityId,Long ampStatusId,Long ampDonorId,Long ampSectorId,int fiscalCalId,String startDate,String closeDate,String region)
	{
		Session session = null ;
		Query q = null ;
		ArrayList ampReports = new ArrayList() ;
		ArrayList approvedActivityList=new ArrayList();
		String queryString = null;
		Iterator iter=null;
		Long All=new Long(0);
		int yrCount = (toYr - fromYr)+1;
		int fiscalYear=0,fiscalQuarter=0;
		double[][] termFunds=new double[yrCount][4];
		double[][] donorFunds=new double[yrCount][4];
		double[][] teamFunds=new double[yrCount][4];
		double[][] loanTeamFunds=new double[yrCount+1][5];
		double[][] grantTeamFunds=new double[yrCount+1][5];
		double[][] kindTeamFunds=new double[yrCount+1][5];
		double totComm = 0.0 ;
		double totDisb = 0.0 ;
		double totExp = 0.0;
		double totPlannedDisb = 0.0;
		double toExchangeRate=1.0;
		double fromExchangeRate=0.0;
		double amount=0.0;
		int fiscalYearFlag=0;
		int flag=0;
		double donorCommAmount=0.0;
		double donorDisbAmount=0.0;
		double donorPlannedDisbAmount=0.0;
		double donorExpAmount=0.0;
		double donorUnDisbAmount=0.0;
		double teamCommAmount=0.0;
		double teamDisbAmount=0.0;
		double teamPlannedDisbAmount=0.0;
		double teamExpAmount=0.0;
		double teamUnDisbAmount=0.0;
		double actualCommitment=0.0;
		double actualDisbursement=0.0;
		int donorCount=0;
		int teamCount=0;
		int termFlag=0;
		String termAssistName=null;
		ArrayList assistance=new ArrayList();
		Iterator iterSector=null;
		DecimalFormat mf = new DecimalFormat("###,###,###,###,###") ;
		String inClause=null;		
		try
		{
			ArrayList dbReturnSet=(ArrayList)DbUtil.getAmpLevel0Teams(ampTeamId);				
			if(dbReturnSet.size()==0)
				inClause= "'" + ampTeamId + "'";
			else
			{
				iter=dbReturnSet.iterator();
				while(iter.hasNext())
				{
					Long teamId= (Long) iter.next();
					if(inClause==null)
						inClause="'" + teamId + "'";
					else
						inClause=inClause + ",'" + teamId + "'";
				}
			}
			logger.debug("Inclause: " + inClause);
			approvedActivityList=DbUtil.getApprovedActivities(inClause);
			session = PersistenceManager.getSession();
			if(startDate==null && closeDate==null)
				queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.ampTeamId in(" + inClause + ")) and (report.donorName is not null) and (report.termAssistName is not null) and (report.reportType='1') order by report.ampTeamId,report.donorName,report.termAssistName,report.fiscalYear";
			else
				queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.ampTeamId in(" + inClause + ")) and (report.actualStartDate='" + startDate + "' or report.actualCompletionDate='" + closeDate + "') and (report.donorName is not null) and (report.termAssistName is not null) and (report.reportType='1') order by report.ampTeamId,report.donorName,report.termAssistName,report.fiscalYear";
			q = session.createQuery(queryString);	
//			q.setParameter("ampTeamId",ampTeamId,Hibernate.LONG) ;
			logger.debug("Number of Records: " + q.list().size());
			multiReport report =null;
			AmpTeamDonors ampTeamDonors=null;
			TermFund donorTermAssist=null;
			TermFund teamTermAssist=null;
			if(q!=null)
			{
				iter = q.list().iterator();
				while(iter.hasNext())
				{
					AmpReportCache ampReportCache = (AmpReportCache) iter.next(); 
					if(approvedActivityList.indexOf(ampReportCache.getAmpActivityId())==-1)
						continue;

					if(ampReportCache.getAmpDonorId()==null)
						continue;

					if(new Long(fiscalCalId).equals(Constants.ETH_FY) || new Long(fiscalCalId).equals(Constants.ETH_CAL))
					{
						if(ampReportCache.getTransactionDate()!=null)
						{
							GregorianCalendar calendar = new GregorianCalendar();
							EthiopianCalendar ec=new EthiopianCalendar();
							EthiopianCalendar tempDate=new EthiopianCalendar();
							calendar.setTime(ampReportCache.getTransactionDate());
							ec=tempDate.getEthiopianDate(calendar);
							if(new Long(fiscalCalId).equals(Constants.ETH_FY))
							{
								fiscalYear=(int)ec.ethFiscalYear;
								fiscalQuarter=(int)ec.ethFiscalQrt;
							}
							if(new Long(fiscalCalId).equals(Constants.ETH_CAL))
							{
								fiscalYear=(int)ec.ethYear;
								fiscalQuarter=(int)ec.ethQtr;
							}
							logger.debug("Ethiopian Fiscal Year: " + fiscalYear);
							logger.debug("From Year: " + fromYr);
							logger.debug("From Year: " + toYr);
						/*	if(fiscalYear<fromYr || fiscalYear>toYr)
								continue;*/
						}
					}

					if(!ampModalityId.equals(All))
					{
						if(ampReportCache.getAmpModalityId()==null)	
							continue;
						if(!(ampModalityId.equals(ampReportCache.getAmpModalityId())))
							continue;
					}

					if(!ampStatusId.equals(All))
					{
						if(ampReportCache.getAmpStatusId()==null)
							continue;
						if(!(ampStatusId.equals(ampReportCache.getAmpStatusId())))
							continue;
					}
	
					if(!ampDonorId.equals(All))
					{
						if(ampReportCache.getAmpDonorId()==null)
							continue;
						if(!(ampDonorId.equals(ampReportCache.getAmpDonorId())))
							continue;
					}
		
					if(!ampSectorId.equals(All))
					{
						int sflag=0;
						iterSector=DbUtil.getAmpReportSectorId(ampReportCache.getAmpActivityId()).iterator();
						while(iterSector.hasNext())
						{
							AmpReportSector sector=(AmpReportSector) iterSector.next();
							if(sector.getAmpSectorId().equals(ampSectorId))
							{
								sflag=1;
								break;
							}
							if(sector.getAmpSubSectorId().equals(new Long(0)))
							{
								if(new Long(sector.getSubSectorName()).equals(ampSectorId))
								{
									sflag=1;
									break;
								}
							}
							if(!(sector.getAmpSubSectorId().equals(new Long(0))) && sector.getAmpSubSectorId().equals(ampSectorId))
							{
								sflag=1;
								break;
							}
						}
						if (sflag==0)
						{
							continue;
						}
					}
					
					if(!region.equals("All"))
					{
						ArrayList location=(ArrayList)DbUtil.getAmpReportLocation(ampReportCache.getAmpActivityId());
						if(location.indexOf(region)==-1)
							continue;
					}					
					if(report==null || !(report.getAmpTeamId().equals(ampReportCache.getAmpTeamId())))
					{
						logger.debug("New donor: " + ampReportCache.getDonorName());
						if(report!=null)
						{
							if(termFlag==1)
							{
								donorTermAssist=new TermFund();
								donorTermAssist.setTermAssistName(termAssistName);
								donorTermAssist.setTermFundTotal(new ArrayList());
								if(assistance.indexOf(termAssistName)==-1)
									assistance.add(termAssistName);
								for(int i=0;i<yrCount;i++)
								{
									TermFundTotal termFundTotal=new TermFundTotal();
									termFundTotal.setTotCommAmount(mf.format(termFunds[i][0]));
									termFundTotal.setTotDisbAmount(mf.format(termFunds[i][1]));
									termFundTotal.setTotExpAmount(mf.format(termFunds[i][2]));
									termFundTotal.setTotPlannedDisbAmount(mf.format(termFunds[i][3]));
									donorTermAssist.getTermFundTotal().add(termFundTotal);
									totComm=totComm + termFunds[i][0];
									totDisb=totDisb + termFunds[i][1];
									totExp=totExp + termFunds[i][2];
									totPlannedDisb=totPlannedDisb + termFunds[i][3];
									donorFunds[i][0]=donorFunds[i][0] + termFunds[i][0];
									donorFunds[i][1]=donorFunds[i][1] + termFunds[i][1];
									donorFunds[i][2]=donorFunds[i][2] + termFunds[i][2];
									donorFunds[i][3]=donorFunds[i][3] + termFunds[i][3];
								}
								logger.debug("End First For");
						
								donorTermAssist.setTotDonorCommAmount(mf.format(totComm));
								donorTermAssist.setTotDonorDisbAmount(mf.format(totDisb));
								donorTermAssist.setTotDonorExpAmount(mf.format(totExp));
								donorTermAssist.setTotDonorPlannedDisbAmount(mf.format(totPlannedDisb));
								donorTermAssist.setTotDonorUnDisbAmount(mf.format(actualCommitment-actualDisbursement));
								ampTeamDonors.getTotalDonorTermAssistFund().add(donorTermAssist);
								for(int i=0;i<=yrCount;i++)
								{
									if(donorTermAssist.getTermAssistName().equals("Loan"))
									{
										if(i<yrCount)
										{
											loanTeamFunds[i][0]=loanTeamFunds[i][0] + termFunds[i][0];
											loanTeamFunds[i][1]=loanTeamFunds[i][1] + termFunds[i][1];
											loanTeamFunds[i][2]=loanTeamFunds[i][2] + termFunds[i][2];
											loanTeamFunds[i][3]=loanTeamFunds[i][3] + termFunds[i][3];
										}
										if(i==yrCount)
										{
											loanTeamFunds[i][0]=loanTeamFunds[i][0] + totComm;
											loanTeamFunds[i][1]=loanTeamFunds[i][1] + totDisb;
											loanTeamFunds[i][2]=loanTeamFunds[i][2] + totExp;
											loanTeamFunds[i][3]=loanTeamFunds[i][3] + totPlannedDisb;
											loanTeamFunds[i][4]=loanTeamFunds[i][4] + (actualCommitment-actualDisbursement);
										}
									}
									if(donorTermAssist.getTermAssistName().equals("Grant"))
									{
										if(i<yrCount)
										{
											grantTeamFunds[i][0]=grantTeamFunds[i][0] + termFunds[i][0];
											grantTeamFunds[i][1]=grantTeamFunds[i][1] + termFunds[i][1];
											grantTeamFunds[i][2]=grantTeamFunds[i][2] + termFunds[i][2];
											grantTeamFunds[i][3]=grantTeamFunds[i][3] + termFunds[i][3];
										}
										if(i==yrCount)
										{
											grantTeamFunds[i][0]=grantTeamFunds[i][0] + totComm;
											grantTeamFunds[i][1]=grantTeamFunds[i][1] + totDisb;
											grantTeamFunds[i][2]=grantTeamFunds[i][2] + totExp;
											grantTeamFunds[i][3]=grantTeamFunds[i][3] + totPlannedDisb;
											grantTeamFunds[i][4]=grantTeamFunds[i][4] + (actualCommitment-actualDisbursement);
										}
									}
									if(donorTermAssist.getTermAssistName().equals("In Kind"))
									{
										if(i<yrCount)
										{
											kindTeamFunds[i][0]=kindTeamFunds[i][0] + termFunds[i][0];
											kindTeamFunds[i][1]=kindTeamFunds[i][1] + termFunds[i][1];
											kindTeamFunds[i][2]=kindTeamFunds[i][2] + termFunds[i][2];
											kindTeamFunds[i][3]=kindTeamFunds[i][3] + termFunds[i][3];
										}
										if(i==yrCount)
										{
											kindTeamFunds[i][0]=kindTeamFunds[i][0] + totComm;
											kindTeamFunds[i][1]=kindTeamFunds[i][1] + totDisb;
											kindTeamFunds[i][2]=kindTeamFunds[i][2] + totExp;
											kindTeamFunds[i][3]=kindTeamFunds[i][3] + totPlannedDisb;
											kindTeamFunds[i][4]=kindTeamFunds[i][4] + (actualCommitment-actualDisbursement);
										}
									}
								}
							}
							donorCommAmount=donorCommAmount + totComm;
							donorDisbAmount=donorDisbAmount + totDisb;
							donorExpAmount=donorExpAmount + totExp;
							donorPlannedDisbAmount=donorPlannedDisbAmount + totPlannedDisb;
							donorUnDisbAmount=donorUnDisbAmount + (actualCommitment-actualDisbursement);
							for(int i=0;i<yrCount;i++)
							{
								FundTotal total=new FundTotal();
								total.setTotCommAmount(mf.format(donorFunds[i][0]));
								total.setTotDisbAmount(mf.format(donorFunds[i][1]));
								total.setTotExpAmount(mf.format(donorFunds[i][2]));
								total.setTotPlannedDisbAmount(mf.format(donorFunds[i][3]));
								ampTeamDonors.getTotalDonorFund().add(total);	
								teamFunds[i][0]=teamFunds[i][0] + donorFunds[i][0];
								teamFunds[i][1]=teamFunds[i][1] + donorFunds[i][1];
								teamFunds[i][2]=teamFunds[i][2] + donorFunds[i][2];
								teamFunds[i][3]=teamFunds[i][3] + donorFunds[i][3];
							}
							logger.debug("End Second For");
							ampTeamDonors.setDonorCommAmount(mf.format(donorCommAmount));
							ampTeamDonors.setDonorDisbAmount(mf.format(donorDisbAmount));
							ampTeamDonors.setDonorExpAmount(mf.format(donorExpAmount));
							ampTeamDonors.setDonorPlannedDisbAmount(mf.format(donorPlannedDisbAmount));
							ampTeamDonors.setDonorUnDisbAmount(mf.format(donorUnDisbAmount));
							report.getDonors().add(ampTeamDonors);
							teamCommAmount=teamCommAmount + donorCommAmount;
							teamDisbAmount=teamDisbAmount + donorDisbAmount;
							teamExpAmount=teamExpAmount + donorExpAmount;
							teamPlannedDisbAmount=teamPlannedDisbAmount + donorPlannedDisbAmount;
							teamUnDisbAmount=teamUnDisbAmount + donorUnDisbAmount;
							
							for(int i=0;i<yrCount;i++)
							{
								FundTotal total=new FundTotal();
								total.setTotCommAmount(mf.format(teamFunds[i][0]));
								total.setTotDisbAmount(mf.format(teamFunds[i][1]));
								total.setTotExpAmount(mf.format(teamFunds[i][2]));
								total.setTotPlannedDisbAmount(mf.format(teamFunds[i][3]));
								report.getTotalTeamFund().add(total);	
							}
							logger.debug("End Third For");
							report.setTeamCommAmount(mf.format(teamCommAmount));
							report.setTeamDisbAmount(mf.format(teamDisbAmount));
							report.setTeamExpAmount(mf.format(teamExpAmount));
							report.setTeamPlannedDisbAmount(mf.format(teamPlannedDisbAmount));
							report.setTeamUnDisbAmount(mf.format(teamUnDisbAmount));
							Iterator teamIter=assistance.iterator();
							while(teamIter.hasNext())
							{
								String assist=(String) teamIter.next();
								logger.debug("Assist:" + assist + ":");
								TermFund termFund=new TermFund();
								if(assist.equals("Loan"))
								{
									termFund.setTermAssistName("Loan");
									termFund.setTermFundTotal(new ArrayList());
									for(int i=0;i<yrCount;i++)
									{
										TermFundTotal termFundTotal=new TermFundTotal();
										termFundTotal.setTotCommAmount(mf.format(loanTeamFunds[i][0]));
										termFundTotal.setTotDisbAmount(mf.format(loanTeamFunds[i][1]));
										termFundTotal.setTotExpAmount(mf.format(loanTeamFunds[i][2]));
										termFundTotal.setTotPlannedDisbAmount(mf.format(loanTeamFunds[i][3]));
										termFund.getTermFundTotal().add(termFundTotal);
									}
									termFund.setTotDonorCommAmount(mf.format(loanTeamFunds[yrCount][0]));
									termFund.setTotDonorDisbAmount(mf.format(loanTeamFunds[yrCount][1]));
									termFund.setTotDonorExpAmount(mf.format(loanTeamFunds[yrCount][2]));
									termFund.setTotDonorPlannedDisbAmount(mf.format(loanTeamFunds[yrCount][3]));
									termFund.setTotDonorUnDisbAmount(mf.format(loanTeamFunds[yrCount][4]));
								}
								if(assist.equals("Grant"))
								{
									termFund.setTermAssistName("Grant");
									termFund.setTermFundTotal(new ArrayList());
									for(int i=0;i<yrCount;i++)
									{
										TermFundTotal termFundTotal=new TermFundTotal();
										termFundTotal.setTotCommAmount(mf.format(grantTeamFunds[i][0]));
										termFundTotal.setTotDisbAmount(mf.format(grantTeamFunds[i][1]));
										termFundTotal.setTotExpAmount(mf.format(grantTeamFunds[i][2]));
										termFundTotal.setTotPlannedDisbAmount(mf.format(grantTeamFunds[i][3]));
										termFund.getTermFundTotal().add(termFundTotal);
									}
									termFund.setTotDonorCommAmount(mf.format(grantTeamFunds[yrCount][0]));
									termFund.setTotDonorDisbAmount(mf.format(grantTeamFunds[yrCount][1]));
									termFund.setTotDonorExpAmount(mf.format(grantTeamFunds[yrCount][2]));
									termFund.setTotDonorPlannedDisbAmount(mf.format(grantTeamFunds[yrCount][3]));
									termFund.setTotDonorUnDisbAmount(mf.format(grantTeamFunds[yrCount][4]));
								}
								if(assist.equals("In Kind"))
								{
									termFund.setTermAssistName("In Kind");
									termFund.setTermFundTotal(new ArrayList());
									for(int i=0;i<yrCount;i++)
									{
										TermFundTotal termFundTotal=new TermFundTotal();
										termFundTotal.setTotCommAmount(mf.format(kindTeamFunds[i][0]));
										termFundTotal.setTotDisbAmount(mf.format(kindTeamFunds[i][1]));
										termFundTotal.setTotExpAmount(mf.format(kindTeamFunds[i][2]));
										termFundTotal.setTotPlannedDisbAmount(mf.format(kindTeamFunds[i][3]));
										termFund.getTermFundTotal().add(termFundTotal);
									}
									termFund.setTotDonorCommAmount(mf.format(kindTeamFunds[yrCount][0]));
									termFund.setTotDonorDisbAmount(mf.format(kindTeamFunds[yrCount][1]));
									termFund.setTotDonorExpAmount(mf.format(kindTeamFunds[yrCount][2]));
									termFund.setTotDonorPlannedDisbAmount(mf.format(kindTeamFunds[yrCount][3]));
									termFund.setTotDonorUnDisbAmount(mf.format(kindTeamFunds[yrCount][4]));
								}
								logger.debug("Added: " + termFund.getTermAssistName());
								report.getTotalTeamTermAssistFund().add(termFund);	
							}
							ampReports.add(report);
							totComm=totDisb=totExp=totPlannedDisb=0.0;
							donorCommAmount=donorDisbAmount=donorExpAmount=donorPlannedDisbAmount=donorUnDisbAmount=0.0;
							teamCommAmount=teamDisbAmount=teamExpAmount=teamPlannedDisbAmount=teamUnDisbAmount=0.0;
							actualCommitment=0.0;
							actualDisbursement=0.0;
							for(int i=0;i<yrCount;i++)
							{
								termFunds[i][0]=termFunds[i][1]=termFunds[i][2]=termFunds[i][3]=0;
								teamFunds[i][0]=teamFunds[i][1]=teamFunds[i][2]=teamFunds[i][3]=0;
								donorFunds[i][0]=donorFunds[i][1]=donorFunds[i][2]=donorFunds[i][3]=0;
								loanTeamFunds[i][0]=loanTeamFunds[i][1]=loanTeamFunds[i][2]=loanTeamFunds[i][3]=loanTeamFunds[i][4]=0;
								grantTeamFunds[i][0]=grantTeamFunds[i][1]=grantTeamFunds[i][2]=grantTeamFunds[i][3]=grantTeamFunds[i][4]=0;
								kindTeamFunds[i][0]=kindTeamFunds[i][1]=kindTeamFunds[i][2]=kindTeamFunds[i][3]=kindTeamFunds[i][4]=0;
							}
							loanTeamFunds[yrCount][0]=loanTeamFunds[yrCount][1]=loanTeamFunds[yrCount][2]=loanTeamFunds[yrCount][3]=0;
							grantTeamFunds[yrCount][0]=grantTeamFunds[yrCount][1]=grantTeamFunds[yrCount][2]=grantTeamFunds[yrCount][3]=0;
							kindTeamFunds[yrCount][0]=kindTeamFunds[yrCount][1]=kindTeamFunds[yrCount][2]=kindTeamFunds[yrCount][3]=0;
							donorCount=0;
						}
						//logger.debug("begin intialize");
						report = new multiReport();
						AmpTeam ampTeam=DbUtil.getAmpTeam(ampReportCache.getAmpTeamId());
						report.setAmpTeamId(ampReportCache.getAmpTeamId());
						report.setTeamName(ampTeam.getName());
						report.setTotalTeamFund(new ArrayList());
						report.setTotalTeamTermAssistFund(new ArrayList());
						report.setCount(++teamCount);
						report.setDonors(new ArrayList());
						ampTeamDonors=new AmpTeamDonors();
						ampTeamDonors.setDonorAgency(ampReportCache.getDonorName());
						ampTeamDonors.setDonorCount(++donorCount);
						ampTeamDonors.setTotalDonorFund(new ArrayList());
						ampTeamDonors.setTotalDonorTermAssistFund(new ArrayList());
						if(ampReportCache.getTermAssistName()!=null)
							termAssistName=ampReportCache.getTermAssistName();
						termFlag=0;
						logger.debug("end init");
											
					}

					if(report.getAmpTeamId().equals(ampReportCache.getAmpTeamId()) && !(ampReportCache.getDonorName().equals(ampTeamDonors.getDonorAgency())))
					{
						logger.debug("Inside donor");
						if(termFlag==1)
						{
							donorTermAssist=new TermFund();
							donorTermAssist.setTermAssistName(termAssistName);
							donorTermAssist.setTermFundTotal(new ArrayList());
							if(assistance.indexOf(termAssistName)==-1)
								assistance.add(termAssistName);
							for(int i=0;i<yrCount;i++)
							{
								TermFundTotal termFundTotal=new TermFundTotal();
								termFundTotal.setTotCommAmount(mf.format(termFunds[i][0]));
								termFundTotal.setTotDisbAmount(mf.format(termFunds[i][1]));
								termFundTotal.setTotExpAmount(mf.format(termFunds[i][2]));
								termFundTotal.setTotPlannedDisbAmount(mf.format(termFunds[i][3]));
								donorTermAssist.getTermFundTotal().add(termFundTotal);
								totComm=totComm + termFunds[i][0];
								totDisb=totDisb + termFunds[i][1];
								totExp=totExp + termFunds[i][2];
								totPlannedDisb=totPlannedDisb + termFunds[i][3];
								donorFunds[i][0]=donorFunds[i][0] + termFunds[i][0];
								donorFunds[i][1]=donorFunds[i][1] + termFunds[i][1];
								donorFunds[i][2]=donorFunds[i][2] + termFunds[i][2];
								donorFunds[i][3]=donorFunds[i][3] + termFunds[i][3];
							}
						
							donorTermAssist.setTotDonorCommAmount(mf.format(totComm));
							donorTermAssist.setTotDonorDisbAmount(mf.format(totDisb));
							donorTermAssist.setTotDonorExpAmount(mf.format(totExp));
							donorTermAssist.setTotDonorPlannedDisbAmount(mf.format(totPlannedDisb));
							donorTermAssist.setTotDonorUnDisbAmount(mf.format(actualCommitment-actualDisbursement));
							ampTeamDonors.getTotalDonorTermAssistFund().add(donorTermAssist);
							for(int i=0;i<=yrCount;i++)
							{
								if(donorTermAssist.getTermAssistName().equals("Loan"))
								{
									if(i<yrCount)
									{
										loanTeamFunds[i][0]=loanTeamFunds[i][0] + termFunds[i][0];
										loanTeamFunds[i][1]=loanTeamFunds[i][1] + termFunds[i][1];
										loanTeamFunds[i][2]=loanTeamFunds[i][2] + termFunds[i][2];
										loanTeamFunds[i][3]=loanTeamFunds[i][3] + termFunds[i][3];
									}
									if(i==yrCount)
									{
										loanTeamFunds[i][0]=loanTeamFunds[i][0] + totComm;
										loanTeamFunds[i][1]=loanTeamFunds[i][1] + totDisb;
										loanTeamFunds[i][2]=loanTeamFunds[i][2] + totExp;
										loanTeamFunds[i][3]=loanTeamFunds[i][3] + totPlannedDisb;
										loanTeamFunds[i][4]=loanTeamFunds[i][4] + (actualCommitment-actualDisbursement);
									}
								}
								if(donorTermAssist.getTermAssistName().equals("Grant"))
								{
									if(i<yrCount)
									{
										grantTeamFunds[i][0]=grantTeamFunds[i][0] + termFunds[i][0];
										grantTeamFunds[i][1]=grantTeamFunds[i][1] + termFunds[i][1];
										grantTeamFunds[i][2]=grantTeamFunds[i][2] + termFunds[i][2];
										grantTeamFunds[i][3]=grantTeamFunds[i][3] + termFunds[i][3];
									}
									if(i==yrCount)
									{
										grantTeamFunds[i][0]=grantTeamFunds[i][0] + totComm;
										grantTeamFunds[i][1]=grantTeamFunds[i][1] + totDisb;
										grantTeamFunds[i][2]=grantTeamFunds[i][2] + totExp;
										grantTeamFunds[i][3]=grantTeamFunds[i][3] + totPlannedDisb;
										grantTeamFunds[i][4]=grantTeamFunds[i][4] + (actualCommitment-actualDisbursement);
									}
								}
								if(donorTermAssist.getTermAssistName().equals("In Kind"))
								{
									if(i<yrCount)
									{
										kindTeamFunds[i][0]=kindTeamFunds[i][0] + termFunds[i][0];
										kindTeamFunds[i][1]=kindTeamFunds[i][1] + termFunds[i][1];
										kindTeamFunds[i][2]=kindTeamFunds[i][2] + termFunds[i][2];
										kindTeamFunds[i][3]=kindTeamFunds[i][3] + termFunds[i][3];
									}
									if(i==yrCount)
									{
										kindTeamFunds[i][0]=kindTeamFunds[i][0] + totComm;
										kindTeamFunds[i][1]=kindTeamFunds[i][1] + totDisb;
										kindTeamFunds[i][2]=kindTeamFunds[i][2] + totExp;
										kindTeamFunds[i][3]=kindTeamFunds[i][3] + totPlannedDisb;
										kindTeamFunds[i][4]=kindTeamFunds[i][4] + (actualCommitment-actualDisbursement);
									}
								}
							}
						}
						donorCommAmount=donorCommAmount + totComm;
						donorDisbAmount=donorDisbAmount + totDisb;
						donorExpAmount=donorExpAmount + totExp;
						donorPlannedDisbAmount=donorPlannedDisbAmount + totPlannedDisb;
						donorUnDisbAmount=donorUnDisbAmount + (actualCommitment-actualDisbursement);
						for(int i=0;i<yrCount;i++)
						{
							FundTotal total=new FundTotal();
							total.setTotCommAmount(mf.format(donorFunds[i][0]));
							total.setTotDisbAmount(mf.format(donorFunds[i][1]));
							total.setTotExpAmount(mf.format(donorFunds[i][2]));
							total.setTotPlannedDisbAmount(mf.format(donorFunds[i][3]));
							ampTeamDonors.getTotalDonorFund().add(total);	
							teamFunds[i][0]=teamFunds[i][0] + donorFunds[i][0];
							teamFunds[i][1]=teamFunds[i][1] + donorFunds[i][1];
							teamFunds[i][2]=teamFunds[i][2] + donorFunds[i][2];
							teamFunds[i][3]=teamFunds[i][3] + donorFunds[i][3];
						}
						ampTeamDonors.setDonorCommAmount(mf.format(donorCommAmount));
						ampTeamDonors.setDonorDisbAmount(mf.format(donorDisbAmount));
						ampTeamDonors.setDonorExpAmount(mf.format(donorExpAmount));
						ampTeamDonors.setDonorPlannedDisbAmount(mf.format(donorPlannedDisbAmount));
						ampTeamDonors.setDonorUnDisbAmount(mf.format(donorUnDisbAmount));
						report.getDonors().add(ampTeamDonors);
						teamCommAmount=teamCommAmount + donorCommAmount;
						teamDisbAmount=teamDisbAmount + donorDisbAmount;
						teamExpAmount=teamExpAmount + donorExpAmount;
						teamPlannedDisbAmount=teamPlannedDisbAmount + donorPlannedDisbAmount;
						teamUnDisbAmount=teamUnDisbAmount + donorUnDisbAmount;
						
						totComm=totDisb=totExp=totPlannedDisb=0.0;
						donorCommAmount=donorDisbAmount=donorExpAmount=donorPlannedDisbAmount=donorUnDisbAmount=0.0;
						actualCommitment=0.0;
						actualDisbursement=0.0;
						for(int i=0;i<yrCount;i++)
						{
							termFunds[i][0]=termFunds[i][1]=termFunds[i][2]=termFunds[i][3]=0;
							donorFunds[i][0]=donorFunds[i][1]=donorFunds[i][2]=donorFunds[i][3]=0;
						}
						ampTeamDonors=new AmpTeamDonors();
						ampTeamDonors.setDonorAgency(ampReportCache.getDonorName());
						ampTeamDonors.setDonorCount(++donorCount);
						ampTeamDonors.setTotalDonorFund(new ArrayList());
						ampTeamDonors.setTotalDonorTermAssistFund(new ArrayList());
						if(ampReportCache.getTermAssistName()!=null)
							termAssistName=ampReportCache.getTermAssistName();
						termFlag=0;
						logger.debug("Outside donor");
					}
					
					if(report.getAmpTeamId().equals(ampReportCache.getAmpTeamId()) && ampReportCache.getDonorName().equals(ampTeamDonors.getDonorAgency()) && !(ampReportCache.getTermAssistName().equals(termAssistName)))
					{
						logger.debug("different project");
						if(termFlag==1)
						{
							donorTermAssist=new TermFund();
							donorTermAssist.setTermAssistName(termAssistName);
							donorTermAssist.setTermFundTotal(new ArrayList());
							if(assistance.indexOf(termAssistName)==-1)
								assistance.add(termAssistName);
							for(int i=0;i<yrCount;i++)
							{
								TermFundTotal termFundTotal=new TermFundTotal();
								termFundTotal.setTotCommAmount(mf.format(termFunds[i][0]));
								termFundTotal.setTotDisbAmount(mf.format(termFunds[i][1]));
								termFundTotal.setTotExpAmount(mf.format(termFunds[i][2]));
								termFundTotal.setTotPlannedDisbAmount(mf.format(termFunds[i][3]));
								donorTermAssist.getTermFundTotal().add(termFundTotal);
								totComm=totComm + termFunds[i][0];
								totDisb=totDisb + termFunds[i][1];
								totExp=totExp + termFunds[i][2];
								totPlannedDisb=totPlannedDisb + termFunds[i][3];
								donorFunds[i][0]=donorFunds[i][0] + termFunds[i][0];
								donorFunds[i][1]=donorFunds[i][1] + termFunds[i][1];
								donorFunds[i][2]=donorFunds[i][2] + termFunds[i][2];
								donorFunds[i][3]=donorFunds[i][3] + termFunds[i][3];
							}
						
							donorTermAssist.setTotDonorCommAmount(mf.format(totComm));
							donorTermAssist.setTotDonorDisbAmount(mf.format(totDisb));
							donorTermAssist.setTotDonorExpAmount(mf.format(totExp));
							donorTermAssist.setTotDonorPlannedDisbAmount(mf.format(totPlannedDisb));
							donorTermAssist.setTotDonorUnDisbAmount(mf.format(actualCommitment-actualDisbursement));
							ampTeamDonors.getTotalDonorTermAssistFund().add(donorTermAssist);
							for(int i=0;i<=yrCount;i++)
							{
								if(donorTermAssist.getTermAssistName().equals("Loan"))
								{
									if(i<yrCount)
									{
										loanTeamFunds[i][0]=loanTeamFunds[i][0] + termFunds[i][0];
										loanTeamFunds[i][1]=loanTeamFunds[i][1] + termFunds[i][1];
										loanTeamFunds[i][2]=loanTeamFunds[i][2] + termFunds[i][2];
										loanTeamFunds[i][3]=loanTeamFunds[i][3] + termFunds[i][3];
									}
									if(i==yrCount)
									{
										loanTeamFunds[i][0]=loanTeamFunds[i][0] + totComm;
										loanTeamFunds[i][1]=loanTeamFunds[i][1] + totDisb;
										loanTeamFunds[i][2]=loanTeamFunds[i][2] + totExp;
										loanTeamFunds[i][3]=loanTeamFunds[i][3] + totPlannedDisb;
										loanTeamFunds[i][4]=loanTeamFunds[i][4] + (actualCommitment-actualDisbursement);
									}
								}
								if(donorTermAssist.getTermAssistName().equals("Grant"))
								{
									if(i<yrCount)
									{
										grantTeamFunds[i][0]=grantTeamFunds[i][0] + termFunds[i][0];
										grantTeamFunds[i][1]=grantTeamFunds[i][1] + termFunds[i][1];
										grantTeamFunds[i][2]=grantTeamFunds[i][2] + termFunds[i][2];
										grantTeamFunds[i][3]=grantTeamFunds[i][3] + termFunds[i][3];
									}
									if(i==yrCount)
									{
										grantTeamFunds[i][0]=grantTeamFunds[i][0] + totComm;
										grantTeamFunds[i][1]=grantTeamFunds[i][1] + totDisb;
										grantTeamFunds[i][2]=grantTeamFunds[i][2] + totExp;
										grantTeamFunds[i][3]=grantTeamFunds[i][3] + totPlannedDisb;
										grantTeamFunds[i][4]=grantTeamFunds[i][4] + (actualCommitment-actualDisbursement);
									}
								}
								if(donorTermAssist.getTermAssistName().equals("In Kind"))
								{
									if(i<yrCount)
									{
										kindTeamFunds[i][0]=kindTeamFunds[i][0] + termFunds[i][0];
										kindTeamFunds[i][1]=kindTeamFunds[i][1] + termFunds[i][1];
										kindTeamFunds[i][2]=kindTeamFunds[i][2] + termFunds[i][2];
										kindTeamFunds[i][3]=kindTeamFunds[i][3] + termFunds[i][3];
									}
									if(i==yrCount)
									{
										kindTeamFunds[i][0]=kindTeamFunds[i][0] + totComm;
										kindTeamFunds[i][1]=kindTeamFunds[i][1] + totDisb;
										kindTeamFunds[i][2]=kindTeamFunds[i][2] + totExp;
										kindTeamFunds[i][3]=kindTeamFunds[i][3] + totPlannedDisb;
										kindTeamFunds[i][4]=kindTeamFunds[i][4] + (actualCommitment-actualDisbursement);
									}
								}
							}
							
						}
						donorCommAmount=donorCommAmount + totComm;
						donorDisbAmount=donorDisbAmount + totDisb;
						donorExpAmount=donorExpAmount + totExp;
						donorPlannedDisbAmount=donorPlannedDisbAmount + totPlannedDisb;
						donorUnDisbAmount=donorUnDisbAmount + (actualCommitment-actualDisbursement);
						totComm=totDisb=totExp=totPlannedDisb=0.0;
						actualCommitment=0.0;
						actualDisbursement=0.0;
						for(int i=0;i<yrCount;i++)
							termFunds[i][0]=termFunds[i][1]=termFunds[i][2]=termFunds[i][3]=0;
						if(ampReportCache.getTermAssistName()!=null)
							termAssistName=ampReportCache.getTermAssistName();
						termFlag=0;
					}
					logger.debug("Funding Information");
					if(ampReportCache.getFiscalYear()!=null && ampReportCache.getFiscalQuarter()!=null)
					{
						//logger.debug("begin if");
						if(new Long(fiscalCalId).equals(Constants.GREGORIAN))
						{
							fiscalYear=ampReportCache.getFiscalYear().intValue();
							fiscalQuarter=ampReportCache.getFiscalQuarter().intValue();
						}
						if(ampReportCache.getCurrencyCode().equals("USD"))
							fromExchangeRate=1.0;
						else
							fromExchangeRate=DbUtil.getExchangeRate(ampReportCache.getCurrencyCode(),Constants.ACTUAL,ampReportCache.getTransactionDate());
						if(ampCurrencyCode.equals("USD"))
							toExchangeRate=1.0;
						else
							toExchangeRate=DbUtil.getExchangeRate(ampCurrencyCode,Constants.ACTUAL,ampReportCache.getTransactionDate());
						if(ampReportCache.getActualCommitment().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
						{
							termFlag=1;
							amount=CurrencyWorker.convert1(ampReportCache.getActualCommitment().doubleValue(),fromExchangeRate,toExchangeRate);
							actualCommitment=actualCommitment+amount;
							if(fiscalYear>=fromYr && fiscalYear<=toYr)
							{
								if(termFunds[fiscalYear%fromYr][0]==0)
									termFunds[fiscalYear%fromYr][0]=amount;
								else
								if(termFunds[fiscalYear%fromYr][0]>0)
									termFunds[fiscalYear%fromYr][0]=termFunds[fiscalYear%fromYr][0] + amount;
							}
						}
						amount=0.0;
							
						if(ampReportCache.getActualDisbursement().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
						{
							termFlag=1;
							amount=CurrencyWorker.convert1(ampReportCache.getActualDisbursement().doubleValue(),fromExchangeRate,toExchangeRate);
							actualDisbursement=actualDisbursement + amount;
							if(fiscalYear>=fromYr && fiscalYear<=toYr)
							{
								if(termFunds[fiscalYear%fromYr][1]==0)
									termFunds[fiscalYear%fromYr][1]=amount;
								else
								if(termFunds[fiscalYear%fromYr][1]>0)
									termFunds[fiscalYear%fromYr][1]=termFunds[fiscalYear%fromYr][1] + amount;
							}
						}
						amount=0.0;
						if(ampReportCache.getActualExpenditure().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
						{
							amount=CurrencyWorker.convert1(ampReportCache.getActualExpenditure().doubleValue(),fromExchangeRate,toExchangeRate);
							if(fiscalYear>=fromYr && fiscalYear<=toYr)
							{
								termFlag=1;
								if(termFunds[fiscalYear%fromYr][2]==0)
									termFunds[fiscalYear%fromYr][2]=amount;
								else
								if(termFunds[fiscalYear%fromYr][2]>0)
									termFunds[fiscalYear%fromYr][2]= termFunds[fiscalYear%fromYr][2] + amount;
							}
						}
						amount=0.0;
						if(ampReportCache.getCurrencyCode().equals("USD"))
							fromExchangeRate=1.0;
						else
							fromExchangeRate=DbUtil.getExchangeRate(ampReportCache.getCurrencyCode(),Constants.PLANNED,ampReportCache.getTransactionDate());
						if(ampCurrencyCode.equals("USD"))
							toExchangeRate=1.0;
						else
							toExchangeRate=DbUtil.getExchangeRate(ampCurrencyCode,Constants.PLANNED,ampReportCache.getTransactionDate());
						if(ampReportCache.getPlannedDisbursement().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
						{
							amount=CurrencyWorker.convert1(ampReportCache.getPlannedDisbursement().doubleValue(),fromExchangeRate,toExchangeRate);
							if(fiscalYear>=fromYr && fiscalYear<=toYr)
							{
								termFlag=1;
								if(termFunds[fiscalYear%fromYr][3]==0)
									termFunds[fiscalYear%fromYr][3]=amount;
								else
								if(termFunds[fiscalYear%fromYr][3]>0)
									termFunds[fiscalYear%fromYr][3]=termFunds[fiscalYear%fromYr][3] + amount;
							}
						}
						amount=0.0;
										
					}

				}
				if(termFlag==1)
				{
					donorTermAssist=new TermFund();
					donorTermAssist.setTermAssistName(termAssistName);
					donorTermAssist.setTermFundTotal(new ArrayList());
					if(assistance.indexOf(termAssistName)==-1)
						assistance.add(termAssistName);
					for(int i=0;i<yrCount;i++)
					{
						TermFundTotal termFundTotal=new TermFundTotal();
						termFundTotal.setTotCommAmount(mf.format(termFunds[i][0]));
						termFundTotal.setTotDisbAmount(mf.format(termFunds[i][1]));
						termFundTotal.setTotExpAmount(mf.format(termFunds[i][2]));
						termFundTotal.setTotPlannedDisbAmount(mf.format(termFunds[i][3]));
						donorTermAssist.getTermFundTotal().add(termFundTotal);
						totComm=totComm + termFunds[i][0];
						totDisb=totDisb + termFunds[i][1];
						totExp=totExp + termFunds[i][2];
						totPlannedDisb=totPlannedDisb + termFunds[i][3];
						donorFunds[i][0]=donorFunds[i][0] + termFunds[i][0];
						donorFunds[i][1]=donorFunds[i][1] + termFunds[i][1];
						donorFunds[i][2]=donorFunds[i][2] + termFunds[i][2];
						donorFunds[i][3]=donorFunds[i][3] + termFunds[i][3];
					}
						
					donorTermAssist.setTotDonorCommAmount(mf.format(totComm));
					donorTermAssist.setTotDonorDisbAmount(mf.format(totDisb));
					donorTermAssist.setTotDonorExpAmount(mf.format(totExp));
					donorTermAssist.setTotDonorPlannedDisbAmount(mf.format(totPlannedDisb));
					donorTermAssist.setTotDonorUnDisbAmount(mf.format(actualCommitment-actualDisbursement));
					ampTeamDonors.getTotalDonorTermAssistFund().add(donorTermAssist);
					for(int i=0;i<=yrCount;i++)
					{
						if(donorTermAssist.getTermAssistName().equals("Loan"))
						{
							if(i<yrCount)
							{
								loanTeamFunds[i][0]=loanTeamFunds[i][0] + termFunds[i][0];
								loanTeamFunds[i][1]=loanTeamFunds[i][1] + termFunds[i][1];
								loanTeamFunds[i][2]=loanTeamFunds[i][2] + termFunds[i][2];
								loanTeamFunds[i][3]=loanTeamFunds[i][3] + termFunds[i][3];
							}
							if(i==yrCount)
							{
								loanTeamFunds[i][0]=loanTeamFunds[i][0] + totComm;
								loanTeamFunds[i][1]=loanTeamFunds[i][1] + totDisb;
								loanTeamFunds[i][2]=loanTeamFunds[i][2] + totExp;
								loanTeamFunds[i][3]=loanTeamFunds[i][3] + totPlannedDisb;
								loanTeamFunds[i][4]=loanTeamFunds[i][4] + (actualCommitment-actualDisbursement);
							}
						}
						if(donorTermAssist.getTermAssistName().equals("Grant"))
						{
							if(i<yrCount)
							{
								grantTeamFunds[i][0]=grantTeamFunds[i][0] + termFunds[i][0];
								grantTeamFunds[i][1]=grantTeamFunds[i][1] + termFunds[i][1];
								grantTeamFunds[i][2]=grantTeamFunds[i][2] + termFunds[i][2];
								grantTeamFunds[i][3]=grantTeamFunds[i][3] + termFunds[i][3];
							}
							if(i==yrCount)
							{
								grantTeamFunds[i][0]=grantTeamFunds[i][0] + totComm;
								grantTeamFunds[i][1]=grantTeamFunds[i][1] + totDisb;
								grantTeamFunds[i][2]=grantTeamFunds[i][2] + totExp;
								grantTeamFunds[i][3]=grantTeamFunds[i][3] + totPlannedDisb;
								grantTeamFunds[i][4]=grantTeamFunds[i][4] + (actualCommitment-actualDisbursement);
							}
						}
						if(donorTermAssist.getTermAssistName().equals("In Kind"))
						{
							if(i<yrCount)
							{
								kindTeamFunds[i][0]=kindTeamFunds[i][0] + termFunds[i][0];
								kindTeamFunds[i][1]=kindTeamFunds[i][1] + termFunds[i][1];
								kindTeamFunds[i][2]=kindTeamFunds[i][2] + termFunds[i][2];
								kindTeamFunds[i][3]=kindTeamFunds[i][3] + termFunds[i][3];
							}
							if(i==yrCount)
							{
								kindTeamFunds[i][0]=kindTeamFunds[i][0] + totComm;
								kindTeamFunds[i][1]=kindTeamFunds[i][1] + totDisb;
								kindTeamFunds[i][2]=kindTeamFunds[i][2] + totExp;
								kindTeamFunds[i][3]=kindTeamFunds[i][3] + totPlannedDisb;
								kindTeamFunds[i][4]=kindTeamFunds[i][4] + (actualCommitment-actualDisbursement);
							}
						}
					}
				}
				donorCommAmount=donorCommAmount + totComm;
				donorDisbAmount=donorDisbAmount + totDisb;
				donorExpAmount=donorExpAmount + totExp;
				donorPlannedDisbAmount=donorPlannedDisbAmount + totPlannedDisb;
				donorUnDisbAmount=donorUnDisbAmount + (actualCommitment-actualDisbursement);

				for(int i=0;i<yrCount;i++)
				{
					FundTotal total=new FundTotal();
					total.setTotCommAmount(mf.format(donorFunds[i][0]));
					total.setTotDisbAmount(mf.format(donorFunds[i][1]));
					total.setTotExpAmount(mf.format(donorFunds[i][2]));
					total.setTotPlannedDisbAmount(mf.format(donorFunds[i][3]));
					ampTeamDonors.getTotalDonorFund().add(total);	
					teamFunds[i][0]=teamFunds[i][0] + donorFunds[i][0];
					teamFunds[i][1]=teamFunds[i][1] + donorFunds[i][1];
					teamFunds[i][2]=teamFunds[i][2] + donorFunds[i][2];
					teamFunds[i][3]=teamFunds[i][3] + donorFunds[i][3];
				}
				ampTeamDonors.setDonorCommAmount(mf.format(donorCommAmount));
				ampTeamDonors.setDonorDisbAmount(mf.format(donorDisbAmount));
				ampTeamDonors.setDonorExpAmount(mf.format(donorExpAmount));
				ampTeamDonors.setDonorPlannedDisbAmount(mf.format(donorPlannedDisbAmount));
				ampTeamDonors.setDonorUnDisbAmount(mf.format(donorUnDisbAmount));
				report.getDonors().add(ampTeamDonors);
				teamCommAmount=teamCommAmount + donorCommAmount;
				teamDisbAmount=teamDisbAmount + donorDisbAmount;
				teamExpAmount=teamExpAmount + donorExpAmount;
				teamPlannedDisbAmount=teamPlannedDisbAmount + donorPlannedDisbAmount;
				teamUnDisbAmount=teamUnDisbAmount + donorUnDisbAmount;
				
				for(int i=0;i<yrCount;i++)
				{
					FundTotal total=new FundTotal();
					total.setTotCommAmount(mf.format(teamFunds[i][0]));
					total.setTotDisbAmount(mf.format(teamFunds[i][1]));
					total.setTotExpAmount(mf.format(teamFunds[i][2]));
					total.setTotPlannedDisbAmount(mf.format(teamFunds[i][3]));
					report.getTotalTeamFund().add(total);	
				}
				report.setTeamCommAmount(mf.format(teamCommAmount));
				report.setTeamDisbAmount(mf.format(teamDisbAmount));
				report.setTeamExpAmount(mf.format(teamExpAmount));
				report.setTeamPlannedDisbAmount(mf.format(teamPlannedDisbAmount));
				report.setTeamUnDisbAmount(mf.format(teamUnDisbAmount));
				Iterator teamIter=assistance.iterator();
				while(teamIter.hasNext())
				{
					String assist=(String) teamIter.next();
					logger.debug("Assist:" + assist + ":");
					TermFund termFund=new TermFund();
					if(assist.equals("Loan"))
					{
						termFund.setTermAssistName("Loan");
						termFund.setTermFundTotal(new ArrayList());
						for(int i=0;i<yrCount;i++)
						{
							TermFundTotal termFundTotal=new TermFundTotal();
							termFundTotal.setTotCommAmount(mf.format(loanTeamFunds[i][0]));
							termFundTotal.setTotDisbAmount(mf.format(loanTeamFunds[i][1]));
							termFundTotal.setTotExpAmount(mf.format(loanTeamFunds[i][2]));
							termFundTotal.setTotPlannedDisbAmount(mf.format(loanTeamFunds[i][3]));
							termFund.getTermFundTotal().add(termFundTotal);
						}
						termFund.setTotDonorCommAmount(mf.format(loanTeamFunds[yrCount][0]));
						termFund.setTotDonorDisbAmount(mf.format(loanTeamFunds[yrCount][1]));
						termFund.setTotDonorExpAmount(mf.format(loanTeamFunds[yrCount][2]));
						termFund.setTotDonorPlannedDisbAmount(mf.format(loanTeamFunds[yrCount][3]));
						termFund.setTotDonorUnDisbAmount(mf.format(loanTeamFunds[yrCount][4]));
					}
					if(assist.equals("Grant"))
					{
						termFund.setTermAssistName("Grant");
						termFund.setTermFundTotal(new ArrayList());
						for(int i=0;i<yrCount;i++)
						{
							TermFundTotal termFundTotal=new TermFundTotal();
							termFundTotal.setTotCommAmount(mf.format(grantTeamFunds[i][0]));
							termFundTotal.setTotDisbAmount(mf.format(grantTeamFunds[i][1]));
							termFundTotal.setTotExpAmount(mf.format(grantTeamFunds[i][2]));
							termFundTotal.setTotPlannedDisbAmount(mf.format(grantTeamFunds[i][3]));
							termFund.getTermFundTotal().add(termFundTotal);
						}
						termFund.setTotDonorCommAmount(mf.format(grantTeamFunds[yrCount][0]));
						termFund.setTotDonorDisbAmount(mf.format(grantTeamFunds[yrCount][1]));
						termFund.setTotDonorExpAmount(mf.format(grantTeamFunds[yrCount][2]));
						termFund.setTotDonorPlannedDisbAmount(mf.format(grantTeamFunds[yrCount][3]));
						termFund.setTotDonorUnDisbAmount(mf.format(grantTeamFunds[yrCount][4]));
					}
					if(assist.equals("In Kind"))
					{
						termFund.setTermAssistName("In Kind");
						termFund.setTermFundTotal(new ArrayList());
						for(int i=0;i<yrCount;i++)
						{
							TermFundTotal termFundTotal=new TermFundTotal();
							termFundTotal.setTotCommAmount(mf.format(kindTeamFunds[i][0]));
							termFundTotal.setTotDisbAmount(mf.format(kindTeamFunds[i][1]));
							termFundTotal.setTotExpAmount(mf.format(kindTeamFunds[i][2]));
							termFundTotal.setTotPlannedDisbAmount(mf.format(kindTeamFunds[i][3]));
							termFund.getTermFundTotal().add(termFundTotal);
						}
						termFund.setTotDonorCommAmount(mf.format(kindTeamFunds[yrCount][0]));
						termFund.setTotDonorDisbAmount(mf.format(kindTeamFunds[yrCount][1]));
						termFund.setTotDonorExpAmount(mf.format(kindTeamFunds[yrCount][2]));
						termFund.setTotDonorPlannedDisbAmount(mf.format(kindTeamFunds[yrCount][3]));
						termFund.setTotDonorUnDisbAmount(mf.format(kindTeamFunds[yrCount][4]));
					}
					logger.debug("Added:" + termFund.getTermAssistName());
					report.getTotalTeamTermAssistFund().add(termFund);	
				}
				ampReports.add(report);
			}	
		}
		catch(Exception ex) 		
		{
			logger.debug("Unable to get report names  from database " + ex.getMessage());
		}
		finally 
		{
			try 
			{
				PersistenceManager.releaseSession(session);
			}
			catch (Exception ex2) 
			{
				logger.debug("releaseSession() failed ");
			}
		}
		return ampReports ;
	}

	public static ArrayList getAmpReportViewProjects(Long ampTeamId,int fromYr,int toYr,String perspective,String ampCurrencyCode,Long ampModalityId,Long ampStatusId,Long ampDonorId,Long ampSectorId,int fiscalCalId,String startDate,String closeDate,String region)
	{
		Session session = null ;
		Query q = null ;
		ArrayList ampReports = new ArrayList() ;
		String queryString = null;
		Iterator iter=null;
		Long All=new Long(0);
		double actualCommitment=0;
		double actualDisbursement=0;
		int flag=0;
		int count=1;
		ArrayList donors=new ArrayList();
		ArrayList assistance=new ArrayList();
		double toExchangeRate=1.0;
		double fromExchangeRate=0.0;
		double amount=0.0;
		int fiscalYear=0,fiscalQuarter=0;
		int yrCount = 3;
		double[] disbFund=new double[4];
		double[] totDisbFund=new double[4];
		double totActualComm=0;
		double totActualDisb=0;
		double commitmentbreakup=0;
		double disbursementbreakup=0;
		double unDisbursedbreakup=0;
		int flagComm = 0;
		int flagDisb = 0;
		String inClause=null;
		Iterator iterSector=null;
		DecimalFormat mf = new DecimalFormat("###,###,###,###,###") ;
		ArrayList approvedActivityList=new ArrayList();
		AmpByAssistTypeList[][] actualTerms=new AmpByAssistTypeList[3][7];
		for(int i = 0; i < 3; i++)
			for(int ii = 0; ii < 7; ii++)
				actualTerms[i][ii] = new AmpByAssistTypeList();		

		try
		{
			int years=0;
			ArrayList dbReturnSet=(ArrayList)DbUtil.getAmpLevel0Teams(ampTeamId);				
			if(dbReturnSet.size()==0)
				inClause= "'" + ampTeamId + "'";
			else
			{
				iter=dbReturnSet.iterator();
				while(iter.hasNext())
				{
					Long teamId= (Long) iter.next();
					if(inClause==null)
						inClause="'" + teamId + "'";
					else
						inClause=inClause + ",'" + teamId + "'";
				}
			}
			approvedActivityList=DbUtil.getApprovedActivities(inClause);
			session = PersistenceManager.getSession();
			if(startDate==null && closeDate==null)
				queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.ampTeamId in(" + inClause + ")) and (report.reportType='1') order by report.activityName,report.ampActivityId,report.donorName";
			else
				queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.ampTeamId in(" + inClause + ")) and (report.actualStartDate='" + startDate + "' or report.actualCompletionDate='" + closeDate + "') and (report.reportType='1') order by report.activityName,report.ampActivityId,report.donorName";
			q = session.createQuery(queryString);	
			Report report=null;
			if(q!=null)
			{
				iter = q.list().iterator();
				while(iter.hasNext())
				{
					AmpReportCache ampReportCache = (AmpReportCache) iter.next(); 
					
					if(approvedActivityList.indexOf(ampReportCache.getAmpActivityId())==-1)
						continue;

					if(new Long(fiscalCalId).equals(Constants.ETH_FY) || new Long(fiscalCalId).equals(Constants.ETH_CAL))
					{
						if(ampReportCache.getTransactionDate()!=null)
						{
							GregorianCalendar calendar = new GregorianCalendar();
							EthiopianCalendar ec=new EthiopianCalendar();
							EthiopianCalendar tempDate=new EthiopianCalendar();
							calendar.setTime(ampReportCache.getTransactionDate());
							ec=tempDate.getEthiopianDate(calendar);
							if(new Long(fiscalCalId).equals(Constants.ETH_FY))
							{
								fiscalYear=(int)ec.ethFiscalYear;
								fiscalQuarter=(int)ec.ethFiscalQrt;
							}
							if(new Long(fiscalCalId).equals(Constants.ETH_CAL))
							{
								fiscalYear=(int)ec.ethYear;
								fiscalQuarter=(int)ec.ethQtr;
							}
						}
					}
					if(!ampModalityId.equals(All))
					{
						if(ampReportCache.getAmpModalityId()==null)
							continue;
						if(!(ampModalityId.equals(ampReportCache.getAmpModalityId())))
							continue;
					}

					if(!ampStatusId.equals(All))
					{
						if(ampReportCache.getAmpStatusId()==null)
							continue;
						if(!(ampStatusId.equals(ampReportCache.getAmpStatusId())))
							continue;
					}
	
					if(!ampDonorId.equals(All))
					{
						if(ampReportCache.getAmpDonorId()==null)
							continue;
						if(!(ampDonorId.equals(ampReportCache.getAmpDonorId())))
							continue;
					}
		
					if(!ampSectorId.equals(All))
					{
						int sflag=0;
						iterSector=DbUtil.getAmpReportSectorId(ampReportCache.getAmpActivityId()).iterator();
						while(iterSector.hasNext())
						{
							AmpReportSector sector=(AmpReportSector) iterSector.next();
							if(sector.getAmpSectorId().equals(ampSectorId))
							{
								sflag=1;
								break;
							}
							if(sector.getAmpSubSectorId().equals(new Long(0)))
							{
								if(new Long(sector.getSubSectorName()).equals(ampSectorId))
								{
									sflag=1;
									break;
								}
							}
							if(!(sector.getAmpSubSectorId().equals(new Long(0))) && sector.getAmpSubSectorId().equals(ampSectorId))
							{
								sflag=1;
								break;
							}
						}
						if (sflag==0)
						{
							continue;
						}
					}
					
					if(!region.equals("All"))
					{
						ArrayList location=(ArrayList)DbUtil.getAmpReportLocation(ampReportCache.getAmpActivityId());
						if(location.indexOf(region)==-1)
							continue;
					}
					
					if(report==null || !(report.getAmpActivityId().equals(ampReportCache.getAmpActivityId())))
					{
						if(report!=null)
						{
							for(int i=0;i<4;i++)
							{
								AmpFund ampFund=new AmpFund();
								ampFund.setDisbAmount(mf.format(disbFund[i]));
								totDisbFund[i]=totDisbFund[i] + disbFund[i];
								report.getAmpFund().add(ampFund);
							}
						
							for(int i=0; i<3; i++)
							{
								AmpFund ampFund = new AmpFund();
								if(!(actualTerms[i][0].equals(null)))
								{
								ampFund.setByTypeComm(actualTerms[i][0]);
								ampFund.setByTypeDisb(actualTerms[i][1]);
								ampFund.setByTypeUnDisb(actualTerms[i][2]);
								ampFund.setByTermsPlDisbForecast1(actualTerms[i][3]);
								ampFund.setByTermsPlDisbForecast2(actualTerms[i][4]);
								ampFund.setByTermsPlDisbForecast3(actualTerms[i][5]);
								ampFund.setByTermsPlDisbForecast4(actualTerms[i][6]);
								report.getAmpFund().add(ampFund);
								}
							}
							
							if(donors.size()==0)
								report.getDonors().add("Unspecified");
							else
								report.getDonors().addAll(donors);
							if(assistance.size()==0)
								report.getAssistance().add("Unspecified");
							else
								report.getAssistance().addAll(assistance);
							report.setAcCommitment(mf.format(actualCommitment));
							report.setAcDisbursement(mf.format(actualDisbursement));
							double undisbursed = actualCommitment - actualDisbursement;
							report.setAcUnDisbursement(mf.format(undisbursed));
							actualCommitment=0;
							actualDisbursement=0;
							donors.clear();
							assistance.clear();
							for(int i=0;i<=yrCount;i++)
								disbFund[i]=0;
							for(int i = 0; i < 3; i++)
								for(int ii = 0; ii < 7; ii++)
									actualTerms[i][ii] = new AmpByAssistTypeList();
							flagComm = 0;
							flagDisb = 0;							
							ampReports.add(report);
						}
						report=new Report();
						report.setDonors(new ArrayList());
						report.setSectors(new ArrayList());
						report.setRegions(new ArrayList());
						report.setAmpFund(new ArrayList());
						report.setAssistance(new ArrayList());
						report.setTitle(ampReportCache.getActivityName());
						report.setAmpActivityId(ampReportCache.getAmpActivityId());
						if(ampReportCache.getLevelName().equals("Not Exist"))
							report.setLevel("Unspecified");
						else
							report.setLevel(ampReportCache.getLevelName());
						if(ampReportCache.getStatusName()!=null)
							report.setStatus(ampReportCache.getStatusName());
						if(DbUtil.getAmpReportSector(ampReportCache.getAmpActivityId()).size()==0)
							report.getSectors().add("Unspecified");
						else
							report.getSectors().addAll(DbUtil.getAmpReportSector(ampReportCache.getAmpActivityId()));
						if(DbUtil.getAmpReportLocation(ampReportCache.getAmpActivityId()).size()==0)
							report.getRegions().add("Unspecified");
						else
							report.getRegions().addAll(DbUtil.getAmpReportLocation(ampReportCache.getAmpActivityId()));
						if(ampReportCache.getTermAssistName()!=null)
							assistance.add(ampReportCache.getTermAssistName());
						if(ampReportCache.getDonorName()!=null)
							donors.add(ampReportCache.getDonorName());
						else
							donors.addAll(DbUtil.getAmpDonorsForActivity(ampReportCache.getAmpActivityId()));
						if(ampReportCache.getActualStartDate()!=null)
							report.setStartDate(DateConversion.ConvertDateToString(ampReportCache.getActualStartDate()));
						else
							report.setStartDate("");
						if(ampReportCache.getActualCompletionDate()!=null)
							report.setCloseDate(DateConversion.ConvertDateToString(ampReportCache.getActualCompletionDate()));
						else
							report.setCloseDate("");
					}
					if(donors.indexOf(ampReportCache.getDonorName())==-1 && ampReportCache.getDonorName()!=null)
						donors.add(ampReportCache.getDonorName());
					if(assistance.indexOf(ampReportCache.getTermAssistName())==-1 && ampReportCache.getTermAssistName()!=null)
						assistance.add(ampReportCache.getTermAssistName());
					if(ampReportCache.getFiscalYear()!=null && ampReportCache.getFiscalQuarter()!=null)
					{
						if(new Long(fiscalCalId).equals(Constants.GREGORIAN))
						{
							fiscalYear=ampReportCache.getFiscalYear().intValue();
							fiscalQuarter=ampReportCache.getFiscalQuarter().intValue();
						}
						if(ampReportCache.getCurrencyCode().equals("USD"))
							fromExchangeRate=1.0;
						else
							fromExchangeRate=DbUtil.getExchangeRate(ampReportCache.getCurrencyCode(),Constants.PLANNED,ampReportCache.getTransactionDate());
						if(ampCurrencyCode.equals("USD"))
							toExchangeRate=1.0;
						else
							toExchangeRate=DbUtil.getExchangeRate(ampCurrencyCode,Constants.PLANNED,ampReportCache.getTransactionDate());
						if(fiscalYear>=toYr && fiscalYear<=(toYr+3))
						{						
							if(ampReportCache.getPlannedDisbursement().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
							{
								amount=CurrencyWorker.convert1(ampReportCache.getPlannedDisbursement().doubleValue(),fromExchangeRate,toExchangeRate);
								AmpByAssistTypeAmount abt=new AmpByAssistTypeAmount(ampReportCache.getTermAssistName(),amount);
								if(ampReportCache.getTermAssistName().equals("Grant"))
									actualTerms[0][fiscalYear%toYr+3].add(abt);
								else if(ampReportCache.getTermAssistName().equals("Loan"))
									actualTerms[1][fiscalYear%toYr+3].add(abt);
								else if(ampReportCache.getTermAssistName().equals("In Kind"))
									actualTerms[2][fiscalYear%toYr+3].add(abt);
							}
							
							if(disbFund[fiscalYear%toYr]==0)
								disbFund[fiscalYear%toYr]=amount;
							else
							if(disbFund[fiscalYear%toYr]>0)
								disbFund[fiscalYear%toYr]=disbFund[fiscalYear%toYr] + amount;
							amount=0.0;

						}
						if(ampReportCache.getPerspective().equals(perspective))
						{
							if(ampReportCache.getCurrencyCode().equals("USD"))
								fromExchangeRate=1.0;
							else
								fromExchangeRate=DbUtil.getExchangeRate(ampReportCache.getCurrencyCode(),Constants.ACTUAL,ampReportCache.getTransactionDate());
							if(ampCurrencyCode.equals("USD"))
								toExchangeRate=1.0;
							else
								toExchangeRate=DbUtil.getExchangeRate(ampCurrencyCode,Constants.ACTUAL,ampReportCache.getTransactionDate());

							commitmentbreakup = 0;
							commitmentbreakup = CurrencyWorker.convert1(ampReportCache.getActualCommitment().doubleValue(),fromExchangeRate,toExchangeRate);
							if(commitmentbreakup != 0)
							{
								AmpByAssistTypeAmount abt=new AmpByAssistTypeAmount(ampReportCache.getTermAssistName(),commitmentbreakup);
								
								if(ampReportCache.getTermAssistName().equals("Grant"))
									actualTerms[0][0].add(abt);
								else if(ampReportCache.getTermAssistName().equals("Loan"))
									actualTerms[1][0].add(abt);
								else if(ampReportCache.getTermAssistName().equals("In Kind"))
									actualTerms[2][0].add(abt);
								else
								{
									AmpByAssistTypeAmount abt1=new AmpByAssistTypeAmount("Unspecified",commitmentbreakup);
									actualTerms[0][0].add(abt1);
								}
									
							}
							actualCommitment=actualCommitment +	commitmentbreakup;
							totActualComm=totActualComm + actualCommitment;

							disbursementbreakup = 0;
							disbursementbreakup = CurrencyWorker.convert1(ampReportCache.getActualDisbursement().doubleValue(),fromExchangeRate,toExchangeRate);
							if(disbursementbreakup != 0)
							{
								AmpByAssistTypeAmount abt=new AmpByAssistTypeAmount(ampReportCache.getTermAssistName(),disbursementbreakup);
								if(ampReportCache.getTermAssistName().equals("Grant"))
									actualTerms[0][1].add(abt);
								else if(ampReportCache.getTermAssistName().equals("Loan"))
									actualTerms[1][1].add(abt);
								else if(ampReportCache.getTermAssistName().equals("In Kind"))
									actualTerms[2][1].add(abt);
								else
								{
									AmpByAssistTypeAmount abt1=new AmpByAssistTypeAmount("Unspecified",commitmentbreakup);
									actualTerms[0][1].add(abt1);
								}
							}
							actualDisbursement=actualDisbursement + disbursementbreakup;							
							totActualDisb = totActualDisb + actualDisbursement;
						
							unDisbursedbreakup = 0;
							unDisbursedbreakup = commitmentbreakup - disbursementbreakup;
							if(unDisbursedbreakup != 0)
							{
								AmpByAssistTypeAmount abt=new AmpByAssistTypeAmount(ampReportCache.getTermAssistName(),unDisbursedbreakup);
								if(ampReportCache.getTermAssistName().equals("Grant"))
									actualTerms[0][2].add(abt);
								else if(ampReportCache.getTermAssistName().equals("Loan"))
									actualTerms[1][2].add(abt);
								else if(ampReportCache.getTermAssistName().equals("In Kind"))
									actualTerms[2][2].add(abt);
								else
								{
									AmpByAssistTypeAmount abt1=new AmpByAssistTypeAmount("Unspecified",commitmentbreakup);
									actualTerms[0][2].add(abt1);
								}	
							}
						}
					}
				}
				for(int i=0;i<4;i++)
				{
					AmpFund ampFund=new AmpFund();
					ampFund.setDisbAmount(mf.format(disbFund[i]));
					totDisbFund[i]=totDisbFund[i] + disbFund[i];
					report.getAmpFund().add(ampFund);
				}
				
				for(int i=0; i<3; i++)
				{
					AmpFund ampFund = new AmpFund();
					ampFund.setByTypeComm(actualTerms[i][0]);
					ampFund.setByTypeDisb(actualTerms[i][1]);
					ampFund.setByTypeUnDisb(actualTerms[i][2]);
					ampFund.setByTermsPlDisbForecast1(actualTerms[i][3]);
					ampFund.setByTermsPlDisbForecast2(actualTerms[i][4]);
					ampFund.setByTermsPlDisbForecast3(actualTerms[i][5]);
					ampFund.setByTermsPlDisbForecast4(actualTerms[i][6]);
					report.getAmpFund().add(ampFund);
				}
				
				if(donors.size()==0)
					report.getDonors().add("Unspecified");
				else
					report.getDonors().addAll(donors);
				if(assistance.size()==0)
					report.getAssistance().add("Unspecified");
				else
					report.getAssistance().addAll(assistance);
				report.setAcCommitment(mf.format(actualCommitment));
				report.setAcDisbursement(mf.format(actualDisbursement));
				double undisbursed = actualCommitment - actualDisbursement;
				report.setAcUnDisbursement(mf.format(undisbursed));
				ampReports.add(report);
			}
		}
		catch(Exception ex) 		
		{
			logger.info("Unable to get report names  from database " + ex.getMessage());
		}
		finally 
		{
			try 
			{
				PersistenceManager.releaseSession(session);
			}
			catch (Exception ex2) 
			{
				logger.debug("releaseSession() failed ");
			}
		}
		return ampReports ;
	}
	// annual forecasting ends here

	public static ArrayList getAmpReportQuarterlyDateRange(Long ampTeamId,int fromYr,int toYr,String perspective,String ampCurrencyCode,Long ampModalityId,Long ampStatusId,Long ampDonorId,Long ampSectorId,int fiscalCalId,String startDate,String closeDate,String region,int ampAdjustmentId)
	{
		Session session = null ;
		Query q = null ;
		ArrayList ampReports = new ArrayList() ;
		ArrayList donors=new ArrayList();
		String queryString = null;
		Iterator iter=null;
		Long All=new Long(0);
		double actualCommitment=0;
		double actualDisbursement=0;
		int flag=0;
		int count=1;
		double toExchangeRate=1.0;
		double fromExchangeRate=0.0;
		double amount=0.0;
		double projCommAmount=0.0;
		double projDisbAmount=0.0;
		double projExpAmount=0.0;
		String inClause=null;
		Iterator iterSector=null;
		DecimalFormat mf = new DecimalFormat("###,###,###,###,###") ;

		try
		{
			int yrCount = (toYr - fromYr)+1;
			double[][] commFund=new double[yrCount][4];
			double[][] disbFund=new double[yrCount][4];
			int fiscalYear=0,fiscalQuarter=0;
			ArrayList dbReturnSet=(ArrayList)DbUtil.getAmpLevel0Teams(ampTeamId);				
			if(dbReturnSet.size()==0)
				inClause= "'" + ampTeamId + "'";
			else
			{
				iter=dbReturnSet.iterator();
				while(iter.hasNext())
				{
					Long teamId= (Long) iter.next();
					if(inClause==null)
						inClause="'" + teamId + "'";
					else
						inClause=inClause + ",'" + teamId + "'";
				}
			}
			logger.debug("Inclause: " + inClause);

			session = PersistenceManager.getSession();
			if(startDate==null && closeDate==null)
				queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.ampTeamId in(" + inClause + ")) and (report.reportType='1') order by report.activityName,report.ampActivityId,report.donorName,report.fiscalYear,report.fiscalQuarter";
			else
				queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.ampTeamId in(" + inClause + ")) and (report.actualStartDate='" + startDate + "' or report.actualCompletionDate='" + closeDate + "') and (report.reportType='1') order by report.activityName,report.ampActivityId,report.donorName,report.fiscalYear,report.fiscalQuarter";
			logger.debug("querystring: " + queryString);
			q = session.createQuery(queryString);	
//			q.setParameter("ampTeamId",ampTeamId,Hibernate.LONG) ;
			Report report=null;
			if(q!=null)
			{
				iter = q.list().iterator();
				while(iter.hasNext())
				{
					AmpReportCache ampReportCache = (AmpReportCache) iter.next(); 

					if(new Long(fiscalCalId).equals(Constants.ETH_FY) || new Long(fiscalCalId).equals(Constants.ETH_CAL))
					{
						if(ampReportCache.getTransactionDate()!=null)
						{
							GregorianCalendar calendar = new GregorianCalendar();
							EthiopianCalendar ec=new EthiopianCalendar();
							EthiopianCalendar tempDate=new EthiopianCalendar();
							calendar.setTime(ampReportCache.getTransactionDate());
							ec=tempDate.getEthiopianDate(calendar);
							if(new Long(fiscalCalId).equals(Constants.ETH_FY))
							{
								fiscalYear=(int)ec.ethFiscalYear;
								fiscalQuarter=(int)ec.ethFiscalQrt;
							}
							if(new Long(fiscalCalId).equals(Constants.ETH_CAL))
							{
								fiscalYear=(int)ec.ethYear;
								fiscalQuarter=(int)ec.ethQtr;
							}
							logger.debug("Ethiopian Fiscal Year: " + fiscalYear);
							logger.debug("From Year: " + fromYr);
							logger.debug("From Year: " + toYr);
							if(fiscalYear<fromYr || fiscalYear>toYr)
								continue;
						}
					}

					if(!ampModalityId.equals(All))
					{
						if(ampReportCache.getAmpModalityId()==null)
							continue;
						if(!(ampModalityId.equals(ampReportCache.getAmpModalityId())))
							continue;
					}

					if(!ampStatusId.equals(All))
					{
						if(ampReportCache.getAmpStatusId()==null)
							continue;
						if(!(ampStatusId.equals(ampReportCache.getAmpStatusId())))
							continue;
					}
	
					if(!ampDonorId.equals(All))
					{
						if(ampReportCache.getAmpDonorId()==null)
							continue;
						if(!(ampDonorId.equals(ampReportCache.getAmpDonorId())))
							continue;
					}
		
					if(!ampSectorId.equals(All))
						{
							int sflag=0;
							logger.debug("Selected Sector Id: " + ampSectorId);
							iterSector=DbUtil.getAmpReportSectorId(ampReportCache.getAmpActivityId()).iterator();
							while(iterSector.hasNext())
							{
								AmpReportSector sector=(AmpReportSector) iterSector.next();
								
								if(sector.getAmpSectorId().equals(ampSectorId) || new Long(sector.getSubSectorName()).equals(ampSectorId))
								{
									logger.debug("Condition true");
									sflag=1;
									break;
								}
							}
							if (sflag==0)
							{
								continue;
							}
						}
					
					if(!region.equals("All"))
					{
						ArrayList location=(ArrayList)DbUtil.getAmpReportLocation(ampReportCache.getAmpActivityId());
						if(location.indexOf(region)==-1)
							continue;
					}
					
					if(report==null || !(report.getTitle().equals(ampReportCache.getActivityName())))
					{
						if(report!=null)
						{
							for(int i=0;i<yrCount;i++)
							{
								for(int qtr=0;qtr<4;qtr++)
								{
									AmpFund ampFund=new AmpFund();
									ampFund.setCommAmount(mf.format(commFund[i][qtr]));
									ampFund.setDisbAmount(mf.format(disbFund[i][qtr]));
									report.getAmpFund().add(ampFund);
								}
							}
							if(donors.size()==0)
								report.getDonors().add("Unspecified");
							else
								report.getDonors().addAll(donors);
							ampReports.add(report);
							donors.clear();
							for(int i=0;i<yrCount;i++)
							{
								for(int qtr=0;qtr<4;qtr++)
								{
									commFund[i][qtr]=0;
									disbFund[i][qtr]=0;
								}
							}
						}
						report=new Report();
						report.setTitle(ampReportCache.getActivityName());
						report.setDonors(new ArrayList());
						report.setSectors(new ArrayList());
						report.setAmpFund(new ArrayList());
						if(ampReportCache.getStatusName()!=null)
							report.setStatus(ampReportCache.getStatusName());
						if(DbUtil.getAmpReportSector(ampReportCache.getAmpActivityId()).size()==0)
							report.getSectors().add("Unspecified");
						else
							report.getSectors().addAll(DbUtil.getAmpReportSector(ampReportCache.getAmpActivityId()));
						if(ampReportCache.getDonorName()!=null)
							donors.add(ampReportCache.getDonorName());
						else
							donors.addAll(DbUtil.getAmpDonorsForActivity(ampReportCache.getAmpActivityId()));
						if(ampReportCache.getActualStartDate()!=null)
							report.setStartDate(DateConversion.ConvertDateToString(ampReportCache.getActualStartDate()));
						else
							report.setStartDate("");
						if(ampReportCache.getActualCompletionDate()!=null)
							report.setCloseDate(DateConversion.ConvertDateToString(ampReportCache.getActualCompletionDate()));
						else
							report.setCloseDate("");
						report.setFiscalYrs(new ArrayList());
						report.setAmpFund(new ArrayList());
						count++;
					}
					if(donors.indexOf(ampReportCache.getDonorName())==-1 && ampReportCache.getDonorName()!=null)
						donors.add(ampReportCache.getDonorName());
					
					if(ampReportCache.getFiscalYear()!=null && ampReportCache.getFiscalQuarter()!=null)
					{
						if(new Long(fiscalCalId).equals(Constants.GREGORIAN))
						{
							fiscalYear=ampReportCache.getFiscalYear().intValue();
							fiscalQuarter=ampReportCache.getFiscalQuarter().intValue();
						}
						if(ampAdjustmentId==Constants.ACTUAL)
						{
							if(ampReportCache.getCurrencyCode().equals("USD"))
								fromExchangeRate=1.0;
							else
								fromExchangeRate=DbUtil.getExchangeRate(ampReportCache.getCurrencyCode(),Constants.ACTUAL,ampReportCache.getTransactionDate());
							if(ampCurrencyCode.equals("USD"))
								toExchangeRate=1.0;
							else
								toExchangeRate=DbUtil.getExchangeRate(ampCurrencyCode,Constants.ACTUAL,ampReportCache.getTransactionDate());
							if(ampReportCache.getActualCommitment().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
							{
								amount=CurrencyWorker.convert1(ampReportCache.getActualCommitment().doubleValue(),fromExchangeRate,toExchangeRate);
								if(fiscalYear>=fromYr && fiscalYear<=toYr)
								{
						//			projCommAmount=projCommAmount + amount;
									if(commFund[fiscalYear%fromYr][fiscalQuarter-1]==0)
										commFund[fiscalYear%fromYr][fiscalQuarter-1]=amount;
									else
									if(commFund[fiscalYear%fromYr][fiscalQuarter-1]>0)
										commFund[fiscalYear%fromYr][fiscalQuarter-1]=commFund[fiscalYear%fromYr][fiscalQuarter-1] + amount;
								}
							}
							amount=0.0;
							if(ampReportCache.getActualDisbursement().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
							{
								amount=CurrencyWorker.convert1(ampReportCache.getActualDisbursement().doubleValue(),fromExchangeRate,toExchangeRate);
								if(fiscalYear>=fromYr && fiscalYear<=toYr)
								{
					//			projDisbAmount=projDisbAmount+amount;
									if(disbFund[fiscalYear%fromYr][fiscalQuarter-1]==0)
										disbFund[fiscalYear%fromYr][fiscalQuarter-1]=amount;
									else
									if(disbFund[fiscalYear%fromYr][fiscalQuarter-1]>0)
										disbFund[fiscalYear%fromYr][fiscalQuarter-1]=disbFund[fiscalYear%fromYr][fiscalQuarter-1] + amount;
								}
							}		
							amount=0.0;
						}
						else
						{
							if(ampReportCache.getCurrencyCode().equals("USD"))
								fromExchangeRate=1.0;
							else
								fromExchangeRate=DbUtil.getExchangeRate(ampReportCache.getCurrencyCode(),Constants.PLANNED,ampReportCache.getTransactionDate());
							if(ampCurrencyCode.equals("USD"))
								toExchangeRate=1.0;
							else
								toExchangeRate=DbUtil.getExchangeRate(ampCurrencyCode,Constants.PLANNED,ampReportCache.getTransactionDate());
							if(ampReportCache.getPlannedCommitment().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
							{
								amount=CurrencyWorker.convert1(ampReportCache.getPlannedCommitment().doubleValue(),fromExchangeRate,toExchangeRate);
								if(fiscalYear>=fromYr && fiscalYear<=toYr)
								{
					//			projCommAmount=projCommAmount + amount;
									if(commFund[fiscalYear%fromYr][fiscalQuarter-1]==0)
										commFund[fiscalYear%fromYr][fiscalQuarter-1]=amount;
									else
									if(commFund[fiscalYear%fromYr][fiscalQuarter-1]>0)
										commFund[fiscalYear%fromYr][fiscalQuarter-1]=commFund[fiscalYear%fromYr][fiscalQuarter-1] + amount;
								}
							}	
							amount=0.0;

							if(ampReportCache.getPlannedDisbursement().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
							{
								amount=CurrencyWorker.convert1(ampReportCache.getPlannedDisbursement().doubleValue(),fromExchangeRate,toExchangeRate);
								if(fiscalYear>=fromYr && fiscalYear<=toYr)
								{
					//			projDisbAmount=projDisbAmount+amount;
									if(disbFund[fiscalYear%fromYr][fiscalQuarter-1]==0)
										disbFund[fiscalYear%fromYr][fiscalQuarter-1]=amount;
									else
									if(disbFund[fiscalYear%fromYr][fiscalQuarter-1]>0)
										disbFund[fiscalYear%fromYr][fiscalQuarter-1]=disbFund[fiscalYear%fromYr][fiscalQuarter-1] + amount;
								}	
							}
							amount=0.0;

							
						}
					}
				}
				for(int i=0;i<yrCount;i++)
				{
					for(int qtr=0;qtr<4;qtr++)
					{
						AmpFund ampFund=new AmpFund();
						ampFund.setCommAmount(mf.format(commFund[i][qtr]));
						ampFund.setDisbAmount(mf.format(disbFund[i][qtr]));
						report.getAmpFund().add(ampFund);
					}
				}
				if(donors.size()==0)
					report.getDonors().add("Unspecified");
				else
					report.getDonors().addAll(donors);
				ampReports.add(report);
			}			
		}
		catch(Exception ex) 		
		{
			logger.debug("Unable to get report names  from database " + ex.getMessage());
		}
		finally 
		{
			try 
			{
				PersistenceManager.releaseSession(session);
			}
			catch (Exception ex2) 
			{
				logger.debug("releaseSession() failed ");
			}
		}
		return ampReports ;
	}

	public static ArrayList getAmpReportViewPlannedProjects(Long ampTeamId,int fromYr,int toYr,String perspective,String ampCurrencyCode,Long ampModalityId,Long ampStatusId,Long ampDonorId,Long ampSectorId,int fiscalCalId,String startDate,String closeDate,String region)
	{
		Session session = null ;
		Query q = null ;
		ArrayList ampReports = new ArrayList() ;
		ArrayList donors=new ArrayList();
		ArrayList commDate=new ArrayList();		
		ArrayList modality=new ArrayList();	
		ArrayList assistance = new ArrayList() ;
		String queryString = null;
		Iterator iter=null;
		Long All=new Long(0);
		double actualCommitment=0;
		double toExchangeRate=1.0;
		double fromExchangeRate=0.0;
		double amount=0.0;
		double plannedDisbAmount = 0.0 ;
		double disbAmount = 0.0 ;
		double expAmount = 0.0 ;
		double projComm = 0.0 ;
		
		int yrCount = 3;

		double[][] disbFund=new double[yrCount][4];
		double[][] plDisbFund=new double[yrCount][4];
		double[][] expFund=new double[yrCount][4];

		int fiscalYear=0,fiscalQuarter=0;
		String inClause=null;
		Iterator iterSector=null;
		DecimalFormat mf = new DecimalFormat("###,###,###,###,###") ;
		ArrayList approvedActivityList=new ArrayList();
				
		try
		{
			ArrayList dbReturnSet=(ArrayList)DbUtil.getAmpLevel0Teams(ampTeamId);				
			if(dbReturnSet.size()==0)
				inClause= "'" + ampTeamId + "'";
			else
			{
				iter=dbReturnSet.iterator();
				while(iter.hasNext())
				{
					Long teamId= (Long) iter.next();
					if(inClause==null)
						inClause="'" + teamId + "'";
					else
						inClause=inClause + ",'" + teamId + "'";
				}
			}
			logger.debug("Inclause: " + inClause);		
			approvedActivityList=DbUtil.getApprovedActivities(inClause);

			session = PersistenceManager.getSession();
			if(startDate==null && closeDate==null)
				queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.ampTeamId in(" + inClause + ")) and (report.reportType='1') order by report.activityName,report.ampActivityId,report.donorName,report.fiscalYear,report.fiscalQuarter";
			else
				queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.ampTeamId in(" + inClause + ")) and (report.actualStartDate='" + startDate + "' or report.actualCompletionDate='" + closeDate + "') and (report.reportType='1') order by report.activityName,report.ampActivityId,report.donorName,report.fiscalYear,report.fiscalQuarter";
			logger.debug("querystring: " + queryString);
			q = session.createQuery(queryString);	
//			q.setParameter("ampTeamId",ampTeamId,Hibernate.LONG) ;
			Report report=null;
			if(q!=null)
			{
				iter = q.list().iterator();
				while(iter.hasNext())
				{
					AmpReportCache ampReportCache = (AmpReportCache) iter.next(); 

					if(approvedActivityList.indexOf(ampReportCache.getAmpActivityId())==-1)
						continue;

					if(new Long(fiscalCalId).equals(Constants.ETH_FY) || new Long(fiscalCalId).equals(Constants.ETH_CAL))
					{
						if(ampReportCache.getTransactionDate()!=null)
						{
							GregorianCalendar calendar = new GregorianCalendar();
							EthiopianCalendar ec=new EthiopianCalendar();
							EthiopianCalendar tempDate=new EthiopianCalendar();
							calendar.setTime(ampReportCache.getTransactionDate());
							ec=tempDate.getEthiopianDate(calendar);
							if(new Long(fiscalCalId).equals(Constants.ETH_FY))
							{
								fiscalYear=(int)ec.ethFiscalYear;
								fiscalQuarter=(int)ec.ethFiscalQrt;
							}
							if(new Long(fiscalCalId).equals(Constants.ETH_CAL))
							{
								fiscalYear=(int)ec.ethYear;
								fiscalQuarter=(int)ec.ethQtr;
							}
							logger.debug("Ethiopian Fiscal Year: " + fiscalYear);
							logger.debug("From Year: " + fromYr);
							logger.debug("From Year: " + toYr);
/*							if(fiscalYear<fromYr || fiscalYear>toYr)
								continue;*/
						}
					}

					if(!ampModalityId.equals(All))
					{
						if(ampReportCache.getAmpModalityId()==null)
							continue;
						if(!(ampModalityId.equals(ampReportCache.getAmpModalityId())))
							continue;
					}

					if(!ampStatusId.equals(All))
					{
						if(ampReportCache.getAmpStatusId()==null)
							continue;
						if(!(ampStatusId.equals(ampReportCache.getAmpStatusId())))
							continue;
					}
	
					if(!ampDonorId.equals(All))
					{
						if(ampReportCache.getAmpDonorId()==null)
							continue;
						if(!(ampDonorId.equals(ampReportCache.getAmpDonorId())))
							continue;
					}
		
					if(!ampSectorId.equals(All))
					{
						int sflag=0;
						iterSector=DbUtil.getAmpReportSectorId(ampReportCache.getAmpActivityId()).iterator();
						while(iterSector.hasNext())
						{
							AmpReportSector sector=(AmpReportSector) iterSector.next();
							if(sector.getAmpSectorId().equals(ampSectorId))
							{
								sflag=1;
								break;
							}
							if(sector.getAmpSubSectorId().equals(new Long(0)))
							{
								if(new Long(sector.getSubSectorName()).equals(ampSectorId))
								{
									sflag=1;
									break;
								}
							}
							if(!(sector.getAmpSubSectorId().equals(new Long(0))) && sector.getAmpSubSectorId().equals(ampSectorId))
							{
								sflag=1;
								break;
							}
						}
						if (sflag==0)
						{
							continue;
						}
					}
					
					if(!region.equals("All"))
					{
						ArrayList location=(ArrayList)DbUtil.getAmpReportLocation(ampReportCache.getAmpActivityId());
						if(location.indexOf(region)==-1)
							continue;
					}
					
					if(report==null || !(report.getAmpActivityId().equals(ampReportCache.getAmpActivityId())))
					{
						if(report!=null)
						{
							for(int i=0;i<yrCount;i++)
							{
								for(int qtr=0;qtr<4;qtr++)
								{
									AmpFund ampFund = new AmpFund();
									ampFund.setPlannedDisbAmount(mf.format(plDisbFund[i][qtr])); 
									ampFund.setDisbAmount(mf.format(disbFund[i][qtr])); 
									ampFund.setExpAmount(mf.format(expFund[i][qtr]));	
									plannedDisbAmount=plannedDisbAmount+ plDisbFund[i][qtr];
					//				disbAmount=disbAmount+ projFund[i][1];
									expAmount=expAmount+ expFund[i][qtr];
									report.getAmpFund().add(ampFund) ;
								}
							}	
							AmpFund ampFund = new AmpFund();
							ampFund.setPlannedDisbAmount(mf.format(plannedDisbAmount)); 
							ampFund.setDisbAmount(mf.format(disbAmount));
							ampFund.setExpAmount(mf.format(expAmount));
							ampFund.setUnDisbAmount(mf.format(projComm - disbAmount));
							report.getAmpFund().add(ampFund) ;	
							report.setAcCommitment(mf.format(actualCommitment));
						
							if(donors.size()==0)
								report.getDonors().add("Unspecified");
							else
								report.getDonors().addAll(donors);
							if(assistance.size()==0)
								report.getAssistance().add("Unspecified");
							else
								report.getAssistance().addAll(assistance);

							if(commDate.size()==0)
								report.getCommitmentDate().add("Unspecified");
							else
								report.getCommitmentDate().addAll(commDate);

							if(modality.size()==0)
								report.getModality().add("Unspecified");
							else
								report.getModality().addAll(modality);
							ampReports.add(report);
							for(int i=0;i<yrCount;i++)
							{
								for(int qtr=0;qtr<4;qtr++)
								{
									disbFund[i][qtr]=0;
									expFund[i][qtr]=0;
									plDisbFund[i][qtr]=0;
								}
							}
							donors.clear();
							assistance.clear();
							commDate.clear();
							modality.clear();
							actualCommitment=0.0;
							plannedDisbAmount=disbAmount=expAmount=projComm=0.0;
						}
						report=new Report();
						report.setDonors(new ArrayList());
						report.setSectors(new ArrayList());
						report.setRegions(new ArrayList());
						report.setAmpFund(new ArrayList());
						report.setAssistance(new ArrayList());
						report.setModality(new ArrayList());
						report.setTitle(ampReportCache.getActivityName());
						report.setAmpActivityId(ampReportCache.getAmpActivityId());
						if(ampReportCache.getLevelName().equals("Not Exist"))
							report.setLevel("Unspecified");
						else
							report.setLevel(ampReportCache.getLevelName());
						if(ampReportCache.getStatusName()!=null)
							report.setStatus(ampReportCache.getStatusName());
						if(DbUtil.getAmpReportSector(ampReportCache.getAmpActivityId()).size()==0)
							report.getSectors().add("Unspecified");
						else
							report.getSectors().addAll(DbUtil.getAmpReportSector(ampReportCache.getAmpActivityId()));
						if(DbUtil.getAmpReportLocation(ampReportCache.getAmpActivityId()).size()==0)
							report.getRegions().add("Unspecified");
						else
							report.getRegions().addAll(DbUtil.getAmpReportLocation(ampReportCache.getAmpActivityId()));
						if(ampReportCache.getTermAssistName()!=null)
							assistance.add(ampReportCache.getTermAssistName());
						if(ampReportCache.getDonorName()!=null)
							donors.add(ampReportCache.getDonorName());
						else
							donors.addAll(DbUtil.getAmpDonorsForActivity(ampReportCache.getAmpActivityId()));
						if(ampReportCache.getModalityName()!=null)
							modality.add(ampReportCache.getModalityName());
						if(ampReportCache.getActualStartDate()!=null)
							report.setStartDate(DateConversion.ConvertDateToString(ampReportCache.getActualStartDate()));
						else
							report.setStartDate("");
						if(ampReportCache.getActualCompletionDate()!=null)
							report.setCloseDate(DateConversion.ConvertDateToString(ampReportCache.getActualCompletionDate()));
						else
							report.setCloseDate("");
						report.setCommitmentDate(new ArrayList());
					}
					if(donors.indexOf(ampReportCache.getDonorName())==-1 && ampReportCache.getDonorName()!=null)
						donors.add(ampReportCache.getDonorName());

					if(modality.indexOf(ampReportCache.getModalityName())==-1 && ampReportCache.getModalityName()!=null)
						modality.add(ampReportCache.getModalityName());

					if(assistance.indexOf(ampReportCache.getTermAssistName())==-1 && ampReportCache.getTermAssistName()!=null)
						assistance.add(ampReportCache.getTermAssistName());
					
					if(ampReportCache.getFiscalYear()!=null && ampReportCache.getFiscalQuarter()!=null)
					{
						if(new Long(fiscalCalId).equals(Constants.GREGORIAN))
						{
							fiscalYear=ampReportCache.getFiscalYear().intValue();
							fiscalQuarter=ampReportCache.getFiscalQuarter().intValue();
						}
			
						if(ampReportCache.getCurrencyCode().equals("USD"))
							fromExchangeRate=1.0;
						else
							fromExchangeRate=DbUtil.getExchangeRate(ampReportCache.getCurrencyCode(),Constants.ACTUAL,ampReportCache.getTransactionDate());
						if(ampCurrencyCode.equals("USD"))
							toExchangeRate=1.0;
						else
							toExchangeRate=DbUtil.getExchangeRate(ampCurrencyCode,Constants.ACTUAL,ampReportCache.getTransactionDate());

						if(ampReportCache.getActualCommitment().doubleValue()>0)
						{
							actualCommitment=actualCommitment + CurrencyWorker.convert1(ampReportCache.getActualCommitment().doubleValue(),fromExchangeRate,toExchangeRate);
							if(commDate.indexOf(DateConversion.ConvertDateToString(ampReportCache.getTransactionDate()))==-1)
								commDate.add(DateConversion.ConvertDateToString(ampReportCache.getTransactionDate()));
							if(fiscalYear>=fromYr && fiscalYear<=toYr)
								projComm = projComm + CurrencyWorker.convert1(ampReportCache.getActualCommitment().doubleValue(),fromExchangeRate,toExchangeRate);

						}
						
						if(ampReportCache.getActualDisbursement().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
						{
							amount=CurrencyWorker.convert1(ampReportCache.getActualDisbursement().doubleValue(),fromExchangeRate,toExchangeRate);
							disbAmount=disbAmount + amount;
							if(fiscalYear>=fromYr && fiscalYear<=toYr)
							{
								if(disbFund[fiscalYear%fromYr][fiscalQuarter-1]==0)
									disbFund[fiscalYear%fromYr][fiscalQuarter-1]=amount;
								else
								if(disbFund[fiscalYear%fromYr][fiscalQuarter-1]>0)
									disbFund[fiscalYear%fromYr][fiscalQuarter-1]=disbFund[fiscalYear%fromYr][fiscalQuarter-1] + amount;
							}
						}
						amount=0.0;
								
						if(ampReportCache.getActualExpenditure().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
						{
							amount=CurrencyWorker.convert1(ampReportCache.getActualExpenditure().doubleValue(),fromExchangeRate,toExchangeRate);
							if(fiscalYear>=fromYr && fiscalYear<=toYr)
							{
								if(expFund[fiscalYear%fromYr][fiscalQuarter-1]==0)
									expFund[fiscalYear%fromYr][fiscalQuarter-1]=amount;
								else
								if(expFund[fiscalYear%fromYr][fiscalQuarter-1]>0)
									expFund[fiscalYear%fromYr][fiscalQuarter-1]=expFund[fiscalYear%fromYr][fiscalQuarter-1] + amount;
							}
						}
						amount=0.0;
						
						if(ampReportCache.getCurrencyCode().equals("USD"))
							fromExchangeRate=1.0;
						else
							fromExchangeRate=DbUtil.getExchangeRate(ampReportCache.getCurrencyCode(),Constants.PLANNED,ampReportCache.getTransactionDate());
						if(ampCurrencyCode.equals("USD"))
							toExchangeRate=1.0;
						else
							toExchangeRate=DbUtil.getExchangeRate(ampCurrencyCode,Constants.PLANNED,ampReportCache.getTransactionDate());
						
						if(ampReportCache.getPlannedDisbursement().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
						{
							amount=CurrencyWorker.convert1(ampReportCache.getPlannedDisbursement().doubleValue(),fromExchangeRate,toExchangeRate);
							if(fiscalYear>=fromYr && fiscalYear<=toYr)
							{
								if(plDisbFund[fiscalYear%fromYr][fiscalQuarter-1]==0)
									plDisbFund[fiscalYear%fromYr][fiscalQuarter-1]=amount;
								else
								if(plDisbFund[fiscalYear%fromYr][fiscalQuarter-1]>0)
									plDisbFund[fiscalYear%fromYr][fiscalQuarter-1]=plDisbFund[fiscalYear%fromYr][fiscalQuarter-1] + amount;
							}
						}
						amount=0.0;
					
					}
					

				}
				for(int i=0;i<yrCount;i++)
				{
					for(int qtr=0;qtr<4;qtr++)
					{
						AmpFund ampFund = new AmpFund();
						ampFund.setPlannedDisbAmount(mf.format(plDisbFund[i][qtr])); 
						ampFund.setDisbAmount(mf.format(disbFund[i][qtr])); 
						ampFund.setExpAmount(mf.format(expFund[i][qtr]));	
						plannedDisbAmount=plannedDisbAmount+ plDisbFund[i][qtr];
		//				disbAmount=disbAmount+ projFund[i][1];
						expAmount=expAmount+ expFund[i][qtr];
						report.getAmpFund().add(ampFund) ;
					}
				}	
				AmpFund ampFund = new AmpFund();
				ampFund.setPlannedDisbAmount(mf.format(plannedDisbAmount)); 
				ampFund.setDisbAmount(mf.format(disbAmount));
				ampFund.setExpAmount(mf.format(expAmount));
				ampFund.setUnDisbAmount(mf.format(projComm - disbAmount));
				report.getAmpFund().add(ampFund) ;	
				report.setAcCommitment(mf.format(actualCommitment));
						
				if(donors.size()==0)
					report.getDonors().add("Unspecified");
				else
					report.getDonors().addAll(donors);

				if(assistance.size()==0)
					report.getAssistance().add("Unspecified");
				else
					report.getAssistance().addAll(assistance);

				if(commDate.size()==0)
					report.getCommitmentDate().add("Unspecified");
				else
					report.getCommitmentDate().addAll(commDate);

				if(modality.size()==0)
					report.getModality().add("Unspecified");
				else
					report.getModality().addAll(modality);
				ampReports.add(report);
			}
		}
		catch(Exception ex) 		
		{
			logger.debug("Unable to get report names  from database " + ex.getMessage());
		}
		finally 
		{
			try 
			{
				PersistenceManager.releaseSession(session);
			}
			catch (Exception ex2) 
			{
				logger.debug("releaseSession() failed ");
			}
		}
		return ampReports ;
	}
	
	public static ArrayList getAmpQuarterlyReportByProject(Long ampTeamId,int fromYr,int toYr,String perspective,String ampCurrencyCode,Long ampModalityId,Long ampStatusId,Long ampDonorId,Long ampSectorId,int fiscalCalId,String startDate,String closeDate,String region)
	{
		Session session = null ;
		Query q = null ;
		ArrayList ampReports = new ArrayList() ;
		String queryString = null;
		Iterator iter=null;
		Long All=new Long(0);
		int yrCount = (toYr - fromYr)+1;
		int fiscalYear=0,fiscalQuarter=0;
		double[][] commFund=new double[yrCount][4];
		double[][] disbFund=new double[yrCount][4];
		double[][] plDisbFund=new double[yrCount][4];
		double[][] expFund=new double[yrCount][4];
		
		double[][] plDisbTermFunds=new double[yrCount][4];
		double[][] disbTermFunds=new double[yrCount][4];
		double[][] expTermFunds=new double[yrCount][4];

		double[][] loanPlDisbDonorFunds=new double[yrCount][4];
		double[][] loanDisbDonorFunds=new double[yrCount][4];
		double[][] loanExpDonorFunds=new double[yrCount][4];
		double[][] grantPlDisbDonorFunds=new double[yrCount][4];
		double[][] grantDisbDonorFunds=new double[yrCount][4];
		double[][] grantExpDonorFunds=new double[yrCount][4];
		double[][] kindPlDisbDonorFunds=new double[yrCount][4];
		double[][] kindDisbDonorFunds=new double[yrCount][4];
		double[][] kindExpDonorFunds=new double[yrCount][4];

		double[][] loanPlDisbTeamFunds=new double[yrCount][4];
		double[][] loanDisbTeamFunds=new double[yrCount][4];
		double[][] loanExpTeamFunds=new double[yrCount][4];
		double[][] grantPlDisbTeamFunds=new double[yrCount][4];
		double[][] grantDisbTeamFunds=new double[yrCount][4];
		double[][] grantExpTeamFunds=new double[yrCount][4];
		double[][] kindPlDisbTeamFunds=new double[yrCount][4];
		double[][] kindDisbTeamFunds=new double[yrCount][4];
		double[][] kindExpTeamFunds=new double[yrCount][4];

		double[][] plDisbDonorFund=new double[yrCount][4];
		double[][] disbDonorFund=new double[yrCount][4];
		double[][] expDonorFund=new double[yrCount][4];

		double[][] plDisbTeamFund=new double[yrCount][4];
		double[][] disbTeamFund=new double[yrCount][4];
		double[][] expTeamFund=new double[yrCount][4];
		Long selTeamId=null;
		double termComm=0.0;
		double totalPlDisb = 0.0 ;
		double totalDisb = 0.0 ;
		double totalExp = 0.0;
		double totalComm = 0.0;
		double toExchangeRate=1.0;
		double fromExchangeRate=0.0;
		double amount=0.0;
		double projCommAmount=0.0;
		double projPlannedDisbAmount=0.0;
		double projDisbAmount=0.0;
		double projExpAmount=0.0;
		double projUnDisbAmount=0.0;
		double donorCommAmount=0.0;
		double donorPlannedDisbAmount=0.0;
		double donorDisbAmount=0.0;
		double donorExpAmount=0.0;
		double donorUnDisbAmount=0.0;
		double teamCommAmount=0.0;
		double teamPlannedDisbAmount=0.0;
		double teamDisbAmount=0.0;
		double teamExpAmount=0.0;
		double teamUnDisbAmount=0.0;

		double loanCommDonorAmount=0.0;
		double grantCommDonorAmount=0.0;
		double kindCommDonorAmount=0.0;

		double loanUnDisbDonorFund=0.0;
		double grantUnDisbDonorFund=0.0;
		double kindUnDisbDonorFund=0.0;

		double loanUnDisbTeamFund=0.0;
		double grantUnDisbTeamFund=0.0;
		double kindUnDisbTeamFund=0.0;

		double loanCommTeamAmount=0.0;
		double grantCommTeamAmount=0.0;
		double kindCommTeamAmount=0.0;

		double actualCommitment=0.0;
		double actualDisbursement=0.0;

		int fiscalYearFlag=0;
		int flag=0;
		int termFlag=0;
		String termAssistName=null;
		int donorCount=0;
		int projCount=0;
		String inClause=null;
		Iterator iterSector=null;
		ArrayList teamAssistance=new ArrayList();
		ArrayList donorAssistance=new ArrayList();
		DecimalFormat mf = new DecimalFormat("###,###,###,###,###") ;
		ArrayList approvedActivityList=new ArrayList();
				
		try
		{
			ArrayList dbReturnSet=(ArrayList)DbUtil.getAmpLevel0Teams(ampTeamId);				
			if(dbReturnSet.size()==0)
				inClause= "'" + ampTeamId + "'";
			else
			{
				iter=dbReturnSet.iterator();
				while(iter.hasNext())
				{
					Long teamId= (Long) iter.next();
					if(inClause==null)
						inClause="'" + teamId + "'";
					else
						inClause=inClause + ",'" + teamId + "'";
				}
			}
			logger.debug("Inclause: " + inClause);
			approvedActivityList=DbUtil.getApprovedActivities(inClause);
			session = PersistenceManager.getSession();
			if(startDate==null && closeDate==null)
				queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.donorName is not null) and (report.ampTeamId in(" + inClause + ")) and (report.reportType='1') order by report.donorName,report.activityName,report.ampActivityId,report.fiscalYear";
			else
				queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.donorName is not null) and (report.ampTeamId in(" + inClause + ")) and (report.actualStartDate='" + startDate + "' or report.actualCompletionDate='" + closeDate + "') and (report.reportType='1') order by report.donorName,report.activityName,report.ampActivityId,report.fiscalYear";
			logger.debug("querystring: " + queryString);
			q = session.createQuery(queryString);	
			multiReport report =null;
			AmpDonors ampDonors =null;
			Project project=null;
			AmpTeamDonors ampTeamDonors=null;
			ProjectTermAssist termAssist=null;
			TermFund donorTermAssist=null;
			TermFund teamTermAssist=null;

			if(q!=null)
			{
				iter = q.list().iterator();
				while(iter.hasNext())
				{
					AmpReportCache ampReportCache = (AmpReportCache) iter.next(); 
					
					if(approvedActivityList.indexOf(ampReportCache.getAmpActivityId())==-1)
						continue;

					if(new Long(fiscalCalId).equals(Constants.ETH_FY) || new Long(fiscalCalId).equals(Constants.ETH_CAL))
					{
						if(ampReportCache.getTransactionDate()!=null)
						{
							GregorianCalendar calendar = new GregorianCalendar();
							EthiopianCalendar ec=new EthiopianCalendar();
							EthiopianCalendar tempDate=new EthiopianCalendar();
							calendar.setTime(ampReportCache.getTransactionDate());
							ec=tempDate.getEthiopianDate(calendar);
							if(new Long(fiscalCalId).equals(Constants.ETH_FY))
							{
								fiscalYear=(int)ec.ethFiscalYear;
								fiscalQuarter=(int)ec.ethFiscalQrt;
							}
							if(new Long(fiscalCalId).equals(Constants.ETH_CAL))
							{
								fiscalYear=(int)ec.ethYear;
								fiscalQuarter=(int)ec.ethQtr;
							}
							logger.debug("Ethiopian Fiscal Year: " + fiscalYear);
							logger.debug("From Year: " + fromYr);
							logger.debug("From Year: " + toYr);
/*							if(fiscalYear<fromYr || fiscalYear>toYr)
								continue;*/
						}
					}

					if(!ampModalityId.equals(All))
					{
						if(ampReportCache.getAmpModalityId()==null)
							continue;
						if(!(ampModalityId.equals(ampReportCache.getAmpModalityId())))
							continue;
					}

					if(!ampStatusId.equals(All))
					{
						if(ampReportCache.getAmpStatusId()==null)
							continue;
						if(!(ampStatusId.equals(ampReportCache.getAmpStatusId())))
							continue;
					}
	
					if(!ampDonorId.equals(All))
					{
						if(ampReportCache.getAmpDonorId()==null)
							continue;
						if(!(ampDonorId.equals(ampReportCache.getAmpDonorId())))
							continue;
					}
		
					if(!ampSectorId.equals(All))
					{
						int sflag=0;
						iterSector=DbUtil.getAmpReportSectorId(ampReportCache.getAmpActivityId()).iterator();
						while(iterSector.hasNext())
						{
							AmpReportSector sector=(AmpReportSector) iterSector.next();
							if(sector.getAmpSectorId().equals(ampSectorId))
							{
								sflag=1;
								break;
							}
							if(sector.getAmpSubSectorId().equals(new Long(0)))
							{
								if(new Long(sector.getSubSectorName()).equals(ampSectorId))
								{
									sflag=1;
									break;
								}
							}
							if(!(sector.getAmpSubSectorId().equals(new Long(0))) && sector.getAmpSubSectorId().equals(ampSectorId))
							{
								sflag=1;
								break;
							}
						}
						if (sflag==0)
						{
							continue;
						}
					}
					
					if(!region.equals("All"))
					{
						ArrayList location=(ArrayList)DbUtil.getAmpReportLocation(ampReportCache.getAmpActivityId());
						if(location.indexOf(region)==-1)
							continue;
					}
					
					if(report==null || !(report.getAmpTeamId().equals(ampReportCache.getAmpTeamId())))
					{
						if(report!=null)
						{
							if(termFlag==1)
							{
								termAssist=new ProjectTermAssist();
								termAssist.setTermAssistName(termAssistName);
								if(donorAssistance.indexOf(termAssist.getTermAssistName())==-1)
								{
									logger.debug("Donor Assistance Added:" + termAssist.getTermAssistName() + ":");
									donorAssistance.add(termAssist.getTermAssistName());
								}
								if(teamAssistance.indexOf(termAssist.getTermAssistName())==-1)
								{
									logger.debug("Team Assistance Added:" + termAssist.getTermAssistName() + ":");
									teamAssistance.add(termAssist.getTermAssistName());
								}
								termAssist.setTermAssistFund(new ArrayList());
								for(int i=0;i<yrCount;i++)
								{
									for(int qtr=0;qtr<4;qtr++)
									{
										AmpFund termFund = new AmpFund();
										termFund.setPlannedDisbAmount(mf.format(plDisbTermFunds[i][qtr])); 
										termFund.setDisbAmount(mf.format(disbTermFunds[i][qtr]));
										termFund.setExpAmount(mf.format(expTermFunds[i][qtr]));
										termAssist.getTermAssistFund().add(termFund);
										totalPlDisb=totalPlDisb + plDisbTermFunds[i][qtr];
										totalDisb=totalDisb + disbTermFunds[i][qtr];
										totalExp= totalExp + expTermFunds[i][qtr];
										plDisbFund[i][qtr]=plDisbFund[i][qtr] + plDisbTermFunds[i][qtr];
										disbFund[i][qtr]=disbFund[i][qtr] + disbTermFunds[i][qtr];
										expFund[i][qtr]=expFund[i][qtr] + expTermFunds[i][qtr];
									}
								}
								termAssist.setTermPlannedDisbAmount(mf.format(totalPlDisb));
								termAssist.setTermDisbAmount(mf.format(totalDisb));
								termAssist.setTermExpAmount(mf.format(totalExp));
								termAssist.setTermUnDisbAmount(mf.format(actualCommitment - actualDisbursement));

							//	projCommAmount=projCommAmount + termComm;
								projPlannedDisbAmount=projPlannedDisbAmount + totalPlDisb;
								projDisbAmount=projDisbAmount + totalDisb;
								projExpAmount=projExpAmount + totalExp;
								projUnDisbAmount=projUnDisbAmount + (actualCommitment-actualDisbursement);
								project.getTermAssist().add(termAssist);
								if(termAssist.getTermAssistName().equals("Loan"))
								{
									for(int i=0;i<yrCount;i++)
									{
										for (int qtr=0;qtr<4 ;qtr++)
										{
											loanDisbDonorFunds[i][qtr]=loanDisbDonorFunds[i][qtr] + disbTermFunds[i][qtr];
											loanExpDonorFunds[i][qtr]=loanExpDonorFunds[i][qtr] + expTermFunds[i][qtr];
											loanPlDisbDonorFunds[i][qtr]=loanPlDisbDonorFunds[i][qtr] + plDisbTermFunds[i][qtr];
										}
									}
									loanUnDisbDonorFund=loanUnDisbDonorFund + (actualCommitment-actualDisbursement);
								}
								if(termAssist.getTermAssistName().equals("Grant"))
								{
									for(int i=0;i<yrCount;i++)
									{
										for (int qtr=0;qtr<4 ;qtr++)
										{
											grantDisbDonorFunds[i][qtr]=grantDisbDonorFunds[i][qtr] + disbTermFunds[i][qtr];
											grantExpDonorFunds[i][qtr]=grantExpDonorFunds[i][qtr] + expTermFunds[i][qtr];
											grantPlDisbDonorFunds[i][qtr]=grantPlDisbDonorFunds[i][qtr] + plDisbTermFunds[i][qtr];
										}
									}
									grantUnDisbDonorFund=grantUnDisbDonorFund + (actualCommitment-actualDisbursement);
								}
								if(termAssist.getTermAssistName().equals("In Kind"))
								{
									for(int i=0;i<yrCount;i++)
									{
										for (int qtr=0;qtr<4 ;qtr++)
										{
											kindDisbDonorFunds[i][qtr]=kindDisbDonorFunds[i][qtr] + disbTermFunds[i][qtr];
											kindExpDonorFunds[i][qtr]=kindExpDonorFunds[i][qtr] + expTermFunds[i][qtr];
											kindPlDisbDonorFunds[i][qtr]=kindPlDisbDonorFunds[i][qtr] + plDisbTermFunds[i][qtr];
										}
									}
									kindUnDisbDonorFund=kindUnDisbDonorFund + (actualCommitment-actualDisbursement);
								}
							}
							for(int i=0;i<yrCount;i++)
							{
								for(int qtr=0;qtr<4;qtr++)
								{
									AmpFund projFund = new AmpFund();
									projFund.setPlannedDisbAmount(mf.format(plDisbFund[i][qtr])); 
									projFund.setDisbAmount(mf.format(disbFund[i][qtr]));
									projFund.setExpAmount(mf.format(expFund[i][qtr]));
									project.getAmpFund().add(projFund) ;
								}
							}
							project.setProjPlannedDisbAmount(mf.format(projPlannedDisbAmount));
							project.setProjDisbAmount(mf.format(projDisbAmount));
							project.setProjExpAmount(mf.format(projExpAmount));
							project.setProjUnDisbAmount(mf.format(projUnDisbAmount));
							ampTeamDonors.getProject().add(project);
							for(int i=0;i<yrCount;i++)
							{
								for(int qtr=0;qtr<4;qtr++)
								{
									plDisbDonorFund[i][qtr]=plDisbDonorFund[i][qtr] + plDisbFund[i][qtr];
									disbDonorFund[i][qtr]=disbDonorFund[i][qtr] + disbFund[i][qtr];
									expDonorFund[i][qtr]=expDonorFund[i][qtr] + expFund[i][qtr];
								}
							}

//							donorCommAmount=donorCommAmount + projCommAmount;
							donorPlannedDisbAmount=donorPlannedDisbAmount + projPlannedDisbAmount;
							donorDisbAmount=donorDisbAmount + projDisbAmount;
							donorExpAmount=donorExpAmount + projExpAmount;
							donorUnDisbAmount=donorUnDisbAmount + projUnDisbAmount;

							for(int i=0;i<yrCount;i++)
							{
								for(int qtr=0; qtr<4;qtr++)
								{
									FundTotal total=new FundTotal();
									total.setTotPlannedDisbAmount(mf.format(plDisbDonorFund[i][qtr]));
									total.setTotDisbAmount(mf.format(disbDonorFund[i][qtr]));
									total.setTotExpAmount(mf.format(expDonorFund[i][qtr]));
									ampTeamDonors.getTotalDonorFund().add(total);	
								}
							}
							ampTeamDonors.setDonorPlannedDisbAmount(mf.format(donorPlannedDisbAmount));
							ampTeamDonors.setDonorDisbAmount(mf.format(donorDisbAmount));
							ampTeamDonors.setDonorExpAmount(mf.format(donorExpAmount));
							ampTeamDonors.setDonorUnDisbAmount(mf.format(donorUnDisbAmount));
//							ampTeamDonors.getTotalDonorTermAssistFund().addAll(donorTotal);
							for(int i=0;i<yrCount;i++)
							{
								for(int qtr=0;qtr<4;qtr++)
								{
									plDisbTeamFund[i][qtr]=plDisbTeamFund[i][qtr] + plDisbDonorFund[i][qtr];
									disbTeamFund[i][qtr]=disbTeamFund[i][qtr] + disbDonorFund[i][qtr];
									expTeamFund[i][qtr]=expTeamFund[i][qtr] + expDonorFund[i][qtr];
								}
							}
//							teamCommAmount=teamCommAmount + donorCommAmount;
							teamPlannedDisbAmount=teamPlannedDisbAmount + donorPlannedDisbAmount;
							teamDisbAmount=teamDisbAmount + donorDisbAmount;
							teamExpAmount=teamExpAmount + donorExpAmount;
							teamUnDisbAmount=teamUnDisbAmount + donorUnDisbAmount;
//							ampTeamDonors.setTotalDonorTermAssistFund(new ArrayList());
	//						logger.debug("Size of Assistance:" + assistance.size() + ":");
							totalPlDisb=totalDisb=totalExp=0.0;
							Iterator donorIter=donorAssistance.iterator();
							while(donorIter.hasNext())
							{
								String assist=(String) donorIter.next();
								logger.debug("Assist:" + assist + ":");
								TermFund termFund=new TermFund();
								if(assist.equals("Loan"))
								{
									termFund.setTermAssistName("Loan");
									termFund.setTermFundTotal(new ArrayList());
									for(int i=0;i<yrCount;i++)
									{
										for(int qtr=0;qtr<4;qtr++)
										{
											TermFundTotal termFundTotal=new TermFundTotal();
											termFundTotal.setTotDisbAmount(mf.format(loanDisbDonorFunds[i][qtr]));
											termFundTotal.setTotExpAmount(mf.format(loanExpDonorFunds[i][qtr]));
											termFundTotal.setTotPlannedDisbAmount(mf.format(loanPlDisbDonorFunds[i][qtr]));
											termFund.getTermFundTotal().add(termFundTotal);
											totalDisb=totalDisb + loanDisbDonorFunds[i][qtr];
											totalExp=totalExp + loanExpDonorFunds[i][qtr];
											totalPlDisb=totalPlDisb + loanPlDisbDonorFunds[i][qtr];
											loanDisbTeamFunds[i][qtr]=loanDisbTeamFunds[i][qtr] + loanDisbDonorFunds[i][qtr];
											loanExpTeamFunds[i][qtr]=loanExpTeamFunds[i][qtr] + loanExpDonorFunds[i][qtr];
											loanPlDisbTeamFunds[i][qtr]=loanPlDisbTeamFunds[i][qtr] + loanPlDisbDonorFunds[i][qtr];
										}
									}
									loanUnDisbTeamFund=loanUnDisbTeamFund + loanUnDisbDonorFund;
									termFund.setTotDonorDisbAmount(mf.format(totalDisb));
									termFund.setTotDonorExpAmount(mf.format(totalExp));
									termFund.setTotDonorPlannedDisbAmount(mf.format(totalPlDisb));
									termFund.setTotDonorUnDisbAmount(mf.format(loanUnDisbDonorFund));
									totalPlDisb=totalDisb=totalExp=0.0;
								}
								if(assist.equals("Grant"))
								{
									termFund.setTermAssistName("Grant");
									termFund.setTermFundTotal(new ArrayList());
									for(int i=0;i<yrCount;i++)
									{
										for(int qtr=0;qtr<4;qtr++)
										{
											TermFundTotal termFundTotal=new TermFundTotal();
											termFundTotal.setTotDisbAmount(mf.format(grantDisbDonorFunds[i][qtr]));
											termFundTotal.setTotExpAmount(mf.format(grantExpDonorFunds[i][qtr]));
											termFundTotal.setTotPlannedDisbAmount(mf.format(grantPlDisbDonorFunds[i][qtr]));
											termFund.getTermFundTotal().add(termFundTotal);
											totalDisb=totalDisb + grantDisbDonorFunds[i][qtr];
											totalExp=totalExp + grantExpDonorFunds[i][qtr];
											totalPlDisb=totalPlDisb + grantPlDisbDonorFunds[i][qtr];
											grantDisbTeamFunds[i][qtr]=grantDisbTeamFunds[i][qtr] + grantDisbDonorFunds[i][qtr];
											grantExpTeamFunds[i][qtr]=grantExpTeamFunds[i][qtr] + grantExpDonorFunds[i][qtr];
											grantPlDisbTeamFunds[i][qtr]=grantPlDisbTeamFunds[i][qtr] + grantPlDisbDonorFunds[i][qtr];
										}
									}
									grantUnDisbTeamFund=grantUnDisbTeamFund + grantUnDisbDonorFund;
									termFund.setTotDonorDisbAmount(mf.format(totalDisb));
									termFund.setTotDonorExpAmount(mf.format(totalExp));
									termFund.setTotDonorPlannedDisbAmount(mf.format(totalPlDisb));
									termFund.setTotDonorUnDisbAmount(mf.format(grantUnDisbDonorFund));
									totalPlDisb=totalDisb=totalExp=0.0;
								}
								if(assist.equals("In Kind"))
								{
									termFund.setTermAssistName("In Kind");
									termFund.setTermFundTotal(new ArrayList());
									for(int i=0;i<yrCount;i++)
									{
										for(int qtr=0;qtr<4;qtr++)
										{
											TermFundTotal termFundTotal=new TermFundTotal();
											termFundTotal.setTotDisbAmount(mf.format(kindDisbDonorFunds[i][qtr]));
											termFundTotal.setTotExpAmount(mf.format(kindExpDonorFunds[i][qtr]));
											termFundTotal.setTotPlannedDisbAmount(mf.format(kindPlDisbDonorFunds[i][qtr]));
											termFund.getTermFundTotal().add(termFundTotal);
											totalDisb=totalDisb + kindDisbDonorFunds[i][qtr];
											totalExp=totalExp + kindExpDonorFunds[i][qtr];
											totalPlDisb=totalPlDisb + kindPlDisbDonorFunds[i][qtr];
											kindDisbTeamFunds[i][qtr]=kindDisbTeamFunds[i][qtr] + kindDisbDonorFunds[i][qtr];
											kindExpTeamFunds[i][qtr]=kindExpTeamFunds[i][qtr] + kindExpDonorFunds[i][qtr];
											kindPlDisbTeamFunds[i][qtr]=kindPlDisbTeamFunds[i][qtr] + kindPlDisbDonorFunds[i][qtr];
										}
									}
									kindUnDisbTeamFund=kindUnDisbTeamFund + kindUnDisbDonorFund;
									termFund.setTotDonorDisbAmount(mf.format(totalDisb));
									termFund.setTotDonorExpAmount(mf.format(totalExp));
									termFund.setTotDonorPlannedDisbAmount(mf.format(totalPlDisb));
									termFund.setTotDonorUnDisbAmount(mf.format(kindUnDisbDonorFund));
									totalPlDisb=totalDisb=totalExp=0.0;
								}
	//							logger.debug("Added:'" + termFund.getTermAssistName());
								ampTeamDonors.getTotalDonorTermAssistFund().add(termFund);	
							}
							report.getDonors().add(ampTeamDonors);
							totalPlDisb=totalDisb=totalExp=0.0;
							Iterator teamIter=teamAssistance.iterator();
							while(teamIter.hasNext())
							{
								String assist=(String) teamIter.next();
								logger.debug("Assist:" + assist + ":");
								TermFund termFund=new TermFund();
								if(assist.equals("Loan"))
								{
									termFund.setTermAssistName("Loan");
									termFund.setTermFundTotal(new ArrayList());
									for(int i=0;i<yrCount;i++)
									{
										for(int qtr=0;qtr<4;qtr++)
										{
											TermFundTotal termFundTotal=new TermFundTotal();
											termFundTotal.setTotDisbAmount(mf.format(loanDisbTeamFunds[i][qtr]));
											termFundTotal.setTotExpAmount(mf.format(loanExpTeamFunds[i][qtr]));
											termFundTotal.setTotPlannedDisbAmount(mf.format(loanPlDisbTeamFunds[i][qtr]));
											termFund.getTermFundTotal().add(termFundTotal);
											totalDisb=totalDisb + loanDisbTeamFunds[i][qtr];
											totalExp=totalExp + loanExpTeamFunds[i][qtr];
											totalPlDisb=totalPlDisb + loanPlDisbTeamFunds[i][qtr];
										}
									}
									termFund.setTotDonorDisbAmount(mf.format(totalDisb));
									termFund.setTotDonorExpAmount(mf.format(totalExp));
									termFund.setTotDonorPlannedDisbAmount(mf.format(totalPlDisb));
									termFund.setTotDonorUnDisbAmount(mf.format(loanUnDisbTeamFund));
									totalPlDisb=totalDisb=totalExp=0.0;
								}
								if(assist.equals("Grant"))
								{
									termFund.setTermAssistName("Grant");
									termFund.setTermFundTotal(new ArrayList());
									for(int i=0;i<yrCount;i++)
									{
										for(int qtr=0;qtr<4;qtr++)
										{	
											TermFundTotal termFundTotal=new TermFundTotal();
											termFundTotal.setTotDisbAmount(mf.format(grantDisbTeamFunds[i][qtr]));
											termFundTotal.setTotExpAmount(mf.format(grantExpTeamFunds[i][qtr]));
											termFundTotal.setTotPlannedDisbAmount(mf.format(grantPlDisbTeamFunds[i][qtr]));
											termFund.getTermFundTotal().add(termFundTotal);
											totalDisb=totalDisb + grantDisbTeamFunds[i][qtr];
											totalExp=totalExp + grantExpTeamFunds[i][qtr];
											totalPlDisb=totalPlDisb + grantPlDisbTeamFunds[i][qtr];
										}
									}
									termFund.setTotDonorDisbAmount(mf.format(totalDisb));
									termFund.setTotDonorExpAmount(mf.format(totalExp));
									termFund.setTotDonorPlannedDisbAmount(mf.format(totalPlDisb));
									termFund.setTotDonorUnDisbAmount(mf.format(grantUnDisbTeamFund));
									totalPlDisb=totalDisb=totalExp=0.0;
								}
								if(assist.equals("In Kind"))
								{
									termFund.setTermAssistName("In Kind");
									termFund.setTermFundTotal(new ArrayList());
									for(int i=0;i<yrCount;i++)
									{
										for(int qtr=0;qtr<4;qtr++)
										{
											TermFundTotal termFundTotal=new TermFundTotal();
											termFundTotal.setTotDisbAmount(mf.format(kindDisbTeamFunds[i][qtr]));
											termFundTotal.setTotExpAmount(mf.format(kindExpTeamFunds[i][qtr]));
											termFundTotal.setTotPlannedDisbAmount(mf.format(kindPlDisbTeamFunds[i][qtr]));
											termFund.getTermFundTotal().add(termFundTotal);
											totalDisb=totalDisb + kindDisbTeamFunds[i][qtr];
											totalExp=totalExp + kindExpTeamFunds[i][qtr];
											totalPlDisb=totalPlDisb + kindPlDisbTeamFunds[i][qtr];
										}
									}
									termFund.setTotDonorDisbAmount(mf.format(totalDisb));
									termFund.setTotDonorExpAmount(mf.format(totalExp));
									termFund.setTotDonorPlannedDisbAmount(mf.format(totalPlDisb));
									termFund.setTotDonorUnDisbAmount(mf.format(kindUnDisbTeamFund));
									totalPlDisb=totalDisb=totalExp=0.0;
								}	
								report.getTotalTeamTermAssistFund().add(termFund);	
							}
							for(int i=0;i<yrCount;i++)
							{
								for(int qtr=0;qtr<4;qtr++)
								{
									FundTotal total=new FundTotal();
									total.setTotPlannedDisbAmount(mf.format(plDisbTeamFund[i][qtr]));
									total.setTotDisbAmount(mf.format(disbTeamFund[i][qtr]));
									total.setTotExpAmount(mf.format(expTeamFund[i][qtr]));
									report.getTotalTeamFund().add(total);	
								}
							}
							report.setTeamPlannedDisbAmount(mf.format(teamPlannedDisbAmount));
							report.setTeamDisbAmount(mf.format(teamDisbAmount));
							report.setTeamExpAmount(mf.format(teamExpAmount));
							report.setTeamUnDisbAmount(mf.format(teamUnDisbAmount));

							projCommAmount=projPlannedDisbAmount=projDisbAmount=projExpAmount=projUnDisbAmount=0;
							donorCommAmount=donorPlannedDisbAmount=donorDisbAmount=donorExpAmount=0;
							teamCommAmount=teamPlannedDisbAmount=teamDisbAmount=teamExpAmount=0;
							loanCommDonorAmount=grantCommDonorAmount=kindCommDonorAmount=0;
							loanCommTeamAmount=grantCommTeamAmount=kindCommTeamAmount=0;
							termComm=totalPlDisb=totalDisb=totalExp=0.0;
							for(int i=0;i<yrCount;i++)
							{
								for(int qtr=0;qtr<4;qtr++)
								{
									plDisbTermFunds[i][qtr]=0;
									disbTermFunds[i][qtr]=0;
									expTermFunds[i][qtr]=0;
									plDisbFund[i][qtr]=0;
									disbFund[i][qtr]=0;
									expFund[i][qtr]=0;
									plDisbDonorFund[i][qtr]=0;
									disbDonorFund[i][qtr]=0;
									expDonorFund[i][qtr]=0;
									kindExpDonorFunds[i][qtr]=0;
									kindDisbDonorFunds[i][qtr]=0;
									kindPlDisbDonorFunds[i][qtr]=0;
									grantExpDonorFunds[i][qtr]=0;
									grantDisbDonorFunds[i][qtr]=0;
									grantPlDisbDonorFunds[i][qtr]=0;
									loanExpDonorFunds[i][qtr]=0;
									loanDisbDonorFunds[i][qtr]=0;
									loanPlDisbDonorFunds[i][qtr]=0;
									plDisbTeamFund[i][qtr]=0;
									disbTeamFund[i][qtr]=0;
									expTeamFund[i][qtr]=0;
									kindExpTeamFunds[i][qtr]=0;
									kindDisbTeamFunds[i][qtr]=0;
									kindPlDisbTeamFunds[i][qtr]=0;
									grantExpTeamFunds[i][qtr]=0;
									grantDisbTeamFunds[i][qtr]=0;
									grantPlDisbTeamFunds[i][qtr]=0;
									loanExpTeamFunds[i][qtr]=0;
									loanDisbTeamFunds[i][qtr]=0;
									loanPlDisbTeamFunds[i][qtr]=0;
								}
							}
							projCount=0;
										
							ampTeamDonors=null;
							if(ampDonorId.equals(All))
							{
								logger.debug("Inside Unspecified");
								queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.donorName is null) and (report.ampTeamId='" + selTeamId + "') and (report.reportType='1') order by report.activityName,report.ampActivityId";
								q = session.createQuery(queryString);	
								Iterator iterUn=q.list().iterator();
								if(q.list().size()>0)
								{
									while(iterUn.hasNext())
									{
										AmpReportCache ampCache= (AmpReportCache) iterUn.next();
										
										if(approvedActivityList.indexOf(ampCache.getAmpActivityId())==-1)
											continue;

										if(!ampModalityId.equals(All))
										{
											if(ampCache.getAmpModalityId()==null)
												continue;
											if(!(ampModalityId.equals(ampCache.getAmpModalityId())))
												continue;
										}
	
										if(!ampStatusId.equals(All))
										{
											if(ampCache.getAmpStatusId()==null)
												continue;
											if(!(ampStatusId.equals(ampCache.getAmpStatusId())))
												continue;
										}
		
										if(!ampSectorId.equals(All))
										{
											int sflag=0;
											iterSector=DbUtil.getAmpReportSectorId(ampCache.getAmpActivityId()).iterator();
											while(iterSector.hasNext())
											{
												AmpReportSector sector=(AmpReportSector) iterSector.next();
												if(sector.getAmpSectorId().equals(ampSectorId))
												{
													sflag=1;
													break;
												}
												if(sector.getAmpSubSectorId().equals(new Long(0)))
												{
													if(new Long(sector.getSubSectorName()).equals(ampSectorId))
													{
														sflag=1;
														break;
													}
												}
												if(!(sector.getAmpSubSectorId().equals(new Long(0))) && sector.getAmpSubSectorId().equals(ampSectorId))
												{
													sflag=1;
													break;
												}
											}
											if (sflag==0)
											{
												continue;
											}
										}
										if(!region.equals("All"))
										{
											ArrayList location=(ArrayList)DbUtil.getAmpReportLocation(ampCache.getAmpActivityId());
											if(location.indexOf(region)==-1)
												continue;
										}
										if(ampTeamDonors==null)
										{
											ampTeamDonors = new AmpTeamDonors();
											ampTeamDonors.setDonorAgency("Unspecified");
											ampTeamDonors.setDonorCount(++donorCount);
											ampTeamDonors.setProject(new ArrayList());
											ampTeamDonors.setTotalDonorFund(new ArrayList());
										}
										project=new Project();
										project.setName(ampCache.getActivityName());
										project.setAmpActivityId(ampCache.getAmpActivityId());
										project.setCount(++projCount);
										project.setAmpFund(new ArrayList());
										for(int i=0;i<yrCount;i++)
										{
											for(int qtr=0;qtr<4;qtr++)
											{
												AmpFund ampFund = new AmpFund();
												ampFund.setPlannedDisbAmount(mf.format(0)); 
												ampFund.setDisbAmount(mf.format(0));
												ampFund.setExpAmount(mf.format(0));
												project.getAmpFund().add(ampFund) ;
											}
										}
										project.setProjPlannedDisbAmount(mf.format(0));
										project.setProjDisbAmount(mf.format(0));
										project.setProjExpAmount(mf.format(0));
										project.setProjUnDisbAmount(mf.format(0));
										ampTeamDonors.getProject().add(project);
									}
									if(ampTeamDonors!=null)
									{
										for(int i=0;i<yrCount;i++)
										{
											for(int qtr=0;qtr<4;qtr++)
											{
												FundTotal total=new FundTotal();
												total.setTotPlannedDisbAmount(mf.format(0));
												total.setTotDisbAmount(mf.format(0));
												total.setTotExpAmount(mf.format(0));
												ampTeamDonors.getTotalDonorFund().add(total);	
											}
										}
										ampTeamDonors.setDonorPlannedDisbAmount(mf.format(0));
										ampTeamDonors.setDonorDisbAmount(mf.format(0));
										ampTeamDonors.setDonorExpAmount(mf.format(0));
										ampTeamDonors.setDonorUnDisbAmount(mf.format(0));
										report.getDonors().add(ampTeamDonors);
									}
								}
							}
							donorCount=0;
							donorAssistance.clear();
							teamAssistance.clear();
							ampReports.add(report);
							logger.debug("Outside Team");
						}		
						report = new multiReport();
						AmpTeam ampTeam=DbUtil.getAmpTeam(ampReportCache.getAmpTeamId());
						report.setAmpTeamId(ampReportCache.getAmpTeamId());
						selTeamId=ampReportCache.getAmpTeamId();
						report.setTeamName(ampTeam.getName());
						report.setTotalTeamFund(new ArrayList());
						report.setTotalTeamTermAssistFund(new ArrayList());
						report.setDonors(new ArrayList());
						ampTeamDonors=new AmpTeamDonors();
						ampTeamDonors.setDonorAgency(ampReportCache.getDonorName());
						ampTeamDonors.setDonorCount(++donorCount);
						ampTeamDonors.setTotalDonorFund(new ArrayList());
						ampTeamDonors.setTotalDonorTermAssistFund(new ArrayList());
						ampTeamDonors.setProject(new ArrayList());
						project=new Project();
						project.setName(ampReportCache.getActivityName());
						project.setAmpActivityId(ampReportCache.getAmpActivityId());
						project.setCount(++projCount);
						project.setAmpFund(new ArrayList());
						project.setTermAssist(new ArrayList());
						if(ampReportCache.getTermAssistName()!=null)
							termAssistName=ampReportCache.getTermAssistName();
						termFlag=0;
					}

					if(report.getAmpTeamId().equals(ampReportCache.getAmpTeamId()) && !(ampReportCache.getDonorName().equals(ampTeamDonors.getDonorAgency())))
					{
						logger.debug("Inside Donor");
						if(termFlag==1)
						{
							termAssist=new ProjectTermAssist();
							termAssist.setTermAssistName(termAssistName);
							if(donorAssistance.indexOf(termAssist.getTermAssistName())==-1)
							{
								logger.debug("Donor Assistance Added:" + termAssist.getTermAssistName() + ":");
								donorAssistance.add(termAssist.getTermAssistName());
							}
							if(teamAssistance.indexOf(termAssist.getTermAssistName())==-1)
							{
								logger.debug("Team Assistance Added:" + termAssist.getTermAssistName() + ":");
								teamAssistance.add(termAssist.getTermAssistName());
							}
							termAssist.setTermAssistFund(new ArrayList());
							for(int i=0;i<yrCount;i++)
							{
								for(int qtr=0;qtr<4;qtr++)
								{
									AmpFund termFund = new AmpFund();
									termFund.setPlannedDisbAmount(mf.format(plDisbTermFunds[i][qtr])); 
									termFund.setDisbAmount(mf.format(disbTermFunds[i][qtr]));
									termFund.setExpAmount(mf.format(expTermFunds[i][qtr]));
									termAssist.getTermAssistFund().add(termFund);
									totalPlDisb=totalPlDisb + plDisbTermFunds[i][qtr];
									totalDisb=totalDisb + disbTermFunds[i][qtr];
									totalExp= totalExp + expTermFunds[i][qtr];
									plDisbFund[i][qtr]=plDisbFund[i][qtr] + plDisbTermFunds[i][qtr];
									disbFund[i][qtr]=disbFund[i][qtr] + disbTermFunds[i][qtr];
									expFund[i][qtr]=expFund[i][qtr] + expTermFunds[i][qtr];
								}
							}
							termAssist.setTermPlannedDisbAmount(mf.format(totalPlDisb));
							termAssist.setTermDisbAmount(mf.format(totalDisb));
							termAssist.setTermExpAmount(mf.format(totalExp));
							termAssist.setTermUnDisbAmount(mf.format(termComm - totalDisb));
							projCommAmount = projCommAmount + termComm;
							projPlannedDisbAmount=projPlannedDisbAmount + totalPlDisb;
							projDisbAmount=projDisbAmount + totalDisb;
							projExpAmount=projExpAmount + totalExp;
							project.getTermAssist().add(termAssist);
							if(termAssist.getTermAssistName().equals("Loan"))
							{
								for(int i=0;i<yrCount;i++)
								{
									for (int qtr=0;qtr<4 ;qtr++)
									{
										loanDisbDonorFunds[i][qtr]=loanDisbDonorFunds[i][qtr] + disbTermFunds[i][qtr];
										loanExpDonorFunds[i][qtr]=loanExpDonorFunds[i][qtr] + expTermFunds[i][qtr];
										loanPlDisbDonorFunds[i][qtr]=loanPlDisbDonorFunds[i][qtr] + plDisbTermFunds[i][qtr];
									}
								}
								loanCommDonorAmount=loanCommDonorAmount + termComm;
							}
							if(termAssist.getTermAssistName().equals("Grant"))
							{
								for(int i=0;i<yrCount;i++)
								{
									for (int qtr=0;qtr<4 ;qtr++)
									{
										grantDisbDonorFunds[i][qtr]=grantDisbDonorFunds[i][qtr] + disbTermFunds[i][qtr];
										grantExpDonorFunds[i][qtr]=grantExpDonorFunds[i][qtr] + expTermFunds[i][qtr];
										grantPlDisbDonorFunds[i][qtr]=grantPlDisbDonorFunds[i][qtr] + plDisbTermFunds[i][qtr];
									}
								}
								grantCommDonorAmount=grantCommDonorAmount + termComm;
							}
							if(termAssist.getTermAssistName().equals("Kind"))
							{
								for(int i=0;i<yrCount;i++)
								{
									for (int qtr=0;qtr<4 ;qtr++)
									{
										kindDisbDonorFunds[i][qtr]=kindDisbDonorFunds[i][qtr] + disbTermFunds[i][qtr];
										kindExpDonorFunds[i][qtr]=kindExpDonorFunds[i][qtr] + expTermFunds[i][qtr];
										kindPlDisbDonorFunds[i][qtr]=kindPlDisbDonorFunds[i][qtr] + plDisbTermFunds[i][qtr];
									}
								}
								kindCommDonorAmount=kindCommDonorAmount + termComm;
							}
							
						}
						for(int i=0;i<yrCount;i++)
						{
							for(int qtr=0;qtr<4;qtr++)
							{
								AmpFund projFund = new AmpFund();
								projFund.setPlannedDisbAmount(mf.format(plDisbFund[i][qtr])); 
								projFund.setDisbAmount(mf.format(disbFund[i][qtr]));
								projFund.setExpAmount(mf.format(expFund[i][qtr]));
								project.getAmpFund().add(projFund) ;
							}
						}
						project.setProjPlannedDisbAmount(mf.format(projPlannedDisbAmount));
						project.setProjDisbAmount(mf.format(projDisbAmount));
						project.setProjExpAmount(mf.format(projExpAmount));
						project.setProjUnDisbAmount(mf.format(projCommAmount - projDisbAmount));
						ampTeamDonors.getProject().add(project);
						for(int i=0;i<yrCount;i++)
						{
							for(int qtr=0;qtr<4;qtr++)
							{
								plDisbDonorFund[i][qtr]=plDisbDonorFund[i][qtr] + plDisbFund[i][qtr];
								disbDonorFund[i][qtr]=disbDonorFund[i][qtr] + disbFund[i][qtr];
								expDonorFund[i][qtr]=expDonorFund[i][qtr] + expFund[i][qtr];
							}
											
						}
						donorCommAmount=donorCommAmount + projCommAmount;
						donorPlannedDisbAmount=donorPlannedDisbAmount + projPlannedDisbAmount;
						donorDisbAmount=donorDisbAmount + projDisbAmount;
						donorExpAmount=donorExpAmount + projExpAmount;

						
						for(int i=0;i<yrCount;i++)
						{
							for(int qtr=0; qtr<4;qtr++)
							{
								FundTotal total=new FundTotal();
								total.setTotPlannedDisbAmount(mf.format(plDisbDonorFund[i][qtr]));
								total.setTotDisbAmount(mf.format(disbDonorFund[i][qtr]));
								total.setTotExpAmount(mf.format(expDonorFund[i][qtr]));
								ampTeamDonors.getTotalDonorFund().add(total);	
							}
						}
						ampTeamDonors.setDonorPlannedDisbAmount(mf.format(donorPlannedDisbAmount));
						ampTeamDonors.setDonorDisbAmount(mf.format(donorDisbAmount));
						ampTeamDonors.setDonorExpAmount(mf.format(donorExpAmount));
						ampTeamDonors.setDonorUnDisbAmount(mf.format(donorCommAmount - donorDisbAmount));
//						ampTeamDonors.getTotalDonorTermAssistFund().addAll(donorTotal);
						for(int i=0;i<yrCount;i++)
						{
							for(int qtr=0;qtr<4;qtr++)
							{
								plDisbTeamFund[i][qtr]=plDisbTeamFund[i][qtr] + plDisbDonorFund[i][qtr];
								disbTeamFund[i][qtr]=disbTeamFund[i][qtr] + disbDonorFund[i][qtr];
								expTeamFund[i][qtr]=expTeamFund[i][qtr] + expDonorFund[i][qtr];
							}
											
						}
						teamCommAmount=teamCommAmount + donorCommAmount;
						teamPlannedDisbAmount=teamPlannedDisbAmount + donorPlannedDisbAmount;
						teamDisbAmount=teamDisbAmount + donorDisbAmount;
						teamExpAmount=teamExpAmount + donorExpAmount;
//						ampTeamDonors.setTotalDonorTermAssistFund(new ArrayList());
//						logger.debug("Size of Assistance:" + assistance.size() + ":");
						termComm=totalPlDisb=totalDisb=totalExp=0.0;
						Iterator donorIter=donorAssistance.iterator();
						while(donorIter.hasNext())
						{
							String assist=(String) donorIter.next();
							logger.debug("Assist:" + assist + ":");
							TermFund termFund=new TermFund();
							if(assist.equals("Loan"))
							{
								termFund.setTermAssistName("Loan");
								termFund.setTermFundTotal(new ArrayList());
								for(int i=0;i<yrCount;i++)
								{
									for(int qtr=0;qtr<4;qtr++)
									{
										TermFundTotal termFundTotal=new TermFundTotal();
										termFundTotal.setTotDisbAmount(mf.format(loanDisbDonorFunds[i][qtr]));
										termFundTotal.setTotExpAmount(mf.format(loanExpDonorFunds[i][qtr]));
										termFundTotal.setTotPlannedDisbAmount(mf.format(loanPlDisbDonorFunds[i][qtr]));
										termFund.getTermFundTotal().add(termFundTotal);
										totalDisb=totalDisb + loanDisbDonorFunds[i][qtr];
										totalExp=totalExp + loanExpDonorFunds[i][qtr];
										totalPlDisb=totalPlDisb + loanPlDisbDonorFunds[i][qtr];
										loanDisbTeamFunds[i][qtr]=loanDisbTeamFunds[i][qtr] + loanDisbDonorFunds[i][qtr];
										loanExpTeamFunds[i][qtr]=loanExpTeamFunds[i][qtr] + loanExpDonorFunds[i][qtr];
										loanPlDisbTeamFunds[i][qtr]=loanPlDisbTeamFunds[i][qtr] + loanPlDisbDonorFunds[i][qtr];
									}
								}
								loanCommTeamAmount=loanCommTeamAmount + loanCommDonorAmount;
								termFund.setTotDonorDisbAmount(mf.format(totalDisb));
								termFund.setTotDonorExpAmount(mf.format(totalExp));
								termFund.setTotDonorPlannedDisbAmount(mf.format(totalPlDisb));
								termFund.setTotDonorUnDisbAmount(mf.format(loanCommDonorAmount-totalDisb));
								totalPlDisb=totalDisb=totalExp=0.0;
							}
							if(assist.equals("Grant"))
							{
								termFund.setTermAssistName("Grant");
								termFund.setTermFundTotal(new ArrayList());
								for(int i=0;i<yrCount;i++)
								{
									for(int qtr=0;qtr<4;qtr++)
									{
										TermFundTotal termFundTotal=new TermFundTotal();
										termFundTotal.setTotDisbAmount(mf.format(grantDisbDonorFunds[i][qtr]));
											termFundTotal.setTotExpAmount(mf.format(grantExpDonorFunds[i][qtr]));
										termFundTotal.setTotPlannedDisbAmount(mf.format(grantPlDisbDonorFunds[i][qtr]));
										termFund.getTermFundTotal().add(termFundTotal);
										totalDisb=totalDisb + grantDisbDonorFunds[i][qtr];
										totalExp=totalExp + grantExpDonorFunds[i][qtr];
										totalPlDisb=totalPlDisb + grantPlDisbDonorFunds[i][qtr];
										grantDisbTeamFunds[i][qtr]=grantDisbTeamFunds[i][qtr] + grantDisbDonorFunds[i][qtr];
										grantExpTeamFunds[i][qtr]=grantExpTeamFunds[i][qtr] + grantExpDonorFunds[i][qtr];
										grantPlDisbTeamFunds[i][qtr]=grantPlDisbTeamFunds[i][qtr] + grantPlDisbDonorFunds[i][qtr];
									}
								}
								grantCommTeamAmount=grantCommTeamAmount + grantCommDonorAmount;
								termFund.setTotDonorDisbAmount(mf.format(totalDisb));
								termFund.setTotDonorExpAmount(mf.format(totalExp));
								termFund.setTotDonorPlannedDisbAmount(mf.format(totalPlDisb));
								termFund.setTotDonorUnDisbAmount(mf.format(grantCommDonorAmount-totalDisb));
								totalPlDisb=totalDisb=totalExp=0.0;
							}
							if(assist.equals("In Kind"))
							{
								termFund.setTermAssistName("In Kind");
								termFund.setTermFundTotal(new ArrayList());
								for(int i=0;i<yrCount;i++)
								{
									for(int qtr=0;qtr<4;qtr++)
									{
										TermFundTotal termFundTotal=new TermFundTotal();
										termFundTotal.setTotDisbAmount(mf.format(kindDisbDonorFunds[i][qtr]));
										termFundTotal.setTotExpAmount(mf.format(kindExpDonorFunds[i][qtr]));
										termFundTotal.setTotPlannedDisbAmount(mf.format(kindPlDisbDonorFunds[i][qtr]));
										termFund.getTermFundTotal().add(termFundTotal);
										totalDisb=totalDisb + kindDisbDonorFunds[i][qtr];
										totalExp=totalExp + kindExpDonorFunds[i][qtr];
										totalPlDisb=totalPlDisb + kindPlDisbDonorFunds[i][qtr];
										kindDisbTeamFunds[i][qtr]=kindDisbTeamFunds[i][qtr] + kindDisbDonorFunds[i][qtr];
										kindExpTeamFunds[i][qtr]=kindExpTeamFunds[i][qtr] + kindExpDonorFunds[i][qtr];
										kindPlDisbTeamFunds[i][qtr]=kindPlDisbTeamFunds[i][qtr] + kindPlDisbDonorFunds[i][qtr];
									}
								}
								kindCommTeamAmount=kindCommTeamAmount + kindCommDonorAmount;
								termFund.setTotDonorDisbAmount(mf.format(totalDisb));
								termFund.setTotDonorExpAmount(mf.format(totalExp));
								termFund.setTotDonorPlannedDisbAmount(mf.format(totalPlDisb));
								termFund.setTotDonorUnDisbAmount(mf.format(kindCommDonorAmount-totalDisb));
								totalPlDisb=totalDisb=totalExp=0.0;
							}
//							logger.debug("Added:'" + termFund.getTermAssistName());
							ampTeamDonors.getTotalDonorTermAssistFund().add(termFund);	
						}
						report.getDonors().add(ampTeamDonors);
						donorAssistance.clear();
						projCommAmount=projPlannedDisbAmount=projDisbAmount=projExpAmount=0;
						donorCommAmount=donorPlannedDisbAmount=donorDisbAmount=donorExpAmount=0;
						termComm=totalPlDisb=totalDisb=totalExp=0.0;
						loanCommDonorAmount=grantCommDonorAmount=kindCommDonorAmount=0.0;
						for(int i=0;i<yrCount;i++)
						{
							for(int qtr=0;qtr<4;qtr++)
							{
								plDisbTermFunds[i][qtr]=0;
								disbTermFunds[i][qtr]=0;
								expTermFunds[i][qtr]=0;
								plDisbFund[i][qtr]=0;
								disbFund[i][qtr]=0;
								expFund[i][qtr]=0;
								plDisbDonorFund[i][qtr]=0;
								disbDonorFund[i][qtr]=0;
								expDonorFund[i][qtr]=0;
								kindExpDonorFunds[i][qtr]=0;
								kindDisbDonorFunds[i][qtr]=0;
								kindPlDisbDonorFunds[i][qtr]=0;
								grantExpDonorFunds[i][qtr]=0;
								grantDisbDonorFunds[i][qtr]=0;
								grantPlDisbDonorFunds[i][qtr]=0;
								loanExpDonorFunds[i][qtr]=0;
								loanDisbDonorFunds[i][qtr]=0;
								loanPlDisbDonorFunds[i][qtr]=0;
							}
						}
						projCount=0;
						ampTeamDonors=new AmpTeamDonors();
						ampTeamDonors.setDonorAgency(ampReportCache.getDonorName());
						ampTeamDonors.setDonorCount(++donorCount);
						ampTeamDonors.setTotalDonorFund(new ArrayList());
						ampTeamDonors.setTotalDonorTermAssistFund(new ArrayList());
						ampTeamDonors.setProject(new ArrayList());
						project=new Project();
						project.setName(ampReportCache.getActivityName());
						project.setAmpActivityId(ampReportCache.getAmpActivityId());
						project.setCount(++projCount);
						project.setAmpFund(new ArrayList());
						project.setTermAssist(new ArrayList());
						if(ampReportCache.getTermAssistName()!=null)
							termAssistName=ampReportCache.getTermAssistName();
						termFlag=0;	
						logger.debug("Outside Donor");
					}

					if(report.getAmpTeamId().equals(ampReportCache.getAmpTeamId()) && ampReportCache.getDonorName().equals(ampTeamDonors.getDonorAgency()) && !(ampReportCache.getAmpActivityId().equals(project.getAmpActivityId())))
					{
						logger.debug("Inside Project");
						if(termFlag==1)
						{
							termAssist=new ProjectTermAssist();
							termAssist.setTermAssistName(termAssistName);
							if(donorAssistance.indexOf(termAssist.getTermAssistName())==-1)
							{
								logger.debug("Donor Assistance Added:" + termAssist.getTermAssistName() + ":");
								donorAssistance.add(termAssist.getTermAssistName());
							}
							if(teamAssistance.indexOf(termAssist.getTermAssistName())==-1)
							{
								logger.debug("Team Assistance Added:" + termAssist.getTermAssistName() + ":");
								teamAssistance.add(termAssist.getTermAssistName());
							}
							termAssist.setTermAssistFund(new ArrayList());
							for(int i=0;i<yrCount;i++)
							{
								for(int qtr=0;qtr<4;qtr++)
								{
									AmpFund termFund = new AmpFund();
									termFund.setPlannedDisbAmount(mf.format(plDisbTermFunds[i][qtr])); 
									termFund.setDisbAmount(mf.format(disbTermFunds[i][qtr]));
									termFund.setExpAmount(mf.format(expTermFunds[i][qtr]));
									termAssist.getTermAssistFund().add(termFund);
									totalPlDisb=totalPlDisb + plDisbTermFunds[i][qtr];
									totalDisb=totalDisb + disbTermFunds[i][qtr];
									totalExp= totalExp + expTermFunds[i][qtr];
									plDisbFund[i][qtr]=plDisbFund[i][qtr] + plDisbTermFunds[i][qtr];
									disbFund[i][qtr]=disbFund[i][qtr] + disbTermFunds[i][qtr];
									expFund[i][qtr]=expFund[i][qtr] + expTermFunds[i][qtr];
								}
							}
							termAssist.setTermPlannedDisbAmount(mf.format(totalPlDisb));
							termAssist.setTermDisbAmount(mf.format(totalDisb));
							termAssist.setTermExpAmount(mf.format(totalExp));
							termAssist.setTermDisbAmount(mf.format(termComm-totalDisb));
							projCommAmount=projCommAmount + termComm;
							projPlannedDisbAmount=projPlannedDisbAmount + totalPlDisb;
							projDisbAmount=projDisbAmount + totalDisb;
							projExpAmount=projExpAmount + totalExp;
							project.getTermAssist().add(termAssist);
							if(termAssist.getTermAssistName().equals("Loan"))
							{
								for(int i=0;i<yrCount;i++)
								{
									for (int qtr=0;qtr<4 ;qtr++)
									{
										loanDisbDonorFunds[i][qtr]=loanDisbDonorFunds[i][qtr] + disbTermFunds[i][qtr];
										loanExpDonorFunds[i][qtr]=loanExpDonorFunds[i][qtr] + expTermFunds[i][qtr];
										loanPlDisbDonorFunds[i][qtr]=loanPlDisbDonorFunds[i][qtr] + plDisbTermFunds[i][qtr];
									}
								}
								loanCommDonorAmount=loanCommDonorAmount + termComm;
							}
							if(termAssist.getTermAssistName().equals("Grant"))
							{
								for(int i=0;i<yrCount;i++)
								{
									for (int qtr=0;qtr<4 ;qtr++)
									{
										grantDisbDonorFunds[i][qtr]=grantDisbDonorFunds[i][qtr] + disbTermFunds[i][qtr];
										grantExpDonorFunds[i][qtr]=grantExpDonorFunds[i][qtr] + expTermFunds[i][qtr];
										grantPlDisbDonorFunds[i][qtr]=grantPlDisbDonorFunds[i][qtr] + plDisbTermFunds[i][qtr];
									}
								}
								grantCommDonorAmount=grantCommDonorAmount + termComm;
							}
							if(termAssist.getTermAssistName().equals("Kind"))
							{
								for(int i=0;i<yrCount;i++)
								{
									for (int qtr=0;qtr<4 ;qtr++)
									{
										kindDisbDonorFunds[i][qtr]=kindDisbDonorFunds[i][qtr] + disbTermFunds[i][qtr];
										kindExpDonorFunds[i][qtr]=kindExpDonorFunds[i][qtr] + expTermFunds[i][qtr];
										kindPlDisbDonorFunds[i][qtr]=kindPlDisbDonorFunds[i][qtr] + plDisbTermFunds[i][qtr];
									}
								}
								kindCommDonorAmount=kindCommDonorAmount + termComm;
							}
							
						}
						for(int i=0;i<yrCount;i++)
						{
							for(int qtr=0;qtr<4;qtr++)
							{
								AmpFund projFund = new AmpFund();
								projFund.setPlannedDisbAmount(mf.format(plDisbFund[i][qtr])); 
								projFund.setDisbAmount(mf.format(disbFund[i][qtr]));
								projFund.setExpAmount(mf.format(expFund[i][qtr]));
								project.getAmpFund().add(projFund) ;
							}
						}
						project.setProjPlannedDisbAmount(mf.format(projPlannedDisbAmount));
						project.setProjDisbAmount(mf.format(projDisbAmount));
						project.setProjExpAmount(mf.format(projExpAmount));
						project.setProjUnDisbAmount(mf.format(projCommAmount - projDisbAmount));
						ampTeamDonors.getProject().add(project);
						for(int i=0;i<yrCount;i++)
						{
							for(int qtr=0;qtr<4;qtr++)
							{
								plDisbDonorFund[i][qtr]=plDisbDonorFund[i][qtr] + plDisbFund[i][qtr];
								disbDonorFund[i][qtr]=disbDonorFund[i][qtr] + disbFund[i][qtr];
								expDonorFund[i][qtr]=expDonorFund[i][qtr] + expFund[i][qtr];
							}
											
						}
						donorCommAmount=donorCommAmount + projCommAmount;
						donorPlannedDisbAmount=donorPlannedDisbAmount + projPlannedDisbAmount;
						donorDisbAmount=donorDisbAmount + projDisbAmount;
						donorExpAmount=donorExpAmount + projExpAmount;
						projCommAmount=projPlannedDisbAmount=projDisbAmount=projExpAmount=0;
						termComm=totalPlDisb=totalDisb=totalExp=0;
						for(int i=0;i<yrCount;i++)
						{
							for(int qtr=0;qtr<4;qtr++)
							{
								plDisbTermFunds[i][qtr]=0;
								disbTermFunds[i][qtr]=0;
								expTermFunds[i][qtr]=0;
								plDisbFund[i][qtr]=0;
								disbFund[i][qtr]=0;
								expFund[i][qtr]=0;
							}
						}
						project=new Project();
						project.setName(ampReportCache.getActivityName());
						project.setAmpActivityId(ampReportCache.getAmpActivityId());
						project.setCount(++projCount);
						project.setAmpFund(new ArrayList());
						project.setTermAssist(new ArrayList());
						if(ampReportCache.getTermAssistName()!=null)
							termAssistName=ampReportCache.getTermAssistName();
						termFlag=0;
						logger.debug("Outside Project");
					}
					
					if(ampReportCache.getTermAssistName()!=null)
					{
						if(report.getAmpTeamId().equals(ampReportCache.getAmpTeamId()) && ampReportCache.getDonorName().equals(ampTeamDonors.getDonorAgency()) && ampReportCache.getAmpActivityId().equals(project.getAmpActivityId()) && !(ampReportCache.getTermAssistName().equals(termAssistName)))
						{
							logger.debug("Inside Terms");
							if(termFlag==1)
							{
								termAssist=new ProjectTermAssist();
								termAssist.setTermAssistName(termAssistName);
								if(donorAssistance.indexOf(termAssist.getTermAssistName())==-1)
								{
									logger.debug("Donor Assistance Added:" + termAssist.getTermAssistName() + ":");
									donorAssistance.add(termAssist.getTermAssistName());
								}
								if(teamAssistance.indexOf(termAssist.getTermAssistName())==-1)
								{
									logger.debug("Team Assistance Added:" + termAssist.getTermAssistName() + ":");
									teamAssistance.add(termAssist.getTermAssistName());
								}
								termAssist.setTermAssistFund(new ArrayList());
								for(int i=0;i<yrCount;i++)
								{
									for(int qtr=0;qtr<4;qtr++)
									{
										AmpFund termFund = new AmpFund();
										termFund.setPlannedDisbAmount(mf.format(plDisbTermFunds[i][qtr])); 
										termFund.setDisbAmount(mf.format(disbTermFunds[i][qtr]));
										termFund.setExpAmount(mf.format(expTermFunds[i][qtr]));
										termAssist.getTermAssistFund().add(termFund);
										totalPlDisb=totalPlDisb + plDisbTermFunds[i][qtr];
										totalDisb=totalDisb + disbTermFunds[i][qtr];
										totalExp= totalExp + expTermFunds[i][qtr];
										plDisbFund[i][qtr]=plDisbFund[i][qtr] + plDisbTermFunds[i][qtr];
										disbFund[i][qtr]=disbFund[i][qtr] + disbTermFunds[i][qtr];
										expFund[i][qtr]=expFund[i][qtr] + expTermFunds[i][qtr];
									}
								}
								termAssist.setTermPlannedDisbAmount(mf.format(totalPlDisb));
								termAssist.setTermDisbAmount(mf.format(totalDisb));
								termAssist.setTermExpAmount(mf.format(totalExp));
								termAssist.setTermUnDisbAmount(mf.format(termComm - totalDisb));
								projCommAmount=projCommAmount + termComm;
								projPlannedDisbAmount=projPlannedDisbAmount + totalPlDisb;
								projDisbAmount=projDisbAmount + totalDisb;
								projExpAmount=projExpAmount + totalExp;
								project.getTermAssist().add(termAssist);
								if(termAssist.getTermAssistName().equals("Loan"))
								{
									for(int i=0;i<yrCount;i++)
									{
										for (int qtr=0;qtr<4 ;qtr++)
										{
											loanDisbDonorFunds[i][qtr]=loanDisbDonorFunds[i][qtr] + disbTermFunds[i][qtr];
											loanExpDonorFunds[i][qtr]=loanExpDonorFunds[i][qtr] + expTermFunds[i][qtr];
											loanPlDisbDonorFunds[i][qtr]=loanPlDisbDonorFunds[i][qtr] + plDisbTermFunds[i][qtr];
										}
									}
									loanCommDonorAmount=loanCommDonorAmount + termComm;
								}
								if(termAssist.getTermAssistName().equals("Grant"))
								{
									for(int i=0;i<yrCount;i++)
									{
										for (int qtr=0;qtr<4 ;qtr++)
										{
											grantDisbDonorFunds[i][qtr]=grantDisbDonorFunds[i][qtr] + disbTermFunds[i][qtr];
											grantExpDonorFunds[i][qtr]=grantExpDonorFunds[i][qtr] + expTermFunds[i][qtr];
											grantPlDisbDonorFunds[i][qtr]=grantPlDisbDonorFunds[i][qtr] + plDisbTermFunds[i][qtr];
										}
									}
									grantCommDonorAmount=grantCommDonorAmount + termComm;
								}
								if(termAssist.getTermAssistName().equals("Kind"))
								{
									for(int i=0;i<yrCount;i++)
									{
										for (int qtr=0;qtr<4 ;qtr++)
										{
											kindDisbDonorFunds[i][qtr]=kindDisbDonorFunds[i][qtr] + disbTermFunds[i][qtr];
											kindExpDonorFunds[i][qtr]=kindExpDonorFunds[i][qtr] + expTermFunds[i][qtr];
											kindPlDisbDonorFunds[i][qtr]=kindPlDisbDonorFunds[i][qtr] + plDisbTermFunds[i][qtr];
										}
									}
									kindCommDonorAmount=kindCommDonorAmount + termComm;
								}
								
							}
							termAssistName=ampReportCache.getTermAssistName();
							termFlag=0;
							for(int i=0;i<yrCount;i++)
							{
								for(int qtr=0;qtr<4;qtr++)
								{
									plDisbTermFunds[i][qtr]=0;
									disbTermFunds[i][qtr]=0;
									expTermFunds[i][qtr]=0;
								}
							}
							termComm=totalPlDisb=totalDisb=totalExp=0.0;
							logger.debug("Outside Terms");
						}
					}

					if(ampReportCache.getFiscalYear()!=null && ampReportCache.getFiscalQuarter()!=null)
					{
						//logger.debug("begin if");
						if(new Long(fiscalCalId).equals(Constants.GREGORIAN))
						{
							fiscalYear=ampReportCache.getFiscalYear().intValue();
							fiscalQuarter=ampReportCache.getFiscalQuarter().intValue();
						}
						if(ampReportCache.getCurrencyCode().equals("USD"))
							fromExchangeRate=1.0;
						else
							fromExchangeRate=DbUtil.getExchangeRate(ampReportCache.getCurrencyCode(),Constants.ACTUAL,ampReportCache.getTransactionDate());
						if(ampCurrencyCode.equals("USD"))
							toExchangeRate=1.0;
						else
							toExchangeRate=DbUtil.getExchangeRate(ampCurrencyCode,Constants.ACTUAL,ampReportCache.getTransactionDate());

						
						if(ampReportCache.getActualCommitment().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
						{
							amount=CurrencyWorker.convert1(ampReportCache.getActualCommitment().doubleValue(),fromExchangeRate,toExchangeRate);
							if(fiscalYear>=fromYr && fiscalYear<=toYr)
							{
								termFlag=1;
								termComm=termComm + amount;
							}
						}
						amount=0.0;
													
						if(ampReportCache.getActualDisbursement().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
						{
							amount=CurrencyWorker.convert1(ampReportCache.getActualDisbursement().doubleValue(),fromExchangeRate,toExchangeRate);
							if(fiscalYear>=fromYr && fiscalYear<=toYr)
							{
								termFlag=1;
								if(disbTermFunds[fiscalYear%fromYr][fiscalQuarter-1]==0)
									disbTermFunds[fiscalYear%fromYr][fiscalQuarter-1]=amount;
								else
								if(disbTermFunds[fiscalYear%fromYr][fiscalQuarter-1]>0)
									disbTermFunds[fiscalYear%fromYr][fiscalQuarter-1]=disbTermFunds[fiscalYear%fromYr][fiscalQuarter-1] + amount;
							}
						}
						amount=0.0;
						if(ampReportCache.getActualExpenditure().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
						{
							amount=CurrencyWorker.convert1(ampReportCache.getActualExpenditure().doubleValue(),fromExchangeRate,toExchangeRate);
							if(fiscalYear>=fromYr && fiscalYear<=toYr)
							{
								termFlag=1;
								if(expTermFunds[fiscalYear%fromYr][fiscalQuarter-1]==0)
									expTermFunds[fiscalYear%fromYr][fiscalQuarter-1]=amount;
								else
								if(expTermFunds[fiscalYear%fromYr][fiscalQuarter-1]>0)
									expTermFunds[fiscalYear%fromYr][fiscalQuarter-1]= expTermFunds[fiscalYear%fromYr][fiscalQuarter-1] + amount;
							}
						}
						amount=0.0;
						if(ampReportCache.getCurrencyCode().equals("USD"))
							fromExchangeRate=1.0;
						else
							fromExchangeRate=DbUtil.getExchangeRate(ampReportCache.getCurrencyCode(),Constants.PLANNED,ampReportCache.getTransactionDate());
						if(ampCurrencyCode.equals("USD"))
							toExchangeRate=1.0;
						else
							toExchangeRate=DbUtil.getExchangeRate(ampCurrencyCode,Constants.PLANNED,ampReportCache.getTransactionDate());
						if(ampReportCache.getPlannedDisbursement().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
						{
							amount=CurrencyWorker.convert1(ampReportCache.getPlannedDisbursement().doubleValue(),fromExchangeRate,toExchangeRate);
							if(fiscalYear>=fromYr && fiscalYear<=toYr)
							{
								termFlag=1;
								if(plDisbTermFunds[fiscalYear%fromYr][fiscalQuarter-1]==0)
									plDisbTermFunds[fiscalYear%fromYr][fiscalQuarter-1]=amount;
								else
								if(plDisbTermFunds[fiscalYear%fromYr][fiscalQuarter-1]>0)
									plDisbTermFunds[fiscalYear%fromYr][fiscalQuarter-1]=plDisbTermFunds[fiscalYear%fromYr][fiscalQuarter-1] + amount;
							}
						}
						amount=0.0;
										
					}

				}
				if(report!=null)
				{
					if(termFlag==1)
					{
						termAssist=new ProjectTermAssist();
						termAssist.setTermAssistName(termAssistName);
						if(donorAssistance.indexOf(termAssist.getTermAssistName())==-1)
						{
							logger.debug("Donor Assistance Added:" + termAssist.getTermAssistName() + ":");
							donorAssistance.add(termAssist.getTermAssistName());
						}
						if(teamAssistance.indexOf(termAssist.getTermAssistName())==-1)
						{
							logger.debug("Team Assistance Added:" + termAssist.getTermAssistName() + ":");
							teamAssistance.add(termAssist.getTermAssistName());
						}
						termAssist.setTermAssistFund(new ArrayList());
						for(int i=0;i<yrCount;i++)
						{
							for(int qtr=0;qtr<4;qtr++)
							{
								AmpFund termFund = new AmpFund();
								termFund.setPlannedDisbAmount(mf.format(plDisbTermFunds[i][qtr])); 
								termFund.setDisbAmount(mf.format(disbTermFunds[i][qtr]));
								termFund.setExpAmount(mf.format(expTermFunds[i][qtr]));
								termAssist.getTermAssistFund().add(termFund);
								totalPlDisb=totalPlDisb + plDisbTermFunds[i][qtr];
								totalDisb=totalDisb + disbTermFunds[i][qtr];
								totalExp= totalExp + expTermFunds[i][qtr];
								plDisbFund[i][qtr]=plDisbFund[i][qtr] + plDisbTermFunds[i][qtr];
								disbFund[i][qtr]=disbFund[i][qtr] + disbTermFunds[i][qtr];
								expFund[i][qtr]=expFund[i][qtr] + expTermFunds[i][qtr];
								}
							}
							termAssist.setTermPlannedDisbAmount(mf.format(totalPlDisb));
							termAssist.setTermDisbAmount(mf.format(totalDisb));
							termAssist.setTermExpAmount(mf.format(totalExp));
							termAssist.setTermUnDisbAmount(mf.format(termComm-totalDisb));
							projCommAmount=projCommAmount + termComm;
							projPlannedDisbAmount=projPlannedDisbAmount + totalPlDisb;
							projDisbAmount=projDisbAmount + totalDisb;
							projExpAmount=projExpAmount + totalExp;
							project.getTermAssist().add(termAssist);
							if(termAssist.getTermAssistName().equals("Loan"))
							{
								for(int i=0;i<yrCount;i++)
								{
									for (int qtr=0;qtr<4 ;qtr++)
									{
										loanDisbDonorFunds[i][qtr]=loanDisbDonorFunds[i][qtr] + disbTermFunds[i][qtr];
										loanExpDonorFunds[i][qtr]=loanExpDonorFunds[i][qtr] + expTermFunds[i][qtr];
										loanPlDisbDonorFunds[i][qtr]=loanPlDisbDonorFunds[i][qtr] + plDisbTermFunds[i][qtr];
									}
								}
								loanCommDonorAmount=loanCommDonorAmount + termComm;
							}
							if(termAssist.getTermAssistName().equals("Grant"))
							{
								for(int i=0;i<yrCount;i++)
								{
									for (int qtr=0;qtr<4 ;qtr++)
									{
										grantDisbDonorFunds[i][qtr]=grantDisbDonorFunds[i][qtr] + disbTermFunds[i][qtr];
										grantExpDonorFunds[i][qtr]=grantExpDonorFunds[i][qtr] + expTermFunds[i][qtr];
										grantPlDisbDonorFunds[i][qtr]=grantPlDisbDonorFunds[i][qtr] + plDisbTermFunds[i][qtr];
									}
								}
								grantCommDonorAmount=grantCommDonorAmount + termComm;
							}
							if(termAssist.getTermAssistName().equals("Kind"))
							{
								for(int i=0;i<yrCount;i++)
								{
									for (int qtr=0;qtr<4 ;qtr++)
									{
										kindDisbDonorFunds[i][qtr]=kindDisbDonorFunds[i][qtr] + disbTermFunds[i][qtr];
										kindExpDonorFunds[i][qtr]=kindExpDonorFunds[i][qtr] + expTermFunds[i][qtr];
										kindPlDisbDonorFunds[i][qtr]=kindPlDisbDonorFunds[i][qtr] + plDisbTermFunds[i][qtr];
									}
								}
								kindCommDonorAmount=kindCommDonorAmount + termComm;
							}
							
						}
						for(int i=0;i<yrCount;i++)
						{
							for(int qtr=0;qtr<4;qtr++)
							{
								AmpFund projFund = new AmpFund();
								projFund.setPlannedDisbAmount(mf.format(plDisbFund[i][qtr])); 
								projFund.setDisbAmount(mf.format(disbFund[i][qtr]));
								projFund.setExpAmount(mf.format(expFund[i][qtr]));
								project.getAmpFund().add(projFund) ;
							}
						}
						project.setProjPlannedDisbAmount(mf.format(projPlannedDisbAmount));
						project.setProjDisbAmount(mf.format(projDisbAmount));
						project.setProjExpAmount(mf.format(projExpAmount));
						project.setProjUnDisbAmount(mf.format(projCommAmount-projDisbAmount));
						ampTeamDonors.getProject().add(project);
						for(int i=0;i<yrCount;i++)
						{
							for(int qtr=0;qtr<4;qtr++)
							{
								plDisbDonorFund[i][qtr]=plDisbDonorFund[i][qtr] + plDisbFund[i][qtr];
								disbDonorFund[i][qtr]=disbDonorFund[i][qtr] + disbFund[i][qtr];
								expDonorFund[i][qtr]=expDonorFund[i][qtr] + expFund[i][qtr];
							}
											
						}
						donorCommAmount=donorCommAmount + projCommAmount;
						donorPlannedDisbAmount=donorPlannedDisbAmount + projPlannedDisbAmount;
						donorDisbAmount=donorDisbAmount + projDisbAmount;
						donorExpAmount=donorExpAmount + projExpAmount;

						
						for(int i=0;i<yrCount;i++)
						{
							for(int qtr=0; qtr<4;qtr++)
							{
								FundTotal total=new FundTotal();
								total.setTotPlannedDisbAmount(mf.format(plDisbDonorFund[i][qtr]));
								total.setTotDisbAmount(mf.format(disbDonorFund[i][qtr]));
								total.setTotExpAmount(mf.format(expDonorFund[i][qtr]));
								ampTeamDonors.getTotalDonorFund().add(total);	
							}
						}
						ampTeamDonors.setDonorPlannedDisbAmount(mf.format(donorPlannedDisbAmount));
						ampTeamDonors.setDonorDisbAmount(mf.format(donorDisbAmount));
						ampTeamDonors.setDonorExpAmount(mf.format(donorExpAmount));
						ampTeamDonors.setDonorUnDisbAmount(mf.format(donorCommAmount-donorDisbAmount));
//						ampTeamDonors.getTotalDonorTermAssistFund().addAll(donorTotal);
						for(int i=0;i<yrCount;i++)
						{
							for(int qtr=0;qtr<4;qtr++)
							{
								plDisbTeamFund[i][qtr]=plDisbTeamFund[i][qtr] + plDisbDonorFund[i][qtr];
								disbTeamFund[i][qtr]=disbTeamFund[i][qtr] + disbDonorFund[i][qtr];
								expTeamFund[i][qtr]=expTeamFund[i][qtr] + expDonorFund[i][qtr];
							}
											
						}
						teamCommAmount=teamCommAmount + donorCommAmount;
						teamPlannedDisbAmount=teamPlannedDisbAmount + donorPlannedDisbAmount;
						teamDisbAmount=teamDisbAmount + donorDisbAmount;
						teamExpAmount=teamExpAmount + donorExpAmount;
//						ampTeamDonors.setTotalDonorTermAssistFund(new ArrayList());
//						logger.debug("Size of Assistance:" + assistance.size() + ":");
						totalPlDisb=totalDisb=totalExp=0.0;
						Iterator donorIter=donorAssistance.iterator();
						while(donorIter.hasNext())
						{
							String assist=(String) donorIter.next();
							logger.debug("Assist:" + assist + ":");
							TermFund termFund=new TermFund();
							if(assist.equals("Loan"))
							{
								termFund.setTermAssistName("Loan");
								termFund.setTermFundTotal(new ArrayList());
								for(int i=0;i<yrCount;i++)
								{
									for(int qtr=0;qtr<4;qtr++)
									{
										TermFundTotal termFundTotal=new TermFundTotal();
										termFundTotal.setTotDisbAmount(mf.format(loanDisbDonorFunds[i][qtr]));
										termFundTotal.setTotExpAmount(mf.format(loanExpDonorFunds[i][qtr]));
										termFundTotal.setTotPlannedDisbAmount(mf.format(loanPlDisbDonorFunds[i][qtr]));
										termFund.getTermFundTotal().add(termFundTotal);
										totalDisb=totalDisb + loanDisbDonorFunds[i][qtr];
										totalExp=totalExp + loanExpDonorFunds[i][qtr];
										totalPlDisb=totalPlDisb + loanPlDisbDonorFunds[i][qtr];
										loanDisbTeamFunds[i][qtr]=loanDisbTeamFunds[i][qtr] + loanDisbDonorFunds[i][qtr];
										loanExpTeamFunds[i][qtr]=loanExpTeamFunds[i][qtr] + loanExpDonorFunds[i][qtr];
										loanPlDisbTeamFunds[i][qtr]=loanPlDisbTeamFunds[i][qtr] + loanPlDisbDonorFunds[i][qtr];
									}
								}
								loanCommTeamAmount=loanCommTeamAmount + loanCommDonorAmount;
								termFund.setTotDonorDisbAmount(mf.format(totalDisb));
								termFund.setTotDonorExpAmount(mf.format(totalExp));
								termFund.setTotDonorPlannedDisbAmount(mf.format(totalPlDisb));
								termFund.setTotDonorUnDisbAmount(mf.format(loanCommDonorAmount-totalDisb));
								totalPlDisb=totalDisb=totalExp=0.0;
							}
							if(assist.equals("Grant"))
							{
								termFund.setTermAssistName("Grant");
								termFund.setTermFundTotal(new ArrayList());
								for(int i=0;i<yrCount;i++)
								{
									for(int qtr=0;qtr<4;qtr++)
									{
										TermFundTotal termFundTotal=new TermFundTotal();
										termFundTotal.setTotDisbAmount(mf.format(grantDisbDonorFunds[i][qtr]));
											termFundTotal.setTotExpAmount(mf.format(grantExpDonorFunds[i][qtr]));
										termFundTotal.setTotPlannedDisbAmount(mf.format(grantPlDisbDonorFunds[i][qtr]));
										termFund.getTermFundTotal().add(termFundTotal);
										totalDisb=totalDisb + grantDisbDonorFunds[i][qtr];
										totalExp=totalExp + grantExpDonorFunds[i][qtr];
										totalPlDisb=totalPlDisb + grantPlDisbDonorFunds[i][qtr];
										grantDisbTeamFunds[i][qtr]=grantDisbTeamFunds[i][qtr] + grantDisbDonorFunds[i][qtr];
										grantExpTeamFunds[i][qtr]=grantExpTeamFunds[i][qtr] + grantExpDonorFunds[i][qtr];
										grantPlDisbTeamFunds[i][qtr]=grantPlDisbTeamFunds[i][qtr] + grantPlDisbDonorFunds[i][qtr];
									}
								}
								grantCommTeamAmount=grantCommTeamAmount + grantCommDonorAmount;
								termFund.setTotDonorDisbAmount(mf.format(totalDisb));
								termFund.setTotDonorExpAmount(mf.format(totalExp));
								termFund.setTotDonorPlannedDisbAmount(mf.format(totalPlDisb));
								termFund.setTotDonorUnDisbAmount(mf.format(grantCommDonorAmount-totalDisb));
								totalPlDisb=totalDisb=totalExp=0.0;
							}
							if(assist.equals("In Kind"))
							{
								termFund.setTermAssistName("In Kind");
								termFund.setTermFundTotal(new ArrayList());
								for(int i=0;i<yrCount;i++)
								{
									for(int qtr=0;qtr<4;qtr++)
									{
										TermFundTotal termFundTotal=new TermFundTotal();
										termFundTotal.setTotDisbAmount(mf.format(kindDisbDonorFunds[i][qtr]));
										termFundTotal.setTotExpAmount(mf.format(kindExpDonorFunds[i][qtr]));
										termFundTotal.setTotPlannedDisbAmount(mf.format(kindPlDisbDonorFunds[i][qtr]));
										termFund.getTermFundTotal().add(termFundTotal);
										totalDisb=totalDisb + kindDisbDonorFunds[i][qtr];
										totalExp=totalExp + kindExpDonorFunds[i][qtr];
										totalPlDisb=totalPlDisb + kindPlDisbDonorFunds[i][qtr];
										kindDisbTeamFunds[i][qtr]=kindDisbTeamFunds[i][qtr] + kindDisbDonorFunds[i][qtr];
										kindExpTeamFunds[i][qtr]=kindExpTeamFunds[i][qtr] + kindExpDonorFunds[i][qtr];
										kindPlDisbTeamFunds[i][qtr]=kindPlDisbTeamFunds[i][qtr] + kindPlDisbDonorFunds[i][qtr];
									}
								}
								kindCommTeamAmount=kindCommTeamAmount + kindCommDonorAmount;
								termFund.setTotDonorDisbAmount(mf.format(totalDisb));
								termFund.setTotDonorExpAmount(mf.format(totalExp));
								termFund.setTotDonorPlannedDisbAmount(mf.format(totalPlDisb));
								termFund.setTotDonorUnDisbAmount(mf.format(kindCommDonorAmount-totalDisb));
								totalPlDisb=totalDisb=totalExp=0.0;
							}
//							logger.debug("Added:'" + termFund.getTermAssistName());
							ampTeamDonors.getTotalDonorTermAssistFund().add(termFund);	
						}
						report.getDonors().add(ampTeamDonors);
						totalPlDisb=totalDisb=totalExp=0.0;
						Iterator teamIter=teamAssistance.iterator();
						while(teamIter.hasNext())
						{
							String assist=(String) teamIter.next();
							logger.debug("Assist:" + assist + ":");
							TermFund termFund=new TermFund();
							if(assist.equals("Loan"))
							{
								termFund.setTermAssistName("Loan");
								termFund.setTermFundTotal(new ArrayList());
								for(int i=0;i<yrCount;i++)
								{
									for(int qtr=0;qtr<4;qtr++)
									{
										TermFundTotal termFundTotal=new TermFundTotal();
										termFundTotal.setTotDisbAmount(mf.format(loanDisbTeamFunds[i][qtr]));
										termFundTotal.setTotExpAmount(mf.format(loanExpTeamFunds[i][qtr]));
										termFundTotal.setTotPlannedDisbAmount(mf.format(loanPlDisbTeamFunds[i][qtr]));
										termFund.getTermFundTotal().add(termFundTotal);
										totalDisb=totalDisb + loanDisbTeamFunds[i][qtr];
										totalExp=totalExp + loanExpTeamFunds[i][qtr];
										totalPlDisb=totalPlDisb + loanPlDisbTeamFunds[i][qtr];
										
									}
								}
								termFund.setTotDonorDisbAmount(mf.format(totalDisb));
								termFund.setTotDonorExpAmount(mf.format(totalExp));
								termFund.setTotDonorPlannedDisbAmount(mf.format(totalPlDisb));
								termFund.setTotDonorUnDisbAmount(mf.format(loanCommTeamAmount-totalDisb));
								totalPlDisb=totalDisb=totalExp=0.0;
							}
							if(assist.equals("Grant"))
							{
								termFund.setTermAssistName("Grant");
								termFund.setTermFundTotal(new ArrayList());
								for(int i=0;i<yrCount;i++)
								{
									for(int qtr=0;qtr<4;qtr++)
									{
										TermFundTotal termFundTotal=new TermFundTotal();
										termFundTotal.setTotDisbAmount(mf.format(grantDisbTeamFunds[i][qtr]));
										termFundTotal.setTotExpAmount(mf.format(grantExpTeamFunds[i][qtr]));
										termFundTotal.setTotPlannedDisbAmount(mf.format(grantPlDisbTeamFunds[i][qtr]));
										termFund.getTermFundTotal().add(termFundTotal);
										totalDisb=totalDisb + grantDisbTeamFunds[i][qtr];
										totalExp=totalExp + grantExpTeamFunds[i][qtr];
										totalPlDisb=totalPlDisb + grantPlDisbTeamFunds[i][qtr];
										
									}
								}
								termFund.setTotDonorDisbAmount(mf.format(totalDisb));
								termFund.setTotDonorExpAmount(mf.format(totalExp));
								termFund.setTotDonorPlannedDisbAmount(mf.format(totalPlDisb));
								termFund.setTotDonorUnDisbAmount(mf.format(grantCommTeamAmount-totalDisb));
								totalPlDisb=totalDisb=totalExp=0.0;
							}
							if(assist.equals("In Kind"))
							{
								termFund.setTermAssistName("In Kind");
								termFund.setTermFundTotal(new ArrayList());
								for(int i=0;i<yrCount;i++)
								{
									for(int qtr=0;qtr<4;qtr++)
									{
										TermFundTotal termFundTotal=new TermFundTotal();
										termFundTotal.setTotDisbAmount(mf.format(kindDisbTeamFunds[i][qtr]));
										termFundTotal.setTotExpAmount(mf.format(kindExpTeamFunds[i][qtr]));
										termFundTotal.setTotPlannedDisbAmount(mf.format(kindPlDisbTeamFunds[i][qtr]));
										termFund.getTermFundTotal().add(termFundTotal);
										totalDisb=totalDisb + kindDisbTeamFunds[i][qtr];
										totalExp=totalExp + kindExpTeamFunds[i][qtr];
										totalPlDisb=totalPlDisb + kindPlDisbTeamFunds[i][qtr];
										
									}
								}
								termFund.setTotDonorDisbAmount(mf.format(totalDisb));
								termFund.setTotDonorExpAmount(mf.format(totalExp));
								termFund.setTotDonorPlannedDisbAmount(mf.format(totalPlDisb));
								termFund.setTotDonorUnDisbAmount(mf.format(kindCommTeamAmount-totalDisb));
								totalPlDisb=totalDisb=totalExp=0.0;
							}
//							logger.debug("Added:'" + termFund.getTermAssistName());
							report.getTotalTeamTermAssistFund().add(termFund);	
						}
//							ampTeamDonors.getTotalDonorTermAssistFund().addAll(donorTotal);
//							report.getDonors().add(ampTeamDonors);
							for(int i=0;i<yrCount;i++)
							{
								for(int qtr=0;qtr<4;qtr++)
								{
									FundTotal total=new FundTotal();
									total.setTotPlannedDisbAmount(mf.format(plDisbTeamFund[i][qtr]));
									total.setTotDisbAmount(mf.format(disbTeamFund[i][qtr]));
									total.setTotExpAmount(mf.format(expTeamFund[i][qtr]));
									report.getTotalTeamFund().add(total);	
								}
							}
							report.setTeamPlannedDisbAmount(mf.format(teamPlannedDisbAmount));
							report.setTeamDisbAmount(mf.format(teamDisbAmount));
							report.setTeamExpAmount(mf.format(teamExpAmount));
							report.setTeamUnDisbAmount(mf.format(teamCommAmount-teamDisbAmount));
							projCount=0;
										
							ampTeamDonors=null;
							if(ampDonorId.equals(All))
							{
								logger.debug("Inside Unspecified");
								queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.donorName is null) and (report.ampTeamId='" + selTeamId + "') and (report.reportType='1') order by report.activityName,report.ampActivityId";
							q = session.createQuery(queryString);	
							Iterator iterUn=q.list().iterator();
							if(q.list().size()>0)
							{
								while(iterUn.hasNext())
								{
									AmpReportCache ampCache= (AmpReportCache) iterUn.next();

									if(approvedActivityList.indexOf(ampCache.getAmpActivityId())==-1)
										continue;

									if(!ampModalityId.equals(All))
									{
										if(ampCache.getAmpModalityId()==null)
											continue;
										if(!(ampModalityId.equals(ampCache.getAmpModalityId())))
											continue;
									}

									if(!ampStatusId.equals(All))
									{
										if(ampCache.getAmpStatusId()==null)
											continue;
										if(!(ampStatusId.equals(ampCache.getAmpStatusId())))
											continue;
									}
	
									if(!ampSectorId.equals(All))
									{
										int sflag=0;
										iterSector=DbUtil.getAmpReportSectorId(ampCache.getAmpActivityId()).iterator();
										while(iterSector.hasNext())
										{
											AmpReportSector sector=(AmpReportSector) iterSector.next();
											if(sector.getAmpSectorId().equals(ampSectorId))
											{
												sflag=1;
												break;
											}
											if(sector.getAmpSubSectorId().equals(new Long(0)))
											{
												if(new Long(sector.getSubSectorName()).equals(ampSectorId))
												{
													sflag=1;
													break;
												}
											}
											if(!(sector.getAmpSubSectorId().equals(new Long(0))) && sector.getAmpSubSectorId().equals(ampSectorId))
											{
												sflag=1;
												break;
											}
										}
										if (sflag==0)
										{
											continue;
										}
									}
									if(!region.equals("All"))
									{
										ArrayList location=(ArrayList)DbUtil.getAmpReportLocation(ampCache.getAmpActivityId());
										if(location.indexOf(region)==-1)
											continue;
									}
									if(ampTeamDonors==null)
									{
										ampTeamDonors = new AmpTeamDonors();
										ampTeamDonors.setDonorAgency("Unspecified");
										ampTeamDonors.setDonorCount(++donorCount);
										ampTeamDonors.setProject(new ArrayList());
										ampTeamDonors.setTotalDonorFund(new ArrayList());
									}
									project=new Project();
									project.setName(ampCache.getActivityName());
									project.setAmpActivityId(ampCache.getAmpActivityId());
									project.setCount(++projCount);
									project.setAmpFund(new ArrayList());
									for(int i=0;i<yrCount;i++)
									{
										for(int qtr=0;qtr<4;qtr++)
										{
											AmpFund ampFund = new AmpFund();
											ampFund.setPlannedDisbAmount(mf.format(0)); 
											ampFund.setDisbAmount(mf.format(0));
											ampFund.setExpAmount(mf.format(0));
											project.getAmpFund().add(ampFund) ;
										}
									}
									project.setProjPlannedDisbAmount(mf.format(0));
									project.setProjDisbAmount(mf.format(0));
									project.setProjExpAmount(mf.format(0));
									project.setProjUnDisbAmount(mf.format(0));
									ampTeamDonors.getProject().add(project);
								}
								if(ampTeamDonors!=null)
								{
									for(int i=0;i<yrCount;i++)
									{
										for(int qtr=0;qtr<4;qtr++)
										{
											FundTotal total=new FundTotal();
											total.setTotPlannedDisbAmount(mf.format(0));
											total.setTotDisbAmount(mf.format(0));
											total.setTotExpAmount(mf.format(0));
											ampTeamDonors.getTotalDonorFund().add(total);	
										}
									}
									ampTeamDonors.setDonorPlannedDisbAmount(mf.format(0));
									ampTeamDonors.setDonorDisbAmount(mf.format(0));
									ampTeamDonors.setDonorExpAmount(mf.format(0));
									ampTeamDonors.setDonorUnDisbAmount(mf.format(0));
									report.getDonors().add(ampTeamDonors);
								}
							}
						}
						ampReports.add(report);
				}
			}
		}
		catch(Exception ex) 		
		{
			logger.debug("Unable to get report names  from database " + ex.getMessage());
		}
		finally 
		{
			try 
			{
				PersistenceManager.releaseSession(session);
			}
			catch (Exception ex2) 
			{
				logger.debug("releaseSession() failed ");
			}
		}
		return ampReports ;
	}

	public static ArrayList getAmpQuarterlyReportMultilateral(Long ampTeamId,int fromYr,int toYr,String perspective,String ampCurrencyCode,Long ampModalityId,Long ampStatusId,Long ampDonorId,Long ampSectorId,int fiscalCalId,String startDate,String closeDate,String region)
	{
		Session session = null ;
		Query q = null ;
		ArrayList ampReports = new ArrayList() ;
		ArrayList teamAssistance=new ArrayList();
		String queryString = null;
		Iterator iter=null;
		Long All=new Long(0);
		int yrCount = (toYr - fromYr)+1;
		int fiscalYear=0,fiscalQuarter=0;
		double[][] commFund=new double[yrCount][4];
		double[][] plDisbFund=new double[yrCount][4];
		double[][] disbFund=new double[yrCount][4];
		double[][] expFund=new double[yrCount][4];

		double[][] commTeamFund=new double[yrCount][4];
		double[][] plDisbTeamFund=new double[yrCount][4];
		double[][] disbTeamFund=new double[yrCount][4];
		double[][] expTeamFund=new double[yrCount][4];
		
		double[][] commTermFunds=new double[yrCount][4];
		double[][] plDisbTermFunds=new double[yrCount][4];
		double[][] disbTermFunds=new double[yrCount][4];
		double[][] expTermFunds=new double[yrCount][4];

		double[][] loanCommTeamFunds=new double[yrCount][4];
		double[][] loanPlDisbTeamFunds=new double[yrCount][4];
		double[][] loanDisbTeamFunds=new double[yrCount][4];
		double[][] loanExpTeamFunds=new double[yrCount][4];
		double[][] grantCommTeamFunds=new double[yrCount][4];
		double[][] grantPlDisbTeamFunds=new double[yrCount][4];
		double[][] grantDisbTeamFunds=new double[yrCount][4];
		double[][] grantExpTeamFunds=new double[yrCount][4];
		double[][] kindCommTeamFunds=new double[yrCount][4];
		double[][] kindPlDisbTeamFunds=new double[yrCount][4];
		double[][] kindDisbTeamFunds=new double[yrCount][4];
		double[][] kindExpTeamFunds=new double[yrCount][4];

		double[][] totComm=new double[yrCount][4];
		double[][] totDisb=new double[yrCount][4];
		double[][] totExp=new double[yrCount][4];
		double[][] totQtr=new double[yrCount][3];
		double totalComm = 0.0 ;
		double totalPlDisb = 0.0 ;
		double totalDisb = 0.0 ;
		double totalExp = 0.0;
		double toExchangeRate=1.0;
		double fromExchangeRate=0.0;
		double amount=0.0;
		int fiscalYearFlag=0;
		int flag=0;
		int termFlag=0;
		double projCommAmount=0.0;
		double projDisbAmount=0.0;
		double projExpAmount=0.0;
		double donorCommAmount=0.0;
		double donorPlDisbAmount=0.0;
		double donorDisbAmount=0.0;
		double donorExpAmount=0.0;
		double donorUnDisbAmount=0.0;
		double teamCommAmount=0.0;
		double teamPlDisbAmount=0.0;
		double teamDisbAmount=0.0;
		double teamExpAmount=0.0;
		double teamUnDisbAmount=0.0;
		double actualCommitment=0.0;
		double actualDisbursement=0.0;
		
		double loanUnDisbTeamFund=0.0;
		double grantUnDisbTeamFund=0.0;
		double kindUnDisbTeamFund=0.0;

		int donorCount=0;
		int teamCount=0;
		Iterator iterSector=null;
		String inClause=null;
		String termAssistName=null;
		DecimalFormat mf = new DecimalFormat("###,###,###,###,###") ;
		ArrayList approvedActivityList=new ArrayList();
				
		try
		{

			ArrayList dbReturnSet=(ArrayList)DbUtil.getAmpLevel0Teams(ampTeamId);				
			if(dbReturnSet.size()==0)
				inClause= "'" + ampTeamId + "'";
			else
			{
				iter=dbReturnSet.iterator();
				while(iter.hasNext())
				{
					Long teamId= (Long) iter.next();
					if(inClause==null)
						inClause="'" + teamId + "'";
					else
						inClause=inClause + ",'" + teamId + "'";
				}
			}
			logger.debug("Inclause: " + inClause);
			approvedActivityList=DbUtil.getApprovedActivities(inClause);
			session = PersistenceManager.getSession();
			if(startDate==null && closeDate==null)
				queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.ampTeamId in(" + inClause + ")) and (report.donorName is not null) and (report.termAssistName is not null) and (report.reportType='1') order by report.ampTeamId,report.donorName,report.termAssistName,report.fiscalYear";
			else
				queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.ampTeamId in(" + inClause + ")) and (report.actualStartDate='" + startDate + "' or report.actualCompletionDate='" + closeDate + "') and (report.donorName is not null) and (report.termAssistName is not null) and (report.reportType='1') order by report.ampTeamId,report.donorName,report.termAssistName,report.fiscalYear";
			q = session.createQuery(queryString);	
			logger.debug("Number of Records: " + q.list().size());
			multiReport report =null;
			AmpDonors ampDonors =null;
			Project project=null;
			AmpTeamDonors ampTeamDonors=null;
			TermFund donorTermAssist=null;
			TermFund teamTermAssist=null;
			if(q!=null)
			{
				iter = q.list().iterator();
				while(iter.hasNext())
				{
					AmpReportCache ampReportCache = (AmpReportCache) iter.next(); 
					
					if(approvedActivityList.indexOf(ampReportCache.getAmpActivityId())==-1)
						continue;

					if(ampReportCache.getAmpDonorId()==null)
						continue;

					if(new Long(fiscalCalId).equals(Constants.ETH_FY) || new Long(fiscalCalId).equals(Constants.ETH_CAL))
					{
						if(ampReportCache.getTransactionDate()!=null)
						{
							GregorianCalendar calendar = new GregorianCalendar();
							EthiopianCalendar ec=new EthiopianCalendar();
							EthiopianCalendar tempDate=new EthiopianCalendar();
							calendar.setTime(ampReportCache.getTransactionDate());
							ec=tempDate.getEthiopianDate(calendar);
							if(new Long(fiscalCalId).equals(Constants.ETH_FY))
							{
								fiscalYear=(int)ec.ethFiscalYear;
								fiscalQuarter=(int)ec.ethFiscalQrt;
							}
							if(new Long(fiscalCalId).equals(Constants.ETH_CAL))
							{
								fiscalYear=(int)ec.ethYear;
								fiscalQuarter=(int)ec.ethQtr;
							}
							logger.debug("Ethiopian Fiscal Year: " + fiscalYear);
							logger.debug("From Year: " + fromYr);
							logger.debug("From Year: " + toYr);
/*							if(fiscalYear<fromYr || fiscalYear>toYr)
								continue;*/
						}
					}

					if(!ampModalityId.equals(All))
					{
						if(ampReportCache.getAmpModalityId()==null)
							continue;
						if(!(ampModalityId.equals(ampReportCache.getAmpModalityId())))
							continue;
					}

					if(!ampStatusId.equals(All))
					{
						if(ampReportCache.getAmpStatusId()==null)
							continue;
						if(!(ampStatusId.equals(ampReportCache.getAmpStatusId())))
							continue;
					}
	
					if(!ampDonorId.equals(All))
					{
						if(ampReportCache.getAmpDonorId()==null)
							continue;
						if(!(ampDonorId.equals(ampReportCache.getAmpDonorId())))
							continue;
					}
		
					if(!ampSectorId.equals(All))
					{
						int sflag=0;
						iterSector=DbUtil.getAmpReportSectorId(ampReportCache.getAmpActivityId()).iterator();
						while(iterSector.hasNext())
						{
							AmpReportSector sector=(AmpReportSector) iterSector.next();
							if(sector.getAmpSectorId().equals(ampSectorId))
							{
								sflag=1;
								break;
							}
							if(sector.getAmpSubSectorId().equals(new Long(0)))
							{
								if(new Long(sector.getSubSectorName()).equals(ampSectorId))
								{
									sflag=1;
									break;
								}
							}
							if(!(sector.getAmpSubSectorId().equals(new Long(0))) && sector.getAmpSubSectorId().equals(ampSectorId))
							{
								sflag=1;
								break;
							}
						}
						if (sflag==0)
						{
							continue;
						}
					}
					
					if(!region.equals("All"))
					{
						ArrayList location=(ArrayList)DbUtil.getAmpReportLocation(ampReportCache.getAmpActivityId());
						if(location.indexOf(region)==-1)
							continue;
					}					
					if(report==null || !(report.getAmpTeamId().equals(ampReportCache.getAmpTeamId())))
					{
						logger.debug("New donor: " + ampReportCache.getDonorName());
						if(report!=null)
						{
							logger.debug("Inside different donors");
							if(termFlag==1)
							{
								donorTermAssist=new TermFund();
								donorTermAssist.setTermAssistName(termAssistName);
								donorTermAssist.setTermFundTotal(new ArrayList());
								if(teamAssistance.indexOf(donorTermAssist.getTermAssistName())==-1)
									teamAssistance.add(donorTermAssist.getTermAssistName());
								for(int i=0;i<yrCount;i++)
								{
									for(int qtr=0;qtr<4;qtr++)
									{
										TermFundTotal termFundTotal=new TermFundTotal();
										termFundTotal.setTotCommAmount(mf.format(commTermFunds[i][qtr]));
										termFundTotal.setTotDisbAmount(mf.format(disbTermFunds[i][qtr]));
										termFundTotal.setTotExpAmount(mf.format(expTermFunds[i][qtr]));
										termFundTotal.setTotPlannedDisbAmount(mf.format(plDisbTermFunds[i][qtr]));
										donorTermAssist.getTermFundTotal().add(termFundTotal);
									
										totalComm=totalComm + commTermFunds[i][qtr];
										totalPlDisb=totalPlDisb + plDisbTermFunds[i][qtr];
										totalDisb=totalDisb + disbTermFunds[i][qtr];
										totalExp= totalExp + expTermFunds[i][qtr];
										commFund[i][qtr]=commFund[i][qtr] + commTermFunds[i][qtr];
										plDisbFund[i][qtr]=plDisbFund[i][qtr] + plDisbTermFunds[i][qtr];
										disbFund[i][qtr]=disbFund[i][qtr] + disbTermFunds[i][qtr];
										expFund[i][qtr]=expFund[i][qtr] + expTermFunds[i][qtr];
									}
								}
								donorTermAssist.setTotDonorCommAmount(mf.format(totalComm));
								donorTermAssist.setTotDonorDisbAmount(mf.format(totalDisb));
								donorTermAssist.setTotDonorExpAmount(mf.format(totalExp));
								donorTermAssist.setTotDonorPlannedDisbAmount(mf.format(totalPlDisb));
								donorTermAssist.setTotDonorUnDisbAmount(mf.format(actualCommitment-actualDisbursement));
								ampTeamDonors.getTotalDonorTermAssistFund().add(donorTermAssist);
								donorCommAmount=donorCommAmount + totalComm;
								donorPlDisbAmount=donorPlDisbAmount + totalPlDisb;
								donorDisbAmount=donorDisbAmount + totalDisb;
								donorExpAmount=donorExpAmount + totalExp;
								donorUnDisbAmount=donorUnDisbAmount + (actualCommitment - actualDisbursement);
								if(donorTermAssist.getTermAssistName().equals("Loan"))
								{
									for(int i=0;i<yrCount;i++)
									{
										for (int qtr=0;qtr<4 ;qtr++)
										{
											loanCommTeamFunds[i][qtr]=loanCommTeamFunds[i][qtr] + commTermFunds[i][qtr];
											loanDisbTeamFunds[i][qtr]=loanDisbTeamFunds[i][qtr] + disbTermFunds[i][qtr];
											loanExpTeamFunds[i][qtr]=loanExpTeamFunds[i][qtr] + expTermFunds[i][qtr];
											loanPlDisbTeamFunds[i][qtr]=loanPlDisbTeamFunds[i][qtr] + plDisbTermFunds[i][qtr];
										}
									
									}
									loanUnDisbTeamFund=loanUnDisbTeamFund + (actualCommitment - actualDisbursement);
								}
								if(donorTermAssist.getTermAssistName().equals("Grant"))
								{
									for(int i=0;i<yrCount;i++)
									{
										for (int qtr=0;qtr<4 ;qtr++)
										{
											grantCommTeamFunds[i][qtr]=grantCommTeamFunds[i][qtr] + commTermFunds[i][qtr];
											grantDisbTeamFunds[i][qtr]=grantDisbTeamFunds[i][qtr] + disbTermFunds[i][qtr];
											grantExpTeamFunds[i][qtr]=grantExpTeamFunds[i][qtr] + expTermFunds[i][qtr];
											grantPlDisbTeamFunds[i][qtr]=grantPlDisbTeamFunds[i][qtr] + plDisbTermFunds[i][qtr];
										}
									}
									grantUnDisbTeamFund=grantUnDisbTeamFund + (actualCommitment - actualDisbursement);
								}
								if(donorTermAssist.getTermAssistName().equals("In Kind"))
								{
									for(int i=0;i<yrCount;i++)
									{
										for (int qtr=0;qtr<4 ;qtr++)
										{
											kindCommTeamFunds[i][qtr]=kindCommTeamFunds[i][qtr] + commTermFunds[i][qtr];
											kindDisbTeamFunds[i][qtr]=kindDisbTeamFunds[i][qtr] + disbTermFunds[i][qtr];
											kindExpTeamFunds[i][qtr]=kindExpTeamFunds[i][qtr] + expTermFunds[i][qtr];
											kindPlDisbTeamFunds[i][qtr]=kindPlDisbTeamFunds[i][qtr] + plDisbTermFunds[i][qtr];
										}
									}
									kindUnDisbTeamFund=kindUnDisbTeamFund + (actualCommitment - actualDisbursement);
								}
							}
							for(int i=0;i<yrCount;i++)
							{
								for(int qtr=0;qtr<4;qtr++)
								{
									FundTotal total=new FundTotal();
									total.setTotCommAmount(mf.format(commFund[i][qtr]));
									total.setTotDisbAmount(mf.format(disbFund[i][qtr]));
									total.setTotExpAmount(mf.format(expFund[i][qtr]));
									total.setTotPlannedDisbAmount(mf.format(plDisbFund[i][qtr]));
									ampTeamDonors.getTotalDonorFund().add(total);	
								
									commTeamFund[i][qtr]=commTeamFund[i][qtr] + commFund[i][qtr];
									plDisbTeamFund[i][qtr]=plDisbTeamFund[i][qtr] + plDisbFund[i][qtr];
									disbTeamFund[i][qtr]=disbTeamFund[i][qtr] + disbFund[i][qtr];
									expTeamFund[i][qtr]=expTeamFund[i][qtr] + expFund[i][qtr];
								}
											
							}
							ampTeamDonors.setDonorCommAmount(mf.format(donorCommAmount));
							ampTeamDonors.setDonorDisbAmount(mf.format(donorDisbAmount));
							ampTeamDonors.setDonorExpAmount(mf.format(donorExpAmount));
							ampTeamDonors.setDonorPlannedDisbAmount(mf.format(donorPlDisbAmount));
							ampTeamDonors.setDonorUnDisbAmount(mf.format(donorUnDisbAmount));
							report.getDonors().add(ampTeamDonors);
							teamCommAmount=teamCommAmount + donorCommAmount;
							teamDisbAmount=teamDisbAmount + donorDisbAmount;
							teamExpAmount=teamExpAmount + donorExpAmount;
							teamPlDisbAmount=teamPlDisbAmount + donorPlDisbAmount;
							teamUnDisbAmount=teamUnDisbAmount + donorUnDisbAmount;
							
							for(int i=0;i<yrCount;i++)
							{
								for(int qtr=0;qtr<4 ;qtr++)
								{
									FundTotal total=new FundTotal();
									total.setTotCommAmount(mf.format(commTeamFund[i][qtr]));
									total.setTotDisbAmount(mf.format(disbTeamFund[i][qtr]));
									total.setTotExpAmount(mf.format(expTeamFund[i][qtr]));
									total.setTotPlannedDisbAmount(mf.format(plDisbTeamFund[i][qtr]));
									report.getTotalTeamFund().add(total);	
								}
							}
							report.setTeamCommAmount(mf.format(teamCommAmount));
							report.setTeamDisbAmount(mf.format(teamDisbAmount));
							report.setTeamExpAmount(mf.format(teamExpAmount));
							report.setTeamPlannedDisbAmount(mf.format(teamPlDisbAmount));
							report.setTeamUnDisbAmount(mf.format(teamUnDisbAmount));
							totalComm=totalPlDisb=totalDisb=totalExp=0.0;
							Iterator teamIter=teamAssistance.iterator();
							while(teamIter.hasNext())
							{
								String assist=(String) teamIter.next();
								logger.debug("Assist:" + assist + ":");
								TermFund termFund=new TermFund();
								if(assist.equals("Loan"))
								{
									termFund.setTermAssistName("Loan");
									termFund.setTermFundTotal(new ArrayList());
									for(int i=0;i<yrCount;i++)
									{
										for(int qtr=0;qtr<4;qtr++)
										{
											TermFundTotal termFundTotal=new TermFundTotal();
											termFundTotal.setTotCommAmount(mf.format(loanCommTeamFunds[i][qtr]));
											termFundTotal.setTotDisbAmount(mf.format(loanDisbTeamFunds[i][qtr]));
											termFundTotal.setTotExpAmount(mf.format(loanExpTeamFunds[i][qtr]));
											termFundTotal.setTotPlannedDisbAmount(mf.format(loanPlDisbTeamFunds[i][qtr]));
											termFund.getTermFundTotal().add(termFundTotal);
											totalComm=totalComm + loanCommTeamFunds[i][qtr];
											totalDisb=totalDisb + loanDisbTeamFunds[i][qtr];
											totalExp=totalExp + loanExpTeamFunds[i][qtr];
											totalPlDisb=totalPlDisb + loanPlDisbTeamFunds[i][qtr];
										}
									}
									termFund.setTotDonorCommAmount(mf.format(totalComm));
									termFund.setTotDonorDisbAmount(mf.format(totalDisb));
									termFund.setTotDonorExpAmount(mf.format(totalExp));
									termFund.setTotDonorPlannedDisbAmount(mf.format(totalPlDisb));
									termFund.setTotDonorUnDisbAmount(mf.format(loanUnDisbTeamFund));
									totalComm=totalPlDisb=totalDisb=totalExp=0.0;
								}
								if(assist.equals("Grant"))
								{
									termFund.setTermAssistName("Grant");
									termFund.setTermFundTotal(new ArrayList());
									for(int i=0;i<yrCount;i++)
									{
										for(int qtr=0;qtr<4;qtr++)
										{
											TermFundTotal termFundTotal=new TermFundTotal();
											termFundTotal.setTotCommAmount(mf.format(grantCommTeamFunds[i][qtr]));
											termFundTotal.setTotDisbAmount(mf.format(grantDisbTeamFunds[i][qtr]));
											termFundTotal.setTotExpAmount(mf.format(grantExpTeamFunds[i][qtr]));
											termFundTotal.setTotPlannedDisbAmount(mf.format(grantPlDisbTeamFunds[i][qtr]));
											termFund.getTermFundTotal().add(termFundTotal);
											totalComm=totalComm + grantCommTeamFunds[i][qtr];
											totalDisb=totalDisb + grantDisbTeamFunds[i][qtr];
											totalExp=totalExp + grantExpTeamFunds[i][qtr];
											totalPlDisb=totalPlDisb + grantPlDisbTeamFunds[i][qtr];
										}
									}
									termFund.setTotDonorCommAmount(mf.format(totalComm));
									termFund.setTotDonorDisbAmount(mf.format(totalDisb));
									termFund.setTotDonorExpAmount(mf.format(totalExp));
									termFund.setTotDonorPlannedDisbAmount(mf.format(totalPlDisb));
									termFund.setTotDonorUnDisbAmount(mf.format(grantUnDisbTeamFund));
									totalPlDisb=totalDisb=totalExp=0.0;
								}
								if(assist.equals("In Kind"))
								{
									termFund.setTermAssistName("In Kind");
									termFund.setTermFundTotal(new ArrayList());
									for(int i=0;i<yrCount;i++)
									{
										for(int qtr=0;qtr<4;qtr++)
										{
											TermFundTotal termFundTotal=new TermFundTotal();
											termFundTotal.setTotCommAmount(mf.format(kindCommTeamFunds[i][qtr]));
											termFundTotal.setTotDisbAmount(mf.format(kindDisbTeamFunds[i][qtr]));
											termFundTotal.setTotExpAmount(mf.format(kindExpTeamFunds[i][qtr]));
											termFundTotal.setTotPlannedDisbAmount(mf.format(kindPlDisbTeamFunds[i][qtr]));
											termFund.getTermFundTotal().add(termFundTotal);
											totalComm=totalComm + kindCommTeamFunds[i][qtr];
											totalDisb=totalDisb + kindDisbTeamFunds[i][qtr];
											totalExp=totalExp + kindExpTeamFunds[i][qtr];
											totalPlDisb=totalPlDisb + kindPlDisbTeamFunds[i][qtr];
										}
									}
									termFund.setTotDonorCommAmount(mf.format(totalComm));
									termFund.setTotDonorDisbAmount(mf.format(totalDisb));
									termFund.setTotDonorExpAmount(mf.format(totalExp));
									termFund.setTotDonorPlannedDisbAmount(mf.format(totalPlDisb));
									termFund.setTotDonorUnDisbAmount(mf.format(kindUnDisbTeamFund));
									totalComm=totalPlDisb=totalDisb=totalExp=0.0;
								}
								logger.debug("Added:'" + termFund.getTermAssistName());
								report.getTotalTeamTermAssistFund().add(termFund);	
							}
							actualCommitment=actualDisbursement=0.0;
							totalComm=totalPlDisb=totalDisb=totalExp=0.0;
							donorCommAmount=donorPlDisbAmount=donorDisbAmount=donorExpAmount=donorUnDisbAmount=0.0;
							teamCommAmount=teamPlDisbAmount=teamDisbAmount=teamExpAmount=teamUnDisbAmount=0.0;
							loanUnDisbTeamFund=grantUnDisbTeamFund=kindUnDisbTeamFund=0.0;
							for(int i=0;i<yrCount;i++)
							{
								for(int qtr=0;qtr<4;qtr++)
								{
									commTermFunds[i][qtr]=0;
									plDisbTermFunds[i][qtr]=0;
									disbTermFunds[i][qtr]=0;
									expTermFunds[i][qtr]=0;
									commFund[i][qtr]=0;
									plDisbFund[i][qtr]=0;
									disbFund[i][qtr]=0;
									expFund[i][qtr]=0;
									commTeamFund[i][qtr]=0;
									plDisbTeamFund[i][qtr]=0;
									disbTeamFund[i][qtr]=0;
									expTeamFund[i][qtr]=0;
									kindCommTeamFunds[i][qtr]=0;
									kindExpTeamFunds[i][qtr]=0;
									kindDisbTeamFunds[i][qtr]=0;
									kindPlDisbTeamFunds[i][qtr]=0;
									grantCommTeamFunds[i][qtr]=0;
									grantExpTeamFunds[i][qtr]=0;
									grantDisbTeamFunds[i][qtr]=0;
									grantPlDisbTeamFunds[i][qtr]=0;
									loanCommTeamFunds[i][qtr]=0;
									loanExpTeamFunds[i][qtr]=0;
									loanDisbTeamFunds[i][qtr]=0;
									loanPlDisbTeamFunds[i][qtr]=0;
								}
							}
							donorCount=0;		
							teamAssistance.clear();
							ampReports.add(report);
						}
						report = new multiReport();
						AmpTeam ampTeam=DbUtil.getAmpTeam(ampReportCache.getAmpTeamId());
						report.setAmpTeamId(ampReportCache.getAmpTeamId());
						report.setTeamName(ampTeam.getName());
						report.setCount(++teamCount);
						report.setTotalTeamFund(new ArrayList());
						report.setTotalTeamTermAssistFund(new ArrayList());
						report.setDonors(new ArrayList());
						ampTeamDonors=new AmpTeamDonors();
						ampTeamDonors.setDonorAgency(ampReportCache.getDonorName());
						ampTeamDonors.setDonorCount(++donorCount);
						ampTeamDonors.setTotalDonorFund(new ArrayList());
						ampTeamDonors.setTotalDonorTermAssistFund(new ArrayList());
						termAssistName=ampReportCache.getTermAssistName();
						termFlag=0;
					}

					if(report.getAmpTeamId().equals(ampReportCache.getAmpTeamId()) && !(ampReportCache.getDonorName().equals(ampTeamDonors.getDonorAgency())))
					{
						if(termFlag==1)
						{
							donorTermAssist=new TermFund();
							donorTermAssist.setTermAssistName(termAssistName);
							donorTermAssist.setTermFundTotal(new ArrayList());
							if(teamAssistance.indexOf(donorTermAssist.getTermAssistName())==-1)
								teamAssistance.add(donorTermAssist.getTermAssistName());
							for(int i=0;i<yrCount;i++)
							{
								for(int qtr=0;qtr<4;qtr++)
								{
									TermFundTotal termFundTotal=new TermFundTotal();
									termFundTotal.setTotCommAmount(mf.format(commTermFunds[i][qtr]));
									termFundTotal.setTotDisbAmount(mf.format(disbTermFunds[i][qtr]));
									termFundTotal.setTotExpAmount(mf.format(expTermFunds[i][qtr]));
									termFundTotal.setTotPlannedDisbAmount(mf.format(plDisbTermFunds[i][qtr]));

									donorTermAssist.getTermFundTotal().add(termFundTotal);
								
									totalComm=totalComm + commTermFunds[i][qtr];
									totalPlDisb=totalPlDisb + plDisbTermFunds[i][qtr];
									totalDisb=totalDisb + disbTermFunds[i][qtr];
									totalExp= totalExp + expTermFunds[i][qtr];

									commFund[i][qtr]=commFund[i][qtr] + commTermFunds[i][qtr];
									plDisbFund[i][qtr]=plDisbFund[i][qtr] + plDisbTermFunds[i][qtr];
									disbFund[i][qtr]=disbFund[i][qtr] + disbTermFunds[i][qtr];
									expFund[i][qtr]=expFund[i][qtr] + expTermFunds[i][qtr];
								}
							}
							donorTermAssist.setTotDonorCommAmount(mf.format(totalComm));
							donorTermAssist.setTotDonorDisbAmount(mf.format(totalDisb));
							donorTermAssist.setTotDonorExpAmount(mf.format(totalExp));
							donorTermAssist.setTotDonorPlannedDisbAmount(mf.format(totalPlDisb));
							donorTermAssist.setTotDonorUnDisbAmount(mf.format(actualCommitment-actualDisbursement));
							ampTeamDonors.getTotalDonorTermAssistFund().add(donorTermAssist);

							donorCommAmount=donorCommAmount + totalComm;
							donorPlDisbAmount=donorPlDisbAmount + totalPlDisb;
							donorDisbAmount=donorDisbAmount + totalDisb;
							donorExpAmount=donorExpAmount + totalExp;
							donorUnDisbAmount=donorUnDisbAmount + (actualCommitment - actualDisbursement);

							if(donorTermAssist.getTermAssistName().equals("Loan"))
							{
								for(int i=0;i<yrCount;i++)
								{
									for (int qtr=0;qtr<4 ;qtr++)
									{
										loanCommTeamFunds[i][qtr]=loanCommTeamFunds[i][qtr] + commTermFunds[i][qtr];
										loanDisbTeamFunds[i][qtr]=loanDisbTeamFunds[i][qtr] + disbTermFunds[i][qtr];
										loanExpTeamFunds[i][qtr]=loanExpTeamFunds[i][qtr] + expTermFunds[i][qtr];
										loanPlDisbTeamFunds[i][qtr]=loanPlDisbTeamFunds[i][qtr] + plDisbTermFunds[i][qtr];
									}
								}
								loanUnDisbTeamFund=loanUnDisbTeamFund + (actualCommitment - actualDisbursement);
							}
							if(donorTermAssist.getTermAssistName().equals("Grant"))
							{
								for(int i=0;i<yrCount;i++)
								{
									for (int qtr=0;qtr<4 ;qtr++)
									{
										grantCommTeamFunds[i][qtr]=grantCommTeamFunds[i][qtr] + commTermFunds[i][qtr];
										grantDisbTeamFunds[i][qtr]=grantDisbTeamFunds[i][qtr] + disbTermFunds[i][qtr];
										grantExpTeamFunds[i][qtr]=grantExpTeamFunds[i][qtr] + expTermFunds[i][qtr];
										grantPlDisbTeamFunds[i][qtr]=grantPlDisbTeamFunds[i][qtr] + plDisbTermFunds[i][qtr];
									}
								}
								grantUnDisbTeamFund=grantUnDisbTeamFund + (actualCommitment - actualDisbursement);
							}
							if(donorTermAssist.getTermAssistName().equals("In Kind"))
							{
								for(int i=0;i<yrCount;i++)
								{
									for (int qtr=0;qtr<4 ;qtr++)
									{
										kindCommTeamFunds[i][qtr]=kindCommTeamFunds[i][qtr] + commTermFunds[i][qtr];
										kindDisbTeamFunds[i][qtr]=kindDisbTeamFunds[i][qtr] + disbTermFunds[i][qtr];
										kindExpTeamFunds[i][qtr]=kindExpTeamFunds[i][qtr] + expTermFunds[i][qtr];
										kindPlDisbTeamFunds[i][qtr]=kindPlDisbTeamFunds[i][qtr] + plDisbTermFunds[i][qtr];
									}
								}
								kindUnDisbTeamFund=kindUnDisbTeamFund + (actualCommitment - actualDisbursement);
							}
						}
						for(int i=0;i<yrCount;i++)
						{
							for(int qtr=0;qtr<4;qtr++)
							{
								FundTotal total=new FundTotal();
								total.setTotCommAmount(mf.format(commFund[i][qtr]));
								total.setTotDisbAmount(mf.format(disbFund[i][qtr]));
								total.setTotExpAmount(mf.format(expFund[i][qtr]));
								total.setTotPlannedDisbAmount(mf.format(plDisbFund[i][qtr]));
								ampTeamDonors.getTotalDonorFund().add(total);	
								
								commTeamFund[i][qtr]=commTeamFund[i][qtr] + commFund[i][qtr];
								plDisbTeamFund[i][qtr]=plDisbTeamFund[i][qtr] + plDisbFund[i][qtr];
								disbTeamFund[i][qtr]=disbTeamFund[i][qtr] + disbFund[i][qtr];
								expTeamFund[i][qtr]=expTeamFund[i][qtr] + expFund[i][qtr];
							}
											
						}
						ampTeamDonors.setDonorCommAmount(mf.format(donorCommAmount));
						ampTeamDonors.setDonorDisbAmount(mf.format(donorDisbAmount));
						ampTeamDonors.setDonorExpAmount(mf.format(donorExpAmount));
						ampTeamDonors.setDonorPlannedDisbAmount(mf.format(donorPlDisbAmount));
						ampTeamDonors.setDonorUnDisbAmount(mf.format(donorUnDisbAmount));
						report.getDonors().add(ampTeamDonors);

						teamCommAmount=teamCommAmount + donorCommAmount;
						teamDisbAmount=teamDisbAmount + donorDisbAmount;
						teamExpAmount=teamExpAmount + donorExpAmount;
						teamPlDisbAmount=teamPlDisbAmount + donorPlDisbAmount;
						teamUnDisbAmount=teamUnDisbAmount + donorUnDisbAmount;
						
						totalComm=totalPlDisb=totalDisb=totalExp=0.0;
						actualCommitment=actualDisbursement=0.0;
						donorCommAmount=donorPlDisbAmount=donorDisbAmount=donorExpAmount=donorUnDisbAmount=0.0;
						for(int i=0;i<yrCount;i++)
						{
							for(int qtr=0;qtr<4;qtr++)
							{
								commTermFunds[i][qtr]=0;
								plDisbTermFunds[i][qtr]=0;
								disbTermFunds[i][qtr]=0;
								expTermFunds[i][qtr]=0;
								commFund[i][qtr]=0;
								plDisbFund[i][qtr]=0;
								disbFund[i][qtr]=0;
								expFund[i][qtr]=0;
							}
						}

						ampTeamDonors=new AmpTeamDonors();
						ampTeamDonors.setDonorAgency(ampReportCache.getDonorName());
						ampTeamDonors.setDonorCount(++donorCount);
						ampTeamDonors.setTotalDonorFund(new ArrayList());
						ampTeamDonors.setTotalDonorTermAssistFund(new ArrayList());
						termAssistName=ampReportCache.getTermAssistName();
						termFlag=0;
					}
					
					if(report.getAmpTeamId().equals(ampReportCache.getAmpTeamId()) && ampReportCache.getDonorName().equals(ampTeamDonors.getDonorAgency()) && !(ampReportCache.getTermAssistName().equals(termAssistName)))
					{
			//			logger.debug("different project");
						if(termFlag==1)
						{
							donorTermAssist=new TermFund();
							donorTermAssist.setTermAssistName(termAssistName);
							donorTermAssist.setTermFundTotal(new ArrayList());
							if(teamAssistance.indexOf(donorTermAssist.getTermAssistName())==-1)
								teamAssistance.add(donorTermAssist.getTermAssistName());
							for(int i=0;i<yrCount;i++)
							{
								for(int qtr=0;qtr<4;qtr++)
								{
									TermFundTotal termFundTotal=new TermFundTotal();
									termFundTotal.setTotCommAmount(mf.format(commTermFunds[i][qtr]));
									termFundTotal.setTotDisbAmount(mf.format(disbTermFunds[i][qtr]));
									termFundTotal.setTotExpAmount(mf.format(expTermFunds[i][qtr]));
									termFundTotal.setTotPlannedDisbAmount(mf.format(plDisbTermFunds[i][qtr]));
									donorTermAssist.getTermFundTotal().add(termFundTotal);
								
									totalComm=totalComm + commTermFunds[i][qtr];
									totalPlDisb=totalPlDisb + plDisbTermFunds[i][qtr];
									totalDisb=totalDisb + disbTermFunds[i][qtr];
									totalExp= totalExp + expTermFunds[i][qtr];

									commFund[i][qtr]=commFund[i][qtr] + commTermFunds[i][qtr];
									plDisbFund[i][qtr]=plDisbFund[i][qtr] + plDisbTermFunds[i][qtr];
									disbFund[i][qtr]=disbFund[i][qtr] + disbTermFunds[i][qtr];
									expFund[i][qtr]=expFund[i][qtr] + expTermFunds[i][qtr];
								}
							}
							donorTermAssist.setTotDonorCommAmount(mf.format(totalComm));
							donorTermAssist.setTotDonorDisbAmount(mf.format(totalDisb));
							donorTermAssist.setTotDonorExpAmount(mf.format(totalExp));
							donorTermAssist.setTotDonorPlannedDisbAmount(mf.format(totalPlDisb));
							donorTermAssist.setTotDonorUnDisbAmount(mf.format(actualCommitment - actualDisbursement));
							ampTeamDonors.getTotalDonorTermAssistFund().add(donorTermAssist);
							donorCommAmount=donorCommAmount + totalComm;
							donorPlDisbAmount=donorPlDisbAmount + totalPlDisb;
							donorDisbAmount=donorDisbAmount + totalDisb;
							donorExpAmount=donorExpAmount + totalExp;
							donorUnDisbAmount=donorUnDisbAmount + (actualCommitment - actualDisbursement);
							if(donorTermAssist.getTermAssistName().equals("Loan"))
							{
								for(int i=0;i<yrCount;i++)
								{	
									for (int qtr=0;qtr<4 ;qtr++)
									{
										loanCommTeamFunds[i][qtr]=loanCommTeamFunds[i][qtr] + commTermFunds[i][qtr];
										loanDisbTeamFunds[i][qtr]=loanDisbTeamFunds[i][qtr] + disbTermFunds[i][qtr];
										loanExpTeamFunds[i][qtr]=loanExpTeamFunds[i][qtr] + expTermFunds[i][qtr];
										loanPlDisbTeamFunds[i][qtr]=loanPlDisbTeamFunds[i][qtr] + plDisbTermFunds[i][qtr];
									}
								}
								loanUnDisbTeamFund=loanUnDisbTeamFund + (actualCommitment - actualDisbursement);
							}
							if(donorTermAssist.getTermAssistName().equals("Grant"))
							{
								for(int i=0;i<yrCount;i++)
								{
									for (int qtr=0;qtr<4 ;qtr++)
									{
										grantCommTeamFunds[i][qtr]=grantCommTeamFunds[i][qtr] + commTermFunds[i][qtr];
										grantDisbTeamFunds[i][qtr]=grantDisbTeamFunds[i][qtr] + disbTermFunds[i][qtr];
										grantExpTeamFunds[i][qtr]=grantExpTeamFunds[i][qtr] + expTermFunds[i][qtr];
										grantPlDisbTeamFunds[i][qtr]=grantPlDisbTeamFunds[i][qtr] + plDisbTermFunds[i][qtr];
									}
								}
								grantUnDisbTeamFund=grantUnDisbTeamFund + (actualCommitment - actualDisbursement);
							}
							if(donorTermAssist.getTermAssistName().equals("In Kind"))
							{
								for(int i=0;i<yrCount;i++)
								{
									for (int qtr=0;qtr<4 ;qtr++)
									{
										kindCommTeamFunds[i][qtr]=kindCommTeamFunds[i][qtr] + commTermFunds[i][qtr];
										kindDisbTeamFunds[i][qtr]=kindDisbTeamFunds[i][qtr] + disbTermFunds[i][qtr];
										kindExpTeamFunds[i][qtr]=kindExpTeamFunds[i][qtr] + expTermFunds[i][qtr];
										kindPlDisbTeamFunds[i][qtr]=kindPlDisbTeamFunds[i][qtr] + plDisbTermFunds[i][qtr];
									}
								}
								kindUnDisbTeamFund=kindUnDisbTeamFund + (actualCommitment - actualDisbursement);
							}
						}
						totalComm=totalPlDisb=totalDisb=totalExp=0.0;
						actualCommitment=actualDisbursement=0;
						for(int i=0;i<yrCount;i++)
						{
							for(int qtr=0;qtr<4;qtr++)
							{
								commTermFunds[i][qtr]=0;
								plDisbTermFunds[i][qtr]=0;
								disbTermFunds[i][qtr]=0;
								expTermFunds[i][qtr]=0;
							}
						}
						termAssistName=ampReportCache.getTermAssistName();
						termFlag=0;
					}
					logger.debug("Funding Information");
					if(ampReportCache.getFiscalYear()!=null && ampReportCache.getFiscalQuarter()!=null)
					{
						logger.debug("begin if");
						if(new Long(fiscalCalId).equals(Constants.GREGORIAN))
						{
							fiscalYear=ampReportCache.getFiscalYear().intValue();
							fiscalQuarter=ampReportCache.getFiscalQuarter().intValue();
						}
						if(ampReportCache.getCurrencyCode().equals("USD"))
							fromExchangeRate=1.0;
						else
							fromExchangeRate=DbUtil.getExchangeRate(ampReportCache.getCurrencyCode(),Constants.ACTUAL,ampReportCache.getTransactionDate());
						if(ampCurrencyCode.equals("USD"))
							toExchangeRate=1.0;
						else
							toExchangeRate=DbUtil.getExchangeRate(ampCurrencyCode,Constants.ACTUAL,ampReportCache.getTransactionDate());
						
						if(ampReportCache.getActualCommitment().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
						{
							termFlag=1;
							amount=CurrencyWorker.convert1(ampReportCache.getActualCommitment().doubleValue(),fromExchangeRate,toExchangeRate);
							actualCommitment=actualCommitment + amount;
							if(fiscalYear>=fromYr && fiscalYear<=toYr)
							{
								if(commTermFunds[fiscalYear%fromYr][fiscalQuarter-1]==0)
									commTermFunds[fiscalYear%fromYr][fiscalQuarter-1]=amount;
								else
								if(commTermFunds[fiscalYear%fromYr][fiscalQuarter-1]>0)
									commTermFunds[fiscalYear%fromYr][fiscalQuarter-1]=commTermFunds[fiscalYear%fromYr][fiscalQuarter-1] + amount;
							}
						}
						amount=0.0;
						
						if(ampReportCache.getActualDisbursement().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
						{
							termFlag=1;
							amount=CurrencyWorker.convert1(ampReportCache.getActualDisbursement().doubleValue(),fromExchangeRate,toExchangeRate);
							actualDisbursement=actualDisbursement + amount;
							if(fiscalYear>=fromYr && fiscalYear<=toYr)
							{
								if(disbTermFunds[fiscalYear%fromYr][fiscalQuarter-1]==0)
									disbTermFunds[fiscalYear%fromYr][fiscalQuarter-1]=amount;
								else
								if(disbTermFunds[fiscalYear%fromYr][fiscalQuarter-1]>0)
									disbTermFunds[fiscalYear%fromYr][fiscalQuarter-1]=disbTermFunds[fiscalYear%fromYr][fiscalQuarter-1] + amount;
							}
						}
						amount=0.0;
						if(ampReportCache.getActualExpenditure().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
						{
							amount=CurrencyWorker.convert1(ampReportCache.getActualExpenditure().doubleValue(),fromExchangeRate,toExchangeRate);
							if(fiscalYear>=fromYr && fiscalYear<=toYr)
							{
								termFlag=1;
								if(expTermFunds[fiscalYear%fromYr][fiscalQuarter-1]==0)
									expTermFunds[fiscalYear%fromYr][fiscalQuarter-1]=amount;
								else
								if(expTermFunds[fiscalYear%fromYr][fiscalQuarter-1]>0)
									expTermFunds[fiscalYear%fromYr][fiscalQuarter-1]= expTermFunds[fiscalYear%fromYr][fiscalQuarter-1] + amount;
							}
						}
						amount=0.0;
						if(ampReportCache.getCurrencyCode().equals("USD"))
							fromExchangeRate=1.0;
						else
							fromExchangeRate=DbUtil.getExchangeRate(ampReportCache.getCurrencyCode(),Constants.PLANNED,ampReportCache.getTransactionDate());
						if(ampCurrencyCode.equals("USD"))
							toExchangeRate=1.0;
						else
							toExchangeRate=DbUtil.getExchangeRate(ampCurrencyCode,Constants.PLANNED,ampReportCache.getTransactionDate());
						if(ampReportCache.getPlannedDisbursement().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
						{
							amount=CurrencyWorker.convert1(ampReportCache.getPlannedDisbursement().doubleValue(),fromExchangeRate,toExchangeRate);
							if(fiscalYear>=fromYr && fiscalYear<=toYr)
							{
								termFlag=1;
								if(plDisbTermFunds[fiscalYear%fromYr][fiscalQuarter-1]==0)
									plDisbTermFunds[fiscalYear%fromYr][fiscalQuarter-1]=amount;
								else
								if(plDisbTermFunds[fiscalYear%fromYr][fiscalQuarter-1]>0)
									plDisbTermFunds[fiscalYear%fromYr][fiscalQuarter-1]=plDisbTermFunds[fiscalYear%fromYr][fiscalQuarter-1] + amount;
							}
						}
						amount=0.0;
										
					}

				}
				logger.debug("Inside different donors");
				if(termFlag==1)
				{
					donorTermAssist=new TermFund();
					donorTermAssist.setTermAssistName(termAssistName);
					donorTermAssist.setTermFundTotal(new ArrayList());
					if(teamAssistance.indexOf(donorTermAssist.getTermAssistName())==-1)
						teamAssistance.add(donorTermAssist.getTermAssistName());
					for(int i=0;i<yrCount;i++)
					{
						for(int qtr=0;qtr<4;qtr++)
						{
							TermFundTotal termFundTotal=new TermFundTotal();
							termFundTotal.setTotCommAmount(mf.format(commTermFunds[i][qtr]));
							termFundTotal.setTotDisbAmount(mf.format(disbTermFunds[i][qtr]));
							termFundTotal.setTotExpAmount(mf.format(expTermFunds[i][qtr]));
							termFundTotal.setTotPlannedDisbAmount(mf.format(plDisbTermFunds[i][qtr]));
							donorTermAssist.getTermFundTotal().add(termFundTotal);
									
							totalComm=totalComm + commTermFunds[i][qtr];
							totalPlDisb=totalPlDisb + plDisbTermFunds[i][qtr];
							totalDisb=totalDisb + disbTermFunds[i][qtr];
							totalExp= totalExp + expTermFunds[i][qtr];
							commFund[i][qtr]=commFund[i][qtr] + commTermFunds[i][qtr];
							plDisbFund[i][qtr]=plDisbFund[i][qtr] + plDisbTermFunds[i][qtr];
							disbFund[i][qtr]=disbFund[i][qtr] + disbTermFunds[i][qtr];
							expFund[i][qtr]=expFund[i][qtr] + expTermFunds[i][qtr];
						}
					}
					donorTermAssist.setTotDonorCommAmount(mf.format(totalComm));
					donorTermAssist.setTotDonorDisbAmount(mf.format(totalDisb));
					donorTermAssist.setTotDonorExpAmount(mf.format(totalExp));
					donorTermAssist.setTotDonorPlannedDisbAmount(mf.format(totalPlDisb));
					donorTermAssist.setTotDonorUnDisbAmount(mf.format(actualCommitment-actualDisbursement));
					ampTeamDonors.getTotalDonorTermAssistFund().add(donorTermAssist);

					donorCommAmount=donorCommAmount + totalComm;
					donorPlDisbAmount=donorPlDisbAmount + totalPlDisb;
					donorDisbAmount=donorDisbAmount + totalDisb;
					donorExpAmount=donorExpAmount + totalExp;
					donorUnDisbAmount=donorUnDisbAmount + (actualCommitment - actualDisbursement);
					
					if(donorTermAssist.getTermAssistName().equals("Loan"))
					{
						for(int i=0;i<yrCount;i++)
						{
							for (int qtr=0;qtr<4 ;qtr++)
							{
								loanCommTeamFunds[i][qtr]=loanCommTeamFunds[i][qtr] + commTermFunds[i][qtr];
								loanDisbTeamFunds[i][qtr]=loanDisbTeamFunds[i][qtr] + disbTermFunds[i][qtr];
								loanExpTeamFunds[i][qtr]=loanExpTeamFunds[i][qtr] + expTermFunds[i][qtr];
								loanPlDisbTeamFunds[i][qtr]=loanPlDisbTeamFunds[i][qtr] + plDisbTermFunds[i][qtr];
							}
						}
						loanUnDisbTeamFund=loanUnDisbTeamFund + (actualCommitment - actualDisbursement);
					}
					if(donorTermAssist.getTermAssistName().equals("Grant"))
					{
						for(int i=0;i<yrCount;i++)
						{
							for (int qtr=0;qtr<4 ;qtr++)
							{
								grantCommTeamFunds[i][qtr]=grantCommTeamFunds[i][qtr] + commTermFunds[i][qtr];
								grantDisbTeamFunds[i][qtr]=grantDisbTeamFunds[i][qtr] + disbTermFunds[i][qtr];
								grantExpTeamFunds[i][qtr]=grantExpTeamFunds[i][qtr] + expTermFunds[i][qtr];
								grantPlDisbTeamFunds[i][qtr]=grantPlDisbTeamFunds[i][qtr] + plDisbTermFunds[i][qtr];
							}
						}
						grantUnDisbTeamFund=grantUnDisbTeamFund + (actualCommitment - actualDisbursement);
					}
					if(donorTermAssist.getTermAssistName().equals("In Kind"))
					{
						for(int i=0;i<yrCount;i++)
						{
							for (int qtr=0;qtr<4 ;qtr++)
							{
								kindCommTeamFunds[i][qtr]=kindCommTeamFunds[i][qtr] + commTermFunds[i][qtr];
								kindDisbTeamFunds[i][qtr]=kindDisbTeamFunds[i][qtr] + disbTermFunds[i][qtr];
								kindExpTeamFunds[i][qtr]=kindExpTeamFunds[i][qtr] + expTermFunds[i][qtr];
								kindPlDisbTeamFunds[i][qtr]=kindPlDisbTeamFunds[i][qtr] + plDisbTermFunds[i][qtr];
							}
						}
						kindUnDisbTeamFund=kindUnDisbTeamFund + (actualCommitment - actualDisbursement);
					}
				}
				for(int i=0;i<yrCount;i++)
				{
					for(int qtr=0;qtr<4;qtr++)
					{
						FundTotal total=new FundTotal();
						total.setTotCommAmount(mf.format(commFund[i][qtr]));
						total.setTotDisbAmount(mf.format(disbFund[i][qtr]));
						total.setTotExpAmount(mf.format(expFund[i][qtr]));
						total.setTotPlannedDisbAmount(mf.format(plDisbFund[i][qtr]));
						ampTeamDonors.getTotalDonorFund().add(total);	
						
						commTeamFund[i][qtr]=commTeamFund[i][qtr] + commFund[i][qtr];
						plDisbTeamFund[i][qtr]=plDisbTeamFund[i][qtr] + plDisbFund[i][qtr];
						disbTeamFund[i][qtr]=disbTeamFund[i][qtr] + disbFund[i][qtr];
						expTeamFund[i][qtr]=expTeamFund[i][qtr] + expFund[i][qtr];
					}
								
				}
				ampTeamDonors.setDonorCommAmount(mf.format(donorCommAmount));
				ampTeamDonors.setDonorDisbAmount(mf.format(donorDisbAmount));
				ampTeamDonors.setDonorExpAmount(mf.format(donorExpAmount));
				ampTeamDonors.setDonorPlannedDisbAmount(mf.format(donorPlDisbAmount));
				ampTeamDonors.setDonorUnDisbAmount(mf.format(donorUnDisbAmount));
				report.getDonors().add(ampTeamDonors);

				teamCommAmount=teamCommAmount + donorCommAmount;
				teamDisbAmount=teamDisbAmount + donorDisbAmount;
				teamExpAmount=teamExpAmount + donorExpAmount;
				teamPlDisbAmount=teamPlDisbAmount + donorPlDisbAmount;
				teamUnDisbAmount=teamUnDisbAmount + donorUnDisbAmount;
							
				for(int i=0;i<yrCount;i++)
				{
					for(int qtr=0;qtr<4 ;qtr++)
					{
						FundTotal total=new FundTotal();
						total.setTotCommAmount(mf.format(commTeamFund[i][qtr]));
						total.setTotDisbAmount(mf.format(disbTeamFund[i][qtr]));
						total.setTotExpAmount(mf.format(expTeamFund[i][qtr]));
						total.setTotPlannedDisbAmount(mf.format(plDisbTeamFund[i][qtr]));
						report.getTotalTeamFund().add(total);	
					}
				}
				report.setTeamCommAmount(mf.format(teamCommAmount));
				report.setTeamDisbAmount(mf.format(teamDisbAmount));
				report.setTeamExpAmount(mf.format(teamExpAmount));
				report.setTeamPlannedDisbAmount(mf.format(teamPlDisbAmount));
				report.setTeamUnDisbAmount(mf.format(teamUnDisbAmount));
				totalComm=totalPlDisb=totalDisb=totalExp=0.0;
				Iterator teamIter=teamAssistance.iterator();
				while(teamIter.hasNext())
				{
					String assist=(String) teamIter.next();
					logger.debug("Assist:" + assist + ":");
					TermFund termFund=new TermFund();
					if(assist.equals("Loan"))
					{
						termFund.setTermAssistName("Loan");
						termFund.setTermFundTotal(new ArrayList());
						for(int i=0;i<yrCount;i++)
						{
							for(int qtr=0;qtr<4;qtr++)
							{
								TermFundTotal termFundTotal=new TermFundTotal();
								termFundTotal.setTotCommAmount(mf.format(loanCommTeamFunds[i][qtr]));
								termFundTotal.setTotDisbAmount(mf.format(loanDisbTeamFunds[i][qtr]));
								termFundTotal.setTotExpAmount(mf.format(loanExpTeamFunds[i][qtr]));
								termFundTotal.setTotPlannedDisbAmount(mf.format(loanPlDisbTeamFunds[i][qtr]));
								termFund.getTermFundTotal().add(termFundTotal);
								totalComm=totalComm + loanCommTeamFunds[i][qtr];
								totalDisb=totalDisb + loanDisbTeamFunds[i][qtr];
								totalExp=totalExp + loanExpTeamFunds[i][qtr];
								totalPlDisb=totalPlDisb + loanPlDisbTeamFunds[i][qtr];
							}
						}
						termFund.setTotDonorCommAmount(mf.format(totalComm));
						termFund.setTotDonorDisbAmount(mf.format(totalDisb));
						termFund.setTotDonorExpAmount(mf.format(totalExp));
						termFund.setTotDonorPlannedDisbAmount(mf.format(totalPlDisb));
						termFund.setTotDonorUnDisbAmount(mf.format(loanUnDisbTeamFund));
						totalComm=totalPlDisb=totalDisb=totalExp=0.0;
					}
					if(assist.equals("Grant"))
					{
						termFund.setTermAssistName("Grant");
						termFund.setTermFundTotal(new ArrayList());
						for(int i=0;i<yrCount;i++)
						{
							for(int qtr=0;qtr<4;qtr++)
							{
								TermFundTotal termFundTotal=new TermFundTotal();
								termFundTotal.setTotCommAmount(mf.format(grantCommTeamFunds[i][qtr]));
								termFundTotal.setTotDisbAmount(mf.format(grantDisbTeamFunds[i][qtr]));
								termFundTotal.setTotExpAmount(mf.format(grantExpTeamFunds[i][qtr]));
								termFundTotal.setTotPlannedDisbAmount(mf.format(grantPlDisbTeamFunds[i][qtr]));
								termFund.getTermFundTotal().add(termFundTotal);
								totalComm=totalComm + grantCommTeamFunds[i][qtr];
								totalDisb=totalDisb + grantDisbTeamFunds[i][qtr];
								totalExp=totalExp + grantExpTeamFunds[i][qtr];
								totalPlDisb=totalPlDisb + grantPlDisbTeamFunds[i][qtr];
							}
						}
						termFund.setTotDonorCommAmount(mf.format(totalComm));
						termFund.setTotDonorDisbAmount(mf.format(totalDisb));
						termFund.setTotDonorExpAmount(mf.format(totalExp));
						termFund.setTotDonorPlannedDisbAmount(mf.format(totalPlDisb));
						termFund.setTotDonorUnDisbAmount(mf.format(grantUnDisbTeamFund));
						totalComm=totalPlDisb=totalDisb=totalExp=0.0;
					}
					if(assist.equals("In Kind"))
					{
						termFund.setTermAssistName("In Kind");
						termFund.setTermFundTotal(new ArrayList());
						for(int i=0;i<yrCount;i++)
						{
							for(int qtr=0;qtr<4;qtr++)
							{
								TermFundTotal termFundTotal=new TermFundTotal();
								termFundTotal.setTotCommAmount(mf.format(kindCommTeamFunds[i][qtr]));
								termFundTotal.setTotDisbAmount(mf.format(kindDisbTeamFunds[i][qtr]));
								termFundTotal.setTotExpAmount(mf.format(kindExpTeamFunds[i][qtr]));
								termFundTotal.setTotPlannedDisbAmount(mf.format(kindPlDisbTeamFunds[i][qtr]));
								termFund.getTermFundTotal().add(termFundTotal);
								totalComm=totalComm + kindCommTeamFunds[i][qtr];
								totalDisb=totalDisb + kindDisbTeamFunds[i][qtr];
								totalExp=totalExp + kindExpTeamFunds[i][qtr];
								totalPlDisb=totalPlDisb + kindPlDisbTeamFunds[i][qtr];
							}
						}
						termFund.setTotDonorCommAmount(mf.format(totalComm));
						termFund.setTotDonorDisbAmount(mf.format(totalDisb));
						termFund.setTotDonorExpAmount(mf.format(totalExp));
						termFund.setTotDonorPlannedDisbAmount(mf.format(totalPlDisb));
						termFund.setTotDonorUnDisbAmount(mf.format(kindUnDisbTeamFund));
						totalComm=totalPlDisb=totalDisb=totalExp=0.0;
					}
					logger.debug("Added:'" + termFund.getTermAssistName());
					report.getTotalTeamTermAssistFund().add(termFund);	
				}
				ampReports.add(report);
			}	
		}
		catch(Exception ex) 		
		{
			logger.debug("Unable to get report names  from database " + ex.getMessage());
		}
		finally 
		{
			try 
			{
				PersistenceManager.releaseSession(session);
			}
			catch (Exception ex2) 
			{
				logger.debug("releaseSession() failed ");
			}
		}
		return ampReports ;
	}

	public static ArrayList getAmpQuarterlyReportBySector(Long ampTeamId,int fromYr,int toYr,String perspective,String ampCurrencyCode,Long ampModalityId,Long ampStatusId,Long ampDonorId,Long ampSectorId,int fiscalCalId,String startDate,String closeDate,String region)
	{
		Session session = null ;
		Query q = null ;
		Query qry=null;
		ArrayList ampReports = new ArrayList() ;
		String queryString = null;
		Long All=new Long(0);
		Iterator iter=null;
		Long ampStrId=null;
		int yrCount = (toYr - fromYr)+1;
		int fiscalYear=0,fiscalQuarter=0;
		double[][] plDisbFund=new double[yrCount][4];
		double[][] disbFund=new double[yrCount][4];
		double[][] expFund=new double[yrCount][4];

		double[][] plDisbSectorFund=new double[yrCount][4];
		double[][] disbSectorFund=new double[yrCount][4];
		double[][] expSectorFund=new double[yrCount][4];
		double[][] plDisbTotalFund=new double[yrCount][4];
		double[][] disbTotalFund=new double[yrCount][4];
		double[][] expTotalFund=new double[yrCount][4];
		
		double[][] plDisbTermFunds=new double[yrCount][4];
		double[][] disbTermFunds=new double[yrCount][4];
		double[][] expTermFunds=new double[yrCount][4];

		double[][] loanPlDisbSectorFunds=new double[yrCount][4];
		double[][] loanDisbSectorFunds=new double[yrCount][4];
		double[][] loanExpSectorFunds=new double[yrCount][4];
		double[][] grantPlDisbSectorFunds=new double[yrCount][4];
		double[][] grantDisbSectorFunds=new double[yrCount][4];
		double[][] grantExpSectorFunds=new double[yrCount][4];
		double[][] kindPlDisbSectorFunds=new double[yrCount][4];
		double[][] kindDisbSectorFunds=new double[yrCount][4];
		double[][] kindExpSectorFunds=new double[yrCount][4];

		double[][] loanPlDisbTotalFunds=new double[yrCount][4];
		double[][] loanDisbTotalFunds=new double[yrCount][4];
		double[][] loanExpTotalFunds=new double[yrCount][4];
		double[][] grantPlDisbTotalFunds=new double[yrCount][4];
		double[][] grantDisbTotalFunds=new double[yrCount][4];
		double[][] grantExpTotalFunds=new double[yrCount][4];
		double[][] kindPlDisbTotalFunds=new double[yrCount][4];
		double[][] kindDisbTotalFunds=new double[yrCount][4];
		double[][] kindExpTotalFunds=new double[yrCount][4];


		double totalComm = 0.0 ;
		double totalPlDisb = 0.0 ;
		double totalDisb = 0.0 ;
		double totalExp = 0.0;

		double termComm=0.0;

		double projCommAmount=0.0;
		double projPlannedDisbAmount=0.0;
		double projDisbAmount=0.0;
		double projExpAmount=0.0;

		double sectorCommAmount=0.0;
		double sectorPlannedDisbAmount=0.0;
		double sectorDisbAmount=0.0;
		double sectorExpAmount=0.0;

		double totalCommAmount=0.0;
		double totalPlannedDisbAmount=0.0;
		double totalDisbAmount=0.0;
		double totalExpAmount=0.0;

		double loanCommSectorAmount=0.0;
		double grantCommSectorAmount=0.0;
		double kindCommSectorAmount=0.0;
		
		double loanCommTotalAmount=0.0;
		double grantCommTotalAmount=0.0;
		double kindCommTotalAmount=0.0;

		int newSector=0;
		int count=1;
		double toExchangeRate=1.0;
		double fromExchangeRate=0.0;
		double amount=0.0;
		
		int donorCount=0;
		int projCount=0;

		int termFlag=0;
		String termAssistName=null;
		Iterator iterActivity=null;
		String inClause=null;
		String inClauseTeam=null;
		ArrayList sectorAssistance=new ArrayList();
		ArrayList totalAssistance=new ArrayList();
		DecimalFormat mf = new DecimalFormat("###,###,###,###,###") ;
		ArrayList approvedActivityList=new ArrayList();
						
		try
		{
			ArrayList dbReturnSet=(ArrayList)DbUtil.getAmpLevel0Teams(ampTeamId);				
			if(dbReturnSet.size()==0)
				inClauseTeam= "'" + ampTeamId + "'";
			else
			{
				iter=dbReturnSet.iterator();
				while(iter.hasNext())
				{
					Long teamId= (Long) iter.next();
					if(inClauseTeam==null)
						inClauseTeam="'" + teamId + "'";
					else
						inClauseTeam=inClauseTeam + ",'" + teamId + "'";
				}
			}
			logger.debug("Inclause: " + inClauseTeam);
			approvedActivityList=DbUtil.getApprovedActivities(inClauseTeam);
			session = PersistenceManager.getSession();
			multiReport report =null;
			AmpTeamDonors ampTeamDonors =null;
			Project project=null;
			ProjectTermAssist termAssist=null;
			TermFund sectorTermAssist=null;
//			logger.debug("Before dbUtil");
			//logger.debug("Sector Size: " + DbUtil.getAmpReportSectors(ampTeamId,fromYr,toYr,perspective).size());
			iter = DbUtil.getAmpReportSectors(inClauseTeam).iterator();
			while(iter.hasNext())
			{
				AmpProjectBySector ampProjectBySector = (AmpProjectBySector) iter.next(); 
				if(!ampSectorId.equals(All))
				{
					AmpSector ampSector=DbUtil.getAmpParentSector(ampSectorId);
					if(!(ampProjectBySector.getSector().getAmpSectorId().equals(ampSector.getAmpSectorId())))
						continue;
				}
				if(report!=null)
				{ 
					logger.debug(report.getSector());
					logger.debug("if report not null");
					if(!(ampTeamDonors.getDonorAgency().equals("Unspecified")))
					{
						if(termFlag==1)
						{
							termAssist=new ProjectTermAssist();
							termAssist.setTermAssistName(termAssistName);
							if(sectorAssistance.indexOf(termAssist.getTermAssistName())==-1)
								sectorAssistance.add(termAssist.getTermAssistName());
							if(totalAssistance.indexOf(termAssist.getTermAssistName())==-1)
								totalAssistance.add(termAssist.getTermAssistName());
				
							termAssist.setTermAssistFund(new ArrayList());
							for(int i=0;i<yrCount;i++)
							{
								for(int qtr=0;qtr<4;qtr++)
								{
									AmpFund termFund = new AmpFund();
									termFund.setPlannedDisbAmount(mf.format(plDisbTermFunds[i][qtr])); 
									termFund.setDisbAmount(mf.format(disbTermFunds[i][qtr]));
									termFund.setExpAmount(mf.format(expTermFunds[i][qtr]));
									termAssist.getTermAssistFund().add(termFund);
									totalPlDisb=totalPlDisb + plDisbTermFunds[i][qtr];
									totalDisb=totalDisb + disbTermFunds[i][qtr];
									totalExp= totalExp + expTermFunds[i][qtr];
									plDisbFund[i][qtr]=plDisbFund[i][qtr] + plDisbTermFunds[i][qtr];
									disbFund[i][qtr]=disbFund[i][qtr] + disbTermFunds[i][qtr];
									expFund[i][qtr]=expFund[i][qtr] + expTermFunds[i][qtr];
								}
							}
							termAssist.setTermPlannedDisbAmount(mf.format(totalPlDisb));
							termAssist.setTermDisbAmount(mf.format(totalDisb));
							termAssist.setTermExpAmount(mf.format(totalExp));
							termAssist.setTermUnDisbAmount(mf.format(termComm-totalDisb));
							projCommAmount=projCommAmount + termComm;
							projPlannedDisbAmount=projPlannedDisbAmount + totalPlDisb;
							projDisbAmount=projDisbAmount + totalDisb;
							projExpAmount=projExpAmount + totalExp;
							project.getTermAssist().add(termAssist);
	//						logger.debug("Term Assist:" + termAssist.getTermAssistName());
							
							if(termAssist.getTermAssistName().equals("Loan"))
							{
								for(int i=0;i<yrCount;i++)
								{
									for (int qtr=0;qtr<4 ;qtr++)
									{
										loanDisbSectorFunds[i][qtr]=loanDisbSectorFunds[i][qtr] + disbTermFunds[i][qtr];
										loanExpSectorFunds[i][qtr]=loanExpSectorFunds[i][qtr] + expTermFunds[i][qtr];
										loanPlDisbSectorFunds[i][qtr]=loanPlDisbSectorFunds[i][qtr] + plDisbTermFunds[i][qtr];
										loanDisbTotalFunds[i][qtr]=loanDisbTotalFunds[i][qtr] + disbTermFunds[i][qtr];
										loanExpTotalFunds[i][qtr]=loanExpTotalFunds[i][qtr] + expTermFunds[i][qtr];
										loanPlDisbTotalFunds[i][qtr]=loanPlDisbTotalFunds[i][qtr] + plDisbTermFunds[i][qtr];
									}
								}
								loanCommSectorAmount=loanCommSectorAmount + termComm;
								loanCommTotalAmount=loanCommTotalAmount + termComm;
							}
							if(termAssist.getTermAssistName().equals("Grant"))
							{
								for(int i=0;i<yrCount;i++)
								{
									for (int qtr=0;qtr<4 ;qtr++)
									{
										grantDisbSectorFunds[i][qtr]=grantDisbSectorFunds[i][qtr] + disbTermFunds[i][qtr];
										grantExpSectorFunds[i][qtr]=grantExpSectorFunds[i][qtr] + expTermFunds[i][qtr];
										grantPlDisbSectorFunds[i][qtr]=grantPlDisbSectorFunds[i][qtr] + plDisbTermFunds[i][qtr];
										grantDisbTotalFunds[i][qtr]=grantDisbTotalFunds[i][qtr] + disbTermFunds[i][qtr];
										grantExpTotalFunds[i][qtr]=grantExpTotalFunds[i][qtr] + expTermFunds[i][qtr];
										grantPlDisbTotalFunds[i][qtr]=grantPlDisbTotalFunds[i][qtr] + plDisbTermFunds[i][qtr];
										
									}
								}
								grantCommSectorAmount=grantCommSectorAmount + termComm;
								grantCommTotalAmount=grantCommTotalAmount + termComm;
							}
							if(termAssist.getTermAssistName().equals("Kind"))
							{
								for(int i=0;i<yrCount;i++)
								{
									for (int qtr=0;qtr<4 ;qtr++)
									{
										kindDisbSectorFunds[i][qtr]=kindDisbSectorFunds[i][qtr] + disbTermFunds[i][qtr];
										kindExpSectorFunds[i][qtr]=kindExpSectorFunds[i][qtr] + expTermFunds[i][qtr];
										kindPlDisbSectorFunds[i][qtr]=kindPlDisbSectorFunds[i][qtr] + plDisbTermFunds[i][qtr];
										kindDisbTotalFunds[i][qtr]=kindDisbTotalFunds[i][qtr] + disbTermFunds[i][qtr];
										kindExpTotalFunds[i][qtr]=kindExpTotalFunds[i][qtr] + expTermFunds[i][qtr];
										kindPlDisbTotalFunds[i][qtr]=kindPlDisbTotalFunds[i][qtr] + plDisbTermFunds[i][qtr];
									}
								}
								kindCommSectorAmount=kindCommSectorAmount + termComm;
								kindCommTotalAmount=kindCommTotalAmount + termComm;
							}

							
	//						logger.debug("After term assist");
						
						}
						logger.debug("Start adding project");
						for(int i=0;i<yrCount;i++)
						{
							for(int qtr=0;qtr<4;qtr++)
							{
								AmpFund projFund = new AmpFund();
								projFund.setPlannedDisbAmount(mf.format(plDisbFund[i][qtr])); 
								projFund.setDisbAmount(mf.format(disbFund[i][qtr]));
								projFund.setExpAmount(mf.format(expFund[i][qtr]));
								project.getAmpFund().add(projFund) ;
							}
						}
						project.setProjPlannedDisbAmount(mf.format(projPlannedDisbAmount));
						project.setProjDisbAmount(mf.format(projDisbAmount));
						project.setProjExpAmount(mf.format(projExpAmount));
						project.setProjUnDisbAmount(mf.format(projCommAmount-projDisbAmount));
						ampTeamDonors.getProject().add(project);
						logger.debug("Project Added");
						for(int i=0;i<yrCount;i++)
						{
							for(int qtr=0;qtr<4;qtr++)
							{
								plDisbSectorFund[i][qtr]=plDisbSectorFund[i][qtr] + plDisbFund[i][qtr];
								disbSectorFund[i][qtr]=disbSectorFund[i][qtr] + disbFund[i][qtr];
								expSectorFund[i][qtr]=expSectorFund[i][qtr] + expFund[i][qtr];
							}
						}
						sectorCommAmount=sectorCommAmount + projCommAmount;
						sectorPlannedDisbAmount=sectorPlannedDisbAmount + projPlannedDisbAmount;
						sectorDisbAmount=sectorDisbAmount + projDisbAmount;
						sectorExpAmount=sectorExpAmount + projExpAmount;
						for(int i=0;i<yrCount;i++)
						{
							for(int qtr=0;qtr<4;qtr++)
							{
								plDisbTotalFund[i][qtr]=plDisbTotalFund[i][qtr] + plDisbSectorFund[i][qtr];
								disbTotalFund[i][qtr]=disbTotalFund[i][qtr] + disbSectorFund[i][qtr];
								expTotalFund[i][qtr]=expTotalFund[i][qtr] + expSectorFund[i][qtr];
							}
						}
						totalCommAmount=totalCommAmount + sectorCommAmount;
						totalPlannedDisbAmount=totalPlannedDisbAmount + sectorPlannedDisbAmount;
						totalDisbAmount=totalDisbAmount + sectorDisbAmount;
						totalExpAmount=totalExpAmount + sectorExpAmount;
					}
					logger.debug("Adding Donor");
					report.getDonors().add(ampTeamDonors);
					logger.debug("Donor Added");
					for(int i=0;i<yrCount;i++)
					{
						for(int qtr=0;qtr<4;qtr++)
						{
							FundTotal total=new FundTotal();
							total.setTotPlannedDisbAmount(mf.format(plDisbSectorFund[i][qtr]));
							total.setTotDisbAmount(mf.format(disbSectorFund[i][qtr]));
							total.setTotExpAmount(mf.format(expSectorFund[i][qtr]));
							report.getTotalSectorFund().add(total);	
						}
					}
					report.setSectorPlannedDisbAmount(mf.format(sectorPlannedDisbAmount));
					report.setSectorDisbAmount(mf.format(sectorDisbAmount));
					report.setSectorExpAmount(mf.format(sectorExpAmount));
					report.setSectorUnDisbAmount(mf.format(sectorCommAmount-sectorDisbAmount));
					
					totalPlDisb=totalDisb=totalExp=0.0;
					logger.debug("Start Iter");
					Iterator sectorIter=sectorAssistance.iterator();
					while(sectorIter.hasNext())
					{
						String assist=(String) sectorIter.next();
						logger.debug("Assist:" + assist + ":");
						TermFund termFund=new TermFund();
						if(assist.equals("Loan"))
						{
							termFund.setTermAssistName("Loan");
							termFund.setTermFundTotal(new ArrayList());
							for(int i=0;i<yrCount;i++)
							{
								for(int qtr=0;qtr<4;qtr++)
								{
									TermFundTotal termFundTotal=new TermFundTotal();
									termFundTotal.setTotDisbAmount(mf.format(loanDisbSectorFunds[i][qtr]));
									termFundTotal.setTotExpAmount(mf.format(loanExpSectorFunds[i][qtr]));
									termFundTotal.setTotPlannedDisbAmount(mf.format(loanPlDisbSectorFunds[i][qtr]));
									termFund.getTermFundTotal().add(termFundTotal);
									totalDisb=totalDisb + loanDisbSectorFunds[i][qtr];
									totalExp=totalExp + loanExpSectorFunds[i][qtr];
									totalPlDisb=totalPlDisb + loanPlDisbSectorFunds[i][qtr];
								}
							}
							termFund.setTotDonorDisbAmount(mf.format(totalDisb));
							termFund.setTotDonorExpAmount(mf.format(totalExp));
							termFund.setTotDonorPlannedDisbAmount(mf.format(totalPlDisb));
							termFund.setTotDonorUnDisbAmount(mf.format(loanCommSectorAmount-totalDisb));
							totalPlDisb=totalDisb=totalExp=0.0;
						}
						if(assist.equals("Grant"))
						{
							termFund.setTermAssistName("Grant");
							termFund.setTermFundTotal(new ArrayList());
							for(int i=0;i<yrCount;i++)
							{
								for(int qtr=0;qtr<4;qtr++)
								{
									TermFundTotal termFundTotal=new TermFundTotal();
									termFundTotal.setTotDisbAmount(mf.format(grantDisbSectorFunds[i][qtr]));
									termFundTotal.setTotExpAmount(mf.format(grantExpSectorFunds[i][qtr]));
									termFundTotal.setTotPlannedDisbAmount(mf.format(grantPlDisbSectorFunds[i][qtr]));
									termFund.getTermFundTotal().add(termFundTotal);
									totalDisb=totalDisb + grantDisbSectorFunds[i][qtr];
									totalExp=totalExp + grantExpSectorFunds[i][qtr];
									totalPlDisb=totalPlDisb + grantPlDisbSectorFunds[i][qtr];
								}
							}
							termFund.setTotDonorDisbAmount(mf.format(totalDisb));
							termFund.setTotDonorExpAmount(mf.format(totalExp));
							termFund.setTotDonorPlannedDisbAmount(mf.format(totalPlDisb));
							termFund.setTotDonorUnDisbAmount(mf.format(grantCommSectorAmount-totalDisb));
							totalPlDisb=totalDisb=totalExp=0.0;
						}
						if(assist.equals("In Kind"))
						{
							termFund.setTermAssistName("In Kind");
							termFund.setTermFundTotal(new ArrayList());
							for(int i=0;i<yrCount;i++)
							{
								for(int qtr=0;qtr<4;qtr++)
								{
									TermFundTotal termFundTotal=new TermFundTotal();
									termFundTotal.setTotDisbAmount(mf.format(kindDisbSectorFunds[i][qtr]));
									termFundTotal.setTotExpAmount(mf.format(kindExpSectorFunds[i][qtr]));
									termFundTotal.setTotPlannedDisbAmount(mf.format(kindPlDisbSectorFunds[i][qtr]));
									termFund.getTermFundTotal().add(termFundTotal);
									totalDisb=totalDisb + kindDisbSectorFunds[i][qtr];
									totalExp=totalExp + kindExpSectorFunds[i][qtr];
									totalPlDisb=totalPlDisb + kindPlDisbSectorFunds[i][qtr];
								}
							}
							termFund.setTotDonorDisbAmount(mf.format(totalDisb));
							termFund.setTotDonorExpAmount(mf.format(totalExp));
							termFund.setTotDonorPlannedDisbAmount(mf.format(totalPlDisb));
							termFund.setTotDonorUnDisbAmount(mf.format(kindCommSectorAmount-totalDisb));
							totalPlDisb=totalDisb=totalExp=0.0;
						}
//						logger.debug("Added:'" + termFund.getTermAssistName());
						report.getTotalSectorTermAssistFund().add(termFund);	
					}
					termComm=totalPlDisb=totalDisb=totalExp=0.0;
					projCommAmount=projPlannedDisbAmount=projDisbAmount=projExpAmount=0.0;
					sectorCommAmount=sectorPlannedDisbAmount=sectorDisbAmount=sectorExpAmount=0.0;
					loanCommSectorAmount=grantCommSectorAmount=kindCommSectorAmount=0.0;
					for(int i=0;i<yrCount;i++)
					{
						for(int qtr=0;qtr<4;qtr++)
						{
							plDisbTermFunds[i][qtr]=0;
							disbTermFunds[i][qtr]=0;
							expTermFunds[i][qtr]=0;
							plDisbFund[i][qtr]=0;
							disbFund[i][qtr]=0;
							expFund[i][qtr]=0;
							plDisbSectorFund[i][qtr]=0;
							disbSectorFund[i][qtr]=0;
							expSectorFund[i][qtr]=0;
							kindExpSectorFunds[i][qtr]=0;
							kindDisbSectorFunds[i][qtr]=0;
							kindPlDisbSectorFunds[i][qtr]=0;
							grantExpSectorFunds[i][qtr]=0;
							grantDisbSectorFunds[i][qtr]=0;
							grantPlDisbSectorFunds[i][qtr]=0;
							loanExpSectorFunds[i][qtr]=0;
							loanDisbSectorFunds[i][qtr]=0;
							loanPlDisbSectorFunds[i][qtr]=0;
						}
					}
					sectorAssistance.clear();
					projCount=0;
//					donorCount=0;
					ampReports.add(report);
					report=null;
					logger.debug("Outside Sector");
				}
				
		/*		ampStrId=ampReportSector.getAmpSectorId();
				queryString = "select sector from " + AmpReportSector.class.getName() + " sector where sector.ampSectorId='" + ampStrId + "'"; 
				logger.debug("querystring: " + queryString);
				q = session.createQuery(queryString);	*/
				Iterator iterSector=ampProjectBySector.getAmpActivityId().iterator();
				while(iterSector.hasNext())
				{
					Long id = (Long) iterSector.next();
					if(inClause==null)
						inClause="'" + id + "'";
					else
						inClause=inClause + ",'" + id + "'";
					logger.debug("In Clause: " + inClause);
				}
				if(startDate==null && closeDate==null)
					queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.ampTeamId in(" + inClauseTeam + ")) and (report.ampActivityId in(" + inClause + ")) and (report.reportType='1') order by report.donorName,report.activityName,report.ampActivityId";
				else
					queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.ampTeamId in(" + inClauseTeam + ")) and (report.actualStartDate='" + startDate + "' or report.actualCompletionDate='" + closeDate + "') and (report.ampActivityId in(" + inClause + ")) and (report.reportType='1') order by report.donorName,report.activityName,report.ampActivityId";
				logger.debug("querystring: " + queryString);
				inClause=null;
				qry = session.createQuery(queryString);
		//		qry.setParameter("ampTeamId",ampTeamId,Hibernate.LONG) ;
				iterActivity=qry.list().iterator();
				while(iterActivity.hasNext())
				{
					AmpReportCache ampReportCache = (AmpReportCache) iterActivity.next();

					if(approvedActivityList.indexOf(ampReportCache.getAmpActivityId())==-1)
						continue;

					if(new Long(fiscalCalId).equals(Constants.ETH_FY) || new Long(fiscalCalId).equals(Constants.ETH_CAL))
					{
						if(ampReportCache.getTransactionDate()!=null)
						{
							GregorianCalendar calendar = new GregorianCalendar();
							EthiopianCalendar ec=new EthiopianCalendar();
							EthiopianCalendar tempDate=new EthiopianCalendar();
							calendar.setTime(ampReportCache.getTransactionDate());
							ec=tempDate.getEthiopianDate(calendar);
							if(new Long(fiscalCalId).equals(Constants.ETH_FY))
							{
								fiscalYear=(int)ec.ethFiscalYear;
								fiscalQuarter=(int)ec.ethFiscalQrt;
							}
							if(new Long(fiscalCalId).equals(Constants.ETH_CAL))
							{
								fiscalYear=(int)ec.ethYear;
								fiscalQuarter=(int)ec.ethQtr;
							}
							logger.debug("Ethiopian Fiscal Year: " + fiscalYear);
							logger.debug("From Year: " + fromYr);
							logger.debug("From Year: " + toYr);
/*							if(fiscalYear<fromYr || fiscalYear>toYr)
								continue;*/
						}
					}
					
					if(!ampModalityId.equals(All))
					{
						if(ampReportCache.getAmpModalityId()==null)
							continue;
						if(!(ampModalityId.equals(ampReportCache.getAmpModalityId())))
							continue;
					}

					if(!ampStatusId.equals(All))
					{
						if(ampReportCache.getAmpStatusId()==null)
							continue;
						if(!(ampStatusId.equals(ampReportCache.getAmpStatusId())))
							continue;
					}
	
					if(!ampDonorId.equals(All))
					{
						if(ampReportCache.getAmpDonorId()==null)
							continue;
						if(!(ampDonorId.equals(ampReportCache.getAmpDonorId())))
							continue;
					}
					if(!ampSectorId.equals(All))
					{
						int sflag=0;
						logger.debug("Selected Sector Id: " + ampSectorId);
						iterSector=DbUtil.getAmpReportSectorId(ampReportCache.getAmpActivityId()).iterator();
						while(iterSector.hasNext())
						{
							AmpReportSector sector=(AmpReportSector) iterSector.next();
							
							if(sector.getAmpSectorId().equals(ampSectorId) || new Long(sector.getSubSectorName()).equals(ampSectorId))
							{
								logger.debug("Condition true");
								sflag=1;
								break;
							}
						}
						if (sflag==0)
						{
							continue;
						}
					}
					
					if(!region.equals("All"))
					{
						ArrayList location=(ArrayList)DbUtil.getAmpReportLocation(ampReportCache.getAmpActivityId());
						if(location.indexOf(region)==-1)
							continue;
					}
					
					if(report==null)
					{
						report = new multiReport();
						report.setSector(ampProjectBySector.getSector().getSectorName());
						logger.debug("Sector: " + ampProjectBySector.getSector().getSectorName());
						report.setDonors(new ArrayList());
						report.setTotalSectorFund(new ArrayList());
						report.setTotalSectorTermAssistFund(new ArrayList());
						report.setTotalTeamFund(new ArrayList());
						report.setTotalTeamTermAssistFund(new ArrayList());
						report.setYearCount(yrCount*3);
						report.setCount((yrCount*3+4));
						ampTeamDonors=new AmpTeamDonors();
						ampTeamDonors.setDonorCount(++donorCount);
						ampTeamDonors.setProject(new ArrayList());
						if(ampReportCache.getAmpDonorId()==null && ampReportCache.getAmpFundingId()==null)
							ampTeamDonors.setDonorAgency("Unspecified");
						else
							ampTeamDonors.setDonorAgency(ampReportCache.getDonorName());
						logger.debug("Donor: " + ampTeamDonors.getDonorAgency());
						if(ampReportCache.getDonorName()!=null)
						{	
							project=new Project();
							project.setCount(++projCount);
							project.setName(ampReportCache.getActivityName());
							project.setAmpActivityId(ampReportCache.getAmpActivityId());
							project.setAmpFund(new ArrayList());
							project.setTermAssist(new ArrayList());
							termAssistName=ampReportCache.getTermAssistName();
							termFlag=0;
						}
					}
					
					if(ampReportCache.getAmpDonorId()==null && ampReportCache.getAmpFundingId()==null)
					{
						project=new Project();
						project.setCount(++projCount);
						project.setName(ampReportCache.getActivityName());
						project.setAmpActivityId(ampReportCache.getAmpActivityId());
						project.setAmpFund(new ArrayList());
						for(int i=0;i<yrCount;i++)
						{
							for(int qtr=0;qtr<4;qtr++)
							{
								AmpFund ampFund = new AmpFund();
								ampFund.setCommAmount(mf.format(0)); 
								ampFund.setDisbAmount(mf.format(0));
								ampFund.setExpAmount(mf.format(0));
								project.getAmpFund().add(ampFund) ;
							}
						}
						project.setProjCommAmount(mf.format(0));
						project.setProjDisbAmount(mf.format(0));
						project.setProjExpAmount(mf.format(0));
						project.setProjUnDisbAmount(mf.format(0));
						ampTeamDonors.getProject().add(project);
					}
					else
					if(ampReportCache.getAmpFundingId()!=null && !(ampReportCache.getDonorName().equals(ampTeamDonors.getDonorAgency())))
					{
						logger.debug("inside donor");
						if(!ampTeamDonors.getDonorAgency().equals("Unspecified"))
						{
							if(termFlag==1)
							{
								termAssist=new ProjectTermAssist();
								termAssist.setTermAssistName(termAssistName);
								if(sectorAssistance.indexOf(termAssist.getTermAssistName())==-1)
									sectorAssistance.add(termAssist.getTermAssistName());
								if(totalAssistance.indexOf(termAssist.getTermAssistName())==-1)
									totalAssistance.add(termAssist.getTermAssistName());
								termAssist.setTermAssistFund(new ArrayList());
								for(int i=0;i<yrCount;i++)
								{
									for(int qtr=0;qtr<4;qtr++)
									{
										AmpFund termFund = new AmpFund();
										termFund.setPlannedDisbAmount(mf.format(plDisbTermFunds[i][qtr])); 
										termFund.setDisbAmount(mf.format(disbTermFunds[i][qtr]));
										termFund.setExpAmount(mf.format(expTermFunds[i][qtr]));
										termAssist.getTermAssistFund().add(termFund);
										totalPlDisb=totalPlDisb + plDisbTermFunds[i][qtr];
										totalDisb=totalDisb + disbTermFunds[i][qtr];
										totalExp= totalExp + expTermFunds[i][qtr];
										plDisbFund[i][qtr]=plDisbFund[i][qtr] + plDisbTermFunds[i][qtr];
										disbFund[i][qtr]=disbFund[i][qtr] + disbTermFunds[i][qtr];
										expFund[i][qtr]=expFund[i][qtr] + expTermFunds[i][qtr];
									}
								}
								termAssist.setTermPlannedDisbAmount(mf.format(totalPlDisb));
								termAssist.setTermDisbAmount(mf.format(totalDisb));
								termAssist.setTermExpAmount(mf.format(totalExp));
								termAssist.setTermUnDisbAmount(mf.format(termComm-totalDisb));
								projCommAmount=projCommAmount + termComm;
								projPlannedDisbAmount=projPlannedDisbAmount + totalPlDisb;
								projDisbAmount=projDisbAmount + totalDisb;
								projExpAmount=projExpAmount + totalExp;
								project.getTermAssist().add(termAssist);
		//						logger.debug("Term Assist:" + termAssist.getTermAssistName());
							
								if(termAssist.getTermAssistName().equals("Loan"))
								{
									for(int i=0;i<yrCount;i++)
									{
										for (int qtr=0;qtr<4 ;qtr++)
										{
											loanDisbSectorFunds[i][qtr]=loanDisbSectorFunds[i][qtr] + disbTermFunds[i][qtr];
											loanExpSectorFunds[i][qtr]=loanExpSectorFunds[i][qtr] + expTermFunds[i][qtr];
											loanPlDisbSectorFunds[i][qtr]=loanPlDisbSectorFunds[i][qtr] + plDisbTermFunds[i][qtr];
											loanDisbTotalFunds[i][qtr]=loanDisbTotalFunds[i][qtr] + disbTermFunds[i][qtr];
											loanExpTotalFunds[i][qtr]=loanExpTotalFunds[i][qtr] + expTermFunds[i][qtr];
											loanPlDisbTotalFunds[i][qtr]=loanPlDisbTotalFunds[i][qtr] + plDisbTermFunds[i][qtr];
										}
									}
									loanCommSectorAmount=loanCommSectorAmount + termComm;
									loanCommTotalAmount=loanCommTotalAmount + termComm;
								}
								if(termAssist.getTermAssistName().equals("Grant"))
								{
									for(int i=0;i<yrCount;i++)
									{
										for (int qtr=0;qtr<4 ;qtr++)
										{
											grantDisbSectorFunds[i][qtr]=grantDisbSectorFunds[i][qtr] + disbTermFunds[i][qtr];
											grantExpSectorFunds[i][qtr]=grantExpSectorFunds[i][qtr] + expTermFunds[i][qtr];
											grantPlDisbSectorFunds[i][qtr]=grantPlDisbSectorFunds[i][qtr] + plDisbTermFunds[i][qtr];
											grantDisbTotalFunds[i][qtr]=grantDisbTotalFunds[i][qtr] + disbTermFunds[i][qtr];
											grantExpTotalFunds[i][qtr]=grantExpTotalFunds[i][qtr] + expTermFunds[i][qtr];
											grantPlDisbTotalFunds[i][qtr]=grantPlDisbTotalFunds[i][qtr] + plDisbTermFunds[i][qtr];
										}
									}
									grantCommSectorAmount=grantCommSectorAmount + termComm;
									grantCommTotalAmount=grantCommTotalAmount + termComm;
								}
								if(termAssist.getTermAssistName().equals("Kind"))
								{
									for(int i=0;i<yrCount;i++)
									{
										for (int qtr=0;qtr<4 ;qtr++)
										{
											kindDisbSectorFunds[i][qtr]=kindDisbSectorFunds[i][qtr] + disbTermFunds[i][qtr];
											kindExpSectorFunds[i][qtr]=kindExpSectorFunds[i][qtr] + expTermFunds[i][qtr];
											kindPlDisbSectorFunds[i][qtr]=kindPlDisbSectorFunds[i][qtr] + plDisbTermFunds[i][qtr];
											kindDisbTotalFunds[i][qtr]=kindDisbTotalFunds[i][qtr] + disbTermFunds[i][qtr];
											kindExpTotalFunds[i][qtr]=kindExpTotalFunds[i][qtr] + expTermFunds[i][qtr];
											kindPlDisbTotalFunds[i][qtr]=kindPlDisbTotalFunds[i][qtr] + plDisbTermFunds[i][qtr];
										}
									}
									kindCommSectorAmount=kindCommSectorAmount + termComm;
									kindCommTotalAmount=kindCommTotalAmount + termComm;
								}
	
		//						logger.debug("After term assist");
						
							}
							for(int i=0;i<yrCount;i++)
							{
								for(int qtr=0;qtr<4;qtr++)
								{
									AmpFund projFund = new AmpFund();
									projFund.setPlannedDisbAmount(mf.format(plDisbFund[i][qtr])); 
									projFund.setDisbAmount(mf.format(disbFund[i][qtr]));
									projFund.setExpAmount(mf.format(expFund[i][qtr]));
									project.getAmpFund().add(projFund) ;
								}
							}
							project.setProjPlannedDisbAmount(mf.format(projPlannedDisbAmount));
							project.setProjDisbAmount(mf.format(projDisbAmount));
							project.setProjExpAmount(mf.format(projExpAmount));
							project.setProjUnDisbAmount(mf.format(projCommAmount-projDisbAmount));
							ampTeamDonors.getProject().add(project);
							for(int i=0;i<yrCount;i++)
							{
								for(int qtr=0;qtr<4;qtr++)
								{
									plDisbSectorFund[i][qtr]=plDisbSectorFund[i][qtr] + plDisbFund[i][qtr];
									disbSectorFund[i][qtr]=disbSectorFund[i][qtr] + disbFund[i][qtr];
									expSectorFund[i][qtr]=expSectorFund[i][qtr] + expFund[i][qtr];
								}
												
							}
							sectorCommAmount=sectorCommAmount + projCommAmount;
							sectorPlannedDisbAmount=sectorPlannedDisbAmount + projPlannedDisbAmount;
							sectorDisbAmount=sectorDisbAmount + projDisbAmount;
							sectorExpAmount=sectorExpAmount + projExpAmount;
						}
						report.getDonors().add(ampTeamDonors);
						projCommAmount=projPlannedDisbAmount=projDisbAmount=projExpAmount=0;
						termComm=totalPlDisb=totalDisb=totalExp=0;
						for(int i=0;i<yrCount;i++)
						{
							for(int qtr=0;qtr<4;qtr++)
							{
								plDisbTermFunds[i][qtr]=0;
								disbTermFunds[i][qtr]=0;
								expTermFunds[i][qtr]=0;
								plDisbFund[i][qtr]=0;
								disbFund[i][qtr]=0;
								expFund[i][qtr]=0;
							}
						}

						projCount=0;
					//	donorCount=0;
						ampTeamDonors=new AmpTeamDonors();
						ampTeamDonors.setDonorAgency(ampReportCache.getDonorName());
						ampTeamDonors.setDonorCount(++donorCount);
						ampTeamDonors.setTotalDonorFund(new ArrayList());
						ampTeamDonors.setTotalDonorTermAssistFund(new ArrayList());
						ampTeamDonors.setProject(new ArrayList());
						project=new Project();
						project.setName(ampReportCache.getActivityName());
						project.setAmpActivityId(ampReportCache.getAmpActivityId());
						project.setCount(++projCount);
						project.setAmpFund(new ArrayList());
						project.setTermAssist(new ArrayList());
						termAssistName=ampReportCache.getTermAssistName();
						termFlag=0;
						logger.debug("Outside donor");
						
					}
					else
					if(ampReportCache.getAmpFundingId()!=null && ampReportCache.getDonorName().equals(ampTeamDonors.getDonorAgency()) && !(ampReportCache.getAmpActivityId().equals(project.getAmpActivityId())))
					{
						logger.debug("inside project");
						if(termFlag==1)
						{
							termAssist=new ProjectTermAssist();
							termAssist.setTermAssistName(termAssistName);
							if(sectorAssistance.indexOf(termAssist.getTermAssistName())==-1)
								sectorAssistance.add(termAssist.getTermAssistName());
							if(totalAssistance.indexOf(termAssist.getTermAssistName())==-1)
								totalAssistance.add(termAssist.getTermAssistName());
							termAssist.setTermAssistFund(new ArrayList());
							for(int i=0;i<yrCount;i++)
							{
								for(int qtr=0;qtr<4;qtr++)
								{
									AmpFund termFund = new AmpFund();
									termFund.setPlannedDisbAmount(mf.format(plDisbTermFunds[i][qtr])); 
									termFund.setDisbAmount(mf.format(disbTermFunds[i][qtr]));
									termFund.setExpAmount(mf.format(expTermFunds[i][qtr]));
									termAssist.getTermAssistFund().add(termFund);
									totalPlDisb=totalPlDisb + plDisbTermFunds[i][qtr];
									totalDisb=totalDisb + disbTermFunds[i][qtr];
									totalExp= totalExp + expTermFunds[i][qtr];
									plDisbFund[i][qtr]=plDisbFund[i][qtr] + plDisbTermFunds[i][qtr];
									disbFund[i][qtr]=disbFund[i][qtr] + disbTermFunds[i][qtr];
									expFund[i][qtr]=expFund[i][qtr] + expTermFunds[i][qtr];
								}
							}
							termAssist.setTermPlannedDisbAmount(mf.format(totalPlDisb));
							termAssist.setTermDisbAmount(mf.format(totalDisb));
							termAssist.setTermExpAmount(mf.format(totalExp));
							termAssist.setTermUnDisbAmount(mf.format(termComm-totalDisb));

							projCommAmount=projCommAmount + termComm;
							projPlannedDisbAmount=projPlannedDisbAmount + totalPlDisb;
							projDisbAmount=projDisbAmount + totalDisb;
							projExpAmount=projExpAmount + totalExp;
							project.getTermAssist().add(termAssist);
		//					logger.debug("Term Assist:" + termAssist.getTermAssistName());
						
							if(termAssist.getTermAssistName().equals("Loan"))
							{
								for(int i=0;i<yrCount;i++)
								{
									for (int qtr=0;qtr<4 ;qtr++)
									{
										loanDisbSectorFunds[i][qtr]=loanDisbSectorFunds[i][qtr] + disbTermFunds[i][qtr];
										loanExpSectorFunds[i][qtr]=loanExpSectorFunds[i][qtr] + expTermFunds[i][qtr];
										loanPlDisbSectorFunds[i][qtr]=loanPlDisbSectorFunds[i][qtr] + plDisbTermFunds[i][qtr];
										loanDisbTotalFunds[i][qtr]=loanDisbTotalFunds[i][qtr] + disbTermFunds[i][qtr];
										loanExpTotalFunds[i][qtr]=loanExpTotalFunds[i][qtr] + expTermFunds[i][qtr];
										loanPlDisbTotalFunds[i][qtr]=loanPlDisbTotalFunds[i][qtr] + plDisbTermFunds[i][qtr];
									}
								}
								loanCommSectorAmount=loanCommSectorAmount + termComm;
								loanCommTotalAmount=loanCommTotalAmount + termComm;
							}
							if(termAssist.getTermAssistName().equals("Grant"))
							{
								for(int i=0;i<yrCount;i++)
								{
									for (int qtr=0;qtr<4 ;qtr++)
									{
										grantDisbSectorFunds[i][qtr]=grantDisbSectorFunds[i][qtr] + disbTermFunds[i][qtr];
										grantExpSectorFunds[i][qtr]=grantExpSectorFunds[i][qtr] + expTermFunds[i][qtr];
										grantPlDisbSectorFunds[i][qtr]=grantPlDisbSectorFunds[i][qtr] + plDisbTermFunds[i][qtr];
										grantDisbTotalFunds[i][qtr]=grantDisbTotalFunds[i][qtr] + disbTermFunds[i][qtr];
										grantExpTotalFunds[i][qtr]=grantExpTotalFunds[i][qtr] + expTermFunds[i][qtr];
										grantPlDisbTotalFunds[i][qtr]=grantPlDisbTotalFunds[i][qtr] + plDisbTermFunds[i][qtr];
										
									}
								}
								grantCommSectorAmount=grantCommSectorAmount + termComm;
								grantCommTotalAmount=grantCommTotalAmount + termComm;
							}
							if(termAssist.getTermAssistName().equals("Kind"))
							{
								for(int i=0;i<yrCount;i++)
								{
									for (int qtr=0;qtr<4 ;qtr++)
									{
										kindDisbSectorFunds[i][qtr]=kindDisbSectorFunds[i][qtr] + disbTermFunds[i][qtr];
										kindExpSectorFunds[i][qtr]=kindExpSectorFunds[i][qtr] + expTermFunds[i][qtr];
										kindPlDisbSectorFunds[i][qtr]=kindPlDisbSectorFunds[i][qtr] + plDisbTermFunds[i][qtr];
										kindDisbTotalFunds[i][qtr]=kindDisbTotalFunds[i][qtr] + disbTermFunds[i][qtr];
										kindExpTotalFunds[i][qtr]=kindExpTotalFunds[i][qtr] + expTermFunds[i][qtr];
										kindPlDisbTotalFunds[i][qtr]=kindPlDisbTotalFunds[i][qtr] + plDisbTermFunds[i][qtr];
									}
								}
								kindCommSectorAmount=kindCommSectorAmount + termComm;
								kindCommTotalAmount=kindCommTotalAmount + termComm;
							}

		//						logger.debug("After term assist");
						
						}
						for(int i=0;i<yrCount;i++)
						{
							for(int qtr=0;qtr<4;qtr++)
							{
								AmpFund projFund = new AmpFund();
								projFund.setPlannedDisbAmount(mf.format(plDisbFund[i][qtr])); 
								projFund.setDisbAmount(mf.format(disbFund[i][qtr]));
								projFund.setExpAmount(mf.format(expFund[i][qtr]));
								project.getAmpFund().add(projFund) ;
							}
						}
						project.setProjPlannedDisbAmount(mf.format(projPlannedDisbAmount));
						project.setProjDisbAmount(mf.format(projDisbAmount));
						project.setProjExpAmount(mf.format(projExpAmount));
						project.setProjUnDisbAmount(mf.format(projCommAmount-projDisbAmount));
						ampTeamDonors.getProject().add(project);
						for(int i=0;i<yrCount;i++)
						{
							for(int qtr=0;qtr<4;qtr++)
							{
								plDisbSectorFund[i][qtr]=plDisbSectorFund[i][qtr] + plDisbFund[i][qtr];
								disbSectorFund[i][qtr]=disbSectorFund[i][qtr] + disbFund[i][qtr];
								expSectorFund[i][qtr]=expSectorFund[i][qtr] + expFund[i][qtr];
							}
											
						}
						sectorCommAmount=sectorCommAmount + projCommAmount;
						sectorPlannedDisbAmount=sectorPlannedDisbAmount + projPlannedDisbAmount;
						sectorDisbAmount=sectorDisbAmount + projDisbAmount;
						sectorExpAmount=sectorExpAmount + projExpAmount;
						projCommAmount=projPlannedDisbAmount=projDisbAmount=projExpAmount=0;
						termComm=totalPlDisb=totalDisb=totalExp=0;
						for(int i=0;i<yrCount;i++)
						{
							for(int qtr=0;qtr<4;qtr++)
							{
								plDisbTermFunds[i][qtr]=0;
								disbTermFunds[i][qtr]=0;
								expTermFunds[i][qtr]=0;
								plDisbFund[i][qtr]=0;
								disbFund[i][qtr]=0;
								expFund[i][qtr]=0;
							}
						}

						project=new Project();
						project.setName(ampReportCache.getActivityName());
						project.setAmpActivityId(ampReportCache.getAmpActivityId());
						project.setCount(++projCount);
						project.setAmpFund(new ArrayList());
						project.setTermAssist(new ArrayList());
						termAssistName=ampReportCache.getTermAssistName();
						termFlag=0;
						logger.debug("outside project");
					}
					else
					if(ampReportCache.getAmpFundingId()!=null && ampReportCache.getDonorName().equals(ampTeamDonors.getDonorAgency()) && ampReportCache.getAmpActivityId().equals(project.getAmpActivityId()) && !(ampReportCache.getTermAssistName().equals(termAssistName)))
					{
						logger.debug("Inside Terms");
						if(termFlag==1)
						{
							termAssist=new ProjectTermAssist();
							termAssist.setTermAssistName(termAssistName);
							if(sectorAssistance.indexOf(termAssist.getTermAssistName())==-1)
								sectorAssistance.add(termAssist.getTermAssistName());
							if(totalAssistance.indexOf(termAssist.getTermAssistName())==-1)
								totalAssistance.add(termAssist.getTermAssistName());
							termAssist.setTermAssistFund(new ArrayList());
							for(int i=0;i<yrCount;i++)
							{
								for(int qtr=0;qtr<4;qtr++)
								{
									AmpFund termFund = new AmpFund();
									termFund.setPlannedDisbAmount(mf.format(plDisbTermFunds[i][qtr])); 
									termFund.setDisbAmount(mf.format(disbTermFunds[i][qtr]));
									termFund.setExpAmount(mf.format(expTermFunds[i][qtr]));
									termAssist.getTermAssistFund().add(termFund);
									totalPlDisb=totalPlDisb + plDisbTermFunds[i][qtr];
									totalDisb=totalDisb + disbTermFunds[i][qtr];
									totalExp= totalExp + expTermFunds[i][qtr];
									plDisbFund[i][qtr]=plDisbFund[i][qtr] + plDisbTermFunds[i][qtr];
									disbFund[i][qtr]=disbFund[i][qtr] + disbTermFunds[i][qtr];
									expFund[i][qtr]=expFund[i][qtr] + expTermFunds[i][qtr];
								}
							}
							termAssist.setTermPlannedDisbAmount(mf.format(totalPlDisb));
							termAssist.setTermDisbAmount(mf.format(totalDisb));
							termAssist.setTermExpAmount(mf.format(totalExp));
							termAssist.setTermUnDisbAmount(mf.format(termComm-totalDisb));

							projCommAmount=projCommAmount + termComm;
							projPlannedDisbAmount=projPlannedDisbAmount + totalPlDisb;
							projDisbAmount=projDisbAmount + totalDisb;
							projExpAmount=projExpAmount + totalExp;

							project.getTermAssist().add(termAssist);
		//					logger.debug("Term Assist:" + termAssist.getTermAssistName());
						
							if(termAssist.getTermAssistName().equals("Loan"))
							{
								for(int i=0;i<yrCount;i++)
								{
									for (int qtr=0;qtr<4 ;qtr++)
									{
										loanDisbSectorFunds[i][qtr]=loanDisbSectorFunds[i][qtr] + disbTermFunds[i][qtr];
										loanExpSectorFunds[i][qtr]=loanExpSectorFunds[i][qtr] + expTermFunds[i][qtr];
										loanPlDisbSectorFunds[i][qtr]=loanPlDisbSectorFunds[i][qtr] + plDisbTermFunds[i][qtr];
										loanDisbTotalFunds[i][qtr]=loanDisbTotalFunds[i][qtr] + disbTermFunds[i][qtr];
										loanExpTotalFunds[i][qtr]=loanExpTotalFunds[i][qtr] + expTermFunds[i][qtr];
										loanPlDisbTotalFunds[i][qtr]=loanPlDisbTotalFunds[i][qtr] + plDisbTermFunds[i][qtr];
									}
								}
								loanCommSectorAmount=loanCommSectorAmount + termComm;
								loanCommTotalAmount=loanCommTotalAmount + termComm;

							}
							if(termAssist.getTermAssistName().equals("Grant"))
							{
								for(int i=0;i<yrCount;i++)
								{
									for (int qtr=0;qtr<4 ;qtr++)
									{
										grantDisbSectorFunds[i][qtr]=grantDisbSectorFunds[i][qtr] + disbTermFunds[i][qtr];
										grantExpSectorFunds[i][qtr]=grantExpSectorFunds[i][qtr] + expTermFunds[i][qtr];
										grantPlDisbSectorFunds[i][qtr]=grantPlDisbSectorFunds[i][qtr] + plDisbTermFunds[i][qtr];
										grantDisbTotalFunds[i][qtr]=grantDisbTotalFunds[i][qtr] + disbTermFunds[i][qtr];
										grantExpTotalFunds[i][qtr]=grantExpTotalFunds[i][qtr] + expTermFunds[i][qtr];
										grantPlDisbTotalFunds[i][qtr]=grantPlDisbTotalFunds[i][qtr] + plDisbTermFunds[i][qtr];
										
									}
								}
								grantCommSectorAmount=grantCommSectorAmount + termComm;
								grantCommTotalAmount=grantCommTotalAmount + termComm;
							}
							if(termAssist.getTermAssistName().equals("Kind"))
							{
								for(int i=0;i<yrCount;i++)
								{
									for (int qtr=0;qtr<4 ;qtr++)
									{
										kindDisbSectorFunds[i][qtr]=kindDisbSectorFunds[i][qtr] + disbTermFunds[i][qtr];
										kindExpSectorFunds[i][qtr]=kindExpSectorFunds[i][qtr] + expTermFunds[i][qtr];
										kindPlDisbSectorFunds[i][qtr]=kindPlDisbSectorFunds[i][qtr] + plDisbTermFunds[i][qtr];
										kindDisbTotalFunds[i][qtr]=kindDisbTotalFunds[i][qtr] + disbTermFunds[i][qtr];
										kindExpTotalFunds[i][qtr]=kindExpTotalFunds[i][qtr] + expTermFunds[i][qtr];
										kindPlDisbTotalFunds[i][qtr]=kindPlDisbTotalFunds[i][qtr] + plDisbTermFunds[i][qtr];
									}
								}
								kindCommSectorAmount=kindCommSectorAmount + termComm;
								kindCommTotalAmount=kindCommTotalAmount + termComm;
							}
						}
						termAssistName=ampReportCache.getTermAssistName();
						termFlag=0;
						termComm=totalPlDisb=totalDisb=totalExp=0.0;
						for(int i=0;i<yrCount;i++)
						{
							for(int qtr=0;qtr<4;qtr++)
							{
								plDisbTermFunds[i][qtr]=0;
								disbTermFunds[i][qtr]=0;
								expTermFunds[i][qtr]=0;
							}
						}
						logger.debug("Outside Terms");
					}
					if(ampReportCache.getFiscalYear()!=null && ampReportCache.getFiscalQuarter()!=null)
					{
						logger.debug("begin if");
						if(new Long(fiscalCalId).equals(Constants.GREGORIAN))
						{
							fiscalYear=ampReportCache.getFiscalYear().intValue();
							fiscalQuarter=ampReportCache.getFiscalQuarter().intValue();
						}
						if(ampReportCache.getCurrencyCode().equals("USD"))
							fromExchangeRate=1.0;
						else
							fromExchangeRate=DbUtil.getExchangeRate(ampReportCache.getCurrencyCode(),Constants.ACTUAL,ampReportCache.getTransactionDate());
						if(ampCurrencyCode.equals("USD"))
							toExchangeRate=1.0;
						else
							toExchangeRate=DbUtil.getExchangeRate(ampCurrencyCode,Constants.ACTUAL,ampReportCache.getTransactionDate());

						if(ampReportCache.getActualCommitment().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
						{
							amount=CurrencyWorker.convert1(ampReportCache.getActualCommitment().doubleValue(),fromExchangeRate,toExchangeRate);
							if(fiscalYear>=fromYr && fiscalYear<=toYr)
							{
								termFlag=1;
								termComm=termComm + amount;
							}
						}
						amount=0.0;
													
						if(ampReportCache.getActualDisbursement().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
						{
							amount=CurrencyWorker.convert1(ampReportCache.getActualDisbursement().doubleValue(),fromExchangeRate,toExchangeRate);
							if(fiscalYear>=fromYr && fiscalYear<=toYr)
							{
								termFlag=1;
								if(disbTermFunds[fiscalYear%fromYr][fiscalQuarter-1]==0)
									disbTermFunds[fiscalYear%fromYr][fiscalQuarter-1]=amount;
								else
								if(disbTermFunds[fiscalYear%fromYr][fiscalQuarter-1]>0)
									disbTermFunds[fiscalYear%fromYr][fiscalQuarter-1]=disbTermFunds[fiscalYear%fromYr][fiscalQuarter-1] + amount;
							}
						}
						amount=0.0;
						if(ampReportCache.getActualExpenditure().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
						{
							amount=CurrencyWorker.convert1(ampReportCache.getActualExpenditure().doubleValue(),fromExchangeRate,toExchangeRate);
							if(fiscalYear>=fromYr && fiscalYear<=toYr)
							{
								termFlag=1;
								if(expTermFunds[fiscalYear%fromYr][fiscalQuarter-1]==0)
									expTermFunds[fiscalYear%fromYr][fiscalQuarter-1]=amount;
								else
								if(expTermFunds[fiscalYear%fromYr][fiscalQuarter-1]>0)
									expTermFunds[fiscalYear%fromYr][fiscalQuarter-1]= expTermFunds[fiscalYear%fromYr][fiscalQuarter-1] + amount;
							}
						}
						amount=0.0;
						if(ampReportCache.getCurrencyCode().equals("USD"))
							fromExchangeRate=1.0;
						else
							fromExchangeRate=DbUtil.getExchangeRate(ampReportCache.getCurrencyCode(),Constants.PLANNED,ampReportCache.getTransactionDate());
						if(ampCurrencyCode.equals("USD"))
							toExchangeRate=1.0;
						else
							toExchangeRate=DbUtil.getExchangeRate(ampCurrencyCode,Constants.PLANNED,ampReportCache.getTransactionDate());
						if(ampReportCache.getPlannedDisbursement().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
						{
							amount=CurrencyWorker.convert1(ampReportCache.getPlannedDisbursement().doubleValue(),fromExchangeRate,toExchangeRate);
							if(fiscalYear>=fromYr && fiscalYear<=toYr)
							{
								termFlag=1;
								if(plDisbTermFunds[fiscalYear%fromYr][fiscalQuarter-1]==0)
									plDisbTermFunds[fiscalYear%fromYr][fiscalQuarter-1]=amount;
								else
								if(plDisbTermFunds[fiscalYear%fromYr][fiscalQuarter-1]>0)
									plDisbTermFunds[fiscalYear%fromYr][fiscalQuarter-1]=plDisbTermFunds[fiscalYear%fromYr][fiscalQuarter-1] + amount;
							}
						}
						amount=0.0;
										
					}

				}			
			}
			if(!(ampTeamDonors.getDonorAgency().equals("Unspecified")))
			{
				if(termFlag==1)
				{
					termAssist=new ProjectTermAssist();
					termAssist.setTermAssistName(termAssistName);
					if(sectorAssistance.indexOf(termAssist.getTermAssistName())==-1)
						sectorAssistance.add(termAssist.getTermAssistName());
					if(totalAssistance.indexOf(termAssist.getTermAssistName())==-1)
						totalAssistance.add(termAssist.getTermAssistName());
					termAssist.setTermAssistFund(new ArrayList());
					for(int i=0;i<yrCount;i++)
					{
						for(int qtr=0;qtr<4;qtr++)
						{
							AmpFund termFund = new AmpFund();
							termFund.setPlannedDisbAmount(mf.format(plDisbTermFunds[i][qtr])); 
							termFund.setDisbAmount(mf.format(disbTermFunds[i][qtr]));
							termFund.setExpAmount(mf.format(expTermFunds[i][qtr]));
							termAssist.getTermAssistFund().add(termFund);
							totalPlDisb=totalPlDisb + plDisbTermFunds[i][qtr];
							totalDisb=totalDisb + disbTermFunds[i][qtr];
							totalExp= totalExp + expTermFunds[i][qtr];
							plDisbFund[i][qtr]=plDisbFund[i][qtr] + plDisbTermFunds[i][qtr];
							disbFund[i][qtr]=disbFund[i][qtr] + disbTermFunds[i][qtr];
							expFund[i][qtr]=expFund[i][qtr] + expTermFunds[i][qtr];
						}
					}
					termAssist.setTermPlannedDisbAmount(mf.format(totalPlDisb));
					termAssist.setTermDisbAmount(mf.format(totalDisb));
					termAssist.setTermExpAmount(mf.format(totalExp));
					termAssist.setTermUnDisbAmount(mf.format(termComm-totalDisb));
					projPlannedDisbAmount=projPlannedDisbAmount + totalPlDisb;
					projDisbAmount=projDisbAmount + totalDisb;
					projExpAmount=projExpAmount + totalExp;
					project.getTermAssist().add(termAssist);
		//			logger.debug("Term Assist:" + termAssist.getTermAssistName());
							
					if(termAssist.getTermAssistName().equals("Loan"))
					{
						for(int i=0;i<yrCount;i++)
						{
							for (int qtr=0;qtr<4 ;qtr++)
							{
								loanDisbSectorFunds[i][qtr]=loanDisbSectorFunds[i][qtr] + disbTermFunds[i][qtr];
								loanExpSectorFunds[i][qtr]=loanExpSectorFunds[i][qtr] + expTermFunds[i][qtr];
								loanPlDisbSectorFunds[i][qtr]=loanPlDisbSectorFunds[i][qtr] + plDisbTermFunds[i][qtr];
								loanDisbTotalFunds[i][qtr]=loanDisbTotalFunds[i][qtr] + disbTermFunds[i][qtr];
								loanExpTotalFunds[i][qtr]=loanExpTotalFunds[i][qtr] + expTermFunds[i][qtr];
								loanPlDisbTotalFunds[i][qtr]=loanPlDisbTotalFunds[i][qtr] + plDisbTermFunds[i][qtr];
							}
						}
						loanCommSectorAmount=loanCommSectorAmount + termComm;
						loanCommTotalAmount=loanCommTotalAmount + termComm;
					}
					if(termAssist.getTermAssistName().equals("Grant"))
					{
						for(int i=0;i<yrCount;i++)
						{
							for (int qtr=0;qtr<4 ;qtr++)
							{
								grantDisbSectorFunds[i][qtr]=grantDisbSectorFunds[i][qtr] + disbTermFunds[i][qtr];
								grantExpSectorFunds[i][qtr]=grantExpSectorFunds[i][qtr] + expTermFunds[i][qtr];
								grantPlDisbSectorFunds[i][qtr]=grantPlDisbSectorFunds[i][qtr] + plDisbTermFunds[i][qtr];
								grantDisbTotalFunds[i][qtr]=grantDisbTotalFunds[i][qtr] + disbTermFunds[i][qtr];
								grantExpTotalFunds[i][qtr]=grantExpTotalFunds[i][qtr] + expTermFunds[i][qtr];
								grantPlDisbTotalFunds[i][qtr]=grantPlDisbTotalFunds[i][qtr] + plDisbTermFunds[i][qtr];
							}
						}
						grantCommSectorAmount=grantCommSectorAmount + termComm;
						grantCommTotalAmount=grantCommTotalAmount + termComm;
					}
					if(termAssist.getTermAssistName().equals("Kind"))
					{
						for(int i=0;i<yrCount;i++)
						{
							for (int qtr=0;qtr<4 ;qtr++)
							{
								kindDisbSectorFunds[i][qtr]=kindDisbSectorFunds[i][qtr] + disbTermFunds[i][qtr];
								kindExpSectorFunds[i][qtr]=kindExpSectorFunds[i][qtr] + expTermFunds[i][qtr];
								kindPlDisbSectorFunds[i][qtr]=kindPlDisbSectorFunds[i][qtr] + plDisbTermFunds[i][qtr];
								kindDisbTotalFunds[i][qtr]=kindDisbTotalFunds[i][qtr] + disbTermFunds[i][qtr];
								kindExpTotalFunds[i][qtr]=kindExpTotalFunds[i][qtr] + expTermFunds[i][qtr];
								kindPlDisbTotalFunds[i][qtr]=kindPlDisbTotalFunds[i][qtr] + plDisbTermFunds[i][qtr];
							}
						}
						kindCommSectorAmount=kindCommSectorAmount + termComm;
						kindCommTotalAmount=kindCommTotalAmount + termComm;
					}

		//			logger.debug("After term assist");
						
				}
				for(int i=0;i<yrCount;i++)
				{
					for(int qtr=0;qtr<4;qtr++)
					{
						AmpFund projFund = new AmpFund();
						projFund.setPlannedDisbAmount(mf.format(plDisbFund[i][qtr])); 
						projFund.setDisbAmount(mf.format(disbFund[i][qtr]));
						projFund.setExpAmount(mf.format(expFund[i][qtr]));
						project.getAmpFund().add(projFund) ;
					}
				}
				project.setProjPlannedDisbAmount(mf.format(projPlannedDisbAmount));
				project.setProjDisbAmount(mf.format(projDisbAmount));
				project.setProjExpAmount(mf.format(projExpAmount));
				project.setProjUnDisbAmount(mf.format(projCommAmount-projDisbAmount));
				ampTeamDonors.getProject().add(project);
				for(int i=0;i<yrCount;i++)
				{
					for(int qtr=0;qtr<4;qtr++)
					{
						plDisbSectorFund[i][qtr]=plDisbSectorFund[i][qtr] + plDisbFund[i][qtr];
						disbSectorFund[i][qtr]=disbSectorFund[i][qtr] + disbFund[i][qtr];
						expSectorFund[i][qtr]=expSectorFund[i][qtr] + expFund[i][qtr];
					}
				}
				sectorCommAmount=sectorCommAmount + projCommAmount;
				sectorPlannedDisbAmount=sectorPlannedDisbAmount + projPlannedDisbAmount;
				sectorDisbAmount=sectorDisbAmount + projDisbAmount;
				sectorExpAmount=sectorExpAmount + projExpAmount;
				for(int i=0;i<yrCount;i++)
				{
					for(int qtr=0;qtr<4;qtr++)
					{
						plDisbTotalFund[i][qtr]=plDisbTotalFund[i][qtr] + plDisbSectorFund[i][qtr];
						disbTotalFund[i][qtr]=disbTotalFund[i][qtr] + disbSectorFund[i][qtr];
						expTotalFund[i][qtr]=expTotalFund[i][qtr] + expSectorFund[i][qtr];
					}
				}
				totalCommAmount=totalCommAmount + sectorCommAmount;
				totalPlannedDisbAmount=totalPlannedDisbAmount + sectorPlannedDisbAmount;
				totalDisbAmount=totalDisbAmount + sectorDisbAmount;
				totalExpAmount=totalExpAmount + sectorExpAmount;
			}
			report.getDonors().add(ampTeamDonors);
			for(int i=0;i<yrCount;i++)
			{
				for(int qtr=0;qtr<4;qtr++)
				{
					FundTotal total=new FundTotal();
					total.setTotPlannedDisbAmount(mf.format(plDisbSectorFund[i][qtr]));
					total.setTotDisbAmount(mf.format(disbSectorFund[i][qtr]));
					total.setTotExpAmount(mf.format(expSectorFund[i][qtr]));
					report.getTotalSectorFund().add(total);	
				}
			}
			report.setSectorPlannedDisbAmount(mf.format(sectorPlannedDisbAmount));
			report.setSectorDisbAmount(mf.format(sectorDisbAmount));
			report.setSectorExpAmount(mf.format(sectorExpAmount));
			report.setSectorUnDisbAmount(mf.format(sectorCommAmount-sectorDisbAmount));
			for(int i=0;i<yrCount;i++)
			{
				for(int qtr=0;qtr<4;qtr++)
				{
					FundTotal total=new FundTotal();
					total.setTotPlannedDisbAmount(mf.format(plDisbTotalFund[i][qtr]));
					total.setTotDisbAmount(mf.format(disbTotalFund[i][qtr]));
					total.setTotExpAmount(mf.format(expTotalFund[i][qtr]));
					report.getTotalTeamFund().add(total);	
				}
			}
			report.setTeamPlannedDisbAmount(mf.format(totalPlannedDisbAmount));
			report.setTeamDisbAmount(mf.format(totalDisbAmount));
			report.setTeamExpAmount(mf.format(totalExpAmount));
			report.setTeamUnDisbAmount(mf.format(totalCommAmount-totalDisbAmount));
			termComm=totalPlDisb=totalDisb=totalExp=0.0;
			Iterator sectorIter=sectorAssistance.iterator();
			while(sectorIter.hasNext())
			{
				String assist=(String) sectorIter.next();
				logger.debug("Assist:" + assist + ":");
				TermFund termFund=new TermFund();
				if(assist.equals("Loan"))
				{
					termFund.setTermAssistName("Loan");
					termFund.setTermFundTotal(new ArrayList());
					for(int i=0;i<yrCount;i++)
					{
						for(int qtr=0;qtr<4;qtr++)
						{
							TermFundTotal termFundTotal=new TermFundTotal();
							termFundTotal.setTotDisbAmount(mf.format(loanDisbSectorFunds[i][qtr]));
							termFundTotal.setTotExpAmount(mf.format(loanExpSectorFunds[i][qtr]));
							termFundTotal.setTotPlannedDisbAmount(mf.format(loanPlDisbSectorFunds[i][qtr]));
							termFund.getTermFundTotal().add(termFundTotal);
							totalDisb=totalDisb + loanDisbSectorFunds[i][qtr];
							totalExp=totalExp + loanExpSectorFunds[i][qtr];
							totalPlDisb=totalPlDisb + loanPlDisbSectorFunds[i][qtr];
						}
					}
					termFund.setTotDonorDisbAmount(mf.format(totalDisb));
					termFund.setTotDonorExpAmount(mf.format(totalExp));
					termFund.setTotDonorPlannedDisbAmount(mf.format(totalPlDisb));
					termFund.setTotDonorUnDisbAmount(mf.format(loanCommSectorAmount-totalDisb));
					totalPlDisb=totalDisb=totalExp=0.0;
				}
				if(assist.equals("Grant"))
				{
					termFund.setTermAssistName("Grant");
					termFund.setTermFundTotal(new ArrayList());
					for(int i=0;i<yrCount;i++)
					{
						for(int qtr=0;qtr<4;qtr++)
						{
							TermFundTotal termFundTotal=new TermFundTotal();
							termFundTotal.setTotDisbAmount(mf.format(grantDisbSectorFunds[i][qtr]));
							termFundTotal.setTotExpAmount(mf.format(grantExpSectorFunds[i][qtr]));
							termFundTotal.setTotPlannedDisbAmount(mf.format(grantPlDisbSectorFunds[i][qtr]));
							termFund.getTermFundTotal().add(termFundTotal);
							totalDisb=totalDisb + grantDisbSectorFunds[i][qtr];
							totalExp=totalExp + grantExpSectorFunds[i][qtr];
							totalPlDisb=totalPlDisb + grantPlDisbSectorFunds[i][qtr];
						}
					}
					termFund.setTotDonorDisbAmount(mf.format(totalDisb));
					termFund.setTotDonorExpAmount(mf.format(totalExp));
					termFund.setTotDonorPlannedDisbAmount(mf.format(totalPlDisb));
					termFund.setTotDonorUnDisbAmount(mf.format(grantCommSectorAmount-totalDisb));
					totalPlDisb=totalDisb=totalExp=0.0;
				}
				if(assist.equals("In Kind"))
				{
					termFund.setTermAssistName("In Kind");
					termFund.setTermFundTotal(new ArrayList());
					for(int i=0;i<yrCount;i++)
					{
						for(int qtr=0;qtr<4;qtr++)
						{
							TermFundTotal termFundTotal=new TermFundTotal();
							termFundTotal.setTotDisbAmount(mf.format(kindDisbSectorFunds[i][qtr]));
							termFundTotal.setTotExpAmount(mf.format(kindExpSectorFunds[i][qtr]));
							termFundTotal.setTotPlannedDisbAmount(mf.format(kindPlDisbSectorFunds[i][qtr]));
							termFund.getTermFundTotal().add(termFundTotal);
							totalDisb=totalDisb + kindDisbSectorFunds[i][qtr];
							totalExp=totalExp + kindExpSectorFunds[i][qtr];
							totalPlDisb=totalPlDisb + kindPlDisbSectorFunds[i][qtr];
						}
					}
					termFund.setTotDonorDisbAmount(mf.format(totalDisb));
					termFund.setTotDonorExpAmount(mf.format(totalExp));
					termFund.setTotDonorPlannedDisbAmount(mf.format(totalPlDisb));
					termFund.setTotDonorUnDisbAmount(mf.format(kindCommSectorAmount-totalDisb));
					totalPlDisb=totalDisb=totalExp=0.0;
				}
				logger.debug("Added:'" + termFund.getTermAssistName());
				report.getTotalSectorTermAssistFund().add(termFund);	
			}
			totalPlDisb=totalDisb=totalExp=0.0;
			sectorIter=totalAssistance.iterator();
			while(sectorIter.hasNext())
			{
				String assist=(String) sectorIter.next();
				logger.debug("Assist:" + assist + ":");
				TermFund termFund=new TermFund();
				if(assist.equals("Loan"))
				{
					termFund.setTermAssistName("Loan");
					termFund.setTermFundTotal(new ArrayList());
					for(int i=0;i<yrCount;i++)
					{
						for(int qtr=0;qtr<4;qtr++)
						{
							TermFundTotal termFundTotal=new TermFundTotal();
							termFundTotal.setTotDisbAmount(mf.format(loanDisbTotalFunds[i][qtr]));
							termFundTotal.setTotExpAmount(mf.format(loanExpTotalFunds[i][qtr]));
							termFundTotal.setTotPlannedDisbAmount(mf.format(loanPlDisbTotalFunds[i][qtr]));
							termFund.getTermFundTotal().add(termFundTotal);
							totalDisb=totalDisb + loanDisbTotalFunds[i][qtr];
							totalExp=totalExp + loanExpTotalFunds[i][qtr];
							totalPlDisb=totalPlDisb + loanPlDisbTotalFunds[i][qtr];
						}
					}
					termFund.setTotDonorDisbAmount(mf.format(totalDisb));
					termFund.setTotDonorExpAmount(mf.format(totalExp));
					termFund.setTotDonorPlannedDisbAmount(mf.format(totalPlDisb));
					termFund.setTotDonorUnDisbAmount(mf.format(loanCommTotalAmount-totalDisb));
					totalPlDisb=totalDisb=totalExp=0.0;
				}
				if(assist.equals("Grant"))
				{
					termFund.setTermAssistName("Grant");
					termFund.setTermFundTotal(new ArrayList());
					for(int i=0;i<yrCount;i++)
					{
						for(int qtr=0;qtr<4;qtr++)
						{
							TermFundTotal termFundTotal=new TermFundTotal();
							termFundTotal.setTotDisbAmount(mf.format(grantDisbTotalFunds[i][qtr]));
							termFundTotal.setTotExpAmount(mf.format(grantExpTotalFunds[i][qtr]));
							termFundTotal.setTotPlannedDisbAmount(mf.format(grantPlDisbTotalFunds[i][qtr]));
							termFund.getTermFundTotal().add(termFundTotal);
							totalDisb=totalDisb + grantDisbTotalFunds[i][qtr];
							totalExp=totalExp + grantExpTotalFunds[i][qtr];
							totalPlDisb=totalPlDisb + grantPlDisbTotalFunds[i][qtr];
						}
					}
					termFund.setTotDonorDisbAmount(mf.format(totalDisb));
					termFund.setTotDonorExpAmount(mf.format(totalExp));
					termFund.setTotDonorPlannedDisbAmount(mf.format(totalPlDisb));
					termFund.setTotDonorUnDisbAmount(mf.format(grantCommTotalAmount-totalDisb));
					totalPlDisb=totalDisb=totalExp=0.0;
				}
				if(assist.equals("In Kind"))
				{
					termFund.setTermAssistName("In Kind");
					termFund.setTermFundTotal(new ArrayList());
					for(int i=0;i<yrCount;i++)
					{
						for(int qtr=0;qtr<4;qtr++)
						{
							TermFundTotal termFundTotal=new TermFundTotal();
							termFundTotal.setTotDisbAmount(mf.format(kindDisbTotalFunds[i][qtr]));
							termFundTotal.setTotExpAmount(mf.format(kindExpTotalFunds[i][qtr]));
							termFundTotal.setTotPlannedDisbAmount(mf.format(kindPlDisbTotalFunds[i][qtr]));
							termFund.getTermFundTotal().add(termFundTotal);
							totalDisb=totalDisb + kindDisbTotalFunds[i][qtr];
							totalExp=totalExp + kindExpTotalFunds[i][qtr];
							totalPlDisb=totalPlDisb + kindPlDisbTotalFunds[i][qtr];
						}
					}
					termFund.setTotDonorDisbAmount(mf.format(totalDisb));
					termFund.setTotDonorExpAmount(mf.format(totalExp));
					termFund.setTotDonorPlannedDisbAmount(mf.format(totalPlDisb));
					termFund.setTotDonorUnDisbAmount(mf.format(kindCommTotalAmount-totalDisb));
					totalPlDisb=totalDisb=totalExp=0.0;
				}
				logger.debug("Added:'" + termFund.getTermAssistName());
				report.getTotalTeamTermAssistFund().add(termFund);	
			}
			ampReports.add(report);
		}
		catch(Exception ex) 		
		{
			logger.debug("Unable to get report names  from database " + ex.getMessage());
		}
		finally 
		{
			try 
			{
				PersistenceManager.releaseSession(session);
			}
			catch (Exception ex2) 
			{
				logger.debug("releaseSession() failed ");
			}
		}
		return ampReports ;
	}

	public static ArrayList getAmpReportByProjectByDonor(Long ampTeamId,int fromYr,int toYr,String perspective,String ampCurrencyCode,Long ampModalityId,Long ampStatusId,Long ampDonorId,Long ampSectorId,int fiscalCalId,String startDate,String closeDate,String region)
	{
		Session session = null ;
		Query q = null ;
		ArrayList ampReports = new ArrayList() ;
		ArrayList donorAssistance = new ArrayList() ;
		ArrayList teamAssistance = new ArrayList() ;
		String termAssistName=null;
		String queryString = null;
		Iterator iter=null;
		Long All=new Long(0);
		int yrCount = (toYr - fromYr)+1;
		int fiscalYear=0,fiscalQuarter=0;
		double[][] termFunds=new double[yrCount][4];
		double[][] projFunds=new double[yrCount][4];
		double[][] donorFunds=new double[yrCount][4];
		double[][] loanDonorFunds=new double[(yrCount+1)][5];
		double[][] grantDonorFunds=new double[(yrCount+1)][5];
		double[][] kindDonorFunds=new double[(yrCount+1)][5];
		double[][] loanTeamFunds=new double[yrCount+1][5];
		double[][] grantTeamFunds=new double[yrCount+1][5];
		double[][] kindTeamFunds=new double[yrCount+1][5];
		double[][] teamFunds=new double[yrCount][4];
		Long selTeamId=null;
		double totComm = 0.0 ;
		double totDisb = 0.0 ;
		double totExp = 0.0;
		double totPlannedDisb = 0.0;
		double toExchangeRate=1.0;
		double fromExchangeRate=0.0;
		double amount=0.0;
		double projCommAmount=0.0;
		double projDisbAmount=0.0;
		double projExpAmount=0.0;
		double projPlannedDisbAmount=0.0;
		double projUnDisbAmount=0.0;
		double donorCommAmount=0.0;
		double donorDisbAmount=0.0;
		double donorExpAmount=0.0;
		double donorPlannedDisbAmount=0.0;
		double donorUnDisbAmount=0.0;
		double teamCommAmount=0.0;
		double teamDisbAmount=0.0;
		double teamExpAmount=0.0;
		double teamPlannedDisbAmount=0.0;
		double teamUnDisbAmount=0.0;
		double actualCommitment=0.0;
		double actualDisbursement=0.0;
		int fiscalYearFlag=0;
		int termFlag=0;
		int donorCount=0;
		int projCount=0;
		int rowspan=1;
		String statusFlag=null;
		String inClause=null;
		Iterator iterSector=null;
		DecimalFormat mf = new DecimalFormat("###,###,###,###,###") ;
		ArrayList approvedActivityList=new ArrayList();
				
		try
		{
			logger.debug("Modality Id: " + ampModalityId);
			ArrayList dbReturnSet=(ArrayList)DbUtil.getAmpLevel0Teams(ampTeamId);				
			if(dbReturnSet.size()==0)
				inClause= "'" + ampTeamId + "'";
			else
			{
				iter=dbReturnSet.iterator();
				while(iter.hasNext())
				{
					Long teamId= (Long) iter.next();
					if(inClause==null)
						inClause="'" + teamId + "'";
					else
						inClause=inClause + ",'" + teamId + "'";
				}
			}
			logger.debug("Inclause: " + inClause);
			approvedActivityList=DbUtil.getApprovedActivities(inClause);

			session = PersistenceManager.getSession();
			if(startDate==null && closeDate==null)
				queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.donorName is not null) and (report.ampTeamId in(" + inClause + ")) and (report.reportType='1') order by report.ampTeamId,report.donorName,report.activityName,report.ampActivityId,report.termAssistName,report.fiscalYear";
			else
				queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.donorName is not null) and (report.ampTeamId in(" + inClause + ")) and (report.actualStartDate='" + startDate + "' or report.actualCompletionDate='" + closeDate + "') and (report.reportType='1') order by report.ampTeamId,report.donorName,report.activityName,report.ampActivityId,report.termAssistName,report.fiscalYear";
			logger.debug("querystring: " + queryString);
			q = session.createQuery(queryString);	
			multiReport report =null;
			AmpTeamDonors ampTeamDonors=null;
			Project project=null;
			ProjectTermAssist termAssist=null;
			TermFund donorTermAssist=null;
			TermFund teamTermAssist=null;
			ArrayList donorTotal=new ArrayList();
			ArrayList termTotal=new ArrayList();

			if(q!=null)
			{
				iter = q.list().iterator();
				while(iter.hasNext())
				{
					AmpReportCache ampReportCache = (AmpReportCache) iter.next(); 

					if(approvedActivityList.indexOf(ampReportCache.getAmpActivityId())==-1)
						continue;

					if(new Long(fiscalCalId).equals(Constants.ETH_FY) || new Long(fiscalCalId).equals(Constants.ETH_CAL))
					{
						if(ampReportCache.getTransactionDate()!=null)
						{
							GregorianCalendar calendar = new GregorianCalendar();
							EthiopianCalendar ec=new EthiopianCalendar();
							EthiopianCalendar tempDate=new EthiopianCalendar();
							calendar.setTime(ampReportCache.getTransactionDate());
							ec=tempDate.getEthiopianDate(calendar);
							if(new Long(fiscalCalId).equals(Constants.ETH_FY))
							{
								fiscalYear=(int)ec.ethFiscalYear;
								fiscalQuarter=(int)ec.ethFiscalQrt;
							}
							if(new Long(fiscalCalId).equals(Constants.ETH_CAL))
							{
								fiscalYear=(int)ec.ethYear;
								fiscalQuarter=(int)ec.ethQtr;
							}
							logger.debug("Ethiopian Fiscal Year: " + fiscalYear);
							logger.debug("From Year: " + fromYr);
							logger.debug("From Year: " + toYr);
/*							if(fiscalYear<fromYr || fiscalYear>toYr)
								continue;*/
						}
					}

					if(!ampModalityId.equals(All))
					{
						if(ampReportCache.getAmpModalityId()==null)
							continue;
						if(!(ampModalityId.equals(ampReportCache.getAmpModalityId())))
							continue;
					}

					if(!ampStatusId.equals(All))
					{
						if(ampReportCache.getAmpStatusId()==null)
							continue;
						if(!(ampStatusId.equals(ampReportCache.getAmpStatusId())))
							continue;
					}
	
					if(!ampDonorId.equals(All))
					{
						if(ampReportCache.getAmpDonorId()==null)
							continue;
						if(!(ampDonorId.equals(ampReportCache.getAmpDonorId())))
							continue;
					}

					if(!ampSectorId.equals(All))
					{
						int sflag=0;
						iterSector=DbUtil.getAmpReportSectorId(ampReportCache.getAmpActivityId()).iterator();
						while(iterSector.hasNext())
						{
							AmpReportSector sector=(AmpReportSector) iterSector.next();
							if(sector.getAmpSectorId().equals(ampSectorId))
							{
								sflag=1;
								break;
							}
							if(sector.getAmpSubSectorId().equals(new Long(0)))
							{
								if(new Long(sector.getSubSectorName()).equals(ampSectorId))
								{
									sflag=1;
									break;
								}
							}
							if(!(sector.getAmpSubSectorId().equals(new Long(0))) && sector.getAmpSubSectorId().equals(ampSectorId))
							{
								sflag=1;
								break;
							}
						}
						if (sflag==0)
						{
							continue;
						}
					}
					
					if(!region.equals("All"))
					{
						ArrayList location=(ArrayList)DbUtil.getAmpReportLocation(ampReportCache.getAmpActivityId());
						if(location.indexOf(region)==-1)
							continue;
					}

					
				//	logger.debug("Report Team Id: " +  report.getAmpTeamId());
					logger.debug("Cache Team Id: " +  ampReportCache.getAmpTeamId());

					if(report==null || !(report.getAmpTeamId().equals(ampReportCache.getAmpTeamId())))
					{
						if(report!=null)
						{
							if(termFlag==1)
							{
								termAssist=new ProjectTermAssist();
								termAssist.setTermAssistName(termAssistName);
								if(donorAssistance.indexOf(termAssist.getTermAssistName())==-1)
								{
									logger.debug("Donor Assistance Added:" + termAssist.getTermAssistName() + ":");
									donorAssistance.add(termAssist.getTermAssistName());
								}
								if(teamAssistance.indexOf(termAssist.getTermAssistName())==-1)
								{
									logger.debug("Team Assistance Added:" + termAssist.getTermAssistName() + ":");
									teamAssistance.add(termAssist.getTermAssistName());
								}
								termAssist.setTermAssistFund(new ArrayList());
								for(int i=0;i<yrCount;i++)
								{
									AmpFund termFund = new AmpFund();
									termFund.setCommAmount(mf.format(termFunds[i][0])); 
									termFund.setDisbAmount(mf.format(termFunds[i][1]));
									termFund.setExpAmount(mf.format(termFunds[i][2]));
									termFund.setPlannedDisbAmount(mf.format(termFunds[i][3]));
									termAssist.getTermAssistFund().add(termFund);
									totComm=totComm + termFunds[i][0];
									totDisb=totDisb + termFunds[i][1];
									totExp=totExp + termFunds[i][2];
									totPlannedDisb=totPlannedDisb + termFunds[i][3];
								}
								termAssist.setTermCommAmount(mf.format(totComm));
								termAssist.setTermDisbAmount(mf.format(totDisb));
								termAssist.setTermExpAmount(mf.format(totExp));
								termAssist.setTermPlannedDisbAmount(mf.format(totPlannedDisb));
								termAssist.setTermUnDisbAmount(mf.format(actualCommitment - actualDisbursement));
								projCommAmount=projCommAmount + totComm;
								projDisbAmount=projDisbAmount + totDisb;
								projExpAmount=projExpAmount + totExp;
								projPlannedDisbAmount=projPlannedDisbAmount + totPlannedDisb;
								projUnDisbAmount=projUnDisbAmount + (actualCommitment-actualDisbursement);
								project.getTermAssist().add(termAssist);
								for(int i=0;i<=yrCount;i++)
								{
									if(termAssist.getTermAssistName().equals("Loan"))
									{
										if(i<yrCount)
										{
											loanDonorFunds[i][0]=loanDonorFunds[i][0] + termFunds[i][0];
											loanDonorFunds[i][1]=loanDonorFunds[i][1] + termFunds[i][1];
											loanDonorFunds[i][2]=loanDonorFunds[i][2] + termFunds[i][2];
											loanDonorFunds[i][3]=loanDonorFunds[i][3] + termFunds[i][3];
										}
										if(i==yrCount)
										{
											loanDonorFunds[i][0]=loanDonorFunds[i][0] + totComm;
											loanDonorFunds[i][1]=loanDonorFunds[i][1] + totDisb;
											loanDonorFunds[i][2]=loanDonorFunds[i][2] + totExp;
											loanDonorFunds[i][3]=loanDonorFunds[i][3] + totPlannedDisb;
											loanDonorFunds[i][4]=loanDonorFunds[i][4] + (actualCommitment-actualDisbursement);
										}
									}
									if(termAssist.getTermAssistName().equals("Grant"))
									{
										if(i<yrCount)
										{
											grantDonorFunds[i][0]=grantDonorFunds[i][0] + termFunds[i][0];
											grantDonorFunds[i][1]=grantDonorFunds[i][1] + termFunds[i][1];
											grantDonorFunds[i][2]=grantDonorFunds[i][2] + termFunds[i][2];
											grantDonorFunds[i][3]=grantDonorFunds[i][3] + termFunds[i][3];
										}	
										if(i==yrCount)
										{
											grantDonorFunds[i][0]=grantDonorFunds[i][0] + totComm;
											grantDonorFunds[i][1]=grantDonorFunds[i][1] + totDisb;
											grantDonorFunds[i][2]=grantDonorFunds[i][2] + totExp;
											grantDonorFunds[i][3]=grantDonorFunds[i][3] + totPlannedDisb;
											grantDonorFunds[i][4]=grantDonorFunds[i][4] + (actualCommitment-actualDisbursement);
										}
									}
									if(termAssist.getTermAssistName().equals("In Kind"))
									{
										if(i<yrCount)
										{
											kindDonorFunds[i][0]=kindDonorFunds[i][0] + termFunds[i][0];
											kindDonorFunds[i][1]=kindDonorFunds[i][1] + termFunds[i][1];
											kindDonorFunds[i][2]=kindDonorFunds[i][2] + termFunds[i][2];
											kindDonorFunds[i][3]=kindDonorFunds[i][3] + termFunds[i][3];
										}
										if(i==yrCount)
										{
											kindDonorFunds[i][0]=kindDonorFunds[i][0] + totComm;
											kindDonorFunds[i][1]=kindDonorFunds[i][1] + totDisb;
											kindDonorFunds[i][2]=kindDonorFunds[i][2] + totExp;
											kindDonorFunds[i][3]=kindDonorFunds[i][3] + totPlannedDisb;
											kindDonorFunds[i][4]=kindDonorFunds[i][4] + (actualCommitment-actualDisbursement);
										}
									}
								}
								for(int i=0;i<yrCount;i++)
								{
									projFunds[i][0]=projFunds[i][0] + termFunds[i][0];
									projFunds[i][1]=projFunds[i][1] + termFunds[i][1];
									projFunds[i][2]=projFunds[i][2] + termFunds[i][2];
									projFunds[i][3]=projFunds[i][3] + termFunds[i][3];
								}
							}
							for(int i=0;i<yrCount;i++)
							{
								AmpFund projFund = new AmpFund();
								projFund.setCommAmount(mf.format(projFunds[i][0])); 
								projFund.setDisbAmount(mf.format(projFunds[i][1]));
								projFund.setExpAmount(mf.format(projFunds[i][2]));
								projFund.setPlannedDisbAmount(mf.format(projFunds[i][3]));
								project.getAmpFund().add(projFund) ;
							}
							project.setProjCommAmount(mf.format(projCommAmount));
							project.setProjDisbAmount(mf.format(projDisbAmount));
							project.setProjExpAmount(mf.format(projExpAmount));
							project.setProjPlannedDisbAmount(mf.format(projPlannedDisbAmount));
							project.setProjUnDisbAmount(mf.format(projUnDisbAmount));
							project.setRowspan(project.getTermAssist().size()+1);
							ampTeamDonors.getProject().add(project);
							for(int i=0;i<yrCount;i++)
							{
								donorFunds[i][0]=donorFunds[i][0] + projFunds[i][0];
								donorFunds[i][1]=donorFunds[i][1] + projFunds[i][1];
								donorFunds[i][2]=donorFunds[i][2] + projFunds[i][2];
								donorFunds[i][3]=donorFunds[i][3] + projFunds[i][3];
							}
							donorCommAmount=donorCommAmount + projCommAmount;
							donorDisbAmount=donorDisbAmount + projDisbAmount;
							donorExpAmount=donorExpAmount + projExpAmount;
							donorPlannedDisbAmount=donorPlannedDisbAmount + projPlannedDisbAmount;
							donorUnDisbAmount = donorUnDisbAmount + projUnDisbAmount;

							for(int i=0;i<yrCount;i++)
							{
								FundTotal total=new FundTotal();
								total.setTotCommAmount(mf.format(donorFunds[i][0]));
								total.setTotDisbAmount(mf.format(donorFunds[i][1]));
								total.setTotExpAmount(mf.format(donorFunds[i][2]));
								total.setTotPlannedDisbAmount(mf.format(donorFunds[i][3]));
								ampTeamDonors.getTotalDonorFund().add(total);	
							}
							ampTeamDonors.setDonorCommAmount(mf.format(donorCommAmount));
							ampTeamDonors.setDonorDisbAmount(mf.format(donorDisbAmount));
							ampTeamDonors.setDonorExpAmount(mf.format(donorExpAmount));
							ampTeamDonors.setDonorPlannedDisbAmount(mf.format(donorPlannedDisbAmount));
							ampTeamDonors.setDonorUnDisbAmount(mf.format(donorUnDisbAmount));

							Iterator termIter=donorAssistance.iterator();
							while(termIter.hasNext())
							{
								String assist=(String) termIter.next();
								logger.debug("Assist:" + assist + ":");
								TermFund termFund=new TermFund();
								if(assist.equals("Loan"))
								{
									termFund.setTermAssistName("Loan");
									termFund.setTermFundTotal(new ArrayList());
									for(int i=0;i<yrCount;i++)
									{
										TermFundTotal termFundTotal=new TermFundTotal();
										termFundTotal.setTotCommAmount(mf.format(loanDonorFunds[i][0]));
										termFundTotal.setTotDisbAmount(mf.format(loanDonorFunds[i][1]));
										termFundTotal.setTotExpAmount(mf.format(loanDonorFunds[i][2]));
										termFundTotal.setTotPlannedDisbAmount(mf.format(loanDonorFunds[i][3]));
										termFund.getTermFundTotal().add(termFundTotal);
										loanTeamFunds[i][0]=loanTeamFunds[i][0] + loanDonorFunds[i][0];
										loanTeamFunds[i][1]=loanTeamFunds[i][1] + loanDonorFunds[i][1];
										loanTeamFunds[i][2]=loanTeamFunds[i][2] + loanDonorFunds[i][2];
										loanTeamFunds[i][3]=loanTeamFunds[i][3] + loanDonorFunds[i][3];
									}
									termFund.setTotDonorCommAmount(mf.format(loanDonorFunds[yrCount][0]));
									termFund.setTotDonorDisbAmount(mf.format(loanDonorFunds[yrCount][1]));
									termFund.setTotDonorExpAmount(mf.format(loanDonorFunds[yrCount][2]));
									termFund.setTotDonorPlannedDisbAmount(mf.format(loanDonorFunds[yrCount][3]));
									termFund.setTotDonorUnDisbAmount(mf.format(loanDonorFunds[yrCount][4]));
									loanTeamFunds[yrCount][0]=loanTeamFunds[yrCount][0] + loanDonorFunds[yrCount][0];
									loanTeamFunds[yrCount][1]=loanTeamFunds[yrCount][1] + loanDonorFunds[yrCount][1];
									loanTeamFunds[yrCount][2]=loanTeamFunds[yrCount][2] + loanDonorFunds[yrCount][2];
									loanTeamFunds[yrCount][3]=loanTeamFunds[yrCount][3] + loanDonorFunds[yrCount][3];
									loanTeamFunds[yrCount][4]=loanTeamFunds[yrCount][4] + loanDonorFunds[yrCount][4];
								}
								if(assist.equals("Grant"))
								{
									logger.debug("Inside Grant");
									logger.debug("Year Count" + yrCount);
									termFund.setTermAssistName("Grant");
									termFund.setTermFundTotal(new ArrayList());
									for(int i=0;i<yrCount;i++)
									{
										TermFundTotal termFundTotal=new TermFundTotal();
										termFundTotal.setTotCommAmount(mf.format(grantDonorFunds[i][0]));
										termFundTotal.setTotDisbAmount(mf.format(grantDonorFunds[i][1]));
										termFundTotal.setTotExpAmount(mf.format(grantDonorFunds[i][2]));
										termFundTotal.setTotPlannedDisbAmount(mf.format(grantDonorFunds[i][3]));
										termFund.getTermFundTotal().add(termFundTotal);
										grantTeamFunds[i][0]=grantTeamFunds[i][0] + grantDonorFunds[i][0];
										grantTeamFunds[i][1]=grantTeamFunds[i][1] + grantDonorFunds[i][1];
										grantTeamFunds[i][2]=grantTeamFunds[i][2] + grantDonorFunds[i][2];
										grantTeamFunds[i][3]=grantTeamFunds[i][3] + grantDonorFunds[i][3];
									}
									termFund.setTotDonorCommAmount(mf.format(grantDonorFunds[yrCount][0]));
									termFund.setTotDonorDisbAmount(mf.format(grantDonorFunds[yrCount][1]));
									termFund.setTotDonorExpAmount(mf.format(grantDonorFunds[yrCount][2]));
									termFund.setTotDonorPlannedDisbAmount(mf.format(grantDonorFunds[yrCount][3]));
									termFund.setTotDonorUnDisbAmount(mf.format(grantDonorFunds[yrCount][4]));
									grantTeamFunds[yrCount][0]=grantTeamFunds[yrCount][0] + grantDonorFunds[yrCount][0];
									grantTeamFunds[yrCount][1]=grantTeamFunds[yrCount][1] + grantDonorFunds[yrCount][1];
									grantTeamFunds[yrCount][2]=grantTeamFunds[yrCount][2] + grantDonorFunds[yrCount][2];
									grantTeamFunds[yrCount][3]=grantTeamFunds[yrCount][3] + grantDonorFunds[yrCount][3];
									grantTeamFunds[yrCount][4]=grantTeamFunds[yrCount][4] + grantDonorFunds[yrCount][4];
								}
								if(assist.equals("In Kind"))
								{
									termFund.setTermAssistName("In Kind");
									termFund.setTermFundTotal(new ArrayList());
									for(int i=0;i<yrCount;i++)
									{
										TermFundTotal termFundTotal=new TermFundTotal();
										termFundTotal.setTotCommAmount(mf.format(kindDonorFunds[i][0]));
										termFundTotal.setTotDisbAmount(mf.format(kindDonorFunds[i][1]));
										termFundTotal.setTotExpAmount(mf.format(kindDonorFunds[i][2]));
										termFundTotal.setTotPlannedDisbAmount(mf.format(kindDonorFunds[i][3]));
										termFund.getTermFundTotal().add(termFundTotal);
										kindTeamFunds[i][0]=kindTeamFunds[i][0] + kindDonorFunds[i][0];
										kindTeamFunds[i][1]=kindTeamFunds[i][1] + kindDonorFunds[i][1];
										kindTeamFunds[i][2]=kindTeamFunds[i][2] + kindDonorFunds[i][2];
										kindTeamFunds[i][3]=kindTeamFunds[i][3] + kindDonorFunds[i][3];
									}
									termFund.setTotDonorCommAmount(mf.format(kindDonorFunds[yrCount][0]));
									termFund.setTotDonorDisbAmount(mf.format(kindDonorFunds[yrCount][1]));
									termFund.setTotDonorExpAmount(mf.format(kindDonorFunds[yrCount][2]));
									termFund.setTotDonorPlannedDisbAmount(mf.format(kindDonorFunds[yrCount][3]));
									termFund.setTotDonorUnDisbAmount(mf.format(kindDonorFunds[yrCount][4]));
									kindTeamFunds[yrCount][0]=kindTeamFunds[yrCount][0] + kindDonorFunds[yrCount][0];
									kindTeamFunds[yrCount][1]=kindTeamFunds[yrCount][1] + kindDonorFunds[yrCount][1];
									kindTeamFunds[yrCount][2]=kindTeamFunds[yrCount][2] + kindDonorFunds[yrCount][2];
									kindTeamFunds[yrCount][3]=kindTeamFunds[yrCount][3] + kindDonorFunds[yrCount][3];
									kindTeamFunds[yrCount][4]=kindTeamFunds[yrCount][4] + kindDonorFunds[yrCount][4];
								}
								logger.debug("Added:'" + termFund.getTermAssistName());
								ampTeamDonors.getTotalDonorTermAssistFund().add(termFund);	
							}
							Iterator teamIter=teamAssistance.iterator();
							while(teamIter.hasNext())
							{
								String assist=(String) teamIter.next();
								logger.debug("Assist:" + assist + ":");
								TermFund termFund=new TermFund();
								if(assist.equals("Loan"))
								{
									termFund.setTermAssistName("Loan");
									termFund.setTermFundTotal(new ArrayList());
									for(int i=0;i<yrCount;i++)
									{
										TermFundTotal termFundTotal=new TermFundTotal();
										termFundTotal.setTotCommAmount(mf.format(loanTeamFunds[i][0]));
										termFundTotal.setTotDisbAmount(mf.format(loanTeamFunds[i][1]));
										termFundTotal.setTotExpAmount(mf.format(loanTeamFunds[i][2]));
										termFundTotal.setTotPlannedDisbAmount(mf.format(loanTeamFunds[i][3]));
										termFund.getTermFundTotal().add(termFundTotal);
									}
									termFund.setTotDonorCommAmount(mf.format(loanTeamFunds[yrCount][0]));
									termFund.setTotDonorDisbAmount(mf.format(loanTeamFunds[yrCount][1]));
									termFund.setTotDonorExpAmount(mf.format(loanTeamFunds[yrCount][2]));
									termFund.setTotDonorPlannedDisbAmount(mf.format(loanTeamFunds[yrCount][3]));
									termFund.setTotDonorUnDisbAmount(mf.format(loanTeamFunds[yrCount][4]));
								}
								if(assist.equals("Grant"))
								{
									termFund.setTermAssistName("Grant");
									termFund.setTermFundTotal(new ArrayList());
									for(int i=0;i<yrCount;i++)
									{
										TermFundTotal termFundTotal=new TermFundTotal();
										termFundTotal.setTotCommAmount(mf.format(grantTeamFunds[i][0]));
										termFundTotal.setTotDisbAmount(mf.format(grantTeamFunds[i][1]));
										termFundTotal.setTotExpAmount(mf.format(grantTeamFunds[i][2]));
										termFundTotal.setTotPlannedDisbAmount(mf.format(grantTeamFunds[i][3]));
										termFund.getTermFundTotal().add(termFundTotal);
									}
									termFund.setTotDonorCommAmount(mf.format(grantTeamFunds[yrCount][0]));
									termFund.setTotDonorDisbAmount(mf.format(grantTeamFunds[yrCount][1]));
									termFund.setTotDonorExpAmount(mf.format(grantTeamFunds[yrCount][2]));
									termFund.setTotDonorPlannedDisbAmount(mf.format(grantTeamFunds[yrCount][3]));
									termFund.setTotDonorUnDisbAmount(mf.format(grantTeamFunds[yrCount][4]));
								}	
								if(assist.equals("In Kind"))
								{
									termFund.setTermAssistName("In Kind");
									termFund.setTermFundTotal(new ArrayList());
									for(int i=0;i<yrCount;i++)
									{
										TermFundTotal termFundTotal=new TermFundTotal();
										termFundTotal.setTotCommAmount(mf.format(kindTeamFunds[i][0]));
										termFundTotal.setTotDisbAmount(mf.format(kindTeamFunds[i][1]));
										termFundTotal.setTotExpAmount(mf.format(kindTeamFunds[i][2]));
										termFundTotal.setTotPlannedDisbAmount(mf.format(kindTeamFunds[i][3]));
										termFund.getTermFundTotal().add(termFundTotal);
									}
									termFund.setTotDonorCommAmount(mf.format(kindTeamFunds[yrCount][0]));
									termFund.setTotDonorDisbAmount(mf.format(kindTeamFunds[yrCount][1]));
									termFund.setTotDonorExpAmount(mf.format(kindTeamFunds[yrCount][2]));
									termFund.setTotDonorPlannedDisbAmount(mf.format(kindTeamFunds[yrCount][3]));
									termFund.setTotDonorUnDisbAmount(mf.format(kindTeamFunds[yrCount][4]));
								}
								logger.debug("Added:'" + termFund.getTermAssistName());
								report.getTotalTeamTermAssistFund().add(termFund);	
							}
//							ampTeamDonors.getTotalDonorTermAssistFund().addAll(donorTotal);
							report.getDonors().add(ampTeamDonors);
							for(int i=0;i<yrCount;i++)
							{
								teamFunds[i][0]=teamFunds[i][0] + donorFunds[i][0];
								teamFunds[i][1]=teamFunds[i][1] + donorFunds[i][1];
								teamFunds[i][2]=teamFunds[i][2] + donorFunds[i][2];
								teamFunds[i][3]=teamFunds[i][3] + donorFunds[i][3];
							}
							teamCommAmount=teamCommAmount + donorCommAmount;
							teamDisbAmount=teamDisbAmount + donorDisbAmount;
							teamExpAmount=teamExpAmount + donorExpAmount;
							teamPlannedDisbAmount=teamPlannedDisbAmount + donorPlannedDisbAmount;
							teamUnDisbAmount=teamUnDisbAmount + donorUnDisbAmount;
							for(int i=0;i<yrCount;i++)
							{
								FundTotal total=new FundTotal();
								total.setTotCommAmount(mf.format(teamFunds[i][0]));
								total.setTotDisbAmount(mf.format(teamFunds[i][1]));
								total.setTotExpAmount(mf.format(teamFunds[i][2]));
								total.setTotPlannedDisbAmount(mf.format(teamFunds[i][3]));
								report.getTotalTeamFund().add(total);	
							}
							report.setTeamCommAmount(mf.format(teamCommAmount));
							report.setTeamDisbAmount(mf.format(teamDisbAmount));
							report.setTeamExpAmount(mf.format(teamExpAmount));
							report.setTeamPlannedDisbAmount(mf.format(teamPlannedDisbAmount));
							report.setTeamUnDisbAmount(mf.format(teamUnDisbAmount));
							projCommAmount=projDisbAmount=projExpAmount=projPlannedDisbAmount=projUnDisbAmount=0;
							donorCommAmount=donorDisbAmount=donorExpAmount=donorPlannedDisbAmount=donorUnDisbAmount=0;
							teamCommAmount=teamDisbAmount=teamExpAmount=teamPlannedDisbAmount=teamUnDisbAmount=0;
							totComm=totDisb=totExp=totPlannedDisb=0;
							actualCommitment=actualDisbursement=0;
							for(int i=0;i<yrCount;i++)
							{
								termFunds[i][0]=termFunds[i][1]=termFunds[i][2]=termFunds[i][3]=0;
								projFunds[i][0]=projFunds[i][1]=projFunds[i][2]=projFunds[i][3]=0;
								donorFunds[i][0]=donorFunds[i][1]=donorFunds[i][2]=donorFunds[i][3]=0;
								teamFunds[i][0]=teamFunds[i][1]=teamFunds[i][2]=teamFunds[i][3]=0;
								loanDonorFunds[i][0]=loanDonorFunds[i][1]=loanDonorFunds[i][2]=loanDonorFunds[i][3]=loanDonorFunds[i][4]=0;
								grantDonorFunds[i][0]=grantDonorFunds[i][1]=grantDonorFunds[i][2]=grantDonorFunds[i][3]=grantDonorFunds[i][4]=0;
								kindDonorFunds[i][0]=kindDonorFunds[i][1]=kindDonorFunds[i][2]=kindDonorFunds[i][3]=kindDonorFunds[i][4]=0;
								loanTeamFunds[i][0]=loanTeamFunds[i][1]=loanTeamFunds[i][2]=loanTeamFunds[i][3]=loanTeamFunds[i][4]=0;
								grantTeamFunds[i][0]=grantTeamFunds[i][1]=grantTeamFunds[i][2]=grantTeamFunds[i][3]=grantTeamFunds[i][4]=0;
								kindTeamFunds[i][0]=kindTeamFunds[i][1]=kindTeamFunds[i][2]=kindTeamFunds[i][3]=kindTeamFunds[i][4]=0;
						}
						loanDonorFunds[yrCount][0]=loanDonorFunds[yrCount][1]=loanDonorFunds[yrCount][2]=loanDonorFunds[yrCount][3]=loanDonorFunds[yrCount][4]=0;
						grantDonorFunds[yrCount][0]=grantDonorFunds[yrCount][1]=grantDonorFunds[yrCount][2]=grantDonorFunds[yrCount][3]=grantDonorFunds[yrCount][4]=0;
						kindDonorFunds[yrCount][0]=kindDonorFunds[yrCount][1]=kindDonorFunds[yrCount][2]=kindDonorFunds[yrCount][3]=kindDonorFunds[yrCount][4]=0;
						loanTeamFunds[yrCount][0]=loanTeamFunds[yrCount][1]=loanTeamFunds[yrCount][2]=loanTeamFunds[yrCount][3]=loanTeamFunds[yrCount][4]=0;
						grantTeamFunds[yrCount][0]=grantTeamFunds[yrCount][1]=grantTeamFunds[yrCount][2]=grantTeamFunds[yrCount][3]=grantTeamFunds[yrCount][4]=0;
						kindTeamFunds[yrCount][0]=kindTeamFunds[yrCount][1]=kindTeamFunds[yrCount][2]=kindTeamFunds[yrCount][3]=kindTeamFunds[yrCount][4]=0;
						projCount=0;
									
						ampTeamDonors=null;
						if(ampDonorId.equals(All))
						{
							logger.debug("Inside Unspecified");
							queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.donorName is null) and (report.ampTeamId='" + selTeamId + "') and (report.reportType='1') order by report.activityName,report.ampActivityId";
							q = session.createQuery(queryString);	
							Iterator iterUn=q.list().iterator();
							if(q.list().size()>0)
							{
								while(iterUn.hasNext())
								{
									AmpReportCache ampCache= (AmpReportCache) iterUn.next();

									if(approvedActivityList.indexOf(ampCache.getAmpActivityId())==-1)
										continue;

									if(!ampModalityId.equals(All))
									{
										if(ampCache.getAmpModalityId()==null)
											continue;
										if(!(ampModalityId.equals(ampCache.getAmpModalityId())))
											continue;
									}

									if(!ampStatusId.equals(All))
									{
										if(ampCache.getAmpStatusId()==null)
											continue;
										if(!(ampStatusId.equals(ampCache.getAmpStatusId())))
											continue;
									}
	
									if(!ampSectorId.equals(All))
									{
										int sflag=0;
										iterSector=DbUtil.getAmpReportSectorId(ampCache.getAmpActivityId()).iterator();
										while(iterSector.hasNext())
										{
											AmpReportSector sector=(AmpReportSector) iterSector.next();
											if(sector.getAmpSectorId().equals(ampSectorId))
											{
												sflag=1;
												break;
											}
											if(sector.getAmpSubSectorId().equals(new Long(0)))
											{
												if(new Long(sector.getSubSectorName()).equals(ampSectorId))
												{
													sflag=1;
													break;
												}
											}
											if(!(sector.getAmpSubSectorId().equals(new Long(0))) && sector.getAmpSubSectorId().equals(ampSectorId))
											{
												sflag=1;
												break;
											}
										}
										if (sflag==0)
										{
											continue;
										}
									}
									if(!region.equals("All"))
									{
										ArrayList location=(ArrayList)DbUtil.getAmpReportLocation(ampCache.getAmpActivityId());
										if(location.indexOf(region)==-1)
											continue;
									}
									if(ampTeamDonors==null)
									{
										ampTeamDonors = new AmpTeamDonors();
										ampTeamDonors.setDonorAgency("Unspecified");
										ampTeamDonors.setDonorCount(++donorCount);
										ampTeamDonors.setProject(new ArrayList());
										ampTeamDonors.setTotalDonorFund(new ArrayList());
									}
									project=new Project();
									project.setName(ampCache.getActivityName());
									project.setAmpActivityId(ampCache.getAmpActivityId());
									project.setCount(++projCount);
									project.setAmpFund(new ArrayList());
									for(int i=0;i<yrCount;i++)
									{
										AmpFund ampFund = new AmpFund();
										ampFund.setCommAmount(mf.format(0)); 
										ampFund.setDisbAmount(mf.format(0));
										ampFund.setExpAmount(mf.format(0));
										ampFund.setPlannedDisbAmount(mf.format(0));
										project.getAmpFund().add(ampFund) ;
									}
									project.setProjCommAmount(mf.format(0));
									project.setProjDisbAmount(mf.format(0));
									project.setProjExpAmount(mf.format(0));
									project.setProjPlannedDisbAmount(mf.format(0));
									project.setProjUnDisbAmount(mf.format(0));
									ampTeamDonors.getProject().add(project);
								}
								if(ampTeamDonors!=null)
								{
									for(int i=0;i<yrCount;i++)
									{
										FundTotal total=new FundTotal();
										total.setTotCommAmount(mf.format(0));
										total.setTotDisbAmount(mf.format(0));
										total.setTotExpAmount(mf.format(0));
										total.setTotPlannedDisbAmount(mf.format(0));
										ampTeamDonors.getTotalDonorFund().add(total);	
									}
									ampTeamDonors.setDonorCommAmount(mf.format(0));
									ampTeamDonors.setDonorDisbAmount(mf.format(0));
									ampTeamDonors.setDonorExpAmount(mf.format(0));
									ampTeamDonors.setDonorPlannedDisbAmount(mf.format(0));
									ampTeamDonors.setDonorUnDisbAmount(mf.format(0));
									report.getDonors().add(ampTeamDonors);
								}
							}
						}
						donorCount=0;
						donorAssistance.clear();
						teamAssistance.clear();
						ampReports.add(report);
						logger.debug("Outside Team");
					}		
					report = new multiReport();
					AmpTeam ampTeam=DbUtil.getAmpTeam(ampReportCache.getAmpTeamId());
					report.setAmpTeamId(ampReportCache.getAmpTeamId());
					selTeamId=ampReportCache.getAmpTeamId();
					report.setTeamName(ampTeam.getName());
					report.setTotalTeamFund(new ArrayList());
					report.setTotalTeamTermAssistFund(new ArrayList());
					report.setDonors(new ArrayList());
					ampTeamDonors=new AmpTeamDonors();
					ampTeamDonors.setDonorAgency(ampReportCache.getDonorName());
					ampTeamDonors.setDonorCount(++donorCount);
					ampTeamDonors.setTotalDonorFund(new ArrayList());
					ampTeamDonors.setTotalDonorTermAssistFund(new ArrayList());
					ampTeamDonors.setProject(new ArrayList());
					project=new Project();
					project.setName(ampReportCache.getActivityName());
					project.setAmpActivityId(ampReportCache.getAmpActivityId());
					project.setCount(++projCount);
					project.setAmpFund(new ArrayList());
					project.setTermAssist(new ArrayList());
					if(ampReportCache.getTermAssistName()!=null)
							termAssistName=ampReportCache.getTermAssistName();
					termFlag=0;
					}

					if(report.getAmpTeamId().equals(ampReportCache.getAmpTeamId()) && !(ampReportCache.getDonorName().equals(ampTeamDonors.getDonorAgency())))
					{
						logger.debug("Inside Donor");
						if(termFlag==1)
						{
							termAssist=new ProjectTermAssist();
							termAssist.setTermAssistName(termAssistName);
							if(donorAssistance.indexOf(termAssist.getTermAssistName())==-1)
							{
								logger.debug("Donor Assistance Added:" + termAssist.getTermAssistName() + ":");
								donorAssistance.add(termAssist.getTermAssistName());
							}
							if(teamAssistance.indexOf(termAssist.getTermAssistName())==-1)
							{
								logger.debug("Team Assistance Added:" + termAssist.getTermAssistName() + ":");
								teamAssistance.add(termAssist.getTermAssistName());
							}
							termAssist.setTermAssistFund(new ArrayList());
							for(int i=0;i<yrCount;i++)
							{
								AmpFund termFund = new AmpFund();
								termFund.setCommAmount(mf.format(termFunds[i][0])); 
								termFund.setDisbAmount(mf.format(termFunds[i][1]));
								termFund.setExpAmount(mf.format(termFunds[i][2]));
								termFund.setExpAmount(mf.format(termFunds[i][3]));
								termAssist.getTermAssistFund().add(termFund);
								totComm=totComm + termFunds[i][0];
								totDisb=totDisb + termFunds[i][1];
								totExp=totExp + termFunds[i][2];
								totPlannedDisb=totPlannedDisb + termFunds[i][3];
							}
							termAssist.setTermCommAmount(mf.format(totComm));
							termAssist.setTermDisbAmount(mf.format(totDisb));
							termAssist.setTermExpAmount(mf.format(totExp));
							termAssist.setTermPlannedDisbAmount(mf.format(totPlannedDisb));
							termAssist.setTermUnDisbAmount(mf.format(actualCommitment - actualDisbursement));

							projCommAmount=projCommAmount + totComm;
							projDisbAmount=projDisbAmount + totDisb;
							projExpAmount=projExpAmount + totExp;
							projPlannedDisbAmount=projPlannedDisbAmount + totPlannedDisb;
							projUnDisbAmount=projUnDisbAmount + (actualCommitment - actualDisbursement);

							project.getTermAssist().add(termAssist);
							for(int i=0;i<=yrCount;i++)
							{
								if(termAssist.getTermAssistName().equals("Loan"))
								{
									if(i<yrCount)
									{
										loanDonorFunds[i][0]=loanDonorFunds[i][0] + termFunds[i][0];
										loanDonorFunds[i][1]=loanDonorFunds[i][1] + termFunds[i][1];
										loanDonorFunds[i][2]=loanDonorFunds[i][2] + termFunds[i][2];
										loanDonorFunds[i][3]=loanDonorFunds[i][3] + termFunds[i][3];
									}
									if(i==yrCount)
									{
										loanDonorFunds[i][0]=loanDonorFunds[i][0] + totComm;
										loanDonorFunds[i][1]=loanDonorFunds[i][1] + totDisb;
										loanDonorFunds[i][2]=loanDonorFunds[i][2] + totExp;
										loanDonorFunds[i][3]=loanDonorFunds[i][3] + totPlannedDisb;
										loanDonorFunds[i][4]=loanDonorFunds[i][4] + (actualCommitment - actualDisbursement);
									}
								}
								if(termAssist.getTermAssistName().equals("Grant"))
								{
									if(i<yrCount)
									{
										grantDonorFunds[i][0]=grantDonorFunds[i][0] + termFunds[i][0];
										grantDonorFunds[i][1]=grantDonorFunds[i][1] + termFunds[i][1];
										grantDonorFunds[i][2]=grantDonorFunds[i][2] + termFunds[i][2];
										grantDonorFunds[i][3]=grantDonorFunds[i][3] + termFunds[i][3];
									}	
									if(i==yrCount)
									{
										grantDonorFunds[i][0]=grantDonorFunds[i][0] + totComm;
										grantDonorFunds[i][1]=grantDonorFunds[i][1] + totDisb;
										grantDonorFunds[i][2]=grantDonorFunds[i][2] + totExp;
										grantDonorFunds[i][3]=grantDonorFunds[i][3] + totPlannedDisb;
										grantDonorFunds[i][4]=grantDonorFunds[i][4] + (actualCommitment - actualDisbursement);
									}
								}
								if(termAssist.getTermAssistName().equals("In Kind"))
								{
									if(i<yrCount)
									{
										kindDonorFunds[i][0]=kindDonorFunds[i][0] + termFunds[i][0];
										kindDonorFunds[i][1]=kindDonorFunds[i][1] + termFunds[i][1];
										kindDonorFunds[i][2]=kindDonorFunds[i][2] + termFunds[i][2];
										kindDonorFunds[i][3]=kindDonorFunds[i][3] + termFunds[i][3];
									}
									if(i==yrCount)
									{
										kindDonorFunds[i][0]=kindDonorFunds[i][0] + totComm;
										kindDonorFunds[i][1]=kindDonorFunds[i][1] + totDisb;
										kindDonorFunds[i][2]=kindDonorFunds[i][2] + totExp;
										kindDonorFunds[i][3]=kindDonorFunds[i][3] + totPlannedDisb;
										kindDonorFunds[i][4]=kindDonorFunds[i][4] + (actualCommitment - actualDisbursement);
									}
								}
							}
							for(int i=0;i<yrCount;i++)
							{
								projFunds[i][0]=projFunds[i][0] + termFunds[i][0];
								projFunds[i][1]=projFunds[i][1] + termFunds[i][1];
								projFunds[i][2]=projFunds[i][2] + termFunds[i][2];
								projFunds[i][3]=projFunds[i][3] + termFunds[i][3];
							}
						}
						for(int i=0;i<yrCount;i++)
						{
							AmpFund projFund = new AmpFund();
							projFund.setCommAmount(mf.format(projFunds[i][0])); 
							projFund.setDisbAmount(mf.format(projFunds[i][1]));
							projFund.setExpAmount(mf.format(projFunds[i][2]));
							projFund.setPlannedDisbAmount(mf.format(projFunds[i][3]));
							project.getAmpFund().add(projFund) ;
						}
						project.setProjCommAmount(mf.format(projCommAmount));
						project.setProjDisbAmount(mf.format(projDisbAmount));
						project.setProjExpAmount(mf.format(projExpAmount));
						project.setProjPlannedDisbAmount(mf.format(projPlannedDisbAmount));
						project.setProjUnDisbAmount(mf.format(projCommAmount - projDisbAmount));
						project.setRowspan(project.getTermAssist().size()+1);
						ampTeamDonors.getProject().add(project);
						for(int i=0;i<yrCount;i++)
						{
							donorFunds[i][0]=donorFunds[i][0] + projFunds[i][0];
							donorFunds[i][1]=donorFunds[i][1] + projFunds[i][1];
							donorFunds[i][2]=donorFunds[i][2] + projFunds[i][2];
							donorFunds[i][3]=donorFunds[i][3] + projFunds[i][3];
						}
						donorCommAmount=donorCommAmount + projCommAmount;
						donorDisbAmount=donorDisbAmount + projDisbAmount;
						donorExpAmount=donorExpAmount + projExpAmount;
						donorPlannedDisbAmount=donorPlannedDisbAmount + projPlannedDisbAmount;
						donorUnDisbAmount=donorUnDisbAmount + projUnDisbAmount;
						for(int i=0;i<yrCount;i++)
						{
							FundTotal total=new FundTotal();
							total.setTotCommAmount(mf.format(donorFunds[i][0]));
							total.setTotDisbAmount(mf.format(donorFunds[i][1]));
							total.setTotExpAmount(mf.format(donorFunds[i][2]));
							total.setTotPlannedDisbAmount(mf.format(donorFunds[i][3]));
							ampTeamDonors.getTotalDonorFund().add(total);	
						}
						ampTeamDonors.setDonorCommAmount(mf.format(donorCommAmount));
						ampTeamDonors.setDonorDisbAmount(mf.format(donorDisbAmount));
						ampTeamDonors.setDonorExpAmount(mf.format(donorExpAmount));
						ampTeamDonors.setDonorPlannedDisbAmount(mf.format(donorPlannedDisbAmount));
						ampTeamDonors.setDonorUnDisbAmount(mf.format(donorUnDisbAmount));
//						ampTeamDonors.getTotalDonorTermAssistFund().addAll(donorTotal);
						for(int i=0;i<yrCount;i++)
						{
							teamFunds[i][0]=teamFunds[i][0] + donorFunds[i][0];
							teamFunds[i][1]=teamFunds[i][1] + donorFunds[i][1];
							teamFunds[i][2]=teamFunds[i][2] + donorFunds[i][2];
							teamFunds[i][3]=teamFunds[i][3] + donorFunds[i][3];
						}
						teamCommAmount=teamCommAmount + donorCommAmount;
						teamDisbAmount=teamDisbAmount + donorDisbAmount;
						teamExpAmount=teamExpAmount + donorExpAmount;
						teamPlannedDisbAmount=teamPlannedDisbAmount + donorPlannedDisbAmount;
						teamUnDisbAmount=teamUnDisbAmount + donorUnDisbAmount;
//						ampTeamDonors.setTotalDonorTermAssistFund(new ArrayList());
//						logger.debug("Size of Assistance:" + assistance.size() + ":");
						Iterator termIter=donorAssistance.iterator();
						while(termIter.hasNext())
						{
							String assist=(String) termIter.next();
							logger.debug("Assist:" + assist + ":");
							TermFund termFund=new TermFund();
							if(assist.equals("Loan"))
							{
								termFund.setTermAssistName("Loan");
								termFund.setTermFundTotal(new ArrayList());
								for(int i=0;i<yrCount;i++)
								{
									TermFundTotal termFundTotal=new TermFundTotal();
									termFundTotal.setTotCommAmount(mf.format(loanDonorFunds[i][0]));
									termFundTotal.setTotDisbAmount(mf.format(loanDonorFunds[i][1]));
									termFundTotal.setTotExpAmount(mf.format(loanDonorFunds[i][2]));
									termFundTotal.setTotPlannedDisbAmount(mf.format(loanDonorFunds[i][3]));
									termFund.getTermFundTotal().add(termFundTotal);
									loanTeamFunds[i][0]=loanTeamFunds[i][0] + loanDonorFunds[i][0];
									loanTeamFunds[i][1]=loanTeamFunds[i][1] + loanDonorFunds[i][1];
									loanTeamFunds[i][2]=loanTeamFunds[i][2] + loanDonorFunds[i][2];
									loanTeamFunds[i][3]=loanTeamFunds[i][3] + loanDonorFunds[i][3];
								}
								termFund.setTotDonorCommAmount(mf.format(loanDonorFunds[yrCount][0]));
								termFund.setTotDonorDisbAmount(mf.format(loanDonorFunds[yrCount][1]));
								termFund.setTotDonorExpAmount(mf.format(loanDonorFunds[yrCount][2]));
								termFund.setTotDonorPlannedDisbAmount(mf.format(loanDonorFunds[yrCount][3]));
								termFund.setTotDonorUnDisbAmount(mf.format(loanDonorFunds[yrCount][4]));
								loanTeamFunds[yrCount][0]=loanTeamFunds[yrCount][0] + loanDonorFunds[yrCount][0];
								loanTeamFunds[yrCount][1]=loanTeamFunds[yrCount][1] + loanDonorFunds[yrCount][1];
								loanTeamFunds[yrCount][2]=loanTeamFunds[yrCount][2] + loanDonorFunds[yrCount][2];
								loanTeamFunds[yrCount][3]=loanTeamFunds[yrCount][3] + loanDonorFunds[yrCount][3];
								loanTeamFunds[yrCount][4]=loanTeamFunds[yrCount][4] + loanDonorFunds[yrCount][4];
							}
							if(assist.equals("Grant"))
							{
								logger.debug("Inside Grant");
								logger.debug("Year Count" + yrCount);
								termFund.setTermAssistName("Grant");
								termFund.setTermFundTotal(new ArrayList());
								for(int i=0;i<yrCount;i++)
								{
									TermFundTotal termFundTotal=new TermFundTotal();
									termFundTotal.setTotCommAmount(mf.format(grantDonorFunds[i][0]));
									termFundTotal.setTotDisbAmount(mf.format(grantDonorFunds[i][1]));
									termFundTotal.setTotExpAmount(mf.format(grantDonorFunds[i][2]));
									termFundTotal.setTotPlannedDisbAmount(mf.format(grantDonorFunds[i][3]));
									termFund.getTermFundTotal().add(termFundTotal);
									grantTeamFunds[i][0]=grantTeamFunds[i][0] + grantDonorFunds[i][0];
									grantTeamFunds[i][1]=grantTeamFunds[i][1] + grantDonorFunds[i][1];
									grantTeamFunds[i][2]=grantTeamFunds[i][2] + grantDonorFunds[i][2];
									grantTeamFunds[i][3]=grantTeamFunds[i][3] + grantDonorFunds[i][3];
								}
								termFund.setTotDonorCommAmount(mf.format(grantDonorFunds[yrCount][0]));
								termFund.setTotDonorDisbAmount(mf.format(grantDonorFunds[yrCount][1]));
								termFund.setTotDonorExpAmount(mf.format(grantDonorFunds[yrCount][2]));
								termFund.setTotDonorPlannedDisbAmount(mf.format(grantDonorFunds[yrCount][3]));
								termFund.setTotDonorUnDisbAmount(mf.format(grantDonorFunds[yrCount][4]));
								grantTeamFunds[yrCount][0]=grantTeamFunds[yrCount][0] + grantDonorFunds[yrCount][0];
								grantTeamFunds[yrCount][1]=grantTeamFunds[yrCount][1] + grantDonorFunds[yrCount][1];
								grantTeamFunds[yrCount][2]=grantTeamFunds[yrCount][2] + grantDonorFunds[yrCount][2];
								grantTeamFunds[yrCount][3]=grantTeamFunds[yrCount][3] + grantDonorFunds[yrCount][3];
								grantTeamFunds[yrCount][4]=grantTeamFunds[yrCount][4] + grantDonorFunds[yrCount][4];
							}
							if(assist.equals("In Kind"))
							{
								termFund.setTermAssistName("In Kind");
								termFund.setTermFundTotal(new ArrayList());
								for(int i=0;i<yrCount;i++)
								{
									TermFundTotal termFundTotal=new TermFundTotal();
									termFundTotal.setTotCommAmount(mf.format(kindDonorFunds[i][0]));
									termFundTotal.setTotDisbAmount(mf.format(kindDonorFunds[i][1]));
									termFundTotal.setTotExpAmount(mf.format(kindDonorFunds[i][2]));
									termFundTotal.setTotPlannedDisbAmount(mf.format(kindDonorFunds[i][3]));
									termFund.getTermFundTotal().add(termFundTotal);
									kindTeamFunds[i][0]=kindTeamFunds[i][0] + kindDonorFunds[i][0];
									kindTeamFunds[i][1]=kindTeamFunds[i][1] + kindDonorFunds[i][1];
									kindTeamFunds[i][2]=kindTeamFunds[i][2] + kindDonorFunds[i][2];
									kindTeamFunds[i][3]=kindTeamFunds[i][3] + kindDonorFunds[i][3];
								}
								termFund.setTotDonorCommAmount(mf.format(kindDonorFunds[yrCount][0]));
								termFund.setTotDonorDisbAmount(mf.format(kindDonorFunds[yrCount][1]));
								termFund.setTotDonorExpAmount(mf.format(kindDonorFunds[yrCount][2]));
								termFund.setTotDonorPlannedDisbAmount(mf.format(kindDonorFunds[yrCount][3]));
								termFund.setTotDonorUnDisbAmount(mf.format(kindDonorFunds[yrCount][4]));
								kindTeamFunds[yrCount][0]=kindTeamFunds[yrCount][0] + kindDonorFunds[yrCount][0];
								kindTeamFunds[yrCount][1]=kindTeamFunds[yrCount][1] + kindDonorFunds[yrCount][1];
								kindTeamFunds[yrCount][2]=kindTeamFunds[yrCount][2] + kindDonorFunds[yrCount][2];
								kindTeamFunds[yrCount][3]=kindTeamFunds[yrCount][3] + kindDonorFunds[yrCount][3];
								kindTeamFunds[yrCount][4]=kindTeamFunds[yrCount][4] + kindDonorFunds[yrCount][4];
							}
							logger.debug("Added:'" + termFund.getTermAssistName());
							ampTeamDonors.getTotalDonorTermAssistFund().add(termFund);	
						}
						report.getDonors().add(ampTeamDonors);
						projCommAmount=projDisbAmount=projExpAmount=projPlannedDisbAmount=projUnDisbAmount=0;
						donorCommAmount=donorDisbAmount=donorExpAmount=donorPlannedDisbAmount=donorUnDisbAmount=0;
						totComm=totDisb=totExp=0;
						actualCommitment=actualDisbursement=0;
						for(int i=0;i<yrCount;i++)
						{
							termFunds[i][0]=termFunds[i][1]=termFunds[i][2]=termFunds[i][3]=0;
							projFunds[i][0]=projFunds[i][1]=projFunds[i][2]=projFunds[i][3]=0;
							donorFunds[i][0]=donorFunds[i][1]=donorFunds[i][2]=donorFunds[i][3]=0;
							loanDonorFunds[i][0]=loanDonorFunds[i][1]=loanDonorFunds[i][2]=loanDonorFunds[i][3]=loanDonorFunds[i][4]=0;
							grantDonorFunds[i][0]=grantDonorFunds[i][1]=grantDonorFunds[i][2]=grantDonorFunds[i][3]=grantDonorFunds[i][4]=0;
							kindDonorFunds[i][0]=kindDonorFunds[i][1]=kindDonorFunds[i][2]=kindDonorFunds[i][3]=kindDonorFunds[i][4]=0;
						}
						loanDonorFunds[yrCount][0]=loanDonorFunds[yrCount][1]=loanDonorFunds[yrCount][2]=loanDonorFunds[yrCount][3]=loanDonorFunds[yrCount][4]=0;
						grantDonorFunds[yrCount][0]=grantDonorFunds[yrCount][1]=grantDonorFunds[yrCount][2]=grantDonorFunds[yrCount][3]=grantDonorFunds[yrCount][4]=0;
						kindDonorFunds[yrCount][0]=kindDonorFunds[yrCount][1]=kindDonorFunds[yrCount][2]=kindDonorFunds[yrCount][3]=kindDonorFunds[yrCount][4]=0;
						projCount=0;
						donorAssistance.clear();
						ampTeamDonors=new AmpTeamDonors();
						ampTeamDonors.setDonorAgency(ampReportCache.getDonorName());
						ampTeamDonors.setDonorCount(++donorCount);
						ampTeamDonors.setTotalDonorFund(new ArrayList());
						ampTeamDonors.setTotalDonorTermAssistFund(new ArrayList());
						ampTeamDonors.setProject(new ArrayList());
						project=new Project();
						project.setName(ampReportCache.getActivityName());
						project.setAmpActivityId(ampReportCache.getAmpActivityId());
						project.setCount(++projCount);
						project.setAmpFund(new ArrayList());
						project.setTermAssist(new ArrayList());
						if(ampReportCache.getTermAssistName()!=null)
							termAssistName=ampReportCache.getTermAssistName();
						termFlag=0;	
						logger.debug("Outside Donor");
					}

					if(report.getAmpTeamId().equals(ampReportCache.getAmpTeamId()) && ampReportCache.getDonorName().equals(ampTeamDonors.getDonorAgency()) && !(ampReportCache.getAmpActivityId().equals(project.getAmpActivityId())))
					{
						logger.debug("Inside Project");
						if(termFlag==1)
						{
							termAssist=new ProjectTermAssist();
							termAssist.setTermAssistName(termAssistName);
							if(donorAssistance.indexOf(termAssist.getTermAssistName())==-1)
							{
								logger.debug("Donor Assistance Added:" + termAssist.getTermAssistName() + ":");
								donorAssistance.add(termAssist.getTermAssistName());
							}
							if(teamAssistance.indexOf(termAssist.getTermAssistName())==-1)
							{
								logger.debug("Team Assistance Added:" + termAssist.getTermAssistName() + ":");
								teamAssistance.add(termAssist.getTermAssistName());
							}
							termAssist.setTermAssistFund(new ArrayList());
							for(int i=0;i<yrCount;i++)
							{
								AmpFund termFund = new AmpFund();
								termFund.setCommAmount(mf.format(termFunds[i][0])); 
								termFund.setDisbAmount(mf.format(termFunds[i][1]));
								termFund.setExpAmount(mf.format(termFunds[i][2]));
								termFund.setPlannedDisbAmount(mf.format(termFunds[i][3]));
								termAssist.getTermAssistFund().add(termFund);
								totComm=totComm + termFunds[i][0];
								totDisb=totDisb + termFunds[i][1];
								totExp=totExp + termFunds[i][2];
								totPlannedDisb=totPlannedDisb + termFunds[i][3];
							}
							termAssist.setTermCommAmount(mf.format(totComm));
							termAssist.setTermDisbAmount(mf.format(totDisb));
							termAssist.setTermExpAmount(mf.format(totExp));
							termAssist.setTermPlannedDisbAmount(mf.format(totPlannedDisb));
							termAssist.setTermUnDisbAmount(mf.format(actualCommitment - actualDisbursement));

							projCommAmount=projCommAmount + totComm;
							projDisbAmount=projDisbAmount + totDisb;
							projExpAmount=projExpAmount + totExp;
							projPlannedDisbAmount=projPlannedDisbAmount + totPlannedDisb;
							projUnDisbAmount=projUnDisbAmount + (actualCommitment-actualDisbursement);

							project.getTermAssist().add(termAssist);
							for(int i=0;i<=yrCount;i++)
							{
								if(termAssist.getTermAssistName().equals("Loan"))
								{
									if(i<yrCount)
									{
										loanDonorFunds[i][0]=loanDonorFunds[i][0] + termFunds[i][0];
										loanDonorFunds[i][1]=loanDonorFunds[i][1] + termFunds[i][1];
										loanDonorFunds[i][2]=loanDonorFunds[i][2] + termFunds[i][2];
										loanDonorFunds[i][3]=loanDonorFunds[i][3] + termFunds[i][3];
									}
									if(i==yrCount)
									{
										loanDonorFunds[i][0]=loanDonorFunds[i][0] + totComm;
										loanDonorFunds[i][1]=loanDonorFunds[i][1] + totDisb;
										loanDonorFunds[i][2]=loanDonorFunds[i][2] + totExp;
										loanDonorFunds[i][3]=loanDonorFunds[i][3] + totPlannedDisb;
										loanDonorFunds[i][4]=loanDonorFunds[i][4] + (actualCommitment - actualDisbursement);
									}
								}
								if(termAssist.getTermAssistName().equals("Grant"))
								{
									if(i<yrCount)
									{
										grantDonorFunds[i][0]=grantDonorFunds[i][0] + termFunds[i][0];
										grantDonorFunds[i][1]=grantDonorFunds[i][1] + termFunds[i][1];
										grantDonorFunds[i][2]=grantDonorFunds[i][2] + termFunds[i][2];
										grantDonorFunds[i][3]=grantDonorFunds[i][3] + termFunds[i][3];
									}	
									if(i==yrCount)
									{
										grantDonorFunds[i][0]=grantDonorFunds[i][0] + totComm;
										grantDonorFunds[i][1]=grantDonorFunds[i][1] + totDisb;
										grantDonorFunds[i][2]=grantDonorFunds[i][2] + totExp;
										grantDonorFunds[i][3]=grantDonorFunds[i][3] + totPlannedDisb;
										grantDonorFunds[i][4]=grantDonorFunds[i][4] + (actualCommitment - actualDisbursement);
									}
								}
								if(termAssist.getTermAssistName().equals("In Kind"))
								{
									if(i<yrCount)
									{
										kindDonorFunds[i][0]=kindDonorFunds[i][0] + termFunds[i][0];
										kindDonorFunds[i][1]=kindDonorFunds[i][1] + termFunds[i][1];
										kindDonorFunds[i][2]=kindDonorFunds[i][2] + termFunds[i][2];
										kindDonorFunds[i][3]=kindDonorFunds[i][3] + termFunds[i][3];
									}
									if(i==yrCount)
									{
										kindDonorFunds[i][0]=kindDonorFunds[i][0] + totComm;
										kindDonorFunds[i][1]=kindDonorFunds[i][1] + totDisb;
										kindDonorFunds[i][2]=kindDonorFunds[i][2] + totExp;
										kindDonorFunds[i][3]=kindDonorFunds[i][3] + totPlannedDisb;
										kindDonorFunds[i][4]=kindDonorFunds[i][4] + (actualCommitment - actualDisbursement);
									}
								}
							}
							for(int i=0;i<yrCount;i++)
							{
								projFunds[i][0]=projFunds[i][0] + termFunds[i][0];
								projFunds[i][1]=projFunds[i][1] + termFunds[i][1];
								projFunds[i][2]=projFunds[i][2] + termFunds[i][2];
								projFunds[i][3]=projFunds[i][3] + termFunds[i][3];
							}
						}
						for(int i=0;i<yrCount;i++)
						{
							AmpFund projFund = new AmpFund();
							projFund.setCommAmount(mf.format(projFunds[i][0])); 
							projFund.setDisbAmount(mf.format(projFunds[i][1]));
							projFund.setExpAmount(mf.format(projFunds[i][2]));
							projFund.setPlannedDisbAmount(mf.format(projFunds[i][3]));
							project.getAmpFund().add(projFund) ;
						}
						project.setProjCommAmount(mf.format(projCommAmount));
						project.setProjDisbAmount(mf.format(projDisbAmount));
						project.setProjExpAmount(mf.format(projExpAmount));
						project.setProjPlannedDisbAmount(mf.format(projPlannedDisbAmount));
						project.setProjUnDisbAmount(mf.format(projUnDisbAmount));

						project.setRowspan(project.getTermAssist().size()+1);
						ampTeamDonors.getProject().add(project);
						for(int i=0;i<yrCount;i++)
						{
							donorFunds[i][0]=donorFunds[i][0] + projFunds[i][0];
							donorFunds[i][1]=donorFunds[i][1] + projFunds[i][1];
							donorFunds[i][2]=donorFunds[i][2] + projFunds[i][2];
							donorFunds[i][3]=donorFunds[i][3] + projFunds[i][3];
						}
						donorCommAmount=donorCommAmount + projCommAmount;
						donorDisbAmount=donorDisbAmount + projDisbAmount;
						donorExpAmount=donorExpAmount + projExpAmount;
						donorPlannedDisbAmount=donorPlannedDisbAmount + projPlannedDisbAmount;
						donorUnDisbAmount=donorUnDisbAmount + projUnDisbAmount;

						projCommAmount=projDisbAmount=projExpAmount=projPlannedDisbAmount=projUnDisbAmount=0;
						totComm=totDisb=totExp=totPlannedDisb=0;
						actualCommitment=actualDisbursement=0.0;
						for(int i=0;i<yrCount;i++)
						{
							termFunds[i][0]=termFunds[i][1]=termFunds[i][2]=termFunds[i][3]=0;
							projFunds[i][0]=projFunds[i][1]=projFunds[i][2]=projFunds[i][3]=0;
						}
						project=new Project();
						project.setName(ampReportCache.getActivityName());
						project.setAmpActivityId(ampReportCache.getAmpActivityId());
						project.setCount(++projCount);
						project.setAmpFund(new ArrayList());
						project.setTermAssist(new ArrayList());
						if(ampReportCache.getTermAssistName()!=null)
							termAssistName=ampReportCache.getTermAssistName();
						termFlag=0;
						logger.debug("Outside Project");
					}
					if(ampReportCache.getTermAssistName()!=null)
					{
						if(report.getAmpTeamId().equals(ampReportCache.getAmpTeamId()) && ampReportCache.getDonorName().equals(ampTeamDonors.getDonorAgency()) && ampReportCache.getAmpActivityId().equals(project.getAmpActivityId()) && !(ampReportCache.getTermAssistName().equals(termAssistName)))
						{
							logger.debug("Inside Terms");
							if(termFlag==1)
							{
								termAssist=new ProjectTermAssist();
								termAssist.setTermAssistName(termAssistName);
								if(donorAssistance.indexOf(termAssist.getTermAssistName())==-1)
								{
									logger.debug("Donor Assistance Added:" + termAssist.getTermAssistName() + ":");
									donorAssistance.add(termAssist.getTermAssistName());
								}
								if(teamAssistance.indexOf(termAssist.getTermAssistName())==-1)
								{
									logger.debug("Team Assistance Added:" + termAssist.getTermAssistName() + ":");
									teamAssistance.add(termAssist.getTermAssistName());
								}
								termAssist.setTermAssistFund(new ArrayList());
								for(int i=0;i<yrCount;i++)
								{
									AmpFund termFund = new AmpFund();
									termFund.setCommAmount(mf.format(termFunds[i][0])); 
									termFund.setDisbAmount(mf.format(termFunds[i][1]));
									termFund.setExpAmount(mf.format(termFunds[i][2]));
									termFund.setPlannedDisbAmount(mf.format(termFunds[i][3]));
									termAssist.getTermAssistFund().add(termFund);
									totComm=totComm + termFunds[i][0];
									totDisb=totDisb + termFunds[i][1];
									totExp=totExp + termFunds[i][2];
									totPlannedDisb=totPlannedDisb + termFunds[i][3];
								}
								termAssist.setTermCommAmount(mf.format(totComm));
								termAssist.setTermDisbAmount(mf.format(totDisb));
								termAssist.setTermExpAmount(mf.format(totExp));
								termAssist.setTermPlannedDisbAmount(mf.format(totPlannedDisb));
								termAssist.setTermUnDisbAmount(mf.format(actualCommitment - actualDisbursement));

								projCommAmount=projCommAmount + totComm;
								projDisbAmount=projDisbAmount + totDisb;
								projExpAmount=projExpAmount + totExp;
								projPlannedDisbAmount=projPlannedDisbAmount + totPlannedDisb;
								projUnDisbAmount=projUnDisbAmount + (actualCommitment - actualDisbursement);

								project.getTermAssist().add(termAssist);
								for(int i=0;i<=yrCount;i++)
								{
									if(termAssist.getTermAssistName().equals("Loan"))
									{
										if(i<yrCount)
										{
											loanDonorFunds[i][0]=loanDonorFunds[i][0] + termFunds[i][0];
											loanDonorFunds[i][1]=loanDonorFunds[i][1] + termFunds[i][1];
											loanDonorFunds[i][2]=loanDonorFunds[i][2] + termFunds[i][2];
											loanDonorFunds[i][3]=loanDonorFunds[i][3] + termFunds[i][3];
										}
										if(i==yrCount)
										{
											loanDonorFunds[i][0]=loanDonorFunds[i][0] + totComm;
											loanDonorFunds[i][1]=loanDonorFunds[i][1] + totDisb;
											loanDonorFunds[i][2]=loanDonorFunds[i][2] + totExp;
											loanDonorFunds[i][3]=loanDonorFunds[i][3] + totPlannedDisb;
											loanDonorFunds[i][4]=loanDonorFunds[i][4] + (actualCommitment - actualDisbursement);
										}
									}
									if(termAssist.getTermAssistName().equals("Grant"))
									{
										if(i<yrCount)
										{
											grantDonorFunds[i][0]=grantDonorFunds[i][0] + termFunds[i][0];
											grantDonorFunds[i][1]=grantDonorFunds[i][1] + termFunds[i][1];
											grantDonorFunds[i][2]=grantDonorFunds[i][2] + termFunds[i][2];
											grantDonorFunds[i][3]=grantDonorFunds[i][3] + termFunds[i][3];
										}	
										if(i==yrCount)
										{
											grantDonorFunds[i][0]=grantDonorFunds[i][0] + totComm;
											grantDonorFunds[i][1]=grantDonorFunds[i][1] + totDisb;
											grantDonorFunds[i][2]=grantDonorFunds[i][2] + totExp;
											grantDonorFunds[i][3]=grantDonorFunds[i][3] + totPlannedDisb;
											grantDonorFunds[i][4]=grantDonorFunds[i][4] + (actualCommitment - actualDisbursement);
										}
									}
									if(termAssist.getTermAssistName().equals("In Kind"))
									{
										if(i<yrCount)
										{
											kindDonorFunds[i][0]=kindDonorFunds[i][0] + termFunds[i][0];
											kindDonorFunds[i][1]=kindDonorFunds[i][1] + termFunds[i][1];
											kindDonorFunds[i][2]=kindDonorFunds[i][2] + termFunds[i][2];
											kindDonorFunds[i][3]=kindDonorFunds[i][3] + termFunds[i][3];
										}
										if(i==yrCount)
										{
											kindDonorFunds[i][0]=kindDonorFunds[i][0] + totComm;
											kindDonorFunds[i][1]=kindDonorFunds[i][1] + totDisb;
											kindDonorFunds[i][2]=kindDonorFunds[i][2] + totExp;
											kindDonorFunds[i][3]=kindDonorFunds[i][3] + totPlannedDisb;
											kindDonorFunds[i][4]=kindDonorFunds[i][4] + (actualCommitment - actualDisbursement);
										}
									}
								}
								for(int i=0;i<yrCount;i++)
								{
									projFunds[i][0]=projFunds[i][0] + termFunds[i][0];
									projFunds[i][1]=projFunds[i][1] + termFunds[i][1];
									projFunds[i][2]=projFunds[i][2] + termFunds[i][2];
									projFunds[i][3]=projFunds[i][3] + termFunds[i][3];
								}
							}	
							termAssistName=ampReportCache.getTermAssistName();
							termFlag=0;
							for(int i=0;i<yrCount;i++)
							termFunds[i][0]=termFunds[i][1]=termFunds[i][2]=termFunds[i][3]=0;
							totComm=totDisb=totExp=totPlannedDisb=0;
							actualCommitment=actualDisbursement=0.0;
							logger.debug("Outside Terms");
						}
					}
										
					if(ampReportCache.getFiscalYear()!=null && ampReportCache.getFiscalQuarter()!=null)
					{
						logger.debug("Inside Fund Calculator");
						if(new Long(fiscalCalId).equals(Constants.GREGORIAN))
						{
							fiscalYear=ampReportCache.getFiscalYear().intValue();
							fiscalQuarter=ampReportCache.getFiscalQuarter().intValue();
						}
						if(ampReportCache.getCurrencyCode().equals("USD"))
							fromExchangeRate=1.0;
						else
							fromExchangeRate=DbUtil.getExchangeRate(ampReportCache.getCurrencyCode(),Constants.PLANNED,ampReportCache.getTransactionDate());
						if(ampCurrencyCode.equals("USD"))
							toExchangeRate=1.0;
						else
							toExchangeRate=DbUtil.getExchangeRate(ampCurrencyCode,Constants.PLANNED,ampReportCache.getTransactionDate());
						if(ampReportCache.getPlannedDisbursement().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
						{
							amount=CurrencyWorker.convert1(ampReportCache.getPlannedDisbursement().doubleValue(),fromExchangeRate,toExchangeRate);
							if(fiscalYear>=fromYr && fiscalYear<=toYr)
							{
								termFlag=1;
								if(termFunds[fiscalYear%fromYr][3]==0)
									termFunds[fiscalYear%fromYr][3]=amount;
								else
								if(termFunds[fiscalYear%fromYr][3]>0)
									termFunds[fiscalYear%fromYr][3]=termFunds[fiscalYear%fromYr][3] + amount;
							}
						}
						logger.debug("Planned Disb: " + amount);
						amount=0.0;
						
						if(ampReportCache.getCurrencyCode().equals("USD"))
							fromExchangeRate=1.0;
						else
							fromExchangeRate=DbUtil.getExchangeRate(ampReportCache.getCurrencyCode(),Constants.ACTUAL,ampReportCache.getTransactionDate());
						if(ampCurrencyCode.equals("USD"))
							toExchangeRate=1.0;
						else
							toExchangeRate=DbUtil.getExchangeRate(ampCurrencyCode,Constants.ACTUAL,ampReportCache.getTransactionDate());
						
						if(ampReportCache.getActualCommitment().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
						{
							termFlag=1;
							amount=CurrencyWorker.convert1(ampReportCache.getActualCommitment().doubleValue(),fromExchangeRate,toExchangeRate);
							actualCommitment=actualCommitment + amount;
							if(fiscalYear>=fromYr && fiscalYear<=toYr)
							{
								if(termFunds[fiscalYear%fromYr][0]==0)
									termFunds[fiscalYear%fromYr][0]=amount;
								else
								if(termFunds[fiscalYear%fromYr][0]>0)
									termFunds[fiscalYear%fromYr][0]=termFunds[fiscalYear%fromYr][0] + amount;
							}
						}
						logger.debug("Actual Comm: " + amount);
						amount=0.0;

						if(ampReportCache.getActualDisbursement().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
						{
							termFlag=1;
							amount=CurrencyWorker.convert1(ampReportCache.getActualDisbursement().doubleValue(),fromExchangeRate,toExchangeRate);
							actualDisbursement=actualDisbursement + amount;
							if(fiscalYear>=fromYr && fiscalYear<=toYr)
							{
								if(termFunds[fiscalYear%fromYr][1]==0)
									termFunds[fiscalYear%fromYr][1]=amount;
								else
								if(termFunds[fiscalYear%fromYr][1]>0)
									termFunds[fiscalYear%fromYr][1]=termFunds[fiscalYear%fromYr][1] + amount;
							}
						}
						logger.debug("Actual Disb: " + amount);
						amount=0.0;
						if(ampReportCache.getActualExpenditure().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
						{
							amount=CurrencyWorker.convert1(ampReportCache.getActualExpenditure().doubleValue(),fromExchangeRate,toExchangeRate);
							if(fiscalYear>=fromYr && fiscalYear<=toYr)
							{
								termFlag=1;
								if(termFunds[fiscalYear%fromYr][2]==0)
									termFunds[fiscalYear%fromYr][2]=amount;
								else
								if(termFunds[fiscalYear%fromYr][2]>0)
									termFunds[fiscalYear%fromYr][2]= termFunds[fiscalYear%fromYr][2] + amount;
							}
						}
						logger.debug("Actual Exp: " + amount);
						amount=0.0;
					}
				}
				if(report!=null)
				{
					if(termFlag==1)
					{
						termAssist=new ProjectTermAssist();
						termAssist.setTermAssistName(termAssistName);
						if(donorAssistance.indexOf(termAssist.getTermAssistName())==-1)
						{
							logger.debug("Donor Assistance Added:" + termAssist.getTermAssistName() + ":");
							donorAssistance.add(termAssist.getTermAssistName());
						}
						if(teamAssistance.indexOf(termAssist.getTermAssistName())==-1)
						{
							logger.debug("Team Assistance Added:" + termAssist.getTermAssistName() + ":");
							teamAssistance.add(termAssist.getTermAssistName());
						}
						termAssist.setTermAssistFund(new ArrayList());
						for(int i=0;i<yrCount;i++)
						{
							AmpFund termFund = new AmpFund();
							termFund.setCommAmount(mf.format(termFunds[i][0])); 
							termFund.setDisbAmount(mf.format(termFunds[i][1]));
							termFund.setExpAmount(mf.format(termFunds[i][2]));
							termFund.setExpAmount(mf.format(termFunds[i][3]));
							termAssist.getTermAssistFund().add(termFund);
							totComm=totComm + termFunds[i][0];
							totDisb=totDisb + termFunds[i][1];
							totExp=totExp + termFunds[i][2];
							totPlannedDisb=totPlannedDisb + termFunds[i][3];
						}
						termAssist.setTermCommAmount(mf.format(totComm));
						termAssist.setTermDisbAmount(mf.format(totDisb));
						termAssist.setTermExpAmount(mf.format(totExp));
						termAssist.setTermPlannedDisbAmount(mf.format(totPlannedDisb));
						termAssist.setTermUnDisbAmount(mf.format(actualCommitment - actualDisbursement));
						projCommAmount=projCommAmount + totComm;
						projDisbAmount=projDisbAmount + totDisb;
						projExpAmount=projExpAmount + totExp;
						projPlannedDisbAmount=projPlannedDisbAmount + totPlannedDisb;
						projUnDisbAmount= projUnDisbAmount + (actualCommitment-actualDisbursement);
						project.getTermAssist().add(termAssist);
						for(int i=0;i<=yrCount;i++)
						{
							if(termAssist.getTermAssistName().equals("Loan"))
							{
								if(i<yrCount)
								{
									loanDonorFunds[i][0]=loanDonorFunds[i][0] + termFunds[i][0];
									loanDonorFunds[i][1]=loanDonorFunds[i][1] + termFunds[i][1];
									loanDonorFunds[i][2]=loanDonorFunds[i][2] + termFunds[i][2];
									loanDonorFunds[i][3]=loanDonorFunds[i][3] + termFunds[i][3];
								}
								if(i==yrCount)
								{
									loanDonorFunds[i][0]=loanDonorFunds[i][0] + totComm;
									loanDonorFunds[i][1]=loanDonorFunds[i][1] + totDisb;
									loanDonorFunds[i][2]=loanDonorFunds[i][2] + totExp;
									loanDonorFunds[i][3]=loanDonorFunds[i][3] + totPlannedDisb;
									loanDonorFunds[i][4]=loanDonorFunds[i][4] + (actualCommitment - actualDisbursement);
								}
							}
							if(termAssist.getTermAssistName().equals("Grant"))
							{
								if(i<yrCount)
								{
									grantDonorFunds[i][0]=grantDonorFunds[i][0] + termFunds[i][0];
									grantDonorFunds[i][1]=grantDonorFunds[i][1] + termFunds[i][1];
									grantDonorFunds[i][2]=grantDonorFunds[i][2] + termFunds[i][2];
									grantDonorFunds[i][3]=grantDonorFunds[i][3] + termFunds[i][3];
								}	
								if(i==yrCount)
								{
									grantDonorFunds[i][0]=grantDonorFunds[i][0] + totComm;
									grantDonorFunds[i][1]=grantDonorFunds[i][1] + totDisb;
									grantDonorFunds[i][2]=grantDonorFunds[i][2] + totExp;
									grantDonorFunds[i][3]=grantDonorFunds[i][3] + totPlannedDisb;
									grantDonorFunds[i][4]=grantDonorFunds[i][4] + (actualCommitment - actualDisbursement);
								}
							}
							if(termAssist.getTermAssistName().equals("In Kind"))
							{
								if(i<yrCount)
								{
									kindDonorFunds[i][0]=kindDonorFunds[i][0] + termFunds[i][0];
									kindDonorFunds[i][1]=kindDonorFunds[i][1] + termFunds[i][1];
									kindDonorFunds[i][2]=kindDonorFunds[i][2] + termFunds[i][2];
									kindDonorFunds[i][3]=kindDonorFunds[i][3] + termFunds[i][3];
								}
								if(i==yrCount)
								{
									kindDonorFunds[i][0]=kindDonorFunds[i][0] + totComm;
									kindDonorFunds[i][1]=kindDonorFunds[i][1] + totDisb;
									kindDonorFunds[i][2]=kindDonorFunds[i][2] + totExp;
									kindDonorFunds[i][3]=kindDonorFunds[i][3] + totPlannedDisb;
									kindDonorFunds[i][4]=kindDonorFunds[i][4] + (actualCommitment - actualDisbursement);
								}
							}
						}
						for(int i=0;i<yrCount;i++)
						{
							projFunds[i][0]=projFunds[i][0] + termFunds[i][0];
							projFunds[i][1]=projFunds[i][1] + termFunds[i][1];
							projFunds[i][2]=projFunds[i][2] + termFunds[i][2];
							projFunds[i][3]=projFunds[i][3] + termFunds[i][3];
						}
					}
					for(int i=0;i<yrCount;i++)
					{
						AmpFund projFund = new AmpFund();
						projFund.setCommAmount(mf.format(projFunds[i][0])); 
						projFund.setDisbAmount(mf.format(projFunds[i][1]));
						projFund.setExpAmount(mf.format(projFunds[i][2]));
						projFund.setExpAmount(mf.format(projFunds[i][3]));
						project.getAmpFund().add(projFund) ;
					}
					project.setProjCommAmount(mf.format(projCommAmount));
					project.setProjDisbAmount(mf.format(projDisbAmount));
					project.setProjExpAmount(mf.format(projExpAmount));
					project.setProjPlannedDisbAmount(mf.format(projPlannedDisbAmount));
					project.setProjUnDisbAmount(mf.format(projUnDisbAmount));
					project.setRowspan(project.getTermAssist().size()+1);
					ampTeamDonors.getProject().add(project);
					for(int i=0;i<yrCount;i++)
					{
						donorFunds[i][0]=donorFunds[i][0] + projFunds[i][0];
						donorFunds[i][1]=donorFunds[i][1] + projFunds[i][1];
						donorFunds[i][2]=donorFunds[i][2] + projFunds[i][2];
						donorFunds[i][3]=donorFunds[i][3] + projFunds[i][3];
					}
					donorCommAmount=donorCommAmount + projCommAmount;
					donorDisbAmount=donorDisbAmount + projDisbAmount;
					donorExpAmount=donorExpAmount + projExpAmount;
					donorPlannedDisbAmount=donorPlannedDisbAmount + projPlannedDisbAmount;
					donorUnDisbAmount=donorUnDisbAmount + projUnDisbAmount;
					for(int i=0;i<yrCount;i++)
					{
						FundTotal total=new FundTotal();
						total.setTotCommAmount(mf.format(donorFunds[i][0]));
						total.setTotDisbAmount(mf.format(donorFunds[i][1]));
						total.setTotExpAmount(mf.format(donorFunds[i][2]));
						total.setTotPlannedDisbAmount(mf.format(donorFunds[i][3]));
						ampTeamDonors.getTotalDonorFund().add(total);	
					}
					ampTeamDonors.setDonorCommAmount(mf.format(donorCommAmount));
					ampTeamDonors.setDonorDisbAmount(mf.format(donorDisbAmount));
					ampTeamDonors.setDonorExpAmount(mf.format(donorExpAmount));
					ampTeamDonors.setDonorPlannedDisbAmount(mf.format(donorPlannedDisbAmount));
					ampTeamDonors.setDonorUnDisbAmount(mf.format(donorUnDisbAmount));
					Iterator termIter=donorAssistance.iterator();
					while(termIter.hasNext())
					{
						String assist=(String) termIter.next();
						logger.debug("Assist:" + assist + ":");
						TermFund termFund=new TermFund();
						if(assist.equals("Loan"))
						{
							termFund.setTermAssistName("Loan");
							termFund.setTermFundTotal(new ArrayList());
							for(int i=0;i<yrCount;i++)
							{
								TermFundTotal termFundTotal=new TermFundTotal();
								termFundTotal.setTotCommAmount(mf.format(loanDonorFunds[i][0]));
								termFundTotal.setTotDisbAmount(mf.format(loanDonorFunds[i][1]));
								termFundTotal.setTotExpAmount(mf.format(loanDonorFunds[i][2]));
								termFundTotal.setTotPlannedDisbAmount(mf.format(loanDonorFunds[i][3]));
								termFund.getTermFundTotal().add(termFundTotal);
								loanTeamFunds[i][0]=loanTeamFunds[i][0] + loanDonorFunds[i][0];
								loanTeamFunds[i][1]=loanTeamFunds[i][1] + loanDonorFunds[i][1];
								loanTeamFunds[i][2]=loanTeamFunds[i][2] + loanDonorFunds[i][2];
								loanTeamFunds[i][3]=loanTeamFunds[i][3] + loanDonorFunds[i][3];
							}
							termFund.setTotDonorCommAmount(mf.format(loanDonorFunds[yrCount][0]));
							termFund.setTotDonorDisbAmount(mf.format(loanDonorFunds[yrCount][1]));
							termFund.setTotDonorExpAmount(mf.format(loanDonorFunds[yrCount][2]));
							termFund.setTotDonorPlannedDisbAmount(mf.format(loanDonorFunds[yrCount][3]));
							termFund.setTotDonorUnDisbAmount(mf.format(loanDonorFunds[yrCount][4]));
							loanTeamFunds[yrCount][0]=loanTeamFunds[yrCount][0] + loanDonorFunds[yrCount][0];
							loanTeamFunds[yrCount][1]=loanTeamFunds[yrCount][1] + loanDonorFunds[yrCount][1];
							loanTeamFunds[yrCount][2]=loanTeamFunds[yrCount][2] + loanDonorFunds[yrCount][2];
							loanTeamFunds[yrCount][3]=loanTeamFunds[yrCount][3] + loanDonorFunds[yrCount][3];
							loanTeamFunds[yrCount][4]=loanTeamFunds[yrCount][4] + loanDonorFunds[yrCount][4];
						}
						if(assist.equals("Grant"))
						{
							logger.debug("Inside Grant");
							logger.debug("Year Count" + yrCount);
							termFund.setTermAssistName("Grant");
							termFund.setTermFundTotal(new ArrayList());
							for(int i=0;i<yrCount;i++)
							{
								TermFundTotal termFundTotal=new TermFundTotal();
								termFundTotal.setTotCommAmount(mf.format(grantDonorFunds[i][0]));
								termFundTotal.setTotDisbAmount(mf.format(grantDonorFunds[i][1]));
								termFundTotal.setTotExpAmount(mf.format(grantDonorFunds[i][2]));
								termFundTotal.setTotPlannedDisbAmount(mf.format(grantDonorFunds[i][3]));
								termFund.getTermFundTotal().add(termFundTotal);
								grantTeamFunds[i][0]=grantTeamFunds[i][0] + grantDonorFunds[i][0];
								grantTeamFunds[i][1]=grantTeamFunds[i][1] + grantDonorFunds[i][1];
								grantTeamFunds[i][2]=grantTeamFunds[i][2] + grantDonorFunds[i][2];
								grantTeamFunds[i][3]=grantTeamFunds[i][3] + grantDonorFunds[i][3];
							}
							termFund.setTotDonorCommAmount(mf.format(grantDonorFunds[yrCount][0]));
							termFund.setTotDonorDisbAmount(mf.format(grantDonorFunds[yrCount][1]));
							termFund.setTotDonorExpAmount(mf.format(grantDonorFunds[yrCount][2]));
							termFund.setTotDonorPlannedDisbAmount(mf.format(grantDonorFunds[yrCount][3]));
							termFund.setTotDonorUnDisbAmount(mf.format(grantDonorFunds[yrCount][4]));
							grantTeamFunds[yrCount][0]=grantTeamFunds[yrCount][0] + grantDonorFunds[yrCount][0];
							grantTeamFunds[yrCount][1]=grantTeamFunds[yrCount][1] + grantDonorFunds[yrCount][1];
							grantTeamFunds[yrCount][2]=grantTeamFunds[yrCount][2] + grantDonorFunds[yrCount][2];
							grantTeamFunds[yrCount][3]=grantTeamFunds[yrCount][3] + grantDonorFunds[yrCount][3];
							grantTeamFunds[yrCount][4]=grantTeamFunds[yrCount][4] + grantDonorFunds[yrCount][4];
						}
						if(assist.equals("In Kind"))
						{
							termFund.setTermAssistName("In Kind");
							termFund.setTermFundTotal(new ArrayList());
							for(int i=0;i<yrCount;i++)
							{
								TermFundTotal termFundTotal=new TermFundTotal();
								termFundTotal.setTotCommAmount(mf.format(kindDonorFunds[i][0]));
								termFundTotal.setTotDisbAmount(mf.format(kindDonorFunds[i][1]));
								termFundTotal.setTotExpAmount(mf.format(kindDonorFunds[i][2]));
								termFundTotal.setTotPlannedDisbAmount(mf.format(kindDonorFunds[i][3]));
								termFund.getTermFundTotal().add(termFundTotal);
								kindTeamFunds[i][0]=kindTeamFunds[i][0] + kindDonorFunds[i][0];
								kindTeamFunds[i][1]=kindTeamFunds[i][1] + kindDonorFunds[i][1];
								kindTeamFunds[i][2]=kindTeamFunds[i][2] + kindDonorFunds[i][2];
								kindTeamFunds[i][3]=kindTeamFunds[i][3] + kindDonorFunds[i][3];
							}
							termFund.setTotDonorCommAmount(mf.format(kindDonorFunds[yrCount][0]));
							termFund.setTotDonorDisbAmount(mf.format(kindDonorFunds[yrCount][1]));
							termFund.setTotDonorExpAmount(mf.format(kindDonorFunds[yrCount][2]));
							termFund.setTotDonorPlannedDisbAmount(mf.format(kindDonorFunds[yrCount][3]));
							termFund.setTotDonorUnDisbAmount(mf.format(kindDonorFunds[yrCount][4]));
							kindTeamFunds[yrCount][0]=kindTeamFunds[yrCount][0] + kindDonorFunds[yrCount][0];
							kindTeamFunds[yrCount][1]=kindTeamFunds[yrCount][1] + kindDonorFunds[yrCount][1];
							kindTeamFunds[yrCount][2]=kindTeamFunds[yrCount][2] + kindDonorFunds[yrCount][2];
							kindTeamFunds[yrCount][3]=kindTeamFunds[yrCount][3] + kindDonorFunds[yrCount][3];
							kindTeamFunds[yrCount][4]=kindTeamFunds[yrCount][4] + kindDonorFunds[yrCount][4];
						}
						logger.debug("Added:'" + termFund.getTermAssistName());
						ampTeamDonors.getTotalDonorTermAssistFund().add(termFund);	
					}
					Iterator teamIter=teamAssistance.iterator();
					while(teamIter.hasNext())
					{
						String assist=(String) teamIter.next();
						logger.debug("Assist:" + assist + ":");
						TermFund termFund=new TermFund();
						if(assist.equals("Loan"))
						{
							termFund.setTermAssistName("Loan");
							termFund.setTermFundTotal(new ArrayList());
							for(int i=0;i<yrCount;i++)
							{
								TermFundTotal termFundTotal=new TermFundTotal();
								termFundTotal.setTotCommAmount(mf.format(loanTeamFunds[i][0]));
								termFundTotal.setTotDisbAmount(mf.format(loanTeamFunds[i][1]));
								termFundTotal.setTotExpAmount(mf.format(loanTeamFunds[i][2]));
								termFundTotal.setTotPlannedDisbAmount(mf.format(loanTeamFunds[i][3]));
								termFund.getTermFundTotal().add(termFundTotal);
							}
							termFund.setTotDonorCommAmount(mf.format(loanTeamFunds[yrCount][0]));
							termFund.setTotDonorDisbAmount(mf.format(loanTeamFunds[yrCount][1]));
							termFund.setTotDonorExpAmount(mf.format(loanTeamFunds[yrCount][2]));
							termFund.setTotDonorPlannedDisbAmount(mf.format(loanTeamFunds[yrCount][3]));
							termFund.setTotDonorUnDisbAmount(mf.format(loanTeamFunds[yrCount][4]));
						}
						if(assist.equals("Grant"))
						{
							termFund.setTermAssistName("Grant");
							termFund.setTermFundTotal(new ArrayList());
							for(int i=0;i<yrCount;i++)
							{
								TermFundTotal termFundTotal=new TermFundTotal();
								termFundTotal.setTotCommAmount(mf.format(grantTeamFunds[i][0]));
								termFundTotal.setTotDisbAmount(mf.format(grantTeamFunds[i][1]));
								termFundTotal.setTotExpAmount(mf.format(grantTeamFunds[i][2]));
								termFundTotal.setTotPlannedDisbAmount(mf.format(grantTeamFunds[i][3]));
								termFund.getTermFundTotal().add(termFundTotal);
							}
							termFund.setTotDonorCommAmount(mf.format(grantTeamFunds[yrCount][0]));
							termFund.setTotDonorDisbAmount(mf.format(grantTeamFunds[yrCount][1]));
							termFund.setTotDonorExpAmount(mf.format(grantTeamFunds[yrCount][2]));
							termFund.setTotDonorPlannedDisbAmount(mf.format(grantTeamFunds[yrCount][3]));
							termFund.setTotDonorUnDisbAmount(mf.format(grantTeamFunds[yrCount][4]));
						}
						if(assist.equals("In Kind"))
						{
							termFund.setTermAssistName("In Kind");
							termFund.setTermFundTotal(new ArrayList());
							for(int i=0;i<yrCount;i++)
							{
								TermFundTotal termFundTotal=new TermFundTotal();
								termFundTotal.setTotCommAmount(mf.format(kindTeamFunds[i][0]));
								termFundTotal.setTotDisbAmount(mf.format(kindTeamFunds[i][1]));
								termFundTotal.setTotExpAmount(mf.format(kindTeamFunds[i][2]));
								termFundTotal.setTotPlannedDisbAmount(mf.format(kindTeamFunds[i][3]));
								termFund.getTermFundTotal().add(termFundTotal);
							}
							termFund.setTotDonorCommAmount(mf.format(kindTeamFunds[yrCount][0]));
							termFund.setTotDonorDisbAmount(mf.format(kindTeamFunds[yrCount][1]));
							termFund.setTotDonorExpAmount(mf.format(kindTeamFunds[yrCount][2]));
							termFund.setTotDonorPlannedDisbAmount(mf.format(kindTeamFunds[yrCount][3]));
							termFund.setTotDonorUnDisbAmount(mf.format(kindTeamFunds[yrCount][4]));
						}
						logger.debug("Added:'" + termFund.getTermAssistName());
						report.getTotalTeamTermAssistFund().add(termFund);	
					}
					report.getDonors().add(ampTeamDonors);
					projCount=0;
					for(int i=0;i<yrCount;i++)
					{
						teamFunds[i][0]=teamFunds[i][0] + donorFunds[i][0];
						teamFunds[i][1]=teamFunds[i][1] + donorFunds[i][1];
						teamFunds[i][2]=teamFunds[i][2] + donorFunds[i][2];
						teamFunds[i][3]=teamFunds[i][3] + donorFunds[i][3];
					}
					teamCommAmount=teamCommAmount + donorCommAmount;
					teamDisbAmount=teamDisbAmount + donorDisbAmount;
					teamExpAmount=teamExpAmount + donorExpAmount;
					teamPlannedDisbAmount=teamPlannedDisbAmount + donorPlannedDisbAmount;
					teamUnDisbAmount=teamUnDisbAmount + donorUnDisbAmount;

					for(int i=0;i<yrCount;i++)
					{
						FundTotal total=new FundTotal();
						total.setTotCommAmount(mf.format(teamFunds[i][0]));
						total.setTotDisbAmount(mf.format(teamFunds[i][1]));
						total.setTotExpAmount(mf.format(teamFunds[i][2]));
						total.setTotPlannedDisbAmount(mf.format(teamFunds[i][3]));
						report.getTotalTeamFund().add(total);	
					}
					report.setTeamCommAmount(mf.format(teamCommAmount));
					report.setTeamDisbAmount(mf.format(teamDisbAmount));
					report.setTeamExpAmount(mf.format(teamExpAmount));
					report.setTeamPlannedDisbAmount(mf.format(teamPlannedDisbAmount));
					report.setTeamUnDisbAmount(mf.format(teamUnDisbAmount));
					ampTeamDonors=null;
					projCount=0;
					if(ampDonorId.equals(All))
					{
						logger.debug("Inside Unspecified");
						queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.donorName is null) and (report.ampTeamId='" + selTeamId + "') and (report.reportType='1') order by report.activityName,report.ampActivityId";
						q = session.createQuery(queryString);	
						Iterator iterUn=q.list().iterator();
						if(q.list().size()>0)
						{
							while(iterUn.hasNext())
							{
								AmpReportCache ampCache= (AmpReportCache) iterUn.next();

								if(approvedActivityList.indexOf(ampCache.getAmpActivityId())==-1)
									continue;

								if(!ampModalityId.equals(All))
								{
									if(ampCache.getAmpModalityId()==null)
										continue;
									if(!(ampModalityId.equals(ampCache.getAmpModalityId())))
										continue;
								}

								if(!ampStatusId.equals(All))
								{
									if(ampCache.getAmpStatusId()==null)
										continue;
									if(!(ampStatusId.equals(ampCache.getAmpStatusId())))
										continue;
								}
								if(!ampSectorId.equals(All))
								{
									int sflag=0;
									iterSector=DbUtil.getAmpReportSectorId(ampCache.getAmpActivityId()).iterator();
									while(iterSector.hasNext())
									{
										AmpReportSector sector=(AmpReportSector) iterSector.next();
										if(sector.getAmpSectorId().equals(ampSectorId))
										{
											sflag=1;
											break;
										}
										if(sector.getAmpSubSectorId().equals(new Long(0)))
										{
											if(new Long(sector.getSubSectorName()).equals(ampSectorId))
											{
												sflag=1;
												break;
											}	
										}
										if(!(sector.getAmpSubSectorId().equals(new Long(0))) && sector.getAmpSubSectorId().equals(ampSectorId))
										{
											sflag=1;
											break;
										}
									}
									if (sflag==0)
									{
										continue;
									}
								}
								if(!region.equals("All"))
								{
									ArrayList location=(ArrayList)DbUtil.getAmpReportLocation(ampCache.getAmpActivityId());
									if(location.indexOf(region)==-1)
										continue;
								}
								if(ampTeamDonors==null)
								{
									ampTeamDonors = new AmpTeamDonors();
									ampTeamDonors.setDonorAgency("Unspecified");
									ampTeamDonors.setDonorCount(++donorCount);
									ampTeamDonors.setProject(new ArrayList());
									ampTeamDonors.setTotalDonorFund(new ArrayList());
								}
								project=new Project();
								project.setName(ampCache.getActivityName());
								project.setAmpActivityId(ampCache.getAmpActivityId());
								project.setCount(++projCount);
								project.setAmpFund(new ArrayList());
								for(int i=0;i<yrCount;i++)
								{
									AmpFund ampFund = new AmpFund();
									ampFund.setCommAmount(mf.format(0)); 
									ampFund.setDisbAmount(mf.format(0));
									ampFund.setExpAmount(mf.format(0));
									ampFund.setPlannedDisbAmount(mf.format(0));
									project.getAmpFund().add(ampFund) ;
								}
								project.setProjCommAmount(mf.format(0));
								project.setProjDisbAmount(mf.format(0));
								project.setProjExpAmount(mf.format(0));
								project.setProjPlannedDisbAmount(mf.format(0));
								project.setProjUnDisbAmount(mf.format(0));
								ampTeamDonors.getProject().add(project);
							}
							if(ampTeamDonors!=null)
							{
								for(int i=0;i<yrCount;i++)
								{
									FundTotal total=new FundTotal();
									total.setTotCommAmount(mf.format(0));
									total.setTotDisbAmount(mf.format(0));
									total.setTotExpAmount(mf.format(0));
									total.setTotPlannedDisbAmount(mf.format(0));
									ampTeamDonors.getTotalDonorFund().add(total);	
								}
								ampTeamDonors.setDonorCommAmount(mf.format(0));
								ampTeamDonors.setDonorDisbAmount(mf.format(0));
								ampTeamDonors.setDonorExpAmount(mf.format(0));
								ampTeamDonors.setDonorPlannedDisbAmount(mf.format(0));
								ampTeamDonors.setDonorUnDisbAmount(mf.format(0));
								report.getDonors().add(ampTeamDonors);
							}
						}
					}
					ampReports.add(report);
						
				}
			}
		}
		catch(Exception ex) 		
		{
			logger.debug("Unable to get report names  from database " + ex.getMessage());
		}
		finally 
		{
			try 
			{
				PersistenceManager.releaseSession(session);
			}
			catch (Exception ex2) 
			{
				logger.debug("releaseSession() failed ");
			}
		}
		return ampReports ;
	}

	public static ArrayList getAmpPhysicalComponentReport(Long ampTeamId,int fromYr,int toYr,String perspective,String ampCurrencyCode,Long ampModalityId,Long ampStatusId,Long ampDonorId,Long ampSectorId,int fiscalCalId,String startDate,String closeDate,String region)
	{
		Session session = null ;
		Query q = null ;
		ArrayList ampReports = new ArrayList() ;
		ArrayList progress = new ArrayList() ;
		double actualCommitment=0.0;
		double actualDisbursement=0.0;	
		double amount=0.0;	
		String queryString = null;
		Iterator iter=null;
		Long All=new Long(0);
		int yrCount = (toYr - fromYr)+1;
		int donorCount=0;
		int projCount=0;
		String inClause=null;
		double toExchangeRate=1.0;
		double fromExchangeRate=0.0;
		int commFlag=0;
		Iterator iterSector=null;
		DecimalFormat mf = new DecimalFormat("###,###,###,###,###") ;
		ArrayList approvedActivityList=new ArrayList();
				
		try
		{
			logger.debug("Modality Id: " + ampModalityId);
			ArrayList dbReturnSet=(ArrayList)DbUtil.getAmpLevel0Teams(ampTeamId);				
			if(dbReturnSet.size()==0)
				inClause= "'" + ampTeamId + "'";
			else
			{
				iter=dbReturnSet.iterator();
				while(iter.hasNext())
				{
					Long teamId= (Long) iter.next();
					if(inClause==null)
						inClause="'" + teamId + "'";
					else
						inClause=inClause + ",'" + teamId + "'";
				}
			}
			logger.debug("Inclause: " + inClause);
			approvedActivityList=DbUtil.getApprovedActivities(inClause);
			session = PersistenceManager.getSession();
			if(startDate==null && closeDate==null)
				queryString = "select report from " + AmpReportCache.class.getName() + " report where report.ampTeamId in(" + inClause + ") and (report.reportType='1') order by report.ampDonorId,report.ampActivityId,report.transactionDate";
			else
				queryString = "select report from " + AmpReportCache.class.getName() + " report where report.ampTeamId in(" + inClause + ") and (report.actualStartDate='" + startDate + "' or report.actualCompletionDate='" + closeDate + "') and (report.reportType='1') order by report.ampDonorId,report.ampActivityId,report.transactionDate";
				
	
			logger.debug("querystring: " + queryString);
			q = session.createQuery(queryString);	
			multiReport report =null;
			AmpTeamDonors ampTeamDonors=null;
			Project project=null;
			if(q!=null)
			{
				iter = q.list().iterator();
				while(iter.hasNext())
				{
					AmpReportCache ampReportCache = (AmpReportCache) iter.next(); 
					
					if(approvedActivityList.indexOf(ampReportCache.getAmpActivityId())==-1)
						continue;

					if(!ampModalityId.equals(All))
					{
						if(ampReportCache.getAmpModalityId()==null)
							continue;
						if(!(ampModalityId.equals(ampReportCache.getAmpModalityId())))
							continue;
					}

					if(!ampStatusId.equals(All))
					{
						if(ampReportCache.getAmpStatusId()==null)
							continue;
						if(!(ampStatusId.equals(ampReportCache.getAmpStatusId())))
							continue;
					}
	
					if(!ampDonorId.equals(All))
					{
						if(ampReportCache.getAmpDonorId()==null)
							continue;
						if(!(ampDonorId.equals(ampReportCache.getAmpDonorId())))
							continue;
					}

					if(!ampSectorId.equals(All))
					{
						int sflag=0;
						iterSector=DbUtil.getAmpReportSectorId(ampReportCache.getAmpActivityId()).iterator();
						while(iterSector.hasNext())
						{
							AmpReportSector sector=(AmpReportSector) iterSector.next();
							if(sector.getAmpSectorId().equals(ampSectorId))
							{
								sflag=1;
								break;
							}
							if(sector.getAmpSubSectorId().equals(new Long(0)))
							{
								if(new Long(sector.getSubSectorName()).equals(ampSectorId))
								{
									sflag=1;
									break;
								}
							}
							if(!(sector.getAmpSubSectorId().equals(new Long(0))) && sector.getAmpSubSectorId().equals(ampSectorId))
							{
								sflag=1;
								break;
							}
						}
						if (sflag==0)
						{
							continue;
						}
					}

										
				//	logger.debug("Report Team Id: " +  report.getAmpTeamId());
					logger.debug("Cache Team Id: " +  ampReportCache.getAmpTeamId());

					if(report==null || !(report.getAmpTeamId().equals(ampReportCache.getAmpTeamId())))
					{
						if(report!=null)
						{
							project.setAcCommitment(mf.format(actualCommitment));
							project.setAcDisbursement(mf.format(actualDisbursement));
							project.setAcUnDisbursement(mf.format(actualCommitment-actualDisbursement));
							ampTeamDonors.getProject().add(project);
							report.getDonors().add(ampTeamDonors);
							ampReports.add(report);
							progress.clear();
							actualCommitment=0.0;
							actualDisbursement=0.0;
							donorCount=0;
							projCount=0;
							commFlag=0;
						}		
						logger.debug("Start init");
						report = new multiReport();
						AmpTeam ampTeam=DbUtil.getAmpTeam(ampReportCache.getAmpTeamId());
						report.setAmpTeamId(ampReportCache.getAmpTeamId());
						report.setTeamName(ampTeam.getName());
						report.setDonors(new ArrayList());
						ampTeamDonors=new AmpTeamDonors();
						if(ampReportCache.getDonorName()==null)
							ampTeamDonors.setDonorAgency("Unspecified");
						else
							ampTeamDonors.setDonorAgency(ampReportCache.getDonorName());
						ampTeamDonors.setDonorCount(++donorCount);
						ampTeamDonors.setProject(new ArrayList());
						if(ampReportCache.getDonorName()!=null)
						{
							project=new Project();
							project.setName(ampReportCache.getActivityName());
							project.setAmpActivityId(ampReportCache.getAmpActivityId());
							project.setCount(++projCount);
							project.setProgress(new ArrayList());
							project.setIssues(new ArrayList());
							project.setMeasures(new ArrayList());
							project.setResponsibleActor(new ArrayList());
							project.setDescription(ampReportCache.getActivityDescription());
							if(ampReportCache.getActivityDescription()!=null)
							{
								Editor ed = org.digijava.module.editor.util.DbUtil.getEditor(ampReportCache.getActivityDescription(),"en");
								if (ed != null && ed.getBody() != null && ed.getBody().trim().length() > 0)
									project.setDescriptionPDFXLS(getDescParsed(ed.getBody()));
								else
									project.setDescriptionPDFXLS(" ");
							}
							else
								project.setDescriptionPDFXLS(" ");

							project.setPlannedCompletionDate(DateConversion.ConvertDateToString(ampReportCache.getPlannedCompletionDate()));
							if(DbUtil.getAmpReportPhysicalPerformance(ampReportCache.getAmpActivityId()) !=null)
								project.getProgress().addAll(DbUtil.getAmpReportPhysicalPerformance(ampReportCache.getAmpActivityId()));
							if(ActivityUtil.getIssues(ampReportCache.getAmpActivityId())!=null)
							{
								ArrayList issues = ActivityUtil.getIssues(ampReportCache.getAmpActivityId());
								Iterator issueIter=issues.iterator();
								while(issueIter.hasNext())
								{
									Issues issue=(Issues) issueIter.next();
									project.getIssues().add(issue.getName());
									Iterator measureIter=issue.getMeasures().iterator();
									while(measureIter.hasNext())
									{
										Measures measure=(Measures) measureIter.next();
										project.getMeasures().add(measure.getName());
										Iterator actorIter=measure.getActors().iterator();
										while(actorIter.hasNext())
										{
											AmpActor actor = (AmpActor) actorIter.next();
											project.getResponsibleActor().add(actor.getName());
										}
									}
								}
							}		

						}
						logger.debug("End init");
					}
					if(ampReportCache.getDonorName()==null)
					{
							project=new Project();
							project.setName(ampReportCache.getActivityName());
							project.setAmpActivityId(ampReportCache.getAmpActivityId());
							project.setCount(++projCount);
							project.setProgress(new ArrayList());
							project.setIssues(new ArrayList());
							project.setMeasures(new ArrayList());
							project.setResponsibleActor(new ArrayList());
							project.setDescription(ampReportCache.getActivityDescription());
							if(ampReportCache.getActivityDescription()!=null)
							{
								Editor ed = org.digijava.module.editor.util.DbUtil.getEditor(ampReportCache.getActivityDescription(),"en");
								if (ed != null && ed.getBody() != null && ed.getBody().trim().length() > 0)
									project.setDescriptionPDFXLS(getDescParsed(ed.getBody()));
								else 
									project.setDescriptionPDFXLS(" ");								
							}
							else
								project.setDescriptionPDFXLS(" ");								

							project.setPlannedCompletionDate(DateConversion.ConvertDateToString(ampReportCache.getPlannedCompletionDate()));
							if(DbUtil.getAmpReportPhysicalPerformance(ampReportCache.getAmpActivityId()) !=null)
								project.getProgress().addAll(DbUtil.getAmpReportPhysicalPerformance(ampReportCache.getAmpActivityId()));
							project.setAcCommitment(mf.format(0));
							project.setAcDisbursement(mf.format(0));
							project.setAcUnDisbursement(mf.format(0));
							if(ActivityUtil.getIssues(ampReportCache.getAmpActivityId())!=null)
							{
								ArrayList issues = ActivityUtil.getIssues(ampReportCache.getAmpActivityId());
								Iterator issueIter=issues.iterator();
								while(issueIter.hasNext())
								{
									Issues issue=(Issues) issueIter.next();
									project.getIssues().add(issue.getName());
									Iterator measureIter=issue.getMeasures().iterator();
									while(measureIter.hasNext())
									{
										Measures measure=(Measures) measureIter.next();
										project.getMeasures().add(measure.getName());
										Iterator actorIter=measure.getActors().iterator();
										while(actorIter.hasNext())
										{
											AmpActor actor = (AmpActor) actorIter.next();
											project.getResponsibleActor().add(actor.getName());
										}
									}
								}
							}
							ampTeamDonors.getProject().add(project);
					}

					if(ampReportCache.getDonorName()!=null)
					{
						if(report.getAmpTeamId().equals(ampReportCache.getAmpTeamId()) && !(ampReportCache.getDonorName().equals(ampTeamDonors.getDonorAgency())))
						{
							logger.debug("Inside Donor");
							if(!(ampTeamDonors.getDonorAgency().equals("Unspecified")))
							{
								project.setAcCommitment(mf.format(actualCommitment));
								project.setAcDisbursement(mf.format(actualDisbursement));
								project.setAcUnDisbursement(mf.format(actualCommitment-actualDisbursement));
								ampTeamDonors.getProject().add(project);
							}
							report.getDonors().add(ampTeamDonors);
							progress.clear();
							actualCommitment=0.0;
							actualDisbursement=0.0;
							projCount=0;
							commFlag=0;
							ampTeamDonors=new AmpTeamDonors();
							ampTeamDonors.setDonorAgency(ampReportCache.getDonorName());
							ampTeamDonors.setDonorCount(++donorCount);
							ampTeamDonors.setProject(new ArrayList());
							project=new Project();
							project.setName(ampReportCache.getActivityName());
							project.setAmpActivityId(ampReportCache.getAmpActivityId());
							project.setCount(++projCount);
							project.setProgress(new ArrayList());
							project.setIssues(new ArrayList());
							project.setMeasures(new ArrayList());
							project.setResponsibleActor(new ArrayList());
							project.setDescription(ampReportCache.getActivityDescription());
							if(ampReportCache.getActivityDescription()!=null)
							{
								Editor ed = org.digijava.module.editor.util.DbUtil.getEditor(ampReportCache.getActivityDescription(),"en");
								if (ed != null && ed.getBody() != null && ed.getBody().trim().length() > 0)
									project.setDescriptionPDFXLS(getDescParsed(ed.getBody()));
								else 
									project.setDescriptionPDFXLS(" ");
							}
							else
								project.setDescriptionPDFXLS(" ");								

							project.setPlannedCompletionDate(DateConversion.ConvertDateToString(ampReportCache.getPlannedCompletionDate()));
							if(DbUtil.getAmpReportPhysicalPerformance(ampReportCache.getAmpActivityId()) !=null)
								project.getProgress().addAll(DbUtil.getAmpReportPhysicalPerformance(ampReportCache.getAmpActivityId()));
							if(ActivityUtil.getIssues(ampReportCache.getAmpActivityId())!=null)
							{
								ArrayList issues = ActivityUtil.getIssues(ampReportCache.getAmpActivityId());
								Iterator issueIter=issues.iterator();
								while(issueIter.hasNext())
								{
									Issues issue=(Issues) issueIter.next();
									project.getIssues().add(issue.getName());
									Iterator measureIter=issue.getMeasures().iterator();
									while(measureIter.hasNext())
									{
										Measures measure=(Measures) measureIter.next();
										project.getMeasures().add(measure.getName());
										Iterator actorIter=measure.getActors().iterator();
										while(actorIter.hasNext())
										{
											AmpActor actor = (AmpActor) actorIter.next();
											project.getResponsibleActor().add(actor.getName());
										}
									}
								}
							}
							logger.debug("Outside Donor");
						}
					}

					if(ampReportCache.getDonorName()!=null)
					{
						if(report.getAmpTeamId().equals(ampReportCache.getAmpTeamId()) && ampReportCache.getDonorName().equals(ampTeamDonors.getDonorAgency()) && !(ampReportCache.getAmpActivityId().equals(project.getAmpActivityId())))
						{
							logger.debug("Inside Project");
							project.setAcCommitment(mf.format(actualCommitment));
							project.setAcDisbursement(mf.format(actualDisbursement));
							project.setAcUnDisbursement(mf.format(actualCommitment-actualDisbursement));
							ampTeamDonors.getProject().add(project);
							progress.clear();
							actualCommitment=0.0;
							actualDisbursement=0.0;
							commFlag=0;
							project=new Project();
							project.setName(ampReportCache.getActivityName());
							project.setAmpActivityId(ampReportCache.getAmpActivityId());
							project.setCount(++projCount);
							project.setProgress(new ArrayList());
							project.setIssues(new ArrayList());
							project.setMeasures(new ArrayList());
							project.setResponsibleActor(new ArrayList());
							project.setDescription(ampReportCache.getActivityDescription());
							if(ampReportCache.getActivityDescription()!=null)
							{
								Editor ed = org.digijava.module.editor.util.DbUtil.getEditor(ampReportCache.getActivityDescription(),"en");
								if (ed != null && ed.getBody() != null && ed.getBody().trim().length() > 0) {
									project.setDescriptionPDFXLS(getDescParsed(ed.getBody()));
								} else {
									project.setDescriptionPDFXLS(" ");	
								}
							}
							else
								project.setDescriptionPDFXLS(" ");								

							project.setPlannedCompletionDate(DateConversion.ConvertDateToString(ampReportCache.getPlannedCompletionDate()));
							if(DbUtil.getAmpReportPhysicalPerformance(ampReportCache.getAmpActivityId()) !=null)
								project.getProgress().addAll(DbUtil.getAmpReportPhysicalPerformance(ampReportCache.getAmpActivityId()));
							if(ActivityUtil.getIssues(ampReportCache.getAmpActivityId())!=null)
							{
								ArrayList issues = ActivityUtil.getIssues(ampReportCache.getAmpActivityId());
								Iterator issueIter=issues.iterator();
								while(issueIter.hasNext())
								{
									Issues issue=(Issues) issueIter.next();
									project.getIssues().add(issue.getName());
									Iterator measureIter=issue.getMeasures().iterator();
									while(measureIter.hasNext())
									{
										Measures measure=(Measures) measureIter.next();
										project.getMeasures().add(measure.getName());
										Iterator actorIter=measure.getActors().iterator();
										while(actorIter.hasNext())
										{
											AmpActor actor = (AmpActor) actorIter.next();
											project.getResponsibleActor().add(actor.getName());
										}
									}
								}
							}
							logger.debug("Outside Project");
						}
					}

					if(ampReportCache.getFiscalYear()!=null && ampReportCache.getFiscalQuarter()!=null)
					{	
						logger.debug("Begin if");
						if(ampReportCache.getCurrencyCode().equals("USD"))
							fromExchangeRate=1.0;
						else
							fromExchangeRate=DbUtil.getExchangeRate(ampReportCache.getCurrencyCode(),Constants.ACTUAL,ampReportCache.getTransactionDate());
						if(ampCurrencyCode.equals("USD"))
							toExchangeRate=1.0;
						else
							toExchangeRate=DbUtil.getExchangeRate(ampCurrencyCode,Constants.ACTUAL,ampReportCache.getTransactionDate());					
					
						if(ampReportCache.getActualCommitment().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
						{
							if(commFlag==0)
							{
								project.setSignatureDate(DateConversion.ConvertDateToString(ampReportCache.getTransactionDate()));
								commFlag=1;
							}
							amount=CurrencyWorker.convert1(ampReportCache.getActualCommitment().doubleValue(),fromExchangeRate,toExchangeRate);
							actualCommitment=actualCommitment + amount;
						}

						amount=0.0;
					
						if(ampReportCache.getActualDisbursement().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
						{
							if(ampReportCache.getFiscalYear().intValue()>=fromYr && ampReportCache.getFiscalYear().intValue()<=toYr)
							{
								amount=CurrencyWorker.convert1(ampReportCache.getActualDisbursement().doubleValue(),fromExchangeRate,toExchangeRate);
								actualDisbursement=actualDisbursement + amount;
							}
						}		
						amount=0.0;
						//logger.debug("End if");
					}		
								
					
				}
				if(report!=null)
				{
					project.setAcCommitment(mf.format(actualCommitment));
					project.setAcDisbursement(mf.format(actualDisbursement));
					project.setAcUnDisbursement(mf.format(actualCommitment-actualDisbursement));
					ampTeamDonors.getProject().add(project);
					report.getDonors().add(ampTeamDonors);
					ampReports.add(report);
				}
			}
		}
		catch(Exception ex) 		
		{
			logger.info("Unable to get report names  from database " + ex.getMessage());
			ex.printStackTrace(System.out);
		}
		finally 
		{
			try 
			{
				PersistenceManager.releaseSession(session);
			}
			catch (Exception ex2) 
			{
				logger.debug("releaseSession() failed ");
			}
		}
		return ampReports ;
	}

//----------To parse the description and display it in the PDF & XLS version of PhysicalComponentReport

	public static String getDescParsed(String str)
	{
		StringBuffer strbuff = new StringBuffer();
		char[] ch = new char[str.length()];
		
		ch = str.toCharArray();

		for(int i=0; i<ch.length; i++)
		{
			if(ch[i] == '<')
			{
				while(ch[i] != '>')
					i++;
			}
			else if(ch[i] == '&')
			{
				if(ch[i+1] == 'n' && ch[i+2] == 'b' && ch[i+3] == 's' && ch[i+4] == 'p' && ch[i+5] == ';')
					i = i+5;
				else
					strbuff.append(ch[i]);
			}
			else
			{
				if(i < ch.length)
					strbuff.append(ch[i]);
			}
		}
		str = new String(strbuff);
		return str;
	}

//----------Advanced Report Function--------------------------------

	public static Collection getColumnList()
	throws java.lang.Exception
	{
		Session session = null;
		session = PersistenceManager.getSession();
		String sqlQuery = "";
		boolean flag =false;
		Iterator iter = null;
		Collection coll = new ArrayList();
		Query query = null;
		AmpColumns ampColumns = new AmpColumns();
		try
		{
			sqlQuery = "select c from "+ AmpColumns.class.getName() + " c";
			query = session.createQuery(sqlQuery);
			if (query != null) 
			{
				iter = query.list().iterator();
				while (iter.hasNext()) 
				{
					ampColumns = (AmpColumns) iter.next();
					coll.add(ampColumns);
				}
				flag = true;
			}
			return coll;
		}
		catch(Exception e)
		{
			System.out.println(" Error in getColumnList()  :  " + e);
		}

		return coll;
			
	}

	public static Collection getMeasureList()
	throws java.lang.Exception
	{
		Session session = null;
		session = PersistenceManager.getSession();
		String sqlQuery = "";
		boolean flag =false;
		Iterator iter = null;
		Collection coll = new ArrayList();
		Query query = null;
		AmpMeasures ampMeasures = new AmpMeasures();
		try
		{
			sqlQuery = "select c from "+ AmpMeasures.class.getName() + " c";
			query = session.createQuery(sqlQuery);
			if (query != null) 
			{
				iter = query.list().iterator();
				while (iter.hasNext()) 
				{
					ampMeasures = (AmpMeasures) iter.next();
					coll.add(ampMeasures);
				}
				flag = true;
			}
			return coll;
		}
		catch(Exception e)
		{
			System.out.println(" Error in getMeasureList()  :  " + e);
		}

		return coll;
			
	}

	public static Collection generateQuery(Collection coll, Long ampTeamId)
	{
		Session session = null;
		Iterator iter = null;
		AmpColumns ampColumns = null;
		String inClause=null;
		Collection columns = new ArrayList();
		DecimalFormat mf = new DecimalFormat("###,###,###,###,###") ;
		Collection result = new ArrayList(1);
		AmpReportCache reportCachePrev = null;
		int j = 0;
		try
		{
			if(coll.size() > 0)
			{
				int ind = 0;
				iter = coll.iterator();
				
				ArrayList dbReturnSet=(ArrayList)DbUtil.getAmpLevel0Teams(ampTeamId);				
				if(dbReturnSet.size()==0)
					inClause= "'" + ampTeamId + "'";
				else
				{
					iter=dbReturnSet.iterator();
					while(iter.hasNext())
					{
						Long teamId= (Long) iter.next();
						if(inClause==null)
							inClause="'" + teamId + "'";
						else
							inClause=inClause + ",'" + teamId + "'";
					}
				}
				logger.debug("Inclause: " + inClause);

				iter = coll.iterator();
				while(iter.hasNext())
				{
					ampColumns = (AmpColumns) iter.next();
					columns.add(ampColumns.getColumnName().toString());
				}
				
				iter = columns.iterator();
				while(iter.hasNext())
				{
					String str = (String) iter.next();
					logger.debug(" >>>> " + str);
				}
				Long lg = new Long("0");
				session = PersistenceManager.getSession();
				String queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.ampTeamId in(" + inClause + ")) ";
				logger.debug( " Query 2 :" + queryString);
				Query query = session.createQuery(queryString);
				iter = query.list().iterator();
				int i = 0, count = 0;
				double actCommit = 0, actDisb = 0, unDisb = 0;

				if(query!=null)
				{
					iter = query.list().iterator();
					while(iter.hasNext())
					{
						AmpReportCache ampReportCache = (AmpReportCache) iter.next();
						
						if(i == 0)
						{
							lg = ampReportCache.getAmpActivityId();
							i = i + 1;
							reportCachePrev = ampReportCache;
							actCommit = ampReportCache.getActualCommitment().doubleValue();
						}
						else
						{
							if(ampReportCache.getAmpActivityId().compareTo(reportCachePrev.getAmpActivityId()) == 0 )
							{
								actCommit = actCommit + ampReportCache.getActualCommitment().doubleValue();
								reportCachePrev = ampReportCache;
							}
							else
							{
								//logger.debug(  reportCachePrev.getActivityName()+ " ::" + reportCachePrev.getAmpActivityId()+ " Not Equak : " + actCommit);
								reportCachePrev = ampReportCache;
								Collection tempColl = new ArrayList();
								
								Report report  = new Report();
								if(columns.contains("commitment") == true)
									report.setAcCommitment(mf.format(actCommit));
								if(columns.contains("level") == true)
									report.setLevel(reportCachePrev.getLevelName());
								if(columns.contains("donor") == true)
									
									report.setDonor(reportCachePrev.getDonorName());
								if(columns.contains("status") == true)
									report.setStatus(reportCachePrev.getStatusName());
								if(columns.contains("title") == true)
									report.setTitle(reportCachePrev.getActivityName());
								if(columns.contains("start date") == true)
									report.setStartDate(DateConversion.ConvertDateToString(ampReportCache.getActualStartDate()));
								if(columns.contains("close date") == true)
									report.setCloseDate(DateConversion.ConvertDateToString(ampReportCache.getActualCompletionDate()));
								if(columns.contains("type of Assistance") == true)
								{
									tempColl.add(reportCachePrev.getTermAssistName());
									report.setAssistance(tempColl);
								}
								if(reportCachePrev.getDonorName() != null)
								{
									if(result.contains(report) == false)
									{
										result.add(report);
									}
								}
								
								actCommit = ampReportCache.getActualCommitment().doubleValue();
							}
						}
					}
				}
				logger.debug( result.size()+ " : Record count : ");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace(System.out);
		}

		finally 
		{
			try 
			{
				PersistenceManager.releaseSession(session);
			}
			catch (Exception ex2) 
			{
				logger.debug("releaseSession() failed ");
			}
		}

		return result;
	}

	public static ReportSelectionCriteria getReportSelectionCriteria(Long ampReportId) 
	{
		Session session = null;
		Query q = null;
		Iterator itr=null;
		Iterator itrColumn=null;
		Iterator itrMeasure=null;
		Iterator itrHierarchy=null;
		ArrayList selection=new ArrayList();
		ArrayList hierarchy=new ArrayList();
		ReportSelectionCriteria rsc = new ReportSelectionCriteria();
		rsc.setColumns(new ArrayList());
		rsc.setHierarchy(new ArrayList());
		Column c=null;
		AmpReports ampReports=null;
		try 
		{
			session = PersistenceManager.getSession();
					
			String queryString = "select ar from "
				+ AmpReports.class.getName()
				+ " ar where (ar.ampReportId='" + ampReportId + "')";

			Query qry = session.createQuery(queryString);
			//logger.debug("Query: " + queryString);
			itr = qry.list().iterator();
			if(itr.hasNext())
			{
				ampReports=(AmpReports) itr.next();
				//logger.debug("Report Name: " + ampReports.getName());
				//logger.debug("Column Size: " + ampReports.getColumns().size());
			//	logger.debug("Report Option: " + ampReports.getOptions());
				itrColumn=ampReports.getColumns().iterator();
				itrMeasure=ampReports.getMeasures().iterator();
				itrHierarchy=ampReports.getHierarchies().iterator();
			}
			while (itrColumn.hasNext()) 
			{
				itrColumn.next();
				c=new Column();
				selection.add(c);
			}
			itrColumn=ampReports.getColumns().iterator();
			while (itrColumn.hasNext()) 
			{
				AmpReportColumn report = (AmpReportColumn) itrColumn.next();
				c=new Column();
				c.setColumnId(report.getColumn().getColumnId());
				c.setColumnName(report.getColumn().getColumnName());
				c.setColumnAlias(report.getColumn().getAliasName());
				selection.set((Integer.parseInt(report.getOrderId())-1),c);
				
			}
			rsc.getColumns().addAll(selection);
			rsc.setMeasures(new ArrayList());
			while (itrMeasure.hasNext())
			{
				AmpMeasures report = (AmpMeasures) itrMeasure.next();
				rsc.getMeasures().add(report.getMeasureId());
			}
			if(ampReports.getHierarchies().size()>0)
			{
				while (itrHierarchy.hasNext()) 
				{
					itrHierarchy.next();
					c=new Column();
					hierarchy.add(c);
				}

				itrHierarchy=ampReports.getHierarchies().iterator();
				while (itrHierarchy.hasNext()) 
				{
					AmpReportHierarchy report = (AmpReportHierarchy) itrHierarchy.next();
					c=new Column();
					c.setColumnId(report.getColumn().getColumnId());
					c.setColumnName(report.getColumn().getColumnName());
					c.setColumnAlias(report.getColumn().getAliasName());
					hierarchy.set((Integer.parseInt(report.getLevelId())-1),c);
				
				}
				rsc.getHierarchy().addAll(hierarchy);
			}
			if(ampReports.getOptions()==null)
				  rsc.setOption(Constants.ANNUAL);
			else
				rsc.setOption(ampReports.getOptions());
			
			//added by Mihai
			rsc.setType(ampReports.getType());
		} 
		catch(Exception e)
		{
			e.printStackTrace(System.out);
		}

		finally 
		{
			try 
			{
				PersistenceManager.releaseSession(session);
			}
			catch (Exception ex2) 
			{
				logger.debug("releaseSession() failed ");
			}
		}
		return rsc;
	}

	public static boolean ignoreHierarchyReportCacheRow(AmpReportCache ampReportCache,AdvancedHierarchyReport ahReport ) {
	if(ampReportCache==null || ahReport==null || ahReport.getLabel()==null || ahReport.getName()==null) return false;
	//holy fix :) - hierarchy should only display funding info connected with its current hierarchical grouping
	if (ampReportCache.getDonorName()!=null)
	if(ahReport.getLabel().trim().equals("Donor") && !ampReportCache.getDonorName().trim().equals(ahReport.getName()))
		return true;
	if (ampReportCache.getRegionName()!=null)
	if(ahReport.getLabel().trim().equals("Region") && !ampReportCache.getRegionName().trim().equals(ahReport.getName()))
		return true;
	if (ampReportCache.getTermAssistName()!=null)
	if(ahReport.getLabel().trim().equals("Type Of Assistance") && !ampReportCache.getTermAssistName().trim().equals(ahReport.getName()))
		return true;
	
	
	return false;
	
	}
	
	public static double setQuarterlyFunding(AmpReportCache ampReportCache, Double value, double fromExchangeRate,
			double toExchangeRate, int fromYr, int toYr, int fiscalQuarter, int fiscalYear, 
			AmpByAssistTypeList[][] terms,
			double[][] funds
			) {
		double amount=0;
		amount=CurrencyWorker.convert1(value.doubleValue(),fromExchangeRate,toExchangeRate);
		AmpByAssistTypeAmount abt=new AmpByAssistTypeAmount(ampReportCache.getTermAssistName(),amount);
		
		if(fiscalYear>=fromYr && fiscalYear<=toYr)
		{
			terms[fiscalYear%fromYr][fiscalQuarter-1].add(abt);
			if (funds[fiscalYear%fromYr][fiscalQuarter-1]==0)
				funds[fiscalYear%fromYr][fiscalQuarter-1]=amount;
			else
				if(funds[fiscalYear%fromYr][fiscalQuarter-1]>0)
					funds[fiscalYear%fromYr][fiscalQuarter-1]=funds[fiscalYear%fromYr][fiscalQuarter-1] + amount;
		}

		return amount;
	}
	
	public static double extractAnnualFunding( AmpReportCache ampReportCache, Double value,
			double  fromExchangeRate, double toExchangeRate, int fromYr, int toYr, int fiscalYear,		 
			AmpByAssistTypeList[][] terms, double[][] funds, int typeIdx ) 
	{
		
		double amount=0.0;
				
		amount=CurrencyWorker.convert1(value.doubleValue(),fromExchangeRate,toExchangeRate);
		AmpByAssistTypeAmount abt=new AmpByAssistTypeAmount(ampReportCache.getTermAssistName(),amount);
		
		
		if(fiscalYear>=fromYr && fiscalYear<=toYr)
		{
			terms[fiscalYear%fromYr][typeIdx].add(abt);
			if(funds[fiscalYear%fromYr][typeIdx]==0) 
				funds[fiscalYear%fromYr][typeIdx]=amount;
			else
				if(funds[fiscalYear%fromYr][typeIdx]>0)
					funds[fiscalYear%fromYr][typeIdx]=funds[fiscalYear%fromYr][typeIdx] + amount;
		}
		
		return amount;

	}
	
	public static ArrayList getAdvancedReport(Long ampTeamId,int fromYr,int toYr,String perspective,String ampCurrencyCode,Long ampModalityId,Long ampStatusId,Long ampDonorId,Long ampSectorId,int fiscalCalId,String startDate,String closeDate,String region,String component,ReportSelectionCriteria rsc)
	{
		Session session = null ;
		Query q = null ;
		ArrayList ampReports = new ArrayList() ;
		ArrayList approvedActivityList=new ArrayList();
		Collection columns=new ArrayList();
		ArrayList hierarchy=new ArrayList();
		ArrayList measures=new ArrayList();
		ArrayList ampActivities=new ArrayList();
		Report reports=null;
		String queryString = null;
		Iterator iter=null;
		Iterator iterColumn=null;
		Long All=new Long(0);
		double totalCommitment=0;
		double totalDisbursement=0;
		int yrCount=(toYr-fromYr) +1;
		ArrayList donors=new ArrayList();
		ArrayList sectors=new ArrayList();
		ArrayList assistance=new ArrayList();
		Set assistanceCopy=new TreeSet();
		ArrayList regions=new ArrayList();
		ArrayList components=new ArrayList();
		ArrayList contacts=new ArrayList();
		ArrayList modality=new ArrayList();
		String level=null;
		String status=null;
		String actualStartDate=null;
		String actualCompletionDate=null;
		double[][] actualFunds=new double[yrCount][3];
		double[][] plannedFunds=new double[yrCount][3];
		double[][] actualCommFunds=new double[yrCount][4];
		double[][] plannedCommFunds=new double[yrCount][4];
		double[][] actualDisbFunds=new double[yrCount][4];
		double[][] plannedDisbFunds=new double[yrCount][4];
		double[][] actualExpFunds=new double[yrCount][4];
		double[][] plannedExpFunds=new double[yrCount][4];
		
		AmpByAssistTypeList[][] actualTerms=new AmpByAssistTypeList[yrCount][3];
		AmpByAssistTypeList[][] plannedTerms=new AmpByAssistTypeList[yrCount][3];
		
		for (int i = 0; i < yrCount; i++) 
			for (int ii = 0; ii < 3; ii++) {
			actualTerms[i][ii]=new AmpByAssistTypeList();			
			plannedTerms[i][ii]=new AmpByAssistTypeList();
			
		}
		
		
		//quarterly stuff
		AmpByAssistTypeList[][] actualCommTerms=new AmpByAssistTypeList[yrCount][4];
		AmpByAssistTypeList[][] plannedCommTerms=new AmpByAssistTypeList[yrCount][4];
		AmpByAssistTypeList[][] actualDisbTerms=new AmpByAssistTypeList[yrCount][4];
		AmpByAssistTypeList[][] plannedDisbTerms=new AmpByAssistTypeList[yrCount][4];
		AmpByAssistTypeList[][] actualExpTerms=new AmpByAssistTypeList[yrCount][4];
		AmpByAssistTypeList[][] plannedExpTerms=new AmpByAssistTypeList[yrCount][4];
		
		for (int i = 0; i < yrCount; i++) 
			for (int ii = 0; ii < 4; ii++) {
				actualCommTerms[i][ii]=new AmpByAssistTypeList();
				plannedCommTerms[i][ii]=new AmpByAssistTypeList();
				actualDisbTerms[i][ii]=new AmpByAssistTypeList();
				plannedDisbTerms[i][ii]=new AmpByAssistTypeList();
				actualExpTerms[i][ii]=new AmpByAssistTypeList();
				plannedExpTerms[i][ii]=new AmpByAssistTypeList();
		}
		
		
		
		
		double[][] subTotActualFunds=new double[yrCount][3];
		double[][] subTotPlannedFunds=new double[yrCount][3];
		
		AmpByAssistTypeList[][] subTotActualTerms=new AmpByAssistTypeList[yrCount][3];
		AmpByAssistTypeList[][] subTotPlannedTerms=new AmpByAssistTypeList[yrCount][3];
		
		for (int i = 0; i < yrCount; i++) 
			for (int ii = 0; ii < 3; ii++) {
				subTotActualTerms[i][ii]=new AmpByAssistTypeList();			
				subTotPlannedTerms[i][ii]=new AmpByAssistTypeList();
			
		}
		
		
		
		
		double[][] subTotActualCommFunds=new double[yrCount][4];
		double[][] subTotPlannedCommFunds=new double[yrCount][4];
		double[][] subTotActualDisbFunds=new double[yrCount][4];
		double[][] subTotPlannedDisbFunds=new double[yrCount][4];
		double[][] subTotActualExpFunds=new double[yrCount][4];
		double[][] subTotPlannedExpFunds=new double[yrCount][4];


		
		//quarterly stuff
		AmpByAssistTypeList[][] subTotActualCommTerms=new AmpByAssistTypeList[yrCount][4];
		AmpByAssistTypeList[][] subTotPlannedCommTerms=new AmpByAssistTypeList[yrCount][4];
		AmpByAssistTypeList[][] subTotActualDisbTerms=new AmpByAssistTypeList[yrCount][4];
		AmpByAssistTypeList[][] subTotPlannedDisbTerms=new AmpByAssistTypeList[yrCount][4];
		AmpByAssistTypeList[][] subTotActualExpTerms=new AmpByAssistTypeList[yrCount][4];
		AmpByAssistTypeList[][] subTotPlannedExpTerms=new AmpByAssistTypeList[yrCount][4];
		
		for (int i = 0; i < yrCount; i++) 
			for (int ii = 0; ii < 4; ii++) {
				subTotActualCommTerms[i][ii]=new AmpByAssistTypeList();
				subTotPlannedCommTerms[i][ii]=new AmpByAssistTypeList();
				subTotActualDisbTerms[i][ii]=new AmpByAssistTypeList();
				subTotPlannedDisbTerms[i][ii]=new AmpByAssistTypeList();
				subTotActualExpTerms[i][ii]=new AmpByAssistTypeList();
				subTotPlannedExpTerms[i][ii]=new AmpByAssistTypeList();
		}
		

		
		
		
		double[][] subSubTotActualFunds=new double[yrCount][3];
		double[][] subSubTotPlannedFunds=new double[yrCount][3];
		
		AmpByAssistTypeList[][] subSubTotActualTerms=new AmpByAssistTypeList[yrCount][3];
		AmpByAssistTypeList[][] subSubTotPlannedTerms=new AmpByAssistTypeList[yrCount][3];
		
		for (int i = 0; i < yrCount; i++) 
			for (int ii = 0; ii < 3; ii++) {
				subSubTotActualTerms[i][ii]=new AmpByAssistTypeList();			
				subSubTotPlannedTerms[i][ii]=new AmpByAssistTypeList();
			
		}
		
		
		
		double[][] subSubTotActualCommFunds=new double[yrCount][4];
		double[][] subSubTotPlannedCommFunds=new double[yrCount][4];
		double[][] subSubTotActualDisbFunds=new double[yrCount][4];
		double[][] subSubTotPlannedDisbFunds=new double[yrCount][4];
		double[][] subSubTotActualExpFunds=new double[yrCount][4];
		double[][] subSubTotPlannedExpFunds=new double[yrCount][4];
	
		//quarterly stuff
		AmpByAssistTypeList[][] subSubTotActualCommTerms=new AmpByAssistTypeList[yrCount][4];
		AmpByAssistTypeList[][] subSubTotPlannedCommTerms=new AmpByAssistTypeList[yrCount][4];
		AmpByAssistTypeList[][] subSubTotActualDisbTerms=new AmpByAssistTypeList[yrCount][4];
		AmpByAssistTypeList[][] subSubTotPlannedDisbTerms=new AmpByAssistTypeList[yrCount][4];
		AmpByAssistTypeList[][] subSubTotActualExpTerms=new AmpByAssistTypeList[yrCount][4];
		AmpByAssistTypeList[][] subSubTotPlannedExpTerms=new AmpByAssistTypeList[yrCount][4];
			
		
		for (int i = 0; i < yrCount; i++) 
			for (int ii = 0; ii < 4; ii++) {
				subSubTotActualCommTerms[i][ii]=new AmpByAssistTypeList();
				subSubTotPlannedCommTerms[i][ii]=new AmpByAssistTypeList();
				subSubTotActualDisbTerms[i][ii]=new AmpByAssistTypeList();
				subSubTotPlannedDisbTerms[i][ii]=new AmpByAssistTypeList();
				subSubTotActualExpTerms[i][ii]=new AmpByAssistTypeList();
				subSubTotPlannedExpTerms[i][ii]=new AmpByAssistTypeList();
		}
		
		double[][] totActualFunds=new double[yrCount][3];
		double[][] totPlannedFunds=new double[yrCount][3];
		double[][] totActualCommFunds=new double[yrCount][4];
		double[][] totPlannedCommFunds=new double[yrCount][4];
		double[][] totActualDisbFunds=new double[yrCount][4];
		double[][] totPlannedDisbFunds=new double[yrCount][4];
		double[][] totActualExpFunds=new double[yrCount][4];
		double[][] totPlannedExpFunds=new double[yrCount][4];
		
		
		
		String inClause=null;
		String orderClause=null;
		String title=null;
		String ampId=null;
		String contactName=null;
		double toExchangeRate=1.0;
		double fromExchangeRate=0.0;
		double amount=0.0;
		double sumUnDisb = 0, actSumCommit = 0, actSumDisb = 0, actSumExp = 0, planSumCommit = 0, planSumDisb = 0, planSumExp = 0;
		double unDisbSubTotal = 0, actCommitSubTotal = 0, actDisbSubTotal = 0, actExpSubTotal = 0, planCommitSubTotal = 0, planDisbSubTotal = 0, planExpSubTotal = 0;
		double unDisbSubSubTotal = 0, actCommitSubSubTotal = 0, actDisbSubSubTotal = 0, actExpSubSubTotal = 0, planCommitSubSubTotal = 0, planDisbSubSubTotal = 0, planExpSubSubTotal = 0;
		double unDisbTotal = 0, actCommitTotal = 0, actDisbTotal = 0, actExpTotal = 0, planCommitTotal = 0, planDisbTotal = 0, planExpTotal = 0;
		
		AmpByAssistTypeList actSumCommitTerms=new AmpByAssistTypeList();
		AmpByAssistTypeList actSumDisbTerms=new AmpByAssistTypeList();
		AmpByAssistTypeList actSumExpTerms=new AmpByAssistTypeList();
		AmpByAssistTypeList planSumCommitTerms=new AmpByAssistTypeList();
		AmpByAssistTypeList planSumDisbTerms=new AmpByAssistTypeList();
		AmpByAssistTypeList planSumExpTerms=new AmpByAssistTypeList();
		AmpByAssistTypeList sumUnDisbTerms=new AmpByAssistTypeList();
		
		AmpByAssistTypeList actCommitSubTotalTerms=new AmpByAssistTypeList();
		AmpByAssistTypeList actDisbSubTotalTerms=new AmpByAssistTypeList();
		AmpByAssistTypeList actExpSubTotalTerms=new AmpByAssistTypeList();
		AmpByAssistTypeList planCommitSubTotalTerms=new AmpByAssistTypeList();
		AmpByAssistTypeList planDisbSubTotalTerms=new AmpByAssistTypeList();
		AmpByAssistTypeList planExpSubTotalTerms=new AmpByAssistTypeList();
		AmpByAssistTypeList UnDisbSubTotalTerms=new AmpByAssistTypeList();
		
		AmpByAssistTypeList actCommitSubSubTotalTerms=new AmpByAssistTypeList();
		AmpByAssistTypeList actDisbSubSubTotalTerms=new AmpByAssistTypeList();
		AmpByAssistTypeList actExpSubSubTotalTerms=new AmpByAssistTypeList();
		AmpByAssistTypeList planCommitSubSubTotalTerms=new AmpByAssistTypeList();
		AmpByAssistTypeList planDisbSubSubTotalTerms=new AmpByAssistTypeList();
		AmpByAssistTypeList planExpSubSubTotalTerms=new AmpByAssistTypeList();
		AmpByAssistTypeList UnDisbSubSubTotalTerms=new AmpByAssistTypeList();
		
		int fiscalYear=0,fiscalQuarter=0;
		int maxYear=0;
		int minYear=0;
		
		
		String objective=null;
		String description=null;
		Iterator iterSector=null;
		ArrayList activityIds=new ArrayList();
		Iterator iterLevel1=null;
		Iterator iterLevel2=null;
		Iterator iterLevel3=null;
		DecimalFormat mf = new DecimalFormat("###,###,###,###,###") ;
		AdvancedReport report=null;
		AdvancedHierarchyReport ahReport=null;
		AdvancedHierarchyReport ahTempLevel2=null;
		AdvancedHierarchyReport ahTempLevel3=null;
		multiReport mreport=null;
		AmpReportCache ampReportCache = null;
		try
		{
			ArrayList dbReturnSet=(ArrayList)DbUtil.getAmpLevel0Teams(ampTeamId);				
			if(dbReturnSet.size()==0)
				inClause= "'" + ampTeamId + "'";
			else
			{
				iter=dbReturnSet.iterator();
				while(iter.hasNext())
				{
					Long teamId= (Long) iter.next();
					if(inClause==null)
						inClause="'" + teamId + "'";
					else
						inClause=inClause + ",'" + teamId + "'";
				}
			}

//			ReportSelectionCriteria rsc=ReportUtil.getReportSelectionCriteria(ampReportId);
			columns=rsc.getColumns();
			hierarchy=(ArrayList)rsc.getHierarchy();
			measures=(ArrayList)rsc.getMeasures();
			iter=columns.iterator();
			while(iter.hasNext())
			{
				Column c=(Column) iter.next();
				if(c.getColumnAlias()!=null)
				{	
					if(orderClause==null)
						orderClause= "report.activityName,report.ampActivityId";
					if(!(c.getColumnId().equals(Constants.ACTIVITY_NAME)))
						orderClause= orderClause + ",report." + c.getColumnAlias();
				}
			}
//			currencies=DbUtil.getAmpCurrencyRate();
			//logger.debug("Inclause: " + inClause);
			approvedActivityList=DbUtil.getApprovedActivities(inClause);
			session = PersistenceManager.getSession();
			if(startDate==null && closeDate==null)
				queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.ampTeamId in(" + inClause + ")) and (report.reportType='"+Long.toString(rsc.getType().longValue())+"') order by " + orderClause;
			else
				queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.ampTeamId in(" + inClause + ")) and (report.actualStartDate='" + startDate + "' or report.actualCompletionDate='" + closeDate + "') and (report.reportType='"+Long.toString(rsc.getType().longValue())+"') order by " + orderClause;
			//logger.debug("querystring: " + queryString);
			q = session.createQuery(queryString);	
			//logger.debug("Query Result: " + q.list().size());
			ampActivities=(ArrayList)q.list();
			iter=ampActivities.iterator();
	//		logger.info("Fiscal id: " + fiscalCalId);
			while(iter.hasNext())
			{
				ampReportCache = (AmpReportCache) iter.next(); 

				if(approvedActivityList.indexOf(ampReportCache.getAmpActivityId())==-1)
					continue;
				
				if(!ampModalityId.equals(All))
				{
					if(ampReportCache.getAmpModalityId()==null)
					{
						iter.remove();
						continue;
					}
					if(!(ampModalityId.equals(ampReportCache.getAmpModalityId())))
					{
						iter.remove();
						continue;
					}
				}
				if(!ampStatusId.equals(All))
				{
					if(ampReportCache.getAmpStatusId()==null)
					{
						iter.remove();
						continue;
					}
					if(!(ampStatusId.equals(ampReportCache.getAmpStatusId())))
					{
						iter.remove();
						continue;
					}
				}
	
				if(!ampDonorId.equals(All))
				{
					if(ampReportCache.getAmpDonorId()==null)
					{
						iter.remove();
						continue;
					}
					if(!(ampDonorId.equals(ampReportCache.getAmpDonorId())))
					{
						iter.remove();
						continue;
					}
				}
			
				if(!ampSectorId.equals(All))
				{
					int sflag=0;
					iterSector=DbUtil.getAmpReportSectorId(ampReportCache.getAmpActivityId()).iterator();
					while(iterSector.hasNext())
					{
						AmpReportSector sector=(AmpReportSector) iterSector.next();
						if(sector.getAmpSectorId().equals(ampSectorId))
						{
							sflag=1;
							break;
						}
						if(sector.getAmpSubSectorId().equals(new Long(0)))
						{
							if(new Long(sector.getSubSectorName()).equals(ampSectorId))
							{
								sflag=1;
								break;
							}
						}
						if(!(sector.getAmpSubSectorId().equals(new Long(0))) && sector.getAmpSubSectorId().equals(ampSectorId))
						{
							sflag=1;
							break;
						}
					}
					if (sflag==0)
					{
						iter.remove();
						continue;
					}
				}
					
				if(!region.equals("All"))
				{
					ArrayList location=(ArrayList)DbUtil.getAmpReportLocation(ampReportCache.getAmpActivityId());
					if(location.indexOf(region)==-1)
					{
						iter.remove();
						continue;
					}
				}
			}

//			q.setParameter("ampTeamId",ampTeamId,Hibernate.LONG) ;
			
	//		report.setProjects(new ArrayList());
	//		Project project=null;
			if(ampActivities!=null && rsc.getHierarchy().size()==0)
			{
				iter = ampActivities.iterator();
				while(iter.hasNext())
				{
					ampReportCache = (AmpReportCache) iter.next(); 
								
					if(reports==null || !(reports.getAmpActivityId().equals(ampReportCache.getAmpActivityId())))
					{
						if(reports!=null)
						{
							iterColumn=columns.iterator();
							while(iterColumn.hasNext())
							{
								//logger.debug("begin while");
								report=new AdvancedReport();
								report.setDonors(new ArrayList());
								report.setSectors(new ArrayList());
								report.setRegions(new ArrayList());
								report.setComponents(new ArrayList());
								report.setAssistance(new ArrayList());
								report.setModality(new ArrayList());
								report.setContacts(new ArrayList());
								
								Column c=(Column) iterColumn.next();
								if(c.getColumnId().equals(Constants.STATUS_NAME))
									report.setStatus(status);
								if(c.getColumnId().equals(Constants.DONOR_NAME))
								{
									if(donors.size()==0)
										donors.add(" ");
									report.getDonors().addAll(donors);
								}
								if(c.getColumnId().equals(Constants.ACTUAL_START_DATE))
								report.setActualStartDate(actualStartDate);
								if(c.getColumnId().equals(Constants.ACTIVITY_NAME))
									report.setTitle(title);
								if(c.getColumnId().equals(Constants.COMPONENT_NAME))
									report.getComponents().addAll(components);
								
								
								
								if(c.getColumnId().equals(Constants.TERM_ASSIST_NAME))
								{
									if(assistance.size()==0)
										assistance.add(" ");
									report.getAssistance().addAll(assistance);
								}
								if(c.getColumnId().equals(Constants.LEVEL_NAME))
									report.setLevel(level);
								if(c.getColumnId().equals(Constants.ACTUAL_COMPLETION_DATE))
									report.setActualCompletionDate(actualCompletionDate);
								if(c.getColumnId().equals(Constants.SECTOR_NAME))
									report.getSectors().addAll(sectors);
								if(c.getColumnId().equals(Constants.REGION_NAME))
									report.getRegions().addAll(regions);
								if(c.getColumnId().equals(Constants.AMP_ID)){
									//report.setAmpId(ampId);
									//logger.info("test AMPID333.+calling pidddddddddd"+ " for:::"+ reports.getAmpActivityId());
									report.setProjId(getProjectId(reports.getAmpActivityId()));
								}
								if(c.getColumnId().equals(Constants.FUNDING_INSTRUMENT))
								{
									if(modality.size()==0)
										modality.add(" ");
									report.getModality().addAll(modality);
								}
								if(c.getColumnId().equals(Constants.CONTACT_NAME))
								{
									if(contacts.size()==0)
										contacts.add(" ");
									report.getContacts().addAll(contacts);
								}
								if(c.getColumnId().equals(Constants.OBJECTIVE))
								{
									report.setObjective(objective);
									if(report.getObjective()!=null)
									{
										Editor ed = org.digijava.module.editor.util.DbUtil.getEditor(objective,"en");
										report.setObjectivePDFXLS(getDescParsed(ed.getBody()));
										logger.debug("the report.getObjectivePDFXLS yields....: "+report.getObjectivePDFXLS());
									}
									else
										report.setObjectivePDFXLS(" ");
								}
								if(c.getColumnId().equals(Constants.DESCRIPTION))
								{
									report.setDescription(description);
									if(report.getDescription()!=null)
									{
										Editor ed = org.digijava.module.editor.util.DbUtil.getEditor(description,"en");
										report.setDescriptionPDFXLS(getDescParsed(ed.getBody()));
										logger.debug("the report.getDescriptionPDFXLS yields....: "+report.getDescriptionPDFXLS());
									}
									else
										report.setDescriptionPDFXLS(" ");
								}
								if(c.getColumnId().equals(Constants.TOTAL_COMMITMENT))
									report.setTotalCommitment(mf.format(totalCommitment));
								if(c.getColumnId().equals(Constants.TOTAL_DISBURSEMENT))
									report.setTotalDisbursement(mf.format(totalDisbursement));
								reports.getRecords().add(report);

							}
							//logger.debug("Reports Size: " + reports.getRecords().size());
							report=new AdvancedReport();
							report.setAmpFund(new ArrayList());
							report.setAssistanceCopy(new ArrayList());
							report.getAssistanceCopy().addAll(assistanceCopy);
							if(rsc.getOption().equals(Constants.ANNUAL))
							{
								annualFundingCompute(yrCount,measures,actualFunds,plannedFunds,
								   		 actualTerms, plannedTerms,actSumCommit, 
										 actSumDisb,actSumExp,planSumCommit,planSumDisb,planSumExp,
										 actSumCommitTerms,actSumDisbTerms, actSumExpTerms,
										 planSumCommitTerms, planSumDisbTerms, planSumExpTerms,
										 totalCommitment,totalDisbursement,report, reports,
										 subTotActualFunds,
										 subTotPlannedFunds,
										 actCommitSubTotal, 
										 actDisbSubTotal ,
										 actExpSubTotal , 
										 planCommitSubTotal,
										 planDisbSubTotal,
										 planExpSubTotal,
										 unDisbSubTotal,
										 false
								);
							}
							else
								quarterlyFundingCompute(yrCount,measures,
										actualCommFunds, actualDisbFunds,actualExpFunds, 
										plannedCommFunds, plannedDisbFunds,plannedExpFunds, 
										actualCommTerms,actualDisbTerms,actualExpTerms,
										plannedCommTerms,plannedDisbTerms,plannedExpTerms,			
										actSumCommit, actSumDisb, actSumExp, planSumCommit,planSumDisb,planSumExp,
										  actSumCommitTerms, actSumDisbTerms, actSumExpTerms,planSumCommitTerms, planSumDisbTerms,planSumExpTerms,
										 totalCommitment,totalDisbursement,report, reports,
										 subTotActualCommFunds,subTotActualDisbFunds,subTotActualExpFunds,
										 subTotPlannedCommFunds,subTotPlannedDisbFunds,subTotPlannedExpFunds,
										  actCommitSubTotal, actDisbSubTotal, actExpSubTotal , planCommitSubTotal,planDisbSubTotal,planExpSubTotal,unDisbSubTotal,
										 false);

							ampReports.add(reports);
							totalCommitment=0;
							totalDisbursement=0;
							donors.clear();
							assistance.clear();
							assistanceCopy.clear();
							modality.clear();
							contacts.clear();
							sectors.clear();
							regions.clear();
							components.clear();
							contactName=null;
							minYear=maxYear=0;
							sumUnDisb=actSumCommit=actSumDisb=actSumExp=planSumCommit=planSumDisb=planSumExp = 0;
							
							//reset subtotals - we need fresh objects here!
							 actSumCommitTerms=new AmpByAssistTypeList();
							 actSumDisbTerms=new AmpByAssistTypeList();
							 actSumExpTerms=new AmpByAssistTypeList();
							 planSumCommitTerms=new AmpByAssistTypeList();
							 planSumDisbTerms=new AmpByAssistTypeList();
							 planSumExpTerms=new AmpByAssistTypeList();
							 sumUnDisbTerms=new AmpByAssistTypeList();
		
							 actCommitSubTotalTerms=new AmpByAssistTypeList();
								actDisbSubTotalTerms=new AmpByAssistTypeList();
								actExpSubTotalTerms=new AmpByAssistTypeList();
								planCommitSubTotalTerms=new AmpByAssistTypeList();
								planDisbSubTotalTerms=new AmpByAssistTypeList();
								planExpSubTotalTerms=new AmpByAssistTypeList();
								UnDisbSubTotalTerms=new AmpByAssistTypeList();
								
								actCommitSubSubTotalTerms=new AmpByAssistTypeList();
								actDisbSubSubTotalTerms=new AmpByAssistTypeList();
								actExpSubSubTotalTerms=new AmpByAssistTypeList();
								planCommitSubSubTotalTerms=new AmpByAssistTypeList();
								planDisbSubSubTotalTerms=new AmpByAssistTypeList();
								planExpSubSubTotalTerms=new AmpByAssistTypeList();
								UnDisbSubSubTotalTerms=new AmpByAssistTypeList();
								
							 
							
							for(int i=0;i<yrCount;i++)
							{
								actualFunds[i][0]=actualFunds[i][1]=actualFunds[i][2]=0;
								plannedFunds[i][0]=plannedFunds[i][1]=plannedFunds[i][2]=0;
								actualCommFunds[i][0]=actualCommFunds[i][1]=actualCommFunds[i][2]=actualCommFunds[i][3]=0;
								plannedCommFunds[i][0]=plannedCommFunds[i][1]=plannedCommFunds[i][2]=plannedCommFunds[i][3]=0;
								actualDisbFunds[i][0]=actualDisbFunds[i][1]=actualDisbFunds[i][2]=actualDisbFunds[i][3]=0;
								plannedDisbFunds[i][0]=plannedDisbFunds[i][1]=plannedDisbFunds[i][2]=plannedDisbFunds[i][3]=0;
								actualExpFunds[i][0]=actualExpFunds[i][1]=actualExpFunds[i][2]=actualExpFunds[i][3]=0;
								plannedExpFunds[i][0]=plannedExpFunds[i][1]=plannedExpFunds[i][2]=plannedExpFunds[i][3]=0;
									for (int y = 0; y < 3; y++) {
									actualTerms[i][y]=new AmpByAssistTypeList();			
									plannedTerms[i][y]=new AmpByAssistTypeList();
									
								}
								
									for (int  y = 0;  y < 4;  y++) {
										actualCommTerms[i][ y]=new AmpByAssistTypeList();
										plannedCommTerms[i][ y]=new AmpByAssistTypeList();
										actualDisbTerms[i][ y]=new AmpByAssistTypeList();
										plannedDisbTerms[i][ y]=new AmpByAssistTypeList();
										actualExpTerms[i][ y]=new AmpByAssistTypeList();
										plannedExpTerms[i][ y]=new AmpByAssistTypeList();
								}
								

							}
						}
						reports= new Report();
						reports.setType(rsc.getType().intValue());
						reports.setRecords(new ArrayList());
						//logger.debug("Init Record");
						title=ampReportCache.getActivityName();
						
						
						
						ampId=ampReportCache.getAmpId();
						reports.setAmpActivityId(ampReportCache.getAmpActivityId());
						
						if(reports.getAmpActivityId().longValue()==100156) {
							logger.info("bad project");
						}
						
						if(DbUtil.getAmpComponent(ampReportCache.getAmpActivityId()).size()==0)
							components.add(" ");
						else
							components.addAll(DbUtil.getAmpComponent(ampReportCache.getAmpActivityId()));
						
						if(ampReportCache.getLevelName().equals("Not Exist"))
							level=" ";
						else
							level=ampReportCache.getLevelName();
						if(ampReportCache.getStatusName()==null)
							status=" ";
						else
							status=ampReportCache.getStatusName();
						if(DbUtil.getAmpReportSector(ampReportCache.getAmpActivityId()).size()==0)
							sectors.add(" ");
						else
							sectors.addAll(DbUtil.getAmpReportSector(ampReportCache.getAmpActivityId()));
						if(DbUtil.getAmpReportLocation(ampReportCache.getAmpActivityId()).size()==0)
							regions.add(" ");
						else
							regions.addAll(DbUtil.getAmpReportLocation(ampReportCache.getAmpActivityId()));
						if(ampReportCache.getTermAssistName()!=null){
							assistance.add(ampReportCache.getTermAssistName());
							assistanceCopy.add(ampReportCache.getTermAssistName());
						} else {
							assistance.add(DbUtil.getAmpAssistanceType(ampReportCache.getAmpActivityId()));
						}
						if(ampReportCache.getDonorName()!=null)
							donors.add(ampReportCache.getDonorName());
						else
							donors.addAll(DbUtil.getAmpDonorsForActivity(ampReportCache.getAmpActivityId()));
						
						if(ampReportCache.getActualStartDate()!=null)
							actualStartDate=DateConversion.ConvertDateToString(ampReportCache.getActualStartDate());
						else
							actualStartDate=" ";
						if(ampReportCache.getActualCompletionDate()!=null)
							actualCompletionDate=DateConversion.ConvertDateToString(ampReportCache.getActualCompletionDate());
						else
							actualCompletionDate=" ";
						if(ampReportCache.getModalityName()!=null)
							modality.add(ampReportCache.getModalityName());
						else
							modality.add(DbUtil.getAmpModalityNames(ampReportCache.getAmpActivityId()));
						AmpActivity ampActivity=(AmpActivity) ActivityUtil.getAmpActivity(ampReportCache.getAmpActivityId());
						if(ampActivity.getDescription()!=null)
						{
							//Editor ed = org.digijava.module.editor.util.DbUtil.getEditor(ampReportCache.getActivityDescription(),"en");
							//description=ed.getBody();
							description = ampActivity.getDescription();
						}
						else
							description=" ";
							
						if(ampActivity.getObjective()!=null)
						{
/*							Editor ed = org.digijava.module.editor.util.DbUtil.getEditor(ampActivity.getObjective(),"en");
							if(ed!=null)
								objective=ed.getBody();
							else
								objective=" ";
*/								
							objective=ampActivity.getObjective();
						}
						else
							objective=" ";
	
						if(ampActivity.getContFirstName()!=null)
							contactName=ampActivity.getContFirstName();
						if(ampActivity.getContLastName()!=null)
							contactName=contactName + " " + ampActivity.getContLastName();
						if(contactName!=null)
							contacts.add(contactName);
						contactName=null;
						if(ampActivity.getMofedCntFirstName()!=null)
							contactName=ampActivity.getMofedCntFirstName();
						if(ampActivity.getMofedCntLastName()!=null)
							contactName=contactName + " " + ampActivity.getMofedCntLastName();
						if(contactName!=null)
							contacts.add(contactName);
	
					}
					//logger.debug("Title:" + title);
					if(donors.indexOf(ampReportCache.getDonorName())==-1 && ampReportCache.getDonorName()!=null)
						donors.add(ampReportCache.getDonorName());
					if(assistance.indexOf(ampReportCache.getTermAssistName())==-1 && ampReportCache.getTermAssistName()!=null) {
						assistance.add(ampReportCache.getTermAssistName());
						assistanceCopy.add(ampReportCache.getTermAssistName());
					}
					if(modality.indexOf(ampReportCache.getModalityName())==-1 && ampReportCache.getModalityName()!=null)
						modality.add(ampReportCache.getModalityName());
					if(ampReportCache.getFiscalYear()!=null && ampReportCache.getFiscalQuarter()!=null)
					{
						//logger.debug("begin if");
						if(new Long(fiscalCalId).equals(Constants.ETH_FY) || new Long(fiscalCalId).equals(Constants.ETH_CAL))
						{
							if(ampReportCache.getTransactionDate()!=null)
							{
								GregorianCalendar calendar = new GregorianCalendar();
								EthiopianCalendar ec=new EthiopianCalendar();
								EthiopianCalendar tempDate=new EthiopianCalendar();
								calendar.setTime(ampReportCache.getTransactionDate());
								ec=tempDate.getEthiopianDate(calendar);
								if(new Long(fiscalCalId).equals(Constants.ETH_FY))
								{
									fiscalYear=(int)ec.ethFiscalYear;
									fiscalQuarter=(int)ec.ethFiscalQrt;
								}
								if(new Long(fiscalCalId).equals(Constants.ETH_CAL))
								{
									fiscalYear=(int)ec.ethYear;
									fiscalQuarter=(int)ec.ethQtr;
								}
							}
						}
						if(new Long(fiscalCalId).equals(Constants.GREGORIAN))
						{
							fiscalYear=ampReportCache.getFiscalYear().intValue();
							fiscalQuarter=ampReportCache.getFiscalQuarter().intValue();
						}
						if(minYear > fiscalYear)
							minYear=fiscalYear;
						if(maxYear < fiscalYear)
							maxYear=fiscalYear;
						if(ampReportCache.getCurrencyCode().equals("USD"))
							fromExchangeRate=1.0;
						else
							fromExchangeRate=DbUtil.getExchangeRate(ampReportCache.getCurrencyCode(),Constants.PLANNED,ampReportCache.getTransactionDate());
						if(ampCurrencyCode.equals("USD"))
							toExchangeRate=1.0;
						else	
							toExchangeRate=DbUtil.getExchangeRate(ampCurrencyCode,Constants.PLANNED,ampReportCache.getTransactionDate());
						
						if(rsc.getOption().equals(Constants.ANNUAL))
						{
							if(ampReportCache.getActualCommitment().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
								totalCommitment+=extractAnnualFunding( ampReportCache, ampReportCache.getActualCommitment(),fromExchangeRate,toExchangeRate, fromYr, toYr, fiscalYear,actualTerms, actualFunds, 0);
							
									
							if(ampReportCache.getActualDisbursement().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
								totalDisbursement+=extractAnnualFunding( ampReportCache, ampReportCache.getActualDisbursement(),
										fromExchangeRate,toExchangeRate, fromYr, toYr, fiscalYear,actualTerms, actualFunds, 1);
								
							if(measures.indexOf(new Long(3))!=-1)							
								if(ampReportCache.getActualExpenditure().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
									extractAnnualFunding( ampReportCache, ampReportCache.getActualExpenditure(),
											fromExchangeRate,toExchangeRate, fromYr, toYr, fiscalYear,actualTerms, actualFunds, 2);
									
							
							if(measures.indexOf(new Long(4))!=-1)								
								if(ampReportCache.getPlannedCommitment().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
									totalCommitment+=extractAnnualFunding( ampReportCache,  ampReportCache.getPlannedCommitment(),fromExchangeRate,toExchangeRate, fromYr, toYr, fiscalYear,plannedTerms, plannedFunds, 0);
							
							if(measures.indexOf(new Long(5))!=-1)
								if(ampReportCache.getPlannedDisbursement().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
									totalDisbursement+=extractAnnualFunding( ampReportCache, ampReportCache.getPlannedDisbursement(),fromExchangeRate,toExchangeRate, fromYr, toYr, fiscalYear, plannedTerms, plannedFunds, 1);
							
							if(measures.indexOf(new Long(6))!=-1)							
								if(ampReportCache.getPlannedExpenditure().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
									extractAnnualFunding( ampReportCache, ampReportCache.getPlannedExpenditure(),fromExchangeRate,toExchangeRate, fromYr, toYr, fiscalYear, plannedTerms, plannedFunds, 2);
									
								
						}
						else
						{	
							if(ampReportCache.getActualCommitment().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
								totalCommitment+=setQuarterlyFunding(ampReportCache, ampReportCache.getActualCommitment(), fromExchangeRate,
										toExchangeRate, fromYr, toYr, fiscalQuarter, fiscalYear, 
										actualCommTerms,actualCommFunds);
							
									
							if(ampReportCache.getActualDisbursement().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
								totalDisbursement+=setQuarterlyFunding(ampReportCache,  ampReportCache.getActualDisbursement(), fromExchangeRate, toExchangeRate, fromYr, toYr, fiscalQuarter, fiscalYear, actualDisbTerms,actualDisbFunds);
							
								
							if(measures.indexOf(new Long(3))!=-1)
								if(ampReportCache.getActualExpenditure().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
								setQuarterlyFunding(ampReportCache, ampReportCache.getActualExpenditure(), fromExchangeRate,toExchangeRate, fromYr, toYr, fiscalQuarter, fiscalYear, actualExpTerms,actualExpFunds);								
						
							if(measures.indexOf(new Long(4))!=-1)
								if(ampReportCache.getPlannedCommitment().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
								totalCommitment+=setQuarterlyFunding(ampReportCache, ampReportCache.getPlannedCommitment(), fromExchangeRate,toExchangeRate, fromYr, toYr, fiscalQuarter, fiscalYear, plannedCommTerms,plannedCommFunds);
								
							if(measures.indexOf(new Long(5))!=-1)
								if(ampReportCache.getPlannedDisbursement().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
							    totalDisbursement+=setQuarterlyFunding(ampReportCache,  ampReportCache.getPlannedDisbursement(), fromExchangeRate,toExchangeRate, fromYr, toYr, fiscalQuarter, fiscalYear, plannedDisbTerms,plannedDisbFunds);
																
							if(measures.indexOf(new Long(6))!=-1)
								if(ampReportCache.getPlannedExpenditure().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
								setQuarterlyFunding(ampReportCache, ampReportCache.getPlannedExpenditure(), fromExchangeRate,toExchangeRate, fromYr, toYr, fiscalQuarter, fiscalYear, plannedExpTerms,plannedExpFunds);
							
						}	
						//logger.debug("End If");
						
					}
					
				}
				//logger.debug("At the end");
				iterColumn=columns.iterator();
				while(iterColumn.hasNext())
				{
					report=new AdvancedReport();
					report.setDonors(new ArrayList());
					report.setAssistance(new ArrayList());
					report.setSectors(new ArrayList());
					report.setRegions(new ArrayList());
					report.setModality(new ArrayList());
					report.setContacts(new ArrayList());
					Column c=(Column) iterColumn.next();
					if(c.getColumnId().equals(Constants.STATUS_NAME))
						report.setStatus(status);
					if(c.getColumnId().equals(Constants.DONOR_NAME))
					{
						if(donors.size()==0)
							donors.add(" ");
						report.getDonors().addAll(donors);
					}
					if(c.getColumnId().equals(Constants.ACTUAL_START_DATE))
						report.setActualStartDate(actualStartDate);
					if(c.getColumnId().equals(Constants.ACTIVITY_NAME))
						report.setTitle(title);
					
					
					if(c.getColumnId().equals(Constants.TERM_ASSIST_NAME))
					{
						if(assistance.size()==0)
							assistance.add(" ");
						report.getAssistance().addAll(assistance);
					}
					if(c.getColumnId().equals(Constants.LEVEL_NAME))
						report.setLevel(level);
					if(c.getColumnId().equals(Constants.ACTUAL_COMPLETION_DATE))
						report.setActualCompletionDate(actualCompletionDate);
					if(c.getColumnId().equals(Constants.SECTOR_NAME))
						report.getSectors().addAll(sectors);
					if(c.getColumnId().equals(Constants.REGION_NAME))
						report.getRegions().addAll(regions);
					if(c.getColumnId().equals(Constants.AMP_ID))
						report.setAmpId(ampId);

					if(c.getColumnId().equals(Constants.FUNDING_INSTRUMENT))
					{
						if(modality.size()==0)
							modality.add(" ");
						report.getModality().addAll(modality);
					}
					if(c.getColumnId().equals(Constants.CONTACT_NAME))
					{
						if(contacts.size()==0)
							contacts.add(" ");
						report.getContacts().addAll(contacts);
					}
					if(c.getColumnId().equals(Constants.OBJECTIVE))
					{
						report.setObjective(objective);
						if(report.getObjective()!=null)
						{
							Editor ed = org.digijava.module.editor.util.DbUtil.getEditor(objective,"en");
							report.setObjectivePDFXLS(getDescParsed(ed.getBody()));
							logger.debug("the report.getObjectivePDFXLS yields....: "+report.getObjectivePDFXLS());
						}
						else
							report.setObjectivePDFXLS(" ");
					}
					if(c.getColumnId().equals(Constants.DESCRIPTION))
					{
						report.setDescription(description);
						if(report.getDescription()!=null)
						{
							Editor ed = org.digijava.module.editor.util.DbUtil.getEditor(description,"en");
							report.setDescriptionPDFXLS(getDescParsed(ed.getBody()));
							logger.debug("the report.getDescriptionPDFXLS yields....: "+report.getDescriptionPDFXLS());
						}
						else
							report.setDescriptionPDFXLS(" ");
					}
					if(c.getColumnId().equals(Constants.TOTAL_COMMITMENT))
						report.setTotalCommitment(mf.format(totalCommitment));
					if(c.getColumnId().equals(Constants.TOTAL_DISBURSEMENT))
						report.setTotalDisbursement(mf.format(totalDisbursement));
					reports.getRecords().add(report);
				}
				report=new AdvancedReport();
				report.setAssistanceCopy(new ArrayList());
				report.getAssistanceCopy().addAll(assistanceCopy);
				
				
				report.setAmpFund(new ArrayList());
				if(rsc.getOption().equals(Constants.ANNUAL))
				{
					annualFundingCompute(yrCount,measures,actualFunds,plannedFunds,
					   		 actualTerms, plannedTerms,actSumCommit, 
							 actSumDisb,actSumExp,planSumCommit,planSumDisb,planSumExp,
							 actSumCommitTerms,actSumDisbTerms, actSumExpTerms,
							 planSumCommitTerms, planSumDisbTerms, planSumExpTerms,
							 totalCommitment,totalDisbursement,report, reports,
							 subTotActualFunds,
							 subTotPlannedFunds,
							 actCommitSubTotal, 
							 actDisbSubTotal ,
							 actExpSubTotal , 
							 planCommitSubTotal,
							 planDisbSubTotal,
							 planExpSubTotal,
							 unDisbSubTotal,
							 false
					);
				}
				else
					quarterlyFundingCompute(yrCount,measures,
							actualCommFunds, actualDisbFunds,actualExpFunds, 
							plannedCommFunds, plannedDisbFunds,plannedExpFunds, 
							actualCommTerms,actualDisbTerms,actualExpTerms,
							plannedCommTerms,plannedDisbTerms,plannedExpTerms,			
							actSumCommit, actSumDisb, actSumExp, planSumCommit,planSumDisb,planSumExp,
							  actSumCommitTerms, actSumDisbTerms, actSumExpTerms,planSumCommitTerms, planSumDisbTerms,planSumExpTerms,
							 totalCommitment,totalDisbursement,report, reports,
							 subTotActualCommFunds,subTotActualDisbFunds,subTotActualExpFunds,
							 subTotPlannedCommFunds,subTotPlannedDisbFunds,subTotPlannedExpFunds,
							  actCommitSubTotal, actDisbSubTotal, actExpSubTotal , planCommitSubTotal,planDisbSubTotal,planExpSubTotal,unDisbSubTotal,
							 false);

				
				ampReports.add(reports);
			}
			// Code for hierarchial report
			if(ampActivities!=null && rsc.getHierarchy().size()>0)
			{
				mreport=new multiReport();
				mreport.setHierarchy(new ArrayList());
				if(hierarchy.size()==1)
				{
					Column colLevel1=(Column) hierarchy.get(0);
					//dbReturnSet=ReportUtil.getLevel1AdvancedReport(inClause,colLevel1.getColumnId(),ampStatusId,ampDonorId,ampModalityId,ampSectorId,region,component,approvedActivityList);
					dbReturnSet=ReportUtil.getLevel1AdvancedReport(inClause,colLevel1.getColumnId(),ampStatusId,ampDonorId,ampModalityId,ampSectorId,region,component, approvedActivityList);
				}
				if(hierarchy.size()==2)
				{
					Column colLevel1=(Column) hierarchy.get(0);
					Column colLevel2=(Column) hierarchy.get(1);
					dbReturnSet=ReportUtil.getLevel2AdvancedReport(inClause,colLevel1.getColumnId(),colLevel2.getColumnId(),ampStatusId,ampDonorId,ampModalityId,ampSectorId,region,component, approvedActivityList);
				}

				if(hierarchy.size()==3)
				{
					Column colLevel1=(Column) hierarchy.get(0);
					Column colLevel2=(Column) hierarchy.get(1);
					Column colLevel3=(Column) hierarchy.get(2);
					dbReturnSet=ReportUtil.getLevel3AdvancedReport(inClause,colLevel1.getColumnId(),colLevel2.getColumnId(),colLevel3.getColumnId(),ampStatusId,ampDonorId,ampModalityId,ampSectorId,region,component, approvedActivityList);
					//logger.debug("Hierarchy 3 Size: " + dbReturnSet.size());
				}
				iterLevel1=dbReturnSet.iterator();
				while(iterLevel1.hasNext())
				{
					AdvancedHierarchyReport ahReportLevel1=(AdvancedHierarchyReport) iterLevel1.next();
					ahReport=new AdvancedHierarchyReport();
					ahReport.setProject(new ArrayList());
					ahReport.setName(ahReportLevel1.getName());
					ahReport.setLabel(ahReportLevel1.getLabel());
					activityIds=(ArrayList)ahReportLevel1.getActivities();
					if(ahReportLevel1.getLevels()!=null)
					{
						ahReport.setLevels(new ArrayList());
						iterLevel2=ahReportLevel1.getLevels().iterator();
						while(iterLevel2.hasNext())
						{
							AdvancedHierarchyReport ahReportLevel2=(AdvancedHierarchyReport) iterLevel2.next();
							ahTempLevel2=new AdvancedHierarchyReport();
							ahTempLevel2.setName(ahReportLevel2.getName());
							ahTempLevel2.setLabel(ahReportLevel2.getLabel());
							ahTempLevel2.setProject(new ArrayList());
							activityIds=(ArrayList)ahReportLevel2.getActivities();
							if(ahReportLevel2.getLevels()!=null)
							{
								ahTempLevel2.setLevels(new ArrayList());
								iterLevel3=ahReportLevel2.getLevels().iterator();
								while(iterLevel3.hasNext())
								{
									AdvancedHierarchyReport ahReportLevel3=(AdvancedHierarchyReport) iterLevel3.next();									
									ahTempLevel3=new AdvancedHierarchyReport();
									ahTempLevel3.setName(ahReportLevel3.getName());
									ahTempLevel3.setLabel(ahReportLevel3.getLabel());
									ahTempLevel3.setProject(new ArrayList());
									activityIds=(ArrayList)ahReportLevel3.getActivities();
									ahTempLevel3=getAdvancedReportRecords(ampActivities,activityIds,rsc,fromYr,toYr,perspective,fiscalCalId,ampCurrencyCode,ahTempLevel3, ahTempLevel2,ahReportLevel1);
									ahTempLevel2.getLevels().add(ahTempLevel3);
									Iterator iterFund = ahTempLevel3.getFundSubTotal().iterator();
									if(rsc.getOption().equals(Constants.ANNUAL))
									{
										for(int i=0;i<=yrCount ;i++ )
										{
											AmpFund ampFund=(AmpFund) iterFund.next();
			
											if(i<yrCount)
											{
												subSubTotActualFunds[i][0]=subSubTotActualFunds[i][0] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getCommAmount()));
												subSubTotActualFunds[i][1]=subSubTotActualFunds[i][1] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getDisbAmount()));
												subSubTotActualFunds[i][2]=subSubTotActualFunds[i][2] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getExpAmount()));
												subSubTotPlannedFunds[i][0]=subSubTotPlannedFunds[i][0] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlCommAmount()));
												subSubTotPlannedFunds[i][1]=subSubTotPlannedFunds[i][1] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlDisbAmount()));
												subSubTotPlannedFunds[i][2]=subSubTotPlannedFunds[i][2] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlExpAmount()));
											}
											if(i==yrCount)						
											{
												actCommitSubSubTotal = actCommitSubSubTotal + Double.parseDouble(DecimalToText.removeCommas(ampFund.getCommAmount()));
												actDisbSubSubTotal = actDisbSubSubTotal + Double.parseDouble(DecimalToText.removeCommas(ampFund.getDisbAmount()));
												actExpSubSubTotal = actExpSubSubTotal + Double.parseDouble(DecimalToText.removeCommas(ampFund.getExpAmount()));
												planCommitSubSubTotal = planCommitSubSubTotal  + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlCommAmount()));
												planDisbSubSubTotal = planDisbSubSubTotal + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlDisbAmount()));
												planExpSubSubTotal = planExpSubSubTotal + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlExpAmount()));
												unDisbSubSubTotal = unDisbSubSubTotal + Double.parseDouble(DecimalToText.removeCommas(ampFund.getUnDisbAmount()));
											}
										}
									}	
									else
									{
										for(int i=0;i<=yrCount ;i++ )
										{
											if(i<yrCount)
											{
												for(int qtr=0;qtr<4;qtr++)
												{
													AmpFund ampFund=(AmpFund) iterFund.next();
													subSubTotActualCommFunds[i][qtr]=subSubTotActualCommFunds[i][qtr] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getCommAmount()));
													subSubTotActualDisbFunds[i][qtr]=subSubTotActualDisbFunds[i][qtr] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getDisbAmount()));
													subSubTotActualExpFunds[i][qtr]=subSubTotActualExpFunds[i][qtr] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getExpAmount()));
													subSubTotPlannedCommFunds[i][qtr]=subSubTotPlannedCommFunds[i][qtr] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlCommAmount()));
													subSubTotPlannedDisbFunds[i][qtr]=subSubTotPlannedDisbFunds[i][qtr] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlDisbAmount()));
													subSubTotPlannedExpFunds[i][qtr]=subSubTotPlannedExpFunds[i][qtr] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlExpAmount()));
												}
											}		
											if(i==yrCount)
											{
												AmpFund ampFund=(AmpFund) iterFund.next();
												actCommitSubSubTotal = actCommitSubSubTotal + Double.parseDouble(DecimalToText.removeCommas(ampFund.getCommAmount()));
												actDisbSubSubTotal = actDisbSubSubTotal + Double.parseDouble(DecimalToText.removeCommas(ampFund.getDisbAmount()));
												actExpSubSubTotal = actExpSubSubTotal + Double.parseDouble(DecimalToText.removeCommas(ampFund.getExpAmount()));
												planCommitSubSubTotal = planCommitSubSubTotal  + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlCommAmount()));
												planDisbSubSubTotal = planDisbSubSubTotal + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlDisbAmount()));
												planExpSubSubTotal = planExpSubSubTotal + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlExpAmount()));
												unDisbSubSubTotal = unDisbSubSubTotal +  Double.parseDouble(DecimalToText.removeCommas(ampFund.getUnDisbAmount()));
											}			
										}
									}
								}//End of iteration of level3
								if(ahTempLevel2.getLevels().size()>0)
								{
									ahTempLevel2.setFundSubTotal(new ArrayList());
									if(rsc.getOption().equals(Constants.ANNUAL))
									{
										for(int i=0;i<yrCount;i++)
										{
											AmpFund ampFund=new AmpFund();
											if(measures.indexOf(new Long(1))!=-1)
												ampFund.setCommAmount(mf.format(subSubTotActualFunds[i][0])); 
											if(measures.indexOf(new Long(2))!=-1)
												ampFund.setDisbAmount(mf.format(subSubTotActualFunds[i][1])); 
											if(measures.indexOf(new Long(3))!=-1)
												ampFund.setExpAmount(mf.format(subSubTotActualFunds[i][2]));	
											if(measures.indexOf(new Long(4))!=-1)
												ampFund.setPlCommAmount(mf.format(subSubTotPlannedFunds[i][0])); 
											if(measures.indexOf(new Long(5))!=-1)
												ampFund.setPlDisbAmount(mf.format(subSubTotPlannedFunds[i][1])); 
											if(measures.indexOf(new Long(6))!=-1)
												ampFund.setPlExpAmount(mf.format(subSubTotPlannedFunds[i][2]));	
																			
											ahTempLevel2.getFundSubTotal().add(ampFund);
	
											subTotActualFunds[i][0] = subTotActualFunds[i][0] + subSubTotActualFunds[i][0];
											subTotActualFunds[i][1] = subTotActualFunds[i][1] + subSubTotActualFunds[i][1];
											subTotActualFunds[i][2] = subTotActualFunds[i][2] + subSubTotActualFunds[i][2];
											subTotPlannedFunds[i][0] = subTotPlannedFunds[i][0] + subSubTotPlannedFunds[i][0];
											subTotPlannedFunds[i][1] = subTotPlannedFunds[i][1] + subSubTotPlannedFunds[i][1];
											subTotPlannedFunds[i][2] = subTotPlannedFunds[i][2] + subSubTotPlannedFunds[i][2];
										}
										actCommitSubTotal = actCommitSubTotal + actCommitSubSubTotal;
										actDisbSubTotal = actDisbSubTotal + actDisbSubSubTotal;
										actExpSubTotal = actExpSubTotal + actExpSubSubTotal;
										planCommitSubTotal = planCommitSubTotal + planCommitSubSubTotal;
										planDisbSubTotal = planDisbSubTotal + planDisbSubSubTotal;
										planExpSubTotal = planExpSubTotal + planExpSubSubTotal;
										unDisbSubTotal = unDisbSubTotal + unDisbSubSubTotal;

										AmpFund fund = new AmpFund();
										fund.setCommAmount(mf.format(actCommitSubTotal));
										fund.setDisbAmount(mf.format(actDisbSubTotal));
										fund.setExpAmount(mf.format(actExpSubTotal));
										fund.setPlCommAmount(mf.format(planCommitSubTotal));
										fund.setPlDisbAmount(mf.format(planDisbSubTotal));
										fund.setPlExpAmount(mf.format(planExpSubTotal));
										fund.setUnDisbAmount(mf.format(unDisbSubTotal));
										ahTempLevel2.getFundSubTotal().add(fund) ;	
									}
									else
									{
										for(int i=0;i<yrCount;i++)
										{
											for(int qtr=0;qtr<4;qtr++)
											{
												AmpFund ampFund=new AmpFund();
												if(measures.indexOf(new Long(1))!=-1)
													ampFund.setCommAmount(mf.format(subSubTotActualCommFunds[i][qtr])); 
												if(measures.indexOf(new Long(2))!=-1)
													ampFund.setDisbAmount(mf.format(subSubTotActualDisbFunds[i][qtr])); 
												if(measures.indexOf(new Long(3))!=-1)
													ampFund.setExpAmount(mf.format(subSubTotActualExpFunds[i][qtr]));	
												if(measures.indexOf(new Long(4))!=-1)
													ampFund.setPlCommAmount(mf.format(subSubTotPlannedCommFunds[i][qtr])); 
												if(measures.indexOf(new Long(5))!=-1)
													ampFund.setPlDisbAmount(mf.format(subSubTotPlannedDisbFunds[i][qtr])); 
												if(measures.indexOf(new Long(6))!=-1)
													ampFund.setPlExpAmount(mf.format(subSubTotPlannedExpFunds[i][qtr]));	
																			
												ahTempLevel2.getFundSubTotal().add(ampFund);
	
												subTotActualCommFunds[i][qtr] = subTotActualCommFunds[i][qtr] + subSubTotActualCommFunds[i][qtr];
												subTotActualDisbFunds[i][qtr] = subTotActualDisbFunds[i][qtr] + subSubTotActualDisbFunds[i][qtr];
												subTotActualExpFunds[i][qtr] = subTotActualExpFunds[i][qtr] + subSubTotActualExpFunds[i][qtr];
												subTotPlannedCommFunds[i][qtr] = subTotPlannedCommFunds[i][qtr] + subSubTotPlannedCommFunds[i][qtr];
												subTotPlannedDisbFunds[i][qtr] = subTotPlannedDisbFunds[i][qtr] + subSubTotPlannedDisbFunds[i][qtr];
												subTotPlannedExpFunds[i][qtr] = subTotPlannedExpFunds[i][qtr] + subSubTotPlannedExpFunds[i][qtr];
											}
										}
										actCommitSubTotal = actCommitSubTotal + actCommitSubSubTotal;
										actDisbSubTotal = actDisbSubTotal + actDisbSubSubTotal;
										actExpSubTotal = actExpSubTotal + actExpSubSubTotal;
										planCommitSubTotal = planCommitSubTotal + planCommitSubSubTotal;
										planDisbSubTotal = planDisbSubTotal + planDisbSubSubTotal;
										planExpSubTotal = planExpSubTotal + planExpSubSubTotal;
										unDisbSubTotal = unDisbSubTotal + unDisbSubSubTotal;

										AmpFund fund = new AmpFund();
										fund.setCommAmount(mf.format(actCommitSubTotal));
										fund.setDisbAmount(mf.format(actDisbSubTotal));
										fund.setExpAmount(mf.format(actExpSubTotal));
										fund.setPlCommAmount(mf.format(planCommitSubTotal));
										fund.setPlDisbAmount(mf.format(planDisbSubTotal));
										fund.setPlExpAmount(mf.format(planExpSubTotal));
										fund.setUnDisbAmount(mf.format(unDisbSubTotal));
										ahTempLevel2.getFundSubTotal().add(fund) ;	
									}
								}
								ahReport.getLevels().add(ahTempLevel2);
								unDisbSubSubTotal=actCommitSubSubTotal=actDisbSubSubTotal=actExpSubSubTotal=planCommitSubSubTotal=planDisbSubSubTotal=planExpSubSubTotal = 0;
								for(int i=0;i<yrCount;i++)
								{
									subSubTotActualFunds[i][0]=subSubTotActualFunds[i][1]=subSubTotActualFunds[i][2]=0;
									subSubTotPlannedFunds[i][0]=subSubTotPlannedFunds[i][1]=subSubTotPlannedFunds[i][2]=0;
									subSubTotActualCommFunds[i][0]=subSubTotActualCommFunds[i][1]=subSubTotActualCommFunds[i][2]=subSubTotActualCommFunds[i][3]=0;
									subSubTotPlannedCommFunds[i][0]=subSubTotPlannedCommFunds[i][1]=subSubTotPlannedCommFunds[i][2]=subSubTotPlannedCommFunds[i][3]=0;
									subSubTotActualDisbFunds[i][0]=subSubTotActualDisbFunds[i][1]=subSubTotActualDisbFunds[i][2]=subSubTotActualDisbFunds[i][3]=0;
									subSubTotPlannedDisbFunds[i][0]=subSubTotPlannedDisbFunds[i][1]=subSubTotPlannedDisbFunds[i][2]=subSubTotPlannedDisbFunds[i][3]=0;
									subSubTotActualExpFunds[i][0]=subSubTotActualExpFunds[i][1]=subSubTotActualExpFunds[i][2]=subSubTotActualExpFunds[i][3]=0;
									subSubTotPlannedExpFunds[i][0]=subSubTotPlannedExpFunds[i][1]=subSubTotPlannedExpFunds[i][2]=subSubTotPlannedExpFunds[i][3]=0;
								}
								
							}
							else
							{
								ahTempLevel2=getAdvancedReportRecords(ampActivities,activityIds,rsc,fromYr,toYr,perspective,fiscalCalId,ampCurrencyCode,ahTempLevel2,ahReportLevel1,ahTempLevel3);
								if(ahTempLevel2.getProject().size()>0)
									ahReport.getLevels().add(ahTempLevel2);
								Iterator iterFund = ahTempLevel2.getFundSubTotal().iterator();
								if(rsc.getOption().equals(Constants.ANNUAL))
								{
									for(int i=0;i<=yrCount ;i++ )
									{
										AmpFund ampFund=(AmpFund) iterFund.next();
		
										if(i<yrCount)
										{
											subTotActualFunds[i][0]=subTotActualFunds[i][0] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getCommAmount()));
											subTotActualFunds[i][1]=subTotActualFunds[i][1] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getDisbAmount()));
											subTotActualFunds[i][2]=subTotActualFunds[i][2] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getExpAmount()));
											subTotPlannedFunds[i][0]=subTotPlannedFunds[i][0] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlCommAmount()));
											subTotPlannedFunds[i][1]=subTotPlannedFunds[i][1] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlDisbAmount()));
											subTotPlannedFunds[i][2]=subTotPlannedFunds[i][2] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlExpAmount()));
										}
										if(i==yrCount)						
										{
											actCommitSubTotal = actCommitSubTotal + Double.parseDouble(DecimalToText.removeCommas(ampFund.getCommAmount()));
											actDisbSubTotal = actDisbSubTotal + Double.parseDouble(DecimalToText.removeCommas(ampFund.getDisbAmount()));
											actExpSubTotal = actExpSubTotal + Double.parseDouble(DecimalToText.removeCommas(ampFund.getExpAmount()));
											planCommitSubTotal = planCommitSubTotal  + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlCommAmount()));
											planDisbSubTotal = planDisbSubTotal + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlDisbAmount()));
											planExpSubTotal = planExpSubTotal + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlExpAmount()));
											unDisbSubTotal = unDisbSubTotal + Double.parseDouble(DecimalToText.removeCommas(ampFund.getUnDisbAmount()));
										}
	
									}
								}
								else
								{
									for(int i=0;i<=yrCount ;i++ )
									{
										if(i<yrCount)
										{
											for(int qtr=0;qtr<4;qtr++)
											{
												AmpFund ampFund=(AmpFund) iterFund.next();
												
												subTotActualCommFunds[i][qtr]=subTotActualCommFunds[i][qtr] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getCommAmount()));
												subTotActualDisbFunds[i][qtr]=subTotActualDisbFunds[i][qtr] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getDisbAmount()));
												subTotActualExpFunds[i][qtr]=subTotActualExpFunds[i][qtr] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getExpAmount()));
												subTotPlannedCommFunds[i][qtr]=subTotPlannedCommFunds[i][qtr] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlCommAmount()));
												subTotPlannedDisbFunds[i][qtr]=subTotPlannedDisbFunds[i][qtr] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlDisbAmount()));
												subTotPlannedExpFunds[i][qtr]=subTotPlannedExpFunds[i][qtr] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlExpAmount()));
											}
										}
										if(i==yrCount)
										{
											AmpFund ampFund=(AmpFund) iterFund.next();
											actCommitSubTotal = actCommitSubTotal + Double.parseDouble(DecimalToText.removeCommas(ampFund.getCommAmount()));
											actDisbSubTotal = actDisbSubTotal + Double.parseDouble(DecimalToText.removeCommas(ampFund.getDisbAmount()));
											actExpSubTotal = actExpSubTotal + Double.parseDouble(DecimalToText.removeCommas(ampFund.getExpAmount()));
											planCommitSubTotal = planCommitSubTotal  + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlCommAmount()));
											planDisbSubTotal = planDisbSubTotal + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlDisbAmount()));
											planExpSubTotal = planExpSubTotal + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlExpAmount()));
											unDisbSubTotal = unDisbSubTotal + Double.parseDouble(DecimalToText.removeCommas(ampFund.getUnDisbAmount()));
										}			
									}
								}
							}
						}//End of Level 2 iteration
						//logger.debug("level2 size: " + ahReport.getLevels().size());
						if(ahReport.getLevels().size()>0)
						{
							ahReport.setFundSubTotal(new ArrayList());
							if(rsc.getOption().equals(Constants.ANNUAL))
							{
								for(int i=0;i<yrCount;i++)
								{
									AmpFund ampFund=new AmpFund();
									if(measures.indexOf(new Long(1))!=-1)
										ampFund.setCommAmount(mf.format(subTotActualFunds[i][0])); 
									if(measures.indexOf(new Long(2))!=-1)
										ampFund.setDisbAmount(mf.format(subTotActualFunds[i][1])); 
									if(measures.indexOf(new Long(3))!=-1)
										ampFund.setExpAmount(mf.format(subTotActualFunds[i][2]));	
									if(measures.indexOf(new Long(4))!=-1)
										ampFund.setPlCommAmount(mf.format(subTotPlannedFunds[i][0])); 
									if(measures.indexOf(new Long(5))!=-1)
										ampFund.setPlDisbAmount(mf.format(subTotPlannedFunds[i][1])); 
									if(measures.indexOf(new Long(6))!=-1)
										ampFund.setPlExpAmount(mf.format(subTotPlannedFunds[i][2]));	
									
									ahReport.getFundSubTotal().add(ampFund);
	
									totActualFunds[i][0] = totActualFunds[i][0] + subTotActualFunds[i][0];
									totActualFunds[i][1] = totActualFunds[i][1] + subTotActualFunds[i][1];
									totActualFunds[i][2] = totActualFunds[i][2] + subTotActualFunds[i][2];
									totPlannedFunds[i][0] = totPlannedFunds[i][0] + subTotPlannedFunds[i][0];
									totPlannedFunds[i][1] = totPlannedFunds[i][1] + subTotPlannedFunds[i][1];
									totPlannedFunds[i][2] = totPlannedFunds[i][2] + subTotPlannedFunds[i][2];
								}

								actCommitTotal = actCommitTotal + actCommitSubTotal;
								actDisbTotal = actDisbTotal + actDisbSubTotal;
								actExpTotal = actExpTotal + actExpSubTotal;
								planCommitTotal = planCommitTotal + planCommitSubTotal;
								planDisbTotal = planDisbTotal + planDisbSubTotal;
								planExpTotal = planExpTotal + planExpSubTotal;
								unDisbTotal = unDisbTotal + unDisbSubTotal;
	
								AmpFund fund = new AmpFund();
								fund.setCommAmount(mf.format(actCommitSubTotal));
								fund.setDisbAmount(mf.format(actDisbSubTotal));
								fund.setExpAmount(mf.format(actExpSubTotal));
								fund.setPlCommAmount(mf.format(planCommitSubTotal));
								fund.setPlDisbAmount(mf.format(planDisbSubTotal));
								fund.setPlExpAmount(mf.format(planExpSubTotal));
								fund.setUnDisbAmount(mf.format(unDisbSubTotal));
								ahReport.getFundSubTotal().add(fund) ;	
							}
							else
							{
								for(int i=0;i<yrCount;i++)
								{
									for(int qtr=0;qtr<4;qtr++)
									{
										AmpFund ampFund=new AmpFund();
										if(measures.indexOf(new Long(1))!=-1)
											ampFund.setCommAmount(mf.format(subTotActualCommFunds[i][qtr])); 
										if(measures.indexOf(new Long(2))!=-1)
											ampFund.setDisbAmount(mf.format(subTotActualDisbFunds[i][qtr])); 
										if(measures.indexOf(new Long(3))!=-1)
											ampFund.setExpAmount(mf.format(subTotActualExpFunds[i][qtr]));	
										if(measures.indexOf(new Long(4))!=-1)
											ampFund.setPlCommAmount(mf.format(subTotPlannedCommFunds[i][qtr])); 
										if(measures.indexOf(new Long(5))!=-1)
											ampFund.setPlDisbAmount(mf.format(subTotPlannedDisbFunds[i][qtr])); 
										if(measures.indexOf(new Long(6))!=-1)
											ampFund.setPlExpAmount(mf.format(subTotPlannedExpFunds[i][qtr]));	
										
										ahReport.getFundSubTotal().add(ampFund);
	
										totActualCommFunds[i][qtr] = totActualCommFunds[i][qtr] + subTotActualCommFunds[i][qtr];
										totActualDisbFunds[i][qtr] = totActualDisbFunds[i][qtr] + subTotActualDisbFunds[i][qtr];
										totActualExpFunds[i][qtr] = totActualExpFunds[i][qtr] + subTotActualExpFunds[i][qtr];
										totPlannedCommFunds[i][qtr] = totPlannedCommFunds[i][qtr] + subTotPlannedCommFunds[i][qtr];
										totPlannedDisbFunds[i][qtr] = totPlannedDisbFunds[i][qtr] + subTotPlannedDisbFunds[i][qtr];
										totPlannedExpFunds[i][qtr] = totPlannedExpFunds[i][qtr] + subTotPlannedExpFunds[i][qtr];
									}
								}
								actCommitTotal = actCommitTotal + actCommitSubTotal;
								actDisbTotal = actDisbTotal + actDisbSubTotal;
								actExpTotal = actExpTotal + actExpSubTotal;
								planCommitTotal = planCommitTotal + planCommitSubTotal;
								planDisbTotal = planDisbTotal + planDisbSubTotal;
								planExpTotal = planExpTotal + planExpSubTotal;
								unDisbTotal = unDisbTotal + unDisbSubTotal;
	
								AmpFund fund = new AmpFund();
								fund.setCommAmount(mf.format(actCommitSubTotal));
								fund.setDisbAmount(mf.format(actDisbSubTotal));
								fund.setExpAmount(mf.format(actExpSubTotal));
								fund.setPlCommAmount(mf.format(planCommitSubTotal));
								fund.setPlDisbAmount(mf.format(planDisbSubTotal));
								fund.setPlExpAmount(mf.format(planExpSubTotal));
								fund.setUnDisbAmount(mf.format(unDisbSubTotal));
								ahReport.getFundSubTotal().add(fund) ;	
							}
							
							mreport.getHierarchy().add(ahReport);
							unDisbSubTotal=actCommitSubTotal=actDisbSubTotal=actExpSubTotal=planCommitSubTotal=planDisbSubTotal=planExpSubTotal = 0;
							for(int i=0;i<yrCount;i++)
							{
								subTotActualFunds[i][0]=subTotActualFunds[i][1]=subTotActualFunds[i][2]=0;
								subTotPlannedFunds[i][0]=subTotPlannedFunds[i][1]=subTotPlannedFunds[i][2]=0;
								subTotActualCommFunds[i][0]=subTotActualCommFunds[i][1]=subTotActualCommFunds[i][2]=subTotActualCommFunds[i][3]=0;
								subTotPlannedCommFunds[i][0]=subTotPlannedCommFunds[i][1]=subTotPlannedCommFunds[i][2]=subTotPlannedCommFunds[i][3]=0;
								subTotActualDisbFunds[i][0]=subTotActualDisbFunds[i][1]=subTotActualDisbFunds[i][2]=subTotActualDisbFunds[i][3]=0;
								subTotPlannedDisbFunds[i][0]=subTotPlannedDisbFunds[i][1]=subTotPlannedDisbFunds[i][2]=subTotPlannedDisbFunds[i][3]=0;
								subTotActualExpFunds[i][0]=subTotActualExpFunds[i][1]=subTotActualExpFunds[i][2]=subTotActualExpFunds[i][3]=0;
								subTotPlannedExpFunds[i][0]=subTotPlannedExpFunds[i][1]=subTotPlannedExpFunds[i][2]=subTotPlannedExpFunds[i][3]=0;
							}
						}

					}
					else
					{
						
						ahReport=getAdvancedReportRecords(ampActivities,activityIds,rsc,fromYr,toYr,perspective,fiscalCalId,ampCurrencyCode,ahReport,null,null);
						
						Iterator iterFund = ahReport.getFundSubTotal().iterator();
						if(rsc.getOption().equals(Constants.ANNUAL))
						{
							//logger.info("equal to annual");
							//for(int i=0;i<=yrCount ;i++ )
							for(int i=0;i<yrCount ;i++ )
							{
								//logger.info("in the for");
								//logger.info("i have come here" + iterFund.next().toString());
								AmpFund ampFund=(AmpFund) iterFund.next();
								//logger.info("after iteration"+ampFund.toString());
	
								if(i<yrCount)
								{
									totActualFunds[i][0]=totActualFunds[i][0] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getCommAmount()));
									totActualFunds[i][1]=totActualFunds[i][1] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getDisbAmount()));
									totActualFunds[i][2]=totActualFunds[i][2] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getExpAmount()));
									totPlannedFunds[i][0]=totPlannedFunds[i][0] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlCommAmount()));
									totPlannedFunds[i][1]=totPlannedFunds[i][1] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlDisbAmount()));
									totPlannedFunds[i][2]=totPlannedFunds[i][2] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlExpAmount()));
								}
								if(i==yrCount)						
								{
									actCommitTotal = actCommitTotal + Double.parseDouble(DecimalToText.removeCommas(ampFund.getCommAmount()));
									actDisbTotal = actDisbTotal + Double.parseDouble(DecimalToText.removeCommas(ampFund.getDisbAmount()));
									actExpTotal = actExpTotal + Double.parseDouble(DecimalToText.removeCommas(ampFund.getExpAmount()));
									planCommitTotal = planCommitTotal  + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlCommAmount()));
									planDisbTotal = planDisbTotal + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlDisbAmount()));
									planExpTotal = planExpTotal + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlExpAmount()));
									unDisbTotal = unDisbTotal + Double.parseDouble(DecimalToText.removeCommas(ampFund.getUnDisbAmount()));
								}
							}
						}
						else
						{
							for(int i=0;i<=yrCount ;i++ )
							{
								if(i<yrCount)
								{
									for(int qtr=0;qtr<4;qtr++)
									{
										AmpFund ampFund=(AmpFund) iterFund.next();
									
										totActualCommFunds[i][qtr]=totActualCommFunds[i][qtr] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getCommAmount()));
										totActualDisbFunds[i][qtr]=totActualDisbFunds[i][qtr] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getDisbAmount()));
										totActualExpFunds[i][qtr]=totActualExpFunds[i][qtr] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getExpAmount()));
										totPlannedCommFunds[i][qtr]=totPlannedCommFunds[i][qtr] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlCommAmount()));
										totPlannedDisbFunds[i][qtr]=totPlannedDisbFunds[i][qtr] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlDisbAmount()));
										totPlannedExpFunds[i][qtr]=totPlannedExpFunds[i][qtr] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlExpAmount()));
									}
								}
								
								if(i==yrCount)
								{	
									AmpFund ampFund=(AmpFund) iterFund.next();
									actCommitTotal = actCommitTotal + Double.parseDouble(DecimalToText.removeCommas(ampFund.getCommAmount()));
									actDisbTotal = actDisbTotal + Double.parseDouble(DecimalToText.removeCommas(ampFund.getDisbAmount()));
									actExpTotal = actExpTotal + Double.parseDouble(DecimalToText.removeCommas(ampFund.getExpAmount()));
									planCommitTotal = planCommitTotal  + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlCommAmount()));
									planDisbTotal = planDisbTotal + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlDisbAmount()));
									planExpTotal = planExpTotal + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlExpAmount()));
									unDisbTotal = unDisbTotal + Double.parseDouble(DecimalToText.removeCommas(ampFund.getUnDisbAmount()));
										
								}			
							}
						}
						if(ahReport.getProject().size()>0)
							mreport.getHierarchy().add(ahReport);
	
						totalCommitment=0;
						totalDisbursement=0;
						donors.clear();
						assistance.clear();
						assistanceCopy.clear();
						modality.clear();
						contacts.clear();
						sectors.clear();
						regions.clear();
						contactName=null;
						minYear=maxYear=0;

						sumUnDisb=actSumCommit=actSumDisb=actSumExp=planSumCommit=planSumDisb=planSumExp = 0;
						unDisbSubTotal=actCommitSubTotal=actDisbSubTotal=actExpSubTotal=planCommitSubTotal=planDisbSubTotal=planExpSubTotal = 0;
						for(int i=0;i<yrCount;i++)
						{
							actualFunds[i][0]=actualFunds[i][1]=actualFunds[i][2]=0;
							plannedFunds[i][0]=plannedFunds[i][1]=plannedFunds[i][2]=0;
							subTotActualFunds[i][0]=subTotActualFunds[i][1]=subTotActualFunds[i][2]=0;
							subTotPlannedFunds[i][0]=subTotPlannedFunds[i][1]=subTotPlannedFunds[i][2]=0;
							subTotActualCommFunds[i][0]=subTotActualCommFunds[i][1]=subTotActualCommFunds[i][2]=subTotActualCommFunds[i][3]=0;
							subTotPlannedCommFunds[i][0]=subTotPlannedCommFunds[i][1]=subTotPlannedCommFunds[i][2]=subTotPlannedCommFunds[i][3]=0;
							subTotActualDisbFunds[i][0]=subTotActualDisbFunds[i][1]=subTotActualDisbFunds[i][2]=subTotActualDisbFunds[i][3]=0;
							subTotPlannedDisbFunds[i][0]=subTotPlannedDisbFunds[i][1]=subTotPlannedDisbFunds[i][2]=subTotPlannedDisbFunds[i][3]=0;
							subTotActualExpFunds[i][0]=subTotActualExpFunds[i][1]=subTotActualExpFunds[i][2]=subTotActualExpFunds[i][3]=0;
							subTotPlannedExpFunds[i][0]=subTotPlannedExpFunds[i][1]=subTotPlannedExpFunds[i][2]=subTotPlannedExpFunds[i][3]=0;
						}
				
					} //End of else
					
				}//End of iteration of levels
				//logger.debug("Level 1 size: " + mreport.getHierarchy().size());
				mreport.setFundTotal(new ArrayList());
				if(rsc.getOption().equals(Constants.ANNUAL))
				{
					for(int i=0;i<yrCount;i++)
					{
						AmpFund ampFund=new AmpFund();
						if(measures.indexOf(new Long(1))!=-1)
							ampFund.setCommAmount(mf.format(totActualFunds[i][0])); 
						if(measures.indexOf(new Long(2))!=-1)
							ampFund.setDisbAmount(mf.format(totActualFunds[i][1])); 
						if(measures.indexOf(new Long(3))!=-1)
							ampFund.setExpAmount(mf.format(totActualFunds[i][2]));	
						if(measures.indexOf(new Long(4))!=-1)
							ampFund.setPlCommAmount(mf.format(totPlannedFunds[i][0])); 
						if(measures.indexOf(new Long(5))!=-1)
							ampFund.setPlDisbAmount(mf.format(totPlannedFunds[i][1])); 
						if(measures.indexOf(new Long(6))!=-1)
							ampFund.setPlExpAmount(mf.format(totPlannedFunds[i][2]));
						mreport.getFundTotal().add(ampFund);
					}						
					AmpFund fund = new AmpFund();
					fund.setCommAmount(mf.format(actCommitTotal));
					fund.setDisbAmount(mf.format(actDisbTotal));
					fund.setExpAmount(mf.format(actExpTotal));
					fund.setPlCommAmount(mf.format(planCommitTotal));
					fund.setPlDisbAmount(mf.format(planDisbTotal));
					fund.setPlExpAmount(mf.format(planExpTotal));
					fund.setUnDisbAmount(mf.format(unDisbTotal));
					mreport.getFundTotal().add(fund) ;	
				}
				else
				{
					for(int i=0;i<yrCount;i++)
					{
						for(int qtr=0;qtr<4;qtr++)
						{
							AmpFund ampFund=new AmpFund();
							if(measures.indexOf(new Long(1))!=-1)
								ampFund.setCommAmount(mf.format(totActualCommFunds[i][qtr])); 
							if(measures.indexOf(new Long(2))!=-1)
								ampFund.setDisbAmount(mf.format(totActualDisbFunds[i][qtr])); 
							if(measures.indexOf(new Long(3))!=-1)
								ampFund.setExpAmount(mf.format(totActualExpFunds[i][qtr]));	
							if(measures.indexOf(new Long(4))!=-1)
								ampFund.setPlCommAmount(mf.format(totPlannedCommFunds[i][qtr])); 
							if(measures.indexOf(new Long(5))!=-1)
								ampFund.setPlDisbAmount(mf.format(totPlannedDisbFunds[i][qtr])); 
							if(measures.indexOf(new Long(6))!=-1)
								ampFund.setPlExpAmount(mf.format(totPlannedExpFunds[i][qtr]));
							mreport.getFundTotal().add(ampFund);
						}
					}						
					AmpFund fund = new AmpFund();
					fund.setCommAmount(mf.format(actCommitTotal));
					fund.setDisbAmount(mf.format(actDisbTotal));
					fund.setExpAmount(mf.format(actExpTotal));
					fund.setPlCommAmount(mf.format(planCommitTotal));
					fund.setPlDisbAmount(mf.format(planDisbTotal));
					fund.setPlExpAmount(mf.format(planExpTotal));
					fund.setUnDisbAmount(mf.format(unDisbTotal));
					mreport.getFundTotal().add(fund) ;	
				}

				ampReports.add(mreport);
			}
		}
		catch(Exception ex) 		
		{
			logger.error("Unable to get report names  from database :" + ex.getMessage());
			logger.error(ex);
			ex.printStackTrace();
			
		}
		finally 
		{
			try 
			{
				PersistenceManager.releaseSession(session);
			}
			catch (Exception ex2) 
			{
				logger.debug("releaseSession() failed ");
			}
		}
		return ampReports ;
	}


	private static void setHSectorInfo(ArrayList level,ArrayList activityIds,Long ampSectorId, Collection ampReports,
			String teamClause,ArrayList approvedActivityList) throws HibernateException, SQLException {
		Iterator iter=null;
		Session session = null;
		Long All=new Long(0);

	session = PersistenceManager.getSession();
	String queryString = "select activity from "
		+ AmpActivity.class.getName()
		+ " activity where (activity.team.ampTeamId in(" + teamClause + "))";

	Query qry = session.createQuery(queryString);
	//logger.debug("Query: " + queryString);
	iter=qry.list().iterator();
	while(iter.hasNext())
	{
		AmpActivity ampActivity=(AmpActivity) iter.next();
		if(approvedActivityList.indexOf(ampActivity.getAmpActivityId())==-1)
			continue;
		activityIds.add(ampActivity.getAmpActivityId());
	}

	queryString = "select sector from "
		+ AmpReportSector.class.getName()
		+ " sector order by sector.sectorName";
	qry = session.createQuery(queryString);
	iter=qry.list().iterator();
	AdvancedHierarchyReport ahReport=null;
	while(iter.hasNext())
	{
		AmpReportSector sector=(AmpReportSector) iter.next();
		if(!ampSectorId.equals(All))
		{
			AmpSector ampSector=DbUtil.getAmpParentSector(ampSectorId);
			if(!(sector.getAmpSectorId().equals(ampSector.getAmpSectorId())))
				continue;
		}
		if(ahReport==null || !(ahReport.getId().equals(sector.getAmpSectorId())))
		{
			if(ahReport!=null)
			{
				if(ahReport.getActivities().size()>0)
					ampReports.add(ahReport);
			}
			ahReport= new AdvancedHierarchyReport();
			ahReport.setId(sector.getAmpSectorId());
			ahReport.setName(sector.getSectorName());
			ahReport.setLabel("Sector ");
			ahReport.setActivities(new ArrayList());
				
		}
		if(activityIds.indexOf(sector.getAmpActivityId())!=-1)
			ahReport.getActivities().add(sector.getAmpActivityId());
	}
	if(ahReport.getActivities().size()>0)
		ampReports.add(ahReport);

	}
	
	
	private static void setHDonorInfo(ArrayList level,ArrayList activityIds,Long ampDonorId, Collection ampReports,
			String teamClause,ArrayList approvedActivityList) throws HibernateException, SQLException {
		Iterator iter=null;
		Iterator iterActivity=null;
		Session session = null;
		Long All=new Long(0);
		
		level=DbUtil.getAmpDonors(teamClause);
		iter=level.iterator();
		while(iter.hasNext())
		{
			AmpOrganisation ampOrganisation=(AmpOrganisation) iter.next();
			if(!(ampDonorId.equals(All)))
			{
				if(!(ampDonorId.equals(ampOrganisation.getAmpOrgId())))
					continue;
			}
			session = PersistenceManager.getSession();
			String queryString = "select distinct activity from "
				+ AmpReportCache.class.getName()
				+ " activity where (activity.ampTeamId in(" + teamClause + ")) and (activity.ampDonorId='" + ampOrganisation.getAmpOrgId() + "') and (activity.reportType='1')";

			Query qry = session.createQuery(queryString);
			//logger.debug("Query: " + queryString);
			if(qry.list().size()>0)
			{
				AdvancedHierarchyReport ahReport= new AdvancedHierarchyReport();
				ahReport.setId(ampOrganisation.getAmpOrgId());
				ahReport.setName(ampOrganisation.getName());
				ahReport.setLabel("Donor ");
				iterActivity = qry.list().iterator();
				ahReport.setActivities(new ArrayList());
				while(iterActivity.hasNext())
				{
					AmpReportCache ampReportCache=(AmpReportCache) iterActivity.next();
					if(approvedActivityList.indexOf(ampReportCache.getAmpActivityId())==-1)
						continue;
					ahReport.getActivities().add(ampReportCache.getAmpActivityId());
				}
				ampReports.add(ahReport);
			}
		}	
	
	}


	private static void setHFundingInstrumentInfo(ArrayList level,ArrayList activityIds,Long ampModalityId, Collection ampReports,
			String teamClause,ArrayList approvedActivityList) throws HibernateException, SQLException {
		Iterator iter=null;
		Iterator iterActivity=null;
		Session session = null;
		Long All=new Long(0);

		
	level=DbUtil.getAmpModality();
	iter=level.iterator();
	while(iter.hasNext())
	{
		AmpModality ampModality=(AmpModality) iter.next();
		if(!(ampModalityId.equals(All)))
		{
			if(!(ampModalityId.equals(ampModality.getAmpModalityId())))
				continue;
		}
		session = PersistenceManager.getSession();
		String queryString = "select distinct activity from "
			+ AmpReportCache.class.getName()
			+ " activity where (activity.ampTeamId in(" + teamClause + ")) and (activity.ampModalityId='" + ampModality.getAmpModalityId() + "') and (activity.reportType='1')";

		Query qry = session.createQuery(queryString);
		//logger.debug("Query: " + queryString);
		if(qry.list().size()>0)
		{
			AdvancedHierarchyReport ahReport= new AdvancedHierarchyReport();
			ahReport.setId(ampModality.getAmpModalityId());
			ahReport.setName(ampModality.getName());
			ahReport.setLabel("Funding Instrument ");
			iterActivity = qry.list().iterator();
			ahReport.setActivities(new ArrayList());
			while(iterActivity.hasNext())
			{
				AmpReportCache ampReportCache=(AmpReportCache) iterActivity.next();
				if(approvedActivityList.indexOf(ampReportCache.getAmpActivityId())==-1)
					continue;
				ahReport.getActivities().add(ampReportCache.getAmpActivityId());
			}
			ampReports.add(ahReport);
		}
	}	
	}
	
	
	private static void setHTermsAssistInfo(ArrayList level,ArrayList activityIds,Collection ampReports,
			String teamClause,ArrayList approvedActivityList) throws HibernateException, SQLException {
	
		Iterator iter=null;
		Iterator iterActivity=null;
		Session session = null;
		Long All=new Long(0);
	
	level=(ArrayList)DbUtil.getAllAssistanceTypes();
	iter=level.iterator();
	while(iter.hasNext())
	{
		AmpTermsAssist ampTermsAssist=(AmpTermsAssist) iter.next();
		session = PersistenceManager.getSession();
		String queryString = "select distinct activity from "
			+ AmpReportCache.class.getName()
			+ " activity where (activity.ampTeamId in(" + teamClause + ")) and (activity.termAssistName='" + ampTermsAssist.getTermsAssistName() + "') and (activity.reportType='1')";

		Query qry = session.createQuery(queryString);
		//logger.debug("Query: " + queryString);
		if(qry.list().size()>0)
		{
			AdvancedHierarchyReport ahReport= new AdvancedHierarchyReport();
			ahReport.setId(ampTermsAssist.getAmpTermsAssistId());
			ahReport.setName(ampTermsAssist.getTermsAssistName());
			ahReport.setLabel("Type Of Assistance ");
			iterActivity = qry.list().iterator();
			ahReport.setActivities(new ArrayList());
			while(iterActivity.hasNext())
			{
				AmpReportCache ampReportCache=(AmpReportCache) iterActivity.next();
				if(approvedActivityList.indexOf(ampReportCache.getAmpActivityId())==-1)
					continue;
				ahReport.getActivities().add(ampReportCache.getAmpActivityId());
			}
			ampReports.add(ahReport);
		}
		
		}
	}
	
	private static void setHStatusInfo(ArrayList level,ArrayList activityIds,Long ampStatusId, Collection ampReports,
			String teamClause,ArrayList approvedActivityList) throws HibernateException, SQLException {
		Iterator iter=null;
		Iterator iterActivity=null;
		Session session = null;
		Long All=new Long(0);
		
		
	level=DbUtil.getAmpStatus();
	iter=level.iterator();
	 
	while(iter.hasNext())
	{
		AmpStatus ampStatus=(AmpStatus) iter.next();
		if(!(ampStatusId.equals(All)))
		{
			if(!(ampStatusId.equals(ampStatus.getAmpStatusId())))
				continue;
		}
		session = PersistenceManager.getSession();
		String queryString = "select activity from "
			+ AmpActivity.class.getName()
			+ " activity where (activity.team.ampTeamId in(" + teamClause + ")) and (activity.status.ampStatusId='" + ampStatus.getAmpStatusId() + "')";

		Query qry = session.createQuery(queryString);
		//logger.debug("Query: " + queryString);
		if(qry.list().size()>0)
		{
			AdvancedHierarchyReport ahReport= new AdvancedHierarchyReport();
			ahReport.setId(ampStatus.getAmpStatusId());
			ahReport.setName(ampStatus.getName());
			ahReport.setLabel("Status ");
			iterActivity = qry.list().iterator();
			ahReport.setActivities(new ArrayList());
			while(iterActivity.hasNext())
			{
				AmpActivity ampActivity=(AmpActivity) iterActivity.next();
				if(approvedActivityList.indexOf(ampActivity.getAmpActivityId())==-1)
					continue;
				ahReport.getActivities().add(ampActivity.getAmpActivityId());
			}
			ampReports.add(ahReport);
		}
	}
	
	}

	private static void setHComponentInfo(ArrayList level,ArrayList activityIds, String component, Collection ampReports,
			String teamClause,ArrayList approvedActivityList) throws HibernateException, SQLException {

		Iterator iter=null;
		Iterator iterActivity=null;
		Session session = null;
	

		
		level=DbUtil.getAmpComponents();
		iter=level.iterator();
		while(iter.hasNext())
		{
			AmpComponent ampComponent=(AmpComponent) iter.next();
			if(!(component.equals("All")))
			{
				if(!(component.equals(ampComponent.getTitle())))
					continue;
			}
			session = PersistenceManager.getSession();
			String queryString = "select distinct activity from "
				+ AmpReportCache.class.getName()
				+ " activity where (activity.ampTeamId in(" + teamClause + ")) and (activity.ampComponentId='" + ampComponent.getAmpComponentId() + "') and (activity.reportType='2')";

			Query qry = session.createQuery(queryString);
			//logger.debug("Query: " + queryString);
			if(qry.list().size()>0)
			{
				AdvancedHierarchyReport ahReport= new AdvancedHierarchyReport();
				ahReport.setId(ampComponent.getAmpComponentId());
				ahReport.setName(ampComponent.getTitle());
				ahReport.setLabel("Component Name ");
				iterActivity = qry.list().iterator();
				ahReport.setActivities(new ArrayList());
				while(iterActivity.hasNext())
				{
					AmpReportCache ampReportCache=(AmpReportCache) iterActivity.next();
					if(approvedActivityList.indexOf(ampReportCache.getAmpActivityId())==-1)
						continue;
					ahReport.getActivities().add(ampReportCache.getAmpActivityId());
				}
				ampReports.add(ahReport);
			}
		}

		
	}

	private static void setHActivityNameInfo(ArrayList level,ArrayList activityIds,List projTitles, Collection ampReports,
			String teamClause,ArrayList approvedActivityList) throws HibernateException, SQLException {
		
		AdvancedHierarchyReport ahReport = null;
		Iterator iterActivity=null;
		Session session = null;
	
		session = PersistenceManager.getSession();
		String queryString = "select activity from "
			+ AmpActivity.class.getName()
			+ " activity where activity.approvalStatus='approved' and activity.team.ampTeamId in (" + teamClause + ") " ;

		
		Query qry = session.createQuery(queryString);
		projTitles = qry.list();
		logger.debug("Query: " + queryString);
		if(projTitles.size()>0)
		{
			iterActivity = projTitles.iterator();
			while(iterActivity.hasNext())
			{
				AmpActivity ampActivity=(AmpActivity) iterActivity.next();
				ahReport= new AdvancedHierarchyReport();
				ahReport.setActivities(new ArrayList());
				ahReport.setId(ampActivity.getAmpActivityId());
				ahReport.setName(ampActivity.getName());
				ahReport.setLabel("Project Title ");
				ahReport.getActivities().add(ampActivity.getAmpActivityId());
				ampReports.add(ahReport);
			}
		}	
	}
	
	private static void setHRegionInfo(ArrayList level,ArrayList activityIds, String region, Collection ampReports,
			String teamClause,ArrayList approvedActivityList) throws HibernateException, SQLException {

		Iterator iter=null;
		Iterator iterActivity=null;
		Session session = null;
		Long All=new Long(0);

		level=DbUtil.getAmpLocations();
		iter=level.iterator();
		while(iter.hasNext())
		{
			AmpRegion ampRegion=(AmpRegion) iter.next();
			if(!(region.equals("All")))
			{
				if(!(region.equals(ampRegion.getName())))
					continue;
			}
			session = PersistenceManager.getSession();
			String queryString = "select distinct activity from "
				+ AmpReportCache.class.getName()
				+ " activity where (activity.ampTeamId in(" + teamClause + ")) and (activity.ampRegionId='" + ampRegion.getAmpRegionId() + "') and (activity.reportType='3')";

			Query qry = session.createQuery(queryString);
			//logger.debug("Query: " + queryString);
			if(qry.list().size()>0)
			{
				AdvancedHierarchyReport ahReport= new AdvancedHierarchyReport();
				ahReport.setId(ampRegion.getAmpRegionId());
				ahReport.setName(ampRegion.getName());
				ahReport.setLabel("Region ");
				iterActivity = qry.list().iterator();
				ahReport.setActivities(new ArrayList());
				while(iterActivity.hasNext())
				{
					AmpReportCache ampReportCache=(AmpReportCache) iterActivity.next();
					if(approvedActivityList.indexOf(ampReportCache.getAmpActivityId())==-1)
						continue;
					ahReport.getActivities().add(ampReportCache.getAmpActivityId());
				}
				ampReports.add(ahReport);
			}
		}

	}
	
	public static ArrayList getLevel1AdvancedReport(String teamClause,Long ampColumnId,Long ampStatusId,
			Long ampDonorId,Long ampModalityId,Long ampSectorId,String region,String component, 
			ArrayList approvedActivityList) 
	{
		ArrayList level=new ArrayList();
		ArrayList activityIds=new ArrayList();
		ArrayList ampReports=new ArrayList();
		List projTitles = new ArrayList();
		
		try 
		{
			if(ampColumnId.equals(Constants.STATUS_NAME))
				setHStatusInfo(level,activityIds,ampStatusId,ampReports,teamClause,approvedActivityList);			
		
			if(ampColumnId.equals(Constants.DONOR_NAME))
				setHDonorInfo(level,activityIds,ampDonorId,ampReports,teamClause,approvedActivityList);	
				
			if(ampColumnId.equals(Constants.FUNDING_INSTRUMENT))
				setHFundingInstrumentInfo(level,activityIds,ampModalityId,ampReports,teamClause,approvedActivityList);	
			
			if(ampColumnId.equals(Constants.SECTOR_NAME))
				setHSectorInfo(level,activityIds,ampSectorId,ampReports,teamClause,approvedActivityList);

			if(ampColumnId.equals(Constants.TERM_ASSIST_NAME))
				setHTermsAssistInfo(level,activityIds,ampReports,teamClause,approvedActivityList);

			if(ampColumnId.equals(Constants.REGION_NAME))
				setHRegionInfo(level,activityIds,region, ampReports,teamClause,approvedActivityList);
			
			
			if(ampColumnId.equals(Constants.COMPONENT_NAME))
				setHComponentInfo(level,activityIds,component,ampReports,teamClause,approvedActivityList);			
			
			
//			start code block for Project title column
			if(ampColumnId.equals(Constants.ACTIVITY_NAME))
				setHActivityNameInfo(level,activityIds,projTitles,ampReports,teamClause,approvedActivityList);
			
// 			end code block for project-title column
			
					
		} catch(Exception e)
		{
			e.printStackTrace(System.out);
		}

		return ampReports;
	}

	public static ArrayList getLevel2AdvancedReport(String teamClause,Long ampColumnId1,Long ampColumnId2,Long ampStatusId,Long ampDonorId,Long ampModalityId,Long ampSectorId,String region,String component, ArrayList approvedActivityList) 
	{
		Session session = null;
		Iterator iterLevel=null;
		Iterator iterActivity=null;
		ArrayList level=new ArrayList();
		ArrayList activityIds=new ArrayList();
		ArrayList ampReports=new ArrayList();
		ArrayList ahReports=new ArrayList();
		String inClause=null;
		AdvancedHierarchyReport ahReportLevel2=null;
		List projTitles = new ArrayList();
		
		try 
		{

			ahReports=getLevel1AdvancedReport(teamClause,ampColumnId1,ampStatusId,ampDonorId,ampModalityId,ampSectorId,region,component, approvedActivityList);
			iterLevel=ahReports.iterator();
			while(iterLevel.hasNext())
			{
				AdvancedHierarchyReport ahReportLevel1=(AdvancedHierarchyReport) iterLevel.next();
				ahReportLevel2=new AdvancedHierarchyReport();
				ahReportLevel2.setId(ahReportLevel1.getId());
				ahReportLevel2.setName(ahReportLevel1.getName());
				ahReportLevel2.setLabel(ahReportLevel1.getLabel());
				ahReportLevel2.setActivities(ahReportLevel1.getActivities());
				iterActivity=ahReportLevel1.getActivities().iterator();
				while(iterActivity.hasNext())
				{
					Long id=(Long) iterActivity.next();
					if(inClause==null)
						inClause="'" + id + "'";
					else
						inClause=inClause + ",'" + id + "'";
				}
				ahReportLevel2.setLevels(new ArrayList());

				if(ampColumnId2.equals(Constants.STATUS_NAME))
					setHStatusInfo(level,activityIds,ampStatusId,ahReportLevel2.getLevels(),teamClause,approvedActivityList);			
			
				if(ampColumnId2.equals(Constants.DONOR_NAME))
					setHDonorInfo(level,activityIds,ampDonorId,ahReportLevel2.getLevels(),teamClause,approvedActivityList);	
					
				if(ampColumnId2.equals(Constants.FUNDING_INSTRUMENT))
					setHFundingInstrumentInfo(level,activityIds,ampModalityId,ahReportLevel2.getLevels(),teamClause,approvedActivityList);	
				
				if(ampColumnId2.equals(Constants.SECTOR_NAME))
					setHSectorInfo(level,activityIds,ampSectorId,ahReportLevel2.getLevels(),teamClause,approvedActivityList);

				if(ampColumnId2.equals(Constants.TERM_ASSIST_NAME))
					setHTermsAssistInfo(level,activityIds,ahReportLevel2.getLevels(),teamClause,approvedActivityList);

				if(ampColumnId2.equals(Constants.REGION_NAME))
					setHRegionInfo(level,activityIds,region, ahReportLevel2.getLevels(),teamClause,approvedActivityList);
				
				
				if(ampColumnId2.equals(Constants.COMPONENT_NAME))
					setHComponentInfo(level,activityIds,component,ahReportLevel2.getLevels(),teamClause,approvedActivityList);			
				
				
//				start code block for Project title column
				if(ampColumnId2.equals(Constants.ACTIVITY_NAME))
					setHActivityNameInfo(level,activityIds,projTitles,ahReportLevel2.getLevels(),teamClause,approvedActivityList);

				
				
				
				ampReports.add(ahReportLevel2);
				inClause=null;
			}
			
		} 
		catch(Exception e)
		{
			e.printStackTrace(System.out);
		}

		finally 
		{
			try 
			{
				PersistenceManager.releaseSession(session);
			}
			catch (Exception ex2) 
			{
				logger.debug("releaseSession() failed ");
			}
		}
		return ampReports;
	}
	
	public static void quarterlyFundingCompute(int yrCount,ArrayList measures,
			double[][] actualCommFunds, 
			double[][] actualDisbFunds,
			double[][] actualExpFunds, 
			double[][] plannedCommFunds, 
			double[][] plannedDisbFunds,
			double[][] plannedExpFunds, 
			AmpByAssistTypeList[][] actualCommTerms,
			AmpByAssistTypeList[][] actualDisbTerms,
			AmpByAssistTypeList[][] actualExpTerms,
			AmpByAssistTypeList[][] plannedCommTerms,
			AmpByAssistTypeList[][] plannedDisbTerms,
			AmpByAssistTypeList[][] plannedExpTerms,			
			double actSumCommit, 
			 double actSumDisb,double actSumExp, double planSumCommit,double planSumDisb,double planSumExp,
			 AmpByAssistTypeList actSumCommitTerms, AmpByAssistTypeList actSumDisbTerms, AmpByAssistTypeList actSumExpTerms,
			 AmpByAssistTypeList planSumCommitTerms, AmpByAssistTypeList planSumDisbTerms, AmpByAssistTypeList planSumExpTerms,
			 double totalCommitment,double totalDisbursement,AdvancedReport report, Report reports,
			 
			 double[][] subTotActualCommFunds,
			 double[][] subTotActualDisbFunds,
			 double[][] subTotActualExpFunds,
			 
			 double[][] subTotPlannedCommFunds,
			 double[][] subTotPlannedDisbFunds,
			 double[][] subTotPlannedExpFunds,
			 double actCommitSubTotal, 
			 double actDisbSubTotal ,
			 double actExpSubTotal , 
			 double planCommitSubTotal,
			 double planDisbSubTotal,
			 double planExpSubTotal,
			 double unDisbSubTotal,
			 boolean computeSubTotal)
	{
	
	for(int i=0;i<yrCount;i++)
	{
		for(int qtr=0;qtr<4;qtr++)
		{
			AmpFund ampFund=new AmpFund();
			if(measures.indexOf(new Long(1))>=0) {
				ampFund.setCommAmount(mf.format(actualCommFunds[i][qtr]));
				ampFund.setByTypeComm(actualCommTerms[i][qtr]);
			}
			if(measures.indexOf(new Long(2))>=0) {
				ampFund.setDisbAmount(mf.format(actualDisbFunds[i][qtr]));
				ampFund.setByTypeDisb(actualDisbTerms[i][qtr]);
			}
			if(measures.indexOf(new Long(3))>=0) {
				ampFund.setExpAmount(mf.format(actualExpFunds[i][qtr]));
				ampFund.setByTypeExp(actualExpTerms[i][qtr]);
			}
			if(measures.indexOf(new Long(4))>=0) {
				ampFund.setPlCommAmount(mf.format(plannedCommFunds[i][qtr]));
				ampFund.setByTypePlComm(plannedCommTerms[i][qtr]);
			}
			if(measures.indexOf(new Long(5))>=0) {
				ampFund.setPlDisbAmount(mf.format(plannedDisbFunds[i][qtr]));
				ampFund.setByTypePlDisb(plannedDisbTerms[i][qtr]);
			}
			if(measures.indexOf(new Long(6))>=0) {
				ampFund.setPlExpAmount(mf.format(plannedExpFunds[i][qtr]));
				ampFund.setByTypePlExp(plannedExpTerms[i][qtr]);
			}
												
			actSumCommit = actSumCommit + actualCommFunds[i][qtr];
			actSumDisb = actSumDisb + actualDisbFunds[i][qtr];
			actSumExp = actSumExp + actualExpFunds[i][qtr];
			planSumCommit = planSumCommit + plannedCommFunds[i][qtr];
			planSumDisb = planSumDisb + plannedDisbFunds[i][qtr];
			planSumExp = planSumExp + plannedExpFunds[i][qtr];
//			sumUnDisb = sumUnDisb + (actualCommFunds[i][qtr]-actualDisbFunds[i][qtr]);

			actSumCommitTerms.addAll(actualCommTerms[i][qtr]);
			actSumDisbTerms.addAll(actualDisbTerms[i][qtr]);
			actSumExpTerms.addAll(actualExpTerms[i][qtr]);
			
			planSumCommitTerms.addAll(plannedCommTerms[i][qtr]);
			planSumDisbTerms.addAll(plannedDisbTerms[i][qtr]);
			planSumExpTerms.addAll(plannedExpTerms[i][qtr]);
			
			if (computeSubTotal) {
										
			subTotActualCommFunds[i][qtr] = subTotActualCommFunds[i][qtr] + actualCommFunds[i][qtr];
			subTotActualDisbFunds[i][qtr] = subTotActualDisbFunds[i][qtr] + actualDisbFunds[i][qtr];
			subTotActualExpFunds[i][qtr] = subTotActualExpFunds[i][qtr] + actualExpFunds[i][qtr];
			subTotPlannedCommFunds[i][qtr] = subTotPlannedCommFunds[i][qtr] + plannedCommFunds[i][qtr];
			subTotPlannedDisbFunds[i][qtr] = subTotPlannedDisbFunds[i][qtr] + plannedDisbFunds[i][qtr];
			subTotPlannedExpFunds[i][qtr] = subTotPlannedExpFunds[i][qtr] + plannedExpFunds[i][qtr];
		
			}
			
			report.getAmpFund().add(ampFund);
		}	
	}
	
	
	if (computeSubTotal) {
		
	
	actCommitSubTotal = actCommitSubTotal + actSumCommit;
	actDisbSubTotal = actDisbSubTotal + actSumDisb;
	actExpSubTotal = actExpSubTotal + actSumExp;
	planCommitSubTotal = planCommitSubTotal + planSumCommit;
	planDisbSubTotal = planDisbSubTotal + planSumDisb;
	planExpSubTotal = planExpSubTotal + planSumExp;
	unDisbSubTotal = unDisbSubTotal + (totalCommitment-totalDisbursement);

	}
	
	AmpFund fund = new AmpFund();
	
	fund.setByTypeComm(actSumCommitTerms);
	fund.setByTypeDisb(actSumDisbTerms);
	fund.setByTypeExp(actSumExpTerms);
	
	fund.setByTypePlComm(planSumCommitTerms);
	fund.setByTypePlDisb(planSumDisbTerms);
	fund.setByTypePlExp(planSumExpTerms);

	AmpByAssistTypeList unDisbTerm = new AmpByAssistTypeList();
	unDisbTerm.addAll(fund.getByTypeComm());
	unDisbTerm.addAll(fund.getByTypePlComm());
	
	unDisbTerm.removeAll(fund.getByTypeDisb());
	unDisbTerm.removeAll(fund.getByTypePlDisb());
	fund.setByTypeUnDisb(unDisbTerm);
	
	fund.setCommAmount(mf.format(actSumCommit));
	fund.setDisbAmount(mf.format(actSumDisb));
	fund.setExpAmount(mf.format(actSumExp));
	fund.setPlCommAmount(mf.format(planSumCommit));
	fund.setPlDisbAmount(mf.format(planSumDisb));
	fund.setPlExpAmount(mf.format(planSumExp));
	fund.setUnDisbAmount(mf.format(totalCommitment-totalDisbursement));
	report.getAmpFund().add(fund) ;	
	//logger.debug("Fund Size: " + report.getAmpFund().size());			
	reports.getRecords().add(report);
}

	public static void annualFundingCompute(int yrCount,ArrayList measures,double[][] actualFunds, double[][] plannedFunds,
			AmpByAssistTypeList[][] actualTerms, AmpByAssistTypeList[][] plannedTerms,double actSumCommit, 
			 double actSumDisb,double actSumExp, double planSumCommit,double planSumDisb,double planSumExp,
			 AmpByAssistTypeList actSumCommitTerms, AmpByAssistTypeList actSumDisbTerms, AmpByAssistTypeList actSumExpTerms,
			 AmpByAssistTypeList planSumCommitTerms, AmpByAssistTypeList planSumDisbTerms, AmpByAssistTypeList planSumExpTerms,
			 double totalCommitment,double totalDisbursement,AdvancedReport report, Report reports,
			 double[][] subTotActualFunds,
			 double[][] subTotPlannedFunds,
			 double actCommitSubTotal, 
			 double actDisbSubTotal ,
			 double actExpSubTotal , 
			 double planCommitSubTotal,
			 double planDisbSubTotal,
			 double planExpSubTotal,
			 double unDisbSubTotal,
			 boolean computeSubTotal) {		
		
			
			
			for(int i=0;i<yrCount;i++)
			{
				AmpFund ampFund=new AmpFund();
				if(measures.indexOf(new Long(1))!=-1){
					ampFund.setCommAmount(mf.format(actualFunds[i][0]));
					ampFund.setByTypeComm(actualTerms[i][0]);
				}
				
				if(measures.indexOf(new Long(2))!=-1) {
					ampFund.setDisbAmount(mf.format(actualFunds[i][1]));
					ampFund.setByTypeDisb(actualTerms[i][1]);
				}
				if(measures.indexOf(new Long(3))!=-1) {
					ampFund.setExpAmount(mf.format(actualFunds[i][2]));
					ampFund.setByTypeExp(actualTerms[i][2]);
				}
				if(measures.indexOf(new Long(4))!=-1) {
					ampFund.setPlCommAmount(mf.format(plannedFunds[i][0]));
					ampFund.setByTypePlComm(plannedTerms[i][0]);
				}
				if(measures.indexOf(new Long(5))!=-1) {
					ampFund.setPlDisbAmount(mf.format(plannedFunds[i][1]));
					ampFund.setByTypePlDisb(plannedTerms[i][1]);
				}
				if(measures.indexOf(new Long(6))!=-1) {
					ampFund.setPlExpAmount(mf.format(plannedFunds[i][2]));
					ampFund.setByTypePlExp(plannedTerms[i][2]);
				}
	
				
				//if(measures.indexOf(new Long(7))!=-1)
				//	ampFund.setUnDisbAmount(mf.format(actualFunds[i][0]-actualFunds[i][1]));	
			
				
				//summing(last column in report table)
				actSumCommitTerms.addAll(actualTerms[i][0]);
				actSumDisbTerms.addAll(actualTerms[i][1]);
				actSumExpTerms.addAll(actualTerms[i][2]);
				
				planSumCommitTerms.addAll(plannedTerms[i][0]);
				planSumDisbTerms.addAll(plannedTerms[i][1]);
				planSumExpTerms.addAll(plannedTerms[i][2]);
				
				
				actSumCommit = actSumCommit + actualFunds[i][0];
				actSumDisb = actSumDisb + actualFunds[i][1];
				actSumExp = actSumExp + actualFunds[i][2];
				planSumCommit = planSumCommit + plannedFunds[i][0];
				planSumDisb = planSumDisb + plannedFunds[i][1];
				planSumExp = planSumExp + plannedFunds[i][2];
//				sumUnDisb = sumUnDisb + (actualFunds[i][0]-actualFunds[i][1]);
				
				if(computeSubTotal) {
				
				subTotActualFunds[i][0] = subTotActualFunds[i][0] + actualFunds[i][0];
				subTotActualFunds[i][1] = subTotActualFunds[i][1] + actualFunds[i][1];
				subTotActualFunds[i][2] = subTotActualFunds[i][2] + actualFunds[i][2];
				subTotPlannedFunds[i][0] = subTotPlannedFunds[i][0] + plannedFunds[i][0];
				subTotPlannedFunds[i][1] = subTotPlannedFunds[i][1] + plannedFunds[i][1];
				subTotPlannedFunds[i][2] = subTotPlannedFunds[i][2] + plannedFunds[i][2];
				}
				
				report.getAmpFund().add(ampFund);
			}
			
			
			if (computeSubTotal) {
			actCommitSubTotal = actCommitSubTotal + actSumCommit;
			actDisbSubTotal = actDisbSubTotal + actSumDisb;
			actExpSubTotal = actExpSubTotal + actSumExp;
			planCommitSubTotal = planCommitSubTotal + planSumCommit;
			planDisbSubTotal = planDisbSubTotal + planSumDisb;
			planExpSubTotal = planExpSubTotal + planSumExp;
			unDisbSubTotal = unDisbSubTotal + (totalCommitment-totalDisbursement);
			}
		
			
			AmpFund fund = new AmpFund();
			
			fund.setByTypeComm(actSumCommitTerms);
			fund.setByTypeDisb(actSumDisbTerms);
			fund.setByTypeExp(actSumExpTerms);
			
			fund.setByTypePlComm(planSumCommitTerms);
			fund.setByTypePlDisb(planSumDisbTerms);
			fund.setByTypePlExp(planSumExpTerms);
			
			
			AmpByAssistTypeList unDisbTerm = new AmpByAssistTypeList();
			unDisbTerm.addAll(fund.getByTypeComm());
			unDisbTerm.addAll(fund.getByTypePlComm());
			
			unDisbTerm.removeAll(fund.getByTypeDisb());
			unDisbTerm.removeAll(fund.getByTypePlDisb());
			fund.setByTypeUnDisb(unDisbTerm);
			
			
			fund.setCommAmount(mf.format(actSumCommit));
			fund.setDisbAmount(mf.format(actSumDisb));
			fund.setExpAmount(mf.format(actSumExp));
			fund.setPlCommAmount(mf.format(planSumCommit));
			fund.setPlDisbAmount(mf.format(planSumDisb));
			fund.setPlExpAmount(mf.format(planSumExp));
			fund.setUnDisbAmount(mf.format(totalCommitment-totalDisbursement));
			report.getAmpFund().add(fund) ;	
			
			reports.getRecords().add(report);
			//logger.debug("Reports Size: " + reports.getRecords().size());
			//logger.debug("Fund Size: " + report.getAmpFund().size());
		
	}

	public static AdvancedHierarchyReport getAdvancedReportRecords(ArrayList ampActivities,ArrayList activityIds,ReportSelectionCriteria rsc,int fromYr,int toYr,String perspective,int fiscalCalId,String ampCurrencyCode,AdvancedHierarchyReport ahReport, AdvancedHierarchyReport ahReport1, AdvancedHierarchyReport ahReport2 )
	{
		Session session = null ;
		Collection columns=new ArrayList();
		ArrayList measures=new ArrayList();
		Report reports=null;
		Iterator iter=null;
		Iterator iterColumn=null;
		double totalCommitment=0;
		double totalDisbursement=0;
		int yrCount=(toYr-fromYr) +1;
		ArrayList donors=new ArrayList();
		ArrayList sectors=new ArrayList();
		ArrayList assistance=new ArrayList();
		ArrayList regions=new ArrayList();
		ArrayList contacts=new ArrayList();
		ArrayList modality=new ArrayList();
		ArrayList components=new ArrayList();
		Set assistanceCopy=new TreeSet();
		String level=null;
		String status=null;
		String actualStartDate=null;
		String actualCompletionDate=null;
		double[][] actualFunds=new double[yrCount][3];
		double[][] plannedFunds=new double[yrCount][3];
		double[][] actualCommFunds=new double[yrCount][4];
		double[][] plannedCommFunds=new double[yrCount][4];
		double[][] actualDisbFunds=new double[yrCount][4];
		double[][] plannedDisbFunds=new double[yrCount][4];
		double[][] actualExpFunds=new double[yrCount][4];
		double[][] plannedExpFunds=new double[yrCount][4];
		
		double[][] subTotActualFunds=new double[yrCount][3];
		double[][] subTotPlannedFunds=new double[yrCount][3];
		double[][] subTotActualCommFunds=new double[yrCount][4];
		double[][] subTotPlannedCommFunds=new double[yrCount][4];
		double[][] subTotActualDisbFunds=new double[yrCount][4];
		double[][] subTotPlannedDisbFunds=new double[yrCount][4];
		double[][] subTotActualExpFunds=new double[yrCount][4];
		double[][] subTotPlannedExpFunds=new double[yrCount][4];
		
		
		AmpByAssistTypeList[][] actualTerms=new AmpByAssistTypeList[yrCount][3];
		AmpByAssistTypeList[][] plannedTerms=new AmpByAssistTypeList[yrCount][3];
		
		for (int i = 0; i < yrCount; i++) 
			for (int ii = 0; ii < 3; ii++) {
			actualTerms[i][ii]=new AmpByAssistTypeList();			
			plannedTerms[i][ii]=new AmpByAssistTypeList();
			
		}
			
		//quarterly stuff
		AmpByAssistTypeList[][] actualCommTerms=new AmpByAssistTypeList[yrCount][4];
		AmpByAssistTypeList[][] plannedCommTerms=new AmpByAssistTypeList[yrCount][4];
		AmpByAssistTypeList[][] actualDisbTerms=new AmpByAssistTypeList[yrCount][4];
		AmpByAssistTypeList[][] plannedDisbTerms=new AmpByAssistTypeList[yrCount][4];
		AmpByAssistTypeList[][] actualExpTerms=new AmpByAssistTypeList[yrCount][4];
		AmpByAssistTypeList[][] plannedExpTerms=new AmpByAssistTypeList[yrCount][4];
		
		for (int i = 0; i < yrCount; i++) 
			for (int ii = 0; ii < 4; ii++) {
				actualCommTerms[i][ii]=new AmpByAssistTypeList();
				plannedCommTerms[i][ii]=new AmpByAssistTypeList();
				actualDisbTerms[i][ii]=new AmpByAssistTypeList();
				plannedDisbTerms[i][ii]=new AmpByAssistTypeList();
				actualExpTerms[i][ii]=new AmpByAssistTypeList();
				plannedExpTerms[i][ii]=new AmpByAssistTypeList();
		}
		
		String title=null;
		String ampId=null;
		String contactName=null;
		double toExchangeRate=1.0;
		double fromExchangeRate=0.0;
		double amount=0.0;
		double sumUnDisb = 0, actSumCommit = 0, actSumDisb = 0, actSumExp = 0, planSumCommit = 0, planSumDisb = 0, planSumExp = 0;
		double unDisbSubTotal = 0, actCommitSubTotal = 0, actDisbSubTotal = 0, actExpSubTotal = 0, planCommitSubTotal = 0, planDisbSubTotal = 0, planExpSubTotal = 0;
		
		AmpByAssistTypeList actSumCommitTerms=new AmpByAssistTypeList();
		AmpByAssistTypeList actSumDisbTerms=new AmpByAssistTypeList();
		AmpByAssistTypeList actSumExpTerms=new AmpByAssistTypeList();
		AmpByAssistTypeList planSumCommitTerms=new AmpByAssistTypeList();
		AmpByAssistTypeList planSumDisbTerms=new AmpByAssistTypeList();
		AmpByAssistTypeList planSumExpTerms=new AmpByAssistTypeList();
		AmpByAssistTypeList sumUnDisbTerms=new AmpByAssistTypeList();
		
		
		int fiscalYear=0,fiscalQuarter=0;
		String objective=null;
		String description=null;
		DecimalFormat mf = new DecimalFormat("###,###,###,###,###") ;
		AdvancedReport report=null;
		AmpReportCache ampReportCache = null;
		try
		{
			columns=rsc.getColumns();
			//logger.debug("Column Size: " + columns.size());
//			hierarchy=(ArrayList)rsc.getHierarchy();
			measures=(ArrayList)rsc.getMeasures();
			//logger.debug("Measure Size: " + measures.size());
			//logger.debug("Number of Activities:" + ampActivities.size());
			iter = ampActivities.iterator();
			while(iter.hasNext())
			{
				
				ampReportCache = (AmpReportCache) iter.next();
				
				
				//logger.info("hierarchy record type is "+ahReport.getLabel()+" ampReportCache donor is "+ampReportCache.getDonorName());
					if(activityIds.indexOf(ampReportCache.getAmpActivityId())==-1)
						continue;
					
					//holy fix :) - hierarchy should only display funding info connected with its current hierarchical grouping
	
					if (ignoreHierarchyReportCacheRow(ampReportCache,ahReport)) continue;
					if (ignoreHierarchyReportCacheRow(ampReportCache,ahReport1)) continue;
					if (ignoreHierarchyReportCacheRow(ampReportCache,ahReport2)) continue;
				
					
					
					
				if(reports==null || !(reports.getAmpActivityId().equals(ampReportCache.getAmpActivityId())))
				{
					if(reports!=null)
					{
						iterColumn=columns.iterator();
						while(iterColumn.hasNext())
						{
							//logger.debug("Begin while 1");
							report=new AdvancedReport();
							report.setDonors(new ArrayList());
							report.setSectors(new ArrayList());
							report.setRegions(new ArrayList());
							report.setAssistance(new ArrayList());
							report.setModality(new ArrayList());
							report.setContacts(new ArrayList());
							report.setComponents(new ArrayList());
							Column c=(Column) iterColumn.next();
							//logger.debug("Column Id: " + c.getColumnId());
							if(c.getColumnId().equals(Constants.STATUS_NAME))
								report.setStatus(status);
							if(c.getColumnId().equals(Constants.DONOR_NAME))
							{
								if(donors.size()==0)
									donors.add(" ");
								report.getDonors().addAll(donors);
							}
							if(c.getColumnId().equals(Constants.ACTUAL_START_DATE))
								report.setActualStartDate(actualStartDate);
							if(c.getColumnId().equals(Constants.ACTIVITY_NAME))
								report.setTitle(title);
							
							if(c.getColumnId().equals(Constants.COMPONENT_NAME))
								report.getComponents().addAll(components);
							
							
							if(c.getColumnId().equals(Constants.TERM_ASSIST_NAME))
							{	
								//logger.debug("Inside type of assistance");
								if(assistance.size()==0)
									assistance.add(" ");
								report.getAssistance().addAll(assistance);
							}
							if(c.getColumnId().equals(Constants.LEVEL_NAME))
								report.setLevel(level);
							if(c.getColumnId().equals(Constants.ACTUAL_COMPLETION_DATE))
								report.setActualCompletionDate(actualCompletionDate);
							if(c.getColumnId().equals(Constants.SECTOR_NAME))
								report.getSectors().addAll(sectors);
							if(c.getColumnId().equals(Constants.REGION_NAME))
								report.getRegions().addAll(regions);
							if(c.getColumnId().equals(Constants.AMP_ID)) {
								report.setProjId(getProjectId(reports.getAmpActivityId()));
							}
							if(c.getColumnId().equals(Constants.FUNDING_INSTRUMENT))
							{
								if(modality.size()==0)
									modality.add(" ");
								report.getModality().addAll(modality);
							}
							if(c.getColumnId().equals(Constants.CONTACT_NAME))
							{	
								if(contacts.size()==0)
									contacts.add(" ");
								report.getContacts().addAll(contacts);
							}
							if(c.getColumnId().equals(Constants.OBJECTIVE))
							{
								report.setObjective(objective);
								if(report.getObjective()!=null)
								{
									Editor ed = org.digijava.module.editor.util.DbUtil.getEditor(objective,"en");
									if (ed != null && ed.getBody() != null && ed.getBody().trim().length() > 0)
										report.setObjectivePDFXLS(getDescParsed(ed.getBody()));
									else 
										report.setObjectivePDFXLS(" ");
								}
								else
								{
									report.setObjectivePDFXLS(" ");
								}
							}
							if(c.getColumnId().equals(Constants.DESCRIPTION))
							{
								report.setDescription(description);
								if(report.getDescription()!=null)
								{
									Editor ed = org.digijava.module.editor.util.DbUtil.getEditor(description,"en");
									if (ed != null && ed.getBody() != null && ed.getBody().trim().length() > 0)
										report.setDescriptionPDFXLS(getDescParsed(ed.getBody()));
									else 
										report.setDescriptionPDFXLS(" ");
								}
								else
									report.setDescriptionPDFXLS(" ");
							}
							if(c.getColumnId().equals(Constants.TOTAL_COMMITMENT))
								report.setTotalCommitment(mf.format(totalCommitment));
							if(c.getColumnId().equals(Constants.TOTAL_DISBURSEMENT))
								report.setTotalDisbursement(mf.format(totalDisbursement));
							reports.getRecords().add(report);
						}
						//logger.debug("Reports Size: " + reports.getRecords().size());
						report=new AdvancedReport();
						report.setAmpFund(new ArrayList());
						report.setAssistanceCopy(new ArrayList());
						report.getAssistanceCopy().addAll(assistanceCopy);
					
						if(rsc.getOption().equals(Constants.ANNUAL))
						
							annualFundingCompute(yrCount,measures,actualFunds,plannedFunds,
							   		 actualTerms, plannedTerms,actSumCommit, 
									 actSumDisb,actSumExp,planSumCommit,planSumDisb,planSumExp,
									 actSumCommitTerms,actSumDisbTerms, actSumExpTerms,
									 planSumCommitTerms, planSumDisbTerms, planSumExpTerms,
									 totalCommitment,totalDisbursement,report, reports,
									 subTotActualFunds,
									 subTotPlannedFunds,
									 actCommitSubTotal, 
									 actDisbSubTotal ,
									 actExpSubTotal , 
									 planCommitSubTotal,
									 planDisbSubTotal,
									 planExpSubTotal,
									 unDisbSubTotal,
									 true
							);
						
						else
				
							quarterlyFundingCompute(yrCount,measures,
									actualCommFunds, actualDisbFunds,actualExpFunds, 
									plannedCommFunds, plannedDisbFunds,plannedExpFunds, 
									actualCommTerms,actualDisbTerms,actualExpTerms,
									plannedCommTerms,plannedDisbTerms,plannedExpTerms,			
									actSumCommit, actSumDisb, actSumExp, planSumCommit,planSumDisb,planSumExp,
									  actSumCommitTerms, actSumDisbTerms, actSumExpTerms,planSumCommitTerms, planSumDisbTerms,planSumExpTerms,
									 totalCommitment,totalDisbursement,report, reports,
									 subTotActualCommFunds,subTotActualDisbFunds,subTotActualExpFunds,
									 subTotPlannedCommFunds,subTotPlannedDisbFunds,subTotPlannedExpFunds,
									  actCommitSubTotal, actDisbSubTotal, actExpSubTotal , planCommitSubTotal,planDisbSubTotal,planExpSubTotal,unDisbSubTotal,
									 true);
							
							
												
						//logger.debug("Reports Size: " + reports.getRecords().size());
						//logger.debug("Fund Size: " + report.getAmpFund().size());
						ahReport.getProject().add(reports);
						totalCommitment=0;
						totalDisbursement=0;
						donors.clear();
						assistance.clear();
						assistanceCopy.clear();
						modality.clear();
						contacts.clear();
						sectors.clear();
						regions.clear();
						components.clear();
						contactName=null;
						sumUnDisb=actSumCommit=actSumDisb=actSumExp=planSumCommit=planSumDisb=planSumExp = 0;
						
						//reset subtotals - we need fresh objects here!
						 actSumCommitTerms=new AmpByAssistTypeList();
						 actSumDisbTerms=new AmpByAssistTypeList();
						 actSumExpTerms=new AmpByAssistTypeList();
						 planSumCommitTerms=new AmpByAssistTypeList();
						 planSumDisbTerms=new AmpByAssistTypeList();
						 planSumExpTerms=new AmpByAssistTypeList();
						 sumUnDisbTerms=new AmpByAssistTypeList();
	
						
						for(int i=0;i<yrCount;i++)
						{
							actualFunds[i][0]=actualFunds[i][1]=actualFunds[i][2]=0;
							plannedFunds[i][0]=plannedFunds[i][1]=plannedFunds[i][2]=0;
							actualCommFunds[i][0]=actualCommFunds[i][1]=actualCommFunds[i][2]=actualCommFunds[i][3]=0;
							plannedCommFunds[i][0]=plannedCommFunds[i][1]=plannedCommFunds[i][2]=plannedCommFunds[i][3]=0;
							actualDisbFunds[i][0]=actualDisbFunds[i][1]=actualDisbFunds[i][2]=actualDisbFunds[i][3]=0;
							plannedDisbFunds[i][0]=plannedDisbFunds[i][1]=plannedDisbFunds[i][2]=plannedDisbFunds[i][3]=0;
							actualExpFunds[i][0]=actualExpFunds[i][1]=actualExpFunds[i][2]=actualExpFunds[i][3]=0;
							plannedExpFunds[i][0]=plannedExpFunds[i][1]=plannedExpFunds[i][2]=plannedExpFunds[i][3]=0;
							
							for (int y = 0; y < 3; y++) {
								actualTerms[i][y]=new AmpByAssistTypeList();			
								plannedTerms[i][y]=new AmpByAssistTypeList();
								
							}
							
								for (int  y = 0;  y < 4;  y++) {
									actualCommTerms[i][ y]=new AmpByAssistTypeList();
									plannedCommTerms[i][ y]=new AmpByAssistTypeList();
									actualDisbTerms[i][ y]=new AmpByAssistTypeList();
									plannedDisbTerms[i][ y]=new AmpByAssistTypeList();
									actualExpTerms[i][ y]=new AmpByAssistTypeList();
									plannedExpTerms[i][ y]=new AmpByAssistTypeList();
							}
							


							
						}
					}
					reports= new Report();
					reports.setType(rsc.getType().intValue());
					reports.setRecords(new ArrayList());
					//logger.debug("Init Record");
					title=ampReportCache.getActivityName();
					ampId=ampReportCache.getAmpId();
					reports.setAmpActivityId(ampReportCache.getAmpActivityId());
					if(ampReportCache.getLevelName().equals("Not Exist"))
						level=" ";
					else
						level=ampReportCache.getLevelName();
					if(ampReportCache.getStatusName()==null)
						status=" ";
					else
						status=ampReportCache.getStatusName();
					if(DbUtil.getAmpReportSector(ampReportCache.getAmpActivityId()).size()==0)
						sectors.add(" ");
					else
						sectors.addAll(DbUtil.getAmpReportSector(ampReportCache.getAmpActivityId()));
				
					if(DbUtil.getAmpComponent(ampReportCache.getAmpActivityId()).size()==0)
						components.add(" ");
					else
						components.addAll(DbUtil.getAmpComponent(ampReportCache.getAmpActivityId()));
					if(DbUtil.getAmpReportLocation(ampReportCache.getAmpActivityId()).size()==0)
						regions.add(" ");
					else
						regions.addAll(DbUtil.getAmpReportLocation(ampReportCache.getAmpActivityId()));
					if(ampReportCache.getTermAssistName()!=null) {
						assistance.add(ampReportCache.getTermAssistName());
						assistanceCopy.add(ampReportCache.getTermAssistName());
					}  else {
						assistance.add(DbUtil.getAmpAssistanceType(ampReportCache.getAmpActivityId()));
					}
				
					if(ampReportCache.getDonorName()!=null)
						donors.add(ampReportCache.getDonorName());
					else
						donors.addAll(DbUtil.getAmpDonorsForActivity(ampReportCache.getAmpActivityId()));					
					if(ampReportCache.getActualStartDate()!=null)
						actualStartDate=DateConversion.ConvertDateToString(ampReportCache.getActualStartDate());
					else
						actualStartDate=" ";
					if(ampReportCache.getActualCompletionDate()!=null)
						actualCompletionDate=DateConversion.ConvertDateToString(ampReportCache.getActualCompletionDate());
					else
						actualCompletionDate=" ";
					if(ampReportCache.getModalityName()!=null)
						modality.add(ampReportCache.getModalityName());
					else
						modality.add(DbUtil.getAmpModalityNames(ampReportCache.getAmpActivityId()));					
					AmpActivity ampActivity=(AmpActivity) ActivityUtil.getAmpActivity(ampReportCache.getAmpActivityId());
					if(ampActivity.getDescription()!=null)
					{
						//Editor ed = org.digijava.module.editor.util.DbUtil.getEditor(ampReportCache.getActivityDescription(),"en");
						//description=ed.getBody();
						description = ampActivity.getDescription();
					}
					else
						description=" ";
							
					if(ampActivity.getObjective()!=null)
					{
/*						Editor ed = org.digijava.module.editor.util.DbUtil.getEditor(ampActivity.getObjective(),"en");
						if(ed!=null)
							objective=ed.getBody();
						else
							objective=" ";
*/							
						objective=ampActivity.getObjective();
					}
					else
						objective=" ";
	
					if(ampActivity.getContFirstName()!=null)
						contactName=ampActivity.getContFirstName();
					if(ampActivity.getContLastName()!=null)
						contactName=contactName + " " + ampActivity.getContLastName();
					if(contactName!=null)
						contacts.add(contactName);
					contactName=null;
					
					if(ampActivity.getMofedCntFirstName()!=null)
						contactName=ampActivity.getMofedCntFirstName();
					if(ampActivity.getMofedCntLastName()!=null)
						contactName=contactName + " " + ampActivity.getMofedCntLastName();
					if(contactName!=null)
						contacts.add(contactName);
				}
				//logger.debug("Title:" + title);
				if(donors.indexOf(ampReportCache.getDonorName())==-1 && ampReportCache.getDonorName()!=null)
					donors.add(ampReportCache.getDonorName());
				if(assistance.indexOf(ampReportCache.getTermAssistName())==-1 && ampReportCache.getTermAssistName()!=null) {
					assistance.add(ampReportCache.getTermAssistName());
					assistanceCopy.add(ampReportCache.getTermAssistName());
				}
				if(modality.indexOf(ampReportCache.getModalityName())==-1 && ampReportCache.getModalityName()!=null)
					modality.add(ampReportCache.getModalityName());
				//Begin fund calculation
				if(ampReportCache.getFiscalYear()!=null && ampReportCache.getFiscalQuarter()!=null)
				{
					//logger.debug("begin fund calculation");
					if(new Long(fiscalCalId).equals(Constants.ETH_FY) || new Long(fiscalCalId).equals(Constants.ETH_CAL))
					{
						if(ampReportCache.getTransactionDate()!=null)
						{
							GregorianCalendar calendar = new GregorianCalendar();
							EthiopianCalendar ec=new EthiopianCalendar();
							EthiopianCalendar tempDate=new EthiopianCalendar();
							calendar.setTime(ampReportCache.getTransactionDate());
							ec=tempDate.getEthiopianDate(calendar);
							if(new Long(fiscalCalId).equals(Constants.ETH_FY))
							{
								fiscalYear=(int)ec.ethFiscalYear;
								fiscalQuarter=(int)ec.ethFiscalQrt;
							}
							if(new Long(fiscalCalId).equals(Constants.ETH_CAL))
							{
								fiscalYear=(int)ec.ethYear;
								fiscalQuarter=(int)ec.ethQtr;
							}
						}
					}
					if(new Long(fiscalCalId).equals(Constants.GREGORIAN))
					{
						fiscalYear=ampReportCache.getFiscalYear().intValue();
						fiscalQuarter=ampReportCache.getFiscalQuarter().intValue();
					}
					/*if(minYear > fiscalYear)
						minYear=fiscalYear;
					if(maxYear < fiscalYear)
						maxYear=fiscalYear;*/
					if(ampReportCache.getCurrencyCode().equals("USD"))
						fromExchangeRate=1.0;
					else
						fromExchangeRate=DbUtil.getExchangeRate(ampReportCache.getCurrencyCode(),Constants.PLANNED,ampReportCache.getTransactionDate());
				
					if(ampCurrencyCode.equals("USD"))
						toExchangeRate=1.0;
					else	
						toExchangeRate=DbUtil.getExchangeRate(ampCurrencyCode,Constants.PLANNED,ampReportCache.getTransactionDate());
					
					if(rsc.getOption().equals(Constants.ANNUAL))
					{
						if(ampReportCache.getActualCommitment().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
							totalCommitment+=extractAnnualFunding( ampReportCache, ampReportCache.getActualCommitment(),fromExchangeRate,toExchangeRate, fromYr, toYr, fiscalYear,actualTerms, actualFunds, 0);
							
							
						if(ampReportCache.getActualDisbursement().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
							totalDisbursement+=extractAnnualFunding( ampReportCache, ampReportCache.getActualDisbursement(),fromExchangeRate,toExchangeRate, fromYr, toYr, fiscalYear,actualTerms, actualFunds, 1);
							
						if(measures.indexOf(new Long(3))!=-1)
							if(ampReportCache.getActualExpenditure().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
								extractAnnualFunding( ampReportCache, ampReportCache.getActualExpenditure(),fromExchangeRate,toExchangeRate, fromYr, toYr, fiscalYear,actualTerms, actualFunds, 2);
							
						
						if(measures.indexOf(new Long(4))!=-1)
						{
							if(ampReportCache.getPlannedCommitment().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
								extractAnnualFunding( ampReportCache, ampReportCache.getPlannedCommitment(),fromExchangeRate,toExchangeRate, fromYr, toYr, fiscalYear, plannedTerms, plannedFunds, 0);
						
						}	
						if(measures.indexOf(new Long(5))!=-1)
							if(ampReportCache.getPlannedDisbursement().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
								extractAnnualFunding( ampReportCache, ampReportCache.getPlannedDisbursement(),fromExchangeRate,toExchangeRate, fromYr, toYr, fiscalYear, plannedTerms, plannedFunds, 1);
						if(measures.indexOf(new Long(6))!=-1)
							if(ampReportCache.getPlannedExpenditure().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
								extractAnnualFunding( ampReportCache, ampReportCache.getPlannedExpenditure(),fromExchangeRate,toExchangeRate, fromYr, toYr, fiscalYear, plannedTerms, plannedFunds, 2);
									
					}
					else
					{	
						if(ampReportCache.getActualCommitment().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
							totalCommitment+=setQuarterlyFunding(ampReportCache,  ampReportCache.getActualCommitment(), fromExchangeRate,
									toExchangeRate, fromYr, toYr, fiscalQuarter, fiscalYear, 
									actualCommTerms,actualCommFunds);
									
						if(ampReportCache.getActualDisbursement().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
							totalDisbursement+=setQuarterlyFunding(ampReportCache, ampReportCache.getActualDisbursement(), fromExchangeRate,
									toExchangeRate, fromYr, toYr, fiscalQuarter, fiscalYear, 
									actualDisbTerms,actualDisbFunds);								
						if(measures.indexOf(new Long(3))!=-1)
							if(ampReportCache.getActualExpenditure().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
								setQuarterlyFunding(ampReportCache,ampReportCache.getActualExpenditure(),  fromExchangeRate,
										toExchangeRate, fromYr, toYr, fiscalQuarter, fiscalYear, 
										actualExpTerms,actualExpFunds);								
						if(measures.indexOf(new Long(4))!=-1)
							if(ampReportCache.getPlannedCommitment().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
								setQuarterlyFunding(ampReportCache,ampReportCache.getPlannedCommitment(),  fromExchangeRate,
										toExchangeRate, fromYr, toYr, fiscalQuarter, fiscalYear, 
										plannedCommTerms,plannedCommFunds);
						
						if(measures.indexOf(new Long(5))!=-1)
							if(ampReportCache.getPlannedDisbursement().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
								setQuarterlyFunding(ampReportCache,ampReportCache.getPlannedDisbursement(),  fromExchangeRate,
											toExchangeRate, fromYr, toYr, fiscalQuarter, fiscalYear, 
											plannedDisbTerms,plannedDisbFunds);
						if(measures.indexOf(new Long(6))!=-1)
							if(ampReportCache.getPlannedExpenditure().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
									setQuarterlyFunding(ampReportCache,ampReportCache.getPlannedExpenditure(),  fromExchangeRate,
											toExchangeRate, fromYr, toYr, fiscalQuarter, fiscalYear, 
											plannedExpTerms,plannedExpFunds);
								
					}
							
				} // End Fund Calculation
	
			} // End of activity iteration
					
			if(reports!=null)
						{
							iterColumn=columns.iterator();
							while(iterColumn.hasNext())
							{
								//logger.debug("begin while 2");
								report=new AdvancedReport();
								report.setDonors(new ArrayList());
								report.setAssistance(new ArrayList());
								report.setSectors(new ArrayList());
								report.setRegions(new ArrayList());
								report.setComponents(new ArrayList());
								report.setModality(new ArrayList());
								report.setContacts(new ArrayList());
								Column c=(Column) iterColumn.next();
								//logger.debug("Column Id: " + c.getColumnId());
								if(c.getColumnId().equals(Constants.STATUS_NAME))
									report.setStatus(status);
								if(c.getColumnId().equals(Constants.DONOR_NAME))
								{
									if(donors.size()==0)
										donors.add(" ");
									report.getDonors().addAll(donors);
								}
								if(c.getColumnId().equals(Constants.ACTUAL_START_DATE))
									report.setActualStartDate(actualStartDate);
								if(c.getColumnId().equals(Constants.ACTIVITY_NAME))
									report.setTitle(title);
								if(c.getColumnId().equals(Constants.TERM_ASSIST_NAME))
								{
									if(assistance.size()==0)
										assistance.add(" ");
									report.getAssistance().addAll(assistance);
								}	
								

								if(c.getColumnId().equals(Constants.COMPONENT_NAME))
									report.getComponents().addAll(components);
								
								
								if(c.getColumnId().equals(Constants.LEVEL_NAME))
									report.setLevel(level);
								if(c.getColumnId().equals(Constants.ACTUAL_COMPLETION_DATE))
									report.setActualCompletionDate(actualCompletionDate);
								if(c.getColumnId().equals(Constants.SECTOR_NAME))
									report.getSectors().addAll(sectors);
								if(c.getColumnId().equals(Constants.REGION_NAME))
									report.getRegions().addAll(regions);
								if(c.getColumnId().equals(Constants.AMP_ID)) {
									report.setProjId(getProjectId(reports.getAmpActivityId()));
								}
									
								if(c.getColumnId().equals(Constants.FUNDING_INSTRUMENT))
								{
									if(modality.size()==0)
										modality.add(" ");
									report.getModality().addAll(modality);
								}
								if(c.getColumnId().equals(Constants.CONTACT_NAME))
								{
									if(contacts.size()==0)
										contacts.add(" ");
									report.getContacts().addAll(contacts);
								}
								if(c.getColumnId().equals(Constants.OBJECTIVE))
								{
									report.setObjective(objective);
									if(report.getObjective()!=null)
									{
										Editor ed = org.digijava.module.editor.util.DbUtil.getEditor(objective,"en");
										if (ed != null && ed.getBody() != null && ed.getBody().trim().length() > 0)
											report.setObjectivePDFXLS(getDescParsed(ed.getBody()));
										else
											report.setObjectivePDFXLS(" ");
									}
									else
										report.setObjectivePDFXLS(" ");
								}
								if(c.getColumnId().equals(Constants.DESCRIPTION))
								{
									report.setDescription(description);
									if(report.getDescription()!=null)
									{
										Editor ed = org.digijava.module.editor.util.DbUtil.getEditor(description,"en");
										if (ed != null && ed.getBody() != null && ed.getBody().trim().length() > 0)
											report.setDescriptionPDFXLS(getDescParsed(ed.getBody()));
										else
											report.setDescriptionPDFXLS(" ");
									}
									else
										report.setDescriptionPDFXLS(" ");
								}
								if(c.getColumnId().equals(Constants.TOTAL_COMMITMENT))
									report.setTotalCommitment(mf.format(totalCommitment));
								if(c.getColumnId().equals(Constants.TOTAL_DISBURSEMENT))
									report.setTotalDisbursement(mf.format(totalDisbursement));
								reports.getRecords().add(report);
								//logger.debug("end while 2");
							}
							report=new AdvancedReport();
							report.setAssistanceCopy(new ArrayList());
							report.getAssistanceCopy().addAll(assistanceCopy);
							report.setAmpFund(new ArrayList());
							if(rsc.getOption().equals(Constants.ANNUAL))
								annualFundingCompute(yrCount,measures,actualFunds,plannedFunds,
								   		 actualTerms, plannedTerms,actSumCommit, 
										 actSumDisb,actSumExp,planSumCommit,planSumDisb,planSumExp,
										 actSumCommitTerms,actSumDisbTerms, actSumExpTerms,
										 planSumCommitTerms, planSumDisbTerms, planSumExpTerms,
										 totalCommitment,totalDisbursement,report, reports,
										 subTotActualFunds,
										 subTotPlannedFunds,
										 actCommitSubTotal, 
										 actDisbSubTotal ,
										 actExpSubTotal , 
										 planCommitSubTotal,
										 planDisbSubTotal,
										 planExpSubTotal,
										 unDisbSubTotal,
										 true
								);
							
							else
								quarterlyFundingCompute(yrCount,measures,
										actualCommFunds, actualDisbFunds,actualExpFunds, 
										plannedCommFunds, plannedDisbFunds,plannedExpFunds, 
										actualCommTerms,actualDisbTerms,actualExpTerms,
										plannedCommTerms,plannedDisbTerms,plannedExpTerms,			
										actSumCommit, actSumDisb, actSumExp, planSumCommit,planSumDisb,planSumExp,
										  actSumCommitTerms, actSumDisbTerms, actSumExpTerms,planSumCommitTerms, planSumDisbTerms,planSumExpTerms,
										 totalCommitment,totalDisbursement,report, reports,
										 subTotActualCommFunds,subTotActualDisbFunds,subTotActualExpFunds,
										 subTotPlannedCommFunds,subTotPlannedDisbFunds,subTotPlannedExpFunds,
										  actCommitSubTotal, actDisbSubTotal, actExpSubTotal , planCommitSubTotal,planDisbSubTotal,planExpSubTotal,unDisbSubTotal,
										 true);
					
								
								
							ahReport.getProject().add(reports);
						}
						reports=null;
						ahReport.setFundSubTotal(new ArrayList());
						if(rsc.getOption().equals(Constants.ANNUAL))
						{
							for(int i=0;i<yrCount;i++)
							{
								AmpFund ampFund=new AmpFund();
								if(measures.indexOf(new Long(1))!=-1)
									ampFund.setCommAmount(mf.format(subTotActualFunds[i][0])); 
								if(measures.indexOf(new Long(2))!=-1)
									ampFund.setDisbAmount(mf.format(subTotActualFunds[i][1])); 
								if(measures.indexOf(new Long(3))!=-1)
									ampFund.setExpAmount(mf.format(subTotActualFunds[i][2]));	
								if(measures.indexOf(new Long(4))!=-1)
									ampFund.setPlCommAmount(mf.format(subTotPlannedFunds[i][0])); 
								if(measures.indexOf(new Long(5))!=-1)
									ampFund.setPlDisbAmount(mf.format(subTotPlannedFunds[i][1])); 
								if(measures.indexOf(new Long(6))!=-1)
									ampFund.setPlExpAmount(mf.format(subTotPlannedFunds[i][2]));	
								ahReport.getFundSubTotal().add(ampFund);
							}

							AmpFund fund = new AmpFund();
							fund.setCommAmount(mf.format(actCommitSubTotal));
							fund.setDisbAmount(mf.format(actDisbSubTotal));
							fund.setExpAmount(mf.format(actExpSubTotal));
							fund.setPlCommAmount(mf.format(planCommitSubTotal));
							fund.setPlDisbAmount(mf.format(planDisbSubTotal));
							fund.setPlExpAmount(mf.format(planExpSubTotal));
							fund.setUnDisbAmount(mf.format(unDisbSubTotal));
							ahReport.getFundSubTotal().add(fund) ;	
						}
						else
						{	
							for(int i=0;i<yrCount;i++)
							{
								for(int qtr=0;qtr<4;qtr++)
								{
									AmpFund ampFund=new AmpFund();
									if(measures.indexOf(new Long(1))!=-1)
										ampFund.setCommAmount(mf.format(subTotActualCommFunds[i][qtr])); 
									if(measures.indexOf(new Long(2))!=-1)
										ampFund.setDisbAmount(mf.format(subTotActualDisbFunds[i][qtr])); 
									if(measures.indexOf(new Long(3))!=-1)
										ampFund.setExpAmount(mf.format(subTotActualExpFunds[i][qtr]));	
									if(measures.indexOf(new Long(4))!=-1)
										ampFund.setPlCommAmount(mf.format(subTotPlannedCommFunds[i][qtr])); 
									if(measures.indexOf(new Long(5))!=-1)
										ampFund.setPlDisbAmount(mf.format(subTotPlannedDisbFunds[i][qtr])); 
									if(measures.indexOf(new Long(6))!=-1)
										ampFund.setPlExpAmount(mf.format(subTotPlannedExpFunds[i][qtr]));	
									ahReport.getFundSubTotal().add(ampFund);
								}
							}

							AmpFund fund = new AmpFund();
							fund.setCommAmount(mf.format(actCommitSubTotal));
							fund.setDisbAmount(mf.format(actDisbSubTotal));
							fund.setExpAmount(mf.format(actExpSubTotal));
							fund.setPlCommAmount(mf.format(planCommitSubTotal));
							fund.setPlDisbAmount(mf.format(planDisbSubTotal));
							fund.setPlExpAmount(mf.format(planExpSubTotal));
							fund.setUnDisbAmount(mf.format(unDisbSubTotal));
							ahReport.getFundSubTotal().add(fund) ;	
						}
		} 
		catch(Exception e)
		{
			e.printStackTrace(System.out);
		}

		finally 
		{
			try 
			{
				PersistenceManager.releaseSession(session);
			}
			catch (Exception ex2) 
			{
				logger.debug("releaseSession() failed ");
			}
		}
		return ahReport;
	}

	public static ArrayList getLevel3AdvancedReport(String teamClause,Long ampColumnId1,Long ampColumnId2,Long ampColumnId3,Long ampStatusId,Long ampDonorId,Long ampModalityId,Long ampSectorId,String region,String component, ArrayList approvedActivityList) 
	{
		Session session = null;

		Iterator iterLevel1=null;
		Iterator iterLevel2=null;
		Iterator iterActivity=null;
		ArrayList level=new ArrayList();
		ArrayList activityIds=new ArrayList();
		ArrayList ampReports=new ArrayList();
		ArrayList ahReports=new ArrayList();
		String inClause=null;
		AdvancedHierarchyReport ahReport=null;
		List projTitles = new ArrayList();
		
		try 
		{

			ahReports=getLevel2AdvancedReport(teamClause,ampColumnId1,ampColumnId2,ampStatusId,ampDonorId,ampModalityId,ampSectorId,region,component, approvedActivityList);
			iterLevel1=ahReports.iterator();
			while(iterLevel1.hasNext())
			{
				AdvancedHierarchyReport ahReportLevel1=(AdvancedHierarchyReport) iterLevel1.next();
				ahReport=new AdvancedHierarchyReport();
				ahReport.setId(ahReportLevel1.getId());
				ahReport.setName(ahReportLevel1.getName());
				ahReport.setLabel(ahReportLevel1.getLabel());
				ahReport.setLevels(new ArrayList());
				ahReport.setActivities(ahReportLevel1.getActivities());
			
				iterLevel2=ahReportLevel1.getLevels().iterator();
				while(iterLevel2.hasNext())
				{
					AdvancedHierarchyReport ahReportLevel2=(AdvancedHierarchyReport) iterLevel2.next();
					AdvancedHierarchyReport ahTempLevel2 = new AdvancedHierarchyReport();
					ahTempLevel2.setId(ahReportLevel2.getId());
					ahTempLevel2.setName(ahReportLevel2.getName());
					ahTempLevel2.setLabel(ahReportLevel2.getLabel());
					iterActivity=ahReportLevel2.getActivities().iterator();
	
					while(iterActivity.hasNext())
					{
						Long id=(Long) iterActivity.next();
						if(inClause==null)
							inClause="'" + id + "'";
						else
							inClause=inClause + ",'" + id + "'";
					}				
					ahTempLevel2.setLevels(new ArrayList());


					if(ampColumnId3.equals(Constants.STATUS_NAME))
						setHStatusInfo(level,activityIds,ampStatusId,ahTempLevel2.getLevels(),teamClause,approvedActivityList);			
				
					if(ampColumnId3.equals(Constants.DONOR_NAME))
						setHDonorInfo(level,activityIds,ampDonorId,ahTempLevel2.getLevels(),teamClause,approvedActivityList);	
						
					if(ampColumnId3.equals(Constants.FUNDING_INSTRUMENT))
						setHFundingInstrumentInfo(level,activityIds,ampModalityId,ahTempLevel2.getLevels(),teamClause,approvedActivityList);	
					
					if(ampColumnId3.equals(Constants.SECTOR_NAME))
						setHSectorInfo(level,activityIds,ampSectorId,ahTempLevel2.getLevels(),teamClause,approvedActivityList);

					if(ampColumnId3.equals(Constants.TERM_ASSIST_NAME))
						setHTermsAssistInfo(level,activityIds,ahTempLevel2.getLevels(),teamClause,approvedActivityList);

					if(ampColumnId3.equals(Constants.REGION_NAME))
						setHRegionInfo(level,activityIds,region, ahTempLevel2.getLevels(),teamClause,approvedActivityList);
					
					
					if(ampColumnId3.equals(Constants.COMPONENT_NAME))
						setHComponentInfo(level,activityIds,component,ahTempLevel2.getLevels(),teamClause,approvedActivityList);			
					
					
//					start code block for Project title column
					if(ampColumnId3.equals(Constants.ACTIVITY_NAME))
						setHActivityNameInfo(level,activityIds,projTitles,ahTempLevel2.getLevels(),teamClause,approvedActivityList);

// 			end code block for project-title column

					ahReport.getLevels().add(ahTempLevel2);
					inClause=null;
				}

				ampReports.add(ahReport);
			} 
		}
		catch(Exception e)
		{
			e.printStackTrace(System.out);
		}

		finally 
		{
			try 
			{
				PersistenceManager.releaseSession(session);
			}
			catch (Exception ex2) 
			{
				logger.debug("releaseSession() failed ");
			}
		}
		return ampReports;
	}

	public static void saveReport(AmpReports ampReports,Long ampTeamId,Long ampMemberId,boolean teamLead)
	{
		Session session = null;
		Transaction tx = null;
		String queryString=null;
		Query query=null;
		Iterator iter=null;
		Set pageFilters = new HashSet();
		try {
			session = PersistenceManager.getSession();
			tx = session.beginTransaction();
			session.save(ampReports);
			ampReports.setDescription("/viewAdvancedReport.do?view=reset&ampReportId="+ampReports.getAmpReportId());
			session.update(ampReports);
			AmpTeam ampTeam = (AmpTeam) session.get(AmpTeam.class, ampTeamId);
			
			if(teamLead == true)
			{
				//logger.info(teamMember.getMemberName() + " is Team Leader ");
				AmpTeamReports ampTeamReports = new AmpTeamReports();
				ampTeamReports.setTeamView(true);
				
				ampTeamReports.setTeam(ampTeam);
				ampTeamReports.setReport(ampReports);
				session.save(ampTeamReports);
			}
			else
			{
				//logger.info(teamMember.getMemberName() + " is Team Memeber ");
				//Long lg = teamMember.getMemberId();
				AmpTeamMember ampTeamMember = (AmpTeamMember) session.get(AmpTeamMember.class, ampMemberId);
				Set reportSet = ampTeamMember.getReports();
				reportSet.add(ampReports);
				ampTeamMember.setReports(reportSet);
				session.save(ampTeamMember);
			}

			queryString = "select filters from " + AmpFilters.class.getName() + " filters ";
			//logger.info( " Filter Query...:: " + queryString);
			query = session.createQuery(queryString);
			if(query!=null)
			{
				iter = query.list().iterator();
				while(iter.hasNext())
				{
					AmpFilters filt = (AmpFilters) iter.next();
					if(filt.getFilterName().compareTo("Region") != 0 && 
						filt.getFilterName().compareTo("Start Date/Close Date") !=0	&& 
						filt.getFilterName().compareTo("Planned/Actual") != 0 )  
					{
						//logger.info("Insertd : " + filt.getFilterName());
						pageFilters.add(filt);
					}
				}
			}

			AmpPages ampPages = new AmpPages();
			ampPages.setFilters(pageFilters);
			ampPages.setPageName(ampReports.getName());
			//logger.info(" Page Name  : " + ampPages.getPageName());
				
			String pageCode = "" + ampReports.getName().trim().charAt(0);
			for(int j=0; j <ampReports.getName().length(); j++)
			{
				if(ampReports.getName().charAt(j) == ' ')
						pageCode = pageCode + ampReports.getName().charAt(j+1);
			}
			ampPages.setPageCode(pageCode);
			session.save(ampPages);
			
			pageFilters = ampPages.getFilters();
			Iterator itr = pageFilters.iterator();
			while (itr.hasNext()) {
				AmpFilters filt = (AmpFilters) itr.next();
				AmpTeamPageFilters tpf = new AmpTeamPageFilters();
				tpf.setFilter(filt);
				tpf.setTeam(ampTeam);
				tpf.setPage(ampPages);
				session.save(tpf);
			}
			
			queryString = "select t from " + AmpTeam.class.getName() + " t " +
					"where t.accessType = 'Management'";
			query = session.createQuery(queryString);
			itr = query.list().iterator();
			while (itr.hasNext()) {
				AmpTeam t = (AmpTeam) itr.next();
				pageFilters = ampPages.getFilters();
				Iterator itr1 = pageFilters.iterator();
				while (itr1.hasNext()) {
					AmpFilters filt = (AmpFilters) itr1.next();
					AmpTeamPageFilters tpf = new AmpTeamPageFilters();
					tpf.setFilter(filt);
					tpf.setTeam(t);
					tpf.setPage(ampPages);
					session.save(tpf);
				}
			}
			
			tx.commit(); 

		}
		catch (Exception ex) {
			logger.error("Exception from saveReport()  " + ex.getMessage()); 
			ex.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
					logger.debug("Transaction Rollbacked");
				}
				catch (HibernateException e) {
					logger.error("Rollback failed :" + e);
				}
			}			
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception e) {
					logger.error("Release session faliled :" + e);
				}
			}
		}
	}

	public static boolean checkDuplicateReportName(String reportTitle){
		boolean found=false;
		Session session = null;
		Query query = null;
		Iterator iter=null;
		String queryString;
		try {
			session = PersistenceManager.getSession();
			queryString = "select report.name from " + AmpReports.class.getName() + " report ";
			//logger.info( " Query :" + queryString);
			query = session.createQuery(queryString);
	//		iter = query.list().iterator();
			
			if(query!=null)
			{
				iter = query.list().iterator();
				//logger.info("............Query is not null............");
				while(iter.hasNext())
				{
					String str = (String) iter.next();
					if( reportTitle.trim().equals(str) )
					{
						found = true;
						break;
					}
					else
						found = false;
				}
			}

		} catch (Exception ex) {
			logger.error("Unable to get checkDupilcateReportName()", ex);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
			}
		}
		return found;
	}

	// added by Rahul for Project ID..
	public static Collection getProjectId(Long activityId)
	{
		Transaction tx=null;
		Session session=null;
		Query query=null;
		String queryString=null;
		
		Collection pid=new ArrayList();
		try{
			session=PersistenceManager.getSession();
			AmpActivity act = (AmpActivity) session.load(AmpActivity.class,activityId);

			if (act != null) 
			{
				if (act.getInternalIds() != null) 
				{
					Iterator tmp = act.getInternalIds().iterator();
					if(!tmp.hasNext())
						pid.add(" Unspecified ");
					
					while (tmp.hasNext()) 
					{
						AmpActivityInternalId actIntId = (AmpActivityInternalId) tmp.next();
						
						if(actIntId.getInternalId().trim().length()== 0 || actIntId.getInternalId()==" ")
							pid.add("unspecified");
						else
							pid.add(actIntId.getInternalId());
					}
				}
				else
				{
					pid.add("Unspecified");
				}
			}
			else
				pid.add("Unspecified");
		}
		catch(Exception e)
		{
			logger.error("UNABLE to fetch Project Id for ActivityId: "+activityId +"==Error="+e.getMessage());
			if(tx !=null)
			{
				try
				{
					tx.rollback();
				}
				catch(Exception exp)
				{
					logger.error("Transaction ROLLBACK Failed.");
					logger.error("error="+exp.getMessage());
				}
			}
		}
		finally
		{
			if(session!=null)
			{
				try
				{
					PersistenceManager.releaseSession(session);
				}
				catch(Exception ex)
				{
					logger.error("Failed to release session....");
					logger.error("error="+ex.getMessage());
				}
			}
		}
		return pid;
	}// get projectId
// end of Advanced Function	
}

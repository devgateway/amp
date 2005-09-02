/*
 * ReportUtil.java Created: 01-Apr-2005 
 */

package org.digijava.module.aim.util;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import java.text.DecimalFormat;
import java.sql.Timestamp;
import java.util.GregorianCalendar;
import java.util.Date;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.aim.dbentity.AmpMeasures;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpPhysicalComponentReport;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTeamReports;
import org.digijava.module.aim.dbentity.AmpReportCache;
import org.digijava.module.aim.dbentity.AmpReportSector;
import org.digijava.module.aim.dbentity.AmpReportColumn;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.helper.AmpFund;
import org.digijava.module.aim.helper.Column;
import org.digijava.module.aim.helper.FundTotal;
import org.digijava.module.aim.helper.Project;
import org.digijava.module.aim.helper.AmpProjectBySector;
import org.digijava.module.aim.helper.multiReport;
import org.digijava.module.aim.helper.Report;
import org.digijava.module.aim.helper.ReportSelectionCriteria;
import org.digijava.module.aim.helper.AdvancedReport;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.EthiopianCalendar;
import org.digijava.module.aim.helper.AmpDonors;
import org.digijava.module.aim.helper.AmpTeamDonors;
import org.digijava.module.aim.helper.DecimalToText;
import org.digijava.module.aim.helper.TermFund;
import org.digijava.module.aim.helper.TermFundTotal;
import org.digijava.module.aim.helper.ProjectTermAssist;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.AmpComponent;
import org.digijava.module.aim.dbentity.AmpMeasures;


/**
 * Utility class for persisting all reports related entities
 * @author priyajith
 */
public class ReportUtil {
	
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
//			logger.debug("Inclause: " + inClause);

			session = PersistenceManager.getSession();
			if(startDate==null && closeDate==null)
				queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.ampTeamId in(" + inClause + ")) order by report.activityName,report.ampActivityId,report.donorName,report.fiscalYear,report.fiscalQuarter";
			else
				queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.ampTeamId in(" + inClause + ")) and (report.actualStartDate='" + startDate + "' or report.actualCompletionDate='" + closeDate + "') order by report.activityName,report.ampActivityId,report.donorName,report.fiscalYear,report.fiscalQuarter";
			logger.debug("querystring: " + queryString);
			q = session.createQuery(queryString);	
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
							ampFund.setUnDisbAmount(mf.format(commAmount - disbAmount));
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
				ampFund.setUnDisbAmount(mf.format(commAmount - disbAmount));
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
			ArrayList sectorId=null;
			session = PersistenceManager.getSession();
			multiReport report =null;
			AmpTeamDonors ampTeamDonors =null;
			Project project=null;
			ProjectTermAssist termAssist=null;
			TermFund sectorTermAssist=null;
			logger.info("Before dbUtil");
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
					logger.info("if report not null");
					if(!(ampTeamDonors.getDonorAgency().equals("Unspecified")))
					{
						logger.info("Inside Donor");
						if(termFlag==1)
						{
							termAssist=new ProjectTermAssist();
							termAssist.setTermAssistName(termAssistName);
							if(sectorAssistance.indexOf(termAssist.getTermAssistName())==-1)
							{
								logger.info("Assistance Added:" + termAssist.getTermAssistName() + ":");
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
							logger.info("Term Assist:" + termAssist.getTermAssistName());
					
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
					queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.ampTeamId in(" + inClauseTeam + ")) and (report.ampActivityId in(" + inClause + ")) order by report.donorName,report.activityName,report.ampActivityId";
				else
					queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.ampTeamId in(" + inClauseTeam + ")) and (report.actualStartDate='" + startDate + "' or report.actualCompletionDate='" + closeDate + "') and (report.ampActivityId in(" + inClause + ")) order by report.donorName,report.activityName,report.ampActivityId";
				logger.info("querystring: " + queryString);
				qry = session.createQuery(queryString);
//				qry.setParameter("ampTeamId",ampTeamId,Hibernate.LONG) ;
				iterActivity=qry.list().iterator();
				inClause=null;
				while(iterActivity.hasNext())
				{
					AmpReportCache ampReportCache = (AmpReportCache) iterActivity.next();
				/*	if(!(ampReportCache.getAmpTeamId().equals(ampTeamId)) || ampReportCache.getFiscalYear().intValue()<fromYr || ampReportCache.getFiscalYear().intValue()>toYr)		
						continue;*/
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
						logger.info("Sector: " + ampProjectBySector.getSector().getSectorName());
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
						logger.info("Inside Donor");
						logger.info("Donor: " + ampTeamDonors.getDonorAgency());
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
			
						logger.info("Outside Donor");
						
					}
					else
					if(ampReportCache.getAmpFundingId()!=null && ampReportCache.getDonorName().equals(ampTeamDonors.getDonorAgency()) && !(ampReportCache.getAmpActivityId().equals(project.getAmpActivityId())))
					{
						logger.info("Inside Project");
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
						logger.info("activity name: " + project.getName());
						project.setAmpActivityId(ampReportCache.getAmpActivityId());
						project.setCount(++projCount);
						project.setAmpFund(new ArrayList());
						project.setTermAssist(new ArrayList());
						termAssistName=ampReportCache.getTermAssistName();
						termFlag=0;
						logger.info("Outside Project");
					}
					else
					if(ampReportCache.getAmpFundingId()!=null && ampReportCache.getDonorName().equals(ampTeamDonors.getDonorAgency()) && ampReportCache.getAmpActivityId().equals(project.getAmpActivityId()) && !(ampReportCache.getTermAssistName().equals(termAssistName)))
					{
						logger.info("Inside Terms");
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
						logger.info("Outside Terms");
					}
					logger.info("Fiscal Year: " + ampReportCache.getFiscalYear());
					logger.info("Fiscal Quarter: " + ampReportCache.getFiscalQuarter());
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
					logger.info("Funding Complete");
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
	
	public static ArrayList getAmpReportMultilateral(Long ampTeamId,int fromYr,int toYr,String perspective,String ampCurrencyCode,Long ampModalityId,Long ampStatusId,Long ampDonorId,Long ampSectorId,int fiscalCalId,String startDate,String closeDate,String region)
	{
		Session session = null ;
		Query q = null ;
		ArrayList ampReports = new ArrayList() ;
		String queryString = null;
		Iterator iter=null;
		Long All=new Long(0);
		int yrCount = (toYr - fromYr)+1;
		int fiscalYear=0,fiscalQuarter=0;
		double[][] termFunds=new double[yrCount][4];
		double[][] donorFunds=new double[yrCount][4];
		double[][] teamFunds=new double[yrCount][4];
		double[][] loanTeamFunds=new double[yrCount+1][4];
		double[][] grantTeamFunds=new double[yrCount+1][4];
		double[][] kindTeamFunds=new double[yrCount+1][4];
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
		double teamCommAmount=0.0;
		double teamDisbAmount=0.0;
		double teamPlannedDisbAmount=0.0;
		double teamExpAmount=0.0;
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
			logger.info("Inclause: " + inClause);
			session = PersistenceManager.getSession();
			if(startDate==null && closeDate==null)
				queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.ampTeamId in(" + inClause + ")) and (report.donorName is not null) and (report.termAssistName is not null) order by report.ampTeamId,report.donorName,report.termAssistName,report.fiscalYear";
			else
				queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.ampTeamId in(" + inClause + ")) and (report.actualStartDate='" + startDate + "' or report.actualCompletionDate='" + closeDate + "') and (report.donorName is not null) and (report.termAssistName is not null) order by report.ampTeamId,report.donorName,report.termAssistName,report.fiscalYear";
			q = session.createQuery(queryString);	
//			q.setParameter("ampTeamId",ampTeamId,Hibernate.LONG) ;
			logger.info("Number of Records: " + q.list().size());
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
							logger.info("Ethiopian Fiscal Year: " + fiscalYear);
							logger.info("From Year: " + fromYr);
							logger.info("From Year: " + toYr);
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
						logger.info("New donor: " + ampReportCache.getDonorName());
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
								logger.info("End First For");
						
								donorTermAssist.setTotDonorCommAmount(mf.format(totComm));
								donorTermAssist.setTotDonorDisbAmount(mf.format(totDisb));
								donorTermAssist.setTotDonorExpAmount(mf.format(totExp));
								donorTermAssist.setTotDonorPlannedDisbAmount(mf.format(totPlannedDisb));
								donorTermAssist.setTotDonorUnDisbAmount(mf.format(totComm-totDisb));
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
										}
									}
								}
							}
							donorCommAmount=donorCommAmount + totComm;
							donorDisbAmount=donorDisbAmount + totDisb;
							donorExpAmount=donorExpAmount + totExp;
							donorPlannedDisbAmount=donorPlannedDisbAmount + totPlannedDisb;
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
							logger.info("End Second For");
							ampTeamDonors.setDonorCommAmount(mf.format(donorCommAmount));
							ampTeamDonors.setDonorDisbAmount(mf.format(donorDisbAmount));
							ampTeamDonors.setDonorExpAmount(mf.format(donorExpAmount));
							ampTeamDonors.setDonorPlannedDisbAmount(mf.format(donorPlannedDisbAmount));
							ampTeamDonors.setDonorUnDisbAmount(mf.format(donorCommAmount-donorDisbAmount));
							report.getDonors().add(ampTeamDonors);
							teamCommAmount=teamCommAmount + donorCommAmount;
							teamDisbAmount=teamDisbAmount + donorDisbAmount;
							teamExpAmount=teamExpAmount + donorExpAmount;
							teamPlannedDisbAmount=teamPlannedDisbAmount + donorPlannedDisbAmount;
							
							for(int i=0;i<yrCount;i++)
							{
								FundTotal total=new FundTotal();
								total.setTotCommAmount(mf.format(teamFunds[i][0]));
								total.setTotDisbAmount(mf.format(teamFunds[i][1]));
								total.setTotExpAmount(mf.format(teamFunds[i][2]));
								total.setTotPlannedDisbAmount(mf.format(teamFunds[i][3]));
								report.getTotalTeamFund().add(total);	
							}
							logger.info("End Third For");
							report.setTeamCommAmount(mf.format(teamCommAmount));
							report.setTeamDisbAmount(mf.format(teamDisbAmount));
							report.setTeamExpAmount(mf.format(teamExpAmount));
							report.setTeamPlannedDisbAmount(mf.format(teamPlannedDisbAmount));
							report.setTeamUnDisbAmount(mf.format(teamCommAmount-teamDisbAmount));
							Iterator teamIter=assistance.iterator();
							while(teamIter.hasNext())
							{
								String assist=(String) teamIter.next();
								logger.info("Assist:" + assist + ":");
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
									termFund.setTotDonorUnDisbAmount(mf.format(loanTeamFunds[yrCount][0]-loanTeamFunds[yrCount][1]));
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
									termFund.setTotDonorUnDisbAmount(mf.format(grantTeamFunds[yrCount][0]-grantTeamFunds[yrCount][1]));
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
									termFund.setTotDonorUnDisbAmount(mf.format(kindTeamFunds[yrCount][0]-kindTeamFunds[yrCount][1]));
								}
								logger.info("Added: " + termFund.getTermAssistName());
								report.getTotalTeamTermAssistFund().add(termFund);	
							}
							ampReports.add(report);
							totComm=totDisb=totExp=totPlannedDisb=0.0;
							donorCommAmount=donorDisbAmount=donorExpAmount=donorPlannedDisbAmount=0.0;
							teamCommAmount=teamDisbAmount=teamExpAmount=teamPlannedDisbAmount=0.0;
							for(int i=0;i<yrCount;i++)
							{
								termFunds[i][0]=termFunds[i][1]=termFunds[i][2]=termFunds[i][3]=0;
								teamFunds[i][0]=teamFunds[i][1]=teamFunds[i][2]=teamFunds[i][3]=0;
								donorFunds[i][0]=donorFunds[i][1]=donorFunds[i][2]=donorFunds[i][3]=0;
								loanTeamFunds[i][0]=loanTeamFunds[i][1]=loanTeamFunds[i][2]=loanTeamFunds[i][3]=0;
								grantTeamFunds[i][0]=grantTeamFunds[i][1]=grantTeamFunds[i][2]=grantTeamFunds[i][3]=0;
								kindTeamFunds[i][0]=kindTeamFunds[i][1]=kindTeamFunds[i][2]=kindTeamFunds[i][3]=0;
							}
							loanTeamFunds[yrCount][0]=loanTeamFunds[yrCount][1]=loanTeamFunds[yrCount][2]=loanTeamFunds[yrCount][3]=0;
							grantTeamFunds[yrCount][0]=grantTeamFunds[yrCount][1]=grantTeamFunds[yrCount][2]=grantTeamFunds[yrCount][3]=0;
							kindTeamFunds[yrCount][0]=kindTeamFunds[yrCount][1]=kindTeamFunds[yrCount][2]=kindTeamFunds[yrCount][3]=0;
							donorCount=0;
						}
						logger.info("begin intialize");
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
						logger.info("end init");
											
					}

					if(report.getAmpTeamId().equals(ampReportCache.getAmpTeamId()) && !(ampReportCache.getDonorName().equals(ampTeamDonors.getDonorAgency())))
					{
						logger.info("Inside donor");
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
							donorTermAssist.setTotDonorUnDisbAmount(mf.format(totComm-totDisb));
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
									}
								}
							}
						}
						donorCommAmount=donorCommAmount + totComm;
						donorDisbAmount=donorDisbAmount + totDisb;
						donorExpAmount=donorExpAmount + totExp;
						donorPlannedDisbAmount=donorPlannedDisbAmount + totPlannedDisb;
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
						ampTeamDonors.setDonorUnDisbAmount(mf.format(donorCommAmount-donorDisbAmount));
						report.getDonors().add(ampTeamDonors);
						teamCommAmount=teamCommAmount + donorCommAmount;
						teamDisbAmount=teamDisbAmount + donorDisbAmount;
						teamExpAmount=teamExpAmount + donorExpAmount;
						teamPlannedDisbAmount=teamPlannedDisbAmount + donorPlannedDisbAmount;
						
						totComm=totDisb=totExp=totPlannedDisb=0.0;
						donorCommAmount=donorDisbAmount=donorExpAmount=donorPlannedDisbAmount=0.0;
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
						logger.info("Outside donor");
					}
					
					if(report.getAmpTeamId().equals(ampReportCache.getAmpTeamId()) && ampReportCache.getDonorName().equals(ampTeamDonors.getDonorAgency()) && !(ampReportCache.getTermAssistName().equals(termAssistName)))
					{
						logger.info("different project");
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
							donorTermAssist.setTotDonorUnDisbAmount(mf.format(totComm-totDisb));
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
									}
								}
							}
							
						}
						donorCommAmount=donorCommAmount + totComm;
						donorDisbAmount=donorDisbAmount + totDisb;
						donorExpAmount=donorExpAmount + totExp;
						donorPlannedDisbAmount=donorPlannedDisbAmount + totPlannedDisb;
						totComm=totDisb=totExp=totPlannedDisb=0.0;
						for(int i=0;i<yrCount;i++)
							termFunds[i][0]=termFunds[i][1]=termFunds[i][2]=termFunds[i][3]=0;
						if(ampReportCache.getTermAssistName()!=null)
							termAssistName=ampReportCache.getTermAssistName();
						termFlag=0;
					}
					logger.info("Funding Information");
					if(ampReportCache.getFiscalYear()!=null && ampReportCache.getFiscalQuarter()!=null)
					{
						logger.info("begin if");
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
					donorTermAssist.setTotDonorUnDisbAmount(mf.format(totComm-totDisb));
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
							}
						}
					}
				}
				donorCommAmount=donorCommAmount + totComm;
				donorDisbAmount=donorDisbAmount + totDisb;
				donorExpAmount=donorExpAmount + totExp;
				donorPlannedDisbAmount=donorPlannedDisbAmount + totPlannedDisb;
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
				ampTeamDonors.setDonorUnDisbAmount(mf.format(donorCommAmount-donorDisbAmount));
				report.getDonors().add(ampTeamDonors);
				teamCommAmount=teamCommAmount + donorCommAmount;
				teamDisbAmount=teamDisbAmount + donorDisbAmount;
				teamExpAmount=teamExpAmount + donorExpAmount;
				teamPlannedDisbAmount=teamPlannedDisbAmount + donorPlannedDisbAmount;
				
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
				report.setTeamUnDisbAmount(mf.format(teamCommAmount-teamDisbAmount));
				Iterator teamIter=assistance.iterator();
				while(teamIter.hasNext())
				{
					String assist=(String) teamIter.next();
					logger.info("Assist:" + assist + ":");
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
						termFund.setTotDonorUnDisbAmount(mf.format(loanTeamFunds[yrCount][0]-loanTeamFunds[yrCount][1]));
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
						termFund.setTotDonorUnDisbAmount(mf.format(grantTeamFunds[yrCount][0]-grantTeamFunds[yrCount][1]));
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
						termFund.setTotDonorUnDisbAmount(mf.format(kindTeamFunds[yrCount][0]-kindTeamFunds[yrCount][1]));
					}
					logger.info("Added:" + termFund.getTermAssistName());
					report.getTotalTeamTermAssistFund().add(termFund);	
				}
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
		String inClause=null;
		Iterator iterSector=null;
		DecimalFormat mf = new DecimalFormat("###,###,###,###,###") ;
		Collection currencies=null;
				
		try
		{
//			int yrCount = toYr - fromYr;
			int years=0;
//			yrCount = (yrCount * 2) + 2;
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
			currencies=DbUtil.getAmpCurrencyRate();
			logger.debug("Inclause: " + inClause);
			session = PersistenceManager.getSession();
			if(startDate==null && closeDate==null)
				queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.ampTeamId in(" + inClause + ")) order by report.activityName,report.ampActivityId,report.donorName";
			else
				queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.ampTeamId in(" + inClause + ")) and (report.actualStartDate='" + startDate + "' or report.actualCompletionDate='" + closeDate + "') order by report.activityName,report.ampActivityId,report.donorName";
			logger.debug("querystring: " + queryString);
			q = session.createQuery(queryString);	
//			q.setParameter("ampTeamId",ampTeamId,Hibernate.LONG) ;
			Report report=null;
	//		report.setProjects(new ArrayList());
	//		Project project=null;
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
		/*					if(fiscalYear<fromYr || fiscalYear>toYr)
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
							for(int i=0;i<4;i++)
							{
								AmpFund ampFund=new AmpFund();
								ampFund.setDisbAmount(mf.format(disbFund[i]));
								totDisbFund[i]=totDisbFund[i] + disbFund[i];
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
							actualCommitment=0;
							actualDisbursement=0;
							donors.clear();
							assistance.clear();
							for(int i=0;i<=yrCount;i++)
								disbFund[i]=0;
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
						if(ampReportCache.getActualStartDate()!=null)
							report.setStartDate(DateConversion.ConvertDateToString(ampReportCache.getActualStartDate()));
						else
							report.setStartDate("");
						if(ampReportCache.getActualCompletionDate()!=null)
							report.setCloseDate(DateConversion.ConvertDateToString(ampReportCache.getActualCompletionDate()));
						else
							report.setCloseDate("");
					}
//					logger.debug("Title:" + report.getTitle());
					if(donors.indexOf(ampReportCache.getDonorName())==-1 && ampReportCache.getDonorName()!=null)
						donors.add(ampReportCache.getDonorName());
					if(assistance.indexOf(ampReportCache.getTermAssistName())==-1 && ampReportCache.getTermAssistName()!=null)
						assistance.add(ampReportCache.getTermAssistName());
					if(ampReportCache.getFiscalYear()!=null && ampReportCache.getFiscalQuarter()!=null)
					{
//						logger.debug("begin if");
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
								amount=CurrencyWorker.convert1(ampReportCache.getPlannedDisbursement().doubleValue(),fromExchangeRate,toExchangeRate);
							if(disbFund[fiscalYear%toYr]==0)
								disbFund[fiscalYear%toYr]=amount;
							else
							if(disbFund[fiscalYear%toYr]>0)
								disbFund[fiscalYear%toYr]=disbFund[fiscalYear%toYr] + amount;
							amount=0.0;
						}
//						logger.debug("end if");
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
							actualCommitment=actualCommitment + CurrencyWorker.convert1(ampReportCache.getActualCommitment().doubleValue(),fromExchangeRate,toExchangeRate);
							totActualComm=totActualComm + actualCommitment;
							actualDisbursement=actualDisbursement + CurrencyWorker.convert1(ampReportCache.getActualDisbursement().doubleValue(),fromExchangeRate,toExchangeRate);
							totActualDisb = totActualDisb + actualDisbursement;
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
				report.setAcCommitment(mf.format(actualCommitment));
				report.setAcDisbursement(mf.format(actualDisbursement));
				double undisbursed = actualCommitment - actualDisbursement;
				report.setAcUnDisbursement(mf.format(undisbursed));
				if(donors.size()==0)
					report.getDonors().add("Unspecified");
				else
					report.getDonors().addAll(donors);
				if(assistance.size()==0)
					report.getAssistance().add("Unspecified");
				else
					report.getAssistance().addAll(assistance);
		/*		report.getProjects().add(project);
				report.setTotComm(mf.format(totActualComm));
				report.setTotDisb(mf.format(totActualDisb));
				report.setTotUnDisb(mf.format(totActualComm-totActualDisb));
				report.setTotDisbFund(new ArrayList());
				for(int i=0;i<4;i++)
				{
					AmpFund ampFund=new AmpFund();
					ampFund.setDisbAmount(mf.format(totDisbFund[i]));
					report.getTotDisbFund().add(ampFund);
				}*/
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
				queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.ampTeamId in(" + inClause + ")) order by report.activityName,report.ampActivityId,report.donorName,report.fiscalYear,report.fiscalQuarter";
			else
				queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.ampTeamId in(" + inClause + ")) and (report.actualStartDate='" + startDate + "' or report.actualCompletionDate='" + closeDate + "') order by report.activityName,report.ampActivityId,report.donorName,report.fiscalYear,report.fiscalQuarter";
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
		ArrayList sectorId=new ArrayList();		
		ArrayList commDate=new ArrayList();		
		ArrayList modality=new ArrayList();	
		ArrayList assistance = new ArrayList() ;
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
		double plannedDisbAmount = 0.0 ;
		double disbAmount = 0.0 ;
		double expAmount = 0.0 ;
		double projComm = 0.0 ;
		
		int yrCount = 3;

		double[][] disbFund=new double[yrCount][4];
		double[][] plDisbFund=new double[yrCount][4];
		double[][] expFund=new double[yrCount][4];

		int fiscalYear=0,fiscalQuarter=0;
		int termFlag=0;
		String inClause=null;
		Iterator iterSector=null;
		DecimalFormat mf = new DecimalFormat("###,###,###,###,###") ;
				
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
			int years=0;

			session = PersistenceManager.getSession();
			if(startDate==null && closeDate==null)
				queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.ampTeamId in(" + inClause + ")) order by report.activityName,report.ampActivityId,report.donorName,report.fiscalYear,report.fiscalQuarter";
			else
				queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.ampTeamId in(" + inClause + ")) and (report.actualStartDate='" + startDate + "' or report.actualCompletionDate='" + closeDate + "') order by report.activityName,report.ampActivityId,report.donorName,report.fiscalYear,report.fiscalQuarter";
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
		double donorCommAmount=0.0;
		double donorPlannedDisbAmount=0.0;
		double donorDisbAmount=0.0;
		double donorExpAmount=0.0;
		double teamCommAmount=0.0;
		double teamPlannedDisbAmount=0.0;
		double teamDisbAmount=0.0;
		double teamExpAmount=0.0;

		double loanCommDonorAmount=0.0;
		double grantCommDonorAmount=0.0;
		double kindCommDonorAmount=0.0;

		double loanCommTeamAmount=0.0;
		double grantCommTeamAmount=0.0;
		double kindCommTeamAmount=0.0;

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
			session = PersistenceManager.getSession();
			if(startDate==null && closeDate==null)
				queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.donorName is not null) and (report.ampTeamId in(" + inClause + ")) order by report.donorName,report.activityName,report.ampActivityId,report.fiscalYear";
			else
				queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.donorName is not null) and (report.ampTeamId in(" + inClause + ")) and (report.actualStartDate='" + startDate + "' or report.actualCompletionDate='" + closeDate + "') order by report.donorName,report.activityName,report.ampActivityId,report.fiscalYear";
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
							ampTeamDonors.setDonorDisbAmount(mf.format(donorCommAmount - donorDisbAmount));
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
							teamCommAmount=teamCommAmount + donorCommAmount;
							teamPlannedDisbAmount=teamPlannedDisbAmount + donorPlannedDisbAmount;
							teamDisbAmount=teamDisbAmount + donorDisbAmount;
							teamExpAmount=teamExpAmount + donorExpAmount;
//							ampTeamDonors.setTotalDonorTermAssistFund(new ArrayList());
	//						logger.debug("Size of Assistance:" + assistance.size() + ":");
							totalPlDisb=totalDisb=totalExp=0.0;
							Iterator donorIter=donorAssistance.iterator();
							while(donorIter.hasNext())
							{
								String assist=(String) donorIter.next();
								logger.info("Assist:" + assist + ":");
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
								logger.info("Assist:" + assist + ":");
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
							report.setTeamUnDisbAmount(mf.format(teamCommAmount-teamDisbAmount));
							projCommAmount=projPlannedDisbAmount=projDisbAmount=projExpAmount=0;
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
								queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.donorName is null) and (report.ampTeamId='" + selTeamId + "') order by report.activityName,report.ampActivityId";
								q = session.createQuery(queryString);	
								Iterator iterUn=q.list().iterator();
								if(q.list().size()>0)
								{
									while(iterUn.hasNext())
									{
										AmpReportCache ampCache= (AmpReportCache) iterUn.next();
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
							logger.info("Assist:" + assist + ":");
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
							logger.info("Assist:" + assist + ":");
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
							logger.info("Assist:" + assist + ":");
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
								queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.donorName is null) and (report.ampTeamId='" + selTeamId + "') order by report.activityName,report.ampActivityId";
							q = session.createQuery(queryString);	
							Iterator iterUn=q.list().iterator();
							if(q.list().size()>0)
							{
								while(iterUn.hasNext())
								{
									AmpReportCache ampCache= (AmpReportCache) iterUn.next();
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
		double teamCommAmount=0.0;
		double teamPlDisbAmount=0.0;
		double teamDisbAmount=0.0;
		double teamExpAmount=0.0;
		int donorCount=0;
		int teamCount=0;
		Iterator iterSector=null;
		String inClause=null;
		String termAssistName=null;
		DecimalFormat mf = new DecimalFormat("###,###,###,###,###") ;
				
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


			session = PersistenceManager.getSession();
			if(startDate==null && closeDate==null)
				queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.ampTeamId in(" + inClause + ")) and (report.donorName is not null) and (report.termAssistName is not null) order by report.ampTeamId,report.donorName,report.termAssistName,report.fiscalYear";
			else
				queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.ampTeamId in(" + inClause + ")) and (report.actualStartDate='" + startDate + "' or report.actualCompletionDate='" + closeDate + "') and (report.donorName is not null) and (report.termAssistName is not null) order by report.ampTeamId,report.donorName,report.termAssistName,report.fiscalYear";
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
								donorTermAssist.setTotDonorUnDisbAmount(mf.format(totalComm-totalDisb));
								ampTeamDonors.getTotalDonorTermAssistFund().add(donorTermAssist);
								donorCommAmount=donorCommAmount + totalComm;
								donorPlDisbAmount=donorPlDisbAmount + totalPlDisb;
								donorDisbAmount=donorDisbAmount + totalDisb;
								donorExpAmount=donorExpAmount + totalExp;
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
								}
								if(donorTermAssist.getTermAssistName().equals("Kind"))
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
							ampTeamDonors.setDonorUnDisbAmount(mf.format(donorCommAmount-donorDisbAmount));
							report.getDonors().add(ampTeamDonors);
							teamCommAmount=teamCommAmount + donorCommAmount;
							teamDisbAmount=teamDisbAmount + donorDisbAmount;
							teamExpAmount=teamExpAmount + donorExpAmount;
							teamPlDisbAmount=teamPlDisbAmount + donorPlDisbAmount;
							
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
							report.setTeamUnDisbAmount(mf.format(teamCommAmount-teamDisbAmount));
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
									termFund.setTotDonorUnDisbAmount(mf.format(totalComm-totalDisb));
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
									termFund.setTotDonorUnDisbAmount(mf.format(totalComm-totalDisb));
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
									termFund.setTotDonorUnDisbAmount(mf.format(totalComm-totalDisb));
									totalComm=totalPlDisb=totalDisb=totalExp=0.0;
								}
								logger.debug("Added:'" + termFund.getTermAssistName());
								report.getTotalTeamTermAssistFund().add(termFund);	
							}
							totalComm=totalPlDisb=totalDisb=totalExp=0.0;
						donorCommAmount=donorPlDisbAmount=donorDisbAmount=donorExpAmount=0.0;
						teamCommAmount=teamPlDisbAmount=teamDisbAmount=teamExpAmount=0.0;
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
							donorTermAssist.setTotDonorUnDisbAmount(mf.format(totalComm-totalDisb));
							ampTeamDonors.getTotalDonorTermAssistFund().add(donorTermAssist);

							donorCommAmount=donorCommAmount + totalComm;
							donorPlDisbAmount=donorPlDisbAmount + totalPlDisb;
							donorDisbAmount=donorDisbAmount + totalDisb;
							donorExpAmount=donorExpAmount + totalExp;

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
							}
							if(donorTermAssist.getTermAssistName().equals("Kind"))
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
						ampTeamDonors.setDonorUnDisbAmount(mf.format(donorCommAmount-donorDisbAmount));
						report.getDonors().add(ampTeamDonors);

						teamCommAmount=teamCommAmount + donorCommAmount;
						teamDisbAmount=teamDisbAmount + donorDisbAmount;
						teamExpAmount=teamExpAmount + donorExpAmount;
						teamPlDisbAmount=teamPlDisbAmount + donorPlDisbAmount;
						
						totalComm=totalPlDisb=totalDisb=totalExp=0.0;
						donorCommAmount=donorPlDisbAmount=donorDisbAmount=donorExpAmount=0.0;
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
							donorTermAssist.setTotDonorUnDisbAmount(mf.format(totalComm - totalDisb));
							ampTeamDonors.getTotalDonorTermAssistFund().add(donorTermAssist);
							donorCommAmount=donorCommAmount + totalComm;
							donorPlDisbAmount=donorPlDisbAmount + totalPlDisb;
							donorDisbAmount=donorDisbAmount + totalDisb;
							donorExpAmount=donorExpAmount + totalExp;
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
							}
							if(donorTermAssist.getTermAssistName().equals("Kind"))
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
							}
						}
						totalComm=totalPlDisb=totalDisb=totalExp=0.0;
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
							amount=CurrencyWorker.convert1(ampReportCache.getActualCommitment().doubleValue(),fromExchangeRate,toExchangeRate);
							if(fiscalYear>=fromYr && fiscalYear<=toYr)
							{
								termFlag=1;
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
					donorTermAssist.setTotDonorUnDisbAmount(mf.format(totalComm-totalDisb));
					ampTeamDonors.getTotalDonorTermAssistFund().add(donorTermAssist);

					donorCommAmount=donorCommAmount + totalComm;
					donorPlDisbAmount=donorPlDisbAmount + totalPlDisb;
					donorDisbAmount=donorDisbAmount + totalDisb;
					donorExpAmount=donorExpAmount + totalExp;
					
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
					}
					if(donorTermAssist.getTermAssistName().equals("Kind"))
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
				ampTeamDonors.setDonorUnDisbAmount(mf.format(donorCommAmount-donorDisbAmount));
				report.getDonors().add(ampTeamDonors);

				teamCommAmount=teamCommAmount + donorCommAmount;
				teamDisbAmount=teamDisbAmount + donorDisbAmount;
				teamExpAmount=teamExpAmount + donorExpAmount;
				teamPlDisbAmount=teamPlDisbAmount + donorPlDisbAmount;
							
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
				report.setTeamUnDisbAmount(mf.format(teamCommAmount-teamDisbAmount));
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
						termFund.setTotDonorUnDisbAmount(mf.format(totalComm-totalDisb));
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
						termFund.setTotDonorUnDisbAmount(mf.format(totalComm-totalDisb));
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
						termFund.setTotDonorUnDisbAmount(mf.format(totalComm-totalDisb));
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
					logger.info(report.getSector());
					logger.info("if report not null");
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
						logger.info("Start adding project");
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
						logger.info("Project Added");
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
					logger.info("Adding Donor");
					report.getDonors().add(ampTeamDonors);
					logger.info("Donor Added");
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
					logger.info("Start Iter");
					Iterator sectorIter=sectorAssistance.iterator();
					while(sectorIter.hasNext())
					{
						String assist=(String) sectorIter.next();
						logger.info("Assist:" + assist + ":");
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
					logger.info("Outside Sector");
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
					queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.ampTeamId in(" + inClauseTeam + ")) and (report.ampActivityId in(" + inClause + ")) order by report.donorName,report.activityName,report.ampActivityId";
				else
					queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.ampTeamId in(" + inClauseTeam + ")) and (report.actualStartDate='" + startDate + "' or report.actualCompletionDate='" + closeDate + "') and (report.ampActivityId in(" + inClause + ")) order by report.donorName,report.activityName,report.ampActivityId";
				logger.debug("querystring: " + queryString);
				inClause=null;
				qry = session.createQuery(queryString);
		//		qry.setParameter("ampTeamId",ampTeamId,Hibernate.LONG) ;
				iterActivity=qry.list().iterator();
				while(iterActivity.hasNext())
				{
					AmpReportCache ampReportCache = (AmpReportCache) iterActivity.next();

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
						logger.info("Sector: " + ampProjectBySector.getSector().getSectorName());
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
						logger.info("Donor: " + ampTeamDonors.getDonorAgency());
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
						logger.info("inside donor");
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
						logger.info("Outside donor");
						
					}
					else
					if(ampReportCache.getAmpFundingId()!=null && ampReportCache.getDonorName().equals(ampTeamDonors.getDonorAgency()) && !(ampReportCache.getAmpActivityId().equals(project.getAmpActivityId())))
					{
						logger.info("inside project");
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
						logger.info("outside project");
					}
					else
					if(ampReportCache.getAmpFundingId()!=null && ampReportCache.getDonorName().equals(ampTeamDonors.getDonorAgency()) && ampReportCache.getAmpActivityId().equals(project.getAmpActivityId()) && !(ampReportCache.getTermAssistName().equals(termAssistName)))
					{
						logger.info("Inside Terms");
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
						logger.info("Outside Terms");
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
		double[][] loanDonorFunds=new double[(yrCount+1)][4];
		double[][] grantDonorFunds=new double[(yrCount+1)][4];
		double[][] kindDonorFunds=new double[(yrCount+1)][4];
		double[][] loanTeamFunds=new double[yrCount+1][4];
		double[][] grantTeamFunds=new double[yrCount+1][4];
		double[][] kindTeamFunds=new double[yrCount+1][4];
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
		double donorCommAmount=0.0;
		double donorDisbAmount=0.0;
		double donorExpAmount=0.0;
		double donorPlannedDisbAmount=0.0;
		double teamCommAmount=0.0;
		double teamDisbAmount=0.0;
		double teamExpAmount=0.0;
		double teamPlannedDisbAmount=0.0;
		int fiscalYearFlag=0;
		int termFlag=0;
		int donorCount=0;
		int projCount=0;
		int rowspan=1;
		String statusFlag=null;
		String inClause=null;
		Iterator iterSector=null;
		DecimalFormat mf = new DecimalFormat("###,###,###,###,###") ;
				
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

			session = PersistenceManager.getSession();
			if(startDate==null && closeDate==null)
				queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.donorName is not null) and (report.ampTeamId in(" + inClause + ")) order by report.ampTeamId,report.donorName,report.activityName,report.ampActivityId,report.termAssistName,report.fiscalYear";
			else
				queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.donorName is not null) and (report.ampTeamId in(" + inClause + ")) and (report.actualStartDate='" + startDate + "' or report.actualCompletionDate='" + closeDate + "') order by report.ampTeamId,report.donorName,report.activityName,report.ampActivityId,report.termAssistName,report.fiscalYear";
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
								termAssist.setTermUnDisbAmount(mf.format(totComm - totDisb));
								projCommAmount=projCommAmount + totComm;
								projDisbAmount=projDisbAmount + totDisb;
								projExpAmount=projExpAmount + totExp;
								projPlannedDisbAmount=projPlannedDisbAmount + totPlannedDisb;
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
							ampTeamDonors.setDonorUnDisbAmount(mf.format(donorCommAmount - donorDisbAmount));
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
									termFund.setTotDonorUnDisbAmount(mf.format(loanDonorFunds[yrCount][0] - loanDonorFunds[yrCount][1]));
									loanTeamFunds[yrCount][0]=loanTeamFunds[yrCount][0] + loanDonorFunds[yrCount][0];
									loanTeamFunds[yrCount][1]=loanTeamFunds[yrCount][1] + loanDonorFunds[yrCount][1];
									loanTeamFunds[yrCount][2]=loanTeamFunds[yrCount][2] + loanDonorFunds[yrCount][2];
									loanTeamFunds[yrCount][3]=loanTeamFunds[yrCount][3] + loanDonorFunds[yrCount][3];
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
									termFund.setTotDonorUnDisbAmount(mf.format(grantDonorFunds[yrCount][0] - grantDonorFunds[yrCount][1]));
									grantTeamFunds[yrCount][0]=grantTeamFunds[yrCount][0] + grantDonorFunds[yrCount][0];
									grantTeamFunds[yrCount][1]=grantTeamFunds[yrCount][1] + grantDonorFunds[yrCount][1];
									grantTeamFunds[yrCount][2]=grantTeamFunds[yrCount][2] + grantDonorFunds[yrCount][2];
									grantTeamFunds[yrCount][3]=grantTeamFunds[yrCount][3] + grantDonorFunds[yrCount][3];
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
									termFund.setTotDonorUnDisbAmount(mf.format(kindDonorFunds[yrCount][0] - kindDonorFunds[yrCount][1]));
									kindTeamFunds[yrCount][0]=kindTeamFunds[yrCount][0] + kindDonorFunds[yrCount][0];
									kindTeamFunds[yrCount][1]=kindTeamFunds[yrCount][1] + kindDonorFunds[yrCount][1];
									kindTeamFunds[yrCount][2]=kindTeamFunds[yrCount][2] + kindDonorFunds[yrCount][2];
									kindTeamFunds[yrCount][3]=kindTeamFunds[yrCount][3] + kindDonorFunds[yrCount][3];
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
									termFund.setTotDonorUnDisbAmount(mf.format(loanTeamFunds[yrCount][0] - loanTeamFunds[yrCount][1]));
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
									termFund.setTotDonorUnDisbAmount(mf.format(grantTeamFunds[yrCount][0] - grantTeamFunds[yrCount][1]));
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
									termFund.setTotDonorUnDisbAmount(mf.format(kindTeamFunds[yrCount][0] - kindTeamFunds[yrCount][1]));
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
							report.setTeamUnDisbAmount(mf.format(teamCommAmount - teamDisbAmount));
							projCommAmount=projDisbAmount=projExpAmount=projPlannedDisbAmount=0;
							donorCommAmount=donorDisbAmount=donorExpAmount=donorPlannedDisbAmount=0;
							teamCommAmount=teamDisbAmount=teamExpAmount=teamPlannedDisbAmount=0;
							totComm=totDisb=totExp=totPlannedDisb=0;
							for(int i=0;i<yrCount;i++)
							{
								termFunds[i][0]=termFunds[i][1]=termFunds[i][2]=termFunds[i][3]=0;
								projFunds[i][0]=projFunds[i][1]=projFunds[i][2]=projFunds[i][3]=0;
								donorFunds[i][0]=donorFunds[i][1]=donorFunds[i][2]=donorFunds[i][3]=0;
								teamFunds[i][0]=teamFunds[i][1]=teamFunds[i][2]=teamFunds[i][3]=0;
								loanDonorFunds[i][0]=loanDonorFunds[i][1]=loanDonorFunds[i][2]=loanDonorFunds[i][3]=0;
								grantDonorFunds[i][0]=grantDonorFunds[i][1]=grantDonorFunds[i][2]=grantDonorFunds[i][3]=0;
								kindDonorFunds[i][0]=kindDonorFunds[i][1]=kindDonorFunds[i][2]=kindDonorFunds[i][3]=0;
								loanTeamFunds[i][0]=loanTeamFunds[i][1]=loanTeamFunds[i][2]=loanTeamFunds[i][3]=0;
								grantTeamFunds[i][0]=grantTeamFunds[i][1]=grantTeamFunds[i][2]=grantTeamFunds[i][3]=0;
								kindTeamFunds[i][0]=kindTeamFunds[i][1]=kindTeamFunds[i][2]=kindTeamFunds[i][3]=0;
						}
						loanDonorFunds[yrCount][0]=loanDonorFunds[yrCount][1]=loanDonorFunds[yrCount][2]=loanDonorFunds[yrCount][3]=0;
						grantDonorFunds[yrCount][0]=grantDonorFunds[yrCount][1]=grantDonorFunds[yrCount][2]=grantDonorFunds[yrCount][3]=0;
						kindDonorFunds[yrCount][0]=kindDonorFunds[yrCount][1]=kindDonorFunds[yrCount][2]=kindDonorFunds[yrCount][3]=0;
						loanTeamFunds[yrCount][0]=loanTeamFunds[yrCount][1]=loanTeamFunds[yrCount][2]=loanTeamFunds[yrCount][3]=0;
						grantTeamFunds[yrCount][0]=grantTeamFunds[yrCount][1]=grantTeamFunds[yrCount][2]=grantTeamFunds[yrCount][3]=0;
						kindTeamFunds[yrCount][0]=kindTeamFunds[yrCount][1]=kindTeamFunds[yrCount][2]=kindTeamFunds[yrCount][3]=0;
						projCount=0;
									
						ampTeamDonors=null;
						if(ampDonorId.equals(All))
						{
							logger.debug("Inside Unspecified");
							queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.donorName is null) and (report.ampTeamId='" + selTeamId + "') order by report.activityName,report.ampActivityId";
							q = session.createQuery(queryString);	
							Iterator iterUn=q.list().iterator();
							if(q.list().size()>0)
							{
								while(iterUn.hasNext())
								{
									AmpReportCache ampCache= (AmpReportCache) iterUn.next();
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
							termAssist.setTermUnDisbAmount(mf.format(totComm - totDisb));
							projCommAmount=projCommAmount + totComm;
							projDisbAmount=projDisbAmount + totDisb;
							projExpAmount=projExpAmount + totExp;
							projPlannedDisbAmount=projPlannedDisbAmount + totPlannedDisb;
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
						ampTeamDonors.setDonorUnDisbAmount(mf.format(donorCommAmount - donorDisbAmount));
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
								termFund.setTotDonorUnDisbAmount(mf.format(loanDonorFunds[yrCount][0] - loanDonorFunds[yrCount][1]));
								loanTeamFunds[yrCount][0]=loanTeamFunds[yrCount][0] + loanDonorFunds[yrCount][0];
								loanTeamFunds[yrCount][1]=loanTeamFunds[yrCount][1] + loanDonorFunds[yrCount][1];
								loanTeamFunds[yrCount][2]=loanTeamFunds[yrCount][2] + loanDonorFunds[yrCount][2];
								loanTeamFunds[yrCount][3]=loanTeamFunds[yrCount][3] + loanDonorFunds[yrCount][3];
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
								termFund.setTotDonorUnDisbAmount(mf.format(grantDonorFunds[yrCount][0] - grantDonorFunds[yrCount][1]));
								grantTeamFunds[yrCount][0]=grantTeamFunds[yrCount][0] + grantDonorFunds[yrCount][0];
								grantTeamFunds[yrCount][1]=grantTeamFunds[yrCount][1] + grantDonorFunds[yrCount][1];
								grantTeamFunds[yrCount][2]=grantTeamFunds[yrCount][2] + grantDonorFunds[yrCount][2];
								grantTeamFunds[yrCount][3]=grantTeamFunds[yrCount][3] + grantDonorFunds[yrCount][3];
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
								termFund.setTotDonorUnDisbAmount(mf.format(kindDonorFunds[yrCount][0] - kindDonorFunds[yrCount][1]));
								kindTeamFunds[yrCount][0]=kindTeamFunds[yrCount][0] + kindDonorFunds[yrCount][0];
								kindTeamFunds[yrCount][1]=kindTeamFunds[yrCount][1] + kindDonorFunds[yrCount][1];
								kindTeamFunds[yrCount][2]=kindTeamFunds[yrCount][2] + kindDonorFunds[yrCount][2];
								kindTeamFunds[yrCount][3]=kindTeamFunds[yrCount][3] + kindDonorFunds[yrCount][3];
							}
							logger.debug("Added:'" + termFund.getTermAssistName());
							ampTeamDonors.getTotalDonorTermAssistFund().add(termFund);	
						}
						report.getDonors().add(ampTeamDonors);
						projCommAmount=projDisbAmount=projExpAmount=projPlannedDisbAmount=0;
						donorCommAmount=donorDisbAmount=donorExpAmount=donorPlannedDisbAmount=0;
						totComm=totDisb=totExp=0;
						for(int i=0;i<yrCount;i++)
						{
							termFunds[i][0]=termFunds[i][1]=termFunds[i][2]=termFunds[i][3]=0;
							projFunds[i][0]=projFunds[i][1]=projFunds[i][2]=projFunds[i][3]=0;
							donorFunds[i][0]=donorFunds[i][1]=donorFunds[i][2]=donorFunds[i][3]=0;
							loanDonorFunds[i][0]=loanDonorFunds[i][1]=loanDonorFunds[i][2]=loanDonorFunds[i][3]=0;
							grantDonorFunds[i][0]=grantDonorFunds[i][1]=grantDonorFunds[i][2]=grantDonorFunds[i][3]=0;
							kindDonorFunds[i][0]=kindDonorFunds[i][1]=kindDonorFunds[i][2]=kindDonorFunds[i][3]=0;
						}
						loanDonorFunds[yrCount][0]=loanDonorFunds[yrCount][1]=loanDonorFunds[yrCount][2]=loanDonorFunds[yrCount][3]=0;
						grantDonorFunds[yrCount][0]=grantDonorFunds[yrCount][1]=grantDonorFunds[yrCount][2]=grantDonorFunds[yrCount][3]=0;
						kindDonorFunds[yrCount][0]=kindDonorFunds[yrCount][1]=kindDonorFunds[yrCount][2]=kindDonorFunds[yrCount][3]=0;
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
							termAssist.setTermUnDisbAmount(mf.format(totComm - totDisb));
							projCommAmount=projCommAmount + totComm;
							projDisbAmount=projDisbAmount + totDisb;
							projExpAmount=projExpAmount + totExp;
							projPlannedDisbAmount=projPlannedDisbAmount + totPlannedDisb;
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
						projCommAmount=projDisbAmount=projExpAmount=projPlannedDisbAmount=0;
						totComm=totDisb=totExp=totPlannedDisb=0;
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
								termAssist.setTermUnDisbAmount(mf.format(totComm - totDisb));
								projCommAmount=projCommAmount + totComm;
								projDisbAmount=projDisbAmount + totDisb;
								projExpAmount=projExpAmount + totExp;
								projPlannedDisbAmount=projPlannedDisbAmount + totPlannedDisb;
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
						logger.debug("Actual Comm: " + amount);
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
						termAssist.setTermUnDisbAmount(mf.format(totComm - totDisb));
						projCommAmount=projCommAmount + totComm;
						projDisbAmount=projDisbAmount + totDisb;
						projExpAmount=projExpAmount + totExp;
						projPlannedDisbAmount=projPlannedDisbAmount + totPlannedDisb;
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
					ampTeamDonors.setDonorUnDisbAmount(mf.format(donorCommAmount - donorDisbAmount));
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
							termFund.setTotDonorUnDisbAmount(mf.format(loanDonorFunds[yrCount][0] - loanDonorFunds[yrCount][1]));
							loanTeamFunds[yrCount][0]=loanTeamFunds[yrCount][0] + loanDonorFunds[yrCount][0];
							loanTeamFunds[yrCount][1]=loanTeamFunds[yrCount][1] + loanDonorFunds[yrCount][1];
							loanTeamFunds[yrCount][2]=loanTeamFunds[yrCount][2] + loanDonorFunds[yrCount][2];
							loanTeamFunds[yrCount][3]=loanTeamFunds[yrCount][3] + loanDonorFunds[yrCount][3];
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
							termFund.setTotDonorUnDisbAmount(mf.format(grantDonorFunds[yrCount][0] - grantDonorFunds[yrCount][1]));
							grantTeamFunds[yrCount][0]=grantTeamFunds[yrCount][0] + grantDonorFunds[yrCount][0];
							grantTeamFunds[yrCount][1]=grantTeamFunds[yrCount][1] + grantDonorFunds[yrCount][1];
							grantTeamFunds[yrCount][2]=grantTeamFunds[yrCount][2] + grantDonorFunds[yrCount][2];
							grantTeamFunds[yrCount][3]=grantTeamFunds[yrCount][3] + grantDonorFunds[yrCount][3];
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
							termFund.setTotDonorUnDisbAmount(mf.format(kindDonorFunds[yrCount][0] - kindDonorFunds[yrCount][1]));
							kindTeamFunds[yrCount][0]=kindTeamFunds[yrCount][0] + kindDonorFunds[yrCount][0];
							kindTeamFunds[yrCount][1]=kindTeamFunds[yrCount][1] + kindDonorFunds[yrCount][1];
							kindTeamFunds[yrCount][2]=kindTeamFunds[yrCount][2] + kindDonorFunds[yrCount][2];
							kindTeamFunds[yrCount][3]=kindTeamFunds[yrCount][3] + kindDonorFunds[yrCount][3];
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
							termFund.setTotDonorUnDisbAmount(mf.format(loanTeamFunds[yrCount][0] - loanTeamFunds[yrCount][1]));
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
							termFund.setTotDonorUnDisbAmount(mf.format(grantTeamFunds[yrCount][0] - grantTeamFunds[yrCount][1]));
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
							termFund.setTotDonorUnDisbAmount(mf.format(kindTeamFunds[yrCount][0] - kindTeamFunds[yrCount][1]));
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
					report.setTeamUnDisbAmount(mf.format(teamCommAmount - teamDisbAmount));
					ampTeamDonors=null;
					projCount=0;
					if(ampDonorId.equals(All))
					{
						logger.debug("Inside Unspecified");
						queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.donorName is null) and (report.ampTeamId='" + selTeamId + "') order by report.activityName,report.ampActivityId";
						q = session.createQuery(queryString);	
						Iterator iterUn=q.list().iterator();
						if(q.list().size()>0)
						{
							while(iterUn.hasNext())
							{
								AmpReportCache ampCache= (AmpReportCache) iterUn.next();
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
		ArrayList status = new ArrayList() ;
		double actualCommitment=0.0;
		double actualExpenditure=0.0;	
		double amount=0.0;	
		String queryString = null;
		Iterator iter=null;
		Long All=new Long(0);
		int yrCount = (toYr - fromYr)+1;
		int donorCount=0;
		int projCount=0;
		int compCount=0;
		String inClause=null;
		double toExchangeRate=1.0;
		double fromExchangeRate=0.0;
		int expFlag=0;
		Iterator iterSector=null;
		DecimalFormat mf = new DecimalFormat("###,###,###,###,###.##") ;
				
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
			logger.info("Inclause: " + inClause);

			session = PersistenceManager.getSession();
			if(startDate==null && closeDate==null)
				queryString = "select report from " + AmpPhysicalComponentReport.class.getName() + " report where report.ampTeamId in(" + inClause + ") order by report.ampDonorId,report.ampActivityId,report.ampComponentId,report.reportingDate";
			else
				queryString = "select report from " + AmpPhysicalComponentReport.class.getName() + " report where report.ampTeamId in(" + inClause + ") and (report.actualStartDate='" + startDate + "' or report.actualCompletionDate='" + closeDate + "') order by report.ampDonorId,report.ampActivityId,report.ampComponentId,report.reportingDate";
				
	
			logger.info("querystring: " + queryString);
			q = session.createQuery(queryString);	
			multiReport report =null;
			AmpTeamDonors ampTeamDonors=null;
			AmpComponent component=null;
			Project project=null;
			if(q!=null)
			{
				iter = q.list().iterator();
				while(iter.hasNext())
				{
					AmpPhysicalComponentReport ampReportCache = (AmpPhysicalComponentReport) iter.next(); 
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
							component.getStatus().addAll(status);
							component.setAcCommitment(mf.format(actualCommitment/1000000.00));
							component.setAcExpenditure(mf.format(actualExpenditure/1000000.00));
							component.setAcBalance(mf.format((actualCommitment-actualExpenditure)/1000000.00));
							project.getComponent().add(component);
							ampTeamDonors.getProject().add(project);
							report.getDonors().add(ampTeamDonors);
							ampReports.add(report);
							status.clear();
							actualCommitment=0.0;
							actualExpenditure=0.0;
							donorCount=0;
							projCount=0;
							compCount=0;
							expFlag=0;
						}		
						report = new multiReport();
						AmpTeam ampTeam=DbUtil.getAmpTeam(ampReportCache.getAmpTeamId());
						report.setAmpTeamId(ampReportCache.getAmpTeamId());
						report.setTeamName(ampTeam.getName());
						report.setDonors(new ArrayList());
						ampTeamDonors=new AmpTeamDonors();
						ampTeamDonors.setDonorAgency(ampReportCache.getDonorName());
						ampTeamDonors.setDonorCount(++donorCount);
						ampTeamDonors.setProject(new ArrayList());
						project=new Project();
						project.setName(ampReportCache.getActivityName());
						project.setAmpActivityId(ampReportCache.getAmpActivityId());
						project.setCount(++projCount);
						project.setComponent(new ArrayList());
						component=new AmpComponent();
						component.setStatus(new ArrayList());
						component.setIssues(new ArrayList());
						component.setMeasures(new ArrayList());
						component.setResponsibleActor(new ArrayList());
						component.setName(ampReportCache.getComponentName());
						component.setObjective(ampReportCache.getObjective());
						component.setAmpComponentId(ampReportCache.getAmpComponentId());
						if(ampReportCache.getMofedContact() != null)
							component.getResponsibleActor().add(ampReportCache.getMofedContact());
						if(ampReportCache.getDonorContact() != null)
							component.getResponsibleActor().add(ampReportCache.getDonorContact());
						component.setSignatureDate(DateConversion.ConvertDateToString(ampReportCache.getSignatureDate()));
						component.setPlannedCompletionDate(DateConversion.ConvertDateToString(ampReportCache.getPlannedCompletionDate()));
						status.add(ampReportCache.getStatus());
						component.setCount(++compCount);
					}

					if(report.getAmpTeamId().equals(ampReportCache.getAmpTeamId()) && !(ampReportCache.getDonorName().equals(ampTeamDonors.getDonorAgency())))
					{
						logger.info("Inside Donor");
						component.getStatus().addAll(status);
						component.setAcCommitment(mf.format(actualCommitment/1000000.00));
						component.setAcExpenditure(mf.format(actualExpenditure/1000000.00));
						component.setAcBalance(mf.format((actualCommitment-actualExpenditure)/1000000.00));
						project.getComponent().add(component);
						ampTeamDonors.getProject().add(project);
						report.getDonors().add(ampTeamDonors);
						status.clear();
						actualCommitment=0.0;
						actualExpenditure=0.0;
						expFlag=0;
						compCount=0;
						projCount=0;
						ampTeamDonors=new AmpTeamDonors();
						ampTeamDonors.setDonorAgency(ampReportCache.getDonorName());
						ampTeamDonors.setDonorCount(++donorCount);
						ampTeamDonors.setProject(new ArrayList());
						project=new Project();
						project.setName(ampReportCache.getActivityName());
						project.setAmpActivityId(ampReportCache.getAmpActivityId());
						project.setCount(++projCount);
						project.setComponent(new ArrayList());
						component=new AmpComponent();
						component.setStatus(new ArrayList());
						component.setIssues(new ArrayList());
						component.setMeasures(new ArrayList());
						component.setResponsibleActor(new ArrayList());
						component.setName(ampReportCache.getComponentName());
						component.setObjective(ampReportCache.getObjective());
						component.setAmpComponentId(ampReportCache.getAmpComponentId());
						if(ampReportCache.getMofedContact() != null)
							component.getResponsibleActor().add(ampReportCache.getMofedContact());
						if(ampReportCache.getDonorContact() != null)
							component.getResponsibleActor().add(ampReportCache.getDonorContact());
						component.setSignatureDate(DateConversion.ConvertDateToString(ampReportCache.getSignatureDate()));
						component.setPlannedCompletionDate(DateConversion.ConvertDateToString(ampReportCache.getPlannedCompletionDate()));
						status.add(ampReportCache.getStatus());
						component.setCount(++compCount);
						logger.info("Outside Donor");
					}

					if(report.getAmpTeamId().equals(ampReportCache.getAmpTeamId()) && ampReportCache.getDonorName().equals(ampTeamDonors.getDonorAgency()) && !(ampReportCache.getAmpActivityId().equals(project.getAmpActivityId())))
					{
						logger.info("Inside Project");
						component.getStatus().addAll(status);
						component.setAcCommitment(mf.format(actualCommitment/1000000.00));
						component.setAcExpenditure(mf.format(actualExpenditure/1000000.00));
						component.setAcBalance(mf.format((actualCommitment-actualExpenditure)/1000000.00));
						project.getComponent().add(component);
						ampTeamDonors.getProject().add(project);
						compCount=0;
						status.clear();
						actualCommitment=0.0;
						actualExpenditure=0.0;
						expFlag=0;
						project=new Project();
						project.setName(ampReportCache.getActivityName());
						project.setAmpActivityId(ampReportCache.getAmpActivityId());
						project.setCount(++projCount);
						project.setComponent(new ArrayList());
						component=new AmpComponent();
						component.setStatus(new ArrayList());
						component.setIssues(new ArrayList());
						component.setMeasures(new ArrayList());
						component.setResponsibleActor(new ArrayList());
						component.setName(ampReportCache.getComponentName());
						component.setObjective(ampReportCache.getObjective());
						component.setAmpComponentId(ampReportCache.getAmpComponentId());
						if(ampReportCache.getMofedContact() != null)
							component.getResponsibleActor().add(ampReportCache.getMofedContact());
						if(ampReportCache.getDonorContact() != null)
							component.getResponsibleActor().add(ampReportCache.getDonorContact());
						component.setSignatureDate(DateConversion.ConvertDateToString(ampReportCache.getSignatureDate()));
						component.setPlannedCompletionDate(DateConversion.ConvertDateToString(ampReportCache.getPlannedCompletionDate()));
						status.add(ampReportCache.getStatus());
						component.setCount(++compCount);
						
						logger.info("Outside Project");
					}

					if(report.getAmpTeamId().equals(ampReportCache.getAmpTeamId()) && ampReportCache.getDonorName().equals(ampTeamDonors.getDonorAgency()) && ampReportCache.getAmpActivityId().equals(project.getAmpActivityId()) && !(ampReportCache.getAmpComponentId().equals(component.getAmpComponentId())))
					{
						logger.info("Inside Component");
						component.getStatus().addAll(status);
						component.setAcCommitment(mf.format(actualCommitment/1000000.00));
						component.setAcExpenditure(mf.format(actualExpenditure/1000000.00));
						component.setAcBalance(mf.format((actualCommitment-actualExpenditure)/1000000.00));
						project.getComponent().add(component);
						status.clear();
						actualCommitment=0.0;
						actualExpenditure=0.0;
						expFlag=0;
						component=new AmpComponent();
						component.setStatus(new ArrayList());
						component.setIssues(new ArrayList());
						component.setMeasures(new ArrayList());
						component.setResponsibleActor(new ArrayList());
						component.setName(ampReportCache.getComponentName());
						component.setObjective(ampReportCache.getObjective());
						component.setAmpComponentId(ampReportCache.getAmpComponentId());
						if(ampReportCache.getMofedContact() != null)
							component.getResponsibleActor().add(ampReportCache.getMofedContact());
						if(ampReportCache.getDonorContact() != null)
							component.getResponsibleActor().add(ampReportCache.getDonorContact());
						component.setSignatureDate(DateConversion.ConvertDateToString(ampReportCache.getSignatureDate()));
						component.setPlannedCompletionDate(DateConversion.ConvertDateToString(ampReportCache.getPlannedCompletionDate()));
						status.add(ampReportCache.getStatus());
						component.setCount(++compCount);
						
						logger.info("Outside Component");
					}
							
										
					if(ampReportCache.getActualCommitment().doubleValue()>0 && ampReportCache.getCommCurrencyCode()!=null)
					{
						logger.info("Inside Fund Calculator");
						
						if(ampReportCache.getCommCurrencyCode().equals("USD"))
							fromExchangeRate=1.0;
						else
							fromExchangeRate=DbUtil.getExchangeRate(ampReportCache.getCommCurrencyCode(),Constants.ACTUAL,ampReportCache.getTransactionDate());
						if(ampCurrencyCode.equals("USD"))
							toExchangeRate=1.0;
						else
							toExchangeRate=DbUtil.getExchangeRate(ampCurrencyCode,Constants.ACTUAL,ampReportCache.getTransactionDate());
						
						amount=CurrencyWorker.convert1(ampReportCache.getActualCommitment().doubleValue(),fromExchangeRate,toExchangeRate);
						actualCommitment=actualCommitment + amount;
					}

					amount=0.0;
					if(expFlag==0)
					{
						if(ampReportCache.getExpCurrencyCode().equals("USD"))
							fromExchangeRate=1.0;
						else
							fromExchangeRate=DbUtil.getExchangeRate(ampReportCache.getExpCurrencyCode(),Constants.ACTUAL,ampReportCache.getReportingDate());
						if(ampCurrencyCode.equals("USD"))
							toExchangeRate=1.0;
						else
							toExchangeRate=DbUtil.getExchangeRate(ampCurrencyCode,Constants.ACTUAL,ampReportCache.getReportingDate());
						amount=CurrencyWorker.convert1(ampReportCache.getActualExpenditure().doubleValue(),fromExchangeRate,toExchangeRate);
						actualExpenditure=actualExpenditure + amount;
						expFlag=1;
					}			
					if(status.indexOf(ampReportCache.getStatus())==-1)
						status.add(ampReportCache.getStatus());
				}
				if(report!=null)
				{
					component.getStatus().addAll(status);
					component.setAcCommitment(mf.format(actualCommitment/1000000));
					component.setAcExpenditure(mf.format(actualExpenditure/1000000));
					component.setAcBalance(mf.format((actualCommitment-actualExpenditure)/1000000));
					project.getComponent().add(component);
					ampTeamDonors.getProject().add(project);
					report.getDonors().add(ampTeamDonors);
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
					logger.info(" >>>> " + str);
				}
				Long lg = new Long("0");
				session = PersistenceManager.getSession();
				String queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.ampTeamId in(" + inClause + ")) ";
				logger.info( " Query 2 :" + queryString);
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
								//logger.info(  reportCachePrev.getActivityName()+ " ::" + reportCachePrev.getAmpActivityId()+ " Not Equak : " + actCommit);
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
				logger.info( result.size()+ " : Record count : ");
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
		ArrayList selection=new ArrayList();
		ReportSelectionCriteria rsc = new ReportSelectionCriteria();
		rsc.setColumns(new ArrayList());
		Column c=null;
				
		try 
		{
			session = PersistenceManager.getSession();
					
			String queryString = "select ar from "
				+ AmpReports.class.getName()
				+ " ar where (ar.ampReportId='" + ampReportId + "')";

			Query qry = session.createQuery(queryString);
			logger.info("Query: " + queryString);
			Iterator itr = qry.list().iterator();
			if(itr.hasNext())
			{
				AmpReports ampReports=(AmpReports) itr.next();
				logger.info("Report Name: " + ampReports.getName());
				logger.info("Column Size: " + ampReports.getColumns().size());
				itr=ampReports.getColumns().iterator();
			}

			while (itr.hasNext()) 
			{
				AmpReportColumn report = (AmpReportColumn) itr.next();
				c=new Column();
				c.setColumnId(report.getColumn().getColumnId());
				c.setColumnName(report.getColumn().getColumnName());
				c.setColumnAlias(report.getColumn().getAliasName());
				selection.add(c);
				
			}
			rsc.getColumns().addAll(selection);
			rsc.setMeasure(new Long(1));


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

	public static ArrayList getAdvancedReport(Long ampTeamId,int fromYr,int toYr,String perspective,String ampCurrencyCode,Long ampModalityId,Long ampStatusId,Long ampDonorId,Long ampSectorId,int fiscalCalId,String startDate,String closeDate,String region,Long ampReportId)
	{
		Session session = null ;
		Query q = null ;
		ArrayList ampReports = new ArrayList() ;
		Collection columns=new ArrayList();
		Report reports=null;
		String queryString = null;
		Iterator iter=null;
		Iterator iterColumn=null;
		Long All=new Long(0);
		double actualCommitment=0;
		double actualDisbursement=0;
		int flag=0;
		int count=1;
		int yrCount=(toYr-fromYr) +1;
		ArrayList donors=new ArrayList();
		ArrayList sectors=new ArrayList();
		ArrayList assistance=new ArrayList();
		ArrayList regions=new ArrayList();
		String level=null;
		String status=null;
		String actualStartDate=null;
		String actualCompletionDate=null;
		double[][] termFunds=new double[yrCount][3];
		Long measure=null;
		String inClause=null;
		String orderClause=null;
		String title=null;

		double toExchangeRate=1.0;
		double fromExchangeRate=0.0;
		double amount=0.0;
		int fiscalYear=0,fiscalQuarter=0;
		
		double totActualComm=0;
		double totActualDisb=0;
		
		Iterator iterSector=null;
		
		DecimalFormat mf = new DecimalFormat("###,###,###,###,###") ;
		Collection currencies=null;
		int firstColumn=0;	
		AdvancedReport report=null;
		AmpReportCache ampReportCache = null;
		try
		{
//			int yrCount = toYr - fromYr;
			int years=0;
//			yrCount = (yrCount * 2) + 2;
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

			ReportSelectionCriteria rsc=ReportUtil.getReportSelectionCriteria(ampReportId);
			columns=rsc.getColumns();
			measure=rsc.getMeasure();
			iter=columns.iterator();
			while(iter.hasNext())
			{
				Column c=(Column) iter.next();
				if(orderClause==null)
					orderClause= "report.activityName,report.ampActivityId";
				if(!(c.getColumnId().equals(Constants.ACTIVITY_NAME)))
					orderClause= orderClause + ",report." + c.getColumnAlias();
			}
			currencies=DbUtil.getAmpCurrencyRate();
			logger.info("Inclause: " + inClause);
			session = PersistenceManager.getSession();
			if(startDate==null && closeDate==null)
				queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.ampTeamId in(" + inClause + ")) order by " + orderClause;
			else
				queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.ampTeamId in(" + inClause + ")) and (report.actualStartDate='" + startDate + "' or report.actualCompletionDate='" + closeDate + "') order by " + orderClause;
			logger.info("querystring: " + queryString);
			q = session.createQuery(queryString);	
			logger.info("Query Result: " + q.list().size());
//			q.setParameter("ampTeamId",ampTeamId,Hibernate.LONG) ;
			
	//		report.setProjects(new ArrayList());
	//		Project project=null;
			if(q!=null)
			{
				iter = q.list().iterator();
				while(iter.hasNext())
				{
					ampReportCache = (AmpReportCache) iter.next(); 

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
		/*					if(fiscalYear<fromYr || fiscalYear>toYr)
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
					
					if(reports==null || !(reports.getAmpActivityId().equals(ampReportCache.getAmpActivityId())))
					{
						if(reports!=null)
						{
							iterColumn=columns.iterator();
							while(iterColumn.hasNext())
							{
								logger.info("Begin while");
								report=new AdvancedReport();
								report.setDonors(new ArrayList());
								report.setAssistance(new ArrayList());
								Column c=(Column) iterColumn.next();
								if(c.getColumnId().equals(Constants.STATUS_NAME))
									report.setStatus(status);
								if(c.getColumnId().equals(Constants.DONOR_NAME))
								{
									if(donors.size()==0)
										report.getDonors().add("Unspecified");
									else
										report.getDonors().addAll(donors);
								}
								if(c.getColumnId().equals(Constants.ACTUAL_START_DATE))
									report.setActualStartDate(actualStartDate);
								if(c.getColumnId().equals(Constants.ACTIVITY_NAME))
									report.setTitle(title);
								if(c.getColumnId().equals(Constants.TERM_ASSIST_NAME))
								{
									if(assistance.size()==0)
										report.getAssistance().add("Unspecified");
									else
										report.setAssistance(assistance);
								}
								if(c.getColumnId().equals(Constants.ACTUAL_COMMITMENT))
									report.setActualCommitment(mf.format(actualCommitment));
								if(c.getColumnId().equals(Constants.LEVEL_NAME))
									report.setLevel(level);
								if(c.getColumnId().equals(Constants.ACTUAL_COMPLETION_DATE))
									report.setActualCompletionDate(actualCompletionDate);
								reports.getRecords().add(report);

							}
							report=new AdvancedReport();
							report.setAmpFund(new ArrayList());
							for(int i=0;i<yrCount;i++)
							{
								AmpFund ampFund=new AmpFund();
								ampFund.setCommAmount(mf.format(termFunds[i][0])); 
								ampFund.setDisbAmount(mf.format(termFunds[i][1])); 
								ampFund.setExpAmount(mf.format(termFunds[i][2]));	
								report.getAmpFund().add(ampFund);
							}
							reports.getRecords().add(report);
							ampReports.add(reports);
							actualCommitment=0;
							actualDisbursement=0;
							donors.clear();
							assistance.clear();
							for(int i=0;i<yrCount;i++)
								termFunds[i][0]=termFunds[i][1]=termFunds[i][2]=0;
							
						}
						reports= new Report();
						reports.setRecords(new ArrayList());
						logger.info("Init Record");
						title=ampReportCache.getActivityName();
						reports.setAmpActivityId(ampReportCache.getAmpActivityId());
						if(ampReportCache.getLevelName().equals("Not Exist"))
							level="Unspecified";
						else
							level=ampReportCache.getLevelName();
						if(ampReportCache.getStatusName()!=null)
							status=ampReportCache.getStatusName();
						/*if(DbUtil.getAmpReportSector(ampReportCache.getAmpActivityId()).size()==0)
							sectors.add("Unspecified");
						else
							sectors.addAll(DbUtil.getAmpReportSector(ampReportCache.getAmpActivityId()));
						if(DbUtil.getAmpReportLocation(ampReportCache.getAmpActivityId()).size()==0)
							regions.add("Unspecified");
						else
							regions.addAll(DbUtil.getAmpReportLocation(ampReportCache.getAmpActivityId()));*/
						if(ampReportCache.getTermAssistName()!=null)
							assistance.add(ampReportCache.getTermAssistName());
						if(ampReportCache.getDonorName()!=null)
							donors.add(ampReportCache.getDonorName());
						if(ampReportCache.getActualStartDate()!=null)
							actualStartDate=DateConversion.ConvertDateToString(ampReportCache.getActualStartDate());
						else
							actualStartDate="";
						if(ampReportCache.getActualCompletionDate()!=null)
							actualCompletionDate=DateConversion.ConvertDateToString(ampReportCache.getActualCompletionDate());
						else
							actualCompletionDate="";
					}
					logger.info("Title:" + title);
					if(donors.indexOf(ampReportCache.getDonorName())==-1 && ampReportCache.getDonorName()!=null)
						donors.add(ampReportCache.getDonorName());
					if(assistance.indexOf(ampReportCache.getTermAssistName())==-1 && ampReportCache.getTermAssistName()!=null)
						assistance.add(ampReportCache.getTermAssistName());
					logger.info("Measure: " + measure);
					if(ampReportCache.getFiscalYear()!=null && ampReportCache.getFiscalQuarter()!=null)
					{
						logger.info("begin if");
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
						if(measure.equals(new Long(Constants.ACTUAL)))
						{	
							if(ampReportCache.getActualCommitment().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
							{
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
							amount=0.0;
							if(ampReportCache.getActualDisbursement().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
							{
								amount=CurrencyWorker.convert1(ampReportCache.getActualDisbursement().doubleValue(),fromExchangeRate,toExchangeRate);
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
									if(termFunds[fiscalYear%fromYr][2]==0)
										termFunds[fiscalYear%fromYr][2]=amount;
									else
									if(termFunds[fiscalYear%fromYr][2]>0)
										termFunds[fiscalYear%fromYr][2]=termFunds[fiscalYear%fromYr][2] + amount;
								}
							}
							amount=0.0;
						}
						else
						{
							if(ampReportCache.getPlannedCommitment().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
							{
								amount=CurrencyWorker.convert1(ampReportCache.getPlannedCommitment().doubleValue(),fromExchangeRate,toExchangeRate);
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
							if(ampReportCache.getPlannedDisbursement().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
							{
								amount=CurrencyWorker.convert1(ampReportCache.getPlannedDisbursement().doubleValue(),fromExchangeRate,toExchangeRate);
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
							if(ampReportCache.getPlannedExpenditure().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
							{
								amount=CurrencyWorker.convert1(ampReportCache.getPlannedExpenditure().doubleValue(),fromExchangeRate,toExchangeRate);
								if(fiscalYear>=fromYr && fiscalYear<=toYr)
								{
									if(termFunds[fiscalYear%fromYr][2]==0)
										termFunds[fiscalYear%fromYr][2]=amount;
									else
									if(termFunds[fiscalYear%fromYr][2]>0)
										termFunds[fiscalYear%fromYr][2]=termFunds[fiscalYear%fromYr][2] + amount;
								}
							}
							amount=0.0;
						}	
						//logger.info("End If");
						
					}
					
				}
				//logger.info("At the end");
				iterColumn=columns.iterator();
				while(iterColumn.hasNext())
				{
					report=new AdvancedReport();
					report.setDonors(new ArrayList());
					report.setAssistance(new ArrayList());
					Column c=(Column) iterColumn.next();
					if(c.getColumnId().equals(Constants.STATUS_NAME))
						report.setStatus(status);
					if(c.getColumnId().equals(Constants.DONOR_NAME))
					{
						if(donors.size()==0)
							report.getDonors().add("Unspecified");
						else
							report.getDonors().addAll(donors);
					}
					if(c.getColumnId().equals(Constants.ACTUAL_START_DATE))
						report.setActualStartDate(actualStartDate);
					if(c.getColumnId().equals(Constants.ACTIVITY_NAME))
						report.setTitle(title);
					if(c.getColumnId().equals(Constants.TERM_ASSIST_NAME))
					{
						if(assistance.size()==0)
							report.getAssistance().add("Unspecified");
						else
							report.setAssistance(assistance);
					}
					if(c.getColumnId().equals(Constants.ACTUAL_COMMITMENT))
						report.setActualCommitment(mf.format(actualCommitment));
					if(c.getColumnId().equals(Constants.LEVEL_NAME))
						report.setLevel(level);
					if(c.getColumnId().equals(Constants.ACTUAL_COMPLETION_DATE))
						report.setActualCompletionDate(actualCompletionDate);
					reports.getRecords().add(report);
				}
				report=new AdvancedReport();
				report.setAmpFund(new ArrayList());
				for(int i=0;i<yrCount;i++)
				{
					AmpFund ampFund=new AmpFund();
					ampFund.setCommAmount(mf.format(termFunds[i][0])); 
					ampFund.setDisbAmount(mf.format(termFunds[i][1])); 
					ampFund.setExpAmount(mf.format(termFunds[i][2]));	
					report.getAmpFund().add(ampFund);
				}
				reports.getRecords().add(report);
				ampReports.add(reports);
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

//---------New Function-----------------------------------------------------------------------------
	public static ArrayList generateAdvancedReport(Long ampTeamId,int fromYr,int toYr, int fiscalCalId,String ampCurrencyCode, String perspective, Long measure, Collection addedColumns)
	{
		Session session = null ;
		Query q = null ;
		ArrayList ampReports = new ArrayList() ;
		Collection columns=new ArrayList();
		Report reports=null;
		String queryString = null;
		Iterator iter=null;
		Iterator iterColumn=null;
		Long All=new Long(0);
		double actualCommitment=0;
		double actualDisbursement=0;
		int flag=0;
		int count=1;
		int yrCount=(toYr-fromYr) +1;
		ArrayList donors=new ArrayList();
		ArrayList sectors=new ArrayList();
		ArrayList assistance=new ArrayList();
		ArrayList regions=new ArrayList();
		String level=null;
		String status=null;
		String actualStartDate=null;
		String actualCompletionDate=null;
		double[][] termFunds=new double[yrCount][3];
	
		String inClause=null;
		String orderClause=null;
		String title=null;

		double toExchangeRate=1.0;
		double fromExchangeRate=0.0;
		double amount=0.0;
		int fiscalYear=0,fiscalQuarter=0;
		
		double totActualComm=0;
		double totActualDisb=0;
		
		Iterator iterSector=null;
		
		DecimalFormat mf = new DecimalFormat("###,###,###,###,###") ;
		Collection currencies=null;
		int firstColumn=0;	
		AdvancedReport report=null;
		AmpReportCache ampReportCache = null;
		try
		{
//			int yrCount = toYr - fromYr;
			int years=0;
//			yrCount = (yrCount * 2) + 2;
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

			
			columns = addedColumns;
			logger.info("Column Size: " + columns.size());
			iter = columns.iterator();
			while(iter.hasNext())
			{
				AmpColumns ampColuns =(AmpColumns) iter.next();
				if(orderClause==null)
					orderClause= "report.activityName,report.ampActivityId";
				if(!(ampColuns.getColumnId().equals(Constants.ACTIVITY_NAME)))
					orderClause= orderClause + ",report." + ampColuns.getAliasName();
			}

			currencies=DbUtil.getAmpCurrencyRate();
			logger.info("Inclause: " + inClause);
			session = PersistenceManager.getSession();
			queryString = "select report from " + AmpReportCache.class.getName() + " report where (report.ampTeamId in(" + inClause + ")) order by " + orderClause;
			logger.info("querystring: " + queryString);
			q = session.createQuery(queryString);	
			logger.info("Query Result: " + q.list().size());

			if(q!=null)
			{
				iter = q.list().iterator();
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
								//logger.info("Begin while");
								report=new AdvancedReport();
								report.setDonors(new ArrayList());
								report.setAssistance(new ArrayList());
								AmpColumns c=(AmpColumns) iterColumn.next();
								if(c.getColumnId().equals(Constants.STATUS_NAME))
									report.setStatus(status);
								if(c.getColumnId().equals(Constants.DONOR_NAME))
								{
									if(donors.size()==0)
										report.getDonors().add("Unspecified");
									else
										report.getDonors().addAll(donors);
								}
								if(c.getColumnId().equals(Constants.ACTUAL_START_DATE))
									report.setActualStartDate(actualStartDate);
								if(c.getColumnId().equals(Constants.ACTIVITY_NAME))
									report.setTitle(title);
								
								if(c.getColumnId().equals(Constants.TERM_ASSIST_NAME))
								{
									if(assistance.size()==0)
										report.getAssistance().add("Unspecified");
									else
										report.setAssistance(assistance);
								}
								if(c.getColumnId().equals(Constants.ACTUAL_COMMITMENT))
									report.setActualCommitment(mf.format(actualCommitment));
								
								if(c.getColumnId().equals(Constants.LEVEL_NAME))
									report.setLevel(level);
								if(c.getColumnId().equals(Constants.ACTUAL_COMPLETION_DATE))
									report.setActualCompletionDate(actualCompletionDate);
								reports.getRecords().add(report);

							}
							
							report=new AdvancedReport();
							report.setAmpFund(new ArrayList());
							for(int i=0;i<yrCount;i++)
							{
								AmpFund ampFund=new AmpFund();
								ampFund.setCommAmount(mf.format(termFunds[i][0])); 
								ampFund.setDisbAmount(mf.format(termFunds[i][1])); 
								ampFund.setExpAmount(mf.format(termFunds[i][2]));	
								report.getAmpFund().add(ampFund);
							}
							reports.getRecords().add(report);
							ampReports.add(reports);
							actualCommitment=0;
							actualDisbursement=0;
							donors.clear();
							assistance.clear();
							for(int i=0;i<yrCount;i++)
								termFunds[i][0]=termFunds[i][1]=termFunds[i][2]=0;
							
						} // CHECK IF REPORT != NULL
						reports= new Report();
						reports.setRecords(new ArrayList());
						//logger.info("Init Record");
						title=ampReportCache.getActivityName();
						reports.setAmpActivityId(ampReportCache.getAmpActivityId());
						if(ampReportCache.getLevelName().equals("Not Exist"))
							level="Unspecified";
						else
							level=ampReportCache.getLevelName();
						if(ampReportCache.getStatusName()!=null)
							status=ampReportCache.getStatusName();
						/*if(DbUtil.getAmpReportSector(ampReportCache.getAmpActivityId()).size()==0)
							sectors.add("Unspecified");
						else
							sectors.addAll(DbUtil.getAmpReportSector(ampReportCache.getAmpActivityId()));
						if(DbUtil.getAmpReportLocation(ampReportCache.getAmpActivityId()).size()==0)
							regions.add("Unspecified");
						else
							regions.addAll(DbUtil.getAmpReportLocation(ampReportCache.getAmpActivityId()));*/
						if(ampReportCache.getTermAssistName()!=null)
							assistance.add(ampReportCache.getTermAssistName());
						if(ampReportCache.getDonorName()!=null)
							donors.add(ampReportCache.getDonorName());
						if(ampReportCache.getActualStartDate()!=null)
							actualStartDate=DateConversion.ConvertDateToString(ampReportCache.getActualStartDate());
						else
							actualStartDate="";
						if(ampReportCache.getActualCompletionDate()!=null)
							actualCompletionDate=DateConversion.ConvertDateToString(ampReportCache.getActualCompletionDate());
						else
							actualCompletionDate="";
					}
					//logger.info("Title:" + title);
					if(donors.indexOf(ampReportCache.getDonorName())==-1 && ampReportCache.getDonorName()!=null)
						donors.add(ampReportCache.getDonorName());
					if(assistance.indexOf(ampReportCache.getTermAssistName())==-1 && ampReportCache.getTermAssistName()!=null)
						assistance.add(ampReportCache.getTermAssistName());
					
					//logger.info("Measure: " + measure);
					if(ampReportCache.getFiscalYear()!=null && ampReportCache.getFiscalQuarter()!=null)
					{
						//logger.info("begin if");
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
						if(measure.equals(new Long(Constants.ACTUAL)))
						{	
							
							if(ampReportCache.getActualCommitment().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
							{
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
							//amount=0.0;
							if(ampReportCache.getActualDisbursement().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
							{
								amount=CurrencyWorker.convert1(ampReportCache.getActualDisbursement().doubleValue(),fromExchangeRate,toExchangeRate);
								if(fiscalYear>=fromYr && fiscalYear<=toYr)
								{
									if(termFunds[fiscalYear%fromYr][1]==0)
										termFunds[fiscalYear%fromYr][1]=amount;
									else
									if(termFunds[fiscalYear%fromYr][1]>0)
										termFunds[fiscalYear%fromYr][1]=termFunds[fiscalYear%fromYr][1] + amount;
								}
							}
							//amount=0.0;
							if(ampReportCache.getActualExpenditure().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
							{
								amount=CurrencyWorker.convert1(ampReportCache.getActualExpenditure().doubleValue(),fromExchangeRate,toExchangeRate);
								if(fiscalYear>=fromYr && fiscalYear<=toYr)
								{
									if(termFunds[fiscalYear%fromYr][2]==0)
										termFunds[fiscalYear%fromYr][2]=amount;
									else
									if(termFunds[fiscalYear%fromYr][2]>0)
										termFunds[fiscalYear%fromYr][2]=termFunds[fiscalYear%fromYr][2] + amount;
								}
							}
//							amount=0.0;
						}
						else
						{
							if(ampReportCache.getPlannedCommitment().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
							{
								amount=CurrencyWorker.convert1(ampReportCache.getPlannedCommitment().doubleValue(),fromExchangeRate,toExchangeRate);
								if(fiscalYear>=fromYr && fiscalYear<=toYr)
								{
									if(termFunds[fiscalYear%fromYr][0]==0)
										termFunds[fiscalYear%fromYr][0]=amount;
									else
									if(termFunds[fiscalYear%fromYr][0]>0)
										termFunds[fiscalYear%fromYr][0]=termFunds[fiscalYear%fromYr][0] + amount;
								}
							}
							//amount=0.0;
							if(ampReportCache.getPlannedDisbursement().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
							{
								amount=CurrencyWorker.convert1(ampReportCache.getPlannedDisbursement().doubleValue(),fromExchangeRate,toExchangeRate);
								if(fiscalYear>=fromYr && fiscalYear<=toYr)
								{
									if(termFunds[fiscalYear%fromYr][1]==0)
										termFunds[fiscalYear%fromYr][1]=amount;
									else
									if(termFunds[fiscalYear%fromYr][1]>0)
										termFunds[fiscalYear%fromYr][1]=termFunds[fiscalYear%fromYr][1] + amount;
								}
							}
							//amount=0.0;
							if(ampReportCache.getPlannedExpenditure().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
							{
								amount=CurrencyWorker.convert1(ampReportCache.getPlannedExpenditure().doubleValue(),fromExchangeRate,toExchangeRate);
								if(fiscalYear>=fromYr && fiscalYear<=toYr)
								{
									if(termFunds[fiscalYear%fromYr][2]==0)
										termFunds[fiscalYear%fromYr][2]=amount;
									else
									if(termFunds[fiscalYear%fromYr][2]>0)
										termFunds[fiscalYear%fromYr][2]=termFunds[fiscalYear%fromYr][2] + amount;
								}
							}
							//amount=0.0;
						}	
						//logger.info("End If");
						
					} // end of Fiscal Year and Fiscal Quarter CHecek
					
				}// End of One AmpReportCache row........
				
				//logger.info("At the end");
				iterColumn=columns.iterator();
				while(iterColumn.hasNext())
				{
					report=new AdvancedReport();
					report.setDonors(new ArrayList());
					report.setAssistance(new ArrayList());
					AmpColumns c=(AmpColumns) iterColumn.next();
					if(c.getColumnId().equals(Constants.STATUS_NAME))
						report.setStatus(status);
					if(c.getColumnId().equals(Constants.DONOR_NAME))
					{
						if(donors.size()==0)
							report.getDonors().add("Unspecified");
						else
							report.getDonors().addAll(donors);
					}
					if(c.getColumnId().equals(Constants.ACTUAL_START_DATE))
						report.setActualStartDate(actualStartDate);
					if(c.getColumnId().equals(Constants.ACTIVITY_NAME))
						report.setTitle(title);
					if(c.getColumnId().equals(Constants.TERM_ASSIST_NAME))
					{
						if(assistance.size()==0)
							report.getAssistance().add("Unspecified");
						else
							report.setAssistance(assistance);
					}
					if(c.getColumnId().equals(Constants.ACTUAL_COMMITMENT))
						report.setActualCommitment(mf.format(actualCommitment));
					if(c.getColumnId().equals(Constants.LEVEL_NAME))
						report.setLevel(level);
					if(c.getColumnId().equals(Constants.ACTUAL_COMPLETION_DATE))
						report.setActualCompletionDate(actualCompletionDate);
					
					reports.getRecords().add(report);
				}
				report=new AdvancedReport();
				report.setAmpFund(new ArrayList());
				for(int i=0;i<yrCount;i++)
				{
					AmpFund ampFund=new AmpFund();
					ampFund.setCommAmount(mf.format(termFunds[i][0])); 
					ampFund.setDisbAmount(mf.format(termFunds[i][1])); 
					ampFund.setExpAmount(mf.format(termFunds[i][2]));	
					report.getAmpFund().add(ampFund);
				}
				reports.getRecords().add(report);
				ampReports.add(reports);
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

// end of Advanced Function	

}

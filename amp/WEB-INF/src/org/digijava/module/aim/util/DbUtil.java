package org.digijava.module.aim.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.Collator;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.util.LabelValueBean;
import org.bouncycastle.cms.CMSException;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.viewfetcher.InternationalizedModelDescription;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.user.Group;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityBudgetStructure;
import org.digijava.module.aim.dbentity.AmpActivityGroup;
import org.digijava.module.aim.dbentity.AmpActivityInternalId;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpAhsurvey;
import org.digijava.module.aim.dbentity.AmpAhsurveyIndicator;
import org.digijava.module.aim.dbentity.AmpAhsurveyQuestion;
import org.digijava.module.aim.dbentity.AmpAhsurveyResponse;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpComments;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpContactProperty;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpDesktopTabSelection;
import org.digijava.module.aim.dbentity.AmpField;
import org.digijava.module.aim.dbentity.AmpFilters;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpFundingMTEFProjection;
import org.digijava.module.aim.dbentity.AmpGPISurveyIndicator;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.digijava.module.aim.dbentity.AmpLevel;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgRecipient;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpOrganisationContact;
import org.digijava.module.aim.dbentity.AmpOrganizationBudgetInformation;
import org.digijava.module.aim.dbentity.AmpPages;
import org.digijava.module.aim.dbentity.AmpPhysicalComponentReport;
import org.digijava.module.aim.dbentity.AmpPhysicalPerformance;
import org.digijava.module.aim.dbentity.AmpReportCache;
import org.digijava.module.aim.dbentity.AmpReportLocation;
import org.digijava.module.aim.dbentity.AmpReportLog;
import org.digijava.module.aim.dbentity.AmpReportPhysicalPerformance;
import org.digijava.module.aim.dbentity.AmpReportSector;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpSectorScheme;
import org.digijava.module.aim.dbentity.AmpStatus;
import org.digijava.module.aim.dbentity.AmpStructureImg;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTeamPageFilters;
import org.digijava.module.aim.dbentity.AmpTeamReports;
import org.digijava.module.aim.dbentity.AmpTermsAssist;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.AmpUserExtension;
import org.digijava.module.aim.dbentity.CMSContentItem;
import org.digijava.module.aim.dbentity.EUActivity;
import org.digijava.module.aim.dbentity.EUActivityContribution;
import org.digijava.module.aim.dbentity.IPAContract;
import org.digijava.module.aim.dbentity.IndicatorActivity;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.AmpPrgIndicatorValue;
import org.digijava.module.aim.helper.AmpProjectBySector;
import org.digijava.module.aim.helper.Assistance;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CountryBean;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.Documents;
import org.digijava.module.aim.helper.Funding;
import org.digijava.module.aim.helper.FundingOrganization;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.Indicator;
import org.digijava.module.aim.helper.Question;
import org.digijava.module.aim.helper.SurveyFunding;
import org.digijava.module.aim.helper.fiscalcalendar.BaseCalendar;
import org.digijava.module.aim.util.caching.AmpCaching;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.visualization.helper.EntityRelatedListHelper;
import org.digijava.module.widget.dbentity.AmpDaValueFiltered;
import org.digijava.module.widget.table.filteredColumn.FilterItemProvider;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.JDBCException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DbUtil {
	private static Logger logger = Logger.getLogger(DbUtil.class);

	public static String filter(String text) {
		return filter(text, false);

	}

	public static String filter(String text, boolean acute) {

		String result = null;

		if (text != null) {
			result = text.replaceAll("&", "&amp;");
			result = result.replaceAll(">", "&gt;");
			result = result.replaceAll("<", "&lt;");
			if (acute) {
				result = result.replaceAll("'", "&acute;");// "&acute;");
			} else {
				result = result.replaceAll("'", "\'");// "&acute;");
			}
			result = result.replaceAll("\"", "&quot;");

		}
		return result;

	}

	public static String deFilter(String text, boolean acute) {
		String result = null;

		if (text != null) {
			result = text.replaceAll("&amp;", "&");
			result = result.replaceAll("&gt;", ">");
			result = result.replaceAll("&lt;", "<");
			if (acute) {
				result = result.replaceAll("&acute;", "'");// "&acute;");
			} else {
				result = result.replaceAll("\'", "'");// "&acute;");
			}
			result = result.replaceAll("&quot;", "\"");

		}
		return result;

	}

	public static String getDescParsed(String str) {
		StringBuffer strbuff = new StringBuffer();
		char[] ch = new char[str.length()];

		ch = str.toCharArray();

		for (int i = 0; i < ch.length; i++) {
			if (ch[i] == '<') {
				while (ch[i] != '>')
					i++;
			} else if (ch[i] == '&') {
				if (ch[i + 1] == 'n' && ch[i + 2] == 'b' && ch[i + 3] == 's'
						&& ch[i + 4] == 'p' && ch[i + 5] == ';')
					i = i + 5;
				else
					strbuff.append(ch[i]);
			} else {
				if (i < ch.length)
					strbuff.append(ch[i]);
			}
		}
		str = new String(strbuff);
		return str;
	}

	/**
	 * Removes the team-reports and member-reports association table.
	 * 
	 * @param reportId
	 *            A Long array of the reports to be updated
	 * @param teamId
	 *            The teamId of the team whose association with the specified
	 *            reports must be removed. When the teams are dissociated with
	 *            the reports, the association from the members of that team
	 *            also gets removed.
	 */
	public static void removeTeamReports(Long reportId[], Long teamId) {
		Session session = null;
		Transaction tx = null;

		if (reportId == null || reportId.length <= 0)
			return;

		try {
			session = PersistenceManager.getRequestDBSession();
			// beginTransaction();
			//
			String queryString = "select tm from "
					+ AmpTeamMember.class.getName()
					+ " tm where (tm.ampTeam=:teamId)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("teamId", teamId, Hibernate.LONG);
			//
			Collection col = qry.list();
			if (col != null && col.size() > 0) {
				for (int i = 0; i < reportId.length; i++) {
					if (reportId[i] != null) {
						queryString = "select r from "
								+ AmpReports.class.getName()
								+ " r where (r.ampReportId=:repId)";
						qry = session.createQuery(queryString);
						qry.setParameter("repId", reportId[i], Hibernate.LONG);
						Iterator itr = qry.list().iterator();
						if (itr.hasNext()) {
							AmpReports ampReport = (AmpReports) itr.next();
							if (ampReport.getMembers() != null) {
								/*
								 * removing the team members association with
								 * the report
								 */
								ampReport.getMembers().removeAll(col);
								if (ampReport.getDesktopTabSelections() != null) {
									for (AmpDesktopTabSelection adts : ampReport
											.getDesktopTabSelections()) {
										if (adts.getOwner().getAmpTeam()
												.getAmpTeamId().equals(teamId)) {
											adts.getOwner()
													.getDesktopTabSelections()
													.remove(adts);
											adts.setOwner(null);
											adts.setReport(null);
											ampReport.getDesktopTabSelections()
													.remove(adts);
										}
									}
								}
								session.update(ampReport);
							}
						}

						/*
						 * removing the teams association with the report
						 */
						queryString = "select tr from "
								+ AmpTeamReports.class.getName()
								+ " tr where (tr.team=:teamId) and "
								+ " (tr.report=:repId)";
						qry = session.createQuery(queryString);
						qry.setParameter("teamId", teamId, Hibernate.LONG);
						qry.setParameter("repId", reportId[i], Hibernate.LONG);
						itr = qry.list().iterator();
						if (itr.hasNext()) {
							AmpTeamReports ampTeamRep = (AmpTeamReports) itr
									.next();
							ampTeamRep.setReport(null);
							ampTeamRep.setTeam(null);
							session.save(ampTeamRep);
							session.delete(ampTeamRep);
						}
					}
				}
			}
			// tx.commit();
		} catch (Exception e) {
			logger.error("Exception from updateMemberReports");
			logger.error(e.getMessage());
			e.printStackTrace();
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception tex) {
					logger.error("Transaction rollback failed");
					logger.error(tex);
				}
			}
		}
	}

	/**
	 * Associated the reports with the given team
	 * 
	 * @param reportId
	 *            The Long array of reportIds which are to be associated with
	 *            the given team
	 * @param teamId
	 *            The team id of the team to which the reports are to be
	 *            assigned
	 * @param teamMemberId the teamMemer           
	 */
	public static void addTeamReports(Long reportId[], Long teamId,Long ampMemberId) {
		Session session = null;
		Transaction tx = null;

		try {
			session = PersistenceManager.getSession();
			// beginTransaction();

			String queryString = "select tm from " + AmpTeam.class.getName()
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
					for (int i = 0; i < reportId.length; i++) {
						temp.append(reportId[i]);
						if ((i + 1) != reportId.length) {
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
							tmpQry.setParameter("tId", team.getAmpTeamId(),
									Hibernate.LONG);
							tmpQry.setParameter("rId", report.getAmpReportId(),
									Hibernate.LONG);
							Iterator tmpItr = tmpQry.list().iterator();
							if (!tmpItr.hasNext()) {
								AmpTeamReports tr = new AmpTeamReports();
								tr.setTeam(team);
								tr.setReport(report);
								tr.setTeamView(false);
								session.save(tr);
							}
						}
						
//						//here we should 

						AmpTeamMember ampTeamMember =null;
						//if(report.getOwnerId()!=null){
						//	ampTeamMember=(AmpTeamMember) session.get(AmpTeamMember.class, report.getOwnerId().getAmpTeamMemId());	
						//}else {
							ampTeamMember = (AmpTeamMember) session.get(AmpTeamMember.class, ampMemberId);	
						//}					
						Set reportSet = ampTeamMember.getReports();
						//reportSet.add(ampReports);  // Not needed because it is set from ampReports object
						report.getMembers().add(ampTeamMember);
						session.saveOrUpdate(ampTeamMember);						
						
					}
				}
			}
			

			
			// tx.commit();
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

	public static AmpReports getAmpReports(Long id) {
		Session session = null;
		AmpReports report = null;
		try {
			session = PersistenceManager.getSession();
			report = (AmpReports) session.get(AmpReports.class, id);
		} catch (Exception ex) {
			logger.error("Unable to get AmpReports by Id :" + ex);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed :" + ex2);
			}
		}
		return report;
	}

//	public static boolean isPublicReport(Long id) {
//		Session session = null;
//		boolean isPublic = false;
//		try {
//			session = PersistenceManager.getRequestDBSession();
//			StringBuilder query = new StringBuilder();
//			query.append("select r.publicReport from ");
//			query.append(AmpReports.class.getName());
//			query.append(" r where r.ampReportId=:id");
//			Query qry = session.createQuery(query.toString());
//			qry.setLong("id", id);
//			Boolean result = (Boolean) qry.uniqueResult();
//			if (result != null) {
//				isPublic = result;
//			}
//
//		} catch (Exception ex) {
//			logger.error("Unable to get AmpReports by Id :" + ex);
//		}
//		return isPublic;
//	}

	public static Collection getFundingDetails(Long fundId) {
		Session session = null;
		Collection fundingDetails = new ArrayList();

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select f from "
					+ AmpFundingDetail.class.getName()
					+ " f "
					+ "where (f.ampFundingId=:fundId) order by f.transactionDate";
			Query qry = session.createQuery(queryString);
			qry.setParameter("fundId", fundId, Hibernate.LONG);
			fundingDetails = qry.list();

		} catch (Exception ex) {
			logger.error("Unable to get fundingDetails :" + ex);
		}
		return fundingDetails;
	}

	public static Collection getDisbursementsFundingOfIPAContract(IPAContract c) {
		Session session = null;
		Collection<AmpFundingDetail> fundingDetails = new ArrayList<AmpFundingDetail>();

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select f from "
					+ AmpFundingDetail.class.getName() + " f "
					+ "where (f.contract=:cId) and f.transactionType=1";
			Query qry = session.createQuery(queryString);
			qry.setParameter("cId", c.getId(), Hibernate.LONG);
			fundingDetails = qry.list();

		} catch (Exception ex) {
			logger.error("Unable to get fundingDetails (disbursements) of an IPA contract:"
					+ ex);
		}
		return fundingDetails;
	}

	public static Collection getFundingByActivity(Long actId) {
		Session session = null;
		Collection funding = new ArrayList();

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select f from " + AmpFunding.class.getName()
					+ " f " + "where (f.ampActivityId=:actId)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("actId", actId, Hibernate.LONG);
			funding = qry.list();

		} catch (Exception ex) {
			logger.error("Unable to get funding :" + ex);
		}
		return funding;
	}
	

//	public static int countActivitiesByQuery(String sQuery,
//			ArrayList<FilterParam> params) {
//
//		Session sess = null;
//		Connection conn = null;
//		CellColumn cc = null;
//		try {
//			sess = PersistenceManager.getSession();
//
//			conn = sess.connection();
//		} catch (HibernateException e) {
//			logger.error(e);
//			e.printStackTrace();
//		}
//
//		int ii = 0;
//
//		String query = "SELECT count(*) FROM amp_activity WHERE amp_activity_id IN ("
//				+ sQuery + " ) ";
//		// System.out.println("MASTER query count activities::: " + query);
//		PreparedStatement ps;
//
//		try {
//			ps = conn.prepareStatement(query);
//			if (params != null) {
//
//				// add params if exist
//				for (int i = 0; i < params.size(); i++) {
//					ps.setObject(i + 1, params.get(i).getValue(), params.get(i)
//							.getSqlType());
//				}
//
//			}
//			ResultSet rs = ps.executeQuery();
//			ResultSetMetaData rsmd;
//			rsmd = rs.getMetaData();
//			rs.next();
//			ii = rs.getInt(1);
//			rs.close();
//
//		} catch (SQLException e) {
//			logger.error(e);
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//			try {
//				if (sess != null) {
//					PersistenceManager.releaseSession(sess);
//				}
//			} catch (Exception e) {
//				logger.error("Error parsing date filters");
//			}
//		}
//		// System.out.println("--------------------- "+ii);
//		return ii;
//	}

	public static String getTrnMessage(String keyTrn) {
		Session session = null;
		Collection funding = new ArrayList();
		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select f.message from "
					+ Message.class.getName() + " f " + "where (f.key=:keyTrn)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("keyTrn", keyTrn, Hibernate.STRING);
			funding = qry.list();

		} catch (Exception ex) {
			logger.error("Unable to get funding :" + ex);
		}
		String s = null;
		if (funding != null)
			if (!funding.isEmpty())
				s = ((String) (funding.iterator().next()));
		// ////System.out.println("aaaaaaaaaaaaa"+s);
		return s;
	}

	public static AmpActivityInternalId getActivityInternalId(Long actId,
			Long orgId) {
		Session session = null;
		AmpActivityInternalId internalId = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select a from "
					+ AmpActivityInternalId.class.getName() + " a "
					+ "where (a.ampActivityId=:actId) and (a.ampOrgId=:orgId)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("actId", actId, Hibernate.LONG);
			qry.setParameter("orgId", orgId, Hibernate.LONG);
			Iterator itr = qry.list().iterator();

			if (itr.hasNext()) {
				internalId = (AmpActivityInternalId) itr.next();
			}
		} catch (Exception ex) {
			logger.error("Unable to get Activity Internal Id :" + ex);
		}
		return internalId;
	}

	public static Collection getActivityInternalId(Long actId) {
		Session session = null;
		Collection col = new ArrayList();

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select aaii.* from amp_activity_internal_id aaii "
					+ "where aaii.amp_activity_id=:actId";
			Query qry = session.createSQLQuery(queryString).addEntity(
					AmpActivityInternalId.class);
			qry.setParameter("actId", actId, Hibernate.LONG);
			col = qry.list();
		} catch (Exception ex) {
			logger.error("Unable to get Activity Internal Id :" + ex);
		}
		return col;
	}

	public static Collection getOrganizations(Long actId, String orgCode) {
		Session session = null;
		Collection orgs = new ArrayList();

		try {
			session = PersistenceManager.getRequestDBSession();
			AmpActivity activity = (AmpActivity) session.load(
					AmpActivity.class, actId);
			Set set = activity.getOrgrole();
			Iterator itr1 = set.iterator();
			while (itr1.hasNext()) {
				AmpOrgRole orgRole = (AmpOrgRole) itr1.next();
				if (orgRole.getRole().getRoleCode().equals(orgCode)) {
					if (!orgs.contains(orgRole.getOrganisation())) {
						orgs.add(orgRole.getOrganisation());
					}
				}
			}
		} catch (Exception ex) {
			logger.error("Unable to get Organizations :" + ex);
		}
		return orgs;
	}

	public static Collection getActivityDocuments(Long id) {
		Session session = null;
		Collection docs = new ArrayList();

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select a from " + AmpActivity.class.getName()
					+ " a " + "where (a.ampActivityId=:id)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				AmpActivity activity = (AmpActivity) itr.next();
				Set set = activity.getDocuments();
				Iterator itr1 = set.iterator();
				while (itr1.hasNext()) {
					CMSContentItem cmsItem = (CMSContentItem) itr1.next();
					docs.add(cmsItem);
				}
			}
		} catch (Exception ex) {
			logger.error("Unable to get ActivityDocuments :" + ex);
		}
		return docs;
	}

	public static List<org.digijava.module.aim.helper.Documents> getKnowledgeDocuments(Long id) {
		Session session = null;
		List<org.digijava.module.aim.helper.Documents> docs = new ArrayList<org.digijava.module.aim.helper.Documents>();

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select a from " + AmpActivity.class.getName()
					+ " a " + "where (a.ampActivityId=:id)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				AmpActivity activity = (AmpActivity) itr.next();
				Set set = activity.getDocuments();
				Iterator itr1 = set.iterator();
				while (itr1.hasNext()) {
					CMSContentItem cmsItem = (CMSContentItem) itr1.next();
					Documents document = new Documents();
					document.setActivityId(activity.getAmpActivityId());
					document.setActivityName(activity.getName());
					document.setDocId(new Long(cmsItem.getId()));
					document.setTitle(cmsItem.getTitle());
					document.setIsFile(cmsItem.getIsFile());
					document.setFileName(cmsItem.getFileName());
					document.setUrl(cmsItem.getUrl());
					document.setDocDescription(cmsItem.getDescription());
					document.setDate(cmsItem.getDate());
					if (cmsItem.getDocType() != null)
						document.setDocType(cmsItem.getDocType().getValue());

					if (cmsItem.getDocLanguage() != null)
						document.setDocLanguage(cmsItem.getDocLanguage()
								.getValue());
					document.setDocComment(cmsItem.getDocComment());

					logger.debug("Doc Desc :" + document.getDocDescription());
					docs.add(document);
				}
			}
		} catch (Exception ex) {
			logger.error("Unable to get ActivityDocuments :" + ex);
		}
		return docs;
	}

	public static Collection getAllDocuments(Long teamId) {
		Session session = null;
		Collection col = new ArrayList();

		try {
			session = PersistenceManager.getRequestDBSession();
			String qryStr = "select act from " + AmpActivity.class.getName()
					+ " act";
			qryStr += " where (act.team=:team)";
			Query qry = session.createQuery(qryStr);
			qry.setParameter("team", teamId, Hibernate.LONG);
			Iterator itr1 = qry.list().iterator();
			while (itr1.hasNext()) {
				AmpActivity act = (AmpActivity) itr1.next();
				Set docs = act.getDocuments();
				if (docs != null) {
					Iterator itr2 = docs.iterator();
					while (itr2.hasNext()) {
						CMSContentItem cmsItem = (CMSContentItem) itr2.next();
						Documents document = new Documents();
						document.setActivityId(act.getAmpActivityId());
						document.setActivityName(act.getName());
						document.setDocId(new Long(cmsItem.getId()));
						document.setTitle(cmsItem.getTitle());
						document.setIsFile(cmsItem.getIsFile());
						document.setFileName(cmsItem.getFileName());
						document.setUrl(cmsItem.getUrl());
						document.setDocDescription(cmsItem.getDescription());
						document.setDate(cmsItem.getDate());
						col.add(document);
					}
				}
			}
		} catch (Exception e) {
			logger.error("Cannot get All documents :" + e);
		}

		return col;
	}

	public static AmpRole getAmpRole(String roleCode) {
		Session session = null;
		AmpRole role = null;

		try {
			session = PersistenceManager.getRequestDBSession();

			String queryString = "select r from " + AmpRole.class.getName()
					+ " r " + "where (r.roleCode=:code)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("code", roleCode, Hibernate.STRING);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext())
				role = (AmpRole) itr.next();

		} catch (Exception e) {
			logger.error("Uanble to get role :" + e);
		}
		return role;
	}

	@Deprecated
	// should be removed !!
	public static Object get(Class c, Long id) {
		Session session = null;
		Object o = null;

		try {
			session = PersistenceManager.getSession();
			o = session.load(c, id);

		} catch (Exception e) {
			logger.error("Uanble to get object of class " + c.getName()
					+ " width id=" + id + ". Error was:" + e);
		} finally {

			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex) {
				logger.error("releaseSession() failed " + ex);
			}
		}
		return o;
	}

	public static Object getObject(Class<?> c, Serializable id) {
		Session session = null;
		Object o = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			o = session.load(c, id);
		} catch (Exception e) {
			logger.error("Uanble to get object of class " + c.getName()
					+ " width id=" + id + ". Error was:" + e);
		}
		return o;
	}

	public static EUActivity getEuActivity(Long id) {
		Session session = null;
		EUActivity o = null;

		try {
			session = PersistenceManager.getSession();
			o = (EUActivity) session.load(EUActivity.class, id);
			o.getName();
			Set<EUActivityContribution> s = o.getContributions();
			for (EUActivityContribution i : s) {
				i.getDonor().getName();
			}

		} catch (Exception e) {
			logger.error("Uanble to get object of class "
					+ EUActivity.class.getName() + " width id=" + id
					+ ". Error was:" + e);
		} finally {

			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex) {
				logger.error("releaseSession() failed " + ex);
			}
		}
		return o;
	}

	public static ArrayList getOrgRole(Long id) {
		ArrayList list = new ArrayList();
		StringBuffer RAOrg = new StringBuffer();
		StringBuffer DNOrg = new StringBuffer();
		StringBuffer IAOrg = new StringBuffer();
		StringBuffer RLOrg = new StringBuffer();
		Iterator iter = null;

		Session session = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			// modified by Priyajith
			// desc:used select query instead of session.load
			// start
			String queryString = "select a from " + AmpActivity.class.getName()
					+ " a " + "where (a.ampActivityId=:id)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			AmpActivity ampActivity = null;
			while (itr.hasNext()) {
				ampActivity = (AmpActivity) itr.next();
			}
			// end
			iter = ampActivity.getOrgrole().iterator();
			while (iter.hasNext()) {
				AmpOrgRole ampOrgRole = (AmpOrgRole) iter.next();
				if (ampOrgRole.getRole().getRoleCode()
						.equals(Constants.REPORTING_AGENCY)) {
					if (RAOrg.length() == 0)
						RAOrg.append(ampOrgRole.getOrganisation().getName());
					else
						RAOrg.append(",").append(
								ampOrgRole.getOrganisation().getName());
				}
				if (ampOrgRole.getRole().getRoleCode()
						.equals(Constants.FUNDING_AGENCY)) {
					if (DNOrg.length() == 0)
						DNOrg.append(ampOrgRole.getOrganisation().getName());
					else
						DNOrg.append(",").append(
								ampOrgRole.getOrganisation().getName());
				}
				if (ampOrgRole.getRole().getRoleCode()
						.equals(Constants.IMPLEMENTING_AGENCY)) {
					if (IAOrg.length() == 0)
						IAOrg.append(ampOrgRole.getOrganisation().getName());
					else
						IAOrg.append(",").append(
								ampOrgRole.getOrganisation().getName());
				}
				if (ampOrgRole.getRole().getRoleCode()
						.equals(Constants.RELATED_INSTITUTIONS)) {
					if (RLOrg.length() == 0)
						RLOrg.append(ampOrgRole.getOrganisation().getName());
					else
						RLOrg.append(",").append(
								ampOrgRole.getOrganisation().getName());
				}
				logger.debug("Organisation :"
						+ ampOrgRole.getOrganisation().getName());
				logger.debug("Role Code : "
						+ ampOrgRole.getRole().getRoleCode());
			}
			list.add(RAOrg.toString());
			list.add(DNOrg.toString());
			list.add(IAOrg.toString());
			list.add(RLOrg.toString());

			logger.debug("Funding Country/Agency" + RAOrg.toString());
			logger.debug("Reporting Country/Agency" + DNOrg.toString());
			logger.debug("Implementing Agency" + IAOrg.toString());
			logger.debug("Related Institution" + RLOrg.toString());

		} catch (Exception ex) {
			logger.error("Unable to get Amp Org Role " + ex.getMessage());
		}
		return list;
	}

	/**
	 * returns null on non-existing organisation
	 * @param id
	 * @return
	 */
	public static AmpOrganisation getOrganisation(Long id) {
		Session session = null;
		AmpOrganisation organization = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			organization = (AmpOrganisation) session.load(AmpOrganisation.class, id);
			Hibernate.initialize(organization.getRecipients());
			Hibernate.initialize(organization.getOrganizationBudgetInfos());
			if (organization.getOrganizationBudgetInfos() != null) {
				for (AmpOrganizationBudgetInformation budgetInfo : organization
						.getOrganizationBudgetInfos()) {
					Hibernate.initialize(budgetInfo.getOrganizations());
				}
			}
		} catch (Exception ex) {
			//logger.error("Unable to get organisation from database", ex);
		}
		//logger.debug("Getting organisation successfully ");
		return organization;
	}

	public static ArrayList getAmpComponent(Long ampActivityId) {
		ArrayList component = new ArrayList();
		Query q = null;
		Session session = null;
		String queryString = null;
		AmpComponent ampComponent = null;
		Iterator iter = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			queryString = " select c from " + AmpComponent.class.getName()
					+ " c where (c.activity.ampActivityId=:ampActivityId )";
			q = session.createQuery(queryString);
			q.setParameter("ampActivityId", ampActivityId, Hibernate.LONG);
			iter = q.list().iterator();

			while (iter.hasNext()) {
				ampComponent = (AmpComponent) iter.next();
				component.add(ampComponent);
			}
		} catch (Exception ex) {
			logger.error("Unable to get Amp PhysicalPerformance", ex);
			// ////System.out.println(ex.toString()) ;
		}
		logger.debug("Getting components executed successfully "
				+ component.size());
		return component;
	}

	public static ArrayList getAmpComponents() {
		ArrayList component = new ArrayList();
		Query q = null;
		Session session = null;
		String queryString = null;
		AmpComponent ampComponent = null;
		Iterator iter = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			queryString = "FROM " + AmpComponent.class.getName();

			q = session.createQuery(queryString);
			iter = q.list().iterator();

			while (iter.hasNext()) {
				ampComponent = (AmpComponent) iter.next();
				component.add(ampComponent);
			}
		} catch (Exception ex) {
			logger.error("Unable to get Amp Components", ex);
		}
		logger.debug("Getting components executed successfully "
				+ component.size());
		return component;
	}

	public static ArrayList getAmpPhysicalProgress(Long ampActivityId,
			Long componentId) {
		ArrayList progress = new ArrayList();
		Query q = null;
		Session session = null;
		String queryString = null;
		AmpPhysicalPerformance ampPhysicalPerformance = null;
		Iterator iter = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			queryString = " select Progress from "
					+ AmpPhysicalPerformance.class.getName()
					+ " Progress where (Progress.ampActivityId=:ampActivityId )";
			if (componentId != null) {
				queryString += " and (Progress.component=:componentId) ";
			}
			q = session.createQuery(queryString);
			q.setParameter("ampActivityId", ampActivityId, Hibernate.LONG);
			if (componentId != null) {
				q.setLong("componentId", componentId);
			}
			iter = q.list().iterator();

			while (iter.hasNext()) {

				ampPhysicalPerformance = (AmpPhysicalPerformance) iter.next();
				logger.debug("Title :"
						+ (String) ampPhysicalPerformance.getTitle());
				logger.debug("DESCRIPTION :"
						+ (String) ampPhysicalPerformance.getDescription());
				progress.add(ampPhysicalPerformance);
			}
		} catch (Exception ex) {
			logger.error("Unable to get Amp PhysicalPerformance", ex);
			// ////System.out.println(ex.toString()) ;
		}
		logger.debug("Getting funding Executed successfully " + progress.size());
		return progress;
	}

	public static ArrayList getAmpPhysicalProgress(Long ampActivityId,
			Long componentId, Session session) {
		ArrayList progress = new ArrayList();
		Query q = null;
		String queryString = null;
		AmpPhysicalPerformance ampPhysicalPerformance = null;
		Iterator iter = null;
		try {
			queryString = " select Progress from "
					+ AmpPhysicalPerformance.class.getName()
					+ " Progress where (Progress.ampActivityId=:ampActivityId )";
			if (componentId != null) {
				queryString += " and (Progress.component=:componentId) ";
			}
			q = session.createQuery(queryString);
			q.setParameter("ampActivityId", ampActivityId, Hibernate.LONG);
			if (componentId != null) {
				q.setLong("componentId", componentId);
			}
			iter = q.list().iterator();

			while (iter.hasNext()) {

				ampPhysicalPerformance = (AmpPhysicalPerformance) iter.next();
				logger.debug("Title :"
						+ (String) ampPhysicalPerformance.getTitle());
				logger.debug("DESCRIPTION :"
						+ (String) ampPhysicalPerformance.getDescription());
				progress.add(ampPhysicalPerformance);
			}
		} catch (Exception ex) {
			logger.error("Unable to get Amp PhysicalPerformance", ex);
			// ////System.out.println(ex.toString()) ;
		}
		logger.debug("Getting funding Executed successfully " + progress.size());
		return progress;
	}

	public static Collection getAmpFunding(Long ampActivityId, Long ampFundingId) {
		Session session = null;
		Query q = null;
		Collection ampFundings = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = new String();
			queryString = "select f from "
					+ AmpFunding.class.getName()
					+ " f where (f.ampActivityId=:ampActivityId ) and (f.ampFundingId=:ampFundingId)";
			q = session.createQuery(queryString);
			q.setParameter("ampActivityId", ampActivityId, Hibernate.LONG);
			q.setParameter("ampFundingId", ampFundingId, Hibernate.LONG);
			ampFundings = q.list();
			logger.debug("DbUtil : getAmpFunding() returning collection of size  "
					+ ampFundings.size());

		} catch (Exception ex) {
			logger.error("Unable to get AmpFunding collection from database",
					ex);
		}

		return ampFundings;
	}

	public static List<AmpFunding> getAmpFunding(Long ampActivityId) {
		logger.debug("getAmpFunding() with ampActivityId=" + ampActivityId);
		Session session = null;
		Query q = null;
		List<AmpFunding> ampFundings = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = new String();
			queryString = "select f from " + AmpFunding.class.getName()
					+ " f where (f.ampActivityId=:ampActivityId) ";
			q = session.createQuery(queryString);
			q.setParameter("ampActivityId", ampActivityId, Hibernate.LONG);
			ampFundings = q.list();
		} catch (Exception ex) {
			logger.error("Unable to get AmpFunding collection from database",
					ex);
		}
		logger.debug("DbUtil : getAmpFunding(ampActivityId) returning collection of size  "
				+ (ampFundings != null ? ampFundings.size() : 0));
		return ampFundings;
	}

	public static ProposedProjCostHelper getActivityProposedProjCost(
			Long ampActivityId) {
		Session session = null;
		Query q = null;
		ProposedProjCostHelper projectCost = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			StringBuilder queryString = new StringBuilder(
					"select new  org.digijava.module.aim.util.ProposedProjCostHelper(act.currencyCode,act.funAmount,act.funDate) from ");
			queryString.append(AmpActivity.class.getName());
			queryString.append(" act where act.ampActivityId=:ampActivityId ");
			queryString.append(" and  act.funAmount is not null ");
			queryString.append(" and  act.funDate is not null ");
			queryString.append(" and  act.currencyCode is not null ");
			q = session.createQuery(queryString.toString());
			q.setLong("ampActivityId", ampActivityId);
			projectCost = (ProposedProjCostHelper) q.uniqueResult();
		} catch (Exception ex) {
			logger.error("Unable to get AmpFunding collection from database",
					ex);
		}
		return projectCost;
	}

	/**
	 * Return total amount using the exchange for each funding according with
	 * funding date
	 * 
	 * @param ampFundingId
	 * @param transactionType
	 * @param adjustmentType
	 * @param currcode
	 * @return
	 */
	public static DecimalWraper getTotalDonorFunding(Long ampFundingId,
			Integer transactionType, Integer adjustmentType, String currcode) {
		Session session = null;
		Query q = null;
		List list = null;
		Iterator iter = null;
		double fromrate;
		double torate;
		DecimalWraper total = new DecimalWraper();
		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = new String();
			queryString = "select f from " + AmpFundingDetail.class.getName()
					+ " f where f.ampFundingId=:ampFundingId "
					+ " and  f.transactionType=:transactionType  "
					+ " and  f.adjustmentType=:adjustmentType ";
			q = session.createQuery(queryString);
			q.setParameter("ampFundingId", ampFundingId, Hibernate.LONG);
			q.setParameter("transactionType", transactionType,
					Hibernate.INTEGER);
			q.setParameter("adjustmentType", adjustmentType, Hibernate.INTEGER);
			list = q.list();
			if (list.size() != 0) {
				iter = list.iterator();
				while (iter.hasNext()) {
					AmpFundingDetail fundingdetails = (AmpFundingDetail) iter
							.next();
					fromrate = Util.getExchange(fundingdetails
							.getAmpCurrencyId().getCurrencyCode(),
							new java.sql.Date(fundingdetails
									.getTransactionDate().getTime()));
					torate = Util.getExchange(currcode, new java.sql.Date(
							fundingdetails.getTransactionDate().getTime()));
					if (total.getValue() == null) {
						total = CurrencyWorker.convertWrapper(fundingdetails
								.getTransactionAmount(), fromrate, torate,
								new java.sql.Date(fundingdetails
										.getTransactionDate().getTime()));
					} else {
						DecimalWraper tmp = CurrencyWorker.convertWrapper(
								fundingdetails.getTransactionAmount(),
								fromrate, torate, new java.sql.Date(
										fundingdetails.getTransactionDate()
												.getTime()));
						total.setValue(tmp.getValue().add(total.getValue()));
						total.setCalculations(tmp.getCalculations() + " + "
								+ total.getCalculations());
					}
				}
			}
		} catch (Exception ex) {
			logger.error("Unable to get sum of funds from database", ex);
		}
		return total;
	}

	public static double getTotalDonorFund(Long ampFundingId,
			Integer transactionType, Integer adjustmentType) {

		logger.debug("getTotalDonorFund() with ampFundingId " + ampFundingId
				+ " transactionType " + transactionType + " adjustmentType "
				+ adjustmentType);

		Session session = null;
		Query q = null;
		List list = null;
		Iterator iter = null;
		Double total = new Double(0.0);
		;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = new String();
			queryString = "select (sum(f.thousandsTransactionAmount) from "
					+ AmpFundingDetail.class.getName()
					+ " f where (f.ampFundingId=:ampFundingId) "
					+ " and (f.transactionType=:transactionType) "
					+ " and (f.adjustmentType=:adjustmentType) group by f.ampFundingId";
			q = session.createQuery(queryString);
			q.setParameter("ampFundingId", ampFundingId, Hibernate.LONG);
			q.setParameter("transactionType", transactionType,
					Hibernate.INTEGER);
			q.setParameter("adjustmentType", adjustmentType, Hibernate.INTEGER);
			list = q.list();
			if (list.size() != 0) {
				iter = list.iterator();
				while (iter.hasNext()) {
					total = (Double) iter.next();
				}
			}
		} catch (Exception ex) {
			logger.error("Unable to get sum of funds from database", ex);
		}

		logger.debug("getTotalDonorFund() total : " + total);
		return total.doubleValue();
	}

	public static Collection getYearlySum(Long ampFundingId,
			Integer transactionType, Integer adjustmentType) {
		logger.debug("getYearlySum() with ampFundingId="
				+ ampFundingId.longValue() + " transactionType="
				+ transactionType.longValue());
		Session session = null;
		Query q = null;
		Collection c = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = new String();
			queryString = "select sum(f.thousandsTransactionAmount) from "
					+ AmpFundingDetail.class.getName()
					+ " f where (f.ampFundingId=:ampFundingId) "
					+ " and (f.transactionType=:transactionType) "
					+ " and (f.adjustmentType=:adjustmentType) group by f.fiscalYear";
			q = session.createQuery(queryString);
			q.setParameter("ampFundingId", ampFundingId, Hibernate.LONG);
			q.setParameter("transactionType", transactionType,
					Hibernate.INTEGER);
			q.setParameter("adjustmentType", adjustmentType, Hibernate.INTEGER);

			c = q.list();
		} catch (Exception ex) {
			logger.error(
					"Unable to get planned commitment by fiscal year from database",
					ex);
		}

		logger.debug("getYearlySum() collection size returned : "
				+ (c != null ? c.size() : 0));
		return c;
	}

	public static Collection getFiscalYears(Long ampFundingId,
			Integer transactionType) {
		logger.debug("getFiscalYears() with ampFundingId="
				+ ampFundingId.longValue() + " transactionType="
				+ transactionType.intValue());

		Session session = null;
		Query q = null;
		Collection c = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = new String();
			queryString = "select f.fiscalYear from "
					+ AmpFundingDetail.class.getName()
					+ " f where (f.ampFundingId=:ampFundingId) "
					+ " and (f.transactionType=:transactionType) "
					+ " group by f.fiscalYear";
			q = session.createQuery(queryString);
			q.setParameter("ampFundingId", ampFundingId, Hibernate.LONG);
			q.setParameter("transactionType", transactionType,
					Hibernate.INTEGER);

			c = q.list();
		} catch (Exception ex) {
			logger.error("Unable to get  fiscal years from database", ex);
		}

		logger.debug("getFiscalYears() collection size returned : "
				+ (c != null ? c.size() : 0));
		return c;
	}

	public static AmpComponent getAmpComponentDescription(Long cid) {
		Query q = null;
		Session session = null;
		String queryString = null;
		AmpComponent comp = null;
		Iterator iter = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			queryString = " select c from " + AmpComponent.class.getName()
					+ " c where (c.ampComponentId=:cid )";
			q = session.createQuery(queryString);
			q.setParameter("cid", cid, Hibernate.LONG);
			iter = q.list().iterator();

			if (iter.hasNext()) {

				comp = (AmpComponent) iter.next();
				logger.debug("Title :" + (String) comp.getTitle());
				logger.debug("DESCRIPTION :" + (String) comp.getDescription());
			}
		} catch (Exception ex) {
			logger.error("Unable to get Amp Component", ex);
			// ////System.out.println(ex.toString()) ;
		}
		logger.debug("Getting Amp Component Executed successfully ");
		return comp;
	}

	public static AmpPhysicalPerformance getAmpPhysicalProgressDescription(
			Long ampPpId) {
		Query q = null;
		Session session = null;
		String queryString = null;
		AmpPhysicalPerformance ampPhysicalPerformance = null;
		Iterator iter = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			queryString = " select Progress from "
					+ AmpPhysicalPerformance.class.getName()
					+ " Progress where (Progress.ampPpId=:ampPpId )";
			q = session.createQuery(queryString);
			q.setParameter("ampPpId", ampPpId, Hibernate.LONG);
			iter = q.list().iterator();

			if (iter.hasNext()) {

				ampPhysicalPerformance = (AmpPhysicalPerformance) iter.next();
				logger.debug("Title :"
						+ (String) ampPhysicalPerformance.getTitle());
				logger.debug("DESCRIPTION :"
						+ (String) ampPhysicalPerformance.getDescription());
				// progress.add(ampPhysicalPerformance);
			}
		} catch (Exception ex) {
			logger.error("Unable to get Amp PhysicalPerformance", ex);
			// ////System.out.println(ex.toString()) ;
		}
		logger.debug("Getting funding Executed successfully ");
		return ampPhysicalPerformance;
	}

	public static Collection getCreatedOrEditedActivities(Long ampTeamId) {
		Collection actList = new ArrayList();
		Session session = null;
		Query q = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select act from "
					+ AmpActivity.class.getName()
					+ " act where (act.team=:ampTeamId)"
					+ " and (act.approvalStatus in ("
					+ Constants.ACTIVITY_NEEDS_APPROVAL_STATUS + ") ) "
					+ " and act.draft != :draftValue";
			q = session.createQuery(queryString);
			q.setParameter("ampTeamId", ampTeamId, Hibernate.LONG);
			q.setBoolean("draftValue", true);

			actList = q.list();

		} catch (Exception ex) {
			logger.error(
					"Unable to get AmpActivity [getCreatedOrEditedActivities()]",
					ex);
		}
		logger.debug("Getting CreatedOrEdited activities Executed successfully ");
		return actList;
	}

	public static String getActivityApprovalStatus(Long actId) {
		Session session = null;
		Query q = null;
		String ans = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			String qry = "select act from "
					+ AmpActivityVersion.class.getName()
					+ " act where act.ampActivityId=:actId";
			q = session.createQuery(qry);
			q.setParameter("actId", actId, Hibernate.LONG);
			Iterator itr = q.list().iterator();
			while (itr.hasNext()) {
				AmpActivityVersion act = (AmpActivityVersion) itr.next();
				ans = act.getApprovalStatus();
			}
		} catch (Exception ex) {
			logger.error(
					"Unable to get AmpActivity [getActivityApprovalStatus()]",
					ex);
		}
		logger.debug("getActivityApprovalStatus Executed successfully ");
		return ans;
	}

	public static Collection getApprovedOrCreatorActivities(Long ampTeamId,
			Long ampTeamMemId) {
		Collection actList = new ArrayList();
		Session session = null;
		Query q = null;
		String queryString;
		try {
			session = PersistenceManager.getRequestDBSession();
			/*
			 * if (new Long(0).equals(ampTeamId)) { // for management workspace
			 * queryString = "select act.ampActivityId from " +
			 * AmpActivity.class.getName() +
			 * " act where (act.approvalStatus=:status1 or act.approvalStatus=:status2)"
			 * ; q = session.createQuery(queryString); } else { // for regular
			 * working team
			 */
			queryString = "select act from "
					+ AmpActivity.class.getName()
					+ " act where (act.team=:ampTeamId)"
					+ " and ( act.activityCreator=:ampTeamMemId "
					+ " or act.approvalStatus=:status1 or act.approvalStatus=:status2)";
			q = session.createQuery(queryString);
			q.setParameter("ampTeamId", ampTeamId, Hibernate.LONG);
			q.setParameter("ampTeamMemId", ampTeamMemId, Hibernate.LONG);
			/* } */
			q.setParameter("status1", "approved", Hibernate.STRING);
			q.setParameter("status2", "edited", Hibernate.STRING);
			actList = q.list();

		} catch (Exception ex) {
			logger.error(
					"Unable to get AmpActivity [getApprovedOrCreatorActivities()]",
					ex);
		}
		logger.debug("Getting ApprovedOrCreator activities Executed successfully ");
		return actList;
	}

	/**
	 * get AmpFunding by ampFundingId
	 * 
	 * @param ampFundingId
	 * @return
	 */
	public static AmpFunding getAmpFundingById(Long ampFundingId) {
		logger.debug("getAmpFundingById() with ampFundingId="
				+ ampFundingId.longValue());
		Session session = null;
		Query q = null;
		AmpFunding ampFunding = null;
		Iterator iter = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = new String();
			queryString = "select f from " + AmpFunding.class.getName()
					+ " f where (f.ampFundingId=:ampFundingId) ";
			q = session.createQuery(queryString);
			q.setParameter("ampFundingId", ampFundingId, Hibernate.LONG);
			iter = q.list().iterator();
			if (iter.hasNext()) {
				ampFunding = (AmpFunding) iter.next();
			}
		} catch (Exception ex) {
			logger.error("Unable to get AmpFunding from database", ex);
		}
		logger.debug("DbUtil : getAmpFundingById(ampFundingId) returning ampFunding  "
				+ (ampFunding != null ? ampFunding.getFinancingId() : "null"));

		return ampFunding;
	}

	public static Collection getQuarterlyDataForProjections(Long ampFundingId,
			int adjustmentType) {
		logger.debug("getQuarterlyDataForProjections with ampFundingId "
				+ ampFundingId + " adjustmentType " + adjustmentType);

		Session session = null;
		Query q = null;
		Collection c = null;
		Integer adjType = new Integer(adjustmentType);

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = new String();
			queryString = "select p.amount,"
					+ "p.projectionDate, p.ampCurrency, p.projected from "
					+ AmpFundingMTEFProjection.class.getName()
					+ " p where (p.ampFunding=:ampFundingId) "
					+ " and (p.projected=:adjType) order by p.projectionDate ";
			q = session.createQuery(queryString);
			q.setParameter("ampFundingId", ampFundingId, Hibernate.LONG);
			q.setParameter("adjType", adjType, Hibernate.INTEGER);
			c = q.list();
		} catch (Exception ex) {
			logger.error("Unable to get quarterly data from database", ex);

		}

		logger.debug("getQuarterlyDataForProjections() returning a list of size : "
				+ c.size());
		return c;
	}

	/**
	 * @author jose Returns a collection of records from amp_funding_detail
	 *         based on below
	 * @param ampFundingId
	 * @param transactionType
	 *            commitment=0,disbursement=1,expenditure=2
	 * @param adjustmentType
	 *            planned=0,actual=1
	 * @return Collection
	 */
	public static Collection getQuarterlyData(Long ampFundingId,
			int transactionType, int adjustmentType) {
		logger.debug("getQuarterlyData with ampFundingId " + ampFundingId
				+ " transactionType " + transactionType + " adjustmentType "
				+ adjustmentType);

		Session session = null;
		Query q = null;
		Collection c = null;
		List<Object[]> ret = new ArrayList<Object[]>();
		Integer trsType = new Integer(transactionType);
		Integer adjType = new Integer(adjustmentType);

		if (transactionType == Constants.MTEFPROJECTION) {
			return getQuarterlyDataForProjections(ampFundingId, adjustmentType);
		}

		try {
			String baseCurr = FeaturesUtil
					.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY);
			if (baseCurr == null)
				baseCurr = "USD";

			session = PersistenceManager.getRequestDBSession();
			String queryString = new String();
			queryString = "select f from " + AmpFundingDetail.class.getName()
					+ " f where (f.ampFundingId=:fundId) "
					+ " and (f.transactionType=:trsType) "
					+ " and (f.adjustmentType=:adjType) "
					+ "order by f.transactionDate ";

			q = session.createQuery(queryString);
			q.setParameter("fundId", ampFundingId, Hibernate.LONG);
			q.setParameter("trsType", transactionType, Hibernate.INTEGER);
			q.setParameter("adjType", adjustmentType, Hibernate.INTEGER);

			c = q.list();

			if (c != null) {
				Iterator<AmpFundingDetail> iter = c.iterator();
				while (iter.hasNext()) {
					Object[] details = new Object[5];
					AmpFundingDetail afd = iter.next();
					details[0] = afd.getThousandsTransactionAmount();
					details[1] = afd.getTransactionDate();
					details[2] = afd.getAmpCurrencyId();
					details[3] = afd.getFixedExchangeRate();
					details[4] = afd.getFixedRateBaseCurrency();
					Double fixedRate = (Double) details[3];
					AmpCurrency fixedCurrency = (AmpCurrency) details[4];
					String fixedCurrCode = fixedCurrency != null ? fixedCurrency
							.getCurrencyCode() : "USD";
					if (fixedRate != null && !baseCurr.equals(fixedCurrCode)) {
						details[3] = null;
					}
					ret.add(details);
				}
			}

		} catch (Exception ex) {
			logger.error("Unable to get quarterly data from database", ex);		
		}	

		logger.debug("getQuarterlyData() returning a list of size : " + ret.size());
		return ret;
	}

	/*
	 * @author Priyajith C
	 */
	// Retrieves all organizations
	public static List<AmpOrganisation> getAllOrganisation() {
		Session session = null;
		Query qry = null;
		List<AmpOrganisation> organisation = new ArrayList<AmpOrganisation>();

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select o from "
					+ AmpOrganisation.class.getName()
					+ " o where (o.deleted is null or o.deleted = false) order by name asc";
			qry = session.createQuery(queryString);
			organisation = qry.list();
		} catch (Exception e) {
			logger.error("Unable to get all organisations");
			logger.debug("Exceptiion " + e);
		}
		return organisation;
	}
	
	/**
	 * SLOW - optimize it later in case you need it
	 * @return
	 */
	public static AmpOrganisation getRandomOrganisation()
	{
		Session session = null;
		Query qry = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select o from "
					+ AmpOrganisation.class.getName()
					+ " o where (o.deleted is null or o.deleted = false) order by ampOrgId asc";
			qry = session.createQuery(queryString);
			return (AmpOrganisation) (qry.list().get(0));
		} catch (Exception e) {
			logger.error("Unable to get all organisations");
			logger.debug("Exceptiion " + e);
		}
		throw new RuntimeException("no organizations found in the database - looks surrealistic");
	}

	public static List<AmpFiscalCalendar> getAllFisCalenders() {
		Session session = null;
		Query qry = null;
		List<AmpFiscalCalendar> fisCals = new ArrayList<AmpFiscalCalendar>();

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select f from "
					+ AmpFiscalCalendar.class.getName() + " f";
			qry = session.createQuery(queryString);
			fisCals = qry.list();
		} catch (Exception e) {
			logger.error("Unable to get all fiscal calendars");
			logger.debug("Exceptiion " + e);
		}
		return fisCals;
	}

	public static Long getBaseFiscalCalendar() {
		Session session = null;
		Query qry = null;
		Long fid = new Long(4);

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "from "
					+ AmpFiscalCalendar.class.getName()
					+ " where startMonthNum=:start and startDayNum=:start and yearOffset=:offset";
			qry = session.createQuery(queryString);
			qry.setParameter("start", new Integer(1), Hibernate.INTEGER);
			qry.setParameter("offset", new Integer(0), Hibernate.INTEGER);
			if (qry.list().size() != 0)
				fid = ((AmpFiscalCalendar) qry.list().get(0))
						.getAmpFiscalCalId();
		} catch (Exception ex) {
			logger.error("Unable to get base fiscal calendar" + ex);
			ex.printStackTrace(System.out);
		}
		return fid;
	}

	public static AmpFiscalCalendar getGregorianCalendar() {
		Session session = null;
		Query qry = null;
		AmpFiscalCalendar calendar = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select f from "
					+ AmpFiscalCalendar.class.getName() + " f "
					+ "where f.baseCal=:baseCalParam order by f.name";
			qry = session.createQuery(queryString);
			qry.setParameter("baseCalParam",
					BaseCalendar.BASE_GREGORIAN.getValue(), Hibernate.STRING);

			if (qry.list() != null)
				calendar = ((AmpFiscalCalendar) qry.list().get(0));
		} catch (Exception ex) {
			logger.error("Unable to get fiscal calendar" + ex);
		}
		return calendar;
	}

	public static Collection<AmpActivity> getAllActivities() {
		Session session = null;
		Query qry = null;
		Collection<AmpActivity> activities = new ArrayList<AmpActivity>();
		try {
			session = PersistenceManager.getSession();
			String queryString = "select f from " + AmpActivity.class.getName()
					+ " f";
			qry = session.createQuery(queryString);
			activities = qry.list();
		} catch (Exception e) {
			logger.error("Unable to get all activities");
			logger.debug("Exceptiion " + e);
		} finally {
			PersistenceManager.releaseSession(session);
		}
		return activities;
	}

	public static Collection<AmpActivityVersion> getAllactivitiesRelatedToOrg(
			Long orgId) {
		Session session = null;
		Query qry = null;
		Collection<AmpActivityVersion> activities = new ArrayList<AmpActivityVersion>();
		try {
			session = PersistenceManager.getSession();
			String queryString = "select role.activity from "
					+ AmpOrgRole.class.getName()
					+ " role,"
					+ AmpActivity.class.getName()
					+ " a "
					+ " where role.activity = a.ampActivityId and role.organisation="
					+ orgId;
			qry = session.createQuery(queryString);
			activities = qry.list();
		} catch (Exception e) {
			logger.error("Unable to get all activities");
			logger.debug("Exceptiion " + e);
		} finally {
			PersistenceManager.releaseSession(session);
		}
		return activities;
	}

	public static Collection getAllOrgActivities(Long orgId) {
		Session session = null;
		Query qry = null;
		Collection activities = new ArrayList();
		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select f from " + AmpActivity.class.getName()
					+ " f where (f.internalId=:orgId)";
			qry = session.createQuery(queryString);
			qry.setParameter("orgId", orgId, Hibernate.LONG);
			activities = qry.list();
		} catch (Exception e) {
			logger.error("Unable to get all activities");
			logger.debug("Exceptiion " + e);
		}
		return activities;
	}

	public static int getAllOrgActivitiesCount(Long orgId) {
		Session session = null;
		Query qry = null;
		int activitiesCount = 0;
		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select count(*) from "
					+ AmpActivityInternalId.class.getName()
					+ " f,"+AmpActivityGroup.class.getName()+" g where f.ampActivity = g.ampActivityLastVersion and (f.organisation=:orgId)";
			qry = session.createQuery(queryString);
			qry.setParameter("orgId", orgId, Hibernate.LONG);
			activitiesCount = (Integer) qry.uniqueResult();
		} catch (Exception e) {
			logger.error("Unable to get all activities");
			logger.debug("Exceptiion " + e);
		}
		return activitiesCount;
	}

	public static AmpFiscalCalendar getAmpFiscalCalendar(Long ampFisCalId) {
		Session session = null;
		Query qry = null;
		AmpFiscalCalendar ampFisCal = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select f from "
					+ AmpFiscalCalendar.class.getName()
					+ " f where (f.ampFiscalCalId=:ampFisCalId)";
			qry = session.createQuery(queryString);
			qry.setParameter("ampFisCalId", ampFisCalId, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				ampFisCal = (AmpFiscalCalendar) itr.next();
			}
		} catch (Exception e) {
			logger.error("Unable to get fiscalCalendar");
			logger.debug("Exceptiion " + e);
		}
		return ampFisCal;
	}

	public static User getUser(String email) {
		Session session = null;
		Query qry = null;
		User user = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select u from " + User.class.getName()
					+ " u where (u.email=:email)";
			qry = session.createQuery(queryString);
			qry.setParameter("email", email, Hibernate.STRING);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				user = (User) itr.next();
			}
		} catch (Exception e) {
			logger.error("Unable to get user");
			logger.debug("Exceptiion " + e);
		}
		return user;
	}

	public static User getUser(Long userId) {
		Session session = null;
		Query qry = null;
		User user = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select u from " + User.class.getName()
					+ " u where (u.id=:userId)";
			qry = session.createQuery(queryString);
			qry.setParameter("userId", userId, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				user = (User) itr.next();
			}
		} catch (Exception e) {
			logger.error("Unable to get user");
			logger.debug("Exceptiion " + e);
		}
		return user;
	}

	/**
	 * @author Arty
	 * @param reportId
	 *            Sets the the defaultTeamReport to null for all the AppSettings
	 *            that were referencing the
	 */
	public static void updateAppSettingsReportDeleted(Long reportId) {
		Session session = null;
		Query qry = null;
		AmpApplicationSettings ampAppSettings = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select a from "
					+ AmpApplicationSettings.class.getName()
					+ " a where (a.defaultTeamReport=:repId)";
			qry = session.createQuery(queryString);
			qry.setLong("repId", reportId);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				ampAppSettings = (AmpApplicationSettings) itr.next();
				ampAppSettings.setDefaultTeamReport(null);
				update(ampAppSettings);
				// ////System.out.println("Am updatat: " +
				// ampAppSettings.getAmpAppSettingsId());
			}
		} catch (Exception e) {
			logger.error("Unable to get TeamAppSettings");
			logger.debug("Exceptiion " + e);
		}
	}

	public static AmpApplicationSettings getTeamAppSettings(Long teamId) {
		Session session = null;
		Query qry = null;
		AmpApplicationSettings ampAppSettings = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "from "
					+ AmpApplicationSettings.class.getName()
					+ " a where (a.team.ampTeamId=:teamId)";
			qry = session.createQuery(queryString);
			qry.setLong("teamId", teamId);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				ampAppSettings = (AmpApplicationSettings) itr.next();
			}

		} catch (Exception e) {
			logger.error("Unable to get TeamAppSettings", e);
		}
		return ampAppSettings;
	}

	public static AmpApplicationSettings getTeamAppSettingsMemberNotNull(
			Long teamId) {
		Session session = null;
		Query qry = null;
		AmpApplicationSettings ampAppSettings = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select a from "
					+ AmpApplicationSettings.class.getName()
					+ " a where (a.team=:teamId) ";
			qry = session.createQuery(queryString);
			qry.setLong("teamId", teamId);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				ampAppSettings = (AmpApplicationSettings) itr.next();
				if (ampAppSettings != null)
					break;
			}

		} catch (Exception e) {
			logger.error("Unable to get TeamAppSettings", e);
		}
		return ampAppSettings;
	}

	public static boolean isUserTranslator(Long userId) {

		logger.debug("In isUserTranslator()");
		User user = null;
		Session session = null;
		boolean flag = false;
		try {
			session = PersistenceManager.getRequestDBSession();

			// modified by Priyajith
			// desc:used select query instead of session.load
			// start
			String queryString = "select u from " + User.class.getName()
					+ " u " + "where (u.id=:id)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", userId, Hibernate.LONG);
			Iterator itrTemp = qry.list().iterator();
			while (itrTemp.hasNext()) {
				user = (User) itrTemp.next();
			}
			// end

			Iterator itr = user.getGroups().iterator();
			if (!itr.hasNext()) {
				logger.debug("No groups");
			}
			while (itr.hasNext()) {
				Group grp = (Group) itr.next();
				logger.debug("Group key is " + grp.getKey());
				if ((grp.getKey() != null) && "TRN".equals(grp.getKey().trim())) {
					logger.debug("setting flag as true");
					flag = true;
					break;
				} else {
					logger.debug("in else");
				}
			}
		} catch (Exception ex) {
			logger.error("Unable to get team member ", ex);
		}
		return flag;
	}

	/*
	 * Get all reports if tabs = null (all) tab = true only get tabs tab = false
	 * reports which aren't tabs
	 */
	public static Collection getAllReports(Boolean tabs) {
		Session session = null;
		Query qry = null;
		Collection reports = new ArrayList();

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select r from " + AmpReports.class.getName()
					+ " r";
			if (tabs != null) {
				if (tabs) {
					queryString += " where r.drilldownTab=true ";
				} else {
					queryString += " where r.drilldownTab=false ";
				}
			}
			qry = session.createQuery(queryString);
			reports = qry.list();
		} catch (Exception e) {
			logger.error("Unable to get all reports", e);
		}
		return reports;
	}

	public static AmpReports getAmpReport(Long id) {
		AmpReports ampReports = null;
		Session session = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			// modified by Priyajith
			// desc:used select query instead of session.load
			// start
			String queryString = "select r from " + AmpReports.class.getName()
					+ " r " + "where (r.ampReportId=:id)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				ampReports = (AmpReports) itr.next();
			}
			// end
		} catch (Exception ex) {
			logger.error("Unable to get reports " + ex);
		}
		return ampReports;
	}

	public static AmpReportLog getAmpReportLog(Long report_id, Long member_id) {
		AmpReportLog ampReportMemberLog = null;
		Session session = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select r from "
					+ AmpReportLog.class.getName() + " r "
					+ "where (r.report=:id and r.member.ampTeamMemId=:member)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", report_id, Hibernate.LONG);
			qry.setParameter("member", member_id, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				ampReportMemberLog = (AmpReportLog) itr.next();
			}
			// end
		} catch (Exception ex) {
			logger.error("Unable to get reportmemberlog " + ex);
		}
		return ampReportMemberLog;
	}

	public static Collection getMembersUsingReport(Long id) {

		Session session = null;
		Collection col = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			// modified by Priyajith
			// desc:used select query instead of session.load
			// start
			String queryString = "select r from " + AmpReports.class.getName()
					+ " r " + "where (r.ampReportId=:id)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);
			Iterator itrTemp = qry.list().iterator();
			AmpReports ampReports = null;
			while (itrTemp.hasNext()) {
				ampReports = (AmpReports) itrTemp.next();
			}
			// end
			Iterator itr = ampReports.getMembers().iterator();
			col = new ArrayList();
			while (itr.hasNext()) {
				col.add(itr.next());
			}
		} catch (Exception e) {
			logger.error("Exception from getMembersUsingReport()", e);
		}
		return col;
	}

	public static Collection getAllConfigurablePages(Long teamId) {
		Session session = null;
		Query qry = null;
		Collection pages = new ArrayList();
		String qryStr = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			qryStr = "select p from " + AmpPages.class.getName()
					+ " p where p.pageCode in ('" + Constants.DESKTOP_PG_CODE
					+ "','" + Constants.FINANCIAL_PG_CODE + "')";
			qry = session.createQuery(qryStr);
			pages = qry.list();

			qryStr = "select tr from " + AmpTeamReports.class.getName()
					+ " tr " + "where (tr.team=:tId)";
			qry = session.createQuery(qryStr);
			qry.setParameter("tId", teamId, Hibernate.LONG);
			Iterator tempItr = qry.list().iterator();
			String params = "";
			while (tempItr.hasNext()) {
				AmpTeamReports tr = (AmpTeamReports) tempItr.next();
				if (params.length() > 0) {
					params += ",";
				}
				params += "'" + tr.getReport().getName().replaceAll("'", "''")
						+ "'";
			}
			if (params.length() > 0) {
				logger.info("Params :" + params);

				qryStr = "select p from " + AmpPages.class.getName() + " p "
						+ "where p.pageName in (" + params + ")";
				Iterator itr = session.createQuery(qryStr).list().iterator();
				while (itr.hasNext()) {
					AmpPages p = (AmpPages) itr.next();
					pages.add(p);
				}
			}

		} catch (Exception e) {
			logger.error("Unable to get all configurable pages");
			logger.error("Exceptiion is :" + e);
			e.printStackTrace(System.out);
		}
		return pages;
	}

	public static Collection getAllPageFilters(Long id) {

		Session session = null;
		Collection col = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			// modified by Priyajith
			// desc:used select query instead of session.load
			// start
			String queryString = "select p from " + AmpPages.class.getName()
					+ " p " + "where (p.ampPageId=:id)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);
			Iterator itrTemp = qry.list().iterator();
			AmpPages ampPage = null;
			while (itrTemp.hasNext()) {
				ampPage = (AmpPages) itrTemp.next();
			}
			// end
			Iterator itr = ampPage.getFilters().iterator();
			col = new ArrayList();
			while (itr.hasNext()) {
				col.add(itr.next());
			}
		} catch (Exception e) {
			logger.error("Exception from getAllPageFilters()", e);
		}
		return col;
	}

	public static AmpPages getAmpPage(Long pageId) {
		Session session = null;
		Query qry = null;
		AmpPages page = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			// modified by Priyajith
			// desc:used select query instead of session.load
			// start
			String queryString = "select p from " + AmpPages.class.getName()
					+ " p " + "where (p.ampPageId=:id)";
			qry = session.createQuery(queryString);
			qry.setParameter("id", pageId, Hibernate.LONG);
			Iterator itrTemp = qry.list().iterator();
			while (itrTemp.hasNext()) {
				page = (AmpPages) itrTemp.next();
			}
			// end

			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				page = (AmpPages) itr.next();
			}
		} catch (Exception e) {
			logger.error("Unable to get AmpPage", e);
		}
		return page;
	}

	public static AmpFilters getAmpFilter(Long filterId) {
		Session session = null;
		Query qry = null;
		AmpFilters filter = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			// modified by Priyajith
			// desc:used select query instead of session.load
			// start
			String queryString = "select f from " + AmpFilters.class.getName()
					+ " f " + "where (f.ampFilterId=:id)";
			qry = session.createQuery(queryString);
			qry.setParameter("id", filterId, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				filter = (AmpFilters) itr.next();
			}
			// end
		} catch (Exception e) {
			logger.error("Unable to get AmpFilter", e);
		}
		return filter;
	}

	public static ArrayList getTeamPageFilters(Long teamId, Long pageId) {
		Session session = null;
		ArrayList col = new ArrayList();

		try {
			session = PersistenceManager.getRequestDBSession();
			String qryStr = "select tpf.filter.ampFilterId from "
					+ AmpTeamPageFilters.class.getName() + " tpf "
					+ "where (tpf.team=:tId) and (tpf.page=:pId)";
			Query qry = session.createQuery(qryStr);
			qry.setParameter("tId", teamId, Hibernate.LONG);
			qry.setParameter("pId", pageId, Hibernate.LONG);

			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				col.add((Long) itr.next());
			}
		} catch (Exception e) {
			logger.error("Exception from getTeamPageFilters()", e);
		}
		return col;
	}

	public static Collection getFilters(Long teamId, Long pageId) {
		Session session = null;
		Collection col = new ArrayList();

		try {
			session = PersistenceManager.getRequestDBSession();
			String qryStr = "select tpf.filter.ampFilterId from "
					+ AmpTeamPageFilters.class.getName() + " tpf "
					+ "where (tpf.team=:tId) and (tpf.page=:pId)";
			Query qry = session.createQuery(qryStr);
			qry.setParameter("tId", teamId, Hibernate.LONG);
			qry.setParameter("pId", pageId, Hibernate.LONG);

			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				Long fId = (Long) itr.next();
				AmpFilters filter = (AmpFilters) session.load(AmpFilters.class,
						fId);
				col.add(filter);
			}
		} catch (Exception e) {
			logger.error("Exception from getFilters()", e);
		}
		return col;
	}

	/**
	 * Replaces DbUtil.getAllAssistanceTypes()
	 */
	public static Collection<AmpCategoryValue> getAllAssistanceTypesFromCM() {
		return CategoryManagerUtil
				.getAmpCategoryValueCollectionByKey(CategoryConstants.TYPE_OF_ASSISTENCE_KEY);
	}

	/**
	 * Loads all objects of T from database, using request session. TODO there
	 * are several methods like this, let's refactor to one.
	 * 
	 * @param <T>
	 * @param object
	 * @return
	 * @throws DgException
	 */
	public static <T> Collection<T> getAll(Class<T> object) throws DgException {
		return getAll(object, PersistenceManager.getRequestDBSession());
	}

	/**
	 * Loads all objects of T from database. Client should care about opening
	 * and releasing session which is passed as parameter to this method.
	 * 
	 * @param <T>
	 * @param object
	 * @param session
	 *            database session. Client should handle session - opening and
	 *            releasing, including transactions if required.
	 * @return
	 * @throws DgException
	 */
	public static <T> Collection<T> getAll(Class<T> object, Session session)
			throws DgException {
		Collection<T> col = null;
		try {
			String queryString = "from " + object.getName();
			Query qry = session.createQuery(queryString);
			col = qry.list();
		} catch (Exception e) {
			logger.error("Exception from getAll()", e);
			throw new DgException(e);
		}
		return col;
	}

	public static Collection getAllCountries() {
		Session session = null;
		Collection col = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select c from " + Country.class.getName()
					+ " c order by c.countryName asc";
			Query qry = session.createQuery(queryString);
			col = qry.list();
		} catch (Exception e) {
			logger.error("Exception from getAllCountries()", e);
		}
		return col;
	}
	
	public static Collection getAllAmpApplicationSettings() {
		Session session = null;
		Collection col = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "FROM " + AmpApplicationSettings.class.getName();
			Query qry = session.createQuery(queryString);
			col = qry.list();
		} catch (Exception e) {
			logger.error("Exception from getAllAmpApplicationSettings()", e);
		}
		return col;
	}

	public static Country getDgCountry(String iso) {
		Session session = null;
		Country country = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select c from " + Country.class.getName()
					+ " c " + "where (c.iso=:iso)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("iso", iso, Hibernate.STRING);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				country = (Country) itr.next();
			}

		} catch (Exception e) {
			logger.error("Exception from getDgCountry()", e);
		}
		return country;
	}

	public static Country getCountryByName(String name) {
		Session session = null;
		Country country = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select c from " + Country.class.getName()
					+ " c " + "where (c.countryName=:name)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("name", name, Hibernate.STRING);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				country = (Country) itr.next();
			}

		} catch (Exception e) {
			logger.error("Exception from getDgCountry()", e);
		}
		return country;
	}

	public static Country getDgCountry(Long id) {
		Session session = null;
		Country country = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select c from " + Country.class.getName()
					+ " c " + "where (c.countryId=:id)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				country = (Country) itr.next();
			}

		} catch (Exception e) {
			logger.error("Exception from getDgCountry()", e);
		}
		return country;
	}

	/*
	 * public static ArrayList getAmpModality() { Session session = null; Query
	 * q = null; AmpModality ampModality = null; ArrayList modality = new
	 * ArrayList(); String queryString = null; Iterator iter = null;
	 * 
	 * try { session = PersistenceManager.getRequestDBSession(); queryString =
	 * " select Modality from " + AmpModality.class.getName() +
	 * " Modality order by Modality.name"; q = session.createQuery(queryString);
	 * iter = q.list().iterator();
	 * 
	 * while (iter.hasNext()) {
	 * 
	 * ampModality = (AmpModality) iter.next(); modality.add(ampModality); }
	 * 
	 * } catch (Exception ex) {
	 * 
	 * logger
	 * .debug("Modality : Unable to get Amp Activity names  from database " +
	 * ex.getMessage());
	 * 
	 * } return modality; }
	 */

	/**
	 * @deprecated Use getAmpStatusFromCM instead which uses the Category
	 *             Manager
	 */
	public static ArrayList getAmpStatus() {
		Session session = null;
		Query q = null;
		AmpStatus ampStatus = null;
		ArrayList status = new ArrayList();
		String queryString = null;
		Iterator iter = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			queryString = " select Status from " + AmpStatus.class.getName()
					+ " Status order by Status.name";
			q = session.createQuery(queryString);
			iter = q.list().iterator();
			while (iter.hasNext()) {

				ampStatus = (AmpStatus) iter.next();
				status.add(ampStatus);
			}

		} catch (Exception ex) {
			logger.error("Unable to get Amp status   from database "
					+ ex.getMessage());
		}
		return status;
	}

	public static ArrayList<AmpCategoryValue> getAmpStatusFromCM(
			HttpServletRequest request) {
		ArrayList<AmpCategoryValue> result = null;
		try {
			result = new ArrayList<AmpCategoryValue>(
					CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.ACTIVITY_STATUS_KEY, true));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @deprecated Use category manager instead
	 * 
	 * @return
	 * 
	 */
	public static ArrayList getAmpLevels() {
		Session session = null;
		Query q = null;
		AmpLevel ampLevel = null;
		ArrayList level = new ArrayList();
		String queryString = null;
		Iterator iter = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			queryString = " select Level from " + AmpLevel.class.getName()
					+ " Level";
			q = session.createQuery(queryString);
			iter = q.list().iterator();

			while (iter.hasNext()) {

				ampLevel = (AmpLevel) iter.next();
				level.add(ampLevel);
			}

		} catch (Exception ex) {
			logger.error("Unable to get Amp levels  from database "
					+ ex.getMessage());
		}
		return level;
	}

	public static Collection<AmpOrganisation> getFiscalCalOrgs(Long fiscalCalId) {

		Session sess = null;
		Collection<AmpOrganisation> col = null;
		Query qry = null;

		try {
			sess = PersistenceManager.getRequestDBSession();
			String queryString = "select o from "
					+ AmpOrganisation.class.getName()
					+ " o where (o.ampFiscalCalId=:ampFisCalId) and (o.deleted is null or o.deleted = false) ";
			qry = sess.createQuery(queryString);
			qry.setParameter("ampFisCalId", fiscalCalId, Hibernate.LONG);
			Iterator<AmpOrganisation> itr = qry.list().iterator();
			col = new ArrayList<AmpOrganisation>();
			while (itr.hasNext()) {
				col.add(itr.next());
			}
		} catch (Exception e) {
			logger.error("Exception from getFiscalCalOrgs()", e);
		}
		return col;

	}

	public static Collection<AmpApplicationSettings> getFiscalCalSettings(
			Long fiscalCalId) {

		Session sess = null;
		Collection<AmpApplicationSettings> col = null;
		Query qry = null;

		try {
			sess = PersistenceManager.getRequestDBSession();
			String queryString = "select o from "
					+ AmpApplicationSettings.class.getName()
					+ " o where (o.fiscalCalendar=:ampFisCalId)";
			qry = sess.createQuery(queryString);
			qry.setLong("ampFisCalId", fiscalCalId);
			Iterator<AmpApplicationSettings> itr = qry.list().iterator();
			col = new ArrayList();
			while (itr.hasNext()) {
				col.add(itr.next());
			}
		} catch (Exception e) {
			logger.error("Exception from getFiscalCalSettings()", e);
		}
		return col;
	}

	public static AmpFiscalCalendar getFiscalCalByName(String name) {
		Session session = null;
		Query qry = null;
		AmpFiscalCalendar ampFisCal = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select f from "
					+ AmpFiscalCalendar.class.getName()
					+ " f where (f.name=:name)";
			qry = session.createQuery(queryString);
			qry.setParameter("name", name, Hibernate.STRING);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				ampFisCal = (AmpFiscalCalendar) itr.next();
			}
		} catch (Exception e) {
			logger.error("Unable to get fiscal Calendar", e);
		}
		return ampFisCal;
	}

    public static Collection searchForOrganisation(String keyword, Long orgType) {
        return searchForOrganisation(keyword, orgType, (long[]) null);
    }

	public static Collection searchForOrganisation(String keyword, Long orgType, long[] excludeIds) {
		Session session = null;
		Collection col = null;
		keyword = keyword.toLowerCase();
        StringBuilder queryString = new StringBuilder();

        try {
			session = PersistenceManager.getRequestDBSession();
			String organizationName = AmpOrganisation.hqlStringForName("org");
			queryString.append(" select org from ")
                    .append(AmpOrganisation.class.getName()).append(" org ")
                    .append(" inner join org.orgGrpId grp ")
                    .append(" where(lower(org.acronym) like '%")
                    .append(keyword)
                    .append("%' or lower(" + organizationName + ") like '%")
                    .append(keyword)
                    .append("%') and grp.orgType=:orgType and (org.deleted is null or org.deleted = false)");

            appendNotIn("org.ampOrgId", excludeIds, queryString);

			Query qry = session.createQuery(queryString.toString());
			qry.setParameter("orgType", orgType, Hibernate.LONG);
			col = qry.list();
		} catch (Exception ex) {
			logger.error("Unable to search ", ex);
		}
		return col;
	}

    public static Collection searchForOrganisation(String keyword) {
        return searchForOrganisation(keyword, (long[]) null);
    }

	public static Collection searchForOrganisation(String keyword, long[] excludeIds) {

		Session session = null;
		Collection col = null;
		keyword = keyword.toLowerCase();
        StringBuilder queryString = new StringBuilder();

		try {
			session = PersistenceManager.getRequestDBSession();
			String organizationName = AmpOrganisation.hqlStringForName("org");
			queryString.append("select distinct org from ")
                    .append(AmpOrganisation.class.getName()).append(" org ")
                    .append("where (lower(acronym) like '%").append(keyword)
                    .append("%' or lower(" + organizationName + ") like '%").append(keyword)
                    .append("%') and (org.deleted is null or org.deleted = false) ");

            appendNotIn("org.ampOrgId", excludeIds, queryString);

			Query qry = session.createQuery(queryString.toString());
			col = qry.list();
		} catch (Exception ex) {
			logger.error("Unable to search ", ex);
		}
		return col;
	}

	/**
	 * This function gets all organizations whose names begin with
	 * namesFirstLetter and name or acronym contain keyword
	 * 
	 * @author Dare
	 */
	public static Collection searchForOrganisation(String namesFirstLetter,
			String keyword) {
		Session session = null;
		Collection col = null;
		if (keyword.length() != 0) {
			keyword = keyword.toLowerCase();
		}
		namesFirstLetter = namesFirstLetter.toLowerCase();

		try {
			session = PersistenceManager.getRequestDBSession();
			String organizationName = AmpOrganisation.hqlStringForName("org");
			String queryString = "select distinct org from "
					+ AmpOrganisation.class.getName() + " org "
					+ "where ((lower(acronym) like '%" + keyword
					+ "%' and lower(" + organizationName + ") like '" + namesFirstLetter
					+ "%') or lower(" + organizationName + ") like '" + namesFirstLetter + "%"
					+ keyword
					+ "%') and (org.deleted is null or org.deleted = false)";
			Query qry = session.createQuery(queryString);
			col = qry.list();
		} catch (Exception ex) {
			logger.debug("Unable to search " + ex);
		}
		return col;
	}

	/**
	 * This function gets all organizations whose names begin with
	 * namesFirstLetter and name or acronym contain keyword and organisation
	 * type is orgType
	 * 
	 * @author Mouhamad
	 */
	public static Collection searchForOrganisation(String namesFirstLetter,
			String keyword, Long orgType) {
		Session session = null;
		Collection col = null;
		if (keyword.length() != 0) {
			keyword = keyword.toLowerCase();
		}
		namesFirstLetter = namesFirstLetter.toLowerCase();

		try {
			session = PersistenceManager.getRequestDBSession();
			String organizationName = AmpOrganisation.hqlStringForName("org");
			String queryString = "select distinct org from "
					+ AmpOrganisation.class.getName()
					+ " org inner join org.orgGrpId grp "
					+ "where grp.orgType=:orgType and ((lower(acronym) like '%"
					+ keyword + "%' and lower(" + organizationName + ") like '" + namesFirstLetter
					+ "%') or lower(" + organizationName + ") like '" + namesFirstLetter + "%"
					+ keyword
					+ "%') and (org.deleted is null or org.deleted = false)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("orgType", orgType, Hibernate.LONG);
			col = qry.list();
		} catch (Exception ex) {
			logger.debug("Unable to search " + ex);
		}
		return col;
	}

    public static List<AmpOrganisation> searchForOrganisationByType(Long orgType) {
        return searchForOrganisationByType(orgType, null);
    }

    /**
     * returns list of all Organisations which belong to a group which belongs to a type and do not have an id in the exclusion area
     * @param orgType
     * @param excludeIds
     * @return
     */
	public static List<AmpOrganisation> searchForOrganisationByType(Long orgType, long[] excludeIds) {

		try {
			Session session = PersistenceManager.getRequestDBSession();
			StringBuilder queryString = new StringBuilder();
			queryString.append("select distinct org from ")
                    .append(AmpOrganisation.class.getName()).append(" org ")
                    .append(" inner join org.orgGrpId grp where grp.orgType=:orgType and (org.deleted is null or org.deleted = false)");

            appendNotIn("org.ampOrgId", excludeIds, queryString);

			Query qry = session.createQuery(queryString.toString());
			qry.setLong("orgType", orgType);
			return qry.list();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

    /**
     * Appends the NOT IN criteria to the queryString
     * @param columnName
     * @param excludeIds
     * @param queryString
     * @return
     */
    private static StringBuilder appendNotIn(String columnName, long[] excludeIds, StringBuilder queryString) {
        if (excludeIds != null && excludeIds.length > 0) {
            queryString.append(" and (").append(columnName).append(" not in (");
            for (long orgId : excludeIds) {
                queryString.append(orgId).append(",");
            }
            // remove last comma
            queryString.setLength(queryString.length() - 1);
            queryString.append("))");
        }
        return queryString;
    }

    public static ArrayList<AmpOrganisation> getAmpOrganisations() {
        return getAmpOrganisations(null);
    }

    /**
     * Returns list of amp organizations, excluding the <code>excludeIds</code>
     * @param excludeIds if not null, the organizations with these ids will be excluded from the search result
     * @return
     */
	public static ArrayList<AmpOrganisation> getAmpOrganisations(long[] excludeIds) {

        Session session = null;
		Query q = null;
		ArrayList<AmpOrganisation> organizations = new ArrayList<AmpOrganisation>();
		StringBuilder queryString = new StringBuilder();

		try {
			session = PersistenceManager.getRequestDBSession();

            queryString.append(" select org from ").append(AmpOrganisation.class.getName()).append(" org ");
            queryString.append(" where (org.deleted is null or org.deleted = false)");

            appendNotIn("org.ampOrgId", excludeIds, queryString);

            queryString.append(" order by org.name");

			q = session.createQuery(queryString.toString());
			if (q.list() != null && q.list().size() > 0) {
				organizations.addAll(q.list());
			}

		} catch (Exception ex) {
			logger.error("Unable to get Amp organisation names  from database "
					+ ex.getMessage());
		}
		return organizations;
	}

	public static List<AmpOrganisation> getAmpOrganisations(Long orgId,
			Long groupId, Long typeId) {
		Session session = null;
		Query q = null;
		List<AmpOrganisation> organizations = null;
		StringBuilder queryString = new StringBuilder();

		try {
			session = PersistenceManager.getRequestDBSession();
			queryString.append(" select org from ");
			queryString.append(AmpOrganisation.class.getName());
			queryString.append(" org inner join org.orgGrpId grp ");
			queryString.append("  inner join grp.orgType type ");

			boolean orgCond = orgId != null && orgId != -1;
			boolean groupCond = groupId != null && groupId != -1;
			boolean typeCond = typeId != null && typeId != -1;

			queryString
					.append(" where (org.deleted is null or org.deleted = false) ");
			/*
			 * if (orgCond || groupCond || typeCond) {
			 * queryString.append(" where 1=1 "); }
			 */
			if (groupCond) {
				queryString.append(" and grp.ampOrgGrpId=:groupId ");
			}
			if (typeCond) {
				queryString.append(" and type.ampOrgTypeId=:typeId ");
			}
			if (orgCond) {
				queryString.append(" and org.ampOrgId=:orgId ");
			}
			queryString.append(" order by org.name ");
			q = session.createQuery(queryString.toString());
			if (orgCond) {
				q.setLong("orgId", orgId);
			}
			if (groupCond) {
				q.setLong("groupId", groupId);
			}
			if (typeCond) {
				q.setLong("typeId", typeId);
			}

			organizations = q.list();

		} catch (Exception ex) {
			logger.error("Unable to get Amp organisation names  from database "
					+ ex.getMessage());
		}
		return organizations;
	}

	public static List<AmpOrganisation> getBilMulOrganisations() {
		Session session = null;
		Query q = null;
		List<AmpOrganisation> organizations = new ArrayList<AmpOrganisation>();
		String queryString = null;
		Iterator iter = null;

		try {
			AmpOrgType tBil = getAmpOrgTypeByCode("BIL");
			AmpOrgType tMul = getAmpOrgTypeByCode("MUL");

			session = PersistenceManager.getRequestDBSession();
			queryString = " select org from "
					+ AmpOrganisation.class.getName()
					+ " org "
					+ " inner join org.orgGrpId grp "
					+ " where grp.orgType='"
					+ tBil.getAmpOrgTypeId()
					+ "' or grp.orgType='"
					+ tMul.getAmpOrgTypeId()
					+ "' and (org.deleted is null or org.deleted = false) order by org.name";
			q = session.createQuery(queryString);
			organizations = q.list();

		} catch (Exception ex) {
			logger.error("Unable to get Amp organisation names  from database "
					+ ex.getMessage());
			ex.printStackTrace();
		}
		return organizations;
	}

	/**
	 * Returns true if org group is used in the widget table otherwise false
	 * 
	 * @param orgGroupId
	 * @return
	 */

	public static boolean isUsed(Long id, boolean isOrgGroup) {
		boolean isUsed = true;
		Session session = null;
		Query q = null;
		String queryString = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			queryString = " select val from "
					+ AmpDaValueFiltered.class.getName()
					+ " val inner join val.column col where val.filterItemId=:id and col.filterItemProvider=:filterItemProvider";
			q = session.createQuery(queryString);
			q.setLong("id", id);
			if (isOrgGroup) {
				q.setLong("filterItemProvider", FilterItemProvider.ORG_GROUPS);
			} else {
				q.setLong("filterItemProvider",
						FilterItemProvider.DONORS_FILTER);
			}
			if (q.list().size() == 0) {
				isUsed = false;
			}
		} catch (Exception ex) {
			logger.error("Unable to get Org Groups  from database "
					+ ex.getMessage());
		}
		return isUsed;
	}
	
	/**
	 * returns a sorted-by-name list of @link {@link OrganizationSkeleton} instances selected by a HQL query <br />
	 * the query should be of the for, "SELECT ampOrgId, ampOrgName ..."
	 * @param query
	 * @return
	 */
	public static List<OrganizationSkeleton> getOrgSkeletonsFromQuery(Query query)
	{
		ArrayList<OrganizationSkeleton> res = new ArrayList<OrganizationSkeleton>();
		List<Object[]> lst = query.list();
		for(Object[] item:lst)
		{
			OrganizationSkeleton skel = new OrganizationSkeleton();
			skel.setAmpOrgId(PersistenceManager.getLong(item[0]));
			skel.setName(PersistenceManager.getString(item[1]));
			res.add(skel);
		}
		Collections.sort(res);
		return res;
	}
	
	/**
	 * returns a representation of the organizations in the database (lightweight, composed of of @link {@link OrganizationSkeleton}).<br />
	 * they are grouped by ampOrgGroup 
	 * @return
	 */
	public static java.util.Map<Long, Set<OrganizationSkeleton>> getOrgSkeletonGroupedByGroupId()
	{	
		java.util.Map<Long, Set<OrganizationSkeleton>> res = new java.util.HashMap<Long, Set<OrganizationSkeleton>>();
		
		String queryString = "select org.orgGrpId.ampOrgGrpId, org.ampOrgId, " + AmpOrganisation.hqlStringForName("org") + " FROM " + AmpOrganisation.class.getName()
				+ " org where (org.deleted is null or org.deleted = false)";
		
		Query q = PersistenceManager.getSession().createQuery(queryString);
		
		List<Object[]> lst = q.list();
		for(Object[] item:lst)
		{
			OrganizationSkeleton skel = new OrganizationSkeleton();			
			skel.setAmpOrgId(PersistenceManager.getLong(item[1]));
			skel.setName(PersistenceManager.getString(item[2]));
			
			Long orgGrpId = PersistenceManager.getLong(item[0]);
			if (!res.containsKey(orgGrpId))
				res.put(orgGrpId, new java.util.TreeSet<OrganizationSkeleton>());
			res.get(orgGrpId).add(skel);
		}
		return res;
	}
	
	/**
	 * returns a sorted-by-name list of @link {@link OrganizationSkeleton} 
	 * @param orgGroupId - orgGroupId to filter by. If equals null -> no filtering
	 * @return
	 */
	public static List<OrganizationSkeleton> getOrgSkeletonByGroupId(Long orgGroupId)
	{
		String orgGrpCondition = orgGroupId == null ? "1=1" : "org.orgGrpId=:orgGroupId";
		
		String queryString = "select org.ampOrgId, " + AmpOrganisation.hqlStringForName("org") + " FROM " + AmpOrganisation.class.getName()
				+ " org where " + orgGrpCondition + " and (org.deleted is null or org.deleted = false)";
		
		Query q = PersistenceManager.getSession().createQuery(queryString);
		if (orgGroupId != null)
			q.setLong("orgGroupId", orgGroupId);
		return getOrgSkeletonsFromQuery(q);	
	}
	

	public static List<AmpOrganisation> getOrganisationByGroupId(Long orgGroupId) {
		Session session = null;
		Query q = null;
		List<AmpOrganisation> organizations = new ArrayList<AmpOrganisation>();
		String queryString = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			queryString = " select org from "
					+ AmpOrganisation.class.getName()
					+ " org where org.orgGrpId=:orgGroupId and (org.deleted is null or org.deleted = false) order by org.name ";
			q = session.createQuery(queryString);
			q.setLong("orgGroupId", orgGroupId);
			organizations = q.list();
		} catch (Exception ex) {
			logger.error("Unable to get Amp organisation names  from database "
					+ ex.getMessage());
		}
		return organizations;
	}

	public static List<OrganizationSkeleton> getDonorOrganisationByGroupId(Long orgGroupId, boolean publicView)
	{
		List<OrganizationSkeleton> organizations = new ArrayList<OrganizationSkeleton>();
		StringBuilder queryString = new StringBuilder(
				"SELECT DISTINCT(orgRole.organisation.ampOrgId) FROM " + AmpOrgRole.class.getName() + " orgRole");
		
		queryString.append(" where orgRole.role.roleCode='DN' and (orgRole.activity.draft = false OR orgRole.activity.draft is null) ");
		
		if (orgGroupId != null && orgGroupId != -1) {
			queryString.append(" and orgRole.organisation.orgGrpId=:orgGroupId ");
		}
		
		if (publicView) {
			queryString.append(String.format(" and orgRole.activity.approvalStatus in ('%s', '%s') and orgRole.activity.team.parentTeamId is not null ", Constants.APPROVED_STATUS, Constants.STARTED_APPROVED_STATUS));
		}

		Query query = PersistenceManager.getSession().createQuery(queryString.toString());
		if (orgGroupId != null && orgGroupId != -1) {
			query.setLong("orgGroupId", orgGroupId);
		}

		String orgIds = Util.toCSStringForIN(query.list());
		
		return getOrgSkeletonsFromQuery(PersistenceManager.getSession().createQuery("SELECT org.ampOrgId, " + AmpOrganisation.hqlStringForName("org") + " FROM " + AmpOrganisation.class.getName() + " org WHERE org.ampOrgId IN (" + orgIds + ")"));
	}

	/**
	 * 
	 * @return List of Mul and Bil organization groups
	 */

	public static Collection<AmpOrgGroup> getBilMulOrgGroups() {
		Collection<AmpOrgGroup> orgGroups = new ArrayList<AmpOrgGroup>();
		Collection<AmpOrgGroup> bilOrgGroups = new ArrayList<AmpOrgGroup>();
		Collection<AmpOrgGroup> mulOrgGroups = new ArrayList<AmpOrgGroup>();

		try {
			AmpOrgType tBil = getAmpOrgTypeByCode("BIL");
			AmpOrgType tMul = getAmpOrgTypeByCode("MUL");
			bilOrgGroups = searchForOrganisationGroupByType(tBil
					.getAmpOrgTypeId());
			mulOrgGroups = searchForOrganisationGroupByType(tMul
					.getAmpOrgTypeId());
			if (bilOrgGroups != null) {
				orgGroups.addAll(bilOrgGroups);
			}
			if (mulOrgGroups != null) {
				orgGroups.addAll(mulOrgGroups);
			}

		} catch (Exception ex) {
			logger.error("Unable to get Amp organisation names  from database "
					+ ex.getMessage());

		}
		return orgGroups;
	}

	/*
	 * gets all organisation groups excluding goverment groups
	 */
	public static Collection<AmpOrgGroup> getAllNonGovOrgGroups() {
		Session session = null;
		Query qry = null;
		Collection<AmpOrgGroup> groups = new ArrayList<AmpOrgGroup>();

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select distinct gr from "
					+ AmpOrgGroup.class.getName()
					+ " gr "
					+ " inner join gr.orgType t where t.orgTypeIsGovernmental is NULL or t.orgTypeIsGovernmental=false order by gr.orgGrpName asc";
			qry = session.createQuery(queryString);
			groups = qry.list();
		} catch (Exception e) {
			logger.error("Unable to get all organisation groups", e);
		}
		return groups;
	}

	public static void updateIndicator(AmpAhsurveyIndicator ind) {
		AmpAhsurveyIndicator oldInd = new AmpAhsurveyIndicator();
		Session session = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			oldInd = (AmpAhsurveyIndicator) session.load(
					AmpAhsurveyIndicator.class, ind.getAmpIndicatorId());

			oldInd.setAmpIndicatorId(ind.getAmpIndicatorId());
			oldInd.setCalcFormulas(ind.getCalcFormulas());
			oldInd.setIndicatorCode(ind.getIndicatorCode());
			oldInd.setIndicatorNumber(ind.getIndicatorNumber());
			oldInd.setName(ind.getName());
			oldInd.setQuestions(ind.getQuestions());
			oldInd.setStatus(ind.getStatus());
			oldInd.setTotalQuestions(ind.getTotalQuestions());

			update(oldInd);
		} catch (Exception ex) {
			logger.error("Unable to get survey indicator : ", ex);
		}
	}

	public static AmpOrgType getAmpOrgTypeByCode(String ampOrgTypeCode) {
		Session session = null;
		Query qry = null;
		AmpOrgType ampOrgType = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select f from " + AmpOrgType.class.getName()
					+ " f where (f.orgTypeCode=:ampOrgTypeCode)";
			qry = session.createQuery(queryString);
			qry.setString("ampOrgTypeCode", ampOrgTypeCode);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				ampOrgType = (AmpOrgType) itr.next();
			}
		} catch (Exception e) {
			logger.error("Unable to get Org Type", e);
		}
		return ampOrgType;
	}
	
	public static ArrayList<AmpOrgType> getAmpOrgTypes() {
		Session session = null;
		Query qry = null;
		ArrayList<AmpOrgType> org_types = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select f from " + AmpOrgType.class.getName()+ " f ";
			qry = session.createQuery(queryString);
			org_types = (ArrayList<AmpOrgType>) qry.list();
			
		} catch (Exception e) {
			logger.error("Unable to get Org Type", e);
		}
		return org_types;
	}

	public static Collection getFundingDetWithCurrId(Long currId) {
		Session sess = null;
		Collection col = null;
		Query qry = null;

		try {
			sess = PersistenceManager.getRequestDBSession();
			String queryString = "select f from "
					+ AmpFundingDetail.class.getName()
					+ " f where (f.ampCurrencyId=:currId)";
			qry = sess.createQuery(queryString);
			qry.setParameter("currId", currId, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			col = new ArrayList();
			while (itr.hasNext()) {
				col.add(itr.next());
			}

		} catch (Exception e) {
			logger.error("Exception from getFundingDetWithCurrId()", e);
		}
		return col;
	}

	public static Collection getActivityTheme(Long themeId) {
		Session sess = null;
		Collection col = null;
		Query qry = null;

		try {
			sess = PersistenceManager.getRequestDBSession();
			String queryString = "select a from " + AmpActivity.class.getName()
					+ " a where (a.themeId=:themeId)";
			qry = sess.createQuery(queryString);
			qry.setParameter("themeId", themeId, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			col = new ArrayList();
			while (itr.hasNext()) {
				col.add(itr.next());
			}

		} catch (Exception e) {
			logger.error("Exception from getActivityTheme()", e);
		}
		return col;
	}

	public static Collection getActivityThemeFromAAT(Long themeId) {
		Session sess = null;
		Collection col = null;
		try {
			// PersistenceManager.gets
			sess = PersistenceManager.getRequestDBSession();
			/*
			 * String queryString = "select a from " + AmpTheme.class.getName()
			 * + " a where (a.themeId=:themeId)";
			 */

			// AmpTheme themeToBeDeleted = (AmpTheme) sess.get(AmpTheme.class,
			// themeId);
			AmpTheme themeToBeDeleted = (AmpTheme) sess.load(AmpTheme.class,
					themeId);

			// qry = sess.createQuery(queryString);
			// qry.setParameter("themeId", themeId, Hibernate.LONG);
			Iterator itr = themeToBeDeleted.getActivities().iterator();

			col = new ArrayList();
			while (itr.hasNext()) {
				col.add(itr.next());
			}

		} catch (Exception e) {
			logger.error("Exception from getActivityTheme()", e);
		}
		return col;
	}

	public static void add(Object object) {
		//logger.debug("In add " + object.getClass().getName());
		Session sess = null;
		Transaction tx = null;

		try {
			sess = PersistenceManager.getRequestDBSession();
			// beginTransaction();
			sess.save(object);
			// tx.commit();
		} catch (Exception e) {
			logger.error("Unable to add " + object.getClass().getName());
			e.printStackTrace();
			if (tx != null) {
				try {
					tx.rollback();
				} catch (HibernateException ex) {
					logger.error("rollback() failed", ex);
				}
			}
		}
	}

	public static void update(Object object) {
		Session sess = null;
		Transaction tx = null;

		try {
			sess = PersistenceManager.getRequestDBSession();
			sess.update(object);
			// session.flush();
		} catch (Exception e) {
			logger.error(e);
		} finally {
			PersistenceManager.releaseSession(sess);
		}
	}
	
	public static void updateField(String className, Long id, String fieldName, Object newValue) {
		try {
			 Session session = PersistenceManager.getRequestDBSession();
			 String idName = PersistenceManager.sf().getClassMetadata(className).getIdentifierPropertyName();
			 Query query = session.createQuery("update " + className + " c set c."+fieldName + "=:val where c."+ idName + "=:id");
			 query.setParameter("val", newValue);
			 query.setParameter("id", id);
			 query.executeUpdate();
		} catch (DgException ex) {
			logger.error(String.format("Could not update \"%s.%s\"=\"%s\". Cause: %s",  className, fieldName,  String.valueOf(newValue), ex.getMessage()));
		}
	}

	public static AmpSectorScheme getAmpSectorSchemeById(Long schemeId) {
		Session session = null;
		Query q = null;
		AmpSectorScheme scheme = new AmpSectorScheme();
		String queryString = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			queryString = " select sch from " + AmpSectorScheme.class.getName()
					+ " sch where sch.ampSecSchemeId=:schemeId";
			q = session.createQuery(queryString);
			q.setLong("schemeId", schemeId);
			Iterator itr = q.list().iterator();
			if (itr.hasNext()) {
				scheme = (AmpSectorScheme) itr.next();
			}
		} catch (Exception ex) {
			logger.error("Unable to get AmpSectorScheme  from database "
					+ ex.getMessage());
		}
		return scheme;
	}

	/**
	 * general function to save/update object
	 * 
	 * @param object
	 * @throws Exception
	 */
	public static void saveOrUpdateObject(Object object) throws Exception {
		Session session = null;
		Transaction tx = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			// beginTransaction();
			session.saveOrUpdate(object);
			// tx.commit();
		} catch (Exception ex) {
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception e) {
					logger.error("...Rollback failed");
					throw new AimException("Can't rollback", e);
				}
			}
			throw new AimException("update failed", ex);
		}
	}

	public static void saveOrg(AmpOrganisation org) throws DgException {
		Session sess = null;
		Transaction tx = null;

		try {
			sess = PersistenceManager.getRequestDBSession();
			// beginTransaction();
			Set<AmpOrganisationContact> organisationContacts = org
					.getOrganizationContacts(); // form org contacts
			/**
			 * contact information
			 */

			if (org.getAmpOrgId() != null) { // edit
				List<AmpOrganisationContact> orgDBContacts = ContactInfoUtil
						.getOrganizationContacts(org.getAmpOrgId());
				// if organization contains contact,which is not in form contact
				// list, we should remove it
				if (orgDBContacts != null && orgDBContacts.size() > 0) {
					Iterator<AmpOrganisationContact> iter = orgDBContacts
							.iterator();
					while (iter.hasNext()) {
						AmpOrganisationContact dbOrgContact = iter.next();
						int count = 0;
						if (organisationContacts != null) {
							for (AmpOrganisationContact formOrgCont : organisationContacts) {
								if (formOrgCont.getId() != null
										&& formOrgCont.getId().equals(
												dbOrgContact.getId())) {
									count++;
									break;
								}
							}
						}
						if (count == 0) { // if organization contains
											// contact,which is not in contact
											// list, we should remove it
							AmpOrganisationContact orgCont = (AmpOrganisationContact) sess
									.get(AmpOrganisationContact.class,
											dbOrgContact.getId());
							AmpContact cont = orgCont.getContact();
							sess.delete(orgCont);
							cont.getOrganizationContacts().remove(orgCont);
							sess.update(cont);
							org.getOrganizationContacts().remove(orgCont);
						}
					}
				}
			}

			if (organisationContacts != null) {
				// this will remove all organisation contact which are linked to
				// this organisation
				for (Iterator iterator = organisationContacts.iterator(); iterator
						.hasNext();) {
					AmpOrganisationContact ampOrganisationContact = (AmpOrganisationContact) iterator
							.next();
					if (org.getAmpOrgId() != null) {
						if (ampOrganisationContact.getId() != null) {
							// AmpContact
							// cont=ampOrganisationContact.getContact();
							AmpOrganisationContact contToBeRemoved = (AmpOrganisationContact) sess
									.get(AmpOrganisationContact.class,
											ampOrganisationContact.getId());
							if (contToBeRemoved != null) {
								AmpContact ampContact = contToBeRemoved
										.getContact();
								sess.delete(contToBeRemoved);
								ampContact.getOrganizationContacts().remove(
										contToBeRemoved);
								sess.update(ampContact);
								org.getOrganizationContacts().remove(
										contToBeRemoved);
							}

						}
					}
				}

				// now re-save all organisation contacts
				for (AmpOrganisationContact organizationContact : organisationContacts) {
					// save or update contact
					AmpContact contact = organizationContact.getContact();
					AmpContact ampContact = null;
					if (contact.getId() != null) { // contact already exists.
						ampContact = (AmpContact) sess.get(AmpContact.class,
								contact.getId());
						ampContact.setName(contact.getName());
						ampContact.setLastname(contact.getLastname());
						ampContact.setTitle(contact.getTitle());
						ampContact.setOrganisationName(contact
								.getOrganisationName());
						ampContact.setCreator(contact.getCreator());
						ampContact.setShared(true);
						ampContact.setOfficeaddress(contact.getOfficeaddress());
						ampContact.setFunction(contact.getFunction());
						// remove old properties
						if (ampContact.getProperties() != null) {
							ampContact.getProperties().clear();
						}
						sess.update(ampContact);
					} else {
						sess.save(contact);
					}

					// save properties
					if (contact.getProperties() != null) {
						for (AmpContactProperty formProperty : contact
								.getProperties()) {
							if (ampContact != null) {
								formProperty.setContact(ampContact);
							} else {
								formProperty.setContact(contact);
							}
							sess.save(formProperty);
						}
					}

					// link org to cont
					AmpOrganisationContact newOrgCont = new AmpOrganisationContact();
					organizationContact.setOrganisation(org);
					organizationContact.setPrimaryContact(organizationContact
							.getPrimaryContact());
					if (ampContact != null) {
						organizationContact.setContact(ampContact);
					} else {
						organizationContact.setContact(contact);
					}
					sess.save(organizationContact);

				}
			}

			sess.saveOrUpdate(org);
			// session.flush();
			// tx.commit();
		} catch (Exception e) {
			logger.error("Unable to update", e);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (HibernateException ex) {
					logger.error("rollback() failed", ex);
				}
			}
			throw new DgException(e);
		}
	}

	public static void delete(Object object) throws JDBCException {
		Session sess = null;
		Transaction tx = null;

		try {
			sess = PersistenceManager.getRequestDBSession();
			// beginTransaction();
			logger.debug("before delete");
			sess.delete(object);
			// session.flush();
			// tx.commit();
		} catch (Exception e) {
			if (e instanceof JDBCException)
				throw (JDBCException) e;
			logger.error("Exception " + e.toString());
			try {
				tx.rollback();
			} catch (HibernateException ex) {
				logger.error("rollback() failed");
				logger.error(ex.toString());
			}
		}
	}

	public static void deleteOrg(AmpOrganisation org) throws JDBCException {
		Session sess = null;
		Transaction tx = null;

		try {
			sess = PersistenceManager.getRequestDBSession();
			tx = sess.beginTransaction();
			logger.debug("before delete");

			/*
			 * Set<AmpOrganisationContact> orgConts =
			 * org.getOrganizationContacts(); if(orgConts != null){ for
			 * (AmpOrganisationContact ampOrganisationContact : orgConts) {
			 * ampOrganisationContact
			 * .getContact().getOrganizationContacts().remove
			 * (ampOrganisationContact); sess.delete(ampOrganisationContact); }
			 * org.getOrganizationContacts().clear(); }
			 */
			org.setDeleted(true);
			sess.saveOrUpdate(org);
			// sess.flush();
			tx.commit();
		} catch (Exception e) {
			if (e instanceof JDBCException)
				throw (JDBCException) e;
			logger.error("Exception " + e.toString());
			try {
				tx.rollback();
			} catch (HibernateException ex) {
				logger.error("rollback() failed");
				logger.error(ex.toString());
			}
		}
	}

	public static void deleteAllStamps(String idxName) {
		Connection con;
		try {
			con = PersistenceManager.getSession().connection();
			con.setAutoCommit(false);
			con.createStatement().execute(
					"DELETE FROM amp_lucene_index WHERE idxName like '"
							+ idxName + "'");
			con.commit();
		} catch (Exception e) {
			logger.error("Error while trying to delete Lucene db stamps: ", e);
		}
	}

	public static void deleteStatus(Long id) {
		AmpStatus oldStatusItem = new AmpStatus();
		Session sess = null;
		Transaction tx = null;

		try {
			sess = PersistenceManager.getRequestDBSession();
			// beginTransaction();
			logger.debug("BEFORE SESS.SAVE()");
			oldStatusItem = (AmpStatus) sess.load(AmpStatus.class, id);

			if (sess != null) {
				logger.debug("DbUtil session is not null");

				logger.debug("DbUtil deleteStatus id : "
						+ oldStatusItem.getAmpStatusId());

				sess.delete(oldStatusItem);

				logger.debug("AFTER SESS.SAVE()");

				// tx.commit();
			} else
				logger.debug("DbUtil session is null");
		} catch (Exception ex) {
			logger.error("Unable to Delete Amp status record", ex);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (HibernateException ex1) {
					logger.error("rollback() failed ", ex1);
				}
			}
		}

	}

	public static Collection getQuarters(Long ampFundingId,
			Integer transactionType, Integer adjustmentType, Integer fiscalYear) {
		logger.debug("getQuarters() with ampFundingId="
				+ ampFundingId.longValue() + " fiscalYear=" + fiscalYear);

		Session session = null;
		Query q = null;
		Collection c = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = new String();
			queryString = "select f.fiscalQuarter from "
					+ AmpFundingDetail.class.getName()
					+ " f where (f.ampFundingId=:ampFundingId) "
					+ " and (f.transactionType=:transactionType) "
					+ " and (f.adjustmentType=:adjustmentType) "
					+ " and (f.fiscalYear=:fiscalYear) "
					+ " group by f.fiscalQuarter";

			q = session.createQuery(queryString);
			q.setParameter("ampFundingId", ampFundingId, Hibernate.LONG);
			q.setParameter("transactionType", transactionType,
					Hibernate.INTEGER);
			q.setParameter("adjustmentType", adjustmentType, Hibernate.INTEGER);
			q.setParameter("fiscalYear", fiscalYear, Hibernate.INTEGER);
			c = q.list();
			logger.debug("No of Quarters : " + q.list().size());
		} catch (Exception ex) {
			logger.error("Unable to get  Quarters from database", ex);
		}

		logger.debug("getQuarters() collection size returned : "
				+ (c != null ? c.size() : 0));
		return c;
	}

	/**
	 * 
	 * @param fiscalCalId
	 * @return
	 */
	public static AmpFiscalCalendar getFiscalCalendar(Long fiscalCalId) {
		logger.debug("getFiscalCalendar with fiscalCalId" + fiscalCalId);
		Session session = null;
		Query q = null;
		Collection collection = null;
		Iterator iter = null;
		AmpFiscalCalendar fc = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = new String();
			queryString = "select f from " + AmpFiscalCalendar.class.getName()
					+ " f where (f.ampFiscalCalId=:fiscalCalId) ";
			q = session.createQuery(queryString);
			q.setParameter("fiscalCalId", fiscalCalId, Hibernate.LONG);
			collection = q.list();
			if (collection.size() > 0) {
				fc = (AmpFiscalCalendar) collection.toArray()[0];
			}
		} catch (Exception ex) {
			logger.error("Unable to get fiscal calendar from database", ex);
		}
		return fc;
	}

	public static Collection getMaxFiscalYears() {
		Session session = null;
		Query q = null;
		Collection c = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select Max(f.fiscalYear), Min(f.fiscalYear) from "
					+ AmpFundingDetail.class.getName() + " f ";

			q = session.createQuery(queryString);

			c = q.list();

		} catch (Exception ex) {
			logger.error("Unable to get  Max fiscal years from database", ex);
		}
		return c;
	}

	public static List getAmpAssistanceType(Long ampActivityId) {
		Session session = null;
		Query q = null;
		List c = new ArrayList();

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = new String();
			queryString = "select distinct f.ampTermsAssistId.termsAssistName from "
					+ AmpFunding.class.getName()
					+ " f where f.ampActivityId=:ampActivityId";

			q = session.createQuery(queryString);
			q.setParameter("ampActivityId", ampActivityId, Hibernate.LONG);
			int i = 0;
			Iterator iter = q.list().iterator();
			while (iter.hasNext()) {
				Assistance assistance = new Assistance();
				logger.debug("Assistance type: " + q.list().get(i));
				if (q.list().get(i) != null) {
					String code = (String) iter.next();
					assistance.setAssistanceType(code);
					i++;
				}
				c.add(assistance);
			}
		} catch (Exception ex) {
			logger.error("Unable to get  Max fiscal years from database", ex);
		}
		return c;
	}

	public static List getAmpModalityNames(Long ampActivityId) {
		Session session = null;
		Query q = null;
		List c = new ArrayList();

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = new String();
			queryString = "select distinct f.modalityId.name from "
					+ AmpFunding.class.getName()
					+ " f where f.ampActivityId=:ampActivityId";

			q = session.createQuery(queryString);
			q.setParameter("ampActivityId", ampActivityId, Hibernate.LONG);

			Iterator iter = q.list().iterator();
			while (iter.hasNext()) {
				String modalityName = (String) iter.next();
				c.add(modalityName);

			}
		} catch (Exception ex) {
			logger.error(ex);
		}

		return c;
	}

	/*
	 * 
	 * public static Date getClosingDate(Long ampFundingId, Integer type) {
	 * 
	 * logger.debug("getClosingDate() with ampFundingId=" + ampFundingId +
	 * " type=" + type);
	 * 
	 * Session session = null; Query q = null; Collection c = null; Iterator
	 * iter = null; Date d = null;
	 * 
	 * try { session = PersistenceManager.getRequestDBSession(); String
	 * queryString = new String(); queryString = "select a.closingDate from " +
	 * AmpClosingDateHistory.class.getName() +
	 * " a where (a.ampFundingId=:ampFundingId) " + " and (a.type=:type)"; q =
	 * session.createQuery(queryString); q.setParameter("ampFundingId",
	 * ampFundingId, Hibernate.LONG); q.setParameter("type", type,
	 * Hibernate.INTEGER); c = q.list(); if (c.size() != 0) { iter =
	 * c.iterator(); if (iter.hasNext()) { d = (Date) iter.next(); } } } catch
	 * (Exception ex) { logger.error("Unable to get closing date from database",
	 * ex); } logger.debug("getClosingDate() returning date:" + d); return d; }
	 */

	public static String getGoeId(Long ampActivityId) {

		logger.debug("getGoeId() with ampActivityId=" + ampActivityId);
		Long ampDonorOrgId = Constants.MOFED_ORG_ID;
		Session session = null;
		Query q = null;
		Collection c = null;
		Iterator iter = null;
		String s = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = new String();
			queryString = "select a.internalId from "
					+ AmpActivityInternalId.class.getName()
					+ " a inner join a.ampActivity ampAct"
					+ " inner join a.organisation ampOrg "
					+ " where (ampAct.ampActivityId=:ampActivityId) "
					+ " and (ampOrg.ampOrgId=:ampDonorOrgId)";

			q = session.createQuery(queryString);
			q.setParameter("ampActivityId", ampActivityId, Hibernate.LONG);
			q.setParameter("ampDonorOrgId", ampDonorOrgId, Hibernate.LONG);
			if (q.list() != null) {
				iter = q.list().iterator();
				if (iter.hasNext())
					s = (String) iter.next();
			} else
				s = "NA";
		} catch (Exception ex) {
			logger.error("Unable to get GOE ID from database", ex);
		}
		logger.debug("getGoeId() returning s:" + s);
		return s;
	}

	public static Collection getFundingIdforG(Long ampActivityId,
			Long ampDonorOrgId, Long ampTermsAssistId) {
		logger.debug(" Funding Term Code passed is " + ampTermsAssistId);
		Session session = null;
		Query q = null;
		Collection funding = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = new String();
			queryString = "select f from "
					+ AmpFunding.class.getName()
					+ " f where (f.ampDonorOrgId =:ampDonorOrgId) and (f.ampTermsAssistId =:ampTermsAssistId) and (f.ampActivityId =:ampActivityId)";
			logger.debug("querystring " + queryString);
			q = session.createQuery(queryString);
			q.setParameter("ampActivityId", ampActivityId, Hibernate.LONG);
			q.setParameter("ampDonorOrgId", ampDonorOrgId, Hibernate.LONG);
			q.setParameter("ampTermsAssistId", ampTermsAssistId, Hibernate.LONG);
			funding = q.list();
		} catch (Exception ex) {
			logger.error("Unable to get Funding records from database", ex);
		}
		logger.debug("Returning Funding Grant : "
				+ (funding != null ? funding.size() : 0));
		return funding;

	}

	public static ArrayList getAmpDonorsByFunding(Long ampTeamId) {
		Session session = null;
		Query q = null;
		ArrayList donors = new ArrayList();
		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = new String();
			queryString = "select f from " + AmpFunding.class.getName() + " f";
			q = session.createQuery(queryString);
			Iterator it = q.list().iterator();
			while (it.hasNext()) {
				AmpFunding el = (AmpFunding) it.next();
				if (el.getAmpActivityId().getTeam().getAmpTeamId()
						.equals(ampTeamId)) {
					AmpOrganisation org = el.getAmpDonorOrgId();
					if (donors.indexOf(org) == -1)
						donors.add(org);
				}
			}
		} catch (Exception ex) {
			logger.error("Unable to get Donors from database", ex);
		}
		return donors;
	}

	public static ArrayList getAmpDonors(Long ampTeamId) {
		ArrayList donor = new ArrayList();
		StringBuffer DNOrg = new StringBuffer();
		Session session = null;
		Query q = null;
		Iterator iterActivity = null;
		Iterator iter = null;
		String inClause = null;

		try {
			ArrayList dbReturnSet = (ArrayList) TeamUtil
					.getAmpLevel0TeamIds(ampTeamId);
			if (dbReturnSet.size() == 0)
				inClause = "'" + ampTeamId + "'";
			else {
				iter = dbReturnSet.iterator();
				while (iter.hasNext()) {
					Long teamId = (Long) iter.next();
					if (inClause == null)
						inClause = "'" + teamId + "'";
					else
						inClause = inClause + ",'" + teamId + "'";
				}
			}
			session = PersistenceManager.getRequestDBSession();
			String queryString = new String();
			queryString = "select activity from " + AmpActivity.class.getName()
					+ " activity where activity.team.ampTeamId in(" + inClause
					+ ")";
			q = session.createQuery(queryString);
			logger.debug("Activity List: " + q.list().size());
			iterActivity = q.list().iterator();
			while (iterActivity.hasNext()) {
				AmpActivity ampActivity = (AmpActivity) iterActivity.next();

				iter = ampActivity.getOrgrole().iterator();
				while (iter.hasNext()) {
					AmpOrgRole ampOrgRole = (AmpOrgRole) iter.next();
					if (ampOrgRole.getRole().getRoleCode()
							.equals(Constants.FUNDING_AGENCY)) {
						if (donor.indexOf(ampOrgRole.getOrganisation()) == -1)
							donor.add(ampOrgRole.getOrganisation());
					}
				}
			}
			logger.debug("Donors: " + donor.size());
			int n = donor.size();
			for (int i = 0; i < n - 1; i++) {
				for (int j = 0; j < n - 1 - i; j++) {
					AmpOrganisation firstOrg = (AmpOrganisation) donor.get(j);
					AmpOrganisation secondOrg = (AmpOrganisation) donor
							.get(j + 1);
					if (firstOrg.getAcronym().compareToIgnoreCase(
							secondOrg.getAcronym()) > 0) {
						AmpOrganisation tempOrg = firstOrg;
						donor.set(j, secondOrg);
						donor.set(j + 1, tempOrg);
					}
				}
			}

		} catch (Exception ex) {
			logger.error("Unable to get Donor ", ex);
		}
		return donor;
	}

	public static Collection getDonorFund1(Long ampFundingId,
			Integer transactionType, Integer adjustmentType) {
		logger.debug("getTotalDonorFund() with ampFundingId " + ampFundingId
				+ " transactionType " + transactionType + " adjustmentType "
				+ adjustmentType);
		Session session = null;
		Query q = null;
		List list = null;
		Iterator iter = null;
		double total = 0.0;
		Collection ampFundings = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = queryString = "select f from "
					+ AmpFundingDetail.class.getName()
					+ " f where (f.ampFundingId=:ampFundingId) "
					+ " and (f.transactionType=:transactionType) "
					+ " and (f.adjustmentType=:adjustmentType) ";

			q = session.createQuery(queryString);
			q.setParameter("ampFundingId", ampFundingId, Hibernate.LONG);
			q.setParameter("transactionType", transactionType,
					Hibernate.INTEGER);
			q.setParameter("adjustmentType", adjustmentType, Hibernate.INTEGER);
			ampFundings = q.list();
			logger.debug("size of result " + ampFundings.size());
		} catch (Exception ex) {
			logger.error("Unable to get sum of funds from database", ex);
		}
		return ampFundings;
	}

	/**
	 * 
	 * @param ampActivityId
	 * @param transactionType
	 * @param adjustmentType
	 * @param ampCurrencyCode
	 * @return
	 */
	public static DecimalWraper getAmpFundingAmount(Long ampActivityId,
			Integer transactionType, Integer adjustmentType,
			String ampCurrencyCode) {
		Session session = null;
		Query q = null;
		Iterator iter = null;
		String inClause = null;
		double fromCurrency = 0.0;
		double toCurrency = 1.0;
		DecimalWraper amount = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = queryString = "select f from "
					+ AmpFunding.class.getName()
					+ " f where (f.ampActivityId=:ampActivityId) ";
			q = session.createQuery(queryString);
			q.setParameter("ampActivityId", ampActivityId, Hibernate.LONG);
			iter = q.list().iterator();
			while (iter.hasNext()) {
				AmpFunding ampFunding = (AmpFunding) iter.next();
				if (inClause == null)
					inClause = "'" + ampFunding.getAmpFundingId() + "'";
				else
					inClause = inClause + ",'" + ampFunding.getAmpFundingId()
							+ "'";
			}
			logger.debug(" transactionType " + transactionType
					+ " adjustmentType " + adjustmentType + " ampCurrencyCode"
					+ ampCurrencyCode);
			queryString = queryString = "select fd from "
					+ AmpFundingDetail.class.getName()
					+ " fd where (fd.transactionType=:transactionType) "
					+ " and (fd.adjustmentType=:adjustmentType) "
					+ " and (fd.ampFundingId.ampFundingId in(" + inClause
					+ "))";
			logger.debug("queryString :" + queryString);
			q = session.createQuery(queryString);
			q.setParameter("transactionType", transactionType,
					Hibernate.INTEGER);
			q.setParameter("adjustmentType", adjustmentType, Hibernate.INTEGER);
			iter = q.list().iterator();
			logger.debug("Size: " + q.list().size());
			amount = new DecimalWraper();
			while (iter.hasNext()) {
				AmpFundingDetail ampFundingDetail = (AmpFundingDetail) iter
						.next();
				Double fixedRateToUSD = ampFundingDetail.getFixedExchangeRate();
				if (fixedRateToUSD != null && fixedRateToUSD.doubleValue() != 1) {
					fromCurrency = fixedRateToUSD.doubleValue();

					toCurrency = Util.getExchange(ampCurrencyCode,
							new java.sql.Date(ampFundingDetail
									.getTransactionDate().getTime()));

					DecimalWraper tmpamount = CurrencyWorker.convertWrapper(
							ampFundingDetail.getTransactionAmount()
									.doubleValue(), fromCurrency, toCurrency,
							new java.sql.Date(ampFundingDetail
									.getTransactionDate().getTime()));
					if (amount.getValue() != null) {
						amount.setValue(amount.getValue().add(
								tmpamount.getValue()));
						amount.setCalculations(amount.getCalculations() + " +"
								+ tmpamount.getCalculations() + "<BR>");
					} else {
						amount.setValue(tmpamount.getValue());
						amount.setCalculations(tmpamount.getCalculations());
					}
				} else {
					toCurrency = Util.getExchange(ampCurrencyCode,
							new java.sql.Date(ampFundingDetail
									.getTransactionDate().getTime()));

					fromCurrency = Util.getExchange(ampFundingDetail
							.getAmpCurrencyId().getCurrencyCode(),
							new java.sql.Date(ampFundingDetail
									.getTransactionDate().getTime()));

					logger.debug("to Currency: " + toCurrency);
					logger.debug("From Currency: " + fromCurrency);

					DecimalWraper tmpamount = CurrencyWorker.convertWrapper(
							ampFundingDetail.getTransactionAmount()
									.doubleValue(), fromCurrency, toCurrency,
							new java.sql.Date(ampFundingDetail
									.getTransactionDate().getTime()));

					if (amount.getValue() != null) {
						amount.setCalculations(amount.getCalculations() + " + "
								+ tmpamount.getCalculations());
						BigDecimal tmp = amount.getValue().add(
								tmpamount.getValue());
						amount.setValue(tmp);
					} else {
						amount.setCalculations(tmpamount.getCalculations());
						amount.setValue(tmpamount.getValue());
					}
				}

			}
			logger.debug("Amount: " + amount);

		} catch (Exception ex) {
			logger.error("Unable to get sum of funds from database", ex);
		}

		return amount;
	}

	/**
	 * Given a page code like 'DTP' for Desktop,'FP' - Financial Progress
	 * returns the page id
	 * 
	 * @param pageCode
	 * @return page id
	 */
	public static Long getPageId(String pageCode) {

		Session session = null;
		Query q = null;
		Collection c = null;
		Long id = null;
		Iterator iter = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = new String();
			queryString = "select a.ampPageId from " + AmpPages.class.getName()
					+ " a where (a.pageCode=:pageCode) ";
			q = session.createQuery(queryString);
			q.setParameter("pageCode", pageCode, Hibernate.STRING);
			c = q.list();
			if (c.size() != 0) {
				iter = c.iterator();
				if (iter.hasNext()) {
					id = (Long) iter.next();
				}
			} else {
				if (logger.isDebugEnabled())
					logger.debug("No page with corresponding name");
			}
		} catch (Exception ex) {
			if (logger.isDebugEnabled())
				logger.error("Unable to get page id  from database", ex);
		}
		if (logger.isDebugEnabled())
			logger.debug("getPageId() returning page id:" + id);
		return id;
	}

	public static Long getAmpPageId(String pageName) {

		Session session = null;
		Query q = null;
		Collection c = null;
		Long id = null;
		Iterator iter = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = new String();
			queryString = "select a.ampPageId from " + AmpPages.class.getName()
					+ " a where (a.pageName=:pageName) ";
			q = session.createQuery(queryString);
			q.setParameter("pageName", pageName, Hibernate.STRING);
			c = q.list();
			if (c.size() != 0) {
				iter = c.iterator();
				if (iter.hasNext()) {
					id = (Long) iter.next();
				}
			} else {
				if (logger.isDebugEnabled())
					logger.debug("No page with corresponding name");
			}
		} catch (Exception ex) {
			if (logger.isDebugEnabled())
				logger.error("Unable to get page id  from database", ex);
		}
		if (logger.isDebugEnabled())
			logger.debug("getPageId() returning page id:" + id);
		return id;
	}

	public static Collection getOrgId(Long ampSecSchemeId) {
		Session session = null;
		Query q = null;
		Collection ampOrgs = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = new String();
			queryString = "select o from "
					+ AmpOrganisation.class.getName()
					+ " o where (o.ampSecSchemeId=:ampSecSchemeId ) and (o.deleted is null or o.deleted = false) ";
			q = session.createQuery(queryString);
			q.setParameter("ampSecSchemeId", ampSecSchemeId, Hibernate.LONG);

			ampOrgs = q.list();
			logger.debug("DbUtil : getOrgId() returning collection of size  "
					+ ampOrgs.size());

		} catch (Exception ex) {
			logger.error("Unable to get AmpFunding collection from database",
					ex);
		}

		return ampOrgs;
	}

	// ----------------

	public static ArrayList getAmpStatusList() {
		Session session = null;
		Query q = null;
		AmpStatus ampStatus = null;
		ArrayList ampStatusList = new ArrayList();
		String queryString = null;
		Iterator iter = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			queryString = " select s from " + AmpStatus.class.getName() + " s ";
			q = session.createQuery(queryString);
			iter = q.list().iterator();

			while (iter.hasNext()) {
				ampStatus = (AmpStatus) iter.next();
				logger.debug("Amp Status Id :"
						+ (Long) ampStatus.getAmpStatusId());
				logger.debug("Amp Status Code :"
						+ (String) ampStatus.getStatusCode());
				logger.debug("Amp Status Name :" + (String) ampStatus.getName());
				ampStatusList.add(ampStatus);
			}

		} catch (Exception ex) {
			logger.debug("Unable to get Amp Status records  from database "
					+ ex.getMessage());
		}
		return ampStatusList;
	}

	public static AmpStatus getAmpStatus(Long id) {
		AmpStatus statusItem = new AmpStatus();
		Session session = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			statusItem = (AmpStatus) session.load(AmpStatus.class, id);
		} catch (Exception ex) {
			logger.error("DbUtil:getAmpStatus: Unable to get Amp Status ", ex);
			// ////System.out.println(ex.toString()) ;
		}
		logger.debug("DbUtil: getAmpStatus(id) executed successfully ");
		return statusItem;
	}

	public static double getFundDetails(Long ampFundingId,
			Integer transactionType, Integer adjustmentType, Integer forcastYear) {
		logger.debug("inside fund details for yr " + forcastYear);
		Session session = null;
		Query q = null;
		Iterator iter = null;
		Double amount = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = new String();
			queryString = " select sum(fd.thousandsTransactionAmount) from "
					+ AmpFundingDetail.class.getName()
					+ " fd where (fd.ampFundingId = :ampFundingId ) and (fd.fiscalYear = :forcastYear) and (fd.transactionType=:transactionType) and (fd.adjustmentType=:adjustmentType) group by fd.fiscalYear ";
			q = session.createQuery(queryString);
			q.setParameter("ampFundingId", ampFundingId, Hibernate.LONG);
			q.setParameter("forcastYear", forcastYear, Hibernate.INTEGER);
			q.setParameter("transactionType", transactionType,
					Hibernate.INTEGER);
			q.setParameter("adjustmentType", adjustmentType, Hibernate.INTEGER);

			iter = q.list().iterator();
			while (iter.hasNext()) {
				amount = (Double) iter.next();
			}
		} catch (Exception ex) {
			logger.debug("Unable to get REcords names  from database "
					+ ex.getMessage());
		}
		if (amount == null) {
			logger.debug("RETURNING ZERO");
			return 0;
		} else {
			return amount.doubleValue();
		}
	}

	public static double getFundDetails(Long ampFundingId,
			Integer transactionType, Integer adjustmentType) {
		Session session = null;
		Query q = null;
		Iterator iter = null;
		Double amount = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = new String();
			queryString = " select sum(fd.thousandsTransactionAmount) from "
					+ AmpFundingDetail.class.getName()
					+ " fd where (fd.ampFundingId = :ampFundingId ) and (fd.transactionType=:transactionType) and (fd.adjustmentType=:adjustmentType) group by fd.ampFundingId ";
			q = session.createQuery(queryString);
			q.setParameter("ampFundingId", ampFundingId, Hibernate.LONG);
			q.setParameter("transactionType", transactionType,
					Hibernate.INTEGER);
			q.setParameter("adjustmentType", adjustmentType, Hibernate.INTEGER);

			iter = q.list().iterator();
			while (iter.hasNext()) {
				amount = (Double) iter.next();
			}

		} catch (Exception ex) {
			logger.debug("Unable to get REcords names  from database "
					+ ex.getMessage());
		}
		if (amount == null) {
			logger.debug("RETURNING ZERO");
			return 0;
		} else {
			return amount.doubleValue();
		}
	}

	public static ArrayList getProjects() {
		Session session = null;
		Query q = null;
		AmpActivity ampActivity = new AmpActivity();
		ArrayList project = new ArrayList();
		List list = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = new String();

			queryString = " select a from " + AmpActivity.class.getName()
					+ " a ";
			// logger.debug("querystring " + queryString);
			q = session.createQuery(queryString);
			Iterator iter = q.list().iterator();

			while (iter.hasNext()) {
				ampActivity = (AmpActivity) iter.next();
				project.add(ampActivity);
			}

		} catch (Exception ex) {

			logger.debug("Projects : Unable to get Amp Activity names from database "
					+ ex.getMessage());

		}
		return project;
	}

	public static double getDonorFund(Long ampFundingId,
			Integer transactionType, Integer adjustmentType) {
		logger.debug("getTotalDonorFund() with ampFundingId " + ampFundingId
				+ " transactionType " + transactionType + " adjustmentType "
				+ adjustmentType);
		Session session = null;
		Query q = null;
		List list = null;
		Iterator iter = null;
		double total = 0.0;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = new String();

			queryString = "select f from " + AmpFundingDetail.class.getName()
					+ " f where (f.ampFundingId=:ampFundingId) "
					+ " and (f.transactionType=:transactionType) "
					+ " and (f.adjustmentType=:adjustmentType) ";

			q = session.createQuery(queryString);
			q.setParameter("ampFundingId", ampFundingId, Hibernate.LONG);
			q.setParameter("transactionType", transactionType,
					Hibernate.INTEGER);
			q.setParameter("adjustmentType", adjustmentType, Hibernate.INTEGER);
			list = q.list();
			logger.debug("size of result " + list.size());
			iter = list.iterator();
			while (iter.hasNext()) {
				AmpFundingDetail fundDetails = new AmpFundingDetail();
				fundDetails = (AmpFundingDetail) iter.next();
				if (fundDetails.getAmpCurrencyId().getCurrencyCode()
						.equals("USD")) { // logger.debug("equals USD");
					total = total
							+ fundDetails.getTransactionAmount().doubleValue();
				} else { // logger.debug(" not equal to USD ") ;
					double fromCurrency = Util.getExchange(fundDetails
							.getAmpCurrencyId().getCurrencyCode(),
							new java.sql.Date(fundDetails.getTransactionDate()
									.getTime()));
					double toCurrency = CurrencyUtil.getExchangeRate("USD");
					// total = total +
					// CurrencyWorker.convert(fundDetails.getTransactionAmount().doubleValue(),"USD")
					// ;
					total = total
							+ CurrencyWorker.convert1(fundDetails
									.getTransactionAmount().doubleValue(),
									fromCurrency, toCurrency);
					// logger.debug("AFTER conversion total is " + total);
				}

			}
		} catch (Exception ex) {
			logger.debug("Unable to get sum of funds from database", ex);
		}
		return total;
	}

	public static double getDonorFundbyFiscalYear(Long ampFundingId,
			Integer transactionType, Integer adjustmentType, Integer fiscalYear) {
		Session session = null;
		Query q = null;
		List list = null;
		Iterator iter = null;
		double total = 0.0;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = new String();

			queryString = "select f from " + AmpFundingDetail.class.getName()
					+ " f where (f.ampFundingId=:ampFundingId) "
					+ " and (f.transactionType=:transactionType) "
					+ " and (f.adjustmentType=:adjustmentType) "
					+ " and (f.fiscalYear=:fiscalYear) ";

			q = session.createQuery(queryString);
			q.setParameter("ampFundingId", ampFundingId, Hibernate.LONG);
			q.setParameter("transactionType", transactionType,
					Hibernate.INTEGER);
			q.setParameter("adjustmentType", adjustmentType, Hibernate.INTEGER);
			q.setParameter("fiscalYear", fiscalYear, Hibernate.INTEGER);
			list = q.list();

			iter = list.iterator();
			logger.debug("size of resultset " + q.list().size());

			while (iter.hasNext()) {
				AmpFundingDetail fundDetails = new AmpFundingDetail();
				fundDetails = (AmpFundingDetail) iter.next();

				double fromCurrency = CurrencyUtil.getExchangeRate(fundDetails
						.getAmpCurrencyId().getCurrencyCode());
				double toCurrency = CurrencyUtil.getExchangeRate("USD");

				if (fundDetails.getAmpCurrencyId().getCurrencyCode()
						.equals("USD")) {
					total = total
							+ fundDetails.getTransactionAmount().doubleValue();
					logger.debug("if total " + total);
				} else {
					total = total
							+ CurrencyWorker.convert1(fundDetails
									.getTransactionAmount().doubleValue(),
									fromCurrency, toCurrency);
					logger.debug(" else total " + total);
				}

			}
			logger.debug("Total K " + total);
		} catch (Exception ex) {
			logger.debug("Unable to get sum of funds from database", ex);
		}
		return total;
	}

	public static double getDonorFundbyFiscalYear(Long ampFundingId,
			Integer transactionType, Integer adjustmentType,
			Integer fiscalYear, Integer fiscalQuarter) {
		/*
		 * logger.debug("getTotalDonorFundbyFiscalYear() with ampFundingId " +
		 * ampFundingId + " transactionType " + transactionType + "
		 * adjustmentType " + adjustmentType + " perspective " + perspective + "
		 * fiscal year " + fiscalYear + " quarter " + fiscalQuarter) ;
		 */
		Session session = null;
		Query q = null;
		List list = null;
		Iterator iter = null;
		double total = 0.0;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = new String();

			queryString = "select f from " + AmpFundingDetail.class.getName()
					+ " f where (f.ampFundingId=:ampFundingId) "
					+ " and (f.transactionType=:transactionType) "
					+ " and (f.adjustmentType=:adjustmentType) "
					+ " and (f.fiscalYear=:fiscalYear) "
					+ " and (f.fiscalQuarter=:fiscalQuarter) ";
			q = session.createQuery(queryString);
			q.setParameter("ampFundingId", ampFundingId, Hibernate.LONG);
			q.setParameter("transactionType", transactionType,
					Hibernate.INTEGER);
			q.setParameter("adjustmentType", adjustmentType, Hibernate.INTEGER);
			q.setParameter("fiscalYear", fiscalYear, Hibernate.INTEGER);
			q.setParameter("fiscalQuarter", fiscalQuarter, Hibernate.INTEGER);
			list = q.list();

			iter = list.iterator();
			while (iter.hasNext()) {
				AmpFundingDetail fundDetails = new AmpFundingDetail();
				fundDetails = (AmpFundingDetail) iter.next();
				if (fundDetails.getAmpCurrencyId().getCurrencyCode()
						.equals("USD")) {
					total = total
							+ fundDetails.getTransactionAmount().doubleValue();
				} else {
					total = total
							+ CurrencyWorker.convert(fundDetails
									.getTransactionAmount().doubleValue(),
									"USD");

				}

			}

		} catch (Exception ex) {
			logger.error("Unable to get sum of funds from database", ex);
		}
		return total;
	}

	public static Collection<AmpOrganisation> getDonors() {
		Session session = null;
		Query q = null;
		Collection<Object[]> donors = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = new String();
			queryString = "select distinct f.ampDonorOrgId, f.ampDonorOrgId.name  from "
					+ AmpFunding.class.getName()
					+ " f  order by f.ampDonorOrgId.name";
			q = session.createQuery(queryString);
			logger.debug("No of Donors : " + q.list().size());
			donors = q.list();
		} catch (Exception ex) {
			logger.error("Unable to get Donors from database", ex);
		}
		Collection<AmpOrganisation> retVal = null;
		if (donors != null) {
			retVal = new ArrayList<AmpOrganisation>();
			for (Object[] org : donors) {
				retVal.add((AmpOrganisation) org[0]);
			}
		}
		return retVal;
	}

	public static Collection getOrganisations() {
		Session session = null;
		Query q = null;
		Collection donors = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = new String();
			queryString = "select distinct org from "
					+ AmpOrganisation.class.getName()
					+ " org  join  org.calendar  cal where (org.deleted is null or org.deleted = false) ";
			q = session.createQuery(queryString);
			donors = q.list();
		} catch (Exception ex) {
			logger.error("Unable to get Donors from database", ex);
		}
		return donors;

	}

	public static Collection getFundingIdbyDonor(Long ampDonorOrgId) {
		Session session = null;
		Query q = null;
		Collection fundingIds = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = new String();
			queryString = "select f from " + AmpFunding.class.getName()
					+ " f where (f.ampDonorOrgId=:ampDonorOrgId)";
			q = session.createQuery(queryString);
			q.setParameter("ampDonorOrgId", ampDonorOrgId, Hibernate.LONG);
			fundingIds = q.list();
		} catch (Exception ex) {
			logger.error("Unable to get Donors from database", ex);
		}
		logger.debug("Returning fundingIDs : "
				+ (fundingIds != null ? fundingIds.size() : 0));
		return fundingIds;

	}

	public static Collection getProjectsbyModality(Long ampModalityId) {
		// logger.debug("Modality Id : " + ampModalityId);
		Session session = null;
		Query q = null;
		Collection projects = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = new String();
			queryString = " select activity from "
					+ AmpActivity.class.getName()
					+ " activity where activity.modality.ampModalityId = :ampModalityId";
			q = session.createQuery(queryString);
			q.setParameter("ampModalityId", ampModalityId, Hibernate.LONG);
			projects = q.list();
		} catch (Exception ex) {
			logger.error("Unable to get Donors from database", ex);
		}
		logger.debug("Returning Projects : "
				+ (projects != null ? projects.size() : 0));
		return projects;

	}

	public static Collection getDonorAgencies() {
		Session session = null;
		Query q = null;
		Collection donorGroups = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = new String();
			queryString = "select distinct o.orgType from "
					+ AmpOrganisation.class.getName()
					+ " o where (o.deleted is null or o.deleted = false) ";
			q = session.createQuery(queryString);
			logger.debug("No of Donors Agencies : " + q.list().size());
			donorGroups = q.list();
		} catch (Exception ex) {
			logger.debug("Unable to get Donor Agencies from database", ex);
		}
		return donorGroups;

	}

	public static Collection getDonorbyAgency(String orgType) {
		logger.debug(" Donor Agency name passed is " + orgType);
		Session session = null;
		Query q = null;
		Collection donorGroups = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = new String();
			queryString = "select o from "
					+ AmpOrganisation.class.getName()
					+ " o where (o.orgType =:orgType) and (o.deleted is null or o.deleted = false) ";
			q = session.createQuery(queryString);
			q.setParameter("orgType", orgType, Hibernate.STRING);
			// logger.debug("No of Org records : " + q.list().size());
			donorGroups = q.list();
		} catch (Exception ex) {
			logger.debug("Unable to get Organisation records from database", ex);
		}
		return donorGroups;

	}

	public static Collection getFundingIdbyOrgId(Long ampDonorOrgId,
			String fundingTermsCode) {
		logger.debug(" Funding Term Assit Id passed is " + fundingTermsCode);
		Session session = null;
		Query q = null;
		Collection funding = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = new String();
			queryString = "select f from "
					+ AmpFunding.class.getName()
					+ " f where (f.ampDonorOrgId =:ampDonorOrgId) and (f.fundingTermsCode =:fundingTermsCode ) ";
			logger.debug("querystring " + queryString);
			q = session.createQuery(queryString);
			q.setParameter("ampDonorOrgId", ampDonorOrgId, Hibernate.LONG);
			q.setParameter("fundingTermsCode", fundingTermsCode,
					Hibernate.STRING);
			logger.debug("No of funding records : " + q.list().size());
			funding = q.list();
		} catch (Exception ex) {
			logger.debug("Unable to get Funding records from database", ex);
		}
		logger.debug("Returning Funding Loan : "
				+ (funding != null ? funding.size() : 0));
		return funding;

	}

	public static Collection getFundingIdforL(Long ampDonorOrgId,
			Long ampTermsAssistId) {
		logger.debug(" Funding Term Code passed is " + ampTermsAssistId);
		Session session = null;
		Query q = null;
		Collection funding = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = new String();
			// queryString = "select f from " + AmpFunding.class.getName() + " f
			// , " + AmpTermsAssist.class.getName() + " t where (f.ampDonorOrgId
			// =:ampDonorOrgId) and (t.termsAssistName = termsAssistName ) ";
			queryString = "select f from "
					+ AmpFunding.class.getName()
					+ " f where (f.ampDonorOrgId =:ampDonorOrgId) and (f.ampTermsAssistId =:ampTermsAssistId)";
			q = session.createQuery(queryString);
			q.setParameter("ampDonorOrgId", ampDonorOrgId, Hibernate.LONG);
			q.setParameter("ampTermsAssistId", ampTermsAssistId, Hibernate.LONG);
			logger.debug("querystring " + queryString);
			funding = q.list();
		} catch (Exception ex) {
			logger.debug("Unable to get Funding records from database", ex);
		}
		logger.debug("Returning Funding L : "
				+ (funding != null ? funding.size() : 0));
		return funding;

	}

	public static Collection getFiscalYears(Long ampFundingId,
			String orgRoleCode) {
		Session session = null;
		Query q = null;
		Collection c = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = new String();
			queryString = "select distinct f.fiscalYear from "
					+ AmpFundingDetail.class.getName()
					+ " f where (f.ampFundingId=:ampFundingId) "
					+ " and (f.orgRoleCode=:orgRoleCode) ";

			q = session.createQuery(queryString);
			q.setParameter("ampFundingId", ampFundingId, Hibernate.LONG);
			q.setParameter("orgRoleCode", orgRoleCode, Hibernate.STRING);

			c = q.list();
		} catch (Exception ex) {
			logger.debug("Unable to get  fiscal years from database", ex);
		}
		logger.debug("getFiscalYears() collection size returned : "
				+ (c != null ? c.size() : 0));
		return c;
	}

	public static Collection getFundingId(Long ampActivityId, Long ampDonorOrgId) {
		Session session = null;
		Query q = null;
		Collection ampFundings = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = new String();
			queryString = "select f from "
					+ AmpFunding.class.getName()
					+ " f where (f.ampActivityId=:ampActivityId ) and (f.ampDonorOrgId=:ampDonorOrgId)";
			q = session.createQuery(queryString);
			q.setParameter("ampActivityId", ampActivityId, Hibernate.LONG);
			q.setParameter("ampDonorOrgId", ampDonorOrgId, Hibernate.LONG);
			ampFundings = q.list();
			logger.debug("DbUtil : getFundingId() returning collection of size  "
					+ ampFundings.size());

		} catch (Exception ex) {
			logger.error("Unable to get AmpFunding collection from database",
					ex);
		}

		return ampFundings;
	}

	public static Collection getActivityId(Long ampOrgId) {
		Session session = null;
		Query q = null;
		ArrayList list = new ArrayList();
		String queryString = null;
		Iterator iter = null;
		Collection act1 = new ArrayList();

		try {
			session = PersistenceManager.getRequestDBSession();
			queryString = "select activity from " + AmpActivity.class.getName()
					+ " activity";
			q = session.createQuery(queryString);
			// q.setParameter("activity.getOrgrole().getOrganisation().getAmpOrgId",ampOrgId,Hibernate.LONG)
			// ;
			Collection act = q.list();

			Iterator actItr = act.iterator();
			while (actItr.hasNext()) {
				AmpActivity ampActivity = (AmpActivity) actItr.next();
				Iterator iter1 = ampActivity.getOrgrole().iterator();

				while (iter1.hasNext()) {
					AmpOrgRole ampOrg = (AmpOrgRole) iter1.next();
					if (ampOrg.getOrganisation().getAmpOrgId().intValue() == ampOrgId
							.intValue()) {
						act1.add(ampActivity);
					} // if
				} // while
			} // while
		} catch (Exception ex) {
			logger.debug("Unable to get activty names  from database "
					+ ex.getMessage());
		}
		return act1;
	}

	public static AmpTermsAssist getAssistanceType(Long ampTermsAssistId) {

		Session session = null;
		Query q = null;
		AmpTermsAssist ampTermsAssist = null;
		Collection c = null;
		Iterator iter = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = new String();
			queryString = "select a from " + AmpTermsAssist.class.getName()
					+ " a where (a.ampTermsAssistId=:ampTermsAssistId) ";
			q = session.createQuery(queryString);
			q.setParameter("ampTermsAssistId", ampTermsAssistId, Hibernate.LONG);
			c = q.list();
			if (c.size() != 0) {
				iter = c.iterator();
				if (iter.hasNext()) {
					ampTermsAssist = (AmpTermsAssist) iter.next();
				}
			} else {
				if (logger.isDebugEnabled())
					logger.debug("Unable to get type of assistance for id "
							+ ampTermsAssistId);
			}
		} catch (Exception ex) {
			logger.error("Unable to get type of assistance from database", ex);
		}
		if (logger.isDebugEnabled()) {
			if (ampTermsAssist != null)
				logger.debug("getAssistanceType() returning type of assistance :"
						+ ampTermsAssist.getTermsAssistName());
		}
		return ampTermsAssist;
	}

	public static Collection getAmpReportSector(Long ampActivityId) {
		Session session = null;
		ArrayList sectors = new ArrayList();

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select a from "
					+ AmpReportSector.class.getName() + " a "
					+ "where (a.ampActivityId=:ampActivityId)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("ampActivityId", ampActivityId, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				AmpReportSector act = (AmpReportSector) itr.next();
				if (sectors.indexOf(act.getSectorName()) == -1)
					sectors.add(act.getSectorName());
			}

		} catch (Exception ex) {
			logger.error("Unable to get activity sectors" + ex);
		}
		return sectors;
	}

	public static Collection getAmpReportSectorId(Long ampActivityId) {
		Session session = null;
		Collection sectors = new ArrayList();

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select a from "
					+ AmpReportSector.class.getName() + " a "
					+ "where (a.ampActivityId=:ampActivityId)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("ampActivityId", ampActivityId, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				AmpReportSector act = (AmpReportSector) itr.next();
				// AmpSector
				// ampSector=DbUtil.getAmpParentSector(act.getAmpSectorId());
				sectors.add(act);
			}

		} catch (Exception ex) {
			logger.warn("Unable to get activity sectors" + ex);
		}
		return sectors;
	}

	public static Collection getAmpReportSectors(String inClause) {
		Session session = null;
		ArrayList sectors = new ArrayList();
		ArrayList activityId = new ArrayList();

		try {
			logger.debug("Team Id:" + inClause);
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select report from "
					+ AmpReportCache.class.getName()
					+ " report where (report.ampTeamId in("
					+ inClause
					+ ")) and (report.reportType='1') group by report.ampActivityId";
			Query qry = session.createQuery(queryString);
			Iterator itr = qry.list().iterator();
			inClause = null;
			while (itr.hasNext()) {
				AmpReportCache ampReportCache = (AmpReportCache) itr.next();
				if (inClause == null)
					inClause = "'" + ampReportCache.getAmpActivityId() + "'";
				else
					inClause = inClause + ",'"
							+ ampReportCache.getAmpActivityId() + "'";
			}
			logger.debug("Activity Id:" + inClause);
			queryString = "select sector from "
					+ AmpReportSector.class.getName()
					+ " sector where (sector.ampActivityId in(" + inClause
					+ ")) order by sector.sectorName,sector.ampActivityId";
			AmpProjectBySector ampProjectBySector = null;
			// logger.debug("Query String: " + queryString);
			qry = session.createQuery(queryString);
			// qry.setParameter("ampTeamId",ampTeamId,Hibernate.LONG);
			// logger.debug("Size: " + qry.list().size());
			itr = qry.list().iterator();
			int flag = 0;
			while (itr.hasNext()) {
				AmpReportSector ampReportSector = (AmpReportSector) itr.next();
				if (ampProjectBySector == null) {
					// logger.debug("Start: ");
					ampProjectBySector = new AmpProjectBySector();
					ampProjectBySector.setAmpActivityId(new ArrayList());
				} else if (!(ampProjectBySector.getSector().getAmpSectorId()
						.equals(ampReportSector.getAmpSectorId()))) {
					ampProjectBySector.getAmpActivityId().addAll(activityId);
					sectors.add(ampProjectBySector);
					ampProjectBySector = new AmpProjectBySector();
					ampProjectBySector.setAmpActivityId(new ArrayList());
					activityId.clear();
					flag = 0;
				}
				if (flag == 0) {
					ampProjectBySector.setSector(ampReportSector);
					flag = 1;
				}
				if (activityId.indexOf(ampReportSector.getAmpActivityId()) == -1) {
					// logger.debug("Id: " +
					// ampReportSector.getAmpActivityId());
					activityId.add(ampReportSector.getAmpActivityId());
				}
				if (!itr.hasNext()) {
					ampProjectBySector.getAmpActivityId().addAll(activityId);
					sectors.add(ampProjectBySector);
				}
			}

			logger.debug("Sectors size: " + sectors.size());

		} catch (Exception ex) {
			logger.error("Unable to get activity sectors" + ex.getMessage());
		}
		return sectors;
	}

	public static Collection getAmpReportLocation(Long ampActivityId) {
		Session session = null;
		Collection regions = new ArrayList();

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select a from "
					+ AmpReportLocation.class.getName() + " a "
					+ "where (a.ampActivityId=:ampActivityId)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("ampActivityId", ampActivityId, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				AmpReportLocation act = (AmpReportLocation) itr.next();
				if (act.getRegion() != null)
					regions.add(act.getRegion());
			}

		} catch (Exception ex) {
			logger.error("Unable to get activity sectors" + ex);
		}
		return regions;
	}

	public static AmpLevel getAmpLevel(Long id) {
		Session session = null;
		AmpLevel level = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select l from " + AmpLevel.class.getName()
					+ " l " + "where (l.ampLevelId=:id)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				level = (AmpLevel) itr.next();
			}

		} catch (Exception ex) {
			logger.error("Unable to get Level" + ex);
		}
		return level;
	}

	public static Collection getAllLevels() {
		Session session = null;
		Collection col = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select c from " + AmpLevel.class.getName()
					+ " c";
			Query qry = session.createQuery(queryString);
			col = qry.list();
		} catch (Exception e) {
			logger.error("Exception from getAllLevels()");
			logger.error(e.toString());
		}
		return col;
	}

	public static Collection getAllActivityStatus() {
		Session session = null;
		Collection col = new ArrayList();

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select c from " + AmpStatus.class.getName()
					+ " c";
			Query qry = session.createQuery(queryString);
			col = qry.list();
		} catch (Exception e) {
			logger.error("Exception from getAllActivityStatus()");
			logger.error(e.toString());
		}
		return col;
	}

	public static Collection getAllTermAssist() {
		Session session = null;
		Collection col = new ArrayList();

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select c from "
					+ AmpTermsAssist.class.getName() + " c";
			Query qry = session.createQuery(queryString);
			col = qry.list();
		} catch (Exception e) {
			logger.error("Exception from getAllTermAssist()");
			logger.error(e.toString());
		}
		return col;
	}

	/*
	 * public static Collection getAllFinancingInstruments() { Session session =
	 * null; Collection col = new ArrayList();
	 * 
	 * try { session = PersistenceManager.getRequestDBSession(); String
	 * queryString = "select c from " + AmpModality.class.getName() + " c";
	 * Query qry = session.createQuery(queryString); col = qry.list(); } catch
	 * (Exception e) {
	 * logger.debug("Exception from getAllFinancingInstruments() : " + e);
	 * e.printStackTrace(System.out); } return col; }
	 */

	public static Collection<AmpCategoryValue> getAllFinancingInstruments() {
		return CategoryManagerUtil
				.getAmpCategoryValueCollectionByKey(CategoryConstants.FINANCING_INSTRUMENT_KEY);
	}

	public static Collection getAllDonorOrgs() {
		Session session = null;
		Collection col = new ArrayList();

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select distinct org from "
					+ AmpFunding.class.getName()
					+ " f inner join f.ampDonorOrgId org "
					+ " order by org.acronym asc";
			Query qry = session.createQuery(queryString);
			col = qry.list();
		} catch (Exception ex) {
			logger.error("Exception from getAllDonorOrgs() : " + ex);
			logger.error(ex.toString());
		}
		return col;
	}

	/**
	 * gets the skeletons of all the OrgGroups in the database
	 * @return
	 */
	public static List<OrgGroupSkeleton> getAllOrgGroupSkeletons()
	{
		List<OrgGroupSkeleton> col = new ArrayList<OrgGroupSkeleton>();

		String orgGrpNameHql = AmpOrgGroup.hqlStringForName("c");
		String queryString = "select c.ampOrgGrpId, " + orgGrpNameHql + " FROM " + AmpOrgGroup.class.getName() + " c";
		Query qry = PersistenceManager.getSession().createQuery(queryString);
		for(Object[] grpInfo:((List<Object[]>) qry.list()))
		{
			OrgGroupSkeleton skel = new OrgGroupSkeleton();
			skel.setAmpOrgGrpId(PersistenceManager.getLong(grpInfo[0]));
			skel.setOrgGrpName(PersistenceManager.getString(grpInfo[1]));
			col.add(skel);
		}
		Collections.sort(col);
		return col;
	}
	
	public static List<AmpOrgGroup> getAllOrgGroups() {
		try {
			Session session = PersistenceManager.getRequestDBSession();
			String orgGrpNameHql = AmpOrgGroup.hqlStringForName("c");
			String queryString = "select c from " + AmpOrgGroup.class.getName()
					+ " c order by lower(" + orgGrpNameHql + ") asc";
			Query qry = session.createQuery(queryString);
			return qry.list();
		} catch (Exception e) {
			logger.debug("Exception from getAllOrgGroups()");
			logger.debug(e.toString());
			return null;
		}
	}

	public static Collection<AmpOrgType> getAllOrgTypes() {
		Session session = null;
		Collection<AmpOrgType> col = new ArrayList<AmpOrgType>();

		try {
			session = PersistenceManager.getRequestDBSession();
			String orgTypeHql = AmpOrgType.hqlStringForName("c");
			String queryString = "select c from " + AmpOrgType.class.getName()
					+ " c order by " + orgTypeHql + " asc";
			Query qry = session.createQuery(queryString);
			col = qry.list();
		} catch (Exception e) {
			logger.error("Exception from getAllOrgTypes()");
			logger.error(e.toString());
		}
		return col;
	}
	
	public static Collection<AmpOrgGroup> getAllContractingAgencyGroupsOfPortfolio()
	{
		if (AmpCaching.getInstance().allContractingAgencyGroupsOfPortfolio != null)
			return new ArrayList<AmpOrgGroup>(AmpCaching.getInstance().allContractingAgencyGroupsOfPortfolio);
		
		Session session = null;
		List<AmpOrgGroup> col = new ArrayList();
		try {
			session = PersistenceManager.getRequestDBSession();
			String rewrittenColumns = SQLUtils.rewriteQuery("amp_org_group", "aog", 
					new HashMap<String, String>(){{
						put("org_grp_name", InternationalizedModelDescription.getForProperty(AmpOrgGroup.class, "orgGrpName").getSQLFunctionCall("aog.amp_org_grp_id"));
					}});
			String idsQueryString = String.format("SELECT DISTINCT amp_org_grp_id FROM v_contracting_agency_groups");
			String queryString = "SELECT " + rewrittenColumns + " FROM amp_org_group aog WHERE aog.amp_org_grp_id IN (" + idsQueryString + ")";

			Query qry = session.createSQLQuery(queryString).addEntity(
					AmpOrgGroup.class);
			col = qry.list();
		} catch (Exception e) {
			logger.error("Exception from getAllContractingAgencyGroupsOfPortfolio()", e);
		}
		AmpCaching.getInstance().allContractingAgencyGroupsOfPortfolio = new ArrayList<AmpOrgGroup>(col);
		return col;
	}

	/**
	 * fetches DONOR org groups of the database portfolio
	 * @return
	 */
	public static List<AmpOrgGroup> getAllOrgGroupsOfPortfolio() {
		if (AmpCaching.getInstance().allOrgGroupsOfPortfolio != null)
			return new ArrayList<AmpOrgGroup>(AmpCaching.getInstance().allOrgGroupsOfPortfolio);

		Session session = null;
		List<AmpOrgGroup> col = new ArrayList<AmpOrgGroup>();
		try {
			session = PersistenceManager.getRequestDBSession();
			String rewrittenColumns = SQLUtils.rewriteQuery("amp_org_group", "aog", 
						new HashMap<String, String>(){{
							put("org_grp_name", InternationalizedModelDescription.getForProperty(AmpOrgGroup.class, "orgGrpName").getSQLFunctionCall("aog.amp_org_grp_id"));
						}});
			String queryString = "select distinct " + rewrittenColumns + " from amp_org_group aog "
					+ "inner join amp_organisation ao on (ao.org_grp_id = aog.amp_org_grp_id) "
					+ "inner join amp_funding af on (af.amp_donor_org_id = ao.amp_org_id) "
					+ "inner join amp_activity aa on (aa.amp_activity_id = af.amp_activity_id) where (ao.deleted is null or ao.deleted = false) ";
			Query qry = session.createSQLQuery(queryString).addEntity(
					AmpOrgGroup.class);
			col = qry.list();
		} catch (Exception e) {
			logger.error("Got exception from getAllOrgGroupsOfPortfolio()", e);
		}
		AmpCaching.getInstance().allOrgGroupsOfPortfolio = new ArrayList<AmpOrgGroup>(col);
		return col;
	}

	public static List<AmpOrgType> getAllOrgTypesOfPortfolio() {
		if (AmpCaching.getInstance().allOrgTypesOfPortfolio != null)
			return new ArrayList<AmpOrgType>(AmpCaching.getInstance().allOrgTypesOfPortfolio);
		
		Session session = null;
		List<AmpOrgType> col = new ArrayList<AmpOrgType>();
		try {
			session = PersistenceManager.getRequestDBSession();
			String rewrittenColumns = SQLUtils.rewriteQuery("amp_org_type", "aot", 
				new HashMap<String, String>(){{
					put("org_type", InternationalizedModelDescription.getForProperty(AmpOrgType.class, "orgType").getSQLFunctionCall("aot.amp_org_type_id"));
				}});

			String queryString = "select distinct " + rewrittenColumns +" from amp_org_type aot "
					+ "inner join amp_org_group aog on (aot.amp_org_type_id=aog.org_type ) "
					+ "inner join amp_organisation ao on (aog.amp_org_grp_id=ao.org_grp_id ) "
					+ "inner join amp_funding af on (af.amp_donor_org_id = ao.amp_org_id) "
					+ "inner join amp_activity aa on (aa.amp_activity_id = af.amp_activity_id) where (ao.deleted is null or ao.deleted = false) ";
			Query qry = session.createSQLQuery(queryString).addEntity(
					AmpOrgType.class);
			col = qry.list();
		} catch (Exception e) {
			logger.error("Exception from getAllOrgTypesOfPortfolio()", e);
		}
		AmpCaching.getInstance().allOrgTypesOfPortfolio = new ArrayList<AmpOrgType>(col);
		return col;
	}

	public static AmpOrgType getAmpOrgType(Long ampOrgTypeId) {
		Session session = null;
		Query qry = null;
		AmpOrgType ampOrgType = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select f from " + AmpOrgType.class.getName()
					+ " f where (f.ampOrgTypeId=:ampOrgTypeId)";
			qry = session.createQuery(queryString);
			qry.setParameter("ampOrgTypeId", ampOrgTypeId, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				ampOrgType = (AmpOrgType) itr.next();
			}
		} catch (Exception e) {
			logger.error("Unable to get Org Type");
			logger.error("Exceptiion " + e);
		}
		return ampOrgType;
	}

	public static AmpOrgGroup getAmpOrgGroup(Long id) {
		Session session = null;
		AmpOrgGroup grp = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select l from " + AmpOrgGroup.class.getName()
					+ " l " + "where (l.ampOrgGrpId=:id)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				grp = (AmpOrgGroup) itr.next();
			}

		} catch (Exception ex) {
			logger.error("Unable to get Org Group" + ex);
		}
		return grp;
	}

	public static AmpOrgGroup getAmpOrgGroupByName(String name) {
		Session session = null;
		AmpOrgGroup grp = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String orgGrpName = AmpOrgGroup.hqlStringForName("l");
			String queryString = "select l from " + AmpOrgGroup.class.getName()
					+ " l " + "where (" + orgGrpName + "=:name)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("name", name, Hibernate.STRING);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				grp = (AmpOrgGroup) itr.next();
			}

		} catch (Exception ex) {
			logger.error("Unable to get Org Group" + ex);
		}
		return grp;
	}

	public static boolean checkAmpOrgGroupDuplication(String name, Long id) {
		boolean duplicateName = false;
		Session session = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			String orgGrpName = AmpOrgGroup.hqlStringForName("l");
			String queryString = "select count(l) from "
					+ AmpOrgGroup.class.getName() + " l "
					+ "where upper(" + orgGrpName + ") like upper(:name) ";
			if (id != null) {
				queryString += " and l.ampOrgGrpId!=" + id;
			}
			Query qry = session.createQuery(queryString);
			qry.setParameter("name", name, Hibernate.STRING);
			Integer amount = (Integer) qry.uniqueResult();
			if (amount != null && amount.intValue() > 0) {
				duplicateName = true;
			}
		} catch (Exception e) {
			logger.error("Unable to get Org Group" + e);
		}

		return duplicateName;
	}

	public static Collection<AmpOrgGroup> searchForOrganisationGroupByType(Long orgType) {
		Session session = null;
		Collection col = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String orgGrpName = AmpOrgGroup.hqlStringForName("org");
			String queryString = "select org from "
					+ AmpOrgGroup.class.getName() + " org "
					+ " where org.orgType=:orgType";
			queryString += "  order by " + orgGrpName;
			Query qry = session.createQuery(queryString);
			qry.setParameter("orgType", orgType, Hibernate.LONG);
			col = qry.list();
		} catch (Exception ex) {
			logger.error("Unable to search " + ex);
		}
		return col;
	}

	public static Collection<AmpOrgGroup> searchForOrganisationGroup(
			String keyword, Long orgType) {
		Session session = null;
		Collection col = null;
		keyword = keyword.toLowerCase();

		try {
			session = PersistenceManager.getRequestDBSession();
			String orgGrpName = AmpOrgGroup.hqlStringForName("org");
			String queryString = "select org from "
					+ AmpOrgGroup.class.getName() + " org "
					+ " where lower(" + orgGrpName + ") like '%" + keyword
					+ "%') and org.orgType=:orgType";
			Query qry = session.createQuery(queryString);
			qry.setParameter("orgType", orgType, Hibernate.LONG);
			col = qry.list();
		} catch (Exception ex) {
			logger.error("Unable to search " + ex);
		}
		return col;
	}

	public static Collection<AmpOrgGroup> searchForOrganisationGroup(
			String keyword) {
		Session session = null;
		Collection col = null;
		keyword = keyword.toLowerCase();

		try {
			session = PersistenceManager.getRequestDBSession();
			String orgGrpName = AmpOrgGroup.hqlStringForName("org");
			String queryString = "select distinct org from "
					+ AmpOrgGroup.class.getName() + " org "
					+ " where lower(" + orgGrpName + ") like '%" + keyword
					+ "%' or lower(org.orgGrpCode) like '%" + keyword + "%'";
			Query qry = session.createQuery(queryString);
			col = qry.list();
		} catch (Exception ex) {
			logger.error("Unable to search " + ex);
		}
		return col;
	}

	public static Collection<AmpOrgGroup> getAllOrganisationGroup() {
		Session session = null;
		Query qry = null;
		Collection organisation = new ArrayList();

		try {
			session = PersistenceManager.getRequestDBSession();
			String orgName = AmpOrganisation.hqlStringForName("o");
			String queryString = "select o from " + AmpOrgGroup.class.getName()
					+ " o order by " + AmpOrgGroup.hqlStringForName("o") + " asc";
			qry = session.createQuery(queryString);
			organisation = qry.list();
		} catch (Exception e) {
			logger.error("Unable to get all organisation groups");
			logger.error("Exceptiion " + e);
		}
		return organisation;
	}

	public static Collection getOrgByGroup(Long Id) {

		Session sess = null;
		Collection col = new ArrayList();
		Query qry = null;

		try {
			sess = PersistenceManager.getRequestDBSession();
			String queryString = "select o from "
					+ AmpOrganisation.class.getName()
					+ " o where (o.orgGrpId=:orgGrpId) and (o.deleted is null or o.deleted = false) ";
			qry = sess.createQuery(queryString);
			qry.setParameter("orgGrpId", Id, Hibernate.LONG);
			col = qry.list();
		} catch (Exception e) {
			logger.error("Exception from getOrgByGroup(): " + e);
			logger.error(e.getMessage() );
		}
		return col;
	}

	public static boolean chkOrgTypeReferneces(Long Id) {

		Session sess = null;
		Query qry = null;
		String queryString = null;

		try {
			sess = PersistenceManager.getRequestDBSession();
			queryString = "select o from " + AmpOrgGroup.class.getName()
					+ " o where (o.orgType=:orgTypeId)";
			qry = sess.createQuery(queryString);
			qry.setParameter("orgTypeId", Id, Hibernate.LONG);
			if (null != qry.list() && qry.list().size() > 0) {
				return true;
			}

			// queryString = "select o from " + AmpOrganisation.class.getName()
			// + " o where (o.orgTypeId=:orgTypeId)";
			// qry = sess.createQuery(queryString);
			// qry.setParameter("orgTypeId", Id, Hibernate.LONG);
			// if (null != qry.list() && qry.list().size() > 0)
			// return true;
			// else {
			// queryString = "select o from " + AmpOrgGroup.class.getName()
			// + " o where (o.orgType=:orgTypeId)";
			// qry = sess.createQuery(queryString);
			// qry.setParameter("orgTypeId", Id, Hibernate.LONG);
			// if (null != qry.list() && qry.list().size() > 0)
			// return true;
			// }
		} catch (Exception e) {
			logger.debug("Exception from chkOrgTypeReferneces(): " + e);
			e.printStackTrace(System.out);
		}
		return false;
	}

	public static AmpOrgType getOrgType(Long typeId) {

		Session sess = null;
		Query qry = null;
		Collection col = new ArrayList();
		AmpOrgType ot = new AmpOrgType();

		try {
			sess = PersistenceManager.getRequestDBSession();
			String queryString = "select o from " + AmpOrgType.class.getName()
					+ " o where (o.ampOrgTypeId=:typeId)";
			qry = sess.createQuery(queryString);
			qry.setParameter("typeId", typeId, Hibernate.LONG);
			col = qry.list();
			Iterator itr = col.iterator();
			while (itr.hasNext()) {
				ot = (AmpOrgType) itr.next();
			}
		} catch (Exception e) {
			logger.debug("Exception from getOrgType() : " + e.getMessage());
		}
		return ot;
	}

	public static Collection getOrgByCode(String action, String code, Long id) {

		Session sess = null;
		Collection col = new ArrayList();
		Query qry = null;
		String queryString;

		try {
			sess = PersistenceManager.getRequestDBSession();
			if ("create".equals(action)) {
				queryString = "select o from "
						+ AmpOrganisation.class.getName()
						+ " o where (o.orgCode=:code) and (o.deleted is null or o.deleted = false) ";
				qry = sess.createQuery(queryString);
				qry.setParameter("code", code, Hibernate.STRING);
			} else if ("edit".equals(action)) {
				queryString = "select o from "
						+ AmpOrganisation.class.getName()
						+ " o where (o.orgCode=:code) and (o.ampOrgId!=:id) and (o.deleted is null or o.deleted = false) ";
				qry = sess.createQuery(queryString);
				qry.setParameter("code", code, Hibernate.STRING);
				qry.setParameter("id", id, Hibernate.LONG);
			}
			col = qry.list();
		} catch (Exception e) {
			logger.debug("Exception from getOrgByCode()");
			logger.debug(e.toString());
		}
		return col;
	}

	public static Collection getOrganisationAsCollection(Long id) {
		Session session = null;
		Collection org = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select o from "
					+ AmpOrganisation.class.getName()
					+ " o "
					+ "where (o.ampOrgId=:id) and (o.deleted is null or o.deleted = false) ";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);
			org = qry.list();
		} catch (Exception ex) {
			logger.error("Unable to get organisation from database", ex);
		}
		logger.debug("Getting organisation successfully ");
		return org;
	}

	public static AmpField getAmpFieldByName(String com) {
		Session session = null;
		Query qry = null;
		AmpField comments = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select o from " + AmpField.class.getName()
					+ " o " + "where (o.fieldName=:com)";
			qry = session.createQuery(queryString);
			qry.setParameter("com", com);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				comments = (AmpField) itr.next();
			}
		} catch (Exception e) {
			logger.error("Unable to get all comments");
			logger.debug("Exceptiion " + e);
		}
		return comments;
	}

	public static AmpField getAmpFieldById(Long fieldId) {
		Session session = null;
		Query qry = null;
		AmpField field = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select o from " + AmpField.class.getName()
					+ " o " + "where (o.ampFieldId=:id)";
			qry = session.createQuery(queryString);
			qry.setParameter("id", fieldId, Hibernate.STRING);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				field = (AmpField) itr.next();
			}
		} catch (Exception e) {
			logger.error("Unable to get field");
			logger.debug("Exceptiion " + e);
		}
		return field;
	}

	public static Collection getAmpFields() {
		Session session = null;
		Query qry = null;
		Collection colAux = new ArrayList();

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select o from " + AmpField.class.getName()
					+ " o ";
			qry = session.createQuery(queryString);
			// colAux = qry.list();
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				AmpField af = (AmpField) itr.next();
				colAux.add(af);
			}
		} catch (Exception e) {
			logger.error("Unable to get all comments");
			logger.debug("Exceptiion " + e);
		}
		return colAux;
	}

	public static ArrayList getAllCommentsByField(Long fid, Long aid) {
		Session session = null;
		Query qry = null;
		ArrayList comments = new ArrayList();

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select o from " + AmpComments.class.getName()
					+ " o "
					+ "where (o.ampFieldId=:fid) and (o.ampActivityId=:aid)";
			qry = session.createQuery(queryString);
			qry.setParameter("fid", fid, Hibernate.LONG);
			qry.setParameter("aid", aid, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				AmpComments com = (AmpComments) itr.next();
				comments.add(com);
			}
		} catch (Exception e) {
			logger.error("Unable to get all comments");
			logger.debug("Exceptiion " + e);
		}
		return comments;
	}

	public static ArrayList getAllCommentsByActivityId(Long aid) {
		Session session = null;
		Query qry = null;
		ArrayList comments = new ArrayList();

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select o from " + AmpComments.class.getName()
					+ " o " + "where (o.ampActivityId=:aid)";
			qry = session.createQuery(queryString);
			qry.setParameter("aid", aid, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				AmpComments com = (AmpComments) itr.next();
				comments.add(com);
			}
		} catch (Exception e) {
			logger.error("Unable to get all comments");
			logger.debug("Exceptiion " + e);
		}
		return comments;
	}
	
	public static ArrayList getBudgetStructure(Long aid) {
		Session session = null;
		Query qry = null;
		ArrayList budgetStructure = new ArrayList();

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select o from " + AmpActivityBudgetStructure.class.getName()
					+ " o " + "where (o.activity=:aid)";
			qry = session.createQuery(queryString);
			qry.setParameter("aid", aid, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				AmpActivityBudgetStructure bs = (AmpActivityBudgetStructure) itr.next();
				budgetStructure.add(bs);
			}
		} catch (Exception e) {
			logger.error("Unable to get all budget structures");
			logger.debug("Exceptiion " + e);
		}
		return budgetStructure;
	}

	public static ArrayList getAllCommentsByActivityId(Long aid, Session session) {
		Query qry = null;
		ArrayList comments = new ArrayList();

		try {
			String queryString = "select o from " + AmpComments.class.getName()
					+ " o " + "where (o.ampActivityId=:aid)";
			qry = session.createQuery(queryString);
			qry.setParameter("aid", aid, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				AmpComments com = (AmpComments) itr.next();
				comments.add(com);
			}
		} catch (Exception e) {
			logger.error("Unable to get all comments");
			logger.debug("Exceptiion " + e);
		}
		return comments;
	}

	public static ArrayList getAllIPAContractsByActivityId(Long aid) {
		Session session = null;
		Query qry = null;
		ArrayList contracts = new ArrayList();

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select o from " + IPAContract.class.getName()
					+ " o " + "where (o.activity=:aid)";
			qry = session.createQuery(queryString);
			qry.setParameter("aid", aid, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				IPAContract com = (IPAContract) itr.next();
				contracts.add(com);
			}
		} catch (Exception e) {
			logger.error("Unable to get all contracts");
			logger.debug("Exceptiion " + e);
		}
		return contracts;
	}

	public static AmpComments getAmpComment(Long id) {
		Session session = null;
		AmpComments comment = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select l from " + AmpComments.class.getName()
					+ " l " + "where (l.ampCommentId=:id)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				comment = (AmpComments) itr.next();
			}

		} catch (Exception ex) {
			logger.error("Unable to get comment" + ex);
		}
		return comment;
	}

	public static int getAmpMaxToYear(Long ampTeamId) {
		Session session = null;
		Query q = null;
		Integer year = null;
		String queryString = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			queryString = " select extract(YEAR from max(afd.transactionDate)) from "
					+ AmpFundingDetail.class.getName()
					+ " afd,"
					+ AmpFunding.class.getName()
					+ " af,"
					+ AmpActivity.class.getName()
					+ " aa where (afd.ampFundingId=af.ampFundingId) and af.ampActivityId=aa.ampActivityId and aa.team.ampTeamId='"
					+ ampTeamId + "'";
			q = session.createQuery(queryString);
			year = (Integer) q.list().get(0);

		} catch (Exception ex) {
			logger.error("Unable to get Amp status   from database "
					+ ex.getMessage());
		}
		return year.intValue();
	}

	public static ArrayList getAmpReportPhysicalPerformance(Long ampActivityId) {
		Session session = null;
		ArrayList progress = new ArrayList();

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select a from "
					+ AmpReportPhysicalPerformance.class.getName() + " a "
					+ "where (a.ampActivityId=:ampActivityId)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("ampActivityId", ampActivityId, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				AmpReportPhysicalPerformance act = (AmpReportPhysicalPerformance) itr
						.next();
				progress.add(act.getTitle() + " : " + act.getDescription());
			}

		} catch (Exception ex) {
			logger.error("Unable to get activity sectors" + ex);
		}
		return progress;
	}

	public static Group getGroup(String key, Long siteId) {
		Session session = null;
		Group group = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			String qryStr = "select grp from " + Group.class.getName()
					+ " grp " + "where (grp.key=:key) and (grp.site=:sid)";
			Query qry = session.createQuery(qryStr);
			qry.setParameter("key", key, Hibernate.STRING);
			qry.setParameter("sid", siteId, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				group = (Group) itr.next();
			}
		} catch (Exception ex) {
			logger.error("Unable to get Group " + ex.getMessage());
		}
		return group;
	}

//	public static ArrayList getApprovedActivities(String inClause) {
//		ArrayList actList = new ArrayList();
//		Session session = null;
//		Query q = null;
//		String queryString;
//		try {
//
//			session = PersistenceManager.getRequestDBSession();
//
//			queryString = "select act.ampActivityId from "
//					+ AmpActivity.class.getName()
//					+ " act where (act.team.ampTeamId in(" + inClause
//					+ ")) and (act.approvalStatus=:status)";
//			q = session.createQuery(queryString);
//			q.setParameter("status", "approved", Hibernate.STRING);
//			actList = (ArrayList) q.list();
//			// logger.debug("Approved Activity List Size: " + actList.size());
//
//		} catch (Exception ex) {
//			logger.error("Unable to get AmpActivity [getApprovedActivities()]",
//					ex);
//		}
//		logger.debug("Getting Approved activities Executed successfully ");
//		return actList;
//	}

//	public static ArrayList getAmpDonors(String inClause) {
//		ArrayList donor = new ArrayList();
//		Session session = null;
//		Query q = null;
//		Iterator iterActivity = null;
//		Iterator iter = null;
//
//		try {
//
//			session = PersistenceManager.getRequestDBSession();
//			String queryString = new String();
//			queryString = "select activity from " + AmpActivity.class.getName()
//					+ " activity where activity.team.ampTeamId in(" + inClause
//					+ ")";
//			q = session.createQuery(queryString);
//			// logger.debug("Activity List: " + q.list().size());
//			iterActivity = q.list().iterator();
//			while (iterActivity.hasNext()) {
//				AmpActivity ampActivity = (AmpActivity) iterActivity.next();
//
//				// logger.debug("Org Role List: " +
//				// ampActivity.getOrgrole().size());
//				iter = ampActivity.getOrgrole().iterator();
//				while (iter.hasNext()) {
//					AmpOrgRole ampOrgRole = (AmpOrgRole) iter.next();
//					if (ampOrgRole.getRole().getRoleCode()
//							.equals(Constants.FUNDING_AGENCY)) {
//						if (donor.indexOf(ampOrgRole.getOrganisation()) == -1)
//							donor.add(ampOrgRole.getOrganisation());
//					}
//				}
//			}
//			logger.debug("Donors: " + donor.size());
//			int n = donor.size();
//			for (int i = 0; i < n - 1; i++) {
//				for (int j = 0; j < n - 1 - i; j++) {
//					AmpOrganisation firstOrg = (AmpOrganisation) donor.get(j);
//					AmpOrganisation secondOrg = (AmpOrganisation) donor
//							.get(j + 1);
//					if (firstOrg.getAcronym().compareToIgnoreCase(
//							secondOrg.getAcronym()) > 0) {
//						AmpOrganisation tempOrg = firstOrg;
//						donor.set(j, secondOrg);
//						donor.set(j + 1, tempOrg);
//					}
//				}
//			}
//
//		} catch (Exception ex) {
//			logger.debug("Unable to get Donor " + ex.getMessage());
//		}
//		return donor;
//	}

//	public static ArrayList getAmpDonorsForActivity(Long id) {
//		ArrayList donor = new ArrayList();
//		Session session = null;
//		Query q = null;
//		Iterator iterActivity = null;
//		Iterator iter = null;
//
//		try {
//
//			session = PersistenceManager.getRequestDBSession();
//			String queryString = new String();
//			queryString = "select activity from " + AmpActivity.class.getName()
//					+ " activity where (activity.ampActivityId=:id)";
//
//			q = session.createQuery(queryString);
//			q.setParameter("id", id, Hibernate.LONG);
//			// logger.debug("Activity List: " + q.list().size());
//			iterActivity = q.list().iterator();
//			while (iterActivity.hasNext()) {
//				AmpActivity ampActivity = (AmpActivity) iterActivity.next();
//
//				// logger.debug("Org Role List: " +
//				// ampActivity.getOrgrole().size());
//				iter = ampActivity.getOrgrole().iterator();
//				while (iter.hasNext()) {
//					AmpOrgRole ampOrgRole = (AmpOrgRole) iter.next();
//					if (ampOrgRole.getRole().getRoleCode()
//							.equals(Constants.FUNDING_AGENCY)) {
//						if (donor.indexOf(ampOrgRole.getOrganisation()) == -1)
//							donor.add(ampOrgRole.getOrganisation());
//					}
//				}
//			}
//			logger.debug("Donors: " + donor.size());
//			int n = donor.size();
//			for (int i = 0; i < n - 1; i++) {
//				for (int j = 0; j < n - 1 - i; j++) {
//					AmpOrganisation firstOrg = (AmpOrganisation) donor.get(j);
//					AmpOrganisation secondOrg = (AmpOrganisation) donor
//							.get(j + 1);
//					if (firstOrg.getAcronym().compareToIgnoreCase(
//							secondOrg.getAcronym()) > 0) {
//						AmpOrganisation tempOrg = firstOrg;
//						donor.set(j, secondOrg);
//						donor.set(j + 1, tempOrg);
//					}
//				}
//			}
//
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			logger.debug("Unable to get Donor " + ex.getMessage());
//		}
//		// return donor;
//
//		ArrayList donorString = new ArrayList();
//		Iterator i = donor.iterator();
//		while (i.hasNext()) {
//			AmpOrganisation element = (AmpOrganisation) i.next();
//			donorString.add(element.getName());
//		}
//
//		return donorString;
//	}

//	public static Collection getAllAhSurveys() {
//		Session session = null;
//
//		try {
//			session = PersistenceManager.getRequestDBSession();
//			String qry = "select svy from " + AmpAhsurvey.class.getName()
//					+ " svy";
//			Query q = session.createQuery(qry);
//			return q.list();
//		} catch (Exception ex) {
//			logger.debug("Unable to get survey : " + ex.getMessage());
//			ex.printStackTrace(System.out);
//		}
//		return null;
//	}

	public static Collection<SurveyFunding> getAllSurveysByActivity(
			Long activityId, EditActivityForm svForm) {
		ArrayList survey = new ArrayList();
		List fundingSet = new ArrayList();
		Set surveySet = new HashSet();
		ArrayList donorOrgs = new ArrayList();
		Session session = null;
		Iterator iter1 = null;
		Iterator iter2 = null;
		Transaction tx = null;
		boolean surveySetEmpty = false;

		try {
			if (activityId == null)
				return survey;
			session = PersistenceManager.getRequestDBSession();

			if (activityId == null || activityId.longValue() == 0)
				return survey;
			AmpActivity activity = (AmpActivity) session.load(
					AmpActivity.class, activityId);

			// This section was added to get all funding organizations even the
			// new ones not yet saved.
			if (svForm.getFunding().getFundingOrganizations() != null) {
				Iterator itr1 = svForm.getFunding().getFundingOrganizations()
						.iterator();
				while (itr1.hasNext()) {
					FundingOrganization fOrg = (FundingOrganization) itr1
							.next();
					// add fundings
					if (fOrg.getFundings() != null) {
						Iterator itr2 = fOrg.getFundings().iterator();
						while (itr2.hasNext()) {
							Funding fund = (Funding) itr2.next();
							AmpFunding ampFunding = new AmpFunding();
							ampFunding.setAmpDonorOrgId(DbUtil
									.getOrganisation(fOrg.getAmpOrgId()));
							if (ampFunding.getAmpDonorOrgId().getOrgGrpId()
									.getOrgType().getOrgTypeCode()
									.equalsIgnoreCase("BIL")
									|| ampFunding.getAmpDonorOrgId()
											.getOrgGrpId().getOrgType()
											.getOrgTypeCode()
											.equalsIgnoreCase("MUL")) {
								fundingSet.add(ampFunding);
							}
						}
					}
				}
			}

			surveySet = activity.getSurvey();
			// logger.debug("fundingSet.size() : " + fundingSet.size());
			// logger.debug("surveySet.size() : " + surveySet.size());
			if (surveySet.size() < 1)
				surveySetEmpty = true;
			if (fundingSet.size() < 1)
				return survey;
			else {
				// adding a survey per donor, having at least one funding for
				// this activity, if there is none
				// or if a new donor with funding is added.
				boolean newSurvey = true;
				// beginTransaction();
				iter1 = fundingSet.iterator();
				while (iter1.hasNext()) {
					AmpFunding ampFund = (AmpFunding) iter1.next();
					donorOrgs.add(ampFund.getAmpDonorOrgId());
					if (!surveySetEmpty) {
						iter2 = surveySet.iterator();
						while (iter2.hasNext()) {
							AmpAhsurvey ahs = (AmpAhsurvey) iter2.next();
							if (ahs.getAmpDonorOrgId().equals(
									ampFund.getAmpDonorOrgId())) {
								newSurvey = false;
								break;
							}
						}
					}
					if (surveySetEmpty || newSurvey) {
						AmpAhsurvey ahsvy = new AmpAhsurvey();
						ahsvy.setAmpActivityId(activity);
						ahsvy.setAmpDonorOrgId(ampFund.getAmpDonorOrgId());
						activity.getSurvey().add(ahsvy);
						surveySetEmpty = false;
					}
					newSurvey = true;
				}

				iter2 = surveySet.iterator();
				while (iter2.hasNext()) {
					AmpAhsurvey ahs = (AmpAhsurvey) iter2.next();
					if (ahs.getPointOfDeliveryDonor() == null) {
						ahs.setPointOfDeliveryDonor(ahs.getAmpDonorOrgId());
					}
				}
				session.update(activity);
				// tx.commit();

				if (activity.getSurvey().isEmpty())
					logger.debug("activity.getSurvey() is empty.");
				else {
					// logger.debug("activity.getSurvey().size() : " +
					// activity.getSurvey().size());
					// logger.debug("donorOrgs.size() : " + donorOrgs.size());
					iter2 = activity.getSurvey().iterator();
					while (iter2.hasNext()) {
						AmpAhsurvey svy = (AmpAhsurvey) iter2.next();
						// getting only those survey records where donor-org is
						// in current funding list
						if (donorOrgs.indexOf(svy.getAmpDonorOrgId()) != -1) {
							SurveyFunding svfund = new SurveyFunding();
							svfund.setSurveyId(svy.getAmpAHSurveyId());
							svfund.setFundingOrgName(svy.getAmpDonorOrgId()
									.getName());
							if (svy.getPointOfDeliveryDonor() != null) {
								svfund.setDeliveryDonorName(svy
										.getPointOfDeliveryDonor().getName());
								svfund.setAcronim(svy.getPointOfDeliveryDonor()
										.getAcronym());
								svfund.setOrgID(svy.getPointOfDeliveryDonor()
										.getAmpOrgId());
							} else {
								svfund.setDeliveryDonorName(svy
										.getAmpDonorOrgId().getName());
								svfund.setAcronim(svy.getAmpDonorOrgId()
										.getAcronym());
								svfund.setOrgID(svy.getAmpDonorOrgId()
										.getAmpOrgId());
							}

							survey.add(svfund);
						}
					}
				}
			}
		} catch (Exception ex) {
			if (tx != null) {
				try {
					tx.rollback();
				} catch (HibernateException e) {
					logger.debug("rollback() failed : " + e.getMessage());
				}
			}
			logger.debug("Unable to get survey : " + ex.getMessage());
			ex.printStackTrace(System.out);
		}
		logger.debug("survey.size() : " + survey.size());
		return survey;
	}

	public static Collection getAllSurveysByActivity(Long activityId) {
		ArrayList survey = new ArrayList();
		Set fundingSet = new HashSet();
		Set surveySet = new HashSet();
		ArrayList donorOrgs = new ArrayList();
		Session session = null;
		Iterator iter1 = null;
		Iterator iter2 = null;
		Transaction tx = null;
		boolean surveySetEmpty = false;

		try {
			if (activityId == null)
				return survey;
			session = PersistenceManager.getRequestDBSession();

			if (activityId == null || activityId.longValue() == 0)
				return survey;
			AmpActivity activity = (AmpActivity) session.load(
					AmpActivity.class, activityId);

			fundingSet = activity.getFunding();
			surveySet = activity.getSurvey();
			// logger.debug("fundingSet.size() : " + fundingSet.size());
			// logger.debug("surveySet.size() : " + surveySet.size());
			if (surveySet.size() < 1)
				surveySetEmpty = true;
			if (fundingSet.size() < 1)
				return survey;
			else {
				// adding a survey per donor, having at least one funding for
				// this activity, if there is none
				// or if a new donor with funding is added.
				boolean newSurvey = true;
				// beginTransaction();
				iter1 = fundingSet.iterator();
				while (iter1.hasNext()) {
					AmpFunding ampFund = (AmpFunding) iter1.next();
					donorOrgs.add(ampFund.getAmpDonorOrgId());
					if (!surveySetEmpty) {
						iter2 = surveySet.iterator();
						while (iter2.hasNext()) {
							AmpAhsurvey ahs = (AmpAhsurvey) iter2.next();
							if (ahs.getAmpDonorOrgId().equals(
									ampFund.getAmpDonorOrgId())) {
								newSurvey = false;
								break;
							}
						}
					}
					if (surveySetEmpty || newSurvey) {
						AmpAhsurvey ahsvy = new AmpAhsurvey();
						ahsvy.setAmpActivityId(activity);
						ahsvy.setAmpDonorOrgId(ampFund.getAmpDonorOrgId());
						activity.getSurvey().add(ahsvy);
						surveySetEmpty = false;
					}
					newSurvey = true;
				}

				iter2 = surveySet.iterator();
				while (iter2.hasNext()) {
					AmpAhsurvey ahs = (AmpAhsurvey) iter2.next();
					if (ahs.getPointOfDeliveryDonor() == null) {
						ahs.setPointOfDeliveryDonor(ahs.getAmpDonorOrgId());
					}
				}
				session.update(activity);
				// tx.commit();

				if (activity.getSurvey().isEmpty())
					logger.debug("activity.getSurvey() is empty.");
				else {
					// logger.debug("activity.getSurvey().size() : " +
					// activity.getSurvey().size());
					// logger.debug("donorOrgs.size() : " + donorOrgs.size());
					iter2 = activity.getSurvey().iterator();
					while (iter2.hasNext()) {
						AmpAhsurvey svy = (AmpAhsurvey) iter2.next();
						// getting only those survey records where donor-org is
						// in current funding list
						if (donorOrgs.indexOf(svy.getAmpDonorOrgId()) != -1) {
							SurveyFunding svfund = new SurveyFunding();
							svfund.setSurveyId(svy.getAmpAHSurveyId());
							svfund.setFundingOrgName(svy.getAmpDonorOrgId()
									.getName());
							if (svy.getPointOfDeliveryDonor() != null) {
								svfund.setDeliveryDonorName(svy
										.getPointOfDeliveryDonor().getName());
								svfund.setAcronim(svy.getPointOfDeliveryDonor()
										.getAcronym());
							} else {
								svfund.setDeliveryDonorName(svy
										.getAmpDonorOrgId().getName());
								svfund.setAcronim(svy.getAmpDonorOrgId()
										.getAcronym());
							}

							survey.add(svfund);
						}
					}
				}
			}
		} catch (Exception ex) {
			if (tx != null) {
				try {
					tx.rollback();
				} catch (HibernateException e) {
					logger.debug("rollback() failed : " + e.getMessage());
				}
			}
			logger.debug("Unable to get survey : " + ex.getMessage());
			ex.printStackTrace(System.out);
		}
		logger.debug("survey.size() : " + survey.size());
		return survey;
	}

	public static List getResposesBySurvey(Long surveyId, Long activityId) {
		ArrayList responses = new ArrayList();
		List response = new ArrayList();
		Collection fundingSet = new ArrayList();
		Session session = null;
		Iterator iter1 = null;
		boolean flag = true;

		try {
			session = PersistenceManager.getRequestDBSession();
			String qry = "select indc from "
					+ AmpAhsurveyIndicator.class.getName()
					+ " indc order by indicator_number asc";
			Collection indicatorColl = session.createQuery(qry).list();

			AmpAhsurvey svy = (AmpAhsurvey) session.get(AmpAhsurvey.class,
					surveyId);
			// response = svy.getResponses();
			/*
			 * qry = "select res from " + AmpAhsurvey.class.getName() +
			 * " res where (res.ampAHSurveyId=:surveyId)"; Query query =
			 * session.createQuery(qry); query.setParameter("surveyId",
			 * surveyId, Hibernate.LONG); response = ( (AmpAhsurvey)
			 * query.list().get(0)).getResponses();
			 */

			// TODO: The whole logic for saving the first survey data and future
			// retrieving must be redone.
			// This query is necesary because of the lazy="false" which is
			// necesary because the way the PI reports are created.
			qry = "select resp from " + AmpAhsurveyResponse.class.getName()
					+ " resp where resp.ampAHSurveyId=:surveyId";
			Query query = session.createQuery(qry);
			query.setParameter("surveyId", surveyId, Hibernate.LONG);
			response = query.list();

			qry = "select fund from "
					+ AmpFunding.class.getName()
					+ " fund where (fund.ampDonorOrgId=:donorId) and (fund.ampActivityId=:activityId)";
			query = session.createQuery(qry);
			query.setParameter("donorId", svy.getAmpDonorOrgId().getAmpOrgId(),
					Hibernate.LONG);
			query.setParameter("activityId", svy.getAmpActivityId()
					.getAmpActivityId(), Hibernate.LONG);
			fundingSet = query.list();

			if (response.size() < 1) // new survey
				flag = false;
			iter1 = indicatorColl.iterator();
			Iterator iter2 = null;
			boolean ansFlag = false;
			while (iter1.hasNext()) {
				AmpAhsurveyIndicator indc = (AmpAhsurveyIndicator) iter1.next();
				Indicator ind = new Indicator();
				ind.setIndicatorCode(indc.getIndicatorCode());
				ind.setName(indc.getName());
				ind.setQuestion(new ArrayList());
				iter2 = session
						.createFilter(indc.getQuestions(),
								"order by this.questionNumber asc").list()
						.iterator();
				// iter2 = session.createFilter(((AmpAhsurveyIndicator)
				// session.load(AmpAhsurveyIndicator.class,
				// indc.getAmpIndicatorId())).getQuestions(),
				// "order by this.questionNumber asc").list().iterator();
				Iterator iter3 = null;
				while (iter2.hasNext()) {
					AmpAhsurveyQuestion q = (AmpAhsurveyQuestion) iter2.next();
					Question ques = new Question();
					ques.setQuestionType(q.getAmpTypeId().getName());
					ques.setQuestionId(q.getAmpQuestionId());
					ques.setQuestionText(q.getQuestionText());
					if (flag) { // response is blank in case of new survey
						iter3 = response.iterator();
						while (iter3.hasNext()) {
							AmpAhsurveyResponse res = (AmpAhsurveyResponse) iter3
									.next();
							if (res.getAmpQuestionId().getAmpQuestionId()
									.equals(q.getAmpQuestionId())) {
								if (q.getQuestionNumber().intValue() == 1) {
									if ("yes".equalsIgnoreCase(res
											.getResponse()))
										ansFlag = true;
								}

								/*
								 * -------------------------------- Defunct now
								 * -------------------------------------
								 */
								// if answer to question #1 of survey is yes
								// then calculate
								// difference(%) between planned & actual
								// disbursement(s)
								if ("calculated".equalsIgnoreCase(q
										.getAmpTypeId().getName())) {
									if (q.getQuestionNumber().intValue() == 10) {
										if (ansFlag) {
											Iterator itr4 = fundingSet
													.iterator();
											Iterator itr5 = null;
											double actual = 0.0;
											double planned = 0.0;
											AmpFundingDetail fd = null;
											while (itr4.hasNext()) {
												AmpFunding ampf = (AmpFunding) itr4
														.next();
												itr5 = ampf.getFundingDetails()
														.iterator();
												while (itr5.hasNext()) {
													fd = (AmpFundingDetail) itr5
															.next();
													if (fd.getTransactionType()
															.intValue() == 1) {
														if (fd.getAdjustmentType()
																.getValue()
																.equals(CategoryConstants.ADJUSTMENT_TYPE_PLANNED
																		.getValueKey()))
															planned += fd
																	.getTransactionAmount()
																	.floatValue();
														else if (fd
																.getAdjustmentType()
																.getValue()
																.equals(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL
																		.getValueKey()))
															actual += fd
																	.getTransactionAmount()
																	.floatValue();
													}
												}
											}

											if (planned == 0.0)
												res.setResponse("nil");
											else {
												NumberFormat formatter = new DecimalFormat(
														"#.##");
												Double percent = new Double(
														(actual * 100)
																/ planned);
												res.setResponse(formatter
														.format(percent));
											}
										} else
											res.setResponse(null);
									}
								}
								/*
								 * -------------------------------- Defunct now
								 * -------------------------------------
								 */

								ques.setResponse(res.getResponse());
								ques.setResponseId(res.getAmpReponseId());
								break;
							}
						}
					}
					ind.getQuestion().add(ques);
				}
				responses.add(ind);
			}
		} catch (Exception ex) {
			logger.error("Unable to get survey responses : ", ex);
		}
		logger.debug("responses.size() : " + responses.size());
		return responses;
	}

	public static AmpAhsurvey getAhSurvey(Long surveyId) {
		AmpAhsurvey survey = new AmpAhsurvey();
		Session session = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String qry = "select svy from " + AmpAhsurvey.class.getName()
					+ " svy where (svy.ampAHSurveyId=:surveyId)";
			Query q = session.createQuery(qry);
			q.setParameter("surveyId", surveyId, Hibernate.LONG);
			survey = (AmpAhsurvey) q.list().get(0);
		} catch (Exception ex) {
			logger.debug("Unable to get survey : ", ex);
		}
		return survey;
	}

	public static void updateSurvey(AmpAhsurvey survey,
			AmpActivityVersion activity) {
		Session session = null;
		Transaction tx = null;

		try {
			if (survey == null || survey.getAmpAHSurveyId() == null) {
				logger.debug("The survey or AHSurvey is null ... no update for Survey");
				return;
			}
			session = PersistenceManager.getRequestDBSession();
			// beginTransaction();

			/*
			 * AmpAhsurvey oldSurvey ; oldSurvey = (AmpAhsurvey)
			 * session.load(AmpAhsurvey.class, survey.getAmpAHSurveyId());
			 * oldSurvey.setAmpActivityId(survey.getAmpActivityId());
			 * oldSurvey.setAmpDonorOrgId(survey.getAmpDonorOrgId());
			 * oldSurvey.setPointOfDeliveryDonor
			 * (survey.getPointOfDeliveryDonor());
			 * oldSurvey.setResponses(survey.getResponses());
			 * 
			 * session.update(oldSurvey);
			 */

			if (survey.getAmpActivityId() == null) {
				survey.setAmpActivityId(activity);
			}

			// With lazy="false" this is how it works ok.
			session.update(survey);// try saveOrUpdate() if this doesnt work.

			// tx.commit();
		} catch (Exception ex) {
			if (tx != null) {
				try {
					tx.rollback();
				} catch (HibernateException e) {
					logger.error("rollback() failed : ", e);
				}
			}
			logger.error("Unable to save survey response : ", ex);
		}
	}

	public static void saveNewSurvey(AmpAhsurvey survey,
			AmpActivityVersion activity) throws DgException {
		Session session = PersistenceManager.getRequestDBSession();

		AmpAhsurvey newSurvey = new AmpAhsurvey();
		newSurvey.setAmpActivityId(activity);
		newSurvey.setAmpAHSurveyId(null);
		newSurvey.setAmpDonorOrgId(survey.getAmpDonorOrgId());
		newSurvey.setPointOfDeliveryDonor(survey.getPointOfDeliveryDonor());
		newSurvey.setResponses(new HashSet<AmpAhsurveyResponse>());
		Iterator iterResponses = survey.getResponses().iterator();
		while (iterResponses.hasNext()) {
			AmpAhsurveyResponse res = (AmpAhsurveyResponse) iterResponses
					.next();
			AmpAhsurveyResponse newResponse = new AmpAhsurveyResponse();
			newResponse.setAmpAHSurveyId(newSurvey);
			newResponse.setAmpQuestionId(res.getAmpQuestionId());
			newResponse.setAmpReponseId(null);
			newResponse.setResponse(res.getResponse());
			// res.setAmpAHSurveyId(newSurvey);
			newSurvey.getResponses().add(newResponse);
			// session.saveOrUpdate(newResponse);
		}
		if (activity.getSurvey() == null) {
			activity.setSurvey(new HashSet<AmpAhsurvey>());
		}
		activity.getSurvey().add(newSurvey);
		// session.saveOrUpdate(newSurvey);
	}

	public static void saveNewSurvey(AmpAhsurvey survey,
			AmpActivityVersion activity, List<Indicator> indicators)
			throws DgException {
		Session session = null;
		if (survey == null /* || survey.getAmpAHSurveyId()==null */) {
			logger.warn("The survey or AHSurvey is null ... no update for Survey");
			return;
		}
		// setup the survey.
		survey.setAmpActivityId(activity);
		if (activity.getSurvey() == null) {
			activity.setSurvey(new HashSet<AmpAhsurvey>());
		}
		activity.getSurvey().add(survey);
		survey.setAmpAHSurveyId(null);

		// setup responses.
		Iterator itr1 = indicators.iterator();
		while (itr1.hasNext()) {
			Iterator itr2 = ((Indicator) itr1.next()).getQuestion().iterator();
			while (itr2.hasNext()) {
				Question q = (Question) itr2.next();
				AmpAhsurveyResponse res = new AmpAhsurveyResponse();
				res.setAmpAHSurveyId(survey);
				session = PersistenceManager.getRequestDBSession();
				AmpAhsurveyQuestion ques = (AmpAhsurveyQuestion) session.load(
						AmpAhsurveyQuestion.class, q.getQuestionId());
				res.setAmpQuestionId(ques);
				res.setResponse(q.getResponse());
				survey.getResponses().add(res);
			}
		}
	}

	public static void saveSurveyResponses(Long surveyId, Collection indicator) {
		Session session = null;
		Transaction tx = null;
		Iterator itr1 = null;
		Iterator itr2 = null;
		boolean flag = true;
		if (surveyId == null) {
			logger.debug("The survey id is null ... no save survey response");
			return;
		}
		try {
			session = PersistenceManager.getRequestDBSession();
			// beginTransaction();

			AmpAhsurvey survey = (AmpAhsurvey) session.get(AmpAhsurvey.class,
					surveyId);
			String qry = "select count(*) from "
					+ AmpAhsurveyResponse.class.getName()
					+ " res where (res.ampAHSurveyId=:surveyId)";
			Integer resposeSize = (Integer) session.createQuery(qry)
					.setParameter("surveyId", surveyId, Hibernate.LONG)
					.uniqueResult();
			// logger.debug("Response size : " + resposeSize.intValue());
			if (resposeSize.intValue() < 1) {
				flag = false;
				logger.debug("Response set is empty");
			}

			itr1 = indicator.iterator();
			while (itr1.hasNext()) {
				itr2 = ((Indicator) itr1.next()).getQuestion().iterator();
				while (itr2.hasNext()) {
					Question q = (Question) itr2.next();
					AmpAhsurveyResponse res = new AmpAhsurveyResponse();
					if (flag)
						// res.setAmpReponseId(q.getResponseId());
						res = (AmpAhsurveyResponse) session.load(
								AmpAhsurveyResponse.class, q.getResponseId());
					res.setAmpAHSurveyId(survey);
					AmpAhsurveyQuestion ques = (AmpAhsurveyQuestion) session
							.load(AmpAhsurveyQuestion.class, q.getQuestionId());
					res.setAmpQuestionId(ques);
					res.setResponse(q.getResponse());
					session.saveOrUpdate(res);
				}
			}
			// tx.commit();
		} catch (Exception ex) {
			if (tx != null) {
				try {
					tx.rollback();
				} catch (HibernateException e) {
					logger.error("rollback() failed : ", e);
				}
			}
			logger.error("Unable to save survey response : ", ex);
		}
	}

	public static Collection<AmpAhsurveyIndicator> getAllAhSurveyIndicators() {
		Collection responses = new ArrayList();
		Session session = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String qry = "select indc from "
					+ AmpAhsurveyIndicator.class.getName()
					+ " indc order by indicator_number asc";
			responses = session.createQuery(qry).list();

		} catch (Exception ex) {
			logger.error("Unable to get survey indicators : ", ex);
		}
		return responses;
	}

	public static AmpAhsurveyIndicator getIndicatorById(Long id) {
		AmpAhsurveyIndicator indc = new AmpAhsurveyIndicator();
		Session session = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String qry = "select indc from "
					+ AmpAhsurveyIndicator.class.getName()
					+ " indc where (indc.ampIndicatorId=:id)";
			indc = (AmpAhsurveyIndicator) session.createQuery(qry)
					.setParameter("id", id, Hibernate.LONG).list().get(0);

		} catch (Exception ex) {
			logger.debug("Unable to get survey indicator : " + ex.getMessage());
			ex.printStackTrace(System.out);
		}
		return indc;
	}
	
	public static Collection<AmpGPISurveyIndicator> getAllGPISurveyIndicators(boolean onlyWithQuestions) {
		Collection responses = new ArrayList();
		Session session = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String qry = "select indc from " + AmpGPISurveyIndicator.class.getName() + " indc ";
			if(onlyWithQuestions) {
				qry += " where total_question > 0 ";
			}
			qry += " order by indicator_code asc";
			responses = session.createQuery(qry).list();

		} catch (Exception ex) {
			logger.error("Unable to get survey indicators : ", ex);
		}
		return responses;
	}

	public static AmpGPISurveyIndicator getGPIIndicatorById(Long id) {
		AmpGPISurveyIndicator indc = new AmpGPISurveyIndicator();
		Session session = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String qry = "select indc from "
					+ AmpGPISurveyIndicator.class.getName()
					+ " indc where (indc.ampIndicatorId=:id)";
			indc = (AmpGPISurveyIndicator) session.createQuery(qry)
					.setParameter("id", id, Hibernate.LONG).list().get(0);

		} catch (Exception ex) {
			logger.debug("Unable to get GPI survey indicator : " + ex.getMessage());
			ex.printStackTrace(System.out);
		}
		return indc;
	}

//	/*
//	 * Methods called to retrieve data that have to be deleted while an activity
//	 * is deleted by Admin start here
//	 */
//	/* get amp comments of a particular activity specified by ampActId */
//	public static Collection getActivityAmpComments(Long ampActId) {
//		Session session = null;
//		Collection col = null;
//		Query qry = null;
//		try {
//			session = PersistenceManager.getRequestDBSession();
//			String queryString = "select com from "
//					+ AmpComments.class.getName() + " com "
//					+ " where (com.ampActivityId=:ampActId)";
//			qry = session.createQuery(queryString);
//			qry.setParameter("ampActId", ampActId, Hibernate.LONG);
//			col = qry.list();
//		} catch (Exception e1) {
//			logger.error("could not retrieve AmpComments " + e1.getMessage());
//			e1.printStackTrace(System.out);
//		}
//		return col;
//	}

	/*
	 * get ampActivity physical component report of a particular activity
	 * specified by ampActId
	 */
	public static Collection getActivityPhysicalComponentReport(Long ampActId) {
		Session session = null;
		Collection col = null;
		Query qry = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select phyCompReport from "
					+ AmpPhysicalComponentReport.class.getName()
					+ " phyCompReport "
					+ " where (phyCompReport.ampActivityId=:ampActId)";
			qry = session.createQuery(queryString);
			qry.setParameter("ampActId", ampActId, Hibernate.LONG);
			col = qry.list();
		} catch (Exception e1) {
			logger.error("could not retrieve AmpPhysicalComponentReport "
					+ e1.getMessage());
			e1.printStackTrace(System.out);
		}
		return col;
	}

	/* get amp report cache of a particular activity specified by ampActId */
	public static Collection getActivityReportCache(Long ampActId) {
		Session session = null;
		Collection col = null;
		Query qry = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select repCache from "
					+ AmpReportCache.class.getName() + " repCache "
					+ " where (repCache.ampActivityId=:ampActId)";
			qry = session.createQuery(queryString);
			qry.setParameter("ampActId", ampActId, Hibernate.LONG);
			col = qry.list();
		} catch (Exception e1) {
			logger.error("could not retrieve AmpReportCache " + e1.getMessage());
			e1.printStackTrace(System.out);
		}
		return col;
	}

	/* get amp report location of a particular activity specified by ampActId */
	public static Collection getActivityReportLocation(Long ampActId) {
		Session session = null;
		Collection col = null;
		Query qry = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select repLoc from "
					+ AmpReportLocation.class.getName() + " repLoc "
					+ " where (repLoc.ampActivityId=:ampActId)";
			qry = session.createQuery(queryString);
			qry.setParameter("ampActId", ampActId, Hibernate.LONG);
			col = qry.list();
		} catch (Exception e1) {
			logger.error("could not retrieve AmpReportLocation "
					+ e1.getMessage());
			e1.printStackTrace(System.out);
		}
		return col;
	}

	/*
	 * get amp activity report physical performance of a particular activity
	 * specified by ampActId
	 */
	public static Collection getActivityRepPhyPerformance(Long ampActId) {
		Session session = null;
		Collection col = null;
		Query qry = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select phyPer from "
					+ AmpReportPhysicalPerformance.class.getName() + " phyPer "
					+ " where (phyPer.ampActivityId=:ampActId)";
			qry = session.createQuery(queryString);
			qry.setParameter("ampActId", ampActId, Hibernate.LONG);
			col = qry.list();
		} catch (Exception e1) {
			logger.error("could not retrieve AmpReportPhysicalPerformance "
					+ e1.getMessage());
			e1.printStackTrace(System.out);
		}
		return col;
	}

	/*
	 * get amp activity report sector of a particular activity specified by
	 * ampActId
	 */
	public static Collection getActivityReportSector(Long ampActId) {
		Session session = null;
		Collection col = null;
		Query qry = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select repSector from "
					+ AmpReportSector.class.getName() + " repSector "
					+ " where (repSector.ampActivityId=:ampActId)";
			qry = session.createQuery(queryString);
			qry.setParameter("ampActId", ampActId, Hibernate.LONG);
			col = qry.list();
		} catch (Exception e1) {
			logger.error("could not retrieve AmpReportSector "
					+ e1.getMessage());
			e1.printStackTrace(System.out);
		}
		return col;
	}

	/* get amp ME indicator value of a particular activity specified by ampActId */
	public static Collection getActivityMEIndValue(Long ampActId) {
    	Session session = null;
        Collection col = null;
        Query qry = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select indAct from "
                + IndicatorActivity.class.getName()
                + " indAct "
                + " where (indAct.activity=:ampActId)";
            qry = session.createQuery(queryString);
            qry.setParameter("ampActId", ampActId, Hibernate.LONG);
            col = qry.list();
        } catch (Exception e1) {
            logger.error("could not retrieve AmpReportSector " + e1.getMessage());
            e1.printStackTrace(System.out);
        }
        return col;
		/*try {

			Collection<IndicatorActivity> activityInd = null;
			AmpActivityVersion activity = ActivityUtil.loadActivity(ampActId);
			if (activity != null) {
				activityInd = activity.getIndicators();
			}
			return activityInd;
		} catch (Exception e) {
			logger.info("Couldn't get activity to delete indicators...");
			return null;
		}
		 */
	}

	/*
	 * Methods called to retrieve data that have to be deleted while an activity
	 * is deleted by Admin end here
	 */

	/*
	 * To check for Status code modified by Govind
	 */
	public static Collection getStatusCodes() {
		logger.info(" in getting the Status codes...");
		Session session = null;
		Collection col = null;
		Query qry = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select st from " + AmpStatus.class.getName()
					+ " st ";
			qry = session.createQuery(queryString);
			col = qry.list();
		} catch (Exception e1) {
			logger.error("could not retrieve Statuc codes... "
					+ e1.getMessage());
			e1.printStackTrace(System.out);
		}
		return col;
	}

	/**
	 * Returns LabelValueBean's for all AmpStatus entities. Status code is used
	 * as value and Status name as Label. Used in HTML select dropdowns and
	 * lists.
	 * 
	 * @return List of LabelValeBean objects
	 * @throws AimException
	 *             if any error happens.
	 * @author Irakli Kobiashvili
	 */
	public static List getAllAmpStatusesLVB() throws AimException {
		try {
			Session session = PersistenceManager.getRequestDBSession();
			String queryString = "from " + AmpStatus.class.getName();
			Query qry = session.createQuery(queryString);
			Collection col = qry.list();
			List result = new ArrayList(col.size());
			for (Iterator iter = col.iterator(); iter.hasNext();) {
				AmpStatus status = (AmpStatus) iter.next();
				LabelValueBean lvb = new LabelValueBean(status.getName(),
						status.getStatusCode());
				result.add(lvb);
			}
			return result;
		} catch (Exception e1) {
			logger.error("could not retrieve Statuses " + e1.getMessage());
			throw new AimException(e1);
		}
	}

	public static Collection<CountryBean> getTranlatedCountries(
			HttpServletRequest request) {
		Collection<CountryBean> trnCnCol = null;
		org.digijava.kernel.entity.Locale navLang = RequestUtils
				.getNavigationLanguage(request);

		try {
			trnCnCol = new ArrayList<CountryBean>();
			Collection<Country> cnCol = FeaturesUtil.getAllDgCountries();
			if (cnCol != null && cnCol.size() != 0) {
				for (Iterator cnIter = cnCol.iterator(); cnIter.hasNext();) {
					Country cn = (Country) cnIter.next();
					cn = getTranlatedCountry(request, cn);

					CountryBean trnCn = new CountryBean();
					trnCn.setId(cn.getCountryId());
					trnCn.setIso(cn.getIso());
					trnCn.setIso3(cn.getIso3());
					trnCn.setName(cn.getCountryName());
					trnCnCol.add(trnCn);
				}

			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		if (trnCnCol != null && trnCnCol.size() != 0) {
			List<CountryBean> sortedCountrieList = new ArrayList<CountryBean>(
					trnCnCol);
			Collections.sort(sortedCountrieList,
					new HelperTrnCountryNameComparator(navLang.getCode()));
			return sortedCountrieList;
		} else {
			return null;
		}
	}

	public static Country getTranlatedCountry(HttpServletRequest request,
			Country country) {
		Session session = null;
		Collection msgCol = null;
		Query qry = null;

		org.digijava.kernel.entity.Locale navLang = RequestUtils
				.getNavigationLanguage(request);
		Site site = RequestUtils.getSite(request);

		try {
			if (country != null) {
				session = PersistenceManager.getRequestDBSession();
				String queryString = "select msg "
						+ " from "
						+ Message.class.getName()
						+ " msg"
						+ " where (msg.key=:msgLangKey) and (msg.siteId=:siteId) and (msg.locale=:locale)";

				qry = session.createQuery(queryString);
				qry.setParameter("siteId", site.getId().toString(),
						Hibernate.STRING);
				qry.setParameter("locale", navLang.getCode(), Hibernate.STRING);
				qry.setParameter("msgLangKey", country.getMessageLangKey(),
						Hibernate.STRING);
				msgCol = qry.list();

				if (msgCol != null && msgCol.size() != 0) {
					for (Iterator msgIter = msgCol.iterator(); msgIter
							.hasNext();) {
						Message msg = (Message) msgIter.next();
						if (msg != null) {
							country.setCountryName(msg.getMessage());
							break;
						}
					}
				}
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		return country;
	}

	public static void deleteUserExt(AmpOrgGroup orgGrp, AmpOrgType orgType,
			AmpOrganisation org) {
		Session session = null;
		Query query;
		if (orgGrp != null || orgType != null || org != null) {
			try {
				Object relatedObj = null;
				session = PersistenceManager.getRequestDBSession();
				String qhl = "delete from " + AmpUserExtension.class.getName()
						+ " ext where ";
				if (orgGrp != null) {
					qhl += " ext.orgGroup=:relatedObj";
					relatedObj = orgGrp;
				}
				if (orgType != null) {
					qhl += " ext.orgType=:relatedObj";
					relatedObj = orgType;
				}
				if (org != null) {
					qhl += " ext.organization=:relatedObj";
					relatedObj = org;
				}
				query = session.createQuery(qhl);
				query.setEntity("relatedObj", relatedObj);
				query.executeUpdate();
			} catch (Exception e) {
				logger.error("Delete Failed: " + e.toString());
			}
		}
	}

	public static class HelperUserNameComparator implements Comparator {
		private Order order;

		public HelperUserNameComparator(Order order) {
			this.order = order;

		}

		public int compare(Object obj1, Object obj2) {
			User user1 = (User) obj1;
			User user2 = (User) obj2;
			int result = user1.getName().compareToIgnoreCase(user2.getName());
			if (Order.DESC.equals(order)) {
				result *= -1;
			}
			return result;
		}
	}

	/**
	 * //for sorting users by Email
	 * 
	 * @author dare
	 * 
	 */
	public static class HelperEmailComparator implements Comparator {
		private Order order;

		public HelperEmailComparator(Order order) {
			this.order = order;

		}

		public int compare(Object obj1, Object obj2) {
			User user1 = (User) obj1;
			User user2 = (User) obj2;
			int result = user1.getEmail().compareToIgnoreCase(user2.getEmail());
			if (Order.DESC.equals(order)) {
				result *= -1;
			}
			return result;
		}
	}

	public static class HelperTrnCountryNameComparator implements
			Comparator<CountryBean> {
		Locale locale;
		Collator collator;

		public HelperTrnCountryNameComparator() {
			this.locale = new Locale("en", "EN");
		}

		public HelperTrnCountryNameComparator(String iso) {
			this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
		}

		public int compare(CountryBean o1, CountryBean o2) {
			collator = Collator.getInstance(locale);
			collator.setStrength(Collator.TERTIARY);

			int result = collator.compare(o1.getName(), o2.getName());
			return result;
		}
	}

	public static class HelperAmpOrgGroupNameComparator implements
			Comparator<AmpOrgGroup> {
		Locale locale;
		Collator collator;

		public HelperAmpOrgGroupNameComparator() {
			this.locale = new Locale("en", "EN");
		}

		public HelperAmpOrgGroupNameComparator(String iso) {
			this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
		}

		public int compare(AmpOrgGroup o1, AmpOrgGroup o2) {
			if (collator == null)
			{
				collator = Collator.getInstance(locale);
				collator.setStrength(Collator.TERTIARY);
			}

			int result = collator.compare(o1.getOrgGrpName(), o2.getOrgGrpName());
			return result;
		}
	}

    public static class HelperAmpSectorNameComparator implements
    	Comparator<AmpSector> {
 	 	 	 	Locale locale;
 	 	 	 	Collator collator;
 	 	 	 	public HelperAmpSectorNameComparator() {
 	 	 	 		this.locale = new Locale("en", "EN");
 	 	 	 	}
 	 	 	 	
 	 	 	 	public HelperAmpSectorNameComparator(String iso) {
 	 	 	 		this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
 	 	 	 	}
 	 	 	 	
 	 	 	 	@Override
 	 	 	 	public int compare(AmpSector o1, AmpSector o2) {
 	 	 	 		if (collator == null)
 	 	 	 			{
 	 	 	 				collator = Collator.getInstance(locale);
 	 	 	 				collator.setStrength(Collator.TERTIARY);
 	 	 	 			}
 	 	 	 		int result = collator.compare(o1.getName(), o2.getName());
 	 	 	 		return result;
 	 	 	 	}
 	 	 	 }       	

	/**
	 * This class is used for sorting AmpOrgGroup by code.
	 * 
	 * @author Dare Roinishvili
	 * 
	 */
	public static class HelperAmpOrgGroupCodeComparator implements
			Comparator<AmpOrgGroup> {
		Locale locale;
		Collator collator;

		public HelperAmpOrgGroupCodeComparator() {
			this.locale = new Locale("en", "EN");
		}

		public HelperAmpOrgGroupCodeComparator(String iso) {
			this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
		}

		public int compare(AmpOrgGroup o1, AmpOrgGroup o2) {
			collator = Collator.getInstance(locale);
			collator.setStrength(Collator.TERTIARY);

			int result = (o1.getOrgGrpCode() != null && o2.getOrgGrpCode() != null) ? collator
					.compare(o1.getOrgGrpCode(), o2.getOrgGrpCode()) : 0;
			return result;
		}
	}

	/**
	 * This class is used for sorting AmpOrgGroup by Type.
	 * 
	 * @author Dare Roinishvili
	 * 
	 */
	public static class HelperAmpOrgGroupTypeComparator implements
			Comparator<AmpOrgGroup> {
		public int compare(AmpOrgGroup o1, AmpOrgGroup o2) {
			AmpOrgType o1Type = o1.getOrgType();
			AmpOrgType o2Type = o2.getOrgType();
			return new HelperAmpOrgTypeNameComparator().compare(o1Type, o2Type);
		}
	}

	public static class HelperAmpOrgTypeNameComparator implements
			Comparator<AmpOrgType> {
		Locale locale;
		Collator collator;

		public HelperAmpOrgTypeNameComparator() {
			this.locale = new Locale("en", "EN");
		}

		public HelperAmpOrgTypeNameComparator(String iso) {
			this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
		}

		public int compare(AmpOrgType o1, AmpOrgType o2) {
			collator = Collator.getInstance(locale);
			collator.setStrength(Collator.TERTIARY);

			int result = collator.compare(o1.getOrgType(), o2.getOrgType());
			return result;
		}
	}

	/**
	 * This class is used for sorting organisations by name.
	 * 
	 * @author Dare Roinishvili
	 * 
	 */
	public static class HelperAmpOrganisationNameComparator implements
			Comparator<AmpOrganisation> {
		Locale locale;
		Collator collator;

		public HelperAmpOrganisationNameComparator() {
			this.locale = new Locale("en", "EN");
		}

		public HelperAmpOrganisationNameComparator(String iso) {
			this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
		}

		public int compare(AmpOrganisation o1, AmpOrganisation o2) {
			collator = Collator.getInstance(locale);
			collator.setStrength(Collator.TERTIARY);

			int result = (o1.getName() == null || o2.getName() == null) ? 0
					: collator.compare(o1.getName().toLowerCase(), o2.getName()
							.toLowerCase());
			return result;
		}
	}

	/**
	 * This class is used for soring organisations by acronym.
	 * 
	 * @author Dare Roinishvili
	 * 
	 */
	public static class HelperAmpOrganisatonAcronymComparator implements
			Comparator<AmpOrganisation> {
		Locale locale;
		Collator collator;

		public HelperAmpOrganisatonAcronymComparator() {
			this.locale = new Locale("en", "EN");
		}

		public HelperAmpOrganisatonAcronymComparator(String iso) {
			this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
		}

		public int compare(AmpOrganisation o1, AmpOrganisation o2) {

			int result = 0;
			collator = Collator.getInstance(locale);
			collator.setStrength(Collator.TERTIARY);

			if (o1.getAcronym() != null && o2.getAcronym() != null) {
				result = collator.compare(o1.getAcronym(), o2.getAcronym());
			} else if (o1.getAcronym() == null && o2.getAcronym() == null) {
				result = 0;
			} else if (o1.getAcronym() == null) {
				result = collator.compare("", o2.getAcronym());
			} else if (o2.getAcronym() == null) {
				result = collator.compare(o1.getAcronym(), "");
			}
			return result;
		}

	}

	/**
	 * This class is used for sorting organisation by group. such long and
	 * complicated case is necessary because orgGroup maybe empty for
	 * organisation
	 * 
	 * @author Dare Roinishvili
	 * 
	 */
	public static class HelperAmpOrganisationGroupComparator implements
			Comparator<AmpOrganisation> {
		Locale locale;
		Collator collator;

		public HelperAmpOrganisationGroupComparator() {
			this.locale = new Locale("en", "EN");
		}

		public HelperAmpOrganisationGroupComparator(String iso) {
			this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
		}

		public int compare(AmpOrganisation o1, AmpOrganisation o2) {
			collator = Collator.getInstance(locale);
			collator.setStrength(Collator.TERTIARY);
			int result = 0;
			// such long and complicated case is necessary because orgType maybe
			// empty for organisation

			AmpOrgGroup oo1=o1.getOrgGrpId();
			AmpOrgGroup oo2=o2.getOrgGrpId();
			if(oo1!=null && oo2!=null){
			AmpOrgType orgType1 = oo1.getOrgType();
			AmpOrgType orgType2 = oo2.getOrgType();
			
			if (orgType1 != null && orgType2 != null) {
				result = new HelperAmpOrgTypeNameComparator().compare(orgType1,
						orgType2);
			} else if (orgType2 == null && orgType1 != null) {
				result = collator.compare(orgType1.getOrgType(), "");
			} else if (orgType1 == null && orgType2 != null) {
				result = collator.compare("", orgType2.getOrgType());
			}
			}else{
				if(oo1==null && oo2!=null){
					AmpOrgType orgType2 = oo2.getOrgType();
					result = collator.compare("", orgType2.getOrgType());
				}else{
					if(oo2==null && oo1!=null){
						AmpOrgType orgType1 = oo1.getOrgType();
						result = collator.compare(orgType1.getOrgType(), "");
					}
				}
			}
			return result;

		}
	}


	/**
	 * This class is used for sorting organisation by Type. such long and
	 * complicated case is necessary because orgType maybe empty for
	 * organisation
	 * 
	 * @author Dare Roinisvili
	 * 
	 */
	public static class HelperAmpOrganisationTypeComparator implements
			Comparator<AmpOrganisation> {
		Locale locale;
		Collator collator;

		public HelperAmpOrganisationTypeComparator() {
			this.locale = new Locale("en", "EN");
		}

		public HelperAmpOrganisationTypeComparator(String iso) {
			this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
		}

		public int compare(AmpOrganisation o1, AmpOrganisation o2) {
			collator = Collator.getInstance(locale);
			collator.setStrength(Collator.TERTIARY);
			int result = 0;
			// such long and complicated case is necessary because orgType maybe
			// empty for organisation
			AmpOrgType orgType1 = o1.getOrgGrpId().getOrgType();
			AmpOrgType orgType2 = o2.getOrgGrpId().getOrgType();
			if (orgType1 != null && orgType2 != null) {
				result = new HelperAmpOrgTypeNameComparator().compare(orgType1,
						orgType2);
			} else if (orgType2 == null && orgType1 != null) {
				result = collator.compare(orgType1.getOrgType(), "");
			} else if (orgType1 == null && orgType2 != null) {
				result = collator.compare("", orgType2.getOrgType());
			}
			return result;

		}
	}

	public static AmpOrganisation getOrganisationByName(String name) {
		AmpOrganisation obResult = null;
		Session sess = null;
		Query qry = null;
		String queryString = null;

		try {
			sess = PersistenceManager.getRequestDBSession();
			String orgName = AmpOrganisation.hqlStringForName("o");
			queryString = "select o from "
					+ AmpOrganisation.class.getName()
					+ " o where (TRIM(" + orgName + ")=:orgName) and (o.deleted is null or o.deleted = false) ";
			qry = sess.createQuery(queryString);
			qry.setString("orgName", name);

			List result = qry.list();
			if (result.size() > 0) {
				obResult = (AmpOrganisation) result.get(0);
			}
			// System.out.println("DBUTIL.GETORGANISATIONBYNAME() : " +
			// qry.getQueryString());
		} catch (Exception e) {
			logger.debug("Exception from getOrganisationByName(): " + e);
			e.printStackTrace(System.out);
		}
		return obResult;
	}

	/**
	 * Compares Values by type(actual,base,target) Used in Multi Program Manager
	 * to sort them in order: base,actual,target of the same year
	 * 
	 * @author dare
	 * 
	 */
	public static class IndicatorValuesComparatorByTypeAndYear implements
			Comparator<AmpPrgIndicatorValue> {
		public int compare(AmpPrgIndicatorValue o1, AmpPrgIndicatorValue o2) {
			int retValue = 0;
			String o1Year = "";
			String o2Year = "";
			// getting year from creation date
			if (o1.getCreationDate() != null) {
				int length = o1.getCreationDate().length();
				o1Year = o1.getCreationDate().substring(length - 4, length);
			}
			if (o2.getCreationDate() != null) {
				int length = o2.getCreationDate().length();
				o2Year = o2.getCreationDate().substring(length - 4, length);
			}
			// o1's creation year is greater than o2's
			if (o1Year.compareTo(o2Year) == 1) {
				retValue = 1;
			} else if (o1Year.compareTo(o2Year) == -1) {// creation year of o1
														// is less than o2's
				retValue = -1;
			} else if (o1Year.compareTo(o2Year) == 0) { // creation years are
														// equal. So we have to
														// sort them in
														// order:base actual
														// target
				retValue = -(new Integer(o1.getValueType())
						.compareTo(new Integer(o2.getValueType())));
			}
			return retValue;
		}
	}

	public static class AmpIndicatorValuesComparatorByTypeAndYear implements
			Comparator<AmpIndicatorValue> {

		public int compare(AmpIndicatorValue o1, AmpIndicatorValue o2) {
			AmpPrgIndicatorValue val1 = new AmpPrgIndicatorValue();
			AmpPrgIndicatorValue val2 = new AmpPrgIndicatorValue();

			val1.setValueType(o1.getValueType());
			val1.setCreationDate(DateConversion.ConvertDateToString(o1
					.getValueDate()));

			val2.setValueType(o2.getValueType());
			val2.setCreationDate(DateConversion.ConvertDateToString(o2
					.getValueDate()));
			return new IndicatorValuesComparatorByTypeAndYear().compare(val1,
					val2);
		}

	}

	/**
	 * @param id
	 * @return
	 * @throws CMSException
	 */
	public static CMSContentItem getCMSContentItem(Long id) throws AimException {

		CMSContentItem item = null;
		Session session = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			item = (CMSContentItem) session.load(CMSContentItem.class, id);
		} catch (Exception ex) {
			logger.debug("Unable to get CMS Content Item from database", ex);
			throw new AimException(
					"Unable to get CMS Content Item from database", ex);
		}
		return item;
	}

	public static int getOrgTypesAmount(String name, Long groupId)
			throws Exception {
		Session sess = null;
		Query qry = null;
		int count = 0;
		try {
			sess = PersistenceManager.getRequestDBSession();
			String orgTypeName = AmpOrgType.hqlStringForName("o");
			String queryString = "select count(*) from "
					+ AmpOrgType.class.getName()
					+ " o where upper(" + orgTypeName + ") like upper('" + name + "')";
			if (groupId != null && groupId.longValue() != 0) {
				queryString += " and o.ampOrgTypeId!=" + groupId;
			}
			qry = sess.createQuery(queryString);
			count = ((Integer) qry.uniqueResult()).intValue();
		} catch (Exception e) {
			logger.error("Exception while getting org types amount:"
					+ e.getMessage());
		}
		return count;
	}

	public static int getOrgTypesByCode(String code, Long typeId)
			throws Exception {
		Session sess = null;
		Query qry = null;
		int count = 0;
		try {
			sess = PersistenceManager.getRequestDBSession();
			String queryString = "select count(*) from "
					+ AmpOrgType.class.getName()
					+ " o where upper(o.orgTypeCode) like upper('" + code
					+ "')";
			if (typeId != null && typeId.longValue() != 0) {
				queryString += " and o.ampOrgTypeId!=" + typeId;
			}
			qry = sess.createQuery(queryString);
			count = ((Integer) qry.uniqueResult()).intValue();
		} catch (Exception e) {
			logger.error("Exception while getting org types by code:"
					+ e.getMessage());
		}
		return count;
	}

	public static class HelperAmpOrgRecipientByOrgName implements
			Comparator<AmpOrgRecipient> {

		Locale locale;
		Collator collator;

		public HelperAmpOrgRecipientByOrgName() {
			this.locale = new Locale("en", "EN");
		}

		public HelperAmpOrgRecipientByOrgName(String iso) {
			this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
		}

		public int compare(AmpOrgRecipient o1, AmpOrgRecipient o2) {
			collator = Collator.getInstance(locale);
			collator.setStrength(Collator.TERTIARY);
			return collator.compare(o1.getOrganization().getName()
					.toLowerCase(), o2.getOrganization().getName()
					.toLowerCase());

		}
	}

	public static class HelperUserNameComparatorAsc implements Comparator {
		public int compare(Object obj1, Object obj2) {
			User user1 = (User) obj1;
			User user2 = (User) obj2;
			return user1.getName().trim()
					.compareToIgnoreCase(user2.getName().trim());
		}
	}

	public static class HelperUserNameComparatorDesc implements Comparator {
		public int compare(Object obj1, Object obj2) {
			User user1 = (User) obj1;
			User user2 = (User) obj2;
			return user2.getName().trim()
					.compareToIgnoreCase(user1.getName().trim());
		}
	}

	/**
	 * //for sorting users by Email
	 * 
	 * @author dare
	 * 
	 */
	public static class HelperEmailComparatorAsc implements Comparator {
		public int compare(Object obj1, Object obj2) {
			User user1 = (User) obj1;
			User user2 = (User) obj2;
			return user1.getEmail().compareTo(user2.getEmail());
		}
	}

	public static class HelperEmailComparatorDesc implements Comparator {
		public int compare(Object obj1, Object obj2) {
			User user1 = (User) obj1;
			User user2 = (User) obj2;
			return user2.getEmail().compareTo(user1.getEmail());
		}
	}

	public enum UserManagerSorting {
		NAMEASCENDING, NAMEDESCENDING, EMAILASCENDING, EMAILDESCENDING
	}

	public enum Order {
		ASC, DESC
	}

	public static Comparator sortUsers(UserManagerSorting criteria) {
		Comparator comparator = null;
		switch (criteria) {
		case NAMEASCENDING:
			comparator = new HelperUserNameComparator(Order.ASC);
			break;
		case NAMEDESCENDING:
			comparator = new HelperUserNameComparator(Order.DESC);
			break;
		case EMAILASCENDING:
			comparator = new HelperEmailComparator(Order.ASC);
			break;
		case EMAILDESCENDING:
			comparator = new HelperEmailComparator(Order.DESC);
			break;
		}
		return comparator;

	}

	public static String getValidationFromTeamAppSettings(Long ampTeamId) {
		Session session = null;
		Query qry = null;
		AmpApplicationSettings ampAppSettings = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select a from "
					+ AmpApplicationSettings.class.getName()
					+ " a where (a.team=:teamId) ";
			qry = session.createQuery(queryString);
			qry.setLong("teamId", ampTeamId);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				ampAppSettings = (AmpApplicationSettings) itr.next();
				if (ampAppSettings != null
						&& ampAppSettings.getValidation() != null
						&& !"".equals(ampAppSettings.getValidation()))
					break;
			}

		} catch (Exception e) {
			logger.error("Unable to get TeamAppSettings", e);
		}
		return ampAppSettings != null ? ampAppSettings.getValidation() : null;
	}
	
	public static AmpStructureImg getStructureImage(Long structureId, Long imgId) {
		Session session = null;
		Query qry = null;
		AmpStructureImg image = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select o from " + AmpStructureImg.class.getName()
					+ " o " + "where (o.structure.ampStructureId=:structureId and o.id=:imgId)";
			qry = session.createQuery(queryString);
			qry.setParameter("structureId", structureId, Hibernate.LONG);
			qry.setParameter("imgId", imgId, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				image = (AmpStructureImg) itr.next();
			}
		} catch (Exception e) {
			logger.error("Unable to get structure image");
			logger.debug("Exceptiion " + e);
		}
		return image;
	}
	
	public static AmpStructureImg getMostRecentlyUploadedStructureImage(Long structureId) {
		Session session = null;
		Query qry = null;
		AmpStructureImg image = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select o from " + AmpStructureImg.class.getName()
					+ " o " + "where o.structure.ampStructureId=:structureId and o.creationTime=" + 
					"(select max(o1.creationTime) from  "
					+ AmpStructureImg.class.getName()+ " o1 " + 
					" where o1.structure.ampStructureId=:structureId)";
			qry = session.createQuery(queryString);
			qry.setParameter("structureId", structureId, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				image = (AmpStructureImg) itr.next();
			}
		} catch (Exception e) {
			logger.error("Unable to get structure image");
			logger.debug("Exceptiion " + e);
		}
		return image;
	}
	
}

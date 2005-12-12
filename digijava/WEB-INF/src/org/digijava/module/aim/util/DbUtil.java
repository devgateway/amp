package org.digijava.module.aim.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.user.Group;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityInternalId;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpClosingDateHistory;
import org.digijava.module.aim.dbentity.AmpComments;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpCurrencyRate;
import org.digijava.module.aim.dbentity.AmpField;
import org.digijava.module.aim.dbentity.AmpFilters;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpKmDocuments;
import org.digijava.module.aim.dbentity.AmpLevel;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpModality;
import org.digijava.module.aim.dbentity.AmpNotes;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpPages;
import org.digijava.module.aim.dbentity.AmpPerspective;
import org.digijava.module.aim.dbentity.AmpPhysicalPerformance;
import org.digijava.module.aim.dbentity.AmpRegion;
import org.digijava.module.aim.dbentity.AmpReportCache;
import org.digijava.module.aim.dbentity.AmpReportLocation;
import org.digijava.module.aim.dbentity.AmpReportSector;
import org.digijava.module.aim.dbentity.AmpReportPhysicalPerformance;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpSectorScheme;
import org.digijava.module.aim.dbentity.AmpStatus;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTeamMemberRoles;
import org.digijava.module.aim.dbentity.AmpTeamPageFilters;
import org.digijava.module.aim.dbentity.AmpTeamReports;
import org.digijava.module.aim.dbentity.AmpTermsAssist;
import org.digijava.module.aim.dbentity.AmpWoreda;
import org.digijava.module.aim.dbentity.AmpZone;
import org.digijava.module.aim.helper.Activity;
import org.digijava.module.aim.helper.AmpLocations;
import org.digijava.module.aim.helper.AmpProject;
import org.digijava.module.aim.helper.AmpProjectBySector;
import org.digijava.module.aim.helper.AmpProjectDonor;
import org.digijava.module.aim.helper.Assistance;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.Currency;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.DecimalToText;
import org.digijava.module.aim.helper.Documents;
import org.digijava.module.aim.helper.EthiopianCalendar;
import org.digijava.module.aim.helper.FilterProperties;
import org.digijava.module.aim.helper.FiscalCalendar;
import org.digijava.module.aim.helper.ReportsCollection;
import org.digijava.module.aim.helper.Sector;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.cms.dbentity.CMSContentItem;

public class DbUtil {
	private static Logger logger = Logger.getLogger(DbUtil.class);

	public static Collection getFundingDetails(Long fundId) {
		Session session = null;
		Collection fundingDetails = new ArrayList();

		try {
			session = PersistenceManager.getSession();
			String queryString = "select f from "
					+ AmpFundingDetail.class.getName()
					+ " f "
					+ "where (f.ampFundingId=:fundId) order by f.transactionDate";
			Query qry = session.createQuery(queryString);
			qry.setParameter("fundId", fundId, Hibernate.LONG);
			fundingDetails = qry.list();

		} catch (Exception ex) {
			logger.error("Unable to get fundingDetails :" + ex);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed :" + ex2);
			}
		}
		return fundingDetails;
	}
	
	public static Collection getFundingByActivity(Long actId) {
		Session session = null;
		Collection funding = new ArrayList();

		try {
			session = PersistenceManager.getSession();
			String queryString = "select f from "
					+ AmpFunding.class.getName()
					+ " f "
					+ "where (f.ampActivityId=:actId)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("actId", actId, Hibernate.LONG);
			funding = qry.list();

		} catch (Exception ex) {
			logger.error("Unable to get funding :" + ex);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed :" + ex2);
			}
		}
		return funding;
	}

	public static AmpActivityInternalId getActivityInternalId(Long actId,
			Long orgId) {
		Session session = null;
		AmpActivityInternalId internalId = null;

		try {
			session = PersistenceManager.getSession();
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
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}
		return internalId;
	}

	public static Collection getOrganizations(Long actId, String orgCode) {
		Session session = null;
		Collection orgs = new ArrayList();

		try {
			session = PersistenceManager.getSession();
			AmpActivity activity = (AmpActivity) session.load(AmpActivity.class,actId);
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
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}
		return orgs;
	}

	public static Collection getActivityDocuments(Long id) {
		Session session = null;
		Collection docs = new ArrayList();

		try {
			session = PersistenceManager.getSession();
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
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}
		return docs;
	}

	public static Collection getKnowledgeDocuments(Long id) {
		Session session = null;
		Collection docs = new ArrayList();

		try {
			session = PersistenceManager.getSession();
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
					logger.debug("Doc Desc :" + document.getDocDescription());
					docs.add(document);
				}
			}
		} catch (Exception ex) {
			logger.error("Unable to get ActivityDocuments :" + ex);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}
		return docs;
	}

	public static Collection getAllDocuments(Long teamId) {
		Session session = null;
		Collection col = new ArrayList();

		try {
			session = PersistenceManager.getSession();
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
						col.add(document);
					}
				}
			}
		} catch (Exception e) {
			logger.error("Cannot get All documents :" + e);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex) {
				logger.error("releaseSession() failed " + ex);
			}
		}

		return col;
	}

	public static AmpRole getAmpRole(String roleCode) {
		Session session = null;
		AmpRole role = null;

		try {
			session = PersistenceManager.getSession();

			String queryString = "select r from " + AmpRole.class.getName()
					+ " r " + "where (r.roleCode=:code)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("code", roleCode, Hibernate.STRING);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext())
				role = (AmpRole) itr.next();

		} catch (Exception e) {
			logger.error("Uanble to get role :" + e);
		} finally {

			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex) {
				logger.error("releaseSession() failed " + ex);
			}
		}
		return role;
	}

	public static AmpModality getModality(Long id) {
		Session session = null;
		AmpModality modality = null;

		try {
			session = PersistenceManager.getSession();

			String queryString = "select m from " + AmpModality.class.getName()
					+ " m " + "where (m.ampModalityId=:id)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext())
				modality = (AmpModality) itr.next();

		} catch (Exception e) {
			logger.error("Uanble to get modality :" + e);
		} finally {

			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex) {
				logger.error("releaseSession() failed " + ex);
			}
		}
		return modality;
	}

	public static long getActivityMaxId() {
		Session session = null;
		long maxId = 0;

		try {
			session = PersistenceManager.getSession();

			String queryString = "select max(act.ampActivityId) from "
					+ AmpActivity.class.getName() + " act";
			Query qry = session.createQuery(queryString);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				Long temp = (Long) itr.next();
				if (temp != null) {
					maxId = temp.longValue();
				}
			}

		} catch (Exception e) {
			logger.error("Uanble to max id :" + e);
		} finally {

			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex) {
				logger.error("releaseSession() failed " + ex);
			}
		}
		return maxId;
	}

	public static AmpLocation getAmpLocation(Long countryId, Long regionId,
			Long zoneId, Long woredaId) {

		Session session = null;
		AmpLocation loc = null;
		boolean flag = false;

		try {
			session = PersistenceManager.getSession();

			String queryString = "select l from " + AmpLocation.class.getName()
					+ " l";
			if (countryId != null && (!(countryId.equals(new Long(-1))))) {
				if (!flag) {
					queryString += " where";
				}
				queryString += " country_id = " + countryId;
				flag = true;
			}

			if (regionId != null && (!(regionId.equals(new Long(-1))))) {
				if (!flag) {
					queryString += " where";
				} else {
					queryString += " and";
				}
				queryString += " region_id = " + regionId;
				flag = true;
			}

			if (zoneId != null && (!(zoneId.equals(new Long(-1))))) {
				if (!flag) {
					queryString += " where";
				} else {
					queryString += " and";
				}
				queryString += " zone_id = " + zoneId;
			}

			if (woredaId != null && (!(woredaId.equals(new Long(-1))))) {
				if (!flag) {
					queryString += " where";
				} else {
					queryString += " and";
				}
				queryString += " woreda_id = " + woredaId;
			}

			System.out.println("query is " + queryString);
			Query qry = session.createQuery(queryString);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext())
				loc = (AmpLocation) itr.next();

		} catch (Exception e) {
			logger.error("Uanble to get location :" + e);
		} finally {

			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex) {
				logger.error("releaseSession() failed " + ex);
			}
		}
		return loc;
	}

	public static AmpActivity getProjectChannelOverview(Long id) {
		Session session = null;
		AmpActivity activity = null;

		try {
			logger.debug("Id is " + id);
			session = PersistenceManager.getSession();

			// modified by Priyajith
			// Desc: removed the usage of session.load and used the select query
			// start
			String queryString = "select a from " + AmpActivity.class.getName()
					+ " a " + "where (a.ampActivityId=:id)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext())
				activity = (AmpActivity) itr.next();
			// end
		} catch (Exception ex) {
			logger
					.error("Unable to get Amp Activity getProjectChannelOverview() :"
							+ ex);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}
		return activity;
	}

	public static AmpActivity getActivityByName(String name) {
		Session session = null;
		AmpActivity activity = null;

		try {
			logger.debug("Activity Name is : " + name);
			session = PersistenceManager.getSession();

			String queryString = "select a from " + AmpActivity.class.getName()
					+ " a " + "where (a.name=:name)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("name", name, Hibernate.STRING);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext())
				activity = (AmpActivity) itr.next();
			// end
		} catch (Exception ex) {
			logger.error("Unable to get Amp Activity getActivityByName() :"
					+ ex);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}
		return activity;
	}

	public static List getAmpSectors(Long id) {
		ArrayList ampSectors = new ArrayList();
		AmpSector ampSector = null;
		Session session = null;
		Iterator iter = null;

		try {
			session = PersistenceManager.getSession();

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

			iter = ampActivity.getSectors().iterator();
			while (iter.hasNext()) {
				ampSector = (AmpSector) iter.next();
				ampSectors.add(ampSector);
			}
		} catch (Exception ex) {
			logger.error("Unable to get Amp sectors from database :" + ex);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		logger.debug("Executed successfully " + ampSectors.size());
		return ampSectors;
	}

	public static List getAmpLocations(Long id) {
		AmpLocation ampLocation = null;
		ArrayList ampLocations = new ArrayList();
		Session session = null;
		Iterator iter = null;
		try {
			session = PersistenceManager.getSession();

			// modified by Priyajith
			// Desc: removed the usage of session.load and used the select query
			// start
			String queryString = "select a from " + AmpActivity.class.getName()
					+ " a " + "where (a.ampActivityId=:id)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);
			iter = qry.list().iterator();
			AmpActivity ampActivity = null;
			while (iter.hasNext()) {
				ampActivity = (AmpActivity) iter.next();
			}
			// end
			logger.debug("Activity: " + ampActivity.getAmpActivityId());
			iter = ampActivity.getLocations().iterator();
			while (iter.hasNext()) {
				ampLocation = (AmpLocation) iter.next();
				AmpLocations location = new AmpLocations();
				location.setCountry(ampLocation.getCountry());
				logger.debug("Country: " + location.getCountry());
				if (ampLocation.getAmpRegion() != null)
					location.setRegion(ampLocation.getAmpRegion().getName());
				logger.debug("Region: " + location.getRegion());
				if (ampLocation.getAmpZone() != null)
					location.setZone(ampLocation.getAmpZone().getName());
				logger.debug("Zone: " + location.getZone());
				if (ampLocation.getAmpWoreda() != null)
					location.setWoreda(ampLocation.getAmpWoreda().getName());
				logger.debug("Woreda: " + location.getWoreda());
				ampLocations.add(location);
			}
		} catch (Exception ex) {
			logger.error("Unable to get amp locations :" + ex.getMessage());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
			}
		}
		return ampLocations;
	}

	public static Collection getAllLocations(Long id) {
		AmpLocation ampLocation = null;
		ArrayList ampLocations = new ArrayList();
		Session session = null;
		Iterator iter = null;
		try {
			session = PersistenceManager.getSession();

			// modified by Priyajith
			// Desc: removed the usage of session.load and used the select query
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

			iter = ampActivity.getLocations().iterator();
			while (iter.hasNext()) {
				ampLocation = (AmpLocation) iter.next();
				ampLocations.add(ampLocation);
			}
		} catch (Exception ex) {
			logger.error("Unable to get amp locations :" + ex.getMessage());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
			}
		}
		return ampLocations;
	}

	public static AmpLocation getAmpLocation(Long id) {
		AmpLocation ampLocation = null;
		Session session = null;
		try {
			session = PersistenceManager.getSession();
			// modified by Priyajith
			// Desc: removed the usage of session.load and
			// used the select query
			// start
			String queryString = "select l from " + AmpLocation.class.getName()
					+ " l " + "where (l.ampLocationId=:id)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				ampLocation = (AmpLocation) itr.next();
			}
			// end
		} catch (Exception ex) {
			logger.debug("Unable to get amp locations " + ex.getMessage());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
			}
		}
		return ampLocation;
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
			session = PersistenceManager.getSession();
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
				if (ampOrgRole.getRole().getRoleCode().equals(
						Constants.REPORTING_AGENCY)) {
					if (RAOrg.length() == 0)
						RAOrg.append(ampOrgRole.getOrganisation().getName());
					else
						RAOrg.append(",").append(
								ampOrgRole.getOrganisation().getName());
				}
				if (ampOrgRole.getRole().getRoleCode().equals(
						Constants.FUNDING_AGENCY)) {
					if (DNOrg.length() == 0)
						DNOrg.append(ampOrgRole.getOrganisation().getName());
					else
						DNOrg.append(",").append(
								ampOrgRole.getOrganisation().getName());
				}
				if (ampOrgRole.getRole().getRoleCode().equals(
						Constants.IMPLEMENTING_AGENCY)) {
					if (IAOrg.length() == 0)
						IAOrg.append(ampOrgRole.getOrganisation().getName());
					else
						IAOrg.append(",").append(
								ampOrgRole.getOrganisation().getName());
				}
				if (ampOrgRole.getRole().getRoleCode().equals(
						Constants.RELATED_INSTITUTIONS)) {
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
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
			}
		}
		return list;
	}

	public static AmpOrganisation getOrganisation(Long id) {
		Session session = null;
		AmpOrganisation org = null;

		try {
			session = PersistenceManager.getSession();
			// modified by Priyajith
			// desc:used select query instead of session.load
			// start
			String queryString = "select o from "
					+ AmpOrganisation.class.getName() + " o "
					+ "where (o.ampOrgId=:id)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				org = (AmpOrganisation) itr.next();
			}
			// end

		} catch (Exception ex) {
			logger.error("Unable to get organisation from database", ex);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		logger.debug("Getting organisation successfully ");
		return org;
	}

	public static ArrayList getAmpComponent(Long ampActivityId) {
		ArrayList component = new ArrayList();
		Query q = null;
		Session session = null;
		String queryString = null;
		AmpComponent ampComponent = null;
		Iterator iter = null;
		try {
			session = PersistenceManager.getSession();
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
			//System.out.println(ex.toString()) ;
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
			}
		}
		logger.debug("Getting components executed successfully "
				+ component.size());
		return component;
	}

	public static ArrayList getAmpPhysicalProgress(Long ampActivityId) {
		ArrayList progress = new ArrayList();
		Query q = null;
		Session session = null;
		String queryString = null;
		AmpPhysicalPerformance ampPhysicalPerformance = null;
		Iterator iter = null;
		try {
			session = PersistenceManager.getSession();
			queryString = " select Progress from "
					+ AmpPhysicalPerformance.class.getName()
					+ " Progress where (Progress.ampActivityId=:ampActivityId )";
			q = session.createQuery(queryString);
			q.setParameter("ampActivityId", ampActivityId, Hibernate.LONG);
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
			//System.out.println(ex.toString()) ;
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
			}
		}
		logger
				.debug("Getting funding Executed successfully "
						+ progress.size());
		return progress;
	}

	public static List getAmpNotes(Long id) {
		ArrayList notes = new ArrayList();
		Session session = null;
		Iterator iter = null;
		try {
			session = PersistenceManager.getSession();
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
			iter = ampActivity.getNotes().iterator();
			while (iter.hasNext()) {
				AmpNotes ampNotes = (AmpNotes) iter.next();
				logger.debug("Notes :" + ampNotes.getDescription());
				notes.add(ampNotes);
			}
		} catch (Exception ex) {
			logger.error("Unable to get Amp Notes ", ex);
			//System.out.println(ex.toString()) ;
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		logger.debug("Getting Notes Executed successfully " + notes.size());
		return notes;
	}

	public static List getAmpKmDocuments(Long id) {
		ArrayList documents = new ArrayList();
		Session session = null;
		Iterator iter = null;
		try {
			session = PersistenceManager.getSession();
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

			iter = ampActivity.getDocuments().iterator();
			while (iter.hasNext()) {
				AmpKmDocuments ampKmDocuments = (AmpKmDocuments) iter.next();
				logger.debug("Documents :" + ampKmDocuments.getName());
				documents.add(ampKmDocuments);
			}
		} catch (Exception ex) {
			logger.error("Unable to get Amp Km Documents ", ex);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		logger.debug("Getting funding Executed successfully "
				+ documents.size());
		return documents;
	}

	public static Collection getAmpFunding(Long ampActivityId, Long ampFundingId) {
		Session session = null;
		Query q = null;
		Collection ampFundings = null;
		try {
			session = PersistenceManager.getSession();
			String queryString = new String();
			queryString = "select f from "
					+ AmpFunding.class.getName()
					+ " f where (f.ampActivityId=:ampActivityId ) and (f.ampFundingId=:ampFundingId)";
			q = session.createQuery(queryString);
			q.setParameter("ampActivityId", ampActivityId, Hibernate.LONG);
			q.setParameter("ampFundingId", ampFundingId, Hibernate.LONG);
			ampFundings = q.list();
			logger
					.debug("DbUtil : getAmpFunding() returning collection of size  "
							+ ampFundings.size());

		} catch (Exception ex) {
			logger.error("Unable to get AmpFunding collection from database",
					ex);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}

		return ampFundings;
	}

	public static Collection getAmpFunding(Long ampActivityId) {
		logger.debug("getAmpFunding() with ampActivityId=" + ampActivityId);
		Session session = null;
		Query q = null;
		Collection ampFundings = null;
		try {
			session = PersistenceManager.getSession();
			String queryString = new String();
			queryString = "select f from " + AmpFunding.class.getName()
					+ " f where (f.ampActivityId=:ampActivityId) ";
			q = session.createQuery(queryString);
			q.setParameter("ampActivityId", ampActivityId, Hibernate.LONG);
			ampFundings = q.list();
		} catch (Exception ex) {
			logger.error("Unable to get AmpFunding collection from database",
					ex);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		logger
				.debug("DbUtil : getAmpFunding(ampActivityId) returning collection of size  "
						+ (ampFundings != null ? ampFundings.size() : 0));
		return ampFundings;
	}

	public static double getTotalDonorFund(Long ampFundingId,
			Integer transactionType, Integer adjustmentType, String perspective) {

		logger.debug("getTotalDonorFund() with ampFundingId " + ampFundingId
				+ " transactionType " + transactionType + " adjustmentType "
				+ adjustmentType + " perspective " + perspective);

		Session session = null;
		Query q = null;
		List list = null;
		Iterator iter = null;
		Double total = new Double(0.0);
		;

		try {
			session = PersistenceManager.getSession();
			String queryString = new String();
			queryString = "select sum(f.transactionAmount) from "
					+ AmpFundingDetail.class.getName()
					+ " f where (f.ampFundingId=:ampFundingId) "
					+ " and (f.orgRoleCode=:perspective) "
					+ " and (f.transactionType=:transactionType) "
					+ " and (f.adjustmentType=:adjustmentType) group by f.ampFundingId";
			q = session.createQuery(queryString);
			q.setParameter("ampFundingId", ampFundingId, Hibernate.LONG);
			q.setParameter("perspective", perspective, Hibernate.STRING);
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
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
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
			session = PersistenceManager.getSession();
			String queryString = new String();
			queryString = "select sum(f.transactionAmount) from "
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
			logger
					.error(
							"Unable to get planned commitment by fiscal year from database",
							ex);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
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
			session = PersistenceManager.getSession();
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
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
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
			session = PersistenceManager.getSession();
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
			//System.out.println(ex.toString()) ;
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
			}
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
			session = PersistenceManager.getSession();
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
				//			progress.add(ampPhysicalPerformance);
			}
		} catch (Exception ex) {
			logger.error("Unable to get Amp PhysicalPerformance", ex);
			//System.out.println(ex.toString()) ;
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
			}
		}
		logger.debug("Getting funding Executed successfully ");
		return ampPhysicalPerformance;
	}
	
	public static Collection getCreatedOrEditedActivities(Long ampTeamId){
		Collection actList = new ArrayList();
		Session session = null;
		Query q = null;
		
		try {
			session = PersistenceManager.getSession();
			String queryString = "select act from " + AmpActivity.class.getName()
									+ " act where (act.team=:ampTeamId)" 
									+ " and (act.approvalStatus='" + "created" 
									+ "' or act.approvalStatus='" + "edited" + "')";
			q = session.createQuery(queryString);
			q.setParameter("ampTeamId", ampTeamId, Hibernate.LONG);
			actList = q.list();
			
		} catch (Exception ex) {
			logger.error("Unable to get AmpActivity [getCreatedOrEditedActivities()]", ex);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
			}
		}
		logger.debug("Getting CreatedOrEdited activities Executed successfully ");
		return actList;
	}
	
	public static boolean checkForParentTeam(Long ampTeamId) {
		Session session = null;
		Query q = null;
		boolean ans = false;
		try {
			session = PersistenceManager.getSession();
			String qry = "select tm from " + AmpTeam.class.getName() + " tm where tm.parentTeamId=:ampTeamId";
			q = session.createQuery(qry);
			q.setParameter("ampTeamId", ampTeamId, Hibernate.LONG);
			if (q != null && q.list().size() > 0)
				ans = false;
			else
				ans = true;
		} catch (Exception ex) {
			logger.error("Unable to get AmpTeam [checkForParentTeam()]", ex);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
			}
		}
		logger.debug("Getting checkForParentTeam Executed successfully ");
		return ans;
	}
	
	public static String getActivityApprovalStatus(Long actId) {
		Session session = null;
		Query q = null;
		String ans = null;
		try {
			session = PersistenceManager.getSession();
			String qry = "select act from " + AmpActivity.class.getName() + " act where act.ampActivityId=:actId";
			q = session.createQuery(qry);
			q.setParameter("actId", actId, Hibernate.LONG);
			Iterator itr = q.list().iterator();
			while (itr.hasNext()) {
				AmpActivity act = (AmpActivity) itr.next();
				ans = act.getApprovalStatus();
			}
		} catch (Exception ex) {
			logger.error("Unable to get AmpTeam [getActivityApprovalStatus()]", ex);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
			}
		}
		logger.debug("Getting checkForParentTeam Executed successfully ");
		return ans;
	}

	public static Collection getAllTMExceptTL(Long teamId) {
		Session session = null;
		Query qry = null;
		Collection members = new ArrayList();

			try {
				session = PersistenceManager.getSession();
				String queryString = "select tm from " + AmpTeamMember.class.getName()
									 + " tm where (tm.ampTeam=:teamId)";

				qry = session.createQuery(queryString);
				qry.setParameter("teamId", teamId, Hibernate.LONG);
				Iterator itr = qry.list().iterator();

				while (itr.hasNext()) {
					AmpTeamMember ampMem = (AmpTeamMember) itr.next();
					Long id = ampMem.getAmpTeamMemId();
					User user = UserUtils.getUser(ampMem.getUser().getId());
					String name = user.getName();
					String role = ampMem.getAmpMemberRole().getRole();
					AmpTeamMemberRoles ampRole = ampMem.getAmpMemberRole();
					AmpTeamMemberRoles headRole = getAmpTeamHeadRole();
					TeamMember tm = new TeamMember();
					tm.setMemberId(id);
					tm.setMemberName(name);
					tm.setRoleName(role);
					tm.setEmail(user.getEmail());
					if (ampRole.getAmpTeamMemRoleId().equals(
							headRole.getAmpTeamMemRoleId())) {
						tm.setTeamHead(true);
					} else {
						tm.setTeamHead(false);
						if (ampMem.getActivities() == null) {
							tm.setActivities(new HashSet());
						}
						else
							tm.setActivities(ampMem.getActivities());
					}
					if (!tm.getTeamHead())
						members.add(tm);
				}
			} catch (Exception e) {
				logger.error("Unable to get all team members [getAllTMExceptTL()]");
				logger.debug("Exceptiion " + e);
			} finally {
				try {
					if (session != null) {
						PersistenceManager.releaseSession(session);
					}
				} catch (Exception ex) {
					logger.error("releaseSession() failed");
				}
			}
			logger.debug("returning members");
			return members;
	}
		
	public static Collection getApprovedOrCreatorActivities(Long ampTeamId, Long ampTeamMemId){
		Collection actList = new ArrayList();
		Session session = null;
		Query q = null;
		String queryString;
		try {
			session = PersistenceManager.getSession();
			
			if (new Long(0).equals(ampTeamId)) {  // for management workspace
				queryString = "select act.ampActivityId from " + AmpActivity.class.getName()
				  + " act where (act.approvalStatus=:status1 or act.approvalStatus=:status2)";
				q = session.createQuery(queryString);
			}
			else {								// for regular working team
				queryString = "select act.ampActivityId from " + AmpActivity.class.getName()
				  			  + " act where (act.team=:ampTeamId)"
							  + " and ( act.activityCreator=:ampTeamMemId "
							  + " or act.approvalStatus=:status1 or act.approvalStatus=:status2)";
			    q = session.createQuery(queryString);
				q.setParameter("ampTeamId", ampTeamId, Hibernate.LONG);
				q.setParameter("ampTeamMemId", ampTeamMemId, Hibernate.LONG);
			}
			q.setParameter("status1", "approved", Hibernate.STRING);
			q.setParameter("status2", "edited", Hibernate.STRING);
			actList = q.list();		

		} catch (Exception ex) {
			logger.error("Unable to get AmpActivity [getApprovedOrCreatorActivities()]", ex);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
			}
		}
		logger.debug("Getting ApprovedOrCreator activities Executed successfully ");
		return actList;
	}
	
	public static ArrayList getProjectList(Long ampTeamId, Long ampTeamMemId,
			boolean teamLeadFlag, Long ampStatusId, Long ampOrgId,
			Long ampSectorId, String region, Long ampCalType, Long ampFromYear,
			Long ampToYear, String ampCurrencyCode, String perspective,
			Long sortOrder, Long sortField) {
		Session session = null;
		Long All = new Long(0);
		Long Asc = new Long(1);
		Long Desc = new Long(0);
		Long Project = new Long(1);
		Long Id = new Long(2);
		Long Donor = new Long(3);
		Long Total = new Long(4);
		String orderString = "";
		String fieldString = "";
		Query q = null;
		double totalCommited = 0.0;
		double fromExchangeRate = 0.0;
		double toExchangeRate = 1.0;
		ArrayList donor = new ArrayList();
		ArrayList project = new ArrayList();
		Iterator iter = null;
		Iterator iterSector = null;
		String queryString = null;
		AmpReportSector sectors = null;
		DecimalFormat mf = new DecimalFormat("###,###,###,###,###");
		String inClause = null;
		int fiscalYear = 0, fiscalQuarter = 0;

		try { 
			ArrayList dbReturnSet = (ArrayList) DbUtil.getAmpLevel0Teams(ampTeamId);
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
			AmpTeam ampTeam=DbUtil.getAmpTeam(ampTeamId);
			Collection temp = new ArrayList();
			if(ampTeam.getAccessType().equals("Team"))
			{	
				if (teamLeadFlag == false) {
					//temp = DbUtil.getApprovedOrCreatorActivities(ampTeamId,ampTeamMemId);
					temp =  DbUtil.getAllMemberAmpActivities(ampTeamMemId);
				}
			}
			//logger.debug("temp.size [from AmpActivity inside getProjectList()] : " + temp.size());
			String inclause1 = null;
			
			if (temp.size() > 0) {
				Iterator tmIter = temp.iterator();
				while (tmIter.hasNext()) {
					//Long actId = (Long) tmIter.next();
					AmpActivity actId = (AmpActivity) tmIter.next();
					if (inclause1 == null)
						inclause1 = "'" + actId.getAmpActivityId() + "'";
					else
						inclause1 = inclause1 + ",'" + actId.getAmpActivityId() + "'";
				}
			}
			//logger.debug("inclause1 : " + inclause1);
			
			if (sortOrder.equals(Desc))
				orderString = " desc";
			if (sortField.equals(Project))
				fieldString = "report.activityName" + orderString
						+ ",report.ampActivityId";
			if (sortField.equals(Id))
				fieldString = "report.ampId" + orderString
						+ ",report.ampActivityId";
			if (sortField.equals(Donor))
				fieldString = "report.donorName" + orderString
						+ ",report.ampActivityId";
			if (sortField.equals(Total))
				fieldString = "report.ampActivityId";
			session = PersistenceManager.getSession();

			boolean noActivities = false;
			if (teamLeadFlag == false && ampTeam.getAccessType().equals("Team")) {
				if (inclause1 != null) {
					queryString = "select report from " + AmpReportCache.class.getName()
								  + " report where report.ampActivityId in("
								  + inclause1 + ") " + "order by " + fieldString + ",report.ampDonorId";
					//logger.debug("inclause1 : " + queryString);
					q = session.createQuery(queryString);
					iter = q.list().iterator();
					//logger.debug("q.list().size [from AmpReportCache inside getProjectList()] : " + q.list().size());
				} else {
					iter = null;
					noActivities = true;
				}
			} else if (teamLeadFlag == true || "Management".equals(ampTeam.getAccessType())){
				String inClause2 = null;
				Iterator actItr = null;
				if ("Management".equals(ampTeam.getAccessType())) {
					//actItr = DbUtil.getApprovedOrCreatorActivities(new Long(0),ampTeamMemId).iterator();
					queryString = "select act.ampActivityId from " + AmpActivity.class.getName()
					  			  + " act where (act.approvalStatus=:status) and (act.team.ampTeamId in(" + inClause + ") )";
					q = session.createQuery(queryString);
					q.setParameter("status", "approved", Hibernate.STRING);
					Collection ls = new ArrayList();
					ls = q.list();
					actItr = ls.iterator();
					
					while (actItr.hasNext()) {
						Long actId = (Long) actItr.next();
						if (inClause2 == null)
							inClause2 = "'" + actId + "'";
						else
							inClause2 = inClause2 + ",'" + actId + "'";
					}
				}
				else {
					actItr = DbUtil.getApprovedOrCreatorActivities(ampTeamId,ampTeamMemId).iterator();
					while(actItr.hasNext()) {
						Long actId = (Long) actItr.next();
						if (inClause2 == null)
							inClause2 = "'" + actId + "'";
						else
							inClause2 = inClause2 + ",'" + actId + "'";
					}
					//logger.debug("inClause2 : " + inClause2);
				}
				if (inClause2 != null) {
					queryString = "select report from " + AmpReportCache.class.getName()
								  + " report where report.ampActivityId in(" + inClause2
								  + ") " + "order by " + fieldString + ",report.ampDonorId";
					logger.debug("inClause2 : " + queryString);
					q = session.createQuery(queryString);
					iter = q.list().iterator();
				}
				else {
					iter = null;
					noActivities = true;
				}
			}

				AmpProject ampProject=null;
				AmpProjectDonor ampProjectDonor = null;
				while(iter != null && iter.hasNext())
				{
					AmpReportCache ampReportCache=(AmpReportCache) iter.next();
			/*		if(teamLeadFlag==false && memberActivityIds.indexOf(ampReportCache.getAmpActivityId())==-1)
						continue;*/

					if(!ampStatusId.equals(All) && !(ampStatusId.equals(ampReportCache.getAmpStatusId())))
							continue;
	
					if(!ampOrgId.equals(All) && !(ampOrgId.equals(ampReportCache.getAmpDonorId())))
							continue;
		
					if(!ampSectorId.equals(All))
					{
						int flag=0;
						logger.debug("Selected Sector Id: " + ampSectorId);
						iterSector=DbUtil.getAmpReportSectorId(ampReportCache.getAmpActivityId()).iterator();
						while(iterSector.hasNext())
						{
							AmpReportSector sector=(AmpReportSector) iterSector.next();
							logger.debug("Sub Sector:" + sector.getSubSectorName() + ":");
							logger.debug("Amp Sector Id:" + ampSectorId + ":");
							if(sector.getAmpSectorId().equals(ampSectorId))
							{
								logger.debug("Condition true");
								flag=1;
								break;
							}
							if(sector.getAmpSubSectorId().equals(new Long(0)))
							{
								if(new Long(sector.getSubSectorName()).equals(ampSectorId))
								{
									flag=1;
									break;
								}
							}
							if(!(sector.getAmpSubSectorId().equals(new Long(0))) && sector.getAmpSubSectorId().equals(ampSectorId))
							{
								flag=1;
								break;
							}
						}
						if (flag==0)
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
					

					if(!(ampFromYear.equals(All)) || !(ampToYear.equals(All)))
					{
						if(ampReportCache.getTransactionDate()==null)
							continue;
						if(ampReportCache.getTransactionDate()!=null)
						{
							GregorianCalendar calendar = new GregorianCalendar();
							EthiopianCalendar ec=new EthiopianCalendar();
							EthiopianCalendar tempDate=new EthiopianCalendar();
							calendar.setTime(ampReportCache.getTransactionDate());
							ec=tempDate.getEthiopianDate(calendar);
							fiscalYear=ampReportCache.getFiscalYear().intValue();
							if(ampCalType.equals(Constants.ETH_FY))
							{
								fiscalYear=(int)ec.ethFiscalYear;
								fiscalQuarter=(int)ec.ethFiscalQrt;
							}
							if(ampCalType.equals(Constants.ETH_CAL))
							{
								fiscalYear=(int)ec.ethYear;
								fiscalQuarter=(int)ec.ethQtr;
							}
							if(fiscalYear<ampFromYear.intValue() || fiscalYear>ampToYear.intValue())
								continue;
						}
					}
					if(ampProject==null || !(ampProject.getAmpActivityId().equals(ampReportCache.getAmpActivityId())))
					{
						if(ampProject!=null)
						{
							if(donor.size()>0)
								ampProject.getDonor().addAll(donor);
							ampProject.setTotalCommited(mf.format(totalCommited));
							project.add(ampProject);
							totalCommited=0.0;
						}
						ampProject=new AmpProject();
						donor.clear();
						ampProject.setName(ampReportCache.getActivityName());
						ampProject.setDonor(new ArrayList());
						ampProject.setSector(new ArrayList());
						ampProject.getSector().addAll(DbUtil.getAmpReportSector(ampReportCache.getAmpActivityId()));
						
						if(ampReportCache.getDonorName()!=null)
						{
							ampProjectDonor = new AmpProjectDonor();
							ampProjectDonor.setDonorName(ampReportCache.getDonorName());
							ampProjectDonor.setAmpDonorId(ampReportCache.getAmpDonorId());
							donor.add(ampProjectDonor);
						}	
						ampProject.setAmpActivityId(ampReportCache.getAmpActivityId());
						ampProject.setAmpId(ampReportCache.getAmpId());
					}
					
					if(ampReportCache.getDonorName()!=null && ampProjectDonor != null && 
							!(ampProjectDonor.getDonorName().equals(ampReportCache.getDonorName())))
					{
						ampProjectDonor = new AmpProjectDonor();
						ampProjectDonor.setDonorName(ampReportCache.getDonorName());
						ampProjectDonor.setAmpDonorId(ampReportCache.getAmpDonorId());
						donor.add(ampProjectDonor);
					}

					if(ampReportCache.getCurrencyCode()!=null)
					{
						if(ampReportCache.getActualCommitment().doubleValue()>0 && ampReportCache.getPerspective().equals(perspective))
						{

							if(ampReportCache.getCurrencyCode().equals("USD"))
								fromExchangeRate=1.0;
							else
								fromExchangeRate=DbUtil.getExchangeRate(ampReportCache.getCurrencyCode(),Constants.ACTUAL,ampReportCache.getTransactionDate());
							if(ampCurrencyCode.equals("USD"))
								toExchangeRate=1.0;
							else
								toExchangeRate=DbUtil.getExchangeRate(ampCurrencyCode,Constants.ACTUAL,ampReportCache.getTransactionDate());
							
							totalCommited=totalCommited + CurrencyWorker.convert1(ampReportCache.getActualCommitment().doubleValue(),fromExchangeRate,toExchangeRate);
							
						}
					}
					

				}
				if (!noActivities) {
					if(donor.size()>0)
						ampProject.getDonor().addAll(donor);
					ampProject.setTotalCommited(mf.format(totalCommited));
					project.add(ampProject);		
					if(sortField.equals(Total))
					{
						int n=project.size();
						if(orderString.equals(""))
						{
							for (int i=0; i<n-1; i++) 
							{
								for (int j=0; j<n-1-i; j++)
								{
									AmpProject firstProject=(AmpProject) project.get(j);
									AmpProject secondProject=(AmpProject) project.get(j+1);
									if (Double.parseDouble(DecimalToText.removeCommas(secondProject.getTotalCommited())) < Double.parseDouble(DecimalToText.removeCommas(firstProject.getTotalCommited()))) 
									{ 
										AmpProject tempProject=firstProject;         
										project.set(j,secondProject);
										project.set(j+1,tempProject);
									}
								}
							}
						}
						else
						{
							for (int i=0; i<n-1; i++) 
							{
								for (int j=0; j<n-1-i; j++)
								{
									AmpProject firstProject=(AmpProject) project.get(j);
									AmpProject secondProject=(AmpProject) project.get(j+1);
									if (Double.parseDouble(DecimalToText.removeCommas(secondProject.getTotalCommited())) > Double.parseDouble(DecimalToText.removeCommas(firstProject.getTotalCommited()))) 
									{ 
										AmpProject tempProject=firstProject;         
										project.set(j,secondProject);
										project.set(j+1,tempProject);
									}
								}
							}
						}					
					}
				}
			}
			catch(Exception ex) 		
			{
				logger.error("Unable to get Amp Activity names getProjectList(parameters)  from database " + ex.getMessage());
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
					logger.error("releaseSession() failed ");
				}
			}
			return project ;
	}

	public static AmpNotes getAmpNotesDetails(Long id) {
		Session session = null;
		AmpNotes notes = null;

		try {
			session = PersistenceManager.getSession();
			// modified by Priyajith
			// desc:used select query instead of session.load
			// start
			String queryString = "select n from " + AmpNotes.class.getName()
					+ " n " + "where (n.ampNotesId=:id)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				notes = (AmpNotes) itr.next();
			}
			// end

		} catch (Exception ex) {
			logger.error("Unable to get organisation from database", ex);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		logger.debug("Getting Note successfully ");
		return notes;
	}

	public static AmpKmDocuments getAmpDocumentDetails(Long id) {
		Session session = null;
		AmpKmDocuments document = null;

		try {
			session = PersistenceManager.getSession();
			// modified by Priyajith
			// Desc: removed the usage of session.load and used the select query
			// start
			String queryString = "select d from "
					+ AmpKmDocuments.class.getName() + " d "
					+ "where (d.ampKmId=:id)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);

			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				document = (AmpKmDocuments) itr.next();
			}
			// end

		} catch (Exception ex) {
			logger.error("Unable to get organisation from database", ex);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		logger.debug("Getting Note successfully ");
		return document;
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
			session = PersistenceManager.getSession();
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
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		logger
				.debug("DbUtil : getAmpFundingById(ampFundingId) returning ampFunding  "
						+ (ampFunding != null ? ampFunding.getFinancingId()
								: "null"));

		return ampFunding;
	}

	/**
	 * @author jose Returns a collection of records from amp_funding_detail
	 *              based on below
	 * @param ampFundingId
	 * @param perspective
	 *                 orgRoleCode 'MA',DN','IA'
	 * @param transactionType
	 *                 commitment=0,disbursement=1,expenditure=2
	 * @param adjustmentType
	 *                 planned=0,actual=1
	 * @return Collection
	 */
	public static Collection getQuarterlyData(Long ampFundingId,
			String perspective, int transactionType, int adjustmentType) {
		logger.debug("getQuarterlyData with ampFundingId " + ampFundingId
				+ " perspective " + perspective + " transactionType "
				+ transactionType + " adjustmentType " + adjustmentType);

		Session session = null;
		Query q = null;
		Collection c = null;
		Integer trsType = new Integer(transactionType);
		Integer adjType = new Integer(adjustmentType);

		try {
			session = PersistenceManager.getSession();
			String queryString = new String();
			queryString = "select f.transactionAmount,"
					+ "f.transactionDate,f.ampCurrencyId from "
					+ AmpFundingDetail.class.getName()
					+ " f where (f.ampFundingId=:ampFundingId) "
					+ " and (f.orgRoleCode=:perspective) "
					+ " and (f.transactionType=:trsType) "
					+ " and (f.adjustmentType=:adjType) order by f.transactionDate ";
			q = session.createQuery(queryString);
			q.setParameter("ampFundingId", ampFundingId, Hibernate.LONG);
			q.setParameter("perspective", perspective, Hibernate.STRING);
			q.setParameter("trsType", trsType, Hibernate.INTEGER);
			q.setParameter("adjType", adjType, Hibernate.INTEGER);
			c = q.list();
		} catch (Exception ex) {
			logger.error("Unable to get quarterly data from database", ex);

		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}

		logger.debug("getQuarterlyData() returning a list of size : "
				+ c.size());
		return c;
	}

	/*
	 * @author Priyajith C
	 */
	// Retreives all organisation;
	public static Collection getAllOrganisation() {
		Session session = null;
		Query qry = null;
		Collection organisation = new ArrayList();

		try {
			session = PersistenceManager.getSession();
			String queryString = "select o from "
					+ AmpOrganisation.class.getName() + " o order by name asc";
			qry = session.createQuery(queryString);
			organisation = qry.list();
		} catch (Exception e) {
			logger.error("Unable to get all organisations");
			logger.debug("Exceptiion " + e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return organisation;
	}

	public static Collection getSectorActivities(Long sectorId) {

		Session sess = null;
		Collection col = null;

		try {
			sess = PersistenceManager.getSession();
			AmpSector sector = (AmpSector) sess.load(AmpSector.class, sectorId);

			Iterator itr = sector.getAidlist().iterator();
			col = new ArrayList();
			while (itr.hasNext()) {
				col.add(itr.next());
			}
		} catch (Exception e) {
			logger.debug("Exception from getSectorActivities()");
			logger.debug(e.toString());
		} finally {
			try {
				if (sess != null) {
					PersistenceManager.releaseSession(sess);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return col;

	}

	public static Collection getAllSectorSchemes() {
		Session session = null;
		Collection col = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select ss from "
					+ AmpSectorScheme.class.getName() + " ss "
					+ "order by ss.secSchemeName";
			Query qry = session.createQuery(queryString);
			col = qry.list();
		} catch (Exception ex) {
			logger.error("Unable to get all sector schemes, " + ex);
		} finally {
			try {
				if (session != null)
					PersistenceManager.releaseSession(session);
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return col;
	}

	public static Collection getAllParentSectors(Long secSchemeId) {
		Session session = null;
		Collection col = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select s from " + AmpSector.class.getName()
					+ " s " + "where amp_sec_scheme_id = " + secSchemeId
					+ " and parent_sector_id is null " + "order by s.name";
			Query qry = session.createQuery(queryString);
			col = qry.list();

		} catch (Exception e) {
			logger.error("Cannot get parent sectors, " + e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
			}
		}
		return col;
	}

	public static Collection getAllChildSectors(Long parSecId) {
		Session session = null;
		Collection col = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select s from " + AmpSector.class.getName()
					+ " s " + "where parent_sector_id = " + parSecId
					+ " order by s.name";
			Query qry = session.createQuery(queryString);
			col = qry.list();

		} catch (Exception e) {
			logger.error("Cannot get child sectors, " + e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
			}
		}
		return col;
	}

	public static void updateSectorOrganisation(Long sectorId,
			AmpOrganisation organisation) {
		AmpSector sector = getAmpSector(sectorId);
		sector.setAmpOrgId(organisation);
		update(sector);
	}

	public static void updateSubSectors(AmpSector sector,
			AmpOrganisation organisation) {

		int index = 0;
		ArrayList sectorList = new ArrayList();
		Iterator itr = null;

		try {

			itr = getSubSectors(sector.getAmpSectorId()).iterator();
			while (itr.hasNext()) {
				Sector subSec = (Sector) itr.next();
				sectorList.add(subSec);
			}
			while (index < sectorList.size()) {
				Sector sec = (Sector) sectorList.get(index++);
				itr = getSubSectors(sec.getSectorId()).iterator();

				while (itr.hasNext()) {
					Sector subSec = (Sector) itr.next();
					sectorList.add(subSec);
				}
			}

			for (int i = 0; i < sectorList.size(); i++) {
				Sector sec = (Sector) sectorList.get(i);
				updateSectorOrganisation(sec.getSectorId(), organisation);
			}
		} catch (Exception e) {
			logger.debug("Exception from updateSubSectors()");
			logger.debug(e.toString());
		}
	}

	// Retreives all sub-sectors within the sector with id 'parentSecId'
	public static Collection getSubSectors(Long parentSecId) {

		Session session = null;
		Query qry = null;
		Collection col = new ArrayList();
		Iterator itr = null;
		AmpSector ampSector = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = new String();

			if (parentSecId.intValue() == 0) {
				queryString = "select s from " + AmpSector.class.getName()
						+ " s where s.parentSectorId is null order by s.name";

				qry = session.createQuery(queryString);
			} else {
				queryString = "select s from " + AmpSector.class.getName()
						+ " s where (s.parentSectorId=:parentSectorId) "
						+ "order by s.name";

				qry = session.createQuery(queryString);
				qry.setParameter("parentSectorId", parentSecId, Hibernate.LONG);
			}
			itr = qry.list().iterator();

			while (itr.hasNext()) {
				ampSector = (AmpSector) itr.next();
				Sector sec = new Sector(ampSector.getAmpSectorId(), ampSector
						.getName(), ampSector.getAmpOrgId().getAmpOrgId(),
						getOrganisation(ampSector.getAmpOrgId().getAmpOrgId())
								.getName());

				col.add(sec);
			}

		} catch (Exception ex) {
			logger.error("Unable to get subsectors");
			logger.debug("Exceptiion " + ex);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}

		return col;
	}

	/*
	 * Retreives the sector details for the sector with the id 'sectorId'
	 */
	public static Sector getSector(Long sectorId) {

		Session session = null;
		Query qry = null;
		Iterator itr = null;
		AmpSector ampSector = null;
		Sector sec = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = new String();
			queryString = "select s from " + AmpSector.class.getName()
					+ " s where (s.ampSectorId=:ampSectorId)";

			qry = session.createQuery(queryString);
			qry.setParameter("ampSectorId", sectorId, Hibernate.LONG);
			itr = qry.list().iterator();

			if (itr.hasNext()) {
				ampSector = (AmpSector) itr.next();
				sec = new Sector(ampSector.getAmpSectorId(), ampSector
						.getName(), ampSector.getAmpOrgId().getAmpOrgId(),
						getOrganisation(ampSector.getAmpOrgId().getAmpOrgId())
								.getName());

			}

		} catch (Exception ex) {
			logger.error("Unable to get sector info");
			logger.debug("Exceptiion " + ex);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}

		return sec;
	}

	public static AmpSector getAmpSector(Long id) {

		Session session = null;
		Query qry = null;
		Iterator itr = null;
		AmpSector ampSector = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = new String();
			queryString = "select s from " + AmpSector.class.getName()
					+ " s where (s.ampSectorId=:ampSectorId)";

			qry = session.createQuery(queryString);
			qry.setParameter("ampSectorId", id, Hibernate.LONG);
			itr = qry.list().iterator();

			if (itr.hasNext()) {
				ampSector = (AmpSector) itr.next();
			}

		} catch (Exception ex) {
			logger.error("Unable to get amp_sector info");
			logger.debug("Exceptiion " + ex);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}

		return ampSector;

	}

	public static AmpSectorScheme getAmpSectorScheme(Long id) {
		Session session = null;
		Query qry = null;
		Iterator itr = null;
		AmpSectorScheme ampSectorScheme = null;
		try {
			session = PersistenceManager.getSession();
			String queryString = new String();
			queryString = "select s from " + AmpSectorScheme.class.getName()
					+ " s where (s.ampSecSchemeId=:ampSectorSchemeId)";

			qry = session.createQuery(queryString);
			qry.setParameter("ampSectorSchemeId", id, Hibernate.LONG);
			itr = qry.list().iterator();

			if (itr.hasNext()) {
				ampSectorScheme = (AmpSectorScheme) itr.next();
			}

		} catch (Exception ex) {
			logger.error("Unable to get amp_sector_scheme info");
			logger.debug("Exception " + ex);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}

		return ampSectorScheme;

	}

	/*
	 * update sector details
	 */
	public static void updateSector(AmpSector sector) {

		if (sector.getParentSectorId() == null
				&& organisationChanged(sector.getAmpSectorId(), sector
						.getAmpOrgId()) == true) {

			updateSubSectors(sector, sector.getAmpOrgId());
		}
		update(sector);
	}

	public static boolean organisationChanged(Long sectorId,
			AmpOrganisation organisation) {
		logger.debug("in organisationChanged()");
		Session sess = null;
		boolean flag = false;

		try {
			sess = PersistenceManager.getSession();
			String qryString = "select s from " + AmpSector.class.getName()
					+ " s where (s.ampSectorId=:ampSectorId)";

			Query qry = sess.createQuery(qryString);
			qry.setParameter("ampSectorId", sectorId, Hibernate.LONG);
			Iterator itr = qry.list().iterator();

			if (itr.hasNext()) {
				AmpSector ampSector = (AmpSector) itr.next();
				logger.debug(ampSector.getAmpOrgId().getName() + " !- "
						+ organisation.getName());
				if (ampSector.getAmpOrgId().getAmpOrgId() != organisation
						.getAmpOrgId()) {
					flag = true;
				}
			}
		} catch (Exception e) {
			logger.debug("Exception thrown from fn organisationChanged()");
			logger.debug(e.toString());
		} finally {
			if (sess != null) {
				try {
					PersistenceManager.releaseSession(sess);
				} catch (Exception ex) {
					logger.debug("releaseSession() 2 failed");
					logger.debug(ex.toString());
				}
			}
		}
		return flag;
	}

	public static Collection getAllFisCalenders() {
		Session session = null;
		Query qry = null;
		Collection fisCals = new ArrayList();

		try {
			session = PersistenceManager.getSession();
			String queryString = "select f from "
					+ AmpFiscalCalendar.class.getName() + " f";
			qry = session.createQuery(queryString);
			fisCals = qry.list();
		} catch (Exception e) {
			logger.error("Unable to get all fiscal calendars");
			logger.debug("Exceptiion " + e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return fisCals;
	}

	public static Collection getAllActivities() {
		Session session = null;
		Query qry = null;
		Collection activities = new ArrayList();
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
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return activities;
	}

	public static AmpFiscalCalendar getAmpFiscalCalendar(Long ampFisCalId) {
		Session session = null;
		Query qry = null;
		AmpFiscalCalendar ampFisCal = null;

		try {
			session = PersistenceManager.getSession();
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
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return ampFisCal;
	}

	public static User getUser(String email) {
		Session session = null;
		Query qry = null;
		User user = null;

		try {
			session = PersistenceManager.getSession();
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
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return user;
	}

	public static AmpTeamMember getAmpTeamMember(Long id) {
		AmpTeamMember ampMember = null;
		Session session = null;

		try {
			session = PersistenceManager.getSession();
			// modified by Priyajith
			// desc:used select query instead of session.load
			// start
			String queryString = "select t from "
					+ AmpTeamMember.class.getName() + " t "
					+ "where (t.ampTeamMemId=:id)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				ampMember = (AmpTeamMember) itr.next();
			}
			// end
		} catch (Exception ex) {
			logger.error("Unable to get team member " + ex);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.debug("releaseSession() failed", ex2);
			}
		}
		return ampMember;
	}

	public static AmpTeamMember getMember(String email) {

		User user = getUser(email);
		if (user == null)
			return null;

		Session session = null;
		Query qry = null;
		AmpTeamMember member = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select tm from "
					+ AmpTeamMember.class.getName()
					+ " tm where (tm.user=:user)";
			qry = session.createQuery(queryString);
			qry.setParameter("user", user.getId(), Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				member = (AmpTeamMember) itr.next();
			}
		} catch (Exception e) {
			logger.error("Unable to get team member");
			logger.debug("Exceptiion " + e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return member;

	}

	public static AmpTeamMember getTeamHead(Long teamId) {

		Session session = null;
		Query qry = null;
		AmpTeamMember member = null;

		try {

			AmpTeamMemberRoles ampRole = getAmpTeamHeadRole();
			session = PersistenceManager.getSession();
			String queryString = "select tm from "
					+ AmpTeamMember.class.getName()
					+ " tm where (tm.ampTeam=:teamId) and (tm.ampMemberRole=:role)";
			qry = session.createQuery(queryString);
			qry.setParameter("teamId", teamId, Hibernate.LONG);
			qry.setParameter("role", ampRole.getAmpTeamMemRoleId(),
					Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				member = (AmpTeamMember) itr.next();
			}
		} catch (Exception e) {
			logger.error("Unable to get team member");
			logger.debug("Exceptiion " + e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return member;
	}

	public static AmpApplicationSettings getTeamAppSettings(Long teamId) {
		Session session = null;
		Query qry = null;
		AmpApplicationSettings ampAppSettings = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select a from "
					+ AmpApplicationSettings.class.getName()
					+ " a where (a.team=:teamId) and a.member is null";
			qry = session.createQuery(queryString);
			qry.setParameter("teamId", teamId, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				ampAppSettings = (AmpApplicationSettings) itr.next();
			}
		} catch (Exception e) {
			logger.error("Unable to get TeamAppSettings");
			logger.debug("Exceptiion " + e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return ampAppSettings;
	}

	public static boolean isUserTranslator(Long userId) {

		logger.debug("In isUserTranslator()");
		User user = null;
		Session session = null;
		boolean flag = false;
		try {
			session = PersistenceManager.getSession();

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
				if (grp.getKey().trim().equals("TRN")) {
					logger.debug("setting flag as true");
					flag = true;
					break;
				} else {
					logger.debug("in else");
				}
			}
		} catch (Exception ex) {
			logger.error("Unable to get team member " + ex);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.debug("releaseSession() failed", ex2);
			}
		}
		return flag;
	}

	public static AmpApplicationSettings getMemberAppSettings(Long memberId) {
		Session session = null;
		Query qry = null;
		AmpApplicationSettings ampAppSettings = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select a from "
					+ AmpApplicationSettings.class.getName()
					+ " a where (a.member=:memberId)";
			qry = session.createQuery(queryString);
			qry.setParameter("memberId", memberId, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				ampAppSettings = (AmpApplicationSettings) itr.next();
			}
		} catch (Exception e) {
			logger.error("Unable to get MemberAppSettings");
			logger.debug("Exceptiion " + e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return ampAppSettings;
	}

	public static Collection getAllTeams() {
		Session session = null;
		Query qry = null;
		Collection teams = new ArrayList();

		try {
			session = PersistenceManager.getSession();
			String queryString = "select t from " + AmpTeam.class.getName()
					+ " t";
			qry = session.createQuery(queryString);
			teams = qry.list();
		} catch (Exception e) {
			logger.debug("cannot get All teams");
			logger.debug(e.toString());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return teams;
	}

	public static AmpTeam getAmpTeam(Long id) {
		Session session = null;
		Query qry = null;
		AmpTeam team = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select t from " + AmpTeam.class.getName()
					+ " t where (t.ampTeamId=:id)";
			qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				team = (AmpTeam) itr.next();
			}
		} catch (Exception e) {
			logger.error("Unable to get team" + e.getMessage());
			logger.debug("Exceptiion " + e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return team;
	}

	public static AmpTeam getTeamByName(String teamName) {
		Session session = null;
		Query qry = null;
		AmpTeam team = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select t from " + AmpTeam.class.getName()
					+ " t where (t.name=:teamName)";
			qry = session.createQuery(queryString);
			qry.setParameter("teamName", teamName, Hibernate.STRING);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				team = (AmpTeam) itr.next();
			}
		} catch (Exception e) {
			logger.error("Unable to get team");
			logger.debug("Exceptiion " + e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return team;
	}

	public static AmpTeamMemberRoles getAmpTeamMemberRole(Long id) {
		Session session = null;
		Query qry = null;
		AmpTeamMemberRoles role = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select r from "
					+ AmpTeamMemberRoles.class.getName()
					+ " r where (r.ampTeamMemRoleId=:id)";
			qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				role = (AmpTeamMemberRoles) itr.next();
			}
		} catch (Exception e) {
			logger.error("Unable to get role");
			logger.debug("Exceptiion " + e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return role;
	}

	public static AmpTeamMemberRoles getAmpRoleByName(String name) {
		Session session = null;
		Query qry = null;
		AmpTeamMemberRoles role = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select r from "
					+ AmpTeamMemberRoles.class.getName()
					+ " r where (r.role=:name)";
			qry = session.createQuery(queryString);
			qry.setParameter("name", name, Hibernate.STRING);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				role = (AmpTeamMemberRoles) itr.next();
			}
		} catch (Exception e) {
			logger.error("Unable to get role");
			logger.debug("Exceptiion " + e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return role;
	}

	public static Collection getMembersUsingRole(Long roleId) {
		Session session = null;
		Query qry = null;
		Collection members = new ArrayList();

		try {
			session = PersistenceManager.getSession();
			String queryString = "select tm from "
					+ AmpTeamMember.class.getName()
					+ " tm where (tm.ampMemberRole=:roleId)";

			qry = session.createQuery(queryString);
			qry.setParameter("roleId", roleId, Hibernate.LONG);
			members = qry.list();
		} catch (Exception e) {
			logger.error("Unable to get all team members");
			logger.debug("Exceptiion " + e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		logger.debug("returning members");
		return members;
	}

	public static Collection getAllTeamMembers(Long teamId) {
		Session session = null;
		Query qry = null;
		Collection members = new ArrayList();

		try {
			session = PersistenceManager.getSession();
			String queryString = "select tm from "
					+ AmpTeamMember.class.getName()
					+ " tm where (tm.ampTeam=:teamId)";

			qry = session.createQuery(queryString);
			qry.setParameter("teamId", teamId, Hibernate.LONG);
			Iterator itr = qry.list().iterator();

			while (itr.hasNext()) {
				AmpTeamMember ampMem = (AmpTeamMember) itr.next();
				Long id = ampMem.getAmpTeamMemId();
				User user = UserUtils.getUser(ampMem.getUser().getId());
				String name = user.getName();
				String role = ampMem.getAmpMemberRole().getRole();
				AmpTeamMemberRoles ampRole = ampMem.getAmpMemberRole();
				AmpTeamMemberRoles headRole = getAmpTeamHeadRole();
				TeamMember tm = new TeamMember();
				tm.setMemberId(id);
				tm.setMemberName(name);
				tm.setRoleName(role);
				tm.setEmail(user.getEmail());
				if (ampRole.getAmpTeamMemRoleId().equals(
						headRole.getAmpTeamMemRoleId())) {
					tm.setTeamHead(true);
				} else {
					tm.setTeamHead(false);
				}
				members.add(tm);
			}
		} catch (Exception e) {
			logger.error("Unable to get all team members");
			logger.debug("Exceptiion " + e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		logger.debug("returning members");
		return members;
	}

	public static Collection getAllMembersUsingActivity(Long activityId) {
		Session session = null;
		Collection col = null;

		try {

			session = PersistenceManager.getSession();
			// modified by Priyajith
			// desc:used select query instead of
			// session.load
			// start
			String queryString = "select a from " + AmpActivity.class.getName()
					+ " a " + "where (a.ampActivityId=:id)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", activityId, Hibernate.LONG);
			Iterator itrTemp = qry.list().iterator();
			AmpActivity ampActivity = null;
			while (itrTemp.hasNext()) {
				ampActivity = (AmpActivity) itrTemp.next();
			}
			// end

			Iterator itr = ampActivity.getMember().iterator();
			col = new ArrayList();
			while (itr.hasNext()) {
				col.add(itr.next());
			}
		} catch (Exception e) {
			logger.debug("Exception from getAllMembersUsingActivity()");
			logger.debug(e.toString());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return col;
	}

	public static Collection getAllMemberActivities(Long memberId) {
		Session session = null;
		Collection col = null;

		try {

			session = PersistenceManager.getSession();
			// modified by Priyajith
			// Desc: removed the usage of session.load and used the select query
			// start
			String queryString = "select t from "
					+ AmpTeamMember.class.getName() + " t "
					+ "where (t.ampTeamMemId=:id)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", memberId, Hibernate.LONG);

			Iterator itrTemp = qry.list().iterator();
			AmpTeamMember ampMember = null;
			while (itrTemp.hasNext()) {
				ampMember = (AmpTeamMember) itrTemp.next();
			}
			// end

			Iterator itr = ampMember.getActivities().iterator();
			col = new ArrayList();
			while (itr.hasNext()) {
				AmpActivity activity = (AmpActivity) itr.next();
				Iterator orgItr = activity.getOrgrole().iterator();
				Activity act = new Activity();
				act.setActivityId(activity.getAmpActivityId());
				act.setName(activity.getName());
				String donors = "";

				while (orgItr.hasNext()) {
					AmpOrgRole orgRole = (AmpOrgRole) orgItr.next();
					if (orgRole.getRole().getRoleCode().equals(Constants.DONOR)) {
						donors += orgRole.getOrganisation().getOrgCode();
					}
				}
				act.setDonors(donors);
				col.add(act);				
			}

		} catch (Exception e) {
			logger.debug("Exception from getAllMemberActivities()");
			logger.debug(e.toString());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return col;
	}
	
	
	public static Collection getAllMemberAmpActivities(Long memberId) {
		Session session = null;
		Collection col = new ArrayList();

		try {

			session = PersistenceManager.getSession();
			// modified by Priyajith
			// Desc: removed the usage of session.load and used the select query
			// start
			String queryString = "select t from "
					+ AmpTeamMember.class.getName() + " t "
					+ "where (t.ampTeamMemId=:id)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", memberId, Hibernate.LONG);
			
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				AmpTeamMember member = (AmpTeamMember) itr.next();
				col.addAll(member.getActivities());
			}
			
		} catch (Exception e) {
			logger.debug("Exception from getAllMemberActivities()");
			logger.debug(e.toString());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return col;
	}	

	/*
	 * public static Collection getAllMemberActivities(Long memberId) { Session
	 * session = null; Collection col = null;
	 * 
	 * try {
	 * 
	 * session = PersistenceManager.getSession(); AmpTeamMember ampMember =
	 * (AmpTeamMember) session.load(AmpTeamMember.class, memberId);
	 * 
	 * Iterator itr = ampMember.getActivities().iterator(); col = new
	 * ArrayList(); while (itr.hasNext()) { col.add(itr.next()); } } catch
	 * (Exception e) { logger.debug("Exception from getAllMemberActivities()");
	 * logger.debug(e.toString()); } finally { try { if (session != null) {
	 * PersistenceManager.releaseSession(session); } } catch (Exception ex) {
	 * logger.debug("releaseSession() failed"); logger.debug(ex.toString()); } }
	 * return col; }
	 *  
	 */

	public static ArrayList getAllMemberReports(Long id) {
		Session session = null;
		ArrayList col = new ArrayList();

		try {

			session = PersistenceManager.getSession();
			// modified by Priyajith
			// Desc: removed the usage of session.load and used the select query
			// start
			String queryString = "select t from "
					+ AmpTeamMember.class.getName() + " t "
					+ "where (t.ampTeamMemId=:id)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);

			Iterator itrTemp = qry.list().iterator();
			AmpTeamMember ampTeamMember = null;
			while (itrTemp.hasNext()) {
				ampTeamMember = (AmpTeamMember) itrTemp.next();
			}
			// end

			Iterator itr = ampTeamMember.getReports().iterator();
			while (itr.hasNext()) {
				AmpReports ampReports = (AmpReports) itr.next();
				if (!(ampReports.getAmpReportId().equals(new Long(7)))) {
					logger.debug("inside iter");
					col.add(ampReports);
				}
			}
		} catch (Exception e) {
			logger.debug("Exception from getAllMemberReports()");
			logger.debug(e.toString());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return col;
	}

	public static Collection getAllTeamAmpActivities(Long teamId) {
		Session session = null;
		Collection col = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select act from "
					+ AmpActivity.class.getName()
					+ " act where (act.team=:teamId)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("teamId", teamId, Hibernate.LONG);
			col = qry.list();
		} catch (Exception e) {
			logger.debug("Exception from getAllTeamAmpActivities()");
			logger.debug(e.toString());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return col;
	}

	public static boolean hasActivities(Long teamId) {
		Session session = null;
		boolean flag = false;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select count(*) from "
					+ AmpActivity.class.getName()
					+ " act where (act.team=:teamId)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("teamId", teamId, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				Integer numActivities = (Integer) itr.next();
				if (numActivities.intValue() != 0) {
					flag = true;
				}
			}
		} catch (Exception e) {
			logger.debug("Exception from hasActivities()");
			logger.debug(e.toString());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return flag;
	}

	public static Collection getAllTeamActivities(Long teamId) {
		Session session = null;
		Collection col = new ArrayList();

		try {
			session = PersistenceManager.getSession();

			String queryString = "";
			Query qry = null;
			
			if (teamId != null) {
				queryString = "select act from " + AmpActivity.class.getName()
								+ " act where (act.team=:teamId)";
				qry = session.createQuery(queryString);
				qry.setParameter("teamId", teamId, Hibernate.LONG);

			} else {
				queryString = "select act from " + AmpActivity.class.getName()
						+ " act where act.team is null";
				qry = session.createQuery(queryString);
			}

			Iterator itr = qry.list().iterator();

			while (itr.hasNext()) {

				AmpActivity activity = (AmpActivity) itr.next();
				Collection temp1 = activity.getOrgrole();
				Collection temp2 = new ArrayList();
				Iterator temp1Itr = temp1.iterator();
				while (temp1Itr.hasNext()) {
					AmpOrgRole orgRole = (AmpOrgRole) temp1Itr.next();
					if (!temp2.contains(orgRole))
						temp2.add(orgRole);
				}
				
				Iterator orgItr = temp2.iterator();

				Activity act = new Activity();
				act.setActivityId(activity.getAmpActivityId());
				act.setName(activity.getName());
				String donors = "";

				while (orgItr.hasNext()) {
					AmpOrgRole orgRole = (AmpOrgRole) orgItr.next();

					if (orgRole.getRole().getRoleCode().equals(Constants.DONOR)) {
						donors += orgRole.getOrganisation().getAcronym();
					}
				}

				act.setDonors(donors);
				col.add(act);

			}

		} catch (Exception e) {
			logger.debug("Exception from getAllTeamActivities()");
			logger.debug(e.toString());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return col;
	}

	/*
	 * return ReportsCollection Object
	 */
	public static Collection getTeamReportsCollection(Long teamId) {
		Session session = null;
		Collection col = null;
		try {
			session = PersistenceManager.getSession();
			String queryString = "select tr from "
					+ AmpTeamReports.class.getName()
					+ " tr where (tr.team=:teamId)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("teamId", teamId, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			col = new ArrayList();
			while (itr.hasNext()) {
				AmpTeamReports ampTeamRep = (AmpTeamReports) itr.next();
				// modified by Priyajith
				// desc:used select query instead of session.load
				// start
				queryString = "select r from " + AmpReports.class.getName()
						+ " r " + "where (r.ampReportId=:id)";
				qry = session.createQuery(queryString);
				qry.setParameter("id", ampTeamRep.getReport().getAmpReportId(),
						Hibernate.LONG);
				Iterator itrTemp = qry.list().iterator();
				AmpReports ampReport = null;
				while (itrTemp.hasNext()) {
					ampReport = (AmpReports) itrTemp.next();
				}
				// end

				ReportsCollection rc = new ReportsCollection();
				rc.setReport(ampReport);
				if (ampTeamRep.getTeamView() == false) {
					rc.setTeamView(false);
				} else {
					rc.setTeamView(true);
				}

				col.add(rc);

			}
		} catch (Exception e) {
			logger.debug("Exception from getTeamReportsCollection");
			logger.debug(e.toString());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return col;
	}

	public static Collection getAllTeamReports(Long teamId) {
		Session session = null;
		Collection col = null;
		try {
			session = PersistenceManager.getSession();
			String queryString = "select tr from "
					+ AmpTeamReports.class.getName()
					+ " tr where (tr.team=:teamId) and tr.report.ampReportId<>'7'";
			Query qry = session.createQuery(queryString);
			qry.setParameter("teamId", teamId, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			col = new ArrayList();
			StringBuffer qryBuffer = new StringBuffer();
			AmpTeamReports ampTeamRep = null;
			while (itr.hasNext()) {
			    ampTeamRep = (AmpTeamReports) itr.next();
			    if (qryBuffer.length() != 0)
			        qryBuffer.append(",");
			    qryBuffer.append(ampTeamRep.getReport().getAmpReportId());
			}
			
			if (qryBuffer != null && qryBuffer.length() > 0) {
				queryString = "select r from " + AmpReports.class.getName() + " r " +
					"where r.ampReportId in (" + qryBuffer + ")";
				logger.debug("Query = " + queryString);
				qry = session.createQuery(queryString);
				col = qry.list();
			}
		} catch (Exception e) {
			logger.debug("Exception from getAllTeamReports()");
			logger.debug(e.toString());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return col;
	}

	public static AmpTeamReports getAmpTeamReport(Long teamId, Long reportId) {
		Session session = null;
		AmpTeamReports ampTeamRep = null;
		try {
			session = PersistenceManager.getSession();
			String queryString = "select tr from "
					+ AmpTeamReports.class.getName()
					+ " tr where (tr.team=:teamId) and (tr.report=:reportId)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("teamId", teamId, Hibernate.LONG);
			qry.setParameter("reportId", reportId, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				ampTeamRep = (AmpTeamReports) itr.next();
			}
		} catch (Exception e) {
			logger.debug("Exception from getAmpTeamReport()");
			logger.debug(e.toString());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return ampTeamRep;
	}

	public static Collection getAllUnassignedActivities() {
		return getAllTeamActivities(null);
	}

	public static Collection getAllUnassignedTeamReports(Long id) {
		Session session = null;
		Collection col = null;
		Collection col1 = null;

		try {
			col = getAllReports();
			session = PersistenceManager.getSession();

			String queryString = "select tr from "
					+ AmpTeamReports.class.getName()
					+ " tr where (tr.team=:teamId)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("teamId", id, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			col1 = new ArrayList();
			while (itr.hasNext()) {
				AmpTeamReports ampTeamRep = (AmpTeamReports) itr.next();
				// modified by Priyajith
				// desc:used select query instead of session.load
				// start
				queryString = "select r from " + AmpReports.class.getName()
						+ " r " + "where (r.ampReportId=:id)";
				qry = session.createQuery(queryString);
				qry.setParameter("id", ampTeamRep.getReport().getAmpReportId(),
						Hibernate.LONG);
				Iterator itrTemp = qry.list().iterator();
				AmpReports ampReport = null;
				while (itrTemp.hasNext()) {
					ampReport = (AmpReports) itrTemp.next();
				}
				// end
				col1.add(ampReport);
			}

			Iterator itr2 = col1.iterator();

			while (itr2.hasNext()) {
				AmpReports rep = (AmpReports) itr2.next();
				Iterator itr1 = col.iterator();
				while (itr1.hasNext()) {
					AmpReports tempRep = (AmpReports) itr1.next();
					if (tempRep.getAmpReportId().equals(rep.getAmpReportId())) {
						col.remove(tempRep);
						break;
					}
				}
			}

		} catch (Exception e) {
			logger.debug("Exceptiion from getAllUnassignedTeamReports()");
			logger.debug("Exceptiion " + e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return col;
	}

	public static AmpTeamMemberRoles getAmpTeamHeadRole() {
		Session session = null;
		Query qry = null;
		AmpTeamMemberRoles ampRole = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select r from "
					+ AmpTeamMemberRoles.class.getName()
					+ " r where r.teamHead = 1";
			qry = session.createQuery(queryString);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				ampRole = (AmpTeamMemberRoles) itr.next();
			}
		} catch (Exception e) {
			logger.error("Unable to get role");
			logger.debug("Exceptiion " + e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return ampRole;
	}

	public static Collection getAllTeamMemberRoles() {
		Session session = null;
		Query qry = null;
		Collection roles = new ArrayList();

		try {
			session = PersistenceManager.getSession();
			String queryString = "select r from "
					+ AmpTeamMemberRoles.class.getName() + " r";
			qry = session.createQuery(queryString);
			roles = qry.list();
		} catch (Exception e) {
			logger.error("Unable to get all roles");
			logger.debug("Exceptiion " + e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return roles;
	}

	public static AmpTeamMember getAmpTeamMember(User user) {
		Session session = null;
		Query qry = null;
		AmpTeamMember member = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select tm from "
					+ AmpTeamMember.class.getName()
					+ " tm where (tm.user=:user)";
			qry = session.createQuery(queryString);
			qry.setParameter("user", user.getId(), Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				member = (AmpTeamMember) itr.next();
			}
		} catch (Exception e) {
			logger.error("Unable to get team Member");
			logger.debug("Exceptiion " + e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return member;
	}

	public static Collection getTeamMembers(Long teamId) {
		Session session = null;
		Collection col = new ArrayList();

		try {
			session = PersistenceManager.getSession();
			AmpTeamMember mem = getTeamHead(teamId);
			String queryString = "select tm from "
					+ AmpTeamMember.class.getName()
					+ " tm where (tm.ampTeam=:teamId) and (tm.ampTeamMemId!=:memId)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("teamId", teamId, Hibernate.LONG);
			qry.setParameter("memId", mem.getAmpTeamMemId(), Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				AmpTeamMember ampMem = (AmpTeamMember) itr.next();
				Long id = ampMem.getAmpTeamMemId();
				User user = UserUtils.getUser(ampMem.getUser().getId());
				String name = user.getName();
				String role = ampMem.getAmpMemberRole().getRole();
				TeamMember tm = new TeamMember();
				tm.setMemberId(id);
				tm.setMemberName(name);
				tm.setRoleName(role);
				col.add(tm);
			}
		} catch (Exception e) {
			logger.debug("Exception from getTeamMembers()");
			logger.debug(e.toString());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return col;
	}

	public static Collection getAllReports() {
		Session session = null;
		Query qry = null;
		Collection reports = new ArrayList();

		try {
			session = PersistenceManager.getSession();
			String queryString = "select r from " + AmpReports.class.getName()
					+ " r";
			qry = session.createQuery(queryString);
			reports = qry.list();
		} catch (Exception e) {
			logger.error("Unable to get all reports");
			logger.debug("Exceptiion " + e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return reports;
	}

	public static AmpReports getAmpReport(Long id) {
		AmpReports ampReports = null;
		Session session = null;

		try {
			session = PersistenceManager.getSession();
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
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.debug("releaseSession() failed", ex2);
			}
		}
		return ampReports;
	}

	public static Collection getMembersUsingReport(Long id) {

		Session session = null;
		Collection col = null;

		try {
			session = PersistenceManager.getSession();
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
			logger.debug("Exception from getMembersUsingReport()");
			logger.debug(e.toString());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return col;
	}

	public static Collection getAllConfigurablePages() {
		Session session = null;
		Query qry = null;
		Collection pages = new ArrayList();

		try {
			session = PersistenceManager.getSession();
			String queryString = "select p from " + AmpPages.class.getName()
					+ " p";
			qry = session.createQuery(queryString);
			pages = qry.list();
		} catch (Exception e) {
			logger.error("Unable to get all configurable pages");
			logger.debug("Exceptiion is :" + e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return pages;
	}

	public static Collection getAllPageFilters(Long id) {

		Session session = null;
		Collection col = null;

		try {
			session = PersistenceManager.getSession();
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
			logger.debug("Exception from getAllPageFilters()");
			logger.debug(e.toString());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return col;
	}

	public static AmpPages getAmpPage(Long pageId) {
		Session session = null;
		Query qry = null;
		AmpPages page = null;

		try {
			session = PersistenceManager.getSession();
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
			logger.error("Unable to get AmpPage");
			logger.debug("Exceptiion " + e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return page;
	}

	public static AmpFilters getAmpFilter(Long filterId) {
		Session session = null;
		Query qry = null;
		AmpFilters filter = null;

		try {
			session = PersistenceManager.getSession();
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
			logger.error("Unable to get AmpFilter");
			logger.debug("Exceptiion " + e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return filter;
	}

	/*
	public static Collection getAllTeamPageFiltersOfTeam(Long teamId) {
		Session session = null;
		Collection col = null;

		try {
			session = PersistenceManager.getSession();
			AmpTeam ampTeam = (AmpTeam) session.load(AmpTeam.class,teamId);
			Iterator itr = ampTeam.getTeamPageFilters().iterator();
			col = new ArrayList();
			while (itr.hasNext()) {
				AmpTeamPageFilters ampTpf = (AmpTeamPageFilters) itr.next();
				if (!col.contains(ampTpf)) {
					col.add(ampTpf);	
				}
			}
		} catch (Exception e) {
			logger.debug("Exception from getAllTeamPageFiltersOfTeam()");
			logger.debug(e.toString());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return col;
	}*/

	public static ArrayList getTeamPageFilters(Long teamId, Long pageId) {
		Session session = null;
		ArrayList col = new ArrayList();

		try {
			session = PersistenceManager.getSession();
			String qryStr = "select tpf.filter.ampFilterId from " +
					AmpTeamPageFilters.class.getName() + " tpf " +
							"where (tpf.team=:tId) and (tpf.page=:pId)";
			Query qry = session.createQuery(qryStr);
			qry.setParameter("tId",teamId,Hibernate.LONG);
			qry.setParameter("pId",pageId,Hibernate.LONG);
			
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
			    col.add((Long) itr.next());
			}
		} catch (Exception e) {
			logger.debug("Exception from getTeamPageFilters()");
			logger.debug(e.toString());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return col;
	}

	public static Collection getFilters(Long teamId, Long pageId) {
		Session session = null;
		Collection col = new ArrayList();

		try {
			session = PersistenceManager.getSession();
			String qryStr = "select tpf.filter.ampFilterId from " +
					AmpTeamPageFilters.class.getName() + " tpf " +
							"where (tpf.team=:tId) and (tpf.page=:pId)";
			Query qry = session.createQuery(qryStr);
			qry.setParameter("tId",teamId,Hibernate.LONG);
			qry.setParameter("pId",pageId,Hibernate.LONG);
			
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				Long fId = (Long) itr.next();
				AmpFilters filter = (AmpFilters) session.load(AmpFilters.class,fId);
			    col.add(filter);
			}
		} catch (Exception e) {
			logger.debug("Exception from getFilters()");
			logger.debug(e.toString());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return col;
	}	
	
	public static Collection getAllAssistanceTypes() {
		Session session = null;
		Collection col = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select ta from "
					+ AmpTermsAssist.class.getName() + " ta ";
			Query qry = session.createQuery(queryString);
			col = qry.list();
		} catch (Exception e) {
			logger.debug("Exception from getAllAssistanceType()");
			logger.debug(e.toString());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return col;
	}

	public static Collection getAllCountries() {
		Session session = null;
		Collection col = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select c from " + Country.class.getName()
					+ " c";
			Query qry = session.createQuery(queryString);
			col = qry.list();
		} catch (Exception e) {
			logger.debug("Exception from getAllCountries()");
			logger.debug(e.toString());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return col;
	}

	public static Country getDgCountry(String iso) {
		Session session = null;
		Country country = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select c from " + Country.class.getName()
					+ " c " + "where (c.iso=:iso)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("iso", iso, Hibernate.STRING);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				country = (Country) itr.next();
			}

		} catch (Exception e) {
			logger.debug("Exception from getDgCountry()");
			logger.debug(e.toString());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return country;
	}

	public static Country getCountryByName(String name) {
		Session session = null;
		Country country = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select c from " + Country.class.getName()
					+ " c " + "where (c.countryName=:name)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("name", name, Hibernate.STRING);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				country = (Country) itr.next();
			}

		} catch (Exception e) {
			logger.debug("Exception from getDgCountry()");
			logger.debug(e.toString());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return country;
	}

	public static Country getDgCountry(Long id) {
		Session session = null;
		Country country = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select c from " + Country.class.getName()
					+ " c " + "where (c.id=:id)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				country = (Country) itr.next();
			}

		} catch (Exception e) {
			logger.debug("Exception from getDgCountry()");
			logger.debug(e.toString());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return country;
	}

	public static AmpRegion getAmpRegion(Long id) {
		Session session = null;
		AmpRegion region = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select r from " + AmpRegion.class.getName()
					+ " r " + "where (r.ampRegionId=:id)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				region = (AmpRegion) itr.next();
			}

		} catch (Exception e) {
			logger.debug("Exception from getAmpRegion()");
			logger.debug(e.toString());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return region;
	}

	public static AmpZone getAmpZone(Long id) {
		Session session = null;
		AmpZone zone = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select z from " + AmpZone.class.getName()
					+ " z " + "where (z.ampZoneId=:id)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				zone = (AmpZone) itr.next();
			}

		} catch (Exception e) {
			logger.debug("Exception from getAmpZone()");
			logger.debug(e.toString());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return zone;
	}

	public static AmpWoreda getAmpWoreda(Long id) {
		Session session = null;
		AmpWoreda woreda = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select w from " + AmpWoreda.class.getName()
					+ " w " + "where (w.ampWoredaId=:id)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				woreda = (AmpWoreda) itr.next();
			}

		} catch (Exception e) {
			logger.debug("Exception from getAmpZone()");
			logger.debug(e.toString());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return woreda;
	}

	public static Collection getAllRegionsUnderCountry(String iso) {
		Session session = null;
		Collection col = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select reg from " + AmpRegion.class.getName()
					+ " reg " + "where country_id = '" + iso
					+ "'  order by reg.name";

			Query qry = session.createQuery(queryString);
			col = qry.list();
		} catch (Exception e) {
			logger.debug("Exception from getAllRegionsUnderCountry()");
			logger.debug(e.toString());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return col;
	}

	public static Collection getAllZonesUnderRegion(Long id) {
		Session session = null;
		Collection col = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select zon from " + AmpZone.class.getName()
					+ " zon " + "where region_id = " + id
					+ " order by zon.name";

			Query qry = session.createQuery(queryString);
			col = qry.list();
		} catch (Exception e) {
			logger.debug("Exception from getAllZonesUnderRegion()");
			logger.debug(e.toString());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return col;
	}

	public static Collection getAllWoredasUnderZone(Long id) {
		Session session = null;
		Collection col = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select wor from " + AmpWoreda.class.getName()
					+ " wor " + "where zone_id = " + id + " order by wor.name";

			Query qry = session.createQuery(queryString);
			col = qry.list();
		} catch (Exception e) {
			logger.debug("Exception from getAllWoredasUnderZone()");
			logger.debug(e.toString());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return col;
	}

	public static ArrayList getAmpModality() {
		Session session = null;
		Query q = null;
		AmpModality ampModality = null;
		ArrayList modality = new ArrayList();
		String queryString = null;
		Iterator iter = null;

		try {
			session = PersistenceManager.getSession();
			queryString = " select Modality from "
					+ AmpModality.class.getName()
					+ " Modality order by Modality.name";
			q = session.createQuery(queryString);
			iter = q.list().iterator();

			while (iter.hasNext()) {

				ampModality = (AmpModality) iter.next();
				modality.add(ampModality);
			}

		} catch (Exception ex) {

			logger
					.debug("Modality : Unable to get Amp Activity names  from database "
							+ ex.getMessage());

		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}
		return modality;
	}

	public static ArrayList getAmpStatus() {
		Session session = null;
		Query q = null;
		AmpStatus ampStatus = null;
		ArrayList status = new ArrayList();
		String queryString = null;
		Iterator iter = null;
		try {
			session = PersistenceManager.getSession();
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
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}
		return status;
	}

	public static ArrayList getAmpLevels() {
		Session session = null;
		Query q = null;
		AmpLevel ampLevel = null;
		ArrayList level = new ArrayList();
		String queryString = null;
		Iterator iter = null;
		try {
			session = PersistenceManager.getSession();
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
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}
		return level;
	}

	public static ArrayList getAmpSectors() {
		Session session = null;
		Query q = null;
		AmpSector ampSector = null;
		ArrayList sector = new ArrayList();
		String queryString = null;
		Iterator iter = null;

		try {
			session = PersistenceManager.getSession();
			queryString = " select Sector from "
					+ AmpSector.class.getName()
					+ " Sector where Sector.parentSectorId is null order by Sector.name";
			q = session.createQuery(queryString);
			iter = q.list().iterator();

			while (iter.hasNext()) {

				ampSector = (AmpSector) iter.next();
				sector.add(ampSector);
			}

		} catch (Exception ex) {
			logger.error("Unable to get Sector names  from database "
					+ ex.getMessage());
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}
		return sector;
	}

	public static ArrayList getAmpSubSectors() {
		Session session = null;
		Query q = null;
		AmpSector ampSector = null;
		ArrayList subsector = new ArrayList();
		String queryString = null;
		Iterator iter = null;

		try {
			session = PersistenceManager.getSession();
			queryString = " select Sector from "
					+ AmpSector.class.getName()
					+ " Sector where Sector.parentSectorId is not null order by Sector.name";
			q = session.createQuery(queryString);
			iter = q.list().iterator();
			while (iter.hasNext()) {
				ampSector = (AmpSector) iter.next();
				subsector.add(ampSector);
			}

		} catch (Exception ex) {
			logger.error("Unable to get Amp sector names  from database "
					+ ex.getMessage());
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}
		return subsector;
	}

	public static ArrayList getAmpSubSectors(Long ampSectorId) {
		Session session = null;
		Query q = null;
		AmpSector ampSector = null;
		ArrayList subsector = new ArrayList();
		String queryString = null;
		Iterator iter = null;

		try {
			session = PersistenceManager.getSession();
			queryString = " select Sector from "
					+ AmpSector.class.getName()
					+ " Sector where Sector.parentSectorId is not null and Sector.parentSectorId.ampSectorId=:ampSectorId";
			q = session.createQuery(queryString);
			q.setParameter("ampSectorId", ampSectorId, Hibernate.LONG);
			iter = q.list().iterator();

			while (iter.hasNext()) {

				ampSector = (AmpSector) iter.next();
				subsector.add(ampSector);
			}

		} catch (Exception ex) {
			logger.error("Unable to get Amp sub sectors  from database "
					+ ex.getMessage());
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}
		return subsector;
	}

	public static AmpSector getAmpParentSector(Long ampSectorId) {
		Session session = null;
		Query q = null;
		AmpSector ampSector = null;
		String queryString = null;
		Iterator iter = null;

		try {
			session = PersistenceManager.getSession();
			queryString = " select Sector from " + AmpSector.class.getName()
					+ " Sector where Sector.ampSectorId=:ampSectorId";
			q = session.createQuery(queryString);
			q.setParameter("ampSectorId", ampSectorId, Hibernate.LONG);
			iter = q.list().iterator();

			ampSector = (AmpSector) iter.next();
			while (ampSector.getParentSectorId() != null)
				ampSector = ampSector.getParentSectorId();
			//	ampSectorId=ampSector.getAmpSectorId();
			//	logger.debug("Sector Id: " + ampSectorId);

		} catch (Exception ex) {
			logger.error("Unable to get Amp sub sectors  from database "
					+ ex.getMessage());
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}
		return ampSector;
	}

	public static ArrayList getAmpLocations() {
		Session session = null;
		Query q = null;
		AmpRegion ampRegion = null;
		ArrayList region = new ArrayList();
		String queryString = null;
		Iterator iter = null;

		try {
			session = PersistenceManager.getSession();
			queryString = " select l from " + AmpRegion.class.getName()
					+ " l order by l.name";
			q = session.createQuery(queryString);
			iter = q.list().iterator();

			while (iter.hasNext()) {

				ampRegion = (AmpRegion) iter.next();
				region.add(ampRegion);
			}

		} catch (Exception ex) {
			logger.error("Unable to get Amp location names  from database "
					+ ex.getMessage());
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}
		return region;
	}

	public static Collection getFiscalCalOrgs(Long fiscalCalId) {

		Session sess = null;
		Collection col = null;
		Query qry = null;

		try {
			sess = PersistenceManager.getSession();
			String queryString = "select o from "
					+ AmpOrganisation.class.getName()
					+ " o where (o.ampFiscalCalId=:ampFisCalId)";
			qry = sess.createQuery(queryString);
			qry.setParameter("ampFisCalId", fiscalCalId, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			col = new ArrayList();
			while (itr.hasNext()) {
				col.add(itr.next());
			}

		} catch (Exception e) {
			logger.debug("Exception from getFiscalCalOrgs()");
			logger.debug(e.toString());
		} finally {
			try {
				if (sess != null) {
					PersistenceManager.releaseSession(sess);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return col;

	}

	public static Collection getFiscalCalSettings(Long fiscalCalId) {

		Session sess = null;
		Collection col = null;
		Query qry = null;

		try {
			sess = PersistenceManager.getSession();
			String queryString = "select o from "
					+ AmpApplicationSettings.class.getName()
					+ " o where (o.fiscalCalendar=:ampFisCalId)";
			qry = sess.createQuery(queryString);
			qry.setParameter("ampFisCalId", fiscalCalId, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			col = new ArrayList();
			while (itr.hasNext()) {
				col.add(itr.next());
			}

		} catch (Exception e) {
			logger.debug("Exception from getFiscalCalSettings()");
			logger.debug(e.toString());
		} finally {
			try {
				if (sess != null) {
					PersistenceManager.releaseSession(sess);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return col;

	}

	public static void addCurrency(AmpCurrency ampCurr,
			AmpCurrencyRate ampCurrRate) {
		add(ampCurr);
		add(ampCurrRate);
	}

	public static void deleteCurrency(AmpCurrency ampCurr,
			AmpCurrencyRate ampCurrRate) {
		delete(ampCurr);
		delete(ampCurrRate);
	}

	public static void updateCurrency(AmpCurrency ampCurr,
			AmpCurrencyRate ampCurrRate) {
		update(ampCurr);
		update(ampCurrRate);
	}

	public static AmpCurrency getCurrencyByCode(String currCode) {
		Session session = null;
		Query qry = null;
		AmpCurrency ampCurrency = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select c from " + AmpCurrency.class.getName()
					+ " c where (c.currencyCode=:currCode)";
			qry = session.createQuery(queryString);
			qry.setParameter("currCode", currCode, Hibernate.STRING);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				ampCurrency = (AmpCurrency) itr.next();
			}
		} catch (Exception e) {
			logger.error("Unable to get currency");
			logger.debug("Exceptiion " + e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return ampCurrency;
	}

	public static AmpFiscalCalendar getFiscalCalByName(String name) {
		Session session = null;
		Query qry = null;
		AmpFiscalCalendar ampFisCal = null;

		try {
			session = PersistenceManager.getSession();
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
			logger.error("Unable to get fiscal Calendar");
			logger.debug("Exceptiion " + e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return ampFisCal;
	}

	public static Currency getCurrency(Long id) {
		logger.debug("in getCurrency" + id);
		Session sess = null;
		Query qry1 = null;
		Query qry2 = null;
		Currency curr = null;

		try {
			sess = PersistenceManager.getSession();
			String queryString = "select c from " + AmpCurrency.class.getName()
					+ " c where (c.ampCurrencyId=:id)";
			qry1 = sess.createQuery(queryString);
			qry1.setParameter("id", id, Hibernate.LONG);
			Iterator itr = qry1.list().iterator();
			if (itr.hasNext()) {
				AmpCurrency ampCurr = (AmpCurrency) itr.next();
				String qryStr = "select cr from "
						+ AmpCurrencyRate.class.getName()
						+ " cr where (cr.toCurrencyCode=:currCode)";
				qry2 = sess.createQuery(qryStr);
				qry2.setParameter("currCode", ampCurr.getCurrencyCode(),
						Hibernate.STRING);
				Iterator itr1 = qry2.list().iterator();
				if (itr1.hasNext()) {
					AmpCurrencyRate ampCurrRate = (AmpCurrencyRate) itr1.next();
					curr = new Currency();
					curr.setCurrencyId(ampCurr.getAmpCurrencyId());
					curr.setCurrencyRateId(ampCurrRate.getAmpCurrencyRateId());
					curr.setCurrencyCode(ampCurr.getCurrencyCode());
					curr.setCountryName(ampCurr.getCountryName());
					curr.setExchangeRate(ampCurrRate.getExchangeRate());
				}
			}

		} catch (Exception e) {
			logger.debug("Exception from getCurrency()");
			logger.debug(e.toString());
		} finally {
			try {
				if (sess != null) {
					PersistenceManager.releaseSession(sess);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		logger.debug("out");
		return curr;
	}

	public static AmpCurrency getAmpcurrency(Long id) {
		AmpCurrency ampCurrency = null;
		Session session = null;

		try {
			session = PersistenceManager.getSession();
			// modified by Priyajith
			// desc:used select query instead of session.load
			// start
			String queryString = "select c from " + AmpCurrency.class.getName()
					+ " c " + "where (c.ampCurrencyId=:id)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				ampCurrency = (AmpCurrency) itr.next();
			}
			// end
		} catch (Exception ex) {
			logger.error("Unable to get currency " + ex);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.debug("releaseSession() failed", ex2);
			}
		}
		return ampCurrency;
	}

	public static Collection searchForOrganisation(String keyword, Long orgType) {
		Session session = null;
		Collection col = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select distinct org from "
					+ AmpOrganisation.class.getName() + " org "
					+ "where (acronym like '%" + keyword + "%' || name like '%"
					+ keyword + "%') and org.orgTypeId=:orgType";
			Query qry = session.createQuery(queryString);
			qry.setParameter("orgType", orgType, Hibernate.LONG);
			col = qry.list();
		} catch (Exception ex) {
			logger.debug("Unable to search " + ex);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.debug("releaseSession() failed", ex2);
			}
		}
		return col;
	}

	public static Collection searchForOrganisation(String keyword) {
		Session session = null;
		Collection col = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select distinct org from "
					+ AmpOrganisation.class.getName() + " org "
					+ "where (acronym like '%" + keyword + "%' || name like '%"
					+ keyword + "%')";
			Query qry = session.createQuery(queryString);
			col = qry.list();
		} catch (Exception ex) {
			logger.debug("Unable to search " + ex);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.debug("releaseSession() failed", ex2);
			}
		}
		return col;
	}

	public static Collection searchForOrganisationByType(Long orgType) {
		Session session = null;
		Collection col = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select distinct org from "
					+ AmpOrganisation.class.getName() + " org "
					+ "where org.orgTypeId=:orgType";
			Query qry = session.createQuery(queryString);
			qry.setParameter("orgType", orgType, Hibernate.LONG);
			col = qry.list();
		} catch (Exception ex) {
			logger.debug("Unable to search " + ex);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.debug("releaseSession() failed", ex2);
			}
		}
		return col;
	}

	public static ArrayList getAmpOrganisations() {
		Session session = null;
		Query q = null;
		AmpOrganisation ampOrganisation = null;
		ArrayList organisation = new ArrayList();
		String queryString = null;
		Iterator iter = null;

		try {
			session = PersistenceManager.getSession();
			queryString = " select org from " + AmpOrganisation.class.getName()
					+ " org order by org.name";
			q = session.createQuery(queryString);
			iter = q.list().iterator();

			while (iter.hasNext()) {
				ampOrganisation = (AmpOrganisation) iter.next();
				organisation.add(ampOrganisation);
			}

		} catch (Exception ex) {
			logger.error("Unable to get Amp organisation names  from database "
					+ ex.getMessage());
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}
		return organisation;
	}

	public static Collection getFundingDetWithCurrId(Long currId) {
		Session sess = null;
		Collection col = null;
		Query qry = null;

		try {
			sess = PersistenceManager.getSession();
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
			logger.debug("Exception from getFundingDetWithCurrId()");
			logger.debug(e.toString());
		} finally {
			try {
				if (sess != null) {
					PersistenceManager.releaseSession(sess);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return col;
	}

	public static Collection getActivityTheme(Long themeId) {
		Session sess = null;
		Collection col = null;
		Query qry = null;

		try {
			sess = PersistenceManager.getSession();
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
			logger.debug("Exception from getActivityTheme()");
			logger.debug(e.toString());
		} finally {
			try {
				if (sess != null) {
					PersistenceManager.releaseSession(sess);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return col;
	}

	public static void add(Object object) {
		logger.debug("In add " + object.getClass().getName());
		Session sess = null;
		Transaction tx = null;

		try {
			sess = PersistenceManager.getSession();
			tx = sess.beginTransaction();
			sess.save(object);
			tx.commit();
		} catch (Exception e) {
			logger.error("Unable to add");
			logger.debug(e.toString());

			if (tx != null) {
				try {
					tx.rollback();
				} catch (HibernateException ex) {
					logger.debug("rollback() failed");
					logger.debug(ex.toString());
				}
			}
		} finally {
			if (sess != null) {
				try {
					PersistenceManager.releaseSession(sess);
				} catch (Exception ex) {
					logger.error("releaseSession() failed ");
					logger.debug(ex.toString());
				}
			}
		}
	}

	public static void update(Object object) {
		Session sess = null;
		Transaction tx = null;

		try {
			sess = PersistenceManager.getSession();
			tx = sess.beginTransaction();
			sess.update(object);
			tx.commit();
		} catch (Exception e) {
			logger.error("Unable to update");
			logger.debug(e.toString());
			if (tx != null) {
				try {
					tx.rollback();
				} catch (HibernateException ex) {
					logger.debug("rollback() failed");
					logger.debug(ex.toString());
				}
			}
		} finally {
			if (sess != null) {
				try {
					PersistenceManager.releaseSession(sess);
				} catch (Exception ex) {
					logger.error("releaseSession() failed ");
					logger.debug(ex.toString());
				}
			}
		}
	}

	public static void delete(Object object) {
		Session sess = null;
		Transaction tx = null;

		try {
			sess = PersistenceManager.getSession();
			tx = sess.beginTransaction();
			logger.debug("before delete");
			sess.delete(object);
			//sess.flush();
			tx.commit();
		} catch (Exception e) {
			logger.error("Exception " + e.toString());
			try {
				tx.rollback();
			} catch (HibernateException ex) {
				logger.error("rollback() failed");
				logger.error(ex.toString());
			}
		} finally {
			try {
				PersistenceManager.releaseSession(sess);
			} catch (Exception e) {
				logger.error("releaseSession() failed");
				logger.error(e.toString());
			}
		}
	}

	public static void deleteStatus(Long id) {
		AmpStatus oldStatusItem = new AmpStatus();
		Session sess = null;
		Transaction tx = null;

		try {
			sess = PersistenceManager.getSession();
			tx = sess.beginTransaction();
			logger.debug("BEFORE SESS.SAVE()");
			oldStatusItem = (AmpStatus) sess.load(AmpStatus.class, id);

			if (sess != null) {
				logger.debug("DbUtil session is not null");

				logger.debug("DbUtil deleteStatus id : "
						+ oldStatusItem.getAmpStatusId());

				sess.delete(oldStatusItem);

				logger.debug("AFTER SESS.SAVE()");

				tx.commit();
			} else
				logger.debug("DbUtil session is null");
		} catch (Exception ex) {
			logger.error("Unable to Delete Amp status record");
			logger.debug(ex.toString());

			if (tx != null) {
				try {
					tx.rollback();
				} catch (HibernateException ex1) {
					logger.debug("rollback() failed ");
				}
			}
		} finally {
			if (sess != null) {
				try {
					PersistenceManager.releaseSession(sess);
				} catch (Exception ex1) {
					logger
							.debug("DbUtil: updateStatus - releaseSession() failed ");
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
			session = PersistenceManager.getSession();
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
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}

		logger.debug("getQuarters() collection size returned : "
				+ (c != null ? c.size() : 0));
		return c;
	}

	/**
	 * @author jose A fiscal calendar id is passed and the start month and start
	 *              day of the corrsponding fiscal calendar is returned wrapped in
	 *              object FiscalCalendar. The month starts from 1 that is Jan is 1,
	 *              Feb is 2.
	 * @param fiscalCalId
	 * @return FiscalCalendar
	 */
	public static FiscalCalendar getFiscalCalendar(Long fiscalCalId) {
		logger.debug("getFiscalCalendar with fiscalCalId" + fiscalCalId);

		Session session = null;
		Query q = null;
		Collection c = null;
		Iterator iter = null;
		FiscalCalendar fc = new FiscalCalendar();
		try {
			session = PersistenceManager.getSession();
			String queryString = new String();
			queryString = "select f.startMonthNum,f.startDayNum from "
					+ AmpFiscalCalendar.class.getName()
					+ " f where (f.ampFiscalCalId=:fiscalCalId) ";
			q = session.createQuery(queryString);
			q.setParameter("fiscalCalId", fiscalCalId, Hibernate.LONG);
			c = q.list();
			if (c.size() != 0) {
				iter = c.iterator();
				if (iter.hasNext()) {
					Object[] row = (Object[]) iter.next();
					Integer month = (Integer) row[0];
					Integer day = (Integer) row[1];
					fc.setStartMonth(month.intValue());
					fc.setStartDay(day.intValue());
				}
			} else {
				logger
						.error("Unable to get start month and start day of fiscal calendar");
				fc.setStartMonth(-1);
				fc.setStartDay(-1);
			}

		} catch (Exception ex) {
			logger.error("Unable to get fiscal calendar from database", ex);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		logger.debug("getFiscalCalendar Start month:" + fc.getStartMonth());
		logger.debug("getFiscalCalendar Start day:" + fc.getStartDay());
		return fc;
	}

	public static Collection getMaxFiscalYears() {
		Session session = null;
		Query q = null;
		Collection c = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select Max(f.fiscalYear), Min(f.fiscalYear) from "
					+ AmpFundingDetail.class.getName() + " f ";

			q = session.createQuery(queryString);

			c = q.list();

		} catch (Exception ex) {
			logger.debug("Unable to get  Max fiscal years from database"
					+ ex.getMessage());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		//logger.debug("getFiscalYears() collection size returned : " + ( c !=
		// null ? c.size() : 0 ) ) ;
		return c;
	}

	public static List getAmpAssistanceType(Long ampActivityId) {
		Session session = null;
		Query q = null;
		List c = new ArrayList();

		try {
			session = PersistenceManager.getSession();
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
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		//logger.debug("getFiscalYears() collection size returned : " + ( c !=
		// null ? c.size() : 0 ) ) ;
		return c;
	}

	public static ArrayList getDonorSectors(Long ampSecSchemeId,
			Long ampActivityId) {
		logger.debug("In getDonorSectors");
		Session session = null;
		Query q = null;
		AmpSector ampSector = null;
		ArrayList sector = new ArrayList();
		String queryString = null;
		Iterator iter = null;

		try {
			session = PersistenceManager.getSession();
			//queryString = " select Sector from " + AmpSector.class.getName()
			// + " Sector where Sector.parentSectorId is null order by
			// Sector.name";

			queryString = "select sector from "
					+ AmpSector.class.getName()
					+ " sector, "
					+ AmpActivity.class.getName()
					+ " act where (sector.ampSecSchemeId = :ampSecSchemeId) and (act.ampActivityId = :ampActivityId) and sector.parentSectorId is null";
			//queryString = "select sector from " + AmpSector.class.getName() +
			// " sector, " + AmpActivity.class.getName() + " act where
			// (sector.ampSecSchemeId = :ampSecSchemeId) and (act.ampActivityId
			// = :ampActivityId)";
			q = session.createQuery(queryString);
			q.setParameter("ampSecSchemeId", ampSecSchemeId, Hibernate.LONG);
			q.setParameter("ampActivityId", ampActivityId, Hibernate.LONG);

			iter = q.list().iterator();

			while (iter.hasNext()) {
				ampSector = (AmpSector) iter.next();
				sector.add(ampSector);
			}
		} catch (Exception ex) {
			logger.error("Unable to get Sector names  from database "
					+ ex.getMessage());
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}
		return sector;
	}

	public static ArrayList getAmpCurrency() {
		AmpCurrency ampCurrency = null;
		Session session = null;
		Query q = null;
		Iterator iter = null;
		String queryString = null;
		ArrayList currency = new ArrayList();
		try {
			session = PersistenceManager.getSession();
			queryString = " select c from " + AmpCurrency.class.getName()
					+ " c where c.activeFlag='1' order by c.currencyName";
			q = session.createQuery(queryString);
			iter = q.list().iterator();

			while (iter.hasNext()) {
				ampCurrency = (AmpCurrency) iter.next();
				currency.add(ampCurrency);
			}
		} catch (Exception ex) {
			logger.error("Unable to get currency " + ex);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.debug("releaseSession() failed", ex2);
			}
		}
		return currency;
	}

	public static Date getClosingDate(Long ampFundingId, Integer type) {

		logger.debug("getClosingDate() with ampFundingId=" + ampFundingId
				+ " type=" + type);

		Session session = null;
		Query q = null;
		Collection c = null;
		Iterator iter = null;
		Date d = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = new String();
			queryString = "select a.closingDate from "
					+ AmpClosingDateHistory.class.getName()
					+ " a where (a.ampFundingId=:ampFundingId) "
					+ " and (a.type=:type)";
			q = session.createQuery(queryString);
			q.setParameter("ampFundingId", ampFundingId, Hibernate.LONG);
			q.setParameter("type", type, Hibernate.INTEGER);
			c = q.list();
			if (c.size() != 0) {
				iter = c.iterator();
				if (iter.hasNext()) {
					d = (Date) iter.next();
				}
			}
		} catch (Exception ex) {
			logger.error("Unable to get closing date from database", ex);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		logger.debug("getClosingDate() returning date:" + d);
		return d;
	}

	public static String getGoeId(Long ampActivityId) {

		logger.debug("getGoeId() with ampActivityId=" + ampActivityId);
		Long ampDonorOrgId = Constants.MOFED_ORG_ID;
		Session session = null;
		Query q = null;
		Collection c = null;
		Iterator iter = null;
		String s = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = new String();
			queryString = "select a.internalId from "
					+ AmpActivityInternalId.class.getName()
					+ " a where (a.ampActivityId=:ampActivityId) "
					+ " and (a.ampOrgId=:ampDonorOrgId)";
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
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
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
			session = PersistenceManager.getSession();
			String queryString = new String();
			queryString = "select f from "
					+ AmpFunding.class.getName()
					+ " f where (f.ampDonorOrgId =:ampDonorOrgId) and (f.ampTermsAssistId =:ampTermsAssistId) and (f.ampActivityId =:ampActivityId)";
			logger.debug("querystring " + queryString);
			q = session.createQuery(queryString);
			q.setParameter("ampActivityId", ampActivityId, Hibernate.LONG);
			q.setParameter("ampDonorOrgId", ampDonorOrgId, Hibernate.LONG);
			q
					.setParameter("ampTermsAssistId", ampTermsAssistId,
							Hibernate.LONG);
			funding = q.list();
		} catch (Exception ex) {
			logger.error("Unable to get Funding records from database", ex);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		logger.debug("Returning Funding Grant : "
				+ (funding != null ? funding.size() : 0));
		return funding;

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
			ArrayList dbReturnSet = (ArrayList) DbUtil
					.getAmpLevel0Teams(ampTeamId);
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
			session = PersistenceManager.getSession();
			String queryString = new String();
			queryString = "select activity from " + AmpActivity.class.getName()
					+ " activity where activity.team.ampTeamId in(" + inClause
					+ ")";
			q = session.createQuery(queryString);
			logger.debug("Activity List: " + q.list().size());
			iterActivity = q.list().iterator();
			while (iterActivity.hasNext()) {
				AmpActivity ampActivity = (AmpActivity) iterActivity.next();
				
//				logger.debug("Org Role List: " + ampActivity.getOrgrole().size());
				iter = ampActivity.getOrgrole().iterator();
				while (iter.hasNext()) {
					AmpOrgRole ampOrgRole = (AmpOrgRole) iter.next();
					if (ampOrgRole.getRole().getRoleCode().equals(
							Constants.FUNDING_AGENCY)) {
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
			logger.debug("Unable to get Donor " + ex.getMessage());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
			}
		}
		return donor;
	}

	public static Collection getDonorFund1(Long ampFundingId,
			Integer transactionType, Integer adjustmentType, String perspective) {
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
			session = PersistenceManager.getSession();
			String queryString = queryString = "select f from "
					+ AmpFundingDetail.class.getName()
					+ " f where (f.ampFundingId=:ampFundingId) "
					+ " and (f.transactionType=:transactionType) "
					+ " and (f.adjustmentType=:adjustmentType) "
					+ " and (f.orgRoleCode=:perspective) ";

			q = session.createQuery(queryString);
			q.setParameter("ampFundingId", ampFundingId, Hibernate.LONG);
			q.setParameter("perspective", perspective, Hibernate.STRING);
			q.setParameter("transactionType", transactionType,
					Hibernate.INTEGER);
			q.setParameter("adjustmentType", adjustmentType, Hibernate.INTEGER);
			ampFundings = q.list();
			logger.debug("size of result " + ampFundings.size());
			/*
			 * iter = list.iterator() ; while ( iter.hasNext() ) {
			 * AmpFundingDetail fundDetails = new AmpFundingDetail();
			 * fundDetails = (AmpFundingDetail)iter.next();
			 * if(fundDetails.getAmpCurrencyId().getCurrencyCode().equals("USD")) {
			 * //logger.debug("equals USD"); total = total +
			 * fundDetails.getTransactionAmount().doubleValue() ; } else {
			 * //logger.debug(" not equal to USD ") ; total = total +
			 * CurrencyWorker.convert(fundDetails.getTransactionAmount().doubleValue(),"USD") ;
			 * //logger.debug("AFTER conversion total is " + total); }
			 * 
			 *  }
			 */
			//logger.debug("Final Total is " + total);
		} catch (Exception ex) {
			logger.debug("Unable to get sum of funds from database"
					+ ex.getMessage());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}

		return ampFundings;
	}

	public static double getAmpFundingAmount(Long ampActivityId,
			Integer transactionType, Integer adjustmentType,
			String perspective, String ampCurrencyCode) {
		Session session = null;
		Query q = null;
		Iterator iter = null;
		String inClause = null;
		double fromCurrency = 0.0;
		double toCurrency = 1.0;
		double amount = 0.0;
		try {
			session = PersistenceManager.getSession();
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
					+ " adjustmentType " + adjustmentType + "perspective"
					+ perspective + "ampCurrencyCode" + ampCurrencyCode);
			queryString = queryString = "select fd from "
					+ AmpFundingDetail.class.getName()
					+ " fd where (fd.transactionType=:transactionType) "
					+ " and (fd.adjustmentType=:adjustmentType) "
					+ " and (fd.orgRoleCode=:perspective) "
					+ " and (fd.ampFundingId in(" + inClause + "))";
			logger.debug("queryString :" + queryString);
			q = session.createQuery(queryString);
			q.setParameter("perspective", perspective, Hibernate.STRING);
			q.setParameter("transactionType", transactionType,
					Hibernate.INTEGER);
			q.setParameter("adjustmentType", adjustmentType, Hibernate.INTEGER);
			iter = q.list().iterator();
			logger.debug("Size: " + q.list().size());
			while (iter.hasNext()) {
				AmpFundingDetail ampFundingDetail = (AmpFundingDetail) iter
						.next();
				toCurrency = DbUtil.getExchangeRate(ampCurrencyCode,
						adjustmentType.intValue(), ampFundingDetail
								.getTransactionDate());
				fromCurrency = DbUtil.getExchangeRate(ampFundingDetail
						.getAmpCurrencyId().getCurrencyCode(), adjustmentType
						.intValue(), ampFundingDetail.getTransactionDate());
				logger.debug("to Currency: " + toCurrency);
				logger.debug("From Currency: " + fromCurrency);

				amount = amount
						+ CurrencyWorker.convert1(ampFundingDetail
								.getTransactionAmount().doubleValue(),
								fromCurrency, toCurrency);
			}
			logger.debug("Amount: " + amount);

		} catch (Exception ex) {
			logger.debug("Unable to get sum of funds from database"
					+ ex.getMessage());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
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
			session = PersistenceManager.getSession();
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
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
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
			session = PersistenceManager.getSession();
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
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
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
			session = PersistenceManager.getSession();
			String queryString = new String();
			queryString = "select o from " + AmpOrganisation.class.getName()
					+ " o where (o.ampSecSchemeId=:ampSecSchemeId ) ";
			q = session.createQuery(queryString);
			q.setParameter("ampSecSchemeId", ampSecSchemeId, Hibernate.LONG);

			ampOrgs = q.list();
			logger.debug("DbUtil : getOrgId() returning collection of size  "
					+ ampOrgs.size());

		} catch (Exception ex) {
			logger.error("Unable to get AmpFunding collection from database",
					ex);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}

		return ampOrgs;
	}

	public static Collection searchSectorCode(String key) {
		Session sess = null;
		Collection col = null;
		Query qry = null;

		try {
			sess = PersistenceManager.getSession();
			String queryString = "select s from " + AmpSector.class.getName()
					+ " s where s.sectorCode like '%" + key + "%'";
			qry = sess.createQuery(queryString);
			Iterator itr = qry.list().iterator();
			col = new ArrayList();
			while (itr.hasNext()) {
				col.add(itr.next());
			}

		} catch (Exception e) {
			logger.debug("Exception from searchSectorCode()");
			logger.debug(e.toString());
		} finally {
			try {
				if (sess != null) {
					PersistenceManager.releaseSession(sess);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return col;

	}

	public static Collection searchSectorName(String key) {
		Session sess = null;
		Collection col = null;
		Query qry = null;

		try {
			sess = PersistenceManager.getSession();
			String queryString = "select s from " + AmpSector.class.getName()
					+ " s where s.name like '%" + key + "%'";
			qry = sess.createQuery(queryString);
			Iterator itr = qry.list().iterator();
			col = new ArrayList();
			while (itr.hasNext()) {
				col.add(itr.next());
			}

		} catch (Exception e) {
			logger.debug("Exception from searchSectorName()");
			logger.debug(e.toString());
		} finally {
			try {
				if (sess != null) {
					PersistenceManager.releaseSession(sess);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return col;
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
			session = PersistenceManager.getSession();
			queryString = " select s from " + AmpStatus.class.getName() + " s ";
			q = session.createQuery(queryString);
			iter = q.list().iterator();

			while (iter.hasNext()) {
				ampStatus = (AmpStatus) iter.next();
				logger.debug("Amp Status Id :"
						+ (Long) ampStatus.getAmpStatusId());
				logger.debug("Amp Status Code :"
						+ (String) ampStatus.getStatusCode());
				logger
						.debug("Amp Status Name :"
								+ (String) ampStatus.getName());
				ampStatusList.add(ampStatus);
			}

		} catch (Exception ex) {
			logger.debug("Unable to get Amp Status records  from database "
					+ ex.getMessage());
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.debug("releaseSession() failed ");
			}
		}
		return ampStatusList;
	}

	public static AmpStatus getAmpStatus(Long id) {
		AmpStatus statusItem = new AmpStatus();
		Session session = null;
		try {
			session = PersistenceManager.getSession();
			statusItem = (AmpStatus) session.load(AmpStatus.class, id);
		} catch (Exception ex) {
			logger.error("DbUtil:getAmpStatus: Unable to get Amp Status ", ex);
			//System.out.println(ex.toString()) ;
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
			}
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
			session = PersistenceManager.getSession();
			String queryString = new String();
			queryString = " select sum(fd.transactionAmount) from "
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
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.debug("releaseSession() failed ");
			}
		}//finally
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
			session = PersistenceManager.getSession();
			String queryString = new String();
			queryString = " select sum(fd.transactionAmount) from "
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
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.debug("releaseSession() failed ");
			}
		}//finally
		if (amount == null) {
			logger.debug("RETURNING ZERO");
			return 0;
		} else {
			return amount.doubleValue();
		}
	}

	public static double getExchangeRate(String currencyCode) {
		if (logger.isDebugEnabled())
			logger.debug("getExchangeRate with currencyCode" + currencyCode);

		Session session = null;
		Query q = null;
		Collection c = null;
		Iterator iter = null;
		Double exchangeRate = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = new String();
			queryString = "select f.exchangeRate from "
					+ AmpCurrencyRate.class.getName()
					+ " f where (f.toCurrencyCode=:currencyCode) ";
			q = session.createQuery(queryString);
			q.setParameter("currencyCode", currencyCode, Hibernate.STRING);
			c = q.list();
			if (c.size() != 0) {
				iter = c.iterator();
				if (iter.hasNext()) {
					exchangeRate = (Double) iter.next();
				}
			} else {
				if (logger.isDebugEnabled())
					logger
							.debug("Unable to get exchange rate for currency code :"
									+ currencyCode);
				exchangeRate = new Double(1.0);
			}
		} catch (Exception ex) {
			logger.debug("Unable to get exchange rate from database", ex);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
/*		if (logger.isDebugEnabled())
			logger.debug("getExchangeRate() for currency code :" + currencyCode
					+ "returns " + exchangeRate); */
		return exchangeRate.doubleValue();
	}

	public static double getExchangeRate(String currencyCode,
			int adjustmentType, Date exchangeRateDate) {
		if (logger.isDebugEnabled())
			logger.debug("getExchangeRate with currencyCode" + currencyCode);

		Session session = null;
		Query q = null;
		Collection c = null;
		Iterator iter = null;
		Double exchangeRate = null;

		try {
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(exchangeRateDate);
			String currencyDate=calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH)+1) + "-01";
		/*	logger.debug("Date: " + exchangeRateDate);
			logger.debug("Exchange Rate Month: " + calendar.get(Calendar.MONTH));
			logger.debug("Exchange Rate Year: " + calendar.get(Calendar.YEAR));
			logger.debug("Currency Code: " + currencyCode);*/
			session = PersistenceManager.getSession();
			String queryString = new String();
			queryString = "select f.exchangeRate from "
					+ AmpCurrencyRate.class.getName()
					+ " f where (f.toCurrencyCode='" + currencyCode + "') and (f.exchangeRateDate='" + currencyDate + "')";
//			logger.debug("queryString:" + queryString);
			q = session.createQuery(queryString);
//			q.setParameter("currencyCode", currencyCode, Hibernate.STRING);
			
//			q.setParameter("exchangeRateDate", exchangeRateDate,Hibernate.DATE);
			if (q.list().size() > 0)
				exchangeRate = (Double) q.list().get(0);
			else {
				queryString = "select f.exchangeRate from "
						+ AmpCurrencyRate.class.getName()
						+ " f where (f.toCurrencyCode=:currencyCode) and (f.exchangeRateDate<:exchangeRateDate) order by f.exchangeRateDate desc";
				q = session.createQuery(queryString);
					q.setParameter("currencyCode", currencyCode,
							Hibernate.STRING);
					q.setParameter("exchangeRateDate", exchangeRateDate,
							Hibernate.DATE);
					if (q.list().size() > 0)
						exchangeRate = (Double) q.list().get(0);
					else {
						queryString = "select f.exchangeRate from "
								+ AmpCurrencyRate.class.getName()
								+ " f where (f.toCurrencyCode=:currencyCode) and (f.exchangeRateDate>:exchangeRateDate) order by f.exchangeRateDate";
						q = session.createQuery(queryString);
						q.setParameter("currencyCode", currencyCode,
								Hibernate.STRING);
						q.setParameter("exchangeRateDate", exchangeRateDate,
								Hibernate.DATE);
						if (q.list().size() > 0)
							exchangeRate = (Double) q.list().get(0);
					}
				}
		
		} catch (Exception ex) {
			logger.debug("Unable to get exchange rate from database " + ex.getMessage());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
/*		if (logger.isDebugEnabled())
			logger.debug("getExchangeRate() for currency code :" + currencyCode
					+ "returns " + exchangeRate); */
		return exchangeRate.doubleValue();
	}

	public static Collection getAmpCurrencyRate() {

		Session session = null;
		Query q = null;
		Collection c = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = new String();
			queryString = "select currency from "
					+ AmpCurrencyRate.class.getName() + " currency";
			q = session.createQuery(queryString);
			c = q.list();
		} catch (Exception ex) {
			logger.debug("Unable to get exchange rate from database", ex);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return c;
	}

	public static double getExchangeRate(Long ampFundingId, String orgRoleCode) {

		if (logger.isDebugEnabled())
			logger.debug("getExchangeRate with ampFundingId=" + ampFundingId
					+ " orgRoleCode=" + orgRoleCode);

		Session session = null;
		Query q = null;
		Collection c = null;
		Iterator iter = null;
		AmpCurrency ampCurrency = null;
		String currencyCode = "";
		double exchangeRate = 1.0;

		try {
			session = PersistenceManager.getSession();
			String queryString = new String();
			queryString = "select f.ampCurrencyId from "
					+ AmpFundingDetail.class.getName()
					+ " f where (f.ampFundingId=:ampFundingId) and (f.orgRoleCode=:orgRoleCode)"
					+ " group by f.ampFundingId";
			q = session.createQuery(queryString);
			q.setParameter("ampFundingId", ampFundingId, Hibernate.LONG);
			q.setParameter("orgRoleCode", orgRoleCode, Hibernate.STRING);
			c = q.list();
			if (c.size() != 0) {
				iter = c.iterator();
				if (iter.hasNext()) {
					ampCurrency = (AmpCurrency) iter.next();
				}
			} else {
				if (logger.isDebugEnabled())
					logger
							.debug("Unable to get ampCurrencyId from table AmpFunding");
			}

			if (ampCurrency != null) {
				currencyCode = ampCurrency.getCurrencyCode();
				exchangeRate = getExchangeRate(currencyCode);
			}
		} catch (Exception ex) {
			logger.debug("Unable to get exchange rate from database", ex);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		logger.debug("getExchangeRate(id,orgRoleCode " + exchangeRate);
		return exchangeRate;
	}

	public static ArrayList getProjects() {
		Session session = null;
		Query q = null;
		AmpActivity ampActivity = new AmpActivity();
		ArrayList project = new ArrayList();
		List list = null;
		try {
			session = PersistenceManager.getSession();
			String queryString = new String();

			queryString = " select a from " + AmpActivity.class.getName()
					+ " a ";
			//logger.debug("querystring " + queryString);
			q = session.createQuery(queryString);
			Iterator iter = q.list().iterator();

			while (iter.hasNext()) {
				ampActivity = (AmpActivity) iter.next();
				project.add(ampActivity);
			}

		} catch (Exception ex) {

			logger
					.debug("Projects : Unable to get Amp Activity names from database "
							+ ex.getMessage());

		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.debug("releaseSession() failed ");
			}
		}
		return project;
	}

	public static double getDonorFund(Long ampFundingId,
			Integer transactionType, Integer adjustmentType, String perspective) {
		logger.debug("getTotalDonorFund() with ampFundingId " + ampFundingId
				+ " transactionType " + transactionType + " adjustmentType "
				+ adjustmentType + " perspective " + perspective);
		Session session = null;
		Query q = null;
		List list = null;
		Iterator iter = null;
		double total = 0.0;

		try {
			session = PersistenceManager.getSession();
			String queryString = new String();

			queryString = "select f from " + AmpFundingDetail.class.getName()
					+ " f where (f.ampFundingId=:ampFundingId) "
					+ " and (f.orgRoleCode=:perspective) "
					+ " and (f.transactionType=:transactionType) "
					+ " and (f.adjustmentType=:adjustmentType) ";

			q = session.createQuery(queryString);
			q.setParameter("ampFundingId", ampFundingId, Hibernate.LONG);
			q.setParameter("perspective", perspective, Hibernate.STRING);
			q.setParameter("transactionType", transactionType,
					Hibernate.INTEGER);
			q.setParameter("adjustmentType", adjustmentType, Hibernate.INTEGER);
			list = q.list();
			logger.debug("size of result " + list.size());
			iter = list.iterator();
			while (iter.hasNext()) {
				AmpFundingDetail fundDetails = new AmpFundingDetail();
				fundDetails = (AmpFundingDetail) iter.next();
				if (fundDetails.getAmpCurrencyId().getCurrencyCode().equals(
						"USD")) { //logger.debug("equals USD");
					total = total
							+ fundDetails.getTransactionAmount().doubleValue();
				} else { //logger.debug(" not equal to USD ") ;
					double fromCurrency = DbUtil.getExchangeRate(fundDetails
							.getAmpCurrencyId().getCurrencyCode());
					double toCurrency = DbUtil.getExchangeRate("USD");
					//total = total +
					// CurrencyWorker.convert(fundDetails.getTransactionAmount().doubleValue(),"USD")
					// ;
					total = total
							+ CurrencyWorker.convert1(fundDetails
									.getTransactionAmount().doubleValue(),
									fromCurrency, toCurrency);
					//logger.debug("AFTER conversion total is " + total);
				}

			}
		} catch (Exception ex) {
			logger.debug("Unable to get sum of funds from database", ex);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}

		return total;
	}

	public static double getDonorFundbyFiscalYear(Long ampFundingId,
			Integer transactionType, Integer adjustmentType,
			String perspective, Integer fiscalYear) {
		Session session = null;
		Query q = null;
		List list = null;
		Iterator iter = null;
		double total = 0.0;

		try {
			session = PersistenceManager.getSession();
			String queryString = new String();

			queryString = "select f from " + AmpFundingDetail.class.getName()
					+ " f where (f.ampFundingId=:ampFundingId) "
					+ " and (f.orgRoleCode=:perspective) "
					+ " and (f.transactionType=:transactionType) "
					+ " and (f.adjustmentType=:adjustmentType) "
					+ " and (f.fiscalYear=:fiscalYear) ";

			q = session.createQuery(queryString);
			q.setParameter("ampFundingId", ampFundingId, Hibernate.LONG);
			q.setParameter("perspective", perspective, Hibernate.STRING);
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

				double fromCurrency = DbUtil.getExchangeRate(fundDetails
						.getAmpCurrencyId().getCurrencyCode());
				double toCurrency = DbUtil.getExchangeRate("USD");

				if (fundDetails.getAmpCurrencyId().getCurrencyCode().equals(
						"USD")) {
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
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}

		return total;
	}

	public static double getDonorFundbyFiscalYear(Long ampFundingId,
			Integer transactionType, Integer adjustmentType,
			String perspective, Integer fiscalYear, Integer fiscalQuarter) {
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
			session = PersistenceManager.getSession();
			String queryString = new String();

			queryString = "select f from " + AmpFundingDetail.class.getName()
					+ " f where (f.ampFundingId=:ampFundingId) "
					+ " and (f.orgRoleCode=:perspective) "
					+ " and (f.transactionType=:transactionType) "
					+ " and (f.adjustmentType=:adjustmentType) "
					+ " and (f.fiscalYear=:fiscalYear) "
					+ " and (f.fiscalQuarter=:fiscalQuarter) ";
			q = session.createQuery(queryString);
			q.setParameter("ampFundingId", ampFundingId, Hibernate.LONG);
			q.setParameter("perspective", perspective, Hibernate.STRING);
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
				if (fundDetails.getAmpCurrencyId().getCurrencyCode().equals(
						"USD")) {
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
			logger.debug("Unable to get sum of funds from database", ex);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}

		return total;
	}

	public static Collection getDonors() {
		Session session = null;
		Query q = null;
		Collection donors = null;
		try {
			session = PersistenceManager.getSession();
			String queryString = new String();
			queryString = "select distinct f.ampDonorOrgId from "
					+ AmpFunding.class.getName() + " f";
			q = session.createQuery(queryString);
			logger.debug("No of Donors : " + q.list().size());
			donors = q.list();
		} catch (Exception ex) {
			logger.debug("Unable to get Donors from database", ex);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return donors;

	}

	public static Collection getFundingIdbyDonor(Long ampDonorOrgId) {
		Session session = null;
		Query q = null;
		Collection fundingIds = null;
		try {
			session = PersistenceManager.getSession();
			String queryString = new String();
			queryString = "select f from " + AmpFunding.class.getName()
					+ " f where (f.ampDonorOrgId=:ampDonorOrgId)";
			q = session.createQuery(queryString);
			q.setParameter("ampDonorOrgId", ampDonorOrgId, Hibernate.LONG);
			//logger.debug("No of funding Id for each donor : " +
			// q.list().size());
			fundingIds = q.list();
		} catch (Exception ex) {
			logger.debug("Unable to get Donors from database", ex);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		logger.debug("Returning fundingIDs : "
				+ (fundingIds != null ? fundingIds.size() : 0));
		return fundingIds;

	}

	public static Collection getProjectsbyModality(Long ampModalityId) {
		//logger.debug("Modality Id : " + ampModalityId);
		Session session = null;
		Query q = null;
		Collection projects = null;
		try {
			session = PersistenceManager.getSession();
			String queryString = new String();
			queryString = " select activity from "
					+ AmpActivity.class.getName()
					+ " activity where activity.modality.ampModalityId = :ampModalityId";
			q = session.createQuery(queryString);
			q.setParameter("ampModalityId", ampModalityId, Hibernate.LONG);
			//				logger.debug("No of projects for each Modality : " +
			// q.list().size());
			projects = q.list();
		} catch (Exception ex) {
			logger.debug("Unable to get Donors from database", ex);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
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
			session = PersistenceManager.getSession();
			String queryString = new String();
			queryString = "select distinct o.orgType from "
					+ AmpOrganisation.class.getName() + " o";
			q = session.createQuery(queryString);
			logger.debug("No of Donors Agencies : " + q.list().size());
			donorGroups = q.list();
		} catch (Exception ex) {
			logger.debug("Unable to get Donor Agencies from database", ex);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return donorGroups;

	}

	public static Collection getDonorbyAgency(String orgType) {
		logger.debug(" Donor Agency name passed is " + orgType);
		Session session = null;
		Query q = null;
		Collection donorGroups = null;
		try {
			session = PersistenceManager.getSession();
			String queryString = new String();
			queryString = "select o from " + AmpOrganisation.class.getName()
					+ " o where (o.orgType =:orgType)";
			q = session.createQuery(queryString);
			q.setParameter("orgType", orgType, Hibernate.STRING);
			//logger.debug("No of Org records : " + q.list().size());
			donorGroups = q.list();
		} catch (Exception ex) {
			logger
					.debug("Unable to get Organisation records from database",
							ex);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
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
			session = PersistenceManager.getSession();
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
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
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
			session = PersistenceManager.getSession();
			String queryString = new String();
			//queryString = "select f from " + AmpFunding.class.getName() + " f
			// , " + AmpTermsAssist.class.getName() + " t where (f.ampDonorOrgId
			// =:ampDonorOrgId) and (t.termsAssistName = termsAssistName ) ";
			queryString = "select f from "
					+ AmpFunding.class.getName()
					+ " f where (f.ampDonorOrgId =:ampDonorOrgId) and (f.ampTermsAssistId =:ampTermsAssistId)";
			q = session.createQuery(queryString);
			q.setParameter("ampDonorOrgId", ampDonorOrgId, Hibernate.LONG);
			q
					.setParameter("ampTermsAssistId", ampTermsAssistId,
							Hibernate.LONG);
			logger.debug("querystring " + queryString);
			funding = q.list();
		} catch (Exception ex) {
			logger.debug("Unable to get Funding records from database", ex);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
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
			session = PersistenceManager.getSession();
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
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		logger.debug("getFiscalYears() collection size returned : "
				+ (c != null ? c.size() : 0));
		return c;
	}

	public static List getAmpCurrency(Long ampActivityId) {
		List currency = null;
		Query q = null;
		Session session = null;
		String queryString = null;

		try {
			session = PersistenceManager.getSession();
			//queryString = " select Progress from " +
			// AmpPhysicalPerformance.class.getName() + " Progress where
			// (Progress.ampActivityId=:ampActivityId )";
			queryString = "select distinct ac.currencyCode from "
					+ AmpCurrency.class.getName()
					+ " ac, "
					+ AmpFundingDetail.class.getName()
					+ " afd, "
					+ AmpFunding.class.getName()
					+ " af where (afd.ampFundingId=af.ampFundingId) and (afd.ampCurrencyId=ac.ampCurrencyId) and (af.ampActivityId=:ampActivityId)";
			q = session.createQuery(queryString);
			q.setParameter("ampActivityId", ampActivityId, Hibernate.LONG);
			currency = q.list();
			Iterator iter = currency.iterator();
			while (iter.hasNext()) {
				iter.next();
				logger.debug("Size :" + currency.size());
				logger.debug("Currency 1:" + (String) currency.get(0));
				logger.debug("Currency 2:" + (String) currency.get(1));

			}
		} catch (Exception ex) {
			logger.error("Unable to get Amp PhysicalPerformance", ex);
			//System.out.println(ex.toString()) ;
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
			}
		}
		logger
				.debug("Getting funding Executed successfully "
						+ currency.size());
		return currency;

	}

	public static Collection getFundingId(Long ampActivityId, Long ampDonorOrgId) {
		Session session = null;
		Query q = null;
		Collection ampFundings = null;
		try {
			session = PersistenceManager.getSession();
			String queryString = new String();
			queryString = "select f from "
					+ AmpFunding.class.getName()
					+ " f where (f.ampActivityId=:ampActivityId ) and (f.ampDonorOrgId=:ampDonorOrgId)";
			q = session.createQuery(queryString);
			q.setParameter("ampActivityId", ampActivityId, Hibernate.LONG);
			q.setParameter("ampDonorOrgId", ampDonorOrgId, Hibernate.LONG);
			ampFundings = q.list();
			logger
					.debug("DbUtil : getFundingId() returning collection of size  "
							+ ampFundings.size());

		} catch (Exception ex) {
			logger.error("Unable to get AmpFunding collection from database",
					ex);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
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
			session = PersistenceManager.getSession();
			queryString = "select activity from " + AmpActivity.class.getName()
					+ " activity";
			q = session.createQuery(queryString);
			//q.setParameter("activity.getOrgrole().getOrganisation().getAmpOrgId",ampOrgId,Hibernate.LONG)
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
					}//if
				}//while
			}//while
		} catch (Exception ex) {
			logger.debug("Unable to get activty names  from database "
					+ ex.getMessage());
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.debug("releaseSession() failed ");
			}
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
			session = PersistenceManager.getSession();
			String queryString = new String();
			queryString = "select a from " + AmpTermsAssist.class.getName()
					+ " a where (a.ampTermsAssistId=:ampTermsAssistId) ";
			q = session.createQuery(queryString);
			q
					.setParameter("ampTermsAssistId", ampTermsAssistId,
							Hibernate.LONG);
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
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		if (logger.isDebugEnabled()) {
			if (ampTermsAssist != null)
				logger
						.debug("getAssistanceType() returning type of assistance :"
								+ ampTermsAssist.getTermsAssistName());
		}
		return ampTermsAssist;
	}

	public static Collection getAmpReportSector(Long ampActivityId) {
		Session session = null;
		ArrayList sectors = new ArrayList();

		try {
			session = PersistenceManager.getSession();
			String queryString = "select a from "
					+ AmpReportSector.class.getName() + " a "
					+ "where (a.ampActivityId=:ampActivityId)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("ampActivityId", ampActivityId, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) 
			{
				AmpReportSector act = (AmpReportSector) itr.next();
				if(sectors.indexOf(act.getSectorName())==-1)
					sectors.add(act.getSectorName());
			}

		} catch (Exception ex) {
			logger.error("Unable to get activity sectors" + ex);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}
		return sectors;
	}

	public static Collection getAmpReportSectorId(Long ampActivityId) {
		Session session = null;
		Collection sectors = new ArrayList();

		try {
			session = PersistenceManager.getSession();
			String queryString = "select a from "
					+ AmpReportSector.class.getName() + " a "
					+ "where (a.ampActivityId=:ampActivityId)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("ampActivityId", ampActivityId, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				AmpReportSector act = (AmpReportSector) itr.next();
				//	AmpSector
				// ampSector=DbUtil.getAmpParentSector(act.getAmpSectorId());
				sectors.add(act);
			}

		} catch (Exception ex) {
			logger.warn("Unable to get activity sectors" + ex);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}
		return sectors;
	}

	public static Collection getAmpReportSectors(String inClause) {
		Session session = null;
		ArrayList sectors = new ArrayList();
		ArrayList activityId = new ArrayList();

		try {
			logger.debug("Team Id:" + inClause);
			session = PersistenceManager.getSession();
			String queryString = "select report from "
					+ AmpReportCache.class.getName()
					+ " report where (report.ampTeamId in(" + inClause
					+ ")) group by report.ampActivityId";
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
			//			logger.debug("Query String: " + queryString);
			qry = session.createQuery(queryString);
			//			qry.setParameter("ampTeamId",ampTeamId,Hibernate.LONG);
			//			logger.debug("Size: " + qry.list().size());
			itr = qry.list().iterator();
			int flag = 0;
			while (itr.hasNext()) {
				AmpReportSector ampReportSector = (AmpReportSector) itr.next();
				if (ampProjectBySector == null) {
//					logger.debug("Start: ");
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
//					logger.debug("Id: " + ampReportSector.getAmpActivityId());
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
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}
		return sectors;
	}

	public static Collection getAmpReportLocation(Long ampActivityId) {
		Session session = null;
		Collection regions = new ArrayList();

		try {
			session = PersistenceManager.getSession();
			String queryString = "select a from "
					+ AmpReportLocation.class.getName() + " a "
					+ "where (a.ampActivityId=:ampActivityId)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("ampActivityId", ampActivityId, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				AmpReportLocation act = (AmpReportLocation) itr.next();
				if(act.getRegion()!=null)
					regions.add(act.getRegion());
			}

		} catch (Exception ex) {
			logger.error("Unable to get activity sectors" + ex);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}
		return regions;
	}

	public static AmpLevel getAmpLevel(Long id) {
		Session session = null;
		AmpLevel level = null;

		try {
			session = PersistenceManager.getSession();
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
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}
		return level;
	}

	// filterFlag, adjustmentFlag, CurrencyCode, calendarId, region,
	// modalityId,donorId(orgId)
	// statusId, sectorId
	public static String[] setFilterDetails(FilterProperties filter) {
		logger.debug("In setFilterDetails(FilterProperties filter) Function");
		Session session = null;
		String names = "";
		String name[] = new String[2];
		try {
			Query q = null;
			session = PersistenceManager.getSession();

			logger.debug("In setFilterDetails()");
			logger
					.info(filter.getAmpTeamId() + " : "
							+ filter.getCurrencyCode() + " :"
							+ filter.getPerspective());
			logger.debug(filter.getCalendarId() + ", " + filter.getRegionId()
					+ " : " + filter.getModalityId());
			logger.debug(filter.getOrgId() + ": " + filter.getStatusId() + ": "
					+ filter.getSectorId());
			String currQ = "", q2 = "", flag = "";
			String regionName = "Region(All) - ", currName = "Currency(All) - ", calName = "Calendar(All) - ", perspective = "";
			String modName = " Financing Instrument(All) -  ", statusName = "Status(All) - ", sectorName = "Sector(All) - ", orgName = "Donor(All) - ";
			String fromYear = "", toYear = "", startDate = "", closeDate = "";
			Iterator iter = null;
			AmpModality mod;
			AmpFiscalCalendar fisCal;
			AmpRegion region;
			AmpSector sector;
			AmpStatus status;
			AmpOrganisation org;
			AmpCurrency curr;
			if (filter.getCurrencyCode().length() == 1) {
				currName = "Currency(All) - ";
				logger.debug("Currency is 0");
			} else {
				currName = "Currency(" + filter.getCurrencyCode() + ") - ";
				logger.debug("Currency is NOt 0 : "
						+ filter.getCurrencyCode().length());
			}

			//			 Gets the Organisation name corresponding to the Modality Id
			currQ = "select report from " + AmpOrganisation.class.getName()
					+ " report where (report.ampOrgId=:orgId)";
			q = session.createQuery(currQ);
			q.setParameter("orgId", filter.getOrgId(), Hibernate.LONG);
			if (q != null) {
				iter = q.list().iterator();
				while (iter.hasNext()) {
					org = (AmpOrganisation) iter.next();
					logger.debug(" Organiation Name : " + org.getOrgCode());
					orgName = "Donor(" + org.getOrgCode() + ") - ";
					flag = "found";
					break;
				}
			}

			// Gets the Modality name corresponding to the Modality Id
			currQ = "select report from " + AmpModality.class.getName()
					+ " report where (report.ampModalityId=:modalityId)";
			q = session.createQuery(currQ);
			q
					.setParameter("modalityId", filter.getModalityId(),
							Hibernate.LONG);
			if (q != null) {
				iter = q.list().iterator();
				while (iter.hasNext()) {
					mod = (AmpModality) iter.next();
					logger.debug(" Modality Name : " + mod.getName());
					modName = " Financing Instrument(" + mod.getName() + ") - ";
					flag = "found";
					break;
				}
			}
			//			 Gets the Status Name corresponding to the Region Id
			currQ = "select report from " + AmpStatus.class.getName()
					+ " report where (report.ampStatusId=:statusId)";
			q = session.createQuery(currQ);
			q.setParameter("statusId", filter.getStatusId(), Hibernate.LONG);
			if (q != null) {
				iter = q.list().iterator();
				while (iter.hasNext()) {
					status = (AmpStatus) iter.next();
					logger.debug(" Status Name : " + status.getName());
					statusName = "Status(" + status.getName() + ") - ";
					flag = "found";
					break;
				}
			}
			//			 Gets the Sector Name corresponding to the Region Id
			currQ = "select report from " + AmpSector.class.getName()
					+ " report where (report.ampSectorId=:sectorId)";
			q = session.createQuery(currQ);
			q.setParameter("sectorId", filter.getSectorId(), Hibernate.LONG);
			if (q != null) {
				iter = q.list().iterator();
				while (iter.hasNext()) {
					sector = (AmpSector) iter.next();
					logger.debug(" Sector Name : " + sector.getName());
					sectorName = "Sector(" + sector.getName() + ") - ";
					flag = "found";
					break;
				}
			}

			//			 Gets the Calendar Name corresponding to the Calendar Id
			currQ = "select report from " + AmpFiscalCalendar.class.getName()
					+ " report where (report.ampFiscalCalId=:calendarId)";
			q = session.createQuery(currQ);
			q.setParameter("calendarId", filter.getCalendarId(),
					Hibernate.INTEGER);
			if (q != null) {
				iter = q.list().iterator();
				while (iter.hasNext()) {
					fisCal = (AmpFiscalCalendar) iter.next();
					logger.debug(" Calendar Name : " + fisCal.getName());
					calName = "Calendar(" + fisCal.getName() + ") - ";
					flag = "found";
					break;
				}
			}

			regionName = "Region(" + filter.getRegionId() + ")";
			perspective = "Perspective(" + filter.getPerspective() + ") - ";
			fromYear = "FromYear(" + filter.getFromYear() + ") - ";
			toYear = "ToYear(" + filter.getToYear() + ") - ";
			if (filter.getStartDate() == null)
				startDate = "StartDate(Not Selected) - ";
			else
				startDate = "StartDate(" + filter.getStartDate() + ") - ";
			if (filter.getCloseDate() == null)
				closeDate = "CloseDate(Not Selected)";
			else
				closeDate = "CloseDate(" + filter.getCloseDate() + ") - ";

			name[0] = perspective + currName + calName + fromYear + toYear
					+ orgName + regionName;
			name[1] = modName + statusName + sectorName + startDate + closeDate;
			//names = perspective + currName + calName + fromYear + toYear +
			// orgName + regionName + modName + statusName + sectorName
			// +startDate + closeDate;

		} catch (Exception e) {
			logger.debug("Exception in filterDetails : " + e);
			e.printStackTrace(System.out);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.debug("releaseSession() failed ");
			}
		}
		logger.debug("Before Return " + name);
		logger.debug("End of setFilterDetails()");
		return (name);
	}// End of SetFilterDetails Function

	public static Collection getAmpLevel0Teams(Long ampTeamId) {
		Session session = null;
		Collection teams = new ArrayList();

		try {
			logger.debug("Team Id:" + ampTeamId);
			session = PersistenceManager.getSession();
			String queryString = "select t from " + AmpTeam.class.getName()
					+ " t " + "where (t.parentTeamId.ampTeamId=:ampTeamId)";
			logger.debug("Query String:" + queryString);
			Query qry = session.createQuery(queryString);
			qry.setParameter("ampTeamId", ampTeamId, Hibernate.LONG);
			Iterator itrTemp = qry.list().iterator();
			AmpTeam ampTeam = null;
			LinkedList list = new LinkedList();
			list.addAll(qry.list());
			while (list.size() > 0) {
				ampTeam = (AmpTeam) list.removeFirst();
				if (ampTeam.getAccessType().equals("Team"))
					teams.add(ampTeam.getAmpTeamId());
				else {
					queryString = "select t from " + AmpTeam.class.getName()
							+ " t " + "where (t.parentTeamId.ampTeamId="
							+ ampTeam.getAmpTeamId() + ")";
					qry = session.createQuery(queryString);
					list.addAll(qry.list());
				}

				//			  ampTeam = (AmpTeam) itrTemp.next();
				//			  teams.add(ampTeam.getAmpTeamId());
			}
			logger.debug("Size: " + teams.size());

		} catch (Exception e) {
			logger.debug("Exception from getAmpLevel0Team()" + e.getMessage());
			logger.debug(e.toString());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return teams;
	}

	public static Collection getAllLevels() {
		Session session = null;
		Collection col = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select c from " + AmpLevel.class.getName()
					+ " c";
			Query qry = session.createQuery(queryString);
			col = qry.list();
		} catch (Exception e) {
			logger.debug("Exception from getAllLevels()");
			logger.debug(e.toString());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return col;
	}

	public static Collection getAllOrgGroups() {
		Session session = null;
		Collection col = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select c from " + AmpOrgGroup.class.getName()
					+ " c";
			Query qry = session.createQuery(queryString);
			col = qry.list();
		} catch (Exception e) {
			logger.debug("Exception from getAllOrgGroups()");
			logger.debug(e.toString());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return col;
	}

	public static Collection getAllOrgTypes() {
		Session session = null;
		Collection col = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select c from " + AmpOrgType.class.getName()
					+ " c";
			Query qry = session.createQuery(queryString);
			col = qry.list();
		} catch (Exception e) {
			logger.debug("Exception from getAllOrgTypes()");
			logger.debug(e.toString());
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return col;
	}

	public static AmpOrgType getAmpOrgType(Long ampOrgTypeId) {
		Session session = null;
		Query qry = null;
		AmpOrgType ampOrgType = null;

		try {
			session = PersistenceManager.getSession();
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
			logger.debug("Exceptiion " + e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return ampOrgType;
	}

	public static AmpOrgGroup getAmpOrgGroup(Long id) {
		Session session = null;
		AmpOrgGroup grp = null;

		try {
			session = PersistenceManager.getSession();
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
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}
		return grp;
	}

	public static Collection getAllOrgGroupByType(Long id) {
		Session session = null;
		Collection col = null;

		try {
			session = PersistenceManager.getSession();
			String queryString;
			Query qry;
			String q1 = "select l from " + AmpOrgGroup.class.getName();
			String q2 = null;
			if (id != null) {
				q2 = " l  where (l.levelId.ampLevelId=:id)";
				queryString = q1 + q2;
				qry = session.createQuery(queryString);
				qry.setParameter("id", id, Hibernate.LONG);
			} else {
				q2 = " l  where (l.levelId is null)";
				queryString = q1 + q2;
				qry = session.createQuery(queryString);
			}
			col = qry.list();

		} catch (Exception ex) {
			logger.error("Unable to get Org Group" + ex);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}
		return col;
	}

	public static Collection getAllOrganisationGroup() {
		Session session = null;
		Query qry = null;
		Collection organisation = new ArrayList();

		try {
			session = PersistenceManager.getSession();
			String queryString = "select o from " + AmpOrgGroup.class.getName()
					+ " o order by org_grp_name asc";
			qry = session.createQuery(queryString);
			organisation = qry.list();
		} catch (Exception e) {
			logger.error("Unable to get all organisation groups");
			logger.debug("Exceptiion " + e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return organisation;
	}

	public static Collection getOrgByGroup(Long Id) {

		Session sess = null;
		Collection col = new ArrayList();
		Query qry = null;

		try {
			sess = PersistenceManager.getSession();
			String queryString = "select o from "
					+ AmpOrganisation.class.getName()
					+ " o where (o.orgGrpId=:orgGrpId)";
			qry = sess.createQuery(queryString);
			qry.setParameter("orgGrpId", Id, Hibernate.LONG);
			col = qry.list();
		} catch (Exception e) {
			logger.debug("Exception from getOrgByGroup()");
			logger.debug(e.toString());
		} finally {
			try {
				if (sess != null) {
					PersistenceManager.releaseSession(sess);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return col;
	}

	public static Collection getOrgByCode(String action, String code, Long id) {

		Session sess = null;
		Collection col = new ArrayList();
		Query qry = null;
		String queryString;

		try {
			sess = PersistenceManager.getSession();
			if ("create".equals(action)) {
				queryString = "select o from "
						+ AmpOrganisation.class.getName()
						+ " o where (o.orgCode=:code)";
				qry = sess.createQuery(queryString);
				qry.setParameter("code", code, Hibernate.STRING);
			} else if ("edit".equals(action)) {
				queryString = "select o from "
						+ AmpOrganisation.class.getName()
						+ " o where (o.orgCode=:code) and (o.ampOrgId!=:id)";
				qry = sess.createQuery(queryString);
				qry.setParameter("code", code, Hibernate.STRING);
				qry.setParameter("id", id, Hibernate.LONG);
			}
			col = qry.list();
		} catch (Exception e) {
			logger.debug("Exception from getOrgByCode()");
			logger.debug(e.toString());
		} finally {
			try {
				if (sess != null) {
					PersistenceManager.releaseSession(sess);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return col;
	}

	public static Collection getOrganisationAsCollection(Long id) {
		Session session = null;
		Collection org = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select o from "
					+ AmpOrganisation.class.getName() + " o "
					+ "where (o.ampOrgId=:id)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);
			org = qry.list();
		} catch (Exception ex) {
			logger.error("Unable to get organisation from database", ex);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		logger.debug("Getting organisation successfully ");
		return org;
	}

	public static AmpField getAmpFieldByName(String com) {
		Session session = null;
		Query qry = null;
		AmpField comments = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select o from " + AmpField.class.getName()
					+ " o " + "where (o.fieldName=:com)";
			qry = session.createQuery(queryString);
			qry.setParameter("com", com, Hibernate.STRING);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				comments = (AmpField) itr.next();
			}
		} catch (Exception e) {
			logger.error("Unable to get all comments");
			logger.debug("Exceptiion " + e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return comments;
	}

	public static ArrayList getAllCommentsByField(Long fid, Long aid) {
		Session session = null;
		Query qry = null;
		ArrayList comments = new ArrayList();

		try {
			session = PersistenceManager.getSession();
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
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return comments;
	}

	public static AmpComments getAmpComment(Long id) {
		Session session = null;
		AmpComments comment = null;

		try {
			session = PersistenceManager.getSession();
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
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}
		return comment;
	}

	public static String[] getMemberInformation(Long userId) {
		logger.debug("In getMemberInformation() : " + userId);
		Session session = null;
		String query = " ";
		Iterator iter = null;
		AmpTeamMember tm = null;
		Vector vect = new Vector();
		String[] info = null;

		Collection memCollInfo = new ArrayList();
		try {
			Query q = null;
			session = PersistenceManager.getSession();
			query = "select member from " + AmpTeamMember.class.getName() + " "
					+ "member where (member.user=:memberId)";
			q = session.createQuery(query);
			q.setParameter("memberId", userId, Hibernate.LONG);
			if (q != null) {
				iter = q.list().iterator();
				while (iter.hasNext()) {
					tm = (AmpTeamMember) iter.next();
					vect.add(tm.getAmpTeam().getName());
					vect.add(tm.getAmpMemberRole().getRole());
				}
			}
			info = new String[vect.size()];
			vect.toArray(info);
		} catch (Exception e) {
			logger.debug("Exception in getTeamMemberInformation() : " + e);
			e.printStackTrace(System.out);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger
						.info("releaseSession() failed for getMemberInformation()");
			}
		}

		return info;
	}

	public static ArrayList getAmpPerspective() {
		Session session = null;
		Query q = null;
		AmpPerspective ampPerspective = null;
		ArrayList perspective = new ArrayList();
		String queryString = null;
		Iterator iter = null;
		try {
			session = PersistenceManager.getSession();
			queryString = " select p from " + AmpPerspective.class.getName()
					+ " p order by p.name";
			q = session.createQuery(queryString);
			iter = q.list().iterator();
			while (iter.hasNext()) {

				ampPerspective = (AmpPerspective) iter.next();
				perspective.add(ampPerspective);
			}

		} catch (Exception ex) {
			logger.error("Unable to get Amp status   from database "
					+ ex.getMessage());
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}
		return perspective;
	}

	public static int getAmpMaxToYear(Long ampTeamId) {
		Session session = null;
		Query q = null;
		Integer year=null;
		String queryString = null;
		try {
			session = PersistenceManager.getSession();
			queryString = " select extract(YEAR from max(afd.transactionDate)) from " + AmpFundingDetail.class.getName()
					+ " afd," +  AmpFunding.class.getName() + " af," + AmpActivity.class.getName() + " aa where (afd.ampFundingId=af.ampFundingId) and af.ampActivityId=aa.ampActivityId and aa.team.ampTeamId='" + ampTeamId + "'";
			q = session.createQuery(queryString);
			year =(Integer) q.list().get(0);

		} catch (Exception ex) {
			logger.error("Unable to get Amp status   from database "
					+ ex.getMessage());
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}
		return year.intValue();
	}


	public static ArrayList getAmpReportPhysicalPerformance(Long ampActivityId) {
		Session session = null;
		ArrayList progress = new ArrayList();

		try {
			session = PersistenceManager.getSession();
			String queryString = "select a from "
					+ AmpReportPhysicalPerformance.class.getName() + " a "
					+ "where (a.ampActivityId=:ampActivityId)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("ampActivityId", ampActivityId, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				AmpReportPhysicalPerformance act = (AmpReportPhysicalPerformance) itr.next();
				progress.add(act.getTitle() + " : " + act.getDescription());
			}

		} catch (Exception ex) {
			logger.error("Unable to get activity sectors" + ex);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}
		return progress;
	}

	
	public static Group getGroup(String key,Long siteId) {
		Session session = null;
		Group group = null;
		try {
			session = PersistenceManager.getSession();
			String qryStr = "select grp from " + Group.class.getName() + " grp " +
					"where (grp.key=:key) and (grp.site=:sid)";
			Query qry = session.createQuery(qryStr);
			qry.setParameter("key",key,Hibernate.STRING);
			qry.setParameter("sid",siteId,Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) { 
				group = (Group) itr.next();		
			}
		} catch (Exception ex) {
			logger.error("Unable to get Group "
					+ ex.getMessage());
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}
		return group;	
	}

	public static AmpPerspective getPerspective(String code) {
		Session session = null;
		AmpPerspective persp = null;
		logger.debug("In getPerspective()" + code);
		try {
			session = PersistenceManager.getSession();
			String qryStr = "select p from " + AmpPerspective.class.getName() + " p " +
					"where (p.code=:code)";
			Query qry = session.createQuery(qryStr);
			qry.setParameter("code",code,Hibernate.STRING);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) { 
				persp = (AmpPerspective) itr.next();
				logger.debug("Got the perspective " + persp.getName());
			}
		} catch (Exception ex) {
			logger.error("Unable to get AmpPerspective "
					+ ex.getMessage());
			ex.printStackTrace(System.out);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}
		return persp;			
	}

	public static ArrayList getApprovedActivities(String inClause){
		ArrayList actList = new ArrayList();
		Session session = null;
		Query q = null;
		String queryString;
		try {
			
			session = PersistenceManager.getSession();
			
			queryString = "select act.ampActivityId from " + AmpActivity.class.getName()
				  + " act where (act.team.ampTeamId in(" + inClause + ")) and (act.approvalStatus=:status)";
			q = session.createQuery(queryString);
			q.setParameter("status", "approved", Hibernate.STRING);
			actList=(ArrayList)q.list();
			logger.debug("Approved Activity List Size: " + actList.size());

		} catch (Exception ex) {
			logger.error("Unable to get AmpActivity [getApprovedActivities()]", ex);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
			}
		}
		logger.debug("Getting Approved activities Executed successfully ");
		return actList;
	}


}

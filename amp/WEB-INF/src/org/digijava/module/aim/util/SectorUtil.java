package org.digijava.module.aim.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.algo.DatabaseWaver;
import org.digijava.kernel.exception.DgException;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityGroup;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpIndicatorSector;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpSectorScheme;
import org.digijava.module.aim.dbentity.AmpThemeIndicators;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.Sector;
import org.digijava.module.aim.util.caching.AmpCaching;
import org.digijava.module.aim.util.time.StopWatch;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Utility class for persisting all Sector with Scheme related entities
 * 
 * @author Govind G Dalwani
 */

public class SectorUtil {

	private static Logger logger = Logger.getLogger(SectorUtil.class);

	public static Collection searchForSector(String keyword, Long ampSecSchemeId) {
		Session session = null;
		Collection col = new ArrayList();

		logger.info("INSIDE Search Sector DBUTIL..... ");

		try {

			session = PersistenceManager.getSession();
			int tempIncr = 0;
			logger.info("searching SECTORS...............");
			/*
			 * String qryStr =
			 * "select country.countryId,country.countryName,region.ampRegionId,region.name "
			 * + "from " + Country.class.getName() + " country , " +
			 * AmpRegion.class.getName() + " " +
			 * "region where region.name like '%" + keyword + "%' and " +
			 * "country.iso = region.country";
			 */
			String sectorNameHql = AmpSector.hqlStringForName("sector");
			String qryStr = "select sector from " + AmpSector.class.getName()
					+ " sector inner join sector.ampSecSchemeId " + " sscheme"
					+ " where lower(" + sectorNameHql + ") like '%"
					+ keyword.toLowerCase() + "%' and (sector.deleted is null or sector.deleted = false) ";
			if (ampSecSchemeId != null) {
				qryStr += " and sscheme.ampSecSchemeId=:ampSecSchemeId";
			}

			Query qry = session.createQuery(qryStr);
			// qry.setParameter("orgType", orgType, Hibernate.LONG) ;
			if (ampSecSchemeId != null) {
				qry.setLong("ampSecSchemeId", ampSecSchemeId);
			}
			// col =qry.list();
			Iterator itr = qry.list().iterator();

			ActivitySector sectr;
			tempIncr = 0;

			while (itr.hasNext()) {
				AmpSector as = (AmpSector) itr.next();
				sectr = new ActivitySector();
				sectr.setSectorScheme(as.getAmpSecSchemeId().getSecSchemeName());
				if (as.getParentSectorId() != null) {

					sectr.setSectorName(as.getParentSectorId().getName());
					sectr.setSectorId(as.getParentSectorId().getAmpSectorId());
					sectr.setSubsectorLevel1Id(as.getAmpSectorId());
					sectr.setSubsectorLevel1Name(as.getName());

					if (as.getParentSectorId().getParentSectorId() != null) {

						sectr.setSectorName(as.getParentSectorId()
								.getParentSectorId().getName());
						sectr.setSectorId(as.getParentSectorId()
								.getAmpSectorId());
						sectr.setSubsectorLevel1Id(as.getParentSectorId()
								.getAmpSectorId());
						sectr.setSubsectorLevel1Name(as.getParentSectorId()
								.getName());
						sectr.setSubsectorLevel2Id(as.getAmpSectorId());
						sectr.setSubsectorLevel2Name(as.getName());

					}
				} else {

					sectr.setSectorName(as.getName());
					sectr.setSectorId(as.getAmpSectorId());

				}
				col.add(sectr);
			}

		} catch (Exception ex) {
			logger.debug("Unable to search sectors" + ex);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.debug("releaseSession() failed", ex2);
			}
		}
		return col;
	}// End Search Sector.

	/**
	 * gets all the sectors attached to an activity
	 * @param id
	 * @return
	 */
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
				// ampSector = (AmpSector) iter.next();
				ampSector = ((AmpActivitySector) iter.next()).getSectorId();
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

//	public static Collection getSectorActivities(Long sectorId) {
//
//		Session sess = null;
//		Collection col = null;
//
//		try {
//			sess = PersistenceManager.getSession();
//			AmpSector sector = (AmpSector) sess.load(AmpSector.class, sectorId);
//
//			Iterator itr = sector.getAidlist().iterator();
//			col = new ArrayList();
//			while (itr.hasNext()) {
//				col.add(itr.next());
//			}
//		} catch (Exception e) {
//			logger.debug("Exception from getSectorActivities()");
//			logger.debug(e.toString());
//		} finally {
//			try {
//				if (sess != null) {
//					PersistenceManager.releaseSession(sess);
//				}
//			} catch (Exception ex) {
//				logger.debug("releaseSession() failed");
//				logger.debug(ex.toString());
//			}
//		}
//		return col;
//
//	}

	public static List<AmpSectorScheme> getAllSectorSchemes() {
		try {
			String sectorSchemeNameHql = AmpSectorScheme.hqlStringForName("ss");
			String queryString = "select ss from "
					+ AmpSectorScheme.class.getName() + " ss "
					+ "order by " + sectorSchemeNameHql;
			Query qry = PersistenceManager.getSession().createQuery(queryString);
			List<AmpSectorScheme> col = new ArrayList<>(qry.list());
			return col;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} 
	}

	@SuppressWarnings("unchecked")
	public static List<AmpSector> getAllParentSectors(Long secSchemeId){
		try
		{ 
			String queryString = "select s from " + AmpSector.class.getName()
					+ " s " + "where amp_sec_scheme_id = " + secSchemeId
					+ " and parent_sector_id is null and (s.deleted is null or s.deleted = false)  " + "order by " + AmpSector.hqlStringForName("s");
			List<AmpSector> sectors = new ArrayList<>(PersistenceManager.getSession().createQuery(queryString).list());
			return sectors;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}	
	
	public static List<AmpSector> getAllParentSectors() {
		Session session = null;
		List<AmpSector> col = null;

		try {
			session = PersistenceManager.getSession();

			String queryString = "select cls from "
					+ AmpClassificationConfiguration.class.getName()
					+ " cls where cls.primary=true ";
			Query qry = session.createQuery(queryString);
			AmpClassificationConfiguration auxConfig = (AmpClassificationConfiguration) qry
					.uniqueResult();

			queryString = "select s from "
					+ AmpSector.class.getName()
					+ " s "
					+ " where parent_sector_id is null and amp_sec_scheme_id = "
					+ auxConfig.getClassification().getAmpSecSchemeId()
					+ " and (s.deleted is null or s.deleted = false)  order by " + AmpSector.hqlStringForName("s");
			qry = session.createQuery(queryString);
			col = qry.list();

		} catch (Exception e) {
			logger.error("Cannot get parent sectors, " + e);
		}
		return col;
	}

	public static List<AmpSector> getAllSectorsFromScheme(Long secSchemeId) {
		Session session = null;
		List<AmpSector> col = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select s from " + AmpSector.class.getName()
					+ " s " + "where amp_sec_scheme_id = " + secSchemeId + " and (s.deleted is null or s.deleted = false) ";
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

	public static Collection<AmpSector> getAllSectors() {
		if (AmpCaching.getInstance().sectorsCache == null)
		{
			Session session = null;			

			try {
				session = PersistenceManager.getRequestDBSession();
				String queryString = "select sc from " + AmpSector.class.getName()
						+ " sc where (sc.deleted is null or sc.deleted = false) order by " + AmpSector.hqlStringForName("sc");
				Query qry = session.createQuery(queryString);
				AmpCaching.getInstance().initSectorsCache(qry.list());
			} catch (Exception e) {
				throw new RuntimeException("Cannot get sectors, ", e);
			}
		}
		return AmpCaching.getInstance().sectorsCache.getAllSectors();
	}
	
	public static Collection<AmpSector> getAllChildSectors(Long parSecId) {
		if (AmpCaching.getInstance().sectorsCache == null)
			getAllSectors(); // force initialization of cache
		
		return AmpCaching.getInstance().sectorsCache.getChildSectors(parSecId);
	}

	public static void updateSectorOrganisation(Long sectorId,
			AmpOrganisation organisation) {
		AmpSector sector = getAmpSector(sectorId);
		sector.setAmpOrgId(organisation);
		DbUtil.update(sector);
		AmpCaching.getInstance().sectorsCache = null; //invalidate
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
						+ " s where s.parentSectorId is null and (s.deleted is null or s.deleted = false) order by " + AmpSector.hqlStringForName("s");

				qry = session.createQuery(queryString);
			} else {
				queryString = "select s from " + AmpSector.class.getName()
						+ " s where (s.parentSectorId=:parentSectorId) "
						+ " and (s.deleted is null or s.deleted = false) order by " + AmpSector.hqlStringForName("s");

				qry = session.createQuery(queryString);
				qry.setParameter("parentSectorId", parentSecId, Hibernate.LONG);
			}
			itr = qry.list().iterator();

			while (itr.hasNext()) {
				ampSector = (AmpSector) itr.next();

				Sector sec = new Sector();
				if (ampSector.getAmpOrgId() != null) {
					sec.setOrgId(ampSector.getAmpOrgId().getAmpOrgId());
					sec.setOrgName(ampSector.getAmpOrgId().getName());
				}
				sec.setSectorId(ampSector.getAmpSectorId());
				sec.setSectorName(ampSector.getName());
				col.add(sec);
			}

		} catch (Exception ex) {
			logger.error("Unable to get subsectors");
			ex.printStackTrace(System.out);
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
					+ " s where (s.ampSectorId=:ampSectorId) and (s.deleted is null or s.deleted = false) ";

			qry = session.createQuery(queryString);
			qry.setParameter("ampSectorId", sectorId, Hibernate.LONG);
			itr = qry.list().iterator();

			if (itr.hasNext()) {
				ampSector = (AmpSector) itr.next();
				sec = new Sector(ampSector.getAmpSectorId(),
						ampSector.getName(), ampSector.getAmpOrgId()
								.getAmpOrgId(), DbUtil.getOrganisation(
								ampSector.getAmpOrgId().getAmpOrgId())
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

//	public static AmpIndicatorSector getIndIcatorSector(Long indicatorId) {
//
//		Session session = null;
//		Query qry = null;
//		AmpIndicatorSector indSectorId = null;
//		Iterator itr = null;
//
//		try {
//			session = PersistenceManager.getRequestDBSession();
//			String queryString = new String();
//			queryString = "select s from " + AmpIndicatorSector.class.getName()
//					+ " s where (s.themeIndicatorId=:themeIndicatorId)";
//
//			qry = session.createQuery(queryString);
//			qry.setParameter("themeIndicatorId", indicatorId, Hibernate.LONG);
//			itr = qry.list().iterator();
//
//			if (itr.hasNext()) {
//				indSectorId = (AmpIndicatorSector) itr.next();
//			}
//
//		} catch (Exception e) {
//			logger.error("Unable to get sector");
//			logger.debug("Exceptiion " + e);
//		}
//
//		return indSectorId;
//	}

	public static boolean getIndIcatorSector(Long indicatorId, Long sectorId) {

		Session session = null;
		Query qry = null;
		List<AmpIndicatorSector> indSectors = null;
		boolean exist = false;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = new String();
			queryString = "select s from "
					+ AmpIndicatorSector.class.getName()
					+ " s where (s.themeIndicatorId=:themeIndicatorId) and s.sectorId=:sectorId";

			qry = session.createQuery(queryString);
			qry.setParameter("themeIndicatorId", indicatorId, Hibernate.LONG);
			qry.setLong("sectorId", sectorId);
			indSectors = qry.list();
			if (indSectors != null && indSectors.size() > 0) {
				exist = true;
			}

		} catch (Exception e) {
			logger.error("Unable to get sector");
			logger.debug("Exceptiion " + e);
		}

		return exist;
	}

	public static AmpSector getAmpSector(Long id)
	{
		try
		{
			AmpSector s = (AmpSector) PersistenceManager.getRequestDBSession().load(AmpSector.class, id);
			//if ((s.getDeleted()) != null && (s.getDeleted()))
				return s;
			//return null; // deleted
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
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
				&& organisationChanged(sector.getAmpSectorId(),
						sector.getAmpOrgId()) == true) {

			updateSubSectors(sector, sector.getAmpOrgId());
		}
		DbUtil.update(sector);
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
					+ " Sector where Sector.parentSectorId is null and (Sector.deleted is null or Sector.deleted = false) order by " + AmpSector.hqlStringForName("Sector");
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
					+ " Sector where Sector.parentSectorId is not null and (Sector.deleted is null or Sector.deleted = false) order by " + AmpSector.hqlStringForName("Sector");
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
					+ " Sector where Sector.parentSectorId is not null and Sector.parentSectorId.ampSectorId=:ampSectorId and (Sector.deleted is null or Sector.deleted = false) ";
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
					+ " Sector where Sector.ampSectorId=:ampSectorId and (Sector.deleted is null or Sector.deleted = false) ";
			q = session.createQuery(queryString);
			q.setParameter("ampSectorId", ampSectorId, Hibernate.LONG);
			iter = q.list().iterator();

			ampSector = (AmpSector) iter.next();
			while (ampSector.getParentSectorId() != null)
				ampSector = ampSector.getParentSectorId();
			// ampSectorId=ampSector.getAmpSectorId();
			// logger.debug("Sector Id: " + ampSectorId);

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

	public static Set<AmpSector> getAmpParentSectors(
			Collection<AmpSector> sectors) {
		if (sectors != null) {
			Set<AmpSector> retList = new HashSet<AmpSector>();
			for (AmpSector sector : sectors) {
				retList.addAll(SectorUtil.getAmpParentSectors(sector
						.getAmpSectorId()));
			}
			return retList;
		}
		return null;
	}

	public static Collection<AmpSector> getAmpParentSectors(Long ampSectorId) {
		Session session = null;
		Query q = null;
		Collection<AmpSector> ret = new ArrayList<AmpSector>();
		String queryString = null;
		Iterator iter = null;

		try {
			session = PersistenceManager.getSession();

			AmpSector ampSector = (AmpSector) session.load(AmpSector.class,
					ampSectorId);
			while (ampSector.getParentSectorId() != null) {
				ampSector = ampSector.getParentSectorId();
				ret.add(ampSector);
			}

		} catch (Exception ex) {
			logger.error("Unable to get Amp sub sectors from database "
					+ ex.getMessage());
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}
		return ret;
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
			// queryString = " select Sector from " + AmpSector.class.getName()
			// + " Sector where Sector.parentSectorId is null order by
			// Sector.name";

			queryString = "select sector from "
					+ AmpSector.class.getName()
					+ " sector, "
					+ AmpActivity.class.getName()
					+ " act where (sector.ampSecSchemeId = :ampSecSchemeId) and (act.ampActivityId = :ampActivityId) and sector.parentSectorId is null and (sector.deleted is null or sector.deleted = false) ";
			// queryString = "select sector from " + AmpSector.class.getName() +
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

	public static Collection searchSectorCode(String key) {
		Session sess = null;
		Collection col = null;
		Query qry = null;

		try {
			sess = PersistenceManager.getSession();
			String queryString = "select s from " + AmpSector.class.getName()
					+ " s where s.sectorCode like '%" + key + "%' and (s.deleted is null or s.deleted = false) ";
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
			String sectorNameHql = AmpSector.hqlStringForName("s");
			String queryString = "select s from " + AmpSector.class.getName()
					+ " s where " + sectorNameHql + " like '%" + key + "%' and (s.deleted is null or s.deleted = false) ";
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

	public static boolean organisationChanged(Long sectorId,
			AmpOrganisation organisation) {
		logger.debug("in organisationChanged()");
		Session sess = null;
		boolean flag = false;

		try {
			sess = PersistenceManager.getSession();
			String qryString = "select s from " + AmpSector.class.getName()
					+ " s where (s.ampSectorId=:ampSectorId) and (s.deleted is null or s.deleted = false) ";

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

	/**
	 * 
	 * 
	 * @return List of sectors and sub-sectors ordered by sectors alphabetically
	 *         and then by sub-sectors alphabetically (Ex. A, a1, a2, a3, B, b1,
	 *         b2, etc...). The names of the sectors are a embelished (upper
	 *         case, or added some spaces) for better presentation. DO NOT save
	 *         this objects back to the database !!!!
	 */
	public static List<AmpSector> getAmpSectorsAndSubSectors(
			String configurationName) {
		List<AmpSector> ret = new ArrayList<AmpSector>();
		Long id = null;
			Collection<AmpClassificationConfiguration> configs = SectorUtil
					.getAllClassificationConfigs();
			Iterator<AmpClassificationConfiguration> confIter = configs
					.iterator();
			while (confIter.hasNext()) {
				AmpClassificationConfiguration conf = confIter.next();
				if (configurationName.equals(conf.getName())) {
					if (conf.getClassification() != null)
						id = conf.getClassification().getAmpSecSchemeId();
				}
			}
			if (id != null) {
				Collection<AmpSector> dbReturnSet = SectorUtil
						.getAllParentSectors(id);
				Iterator<AmpSector> iter = dbReturnSet.iterator();
				while (iter.hasNext()) {
					AmpSector ampSector = iter.next();
					ampSector.setName(ampSector.getName().toUpperCase());
					ret.add(ampSector);
					Collection<AmpSector> dbChildReturnSet = SectorUtil
							.getAllChildSectors(ampSector.getAmpSectorId());

					Iterator<AmpSector> iterSub = dbChildReturnSet.iterator();
					while (iterSub.hasNext()) {
						AmpSector ampSubSector = (AmpSector) iterSub.next();
						String temp = " -- " + ampSubSector.getName();
						ampSubSector.setName(temp);
						ret.add(ampSubSector);

						Collection<AmpSector> dbChildReturnSet2 = SectorUtil
								.getAllChildSectors(ampSubSector
										.getAmpSectorId());
						if (dbChildReturnSet2 != null
								&& dbChildReturnSet2.size() > 0) {
							for (AmpSector ampSubSubSector : dbChildReturnSet2) {
								ampSubSubSector.setName(" -- -- "
										+ ampSubSubSector.getName());
								ret.add(ampSubSubSector);
							}
						}
					}
				}
			}

		return ret;
	}

	/**
	 * TODO: this is poor man's recursion
	 * @param configurationName
	 * @return
	 */
	public static List<AmpSector> getAmpSectorsAndSubSectorsHierarchy(
			String configurationName) {
		if (AmpCaching.getInstance().sectorsCache == null)
			getAllSectors(); //force rebuilding cache
		if (AmpCaching.getInstance().sectorsCache.sectorsHierarchy.containsKey(configurationName))
			return new ArrayList<AmpSector>(AmpCaching.getInstance().sectorsCache.sectorsHierarchy.get(configurationName));
		
		List<AmpSector> ret = new ArrayList<AmpSector>();
		Long id = null;

			Collection<AmpClassificationConfiguration> configs = SectorUtil
					.getAllClassificationConfigs();
			Iterator<AmpClassificationConfiguration> confIter = configs
					.iterator();
			while (confIter.hasNext()) {
				AmpClassificationConfiguration conf = confIter.next();
				if (configurationName.equals(conf.getName())) {
					if (conf.getClassification() != null)
						id = conf.getClassification().getAmpSecSchemeId();
				}
			}
			if (id != null) {
				Collection<AmpSector> dbReturnSet = SectorUtil
						.getAllParentSectors(id);
				Iterator<AmpSector> iter = dbReturnSet.iterator();
				while (iter.hasNext()) {
					AmpSector ampSector = iter.next();
					ampSector.setName(ampSector.getName().toUpperCase());
					ret.add(ampSector);
					Collection<AmpSector> dbChildReturnSet = SectorUtil
							.getAllChildSectors(ampSector.getAmpSectorId());
					ampSector.getChildren().addAll(dbChildReturnSet);
					if (ampSector.getChildren() != null) {
						Iterator<AmpSector> iter2 = ampSector.getChildren()
								.iterator();
						while (iter2.hasNext()) {
							AmpSector ampSubSector = iter2.next();
							Collection<AmpSector> dbChildReturnSet2 = SectorUtil
									.getAllChildSectors(ampSubSector
											.getAmpSectorId());
							if (dbChildReturnSet2 != null)
								ampSubSector.getChildren().addAll(
										dbChildReturnSet2);
						}
					}
				}
			}
	
		AmpCaching.getInstance().sectorsCache.sectorsHierarchy.put(configurationName, ret);
		return new ArrayList<AmpSector>(ret);
	}

	
	/**
	 * TODO: this is poor man's recursion
	 * it can certainly be rewritten
	 * @param configurationName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Collection<SectorSkeleton> getAmpSectorsAndSubSectorsHierarchyFaster(String configurationName) {
		Long id = null;
		Collection<AmpClassificationConfiguration> configs = SectorUtil.getAllClassificationConfigs();
		for (AmpClassificationConfiguration conf:configs){
				if (configurationName.equals(conf.getName())) {
					if (conf.getClassification() != null)
						id = conf.getClassification().getAmpSecSchemeId();
				}
			}
		if (id == null)
			return new HashSet<>(); // empty result

		Map<Long, SectorSkeleton> parents = SectorSkeleton.getParentSectors(id);
		Map<Long, SectorSkeleton> children = SectorSkeleton.getAllSectors(parents);
		Map<Long, SectorSkeleton> subchildren = SectorSkeleton.getAllSectors(children);
		SectorSkeleton.setParentChildRelationships(parents, children, subchildren);
		return new TreeSet<>(parents.values());
	}

	// Govind's Starts from here!!
	/*
	 * this is to get the sector schemes from the ampSectorScheme table
	 */
	@SuppressWarnings("unchecked")
	public static Collection<AmpSectorScheme> getSectorSchemes() {
		String queryString = null;
		Session session = null;
		Collection<AmpSectorScheme> col = null;
		Query qry = null;

		try {
			session = PersistenceManager.getSession();// TODO why not thread
														// session? please check
														// and remove this
														// comment.
			queryString = "select pi from " + AmpSectorScheme.class.getName()
					+ " pi ";
			qry = session.createQuery(queryString);
			col = qry.list();
		} catch (Exception ex) {
			logger.error("Unable to get report names  from database "
					+ ex.getMessage());
			ex.printStackTrace(System.out);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}
		return col;
	}

	/*
	 * this is to get the level one sectors from the AmpSector table
	 */
	public static Collection getSectorLevel1(Integer schemeId) {
		String queryString = null;
		Session session = null;
		Collection col = null;
		Query qry = null;

		try {
			session = PersistenceManager.getSession();
			queryString = "select pi from "
					+ AmpSector.class.getName()
					+ " pi where pi.ampSecSchemeId=:schemeId and pi.parentSectorId IS null and (pi.deleted is null or pi.deleted = false) order by " + AmpSector.hqlStringForName("pi");
			qry = session.createQuery(queryString);
			qry.setParameter("schemeId", schemeId, Hibernate.INTEGER);
			col = qry.list();
			// session.flush();
		} catch (Exception ex) {
			logger.error("Unable to get report names  from database "
					+ ex.getMessage());
			ex.printStackTrace(System.out);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}
		return col;
	}

	/*
	 * this is to get the level one sectors that has some funding
	 */
	public static Collection getFundingLocationSectorLevel1(Integer schemeId) {
		String queryString = null;
		Session session = null;
		Collection col = null;
		Query qry = null;

		try {
			session = PersistenceManager.getSession();
			queryString = "select pi from "
					+ AmpSector.class.getName()
					+ " pi "
					+ " where pi.ampSecSchemeId=:schemeId and pi.parentSectorId IS null and (pi.deleted is null or pi.deleted = false) "
					+ " and "
					+ "(pi.ampSectorId in (select aas.sectorId from "
					+ AmpActivitySector.class.getName()
					+ " aas"
					+ " where aas.activityId in (select al.activity from "
					+ AmpActivityLocation.class.getName()
					+ " al) "
					+ " and aas.activityId in (select af.ampActivityId from "
					+ AmpFunding.class.getName()
					+ " af "
					+ "where af.ampFundingId in (select afd.ampFundingId from "
					+ AmpFundingDetail.class.getName()
					+ " afd)))"
					+ " or "
					+ " pi.ampSectorId in (select asec.parentSectorId from "
					+ AmpSector.class.getName()
					+ " asec "
					+ " where (asec.deleted is null or asec.deleted = false) and asec.ampSectorId in (select aas.sectorId from "
					+ AmpActivitySector.class.getName()
					+ " aas"
					+ " where aas.activityId in (select al.activity from "
					+ AmpActivityLocation.class.getName()
					+ " al) "
					+ " and aas.activityId in (select af.ampActivityId from "
					+ AmpFunding.class.getName()
					+ " af "
					+ "where af.ampFundingId in (select afd.ampFundingId from "
					+ AmpFundingDetail.class.getName()
					+ " afd))))"
					+ " or "
					+ " pi.ampSectorId in (select asector.parentSectorId from "
					+ AmpSector.class.getName()
					+ " asector "
					+ " where (asector.deleted is null or asector.deleted = false) and asector.ampSectorId in  (select asec.parentSectorId from "
					+ AmpSector.class.getName() + " asec "
					+ " where (asec.deleted is null or asec.deleted = false) and asec.ampSectorId in (select aas.sectorId from "
					+ AmpActivitySector.class.getName() + " aas"
					+ " where aas.activityId in (select al.activity from "
					+ AmpActivityLocation.class.getName() + " al) "
					+ " and aas.activityId in (select af.ampActivityId from "
					+ AmpFunding.class.getName() + " af "
					+ "where af.ampFundingId in (select afd.ampFundingId from "
					+ AmpFundingDetail.class.getName() + " afd)))))" +

					") order by " + AmpSector.hqlStringForName("pi");

			qry = session.createQuery(queryString);
			qry.setParameter("schemeId", schemeId, Hibernate.INTEGER);
			col = qry.list();
			// session.flush();
		} catch (Exception ex) {
			logger.error("Unable to get sectors with funding and location from database "
					+ ex.getMessage());
			ex.printStackTrace(System.out);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}
		return col;
	}

	/*
	 * get scheme to be edited
	 */
	public static Collection getEditScheme(Integer schemeId) {
		String queryString = null;
		Session session = null;
		Collection col = null;
		Query qry = null;

		try {
			session = PersistenceManager.getSession();
			queryString = "select pi from " + AmpSectorScheme.class.getName()
					+ " pi where pi.ampSecSchemeId=:schemeId";
			qry = session.createQuery(queryString);
			qry.setParameter("schemeId", schemeId, Hibernate.INTEGER);
			col = qry.list();
		} catch (Exception ex) {
			logger.error("Unable to get report names  from database "
					+ ex.getMessage());
			ex.printStackTrace(System.out);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}
		return col;
	}

	/**
	 * Returns All Configurations of Classifications
	 * 
	 * @return All Configurations
	 * @throws DgException
	 *             If exception occurred
	 */
	public static List<AmpClassificationConfiguration> getAllClassificationConfigs() {
		String queryString = "select cls from " + AmpClassificationConfiguration.class.getName() + " cls ";
		return PersistenceManager.getSession().createQuery(queryString).list();
	}

	/**
	 * Returns Classification Configuration by Configuration Id
	 * 
	 * @param configId
	 *            Configuration Id
	 * @return Classification Configuration using Configuration Id
	 * @throws DgException
	 *             If exception occurred
	 */
	public static AmpClassificationConfiguration getClassificationConfigById(
			Long configId) throws DgException {

		Session session = null;
		AmpClassificationConfiguration config = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			config = (AmpClassificationConfiguration) session.load(
					AmpClassificationConfiguration.class, configId);
		} catch (Exception ex) {
			logger.error("Unable to get configs from database "
					+ ex.getMessage());
			throw new DgException(ex);

		}

		return config;
	}

	/**
	 * Returns true if specified Scheme is selected as default classification in
	 * the configuration otherwise returns false.
	 * 
	 * @param classificationId
	 *            Id of classification
	 * @return true If specified classification is selected as default
	 *         classification in the configuration
	 * @throws DgException
	 *             If exception occurred
	 */
	public static boolean isSchemeUsed(Long classificationId)
			throws DgException {

		Session session = null;
		boolean used = false;
		String queryString = null;
		List configs = null;
		Query qry = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			queryString = "select cls from "
					+ AmpClassificationConfiguration.class.getName()
					+ " cls where cls.classification=:classificationId ";
			qry = session.createQuery(queryString);
			qry.setLong("classificationId", classificationId);
			configs = qry.list();
			if (configs != null && configs.size() > 0) {
				used = true;
			}
		} catch (Exception ex) {
			logger.error("Unable to get configs from database "
					+ ex.getMessage());
			throw new DgException(ex);

		}

		return used;
	}

	/**
	 * Returns true if specified classification is selected as default
	 * classification in the configuration otherwise returns false.
	 * 
	 * @param classificationId
	 *            Id of classification
	 * @return true If specified classification is selected as default
	 *         classification in the configuration
	 * @throws DgException
	 *             If exception occurred
	 */
	public static boolean isClassificationUsed(Long classificationId)
			throws DgException {

		Session session = null;
		boolean used = false;
		String queryString = null;
		List configs = null;
		Query qry = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			queryString = "select aps from "
					+ AmpActivitySector.class.getName()
					+ " aps where aps.classificationConfig=:classificationId";
			qry = session.createQuery(queryString);
			qry.setLong("classificationId", classificationId);
			configs = qry.list();
			if (configs != null && configs.size() > 0) {
				used = true;
			}
		} catch (Exception ex) {
			logger.error("Unable to get configs from database "
					+ ex.getMessage());
			throw new DgException(ex);

		}

		return used;
	}

	/**
	 * adds or update classification configuration
	 * 
	 * 
	 * @param configId
	 *            Id of configuration
	 * @param configName
	 *            Name of configuration
	 * @param description 
	 * @param multiSector
	 * @param classification
	 *            Default classification
	 * @throws DgException
	 *             If exception occurred
	 */
	public static void saveClassificationConfig(Long configId,
			String configName, String description, boolean multiSector,
			AmpSectorScheme classification) throws DgException {

		Session session = null;
		AmpClassificationConfiguration config = null;
		Transaction tx = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			if (configId != null && !configId.equals(0l)) {
				// Load existed configuration for update procedure.
				config = (AmpClassificationConfiguration) session.load(
						AmpClassificationConfiguration.class, configId);
			} else {
				// Create new configuration
				config = new AmpClassificationConfiguration();

			}
			config.setName(configName);
            config.setDescription(description);			
			config.setMultisector(multiSector);
			config.setClassification(classification);
			// beginTransaction();
			session.saveOrUpdate(config);
			// tx.commit();

		} catch (Exception ex) {
			logger.error("Unable to save config to database " + ex.getMessage());
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception rbf) {
					logger.error("Rollback failed");
				}
			}
			throw new DgException(ex);

		}

	}

	public static int getClassificationConfigCount(String name, Long id)
			throws Exception {
		int retValue = 0;
		Session session = null;
		String queryString = null;
		Query query = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			queryString = "select count(cc.id) from "
					+ AmpClassificationConfiguration.class.getName()
					+ " cc where lower(cc.name)=lower('" + name + "')";
			if (id != null) {
				queryString += " and cc.id!=" + id;
			}
			query = session.createQuery(queryString);
			retValue = (Integer) query.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retValue;
	}

	/**
	 * Loads AmpClassificationConfiguration bean which is primary. Please see
	 * next method which is old version and was not touch to not damage
	 * anything.
	 * 
	 * @return primary configuration
	 * @throws DgException
	 */
	public static AmpClassificationConfiguration getPrimaryConfigClassification()
			throws DgException {
		Session session = PersistenceManager.getRequestDBSession();
		String queryString = null;
		Query qry = null;
		try {
			queryString = "select config from "
					+ AmpClassificationConfiguration.class.getName()
					+ " config inner join config.classification cls "
					+ " where config.primary=true ";
			qry = session.createQuery(queryString);
			// There must be only one primary configuration in database
			return (AmpClassificationConfiguration) qry.uniqueResult();

		} catch (Exception ex) {
			logger.error("Unable to save config to database " + ex.getMessage());
			throw new DgException(ex);

		}
	}

	/**
	 * Loads AmpClassificationConfiguration bean which is secondary. 
	 * @return primary configuration
	 * @throws DgException
	 */
	public static AmpClassificationConfiguration getSecondaryConfigClassification()
			throws DgException {
		Session session = PersistenceManager.getRequestDBSession();
		String queryString = null;
		Query qry = null;
		try {
			queryString = "select config from "
					+ AmpClassificationConfiguration.class.getName()
					+ " config inner join config.classification cls "
					+ " where config.name='Secondary' ";
			qry = session.createQuery(queryString);
			// There must be only one primary configuration in database
			return (AmpClassificationConfiguration) qry.uniqueResult();

		} catch (Exception ex) {
			logger.error("Unable to save config to database " + ex.getMessage());
			throw new DgException(ex);

		}
	}

	/**
	 * gets id of classification which is selected in primary configuration
	 * 
	 * 
	 * @return Id of classification
	 * @throws DgException
	 *             If exception occurred
	 */
	public static Long getPrimaryConfigClassificationId() throws DgException {

		Session session = null;
		Long id = null;
		String queryString = null;
		Query qry = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			queryString = "select cls.ampSecSchemeId from "
					+ AmpClassificationConfiguration.class.getName()
					+ " config inner join config.classification cls "
					+ " where config.primary=true ";
			// queryString = "select cls.ampSecSchemeId from "
			// + AmpClassificationConfiguration.class.getName() +
			// " config inner join config.classification cls "+
			// " where config.primary=true ";
			qry = session.createQuery(queryString);
			// There must be only one primary configuration in database
			id = (Long) qry.uniqueResult();

		} catch (Exception ex) {
			logger.error("Unable to get config from database ", ex);
			throw new DgException(ex);

		}
		return id;

	}

	/***
	 * 
	 * @param classificationId
	 */
	public static void deleteClassification(Long classificationId) {
		Session session = null;
		AmpClassificationConfiguration config = null;
		Transaction tx = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			// beginTransaction();
			config = (AmpClassificationConfiguration) session.load(
					AmpClassificationConfiguration.class, classificationId);
			session.delete(config);
			// tx.commit();
			// session.flush();
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/*
	 * this is to delete a scheme
	 */
	public static void deleteScheme(Long schemeId) {
		logger.info(" deleting the scheme");
		Session session = null;
		Transaction tx = null;
		try {
			session = PersistenceManager.getSession();
			AmpSectorScheme scheme = (AmpSectorScheme) session.load(
					AmpSectorScheme.class, schemeId);
			// beginTransaction();
			session.delete(scheme);
			// tx.commit();
		} catch (Exception e) {
			logger.error("Exception from deleteQuestion() :" + e.getMessage());
			e.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception trbf) {
					logger.error("Transaction roll back failed ");
					e.printStackTrace(System.out);
				}
			}
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Failed to release session :"
							+ rsf.getMessage());
				}
			}
		}
	}

	/*
	 * this is to get the level one sectors from the AmpSector table
	 */
	public static AmpSectorScheme getParentSchemeId(Long id) {
		String queryString = null;
		Session session = null;
		Collection col = null;
		Query qry = null;
		AmpSectorScheme schemeId = null;

		try {
			session = PersistenceManager.getSession();

			AmpSector sec = (AmpSector) session.load(AmpSector.class, id);
			schemeId = sec.getAmpSecSchemeId();

		} catch (Exception ex) {
			logger.error("Unable to get report names  from database "
					+ ex.getMessage());
			ex.printStackTrace(System.out);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}
		return schemeId;
	}

	public static void deleteSector(Long sectorId) {
		logger.info(" deleting the Sector");
		Session session = null;
		Transaction tx = null;
		try {
			session = PersistenceManager.getSession();
			AmpSector sector = (AmpSector) session.load(AmpSector.class,
					sectorId);
			// beginTransaction();
			//sector.setAidlist(null);
			sector.setDeleted(true);
			session.saveOrUpdate(sector);
			AmpCaching.getInstance().sectorsCache=null;
			// tx.commit();
		} catch (Exception e) {
			logger.error("Exception from deleteQuestion() :" + e.getMessage());
			e.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception trbf) {
					logger.error("Transaction roll back failed ");
					e.printStackTrace(System.out);
				}
			}
		} /*finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Failed to release session :"
							+ rsf.getMessage());
				}
			}
		}*/
	}

	public static void deleteIndSector(Long sectorid, Long indid) {

		logger.info(" deleting the indsectors");
		Session session = null;
		Transaction tx = null;
		AmpThemeIndicators ampThemeInd = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			// beginTransaction();
			ampThemeInd = (AmpThemeIndicators) session.load(
					AmpThemeIndicators.class, indid);
			Iterator itr = ampThemeInd.getSectors().iterator();
			while (itr.hasNext()) {
				AmpIndicatorSector ind = (AmpIndicatorSector) itr.next();
				if (ind.getSectorId().getAmpSectorId().equals(sectorid)) {
					itr.remove();
					session.delete(ind);

				}
			}
			session.update(ampThemeInd);
			// tx.commit();
			// session.flush();

		} catch (Exception e) {
			// TODO: handle exception
			logger.error("Exception from deleteIndSectors:" + e.getMessage());
			e.printStackTrace(System.out);
		}

	}

	public static Set<AmpSector> getSectorDescendents(
			Collection<AmpSector> parentSectors) {
		Set<AmpSector> generatedSectors = new HashSet<AmpSector>();

		generatedSectors.addAll(parentSectors);
		Iterator sectorIterator = parentSectors.iterator();

		while (sectorIterator.hasNext()) {// process each sector and get all its
											// children
			AmpSector currentSector = (AmpSector) sectorIterator.next();
			if (currentSector != null) {
				Collection childSectors = SectorUtil
						.getAllChildSectors(currentSector.getAmpSectorId());
				generatedSectors.addAll(childSectors); // add the children
														// sectors to the filter

				// add the grand children
				Iterator childSectorsIterator = childSectors.iterator();
				while (childSectorsIterator.hasNext()) {
					AmpSector currentChild = (AmpSector) childSectorsIterator
							.next();
					Collection grandChildrenSectors = SectorUtil
							.getAllChildSectors(currentChild.getAmpSectorId());
					generatedSectors.addAll(grandChildrenSectors);
				}

			}

		}

		return generatedSectors;
	}

	// This recursive method helps the generateLevelHierarchy method.
	public static AmpSector getTopLevelParent(AmpSector topLevelSector) {
		if (topLevelSector.getParentSectorId() != null) {
			topLevelSector = getTopLevelParent(topLevelSector
					.getParentSectorId());
		}
		return topLevelSector;
	}

	/**
	 * returns set of all (recursive) descendants' ids of a given set of sectors
	 * @param locations
	 * @return
	 */
	public static Set<Long> populateWithDescendantsIds(Collection<AmpSector> sectors)
	{
		Set<Long> allOutputLocations = getRecursiveChildrenOfSectors(AlgoUtils.collectIds(new HashSet<Long>(), sectors));
		return allOutputLocations;
	}	
	
	/**
	 * recursively get all children of a set of AmpSectors, by a wave algorithm
	 * @param inIds
	 * @return
	 */
	public static Set<Long> getRecursiveAscendantsOfSectors(Collection<Long> inIds)
	{
		return AlgoUtils.runWave(inIds, 
			new DatabaseWaver("SELECT DISTINCT (parent_sector_id) FROM amp_sector WHERE (parent_sector_id IS NOT NULL) AND (amp_sector_id IN ($))"));
	}
	
	/**
	 * recursively get all children of a set of AmpCategoryValueLocations, by a wave algorithm
	 * @param inIds
	 * @return
	 */
	public static Set<Long> getRecursiveChildrenOfSectors(Collection<Long> inIds)
	{
		return AlgoUtils.runWave(inIds, 
				new DatabaseWaver("SELECT DISTINCT amp_sector_id FROM amp_sector WHERE (deleted is null or deleted = false) AND parent_sector_id IN ($)"));
	}

	public static List<AmpActivityVersion> getActivitiesForSector(Long id) {
		Session session = null;
		List<AmpActivityVersion> activities = null;
		try {
			session = PersistenceManager.getSession();
			String queryString = "select aps from "
					+ AmpActivitySector.class.getName()
					+ " aps,"
					+ AmpActivityGroup.class.getName()
					+ " apg "
					+ " where aps.activityId.ampActivityId = apg.ampActivityLastVersion.ampActivityId and  aps.sectorId.ampSectorId=:id and apg.ampActivityLastVersion.deleted is not true";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", id);
			activities = qry.list();
		} catch (Exception e) {
			logger.error(e);
		}
		return activities;
	}
}
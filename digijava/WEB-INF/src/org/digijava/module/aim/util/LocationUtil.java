package org.digijava.module.aim.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import org.apache.log4j.Logger;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpRegion;
import org.digijava.module.aim.dbentity.AmpZone;
import org.digijava.module.aim.dbentity.AmpWoreda;
import org.digijava.module.aim.helper.Location;

public class LocationUtil {

	private static Logger logger = Logger.getLogger(LocationUtil.class);

	public static Collection searchForLocation(String keyword, int implevel) {
		Session session = null;
		Collection col = new ArrayList();
		
		logger.info("INSIDE Search Location DBUTIL..... ");
	
		try {
			
			session = PersistenceManager.getSession();
			int tempIncr = 0;
			if (implevel == 2) {
				logger.info("searching regions........");
				String qryStr = "select country.countryId,country.countryName,region.ampRegionId,region.name "
						+ "from "
						+ Country.class.getName()
						+ " country , "
						+ AmpRegion.class.getName()
						+ " "
						+ "region where region.name like '%"
						+ keyword
						+ "%' and " + "country.iso = region.country";
				
				Query qry = session.createQuery(qryStr);
				Iterator itr = qry.list().iterator();
				
				while (itr.hasNext()) {
					Object obj[] = (Object[]) itr.next();
					Long cId = (Long) obj[0];
					String cName = (String) obj[1];
					Long rId = (Long) obj[2];
					String rName = (String) obj[3];
					
					Location loc = new Location();
					loc.setCountry(cName);
					loc.setCountryId(cId);
					loc.setRegion(rName);
					loc.setRegionId(rId);
					loc.setLocId(new Long(System.currentTimeMillis() + (tempIncr++)));
					col.add(loc);
				}
			} else if (implevel == 3) {
				logger.info("searching zones........");
				
/*				String qryStr = "select country.countryId,country.countryName,region.ampRegionId,region.name "
					+ "from "
					+ Country.class.getName()
					+ " country , "
					+ AmpRegion.class.getName()
					+ " "
					+ "region where region.name like '%"
					+ keyword
					+ "%' and " + "country.iso = region.country";
*/
				String qryStr = "select country.countryId,country.countryName,region.ampRegionId, region.name, zone.ampZoneId, zone.name "
					+ "from "
					+ Country.class.getName()
					+ " country, "
					+ AmpRegion.class.getName()
					+ " region, "
					+ AmpZone.class.getName()
					+ " zone "
					+ "where zone.name like '%"
					+  keyword
					+ "%' and "
					+ "region.ampRegionId = zone.region and "
					+ "country.iso = zone.country";
				
				Query qry = session.createQuery(qryStr);
				Iterator itr = qry.list().iterator();
				
				tempIncr = 0;
				while (itr.hasNext()) {
					Object obj[] = (Object[]) itr.next();
					Long cId = (Long) obj[0];
					String cName = (String) obj[1];
					Long rId = (Long) obj[2];
					String rName = (String) obj[3];
					Long zId = (Long) obj[4];
					String zName = (String) obj[5];
					
					Location loc = new Location();
					loc.setLocId(new Long(System.currentTimeMillis()+(tempIncr++)));
					logger.info("Loc id set as " + loc.getLocId());
					loc.setCountry(cName);
					loc.setCountryId(cId);
					loc.setRegion(rName);
					loc.setRegionId(rId);
					loc.setZone(zName);
					loc.setZoneId(zId);
					col.add(loc);
				}
				
			} else if (implevel == 4) {
				logger.info("searching Woredas........");
				
				String qryStr = "select country.countryId,country.countryName,region.ampRegionId, region.name, zone.ampZoneId, zone.name, woreda.ampWoredaId, woreda.name "
					+ "from "
					+ Country.class.getName()
					+ " country, "
					+ AmpRegion.class.getName()
					+ " region, "
					+ AmpZone.class.getName()
					+ " zone, "
					+ AmpWoreda.class.getName()
					+ " woreda, "
					+ " where woreda.name like '%"
					+  keyword
					+ "%' and "
					+ "region.ampRegionId = zone.region and "
					+ "woreda.zone = zone.ampZoneId and "
					+ "country.iso = zone.country";
				

				Query qry = session.createQuery(qryStr);
				Iterator itr = qry.list().iterator();
				
				tempIncr = 0;
				while (itr.hasNext()) {
					Object obj[] = (Object[]) itr.next();
					Long cId = (Long) obj[0];
					String cName = (String) obj[1];
					Long rId = (Long) obj[2];
					String rName = (String) obj[3];
					Long zId = (Long) obj[4];
					String zName = (String) obj[5];
					Long wId = (Long) obj[6];
					String wName = (String) obj[7];
					
					Location loc = new Location();
					loc.setLocId(new Long(System.currentTimeMillis() + (tempIncr++)));
					loc.setCountry(cName);
					loc.setCountryId(cId);
					loc.setRegion(rName);
					loc.setRegionId(rId);
					loc.setZone(zName);
					loc.setZoneId(zId);
					loc.setWoreda(wName);
					loc.setWoredaId(wId);
					col.add(loc);
				}
				
			} else {
				
				logger.info("Imp level is not selected for search....");
				/*String queryString = "select loc from "
						+ AmpLocation.class.getName() + " loc "
						+ "where country like '%" + keyword + "%'";
				Query qry = session.createQuery(queryString);*/
			}
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
	//End Search Location.

}
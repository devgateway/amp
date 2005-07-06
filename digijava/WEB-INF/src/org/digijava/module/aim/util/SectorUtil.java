package org.digijava.module.aim.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpSectorScheme;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.Sector;
;


public class SectorUtil {

	private static Logger logger = Logger.getLogger(LocationUtil.class);

	public static Collection searchForSector(String keyword) {
		Session session = null;
		Collection col = new ArrayList();
		
		logger.info("INSIDE Search Sector DBUTIL..... ");
	
		try {
			
			session = PersistenceManager.getSession();
			int tempIncr = 0;
				logger.info("searching SECTORS...............");
				/*String qryStr = "select country.countryId,country.countryName,region.ampRegionId,region.name "
						+ "from "
						+ Country.class.getName()
						+ " country , "
						+ AmpRegion.class.getName()
						+ " "
						+ "region where region.name like '%"
						+ keyword
						+ "%' and " + "country.iso = region.country"; */

				String qryStr = "select sector.name, sector.type "
					+ "from "
					+ AmpSector.class.getName()
					+ " sector , "
					+ AmpSectorScheme.class.getName()
					+ " sscheme"
					+ " where sector.name like '%"
					+ keyword
					+ "%' and " + "sector.ampSecSchemeId = sscheme.ampSecSchemeId";
							
				Query qry = session.createQuery(qryStr);
				//qry.setParameter("orgType", orgType, Hibernate.LONG) ;				
				//col =qry.list();
				Iterator itr = qry.list().iterator();
				
				ActivitySector sectr;
				tempIncr = 0;
				
				while (itr.hasNext()) {
					Object obj[] = (Object[]) itr.next();
					System.out.println("object of type:"+obj[0].getClass().getName());
					String sName = (String) obj[0];
					
					sectr = new ActivitySector();
					sectr.setSectorId(new Long(System.currentTimeMillis()+(tempIncr++)));
					logger.info("sector id set as " + sectr.getSectorId());
					sectr.setSectorName(sName);
					logger.info("sector name set as " +	 sectr.getSectorName());
					logger.info("adding sector now...");
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
	}//End Search Sector.
}
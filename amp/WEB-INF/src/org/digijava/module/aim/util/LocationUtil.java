package org.digijava.module.aim.util;



import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.AmpARVRegions;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpRegion;
import org.digijava.module.aim.dbentity.AmpSiteFlag;
import org.digijava.module.aim.dbentity.AmpWoreda;
import org.digijava.module.aim.dbentity.AmpZone;
import org.digijava.module.aim.dbentity.CMSContentItem;
import org.digijava.module.aim.exception.dynlocation.DuplicateLocationCodeException;
import org.digijava.module.aim.helper.AmpLocations;
import org.digijava.module.aim.helper.Location;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class LocationUtil {

	private static Logger logger = Logger.getLogger(LocationUtil.class);
        
         private static boolean countryHasFlag(String iso) throws HibernateException, DgException {
        boolean show = true;
        Collection<Country> countries = FeaturesUtil.getDefaultCountry(iso);
        Iterator<Country> iter = countries.iterator();
        while (iter.hasNext()) {
            Country country = iter.next();

            Session session = PersistenceManager.getRequestDBSession();
            String qryStr = "select flg from " + AmpSiteFlag.class.getName() + " flg  where flg.countryId=" + country.getCountryId();
            Query qry = session.createQuery(qryStr);
            List flags = qry.list();
            if (flags != null && flags.size() > 0) {
                show = false;
                break;
            }
        }

        return show;
    }

    public static boolean countryHasRef(String iso) {
        Session session = null;
        List col = new ArrayList();
        String qryStr = null;
        Query qry = null;
        boolean show = true;
        Class[] classNames =
                {
        		AmpActivity.class, User.class, AmpRegion.class, AmpWoreda.class, AmpZone.class,
        		CMSContentItem.class, AmpCurrency.class, AmpLocation.class, AmpOrganisation.class,
              };
            String subQryStr="cls.country";

        try {
            if (iso == null || iso.equals("Select") || FeaturesUtil.getDefaultCountryIso().equals(iso)) {
                show = false;
                return show;
            }
            session = PersistenceManager.getRequestDBSession();
            for (int i = 0; i < classNames.length; i++) {
                if (classNames[i].equals(AmpOrganisation.class) || classNames[i].equals(AmpCurrency.class)) {
                    subQryStr = "cls.countryId";
                } else {
                    if (classNames[i].equals(AmpLocation.class)) {
                        subQryStr = "cls.dgCountry";
                    }
                }
                qryStr = "select c from " + classNames[i].getName() +
                        " cls inner join " + subQryStr + " c where c.iso=:iso";
                qry = session.createQuery(qryStr);
                qry.setString("iso", iso);
                List contries = qry.list();
                if (contries != null && contries.size() > 0) {
                    col.add(contries);
                }
            }
            if (col.size() > 0) {
                show = false;
            } else {
                show = countryHasFlag(iso);
            }
        } catch (Exception ex) {
            show = false;
            throw new RuntimeException(ex);
        }
        return show;
    }




	public static Collection searchForLocation(String keyword, int implevel) {
		Session session = null;
		Collection col = new ArrayList();

		//logger.info("INSIDE Search Location DBUTIL..... ");

		try {

			session = PersistenceManager.getRequestDBSession();
			int tempIncr = 0;
			if (implevel == 2) {
				//logger.info("searching regions........");
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
				//logger.info("searching zones........");

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
					//logger.info("Loc id set as " + loc.getLocId());
					loc.setCountry(cName);
					loc.setCountryId(cId);
					loc.setRegion(rName);
					loc.setRegionId(rId);
					loc.setZone(zName);
					loc.setZoneId(zId);
					col.add(loc);
				}

			} else if (implevel == 4) {
				//logger.info("searching Woredas........");

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
		} 
		return col;
	}
	//End Search Location.
	public static AmpLocation getAmpLocationByCVLocation(Long ampCVLocationId) {
		Session session = null;
		AmpLocation loc = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			
			String queryString 	= "select l from " + AmpLocation.class.getName()
					+ " l where location=:locationId order by l.ampLocationId";
			Query qry = session.createQuery(queryString);
			qry.setLong("locationId", ampCVLocationId);
			
			Collection result	= qry.list();
			if ( result != null && result.size() > 0 ) {
				return (AmpLocation)result.iterator().next();
			}
			
		} catch (Exception e) {
			logger.error("Uanble to get location :" + e);
		} 
		return loc;
		
	}
	
	@Deprecated
	public static AmpLocation getAmpLocation(String countryId, Long regionId,
			Long zoneId, Long woredaId) {

		Session session = null;
		AmpLocation loc = null;

		try {
			session = PersistenceManager.getRequestDBSession();

			String queryString = "select l from " + AmpLocation.class.getName()
					+ " l";
			if (countryId != null && (!(countryId.equals(new Long(-1))))) {
				queryString += " where country_id like '" + countryId+"'";
			}
                        else{
                             queryString += " where country_id  is NULL ";
                        }

			if (regionId != null && (!(regionId.equals(new Long(-1))))) {
				queryString += " and region_id = " + regionId;
			}
                        else{
                             queryString += " and region_id  is NULL ";
                        }

			if (zoneId != null && (!(zoneId.equals(new Long(-1))))) {
				queryString += " and zone_id = " + zoneId;
			}
                        else{
                            queryString += " and zone_id  is NULL ";
                        }

			if (woredaId != null && (!(woredaId.equals(new Long(-1))))) {
				queryString += " and woreda_id = " + woredaId;
			}
                        else{
                            queryString += " and woreda_id  is NULL ";
                        }

			Query qry = session.createQuery(queryString);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext())
				loc = (AmpLocation) itr.next();

		} catch (Exception e) {
			logger.error("Uanble to get location :" + e);
		} 
		return loc;
	}
        public static boolean isAssignedToActivity(Long countryId, Long regionId,
			Long zoneId, Long woredaId)throws DgException{
          boolean  isAssignedToActivity=true;
          Session session = null;
          boolean flag = false;
          try {
            session = PersistenceManager.getRequestDBSession();

                        String queryString = "select  l from " + AmpLocation.class.getName()
                                        + " l join l.aidlocation locs";
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

                        Query qry = session.createQuery(queryString);
                        List ampLocations=qry.list();
                        if(ampLocations==null||ampLocations.size()==0){
                          isAssignedToActivity=false;
                        }


          }
          catch (Exception ex) {
            logger.error("Uanble to get location :" + ex);
            throw new DgException(ex);

          }



          return isAssignedToActivity;
        }




	public static List getAmpLocations(Long id) {
		AmpLocation ampLocation = null;
		ArrayList ampLocations = new ArrayList();
		Session session = null;
		Iterator iter = null;
		try {
			session = PersistenceManager.getRequestDBSession();

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
			//logger.debug("Activity: " + ampActivity.getAmpActivityId());
			iter = ampActivity.getLocations().iterator();
			while (iter.hasNext()) {
				ampLocation = (AmpLocation) iter.next();
				AmpLocations location = new AmpLocations();
				location.setCountry(ampLocation.getCountry());
				//logger.debug("Country: " + location.getCountry());
				if (ampLocation.getAmpRegion() != null)
					location.setRegion(ampLocation.getAmpRegion().getName());
				//logger.debug("Region: " + location.getRegion());
				if (ampLocation.getAmpZone() != null)
					location.setZone(ampLocation.getAmpZone().getName());
				//logger.debug("Zone: " + location.getZone());
				if (ampLocation.getAmpWoreda() != null)
					location.setWoreda(ampLocation.getAmpWoreda().getName());
				//logger.debug("Woreda: " + location.getWoreda());
				ampLocations.add(location);
			}
		} catch (Exception ex) {
			logger.error("Unable to get amp locations :" + ex.getMessage());
		} 
		return ampLocations;
	}

	public static List getAllVRegions() {
		ArrayList ampRegions = new ArrayList();
		Session session = null;
		Iterator iter = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			Connection connection = session.connection();
			
			//String queryString = "select distinct region_id, region from v_regions;";
			String queryString = "select distinct amp_region_id as region_id, name as region from amp_region order by region";
			
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(queryString);
			

		    while (rs.next()) {

		        // get current row values
		    	AmpARVRegions region=new AmpARVRegions();
		    	Long rId = rs.getLong(1);
		    	//Long rId = rs.getLong(2);
		    	String reg = rs.getString(2);
				//region.setActivityId(aId);
				region.setRegion(reg);
				region.setRegionId(rId);
				ampRegions.add(region);

		        // print values
		        }

		    // close statement and connection
		    stmt.close();
		    //con.close();
			/*Query qry = session.createQuery(queryString);
			iter = qry.list().iterator();
			while(iter.hasNext())
			{
				Object obj[] = (Object[]) iter.next();
				AmpARVRegions region=new AmpARVRegions();
				Long aId = (Long) obj[0];
				String reg=(String)obj[1];
				Long rId=(Long)obj[2];
				region.setActivityId(aId);
				region.setRegion(reg);
				region.setRegionId(rId);
				ampRegions.add(region);
			}
			*/
			
		} catch (Exception ex) {
			logger.error("Unable to get amp Regions :" + ex.getMessage());
		}
		return ampRegions;
	}
        /**
         * 
         * @deprecated use DynLocationManagerUtil.getLocationsOfTypeRegionOfDefCountry() instead
         */
       @Deprecated
        public static List getAllDefCountryArRegions() {
		ArrayList ampRegions = new ArrayList();
		Session session = null;
                Query q=null;
		Iterator <AmpRegion> iter = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select reg  from " +AmpRegion.class.getName()
                                +" reg  inner join reg.country country where country.iso=:iso order by reg.name";
                       
			q=session.createQuery(queryString);
                        q.setString("iso",  FeaturesUtil.getDefaultCountryIso());
                        List regions=q.list();
                        if(regions!=null){
                         iter=regions.iterator();
                         while(iter.hasNext()){
                            AmpRegion reg=iter.next();
                            AmpARVRegions region=new AmpARVRegions();
                            region.setRegionId(reg.getAmpRegionId());
                            region.setRegion(reg.getName());
                            ampRegions.add(region);
                         }
                        }
                       


		
		} catch (Exception ex) {
			logger.error("Unable to get amp Regions :" + ex.getMessage());
		} 
		return ampRegions;
	}
	
	public static Collection getAllLocations(Long id) {
		AmpLocation ampLocation = null;
		ArrayList ampLocations = new ArrayList();
		Session session = null;
		Iterator iter = null;
		try {
			session = PersistenceManager.getRequestDBSession();

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
		} 
		return ampLocations;
	}

	public static AmpLocation getAmpLocation(Long id) {
		AmpLocation ampLocation = null;
		Session session = null;
		try {
			session = PersistenceManager.getRequestDBSession();
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
		} 
		return ampLocation;
	}

	public static AmpRegion getAmpRegion(Long id) {
		Session session = null;
		AmpRegion region = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select r from " + AmpRegion.class.getName()
					+ " r " + "where (r.ampRegionId=:id) order by r.regionCode, r.name ";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				region = (AmpRegion) itr.next();
			}

		} catch (Exception e) {
			logger.debug("Exception from getAmpRegion()");
			logger.debug(e.toString());
		} 
		return region;
	}

	public static AmpZone getAmpZone(Long id) {
		Session session = null;
		AmpZone zone = null;

		try {
			session = PersistenceManager.getRequestDBSession();
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
		} 
		return zone;
	}

	public static AmpWoreda getAmpWoreda(Long id) {
		Session session = null;
		AmpWoreda woreda = null;

		try {
			session = PersistenceManager.getRequestDBSession();
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
		} 
		return woreda;
	}

	public static Collection<AmpRegion> getAllRegionsUnderCountryUnselected(String iso, Collection selectedLocations) {
		Session session = null;
		Collection<AmpRegion> col = null;

		try {
			org.digijava.module.aim.helper.Location locationHlp = null;
			String selLocNames = "";
			for (Iterator it = selectedLocations.iterator(); it.hasNext();) {
        		locationHlp = (org.digijava.module.aim.helper.Location) it.next();
        		//
        		selLocNames += "'" + locationHlp.getRegion() + "'" + ", ";
        	}
			selLocNames = selLocNames.substring(0, selLocNames.lastIndexOf(","));
			//
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select reg from " + AmpRegion.class.getName()
					+ " reg " + "where (country_id = '" + iso + "' " 
					+ "and reg.name not in ("+selLocNames+")) "
					+ "order by reg.regionCode, reg.name";

			Query qry = session.createQuery(queryString);
			col = qry.list();
		} catch (Exception e) {
			logger.debug("Exception from getAllRegionsUnderCountry()");
			logger.debug(e.toString());
		} 
		return col;
	}

	public static Collection<AmpRegion> getAllRegionsUnderCountry(String iso) {
		Session session = null;
		Collection<AmpRegion> col = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select reg from " + AmpRegion.class.getName()
					+ " reg " + "where country_id = '" + iso + "' order by reg.regionCode, reg.name";

			Query qry = session.createQuery(queryString);
			col = qry.list();
		} catch (Exception e) {
			logger.debug("Exception from getAllRegionsUnderCountry()");
			logger.debug(e.toString());
		} 
		return col;
	}

	public static Collection<AmpZone> getAllZonesUnderRegion(Long id) {
		Session session = null;
		Collection<AmpZone> col = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select zon from " + AmpZone.class.getName()
					+ " zon " + "where region_id = " + id
					+ " order by zon.name";

			Query qry = session.createQuery(queryString);
			col = qry.list();
		} catch (Exception e) {
			logger.debug("Exception from getAllZonesUnderRegion()");
			logger.debug(e.toString());
		} 
		return col;
	}

	public static Collection<AmpWoreda> getAllWoredasUnderZone(Long id) {
		Session session = null;
		Collection<AmpWoreda> col = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select wor from " + AmpWoreda.class.getName()
					+ " wor " + "where zone_id = " + id + " order by wor.name";

			Query qry = session.createQuery(queryString);
			col = qry.list();
		} catch (Exception e) {
			logger.debug("Exception from getAllWoredasUnderZone()");
			logger.debug(e.toString());
		} 
		return col;
	}
         /**
         * Returns list of locations using their ids
         * @param ids consists  selected locations id separted by comma
         * @return List of AmpCategoryValueLocations beans
         * @throws DgException if anything goes wrong
         */

        public static List<AmpCategoryValueLocations> getAllLocations(String ids) throws DgException{
		Session session = null;
		 List<AmpCategoryValueLocations> col = null;

		try {
                    
			session = PersistenceManager.getRequestDBSession();
			String queryString = " from " + AmpCategoryValueLocations.class.getName()+
                        " vl where vl.parentLocation  is null " ;
                        
                         if(ids!=null&&ids.length()>0){
                            String id=ids.substring(0, ids.length()-1);
                            queryString+="  or vl.parentLocation  in ("+id+")";
                        }

			Query qry = session.createQuery(queryString);
			col = qry.list();
		} catch (Exception e) {
			logger.error("Unable to get locations from database "
					+ e.getMessage());
                        throw new DgException(e);
		} 
		return col;
	}
        
         /**
         * Returns location using its id
         * @param id of location
         * @return AmpCategoryValueLocations bean
         * @throws DgException if anything goes wrong
         */
        
         public static AmpCategoryValueLocations getAmpCategoryValueLocationById(Long id) throws DgException {
		Session session = null;
		 AmpCategoryValueLocations loc= null;

		try {
                    
			session = PersistenceManager.getRequestDBSession();
			loc = (AmpCategoryValueLocations) session.load(AmpCategoryValueLocations.class, id);
			/*
			String queryString = " from " + AmpCategoryValueLocations.class.getName()+
                        " vl where vl.id=:id" ;
			Query qry = session.createQuery(queryString);
                        qry.setLong("id", id);
			loc = (AmpCategoryValueLocations)qry.uniqueResult();*/
		} catch (Exception e) {
			logger.error("Unable to get location from database "
					+ e.getMessage());
                         throw new DgException(e);
		} 
		return loc;
	}
         
         
         /**
         * Saves location into the database
         * @param b 
         * @param AmpCategoryValueLocations bean
         * @throws DgException 
         */
         
       public static void saveLocation(AmpCategoryValueLocations loc, boolean editing) throws DgException{
        Session session = null;
        Transaction tx = null;

        if (!editing){
        	
        	/*  country check for duplicate iso and iso3 codes */
        	boolean isCountry	=  
        		CategoryManagerUtil.equalsCategoryValue( loc.getParentCategoryValue(), CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY);
        	if ( isCountry ) {
        		AmpCategoryValueLocations tempLoc	= 
        			DynLocationManagerUtil.getLocationByIso(loc.getIso(), CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY );
        		if ( tempLoc != null ) 
        			throw new DuplicateLocationCodeException("There is already a country with the same iso !", "iso", loc.getParentCategoryValue().getValue() );
        		tempLoc	= 
        			DynLocationManagerUtil.getLocationByIso3(loc.getIso3(), CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY );
        		if ( tempLoc != null ) 
        			throw new DuplicateLocationCodeException("There is already a country with the same iso 3!", "iso3", loc.getParentCategoryValue().getValue() );
        		
        	}
        	
        	AmpCategoryValueLocations tempLoc	= 
        		DynLocationManagerUtil.getLocationByName(loc.getName(), loc.getParentCategoryValue(), loc.getParentLocation() );
        	if ( tempLoc != null ) 
        		throw new DuplicateLocationCodeException("There is already a location with the same name!", "name", loc.getParentCategoryValue().getValue() );
        }
        
//        if ( loc.getCode() != null && loc.getCode().length() > 0 ) {
//		        AmpCategoryValueLocations tempLoc	= 
//		        	DynLocationManagerUtil.getLocationByCode(loc.getCode(), loc.getParentCategoryValue());
//		        if ( tempLoc != null ) 
//		        	throw new DuplicateLocationCodeException("There is already a location with the same code", "code", loc.getParentCategoryValue().getValue() );
//        }
//        else {
//        	if ( !isCountry ){
//        		// For consistency reasons this should be checked through javascript on client-side
//        		throw new DgException ("Code should not be empty for locations that are not country.");
//        	}
//        }
        /* END - country check for duplicate iso and iso3 codes */
        
        try {

            session = PersistenceManager.getRequestDBSession();
            tx = session.beginTransaction();
            session.saveOrUpdate(loc);
            tx.commit();
        } catch (Exception e) {

            if (tx != null) {
                try {
                    tx.rollback();

                } catch (HibernateException ex) {
                     logger.error("Unable to rollback transaction  "
					+ e.getMessage());
                }
            }
            logger.error("Unable to save location into the database "
					+ e.getMessage());
             throw new DgException(e);
        }

    }
	public static ArrayList getAmpLocationsForDefaultCountry() {
		Session session = null;
		Query q = null;
		AmpRegion ampRegion = null;
		ArrayList region = new ArrayList();
		String queryString = null;
		Iterator iter = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			queryString = " select l from " + AmpRegion.class.getName()
					+ " l where l.country=:country order by l.name";
			q = session.createQuery(queryString);
			q.setParameter("country", FeaturesUtil.getGlobalSettingValue("Default Country"),Hibernate.STRING);
			iter = q.list().iterator();

			while (iter.hasNext()) {

				ampRegion = (AmpRegion) iter.next();
				region.add(ampRegion);
			}

		} catch (Exception ex) {
			logger.error("Unable to get Amp location names  from database "
					+ ex.getMessage());
		} 
		return region;
	}
	public static ArrayList getAmpLocations() {
		Session session = null;
		Query q = null;
		AmpRegion ampRegion = null;
		ArrayList region = new ArrayList();
		String queryString = null;
		Iterator iter = null;

		try {
			session = PersistenceManager.getRequestDBSession();
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
		} 
		return region;
	}

	public static Long getDgCountryWithMaxCountryId() {
		Session session = null;
		Long id = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select max(c.countryId) from " + Country.class.getName() + " c";
			id = (Long) session.createQuery(queryString).list().get(0);
		} catch (Exception ex) {
			logger.debug("Exception from getDgCountry()" + ex);
		} 
		return id;
	}

	public static boolean chkDuplicateIso(String name, String iso, String iso3) {
		boolean result = true;
		Session session = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select c from " + Country.class.getName()
									+ " c where (c.countryName=:name) or (c.iso=:iso) or (c.iso3=:iso3)";
			Query q = session.createQuery(queryString);
			q.setParameter("name", name, Hibernate.STRING);
			q.setParameter("iso", iso, Hibernate.STRING);
			q.setParameter("iso3", iso3, Hibernate.STRING);
			if (null != q.list() && q.list().size() > 0)
				result = false;
		}
		catch (Exception ex) {
			logger.debug("Exception from chkDuplicateIso(): " + ex);
			ex.printStackTrace(System.out);
		}
		
		return result;
	}

    public static class HelperAmpRegionNameComparator
        implements Comparator {
        public int compare(Object obj1, Object obj2) {
            AmpRegion indic1 = (AmpRegion) obj1;
            AmpRegion indic2 = (AmpRegion) obj2;
            return indic1.getName().compareTo(indic2.getName());
        }
        }
        /**
         * Saves location into the database
         * @param AmpLocation bean
         */
         
       public static void saveLocation(AmpLocation loc) throws DgException{
        Session session = null;
        Transaction tx = null;


        try {

            session = PersistenceManager.getRequestDBSession();
            tx = session.beginTransaction();
            session.saveOrUpdate(loc);
            tx.commit();
        } catch (Exception e) {

            if (tx != null) {
                try {
                    tx.rollback();

                } catch (HibernateException ex) {
                     logger.error("Unable to rollback transaction  "
					+ e.getMessage());
                }
            }
            logger.error("Unable to save location into the database "
					+ e.getMessage());
             throw new DgException(e);
        }

    }
     public static Collection<Long> populateWithDescendants(Collection<Long> locations) {
        Session session = null;
        Collection<Long> destinationCollection = new ArrayList<Long>();

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select loc.id from "
                    + AmpCategoryValueLocations.class.getName()
                    + " loc where parentLocation.id in (:locationids)";
            Query q = session.createQuery(queryString);
            q.setParameterList("locationids", locations);
            destinationCollection = q.list();
            if (destinationCollection != null && destinationCollection.size() > 0) {
                locations.addAll(populateWithDescendants(destinationCollection));
            }
        } catch (Exception ex) {
            logger.error("Exception from  populateWithDescendants: ", ex);
        }
        return locations;
    }
    public static AmpCategoryValueLocations getTopAncestor(AmpCategoryValueLocations loc, String beforeImpLoc) {
        AmpCategoryValueLocations parent = loc.getParentLocation();
        if (beforeImpLoc!=null) {
            if (parent.getParentCategoryValue().getValue().equals(beforeImpLoc)) {
                return loc;
            }
        }
        if (parent == null) {
            return loc;
        } else {
            return getTopAncestor(parent, beforeImpLoc);
        }
    }
    public static class HelperLocationAncestorLocationNamesAsc implements Comparator<Location> {

        Locale locale;
        Collator collator;

        public HelperLocationAncestorLocationNamesAsc(){
            this.locale=new Locale("en", "EN");
        }

        public HelperLocationAncestorLocationNamesAsc(String iso) {
            this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
        }

        public int compare(Location loc1, Location loc2) {
            collator = Collator.getInstance(locale);
            collator.setStrength(Collator.TERTIARY);
            List<String> loca1AncestorLocationNames=loc1.getAncestorLocationNames();
            List<String> loca2AncestorLocationNames=loc2.getAncestorLocationNames();
            if(loca1AncestorLocationNames==null||loca1AncestorLocationNames.isEmpty()){
                return -1;
            }
            if(loca2AncestorLocationNames==null||loca2AncestorLocationNames.isEmpty()){
                 return 1;
            }
            StringBuilder location1FullName=new StringBuilder();
            for(String name:loca1AncestorLocationNames){
                location1FullName.append(name);
            }
            StringBuilder location2FullName=new StringBuilder();
            for(String name:loca2AncestorLocationNames){
                location2FullName.append(name);
            }
            return collator.compare(location1FullName.toString(), location2FullName.toString());

        }
    }
}

package org.digijava.module.aim.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.dgfoundation.amp.Util;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.exception.DynLocationStructuralException;
import org.digijava.module.aim.exception.DynLocationStructureStringException;
import org.digijava.module.aim.form.DynLocationManagerForm;
import org.digijava.module.aim.form.DynLocationManagerForm.Option;
import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryConstants.HardCodedCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DynLocationManagerUtil {
	private static Logger logger = Logger
			.getLogger(DynLocationManagerUtil.class);
	
	public static List<AmpCategoryValueLocations> regionsOfDefaultCountry = Collections.synchronizedList(
	 			new ArrayList<AmpCategoryValueLocations>()   ); 

	
	public static void clearRegionsOfDefaultCountryCache() {
		synchronized (DynLocationManagerUtil.regionsOfDefaultCountry) {
			DynLocationManagerUtil.regionsOfDefaultCountry.clear();
		}
	}
	
	public static Collection<AmpCategoryValueLocations> getHighestLayerLocations(
			AmpCategoryClass implLoc, DynLocationManagerForm myForm,
			ActionMessages errors) {
		Collection<AmpCategoryValueLocations> locations = null;
		Session dbSession = null;
		Collection<AmpCategoryValueLocations> rootLocations = null;
		HashSet<AmpCategoryValueLocations> badLayerLocations = new HashSet<AmpCategoryValueLocations>();

		myForm.setUnorganizedLocations(badLayerLocations);

		try {
			dbSession = PersistenceManager.getSession();
			String queryString = "select loc from "
					+ AmpCategoryValueLocations.class.getName() + " loc";
			Query qry = dbSession.createQuery(queryString);
			locations = qry.list();
			if (locations != null && locations.size() > 0) {
				rootLocations = findRootLocations(locations);
				checkTree(rootLocations, badLayerLocations);
				if (badLayerLocations.size() > 0) {
					String errorListStr = collectionToString(badLayerLocations);
					errors.add("title", new ActionMessage(
							"error.aim.dynRegionManager.badLayerProblem",
							errorListStr));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				PersistenceManager.releaseSession(dbSession);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed :" + ex2);
			}
		}

		return rootLocations;
	}

	
	public static List<AmpCategoryValueLocations> loadLocations(Set<Long> allSelectedLocations)
	{
		Session dbSession = PersistenceManager.getSession();
		
		String queryString = "select loc from "
				+ AmpCategoryValueLocations.class.getName() + " loc WHERE loc.id IN (" + Util.toCSStringForIN(allSelectedLocations) + ")";
		Query qry = dbSession.createQuery(queryString);
		List<AmpCategoryValueLocations> locations = qry.list();
		return locations;
	}
	
	
	public static void deleteLocation(Long id, ActionMessages errors) {
		Session dbSession = null;
		Transaction tx = null;
		try {
			dbSession = PersistenceManager.getSession();
//beginTransaction();

			AmpCategoryValueLocations loc = (AmpCategoryValueLocations) dbSession
					.load(AmpCategoryValueLocations.class, id);
			
			if ( loc.getParentLocation() != null )
				loc.getParentLocation().getChildLocations().remove(loc);
//			String queryString = "delete from " + AmpLocation.class.getName()
//					+ " a where a.location=" + loc.getId();
//			Query qry = dbSession.createQuery(queryString);
//			qry.executeUpdate();

			if (loc != null)
				dbSession.delete(loc);
//session.flush();
			//tx.commit();
		} catch (Exception e) {
			//tx.rollback();
			if (errors != null)
				errors.add("title", new ActionMessage(
						"error.aim.dynRegionManager.locationIsInUse"));
			e.printStackTrace();
		} finally {
			try {
				PersistenceManager.releaseSession(dbSession);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed :" + ex2);
			}
		}
	}

	public static void saveStructure(String treeStructure,
			String unorganizedLocations, AmpCategoryClass implLoc,
			ActionMessages errors) {
		if (treeStructure.length() < 4)
			return;
		Collection<AmpCategoryValueLocations> locations = null;
		Session dbSession = null;
		HashMap<Long, AmpCategoryValueLocations> locationsMap = new HashMap<Long, AmpCategoryValueLocations>();
		Transaction tx = null;
		try {
			dbSession = PersistenceManager.getSession();
//beginTransaction();
			String queryString = "select loc from "
					+ AmpCategoryValueLocations.class.getName() + " loc ";
			Query qry = dbSession.createQuery(queryString);
			locations = qry.list();

			Iterator<AmpCategoryValueLocations> iter = locations.iterator();
			while (iter.hasNext()) {
				AmpCategoryValueLocations loc = iter.next();
				locationsMap.put(loc.getId(), loc);
			}

			NodeInfo nodeInfo = new NodeInfo(treeStructure);

			while (nodeInfo.hasNext()) {
				nodeInfo.nextInfo();
				Long id = nodeInfo.getId();
				Long parentId = nodeInfo.getParentId();
				Integer layerIndex = nodeInfo.getLayerIndex();

				AmpCategoryValueLocations loc = locationsMap.get(id);
				AmpCategoryValue layer = implLoc.getPossibleValues().get(
						layerIndex);
				AmpCategoryValueLocations parent = locationsMap.get(parentId);

				loc.setParentCategoryValue(layer);
				loc.setParentLocation(parent);
				if (parent != null) {
					if (parent.getChildLocations() == null)
						parent
								.setChildLocations(new TreeSet<AmpCategoryValueLocations>(
										alphabeticalLocComp));
					parent.getChildLocations().add(loc);
				}

			}

			TreeSet<AmpCategoryValueLocations> returnLocations = new TreeSet<AmpCategoryValueLocations>(
					alphabeticalLocComp);
			HashSet<AmpCategoryValueLocations> badLayerLocations = new HashSet<AmpCategoryValueLocations>();

			NodeInfo unorgInfo = new NodeInfo(unorganizedLocations);
			while (unorgInfo.hasNext()) {
				unorgInfo.nextInfo();
				Long id = unorgInfo.getId();
				AmpCategoryValueLocations loc = locationsMap.get(id);
				locations.remove(loc);
			}

			Collection<AmpCategoryValueLocations> rootLocations = findRootLocations(locations);
			checkTree(rootLocations, badLayerLocations);
			if (badLayerLocations.size() > 0) {
				String errorListStr = collectionToString(badLayerLocations);
				if (errors != null)
					errors.add("title", new ActionMessage(
							"error.aim.dynRegionManager.badLayerProblem",
							errorListStr));
				throw new DynLocationStructuralException(
						"Some locations seem to have the wrong Implementation Location category associated: "
								+ errorListStr);
			}
			//tx.commit();
		} catch (Exception e) {
			tx.rollback();
			if (errors != null)
				errors.add("title", new ActionMessage(
						"error.aim.dynRegionManager.treeSavingProblem"));
			e.printStackTrace();
		} finally {
			try {
				PersistenceManager.releaseSession(dbSession);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed :" + ex2);
			}
		}
	}

	private static Collection<AmpCategoryValueLocations> findRootLocations(
			Collection<AmpCategoryValueLocations> allLocations) {
		TreeSet<AmpCategoryValueLocations> returnLocations = new TreeSet<AmpCategoryValueLocations>(
				alphabeticalLocComp);
		Iterator<AmpCategoryValueLocations> iterator = allLocations.iterator();
		while (iterator.hasNext()) {
			AmpCategoryValueLocations loc = iterator.next();
			if (loc.getParentLocation() == null)
				returnLocations.add(loc);
		}
		return returnLocations;
	}

	private static void checkSiblings(
			Collection<AmpCategoryValueLocations> siblingLocations,
			Collection<AmpCategoryValueLocations> badLayerLocations,
			Integer parentLayerIndex) {

		Iterator<AmpCategoryValueLocations> iter = siblingLocations.iterator();
		while (iter.hasNext()) {
			AmpCategoryValueLocations loc = iter.next();
			if (loc.getParentCategoryValue().getIndex() <= parentLayerIndex) {
				loc.getParentLocation().getChildLocations().remove(loc);
				loc.setParentLocation(null);
				badLayerLocations.add(loc);
			} else if (loc.getChildLocations() != null
					&& loc.getChildLocations().size() > 0) {
				checkSiblings(loc.getChildLocations(), badLayerLocations, loc
						.getParentCategoryValue().getIndex());
			}
		}
	}

	public static void checkTree(
			Collection<AmpCategoryValueLocations> rootLocations,
			Collection<AmpCategoryValueLocations> badLayerLocations) {

		badLayerLocations.clear();

		Iterator<AmpCategoryValueLocations> locIter = rootLocations.iterator();
		while (locIter.hasNext()) {
			AmpCategoryValueLocations tempLoc = locIter.next();
			if (tempLoc.getChildLocations() != null
					&& tempLoc.getChildLocations().size() > 0) {
				checkSiblings(tempLoc.getChildLocations(), badLayerLocations,
						tempLoc.getParentCategoryValue().getIndex());
			}
		}

	}

	public static String collectionToString(Collection<? extends Object> col)
			throws NullPointerException {
		if (col == null)
			throw new NullPointerException("col param cannot be null");
		if (col.size() == 0)
			return "";

		String retString = "";
		Iterator<? extends Object> iter = col.iterator();
		while (true) {
			retString += iter.next();
			if (iter.hasNext())
				retString += ", ";
			else
				break;
		}

		return retString;
	}

	public static void synchronizeCountries() {
		logger.info("Starting countries synchronization");
		Session dbSession = null;
		Transaction tx = null;
		Collection<Country> countries = null;
		try {
			dbSession = PersistenceManager.getSession();
//beginTransaction();
			String queryString = "select c from " + Country.class.getName()
					+ " c ";
			Query qry = dbSession.createQuery(queryString);
			countries = qry.list();

			if (countries == null)
				return;

			Set<AmpCategoryValueLocations> countryLocations = DynLocationManagerUtil
					.getLocationsByLayer(CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY);
			HashMap<String, AmpCategoryValueLocations> nameToLocationsMap = new HashMap<String, AmpCategoryValueLocations>();
			HashMap<String, AmpCategoryValueLocations> iso3ToLocationsMap = new HashMap<String, AmpCategoryValueLocations>();

			if (countryLocations != null && countryLocations.size() > 0) {
				for (AmpCategoryValueLocations locCountry : countryLocations) {
					nameToLocationsMap.put(locCountry.getName(), locCountry);
					if (locCountry.getIso3() != null
							&& locCountry.getIso3().length() > 0) {
						iso3ToLocationsMap
								.put(locCountry.getIso3(), locCountry);
					}
				}
			}

			AmpCategoryValue layer = CategoryManagerUtil
					.getAmpCategoryValueFromDB(CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY);
			if (layer == null) {
				logger
						.error("No Country value found in category Implementation Location. Please correct this.");
				throw new Exception(
						"No Country value found in category Implementation Location. Please correct this.");
			}

			for (Country country : countries) {
				if (country.getIso3() != null && country.getIso3().length() > 0
						&& iso3ToLocationsMap.get(country.getIso3()) != null) {
					continue;
				}
				if (nameToLocationsMap.get(country.getCountryName()) == null) {
					AmpCategoryValueLocations locCountry = new AmpCategoryValueLocations();
					locCountry.setParentCategoryValue(layer);
					locCountry.setName(country.getCountryName());
					locCountry.setIso3(country.getIso3());
					locCountry.setIso(country.getIso());
					if (country.getCountryId() != null)
						locCountry.setCode(country.getCountryId().toString());
					dbSession.save(locCountry);
				}
			}

			Long maxCountryCode = 0L;
			HashMap<String, Country> namesToDgCountriesMap = new HashMap<String, Country>();
			HashMap<String, Country> iso3ToDgCountriesMap = new HashMap<String, Country>();
			for (Country country : countries) {
				namesToDgCountriesMap.put(country.getCountryName(), country);
				if (country.getIso3() != null && country.getIso3().length() > 0) {
					iso3ToDgCountriesMap.put(country.getIso3(), country);
				}
				if (country.getCountryId() != null
						&& country.getCountryId() > maxCountryCode) {
					maxCountryCode = country.getCountryId();
				}
			}

			if (countryLocations != null) {
				for (AmpCategoryValueLocations location : countryLocations) {
					if (namesToDgCountriesMap.get(location.getName()) == null) {
						Country country = null;
						if (location.getIso3() != null
								&& location.getIso3().length() > 0)
							country = iso3ToDgCountriesMap.get(location
									.getIso3());
						if (country == null)
							country = new Country();
						country.setCountryName(location.getName());
						country.setIso(location.getIso());
						country.setIso3(location.getIso3());
						country.setAvailable(true);
						country.setDecCtryFlag("t");
						country.setStat("t");
						country.setShowCtry("t");
						if (location.getCode() != null) {
							try {
								country.setCountryId(Long.parseLong(location
										.getCode()));
							} catch (NumberFormatException e) {
								logger
										.info("Cannot transform location country code ('"
												+ location.getCode()
												+ "') to dg Country code. Settting country id as maximum.");
								country.setCountryId(maxCountryCode + 1);
							}
						} else {
							country.setCountryId(maxCountryCode + 1);
						}
						dbSession.saveOrUpdate(country);
					}
				}
			}
			//tx.commit();
			logger.info("Countries synchronization done.");
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
			logger.info("Countries synchronization NOT performed.");
		} finally {
			try {
				PersistenceManager.releaseSession(dbSession);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed :" + ex2);
			}
		}
	}

	@Deprecated
	public static AmpCategoryValueLocations getLocationByName(
			String locationName, HardCodedCategoryValue hcLocationLayer) {
		try {
			AmpCategoryValue layer = CategoryManagerUtil
					.getAmpCategoryValueFromDB(hcLocationLayer);
			return getLocationByName(locationName, layer);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @param locationName
	 * @param cvLocationLayer
	 *            the AmpCategoryValue specifying the layer (level) of the
	 *            location...like Country or Region
	 * @return
	 */
	@Deprecated
	public static AmpCategoryValueLocations getLocationByName(
			String locationName, AmpCategoryValue cvLocationLayer) {
		Session dbSession = null;

		try {
			dbSession = PersistenceManager.getSession();
			String queryString = "select loc from "
					+ AmpCategoryValueLocations.class.getName()
					+ " loc where (loc.name=:name)";
			if (cvLocationLayer != null) {
				queryString += " AND (loc.parentCategoryValue=:cvId) ";
			}
			Query qry = dbSession.createQuery(queryString);
			if (cvLocationLayer != null) {
				qry.setLong("cvId", cvLocationLayer.getId());
			}
			qry.setString("name", locationName);
			AmpCategoryValueLocations loc = (AmpCategoryValueLocations) qry
					.uniqueResult();
			return loc;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				PersistenceManager.releaseSession(dbSession);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed :" + ex2);
			}
		}
		return null;
	}

	public static AmpCategoryValueLocations getLocationByIso3(
			String locationIso3, HardCodedCategoryValue hcLocationLayer) {
		try {
			AmpCategoryValue layer = CategoryManagerUtil
					.getAmpCategoryValueFromDB(hcLocationLayer);
			return getLocationByIso3(locationIso3, layer);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @param locationIso
	 * @param cvLocationLayer
	 *            the AmpCategoryValue specifying the layer (level) of the
	 *            location...like Country or Region
	 * @return
	 */
	public static AmpCategoryValueLocations getLocationByIso3(
			String locationIso3, AmpCategoryValue cvLocationLayer) {
		Session dbSession = null;

		try {
			dbSession = PersistenceManager.getSession();
			String queryString = "select loc from "
					+ AmpCategoryValueLocations.class.getName()
					+ " loc where (loc.iso3=:iso3)";
			if (cvLocationLayer != null) {
				queryString += " AND (loc.parentCategoryValue=:cvId) ";
			}
			Query qry = dbSession.createQuery(queryString);
			if (cvLocationLayer != null) {
				qry.setLong("cvId", cvLocationLayer.getId());
			}
			qry.setString("iso3", locationIso3);
			AmpCategoryValueLocations loc = (AmpCategoryValueLocations) qry
					.uniqueResult();
			return loc;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				PersistenceManager.releaseSession(dbSession);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed :" + ex2);
			}
		}
		return null;
	}

	public static AmpCategoryValueLocations getLocationByName(
			String locationName, HardCodedCategoryValue hcLocationLayer,
			AmpCategoryValueLocations parentLocation)
			throws NullPointerException {
		try {
			AmpCategoryValue layer = CategoryManagerUtil
					.getAmpCategoryValueFromDB(hcLocationLayer);
			return getLocationByName(locationName, layer, parentLocation);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static AmpCategoryValueLocations getLocationByName(
			String locationName, AmpCategoryValue cvLocationLayer,
			AmpCategoryValueLocations parentLocation)
			throws NullPointerException, NonUniqueResultException {
		Session dbSession = null;

		if (cvLocationLayer == null)
			throw new NullPointerException(
					"Value for Implementation Location cannot be null");
		try {
			dbSession = PersistenceManager.getSession();
			String queryString = "select loc from "
					+ AmpCategoryValueLocations.class.getName()
					+ " loc where (loc.name=:name) "
					+ " AND (loc.parentCategoryValue=:cvId) ";
			if (parentLocation == null) {
				queryString += "AND (loc.parentLocation is null) ";
			} else {
				queryString += "AND (loc.parentLocation=:parentLocationId) ";
			}
			Query qry = dbSession.createQuery(queryString);
			qry.setLong("cvId", cvLocationLayer.getId());
			qry.setString("name", locationName);
			if (parentLocation != null)
				qry.setLong("parentLocationId", parentLocation.getId());

			Collection<AmpCategoryValueLocations> locations = qry.list();
			if (locations != null && locations.size() > 0) {
				return locations.toArray(new AmpCategoryValueLocations[0])[0];
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				PersistenceManager.releaseSession(dbSession);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed :" + ex2);
			}
		}
		return null;
	}

	public static AmpCategoryValueLocations getLocationByIso(
			String locationIso, HardCodedCategoryValue hcLocationLayer) {
		try {
			AmpCategoryValue layer = CategoryManagerUtil
					.getAmpCategoryValueFromDB(hcLocationLayer);
			return getLocationByIso(locationIso, layer);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @param locationIso
	 * @param cvLocationLayer
	 *            the AmpCategoryValue specifying the layer (level) of the
	 *            location...like Country or Region
	 * @return
	 */
	public static AmpCategoryValueLocations getLocationByIso(
			String locationIso, AmpCategoryValue cvLocationLayer) {
		Session dbSession = null;

		try {
			dbSession = PersistenceManager.getSession();
			String queryString = "select loc from "
					+ AmpCategoryValueLocations.class.getName()
					+ " loc where (loc.iso=:iso)";
			if (cvLocationLayer != null) {
				queryString += " AND (loc.parentCategoryValue=:cvId) ";
			}
			Query qry = dbSession.createQuery(queryString);
			qry.setCacheable(true);
			if (cvLocationLayer != null) {
				qry.setLong("cvId", cvLocationLayer.getId());
			}
			qry.setString("iso", locationIso);
			AmpCategoryValueLocations loc = (AmpCategoryValueLocations) qry
					.uniqueResult();
			return loc;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				PersistenceManager.releaseSession(dbSession);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed :" + ex2);
			}
		}
		return null;
	}

	public static AmpCategoryValueLocations getLocationOpenedSession(Long id) {
 	 	Session dbSession = null;
 	 	try {
 	 		dbSession = PersistenceManager.getRequestDBSession();
 	 		String queryString = "select loc from "
 	 		+ AmpCategoryValueLocations.class.getName()
 	 		+ " loc where (loc.id=:id)" ;                   
 	 		Query qry = dbSession.createQuery(queryString);
 	 		qry.setLong("id", id);
 	 		AmpCategoryValueLocations returnLoc = (AmpCategoryValueLocations)qry.uniqueResult();
 	 		return returnLoc;
 	 	} catch (Exception e) {
 	 		e.printStackTrace();
 	 	}
 	 	return null;
 	}
	
	public static AmpCategoryValueLocations getLocationByCode(
			String locationCode, HardCodedCategoryValue hcLocationLayer) {
		try {
			AmpCategoryValue layer = CategoryManagerUtil
					.getAmpCategoryValueFromDB(hcLocationLayer);
			return getLocationByCode(locationCode, layer);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @param locationIso
	 * @param cvLocationLayer
	 *            the AmpCategoryValue specifying the layer (level) of the
	 *            location...like Country or Region
	 * @return
	 */
	public static AmpCategoryValueLocations getLocationByCode(
			String locationCode, AmpCategoryValue cvLocationLayer) {
		Session dbSession = null;

		try {
			dbSession = PersistenceManager.getSession();
			String queryString = "select loc from "
					+ AmpCategoryValueLocations.class.getName()
					+ " loc where (loc.code=:code)";
			if (cvLocationLayer != null) {
				queryString += " AND (loc.parentCategoryValue=:cvId) ";
			}
			Query qry = dbSession.createQuery(queryString);
			if (cvLocationLayer != null) {
				qry.setLong("cvId", cvLocationLayer.getId());
			}
			qry.setString("code", locationCode);
			AmpCategoryValueLocations loc = (AmpCategoryValueLocations) qry
					.uniqueResult();
			return loc;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				PersistenceManager.releaseSession(dbSession);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed :" + ex2);
			}
		}
		return null;
	}

	/**
	 * 
	 * @param id
	 * @param initChildLocs
	 *            child locations are lazily initialized, so if you want to
	 *            access them put "true" here
	 * @return
	 */
	public static AmpCategoryValueLocations getLocation(Long id,
			boolean initChildLocs) {
		Session dbSession = null;
		try {
			dbSession = PersistenceManager.getSession();
			String queryString = "select loc from "
					+ AmpCategoryValueLocations.class.getName()
					+ " loc where (loc.id=:id)";
			Query qry = dbSession.createQuery(queryString);
			qry.setLong("id", id);
			AmpCategoryValueLocations returnLoc = (AmpCategoryValueLocations) qry
					.uniqueResult();
			if (initChildLocs)
				returnLoc.getChildLocations().size();
			return returnLoc;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				PersistenceManager.releaseSession(dbSession);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed :" + ex2);
			}
		}
		return null;
	}

	public static AmpCategoryValueLocations getLocationByIdRequestSession(Long id) {
		Session dbSession                                                                               = null;
		try {
			dbSession							= PersistenceManager.getRequestDBSession();

			AmpCategoryValueLocations returnLoc	= (AmpCategoryValueLocations) dbSession.get(AmpCategoryValueLocations.class, id);
			return returnLoc;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * returns set of all (recursive) descendants' ids of a given set of locations
	 * @param locations
	 * @return
	 */
	public static Set<Long> populateWithDescendantsIds(Collection<AmpCategoryValueLocations> locations)
	{
		Set<Long> allInputLocations = new HashSet<Long>();
		
		for(AmpCategoryValueLocations acvl:locations)
			allInputLocations.add(acvl.getId());
		
		Set<Long> allOutputLocations = getRecursiveChildrenOfCategoryValueLocations(allInputLocations);
		return allOutputLocations;
	}
	
	/**
	 * returns set of all (recursive) descendants of a given set of locations
	 * @param destCollection
	 * @param locations
	 */
	public static void populateWithDescendants(Set <AmpCategoryValueLocations> destCollection, 
			Collection<AmpCategoryValueLocations> locations ) {
		
		Set<Long> allOutputLocations = populateWithDescendantsIds(locations);
		for(Long outputId:allOutputLocations)
			destCollection.add(getLocation(outputId, false));
	}
	
	/**
	 * recursively get all children of a set of AmpCategoryValueLocations, by a wave algorithm
	 * @param inIds
	 * @return
	 */
	public static Set<Long> getRecursiveChildrenOfCategoryValueLocations(Collection<Long> inIds)
	{
		Set<Long> result = new HashSet<Long>();
		if (inIds == null)
			return result;
		Set<Long> currentWave = new HashSet<Long>();currentWave.addAll(inIds);
		while (currentWave.size() > 0)
		{
			result.addAll(currentWave);
			currentWave = getChildrenOfCategoryValueLocations(currentWave);
			currentWave.removeAll(result); // in case there is a cycle somewhere in the DB, do not cycle forever
		}
		return result;
	}
	
	/*
	 * returns the list of all the children of all the AmpCategoryValueLocations given by ids
	 * NON-RECURSIVE
	 */
	private static Set<Long> getChildrenOfCategoryValueLocations(Collection<Long> inIds)
	{
		Set<Long> result = new HashSet<Long>();
		if (inIds == null)
			return result;
		Connection conn = null;
		try
		{
			conn = PersistenceManager.getJdbcConnection();
			String query = "SELECT DISTINCT id FROM amp_category_value_location WHERE parent_location IN (" + Util.toCSStringForIN(inIds) + ")";
			ResultSet rs = conn.createStatement().executeQuery(query);
			while (rs.next())
				result.add(rs.getLong(1));
			rs.close();
			return result;
		}
		catch(SQLException e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			try {conn.close();}
			catch(Exception e){};
		}
	}
	
	public static void populateWithAscendants(Collection <AmpCategoryValueLocations> destCollection, 
			Collection<AmpCategoryValueLocations> locations ) {
		if (  locations != null ) {
			Iterator<AmpCategoryValueLocations> iterLoc	= locations.iterator();
			while (iterLoc.hasNext()) {
				AmpCategoryValueLocations loc	 = iterLoc.next();
				
				while (loc.getParentLocation() != null){
					loc		= loc.getParentLocation();
					destCollection.add(loc);
				}
			}
		}
		
	}
	
	public static Collection<AmpCategoryValueLocations> getRegionsOfDefCountryHierarchy() throws DgException  {
		
		List<AmpCategoryValueLocations> ret	= null;
		synchronized (DynLocationManagerUtil.regionsOfDefaultCountry) {
			if ( DynLocationManagerUtil.regionsOfDefaultCountry.size() == 0 ) {
		 	 	AmpCategoryValueLocations country = DynLocationManagerUtil.getLocationByIso( 
		 	 	FeaturesUtil.getDefaultCountryIso(), CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY );
		 	 	if (country != null){
			 	 	country = getLocationOpenedSession( country.getId() );
			 	 	
			 	 	Set<AmpCategoryValueLocations> children				= country.getChildLocations();
			 	 	
			 	 	DynLocationManagerUtil.regionsOfDefaultCountry.addAll(children);
		 	 	}
			}
	 	 	
 	 		ret	= new ArrayList<AmpCategoryValueLocations>( DynLocationManagerUtil.regionsOfDefaultCountry.size() );
 	 		ret.addAll(DynLocationManagerUtil.regionsOfDefaultCountry);
		}
 	 	return ret;
 	 }
	
	public static Set<AmpCategoryValueLocations> getLocationsOfTypeRegionOfDefCountry()
			throws Exception {
		TreeSet<AmpCategoryValueLocations> returnSet = new TreeSet<AmpCategoryValueLocations>(
				alphabeticalLocComp);
		String defCountryIso = FeaturesUtil.getDefaultCountryIso();
		if (defCountryIso != null) {
			Set<AmpCategoryValueLocations> allRegions = getLocationsByLayer(CategoryConstants.IMPLEMENTATION_LOCATION_REGION);
			if (allRegions != null && allRegions.size() > 0) {
				Iterator<AmpCategoryValueLocations> regIter = allRegions
						.iterator();
				while (regIter.hasNext()) {
					AmpCategoryValueLocations reg = regIter.next();
					AmpCategoryValueLocations country = getAncestorByLayer(reg,
							CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY);
					if (defCountryIso.equals(country.getIso())) {
						returnSet.add(reg);
					}
				}
			}
		} else
			throw new Exception("No default country iso could be retrieved!");
		return returnSet;
	}

	public static Set<AmpCategoryValueLocations> getLocationsOfTypeRegion() {
		return getLocationsByLayer(CategoryConstants.IMPLEMENTATION_LOCATION_REGION);
	}

	public static Set<AmpCategoryValueLocations> getLocationsByLayer(
			HardCodedCategoryValue hcLayer) {
		try {
			AmpCategoryValue layer = CategoryManagerUtil
					.getAmpCategoryValueFromDB(hcLayer);
			return getLocationsByLayer(layer);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static AmpCategoryValueLocations getAncestorByLayer(
			AmpCategoryValueLocations loc, HardCodedCategoryValue hcValue) {
		if (loc == null)
			logger
					.error("loc parameter in getAncestorByLayer should not be null");
		AmpCategoryValueLocations temp = loc;
		if (temp.getParentCategoryValue().getValue().equals(
				hcValue.getValueKey()))
			return temp;
		while (temp.getParentLocation() != null) {
			temp = temp.getParentLocation();
			if (temp.getParentCategoryValue().getValue().equals(
					hcValue.getValueKey()))
				return temp;
		}
		return null;
	}

	/**
	 * 
	 * @param cvLayer
	 * @return
	 */
	public static Set<AmpCategoryValueLocations> getLocationsByLayer(
			AmpCategoryValue cvLayer) {
		TreeSet<AmpCategoryValueLocations> returnSet = new TreeSet<AmpCategoryValueLocations>(
				alphabeticalLocComp);
		Session dbSession = null;
		try {
			dbSession = PersistenceManager.getSession();
			String queryString = "select loc from "
					+ AmpCategoryValueLocations.class.getName()
					+ " loc where (loc.parentCategoryValue=:cvId) ";
			Query qry = dbSession.createQuery(queryString);
			qry.setLong("cvId", cvLayer.getId());
			returnSet.addAll(qry.list());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				PersistenceManager.releaseSession(dbSession);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed :" + ex2);
			}
		}
		if (returnSet.size() > 0)
			return returnSet;
		return null;
	}

	public static List<String> getParents(AmpCategoryValueLocations loc) {
		ArrayList<String> returnList = new ArrayList<String>();
		if (loc == null)
			return returnList;
		else
			returnList.add(loc.getName());
		AmpCategoryValueLocations temp = loc;
		while (temp.getParentLocation() != null) {
			temp = temp.getParentLocation();
			returnList.add(0, temp.getName());
		}
		return returnList;
	}

	public static Map<Integer, String> getLayerToAncestorNameMap(
			AmpCategoryValueLocations loc) {
		TreeMap<Integer, String> returnList = new TreeMap<Integer, String>();
		if (loc == null)
			return returnList;
		else
			returnList.put(loc.getParentCategoryValue().getIndex(), loc
					.getName());
		AmpCategoryValueLocations temp = loc;
		while (temp.getParentLocation() != null) {
			temp = temp.getParentLocation();
			returnList.put(temp.getParentCategoryValue().getIndex(), temp
					.getName());
		}
		return returnList;
	}

	/**
	 * 
	 * @param ampCVLocation
	 * @return If there is a corresponding AmpLocation object in the database
	 *         then it is returned. Otherwise a new entity is being created and
	 *         saved to the db.
	 * @throws Exception
	 */
	public static AmpLocation getAmpLocation(
			AmpCategoryValueLocations ampCVLocation) throws Exception {
		if (ampCVLocation == null)
			throw new Exception("ampCVLocations is null");

		AmpLocation ampLoc = LocationUtil
				.getAmpLocationByCVLocation(ampCVLocation.getId());

		if (ampLoc == null) {
			ampLoc = new AmpLocation();
			ampLoc.setDescription(new String(" "));

			ampLoc.setLocation(ampCVLocation);
			AmpCategoryValueLocations regionLocation = DynLocationManagerUtil
					.getAncestorByLayer(ampCVLocation,
							CategoryConstants.IMPLEMENTATION_LOCATION_REGION);
			if (regionLocation != null) {
				ampLoc.setRegionLocation(regionLocation);
				ampLoc.setRegion(regionLocation.getName());
			}
			DbUtil.add(ampLoc);
		}
		return ampLoc;
	}

	/**
	 * 
	 * @param indexOfLayer
	 * @return Number of locations of the layer specified by indexOfLayer
	 */
	public static int getNumOfLocations(int indexOfLayer) {
		Session dbSession = null;
		try {
			AmpCategoryValue cvLocationType = CategoryManagerUtil
					.getAmpCategoryValueFromDb(
							CategoryConstants.IMPLEMENTATION_LOCATION_KEY,
							(long) indexOfLayer);
			if (cvLocationType != null) {
				dbSession = PersistenceManager.getSession();
				String queryString = "select count(loc) from "
						+ AmpCategoryValueLocations.class.getName()
						+ " loc where (loc.parentCategoryValue=:cvId)";
				Query qry = dbSession.createQuery(queryString);
				qry.setCacheable(true);
				qry.setLong("cvId", cvLocationType.getId());
				int returnValue = (Integer) qry.uniqueResult();
				return returnValue;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				PersistenceManager.releaseSession(dbSession);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed :" + ex2);
			}
		}
		return 0;
	}

	public static Comparator<AmpCategoryValueLocations> alphabeticalLocComp = new Comparator<AmpCategoryValueLocations>() {
		public int compare(AmpCategoryValueLocations o1,
				AmpCategoryValueLocations o2) {
			return o1.getName().compareTo(o2.getName());
		}
	};

	public static ErrorCode importExcelFile(InputStream inputStream,
			Option option) throws AimException {
		POIFSFileSystem fsFileSystem = null;
		try {
			fsFileSystem = new POIFSFileSystem(inputStream);
			HSSFWorkbook workBook = new HSSFWorkbook(fsFileSystem);
			HSSFSheet hssfSheet = workBook.getSheetAt(0);
			Row hssfRow = hssfSheet.getRow(0);
			int physicalNumberOfCells = hssfRow.getPhysicalNumberOfCells();
			List<AmpCategoryValue> implLocs = new ArrayList<AmpCategoryValue>(
					CategoryManagerUtil
							.getAmpCategoryValueCollectionByKey(CategoryConstants.IMPLEMENTATION_LOCATION_KEY));
			int i = 1;
			int hierarchyNumberOfCells=implLocs.size();
			// last five cells are not hierarchy cells and first cell is db id
			if (hierarchyNumberOfCells+6 != physicalNumberOfCells) {
				return ErrorCode.NUMBER_NOT_MATCH;
			}
			for (AmpCategoryValue location : implLocs) {
				if (!hssfRow.getCell(i).getStringCellValue()
						.equals(location.getValue())) {
					return ErrorCode.NAME_NOT_MATCH;
				}
				i++;
			}
			i--;
			
			for (int j = 1; j < hssfSheet.getPhysicalNumberOfRows(); j++) {
				AmpCategoryValueLocations parentLoc=null;
				hssfRow = hssfSheet.getRow(j);
				Cell cell =hssfRow.getCell(0);
				Long databaseId=null;
				if(cell!=null){
					switch(cell.getCellType()){
						case Cell.CELL_TYPE_STRING:databaseId=(cell.getStringCellValue()==null||(cell.getStringCellValue()!=null&&cell.getStringCellValue().trim().equals("")))?null:Long.parseLong(cell.getStringCellValue());break;
						case Cell.CELL_TYPE_NUMERIC:databaseId=(long) cell.getNumericCellValue();break;
					}
				}
				List<String> locationNames = new ArrayList<String>();
				int k = 1;
				
				for (; k <=hierarchyNumberOfCells; k++) {
					cell = hssfRow.getCell(k);
					if (cell == null) {
						k=hierarchyNumberOfCells+1;
						break;
					}
					String location = getValue(cell);
					if(location==null||location.trim().length()==0){
						k=hierarchyNumberOfCells+1;
						break;
					}
					locationNames.add(location);
				}
				cell=hssfRow.getCell(k++);
				String lalitude=getValue(cell);
				cell=hssfRow.getCell(k++);
				String longitude=getValue(cell);
				cell=hssfRow.getCell(k++);
				String geoID=getValue(cell);
				if (geoID!=null && geoID.contains(".0")){
					geoID = geoID.replace(".0", "");
				}
				cell=hssfRow.getCell(k++);
				String iso=getValue(cell);
				cell=hssfRow.getCell(k++);
				String iso3=getValue(cell);
					for (k = 0; k < locationNames.size(); k++) {
						String name = locationNames.get(k);
						AmpCategoryValue implLoc = implLocs.get(k);
						if (k == locationNames.size() - 1) {
							AmpCategoryValueLocations location=new AmpCategoryValueLocations(); ;
							AmpCategoryValueLocations currentLoc = getLocationByName(
									name, implLoc, parentLoc);
							if (currentLoc != null) {
								if (option.equals(Option.NEW)) {
									break;
								}
								else{
									location=currentLoc;
								}
							}
							else{
								if (option.equals(Option.OVERWRITE)) {
									if(databaseId!=null&&databaseId!=0){
										location=getLocationByIdRequestSession(databaseId);
									}
									else{
										break;
									}
								}
							}
							if(location!=null){
								if(location.getParentCategoryValue()!=null&&!location.getParentCategoryValue().equals(implLoc)){
									break;
								}
								location.setName(name);
								location.setGsLat(lalitude);
								location.setGsLong(longitude);
								location.setGeoCode(geoID);
								location.setIso(iso);
								location.setIso3(iso3);
								location.setParentCategoryValue(implLoc);
								location.setParentLocation(parentLoc);
								boolean edit=(location.getId()==null)?false:true;
								LocationUtil.saveLocation(location, edit);
							}
							
						} else {
							parentLoc=getLocationByName(name, implLoc,
									parentLoc);
							if(parentLoc==null){
								return ErrorCode.INCORRECT_CONTENT;
							}	
						}
					}
			}

		} 
		catch (NullPointerException e) {
			logger.error("file is not ok");
			throw new AimException("Cannot import regions", e);
		}
		catch (IllegalStateException e) {
			logger.error("file is not ok", e);
			return ErrorCode.INCORRECT_CONTENT;
		}
		catch(IOException e){
			logger.error("file is not ok", e);
			return ErrorCode.INCORRECT_CONTENT;
		}
		catch (Exception e) {
			logger.error(e);
			throw new AimException("Cannot import regions", e);
		}
		return ErrorCode.CORRECT_CONTENT;

	}

	private static String getValue(Cell cell) {
		String value=null;
		if(cell!=null){
			switch(cell.getCellType()){
				case Cell.CELL_TYPE_STRING:value=(cell.getStringCellValue()!=null&&cell.getStringCellValue().trim().equals(""))?null:cell.getStringCellValue();break;
				case Cell.CELL_TYPE_NUMERIC: value=""+cell.getNumericCellValue();break;
			}
		}
		return value;
	}
	
	public enum ErrorCode{
		NUMBER_NOT_MATCH,NAME_NOT_MATCH,INCORRECT_CONTENT,CORRECT_CONTENT
	}

	public static class NodeInfo {
		private int i = 0;
		private String[] pairs = null;

		private Long id = null;
		private Long parentId = null;
		private Integer layerIndex = null;

		public NodeInfo(String structureStr) {
			this.pairs = structureStr.split("\\|");
		}

		public void nextInfo() throws DynLocationStructureStringException {
			if (i < pairs.length && pairs[i].length() > 0) {
				String[] ids = pairs[i++].split("p|h");
				if (ids.length != 3) {
					throw new DynLocationStructureStringException(
							"Exactly 3 tokens should be found in each pair. This pair is wrong: "
									+ pairs[i]);
				}
				this.id = Long.parseLong(ids[0]);
				this.parentId = Long.parseLong(ids[1]);
				this.layerIndex = Integer.parseInt(ids[2]);
			} else
				i++;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Long getParentId() {
			return parentId;
		}

		public void setParentId(Long parentId) {
			this.parentId = parentId;
		}

		public Integer getLayerIndex() {
			return layerIndex;
		}

		public void setLayerIndex(Integer layerIndex) {
			this.layerIndex = layerIndex;
		}

		public boolean hasNext() {
			return i < this.pairs.length;
		}

	}
}

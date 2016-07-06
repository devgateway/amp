package org.digijava.module.aim.util;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.algo.DatabaseWaver;
import org.digijava.kernel.ampapi.endpoints.indicator.IndicatorEPConstants;
import org.digijava.kernel.ampapi.endpoints.indicator.IndicatorService;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpIndicatorLayer;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpLocationIndicatorValue;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.exception.DynLocationStructuralException;
import org.digijava.module.aim.exception.DynLocationStructureStringException;
import org.digijava.module.aim.form.DynLocationManagerForm;
import org.digijava.module.aim.form.DynLocationManagerForm.Option;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryConstants.HardCodedCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Query;
import org.hibernate.Session;

import java.io.IOException;
import java.io.InputStream;
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
import java.util.TreeMap;
import java.util.TreeSet;

public class DynLocationManagerUtil {
	private static Logger logger = Logger
			.getLogger(DynLocationManagerUtil.class);
	
	public static List<AmpCategoryValueLocations> regionsOfDefaultCountry = new ArrayList<AmpCategoryValueLocations>();
	
	public static void clearRegionsOfDefaultCountryCache() {
		synchronized (DynLocationManagerUtil.regionsOfDefaultCountry) {
			DynLocationManagerUtil.regionsOfDefaultCountry.clear();
		}
	}
	
	public static Collection<AmpCategoryValueLocations> getHighestLayerLocations(DynLocationManagerForm myForm,	ActionMessages errors) {
		Collection<AmpCategoryValueLocations> locations = null;
		Session dbSession = null;
		Collection<AmpCategoryValueLocations> rootLocations = null;
		HashSet<AmpCategoryValueLocations> badLayerLocations = new HashSet<AmpCategoryValueLocations>();

		myForm.setUnorganizedLocations(badLayerLocations);

		try {
			dbSession = PersistenceManager.getSession();
			String queryString = "select loc from " + AmpCategoryValueLocations.class.getName() + " loc";
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
		}

		return rootLocations;
	}

	
	public static List<AmpCategoryValueLocations> loadLocations(Set<Long> allSelectedLocations) {
		if (allSelectedLocations == null)
			return null;
		Session dbSession = PersistenceManager.getSession();
		
		String queryString = "select loc from "
				+ AmpCategoryValueLocations.class.getName() + " loc WHERE loc.id IN (" + Util.toCSStringForIN(allSelectedLocations) + ")";
		Query qry = dbSession.createQuery(queryString);
		List<AmpCategoryValueLocations> locations = qry.list();
		return locations;
	}
	
	
	public static void deleteLocation(Long id, ActionMessages errors) {
		Session dbSession = PersistenceManager.getSession();
		try {
			AmpCategoryValueLocations loc = (AmpCategoryValueLocations) dbSession.load(AmpCategoryValueLocations.class, id);
			
			AmpLocation ampLoc = DynLocationManagerUtil.getAmpLocation(loc);
			if (ampLoc != null && ampLoc.getActivities() != null && ampLoc.getActivities().size() > 0) {
				errors.add("title", new ActionMessage("error.aim.dynRegionManager.locationIsInUse"));
			} else {
				if (loc.getParentLocation() != null)
					loc.getParentLocation().getChildLocations().remove(loc);
				
				if (ampLoc != null) {
					dbSession.delete(ampLoc);
				}
				
				dbSession.delete(loc);
			}
		} catch (Exception e) {
			errors.add("title", new ActionMessage("error.aim.dynRegionManager.locationIsInUse"));
			logger.error(e);
		}
	}

	public static void saveStructure(String treeStructure, String unorganizedLocations, List<AmpCategoryValue> levelLocations, ActionMessages errors) {
		if (treeStructure.length() < 4)
			return;
		Collection<AmpCategoryValueLocations> locations = null;
		Session dbSession = null;
		HashMap<Long, AmpCategoryValueLocations> locationsMap = new HashMap<Long, AmpCategoryValueLocations>();
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
				AmpCategoryValue layer = levelLocations.get(layerIndex);
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
			if (errors != null)
				errors.add("title", new ActionMessage(
						"error.aim.dynRegionManager.treeSavingProblem"));
			e.printStackTrace();
		}
	}

	private static Collection<AmpCategoryValueLocations> findRootLocations(Collection<AmpCategoryValueLocations> allLocations) {
		TreeSet<AmpCategoryValueLocations> returnLocations = new TreeSet<AmpCategoryValueLocations>(alphabeticalLocComp);
		
		for(AmpCategoryValueLocations loc : allLocations) {
			if (loc.getParentLocation() == null) {
				returnLocations.add(loc);
			}
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

	public static void checkTree(Collection<AmpCategoryValueLocations> rootLocations, Collection<AmpCategoryValueLocations> badLayerLocations) {

		badLayerLocations.clear();

		Iterator<AmpCategoryValueLocations> locIter = rootLocations.iterator();
		while (locIter.hasNext()) {
			AmpCategoryValueLocations tempLoc = locIter.next();
			if (tempLoc.getChildLocations() != null	&& tempLoc.getChildLocations().size() > 0) {
				checkSiblings(tempLoc.getChildLocations(), badLayerLocations, tempLoc.getParentCategoryValue().getIndex());
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

			AmpCategoryValue layer = CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY.getAmpCategoryValueFromDB();
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
			e.printStackTrace();
			logger.info("Countries synchronization NOT performed.");
		}
	}

	public static AmpCategoryValueLocations getLocationByIso3(
			String locationIso3, HardCodedCategoryValue hcLocationLayer) {
		try {
			AmpCategoryValue layer = hcLocationLayer.getAmpCategoryValueFromDB();
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
		}
		return null;
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
			String locationNameHql = AmpCategoryValueLocations.hqlStringForName("loc");
			String queryString = "select loc from "
					+ AmpCategoryValueLocations.class.getName()
					+ " loc where (" + locationNameHql + "=:name) "
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
		}
		return null;
	}

	public static AmpCategoryValueLocations getLocationByName(
			String locationName, AmpCategoryValue cvLocationLayer)
			throws NullPointerException, NonUniqueResultException {
		Session dbSession = null;

		if (cvLocationLayer == null)
			throw new NullPointerException(
					"Value for Implementation Location cannot be null");
		try {
			dbSession = PersistenceManager.getSession();
			String locationNameHql = AmpCategoryValueLocations.hqlStringForName("loc");
			String queryString = "select loc from "
					+ AmpCategoryValueLocations.class.getName()
					+ " loc where (" + locationNameHql + "=:name) "
					+ " AND (loc.parentCategoryValue=:cvId) ";
			Query qry = dbSession.createQuery(queryString);
			qry.setLong("cvId", cvLocationLayer.getId());
			qry.setString("name", locationName);
			Collection<AmpCategoryValueLocations> locations = qry.list();
			if (locations != null && locations.size() > 0) {
				return locations.toArray(new AmpCategoryValueLocations[0])[0];
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static AmpCategoryValueLocations getLocationByIso(
			String locationIso, HardCodedCategoryValue hcLocationLayer) {
		return getLocationByIso(locationIso, hcLocationLayer, true);
	}

    /**
     * This method does not read the layer as a <code>HardCodedCategoryValue</code> value
     * from the cache, instead reads it from the database
     *
     * We need this in cases when the default value is cached (say, implementation_location=Region)
     * But we need to load (say, the defaultCountry, where implementation_location=Country)
     * Otherwise we get null.
     * 
     * !! CONSTANTIN: on 12/03/2014, the code branches under if (readLayerFromCache) {} else {} are both reimplementations of the same thing. Rewriting to ignore the parameter !!
     *
     * @param locationIso
     * @param hcLocationLayer
     * @param readLayerFromCache
     * @return
     */
    public static AmpCategoryValueLocations getLocationByIso(
            String locationIso, HardCodedCategoryValue hcLocationLayer, boolean readLayerFromCache) {
        try {
            AmpCategoryValue layer = hcLocationLayer.getAmpCategoryValueFromDB();
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
					+ " loc where (lower(loc.iso) like :iso)";
			if (cvLocationLayer != null) {
				queryString += " AND (loc.parentCategoryValue=:cvId) ";
			}
			Query qry = dbSession.createQuery(queryString);
			qry.setCacheable(true);
			if (cvLocationLayer != null) {
				qry.setLong("cvId", cvLocationLayer.getId());
			}
			qry.setString("iso", locationIso.toLowerCase());
			AmpCategoryValueLocations loc = (AmpCategoryValueLocations) qry
					.uniqueResult();
			return loc;
		} catch (Exception e) {
			e.printStackTrace();
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
			AmpCategoryValue layer = hcLocationLayer.getAmpCategoryValueFromDB();
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
		Set<Long> allOutputLocations = getRecursiveChildrenOfCategoryValueLocations(AlgoUtils.collectIds(new HashSet<Long>(), locations));
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
	 * recursively get all ancestors (parents) of a set of AmpCategoryValueLocations, by a wave algorithm
	 * @param inIds
	 * @return
	 */
	public static Set<Long> getRecursiveAscendantsOfCategoryValueLocations(Collection<Long> inIds)
	{
		return AlgoUtils.runWave(inIds, 
				new DatabaseWaver("SELECT DISTINCT(parent_location) FROM amp_category_value_location WHERE (parent_location IS NOT NULL) AND (id IN ($))"));
	}
		
	/**
	 * recursively get all children of a set of AmpCategoryValueLocations, by a wave algorithm
	 * @param inIds
	 * @return
	 */
	public static Set<Long> getRecursiveChildrenOfCategoryValueLocations(Collection<Long> inIds)
	{
		return AlgoUtils.runWave(inIds, 
				new DatabaseWaver("SELECT DISTINCT id FROM amp_category_value_location WHERE parent_location IN ($)"));
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
	
	/**
	 * returns the ACVL of the country the current AMP installation is running on
	 * @return
	 */
	public static AmpCategoryValueLocations getDefaultCountry()
	{
		AmpCategoryValueLocations country = DynLocationManagerUtil.getLocationByIso(FeaturesUtil.getDefaultCountryIso(), CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY );
		return country;
	}
	
	public static Collection<AmpCategoryValueLocations> getRegionsOfDefCountryHierarchy() throws DgException 
	{	
		synchronized (DynLocationManagerUtil.regionsOfDefaultCountry)
		{
			if ( DynLocationManagerUtil.regionsOfDefaultCountry.isEmpty())
			{
		 	 	AmpCategoryValueLocations country = DynLocationManagerUtil.getDefaultCountry();
		 	 	if (country != null){
			 	 	country = getLocationOpenedSession( country.getId() );
			 	 	
			 	 	Set<AmpCategoryValueLocations> children				= country.getChildLocations();
			 	 	
			 	 	DynLocationManagerUtil.regionsOfDefaultCountry.addAll(children);
		 	 	}
			}
			return Collections.unmodifiableList(regionsOfDefaultCountry);
		}
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

	public static Set<AmpCategoryValueLocations> getLocationsByLayer(HardCodedCategoryValue hcLayer) {
		try {
			AmpCategoryValue layer = hcLayer.getAmpCategoryValueFromDB();
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
	public static Set<AmpCategoryValueLocations> getLocationsByLayer(AmpCategoryValue cvLayer) {
		TreeSet<AmpCategoryValueLocations> returnSet = new TreeSet<AmpCategoryValueLocations>(
				alphabeticalLocComp);
		try {
			Session dbSession = PersistenceManager.getSession();
			String queryString = "select loc from "
					+ AmpCategoryValueLocations.class.getName()
					+ " loc where (loc.parentCategoryValue=:cvId) ";
			Query qry = dbSession.createQuery(queryString);
			qry.setLong("cvId", cvLayer.getId());
            qry.setCacheable(true);
			returnSet.addAll(qry.list());
			return returnSet;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
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
		}
		return 0;
	}

	public static Comparator<AmpCategoryValueLocations> alphabeticalLocComp = new Comparator<AmpCategoryValueLocations>() {
		public int compare(AmpCategoryValueLocations o1, AmpCategoryValueLocations o2) {
			return o1.getName().compareTo(o2.getName());
		}
	};

    public static ErrorCode importExcelFile(InputStream inputStream, Option option) throws AimException {
        try {
            Workbook workBook = WorkbookFactory.create(inputStream);
            Sheet hssfSheet = workBook.getSheetAt(0);
            Row hssfRow = hssfSheet.getRow(0);

            int physicalNumberOfCells = hssfRow.getPhysicalNumberOfCells();
            List<AmpCategoryValue> implLocs = new ArrayList<AmpCategoryValue>(
                    CategoryManagerUtil.getAmpCategoryValueCollectionByKeyExcludeDeleted(CategoryConstants.IMPLEMENTATION_LOCATION_KEY));
            
            int i = 1;
            int hierarchyNumberOfCells=implLocs.size();
            // last five cells are not hierarchy cells and first cell is db id
            if (hierarchyNumberOfCells + 6 != physicalNumberOfCells) {
                return ErrorCode.NUMBER_NOT_MATCH;
            }

            for (AmpCategoryValue location : implLocs) {
            	String cellValue = hssfRow.getCell(i).getStringCellValue();
                if (!StringUtils.equalsIgnoreCase(cellValue, location.getValue())
                		&& !StringUtils.equalsIgnoreCase(cellValue, TranslatorWorker.translateText(location.getValue()))) {
                    return ErrorCode.NAME_NOT_MATCH;
                }
                
                i++;
            }

            for (int j = 1; j < hssfSheet.getPhysicalNumberOfRows(); j++) {
                AmpCategoryValueLocations parentLoc=null;
                hssfRow = hssfSheet.getRow(j);

                if (hssfRow != null) {

                    Cell cell = hssfRow.getCell(0);
                    Long databaseId = null;

                    if (cell != null) {
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_STRING:
                                databaseId = (cell.getStringCellValue() == null || (cell.getStringCellValue() != null && cell.getStringCellValue().trim().equals(""))) ? null : Long.parseLong(cell.getStringCellValue());
                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                                databaseId = (long) cell.getNumericCellValue();
                                break;
                        }
                    }

                    List<String> locationNames = new ArrayList<String>();
                    int k = 1;

                    for (; k <= hierarchyNumberOfCells; k++) {
                        cell = hssfRow.getCell(k);
                        if (cell == null) {
                            k = hierarchyNumberOfCells + 1;
                            break;
                        }
                        String location = getValue(cell);
                        if (location == null || location.trim().length() == 0) {
                            k = hierarchyNumberOfCells + 1;
                            break;
                        }
                        locationNames.add(location);
                    }

                    cell = hssfRow.getCell(k++);
                    String lalitude = getValue(cell);
                    cell = hssfRow.getCell(k++);
                    String longitude = getValue(cell);
                    cell = hssfRow.getCell(k++);
                    String geoID = getValue(cell);
                    
                    if (geoID != null && geoID.contains(".0")) {
                        geoID = geoID.replace(".0", "");
                    }
                    
                    cell = hssfRow.getCell(k++);
                    String iso = getValue(cell);
                    cell = hssfRow.getCell(k++);
                    String iso3 = getValue(cell);
                    for (k = 0; k < locationNames.size(); k++) {
                        String name = locationNames.get(k);
                        AmpCategoryValue implLoc = implLocs.get(k);
                        if (k == locationNames.size() - 1) {
                            AmpCategoryValueLocations location = new AmpCategoryValueLocations();

                            AmpCategoryValueLocations currentLoc = getLocationByName(name, implLoc, parentLoc);
                            if (currentLoc != null) {
                                if (option.equals(Option.NEW)) {
                                    break;
                                } else {
                                    location = currentLoc;
                                }
                            } else {
                                if (option.equals(Option.OVERWRITE)) {
                                    if (databaseId != null && databaseId != 0) {
                                        location = getLocationByIdRequestSession(databaseId);
                                    } else {
                                        break;
                                    }
                                }
                            }
                            if (location != null) {
                                if (location.getParentCategoryValue() != null && !location.getParentCategoryValue().equals(implLoc)) {
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
                                boolean edit = location.getId() != null;
                                LocationUtil.saveLocation(location, edit);
                            }
                        } else {
                            parentLoc = getLocationByName(name, implLoc, parentLoc);
                            if (parentLoc == null) {
                            	logger.error("Parent location is null");
                                return ErrorCode.INCORRECT_CONTENT;
                            }
                        }
                    }
                }
            }

        }
        catch (Exception e) {
            logger.error("Exception throwed during the import of the regions.", e);
            return ErrorCode.INCORRECT_CONTENT;
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
		NUMBER_NOT_MATCH, NAME_NOT_MATCH, INCORRECT_CONTENT, CORRECT_CONTENT, INEXISTANT_ADM_LEVEL, LOCATION_NOT_FOUND
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
	
 public static List <AmpIndicatorLayer> getIndicatorByCategoryValueId (Long id) {
		Session dbSession = PersistenceManager.getSession();
		String queryString = "select ind from "
				+ AmpIndicatorLayer.class.getName()
				+ " ind where ind.admLevel.id=:id ";
		Query qry = dbSession.createQuery(queryString);
		qry.setCacheable(true);
		qry.setLong("id", id);
		return qry.list();
	 
 }

 public static List <AmpIndicatorLayer> getIndicatorByCategoryValueIdAndIndicatorId(Long admLevelId, Long indicatorId) {
		Session dbSession = PersistenceManager.getSession();
		String queryString = "select ind from "
				+ AmpIndicatorLayer.class.getName()
				+ " ind where ind.admLevel.id=:id and ind.id=:indicatorId ";
		Query qry = dbSession.createQuery(queryString);
		qry.setCacheable(true);
		qry.setLong("id", admLevelId);
		qry.setLong("indicatorId", indicatorId);
		return qry.list();

 }

    public static List <AmpIndicatorLayer> getIndicatorLayers () {
        return getIndicatorLayers(IndicatorEPConstants.DEFAULT_INDICATOR_ORDER_FIELD, "");

    }

    public static List <AmpIndicatorLayer> getIndicatorLayers (String orderBy, String sort) {
		Session dbSession = PersistenceManager.getSession();

		String queryString = "select ind from "
				+ AmpIndicatorLayer.class.getName() + " ind order by " + orderBy + " " + sort;
		Query qry = dbSession.createQuery(queryString);
		return qry.list();
	 
}

    public static List <AmpIndicatorLayer> getIndicatorLayerByAccessType(long accessTypeId, String orderBy, String sort) {
        Session dbSession = PersistenceManager.getSession();
        String queryString = "select ind from "
                + AmpIndicatorLayer.class.getName() + " ind where accessType.id=:accessTypeId order by " + orderBy + " " + sort;
        Query qry = dbSession.createQuery(queryString);
        qry.setLong("accessTypeId", accessTypeId);
        return qry.list();

    }

    public static List <AmpIndicatorLayer> getIndicatorLayerByCreatedBy (AmpTeamMember teamMember, String orderBy, String sort) {
        Session dbSession = PersistenceManager.getSession();
        String queryString = "select ind from " + AmpIndicatorLayer.class.getName() + " ind ";
        queryString += " left join ind.sharedWorkspaces s ";
        queryString += " where createdBy.ampTeamMemId=:teamMemberId ";

        Collection<AmpTeam> workspaces = null;
        TeamMember tm = TeamUtil.getCurrentMember();
        if (tm != null) {
            workspaces=TeamMemberUtil.getAllTeamsForUser(tm.getEmail());
        }
        queryString += " or ( s.workspace.ampTeamId in( " + Util.toCSStringForIN(workspaces) + ")) ";
        queryString += " order by " + orderBy + " " + sort;

        Query qry = dbSession.createQuery(queryString);
        qry.setLong("teamMemberId", teamMember.getAmpTeamMemId());
        return qry.list();

    }

    public static AmpIndicatorLayer getIndicatorLayerById (Long id) {
        Session dbSession = PersistenceManager.getSession();
        String queryString = "select ind from "
                + AmpIndicatorLayer.class.getName() + " ind where id=:id";
        Query qry = dbSession.createQuery(queryString);
        qry.setLong("id", id);
        if (qry.list().size()==1)
            return (AmpIndicatorLayer) qry.list().get(0);
        else
            return null;

    }

 public static List <AmpLocationIndicatorValue> getLocationIndicatorValueByLocation (AmpCategoryValueLocations location) {
	 Session dbSession = PersistenceManager.getSession();
		String queryString = "select value from "
				+ AmpLocationIndicatorValue.class.getName()
				+ " value where value.location.id=:id)";
		Query qry = dbSession.createQuery(queryString);
		qry.setCacheable(true);
		qry.setLong("id", location.getId());
		return qry.list(); 
 }

 public static List <AmpLocationIndicatorValue> getLocationIndicatorValueByLocationAndIndicator (AmpCategoryValueLocations location,AmpIndicatorLayer indicator) {
	 Session dbSession = PersistenceManager.getSession();
		String queryString = "select value from "
				+ AmpLocationIndicatorValue.class.getName()
				+ " value where value.location.id=:id "
				+ " and value.indicator.id=:indicatorId ";

		Query qry = dbSession.createQuery(queryString);
		qry.setCacheable(true);
		qry.setLong("id", location.getId());
		qry.setLong("indicatorId", indicator.getId());
		return qry.list();
 }
 
 public static void deleteIndicatorLayer (AmpIndicatorLayer indLayer) {
	 Session dbSession = PersistenceManager.getSession();
	 String queryString = "delete from "
				+ AmpLocationIndicatorValue.class.getName()
				+ "  where indicator.id=:indicatorLayerId";
	
	 Query qry = dbSession.createQuery(queryString);
   	 qry.setLong("indicatorLayerId", indLayer.getId());
	 qry.executeUpdate();
	 dbSession.delete(indLayer);
 }
 
 public static AmpLocationIndicatorValue getLocationIndicatorValue (Long indicator, Long location) {
	 Session dbSession = PersistenceManager.getSession();
		String queryString = "select value from "
				+ AmpLocationIndicatorValue.class.getName()
				+ " value where value.location.id=:locationId and value.indicator.id=:indicatorId)";
		Query qry = dbSession.createQuery(queryString);
		qry.setCacheable(true);
		qry.setLong("locationId", location);
		qry.setLong("indicatorId", indicator);
		return (AmpLocationIndicatorValue)qry.uniqueResult(); 
 }

    /**
     * Return the errorc
     * @param inputStream
     * @param option
     * @return returns
     * @throws AimException
     */
    public static ErrorWrapper importIndicatorTableExcelFile(InputStream inputStream, Option option) throws AimException {
       return importIndicatorTableExcelFile(inputStream, option, 0);
    }

    /**
     * Return the errorc
     * @param inputStream
     * @param option
     * @return returns
     * @throws AimException
     */
    public static ErrorWrapper importIndicatorTableExcelFile(InputStream inputStream, Option option, long indicatorId) throws AimException {
        POIFSFileSystem fsFileSystem = null;

        Set<String> geoIdsWithProblems=new HashSet<String>();
        try {
            fsFileSystem = new POIFSFileSystem(inputStream);
            HSSFWorkbook workBook = new HSSFWorkbook(fsFileSystem);

            HSSFSheet hssfSheet = workBook.getSheetAt(0);
            Row hssfRow = hssfSheet.getRow(0);
            Cell admLevelCell = hssfRow.getCell(0);
            String admLevel = "";
            AmpCategoryValue selectedAdmLevel = null;
            if (admLevelCell!= null) {
                admLevel = admLevelCell.getStringCellValue();
            }
            List<AmpCategoryValue> implLocs = new ArrayList<AmpCategoryValue>(
                    CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.IMPLEMENTATION_LOCATION_KEY));

            for (AmpCategoryValue admLevelValue:implLocs) {
                if (admLevel.equalsIgnoreCase(admLevelValue.getValue()) && admLevelValue.isVisible()) {
                    selectedAdmLevel = admLevelValue;
                    break;
                }
            }

            if (selectedAdmLevel == null) {
                return new ErrorWrapper(ErrorCode.INEXISTANT_ADM_LEVEL);
            }

            int physicalNumberOfCells = hssfRow.getPhysicalNumberOfCells();
            List<AmpIndicatorLayer> indicatorLayers;
            if (indicatorId > 0) {
                indicatorLayers = DynLocationManagerUtil.getIndicatorByCategoryValueIdAndIndicatorId(selectedAdmLevel.getId(), indicatorId);
            } else {
                indicatorLayers = DynLocationManagerUtil.getIndicatorByCategoryValueId(selectedAdmLevel.getId());
            }
            int indicatorNumberOfCells=indicatorLayers.size();
            if (indicatorNumberOfCells+2 < physicalNumberOfCells) {
                return new ErrorWrapper(ErrorCode.NUMBER_NOT_MATCH);
            }
            List<AmpIndicatorLayer>  orderedIndicators = new ArrayList<AmpIndicatorLayer>();
            for (int i=2;i<physicalNumberOfCells;i++) {
                String cellValue = hssfRow.getCell(i).getStringCellValue();
                boolean found = false;
                for (AmpIndicatorLayer indicator:indicatorLayers) {
                    if (indicator.getName().equalsIgnoreCase(cellValue)) {
                        found = true;
                        orderedIndicators.add(indicator);
                        break;
                    }
                }
                if (!found) {
                    return new ErrorWrapper(ErrorCode.NAME_NOT_MATCH);
                }
            }
            for (int j = 1; j < hssfSheet.getPhysicalNumberOfRows(); j++) {
                hssfRow = hssfSheet.getRow(j);
                if (hssfRow != null) {
                    AmpCategoryValueLocations locationObject = null;
                    String geoCodeId = null;

                    Cell geoCodeIdCell = hssfRow.getCell(1);
                    if (geoCodeIdCell != null) {
                        geoCodeId = getValue(geoCodeIdCell);
                        //some versions of excel converts to numeric and adds a .0 at the end
                        if (StringUtils.isNotEmpty(geoCodeId) && !".0".equals(geoCodeId)) {
                            geoCodeId = geoCodeId.replace(".0", "");
                            locationObject = DynLocationManagerUtil.getLocationByGeoCode(geoCodeId, selectedAdmLevel);
                            if(locationObject == null) {
                                geoIdsWithProblems.add(geoCodeId);
                                continue;
                            }
                        } else {
                            continue;
                        }
                    }

                    int index = 2;
                    for (AmpIndicatorLayer indicator : orderedIndicators) {
                        Cell cell = hssfRow.getCell(index++);
                        String value = getValue(cell);
                        AmpLocationIndicatorValue locationIndicatorValue = DynLocationManagerUtil.getLocationIndicatorValue (1l,indicator.getId());
                        if (locationIndicatorValue!=null && option.equals(Option.NEW)) {
                            continue;
                        }
                        else {
                            if (locationIndicatorValue!= null) {
                                locationIndicatorValue.setValue(Double.valueOf(value));
                            }
                            else {
                                locationIndicatorValue = new AmpLocationIndicatorValue();
                                locationIndicatorValue.setValue(Double.valueOf(value));
                                locationIndicatorValue.setIndicator(indicator);
                                locationIndicatorValue.setLocation(locationObject);
                            }
                        }
                        DbUtil.saveOrUpdateObject(locationIndicatorValue);
                    }
                }
            }

        }
        catch (NullPointerException e) {
            logger.error("file is not ok");
            throw new AimException("Cannot import indicator values", e);
        }
        catch (IllegalStateException e) {
            logger.error("file is not ok", e);
            return new ErrorWrapper(ErrorCode.INCORRECT_CONTENT);
        }
        catch(IOException e){
            logger.error("file is not ok", e);
            return new ErrorWrapper(ErrorCode.INCORRECT_CONTENT);
        }
        catch (Exception e) {
            logger.error(e);
            throw new AimException("Cannot import indicator values", e);
        }
        if(geoIdsWithProblems.size()>0){
            //we have geoids with errors
            return new ErrorWrapper(ErrorCode.LOCATION_NOT_FOUND,geoIdsWithProblems);
        }else{
            return new ErrorWrapper(ErrorCode.CORRECT_CONTENT);}
    }
 
	/**
	 * 
	 * @param geoCode
	 * @param cvLocationLayer
	 *            the AmpCategoryValue specifying the layer (level) of the
	 *            location...like Country or Region
	 * @return
	 */
	public static AmpCategoryValueLocations getLocationByGeoCode(
			String geoCode, AmpCategoryValue cvLocationLayer) {
		Session dbSession = null;

		try {
			dbSession = PersistenceManager.getSession();
			String queryString = "select loc from "
					+ AmpCategoryValueLocations.class.getName()
					+ " loc where (loc.geoCode=:geoCode)";
			if (cvLocationLayer != null) {
				queryString += " AND (loc.parentCategoryValue=:cvId) ";
			}
			Query qry = dbSession.createQuery(queryString);
			if (cvLocationLayer != null) {
				qry.setLong("cvId", cvLocationLayer.getId());
			}
			qry.setString("geoCode", geoCode);
			AmpCategoryValueLocations loc = (AmpCategoryValueLocations) qry
					.uniqueResult();
			return loc;
		} catch (Exception e) {
			logger.error("Exception getting AmpCategoryValueLocations by geoCode:"+geoCode,e);
		}
		return null;
	}
	public static class ErrorWrapper{
		ErrorCode errorCode;
		Set<String> moreinfo;
		public ErrorWrapper(ErrorCode errorCode) {
			this.errorCode=errorCode;
		}
		public ErrorWrapper(ErrorCode errorCode,Set<String>moreinfo){
			this(errorCode);
			this.moreinfo=moreinfo;
		}
		public ErrorCode getErrorCode() {return errorCode;}
		public void setErrorCode(ErrorCode errorCode) {this.errorCode = errorCode;}
		public Set<String> getMoreinfo() {return moreinfo;}
		public void setMoreinfo(Set<String> moreinfo) {this.moreinfo = moreinfo;}
		
	}

}

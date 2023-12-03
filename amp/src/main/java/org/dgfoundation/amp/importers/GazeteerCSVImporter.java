package org.dgfoundation.amp.importers;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.ampapi.postgis.entity.AmpLocator;
import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Properties;


/**
 * In order to import the file, copy the csv for the country you want to import locations
 * (extracted from http://download.geonames.org/export/dump/) into the directory /doc
 * and save with the name: gazeteer.csv When AMP is started the locations will be imported
 * into the DB
 * 
 * @author Emanuel
 *
 */
public class GazeteerCSVImporter extends CSVImporter {

    private static Logger logger = Logger.getLogger(GazeteerCSVImporter.class);

    public GazeteerCSVImporter(String importFileName, String[] columnNames, Properties extraProperties) {
        super(importFileName, columnNames, extraProperties);
    }

    @Override
    protected Class[] getImportedTypes() {
        return new Class[] { AmpLocator.class };
    }

    @Override
    protected void saveToDB(Map<String, String> o) throws HibernateException {
        logger.info("importing: " + o);
        AmpLocator locator = new AmpLocator();
        if (o.get("geonameId") != null) {
            locator.setGeonameId(Long.valueOf(o.get("geonameId")));

        }
        locator.setName(o.get("name"));
        locator.setAsciiName(o.get("ascciName"));
        locator.setAlternateNames(o.get("alternateNames"));
        if (o.get("featureClass") != null) {
            locator.setFeatureClass(o.get("featureClass").charAt(0));
        }
        locator.setFeatureCode(o.get("featureCode"));
        locator.setCountryIso(o.get("countryIso"));
        locator.setCc2(o.get("cc2"));
        locator.setAdmin1(o.get("adm1"));
        locator.setAdmin2(o.get("adm2"));
        locator.setAdmin3(o.get("adm3"));
        locator.setAdmin4(o.get("adm4"));
        if (o.get("population") != null) {
            locator.setPopulation(Long.valueOf(o.get("population")));
        }
        if (o.get("elevation") != null) {
            locator.setElevation(Integer.valueOf(o.get("elevation")));
        }
        if (o.get("gtopo30") != null) {
            locator.setGtopo30(Integer.valueOf(o.get("gtopo30")));
        }
        locator.setTimezone(o.get("timezone"));
        locator.setLatitude(o.get("latitude"));
        locator.setLongitude(o.get("longitude"));
        
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-mm-dd");
        try {
            locator.setLastModified(formater.parse(o.get("lastModified")));
        } catch (ParseException e) {
            logger.warn("Error parsing date: " + o.get("lastModifed") + " when importing AmpLocator");
        }
        session.save(locator);

    }

    public boolean isTableEmpty() {
        boolean isTableEmpty = false;
        try {
            //first check if amp_locator table exists
            if (SQLUtils.tableExists("amp_locator")){
                Query q = PersistenceManager.getSession().createQuery("select count(*) from " + AmpLocator.class.getName() + " loc ");
                isTableEmpty = PersistenceManager.getInteger(q.uniqueResult()) == 0;
            }
        } catch (HibernateException e) {
            logger.error("Error checking if AmpLocator table is empty" , e);
        }
        return isTableEmpty;

    }

}

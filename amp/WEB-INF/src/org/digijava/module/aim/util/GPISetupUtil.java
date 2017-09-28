package org.digijava.module.aim.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpGPISurveyIndicator;
import org.digijava.module.aim.dbentity.AmpMeasures;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.GPIDefaultFilters;
import org.digijava.module.aim.dbentity.GPISetup;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;

public class GPISetupUtil {

    public static AmpMeasures getMeasure(Long id) {
        AmpMeasures measure = null;
        try {
            Session session = null;
            session = PersistenceManager.getRequestDBSession();
            measure = (AmpMeasures) session.get(AmpMeasures.class, id);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return measure;
    }

    public static GPISetup getSetup() {
        GPISetup setup = null;
        try {
            Session session = null;
            session = PersistenceManager.getRequestDBSession();
            setup = (GPISetup) session.createCriteria(GPISetup.class).uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return setup;
    }

    public static void saveGPISetup(GPISetup setup) throws Exception {
        Session session = null;
        session = PersistenceManager.getRequestDBSession();
        session.saveOrUpdate(setup);
    }
    
    public static void saveDescription(Long id, String description) {
        try {
            Session session = null;
            session = PersistenceManager.getRequestDBSession();
            AmpGPISurveyIndicator indicator = (AmpGPISurveyIndicator) session.load(AmpGPISurveyIndicator.class, id);
            indicator.setDescription(description);
            session.saveOrUpdate(indicator);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void saveDonorType(Long id) {
        try {
            Session session = PersistenceManager.getRequestDBSession();
            String qry = "FROM " + GPIDefaultFilters.class.getName() + " as df WHERE df.key LIKE :key_ AND df.value LIKE :value_";
            GPIDefaultFilters filter = (GPIDefaultFilters) session.createQuery(qry)
                .setString("key_", GPIDefaultFilters.GPI_DEFAULT_FILTER_ORG_GROUP)
                .setString("value_", id.toString())
                .uniqueResult();
            if(filter == null) {
                filter = new GPIDefaultFilters();
                filter.setKey(GPIDefaultFilters.GPI_DEFAULT_FILTER_ORG_GROUP);
                filter.setValue(id.toString());
                session.save(filter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static Collection<String> getSavedFilters(String key) throws DgException {
        Collection<String> ret = new ArrayList<String>();   
        Session session = PersistenceManager.getRequestDBSession();
        String qryString = "FROM " + GPIDefaultFilters.class.getName() + " WHERE key = '" + GPIDefaultFilters.GPI_DEFAULT_FILTER_ORG_GROUP + "'" ;
        Query qrySavedDonorTypes = session.createQuery(qryString);
        Collection<GPIDefaultFilters> defaultFilters =  qrySavedDonorTypes.list();
        
        List<AmpOrgType> types = DbUtil.getAllOrgTypesOfPortfolio();
        Iterator<AmpOrgType> iterTypes = types.iterator();
        while(iterTypes.hasNext()) {
            AmpOrgType type = iterTypes.next();
            Iterator<GPIDefaultFilters> iFilters = defaultFilters.iterator();
            while(iFilters.hasNext()) {
                GPIDefaultFilters auxFilter = iFilters.next();
                if(type.getAmpOrgTypeId().toString().equals(auxFilter.getValue())) {
                    ret.add(auxFilter.getValue());
                }
            }
        }
        return ret;
    }
    
    public static void cleanupSavedDonorTypes() throws DgException {
        Session session = PersistenceManager.getRequestDBSession();
        session.createQuery("DELETE FROM " + GPIDefaultFilters.class.getName()).executeUpdate();
    }
}

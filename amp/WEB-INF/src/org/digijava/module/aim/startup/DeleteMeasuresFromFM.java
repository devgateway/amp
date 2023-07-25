package org.digijava.module.aim.startup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.util.FeaturesUtil;

public class DeleteMeasuresFromFM {
    
    protected final List<String> measures;
    private static Logger logger = Logger.getLogger(DeleteMeasuresFromFM.class);
    
    public DeleteMeasuresFromFM(List<String> measNames) {
        this.measures = new ArrayList<String>(measNames);
    }
    
    public void work() {
        logger.error("deleting from the FM false-measures with names: " + measures);
        String measNamesStr = Util.toCSStringForIN(measures);
        List<?> ids = PersistenceManager.getSession().createNativeQuery(
                String.format("SELECT id FROM amp_features_visibility WHERE name IN (%s) AND parent = (SELECT id FROM amp_modules_visibility where name='Measures')",
                        measNamesStr)).list();
        for(Object obj:ids) {
            long id = PersistenceManager.getLong(obj);
            FeaturesUtil.deleteFeatureVisibility(id, PersistenceManager.getRequestDBSession());
            //PersistenceManager.getSession().createNativeQuery("DELETE FROM amp_features_templates WHERE )
        }
    }
}

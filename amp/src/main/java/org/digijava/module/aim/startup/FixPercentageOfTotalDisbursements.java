package org.digijava.module.aim.startup;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.util.FeaturesUtil;

import java.util.*;

public class FixPercentageOfTotalDisbursements {
    
    /**
     * Map<measure_to_replace, Pair<measure_with_which_to_replace, measure_expression>>
     */
    protected final Map<String, Pair<String, String>> measuresToReplace =
            Collections.unmodifiableMap(new HashMap<String, Pair<String, String>>() {{
                put("Percentage of Total Disbursements",
                        Pair.of(MeasureConstants.PERCENTAGE_OF_TOTAL_DISBURSEMENTS, "percentageDisbursements"));
            }});
    
    private static Logger logger = Logger.getLogger(FixPercentageOfTotalDisbursements.class);
    
    public FixPercentageOfTotalDisbursements() {
    }
    
    public void work() {
        for(String fromMeasure:measuresToReplace.keySet()) {
            Pair<String, String> toMeasure = measuresToReplace.get(fromMeasure);
            if (fromMeasure.equals(toMeasure.getLeft())) {
                throw new RuntimeException(String.format("cannot replace references to one measure with references to oneself: <%s>", fromMeasure));
            }
            long measId = ensureMeasureExists(toMeasure.getLeft(), toMeasure.getRight());
            long oldMeasId = getMeasureId(fromMeasure);
            if (measId == oldMeasId)
                throw new Error("huge bug: fromMeasure != toMeasure, but oldMeasId == measId");
            replaceMeasureReferences(oldMeasId, measId);
        }
        new DeleteMeasuresFromFM(new ArrayList<>(measuresToReplace.keySet())).work();
        ensureMeasuresUniqueness();
    }
    
    /**
     * returns -1 if none exists
     * @param measureName
     * @return
     */
    protected long getMeasureId(String measureName) {
        String measureGettingQuery = "SELECT measureid FROM amp_measures WHERE measurename = '" + SQLUtils.sqlEscapeStr(measureName) + "'";
        List<?> ids = PersistenceManager.getSession().createNativeQuery(measureGettingQuery).list();
        
        if (ids.size() > 1) throw new Error(String.format("the database has %d measures with the name '%s'. Fix your database!", ids.size(), measureName));
        if (ids.isEmpty()) return -1;
        return PersistenceManager.getLong(ids.get(0));
    }
    
    protected long ensureMeasureExists(String toMeasure, String measureExpression) {
        long id = getMeasureId(toMeasure);
        if (id > 0) {
            logger.info(String.format("Measure <%s> already exists in amp_measures", toMeasure));
            return id;
        }
        
        logger.error(String.format("inserting Measure <%s> in amp_measures...", toMeasure));
        PersistenceManager.getSession().createNativeQuery(
                String.format("INSERT INTO amp_measures(measureid, measurename, aliasname, type, expression) VALUES (nextval('amp_measures_seq'), '%s', '%s', 'A', '%s')", 
                        toMeasure, toMeasure, measureExpression)).executeUpdate();
        
        return getMeasureId(toMeasure);
    }
    
    protected void replaceMeasureReferences(long oldMeasId, long toMeasure) {
        if (oldMeasId <= 0) return; // nothing to do
        logger.error(String.format("replacing all references to measure id %d with references to measure_id %d", oldMeasId, toMeasure));
        int updated = PersistenceManager.getSession().createNativeQuery("UPDATE amp_report_measures SET measureid = " + toMeasure + " where measureid = " + oldMeasId).executeUpdate();
        int deleted = PersistenceManager.getSession().createNativeQuery("DELETE FROM amp_measures WHERE measureid = " + oldMeasId).executeUpdate();        
        logger.error(String.format("replaced references in %d reports, deleted entry from amp_measures: %d", updated, deleted));
    }
    
    protected void ensureMeasuresUniqueness() {
        long measuresModuleId = PersistenceManager.getLong(
                PersistenceManager.getSession().createNativeQuery("SELECT id FROM amp_modules_visibility where name='Measures'").uniqueResult());
        
        List<Object[]> ids = PersistenceManager.getSession().createNativeQuery(
                String.format("SELECT afv.id, afv.name FROM amp_features_visibility afv WHERE afv.parent = %d AND (select count(*) from amp_features_visibility afv2 where afv2.name = afv.name and afv2.parent = afv.parent and afv2.id > afv.id) > 0",
                        measuresModuleId)).list();
        
        for(Object[] obj:ids) {
            long id = PersistenceManager.getLong(obj[0]);
            String name = (String) obj[1];
            logger.error(
                    String.format("deleting measure-in-fm with id = %d and name = <%s>, because an another with the same name but with a lower ID exists", id, name));
            FeaturesUtil.deleteFeatureVisibility(id, PersistenceManager.getSession());
            //PersistenceManager.getSession().createNativeQuery("DELETE FROM amp_features_templates WHERE )
        }
    }
}

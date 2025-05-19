package org.digijava.module.aim.util;

import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.type.LongType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Utility for searching for incorrect data.
 * @author Irakli Kobiashvili
 *
 */
public class InvalidDataUtil {
    
    /**
     * Loads activity-sector connection beans for those activities
     * @param activityIds
     * @return
     * @throws DgException
     */
    @SuppressWarnings("unchecked")
    public static List<ActivitySectorPercentages> getActivitiesWithIncorrectSectorPersentage(List<Long> activityIds) throws DgException {
        List<ActivitySectorPercentages> results = null;
        boolean filter = activityIds!=null && activityIds.size()>0;
        String oql ="select actsec.activityId.ampActivityId, actsec.activityId.name, actsec.activityId.ampId,count(actsec.activityId), sum(actsec.sectorPercentage) ";
        oql+="from "+AmpActivitySector.class.getName()+" as actsec ";
        if (filter){
            oql+="where actsec.activityId in (:id_list) ";
        }
        oql+="group by actsec.activityId having sum(actsec.sectorPercentage) <> 100";
        Session session = PersistenceManager.getRequestDBSession();
        Query query = session.createQuery(oql);
        if(filter){
            query.setParameterList("id_list", activityIds,LongType.INSTANCE);
        }
        List dataset = query.list();
        if (dataset!=null && dataset.size()>0){
            results = new ArrayList<ActivitySectorPercentages>(dataset.size());
            for (Object row : dataset) {
                Object[] fields = (Object[])row; 
                Long actId=(Long)fields[0];
                String activityName = (String)fields[1];
                String ampId = (String)fields[2];
                Integer numOfSectrs = (Integer)fields[3];
                Float sumOfPercents = (Float)fields[4];
                ActivitySectorPercentages result = new ActivitySectorPercentages(
                        actId.longValue(),
                        activityName,
                        ampId,
                        numOfSectrs.intValue(),
                        sumOfPercents.intValue());
                results.add(result);
            }
        }
        return results;
    }
    
    public static class ActivitySectorPercentages{

        long activityId;
        String activityName;
        String ampId;
        int numOfSectors;
        float totalPercentage;
        
        public ActivitySectorPercentages() {
        }
        
        public ActivitySectorPercentages(long actId, String actName, String amp_id, int sectorNumber, float sumPercents) {
            this.activityId = actId;
            this.activityName = actName;
            this.ampId = amp_id;
            this.numOfSectors = sectorNumber;
            this.totalPercentage = sumPercents;
        }
        
        public long getActivityId() {
            return activityId;
        }

        public void setActivityId(long activityId) {
            this.activityId = activityId;
        }

        public String getActivityName() {
            return activityName;
        }

        public void setActivityName(String activityName) {
            this.activityName = activityName;
        }

        public String getAmpId() {
            return ampId;
        }

        public void setAmpId(String ampId) {
            this.ampId = ampId;
        }

        public int getNumOfSectors() {
            return numOfSectors;
        }

        public void setNumOfSectors(int numOfSectors) {
            this.numOfSectors = numOfSectors;
        }

        public float getTotalPercentage() {
            return totalPercentage;
        }

        public void setTotalPercentage(float totalPercentage) {
            this.totalPercentage = totalPercentage;
        }
    }
    
    public static class ActivitySectorPercentagesComparator implements Comparator<ActivitySectorPercentages>{

        public int compare(ActivitySectorPercentages o1,ActivitySectorPercentages o2) {
            String name1=o1.getActivityName();
            String name2=o2.getActivityName();
            return name1.compareTo(name2);
        }
        
    }
}

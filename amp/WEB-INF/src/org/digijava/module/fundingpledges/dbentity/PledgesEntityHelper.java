package org.digijava.module.fundingpledges.dbentity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.fundingpledges.form.PledgeForm;
import org.digijava.module.translation.util.ContentTranslationUtil;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * 
 * @author Diego Dimunzio
 * 
 */
public class PledgesEntityHelper {
    private static Logger logger = Logger.getLogger(PledgesEntityHelper.class);
    
    public static <T> List<T> fetchEntities(Class<?> clazz, Object... values)
    {
        int n = values.length;      
        if (n % 2 == 1)
            throw new RuntimeException("even number of arguments expected!");
        
        StringBuilder queryBld = new StringBuilder("SELECT p FROM " + clazz.getName() + " p WHERE (1 = 1)");
        for(int i = 0; i < n / 2; i++) 
        {
            String fieldName = (String) values[2 * i];
            queryBld.append(" AND (p." + fieldName + " = ?)");
        }
        
        Query query = PersistenceManager.getSession().createQuery(queryBld.toString());
        for(int i = 0; i < n / 2; i++)
            setParameter(query, i, values[i * 2 + 1]);
        return new ArrayList<T>(query.list());
    }
    
    /**
     * function exists because for some reasons query.setParameter(int i, Object val) leads to Hibernate crashing in query.list()
     * @param query
     * @param i
     * @param val
     */
    private static void setParameter(Query query, int i, Object val)
    {
        if (val instanceof Long)
            query.setLong(i, (Long) val);
        else
            if (val instanceof String)
                query.setString(i, (String) val);
        else
            query.setParameter(i, val); // hope for the best
    }
    
    public static List<FundingPledges> getPledges()
    {
        return fetchEntities(FundingPledges.class);
    }
    
    public static List<AmpFundingDetail> getFundingRelatedToPledges(FundingPledges pledge)
    {
        return fetchEntities(AmpFundingDetail.class, "pledgeid", pledge.getId());
    }
    
    public static List<FundingPledges> getPledgesByDonorGroup(Long donorGrpId)
    {
        return fetchEntities(FundingPledges.class, "organizationGroup", donorGrpId);
    }
    
    /**
     * not used anywhere (but still testcased).
     * Scheduled to be removed if no usage for it arises
     * @param donorid
     * @return
     */
    @Deprecated
    public static List<FundingPledges> getPledgesByDonor(Long donorid)
    {
        return fetchEntities(FundingPledges.class, "organization", donorid);
    }   

    /**
     * not used anywhere (but still testcased).
     * Scheduled to be removed if no usage for it arises
     * @param donorid
     * @return
     */
    @Deprecated
    public static List<FundingPledges> getPledgesByDonorAndTitle(Long donorid, String title)
    {
        return fetchEntities(FundingPledges.class, "organization", donorid, "title.value", title);
    }
    
    public static FundingPledges getPledgesById(Long id)
    {
        List<FundingPledges> fp = fetchEntities(FundingPledges.class, "id", id);
        return fp.size() == 0 ? null : fp.get(0);
    }
    
    public static FundingPledges getPledgesByFreeTextName(String name){
        List<FundingPledges> fp = fetchEntities(FundingPledges.class, "titleFreeText", name);
        return fp.size() == 0 ? null : fp.get(0);
    };
    
    public static List<FundingPledgesDetails> getPledgesDetails(Long pledgeid)
    {
        return fetchEntities(FundingPledgesDetails.class, "pledgeid", pledgeid);
    }
    
    public static List<FundingPledgesLocation> getPledgesLocations(Long pledgeid)
    {
        return fetchEntities(FundingPledgesLocation.class, "pledgeid", pledgeid);
    }
    
    public static List<FundingPledgesProgram> getPledgesPrograms(Long pledgeid)
    {
        return fetchEntities(FundingPledgesProgram.class, "pledgeid", pledgeid);
    }
    
    public static List<FundingPledgesSector> getPledgesSectors(Long pledgeid)
    {
        return fetchEntities(FundingPledgesSector.class, "pledgeid", pledgeid);
    }
    
    public static boolean useFreeText(){
        return FeaturesUtil.isVisibleField("Use Free Text");
    };
    
    /**
     * iterates a Collection and removed everything in it
     * @param session
     * @param entities
     */
    public static void deleteAllEntities(Session session, Collection<?> entities)
    {
        if (entities == null)
            return;
        
        for(Object obj:entities)
        {
            session.delete(obj);
        }
    }
    
    public static void removePledge(FundingPledges pledge) throws DgException
    {
        try {
            Session session = PersistenceManager.getSession();

            deleteAllEntities(session, getPledgesSectors(pledge.getId()));
            deleteAllEntities(session, getPledgesLocations(pledge.getId()));
            deleteAllEntities(session, getPledgesPrograms(pledge.getId()));
            deleteAllEntities(session, getPledgesDetails(pledge.getId()));
            
            for(AmpFundingDetail detail:getFundingRelatedToPledges(pledge))
            {
                detail.setPledgeid(null);
                session.update(detail);             
            }
            session.delete(pledge);
        } catch (HibernateException e) {
            logger.error("Error deleting pledge", e);
            throw new DgException("error deleting pledge");
        }
    }
            
    public static AmpOrganisation getOrganizationById(Long id)
    {
        return (AmpOrganisation) PersistenceManager.getSession().load(AmpOrganisation.class, id);
    }
    
    public static AmpOrgGroup getOrgGroupById(Long id)
    {
        return (AmpOrgGroup) PersistenceManager.getSession().load(AmpOrgGroup.class, id);
    }
    
    /**
     * get pledge name for autocomplete box 
     * @return
     * @throws Exception
     */
    @Deprecated
    public static String[] getPledgeNames() throws Exception
    {
        List<String> rawPledgeNames = (List<String>) PersistenceManager.getSession().createQuery(
                "select distinct(pl.title) from " + FundingPledges.class.getName() + " pl ").list();
        
        String[] retValue = new String[rawPledgeNames.size()];
        int i = 0;
        for(String title : rawPledgeNames)
        {
            if(title != null){
                title = title.replace('\n', ' ');
                title = title.replace('\r', ' ');
                title = title.replace("\\", "");
            }
            retValue[i] = title;
            i++;
        }       
        return retValue;
    }
}

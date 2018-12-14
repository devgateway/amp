package org.digijava.module.aim.util;
        
import java.sql.SQLException;
import java.text.Collator;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.viewfetcher.InternationalizedModelDescription;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.helper.ActivityIndicator;
import org.digijava.module.aim.helper.AllPrgIndicators;
import org.digijava.module.aim.helper.AmpPrgIndicator;
import org.digijava.module.aim.helper.AmpPrgIndicatorValue;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.IndicatorThemeBean;
import org.digijava.module.translation.util.ContentTranslationUtil;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.hibernate.*;
import org.hibernate.type.IntegerType;
        
        
        /**
 * Indicator utilities.
 * Methods for manipulating {@link AmpIndicator}, {@link AmpIndicatorValue},
 * {@link IndicatorConnection} and all its subclasses.
 * Also indicator relation management methods can be found here. 
 *
 */
public class IndicatorUtil {

    private static Logger logger = Logger.getLogger(IndicatorUtil.class);
    
    
    /**
     * Loads Indicator from db.
     * NULL if nothing found with specified ID.
     * @param id primary key values of AmpIndicator
     * @return {@link AmpIndicator} object loaded from db.
     * @throws DgException
     */
    public static AmpIndicator getIndicator(Long id) throws DgException{
        Session session = PersistenceManager.getRequestDBSession();
        AmpIndicator indicator=null;
        try {
            indicator = (AmpIndicator)session.load(AmpIndicator.class, id);
        } catch (ObjectNotFoundException e) {
            logger.debug("indicator with "+id+"not found");
        } catch (Exception e) {
            throw new DgException("Cannot load indicator",e);
        }
        return indicator;
    }
    
    /**
     * Saves {@link AmpIndicator} or updates in db.
     * @param indicator object.
     * @throws DgException if anything goes wrong rollback is attempted.
     */
    public static void saveIndicator(AmpIndicator indicator) throws DgException {
        Session session = PersistenceManager.getRequestDBSession();
        Transaction tx=null;
        try {
//beginTransaction();
            session.saveOrUpdate(indicator);
            //tx.commit();
        } catch (HibernateException e) {
            logger.error("Error saving indicator",e);
            if (tx!=null){
                try {
                    tx.rollback();
                } catch (Exception ex) {
                    throw new DgException("Cannot rallback save Indicator actioin",ex);
                }
                throw new DgException("Cannot save Indicator!",e);
            }
        }
    }



    public static AmpIndicatorSubgroup getIndicatorSubGroup(Long id) throws DgException {
        Session session = PersistenceManager.getRequestDBSession();
        AmpIndicatorSubgroup indicator = null;
        try {
            indicator = (AmpIndicatorSubgroup) session.load(AmpIndicatorSubgroup.class, id);
        } catch (ObjectNotFoundException e) {
            logger.debug("indicator with " + id + "not found");
        } catch (Exception e) {
            throw new DgException("Cannot load indicator", e);
        }
        return indicator;
    }


        
     /**
     * Searches database for {@link AmpIndicator} with given name.
     * Returns true if such indicator exists otherwise returns false
     * @param indicator object.
     * @throws DgException if anything goes wrong rollback is attempted.
     * @return true or false
     */
    public static boolean validateIndicatorName(AmpIndicator indicator) throws DgException {

        boolean exists = true;

        try {
            Session session = PersistenceManager.getRequestDBSession();
            String indicatorName = InternationalizedModelDescription.getForProperty(AmpIndicator.class, "name").getSQLFunctionCall("ind.indicatorId");
            String qrString = "Select ind  from " + AmpIndicator.class.getName() + " ind where " + indicatorName + "=:name ";
            
            Long indicatorId = indicator.getIndicatorId();
            if (indicatorId != null && indicatorId != 0) {
                qrString += "and ind.indicatorId !=:id ";
            }
            Query q=session.createQuery(qrString);
            q.setString("name", indicator.getName());
             if (indicatorId != null && indicatorId != 0) {
               q.setLong("id",indicatorId);
            }
            List result=q.list();
            if(result.size()==0){
                exists=false;
            }
            

        } catch (HibernateException e) {
            logger.error("Error saving indicator", e);

        }
        return exists;
    }
    
    /**
     * Removes {@link AmpIndicator} from db.
     * Also Indicator values will be deleted because of AmpIndicator.hbm.xml mappings.
     * @param id primary key of indicator
     * @throws DgException
     */
    public static void deleteIndicator(Long id) throws DgException{
        Session session=PersistenceManager.getRequestDBSession();
        Transaction tx=null;
        try {
//beginTransaction();
            //indicator.getSectors().clear();
            //session.update(indicator);
            AmpIndicator indicator = (AmpIndicator)session.load(AmpIndicator.class, id);

            //Indicator values for themes.
            if (indicator.getValuesTheme()!=null){
                for (IndicatorConnection indicatorValues : getAllConnectionsOfIndicator(indicator)) {
                    try {
                        session.delete(indicatorValues);
                    } catch (HibernateException e) {
                        e.printStackTrace();
                    }
                }
                indicator.getValuesTheme().clear();
            }

            session.delete(indicator);
            //tx.commit();
        } catch (HibernateException e) {
            logger.error("caannot remove Indicator!",e);
            if (tx!=null){
                try {
                    tx.rollback();
                } catch (HibernateException e1) {
                    throw new DgException("Cannot rollback delete AmpIndicator action");
                }
            }
            throw new DgException("Cannot delete AmpIndicator");
        }
        
    }

    /**
     * Recursively all indicators of the theme object.
     * If childrenToo parameter is true then all indicators assigned to all sub themes at all levels of the specified theme are also add to the results.
     * If this parameter is  false then only specified theme indicators are returned.
     * @param prog
     * @param childrenToo
     * @return
     * @throws DgException
     */
    public static Set<IndicatorTheme> getIndicators(AmpTheme prog, boolean childrenToo) throws DgException{
        Set<IndicatorTheme> indicators = new TreeSet<IndicatorTheme>();
        if (prog.getIndicators() != null) {
            indicators.addAll(prog.getIndicators());
        }
        if (childrenToo) {
            Collection<AmpTheme> children = ProgramUtil.getSubThemes(prog.getAmpThemeId());
            if (children != null) {
                for (AmpTheme child : children) {
                    Set<IndicatorTheme> subIndicators = getIndicators(child, childrenToo);
                    indicators.addAll(subIndicators);
                }
            }
        }
        return indicators;
    }


    /**
     * Returns all connections objects for indicator.
     * connection objects are for both {@link AmpTheme} and {@link AmpActivity}
     * @param indicator
     * @return
     * @throws DgException
     */
    @SuppressWarnings("unchecked")
    public static List<IndicatorConnection> getAllConnectionsOfIndicator(AmpIndicator indicator) throws DgException{
        List<IndicatorConnection> result=null;
        Long indicatorId=indicator.getIndicatorId();
        Session session=PersistenceManager.getRequestDBSession();
        String oql="from "+IndicatorConnection.class.getName()+" c where c.indicator.indicatorId=:indId";
        try {
            Query query=session.createQuery(oql);
            query.setLong("indId", indicatorId);
            result=query.list();
        } catch (HibernateException e) {
            throw new DgException("Cannot search connections for indicators",e);
        }
        return result;
    }
    
            
    /**
     * Loads IndicatorTheme connection bean.
     * NULL if not found.
     * @param connId
     * @return bean that represents connection between AmpTheme and Indicator - {@link IndicatorTheme}
     * @throws DgException
     */
    public static IndicatorTheme getConnectionToTheme(Long connId) throws DgException{
        Session session=PersistenceManager.getRequestDBSession();
        IndicatorTheme conn=null;
        try {
            conn=(IndicatorTheme)session.load(IndicatorTheme.class, connId);
        } catch (ObjectNotFoundException e) {
            logger.debug("Cannot fined IndicatorTheme with Id="+connId);
        } catch (Exception ex){
            throw new DgException("Cannot load connection to theme (IndicatorTheme) with id "+connId,ex);
        }
        return conn;
    }
    
    /**
     * Loads IdnicatorActivity connection bean by ID.
     * If nothing found NULL is returned.
     * @param connId
     * @return connection bean or NULL
     * @throws DgException
     */
    public static IndicatorActivity getConnectionToActivity(Long connId) throws DgException{
        Session session = PersistenceManager.getRequestDBSession();
        IndicatorActivity result=null;
        try {
            result= (IndicatorActivity)session.load(IndicatorActivity.class, connId);
        } catch (ObjectNotFoundException e) {
            logger.debug("Activity-Indicator conenction with ID="+connId+" not found",e);
        } catch (Exception ex){
            throw new DgException("cannot load IndicatorActivity with id="+connId,ex);
        }
        return result;
    }

    /**
     * Loads sector indicator connection by its ID.
     * @param connId
     * @return
     * @throws DgException
     */
    public static IndicatorSector getConnectionToSector(Long connId) throws DgException{
        Session session = PersistenceManager.getRequestDBSession();
        IndicatorSector result=null;
        try {
            result= (IndicatorSector)session.load(IndicatorSector.class, connId);
        } catch (ObjectNotFoundException e) {
            logger.debug("Sector-Indicator conenction with ID="+connId+" not found",e);
        } catch (Exception ex){
            throw new DgException("cannot load IndicatorSector with id="+connId,ex);
        }
        return result;
    }
        
        /**
     * Returns any indicator connection type by its id and class.
     * @param <E>
     * @param connId
     * @param clazz
     * @return
     * @throws DgException
     */
    @SuppressWarnings("unchecked")
    public static <E extends IndicatorConnection> E getConnection(Long connId,Class<E> clazz)throws DgException{
        Session session = PersistenceManager.getRequestDBSession();
        E result=null;
        try {
            result= (E)session.load(clazz, connId);
        } catch (ObjectNotFoundException e) {
            logger.debug("Sector-Indicator conenction with ID="+connId+" not found",e);
        } catch (Exception ex){
            throw new DgException("cannot load IndicatorSector with id="+connId,ex);
        }
        return result;
    }
    
    /**
     * Removes connection bean dropping connection between indicator and program or activity.
     * Indicator itself is unaffected but values for this connections will be deleted with connection bean.
     * @param conenctionId id of the connection bean.
     * @throws DgException
     */
    public static IndicatorTheme removeConnection(Long connectionId)throws DgException{
        IndicatorTheme con = getConnectionToTheme(connectionId);
        removeConnection(con);
        return con;
    }
    
    /**
     * Removes connection bean dropping connection between indicator and program or activity.
     * Indicator itself is unaffected but values for this connections will be deleted with connection bean.
     * @param conn
     * @throws DgException
     */
    public static void removeConnection(IndicatorConnection conn)throws DgException{
        Session session=PersistenceManager.getRequestDBSession();
        Transaction tx=null;
        try {
//beginTransaction();
            session.delete(conn);
            //tx.commit();
        } catch (HibernateException e) {
            if (tx!=null){
                try {
                    tx.rollback();
                } catch (HibernateException e1) {
                    throw new DgException("Cannot rollback indicator connection deletion operation!",e1);
                }
                throw new DgException("Cannot delete indicator connection bean ",e);
            }
        }
    }
    
    public static Map<String, Long> getIndicatorNamesToIds(Set<IndicatorActivity> vals) {
        Map<String, Long> result = new HashMap<String, Long>();
        for (IndicatorActivity item : vals) {
            result.put(item.getIndicator().getName(), item.getIndicator().getIndicatorId());
        }
        return result;
    }
    
    
    /**
     * Loads all indicators from DB.
     * @return list of {@link AmpIndicator} objects
     * @throws DgException
     */
    @SuppressWarnings("unchecked")
    public static List<AmpIndicator> getAllIndicators() throws DgException{
        Session session = PersistenceManager.getRequestDBSession();
        List result=null;
        try {
            result=session.createQuery("from "+AmpIndicator.class.getName()).list();
        } catch (HibernateException e) {
            throw new DgException("Cannot load all indicators!",e);
        }
        return result;
    }

    /**
     * Assigns Indicator to program (AmpTheme).
     * @see #assignIndicatorToTheme(AmpTheme, AmpIndicator)
     * @param themeId
     * @param indicatorId
     * @throws DgException
     */
    
    
    
    public static void assignIndicatorToTheme(Long themeId, Long indicatorId) throws DgException{
        AmpTheme theme=ProgramUtil.getThemeById(themeId);
        AmpIndicator indicator=IndicatorUtil.getIndicator(indicatorId);
        assignIndicatorToTheme(theme, indicator);
    }
    
    
    
    
    /**
     * Assigns indicator to program (theme).
     * Actually this creates new {@link IndicatorTheme} object in db 
     * which is subclass of {@link IndicatorConnection} db bean.
     * @param theme
     * @param indicator
     * @throws DgException
     */
    public static IndicatorTheme assignIndicatorToTheme(AmpTheme theme, AmpIndicator indicator) throws DgException{
        IndicatorTheme connection=new IndicatorTheme();
        connection.setIndicator(indicator);
        connection.setTheme(theme);
        Session sessioin=PersistenceManager.getRequestDBSession();
        Transaction tx=null;
        try {
//beginTransaction();
            sessioin.save(connection);
            //tx.commit();
//session.flush();
            return connection;
        } catch (HibernateException e) {
            if (tx!=null){
                try {
                    tx.rollback();
                } catch (HibernateException e1) {
                    throw new DgException("Cannot rollback create indicator-theme connection action",e1);
                }
            }
            throw new DgException("Cannot create indicator-theme connection ",e);
        }
    }

    public static IndicatorActivity assignIndicatorToActivity(AmpActivityVersion  activity, AmpIndicator indicator) throws DgException{
        IndicatorActivity connection=new IndicatorActivity();
        connection.setIndicator(indicator);
        connection.setActivity(activity);
        // TODO replace all lines below awith: return saveConnectionToActivity(connection);
        Session sessioin=PersistenceManager.getRequestDBSession();
        Transaction tx=null;
        try {
//beginTransaction();
            sessioin.save(connection);
            //tx.commit();
//session.flush();
            return connection;
        } catch (HibernateException e) {
            if (tx!=null){
                try {
                    tx.rollback();
                } catch (HibernateException e1) {
                    throw new DgException("Cannot rollback create indicator-activity connection action",e1);
                }
            }
            throw new DgException("Cannot create indicator-activity connection ",e);
        }
    }

    /**
     * 
     * @param connection
     * @return
     * @throws DgException
     */
    public static IndicatorActivity saveConnectionToActivity(IndicatorActivity connection, Session hbSession) throws DgException{
        Transaction tx=null;
        try {
//beginTransaction();
            hbSession.saveOrUpdate(connection);
            //tx.commit();
            return connection;
        } catch (HibernateException e) {
            e.printStackTrace();
            if (tx!=null){
                try {
                    tx.rollback();
                } catch (HibernateException e1) {
                    throw new DgException("Cannot rollback create indicator-activity connection action",e1);
                }
            }
            throw new DgException("Cannot create indicator-activity connection ",e);
        }
    }

    /**
     * Saves any IndicatorConnection to db.
     * @param <E>
     * @param connection
     * @return
     * @throws DgException
     */
    public static <E extends IndicatorConnection> E saveIndicatorConnection(E connection) throws DgException{
        Session sessioin=PersistenceManager.getRequestDBSession();
        Transaction tx=null;
        try {
//beginTransaction();
            sessioin.saveOrUpdate(connection);
            //tx.commit();
            return connection;
        } catch (HibernateException e) {
            if (tx!=null){
                try {
                    tx.rollback();
                } catch (HibernateException e1) {
                    throw new DgException("Cannot rollback create indicator connection action",e1);
                }
            }
            throw new DgException("Cannot create indicator connection ",e);
        }
    }
    
    /**
     * Loads all indictaor connections of specified type.
     * @param <E>
     * @param clazz
     * @return
     * @throws DgException
     */
    @SuppressWarnings("unchecked")
    public static <E extends IndicatorConnection> List<E> getAllConnections(Class<E> clazz)throws DgException{
        List<E> result = new ArrayList<E>();
        Session session=PersistenceManager.getRequestDBSession();
        String oql = "from "+clazz.getName();
        try {
            Query query = session.createQuery(oql);
            result = (List<E>)query.list();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new DgException("Cannot load indicator connection",e);
        }
        return result;
    }
    
    /**
     * Return all indicators (Not connection beans) assigned to specified Theme 
     * @param themeId primary key of the theme who's indicators should be returned.
     * @return set of {@link AmpIndicator} beans.
     * @throws DgException
     */
    public static Set<AmpIndicator> getThemeIndicators(Long themeId) throws DgException{
        //TODO no need in his method. or retrive ampTheme and get list of connections from it.
        Session session=PersistenceManager.getRequestDBSession();
        Set<AmpIndicator> result=null;
        String oql="from "+IndicatorTheme.class.getName()+" ti where ti.theme.ampThemeId=:themeId";
        try {
            Query query=session.createQuery(oql);
            query.setLong("themeId", themeId);
            List resultList=query.list();
            if (resultList!=null && resultList.size()>0){
                result=new HashSet<AmpIndicator>();
                for (Iterator resIter = resultList.iterator(); resIter.hasNext();) {
                    IndicatorTheme themeConnection = (IndicatorTheme) resIter.next();
                    result.add(themeConnection.getIndicator());
                }
            }
        } catch (HibernateException e) {
            throw new DgException("cannot load Theme("+themeId+") indicators!",e);
        }
        return result;
    }
    
    public static Set<IndicatorTheme> getIndicatorThemeConnections(Long themeId) throws DgException{
        AmpTheme theme=ProgramUtil.getThemeById(themeId);
        return getIndicatorThemeConnections(theme);
    }

    public static Set<IndicatorTheme> getIndicatorThemeConnections(AmpTheme theme){
        return theme.getIndicators();
    }
    
    /**
     * loads Indicator value for the specified connection and indicator Id;
     * @param indicatorValueId
     * @param connectionId
     * @return
     * @throws Exception
     * @author Dare Roinishvili
     */
    public static AmpIndicatorValue loadAmpIndicatorValue (Long indicatorValueId, Long connectionId) throws Exception{
        Session session=PersistenceManager.getRequestDBSession();               
        String queryStr=null;
        Query qry=null;
        AmpIndicatorValue ampIndValue=null;
        try {
            queryStr = "from " + AmpIndicatorValue.class.getName() +
            " indVal where indVal.indValId=:indicatorId and indVal.indicatorConnection.id=:connId";
         qry = session.createQuery(queryStr);
         qry.setParameter("indicatorId",indicatorValueId);
         qry.setParameter("connId",connectionId );
         ampIndValue =(AmpIndicatorValue) qry.uniqueResult();   
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("cannot load Indicator value",e);           
        }
        return ampIndValue;
    }
        
        /**
     * loads Indicator value for the specified indicatorValue Id;
     * @param indicatorValueId
     * @return Indicator value
     * @throws DgException
     */
    public static AmpIndicatorValue getAmpIndicatorValue (Long indicatorValueId) throws DgException{
        Session session=PersistenceManager.getRequestDBSession();               
        try {
    
                 AmpIndicatorValue ampIndValue =(AmpIndicatorValue) session.load(AmpIndicatorValue.class, indicatorValueId);    
                 return ampIndValue;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DgException("cannot load Indicator value",e);         
        }
        
    }
    
    /**
     * removes ampIndicatorValue and AmpLocation from the db
     * @param indicatorValueId
     * @param connectionId
     * @throws Exception
     * @author Dare Roinishvili
     */
    public static void removeProgramIndicatorValue(Long indicatorValueId, Long connectionId) throws Exception{
        Session session=PersistenceManager.getRequestDBSession();
        Transaction tx=null;                
        AmpIndicatorValue ampIndValue;
        try {
            ampIndValue=loadAmpIndicatorValue(indicatorValueId,connectionId);
//beginTransaction();
             //deleting AmpLocation
            AmpLocation ampLocation=ampIndValue.getLocation();          
            if(ampLocation!=null){          
                session.delete(ampLocation);
            }
            
        //deleting AmpIndicatorValue        
         session.delete(ampIndValue);       
         //tx.commit();
//session.flush();
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("cannot delete Indicator value",e); 
        }
    }
    
    
    /**
     * Update {@link IndicatorTheme} connection bean.
     * To create new in db, use {@link #assignIndicatorToTheme(AmpTheme, AmpIndicator)}
     * @param connection
     * @throws DgException
     */
    public static void updateThemeConnection(IndicatorTheme connection) throws DgException{
        Session session=PersistenceManager.getRequestDBSession();
        Transaction tx=null;
        try {
//beginTransaction();
            Set<AmpIndicatorValue> indValues=connection.getValues();                    
            if(indValues!=null && indValues.size()>0){
                Iterator<AmpIndicatorValue> iter=indValues.iterator();
                while(iter.hasNext()){
                    AmpIndicatorValue indicatorValue=iter.next();
                    AmpLocation location=indicatorValue.getLocation();
                    if (location!=null){
                        if(location.getAmpLocationId()==null){
                            session.save(location);
                        }else {
                            AmpLocation oldLocation=(AmpLocation) session.load(AmpLocation.class, location.getAmpLocationId());
                            oldLocation.setDescription(location.getDescription());
                            oldLocation.setGisCoordinates(location.getGisCoordinates());
                            oldLocation.setIso3Code(location.getIso3Code());
                            oldLocation.setLanguage(location.getLanguage());
                            oldLocation.setName(location.getName());
                            oldLocation.setVersion(location.getVersion());
                            //indicatorValue.setLocation(oldLocation);                                  
                            session.update(oldLocation);
                        }
                        
                    }
                }
            }
            session.update(connection);
            //tx.commit();
        } catch (HibernateException e) {
            if (tx!=null){
                try {
                    tx.rollback();
                } catch (HibernateException e1) {
                    throw new DgException("Cannot rollback update operation of IndicatorTheme bean",e);
                }
            }
            throw new DgException("Cannot update IndicatorTheme bean",e);
        }
    }
    
    /**
     * Tries to find connection bean between activity and indicator.
     * If not found NULL is returned.
     * @param activity
     * @param indicator
     * @return
     * @throws DgException
     */
    public static IndicatorActivity findActivityIndicatorConnection(AmpActivityVersion activity,AmpIndicator indicator) throws DgException{
        IndicatorActivity result=null;
        //these two line may throw null pointer exception, but don't fix here, fix caller of this method to not path null here!
        Long activityId=activity.getAmpActivityId();
        Long indicatorId=indicator.getIndicatorId();
        Session session=PersistenceManager.getRequestDBSession();
        String oql="from "+IndicatorActivity.class.getName()+" conn ";
        oql+=" where conn.activity.ampActivityId=:actId and conn.indicator.indicatorId=:indicId";
        try {
            Query query=session.createQuery(oql);
            query.setLong("actId", activityId);
            query.setLong("indicId", indicatorId);
            result=(IndicatorActivity)query.uniqueResult();
        } catch (ObjectNotFoundException e) {
            logger.debug("Cannot find conenction for activity("+activityId+") and indicator("+indicatorId+")!");
        } catch (HibernateException e) {
            throw new DgException("Error searching conenction for activity("+activityId+") and indicator("+indicatorId+")!",e);
        }
        return result;
    }
    
    /**
     * Tries to find connection bean between activity and indicator.
     * If not found NULL is returned.
     * @param activity
     * @param indicator
     * @return
     * @throws DgException
     */
    public static IndicatorActivity findActivityIndicatorConnection(AmpActivityVersion activity,AmpIndicator indicator, Session session) throws DgException{
        IndicatorActivity result=null;
        //these two line may throw null pointer exception, but don't fix here, fix caller of this method to not path null here!
        Long activityId=activity.getAmpActivityId();
        Long indicatorId=indicator.getIndicatorId();
        String oql="from "+IndicatorActivity.class.getName()+" conn ";
        oql+=" where conn.activity.ampActivityId=:actId and conn.indicator.indicatorId=:indicId";
        try {
            Query query=session.createQuery(oql);
            query.setLong("actId", activityId);
            query.setLong("indicId", indicatorId);
            result=(IndicatorActivity)query.uniqueResult();
        } catch (ObjectNotFoundException e) {
            logger.debug("Cannot find conenction for activity("+activityId+") and indicator("+indicatorId+")!");
        } catch (HibernateException e) {
            throw new DgException("Error searching conenction for activity("+activityId+") and indicator("+indicatorId+")!",e);
        }
        return result;
    }
    /**
     * Tries to find connection bean between activity and indicator.
     * If not found NULL is returned.
     * @param activityId
     * @param indicatorId
     * @return
     * @throws DgException
     */
    public static IndicatorActivity findActivityIndicatorConnection(Long activityId,Long indicatorId) throws DgException{
        IndicatorActivity result=null;
        Session session=PersistenceManager.getRequestDBSession();
        String oql="from "+IndicatorActivity.class.getName()+" conn ";
        oql+=" where conn.activity.ampActivityId=:actId and conn.indicator.indicatorId=:indicId";
        try {
            Query query=session.createQuery(oql);
            query.setLong("actId", activityId);
            query.setLong("indicId", indicatorId);
            result=(IndicatorActivity)query.uniqueResult();
        } catch (HibernateException e) {
            throw new DgException("Error searching conenction for activity("+activityId+") and indicator("+indicatorId+")!",e);
        }
        return result;
    }

    public static void saveActivityIndicatorConnection(ActivityIndicator actInd,AmpTeamMember member) throws DgException {
        IndicatorActivity indAct = null;
        Session session = null;
        Transaction tx = null;
        try {
            session = PersistenceManager.getRequestDBSession();
//beginTransaction();
            indAct = findActivityIndicatorConnection(actInd.getActivityId(), actInd.getIndicatorId());
            Set<AmpIndicatorValue> values = indAct.getValues();
            boolean hasRevisedValue = false;
            if (values != null && !values.isEmpty()) {
                for (AmpIndicatorValue value : values) {
                    if (value.getValueType() == AmpIndicatorValue.BASE) {
                        value.setValue(new Double(actInd.getBaseVal()));
                        value.setComment(actInd.getBaseValComments());
                        value.setValueDate(DateConversion.getDate(actInd.getBaseValDate()));
                    } else {
                        if (value.getValueType() == AmpIndicatorValue.TARGET) {
                            value.setValue(new Double(actInd.getTargetVal()));
                            value.setComment(actInd.getTargetValComments());
                            value.setValueDate(DateConversion.getDate(actInd.getTargetValDate()));

                        } else {
                            if (value.getValueType() == AmpIndicatorValue.REVISED) {
                                value.setValue(new Double(actInd.getRevisedTargetVal()));
                                value.setComment(actInd.getRevisedTargetValComments());
                                value.setValueDate(DateConversion.getDate(actInd.getRevisedTargetValDate()));
                                hasRevisedValue = true;
                            }

                        }
                    }
                }
                if (!hasRevisedValue && actInd.getRevisedTargetValDate() != null) {
                    AmpIndicatorValue indValRevised = null;
                    if (actInd.getRevisedTargetVal() != null) {
                        indValRevised = new AmpIndicatorValue();
                        indValRevised.setValueType(AmpIndicatorValue.REVISED);
                        indValRevised.setValue(new Double(actInd.getRevisedTargetVal()));
                        indValRevised.setComment(actInd.getRevisedTargetValComments());
                        indValRevised.setValueDate(DateConversion.getDate(actInd.getRevisedTargetValDate()));
                        AmpIndicatorRiskRatings risk = null;
                        if (actInd.getRisk() != null) {
                            risk = (AmpIndicatorRiskRatings) session.load(AmpIndicatorRiskRatings.class, actInd.getRisk());
                        }
                        AmpCategoryValue categoryValue = null;
                        if (actInd.getIndicatorsCategory() != null) {
                            categoryValue = (AmpCategoryValue) session.get(AmpCategoryValue.class, actInd.getIndicatorsCategory().getId());
                        }
                        indValRevised.setRisk(risk);
                        indValRevised.setLogFrame(categoryValue);
                        indValRevised.setIndicatorConnection(indAct);
                        indAct.getValues().add(indValRevised);
                    }

                }
            } else {
                //indAct.setValues(new HashSet<AmpIndicatorValue>());
                AmpIndicatorRiskRatings risk = null;
                if (actInd.getRisk() != null) {
                    risk = (AmpIndicatorRiskRatings) session.load(AmpIndicatorRiskRatings.class, actInd.getRisk());
                }
                AmpCategoryValue categoryValue = null;
                if (actInd.getIndicatorsCategory() != null) {
                    categoryValue = (AmpCategoryValue) session.get(AmpCategoryValue.class, actInd.getIndicatorsCategory().getId());
                }
                AmpIndicatorValue indValTarget = null;
                if (actInd.getTargetVal() != null) {
                    indValTarget = new AmpIndicatorValue();
                    indValTarget.setValueType(AmpIndicatorValue.TARGET);
                    indValTarget.setValue(new Double(actInd.getTargetVal()));
                    indValTarget.setComment(actInd.getTargetValComments());
                    indValTarget.setValueDate(DateConversion.getDate(actInd.getTargetValDate()));
                    indValTarget.setRisk(risk);
                    indValTarget.setLogFrame(categoryValue);
                    indValTarget.setIndicatorConnection(indAct);
                    indAct.getValues().add(indValTarget);
                }
                AmpIndicatorValue indValBase = null;
                if (actInd.getBaseVal() != null) {
                    indValBase = new AmpIndicatorValue();
                    indValBase.setValueType(AmpIndicatorValue.BASE);
                    indValBase.setValue(new Double(actInd.getBaseVal()));
                    indValBase.setComment(actInd.getBaseValComments());
                    indValBase.setValueDate(DateConversion.getDate(actInd.getBaseValDate()));
                    indValBase.setRisk(risk);
                    indValBase.setLogFrame(categoryValue);
                    indValBase.setIndicatorConnection(indAct);
                    indAct.getValues().add(indValBase);
                }

            }
            if (ActivityVersionUtil.isVersioningEnabled()) {
                AmpActivityVersion act = (AmpActivityVersion) session.load(AmpActivityVersion.class, actInd.getActivityId());
                Hibernate.initialize(act);
                Hibernate.initialize(act.getActivityContacts());
                AmpActivityGroup tmpGroup = act.getAmpActivityGroup();
                act = ActivityVersionUtil.cloneActivity(act);
                ContentTranslationUtil.cloneTranslations(act);
                act.setAmpActivityId(null);
                                
                if (tmpGroup == null) {
                    //we need to create a group for this activity
                    tmpGroup = new AmpActivityGroup();
                    
                }
                tmpGroup.setAmpActivityLastVersion(act);
                session.saveOrUpdate(tmpGroup);
                act.setAmpActivityGroup(tmpGroup);

                org.dgfoundation.amp.onepager.util.ActivityUtil.saveContacts(act, session, false, member);
                Set<IndicatorActivity> indicators=act.getIndicators();
                session.save(act);
                Set<AmpIndicatorValue> newValues;
                for (IndicatorActivity indicator : indicators) {
                    if (indAct.getIndicator().getIndicatorId().equals(indicator.getIndicator().getIndicatorId())) {
                        newValues=indAct.getValues();
                    }
                    else{
                        newValues=new HashSet<AmpIndicatorValue>(indicator.getValues());
                    }
                    indicator.getValues().clear();
                    for (AmpIndicatorValue value : newValues) {
                        AmpIndicatorValue newValue = (AmpIndicatorValue) value.clone();
                        newValue.setIndicatorConnection(indicator);
                        newValue.setIndValId(null);
                        indicator.getValues().add(newValue);
                    }

                    session.saveOrUpdate(indicator);
                }
                
                
            } else {
                session.saveOrUpdate(indAct);
            }
           session.flush();
            //tx.commit();
        } catch (Exception e) {
            logger.error("error", e);

        }
    }

    
    /**
     * Loads all indicators for activity.
     * NULL if there are no Indicators
     * TODO correct this method
     * @param activityId
     * @return
     * @throws DgException
     */
    public static Set<AmpIndicator> getActivityIndicators(Long activityId) throws DgException{
        Set<AmpIndicator> result=null;
        Session sesison=PersistenceManager.getRequestDBSession();
        String indicatorName = InternationalizedModelDescription.getForProperty(AmpIndicator.class, "name").getSQLFunctionCall("indi.indicatorId");
        String oql="select indi from "+AmpIndicator.class.getName()+" indi ";
        oql+="where indi.valuesActivity.activity.ampActivityId =:actId ";
        oql+="order by " + indicatorName;
        try {
            Query query=sesison.createQuery(oql);
            query.setLong("actId", activityId);
            List resultList=query.list();
            if (result!=null && result.size()>0){
                result=new HashSet<AmpIndicator>();
                for (Iterator iterator = resultList.iterator(); iterator.hasNext();) {
                    AmpIndicator indicator = (AmpIndicator) iterator.next();
                    result.add(indicator);
                }
            }
        } catch (HibernateException e) {
            throw new DgException("Cannot load indicators for Activity with id "+activityId,e);
        }
        return result;
    }

    
    /**
     * Loads all indicators for activity.
     * NULL if there are no Indicators
     * TODO correct this method
     * @param activityId
     * @return
     * @throws DgException
     */
    public static Set<IndicatorActivity> getAllIndicatorsForActivity(Long activityId) throws DgException{
        Set<IndicatorActivity> result=null;
        Session sesison=PersistenceManager.getRequestDBSession();
        String oql="select indi from "+IndicatorActivity.class.getName()+" indi ";
        oql+="where indi.activity.ampActivityId =:actId ";
        try {
            Query query=sesison.createQuery(oql);
            query.setLong("actId", activityId);
            List resultList=query.list();
            if (resultList!=null && resultList.size()>0){
                result=new HashSet<IndicatorActivity>();
                for (Iterator iterator = resultList.iterator(); iterator.hasNext();) {
                    IndicatorActivity indicator = (IndicatorActivity) iterator.next();
                    result.add(indicator);
                }
            }
        } catch (HibernateException e) {
            logger.error(e.getMessage(), e);
            throw new DgException("Cannot load indicators for Activity with id "+activityId,e);
        }
        return result;
    }

    /**
     * Returns Indicator helper beans for activity.
     * Used on edit activity action. please remove this when Edit activity is refactored.
     * @param activityId
     * @return set of indicator helper beans.
     * @throws DgException
     */
    public static List<ActivityIndicator> getActivityIndicatorHelperBeans(Long activityId) throws DgException{
        if(activityId == null)
            return null;
        AmpActivityVersion  activity=ActivityUtil.loadActivity(activityId);
        return getActivityIndicatorHelperBeans(activity);
    }
    
    
    /**
     * Returns set of indicator helper beans for activity.
     * @param activity
     * @return
     * @throws DgException
     */
    public static List<ActivityIndicator> getActivityIndicatorHelperBeans(AmpActivityVersion  activity) throws DgException{
        List<ActivityIndicator> result = new ArrayList<ActivityIndicator>();;
        if (activity==null) return null;
        Set<IndicatorActivity> indicators =activity.getIndicators();
        if (indicators!=null && indicators.size()>0){
            for (IndicatorActivity connection : indicators) {
                ActivityIndicator helper=new ActivityIndicator();
                
                helper = createIndicatorHelperBean(connection);

                result.add(helper);
            }
        }
        
        return result;
    }

    /**
     * Returns set of indicator helper beans for IndicatorActivity list.
     * @param indicators
     * @return
     * @throws DgException
     */
    public static List<ActivityIndicator> createActivityIndicatorHelperBeans(Long  activityId) throws DgException{
        List<ActivityIndicator> result = new ArrayList<ActivityIndicator>();
        Set<IndicatorActivity> indicators=getAllIndicatorsForActivity(activityId);
        if (indicators!=null && indicators.size()>0){
            for (IndicatorActivity connection : indicators) {
                ActivityIndicator helper=new ActivityIndicator();
                helper = createIndicatorHelperBean(connection);
                result.add(helper);
            }
        }
        return result;
    }

    
    /**
     * Compares {@link AmpIndicator} objects by 'name' property
     * @author Irakli Kobiashvili
     *
     */
    public static class IndicatorNameComparator implements Comparator<AmpIndicator> {

        public int compare(AmpIndicator indic0, AmpIndicator indic1) {
            return indic0.getName().toLowerCase().compareTo(indic1.getName().toLowerCase());
        }
        
    }
    
    /**
     * Compares {@link IndicatorTheme} objects by its indicators name property.
     * This is possible because IndicatorTheme should always have reference to one {@link AmpIndicator}
     * @author Irakli Kobiashvili
     *
     */
    public static class IndThemeIndciatorNameComparator implements Comparator<IndicatorTheme>{

        public int compare(IndicatorTheme ind0, IndicatorTheme ind1) {
            return ind0.getIndicator().getName().compareTo(ind1.getIndicator().getName());
        }
        
    }
    
    /**
     * Compares {@link IndicatorThemeBean} by their indicator's name
     * @author Dare Roinishvili
     *
     */
    public static class IndThemeBeanComparatorByIndciatorName implements Comparator<IndicatorThemeBean>{

        public int compare(IndicatorThemeBean ind0, IndicatorThemeBean ind1) {
            return ind0.getIndicator().getName().compareTo(ind1.getIndicator().getName());
        }
        
    }

    //TODO INDIC this is stupid temporary solution. risk should be moved to connection but business team does not care about this and we have no time to do this changes.
    public static AmpIndicatorRiskRatings getRisk(IndicatorActivity connection){
        if (connection!=null && connection.getValues()!=null){
            Iterator<AmpIndicatorValue> iter=connection.getValues().iterator();
            if (iter.hasNext()) return iter.next().getRisk();
        }
        return null;
    }
    
    public static AmpIndicatorRiskRatings getRisk(Long id){
        Session sess=null;
        AmpIndicatorRiskRatings retVal=null;
        try {
            sess=PersistenceManager.getRequestDBSession();
            retVal=(AmpIndicatorRiskRatings)sess.get(AmpIndicatorRiskRatings.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }
    
    public static ActivityIndicator createIndicatorHelperBean(IndicatorActivity connection) {
        ActivityIndicator bean=new ActivityIndicator();
        bean.setActivityId(connection.getActivity().getAmpActivityId());
        bean.setIndicatorId(connection.getIndicator().getIndicatorId());
        bean.setIndicatorName(connection.getIndicator().getName());
        bean.setIndicatorCode(connection.getIndicator().getCode());
        if (connection.getIndicator().getIndicatorsCategory()!=null){
            bean.setAmpCategoryValue(connection.getIndicator().getIndicatorsCategory().getValue());
        }else{
            bean.setAmpCategoryValue("Unknown");
        }
        bean.setIndicatorsCategory(connection.getIndicator().getIndicatorsCategory());
        
        //set values to helper bean
        Collection<AmpIndicatorValue> values=connection.getValues();
        if (values!=null){
            for (AmpIndicatorValue value : values) {
                if (value.getValueType()==AmpIndicatorValue.ACTUAL){
                    bean.setActualVal(new Float(value.getValue()));
                    bean.setActualValComments(value.getComment());
                    bean.setActualValDate(DateConversion.convertDateToLocalizedString(value.getValueDate()));
                    bean.setCurrentVal(new Float(value.getValue()));
                    bean.setCurrentValComments(value.getComment());
                    bean.setCurrentValDate(DateConversion.convertDateToLocalizedString(value.getValueDate()));
                    bean.setIndicatorsCategory(value.getLogFrame());
                }
                if (value.getValueType()==AmpIndicatorValue.BASE){
                    bean.setBaseVal(new Float(value.getValue()));
                    bean.setBaseValComments(value.getComment());
                    bean.setBaseValDate(DateConversion.convertDateToString(value.getValueDate()));
                    bean.setIndicatorsCategory(value.getLogFrame());
                }
                if (value.getValueType()==AmpIndicatorValue.TARGET){
                    bean.setTargetVal(new Float(value.getValue()));
                    bean.setTargetValComments(value.getComment());
                    bean.setTargetValDate(DateConversion.convertDateToString(value.getValueDate()));
                    bean.setIndicatorsCategory(value.getLogFrame());
                }
                if (value.getValueType()==AmpIndicatorValue.REVISED){
                    if(value.getValue()!=null){
                        bean.setRevisedTargetVal(new Float(value.getValue()));
                    }
                    bean.setRevisedTargetValComments(value.getComment());
                    if(value.getValueDate()!=null){
                        bean.setRevisedTargetValDate(DateConversion.convertDateToString(value.getValueDate()));
                    }
                    bean.setIndicatorsCategory(value.getLogFrame());
                }
            }
        }
        //if no revised value then use target as revised target 
        if (bean.getRevisedTargetVal()==null){
            bean.setRevisedTargetVal(bean.getTargetVal());
            bean.setRevisedTargetValDate(bean.getTargetValDate());
        }
        
        //calculate progress if possible 
        Float actual=bean.getActualVal();
        Float base=bean.getBaseVal();
        Float target=bean.getRevisedTargetVal();
        float progress=0;
        if (actual!=null && base!=null && target!=null){
            progress=(actual-base)/(target-base);
            if (progress<0){
                progress=0;
            }
        }
        NumberFormat format=NumberFormat.getPercentInstance();
        format.setMaximumFractionDigits(2);
        bean.setProgress(format.format(progress));
        
        AmpIndicatorRiskRatings riskObj=getRisk(connection);
        if(riskObj!=null){
            bean.setRisk(riskObj.getAmpIndRiskRatingsId());
            bean.setRiskName(riskObj.getRatingName());
        }   
        
        return bean;
    }
    
    @Deprecated
    public static ArrayList getAmpIndicator() {
        Session session = null;
        Query q = null;
        AmpIndicator ampIndicator = null;
        ArrayList Indicator = new ArrayList();
        String queryString = null;
        Iterator iter = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String indicatorName = InternationalizedModelDescription.getForProperty(AmpIndicator.class, "name").getSQLFunctionCall("t.indicatorId");
            queryString = " select t from " + AmpIndicator.class.getName()
                    + " t order by " + indicatorName;
            q = session.createQuery(queryString);
            iter = q.list().iterator();

            while (iter.hasNext()) {
                ampIndicator = (AmpIndicator) iter.next();
                Indicator.add(ampIndicator);
            }

        } catch (Exception ex) {
            logger.error("Unable to get Amp indicators names  from database "
                    + ex.getMessage());
        }
        return Indicator;
    }

    @Deprecated
    public static AllPrgIndicators getAmpIndicator(Long indId) {
        Session session = null;
        AllPrgIndicators tempPrgInd = new AllPrgIndicators();

        try {
            session = PersistenceManager.getRequestDBSession();
            AmpIndicator tempInd = (AmpIndicator) session.load(
                    AmpIndicator.class, indId);

            tempPrgInd.setIndicatorId(tempInd.getIndicatorId());

            tempPrgInd.setName(tempInd.getName());
            tempPrgInd.setCode(tempInd.getCode());
            tempPrgInd.setType(tempInd.getType());
            tempPrgInd.setDescription(tempInd.getDescription());
            tempPrgInd.setCreationDate(DateConversion
                    .convertDateToLocalizedString(tempInd.getCreationDate()));
            tempPrgInd.setCategory(tempInd.getCategory());
            tempPrgInd.setSector(tempInd.getSectors());
            //TODO INDIC tempPrgInd.setActivity(tempInd.getActivity());
            
//          tempPrgInd.setActualVal(tempInd.getActualVal());
//          tempPrgInd.setActualValComments(tempInd.getActualValComments());
//          tempPrgInd.setActualValDate(tempInd.getActualValDate());
//          tempPrgInd.setBaseVal(tempInd.getBaseVal());
//          tempPrgInd.setBaseValComments(tempInd.getBaseValComments());
//          tempPrgInd.setBaseValDate(tempInd.getBaseValDate());
//          tempPrgInd.setTargetVal(tempInd.getTargetVal());
//          tempPrgInd.setTargetValComments(tempInd.getTargetValComments());
//          tempPrgInd.setTargetValDate(tempInd.getTargetValDate());
//          tempPrgInd.setRevisedTargetVal(tempInd.getRevisedTargetVal());
//          tempPrgInd.setRevisedTargetValComments(tempInd.getRevisedTargetValComments());
//          tempPrgInd.setRevisedTargetValDate(tempInd.getRevisedTargetValDate());
            tempPrgInd.setRisk(tempInd.getRisk());
            tempPrgInd.setIndicatorsCategory(tempInd.getIndicatorsCategory());
            if(tempInd.isDefaultInd()){
                tempPrgInd.setType("global");
            }
            
//session.flush();
        } catch (Exception e) {
            logger.error("Unable to get the specified Indicator");
            logger.debug("Exception : " + e);
        }
        return tempPrgInd;
    }
    
    @Deprecated
    public static Object getAmpMEIndicatorValue(Long indId, Long actId){
        Session session = null;
        Query q = null;
        String queryString = null;
        Object val=null;
        try {
        if(actId!=null){
        session = PersistenceManager.getRequestDBSession();
        queryString = " select val from "
                + AmpMEIndicatorValue.class.getName()
                + " val where val.indicator=:indId and val.activityId=:actId";
        q = session.createQuery(queryString);
        q.setLong("indId", indId);
        q.setLong("actId", actId);
        val=q.uniqueResult();
        }
        }
        catch (Exception e) {
            logger.error("Unable to get the specified Indicator");
            logger.debug("Exception : " + e);
        }
        return val;
        
    }

    @Deprecated
    public static AllPrgIndicators getAmpIndicator(Long indId, Long actId) {
        Session session = null;
        AllPrgIndicators tempPrgInd = new AllPrgIndicators();
        AmpIndicator tempInd=null;

        try {
            Object val=getAmpMEIndicatorValue(indId, actId);
            if(val!=null){
            AmpMEIndicatorValue value = (AmpMEIndicatorValue) val;
            tempInd = value.getIndicator();
            tempPrgInd.setActualVal(value.getActualVal());
            tempPrgInd.setActualValComments(value.getActualValComments());
            tempPrgInd.setActualValDate(value.getActualValDate());
            tempPrgInd.setBaseVal(value.getBaseVal());
            tempPrgInd.setBaseValComments(value.getBaseValComments());
            tempPrgInd.setBaseValDate(value.getBaseValDate());
            tempPrgInd.setTargetVal(value.getTargetVal());
            tempPrgInd.setTargetValComments(value.getTargetValComments());
            tempPrgInd.setTargetValDate(value.getTargetValDate());
            tempPrgInd.setRevisedTargetVal(value.getRevisedTargetVal());
            tempPrgInd.setRevisedTargetValComments(value
                    .getRevisedTargetValComments());
            tempPrgInd.setRevisedTargetValDate(value.getRevisedTargetValDate());
            }
            else{
                session = PersistenceManager.getRequestDBSession();
                tempInd=(AmpIndicator) session.load(
                        AmpIndicator.class, indId);
            }
            tempPrgInd.setIndicatorId(tempInd.getIndicatorId());
            tempPrgInd.setName(tempInd.getName());
            tempPrgInd.setCode(tempInd.getCode());
            tempPrgInd.setRisk(tempInd.getRisk());
            tempPrgInd.setIndicatorsCategory(tempInd.getIndicatorsCategory());
            
        } catch (Exception e) {
            logger.error("Unable to get the specified Indicator");
            logger.debug("Exception : " + e);
        }
        return tempPrgInd;
    }

    @Deprecated
    public static Collection getAllIndicators(Long ampThemeId) {
        Session session = null;
        AmpTheme tempAmpTheme = null;
        Collection themeInd = new ArrayList();

        try {
            session = PersistenceManager.getRequestDBSession();
            tempAmpTheme = (AmpTheme) session.load(AmpTheme.class, ampThemeId);
            Set themeIndSet = tempAmpTheme.getIndicators();
            Iterator itrIndSet = themeIndSet.iterator();
            while (itrIndSet.hasNext()) {
                AmpIndicator tempThemeInd = (AmpIndicator) itrIndSet.next();
                AmpPrgIndicator tempPrgInd = new AmpPrgIndicator();
                Long ampThemeIndId = tempThemeInd.getIndicatorId();
                tempPrgInd.setIndicatorId(ampThemeIndId);
                tempPrgInd.setName(tempThemeInd.getName());
                tempPrgInd.setCode(tempThemeInd.getCode());
                tempPrgInd.setCreationDate(DateConversion
                        .convertDateToLocalizedString(tempThemeInd.getCreationDate()));
                tempPrgInd.setPrgIndicatorValues(ProgramUtil
                        .getThemeIndicatorValues(ampThemeIndId));
                themeInd.add(tempPrgInd);
            }
        } catch (Exception ex) {
            logger.error("Exception from getThemeIndicators()  "
                    + ex.getMessage());
            ex.printStackTrace(System.out);
        }
        return themeInd;
    }

    @Deprecated
    public static void deleteIndicatorOLD(Long indId) {
        Session session = null;
        Transaction tx = null;
        try {
            session = PersistenceManager.getRequestDBSession();
//beginTransaction();
            AmpIndicator tempindInd = (AmpIndicator) session.load(
                    AmpIndicator.class, indId);

            Collection sect = tempindInd.getSectors();
            if (sect != null && sect.size() > 0) {
                Iterator<AmpSector> sectIter = sect.iterator();
                while (sectIter.hasNext()) {
                    AmpSector sector = sectIter.next();
                    AmpSector tempAmpSector = null;
                    tempAmpSector = (AmpSector) session.load(AmpSector.class,
                            sector.getAmpSectorId());
                    Set ampThemeSet = new HashSet();
                    tempAmpSector.getIndicators().remove(tempindInd);
                    tempindInd.getSectors().remove(tempAmpSector);
                }
            }

            session.delete(tempindInd);
            //tx.commit();
        } catch (Exception e) {
            logger.error("Unable to delete the indicator");
            logger.debug("Exception : " + e);
        }
    }

    public static Collection getAllNonDefaultIndicators() {
        Session session = null;
        Collection col = null;
        Query qry = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select nondefInd from "
                    + AmpIndicator.class.getName()
                    + " nondefInd where nondefInd.defaultInd = false";
            qry = session.createQuery(queryString);
            col = qry.list();
        } catch (Exception e) {
            logger.error("Unable to get non-default indicators");
            logger.debug("Exception : " + e);
            e.printStackTrace();
        }
        return col;
    }

    public static Collection getAllDefaultIndicators(Long ampActivityId) {
        Session session = null;
        Collection<ActivityIndicator> coll = new ArrayList();
        Query qry = null;
        AmpIndicator ampIndicator = null;
        Iterator iter = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select nondefInd from "
                    + AmpIndicator.class.getName()
                    + " nondefInd where nondefInd.defaultInd = true";
            qry = session.createQuery(queryString);
            iter = qry.list().iterator();

            while (iter.hasNext()) {
                ampIndicator = (AmpIndicator) iter.next();
                ActivityIndicator actInd = new ActivityIndicator();
                actInd.setIndicatorId(ampIndicator.getIndicatorId());
                Object val=getAmpMEIndicatorValue(ampIndicator.getIndicatorId(), ampActivityId);
                if(val!=null){
                    AmpMEIndicatorValue value = (AmpMEIndicatorValue) val;
                    actInd.setIndicatorValId(value.getAmpMeIndValId());
                    
                    
                    //================
                    // AMP-2828 by mouhamad
                    String dateFormat = FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GLOBALSETTINGS_DATEFORMAT);
                    dateFormat = dateFormat.replace("m", "M");
                      
                    SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
                        
                    actInd.setActualVal(value.getActualVal());
                    actInd.setActualValComments(value.getActualValComments());
                    actInd.setActualValDate(formatter.format(value.getActualValDate()));
                    

                    actInd.setCurrentVal(value.getActualVal());
                    actInd.setCurrentValComments(value.getActualValComments());
                    actInd.setCurrentValDate(formatter.format(value.getActualValDate()));
                    
                    actInd.setBaseVal(value.getBaseVal());
                    actInd.setBaseValComments(value.getBaseValComments());
                    actInd.setBaseValDate(formatter.format(value.getBaseValDate()));
                    actInd.setTargetVal(value.getTargetVal());
                    actInd.setTargetValComments(value.getTargetValComments());
                    actInd.setTargetValDate(formatter.format(value.getTargetValDate()));
                    actInd.setRevisedTargetVal(value.getRevisedTargetVal());
                    actInd.setRevisedTargetValComments(value
                            .getRevisedTargetValComments());
                    actInd.setRevisedTargetValDate(formatter.format(value.getRevisedTargetValDate()));
                    if(value.getRisk()!=null){
                    actInd.setRisk(value.getRisk().getAmpIndRiskRatingsId());
                    }
                    actInd.setIndicatorsCategory(value.getIndicatorsCategory());
                        
                    //  ===================
                            
                    
                }
                actInd.setIndicatorCode(ampIndicator.getCode());
                actInd.setIndicatorName(ampIndicator.getName());
                //actInd.setIndicatorValId(ampIndicator.getIndicatorId() + 1);
                coll.add(actInd);
            }

        } catch (Exception e) {
            logger.error("Unable to get default indicators");
            logger.debug("Exception : " + e);
            e.printStackTrace();
        }
        return coll;
    }
    
    public static Collection getAllDefaultIndicators()
    {
        Session session = null;
        Collection col = new ArrayList();
        Query qry = null;
        try
        {
            session = PersistenceManager.getRequestDBSession();
    
            String queryString = "select defInd from "
                + AmpIndicator.class.getName()
                + " defInd where defInd.defaultInd = true";
            
            qry = session.createQuery(queryString);
            col = qry.list();
        }
        catch(Exception e)
        {
            logger.error("Unable to get default indicators");
            logger.debug("Exception : " + e);
        }
        return col;
    }

    @Deprecated
    public static Collection getActivityIndicatorsList(Long ampActivityId) {

        Session session = null;
        Query qry = null;
        AmpActivityVersion  tempAmpactivity = null;
        Collection coll = new ArrayList();
        try {
            if(ampActivityId!=null&&ampActivityId>0){
            session = PersistenceManager.getRequestDBSession();
            tempAmpactivity = (AmpActivityVersion) session.load(AmpActivityVersion.class,
                    ampActivityId);
            Set activityIndiSet = tempAmpactivity.getIndicators();
            Iterator itrIndSet = activityIndiSet.iterator();
            while (itrIndSet.hasNext()) {
                AmpIndicator tempThemeInd = (AmpIndicator) itrIndSet.next();
                ActivityIndicator actInd = new ActivityIndicator();
                Long ampThemeIndId = tempThemeInd.getIndicatorId();
                actInd.setIndicatorId(ampThemeIndId);
                actInd.setIndicatorCode(tempThemeInd.getCode());
                actInd.setIndicatorName(tempThemeInd.getName());
                Object val=getAmpMEIndicatorValue(ampThemeIndId,ampActivityId);
                if(val!=null){
                AmpMEIndicatorValue value = (AmpMEIndicatorValue) val;
                actInd.setIndicatorValId(value.getAmpMeIndValId());
                
                
                //================
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    
                actInd.setActualVal(value.getActualVal());
                actInd.setActualValComments(value.getActualValComments());
                actInd.setActualValDate(formatter.format(value.getActualValDate()));
                
                actInd.setCurrentVal(value.getActualVal());
                actInd.setCurrentValComments(value.getActualValComments());
                actInd.setCurrentValDate(formatter.format(value.getActualValDate()));
                
                
                actInd.setBaseVal(value.getBaseVal());
                actInd.setBaseValComments(value.getBaseValComments());
                actInd.setBaseValDate(formatter.format(value.getBaseValDate()));
                actInd.setTargetVal(value.getTargetVal());
                actInd.setTargetValComments(value.getTargetValComments());
                actInd.setTargetValDate(formatter.format(value.getTargetValDate()));
                actInd.setRevisedTargetVal(value.getRevisedTargetVal());
                actInd.setRevisedTargetValComments(value
                        .getRevisedTargetValComments());
                actInd.setRevisedTargetValDate(formatter.format(value.getRevisedTargetValDate()));
                if(value.getRisk()!=null){
                actInd.setRisk(value.getRisk().getAmpIndRiskRatingsId());
                }
                actInd.setIndicatorsCategory(value.getIndicatorsCategory());
                    
                //  ===================
                        
                
                
                
                
                
                }
                actInd.setActivityId(ampActivityId);
                coll.add(actInd);
            }
            }
        }

        catch (Exception ex) {
            logger.error("Exception from getThemeIndicators()  "
                    + ex.getMessage());
            ex.printStackTrace(System.out);
        }
        return coll;

    }

    @Deprecated
    public static String getIndicatorName(Long id) {
        Session session = null;
        AmpIndicator Ind = null;
        String indName = null;
        String indCode = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            Ind = (AmpIndicator) session.load(AmpIndicator.class, id);
            indName = Ind.getName();

        } catch (Exception ex1) {
            logger.error("Unable to get AmpIndicator object : " + ex1);
        }
        return indName;
    }

    @Deprecated
    public static String getIndicatorCode(Long id) {
        Session session = null;
        AmpIndicator Ind = null;
        String indCode = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            Ind = (AmpIndicator) session.load(AmpIndicator.class, id);
            indCode = Ind.getCode();
        } catch (Exception ex1) {
            logger.error("Unable to get AmpIndicator object : " + ex1);
        }
        return indCode;
    }
    
    
      public static ArrayList searchForindicator(String sectorname) {
        Session session = null;
        
        Iterator iter =null;
        AmpIndicator ampIndicator = null;
        AmpSector ampsector =null;
        ArrayList Indicator = new ArrayList();
        try {
            session = PersistenceManager.getRequestDBSession();
            String sectorName = AmpSector.hqlStringForName("sec");
            String queryString = "select sec from "
                + AmpSector.class.getName() + " sec "
                + "where " + sectorName + "=:name and (sec.deleted is null or sec.deleted = false) ";
                Query qry = session.createQuery(queryString);
                qry.setString("name", sectorname);
    
              iter = qry.list().iterator();

            while (iter.hasNext()) {
                ampsector = (AmpSector) iter.next();
                for(Iterator itr = ampsector.getIndicators().iterator();itr.hasNext();){
                    AmpIndicator sect=(AmpIndicator)itr.next();
                    Indicator.add(sect);
                }
                
            }
               
        } catch (Exception ex) {
            logger.debug("Unable to search " + ex);
            }
        return Indicator;
    }
      
      /**
       * 
       * @param keyWord
       * @param sectorName
       * @return List of Idicators
       * @author dare
       */
      public static ArrayList searchIndicators(String keyWord,String sectorName){
            Session session = null;
            Query qry = null;
            List retValue=new ArrayList();
            try{
                session=PersistenceManager.getRequestDBSession();
                String queryString=null;    
                String sectorNameHql = AmpSector.hqlStringForName("sec");
                String indicatorNameHql = InternationalizedModelDescription.getForProperty(AmpIndicator.class, "name").getSQLFunctionCall("ind.indicatorId");
                
                if (keyWord!=null && keyWord.length()>0){
                    if(sectorName!=null && sectorName.length()>0 && !sectorName.equals("-1")){
                        queryString="select ind from "+ AmpIndicator.class.getName() +" ind inner join ind.sectors sec where " + indicatorNameHql + " like '%" + keyWord + "%'"
                                + " and " + sectorNameHql + "='" + sectorName + "'";
                        qry = session.createQuery(queryString);
                        retValue=qry.list();
                    }else {
                        queryString="select ind from "+ AmpIndicator.class.getName() +" ind where " + indicatorNameHql + " like '%" + keyWord + "%'";
                        qry = session.createQuery(queryString);
                        retValue=qry.list();
                    } 
                }else if (sectorName!=null && sectorName.length()>0 && !sectorName.equals("-1")){
                    retValue=searchForindicator(sectorName);
                }           
            }catch (Exception ex) {
                logger.debug("Unable to search " + ex);
                ex.printStackTrace();
            }
            return (ArrayList)retValue;
        }
      
      public static void deleteIndsector(Long sectorid,Long indid){
        
         logger.info(" deleting the indsectors");
            Session session = null;
            Transaction tx = null;
            AmpIndicator ampInd=null;
       
       try {
           session = PersistenceManager.getRequestDBSession();
//beginTransaction();
          AmpIndicator tempindInd = (AmpIndicator) session.load(AmpIndicator.class, indid);
          
         Collection sect = tempindInd.getSectors();
            if (sect != null && sect.size() > 0) {
                Iterator<AmpSector> sectIter = sect.iterator();
                while (sectIter.hasNext()) {
                    AmpSector sector = sectIter.next();
                    
                    if(sector!=null && sector.getAmpSectorId().equals(sectorid)){
                    
                        AmpSector tempAmpSector = null;
                        tempAmpSector = (AmpSector) session.load(AmpSector.class,sector.getAmpSectorId());
                        Set ampThemeSet = new HashSet();
                        tempAmpSector.getIndicators().remove(tempindInd);
                        tempindInd.getSectors().remove(tempAmpSector);
                    }
              }
         }
            session.update(tempindInd);
            //tx.commit();
//session.flush();
            
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("Exception from deleteIndSectors:" + e.getMessage());
            e.printStackTrace(System.out);
        }
    }
    
      @Deprecated
    public static void deleteIndtheme(Long indid){
        
         logger.info(" deleting the indthemes");
            Session session = null;
            Transaction tx = null;
            AmpIndicator ampInd=null;
       
       try {
           session = PersistenceManager.getRequestDBSession();
//beginTransaction();
          AmpIndicator tempindInd = (AmpIndicator) session.load(AmpIndicator.class, indid);

          //TODO INDIC
//       Collection theme = tempindInd.getThemes();
//          if (theme != null && theme.size() > 0) {
//              Iterator<AmpTheme> themeItr = theme.iterator();
//              while (themeItr.hasNext()) {
//                  AmpTheme themeInd = themeItr.next();
//                  
//                  if(themeInd!=null){
//                  
//                      AmpTheme tempAmpTheme = null;
//                      tempAmpTheme = (AmpTheme) session.load(AmpTheme.class,themeInd.getAmpThemeId());
//                      Set ampThemeSet = new HashSet();
//                      tempAmpTheme.getIndicators().remove(tempindInd);
//                      tempindInd.getThemes().remove(tempAmpTheme);
//                  }
//            }
//       }
            session.update(tempindInd);
            //tx.commit();
//session.flush();
            
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("Exception from deleteIndSectors:" + e.getMessage());
            e.printStackTrace(System.out);
        }
    }
    
    @Deprecated
     public static AmpIndicator getIndicatorById(Long indId){
         Session session = null;
         AmpIndicator tempInd = new AmpIndicator();

         try{
             session = PersistenceManager.getRequestDBSession();
             tempInd = (AmpIndicator) session.load(AmpIndicator.class,indId);
//session.flush();
         }
         catch(Exception e){
             logger.error("Unable to get the specified Indicator");
             logger.debug("Exception : "+e);
         }
         return tempInd;
        }
     
     @Deprecated
     public static void saveEditPrgIndValues(Collection<AmpPrgIndicatorValue> prgIndValues,AmpIndicator ampInd)
        {
            Session session = null;
            Transaction tx = null;
            try
            {
                session = PersistenceManager.getRequestDBSession();
                Iterator indValItr = prgIndValues.iterator();
                while(indValItr.hasNext())
                {
                    AmpThemeIndicatorValue ampThIndVal = null;
                    AmpPrgIndicatorValue ampPrgIndVal = (AmpPrgIndicatorValue) indValItr.next();
                    if(ampPrgIndVal.getIndicatorValueId() == null){
                        ampThIndVal = new AmpThemeIndicatorValue();
                    }else{
                                                ampThIndVal = (AmpThemeIndicatorValue) session.load(AmpThemeIndicatorValue.class,ampPrgIndVal.getIndicatorValueId());
                    }
                    ampThIndVal.setValueAmount(ampPrgIndVal.getValAmount());
                    ampThIndVal.setCreationDate(DateConversion.getDate(ampPrgIndVal.getCreationDate()));
                    ampThIndVal.setValueType(ampPrgIndVal.getValueType());
                    ampThIndVal.setIndicatorId(ampInd);
//beginTransaction();
                    session.saveOrUpdate(ampThIndVal);
                    //tx.commit();
                    }
                }
            
            catch(Exception ex)
            {
                
                     logger.error("Unable to get the specified Indicator");
                     logger.debug("Exception : "+ex);
            }
            
        }
        public static String getRiskColor(int overallRisk) {
            String fntColor = "";
            int r = 0,g = 0,b = 0;
            switch (overallRisk) {
            case Constants.HIGHLY_SATISFACTORY:
                r = Constants.HIGHLY_SATISFACTORY_CLR.getRed();
                g = Constants.HIGHLY_SATISFACTORY_CLR.getGreen();
                b = Constants.HIGHLY_SATISFACTORY_CLR.getBlue();
                break;
            case Constants.VERY_SATISFACTORY:
                r = Constants.VERY_SATISFACTORY_CLR.getRed();
                g = Constants.VERY_SATISFACTORY_CLR.getGreen();
                b = Constants.VERY_SATISFACTORY_CLR.getBlue();
                break;
            case Constants.SATISFACTORY:
                r = Constants.SATISFACTORY_CLR.getRed();
                g = Constants.SATISFACTORY_CLR.getGreen();
                b = Constants.SATISFACTORY_CLR.getBlue();
                break;
            case Constants.UNSATISFACTORY:
                r = Constants.UNSATISFACTORY_CLR.getRed();
                g = Constants.UNSATISFACTORY_CLR.getGreen();
                b = Constants.UNSATISFACTORY_CLR.getBlue();
                break;
            case Constants.VERY_UNSATISFACTORY:
                r = Constants.VERY_UNSATISFACTORY_CLR.getRed();
                g = Constants.VERY_UNSATISFACTORY_CLR.getGreen();
                b = Constants.VERY_UNSATISFACTORY_CLR.getBlue();
                break;
            case Constants.HIGHLY_UNSATISFACTORY:
                r = Constants.HIGHLY_UNSATISFACTORY_CLR.getRed();
                g = Constants.HIGHLY_UNSATISFACTORY_CLR.getGreen();
                b = Constants.HIGHLY_UNSATISFACTORY_CLR.getBlue();
            }

            String hexR = Integer.toHexString(r);
            String hexG = Integer.toHexString(g);
            String hexB = Integer.toHexString(b);
            if (hexR.equals("0"))
                fntColor += "00";
            else
                fntColor += hexR;

            if (hexG.equals("0"))
                fntColor += "00";
            else
                fntColor += hexG;

            if (hexB.equals("0"))
                fntColor += "00";
            else
                fntColor += hexB;

            return fntColor;
        }
        
        public static String getRiskRatingName(int risk) {
            Session session = null;
            String riskName = "";

            try {
                session = PersistenceManager.getSession();
                String qryStr = "select r.ratingName from " + AmpIndicatorRiskRatings.class.getName() + "" +
                        " r where (r.ratingValue=:risk)";
                Query qry = session.createQuery(qryStr);
                qry.setParameter("risk",new Integer(risk),IntegerType.INSTANCE);
                Iterator itr = qry.list().iterator();
                if (itr.hasNext()) {
                    riskName = (String) itr.next();
                }
            }
            catch(Exception e) {
                logger.error("Unable to get risk ratibg value");
                e.printStackTrace(System.out);
            }
            return riskName;
        }
     
     public static Collection<AmpIndicatorRiskRatings> getRisks(Long actId) throws Exception{
         ArrayList<AmpIndicatorRiskRatings> risks=new ArrayList<AmpIndicatorRiskRatings>();
         try {
             Set<IndicatorActivity> valuesActivity=ActivityUtil.loadActivity(actId).getIndicators();
                if(valuesActivity!=null && valuesActivity.size()>0){
                    Iterator<IndicatorActivity> it=valuesActivity.iterator();
                    while(it.hasNext()){
                         IndicatorActivity indActivity=it.next();
                         Set<AmpIndicatorValue> values=indActivity.getValues();                 
                         for(Iterator<AmpIndicatorValue> valuesIter=values.iterator();valuesIter.hasNext();){
                             AmpIndicatorValue val=valuesIter.next();
                             if(val.getRisk()!=null){
                                 risks.add(val.getRisk());
                                 break;//all values have same risk and this risk should go to connection.
                             }                                      
                        }
                    }
                }
        } catch (Exception e) {
             logger.error("Unable to get risks");
             e.printStackTrace();
        }
            
            return risks;
     }
     
     
     public static int getOverallRisk(Long actId) {
            Collection<AmpIndicatorRiskRatings> risks=null;
            int risk = 0;
            try {
                risks=getRisks(actId);
                Iterator<AmpIndicatorRiskRatings> itr = risks.iterator();
                float temp = 0;
                while (itr.hasNext()) {
                    AmpIndicatorRiskRatings ampIndicatorRisk = (AmpIndicatorRiskRatings) itr.next();
                    temp += ampIndicatorRisk.getRatingValue();
                }
                if (risks.size() > 0) {
                    temp /= (float) risks.size();
                    temp = Math.round(temp);
                    if (temp < 0)
                        risk = (int) Math.floor(temp);
                    else if(temp > 0)
                        risk = (int) Math.ceil(temp);
                    else
                        risk = -1;
                }
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
            return risk;
        }
     
     /**
         * This class is used for sorting IndicatorSector onjects by name
         * @author Dare Roinishvili
         *
         */
        public static class HelperIndicatorSectorNameComparator implements Comparator<IndicatorSector> {
            Locale locale;
            Collator collator;

            public HelperIndicatorSectorNameComparator(){
                this.locale=new Locale("en", "EN");
            }

            public HelperIndicatorSectorNameComparator(String iso) {
                this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
            }

            public int compare(IndicatorSector o1, IndicatorSector o2) {
                collator = Collator.getInstance(locale);
                collator.setStrength(Collator.TERTIARY);

                int result = collator.compare(o1.getIndicator().getName().toLowerCase(),o2.getIndicator().getName().toLowerCase());
                return result;
            }
        } 
}

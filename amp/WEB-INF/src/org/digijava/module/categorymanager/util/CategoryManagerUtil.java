package org.digijava.module.categorymanager.util;

import org.apache.log4j.Logger;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.exception.NoCategoryClassException;
import org.digijava.module.calendar.entity.AmpEventType;
import org.digijava.module.categorymanager.action.CategoryManager;
import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.dbentity.AmpLinkedCategoriesState;
import org.digijava.module.categorymanager.util.CategoryConstants.HardCodedCategoryValue;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.util.*;

public class CategoryManagerUtil {
    private static Logger logger = Logger.getLogger(CategoryManagerUtil.class);

    /**
     * Looks up the AmpCategoryValue with id = categoryValueId. If not null it adds it to the Set someSet.
     * @param categoryValueId
     * @param someSet
     */
    public static void addCategoryToSet (Long categoryValueId, Set someSet) {
        if(categoryValueId != null) {
            if( someSet == null )
                logger.error("The set received as argument 2 in null. Cannot insert AmpCategoryValue ");
            AmpCategoryValue ampCategoryValue   = CategoryManagerUtil.getAmpCategoryValueFromDb( categoryValueId );
            if (ampCategoryValue != null)
                    someSet.add(ampCategoryValue);
        }
    }

    /**
     * Used for translating the drop-downs with category values. The actual translation should be done in the CategoryManager page
     * in "Translator View". This function just extracts the translation from the database
     * @param ampCategoryValue
     * @param request
     * @return The translated category value or ,if any error appears, the empty string
     */
    public static String translateAmpCategoryValue(AmpCategoryValue ampCategoryValue) {
        //translation key is generated from the text hashcode
        String key=TranslatorWorker.generateTrnKey(ampCategoryValue.getValue());
        return translate(key, ampCategoryValue.getValue() );
        //return translate(CategoryManagerUtil.getTranslationKeyForCategoryValue(ampCategoryValue), request, ampCategoryValue.getValue() );
    }
    public static String translate(String key, String defaultValue) {
        Session session = null;
        String ret      = "";
        String lang = TLSUtils.getLangCode();
        try{
            session         = PersistenceManager.getSession();
            String qryStr   = "select m from "
                        + Message.class.getName() + " m "
                        + "where (m.locale=:langIso and m.key=:translationKey and m.siteId=:thisSiteId)";

            Query qry       = session.createQuery(qryStr);
            qry.setParameter("langIso", lang,StringType.INSTANCE);
            qry.setParameter("translationKey", key.toLowerCase(),StringType.INSTANCE);
            qry.setParameter("thisSiteId", TLSUtils.getThreadLocalInstance().site.getId().toString(),StringType.INSTANCE);

            Message m       = (Message)qry.uniqueResult();
            if ( m == null ) {
                logger.debug("No translation found for key '"+ key +"' for lang " + lang + ".");
                return defaultValue;
            }
            ret             = m.getMessage();
        }
        catch (Exception ex) {
            logger.error("Exception : " + ex.getMessage());
            ex.printStackTrace(System.out);
        }
        if ( ret == null || ret.equals("") )
                ret = defaultValue;
        return ret;
    }
    
    public static AmpCategoryValue getAmpCategoryValueFromCollectionById( Long ampCategoryValueId, Collection<AmpCategoryValue> col ) {
        if ( ampCategoryValueId == null || col == null) {
            logger.info("Couldn't get AmpCategoryValue because one of the parameters is null");
            return null;
        }
        Iterator<AmpCategoryValue> iterator = col.iterator();
        
        while ( iterator.hasNext() ) {
            AmpCategoryValue val        = iterator.next();
            if ( ampCategoryValueId.equals(val.getId()) )
                return val;
        }
        return null;
    }
    
    /**
     *
     * @param categoryId
     * @param values - a set of AmpCategoryValuews
     * @return The AmpCategoryValue object witch belongs to the AmpCategoryClass with id=categoryId
     * or null if the object is not in the set.
     */
    public static AmpCategoryValue getAmpCategoryValueFromList(Long categoryId, Collection values) {
        if ( categoryId == null || values == null) {
            logger.info("Couldn't get AmpCategoryValue because one of the parameters is null");
            return null;
        }
        Iterator iterator   = values.iterator();
        while( iterator.hasNext() ) {
            AmpCategoryValue ampCategoryValue   = (AmpCategoryValue)iterator.next();
            if ( ampCategoryValue.getAmpCategoryClass().getId().longValue() == categoryId.longValue() ) {
                return ampCategoryValue;
            }
        }
        return null;
    }
    /**
     *
     * @param categoryName
     * @param values
     * @return The AmpCategoryValue object witch belongs to the AmpCategoryClass with name=categoryName
     * or null if the object is not in the set.
     */
    public static AmpCategoryValue getAmpCategoryValueFromList(String categoryKey, Set<AmpCategoryValue> values) {
        if (categoryKey == null || values == null) {
            logger.info("Couldn't get AmpCategoryValue because one of the parameters is null");
            return null;
        }
        
        for(AmpCategoryValue ampCategoryValue:values) {
            if (ampCategoryValue.getAmpCategoryClass().getKeyName().equalsIgnoreCase(categoryKey)) {
                return ampCategoryValue;
            }
        }
        return null;
    }
    
    
    public static List<AmpEventType>  getAmpEventColors() throws NoCategoryClassException{
List<AmpEventType> eventTypeList = new ArrayList<AmpEventType>(); 
        
        AmpCategoryClass categoryClass = CategoryManagerUtil.loadAmpCategoryClassByKey(CategoryConstants.EVENT_TYPE_KEY);   
        Iterator<AmpCategoryValue> categoryClassIter = categoryClass.getPossibleValues().iterator();
         while(categoryClassIter.hasNext()){
            AmpEventType eventType = new AmpEventType();
            AmpCategoryValue item = (AmpCategoryValue) categoryClassIter.next();
             eventType.setName(item.getValue());
             eventType.setId(item.getId());
             Set<AmpCategoryValue> usedValues = item.getUsedValues();
             if (usedValues==null || usedValues.size()==0) {
                 eventType.setColor("grey"); //here select grey color by default if it's not seted on category manager. Thus the event doesn't lose on calendar view.
             } else {
                Iterator<AmpCategoryValue> it = usedValues.iterator();
                while (it.hasNext()){
                    AmpCategoryValue categoryValueItem = (AmpCategoryValue) it.next();
                    eventType.setColor(categoryValueItem.getValue());
                }
             }
             eventTypeList.add(eventType);
        }
        return eventTypeList;
        
    }
    
    /**
     *
     * @param categoryKey
     * @param values
     * @return The AmpCategoryValue object witch belongs to the AmpCategoryClass with key=categoryKey
     * or null if the object is not in the set.
     */
    public static AmpCategoryValue getAmpCategoryValueFromListByKey(String categoryKey, Set<AmpCategoryValue> values) {
        if ( categoryKey == null || values == null) {
            logger.info("Couldn't get AmpCategoryValue because one of the parameters is null");
            return null;
        }

        for(AmpCategoryValue ampCategoryValue:values){
            if ( ampCategoryValue.getAmpCategoryClass().getKeyName().equals(categoryKey) ) {
                return ampCategoryValue;
            }
        }
        return null;
    }

    /**
     *
     * @param currentTeam
     * @param categKey
     * @return A prefixed category if exists.
     */
    public static String getAlternateKey(final AmpTeam currentTeam, final String categKey) {
        if (currentTeam != null && currentTeam.getWorkspacePrefix() != null && (!categKey.
                equalsIgnoreCase(CategoryConstants.IMPLEMENTATION_LEVEL_KEY)
                && !categKey.equalsIgnoreCase(CategoryConstants.IMPLEMENTATION_LOCATION_KEY))) {
            String tmpKey = currentTeam.getWorkspacePrefix().getValue() + categKey;
            if (CategoryManagerUtil.loadAmpCategoryClassByKey(tmpKey) != null) {
                return tmpKey; // a prefixed category exists -> return its key
            }
        }
        return categKey;
    }

    public static List<AmpCategoryValue> getAmpCategoryValuesFromListByKey(String categoryKey, Set<AmpCategoryValue> values) {
        List<AmpCategoryValue> ret = new ArrayList<AmpCategoryValue>();
        if ( categoryKey == null || values == null) {
            logger.info("Couldn't get AmpCategoryValue because one of the parameters is null");
            return null;
        }
        for(AmpCategoryValue ampCategoryValue:values) {
            if ( ampCategoryValue.getAmpCategoryClass().getKeyName().equals(categoryKey) ) {
//              if ((ampCategoryValue.getDeleted() == null) || (ampCategoryValue.getDeleted() == false)) 
                {
                    ret.add(ampCategoryValue);
                }
            }
        }
        return ret;
    }
    
    /**
     * 
     * @param categoryKey The key of the category
     * @param categoryIndex The index of the value within the category
     * @return
     */
    public static AmpCategoryValue getAmpCategoryValueFromDb(String categoryKey, Long categoryIndex) {
        Session dbSession           = null;
        Collection returnCollection = null;
        try {
        
            dbSession= PersistenceManager.getRequestDBSession();
            String queryString;
            Query qry;

            queryString = "select v from "
                + AmpCategoryValue.class.getName()
                + " v join v.ampCategoryClass as c where c.keyName=:key AND v.index=:index";
            qry         = dbSession.createQuery(queryString);
            qry.setParameter("key", categoryKey, StringType.INSTANCE);
            qry.setParameter("index", categoryIndex, LongType.INSTANCE);
            returnCollection    = qry.list();

        } catch (Exception ex) {
            logger.error("Unable to get AmpCategoryValue: " + ex.getMessage());
            ex.printStackTrace();
        } 
//      finally {
//          try {
//              PersistenceManager.releaseSession(dbSession);
//          } catch (Exception ex2) {
//              logger.error("releaseSession() failed :" + ex2);
//          }
    //}
    
        if (returnCollection != null) {
            Iterator it=returnCollection.iterator();
            if(it.hasNext())
            {
                AmpCategoryValue x=(AmpCategoryValue)it.next();
                return x;
            }
        }
        return null;
    }
    
    public static AmpCategoryValue getAmpCategoryValueFromDb(Long valueId) {
        return getAmpCategoryValueFromDb( valueId, false );
    }
    
    /**
     *
     * @param valueId
     * @param initializeTaggedValues AmpCategoryValue.usedByValues property is lazyly initialized. Setting the initializeTaggedValues as true will initialize the 
     * property before closing the hibernate session
     * @return Extracts the AmpCategoryValue with id=valueId from the database. Return null if not found.
     */
    public static AmpCategoryValue getAmpCategoryValueFromDb(Long valueId, boolean initializeTaggedValues)
    {
        if (valueId == null)
            return null;
        try
        {
            Session dbSession = PersistenceManager.getSession();
            AmpCategoryValue retVal = (AmpCategoryValue) dbSession.get(AmpCategoryValue.class, valueId);
            if (retVal != null && initializeTaggedValues)
                retVal.getUsedByValues().size(); // eager load
            return retVal;
        }
        catch(Exception e)
        {
            return null;
        }
    }   
     
    
    /**
     * 
     * @param tagId   the id of the tag (that is the id of the AmpCategoryValue object used as tag)
     * @param categoryKey   if not null, only the AmpCategoryValue objects belonging to the category with key=categoryKey will be returned in the set
     * @return set of AmpCategoryValue objects tagged with tag that has id=tagId
     */
    public static Set<AmpCategoryValue> getTaggedCategoryValues(Long tagId, String categoryKey) {
        
        AmpCategoryValue tag    = getAmpCategoryValueFromDb(tagId, true);
        if ( tag != null && tag.getUsedByValues() != null ) {
            return orderCategoryValues(tag.getUsedByValues(), categoryKey);
        }
        return null;
    }
    /**
     * 
     * @param tag   the AmpCategoryValue object used as tag
     * @param categoryKey   if not null, only the AmpCategoryValue objects belonging to the category with key=categoryKey will be returned in the set
     * @return set of AmpCategoryValue objects tagged with the specified tag
     */
    public static Set<AmpCategoryValue> getTaggedCategoryValues(AmpCategoryValue tag, String categoryKey) {
        try {
            if ( tag != null && tag.getUsedByValues() != null ) {
                return orderCategoryValues(tag.getUsedByValues(), categoryKey);
            }
            return null;
        }
        catch (Exception e) {
            logger.info( e.getMessage() );
            logger.info( "Trying to reload the AmpCategoryValue object in order to initialize lazy property" );
            return getTaggedCategoryValues(tag.getId(), categoryKey);
        }
    }
    /**
     * 
     * @param unorderedSet
     * @param categoryKey
     * @return a set containing filtered and ordered category values
     */
    private static Set<AmpCategoryValue> orderCategoryValues(Set<AmpCategoryValue> unorderedSet, String categoryKey) {
        TreeSet<AmpCategoryValue> returnSet = new TreeSet<AmpCategoryValue>(
                new Comparator<AmpCategoryValue>() {
                    public int compare(AmpCategoryValue o1, AmpCategoryValue o2) {
                        if ( o1.getAmpCategoryClass().getId().equals( o2.getAmpCategoryClass().getId()) ) {
                            return o1.getIndex() - o2.getIndex();
                        }
                        else
                            return o1.getAmpCategoryClass().getKeyName().compareTo( o2.getAmpCategoryClass().getKeyName() );                    
                    }
                }
        );
        if ( unorderedSet != null)  {       
            if ( categoryKey != null ) {
                Iterator<AmpCategoryValue> iter     = unorderedSet.iterator();
                while ( iter.hasNext() ) {
                    AmpCategoryValue item               = iter.next();
                    if ( item.getAmpCategoryClass().getKeyName().equals(categoryKey) )
                        returnSet.add(item);
                }
            }
            else {
                returnSet.addAll( unorderedSet );
            }
            return returnSet;
        }
        return null;
            
    }
    
    
    /**
     *
     * @param categoryId
     * @return AmpCategoryClass object with id=categoryId from the database
     */
    public static AmpCategoryClass loadAmpCategoryClass(Long categoryId) throws NoCategoryClassException {
        Session dbSession           = null;
        Collection returnCollection = null;
        try {
            dbSession           = PersistenceManager.getSession();
            String queryString;
            Query qry;

            queryString = "select c from "
                + AmpCategoryClass.class.getName()
                + " c where c.id=:id";
            qry         = dbSession.createQuery(queryString);
            qry.setLong("id", categoryId);



            returnCollection    = qry.list();

        } catch (Exception ex) {
            logger.error("Unable to get Categories: " + ex.getMessage());
            ex.printStackTrace();
        } 
        if((returnCollection!=null)&&(!returnCollection.isEmpty())){
            return (AmpCategoryClass)returnCollection.toArray()[0];
        }else{
            throw new NoCategoryClassException("No AmpCategoryClass found with id '" + categoryId + "'");
            //logger.error( "No AmpCategoryClass found with id '" + categoryId + "'" );
            
        }
    }
    /**
     *
     * @param categoryId
     * @return AmpCategoryClass object with name=categoryName from the database
     * @deprecated use loadAmpCategoryClassByKey instead
     */
    @Deprecated
    public static AmpCategoryClass loadAmpCategoryClass(String name) throws NoCategoryClassException {
        Session dbSession           = null;
        Collection returnCollection = null;
        try {
            dbSession           = PersistenceManager.getSession();
            String queryString;
            Query qry;

            queryString = "select c from "
                + AmpCategoryClass.class.getName()
                + " c where c.name=:name";
            qry         = dbSession.createQuery(queryString);
            qry.setParameter("name", name,StringType.INSTANCE);



            returnCollection    = qry.list();

        } catch (Exception ex) {
            logger.error("Unable to get Categories: " + ex.getMessage());
            ex.printStackTrace();
        }
        if((returnCollection!=null)&&(!returnCollection.isEmpty())){
            return (AmpCategoryClass)returnCollection.toArray()[0];
        }else{
            throw new NoCategoryClassException("No AmpCategoryClass found with name '" + name + "'");
        }

    }
    /**
     *
     * @param categoryName
     * @param ordered overrides the property in AmpCategoryClass and decides whether the values should be ordered alphabetically.
     * If null the ordered property of the AmpCategoryClass object is checked.
     * @param request TODO
     * @return A collection of AmpCategoryValues witch belong to the AmpCategoryClass with name=categoryName
     * @throws Exception in case request is null and the values need to be ordered alphabetically
     */
    @Deprecated
    public static Collection<AmpCategoryValue> getAmpCategoryValueCollection(String categoryName, Boolean ordered) throws Exception {
        boolean shouldOrderAlphabetically;
        
        AmpCategoryClass ampCategoryClass = null; 
        try {
            ampCategoryClass = CategoryManagerUtil.loadAmpCategoryClass(categoryName);
        } catch (NoCategoryClassException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (ampCategoryClass == null)
            return null;
        if (ordered == null) {
            shouldOrderAlphabetically   = ampCategoryClass.getIsOrdered();
        }
        else
            shouldOrderAlphabetically   = ordered.booleanValue();

        List<AmpCategoryValue> ampCategoryValues            = ampCategoryClass.getPossibleValues();

        if ( !shouldOrderAlphabetically )
                return ampCategoryValues;
        
        TreeSet<AmpCategoryValue> treeSet   = new TreeSet<AmpCategoryValue>( new CategoryManagerUtil.CategoryComparator() );
        treeSet.addAll(ampCategoryValues);


        return treeSet;
    }

    public static boolean isExitingAmpCategoryValue(String categoryKey, Long id, boolean onlyVisible) {
        Long count = (Long) PersistenceManager.getSession().createQuery(
                "select count(a) from " + AmpCategoryValue.class.getName()
                + " a where a.id=:id "
                + (onlyVisible ? "and (a.deleted=false or a.deleted is null) " : "") 
                + "and a.ampCategoryClass.keyName=:keyName")
            .setParameter("id", id)
            .setParameter("keyName", categoryKey)
            .uniqueResult();
        return count.intValue() == 1;
    }

    /**
     * This is a wrapper function for getAmpCategoryValueCollectionByKey(String categoryKey, Boolean ordered). 
     * The function is called with ordered = false
     * @param categoryKey
     * @return 
     */
    public static Collection<AmpCategoryValue> getAmpCategoryValueCollectionByKey(String categoryKey){
        return getAmpCategoryValueCollectionByKey(categoryKey, false);      
    }

    /**
     * gets all ACV that are not also deleted 
     * The function is called with ordered = false 
     * @param categoryKey
     * @return
     */
    public static Collection<AmpCategoryValue> getAmpCategoryValueCollectionByKeyExcludeDeleted(String categoryKey){
        return getAmpCategoryValueCollectionByKeyExcludeDeleted(categoryKey,false);
    }
    /**
     * gets all ACV that are not also deleted 
     * @param categoryKey
     * @param ordered if this is true the returned collection is ordered by name ascending, if it is false the returned collection is ordered by index ascending
     * @return
     */
    public static Collection<AmpCategoryValue> getAmpCategoryValueCollectionByKeyExcludeDeleted(String categoryKey,Boolean ordered){
        Collection<AmpCategoryValue> visibleValues = new ArrayList<AmpCategoryValue>(); 
        Collection<AmpCategoryValue> acvList = CategoryManagerUtil.getAmpCategoryValueCollectionByKey(categoryKey,ordered);
        if (acvList != null) {
            for (AmpCategoryValue acvalue : acvList) {
                if (acvalue.isVisible()) {
                    visibleValues.add(acvalue);
                }
            }
        }
        return visibleValues;       
    }   
    
    
    /**
     * because the amp_categories table does not change during the runtime of AMP, we can safely cache them
     */
    private static Map<String, AmpCategoryClass> categoryValuesByKey = Collections.synchronizedMap(new HashMap<String, AmpCategoryClass>());
    /**
     * 
     * @param categoryKey
     */
    public static void removeAmpCategryBykey(String categoryKey){
        categoryValuesByKey.remove(categoryKey);
    }
    /**
     *
     * @param categoryKey
     * @param ordered overrides the property in AmpCategoryClass and decides whether the values should be ordered alphabetically.
     * If null the ordered property of the AmpCategoryClass object is checked.
     * @return A collection of AmpCategoryValues witch belong to the AmpCategoryClass with key=categoryKey
     * @throws Exception in case request is null and the values need to be ordered alphabetically
     */
    public static Collection<AmpCategoryValue> getAmpCategoryValueCollectionByKey(String categoryKey, Boolean ordered) 
    {   
        AmpCategoryClass ampCategoryClass;
        
        if (categoryValuesByKey.containsKey(categoryKey))
            ampCategoryClass = categoryValuesByKey.get(categoryKey);
        else
        {       
            ampCategoryClass = CategoryManagerUtil.loadAmpCategoryClassByKey(categoryKey);          
            if (ampCategoryClass == null)
                return null;
            categoryValuesByKey.put(categoryKey, ampCategoryClass);
        }

        List<AmpCategoryValue> ampCategoryValues = ampCategoryClass.getPossibleValues();
        ampCategoryValues.removeAll(Collections.singleton(null));
        
        PersistenceManager.getSession().evict(ampCategoryClass); // else funny things will happen if someone tries to delete()
        
        boolean shouldOrderAlphabetically = ordered == null ? ampCategoryClass.getIsOrdered() : ordered;
        if (!shouldOrderAlphabetically) {
            return ampCategoryValues;
        }
        
        TreeSet<AmpCategoryValue> treeSet   = new TreeSet<AmpCategoryValue>( new CategoryManagerUtil.CategoryComparator() );
        treeSet.addAll(ampCategoryValues);
        
        return treeSet;
    }
    /**
     *
     * @param categoryId
     * @param ordered must be true if the AmpCategoryValues should be ordered alphabetically.
     * If false the ordered property of the AmpCategoryClass object is checked.
     * @return A collection of AmpCategoryValues witch belong to the AmpCategoryClass with id=categoryId
     * @throws Exception in case request is null and the values need to be ordered alphabetically
     */
    public static Collection<AmpCategoryValue> getAmpCategoryValueCollection(Long categoryId, Boolean ordered) throws Exception {
        boolean shouldOrderAlphabetically;
        
        AmpCategoryClass ampCategoryClass;
        try {
            ampCategoryClass    = CategoryManagerUtil.loadAmpCategoryClass(categoryId);
        }
        catch(Exception E) {
            E.printStackTrace();
            return null;
        }
        if (ampCategoryClass == null)
            return null;
        if (ordered == null) {
            shouldOrderAlphabetically   = ampCategoryClass.getIsOrdered();
        }
        else
            shouldOrderAlphabetically   = ordered.booleanValue();
    
        List<AmpCategoryValue> ampCategoryValues        = ampCategoryClass.getPossibleValues();
    
        if ( !shouldOrderAlphabetically )
                return ampCategoryValues;
            
        TreeSet<AmpCategoryValue> treeSet   = new TreeSet<AmpCategoryValue>( new CategoryManagerUtil.CategoryComparator() );
        treeSet.addAll(ampCategoryValues);
        return treeSet;
        
    }
    /**
     * returns a string containing only ascii characters
     * @param input The string that needs to be filtered
     * @return
     */
    public static String asciiStringFilter (String input) {
        byte [] bytearray       = input.getBytes(); 
        
        CharsetDecoder decoder  = Charset.forName("US-ASCII").newDecoder();
        decoder.replaceWith("_");
        decoder.onMalformedInput(CodingErrorAction.REPLACE);
        decoder.onUnmappableCharacter(CodingErrorAction.REPLACE);
        
        try {
            CharBuffer buffer       = decoder.decode( ByteBuffer.wrap(bytearray) );
            return buffer.toString();
        } catch (CharacterCodingException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String getTranslationKeyForCategoryName(String classKeyName) {
            String translationKey       = "cm:category_" + classKeyName + "_name";
            return translationKey;
        
    }
    public static String getTranslationKeyForCategoryDescription(String classKeyName) {
        String translationKey       = "cm:category_" + classKeyName + "_description";
        return translationKey;
    
    }
    /**
     *
     * @param ampCategoryValue
     * @return the translation key for the ampCategoryValue
     */
    public static String getTranslationKeyForCategoryValue(AmpCategoryValue ampCategoryValue) {
        return getTranslationKeyForCategoryValue(ampCategoryValue.getValue(), ampCategoryValue.getAmpCategoryClass().getKeyName() );
    }
    
    /**
     *
     * @param ampCategoryValue
     * @return the translation key for the ampCategoryValue
     */
    public static String getTranslationKeyForCategoryValue(String value, String classKeyName) {
        String filteredValue            = asciiStringFilter( value );
        String translationKey           = "cm:category_" + classKeyName +
                                        "_" + filteredValue;
        return translationKey.toLowerCase();
    }
    
    /**
     *
     * @param key The key of the AmpCategoryClass object. (A key can be attributed when creating a new category)
     * @return The AmpCategoryClass object with the specified key. If not found returns null.
     */
    public static AmpCategoryClass loadAmpCategoryClassByKey(String key)
    {
        Session dbSession = PersistenceManager.getSession();
        dbSession.clear();
        List<AmpCategoryClass> col;
        try {
            //AmpCategoryClass dbCategory       = new AmpCategoryClass();
                String queryString  = "select c from " + AmpCategoryClass.class.getName() + " c where c.keyName=:key";
                Query query         = dbSession.createQuery(queryString);
                query.setParameter("key", key,StringType.INSTANCE);
                query.setCacheable(true);
                col = query.list();
                if (col.isEmpty())
                    return null;
                if (col.size() > 1)
                    throw new RuntimeException("multiple category classes found with the same key: " + col.size());
                return col.get(0);
        } catch (Exception ex) {            
            logger.error("Error retrieving AmpCategoryClass with key '" + key + "': " + ex);
            throw new RuntimeException(ex);
        }
    }

    public static String getStringValueOfAmpCategoryValue(AmpCategoryValue ampCategoryValue) {
        if (ampCategoryValue != null) {
            return ampCategoryValue.getValue();
        }
        return "";
    }
    
    public static Collection<String> getStringValueOfAmpCategoryValues(Collection<AmpCategoryValue> values) {
        Collection<String> ret=new ArrayList<String>();
        Iterator i=values.iterator();
        while (i.hasNext()) {
            AmpCategoryValue elem = (AmpCategoryValue) i.next();
            ret.add(elem.getValue());
        }
        return ret;
    }
    
    public static AmpCategoryValue addValueToCategory(String categoryKey, String value) throws Exception {
        Session dbSession           = null;
        String error                = null;
        AmpCategoryValue ampCategoryValue   = null;
        try {
            dbSession           = PersistenceManager.getSession();
            String queryString;
            Query qry;

            queryString = "select c from "
                + AmpCategoryClass.class.getName()
                + " c where c.keyName=:categoryKey";
            qry         = dbSession.createQuery(queryString);
            qry.setParameter("categoryKey", categoryKey ,StringType.INSTANCE);
            
            List<AmpCategoryClass> resultList       = qry.list();
            
            if ( resultList == null || resultList.size() != 1 )
                error   = "There was a problem loading the specified AmpCategoryClass with key:" + categoryKey;
            else {
                AmpCategoryClass ampCategoryClass   = resultList.get(0);
                
                if ( ampCategoryClass.getPossibleValues() == null )
                    ampCategoryClass.setPossibleValues( new ArrayList<AmpCategoryValue>() );
                 
                ampCategoryValue    = new AmpCategoryValue();
                ampCategoryValue.setValue(value);
                ampCategoryValue.setAmpCategoryClass(ampCategoryClass);
                ampCategoryValue.setIndex( ampCategoryClass.getPossibleValues().size() );
                
                List<AmpCategoryValue> tempList     = new ArrayList<AmpCategoryValue>();
                tempList.addAll(ampCategoryClass.getPossibleValues());
                tempList.add( ampCategoryValue );
                
                if ( CategoryManager.checkDuplicateValues(tempList) == null ) {
                    ampCategoryClass.getPossibleValues().add( ampCategoryValue );
                }
                else
                    error                           = "The value already exists";
//session.flush();
                
            }

        } catch (Exception ex) {
            logger.error("Unable to get Categories: " + ex.getMessage());
            error   = "A Hibernate exception occured. See Stacktrace above.";
            ex.printStackTrace();
        } 
        
        if ( error != null)
            throw new Exception( error );
        if ( ampCategoryValue == null)
            throw new Exception( "AmpCategoryValue object should not be null here" );
        return ampCategoryValue;
        
    }
    
    /**
     * Verify that a category value has been inserted in CategoryConstants as a HardCodedcategoryValue
     * @return true if the category value key has been inserted in the CategoryConstants class.
     */
    public static boolean verifyDeletionProtectionForCategoryValue (String categoryKey, String valueKey ) {
        Field[] fields  = CategoryConstants.class.getDeclaredFields();
        for (int i=0; i< fields.length; i++  ) {
            Class fieldClass    = fields[i].getType();
            if ( fieldClass.equals( HardCodedCategoryValue.class ) ) {
                HardCodedCategoryValue proprietyValue;
                try {
                    proprietyValue = (HardCodedCategoryValue)fields[i].get(null);
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
                if ( proprietyValue.getCategoryKey().equals(categoryKey) 
                        && proprietyValue.getValueKey().equals(valueKey) 
                        && proprietyValue.isProtectOnDelete()) {
                    return true;
                }
            }
        }
        return false;
    }   
    
    public static boolean isCategoryKeyInUse(String key) {
        try {
            return CategoryManagerUtil.loadAmpCategoryClassByKey(key) != null;
        } catch (Exception e) {
            //logger.info("Category key '" + key + "' was not found in database.");
            return false;
        }
    }
    
    public static AmpLinkedCategoriesState getState(AmpCategoryClass mainCategory, AmpCategoryClass usedCategory){
        AmpLinkedCategoriesState retVal = null;
        Session dbSession   = null;
        Query qry = null;
        String queryString =null;
        try {
            dbSession = PersistenceManager.getRequestDBSession();
            queryString = "select c from "  + AmpLinkedCategoriesState.class.getName()+ " c where c.mainCategory=:mainCategory and c.linkedCategory=:usedCategory";
            qry         = dbSession.createQuery(queryString);
            qry.setEntity("mainCategory", mainCategory);
            qry.setEntity("usedCategory", usedCategory);
            retVal = (AmpLinkedCategoriesState)qry.uniqueResult();
        } catch (Exception e) {
            logger.error("Failed to get state ", e);
        }
        return retVal;
    }

    public static Collection<AmpCategoryValue> getOrderedPossibleValues(final AmpCategoryClass ampCategoryClass) {
        if (ampCategoryClass.getIsOrdered() && ampCategoryClass.getPossibleValues() != null) {
            TreeSet<AmpCategoryValue> treeSet = new TreeSet<AmpCategoryValue>(new CategoryManagerUtil
                    .CategoryComparator());
            treeSet.addAll(ampCategoryClass.getPossibleValues());
            return treeSet;
        }
        return ampCategoryClass.getPossibleValues();
    }
    
    public static String checkImplementationLocationCategory() {
        String errorString = "The following values were not found: ";
        String separator = "";
        
        AmpCategoryValue country = CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_0.getAmpCategoryValueFromDB();
        AmpCategoryValue region = CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_1.getAmpCategoryValueFromDB();
        AmpCategoryValue zone = CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_2.getAmpCategoryValueFromDB();
        AmpCategoryValue district = CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_3.getAmpCategoryValueFromDB();
        
        if (country == null) {
            errorString += CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_0.getValueKey();
            separator = ", ";
        }
        if (region == null) {
            errorString += separator + CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_1.getValueKey();
            separator = ", ";
        }
        if (zone == null) {
            errorString += separator + CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_2.getValueKey();
            separator = ", ";
        }
        if (district == null) {
            errorString += separator + CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_3.getValueKey();
            separator = ", ";
        }
        if (separator != "") {
            return errorString;
        } else { // checking order
            if (country.getIndex() >= region.getIndex()) {
                return "Administrative Level 0 must be before Administrative Level 1";
            } else if (region.getIndex() >= zone.getIndex()) {
                return "Administrative Level 1 must be before Administrative Level 2";
            } else if (zone.getIndex() >= district.getIndex()) {
                return "Administrative Level 2 must be before Administrative Level 3";
            }
        }
        return null;
    }

    /**
     *
     * @author Alex Gartner
     * A comparator for AmpCategoryValue objects which is needed when ordering them alphabetically
     */
    public static class CategoryComparator implements Comparator<AmpCategoryValue> {
        public CategoryComparator() {
        }
        
        public int compare(AmpCategoryValue catValue1, AmpCategoryValue catValue2) {
//          if ( request == null )
//              return catValue1.getValue().compareTo( catValue2.getValue() );
            
//          String transValue1      = catValue1!=null?CategoryManagerUtil.translateAmpCategoryValue(catValue1, request):"";
//          String transValue2      = catValue2!=null?CategoryManagerUtil.translateAmpCategoryValue(catValue2, request):"";
            String transValue1      = catValue1 != null ? TranslatorWorker.translateText(catValue1.getValue()):"";
            String transValue2      = catValue2 != null ? TranslatorWorker.translateText(catValue2.getValue()):"";
            return transValue1.compareTo( transValue2 );            
        }
        public boolean equals(AmpCategoryValue value1, AmpCategoryValue value2) {
            if ( this.compare(value1, value2) == 0 )
                    return true;
            return false;
        }
    }   

    /**
     * calculates list of available choices when someone has selected a category key 
     * @return
     */
    public static List<AmpCategoryValue> getAllAcceptableValuesForACVClass(String categoryKey, Collection<AmpCategoryValue> relatedCollection)
    {
        List<AmpCategoryValue> collectionByKey = new ArrayList<AmpCategoryValue>();
        Collection<AmpCategoryValue> collectionPrefiltered =
                CategoryManagerUtil.getAmpCategoryValueCollectionByKey(categoryKey, null);
        for (AmpCategoryValue acv: collectionPrefiltered){
            if (acv!= null && acv.isVisible())
                collectionByKey.add(acv);
        }
        
        if (relatedCollection != null) {
            Set<AmpCategoryValue> relatedReunion = new TreeSet<AmpCategoryValue>();
            for (AmpCategoryValue ampCategoryValue : relatedCollection)
                if (ampCategoryValue != null && ampCategoryValue.isVisible())
                    relatedReunion.addAll(CategoryManagerUtil.getAmpCategoryValueFromDb(ampCategoryValue.getId(), true).getUsedByValues());
            collectionByKey.retainAll(relatedReunion);
        }
        return collectionByKey;
    }

    /**
     * null-guards the result
     * @param id
     * @return
     */
    public static AmpCategoryValue loadAcvOrNull(Long id)
    {
        return id == null ? null : getAmpCategoryValueFromDb(id);
    }
    
    
    /**
     *
     * @return A collection of all AmpCategoryValues from DB
     */
    public static List<AmpCategoryValue> getAllCategoryValues()  {
        Session session = null;
        Query qry = null;
        List<AmpCategoryValue> cvs = new ArrayList<AmpCategoryValue>();
        
        try  {
            session = PersistenceManager.getRequestDBSession();
            String queryString = " from " + AmpCategoryValue.class.getName() + " cv";
            qry = session.createQuery(queryString);
            cvs = qry.list();
        } catch(Exception ex) {
            throw new RuntimeException("Cannot get cvs, ", ex);
        }
        
        return cvs;
    }
        
}


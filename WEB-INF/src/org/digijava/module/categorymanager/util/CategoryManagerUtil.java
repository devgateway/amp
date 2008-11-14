package org.digijava.module.categorymanager.util;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpCategoryClass;
import org.digijava.module.aim.dbentity.AmpCategoryValue;
import org.digijava.module.aim.exception.NoCategoryClassException;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;

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
			AmpCategoryValue ampCategoryValue	= CategoryManagerUtil.getAmpCategoryValueFromDb( categoryValueId );
			if (ampCategoryValue != null)
					someSet.add(ampCategoryValue);
		}
	}

	/**
	 * Used for translating the drop-downs with category values. The actual translation should be done in the CategoryManager page
	 * in "Translator View". This function just extracts the translation from the database
	 * @param ampCategoryValue
	 * @param request
	 * @return The translated category value or ,if any eror appear, the empty string
	 */
	public static String translateAmpCategoryValue(AmpCategoryValue ampCategoryValue, HttpServletRequest request) {
		return translate(CategoryManagerUtil.getTranslationKeyForCategoryValue(ampCategoryValue), request, ampCategoryValue.getValue() );
	}
	public static String translate(String key, HttpServletRequest request, String defaultValue) {
		Session session	= null;
		String ret		= "";
		String	lang	= RequestUtils.getNavigationLanguage(request).getCode();
		Long	siteId	= RequestUtils.getSite(request).getId();
		try{
			session			= PersistenceManager.getSession();
			String qryStr	= "select m from "
						+ Message.class.getName() + " m "
						+ "where (m.locale=:langIso and m.key=:translationKey and m.siteId=:thisSiteId)";

			Query qry		= session.createQuery(qryStr);
			qry.setParameter("langIso", lang, Hibernate.STRING);
			qry.setParameter("translationKey", key.toLowerCase() ,Hibernate.STRING);
			qry.setParameter("thisSiteId", siteId+"", Hibernate.STRING);

			Message m		= (Message)qry.uniqueResult();
			if ( m == null ) {
				logger.debug("No translation found for key '"+ key +"' for lang " + lang + ".");
				return defaultValue;
			}
			ret				= m.getMessage();
		}
		catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
			ex.printStackTrace(System.out);
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		if ( ret == null || ret.equals("") )
				ret	= defaultValue;
		return ret;
	}
	/**
	 *
	 * @param categoryId
	 * @param values - a set of AmpCategoryValuews
	 * @return The AmpCategoryValue object witch belongs to the AmpCategoryClass with id=categoryId
	 * or null if the object is not in the set.
	 */
	public static AmpCategoryValue getAmpCategoryValueFromList(Long categoryId, Set values) {
		if ( categoryId == null || values == null) {
			logger.info("Couldn't get AmpCategoryValue because one of the parameters is null");
			return null;
		}
		Iterator iterator	= values.iterator();
		while( iterator.hasNext() ) {
			AmpCategoryValue ampCategoryValue	= (AmpCategoryValue)iterator.next();
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
	public static AmpCategoryValue getAmpCategoryValueFromList(String categoryName, Set values) {
		if ( categoryName == null || values == null) {
			logger.info("Couldn't get AmpCategoryValue because one of the parameters is null");
			return null;
		}
		Iterator iterator	= values.iterator();
		while( iterator.hasNext() ) {
			AmpCategoryValue ampCategoryValue	= (AmpCategoryValue)iterator.next();
			if ( ampCategoryValue.getAmpCategoryClass().getName().equals(categoryName) ) {
				return ampCategoryValue;
			}
		}
		return null;
	}
	/**
	 *
	 * @param categoryKey
	 * @param values
	 * @return The AmpCategoryValue object witch belongs to the AmpCategoryClass with key=categoryKey
	 * or null if the object is not in the set.
	 */
	public static AmpCategoryValue getAmpCategoryValueFromListByKey(String categoryKey, Set values) {
		if ( categoryKey == null || values == null) {
			logger.info("Couldn't get AmpCategoryValue because one of the parameters is null");
			return null;
		}
		Iterator iterator	= values.iterator();
		while( iterator.hasNext() ) {
			AmpCategoryValue ampCategoryValue	= (AmpCategoryValue)iterator.next();
			if ( ampCategoryValue.getAmpCategoryClass().getKeyName().equals(categoryKey) ) {
				return ampCategoryValue;
			}
		}
		return null;
	}
	
	public static Collection getAmpCategoryValuesFromListByKey(String categoryKey, Set values) {
		Collection<AmpCategoryValue> ret=new ArrayList<AmpCategoryValue>();
		if ( categoryKey == null || values == null) {
			logger.info("Couldn't get AmpCategoryValue because one of the parameters is null");
			return null;
		}
		Iterator iterator	= values.iterator();
		while( iterator.hasNext() ) {
			AmpCategoryValue ampCategoryValue	= (AmpCategoryValue)iterator.next();
			if ( ampCategoryValue.getAmpCategoryClass().getKeyName().equals(categoryKey) ) {
				ret.add(ampCategoryValue);
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
		Session dbSession			= null;
		Collection returnCollection	= null;
		try {
		
			dbSession= PersistenceManager.getRequestDBSession();
			String queryString;
			Query qry;

			queryString = "select v from "
				+ AmpCategoryValue.class.getName()
				+ " v join v.ampCategoryClass as c where c.keyName=:key AND v.index=:index";
			qry			= dbSession.createQuery(queryString);
			qry.setParameter("key", categoryKey, Hibernate.STRING);
			qry.setParameter("index", categoryIndex, Hibernate.LONG);
			returnCollection	= qry.list();

		} catch (Exception ex) {
			logger.error("Unable to get AmpCategoryValue: " + ex.getMessage());
			ex.printStackTrace();
		} 
//		finally {
//			try {
//				PersistenceManager.releaseSession(dbSession);
//			} catch (Exception ex2) {
//				logger.error("releaseSession() failed :" + ex2);
//			}
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
	
	/**
	 *
	 * @param valueId
	 * @return Extracts the AmpCategoryValue with id=valueId from the database. Return null if not found.
	 */
	public static AmpCategoryValue getAmpCategoryValueFromDb(Long valueId) {
		if(valueId!=null)
		{
			Session dbSession			= null;
			Collection returnCollection	= null;
			try {
				dbSession			= PersistenceManager.getSession();
				String queryString;
				Query qry;

				queryString = "select v from "
					+ AmpCategoryValue.class.getName()
					+ " v where v.id=:id";
				qry			= dbSession.createQuery(queryString);
				qry.setParameter("id", valueId, Hibernate.LONG);
				returnCollection	= qry.list();

			} catch (Exception ex) {
				logger.error("Unable to get AmpCategoryValue: " + ex.getMessage());
				ex.printStackTrace();
			} finally {
				try {
					PersistenceManager.releaseSession(dbSession);
				} catch (Exception ex2) {
					logger.error("releaseSession() failed :" + ex2);
				}
			}
			Iterator it=returnCollection.iterator();
			if(it.hasNext())
			{
				AmpCategoryValue x=(AmpCategoryValue)it.next();
				return x;
			}
		}
		else
			logger.debug("[getAmpCategoryValueFromDb] valueId is null");
		return null;
	}
	/**
	 *
	 * @param categoryId
	 * @return AmpCategoryClass object with id=categoryId from the database
	 */
	public static AmpCategoryClass loadAmpCategoryClass(Long categoryId) throws NoCategoryClassException {
		Session dbSession			= null;
		Collection returnCollection	= null;
		try {
			dbSession			= PersistenceManager.getSession();
			String queryString;
			Query qry;

			queryString = "select c from "
				+ AmpCategoryClass.class.getName()
				+ " c where c.id=:id";
			qry			= dbSession.createQuery(queryString);
			qry.setParameter("id", categoryId, Hibernate.LONG);



			returnCollection	= qry.list();

		} catch (Exception ex) {
			logger.error("Unable to get Categories: " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			try {
				PersistenceManager.releaseSession(dbSession);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed :" + ex2);
			}
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
	 */
	public static AmpCategoryClass loadAmpCategoryClass(String name) throws NoCategoryClassException {
		Session dbSession			= null;
		Collection returnCollection	= null;
		try {
			dbSession			= PersistenceManager.getSession();
			String queryString;
			Query qry;

			queryString = "select c from "
				+ AmpCategoryClass.class.getName()
				+ " c where c.name=:name";
			qry			= dbSession.createQuery(queryString);
			qry.setParameter("name", name, Hibernate.STRING);



			returnCollection	= qry.list();

		} catch (Exception ex) {
			logger.error("Unable to get Categories: " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			try {
				PersistenceManager.releaseSession(dbSession);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed :" + ex2);
			}
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
	 * @return A collection of AmpCategoryValues witch belong to the AmpCategoryClass with name=categoryName
	 */
	public static Collection getAmpCategoryValueCollection(String categoryName, Boolean ordered) {
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
			shouldOrderAlphabetically	= ampCategoryClass.getIsOrdered();
		}
		else
			shouldOrderAlphabetically	= ordered.booleanValue();

		List ampCategoryValues			= ampCategoryClass.getPossibleValues();

		if ( !shouldOrderAlphabetically )
				return ampCategoryValues;

		TreeSet treeSet	= new TreeSet( new CategoryManagerUtil().new CategoryComparator() );
		treeSet.addAll(ampCategoryValues);


		return treeSet;
	}
	/**
	 * This is a wrapper function for getAmpCategoryValueCollectionByKey(String categoryKey, Boolean ordered). The function
	 *  is called with ordered = null.
	 * @param categoryKey
	 * @return 
	 */
	public static Collection<AmpCategoryValue> getAmpCategoryValueCollectionByKey(String categoryKey) {
		return getAmpCategoryValueCollectionByKey(categoryKey, null);
	}
	/**
	 *
	 * @param categoryKey
	 * @param ordered overrides the property in AmpCategoryClass and decides whether the values should be ordered alphabetically.
	 * If null the ordered property of the AmpCategoryClass object is checked.
	 * @return A collection of AmpCategoryValues witch belong to the AmpCategoryClass with key=categoryKey
	 */
	public static Collection<AmpCategoryValue> getAmpCategoryValueCollectionByKey(String categoryKey, Boolean ordered) {
		boolean shouldOrderAlphabetically;
		AmpCategoryClass ampCategoryClass = null;
		try {
			ampCategoryClass = CategoryManagerUtil.loadAmpCategoryClassByKey(categoryKey);
		} catch (NoCategoryClassException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (ampCategoryClass == null)
			return null;
		if (ordered == null) {
			shouldOrderAlphabetically	= ampCategoryClass.getIsOrdered();
		}
		else
			shouldOrderAlphabetically	= ordered.booleanValue();

		List<AmpCategoryValue> ampCategoryValues			= ampCategoryClass.getPossibleValues();

		if ( !shouldOrderAlphabetically )
				return ampCategoryValues;

		TreeSet<AmpCategoryValue> treeSet	= new TreeSet<AmpCategoryValue>( new CategoryManagerUtil().new CategoryComparator() );
		treeSet.addAll(ampCategoryValues);


		return treeSet;
	}
	/**
	 *
	 * @param categoryId
	 * @param ordered must be true if the AmpCategoryValues should be ordered alphabetically.
	 * If false the ordered property of the AmpCategoryClass object is checked.
	 * @return A collection of AmpCategoryValues witch belong to the AmpCategoryClass with id=categoryId
	 */
	public static Collection getAmpCategoryValueCollection(Long categoryId, Boolean ordered) {
		boolean shouldOrderAlphabetically;
		try {
			AmpCategoryClass ampCategoryClass	= CategoryManagerUtil.loadAmpCategoryClass(categoryId);
			if (ampCategoryClass == null)
				return null;
			if (ordered == null) {
				shouldOrderAlphabetically	= ampCategoryClass.getIsOrdered();
			}
			else
				shouldOrderAlphabetically	= ordered.booleanValue();
		
			List ampCategoryValues		= ampCategoryClass.getPossibleValues();
		
			if ( !shouldOrderAlphabetically )
					return ampCategoryValues;
		
			TreeSet treeSet	= new TreeSet( new CategoryManagerUtil().new CategoryComparator() );
			treeSet.addAll(ampCategoryValues);
			return treeSet;
		}
		catch(Exception E) {
			E.printStackTrace();
			return null;
		}
	}
	/**
	 * returns a string containing only ascii characters
	 * @param input The string that needs to be filtered
	 * @return
	 */
	public static String asciiStringFilter (String input) {
		byte [] bytearray		= input.getBytes(); 
		
		CharsetDecoder decoder	= Charset.forName("US-ASCII").newDecoder();
		decoder.replaceWith("_");
		decoder.onMalformedInput(CodingErrorAction.REPLACE);
		decoder.onUnmappableCharacter(CodingErrorAction.REPLACE);
		
		try {
			CharBuffer buffer 		= decoder.decode( ByteBuffer.wrap(bytearray) );
			return buffer.toString();
		} catch (CharacterCodingException e) {
			e.printStackTrace();
			return null;
		}
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
		String filteredValue			= asciiStringFilter( value );
		String translationKey			= "cm:category_" + classKeyName +
										"_" + filteredValue;
		return translationKey.toLowerCase();
	}
	
	/**
	 *
	 * @param key The key of the AmpCategoryClass object. (A key can be attributed when creating a new category)
	 * @return The AmpCategoryClass object with the specified key. If not found returns null.
	 */
	public static AmpCategoryClass loadAmpCategoryClassByKey(String key) throws NoCategoryClassException
	{
		Session dbSession			= null;
		Collection col=new ArrayList();
		try {
			dbSession						= PersistenceManager.getSession();
			//AmpCategoryClass dbCategory		= new AmpCategoryClass();
				String queryString	= "select c from " + AmpCategoryClass.class.getName() + " c where c.keyName=:key";
				Query query			= dbSession.createQuery(queryString);
				query.setParameter("key", key, Hibernate.STRING);
				 col		= query.list();

		} catch (Exception ex) {
			logger.error("Error retrieving AmpCategoryClass with key '" + key + "': " + ex);
		} finally {
			try {
				PersistenceManager.releaseSession(dbSession);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed :" + ex2);
			}
		}

		if(!col.isEmpty())
		{
			Iterator it=col.iterator();

			AmpCategoryClass x=(AmpCategoryClass) it.next();
			return x;
		}
		else{
			throw new NoCategoryClassException("No AmpCategoryClass found with key '" + key + "'");
		}
	}
	/**
	 *
	 * @param key The AmpCategoryClass key
	 * @param ordered must be true if the AmpCategoryValues should be ordered alphabetically.
	 * If false the ordered property of the AmpCategoryClass object is checked.
	 * @return A collection of AmpCategoryValues witch belong to the AmpCategoryClass with the specified key
	 */
	public static Collection getAmpCategoryByKey(String key, Boolean ordered) {
		boolean shouldOrderAlphabetically;
		AmpCategoryClass ampCategoryClass = null;
		try {
			ampCategoryClass = CategoryManagerUtil.loadAmpCategoryClassByKey(key);
		} catch (NoCategoryClassException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (ampCategoryClass == null)
			return null;
		if (ordered == null) {
			shouldOrderAlphabetically	= ampCategoryClass.getIsOrdered();
		}
		else
			shouldOrderAlphabetically	= ordered.booleanValue();

		List ampCategoryValues		= ampCategoryClass.getPossibleValues();

		if ( !shouldOrderAlphabetically )
				return ampCategoryValues;

		TreeSet treeSet	= new TreeSet( new CategoryManagerUtil().new CategoryComparator() );
		treeSet.addAll(ampCategoryValues);


		return treeSet;
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
	
	
	public static boolean isCategoryKeyInUse(String key) {
		try {
			CategoryManagerUtil.loadAmpCategoryClassByKey(key);
			return true;
		} catch (NoCategoryClassException e) {
			// TODO Auto-generated catch block
			logger.info("Category key '" + key + "' was not found in database.");
			return false;
		}
	}
	/**
	 *
	 * @author Alex Gartner
	 * A comparator for AmpCategoryValue objects which is needed when ordering them alphabetically
	 */
	public class CategoryComparator implements Comparator {
		public int compare(Object value1, Object value2) {
			AmpCategoryValue catValue1	= (AmpCategoryValue) value1;
			AmpCategoryValue catValue2	= (AmpCategoryValue) value2;
			return catValue1.getValue().compareTo( catValue2.getValue() );

		}
		public boolean equals(Object value1, Object value2) {
			if ( this.compare(value1, value2) == 0 )
					return true;
			return false;
		}
	}

}


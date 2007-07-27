package org.digijava.module.aim.helper;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import org.apache.log4j.Logger;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpCategoryClass;
import org.digijava.module.aim.dbentity.AmpCategoryValue;

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
	 * @param lang
	 * @return The translated category value
	 */
	public static String translateAmpCategoryValue(AmpCategoryValue ampCategoryValue, HttpServletRequest request ,String lang) {
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
			qry.setParameter("translationKey", key ,Hibernate.STRING);
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
	public static AmpCategoryClass loadAmpCategoryClass(Long categoryId) {
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
            return null;
        }
	}
	/**
	 *
	 * @param categoryId
	 * @return AmpCategoryClass object with name=categoryName from the database
	 */
	public static AmpCategoryClass loadAmpCategoryClass(String name) {
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
            return null;
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
		AmpCategoryClass ampCategoryClass	= CategoryManagerUtil.loadAmpCategoryClass(categoryName);
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
	 *
	 * @param categoryKey
	 * @param ordered overrides the property in AmpCategoryClass and decides whether the values should be ordered alphabetically.
	 * If null the ordered property of the AmpCategoryClass object is checked.
	 * @return A collection of AmpCategoryValues witch belong to the AmpCategoryClass with key=categoryKey
	 */
	public static Collection getAmpCategoryValueCollectionByKey(String categoryKey, Boolean ordered) {
		boolean shouldOrderAlphabetically;
		AmpCategoryClass ampCategoryClass	= CategoryManagerUtil.loadAmpCategoryClassByKey(categoryKey);
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
	 *
	 * @param categoryId
	 * @param ordered must be true if the AmpCategoryValues should be ordered alphabetically.
	 * If false the ordered property of the AmpCategoryClass object is checked.
	 * @return A collection of AmpCategoryValues witch belong to the AmpCategoryClass with id=categoryId
	 */
	public static Collection getAmpCategoryValueCollection(Long categoryId, Boolean ordered) {
		boolean shouldOrderAlphabetically;
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
	/**
	 *
	 * @param ampCategoryValue
	 * @return the translation key for the ampCategoryValue
	 */
	public static String getTranslationKeyForCategoryValue(AmpCategoryValue ampCategoryValue) {
		String translationKey			= "aim:category" + ampCategoryValue.getAmpCategoryClass().getId() +
										"_" + ampCategoryValue.getAmpCategoryClass().getName() + "_" +
										ampCategoryValue.getValue();
		return translationKey;
	}
	/**
	 *
	 * @param key The key of the AmpCategoryClass object. (A key can be attributed when creating a new category)
	 * @return The AmpCategoryClass object with the specified key. If not found returns null.
	 */
	public static AmpCategoryClass loadAmpCategoryClassByKey(String key)
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
			logger.error("Unable to get the id from AmpCategoryClass: " + ex);
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
		else return null;
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
		AmpCategoryClass ampCategoryClass	= CategoryManagerUtil.loadAmpCategoryClassByKey(key);
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

